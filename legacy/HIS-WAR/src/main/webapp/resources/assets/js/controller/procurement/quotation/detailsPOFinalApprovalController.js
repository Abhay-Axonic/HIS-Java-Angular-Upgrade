(function () {
  "use strict";

  function detailsPOFinalApprovalController($scope, $rootScope, $location, rejectReasonService, $http, StatusService, $cookies, CONSTANTS, GenericService, $state, growl, commonDetailService, CurrencyService) {
    var self = this;
    var itemId;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
      $rootScope.loginpage = true;
      self.model = {
        SelectedItems: [],
        POUoms: [],
        bonusUoms: [],
        isConsignment: false,
        selectAllRows: false,
        selectedIndex: null,
        otherChargesDropdownList: [],
        otherChargesList: [],
        selectedOtherChargeIndex: null,
        defaultOtherCharge: [{
          "otherCharges": "Select Other Charge"
        }],
        purchaseOrders: {
          items: []
        }
      };

      rejectReasonService.GetReason().then(function (reason) {
        self.model.rejectReasons = reason;
      });

      CurrencyService.GetCurrency().then(function(currency){
        self.model.Currency = currency;
      });

      self.model.NavigateToBack = NavigateToBack;
      self.model.SelectAllItems = SelectAllItems;
      self.model.SelectItem = SelectItem;
      self.model.SaveRejection = SaveRejection;
      self.model.SaveApproval = SaveApproval;
      self.model.ViewOtherCharges = ViewOtherCharges;
      self.model.ViewPOUom = ViewPOUom;
      self.model.ViewBonusUom = ViewBonusUom;
      self.model.ViewStagedQuantity = ViewStagedQuantity;
      self.model.ViewApprovedStagedQuantity = ViewApprovedStagedQuantity;
      self.model.sumApprovedQty = sumApprovedQty;
      self.model.RejectionPopup = RejectionPopup;
      self.model.ModalDismiss = ModalDismiss;
      self.model.SavePOApproval = SavePOApproval;
      self.model.isDisable = isDisable;
      self.model.OpenPopup = OpenPopup;
      self.model.ClosePopup = ClosePopup;
      self.model.ViewItems = ViewItems;
      self.model.ViewPRList = ViewPRList;
      self.model.ViewTerms = ViewTerms;
      self.model.ValidateSaveButton = ValidateSaveButton;
      self.model.ViewHistory = ViewHistory;
      self.model.SetDecimalOtherCharge = SetDecimalOtherCharge;
      self.model.AddOtherCharge = AddOtherCharge;
      self.model.lessOtherCharge = lessOtherCharge;
      self.model.sumOtherCharges = sumOtherCharges;
      self.model.fillOtherChargesList = fillOtherChargesList;
      self.model.PurchaseOrderApproval=PurchaseOrderApproval;
      self.model.pharmacyGet=pharmacyGet;
      self.model.CalculateStock = CalculateStock;
      self.model.selectAllRows = true;
      
      GetPurchaseOrders();
      pharmacyGet();
      
      self.model.DisableEditForOrder=DisableEditForOrder;
    }

    var SetDecimalOtherCharge = function (item) {
      if (isNaN(item.amount) || !item.amount || item.amount == 0) {
        item.amount = 0;
      } else {
        item.amount = parseFloat(item.amount).toFixed(2);
      }
    }

    var AddOtherCharge = function (ocId, index) {
      otherCharge: self.model.otherChargesDropdownList[0]
      self.model.otherChargesList.push({ otherChargesId: ocId, amount: 0 });
      self.model.otherChargesList[index + 1].otherCharge = self.model.otherChargesDropdownList[0];
      if(self.model.otherChargesList.length > 1){
        $scope.showMinus = true;
      }
      else{
        $scope.showMinus = false;
      }
    }

    var lessOtherCharge = function (item, idx) {
      var index = self.model.otherChargesList.indexOf(item);
      self.model.otherChargesList.splice(index, 1);
      if(self.model.otherChargesList.length > 1){
        $scope.showMinus = true;
      }
      else{
        $scope.showMinus = false;
      }
    }

    var validateCharges = function () {
      var isValidItem = true;
      if (!self.model.otherChargesList.length) {
        growl.error('Please add other charges and amount');
        isValidItem = false;
      } else {
        angular.forEach(self.model.otherChargesList, function (charges) {

          charges['isChargesValid'] = false;
          charges['isAmountValid'] = false;

          if (charges.otherCharge.id) {
            if (!parseFloat(charges.amount)) {
              charges['isAmountValid'] = true;
              growl.error('Please enter amount');
              isValidItem = false;
            }
          }
          if (parseFloat(charges.amount)) {
            if (!charges.otherCharge.id) {
              charges['isChargesValid'] = true;
              growl.error('Please select other charges');
              isValidItem = false;
            }
          }
        });
      }
      return isValidItem;
    }

    function DuplicateData(chargeList) {
      var isValid = true;
      var object = {};
      var result = [];

      chargeList.forEach(function (item) {
        if (!!item.otherCharge.id) {
          if (!object[item.otherCharge.id])
            object[item.otherCharge.id] = 0;
          object[item.otherCharge.id] += 1;
        }
      })

      for (var prop in object) {
        if (object[prop] >= 2) {
          result.push(prop);
        }
      }

      if (!!result.length) {
        isValid = false;
        growl.error("Duplicate other charges");
      }

      return isValid;
    }
    $scope.cancelOtherCharges = function ()
    {
    	var total = 0;
    	var otherList=[];
        self.model.otherChargesList.forEach(function (value) {
          value.otherCharge = self.model.otherChargesDropdownList.find(function (key) {
            if (value.otherCharge.id == key.id) {
              value.otherChargesId = key.id;
              return key;
            }
          });
        });

        angular.forEach(self.model.otherChargesList, function (value, key) {
          // delete value['otherCharge'];
          total = total + parseFloat(value.amount);
          if(value.amount!=undefined && value.amount!=null && value.amount>0)
          {
          otherList.push(value)
          }
        });
        self.model.otherChargesList=otherList;

        angular.forEach(self.model.purchaseOrders.otherCharges, function(datacharges){
          datacharges.amount = total;
        });

        self.model.purchaseOrders.totalOtherAmount = total;
        self.model.otherChargesAmount = total;
        self.model.ClosePopup('close', 'otherCharges_Modal');
    }
    var sumOtherCharges = function (chargeList) {
      if (validateCharges() && DuplicateData(chargeList)) {
        var total = 0;
        self.model.otherChargesList.forEach(function (value) {
          value.otherCharge = self.model.otherChargesDropdownList.find(function (key) {
            if (value.otherCharge.id == key.id) {
              value.otherChargesId = key.id;
              return key;
            }
          });
        });

        angular.forEach(self.model.otherChargesList, function (value, key) {
          // delete value['otherCharge'];
          total = total + parseFloat(value.amount);
        });


        angular.forEach(self.model.purchaseOrders.otherCharges, function(datacharges){
          datacharges.amount = total;
        });

        self.model.purchaseOrders.totalOtherAmount = total;
        self.model.otherChargesAmount = total;
        self.model.ClosePopup('close', 'otherCharges_Modal');
      }
    }

    var fillOtherChargesList = function (item) {
       angular.forEach(item.items, function(data){
        if(item.otherCharges){
          $scope.selectedOtherCharges = [];
          angular.forEach(item.otherCharges, function(data){
            var obj = {
              otherChargesId: '',
              amount: ''
            }
            obj.otherChargesId = data.otherChargesId;
            obj.amount = data.amount;
            $scope.selectedOtherCharges.push(obj);
            angular.forEach($scope.selectedOtherCharges, function (value) {
              value.otherCharge = self.model.otherChargesDropdownList.find(function (key) {
                return key.id == value.otherChargesId;
              });
            });
            $scope.selectedOtherCharges.forEach(function (othercharge) {
              var amountNumber = parseFloat(othercharge.amount);
              if (amountNumber == 0) {
                amountNumber = 0;
              } else {
                othercharge.amount = amountNumber.toFixed(2);
              }
            });
            $scope.dataother = $scope.selectedOtherCharges;
          });
        }
        else{
          data['selectedOtherCharges'] = [{ otherChargesId: self.model.otherChargesDropdownList[0].id, amount: 0 }];
          angular.forEach(data.selectedOtherCharges, function (value) {
            value.otherCharge = self.model.otherChargesDropdownList.find(function (key) {
              return key.id == value.otherChargesId;
            });
          });
          data.selectedOtherCharges.forEach(function (othercharge) {
            var amountNumber = parseFloat(othercharge.amount);
            if (amountNumber == 0) {
              amountNumber = 0;
            } else {
              othercharge.amount = amountNumber.toFixed(2);
            }
          });
        }
        if(!item.otherCharges){
          self.model.otherChargesList = data.selectedOtherCharges;
        }
        else{
          self.model.otherChargesList = $scope.dataother;
        }
        
        self.model.selectedOtherChargeIndex = self.model.purchaseOrders.items.indexOf(data);
        if(self.model.otherChargesList.length > 1){
          $scope.showMinus = true;
        }
        else{
          $scope.showMinus = false;
        }
      });
    }

    var ViewPRList = function (item) {
      self.model.purchaseOrders.items.find(function(value){
        if (item.id == value.id) {
          self.model.prList = value.prId;
        }
      });
    }

    var ValidateSaveButton = function(){
      var isValid = false;
      if (self.model.purchaseOrders.items) {
        self.model.purchaseOrders.items.find(function(item){
          if(!!item.isSelected){
            isValid = true;
          }
        });

        return isValid;
      }
    }

    var GetPurchaseOrders = function () {
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER_APPROVAL.DETAILS_API  + $scope.unitId + '/'+$scope.organizationId + '/' + commonDetailService.getDataId();
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.purchaseOrders = response.data;
          self.model.vendorId = response.data.vendorId;
          self.model.assetTypeId = response.data.assetTypeId;
          self.model.poTypeId = response.data.poTypeId;
          self.model.paymentTerm = response.data.paymentTerm;
          self.model.remark = response.data.remark;
          self.model.requestItems = JSON.parse(JSON.stringify(response.data.items));
          self.model.otherChargesDropdownList = self.model.defaultOtherCharge.concat(response.data.otherChargesList);
          self.model.SelectAllItems();
          self.model.purchaseOrders.items.forEach(function (item) {
            var uomList = null;
            var uomListBonus = null;
            item.newPOQty = item.poQuantity;
            item.isDisabled = true;
            item.forApproveStageQty = item.purchaseOrderStagedDto;
            // item.prQuantity = 0;
            item.fillRejectQtyFlag = true;
            if (!!item.rejectQuantity) {
              item.isReject = true;
            }
            if (self.model.assetTypeId == 1) {
              uomList = item.uom.find(function(uom){
                return item.poUomTypeId == uom.uomTypeId;
              });
              uomListBonus = item.uom.find(function(uom){
                return item.bonusUomTypeId == uom.uomTypeId;
              });
              if(uomListBonus != undefined){
                item.bonusUomType = uomListBonus.uomDetails;
              }
              item.poUomType = uomList.uomDetails;
              if(item.isThreeUOMApplicable == false){
                item.uomListdata = item.uom;
                item.uom = [];
                angular.forEach(item.uomListdata, function(data){
                  if(data.uomType != 'Stock'){
                    item.uom.push(data);
                  }
                });
              }
              item.selectedUom = item.uom.find(function(uom){
                return item.poUomTypeId == uom.uomTypeId;
              });
            }
            item['selectedOtherCharges'] = [{ otherChargesId: self.model.otherChargesDropdownList[0].id, amount: 0 }];
            
            CalculateStock(item, item.uom, uomList);
            if(item.approvedQuantity){
              $scope.calculate(item);
            }
          });
          $rootScope.stopLoader();
          if(self.model.purchaseOrders.otherCharges != null){
          self.model.purchaseOrders.totalOtherAmount=0;
          angular.forEach(self.model.purchaseOrders.otherCharges, function(d){
            self.model.purchaseOrders.totalOtherAmount += d.amount;
          });
          }

          
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
        var poQty = item.newPOQty || 0;

        uomList.uomDetailsData = uomList.uomDetails.split(' ')[0];
        if(item.isThreeUOMApplicable == false){
          if (uomList.uomDetailsData == "P") {
            // item.newPRQuantity = parseInt(prQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
           // item.poQuantity = Math.ceil(poQty / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
           item.receivedQtyCon=(item.approvedQuantity +"*"+ item.poUom[0].conversion);
           item.receivedQtyCalculation = (item.approvedQuantity * item.poUom[0].conversion);
           item.uomUnitData = item.uomListdata[2].uomUnit;
            if(item.bonusApprovedQuantity != undefined && item.bonusApprovedQuantity != null){
              item.receivedQtyBonusCon=(item.bonusApprovedQuantity +"*"+ item.bonusUom[0].conversion);
              item.receivedQtyBonusCalculation = (item.bonusApprovedQuantity * item.bonusUom[0].conversion);
              item.uomUnitDataBonus = item.uomListdata[2].uomUnit;
            }
            else{
              item.receivedQtyBonusCon=(item.bonusQuantity +"*"+ item.bonusUom[0].conversion);
              item.receivedQtyBonusCalculation = (item.bonusQuantity * item.bonusUom[0].conversion);
              item.uomUnitDataBonus = item.uomListdata[2].uomUnit;
            }
          } else if (uomList.uomDetailsData == "S") {
            // item.newPRQuantity = parseInt(prQuantity / (uom[1].conversion * uom[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
           // item.poQuantity = Math.ceil(poQty / (uom[1].conversion * uom[2].conversion));
          } else if (uomList.uomDetailsData == "D") {
            // item.newPRQuantity = parseInt(prQuantity / (uom[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[2].conversion));
           // item.poQuantity = Math.ceil(poQty / (uom[2].conversion));
           item.receivedQtyCon=(item.approvedQuantity);
           item.receivedQtyCalculation = (item.approvedQuantity);
           item.uomUnitData = item.uomListdata[2].uomUnit;
            if(item.bonusApprovedQuantity != undefined && item.bonusApprovedQuantity != null){
              item.receivedQtyBonusCon=(item.bonusApprovedQuantity);
              item.receivedQtyBonusCalculation = (item.bonusApprovedQuantity);
              item.uomUnitDataBonus = item.uomListdata[2].uomUnit;
            }
            else{
              item.receivedQtyBonusCon=(item.bonusQuantity);
              item.receivedQtyBonusCalculation = (item.bonusQuantity);
              item.uomUnitDataBonus = item.uomListdata[2].uomUnit;
            }
          }
        }
        else{
          if (uomList.uomDetailsData == "P") {
            // item.newPRQuantity = parseInt(prQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
           // item.poQuantity = Math.ceil(poQty / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
           item.receivedQtyCon = (item.approvedQuantity +"*"+ item.poUom[0].conversion+"*"+ item.poUom[1].conversion);
           item.receivedQtyCalculation = (item.approvedQuantity * item.poUom[0].conversion * item.poUom[1].conversion);
           item.uomUnitData = uom[2].uomUnit;
            if(item.bonusApprovedQuantity != undefined && item.bonusApprovedQuantity != null){
              item.receivedQtyBonusCon = (item.bonusApprovedQuantity +"*"+ item.bonusUom[0].conversion+"*"+ item.bonusUom[1].conversion);
              item.receivedQtyBonusCalculation = (item.bonusApprovedQuantity * item.bonusUom[0].conversion * item.bonusUom[1].conversion);
              item.uomUnitDataBonus = uom[2].uomUnit;
            }
            else{
              item.receivedQtyBonusCon = (item.bonusQuantity +"*"+ item.bonusUom[0].conversion+"*"+ item.bonusUom[1].conversion);
              item.receivedQtyBonusCalculation = (item.bonusQuantity * item.bonusUom[0].conversion * item.bonusUom[1].conversion);
              item.uomUnitDataBonus = uom[2].uomUnit;
            }
          } else if (uomList.uomDetailsData == "S") {
            // item.newPRQuantity = parseInt(prQuantity / (uom[1].conversion * uom[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[1].conversion * uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[1].conversion * uom[2].conversion));
           // item.poQuantity = Math.ceil(poQty / (uom[1].conversion * uom[2].conversion));
           item.receivedQtyCon=(item.approvedQuantity +"*"+ item.poUom[1].conversion);
           item.receivedQtyCalculation = (item.approvedQuantity * item.poUom[1].conversion);
           item.uomUnitData = uom[2].uomUnit;
            if(item.bonusApprovedQuantity != undefined && item.bonusApprovedQuantity != null){
              item.receivedQtyBonusCon = (item.bonusApprovedQuantity +"*"+ item.bonusUom[1].conversion);
              item.receivedQtyBonusCalculation = (item.bonusApprovedQuantity * item.bonusUom[1].conversion);
              item.uomUnitDataBonus = uom[2].uomUnit;
            }
            else{
              item.receivedQtyBonusCon = (item.bonusQuantity +"*"+ item.bonusUom[1].conversion);
              item.receivedQtyBonusCalculation = (item.bonusQuantity * item.bonusUom[1].conversion);
              item.uomUnitDataBonus = uom[2].uomUnit;
            }
          } else if (uomList.uomDetailsData == "D") {
            // item.newPRQuantity = parseInt(prQuantity / (uom[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[2].conversion));
           // item.poQuantity = Math.ceil(poQty / (uom[2].conversion));
           item.receivedQtyCon=(item.approvedQuantity);
           item.receivedQtyCalculation = (item.approvedQuantity);
           item.uomUnitData = uom[2].uomUnit;
            if(item.bonusApprovedQuantity != undefined && item.bonusApprovedQuantity != null){
              item.receivedQtyBonusCon=(item.bonusApprovedQuantity);
              item.receivedQtyBonusCalculation = (item.bonusApprovedQuantity);
              item.uomUnitDataBonus = uom[2].uomUnit;
            }
            else{
              item.receivedQtyBonusCon=(item.bonusQuantity);
              item.receivedQtyBonusCalculation = (item.bonusQuantity);
              item.uomUnitDataBonus = uom[2].uomUnit;
            }
          }
        }
      } else {
        // item.newPRQuantity = item.prQuantity;
        item.newMaxOrderQuantity = item.maxOrderQuantity || 0;
        item.newMaxStoreQuantity = item.maxStoreQuantity || 0;
      }
    }

    var ViewHistory = function(item){
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER.COP_HISTORY + item.itemId + "/" + self.model.vendorId + "/last/"+item.assetTypeId;

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

    var ModalDismiss = function (popupName) {
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
      self.model.rejectNote = undefined;
      self.model.onclose.isReject = false;
      self.model.rejectQty = 0;
      self.model.rejectReason = undefined;
      //call cancelRejection for resolved HIS-9501 PO approval for asset - Reduction in the approved quantity does not show the rejection pop up.
      CancelRejection(self.model.onclose);
    }

    var RejectionPopup = function (item) {
      self.model.onclose = item;
      if (item.isReject) {
        itemId = item.id;
        self.model.rejectQty = item.rejectQuantity;
        self.model.action = 'Open';
        var popup = angular.element('#qtyaccepatReject');
        popup.modal('show');
        self.model.reasons = self.model.rejectReasons;
        self.model.rejectReason = self.model.reasons[0];
      } else {
        CancelRejection(item);
      }
    }

    var CancelRejection = function (item) {
      var prevRequest = self.model.requestItems.find(function (prevItem) {
        return prevItem.id == item.id;
      });

      if (prevRequest) {
        item.rejectQuantity = prevRequest.rejectQuantity;
        item.approvedQuantity = prevRequest.poQuantity;
        item.rejectNote = prevRequest.rejectNote;
        item.rejectReasonId = prevRequest.rejectReasonId;
        if (item.approvedQuantity == item.poQuantity) {
          item.statusId = setStatus('Approved');
        }
        else{
          item.statusId = prevRequest.statusId;
        }
        item.fillRejectQtyFlag = true;
        //added for when call cancelRejection for resolved HIS-9501 PO approval for asset - Reduction in the approved quantity does not show the rejection pop up.
        item.forApproveStageQty = item.purchaseOrderStagedDto;
        $scope.calculate1(item);
        $scope.amountround1 = 0;
      }
    }

    var SaveApproval = function (item) {
      var approvedQuantity = parseInt(item.approvedQuantity);
      if (approvedQuantity > item.poQuantity) {
        item.rejectQuantity = 0;
        self.model.rejectQty = 0;
        item.isReject = false;
        item.statusId = setStatus('Approved');
      } else if (approvedQuantity == 0 || !approvedQuantity) {
        item.rejectQuantity = item.poQuantity;
        self.model.rejectQty = item.rejectQuantity;
        item.isReject = true;
        item.statusId = setStatus('Rejected');
      } else if (approvedQuantity < item.poQuantity) {
        item.rejectQuantity = item.poQuantity - approvedQuantity;
        self.model.rejectQty = item.rejectQuantity;
        item.isReject = true;
        item.statusId = setStatus('Approved');
      } else if (approvedQuantity == item.poQuantity) {
        item.rejectQuantity = item.poQuantity - approvedQuantity;
        item.isReject = false;
        item.statusId = setStatus('Approved');
      }
    }

    var SaveRejection = function () {
      if (ValidateRejection()) {
        var rejectItemId = itemId;
        self.model.SelectedItems.forEach(function (item) {
          item.fillRejectQtyFlag = true;
          if (item.id == rejectItemId) {
            if (self.model.rejectQty > item.poQuantity) {
              growl.error('Rejection quantity is must be less than ' + item.poQuantity);
              // growl.error('Please Enter Valid Quantity');
              return false;
            } else if (self.model.rejectQty == item.poQuantity) {
              item.rejectQuantity = item.poQuantity;
              item.approvedQuantity = item.poQuantity - item.rejectQuantity;
              item.rejectReasonId = self.model.rejectReasons[0].id;
              item.statusId = setStatus('Rejected'); // Rejected
              item.isReject = true;
            //  RejectionPopup(item);
            } else if (self.model.rejectQty == 0 || !self.model.rejectQty) {
              item.approvedQuantity = item.poQuantity;
              item.rejectQuantity = 0;
              item.rejectReasonId = self.model.rejectReasons[0].id;
              item.statusId = setStatus('Approved'); // Approved
              item.isReject = false;
           //   RejectionPopup(item);
            } else {
              item.rejectQuantity = self.model.rejectQty;
              item.rejectNote = self.model.rejectNote;
              item.approvedQuantity = item.poQuantity - item.rejectQuantity;
              item.rejectReasonId = self.model.rejectReasons[0].id;
              item.statusId = setStatus('Approved'); // old Partially Approved
              item.isReject = true;
           //   RejectionPopup(item);
            }
            self.model.rejectQty = undefined;
            self.model.rejectNote = undefined;
          //  ModalDismiss('qtyaccepatReject');
          var popup = angular.element('#qtyaccepatReject');
          popup.modal('hide');
          $scope.calculate(item);
          }
        });
      }
    }

    $scope.amountround1 = 0;
    $scope.calculate = function (item) {
      item.poAmount = item.approvedQuantity * item.cop;
      item.totalDiscount = (item.approvedQuantity * item.cop) * (item.discount/100);
      item.taxAmount = ((item.approvedQuantity * item.cop)-item.totalDiscount) * (item.taxPercentage/100);
      $scope.amountround1 = $scope.amountround1 + ((item.poAmount - item.totalDiscount) + item.taxAmount + item.totalOtherAmount);
      $scope.amountround = $scope.amountround1.toFixed();
      $scope.amount1 = $scope.amountround1.toFixed(2);
      $scope.roundOff1 = parseInt($scope.amountround) - $scope.amount1;
      if($scope.roundOff1 < 0.50){
          self.model.purchaseOrders.netAmount = $scope.amountround;
          self.model.purchaseOrders.roundOffAmount = parseInt($scope.amountround) - $scope.amount1;
        }
        if($scope.roundOff1 >= 0.50){
          self.model.purchaseOrders.netAmount = math.round($scope.amount1);
          self.model.purchaseOrders.roundOffAmount = self.model.purchaseOrders.netAmount - $scope.amount1;
        }
     /* if($scope.roundOff1 < 0.50){ --Removed because this issue HIS-23937
        self.model.purchaseOrders.netAmount = $scope.amountround;
        self.model.purchaseOrders.roundOffAmount = parseInt($scope.amountround) - $scope.amount1;
      }
      if($scope.roundOff1 >= 0.50){
        self.model.purchaseOrders.netAmount = math.round($scope.amount1);
        self.model.purchaseOrders.roundOffAmount = self.model.purchaseOrders.netAmount - $scope.amount1;
      }*/
    }

    $scope.calculate1 = function (item) {
      $scope.amountround1 = 0;
      item.poAmount = item.approvedQuantity * item.cop;
      item.totalDiscount = (item.approvedQuantity * item.cop) * (item.discount/100);
      item.taxAmount = ((item.approvedQuantity * item.cop)-item.totalDiscount) * (item.taxPercentage/100);
      $scope.amountround1 = $scope.amountround1 + ((item.poAmount - item.totalDiscount) + item.taxAmount + item.totalOtherAmount);
      $scope.amountround = $scope.amountround1.toFixed();
      $scope.amount1 = $scope.amountround1.toFixed(2);
      $scope.roundOff1 = parseInt($scope.amountround) - $scope.amount1;
      if($scope.roundOff1 < 0.50){
        self.model.purchaseOrders.netAmount = $scope.amountround;
        self.model.purchaseOrders.roundOffAmount = parseInt($scope.amountround) - $scope.amount1;
      }
      if($scope.roundOff1 >= 0.50){
        self.model.purchaseOrders.netAmount = math.round($scope.amount1);
        self.model.purchaseOrders.roundOffAmount = self.model.purchaseOrders.netAmount - $scope.amount1;
      }
    }
    var PurchaseOrderApproval = function (id) {
        
        var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER_APPROVAL.APPROVAL_API + '/' +id;
        var data={
        		"userId":$scope.userId
        };

        $rootScope.startLoader();
        GenericService.serviceAction("PATCH", URI, data).then(
          function (response) {
        	  growl.success(response.data.message);
        	  $state.go('POFinalApproval');
            $rootScope.stopLoader();
            
          },
          function (err) {
        	  growl.error(err.data.message);
            $rootScope.stopLoader();
          });
      }
    
    var pharmacyGet = function () {
		var confignew = self.model;
		var URI = CONSTANTS.MASTER.UNIT.APPLICATION_CONFIGURATION.APPLICATION_CONFIGURATION_GET_API + $scope.unitId;
		$rootScope.startLoader();

		GenericService.serviceAction("GET", URI, {}).then(
			function (response) {
				$rootScope.isPoApproval = response.data[0].isPoApproval;
				$rootScope.stopLoader();
			},
			function (err) {
				$rootScope.stopLoader();
			});
	}
    var SavePOApproval = function () {
      if (ValidateApproval()) {
        var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER_APPROVAL.SAVE_API + commonDetailService.getDataId();

        var data = {
          "items": [],
          "paymentTerm": self.model.paymentTerm,
          "remark": self.model.remark,
          "otherCharges": [],
          "organizationId": $scope.organizationId,
          "unitId": $scope.unitId,
          "userId": $scope.userId
        }

        $rootScope.startLoader();
        if (self.model.SelectedItems.length < self.model.purchaseOrders.items.length) {
          var flag = self.model.purchaseOrders.items.find(function(x){
            if (x.statusId == 10) {
              return true;
            } else {
              return false;
            }
          });

          if (!!flag) {
            data.otherCharges = self.model.purchaseOrders.otherCharges;
            angular.forEach(self.model.SelectedItems, function (item) {
              var approvedStageQuantity = [];
              item.forApproveStageQty.forEach(function(x){
                approvedStageQuantity.push({
                  "id": x.id,
                  "approvedQuantity": parseInt(x.approvedQuantity)
                });
              })
              var newItem = {
                "id": item.id,
                "poApprovedQuantity": parseInt(item.approvedQuantity),
                "bonusApprovedQuantity": item.bonusApprovedQuantity,
                "rejectedQuantity": parseInt(item.rejectQuantity),
                "rejectReasonId": self.model.rejectReason ? self.model.rejectReason.id : 1,
                "rejectNote": self.model.rejectNote,
                "statusId": item.statusId,
                "remark": item.remark,
                "purchaseOrderStaged": approvedStageQuantity
              }

              data.items.push(newItem);

              var statusHash = {};
              data.statusId = null;
              data.approvalStatusId = null;
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
                data.approvalStatusId = setStatus('Partially Approved');
              } else if(statuses[0] == 7) {
                data.statusId = setStatus('Pending');
                data.approvalStatusId = setStatus('Partially Approved');
              } else {
                data.statusId = setStatus('Pending');
                data.approvalStatusId = setStatus('Partially Approved');
              }
            });
          } else {
            angular.forEach(self.model.SelectedItems, function (item) {
              var approvedStageQuantity = [];
              item.forApproveStageQty.forEach(function(x){
                approvedStageQuantity.push({
                  "id": x.id,
                  "approvedQuantity": parseInt(x.approvedQuantity)
                });
              })
              var newItem = {
                "id": item.id,
                "poApprovedQuantity": parseInt(item.approvedQuantity),
                "bonusApprovedQuantity": item.bonusApprovedQuantity,
                "rejectedQuantity": parseInt(item.rejectQuantity),
                "rejectReasonId": self.model.rejectReason ? self.model.rejectReason.id : 1,
                "rejectNote": self.model.rejectNote,
                "statusId": item.statusId,
                "remark": item.remark,
                "purchaseOrderStaged": approvedStageQuantity
              }
              data.otherCharges = self.model.purchaseOrders.otherCharges;
              data.items.push(newItem);

              var statusHash = {};
              data.statusId = null;
              data.approvalStatusId = null;
              data.items.forEach(function(rec) {
                var statusId = rec.statusId;
                statusHash[statusId] = true;
              });

              var statuses = Object.keys(statusHash);
              if(statuses.length > 1) {
                data.statusId = setStatus('Pending');
                // changed
                data.approvalStatusId = setStatus('Approved');
              } else if(statuses[0] == 5) {
                data.statusId = setStatus('Pending');
                data.approvalStatusId = setStatus('Approved');
              } else if(statuses[0] == 7) {
                // changed
                data.statusId = setStatus('Pending');
                data.approvalStatusId = setStatus('Approved');
              } else {
                data.statusId = setStatus('Pending');
                data.approvalStatusId = setStatus('Partially Approved');
              }
            });
          }
        } else if (self.model.SelectedItems.length == self.model.purchaseOrders.items.length) {
          data.otherCharges = self.model.purchaseOrders.otherCharges;
          angular.forEach(self.model.SelectedItems, function (item) {
            var approvedStageQuantity = [];
            item.forApproveStageQty.forEach(function(x){
              approvedStageQuantity.push({
                "id": x.id,
                "approvedQuantity": parseInt(x.approvedQuantity)
              });
            })
            var newItem = {
              "id": item.id,
              "poApprovedQuantity": parseInt(item.approvedQuantity),
              "bonusApprovedQuantity": item.bonusApprovedQuantity,
              "rejectedQuantity": parseInt(item.rejectQuantity),
              "rejectReasonId": self.model.rejectReason ? self.model.rejectReason.id : 1,
              "rejectNote": self.model.rejectNote,
              "statusId": item.statusId,
              "remark": item.remark,
              "purchaseOrderStaged": approvedStageQuantity
            }

            data.items.push(newItem);

            var statusHash = {};
            data.statusId = null;
            data.approvalStatusId = null;
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
        }

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

    StatusService.GetStatus().then(function (status) {
      self.model.statuses = status;
    });

    function setStatus(x) {
      var status = self.model.statuses.find(function (obj) {
        return obj.status == x;
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
        self.model.purchaseOrders.items.forEach(function (item) {
          if (!isDisable(item)) {
          if(self.model.purchaseOrders.reviewedBy >0){
          item.isSelected = false;
          }
          if(self.model.purchaseOrders.reviewedBy <= 0){
          item.isSelected = true;
          }

            item.isDisabled = false;
            item.approvedQuantity = item.poQuantity;
            item.bonusApprovedQuantity = item.bonusQuantity;
            item.statusId = 5;
            item.purchaseOrderStagedDto.forEach(function(x){
              x.approvedQuantity = x.quantity;
            });
            self.model.SelectedItems.push(item);
          }
if(self.model.purchaseOrders.reviewedBy >0){
          item.isSelected = false;
          }
          if(self.model.purchaseOrders.reviewedBy <= 0){
          item.isSelected = true;
          }
        });
      } else {
        self.model.SelectedItems = [];
        self.model.purchaseOrders.items.forEach(function (item) {
          if (!isDisable(item)) { // todo
            item.isSelected = false;
            item.isDisabled = true;
            item.approvedQuantity = 0;
            item.bonusApprovedQuantity = 0;
            item.statusId = 10;
            item.purchaseOrderStagedDto.forEach(function(x){
              x.approvedQuantity = null;
            });
          }
          if(self.model.purchaseOrders.reviewedBy >0){
                    item.isSelected = false;
                    }
                    if(self.model.purchaseOrders.reviewedBy <= 0){
                    item.isSelected = true;
                    }
        });
      }
    }

    var SelectItem = function (item) {
      var items = self.model.SelectedItems;

      if (item.isSelected) {
        items.push(item);
        item.isDisabled = false;
        item.approvedQuantity = item.poQuantity;
        item.fillRejectQtyFlag = true;
        item.rejectQuantity = 0;
        item.bonusApprovedQuantity = item.bonusQuantity;
        item.purchaseOrderStagedDto.forEach(function(value){
          value.approvedQuantity = value.quantity;
        });
        item.statusId = 5;
      } else {
        item.isDisabled = true;
        item.isReject = false;
        item.approvedQuantity = 0;
        item.bonusApprovedQuantity = 0;
        // item.fillRejectQtyFlag = false;
        item.statusId = 10;
        item.purchaseOrderStagedDto.forEach(function(value){
          value.approvedQuantity = null;
        });
        var index = items.indexOf(item);
        if (index > -1) {
          items.splice(index, 1);
        }
      }

      if (self.model.purchaseOrders.items.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
    }

    var ValidateRejection = function () {
      var isValid = true;
      if (!self.model.rejectReason) {
        isValid = false;
        growl.error('Please select reason to reject');
      }

      return isValid;
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

        if (self.model.poTypeId == 3) {
          if (data.approvedQuantity > data.prQuantity) {
            growl.error("Approved quantity should not greater than PR quantity");
            isValid = false;
          }
        }
      })

      return isValid;
    }

    var ViewOtherCharges = function (item){
      self.model.purchaseOrders.items.find(function(x){
        if (item.id == x.id) {
          self.model.otherCharges = x.otherCharges;
        }
      });
    }

//    var ViewStagedQuantity = function (item) {
//      self.model.purchaseOrders.items.find(function(data){
//        if (item.id == data.id) {
//          self.model.stagedQuantity = data.forApproveStageQty;
//        }
//      });
//    }
    
    var ViewStagedQuantity = function (item) {
        self.model.purchaseOrders.items.find(function(data){
          if (item.id == data.id) {
            self.model.stagedQuantity = data.initialPurchaseOrderStagedDto;
          }
        });
      }

    var ViewApprovedStagedQuantity = function (item) {
      var poQty = [];
      item.forApproveStageQty.forEach(function(x){
        poQty.push({
          "id": x.id,
          "stagedDate": x.stagedDate,
          "quantity": x.quantity,
          "approvedQuantity": x.approvedQuantity !=null && x.approvedQuantity!=undefined ?  x.approvedQuantity :  (x.quantity !=undefined && x.quantity !=null ? x.quantity :item.approvedQuantity || 0),
          "poUomUnit": item.poUomUnit !=null && item.poUomUnit!=undefined ?  item.poUomUnit :"",
        });
      });
      
      if(poQty!=undefined && poQty!=null && poQty.length==1 )
      {
    	  poQty[0].approvedQuantity=item.approvedQuantity;
      }
      self.model.approvedStageQuantity = poQty;
      // item.forApproveStageQty = poQty;
      self.model.selectedIndex = self.model.purchaseOrders.items.indexOf(item);
      if (!item.isSelected) {
        self.model.isSumVisible = false;
      } else {
        self.model.isSumVisible = true;
      }
    }

    var ValidApprovedQty = function(item, qty){
      var isValid = true;

      if (self.model.poTypeId == 3) {
        if (parseInt(qty) > parseInt(item.prQuantity)) {
          growl.error("Approved quantity should not greater than PR quantity");
          isValid = false;
        }
      }

      return isValid;
    }

    var sumApprovedQty = function(){
      var total = 0;
      var selectedItem = self.model.purchaseOrders.items[self.model.selectedIndex];
      angular.forEach(self.model.approvedStageQuantity, function (value, key) {
        if (!parseInt(value.approvedQuantity)) {
          value.approvedQuantity = 0
        }
        total = total + parseInt(value.approvedQuantity);
      });


      if (ValidApprovedQty(selectedItem, total))
      {
        selectedItem.approvedQuantity = total;
        selectedItem.fillRejectQtyFlag = true;
        selectedItem.forApproveStageQty = self.model.approvedStageQuantity;
        if (selectedItem.approvedQuantity > selectedItem.poQuantity)
        {
          selectedItem.rejectQuantity = 0;
          selectedItem.isReject = false;
        }
        else
          {
          selectedItem.rejectQuantity = selectedItem.poQuantity - selectedItem.approvedQuantity;
          if(selectedItem.rejectQuantity > 0){
        	//added to resolved HIS-9501 PO approval for asset - Reduction in the approved quantity does not show the rejection pop up(change position of isReject).
        	  selectedItem.isReject = true;
        	  RejectionPopup(selectedItem);
            //selectedItem.isReject = true;
          }
          else{
            selectedItem.isReject = false;
          }
         
        }

        self.model.ClosePopup('close', 'ApprovedStagedQuantity');
      }
    }

    var ViewPOUom = function (item) {
      self.model.POUoms = [];
      self.model.purchaseOrders.items.find(function(x){
        if (item.id == x.id) {
          if(x.isThreeUOMApplicable == false){
            x.poUom.find(function(d){
              if(d.uomTypeId != 2){
                self.model.POUoms.push(d);
              }
            })
          }
          else{
            self.model.POUoms = x.poUom;
          }
        }
      });
    }

    var ViewBonusUom = function (item) {
      self.model.bonusUoms = [];
      self.model.purchaseOrders.items.find(function(x){
        if (item.id == x.id) {
          if(x.isThreeUOMApplicable == false){
            x.bonusUom.find(function(d){
              if(d.uomTypeId != 2){
                self.model.bonusUoms.push(d);      
              }
            })
          }
          else{
            self.model.bonusUoms = x.bonusUom;
          }
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

    var ViewItems = function (item) {
      self.model.purchaseOrders.items.find(function(x){
        if (item.id == x.id) {
          self.model.addedAccessoryItems = x.poAccessoriesMapper;
        }
      });
    }

    var ViewTerms = function (viewModal, item) {
      self.model.itemTerms = item;
      switch(viewModal){
        case "warrantyPeriodModal":
          self.model.purchaseOrders.items.find(function(x){
            if (item.itemId == x.itemId) {
              //self.model.warrantyPeriod = x.warrentyPeriod;
              self.model.warrantyPeriod = x.warrantyPeriod; //for resolved HIS-9500 PO approval for asset - Details screen does not show warranty period
            }
          });
        break;
        case "expectedSuportTermsModal":
          self.model.purchaseOrders.items.find(function(x){
            if (item.itemId == x.itemId) {
              self.model.supportTerms = x.supportTerms;
            }
          });
        break;
        case "expectedPaymentTermsModal":
          self.model.purchaseOrders.items.find(function(x){
            if (item.itemId == x.itemId) {
              self.model.paymentTerms = x.expectedPayment;
            }
          });
        break;
        case "expectedTechSpecificationModal":
          self.model.purchaseOrders.items.find(function(x){
            if (item.itemId == x.itemId) {
              self.model.technicalSpecs = x.technicalSpecification;
            }
          });
        break;
      }
    }

    var NavigateToBack = function () {
      $state.go('POFinalApproval');
    }

    var DisableEditForOrder = function () {
    	if(self.model.purchaseOrders.approvalStatus=='Approved' || self.model.purchaseOrders.approvalStatus=='Rejected' ){
    		return true;
    	}
    	return false;
    }

    $scope.validateApprovedQty = function(data){
      var isValidItem = true;
      if(data.approvedQuantity > data.quantity){
        growl.error("Approved quantity is greater than PR Quantity");
        data.approvedQuantity = '';
        isValidItem = false;
      }
      return isValidItem;
    }

    initializeController();
  }
  
  function config($stateProvider) {
    $stateProvider.state("detailsPOFinalApproval", {
      url: "/detailsPOFinalApproval",
      templateUrl: 'views/procurement/quotation/details-POFinal-approval.html',
      controller: "DetailsPOFinalApproval.Controller",
      controllerAs: "vm",
      title: 'Purchase Order Final Approval'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("DetailsPOFinalApproval.Controller", detailsPOFinalApprovalController);
})();