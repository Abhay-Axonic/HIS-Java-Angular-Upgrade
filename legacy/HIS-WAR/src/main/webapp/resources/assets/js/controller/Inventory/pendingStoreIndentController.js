(function () {
  "use strict";
  var CACHE = {};
  function pendingStoreIndentController($scope, $rootScope, $http, $cookies, CONSTANTS, growl, GenericService,PaginationService, cancelReasonService, screenDataService, commonDetailService, $state, $filter, reportService) {
    var self = this;
    var selectItemList = [];
    var pendingStoreIndentId;
    var cancelIndex;

    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
      $rootScope.loginpage = true;
      self.model = {
        selectItemList: [],
        fromStores: [{
          "store": "All Stores"
        }],
        toStores: [{
          "store": "All Stores"
        }],
        indentTypes: [{
          "type": "All"
        }],
        priorities: [{
          "priority": "All"
        }],
        statuses: [{
          "status": "All"
        }],
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
      cancelReasonService.GetReason().then(function (reason) {
        self.model.reasons = reason;
      });
      self.model.SearchPendingStoreIndent = searchPendingStoreIndent;
      self.model.storeIndentPendingDetails = storeIndentPendingDetails;
      self.model.saveRejection = saveRejection;
      self.model.openmodal = openmodal;
      self.model.isDisable = isDisable;
      self.model.printPendingStoreIndentReport = printPendingStoreIndentReport;

      PopulateDropdown();
      PopulateStatusDropdown();
    }
    var initialCall = function () {
      if (!screenDataService.getScreenData()) {
        searchPendingStoreIndent()
      }
      else {
        searchPendingStoreIndent();
        screenDataService.getScreenData().forEach(function (dataobj) {
          if (dataobj.screenName == 'Pending Store Indent') {
            var requestData = dataobj.searchrequest;
            self.model.fromStore = self.model.fromStores.find(function (fromStore) {
              return requestData.fromStore.id == fromStore.id;
            });
            self.model.toStore = self.model.toStores.find(function (toStore) {
              return requestData.toStore.id == toStore.id;
            });
            self.model.indentType = self.model.indentTypes.find(function (indentType) {
              return requestData.indentType.id == indentType.id;
            });
            self.model.priority = self.model.priorities.find(function (priority) {
              return requestData.priority.id == priority.id;
            });
            self.model.status = self.model.statuses.find(function (status) {
              return requestData.status.id == status.id;
            });
            self.model.indentNumber = requestData.indentNumber
          }
        });
      }
    }


    //Method to populate the list of store indent dropdown
    var PopulateDropdown = function () {
      var indents = self.model;
      indents.fromStore = indents.fromStores[0];
      indents.toStore = indents.toStores[0];
      indents.indentType = indents.indentTypes[0];
      indents.priority = indents.priorities[0];

      var URI = CONSTANTS.INVENTORY.STORE.PENDING_STORE_INDENT.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          indents.fromStores = indents.fromStores.concat(response.data.fromStore);
          indents.toStores = indents.toStores.concat(response.data.toStore);
          indents.indentTypes = indents.indentTypes.concat(response.data.indentType);
          indents.priorities = indents.priorities.concat(response.data.priority);
          initialCall();
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var PopulateStatusDropdown = function () {
      var indents = self.model;
      //indents.status = indents.statuses[0];

      var URI = CONSTANTS.INVENTORY.STORE.STORE_INDENT.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          indents.statuses = indents.statuses.concat(response.data.status);
          for(var i=0;i<indents.statuses.length;i++)
          {
            if(indents.statuses[i].status=='Pending')
            {
              indents.status = indents.statuses[i];
            }
          }
          initialCall();
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    function storeIndentPendingDetails(data) {
      commonDetailService.setDataId(data.id);
      $state.go('detailspendingStoreIndents');
    }


    var searchPendingStoreIndent = function () {
      var URI = CONSTANTS.INVENTORY.STORE.PENDING_STORE_INDENT.SEARCH_API;
      var data = {
        "fromStoreId": self.model.fromStore.id ? self.model.fromStore.id : undefined,
        "toStoreId": self.model.toStore.id ? self.model.toStore.id : undefined,
        "indentNo": self.model.indentNumber ? self.model.indentNumber : undefined,
        "indentTypeId": self.model.indentType.id ? self.model.indentType.id : undefined,
        "priorityId": self.model.priority.id ? self.model.priority.id : undefined,
        "statusId": self.model.status.id ? self.model.status.id : undefined,
        "unitId":$scope.unitId,
        "userId":$scope.userId,

      };
      $rootScope.startLoader()
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          self.model.pagination.data=[];
          self.model.pendingStoreIndentsList = response.data;
          if(!!response && !!response.data && response.data.length > 0){            
            PaginationService.searchData(self.model.pendingStoreIndentsList, self.model.pagination);
          }
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
          growl.error('Error');
        });
      var screenData = {
        searchrequest: {
          "fromStore": self.model.fromStore,
          "toStore": self.model.toStore,
          "indentNo": self.model.indentNumber,
          "indentType": self.model.indentType,
          "priority": self.model.priority
        },
        screenName: 'Pending Store Indent',
      }
      screenDataService.setScreenData(screenData);

    }

    var openmodal = function (pid) {
      self.model.cancelReason = '';
      self.model.cancelNote = '';
      $("#CanellationModalForPendingStoreIndend").modal();
      pendingStoreIndentId = pid;
    }

    var saveRejection = function () {
      if (ValidateRejection()) {
        var URI = CONSTANTS.INVENTORY.STORE.PENDING_STORE_INDENT.CANCEL_API + pendingStoreIndentId;
        var data = {
          "closeReasonId": self.model.cancelReason,
          "closeNote": self.model.cancelNote,
          "statusId": 8,
          "userId":$scope.userId
        };
        $rootScope.startLoader()
        GenericService.serviceAction("PATCH", URI, data).then(
          function (response) {
            $rootScope.stopLoader();
            self.model.reasonResponse = response.data;
            searchPendingStoreIndent();
          })
        self.model.cancelNote = '';
        self.model.cancelReason = '';
        ModalDismiss('CanellationModalForPendingStoreIndend');
      }


    }

    var ValidateRejection = function () {
      var isValid = true;
      if (!self.model.cancelReason) {
        isValid = false;
        growl.error('Please Select Reason to Cancel');
      }
      return isValid;
    }

    var ModalDismiss = function (popupName) {
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
    }

    var isDisable = function (Item) {
      if (Item.status == 'Cancelled' || Item.status == 'Closed') {
        return true;
      }
      return false;
    }
    
    /***Chetana**/
    var printPendingStoreIndentReport = function (item) {
        var data = {
            "appName": "HIS",
            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
            "reportDesignFilePath": "get_Store_Indent.rpt",
            "reportFormat": "PDF",
            "exportFileName": "PendingStoreIndent",
            "reportParams": [
                {
                    "paramName": "StoreIndent_id",
                    "paramType": "Integer",
                    "paramValue": item.id
                }
            ]
        };
        reportService.generate(REPORT_API_URI1, data);
    }
   /*****/ 
    
    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("pendingStoreIndents", {
      url: "/pendingStoreIndents",
      templateUrl: 'views/PharmacyInventory/InPatient/pending-store-indents.html',
      controller: "PendingStoreIndent.Controller",
      title: 'Pending Store Indent',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 126);
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
    .controller("PendingStoreIndent.Controller", pendingStoreIndentController);
})();