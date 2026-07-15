(function () {
  "use strict";

  function detailsLoanInController($rootScope, $scope, $http, $cookies, CONSTANTS, $state, GenericService, commonDetailService, growl, CurrencyService) {
    var self = this;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
      $rootScope.loginpage = true;

      self.model = {
        uomDetails: []
      };
      self.model.NavigateToBack = NavigateToBack;
      self.model.ViewUomDetails = ViewUomDetails;
      GetLoanIn(commonDetailService.getDataId());
    }

    CurrencyService.GetCurrency().then(function (currency) {
      self.model.Currency = currency;
    });
    
    var GetLoanIn = function (loanInId) {
      commonDetailService.setDataId(null);

      var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_IN.DETAILS_API  + $scope.unitId + "/" + loanInId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.loanInData = response.data;
          self.model.assetTypeId = response.data.assetTypeId;
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var ViewUomDetails = function (item) {
      self.model.loanInData.items.find(function(x){
        if (item.id == x.id) {
          self.model.uomDetails = x.uomList;
        }
      });
    }

    var NavigateToBack = function (){
      $state.go('loanIn');
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailsLoanIn", {
      url: "/detailsLoanIn",
      templateUrl: 'views/procurement/consignment/details-loan-in.html',
      controller: "DetailsLoanIn.Controller",
      controllerAs: "vm",
      title: 'Loan In'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("DetailsLoanIn.Controller", detailsLoanInController);
})();