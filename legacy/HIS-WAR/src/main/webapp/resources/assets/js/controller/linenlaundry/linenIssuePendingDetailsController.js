(function () {
    "use strict";
  
    function linenIssuePendingDetailsController($scope, $rootScope, $http, $state, commonDetailService, StatusService, $cookies, CONSTANTS, GenericService, $filter, growl) {
      var self = this;
      
      //Default Constructor
      var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
      function initializeController() {
        $rootScope.loginpage = true;
        self.model = {
            requestData: [],


            };
            self.model.fromMaterialPending = fromMaterialPending;
            self.model.ApprovePending = ApprovePending;
            self.model.batchOnChange = batchOnChange;
            self.model.AddDuplicateRow = AddDuplicateRow;
            self.model.validatePending = validatePending;
            self.model.RemoveRowData = RemoveRowData;
            self.model.setStatus = setStatus;

            fromMaterialPending();
        }


        var batchOnChange = function (batch, item) {
            debugger
            var batch = item.batch.find(function (obj) {
                return obj.batchId == batch.batchId;
            });
            batch.expiry = batch.expiry;
         
        }

        function fromMaterialPending() {

            var pendingId = commonDetailService.getDataId();
            
            var URI = CONSTANTS.LINEN_LAUNDRY.LINEN_ISSUE.LINEN_PENDING_ISSUE.DETAIL_API + $scope.unitId + '/'+$scope.organizationId + '/' + pendingId;
            var data = {};
            GenericService.serviceAction("GET", URI, data).then(
                function (response) {
                    self.model.materialIssuePendingDetail = response.data;
                    self.model.materialIssuePendingDetail.items.forEach(function (item) {
                        item.batch.forEach(function (batch) {
                            batch.batchId = batch.batchId.toString();
                            self.model.batchOnChange(batch, item);
                        });
                    });
                });
        }
   
        var ApprovePending = function () {
            var store = self.model;
            var pId = commonDetailService.getDataId();

            var URI = CONSTANTS.LINEN_LAUNDRY.LINEN_ISSUE.LINEN_PENDING_ISSUE.SAVE_API;

            if (validatePending()) {
                var data = {
                    "id": pId,
                    "statusId": setStatus('Completed'),
                    "carrierId": parseInt(self.model.id),
                    "remark": self.model.remark,
                    "items": [],
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "storeId":$scope.storeId
                };

                angular.forEach(store.materialIssuePendingDetail.items, function (item, key) {
                    if (item.issueQuantity > 0) {
                        var storeItem = {
                            "id": item.id,
                            "issueQuantity": item.issueQuantity,
                            "batches": []
                        }

                        angular.forEach(item.batch, function (batch) {
                            var batch = {
                                "id":  batch.id ? batch.id : undefined,
                                "batchId": parseInt(batch.batchId) ,
                                "issueQuantity": parseInt(batch.issueQuantity) 
                            }
                            storeItem.batches.push(batch);
                        });

                        data.items.push(storeItem);
                    }
                });

                if (data.items.length > 0) {
                    $rootScope.startLoader();
                    GenericService.serviceAction("PATCH", URI, data).then(
                        function (response) {
                            // ClearRequests();
                            $rootScope.stopLoader();
                            NavigteToMaterialPending();
                            growl.success(response.data.message);
                        },
                        function (err) {
                            $rootScope.stopLoader();
                            growl.error('Something Went Wrong');
                        });
                }
                else {
                    $rootScope.stopLoader();
                    NavigteToMaterialPending();
                }
            }
        }
        function validatePending() {

            var isValid = true;
            var materialPendingDetail = self.model.materialIssuePendingDetail.items;
            // !self.model.fromStore.id || !self.model.toStore.id || !self.model.priority.id || 
            if (materialPendingDetail.length < 1) {
                growl.error('Error', {
                    title: "Please validate all required fields"
                });
                isValid = false;
            }
            for (let pendingDetail of materialPendingDetail) {
                if (pendingDetail.issueQuantity < 0) {
                    growl.error('Error', {
                        title: "Please Enter Valid Issue Quantity"
                    });
                    isValid = false;
                }
            }
            for (let pendingDetail of materialPendingDetail) {
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

        StatusService.GetStatus().then(function (status) {
            self.model.statuses = status;
        });

        var AddDuplicateRow = function (item) {
            var data = angular.copy(item.batch[0]);
            data.expiry = '';
            data.batchId = '';
            data.displayQuantity = '';
            data.issueQuantity = '';
            data.batchNo = '';
            data.statusId = '';
            item.batch.push(data);
            // batch.splice(index + 1, 0, angular.copy(batch[index]));
        }


        var RemoveRowData = function (item) {
            var index = item.batch.indexOf(item);
            item.batch.splice(index, 1);
        }

        var NavigteToMaterialPending = function () {
            $state.go('linenIssue');
        }

        initializeController();
    }
  
    function config($stateProvider) {
      $stateProvider.state("linenIssuePendingDetails", {
        url: "/linenIssuePendingDetails",
        templateUrl: 'views/Linen/linen-issue-pending-details.html',
        controller: "LinenIssuePendingDetails.Controller",
        controllerAs: "vm"
      });
    }
  
    angular
      .module("myApp")
      .config(config)
      .controller("LinenIssuePendingDetails.Controller", linenIssuePendingDetailsController);
  })();
 