(function () {
    "use strict";

    function confirmStockDetailsController($scope, $rootScope, $http, commonDetailService, StatusService, $state, rejectReasonService, $cookies, CONSTANTS, GenericService, growl) {
        var self = this;
        var itemId;
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
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
            self.model.fromConfirmStock = fromConfirmStock
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
            self.model.ValidateAndUpdate = ValidateAndUpdate;
            self.model.RejectConfirmStock = RejectConfirmStock;
            self.model.isRejected = false;           
            self.model.SelectedItems = [];       
            fromConfirmStock();
        }    
                      
        var ValidateAndUpdate = function (data, popupName, id) {
            $scope.computeDetailId = data;            
            var popup = angular.element('#' + popupName);
            popup.modal('show');
        }
        
        var RejectConfirmStock = function () {
            $scope.count = 0;
            angular.forEach(self.model.confirmStockDetail.items, function(value, key){
                if($scope.computeDetailId == value.id){
                    value.status = 'Rejected';
                }
                if(value.status == 'Rejected'){
                    $scope.count++;
                }
            });
            if($scope.count == self.model.confirmStockDetail.items.length){
                $scope.statusId = 7;
            }
            else{
                $scope.statusId = 2;
            }
        	self.model.isRejected = true;
            var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.CONFIRM_STOCK.REJECT_CONFIRM_STOCK + $scope.computeDetailId + "/" + $scope.unitId + "/" + $scope.statusId;
                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, "").then(
                    function (response) {
                    	$rootScope.stopLoader();
                        growl.success("Confirm Stock Rejected Successfully and move to Upload Stock Approval.");
                        fromConfirmStock();
                        self.model.isRejected = false; 
                        self.model.SelectedItems = [];
                        self.model.selectAllRows = false;
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.msg);
                        self.model.isRejected = false;
                        self.model.SelectedItems = [];
                        self.model.selectAllRows = false;
                    });
        }
       


        function fromConfirmStock() {
            var id = commonDetailService.getDataId();
            var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.CONFIRM_STOCK.DETAIL_API + id + "/" + $scope.unitId;
            var data = {};
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, data).then(
                function (response) {
                	$rootScope.stopLoader();
                    self.model.confirmStockDetail = response.data;
                    self.model.confirmStockDetail.items.forEach(function (item) {
                        item.isDisabled = true;
                        item.cop = item.cop.toFixed(2); 
                        item.mrp = item.mrp.toFixed(2); 
                        
                        if(item.status == 'Approved')
                        	$scope.isUpdate = true;
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
                self.model.confirmStockDetail.items.forEach(function (item) {
                	if(item.status != 'Rejected'){
                		  if (!!isDisable(item)) {
                              item.isSelected = true;
                              self.model.SelectedRequestIds.push(item.id);
                              item.isDisabled = false;
                              self.model.SelectedItems.push(item);
                          }
                	}                  
                });
            } else {
                self.model.SelectedRequestIds = [];
                self.model.SelectedItems = [];
                self.model.confirmStockDetail.items.forEach(function (item) {
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

            if (self.model.confirmStockDetail.items.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
        }


        var isSelected = function (item) {
            return item.isSelected;
        }

        var ApprovedQuantity = function (item) {
            if (item.acceptedQuantity > item.adjustmentQuantity) {
                item.rejectedQuantity = 0;
                self.model.rejectQty = undefined;
                item.isReject = false;
                item.status = setStatus('Approved');; // Approved.
            } else if (item.acceptedQuantity == 0 || !item.acceptedQuantity) {
                item.rejectedQuantity = item.quantity;
                self.model.rejectQty = item.rejectedQuantity;
                item.isReject = true;
                item.status = setStatus('Rejected');; // Rejected
            } else if (item.acceptedQuantity < item.adjustmentQuantity) {
                item.rejectedQuantity = item.adjustmentQuantity - item.acceptedQuantity;
                self.model.rejectQty = item.rejectedQuantity;
                item.isReject = true;
                item.status = setStatus('Partially Approved');; // Partially Approved
            } else if (item.acceptedQuantity == item.adjustmentQuantity) {
                item.rejectedQuantity = item.adjustmentQuantity - item.acceptedQuantity;
                self.model.rejectQty = item.rejectedQuantity;
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
                        if (self.model.rejectQty > item.adjustmentQuantity) {
                            growl.error('Please Enter Valid Quantity');
                            return false;
                        } else if (self.model.rejectQty == item.adjustmentQuantity) {
                            item.rejectedQuantity = item.adjustmentQuantity;
                            item.acceptedQuantity = item.adjustmentQuantity - item.rejectQuantity;
                            item.rejectReasonId = self.model.rejectReasons[0].id;
                            item.statusId = setStatus('Rejected'); // Rejected
                        } else if (self.model.rejectQty == 0 || !self.model.rejectQty) {
                            item.acceptedQuantity = item.adjustmentQuantity;
                            item.rejectedQuantity = 0;
                            item.rejectReasonId = self.model.rejectReasons[0].id;
                            item.statusId = setStatus('Pending'); // Approved
                        } else {
                            item.rejectedQuantity = self.model.rejectQty;
                            item.rejectNote = self.model.rejectNote;
                            item.acceptedQuantity = item.adjustmentQuantity - item.rejectQuantity;
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
                self.model.confirmStockDetail.items.forEach(function (item) {
                    if (self.model.selectAllRows)
                        item.acceptedQuantity = item.adjustmentQuantity;
                    else
                        item.acceptedQuantity = 0;
                });
            else
                if (item.isSelected)
                    item.acceptedQuantity = item.adjustmentQuantity;
                else
                    item.acceptedQuantity = 0;
        }

        var SaveApproval = function () {

            if (ValidateApproval()) {
                var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.CONFIRM_STOCK.DETAIL_APPROVE_API + commonDetailService.getDataId();
                var data = {
                    "statusId": setStatus('Approved'),
                    "items": [],
                    "unitId": $scope.unitId,
                    "userId": $scope.userId,
                    "organizationId": $scope.organizationId
                }

                $rootScope.startLoader();
                angular.forEach(self.model.SelectedItems, function (item) {
                	if(item.status != 'Rejected'){
                		var newItem = {
                                "id": item.id,
                                "approvalQunatity": parseInt(item.approvalQuantity) ? item.approvalQuantity : 0,
                                //"rejectQuantity": parseInt(item.rejectQunatity),
                                "rejectReasonId": item.rejectReasonId ? item.rejectReasonId : 1,
                                "rejectNote": item.rejectNote ? item.rejectNote : "default note",
                                //"statusId": item.status ? item.status : 6
                                "statusId": 6
                            }

                            if((parseInt(item.approvalQuantity) < parseInt(item.quantity))){
                                var rejectedQuantity = parseInt(item.quantity) - parseInt(item.approvalQuantity);
                                  newItem.rejectQuantity = rejectedQuantity;
                              }else{
                               newItem.rejectQuantity  = 0;
                            }  
                            data.items.push(newItem);
                	}
                    
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
//        var SaveApproval = function () {
//
//            if (ValidateApproval()) {
//                var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.CONFIRM_STOCK.DETAIL_APPROVE_API + commonDetailService.getDataId();
//                var data = {
//                    "statusId": setStatus('Closed'),
//                    "items": []
//                }
//
//                $rootScope.startLoader();
//                angular.forEach(self.model.SelectedItems, function (item) {
//                    var newItem = {
//                        "id": item.id,
//                        "approvalQunatity": parseInt(item.acceptedQuantity) ? item.acceptedQuantity : 0,
//                        "rejectQuantity": parseInt(item.rejectedQuantity),
//                        "rejectReasonId": item.rejectReasonId ? item.rejectReasonId : 1,
//                        "rejectNote": item.rejectNote ? item.rejectNote : "default note",
//                        "statusId": item.status ? item.status : 6
//                    }
//
//                    if (item.acceptedQuantity > 0 && item.acceptedQuantity < item.adjustmentQuantity) {
//                        newItem.statusId = setStatus('Partially Approved');  
//                    } 
//                    else if (item.rejectedQuantity >= item.adjustmentQuantity) {
//                        newItem.statusId = setStatus('Rejected');
//                    }
//                    else if (item.rejectedQuantity > 0 && item.rejectedQuantity < item.adjustmentQuantity) {
//                        newItem.statusId = setStatus('Partially Approved');
//                    }
//
//                    data.items.push(newItem);
//                });
//
//                GenericService.serviceAction("PATCH", URI, data).then(
//                    function (response) {
//                        $rootScope.stopLoader();
//                        growl.success(response.data.message);
//                        NavigateToBack();
//                    }, function (err) {
//                        $rootScope.stopLoader();
//                        growl.error('Somthing Went Wrong');
//                    });
//            }
//        }

        var ValidateApproval = function () {
            var isValid = true;

            if (!self.model.SelectedItems || self.model.SelectedItems.length == 0) {
                growl.error('Select Items to Update and Save');
                isValid = false;
            }

            return isValid;
        }

        var NavigateToBack = function () {
            $state.go('confirmStock');
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
                item.rejectedQuantity = prevRequest.rejectedQuantity;
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

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("detailsConfirmStock", {
            url: "/detailsConfirmStock",
            templateUrl: 'views/PharmacyInventory/StockAdjustment/details-confirm-stock.html',
            controller: "ConfirmStockDetails.Controller",
            controllerAs: "vm",
            title: 'Confirm Stock'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("ConfirmStockDetails.Controller", confirmStockDetailsController);
})();
