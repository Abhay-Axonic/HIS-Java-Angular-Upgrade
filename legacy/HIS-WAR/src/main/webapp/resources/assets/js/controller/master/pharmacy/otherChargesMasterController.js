(function () {
  "use strict";
  function otherChargesMasterController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, PaginationService, growl) {
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
      self.model.SaveOtherCharges = SaveOtherCharges;
      self.model.ClearFields = ClearFields;
      self.model.PopulateOtherCharge = PopulateOtherCharge;
      self.model.ValidateOtherCharge = ValidateOtherCharge;
      self.model.clearSearch = clearSearch;
      getOtherCharge();
    }

    var SaveOtherCharges = function () {
      if (ValidateOtherCharge()) {
        if (!self.model.id) {
          CreateOtherCharge();
        } else {
          updateOtherCharge();
        }
      }
    }

    //Method to get the list of Other Charge
    var getOtherCharge = function () {
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.OTHER_CHARGES_MASTER.OTHER_CHARGES_API+$scope.organizationId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.OtherChargesList = response.data;
          if (!!response && !!response.data && response.data.length > 0) {
            PaginationService.searchData(self.model.OtherChargesList, self.model.pagination);
          }
          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        });
    }

    //Method to populate Other Charge for editing
    function findInRecordInArray(array, key, value) {
      for (var i = 0; i < array.length; i++) {
        if (array[i][key] == value) {
          return array[i];
        }
      }
      return null;
    }

    function getOtherChargeById(id) {
      return findInRecordInArray(self.model.OtherChargesList, 'id', id)
    }

    function PopulateOtherCharge(id) {
      self.model.isotherChargeCodeValid = false;
      self.model.isotherChargeDescriptionValid = false;
      self.model.activeOtherChargeId = id;
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.OTHER_CHARGES_MASTER.OTHER_CHARGES_API +$scope.organizationId + "/" + id;
      var data = {};
      GenericService.serviceAction("GET", URI, data).then(
        function (response) {
          var otherCharge = getOtherChargeById(id);
          otherCharge = response.data;
          self.model.code = response.data.code;
          self.model.description = response.data.description;
          self.model.status = response.data.status;
          self.model.id = response.data.id;
          self.model.isEdit = true;
        });
    };

    //Method to activate or deactivate Other Charge
    var ToggleStatus = function (othercharge) {
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.OTHER_CHARGES_MASTER.OTHER_CHARGES_API +$scope.organizationId + "/" + othercharge.id;

      var data = {
        "status": othercharge.status
      };
      if (othercharge.status == false) {
        ClearFields();
      }
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          growl.success('Status changed successfully');
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
          othercharge.status = !othercharge.status;
          growl.error('Something went wrong');
        });
    }

    var ValidatCode = function () {
      var isValid = true;

      self.model.OtherChargesList.forEach(function (element) {
        if (element.code == self.model.code) {
          growl.error('Duplicate Other Charges code');
          isValid = false;
        }
      });
      if(!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.code)) {
        growl.error('Other Charges Code can only be alpha numeric with single hypen.');
        return;
     }
      return isValid;
    }

    var ValidateCodeForUpdate = function () {
      var isValid = true;

      self.model.OtherChargesList.forEach(function (element) {
        if(element.id != self.model.activeOtherChargeId){
          if (element.code == self.model.code) {
            growl.error('Duplicate Other Charges code');
            isValid = false;
          }
        }
      });
      if(!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.code)) {
        growl.error('Other Charges Code can only be alpha numeric with single hypen.');
        return;
     }
      return isValid;
    }

    //Method to add the OtherCharge
    var CreateOtherCharge = function () {
      if(ValidatCode()){
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.OTHER_CHARGES_MASTER.OTHER_CHARGES_API+$scope.organizationId;
      var data = {
        "id": self.model.id,
        "code": self.model.code,
        "description": self.model.description,
        "status": true
      }
      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          var othercharge = {
            "id": response.data.id,
            "code": self.model.code,
            "description": self.model.description,
            "status": true
          };
          self.model.OtherChargesList.push(othercharge);
          PaginationService.searchData(self.model.OtherChargesList, self.model.pagination);
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

    //Method to update the Other Charge
    var updateOtherCharge = function () {
      if(ValidateCodeForUpdate()){
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.OTHER_CHARGES_MASTER.OTHER_CHARGES_API +$scope.organizationId +"/" + self.model.id;
      var data = {
        "code": self.model.code,
        "description": self.model.description,
      }
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          var othercharge = self.model.OtherChargesList.find(function (type) {
            return type.id == self.model.id;
          });
          othercharge.code = data.code;
          othercharge.description = data.description;

          ClearFields();  $rootScope.stopLoader();
          growl.success(response.data.message);
          self.model.activeOtherChargeId = null;
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
      self.model.description = "";
      self.model.id = undefined;
      self.model.activeOtherChargeId = undefined;
      self.model.pagination.search = ""

    }

    var clearSearch = function(){
      self.model.pagination.search = "";
      PaginationService.searchData(self.model.OtherChargesList, self.model.pagination);      
    }

    var ValidateOtherCharge = function () {
      var isValid = true;

      if (!self.model.code) {
        isValid = false;
        self.model.isotherChargeCodeValid = true;
      }
      if (!self.model.description) {
        isValid = false;
        self.model.isotherChargeDescriptionValid = true;
      }
      return isValid;
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("otherCharges", {
      url: "/otherCharges",
      templateUrl: "views/master/GlobalMaster/other-charges.html",
      controller: "OtherCharges.Controller",
      title: 'Other Charges Master',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 475);
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
    .controller("OtherCharges.Controller", otherChargesMasterController)
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