(function () {
    "use strict";
  
    function viewAuctionVendorNegotiationController($scope, $rootScope, $http, $filter, $state, GenericService, commonDetailService, growl, StatusService, CONSTANTS) {
      var self = this;
  
      var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
        $rootScope.loginpage = true;
  
        self.model = {
          comparisonDetailsModal: {}
        };
  
        self.model.ViewTermsModal = ViewTermsModal;
        self.model.NavigateToBack = NavigateToBack;
        self.model.OpenPopup = OpenPopup;
        self.model.ClosePopup = ClosePopup;
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
          case "cop":
            self.model.title = "Expected COP";
            self.model.vendorTitle = "Vendor COP";
            self.model.comparisonDetailsModal.term = item.cop;
            self.model.comparisonDetailsModal.vendorTerm = item.vendorCop;
          break;
          case "warrantyPeriod":
            self.model.title = "Expected Warranty Period";
            self.model.vendorTitle = "Vendor Warranty Period";
            self.model.comparisonDetailsModal.term = item.warrantyPeriod;
            self.model.comparisonDetailsModal.vendorTerm = item.vendorWarrantyPeriod;
          break;
          case "supportTerms":
            self.model.title = "Expected Support Terms";
            self.model.vendorTitle = "Vendor Support Terms";
            self.model.comparisonDetailsModal.term = item.supportTerms;
            self.model.comparisonDetailsModal.vendorTerm = item.vendorSupportTerms;
          break;
          case "techSpecs":
            self.model.title = "Expected Technical Specification";
            self.model.vendorTitle = "Vendor Technical Specification";
            self.model.comparisonDetailsModal.term = item.technicalSpecifications;
            self.model.comparisonDetailsModal.vendorTerm = item.vendorTechSpec;
          break;
          case "paymentTerms":
            self.model.title = "Expected Payment Terms";
            self.model.vendorTitle = "Vendor Payment Terms";
            self.model.comparisonDetailsModal.term = item.paymentTerms;
            self.model.comparisonDetailsModal.vendorTerm = item.vendorPaymentTerms;
          break;
          case "remark":
            self.model.title = "Expected Remark";
            self.model.vendorTitle = "Vendor Remark";
            self.model.comparisonDetailsModal.term = item.remark;
            self.model.comparisonDetailsModal.vendorTerm = item.vendorRemark;
          break;
        }
      }
  
      var ButtonVisible = function () {
        var list = [];
        self.model.itemInfo.forEach(function(data){
          data.negotiationDetails.forEach(function(detail){
            if (!detail.id) {
              list.push(detail.negotitateStatus);
            }
          });
        });
  
        if (list.indexOf(3) !== -1) {
          self.model.buttonFlag = true;
        } else {
          self.model.buttonFlag = false;
        }
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
  
      var NavigateToBack = function () {
        $state.go('auctionVendorNegotiation');
      }
  
      initializeController();
    }
    
    function config($stateProvider) {
      $stateProvider.state("viewAuctionVendorNegotiation", {
        url: "/viewAuctionVendorNegotiation",
        templateUrl: 'views/master/GlobalMaster/AssetManagenent/Master/view-Auction-Vendor-negotiation.html',
        controller: "viewAuctionVendorNegotiation.Controller",
        controllerAs: "vm",
        title: 'Vendor Negotiation'
      });
    }
  
    angular
      .module("myApp")
      .config(config)
      .controller("viewAuctionVendorNegotiation.Controller", viewAuctionVendorNegotiationController);
  })();