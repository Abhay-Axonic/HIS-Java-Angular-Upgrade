(function () {
  "use strict";

  function detailsAuctionRequisitionApprovalController($rootScope, $scope, $http, $cookies, CONSTANTS, $state, GenericService, CurrencyService, commonDetailService, rejectReasonService, StatusService, growl) {
    var self = this;
    var itemId;
    var PrId;

    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
      $rootScope.loginpage = true;

      self.model = {
        SelectedItems: [],
        SelectedItemsPopup: []
      };

      rejectReasonService.GetReason().then(function (reason) {
        self.model.rejectReasons = reason;
      });

      StatusService.GetStatus().then(function (status) {
        self.model.statuses = status;
      });
      CurrencyService.GetCurrency().then(function (currency) {
        self.model.Currency = currency;
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
      self.model.viewAssets = viewAssets;
      self.model.valueOnChange = valueOnChange;
      self.model.SaveAccessoryDetails = SaveAccessoryDetails;

      self.model.SelectAllItemsPopup = SelectAllItemsPopup;
      self.model.SelectItemPopup = SelectItemPopup;
      self.model.valueOnChangePopup = valueOnChangePopup;
      self.model.SaveAcceptRejectPopup = SaveAcceptRejectPopup;
      self.model.ApprovedQuantityPopup = ApprovedQuantityPopup;
      self.model.ValidateQuantity = ValidateQuantity;
      self.model.RejectionAccessoryPopup = RejectionAccessoryPopup;
      self.model.ValidateCOP = ValidateCOP;
      GetAuctionRequisitionData(commonDetailService.getDataId());
    }

    var GetAuctionRequisitionData = function (auctionRequisitionId) {
      var id = auctionRequisitionId;
      var URI = CONSTANTS.ASSET.AUCTION_REQUISITION_APPROVAL.DETAILS_API + id;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.itemList = response.data;
          self.model.requestItems = JSON.parse(JSON.stringify(response.data));
          self.model.itemList.items.forEach(function (item) {
            item.isDisabled = true;
          });
          self.model.reasons = self.model.rejectReasons;
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error('No Data Found', { title: err.status });
        });
    }
    var viewAssets = function (item) {
      self.model.accessoryList = item.accessoriesDetails;
      self.model.accessoryList.forEach(function (item) {
        item.isDisabled = true;
      });
      $('#addAsset').modal();
      self.model.currentBatch = item;
    }
    var RejectionPopup = function (item) {
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
      var prevRequest = self.model.requestItems.items.find(function (prevItem) {
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

    var RejectionAccessoryPopup = function (item) {
      if (item.isReject) {
        itemId = item.id;
        self.model.action = 'Open';
        var popup = angular.element('#qtyaccepatRejectPopup');
        popup.modal('show');
        self.model.reasons = self.model.rejectReasons;
        self.model.rejectReason = self.model.reasons[0];
        // self.model.rejectQty = item.rejectQuantity;
        self.model.rejectNote = undefined;
      } else {
        CancelRejectionPopup(item);
      }
    }

    var CancelRejectionPopup = function (item) {
      var prevRequest = self.model.requestItems.items.find(function (prevItem) {
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
        var rejectItemId = itemId;
        self.model.SelectedItems.forEach(function (item) {
          if (item.id == rejectItemId) {
            if (self.model.rejectQty > item.quantity) {
              growl.error('Please Enter Valid Quantity');
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
              item.statusId = setStatus('Partially Approved');
            }
            ModalDismiss('qtyaccepatReject');
          }
        });
      }
    }

    var ApprovedQuantity = function (item) {
      if (parseInt(item.approvedQuantity) > item.auctionQty) {
        item.rejectQuantity = 0;
        self.model.rejectQty = undefined;
        item.isReject = false;
        item.statusId = setStatus('Approved');
      } else if (item.approvedQuantity == 0 || !item.approvedQuantity) {
        item.rejectQuantity = item.auctionQty;
        self.model.rejectQty = item.rejectQuantity;
        item.isReject = true;
        item.statusId = setStatus('Rejected');
      } else if (item.approvedQuantity < item.auctionQty) {
        item.rejectQuantity = item.auctionQty - item.approvedQuantity;
        self.model.rejectQty = item.rejectQuantity;
        item.isReject = true;
        item.statusId = setStatus('Partially Approved');
      } else if (item.approvedQuantity == item.auctionQty) {
        item.rejectQuantity = item.auctionQty - item.approvedQuantity;
        self.model.rejectQty = item.rejectQuantity;
        item.isReject = false;
        item.statusId = setStatus('Approved');
      }

    }

    var SaveApproval = function () {

      if (ValidateApproval()) {
        var URI = CONSTANTS.ASSET.AUCTION_REQUISITION_APPROVAL.SAVE_API;

        var data = {
          "id": self.model.itemList.id,
          "approvalStatusId": 4,
          "statusId": 2,
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
            "acceptedAuctionQuantiity": item.approvedQuantity,
            "auctionPrice": item.auctionPrice,
            "totalAuctionPrice": item.totalAuctionPrice,
            "rejectAuctionQuantity": item.rejectQuantity,
            "rejectReasonId": item.rejectReasonId ? item.rejectReasonId : 1,
            "rejectNote": item.rejectNote,
            "statusId": 4,
            "accessories": item.accessoriesList
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
            growl.error(err.data.message);
          });
      }
    }

    var isSelected = function (item) {
      return item.isSelected;
    }

    var SelectAllItems = function () {
      if (self.model.selectAllRows) {
        self.model.itemList.items.forEach(function (item) {
          if (!isDisable(item)) {
            item.isSelected = true;
            item.isDisabled = false;
            self.model.SelectedItems.push(item);
          }
        });
      } else {
        self.model.SelectedItems = [];
        self.model.itemList.items.forEach(function (item) {
          CancelRejection(item);
          item.isSelected = false;
          item.isDisabled = true;
          item.isReject = false;
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
        var index = items.indexOf(item);
        if (index > -1) {
          items.splice(index, 1);
        }
      }

      if (self.model.itemList.items.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
    }

    var isDisable = function (item) {
      if (item.status == "Approved" || item.status == "Partially Approved" || item.status == "Reject" || item.status == "Cancelled") {
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

      if (!self.model.SelectedItems || self.model.SelectedItems.length == 0) {
        growl.error('Select Items to Update and Save');
        isValid = false;
      }
      self.model.itemList.items.forEach(function (item) {
        if (!ValidateQuantity(item)) {
          isValid = false
        }
        if (!ValidateCOP(item)) {
          isValid = false
        }
      })
      return isValid;
    }

    var NavigateToBack = function () {
      $state.go('auctionRequisitionApproval');
    }

    var ModalDismiss = function (popupName) {
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
    }

    var setStatus = function (x) {
      var status = self.model.statuses.find(function (obj) {
        return obj.status == x;
      });

      return status.id;
    }


    function valueOnChange(item, isSelectAll) {
      if (isSelectAll)
        self.model.itemList.items.forEach(function (item) {
          if (self.model.selectAllRows) {
            item.approvedQuantity = item.auctionQty;
            item.rejectQuantity = 0;
            item.isReject = false;
          } else{
            item.approvedQuantity = 0;
            item.rejectQuantity = 0;
          }
        });
      else
        if (item.isSelected) {
          item.approvedQuantity = item.auctionQty;
          item.rejectQuantity = 0;
          item.isReject = false;
        } else{
          item.approvedQuantity = 0;
          item.rejectQuantity = 0;
        }
    }

    var SaveAccessoryDetails = function () {
      if (ValidateAccesories()) {
        self.model.itemList.items.forEach(function (obj) {
          obj.accessoriesList = [];
          if (obj.batchId == self.model.currentBatch.batchId) {
            self.model.accessoryList.forEach(function (item) {
              obj.accessoriesList.push({
                "id": item.id,
                "approvedQuantity": item.approvedQuantity,
                "rejectQuantity": item.rejectQuantity,
                "rejectReasonId": item.reasonId ? item.reasonId : 1,
                "rejectNote": item.rejectNote
              })
            });
          }
        });
        self.model.accessoryList = [];

        var popup = angular.element('#addAsset');
        popup.modal('hide');
      }
    }
    var ValidateAccesories = function () {
      var isValid = true;
      // self.model.accessoryList.forEach(function (item) {
      //   if (!ValidateQuantity(item)) {
      //     isValid = false;
      //   }
      //   if (!ValidateReason(item)) {
      //     isValid = false;
      //   }
      // })
      return isValid;
    }

    var SelectAllItemsPopup = function () {
      if (self.model.selectAllRowsPopup) {
        self.model.itemList.items.forEach(function (item) {
          if (!isDisable(item)) {
            item.isSelected = true;
            item.isDisabled = false;
            self.model.SelectedItemsPopup.push(item);
          }
        });
      } else {
        self.model.SelectedItemsPopup = [];
        self.model.itemList.items.forEach(function (item) {
          item.isSelected = false;
          item.isDisabled = true;
        });
      }
    }

    var SelectItemPopup = function (item) {
      var items = self.model.SelectedItemsPopup;

      if (item.isSelected) {
        items.push(item);
        item.isDisabled = false;
      } else {
        item.isDisabled = true;
        item.isReject = false;
        var index = items.indexOf(item);
        if (index > -1) {
          items.splice(index, 1);
        }
      }

      if (self.model.itemList.items.every(isSelected)) {
        self.model.selectAllRowsPopup = true;
      } else {
        self.model.selectAllRowsPopup = false;
      }
    }

    function valueOnChangePopup(item, isSelectAll) {
      if (isSelectAll)
        self.model.accessoryList.items.forEach(function (item) {
          if (self.model.selectAllRows) {
            item.approvedQuantity = item.quantity;
            item.rejectQuantity = 0;
            item.isReject = false;
          } else
            item.approvedQuantity = 0;
        });
      else
        if (item.isSelected) {
          item.approvedQuantity = item.quantity;
          item.rejectQuantity = 0;
          item.isReject = false;
        } else
          item.approvedQuantity = 0;
    }

    var SaveAcceptRejectPopup = function () {
      if (ValidateRejection()) {
        var rejectItemId = itemId;
        self.model.SelectedItems.forEach(function (item) {
          if (item.id == rejectItemId) {
            if (self.model.rejectQtyPopup > item.quantity) {
              growl.error('Please Enter Valid Quantity');
              return false;
            } else if (self.model.rejectQtyPopup == item.quantity) {
              item.rejectQuantity = item.quantity;
              item.approvedQuantity = item.quantity - item.rejectQuantity;
              item.rejectReasonId = self.model.reasons[0].id;
              item.statusId = setStatus('Rejected');
            } else if (self.model.rejectQtyPopup == 0 || !self.model.rejectQtyPopup) {
              item.approvedQuantity = item.quantity;
              item.rejectQuantity = 0;
              item.rejectReasonId = self.model.reasons[0].id;
              item.statusId = setStatus('Approved');
            } else {
              item.rejectQuantity = self.model.rejectQtyPopup;
              item.rejectNote = self.model.rejectNote;
              item.approvedQuantity = item.quantity - item.rejectQuantity;
              item.rejectReasonId = self.model.reasons[0].id;
              item.statusId = setStatus('Partially Approved');
            }
            ModalDismiss('qtyaccepatRejectPopup');
          }
        });
      }
    }

    var ApprovedQuantityPopup = function (item) {
      if (parseInt(item.approvedQuantity) > item.quantity) {
        item.rejectQuantity = 0;
        self.model.rejectQtyPopup = undefined;
        item.isReject = false;
        item.statusId = setStatus('Approved');
      } else if (item.approvedQuantity == 0 || !item.approvedQuantity) {
        item.rejectQuantity = item.quantity;
        self.model.rejectQtyPopup = item.rejectQuantity;
        item.isReject = true;
        item.statusId = setStatus('Rejected');
      } else if (item.approvedQuantity < item.quantity) {
        item.rejectQuantity = item.quantity - item.approvedQuantity;
        self.model.rejectQtyPopup = item.rejectQuantity;
        item.isReject = true;
        item.statusId = setStatus('Partially Approved');
      } else if (item.approvedQuantity == item.quantity) {
        item.rejectQuantity = item.quantity - item.approvedQuantity;
        self.model.rejectQtyPopup = item.rejectQuantity;
        item.isReject = false;
        item.statusId = setStatus('Approved');
      }

    }

    var ValidateQuantity = function (item) {
      var isValid = true;
      if (isNaN(parseFloat(item.approvedQuantity))) {
        item.isQuantityValid = true;
        isValid = false;
        growl.error("Please Enter Approved Scrap Quantity");
      }

      else if (parseInt(item.approvedQuantity) > item.quantity | parseInt(item.approvedQuantity) > item.auctionQty) {
        item.isQuantityValid = true;
        isValid = false;
        growl.error('Entered approved quantity is exceeding the auction quantity');
      } else {
        item.approvedQuantity = parseInt(item.approvedQuantity);
        isValid = true;
      }

      return isValid;
    }

    var ValidateCOP = function (item) {
      var isValid = true;

      if (isNaN(parseFloat(item.auctionPrice))) {
        item.auctionPrice = '';
        item.isCopValid = true;
        isValid = false;
        growl.error("Please Enter Auction Price");
      } else if (parseFloat(item.auctionPrice) == 0) {
        item.isCopValid = true;
        isValid = false;
        growl.error('Auction Price cannot be zero');
      } else if (item.auctionPrice > 0) {
        item.auctionPrice = parseFloat(item.auctionPrice).toFixed(2);
      } 
      
      if (parseFloat(item.cop) > parseFloat(item.auctionPrice)) {
        item.isCopValid = true;
        isValid = false;
        growl.error('Auction Price cannot be less than cop');
      }

      return isValid;
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailsAuctionRequisitionApproval", {
      url: "/detailsAuctionRequisitionApproval",
      templateUrl: 'views/Asset Management/details-auction-requisition-approval.html',
      controller: "DetailsAuctionRequisitionApproval.Controller",
      controllerAs: "vm",
      title: 'Auction Requisition Approval'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("DetailsAuctionRequisitionApproval.Controller", detailsAuctionRequisitionApprovalController);
})();
