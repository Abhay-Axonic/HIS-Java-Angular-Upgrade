(function () {
    "use strict";
  
    function auctionEnquirySentToVendorController($scope, $rootScope, $http, $filter, $state, $cookies, CONSTANTS, GenericService,PaginationService, commonDetailService, cancelReasonService, StatusService, growl) {
      var self = this;
  
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
          selectAllRows: false,
          storeList: [{
            "store": "All"
          }],
          statuses: [{
            "status": "All"
          }]
        };
  
        self.model.OpenFromDate = OpenFromDate;
        self.model.OpenToDate = OpenToDate;
        self.model.SearchVendorNames = SearchVendorNames;
        self.model.AddVendorName = AddVendorName;
        self.model.vendorNames = [];
        self.model.NavigateToDetails = NavigateToDetails;
  
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
  
        var URI = CONSTANTS.PROCUREMENT.QUOTATION.ENQUIRY_SENT_TO_VENDOR.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;
  
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.storeList = self.model.storeList.concat(response.data.store);
            self.model.statuses = self.model.statuses.concat(response.data.status);
            $rootScope.stopLoader();
            GetQuotations();
          },
          function (err) {
            $rootScope.stopLoader();
          });
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
          "detail": "Vendor not found with name " + search
        };
        self.model.vendorNames = [item];
        return self.model.vendorNames;
      }
  
      function AddVendorName (vendor) {
        self.model.vendorName = vendor.vendorName;
        self.model.vendorId = vendor.id;
      }

      var NavigateToDetails = function (enquiry) {
        // commonDetailService.setDataId(enquiry.id);
        $state.go('detailsAuctionEnquirySentToVendor');
      }
  
      initializeController();
    }

    function config($stateProvider) {
      $stateProvider.state("auctionEnquirySentToVendor", {
        url: "/auctionEnquirySentToVendor",
        templateUrl: 'views/master/GlobalMaster/AssetManagenent/Master/auction-enquiry-sent-to-vendor.html',
        controller: "auctionEnquirySentToVendor.Controller",
        title: 'Auction Enquiry Sent to Vendor',
        resolve : {
    
              data: function($q, $state, $timeout) {
                  var deferred = $q.defer();
          
                  $timeout(function() {
                      var arr = JSON.parse(localStorage.getItem('permissions'))
                       .modules.filter(function(item){
                         return (item.id == 846);
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
      .controller("auctionEnquirySentToVendor.Controller", auctionEnquirySentToVendorController);
  })();