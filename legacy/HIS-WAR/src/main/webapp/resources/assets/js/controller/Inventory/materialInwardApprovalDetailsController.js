(function () {
    "use strict";

    function materialInwardApprovalDetailsController($scope, $rootScope, $state, StatusService,CurrencyService, rejectReasonService, commonDetailService, $http, $cookies, CONSTANTS, GenericService, growl) {
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
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
              });
            StatusService.GetStatus().then(function (status) {
                self.model.statuses = status;
            });
            rejectReasonService.GetReason().then(function (reason) {
                self.model.rejectReasons = reason;
            });

            self.model.fromInwardApproval = fromInwardApproval;
            self.model.SelectAllItems = SelectAllItems;
            self.model.SelectItem = SelectItem;
            self.model.isSelected = isSelected;
            self.model.valueOnChange = valueOnChange;
            self.model.RejectionPopup = RejectionPopup;
            self.model.CancelRejection = CancelRejection;
            self.model.ModalDismiss = ModalDismiss;
            self.model.ApprovedQuantity = ApprovedQuantity;
            self.model.SaveAcceptReject = SaveAcceptReject;
            self.model.ValidateRejection = ValidateRejection;
            self.model.SaveApproval = SaveApproval;
            self.model.ValidateApproval = ValidateApproval;
            self.model.NavigateToBack = NavigateToBack;
            self.model.ValidateCredit = ValidateCredit;

            fromInwardApproval();
        }


        function fromInwardApproval() {
            var id = commonDetailService.getDataId();
            var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_INWARDS_APPROVAL.DETAIL_API + id;
            var data = {};
            GenericService.serviceAction("GET", URI, data).then(
            function (response) {
                self.model.inwardApprovalDetails = response.data;
                self.model.inwardApprovalDetails.items.forEach(function (item) {
                    item.isDisabled = true;
                    item.cop = item.cop ? item.cop.toFixed(2) : 0.00;
                    item.taxPercentage = item.taxPercentage ? item.taxPercentage.toFixed(2) : 0.00;
                    item.totalAmount = item.totalAmount ? item.totalAmount.toFixed(2) : 0.00;
                    item.taxAmount = item.taxAmount ? item.taxAmount.toFixed(2) : 0.00;
                    item.netAmount = item.netAmount ? item.netAmount.toFixed(2) : 0.00;
                    item.creditNetAmount = item.creditNetAmount ? item.creditNetAmount : 0;
                    item.creditNoteAmount = item.creditNoteAmount ? item.creditNoteAmount : 0;
                    item.creditTaxAmount = item.creditTaxAmount ? item.creditTaxAmount : 0;
                    if(self.model.inwardApprovalDetails.replacementTypeId == 8){
                        item.uom.find(function(u){
                            if(u.uomType == item.uomType){
                                item.uomType = u.uomDetails;
                            }
                        })
                        if(item.approvedQuantity != undefined && item.approvedQuantity != null && item.approvedQuantity != 0){
                            if(item.isThreeUOMApplicable == false){
                                item.uomListData = item.uom;
                                item.uom = [];
                                angular.forEach(item.uomListData, function(data){
                                    if(data.uomType != 'Stock'){
                                    item.uom.push(data);
                                    }
                                });
                                if(item.uomTypeId == 1){
                                    item.receivedQtyCon = (item.approvedQuantity +"*"+ item.uomListData[0].conversion);
                                    item.receivedQtyCalculation = (item.approvedQuantity * item.uomListData[0].conversion);
                                    item.uomUnitData = item.uomListData[2].uomUnit;
                                }
                                else if(item.uomTypeId == 2){
                                    
                                }
                                else{
                                    item.receivedQtyCon = (item.approvedQuantity);
                                    item.receivedQtyCalculation = (item.approvedQuantity);
                                    item.uomUnitData = item.uomListData[2].uomUnit;
                                }
                            }
                            else{
                                if(item.uomTypeId == 1){
                                    item.receivedQtyCon = (item.approvedQuantity +"*"+ item.uom[0].conversion +"*"+ item.uom[1].conversion);
                                    item.receivedQtyCalculation = (item.approvedQuantity * item.uom[0].conversion * item.uom[1].conversion);
                                    item.uomUnitData = item.uom[2].uomUnit;
                                }
                                else if(item.uomTypeId == 2){
                                    item.receivedQtyCon = (item.approvedQuantity +"*"+ item.uom[1].conversion);
                                    item.receivedQtyCalculation = (item.approvedQuantity * item.uom[1].conversion);
                                    item.uomUnitData = item.uom[2].uomUnit;
                                }
                                else{
                                    item.receivedQtyCon = (item.approvedQuantity);
                                    item.receivedQtyCalculation = (item.approvedQuantity);
                                    item.uomUnitData = item.uom[2].uomUnit;
                                }
                            }
                        }
                        else{
                            if(item.isThreeUOMApplicable == false){
                                item.uomListData = item.uom;
                                item.uom = [];
                                angular.forEach(item.uomListData, function(data){
                                    if(data.uomType != 'Stock'){
                                    item.uom.push(data);
                                    }
                                });
                                if(item.uomTypeId == 1){
                                    item.receivedQtyCon = (item.receivedQuantity +"*"+ item.uomListData[0].conversion);
                                    item.receivedQtyCalculation = (item.receivedQuantity * item.uomListData[0].conversion);
                                    item.uomUnitData = item.uomListData[2].uomUnit;
                                }
                                else if(item.uomTypeId == 2){
                                    
                                }
                                else{
                                    item.receivedQtyCon = (item.receivedQuantity);
                                    item.receivedQtyCalculation = (item.receivedQuantity);
                                    item.uomUnitData = item.uomListData[2].uomUnit;
                                }
                            }
                            else{
                                if(item.uomTypeId == 1){
                                    item.receivedQtyCon = (item.receivedQuantity +"*"+ item.uom[0].conversion +"*"+ item.uom[1].conversion);
                                    item.receivedQtyCalculation = (item.receivedQuantity * item.uom[0].conversion * item.uom[1].conversion);
                                    item.uomUnitData = item.uom[2].uomUnit;
                                }
                                else if(item.uomTypeId == 2){
                                    item.receivedQtyCon = (item.receivedQuantity +"*"+ item.uom[1].conversion);
                                    item.receivedQtyCalculation = (item.receivedQuantity * item.uom[1].conversion);
                                    item.uomUnitData = item.uom[2].uomUnit;
                                }
                                else{
                                    item.receivedQtyCon = (item.receivedQuantity);
                                    item.receivedQtyCalculation = (item.receivedQuantity);
                                    item.uomUnitData = item.uom[2].uomUnit;
                                }
                            }
                        }
                    }
                });
                self.model.inwardApprovalDetails.totalAmount = self.model.inwardApprovalDetails.totalAmount;
                //   self.model.inwardApprovalDetails.totalAmount = self.model.inwardApprovalDetails.totalAmount.toFixed(2);
                self.model.inwardApprovalDetails.totalNetAmount = self.model.inwardApprovalDetails.totalNetAmount.toFixed(2);
                self.model.inwardApprovalDetails.totalTaxAmount = self.model.inwardApprovalDetails.totalTaxAmount.toFixed(2);
            });
        }

        var SelectAllItems = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestIds = [];
                self.model.inwardApprovalDetails.items.forEach(function (item) {
                    // if (!!isDisable(item)) {
                        item.isSelected = true;
                        self.model.SelectedRequestIds.push(item.id);
                        item.isDisabled = false;
                        self.model.SelectedItems.push(item);
                    // }
                });
            } else {
                self.model.SelectedRequestIds = [];
                self.model.SelectedItems = [];
                self.model.inwardApprovalDetails.items.forEach(function (item) {
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
                //     items.splice(index, 1);
                // }
            }

            if (self.model.inwardApprovalDetails.items.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
        }


        var isSelected = function (item) {
            return item.isSelected;
        }

        function valueOnChange(item, isSelectAll) {
            if (isSelectAll)
                self.model.inwardApprovalDetails.items.forEach(function (item) {
                    if (self.model.selectAllRows)
                        item.approvedQuantity = item.receivedQuantity;
                    else
                        item.approvedQuantity = 0;
                });
            else
                if (item.isSelected)
                    item.approvedQuantity = item.receivedQuantity;
                else
                    item.approvedQuantity = 0;
        }


        var RejectionPopup = function (item) {
            // console.log(self.model.status);
            self.model.onclose = item;
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
            var prevRequest = self.model.inwardApprovalDetails.items.find(function (prevItem) {
                return prevItem.id == item.id;
            });

            if (prevRequest) {
                item.approvedQuantity = parseInt(prevRequest.approvedQuantity) + prevRequest.rejectedQuantity;
                item.rejectedQuantity = 0;
                item.rejectNote = prevRequest.rejectNote;
                item.rejectReasonId = prevRequest.rejectReasonId;
                item.statusId = prevRequest.statusId;
            }
            $scope.calculateReceivedQty(item);
        }

        var ModalDismiss = function (popupName) {
            var popup = angular.element('#' + popupName);
            popup.modal('hide');
            self.model.onclose.isReject = false;
            console.log("Item : ",self.model.onclose);
            console.log("Approved Qty ",self.model.onclose.receivedQuantity);
            self.model.onclose.approvedQuantity = self.model.onclose.receivedQuantity ;
            self.model.onclose.rejectedQuantity = 0;
        }


        function setStatus(x) {

            var status = self.model.statuses.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }

        $scope.validApprovedQty = function(item){
            var isValidQty = true;
            if(item.approvedQuantity > item.receivedQuantity){
                growl.error("Entered approved quantity is greater than received quantity");
                item.approvedQuantity = 0;
                isValidQty = false;
            }
            return isValidQty;
        }

        var ApprovedQuantity = function (item) {
            if($scope.validApprovedQty(item)){
                if (item.approvedQuantity > item.receivedQuantity) {
                    item.rejectedQuantity = 0;
                    self.model.rejectQty = undefined;
                    item.isReject = false;
                    item.status = setStatus('Approved');; // Approved.
                    RejectionPopup(item);
                } else if (item.approvedQuantity == 0 || !item.approvedQuantity) {
                    item.rejectedQuantity = item.quantity;
                    self.model.rejectQty = item.rejectedQuantity;
                    item.isReject = true;
                    item.status = setStatus('Rejected');; // Rejected
                    RejectionPopup(item);
                } else if (item.approvedQuantity < item.receivedQuantity) {
                    item.rejectedQuantity = item.receivedQuantity - item.approvedQuantity;
                    self.model.rejectQty = item.rejectedQuantity;
                    item.isReject = true;
                    item.status = setStatus('Partially Approved');; // Partially Approved
                    RejectionPopup(item);
                } else if (item.approvedQuantity == item.receivedQuantity) {
                    item.rejectedQuantity = item.receivedQuantity - item.approvedQuantity;
                    self.model.rejectQty = item.rejectedQuantity;
                    item.isReject = false;
                    item.status = setStatus('Approved');; // Approved.
                    RejectionPopup(item);
                }
            }
        }

        var SaveAcceptReject = function () {
            if (ValidateRejection()) {
                var rejectItemId = itemId;
                self.model.SelectedItems.forEach(function (item) {
                    if (item.id == rejectItemId) {
                        if (self.model.rejectQty > item.receivedQuantity) {
                            growl.error('Please Enter Valid Quantity');
                            return false;
                        } else if (self.model.rejectQty == item.receivedQuantity) {
                            item.rejectedQuantity = item.receivedQuantity;
                            item.approvedQuantity = item.receivedQuantity - item.rejectedQuantity;
                            item.rejectReasonId = self.model.rejectReasons[0].id;
                            item.statusId = setStatus('Rejected'); // Rejected
                        } else if (self.model.rejectQty == 0 || !self.model.rejectQty) {
                            item.approvedQuantity = item.receivedQuantity;
                            item.rejectedQuantity = 0;
                            item.rejectReasonId = self.model.rejectReasons[0].id;
                            item.statusId = setStatus('Pending'); // Approved
                        } else {
                            item.rejectedQuantity = self.model.rejectQty;
                            item.rejectNote = self.model.rejectNote;
                            item.approvedQuantity = item.receivedQuantity - item.rejectedQuantity;
                            item.rejectReasonId = self.model.rejectReasons[0].id;
                            item.statusId = setStatus('Partially Approved'); // Partially Approved
                        }
                        var popup = angular.element('#qtyaccepatReject');
                        popup.modal('hide');
                        if(item.rejectQuantity == 0){
                            item.isReject = false;
                        }
                        $scope.calculateReceivedQty(item);
                    }
                });
            }
        }

        $scope.calculateReceivedQty = function (item) {
			if(item.isThreeUOMApplicable == false){
				if(item.uomTypeId == 1){
					item.receivedQtyCon = (item.approvedQuantity +"*"+ item.uomListData[0].conversion);
					item.receivedQtyCalculation = (item.approvedQuantity * item.uomListData[0].conversion);
                    item.uomUnitData = item.uomListData[2].uomUnit;
				}
				else if(item.uomTypeId == 2){
					
				}
				else{
					item.receivedQtyCon = (item.approvedQuantity);
					item.receivedQtyCalculation = (item.approvedQuantity);
                    item.uomUnitData = item.uomListData[2].uomUnit;
				}
			}
			else{
				if(item.uomTypeId == 1){
					item.receivedQtyCon = (item.approvedQuantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
					item.receivedQtyCalculation = (item.approvedQuantity * item.uom[0].conversion * item.uom[1].conversion);
                    item.uomUnitData = item.uom[2].uomUnit;
				}
				else if(item.uomTypeId == 2){
					item.receivedQtyCon = (item.approvedQuantity +"*"+ item.uom[1].conversion);
					item.receivedQtyCalculation = (item.approvedQuantity * item.uom[1].conversion);
                    item.uomUnitData = item.uom[2].uomUnit;
				}
				else{
					item.receivedQtyCon = (item.approvedQuantity);
					item.receivedQtyCal = (item.approvedQuantity);
                    item.uomUnitData = item.uom[2].uomUnit;
				}
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


        $scope.saveBtnDisabled=false;
        var SaveApproval = function () {
        	$scope.saveBtnDisabled=true;
            if (ValidateApproval()) {
                var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_INWARDS_APPROVAL.APPROVE_API + commonDetailService.getDataId();
                var data = {
                    "statusId": setStatus('Closed'),
                    "assetTypeId":self.model.inwardApprovalDetails.assetTypeId,
                    "approvalStatusId": setStatus('Rejected'),
                    "item": [],
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId
                }

                $rootScope.startLoader();
                if(self.model.inwardApprovalDetails.replacementTypeId  == 8){
                    angular.forEach(self.model.SelectedItems, function (item) {
                        var newItem = { 
                            "id":item.id,
                            "approvedQuantity": parseInt(item.approvedQuantity),
                            "rejectQuantity": item.rejectedQuantity,
                            "rejectReasonId": item.rejectReasonId ,
                            "rejectNote": item.rejectNote ,
                            "creditNoteAmount": item.creditNoteAmount ? item.creditNoteAmount : undefined,
                            "creditTaxAmount": item.creditTaxAmount ? item.creditTaxAmount : undefined,
                            "creditNetAmount": item.creditNetAmount ? item.creditNetAmount : undefined,
                            "statusId":setStatus('Approved')
                        }

                        if (item.approvedQuantity > 0 && item.approvedQuantity < item.receivedQuantity) {
                            newItem.statusId = setStatus('Partially Approved');
    
                            if (data.approvalStatusId == setStatus('Rejected')) {
                                data.approvalStatusId = setStatus('Partially Approved');
                            }
                        }
                        else if (item.approvedQuantity >= item.receivedQuantity) {
                            data.approvalStatusId = setStatus('Approved');
    
                            if (data.approvalStatusId == setStatus('Rejected')) {
                                data.approvalStatusId = setStatus('Approved');
                            }
                        }
                        else if (item.rejectedQuantity >= item.receivedQuantity) {
                            newItem.statusId = setStatus('Rejected');
                        }
                        else if (item.rejectedQuantity > 0 && item.rejectedQuantity < item.receivedQuantity) {
                            newItem.statusId = setStatus('Partially Approved');
    
                            if (data.approvalStatusId == setStatus('Rejected')) {
                                data.approvalStatusId = setStatus('Approved');
                            }
                        }
    
                        data.item.push(newItem);
                      
                    });
                    var getLength=self.model.inwardApprovalDetails.items.length;
                    var count=0;
                    angular.forEach(self.model.inwardApprovalDetails.items, function (item) {
                  	  if(item.statusId==5 || item.statusId==6 ||item.statusId==7)
                  		  {
                  		  count++;
                  		  }
                    });
                    angular.forEach(self.model.SelectedItems, function (item) {
                  	  if(item.statusId==2)
                  		  {
                  		  count++;
                  		  }
                    });
                    if(getLength==count)
                    {
                      data.approvalStatusId = setStatus('Closed');
                      data.statusId = setStatus('Closed');
                      
                    }
                    else if(getLength==self.model.SelectedItems.length)
                  	  {
                  	  data.approvalStatusId = setStatus('Closed');
                  	data.statusId = setStatus('Closed');
                  	  }
                  	  else
                  	  {
                  	  data.approvalStatusId = setStatus('Partially Completed');
                  	data.statusId = setStatus('Partially Completed');
                  	  }
                }  
                else{
                    angular.forEach(self.model.inwardApprovalDetails.items, function (item) {
                        var newItem = { 
                            "id":item.id, 
                            "creditNoteAmount": item.creditNoteAmount ? item.creditNoteAmount : undefined,
                            "creditTaxAmount": item.creditTaxAmount ? item.creditTaxAmount : undefined,
                            "creditNetAmount": item.creditNetAmount ? item.creditNetAmount : undefined,
                            "statusId":setStatus('Approved')
                        }

                        if (item.approvedQuantity > 0 && item.approvedQuantity < item.receivedQuantity) {
                            newItem.statusId = setStatus('Partially Approved');

                            if (data.approvalStatusId == setStatus('Rejected')) {
                                data.approvalStatusId = setStatus('Partially Approved');
                            }
                        }
                        else if (item.approvedQuantity >= item.receivedQuantity) {
                            data.approvalStatusId = setStatus('Approved');

                            if (data.approvalStatusId == setStatus('Rejected')) {
                                data.approvalStatusId = setStatus('Approved');
                            }
                        }
                        else if (item.rejectedQuantity >= item.receivedQuantity) {
                            newItem.statusId = setStatus('Rejected');
                        }
                        else if (item.rejectedQuantity > 0 && item.rejectedQuantity < item.receivedQuantity) {
                            newItem.statusId = setStatus('Partially Approved');

                            if (data.approvalStatusId == setStatus('Rejected')) {
                                data.approvalStatusId = setStatus('Approved');
                            }
                        }

                        data.item.push(newItem);
                       
                    });
//                    var getLength=self.model.inwardApprovalDetails.items.length;
//                    var count=0;
                    angular.forEach(self.model.inwardApprovalDetails.items, function (item) {
                  	  if(item.creditNetAmount==parseFloat(item.netAmount) && item.creditNoteAmount==parseFloat(item.totalAmount)
                  			  && item.creditTaxAmount==parseFloat(item.taxAmount))
              		  {
                  		data.statusId = setStatus('Closed');
              		  }
                  	  else
                  		  {
                  		data.statusId = setStatus('Partially Completed');
                  		  }
                    });
//                    angular.forEach(self.model.SelectedItems, function (item) {
//                    	  if(item.statusId==2)
//                    		  {
//                    		  count++;
//                    		  }
//                      });
//                    if(getLength==count)
//                    {
//                      data.approvalStatusId = setStatus('Closed');
//                      data.statusId = setStatus('Closed');
//                    }
//                    else if(getLength==self.model.SelectedItems.length)
//                  	  {
//                  	  data.approvalStatusId = setStatus('Closed');
//                  	data.statusId = setStatus('Closed');
//                  	  }
//                  	  else
//                  	  {
//                  	  data.approvalStatusId = setStatus('Partially Completed');
//                  	data.statusId = setStatus('Partially Completed');
//                  	  }
                }
                GenericService.serviceAction("PATCH", URI, data).then(function (response) {
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
            if(self.model.inwardApprovalDetails.replacementTypeId == 8){
                if (!self.model.SelectedItems || self.model.SelectedItems.length == 0) {
                    growl.error('Select Items to Update and Save');
                    isValid = false;
                }
                angular.forEach(self.model.inwardApprovalDetails.items, function(data){
                    if(data.isSelected == true){
                        if(data.approvedQuantity == undefined || data.approvedQuantity == 0){
                            growl.error("Please enter quantity");
                            isValid = false;
                        }
                    }
                });
            }
            else
            {
                isValid  = true;
            }
            if(!isValid)
            {
            $scope.saveBtnDisabled=false;
            }
            return isValid;
        }

        var NavigateToBack = function () {
            $state.go('materialInwardsApproval');
        } 

        var ValidateCredit = function(type, item) {
            if(type == "Note") {
                if(Number(item.creditNoteAmount) > Number(item.totalAmount)){
                    self.model.isCreditNoteValid = true;
                    growl.error("Credit Note " + self.model.Currency + " must be less than or equals to Total " + self.model.Currency);
                    item.creditNoteAmount = item.totalAmount;
                } else {
                    self.model.isCreditNoteValid = false;
                    var taxamount = item.creditTaxAmount ? item.creditTaxAmount : 0;
                    item.creditNetAmount = Number(item.creditNoteAmount) + Number(taxamount);
                    if(Number(item.creditNetAmount) > Number(item.totalNetAmount)){
                        self.model.isCreditNetValid = true;
                        growl.error("Credit Net " + self.model.Currency + " must be less than or equals to Net " + self.model.Currency);
                        item.creditNetAmount = item.totalNetAmount;
                    } else {
                        self.model.isCreditNetValid = false;
                    }
                }
            } else if(type == "Tax") {
                if(Number(item.creditTaxAmount) > Number(item.totalTaxAmount)){
                    self.model.isCreditTaxValid = true;
                    growl.error("Credit Tax " + self.model.Currency + " must be less than or equals to Total Tax " + self.model.Currency);
                    item.creditTaxAmount = item.totalTaxAmount;
                } else {
                    self.model.isCreditTaxValid = false;
                    var creditmount = item.creditNoteAmount ? item.creditNoteAmount : 0;
                    item.creditNetAmount = Number(item.creditTaxAmount) + Number(creditmount);
                    if(Number(item.creditNetAmount) > Number(item.totalNetAmount)){
                        self.model.isCreditNetValid = true;
                        growl.error("Credit Net " + self.model.Currency + " must be less than or equals to Net " + self.model.Currency);
                        item.creditNetAmount = item.totalNetAmount;
                    } else {
                        self.model.isCreditNetValid = false;
                    }
                }
            } else if(type == "Net") {
                if(Number(item.creditNetAmount) > Number(item.totalNetAmount)){
                    self.model.isCreditNetValid = true;
                    growl.error("Credit Net " + self.model.Currency + " must be less than or equals to Net " + self.model.Currency);
                    item.creditNetAmount = item.totalNetAmount;
                } else {
                    self.model.isCreditNetValid = false;
                }
            }
            item.creditNetAmount=parseFloat(item.creditNoteAmount)+parseFloat(item.creditTaxAmount);
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("detailsMaterialInwardsApproval", {
            url: "/detailsMaterialInwardsApproval",
            templateUrl: 'views/PharmacyInventory/Store/details-material-inwords-approval.html',
            controller: "MaterialInwardApprovalDetails.Controller",
            controllerAs: "vm",
            title: 'Material Inward Approval'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("MaterialInwardApprovalDetails.Controller", materialInwardApprovalDetailsController);
})();
