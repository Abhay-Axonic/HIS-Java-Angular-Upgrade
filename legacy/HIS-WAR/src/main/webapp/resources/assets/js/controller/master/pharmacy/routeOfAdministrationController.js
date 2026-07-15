(function () {
    "use strict";
    function routeOfAdministrationController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, growl ,PaginationService) {
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
        self.model.PopulateRouteOfAdministration = populateRouteOfAdministration;
        self.model.SaveRouteOfAdministration = saveRouteOfAdministration;
        self.model.ClearFields = clearFields;
        self.model.ToggleStatus = ToggleStatus;
        self.model.clearSearch = clearSearch;
        self.model.ValidateROA = ValidateROA;
    
        getRouteOfAdministrationData();
      }   
  
      //Method to get the list of administration Rout
      function getRouteOfAdministrationData() {
        var URI = CONSTANTS.MASTER.GLOBAL.CLINICAL.ROUTE_OF_ADMINSTRATION_API+$scope.organizationId;
        var data = {};
        GenericService.serviceAction("GET", URI, data).then(
          function (response){
        	  self.model.pagination.data=[];
            self.model.routeOfAdministrationDataList = response.data;
            
            if(!!response && !!response.data && response.data.length > 0){            
                PaginationService.searchData(self.model.routeOfAdministrationDataList, self.model.pagination);
              }
          });
      };
  
      //Method to populate administration Rout for editing
      function findInRecordInArray(array, key, value) {
        for(var i=0; i<array.length; i++){
          if(array[i][key] == value){
            return array[i];
          }
        }
        return null;
      }
      function getRouteOfAdministrationById (administrationRoutId) {
        return findInRecordInArray (self.model.routeOfAdministrationDataList, 'administrationRoutId', administrationRoutId)
      }

      //Method to populate
      function populateRouteOfAdministration (administrationRout) {
          self.model.activeadministrationRoutId = administrationRout.id;
          var URI = CONSTANTS.MASTER.GLOBAL.CLINICAL.ROUTE_OF_ADMINSTRATION_API +$scope.organizationId +"/"+ administrationRout.id;
          var data = {};
          GenericService.serviceAction("GET", URI, data).then(
            function (response){
                administrationRout = response.data; 
                self.model.code = response.data.code;
                self.model.description = response.data.description;
                self.model.administrationRoutId = response.data.id;
                self.model.isEdit = true;
                
              });
          };
  
    var ToggleStatus = function (administrationRout) {
      var URI = CONSTANTS.MASTER.GLOBAL.CLINICAL.ROUTE_OF_ADMINSTRATION_API +$scope.organizationId + "/"+ administrationRout.id;

      var data = {
        "status": administrationRout.status
      };
      if (administrationRout.status == false) {
        clearFields();
      }
      $rootScope.startLoader();
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          growl.success('Status changed successfully');
        }, function (err) {
          if(err.status == 406){
            growl.error(err.data.message);
            administrationRout.status = true;
          }else{
         
          growl.error('Something Went Wrong');
        }
        });
        $rootScope.stopLoader();
    }
  
    var ValidateCode = function () {
      var isValid = true;

      self.model.routeOfAdministrationDataList.forEach(function (element) {
        if (element.code == self.model.code) {
          growl.error('Duplicate Route Of Administration code');
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

      self.model.routeOfAdministrationDataList.forEach(function (element) {
        if(element.id != self.model.activeadministrationRoutId){
          if (element.code == self.model.code) {
            growl.error('Duplicate Route Of Administration code');
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

      //Method to add or update the RouteOfAdministration 
      function saveRouteOfAdministration () {
        if(ValidateROA()){
        if(self.model.isEdit) {
          updateRouteOfAdministration();
        } else{
          createRouteOfAdministration();
        }
      }
      };
  
      //Method to add the RouteOfAdministration 
      function createRouteOfAdministration () {
        if(ValidateCode()){
        var URI = CONSTANTS.MASTER.GLOBAL.CLINICAL.ROUTE_OF_ADMINSTRATION_API +$scope.organizationId + "/"+ $scope.userId;
        var  data = {
          "code": self.model.code,
          "description": self.model.description,
          "status": true
      }
      $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response){
            var insertedData = {
              "id": response.data.id,
              "code": self.model.code,
              "description": self.model.description,
              "status": true
            };
            self.model.routeOfAdministrationDataList.push(insertedData);
            clearFields();
            $rootScope.stopLoader();
            growl.success(response.data.message);
            getRouteOfAdministrationData();
          }, 
           function errorCallback(response) {
            $rootScope.stopLoader();
            growl.error('Something went wrong');
          });
        }
      };
  
      //Method to update the RouteOfAdministration type
      function updateRouteOfAdministration () {
        if(ValidateCodeForUpdate()){
        var URI = CONSTANTS.MASTER.GLOBAL.CLINICAL.ROUTE_OF_ADMINSTRATION_API +$scope.organizationId+ "/" + self.model.administrationRoutId;
        var administrationRout = getRouteOfAdministrationById(self.model.activeadministrationRoutId);
        var data = {
          "code": self.model.code,
          "description": self.model.description,
          "status": true
        }
        GenericService.serviceAction("PATCH", URI, data).then(
          function (response){
            var administrationRout = self.model.routeOfAdministrationDataList.find(function (type) {
              return type.id == self.model.activeadministrationRoutId;
            });
          
            administrationRout.description = data.description;
            administrationRout.code = 
            data.code;  
            administrationRout.status = data.status;
            clearFields();
            $rootScope.stopLoader();
            growl.success(response.data.message);
            self.model.activeadministrationRoutId = null;
            self.model.isEdit = false;
        });
      }
      };
  
      //Method to clear the fileds
      function clearFields() {
        self.model.description = "";
        self.model.code = "";
      }
      var clearSearch = function(){
        self.model.search = "";
      }
      var ValidateROA = function () {
        var isValid = true;
        if (!self.model.code) {
          isValid = false;
          self.model.isCodeValid = true;
        }
        if (!self.model.description) {
          isValid = false;
          self.model.isDescriptionValid = true;
        }
        return isValid;
      }
      initializeController();
    }
    function config($stateProvider) {
      $stateProvider.state("routeAdministration", {
        url: "/routeAdministration",
        templateUrl: "views/master/GlobalMaster/AssetManagenent/Master/route-of-adminstration.html",
        controller: "RouteOfAdministration.Controller",
        title: 'Route Of Administration',
    	resolve : {

   			data: function($q, $state, $timeout) {
   		        var deferred = $q.defer();
   		
   		        $timeout(function() {
   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
   				 	.modules.filter(function(item){
   	            	   return (item.id == 477);
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
      .controller("RouteOfAdministration.Controller", routeOfAdministrationController)  
  })();
  
  
