(function () {
    "use strict";

    function detailsConsignmentPOApprovalController($scope, $rootScope, $location,  $http, StatusService, $cookies, CONSTANTS, GenericService, $state, growl, commonDetailService, CurrencyService) {
        var self = this;
        var itemId;

        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;
            self.model = {
                SelectedItems: [],
                isConsignment: false,
                selectAllRows: false,
                selectedIndex: null,
                purchaseOrders: {
                    items: []
                }
            };

            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            });

            self.model.NavigateToBack = NavigateToBack;
            self.model.SelectAllItems = SelectAllItems;
            self.model.SelectItem = SelectItem;
            self.model.ModalDismiss = ModalDismiss;
            self.model.SavePOApproval = SavePOApproval;
            self.model.isDisable = isDisable;
            self.model.OpenPopup = OpenPopup;
            self.model.ClosePopup = ClosePopup;
            self.model.ValidateSaveButton = ValidateSaveButton;

            GetPurchaseOrders();
        }

        StatusService.GetStatus().then(function (status) {
            self.model.statuses = status;
        });

        function setStatus(x) {
            var status = self.model.statuses.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }
 
        var ValidateSaveButton = function () {
            var isValid = false;
            if (self.model.purchaseOrders.items) {
                self.model.purchaseOrders.items.find(function (item) {
                    if (!!item.isSelected) {
                        isValid = true;
                    }
                });
                return isValid;
            }
        }

        var GetPurchaseOrders = function () {
    
            var URI = CONSTANTS.PROCUREMENT.CONSIGNMENT.CONSIGNMENT_PO_APPROVAL.DETAILS_API + $scope.unitId + '/' + commonDetailService.getDataId();
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.purchaseOrders = response.data;
                    self.model.id=response.data.id;
                    self.model.remark = response.data.remark;
                    self.model.totalAmount=response.data.totalAmount;
                    self.model.totalTaxAmount=response.data.taxAmount
                    self.model.netAmount=response.data.netAmount
                    self.model.requestItems = JSON.parse(JSON.stringify(response.data.items));
                    self.model.purchaseOrders.items.forEach(function (item) {
                        item.isDisabled = true;
                        item.fillRejectQtyFlag = true;
                    });

                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error(err.data.message);
                });
        }


        var ModalDismiss = function (popupName) {
            var popup = angular.element('#' + popupName);
            popup.modal('hide');
            self.model.rejectNote = undefined;
        }

        var ValidateApproval = function () {
            var isValid = true;

            if (!self.model.SelectedItems || self.model.SelectedItems.length == 0) {
                growl.error('Please select items to update and save');
                isValid = false;
            }
            return isValid;
        }

        var SavePOApproval = function () {
            if (ValidateApproval()) {
                var URI = CONSTANTS.PROCUREMENT.CONSIGNMENT.CONSIGNMENT_PO_APPROVAL.UPDATE_API + commonDetailService.getDataId();

                var data = {
                    "statusId": 8,
                    "approvalStatusId": 8,
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId
                }


                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        growl.success(response.data.message);
                        NavigateToBack();
                    }, function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.message);
                    });
            }
        }

        var isSelected = function (item) {
            return item.isSelected;
        }

        var isDisable = function (item) {
            if (item.status !== "Pending Approval") {
                return true;
            }
            return false;
        }

        var SelectAllItems = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedItems = [];
                self.model.purchaseOrders.items.forEach(function (item) {
                    if (!isDisable(item)) {
                        item.isSelected = true;
                        item.isDisabled = false;
                        item.statusId = 5;
                        item.purchaseOrderStagedDto.forEach(function (x) {
                            x.approvedQuantity = x.quantity;
                        });
                        self.model.SelectedItems.push(item);
                    }
                });
            } else {
                self.model.SelectedItems = [];
                self.model.purchaseOrders.items.forEach(function (item) {
                    if (!isDisable(item)) { // todo
                        item.isSelected = false;
                        item.isDisabled = true;
                        item.statusId = 10;
                        item.purchaseOrderStagedDto.forEach(function (x) {
                            x.approvedQuantity = null;
                        });
                    }
                });
            }
        }

        var SelectItem = function (item) {
            var items = self.model.SelectedItems;

            if (item.isSelected) {
                items.push(item);
                item.isDisabled = false;
                item.statusId = 5;
            } else {
                item.isDisabled = true;
                item.isReject = false;
                item.statusId = 10;
                var index = items.indexOf(item);
                if (index > -1) {
                    items.splice(index, 1);
                }
            }

            if (self.model.purchaseOrders.items.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
        }

        var OpenPopup = function (action, modal) {
            self.model.action = action;
            var popup = angular.element('#' + modal);
            popup.modal('show');
        }

        var ClosePopup = function (action, modal) {
            self.model.action = action;
            var popup = angular.element('#' + modal);
            popup.modal('hide');
        }

        var NavigateToBack = function () {
            $state.go('consignmentPOApproval');
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("detailsConsignmentPOApproval", {
            url: "/detailsConsignmentPOApproval",
            templateUrl: 'views/procurement/consignment/details-consignment-po-approval.html',
            controller: "DetailsConsignmentPOApproval.Controller",
            controllerAs: "vm",
            title: 'Consignment PO Approval'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("DetailsConsignmentPOApproval.Controller", detailsConsignmentPOApprovalController);
})();