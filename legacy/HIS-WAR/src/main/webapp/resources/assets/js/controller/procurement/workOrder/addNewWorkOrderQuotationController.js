(function () {
  "use strict";

  function addNewWorkOrderQuotationController($scope, $rootScope, $http, $filter, $state,  CONSTANTS, GenericService,PaginationService, commonDetailService, growl, CurrencyService, StatusService,$cookies) {
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
        contractTypes: [{
          "description": "Select Contract Type"
        }],
      /*   batchText: [{
          "batchNo": "Select Batch"
        }], */
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
        termsVisible: 1,
        termsList: [],
        requestType: 1,
        search: {
          itemName: undefined,
          itemCode: undefined
        },
        accessoryFlag: false,
        termsFlag: false
      };

      StatusService.GetStatus().then(function (status) {
        self.model.statuses = status;
      });

      CurrencyService.GetCurrency().then(function(currency){
        self.model.Currency = currency;
      });

      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.isItemChecked = false;
      self.model.PopulateAssetCategoryDropdown = PopulateAssetCategoryDropdown;
      self.model.PopulateProductCategory = PopulateProductCategory;
      self.model.GetItemList = GetItemList;
      self.model.SelectAllItems = SelectAllItems;
      self.model.SelectItem = SelectItem;
      self.model.AddItems = AddItems;
      self.model.RemoveItem = RemoveItem;
      self.model.ConfirmRemove = ConfirmRemove;
      self.model.ValidateBatchNo = ValidateBatchNo;
      self.model.ValidateQuantity = ValidateQuantity;
      self.model.ValidateSpecification = ValidateSpecification;
      self.model.SaveWorkOrderQuotation = SaveWorkOrderQuotation;
      self.model.NavigateToBack = NavigateToBack;
      self.model.ChangeRequestType = ChangeRequestType;
      self.model.GetWorkOrders = GetWorkOrders;
      self.model.Expand = Expand;
      self.model.SelectAllIndentItems = SelectAllIndentItems;
      self.model.SelectIndentItem = SelectIndentItem;
      self.model.AddWorkOrderItems = AddWorkOrderItems;
      self.model.OpenPopup = OpenPopup;
      self.model.ClosePopup = ClosePopup;
      self.model.fillPRModalList = fillPRModalList;
      self.model.PrDetails = PrDetails;
      self.model.AddInclusion = AddInclusion;
      self.model.SaveInclusion = SaveInclusion;
      self.model.ViewRequestDetails = ViewRequestDetails;
      self.model.SetOldValueStore = SetOldValueStore;
      self.model.ChangeFromStore = ChangeFromStore;
      self.model.ConfirmStorePopup = ConfirmStorePopup;
      self.model.SetOldValueAssetType = SetOldValueAssetType;
      self.model.ChangeFromAssetType = ChangeFromAssetType;
      self.model.ConfirmAssetTypePopup = ConfirmAssetTypePopup;

      PopulateDropdown();
      PopulateDropdownContractType();
    }

    var PrDetails = function (prDetailList) {
      var popup = angular.element('#PRDetails');
      popup.modal('show');
      self.model.prDetailList = prDetailList;
    }

    var fillPRModalList = function (list) {
      self.model.prModalList = list;
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

    var ValidateBatchNo = function (item) {
      var isValid = true;
      if(self.model.requestType == 13){
        return isValid;
      }
      else{
      if (!item.batchNumber) {
        item.isBatchNoValid = true;
        isValid = false;
        growl.error("Please Select Batch");
      }
    }
      return isValid;
    }
    var ValidateQuantity = function (item) {
      var isValid = true;
      if(self.model.requestType == 13){
        return isValid;
      }
      else{
      if (!item.quantity) {
        item.isQuantity = true;
        isValid = false;
        growl.error("Please Enter Quantity.");
      }
    }
      return isValid;
    }
   
    var ValidateSpecification = function (item) {
      var isValid = true;
      if (!item.specification) {
        item.isSpecifications = true;
        isValid = false;
        growl.error("Please Enter Specification.");
      }
      return isValid;
    }
    
    var PopulateDropdown = function () {
      try {
        $rootScope.startLoader();
       // self.model.store = self.model.stores[0];

        var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_QUOTATION_REQUEST.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.stores = self.model.stores.concat(response.data.store);
            var prId = commonDetailService.getDataId();
            $rootScope.stopLoader();
            for(var i=0;self.model.stores.length > i;i++)
            {
              if(self.model.stores[i].id==JSON.parse(sessionStorage.getItem('selectedTopStore')).storeId)
              {
                self.model.store = self.model.stores[i];
              }
            }
            if (!!prId) {
              commonDetailService.setDataId(null);
              GetEnquiry(prId);
            }
          },
          function (err) {
            $rootScope.stopLoader();
          });
      } catch (error) {
        alert(error);
      }
    }

    var PopulateDropdownContractType = function () {
      try {
        $rootScope.startLoader();
        self.model.contractType = self.model.contractTypes[0];
        var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_REQUEST.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.contractTypes = self.model.contractTypes.concat(response.data.contractType);
            $rootScope.stopLoader();
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
        if (requestType == 13) {
          self.model.action = 'Open';
          var popup = angular.element('#againstRequest');
          popup.modal('show');
          self.model.indents = [];
          self.model.fromDate = new Date();
          self.model.toDate = new Date();
        } else if (requestType == 1) {
          self.model.action = 'Open';
          var popup = angular.element('#addNewItemWasteStore');
          popup.modal('show');
          self.model.selectAllRows = false;
          self.model.SelectedItemsIds = [];
          PopulateAssetCategoryDropdown();
          self.model.search.itemCode = undefined;
          self.model.search.itemName = undefined;
          self.model.items = [];
          self.model.pagination.data=[];
        }
      }
    }
    var GetEnquiry = function (enquiryId) {
      var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_QUOTATION_REQUEST.DETAILS_API + $scope.unitId + '/' + enquiryId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.enquiryId = response.data.id;
          self.model.store = self.model.stores.find(function (store) {
            return response.data.storeId == store.id;
          });

          self.model.contractType = self.model.contractTypes.find(function (type) {
            return response.data.contractTypeId == type.id;
          });
        
          self.model.remark = response.data.remark;
          self.model.requestType = response.data.requestTypeId;
          self.model.itemDetails = [];
          
          self.model.itemDetails = response.data.details;
          angular.forEach(response.data.details, function (item) {
         
             item.batch=item.batchList;
            var a = item.batchList.find(function (batch) {
              return item.batchId == batch.batchId;
            });
            item.batchNumber = a;
        
            // a.batchNo = a.batchNo.toString();

            // var enquiryItem = {
            //   "id": item.id,
            //   "itemId": item.itemId,
            //   "itemCode": item.itemCode,
            //   "itemName": item.itemName,
            //   "quantity": item.quantity,
            //   "remark": item.remark,
            //   "currentVendor": item.currentVendor,
            //   "currentVendorId": item.currentVendorId,
            //   "specification": item.specification,
            //   "expectedInclusion": item.expectedInclusion,
            //   "batchNumber": a
            // }
            // self.model.itemDetails.push(enquiryItem);
          
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

      if (!self.model.contractType.id) {
        self.model.isContractType = true;
        isValid = false;
      }

      if (!self.model.requestType) {
        isValid = false;
      }

      if (!self.model.store.id || !self.model.contractType.id || !self.model.requestType) {
        growl.error('Please select required fields');
        isValid = false;
      }

      return isValid;
    }

    var ValidateItems = function () {
      var isValidItem = true;
      if (!self.model.itemDetails.length) {
        growl.error('Please add items to save workorder quotation');
        isValidItem = false;
      } else {
        angular.forEach(self.model.itemDetails, function (item){
          if (!item.expectedInclusion) {
            item.isExpectedInclusion = true;
            growl.error('Please add expected inclusion');
            isValidItem = false;
          }
          if (!ValidateBatchNo(item)) {
            item.isBatchValid=true;
            isValidItem = false;
          }
          if (!ValidateQuantity(item)) {
            item.isQuantity=true;
            isValidItem = false;
          }
          if (!ValidateSpecification(item)) {
            item.isSpecifications=true;
            isValidItem = false;
          }
        });
      }

      return isValidItem;
    }
   
    var PopulateAssetCategoryDropdown = function () {
      // todo: assetType Id hardcode
      var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + 2 + "/"+$scope.unitId + "/"+$scope.organizationId;

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
        growl.error('From date should be less than to date');
        isValid = false;
      }

      return isValid;
    }

    var GetWorkOrders = function () {
      if (ValidateDate()) {
        var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_QUOTATION_REQUEST.QO_SEARCH;

        var data = {
          "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
          "contractTypeId": self.model.contractType.id,
          "storeId": self.model.store.id,
          "unitId":$scope.unitId,
        }
        $rootScope.startLoader();  
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.indents = response.data;
            angular.forEach(self.model.indents, function (pr) {
              pr["isChecked"] = false;
              angular.forEach(pr.items, function (item) {
                item["isChecked"] = false;
              });
            });

            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
            self.model.indents = [];
            growl.error(err.data.message);
          });
      }
    }

    var Expand = function (item) {
      item.Expanded = !item.Expanded;
    }

    var GetItemList = function () {
      if(self.model.itemName){
        if(self.model.itemName.length >= 3){    
          var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_QUOTATION_REQUEST.GET_ITEM_LIST;
          
          // todo: assetTypeId hardcode
          var data = {
            "assetTypeId": 2,
            "assetCategoryId": self.model.assetCategory.id,
            "productCategoryId": self.model.productCategory.id,
            "storeId": self.model.store.id,
            "itemCode": self.model.itemCode,
            "itemName": self.model.itemName,
            "unitId":$scope.unitId,
            "organizationId":$scope.organizationId,
            "userId":$scope.userId
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
          var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_QUOTATION_REQUEST.GET_ITEM_LIST;
          
          // todo: assetTypeId hardcode
          var data = {
            "assetTypeId": 2,
            "assetCategoryId": self.model.assetCategory.id,
            "productCategoryId": self.model.productCategory.id,
            "storeId": self.model.store.id,
            "itemCode": self.model.itemCode,
            "itemName": self.model.itemName,
            "unitId":$scope.unitId,
            "organizationId":$scope.organizationId,
            "userId":$scope.userId
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
      if (!self.model.SelectedItemsIds.length) {
        growl.error('Please select items to add');
        return false;
      } else {
        var URI = CONSTANTS.GLOBAL.DETAILS_BATCH;
        var data = {
          "itemIds": self.model.SelectedItemsIds,
          "storeId": self.model.store.id,
          "unitId":$scope.unitId
        };

        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            response.data.forEach(function (value) {
              var isFound = self.model.itemDetails.find(function (x) {
                return x.itemId == value.itemId;
              });

              if (!isFound) {
                self.model.itemDetails.push(value);

               // value.batch = self.model.batchText.concat(value.batch);
              // value.batchNumber = value.batch[0];
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
            // self.model.SelectedItemsIds = [];
            growl.error(err.data.message);
          });
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
        var requestNumber = prItem.requestNumber;
        var requestDate = moment(prItem.requestDate).format('YYYY-MM-DD');
        var prId1 = prItem.id;
        self.model.selectedWorkOrderId = prItem.id;
        prItem.items.forEach(function (item) {
          var quotationDetails = [];
          var itemId = item.itemId;
          var reqItemId = item.id;
          // var prQuantity = Number(item.prQuantity);
          // var prDate = moment(item.prDate).format('YYYY-MM-DD');
          var prObj = { id: reqItemId, requestNumber: requestNumber, requestDate: requestDate};
          // var prIdList = {prId: prId1, prDetailId: [prId]};
          if (itemDetailsHash.hasOwnProperty(itemId)) {
            // item found in already saved list
            var itemDetail = itemDetailsHash[itemId];
            if (itemDetail.prIds && itemDetail.prIds.length) {
              // prIds list is present
              if (itemDetail.prIds.indexOf(reqItemId) == -1) {
                itemDetail.requestDetails.push(prObj);
                // itemDetail.prDataList.push(prIdList);
                itemDetail.prIds.push(reqItemId);
                // itemDetail.prQuantity = item.prQuantity
              }
            } else {
              // prIds list not present
              itemDetail.requestDetails = [prObj]
              // itemDetail.prDataList = [prIdList]
              itemDetail.prIds = [reqItemId];
            }

          } else if (newItemsHash.hasOwnProperty(itemId)) {
            // item found in new item hash
            var savedItem = newItemsHash[itemId];
            savedItem.requestDetails.push(prObj);
            // savedItem.prDataList.push(prIdList);
            savedItem.prIds.push(reqItemId);
          } else {
            // item not found in item hash
            var clonedItem = JSON.parse(JSON.stringify(item));
            clonedItem.detailsId = clonedItem.id;
            delete clonedItem.id;
            clonedItem.requestDetails = [prObj]
            // clonedItem.prDataList = [prIdList]
            clonedItem.prIds = [reqItemId];
            newItemsHash[itemId] = clonedItem;
            // newItemsHash.workOrderRequestDetailId = reqItemId;
          }
        });
      });

      for (var itemId in newItemsHash) {
        self.model.itemDetails.push(newItemsHash[itemId]);
      }

      console.log(self.model.itemDetails);
    }

    var SelectedItems = function () {
      self.model.prDetails = [];
      var itemIdArray = [];
      angular.forEach(self.model.indents, function (pr) {
        var prData = null;
        if (pr.isChecked) {
          prData = {
            "id": pr.id,
            "woIds": []
          }
        }

        angular.forEach(pr.items, function (item) {
          if (item.isChecked) {
            prData.woIds.push(item.id);
          }
        });

        if (pr.isChecked && prData.woIds.length > 0)
          self.model.prDetails.push(prData);
      });
    }

    var AddWorkOrderItems = function () {
      if (self.model.intendItemIds.length == 0) {
        growl.error('Please select items to add');
        return false;
      } else {
        var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_QUOTATION_REQUEST.ADD_ITEM_QO+ "/" + $scope.unitId;

        SelectedItems();

        var data = self.model.prDetails;
        
        
        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            var responseData = response.data;
            updatePrItemDetails(responseData);

            self.model.indentItemDetails = [];
            self.model.intendItemIds = [];
            self.model.items = [];
            self.model.indents = [];

            var popup = angular.element('#againstRequest');
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

    // Direct Request Items Selection
    var isSelected = function (item) {
      return item.isSelected;
    }

    var SelectAllItems = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedItemsIds = self.model.items.map(function (item) {
          item.isSelected = true;
          self.model.isItemChecked = true;
          return item.itemId;
        });
      } else {
        self.model.SelectedItemsIds = [];
        self.model.items.forEach(function (item) {
          item.isSelected = false;
          self.model.isItemChecked = true;
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
      self.model.isItemChecked = false;
      self.model.items.forEach(function (item) {
        if (item.isSelected == true) {
          self.model.isItemChecked = true;
          return;
        }
      });
    }

    var ConfirmRemove = function(item){
      self.model.OpenPopup('Open', 'confirmRemove');
      self.model.itemForRemove = item;
    }

    var RemoveItem = function (item) {
      var newArray = [];

      self.model.itemDetails.map(function (obj) {
        if (obj.itemId !== item.itemId) {
          newArray.push(obj);
        }
      });

      self.model.itemDetails = newArray;
      self.model.ClosePopup('Close', 'confirmRemove');
    }

    var AddInclusion = function (item) {
      self.model.itemInclusion = item;
      self.model.expectedInclusion = item.expectedInclusion;
    }

    var SaveInclusion = function(){
      self.model.itemInclusion.expectedInclusion = self.model.expectedInclusion;
      self.model.itemInclusion.isExpectedInclusion = false;
      ClosePopup('Close', 'expectedInclusion');
    }

    var ViewRequestDetails = function (item) {
      self.model.requestList = item.requestDetails;
    }

    var SaveWorkOrderQuotation = function () {
      if (ValidateItems()) {
        if (!self.model.enquiryId) {
          CreateWorkOrderQuotation();
        } else {
          UpdateWorkOrderQuotation();
        }
      }
    }

    var CreateWorkOrderQuotation = function () {
      var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_QUOTATION_REQUEST.DETAILS_API;
      var data = {
        "approvalStatusId": setStatus('New'),
        "contractTypeId": self.model.contractType.id,
        "remark": self.model.remark,
        "requestTypeId": self.model.requestType,
        "statusId": setStatus('New'),
        "storeId": self.model.store.id,
        "workOrderId": self.model.selectedWorkOrderId,
        "sendVendorSelection": false,
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId
      };

      angular.forEach(self.model.itemDetails, function (item, key) {
        var quotationItem = {
          "itemId": item.itemId,
          "batchId": item.batchId,
          "workOrderRequestDetailId": item.detailsId,
          "quantity": parseInt(item.quantity),
          "specification": item.specification,
          "expectedInclusion": item.expectedInclusion,
          "remark": item.remark,
          "statusId": setStatus('New')
        }

        if (self.model.requestType == 1) {
          quotationItem.currentVendorId = item.batchNumber.currentVendorId;
          quotationItem.batchId = item.batchNumber.batchId;
        } else if (self.model.requestType == 13) {
          quotationItem.currentVendorId = item.currentVendorId;
          quotationItem.batchId = item.batchId;
        }

        data.items.push(quotationItem);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          NavigateToBack();
          growl.success(response.data.message);
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var UpdateWorkOrderQuotation = function () {
      var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_QUOTATION_REQUEST.DETAILS_API + self.model.enquiryId;

      var data = {
        "remark": self.model.remark,
        "sendVendorSelection": false,
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId
      };
      angular.forEach(self.model.itemDetails, function (item, key) {
        if (!!item.id) {
          var enquiryItem = {
            "id": item.id,
            "specification": item.specification,
            "remark": item.remark,
            "quantity": parseInt(item.quantity),
            "expectedInclusion": item.expectedInclusion,
            "batchId": item.batch.batchId
          }
        } else {
          var enquiryItem = {
          "id": item.id,
          "itemId": item.itemId,
          "quantity": parseInt(item.quantity),
          "specification": item.specification,
          "expectedInclusion": item.expectedInclusion,
          "workOrderRequestDetailId": item.workOrderRequestDetailId,
          "batchId": item.batch.batchId,
          "remark": item.remark,
          "statusId": setStatus('New')
          }
        }

        if (self.model.requestType == 1) {
          enquiryItem.currentVendorId = item.batchNumber.currentVendorId;
          enquiryItem.batchId = item.batchNumber.batchId;
        } else if (self.model.requestType == 13) {
          enquiryItem.currentVendorId = item.currentVendorId;
          enquiryItem.batchId = item.batchId;
        }

        data.items.push(enquiryItem);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          NavigateToBack();
          growl.success(response.data.message);
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var NavigateToBack = function () {
      $state.go('workorderQuotationRequest');
    }

    var SetOldValueStore = function (store) {
      self.model.oldStore = store;
      self.model.store = self.model.oldStore;
    }

    var ChangeFromStore = function (store) {
      if (!!self.model.itemDetails.length) {
        self.model.store = self.model.oldStore;
        self.model.storeNew = JSON.parse(JSON.stringify(store));
        PopupAction('Open', 'confirmStore');
      }
    }

    var ConfirmStorePopup = function () {
      self.model.store = self.model.storeNew;
      PopupAction('close', 'confirmStore');
      self.model.itemDetails = [];
    }
    var SetOldValueAssetType = function (contractType) {
      self.model.oldContractType = contractType;
      self.model.contractType = self.model.oldContractType;
    }

    var ChangeFromAssetType = function (contractType) {
      if (!!self.model.itemDetails.length) {
        self.model.contractType = self.model.oldContractType;
        self.model.ContractNew = JSON.parse(JSON.stringify(contractType));
        PopupAction('Open', 'confirmAssetTypePopup');
      }
    }

    var ConfirmAssetTypePopup = function () {
      self.model.contractType = self.model.ContractNew;
      PopupAction('close', 'confirmAssetTypePopup');
      self.model.itemDetails = [];
    }

    var PopupAction = function (action, modal) {
      var popup = angular.element('#' + modal);
      self.model.action = action;
      if (action == "Open") {
        popup.modal('show');
      } else {
        popup.modal('hide');
      }
    }
    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("addnewWorkorderQuotation", {
      url: "/addnewWorkorderQuotation",
      templateUrl: 'views/procurement/workOrder/add-new-workorder-quotation-request.html',
      controller: "AddnewWorkorderQuotation.Controller",
      controllerAs: "vm",
      title: 'Work Order Quotation Request'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("AddnewWorkorderQuotation.Controller", addNewWorkOrderQuotationController);
})();