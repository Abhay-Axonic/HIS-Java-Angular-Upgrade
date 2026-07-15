(function () {
    "use strict";
  
    function detailsAuctionEnquirySentToVendorController($rootScope, $scope, $http, $cookies, CONSTANTS, $state, GenericService, commonDetailService, growl) {
      var self = this;
  
      //Default Constructor
      var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
        $rootScope.loginpage = true;
  
        self.model = {
          termsVisible: 1
        };
        self.model.NavigateToBack = NavigateToBack;
        self.model.ViewTerms = ViewTerms;
      }
  
      var ViewTerms = function (item) {
        self.model.quotationList.items.find(function(x){
          if (item.id == x.id) {
            self.model.warrantyPeriod = x.warrantyPeriod;
            self.model.supportTerms = x.supportTerms;
            self.model.paymentTerms = x.paymentTerms;
            self.model.technicalSpecification = x.technicalSpecification;
          }
        });
      }
  
      var NavigateToBack = function (){
        $state.go('auctionEnquirySentToVendor');
      }
  
      initializeController();
    }

    function config($stateProvider) {
      $stateProvider.state("detailsAuctionEnquirySentToVendor", {
        url: "/detailsAuctionEnquirySentToVendor",
        templateUrl: 'views/master/GlobalMaster/AssetManagenent/Master/details-auction-enquiry-sent-to-vendor.html',
        controller: "detailsAuctionEnquirySentToVendor.Controller",
        controllerAs: "vm",
        title: 'Auction Enquiry Sent to Vendor'
      });
    }
  
    angular
      .module("myApp")
      .config(config)
      .controller("detailsAuctionEnquirySentToVendor.Controller", detailsAuctionEnquirySentToVendorController);
  })();