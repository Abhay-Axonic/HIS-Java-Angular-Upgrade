(function () {
    "use strict";

    function drugRecallController($scope, $rootScope, $http, commonDetailService, StatusService, PaginationService, screenDataService, cancelReasonService, $state, $cookies, CONSTANTS, GenericService, growl, reportService) {
        var self = this;
        var drugId;
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
                drugRecallData: undefined


            };

            StatusService.GetStatus().then(function (status) {
                self.model.statuselect = status;
            });

            cancelReasonService.GetReason().then(function (reason) {
                self.model.reasons = reason;
            });
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.DrugRecallSearch = DrugRecallSearch;
            self.model.DrugRecallDetails = DrugRecallDetails;
            self.model.EditDrugRecall = EditDrugRecall;
            self.model.SelectDrugRequest = SelectDrugRequest;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.isSelected = isSelected;
            self.model.isDisable = isDisable;
            self.model.ApproveDrugRecall = ApproveDrugRecall;
            self.model.setStatus = setStatus;
            self.model.ValidateAndUpdate = ValidateAndUpdate;
            self.model.AddNewDrugRecall = AddNewDrugRecall;
            self.model.printDrugRecallReport = printDrugRecallReport;
            initialCall();

        }
        var OpenFromDate = function () {
            self.model.fromDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }

        var OpenToDate = function () {
            self.model.toDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
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
                DrugRecallSearch()
            } else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'Drug Recall') {
                        var requestData = dataobj.searchrequest;
                        self.model.againstId = requestData.requestTypeId
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);

                    }
                });
                DrugRecallSearch();
            }

        }

        var DrugRecallSearch = function () {
            if (ValidateDate()) {
                var URI = CONSTANTS.INVENTORY.STORE.DRUG_RECALL.SEARCH_API;
                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "recallNo": self.model.recallNo,
                    "requestTypeId": self.model.againstId,
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
                        self.model.drugRecallData = response.data;
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchDataByField(self.model.drugRecallData, self.model.pagination);
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
                        "requestTypeId": self.model.againstId,
                    },
                    screenName: 'Drug Recall',
                }
                screenDataService.setScreenData(screenData);
            }
        }

        var SelectAllRequests = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestIds = [];
                self.model.drugRecallData.forEach(function (drug) {
                    if (!isDisable(drug)) {
                        drug.isSelected = true;
                        self.model.isItemChecked = true;
                        self.model.SelectedRequestIds.push(drug.id);
                    }
                });
            } else {
                self.model.SelectedRequestIds = [];
                self.model.drugRecallData.forEach(function (drug) {
                    drug.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }

        }

        var isSelected = function (drug) {
            if (!isDisable(drug))
                return drug.isSelected;
            else
                return true;
        }
        var isDisable = function (drug) {
            if (drug.status != 'New') {
                return true;
            }
            return false;
        }

        var SelectDrugRequest = function (drug) {
            var drugIds = self.model.SelectedRequestIds;

            if (drug.isSelected) {
                drugIds.push(drug.id);
            } else {
                var index = drugIds.indexOf(drug.id);
                if (index > -1) {
                    drugIds.splice(index, 1);
                }
            }

            if (self.model.drugRecallData.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            self.model.isItemChecked = false;
            self.model.drugRecallData.forEach(function (drug) {
                if (drug.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }

        var ApproveDrugRecall = function () {
            if (self.model.action == 'Update') {
                var URI = CONSTANTS.INVENTORY.STORE.DRUG_RECALL.APPROVE_API;

                var data = {
                    "drugRecallId": self.model.SelectedRequestIds,
                    "statusId": setStatus('Closed'),
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
                        DrugRecallSearch();
                        self.model.selectAllRows = false;
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.msg);
                    });
            } else if (self.model.action == 'Cancel') {
                if (ValidateCancelation()) {
                    var URI = CONSTANTS.INVENTORY.STORE.DRUG_RECALL.CANCEL_API + drugId;

                    var data = {
                        "cancelReasonId": self.model.cancelReason,
                        "cancelNote": self.model.cancelNote,
                        "statusId": setStatus('Cancelled'),
                        "unitId":$scope.unitId,
                        "organizationId":$scope.organizationId,
                        "userId":$scope.userId,
                        "storeId":$scope.storeId

                    };

                    GenericService.serviceAction("PATCH", URI, data).then(
                        function (response) {
                            self.model.SelectedRequestIds = [];
                            $rootScope.stopLoader();
                            growl.success(response.data.message);
                            ModalDismiss('confirmCancel');
                            DrugRecallSearch();
                        },
                        function (err) {
                            $rootScope.stopLoader();
                            growl.error(err.data.msg);
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

        var ValidateAndUpdate = function (action, popupName, id) {
            drugId = id;
            self.model.action = action;
            var popup = angular.element('#' + popupName);
            popup.modal('show');
        }

        var DrugRecallDetails = function (recall) {
            commonDetailService.setDataId(recall.id);
            $state.go('detailsDrugRecall');
        }

        var AddNewDrugRecall = function () {
            commonDetailService.setDataId(null);
            $state.go('AddNewDrugRecall');
        }

        var EditDrugRecall = function (drug) {
            commonDetailService.setDataId(drug.id);
            $state.go('AddNewDrugRecall');
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
        
        
        
        /***Sharad**/
        const REGEX_ALL = /([^\W_]+[^\s-]*) */g;
        const REGEX_FIRST = /([^\W_]+[^\s-]*)/;

        const capitalize = (input, all) => {
          if (!input) return '';
          const regex = (all) ? REGEX_ALL : REGEX_FIRST;
          return input.replace(regex, (txt) =>
            txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase());
        }
        
        
        
        /***Chetana**/
        var printDrugRecallReport = function (drug) {
            var data = {
                "appName": "HIS",
                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                "reportDesignFilePath": "get_Drug_Recall.rpt",
                "reportFormat": "PDF",
                "exportFileName": "DrugRecall",
                "reportParams": [
                    {
                        "paramName": "id",
                        "paramType": "Integer",
                        "paramValue": drug.id
                    }
                ]
            };
            reportService.generate(REPORT_API_URI1, data);
        }
       /*****/ 
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("drugRecall", {
            url: "/drugRecall",
            templateUrl: 'views/PharmacyInventory/Store/drug-recall.html',
            controller: "DrugRecall.Controller",
            title: 'Drug Recall',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 139);
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
        .controller("DrugRecall.Controller", drugRecallController);
})();