(function () {
  "use strict";

  function detailsPRApprovalController($rootScope, $scope, $http, $cookies, CONSTANTS, $state, GenericService, commonDetailService, rejectReasonService, StatusService, growl) {
    var self = this;
    var itemId;
    var prId;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
      $rootScope.loginpage = true;

      self.model = {
        SelectedItems: [],
        purchaseRequests: {
          items: []
        }
      };

      rejectReasonService.GetReason().then(function (reason) {
        self.model.rejectReasons = reason;
      });

      StatusService.GetStatus().then(function (status) {
        self.model.statuses = status;
      });

      self.model.NavigateToBack = NavigateToBack;
      self.model.RejectionPopup = RejectionPopup;
      self.model.SelectItem = SelectItem;
      self.model.SelectAllItems = SelectAllItems;
      self.model.ApproveSelectedItems = ApproveSelectedItems;
      self.model.SaveAcceptReject = SaveAcceptReject;
      self.model.ApprovedQuantity = ApprovedQuantity;
      self.model.ModalDismiss = ModalDismiss;
      self.model.isDisable = isDisable;
      self.model.ValidateSaveButton = ValidateSaveButton;
      self.model.ListContainData = ListContainData;
      self.model.ModalDismissRejectionPopup = ModalDismissRejectionPopup;

      GetPurchaseRequest(commonDetailService.getDataId());
    }

    var setStatus = function (x) {
      var status = self.model.statuses.find(function (obj) {
        return obj.status == x;
      });

      return status.id;
    }

    var ValidateSaveButton = function () {
      var isValid = false;
      if (self.model.purchaseRequests.items) {
        self.model.purchaseRequests.items.find(function (item) {
          if (!!item.isSelected) {
            isValid = true;
          }
        });

        return isValid;
      }
    }

    var GetPurchaseRequest = function (purchaseId) {
      prId = purchaseId;
      commonDetailService.setDataId(null); 
      var URI = CONSTANTS.PROCUREMENT.PURCHASE.PURCHASE_REQUEST_APPROVAL.SAVE_API + $scope.unitId + '/'+$scope.organizationId + '/' + purchaseId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
        	$rootScope.stopLoader();
          self.model.purchaseRequests = response.data;
          self.model.assetTypeId = response.data.assetTypeId;
          self.model.prTypeId = response.data.prTypeId;
          self.model.requestItems = JSON.parse(JSON.stringify(response.data));
          self.model.purchaseRequests.items.forEach(function (item) {
            item.isDisabled = true;
            item.isOpen = false;
            if (!!item.rejectQuantity) {
              item.isReject = true;
            }
            if (self.model.assetTypeId == 1) {
              var uomList = item.uom.find(function (uom) {
                return item.uomType == uom.uomType;
              });
              item.uomDetails = uomList.uomDetails;
              CalculateStock(item, item.uom, uomList);
            } else {
              var uomList = null;
              if(item.threeUOMApplicable == false){
                item.uomListdata = item.uom;
                item.uom = [];
                angular.forEach(item.uomListdata, function(data){
                  if(data.uomType != 'Stock'){
                    item.uom.push(data);
                  }
                });
              }
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
          if(item.threeUOMApplicable == false){
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
          if(item.threeUOMApplicable != false){
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
        } else if (uomList.uomDetails == "D") {
          item.newQty = parseInt(stockAvailable / (uom[2].conversion));
          item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[2].conversion));
          item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[2].conversion));
          if(item.approvedQuantity == undefined || item.approvedQuantity == null){
            item.receivedQtyCon=(item.quantity);
            item.receivedQtyCalculation = (item.quantity);
          }
          else{
            item.receivedQtyCon=(item.approvedQuantity);
            item.receivedQtyCalculation = (item.approvedQuantity);
          }
          item.uomUnitData = uom[2].uomUnit;
        }
      } else {
        item.newQty = item.availableQuantity;
        item.newMaxOrderQuantity = item.maxOrderQuantity || 0;
        item.newMaxStoreQuantity = item.maxStoreQuantity || 0;
      }
    }

    $scope.calculateReceivedQty = function (item) {
      var uomTypeId = item.uomDetails.split(" - ")[0];
			if(item.threeUOMApplicable == false){
				if(uomTypeId == "P"){
					item.receivedQtyCon = (item.approvedQuantity +"*"+ item.uom[0].conversion);
					item.receivedQtyCal = (item.approvedQuantity * item.uom[0].conversion);
          item.uomUnitData = item.uom[2].uomUnit;
				}
				else if(uomTypeId == "S"){
					
				}
				else{
					item.receivedQtyCon = (item.approvedQuantity);
					item.receivedQtyCal = (item.approvedQuantity);
          item.uomUnitData = item.uom[2].uomUnit;
				}
			}
			else{
				if(uomTypeId == "P"){
					item.receivedQtyCon = (item.approvedQuantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
					item.receivedQtyCal = (item.approvedQuantity * item.uom[0].conversion * item.uom[1].conversion);
          item.uomUnitData = item.uom[2].uomUnit;
				}
				else if(uomTypeId == "S"){
					item.receivedQtyCon = (item.approvedQuantity +"*"+ item.uom[1].conversion);
					item.receivedQtyCal = (item.approvedQuantity * item.uom[1].conversion);
          item.uomUnitData = item.uom[2].uomUnit;
				}
				else{
					item.receivedQtyCon = (item.approvedQuantity);
					item.receivedQtyCal = (item.approvedQuantity);
          item.uomUnitData = item.uom[2].uomUnit;
				}
			}
		}

    var ModalDismiss = function (popupName) {
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
    }

    var RejectionPopup = function (item) {
      if (item.isReject) {
        itemId = item.id;
        self.model.action = 'Open';
        var popup = angular.element('#qtyaccepatReject');
        popup.modal('show');
        self.model.rejectNote = undefined;
        item.isOpen = false;
      } else {
        CancelRejection(item);
      }
    }

    var CancelRejection = function (item) {
      var prevRequest = self.model.requestItems.items.find(function (prevItem) {
        return prevItem.id == item.id;
      });

      if (prevRequest) {
        item.rejectQuantity = prevRequest.rejectQuantity;
        item.approvedQuantity = prevRequest.quantity;
        item.rejectNote = prevRequest.rejectNote;
        item.rejectReasonId = prevRequest.rejectReasonId;
        item.statusId = 5;
        item.isOpen = false;
        $scope.calculateReceivedQty(item);
      }
    }

    var SaveAcceptReject = function () {
      if (ValidateRejection()) {
        var rejectItemId = itemId;
        self.model.SelectedItems.forEach(function (item) {
          if (item.id == rejectItemId) {
            if (self.model.rejectQty > item.quantity) {
              growl.error('Rejection quantity is must be less than ' + item.quantity);
              item.isOpen = false;
              return false;
            } else if (self.model.rejectQty == item.quantity) {
              item.rejectQuantity = item.quantity;
              item.approvedQuantity = item.quantity - item.rejectQuantity;
              item.rejectReasonId = self.model.reasons[0].id;
              item.statusId = setStatus('Rejected');
              item.isOpen = false;
            } else if (self.model.rejectQty == 0 || !self.model.rejectQty) {
              item.approvedQuantity = item.quantity;
              item.rejectQuantity = 0;
              item.rejectReasonId = self.model.reasons[0].id;
              item.statusId = setStatus('Approved');
              item.isOpen = false;
            } else {
              item.rejectQuantity = self.model.rejectQty;
              item.rejectNote = self.model.rejectNote;
              item.approvedQuantity = item.quantity - item.rejectQuantity;
              item.rejectReasonId = self.model.reasons[0].id;
              item.statusId = setStatus('Approved');
              item.isOpen = false;
            }
            if(self.model.rejectQty == 0){
              item.isReject = false;
              item.isOpen = false;
            }
            self.model.rejectQty = undefined;
            self.model.rejectNote = undefined;
            ModalDismiss('qtyaccepatReject');
            $scope.calculateReceivedQty(item);
          }
        });
      }
    }

    var validateQuantity = function (item){
      var isValid = true;
      if(item.approvedQuantity > item.quantity) {
        growl.error("Approved quantity is greater than quantity");
        isValid = false;
      }
      return isValid;
    }

    var ApprovedQuantity = function (item) {

      console.log("Item Id : ",item.itemId);
      console.log("isOpen : ",item.isOpen);
      if (validateQuantity(item)) {
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
          item.isReject = true;
          item.rejectReasonId = self.model.reasons[0].id;
          item.statusId = setStatus('Approved');
          RejectionPopup(item);
        } else if (item.approvedQuantity == item.quantity) {
          item.rejectQuantity = item.quantity - item.approvedQuantity;
          self.model.rejectQty = item.rejectQuantity;
          item.isReject = false;
          item.statusId = setStatus('Approved');
          RejectionPopup(item);
        }
      }
    }




    var ApproveSelectedItems = function () {
      if (ValidateApproval()) {
        var URI = CONSTANTS.PROCUREMENT.PURCHASE.PURCHASE_REQUEST_APPROVAL.SAVE_API + prId;

        var pending = 0;
        var approved = 0;
        var reject = 0;
        var partial = 0;

        var data = {
          "statusId": setStatus('Pending'),
          "approvalStatusId": setStatus('Rejected'),
          "items": [],
          "unitId":$scope.unitId,
          "organizationId":$scope.organizationId,
          "userId":$scope.userId
        }

        $rootScope.startLoader();
        angular.forEach(self.model.SelectedItems, function (item) {
          var newItem = {
            "id": item.id,
            "approvedQuantity": parseInt(item.approvedQuantity),
            "rejectQuantity": item.rejectQuantity,
            "rejectNote": self.model.rejectNote,
            "statusId": item.status ? item.status : 6
          }

          if (item.rejectQuantity) {
            if (!item.rejectReasonId) {
              newItem.rejectReasonId = 1;
            }
            else
              newItem.rejectReasonId = item.rejectReasonId;
          }
          if (item.approvedQuantity >= item.quantity) {
            newItem.statusId = setStatus('Approved');
            item.statusId = setStatus('Approved');
          }
          else if (item.approvedQuantity > 0 && item.approvedQuantity < item.quantity) {
            newItem.statusId = setStatus('Approved');
            item.statusId = setStatus('Approved');
          }
          else {
            newItem.statusId = setStatus('Rejected');
            item.statusId = setStatus('Rejected');
          }

          data.items.push(newItem);
        });
        angular.forEach(self.model.purchaseRequests.items, function (item) {
          if (item.statusId == setStatus('Pending Approval')) {
            pending++;
          }
          else if (item.statusId == setStatus('Rejected')) {
            reject++;
          }
          else if (item.statusId == setStatus('Approved')) {
            approved++;
          }
          else if (item.statusId == setStatus('Partially Approved')) {
            partial++;
          }
        });

        if (pending > 0 && (partial > 0 || approved > 0 || reject > 0)) {
          data.approvalStatusId = setStatus('Partially Approved');
        }
        else if (pending == 0) {
          if (approved > 0 || partial > 0) {
            data.approvalStatusId = setStatus('Approved');
          }
          else {
            data.approvalStatusId = setStatus('Rejected');
            data.statusId = setStatus('Closed');
          }
        }

        if(self.model.purchaseRequests.items.length == 1){
          angular.forEach(self.model.purchaseRequests.items, function(d){
            if(d.quantity == d.rejectQuantity){
              data.approvalStatusId = setStatus('Rejected');
              data.statusId = setStatus('Closed');
            }
            else{
              data.approvalStatusId = setStatus('Approved');
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
          }
        );
      }
    }

    var isSelected = function (item) {
      return item.isSelected;
    }

    var ListContainData = function () {
      var isValid = true;
      var list = self.model.purchaseRequests.items;
      list.find(function (item) {
        if (!item.isSelected) {
          isValid = false;
        }
      });

      return isValid;
    }

    var SelectAllItems = function () {
      if (self.model.selectAllRows) {
        self.model.purchaseRequests.items.forEach(function (item) {
          if (!isDisable(item)) {
            item.isSelected = true;
            item.isDisabled = false;
            if(item.approvedQuantity == undefined || item.approvedQuantity == null){
              item.approvedQuantity = item.quantity;
            }
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
        item.isDisabled = false;
        item.statusId = 5;
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

    var ValidateApproval = function () {
      var isValid = true;

      if (!self.model.SelectedItems || self.model.SelectedItems.length == 0) {
        growl.error('Select Items to Update and Save');
        isValid = false;
      }

      self.model.purchaseRequests.items.forEach(function (item) {
        if (parseInt(item.approvedQuantity) > (item.newMaxOrderQuantity + item.quantity)) {
          growl.error("Approved quantity should less than pending quantity");
          item.isApprovedQtyValid = true;
          isValid = false;
        }
        if(item.approvedQuantity > item.quantity){
          growl.error("Approved quantity is greater than quantity");
          isValid = false;
        }
      });

      return isValid;
    }

    var ValidateRejection = function () {
      var isValid = true;
      if (!self.model.rejectReason) {
        isValid = false;
        growl.error('Please Select Reason to Reject');
      }
      if(self.model.rejectQty == '' || self.model.rejectQty == undefined){
        isValid = false;
        growl.error('Please Select Qty to Reject');
      }

      return isValid;
    }

    var NavigateToBack = function () {
      $state.go('purchaseRequestApproval');
    }

    var ModalDismissRejectionPopup = function (item){
      item.items.forEach(function (data){
        var prevRequest = self.model.requestItems.items.find(function(prevItem){
          return prevItem.id == data.id;
        });
  
        if (prevRequest) {
          if(data.isSelected == true){
            data.rejectQuantity = prevRequest.rejectQuantity;
            data.approvedQuantity = prevRequest.quantity;
            data.isReject = false;
            data.isOpen = false;
            $scope.calculateReceivedQty(data);
          }
        }
      });
      $('#qtyaccepatReject').modal('hide');
    } 

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailsPurchaseRequestApproval", {
      url: "/detailsPurchaseRequestApproval",
      templateUrl: 'views/procurement/purchase/details-purchase-request-approval.html',
      controller: "DetailsPRApprovalController.Controller",
      controllerAs: "vm",
      title: 'Purchase Request Approval'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("DetailsPRApprovalController.Controller", detailsPRApprovalController);
})();