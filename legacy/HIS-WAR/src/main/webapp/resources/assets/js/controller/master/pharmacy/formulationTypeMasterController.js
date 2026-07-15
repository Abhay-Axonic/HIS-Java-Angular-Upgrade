(function () {
  "use strict";
  function formulationTypeMasterController($scope,$rootScope, $http, $filter, $cookies, CONSTANTS, GenericService,PaginationService, growl) {
    var self = this;
    $rootScope.loginpage = true;
    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
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
      self.model.PopulateFormulationType = populateFormulationType;
      self.model.ToggleStatus = ToggleStatus;
      self.model.SaveFormulationType = saveFormulationType;
      self.model.ClearFields = ClearFields;
      self.model.ValidateFormulationMaster = validateFormulationMaster;
      self.model.clearSearch = clearSearch;
      self.model.searchFormulation = searchFormulation;
        getFormulationTypeData();
    }

    

    //Method to get the list of Formulation Type
    function getFormulationTypeData() {
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.FORMULATION_TYPE.FORMULATION_TYPE_API + $scope.organizationId;
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.formulationTypeDataList = response.data;

          if(!!response && !!response.data && response.data.length > 0){            
//            PaginationService.searchData(self.model.formulationTypeDataList, self.model.pagination);
            searchFormulation(self.model.formulationTypeDataList, self.model.pagination);
          }
          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        });
    };

    //Method to populate formulation type for editing
    function populateFormulationType(formulationTypeId) {
      self.model.isValidFormulationTypeCode = false;
      self.model.isValidFormulationType = false;
      self.model.activeFormulationTypeId = formulationTypeId;
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.FORMULATION_TYPE.FORMULATION_TYPE_API + $scope.organizationId + "/" + formulationTypeId;
      var data = {};
      GenericService.serviceAction("GET", URI, data).then(
        function (response) {
          self.model.formulationTypeCode = response.data.code;
          self.model.formulationTypeName = response.data.type;
          self.model.formulationTypeId = response.data.id;
          self.model.status = response.data.status;
          self.model.isEdit = true;
        });
    };

    //Method to activate or deactivate formulation type
    function ToggleStatus(formulationType) {
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.FORMULATION_TYPE.FORMULATION_TYPE_API + "/" + formulationType.id;
      var data = {
        "status": formulationType.status,
        "organizationId":$scope.organizationId,
      };
      if (formulationType.status == false) {
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
            formulationType.status = true;
          }else{
         
          growl.error('Something Went Wrong');
        }
        });
        $rootScope.stopLoader();
    };

    var ValidateCode = function () {
      var isValid = true;

      self.model.formulationTypeDataList.forEach(function (element) {
        if (element.code == self.model.formulationTypeCode) {
          growl.error('Duplicate formulation type code');
          isValid = false;
        }
      });
      if(!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.formulationTypeCode)) {
        growl.error('Code can only be alpha numeric with single hypen.');
        return;
     }
      return isValid;
    }

    var ValidateCodeForUpdate = function () {
      var isValid = true;

      self.model.formulationTypeDataList.forEach(function (element) {
        if(element.id != self.model.activeFormulationTypeId){
          if (element.code == self.model.formulationTypeCode) {
            growl.error('Duplicate formulation type code');
            isValid = false;
          }
        }
      });
      if(!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.formulationTypeCode)) {
        growl.error('Code can only be alpha numeric with single hypen.');
        return;
     }
      return isValid;
    }

    //Method to add or update the formulation type
    function saveFormulationType() {
      if(validateFormulationMaster()){
      if (!self.model.formulationTypeId) {
        createFormulationType();
       
      } else {
        updateFormulationType();
      }
    }
    };

    //Method to add the formulation type
    function createFormulationType() {
      if(ValidateCode()){
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.FORMULATION_TYPE.FORMULATION_TYPE_API;
      var data = {
        "code": self.model.formulationTypeCode,
        "type": self.model.formulationTypeName,
        "status": true,
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId
      }
      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
/*          var insertedData = {
            "id": response.data.id,
            "type": self.model.formulationTypeName,
            "code": self.model.formulationTypeCode,
            "status": true
          };
          self.model.formulationTypeDataList.push(insertedData);
//          PaginationService.searchData(self.model.formulationTypeDataList, self.model.pagination);
          searchFormulation(self.model.formulationTypeDataList, self.model.pagination);
          PaginationService.setLastPage(self.model.pagination);
*/
          ClearFields();
          getFormulationTypeData();
          $rootScope.stopLoader();
          growl.success("Formulation Type Inserted Successfully");
        }, function errorCallback(response) {
          $rootScope.stopLoader();
          console.log(response.data || 'Request Failed');
        });
      }
    };

    //Method to update the formulation type
    function updateFormulationType() {
      if(ValidateCodeForUpdate()){
      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.FORMULATION_TYPE.FORMULATION_TYPE_API  + self.model.formulationTypeId;
      var data = {
        "type": self.model.formulationTypeName,
        "code": self.model.formulationTypeCode,
        "status": true,
        "organizationId":$scope.organizationId
      }
      $rootScope.startLoader();
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
         var formulationData = self.model.formulationTypeDataList.find(function (type) {
          return type.id == self.model.formulationTypeId;
        });
        formulationData.type = data.type;
        formulationData.code = data.code;
        formulationData.status = data.status;
        ClearFields();
        getFormulationTypeData();
        $rootScope.stopLoader();
        growl.success("Formulation Type Updated Successfully");
          //self.model.activeFormulationTypeId = null;
          self.model.isEdit = false;
        });
      }
    };

    //Method to clear the fileds
    function ClearFields() {
      self.model.formulationTypeName = "";
      self.model.formulationTypeCode = "";
      self.model.activeFormulationTypeId = undefined;
      self.model.formulationTypeId = undefined;
      self.model.pagination.search = "";

    }

    var clearSearch = function(){
      self.model.pagination.search = "";
//      PaginationService.searchData(self.model.formulationTypeDataList, self.model.pagination);
      searchFormulation(self.model.formulationTypeDataList, self.model.pagination);
    }

    // Methode to Search the Formulation
        var searchFormulation = function (formulationTypeDataList, pagination) {
                var data = formulationTypeDataList;
                var searchQuery = pagination.search.toLowerCase();

                if (searchQuery) {
                    pagination.filteredData = $filter('filter')(data, function (item) {
                        return (
                            (item.code != undefined ? item.code.toLowerCase().includes(searchQuery) : "") ||
        					(item.type != undefined ? item.type.toLowerCase().includes(searchQuery) : "") ||
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

    var validateFormulationMaster = function () {
      var isValid = true;
      if (!self.model.formulationTypeCode) {
        isValid = false;
        self.model.isValidFormulationTypeCode = true;
      }
      if (!self.model.formulationTypeName) {
        isValid = false;
        self.model.isValidFormulationType = true;
      }
      return isValid;
    }
    initializeController();
  }
  function config($stateProvider) {
    $stateProvider.state("pharmacyformulationTypeMaster", {
      url: "/pharmacyformulationTypeMaster",
      templateUrl: "views/master/GlobalMaster/pharmacy-formulation-type-master.html",
      controller: "FormulationTypeMaster.Controller",
      title: 'Formulation Type Master',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 459);
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
    .controller("FormulationTypeMaster.Controller", formulationTypeMasterController);
})();


