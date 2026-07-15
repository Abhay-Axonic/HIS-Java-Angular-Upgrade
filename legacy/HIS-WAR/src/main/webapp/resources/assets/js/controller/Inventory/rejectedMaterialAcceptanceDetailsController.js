(function () {
  "use strict";

  function rejectedMaterialAcceptanceDetailsController($scope, $cookies, CONSTANTS, $rootScope, $http, $state, rejectReasonService, commonDetailService, StatusService, GenericService, $filter, growl,stopTransactionService) {
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
      self.model.quantityChange = quantityChange;
      self.model.ValidateQuantity = ValidateQuantity;
      self.model.ModalDismissClose = ModalDismissClose;
      fromRejectedMaterialAcceptance();
    }

    var quantityChange = function (item) {
      if (!item.acceptedQuantity)
        item.acceptedQuantity = 0;
    }

    function fromRejectedMaterialAcceptance() {
      var id = commonDetailService.getDataId();
      var URI = CONSTANTS.INVENTORY.STORE.REJECTED_MATERIAL_ACCEPTANCE.DETAILS_API + id;
      var data = {};
      $rootScope.startLoader()
      GenericService.serviceAction("GET", URI, data).then(
        function (response) {
          self.model.materialRejectDetail = response.data;
          self.model.materialRejectDetail.items.forEach(function (item) {
            item.isDisabled = true;
            item.acceptedQuantity = item.acceptQuantity;
            item.returnQuantity = item.rejectQuantity;
            if (item.rejectedRejectQuantity){
              item.isReject = true;
            }
            item.uom.find(function(d){
              if(d.uomTypeId == item.uomTypeId){
                  item.uomType = d.uomDetails;
              }
            });
            if(item.isThreeUOMApplicable == false){
              if(item.rejectedRejectQuantity == undefined || item.rejectedRejectQuantity == null || item.rejectedRejectQuantity == ""){
                if(item.uomTypeId ==1){
                  item.receivedQtyCon = (item.rejectQuantity +"*"+ item.uom[0].conversion);
                  item.receivedQtyCalculation = (item.rejectQuantity * item.uom[0].conversion);
                  item.uomUnitData = item.uom[2].uomUnit;
                }
                else if(item.uomTypeId == 2){
                    
                }
                else{
                  item.receivedQtyCon = (item.rejectQuantity);
                  item.receivedQtyCalculation = (item.rejectQuantity);
                  item.uomUnitData = item.uom[2].uomUnit;
                } 
              }
              else{
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
            }
            else{
              if(item.rejectedRejectQuantity == undefined || item.rejectedRejectQuantity == null || item.rejectedRejectQuantity == ""){
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
              else{
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
            }
          });
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
        self.model.materialRejectDetail.items.forEach(function (item) {
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
        self.model.materialRejectDetail.items.forEach(function (item) {
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
        //var index = item.indexOf(item);
        // comment above code and added below code for remove item in selected list by Minesh.
        var index = batches.indexOf(item);
        if (index > -1) {
          batches.splice(index, 1);
        }
      }

      if (self.model.materialRejectDetail.items.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
    }


    var isSelected = function (item) {
      return item.isSelected;
    }

    var ApprovedQuantity = function (item) {
      if (item.acceptedQuantity > item.returnQuantity) {
        item.rejectedRejectQuantity = 0;
        self.model.rejectQty = undefined;
        item.isReject = false;
        item.status = setStatus('Approved');; // Approved.
      } else if (item.acceptedQuantity == 0 || !item.acceptedQuantity) {
        item.rejectedRejectQuantity = item.quantity;
        self.model.rejectQty = item.rejectedRejectQuantity;
        item.isReject = true;
        RejectionPopup(item);
        item.status = setStatus('Rejected');; // Rejected
      } else if (item.acceptedQuantity < item.returnQuantity) {
        item.rejectedRejectQuantity = item.returnQuantity - item.acceptedQuantity;
        self.model.rejectQty = item.rejectedRejectQuantity;
        item.isReject = true;
        item.status = setStatus('Partially Approved');; // Partially Approved
        RejectionPopup(item);
      } else if (item.acceptedQuantity == item.returnQuantity) {
        item.rejectedRejectQuantity = item.returnQuantity - item.acceptedQuantity;
        self.model.rejectQty = item.rejectedRejectQuantity;
        item.isReject = false;
        item.status = setStatus('Approved');; // Approved.
        RejectionPopup(item);
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
            if (self.model.rejectQty > item.returnQuantity) {
              growl.error('Please enter reject quantity less than or equal to the previous reject quantity');
              return false;
            } else if (self.model.rejectQty == item.returnQuantity) {
              item.rejectedRejectQuantity = item.returnQuantity;
              item.acceptedQuantity = item.returnQuantity - item.rejectedRejectQuantity;
              item.rejectReasonId = self.model.rejectReasons[0].id;
              item.statusId = setStatus('Rejected'); // Rejected
            } else if (self.model.rejectQty == 0 || !self.model.rejectQty) {
              item.acceptedQuantity = item.returnQuantity;
              item.rejectedRejectQuantity = 0;
              item.rejectReasonId = self.model.rejectReasons[0].id;
              item.statusId = setStatus('Pending'); // Approved
            } else {
              item.rejectedRejectQuantity = self.model.rejectQty;
              item.rejectNote = self.model.rejectNote;
              item.acceptedQuantity = item.returnQuantity - item.rejectedRejectQuantity;
              item.rejectReasonId = self.model.rejectReasons[0].id;
              item.statusId = setStatus('Partially Approved'); // Partially Approved
            }
            ModalDismiss('qtyaccepatReject');
            $scope.calculateReceivedQty(item);
          }
        });
      }
    }

    function valueOnChange(item, isSelectAll) {
      if (isSelectAll)
        self.model.materialRejectDetail.items.forEach(function (item) {
          if (self.model.selectAllRows)
            item.acceptedQuantity = item.returnQuantity;
          else{
            item.acceptedQuantity = 0;
            item.rejectedRejectQuantity = 0;
          }
        });
      else
        if (item.isSelected)
          item.acceptedQuantity = item.returnQuantity;
        else{
          item.acceptedQuantity = 0;
          item.rejectedRejectQuantity = 0;
        }
    }
    
    $scope.saveDisabled = true;
    $scope.saveBtnDisabled=false;
    var SaveApproval = function () {
      $scope.saveDisabled = false;
      $scope.saveBtnDisabled=true;
    	if (ValidateApproval()) {
    		
    		  ActualSaveApproval();
    		
    		/*
        $scope.saveDisabled = false;
   		 self.model.selectedStopIds = [];
   		 angular.forEach(self.model.SelectedItems, function (item) {        	
       			  self.model.selectedStopIds.push(item.itemId);                       	
       	 });        	
       	 var stopData = {
	                "itemIds": self.model.selectedStopIds,
	                "storeId": self.model.materialRejectDetail.toStoreId	      		 
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
        var partiallyApproved = 0;
        var rejected = 0 ;
        var approved = 0;
        var URI = CONSTANTS.INVENTORY.STORE.REJECTED_MATERIAL_ACCEPTANCE.SAVE_API + commonDetailService.getDataId();
        var data = {
          "statusId": setStatus('Completed'),
          "returnId": self.model.materialRejectDetail.returnId,
          "issueId": self.model.materialRejectDetail.materialIssueId,
          "items": [],
          "unitId":$scope.unitId,
          "organizationId":$scope.organizationId,
          "userId":$scope.userId,
          "storeId":$scope.storeId

        }


        $rootScope.startLoader();
        angular.forEach(self.model.SelectedItems, function (item) {
          var newItem = {
            "id": item.id,
            "batchMapperId": item.batchMapperId,
            "returnDetailId": item.returnDetailId,
            "acceptedQuantity": parseInt(item.acceptedQuantity),
            "rejectQuantity": parseInt(item.rejectedRejectQuantity),
            "rejectReasonId": parseInt(self.model.rejectReason) ? self.model.rejectReason : 1,
            "rejectNote": self.model.rejectNote,
          }

           if (item.acceptedQuantity > 0 && item.acceptedQuantity < item.returnQuantity) {
             //newItem.statusId = setStatus('Partially Approved');
             partiallyApproved++;
           }

           else if (item.rejectQuantity >= item.returnQuantity) {
             //newItem.statusId = setStatus('Rejected');
             rejected++;
           }
           else if (item.acceptedQuantity == item.returnQuantity) {
            //newItem.statusId = setStatus('Partially Approved');
             approved++;

           }

          data.items.push(newItem);
        });
        if (approved == self.model.materialRejectDetail.items.length)
        {
          data.statusId =  setStatus('Completed');
        }
        else
        if (partiallyApproved > 0)
        {
          data.statusId =  setStatus('Partially Completed');
        }
        else
        if (rejected == self.model.materialRejectDetail.items.length)
        {
          data.statusId =  setStatus('Rejected');
        }
        else
        {
          data.statusId =  setStatus('Partially Completed');
        }
        console.log("Approval Status : ",data.statusId);
        var finalApprovalQty=0;
        var finalRejectQty=0;
        var totalInitialQty=0;
        angular.forEach(self.model.materialRejectDetail.items, function (item) {
        	totalInitialQty=totalInitialQty+(item.rejectQuantity!=undefined && item.rejectQuantity!=null ? item.rejectQuantity :0)
        	finalApprovalQty=finalApprovalQty+(item.acceptedQuantity!=undefined && item.acceptedQuantity!=null ? item.acceptedQuantity :0 )
        	finalRejectQty=finalRejectQty+(item.rejectedRejectQuantity!=undefined && item.rejectedRejectQuantity!=null ? item.rejectedRejectQuantity :0 )
        });
        if(totalInitialQty==finalApprovalQty+finalRejectQty)
        	{
        	data.statusId =  setStatus('Completed');
        	}
        GenericService.serviceAction("PATCH", URI, data).then(
          function (response) {
            $rootScope.stopLoader();
            growl.success(response.data.message);
            NavigateToBack();
            $scope.saveDisabled = true;
          }, function (err) {
            $rootScope.stopLoader();
            growl.error('Something Went Wrong');
          });      
    }

    var ValidateApproval = function () {
      var isValid = true;

      if (!self.model.SelectedItems || self.model.SelectedItems.length == 0) {
        growl.error('Select Items to Update and Save');
        isValid = false;
        $scope.saveDisabled = true;
      }
      self.model.materialRejectDetail.items.forEach(function (item) {
        if (!ValidateQuantity(item)) {
          isValid = false;
          $scope.saveDisabled = true;
        }
      })
                  if(!isValid)
            {
            $scope.saveBtnDisabled=false;
            }
      return isValid;
    }

    var NavigateToBack = function () {
      $state.go('rejectedMaterialAcceptance');
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
      var prevRequest = self.model.materialRejectDetail.items.find(function (prevItem) {
        return prevItem.id == item.id;
      });

      if (prevRequest) {
        item.acceptedQuantity = parseInt(item.acceptedQuantity) + parseInt(item.rejectedRejectQuantity);
        item.rejectedRejectQuantity = 0;
        item.rejectNote = prevRequest.rejectNote;
        item.rejectReasonId = prevRequest.rejectReasonId;
        item.statusId = prevRequest.statusId;
        $scope.calculateReceivedQty(item);
      }
    }


    var ModalDismiss = function (popupName) {
      var popup = angular.element('#' + popupName);
      popup.modal('hide');

    }
    var ModalDismissClose = function (popupName) {
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
      self.model.onclose.isReject = false;
      self.model.onclose.acceptedQuantity = self.model.onclose.rejectQuantity;
      self.model.onclose.rejectedRejectQuantity = 0;
      $scope.calculateReceivedQty(self.model.onclose);
    }

    var ValidateQuantity = function (item) {
      var isValid = true;

      if (parseInt(item.rejectedRejectQuantity) > item.returnQuantity) {
        item.isQuantityValid = true;
        isValid = false;
        growl.error('Entered reject quantity is exceeding the returnQuantity');
      }
      else if (parseInt(item.acceptedQuantity) > item.returnQuantity) {
        item.isQuantityValid = true;
        isValid = false;
        growl.error('Entered approved quantity is exceeding the returnQuantity');
      }

      else {
        item.rejectedRejectQuantity = parseInt(item.rejectedRejectQuantity);
        item.acceptedQuantity = parseInt(item.acceptedQuantity);
        isValid = true;
      }

      return isValid;
    }
    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailsRejectedMaterialAcceptance", {
      url: '/detailsRejectedMaterialAcceptance',
      templateUrl: 'views/PharmacyInventory/InPatient/details-rejected-material-acceptance.html',
      controller: "RejectedMaterialAcceptanceDetails.Controller",
      controllerAs: 'vm',
      title: 'Rejected Material Acceptance'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("RejectedMaterialAcceptanceDetails.Controller", rejectedMaterialAcceptanceDetailsController)
})();
