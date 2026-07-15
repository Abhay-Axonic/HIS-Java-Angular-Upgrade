(function () {
  "use strict";

  function detailsWorkOrderController($rootScope, $scope, $http, $cookies, CONSTANTS, $state, GenericService, commonDetailService, growl) {
    var self = this;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
      $rootScope.loginpage = true;

      self.model = {};
      self.model.NavigateToBack = NavigateToBack;
      GetWorkOrder(commonDetailService.getDataId());
    }

    var GetWorkOrder = function (orderId) {
      commonDetailService.setDataId(null);

      var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_REQUEST.DETAILS_API + $scope.unitId + '/' + orderId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.workOrderDetails = response.data;
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var NavigateToBack = function (){
      $state.go('workorderRequest');
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailsWorkorder", {
      url: "/detailsWorkorder",
      templateUrl: 'views/procurement/workOrder/details-workorder-request.html',
      controller: "DetailsWorkOrder.Controller",
      controllerAs: "vm",
      title: 'Work Order Request'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("DetailsWorkOrder.Controller", detailsWorkOrderController);
})();