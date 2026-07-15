(function () {
  "use strict";

  function detailsLocalPRApprovalController($rootScope, $scope, $http, $cookies, CONSTANTS, $state, GenericService, commonDetailService, rejectReasonService, StatusService, growl) {
    var self = this;
    var itemId;
    var PrId;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
    function initializeController() {
      $rootScope.loginpage = true;

      self.model = {
        SelectedItems: [],
        purchaseRequests: {
          items: []
        }
      };

      rejectReasonService.GetReason().then(function(reason) {
        self.model.rejectReasons = reason;
      });
      
      StatusService.GetStatus().then(function(status) {
        self.model.statuses = status;
      });

      self.model.NavigateToBack = NavigateToBack;
      self.model.RejectionPopup = RejectionPopup;
      self.model.SelectItem = SelectItem;
      self.model.SelectAllItems = SelectAllItems;
      self.model.SaveApproval = SaveApproval;
      self.model.SaveAcceptReject = SaveAcceptReject;
      self.model.ApprovedQuantity = ApprovedQuantity;
      self.model.ModalDismiss = ModalDismiss;
      self.model.isDisable = isDisable;
      self.model.ValidateSaveButton = ValidateSaveButton;
      self.model.ModalDismissOnClose = ModalDismissOnClose;
      GetPurchaseRequest(commonDetailService.getDataId());
    }

    var ValidateSaveButton = function(){
      var isValid = false;
      if (self.model.purchaseRequests.items) {
        self.model.purchaseRequests.items.find(function(item){
          if(!!item.isSelected){
            isValid = true;
          }
        });

        return isValid;
      }
    }

    var GetPurchaseRequest = function (purchaseId) {
      commonDetailService.setDataId(null);
      PrId = purchaseId;
      var URI = CONSTANTS.PROCUREMENT.PURCHASE.LOCAL_PURCHASE_APPROVAL.DETAILS_API + $scope.unitId + '/'+$scope.organizationId + '/' + purchaseId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.purchaseRequests = response.data;
          self.model.assetTypeId = response.data.assetTypeId;
          self.model.prTypeId = response.data.prTypeId;
          self.model.requestItems = JSON.parse(JSON.stringify(response.data));
          self.model.purchaseRequests.items.forEach(function(item){
            item.isDisabled = true;
            if (!!item.rejectQuantity) {
              item.isReject = true;
            }

            if (self.model.assetTypeId == 1) {
              var uomList = item.uom.find(function(uom){
                return item.uomType == uom.uomType;
              });
              item.uomType = uomList.uomDetails;
              CalculateStock(item, item.uom, uomList);
            } else {
              var uomList = null;
              CalculateStock(item, item.uom, uomList);
            }
          });
          self.model.reasons = self.model.rejectReasons;
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var CalculateStock = function (item, uom, uomList) {
      if (!!uomList) {
        var stockAvailable = item.availableQuantity;
        var maxOrderQuantity = item.maxOrderQuantity || 0;
        var maxStoreQuantity = item.maxStoreQuantity || 0;

        uomList.uomDetails = uomList.uomDetails.split(' ')[0];

        if (uomList.uomDetails == "P") {
          item.newQty = parseInt(stockAvailable / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
          item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
          item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
          if(item.isThreeUOMApplicable == false){
            if(item.approvedQuantity == undefined || item.approvedQuantity == null){
              item.receivedQtyCon=(item.quantity +"*"+ uom[0].conversion);
              item.receivedQtyCalculation = (item.quantity * uom[0].conversion);
              item.uomUnitData = uom[2].uomUnit;
            }
            else{
              item.receivedQtyCon=(item.approvedQuantity +"*"+ uom[0].conversion);
              item.receivedQtyCalculation = (item.approvedQuantity * uom[0].conversion);
              item.uomUnitData = uom[2].uomUnit;
            }
          }
          else{
            if(item.approvedQuantity == undefined || item.approvedQuantity == null){
              item.receivedQtyCon = (item.quantity +"*"+ uom[0].conversion+"*"+ uom[1].conversion);
              item.receivedQtyCalculation = (item.quantity * uom[0].conversion * uom[1].conversion);
              item.uomUnitData = uom[2].uomUnit;
            }
            else{
              item.receivedQtyCon = (item.approvedQuantity +"*"+ uom[0].conversion+"*"+ uom[1].conversion);
              item.receivedQtyCalculation = (item.approvedQuantity * uom[0].conversion * uom[1].conversion);
              item.uomUnitData = uom[2].uomUnit; 
            }
          }
        } else if (uomList.uomDetails == "S") {
          item.newQty = parseInt(stockAvailable / (uom[1].conversion * uom[2].conversion));
          item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[1].conversion * uom[2].conversion));
          item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[1].conversion * uom[2].conversion));
          if(item.isThreeUOMApplicable != false){
            if(item.approvedQuantity == undefined || item.approvedQuantity == null){
              item.receivedQtyCon=(item.quantity +"*"+ uom[1].conversion);
              item.receivedQtyCalculation = (item.quantity * uom[1].conversion);
              item.uomUnitData = uom[2].uomUnit;
            }
            else{
              item.receivedQtyCon=(item.approvedQuantity +"*"+ uom[1].conversion);
              item.receivedQtyCalculation = (item.approvedQuantity * uom[1].conversion);
              item.uomUnitData = uom[2].uomUnit; 
            }
          }
        } else if (uomList.uomDetails == "D") {
          item.newQty = parseInt(stockAvailable / (uom[2].conversion));
          item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[2].conversion));
          item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[2].conversion));
          if(item.approvedQuantity == undefined || item.approvedQuantity == null){
            item.receivedQtyCon=(item.quantity);
            item.receivedQtyCalculation = (item.quantity);
            item.uomUnitData = uom[2].uomUnit;
          }
          else{
            item.receivedQtyCon=(item.approvedQuantity);
            item.receivedQtyCalculation = (item.approvedQuantity);
            item.uomUnitData = uom[2].uomUnit;
          }
        }
      } else {
        item.newQty = item.availableQuantity;
        item.newMaxOrderQuantity = item.maxOrderQuantity || 0;
        item.newMaxStoreQuantity = item.maxStoreQuantity || 0;
      }
    }

    var RejectionPopup = function (item) {
      self.model.itemTrueFalse = item;
      if (item.isReject) {
        itemId = item.id;
        self.model.action = 'Open';
        var popup = angular.element('#qtyaccepatReject');
        popup.modal('show');
        self.model.reasons = self.model.rejectReasons;
        self.model.rejectReason = self.model.reasons[0];
        // self.model.rejectQty = item.rejectQuantity;
        self.model.rejectNote = undefined;
      } else {
        CancelRejection(item);
      }
    }

    var CancelRejection = function (item) {
      var prevRequest = self.model.requestItems.items.find(function(prevItem){
        return prevItem.id == item.id;
      });

      if (prevRequest) {
        item.rejectQuantity = prevRequest.rejectQuantity;
        item.approvedQuantity = prevRequest.quantity;
        item.rejectNote = prevRequest.rejectNote;
        item.rejectReasonId = prevRequest.rejectReasonId;
        // item.statusId = prevRequest.statusId;
        item.statusId = 5;
        $scope.calculateReceivedQty(item);
      }
    }

    var SaveAcceptReject = function () {
      if (ValidateRejection()) {
        var rejectItemId = itemId;
        self.model.SelectedItems.forEach(function(item){
          if (item.id == rejectItemId) {
            if (self.model.rejectQty > item.quantity) {
              growl.error('Rejection quantity is must be less than ' + item.quantity);
              return false;
            } else if (self.model.rejectQty == item.quantity) {
              item.rejectQuantity = item.quantity;
              item.approvedQuantity = item.quantity - item.rejectQuantity;
              item.rejectReasonId = self.model.reasons[0].id;
              item.statusId = setStatus('Rejected');
            } else if (self.model.rejectQty == 0 || !self.model.rejectQty) {
              item.approvedQuantity = item.quantity;
              item.rejectQuantity = 0;
              item.rejectReasonId = self.model.reasons[0].id;
              item.statusId = setStatus('Approved');
            } else {
              item.rejectQuantity = self.model.rejectQty;
              item.rejectNote = self.model.rejectNote;
              item.approvedQuantity = item.quantity - item.rejectQuantity;
              item.rejectReasonId = self.model.reasons[0].id;
              item.statusId = setStatus('Approved');
            }
            self.model.rejectQty = undefined;
            self.model.rejectNote = undefined;
            ModalDismiss('qtyaccepatReject');
            $scope.calculateReceivedQty(item);
          }
        });
      }
    }

    $scope.calculateReceivedQty = function (item) {
      if(item.isThreeUOMApplicable == false){
        if(item.uomTypeId == 1){
          item.receivedQtyCon = (item.approvedQuantity +"*"+ item.uom[0].conversion);
          item.receivedQtyCalculation = (item.approvedQuantity * item.uom[0].conversion);
          item.uomUnitData = item.uom[2].uomUnit;
        }
        else if(item.uomTypeId == 2){
        
        }
        else{
          item.receivedQtyCon = (item.approvedQuantity);
          item.receivedQtyCalculation = (item.approvedQuantity);
          item.uomUnitData = item.uom[2].uomUnit;
        }
      }
      else{
        if(item.uomTypeId == 1){
          item.receivedQtyCon = (item.approvedQuantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
          item.receivedQtyCalculation = (item.approvedQuantity * item.uom[0].conversion * item.uom[1].conversion);
          item.uomUnitData = item.uom[2].uomUnit;
        }
        else if(item.uomTypeId == 2){
          item.receivedQtyCon = (item.approvedQuantity +"*"+ item.uom[1].conversion);
          item.receivedQtyCalculation = (item.approvedQuantity * item.uom[1].conversion);
          item.uomUnitData = item.uom[2].uomUnit;
        }
        else{
          item.receivedQtyCon = (item.approvedQuantity);
          item.receivedQtyCalculation = (item.approvedQuantity);
          item.uomUnitData = item.uom[2].uomUnit;
        }
      }
    }

    var ApprovedQuantity = function (item) {
      if(item.quantity >= item.approvedQuantity){
        if (item.approvedQuantity > item.quantity) {
          item.rejectQuantity = 0;
          self.model.rejectQty = undefined;
          item.isReject = false;
          item.statusId = setStatus('Approved');
        } else if (item.approvedQuantity == 0 || !item.approvedQuantity) {
          item.approvedQuantity = 0
          item.rejectQuantity = item.quantity;
          self.model.rejectQty = item.rejectQuantity;
          item.isReject = true;
          item.rejectReasonId = self.model.reasons[0].id;
          item.statusId = setStatus('Rejected');
          RejectionPopup(item);
        } else if (item.approvedQuantity < item.quantity) {
          item.rejectQuantity = item.quantity - item.approvedQuantity;
          self.model.rejectQty = item.rejectQuantity;
          item.rejectReasonId = self.model.reasons[0].id;
          item.isReject = true;
          item.statusId = setStatus('Approved');
          RejectionPopup(item);
        } else if (item.approvedQuantity == item.quantity) {
          item.rejectQuantity = item.quantity - item.approvedQuantity;
          self.model.rejectQty = item.rejectQuantity;
          item.isReject = false;
          item.statusId = setStatus('Approved');
          RejectionPopup(item);
        }
      }else{
    	   if (item.approvedQuantity == 0 || !item.approvedQuantity) {
    		   item.approvedQuantity = 0
    	          item.rejectQuantity = item.quantity;
    	          self.model.rejectQty = item.rejectQuantity;
    	          item.isReject = true;
    	          item.rejectReasonId = self.model.reasons[0].id;
    	          item.statusId = setStatus('Rejected');
    	          RejectionPopup(item);
          }
    	   if(parseInt(item.approvedQuantity)>item.quantity){
    		   growl.error('Please enter quantity less than PR Quantity');
    	   }
      }
   
      
    }

    var SaveApproval = function () {
      if (ValidateApproval()) {
        var URI = CONSTANTS.PROCUREMENT.PURCHASE.LOCAL_PURCHASE_APPROVAL.SAVE_API + PrId;
        
        var data = {
          "items": [],
          "unitId":$scope.unitId,
          "organizationId":$scope.organizationId,
          "userId":$scope.userId,
          "storeId":$scope.storeId
        }

        $rootScope.startLoader();
        if (self.model.SelectedItems.length < self.model.purchaseRequests.items.length) {
          var flag = self.model.purchaseRequests.items.find(function(x){
            if (x.statusId == 10) {
              return true;
            } else {
              return false;
            }
          });

          if (!!flag) {
            angular.forEach(self.model.SelectedItems, function (item) {
              var newItem = {
                "id": item.id,
                "approvedQuantity": item.approvedQuantity,
                "rejectQuantity": item.rejectQuantity,
                "rejectReasonId": item.rejectReasonId ? item.rejectReasonId : 1,
                "rejectNote": item.rejectNote,
                "statusId": item.statusId
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

              // var statuses = Object.keys(statusHash);
              // data.statusId = setStatus('Pending');
              // data.approvalStatusId = setStatus('Partially Approved');
            });
          } else {
            angular.forEach(self.model.SelectedItems, function (item) {
              var newItem = {
                "id": item.id,
                "approvedQuantity": item.approvedQuantity,
                "rejectQuantity": item.rejectQuantity,
                "rejectReasonId": item.rejectReasonId ? item.rejectReasonId : 1,
                "rejectNote": item.rejectNote,
                "statusId": item.statusId
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
        } else if (self.model.SelectedItems.length == self.model.purchaseRequests.items.length) {
          angular.forEach(self.model.SelectedItems, function (item) {
            var newItem = {
              "id": item.id,
              "approvedQuantity": item.approvedQuantity,
              "rejectQuantity": item.rejectQuantity,
              "rejectReasonId": item.rejectReasonId ? item.rejectReasonId : 1,
              "rejectNote": item.rejectNote,
              "statusId": item.statusId
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
        data.organizationId = $scope.organizationId;
        GenericService.serviceAction("PATCH", URI, data).then(
          function(response){
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

    var SelectAllItems = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedItems = [];
        self.model.purchaseRequests.items.forEach(function(item) {
          if (!isDisable(item)) {
            item.isSelected = true;
            item.isDisabled = false;
            item.approvedQuantity = item.quantity;
            self.model.SelectedItems.push(item);
            item.statusId = 5;
          }
        });
      } else {
        self.model.SelectedItems = [];
        self.model.purchaseRequests.items.forEach(function (item) {
          CancelRejection(item);
          item.isSelected = false;
          item.isDisabled = true;
          item.statusId = 10;
          if (item.status == "Pending Approval") {
            item.isReject = false;
          }
        });
      }
    }

    var SelectItem = function (item) {
      var items = self.model.SelectedItems;

      if (item.isSelected) {
        items.push(item);
        item.statusId = 5;
        item.isDisabled = false;
        item.approvedQuantity = item.quantity;
      } else {
        item.isReject = false;
        item.isDisabled = true;
        item.approvedQuantity = 0;
        item.statusId = 10;
        CancelRejection(item);
        var index = items.indexOf(item);
        if (index > -1) {
          items.splice(index, 1);
        }
      }

      if (self.model.purchaseRequests.items.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
    }

    var isDisable = function (item) {
      if (item.status !== "Pending Approval") {
        return true;
      }

      return false;
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
      var isValidAllItems=true;
      if (!self.model.SelectedItems || self.model.SelectedItems.length == 0) {
        growl.error('Please select items to update and save');
        isValid = false;
      }

      self.model.purchaseRequests.items.forEach(function(item){
        if (parseInt(item.approvedQuantity) > (item.newMaxOrderQuantity+item.quantity)) {
          growl.error("Approved quantity should less than pending quantity");
          item.isApprovedQtyValid = true; 
          isValid = false;
        }
        if(item.quantity < parseInt(item.approvedQuantity)){
          growl.error("Approved quantity should less than PR quantity");
          item.isApprovedQtyValid = true; 
          isValid = false;
        }
        if(item.approvedQuantity==0 && item.rejectQuantity==0)
        {
        growl.error('Accepted & Reject quantity both can not be zero');
        isValid = false;
        isValidAllItems=false;
        item.approvedQuantity = item.quantity;
        }
      });
      if(isValidAllItems==false)
      {
      isValid=false;
      }
      return isValid;
    }

    var NavigateToBack = function () {
      $state.go('localPurchhaseRequestApproval');
    }
    
    var ModalDismiss = function (popupName) {
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
    }
    var ModalDismissOnClose = function(popupName){
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
      var test = self.model.itemTrueFalse;
      test.isReject= false;
      test.approvedQuantity = test.quantity;
      test.rejectQuantity = 0;
    }

    var setStatus = function (x){
      var status = self.model.statuses.find(function(obj){
        return obj.status == x;
      });
      
      return status.id;
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailslocalPurchhaseRequestApproval", {
      url: "/detailslocalPurchhaseRequestApproval",
      templateUrl: 'views/procurement/purchase/details-local-purchase-approval.html',
      controller: "DetailsLocalPRApproval.Controller",
      controllerAs: "vm",
      title: 'Local Purchase Request Approval'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("DetailsLocalPRApproval.Controller", detailsLocalPRApprovalController);
})();