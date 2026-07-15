(function () {
  "use strict";

  function detailsWorkOrderQuotationController($rootScope, $scope, $http, $cookies, CONSTANTS, $state, GenericService, commonDetailService, growl) {
    var self = this;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
      $rootScope.loginpage = true;

      self.model = {};
      self.model.NavigateToBack = NavigateToBack;
      self.model.AddInclusion = AddInclusion;
      self.model.ViewRequestDetails = ViewRequestDetails;
      self.model.OpenPopup = OpenPopup;
      self.model.ClosePopup = ClosePopup;

      GetWorkOrder(commonDetailService.getDataId());
    }

    var OpenPopup = function (action, modal) {
      self.model.action = action;
      var popup = angular.element('#' + modal);
      popup.modal('show');
    }

    var ClosePopup = function (action, modal) {
      self.model.action = action;
      var popup = angular.element('#' + modal);
      popup.modal('hide');
    }

    var GetWorkOrder = function (orderId) {
      commonDetailService.setDataId(null);

      var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_QUOTATION_REQUEST.DETAILS_API + $scope.unitId + '/' + orderId;

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

    var AddInclusion = function (item) {
      self.model.expectedInclusion = item.expectedInclusion;
    }

    var ViewRequestDetails = function (item) {
      self.model.requestList = item.requestDetails;
    }

    var NavigateToBack = function (){
      $state.go('workorderQuotationRequest');
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailsWorkorderQuotation", {
      url: "/detailsWorkorderQuotation",
      templateUrl: 'views/procurement/workOrder/details-workorder-quotation-request.html',
      controller: "DetailsWorkOrderQuotation.Controller",
      controllerAs: "vm",
      title: 'Work Order Quotation Request'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("DetailsWorkOrderQuotation.Controller", detailsWorkOrderQuotationController);
})();