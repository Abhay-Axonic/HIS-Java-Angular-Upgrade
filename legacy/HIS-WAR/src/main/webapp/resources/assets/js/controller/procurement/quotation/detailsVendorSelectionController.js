(function () {
  "use strict";

  function detailsVendorSelectionController($scope, $rootScope, $http, $filter, $state, GenericService, commonDetailService, growl, StatusService, $cookies , CONSTANTS) {
    var self = this;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
    function initializeController() {
      $rootScope.loginpage = true;

      self.model = {
        comparisonDetailsModal: {},
        btnTypeId: null
      };

      StatusService.GetStatus().then(function (status) {
        self.model.statuses = status;
      });

      self.model.SaveRecord = SaveRecord;
      self.model.ViewTermsModal = ViewTermsModal;
      self.model.SaveTerms = SaveTerms;
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
          GetApprovalDetails(approvalId);
        }
      } catch (error) {
        growl.error(error);
      }
    }

    var GetApprovalDetails = function (approvalId) {
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.VENDOR_SELECTION.DETAILS_API + approvalId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.approvalId = response.data.id;
          self.model.qrNumber = response.data.qrNumber;
          self.model.itemInfo = response.data.items;

          self.model.itemInfo.forEach(function(item) {
            var vendorSelectionDetails = item.vendorSelectionDetails;
            vendorSelectionDetails.forEach(function(vendorDetail){
              if(!vendorDetail.quantity) {
                vendorDetail.quantity = item.quantity;
              }
              if (vendorDetail.isAccept) {
                vendorDetail.setVendorStatus = 1;
                self.model.btnTypeId = 1;
              } else{
                vendorDetail.setVendorStatus = 2;
                self.model.btnTypeId = 2;
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

    var SaveTerms = function(modelName) {
      var saveTerm = self.model.itemInfo.find(function(item){
        return (self.model.comparisonDetailsModal.currentItemId == item.id)
      });

      if(saveTerm){
        switch(modelName){
          case "warrantyPeriodModal":
            saveWarrentyPeriod(saveTerm);
          break;
          case "supportTermsModal":
            saveSupportTerms(saveTerm);
          break;
          case "paymentTermsModal":
            savePaymentTerms(saveTerm);
          break;
          case "techSpecModal":
            saveTechnicalSpec(saveTerm);
          break;
        } 
      }
    }

    var saveWarrentyPeriod = function(saveTerm){
      var data = self.model.comparisonDetailsModal.negotiationWarrantyPeriod;
      self.model.comparisonDetailsModal.negotiationWarrantyPeriod.forEach(function(vendorDetail){
        var obj = saveTerm.vendorSelectionDetails.find(function(vendor){
          return (vendor.id == vendorDetail.id);
        });
        if(obj){
          obj.warrantyFlag = vendorDetail.warrantyFlag;
          obj.warrantyRejectNote = vendorDetail.warrantyRejectNote;
          self.model.PopupAction('Close', 'warrantyPeriodModal');
        }
      });
    }

    var saveSupportTerms = function(saveTerm){
      var data = self.model.comparisonDetailsModal.negotiationSupportTerms;
      self.model.comparisonDetailsModal.negotiationSupportTerms.forEach(function(vendorDetail){
        var obj = saveTerm.vendorSelectionDetails.find(function(vendor){
          return (vendor.id == vendorDetail.id);
        });
        if(obj){
          obj.supportFlag = vendorDetail.supportFlag;
          obj.supportRejectNote = vendorDetail.supportRejectNote;
          self.model.PopupAction('Close', 'SupportTermsModal');
        }
      })
    }

    var saveTechnicalSpec = function(saveTerm){
      var data = self.model.comparisonDetailsModal.negotiationTechSpec;
      self.model.comparisonDetailsModal.negotiationTechSpec.forEach(function(vendorDetail){
        var obj = saveTerm.vendorSelectionDetails.find(function(vendor){
          return (vendor.id == vendorDetail.id);
        });
        if(obj){
          obj.technicalSpecFlag = vendorDetail.technicalSpecFlag;
          obj.technicalSpecRejectNote = vendorDetail.technicalSpecRejectNote;
          self.model.PopupAction('Close', 'TechnicalSpecificationModal');
        }
      })
    }

    var savePaymentTerms = function(saveTerm){
      var data = self.model.comparisonDetailsModal.negotiationPaymentTerms;
      self.model.comparisonDetailsModal.negotiationPaymentTerms.forEach(function(vendorDetail){
        var obj = saveTerm.vendorSelectionDetails.find(function(vendor){
          return (vendor.id == vendorDetail.id);
        });
        if(obj){
          obj.paymentFlag = vendorDetail.paymentFlag;
          obj.paymentRejectNote = vendorDetail.paymentRejectNote;
          self.model.PopupAction('Close', 'PaymentTermsModal');
        }
      })
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

    var SaveRecord = function (action) {
      if (action == 'Save') {
        self.model.dataStatus = setStatus('Pending');
      } else if (action == 'Send') {
        self.model.dataStatus = setStatus('Closed');
      }
      if(ValidateQuantity()) {
        SaveVendorSelection();
      }
    }

    var SaveVendorSelection = function () {
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.VENDOR_SELECTION.DETAILS_API + self.model.approvalId;

      var data = {
        "statusId": self.model.dataStatus,
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId
      };

      angular.forEach(self.model.itemInfo, function (item, key) {
        var innerData = {
          "id": item.id,
          "statusId": self.model.dataStatus,
          "vendorSelectionDetails": []
        }

        for (var vendorDetail in item.vendorSelectionDetails) {
          if (item.vendorSelectionDetails.hasOwnProperty(vendorDetail)) {
            var details = {
              "id": item.vendorSelectionDetails[vendorDetail].id,
              "isAccept": item.isAccept,
              "warrantyPeriod": item.vendorSelectionDetails[vendorDetail].warrantyFlag,
              "supportTerm": item.vendorSelectionDetails[vendorDetail].supportFlag,
              "technicalSpecification": item.vendorSelectionDetails[vendorDetail].technicalSpecFlag,
              "expectedPayment": item.vendorSelectionDetails[vendorDetail].paymentFlag,
              "quantity": item.vendorSelectionDetails[vendorDetail].quantity,
              "remark": item.vendorSelectionDetails[vendorDetail].remark,
              "rejectNote": item.vendorSelectionDetails[vendorDetail].rejectNote
            }

            if (item.vendorSelectionDetails[vendorDetail].setVendorStatus == 1) {
              details["isAccept"] = true;
              details["rejectNote"] = null;
            } else if(item.vendorSelectionDetails[vendorDetail].setVendorStatus == 2) {
              details["isAccept"] = false;
            }

            innerData.vendorSelectionDetails.push(details);
          }
        }
 
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

    var setStatus = function (data) {
      var status = self.model.statuses.find(function (obj) {
        return obj.status == data;
      });

      return status.id;
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
//      var list = [];
//      self.model.itemInfo.forEach(function(data){
//        data.vendorSelectionDetails.forEach(function(vendorDetail){
//          list.push(vendorDetail.setVendorStatus);
//        });
//      });
//
//      if (list.indexOf(3) !== -1) {
//        self.model.buttonFlag = true;
//      } else {
//        self.model.buttonFlag = false;
//      }
    }

    var NavigateToBack = function () {
      $state.go('vendorSelection');
    }

    var ValidateQuantity = function () {
      var isValid = true;
      for(let item of self.model.itemInfo) {
        for (let vendorDetail of item.vendorSelectionDetails) {
          if(!vendorDetail.quantity) {
            vendorDetail.isQuantityValid = true;
            isValid = false;
            growl.error("Enter Quantity");
          }
        }
      }
      return isValid;
    }

    initializeController();
  }
 
  function config($stateProvider) {
    $stateProvider.state("detailsVendorSelection", {
      url: "/detailsVendorSelection", 
      templateUrl: 'views/procurement/quotation/details-vendor-selection.html',
      controller: "DetailsVendorSelection.Controller",
      controllerAs: "vm",
      title: 'Vendor Selection',
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("DetailsVendorSelection.Controller", detailsVendorSelectionController);
})();