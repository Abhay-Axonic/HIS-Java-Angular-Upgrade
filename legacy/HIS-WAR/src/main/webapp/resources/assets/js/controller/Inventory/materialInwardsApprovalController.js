(function () {
    "use strict";
  
    function materialInwardsApprovalController($scope, $rootScope, StatusService, PaginationService, $filter,commonDetailService, $state,  cancelReasonService, $http, $cookies, CONSTANTS, GenericService, growl, reportService) {
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
          },
            stores: [{
                "store": "All Stores"
              }],
              statuses: [{
                "status": "All Status"
              }],
              assetTypes: [{
                "type": "All Asset Type"
              }],
              fromDateOptions: {
                formatYear: 'yyyy',
                showWeeks: false,
                maxDate: new Date(),
                minDate : moment(new Date()).subtract(180, 'days').toDate()
              },
              toDateOptions: {
                formatYear: 'yyyy',
                showWeeks: false,
                maxDate: new Date()
              },
              fromDate: new Date(),
              toDate: new Date(),
              SelectedRequestIds: [],
              selectAllRows: false,
      
  
         
      };
      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.PopulateDropdown = PopulateDropdown;
      self.model.SearchVendorNames = SearchVendorNames;
      self.model.nameNotFound = nameNotFound;
      self.model.AddVendorName = AddVendorName;
      self.model.search = '';
      self.model.prevSearch = '';
      self.model.vendorNames = [];
      self.model.InwardApprovalSearch = InwardApprovalSearch;
      self.model.MaterialInwardsApprovalDetails = MaterialInwardsApprovalDetails;
      self.model.printMaterialInwardApprovalReport = printMaterialInwardApprovalReport;

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
      var returnable = self.model;
      returnable.store = returnable.stores[0];
      returnable.status = returnable.statuses[0];
      returnable.assetType = returnable.assetTypes[0];
      var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_INWARDS_APPROVAL.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          returnable.stores = returnable.stores.concat(response.data.store);
          returnable.statuses = returnable.statuses.concat(response.data.status);
          returnable.assetTypes = returnable.assetTypes.concat(response.data.assetType);
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
        InwardApprovalSearch();
    }

    function SearchVendorNames(search) {
      if (search.length < 1) return;

      if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
        self.model.prevSearch = search;
        var URI = CONSTANTS.GLOBAL.VENDOR_SEARCH_API + search + "/"+$scope.unitId;
        $rootScope.startLoader();
        return GenericService.serviceAction("GET", URI, {})
          .then(function (response) {
            if (!!response.data && response.data.length > 0) {
              self.model.vendorNames = response.data;
              $rootScope.stopLoader();
              return $filter('filter')(self.model.vendorNames, {
                $: search
              });
            } else {
              $rootScope.stopLoader();
              return nameNotFound(search);
            }
          }, function (err) {
            $rootScope.stopLoader();
            return nameNotFound(search);
          });
      } else {
        if (!!self.model.vendorNames && self.model.vendorNames.length > 0 && self.model.vendorNames[0].itemFound != undefined && !self.model.vendorNames[0].itemFound) {
          return nameNotFound(search);
        } else {
          return $filter('filter')(self.model.vendorNames, {
            $: search
          });
        }
      }
    }

    function nameNotFound(search) {
      var item = {
        "itemFound": false,
        "detail": "Vendor Not found with name " + search
      };
      self.model.vendorNames = [item];
      return self.model.vendorNames;
    }

    function AddVendorName(vendor) {
      self.model.vendorName = vendor.vendorName;
      self.model.vendorId = vendor.id;
    }

    var InwardApprovalSearch = function () {
      var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_INWARDS_APPROVAL.SEARCH_API;
      var data = {
        "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
        "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
        "storeId": self.model.store.id,
        "statusId": self.model.status.id,
        "vendorId": self.model.vendorId,
        "assetTypeId": self.model.assetType.id,
        "inwardNo": self.model.inwardNo,
        "unitId":$scope.unitId
      };
      $rootScope.startLoader()
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          $rootScope.stopLoader(); 
          self.model.pagination.data=[];
          self.model.inwardSearchData = response.data;
          if(!!response && !!response.data && response.data.length > 0){            
            PaginationService.searchData(self.model.inwardSearchData, self.model.pagination);
          }
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
          growl.error('Error');
        });
    }

    var MaterialInwardsApprovalDetails = function (inward) {
      commonDetailService.setDataId(inward.id);
      $state.go('detailsMaterialInwardsApproval');
  }

    /***Chetana**/
    var printMaterialInwardApprovalReport = function (inward) {
        var data = {
            "appName": "HIS",
            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
            "reportDesignFilePath": "get_inward.rpt",
            "reportFormat": "PDF",
            "exportFileName": "MaterialInwardsApproval",
            "reportParams": [
                {
                    "paramName": "id",
                    "paramType": "Integer",
                    "paramValue": inward.id
                }
            ]
        };
        reportService.generate(REPORT_API_URI1, data);
    }
   /*****/ 


      initializeController();
    }
  
    function config($stateProvider) {
      $stateProvider.state("materialInwardsApproval", {
        url: "/materialInwardsApproval",
        templateUrl: 'views/PharmacyInventory/Store/material-inwords-approval.html',
        controller: "MaterialInwardsApproval.Controller",
        title: 'Material Inward Approval',
    	resolve : {

   			data: function($q, $state, $timeout) {
   		        var deferred = $q.defer();
   		
   		        $timeout(function() {
   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
   				 	.modules.filter(function(item){
   	            	   return (item.id == 142);
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
      .controller("MaterialInwardsApproval.Controller", materialInwardsApprovalController);
  })(); 