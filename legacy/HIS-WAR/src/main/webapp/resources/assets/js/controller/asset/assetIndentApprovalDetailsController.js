(function () {
  "use strict";

  function assetIndentApprovalDetailsController($scope, $rootScope, StatusService,$state, rejectReasonService, commonDetailService, $http, $cookies, CONSTANTS, GenericService, growl) {
    var self = this;
var itemId;
    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); 
    if(cookieObject == undefined){ 
    	$state.go('login');
    	return;
    	}
    $scope.unitId = cookieObject.unitId;
    $scope.organizationId = cookieObject.organizationId;
    $scope.userId = cookieObject.userId;
    $scope.storeId = cookieObject.storeId; 
    
    function initializeController() {
      $rootScope.loginpage = true;
      self.model = {
        SelectedItems: [],
        selectItemList: [],
        selectAllRows: [],

        selectItemListAcessory: [],
        selectAllRowsAcessory: [],
        SelectedItemsAcessory: [],
      };

      StatusService.GetStatus().then(function (status) {
        self.model.statuses = status;
      });
      rejectReasonService.GetReason().then(function (reason) {
        self.model.rejectReasons = reason;
      });
      self.model.accessoryModal = accessoryModal;
      self.model.ApprovedQuantity = ApprovedQuantity;
      self.model.SaveAcceptReject = SaveAcceptReject;
      self.model.valueOnChange = valueOnChange;
      self.model.setStatus = setStatus;
      self.model.isDisable = isDisable;

      self.model.SelectAllItems = SelectAllItems;
      self.model.SelectItem = SelectItem; 
      self.model.AssetIndentApproval = AssetIndentApproval;
      self.model.RejectionPopup = RejectionPopup;
      self.model.quantityChange = quantityChange;
      self.model.ValidateQuantity =ValidateQuantity;
      self.model.ModalDismiss = ModalDismiss;


      fromAssetIndentApproval();

    }

     $scope.flagBtn = false;
    function fromAssetIndentApproval() {
      var id = commonDetailService.getDataId();
      var URI = CONSTANTS.ASSET.ASSET_INDENT_APPROVAL.DETAIL_API + $scope.unitId + '/'+$scope.organizationId + '/' + id;
      var data = {};
      GenericService.serviceAction("GET", URI, data).then(
        function (response) {
          self.model.requestItems = JSON.parse(JSON.stringify(response.data));
          self.model.assetIndentApprovalDetails = response.data;
          if(self.model.assetIndentApprovalDetails.approvalStatus =='Approved' || self.model.assetIndentApprovalDetails.approvalStatus =='Rejected' || self.model.assetIndentApprovalDetails.approvalStatus =='Cancelled')
          {
        	  $scope.flagBtn = true;
          }
          self.model.assetIndentApprovalDetails.items.forEach(function (item) {
            item.isDisabled = true;
            item['newAccessoryList'] = [];
          });
        });
    }

    var accessoryModal = function (asset) {
      $("#viewDetailsAccessoriesModal").modal()
      self.model.acessoryList = [];
      self.model.acessoryList = asset.accessories;
      self.model.currentItem = asset;
      self.model.acessoryList.forEach(function (accessory) {
        accessory.isDisabled = true;
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
        self.model.assetIndentApprovalDetails.items.forEach(function (item) {
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
        self.model.assetIndentApprovalDetails.items.forEach(function (item) {
          item.isSelected = false;
          item.isDisabled = true;
        });
      }
    }

    var SelectItem = function (item) {

      var items = self.model.SelectedItems;

      if (item.isSelected) {
        items.push(item);
        item.isDisabled = false;
      } else {
        item.isDisabled = true;
        item.isReject = false;
        var index = item.indexOf(item);
        if (index > -1) {
          items.splice(index, 1);
        }
      }

      if (self.model.assetIndentApprovalDetails.items.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
    }

 

    var isSelected = function (item) {
      return item.isSelected;
    }

    var ApprovedQuantity = function (item) {
      self.model.reasons = self.model.rejectReasons;
      itemId = item.id;
      if (item.approvedQuantity > item.quantity) {
        item.rejectedQuantity = 0;
        self.model.rejectQty = undefined;
        item.isReject = false;
        item.status = setStatus('Approved');; // Approved.
        $("#qtyaccepatReject").modal();
      } else if (item.approvedQuantity == 0 || !item.approvedQuantity) {
        item.rejectedQuantity = item.quantity;
        self.model.rejectQty = item.rejectedQuantity;
        item.isReject = true;
        item.status = setStatus('Rejected');; // Rejected
        $("#qtyaccepatReject").modal();
      } else if (item.approvedQuantity < item.quantity) {
        item.rejectedQuantity = item.quantity - item.approvedQuantity;
        self.model.rejectQty = item.rejectedQuantity;
        item.isReject = true;
        item.status = setStatus('Partially Approved');; // Partially Approved
        $("#qtyaccepatReject").modal();
      } else if (item.approvedQuantity == item.quantity) {
        item.rejectedQuantity = item.quantity - item.approvedQuantity;
        self.model.rejectQty = item.rejectedQuantity;
        item.isReject = false;
        item.status = setStatus('Approved');; // Approved.
        $("#qtyaccepatReject").modal();
      }
    }


    var SaveAcceptReject = function () {
      if (ValidateRejection()) {
        var rejectItemId = itemId;
        self.model.SelectedItems.forEach(function (item) {
          if (item.id == rejectItemId) {
            if (self.model.rejectQty > item.quantity) {
              growl.error('Please Enter Valid Reject Quantity');
              return false;
            } else if (self.model.rejectQty == item.quantity) {
              item.rejectedQuantity = item.quantity;
              item.approvedQuantity = 0;
              item.rejectReasonId = self.model.rejectReasons[0].id;
              item.statusId = setStatus('Rejected'); // Rejected
            } else if (self.model.rejectQty == 0 || !self.model.rejectQty) {
              item.approvedQuantity = item.quantity;
              item.rejectedQuantity = 0;
              item.rejectReasonId = self.model.rejectReasons[0].id;
              item.statusId = setStatus('Pending'); // Approved
            } else {
              item.rejectedQuantity = self.model.rejectQty;
              item.rejectNote = self.model.rejectNote;
              item.approvedQuantity = item.quantity - item.rejectedQuantity;
              item.rejectReasonId = self.model.rejectReasons[0].id;
              item.statusId = setStatus('Partially Approved'); // Partially Approved
            }
            ModalDismiss('qtyaccepatReject');
          }
        });
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


    function valueOnChange(item, isSelectAll) {
      if (isSelectAll)
        self.model.assetIndentApprovalDetails.items.forEach(function (item) {
          if (self.model.selectAllRows)
            item.approvedQuantity = item.quantity;
          else
            item.approvedQuantity = 0;
        });
      else
        if (item.isSelected)
          item.approvedQuantity = item.quantity;
        else
          item.approvedQuantity = 0;
    }
 


    var ModalDismiss = function (popupName) {
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
    }

    var AssetIndentApproval = function () {
      var pending = 0;
      var approved = 0;
      var reject = 0;
      var partial = 0;

      if (ValidateApproval()) {
        var URI = CONSTANTS.ASSET.ASSET_INDENT_APPROVAL.SAVE_API +  commonDetailService.getDataId();
        var data = {
          "statusId": setStatus('Pending'),
          "approvalStatusId": setStatus('Rejected'),
          "unitId":$scope.unitId,
          "organizationId":$scope.organizationId,
          "userId":$scope.userId,
          "storeId":$scope.storeId,
          "items": []
        }

        $rootScope.startLoader();
        angular.forEach(self.model.SelectedItems, function (item) {
          var newItem = {
            "id": item.id,
            "approvedQuantity": item.approvedQuantity,
            "rejectQuantity": item.rejectedQuantity,
            "rejectReasonId":1, 
            "rejectNote": item.rejectNote,
            "statusId": 6,
            "specification": item.specification,
            "accessories": item.newAccessoryList,
          }

          if (item.approvedQuantity >= item.quantity) {
            newItem.statusId = setStatus('Approved');
            item.statusId = setStatus('Approved');
          }
          else if (item.approvedQuantity > 0 && item.approvedQuantity < item.quantity) {
            newItem.statusId = setStatus('Partially Approved');
            item.statusId = setStatus('Partially Approved');
          }
          else {
            newItem.statusId = setStatus('Rejected');
            item.statusId = setStatus('Rejected');
          }
 

          data.items.push(newItem);
        });
        angular.forEach(self.model.assetIndentApprovalDetails.items, function (item) {
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
        growl.error('Select Items to Approve');
        isValid = false;
      }
      self.model.assetIndentApprovalDetails.items.forEach(function(item){
        if(!ValidateQuantity(item)){
          isValid= false;
        }
      })
      return isValid;
    }


    var NavigateToBack = function () {
      $state.go('assetIndentApproval');
    }

    
    var RejectionPopup = function (item) {
      self.model.onclose = item;
      self.model.rejectQty = '';
      self.model.rejectReason = '';
      self.model.rejectNote = '';
      if (item.isReject) {
        itemId = item.id;
        $("#qtyaccepatReject").modal();
        self.model.reasons = self.model.rejectReasons;
      } else {
        CancelRejection(item);
      }
    }

    var quantityChange = function (item) {
      if (!item.approvedQuantity)
        item.approvedQuantity = 0;
    }

    var CancelRejection = function (item) {
      // self.model.onclose.isReject = false;
      var prevRequest = self.model.requestItems.items.find(function (prevItem) {
        return prevItem.id == item.id;
      });

      if (prevRequest) {
        item.rejectQuantity = prevRequest.rejectQuantity;
        item.rejectedQuantity = prevRequest.rejectedQuantity;
        item.approvedQuantity = prevRequest.quantity;
        item.rejectNote = prevRequest.rejectNote;
        item.rejectReasonId = prevRequest.rejectReasonId;
        item.statusId = prevRequest.statusId;
      }
    }

    var ValidateQuantity = function (item) {
      var isValid = true;
      if (isNaN(parseFloat(item.approvedQuantity))) {
          item.isQuantityValid = true;
          isValid = false;
          growl.error("Please Enter Accept approved");
      }

      else if (parseInt(item.approvedQuantity) > item.quantity) {
          item.isQuantityValid = true;
          isValid = false;
          growl.error('Entered approved quantity is exceeding the indent quantity');
      } else {
          item.approvedQuantity = parseInt(item.approvedQuantity);
          isValid = true;
      }

      return isValid;
  }
    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailsAssetIndentApproval", {
      url: "/detailsAssetIndentApproval",
      templateUrl: 'views/master/UnitMaster/assetManagement/details-asset-indent-approval.html',
      controller: "AssetIndentApprovalDetails.Controller",
      controllerAs: "vm",
      title: 'Asset Indent Approval'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("AssetIndentApprovalDetails.Controller", assetIndentApprovalDetailsController);
})();


