(function () {
  "use strict";
  function pharmalogicalclassificationMasterController($scope, $rootScope, $http, $filter, $cookies, CONSTANTS, GenericService,PaginationService, growl) {
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
      self.model.SavePharmalogicalClassification = SavePharmalogicalClassification;
      self.model.ClearFields = ClearFields;
      self.model.PopulatePharmalogicalClassification = PopulatePharmalogicalClassification;
      self.model.ValidatePharmalogicalClassification = ValidatePharmalogicalClassification;
      self.model.clearSearch = clearSearch;
      self.model.searchPharmacological = searchPharmacological;
      getPharmalogicalClassification();
    }

    var SavePharmalogicalClassification = function () {
      if (ValidatePharmalogicalClassification()) {
        if (!self.model.id) {
          CreatePharmalogicalClassification();
        } else {
          updatePharmalogicalClassification();
        }
      }
    }

    //Method to get the list of Pharmalogical Classification
    var getPharmalogicalClassification = function () {
      var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.PHARMALOGICAL_CLASSIFICATION_MASTER.PHARMALOGICAL_CLASSIFICATION_API + "org/"+$scope.organizationId;
      
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.pharmalogicalClassificationList = response.data;
          if(!!response && !!response.data && response.data.length > 0){            
              searchPharmacological(self.model.pharmalogicalClassificationList, self.model.pagination);
          }
          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        });
    }

    //Method to populate Pharmalogical Classification for editing
    function findInRecordInArray(array, key, value) {
      for (var i = 0; i < array.length; i++) {
        if (array[i][key] == value) {
          return array[i];
        }
      }
      return null;
    }

    function getPharmalogicalClassificationById(id) {
      return findInRecordInArray(self.model.pharmalogicalClassificationList, 'id', id)
    }

    function PopulatePharmalogicalClassification(id) {
      self.model.isclassificationCodeValid = false;
      self.model.isclassificationValid = false;
      self.model.activeClassificationId = id;
      var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.PHARMALOGICAL_CLASSIFICATION_MASTER.PHARMALOGICAL_CLASSIFICATION_API + id;
      var data = {};
      GenericService.serviceAction("GET", URI, data).then(
        function (response) {
          var pharmalogicalClassification = getPharmalogicalClassificationById(id);
          pharmalogicalClassification = response.data;
          self.model.code = response.data.code;
          self.model.classification = response.data.classification;
          self.model.status = response.data.status;
          self.model.id = response.data.id;
          self.model.isEdit = true;
          self.model.antibioticTest = response.data.antibioticTest;
        });
    };

    //Method to activate or deactivate Pharmalogical Classification
    var ToggleStatus = function (pharmaClassification) {
      var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.PHARMALOGICAL_CLASSIFICATION_MASTER.PHARMALOGICAL_CLASSIFICATION_API + pharmaClassification.id;
      var data = {
        "status": pharmaClassification.status,
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId
      };
      if (pharmaClassification.status == false) {
        ClearFields();
      }
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          growl.success('Status changed successfully');
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
          pharmaClassification.status = !pharmaClassification.status;
          growl.error('Something went wrong');
        });
    }


    var ValidateCode = function () {
      var isValid = true;

      self.model.pharmalogicalClassificationList.forEach(function (element) {
        if (element.code == self.model.code) {
          growl.error('Duplicate Pharmalogical Classification code');
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

      self.model.pharmalogicalClassificationList.forEach(function (element) {
        if(element.id != self.model.activeClassificationId){
          if (element.code == self.model.code) {
            growl.error('Duplicate Pharmalogical Classification code');
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

    //Method to add the Pharmalogical Classification
    var CreatePharmalogicalClassification = function () {
      if(ValidateCode()){
      var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.PHARMALOGICAL_CLASSIFICATION_MASTER.PHARMALOGICAL_CLASSIFICATION_API;
      var data = {
        "code": self.model.code,
        "classification": self.model.classification,
        "status": true,
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId,
        "antibioticTest":self.model.antibioticTest
      }
      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          var pharmalogicalClassification = {
            "id": response.data.id,
            "code": self.model.code,
            "classification": self.model.classification,
            "status": true
          };
          self.model.pharmalogicalClassificationList.push(pharmalogicalClassification);
          searchPharmacological(self.model.pharmalogicalClassificationList, self.model.pagination);          PaginationService.setLastPage(self.model.pagination);
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
    var updatePharmalogicalClassification = function () {
      if(ValidateCodeForUpdate()){
      var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.PHARMALOGICAL_CLASSIFICATION_MASTER.PHARMALOGICAL_CLASSIFICATION_API + self.model.id;
      var data = {
        "code": self.model.code,
        "classification": self.model.classification,
        "status": true,
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId,
        "antibioticTest":self.model.antibioticTest
      }
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          var pharmalogicalClassification = self.model.pharmalogicalClassificationList.find(function (type) {
            return type.id == self.model.id;
          });
          pharmalogicalClassification.code = data.code;
          pharmalogicalClassification.classification = data.classification;
          pharmalogicalClassification.status = data.status;

          ClearFields();
          $rootScope.stopLoader();
          growl.success(response.data.message);
          self.model.activeClassificationId = null;
          self.model.isEdit = false;
        });
      }
    };

    //Method to clear the fileds
    function ClearFields() {
      self.model.code = "";
      self.model.classification = "";
      self.model.id = undefined;
      self.model.pagination.search = '';
      self.model.antibioticTest = false;

    }
    var clearSearch = function(){
      self.model.pagination.search = "";
      searchPharmacological(self.model.pharmalogicalClassificationList, self.model.pagination);
     }

    // Methode to Search the Pharmacological
        var searchPharmacological = function (pharmalogicalClassificationList, pagination) {
                var data = pharmalogicalClassificationList;
                var searchQuery = pagination.search.toLowerCase();

                if (searchQuery) {
                    pagination.filteredData = $filter('filter')(data, function (item) {
                        return (
        					(item.code != undefined ? item.code.toLowerCase().includes(searchQuery) : "") ||
        					(item.classification != undefined ? item.classification.toLowerCase().includes(searchQuery) : "")
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

    var ValidatePharmalogicalClassification = function () {
      var isValid = true;

      if (!self.model.code) {
        isValid = false;
        self.model.isclassificationCodeValid = true;
      }
      if (!self.model.classification) {
        isValid = false;
        self.model.isclassificationValid = true;
      }
      return isValid;
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("pharmacologicalClassificationMaster", {
      url: "/pharmacologicalClassificationMaster",
      templateUrl: 'views/PharmacyInventory/StockAdjustment/pharmacological-classification-master.html',
      controller: "PharmalogicalClassificationController.Controller",
      title: 'Pharmacological Classification Master',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 634);
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
    .controller("PharmalogicalClassificationController.Controller", pharmalogicalclassificationMasterController)
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