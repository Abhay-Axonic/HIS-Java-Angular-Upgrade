(function () {
  "use strict";

  function viewQuotationSelectionController($scope, $rootScope, $http, $filter, $state, GenericService, commonDetailService, growl, StatusService, CONSTANTS) {
    var self = this;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
      $rootScope.loginpage = true;

      self.model = {
        comparisonDetailsModal: {}
      };

      StatusService.GetStatus().then(function (status) {
        self.model.statuses = status;
      });

      self.model.SaveRecord = SaveRecord;
      self.model.ViewTermsModal = ViewTermsModal;
      self.model.NavigateToBack = NavigateToBack;
      self.model.PopupAction = PopupAction;

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
          GetApprovalDetails(approvalId);
        }
      } catch (error) {
        growl.error(error);
      }
    }

    var GetApprovalDetails = function (approvalId) {
      var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.QUOTATION_SELECTION.DETAILS_API + approvalId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
         
          self.model.approvalId = response.data.id;
          self.model.qrNumber = response.data.qrNo;
          self.model.itemInfo = response.data.detail;

          self.model.itemInfo.forEach(function(item) {
            var vendorSelectionDetails = item.mapper;
             vendorSelectionDetails.forEach(function(vendorDetail){
              if (vendorDetail.isAccepted) {
                vendorDetail.setVendorStatus = 1;
              } else{
                vendorDetail.setVendorStatus = 2;
              }
            }); 
          });
          // self.model.ButtonVisible();

          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
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
    
    var ViewTermsModal = function (item, viewModal) {
      self.model.comparisonDetailsModal.currentItemId = item.id;

      switch(viewModal){
        case "inclusionDetails":
          self.model.comparisonDetailsModal.inclusionDetails = item.inclusionDetails;
          self.model.comparisonDetailsModal.negotiationInclusionDetails = JSON.parse(JSON.stringify(item.mapper));
        break;
        case "exclusionDetails":
          self.model.comparisonDetailsModal.exclusionDetails = item.exclusionDetails;
          self.model.comparisonDetailsModal.negotiationExclusionDetails = JSON.parse(JSON.stringify(item.mapper));
        break;
        
        case "paymentTerms":
          self.model.comparisonDetailsModal.paymentTerms = item.paymentTerms;
          self.model.comparisonDetailsModal.negotiationPaymentTerms = JSON.parse(JSON.stringify(item.mapper));
        break;
      }
    }

    var ValidItems = function() {
      var list = [];
      var isValid = true;
      angular.forEach(self.model.itemInfo, function (item) {
        angular.forEach(item.negotiationDetails, function(detail){
          if (!detail.id) {
            list.push(detail);
          }
        });
      });

      if (!list || !list.length) {
        growl.error('No Vendor To Update');
        isValid = false;
      }

      return isValid;
    }

    var SaveRecord = function (action) {
      if (ValidItems()) {
        // if (action  == 'save') {
        CreateVendorNegotiation(action);
        // } else if (action  == 'send') {
          // CreateVendorNegotiation();
        // }
      }
    }

    var CreateVendorNegotiation = function (action) {
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.VENDOR_NEGOTIATION.DETAILS_API + self.model.nigotiationId;

      var data = {
        "qrId": self.model.qrId,
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId,

      };
      if (action == 'save') {
        data.statusId = setStatus('Pending');
      } else if(action == 'send'){
        data.statusId = setStatus('Completed');
      }

      angular.forEach(self.model.itemInfo, function (item, key) {
        var innerData = {
          "id": item.id,
          "qrDetailId": item.qrDetailId,
          "negotiationDetails": {}
        }

        angular.forEach(item.negotiationDetails, function(detail){
          if (!detail.id) {
            innerData.negotiationDetails = {
              "negotiationDate": moment(detail.negotiationDate).format("YYYY-MM-DD"),
              "negotiationCop": parseFloat(detail.negotiationCop),
              "negotiationWarrantyPeriod": detail.negotiationWarrantyPeriod,
              "negotiationSupportTerms": detail.negotiationSupportTerms,
              "negotiationTechSpec": detail.negotiationTechSpec,
              "negotiationPaymentTerms": detail.negotiationPaymentTerms,
              "negotiationRemark": detail.negotiationRemark,
              "rejectNote": detail.negotiationRemark,
              "accept": false,
              "reject": false,
              "onHold": false
            }

            if (detail.negotitateStatus == 1) {
              innerData.negotiationDetails["accept"] = true;
              innerData.negotiationDetails["rejectNote"] = null;
            } else if(detail.negotitateStatus == 2) {
              innerData.negotiationDetails["reject"] = true;
            } else if (detail.negotitateStatus == 3) {
              innerData.negotiationDetails["onHold"] = true;
              innerData.negotiationDetails["rejectNote"] = null;
            }
          }
        });

        data.items.push(innerData);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          NavigateToBack();
          growl.success(response.data.message);
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
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

    var setStatus = function (x) {
      var status = self.model.statuses.find(function (obj) {
        return obj.status == x;
      });

      return status.id;
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
      $state.go('quotationSelection');
    }

    initializeController();
  }
 
  function config($stateProvider) {
    $stateProvider.state("viewQuotationSelect", {
      url: "/viewQuotationSelect",
	  templateUrl: 'views/procurement/workOrder/view-quotation-selection.html',
      controller: "viewQuotationSelection.Controller",
      controllerAs: "vm",
      title: 'Work Order Quotation Selection'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("viewQuotationSelection.Controller", viewQuotationSelectionController);
})();




				