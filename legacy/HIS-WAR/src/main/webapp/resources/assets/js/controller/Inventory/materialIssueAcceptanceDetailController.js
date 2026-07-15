(function () {
  "use strict";

  function materialIssueAcceptanceDetailController($scope, $rootScope, $http, $state, $cookies, CONSTANTS, commonDetailService, rejectReasonService, StatusService, GenericService, $filter, growl,stopTransactionService) {
    var self = this;
    var selectItemList = [];
    var masterId;

    var itemId;
    var batch;
    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
      $rootScope.loginpage = true;
      self.model = {
        SelectedItems: [],
        requestData: [],

      };
      self.model.fromMaterialIssueAcceptance = fromMaterialIssueAcceptance;
      self.model.SelectAllItems = SelectAllItems;
      self.model.SelectItem = SelectItem;
      self.model.ApprovedQuantity = ApprovedQuantity;
      self.model.SaveApproval = SaveApproval;
      self.model.setStatus = setStatus;
      self.model.RejectionPopup = RejectionPopup;
      self.model.SaveAcceptReject = SaveAcceptReject;
      self.model.valueOnChange = valueOnChange;
      self.model.ModalDismiss = ModalDismiss;
      self.model.quantityChange = quantityChange;
      self.model.ValidateQuantity = ValidateQuantity;
      self.model.SetFocus = SetFocus;
      self.model.BarcodeScan = BarcodeScan;
      self.model.ModalDismissRejection = ModalDismissRejection;
       $scope.itemIdx  = 0;
      StatusService.GetStatus().then(function (status) {
        self.model.statuses = status;
      });
      rejectReasonService.GetReason().then(function (reason) {
        self.model.rejectReasons = reason;
      });
      fromMaterialIssueAcceptance();
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
                AddBatchToDetail(self.model.materialPickerDetail, itemId, batchId);
            }

            event.stopPropagation();
            event.preventDefault();

            SetFocus();
        }
    }
    function fromMaterialIssueAcceptance() {
      var id = commonDetailService.getDataId();
      var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_ISSUE_ACCEPTANCE.DETAILS_API + $scope.unitId + '/'+$scope.organizationId + '/' + id;
      var data = {};
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, data).then(
        function (response) {
          self.model.materialIssueDetail = response.data;
          
          //self.model.materialIssueDetail.items.original = angular.copy(self.model.materialIssueDetail.items);
          $scope.initialStatusId=setStatus(response.data.status);
          self.model.materialIssueDetail.items.forEach(function (item) {
            item.isDisabled = true;
            if (item.rejectQuantity)
              item.isReject = true;
            

            item.selectedUom = item.uom.find(function (uom) {
              return item.uomTypeId == uom.uomTypeId;
            });
            item.uomType = item.selectedUom.uomDetails;
            if(item.isThreeUOMApplicable == false){
                item.uomListdata = item.uom;
                item.uom = [];
                angular.forEach(item.uomListdata, function(data){
                  if(data.uomType != 'Stock'){
                    item.uom.push(data);
                  }
                });
              }
            
            var uomdata = item.selectedUom.uomDetails.split(' ')[0];
            if(item.isThreeUOMApplicable == false){
              if(item.rejectQuantity == undefined || item.rejectQuantity == null || item.rejectQuantity == 0){
                if(uomdata == 'P'){
                  item.receivedQtyCon = (item.issueQuantity +"*"+ item.uomListdata[0].conversion);
                  item.receivedQtyCalculation = (item.issueQuantity * item.uomListdata[0].conversion);
                  item.uomUnitData = item.uomListdata[2].uomUnit;
                }
                else if(uomdata == 'S'){
                  
                }
                else{
                  item.receivedQtyCon = (item.issueQuantity);
                  item.receivedQtyCalculation = (item.issueQuantity);
                  item.uomUnitData = item.uomListdata[2].uomUnit;
                }
              }
              else{
                if(uomdata == 'P'){
                  item.receivedQtyCon = (item.acceptQuantity +"*"+ item.uomListdata[0].conversion);
                  item.receivedQtyCalculation = (item.acceptQuantity * item.uomListdata[0].conversion);
                  item.uomUnitData = item.uomListdata[2].uomUnit;
                }
                else if(uomdata == 'S'){
                  
                }
                else{
                  item.receivedQtyCon = (item.acceptQuantity);
                  item.receivedQtyCalculation = (item.acceptQuantity);
                  item.uomUnitData = item.uomListdata[2].uomUnit;
                }
              }
            } 
            else{
              if(item.rejectQuantity == undefined || item.rejectQuantity == null || item.rejectQuantity == 0){
                if(uomdata == 'P'){
                  item.receivedQtyCon = (item.issueQuantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
                  item.receivedQtyCalculation = (item.issueQuantity * item.uom[0].conversion * item.uom[1].conversion);
                  item.uomUnitData = item.uom[2].uomUnit;
                }
                else if(uomdata == 'S'){
                  item.receivedQtyCon = (item.issueQuantity +"*"+ item.uom[1].conversion);
                  item.receivedQtyCalculation = (item.issueQuantity * item.uom[1].conversion);
                  item.uomUnitData = item.uom[2].uomUnit;
                }
                else{
                  item.receivedQtyCon = (item.issueQuantity);
                  item.receivedQtyCalculation = (item.issueQuantity);
                  item.uomUnitData = item.uom[2].uomUnit;
                }
              }
              else{
                if(uomdata == 'P'){
                  item.receivedQtyCon = (item.acceptQuantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
                  item.receivedQtyCalculation = (item.acceptQuantity * item.uom[0].conversion * item.uom[1].conversion);
                  item.uomUnitData = item.uom[2].uomUnit;
                }
                else if(uomdata == 'S'){
                  item.receivedQtyCon = (item.acceptQuantity +"*"+ item.uom[1].conversion);
                  item.receivedQtyCalculation = (item.acceptQuantity * item.uom[1].conversion);
                  item.uomUnitData = item.uom[2].uomUnit;
                }
                else{
                  item.receivedQtyCon = (item.acceptQuantity);
                  item.receivedQtyCalculation = (item.acceptQuantity);
                  item.uomUnitData = item.uom[2].uomUnit;
                }
              }   
            }
          });
          
        //added by minesh for total of availableQuantity from all batch and get single Unique Batch                     
         /* var batchNo = [];
          var uniqueItems = [];
      	angular.forEach(self.model.materialIssueDetail.items, function(item,key){                		
      		
      			var index = batchNo.indexOf(item.batchNo);                    			 
              	if(index === -1)
              	{
              		batchNo.push(item.batchNo);
              		uniqueItems.push(item);
              	}else{
              			uniqueItems[index].issueQuantity += item.issueQuantity;
      					//item.availableStockQuantity = uniqueItems[index].issueQuantity ;
              	}
      		});
      		
      		self.model.materialIssueDetail.items=uniqueItems;*/
          //added by minesh for total of availableQuantity from all batch and get single Unique Batch

          $rootScope.stopLoader();
        });
    }

    var isSelected = function (item) {
      return item.isSelected;
    }


    var SelectAllItems = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedRequestIds = [];
        self.model.SelectedItems=[];
        self.model.materialIssueDetail.items.forEach(function (items) {
          if (!isDisable(items) && items.status != 4) {
            items.isSelected = true;
            items.isDisabled = false;
            self.model.SelectedRequestIds.push(items.id);
            self.model.SelectedItems.push(items);
          }
        });
      } else {
        self.model.SelectedRequestIds = [];
        self.model.SelectedItems = [];
        self.model.materialIssueDetail.items.forEach(function (items) {
          items.isSelected = false;
          items.isDisabled = true;
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

      if (self.model.materialIssueDetail.items.every(isSelected)) {
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

document.getElementById('qtyaccepatReject').onclick = function(e){
 if(self.model.rejectQty==0){
 self.model.rejectQty = self.model.materialIssueDetail.items[$scope.itemIdx].rejectQuantity;
 }

}


    var RejectionPopup = function (item, idx) {
    	if(item.isReject==true)
    		{
    		$scope.idx = idx;
    		}
      self.model.onclose = item;
      //self.model.rejectQty = '';
      self.model.rejectReason = '';
      if (item.isReject) {
        itemId = item.id;
        batch=item.batchId;
      //  $("#qtyaccepatReject").modal()
        self.model.action = 'Open';
        var popup = angular.element('#qtyaccepatReject');
        popup.modal('show');
        $scope.itemIdx = self.model.materialIssueDetail.items.indexOf(item);
        self.model.reasons = self.model.rejectReasons;
        if(item.rejectQuantity ==  undefined || item.rejectQuantity == null || item.rejectQuantity == 0){
          self.model.rejectQty = item.issueQuantity;
        }
      } else {
        CancelRejection(item);
      }
    }
    var quantityChange = function (item) {
      if (!item.acceptQuantity)
        item.acceptQuantity = 0;
    }

    var CancelRejection = function (item) {
      var prevRequest = self.model.materialIssueDetail.items.find(function (prevItem) {
        return prevItem.id == item.id;
      });

      if (prevRequest) {

        //item.rejectQuantity = prevRequest.rejectQuantity;
        item.acceptQuantity = parseInt(item.acceptQuantity) + parseInt(item.rejectQuantity);
        item.rejectQuantity = 0;
        item.rejectNote = prevRequest.rejectNote;
        item.rejectReasonId = prevRequest.rejectReasonId;
        item.statusId = prevRequest.statusId;
        $scope.calculateReceivedQty(item);
      }
    }


    var ApprovedQuantity = function (item, idx) {
      if (item.acceptQuantity > item.issueQuantity) {
        item.rejectQuantity = 0;
        self.model.rejectQty = undefined;
        item.isReject = false;
      } else if (item.acceptQuantity == 0 || !item.acceptQuantity) {
        item.rejectQuantity = item.issueQuantity;
        self.model.rejectQty = item.rejectQuantity;
        item.isReject = true;
        RejectionPopup(item, idx);
      } else if (item.acceptQuantity < item.issueQuantity) {
        item.rejectQuantity = item.issueQuantity - item.acceptQuantity;
        self.model.rejectQty = item.rejectQuantity;
        item.isReject = true;
        RejectionPopup(item, idx);
      } else if (item.acceptQuantity == item.issueQuantity) {
        item.rejectQuantity = item.issueQuantity - item.acceptQuantity;
        self.model.rejectQty = item.rejectQuantity;
        item.isReject = false;
        RejectionPopup(item, idx);
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
 	                "storeId": self.model.materialIssueDetail.fromStoreId
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
        var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_ISSUE_ACCEPTANCE.SAVE_API + commonDetailService.getDataId();
        var data = {
          "statusId": setStatus('Closed'),
          "storeId" :self.model.materialIssueDetail.toStoreId,
          "items": [],
          "unitId":$scope.unitId,
          "organizationId":$scope.organizationId,
          "userId":$scope.userId,
          "storeId":$scope.storeId

        }

        angular.forEach(self.model.SelectedItems, function (item) {
          var newItem = {
            "batchMapperId": parseInt(item.batchMapperId),
            "acceptedQuantity": parseInt(item.acceptQuantity),
            "rejectQuantity": item.rejectQuantity ? item.rejectQuantity : 0,
            "rejectReasonId": self.model.rejectReasons.id ? self.model.rejectReasons.id : 1,
            "rejectNote": self.model.rejectNote,
            "isConsignment": item.isConsignment
          }
          data.items.push(newItem);
          var getLength=self.model.materialIssueDetail.items.length;
          var count=0;
          angular.forEach(self.model.materialIssueDetail.items, function (item) {
        	  if(item.status==4)
        		  {
        		  count++;
        		  }
          });
          if($scope.initialStatusId!=18 && getLength==self.model.SelectedItems.length)
    	  {
    	  data.newStatusId = setStatus('Closed');
    	  }
          else
        	  {
        	  data.newStatusId=$scope.initialStatusId;
        	  }
          if(getLength-1==count)
          {
            data.statusId = setStatus('Closed');
          }
          else if(getLength==self.model.SelectedItems.length+count)
        	  {
        	  data.statusId = setStatus('Closed');
        	  }
        	  else
        	  {
        	  data.statusId = setStatus('Partially Completed');
        	  }
        });
        self.model.materialIssueDetail.acceptQuantity1 = 0;
        self.model.materialIssueDetail.indentQuantity1 = 0;
        $scope.list = self.model.materialIssueDetail.items;
        var cleaned = [];
        $scope.list.forEach(function(itm) {
          var unique = true;
          cleaned.forEach(function(itm2) {
            if (_.isEqual(itm.itemId, itm2.itemId)) unique = false;
          });
          if (unique) cleaned.push(itm);
        });
        $scope.list = cleaned;
        angular.forEach($scope.list, function(d) {
          self.model.materialIssueDetail.indentQuantity1 += d.indentQuantity;
        });
        angular.forEach(self.model.materialIssueDetail.items, function(val) {
          self.model.materialIssueDetail.acceptQuantity1 += val.acceptQuantity;
        });
//        if(self.model.materialIssueDetail.acceptQuantity1 < self.model.materialIssueDetail.indentQuantity1){
//          data.statusId = setStatus('Partially Completed');
//        }
        $rootScope.startLoader();
        GenericService.serviceAction("PATCH", URI, data).then(
          function (response) {
            $rootScope.stopLoader();
            growl.success(response.data.message);
            NavigateToBack();
            $scope.saveDisabled = true;
          }, function (err) {
            $rootScope.stopLoader();
          //  growl.error('Somthing Went Wrong');
            growl.error(err.data.summary);
          });
    }

    function setStatus(x) {
      var status = self.model.statuses.find(function (obj) {
        return obj.status == x;
      });

      return status.id;
    }

    var isDisable = function (items) {
      if (items.statusId == 10) {
        return true;
      }
      return false;
    }


    var ValidateApproval = function () {
      var isValid = true;

      if (!self.model.SelectedItems || self.model.SelectedItems.length == 0) {
        growl.error('Select Items to Update and Save');
        isValid = false;
        $scope.saveDisabled = true;
      }
      for (let issueDetail of self.model.materialIssueDetail.items) {
        /*if (!ValidateQuantity(issueDetail)) {
          isValid = false;
        }*/
    	//commented above code and added below code by minesh (condition check for only selected item).
        if (issueDetail.isSelected && !ValidateQuantity(issueDetail)) {
        	isValid = false;
          $scope.saveDisabled = true;
        }
      }
      if(!isValid)
      {
      $scope.saveBtnDisabled=false;
      }
      // for (let issueDetail of self.model.materialIssueDetail.items) {
      //   if (issueDetail.rejectQuantity > issueDetail.issueQuantity) {
      //     growl.error('Error', {
      //       title: "Please Enter Valid Reject Quantity"
      //     });
      //     isValid = false;
      //   }
    // }
    return isValid;
  }


  var ValidateQuantity = function (item) {
    var isValid = true;

    if (isNaN(parseFloat(item.acceptQuantity))) {
      item.isQuantityValid = true;
      isValid = false;
      growl.error("Please Enter Accepted Quantity");
    }
    else if (item.acceptQuantity > item.issueQuantity) {
      growl.error("Please Enter Accepted Quantity less than or equal to issue quantity");
      item.isQuantityValid = true;
      isValid = false;
    }
    else if (item.acceptQuantity > item.maxOrderQuantity) {
      growl.error("Qty is exceeding the max store qty\n Please reduce some qty");
      item.isQuantityValid = true;
      isValid = false;
    }
    return isValid;
  }



  var NavigateToBack = function () {
    $state.go('materialIssueAcceptance');
  }

  var ModalDismiss = function (popupName,idx) {
    var popup = angular.element('#' + popupName);
    popup.modal('hide');
    self.model.onclose.isReject = false;
    self.model.materialIssueDetail.items[$scope.itemIdx].isReject = false;
   //self.model.onclose.acceptQuantity = self.model.onclose.indentQuantity-self.model.onclose.pendingQuantity;

   //line 391 added by ganesh
    //self.model.onclose.acceptQuantity = self.model.onclose.issueQuantity-self.model.onclose.pendingQuantity;
   // self.model.onclose.rejectQuantity = 0;
    
    self.model.materialIssueDetail.items[idx].acceptQuantity = parseInt(self.model.materialIssueDetail.items[idx].acceptQuantity) + self.model.materialIssueDetail.items[idx].rejectQuantity;
    self.model.materialIssueDetail.items[idx].rejectQuantity =  0;
    $scope.calculateReceivedQty(self.model.materialIssueDetail.items[idx]);
  }


  var SaveAcceptReject = function () {
    if (ValidateRejection()) {
      var rejectItemId = itemId;
      var rejectedBatchId = batch;
      console.log("Rejected batch Id : ",rejectedBatchId);
      self.model.materialIssueDetail.items.forEach(function (item,key) {
        if(item.isSelected == true){
        	if($scope.idx!=undefined && $scope.idx!=null)
        		{
          if (item.id == rejectItemId && item.batchId == rejectedBatchId && key == $scope.idx) {
            if (self.model.rejectQty > item.issueQuantity) {
              growl.error('Please Enter Reject Quantity Less Than Issue Quantity');
              return false;
            } else if (self.model.rejectQty == item.issueQuantity) {
              item.rejectQuantity = item.issueQuantity;
              item.acceptQuantity = item.issueQuantity - item.rejectQuantity;
              item.rejectReasonId = self.model.rejectReasons[0].id;
            } else if (self.model.rejectQty == 0 || !self.model.rejectQty) {
              item.acceptQuantity = item.issueQuantity;
              item.rejectQuantity = 0;
              item.rejectReasonId = self.model.rejectReasons[0].id;
              item.isReject = false;
            } else {
              item.rejectQuantity = self.model.rejectQty;
              item.rejectNote = self.model.rejectNote;
              item.acceptQuantity = item.issueQuantity - item.rejectQuantity;
              item.rejectReasonId = self.model.rejectReasons[0].id;
            }
            ModalDismissRejection('qtyaccepatReject');
           if(item.rejectQuantity == 0){
              item.isReject = false;
            }
            else{
              item.isReject = true;
            }
            
            self.model.rejectQty = undefined;
            self.model.rejectReason = undefined;
            self.model.rejectNote = undefined;
            $scope.calculateReceivedQty(item);
          }
        }
      }
      });
    }
  }

  var ModalDismissRejection = function (popupName) {
	    var popup = angular.element('#' + popupName);
	    popup.modal('hide');
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
      self.model.materialIssueDetail.items.forEach(function (item) {
        if (self.model.selectAllRows) {
        	if(item.rejectQuantity>0)
        		{
        		 item.acceptQuantity = item.acceptQuantity;
        		 item.rejectQuantity = item.rejectQuantity;
        		}else
        			{
        			 item.acceptQuantity = item.issueQuantity;
        	          item.rejectQuantity = 0;
        	          item.isReject = false;
        			}
         
        }

        else{
          item.acceptQuantity = 0;
          item.rejectQuantity = 0;
        }
      });
    else
      if (item.isSelected) {
    	  if(item.rejectQuantity>0)
  		{
  		 item.acceptQuantity = item.acceptQuantity;
  		 item.rejectQuantity = item.rejectQuantity;
  		}else
  			{
  			 item.acceptQuantity = item.issueQuantity;
  	          item.rejectQuantity = 0;
  	          item.isReject = false;
  			}
      }
      else{
        item.acceptQuantity = 0;
        item.rejectQuantity = 0;
      }
  }

  $scope.calculateReceivedQty = function (item) {
    if(item.acceptQuantity == undefined || item.acceptQuantity == null || item.acceptQuantity == ""){
      item.acceptQuantity = 0;
    }
    if(item.isThreeUOMApplicable == false){
      if(item.uomTypeId == 1){
        item.receivedQtyCon = (item.acceptQuantity +"*"+ item.uomListdata[0].conversion);
        item.receivedQtyCalculation = (item.acceptQuantity * item.uomListdata[0].conversion);
        item.uomUnitData = item.uomListdata[2].uomUnit;
      }
      else if(item.uomTypeId == 2){
      
      }
      else{
        item.receivedQtyCon = (item.acceptQuantity);
        item.receivedQtyCalculation = (item.acceptQuantity);
        item.uomUnitData = item.uomListdata[2].uomUnit;
      }
    }
    else{
      if(item.uomTypeId == 1){
        item.receivedQtyCon = (item.acceptQuantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
        item.receivedQtyCalculation = (item.acceptQuantity * item.uom[0].conversion * item.uom[1].conversion);
        item.uomUnitData = item.uom[2].uomUnit;
      }
      else if(item.uomTypeId == 2){
        item.receivedQtyCon = (item.acceptQuantity +"*"+ item.uom[1].conversion);
        item.receivedQtyCalculation = (item.acceptQuantity * item.uom[1].conversion);
        item.uomUnitData = item.uom[2].uomUnit;
      }
      else{
        item.receivedQtyCon = (item.acceptQuantity);
        item.receivedQtyCalculation = (item.acceptQuantity);
        item.uomUnitData = item.uom[2].uomUnit;
      }
    }
  }

  initializeController();
}

  function config($stateProvider) {
  $stateProvider.state("detailsmaterialIssueAcceptance", {
    url: '/detailsmaterialIssueAcceptance',
    templateUrl: 'views/PharmacyInventory/InPatient/details-material-issue-acceptance.html',
    controller: "MaterialIssueAcceptanceDetail.Controller",
    controllerAs: 'vm',
    title: 'Material Issue Acceptance'
  });
}

angular
  .module("myApp")
  .config(config)
  .controller("MaterialIssueAcceptanceDetail.Controller", materialIssueAcceptanceDetailController)
}) ();   