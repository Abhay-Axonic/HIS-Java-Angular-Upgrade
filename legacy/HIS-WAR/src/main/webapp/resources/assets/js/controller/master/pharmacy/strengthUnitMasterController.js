(function () {
  "use strict";
  function strengthUnitMasterController($scope, $rootScope, $http, $filter, $cookies, CONSTANTS, GenericService, PaginationService, growl) {
    var self = this;
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
      self.model.SaveStrengthUnits = SaveStrengthUnits;
      self.model.ClearFields = ClearFields;
      self.model.PopulateStrengthUnits = PopulateStrengthUnits;
      self.model.ValidateStrengthUnit = ValidateStrengthUnit;
      self.model.searchStrength = searchStrength;
      self.model.validUpdate = false;
      self.model.clearSearch = clearSearch;
      getStrengthUnits();
    }

    var SaveStrengthUnits = function () {
      if (ValidateStrengthUnit()) {
        if (!self.model.id) {
          CreateStrengthUnit();
        } else {
          updateStrengthUnit();
        }
      }
    }

    //Method to get the list of Strength Unit
    var getStrengthUnits = function () {
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.STRENGTH_UNIT_MASTER.STRENGTH_UNIT_API+$scope.organizationId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.UnitList = response.data;
          if (!!response && !!response.data && response.data.length > 0) {
              searchStrength(self.model.UnitList, self.model.pagination);
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

    function getStrengthUnitById(id) {
      return findInRecordInArray(self.model.UnitList, 'id', id)
    }

    function PopulateStrengthUnits(id) {
      self.model.isunitCodeValid = false;
      self.model.isunitDescriptionValid = false;
      self.model.activeStrengthUnitId = id;
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.STRENGTH_UNIT_MASTER.STRENGTH_UNIT_API +$scope.organizationId  + "/"+ id;
      var data = {};
      GenericService.serviceAction("GET", URI, data).then(
        function (response) {
          var unit = getStrengthUnitById(id);
          unit = response.data;
          self.model.code = response.data.code;
          self.model.units = response.data.unit;
          self.model.id = response.data.id;
          self.model.isEdit = true;
        });
    };

    //Method to activate or deactivate  Unit
    var ToggleStatus = function (unit) {
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.STRENGTH_UNIT_MASTER.STRENGTH_UNIT_API +$scope.organizationId  + "/"+ unit.id;

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

    //Method to add the unit
    var CreateStrengthUnit = function () {
      if (ValidateUnitCode()) {
        var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.STRENGTH_UNIT_MASTER.STRENGTH_UNIT_API + "/" + $scope.organizationId;
        var data = {
          "id": self.model.id,
          "code": self.model.code,
          "unit": self.model.units,
          "organizationId":$scope.organizationId,
          "unitId":$scope.unitId,
          "userId":$scope.userId,
//          "storeId":$scope.storeId,
          "status": true
        }
        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            var unit = {
              "id": response.data.id,
              "code": self.model.code,
              "unit": self.model.units,
              "status": true
            };
            self.model.UnitList.push(unit);
            searchStrength(self.model.UnitList, self.model.pagination);
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

    //Method to update the  Unit
    var updateStrengthUnit = function () {
      if (ValidateUnitCodeForUpdate()) {
        var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.STRENGTH_UNIT_MASTER.STRENGTH_UNIT_API +$scope.organizationId + "/" + self.model.id;
        var data = {
          "code": self.model.code,
          "unit": self.model.units,
        }
        // $rootScope.startLoader();
        GenericService.serviceAction("PATCH", URI, data).then(
          function (response) {
            var unit = self.model.UnitList.find(function (type) {
              return type.id == self.model.id;
            });
            unit.code = data.code;
            unit.unit = data.unit;

            ClearFields();
            $rootScope.stopLoader();
            growl.success(response.data.message);
            self.model.activeStrengthUnitId = null;
            self.model.isEdit = false;
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
            growl.error('Something went wrong');
          });
      }
    };

    //Method to clear the fileds
    function ClearFields() {
      self.model.code = "";
      self.model.units = "";
      self.model.id = undefined;
      self.model.pagination.search = '';

    }
    var clearSearch = function () {
      self.model.pagination.search = '';
      searchStrength(self.model.UnitList, self.model.pagination);
    }

    // Methode to Search the Strength
        var searchStrength = function (UnitList, pagination) {
                var data = UnitList;
                var searchQuery = pagination.search.toLowerCase();

                if (searchQuery) {
                    pagination.filteredData = $filter('filter')(data, function (item) {
                        return (

        					(item.code != undefined ? item.code.toLowerCase().includes(searchQuery) : "") ||
        					(item.unit != undefined ? item.unit.toLowerCase().includes(searchQuery) : "")
                        );
                    });
                } else {
                    pagination.filteredData = data;
                }

                setTotalPages(pagination);
            }


        var setTotalPages = function (pagination) {
                pagination.currentPage = 1;
                pagination.totalPages = Math.ceil(pagination.filteredData.length / parseInt(pagination.pageSize, 10));
               return setPagedData(pagination);
        }

        var setPagedData = function (pagination) {
              var limit = parseInt(pagination.pageSize, 10);
              var offset = (pagination.currentPage - 1) * limit;
              pagination.data = pagination.filteredData.slice(offset, offset + limit);
        }

    var ValidateStrengthUnit = function () {
      var isValid = true;
      if (!self.model.code) {
        isValid = false;
        self.model.isunitCodeValid = true;
      }
      if (!self.model.units) {
        isValid = false;
        self.model.isunitDescriptionValid = true;
      }
      return isValid;
    }

    var ValidateUnitCode = function () {
      var isValid = true;

      self.model.UnitList.forEach(function (element) {
        if (element.code == self.model.code) {
          growl.error('Duplicate Strength code');
          isValid = false;
        }
      });
      if (!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.code)) {
        growl.error('Strength Code can only be alpha numeric with single hypen.');
        return;
      }
      return isValid;
    }

    var ValidateUnitCodeForUpdate = function () {
      var isValid = true;

      self.model.UnitList.forEach(function (element) {
        if (element.id != self.model.activeStrengthUnitId) {
          if (element.code == self.model.code) {
            growl.error('Duplicate Strength code');
            isValid = false;
          }
        }
      });
      if (!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.code)) {
        growl.error('Strength Code can only be alpha numeric with single hypen.');
        return;
      }
      return isValid;
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("pharmacyStrengthUnitMaster", {
      url: "/pharmacyStrengthUnitMaster",
      templateUrl: "views/PharmacyInventory/pharmacy-strength-unit-master.html",
      controller: "StrengthUnitMaster.Controller",
      title: 'Strength Clinical Unit Master',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 468);
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
    .controller("StrengthUnitMaster.Controller", strengthUnitMasterController)
})();