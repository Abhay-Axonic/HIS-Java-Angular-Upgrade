(function () {
  "use strict";
  function taxComponentMasterController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService,PaginationService, growl) {
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
      self.model.SaveTaxComponents = SaveTaxComponents;
      self.model.ClearFields = ClearFields;
      self.model.PopulateTaxComponent = PopulateTaxComponent;
      self.model.ValidateTaxComponent = ValidateTaxComponent;
      self.model.clearSearch = clearSearch;
      getTaxComponent();
    }

    var SaveTaxComponents = function () {
      if (ValidateTaxComponent()) {
        if (!self.model.id) {
          CreateTaxComponent();
        } else {
          updateTaxComponent();
        }
      }
    }

    //Method to get the list of Tax Component
    var getTaxComponent = function () {
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.TAX_COMPONENT_MASTER.TAX_COMPONENT_API +$scope.organizationId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.taxComponentList = response.data;
          if (!!response && !!response.data && response.data.length > 0) {
            PaginationService.searchData(self.model.taxComponentList, self.model.pagination);
          }
          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        });
    }

    //Method to populate Tax Component for editing
    function findInRecordInArray(array, key, value) {
      for (var i = 0; i < array.length; i++) {
        if (array[i][key] == value) {
          return array[i];
        }
      }
      return null;
    }

    function getTaxComponentById(id) {
      return findInRecordInArray(self.model.taxComponentList, 'id', id)
    }

    function PopulateTaxComponent(id) {
      self.model.istaxComponentCodeValid = false;
      self.model.isdescriptionValid = false;
      self.model.activeTaxComponentId = id;
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.TAX_COMPONENT_MASTER.TAX_COMPONENT_API+$scope.organizationId+"/" + id;
      var data = {};
      GenericService.serviceAction("GET", URI, data).then(
        function (response) {
          var taxComponent = getTaxComponentById(id);
          taxComponent = response.data;
          self.model.code = response.data.code;
          self.model.component = response.data.component;
          self.model.status = response.data.status;
          self.model.id = response.data.id;
          self.model.isEdit = true;
        });
    };

    //Method to activate or deactivate Tax Component
    var ToggleStatus = function (taxComponent) {

      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.TAX_COMPONENT_MASTER.TAX_COMPONENT_API +$scope.organizationId + "/" + taxComponent.id;

      var data = {
        "status": taxComponent.status
      };
      if (taxComponent.status == false) {
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
            taxComponent.status = true;
          }else{
         
          growl.error('Something Went Wrong');
        }
        });
        $rootScope.stopLoader();
    }

    var ValidateCode = function () {
      var isValid = true;

      self.model.taxComponentList.forEach(function (element) {
        if (element.code == self.model.code) {
          growl.error('Duplicate Tax Component code');
          isValid = false;
        }
      });
      if(!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.code)) {
        growl.error('Tax Component Code can only be alpha numeric with single hypen.');
        return;
     }
      return isValid;
    }

    var ValidateCodeForUpdate = function () {
      var isValid = true;

      self.model.taxComponentList.forEach(function (element) {
        if(element.id != self.model.activeTaxComponentId){
          if (element.code == self.model.code) {
            growl.error('Duplicate Tax Component code');
            isValid = false;
          }
        }
      });
      if(!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.code)) {
        growl.error('Tax Component Code can only be alpha numeric with single hypen.');
        return;
     }
      return isValid;
    }

    //Method to add the TaxComponent
    var CreateTaxComponent = function () {
      if(ValidateCode()){
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.TAX_COMPONENT_MASTER.TAX_COMPONENT_API +$scope.organizationId;
      var data = {
        "id": self.model.id,
        "code": self.model.code,
        "component": self.model.component,
        "status": true
      }
      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          var taxComponent = {
            "id": response.data.id,
            "code": self.model.code,
            "component": self.model.component,
            "status": true
          };
          self.model.taxComponentList.push(taxComponent);
          PaginationService.searchData(self.model.taxComponentList, self.model.pagination);
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

    //Method to update the Tax Component
    var updateTaxComponent = function () {
      if(ValidateCodeForUpdate()){
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.TAX_COMPONENT_MASTER.TAX_COMPONENT_API +$scope.organizationId + "/"+ self.model.id;

      var data = {
        "code": self.model.code,
        "component": self.model.component,
      }

       $rootScope.startLoader();
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          var taxComponent = self.model.taxComponentList.find(function (type) {
            return type.id == self.model.id;
          });
          taxComponent.code = data.code;
          taxComponent.component = data.component;

          ClearFields();
          $rootScope.stopLoader();
          growl.success(response.data.message);
          self.model.activeTaxComponentId = null;
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
      self.model.id = undefined;
      self.model.code = "";
      self.model.component = "";
      self.model.pagination.search = '';

    }

    var clearSearch = function(){
      self.model.pagination.search = '';
      PaginationService.searchData(self.model.taxComponentList, self.model.pagination);

    }

    var ValidateTaxComponent = function () {
      var isValid = true;
      if (!self.model.code) {
        isValid = false;
        self.model.istaxComponentCodeValid = true;
      }
      if (!self.model.component) {
        isValid = false;
        self.model.isdescriptionValid = true;
      }
      return isValid;
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("taxComponentMaster", {
      url: "/taxComponentMaster",
      templateUrl: 'views/master/GlobalMaster/tax-component-master.html',
      controller: "TaxComponents.Controller",
      title: 'Tax Component Master',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 473);
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
    .controller("TaxComponents.Controller", taxComponentMasterController)
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