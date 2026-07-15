(function () {
  "use strict";

  function viewQuotationNegotiationController($scope, $rootScope, $http, $filter, $state, GenericService, commonDetailService, growl, StatusService, CONSTANTS) {
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
          GetVendorDetails(approvalId);
        }
      } catch (error) {
        growl.error(error);
      }
    }

    var GetVendorDetails = function (approvalId) {
      var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.QUOTATION_NEGOTIATION.DETAILS_API + approvalId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.nigotiationId = response.data.id;
          self.model.qrNumber = response.data.qrNumber;
          self.model.qrDate = response.data.qrDate;
          self.model.vqrNumber = response.data.vqrNumber;
          self.model.vqrDate = response.data.vqrDate;
          self.model.storeName = response.data.storeName;
          self.model.vendorName = response.data.vendorName;
          self.model.refNumber = response.data.refNumber;
          self.model.refDate = response.data.refDate;
          self.model.contractType = response.data.contractType;
          self.model.addedBy = response.data.addedBy;
          self.model.itemInfo = response.data.detail;
          self.model.qrId = response.data.qrId;
          self.model.columnNumber = response.data.detail[0].negotiationDetails.length;
          self.model.itemInfo.forEach(function(item) {
            var negotiationDetails = item.negotiationDetails;
            var dataLength = negotiationDetails.length;
            var lastData = negotiationDetails[dataLength-1];
            if (negotiationDetails.length < 5 && negotiationDetails.length > 0 && lastData.accept != true) {
              negotiationDetails.push({
                "negotiationDate": new Date(),
                "negotiationCop": lastData.negotiationCop,
                "negotiationRemark": lastData.negotiationRemark,
                "negotiationInclusion": lastData.negotiationInclusion,
                "negotiationExclusion": lastData.negotiationExclusion,
                "negotiationPaymentTerms": lastData.negotiationPaymentTerms,
                "accept": lastData.accept,
                "reject": lastData.reject,
                "onHold": lastData.onHold,
                "rejectNote": lastData.rejectNote
              });
            } else if (negotiationDetails.length <= 0) {
              negotiationDetails.push({
                "negotiationDate": new Date(),
                "negotiationCop": item.vendorCop,
                "negotiationRemark": item.vendorRemark,
                "negotiationInclusion": item.vendoInclusion,
                "negotiationExclusion": item.vendorExclusion,
                "negotiationPaymentTerms": item.vendorPaymentTerms,
                "accept": item.accept,
                "reject": item.reject,
                "onHold": item.onHold,
                "rejectNote": ""
              });
            }

            negotiationDetails.forEach(function(detail){
              if (detail.accept == true) {
                detail.negotitateStatus = 1;
              } else if (detail.reject == true) {
                detail.negotitateStatus = 2;
              } else if (detail.onHold == true) {
                detail.negotitateStatus = 3;
              }
            });
          });
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var ViewTermsModal = function (item, viewModal) {
      self.model.comparisonDetailsModal.currentItemId = item.id;

      switch(viewModal){
        case "inclusionDetails":
          self.model.title = "Inclusion Details";
          self.model.comparisonDetailsModal.term = item.inclusionDetails;
        break;
        case "exclusionDetails":
          self.model.title = "Exclusion Details";
          self.model.comparisonDetailsModal.term = item.exclusionDetails;
        break;
        case "paymentTerms":
          self.model.title = "Payment Terms";
          self.model.comparisonDetailsModal.term = item.paymentTerms;
        break;
        case "remark":
          self.model.title = "Remark";
          self.model.comparisonDetailsModal.term = item.remark;
        break;
       /*
        case "paymentTerms":
          self.model.title = "Expected Payment Terms";
          self.model.vendorTitle = "Vendor Payment Terms";
          self.model.comparisonDetailsModal.term = item.paymentTerms;
          self.model.comparisonDetailsModal.vendorTerm = item.vendorPaymentTerms;
        break;
        */
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
              "negotiationInclusion": detail.negotiationInclusion,
              "negotiationExclusion": detail.negotiationExclusion,
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
      $state.go('quotationNegotiation');
    }

    initializeController();
  }
 
  function config($stateProvider) {
    $stateProvider.state("viewQuotationNegotiation", {
      url: "/viewQuotationNegotiation",
	  templateUrl: 'views/procurement/workOrder/view-quotation-negotiation.html',
      controller: "viewQuotationNegotiation.Controller",
      controllerAs: "vm",
      title: 'Quotation Negotiation'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("viewQuotationNegotiation.Controller", viewQuotationNegotiationController);
})();




				