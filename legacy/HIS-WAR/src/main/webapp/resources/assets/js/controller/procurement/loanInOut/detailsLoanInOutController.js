(function () {
  "use strict";

  function detailsLoanInOutController($rootScope, $scope, $http, $cookies, CONSTANTS, $state, GenericService, commonDetailService, growl, CurrencyService) {
    var self = this;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
      $rootScope.loginpage = true;

      self.model = {
        uomDetails: []
      };
      CurrencyService.GetCurrency().then(function(currency){
        self.model.Currency = currency;
      });
      self.model.NavigateToBack = NavigateToBack;
      self.model.ViewUomDetails = ViewUomDetails;
      GetLoanIn(commonDetailService.getDataId());
    }

    var GetLoanIn = function (loanInOutId) {
      commonDetailService.setDataId(null);

      var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_INOUT.DETAILS_API + $scope.unitId + '/' + loanInOutId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.assetTypeId = response.data.assetTypeId;
          self.model.loanInOutData = response.data;
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var ViewUomDetails = function (item) {
      self.model.loanInOutData.items.find(function(val){
        if (item.id == val.id) {
          self.model.uomDetails = val.uomDetails;
        }
      });
    }

    var NavigateToBack = function (){
      $state.go('loanInOut');
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailsLoanInOut", {
      url: "/detailsLoanInOut",
      templateUrl: 'views/procurement/consignment/details-loan-in-out.html',
      controller: "DetailsLoanInOut.Controller",
      controllerAs: "vm",
      title: 'Loan In Out'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("DetailsLoanInOut.Controller", detailsLoanInOutController);
})();