(function () {
    "use strict";

    function batchExpDateChangeController($scope, $rootScope, $state, $http, $cookies, CONSTANTS,screenDataService,PaginationService, StatusService, cancelReasonService, commonDetailService, GenericService, growl, reportService) {
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
                    "store": "All Stores"
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

            self.model.PopulateDropdown = PopulateDropdown;
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.BatchChangeSearch = BatchChangeSearch;
            self.model.SelectBatchRequest = SelectBatchRequest;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.isDisable = isDisable;
            self.model.isSelected = isSelected;
            self.model.ApproveBatchChange = ApproveBatchChange;
            self.model.ValidateAndUpdate = ValidateAndUpdate;
            self.model.BatchChangeDetails = BatchChangeDetails;
            self.model.EditBatchChange = EditBatchChange;
            self.model.AddBatchChange = AddBatchChange;
            self.model.printBatchChangeReport = printBatchChangeReport;

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
        var initialCall = function () {
            if (!screenDataService.getScreenData()) {
                BatchChangeSearch()
            }
            else {
              screenDataService.getScreenData().forEach(function (dataobj) {
                if (dataobj.screenName == 'Batch Expiry Date Change') {
      
                  var requestData = dataobj.searchrequest;
                  self.model.store = self.model.stores.find(function (store) {
                    return requestData.store.id == store.id;
                  });
                  self.model.fromDate = new Date(requestData.fromDate);
                  self.model.toDate = new Date(requestData.toDate);
      
                }
              });
              BatchChangeSearch();
            }
      
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
        var BatchChangeSearch = function () {
            if(ValidateDate()){
            var URI = CONSTANTS.INVENTORY.STORE.BATCH_CHANGE.SEARCH_API;
            var data = {
                "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                "storeId": self.model.store.id,
                "unitId":$scope.unitId

            };
            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    for (var i = 0; i < response.data.length; i++) {
                        response.data[i]["isChecked"] = false;
                    }
                    self.model.pagination.data=[];
                    self.model.batchChangeData = response.data;
                    if(!!response && !!response.data && response.data.length > 0){            
                        PaginationService.searchData(self.model.batchChangeData, self.model.pagination);
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
                    screenName: 'Batch Expiry Date Change',
                  }
                  screenDataService.setScreenData(screenData);
            }
        }

        var SelectAllRequests = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestIds = [];
                self.model.batchChangeData.forEach(function (batch) {
                    if (!isDisable(batch)) {
                        batch.isSelected = true;
                        self.model.isItemChecked = true;
                        self.model.SelectedRequestIds.push(batch.id);
                    }
                });
            } else {
                self.model.SelectedRequestIds = [];
                self.model.batchChangeData.forEach(function (batch) {
                    batch.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }

        }

        var isSelected = function (batch) {
            if (!isDisable(batch))
                return batch.isSelected;
            else
                return true;
        }
        var isDisable = function (batch) {
            if (batch.status != 'New') {
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

            if (self.model.batchChangeData.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            self.model.isItemChecked = false;
            self.model.batchChangeData.forEach(function (batch) {
                if (batch.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }
        var ValidateAndUpdate = function (action, popupName, id) {
            self.model.cancelReason = undefined;
            self.model.cancelNote = ''; 
            batchId = id;
            self.model.action = action;
            var popup = angular.element('#' + popupName);
            popup.modal('show');
        }

        var ApproveBatchChange = function () {
            if (self.model.action == 'Update') {
                var URI = CONSTANTS.INVENTORY.STORE.BATCH_CHANGE.APPROVE_API;

                var data = {
                    "adjustmentId": self.model.SelectedRequestIds,
                    "statusId": setStatus('Pending'),
                    "approvalStatusId": setStatus('Pending Approval'),
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
                        self.model.isItemChecked = false;
                        BatchChangeSearch();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.msg);
                    });
            } else if (self.model.action == 'Cancel') {
                if (ValidateCancelation()) {
                    var URI = CONSTANTS.INVENTORY.STORE.BATCH_CHANGE.CANCEL_API + batchId;

                    var data = {
                        "cancelReasonId": self.model.cancelReason,
                        "cancelNote": self.model.cancelNote,
                        "statusId": setStatus('Cancelled'),
                        "approvalStatusId":setStatus("Cancelled"),
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
                            ModalDismiss('confirmCancel');
                            BatchChangeSearch();
                            self.model.cancelNote = '';
                            self.model.cancelReason = '';
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

        var AddBatchChange = function () {
            commonDetailService.setDataId(null);
            $state.go('additemBatchChange');
        }

        var EditBatchChange = function (batch) {
            commonDetailService.setDataId(batch.id);
            $state.go('additemBatchChange');
        }

        var BatchChangeDetails = function (batch) {
            commonDetailService.setDataId(batch.id);
            $state.go('detailBatchChange');
        }
        
        /***Chetana**/
        var printBatchChangeReport = function (batch) {
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
        $stateProvider.state("batchExpDateChange", {
            url: "/batchExpDateChange",
            templateUrl: 'views/PharmacyInventory/Store/batch-exp-date-change.html',
            controller: "BatchExpDateChange.Controller",
            title: 'Batch/MRP/Date of expiry Change',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 137 );
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
        .controller("BatchExpDateChange.Controller", batchExpDateChangeController);
})(); 