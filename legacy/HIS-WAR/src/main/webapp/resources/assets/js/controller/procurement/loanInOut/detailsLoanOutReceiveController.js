(function () {
  "use strict";

  function detailsLoanOutReceiveController($rootScope, $scope, $http, $cookies, CONSTANTS, $state, GenericService, commonDetailService, growl, CurrencyService) {
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

    var GetLoanIn = function (loanOutId) {
      commonDetailService.setDataId(null);

      var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_OUT_RECEIVE.DETAILS_API + $scope.unitId + '/' + loanOutId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.assetTypeId = response.data.assetTypeId;
          self.model.loanOutReceiveData = response.data;
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var ViewUomDetails = function (item) {
      self.model.loanOutReceiveData.items.find(function(x){
        if (item.id == x.id) {
          self.model.uomDetails = x.uomDetails;
        }
      });
    }

    var NavigateToBack = function (){
      $state.go('loanOutReceive');
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailsLoanOutReceive", {
      url: '/detailsLoanOutReceive',
      templateUrl: 'views/procurement/consignment/details-loan-out-receive.html',
      controller: "DetailsLoanOutReceive.Controller",
      controllerAs: "vm",
      title: 'Loan Out Receive'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("DetailsLoanOutReceive.Controller", detailsLoanOutReceiveController);
})();