(function () {
  "use strict";

  function addNewLinenIndentController($scope, $state, $rootScope, $http, $filter, $cookies, CONSTANTS, StatusService, GenericService, commonDetailService, growl) {
    var self = this;

    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
      $rootScope.loginpage = true;
      self.model = {
        assetCategories: [{
          "category": "Select Asset Category"
        }],
        productCategories: [{
          "category": "Select Product Category"
        }],
        defaultStores: [{
          "store": "Select Store"
        }],
        statuses: [{
          "status": "All"
        }],
        SelectedItemsIds: [],
        selectAllRows: false,
        SelectedItemIds: [],
        ItemDetailList: [],
      };

      self.model.GetAssetCategoryList = GetAssetCategoryList;
      self.model.GetProductCategoryList = GetProductCategoryList;
      self.model.IsMainStore = IsMainStore;
      self.model.GetItemList = GetItemList;
      self.model.searchNewItem = searchNewItem;
      self.model.SelectItem = SelectItem;
      self.model.SelectAllItems = SelectAllItems;
      self.model.AddItems = AddItems;
      self.model.isSelected = isSelected;
      self.model.SaveLinenIndent = SaveLinenIndent;
      self.model.NavigteToBack = NavigteToBack;
      self.model.removeRowData = removeRowData;


      PopulateDropdown();

    }


    var IsMainStore = function (isOn) {
      var indent = self.model;

      if (isOn) {
        indent.toStores = indent.defaultStores.concat($filter('filter')(indent.allStores, {
          isMainStore: true
        }, true));
      } else {
        indent.toStores = indent.defaultStores.concat(indent.allStores);
      }
    }

    function setStatus(x) {
      var status = self.model.statuses.find(function (obj) {
        return obj.status == x;
      });

      return status.id;
    }

    var PopulateDropdown = function () {
      var indent = self.model;

      var URI = CONSTANTS.LINEN_LAUNDRY.LINEN_INDENT.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId+ '/'+$scope.userId;
      indent.fromStores = indent.defaultStores;
      indent.toStores = indent.defaultStores;
      indent.status = indent.statuses[0];
      indent.fromStore = indent.fromStores[0];
      indent.toStore = indent.toStores[0];

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          indent.fromStores = indent.defaultStores.concat(response.data.fromStore);
          indent.allStores = response.data.toStore;
          indent.toStores = indent.defaultStores.concat(response.data.toStore);
          indent.statuses = indent.statuses.concat(response.data.status);
          indent.priorities = response.data.priority;
          indent.priority = indent.priorities[1];

          $rootScope.stopLoader();
          var indentId = commonDetailService.getDataId();
          $rootScope.stopLoader();
          if (!!indentId) {
            commonDetailService.setDataId(null);
            GetLinenIndent(indentId);
          } else {
            return false;
          }
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }
    function GetItemList() {
      self.model.action = 'Open';
      var popup = angular.element('#addNewItemWasteStore');
      popup.modal('show');
      self.model.searchNewItemList = [];
      self.model.isItemChecked = false;
      GetAssetCategoryList();

    }
    var GetAssetCategoryList = function () {
      var indent = self.model;

      var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + 1 + "/"+$scope.unitId + "/"+$scope.organizationId ;

      indent.assetCategory = indent.assetCategories[0];
      indent.productCategory = indent.productCategories[0];

      indent.assetCategories.splice(1);
      indent.productCategories.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          indent.assetCategories = indent.assetCategories.concat(response.data);
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var GetProductCategoryList = function (id) {
      var indent = self.model;

      var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + id + "/"+$scope.organizationId ;
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          indent.productCategories = indent.productCategories.concat(response.data);
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    function searchNewItem() {

      var URI = CONSTANTS.GLOBAL.ITEM_API +"unit/"+$scope.unitId;

      const data = {
        "assetTypeId": 2,
        "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
        "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
        // "fromStoreId": self.model.fromStore.id ? self.model.fromStore.id : undefined,
        // "toStoreId": self.model.toStore.id ? self.model.toStore.id : undefined,
      }
      self.model.searchNewItemList = [];
      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          self.model.searchNewItemList = response.data;
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
          growl.error('Error');
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

    var AddItems = function () {
      var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId;

      var data = {
        "itemIds": self.model.SelectedItemsIds,
        "storeId": self.model.fromStore.id
      };
      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          response.data.forEach(function (item) {
            var isFound = self.model.ItemDetailList.find(function (x) {
              return x.itemId == item.itemId;
            });
            if (!isFound) {
              self.model.ItemDetailList.push(item);
            }
          });
          self.model.SelectedItemsIds = [];
          $rootScope.stopLoader();
          self.model.searchNewItemList = [];
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var isSelected = function (item) {
      return item.isSelected;
    }

    var SaveLinenIndent = function () {
      if (validateSave()) {
        if (!self.model.indentId) {
          saveNewLinenIndentRequest();
        } else {
          UpdateLinenIndent();
        }
      }
    }
    var saveNewLinenIndentRequest = function () {
      var store = self.model;

      var URI = CONSTANTS.LINEN_LAUNDRY.LINEN_INDENT.SAVE_API;

      var data = {
        "fromStoreId": store.fromStore.id,
        "toStoreId": store.toStore.id,
        "priorityId": store.priority.id,
        "remark": store.remark,
        "statusId": setStatus('New'),
        "approvalStatusId": setStatus('New'),
        "userId":$scope.userId,
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "storeId":$scope.storeId,
        "items": []
      };

      angular.forEach(store.ItemDetailList, function (item, key) {
        var storeItem = {
          "itemId": item.itemId,
          "quantity": parseInt(item.quantity),
          "specification":item.specification,
          "statusId": setStatus('New')
        }
        data.items.push(storeItem);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          NavigteToBack();
          growl.success(response.data.message);
        },

        function (err) {
          $rootScope.stopLoader();
          growl.error('Something Went Wrong');
        });
    }

    var UpdateLinenIndent = function () {
      var indent = self.model;
      var URI = CONSTANTS.LINEN_LAUNDRY.LINEN_INDENT.UPDATE_API + indent.indentId;
      var data = {
        "priorityId": indent.priority.id,
        "remark": indent.remark,
        "userId":$scope.userId,
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "storeId":$scope.storeId,
        "items": []
      };

      angular.forEach(indent.ItemDetailList, function (item, key) {

        var indentItem = {
          "id": item.id,
          "itemId": item.itemId,
          "quantity": parseInt(item.quantity),
          "specification":item.specification,

        }
        if (item.id == '' || item.id == null) {
          indentItem.statusId = setStatus('New')
        }

        data.items.push(indentItem);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {

          $rootScope.stopLoader();
          NavigteToBack();
          growl.success(response.data.message);
        },

        function (err) {
          $rootScope.stopLoader();
          growl.error('Something Went Wrong');
        });
    }


    var NavigteToBack = function () {
      $state.go('linenIndent');
    }

    function validateSave() {
      var isValid = true;
      var ItemDetailList = self.model.ItemDetailList;
      // !self.model.fromStore.id || !self.model.toStore.id || !self.model.priority.id || 

      if (ItemDetailList.length < 1) {
        growl.error('Error', {
          title: "Please validate all required fields"
        });
        isValid = false;
      }
      if (self.model.fromStore.id == self.model.toStore.id) {
        growl.warning('Warning', {
          title: "Please Select Different Stores"
        });
        isValid = false;
      }
      for (let ItemDetail of ItemDetailList) {
        if (ItemDetail.quantity == null) {
          growl.error('Error', {
            title: "Please Enter Indent Quantity"
          });
          isValid = false;
        }
      }
      return isValid;
    }
    var GetLinenIndent = function (indentId) {

      var URI = CONSTANTS.LINEN_LAUNDRY.LINEN_INDENT.DETAIL_API + indentId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.indentId = response.data.id;

          self.model.fromStore = self.model.fromStores.find(function (item) {
            return response.data.fromStoreId == item.id;
          });

          self.model.toStore = self.model.toStores.find(function (item) {
            return response.data.toStoreId == item.id;
          });

          self.model.priority = self.model.priorities.find(function (item) {
            return response.data.priorityId == item.id;
          });


          self.model.remark = response.data.remark;

          self.model.itemDetails = [];

          angular.forEach(response.data.items, function (item) {
            var indentItem = {
              "id": item.id,
              "fromStore": item.fromStore,
              "toStore": item.toStore,
              "fromStoreId": item.fromStoreId,
              "toStoreId": item.toStoreId,
              "priorityId": item.id,
              "indentDetailId": item.indentDetailId,
              "itemId": item.itemId,
              "itemCode": item.itemCode,
              "itemName": item.itemName,
              "quantity": item.quantity,
              "remark": item.remark,
              "indentQuantity": item.indentQuantity,
              "stockAvailable": item.stockAvailable,
              "stockInTransit": item.stockInTransit,
            }

            self.model.ItemDetailList.push(indentItem);
          });
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }
    function removeRowData(item) {
      var index = self.model.ItemDetailList.indexOf(item);
      self.model.ItemDetailList.splice(index, 1);
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("addNewLinenIndent", {
      url: "/addNewLinenIndent",
      templateUrl: 'views/Linen/add-new-linen-indent.html',
      controller: "AddNewLinenIndent.Controller",
      title: 'Linen Indent',
      controllerAs: "vm"
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("AddNewLinenIndent.Controller", addNewLinenIndentController);
})();
