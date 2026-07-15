(function () {
    "use strict";
  
    function detailsSettleController($rootScope, $scope, $http, $cookies, CONSTANTS, $state, GenericService, commonDetailService, growl, CurrencyService) {
      var self = this;

      var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
        $rootScope.loginpage = true;
        self.model = {
          grnSettledList: {
            items: []
          }
        };

        self.model.GetGRNSettledList = GetGRNSettledList;
        self.model.NavigateToBack = NavigateToBack;
        GetGRNSettledList();
      }

      var NavigateToBack = function () {
        commonDetailService.setDataId(2);
        $state.go('grnSettlement');
      }

      var GetGRNSettledList = function () {
        var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN_SETTLEMENT.DETAILS_GRN_SETTLED_API + commonDetailService.getDataId() + "/" + $scope.unitId;
        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, {}).then(function (response) {
          $rootScope.stopLoader();
          self.model.grnSettledList = response.data;
          angular.forEach(self.model.grnSettledList.settledDetails,function(value, key) {
              if(value.invoicedate.toString().includes("-"))
                  {
                  value.stringDate=true;
                  }
          });
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
      }

      initializeController();
    }

    function config($stateProvider) {
      $stateProvider.state("detailsSettle", {
        url: "/detailsSettle",
       templateUrl: 'views/procurement/quotation/details-settled-list.html',
        controller: "detailsSettle.Controller",
        controllerAs: "vm",
        title: 'Good Receipt Note Settlement'
      });
    }
  
    angular
      .module("myApp")
      .config(config)
      .controller("detailsSettle.Controller", detailsSettleController);
  })();