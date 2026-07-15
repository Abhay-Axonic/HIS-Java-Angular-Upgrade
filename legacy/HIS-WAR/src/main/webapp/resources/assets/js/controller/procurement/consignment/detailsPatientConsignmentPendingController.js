(function () {
    "use strict";

    function detailsPatientConsignmentPendingController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, $window, $filter, $state, growl, CurrencyService, commonDetailService, StatusService, cancelReasonService,pharamcyContractService) {
        var self = this;
        var itemId;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;

            self.model = {
                /*   carrierList: [{
                      "carrierName": "Select carrier"
                  }], */
            };


            //Methods
            StatusService.GetStatus().then(function (status) {
                self.model.Status = status;
            })

            self.model.batchOnChange = batchOnChange;
            self.model.AddDuplicateRow = AddDuplicateRow;
            self.model.RemoveRowData = RemoveRowData;
            self.model.SavePendingIndent = SavePendingIndent;
            self.model.AddBatchToDetail = AddBatchToDetail;
            $scope.patientId = pharamcyContractService.billObj.patientId;
            GetDetailsPatientPendingIndent(commonDetailService.getDataId());


        }

        var GetDetailsPatientPendingIndent = function (itemId) {

        	
        	$scope.cookieStoreId = $scope.storeId;
            var URI = CONSTANTS.PROCUREMENT.PATIENT_CONSIGNMENT_ISSUE.PENDING_DETAILS_API +$scope.organizationId + "/" +$scope.unitId+"/"+$scope.storeId + "/" + itemId +"/"+$scope.cookieStoreId;
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.patientPendingIndentDetailList = response.data;
                    // self.model.carrierList = self.model.carrierList.concat(response.data.carrier);
                    angular.forEach(self.model.patientPendingIndentDetailList.items, function (value, key) {
                        value['batchList'] = [value.batch[0]];
                        value['batchList']['uomList'] = [];
                        value['tempIssueQuantity'] = value.issueQuantity;
                    });
                    $rootScope.stopLoader();
                });
        }

        var batchOnChange = function (selectedBatch, batch) {
            var conversionPurchase;
            var conversionStock;
            var conversionDespense;

            var ipUom = selectedBatch.uom.find(function (obj) {
                return obj.ipUom == true;
            });

            selectedBatch.uom.forEach(function (uom) {
                if (uom.uomTypeId == 1) {
                    conversionPurchase = uom.conversion;
                }
                else if (uom.uomTypeId == 2) {
                    conversionStock = uom.conversion
                }
                else {
                    conversionDespense = uom.conversion
                }
            });

            if (ipUom.uomTypeId == 1) {
                batch.selectedBatch.displayQuantity = Math.round((((selectedBatch.availableStockQuantity / conversionPurchase) / conversionStock) / conversionDespense));
            }
            else if (ipUom.uomTypeId == 2) {
                batch.selectedBatch.displayQuantity = Math.round((selectedBatch.availableStockQuantity / conversionStock) / conversionDespense);
            }
            else {
                batch.selectedBatch.displayQuantity = Math.round(selectedBatch.availableStockQuantity / conversionDespense);
            }

            batch.selectedBatch.uomType = ipUom.uomType;
            batch.selectedBatch.uomTypeId = ipUom.uomTypeId;
            batch.selectedBatch.uomUnit = ipUom.uomUnit;
            batch.selectedBatch.uomUnitId = ipUom.uomUnitId;
        }

        var AddDuplicateRow = function (index, item) {
            var batch = angular.copy(item.batchList[0]);
            batch.expiry = '';
            batch.uomType = '';
            batch.uomTypeId = '';
            batch.uomUnit = '';
            batch.uomUnitId = '';
            batch.issueQuantity='';
            batch.selectedBatch.expiry='';
            batch.displayQuantity = 0;
            item.batchList.push(batch);
        }

        var DuplicateRowItems = function (batches) {
            batches.batch = '',
                batches.batch.expiry = '';
            batches.batch.uomType = '';
            batches.batch.uomUnit = '';
            batches.batch.displayQuantity = '';
            batches.batch.issueQuantity = '';
        }

        var RemoveRowData = function (item) {
            var index = item.batchList.indexOf(item);
            item.batchList.splice(index, 1);
        }

        var SetStatus = function (x) {
            var status = self.model.Status.find(function (obj) {
                return obj.status == x;
            });
            return status.id;
        }

        var ChangeItemStatus = function () {
            if (item.indentQuantity > parseInt(item.issueQuantity)) {
                item.statusId = SetStatus('Partially Completed');
            } else if (item.indentQuantity == parseInt(item.issueQuantity)) {
                item.statusId = SetStatus('Completed');
            }
        }

        var SavePendingIndent = function () {
            var URI = CONSTANTS.PROCUREMENT.PATIENT_CONSIGNMENT_ISSUE.SAVE_API + self.model.patientPendingIndentDetailList.id;
            if (!!Validate()) {
                var data = {
                    "id": self.model.patientPendingIndentDetailList.id,
                    "implantIndentId": self.model.patientPendingIndentDetailList.implantIndentId,
                    "visitType": self.model.patientPendingIndentDetailList.visitTypeId,
                    "admissionId": self.model.patientPendingIndentDetailList.admissionId,
                    "doctorId": self.model.patientPendingIndentDetailList.doctorId,
                    "storeId": self.model.patientPendingIndentDetailList.storeId,
                    "remark": self.model.remark,
                    "statusId": SetStatus('Completed'),
                    "patientId":$scope.patientId,                 
                    "items": [],
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "storeId":$scope.storeId,
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    
                };

                angular.forEach(self.model.patientPendingIndentDetailList.items, function (item, key) 
                		{
                    var items = {
                        "id": item.id,
                        "statusId": item.statusId,
                        "implantIndentDetailId": item.implantIndentDetailId,
                        "batchList": [],
                        "markUpDown":item.markUpDown,
                	    "markUpDownPer":item.markUpDownPer,
                	    "itemId":item.itemId,
                	    "productCategoryId":item.productCategoryId,
                   
                    }

                    angular.forEach(item.batchList, function (batch) {
                        var batchList = {
                            "batchId": batch.selectedBatch.batchId,
                            "uomTypeId": batch.selectedBatch.uomTypeId,
                            "uomUnitId": batch.selectedBatch.uomUnitId,
                            "sellingPrice":batch.selectedBatch.sellingPrice,
                            "issueQuantity": parseInt(batch.issueQuantity)
                        }
                        items.batchList.push(batchList);
                    });

                    data.items.push(items);

                });
                data.items.forEach(function (key) {
                    var itemStatusList = [];
                    itemStatusList.push(key.statusId);

                    if (itemStatusList.indexOf(18) !== -1) { // partially Completed status id
                        data.statusId = 18; // partially Completed stutus
                    } else {
                        data.statusId = 4; //  Completed stutus
                    }
                });
                if (data.items.length > 0) {
                    $rootScope.startLoader();
                    GenericService.serviceAction("PATCH", URI, data).then(
                        function (response) {
                            // ClearRequests();
                            $rootScope.stopLoader();
                            NavigteToPatientConsumableIssue();
                            growl.success(response.data.message);
                        },
                        function (err) {
                            $rootScope.stopLoader();
                            growl.error('Something Went Wrong');
                        });
                } else {
                    $rootScope.stopLoader();
                    NavigteToPatientConsumableIssue();
                }
            }
        }

        var Validate = function () {
            var isValid = true;

            if (!self.model.patientPendingIndentDetailList.items || self.model.patientPendingIndentDetailList.items.length <= 0) {
                isValid = false;
                growl.error("Please add some items in the list");
            } else {
                self.model.patientPendingIndentDetailList.items.forEach(function (item) {

                    if (item.issueQuantity > item.indentQuantity) {

                        isValid = false;
                        growl.error("Issue Quantity Should be less than Indent Quantity");
                    }
                    var isBatchSelected = false;

                    for (let batch of item.batchList) {
                        if (!isBatchSelected && batch.selectedBatch) {
                            isBatchSelected = true;
                        }
                    }
                    if (!isBatchSelected) {
                        growl.error("Please Select Batch");
                        isValid = false;
                        batch['isBatchValid'] = true;
                    }
                    for (let batch of item.batchList) {
                        if (batch.issueQuantity == 0 || batch.issueQuantity==undefined) {
                            growl.error("Please Enter Valid Issue Quantity In batch");
                            isValid = false;
                        }
                    }

                    /*          item.batchList.forEach(function (batch) {
                                 batch['isBatchValid'] = false;
                                 batch['isIssueQuantityValid'] = false;
         
                                 if (!batch.batchId) {
                                     batch['isBatchValid'] = true;
                                     isValid = false;
                                     growl.error("Please select batch");
                                 }
                             }); */
                });
            }

            return isValid;
        }

        var NavigteToPatientConsumableIssue = function () {
            $state.go('patientConsignmentIssue');
        }

        var AddBatchToDetail = function (worklist, itemId, batchId) {
            var itemId = itemId;
            var batchId = batchId;

        }


        initializeController();
    }
    function config($stateProvider) {
        $stateProvider.state("detailsPatientConsignmentPendingIndent", {
            url: '/detailsPatientConsignmentPendingIndent',
            templateUrl: 'views/procurement/consignment/details-patient-consignment-pending.html',
            controller: "DetailsPatientConsignmentPending.Controller",
            controllerAs: "vm",
            title: 'Patient Consignment Issue'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("DetailsPatientConsignmentPending.Controller", detailsPatientConsignmentPendingController)
})();
