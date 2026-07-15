(function () {
  "use strict";

  function detailsQuotationSelectController($scope, $rootScope, $http, $filter, $state, GenericService, commonDetailService, growl, StatusService, CONSTANTS) {
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
      var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.QUOTATION_SELECTION.DETAILS_API + approvalId;
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
       /*    response.data =
            {
              "id": 12,
              "statusId": 2,
              "status": "Pending",
              "qrNo": "WOQR-1",
              "qrDate": 1553589189665,
              "storeName": "Pharmacy Store",
              "contractType": "description",
              "qrAgainst": "Against Purchase Request",
              "addedBy": "Admin",
              "detail": [
                {
                  "id": 14,
                  "statusId": 2,
                  "status": "Pending",
                  "itemId": 662,
                  "itemCode": "102",
                  "itemName": "mh1",
                  "specification": "thejkwkjjifhdeifuhi",
                  "inclusionDetails": "efdsfdf",
                  "exclusionDetails": "wsdwsddwsd",
                  "paymentTerms": "edfede",
                  "batchId": 359,
                  "batchNo": "mh001",
                  "mapper": [
                    {
                      "id": 49,
                      "vendorName": "POW-53",
                      "vendorQRNumber": "VQR-94",
                      "isAccepted": true,
                      "quantity": 4,
                      "amount": 5,
                      "rejectNote": "",
                      "exclusionFlag": true,
                      "inclusionFlag": true,
                      "paymentFlag": false,
                      "negotiationInclusionDetails": "fsdf",
                      "negotiationExclusionDetails": "fdsf",
                      "negotiationPaymentTerms": "dfsd"


                    },
                    {
                      "id": 43,
                      "vendorName": "SDS-785",
                      "vendorQRNumber": "VQR-94",
                      "isAccepted": true,
                      "quantity": 24,
                      "amount": 45,
                      "rejectNote": "",
                      "exclusionFlag": true,
                      "inclusionFlag": false,
                      "paymentFlag": true,
                      "exclusionFlag": true,
                      "negotiationInclusionDetails": "ghgfh",
                      "negotiationExclusionDetails": "jhjgh",
                      "negotiationPaymentTerms": "lkj"

                    }
                  ]
                }
              ]
            } */
          self.model.approvalId = response.data.id;
          self.model.qrNumber = response.data.qrNo;
          self.model.itemInfo = response.data.detail;

          self.model.itemInfo.forEach(function (item) {
            var vendorSelectionDetails = item.mapper;
            vendorSelectionDetails.forEach(function (vendorDetail) {
              if (vendorDetail.isAccepted) {
                vendorDetail.setVendorStatus = 1;
              } else {
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

    var SaveTerms = function (modelName) {
      var saveTerm = self.model.itemInfo.find(function (item) {
        return (self.model.comparisonDetailsModal.currentItemId == item.id)
      });

      if (saveTerm) {
        switch (modelName) {
          case "InclusionDetailsModal":
            saveInclusionDetails(saveTerm);
            break;
          case "ExclusionDetailsModal":
            saveExclusionDetails(saveTerm);
            break;
          case "PaymentTermsModal":
            savePaymentTerms(saveTerm);
            break;

        }
      }
    }

    var saveInclusionDetails = function (saveTerm) {
      var data = self.model.comparisonDetailsModal.negotiationInclusionDetails;
      self.model.comparisonDetailsModal.negotiationInclusionDetails.forEach(function (vendorDetail) {
        var obj = saveTerm.mapper.find(function (vendor) {
          return (vendor.id == vendorDetail.id);
        });
        if (obj) {
          obj.inclusionFlag = vendorDetail.inclusionFlag;
          obj.paymentRejectNote = vendorDetail.paymentRejectNote;
          self.model.PopupAction('Close', 'InclusionDetailsModal');
        }
      })
    }

    var saveExclusionDetails = function (saveTerm) {
      var data = self.model.comparisonDetailsModal.negotiationExclusionDetails;
      self.model.comparisonDetailsModal.negotiationExclusionDetails.forEach(function (vendorDetail) {
        var obj = saveTerm.mapper.find(function (vendor) {
          return (vendor.id == vendorDetail.id);
        });
        if (obj) {
          obj.exclusionFlag = vendorDetail.exclusionFlag;
          obj.paymentRejectNote = vendorDetail.paymentRejectNote;
          self.model.PopupAction('Close', 'ExclusionDetailsModal');
        }
      })
    }

    var savePaymentTerms = function (saveTerm) {
      var data = self.model.comparisonDetailsModal.negotiationPaymentTerms;
      self.model.comparisonDetailsModal.negotiationPaymentTerms.forEach(function (vendorDetail) {
        var obj = saveTerm.mapper.find(function (vendor) {
          return (vendor.id == vendorDetail.id);
        });
        if (obj) {
          obj.paymentFlag = vendorDetail.paymentFlag;
          obj.paymentRejectNote = vendorDetail.paymentRejectNote;
          self.model.PopupAction('Close', 'PaymentTermsModal');
        }
      })
    }

    var ViewTermsModal = function (item, viewModal) {
      self.model.comparisonDetailsModal.currentItemId = item.id;

      switch (viewModal) {
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

    var SaveRecord = function (action) {
      if (action == 'Save') {
        self.model.dataStatus = setStatus('Pending');
      } else if (action == 'Send') {
        self.model.dataStatus = setStatus('Closed');
      }

      SaveQuotationSelection();
    }

    var SaveQuotationSelection = function () {
      var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.QUOTATION_SELECTION.SAVE_API + self.model.approvalId;

      var data = {
      //  "statusId": self.model.dataStatus,
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId
      };

      angular.forEach(self.model.itemInfo, function (item, key) {
        var innerData = {
          "id": item.id,
        //  "statusId": self.model.dataStatus,
          "mapper": []
        }

        for (var vendorDetail in item.mapper) {
          if (item.mapper.hasOwnProperty(vendorDetail)) {
            var details = {
              "id": item.mapper[vendorDetail].id,
              "isAccepted": item.isAccepted,
              "quantity": item.mapper[vendorDetail].quantity,
              "rejectNote": item.mapper[vendorDetail].rejectNote,
              "inclusionDetails": item.mapper[vendorDetail].inclusionFlag,
              "exclusionDetails": item.mapper[vendorDetail].exclusionFlag,
              "paymentTerms": item.mapper[vendorDetail].paymentFlag,
              "remark": item.mapper[vendorDetail].remark
            
            }
            if (item.mapper[vendorDetail].setVendorStatus == 1) {
              details["isAccepted"] = true;
              details["rejectNote"] = null;
            } else if (item.mapper[vendorDetail].setVendorStatus == 2) {
              details["isAccepted"] = false;
            }
            innerData.mapper.push(details);
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

    var ButtonVisible = function () {
      var list = [];
      self.model.itemInfo.forEach(function (data) {
        data.mapper.forEach(function (vendorDetail) {
          list.push(vendorDetail.setVendorStatus);
        });
      });

      if (list.indexOf(3) !== -1) {
        self.model.buttonFlag = true;
      } else {
        self.model.buttonFlag = false;
      }
    }

    var NavigateToBack = function () {
      $state.go('vendorSelection');
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailsQuotationSelect", {
      url: "/detailsQuotationSelect",
      templateUrl: 'views/procurement/workOrder/details-workorder-quotation-selection.html',
      controller: "DetailsQuotationSelect.Controller",
      controllerAs: "vm",
      title: 'Work Order Quotation Selection'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("DetailsQuotationSelect.Controller", detailsQuotationSelectController);
})();