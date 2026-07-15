(function () {
    "use strict";
  
    function auctionQuotationFillinDetailsController($rootScope, $scope, $http, $cookies, CONSTANTS, $state, GenericService, commonDetailService, growl, CurrencyService) {
      var self = this;
  
      var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
        $rootScope.loginpage = true;
  
        CurrencyService.GetCurrency().then(function(currency){
          self.model.Currency = currency;
        });
        
        self.model = {
          expectedAccessories: [],
          termsVisible: 1
        };
        self.model.NavigateToBack = NavigateToBack;
        self.model.ViewAccessories = ViewAccessories;
        self.model.ViewTerms = ViewTerms;
      }
  
      var ViewAccessories = function (item) {
        self.model.quotationList.items.find(function(x){
          if (item.id == x.id) {
            self.model.expectedAccessories = x.expectedAccessories;
          }
        });
      }
  
      var ViewTerms = function (item) {
        self.model.quotationList.items.find(function(x){
          if (item.itemId == x.itemId) {
            self.model.warrantyPeriod = x.warrantyPeriod;
            self.model.supportTerms = x.supportTerms;
            self.model.paymentTerms = x.paymentTerms;
            self.model.technicalSpecification = x.technicalSpecification;
            self.model.vendorWarrantyPeriod = x.vendorWarrantyPeriod;
            self.model.vendorExpectedSupport = x.vendorExpectedSupport;
            self.model.vendorExpectedPayment = x.vendorExpectedPayment;
            self.model.vendorTechnicalSpecification = x.vendorTechnicalSpecification;
          }
        });
      }
  
      var NavigateToBack = function (){
        $state.go('auctionQuotationFillin');
      }
  
      initializeController();
    }
    
    function config($stateProvider) {
      $stateProvider.state("auctionQuotationFillinDetails", {
        url: "/auctionQuotationFillinDetails",
        templateUrl: 'views/master/GlobalMaster/AssetManagenent/Master/auction-quotation-fill-in-details.html',
        controller: "auctionQuotationFillinDetails.Controller",
        controllerAs: "vm",
        title: 'Quotation Fill In'
      });
    }
  
    angular
      .module("myApp")
      .config(config)
      .controller("auctionQuotationFillinDetails.Controller", auctionQuotationFillinDetailsController);
  })();