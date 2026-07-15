(function () {
    "use strict";

    function detailsImplantReturnAcceptanceController($scope, $rootScope, $http, GenericService, $filter, $state, growl, CurrencyService, commonDetailService, $cookies, CONSTANTS, rejectReasonService, StatusService) {
        var self = this;
        var selectedItemId;


        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject');
        if(cookieObject == undefined){ 
        	$state.go('login'); 
        	return;
        	}
        $scope.unitId = cookieObject.unitId;
        $scope.organizationId = cookieObject.organizationId;
        $scope.userId = cookieObject.userId;
        $scope.storeId = cookieObject.storeId; 
        
        function initializeController() {
            $rootScope.loginpage = true;

            self.model = {
                isRejectQuantityValid: [],
                SelectedRequestItems: [],

            };

            //Methods
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })
            StatusService.GetStatus().then(function (status) {
                self.model.statuses = status;
            })

            self.model.GetDetailsPatientReturnAcceptanceImplant = GetDetailsPatientReturnAcceptanceImplant;
            self.model.SavePopup = SavePopup;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.SelectRequestItem = SelectRequestItem;
            self.model.RejectQty = RejectQty;
            self.model.SaveRejectPopup = SaveRejectPopup;
            self.model.ValidateBill = ValidateBill;
            self.model.SaveImplantReturnAcceptance = SaveImplantReturnAcceptance;
            self.model.NavigateToPatientReturnAcceptance = NavigateToPatientReturnAcceptance;
            self.model.ChangeItemStatus = ChangeItemStatus;
            self.model.CancelRejection = CancelRejection;

            GetDetailsPatientReturnAcceptanceImplant(commonDetailService.getDataId());
        }
        rejectReasonService.GetReason().then(function (reason) {
            self.model.rejectReasons = reason;
        });

        var GetDetailsPatientReturnAcceptanceImplant = function (itemId) {
            var URI = CONSTANTS.PROCUREMENT.IMPLANT_INDENT_RETURN_ACCEPTANCE.DETAILS_API + itemId + '/' + $scope.unitId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {

                    self.model.detailsImplantReturnAcceptanceImplantList = response.data;
                    self.model.detailsImplantReturnAcceptanceImplantList.items.forEach(function (item) {
                        item.isDisabled = true;
                        item.acceptQuantity = item.acceptQuantity || item.returnQuantity;
                        if (!!item.acceptQuantity && !!item.returnQuantity) {
                            ChangeItemStatus(item);
                        }
                        if (!!item.rejectQuantity) {
                            item.isRejected = true;
                            item.rejectReasonId = 1;
                        }
                    })
                
            $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error(err.data.message);
                });
        }

        var SelectAllRequests = function () {

            if (self.model.selectAllRows) {
                self.model.SelectedRequestItems = [];
                self.model.detailsImplantReturnAcceptanceImplantList.items.forEach(function (item) {
                    item.isSelected = true;
                    item.isDisabled = false;
                    self.model.SelectedRequestItems.push(item);

                });
            } else {
                self.model.SelectedRequestItems = [];
                self.model.detailsImplantReturnAcceptanceImplantList.items.forEach(function (item) {
                    item.isSelected = false;
                    item.isDisabled = true;
                });
            }
        }

        var isSelected = function (item) {
            return item.isSelected;
        }

        var SelectRequestItem = function (item) {

            var itemIds = self.model.SelectedRequestItems;

            if (item.isSelected) {
                itemIds.push(item);
                item.isDisabled = false;

            } else {
                var index = itemIds.indexOf(item);
                item.isDisabled = true;
                item.isRejected = false;
                item.acceptQuantity = 0;
                item.rejectQuantity = 0;
                if (index > -1) {
                    itemIds.splice(index, 1);
                }
            }

            if (self.model.detailsImplantReturnAcceptanceImplantList.items.every(isSelected)) {
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
                growl.error("Please add some items .");
            } else {
                self.model.SelectedRequestItems.forEach(function (item) {
                    item['isRejectQuantityValid'] = false;

                    if ((item.returnQuantity < (item.acceptQuantity))) {
                        item['isRejectQuantityValid'] = true;
                        isValid = false;
                        growl.error("Please enter quantity less than return quantity");
                    }

                    if (!item.acceptQuantity) {
                        item['isRejectQuantityValid'] = true;
                        isValid = false;
                        growl.error("Please enter quantity");
                    }

                    if (item.acceptQuantity == 0) {
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

            self.model.selectedItem = item;
            if (item.isRejected) {
                selectedItemId = item.id;
                self.model.qty = item.rejectQuantity;
                self.model.rejectReason = self.model.rejectReasons[0];
                self.model.rejectionFlag = false;

                self.model.action = 'Open';
                var popup = angular.element('#qtyaccepatReject');
                popup.modal('show');
            }
            else {
                item.isRejected = true;
                item.rejectReasonId = 1;
                var popup = angular.element('#cancelStatus');
                popup.modal('show');
            }
        }

        var SaveRejectPopup = function () {

            self.model.SelectedRequestItems.forEach(function (item) {

                if (item.id == selectedItemId) {
                    item.rejectQuantity = self.model.qty;
                    item.acceptQuantity = item.returnQuantity - self.model.qty;
                    item['rejectReasonId'] = self.model.rejectReason.id;
                    item['note'] = self.model.rejectNote;
                }
            });
            self.model.rejectReason = undefined;
            self.model.rejectNote = undefined;
        }



        var CancelRejection = function (itemList) {
            self.model.selectedItem.isRejected = false;
            self.model.qty = 0;
            self.model.selectedItem.rejectQuantity = 0;
            self.model.selectedItem.acceptQuantity = self.model.selectedItem.quantityIssued;
        }

        var ChangeItemStatus = function (item) {
            if (!!item.rejectQuantity) {
                item.isRejected = true;
                item.rejectReasonId = 1;
            }
            if (item.acceptQuantity > item.returnQuantity) {
                item.rejectQuantity = 0;
                item.isRejected = false;
            } else if (item.acceptQuantity == 0 || !item.acceptQuantity) {
                item.rejectQuantity = item.returnQuantity;
                item.isRejected = true;
                item.statusId = setStatus('Rejected');
            } else if (item.acceptQuantity < item.returnQuantity) {
                item.rejectQuantity = item.returnQuantity - item.acceptQuantity;
                item.isRejected = true;
                item.statusId = setStatus('Partially Approved'); 
            } else if (item.acceptQuantity == item.returnQuantity) {
                item.rejectQuantity = item.returnQuantity - item.acceptQuantity;
                item.isRejected = false;
                item.statusId = setStatus('Approved');
            }
        }



        var SaveImplantReturnAcceptance = function (itemList) {
            if (ValidateBill(itemList)) {
                var URI = CONSTANTS.PROCUREMENT.IMPLANT_INDENT_RETURN_ACCEPTANCE.SAVE_API;
                var data = {
                    "id": itemList.id,
                    "statusId": setStatus('Completed'),
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "storeId":$scope.storeId,
                    "items": []
                }
                angular.forEach(itemList.items, function (item, key) {
                    if (item.isSelected) {
                        var PatientData = {
                            "id": item.id,
                            "statusId":  setStatus('Completed'),
                            "acceptQuantity": item.acceptQuantity,
                            "issueRejectedQuantity": item.rejectQuantity,
                            "rejectReason": item.rejectReasonId,
                            "rejectedNote": item.note,
                            "issueDetailBatchMapper": item.issueDetailBatchMapper,
                        }
                        data.items.push(PatientData);
                    }
                });
                data.items.forEach(function (key) {
                    var itemStatusList = [];
                    itemStatusList.push(key.statusId);

                    if (itemStatusList.indexOf(8) !== -1) { // Closed status id
                        data.statusId = 8; // Closed stutus
                    } else {
                        data.statusId = undefined; //  no stutus
                    }
                });
                // console.log(data);
                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        ClearFields(itemList);
                        NavigateToPatientReturnAcceptance();
                        growl.success('Implant return acceptance Successfully saved');
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error('Error while saving implant return acceptance');
                    });
            }
        }

        var SavePopup = function () {
            var popup = angular.element('#confirmSave');
            popup.modal('show');
        }


        var NavigateToPatientReturnAcceptance = function () {
            document.getElementById('popupClose').click();
            setTimeout(function () {
                $state.go('returnAcceptance');
            }, 1000);
        }

        function setStatus(x) {
            var status = self.model.statuses.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("detailsImplantReturnAcceptance", {
            url: "/detailsImplantReturnAcceptance",
            templateUrl: 'views/procurement/consignment/details-implant-return-acceptance.html',
            controller: "DetailsImplantReturnAcceptance.Controller",
            controllerAs: "vm",
            title: 'Return Acceptance'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("DetailsImplantReturnAcceptance.Controller", detailsImplantReturnAcceptanceController)
})();
