(function () {
  "use strict";

  function detailsPOApprovalController($scope, $rootScope, $location, BillingGenericService, rejectReasonService, $http, StatusService, $cookies, CONSTANTS, GenericService, $state, growl, commonDetailService, CurrencyService) {
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
      self.model.CalculateStock1 = CalculateStock1;
      
      GetPurchaseOrders();
      pharmacyGet();
      
      self.model.DisableEditForOrder=DisableEditForOrder;
    }

    var CalculateStock1 = function (item, uom, uomList, oldValue) {
      if (!!uomList) {
        if(!item.stockAvailable){
          item.stockAvailable = 0
        }

        if(item.id && item.poQuantity1){
          var prQuantity = item.poQuantity1;
          }
        else if(item.prQuantity1){
          var prQuantity = item.prQuantity1;
        }
        else{
          var prQuantity = item.poQuantity1;
        }
          var maxOrderQuantity = item.maxOrderQuantity || 0;
          var maxStoreQuantity = item.maxStoreQuantity || 0;

          var uom = uom.filter(function (uom) {
            if (!!uom.uomTypeId) {
              return uom;
            }
          });

          if (!!uomList.uomDetails) {
            uomList.uomDetailsData = uomList.uomDetails.split(' ')[0];
          }

          var oldValue1 = JSON.parse(oldValue);
          var qtylease = 0;
          item.forApproveStageQty.forEach(function (value) {
        	  if(item.isThreeUOMApplicable == false){
        	  if(item.poUomTypeId == 1){
        		  value.quantity = parseInt(value.actualQuantity * (item.uom[0].conversion * item.uom[1].conversion));
        	  }
        	  if(item.poUomTypeId == 3){
        		  value.quantity = parseInt(value.actualQuantity * (item.uom[2].conversion));
        	  }
        	  }
        	  else
        		  {
        		  if(item.poUomTypeId == 1){
            		  value.quantity = parseInt(value.actualQuantity * (item.uom[0].conversion * item.uom[1].conversion));
            	  }
        		  if(item.poUomTypeId == 2){
            		  value.quantity = parseInt(value.actualQuantity * (item.uom[1].conversion * item.uom[2].conversion));
            	  }
            	  if(item.poUomTypeId == 3){
            		  value.quantity = parseInt(value.actualQuantity * (item.uom[2].conversion));
            	  }
        		  }
        	  
        	  if(item.isThreeUOMApplicable == false){
            	  if(uomList.uomTypeId == 1){
            		  value.quantity = parseInt(value.quantity / (item.uom[0].conversion * item.uom[1].conversion * item.uom[2].conversion));
            	  }
            	  if(uomList.uomTypeId == 3){
            		  value.quantity = parseInt(value.quantity / (item.uomListdata[2].conversion));
            	  }
            	  }
            	  else
            		  {
            		  if(uomList.uomTypeId == 1){
                		  value.quantity = parseInt(value.quantity / (item.uom[0].conversion * item.uom[1].conversion * item.uom[2].conversion));
                	  }
            		  if(uomList.uomTypeId == 2){
                		  value.quantity = parseInt(value.quantity / (item.uom[1].conversion * item.uom[2].conversion));
                	  }
                	  if(uomList.uomTypeId == 3){
                		  value.quantity = parseInt(value.quantity / (item.uom[2].conversion));
                	  }
            		  }
          });
          if(item.isThreeUOMApplicable == false){
            if(item.poUomTypeId == 3){
              if(oldValue1.uomTypeId == 3){
                if(uomList.uomTypeId == 2){
                  qtylease = parseInt(prQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
                }
                if(uomList.uomTypeId == 1){
                  qtylease = parseInt(prQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
                }
              }else if(oldValue1.uomTypeId == 1){
                if(uomList.uomTypeId == 2){
                  qtylease = parseInt(prQuantity / (item.uomListdata[0].conversion * item.uomListdata[2].conversion));
                }
                if(uomList.uomTypeId == 3){
                  qtylease = parseInt(prQuantity * (item.uomListdata[2].conversion));
                }
              }
              else if(oldValue1.uomTypeId == 2){
                if(uomList.uomTypeId == 1){
                  qtylease = parseInt(prQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
                }
                if(uomList.uomTypeId == 3){
                  qtylease = parseInt(prQuantity * (item.uomListdata[2].conversion));
                }
              }
            }
  
            if(item.poUomTypeId == 2){
              if(oldValue1.uomTypeId == 3){
                if(uomList.uomTypeId == 2){
                  qtylease = parseInt(prQuantity * item.uomListdata[2].conversion);
                }
                if(uomList.uomTypeId == 1){
                  qtylease = parseInt(prQuantity / (item.uomListdata[0].conversion * item.uomListdata[2].conversion));
                }
              }else if(oldValue1.uomTypeId == 1){
                if(uomList.uomTypeId == 2){
                  qtylease = parseInt(prQuantity * (item.uomListdata[0].conversion * item.uomListdata[2].conversion));
                }
                if(uomList.uomTypeId == 3){
                  qtylease = parseInt(prQuantity * (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
                }
              }
              else if(oldValue1.uomTypeId == 2){
                if(uomList.uomTypeId == 1){
                  qtylease = parseInt(prQuantity / (item.uomListdata[0].conversion * item.uomListdata[2].conversion));
                }
                if(uomList.uomTypeId == 3){
                  qtylease = parseInt(prQuantity * (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
                }
              }
            }
  
            if(item.poUomTypeId == 1){
              if(oldValue1.uomTypeId == 3){
                if(uomList.uomTypeId == 2){
                  qtylease = parseInt(prQuantity * (item.uomListdata[0].conversion * item.uomListdata[2].conversion));
                }
                if(uomList.uomTypeId == 1){
                  qtylease = parseInt(prQuantity * item.uomListdata[2].conversion);
                }
              }else if(oldValue1.uomTypeId == 1){
                if(uomList.uomTypeId == 2){
                  qtylease = parseInt(prQuantity * (item.uomListdata[0].conversion * item.uomListdata[2].conversion));
                }
                if(uomList.uomTypeId == 3){
                  qtylease = parseInt(prQuantity *  (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
                }
              }
              else if(oldValue1.uomTypeId == 2){
                if(uomList.uomTypeId == 1){
                  qtylease = parseInt(prQuantity * item.uomListdata[2].conversion);
                }
                if(uomList.uomTypeId == 3){
                  qtylease = parseInt(prQuantity * (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
                }
              }
            }
           
            if (uomList.uomDetailsData == "P") {
              item.prQuantity = qtylease;
//              item.poQuantity = qtylease;
              item.approvedQuantity = qtylease;
              
              item.newQty = parseInt(maxOrderQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
              item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
              item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
              item.stockAvailable = parseInt(item.stockAvailable1 / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
              item.receivedQtyCon=(item.approvedQuantity +"*"+ item.poUom[0].conversion);
              item.receivedQtyCalculation = (item.approvedQuantity * item.poUom[0].conversion);
              item.uomUnitData = item.uomListdata[2].uomUnit;
            } else if (uomList.uomDetailsData == "S") {
//              item.poQuantity = qtylease;
              item.prQuantity = qtylease;
              item.approvedQuantity = qtylease;
  
              item.newQty = parseInt(maxOrderQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
              item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
              item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
              item.stockAvailable = parseInt(item.stockAvailable1 / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            } else if (uomList.uomDetailsData == "D") {
//              item.poQuantity = qtylease;
              item.prQuantity = qtylease;
              item.approvedQuantity = qtylease;
              
              item.newQty = parseInt(maxOrderQuantity / (item.uomListdata[2].conversion));
              item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[2].conversion));
              item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[2].conversion));
              item.stockAvailable = parseInt(item.stockAvailable1 / (item.uomListdata[2].conversion));
              item.receivedQtyCon=(item.approvedQuantity);
              item.receivedQtyCalculation = (item.approvedQuantity);
              item.uomUnitData = item.uomListdata[2].uomUnit;
            }
          }
          else{
            if(item.poUomTypeId == 3){
              if(oldValue1.uomTypeId == 3){
                if(uomList.uomTypeId == 2){
                  qtylease = parseInt(prQuantity / (uom[1].conversion * uom[2].conversion));
                }
                if(uomList.uomTypeId == 1){
                  qtylease = parseInt(prQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
                }
              }else if(oldValue1.uomTypeId == 1){
                if(uomList.uomTypeId == 2){
                  qtylease = parseInt(prQuantity / (uom[1].conversion * uom[2].conversion));
                }
                if(uomList.uomTypeId == 3){
                  qtylease = parseInt(prQuantity * (uom[2].conversion));
                }
              }
              else if(oldValue1.uomTypeId == 2){
                if(uomList.uomTypeId == 1){
                  qtylease = parseInt(prQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
                }
                if(uomList.uomTypeId == 3){
                  qtylease = parseInt(prQuantity * (uom[2].conversion));
                }
              }
            }
  
            if(item.poUomTypeId == 2){
              if(oldValue1.uomTypeId == 3){
                if(uomList.uomTypeId == 2){
                  qtylease = parseInt(prQuantity * uom[2].conversion);
                }
                if(uomList.uomTypeId == 1){
                  qtylease = parseInt(prQuantity / (uom[0].conversion * uom[2].conversion));
                }
              }else if(oldValue1.uomTypeId == 1){
                if(uomList.uomTypeId == 2){
                  qtylease = parseInt(prQuantity * (uom[0].conversion * uom[2].conversion));
                }
                if(uomList.uomTypeId == 3){
                  qtylease = parseInt(prQuantity * (uom[1].conversion * uom[2].conversion));
                }
              }
              else if(oldValue1.uomTypeId == 2){
                if(uomList.uomTypeId == 1){
                  qtylease = parseInt(prQuantity / (uom[0].conversion * uom[2].conversion));
                }
                if(uomList.uomTypeId == 3){
                  qtylease = parseInt(prQuantity * (uom[1].conversion * uom[2].conversion));
                }
              }
            }
  
            if(item.poUomTypeId == 1){
              if(oldValue1.uomTypeId == 3){
                if(uomList.uomTypeId == 2){
                  qtylease = parseInt(prQuantity * (uom[0].conversion * uom[2].conversion));
                }
                if(uomList.uomTypeId == 1){
                  qtylease = parseInt(prQuantity * uom[2].conversion);
                }
              }else if(oldValue1.uomTypeId == 1){
                if(uomList.uomTypeId == 2){
                  qtylease = parseInt(prQuantity * (uom[0].conversion * uom[2].conversion));
                }
                if(uomList.uomTypeId == 3){
                  qtylease = parseInt(prQuantity *  (uom[0].conversion * uom[1].conversion * uom[2].conversion));
                }
              }
              else if(oldValue1.uomTypeId == 2){
                if(uomList.uomTypeId == 1){
                  qtylease = parseInt(prQuantity * uom[2].conversion);
                }
                if(uomList.uomTypeId == 3){
                  qtylease = parseInt(prQuantity * (uom[0].conversion * uom[1].conversion * uom[2].conversion));
                }
              }
            }
           
            if (uomList.uomDetailsData == "P") {
              item.prQuantity = qtylease;
//              item.poQuantity = qtylease;
              item.approvedQuantity = qtylease;
              
              item.newQty = parseInt(maxOrderQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
              item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
              item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
              item.stockAvailable = parseInt(item.stockAvailable1 / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
              item.receivedQtyCon = (item.approvedQuantity +"*"+ item.poUom[0].conversion+"*"+ item.poUom[1].conversion);
              item.receivedQtyCalculation = (item.approvedQuantity * item.poUom[0].conversion * item.poUom[1].conversion);
              item.uomUnitData = uom[2].uomUnit;
            } else if (uomList.uomDetailsData == "S") {
//              item.poQuantity = qtylease;
              item.prQuantity = qtylease;
              item.approvedQuantity = qtylease;
              item.newQty = parseInt(maxOrderQuantity / (uom[1].conversion * uom[2].conversion));
              item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[1].conversion * uom[2].conversion));
              item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[1].conversion * uom[2].conversion));
              item.stockAvailable = parseInt(item.stockAvailable1 / (uom[1].conversion * uom[2].conversion));
              item.receivedQtyCon=(item.approvedQuantity +"*"+ item.poUom[1].conversion);
              item.receivedQtyCalculation = (item.approvedQuantity * item.poUom[1].conversion);
              item.uomUnitData = uom[2].uomUnit;
            } else if (uomList.uomDetailsData == "D") {
//              item.poQuantity = qtylease;
              item.prQuantity = qtylease;
              item.approvedQuantity = qtylease;
              item.newQty = parseInt(maxOrderQuantity / (uom[2].conversion));
              item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[2].conversion));
              item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[2].conversion));
              item.stockAvailable = parseInt(item.stockAvailable1 / (uom[2].conversion));
              item.receivedQtyCon=(item.approvedQuantity);
              item.receivedQtyCalculation = (item.approvedQuantity);
              item.uomUnitData = uom[2].uomUnit;
            }
          }
          item.isReject=false;
          RejectionPopup(item);
          $scope.calculateAmtForAllItems();
        }
      // } 
      else {
        item.newPRQty = 0;
        item.newMaxOrderQuantity = 0;
        item.newMaxStoreQuantity = 0;
      }
    }

    $scope.calculateReceivedQty = function (item) {
      if(item.approvedQuantity == undefined || item.approvedQuantity == null || item.approvedQuantity == ""){
        item.approvedQuantity = 0;
      }
//      item.poUomTypeId=item.selectedUom.uomTypeId;
      if(item.isThreeUOMApplicable == false){
        if(item.selectedUom.uomTypeId == 1){
          item.receivedQtyCon = (item.approvedQuantity +"*"+ item.poUom[0].conversion);
          item.receivedQtyCalculation = (item.approvedQuantity * item.poUom[0].conversion);
          item.uomUnitData = item.poUom[2].uomUnit;
        }
        else if(item.selectedUom.uomTypeId == 2){
        
        }
        else{
          item.receivedQtyCon = (item.approvedQuantity);
          item.receivedQtyCalculation = (item.approvedQuantity);
          item.uomUnitData = item.poUom[2].uomUnit;
        }
      }
      else{
        if(item.selectedUom.uomTypeId== 1){
          item.receivedQtyCon = (item.approvedQuantity +"*"+ item.poUom[0].conversion+"*"+ item.poUom[1].conversion);
          item.receivedQtyCalculation = (item.approvedQuantity * item.poUom[0].conversion * item.poUom[1].conversion);
          item.uomUnitData = item.poUom[2].uomUnit;
        }
        else if(item.selectedUom.uomTypeId == 2){
          item.receivedQtyCon = (item.approvedQuantity +"*"+ item.poUom[1].conversion);
          item.receivedQtyCalculation = (item.approvedQuantity * item.poUom[1].conversion);
          item.uomUnitData = item.poUom[2].uomUnit;
        }
        else{
          item.receivedQtyCon = (item.approvedQuantity);
          item.receivedQtyCalculation = (item.approvedQuantity);
          item.uomUnitData = item.poUom[2].uomUnit;
        }
      }
    }

    $scope.calculateReceivedBonusQty = function(item){
      if(item.bonusApprovedQuantity == undefined || item.bonusApprovedQuantity == null || item.bonusApprovedQuantity == ""){
        item.bonusApprovedQuantity = 0;
      }
      if(item.isThreeUOMApplicable == false){
        if(item.bonusUomTypeId == 1){
          item.receivedQtyBonusCon = (item.bonusApprovedQuantity +"*"+ item.bonusUom[0].conversion);
          item.receivedQtyBonusCalculation = (item.bonusApprovedQuantity * item.bonusUom[0].conversion);
          item.uomUnitDataBonus = item.bonusUom[2].uomUnit;
        }
        else if(item.bonusUomTypeId == 2){
        
        }
        else{
          item.receivedQtyBonusCon = (item.bonusApprovedQuantity);
          item.receivedQtyBonusCalculation = (item.bonusApprovedQuantity);
          item.uomUnitDataBonus = item.bonusUom[2].uomUnit;
        }
      }
      else{
        if(item.bonusUomTypeId == 1){
          item.receivedQtyBonusCon = (item.bonusApprovedQuantity +"*"+ item.bonusUom[0].conversion+"*"+ item.bonusUom[1].conversion);
          item.receivedQtyBonusCalculation = (item.bonusApprovedQuantity * item.bonusUom[0].conversion * item.bonusUom[1].conversion);
          item.uomUnitDataBonus = item.bonusUom[2].uomUnit;
        }
        else if(item.bonusUomTypeId == 2){
          item.receivedQtyBonusCon = (item.bonusApprovedQuantity +"*"+ item.bonusUom[1].conversion);
          item.receivedQtyBonusCalculation = (item.bonusApprovedQuantity * item.bonusUom[1].conversion);
          item.uomUnitDataBonus = item.bonusUom[2].uomUnit;
        }
        else{
          item.receivedQtyBonusCon = (item.bonusApprovedQuantity);
          item.receivedQtyBonusCalculation = (item.bonusApprovedQuantity);
          item.uomUnitDataBonus = item.bonusUom[2].uomUnit;
        }
      }
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
          if(charges.otherCharge!=undefined && charges.otherCharge!=null)
        	  {
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
    	  if(item.otherCharge!=undefined && item.otherCharge!=null)
    	  {
        if (!!item.otherCharge.id) {
          if (!object[item.otherCharge.id])
            object[item.otherCharge.id] = 0;
          object[item.otherCharge.id] += 1;
        }
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
    	if(self.model.otherChargesList!=undefined && self.model.otherChargesList!=null && self.model.otherChargesList.length>0)
    		{
    		self.model.otherChargesList.forEach(function (othercharge) {
                othercharge.amount=(othercharge.savedAmount!=undefined && othercharge.savedAmount!=null ? othercharge.savedAmount:0);
            });
    		}
//    	var total = 0;
//    	var otherList=[];
//        self.model.otherChargesList.forEach(function (value) {
//          value.otherCharge = self.model.otherChargesDropdownList.find(function (key) {
//            if (value.otherCharge!=undefined && value.otherCharge!=null && value.otherCharge.id == key.id) {
//            	if(value.otherChargesId==undefined || value.otherChargesId==null)
//            		{
//            		value.otherChargesId = key.id;
//            		}
//            	else
//            		{
//            		value.otherChargesId = key.id;
//            		}
//              return key;
//            }
//          });
//        });
//
//        angular.forEach(self.model.otherChargesList, function (value, key) {
//          // delete value['otherCharge'];
//          total = total + parseFloat(value.amount);
//          if(value.amount!=undefined && value.amount!=null && value.amount>0)
//          {
//          otherList.push(value)
//          }
//        });
//        self.model.otherChargesList=otherList;
//        var otherChergesFromPo=self.model.purchaseOrders.otherCharges;
//        angular.forEach(self.model.purchaseOrders.otherCharges, function(datacharges){
//          datacharges.amount = total;
//        });
//        self.model.purchaseOrders.otherCharges = [];
//        self.model.otherChargesList.forEach(function(othercharData){
//          var obj = {
//            otherChargesId : '',
//            amount: '',
//            id:'',
//            isFromPo:''
//          }
//          obj.otherChargesId = othercharData.otherChargesId;
//          obj.amount = othercharData.amount;
//          obj.id=othercharData.id;
//          otherChergesFromPo.forEach(function(othercharDataGrn){
//        	  if(othercharDataGrn.otherChargesId==othercharData.otherChargesId)
//        		  {
//        		  obj.isFromPo=othercharDataGrn.isFromPo;
//        		  }
//        	  else
//        		  {
//        		  obj.isFromPo=false;
//        		  }
//          });
//          self.model.purchaseOrders.otherCharges.push(obj);
//    });
//        self.model.purchaseOrders.netAmount = parseFloat(self.model.purchaseOrders.netAmount) - parseFloat(self.model.purchaseOrders.totalOtherAmount);
//        self.model.purchaseOrders.totalOtherAmount = total;
//        self.model.otherChargesAmount = total;
//        self.model.purchaseOrders.netAmount = parseFloat(self.model.purchaseOrders.netAmount) + parseFloat(self.model.purchaseOrders.totalOtherAmount);
//        $scope.calculateOtherChargesAmountForItem();
//        $scope.calculateAmtForAllItems();
        self.model.ClosePopup('close', 'otherCharges_Modal');
    }
    var sumOtherCharges = function (chargeList) {
    	if($scope.approvedQtyItemListFlag==true)
    	  {
    		self.model.otherChargesList=chargeList;
    	  }
      if (validateCharges() && DuplicateData(chargeList)) {
        var total = 0;
        self.model.otherChargesList.forEach(function (value) {
          value.otherCharge = self.model.otherChargesDropdownList.find(function (key) {
            if (value.otherCharge!=undefined && value.otherCharge!=null && value.otherCharge.id == key.id) {
            	if(value.otherChargesId==undefined || value.otherChargesId==null)
            		{
            		value.otherChargesId = key.id;
            		}
            	else
            		{
            		value.otherChargesId = key.id;
            		}
              return key;
            }
          });
        });

        angular.forEach(self.model.otherChargesList, function (value, key) {
          // delete value['otherCharge'];
          total = total + parseFloat(value.amount);
        });

        var otherChergesFromPo=self.model.purchaseOrders.otherCharges;
        angular.forEach(self.model.purchaseOrders.otherCharges, function(datacharges){
          datacharges.amount = total;
        });
        self.model.purchaseOrders.otherCharges = [];
        self.model.otherChargesList.forEach(function(othercharData){
          var obj = {
            otherChargesId : '',
            amount: '',
            id:'',
            isFromPo:''
          }
          obj.otherChargesId = othercharData.otherChargesId;
          obj.amount = othercharData.amount;
          obj.id=othercharData.id;
          otherChergesFromPo.forEach(function(othercharDataGrn){
        	  if(othercharDataGrn.otherChargesId==othercharData.otherChargesId)
        		  {
        		  obj.isFromPo=othercharDataGrn.isFromPo;
        		  }
        	  else
        		  {
        		  obj.isFromPo=false;
        		  }
          });
          self.model.purchaseOrders.otherCharges.push(obj);
        });

        self.model.purchaseOrders.netAmount = parseFloat(self.model.purchaseOrders.netAmount) - parseFloat(self.model.purchaseOrders.totalOtherAmount);
        self.model.purchaseOrders.totalOtherAmount = total;
        self.model.otherChargesAmount = total;
        self.model.purchaseOrders.netAmount = parseFloat(self.model.purchaseOrders.netAmount) + parseFloat((self.model.purchaseOrders.netAmount!=undefined
        && self.model.purchaseOrders.netAmount!=null && self.model.purchaseOrders.netAmount>0 ? self.model.purchaseOrders.totalOtherAmount :0));
        $scope.calculateOtherChargesAmountForItem();
        $scope.calculateAmtForAllItems();
        self.model.ClosePopup('close', 'otherCharges_Modal');
      }
    }

    var fillOtherChargesList = function (item) {
       angular.forEach(item.items, function(data){
        if(item.otherCharges){
          $scope.selectedOtherCharges = [];
          angular.forEach(item.otherCharges, function(data1){
            var obj = {
              otherChargesId: '',
              amount: '',
              id:''
            }
            obj.otherChargesId = data1.otherChargesId;
            obj.amount = data1.amount;
            obj.id=data1.id;
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
                  othercharge.savedAmount = amountNumber.toFixed(2);
                }
              });
              $scope.dataother = $scope.selectedOtherCharges;
              data['selectedOtherCharges']=$scope.dataother;
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
              othercharge.savedAmount = amountNumber.toFixed(2);
            }
          });
        }
        if(!item.otherCharges){
          self.model.otherChargesList = data.selectedOtherCharges;
        }
        else{
          self.model.otherChargesList = data.selectedOtherCharges;
        }
        self.model.otherChargesDropdownList = self.model.defaultOtherCharge.concat(item.otherChargesList);
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
      $scope.grnIdForDoccumentUpload = commonDetailService.getDataId();
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER_APPROVAL.DETAILS_API  + $scope.unitId + '/'+$scope.organizationId + '/' + commonDetailService.getDataId();
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(function (response) {
    	  $scope.isQuantityRejected=false;
    	  var count=0;
        self.model.purchaseOrders = response.data;
        if(self.model.purchaseOrders.otherCharges==undefined || self.model.purchaseOrders.otherCharges==null ||  self.model.purchaseOrders.otherCharges.length==0)
        	{
        	self.model.purchaseOrders.totalOtherAmount=0;
        	}
        self.model.vendorId = response.data.vendorId;
        self.model.assetTypeId = response.data.assetTypeId;
        self.model.poTypeId = response.data.poTypeId;
        self.model.paymentTerm = response.data.paymentTerm;
        self.model.remark = response.data.remark;
        self.model.requestItems = JSON.parse(JSON.stringify(response.data.items));
        self.model.otherChargesDropdownList = self.model.defaultOtherCharge.concat(response.data.otherChargesList);
        self.model.otherChargesList = self.model.otherChargesDropdownList;
        self.model.purchaseOrders.roundOffAmount = self.model.purchaseOrders.roundOffAmount.toFixed(2);
        self.model.selectAllRows = true;
        self.model.roundOffCHECK = 0;
        self.model.roundOffCHECK = self.model.purchaseOrders.roundOffAmount;
        if(self.model.purchaseOrders.approvalStatusId==7)
        	{
        	self.model.purchaseOrders.otherCharges=[];
        	}
        self.model.purchaseOrders.items.forEach(function (item) {
        	if(item.approvedQuantity==undefined || item.approvedQuantity==null)
        		{
        		item.approvedQuantity=item.poQuantity;
        		}
        	item.isSelected = true;
          item.statusId = 5;
        });
        $scope.calculateOtherChargesAmountForItem();
    //    self.model.purchaseOrders.netAmount=self.model.purchaseOrders.netAmount+(self.model.purchaseOrders.roundOffAmount!=undefined ? parseFloat(self.model.purchaseOrders.roundOffAmount) :0)
        self.model.purchaseOrders.items.forEach(function (item) {
//        	if(item.rejectQuantity!=undefined &&item.rejectQuantity!=null && item.approvedQuantity!=undefined && item.approvedQuantity!=null)
//          {
          	$scope.calculateAmtForAllItems();
//          }
          	item.bonusApprovedQuantity=item.bonusQuantity;
            item.purchaseOrderStagedDto.forEach(function (stagedQty) {
            	stagedQty.poUomUnit=item.poUomUnit;
            	stagedQty.poUomTypeId=item.poUomTypeId;
            	stagedQty.actualQuantity=stagedQty.quantity;
            })
            var flag=false;
            angular.forEach(self.model.purchaseOrders.otherCharges, function(d){
            	if(d.isFromPo==false)
            		{
            		flag=true;
            		}
            })
            if(flag==true)
        	  {
          self.model.purchaseOrders.netAmount = self.model.purchaseOrders.netAmount - self.model.purchaseOrders.totalOtherAmount;
        	  }
            $scope.calculateOtherChargesAmountForItem();
            $scope.calculateAmtForAllItems();
        	item.poQuantityOld = item.poQuantity;
        	if(item.rejectQuantity>0)
          {
        		count++;
          }
        	if(count>0)
          {
        		$scope.isQuantityRejected=true;
          }
          item.newPOQty = item.poQuantity;
          item.prQuantity1 = item.prQuantity;
          item.poQuantity1 = item.poQuantity;
          if(item.approvedQuantity == null && item.approvedQuantity == undefined){
            item.approvedQuantity = item.poQuantity;
          }
          item.isDisabled = false;
          item.forApproveStageQty = item.purchaseOrderStagedDto;
          item.fillRejectQtyFlag = true;
          if(item.approvedQuantity != undefined && item.approvedQuantity != null){
            item.poAmount = item.approvedQuantity * item.cop;
          }
          else{
            item.poAmount = item.poQuantity * item.cop;
          }
          if (!!item.rejectQuantity) {
            item.isReject = true;
          }
          var uomListBonus = null;
          if (self.model.assetTypeId == 1) {
            item.selectedUom = item.uom.find(function(uom){
              return item.poUomTypeId == uom.uomTypeId;
            });
            uomListBonus = item.uom.find(function(uom){
              return item.bonusUomTypeId == uom.uomTypeId;
            });
            if(uomListBonus!=undefined && uomListBonus!=null )
            	{
            	item.bonusUomType = uomListBonus.uomDetails;
            	}
            if(item.isThreeUOMApplicable == false){
              item.uomListdata = item.uom;
              item.uom = [];
              angular.forEach(item.uomListdata, function(data){
                if(data.uomType != 'Stock'){
                  item.uom.push(data);
                }
              });
            }
          }
          item['selectedOtherCharges'] = [{ otherChargesId: self.model.otherChargesDropdownList[0].id, amount: 0 }];
          self.model.purchaseOrders.totalOtherAmount = 0;
         var flag=false;
            angular.forEach(self.model.purchaseOrders.otherCharges, function(d){
            	if(d.isFromPo==false)
            		{
            		flag=true;
            		}
              self.model.purchaseOrders.totalOtherAmount += d.amount;
            });
          if(self.model.purchaseOrders.netAmount==undefined || self.model.purchaseOrders.netAmount==null || self.model.purchaseOrders.netAmount==0)
          {
          self.model.purchaseOrders.totalOtherAmount=0.00;
          }
          self.model.purchaseOrders.totalNetAmount=self.model.purchaseOrders.netAmount;
          self.model.SelectedItems.push(item);
          CalculateStock(item, item.uom, item.selectedUom);
          // if(item.status == 'Approved'){
          //   $scope.calculateAmtForAllItems();
          // }
        });
        if(response.data.discountTypeId == 2){
          self.model.purchaseOrders.discount = self.model.purchaseOrders.totalDiscount; 
        }
        $rootScope.stopLoader();
      },
      function (err) {
        $rootScope.stopLoader();
        growl.error(err.data.message);
      });
    }

    var CalculateStock = function (item, uom, uomList) {
      if (!!uomList) {
        var prQuantity1 = item.prQuantity1;
        var maxOrderQuantity = item.maxOrderQuantity || 0;
        var maxStoreQuantity = item.maxStoreQuantity || 0;
        var poQty = item.newPOQty || 0;
        item.forApproveStageQty.forEach(function (value) {
      	  if(item.isThreeUOMApplicable == false){
	      	  if(value.poUomTypeId == 1){
	      		  value.quantity = parseInt(value.actualQuantity * (item.uom[0].conversion * item.uom[1].conversion));
	      	  }
	      	  if(value.poUomTypeId == 3){
	      		  value.quantity = parseInt(value.actualQuantity * (item.uom[1].conversion));
	      	  }
      	  }
      	  else
  		  {
      		  if(value.poUomTypeId == 1){
          		  value.quantity = parseInt(value.actualQuantity * (item.uom[0].conversion * item.uom[1].conversion * item.uom[2].conversion));
          	  }
      		  if(value.poUomTypeId == 2){
          		  value.quantity = parseInt(value.actualQuantity * (item.uom[1].conversion * item.uom[2].conversion));
          	  }
          	  if(value.poUomTypeId == 3){
          		  value.quantity = parseInt(value.actualQuantity * (item.uom[2].conversion));
          	  }
  		  }
      	  
      	  if(item.isThreeUOMApplicable == false){
          	  if(uomList.uomTypeId == 1){
          		  value.quantity = parseInt(value.quantity / (item.uom[0].conversion * item.uom[1].conversion));
          	  }
          	  if(uomList.uomTypeId == 3){
          		  value.quantity = parseInt(value.quantity / (item.uom[1].conversion));
          	  }
      	  }
      	  else
  		  {
      		  if(uomList.uomTypeId == 1){
          		  value.quantity = parseInt(value.quantity / (item.uom[0].conversion * item.uom[1].conversion * item.uom[2].conversion));
          	  }
      		  if(uomList.uomTypeId == 2){
          		  value.quantity = parseInt(value.quantity / (item.uom[1].conversion * item.uom[2].conversion));
          	  }
          	  if(uomList.uomTypeId == 3){
          		  value.quantity = parseInt(value.quantity / (item.uom[2].conversion));
          	  }
  		  }
        });
        if(item.approvedQuantity == undefined || item.approvedQuantity == null || item.approvedQuantity == ""){
          item.approvedQuantity = 0;
        }
        if(item.bonusApprovedQuantity == undefined || item.bonusApprovedQuantity == null || item.bonusApprovedQuantity == ""){
          item.bonusApprovedQuantity = 0;
        }
        if(item.bonusQuantity == undefined || item.bonusQuantity == null || item.bonusQuantity == ""){
          item.bonusQuantity = 0;
        }
        uomList.uomDetailsData = uomList.uomDetails.split(' ')[0];
        if(item.isThreeUOMApplicable == false){
          if (uomList.uomDetailsData == "P") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.receivedQtyCon=(item.approvedQuantity +"*"+ item.poUom[0].conversion);
            item.receivedQtyCalculation = (item.approvedQuantity * item.poUom[0].conversion);
            item.uomUnitData = item.uomListdata[2].uomUnit;
//            if(item.bonusApprovedQuantity != undefined && item.bonusApprovedQuantity != null){
//              item.receivedQtyBonusCon=(item.bonusApprovedQuantity +"*"+ item.bonusUom[0].conversion);
//              item.receivedQtyBonusCalculation = (item.bonusApprovedQuantity * item.bonusUom[0].conversion);
//              item.uomUnitDataBonus = item.uomListdata[2].uomUnit;
//            }
//            else{
//              item.receivedQtyBonusCon=(item.bonusQuantity +"*"+ item.bonusUom[0].conversion);
//              item.receivedQtyBonusCalculation = (item.bonusQuantity * item.bonusUom[0].conversion);
//              item.uomUnitDataBonus = item.uomListdata[2].uomUnit;
//            }
          } else if (uomList.uomDetailsData == "S") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
          } else if (uomList.uomDetailsData == "D") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[2].conversion));
            item.receivedQtyCon=(item.approvedQuantity);
            item.receivedQtyCalculation = (item.approvedQuantity);
            item.uomUnitData = item.uomListdata[2].uomUnit;
//            if(item.bonusApprovedQuantity != undefined && item.bonusApprovedQuantity != null){
//              item.receivedQtyBonusCon=(item.bonusApprovedQuantity);
//              item.receivedQtyBonusCalculation = (item.bonusApprovedQuantity);
//              item.uomUnitDataBonus = item.uomListdata[2].uomUnit;
//            }
//            else{
//              item.receivedQtyBonusCon=(item.bonusQuantity);
//              item.receivedQtyBonusCalculation = (item.bonusQuantity);
//              item.uomUnitDataBonus = item.uomListdata[2].uomUnit;
//            }
          }
        }
        else{
          if (uomList.uomDetailsData == "P") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.receivedQtyCon = (item.approvedQuantity +"*"+ item.poUom[0].conversion+"*"+ item.poUom[1].conversion);
            item.receivedQtyCalculation = (item.approvedQuantity * item.poUom[0].conversion * item.poUom[1].conversion);
            item.uomUnitData = uom[2].uomUnit;
//            if(item.bonusApprovedQuantity != undefined && item.bonusApprovedQuantity != null){
//              item.receivedQtyBonusCon = (item.bonusApprovedQuantity +"*"+ item.bonusUom[0].conversion+"*"+ item.bonusUom[1].conversion);
//              item.receivedQtyBonusCalculation = (item.bonusApprovedQuantity * item.bonusUom[0].conversion * item.bonusUom[1].conversion);
//              item.uomUnitDataBonus = uom[2].uomUnit;
//            }
//            else{
//              item.receivedQtyBonusCon = (item.bonusQuantity +"*"+ item.bonusUom[0].conversion+"*"+ item.bonusUom[1].conversion);
//              item.receivedQtyBonusCalculation = (item.bonusQuantity * item.bonusUom[0].conversion * item.bonusUom[1].conversion);
//              item.uomUnitDataBonus = uom[2].uomUnit;
//            }
          } else if (uomList.uomDetailsData == "S") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[1].conversion * uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[1].conversion * uom[2].conversion));
            item.receivedQtyCon=(item.approvedQuantity +"*"+ item.poUom[1].conversion);
            item.receivedQtyCalculation = (item.approvedQuantity * item.poUom[1].conversion);
            item.uomUnitData = uom[2].uomUnit;
//            if(item.bonusApprovedQuantity != undefined && item.bonusApprovedQuantity != null){
//              item.receivedQtyBonusCon = (item.bonusApprovedQuantity +"*"+ item.bonusUom[1].conversion);
//              item.receivedQtyBonusCalculation = (item.bonusApprovedQuantity * item.bonusUom[1].conversion);
//              item.uomUnitDataBonus = uom[2].uomUnit;
//            }
//            else{
//              item.receivedQtyBonusCon = (item.bonusQuantity +"*"+ item.bonusUom[1].conversion);
//              item.receivedQtyBonusCalculation = (item.bonusQuantity * item.bonusUom[1].conversion);
//              item.uomUnitDataBonus = uom[2].uomUnit; 
//            }
          } else if (uomList.uomDetailsData == "D") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[2].conversion));
            item.receivedQtyCon=(item.approvedQuantity);
            item.receivedQtyCalculation = (item.approvedQuantity);
            item.uomUnitData = uom[2].uomUnit;
//            if(item.bonusApprovedQuantity != undefined && item.bonusApprovedQuantity != null){
//              item.receivedQtyBonusCon=(item.bonusApprovedQuantity);
//              item.receivedQtyBonusCalculation = (item.bonusApprovedQuantity);
//              item.uomUnitDataBonus = uom[2].uomUnit;
//            }
//            else{
//              item.receivedQtyBonusCon=(item.bonusQuantity);
//              item.receivedQtyBonusCalculation = (item.bonusQuantity);
//              item.uomUnitDataBonus = uom[2].uomUnit; 
//            }
          }
        }
        if(item.bonusQuantity!=undefined && item.bonusQuantity!=null)
        {
        if(item.bonusUomTypeId==1)
            {
            item.receivedQtyBonusCon = (item.bonusQuantity +"*"+ item.bonusUom[0].conversion+"*"+ item.bonusUom[1].conversion);
            item.receivedQtyBonusCalculation = (item.bonusQuantity * item.bonusUom[0].conversion * item.bonusUom[1].conversion);
            item.uomUnitDataBonus = uom[2].uomUnit;
            }
        else if(item.bonusUomTypeId==2)
            {
            item.receivedQtyBonusCon = (item.bonusQuantity +"*"+ item.bonusUom[1].conversion);
            item.receivedQtyBonusCalculation = (item.bonusQuantity * item.bonusUom[1].conversion);
            item.uomUnitDataBonus = uom[2].uomUnit;
            }
        
        }
      } else {
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
      item.isReject=false;
      prevRequest=item;
      if (prevRequest) {
        item.rejectQuantity = 0;
        item.approvedQuantity = prevRequest.prQuantity!=null ? prevRequest.prQuantity:prevRequest.poQuantity;
        item.rejectNote = prevRequest.rejectNote;
        item.rejectReasonId = prevRequest.rejectReasonId;
        if (item.approvedQuantity == (prevRequest.prQuantity!=null ? prevRequest.prQuantity:prevRequest.poQuantity)) {
          item.statusId = setStatus('Approved');
        }
        else{
          item.statusId = prevRequest.statusId;
        }
        item.fillRejectQtyFlag = true;
        //added for when call cancelRejection for resolved HIS-9501 PO approval for asset - Reduction in the approved quantity does not show the rejection pop up.
        item.forApproveStageQty = item.purchaseOrderStagedDto;
        $scope.calculateAmtForAllItems();
        sumOtherCharges(self.model.purchaseOrders.otherCharges);
        $scope.calculateReceivedQty(item);
        $scope.amountround1 = 0;
      }
    }

    $scope.getPatientDocumentList = function()
    {  
      $rootScope.startLoader();
      var data =
      {
        "patientId":0,
        "unitId":$scope.unitId,
        "organisationId":$scope.organizationId,
        "userId":undefined,
        "visitTypeId":undefined,
        "admissionId":undefined,
        "poId":$scope.grnIdForDoccumentUpload,
      }
      var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
      BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
        $scope.patientDocumentList = [];
        if (response.data.status == "success") 
        {
       	  $scope.patientDocumentList = response.data.listObject; 
       	  $('#uploadDetails').modal('show');
       	  angular.forEach($scope.patientDocumentList,function(value, key) {
       		  var filepath = value.documentPath
            var arr = filepath.split("/");
            var length=arr.length;
            var fileName1=arr[length-1];
            var arr1 = fileName1.split("_");
            var length1=arr1.length;
            var fileName=arr1[length1-1];
            value.fileName=fileName;
          });
          $rootScope.stopLoader();
        }
        else if(response.data.status == "error")
        {
       	  $rootScope.stopLoader();
        }
      });
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
            if (self.model.rejectQty > (item.prQuantity!=null ? item.prQuantity : item.poQuantity) ) {
              growl.error('Rejection quantity is must be less than ' + item.poQuantity);
              return false;
            } else if (self.model.rejectQty == (item.prQuantity!=null ? item.prQuantity : item.poQuantity)) {
              item.rejectQuantity = item.prQuantity!=null ? item.prQuantity : item.poQuantity;
              item.approvedQuantity = (item.prQuantity!=null ? item.prQuantity : item.poQuantity) - item.rejectQuantity;
              item.rejectReasonId = self.model.rejectReasons[0].id;
              item.statusId = setStatus('Rejected'); 
              item.isReject = true;
            } else if (self.model.rejectQty == 0 || !self.model.rejectQty) {
              item.approvedQuantity = item.prQuantity!=null ? item.prQuantity : item.poQuantity;
              item.rejectQuantity = 0;
              item.rejectReasonId = self.model.rejectReasons[0].id;
              item.statusId = setStatus('Approved');
              item.isReject = false;
            } else {
              item.rejectQuantity = self.model.rejectQty;
              item.rejectNote = self.model.rejectNote;
              item.approvedQuantity = (item.prQuantity!=null ? item.prQuantity : item.poQuantity) - item.rejectQuantity;
              item.rejectReasonId = self.model.rejectReasons[0].id;
              item.statusId = setStatus('Approved'); // old Partially Approved
              item.isReject = true;
            }
            self.model.rejectQty = undefined;
            self.model.rejectNote = undefined;
            var popup = angular.element('#qtyaccepatReject');
            popup.modal('hide');
            $scope.calculateAmtForAllItems();
            if($scope.approvedQtyItemListFlag==true)
      	  {
      	  sumOtherCharges(self.model.purchaseOrders.otherCharges);
      	  }
            $scope.calculateReceivedQty(item);
          }
        });
      }
    }
    $scope.calculateOtherChargesAmountForItem = function () {
    	self.model.purchaseOrders.netAmount=0;
    	angular.forEach(self.model.purchaseOrders.items, function(val){
    		if(val.approvedQuantity!=undefined && val.approvedQuantity!=null && val.approvedQuantity>0)
    			{
    			self.model.purchaseOrders.netAmount=self.model.purchaseOrders.netAmount+parseFloat(val.netAmount);
    			}
    	})
        angular.forEach(self.model.purchaseOrders.items, function(val){
        	if(val.approvedQuantity!=undefined && val.approvedQuantity!=null && val.approvedQuantity>0)
			{
          if(val.netAmount){
            self.model.otherChargesPer = ((val.netAmount * 100) / (self.model.purchaseOrders.netAmount)); 
            self.model.calculateOtherCharges = (self.model.purchaseOrders.totalOtherAmount * (self.model.otherChargesPer / 100));
            val.totalOtherAmount = self.model.calculateOtherCharges;
          }
			}
        	else
        		{
        		val.totalOtherAmount=0;
        		val.netAmount=0;
        		}
        });
      }
    $scope.calculateAmtForAllItems = function () {
      $scope.amountround1 = 0;
      if(!self.model.purchaseOrders.discount){
        self.model.purchaseOrders.discount = 0;
      }
      self.model.purchaseOrders.totalAmount = 0;
      self.model.purchaseOrders.totalTaxAmount = 0;
      self.model.purchaseOrders.totalDiscount = 0;
      self.model.purchaseOrders.netAmount = 0;
      self.model.purchaseOrders.roundOffAmount = 0;
      self.model.purchaseOrders.totalOtherAmount=0;
      angular.forEach(self.model.purchaseOrders.items, function(item){
        // var discount = parseFloat(self.model.purchaseOrders.totalDiscount);
        // var discountPercent = ((isNaN(discount) ? 0 :discount) / self.model.totalAmount);
        // item.totalDiscount = item.poAmount * discountPercent;
//    	  if(item.status == 'Approved' && self.model.purchaseOrders.approvalStatus=='Partially Approved')
//    		  {
//    		  item.isSelected=false;
//    		  }
        if(item.isSelected == true){
          if(item.approvedQuantity != undefined && item.approvedQuantity != null){
            item.poAmount = item.approvedQuantity * item.cop;
          }
          else{

            item.poAmount = item.poQuantity * item.cop;
          }
          if(item.poAmount==undefined || item.poAmount==null || item.poAmount==0)
        		  {
        	      		$scope.approvedQtyItemListFlag=true;
        		  }
          if(isNaN(item.totalOtherAmount) || item.totalOtherAmount == undefined || item.totalOtherAmount == null || item.totalOtherAmount == ""){
        	  item.totalOtherAmount = 0;
          }
          // if(self.model.purchaseOrders.discountTypeId != 2){
          //   item.totalDiscount = item.poAmount * (item.discount/100);
          // }
          // if(self.model.purchaseOrders.discountTypeId == null){
          item.totalDiscount = (item.poAmount * (item.discount/100)).toFixed(2);
          item.totalDiscount = parseFloat(item.totalDiscount);
          // }
          item.taxAmount = (item.poAmount - item.totalDiscount) * (item.taxPercentage/100);
          item.netAmount = (item.poAmount - item.totalDiscount)+(item.taxAmount + item.totalOtherAmount);
          if(item.status != 'Approved' && (self.model.purchaseOrders.approvalStatus=='Partially Approved' || self.model.purchaseOrders.approvalStatus=='Pending Approval'))
		  {
		  
          self.model.purchaseOrders.totalAmount += item.poAmount;
          self.model.purchaseOrders.totalTaxAmount += item.taxAmount;
          self.model.purchaseOrders.totalDiscount += item.totalDiscount;
          // if(self.model.purchaseOrders.totalDiscount != undefined && self.model.purchaseOrders.totalDiscount != null){
          //   self.model.purchaseOrders.totalDiscount = self.model.purchaseOrders.totalDiscount.toFixed(2);
          //   self.model.purchaseOrders.totalDiscount = parseFloat(self.model.purchaseOrders.totalDiscount);
          // }
          if(item.selectedOtherCharges != undefined && item.selectedOtherCharges != null && item.selectedOtherCharges.length != 0 &&(item.totalOtherAmount==undefined || item.totalOtherAmount==null || item.totalOtherAmount==0) ){
            angular.forEach(item.selectedOtherCharges, function(otherC){
              self.model.purchaseOrders.totalOtherAmount += parseFloat(otherC.amount);
            })
          }
          
          if((item.approvedQuantity==null || item.approvedQuantity==undefined || item.approvedQuantity==0) && ( item.poQuantity==undefined || item.poQuantity==null || item.poQuantity==0))
          {
        	  item.totalOtherAmount=0;
          }
          self.model.purchaseOrders.totalOtherAmount=self.model.purchaseOrders.totalOtherAmount+item.totalOtherAmount;
          $scope.amountround1 = $scope.amountround1 + ((item.poAmount - item.totalDiscount) + item.taxAmount + item.totalOtherAmount);
          $scope.amountround = $scope.amountround1.toFixed();
          $scope.amount1 = $scope.amountround1.toFixed(2);
          $scope.roundOff1 = parseInt($scope.amountround) - $scope.amount1;
          if($scope.roundOff1 < 0.50){
            self.model.purchaseOrders.netAmount = $scope.amountround;
            self.model.purchaseOrders.roundOffAmount = parseInt($scope.amountround) - $scope.amount1;
            self.model.purchaseOrders.roundOffAmount = self.model.purchaseOrders.roundOffAmount.toFixed(2);
            $scope.selectedCount = 0;
            angular.forEach(self.model.purchaseOrders.items, function(d){
              if(d.isSelected == true){
                $scope.selectedCount = $scope.selectedCount + 1;
              }
            })
            if(self.model.roundOffCHECK == 0 && self.model.purchaseOrders.items.length == $scope.selectedCount){
            	if(self.model.purchaseOrders.roundOffAmount != 0){
            	   self.model.purchaseOrders.netAmount=$scope.amountround;
            	   if(self.model.purchaseOrders.approvalStatus!="Approved")
            		   {
            		   if(parseFloat(self.model.purchaseOrders.roundOffAmount)>0.00)
        			   {
        			   self.model.purchaseOrders.roundOffAmount=self.model.purchaseOrders.roundOffAmount;
        			   }
        		   else
        			   {
        			   self.model.purchaseOrders.roundOffAmount = 0;
        			   }
            		   }
            	}
            }
          }
          if($scope.roundOff1 >= 0.50){
            self.model.purchaseOrders.netAmount = $scope.amountround;
            self.model.purchaseOrders.roundOffAmount = self.model.purchaseOrders.netAmount - $scope.amount1;
            self.model.purchaseOrders.roundOffAmount = self.model.purchaseOrders.roundOffAmount.toFixed(2);
            $scope.selectedCount = 0;
            angular.forEach(self.model.purchaseOrders.items, function(d){
              if(d.isSelected == true){
                $scope.selectedCount = $scope.selectedCount + 1;
              }
            })
            if(self.model.roundOffCHECK == 0 && self.model.purchaseOrders.items.length == $scope.selectedCount){
            	if(self.model.purchaseOrders.roundOffAmount != 0){
            	   self.model.purchaseOrders.netAmount=self.model.purchaseOrders.netAmount;
            	   if(self.model.purchaseOrders.approvalStatus!="Approved")
        		   {
            		   if(parseFloat(self.model.purchaseOrders.roundOffAmount)>0.00)
            			   {
            			   self.model.purchaseOrders.roundOffAmount=self.model.purchaseOrders.roundOffAmount;
            			   }
            		   else
            			   {
            			   self.model.purchaseOrders.roundOffAmount = 0;
            			   }
        		   }
            	}
            }
          }
        }
          else if((item.status == 'Closed' && self.model.purchaseOrders.approvalStatus=='Approved') || (item.status == 'Approved' && self.model.purchaseOrders.approvalStatus!='Partially Approved'))
		  {
    		  
              self.model.purchaseOrders.totalAmount += item.poAmount;
              self.model.purchaseOrders.totalTaxAmount += item.taxAmount;
              self.model.purchaseOrders.totalDiscount += item.totalDiscount;
              // if(self.model.purchaseOrders.totalDiscount != undefined && self.model.purchaseOrders.totalDiscount != null){
              //   self.model.purchaseOrders.totalDiscount = self.model.purchaseOrders.totalDiscount.toFixed(2);
              //   self.model.purchaseOrders.totalDiscount = parseFloat(self.model.purchaseOrders.totalDiscount);
              // }
              if(item.selectedOtherCharges != undefined && item.selectedOtherCharges != null && item.selectedOtherCharges.length != 0 &&(item.totalOtherAmount==undefined || item.totalOtherAmount==null || item.totalOtherAmount==0) ){
                angular.forEach(item.selectedOtherCharges, function(otherC){
                  self.model.purchaseOrders.totalOtherAmount += parseFloat(otherC.amount);
                })
              }
              
              if(item.approvedQuantity==undefined || item.approvedQuantity==0)
              {
            	  item.totalOtherAmount=0;
              }
              self.model.purchaseOrders.totalOtherAmount=self.model.purchaseOrders.totalOtherAmount+item.totalOtherAmount;
              $scope.amountround1 = $scope.amountround1 + ((item.poAmount - item.totalDiscount) + item.taxAmount + item.totalOtherAmount);
              $scope.amountround = $scope.amountround1.toFixed();
              $scope.amount1 = $scope.amountround1.toFixed(2);
              $scope.roundOff1 = parseInt($scope.amountround) - $scope.amount1;
              if($scope.roundOff1 < 0.50){
                self.model.purchaseOrders.netAmount = $scope.amountround;
                self.model.purchaseOrders.roundOffAmount = parseInt($scope.amountround) - $scope.amount1;
                self.model.purchaseOrders.roundOffAmount = self.model.purchaseOrders.roundOffAmount.toFixed(2);
                $scope.selectedCount = 0;
                angular.forEach(self.model.purchaseOrders.items, function(d){
                  if(d.isSelected == true){
                    $scope.selectedCount = $scope.selectedCount + 1;
                  }
                })
                if(self.model.roundOffCHECK == 0 && self.model.purchaseOrders.items.length == $scope.selectedCount){
                	if(self.model.purchaseOrders.roundOffAmount != 0){
                	   self.model.purchaseOrders.netAmount=$scope.amountround;
                	   if(self.model.purchaseOrders.approvalStatus!="Approved")
                		   {
                		   if(parseFloat(self.model.purchaseOrders.roundOffAmount)>0.00)
            			   {
            			   self.model.purchaseOrders.roundOffAmount=self.model.purchaseOrders.roundOffAmount;
            			   }
            		   else
            			   {
            			   self.model.purchaseOrders.roundOffAmount = 0;
            			   }
                		   }
                	}
                }
              }
              if($scope.roundOff1 >= 0.50){
                self.model.purchaseOrders.netAmount = $scope.amountround;
                self.model.purchaseOrders.roundOffAmount = self.model.purchaseOrders.netAmount - $scope.amount1;
                self.model.purchaseOrders.roundOffAmount = self.model.purchaseOrders.roundOffAmount.toFixed(2);
                $scope.selectedCount = 0;
                angular.forEach(self.model.purchaseOrders.items, function(d){
                  if(d.isSelected == true){
                    $scope.selectedCount = $scope.selectedCount + 1;
                  }
                })
                if(self.model.roundOffCHECK == 0 && self.model.purchaseOrders.items.length == $scope.selectedCount){
                	if(self.model.purchaseOrders.roundOffAmount != 0){
                	   self.model.purchaseOrders.netAmount=self.model.purchaseOrders.netAmount;
                	   if(self.model.purchaseOrders.approvalStatus!="Approved")
            		   {
                		   if(parseFloat(self.model.purchaseOrders.roundOffAmount)>0.00)
                			   {
                			   self.model.purchaseOrders.roundOffAmount=self.model.purchaseOrders.roundOffAmount;
                			   }
                		   else
                			   {
                			   self.model.purchaseOrders.roundOffAmount = 0;
                			   }
            		   }
                	}
                }
              }
            }
        }
        else{
          item.poAmount = 0;
          item.totalDiscount = 0;
          item.taxAmount = 0;
          item.netAmount = 0;
        }
      });
//      if((self.model.purchaseOrders.totalOtherAmount==undefined ||self.model.purchaseOrders.totalOtherAmount==null || self.model.purchaseOrders.totalOtherAmount==0)
//    		  && (self.model.purchaseOrders.otherCharges!=undefined && self.model.purchaseOrders.otherCharges!=null && self.model.purchaseOrders.otherCharges.length>0) )
//    	  {
    	  self.model.purchaseOrders.totalOtherAmount=0;
      angular.forEach(self.model.purchaseOrders.otherCharges, function (value, key) {
    	  self.model.purchaseOrders.totalOtherAmount = self.model.purchaseOrders.totalOtherAmount + parseFloat(value.amount);
      })
      self.model.purchaseOrders.totalOtherAmount=self.model.purchaseOrders.totalOtherAmount.toFixed();
      if(self.model.purchaseOrders.netAmount==undefined || self.model.purchaseOrders.netAmount==null || self.model.purchaseOrders.netAmount==0)
    	  {
    	  self.model.purchaseOrders.totalOtherAmount=0.00;
    	  }
      self.model.purchaseOrders.totalNetAmount=parseFloat(self.model.purchaseOrders.netAmount!=undefined && self.model.purchaseOrders.netAmount!=null && self.model.purchaseOrders.netAmount>0 ? self.model.purchaseOrders.netAmount :self.model.purchaseOrders.totalNetAmount);
      self.model.purchaseOrders.netAmount=parseFloat(self.model.purchaseOrders.netAmount!=undefined && self.model.purchaseOrders.netAmount!=null && self.model.purchaseOrders.netAmount>0 ? self.model.purchaseOrders.netAmount :self.model.purchaseOrders.totalNetAmount);
      if(self.model.purchaseOrders.totalAmount==undefined || self.model.purchaseOrders.totalAmount==null || self.model.purchaseOrders.totalAmount==0 )
    	  {
    	  self.model.purchaseOrders.totalNetAmount=0;
    	  self.model.purchaseOrders.netAmount=0;
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
        	  $state.go('POApproval');
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
    	var count=0;
    	self.model.purchaseOrders.items.forEach(function (item) {
		    if(item.isSelected)
        {
			    count++;
			  }
	    });
	    if(count!=self.model.purchaseOrders.items.length)
  	  {
  	    growl.error("Please accept / reject all items");
  	    return false;
  	  }
      if (ValidateApproval()) {
        var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER_APPROVAL.SAVE_API + commonDetailService.getDataId();

        var data = {
          "items": [],
          "paymentTerm": self.model.paymentTerm,
          "remark": self.model.remark,
          "otherCharges": [],
          "organizationId": $scope.organizationId,
          "unitId": $scope.unitId,
          "userId": $scope.userId,
          "purchaseType":self.model.purchaseOrders.purchaseType,
          "roundOffAmount": self.model.purchaseOrders.roundOffAmount,
          "totalNetAmount": self.model.purchaseOrders.netAmount
        }

        $rootScope.startLoader();
        data.otherCharges = self.model.purchaseOrders.otherCharges;
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
                "purchaseOrderStaged": approvedStageQuantity,
                "poUomTypeId":item.selectedUom.uomTypeId,
                "totalOtherAmount":item.totalOtherAmount,
                "totalAmount":item.totalAmount,
                "totalDiscount":item.totalDiscount,
                "taxAmount":item.taxAmount,
                "netAmount":item.netAmount
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
                data.statusId = setStatus('Approved');
                data.approvalStatusId = setStatus('Partially Approved');
              } else if(statuses[0] == 5) {
                data.statusId = setStatus('Approved');
                data.approvalStatusId = setStatus('Partially Approved');
              } else if(statuses[0] == 7) {
                data.statusId = setStatus('Approved');
                data.approvalStatusId = setStatus('Partially Approved');
              } else {
                data.statusId = setStatus('Approved');
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
                "purchaseOrderStaged": approvedStageQuantity,
                "poUomTypeId":item.selectedUom.uomTypeId,
                "totalOtherAmount":item.totalOtherAmount,
                "totalAmount":item.totalAmount,
                "totalDiscount":item.totalDiscount,
                "taxAmount":item.taxAmount,
                "netAmount":item.netAmount
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
                data.statusId = setStatus('Approved');
                // changed
                data.approvalStatusId = setStatus('Approved');
              } else if(statuses[0] == 5) {
                data.statusId = setStatus('Approved');
                data.approvalStatusId = setStatus('Approved');
              } else if(statuses[0] == 7) {
                // changed
                data.statusId = setStatus('Approved');
                data.approvalStatusId = setStatus('Approved');
              } else {
                data.statusId = setStatus('Approved');
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
                "approvedQuantity": parseInt(x.quantity)
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
              "purchaseOrderStaged": approvedStageQuantity,
              "poUomTypeId":item.uom == null ? null : item.selectedUom.uomTypeId,
              "poUomUnitId":item.uom == null ? null : item.selectedUom.uomUnitId,	  
              //"poUomTypeId":item.selectedUom.uomTypeId,
              "totalOtherAmount":item.totalOtherAmount,
              "totalAmount":item.totalAmount,
              "totalDiscount":item.totalDiscount,
              "taxAmount":item.taxAmount,
              "netAmount":item.netAmount
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
              data.statusId = setStatus('Approved');
              data.approvalStatusId = setStatus('Partially Approved');
            } else if(statuses[0] == 5) {
              data.statusId = setStatus('Approved');
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
        var flag=false;
        angular.forEach(self.model.purchaseOrders.items, function (status) {
        	if(status.statusId!=5 && status.statusId!=7)
        		{
        		flag=true;
        		}
        })
        if(flag==true)
        	{
        	data.approvalStatusId = setStatus('Partially Approved');
        	}
        else
        	{
        	data.approvalStatusId = setStatus('Approved');
        	}
        angular.forEach(data.items, function (status) {
        	if(status.rejectedQuantity!=undefined && status.rejectedQuantity!=null && status.rejectedQuantity>0)
        		{
        		var rejectedQtyToSub=status.rejectedQuantity;
        		for(var i=status.purchaseOrderStaged.length-1;i>=0;i--)
        			{
        				if(rejectedQtyToSub>=status.purchaseOrderStaged[i].approvedQuantity)
        					{
        					rejectedQtyToSub=rejectedQtyToSub-status.purchaseOrderStaged[i].approvedQuantity;
        					status.purchaseOrderStaged[i].approvedQuantity=0;
        					}
        				else
        					{
        					status.purchaseOrderStaged[i].approvedQuantity=status.purchaseOrderStaged[i].approvedQuantity-rejectedQtyToSub;
        					rejectedQtyToSub=0;
        					}
        			}
        		}
        });
        var otherChargesListToSave=[];
        angular.forEach(self.model.purchaseOrders.otherCharges, function (item) {
        	if(item.amount!=undefined && item.amount!=null && item.amount>0)
        		{
        		otherChargesListToSave.push(item);
        		}
        })
        data.otherCharges = otherChargesListToSave;
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
    	
    	   
    	   /*   if(self.model.purchaseOrders.items.length == 1){
    	  
    	    	  self.model.purchaseOrders.items.forEach(function (item) {
    	    		  item.quantityChanged = false;
		    	      	if(item.poQuantityOld == item.poQuantity){
		    	      		item.quantityChanged = true;
		    	      	}
    	    	  });
    	      }*/
    	      
      if (self.model.selectAllRows) {
        self.model.SelectedItems = [];
        self.model.purchaseOrders.items.forEach(function (item) {
        	// CancelRejection(item);
//        	if(item.quantityChanged ==  false){
        		   if (!isDisable(item)) {
        	            item.isSelected = true;
        	            item.isDisabled = false;
        	            item.approvedQuantity = item.poQuantity;
        	            item.bonusApprovedQuantity = item.bonusQuantity;
        	            item.statusId = 5;
        	            item.purchaseOrderStagedDto.forEach(function(x){
        	              x.approvedQuantity = x.quantity;
        	            });
        	            self.model.SelectedItems.push(item);
//        	}
       
          }
        });
        $scope.calculateAmtForAllItems();
      } else {
        self.model.SelectedItems = [];
        self.model.purchaseOrders.items.forEach(function (item) {
        	// CancelRejection(item);
//        	if(item.quantityChanged ==  false){
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
//        	}
       
        });
        $scope.calculateAmtForAllItems();
      }
    }

    var SelectItem = function (item) {
      var items = self.model.SelectedItems;
      
    /*  var quantityChanged = false;
      if(self.model.purchaseOrders.items.length == 1){
      	if(item.poQuantityOld == item.poQuantity){
      		var quantityChanged = true;
      	}
      }
      */
      if (item.isSelected) {
//    	  if(quantityChanged == false){
          item.isDisabled = false;
          item.approvedQuantity = item.poQuantity;
          item.fillRejectQtyFlag = true;
          item.rejectQuantity = 0;
          item.bonusApprovedQuantity = item.bonusQuantity;
          item.purchaseOrderStagedDto.forEach(function(value){
            value.approvedQuantity = value.quantity;
          });
          item.statusId = 5;
          items.push(item); 
          if(items.length!=self.model.purchaseOrders.items.length)
    	  {
    	    growl.error("Please accept / reject all items");
    	    return false;
    	  }
          $scope.calculateAmtForAllItems();
//        }
      } 
      else 
      {
//        if(quantityChanged == false){
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
          var count=0;
    	    items.forEach(function (item) {
    		    if(item.isSelected)
            {
    			    count++;
    			  }
    	    });
    	    if(count!=self.model.purchaseOrders.items.length)
      	  {
      	    growl.error("Please accept / reject all items");
      	    return false;
      	  }
          $scope.calculateAmtForAllItems();
//        }
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

    var ViewStagedQuantity = function (item) {
      self.model.purchaseOrders.items.find(function(data){
        if (item.id == data.id) {
          self.model.stagedQuantity = data.initialPurchaseOrderStagedDto!=undefined && data.initialPurchaseOrderStagedDto!=null ? data.initialPurchaseOrderStagedDto : data.forApproveStageQty ;
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
          "poUomUnit":x.poUomUnit,
          "actualQuantity": x.actualQuantity,
          "poUomTypeId":x.poUomTypeId,
          "isPrQuantity":item.prQuantity!=undefined && item.prQuantity!=null && item.prQuantity>0 ? true :false
        });
      });
      if(poQty!=undefined && poQty!=null && poQty.length==1 )
      {
    	  poQty[0].approvedQuantity=item.approvedQuantity;
      }
      self.model.approvedStageQuantity = poQty;
      self.model.rejectedStageQuantity = item.rejectQuantity;
      self.model.actualApprovedStageQuantity =item.approvedQuantity;
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

    $scope.roundoffCalculation = function(){
      self.model.purchaseOrders.netAmount = 0;
      angular.forEach(self.model.purchaseOrders.items, function(val){
        self.model.purchaseOrders.netAmount += val.netAmount;
      });
      if(self.model.purchaseOrders.roundOffAmount < 0){
        var roundoff = self.model.purchaseOrders.roundOffAmount.split("-");
        self.model.purchaseOrders.netAmount = self.model.purchaseOrders.netAmount - parseFloat(roundoff[1]);
        self.model.purchaseOrders.netAmount = self.model.purchaseOrders.netAmount.toFixed(2);
      } 
      if(self.model.purchaseOrders.roundOffAmount > 0){
        self.model.purchaseOrders.netAmount = self.model.purchaseOrders.netAmount + parseFloat(self.model.purchaseOrders.roundOffAmount);
        self.model.purchaseOrders.netAmount = self.model.purchaseOrders.netAmount.toFixed(2);
      }
      if(self.model.purchaseOrders.roundOffAmount == 0){
        self.model.purchaseOrders.netAmount = 0;
        angular.forEach(self.model.purchaseOrders.items, function(val){
          self.model.purchaseOrders.netAmount += val.netAmount;
        });
      }
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
        if (selectedItem.approvedQuantity > (selectedItem.prQuantity!=undefined && selectedItem.prQuantity!=null && selectedItem.prQuantity>0 ? selectedItem.prQuantity: selectedItem.poQuantity))
        {
          selectedItem.rejectQuantity = 0;
          selectedItem.isReject = false;
        }
        else
        {
          selectedItem.rejectQuantity = (selectedItem.prQuantity!=undefined && selectedItem.prQuantity!=null && selectedItem.prQuantity>0 ? selectedItem.prQuantity:selectedItem.poQuantity) - selectedItem.approvedQuantity;
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
        CalculateStock(selectedItem, selectedItem.uom, selectedItem.selectedUom);
        self.model.ClosePopup('close', 'ApprovedStagedQuantity');
        $scope.calculateAmtForAllItems();
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
      $state.go('POApproval');
    }

    var DisableEditForOrder = function () {
    	if(self.model.purchaseOrders.approvalStatus=='Approved' || self.model.purchaseOrders.approvalStatus=='Rejected' ){
    		return true;
    	}
    	return false;
    }

    $scope.validateApprovedQty = function(data){
      var isValidItem = true;
      if(data.isPrQuantity==true)
    	  {
    	  if(data.approvedQuantity > (data.quantity!=undefined && data.quantity!=null && data.quantity>0 ? data.quantity : data.actualQuantity)){
    	        growl.error("Approved quantity is greater than PR Quantity");
    	        data.approvedQuantity = '';
    	        isValidItem = false;
    	      }
    	  }
      else
    	  {
    	  if(data.approvedQuantity > (data.quantity!=undefined && data.quantity!=null && data.quantity>0 ? data.quantity : data.actualQuantity)){
    	        growl.error("Approved quantity is greater than PO Quantity");
    	        data.approvedQuantity = '';
    	        isValidItem = false;
    	      }
    	  }
      return isValidItem;
    }

    initializeController();
  }
  
  function config($stateProvider) {
    $stateProvider.state("detailsPOApproval", {
      url: "/detailsPOApproval",
      templateUrl: 'views/procurement/quotation/details-PO-approval.html',
      controller: "DetailsPOApproval.Controller",
      controllerAs: "vm",
      title: 'Purchase Order Approval'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("DetailsPOApproval.Controller", detailsPOApprovalController);
})();