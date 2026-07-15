(function () {
    "use strict";
  
    function detailsAuctionVendorSelectionController($scope, $rootScope, $http, $filter, $state, GenericService, commonDetailService, growl, StatusService, CONSTANTS) {
      var self = this;
  
      var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
        $rootScope.loginpage = true;
  
        self.model = {
          comparisonDetailsModal: {},
          btnTypeId: null
        };
  
        self.model.ViewTermsModal = ViewTermsModal;
        self.model.NavigateToBack = NavigateToBack;
        self.model.PopupAction = PopupAction;
        self.model.ButtonVisible = ButtonVisible;
  
        PopulateDropdown();
      }
  
      var PopulateDropdown = function () {
        try {
          $rootScope.startLoader();
          var approvalId = commonDetailService.getDataId();
          if (!!approvalId) {
            $rootScope.stopLoader();
            commonDetailService.setDataId(null);
          }
        } catch (error) {
          growl.error(error);
        }
      }
  
      var ViewTermsModal = function (item, viewModal) {
        self.model.comparisonDetailsModal.currentItemId = item.id;
  
        switch(viewModal){
          case "warrantyPeriod":
            self.model.comparisonDetailsModal.warrantyPeriod = item.warrantyPeriod;
            self.model.comparisonDetailsModal.negotiationWarrantyPeriod = JSON.parse(JSON.stringify(item.vendorSelectionDetails));
          break;
          case "supportTerms":
            self.model.comparisonDetailsModal.supportTerms = item.supportTerms;
            self.model.comparisonDetailsModal.negotiationSupportTerms = JSON.parse(JSON.stringify(item.vendorSelectionDetails));
          break;
          case "techSpecs":
            self.model.comparisonDetailsModal.technicalSpecifications = item.technicalSpecifications;
            self.model.comparisonDetailsModal.negotiationTechSpec = JSON.parse(JSON.stringify(item.vendorSelectionDetails));
          break;
          case "paymentTerms":
            self.model.comparisonDetailsModal.paymentTerms = item.paymentTerms;
            self.model.comparisonDetailsModal.negotiationPaymentTerms = JSON.parse(JSON.stringify(item.vendorSelectionDetails));
          break;
        }
      }
  
      var PopupAction = function (action, modal) {
        var popup = angular.element('#' + modal);
        self.model.action = action;
        if (action == 'Close') {
          popup.modal('hide');
        } else if (action == 'Open') {
          popup.modal('show');
        }
      }
  
      var ButtonVisible = function (type) {
        self.model.btnTypeId = type;
      }
  
      var NavigateToBack = function () {
        $state.go('auctionVendorSelection');
      }
  
      initializeController();
    }
    
    function config($stateProvider) {
      $stateProvider.state("detailsAuctionVendorSelection", {
        url: "/detailsAuctionVendorSelection",
        templateUrl: 'views/master/GlobalMaster/AssetManagenent/Master/details-auction-vendor-selection.html',
        controller: "detailsAuctionVendorSelection.Controller",
        controllerAs: "vm",
        title: 'Auction Vendor Selection'
      });
    }
  
    angular
      .module("myApp")
      .config(config)
      .controller("detailsAuctionVendorSelection.Controller", detailsAuctionVendorSelectionController);
  })();