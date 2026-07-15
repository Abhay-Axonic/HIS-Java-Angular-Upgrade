(function () {
    "use strict";

    function detailsAssetPendingIssueController($scope, $rootScope, $http, $state, commonDetailService, StatusService, $cookies, CONSTANTS, GenericService, $filter, growl) {
        var self = this;
        var selectItemList = [];
        var masterId;
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;
            self.model = {

                requestData: [],


            };
            StatusService.GetStatus().then(function (status) {
                self.model.statuses = status;
            });
            self.model.GetDetailsAssetPendingIssue = GetDetailsAssetPendingIssue;
            self.model.SaveAssetPendingIssue = SaveAssetPendingIssue;
            self.model.uomSelectEvent = uomSelectEvent;
            self.model.batchOnChange = batchOnChange;
            self.model.AddDuplicateRow = AddDuplicateRow;
            self.model.validatePending = validatePending;
            self.model.RemoveRowData = RemoveRowData;
            self.model.setStatus = setStatus;
            self.model.openViewModal = openViewModal;
            self.model.SaveAccessory = SaveAccessory;

            GetDetailsAssetPendingIssue();
        }
        var uomSelectEvent = function (batch) {
            batch.uom.forEach(function (uom) {
                if (uom.uomTypeId == batch.uomTypeId) batch.selectedUom = uom;
            });
        }

        var batchOnChange = function (batches, item) {
            var batch = item.batch.find(function (obj) {
                return obj.batchId == batches.batchId;
            });
            batches.expiry = batch.expiry;
            // batches.issueQuantity = batch.issueQuantity; 
        }

        var GetDetailsAssetPendingIssue = function () {

            var pendingId = commonDetailService.getDataId();
            var URI = CONSTANTS.ASSET.ASEET_ISSUE.DETAIL_API + pendingId;
            var data = {};
            GenericService.serviceAction("GET", URI, data).then(
                function (response) {
                    self.model.assetIssuePendingDetail = response.data;
                    self.model.assetIssuePendingDetail.item.forEach(function (item) {
                        item.batch.forEach(function (batch) {
                            batch.batchId = batch.batchId.toString();
                            self.model.batchOnChange(batch, item);
                        });
                    });
                });
        }

     


        var SaveAssetPendingIssue = function () {
            var asset = self.model;
            var pId = commonDetailService.getDataId();

            var URI = CONSTANTS.ASSET.ASEET_ISSUE.SAVE_API;
            if (validatePending()) {
                var data = {
                    "id": pId,
                    "statusId": setStatus('Completed'),
                    "carrierId": parseInt(self.model.id),
                    "remark": self.model.remark,
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "storeId":$scope.storeId,
                    "item": []
                };

                angular.forEach(asset.assetIssuePendingDetail.item, function (item, key) {
                    if (item.issueQuantity > 0) {
                        var assetItem = {
                            "id": item.id,
                            "issueQuantity": parseInt(item.issueQuantity),
                            "batches": []
                        }

                        angular.forEach(item.batch, function (batch) {
                            var batch = {
                                "batchId": parseInt(batch.batchId),
                                "issueQuantity": parseInt(batch.issueQuantity),
                                "asseccories": batch.accessoriesList
                            }
                            assetItem.batches.push(batch);
                        });

                        data.item.push(assetItem);
                    }
                });

                if (data.item.length > 0) {
                    $rootScope.startLoader();
                    GenericService.serviceAction("POST", URI, data).then(
                        function (response) {
                            // ClearRequests();
                            $rootScope.stopLoader();
                            NavigteToAssetIssueMaster();
                            growl.success(response.data.message);
                        },
                        function (err) {
                            $rootScope.stopLoader();
                            growl.error('Something Went Wrong');
                        });
                }
                else {
                    $rootScope.stopLoader();
                    NavigteToAssetIssueMaster();
                }
            }
        }

        function validatePending() {

            var isValid = true;
            var assetIssuePendingDetail = self.model.assetIssuePendingDetail.item;
            if (assetIssuePendingDetail.length < 1) {
                growl.error('Error', {
                    title: "Please validate all required fields"
                });
                isValid = false;
            }
            for (let pendingDetail of assetIssuePendingDetail) {
                if (pendingDetail.issueQuantity < 0) {
                    growl.error('Error', {
                        title: "Please Enter Valid Issue Quantity"
                    });
                    isValid = false;
                }
            }
            for (let pendingDetail of assetIssuePendingDetail) {
                if (pendingDetail.issueQuantity > pendingDetail.pendingQuantity) {
                    growl.error('Error', {
                        title: "Issue Quantity Should be less than Pending Quantity"
                    });
                    isValid = false;
                }
            }
            return isValid;
        }

        function setStatus(x) {
            var status = self.model.statuses.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }

        var AddDuplicateRow = function (item) {
            var data = angular.copy(item.batches[0]);
            data.expiry = '';
            data.uomType = '';
            data.uomUnit = '';
            data.batchId = '';
            data.displayQuantity = '';
            data.issueQuantity = '';
            data.batchNo = '';
            data.statusId = '';
            item.batches.push(data);
            // batch.splice(index + 1, 0, angular.copy(batch[index]));
        }

        var RemoveRowData = function (item) {
            var index = item.batches.indexOf(item);
            item.batches.splice(index, 1);
        }

        var NavigteToAssetIssueMaster = function () {
            $state.go('assetIssueMaster');
        }
        var openViewModal = function (batch, item) {
            self.model.accessoryList = [];
            self.model.accessoryList = batch.asseccories;
            $("#view_accessories_modal").modal();
            self.model.currentBatch = batch;
            self.model.currentItem = item;
        }
        var SaveAccessory = function () {
            self.model.assetIssuePendingDetail.item.forEach(function (obj) {
                if (obj.itemId == self.model.currentItem.itemId) {
                    obj.batch.forEach(function (batch) {
                        batch.accessoriesList = [];
                        if (batch.batchId == self.model.currentBatch.batchId) {
                            self.model.accessoryList.forEach(function (item) {
                                batch.accessoriesList.push({
                                    "batchId": parseInt(item.batchId),
                                    "issueQuantity": parseInt(item.quantity)
                                })
                            });
                        }
                    })
                }
            });
            self.model.accessoryList = [];

            var popup = angular.element('#view_accessories_modal');
            popup.modal('hide');
        }


        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("DetailsAssetPendingIssue", {
            url: '/DetailsAssetPendingIssue',
            templateUrl: 'views/master/UnitMaster/assetManagement/details-asset-pending-issue.html',
            controller: "DetailsAssetPendingIssue.Controller",
            controllerAs: "vm"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("DetailsAssetPendingIssue.Controller", detailsAssetPendingIssueController)
})();