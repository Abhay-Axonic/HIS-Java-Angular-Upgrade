(function () {
  "use strict";

  function addNewWorkOrderController($scope, $rootScope, $filter, $http, $state,  CONSTANTS, GenericService,PaginationService, commonDetailService, rejectReasonService, StatusService, growl,$cookies) {
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
        assetCategories: [{
          "category": "Select Asset Category"
        }],
        productCategories: [{
          "category": "Select Product Category"
        }],
        stores: [{
          "store": "Select Store"
        }],
        contractTypes: [{
          "description": "Select Contract Type"
        }],

        SelectedItemsIds: [],
        itemDetails: [],
        purchaseId: null,
        isSpecification: [],
        vendorSearch: '',
        vendorNames: [],
        search: {
          itemName: undefined,
          itemCode: undefined
        },
        // batchText: [{
        //   "batchNo": "Select Batch"
        // }]
      };

      rejectReasonService.GetReason().then(function (reason) {
        self.model.rejectReasons = reason;
      });

      StatusService.GetStatus().then(function (status) {
        self.model.statusList = status;
      });
      
      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.PopulateAssetCategoryDropdown = PopulateAssetCategoryDropdown;
      self.model.PopulateProductCategory = PopulateProductCategory;
      self.model.GetItemList = GetItemList;
      self.model.SelectAllItems = SelectAllItems;
      self.model.SelectItem = SelectItem;
      self.model.AddItems = AddItems;
      self.model.AddCopyItem=AddCopyItem;
      self.model.isItemChecked = false;
      self.model.RemoveItem = RemoveItem;
      self.model.SaveWorkOrder = SaveWorkOrder;
      self.model.NavigateToWorkOrder = NavigateToWorkOrder;
      self.model.AddAssets = AddAssets;
      self.model.SetDecimal = SetDecimal;
      self.model.SearchVendorNames = SearchVendorNames;
      self.model.AddVendorName = AddVendorName;
      self.model.ConfirmRemove = ConfirmRemove;
      self.model.ValidateBatchNo = ValidateBatchNo;
      self.model.ValidateQuantity = ValidateQuantity;
      self.model.ValidateSpecification = ValidateSpecification;
      self.model.ValidateVendor = ValidateVendor;
      self.model.SetOldValueStore = SetOldValueStore;
      self.model.ChangeFromStore = ChangeFromStore;
      self.model.ConfirmStorePopup = ConfirmStorePopup;
      self.model.SetOldValueAssetType = SetOldValueAssetType;
      self.model.ChangeFromAssetType = ChangeFromAssetType;
      self.model.ConfirmAssetTypePopup = ConfirmAssetTypePopup;
      self.model.PopupAction = PopupAction;
      self.model.SetOldContractType=SetOldContractType;
      PopulateDropdown();
    }

    var setStatus = function (data) {
      var status = self.model.statusList.find(function (obj) {
        return obj.status == data;
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
    var ValidateBatchNo = function (item) {
      var isValid = true;
      if (!item.selectedBatchNo) {
        item.isBatchNoValid = true;
        isValid = false;
        growl.error("Please Select Batch");
      }
      if(item.selectedBatchNo.batchId == item.selectedBatch.batchId){
        isValid = false;
        growl.warning("Please Select Different Batch");
      }
      return isValid;
    }
    var ValidateQuantity = function (item) {
      var isValid = true;
      if (!item.quantity) {
        item.isQuantity = true;
        isValid = false;
        growl.error("Please Enter Quantity.");
      }
      else{
        if(item.quantity == 0){
          growl.error("Quantity cannot be zero");
          item.isQuantity = true;
          isValid = false;
        }
        if(item.quantity > item.selectedBatchNo.currentQuantity){
          growl.warning("Quantity is greater than available quantity");
          item.quantity = "";
          item.isQuantity = true;
          isValid = false;
        }
        else{

        }
      }
      return isValid;
    }

    var ValidateSpecification = function (item) {
      var isValid = true;
      if (!item.specifications) {
        item.isSpecifications = true;
        isValid = false;
        growl.error("Please Enter Specification.");
      }
      return isValid;
    }

    var ValidateVendor = function (item) {
      var isValid = true;
      if (!item.vendorName) {
        item.isVendor = true;
        isValid = false;
        growl.error("Please Enter Vendor.");
      }
      return isValid;
    }
    var PopulateDropdown = function () {
      try {
        $rootScope.startLoader();
       // self.model.store = self.model.stores[0];
      //  self.model.contractType = self.model.contractTypes[0];

        var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_REQUEST.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.stores = self.model.stores.concat(response.data.store);
            self.model.contractTypes = self.model.contractTypes.concat(response.data.contractType);
            for(var i=0;i<self.model.stores.length;i++)
            {
              if(self.model.stores[i].id==JSON.parse(sessionStorage.getItem('selectedTopStore')).storeId)
              {
                self.model.store = self.model.stores[i];
              }
            }

            var purchaseId = commonDetailService.getDataId();
            $rootScope.stopLoader();
            if (!!purchaseId) {
              commonDetailService.setDataId(null);
              GetWorkOrder(purchaseId);
            }
          },
          function (err) {
            $rootScope.stopLoader();
          });
      } catch (error) {
        alert(error);
      }
    }

    var AddAssets = function (prType) {
      if (ValidateDetails()) {

        self.model.action = 'Open';
        var popup = angular.element('#addNewItemWasteStore');
        popup.modal('show');
        PopulateAssetCategoryDropdown();
        self.model.items = [];
        self.model.pagination.data=[];
        self.model.itemCode = undefined;
        self.model.itemName = undefined;
      }
    }

    var GetWorkOrder = function (purchaseId) {
      var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_REQUEST.DETAILS_API + $scope.unitId + '/' + purchaseId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.purchaseId = response.data.id;
          self.model.store = self.model.stores.find(function (item) {
            return response.data.storeId == item.id;
          });
          self.model.contractType = self.model.contractTypes.find(function (item) {
            return response.data.contractType == item.description;
          });

          self.model.remark = response.data.remark;
          self.model.itemDetails = [];
          self.model.itemDetails = response.data.items;

          angular.forEach(self.model.itemDetails, function (item) {

            item.selectedBatchNo = item.batchList.find(function (batch) {
              return item.batchId == batch.batchId;
            });
            // item.currentVendorName = item.currentVendor;
            item.vendorName = item.vendor;
            item.specifications = item.specification;
            item.batch = item.batchList;
          });

          // });

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

      if (!self.model.contractType.id) {
        isValid = false;
        self.model.isContractTypeValid = true;
      }

      if (!self.model.store.id || !self.model.contractType.id) {
        isValid = false;
        growl.error('Please select required field');
      }

      return isValid;
    }

    var ValidateItems = function () {
      var isValidItem = true;
      var isValid = true;
      var items = self.model.itemDetails;
      if (!items.length) {
        growl.error('Please add items');
        isValidItem = false;
      } else {
        angular.forEach(items, function (item) {
          if (!item.selectedBatchNo) {
            item.isBatchNoValid=true;
            growl.error("Please Select Batch");
            isValidItem = false;
          }
          if (!item.quantity || item.quantity <= 0) {
            item.isQuantity = true;
            growl.error("Please Enter Quantity");
            isValidItem = false;
          }
          if(item.quantity){
            if(item.quantity > item.selectedBatchNo.currentQuantity){
              growl.error("Please Enter Valid Quantity.");
              item.isQuantity = true;
              isValid = false;
            }
          }
          if (!item.specifications) {
            item.isSpecifications = true;
            growl.error("Please Enter Specifications");
            isValidItem = false;
          }
          if (!item.vendorName || !item.vendorId) {
            item.isVendor = true;
            growl.error("Please Enter valid Vendor Name");
            isValidItem = false;
          }
        })

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
      var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + 2 + "/"+$scope.unitId + "/"+$scope.organizationId;

      self.model.assetCategory = self.model.assetCategories[0];
      self.model.productCategory = self.model.productCategories[0];

      self.model.assetCategories.splice(1);
      self.model.productCategories.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.assetCategories = self.model.assetCategories.concat(response.data);
          self.model.assetCategory = self.model.assetCategories[1];
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

    var GetItemList = function () {
      var URI = CONSTANTS.GLOBAL.ITEMS_WORKORDER;

      var data = {
        "assetTypeId": 2,
        "assetCategoryId": self.model.assetCategory.id,
        "productCategoryId": self.model.productCategory.id,
        "storeId": self.model.store.id,
        "itemCode": self.model.itemCode,
		"itemName": self.model.itemName,
		"unitId":$scope.unitId
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

        $rootScope.startLoaderPopup();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            response.data.forEach(function (item) {
              var isFound = self.model.itemDetails.find(function (x) {
                return x.itemId == item.itemId;
              });
              if (!isFound) {
                self.model.itemDetails.push(item);
                // item.batch = self.model.batchText.concat(item.batch);
                item.batchNumber = item.batch[0];
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
    var AddCopyItem = function (index, itemDetails) {
      itemDetails.splice(index + 1, 0, CopyItem(index, itemDetails));
    }

    var CopyItem = function (index, itemDetails) {
      var item = itemDetails[index];
      return {
        itemCode: item.itemCode,
        itemName: item.itemName,
        itemId: item.itemId,
        batch: item.batch,
        selectedBatch: item.batch[0],
      }
    }
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

/*     var RemoveItem = function (item) {
      var newArray = [];

      self.model.itemDetails.map(function (obj) {
        if (obj !== item) {
          newArray.push(obj);
        }
      });
      self.model.itemDetails = newArray;
    } */

    var SaveWorkOrder = function () {
      if (ValidateItems()) {
        if (!self.model.purchaseId) {
          CreateWorkOrder();
        } else {
          UpdateWorkOrder();
        }
      }
    }

    var CreateWorkOrder = function () {

      var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_REQUEST.DETAILS_API;

      var data = {
        "contractTypeId": self.model.contractType.id,
        "storeId": self.model.store.id,
        "remark": self.model.remark,
        "statusId": setStatus('New'),
        "approvalStatusId": setStatus('New'),
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId
      };

      angular.forEach(self.model.itemDetails, function (item, key) {
        var orderItem = {
          "itemId": item.itemId,
          "quantity": parseInt(item.quantity),
          "currentVendorId": item.selectedBatchNo.currentVendorId,
          "remark": item.remark,
          "specification": item.specifications,
          "vendorId": item.vendorId,
          "batchId": item.selectedBatchNo.batchId,
          "statusId": setStatus('New')
        }

        data.items.push(orderItem);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          NavigateToWorkOrder();
          growl.success(response.data.message);
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var UpdateWorkOrder = function () {
      var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_REQUEST.DETAILS_API + self.model.purchaseId;

      var data = {
        "remark": self.model.remark,
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId,
        "contractTypeId" : self.model.contractType.id
      };
      angular.forEach(self.model.itemDetails, function (item, key) {
        if (!!item.id) {
          var orderItem = {
            "id": item.id,
            "quantity": parseInt(item.quantity),
            "remark": item.remark,
            "specification": item.specifications,
            "currentVendorId": item.selectedBatchNo.currentVendorId,
            "vendorId": item.vendorId,
            "batchId": item.selectedBatchNo.batchId
          }
        } else {
          var orderItem = {
            "id": item.id,
            "itemId": item.itemId,
            "quantity": parseInt(item.quantity),
            "remark": item.remark,
            "specification": item.specifications,
            "currentVendorId": item.selectedBatchNo.currentVendorId,
            "vendorId": item.vendorId,
            "batchId": item.selectedBatchNo.batchId,
            "statusId": setStatus('New')
          }
        }

        data.items.push(orderItem);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          NavigateToWorkOrder();
          growl.success(response.data.message);
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var SearchVendorNames = function (search) {
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

    var nameNotFound = function (search) {
      var item = {
        "vendorFound": false,
        "detail": "Vendor not found with name " + search,
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

    var NavigateToWorkOrder = function () {
      $state.go('workorderRequest');
    }

    // store change confirmation
    var SetOldValueStore = function (store) {
      self.model.oldStore = store;
      self.model.store = self.model.oldStore;
    }
    
    var SetOldContractType = function (contractType) {
        self.model.oldContractType = contractType;
        self.model.contractType = self.model.oldContractType;
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

    var ConfirmRemove = function (index,item) {
      self.model.PopupAction('Open', 'confirmRemove');
      self.model.itemForRemove = item;
    }

    var RemoveItem = function (item) {
      var index = self.model.itemDetails.indexOf(item);
      self.model.itemDetails.splice(index, 1);
      self.model.PopupAction('Close', 'confirmRemove');
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
    $stateProvider.state("addItemWorkorder", {
      url: "/addItemWorkorder",
      templateUrl: 'views/procurement/workOrder/add-item-workorder-request.html',
      controller: "AddItemWorkorder.Controller",
      controllerAs: "vm",
      title: 'Work Order Request'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("AddItemWorkorder.Controller", addNewWorkOrderController);
})();