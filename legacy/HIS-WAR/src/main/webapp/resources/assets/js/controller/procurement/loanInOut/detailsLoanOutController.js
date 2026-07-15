(function () {
  "use strict";

  function detailsLoanOutController($rootScope, $scope, $http, $cookies, CONSTANTS, $state, GenericService, commonDetailService, growl, CurrencyService) {
    var self = this;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
      $rootScope.loginpage = true;
      
      self.model = {};
      CurrencyService.GetCurrency().then(function(currency){
        self.model.Currency = currency;
      });
      self.model.NavigateToBack = NavigateToBack;
      GetLoanOut(commonDetailService.getDataId());
    }

    var GetLoanOut = function (loanOutId) {
      commonDetailService.setDataId(null);

      var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_OUT.DETAILS_API + $scope.unitId + '/' + loanOutId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.loanOutData = response.data;
          self.model.assetTypeId = response.data.assetTypeId;
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var NavigateToBack = function (){
      $state.go('loanOut');
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailsLoanOut", {
      url: "/detailsLoanOut",
      templateUrl: 'views/procurement/consignment/details-loan-out.html',
      controller: "DetailsLoanOut.Controller",
      controllerAs: "vm",
      title: 'Loan Out'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("DetailsLoanOut.Controller", detailsLoanOutController);
})();