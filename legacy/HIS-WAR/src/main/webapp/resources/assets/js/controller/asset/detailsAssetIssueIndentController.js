(function () {
    "use strict";

    function detailsAssetIssueIndentController($scope, $rootScope, $http, $state, commonDetailService, StatusService, $cookies, CONSTANTS, GenericService, $filter, growl,stopTransactionService) {
        var self = this;
        // Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;
            self.model = {

            }
            self.model.detailsAssetIssue = detailsAssetIssue;
            self.model.AddDuplicateRow = AddDuplicateRow;
            self.model.RemoveRowData = RemoveRowData;
            self.model.saveAssetIssue = saveAssetIssue;
            self.model.setStatus = setStatus;
            self.model.generateStatus = generateStatus;
            self.model.validate = validate;

            detailsAssetIssue();
        }

        function detailsAssetIssue() {
            var issueId = commonDetailService.getDataId();
            $rootScope.startLoader();
            var URI = CONSTANTS.ASSET.ASEET_ISSUE.DETAIL_API + $scope.unitId + '/'+$scope.organizationId + '/' + issueId;
            var data = {};
            GenericService.serviceAction("GET", URI, data).then(
            function (response) {
                self.model.assetIssueIndentDetail = response.data;
                angular.forEach(self.model.assetIssueIndentDetail.item, function(data){
                    if (data.batch) {
                        if(data.batch.length > 0) {
                            data['batchData'] = [data.batch[0]];
                        }
                    }
                    else {
                        data['batchData'] = [];
                    }
                    angular.forEach(data.batch, function(bdata){
                        if(bdata.batchNo == undefined){
                            bdata.batchNo = bdata.batchDetails;
                        }
                    });
                    angular.forEach(self.model.assetIssueIndentDetail.item, function(list,key){
                        var batchNo = [];
                        var uniqueBatches = [];
                        if(list.batch){ //added if condition on 15-04-2020 (only condition not body code )
                            list.batch.forEach(function (batch) {
                                var index = batchNo.indexOf(batch.batchNo);                    			 
                                if(index === -1)
                                {
                                    batchNo.push(batch.batchNo);
                                    uniqueBatches.push(batch);
                                }else{
                                        /*uniqueBatches[index].availableStockQuantity += batch.availableStockQuantity;
                                        batch.availableStockQuantity = uniqueBatches[index].availableStockQuantity ;*/
                                    if(uniqueBatches.length >=index)
                                    {
                                    uniqueBatches[index].availableStockQuantity += batch.availableStockQuantity;
                                                batch.availableStockQuantity = uniqueBatches[index].availableStockQuantity ;  
                                    }
                                        
                                }
                            });
                            list.batch=uniqueBatches; //added if condition on 15-04-2020 (only condition not body code)
                        }
                    });
                });
                $rootScope.stopLoader();
            });
        }

        var AddDuplicateRow = function (item) {
            var data = (angular.copy(item.batchData[0]));
            data.issueQuantity = '';
            data.availableStockQuantity = '';
            data.batchNo = '';
            item.batchData.push(data);
        }

        var RemoveRowData = function (item, batch) {
            var index = item.batchData.indexOf(batch);
            item.batchData.splice(index, 1);
        }

        function setStatus(x) {
            console.log("Checking Status : ",x);
            var status = self.model.statuses.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }

        StatusService.GetStatus().then(function (status) {
            self.model.statuses = status;
        });

        function generateStatus() {
            // var status = 'Completed';
            // var assetIssueIndentDetail = self.model.assetIssueIndentDetail.item;
            // for (let issueDetail of assetIssueIndentDetail)
            // {
            //     if (issueDetail.issueQuantity <= issueDetail.pendingQuantity)
            //     {
            //         status = 'Completed';
            //     }
            //     else if(issueDetail.issueQuantity == 0)
            //     {
            //         status = 'Pending';
            //     }
            // }

            // return status;
            var status = 'Completed';
            var assetIssueIndentDetail = self.model.assetIssueIndentDetail.item;
            $scope.count = self.model.assetIssueIndentDetail.item.length;
            $scope.statusCount = 0;
            for (let issueDetail of assetIssueIndentDetail)
            {
                if (issueDetail.issueQuantity < issueDetail.pendingQuantity)
                {
                    status = 'Partially Completed';
                }
                else if(issueDetail.issueQuantity == 0)
                {
                    status = 'Pending';
                }
                else{
                    status = 'Completed';
                    $scope.statusCount = $scope.statusCount + 1; 
                }
            }

            if($scope.count == $scope.statusCount){
                status = "Completed";
            }
            else{
                status = "Partially Completed";
            }

            return status;
        }

        var validate = function () {
            var isValid = true;
            angular.forEach(self.model.assetIssueIndentDetail.item, function(data){
                angular.forEach(data.batchData, function(bdata){
                    if(bdata.selectedBatch == undefined){
                        growl.error("Please select batch");
                        isValid = false;
                    }
                    if(bdata.issueQuantity > data.indentQuantity){
                        growl.error("Issue quantity is greater than pending indent quantity");
                        isValid = false;
                    }
                });
                if(data.issueQuantity == 0){
                    growl.error("Enter issue quantity");
                    isValid = false;
                }
            });
            return isValid;
        }

        var saveAssetIssue = function () {
            if(validate()){
                var URI = CONSTANTS.ASSET.ASEET_ISSUE.SAVE_API;
                var assetId = commonDetailService.getDataId();
                var pickerId=self.model.id;
                var data1 = {
                    "id": assetId,
                    "organizationId": $scope.organizationId,
                    "unitId": $scope.unitId,
                    "statusId": setStatus(generateStatus()),
                    "carrierId": pickerId,
                    "userId": $scope.userId,
                    "remark":self.model.remark,
                    "item": []
                }
                angular.forEach(self.model.assetIssueIndentDetail.item, function(data){
                    var itemData = {
                        "id": data.id,
                        "issueQuantity": data.issueQuantity,
                        "batches" : []
                    }
                    angular.forEach(data.batchData, function(batchlist){    
                            var bdata = {
                                "batchId" : batchlist.selectedBatch.batchId,
                                "issueQuantity" : batchlist.issueQuantity
                            }
                            itemData.batches.push(bdata);
                    });
                    data1.item.push(itemData);
                });
                GenericService.serviceAction("POST", URI, data1).then(
                function (response) {
                    $rootScope.stopLoader();
                    NavigteToAssetIssueIndent();
                    growl.success(response.data.message);
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error('Something Went Wrong');
                });
            }
        }

        var NavigteToAssetIssueIndent = function () {
            $state.go('assetIssueIndent');
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("detailsAssetIssueIndent", {
            url: '/detailsAssetIssueIndent',
            templateUrl: 'views/master/UnitMaster/assetManagement/details-asset-issue-indent.html',
            controller: "detailsAssetIssueIndent.Controller",
            controllerAs: 'vm',
            title: 'Asset Issue'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("detailsAssetIssueIndent.Controller", detailsAssetIssueIndentController)
})();  