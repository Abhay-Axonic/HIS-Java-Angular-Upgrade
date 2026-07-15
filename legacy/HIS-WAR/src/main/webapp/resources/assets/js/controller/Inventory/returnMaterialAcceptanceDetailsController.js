(function () {
  "use strict";

  function returnMaterialAcceptanceDetailsController($scope, $rootScope, $http, $state, $cookies, CONSTANTS, rejectReasonService, commonDetailService, StatusService, GenericService, $filter, growl,stopTransactionService) {
    var self = this;
    var selectItemList = [];
    var masterId;
    var mreturnId;
    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
    function initializeController() {
      $rootScope.loginpage = true;
      self.model = {
        SelectedItems: [],
        selectItemList: [],
        selectAllRows: [],
        remark:''

      };
      StatusService.GetStatus().then(function (status) {
        self.model.statuses = status;
      });
      rejectReasonService.GetReason().then(function (reason) {
        self.model.rejectReasons = reason;
      });
      self.model.fromMaterialReturnAcceptance = fromMaterialReturnAcceptance;
      self.model.SelectAllItems = SelectAllItems;
      self.model.SelectItem = SelectItem;
      self.model.setStatus = setStatus;
      self.model.isSelected = isSelected
      self.model.isDisable = isDisable;
      self.model.setStatus = setStatus;
      self.model.valueOnChange = valueOnChange;
      self.model.ApprovedQuantity = ApprovedQuantity;
      self.model.ValidateRejection = ValidateRejection;
      self.model.RejectionPopup = RejectionPopup;
      self.model.CancelRejection = CancelRejection;
      self.model.ModalDismiss = ModalDismiss;
      self.model.SaveAcceptReject = SaveAcceptReject;
      self.model.SaveApproval = SaveApproval;
      self.model.ValidateApproval = self.model.ValidateApproval;
      self.model.NavigateToBack = NavigateToBack;
      self.model.ModalDismissClose = ModalDismissClose;
      self.model.SetFocus = SetFocus;
      self.model.BarcodeScan = BarcodeScan;


      fromMaterialReturnAcceptance();
    }

    function setStatus(x) {

      var status = self.model.statuses.find(function (obj) {
        return obj.status == x;
      });

      return status.id;
    }
	   var SetFocus = function () {
	        self.model.barcode = undefined;
	        $window.document.getElementById('barcode').focus();
	    }
	    var BarcodeScan = function (event, barcode) {
	        var enterCode = 13;
	        if (!barcode && event.which === enterCode) {
	            barcode = self.model.barcode;
	        }

	        if (!!barcode) {
	            var itemId = barcode.split(' ')[0];
	            var batchId = barcode.split(' ')[1];

	            if (!!itemId && !!batchId) {
	                //AddBatchToDetail(self.model.materialPickerDetail, itemId, batchId);
	            }

	            event.stopPropagation();
	            event.preventDefault();

	            SetFocus();
	        }
	    }
    function fromMaterialReturnAcceptance() {
      var id = commonDetailService.getDataId();
      var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_RETURN_ACCEPTANCE.DETAIL_API +$scope.unitId + '/' + id; 
      var data = {};
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, data).then(
        function (response) {
          self.model.materialReturnDetail = response.data;
          if(self.model.materialReturnDetail.items) {
            self.model.materialReturnDetail.items.forEach(function (item) {
              item.isDisabled = true;
              item.acceptedQuantity = item.acceptQuantity;
              item.rejectedQuantity = item.rejectQuantity;
              if (item.rejectQuantity)
                item.isReject = true;
              item.originalRejectedQuantity = item.rejectQuantity;
              item.originalAcceptedQuantity = item.acceptQuantity;
              item.uom.find(function(d){
                if(d.uomTypeId == item.uomTypeId){
                  item.uomType = d.uomDetails;
                }
              });
              if(item.isThreeUOMApplicable == false){
                if(self.model.materialReturnDetail.approvalStatus == 'Approved'){
                  if(item.uomTypeId ==1){
                    item.receivedQtyCon = (item.acceptedQuantity +"*"+ item.uom[0].conversion);
                    item.receivedQtyCalculation = (item.acceptedQuantity * item.uom[0].conversion);
                    item.uomUnitData = item.uom[2].uomUnit;
                  }
                  else if(item.uomTypeId == 2){
                      
                  }
                  else{
                    item.receivedQtyCon = (item.acceptedQuantity);
                    item.receivedQtyCalculation = (item.acceptedQuantity);
                    item.uomUnitData = item.uom[2].uomUnit;
                  }
                }
                else{
                  if(item.uomTypeId ==1){
                    item.receivedQtyCon = (item.returnQuantity +"*"+ item.uom[0].conversion);
                    item.receivedQtyCalculation = (item.returnQuantity * item.uom[0].conversion);
                    item.uomUnitData = item.uom[2].uomUnit;
                  }
                  else if(item.uomTypeId == 2){
                          
                  }
                  else{
                    item.receivedQtyCon = (item.returnQuantity);
                    item.receivedQtyCalculation = (item.returnQuantity);
                    item.uomUnitData = item.uom[2].uomUnit;
                  }
                }
              }
              else{
                if(self.model.materialReturnDetail.approvalStatus == 'Approved'){
                  if(item.uomTypeId ==1){
                    item.receivedQtyCon = (item.acceptedQuantity +"*"+ item.uom[0].conversion +"*"+ item.uom[1].conversion);
                    item.receivedQtyCalculation = (item.acceptedQuantity * item.uom[0].conversion * item.uom[1].conversion);
                    item.uomUnitData = item.uom[2].uomUnit;
                  }
                  else if(item.uomTypeId == 2){
                    item.receivedQtyCon = (item.acceptedQuantity +"*"+ item.uom[1].conversion);
                    item.receivedQtyCalculation = (item.acceptedQuantity * item.uom[1].conversion);
                    item.uomUnitData = item.uom[2].uomUnit;
                  }
                  else{
                    item.receivedQtyCon = (item.acceptedQuantity);
                    item.receivedQtyCalculation = (item.acceptedQuantity);
                    item.uomUnitData = item.uom[2].uomUnit;
                  }
                }
                else{
                  if(item.uomTypeId ==1){
                    item.receivedQtyCon = (item.returnQuantity +"*"+ item.uom[0].conversion +"*"+ item.uom[1].conversion);
                    item.receivedQtyCalculation = (item.returnQuantity * item.uom[0].conversion * item.uom[1].conversion);
                    item.uomUnitData = item.uom[2].uomUnit;
                  }
                  else if(item.uomTypeId == 2){
                    item.receivedQtyCon = (item.returnQuantity +"*"+ item.uom[1].conversion);
                    item.receivedQtyCalculation = (item.returnQuantity * item.uom[1].conversion);
                    item.uomUnitData = item.uom[2].uomUnit;
                  }
                  else{
                    item.receivedQtyCon = (item.returnQuantity);
                    item.receivedQtyCalculation = (item.returnQuantity);
                    item.uomUnitData = item.uom[2].uomUnit;
                  }
                }
              }
            });
        }
          $rootScope.stopLoader();
        });
    }

    $scope.calculateReceivedQty = function (item) {
      if(item.acceptedQuantity == undefined || item.acceptedQuantity == null || item.acceptedQuantity == ""){
        item.acceptedQuantity = 0;
      }
      if(item.isThreeUOMApplicable == false){
        if(item.uomTypeId == 1){
          item.receivedQtyCon = (item.acceptedQuantity +"*"+ item.uom[0].conversion);
          item.receivedQtyCalculation = (item.acceptedQuantity * item.uom[0].conversion);
          item.uomUnitData = item.uom[2].uomUnit;
        }
        else if(item.uomTypeId == 2){
        
        }
        else{
          item.receivedQtyCon = (item.acceptedQuantity);
          item.receivedQtyCalculation = (item.acceptedQuantity);
          item.uomUnitData = item.uom[2].uomUnit;
        }
      }
      else{
        if(item.uomTypeId == 1){
          item.receivedQtyCon = (item.acceptedQuantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
          item.receivedQtyCalculation = (item.acceptedQuantity * item.uom[0].conversion * item.uom[1].conversion);
          item.uomUnitData = item.uom[2].uomUnit;
        }
        else if(item.uomTypeId == 2){
          item.receivedQtyCon = (item.acceptedQuantity +"*"+ item.uom[1].conversion);
          item.receivedQtyCalculation = (item.acceptedQuantity * item.uom[1].conversion);
          item.uomUnitData = item.uom[2].uomUnit;
        }
        else{
          item.receivedQtyCon = (item.acceptedQuantity);
          item.receivedQtyCalculation = (item.acceptedQuantity);
          item.uomUnitData = item.uom[2].uomUnit;
        }
      }
    }

    var SelectAllItems = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedRequestIds = [];
        self.model.SelectedItems = [];
        self.model.materialReturnDetail.items.forEach(function (mreturn) {
          if(mreturn.rejectedQuantity == mreturn.returnQuantity){
            mreturn.statusId = setStatus('Rejected');
          }
          else if(mreturn.rejectedQuantity == 0 || !mreturn.rejectedQuantity){
            mreturn.statusId = setStatus('Pending Approval');
          }
          else{
            mreturn.statusId = setStatus('Partially Approved');
          }
          if (!!isDisable(mreturn)) {
            mreturn.isSelected = true;
            self.model.SelectedRequestIds.push(mreturn.id);
            mreturn.isDisabled = false;
            self.model.SelectedItems.push(mreturn);
          }
        });
      } else {
        self.model.SelectedRequestIds = [];
        self.model.SelectedItems = [];
        self.model.materialReturnDetail.items.forEach(function (mreturn) {
          if(mreturn.rejectedQuantity == mreturn.returnQuantity){
            mreturn.statusId = setStatus('Rejected');
          }
          else if(mreturn.rejectedQuantity == 0 || !mreturn.rejectedQuantity){
            mreturn.statusId = setStatus('Pending Approval');
          }
          else{
            mreturn.statusId = setStatus('Partially Approved');
          }
          mreturn.isSelected = false;
          mreturn.isDisabled = true;
        });
      }
    }

    var SelectItem = function (mreturn) {
      var mreturns = self.model.SelectedItems;
      if(mreturn.rejectedQuantity == mreturn.returnQuantity){
        mreturn.statusId = setStatus('Rejected');
      }
      else if(mreturn.rejectedQuantity == 0 || !mreturn.rejectedQuantity){
        mreturn.statusId = setStatus('Pending Approval');
      }
      else{
        mreturn.statusId = setStatus('Partially Approved');
      }
      if (mreturn.isSelected) {
        mreturns.push(mreturn);
        mreturn.isDisabled = false;
      } else {
        mreturn.isDisabled = true;
        mreturn.isReject = false;
        var index = mreturns.indexOf(mreturn);
        if (index > -1) {
          mreturns.splice(index, 1);
        }
      }

      if (self.model.materialReturnDetail.items.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
      if(self.model.SelectedItems.length>0)
      {
        document.getElementById("saveButton").enabled;
        console.log("Button Enabled");
      }
      else
      {
        document.getElementById("saveButton").disabled;
        console.log("Button Disabled");
      }
    }
    var isDisable = function (items) {
      if (items.statusId == 10) {
        return true;
      }
      return false;
    }
    var isSelected = function (mreturn) {
      return mreturn.isSelected;
    }


    function valueOnChange(mreturn, isSelectAll) {
      if (isSelectAll)
        self.model.materialReturnDetail.items.forEach(function (mreturn) {
          if (self.model.selectAllRows) {
            mreturn.acceptedQuantity = mreturn.returnQuantity;
            mreturn.rejectedQuantity = 0;
            mreturn.isReject = false;
          } else{
            mreturn.acceptedQuantity = 0;
            mreturn.rejectedQuantity = 0;
          }
        });
      else {
        if (mreturn.isSelected) {
          mreturn.acceptedQuantity = mreturn.returnQuantity;
          mreturn.rejectedQuantity = 0;
          mreturn.isReject = false;
        }
        else {
          mreturn.acceptedQuantity = 0;
          mreturn.rejectedQuantity = 0;
        }
      }
    }


    var ApprovedQuantity = function (mreturn) {
      if (mreturn.acceptedQuantity > mreturn.returnQuantity) {
        //Invalid
        mreturn.rejectedQuantity = 0;
        self.model.rejectQty = undefined;
        mreturn.isReject = false;
        mreturn.acceptedQuantity = '';
        growl.error("Accept Return Qty can not be greater than Returned Qty");
       // mreturn.status = setStatus('Approved');; // Approved.
      } else if (mreturn.acceptedQuantity == 0 || !mreturn.acceptedQuantity) {
        mreturn.rejectedQuantity = mreturn.returnQuantity;
        self.model.rejectQty = mreturn.rejectedQuantity;
        mreturn.isReject = true;
        RejectionPopup(mreturn);
        mreturn.status = setStatus('Rejected');; // Rejected
      } else if (mreturn.acceptedQuantity < mreturn.returnQuantity) {
        mreturn.rejectedQuantity = mreturn.returnQuantity - mreturn.acceptedQuantity;
        self.model.rejectQty = mreturn.rejectedQuantity;
        mreturn.isReject = true;
        RejectionPopup(mreturn);
        mreturn.status = setStatus('Partially Approved');; // Partially Approved
      } else if (mreturn.acceptedQuantity == mreturn.returnQuantity) {
        mreturn.rejectedQuantity = mreturn.returnQuantity - mreturn.acceptedQuantity;
        self.model.rejectQty = mreturn.rejectedQuantity;
        mreturn.isReject = false;
        RejectionPopup(mreturn);
        mreturn.status = setStatus('Approved');; // Approved.
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

    var RejectionPopup = function (mreturn) {
      self.model.onclose = mreturn;
      if (mreturn.isReject) {
        mreturnId = mreturn.id;
        self.model.action = 'Open';
        var popup = angular.element('#qtyaccepatReject');
        popup.modal('show');
        self.model.reasons = self.model.rejectReasons;
      } else {
        CancelRejection(mreturn);
      }
    }

    var CancelRejection = function (mreturn) {
      var prevRequest = self.model.materialReturnDetail.items.find(function (prevItem) {
        return prevItem.id == mreturn.id;
      });

      if (prevRequest) {
        mreturn.acceptedQuantity = parseInt(prevRequest.acceptedQuantity) + parseInt(prevRequest.rejectedQuantity);
        mreturn.rejectedQuantity = 0;
        mreturn.rejectNote = prevRequest.rejectNote;
        mreturn.rejectReasonId = prevRequest.rejectReasonId;
        mreturn.statusId = prevRequest.statusId;
        $scope.calculateReceivedQty(mreturn);
      }
    }


    var ModalDismiss = function (popupName) {
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
    }

    var ModalDismissClose = function (popupName) {
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
      console.log("Item : ",self.model.onclose);
      self.model.onclose.isReject = false;
      self.model.onclose.rejectedQuantity = self.model.onclose.originalRejectedQuantity;
      self.model.onclose.acceptedQuantity = self.model.onclose.returnQuantity-self.model.onclose.originalAcceptedQuantity;
      $scope.calculateReceivedQty(self.model.onclose);
      //self.model.rejectQty = 0;
     // self.model.onclose.rejectedQuantity = 0;
     // self.model.onclose.acceptedQuantity = self.model.onclose.acceptedQuantity-self.model.onclose.rejectedQuantity ;


    }

    var SaveAcceptReject = function () {
      if (ValidateRejection()) {
        var rejectItemId = mreturnId;
        self.model.SelectedItems.forEach(function (mreturn) {
          if (mreturn.id == rejectItemId) {
            if (self.model.rejectQty > mreturn.returnQuantity) {
              growl.error('Please Enter Recject less than or  equal to return quantity');
              return false;
            } else if (self.model.rejectQty == mreturn.returnQuantity) {
              mreturn.rejectedQuantity = mreturn.returnQuantity;
              mreturn.acceptedQuantity = mreturn.returnQuantity - mreturn.rejectedQuantity;
              mreturn.rejectReasonId = self.model.rejectReasons[0].id;
              mreturn.statusId = setStatus('Rejected'); // Rejected
            } else if (self.model.rejectQty == 0 || !self.model.rejectQty) {
              mreturn.acceptedQuantity = mreturn.returnQuantity;
              mreturn.rejectedQuantity = 0;
              mreturn.rejectReasonId = self.model.rejectReasons[0].id;
              mreturn.statusId = setStatus('Pending'); // Approved
            } else {
              mreturn.rejectedQuantity = self.model.rejectQty;
              mreturn.rejectNote = self.model.rejectNote;
              mreturn.acceptedQuantity = mreturn.returnQuantity - mreturn.rejectedQuantity;
              mreturn.rejectReasonId = self.model.rejectReasons[0].id;
              mreturn.statusId = setStatus('Partially Approved'); // Partially Approved
            }
            ModalDismiss('qtyaccepatReject');
            $scope.calculateReceivedQty(mreturn);
          }
        });
      }
    }

    $scope.saveDisabled = true;
    $scope.saveBtnDisabled=false;
    var SaveApproval = function () {
        $scope.saveBtnDisabled=true;
      $scope.saveDisabled = false;
    	 if (ValidateApproval()) {
    		 $scope.saveDisabled = false;
    		 ActualSaveApproval();
    		 
    		 /*
        $scope.saveDisabled = false;
    		 self.model.selectedStopIds = [];
    		 angular.forEach(self.model.SelectedItems, function (item) {         	
        			  self.model.selectedStopIds.push(item.itemId);                       	
        	 });        	
        	 var stopData = {
 	                "itemIds": self.model.selectedStopIds,
 	                "storeId": self.model.materialReturnDetail.toStoreId	      		 
 	              };  	    
  	 	  stopTransactionService.GetstopTransactionData(stopData).then(function(responseData){        
           var StopTransactionData = responseData;  
           if(StopTransactionData.length > 0 ){    
                    var startdate =  $filter('date')(StopTransactionData[0].startDate, "dd-MM-yyyy");
                    var stopdate=  $filter('date')(StopTransactionData[0].stopDate, "dd-MM-yyyy");
                    growl.warning("Stop Transation is in Process for this Item(s) <br/>Start Date : "+ startdate +" <br/> Stop Date: "+ stopdate +" <br/> Days: "+ StopTransactionData[0].days +" <br/>  Hours: "+ StopTransactionData[0].hours +" <br/>  Minutes: "+ StopTransactionData[0].minutes +"  ");                   
                    $rootScope.stopLoader(); 
                    self.model.selectedStopIds = []; 
                    return false;
            }
           else{
        	   ActualSaveApproval();
           }
        });
    	 */}
    }
    var ActualSaveApproval = function () {
      $scope.saveDisabled = false;
      var pending = 0;
      var approved = 0;
      var reject = 0;
      var partial = 0;
      if (ValidateApproval()) {
        $scope.saveDisabled = false;
        var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_RETURN_ACCEPTANCE.APPROVE_API + commonDetailService.getDataId();
        var data = {
          "statusId": setStatus('Closed'),
          "approvalStatusId": setStatus('Rejected'),
          "items": [],
          "unitId":$scope.unitId,
          "userId":$scope.userId,
          "storeId":$scope.storeId

        }
        data.remark=self.model.remark;
        $rootScope.startLoader();
        angular.forEach(self.model.SelectedItems, function (item) {
          var newItem = {
            "id": item.id,
            "acceptedQuantity": parseInt(item.acceptedQuantity) ? parseInt(item.acceptedQuantity) : 0,
            "rejectedQty": parseInt(item.rejectedQuantity) ? item.rejectedQuantity : 0,
            "rejectReasonId": self.model.rejectReasonId ? self.model.rejectReasonId : 1,
            "rejectNote": self.model.rejectNote,
            "statusId": item.status ? item.status : 6
          }
          if (item.acceptedQuantity == item.returnQuantity) {
            newItem.statusId = setStatus('Approved');
            item.statusId = setStatus('Approved');
          }
          else if (item.acceptedQuantity > 0 && item.acceptedQuantity < item.returnQuantity) {
            newItem.statusId = setStatus('Partially Approved');
            item.statusId = setStatus('Partially Approved');
          }
          else {
            newItem.statusId = setStatus('Partially Approved');
            item.statusId = setStatus('Partially Approved');
          }
          data.items.push(newItem);
        });
        angular.forEach(self.model.materialReturnDetail.items, function (item) {
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
            $scope.saveDisabled = true;
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
        $scope.saveDisabled = true;
      }
      else if (self.model.SelectedItems) {
        angular.forEach(self.model.SelectedItems, function (item) {
          if (parseInt(item.acceptedQuantity) > item.returnQuantity) {
            growl.error('Please Enter Valid Accept Quantity')
            isValid = false;
            $scope.saveDisabled = true;
          }
        });
      }

      if(self.model.SelectedItems.length != self.model.materialReturnDetail.items.length){
        growl.error('Please Accept/Reject the quantity for all items');
        isValid = false;
        $scope.saveDisabled = true;
      }
      if(!isValid)
      {
      $scope.saveBtnDisabled=false;
      }
      return isValid;
    }

    var NavigateToBack = function () {
      $state.go('materialReturnAcceptance');
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailsMaterialReturnAcceptance", {
      url: '/detailsMaterialReturnAcceptance',
      templateUrl: 'views/PharmacyInventory/InPatient/details-material-return-acceptance.html',
      controller: "ReturnMaterialAcceptanceDetails.Controller",
      controllerAs: 'vm',
      title: 'Material Return Acceptance'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("ReturnMaterialAcceptanceDetails.Controller", returnMaterialAcceptanceDetailsController)
})();
