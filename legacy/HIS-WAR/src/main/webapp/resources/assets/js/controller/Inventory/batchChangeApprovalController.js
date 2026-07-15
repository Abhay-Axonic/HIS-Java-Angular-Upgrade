(function () {
    "use strict";

    function batchChangeApprovalController($scope, $rootScope, $http, StatusService,PaginationService, cancelReasonService, screenDataService, commonDetailService, $state, $cookies, CONSTANTS, GenericService, growl, reportService) {
        var self = this;
        var batchId;
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
        function initializeController() {
            $rootScope.loginpage = true;
            self.model = {
                pagination: {
                    currentPage: 1,
                    totalPages: 0,
                    pageSize: '10',
                    search: '',
                    searchData: PaginationService.searchData,
                    searchDataByField:PaginationService.searchDataByField,
                    setTotalPages: PaginationService.setTotalPages,
                    getPagesList: PaginationService.getPagesList,
                    setPage: PaginationService.setPage,
                    setFirstPage: PaginationService.setFirstPage,
                    setPreviousPage: PaginationService.setPreviousPage,
                    setNextPage: PaginationService.setNextPage,
                    setLastPage: PaginationService.setLastPage,
                    sortBy: PaginationService.sortBy
                },
                stores: [{
                    "store": "Select Store"
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
                batchChangeApprovalData: undefined
            };
            StatusService.GetStatus().then(function (status) {
                self.model.statuselect = status;
            });

            cancelReasonService.GetReason().then(function (reason) {
                self.model.reasons = reason;
            });

            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.PopulateDropdown = PopulateDropdown;
            self.model.BatchChangeApprovalSearch = BatchChangeApprovalSearch;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.SelectBatchRequest = SelectBatchRequest;
            self.model.isSelected = isSelected;
            self.model.isDisable = isDisable;
            self.model.ValidateAndUpdate = ValidateAndUpdate;
            self.model.ApproveBatchChange = ApproveBatchChange;
            self.model.setStatus = setStatus;
            self.model.BatchChangeApprovalDetails = BatchChangeApprovalDetails;
            self.model.ValidateCancelation = ValidateCancelation;
            self.model.printBatchChangeApprovalReport = printBatchChangeApprovalReport;

            PopulateDropdown();
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
            var batch = self.model;
            var URI = CONSTANTS.GLOBAL.USER_STORE_API + "/"+$scope.organizationId + "/"+$scope.unitId+ "/"+$scope.userId ;
            batch.store = batch.stores[0];
            batch.stores.splice(1);
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    batch.stores = batch.stores.concat(response.data);
                    initialCall();
                    $rootScope.stopLoader();
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
        var initialCall = function () {
            if (!screenDataService.getScreenData()) {
                BatchChangeApprovalSearch()
            }
            else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'Batch Expiry Date Change Approval') {

                        var requestData = dataobj.searchrequest;
                        self.model.store = self.model.stores.find(function (store) {
                            return requestData.store.id == store.id;
                        });
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);

                    }
                });
                BatchChangeApprovalSearch();
            }

        }

        var BatchChangeApprovalSearch = function () {
            if (ValidateDate()) {
                var URI = CONSTANTS.INVENTORY.STORE.BATCH_CHANGE_APPROVAL.SEARCH_API;
                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "storeId": self.model.store.id,
                    "unitId":$scope.unitId

                };
                $rootScope.startLoader()
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        for (var i = 0; i < response.data.length; i++) {
                            response.data[i]["isChecked"] = false;
                        }
                        self.model.pagination.data=[];
                        self.model.batchChangeApprovalData = response.data;
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.batchChangeApprovalData, self.model.pagination);
                          }
                        self.model.selectAllRows = false;
                        self.model.isItemChecked = false;
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoader();
                        growl.error('Error');
                    });
                var screenData = {
                    searchrequest: {
                        "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                        "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                        "store": self.model.store
                    },
                    screenName: 'Batch Expiry Date Change Approval',
                }
                screenDataService.setScreenData(screenData);
            }
        }


        var SelectAllRequests = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestIds = [];
                self.model.batchChangeApprovalData.forEach(function (batch) {
                    if (!isDisable(batch)) {
                        batch.isSelected = true;
                        self.model.isItemChecked = true;
                        self.model.SelectedRequestIds.push(batch.id);
                    }
                });
            } else {
                self.model.SelectedRequestIds = [];
                self.model.batchChangeApprovalData.forEach(function (batch) {
                    batch.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }

        }
        var isSelected = function (batch) {
            return batch.isSelected;
        }

        var isDisable = function (batch) {
            if (batch.approvalStatusId != 10 || batch.statusId != 2) {
                return true;
            }
            return false;
        }

        var SelectBatchRequest = function (batch) {
            var batchIds = self.model.SelectedRequestIds;

            if (batch.isSelected) {
                batchIds.push(batch.id);
            } else {
                var index = batchIds.indexOf(batch.id);
                if (index > -1) {
                    batchIds.splice(index, 1);
                }
            }

            if (self.model.batchChangeApprovalData.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            self.model.isItemChecked = false;
            self.model.batchChangeApprovalData.forEach(function (batch) {
                if (batch.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }

        var ValidateAndUpdate = function (action, popupName, id) {
            batchId = id;
            self.model.action = action;
            var popup = angular.element('#' + popupName);
            popup.modal('show');
        }


        var ApproveBatchChange = function () {
            if (self.model.action == 'Update') {
                var URI = CONSTANTS.INVENTORY.STORE.BATCH_CHANGE_APPROVAL.APPROVE_API;

                var data = {
                    "adjustmentId": self.model.SelectedRequestIds,
                    "statusId": setStatus('Closed'),
                    "approvalStatusId": setStatus('Approved'),
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "storeId":$scope.storeId

                };

                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        self.model.SelectedRequestIds = [];
                        $rootScope.stopLoader();
                        growl.success(response.data.message);
                        self.model.selectAllRows = false;
                        BatchChangeApprovalSearch();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        //growl.error(err.data.msg);
                        
                        /* start code for resolved HIS-10203 Batch/MRP/Expiry Date Change Approval : Unable to Cancel */
                        growl.error(err.data.message);
                        /* end code for resolved HIS-10203 Batch/MRP/Expiry Date Change Approval : Unable to Cancel */
                    });
            } else if (self.model.action == 'Cancel') {
                if (ValidateCancelation()) {
                    var URI = CONSTANTS.INVENTORY.STORE.BATCH_CHANGE.CANCEL_API + batchId;

                    var data = {
                        "cancelReasonId": self.model.cancelReason,
                        "cancelNote": self.model.cancelNote,
                        "statusId": setStatus('Cancelled'),
                        /* start code for resolved HIS-10203 Batch/MRP/Expiry Date Change Approval : Unable to Cancel */
                        "approvalStatusId": setStatus('Cancelled'),
                        "unitId":$scope.unitId,
                        "organizationId":$scope.organizationId,
                        "userId":$scope.userId,
                        "storeId":$scope.storeId

                        /* end code for resolved HIS-10203 Batch/MRP/Expiry Date Change Approval : Unable to Cancel */
                    };

                    GenericService.serviceAction("PATCH", URI, data).then(
                        function (response) {
                            self.model.SelectedRequestIds = [];
                            $rootScope.stopLoader();
                            growl.success(response.data.message);
                            ModalDismiss('confirmCancel');
                            BatchChangeApprovalSearch();
                            StoreIndentData();
                            self.model.cancelReason = '';
                            self.model.cancelNote = '';
                        },
                        function (err) {
                            $rootScope.stopLoader();
                            //growl.error(err.data.msg);
                            
                            /* start code for resolved HIS-10203 Batch/MRP/Expiry Date Change Approval : Unable to Cancel */
                            growl.error(err.data.message);
                            /* end code for resolved HIS-10203 Batch/MRP/Expiry Date Change Approval : Unable to Cancel */
                        });
                }
            }
        }


        function setStatus(x) {
            var status = self.model.statuselect.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }


        var BatchChangeApprovalDetails = function (batch) {
            commonDetailService.setDataId(batch.id);
            $state.go('detailBatchChangeApproval');
        }


        var ValidateCancelation = function () {
            var isValid = true;
            if (!self.model.cancelReason) {
                isValid = false;
                growl.error('Please Select Reason to Cancel');
            }
            return isValid;
        }

        var ModalDismiss = function (popupName) {
            var popup = angular.element('#' + popupName);
            popup.modal('hide');
        }


        /***Chetana**/
        var printBatchChangeApprovalReport = function (batch) {
            var data = {
                "appName": "HIS",
                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                "reportDesignFilePath": "get_Expiry_batch_mrp_Changes.rpt",
                "reportFormat": "PDF",
                "exportFileName": "BatchChangeApproval",
                "reportParams": [
                    {
                        "paramName": "id",
                        "paramType": "Integer",
                        "paramValue": batch.id
                    }
                ]
            };
            reportService.generate(REPORT_API_URI1, data);
        }
       /*****/ 

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("batchExpDateChangeApproval", {
            url: "/batchExpDateChangeApproval",
            templateUrl: 'views/PharmacyInventory/Store/batch-exp-date-change-approval.html',
            controller: "BatchChangeApproval.Controller",
            title: 'Batch/MRP/Date of expiry Change Approval',
            resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 138);
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
        .controller("BatchChangeApproval.Controller", batchChangeApprovalController);
})();
