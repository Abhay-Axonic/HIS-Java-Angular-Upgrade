(function () {
    "use strict";

    function detailsCrashCartAdministrationApprovalController($scope, $rootScope, $http, GenericService, $filter, $state, growl, CurrencyService, commonDetailService, $cookies, CONSTANTS, rejectReasonService) {
        var self = this;
        var selectedItemId;


        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
        function initializeController() {
            $rootScope.loginpage = true;

            self.model = {
                isRejectQuantityValid: [],
                SelectedRequestItems: [],

            };

            //Methods
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            });
            rejectReasonService.GetReason().then(function (reason) {
                self.model.rejectReasons = reason;
            });

            self.model.GetDetailsAdministrationApproval = GetDetailsAdministrationApproval;
            self.model.SavePopup = SavePopup;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.SelectRequestItem = SelectRequestItem;
            self.model.RejectQty = RejectQty;
            self.model.SaveRejectPopup = SaveRejectPopup;
            self.model.ValidateBill = ValidateBill;
            self.model.SaveAdministrationApproval = SaveAdministrationApproval;
            self.model.NevigateToAdministrationApproval = NevigateToAdministrationApproval;

            GetDetailsAdministrationApproval(commonDetailService.getDataId());
        }


        var GetDetailsAdministrationApproval = function (itemId) {

            var URI = CONSTANTS.PHARMACY.NURSING.ADMINISTRATION_APPROVAL.DETAIL_API +  $scope.unitId + '/' + itemId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {

                    self.model.detailsAdministrationApprovalList = response.data;
                });
            $rootScope.stopLoader();
        }

        var SelectAllRequests = function () {

            if (self.model.selectAllRows) {
                self.model.SelectedRequestItems = [];
                self.model.detailsAdministrationApprovalList.items.forEach(function (item) {
                    item.isSelected = true;
                    self.model.SelectedRequestItems.push(item);

                });
            } else {
                self.model.SelectedRequestItems = [];
                self.model.detailsAdministrationApprovalList.items.forEach(function (item) {
                    item.isSelected = false;
                });
            }
            // console.log(self.model.SelectedRequestItems);
        }

        var isSelected = function (item) {
            return item.isSelected;
        }

        var SelectRequestItem = function (item) {

            var itemIds = self.model.SelectedRequestItems;

            if (item.isSelected) {
                itemIds.push(item);
            } else {
                var index = itemIds.indexOf(item);
                if (index > -1) {
                    itemIds.splice(index, 1);
                }
            }

            if (self.model.detailsAdministrationApprovalList.items.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            // console.log(itemIds);
        }

        var ValidateBill = function (itemList) {
            var isValid = true;

            if (self.model.rejectionFlag == false) {
                var isValid = false;
                growl.error("Please save rejection reason");
            }

            if (!self.model.SelectedRequestItems || self.model.SelectedRequestItems.length <= 0) {
                isValid = false;
                growl.error("Please add some items in the bill");
            } else {
                self.model.SelectedRequestItems.forEach(function (item) {
                    item['isRejectQuantityValid'] = false;

                    if ((item.requiredQuantity < (item.approvedQuantity)) || !item.approvedQuantity) {
                        item['isRejectQuantityValid'] = true;
                        isValid = false;
                        growl.error("Please enter valid quantity");
                    }

                    if (item.approvedQuantity == 0) {
                        item['isRejectQuantityValid'] = true;
                        isValid = false;
                        growl.error("Please enter quantity greater than zero");
                    }
                });
            }
            return isValid;
        }

        var ClearFields = function (itemList) {

            itemList.Id = "1" + (new Date()).getTime();
            itemList.rejectedNo = null;
            itemList.rejectedDate = null;
            itemList.wardName = null;
            itemList.uhid = null;
            itemList.patientDetail = null;
            itemList.doctorDetail = null;
            itemList.store = null;
            itemList.items = [];
        }

        var RejectQty = function (item) {

            if (item.isRejected) {
                selectedItemId = item.id;
                self.model.qty = item.rejectQuantity;
                self.model.rejectReason = self.model.rejectReasons[0];
                self.model.rejectionFlag = false;

                self.model.action = 'Open';
                var popup = angular.element('#qtyaccepatReject');
                popup.modal('show');
            }
        }

        var SaveRejectPopup = function () {

            self.model.SelectedRequestItems.forEach(function (item) {

                if (item.id == selectedItemId) {
                    item.rejectQuantity = self.model.qty;
                    item.approvedQuantity = item.requiredQuantity - self.model.qty;
                    item['rejectReasonId'] = self.model.rejectReason.id;
                    item['note'] = self.model.rejectNote;
                }
            });
        }

        var SaveAdministrationApproval = function (itemList) {
            if (ValidateBill(itemList)) {

                var URI = CONSTANTS.PHARMACY.NURSING.ADMINISTRATION_APPROVAL.SAVE_API + itemList.id;

                var data = {
                    "statusId": 4,
                    "items": [],
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "storeId":$scope.storeId

                }
                angular.forEach(itemList.items, function (item, key) {
                    if (item.isSelected) {
                        if (item.isRejected) {
                            var newitem = {
                                "id": item.id,
                                "approvedQuantity": Number(item.approvedQuantity),
                                "rejectQuantity": item.rejectQuantity,
                                "reasonId": self.model.rejectReason.id,
                                "note": item.note,
                                // "batchMapperId": item.batchMapperId
                            }

                            if (item.approvedQuantity && item.rejectQuantity) {
                                newitem.statusId = 5;
                            }
                            else if (item.approvedQuantity && !item.rejectQuantity) {
                                newitem.statusId = 5;
                            }
                            else {
                                newitem.statusId = 7;
                            }
                            data.items.push(newitem);
                        } else {
                            var newitem = {
                                "id": item.id,
                                "approvedQuantity": Number(item.approvedQuantity),
                                "rejectQuantity": item.rejectQuantity,
                                "reasonId": self.model.rejectReasons[0].id,
                                "note": item.note,
                                // "batchMapperId": item.batchMapperId
                            }

                            if (item.approvedQuantity && item.rejectQuantity) {
                                newitem.statusId = 5;
                            }
                            else if (item.approvedQuantity && !item.rejectQuantity) {
                                newitem.statusId = 5;
                            }
                            else {
                                newitem.statusId = 7;
                            }
                            data.items.push(newitem);
                        }
                    }
                });
                // console.log(data);
                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        ClearFields(itemList);
                        NevigateToAdministrationApproval();
                        growl.success('Administration approval Successfully saved');
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error('Error while saving administration approval');
                    });
            }
        }

        var SavePopup = function () {
            var popup = angular.element('#confirmSave');
            popup.modal('show');
        }

        var NevigateToAdministrationApproval = function () {
            document.getElementById('popupClose').click();
            setTimeout(function () {
                $state.go('administrationApproval');
            }, 1000);

        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("detailsAdministrationApproval", {
            url: "/detailsAdministrationApproval",
            templateUrl: 'views/PharmacyInventory/InPatient/details-crash-cart-medication-administration-approval.html',
            controller: "DetailsCrashCartAdministrationApproval.Controller",
            controllerAs: "vm"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("DetailsCrashCartAdministrationApproval.Controller", detailsCrashCartAdministrationApprovalController)
})();