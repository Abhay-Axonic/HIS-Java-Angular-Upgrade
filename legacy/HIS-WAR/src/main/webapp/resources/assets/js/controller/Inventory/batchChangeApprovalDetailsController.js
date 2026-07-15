(function () {
  "use strict";

  function batchChangeApprovalDetailsController($scope, $rootScope, $http, $cookies, CONSTANTS, StatusService, CurrencyService, GenericService, rejectReasonService, $state, commonDetailService, growl,stopTransactionService,$filter) {
    var self = this;
    var batchId;
    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
      $rootScope.loginpage = true;
      self.model = {
        selectAllRows: false,
        SelectedItems: [],
        SelectedRequestIds: []


      };
      CurrencyService.GetCurrency().then(function (currency) {
        self.model.Currency = currency;
      })
      rejectReasonService.GetReason().then(function (reason) {
        self.model.rejectReasons = reason;
      });
      StatusService.GetStatus().then(function (status) {
        self.model.statuses = status;
      });

      self.model.fromBatchChangeApproval = fromBatchChangeApproval;
      self.model.SelectAllRequests = SelectAllRequests;
      self.model.SelectBatch = SelectBatch;
      self.model.isDisable = isDisable;
      self.model.isSelected = isSelected;
      self.model.RejectionPopup = RejectionPopup;
      self.model.SaveApproval = SaveApproval;
      self.model.NavigateToBack = NavigateToBack;
      self.model.setStatus = setStatus;
      self.model.ApproveBatchChange = ApproveBatchChange;
      self.model.ModalDismiss = ModalDismiss;
      self.model.ModalDismissClose = ModalDismissClose;

      fromBatchChangeApproval();
    }


    function fromBatchChangeApproval() {
      var id = commonDetailService.getDataId();
      var URI = CONSTANTS.INVENTORY.STORE.BATCH_CHANGE_APPROVAL.DETAILS_API + id;
      var data = {};
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, data).then(
        function (response) {
          self.model.batchChangeApprovalDetail = response.data;
          self.model.requestItems = JSON.parse(JSON.stringify(response.data));
          self.model.batchChangeApprovalDetail.batch.forEach(function (batch) {
            batch.isDisabled = true;
            if (batch.statusId == 7)
              batch.isReject = true;
            batch.oldMrp = batch.oldMrp.toFixed(2);
            batch.newMrp = batch.newMrp.toFixed(2);
            // if (batch.mrpUomTypeId == 1) {
            //   batch.mrpUomType = "P - Box"
            // }
            // else if (batch.mrpUomTypeId == 2) {
            //   batch.mrpUomType = "S - Strip"
            // }
            // else if (batch.mrpUomTypeId == 3) {
            //   batch.mrpUomType = "D - Tablet"
            // }
            batch.uomList.find(function(d){
              if(d.uomType == batch.uomType){
                batch.uomType = d.uomDetails;
              }
              if(batch.mrpUomTypeId == d.uomTypeId){
                batch.mrpUomType = d.uomDetails;
              }
            });
          });
          $rootScope.stopLoader();
        });
    }

    var isSelected = function (batch) {
      if (!isDisable(batch))
        return batch.isSelected;
      else
        return true;
    }

    var isDisable = function (batch) {
      if (batch.statusId != 10) {
        return true;
      }
      return false;
    }

    var SelectBatch = function (batch) {

      var batches = self.model.SelectedItems;

      if (batch.isSelected) {
        batches.push(batch);
        batch.isDisabled = false;
      } else {
        batch.isDisabled = true;
        var index = batches.indexOf(batch);
        if (index > -1) {
          batches.splice(index, 1);
        }
      }

      if (self.model.batchChangeApprovalDetail.batch.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
    }
    var SelectAllRequests = function () {

      if (self.model.selectAllRows) {
        self.model.SelectedRequestIds = [];
        self.model.batchChangeApprovalDetail.batch.forEach(function (batch) {
          if (!isDisable(batch)) {
            batch.isSelected = true;
            batch.isDisabled = false;
            self.model.SelectedRequestIds.push(batch.id);
            self.model.SelectedItems.push(batch);
          }
        });
      } else {
        self.model.SelectedRequestIds = [];
        self.model.SelectedItems = [];
        self.model.batchChangeApprovalDetail.batch.forEach(function (batch) {
          batch.isSelected = false;
          batch.isDisabled = true;
        });
      }
    }

    var ValidateRejection = function () {
      var isValid = true;
      if (!self.model.cancelReason) {
        isValid = false;
        growl.error('Please Select Reason to Reject');
      }

      return isValid;
    }
    function setStatus(x) {

      var status = self.model.statuses.find(function (obj) {
        return obj.status == x;
      });

      return status.id;
    }


    var ValidateApproval = function () {
      var isValid = true;

      if (!self.model.SelectedItems || self.model.SelectedItems.length == 0) {
        growl.error('Select Items to Approve');
        isValid = false;
      }
      return isValid;
    }

    var SaveApproval = function () {
    	
     	ActualSaveApproval();
    	
    	/*
        if (ValidateApproval()) {
        	self.model.stopItemIds = [];
            angular.forEach(self.model.batchChangeApprovalDetail.batch, function (item, key) {                     
               	 	self.model.stopItemIds.push(item.itemId);                         
             });
       	 var stopData = {
  	                "itemIds": self.model.stopItemIds,
  	                "storeId": self.model.batchChangeApprovalDetail.storeId 	      		 
  	              };      	    
      	 	stopTransactionService.GetstopTransactionData(stopData).then(function(responseData){        
               var StopTransactionData = responseData;  
               if(StopTransactionData.length > 0 ){    
                        var startdate =  $filter('date')(StopTransactionData[0].startDate, "dd-MM-yyyy");
                        var stopdate=  $filter('date')(StopTransactionData[0].stopDate, "dd-MM-yyyy");
                        growl.warning("Stop Transation is in Process for this Item(s) <br/>Start Date : "+ startdate +" <br/> Stop Date: "+ stopdate +" <br/> Days: "+ StopTransactionData[0].days +" <br/>  Hours: "+ StopTransactionData[0].hours +" <br/>  Minutes: "+ StopTransactionData[0].minutes +"  ");                    
                        $rootScope.stopLoader(); 
                        self.model.stopItemIds = [];
                        return false;
                }
               else{
               	ActualSaveApproval();
               }
      	  });
        }    	
    */}
    var ActualSaveApproval = function () {
      var pending = 0;
      var approved = 0;
      var reject = 0;
      var partial = 0;
      if (ValidateApproval()) {
        var id = commonDetailService.getDataId();
        var URI = CONSTANTS.INVENTORY.STORE.BATCH_CHANGE_APPROVAL.BATCH_APPROVE_API + id;
        var data = {
          "statusId": setStatus('Closed'),
          "approvalStatusId": setStatus('Rejected'),
          "batch": [],
          "unitId":$scope.unitId,
          "organizationId":$scope.organizationId,
          "userId":$scope.userId,

        };

        var is_allapproved = true;
        angular.forEach(self.model.batchChangeApprovalDetail.batch, function (item, key) {
          if (item.isSelected == true) {
            var storeItem = {
              "id": item.id,
              "rejectReasonId": item.cancelReason ? item.cancelReason : undefined,
              "rejectNote": item.cancelNote ? item.cancelNote : undefined,
              "statusId": (item.isReject ? setStatus('Rejected') : setStatus('Approved'))
            }
          }
          else {
            var storeItem = {
              "id": item.id,
              "rejectReasonId": item.cancelReason ? item.cancelReason : 1,
              "rejectNote": item.cancelNote ? item.cancelNote : undefined,
              "statusId": setStatus('Rejected')
            }
          }
          if (item.isReject) {
            item.statusId = setStatus('Rejected');
          }
          else {
            item.statusId = setStatus('Approved')
          }
          // if (data.approvalStatusId == setStatus('Rejected') && !item.isReject) {
          //   data.approvalStatusId = setStatus('Partially Approved');
          // }
          // else if (is_allapproved && item.isReject) {
          //   is_allapproved = false;
          // }
          // else if (is_allapproved == false) {
          //   data.statusId = setStatus('Closed');
          //   data.approvalStatusId = setStatus('Partially Approved')

          // }

          data.batch.push(storeItem);
        });


        angular.forEach(self.model.batchChangeApprovalDetail.batch, function (item) {
          if (item.statusId == setStatus('Rejected')) {
            reject++;
          }
          else if (item.statusId == setStatus('Approved')) {
            approved++;
          }
        });

        if (approved > 0 && reject > 0 ) {
          data.approvalStatusId = setStatus('Partially Approved');
        }
        else if (reject == 0) {
          if (approved > 0 || partial > 0) {
            data.approvalStatusId = setStatus('Approved');
          }
          else {
            data.approvalStatusId = setStatus('Rejected');
            data.statusId = setStatus('Closed');
          }
        }



        // if (is_allapproved) {
        //   data.approvalStatusId = setStatus('Approved');
        //   data.statusId = setStatus('Closed');
        // }
        // else if (data.approvalStatusId == setStatus('Reject')) {
        //   data.statusId = setStatus('Closed');
        // }
        $rootScope.startLoader();
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

    var NavigateToBack = function () {
      $state.go('batchExpDateChangeApproval');
    }
    var RejectionPopup = function (batch) {
      self.model.cancelReason = undefined;
      self.model.cancelNote = '';

      self.model.onclose = batch;
      self.model.onSelectedItem = batch;
      if (batch.isReject) {
        batchId = batch.id;
        self.model.action = 'Open';
        var popup = angular.element('#confirmCancel');
        popup.modal('show');
        self.model.reasons = self.model.rejectReasons;
      } else {
        CancelRejection(batch);
      }
    }



    var CancelRejection = function (item) {
      // self.model.onclose.isReject = false;
      var prevRequest = self.model.requestItems.items.find(function (prevItem) {
        return prevItem.id == item.id;
      });

      if (prevRequest) {
        item.rejectNote = prevRequest.rejectNote;
        item.rejectReasonId = prevRequest.rejectReasonId;
        item.statusId = prevRequest.statusId;
      }
    }
    var ApproveBatchChange = function (item) {
      if (ValidateRejection()) {
        if (item.isReject) {
          item.cancelReason = self.model.cancelReason;
          item.cancelNote = self.model.cancelNote;
        }
        ModalDismiss('confirmCancel')
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
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailBatchChangeApproval", {
      url: "/detailBatchChangeApproval",
      templateUrl: 'views/PharmacyInventory/Store/details-batch-change-approval.html',
      controller: "BatchChangeApprovalDetails.Controller",
      controllerAs: "vm",
      title: 'Batch/MRP/Expiry Date Change Approval'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("BatchChangeApprovalDetails.Controller", batchChangeApprovalDetailsController);
})();