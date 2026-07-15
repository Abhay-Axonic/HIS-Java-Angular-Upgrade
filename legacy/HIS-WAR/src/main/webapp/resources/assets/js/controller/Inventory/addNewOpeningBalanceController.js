(function () {
  "use strict";

  function addNewOpeningBalanceController($scope, $rootScope, $http, $cookies, CONSTANTS,BillingGenericService,GenericServiceFileUpload, GenericService,PaginationService, $state, openingBalanceService, CurrencyService, productService, growl,stopTransactionService,$filter) {
    var self = this;
    var uomUnitIdSet;
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
        currentUomList: [],
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
        storeFlag: false,
        storageDetailsList: [],
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
      self.model.CallUOM = CallUOM;
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
      // // self.model.getproductCategory = getproductCategory;
      // self.model.getAllItemDetails = getAllItemDetails;
      self.model.GetProductCategoryList = GetProductCategoryList;
      self.model.uomInfoOnClick = uomInfoOnClick;
      self.model.uomModalOnClick = uomModalOnClick;
      self.model.selectedUOMList = [];
      self.model.selectedUOMUnit = [];
      self.model.selectedItemIndex = 0;
      self.model.NavigteToOpeningBalance = NavigteToOpeningBalance;
      self.model.fillItemList = fillItemList;
      self.model.SaveOpeningBalance = SaveOpeningBalance;
      self.model.uomSelectEvent = uomSelectEvent;
      self.model.modalUOMSelectEvent = modalUOMSelectEvent;
      self.model.isItemChecked = false;
      self.model.ItemDetailList = [];
      self.model.searchNewItemList = [];
      self.model.validateSave = validateSave;
      self.model.setSelectedDate = setSelectedDate;
      self.model.ValidateConsumableAmount = ValidateConsumableAmount;
      self.model.selectonchange = selectonchange;
      self.model.ValidateQuantity = ValidateQuantity;
      self.model.onUomChange = onUomChange;
      self.model.onStoreChange = onStoreChange;
      self.model.confirmClear = confirmClear;
      self.model.itemRemovePopup = itemRemovePopup;
      self.model.CalculateCOP = CalculateCOP;
      self.model.SetOldValueStore = SetOldValueStore;
      self.model.ChangeFromStore = ChangeFromStore;
      self.model.ConfirmStorePopup = ConfirmStorePopup;

      self.model.ConfirmRemove = ConfirmRemove;
      self.model.RemoveItem = RemoveItem;
      self.model.ValidateCOP = ValidateCOP;
      self.model.ValidateBatch = ValidateBatch;
      self.model.ValidateUom = ValidateUom;
      self.model.ValidateExpiry = ValidateExpiry;
      self.model.ValidateMRP = ValidateMRP;
      // self.model.ValidateMaxOrderQuantity = ValidateMaxOrderQuantity;
      self.model.ValidateMarkup = ValidateMarkup;
      self.model.ValidateMrpUom =ValidateMrpUom;
      self.model.SelectTax = SelectTax;
      self.model.DetailsClick = DetailsClick;
      self.model.ChangeRack = ChangeRack;
      self.model.ChangeSelf = ChangeSelf;
      self.model.AddRack = AddRack;
      self.model.lessRack = lessRack;
      self.model.SaveStorageDetails = SaveStorageDetails;
      self.model.ViewStorageDetails = ViewStorageDetails;
      self.model.OpenPopup = OpenPopup;
      self.model.uploadDoccument = uploadDoccument;

      GetUOMUnitList();
      PopulateDropdown();
    }

    var uomSelectEvent = function (datum) {
    	        datum.selectedUom = $filter('filter')(datum.uom, {
                        ipUom: true
                    }, true)[0];
      self.model.onUomChange(datum);
      datum.uom.forEach(function (uom) {
        if (uom.uomTypeId == datum.uomTypeId) datum.selectedUom = uom;
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
        self.model.rackList = item.racks;
        self.model.storageDetailsList = [{ rackId: '', shelfId: '', remark: '' }]
      });
    }

    var GetUOMUnitList = function () {
      var URI = CONSTANTS.GLOBAL.UOM_UNITS + "/"+ $scope.organizationId ;

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

    var CalculateCOP = function (item) {
      if(item.newmaxOrderQuantity >= item.quantity){
        if (parseInt(item.quantity) > 0 && parseFloat(item.cop) > 0) {
          item.copAmount = (parseInt(item.quantity) * parseFloat(item.cop)).toFixed(2);
          item.taxAmount = ((item.copAmount * item.selectedTax.taxPercent)/100).toFixed(2);
          // item.lcop = (((item.cop * item.quantity) + ((item.copAmount * item.selectedTax.taxPercent)/100)) / item.receivedQtyCalculation).toFixed(2);
          item.lcop = ((parseFloat(item.copAmount) + parseFloat(item.taxAmount)) / item.receivedQtyCalculation).toFixed(2);
        } else {
          item.copAmount = 0;
          item.lcop = 0;
        }
      }
      else{
        growl.error('Enter quantity less than Pending Qty');
        item.isQuantityValid = true;
      }
    
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
        if( self.model.itemName.length >= 3){
          // var URI = CONSTANTS.GLOBAL.ITEM_API +"unit/"+$scope.unitId;
          var URI = CONSTANTS.GLOBAL.STORE_WISE_ITEM_API;
          
          const data = {
            "assetTypeId": 1,
            "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
            "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
            "storeId": self.model.store.id,
            "itemCode": self.model.itemCode,
            "itemName": self.model.itemName,
            "unitId" : $scope.unitId
          }

          // self.model.searchNewItemList = [];
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
          var URI = CONSTANTS.GLOBAL.STORE_WISE_ITEM_API;
          
          const data = {
            "assetTypeId": 1,
            "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
            "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
            "storeId": self.model.store.id,
            "itemCode": self.model.itemCode,
            "itemName": self.model.itemName,
            "unitId" : $scope.unitId
          }

          // self.model.searchNewItemList = [];
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
      // var opbId = openingBalanceService.getOpeningBalanceId();

      var URI = CONSTANTS.INVENTORY.STOCK.OPENING_BALANCE.DETAILS_API + $scope.unitId + '/'+$scope.organizationId + '/' + opbId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.opbId = response.data.id;

          self.model.store = self.model.stores.find(function (item) {
            return response.data.storeId == item.id;
            //self.model.store.id = response.data.storeId;
          });

          self.model.itemDetails = [];
          angular.forEach(response.data.items, function (item, key) {
            item.expiryDate = new Date(item.expiryDate);
            item.mrp = item.mrp.toFixed(2);
            item.cop = item.cop.toFixed(2);
            item.markup = item.markup.toFixed(2);
            item.selectedTax = item.taxList.find(function (val) {
              return item.taxId == val.taxId;
            });
            if(!item.selectedTax) {
              item.taxId = 11;
              item.selectedTax = item.taxList.find(function (val) {
                return item.taxId == val.taxId;
              });
            } 
            item.selectedTax.taxPercent = item.selectedTax.taxPercent.toFixed(2);
            item.mrpUOM = item.uom;
            item.selectedMrpUom = item.mrpUOM.find(function (mrp) {
              return item.mrpUomTypeId == mrp.uomTypeId;
            });
            angular.forEach(item.uom, function(u){
              if(u.uomTypeId == item.mrpUomTypeId){
                  item.MrpuomUnit = u.uomUnit;
              } 
            });
            $scope.listData = [];
            $scope.listData.push(item);
            if ($scope.listData)
            self.model.fillItemList($scope.listData);
            if(item.isThreeUOMApplicable == false){
              item.uomListdata = item.uom;
              item.uom = [];
              item.mrpUOM = [];
              angular.forEach(item.uomListdata, function(data){
                if(data.uomType != 'Stock'){
                  item.uom.push(data);
                  item.mrpUOM.push(data);
                }
              });
            }
            onUomChange(item);
           /* start code for resolved HIS-10479 Opening Balance : In case of Without Save UOM Popup Showing "Something Went Wrong" */
            item.isBatchRequired = item.isBatchRequired ? item.isBatchRequired : item.isBatchReq;
           /* end code for resolved HIS-10479 Opening Balance : In case of Without Save UOM Popup Showing "Something Went Wrong" */
            
          });
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }
    $scope.saveBtnDisabled=false;
    var SaveOpeningBalance = function () {
    	$scope.saveBtnDisabled=true;
      if (validateSave()) {
        if (!self.model.opbId) {
          SaveNewItemRequest();
        } else {
          UpdateOpeningBalance();
        }
      }
    }
    var UpdateOpeningBalance = function () {
    	$scope.saveBtnDisabled=true;
      if($scope.validaterate()){
        var opb = self.model;
        $scope.opbIdForDoccumentUpload = opb.id;
      var URI = CONSTANTS.INVENTORY.STOCK.OPENING_BALANCE.UPDATE_API + opb.opbId;

      var data = {

        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId,
        "storeId":$scope.storeId

      };

      angular.forEach(opb.ItemDetailList, function (item, key) {
        self.model.storageDetailsData = [];
        if(item.storageDetails != undefined){
           item.storageDetails.forEach(function (sDetails){
            var obj = {
              rackId : '',
              remark : '',
              shelfId : '',
            }
            obj.rackId = sDetails.rackId;
            obj.remark = sDetails.remark;
            obj.shelfId = sDetails.shelfId;
            self.model.storageDetailsData.push(obj);
          });
        }
        var uom = [];

        if(item.isThreeUOMApplicable == false){
          item.uomListdata.forEach(function (units) {
            var uomUnit = item.uomListdata.find(function (uomUnit) {
              return uomUnit.uomTypeId == units.uomTypeId;
            });
  
            var unit = angular.copy(units);
            unit.id = uomUnit.id;
            uom.push(unit);
          });
        }
        else{
          item.defaultUOM.forEach(function (units) {
            var uomUnit = item.uom.find(function (uomUnit) {
              return uomUnit.uomTypeId == units.uomTypeId;
            });
  
            var unit = angular.copy(units);
            unit.id = uomUnit.id;
            uom.push(unit);
          });
        }

        self.model.mrpUomTypeId ='';
        if( item.selectedMrpUom != undefined){
          self.model.mrpUomTypeId = item.selectedMrpUom.uomTypeId;
        }
        else{
          self.model.mrpUomTypeId = 3;
        }

        self.model.uomTypeId ='';
        self.model.uomUnitId =''; 
        if( item.selectedUom != undefined){
          self.model.uomTypeId = item.selectedUom.uomTypeId;
          self.model.uomUnitId = item.selectedUom.uomUnitId;
        }
        else{
          self.model.uomTypeId = 3;
          self.model.uomUnitId = 57;
        }
        item.expiryDate=moment(item.expiryDate).format("YYYY-MM-DD");
        item.expiryDate = item.expiryDate.split("-");
        var y = item.expiryDate[0];
        var m = item.expiryDate[1];
        var d = item.expiryDate[2];

        m = parseInt(m);
        if(m < 10){
            m = "0"+ m;
        }

        item.expiryDate = y + "-" + m + "-" + d;

        var storeItem = {
          "itemId": item.itemId,
          "isBatchReq":item.isBatchRequired,
          "batchNo": item.batchNo ? item.batchNo : null,
          "expiryDate": item.expiryDate ? item.expiryDate : null,
          "quantity": item.quantity,
          "cop": item.cop,
          "lcop": item.lcop,
          "mrpUomTypeId": self.model.mrpUomTypeId,
          "mrp": item.mrp,
          "copAmount": item.copAmount,
          "markup": item.markup,
          "taxId": item.selectedTax.taxId,
          "taxPercent": item.selectedTax.taxPercent,
          "taxAmount": item.taxAmount,
          "remark": item.remark,
          "statusId": 1,
          "uomTypeId": self.model.uomTypeId,
          "uomUnitId": self.model.uomUnitId,
          "uom": uom,
          "storageDetails": self.model.storageDetailsData ? self.model.storageDetailsData : []
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
          if($scope.encounterDetailsList.length == 1){
        	  angular.forEach($scope.encounterDetailsList,function(value,key){
        		  if (!angular.equals(value.file, {}) && value.file != undefined) {
        		      $scope.uploadPatientImage();
        		  }
        	  });
          }
          if($scope.encounterDetailsList.length > 1){
            $scope.uploadPatientImage();
          }
          growl.success(response.data.message);
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error('Something Went Wrong');
        });  
      }
    }

    var AddButtonClick = function (index) {
      var data = self.model.openingBalanceData[index];
      self.model.currentUomList = [];
      self.model.selectAllRows = false;
      if (data.uomList) {
        self.model.currentUomList = [data.uomList[0]];
      }
    }


    var FromOpeningBalance = function () {
      var id = productService.getProductId();
      if (id !== 0) {
        self.model.storeFlag = true;
        var URI = CONSTANTS.INVENTORY.STOCK.OPENING_BALANCE.DETAILS_API + $scope.unitId + '/'+$scope.organizationId + '/' + id;
        var data = {};
        GenericService.serviceAction("GET", URI, data).then(
          function (response) {
            // self.model.storeData = productService.getStoreId();
            self.model.openingBalanceData = response.data;
          });
      } else {
        self.model.storeFlag = false;
      }
    }

    var CallUOM = function (idx) {
      var data = self.model.openingBalanceData[idx];
      if (data.uomUnitId == "1") {
        data.selectedUom = data.uomList[0].puomUnitDesc;
        uomUnitIdSet = data.uomList[0].pUomId;
      } else if (data.uomUnitId == "2") {
        data.selectedUom = data.uomList[0].suomUnitDesc;
        uomUnitIdSet = data.uomList[0].sUomId;
      } else if (data.uomUnitId == "3") {
        data.selectedUom = data.uomList[0].duomUnitDesc;
        uomUnitIdSet = data.uomList[0].dUomId;
      }
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

        if (!ValidateUom(ItemDetail)) {
          isValid = false;
        }

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
        if(!ValidateMrpUom(ItemDetail)){
          isValid  = false;
        }
      }
      if(!isValid)
      {
      $scope.saveBtnDisabled=false;
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

    var onUomChange = function (item) {
      var conversionPurchase;
      var conversionStock;
      var conversionDespense;
      
      if(item.quantity == undefined || item.quantity == null || item.quantity == ''){
        item.quantity = 0;
      }
      if(item.isThreeUOMApplicable == false){
        item.uomListdata.forEach(function (uom) {
          if (uom.uomTypeId == 1) {
            conversionPurchase = uom.conversion;
          } else if (uom.uomTypeId == 2) {
            conversionStock = uom.conversion
          } else if (uom.uomTypeId == 3) {
            conversionDespense = uom.conversion
          }
        });
    	  if(item.selectedUom){
          if (!!item.selectedUom && item.selectedUom.uomTypeId == 1) {
            item.leaseQuantity = Math.round((((item.maxOrderQuantity / conversionPurchase) /conversionStock) / conversionDespense));
            item.newmaxOrderQuantity = parseInt((item.maxOrderQuantity / (conversionPurchase *conversionStock * conversionDespense)));
            item.newstockAvailable = parseInt((item.stockAvailable / (conversionPurchase * conversionStock * conversionDespense)));
            item.receivedQtyCon=(item.quantity +"*"+ conversionPurchase);
            item.receivedQtyCalculation = (item.quantity * conversionPurchase);
            item.uomUnitData = item.uomListdata[2].uomUnit;
          } else if(!!item.selectedUom && item.selectedUom.uomTypeId == 2){
            item.leaseQuantity = Math.round(((item.maxOrderQuantity / conversionStock)  / conversionDespense));
            item.newmaxOrderQuantity = parseInt((item.maxOrderQuantity / (conversionStock * conversionDespense)));
            item.newstockAvailable = parseInt((item.stockAvailable / (conversionStock * conversionDespense)));
          } else if (!!item.selectedUom && item.selectedUom.uomTypeId == 3) {
            item.leaseQuantity = Math.round(item.maxOrderQuantity / conversionDespense);
            item.newmaxOrderQuantity = parseInt((item.maxOrderQuantity / (conversionDespense)));
            item.newstockAvailable = parseInt((item.stockAvailable / (conversionDespense)));
            item.receivedQtyCon=(item.quantity);
            item.receivedQtyCalculation = (item.quantity);
            item.uomUnitData = item.uomListdata[2].uomUnit;
          }
        }
        else{
          if (item.uomTypeId == 1) {
            item.leaseQuantity = Math.round((((item.maxOrderQuantity / conversionPurchase) /conversionStock) / conversionDespense));
            item.newmaxOrderQuantity = parseInt((item.maxOrderQuantity / (conversionPurchase * conversionStock * conversionDespense)));
            item.newstockAvailable = parseInt((item.stockAvailable / (conversionPurchase * conversionStock * conversionDespense)));
            item.receivedQtyCon=(item.quantity +"*"+ conversionPurchase);
            item.receivedQtyCalculation=(item.quantity * conversionPurchase);
            item.uomUnitData = item.uomListdata[2].uomUnit;
          } else if(!!item.selectedUom && item.selectedUom.uomTypeId == 2){
              item.leaseQuantity = Math.round(((item.maxOrderQuantity / conversionStock)  / conversionDespense));
                item.newmaxOrderQuantity = parseInt((item.maxOrderQuantity / (conversionStock * conversionDespense)));
                item.newstockAvailable = parseInt((item.stockAvailable / (conversionStock * conversionDespense)));
          }else if (item.uomTypeId == 3) {
            item.leaseQuantity = Math.round(item.maxOrderQuantity / conversionDespense);
            item.newmaxOrderQuantity = parseInt((item.maxOrderQuantity / (conversionDespense)));
            item.newstockAvailable = parseInt((item.stockAvailable / (conversionDespense)));
            item.receivedQtyCon=(item.quantity);
            item.receivedQtyCalculation=(item.quantity);
            item.uomUnitData = item.uomListdata[2].uomUnit;
          }
        }
      }
      else{
        item.defaultUOM.forEach(function (uom) {
          if (uom.uomTypeId == 1) {
            conversionPurchase = uom.conversion;
          } else if (uom.uomTypeId == 2) {
            conversionStock = uom.conversion
          } else if (uom.uomTypeId == 3) {
            conversionDespense = uom.conversion
          }
          item.uom.forEach(function(u){
            if(u.uomUnitId == uom.uomUnitId){
              uom.uomUnit = u.uomUnit;
            }
          })
        });
    	  if(item.selectedUom){
          if (!!item.selectedUom && item.selectedUom.uomTypeId == 1) {
            item.leaseQuantity = Math.round((((item.maxOrderQuantity / conversionPurchase) / conversionStock) / conversionDespense));
            item.newmaxOrderQuantity = parseInt((item.maxOrderQuantity / (conversionPurchase * conversionStock * conversionDespense)));
            item.newstockAvailable = parseInt((item.stockAvailable / (conversionPurchase * conversionStock * conversionDespense)));
            item.receivedQtyCon=(item.quantity +"*"+ conversionPurchase +"*"+ conversionStock);
            item.receivedQtyCalculation=(item.quantity * conversionPurchase * conversionStock);
            item.uomUnitData = item.defaultUOM[2].uomUnit;
          } else if (!!item.selectedUom && item.selectedUom.uomTypeId == 2) {
            item.leaseQuantity = Math.round((item.maxOrderQuantity / conversionStock) / conversionDespense);
            item.newmaxOrderQuantity = parseInt((item.maxOrderQuantity / (conversionStock * conversionDespense)));
            item.newstockAvailable = parseInt((item.stockAvailable / (conversionStock * conversionDespense)));
            item.receivedQtyCon=(item.quantity +"*"+ conversionStock);
            item.receivedQtyCalculation=(item.quantity * conversionStock);
            item.uomUnitData = item.defaultUOM[2].uomUnit;
          } else if (!!item.selectedUom && item.selectedUom.uomTypeId == 3) {
            item.leaseQuantity = Math.round(item.maxOrderQuantity / conversionDespense);
            item.newmaxOrderQuantity = parseInt((item.maxOrderQuantity / (conversionDespense)));
            item.newstockAvailable = parseInt((item.stockAvailable / (conversionDespense)));
            item.receivedQtyCon=(item.quantity);
            item.receivedQtyCalculation=(item.quantity);
            item.uomUnitData = item.defaultUOM[2].uomUnit;
          }
        }
        else{
          if (item.uomTypeId == 1) {
            item.leaseQuantity = Math.round((((item.maxOrderQuantity / conversionPurchase) / conversionStock) / conversionDespense));
            item.newmaxOrderQuantity = parseInt((item.maxOrderQuantity / (conversionPurchase * conversionStock * conversionDespense)));
            item.newstockAvailable = parseInt((item.stockAvailable / (conversionPurchase * conversionStock * conversionDespense)));
            item.receivedQtyCon=(item.quantity +"*"+ conversionPurchase +"*"+ conversionStock);
            item.receivedQtyCalculation=(item.quantity * conversionPurchase * conversionStock);
            item.uomUnitData = item.defaultUOM[2].uomUnit;
          } else if (item.uomTypeId == 2) {
            item.leaseQuantity = Math.round((item.maxOrderQuantity / conversionStock) / conversionDespense);
            item.newmaxOrderQuantity = parseInt((item.maxOrderQuantity / (conversionStock * conversionDespense)));
            item.newstockAvailable = parseInt((item.stockAvailable / (conversionStock * conversionDespense)));
            item.receivedQtyCon=(item.quantity +"*"+ conversionStock);
            item.receivedQtyCalculation=(item.quantity * conversionStock);
            item.uomUnitData = item.defaultUOM[2].uomUnit;
          } else if (item.uomTypeId == 3) {
            item.leaseQuantity = Math.round(item.maxOrderQuantity / conversionDespense);
            item.newmaxOrderQuantity = parseInt((item.maxOrderQuantity / (conversionDespense)));
            item.newstockAvailable = parseInt((item.stockAvailable / (conversionDespense)));
            item.receivedQtyCon=(item.quantity);
            item.receivedQtyCalculation=(item.quantity);
            item.uomUnitData = item.defaultUOM[2].uomUnit;
          }
        }
      }  
    }

    var ValidateUom = function (item) {
      var isValid = true;
      if(item.isBatchRequired == true){
         if (!item.selectedUom) {
          item.isUomValid = true;
          isValid = false;
          growl.error("Please Select UOM");
        }
      }
      return isValid;
    }

    var ValidateMrpUom = function (item) {
        angular.forEach(item.uom, function(u){
            if(item.selectedMrpUom){
                if(u.uomTypeId == item.selectedMrpUom.uomTypeId){
                    item.selectedMrpUom.uomUnitId = u.uomUnitId;
                    item.selectedMrpUom.uomUnit = u.uomUnit;
                    item.MrpuomUnit = u.uomUnit;
                }
            }
        });
      var isValid = true;
      if(item.isBatchRequired == true){
        if (!item.selectedMrpUom) {
          item.isMrpUomValid = true;
          isValid = false;
          growl.error("Please Select MRP UOM");
        }
      }
      return isValid;
    }
    var ValidateBatch = function (item) {
      var isValid = true;
      // if(item.isBatchRequired == true){
        if (!item.batchNo) {
          item.isBatchNoValid = true;
          isValid = false;
          growl.error("Please Enter Batch No.");
        }
      // }
      return isValid;
    }

    $scope.expiryDateVal =  function(event, item){
    	item.isExpiryDateValid = true;
    	growl.error("Invalid Expiry Date");
    }
    
    var ValidateExpiry = function (item) {
      var isValid = true;
      if(item.isBatchRequired == true){
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
      }
      
      return isValid;
    }

    var ValidateQuantity = function (item) {
      var isValid = true;

      if (isNaN(parseFloat(item.quantity)) || item.isQuantityValid) {
        item.isQuantityValid = true;
        item.leaseQuantity = 0;
        isValid = false;
        growl.error("Please Enter Valid Quantity");
      } else if (parseInt(item.quantity) == 0) {
        item.isQuantityValid = true;
        item.leaseQuantity = 0;
        isValid = false;
        growl.error('Quantity cannot be zero');
      } else {
        item.quantity = parseInt(item.quantity);
        isValid = true;
      }

      return isValid;
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

    var CalculateLeaseQuantity = function (item) {
      var conversionPurchase;
      var conversionStock;
      var conversionDespense;

      item.defaultUOM.forEach(function (uom) {
        if (uom.uomTypeId == 1) {
          conversionPurchase = uom.conversion;
        } else if (uom.uomTypeId == 2) {
          conversionStock = uom.conversion
        } else if (uom.uomTypeId == 3) {
          conversionDespense = uom.conversion
        }
      });

      if (!!item.selectedUom && item.selectedUom.uomTypeId == 1) {
        item.leaseQuantity = parseInt(item.quantity) * conversionPurchase * conversionStock * conversionDespense;
      } else if (!!item.selectedUom && item.selectedUom.uomTypeId == 2) {
        item.leaseQuantity = parseInt(item.quantity) * conversionStock * conversionDespense;
      } else if (!!item.selectedUom && item.selectedUom.uomTypeId == 3) {
        item.leaseQuantity = parseInt(item.quantity) * conversionDespense;
      }
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
      data.lcop = 0;
      data.markup = '';
      data.leaseQuantity = 0;
      data.isBatchNoValid = false;
      data.isCopValid = false;
      data.isExpiryDateValid = false;
      data.isMrpValid = false;
      data.isQuantityValid = false;
      data.isUomValid = false;

      self.model.storageDetailsList.forEach(function(data){
        data.rackName = [],
        data.selfName = [],
        data.remark = "",
        data.rackId = "",
        data.shelfId = ""
      });

      delete data.id;
      delete data.selectedUom;
      delete data.uomType;
      delete data.uomTypeId;

      if(data.isBatchRequired == false){
        data.batchNo = 'NA';
      }
      self.model.ItemDetailList.push(data);
    }

    var AddItems = function () {
    	
    	ActualAddItems();
    	
    	/*
    	var check = false;
        angular.forEach(self.model.ItemDetailList,function(value,key){
        	
        	  angular.forEach( self.model.items,function(value1,key1){
                  if(value1.isSelected == true && value1.batchId == value.selectedBatch.batchId) {
                      check = true;
                  }
                  });
        	
            });
        
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
      var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId;
      var data = {
        "itemIds": self.model.SelectedItemsIds,
        "storeId": self.model.store.id
      };

      $rootScope.startLoaderPopup();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          angular.forEach(response.data, function (item){
            item.selectedTax = item.taxList.find(function (val) {
              return item.taxId == val.taxId;
            });
            item.mrpUOM = item.uom;
            if(item.isThreeUOMApplicable == false){
                item.uomListdata = item.uom;
                item.uom = [];
                item.mrpUOM = [];
                angular.forEach(item.uomListdata, function(data){
                  if(data.uomType != 'Stock'){
                    item.uom.push(data);
                    item.mrpUOM.push(data);
                  }
                });
              }
            if(!item.selectedTax) {
              item.taxId = 11;
              item.selectedTax = item.taxList.find(function (val) {
                return item.taxId == val.taxId;
              });
            }
            if(item.isBatchRequired == false){
              item.batchNo = 'NA';
            }
          })
          self.model.fillItemList(response.data);
          // self.model.ItemDetailList = self.model.ItemDetailList.concat(response.data);
          self.model.SelectedItemsIds = [];
          self.model.items = [];

          $rootScope.stopLoaderPopup();
          // getAllItemDetails();
          //growl.success(response.data.message);

        },
        function (err) {
          $rootScope.stopLoaderPopup();
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
    	$scope.saveBtnDisabled=true;
      if($scope.validaterate()){
        var store = self.model;
      var URI = CONSTANTS.INVENTORY.STOCK.OPENING_BALANCE.SAVE_API;

      var data = {
        "storeId": store.store.id,
        "statusId": 1,
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId

      };

      angular.forEach(store.ItemDetailList, function (item, key) {
        self.model.storageDetailsData = [];
        if(item.storageDetails != undefined){
           item.storageDetails.forEach(function (sDetails){
            var obj = {
              rackId : '',
              remark : '',
              shelfId : '',
            }
            obj.rackId = sDetails.rackId;
            obj.remark = sDetails.remark;
            obj.shelfId = sDetails.shelfId;
            self.model.storageDetailsData.push(obj);
          });
        }
        self.model.mrpUomTypeId ='';
        if( item.selectedMrpUom != undefined){
          self.model.mrpUomTypeId = item.selectedMrpUom.uomTypeId;
        }
        else{
          self.model.mrpUomTypeId = 3;
        }

        self.model.uomTypeId ='';
        self.model.uomUnitId =''; 
        if( item.selectedUom != undefined){
          self.model.uomTypeId = item.selectedUom.uomTypeId;
          self.model.uomUnitId = item.selectedUom.uomUnitId;
        }
        else{
          self.model.uomTypeId = 3;
          self.model.uomUnitId = 57;
        }
        if(item.expiry!=undefined && item.expiry!=null)
        	{
        item.expiry = item.expiry.split("-");
        var y = item.expiry[0];
        var m = item.expiry[1];
        var d = item.expiry[2];
        var m = parseInt(m);

        if(m < 10){
          m = "0" + m;
        }

        item.expiry = y + "-" + m + "-" + d;
        	}
        var storeItem = {
          "itemId": item.itemId,
          "batchNo": item.batchNo ? item.batchNo : null,
          "isBatchReq":item.isBatchRequired,
          "expiryDate": item.expiry ? item.expiry : null,
          "quantity": item.quantity,
          "cop": item.cop,
          "mrpUomTypeId":self.model.mrpUomTypeId,
          "mrp": item.mrp,
          "copAmount": item.copAmount,
          "markup": item.markup,
          "taxId": item.selectedTax.taxId,
          "taxPercent": item.selectedTax.taxPercent,
          "taxAmount": item.taxAmount,
          "remark": item.remark,
          "statusId": 1,
          "uomTypeId": self.model.uomTypeId,
          "uomUnitId": self.model.uomUnitId,
          "uom": [],
          "lcop": item.lcop,
          "storageDetails": self.model.storageDetailsData ? self.model.storageDetailsData : []
        }
        if(item.isThreeUOMApplicable == false){
          storeItem.uom = item.uomListdata;
        }
        else{
          storeItem.uom = item.defaultUOM;
        }
        data.items.push(storeItem);

      });

      /*var uomObj = angular.forEach(storeItem, function (item, key) {
        var uom = {
          "uomTypeId": item.uomTypeId,
          "uomUnitId": item.uomUnitId,
          "conversion": item.conversion
        }
        // storeItem.push(uom);
      });*/
      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          // ClearRequests();
          $scope.opbIdForDoccumentUpload = response.data.id;
          $rootScope.stopLoader();
          if($scope.encounterDetailsList.length == 1){
        	  angular.forEach($scope.encounterDetailsList,function(value,key){
        		  if (!angular.equals(value.file, {}) && value.file != undefined) {
        		      $scope.uploadPatientImage();
        		  }
        	  });
          }
          if($scope.encounterDetailsList.length > 1){
            $scope.uploadPatientImage();
          }
          NavigteToOpeningBalance();
          growl.success(response.data.message);
        },



        function (err) {
          $rootScope.stopLoader();
          growl.error('Something Went Wrong');
        });
      }
    }

    var NavigteToOpeningBalance = function () {
      $state.go('openingBalance');
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

    var SelectTax = function(tax, item) {
      if (!!tax) {
        item.selectedTaxPercent = tax.taxPercent;
        item.taxAmount=((item.copAmount * tax.taxPercent )/ 100).toFixed(2); 
      }
      var copAmount = parseFloat(item.copAmount);
      var quantity = parseFloat(item.quantity);
      item.lcop = ((copAmount + parseFloat(item.taxAmount)) / item.receivedQtyCalculation).toFixed(2);
    }

    var DetailsClick = function (item, idx) {
      self.model.selectItemId = item.itemId;
      self.model.storageIndex = idx;
      if(item.storageDetails){
        self.model.storageDetailsList = self.model.ItemDetailList[self.model.storageIndex].storageDetails;
        console.log("self.model.storagedetailslist", self.model.storageDetailsList);
        self.model.storageDetailsList.forEach(function (data){
        	$scope.rackName={
        			rackId:"",
        			rack:""
        	}
          $scope.selfName = {
            id: "",
            shelf: "",
          }
          var rack = self.model.rackList.find(function (rackData){
            if(data.rackId == rackData.rackId){
            	$scope.rackName.rackId=data.rackId;
            	$scope.rackName.rack=data.rackName;
            	$scope.rackName.shelfs=rackData.shelfs;
              
              var shelf = rackData.shelfs.find(function (shelfData){
                $scope.selfName.id = data.shelfId;
                $scope.selfName.shelf = data.shelfName;
              });
              data.rackName=	$scope.rackName;
              data.selfName=	$scope.selfName;
            }
          });
        });
      }
      else{
        self.model.storageDetailsList.forEach(function(item){
          item.rackName = [],
          item.selfName = [],
          item.remark = "",
          item.rackId = "",
          item.shelfId = ""
        });
      }
    }

    var ChangeRack = function (rackName) {
      rackName.shelfs = rackName.shelfs;
      self.model.itemRackName = rackName.rack;
    }

    var ChangeSelf = function (rackData, rackShelf) {
      self.model.itemselfName = rackShelf.shelf;
      // self.model.itemStorageDetails = rackShelf.items;
    }

    var AddRack = function (idx) {
      self.model.storageDetailsList.push({ rackId: '', shelfId: '', remark: '' });
    }

    var lessRack = function (idx) {
      self.model.storageDetailsList.splice(idx, 1);
    }

    var SaveStorageDetails = function () {
      self.model.ItemDetailList[self.model.storageIndex].storageDetails = [];
      self.model.storageDetailsList.forEach(function (x) {
        self.model.ItemDetailList[self.model.storageIndex].storageDetails.push({ rackId: x.rackName.rackId, rackName: x.rackName, shelfId: x.selfName.id, selfName: x.selfName, remark: x.remark });
      });
      console.log(self.model.ItemDetailList);
    }

    var OpenPopup = function (action, popupName, data) {
      self.model.action = action;
      var popup = angular.element('#' + popupName);
      self.model.id = data.id;
      popup.modal('show');
    }

    var ViewStorageDetails = function (rack, selfName) {
      self.model.itemStorageDetails = selfName.items;
    }

    $scope.validaterate = function(){
      var isValid = true;
      angular.forEach(self.model.ItemDetailList, function(item){
        if(parseInt(item.cop) > parseInt(item.mrp)){
          growl.error("MRP should be greater than or equal to COP");
          item.mrp = 0;
          isValid = false;
        }
      });
      if(!isValid)
      {
      $scope.saveBtnDisabled=false;
      }
      return isValid;
    }

    $scope.validateBatchNo = function (item, index) {
      var isValid = true;
      if(self.model.ItemDetailList.length > 1){
        angular.forEach(self.model.ItemDetailList, function(data, key){
          if(data.itemId == item.itemId && data.batchNo == item.batchNo && index != key){
            growl.error("Please enter different Batch No");
            isValid = false;
            item.batchNo = '';
          }
        });
      }
      return isValid;
    }

    $scope.getDocumentType = function(){
      $rootScope.startLoader();
      var URI = BASE_URL + ROOT_URL + GETACTIVEDOCUMENTTYPELIST;
      BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
        $scope.documentTypeList = [];
        if (response.data.status == "success") {
          $scope.documentTypeList = response.data.listObject;
        }
      });
    }
    $scope.getDocumentType();
    
    $scope.addImageDetails = function()
    { 
      var encounterDetails = angular.copy($scope.encounterDetails);
      $scope.encounterDetailsList.push(encounterDetails);
    };
  
    $scope.removeImageDetails = function(deleteIndex)
    { 
      $scope.encounterDetailsList.splice(deleteIndex, 1);
    }	
        
    $scope.encounterDetailsList=[];
    $('#fileId').val(undefined);
    var uploadDoccument =  function()
    {
      $scope.forView = false;
      $scope.encounterDetails={};      
      $scope.encounterDetails.patientId = 0;	
      $scope.encounterDetails.visitTypeId = 1;	
      $scope.encounterDetails.createdBy = $scope.userId;
      $scope.encounterDetails.updatedBy = $scope.userId;
      $scope.encounterDetails.unitId = $scope.unitId;
      $scope.encounterDetails.organisationId = $scope.organizationId;
      $scope.encounterDetails.opbId = $scope.opbIdForDoccumentUpload;
      $scope.encounterDetails.uploadDoccumentDate=new Date();
      if($scope.encounterDetailsList.length>0)
      {
        $('#uploadDetails').modal('show');
      }
      else{
        $('#uploadDetails').modal('show');
        var encounterDetails = angular.copy($scope.encounterDetails);
        // encounterDetails.patientId = encounterObj.patientId;
        $scope.encounterDetailsList.push(encounterDetails);
      }
      $scope.getPatientDocumentList();      
    }
       
    $scope.forView = false;   
    $scope.uploadPatientImage = function()
    {
      $scope.finalDocUploadList=[];
      var count=0;
      $scope.forView = false;
      angular.forEach($scope.encounterDetailsList, function(value, key) {  
        if(value.documentTypeId==undefined || value.documentTypeId=="" || value.documentTypeId==null){
          growl.warning("Please select document type .",
          {
            title : 'warning!'
          });
          $rootScope.stopLoader();
          return false;
        }
        if (angular.equals(value.file, {}) || value.file==undefined) {
          growl.warning('Please select file.',
          {
            title : 'warning!'
          });
          $rootScope.stopLoader();
          return false;
        }
        var ext = value.file.name.split(".").pop().toLowerCase();
        if($.inArray(ext, ["doc","pdf",'docx',"txt"]) == -1) {
          growl.warning('You can only upload *.txt, *.pdf, *.docx, *.doc file',
          {
            title : 'warning!'
          });
          $rootScope.stopLoader();
          return false;
        }
        $scope.finalDocUploadList.push(value);
        console.log("final list",$scope.finalDoc);
      });
         
      if($scope.encounterDetailsList.length==$scope.finalDocUploadList.length){
        $('#uploadDetails').modal('hide');   
        $rootScope.startLoader();
        $scope.count = 0;
        angular.forEach($scope.encounterDetailsList, function(value, key) 
        {
          var promise = $scope.uploadEncounterDoc(value);
          promise.then(function(response) 
          {
            if(response.data.status == "success")
            {
              $scope.count++;
              if($scope.encounterDetailsList.length==$scope.count)
              {
                $('#fileId').val(undefined);
                $scope.finalDocUploadList=[];
                $scope.encounterDetailsList = [];
                $rootScope.stopLoader();
              }
            }
            else 
            {
              growl.error('Something went wrong.',
              {
                title : 'Error!'
              });
            }
          });      
        });
      }    
    }
       
    $scope.getPatientDocumentList = function()
    {
      if($scope.opbIdForDoccumentUpload != undefined && $scope.opbIdForDoccumentUpload != null && $scope.opbIdForDoccumentUpload != 0){
        $rootScope.startLoader();
        var data =
        {
          "patientId":0,
          "unitId":$scope.unitId,
          "organisationId":$scope.organizationId,
          "userId":undefined,
          "visitTypeId":undefined,
          "admissionId":undefined,
          "opbId":$scope.opbIdForDoccumentUpload,
        }
        var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
        BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
          $scope.patientDocumentList = [];
          if (response.data.status == "success") 
          {
            $scope.patientDocumentList = response.data.listObject;  
            angular.forEach($scope.patientDocumentList,function(value, key) {
              var filepath = value.documentPath
              var arr = filepath.split("/");
              var length=arr.length;
              var fileName1=arr[length-1];
              var arr1 = fileName1.split("_");
              var length1=arr1.length;
              var fileName=arr1[length1-1];
              value.fileName=fileName;
            });            
            $rootScope.stopLoader();
          }
          else if(response.data.status == "error")
          {
            $rootScope.stopLoader();
          }
        });
      }
    }
    
    $scope.uploadEncounterDoc = function(Obj)
    {
      var fd = new FormData();
      fd.append('encounterId', 0);
      fd.append('visitTypeId', Obj.visitTypeId);
      fd.append('admissionId', 0);
      fd.append('patientId',0);
      fd.append('documentTypeId', Obj.documentTypeId);
      fd.append('createdBy',Obj.createdBy );
      fd.append('updatedBy', Obj.updatedBy);
      fd.append('unitId', Obj.unitId);
      fd.append('organisationId', Obj.organisationId);
      fd.append('uploadDoccumentDate', moment(Obj.uploadDoccumentDate).format('DD-MM-YYYY HH:mm:ss'));
      fd.append('file',Obj.file );
      fd.append('expenseId', 0);
      fd.append('grnId', 0);
      fd.append('poId',0);
      fd.append('incidentReportingMapperId',0);
      fd.append('companyExpenseId',0);
      fd.append('createdDate',null);
      fd.append('fileEditedName',null);
      fd.append('opbId', $scope.opbIdForDoccumentUpload);
      fd.append('vendorId',0);
      var URI1 = BASE_URL + ROOT_URL + UPLOAD_ENCOUNTER_DOCUMENT;
      return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
      {	
        return response;
      });
    }
    
    $scope.viewUploadedDoccument = function(encounterObj)
    {
      $scope.forView = true;
      $scope.getPatientDocumentList(encounterObj);
      $('#uploadDetails').modal('show');
    }

    $scope.validateDocType = function(){
      angular.forEach($scope.encounterDetailsList, function(value, key) {  
        if(value.documentTypeId==undefined || value.documentTypeId=="" || value.documentTypeId==null){
          growl.warning("Please select document type .",
          {
            title : 'warning!'
          });
        }
      });
    }

    $scope.calculateReceivedQty = function(item){
      if(item.isThreeUOMApplicable == false){
        if(item.selectedUom.uomTypeId == 1){
          item.receivedQtyCon = (item.quantity +"*"+ item.uomListdata[0].conversion);
          item.receivedQtyCalculation = (item.quantity * item.uomListdata[0].conversion);
          item.uomUnitData = item.uomListdata[2].uomUnit;
        }
        else if(item.selectedUom.uomTypeId == 2){
            
        }
        else{
          item.receivedQtyCon = (item.quantity);
          item.receivedQtyCalculation = (item.quantity);
          item.uomUnitData = item.uomListdata[2].uomUnit;
        }
      }
      else{
        if(item.selectedUom.uomTypeId == 1){
          item.receivedQtyCon = (item.quantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
          item.receivedQtyCalculation = (item.quantity * item.uom[0].conversion * item.uom[1].conversion);
          item.uomUnitData = item.uom[2].uomUnit;
        }
        else if(item.selectedUom.uomTypeId == 2){
          item.receivedQtyCon = (item.quantity +"*"+ item.uom[1].conversion);
          item.receivedQtyCalculation = (item.quantity * item.uom[1].conversion);
          item.uomUnitData = item.uom[2].uomUnit;
        }
        else{
          item.receivedQtyCon = (item.quantity);
          item.receivedQtyCalculation = (item.quantity);
          item.uomUnitData = item.uom[2].uomUnit;
        }
      }
    }

    // $scope.validateMRP = function (item, index) {
    //   var isValid = true;
    //   if(self.model.ItemDetailList.length > 1){
    //     angular.forEach(self.model.ItemDetailList, function(data, key){
    //       if(data.itemId == item.itemId && data.mrp == item.mrp && index != key){
    //         growl.error("Please enter different mrp");
    //         isValid = false;
    //         item.mrp = '';
    //       }
    //     });
    //   }
    //   return isValid;
    // }

    // $scope.validateExpiryDate = function (item, index) {
    //   var isValid = true;
    //   if(self.model.ItemDetailList.length > 1){
    //     angular.forEach(self.model.ItemDetailList, function(data, key){
    //       if(data.itemId == item.itemId && moment(data.expiryDate).format('YYYY/MM/DD') == moment(item.expiryDate).format('YYYY/MM/DD') && index != key){
    //         growl.error("Please enter different Expiry Date");
    //         isValid = false;
    //         item.expiryDate = '';
    //       }
    //     });
    //   }
    //   return isValid;
    // }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("addNewOpeningBalance", {
      url: "/addNewOpeningBalance",
      templateUrl: 'views/PharmacyInventory/Store/add-new-opening-balance.html',
      controller: "AddNewOpeningBalance.Controller",
      controllerAs: "vm",
      title: 'Opening Balance'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("AddNewOpeningBalance.Controller", addNewOpeningBalanceController);
})();