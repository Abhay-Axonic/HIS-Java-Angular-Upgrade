(function () {
  "use strict";
  var itemId;
  function varianceApprovalDetailsController($scope, $rootScope, $http, $state, commonDetailService, rejectReasonService, $cookies, CONSTANTS, GenericService, growl) {
    var self = this;

    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
      $rootScope.loginpage = true;
      self.model = {
        SelectedItems: [],
        selectItemList: [],
        selectAllRows: []


      };

      rejectReasonService.GetReason().then(function (reason) {
        self.model.rejectReasons = reason;
      });

      self.model.fromVarianceApproval = fromVarianceApproval;
      self.model.setStatus = setStatus;
      self.model.SelectAllItems = SelectAllItems;
      self.model.SelectItem = SelectItem;
      self.model.isDisable = isDisable;
      self.model.ValidateApproval = ValidateApproval;
      self.model.SaveApproval = SaveApproval;
      self.model.NavigateToBack = NavigateToBack;
      self.model.RejectionPopup = RejectionPopup;
      self.model.CancelRejection = CancelRejection;
      self.model.ModalDismiss = ModalDismiss;
      self.model.ValidateRejection = ValidateRejection;
      self.model.SaveAcceptReject = SaveAcceptReject;
      fromVarianceApproval()
    }


    function fromVarianceApproval() {
      var id = commonDetailService.getDataId();
      var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.VARIANCE_APPROVAL.DETAIL_API + id + "/" + $scope.unitId;
      var data = {};
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, data).then(
        function (response) {
           $rootScope.stopLoader();
          self.model.varianceDetail = response.data;
          $scope.storeId = response.data.storeId;           
          angular.forEach(self.model.varianceDetail.items, function (item, key) {
        	  item.cop = item.cop.toFixed(2);
              item.mrp = item.mrp.toFixed(2);
          });
        });
    }

    function setStatus(x) {

      var status = self.model.statuses.find(function (obj) {
        return obj.status == x;
      });

      return status.id;
    }

    var isDisable = function (item) {
      if (item.statusId != 10 || item.statusId == 7 || item.statusId == 9 || item.statusId == 5) {
        return true;
      }
      return false;
    }

    var SelectAllItems = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedRequestIds = [];
        self.model.varianceDetail.items.forEach(function (item) {
          if (!!isDisable(item)) {
            item.isSelected = true;
            self.model.SelectedRequestIds.push(item.id);
            item.isDisabled = false;
            self.model.SelectedItems.push(item);
          }
        });
      } else {
        self.model.SelectedRequestIds = [];
        self.model.SelectedItems = [];
        self.model.varianceDetail.items.forEach(function (item) {
          item.isSelected = false;
          item.isDisabled = true;
        });
      }
    }

    var SelectItem = function (item) {

      var batches = self.model.SelectedItems;

      if (item.isSelected) {
        batches.push(item);
        item.isDisabled = false;
      } else {
        item.isDisabled = true;
        item.isReject = false;
        var index = item.indexOf(item);
        if (index > -1) {
          batches.splice(index, 1);
        }
      }

      if (self.model.varianceDetail.items.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
    }


    var isSelected = function (item) {
      return item.isSelected;
    }


    var ValidateRejection = function () {
      var isValid = true;
      if (!self.model.rejectReason) {
        isValid = false;
        growl.error('Please Select Reason to Reject');
      }

      return isValid;
    }


    var SaveApproval = function () {

      if (ValidateApproval()) {
        var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.VARIANCE_APPROVAL.APPROVE_API + commonDetailService.getDataId();
        var data = {
          "statusId": 4,
          "storeId":  $scope.storeId,
          "items": [],
          "unitId": $scope.unitId,
          "userId" : $scope.userId
        }

        $rootScope.startLoader();
        angular.forEach(self.model.SelectedItems, function (item) { 
          var newItem = {
            "id": item.id,
            "isApproved": item.isApproved  ? item.isApproved:true,
            "rejectReasonId": item.rejectReasonId ? item.rejectReasonId : 1,  
            "rejectNote": item.rejectNote ? item.rejectNote : "default note" 
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
            growl.error('Somthing Went Wrong');
          });
      }
    }

    var ValidateApproval = function () {
      var isValid = true;

      if (!self.model.SelectedItems || self.model.SelectedItems.length == 0) {
        growl.error('Select Items to Update and Save');
        isValid = false;
      }

      return isValid;
    }

    var NavigateToBack = function () {
      $state.go('varianceApproval'); 
    }

    var RejectionPopup = function (item) {
      // console.log(self.model.status);
      if (item.isReject) {
        itemId = item.id;
        // self.model.rejectQty = item.rejectQuantity;
        self.model.action = 'Open';
        var popup = angular.element('#qtyaccepatReject');
        popup.modal('show');
        self.model.reasons = self.model.rejectReasons;
        // self.model.rejectReason = self.model.reasons[0];
      } else {
        CancelRejection(item);
      }
    }

    var CancelRejection = function (item) {
      var prevRequest = self.model.requestItems.items.find(function (prevItem) {
        return prevItem.id == item.id;
      });

      if (prevRequest) {
        item.rejectedQuantity = prevRequest.rejectedQuantity;
        item.acceptedQuantity = prevRequest.acceptedQuantity;
        item.rejectNote = prevRequest.rejectNote;
        item.rejectReasonId = prevRequest.rejectReasonId;
        item.statusId = prevRequest.statusId;
      }
    }

    var ModalDismiss = function (popupName) {
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
    }


    var SaveAcceptReject = function () {
      if (ValidateRejection()) {
        ModalDismiss('qtyaccepatReject');

      }
    }


    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailsVarianceApproval", {
      url: "/detailsVarianceApproval",
      templateUrl: 'views/PharmacyInventory/StockAdjustment/details-variance-approval.html',
      controller: "VarianceApprovalDetails.Controller",
      controllerAs: "vm",
      title: "Variance Approval"
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("VarianceApprovalDetails.Controller", varianceApprovalDetailsController);
})();
