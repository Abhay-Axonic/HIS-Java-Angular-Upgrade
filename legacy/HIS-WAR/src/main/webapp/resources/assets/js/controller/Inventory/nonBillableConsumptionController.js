(function () {
    "use strict";

    function nonBillableConsumptionController($scope, StatusService, screenDataService,PaginationService, cancelReasonService, commonDetailService, $state, $rootScope, $http, $cookies, CONSTANTS, GenericService, growl, reportService) {
        var self = this;
        var consumptionId;
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
        function initializeController() {
            $rootScope.loginpage = true;
            var date = new Date();
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
                statuses: [{
                    "status": "All Status"
                }],
                fromDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date(),
                    minDate : date.setDate((new Date()).getDate() - 90),
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
                nonBillableData: undefined



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
            self.model.NonBillableSearch = NonBillableSearch;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.isSelected = isSelected;
            self.model.isDisable = isDisable;
            self.model.SelectConsumptionRequest = SelectConsumptionRequest;
            self.model.ApproveNonBIllableConsumption = ApproveNonBIllableConsumption;
            self.model.setStatus = setStatus;
            self.model.ValidateAndUpdate = ValidateAndUpdate;
            self.model.AddConsumption = AddConsumption;;
            self.model.EditConsumption = EditConsumption;
            self.model.ConsumptionDetails = ConsumptionDetails;
            self.model.printNonBillableConsumptionReport = printNonBillableConsumptionReport;

            PopulateDropdown();
            
        }

        var initialCall = function () {
            if (!screenDataService.getScreenData()) {
                NonBillableSearch()
            }
            else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'Non Billable Consumption') {

                        var requestData = dataobj.searchrequest;
                        self.model.store = self.model.stores.find(function (store) {
                            if (requestData.store.id == store.id)
                                return store;
                        });
                        self.model.status = self.model.statuses.find(function (status) {
                            return requestData.status.id == status.id;
                        });
                        self.model.consumptionNo = requestData.consumptionNo
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);
                    }
                });
                NonBillableSearch();
            }

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
            var consumption = self.model;
            consumption.store = consumption.stores[0];
            consumption.status = consumption.statuses[0];
            var URI = CONSTANTS.INVENTORY.NURSING.NON_BILLABLE_CONSUMPTION.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    consumption.stores = consumption.stores.concat(response.data.store);
                    consumption.statuses = consumption.statuses.concat(response.data.status);
                    initialCall();
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
      
        var NonBillableSearch = function () {
            if(ValidateDate()){
            var URI = CONSTANTS.INVENTORY.NURSING.NON_BILLABLE_CONSUMPTION.SEARCH_API;
            var data = {
                "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                "storeId": self.model.store.id,
                "statusId": self.model.status.id,
                "consumptionNo": self.model.consumptionNo,
                "unitId":$scope.unitId
            };
            self.model.pagination.data=[];
            self.model.pagination.totalPages=0;
            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    for (var i = 0; i < response.data.length; i++) {
                        response.data[i]["isChecked"] = false;
                    }
                   
                    self.model.nonBillableData = response.data;
                    if(!!response && !!response.data && response.data.length > 0){            
                        PaginationService.searchData(self.model.nonBillableData, self.model.pagination);
                      }
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                    growl.error('Error');
                });
            var screenData = {
                searchrequest: {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "store": self.model.store,
                    "status": self.model.status,
                    "consumptionNo": self.model.consumptionNo
                },
                screenName: 'Non Billable Consumption',
            }
            screenDataService.setScreenData(screenData);
        }
    }
        var SelectAllRequests = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestIds = [];
                self.model.nonBillableData.forEach(function (consumption) {
                    if (!isDisable(consumption)) {
                        consumption.isSelected = true;
                        self.model.isItemChecked = true;
                        self.model.SelectedRequestIds.push(consumption.id);
                    }
                });
            } else {
                self.model.SelectedRequestIds = [];
                self.model.nonBillableData.forEach(function (consumption) {
                    consumption.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }

        }

        var isSelected = function (consumption) {
            if (!isDisable(consumption))
                return consumption.isSelected;
            else
                return true;
        }
        var isDisable = function (consumption) {
            if (consumption.status != 'New') {
                return true;
            }
            return false;
        }

        var SelectConsumptionRequest = function (consumption) {
            var consumptionIds = self.model.SelectedRequestIds;

            if (consumption.isSelected) {
                consumptionIds.push(consumption.id);
            } else {
                var index = consumptionIds.indexOf(consumption.id);
                if (index > -1) {
                    consumptionIds.splice(index, 1);
                }
            }

            if (self.model.nonBillableData.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            self.model.isItemChecked = false;
            self.model.nonBillableData.forEach(function (consumption) {
                if (consumption.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }
        $scope.saveBtnDisabled=false;
        var ApproveNonBIllableConsumption = function () {
        	$scope.saveBtnDisabled=true;
            if (self.model.action == 'Update') {
                var URI = CONSTANTS.INVENTORY.NURSING.NON_BILLABLE_CONSUMPTION.APPROVE_API;

                var data = {
                    "consumptionId": self.model.SelectedRequestIds,
                    "statusId": setStatus('Pending'),
                    "approvalStatusId": setStatus('Pending Approval'),
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId

                };

                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        self.model.SelectedRequestIds = [];
                        $rootScope.stopLoader();
                        growl.success(response.data.message);
                        NonBillableSearch();
                        self.model.selectAllRows = false;
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.msg);
                    });
            } else if (self.model.action == 'Cancel') {
                if (ValidateCancelation()) {
                    var URI = CONSTANTS.INVENTORY.NURSING.NON_BILLABLE_CONSUMPTION.CANCEL_API + consumptionId;

                    var data = {
                        "cancelReasonId": self.model.cancelReason,
                        "cancelNote": self.model.cancelNote,
                        "statusId": setStatus('Cancelled'),
                        "unitId":$scope.unitId,
                        "organizationId":$scope.organizationId,
                        "userId":$scope.userId

                    };

                    GenericService.serviceAction("PATCH", URI, data).then(
                        function (response) {
                            self.model.SelectedRequestIds = [];
                            $rootScope.stopLoader();
                            growl.success(response.data.message);
                            ModalDismiss('confirmCancel');
                            NonBillableSearch();
                        },
                        function (err) {
                            $rootScope.stopLoader();
                            growl.error(err.data.msg);
                        });
                }
            }
            var popup = angular.element('#confirmLogout1');
            popup.modal('hide');
        }

        var setStatus = function (x) {
            var status = self.model.statuselect.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }

        var ValidateAndUpdate = function (action, popupName, id) {
            self.model.cancelReason = undefined;
            consumptionId = id;
            self.model.action = action;
            var popup = angular.element('#' + popupName);
            popup.modal('show');
        }

        var AddConsumption = function () {
            commonDetailService.setDataId(null);
            $state.go('addNewnonbillableConsumption');
        }

        var EditConsumption = function (consumption) {
            commonDetailService.setDataId(consumption.id);
            $state.go('addNewnonbillableConsumption');
        }

        var ConsumptionDetails = function (consumption) {
            commonDetailService.setDataId(consumption.id);
            $state.go('detailsnonbillableConsumption');
        }

        var ValidateCancelation = function () {
            var isValid = true;
            if (!self.model.cancelReason) {
                isValid = false;
                growl.error('Please Select Reason to Cancel');
            }
            if(!isValid)
            {
            $scope.saveBtnDisabled=false;
            }
            return isValid;
        }
        var ModalDismiss = function (popupName) {
            var popup = angular.element('#' + popupName);
            popup.modal('hide');
        }

        /***Chetana**/
        var printNonBillableConsumptionReport = function (info) {
            var data = {
                "appName": "HIS",
                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                "reportDesignFilePath": "get_Nonbillableconsumption.rpt",
                "reportFormat": "PDF",
                "exportFileName": "NonBillableConsumption",
                "reportParams": [
                    {
                        "paramName": "id",
                        "paramType": "Integer",
                        "paramValue": info.id
                    }
                ]
            };
            reportService.generate(REPORT_API_URI1, data);
        }
       /*****/ 
        
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("nonBillableConsumption", {
            url: "/nonBillableConsumption",
            templateUrl: 'views/PharmacyInventory/non-billable-consumption.html',
            controller: "NonBillableConsumption.Controller",
            title: 'Department Consumption', 
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 63);
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
        .controller("NonBillableConsumption.Controller", nonBillableConsumptionController);
})();   