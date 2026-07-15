(function () {
  "use strict";

  function detailsGRNSettlementApprovalController($scope, $rootScope, $location, rejectReasonService, $http, StatusService, $cookies, CONSTANTS, GenericService, $state, growl, commonDetailService, CurrencyService) {
    var self = this;
    var itemId;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
      $rootScope.loginpage = true;
      self.model = {
        SelectedItems: [],
        GRNUoms: [],
        bonusUoms: [],
        otherCharges: [],
        selectAllRows: false,
        poList: [],
        grnSettlementList: {
          items: []
        }
      };

      rejectReasonService.GetReason().then(function (reason) {
        self.model.rejectReasons = reason;
      });

      StatusService.GetStatus().then(function (status) {
        self.model.statuses = status;
      });

      CurrencyService.GetCurrency().then(function(currency){
        self.model.Currency = currency;
      });

      self.model.NavigateToBack = NavigateToBack;
      self.model.SelectAllItems = SelectAllItems;
      self.model.SelectItem = SelectItem;
      self.model.SaveRejection = SaveRejection;
      self.model.SaveApproval = SaveApproval;
      self.model.ViewGRNUom = ViewGRNUom;
      self.model.ViewBonusUom = ViewBonusUom;
      self.model.RejectionPopup = RejectionPopup;
      self.model.ModalDismiss = ModalDismiss;
      self.model.SaveGRNApproval = SaveGRNApproval;
      self.model.isDisable = isDisable;
      self.model.ViewStagedQuantity = ViewStagedQuantity;
      self.model.ViewOtherCharges = ViewOtherCharges;
      self.model.ViewPOList = ViewPOList;
      self.model.ViewTerms = ViewTerms;
      self.model.OpenPopup = OpenPopup;
      self.model.ClosePopup = ClosePopup;
      self.model.ValidateSaveButton = ValidateSaveButton;
      self.model.ViewItems = ViewItems;
      self.model.ViewApprovedStagedQuantity = ViewApprovedStagedQuantity;
      self.model.SumApprovedQty = SumApprovedQty;
      self.model.ViewHistory = ViewHistory;
      self.model.ModalDismissOnClose = ModalDismissOnClose;
      self.model.ViewLastGrnDetails = ViewLastGrnDetails;
      self.model.viewCreditNoteAmountDetails = viewCreditNoteAmountDetails;
      self.model.totalCreditNoteAmount = 0;
      self.model.viewCreditNoteAmountList = [];

      GetGRNSettlementList();
    }

    var ViewHistory = function(item){
      var URI = CONSTANTS.GLOBAL.PO_HISTORY + $scope.unitId + '/' + item.itemId+ '/' + item.poId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.lastOrderDetails = response.data;
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var ViewStagedQuantity = function (item) {
      self.model.grnSettlementList.items.find(function(key){
        if (item.id == key.id) {
          self.model.grnStagedQuantity = key.grnStagedQuantity;
        }
      });
    }

    var ViewPOList = function (item) {
      self.model.grnSettlementList.items.find(function(key){
        if (item.id == key.id) {
          self.model.poList = key.poList;
        }
      });
    }

    var ValidateSaveButton = function(){
      var isValid = false;
      if (self.model.grnSettlementList.items) {
        self.model.grnSettlementList.items.find(function(item){
          if(!!item.isSelected){
            isValid = true;
          }
        });

        return isValid;
      }
    }

    var GetGRNSettlementList = function () {
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN_SETTLEMENT.DETAILS_GRN_SETTLEMENT_API +  $scope.unitId + '/'+$scope.organizationId +'/'+ commonDetailService.getDataId();
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
            $rootScope.stopLoader();
            self.model.grnSettlementList = response.data;
            self.model.assetTypeId = response.data.assetTypeId;
            self.model.grnTypeId = response.data.grnTypeId;
            self.model.requestItems = JSON.parse(JSON.stringify(response.data.items));
            self.model.grnSettlementList.items.forEach(function (item) {
            	item.grnStagedQuantity.forEach(function (item1) {
            		item1.pendingQuantity=item.poPendingQuantity;
            	});
                item.isDisabled = true;
                item.forApproveStageQty = item.grnStagedQuantity;
                item.fillRejectQtyFlag = true;
                if(self.model.grnSettlementList.isSkipPhysicalInspection)
                	{
                	 if(item.grnApprovedQuantity){
                         item.totalAmount = ((item.grnApprovedQuantity+item.rejectedQuantity) * item.cop).toFixed(2);
                         item.totalDiscount = item.totalDiscount;
                         item.taxAmount = (item.totalAmount - item.totalDiscount) * (item.taxPercentage / 100);
                         item.netAmount = (item.totalAmount) - item.totalDiscount + (item.taxAmount) + (item.totalOtherAmount);
                       }
                       else{
                         item.totalAmount = (item.grnReceivedQuantity * item.cop).toFixed(2);
                       }
                	}
                else
                	{
                	 if(item.grnApprovedQuantity){
                         item.totalAmount = (item.grnApprovedQuantity * item.cop).toFixed(2);
                         item.totalDiscount = item.totalDiscount;
                         item.taxAmount = (item.totalAmount - item.totalDiscount) * (item.taxPercentage / 100);
                         item.netAmount = (item.totalAmount) - item.totalDiscount + (item.taxAmount) + (item.totalOtherAmount);
                       }
                       else{
                         item.totalAmount = (item.grnReceivedQuantity * item.cop).toFixed(2);
                       }
                	}
               

                if (!!item.rejectedQuantity) {
                item.isReject = true;
                }
            
                var uomList = null;
                if (self.model.assetTypeId == 1) {
                uomList = item.uom.find(function(uom){
                    return item.grnUomTypeId == uom.uomTypeId;
                });
                }
            CalculateStock(item, item.uom, uomList);
          });
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var CalculateStock = function (item, uom, uomList) {
      if (!!uomList) {
        var prQuantity = item.prQuantity;
        var maxOrderQuantity = item.maxOrderQuantity || 0;
        var maxStoreQuantity = item.maxStoreQuantity || 0;

        uomList.uomDetails = uomList.uomDetails.split(' ')[0];
        if (uomList.uomDetails == "P") {
          // item.newPRQuantity = parseInt(prQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
          item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
          item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
        } else if (uomList.uomDetails == "S") {
          // item.newPRQuantity = parseInt(prQuantity / (uom[1].conversion * uom[2].conversion));
          item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[1].conversion * uom[2].conversion));
          item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[1].conversion * uom[2].conversion));
        } else if (uomList.uomDetails == "D") {
          // item.newPRQuantity = parseInt(prQuantity / (uom[2].conversion));
          item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[2].conversion));
          item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[2].conversion));
        }
      } else {
        // item.newPRQuantity = item.prQuantity;
        item.newMaxOrderQuantity = item.maxOrderQuantity || 0;
        item.newMaxStoreQuantity = item.maxStoreQuantity || 0;
      }
    }

    var NavigateToBack = function () {
      $state.go('grnSettlement');
    }

    var ViewApprovedStagedQuantity = function (item) {
      var poQty = [];
      item.forApproveStageQty.forEach(function(key){
        poQty.push({
          "id": key.id,
          "poStagedDate": key.poStagedDate,
          "stagedQuantity": key.stagedQuantity,
          "receivedQuantity": key.receivedQuantity,
          "approvedQuantity": key.approvedQuantity || 0,
          "poApprovedQuantity": key.poApprovedQuantity
        });
      });
      
      self.model.approvedStageQuantity = poQty;
      self.model.selectedIndex = self.model.grnSettlementList.items.indexOf(item);
      if (!item.isSelected) {
        self.model.isSumVisible = false;
      } else {
        self.model.isSumVisible = true;
      }
    }

    var SumApprovedQty = function(x){
      var total = 0;
      var selectedItem = self.model.grnSettlementList.items[self.model.selectedIndex];
      angular.forEach(self.model.approvedStageQuantity, function (value, key) {
        if (!parseInt(value.approvedQuantity)) {
          value.approvedQuantity = 0
        }
        total = total + parseInt(value.approvedQuantity);
      });
      selectedItem.grnApprovedQuantity = total;
      if (ValidApprovedQty(selectedItem, selectedItem.grnApprovedQuantity)) {
        selectedItem.fillRejectQtyFlag = true;
        selectedItem.forApproveStageQty = self.model.approvedStageQuantity;
        if (selectedItem.grnApprovedQuantity >= selectedItem.grnReceivedQuantity) {
          selectedItem.rejectedQuantity = 0;
          selectedItem.isReject = false;
        } else{
          selectedItem.rejectedQuantity = selectedItem.grnReceivedQuantity - selectedItem.grnApprovedQuantity;
          selectedItem.isReject = true;
          RejectionPopup(selectedItem);
        }

        self.model.ClosePopup('close', 'ApprovedStagedQuantity');
      }
    }

    var ValidApprovedQty = function(item, qty){
      var isValid = true;

      if (self.model.grnTypeId == 4) {
        if (parseInt(qty) > parseInt(item.poQuantity)) {
          growl.error("Approved quantity should not greater than PO quantity");
          isValid = false;
        }
      }

      return isValid;
    }

    var SaveApproval = function (item) {
      if (item.approvedQuantity > item.grnReceivedQuantity) {
        item.rejectedQuantity = 0;
        self.model.rejectQty = 0;
        item.isReject = false;
        item.statusId = setStatus('Approved');
        RejectionPopup(item);
      } else if (item.approvedQuantity == 0 || !item.approvedQuantity) {
        item.rejectedQuantity = item.grnReceivedQuantity;
        self.model.rejectQty = item.rejectedQuantity;
        item.isReject = true;
        item.statusId = setStatus('Rejected');
        RejectionPopup(item);
      } else if (item.approvedQuantity < item.grnReceivedQuantity) {
        item.rejectedQuantity = item.grnReceivedQuantity - item.approvedQuantity;
        self.model.rejectQty = item.rejectedQuantity;
        item.isReject = true;
        item.statusId = setStatus('Approved');
        RejectionPopup(item);
      } else if (item.approvedQuantity == item.grnReceivedQuantity) {
        item.rejectedQuantity = item.grnReceivedQuantity - item.approvedQuantity;
        item.isReject = false;
        item.statusId = setStatus('Approved');
        RejectionPopup(item);
      }
    }

    var SaveRejection = function () {
      if (ValidateRejection()) {
        var rejectItemId = itemId;
        var rejectQty = parseInt(self.model.rejectQty);
        self.model.SelectedItems.forEach(function (item) {
          if (item.id == rejectItemId) {
            if (self.model.grnTypeId == 1) {
              if (rejectQty > item.grnReceivedQuantity) {
                growl.error('Rejection quantity is must be less than ' + item.grnReceivedQuantity);
                // growl.error('Please Enter Valid Quantity');
                return false;
              } else if (rejectQty == item.grnReceivedQuantity) {
                item.rejectedQuantity = item.grnReceivedQuantity;
                item.approvedQuantity = item.grnReceivedQuantity - item.rejectedQuantity;
                item.rejectReasonId = self.model.rejectReasons[0].id;
                item.statusId = setStatus('Rejected');
              } else if (rejectQty == 0 || !rejectQty) {
                item.approvedQuantity = item.grnReceivedQuantity;
                item.rejectedQuantity = 0;
                item.rejectReasonId = self.model.rejectReasons[0].id;
                item.statusId = setStatus('Approved');
              } else {
                item.rejectedQuantity = rejectQty;
                item.rejectNote = self.model.rejectNote;
                item.approvedQuantity = item.grnReceivedQuantity - item.rejectedQuantity;
                item.rejectReasonId = self.model.rejectReasons[0].id;
                item.statusId = setStatus('Approved');
              }
            } else if (self.model.grnTypeId == 4) {
              item.fillRejectQtyFlag = false;
              if (rejectQty > item.grnReceivedQuantity) {
                growl.error('Rejection quantity is must be less than ' + item.grnReceivedQuantity);
                // growl.error('Please Enter Valid Quantity');
                return false;
              } else if (rejectQty == item.grnReceivedQuantity) {
                item.rejectedQuantity = item.grnReceivedQuantity;
                item.grnApprovedQuantity = item.grnReceivedQuantity - item.rejectedQuantity;
                item.rejectReasonId = self.model.rejectReasons[0].id;
                item.statusId = setStatus('Rejected');
              } else if (rejectQty == 0 || !rejectQty) {
                item.grnApprovedQuantity = item.grnReceivedQuantity;
                item.rejectedQuantity = 0;
                item.rejectReasonId = self.model.rejectReasons[0].id;
                item.statusId = setStatus('Approved');
              } else {
                item.rejectedQuantity = rejectQty;
                item.rejectNote = self.model.rejectNote;
                item.grnApprovedQuantity = item.grnReceivedQuantity - item.rejectedQuantity;
                item.rejectReasonId = self.model.rejectReasons[0].id;
                item.statusId = setStatus('Approved');
              }
            }

            self.model.rejectQty = undefined;
            self.model.rejectNote = undefined;
            ModalDismiss('qtyaccepatReject');
          }
        });
      }
    }

    var SaveGRNApproval = function () {
      self.model.ValidateSaveButton();
      if (ValidateApproval()) {
        var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN_APPROVAL.APPROVE_API + commonDetailService.getDataId();
        
        var data = {
          "items": []
        }

        $rootScope.startLoader();
        angular.forEach(self.model.SelectedItems, function (item) {
          var approvedStageQuantity = [];
          item.forApproveStageQty.forEach(function(key){
            approvedStageQuantity.push({
              "id": key.id,
              "approvedQuantity": parseInt(key.approvedQuantity)
            });
          });

          var newItem = {
            "id": item.id,
            "bonusApprovedQuantity": parseInt(item.bonusApprovedQuantity),
            "rejectedQuantity": parseInt(item.rejectedQuantity),
            "rejectReasonId": self.model.rejectReason ? self.model.rejectReason.id : 1,
            "rejectNote": self.model.rejectNote,
            "statusId": item.statusId
          }

          if (self.model.grnTypeId == 4) {
            newItem.grnApprovedQuantity = parseInt(item.grnApprovedQuantity);
            newItem.grnStagedQuantity = approvedStageQuantity;
          } else if (self.model.grnTypeId == 1) {
            newItem.grnApprovedQuantity = parseInt(item.approvedQuantity);
          }

          data.items.push(newItem);

          var statusHash = {};
          data.statusId = null;
          data.approvalStatusId = null;
          
          data.userId= cookieObject.userId,
          data.organizationId =cookieObject.organizationId,
          data.unitId=cookieObject.unitId
          
          data.items.forEach(function(rec) {
            var statusId = rec.statusId;
            statusHash[statusId] = true;
          });

          var statuses = Object.keys(statusHash);
          if(statuses.length > 1) {
            data.statusId = setStatus('Pending');
            data.approvalStatusId = setStatus('Partially Approved');
          } else if(statuses[0] == 5) {
            data.statusId = setStatus('Pending');
            data.approvalStatusId = setStatus('Approved');
          } else if(statuses[0] == 7) {
            data.statusId = setStatus('Closed');
            data.approvalStatusId = setStatus('Rejected');
          } else {
            data.statusId = setStatus('Pending');
            data.approvalStatusId = setStatus('Partially Approved');
          }

        });

        GenericService.serviceAction("PATCH", URI, data).then(
          function (response) {
            $rootScope.stopLoader();
            growl.success(response.data.message);
            NavigateToBack();
          }, function (err) {
            $rootScope.stopLoader();
            growl.error(err.data.message);
          });
      }
    }

    var isSelected = function (item) {
      return item.isSelected;
    }

    function setStatus(key) {
      var status = self.model.statuses.find(function (obj) {
        return obj.status == key;
      });

      return status.id;
    }

    var isDisable = function (item) {
      if (item.status !== "Pending Approval") {
        return true;
      }
      return false;
    }

    var SelectAllItems = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedItems = [];
        self.model.grnSettlementList.items.forEach(function (item) {
          if (!isDisable(item)) {
            item.isSelected = true;
            item.isDisabled = false;
            item.bonusApprovedQty = item.bonusApprovedQuantity;
            item.statusId = 5;
            self.model.SelectedItems.push(item);

            if (self.model.grnTypeId == 4) {
              item.grnApprovedQuantity = item.grnReceivedQuantity;
              item.rejectedQuantity = 0;
            } else if (self.model.grnTypeId == 1) {
              item.approvedQuantity = item.grnReceivedQuantity;
              item.rejectedQuantity = 0;
            }

            item.grnStagedQuantity.forEach(function(key){
              key.approvedQuantity = key.receivedQuantity;
            });
          }
        });
      } else {
        self.model.SelectedItems = [];
        self.model.grnSettlementList.items.forEach(function (item) {
          item.isSelected = false;
          item.isDisabled = true;
          item.bonusApprovedQty = 0;
          item.statusId = 10;

          if (self.model.grnTypeId == 4) {
            item.grnApprovedQuantity = 0;
          } else if (self.model.grnTypeId == 1) {
            item.approvedQuantity = 0;
          }

          item.grnStagedQuantity.forEach(function(key){
            key.approvedQuantity = null;
          });
        });
      }
    }

    var SelectItem = function (item) {
      var items = self.model.SelectedItems;

      if (item.isSelected) {
        items.push(item);
        item.isDisabled = false;
        item.approvedQuantity = item.grnReceivedQuantity;
        item.bonusApprovedQty = item.bonusReceivedQuantity;
        item.fillRejectQtyFlag = true;

        if (self.model.grnTypeId == 4) {
          item.grnApprovedQuantity = item.grnReceivedQuantity;
          item.rejectedQuantity = 0;
        } else if (self.model.grnTypeId == 1) {
          item.approvedQuantity = item.grnReceivedQuantity;
        }

        item.grnStagedQuantity.forEach(function(key){
          key.approvedQuantity = key.receivedQuantity;
        });
        item.statusId = 5;
      } else {
        item.isDisabled = true;
        item.isReject = false;
        item.bonusApprovedQuantity = 0;
        item.statusId = 10;
        // item.fillRejectQtyFlag = false;

        if (self.model.grnTypeId == 4) {
          item.grnApprovedQuantity = 0;
          item.rejectedQuantity = 0;
        } else if (self.model.grnTypeId == 1) {
          item.approvedQuantity = 0;
        }

        item.grnStagedQuantity.forEach(function(key){
          key.approvedQuantity = null;
        });

        var index = items.indexOf(item);
        if (index > -1) {
          items.splice(index, 1);
        }
      }

      if (self.model.grnSettlementList.items.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
    }

    var RejectionPopup = function (item) {
      self.model.itemTrueFalse = item;
      if (item.isReject) {
        itemId = item.id;
        self.model.rejectQty = item.rejectedQuantity;
        self.model.action = 'Open';
        var popup = angular.element('#qtyaccepatReject');
        popup.modal('show');
        self.model.reasons = self.model.rejectReasons;
        self.model.rejectReason = self.model.reasons[0];
        // item.fillRejectQtyFlag = false;
      } else {
        CancelRejection(item);
      }
    }

    var CancelRejection = function (item) {
      var prevRequest = self.model.requestItems.find(function (prevItem) {
        return prevItem.id == item.id;
      });

      if (prevRequest) {
        item.rejectedQuantity = prevRequest.rejectedQuantity;
        item.rejectNote = prevRequest.rejectNote;
        item.rejectReasonId = prevRequest.rejectReasonId;
        item.statusId = prevRequest.statusId;
        if (self.model.grnTypeId == 4) {
          item.grnApprovedQuantity = prevRequest.grnApprovedQuantity;
        } else if (self.model.grnTypeId == 1) {
          item.approvedQuantity = prevRequest.approvedQuantity;
        }
        item.fillRejectQtyFlag = true;
      }
    }
    var ModalDismissOnClose = function(popupName){
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
      var test = self.model.itemTrueFalse;
      test.isReject= false;
    }
    var ValidateApproval = function () {
      var isValid = true;

      if (!self.model.SelectedItems || self.model.SelectedItems.length == 0) {
        growl.error('Please select items to update and save');
        isValid = false;
      }

      self.model.SelectedItems.forEach(function(data){
        if (!data.fillRejectQtyFlag) {
          growl.error('Please fill approved quantity');
          isValid = false;
        }

        if (self.model.grnTypeId == 4) {
          if (data.grnApprovedQuantity > data.poQuantity) {
            growl.error("Approved quantity should not greater than PO quantity");
            isValid = false;
          }
        }
      })

      return isValid;
    }

    var ValidateRejection = function () {
      var isValid = true;
      if (!self.model.rejectReason) {
        isValid = false;
        growl.error('Please select reason to reject');
      }

      return isValid;
    }

    var ViewOtherCharges = function (item){
      self.model.otherCharges = item.grnOtherChargesMapper;
    }

    var ViewGRNUom = function (item) {
      self.model.grnSettlementList.items.find(function(key){
        if (item.id == key.id) {
          self.model.GRNUoms = key.grnUom;
        }
      });
    }

    var ViewBonusUom = function (item) {
      self.model.grnSettlementList.items.find(function(key){
        if (item.id == key.id) {
          self.model.bonusUoms = key.bonusUom;
        }
      });
    }

    var ModalDismiss = function (popupName) {
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
      self.model.rejectNote = undefined;
    }

    var ViewTerms = function (viewModal, item) {
      switch(viewModal){
        case "warrantyPeriodModal":
          self.model.grnSettlementList.items.find(function(key){
            if (item.itemId == key.itemId) {
              self.model.warrantyPeriod = key.warrentyPeriod;
            }
          });
        break;
        case "expectedSuportTermsModal":
          self.model.grnSettlementList.items.find(function(key){
            if (item.itemId == key.itemId) {
              self.model.supportTerms = key.supportTerms;
            }
          });
        break;
        case "expectedPaymentTermsModal":
          self.model.grnSettlementList.items.find(function(key){
            if (item.itemId == key.itemId) {
              self.model.paymentTerms = key.expectedPayment;
            }
          });
        break;
        case "expectedTechSpecificationModal":
          self.model.grnSettlementList.items.find(function(key){
            if (item.itemId == key.itemId) {
              self.model.technicalSpecs = key.technicalSpecification;
            }
          });
        break;
      }
    }

    var ViewItems = function (item) {
      self.model.grnSettlementList.items.find(function(key){
        if (item.id == key.id) {
          self.model.getAccessoryList = key.grnAccessoriesMapper;
        }
      });
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

    var ViewLastGrnDetails = function (item,index) {
     // var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN.GRN_HISTORY + item.vendorId + "/" + item.items[index].itemId +"/"+item.id;
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN.PRE_GRN_HISTORY + item.vendorId + "/" + item.items[index].itemId;
      
      $rootScope.stopLoaderPopup();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.lastGRNDetailsList = response.data;
          angular.forEach(self.model.lastGRNDetailsList,function(value, key) {
        	  if(value.expirydate != undefined && value.expirydate != null){
        		   if(value.expirydate.toString().includes("-"))
                   {
        			   value.stringDate=true;
                   }  
        	  }
          });
          $rootScope.stopLoaderPopup();
        },
        function (err) {
          $rootScope.stopLoaderPopup();
        });
    }
    
    var viewCreditNoteAmountDetails = function () {
        self.model.viewCreditNoteAmountList = [];
        self.model.viewCreditNoteAmountList = self.model.grnSettlementList.creditNoteAmountList;
    }

    var rePrintBillSlip = function (grn,storeId) {
      var data = {
        "appName": "HIS",
        "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
        "reportDesignFilePath": "get_GRN.rpt",
        "reportFormat": "PDF",
        "exportFileName": "Grn",
        "reportParams": [
          {
            "paramName": "Id",
            "paramType": "Integer",
            "paramValue": grn.id
          },
          {
            "paramName": "Storeid",
            "paramType": "Integer",
            "paramValue": storeId
          }
        ]
      };
      reportService.generate(REPORT_API_URI1, data);
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailsGRNSettlementApproval", {
      url: "/detailsGRNSettlementApproval",
      templateUrl: 'views/procurement/quotation/details-GRN-Settlement-Approval.html',
      controller: "detailsGRNSettlementApproval.Controller",
      controllerAs: "vm",
      title: 'Good Receipt Note Settlement Approval'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("detailsGRNSettlementApproval.Controller", detailsGRNSettlementApprovalController);
})();