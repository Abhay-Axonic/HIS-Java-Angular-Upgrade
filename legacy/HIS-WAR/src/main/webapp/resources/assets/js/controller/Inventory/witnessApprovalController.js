(function () {
    "use strict";
  
    function witnessApprovalController($scope, $rootScope, $http, commonDetailService,PaginationService, screenDataService, $state, $cookies, CONSTANTS, GenericService, growl, reportService) {
      var self = this;
      
      //Default Constructor
      var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
      function initializeController() {
        $rootScope.loginpage = true;
        $rootScope.loginpage = true;
      self.model = {
        pagination: {
          currentPage: 1,
          totalPages: 0,
          pageSize: '10',
          search: '',
          searchData: PaginationService.searchData,
          searchDataByField:PaginationService.searchDataByField,
          setTotalPages: PaginationService.setTotalPages,
          getPagesList: PaginationService.getPagesList,
          setPage: PaginationService.setPage,
          setFirstPage: PaginationService.setFirstPage,
          setPreviousPage: PaginationService.setPreviousPage,
          setNextPage: PaginationService.setNextPage,
          setLastPage: PaginationService.setLastPage,
          sortBy: PaginationService.sortBy
        },
        stores: [{
          "store": "All Stores"
        }],
        statuses: [{
          "status": "All Status"
        }],
        fromDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate: new Date(),
          minDate : moment(new Date()).subtract(180, 'days').toDate()
        },
        assetTypes: [{
          "type": "All Types"
        }],
        toDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate: new Date()
        },
        fromDate: new Date(),
        toDate: new Date(),


      };

      self.model.PopulateDropdown = PopulateDropdown;
      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.WitnessApprovalSearch = WitnessApprovalSearch;
      self.model.WitnessApprovalDetails = WitnessApprovalDetails;
      self.model.printWitnessApprovalReport = printWitnessApprovalReport;

      PopulateDropdown();
    }

    var OpenFromDate = function () {
      self.model.fromDateOpened = true;
      self.model.toDateOptions.minDate = self.model.fromDate;
    }

    var OpenToDate = function () {
      self.model.toDateOpened = true;
      self.model.toDateOptions.minDate = self.model.fromDate;
    }

    var PopulateDropdown = function () {
      var disposal = self.model;
      disposal.store = disposal.stores[0];
      disposal.status = disposal.statuses[0];
      disposal.assetType = disposal.assetTypes[0];
      var URI = CONSTANTS.INVENTORY.SCRAP.WITNESS_APPROVAL.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          disposal.stores = disposal.stores.concat(response.data.store);
          disposal.assetTypes = disposal.assetTypes.concat(response.data.assetType);
          disposal.statuses = disposal.statuses.concat(response.data.status);
          initialCall();
          $rootScope.stopLoader();
          // GetStoreIndents();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var initialCall = function () {
      if (!screenDataService.getScreenData()) {
        WitnessApprovalSearch()
      }
      else {
        screenDataService.getScreenData().forEach(function (dataobj) {
          if (dataobj.screenName == 'Witness Approval') {

            var requestData = dataobj.searchrequest;
            self.model.store = self.model.stores.find(function (store) {
              return requestData.store.id == store.id;
            }); 
            self.model.assetType = self.model.assetTypes.find(function (assetType) {
              return requestData.assetType.id == assetType.id;
            });
            self.model.status = self.model.statuses.find(function (status) {
              return requestData.status.id == status.id;
            });
            self.model.fromDate = new Date(requestData.fromDate);
            self.model.toDate = new Date(requestData.toDate);

          }
        });
        WitnessApprovalSearch();
      }

    }
    var ValidateDate = function () {
      var isValid = true;

      if (!self.model.fromDate || !self.model.toDate) {
        growl.error('Enter Valid Date');
        isValid = false;
      }

      return isValid;
    }
    var WitnessApprovalSearch = function () {
      if(ValidateDate())
      {
      var URI = CONSTANTS.INVENTORY.SCRAP.WITNESS_APPROVAL.SEARCH_API;
      var data = {
        "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
        "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
        "storeId": self.model.store.id,
        "statusId": self.model.status.id,
        "assetTypeId": self.model.assetType.id,
        "scrapType": 1,
        "unitId": $scope.unitId
      };
      $rootScope.startLoader()
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          self.model.pagination.data=[];
          self.model.witnessApprovalData = response.data;
          if(!!response && !!response.data && response.data.length > 0){            
            PaginationService.searchDataByField(self.model.witnessApprovalData, self.model.pagination);
          }
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
          growl.error('Error');
        });
        var screenData = {
          searchrequest: {
            "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
            "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
            "store": self.model.store,
            "assetType": self.model.assetType,
            "status": self.model.status,
          },
          screenName: 'Witness Approval',
        }
        screenDataService.setScreenData(screenData);
    }
  }
    var WitnessApprovalDetails = function (disposal) {
      commonDetailService.setDataId(disposal.id);
      $state.go('detailsWitnessApproval');
    }

    
    /***Chetana**/
    var printWitnessApprovalReport = function (disposal) {
        var data = {
            "appName": "HIS",
            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
            "reportDesignFilePath": "get_WitnessApproval.rpt",
            "reportFormat": "PDF",
            "exportFileName": "WitnessApproval",
            "reportParams": [
                {
                    "paramName": "id",
                    "paramType": "Integer",
                    "paramValue": disposal.id
                }
            ]
        };
        reportService.generate(REPORT_API_URI1, data);
    }
   /*****/ 
    initializeController();
  }
  
    function config($stateProvider) {
      $stateProvider.state("witnessApproval", {
        url: "/witnessApproval", 
        templateUrl: 'views/PharmacyInventory/Store/witness-approval.html',
        controller: "WitnessApproval.Controller",
        title: 'Witness Approval',
    	resolve : {

   			data: function($q, $state, $timeout) {
   		        var deferred = $q.defer();
   		
   		        $timeout(function() {
   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
   				 	.modules.filter(function(item){
   	            	   return (item.id == 160);
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
      .controller("WitnessApproval.Controller", witnessApprovalController);
  })();
 