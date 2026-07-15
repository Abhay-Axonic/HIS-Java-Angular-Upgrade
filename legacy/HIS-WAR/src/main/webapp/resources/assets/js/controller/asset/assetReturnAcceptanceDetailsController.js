(function () {
    "use strict";

    function assetReturnAcceptanceDetailsController($scope, $rootScope, $http,$state, $cookies, CONSTANTS, commonDetailService, GenericService, growl,rejectReasonService,StatusService) {
        var self = this;
        var areturnId;
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;
            self.model = {
                SelectedItems: [],
                selectItemList: [],
                selectAllRows: [],
                SelectedRequestId: []
            };
            
            //Methods 
            StatusService.GetStatus().then(function (status) {
                self.model.statuses = status;
            });
            
            rejectReasonService.GetReason().then(function (reason) {
                self.model.rejectReasons = reason;
            });
            
            self.model.SelectAllItems = SelectAllItems;
            self.model.SelectItem = SelectItem;
            self.model.isDisable = isDisable;
            self.model.isSelected = isSelected;
            self.model.ValidateRejection = ValidateRejection;
            self.model.RejectionPopup = RejectionPopup;
            self.model.ModalDismiss = ModalDismiss;
            self.model.SaveAcceptReject = SaveAcceptReject;
            self.model.setStatus = setStatus;
            self.model.saveAssetReturnAcceptance = saveAssetReturnAcceptance;
            self.model.validateQty = validateQty;
            self.model.NavigteToAssetIssueReturn = NavigteToAssetIssueReturn;

            fromAssetReturnAcceptance();
        }

        var setStatus = function (x) {
            var status = self.model.statuses.find(function (obj) {
                return obj.status == x;
            });
            return status.id;
        }

        function fromAssetReturnAcceptance() {
            var id = commonDetailService.getDataId();
            var URI = CONSTANTS.ASSET.ASSET_RETURN_ACCEPTANCE.DETAIL_API + id + '/'+ $scope.unitId;
            var data = {};
            GenericService.serviceAction("GET", URI, data).then(
                function (response) {
                    self.model.assetReturnAcceptanceDetailData = response.data;
                    self.model.assetReturnAcceptanceDetailData.item.forEach(function (item) {
                        item.isDisabled = true;
                        if (item.rejectQuantity)
                            item.isReject = true;
                            item.rejectedQuantity = item.rejectQuantity;
                    });
                });
        }

        var SelectAllItems = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestIds = [];
                self.model.assetReturnAcceptanceDetailData.item.forEach(function (areturn) {
                    // if (!!isDisable(areturn)) {
                        areturn.isSelected = true;
                        self.model.SelectedRequestIds.push(areturn.id);
                        areturn.isDisabled = false;
                        self.model.SelectedItems.push(areturn);
                    // }
                });
            } else {
                self.model.SelectedRequestIds = [];
                self.model.SelectedItems = [];
                self.model.assetReturnAcceptanceDetailData.item.forEach(function (areturn) {
                    areturn.isSelected = false;
                    areturn.isDisabled = true;
                });
            }
        }

        var SelectItem = function (areturn) {
            var mreturns = self.model.SelectedItems;

            if (areturn.isSelected) {
                mreturns.push(areturn);
                areturn.isDisabled = false;
            } else {
                areturn.isDisabled = true;
                areturn.isReject = false;
                var index = areturn.indexOf(areturn);
                if (index > -1) {
                    mreturns.splice(index, 1);
                }
            }

            if (self.model.assetReturnAcceptanceDetailData.item.every(isSelected)) {
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

        var isSelected = function (areturn) {
            return areturn.isSelected;
        }

        var ValidateRejection = function () {
            var isValid = true;
            if (!self.model.rejectReason) {
                isValid = false;
                growl.error('Please Select Reason to Reject');
            }

            return isValid;
        }

        var RejectionPopup = function (areturn) {
            if (areturn.isReject) {
                areturnId = areturn.id;
                self.model.action = 'Open';
                var popup = angular.element('#qtyaccepatReject');
                popup.modal('show');
                self.model.reasons = self.model.rejectReasons;
            } else {
                CancelRejection(areturn);
            }
        }

        var CancelRejection = function (areturn) {
            var prevRequest = self.model.requestItems.areturn.find(function (prevItem) {
                return prevItem.id == areturn.id;
            });

            if (prevRequest) {
                areturn.rejectedQuantity = prevRequest.rejectedQuantity;
                areturn.acceptedQuantity = prevRequest.acceptedQuantity;
                areturn.rejectNote = prevRequest.rejectNote;
                areturn.rejectReasonId = prevRequest.rejectReasonId;
                areturn.statusId = prevRequest.statusId;
            }
        }


        var ModalDismiss = function (popupName) {
            var popup = angular.element('#' + popupName);
            popup.modal('hide');
        }

        var SaveAcceptReject = function () {
            if (ValidateRejection()) {
                var rejectItemId = areturnId;
                self.model.SelectedItems.forEach(function (areturn) {
                    if (areturn.id == rejectItemId) {
                        if (self.model.rejectQty > areturn.returnQuantity) {
                            growl.error('Please Enter Recject less than or  equal to return quantity');
                            return false;
                        } else if (self.model.rejectQty == areturn.returnQuantity) {
                            areturn.rejectedQuantity = areturn.returnQuantity;
                            areturn.acceptedQuantity = areturn.returnQuantity - areturn.rejectedQuantity;
                            areturn.rejectReasonId = self.model.rejectReasons[0].id;
                            areturn.statusId = setStatus('Rejected'); // Rejected
                        } else if (self.model.rejectQty == 0 || !self.model.rejectQty) {
                            areturn.acceptedQuantity = areturn.returnQuantity;
                            areturn.rejectedQuantity = 0;
                            areturn.rejectReasonId = self.model.rejectReasons[0].id;
                            areturn.statusId = setStatus('Pending'); // Approved
                        } else {
                            areturn.rejectedQuantity = self.model.rejectQty;
                            areturn.rejectNote = self.model.rejectNote;
                            areturn.acceptedQuantity = areturn.returnQuantity - areturn.rejectedQuantity;
                            areturn.rejectReasonId = self.model.rejectReasons[0].id;
                            areturn.statusId = setStatus('Partially Approved'); // Partially Approved
                        }
                        ModalDismiss('qtyaccepatReject');
                    }
                });
            }
        }

        var NavigteToAssetIssueReturn = function () {
            $state.go('assetReturnAcceptance');
        }

        var validateQty = function () {
            var isValid = true;
            angular.forEach(self.model.assetReturnAcceptanceDetailData.item, function(asset){
                if(!asset.acceptedQuantity){
                    growl.error("Please enter accepted quantity");
                    isValid = false;
                }
            });
            return isValid;
        }

        var saveAssetReturnAcceptance = function() {
            if(validateQty()){
                var URI = CONSTANTS.ASSET.ASSET_RETURN_ACCEPTANCE.SAVE_API + self.model.assetReturnAcceptanceDetailData.id;
                angular.forEach(self.model.assetReturnAcceptanceDetailData.item, function(asset){
                    var data = {
                        "id": self.model.assetReturnAcceptanceDetailData.id,
                        "organizationId": $scope.organizationId,
                        "unitId": $scope.unitId,
                        "userId": $scope.userId,
                        "items" : []
                    }       
                    var obj = {
                        "id": asset.id,
                        "acceptedQuantity": asset.acceptedQuantity,
                        "rejectQuantity": asset.rejectedQuantity,
                        "rejectNote": asset.rejectNote,
                        "rejectReasonId": asset.rejectReasonId,
                        "statusId": 4,
                        "accessories": []
                    }
                    data.items.push(obj);
                    $rootScope.startLoader()
                    GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        NavigteToAssetIssueReturn();
                        growl.success(response.data.message);
                        
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoader();
                        growl.error('Error');
                    }); 
                });
            }
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("assetReturnAcceptanceDetails", {
            url: "/detailsAssetReturnAcceptance",
            templateUrl: 'views/master/UnitMaster/assetManagement/details-asset-return-acceptance.html',
            controller: "AssetReturnAcceptanceDetails.Controller",
            controllerAs: "vm",
            title: 'Asset Return Acceptance'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("AssetReturnAcceptanceDetails.Controller", assetReturnAcceptanceDetailsController);
})(); 