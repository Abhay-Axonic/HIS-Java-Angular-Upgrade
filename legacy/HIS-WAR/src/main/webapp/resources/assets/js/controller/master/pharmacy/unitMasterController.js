(function () {
  "use strict";
  function unitMasterController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService,PaginationService, growl){
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
      self.model.SaveUnits = SaveUnits;
      self.model.ClearFields = ClearFields;
      self.model.PopulateUnits = PopulateUnits;
      self.model.ValidateUnit = ValidateUnit;
      self.model.clearSearch = clearSearch;
      getUnits();
    }

    var SaveUnits = function () {
      if (ValidateUnit()) {
        if (!self.model.id) {
          CreateUnit();
        } else {
          updateUnit();
        }
      }
    }

    //Method to get the list of Unit
    var getUnits = function () {
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.UNIT_MASTER.UNIT_API +$scope.organizationId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.UnitList = response.data;
          if(!!response && !!response.data && response.data.length > 0){            
            PaginationService.searchData(self.model.UnitList, self.model.pagination);
          }
          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        });
    }

    //Method to populate Unit  for editing
    function findInRecordInArray(array, key, value) {
      for (var i = 0; i < array.length; i++) {
        if (array[i][key] == value) {
          return array[i];
        }
      }
      return null;
    }

    function getUnitById(id) {
      return findInRecordInArray(self.model.UnitList, 'id', id)
    }

    function PopulateUnits(id) {
      self.model.ispharmacyUnitCodeValid = false;
      self.model.ispharmacyDescriptionValid = false;
      self.model.activeUnitId = id;
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.UNIT_MASTER.UNIT_API +$scope.organizationId + "/"+ id;
      var data = {};
      GenericService.serviceAction("GET", URI, data).then(
        function (response) {
          var unit = getUnitById(id);
          unit = response.data;
          self.model.code = response.data.code;
          self.model.units = response.data.units;
          self.model.status = response.data.status;
          self.model.id = response.data.id;
          self.model.isEdit = true;
        });
    };

    //Method to activate or deactivate  Unit
    var ToggleStatus = function (unit) {
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.UNIT_MASTER.UNIT_API +$scope.organizationId + "/" + unit.id;

      var data = {
        "status": unit.status
      };
      if (unit.status == false) {
        ClearFields();
      }
      $rootScope.startLoader();
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          growl.success('Status changed successfully');
        }, function (err) {
          if (err.status == 406) {
            growl.error(err.data.message);
            unit.status = true;
          } else {

            growl.error('Something Went Wrong');
          }
        });
      $rootScope.stopLoader();
    }

    var ValidateUnitCode = function () {
      var isValid = true;

      self.model.UnitList.forEach(function (element) {
        if (element.code == self.model.code) {
          growl.error('Duplicate Unit code');
          isValid = false;
        }
      });
      if (!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.code)) {
        growl.error('Code can only be alpha numeric with single hypen.');
        return;
      }
      return isValid;
    }

    var ValidateUnitCodeForUpdate = function () {
      var isValid = true;

      self.model.UnitList.forEach(function (element) {
        if (element.id != self.model.activeUnitId) {
          if (element.code == self.model.code) {
            growl.error('Duplicate Unit code');
            isValid = false;
          }
        }
      });
      if (!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.code)) {
        growl.error('Code can only be alpha numeric with single hypen.');
        return;
      }
      return isValid;
    }

    //Method to add the unit
    var CreateUnit = function () {
      if (ValidateUnitCode()) {
        var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.UNIT_MASTER.UNIT_API +$scope.organizationId;
        var data = {
          "id": self.model.id,
          "code": self.model.code,
          "units": self.model.units,
          "status": true
        }
        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            var unit = {
              "id": response.data.id,
              "code": self.model.code,
              "units": self.model.units,
              "status": true
            };
            self.model.UnitList.push(unit);
            PaginationService.searchData(self.model.UnitList, self.model.pagination);
            PaginationService.setLastPage(self.model.pagination);
            ClearFields();

            $rootScope.stopLoader();
            growl.success('Record saved successfully');
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
            growl.error('Something went wrong');
          });
      }
    }

    //Method to update the  Unit
    var updateUnit = function () {
      if (ValidateUnitCodeForUpdate()) {
        var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.UNIT_MASTER.UNIT_API +$scope.organizationId + "/"+ self.model.id;
        var data = {
          "code": self.model.code,
          "units": self.model.units,
        }
        // $rootScope.startLoader();
        GenericService.serviceAction("PATCH", URI, data).then(
          function (response) {
            var unit = self.model.UnitList.find(function (type) {
              return type.id == self.model.id;
            });
            unit.code = data.code;
            unit.units = data.units;

            ClearFields();
            $rootScope.stopLoader();
            growl.success(response.data.message);
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
            growl.error('Something went wrong');
          });
      }
    }
  
    //Method to clear the fileds
    function ClearFields() {
      self.model.code = "";
      self.model.units = "";
      self.model.id = undefined;
      self.model.pagination.search = '';

    }
    var clearSearch = function () {
      self.model.pagination.search = '';
      PaginationService.searchData(self.model.UnitList, self.model.pagination);

    }

    var ValidateUnit = function () {
      var isValid = true;
      if (!self.model.code) {
        isValid = false;
        self.model.ispharmacyUnitCodeValid = true;
      }
      if (!self.model.units) {
        isValid = false;
        self.model.ispharmacyDescriptionValid = true;
      }
      return isValid;
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("pharmacyunitMaster", {
      url: "/pharmacyunitMaster",
      templateUrl: "views/PharmacyInventory/pharmacy-unit-master.html",
      controller: "UnitMaster.Controller",
      title: 'Clinical Unit Master',
  	  resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 467);
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
    .controller("UnitMaster.Controller", unitMasterController)
})();