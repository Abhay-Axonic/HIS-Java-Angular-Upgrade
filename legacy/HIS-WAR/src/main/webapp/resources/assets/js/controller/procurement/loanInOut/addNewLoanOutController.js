(function () {
  "use strict";

  function addNewLoanOutController($scope, $rootScope, $http, $filter, $state, CONSTANTS, GenericService,PaginationService, commonDetailService, CurrencyService, rejectReasonService, StatusService, growl,$cookies,stopTransactionService) {
    var self = this;

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
        dcDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          minDate: new Date(),
        },
        dcDate: new Date(),
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
        assetTypes: [{
          "type": "Select Asset Type"
        }],
        SelectedItemsIds: [],
        itemDetails: [],
        loanOutId: null,
        indentItemDetails: [],
        isQuantity: [],
        dateOpen: [],
        expiryDate: [],
        searchLoan: {
          itemName: undefined,
          itemCode: undefined,
          batchName: undefined
        }
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
      self.model.SaveLoanOut = SaveLoanOut;
      self.model.NavigateToLoanOut = NavigateToLoanOut;
      self.model.GetItems = GetItems;
      self.model.Expand = Expand;
      self.model.SearchVendorNames = SearchVendorNames;
      self.model.AddVendorName = AddVendorName;
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
      self.model.ValidateLoanOutQuantity = ValidateLoanOutQuantity;
      PopulateDropdown();
    }

    // store change confirmation
    var SetOldValueStore = function (store) {
      self.model.oldStore = store;
      self.model.store = self.model.oldStore;
    }

    var ChangeFromStore = function (store) {
      if (!!self.model.itemDetails.length) {
        self.model.store = self.model.oldStore;
        self.model.storeNew = JSON.parse(JSON.stringify(store));
        OpenPopup('Open', 'confirmStore');
      }
    }

    var ConfirmStorePopup = function () {
      self.model.store = self.model.storeNew;
      ClosePopup('close', 'confirmStore');
      self.model.itemDetails = [];
    }

    // asset type change confirmation
    var SetOldValueAssetType = function (assetType) {
      self.model.oldAssetType = assetType;
      self.model.assetType = self.model.oldAssetType;
    }

    var ChangeFromAssetType = function (assetType) {
      if (!!self.model.itemDetails.length) {
        self.model.assetType = self.model.oldAssetType;
        self.model.assetTypeNew = JSON.parse(JSON.stringify(assetType));
        OpenPopup('Open', 'confirmAssetTypePopup');
      }
    }

    var ConfirmAssetTypePopup = function () {
      self.model.assetType = self.model.assetTypeNew;
      ClosePopup('close', 'confirmAssetTypePopup');
      self.model.itemDetails = [];
    }

    var OpenExpiryDate = function (idx) {
      self.model.dateOpen[idx] = true;
    }

    var OpenDCDate = function () {
      self.model.dcDateOpened = true;
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

    var setStatus = function (x) {
      var status = self.model.statusList.find(function (obj) {
        return obj.status == x;
      });

      return status.id;
    }

    var PopulateDropdown = function () {
      try {
        $rootScope.startLoader();
       // self.model.store = self.model.stores[0];
        self.model.assetType = self.model.assetTypes[0];

        var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_OUT.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.stores = self.model.stores.concat(response.data.store);
            self.model.assetTypes = self.model.assetTypes.concat(response.data.assetType);
            for(var i=0;self.model.stores.length > i;i++)
            {
              if(self.model.stores[i].id==JSON.parse(sessionStorage.getItem('selectedTopStore')).storeId)
              {
                self.model.store = self.model.stores[i];
              }
            }

            var loanOutId = commonDetailService.getDataId();
            $rootScope.stopLoader();
            if (!!loanOutId) {
              commonDetailService.setDataId(null);
              GetLoanOut(loanOutId);
            }
          },
          function (err) {
            $rootScope.stopLoader();
          });
      } catch (error) {
        alert(error);
      }
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
          "loanInId": []
        }
        angular.forEach(po.items, function (item) {
          if (item.isChecked) {
            poData.loanInId.push(item.id);
          }
        });
        if (po.isChecked && poData.loanInId.length > 0) {
          self.model.poDetails = poData;
        }
      }
    }

    var isSelectedItem = function (item) {
      return item.isSelectedItem;
    }

    var GetItems = function () {
      if (ValidateDetails()) {
        self.model.items = [];
        self.model.pagination.data=[];
        var popup = angular.element('#getItemLoan');
        popup.modal('show');
        PopulateAssetCategoryDropdown();

        self.model.itemName = undefined;
        self.model.itemCode = undefined;
        self.model.searchLoan.batchNo = undefined;
      }
    }

    var GetLoanOut = function (loanOutId) {
      var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_OUT.DETAILS_API+ $scope.unitId + '/' + loanOutId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          // todo
          self.model.loanOutId = loanOutId;

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
          // self.model.loanInNumber = new Date(response.data.loanInNumber);
          self.model.itemDetails = [];

          angular.forEach(response.data.items, function (item) {
            var indentItem = {
              "id": item.id,
              "loanOutQty": item.loanOutQuantity,
              "loanInDetailId": item.loanInDetailId,
              "itemId": item.itemId,
              "itemCode": item.itemCode,
              "itemName": item.itemName,
              "batchNo": item.batchNo,
              "batchId": item.batchId,
              "expiry": item.expiryDate,
              "returnQuantity": item.returnQuantity,
              "cop": item.cop,
              "totalAmount": item.totalAmount,
              "remark": item.remark,
              "availableQuantity": item.stockAvailable
            }

            if (self.model.assetType.id == 1) {
              indentItem.uom = item.uom;
              indentItem.uomType = item.uom;
              indentItem.uomUnit = item.uomUnit;
              indentItem.uomTypeId = item.uomTypeId;
              indentItem.uomUnitId = item.uomUnitId;
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

      if (!self.model.vendorId) {
        isValid = false;
        self.model.isVendorId = true;
      }

      if (!self.model.store.id || !self.model.assetType.id || !self.model.vendorId) {
        isValid = false;
        growl.error('Please select required field');
      }

      return isValid;
    }

    var ValidateItems = function () {
      var isValidItem = true;

      if (self.model.dcNumber == undefined) {
          growl.error("Please enter DC number");
          isValidItem = false;
        
      }
      
      if (!!self.model.dcNumber) {
        if (!/^[a-z0-9]*[-]*[/]?[a-z0-9]*$/i.test(self.model.dcNumber)) {
          growl.error("Please enter valid DC number");
          isValidItem = false;
        }
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
          if (!ValidateLoanOutQuantity(self.model.itemDetails[i])) {
            isValidItem = false;
          }
        }
      }

      return isValidItem;
    }

    var PopulateAssetCategoryDropdown = function () {
      var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + self.model.assetType.id + '/'+$scope.unitId+ '/'+$scope.organizationId;

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

    var Expand = function (item) {
      item.Expanded = !item.Expanded;
    }

    var GetItemList = function () {
      if(self.model.itemName){
        if(self.model.itemName.length >= 3){
          //var URI = CONSTANTS.GLOBAL.BATCH_API + "unit/"+$scope.unitId;
          var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_OUT.GET_ITEM_SEARCH_API;

          var data = {
            "assetTypeId": self.model.assetType.id,
            "assetCategoryId": self.model.assetCategory.id,
            "productCategoryId": self.model.productCategory.id,
            "storeId": self.model.store.id,
            "itemCode": self.model.itemCode,
            "itemName": self.model.itemName,
            "batchName": self.model.batchNo,
            "unitId":$scope.unitId
          }
          self.model.items = [];
          self.model.pagination.data=[];
          self.model.pagination.totalPages=0;
          $rootScope.startLoaderPopup();
          GenericService.serviceAction("POST", URI, data).then(
            function (response) {
              self.model.items = response.data;
              if(!!response && !!response.data && response.data.length > 0){            
                PaginationService.searchDataByField(self.model.items, self.model.pagination);
              }
              self.model.selectAllRows = false;

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
          var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_OUT.GET_ITEM_SEARCH_API;

          var data = {
            "assetTypeId": self.model.assetType.id,
            "assetCategoryId": self.model.assetCategory.id,
            "productCategoryId": self.model.productCategory.id,
            "storeId": self.model.store.id,
            "itemCode": self.model.itemCode,
            "itemName": self.model.itemName,
            "batchName": self.model.batchNo,
            "unitId":$scope.unitId
          }
          self.model.items = [];
          self.model.pagination.data=[];
          self.model.pagination.totalPages=0;
          $rootScope.startLoaderPopup();
          GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.items = response.data;
            if(!!response && !!response.data && response.data.length > 0){            
              PaginationService.searchDataByField(self.model.items, self.model.pagination);
            }
            self.model.selectAllRows = false;

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
    	 if (!self.model.SelectedItemsIds.length) {
 	        growl.error('Please select items to add');
 	        return false;
 	      }else{
 	    	  
 	    	 ActaulAddItems();
 	    	  
 	    	  /*
 	    	 self.model.selectedStopIds = [];
 	    	self.model.SelectedItemsIds.forEach(function (item) {
 	    		self.model.selectedStopIds.push(item.itemId);
 	    	});
 	    	  var stopData = {
 	   	                "itemIds":  self.model.selectedStopIds,
 	   	                "storeId": self.model.store.id	      		 
 	   	          };
 	        	stopTransactionService.GetstopTransactionData(stopData).then(function(responseData){        
 	                var StopTransactionData = responseData;  
 	                if(StopTransactionData.length > 0 ){    
 	                         var startdate =  $filter('date')(StopTransactionData[0].startDate, "dd-MM-yyyy");
 	                         var stopdate=  $filter('date')(StopTransactionData[0].stopDate, "dd-MM-yyyy");
 	                         growl.warning("Stop Transation is in Process for this Item(s) <br/>Start Date : "+ startdate +" <br/> Stop Date: "+ stopdate +" <br/> Days: "+ StopTransactionData[0].days +" <br/>  Hours: "+ StopTransactionData[0].hours +" <br/>  Minutes: "+ StopTransactionData[0].minutes +"  ");                    
 	                          $rootScope.stopLoader(); 
 	                         $rootScope.stopLoaderPopup();
 	                         self.model.SelectedItemsIds = [];
 	                         self.model.selectedStopIds = [];
 	                         return false;
 	                 }
 	                else{
 	                	ActaulAddItems();
 	                }
 	       	  });    
 	      */}
    }

    var ActaulAddItems = function () {    
        $rootScope.startLoaderPopup();
        self.model.SelectedItemsIds.forEach(function (item) {
          var isFound = self.model.itemDetails.find(function (x) {
            return x.batchId == item.batchId;
          });
          if (!isFound) {
        	 item.cop = item.cop!=undefined ?  item.cop.toFixed(2) : 0;
            self.model.itemDetails.push(item);
            // item.loanOutQty = 0;
          }
        });

        self.model.SelectedItemsIds = [];
        self.model.items = [];

        var popup = angular.element('#getItemLoan');
        popup.modal('hide');

        $rootScope.stopLoaderPopup();
      
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
          hash[itemId][indentId] = { pendingQuantity: item.pendingQuantity }
        } else {
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
            return item;
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
        itemIds.push(item);
      } else {
        var index = itemIds.indexOf(item);
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

    var SaveLoanOut = function () {
      if (ValidateItems()) {
        if (!self.model.loanOutId) {
          CreateLoanOut();
        } else {
          UpdateLoanOut();
        }
      }
    }

    var CreateLoanOut = function () {
      var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_OUT.SAVE_API;

      var data = {
        "storeId": self.model.store.id,
        "vendorId": self.model.vendorId,
        "assetTypeId": self.model.assetType.id,
        "dcNo": self.model.dcNumber,
        "dcDate": moment(self.model.dcDate).format('YYYY-MM-DD'),
        "statusId": setStatus("New"),
        "totalAmount": self.model.totalAmount,
        "remark": self.model.remark,
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId

      };

      angular.forEach(self.model.itemDetails, function (item) {
        var loanItem = {
          "batchId": item.batchId,
          "loanOutQuantity": parseInt(item.loanOutQty),
          "remark": item.remark,
          "totalAmount": item.totalAmount,
          "cop": item.cop
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
          NavigateToLoanOut();
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var UpdateLoanOut = function () {
      var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_OUT.DETAILS_API  + self.model.loanOutId;

      var data = {
        "dcNo": self.model.dcNumber,
        "dcDate": moment(self.model.dcDate).format('YYYY-MM-DD'),
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
          "loanOutQuantity": parseInt(item.loanOutQty),
          "remark": item.remark,
          "totalAmount": item.totalAmount,
          "cop": item.cop
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
          NavigateToLoanOut();
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

    var NavigateToLoanOut = function () {
      $state.go('loanOut');
    }

    var ValidateLoanOutQuantity = function (item) {
      var isValid = true;

      if (isNaN(parseFloat(item.loanOutQty))) {
        item.isQuantityValid = true;
        isValid = false;
        growl.error("Please Enter Quantity");
      } else if (parseInt(item.loanOutQty) == 0) {
        item.isQuantityValid = true;
        isValid = false;
        growl.error('Quantity Cannot Be Zero');
      }
      else if (parseInt(item.loanOutQty) > item.availableQuantity) {
        item.isQuantityValid = true;
        isValid = false;
        growl.error('Entered quantity is exceeding the available quantity');
      } else {
        item.loanOutQty = parseInt(item.loanOutQty);
        isValid = true;
      }

      return isValid;
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("addNewLoanOut", {
      url: "/addNewLoanOut",
      templateUrl: 'views/procurement/consignment/add-new-loan-out.html',
      controller: "AddNewLoanOut.Controller",
      controllerAs: "vm",
      title: 'Loan Out'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("AddNewLoanOut.Controller", addNewLoanOutController);
})();