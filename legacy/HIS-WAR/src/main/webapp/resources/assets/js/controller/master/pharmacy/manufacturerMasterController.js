(function () {
  "use strict";
  function manufacturerMasterController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, PaginationService, growl) {
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
      self.model.SaveManufacturer = SaveManufacturer;
      self.model.ClearFields = ClearFields;
      self.model.PopulateManufacturer = PopulateManufacturer;
      self.model.ValidateManufacturer = ValidateManufacturer;
      self.model.detail_address = '';
      self.model.getAddress = getAddress;
      self.model.clearSearch = clearSearch;
      self.model.getPrefix =getPrefix;
      self.model.previousCode= null; 
      getManufacturer();
      getPrefix();
    }

    var SaveManufacturer = function () {
      if (ValidateManufacturer()) {
        if (!self.model.id) {
          CreateManufacturer();
        } else {
          updateManufacturer();
        }
      }
    }

    $scope.codeDisableFlag=true;
    //Method to get the list of Manufacturer
    var getManufacturer = function () {
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.MANUFACTURER_MASTER.MANUFATURER_API +$scope.organizationId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.manufactureDataList = response.data;

          if(!!response && !!response.data && response.data.length > 0){            
            PaginationService.searchData(self.model.manufactureDataList, self.model.pagination);
          }
          
          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        });
    }

    var getPrefix = function()
    {

        var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.MANUFACTURER_MASTER.MANUFATURER_PREFIX_API +$scope.organizationId+"/"+177;

        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            
            if(!!response && !!response.data ){            
            	 //self.model.code= response.data.code; 
            	 self.model.previousCode= response.data.code; 
            }
            
            $rootScope.stopLoader();
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
          });
      
    	
    }
    //Method to populate Manufacturer for editing
    function findInRecordInArray(array, key, value) {
      for (var i = 0; i < array.length; i++) {
        if (array[i][key] == value) {
          return array[i];
        }
      }
      return null;
    }

    function getManufacturerById(id) {
      return findInRecordInArray(self.model.manufactureDataList, 'id', id)
    }

    function PopulateManufacturer(id) {
      $rootScope.startLoader();
      self.model.ismanufacturerCodeValid = false;
      self.model.ismanufacturerValid = false;
      self.model.isaddressValid = false;
      self.model.activeManufacturerId = id;
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.MANUFACTURER_MASTER.MANUFATURER_API +$scope.organizationId + "/" + id;
      var data = {};
      GenericService.serviceAction("GET", URI, data).then(
        function (response) {
          var manufacture = getManufacturerById(id);
          manufacture = response.data;
          self.model.code = response.data.code;
          self.model.name = response.data.name;
          self.model.address = response.data.address;
          self.model.id = response.data.id;
          self.model.status = response.data.status;
          self.model.isEdit = true;
          $scope.codeDisableFlag=false;
          
          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        });
    };

    var getAddress = function(manufacture){
      self.model.detail_address = manufacture.address;
      
    }

    //Method to activate or deactivate Manufacturer
    var ToggleStatus = function (manufacture) {
      $rootScope.startLoader();
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.MANUFACTURER_MASTER.MANUFATURER_API +$scope.organizationId + "/"+ manufacture.id;
      var data = {
        "status": manufacture.status
      };
      if (manufacture.status == false) {
        ClearFields();
      }
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          growl.success('Status changed successfully');
        }, function (err) {
          if(err.status == 406){
            growl.error(err.data.message);
            manufacture.status = true;
          } else{         
            growl.error('Something Went Wrong');
          }          
          $rootScope.stopLoader();
        });
    }

    var ValidateCode = function () {
      var isValid = true;

      self.model.manufactureDataList.forEach(function (element) {
        if (element.code == self.model.code) {
          growl.error('Duplicate Manufacture code');
          isValid = false;
        }
      });
      /*if(!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.code)) {
        growl.error('Manufacture Code can only be alpha numeric with single hypen.');
        return;
     }*/
      return isValid;
    }

    var ValidateCodeForUpdate = function () {
      var isValid = true;

      self.model.manufactureDataList.forEach(function (element) {
        if(element.id != self.model.activeManufacturerId){
          if (element.code == self.model.code) {
            growl.error('Duplicate Manufacture code');
            isValid = false;
          }
        }
      });
     /* if(!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.code)) {
        growl.error('Manufacture Code can only be alpha numeric with single hypen.');
        return;
     }*/
      return isValid;
    }

    //Method to add the Manufacturer
    var CreateManufacturer = function () {
      if(ValidateCode()){
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.MANUFACTURER_MASTER.MANUFATURER_API+$scope.organizationId;

      var data = {
        "name": self.model.name,
        "code": self.model.code,
        "address": self.model.address,
        "status": true,
      }

      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          var manufacture = {
            "id": response.data.id,
            "name": self.model.name,
            "code": self.model.code,
            "address": self.model.address,
            "status": true
          };
          self.model.manufactureDataList.push(manufacture);
          PaginationService.searchData(self.model.manufactureDataList, self.model.pagination);
          PaginationService.setLastPage(self.model.pagination);
          ClearFields();
          getPrefix();
          $scope.codeDisableFlag=true;
          $rootScope.stopLoader();
          growl.success(response.data.message);
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
          growl.error('Something went wrong');
        });
      }
    };

    //Method to update the Manufacturer
    var updateManufacturer = function () {
      if(ValidateCodeForUpdate()){
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.MANUFACTURER_MASTER.MANUFATURER_API +$scope.organizationId+ "/" + self.model.id;

      var data = {
        "name": self.model.name,
        "code": self.model.code,
        "address": self.model.address,
      }

      $rootScope.startLoader();
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          var manufacture = self.model.manufactureDataList.find(function (type) {
            return type.id == self.model.id;
          });
          manufacture.name = data.name;
          manufacture.code = data.code;
          self.model.detail_address = data.address;

          ClearFields();
          getManufacturer();
	      if(self.model.previousCode!=undefined)
	    	  {
	    	   self.model.code = self.model.previousCode;
	    	  }
	      else{
	    	  getPrefix();
	        }
          $rootScope.stopLoader();
          growl.success(response.data.message);
          self.model.activeManufacturerId = null;
          self.model.isEdit = false;
          $scope.codeDisableFlag=true;
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
      self.model.name = "";
      self.model.address = "";
      self.model.id = undefined;
    }

    var clearSearch = function(){
      self.model.pagination.search = "";
      PaginationService.searchData(self.model.manufactureDataList, self.model.pagination);
    }
    
    var ValidateManufacturer = function () {
      var isValid = true;

      if (!self.model.code) {
        isValid = false;
        self.model.ismanufacturerCodeValid = true;
      }
      if (!self.model.name) {
        isValid = false;
        self.model.ismanufacturerValid = true;
      }
      if (!self.model.address) {
        isValid = false;
        self.model.isaddressValid = true;
      }
      return isValid;
    }
    $scope.limitKeypress = function ($event, value, maxLength) {
      
      if (value != undefined && value.toString().length >= maxLength) {
          $event.preventDefault();
      }
  }
    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("manufacturerMaster", {
      url: "/manufacturerMaster",
      templateUrl: "views/master/GlobalMaster/manufacturer-master.html",
      controller: "ManufacturerMaster.Controller",
      title: 'Manufacturer Master',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 461);
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
    .controller("ManufacturerMaster.Controller", manufacturerMasterController)
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