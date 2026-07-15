(function () {
  "use strict";

  function detailWorkOrderApprovalController($rootScope, $scope, $http, $cookies, CONSTANTS, $state, GenericService, commonDetailService, rejectReasonService, StatusService, growl) {
    var self = this;
    var selectedItemId;
    var selectedWorkOrderId;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
    function initializeController() {
      $rootScope.loginpage = true;

      self.model = {
        SelectedItems: [],
        selectAllRows: false,
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
      self.model.ModalDismissRejectionPopup = ModalDismissRejectionPopup;
      self.model.sendForQuotationRadioBtn = 'creation';
      GetWorkOrder(commonDetailService.getDataId());
    }

    var GetWorkOrder = function (workOrderId) {
      commonDetailService.setDataId(null);
      selectedWorkOrderId = workOrderId;
      // todo: workOrderId
      var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_APPROVAL.DETAILS_API + $scope.unitId + '/' + workOrderId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
       
          if(response.data.isCreation){
            self.model.sendForQuotationRadioBtn='creation';
          }
          else if(response.data.isQuotation){
            self.model.sendForQuotationRadioBtn='quotation';
          }
          else{
            self.model.sendForQuotationRadioBtn='creation';
          }
          self.model.workOrderData = response.data;
          self.model.approvalStatusId=response.data.approvalStatusId;
          self.model.requestItems = JSON.parse(JSON.stringify(response.data));
          self.model.workOrderData.items.forEach(function(item){
            // todo: status
            // item.status = "Pending Approval";
            item.isDisabled = true;
          });
          self.model.reasons = self.model.rejectReasons;
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var RejectionPopup = function (item) {
      if (item.isReject) {
        selectedItemId = item.id;
        self.model.action = 'Open';
        var popup = angular.element('#qtyaccepatReject');
        popup.modal('show');
        // self.model.rejectReason = self.model.reasons[0];
        self.model.rejectQty = item.rejectQuantity;
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
        item.approvedQuantity = prevRequest.approvedQuantity;
        item.rejectNote = prevRequest.rejectNote;
        item.rejectReasonId = prevRequest.rejectReasonId;
        item.statusId = prevRequest.statusId;
      }
    }

    var SaveAcceptReject = function () {
      if (ValidateRejection()) {
        var rejectItemId = selectedItemId;
        self.model.SelectedItems.forEach(function(item){
          if (item.isSelected)
          {
            if (item.id == rejectItemId) {
              if (self.model.rejectQty > item.quantity) {
                growl.error('Please enter quantity less than or equal to work order quantity.');
                return false;
              }
              else if(item.approvedQuantity == 0) {
                growl.error('Please enter quantity greater than 0.');
                return false;
              }else if (self.model.rejectQty == item.quantity) {
                item.rejectQuantity = item.quantity;
                item.approvedQuantity = item.quantity - item.rejectQuantity;
                item.rejectReasonId = self.model.reasons[0].id;
                item.statusId = setStatus('Rejected');
              } else if (self.model.rejectQty == 0 || !self.model.rejectQty) {
                item.approvedQuantity = item.quantity;
                item.rejectQuantity = 0;
                item.isReject = false;
                growl.error('Please Enter Reject Quantity');
                return false;
                /*item.rejectReasonId = self.model.reasons[0].id;
                item.statusId = setStatus('Approved');*/
              } else {
                item.rejectQuantity = self.model.rejectQty;
                item.rejectNote = self.model.rejectNote;
                item.approvedQuantity = item.quantity - item.rejectQuantity;
                item.rejectReasonId = self.model.reasons[0].id;
                item.statusId = setStatus('Partially Approved');
              }
              ModalDismiss('qtyaccepatReject');
            }
          }
        });
      }
    }

    var ApprovedQuantity = function (item) {
      if (item.approvedQuantity == 0)
      {
          item.rejectQuantity = 0;
          item.isapprovedQuantity=false;
          self.model.rejectQty = undefined;
          item.isReject = false;
          growl.error('Please enter quantity greater than 0');

      }
      else if (item.approvedQuantity > item.quantity) {
        item.rejectQuantity = 0;
        item.isapprovedQuantity=true;
        self.model.rejectQty = undefined;
        item.isReject = false;
        growl.error('Please enter quantity less than or equal to work order quantity')

        item.statusId = setStatus('Approved');
      } else if (!item.approvedQuantity) {
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
        item.statusId = setStatus('Partially Approved');
        RejectionPopup(item);
      } else if (item.approvedQuantity == item.quantity) {
        item.rejectQuantity = item.quantity - item.approvedQuantity;
        self.model.rejectQty = item.rejectQuantity;
        item.isReject = false;
        item.statusId = setStatus('Approved');
        RejectionPopup(item);
      }
      
    }

    var SaveApproval = function () {
      if (ValidateApproval()) {
        var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_APPROVAL.APPROVE_API + selectedWorkOrderId;
        
        var data = {
          "items": [],
          "unitId":$scope.unitId,
          "organizationId":$scope.organizationId,
          "userId":$scope.userId
        }

        if(self.model.sendForQuotationRadioBtn == 'quotation'){
          data.sendWorkOrderCreation = false;
          data.sendQuotationRequest = true;
          // console.log('1');
        } 
        else if (self.model.sendForQuotationRadioBtn == 'creation') {
          data.sendWorkOrderCreation = true;
          data.sendQuotationRequest = false;
          // console.log('2');
        } 
        $rootScope.startLoader();
        angular.forEach(self.model.SelectedItems, function (item) {
          if (item.isSelected && item.approvedQuantity > 0)
          {
            var newItem = {
              "id": item.id,
              "approvedQuantity": parseInt(item.approvedQuantity),
              "rejectQuantity": parseInt(item.rejectQuantity) ? item.rejectQuantity: undefined,
              "rejectReasonId": item.rejectReasonId ? item.rejectReasonId : 1,
              "rejectReason": item.rejectNote,
              "statusId": item.statusId
            }

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
          /*if(statuses.length > 1) {
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
          }*/
          if(statuses[0] == 7) {
            data.statusId = setStatus('Closed');
            data.approvalStatusId = setStatus('Rejected');
          } else {
            data.statusId = setStatus('Pending');
            data.approvalStatusId = setStatus('Approved');
          }

        });

        GenericService.serviceAction("PATCH", URI, data).then(
          function(response){
            $rootScope.stopLoader();
            growl.success(response.data.message);
            NavigateToBack();
          }, function (err) {
            $rootScope.stopLoader();
            //growl.error(err.data.message);
          });
      }
    }

    var isSelected = function (item) {
      return item.isSelected;
    }

    var SelectAllItems = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedItems = [];
        self.model.workOrderData.items.forEach(function(item) {
          if (!isDisable(item)) {
            item.isSelected = true;
            item.isDisabled = false;
            item.approvedQuantity = item.quantity;
            item.statusId = setStatus("Approved");
            self.model.SelectedItems.push(item);
          }
        });
      } else {
        self.model.SelectedItems = [];
        self.model.workOrderData.items.forEach(function (item) {
          CancelRejection(item);
          item.isSelected = false;
          item.isDisabled = true;
          item.approvedQuantity = 0;
          item.isReject = false;
          item.statusId = setStatus("Pending Approval");
        });
      }
    }

    var SelectItem = function (item) {
      var items = self.model.SelectedItems;

      if (item.isSelected) {
        items.push(item);
        item.isDisabled = false;
        item.approvedQuantity = item.quantity;
        item.statusId = setStatus("Approved");
      } else {
        item.isSelected = false;
        item.isReject = false;
        item.isDisabled = true;
        item.approvedQuantity = 0;
        item.statusId = setStatus("Pending Approval");
        CancelRejection(item);
        var index = items.indexOf(item);
        if (index > -1) {
          items.splice(index, 1);
        }
      }

      if (self.model.workOrderData.items.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
    }

    var isDisable = function (item) {
      if (item.status !== "Pending") {
        return true;
      }

      return false;
    }

    var ValidateRejection = function () {
      var isValid = true;
      if (!self.model.rejectReason) {
        isValid = false;
        growl.error('Please Select Reason to Reject');
      }

      return isValid;
    }

    var ValidateApproval = function () {
      var isValid = true;

      if (!self.model.SelectedItems || self.model.SelectedItems.length == 0  ) {
        growl.error('Select Items to Update and Save');
        isValid = false;
      }
      if(self.model.sendForQuotationRadioBtn==undefined){
        growl.error('Select option to Update and Save');
        isValid = false;
      }
      if(self.model.sendForQuotationRadioBtn == 'creation' || self.model.sendForQuotationRadioBtn == 'quotation'){
        isValid = true;
      }
     
      return isValid;
    }

    var NavigateToBack = function () {
      $state.go('workorderApprovalWorklist');
    }
    
    var ModalDismiss = function (popupName) {
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
    }

    var ModalDismissRejectionPopup = function (item){
      item.items.forEach(function (data){
        var prevRequest = self.model.requestItems.items.find(function(prevItem){
          return prevItem.id == data.id;
        });
  
        if (prevRequest) {
          if(data.isSelected){
            data.rejectQuantity = prevRequest.rejectQuantity;
            data.approvedQuantity = prevRequest.quantity;
            data.isReject = false;
          }
        }
      });
      $('#qtyaccepatReject').modal('hide');
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
    $stateProvider.state("detailsWorkorderApprovalWorklist", {
      url: "/detailsWorkorderApprovalWorklist",
      templateUrl: 'views/procurement/workOrder/details-workorder-approval-worklist.html',
      controller: "DetailsWorkorderApprovalWorklist.Controller",
      controllerAs: "vm",
      title: 'Work Order Approval Worklist'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("DetailsWorkorderApprovalWorklist.Controller", detailWorkOrderApprovalController);
})();