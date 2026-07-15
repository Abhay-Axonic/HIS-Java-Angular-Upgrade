(function () {
    "use strict";
  
    function auctionVendorSelectionController($scope, $rootScope, $http, $filter, $state, $cookies, CONSTANTS,PaginationService, GenericService, commonDetailService, rejectReasonService, StatusService, growl, screenDataService,reportService) {
      var self = this;
  
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
          fromDate: new Date(),
          toDate: new Date(),
          fromDateOptions: {
            formatYear: 'yyyy',
            showWeeks: false,
            maxDate : new Date(),
            minDate : moment(new Date()).subtract(180, 'days').toDate()
          },
          toDateOptions: {
            formatYear: 'yyyy',
            showWeeks: false,
            maxDate : new Date()
          },
          storeList: [{
            "store": "All"
          }],
          statuses: [{
            "status": "All"
          }],
          vendorNames: []
        };
  
        self.model.OpenFromDate = OpenFromDate;
        self.model.OpenToDate = OpenToDate;
        self.model.NavigateToDetails = NavigateToDetails;
        self.model.NavigateToEdit = NavigateToEdit;
        self.model.SearchVendorNames = SearchVendorNames;
        self.model.AddVendorName = AddVendorName;
  
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
        self.model.store = self.model.storeList[0];
        self.model.status = self.model.statuses[0];
  
        var URI = CONSTANTS.PROCUREMENT.QUOTATION.VENDOR_SELECTION.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;
  
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.storeList = self.model.storeList.concat(response.data.store);
            self.model.statuses = self.model.statuses.concat(response.data.status);
            $rootScope.stopLoader();
            initialCall();
          },
          function (err) {
            $rootScope.stopLoader();
          });
      }
  
      var initialCall = function () {
        if (!screenDataService.getScreenData()) {
            
        } else {
          screenDataService.getScreenData().forEach(function (dataobj) {
            if (dataobj.screenName == 'scrapVendorSelection'){
              var requestData = dataobj.searchrequest;
                     
              self.model.status = self.model.statuses.find(function (status) {
                  return requestData.status.id == status.id;
              });
              self.model.store = self.model.storeList.find(function (store) {
                  return requestData.store.id == store.id;
              });
              self.model.fromDate = new Date(requestData.fromDate);
              self.model.toDate = new Date(requestData.toDate);
            }
          });
        }
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
  
      function AddVendorName (vendor) {
        self.model.vendorName = vendor.vendorName;
        self.model.vendorId = vendor.id;
      }
  
      var NavigateToEdit = function () {
        $state.go('detailsAuctionVendorSelection');
      }
  
      var NavigateToDetails = function () {
        $state.go('viewAuctionVendorSelection');
      }

      initializeController();
    }
    
    function config($stateProvider) {
      $stateProvider.state("auctionVendorSelection", {
        url: "/auctionVendorSelection",
        templateUrl: 'views/master/GlobalMaster/AssetManagenent/Master/auction-vendor-selection.html',
        controller: "auctionVendorSelection.Controller",
        title: 'Auction Vendor Selection',
        resolve : {
  
              data: function($q, $state, $timeout) {
                  var deferred = $q.defer();
          
                  $timeout(function() {
                      var arr = JSON.parse(localStorage.getItem('permissions'))
                       .modules.filter(function(item){
                         return (item.id == 849);
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
      .controller("auctionVendorSelection.Controller", auctionVendorSelectionController);
  })();