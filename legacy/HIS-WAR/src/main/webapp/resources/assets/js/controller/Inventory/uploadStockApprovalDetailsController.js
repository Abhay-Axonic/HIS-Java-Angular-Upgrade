(function () {
    "use strict";

    function uploadStockApprovalDetailsController($scope, $rootScope, commonDetailService, CurrencyService, StatusService, $state, $http, $cookies, CONSTANTS, GenericService, growl) {
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
            self.model.valueOnChange  = valueOnChange; 
            self.model.isSelected = isSelected; 
            self.model.SelectedItems = [];
            fromUploadStock();
        }

        function fromUploadStock() {          
            var id = commonDetailService.getDataId();
            var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.UPLOAD_STOCK_APPROVAL.DETAIL_API + id;
            var data = {};
            $rootScope.startLoader(); 
            GenericService.serviceAction("GET", URI, data).then(
                function (response) {
                    self.model.uploadStockApprovalDetail = response.data;
                    self.model.uploadStockApprovalDetail.items.forEach(function (item) {
                        item.cop = item.cop.toFixed(2);
                        item.mrp = item.mrp.toFixed(2);
                        item.isDisabled = true;
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


        var SaveApproval = function () {

            if (ValidateApproval()) {
                var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.UPLOAD_STOCK_APPROVAL.APPROVE_API + commonDetailService.getDataId();
                var data = {
                    "statusId": setStatus('Approved'),
                    "approvalStatusId": setStatus('Approved'),
                    "items": [],
                    "userId": $scope.userId,
                    "unitId": $scope.unitId,
                    "organizationId": $scope.organizationId
                }

                $rootScope.startLoader();
                angular.forEach(self.model.SelectedItems, function (item) {
                    var newItem = {
                        "id": item.id,
                        "uploadedQuantity": item.uploadedQuantity,
                        "statusId": 5
                    }

                    // if (item.acceptedQuantity > 0 && item.acceptedQuantity < item.consumedQuantity) {
                    //     newItem.statusId = setStatus('Partially Approved');

                    //     if (data.approvalStatusId == setStatus('Reject')) {
                    //         data.approvalStatusId = setStatus('Partially Approved');
                    //     }
                    // }
                    // else if (item.acceptedQuantity >= item.consumedQuantity) {
                    //     data.approvalStatusId = setStatus('Approved');

                    //     if (data.approvalStatusId == setStatus('Reject')) {
                    //         data.approvalStatusId = setStatus('Approved');
                    //     }
                    // }
                    // else if (item.rejectedQuantity >= item.consumedQuantity) {
                    //     newItem.statusId = setStatus('Rejected');
                    // }
                    // else if (item.rejectedQuantity > 0 && item.rejectedQuantity < item.consumedQuantity) {
                    //     newItem.statusId = setStatus('Partially Approved');

                    //     if (data.approvalStatusId == setStatus('Reject')) {
                    //         data.approvalStatusId = setStatus('Approved');
                    //     }
                    // }

                    data.items.push(newItem);
                });

                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        growl.success(response.data.message);
                        NavigateToBack();
                    }, function (err) {
                        $rootScope.stopLoader();
                        growl.error('Somthing Went Wrong');
                    });
            }
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
      
//            else if (parseInt(item.uploadedQuantity) > item.quantity) {
//                item.isQuantityValid = true;
//                isValid = false;
//                growl.error('Entered upload scrap quantity is exceeding the quantity');
//            } else {
//                item.uploadedQuantity = parseInt(item.uploadedQuantity);
//                isValid = true;
//            } 
//      
            return isValid;
        }
        var NavigateToBack = function () {
            $state.go('uploadStockApproval');
        }
        function valueOnChange(item, isSelectAll) {
//            if (isSelectAll)
//              self.model.uploadStockApprovalDetail.items.forEach(function (item) {
//                if (self.model.selectAllRows) {
//                  item.uploadedQuantity = item.quantity;
//                } else
//                  item.uploadedQuantity = 0;
//              });
//            else
//              if (item.isSelected) {
//                item.uploadedQuantity = item.quantity;
//              } else
//                item.uploadedQuantity = 0; 
          }
      
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("detailsUploadStockApproval", {
            url: "/detailsUploadStockApproval",
            templateUrl: 'views/PharmacyInventory/StockAdjustment/details-upload-stock-approval.html',
            controller: "UploadStockApprovalDetails.Controller",
            controllerAs: "vm",
            title: 'Upload Stock Approval'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("UploadStockApprovalDetails.Controller", uploadStockApprovalDetailsController);
})();
