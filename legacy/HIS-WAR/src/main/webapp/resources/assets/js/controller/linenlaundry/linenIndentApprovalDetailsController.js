(function () {
    "use strict";

    function linenIndentApprovalDetailsController($scope, $rootScope, StatusService, rejectReasonService, commonDetailService,$state, $http, $cookies, CONSTANTS, GenericService, growl) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
        function initializeController() {
            $rootScope.loginpage = true;
            self.model = {
                selectAllRows: false,
                SelectedItems: [],
                SelectedRequestIds: []

            };
            
            StatusService.GetStatus().then(function (status) {
                self.model.statuses = status;
            });

            rejectReasonService.GetReason().then(function (reason) {
                self.model.rejectReasons = reason;
            });

            self.model.fromLinenIndentApproval = fromLinenIndentApproval;
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

            fromLinenIndentApproval();


        }

        function fromLinenIndentApproval() {
            var id = commonDetailService.getDataId();
            var URI = CONSTANTS.LINEN_LAUNDRY.LINEN_INDENT_APPROVAL.DETAIL_API + $scope.unitId + '/'+ $scope.organizationId + '/' + id;
            var data = {};
            GenericService.serviceAction("GET", URI, data).then(
                function (response) {
                    self.model.indentApprovalDetail = response.data;
                    self.model.indentApprovalDetail.items.forEach(function (item) {
                        item.isDisabled = true;

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
            if (item.statusId != 10) {
                return true;
            }
            return false;
        }


        var SelectAllItems = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestIds = [];
                self.model.indentApprovalDetail.items.forEach(function (item) {
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
                self.model.indentApprovalDetail.items.forEach(function (item) {
                    item.isSelected = false;
                    item.isDisabled = true;
                });
            }
        }

        var SelectItem = function (item) {

            var itemes = self.model.SelectedItems;

            if (item.isSelected) {
                itemes.push(item);
                item.isDisabled = false;
            } else {
                item.isDisabled = true;
                item.isReject = false;
                // var index = item.indexOf(item);
                // if (index > -1) {
                //     itemes.splice(index, 1);
                // }
            }

            if (self.model.indentApprovalDetail.items.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
        }


        var isSelected = function (item) {
            return item.isSelected;
        }

        var ApprovedQuantity = function (item) {
            if (item.approvedQuantity > item.quantity) {
                item.rejectedQuantity = 0;
                self.model.rejectQty = undefined;
                item.isReject = false;
                item.status = setStatus('Approved');; // Approved.
            } else if (item.approvedQuantity == 0 || !item.approvedQuantity) {
                item.rejectedQuantity = item.quantity;
                self.model.rejectQty = item.rejectedQuantity;
                item.isReject = true;
                item.status = setStatus('Rejected');; // Rejected
            } else if (item.approvedQuantity < item.quantity) {
                item.rejectedQuantity = item.quantity - item.approvedQuantity;
                self.model.rejectQty = item.rejectedQuantity;
                item.isReject = true;
                item.status = setStatus('Partially Approved');; // Partially Approved
            } else if (item.approvedQuantity == item.quantity) {
                item.rejectedQuantity = item.quantity - item.approvedQuantity;
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
                        if (self.model.rejectQty > item.quantity) {
                            growl.error('Please Enter Valid Quantity');
                            return false;
                        } else if (self.model.rejectQty == item.quantity) {
                            item.rejectedQuantity = item.quantity;
                            item.approvedQuantity = item.quantity - item.rejectQuantity;
                            item.rejectReasonId = self.model.rejectReasons[0].id;
                            item.statusId = setStatus('Rejected'); // Rejected
                        } else if (self.model.rejectQty == 0 || !self.model.rejectQty) {
                            item.approvedQuantity = item.quantity;
                            item.rejectedQuantity = 0;
                            item.rejectReasonId = self.model.rejectReasons[0].id;
                            item.statusId = setStatus('Pending'); // Approved
                        } else {
                            item.rejectedQuantity = self.model.rejectQty;
                            item.rejectNote = self.model.rejectNote;
                            item.approvedQuantity = item.quantity - item.rejectQuantity;
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
                self.model.indentApprovalDetail.items.forEach(function (item) {
                    if (self.model.selectAllRows)
                        item.approvedQuantity = item.quantity;
                    else
                        item.approvedQuantity = 0;
                });
            else
                if (item.isSelected)
                    item.approvedQuantity = item.quantity;
                else
                    item.approvedQuantity = 0;
        }

        var SaveApproval = function () {

            if (ValidateApproval()) {
                var URI = CONSTANTS.LINEN_LAUNDRY.LINEN_INDENT_APPROVAL.DETAIL_APPROVE_API + commonDetailService.getDataId();
                var data = {
                    "statusId": setStatus('Closed'),
                    "approvalStatusId": setStatus('Rejected'),
                    "items": [],
                    "userId":$scope.userId,
                    "organizationId":$scope.organizationId,
					"unitId":$scope.unitId,
                    
                }

                $rootScope.startLoader();
                angular.forEach(self.model.SelectedItems, function (item) {
                    var newItem = {
                        "id": item.id,
                        "approvedQuantity": parseInt(item.approvedQuantity) ? item.approvedQuantity : 0,
                        "rejectedQty": parseInt(item.rejectedQuantity),
                        "rejectReasonId": self.model.rejectReasonId ? self.model.rejectReasonId : 1,
                        "rejectNote": self.model.rejectNote ? self.model.rejectNote : "default note",
                        "statusId": item.status ? item.status : 6
                    }

                    if (item.approvedQuantity > 0 && item.approvedQuantity < item.quantity) {
                        newItem.statusId = setStatus('Partially Approved');

                        if (data.approvalStatusId == setStatus('Reject')) {
                            data.approvalStatusId = setStatus('Partially Approved');
                        }
                    }
                    else if (item.approvedQuantity >= item.quantity) {
                        data.approvalStatusId = setStatus('Approved');

                        if (data.approvalStatusId == setStatus('Reject')) {
                            data.approvalStatusId = setStatus('Approved');
                        }
                    }
                    else if (item.rejectedQuantity >= item.quantity) {
                        newItem.statusId = setStatus('Rejected');
                    }
                    else if (item.rejectedQuantity > 0 && item.rejectedQuantity < item.quantity) {
                        newItem.statusId = setStatus('Partially Approved');

                        if (data.approvalStatusId == setStatus('Reject')) {
                            data.approvalStatusId = setStatus('Approved');
                        }
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
        }

        var ValidateApproval = function () {
            var isValid = true;

            if (!self.model.SelectedItems || self.model.SelectedItems.length == 0) {
                growl.error('Select Items to Update and Save');
                isValid = false;
            }

            return isValid;
        }
        
        var NavigateToBack = function () {
            $state.go('linenIndentApproval');
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
                item.approvedQuantity = prevRequest.approvedQuantity;
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
        $stateProvider.state("linenIndentApprovalDetails", {
            url: "/linenIndentApprovalDetails",
            templateUrl: 'views/Linen/linen-indent-approval-details.html',
            controller: "LinenIndentApprovalDetail.Controller",
            controllerAs: "vm"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("LinenIndentApprovalDetail.Controller", linenIndentApprovalDetailsController);
})();

