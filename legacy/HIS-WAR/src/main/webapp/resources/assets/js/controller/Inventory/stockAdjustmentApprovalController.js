(function () {
    "use strict";

    function stockAdjustmentApprovalController($scope, $rootScope, $http, CONSTANTS, StatusService, cancelReasonService, commonDetailService, $state, GenericService, growl,$cookies,$filter,reportService) {
        var self = this;
        var stockId;
        var uploadId;
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;
            self.model = {
                stores: [{
                    "store": "All Stores"
                }],
                statuses: [{
                    "status": "All Status"
                }],
                fromDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date(),
                    minDate : moment(new Date()).subtract(180, 'days').toDate()
                },
                toDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                   maxDate: new Date() 
                },
                fromDate: new Date(),
                toDate: new Date(),
                SelectedRequestIds: [],
                selectAllRows: false,


            };
            StatusService.GetStatus().then(function (status) {
                self.model.statuselect = status;
            });

            cancelReasonService.GetReason().then(function (reason) {
                self.model.reasons = reason;
            });
            
            var selectedTopStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
	 	$scope.storeId = selectedTopStore != null ? selectedTopStore.storeId : undefined; // store null -> undefined (the calls gate on storeReady)        
	 
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.PopulateDropdown = PopulateDropdown;
            self.model.searchStockAdjustmentApproval = searchStockAdjustmentApproval;
            self.model.ValidateAndUpdate = ValidateAndUpdate;
            self.model.ApproveStockAdjustment = ApproveStockAdjustment;
            self.model.isDisable = isDisable;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.SelectUploadStock = SelectUploadStock;
            self.model.printUploadStockApprovalReport = printUploadStockApprovalReport;
            self.model.StockApprovalDetails = StockApprovalDetails;
            
            PopulateDropdown();
            searchStockAdjustmentApproval();
        }

        var StockApprovalDetails = function (upload) {
            commonDetailService.setDataId(upload.id);
            $state.go('stockAdjustmentApprovalDetails');
        }

        var printUploadStockApprovalReport = function (upload) {
            var data = {
                "appName": "HIS",
                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                "reportDesignFilePath": "get_Upload_stock_Approval.rpt",
                "reportFormat": "PDF",
                "exportFileName": "UploadStockApproval",
                "reportParams": [
                    {
                        "paramName": "id",
                        "paramType": "Integer",
                        "paramValue": upload.id
                    }
                ]
            };
            reportService.generate(REPORT_API_URI1, data);
        }

        var isDisable = function (upload) {
            if (upload.status != 'Pending' || upload.approvalStatus != 'Pending Approval') {
                return true;
            }
            return false;
        }

        var SelectAllRequests = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestIds = [];
                self.model.stockAdjustmentApprovalData.forEach(function (upload) {
                    if (!isDisable(upload)) {
                        upload.isSelected = true;
                        self.model.isItemChecked = true;
                        self.model.SelectedRequestIds.push(upload.id);
                    }
                });
            } else {
                self.model.SelectedRequestIds = [];
                self.model.stockAdjustmentApprovalData.forEach(function (upload) {
                    upload.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }

        }

        var SelectUploadStock = function (upload) {
            var indentIds = self.model.SelectedRequestIds;
            $scope.uploadId = upload.id;
            if (upload.isSelected) {
                indentIds.push(upload.id);
            } else {
                var index = indentIds.indexOf(upload.id);
                if (index > -1) {
                    indentIds.splice(index, 1);
                }
            }

            if (self.model.stockAdjustmentApprovalData.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            self.model.isItemChecked = false;
            self.model.stockAdjustmentApprovalData.forEach(function (upload) {
                if (upload.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }

        var isSelected = function (upload) {
            if (!isDisable(upload))
                return upload.isSelected;
            else
                return true;
        }


        var OpenFromDate = function () {
            self.model.fromDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }

        var OpenToDate = function () {
            self.model.toDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }
        var PopulateDropdown = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var stock = self.model;
            stock.store = stock.stores[0];
            stock.status = stock.statuses[0];
            var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.CONFIRM_STOCK.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    stock.stores = stock.stores.concat(response.data.store);
                    stock.statuses = stock.statuses.concat(response.data.status);
                    try{                        
            		 	self.model.store  =self.model.stores.filter(x=>x.id==$scope.storeId)[0];            		 
                    }catch (e) {
                        console.log("error!",e);          
                    }  
                    $rootScope.stopLoader();
                    // GetStoreIndents();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var ValidateDate = function () {
            var isValid = true;

            if (!self.model.fromDate || !self.model.toDate) {
                growl.error('Enter Valid Date');
                isValid = false;
            }

            return isValid;
        }

        var searchStockAdjustmentApproval = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            if (ValidateDate()) {
                var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.UPLOAD_STOCK_APPROVAL.SEARCH_API;
                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "storeId": self.model.store.id,
                    "statusId": self.model.status.id
                };
                try{
                	if(self.model.store.id  == undefined && isSearch == undefined)
                		data.storeId = $scope.storeId;
                	else if(self.model.store.id  == undefined && isSearch != undefined){
               	 			data.storeId= "";
                	}else{
                        data.storeId = self.model.store.id;            		
                	}}catch(e){} 
                $rootScope.startLoader()
                GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    for (var i = 0; i < response.data.length; i++) {
                        response.data[i]["isChecked"] = false;
                    }
                    self.model.stockAdjustmentApprovalData = response.data;
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                    growl.error('Error');
                });
            }
        }

        var ValidateAndUpdate = function (action, popupName, id) {
            uploadId = id;
            $scope.uploadId=id;
            self.model.action = action;
            var popup = angular.element('#' + popupName);
            popup.modal('show');
        }

         var ApproveStockAdjustment = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            if (self.model.action == 'Update') {
                var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.UPLOAD_STOCK_APPROVAL.APPROVE_API;

                var data = {
                    "uploadStockId": self.model.SelectedRequestIds,
                    "statusId": setStatus('Pending'),
                    "approvalStatusId": setStatus('Approved'),
                    "userId": $scope.userId ,
                    "organizationId":$scope.organizationId,
                    "unitId":$scope.unitId
                };

                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        self.model.SelectedRequestIds = [];
                        self.model.isItemChecked = false;
                        self.model.selectAllRows = false;
                        $rootScope.stopLoader();
                        growl.success(response.data.message);
                        UploadStockApprovalSearch();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.msg);
                    });
            } else if (self.model.action == 'Cancel') {
                if (ValidateCancelation()) {
                    var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.UPLOAD_STOCK_APPROVAL.CANCEL_API + $scope.uploadId;

                    var data = {
                        "cancelReasonId": self.model.cancelReason,
                        "cancelNote": self.model.cancelNote,
                        "statusId": setStatus('Cancelled'),
                        "userId" : $scope.userId
                    };

                    GenericService.serviceAction("PATCH", URI, data).then(
                        function (response) {
                            self.model.SelectedRequestIds = [];
                            $rootScope.stopLoader();
                            growl.success(response.data.message);
                            ModalDismiss('confirmCancel');
                            searchStockAdjustmentApproval();
                            self.model.cancelReason = '';
                            self.model.cancelNote = '';
                            UploadStockApprovalSearch();
                            
                        },
                        function (err) {
                            $rootScope.stopLoader();
                            growl.error(err.data.msg);
                        });
                }

            }
        }
        
        var ModalDismiss = function (popupName) {
            var popup = angular.element('#' + popupName);
            popup.modal('hide');
        }
        
        var ValidateCancelation = function () {
            var isValid = true;
            if (!self.model.cancelReason) {
                isValid = false;
                growl.error('Please Select Reason to Cancel');
            }
            return isValid;
        }

        function setStatus(x) {
            var status = self.model.statuselect.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("stockAdjustmentApproval", {
            url: "/stockAdjustmentApproval",
            templateUrl: 'views/PharmacyInventory/StockAdjustment/stock-adjustment-approval.html',
            controller: "stockAdjustmentApproval.Controller",
            title: 'Stock Adjustment Approval',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 1033);
	   				 	});
	   					if(arr.length <= 0){
	   						$state.go("accessDenied")
	   						deferred.reject();
	   					 } else {
	   						deferred.resolve();
	   					 }
	   		        });
	   		
	   		        return deferred.promise;
	   		      }
            },
            controllerAs: "vm"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("stockAdjustmentApproval.Controller", stockAdjustmentApprovalController);
})();
