(function() {
  "use strict";
  function mainstoreMasterController(
    $scope,
    $rootScope,
    $http,
    CONSTANTS,
    GenericService,
    PaginationService,
    growl,
    $cookies
  ) {
    var self = this;
    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
      $rootScope.loginpage = true;
      self.model = {
        isLinen: false,
        isPharmacy: false,
        isNarcotic: false,
        isCSSD: false,
        isLaundry: false,
        isMaintenance: false,
        storeTypes: [
          {
            type: "Select Store Type"
          },
          
        ],
        visitTypes: null,
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
      self.model.SaveMainStore = SaveMainStore;
      self.model.ClearFields = ClearFields;
      self.model.PopulateMainStore = PopulateMainStore;
      self.model.ValidateMainStore = ValidateMainStore;
      self.model.PopulateStoreType = PopulateStoreType;
      self.model.clearSearch = clearSearch;
      getMainStore();
      PopulateStoreType();
      PopulateVisitType();
    }

    var SaveMainStore = function() {
      if (ValidateMainStore()) {
        if (!self.model.id) {
          CreateMainStore();
        } else {
          updateMainStore();
        }
      }
    };

    var PopulateVisitType = function() {
      self.model.visitTypes = [
        { visit_type_id: 1, visit_type_name: "OP" },
        { visit_type_id: 2, visit_type_name: "IP" },
        { visit_type_id: 3, visit_type_name: "Day Care" },
        { visit_type_id: 4, visit_type_name: "ER" }
      ];
    };

    //Method to Populate Store Type Dropdown
    var PopulateStoreType = function() {
      var storeType = self.model;

      var URI = CONSTANTS.GLOBAL.STORE_TYPE_API + '/' + $scope.organizationId;

      storeType.storeType = storeType.storeTypes[0];

      storeType.storeTypes.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function(response) {
          storeType.storeTypes = storeType.storeTypes.concat(response.data);
          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        }
      );
    };

    //Method to get the list of Main Store
    var getMainStore = function() {
      var URI =
        CONSTANTS.MASTER.UNIT.PHARAMACY.MAIN_STORE_MASTER
          .MAIN_STORE_MASTER_LS_API+ '/' + $scope.unitId +'/'+$scope.organizationId;
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function(response) {
          self.model.mainStoreList = response.data;
          angular.forEach(self.model.mainStoreList, function(value , key) {
          angular.forEach($scope.ledgerList, function(value1 , key) {
              if(value.generalLedgerId==value1.generalLedgerId)
                  {
                  value.generalLedgerName=value1.generalLedgerName;
                  }
          });
        });
          if(!!response && !!response.data && response.data.length > 0){            
            PaginationService.searchData(self.model.mainStoreList, self.model.pagination);
          }
          $rootScope.stopLoader();
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        }
      );
    };

    //Method to populate Main Store for editing
    function findInRecordInArray(array, key, value) {
      for (var i = 0; i < array.length; i++) {
        if (array[i][key] == value) {
          return array[i];
        }
      }
      return null;
    }

    function getMainStoreById(id) {
      return findInRecordInArray(self.model.mainStoreList, "id", id);
    }

    function PopulateMainStore(id) {
      self.model.ismainstoreCodeValid = false;
      self.model.ismainstoreNameValid = false;
      self.model.ismainstoreTypeValid = false;
      self.model.activeMainStoreId = id;
      var URI =
        CONSTANTS.MASTER.UNIT.PHARAMACY.MAIN_STORE_MASTER
          .MAIN_STORE_MASTER_DU_API + id;
      var data = {};
      GenericService.serviceAction("GET", URI, data).then(function(response) {
        var mainStore = getMainStoreById(id);
        mainStore = response.data;
            angular.forEach($scope.ledgerList, function(value , key) {
                if(mainStore.generalLedgerId==value.generalLedgerId)
                    {
                    self.model.generalLedgerName=value.generalLedgerName;
                    self.model.generalLedgerId=value.generalLedgerId;
                    }
            });
        var storeType = self.model.storeTypes.find(function(type) {
          return response.data.storeTypeId == type.id;
        });
        self.model.code = response.data.storeCode;
        self.model.storeName = response.data.storeName;
        self.model.storeType = storeType;
        self.model.status = response.data.status;
        self.model.visit_type_id = response.data.visitTypeId;
        self.model.id = response.data.id;
        self.model.isLinen = response.data.isLinen;
        self.model.isPharmacy = response.data.isPharmacy;
        self.model.isNarcotic = response.data.isNarcotic;
        self.model.isCSSD = response.data.isCSSD;
        self.model.isLaundry = response.data.isLaundry;
        self.model.isMaintenance = response.data.isMaintenance;
        self.model.isEdit = true;
      });
    }

    //Method to activate or deactivate Main Store
    var ToggleStatus = function(mainStore) {
      var URI =
        CONSTANTS.MASTER.UNIT.PHARAMACY.MAIN_STORE_MASTER
          .MAIN_STORE_MASTER_DU_UPDATE_API + mainStore.id + "/" + mainStore.status+ "/" + $scope.organizationId+ "/" + $scope.unitId+ "/" + $scope.userId;
      var data = {
      };
      if (mainStore.status == false) {
        ClearFields();
      }
      GenericService.serviceAction("PATCH", URI,data).then(
        function(response) {
          $rootScope.stopLoader();
          if(response.data.status==200)
          {
            growl.success(response.data.message);
          }
          else
          {
            mainStore.status = !mainStore.status;
            growl.error(response.data.message);
          }

        },
        function errorCallback(response) {
          $rootScope.stopLoader();
          mainStore.status = !mainStore.status;
          growl.error(response.data.message);
        }
      );
    };
    $scope.getLedgerList = function(keyword)
    {
        $scope.ledgerList=[];
       var URI = BASE_URL + ROOT_URL
       + GETGENERALLEDGERMASTERACTIVELIST+"/"+$scope.organizationId;
       var data = {
               
           }
        GenericService
       .serviceAction("POST", URI, data)
       .then(
               function(response) {
                   if (response.data.status == "success") {
                       $rootScope.stopLoader();
                       $scope.ledgerList=response.data.listObject;
                   }
               });
    }
    $scope.getLedgerList();
    $scope.ledgerNameSearch = function(keyword)
    {
    	self.model.generalLedgerId=null;
       var URI = BASE_URL + ROOT_URL
       + GETGENERALLEDGERMASTERACTIVELIST+"/"+$scope.organizationId;
       var data = {
               "generalLedgerName":keyword
           }
       return GenericService
       .serviceAction("POST", URI, data)
       .then(
               function(response) {
                   if (response.data.status == "success") {
                       $rootScope.stopLoader();
                       return response.data.listObject;
                   }
               });
    }
    $scope.SelectLedgerName =function(ledger)
   {
        self.model.generalLedgerId=ledger.generalLedgerId;
   }
    $scope.clearText = function()
    {
    	if(self.model.generalLedgerId==undefined || self.model.generalLedgerId==null || self.model.generalLedgerId==0)
    		{
    		self.model.generalLedgerName="";
    		}
    }
    var ValidateCode = function() {
      var isValid = true;

      self.model.mainStoreList.forEach(function(element) {
        if (element.storeCode == self.model.code) {
          growl.error("Duplicate Store code");
          isValid = false;
        }
      });
      if (!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.code)) {
        growl.error("Code can only be alpha numeric with single hypen.");
        return;
      }
      return isValid;
    };

    var ValidateCodeForUpdate = function() {
      var isValid = true;

      self.model.mainStoreList.forEach(function(element) {
        if (element.id != self.model.activeMainStoreId) {
          if (element.storeCode == self.model.code) {
            growl.error("Duplicate Store code");
            isValid = false;
          }
        }
      });
      if (!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.code)) {
        growl.error("Code can only be alpha numeric with single hypen.");
        return;
      }
      return isValid;
    };

    //Method to add the Main Store
    var CreateMainStore = function() {
      if (ValidateCode()) {
        var URI =
          CONSTANTS.MASTER.UNIT.PHARAMACY.MAIN_STORE_MASTER
            .MAIN_STORE_MASTER_LS_API;
        var data = {
          storeTypeId: self.model.storeType.id,
          storeName: self.model.storeName,
          storeCode: self.model.code,
          visitTypeId: self.model.visit_type_id,
          isLinen: self.model.isLinen,
          isPharmacy: self.model.isPharmacy,
          isNarcotic: self.model.isNarcotic,
          isCSSD: self.model.isCSSD,
          isLaundry: self.model.isLaundry,
          isMaintenance: self.model.isMaintenance,
          unitId:$scope.unitId,
          organizationId:$scope.organizationId,
          userId:$scope.userId,
          storeId:$scope.storeId,
          status: true,
          generalLedgerId:self.model.generalLedgerId
          // "opcheckboxId": self.model.opcheckboxId
        };
        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function(response) {
        	  self.model.generalLedgerName="";
            var storeType = self.model.storeTypes.find(function(store) {
              return store.id == self.model.storeType.id;
            });
            var mainStore = {
              id: response.data.id,
              storeTypeId: self.model.storeType.id,
              storeType: storeType.type,
              storeName: self.model.storeName,
              storeCode: self.model.code,
              isLinen: self.model.isLinen,
              isPharmacy: self.model.isPharmacy,
              isNarcotic: self.model.isNarcotic,
              isCSSD: self.model.isCSSD,
              isLaundry: self.model.isLaundry,
              isMaintenance: self.model.isMaintenance,
              status: true
            };
            self.model.mainStoreList.push(mainStore);
            PaginationService.searchData(self.model.mainStoreList, self.model.pagination);
            PaginationService.setLastPage(self.model.pagination);
            ClearFields();
            getMainStore();
            $rootScope.stopLoader();
            growl.success(response.data.message);
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
            growl.error("Something went wrong");
          }
        );
      }
    };

    //Method to update the Main Store
    var updateMainStore = function() {
      if (ValidateCodeForUpdate()) {
        var URI =
          CONSTANTS.MASTER.UNIT.PHARAMACY.MAIN_STORE_MASTER
            .MAIN_STORE_MASTER_DU_API + self.model.id;
        var data = {
          storeTypeId: self.model.storeType.id,
          storeName: self.model.storeName,
          storeCode: self.model.code,
          visitTypeId: self.model.visit_type_id,
          isLinen: self.model.isLinen,
          isPharmacy: self.model.isPharmacy,
          isNarcotic: self.model.isNarcotic,
          isCSSD: self.model.isCSSD,
          isLaundry: self.model.isLaundry,
          isMaintenance: self.model.isMaintenance,
          unitId:$scope.unitId,
          organizationId:$scope.organizationId,
          userId:$scope.userId,
          storeId:$scope.storeId,
          generalLedgerId:self.model.generalLedgerId
        };
        GenericService.serviceAction("PATCH", URI, data).then(
          function(response) {
        	  self.model.generalLedgerName="";
            var storeType = self.model.storeTypes.find(function(type) {
              return type.id == self.model.storeType.id;
            });
            var mainStore = self.model.mainStoreList.find(function(type) {
              return type.id == self.model.id;
            });
            mainStore.id = response.data.id;
            mainStore.storeType = storeType.type;
            mainStore.storeName = self.model.storeName;
            mainStore.storeCode = self.model.code;
            mainStore.isLinen = self.model.isLinen,
            mainStore.isPharmacy = self.model.isPharmacy,
            mainStore.isNarcotic = self.model.isNarcotic,
            mainStore.isCSSD = self.model.isCSSD,
            mainStore.isLaundry = self.model.isLaundry,
            mainStore.isMaintenance = self.model.isMaintenance,
            ClearFields();
            getMainStore();
            $rootScope.stopLoader();
            growl.success(response.data.message);
            self.model.activeMainStoreId = null;
            self.model.isEdit = false;
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
            growl.error("Something went wrong");
          }
        );
      }
    };

    //Method to clear the fileds
    function ClearFields() {
      self.model.pagination.search = '';
      self.model.code = "";
      self.model.storeName = "";
      self.model.isLinen = false;
      self.model.isPharmacy = false;
      self.model.isNarcotic = false;
      self.model.isCSSD = false;
      self.model.isLaundry = false;
      self.model.isMaintenance = false;
      self.model.visit_type_id = null;
      PopulateStoreType();
      PopulateVisitType();
    }
    var clearSearch = function(){
      self.model.search = "";
      PaginationService.searchData(self.model.mainStoreList, self.model.pagination);
    }

    var ValidateMainStore = function() {
      var isValid = true;
      if (!self.model.code) {
        isValid = false;
        self.model.ismainstoreCodeValid = true;
      }
      if (!self.model.storeName) {
        isValid = false;
        self.model.ismainstoreNameValid = true;
      }
      if (!self.model.storeType.id) {
        isValid = false;
        self.model.ismainstoreTypeValid = true;
      }
      if (!self.model.visit_type_id) {
        isValid = false;
        growl.error("Please Select Visit Type!", { title: "Warning" });
      }
      return isValid;
    };

    initializeController();
  }

  function config($stateProvider) {

	  $stateProvider.state("mainstoremaster", {
      url: "/mainstoremaster",
      templateUrl:"views/PharmacyInventory/StockAdjustment/main-store-master.html",
      controller: "MainStoreMaster.Controller",
      title: 'Main Store Master',
  	  resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 631);
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
    .controller("MainStoreMaster.Controller", mainstoreMasterController)
    .directive("showFocus", function($timeout) {
      return function(scope, element, attrs) {
        scope.$watch(
          attrs.showFocus,
          function(newValue, oldValue) {
            $timeout(function() {
              newValue != oldValue && element.focus();
            });
          },
          true
        );
      };
    });
})();
