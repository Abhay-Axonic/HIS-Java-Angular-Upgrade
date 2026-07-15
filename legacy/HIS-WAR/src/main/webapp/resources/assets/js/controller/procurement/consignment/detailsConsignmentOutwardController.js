(function () {
  "use strict";

  function detailsConsignmentOutwardController($rootScope, $scope, $http, $cookies, CONSTANTS, $state, GenericService, CurrencyService, commonDetailService, growl) {
    var self = this;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
      $rootScope.loginpage = true;

      self.model = {};
      CurrencyService.GetCurrency().then(function (currency) {
        self.model.Currency = currency;
      })
      self.model.NavigateToBack = NavigateToBack;
      if (localStorage.getItem('outwardId')) {
        GetConsignOutwardRequests(localStorage.getItem('outwardId'))
      } else {
        var outwardId = commonDetailService.getDataId();
        $rootScope.stopLoader();
        if (!!outwardId) {
          GetConsignOutwardRequests(outwardId);
        } else {
          return false;
        }
      }
      // GetConsignOutwardRequests(commonDetailService.getDataId());
    }

    var GetConsignOutwardRequests = function (outwardId) {
      commonDetailService.setDataId(null);
      let id;
      if (outwardId) {
        id = outwardId
      } else {
        id = localStorage.getItem('outwardId')
      }
      var URI = CONSTANTS.PROCUREMENT.CONSIGNMENT.CONSIGNMENT_OUTWARD.DETAILS_API + $scope.unitId + '/' + id;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {

          self.model.consignOutwardRequestsDetails = response.data;

          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var NavigateToBack = function () {
      $state.go('consignmentOutward');
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailsConsignmentOutward", {
      url: "/detailsConsignmentOutward",
      templateUrl: 'views/procurement/consignment/details-consignment-outward.html',
      controller: "DetailsConsignmentOutward.Controller",
      controllerAs: "vm",
      title: 'Consignment Outward'
    });

  }

  angular
    .module("myApp")
    .config(config)
    .controller("DetailsConsignmentOutward.Controller", detailsConsignmentOutwardController);
})();