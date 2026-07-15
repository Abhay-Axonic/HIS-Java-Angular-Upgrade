(function () {
  "use strict";

  function addKITmasterPharmacyController($rootScope, StatusService, $scope, $http, $filter, $cookies, CONSTANTS, $state, GenericService, CurrencyService, commonDetailService, growl) {
    var self = this;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
      $rootScope.loginpage = true;

      self.model = {
        kitTypeList: [{
          "description": "Select Kit Type"
        }],
        assetCategories: [{
          "category": "Select Asset Category"
        }],
        productCategories: [{
          "category": "Select Product Category"
        }],

        SelectedItemsIds: [],
        itemList: [],
        ItemDetailList: [],

      };

      self.model.GetItemList = GetItemList;
      self.model.NavigteToBack = NavigteToBack;
      self.model.PopulateAssetCategoryDropdown = PopulateAssetCategoryDropdown;
      self.model.GetProductCategoryList = GetProductCategoryList;
      self.model.SearchNewItem = SearchNewItem;
      self.model.GetAllItemDetails = GetAllItemDetails;
      self.model.SelectAllItems = SelectAllItems;
      self.model.SelectItem = SelectItem;
      self.model.AddItems = AddItems;
      self.model.ValidateQuantity = ValidateQuantity;
      self.model.SaveKitMaster = SaveKitMaster;
      PopulateDropdown();
    }

    var PopulateDropdown = function () {
      self.model.kitType = self.model.kitTypeList[0];
      var URI = CONSTANTS.MASTER.UNIT.KIT_MASTER.DROPDOWN_API ;
     // var URI = 'http://clini-his.predikly.com:8082/HIS-WAR/api/global/status';
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.kitTypeList = self.model.kitTypeList.concat(response.data.kitType);
          $rootScope.stopLoader();
          var kitId = commonDetailService.getDataId();
          $rootScope.stopLoader();
          if (!!kitId) {
            commonDetailService.setDataId(null);
            GetKitMasterData(kitId);
          }

        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var GetKitMasterData = function (id) {
      self.model.activeImplant = id;
      var URI = CONSTANTS.MASTER.UNIT.KIT_MASTER.DETAILS_API + id;
      //var URI = 'http://clini-his.predikly.com:8082/HIS-WAR/api/global/status';
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
         
          var kitType = self.model.kitTypeList.find(function (type) {
            return response.data.kitTypeId == type.id;
          });

          self.model.masterId = response.data.id;
          self.model.kitType = kitType;
          self.model.kitCode = response.data.code;
          self.model.kitName = response.data.description;
          self.model.ItemDetailList = [];
          self.model.ItemDetailList = response.data.items;
          $rootScope.stopLoader();
        });
    };

    var setStatus = function (x) {
      var status = self.model.statusList.find(function (obj) {
        return obj.status == x;
      });

      return status.id;
    }

    StatusService.GetStatus().then(function (status) {
      self.model.statusList = status;
    });

    var ValidateQuantity = function (item) {
      var isValid = true;
      if (!item.quantity) {
        item.isQuantity = true;
        isValid = false;
        growl.error("Please Enter Quantity.");
      }
      else if (item.quantity == 0) {
        item.isQuantity = true;
        isValid = false;
        growl.error("Quantity should not be zero.");
      }
      return isValid;
    }

    var GetItemList = function () {

      self.model.selectAllRows = false;
      self.model.NewItemList = [];
      self.model.action = 'Open';
      var popup = angular.element('#addItem_modal');
      popup.modal('show');
      self.model.isItemChecked = false;
      self.model.search = {};
      PopulateAssetCategoryDropdown();

    }

    var PopulateAssetCategoryDropdown = function () {
      var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + 1 + "/"+$scope.unitId + "/"+$scope.organizationId ;

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
    var GetProductCategoryList = function (id) {
      var productCategory = self.model;
      productCategory.productCategory = productCategory.productCategories[0];
      productCategory.productCategories.splice(1);
      //TODO : Add asset category to the uri
      var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + id + "/"+$scope.organizationId ;
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            productCategory.productCategories = productCategory.productCategories.concat(response.data);
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
          });
    }

    var SearchNewItem = function () {
      self.model.selectAllRows = false;
      self.model.isItemChecked = false;
      self.model.SelectedItemsIds = [];
      var URI = CONSTANTS.GLOBAL.ITEM_API +"unit/"+$scope.unitId;

      const data = {
        "assetTypeId": 1,
        "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
        "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
        "itemCode" : self.model.search.itemCode,
        "itemName" : self.model.search.itemName
      }
      self.model.NewItemList = [];
      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          self.model.NewItemList = response.data;
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
          growl.error('Error');
        });
    }
    var GetAllItemDetails = function () {

      self.model.NewItemList.forEach(function (item) {
        if (item.isSelected == true) {
          var isFound = self.model.itemList.find(function (x) {
            return x.batchId == item.batchId;

          });
          if (!isFound) {
            item.mrp = item.mrp.toFixed(2);
            self.model.itemList.push(item);
          }
          else {
            growl.error('Item already present in the list')
          }
        }
      });
    }

    var AddItems = function () {
      if (!self.model.SelectedItemsIds.length) {
        growl.error('Please select items');
      } else {
         var URI = CONSTANTS.GLOBAL.KIT_ITEM_DETAILS_API;
        //var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId;

        var data = {
          "itemIds": self.model.SelectedItemsIds,
          "storeId": 0,
          "unitId":$scope.unitId,
          "organizationId":$scope.organizationId
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
              } else {
                growl.error('Item Present In the List')
              }

            });
            self.model.SelectedItemsIds = [];
            self.model.items = [];
            var selectedUom;
            angular.forEach(self.model.ItemDetailList, function (x) {
            	
              if ( (x.id ==  undefined || x.id == null) && x.uom.length > 0 )
              {
                if (x.uom) {
                  selectedUom = x.uom.find(function (sUom) {
                    return sUom.storeUom == true;
                  });
                  x["uomType"] = selectedUom.uomType;
                  x["uomUnit"] = selectedUom.uomUnit;
                  x["uomTypeId"] = selectedUom.uomTypeId;
                  x["uomUnitId"] = selectedUom.uomUnitId;
                }
                else {
                  x["uomType"] = x.uomType;
                  x["uomUnit"] = x.uomUnit;
                  x["uomTypeId"] = x.uomTypeId;
                  x["uomUnitId"] = x.uomUnitId;
                }

              }
            });
            $rootScope.stopLoader();

            self.model.searchNewItemList = [];
            var popup = angular.element('#addItem_modal');
            popup.modal('hide');
          },
          function (err) {
            $rootScope.stopLoader();
            growl.error(err.data.message);
          });
      }
    }

    var SelectAllItems = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedItemsIds = self.model.NewItemList.map(function (item) {
          item.isSelected = true;
          self.model.isItemChecked = true;
          return item.itemId;
        });
      } else {
        self.model.SelectedItemsIds = [];
        self.model.NewItemList.forEach(function (item) {
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

      if (self.model.NewItemList.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }

      self.model.isItemChecked = false;
      self.model.NewItemList.forEach(function (item) {
        if (item.isSelected == true) {
          self.model.isItemChecked = true;
          return;
        }
      });
    }

    var isSelected = function (item) {
      return item.isSelected;
    }



    var NavigteToBack = function () {
      $state.go('KITmasterPharmacy');
    }


    var ValidateKitMaster = function () {
      var isValid = true;

      if (!self.model.kitType.id) {
        isValid = false;
        self.model.isValidKitType = true;
      }
      if (!self.model.kitCode) {
        isValid = false;
        self.model.isValidKitCode = true;
      }
      if (!self.model.kitName) {
        isValid = false;
        self.model.isValidKitName = true;
      }

      var items = self.model.ItemDetailList;
      if (!items.length) {
        growl.error('Please add items');
        isValid = false;
      }
      else {
        angular.forEach(items, function (item) {
          if (!item.quantity) {
            item.isQuantity = true;
            growl.error("Please Enter Quantity.");
            isValid = false;
          }
          else if (parseInt(item.quantity) == 0) {
            item.isQuantity = true;
            isValid = false;
            growl.error('Quantity Cannot Be Zero');
          }

        })
      }
      return isValid;
    }

    var SaveKitMaster = function () {
      if (ValidateKitMaster()) {
        if (!self.model.masterId) {
          CreateKitMaster();
        } else {
          UpdateKitMaster();
        }
      }
    };

    var CreateKitMaster = function () {
      var URI = CONSTANTS.MASTER.UNIT.KIT_MASTER.SAVE_API ;

      //var URI = CONSTANTS.MASTER.UNIT.IMPLANT_MASTER.SAVE_API;
      var data = {
        "code": self.model.kitCode,
        "description": self.model.kitName,
        "kitTypeId": self.model.kitType.id,
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId
      }

      angular.forEach(self.model.ItemDetailList, function (item, key) {
        var kitItem = {
          "id": item.itemId,
          "status": item.status,
          "uomUnitId": item.uomUnitId,
          "uomTypeId": item.uomTypeId,
          "quantity": parseInt(item.quantity)
        }

        data.items.push(kitItem);
      });


      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
      //    ClearFields();
          NavigteToBack();
          $rootScope.stopLoader();
          growl.success(response.data.message);
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
          growl.error('Something went wrong');
        });


    };

    var UpdateKitMaster = function () {
      var URI = CONSTANTS.MASTER.UNIT.KIT_MASTER.UPDATE_API + self.model.masterId;
      //var URI = CONSTANTS.MASTER.UNIT.IMPLANT_MASTER.DETAILS_API + self.model.masterId;

      var data = {
        "id":self.model.masterId,
        "code": self.model.kitCode,
        "description": self.model.kitName,
        "kitTypeId": self.model.kitType.id,
        "status": self.model.status,
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId
      }

      angular.forEach(self.model.ItemDetailList, function (item, key) {
        if (!!item.id) {
          var kitItems = {
            "id": item.id,
            "itemId": item.itemId,
            "status": item.status,
            "uomUnitId": item.uomUnitId,
            "uomTypeId": item.uomTypeId,
            "quantity": parseInt(item.quantity)
          }
        }
        else {
          var kitItems = {
            "id": item.id,
            "itemId": item.itemId,
            "status": item.status,
            "uomUnitId": item.uomUnitId,
            "uomTypeId": item.uomTypeId,
            "quantity": parseInt(item.quantity)
          }
        }
        data.items.push(kitItems);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          NavigteToBack();
         // ClearFields();     
          growl.success(response.data.message);
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error('Something Went Wrong');
        });
    };

    function ClearFields() {
      self.model.kitType = [self.model.kitTypeList[0]];
      self.model.kitName = "";
      self.model.KitCode = "";
      self.model.kitTypeList.splice(1);
      self.model.search = '';
      self.model.ItemDetailList=[];
      self.model.isOk = false;
    }


    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("addKITmasterPharmacy", {
      url: "/addKITmasterPharmacy",
      title:"Kit Master",
      templateUrl: 'views/master/UnitMaster/pharmacy/add-KIT-master-pharmacy.html',
      controller: "AddKITmasterPharmacy.Controller",
      controllerAs: "vm"
    });

  }

  angular
    .module("myApp")
    .config(config)
    .controller("AddKITmasterPharmacy.Controller", addKITmasterPharmacyController);
})();

