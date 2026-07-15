(function () {
  "use strict";

  function scrapRequestApprovalDetailsController($scope, $rootScope, $http, $state, rejectReasonService, StatusService, commonDetailService, $cookies, CONSTANTS, GenericService, growl,stopTransactionService,$filter) {
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

      self.model.fromScrapRequest = fromScrapRequest;
      self.model.SelectAllItems = SelectAllItems;
      self.model.SelectItem = SelectItem;
      self.model.isDisable = isDisable;
      self.model.isSelected = isSelected;
      self.model.ApprovedQuantity = ApprovedQuantity;
      self.model.SaveAcceptReject = SaveAcceptReject;
      self.model.valueOnChange = valueOnChange;
      self.model.ModalDismiss = ModalDismiss;
      self.model.RejectionPopup = RejectionPopup;
      self.model.CancelRejection = CancelRejection;
      self.model.NavigateToBack = NavigateToBack;
      self.model.setStatus = setStatus;
      self.model.SaveApproval = SaveApproval;
      self.model.ValidateApproval = ValidateApproval;
      self.model.ValidateRejection = ValidateRejection;
      self.model.ModalDismissClose = ModalDismissClose;
      self.model.ValidateQuantity = ValidateQuantity;

      fromScrapRequest();
    }

    function setStatus(x) {

      var status = self.model.statuses.find(function (obj) {
        return obj.status == x;
      });

      return status.id;
    }
    function fromScrapRequest() {
      var id = commonDetailService.getDataId();
      $rootScope.startLoader();
      var URI = CONSTANTS.INVENTORY.SCRAP.SCRAP_REQUEST_APPROVAL.DETAIL_API +$scope.unitId + '/'+ id;
      var data = {};
      GenericService.serviceAction("GET", URI, data).then(
        function (response) {
          self.model.scrapDetailData = response.data;
          $scope.storeId = self.model.scrapDetailData.storeId;
          self.model.scrapDetailData.items.forEach(function (item) {
            self.model.requestItems = JSON.parse(JSON.stringify(response.data));
            item.isDisabled = true;
            if (item.rejectQuantity) {
              item.isReject = true;
            }
            else{
              item.isReject = false;
            }
            item.uomList.find(function(u){
              if(u.uomType == item.uomType){
                item.uomType = u.uomDetails;
              }
            })
            if(item.acceptedQuantity == undefined || item.acceptedQuantity == null || item.acceptedQuantity == 0){
              if(item.isThreeUOMApplicable == false){
                if(item.uomTypeId == 1){
                  item.receivedQtyCon = (item.scrapQuantity +"*"+ item.uomList[0].conversion);
                  item.receivedQtyCalculation = (item.scrapQuantity * item.uomList[0].conversion);
                  item.uomUnitData = item.uomList[2].uomUnit;
                }
                else if(item.uomTypeId == 2){
                    
                }
                else{
                  item.receivedQtyCon = (item.scrapQuantity);
                  item.receivedQtyCalculation = (item.scrapQuantity);
                  item.uomUnitData = item.uomList[2].uomUnit;
                }
              }
              else{
                if(item.uomTypeId == 1){
                  item.receivedQtyCon = (item.scrapQuantity +"*"+ item.uomList[0].conversion +"*"+ item.uomList[1].conversion);
                  item.receivedQtyCalculation = (item.scrapQuantity * item.uomList[0].conversion * item.uomList[1].conversion);
                  item.uomUnitData = item.uomList[2].uomUnit;
                }
                else if(item.uomTypeId == 2){
                  item.receivedQtyCon = (item.scrapQuantity +"*"+ item.uomList[1].conversion);
                  item.receivedQtyCalculation = (item.scrapQuantity * item.uomList[1].conversion);
                  item.uomUnitData = item.uomList[2].uomUnit;
                }
                else{
                  item.receivedQtyCon = (item.scrapQuantity);
                  item.receivedQtyCalculation = (item.scrapQuantity);
                  item.uomUnitData = item.uomList[2].uomUnit;
                }
              }
            }
            else{
              if(item.isThreeUOMApplicable == false){
                if(item.uomTypeId == 1){
                  item.receivedQtyCon = (item.acceptedQuantity +"*"+ item.uomList[0].conversion);
                  item.receivedQtyCalculation = (item.acceptedQuantity * item.uomList[0].conversion);
                  item.uomUnitData = item.uomList[2].uomUnit;
                }
                else if(item.uomTypeId == 2){
                    
                }
                else{
                  item.receivedQtyCon = (item.acceptedQuantity);
                  item.receivedQtyCalculation = (item.acceptedQuantity);
                  item.uomUnitData = item.uomList[2].uomUnit;
                }
              }
              else{
                if(item.uomTypeId == 1){
                  item.receivedQtyCon = (item.acceptedQuantity +"*"+ item.uomList[0].conversion +"*"+ item.uomList[1].conversion);
                  item.receivedQtyCalculation = (item.acceptedQuantity * item.uomList[0].conversion * item.uomList[1].conversion);
                  item.uomUnitData = item.uomList[2].uomUnit;
                }
                else if(item.uomTypeId == 2){
                  item.receivedQtyCon = (item.acceptedQuantity +"*"+ item.uomList[1].conversion);
                  item.receivedQtyCalculation = (item.acceptedQuantity * item.uomList[1].conversion);
                  item.uomUnitData = item.uomList[2].uomUnit;
                }
                else{
                  item.receivedQtyCon = (item.acceptedQuantity);
                  item.receivedQtyCalculation = (item.acceptedQuantity);
                  item.uomUnitData = item.uomList[2].uomUnit;
                }
              }
            }
          });
          $rootScope.stopLoader();
        });
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
        self.model.scrapDetailData.items.forEach(function (item) {
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
        self.model.scrapDetailData.items.forEach(function (item) {
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

      if (self.model.scrapDetailData.items.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
    }


    var isSelected = function (item) {
      return item.isSelected;
    }
    var ApprovedQuantity = function (item) {
      if (item.acceptedQuantity > item.scrapQuantity) {
        item.rejectQuantity = item.scrapQuantity - item.acceptedQuantity;
        item.rejectQuantity = 0;
        self.model.rejectQty = undefined;
        item.isReject = false;
        item.status = setStatus('Approved');; // Approved.
        RejectionPopup(item);
      } else if (item.acceptedQuantity == 0 || !item.acceptedQuantity) {
        item.rejectQuantity = item.scrapQuantity;
        self.model.rejectQty = item.rejectQuantity;
        item.isReject = true;
        item.acceptedQuantity = parseInt(item.acceptedQuantity);
        item.status = setStatus('Rejected');; // Rejected
        RejectionPopup(item);
      } else if (item.acceptedQuantity < item.scrapQuantity) {
        item.acceptedQuantity = parseInt(item.acceptedQuantity);
        item.rejectQuantity = item.scrapQuantity - item.acceptedQuantity;
        self.model.rejectQty = item.rejectQuantity;
        item.isReject = true;
        item.status = setStatus('Partially Approved');; // Partially Approved
        RejectionPopup(item);
      } else if (item.acceptedQuantity == item.scrapQuantity) {
        item.rejectQuantity = item.scrapQuantity - item.acceptedQuantity;
        self.model.rejectQty = item.rejectQuantity;
        item.isReject = false;
        item.acceptedQuantity = parseInt(item.acceptedQuantity);
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
            if (self.model.rejectQty > item.scrapQuantity) {
              growl.error('Please Enter Valid Quantity');
              return false;
            } else if (self.model.rejectQty == item.scrapQuantity) {
              item.rejectQuantity = item.scrapQuantity;
              item.acceptedQuantity = item.scrapQuantity - item.rejectQuantity;
              item.rejectReasonId = self.model.rejectReasons[0].id;
              item.statusId = setStatus('Rejected'); // Rejected
            } else if (self.model.rejectQty == 0 || !self.model.rejectQty) {
              item.acceptedQuantity = item.scrapQuantity;
              item.rejectQuantity = 0;
              item.rejectReasonId = self.model.rejectReasons[0].id;
              item.statusId = setStatus('Pending'); // Approved
            } else {
              item.rejectQuantity = self.model.rejectQty;
              item.rejectNote = self.model.rejectNote;
              item.acceptedQuantity = item.scrapQuantity - item.rejectQuantity;
              item.rejectReasonId = self.model.rejectReasons[0].id;
              item.statusId = setStatus('Partially Approved'); // Partially Approved
            }
            $scope.calculateReceivedQty(item);
            ModalDismiss('qtyaccepatReject');
          }
        });
      }
    }
    function valueOnChange(item, isSelectAll) {
      if (isSelectAll)
        self.model.scrapDetailData.items.forEach(function (item) {
          if (self.model.selectAllRows)
            item.acceptedQuantity = item.scrapQuantity;
          else
            item.acceptedQuantity = 0;
        });
      else
        if (item.isSelected)
          item.acceptedQuantity = item.scrapQuantity;
        else
          item.acceptedQuantity = 0;
    }
    var ModalDismissClose = function (popupName) {
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
      self.model.onclose.isReject = false;
      self.model.onclose.acceptedQuantity = self.model.onclose.scrapQuantity;
      self.model.onclose.rejectQuantity = 0;
    }

    var ModalDismiss = function (popupName) {
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
    }
    var RejectionPopup = function (item) {
      self.model.onclose = item;
     // self.model.rejectQty = '';
      self.model.rejectReason = '';
      self.model.rejectNote = '';
      if (item.isReject) {
        itemId = item.id;
        $("#qtyaccepatReject").modal()
        self.model.reasons = self.model.rejectReasons;
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
        item.acceptedQuantity = prevRequest.scrapQuantity;
        item.rejectNote = prevRequest.rejectNote;
        item.rejectReasonId = prevRequest.rejectReasonId;
        item.statusId = prevRequest.statusId;
        $scope.calculateReceivedQty(item);
      }
    }

    var NavigateToBack = function () {
      $state.go('approvalScrapRequest');
    }
    
    var SaveApproval = function () {
       // $rootScope.startLoader(); 
    if (ValidateApproval()) {
    	
    	 ActualSaveApproval(); /*
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
        	  $rootScope.stopLoader(); 
        	  ActualSaveApproval();
          }
 	  });
     */}
    }

    var ActualSaveApproval = function () {
   
        var URI = CONSTANTS.INVENTORY.SCRAP.SCRAP_REQUEST_APPROVAL.APPROVE_API + commonDetailService.getDataId();
        var data = {
          "approvalStatusId": setStatus('Approved'),
          "unitId": $scope.unitId,
          "userId":$scope.userId,
          "organizationId":$scope.organizationId,
          "item": []
        }

        $rootScope.startLoader();
        angular.forEach(self.model.SelectedItems, function (item) {
          var newItem = {
            "id": item.id,
            "acceptedQuantity": parseInt(item.acceptedQuantity) ? item.acceptedQuantity : 0,
            "rejectedQty": parseInt(item.rejectQuantity),
            "rejectReasonId": self.model.rejectReason ? self.model.rejectReason : 1,
            "rejectNote": self.model.rejectNote,
            "statusId": item.status ? item.status : 6,
            "organizationId": $scope.organizationId,
            "unitId": $scope.unitId,
            "userId": $scope.userId
          }

          // if (item.acceptedQuantity > 0 && item.acceptedQuantity < item.scrapQuantity) {
          //   newItem.statusId = setStatus('Partially Approved');

          //   if (data.approvalStatusId == setStatus('Reject')) {
          //     data.approvalStatusId = setStatus('Partially Approved');
          //   }
          // }
          // else if (item.acceptedQuantity >= item.scrapQuantity) {
          //   data.approvalStatusId = setStatus('Approved');

          //   if (data.approvalStatusId == setStatus('Reject')) {
          //     data.approvalStatusId = setStatus('Approved');
          //   }
          // }
          // else if (item.rejectQuantity >= item.scrapQuantity) {
          //   newItem.statusId = setStatus('Rejected');
          // }
          // else if (item.rejectQuantity > 0 && item.rejectQuantity < item.scrapQuantity) {
          //   newItem.statusId = setStatus('Partially Approved');

          //   if (data.approvalStatusId == setStatus('Reject')) {
          //     data.approvalStatusId = setStatus('Approved');
          //   }
          // }

          data.item.push(newItem);
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
      $rootScope.stopLoader();
      if (!self.model.SelectedItems || self.model.SelectedItems.length == 0) {
        growl.error('Select Items to Update and Save');
        isValid = false;
      }
      else if (self.model.SelectedItems) {
        angular.forEach(self.model.SelectedItems, function (item) {
            if (!ValidateQuantity(item)) {
                isValid = false;
            }
        });

    }


      return isValid;
    }

    var ValidateQuantity = function (item) {
      var isValid = true;
      if (isNaN(parseFloat(item.acceptedQuantity))) {
          item.isQuantityValid = true;
          isValid = false;
          growl.error("Please Enter Accept Scrap Quantity");
      }

      else if (parseInt(item.acceptedQuantity) > item.scrapQuantity) {
          item.isQuantityValid = true;
          isValid = false;
          growl.error('Entered accept scrap quantity is exceeding the scrap quantity');
      } else {
          item.acceptedQuantity = parseInt(item.acceptedQuantity);
          isValid = true;
      }

      return isValid;
    }

    $scope.calculateReceivedQty = function(item){
      if(item.isThreeUOMApplicable == false){
          if(item.uomTypeId == 1){
              item.receivedQtyCon = (item.acceptedQuantity +"*"+ item.uomList[0].conversion);
              item.receivedQtyCalculation = (item.acceptedQuantity * item.uomList[0].conversion);
              item.uomUnitData = item.uomList[2].uomUnit;
          }
          else if(item.uomTypeId == 2){
                  
          }
          else{
              item.receivedQtyCon = (item.acceptedQuantity);
              item.receivedQtyCalculation = (item.acceptedQuantity);
              item.uomUnitData = item.uomList[2].uomUnit;
          }
      }
      else{
          if(item.uomTypeId == 1){
              item.receivedQtyCon = (item.acceptedQuantity +"*"+ item.uomList[0].conversion+"*"+ item.uomList[1].conversion);
              item.receivedQtyCalculation = (item.acceptedQuantity * item.uomList[0].conversion * item.uomList[1].conversion);
              item.uomUnitData = item.uomList[2].uomUnit;
          }
          else if(item.uomTypeId == 2){
              item.receivedQtyCon = (item.acceptedQuantity +"*"+ item.uomList[1].conversion);
              item.receivedQtyCalculation = (item.acceptedQuantity * item.uomList[1].conversion);
              item.uomUnitData = item.uomList[2].uomUnit;
          }
          else{
              item.receivedQtyCon = (item.acceptedQuantity);
              item.receivedQtyCalculation = (item.acceptedQuantity);
              item.uomUnitData = item.uomList[2].uomUnit;
          }
      }
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailsApprovalScrapRequest", {
      url: "/detailsApprovalScrapRequest",
      templateUrl: 'views/PharmacyInventory/Store/details-approved-scrap-request.html',
      controller: "ScrapRequestApprovalDetails.Controller",
      controllerAs: "vm",
      title: 'Scrap Request Approval'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("ScrapRequestApprovalDetails.Controller", scrapRequestApprovalDetailsController);
})();
