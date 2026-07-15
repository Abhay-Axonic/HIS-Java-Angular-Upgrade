(function () {
  "use strict";

  function detailsQuotationSentController($rootScope, $scope, $http, $cookies, CONSTANTS, $state, GenericService, commonDetailService, growl) {
    var self = this;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
    function initializeController() {
      $rootScope.loginpage = true;

      self.model = {
        prDetailList: [],
        expectedAccessories: [],
        termsVisible: 1
      };
      self.model.NavigateToBack = NavigateToBack;
      self.model.AddInclusion = AddInclusion;
      self.model.ViewRequestDetails = ViewRequestDetails;
      self.model.OpenPopup = OpenPopup;
      self.model.ClosePopup = ClosePopup;

      GetQuotationEnquiry(commonDetailService.getDataId());
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

    var GetQuotationEnquiry = function (quotationId) {
      commonDetailService.setDataId(null);

      var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.QUOTATION_SENT_TO_VENDOR.DETAILS_API + quotationId;

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

    var AddInclusion = function (item) {
      self.model.expectedInclusion = item.expectedInclusion;
    }

    var ViewRequestDetails = function (item) {
    
      self.model.requestList = item.requestDetails;
    }

    var NavigateToBack = function (){
      $state.go('quotationSendToVendor');
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailsQuotationS", {
      url: "/detailsQuotationS",
      templateUrl: 'views/procurement/workOrder/details-quotation-send-to-vendor.html',
      controller: "DetailsQuotationS.Controller",
      controllerAs: "vm",
      title: "Quotation Sent To Vendor"
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("DetailsQuotationS.Controller", detailsQuotationSentController);
})();