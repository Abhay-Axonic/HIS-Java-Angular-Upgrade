(function () {
  "use strict";

  function witnessApprovalDetailsController($scope, $rootScope, commonDetailService, $state, $http,StatusService,rejectReasonService, $cookies, CONSTANTS, GenericService, growl,stopTransactionService,$filter) {
    var self = this;
    var itemId;
    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
      $rootScope.loginpage = true;
      self.model = {

        SelectedItems: [],
        selectItemList: [],
        selectAllRows: []


      };
      StatusService.GetStatus().then(function (status) {
        self.model.statuses = status;
      });
      rejectReasonService.GetReason().then(function (reason) {
        self.model.rejectReasons = reason;
      });
      self.model.fromWitnessApproval = fromWitnessApproval;
      self.model.setStatus = setStatus;
      self.model.SelectAllItems = SelectAllItems;
      self.model.SelectItem = SelectItem;
      self.model.isDisable = isDisable;
      self.model.valueOnChange = valueOnChange;
      self.model.ApprovedQuantity = ApprovedQuantity;
      self.model.ValidateApproval = ValidateApproval;
      self.model.SaveApproval = SaveApproval;
      self.model.NavigateToBack = NavigateToBack;
      self.model.RejectionPopup = RejectionPopup;
      self.model.CancelRejection = CancelRejection;
      self.model.SaveAcceptReject = SaveAcceptReject;
      self.model.ModalDismiss = ModalDismiss;
      self.model.ModalDismissClose = ModalDismissClose;
      fromWitnessApproval();

    }
    function fromWitnessApproval() {
      var id = commonDetailService.getDataId();
      var URI = CONSTANTS.INVENTORY.SCRAP.WITNESS_APPROVAL.DETAIL_API + id;
      var data = {};
      GenericService.serviceAction("GET", URI, data).then(
        function (response) {
          self.model.witnessDetailData = response.data;
          $scope.storeId = self.model.witnessDetailData.storeId;
          console.log("Status Id: ",self.model.witnessDetailData.statusId);
          self.model.witnessDetailData.items.forEach(function (item) {
            item.isDisabled = true;
            item.rejectedQuantity = item.rejectQuantity;
            if(item.rejectedQuantity){
              item.isReject = true;
            }
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
        self.model.witnessDetailData.items.forEach(function (item) {
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
        self.model.witnessDetailData.items.forEach(function (item) {
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
        // var index = item.indexOf(item);
        // if (index > -1) {
        //   items.splice(index, 1);
        // }
      }

      if (self.model.witnessDetailData.items.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
    }


    var isSelected = function (item) {
      return item.isSelected;
    }

    var ApprovedQuantity = function (item) {
      if (item.approvedQuantity > item.disposeQuantity) {
        // item.rejectedQuantity = 0;
        // self.model.rejectQty = undefined;
        // item.isReject = false;
        // item.status = setStatus('Approved');; // Approved.
        // RejectionPopup(item);
        growl.error("Approved quantity is grater than dispose quantity");
        item.approvedQuantity = 0;
      } else if (item.approvedQuantity == 0 || !item.approvedQuantity) {
        item.rejectedQuantity = item.quantity;
        self.model.rejectQty = item.rejectedQuantity;
        item.isReject = true;
        item.status = setStatus('Rejected');; // Rejected
        RejectionPopup(item);
      } else if (item.approvedQuantity < item.disposeQuantity) {
        item.rejectedQuantity = item.disposeQuantity - item.approvedQuantity;
        self.model.rejectQty = item.rejectedQuantity;
        item.isReject = true;
        item.status = setStatus('Partially Approved');; // Partially Approved
        RejectionPopup(item);
      } else if (item.approvedQuantity == item.disposeQuantity) {
        item.rejectedQuantity = item.disposeQuantity - item.approvedQuantity;
        self.model.rejectQty = item.rejectedQuantity;
        item.rejectQuantity = item.rejectedQuantity;
        item.isReject = false;
        item.status = setStatus('Approved');; // Approved.
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

    var SaveAcceptReject = function () {
      if (ValidateRejection()) {
        var rejectItemId = itemId;
        self.model.SelectedItems.forEach(function (item) {
          if (item.id == rejectItemId) {
        	  /* start code for resolved HIS-10576 Store/Inventory- Rejected quantity is not showing on witness approval screen */
        	  item.rejectedQuantity = angular.copy(self.model.rejectQty);
        	  /* end code for resolved HIS-10576 Store/Inventory- Rejected quantity is not showing on witness approval screen */
            if (self.model.rejectQty > item.disposeQuantity) {
              growl.error('Please Enter Valid Quantity');
              return false;
            } else if (self.model.rejectQty == item.disposeQuantity) {
              item.rejectQuantity = item.disposeQuantity;
              item.approvedQuantity = item.disposeQuantity - item.rejectedQuantity;
              item.rejectReasonId = self.model.rejectReasons[0].id;
              item.statusId = setStatus('Rejected'); // Rejected
            } else if (self.model.rejectQty == 0 || !self.model.rejectQty) {
              item.approvedQuantity = item.disposeQuantity;
              item.rejectQuantity = 0;
              item.rejectReasonId = self.model.rejectReasons[0].id;
              item.statusId = setStatus('Pending'); // Approved
            } else {
              item.rejectQuantity = self.model.rejectQty;
              item.rejectNote = self.model.rejectNote;
              item.approvedQuantity = item.disposeQuantity - item.rejectedQuantity;
              item.rejectReasonId = self.model.rejectReasons[0].id;
              item.statusId = setStatus('Partially Approved'); // Partially Approved
            }
            ModalDismiss('qtyaccepatReject');
          }
        });
      }
    }
    function valueOnChange(item, isSelectAll) {
      if (isSelectAll)
        self.model.witnessDetailData.items.forEach(function (item) {
          if (self.model.selectAllRows)
            item.approvedQuantity = item.disposeQuantity;
          else
            item.approvedQuantity = 0;
        });
      else
        if (item.isSelected)
          item.approvedQuantity = item.disposeQuantity;
        else
          item.approvedQuantity = 0;
    }
    $scope.saveBtnDisabled=false;
    var SaveApproval = function () {
        $scope.saveBtnDisabled=true;
    	if (ValidateApproval()) {
    		
    		 ActualSaveApproval();
    		/*
        	self.model.selectedStopIds = [];
           angular.forEach(self.model.SelectedItems, function (item) {
       		  self.model.selectedStopIds.push(item.itemId);
       	    });	
       	  var stopData = {
    	                "itemIds": self.model.selectedStopIds,
    	                "storeId": $scope.storeId  	      		 
    	              };
     	    
     	 	stopTransactionService.GetstopTransactionData(stopData).then(function(responseData){        
              var StopTransactionData = responseData;  
              if(StopTransactionData.length > 0 ){    
                       var startdate =  $filter('date')(StopTransactionData[0].startDate, "dd-MM-yyyy");
                       var stopdate=  $filter('date')(StopTransactionData[0].stopDate, "dd-MM-yyyy");
                       growl.warning("Stop Transation is in Process for this Item(s) <br/>Start Date : "+ startdate +" <br/> Stop Date: "+ stopdate +" <br/> Days: "+ StopTransactionData[0].days +" <br/>  Hours: "+ StopTransactionData[0].hours +" <br/>  Minutes: "+ StopTransactionData[0].minutes +"  ");                   
                       $rootScope.stopLoader(); 
                       self.model.selectedStopIds = [];
                       self.model.SelectedItems = [];
                       return false;
               }
              else{
            	  ActualSaveApproval();
              }
     	  });
         */}
    }
    var ActualSaveApproval = function () {      
        var URI = CONSTANTS.INVENTORY.SCRAP.WITNESS_APPROVAL.SAVE_API + commonDetailService.getDataId();
        var data = {
          "statusId": setStatus('Closed'),
       // start code for HIS-8553 Scrap witness approval does not reduce the quantity on store stock dashboard
          "storeId": self.model.witnessDetailData.storeId,
       // end code for HIS-8553 Scrap witness approval does not reduce the quantity on store stock dashboard
          "items": [],
          "unitId":$scope.unitId,
          "organizationId":$scope.organizationId,
          "userId": $scope.userId
        }

        $rootScope.startLoader();
        angular.forEach(self.model.SelectedItems, function (item) {
          if(!item.approvedQuantity){
            item.approvedQuantity = item.approvedDisposedQty;
          }
          var newItem = {
            "id": item.id,
            "approvedDisposedQuantity": parseInt(item.approvedQuantity) ? item.approvedQuantity : 0,
            "rejectQuantity": parseInt(item.rejectedQuantity),
            "rejectReasonId": self.model.rejectReasonId ? self.model.rejectReasonId : 1,
            "rejectNote": self.model.rejectNote ? self.model.rejectNote : "default note",
            "statusId": item.status ? item.status : 6,
            "batchId": item.batchId ? item.batchId : undefined
          }

          if (item.approvedQuantity > 0 && item.approvedQuantity < item.disposeQuantity) {
            newItem.statusId = setStatus('Partially Approved');
          }
          else if (item.rejectedQuantity >= item.disposeQuantity) {
            newItem.statusId = setStatus('Rejected');
          }
          else if (item.rejectedQuantity > 0 && item.rejectedQuantity < item.disposeQuantity) {
            newItem.statusId = setStatus('Partially Approved');
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

    var ValidateApproval = function () {
      var isValid = true;

      if (!self.model.SelectedItems || self.model.SelectedItems.length == 0) {
        growl.error('Select Items to Update and Save');
        isValid = false;
      }

      angular.forEach(self.model.SelectedItems, function(data){
        if(data.approvedQuantity == undefined){
          data.approvedQuantity = data.approvedDisposedQty;
        }
        if(data.approvedQuantity == undefined || data.approvedQuantity == null){
          growl.error("Please enter approved disposed quantity");
          isValid = false;
        }
      });
      if(!isValid)
      {
      $scope.saveBtnDisabled=false;
      }
      return isValid;
    }

    var NavigateToBack = function () {
      $state.go('witnessApproval');
    }
    var ModalDismissClose = function (popupName) {
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
      self.model.onclose.isReject = false;
      self.model.onclose.approvedQuantity = self.model.onclose.disposeQuantity;
      self.model.onclose.rejectedQuantity = 0;
      /* start code for resolved HIS-10576 Store/Inventory- Rejected quantity is not showing on witness approval screen */
      self.model.onclose.rejectQuantity = 0;
      /* end code for resolved HIS-10576 Store/Inventory- Rejected quantity is not showing on witness approval screen */
    }
    var RejectionPopup = function (item) {
      self.model.onclose = item;
      if (item.isReject) {
         itemId = item.id;
        self.model.action = 'Open';
        var popup = angular.element('#qtyaccepatReject');
        popup.modal('show');
        self.model.reasons = self.model.rejectReasons;
      } else {
        CancelRejection(item);
      }
    }
    var CancelRejection = function (item) {
      var prevRequest = self.model.witnessDetailData.items.find(function (prevItem) {
        return prevItem.id == item.id;
      });

      if (prevRequest) {
        item.approvedQuantity = prevRequest.approvedQuantity + prevRequest.rejectQuantity;
        item.rejectQuantity = 0;
        item.rejectNote = prevRequest.rejectNote;
        item.rejectReasonId = prevRequest.rejectReasonId;
        item.statusId = prevRequest.statusId;
      }
    }

    var ModalDismiss = function (popupName) {
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
    }
   
  initializeController();
}

  function config($stateProvider) {
  $stateProvider.state("detailsWitnessApproval", {
    url: "/detailsWitnessApproval",
    templateUrl: 'views/PharmacyInventory/Store/details-witness-approval.html',
    controller: "WitnessApprovalDetails.Controller",
    controllerAs: "vm",
    title: 'Witness Approval'
  });
}

angular
  .module("myApp")
  .config(config)
  .controller("WitnessApprovalDetails.Controller", witnessApprovalDetailsController);
}) ();