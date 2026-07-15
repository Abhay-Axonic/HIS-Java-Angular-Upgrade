(function () {
  "use strict";
  function storageUnitMasterController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService,PaginationService, growl) {
    var self = this;
    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
      $rootScope.loginpage = true;
      self.model = {
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
      //Methods 
      self.model.ToggleStatus = ToggleStatus;
      self.model.SaveStorageUnits = SaveStorageUnits;
      self.model.ClearFields = ClearFields;
      self.model.PopulateStorageUnit = PopulateStorageUnit;
      self.model.ValidateStorageUnit = ValidateStorageUnit;
      self.model.clearSearch=clearSearch;
      getStorageUnit();

    }

    var SaveStorageUnits = function () {
      if (ValidateStorageUnit()) {
        if (!self.model.id) {
          CreateStorageUnit();
        } else {
          updateStorageUnit();
        }
      }
    }

    //Method to get the list of Storage Unit
    var getStorageUnit = function () {
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.STORAGE_UNIT_MASTER.STORAGE_UNIT_API +$scope.organizationId;
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.storageUnitList = response.data;
          if (!!response && !!response.data && response.data.length > 0) {
            PaginationService.searchData(self.model.storageUnitList, self.model.pagination);
          }
          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        });
    }

    //Method to populate Storage Unit for editing
    function findInRecordInArray(array, key, value) {
      for (var i = 0; i < array.length; i++) {
        if (array[i][key] == value) {
          return array[i];
        }
      }
      return null;
    }

    function getStorageUnitById(id) {
      return findInRecordInArray(self.model.storageUnitList, 'id', id)
    }

    function PopulateStorageUnit(id) {
      self.model.isstorageUnitCodeValid = false;
      self.model.isstorageUnitValid = false;
      self.model.activeStorageUnitId = id;
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.STORAGE_UNIT_MASTER.STORAGE_UNIT_API +$scope.organizationId +"/" + id;
      var data = {};
      GenericService.serviceAction("GET", URI, data).then(
        function (response) {
          var storageUnit = getStorageUnitById(id);
          storageUnit = response.data;
          self.model.code = response.data.code;
          self.model.unitName = response.data.unitName;
          self.model.status = response.data.status;
          self.model.id = response.data.id;
          self.model.isEdit = true;
        });
    };

    //Method to activate or deactivate Storage Unit
    var ToggleStatus = function (storageUnit) {
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.STORAGE_UNIT_MASTER.STORAGE_UNIT_API +$scope.organizationId + "/"+ storageUnit.id;
      var data = {
        "status": storageUnit.status
      };
      if (storageUnit.status == false) {
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
            storageUnit.status = true;
          }else{
         
          growl.error('Something Went Wrong');
        }
        });
        $rootScope.stopLoader();
    }

    var ValidateCode = function () {
      var isValid = true;

      self.model.storageUnitList.forEach(function (element) {
        if (element.code == self.model.code) {
          growl.error('Duplicate code');
          isValid = false;
        }
      });
      if(!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.code)) {
        growl.error('Code can only be alpha numeric with single hypen.');
        return;
     }
      return isValid;
    }

    var ValidateCodeForUpdate = function () {
      var isValid = true;

      self.model.storageUnitList.forEach(function (element) {
        if(element.id != self.model.activeStorageUnitId){
          if (element.code == self.model.code) {
            growl.error('Duplicate code');
            isValid = false;
          }
        }
      });
      if(!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.code)) {
        growl.error('Code can only be alpha numeric with single hypen.');
        return;
     }
      return isValid;
    }

    //Method to add the StorageUnit
    var CreateStorageUnit = function () {
      if(ValidateCode()){
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.STORAGE_UNIT_MASTER.STORAGE_UNIT_API +$scope.organizationId;
      var data = {
        "id": self.model.id,
        "code": self.model.code,
        "unitName": self.model.unitName,
        "status": true
      }
      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          var storageUnit = {
            "id": response.data.id,
            "code": self.model.code,
            "unitName": self.model.unitName,
            "status": true
          };
          self.model.storageUnitList.push(storageUnit);
          PaginationService.searchData(self.model.storageUnitList, self.model.pagination);
          PaginationService.setLastPage(self.model.pagination);
          ClearFields();

          $rootScope.stopLoader();
          growl.success(response.data.message);
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
          growl.error('Something went wrong');
        });
      }
    };

    //Method to update the Storage Unit
    var updateStorageUnit = function () {
      if(ValidateCodeForUpdate()){
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.STORAGE_UNIT_MASTER.STORAGE_UNIT_API +$scope.organizationId + "/" + self.model.id;
      var data = {
        "code": self.model.code,
        "unitName": self.model.unitName,
      }
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          var storageUnit = self.model.storageUnitList.find(function (type) {
            return type.id == self.model.id;
          });
          storageUnit.code = data.code;
          storageUnit.unitName = data.unitName;

          ClearFields();
          growl.success(response.data.message);
          self.model.activeStorageUnitId = null;
          self.model.isEdit = false;
        });
      }
    };

    //Method to clear the fileds
    function ClearFields() {
      self.model.code = "";
      self.model.search = '';
      self.model.unitName = "";
      self.model.id = undefined;
      self.model.isstorageUnitCodeValid = false;
      self.model.isstorageUnitValid = false;
      self.model.pagination.search = '';
    }

    var clearSearch = function(){
      self.model.pagination.search = '';
      PaginationService.searchData(self.model.UnitList, self.model.pagination);
    }

    var ValidateStorageUnit = function () {
      var isValid = true;

      if (!self.model.code) {
        isValid = false;
        self.model.isstorageUnitCodeValid = true;
      }
      if (!self.model.unitName) {
        isValid = false;
        self.model.isstorageUnitValid = true;
      }
      return isValid;
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("storageunitMaster", {
      url: "/storageunitMaster",
      templateUrl: 'views/PharmacyInventory/storage-unit-master.html',
      controller: "StorageController.Controller",
      title: 'Storage Unit Master',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 470);
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
      controllerAs: "vm",
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("StorageController.Controller", storageUnitMasterController)
    .directive('showFocus', function ($timeout) {
      return function (scope, element, attrs) {
        scope.$watch(attrs.showFocus,
          function (newValue, oldValue) {
            $timeout(function () {
              (newValue != oldValue) && element.focus();
            });
          }, true);
      };
    });
})();