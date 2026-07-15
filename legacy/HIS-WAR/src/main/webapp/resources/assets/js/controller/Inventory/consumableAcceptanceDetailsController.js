
(function () {
    "use strict";

    function consumableAcceptanceDetailsController($scope, $rootScope, $http, StatusService, $state, commonDetailService, rejectReasonService, $cookies, CONSTANTS, GenericService, growl) {
        var self = this;
        var itemId;
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;
            self.model = {
                SelectedItems: [],
                selectItemList: [],
                selectAllRows: false

            };
            StatusService.GetStatus().then(function (status) {
                self.model.statuses = status;
            });
            rejectReasonService.GetReason().then(function (reason) {
                self.model.rejectReasons = reason;
            });
            self.model.fromConsumableAcceptance = fromConsumableAcceptance;
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
            self.model.ValidateQuantity = ValidateQuantity;
            self.model.ModalDismissOnClose = ModalDismissOnClose;;
            self.model.ChangeItemStatus = ChangeItemStatus;


            fromConsumableAcceptance();

        }
        function fromConsumableAcceptance() {
            var id = commonDetailService.getDataId();
            var URI = CONSTANTS.INVENTORY.NURSING.CONSUMABLE_ACCEPTANCE.DETAIL_API + id;
            var data = {};
            GenericService.serviceAction("GET", URI, data).then(
                function (response) {
                    self.model.consumableDetail = response.data;
                    self.model.consumableDetail.batch.forEach(function (item) {
                        var uomData = item.uom.find(function (obj) {
                            return obj.uomType == item.uomType;
                          }); 
                        var uomTypeId = uomData.uomTypeId;
          
                        if(item.isThreeUOMApplicable == false){
                            if(uomTypeId == 1){
                                item.receivedQtyCon = (item.consumedQuantity +"*"+ item.uom[0].conversion);
                                item.receivedQtyCalculation = (item.consumedQuantity * item.uom[0].conversion);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                            else if(uomTypeId == 2){
                                
                            }
                            else{
                                item.receivedQtyCon = (item.consumedQuantity);
                                item.receivedQtyCalculation = (item.consumedQuantity);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                        }
                        else{
                            if(uomTypeId == 1){
                                item.receivedQtyCon = (item.consumedQuantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
                                item.receivedQtyCalculation = (item.consumedQuantity * item.uom[0].conversion * item.uom[1].conversion);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                            else if(uomTypeId == 2){
                                item.receivedQtyCon = (item.consumedQuantity +"*"+ item.uom[1].conversion);
                                item.receivedQtyCalculation = (item.consumedQuantity * item.uom[1].conversion);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                            else{
                                item.receivedQtyCon = (item.consumedQuantity);
                                item.receivedQtyCalculation = (item.consumedQuantity);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                        }
                        item.isDisabled = true;
                        item.acceptQuantity = item.acceptQuantity || item.quantityIssued;
                        if (!!item.acceptQuantity && !!item.quantityIssued) {
                            ChangeItemStatus(item);
                        }
                        if (item.rejectedQuantity)
                            item.isReject = true;
                    });
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
        var SelectAllItems = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestIds = [];
                self.model.consumableDetail.batch.forEach(function (items) {
                    if (!!isDisable(items)) {
                        items.isSelected = true;
                        self.model.SelectedRequestIds.push(items.id);
                        items.isDisabled = false;
                        self.model.SelectedItems.push(items);
                    }
                });
            } else {
                self.model.SelectedRequestIds = [];
                self.model.SelectedItems = [];
                self.model.consumableDetail.batch.forEach(function (items) {
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

            if (self.model.consumableDetail.batch.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
        }



        var isSelected = function (batch) {
            return batch.isSelected;
        }

        var ApprovedQuantity = function (batch) {
            if (batch.acceptedQuantity > batch.consumedQuantity) {
                batch.rejectedQuantity = 0;
                self.model.rejectQty = undefined;
                batch.isReject = false;
                batch.status = setStatus('Approved');; // Approved.
            } else if (batch.acceptedQuantity == 0 || !batch.acceptedQuantity) {
                batch.rejectedQuantity = batch.consumedQuantity;
                self.model.rejectQty = batch.rejectedQuantity;
                batch.isReject = true;
                batch.status = setStatus('Rejected');; // Rejected
            } else if (batch.acceptedQuantity < batch.consumedQuantity) {
                batch.rejectedQuantity = batch.consumedQuantity - batch.acceptedQuantity;
                self.model.rejectQty = batch.rejectedQuantity;
                batch.isReject = true;
                batch.status = setStatus('Approved');; // Partially Approved
            } else if (batch.acceptedQuantity == batch.consumedQuantity) {
                batch.rejectedQuantity = batch.consumedQuantity - batch.acceptedQuantity;
                self.model.rejectQty = batch.rejectedQuantity;
                batch.isReject = false;
                batch.status = setStatus('Approved');; // Approved.
            }
            $scope.calculateReceivedQty(batch);
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
                self.model.SelectedItems.forEach(function (batch) {
                    if (batch.isReject == true) {
                        if (self.model.rejectQty > batch.consumedQuantity) {
                            growl.error('Please Enter Valid Quantity');
                            return false;
                        } else if (self.model.rejectQty == batch.consumedQuantity) {
                            batch.rejectedQuantity = batch.consumedQuantity;
                            batch.acceptedQuantity = 0;
                            batch.rejectReasonId = self.model.rejectReasons[0].id;
                            batch.statusId = setStatus('Rejected'); // Rejected
                        } else if (self.model.rejectQty == 0 || !self.model.rejectQty) {
                            batch.acceptedQuantity = batch.consumedQuantity;
                            batch.rejectedQuantity = 0;
                            batch.rejectReasonId = self.model.rejectReasons[0].id;
                            batch.statusId = setStatus('Pending'); // Approved
                        } else {
                            batch.rejectedQuantity = self.model.rejectQty;
                            batch.rejectNote = self.model.rejectNote;
                            batch.acceptedQuantity = batch.consumedQuantity - batch.rejectedQuantity;
                            batch.rejectReasonId = self.model.rejectReasons[0].id;
                            batch.statusId = setStatus('Partially Approved'); // Partially Approved
                        }
                        $scope.calculateReceivedQty(batch);
                        ModalDismiss('qtyaccepatReject');
                    }
                });
            }
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

        function valueOnChange(batch, isSelectAll) {
            if (isSelectAll)
                self.model.consumableDetail.batch.forEach(function (batch) {
                    if (self.model.selectAllRows)
                        batch.acceptedQuantity = batch.consumedQuantity;
                    else
                        batch.acceptedQuantity = 0;
                });
            else
                if (batch.isSelected)
                    batch.acceptedQuantity = batch.consumedQuantity;
                else
                    batch.acceptedQuantity = 0;
        }

        var SaveApproval = function () {

            if (ValidateApproval()) {
                var URI = CONSTANTS.INVENTORY.NURSING.CONSUMABLE_ACCEPTANCE.DETAIL_APPROVE_API + commonDetailService.getDataId();
                var data = {
                    "statusId": setStatus('Closed'),
                    "approvalStatusId": setStatus('Rejected'),
                    "batch": [],
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId

                }
                
                $rootScope.startLoader();
                $scope.saveCount = 0;
                angular.forEach(self.model.SelectedItems, function (item) {
                    var newItem = {
                        "id": item.id,
                        "acceptedQuantity": parseInt(item.acceptedQuantity) ? item.acceptedQuantity : 0,
                        "rejectedQuantity": parseInt(item.rejectedQuantity),
                        "rejectReasonId": self.model.rejectReason,
                        "rejectNote": self.model.rejectNote,
                        "statusId": item.status ? item.status : 6
                    }
                    $scope.saveCount = $scope.saveCount + 1;
                    if (item.acceptedQuantity > 0 && item.acceptedQuantity < item.consumedQuantity) {
                        newItem.statusId = setStatus('Partially Approved');

                        if (data.approvalStatusId == setStatus('Rejected')) {
                            data.approvalStatusId = setStatus('Partially Approved');
                        }
                    }
                    else if (item.acceptedQuantity >= item.consumedQuantity) {
                        data.approvalStatusId = setStatus('Approved');

                        if (data.approvalStatusId == setStatus('Rejected')) {
                            data.approvalStatusId = setStatus('Approved');
                        }
                    }
                    else if (item.rejectedQuantity >= item.consumedQuantity) {
                        newItem.statusId = setStatus('Rejected');
                    }
                    else if (item.rejectedQuantity > 0 && item.rejectedQuantity < item.consumedQuantity) {
                        newItem.statusId = setStatus('Partially Approved');

                        if (data.approvalStatusId == setStatus('Rejected')) {
                            data.approvalStatusId = setStatus('Approved');
                        }
                    }

                    data.batch.push(newItem);
                });
                
                
                $scope.getCount = 0;
                self.model.consumableDetail.batch.forEach(function (item) {
                	 if (item.statusId ==10 || (item.rejectedQuantity =!undefined && item.rejectedQuantity != null && item.rejectedQuantity>0)) {
                     	$scope.getCount = $scope.getCount + 1;
                     }
                });
                
                if($scope.getCount > $scope.saveCount){
                	 data.statusId = setStatus('Partially Completed');
                	 data.approvalStatusId = setStatus('Partially Approved');
                }
                
                
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

            if (!self.model.SelectedItems || self.model.SelectedItems.length == 0) {
                growl.error('Select Items to Approve');
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
                growl.error("Please Enter Consumed Quantity");
            }

            else if (parseInt(item.acceptedQuantity) > item.consumedQuantity) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error('Entered  accept quantity is exceeding the consumed quantity');
            } else {
                item.acceptedQuantity = parseInt(item.acceptedQuantity);
                isValid = true;
            }

            return isValid;
        }

        var NavigateToBack = function () {
            $state.go('consumableAcceptance');
        }

        var RejectionPopup = function (batch) {
            self.model.onclose = batch;
            self.model.rejectQty = '';
            self.model.rejectReason = '';
            self.model.rejectNote = '';


            // console.log(self.model.status);
            if (batch.isReject) {
                itemId = batch.id;
                // self.model.rejectQty = item.rejectQuantity;
                self.model.action = 'Open';
                var popup = angular.element('#qtyaccepatReject');
                popup.modal('show');
                self.model.reasons = self.model.rejectReasons;
                // self.model.rejectReason = self.model.reasons[0];
            } else {
                CancelRejection(batch);
            }
            $scope.calculateReceivedQty(batch);
        }

        var CancelRejection = function (batch) {
            // var prevRequest = self.model.requestItems.batch.find(function (prevItem) {
            //     return prevItem.id == batch.id;
            // });

            // if (prevRequest) {
                batch.rejectedQuantity = 0;
                batch.acceptedQuantity = batch.consumedQuantity;
                batch.rejectNote = batch.rejectNote;
                batch.rejectReasonId = batch.rejectReasonId;
                batch.statusId = batch.statusId;

            // }
        }

        var ChangeItemStatus = function (item) {
            self.model.selectedItem = item;
            if (!!item.rejectedQuantity) {
                item.isReject = true;
                self.model.reasons = self.model.rejectReasons;
                self.model.action = 'Open';
                self.model.qty = Number(item.consumedQuantity) - Number(item.acceptedQuantity);
                var popup = angular.element('#qtyaccepatReject');
                popup.modal('show');
            }
        }

        var ModalDismiss = function (popupName) {
            var popup = angular.element('#' + popupName);
            popup.modal('hide');
        }

        var ModalDismissOnClose = function (popupName) {
            var popup = angular.element('#' + popupName);
            popup.modal('hide');
            // self.model.onclose.isReject = false;
            self.model.consumableDetail.batch.forEach(function(data){
                if(data.isSelected == true){
                    data.isReject = false;
                    data.acceptedQuantity = data.rejectedQuantity + data.acceptedQuantity;
                    data.rejectedQuantity = 0;  
                }
                $scope.calculateReceivedQty(data);
            });
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("detailsConsumableAcceptance", {
            url: "/detailsConsumableAcceptance",
            templateUrl: 'views/PharmacyInventory/InPatient/details-consumable-acceptance.html',
            controller: "ConsumableAcceptanceDetails.Controller",
            controllerAs: "vm",
            title: 'Consumable Acceptance'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("ConsumableAcceptanceDetails.Controller", consumableAcceptanceDetailsController);
})(); 
