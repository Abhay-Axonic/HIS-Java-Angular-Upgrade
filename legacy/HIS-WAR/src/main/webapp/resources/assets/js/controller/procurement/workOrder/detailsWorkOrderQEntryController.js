(function () {
  "use strict";

  function detailsWorkOrderQEntryController($rootScope, $scope, $http, $cookies, CONSTANTS, $state, GenericService, commonDetailService, growl) {
    var self = this;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
      $rootScope.loginpage = true;

      self.model = {};
      self.model.NavigateToBack = NavigateToBack;
      self.model.OpenPopup = OpenPopup;
      self.model.ClosePopup = ClosePopup;
      self.model.ViewExclusion = ViewExclusion;
      self.model.ViewInclusion = ViewInclusion;
      self.model.ViewPaymentTerms = ViewPaymentTerms;

      GetQuotationEnquiry(commonDetailService.getDataId());
    }

    var GetQuotationEnquiry = function (quotationId) {
      commonDetailService.setDataId(null);

      var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.QUOTATION_ENTRY.DETAILS_API + quotationId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.quotationList = response.data;
          $rootScope.stopLoader();
        },
        function (err) {
          growl.error(err.data.message);
          $rootScope.stopLoader();
        });
    }

    var ViewExclusion = function (item) {
      self.model.expectedExclusion = item.expectedExclusion;
    }

    var ViewInclusion = function (item) {
      self.model.expectedInclusion = item.expectedInclusion;
    }

    var ViewPaymentTerms = function (item) {
      self.model.paymentTerm = item.paymentTerms;
    }

    var NavigateToBack = function (){
      $state.go('workorderQuotationEntry');
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

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailsWorkorderQEntry", {
      url: "/detailsWorkorderQEntry",
	  templateUrl: 'views/procurement/workOrder/details-workorder-quotation-entry.html',
      controller: "DetailsWorkorderQEntry.Controller",
      controllerAs: "vm",
      title: 'Work Order Quotation Entry'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("DetailsWorkorderQEntry.Controller", detailsWorkOrderQEntryController);
})();