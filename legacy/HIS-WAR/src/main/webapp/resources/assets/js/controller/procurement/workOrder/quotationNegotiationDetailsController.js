(function () {
  "use strict";

  function quotationNegotiationDetailsController($scope, $rootScope, $http, $filter, $state, GenericService, commonDetailService, growl, StatusService, CONSTANTS) {
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
          self.model.vendorId = response.data.vendorId;
          self.model.vendorName = response.data.vendorName;
          self.model.refNumber = response.data.refNumber;
          self.model.refDate = response.data.refDate;
          self.model.addedBy = response.data.addedBy;
          self.model.itemInfo = response.data.detail;
          self.model.qrId = response.data.qrId;
          self.model.contractType = response.data.contractType;
          self.model.status = response.data.status;
          self.model.itemInfo.forEach(function(item) {
            var negotiationDetails = item.negotiationDetails;
            var lastData = negotiationDetails[negotiationDetails.length-1];
            
            if (negotiationDetails.length < 5 && negotiationDetails.length > 0 && lastData.accept != true) {
              negotiationDetails.push({
                "negotiationDate": new Date(),
                "negotiationQuantity": lastData.negotiationQuantity,
                "negotiationAmount": lastData.negotiationAmount,
                "negotiationInclusion": lastData.negotiationInclusion,
                "negotiationExclusion": lastData.negotiationExclusion,
                "negotiationPaymentTerms": lastData.negotiationPaymentTerms,
                "negotiationRemark": lastData.negotiationRemark,
                "accept": lastData.accept,
                "reject": lastData.reject,
                "onHold": lastData.onHold,
                "rejectNote": lastData.rejectNote
              });
            } else if (negotiationDetails.length <= 0) {
              negotiationDetails.push({
                "negotiationDate": new Date(),
                "negotiationQuantity": item.negotiationQuantity,
                "negotiationAmount": item.negotiationAmount,
                "negotiationInclusion": item.negotiationInclusion,
                "negotiationExclusion": item.negotiationExclusion,
                "negotiationPaymentTerms": item.negotiationPaymentTerms,
                "negotiationRemark": item.negotiationRemark,
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
        growl.error('No vendor to update');
        isValid = false;
      }

      return isValid;
    }

    var SaveRecord = function (action) {
      if (ValidItems()) {
        CreateVendorNegotiation(action);
      }
    }

    var CreateVendorNegotiation = function (action) {
      var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.QUOTATION_NEGOTIATION.DETAILS_API + self.model.nigotiationId;

      var data = {
        //"vendorQuotationId": self.model.qrId,
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId
      };
      
      angular.forEach(self.model.itemInfo, function (item) {
        var innerData = {
           "id": item.id,
           "mapperList":[]
        }
         angular.forEach(item.negotiationDetails, function(detail){
          if (!detail.id) {
            var newdata  = {
              "negotiationQuantity": detail.negotiationQuantity,
              "negotiationAmount": parseFloat(detail.negotiationAmount),
              "negotiationInclusion": detail.negotiationInclusion,
              "negotiationExclusion": detail.negotiationExclusion,
              "negotiationPaymentTerms": detail.negotiationPaymentTerms,
              "negotiationRemark": detail.negotiationRemark,
              "rejectNote": detail.rejectNote,
            }

            if (detail.negotitateStatus == 1) {
              newdata["accept"]=true;
              newdata["reject"]=false;
              newdata["onHold"]=false;
              innerData.mapperList["accept"] = true;
              innerData.mapperList["rejectNote"] = null;
            } else if(detail.negotitateStatus == 2) {
              newdata["accept"]=false;
              newdata["reject"]=true;
              newdata["onHold"]=false;
              innerData.mapperList["reject"] = true;
            } else if (detail.negotitateStatus == 3) {
              newdata["accept"]=false;
              newdata["reject"]=false;
              newdata["onHold"]=true;
              innerData.mapperList["onHold"] = true;
              innerData.mapperList["rejectNote"] = null;
            }
            innerData.mapperList["vendorId"]=self.model.vendorId;
          }
          innerData.mapperList.push(newdata);
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

    var setStatus = function (data) {
      var status = self.model.statuses.find(function (obj) {
        return obj.status == data;
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
    $stateProvider.state("detailsQuotationNegotiation", {
      url: "/detailsQuotationNegotiation",
	  templateUrl: 'views/procurement/workOrder/details-quotation-negotiation.html',
      controller: "DetailsQuotationNegotiation.Controller",
      controllerAs: "vm",
      title: 'Quotation Negotiation'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("DetailsQuotationNegotiation.Controller", quotationNegotiationDetailsController);
})();




				