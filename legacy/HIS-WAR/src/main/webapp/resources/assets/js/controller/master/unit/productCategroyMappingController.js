(function() {
  "use strict";

  function productCategroyMappingController(
    $scope,
    $rootScope,
    $http,
    CONSTANTS,
    GenericService,
    PaginationService,
    $filter,
    $state,
    growl,
    $cookies
  ) {
    var self = this;

    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
      $rootScope.loginpage = true;
      self.model = {
        Stores: [
          {
            store: "Select Store"
          }
        ],
        pagination: {
          currentPage: 1,
          totalPages: 0,
          pageSize: '10',
          search: '',
          searchData: PaginationService.searchData,
          setTotalPages: PaginationService.setTotalPages,
          getPagesList: PaginationService.getPagesList,
          setPage: PaginationService.setPage,
          setFirstPage: PaginationService.setFirstPage,
          setPreviousPage: PaginationService.setPreviousPage,
          setNextPage: PaginationService.setNextPage,
          setLastPage: PaginationService.setLastPage,
          sortBy: PaginationService.sortBy
        },
        categoryMappingList: []
      };
      self.model.GetCategoryMappingList = GetCategoryMappingList;
      self.model.ToggleStatus = ToggleStatus;
      //    self.model.SaveProductCategoryMapping = SaveProductCategoryMapping;
      self.model.CreateProductCategoryMapping = CreateProductCategoryMapping;
      self.model.UpdateProductCategoryMapping = UpdateProductCategoryMapping;
      self.model.SetOldValueStore = SetOldValueStore;
      self.model.ChangeFromStore = ChangeFromStore;
      self.model.ConfirmStorePopup = ConfirmStorePopup;
      self.model.OpenPopup = OpenPopup;
      self.model.ClosePopup = ClosePopup;
      self.model.IdList = [];
      self.model.productCategoryId;
      self.model.StoreId;
      self.model.id;
      PopulateStore();
    }

    //Method to Populate Store Dropdown
    var PopulateStore = function() {
      var categoryMapping = self.model;

      var URI = CONSTANTS.GLOBAL.USER_STORE_API + "/"+$scope.organizationId + "/"+$scope.unitId+ "/"+$scope.userId ;

      categoryMapping.store = categoryMapping.Stores[0];

      categoryMapping.Stores.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function(response) {
          categoryMapping.Stores = categoryMapping.Stores.concat(response.data);
          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        }
      );
    };

    var GetCategoryMappingList = function(store) {
      if (store.id == undefined) {
        store.id = 0;
      }
      var categoryMapping = self.model;
      var URI =
        CONSTANTS.MASTER.UNIT.PHARAMACY.PRODUCT_CATEGORY_MAPPING
          .PRODUCT_CATEGORY_MAPPING_API +$scope.organizationId+'/'+ store.id;
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function(response) {
          self.model.categoryMappingList = response.data;
          if(!!response && !!response.data && response.data.length > 0){            
            PaginationService.searchData(self.model.categoryMappingList, self.model.pagination);
          }
          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        }
      );
    };
    var ToggleStatus = function(productRestrictionMaster) {
      self.model.productCategoryId = productRestrictionMaster.productCategoryId;
      self.model.StoreId = self.model.store.id;
      self.model.id = productRestrictionMaster.id;
      if (productRestrictionMaster.status == true) {
        CreateProductCategoryMapping(productRestrictionMaster);
      } else {
        UpdateProductCategoryMapping(productRestrictionMaster);
      }
    };

    var CreateProductCategoryMapping = function(productRestrictionMaster) {
      var URI =
        CONSTANTS.MASTER.UNIT.PHARAMACY.PRODUCT_CATEGORY_MAPPING
          .PRODUCT_CATEGORY_MAPPING_SAVE;

      var data = {
        storeId: self.model.store.id,
        productCategoryId: self.model.productCategoryId,
        status: true
      };

      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function(response) {
          $rootScope.stopLoader();
          growl.success(response.data.message);
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
          productRestrictionMaster.status = !productRestrictionMaster.status;
          growl.error("Something went wrong");
        }
      );
    };

    var UpdateProductCategoryMapping = function(productRestrictionMaster) {
      var URI =
        CONSTANTS.MASTER.UNIT.PHARAMACY.PRODUCT_CATEGORY_MAPPING
          .PRODUCT_CATEGORY_MAPPING_API +
        self.model.id +
        "/false";

      $rootScope.startLoader();
      GenericService.serviceAction("PATCH", URI, {}).then(
        function(response) {
          $rootScope.stopLoader();
          growl.success(response.data.message);
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
          productRestrictionMaster.status = !productRestrictionMaster.status;
          growl.error("Something went wrong");
        }
      );
    };

    var OpenPopup = function(action, modal) {
      self.model.action = action;
      var popup = angular.element("#" + modal);
      popup.modal("show");
    };

    var ClosePopup = function(action, modal) {
      self.model.action = action;
      var popup = angular.element("#" + modal);
      popup.modal("hide");
    };

    var SetOldValueStore = function(store) {
      self.model.oldStore = store;
      self.model.store = self.model.oldStore;
    };

    var ChangeFromStore = function(store) {
      if (!!self.model.categoryMappingList.length) {
        self.model.store = self.model.oldStore;
        self.model.storeNew = JSON.parse(JSON.stringify(store));
        OpenPopup("Open", "confirmStore");
      } else {
        GetCategoryMappingList(store);
      }
    };

    var ConfirmStorePopup = function(storeData) {
      self.model.store = self.model.storeNew;
      ClosePopup("close", "confirmStore");
      GetCategoryMappingList(self.model.store);
      //self.model.storeGenericList = [];
    };

    initializeController();
  }

  function config($stateProvider) {
     $stateProvider.state("productRestriction.productCategoryMapping", {
      url: "/productCategoryMapping",
      templateUrl:"views/PharmacyInventory/StockAdjustment/product-category-mapping.html",
      controller: "ProductCategoryMapping.Controller",
      title: 'Product Restriction',
      resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 633);
				 	});
					if(arr.length <= 0){
						$state.go("accessDenied")
						deferred.reject();
					 } else {
						deferred.resolve();
					 }
		        });
		
		        return deferred.promise;
		      }
		 },
      controllerAs: "vm"
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller(
      "ProductCategoryMapping.Controller",
      productCategroyMappingController
    );
})();
