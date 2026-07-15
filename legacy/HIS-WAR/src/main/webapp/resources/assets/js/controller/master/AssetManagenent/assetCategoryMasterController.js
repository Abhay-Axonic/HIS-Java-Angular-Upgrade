(function () {
  "use strict";
  function assetCategoryMasterController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, growl,PaginationService) {
    var self = this;

    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
      $rootScope.loginpage = true;
      self.model = {
        categoryDropdown: [{
          "type": "Select Asset Type"
        }],
        
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
          }
      };
      self.model.ShowConfirmPopup = ShowConfirmPopup;
      self.model.GetAssetCategory = GetAssetCategory;
      self.model.ToggleStatus = ToggleStatus;
      self.model.SaveAssetCategory = SaveAssetCategory;
      self.model.ClearFields = ClearFields;
      self.model.ValidateAssetCategory = ValidateAssetCategory;
      self.model.clearSearch = clearSearch;
      self.model.SetOldValueAssetType = SetOldValueAssetType;
      self.model.ChangeAssetType = ChangeAssetType;
      self.model.ConfirmAssetTypePopup = ConfirmAssetTypePopup;
      PopulateDropdown();
    }

    //Get Asset type
    var PopulateDropdown = function () {
      var assetCategory = self.model;
      assetCategory.assetType = assetCategory.categoryDropdown[0];

      assetCategory.categoryDropdown.splice(1);
      var URI = CONSTANTS.GLOBAL.ASSET_TYPE_API + "/"+$scope.unitId + "/"+$scope.organizationId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          assetCategory.categoryDropdown = assetCategory.categoryDropdown.concat(response.data);
          $rootScope.stopLoader();
          GetAssetCategories();
        },
        function (err) {

        });
    }

    var GetAssetCategories = function () {
      var URI = CONSTANTS.MASTER.UNIT.FIXED_ASSET.ASSET_CATEGORIES_API +"org/"+$scope.organizationId;
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
        	
          self.model.pagination.data=[];
          self.model.assetCategories = response.data;
          
          if(!!response && !!response.data && response.data.length > 0){            
              PaginationService.searchData(self.model.assetCategories, self.model.pagination);
            }
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var GetAssetCategory = function (categoryId) {
      self.model.isValidCode = false;
      self.model.isValidType = false;
      self.model.isValidCategory = false;

      self.model.activeCategoryId = categoryId;
      var URI = CONSTANTS.MASTER.UNIT.FIXED_ASSET.ASSET_CATEGORIES_API + categoryId;
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.categoryId = response.data.id;
          var assetType = self.model.categoryDropdown.find(function (type) {
            return response.data.assetTypeId == type.id;
          });
          self.model.assetType = assetType;
          self.model.categoryCode = response.data.code;
          self.model.categoryDescription = response.data.category;
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    };

    var ToggleStatus = function (category) {
      var URI = CONSTANTS.MASTER.UNIT.FIXED_ASSET.ASSET_CATEGORIES_API + category.id;
      var data = {
        "status": category.status,
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId
      };
      if (category.status == false) {
        ClearFields();
      }
      $rootScope.startLoader();
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          growl.success('Status changed successfully');
        }, function (err) {
          if(err.status == 406){
            growl.error(err.data.message);
            category.status = true;
          }else{
         
          growl.error('Something Went Wrong');
        }
        });
        $rootScope.stopLoader();
    };

    var ShowConfirmPopup = function () {
      var popup = angular.element('#confirmSave');
      popup.modal('show');
    }

    var SaveAssetCategory = function () {
      if (ValidateAssetCategory()) {
        if (!self.model.categoryId) {
          CreateAssetCategory();
        } else {
          UpdateAssetCategory();
        }
      }
    }

    var ValidateAssetCategory = function () {
      var isValid = true;
      if (!self.model.categoryCode) {
        isValid = false;
        self.model.isValidCode = true;
      }
      if (!self.model.assetType.id) {
        isValid = false;
        self.model.isValidType = true;
      }
      if (!self.model.categoryDescription) {
        isValid = false;
        self.model.isValidCategory = true;
      }

      return isValid;
    }


    var ValidateCode = function () {
      var isValid = true;

      self.model.assetCategories.forEach(function (element) {
        if (element.code == self.model.categoryCode) {
          growl.error('Duplicate asset category code');
          isValid = false;
        }
      });
      if(!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.categoryCode)) {
        //-------------------------^^^^^^^^^^
        growl.error('Code can only be alpha numeric with single hypen.');
        return;
     }
      return isValid;
    }

    var ValidateCodeForUpdate = function () {
      var isValid = true;

      self.model.assetCategories.forEach(function (element) {
        if(element.id != self.model.activeCategoryId){
          if (element.code == self.model.categoryCode) {
            growl.error('Duplicate asset category code');
            isValid = false;
            self.model.validUpdate = true;
          }
        }
      });
      if(!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.categoryCode)) {
        //-------------------------^^^^^^^^^^
        growl.error('Code can only be alpha numeric with single hypen.');
        return;
     }
      return isValid;
    }

    var CreateAssetCategory = function () {
      if(ValidateCode()){
      var URI = CONSTANTS.MASTER.UNIT.FIXED_ASSET.ASSET_CATEGORIES_API;
      var data = {
        "assetTypeId": self.model.assetType.id,
        "code": self.model.categoryCode,
        "category": self.model.categoryDescription,
        "status": true,
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId
      }
      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          var assetType = self.model.categoryDropdown.find(function (type) {
            return type.id == self.model.assetType.id;
          });
          var insertedData = {
            "id": response.data.id,
            "assetType": assetType.type,
            "code": data.code,
            "category": data.category,
            "status": data.status
          };
          self.model.assetCategories.push(insertedData);
          GetAssetCategories();
          ClearFields();
          $rootScope.stopLoader();
          growl.success(response.data.message);
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
      }
    };

    var UpdateAssetCategory = function () {
      if(ValidateCodeForUpdate()){
      var URI = CONSTANTS.MASTER.UNIT.FIXED_ASSET.ASSET_CATEGORIES_API + self.model.activeCategoryId;
      var data = {
        "assetTypeId": self.model.assetType.id,
        "code": self.model.categoryCode,
        "category": self.model.categoryDescription,
        "status": true,
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId
      }
      $rootScope.startLoader();
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          var assetCategory = self.model.assetCategories.find(function (category) {
            return category.id == self.model.activeCategoryId;
          });
          var assetType = self.model.categoryDropdown.find(function (type) {
            return type.id == data.assetTypeId;
          });
          assetCategory.assetType = assetType.type;
          assetCategory.code = data.code;
          assetCategory.category = data.category;
          ClearFields();
          $rootScope.stopLoader();
          growl.success(response.data.message);
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error('Something Went Wrong');
        });
      }
    };

    var ClearFields = function () {
      self.model.assetType = self.model.categoryDropdown[0];
      self.model.categoryCode = "";
      self.model.categoryDescription = "";
      self.model.categoryId = undefined;
      self.model.activeCategoryId = null;
    }
    var clearSearch = function(){
      self.model.search = "";
    }

    

    var SetOldValueAssetType = function (assetType) {
      self.model.oldAssetType = assetType;
      self.model.assetType = self.model.oldAssetType;
    }

    var ChangeAssetType = function (assetType) {
      if (self.model.categoryCode || self.model.categoryDescription ) {
        self.model.assetType = self.model.oldAssetType;
        self.model.assetTypeNew = JSON.parse(JSON.stringify(assetType));
        $("#storeChange").modal()
      }
    }

    var ConfirmAssetTypePopup = function () {
      self.model.assetType = self.model.assetTypeNew;
      self.model.categoryCode = '';
      self.model.categoryDescription = '';
      ModalDismiss('storeChange');
    }

    var ModalDismiss = function (popupName) {
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
    }

    initializeController();
  }

  function config($stateProvider) {
      $stateProvider.state("assetCategoryMaster", {
      url: "/assetCategoryMaster",
      templateUrl: 'views/master/GlobalMaster/AssetManagenent/Master/asset-category-master.html',
      controller: "AssetCategoryMaster.Controller",
      title: 'Asset Category Master',
  	  resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 642);
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
    .controller("AssetCategoryMaster.Controller", assetCategoryMasterController)
})();