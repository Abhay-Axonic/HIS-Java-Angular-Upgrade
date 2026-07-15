(function () {
    "use strict";

    function scrapRequestController($scope, $rootScope, $http, $state, StatusService, PaginationService,cancelReasonService,screenDataService, commonDetailService, $cookies, CONSTANTS, GenericService, growl,reportService) {
        var self = this;
        var scrapId;
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
                statuses: [{
                    "status": "All"
                }],
                assetTypes: [{
                    "type": "All"
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
                scrapRequestData: undefined
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
            self.model.ScrapRequestSearch = ScrapRequestSearch;
            self.model.SelectScrapRequest = SelectScrapRequest;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.isDisable = isDisable;
            self.model.isSelected = isSelected;
            self.model.ValidateAndUpdate = ValidateAndUpdate;
            self.model.ApproveScrapRequest = ApproveScrapRequest;
            self.model.setStatus = setStatus;
            self.model.AddScrap = AddScrap;
            self.model.EditScrap = EditScrap;
            self.model.print = print;
            self.model.ScrapDetails = ScrapDetails;

            PopulateDropdown();

        }
        var initialCall = function () {
            if (!screenDataService.getScreenData()) {
                ScrapRequestSearch();
            }
            else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'Scrap Request') {

                        var requestData = dataobj.searchrequest;
                        self.model.store = self.model.stores.find(function (store) {
                            return requestData.store.id == store.id;
                        });
                        self.model.assetType = self.model.assetTypes.find(function (assetType) {
                            return requestData.assetType.id == assetType.id;
                        });
                        self.model.status = self.model.statuses.find(function (status) {
                            return requestData.status.id == status.id;
                        });
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);

                    }
                });
                ScrapRequestSearch();
            }

        }

        function setStatus(x) {
            var status = self.model.statuselect.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
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
            var scrap = self.model;
            scrap.store = scrap.stores[0];
            scrap.status = scrap.statuses[0];
            scrap.assetType = scrap.assetTypes[0];
            var URI = CONSTANTS.INVENTORY.SCRAP.SCRAP_REQUEST.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    scrap.stores = scrap.stores.concat(response.data.store);
                    scrap.statuses = scrap.statuses.concat(response.data.status);
                    scrap.assetTypes = scrap.assetTypes.concat(response.data.assetType);
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

        var ScrapRequestSearch = function () {
            if (ValidateDate()) {
                var URI = CONSTANTS.INVENTORY.SCRAP.SCRAP_REQUEST.SEARCH_API;

                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "storeId": self.model.store.id ? self.model.store.id : undefined,
                    "statusId": self.model.status.id ? self.model.status.id : undefined,
                    "assetTypeId": self.model.assetType.id ? self.model.assetType.id : undefined,
                    "unitId": $scope.unitId
                };
                $rootScope.startLoader()

                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        // for (var i = 0; i < response.data.length; i++) {
                        //   response.data[i]["isChecked"] = false;
                        // }
                        self.model.pagination.data=[];
                        self.model.scrapRequestData = response.data;
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.scrapRequestData, self.model.pagination);
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
                        "assetType": self.model.assetType
                    },
                    screenName: 'Scrap Request',
                }
                screenDataService.setScreenData(screenData);
            }
        }


        var SelectAllRequests = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestIds = [];
                self.model.scrapRequestData.forEach(function (scrap) {
                    if (!isDisable(scrap)) {
                        scrap.isSelected = true;
                        self.model.isItemChecked = true;
                        self.model.SelectedRequestIds.push(scrap.id);
                    }
                });
            } else {
                self.model.SelectedRequestIds = [];
                self.model.scrapRequestData.forEach(function (scrap) {
                    scrap.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }

        }

        var isSelected = function (scrap) {
            if (!isDisable(scrap))
                return scrap.isSelected;
            else
                return true;
        }
        var isDisable = function (scrap) {
            if (scrap.status != 'New') {
                return true;
            }
            return false;
        }

        var SelectScrapRequest = function (scrap) {
            var scrapIds = self.model.SelectedRequestIds;

            if (scrap.isSelected) {
                scrapIds.push(scrap.id);
            } else {
                var index = scrapIds.indexOf(scrap.id);
                if (index > -1) {
                    scrapIds.splice(index, 1);
                }
            }

            if (self.model.scrapRequestData.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            self.model.isItemChecked = false;
            self.model.scrapRequestData.forEach(function (scrap) {
                if (scrap.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }

        var ApproveScrapRequest = function () {
            if (self.model.action == 'Update') {
                var URI = CONSTANTS.INVENTORY.SCRAP.SCRAP_REQUEST.APPROVE_API;

                var data = {
                    "scrapId": self.model.SelectedRequestIds,
                    "statusId": setStatus('Pending'),
                    "approvalStatusId": setStatus('Pending Approval'),
                    "userId": $scope.userId
                };

                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        self.model.SelectedRequestIds = [];
                        $rootScope.stopLoader();
                        growl.success(response.data.message);
                        ScrapRequestSearch();
                        self.model.selectAllRows = false;
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.msg);
                    });
            } else if (self.model.action == 'Cancel') {
                if (ValidateCancelation()) {
                    var URI = CONSTANTS.INVENTORY.SCRAP.SCRAP_REQUEST.CANCEL_API + scrapId;

                    var data = {
                        "cancelReasonId": self.model.cancelReason,
                        "cancelNote": self.model.cancelNote,
                        "statusId": setStatus('Cancelled'),
                        "userId": $scope.userId
                    };

                    GenericService.serviceAction("PATCH", URI, data).then(
                        function (response) {
                            self.model.SelectedRequestIds = [];
                            $rootScope.stopLoader();
                            growl.success(response.data.message);
                            ModalDismiss('confirmCancel');
                            ScrapRequestSearch();
                            self.model.cancelReason = '';
                            self.model.cancelNote = '';
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



        var ValidateAndUpdate = function (action, popupName, id) {
            self.model.cancelReason=  undefined;
            self.model.cancelNote = '';
            scrapId = id;
            self.model.action = action;
            var popup = angular.element('#' + popupName);
            popup.modal('show');
        }


        var AddScrap = function () {
            commonDetailService.setDataId(null);
            $state.go('addItemScrapApproval');
        }

        var EditScrap = function (scrap) {
            commonDetailService.setDataId(scrap.id);
            $state.go('addItemScrapApproval');
        }

        
        var print = function (scrap){
        	 
        	 var data = {
 					    "appName": "HIS",
 					    "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
 					    "reportDesignFilePath": "get_Scrap_Request.rpt",
 					    "reportFormat": "PDF",
 					    "exportFileName": "Certificate",
 					    "reportParams": [
 					    {
 					    "paramName": "id",
 					    "paramType": "Integer",
 					    "paramValue":  scrap.id

 					    }
 					    ]
 					    };
 					    reportService.generate(REPORT_API_URI1,data);
         }

        var ScrapDetails = function (scrap) {
            commonDetailService.setDataId(scrap.id);
            $state.go('detailsScrapApproval');
        }

       
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("scrapRequest", {
            url: "/scrapRequest",
            templateUrl: 'views/PharmacyInventory/Store/scrap-approval-worklist.html',
            controller: "ScrapRequest.Controller",
            title: 'Scrap Request',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 156);
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
        .controller("ScrapRequest.Controller", scrapRequestController);
})();
