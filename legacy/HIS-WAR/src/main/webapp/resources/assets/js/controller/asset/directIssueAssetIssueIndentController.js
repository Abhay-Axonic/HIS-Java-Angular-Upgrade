(function () {
    "use strict";

    function directIssueAssetIssueIndentController($scope, StatusService, cancelReasonService, $state, commonDetailService,screenDataService, $rootScope, $http, $cookies, CONSTANTS, GenericService, growl, reportService) {
        var self = this;
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); 
        if(cookieObject == undefined){ 
            $state.go('login'); return;
        }
        $scope.unitId = cookieObject.unitId;
        $scope.organizationId = cookieObject.organizationId;
        $scope.userId = cookieObject.userId;
        $scope.storeId = cookieObject.storeId; 
        
        function initializeController() {
            $rootScope.loginpage = true;
            self.model = {
                fromStores: [{
                    "store": "All Stores"
                }],
                toStores: [{
                    "store": "All Stores"
                }],
                assetCategories: [{
                    "category": "Select Asset Category"
                }],
                productCategories: [{
                    "category": "Select Product Category"
                }],
                carriers: [{
                    "name": "Select Carrier "
                }],
                SelectedItemsIds: [],
                fixedAssetList: [],
            };

            self.model.openAddItem = openAddItem;
            self.model.GetAssetCategoryList = GetAssetCategoryList; 
            self.model.GetProductCategoryList = GetProductCategoryList;
            self.model.AddItems = AddItems;
            self.model.SelectItem = SelectItem;
            self.model.SelectAllItems = SelectAllItems;
            self.model.ConfirmRemove = ConfirmRemove;
            self.model.removeRowData = removeRowData;
            self.model.AddDuplicateRow = AddDuplicateRow;
            self.model.savedirectIssueAssetIssue = savedirectIssueAssetIssue;
            self.model.setStatus = setStatus;
            self.model.generateStatus = generateStatus;
            self.model.NavigteToAssetIssue = NavigteToAssetIssue;
            self.model.RemoveRowData = RemoveRowData;
            self.model.validate = validate;
            self.model.validateStore = validateStore;
            self.model.ChangeToStore = ChangeToStore;
            self.model.ConfirmToStorePopup = ConfirmToStorePopup;

            PopulateDropdown();
            getDropdownsList();
        }

        var ChangeToStore = function (toStore, oldToStore) {
            if (!!oldToStore.id && !!self.model.fixedAssetList.length) {
                self.model.toStore = oldToStore;
                self.model.toStoreNew = JSON.parse(JSON.stringify(toStore));
                $("#tostoreChange").modal()
            }
        }

        var ConfirmToStorePopup = function () {
            self.model.toStore = self.model.toStoreNew;
            self.model.fixedAssetList = [];
            ModalDismiss('tostoreChange');
        }


        var ModalDismiss = function (popupName) {
            var popup = angular.element('#' + popupName);
            popup.modal('hide');
        }

        var PopulateDropdown = function () {
            var assetIssueIndent = self.model;
            assetIssueIndent.fromStore = assetIssueIndent.fromStores[0];
            assetIssueIndent.toStore = assetIssueIndent.toStores[0];

            var URI = CONSTANTS.ASSET.ASEET_ISSUE.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
                assetIssueIndent.fromStores = assetIssueIndent.fromStores.concat(response.data.fromStore);
                assetIssueIndent.toStores = assetIssueIndent.toStores.concat(response.data.toStore);
                self.model.fromStore = self.model.fromStores.find(function (store) {
                    return $scope.storeId == store.id;
                });
                $rootScope.stopLoader();
            },
            function (err) {
                $rootScope.stopLoader();
            });
        }

        var getDropdownsList = function () {
            var areturn = self.model;
            areturn.carrier = areturn.carriers[0];
            var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_RETURN.ADD_NEW_DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    areturn.carriers = areturn.carriers.concat(response.data.carrier);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var validateStore = function () {
            var isvalid = true;
            if(self.model.fromStore.id == undefined || self.model.toStore.id == undefined){
                growl.error("Please select store");
                isvalid = false;
            }
            return isvalid;
        }

        var openAddItem = function () {
            if(validateStore()){    
                self.model.selectAllRows = false;
                self.model.search = '';
                self.model.SelectedItemsIds = [];
                $scope.searchItemList = [];
                $("#addNewItemWasteStore").modal();
                GetAssetCategoryList();
            }
        }

        var GetAssetCategoryList = function () {
            var asset = self.model;
            var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + 2 + "/"+$scope.unitId + "/"+$scope.organizationId;
            asset.assetCategory = asset.assetCategories[0];
            asset.productCategory = asset.productCategories[0];
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    asset.assetCategories = asset.assetCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        
        var GetProductCategoryList = function (id) {
            var asset = self.model;
            var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + id + "/"+$scope.organizationId ;
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    asset.productCategories = asset.productCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var isSelected = function (item) {
            return item.isSelected;
        }

        $scope.searchNewItem = function () {
            if($scope.itemName){
                if($scope.itemName.length >= 3){        
                    var data = {
                        "assetTypeId": 2,
                        "storeId": self.model.fromStore.id,
                        "itemName": $scope.itemName,
                        "itemCode": $scope.itemCode
                    };
                    
                    var URI = BASE_URL + ROOT_URL + "/api/global/items/unit/"+$scope.unitId;
                    GenericService.serviceAction("POST", URI, data).then(function(response) {
                        $scope.searchItemList = response.data;
                        angular.forEach($scope.searchItemList,function(value, index){
                            value.isSelected = false;
                        })
                        console.log("$scope.searchItemList",$scope.searchItemList);
                    });
                }
            }
            else if($scope.itemCode){
                if($scope.itemCode.length >= 3){
                    var data = {
                        "assetTypeId": 2,
                        "storeId": self.model.fromStore.id,
                        "itemName": $scope.itemName,
                        "itemCode": $scope.itemCode
                    };
                    
                    var URI = BASE_URL + ROOT_URL + "/api/global/items/unit/"+$scope.unitId;
                    GenericService.serviceAction("POST", URI, data).then(function(response) {
                        $scope.searchItemList = response.data;
                        angular.forEach($scope.searchItemList,function(value, index){
                            value.isSelected = false;
                        })
                        console.log("$scope.searchItemList",$scope.searchItemList);
                    });
                }
            }
            else{
                $scope.searchItemList = [];
            }
        }

        var SelectAllItems = function () {
            if (self.model.selectAllRows) {
                if(self.model.SelectedItemsIds){
                    self.model.data = $scope.searchItemList.map(function (item) {
                        item.isSelected = true;
                        self.model.isItemChecked = true;
                        self.model.SelectedItemsIds.push(item.itemId);
                        return self.model.SelectedItemsIds;
                    });
                }
                else{
                    self.model.SelectedItemsIds = $scope.searchItemList.map(function (item) {
                        item.isSelected = true;
                        self.model.isItemChecked = true;

                        return item.itemId;
                    });
                }
            } else {
                self.model.SelectedItemsIds = [];
                $scope.searchItemList.forEach(function (item) {
                    item.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }
        }

        var SelectItem = function (item) {
            var itemIds = self.model.SelectedItemsIds;

            if (item.isSelected) {
                if(item.itemId){
                    itemIds.push(item.itemId);
                }
                else{
                    itemIds.push(item.itemid);
                }
            } else {
                if(item.itemId){
                    var index = itemIds.indexOf(item.itemId);
                    if (index > -1) {
                        itemIds.splice(index, 1);
                    }
                }
                else{
                    var index = itemIds.indexOf(item.itemid);
                    if (index > -1) {
                        itemIds.splice(index, 1);
                    }
                }
            }

            if ($scope.searchItemList.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            self.model.isItemChecked = false;
            $scope.searchItemList.forEach(function (item) {
                if (item.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }

        var AddItems = function () {
            var URI = CONSTANTS.ASSET.ASEET_ISSUE.DirectIssue_Details_Api;
            var data = {
                "itemIds": self.model.SelectedItemsIds,
                "storeId": self.model.fromStore.id,
                "toStoreId": self.model.toStore.id,
                "unitId": $scope.unitId,
                "organizationId": $scope.organizationId 
            };
            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    response.data.forEach(function (item) {
                        var isFound = self.model.fixedAssetList.find(function (x) {
                            return x.itemId == item.itemId;
                        });
                        if (!isFound) {
                            self.model.fixedAssetList.push(item);
                        }
                        if (item.batch) {
                            if(item.batch.length > 0) {
                                item['batchData1'] = [item.batch[0]];
                            }
                        }
                        else {
                            item['batchData1'] = [];
                        }
                        angular.forEach(item.batch, function(bdata){
                            if(bdata.batchNo == undefined){
                                bdata.batchNo = bdata.batchDetails;
                            }
                        });
                        angular.forEach(self.model.fixedAssetList, function(list,key){
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
                    $scope.searchItemList = [];
                    self.model.SelectedItemsIds = [];

                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error(err.data.message);
                });
        }

        var ConfirmRemove = function (item) {
            var popup = angular.element('#confirmRemove');
            popup.modal('show');
            self.model.itemForRemove = item;
        }

        function removeRowData() {
            var index = self.model.fixedAssetList.indexOf(self.model.itemRemove);
            self.model.fixedAssetList.splice(index, 1);
            var popup = angular.element('#confirmRemove');
            popup.modal('hide');
        }

        var AddDuplicateRow = function (item) {
            var data = (angular.copy(item.batchData1[0]));
            data.issueQuantity = '';
            data.availableStockQuantity = '';
            data.batchNo = '';
            item.batchData1.push(data);
        }

        var RemoveRowData = function (item, batch) {
            var index = item.batchData1.indexOf(batch);
            item.batchData1.splice(index, 1);
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
            var status = 'Completed';
            var fixedAssetList = self.model.fixedAssetList;
            for (let issueDetail of fixedAssetList)
            {
                if (issueDetail.issueQuantity <= issueDetail.pendingQuantity)
                {
                    status = 'Completed';
                }
                else if(issueDetail.issueQuantity == 0)
                {
                    status = 'Pending';
                }
            }

            return status;
        }

        var NavigteToAssetIssue = function () {
            commonDetailService.setDataId(2);
            $state.go('assetIssueIndent');
        }

        var validate = function () {
            var isValid = true;
            if(self.model.fixedAssetList.length == 0){
                growl.error("Please add item");
                isValid = false;
            }

            if(!self.model.carrier.id){
                growl.error('Please select carrier');
                isValid = false;
            }
            
            return isValid;
        }

        $scope.batchChange = function(batch, item, idex){
            angular.forEach(item.batchData1, function(data){
                if(data.batchNo == " "){
                    data.batchId = data.selectedBatch.batchId;
                    data.batchNo = batch.selectedBatch.batchNo;
                    data.issueQuantity = batch.selectedBatch.issueQuantity;
                    data.availableStockQuantity = batch.selectedBatch.availableStockQuantity;
                    data.batchDetails = batch.selectedBatch.batchDetails;
                }
            });
        }

        var savedirectIssueAssetIssue = function () {
            if(validate()){
                var URI = CONSTANTS.ASSET.ASEET_ISSUE.DirectIssue_Save_Api;
                var carriedId = self.model.carrier.id;
                var data = { 
                    "fromStoreId" : self.model.fromStore.id,
                    "toStoreId" : self.model.toStore.id,
                    "organizationId" : $scope.organizationId,
                    "unitId" : $scope.unitId,
                    "statusId" : setStatus(generateStatus()),
                    "carrierId" : carriedId,
                    "userId" : $scope.userId,
                    "item" : []
                }
                
                $scope.validateQuntity = false;
                angular.forEach(self.model.fixedAssetList, function(item){
                    var obj = {
                        "itemId" : item.itemId,
                        "issueQuantity" : item.issueQuantity,
                        "batches" : []
                    }
                    data.item.push(obj);   
                    angular.forEach(item.batchData1, function(bdata){ 
                        var batchData = {
                            "batchId" : bdata.selectedBatch.batchId,
                            "issueQuantity": bdata.issueQuantity,
                            "asseccories" : []
                        }
                        if(bdata.issueQuantity == undefined){
                            $scope.validateQuntity = true;    
                       }
                        obj.batches.push(batchData);
                        $rootScope.startLoader();        
                    });
                });
                if($scope.validateQuntity == true){
                    growl.error("Please enter quantity");  
                    $rootScope.stopLoader();
                    return false;  
                }
                GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    NavigteToAssetIssue();
                    growl.success("Asset Issue Saved Successfully..");
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error('Something Went Wrong');
                });
            }
        }
        
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("directIssueAssetIssueIndent", {
            url: '/directIssueAssetIssueIndent',
            templateUrl: 'views/master/UnitMaster/assetManagement/direct-issue-asset-issue-indent.html',
            controller: "directIssueAssetIssueIndent.Controller",
            title: 'Direct Issue',
            controllerAs: "vm"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("directIssueAssetIssueIndent.Controller", directIssueAssetIssueIndentController);
})(); 