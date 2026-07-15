(function () {
  "use strict";

  function returnableGatepassAppprovalDetailsController($scope,$state, $rootScope, StatusService, rejectReasonService, commonDetailService, $http, $cookies, CONSTANTS, GenericService, growl) {
    var self = this;
    var returnableId;

    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;  
    function initializeController() {
      $rootScope.loginpage = true;
      self.model = {
        SelectedItems: [],
        selectItemList: [],
        selectAllRows: [],
        bonusBatchExpiryData:[],
        bonusSelectedItemBatchExpiryIndex:0,

      };
      StatusService.GetStatus().then(function (status) {
        self.model.statuses = status;
      });
      rejectReasonService.GetReason().then(function (reason) {
        self.model.rejectReasons = reason;
      });
      self.model.fromReturnableGateepassApproval = fromReturnableGateepassApproval;
      self.model.setStatus = setStatus;
      self.model.SelectAllItems = SelectAllItems;
      self.model.SelectItem = SelectItem;
      self.model.isSelected = isSelected;
      self.model.isDisable = isDisable;
      self.model.valueOnChange = valueOnChange;
      self.model.ApprovedQuantity = ApprovedQuantity;
      self.model.RejectionPopup = RejectionPopup;
      self.model.CancelRejection = CancelRejection;
      self.model.ValidateRejection = ValidateRejection;
      self.model.SaveAcceptReject = SaveAcceptReject;
      self.model.ModalDismiss = ModalDismiss;
      self.model.SaveApproval = SaveApproval;
      self.model.ValidateApproval = ValidateApproval;
      self.model.accessoryModal=accessoryModal;
      self.model.ModalDismissClose= ModalDismissClose;
      self.model.bonusBatchExpiryInfoOnClick=bonusBatchExpiryInfoOnClick;
      self.model.bonusBatchExpiryModalOnClick=bonusBatchExpiryModalOnClick;

      fromReturnableGateepassApproval();
    }

    $scope.requestTypeToCheck;
    function fromReturnableGateepassApproval() {
      var id = commonDetailService.getDataId();
      var URI = CONSTANTS.INVENTORY.STORE.RETURNABLE_GATEPASS_APPROVAL.DETAIL_API  + $scope.unitId + '/'+$scope.organizationId + '/' + id;
      var data = {};
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, data).then(
      function (response) {
        self.model.returnableGatepassAppprovalDetailData = response.data;
        $scope.requestTypeToCheck=self.model.returnableGatepassAppprovalDetailData.requestedType;
        self.model.returnableGatepassAppprovalDetailData.items.forEach(function (item) {
          if(item.uomList!=undefined && item.uomList!=null && item.uomList.length != 0){
            item.uomList.find(function(d){
              if(d.uomTypeId == item.uomTypeId){
                  item.uomType = d.uomDetails;
              }
              if(item.bonusUomTypeId == d.uomTypeId){
                item.bonusUom = d.uomDetails;
              }
            });
          }
        	item.grnBonusBatchExpiryData=item.returnableBonusBatchExpiry;
        	var bonusReturnQuantity=0;
      	  var bonusReturnedQuantity=0;
      	  angular.forEach(item.grnBonusBatchExpiryData, function(bonus){
      		  bonusReturnQuantity=bonusReturnQuantity+parseInt(bonus.returnQuantity);
      		  bonusReturnedQuantity=bonusReturnedQuantity+parseInt(bonus.returnedQuantity);
            bonus.bonusUom = item.bonusUom;
            bonus.bonusQuantityLease = bonus.bonusQuantity;
            if(item.bonusUomTypeId == 1){
              bonus.bonusQuantity = (bonus.bonusQuantity / (item.uomList[0].conversion * item.uomList[1].conversion * item.uomList[2].conversion)); 
            }
            else if(item.bonusUomTypeId == 2){
                bonus.bonusQuantity = (bonus.bonusQuantity / (item.uomList[1].conversion * item.uomList[2].conversion));
            }
            else{
                 if(item.uomList!=undefined && item.uomList!=null && item.uomList.length != 0){
                  bonus.bonusQuantity = (bonus.bonusQuantity / (item.uomList[2].conversion));
                 }
            }
            if(bonus.returnedQuantity != undefined && bonus.returnedQuantity != null && bonus.returnedQuantity != 0){
              if(item.bonusUomTypeId == 1){
                bonus.returnedQuantity = (bonus.returnedQuantity / (item.uomList[0].conversion * item.uomList[1].conversion * item.uomList[2].conversion)); 
                bonusReturnedQuantity = (bonusReturnedQuantity / (item.uomList[0].conversion * item.uomList[1].conversion * item.uomList[2].conversion)); 
              }
              else if(item.bonusUomTypeId == 2){
                bonus.returnedQuantity = (bonus.returnedQuantity / (item.uomList[1].conversion * item.uomList[2].conversion));
                bonusReturnedQuantity = (bonusReturnedQuantity / (item.uomList[1].conversion * item.uomList[2].conversion));
              }
              else{
                bonus.returnedQuantity = (bonus.returnedQuantity / (item.uomList[2].conversion));
                bonusReturnedQuantity = (bonusReturnedQuantity / (item.uomList[2].conversion));
              }
              if(bonusReturnQuantity==undefined || bonusReturnQuantity==null || bonusReturnQuantity == 0){
            	  if(item.statusId == 6){
            		  bonusReturnQuantity = parseInt(bonus.bonusQuantity) - bonusReturnedQuantity;
                      bonus.returnQuantity = parseInt(bonus.bonusQuantity) - bonusReturnedQuantity;  
            	  }
              }
              
            }
      	  })
      	  item.bonusReturnedQuantity=bonusReturnedQuantity!=undefined && bonusReturnedQuantity!=null ? bonusReturnedQuantity:0;
      	  item.bonusReturnQuantity=bonusReturnQuantity!=undefined && bonusReturnQuantity!=null ? bonusReturnQuantity:0;
          item.isDisabled = true;
          if(item.rejectedQuantity != null && item.rejectedQuantity >0){
        	  item.isReject = true;
          }
          if(item.actualDiscountPercentage == undefined || item.actualDiscountPercentage == null || isNaN(item.actualDiscountPercentage)){
            item.actualDiscountPercentage = 0;
          }
          if(item.uomList!=undefined && item.uomList!=null && item.uomList.length != 0){
            if(item.approvedQuantity != undefined && item.approvedQuantity != null && item.approvedQuantity != 0){
              if(item.isThreeUOMApplicable == false){
                if(item.uomTypeId ==1){
                  item.receivedQtyCon = (item.approvedQuantity +"*"+ item.uomList[0].conversion);
                  item.receivedQtyCalculation = (item.approvedQuantity * item.uomList[0].conversion);
                }
                else if(item.uomTypeId == 2){
                    
                }
                else{
                  item.receivedQtyCon = (item.approvedQuantity);
                  item.receivedQtyCalculation = (item.approvedQuantity);
                }
              }
              else{
                if(item.uomTypeId ==1){
                  item.receivedQtyCon = (item.approvedQuantity +"*"+ item.uomList[0].conversion +"*"+ item.uomList[1].conversion);
                  item.receivedQtyCalculation = (item.approvedQuantity * item.uomList[0].conversion * item.uomList[1].conversion);
                }
                else if(item.uomTypeId == 2){
                  item.receivedQtyCon = (item.approvedQuantity +"*"+ item.uomList[1].conversion);
                  item.receivedQtyCalculation = (item.approvedQuantity * item.uomList[1].conversion);
                }
                else{
                  item.receivedQtyCon = (item.approvedQuantity);
                  item.receivedQtyCalculation = (item.approvedQuantity);
                }
              }
            }
            else{
              if(item.isThreeUOMApplicable == false){
                if(item.uomTypeId ==1){
                  item.receivedQtyCon = (item.returnQuantity +"*"+ item.uomList[0].conversion);
                  item.receivedQtyCalculation = (item.returnQuantity * item.uomList[0].conversion);
                }
                else if(item.uomTypeId == 2){
                    
                }
                else{
                  item.receivedQtyCon = (item.returnQuantity);
                  item.receivedQtyCalculation = (item.returnQuantity);
                }
              }
              else{
                if(item.uomTypeId ==1){
                  item.receivedQtyCon = (item.returnQuantity +"*"+ item.uomList[0].conversion +"*"+ item.uomList[1].conversion);
                  item.receivedQtyCalculation = (item.returnQuantity * item.uomList[0].conversion * item.uomList[1].conversion);
                }
                else if(item.uomTypeId == 2){
                  item.receivedQtyCon = (item.returnQuantity +"*"+ item.uomList[1].conversion);
                  item.receivedQtyCalculation = (item.returnQuantity * item.uomList[1].conversion);
                }
                else{
                  item.receivedQtyCon = (item.returnQuantity);
                  item.receivedQtyCalculation = (item.returnQuantity);
                }
              }
            }
          }
        });
        $scope.calculateRateConversion();
        $rootScope.stopLoader();
      },
      function (err) {
        $rootScope.stopLoader();
        growl.error('Error');
      });
    }
    $scope.closeBonusBatchExpiry =function()
    {
        self.model.bonusBatchExpiryData=[];
    }
    $scope.lessBonusQuantity = function (item) {
        var index = self.model.bonusBatchExpiryData.indexOf(item);
        self.model.bonusBatchExpiryData.splice(index, 1);
      }
    var bonusBatchExpiryInfoOnClick = function (batchItem, index) {
        self.model.bonusSelectedItemBatchExpiryIndex = index;
        var sumOfBonusReceivedQuantity=0;
        if(batchItem.grnBonusBatchExpiryData!=undefined && batchItem.grnBonusBatchExpiryData!=null && batchItem.grnBonusBatchExpiryData.length>0)
            {
        angular.forEach(batchItem.grnBonusBatchExpiryData, function (grnBonusBatchExpiryCount) {
            sumOfBonusReceivedQuantity=sumOfBonusReceivedQuantity+parseInt(grnBonusBatchExpiryCount.bonusQuantity);
            self.model.bonusBatchExpiryData.push({
                returnableBonusBatchExpiryId:grnBonusBatchExpiryCount.returnableBonusBatchExpiryId,
                batchNo: grnBonusBatchExpiryCount.batchNo,
                expiryDate: grnBonusBatchExpiryCount.expiryDate,
                batchId:grnBonusBatchExpiryCount.batchId,
                bonusQuantity: grnBonusBatchExpiryCount.bonusQuantity,
                bonusReturnedQuantity: grnBonusBatchExpiryCount.returnedQuantity,
                bonusReturnQuantity: grnBonusBatchExpiryCount.returnQuantity,
                bonusUom: grnBonusBatchExpiryCount.bonusUom,
                bonusQuantityLease: grnBonusBatchExpiryCount.bonusQuantityLease
            });
          });
        batchItem.bonusReceivedQuantity=sumOfBonusReceivedQuantity;
            }
        else
            {
            sumOfBonusReceivedQuantity=0;
            self.model.bonusBatchExpiryData.push({
                returnableBonusBatchExpiryId:null,
                batchId:null,
                batchNo: "",
                expiryDate: "",
                bonusQuantity: "",
                bonusReturnedQuantity: "",
                bonusReturnQuantity: "",
            });
            }
        
    }

    $scope.validate = function () {
      var isValid = true;
      angular.forEach(self.model.bonusBatchExpiryData, function(d){
        if(d.bonusReturnQuantity == undefined || d.bonusReturnQuantity == null || d.bonusReturnQuantity == 0){
          growl.error("Please enter return quantity..!");
          isValid = false;
        }
      });
      return isValid;
    }

    var bonusBatchExpiryModalOnClick = function () {
      if($scope.validate()){
        self.model.returnableGatepassAppprovalDetailData.items[self.model.bonusSelectedItemBatchExpiryIndex].grnBonusBatchExpiryData = [];
        var modal_list = self.model.bonusBatchExpiryData;
        modal_list.forEach(function (modal, index) {
        	self.model.returnableGatepassAppprovalDetailData.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusReturnQuantity=0
        	self.model.returnableGatepassAppprovalDetailData.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusReturnQuantity=
        	self.model.returnableGatepassAppprovalDetailData.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusReturnQuantity+parseInt(modal.bonusReturnQuantity);
          self.model.returnableGatepassAppprovalDetailData.items[self.model.bonusSelectedItemBatchExpiryIndex].grnBonusBatchExpiryData.push({
            returnableBonusBatchExpiryId:modal.returnableBonusBatchExpiryId!=null ? modal.returnableBonusBatchExpiryId :null ,
            batchNo: modal.batchNo,
            expiryDate: modal.expiryDate,
            bonusQuantity: modal.bonusQuantity,
            batchId:modal.batchId,
            returnedQuantity: modal.bonusReturnedQuantity,
            returnQuantity: modal.bonusReturnQuantity,
            bonusQuantityLease: modal.bonusQuantityLease,
            bonusReturnQuantity: modal.bonusReturnQuantity,
            bonusUom:modal.bonusUom
          });
        });
        self.model.bonusBatchExpiryData=[];
        $('#bouns_selectBatchExpiry_modal').modal('hide');
      }
    }

    $scope.calculateRateConversion = function () {
      angular.forEach(self.model.returnableGatepassAppprovalDetailData.items, function(item){
        if(item.uomList!=undefined && item.uomList!=null && item.uomList.length != 0){
        	var uomTypeToCompare=item.uomType.split(" - ")[0];
          if (uomTypeToCompare == "P") {
            item.cop = parseFloat(item.cop * (item.uomList[0].conversion * item.uomList[1].conversion * item.uomList[2].conversion)).toFixed(2);
          } else if (uomTypeToCompare == "S") {
            item.cop = parseFloat(item.cop * (item.uomList[1].conversion * item.uomList[2].conversion)).toFixed(2);
          } else if (uomTypeToCompare == "D") {
            item.cop = parseFloat(item.cop * (item.uomList[2].conversion)).toFixed(2);
          }
        }
      });
    }
 
    function setStatus(x) {

      var status = self.model.statuses.find(function (obj) {
        return obj.status == x;
      });

      return status.id;
    }

    var accessoryModal= function(){
      $("#viewDetailsAccessoriesModal").modal()
      self.model.accessoryList = [];
      self.model.accessoryList = asset.accessoriesResponse;
  }

    var isDisable = function (returnable) {
      if (returnable.statusId != 10 || returnable.statusId == 7 || returnable.statusId == 9 || returnable.statusId == 5) {
        return true;
      }
      return false;
    }

    var SelectAllItems = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedRequestIds = [];
        self.model.returnableGatepassAppprovalDetailData.items.forEach(function (returnable) {
          if (returnable.isDisabled == true && returnable.statusId != 6) {
            returnable.isSelected = true;
            self.model.SelectedRequestIds.push(returnable.id);
            returnable.isDisabled = false;
            self.model.SelectedItems.push(returnable);
          }
        });
      } else {
        self.model.SelectedRequestIds = [];
        self.model.SelectedItems = [];
        self.model.returnableGatepassAppprovalDetailData.items.forEach(function (returnable) {
          returnable.isSelected = false;
          returnable.isDisabled = true;
        });
      }
    }

    var SelectItem = function (returnable) {
      var returnables = self.model.SelectedItems;

      if (returnable.isSelected) {
        returnables.push(returnable);
        returnable.isDisabled = false;
        if (self.model.returnableGatepassAppprovalDetailData.items.every(isSelected)) {
          self.model.selectAllRows = true;
        } else {
          self.model.selectAllRows = false;
        }
      } else {
        returnable.isDisabled = true;
        returnable.isReject = false;
        if (self.model.returnableGatepassAppprovalDetailData.items.every(isSelected)) {
          self.model.selectAllRows = true;
        } else {
          self.model.selectAllRows = false;
        }
        var index = returnable.indexOf(returnable);
        if (index > -1) {
          returnables.splice(index, 1);
        }
      }
    }


    var isSelected = function (returnable) {
      return returnable.isSelected;
    }

    function valueOnChange(returnable, isSelectAll) {
      if (isSelectAll)
        self.model.returnableGatepassAppprovalDetailData.items.forEach(function (returnable) {
          if (self.model.selectAllRows){
            returnable.approvedQuantity = returnable.returnQuantity;
          }
          else{
            returnable.approvedQuantity = 0;
            returnable.rejectedQuantity = 0;
          }
        });
      else
        if (returnable.isSelected){
          returnable.approvedQuantity = returnable.returnQuantity;
        }
        else{
          returnable.rejectedQuantity = 0;
          returnable.approvedQuantity = 0;
        }
    }

    var ApprovedQuantity = function (returnable) {
    	if(returnable.approvedQuantity==undefined || returnable.approvedQuantity==null)
		{
			returnable.approvedQuantity=0;
		}
    	if(returnable.availableQuantity<=0)
        {
            growl.error("Available Quantity must be greater than zero");
            returnable.approvedQuantity=0;
            return false;
        }
    	if(returnable.approvedQuantity>returnable.returnQuantity)
    		{
    		growl.error("Approved Quantity must be less than or equal to return Quantity");
    		return false;
            
    		}
    	if($scope.requestTypeToCheck=='Rejected Against GRN')
    		{
    		  if(returnable.returnQuantity<returnable.approvedQuantity)
    			  {
    			  growl.error("Approved Quantity must be less than or equal to Available for returned Quantity");
    	            
    	            return false;
    			  }
    		}
    	else
    		{
    		 if(returnable.availableQuantity<returnable.approvedQuantity)
			  {
			  growl.error("Approved Quantity must be less than or equal to Available for returned Quantity");
	            
	            return false;
			  }
    		}
      if (returnable.approvedQuantity > returnable.returnQuantity) {
        returnable.rejectedQuantity = 0;
        self.model.rejectQty = undefined;
        returnable.isReject = false;
        returnable.statusId = setStatus('Approved'); // Approved.
        RejectionPopup(returnable);
      } else if (returnable.approvedQuantity == 0 || !returnable.returnQuantity) {
        returnable.rejectedQuantity = returnable.returnQuantity;
        self.model.rejectQty = returnable.rejectedQuantity;
        returnable.isReject = true;
        returnable.statusId = setStatus('Rejected'); // Rejected
        RejectionPopup(returnable);
      } else if (returnable.approvedQuantity < returnable.returnQuantity) {
        returnable.rejectedQuantity = returnable.returnQuantity - returnable.approvedQuantity;
        self.model.rejectQty = returnable.rejectedQuantity;
        returnable.isReject = true;
        returnable.statusId = setStatus('Approved'); // Partially Approved
        RejectionPopup(returnable);
      } else if (returnable.approvedQuantity == returnable.returnQuantity) {
        returnable.rejectedQuantity = returnable.returnQuantity - returnable.approvedQuantity;
        self.model.rejectQty = returnable.rejectedQuantity;
        returnable.isReject = false;
        returnable.statusId = setStatus('Approved'); // Approved.
      }
    }

    var ValidateRejection = function () {
      var isValid = true;
      if (!self.model.rejectReason) {
        isValid = false;
        growl.error('Please Select Reason to Reject');
      }

      return isValid;
    }

    var RejectionPopup = function (returnable) {
      self.model.onclose = returnable;
      self.model.approvedQuantityonClose= returnable.returnQuantity;
      self.model.rejectQuantityonClose=0;
      self.model.rejectedQuantity = 0;
      // console.log(self.model.status);
      if (returnable.isReject) {
        returnableId = returnable.id;
        // self.model.rejectQty = item.rejectQuantity;
        self.model.action = 'Open';
        self.model.rejectReason = undefined;
        var popup = angular.element('#qtyaccepatReject');
        popup.modal('show');
        self.model.reasons = self.model.rejectReasons;
        // self.model.rejectReason = self.model.reasons[0];
      } else {
        CancelRejection(returnable);
      }
    }

    var CancelRejection = function (returnable) {
      var prevRequest = self.model.returnableGatepassAppprovalDetailData.items.find(function (prevItem) {
        return prevItem.id == returnable.id;
      });

      if (prevRequest) {
        returnable.rejectedQuantity = 0;
        returnable.approvedQuantity = prevRequest.returnQuantity;
        returnable.rejectNote = prevRequest.rejectNote;
        returnable.rejectReasonId = prevRequest.rejectReasonId;
        returnable.statusId = prevRequest.statusId;
      }
      $scope.calculateReceivedQty(returnable);
    }


    var ModalDismiss = function (popupName) {
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
    }
    var ModalDismissClose = function (popupName) {
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
      self.model.onclose.isReject = false;
      self.model.onclose.approvedQuantity= self.model.approvedQuantityonClose;
      self.model.onclose.rejectedQuantity = self.model.rejectQuantityonClose;

    }


    var SaveAcceptReject = function () {
      if (ValidateRejection()) {
        var rejectItemId = returnableId;
        self.model.SelectedItems.forEach(function (returnable) {
          if (returnable.id == rejectItemId) {
            if (self.model.rejectQty > returnable.returnQuantity) {
              growl.error('Please Enter Valid Quantity');
              return false;
            } else if (self.model.rejectQty == returnable.returnQuantity) {
              returnable.rejectedQuantity = returnable.returnQuantity;
              returnable.approvedQuantity = returnable.returnQuantity - returnable.rejectedQuantity;
              returnable.rejectReasonId = self.model.rejectReasons[0].id;
              returnable.statusId = setStatus('Rejected'); // Rejected
            } else if (self.model.rejectQty == 0 || !self.model.rejectQty) {
              returnable.approvedQuantity = returnable.returnQuantity;
              returnable.rejectedQuantity = 0;
              returnable.rejectReasonId = self.model.rejectReasons[0].id;
              returnable.statusId = setStatus('Pending'); // Approved
              returnable.isReject = false;
            } else {
              returnable.rejectedQuantity = self.model.rejectQty;
              returnable.rejectNote = self.model.rejectNote;
              returnable.approvedQuantity = returnable.returnQuantity - returnable.rejectedQuantity;
              returnable.rejectReasonId = self.model.rejectReasons[0].id;
              returnable.statusId = setStatus('Approved'); // Partially Approved
            }
            ModalDismiss('qtyaccepatReject');
            $scope.calculateReceivedQty(returnable);
            // if(returnable.rejectedQuantity != 0){
            //   self.model.returnableGatepassAppprovalDetailData.totalAmount = returnable.approvedQuantity * returnable.cop;
            //   self.model.returnableGatepassAppprovalDetailData.netAmount =  self.model.returnableGatepassAppprovalDetailData.totalAmount + returnable.totalTaxAmount;
            // }
          }
        });
      }
    }

    $scope.calculateReceivedQty = function (item) {
			if(item.isThreeUOMApplicable == false){
				if(item.uomTypeId == 1){
					item.receivedQtyCon = (item.approvedQuantity +"*"+ item.uomList[0].conversion);
					item.receivedQtyCalculation = (item.approvedQuantity * item.uomList[0].conversion);
				}
				else if(item.uomTypeId == 2){
					
				}
				else{
					item.receivedQtyCon = (item.approvedQuantity);
					item.receivedQtyCalculation = (item.approvedQuantity);
				}
			}
			else{
				if(item.uomTypeId == 1){
					item.receivedQtyCon = (item.approvedQuantity +"*"+ item.uomList[0].conversion+"*"+ item.uomList[1].conversion);
					item.receivedQtyCalculation = (item.approvedQuantity * item.uomList[0].conversion * item.uomList[1].conversion);
				}
				else if(item.uomTypeId == 2){
					item.receivedQtyCon = (item.approvedQuantity +"*"+ item.uomList[1].conversion);
					item.receivedQtyCalculation = (item.approvedQuantity * item.uomList[1].conversion);
				}
				else{
					item.receivedQtyCon = (item.approvedQuantity);
					item.receivedQtyCalculation = (item.approvedQuantity);
				}
			}
		}

    $scope.saveBtnDisabled=false;
    var SaveApproval = function () {
    	$scope.saveBtnDisabled=true;
      if (ValidateApproval()) {
        var URI = CONSTANTS.INVENTORY.STORE.RETURNABLE_GATEPASS_APPROVAL.APPROVE_API + commonDetailService.getDataId();
        var data = {
          "statusId": setStatus('Closed'),
          "approvalStatusId": setStatus('Rejected'),
          "assetTypeId":self.model.returnableGatepassAppprovalDetailData.assetTypeId,
          "items": [],
          "totalAmount": self.model.returnableGatepassAppprovalDetailData.totalAmount,
          "totalTaxAmount": self.model.returnableGatepassAppprovalDetailData.totalTaxAmount,
          "netAmount": self.model.returnableGatepassAppprovalDetailData.netAmount,
          "unitId":$scope.unitId,
          "organizationId":$scope.organizationId,
          "userId":$scope.userId
        }

        $rootScope.startLoader();
        angular.forEach(self.model.SelectedItems, function (item) {
          var array=[];
          angular.forEach(item.grnBonusBatchExpiryData, function (item1, key) {
            if(item.bonusUomTypeId == 1 || item.bonusUomTypeId == 2){
//              item1.returnQuantity = item1.returnQuantity * item.uomList[0].conversion * item.uomList[1].conversion * item.uomList[2].conversion;
            }
            if(item1.returnQuantity!="" && item1.returnQuantity>0)
            {
              array.push({
                returnableBonusBatchExpiryId: item1.returnableBonusBatchExpiryId!=null ? item1.returnableBonusBatchExpiryId :null ,
                batchNo: item1.batchNo,
                expiryDate: item1.expiryDate,
                bonusQuantity: item1.bonusQuantityLease,
                batchId: item1.batchId,
                returnedQuantity: item1.bonusReturnedQuantity != undefined && item1.bonusReturnedQuantity != null ? item1.bonusReturnedQuantity : 0,
                returnQuantity: item1.returnQuantity
              });
            }
          });
        
          item.grnBonusBatchExpiryData=array;

          var newItem = {
            "id": item.id,
            "approvedQuantity": parseInt(item.approvedQuantity) ? item.approvedQuantity : 0,
            "rejectedQuantity": parseInt(item.rejectedQuantity),
            "rejectReasonId": self.model.rejectReasonId ? self.model.rejectReasonId : 1,
            "rejectNote": self.model.rejectNote ? self.model.rejectNote : "default note",
            "statusId": item.status ? item.status : 6,
            "bonusUomTypeId" : item.bonusUomTypeId,
      		"uomTypeId" : item.uomTypeId,
            "returnableBonusBatchExpiry":item.grnBonusBatchExpiryData!=undefined && item.grnBonusBatchExpiryData!=null ? item.grnBonusBatchExpiryData:[]
          }

          if (item.approvedQuantity > 0 && item.approvedQuantity < item.returnQuantity) {
            newItem.statusId = setStatus('Approved');

            /*if (data.approvalStatusId == setStatus('Reject')) {
              data.approvalStatusId = setStatus('Partially Approved');
            }*/
            /* start code for resolved HIS-10190 After Save Returnable Gate Pass Approval Loader keeps Rotating */
            if (data.approvalStatusId == setStatus('Rejected')) {
            	data.approvalStatusId = setStatus('Approved');
            }
            /* end code for resolved HIS-10190 After Save Returnable Gate Pass Approval Loader keeps Rotating */
          }
          else if (item.approvedQuantity >= item.returnQuantity) {
            data.approvalStatusId = setStatus('Approved');

            if (data.approvalStatusId == setStatus('Rejected')) {
              data.approvalStatusId = setStatus('Approved');
            }
          }
          else if (item.rejectedQuantity >= item.returnQuantity) {
            newItem.statusId = setStatus('Rejected');
          }
          else if (item.rejectedQuantity > 0 && item.rejectedQuantity < item.returnQuantity) {
            newItem.statusId = setStatus('Approved');

            /*if (data.approvalStatusId == setStatus('Reject')) {
              data.approvalStatusId = setStatus('Approved');
            }*/
            /* start code for resolved HIS-10190 After Save Returnable Gate Pass Approval Loader keeps Rotating */
            if (data.approvalStatusId == setStatus('Rejected')) {
            	data.approvalStatusId = setStatus('Approved');
            }
            /* end code for resolved HIS-10190 After Save Returnable Gate Pass Approval Loader keeps Rotating */
          }

          $scope.count = 0;
          if(self.model.returnableGatepassAppprovalDetailData.items.length > 1){
            angular.forEach(self.model.returnableGatepassAppprovalDetailData.items, function(d){
              if(d.id == item.id){
                d.statusId = newItem.statusId;
              }
              if(d.statusId == 6 || d.statusId == 5){
                $scope.count++;
              }
            })
  
            if(self.model.returnableGatepassAppprovalDetailData.items.length == $scope.count){
              data.approvalStatusId = setStatus('Approved');
              data.statusId = setStatus('Closed');
            }
            else{
              data.approvalStatusId =6; //Partially Approval
              data.statusId = setStatus('Pending');
            }
          }

          data.items.push(newItem);
        });

        GenericService.serviceAction("PATCH", URI, data).then(
          function (response) {
            $rootScope.stopLoader();
            growl.success(response.data.message);
            NavigateToBack();
          }, function (err) {
            $rootScope.stopLoader();
           // growl.error('Somthing Went Wrong');
            growl.error(err.data.message);
          });
      }
    }

    var ValidateApproval = function () {
      var isValid = true;

      if (!self.model.SelectedItems || self.model.SelectedItems.length == 0) {
        growl.error('Select Items to Update and Save');
        isValid = false;
      }
      angular.forEach(self.model.SelectedItems, function (item) {
  	
if($scope.requestTypeToCheck=='Rejected Against GRN')
	{
	if(item.approvedQuantity>item.returnQuantity)
	{
	growl.error("Approved Quantity must be less than or equal to return Quantity");
	isValid = false;
    
	}
	else if(item.returnQuantity<item.approvedQuantity)
		  {
		  growl.error("Approved Quantity must be less than or equal to Available for returned Quantity");
            
		  isValid = false;
		  }
	}
else
	{
	if(item.approvedQuantity>item.returnQuantity)
	{
	growl.error("Approved Quantity must be less than or equal to return Quantity");
	isValid = false;
    
	}
	else if(item.availableQuantity<item.approvedQuantity)
	  {
	  growl.error("Approved Quantity must be less than or equal to Available for returned Quantity");
        
	  isValid = false;
	  }
	}
      });
      if(!isValid)
      {
      $scope.saveBtnDisabled=false;
      }
      return isValid;
    }

    var NavigateToBack = function () {
      $state.go('returnaleGatepassApproval');
    }

    $scope.checkBonusReturnQty=function(item)
    {
      if(parseInt(item.bonusReturnQuantity)>(parseInt(item.bonusQuantity)-parseInt(item.bonusReturnedQuantity!="" && item.bonusReturnedQuantity!=null && item.bonusReturnedQuantity!=undefined ? item.bonusReturnedQuantity :0)))
      {
        growl.error("Enter Quantity Must Be Less Than Or Equal To Available For Return");
        item.bonusReturnQuantity=0;
      }
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailsreturnaleGatepassApproval", {
      url: "/detailsreturnaleGatepassApproval",
      templateUrl: 'views/PharmacyInventory/Store/details-returnable-gatepass-approval.html',
      controller: "ReturnableGatepassAppprovalDetails.Controller",
      controllerAs: "vm",
      title: 'Returnable Gatepass Approval Master'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("ReturnableGatepassAppprovalDetails.Controller", returnableGatepassAppprovalDetailsController);
})();
