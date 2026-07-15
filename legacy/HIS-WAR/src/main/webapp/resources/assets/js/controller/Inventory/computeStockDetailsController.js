(function () {
  "use strict";

  function computeStockDetailsController($scope, $rootScope, $http, $cookies, CONSTANTS, $state, CurrencyService, StatusService, rejectReasonService, commonDetailService, GenericService, growl,$filter) {
    var self = this;
    var itemId;
    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
      $rootScope.loginpage = true;
      self.model = {
        dateOptions: {
          formatYear: 'yyyy',
          showWeeks: false
        },
        expiryDateOptions: {
          minDate: new Date(),
          minMode: "month",
          maxMode: "year",
          datepickerMode: "month"
        },
        dateOpen: [],

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

      CurrencyService.GetCurrency().then(function (currency) {
        self.model.Currency = currency;
      })

      self.model.OpenExpiryDate = OpenExpiryDate;
      self.model.setSelectedDate = setSelectedDate;
      self.model.setStatus = setStatus;
      self.model.isDisable = isDisable;
      self.model.SelectAllItems = SelectAllItems;
      self.model.SelectItem = SelectItem;
      self.model.isSelected = isSelected;
      self.model.SaveAcceptReject = SaveAcceptReject;
      self.model.ApprovedQuantity = ApprovedQuantity;
      self.model.fromComputeStock = fromComputeStock;
      self.model.ValidateRejection = ValidateRejection;
      self.model.valueOnChange = valueOnChange;
      self.model.RejectionPopup = RejectionPopup;
      self.model.ModalDismiss = ModalDismiss;
      self.model.CancelRejection = CancelRejection;
      self.model.ValidateApproval = ValidateApproval;
      self.model.SaveApproval = SaveApproval;
      self.model.NavigateToBack = NavigateToBack;
      self.model.ValidateBatch = ValidateBatch;
      self.model.ValidateExpiry = ValidateExpiry;
      self.model.ValidateQuantity = ValidateQuantity;
      self.model.ValidateMRP = ValidateMRP;
      
      self.model.batchList = []; 
      self.model.AddDuplicateRow = AddDuplicateRow;
      self.model.RemoveRowData = RemoveRowData;
      self.model.batchOnChange = batchOnChange;
      self.model.SelectedItems = [];
      self.model.SetbatchDetail = SetbatchDetail;
      self.model.isBatchDisabled = false;
      self.model.ValidateApprovedQuantity = ValidateApprovedQuantity;
      self.model.IsNewBatchAvailable = false;
      self.model.newBatches = [];
      fromComputeStock();
    }

    var SetbatchDetail = function (compute){ 
        if(compute !=null && compute.batch.length == 1)  {
            self.model.isBatchDisabled = true;
            compute.batch.forEach(function(batch){
            	 batch.selectedBatch = batch;       
                 self.model.isDropDownDisabled = true;
            });               
        }else{
            compute.batch.forEach(function(batch){
           	    batch.selectedBatch = batch;      
           	  });   
            self.model.isBatchDisabled = false;      
            self.model.isDropDownDisabled = true;	
            //batch.selectedBatch.notAnOption = true;            
            var IsnewBatchAvailable=compute.batch.filter(x=>x.isnew ==true);
            if(IsnewBatchAvailable.length == 0){
            	self.model.IsNewBatchAvailable = true;
            }
            else{
                self.model.IsNewBatchAvailable = false;
            }
        }   	
     } 
    
    var ValidateApprovedQuantity = function (batch,uploadedQuantity) {
        var isValid = true;          
        var totalBatchDifference=0;
        uploadedQuantity.batch.forEach(function(newBatch){              
            var  batchdifference=  parseInt(newBatch.approvedQuantity) -  parseInt(newBatch.uploadedQuantity);              
            totalBatchDifference = totalBatchDifference + batchdifference;
        });
       var Totaldifference =  parseInt(uploadedQuantity.uploadedQuantity) -  parseInt(uploadedQuantity.availableQuantity);          
        if(totalBatchDifference > Totaldifference){
            batch.isApprovedQuantity = true;
          growl.error('Approved Quantity can not be match with uploaded quantity.');
        }
        //  else if(parseInt(batch.approvedQuantity) <= 0){
        // 	growl.error('Approved Quantity can be greater than zero.');
        // }
        return isValid;
    }       
    
    var batchSelected = "";
    var batchOnChange = function (selectedBatch,batch) {   
    	if(selectedBatch !=null){
         batch.selectedBatch.notAnOption = true;            
         batchSelected = batch.selectedBatch;
         batch.expiryDate = batchSelected.expiryDate;
   		 batch.availableQuantity = Math.round(batchSelected.availableQuantity);
   		 batch.uom = batchSelected.uom;
   		 batch.uomTypeId = batchSelected.uomTypeId;
   		 batch.uomUnit = batchSelected.uomUnit;
   		 batch.uomUnitId = batchSelected.uomUnitId;
   		 batch.uploadedQuantity = Math.round(batchSelected.uploadedQuantity);
   		 batch.difference = Math.round(batchSelected.uploadedQuantity - batchSelected.availableQuantity);
   		 batch.approvedQuantity = batchSelected.uploadedQuantity;
   		batch.batchId = batchSelected.batchId;
     	} 	
    	else{
    	     batch.expiryDate = batchSelected.expiryDate;
    		 batch.availableQuantity = Math.round(batchSelected.availableQuantity);
    		 batch.uom = batchSelected.uom;
    		 batch.uomTypeId = batchSelected.uomTypeId;
    		 batch.uomUnit = batchSelected.uomUnit;
    		 batch.uomUnitId = batchSelected.uomUnitId;
    		 batch.uploadedQuantity = Math.round(batchSelected.uploadedQuantity);
    		 batch.difference = Math.round(batchSelected.uploadedQuantity - batchSelected.availableQuantity);
    		 batch.approvedQuantity = batchSelected.uploadedQuantity;
    		 batch.batchId = batchSelected.batchId;
    		 batch.selectedBatch = batchSelected;    		
    	}     
    } 
   
      var AddDuplicateRow = function (item) {        
        // var list = self.model.newBatches.filter(x=>x.isnew == true);
         var list = item.batch.filter(x=>x.isnew == true);
         if(list.length > 0){            
        	 var data = list[0];    
         data.isnew = false;
         data.notAnOption = false;
         item.batchList.push(data); 
         }else{
            self.model.IsNewBatchAvailable = true;
         } 
          var dataAvailable = item.batch.filter(x=>x.isnew == true);  
         if(dataAvailable.length == 0){
             self.model.IsNewBatchAvailable = true;
         }             
     }

    var RemoveRowData = function (batch, item) {
        var index = item.batchList.indexOf(batch);
        item.batchList.splice(index, 1);
    }
    
    function fromComputeStock() {
      var id = commonDetailService.getDataId();
      var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.COMPUTE_STOCK.DETAIL_API + id;
      var data = {};
      $rootScope.startLoader(); 
      GenericService.serviceAction("GET", URI, data).then(
        function (response) {
          self.model.computeStockDetail = response.data;  
           self.model.computeStockDetail.items.forEach(function (item) {
            item.isDisabled = true;
            response.data.statusId = item.statusId;
            item.cop = item.cop.toFixed(2);
            item.mrp = item.mrp.toFixed(2);
            
            if (!!item.batch) {            	
            	 var uploadedBatches = item.batch.filter(x => x.isnew == false);
            	 self.model.newBatches = item.batch.filter(x => x.isnew == true);
                item['batchList'] = uploadedBatches;
                item['batchList']['uomList'] = [];             
                
                item.batch.forEach(function(batch){
                    batch.approvedQuantity = batch.uploadedQuantity;
                });
            }        
          });
          $rootScope.stopLoader(); 
        });
    }

    var OpenExpiryDate = function (idx) {
      self.model.dateOpen[idx] = true;
    }

    var setSelectedDate = function (item) {
      var expDateYear = item.newExpiryDate.getFullYear();
      var expDateMonth = item.newExpiryDate.getMonth() + 1;
      var expDateDay = new Date(expDateYear, expDateMonth, 0).getDate();
      item.newitemExpiry = (expDateYear + "-" + expDateMonth + "-" + expDateDay);
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
        self.model.computeStockDetail.items.forEach(function (item) {
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
        self.model.computeStockDetail.items.forEach(function (item) {
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
        var index = items.indexOf(item);
        if (index > -1) {
          items.splice(index, 1);
        }
      }

      if (self.model.computeStockDetail.items.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
    }

    var isSelected = function (item) {
      return item.isSelected;
    }
    var ApprovedQuantity = function (item) {
      if (item.difference > 0) {
        // item.rejectedQuantity = item.difference;
        self.model.rejectQty = undefined;
        item.isReject = false;
        item.status = setStatus('Approved');; // Approved.
      } else if (item.difference == 0) {
        item.rejectQuantity = 0; 
        self.model.rejectQty = item.rejectQuantity; 
        item.isReject = true;
        item.status = setStatus('Rejected');; // Rejected
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
            if (self.model.rejectQty > item.difference) {
              growl.error('Please Enter Valid Quantity');
              return false;
            } else if (self.model.rejectQty == item.difference) {
              item.rejectQuantity = item.difference; 
              item.rejectReasonId = self.model.rejectReasons[0].id;
              item.statusId = setStatus('Rejected'); // Rejected
            } else if (self.model.rejectQty == 0 || !self.model.rejectQty) {
              item.rejectQuantity = 0; 
              item.rejectReasonId = self.model.rejectReasons[0].id;
              item.statusId = setStatus('Pending'); // Approved
            } else {
              item.rejectQuantity = self.model.rejectQty; 
              item.rejectNote = self.model.rejectNote;
              item.rejectReasonId = self.model.rejectReasons[0].id;
              item.statusId = setStatus('Partially Approved'); // Partially Approved
            }
            ModalDismiss('qtyaccepatReject');
          }
        });
      }
    }
    function valueOnChange(item, isSelectAll) {
      if (isSelectAll){
    	  self.model.computeStockDetail.items.forEach(function (item) {
              if (self.model.selectAllRows){
            	  if(item.uploadedQuantity > item.availableQuantity){
            		  item.rejectQuantity = 0;
            	  }
            	  else if(item.uploadedQuantity < item.availableQuantity){
            		  item.rejectQuantity = item.availableQuantity - item.uploadedQuantity; 
            	  }         
              }             
            });
      }        
      else{
    	  if(item.uploadedQuantity > item.availableQuantity){
    		  item.rejectQuantity = 0;
    	  }
    	  else if(item.uploadedQuantity < item.availableQuantity){
    		    item.rejectQuantity = item.availableQuantity - item.uploadedQuantity; 
    	  }    
      }      
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
      var prevRequest = self.model.requestItems.item.find(function (prevItem) {
        return prevItem.id == item.id;
      });

      if (prevRequest) {
        item.rejectQuantity = prevRequest.rejectQuantity; 
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

    var SaveApproval = function () {

      if (ValidateApproval()) {
        var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.COMPUTE_STOCK.DETAIL_APPROVE_API + commonDetailService.getDataId();
        var data = {
          "statusId": setStatus('Completed'),
          "items": [],
          "organizationId": $scope.organizationId,
          "userId": $scope.userId,
          "unitId": $scope.unitId
        }

        $rootScope.startLoader();
        angular.forEach(self.model.SelectedItems, function (item) {
          var newItem = {
            "id": item.id,
            "itemId": item.itemId,
            "batchNo": item.batchNo,
            "expiryDate": item.expiryDate,
            "mrp": item.mrp,
            "newBatchNo": item.newBatchNo,
            "newExpiryDate": item.newitemExpiry,
            "newMrp": parseInt(item.newMrp),
            "rejectQuantity": parseInt(item.rejectQuantity), 
            "rejectReasonId": item.rejectReasonId ? item.rejectReasonId : 1,
            "rejectNote": item.rejectNote ? item.rejectNote : "default note",
            "remark": item.remark,
            
            "varianceAtCOP":item.varianceAtCOP, 
            "varianceAtMrp":item.varianceAtMrp, 
            "newBatchNo":item.newBatchNo,            
            "newMrp":item.newMrp,          
            "cop":item.cop,
            "approvedQuantity":item.approvedQuantity,
            "batches" :[] 
          }       
          data.items.push(newItem);
          
          angular.forEach(item.batchList, function (batch) {
        	 if(!batch.isnew){
              var batch = {
                   "id":batch.id,
            	   "batchId": batch.batchId,
                  "uomTypeId": batch.uomTypeId,
                  "uomUnitId": batch.uomUnitId,
                  "uploadedQuantity": parseInt(batch.approvedQuantity),
                  "quantity": parseInt(batch.availableQuantity)
              }
              if((parseInt(item.uploadedQuantity) < parseInt(item.availableQuantity)) && parseInt(batch.uploadedQuantity) < parseInt(batch.quantity)){
                  var rejectedQuantity = parseInt(batch.quantity) - parseInt(batch.uploadedQuantity);
                  batch.rejectedQuantity = rejectedQuantity;
              }else{
                    batch.rejectedQuantity  = 0 ;
              }           
              newItem.batches.push(batch);
        	 }
          });
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
      var TotalApprovedQuantty = 0;   
        	if (!self.model.SelectedItems || self.model.SelectedItems.length == 0) {
    	        growl.error('Select Items to Update and Save');
    	        isValid = false;
    	      } else {
    	    	  angular.forEach(self.model.SelectedItems, function (item) {
    	    	  var totalBatchDifference=0;
    	    	  var IsnewBatchAvailable=item.batch.filter(x=>x.isnew ==true);
    	    	  
    	    	 if(item.batch.length > 1 && parseInt(item.uploadedQuantity) > parseInt(item.availableQuantity)){
    	           angular.forEach(item.batch, function (batch) {        
    	              var  batchdifference=  parseInt(batch.approvedQuantity);              
    	              totalBatchDifference = totalBatchDifference + batchdifference;
    	          });
    	           
    	         var Totaldifference =  parseInt(item.uploadedQuantity); 
    	          // if(totalBatchDifference !=0 && totalBatchDifference != Totaldifference){
    	          //        growl.error('Approved Quantity can not be match with uploaded quantity.');
    	          //        isValid = false;
    	    			//  return false;
    	          // } 
                // else if(totalBatchDifference > Totaldifference){
    	          //     growl.error('Approved Quantity can not be match with uploaded quantity.');
    	          //      isValid = false;
    	    		  //  return false;
    	          // } 
              //    if(IsnewBatchAvailable.length >0){
    	    		//   growl.error('Approved Quantity can not be match with uploaded quantity.');
   	          //      isValid = false;
   	    		  //  return false; 
    	    	  // }
    	    	 }
    	    	});                	  	
    	}
      return isValid;
    }

    var NavigateToBack = function () {
      $state.go('computeStock');
    }


    var ValidateBatch = function (item) {
      var isValid = true;
      if (!item.newBatchNo) {
        item.isBatchNoValid = true;
        isValid = false;
        growl.error("Please Enter Batch No.");
      }

      return isValid;
    }

    var ValidateExpiry = function (item) {
      var isValid = true;
      if (!item.newExpiryDate) {
        item.isExpiryDateValid = true;
        isValid = false;
        growl.error("Please Select Expiry Date");
      } else {
        var expYear = new Date().getFullYear();
        var expMonth = new Date().getMonth() + 1;
        var selectedYear = new Date(item.newExpiryDate).getFullYear();
        var selectedMonth = new Date(item.newExpiryDate).getMonth() + 1;

        if (selectedYear <= expYear && selectedMonth < expMonth) {
          growl.error('Expiry date should be later than current month and year');
          item.isExpiryDateValid = true;
          isValid = false;
        }
      }

      return isValid;
    }

    var ValidateQuantity = function (item) {
      var isValid = true;

      if (item.rejectQuantity) { 
        item.quantity = parseInt(item.rejectQuantity);
        isValid = true;
      }

      return isValid;
    }
    var ValidateMRP = function (item) {
      var isValid = true;

      if (isNaN(parseFloat(item.newMrp))) {
        item.newMrp = '';
        item.isMrpValid = true;
        isValid = false;
        growl.error("Please Enter New MRP");
      } else if (parseFloat(item.newMrp) == 0) {
        item.isMrpValid = true;
        isValid = false;
        growl.error('MRP cannot be zero');
      } else if (parseFloat(item.newMrp) > 0) {
        item.newMrp = parseFloat(item.newMrp).toFixed(2);
      }
      return isValid;
    }
    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailsComputeStock", {
      url: "/detailsComputeStock",
      templateUrl: 'views/PharmacyInventory/StockAdjustment/details-compute-stock.html',
      controller: "ComputeStockDetails.Controller",
      controllerAs: "vm",
      title: 'Compute Stock'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("ComputeStockDetails.Controller", computeStockDetailsController);  
  
})();
