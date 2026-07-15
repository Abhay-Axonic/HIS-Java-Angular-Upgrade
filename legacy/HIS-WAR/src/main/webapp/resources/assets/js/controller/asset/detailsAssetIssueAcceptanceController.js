(function () {
    "use strict";

    function detailsAssetIssueAcceptanceController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, $filter, $state, growl, CurrencyService, commonDetailService, StatusService, rejectReasonService) {
        var self = this;
        var selectedItemId;

        var itemId;
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;

            self.model = {
                SelectedItems: [],
                selectAllRows: false
            };

            //Methods 
            StatusService.GetStatus().then(function (status) {
                self.model.statuses = status;
            });

            rejectReasonService.GetReason().then(function (reason) {
                self.model.rejectReasons = reason;
            });

            self.model.GetAssetIssueAcceptanceDetails = GetAssetIssueAcceptanceDetails;
            self.model.isSelected = isSelected;
            self.model.SelectAllItems = SelectAllItems;
            self.model.SelectItem = SelectItem;
            self.model.isDisable = isDisable;
            self.model.RejectionPopup = RejectionPopup;
            self.model.quantityChange = quantityChange;
            self.model.ApprovedQuantity = ApprovedQuantity;
            self.model.ModalDismiss = ModalDismiss;
            self.model.valueOnChange = valueOnChange;
            self.model.SaveAcceptReject = SaveAcceptReject;
            self.model.ValidateApproval = ValidateApproval;
            self.model.ValidateQuantity = ValidateQuantity;
            self.model.openAccessory = openAccessory;
            self.model.SaveApproval = SaveApproval;
            self.model.setStatus = setStatus;

            GetAssetIssueAcceptanceDetails();
        }

        var setStatus = function (x) {
            var status = self.model.statuses.find(function (obj) {
                return obj.status == x;
            });
            return status.id;
        }

        var GetAssetIssueAcceptanceDetails = function (itemId) {
            var id = commonDetailService.getDataId();
            var URI = CONSTANTS.ASSET.ASEET_ISSUE_ACCEPTANCE.DETAIL_API  + $scope.unitId + '/'+$scope.organizationId + '/' + id;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {

                    self.model.assetIssueAcceptanceDetailsList = response.data;
                    angular.forEach(self.model.assetIssueAcceptanceDetailsList.itemResponse,function(item){
                    	 item.isDisabled = true;
                         item.approvedQuantity = item.issueQuantity;
                         if (item.rejectQuantity)
                             item.isReject = true;
                    });
                  /*  self.model.assetIssueAcceptanceDetailsList.itemResponse.forEach(function (item) {
                        item.isDisabled = true;
                        if (item.rejectQuantity)
                            item.isReject = true;
                    });*/

                });
            $rootScope.stopLoader();
        }

        var isSelected = function (item) {
            return item.isSelected;
        }


        var SelectAllItems = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestIds = [];
                self.model.assetIssueAcceptanceDetailsList.itemResponse.forEach(function (items) {
                    if (!isDisable(items)) {
                        items.isSelected = true;
                        items.isDisabled = false;
                        self.model.SelectedRequestIds.push(items.id);
                        self.model.SelectedItems.push(items);
                    }
                });
            } else {
                self.model.SelectedRequestIds = [];
                self.model.SelectedItems = [];
                self.model.assetIssueAcceptanceDetailsList.itemResponse.forEach(function (items) {
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

            if (self.model.assetIssueAcceptanceDetailsList.itemResponse.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
        }

        var isDisable = function (items) {
            if (items.statusId == 10) {
                return true;
            }
            return false;
        }

        var RejectionPopup = function (item) {

            self.model.rejectQty = '';
            self.model.rejectReason = '';
            if (item.isReject) {
                itemId = item.id;
                $("#qtyaccepatReject").modal()
                self.model.reasons = self.model.rejectReasons;
            } else {
                CancelRejection(item);
            }
        }
        var quantityChange = function (item) {
            if (!item.approvedQuantity)
                item.approvedQuantity = 0;
        }

        var CancelRejection = function (item) {
            var prevRequest = self.model.assetIssueAcceptanceDetailsList.itemResponse.find(function (prevItem) {
                return prevItem.id == item.id;
            });

            if (prevRequest) {

                item.rejectQuantity = 0;;
                item.approvedQuantity = prevRequest.issueQuantity;
                item.rejectNote = prevRequest.rejectNote;
                item.rejectReasonId = prevRequest.rejectReasonId;
                item.statusId = prevRequest.statusId;
            }
        }

        var ApprovedQuantity = function (item) {
            if (item.approvedQuantity > item.issueQuantity) {
                item.rejectQuantity = 0;
                self.model.rejectQty = undefined;
                item.isReject = false;
            } else if (item.approvedQuantity == 0 || !item.approvedQuantity) {
                item.rejectQuantity = item.issueQuantity;
                self.model.rejectQty = item.rejectQuantity;
                item.isReject = true;
            } else if (item.approvedQuantity < item.issueQuantity) {
                item.rejectQuantity = item.issueQuantity - item.approvedQuantity;
                self.model.rejectQty = item.rejectQuantity;
                item.isReject = true;
            } else if (item.approvedQuantity == item.issueQuantity) {
                item.rejectQuantity = item.issueQuantity - item.approvedQuantity;
                self.model.rejectQty = item.rejectQuantity;
                item.isReject = false;
            }
        }
        var ModalDismiss = function (popupName) {
            var popup = angular.element('#' + popupName);
            popup.modal('hide');
        }
        
        var NavigteToBack = function () {
            $state.go('assetIssueAcceptance');
        }

        function valueOnChange(item, isSelectAll) {
            if (isSelectAll)
                self.model.assetIssueAcceptanceDetailsList.itemResponse.forEach(function (item) {
                    if (self.model.selectAllRows) {
                        item.approvedQuantity = item.issueQuantity;
                        item.rejectQuantity = 0;
                        item.isReject = false;
                    }

                    else{
                        item.approvedQuantity = 0;
                        item.rejectQuantity = 0;
                    }
                });
            else
                if (item.isSelected) {
                    item.approvedQuantity = item.issueQuantity;
                    item.rejectQuantity = 0;
                    item.isReject = false;
                }
                else{
                    item.approvedQuantity = 0;
                    item.rejectQuantity = 0;
                }
        }

        var SaveAcceptReject = function () {
            if (ValidateRejection()) {
                var rejectItemId = itemId;
                self.model.SelectedItems.forEach(function (item) {
                    if (item.id == rejectItemId) {
                        if (self.model.qty > item.issueQuantity) {
                            growl.error('Please Enter Reject Quantity Less Than Issue Quantity');
                            return false;
                        } else if (self.model.qty == item.issueQuantity) {
                            item.rejectQuantity = item.issueQuantity;
                            item.approvedQuantity = item.issueQuantity - item.rejectQuantity;
                            item.rejectReasonId = self.model.rejectReasons[0].id;
                        } else if (self.model.qty == 0 || !self.model.qty) {
                            item.approvedQuantity = item.issueQuantity;
                            item.rejectQuantity = 0;
                            item.rejectReasonId = self.model.rejectReasons[0].id;
                        } else {
                            item.rejectQuantity = self.model.qty;
                            item.rejectNote = self.model.rejectNote;
                            item.approvedQuantity = item.issueQuantity - item.rejectQuantity;
                            item.rejectReasonId = self.model.rejectReasons[0].id;
                        }
                        ModalDismiss('qtyaccepatReject');
                        self.model.qty = undefined;
                        self.model.rejectReason = undefined;
                        self.model.rejectNote = undefined;
                    }
                });
            }
        }



        var ValidateRejection = function () {
            var isValid = true;
            if (!self.model.rejectReason) {
                isValid = false;
                growl.error('Please Select Reason to Reject');
            }

            if(!self.model.qty){
                isValid = false;
                growl.error('Please enter reject quantity');
            }

            return isValid;
        }

        var ValidateApproval = function () {
            var isValid = true;

            if (!self.model.SelectedItems || self.model.SelectedItems.length == 0) {
                growl.error('Select Items to Update and Save');
                isValid = false;
            }
            for (let issueDetail of self.model.assetIssueAcceptanceDetailsList.itemResponse) {
                if (!ValidateQuantity(issueDetail)) {
                    isValid = false;
                }
            }
            return isValid;
        }

        var ValidateQuantity = function (item) {
            var isValid = true;

            if (item.approvedQuantity == 0 || item.approvedQuantity == undefined) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error("Please Enter Approved Quantity");
            }
            else if (item.approvedQuantity > item.issueQuantity) {
                growl.error("Please Enter Accepted Quantity less than or equal to issue quantity");
                item.isQuantityValid = true;
                isValid = false;
            }
            return isValid;
        }

        var openAccessory = function (item) {
            self.model.accessoryList = [];
            $("#viewDetailsAccessoriesModal").modal();
            self.model.itemForAccessoryDetail = item.asseccories;
            self.model.curentItem = item;
        }
        var SaveApproval = function () {
            if (ValidateApproval()) {
                var URI = CONSTANTS.ASSET.ASEET_ISSUE_ACCEPTANCE.SAVE_API + commonDetailService.getDataId();
                var data = {
                    "statusId": setStatus('Closed'),
                    "userId":$scope.userId,
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "storeId":$scope.storeId,
                    "itemRequest": []
                }

                $rootScope.startLoader();
                angular.forEach(self.model.SelectedItems, function (item) {
                    var newItem = {
                        "batchMapperId": parseInt(item.batchMapperId),
                        "acceptedQuantity": parseInt(item.approvedQuantity),
                        "rejectQuantity": item.rejectQuantity ? item.rejectQuantity : 0,
                        "rejectReasonId": self.model.rejectReasons.id ? self.model.rejectReasons.id : 1,
                        "rejectNote": self.model.rejectNote
                    }
                    data.itemRequest.push(newItem);

                    if (self.model.rejectQuantity > 0)
                        data.statusId = setStatus('Partially Completed')
                });

                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        growl.success(response.data.message);
                        NavigteToBack();
                    }, function (err) {
                        $rootScope.stopLoader();
                        growl.error('Somthing Went Wrong');
                    });
            }
        }
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("detailsAssetIssueAcceptance", {
            url: "/detailsAssetIssueAcceptance",
            templateUrl: 'views/master/UnitMaster/assetManagement/details-asset-issue-acceptance.html',
            controller: "DetailsAssetIssueAcceptance.Controller",
            controllerAs: "vm",
            title: 'Asset Issue Acceptance'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("DetailsAssetIssueAcceptance.Controller", detailsAssetIssueAcceptanceController)
})();
