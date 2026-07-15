(function () {
    "use strict";

    function stockAdjustmentApprovalDetailsController($scope, $rootScope, commonDetailService, CurrencyService, StatusService, $state, $http, $cookies, CONSTANTS, GenericService, growl) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;
            self.model = {
                SelectedItems: [],
                selectItemList: []             
            };
            StatusService.GetStatus().then(function (status) {
                self.model.statuses = status;
            });
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
              });
              self.model.selectAllRows = false;
            self.model.SaveApproval = SaveApproval;
            self.model.NavigateToBack = NavigateToBack;
            self.model.setStatus = setStatus;
            self.model.SelectAllItems = SelectAllItems;
            self.model.SelectItem = SelectItem;
            self.model.ValidateQuantity  = ValidateQuantity; 
            self.model.isSelected = isSelected; 
            self.model.calculateVariance = calculateVariance;
            self.model.SelectedItems = [];
            fromUploadStock();
        }

        var calculateVariance = function(item){
            item.varianceQty = item.quantity - item.uploadedQuantity;
            item.varianceCOP = item.quantity * item.cop - item.uploadedQuantity * item.cop;
            item.varianceMRP = item.quantity * item.mrp - item.uploadedQuantity * item.mrp;
        }

        function fromUploadStock() {          
            var id = commonDetailService.getDataId();
            var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.UPLOAD_STOCK_APPROVAL.DETAIL_API + id;
            var data = {};
            $rootScope.startLoader(); 
            GenericService.serviceAction("GET", URI, data).then(
                function (response) {
                    self.model.uploadStockApprovalDetail = response.data;
                    self.model.GRNRemark=response.data.remark;
                    self.model.uploadStockApprovalDetail.items.forEach(function (item) {
                        item.cop = item.cop.toFixed(2);
                        item.mrp = item.mrp.toFixed(2);
                        item.cop = commonDetailService.calculateRate(item.uomList, item.uomTypeId, item.cop);
                        item.mrp = commonDetailService.calculateRate(item.uomList, item.uomTypeId, item.mrp);
                        item.isDisabled = true;
                        item.varianceCOP = item.varianceCop.toFixed(2);
                        item.varianceMRP = item.varianceMrp;
                        item.uomList.find(function(u){
                            if(u.uomType == item.uom){
                               item.uom = u.uomDetails;
                            }
                        })
                        
                         if(item.assetTypeId == 2){
                        	 
                        	 item.receivedQtyCon = (item.uploadedQuantity);
                             item.receivedQtyCalculation = (item.uploadedQuantity);
                        	 
                         }else{
                        	 
			                        
			                        
			                        if(item.isThreeUOMApplicable == false){
			                            if(item.uomTypeId == 1){
			                                item.receivedQtyCon = (item.uploadedQuantity +"*"+ item.uomList[0].conversion);
			                                item.receivedQtyCalculation = (item.uploadedQuantity * item.uomList[0].conversion);
			                                item.uomUnitData = item.uomList[2].uomUnit;
			                            }
			                            else if(item.uomTypeId == 2){
			                                
			                            }
			                            else{
			                                item.receivedQtyCon = (item.uploadedQuantity);
			                                item.receivedQtyCalculation = (item.uploadedQuantity);
			                                item.uomUnitData = item.uomList[2].uomUnit;
			                            }
			                        }
			                        else{
			                            if(item.uomTypeId == 1){
			                                item.receivedQtyCon = (item.uploadedQuantity +"*"+ item.uomList[0].conversion +"*"+ item.uomList[1].conversion);
			                                item.receivedQtyCalculation = (item.uploadedQuantity * item.uomList[0].conversion * item.uomList[1].conversion);
			                                item.uomUnitData = item.uomList[2].uomUnit;
			                            }
			                            else if(item.uomTypeId == 2){
			                                item.receivedQtyCon = (item.uploadedQuantity +"*"+ item.uomList[1].conversion);
			                                item.receivedQtyCalculation = (item.uploadedQuantity * item.uomList[1].conversion);
			                                item.uomUnitData = item.uomList[2].uomUnit;
			                            }
			                            else{
			                                item.receivedQtyCon = (item.uploadedQuantity);
			                                item.receivedQtyCalculation = (item.uploadedQuantity);
			                                item.uomUnitData = item.uomList[2].uomUnit;
			                            }
			                        }
                         }
                    })
                    $rootScope.stopLoader(); 
                });
        }

        function setStatus(x) {

            var status = self.model.statuses.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }

        $scope.saveBtnDisabled=false;
        var SaveApproval = function () {
            $scope.saveBtnDisabled=true;
            // if (ValidateApproval()) {
                var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.UPLOAD_STOCK_APPROVAL.NEW_APPROVE_API + commonDetailService.getDataId();
                var data = {
                    "statusId": setStatus('Approved'),
                    "approvalStatusId": setStatus('Approved'),
                    "items": [],
                    "userId": $scope.userId,
                    "unitId": $scope.unitId,
                    "organizationId": $scope.organizationId
                }

                $rootScope.startLoader();
                angular.forEach(self.model.uploadStockApprovalDetail.items, function (item) {
                    var newItem = {
                        "id": item.id,
                        "uploadedQuantity": item.uploadedQuantity,
                        "statusId": 5,
                        "varianceQty" : item.varianceQty,
                        "varianceCop" : item.varianceCOP,
                        "varianceMrp" : item.varianceMRP
                    }
                    data.items.push(newItem);
                });

                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        growl.success("Stock adjustment approval save successfully..!");
                        NavigateToBack();
                    }, function (err) {
                        $rootScope.stopLoader();
                        growl.error('Somthing Went Wrong');
                    });
            // }
        }
        var ValidateApproval = function () {
            var isValid = true;

            if (!self.model.SelectedItems || self.model.SelectedItems.length == 0) {
                growl.error('Select Items to Update and Save');
                isValid = false;
            }
            else if (self.model.SelectedItems) {
                angular.forEach(self.model.SelectedItems, function (item) {
                    if (!ValidateQuantity(item)) {
                        isValid = false;
                    }
                });
        
            } 

            return isValid;
        }


        var SelectAllItems = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestIds = [];
                self.model.uploadStockApprovalDetail.items.forEach(function (item) {
                    if (!!isDisable(item)) {
                        item.isSelected = true;
                        self.model.SelectedRequestIds.push(item.id);
                        item.isDisabled = false;
                        self.model.SelectedItems.push(item);
                    }
                });
            } else {
                self.model.SelectedRequestIds = [];
                self.model.SelectedItems = [];
                self.model.uploadStockApprovalDetail.items.forEach(function (item) {
                    item.isSelected = false;
                    item.isDisabled = true;
                });
            }
        }
        var isDisable = function (items) {
            if (items.statusId != 2) {
              return true;
            }
            return false;
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

            if (self.model.uploadStockApprovalDetail.items.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
        }
        var isSelected = function (item) {
            return item.isSelected;
          }

        var ValidateQuantity = function (item) {
            var isValid = true;
            if (isNaN(parseFloat(item.uploadedQuantity))) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error("Please Enter Upload Quantity");
            }
            return isValid;
        }
        var NavigateToBack = function () {
            $state.go('stockAdjustmentApproval');
        }

        $scope.calculateReceivedQty = function(item){
            if(item.isThreeUOMApplicable == false){
                if(item.uomTypeId == 1){
                    item.receivedQtyCon = (item.uploadedQuantity +"*"+ item.uomList[0].conversion);
                    item.receivedQtyCalculation = (item.uploadedQuantity * item.uomList[0].conversion);
                    item.uomUnitData = item.uomList[2].uomUnit;
                }
                else if(item.uomTypeId == 2){
                        
                }
                else{
                    item.receivedQtyCon = (item.uploadedQuantity);
                    item.receivedQtyCalculation = (item.uploadedQuantity);
                    item.uomUnitData = item.uomList[2].uomUnit;
                }
            }
            else{
                if(item.uomTypeId == 1){
                    item.receivedQtyCon = (item.uploadedQuantity +"*"+ item.uomList[0].conversion+"*"+ item.uomList[1].conversion);
                    item.receivedQtyCalculation = (item.uploadedQuantity * item.uomList[0].conversion * item.uomList[1].conversion);
                    item.uomUnitData = item.uomList[2].uomUnit;
                }
                else if(item.uomTypeId == 2){
                    item.receivedQtyCon = (item.uploadedQuantity +"*"+ item.uomList[1].conversion);
                    item.receivedQtyCalculation = (item.uploadedQuantity * item.uomList[1].conversion);
                    item.uomUnitData = item.uomList[2].uomUnit;
                }
                else{
                    item.receivedQtyCon = (item.uploadedQuantity);
                    item.receivedQtyCalculation = (item.uploadedQuantity);
                    item.uomUnitData = item.uomList[2].uomUnit;
                }
            }
        }
      
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("stockAdjustmentApprovalDetails", {
            url: "/stockAdjustmentApprovalDetails",
            templateUrl: 'views/PharmacyInventory/StockAdjustment/details-stock-adjustment-approval.html',
            controller: "StockAdjustmentApprovalDetails.Controller",
            controllerAs: "vm",
            title: 'Stock Adjustment Approval'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("StockAdjustmentApprovalDetails.Controller", stockAdjustmentApprovalDetailsController);
})();
