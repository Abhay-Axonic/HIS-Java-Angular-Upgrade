(function () {
    "use strict";

    function consignmentInwardController($scope, $rootScope, $http, $cookies, CONSTANTS, $state, GenericService,PaginationService, StatusService, commonDetailService, screenDataService, cancelReasonService, growl, $filter, reportService) {
        var self = this;

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
                fromDate: new Date(),
                toDate: new Date(),
                indentId: null,
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
                stores: [{
                    "store": "All Stores"
                }],
                statuses: [{
                    "status": "All Status"
                }],
                reasons: [{
                    "reason": "Select  Reason"
                }],
                selectItemList: [],
                SelectedRequestIds: [],
                selectAllRows: false,

            };
            cancelReasonService.GetReason().then(function (reason) {
                self.model.cancelReasons = reason;
            });
            //            cancelReasonService.GetReason().then(function (reason) {
            //                self.model.reasons = self.model.reasons.concat(reason);
            //            });
            StatusService.GetStatus().then(function (status) {
                self.model.statuselect = status;
            });
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.EditConsignmentInward = EditConsignmentInward;
            self.model.ConsignmentInwardAddNew = ConsignmentInwardAddNew;
            self.model.ConsignmentInwardDetails = ConsignmentInwardDetails;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.SelectConsignmentInward = SelectConsignmentInward;
            self.model.isSelected = isSelected;
            self.model.isDisable = isDisable;
            self.model.ApproveInward = ApproveInward;
            self.model.ValidateAndUpdate = ValidateAndUpdate;
            self.model.ConsignmentInwardSearch = ConsignmentInwardSearch;
            self.model.ModalDismiss = ModalDismiss;
            self.model.AddVendorName = AddVendorName;
            self.model.vendorNames = [];
            self.model.SearchVendorNames = SearchVendorNames;
            self.model.prevSearch = '';
            self.model.nameNotFound = nameNotFound;
            self.model.printConsignmentInwardReport = printConsignmentInwardReport;

            PopulateDropdown();
        }

        function nameNotFound(search) {
            var item = {
                "itemFound": false,
                "detail": "Vendor Not found with name " + search
            };
            self.model.vendorNames = [item];
            return self.model.vendorNames;
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

            var inward = self.model;

            var URI = CONSTANTS.PROCUREMENT.CONSIGNMENT.CONSIGNMENT_INWARD.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

            inward.store = inward.stores[0];
            inward.status = inward.statuses[0];

            inward.stores.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    inward.stores = inward.stores.concat(response.data.store);
                    inward.statuses = inward.statuses.concat(response.data.status);
                    initialCall();
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var initialCall = function () {
            if (!screenDataService.getScreenData()) {
                ConsignmentInwardSearch()
            }
            else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'Consignment Inward') {

                        var requestData = dataobj.searchrequest;
                        self.model.store = self.model.stores.find(function (store) {
                            return requestData.store.id == store.id;
                        });
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);

                    }
                });
                ConsignmentInwardSearch();
            }
        }

        var ConsignmentInwardSearch = function () {
            if (ValidateDate()) {
                var URI = CONSTANTS.PROCUREMENT.CONSIGNMENT.CONSIGNMENT_INWARD.SEARCH_API;
                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "storeId": self.model.store.id,
                    "vendorId": self.model.vendorId,
                    "inwardNo": self.model.inwardNo,
                    "statusId": self.model.status.id,
                    "unitId":$scope.unitId
                };
                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        self.model.pagination.data=[];
                        self.model.consignmentInwardData = response.data;
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.consignmentInwardData, self.model.pagination);
                          }
                        self.model.selectAllRows = false;
                        self.model.isItemChecked = false;
                        $rootScope.stopLoader();
                    },
                    function errorCallback(response) {
                        growl.error('Error');
                        $rootScope.stopLoader();
                    });
                var screenData = {
                    searchrequest: {
                        "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                        "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                        "store": self.model.store
                    },
                    screenName: 'Consignment Inward',
                }
                screenDataService.setScreenData(screenData);
            }
        }

        function AddVendorName(vendor) {
            self.model.vendorName = vendor.vendorName;
            self.model.vendorId = vendor.id;
        }

        function SearchVendorNames(search) {
            if (search.length < 1) return;
            if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
                self.model.prevSearch = search;
                var URI = CONSTANTS.GLOBAL.VENDOR_SEARCH_API + search + "/"+$scope.unitId;
                $rootScope.startLoader();
                return GenericService.serviceAction("GET", URI, {})
                    .then(function (response) {
                        if (!!response.data && response.data.length > 0) {
                            self.model.vendorNames = response.data;
                            $rootScope.stopLoader();
                            return $filter('filter')(self.model.vendorNames, {
                                $: search
                            });
                        } else {
                            $rootScope.stopLoader();
                            return nameNotFound(search);
                        }
                    }, function (err) {
                        $rootScope.stopLoader();
                        return nameNotFound(search);
                    });
            } else {
                if (!!self.model.vendorNames && self.model.vendorNames.length > 0 && self.model.vendorNames[0].itemFound != undefined && !self.model.vendorNames[0].itemFound) {
                    return nameNotFound(search);
                } else {
                    return $filter('filter')(self.model.vendorNames, {
                        $: search
                    });
                }
            }
        }

        var ValidateDate = function () {
            var isValid = true;
            if (self.model.fromDate > self.model.toDate) {
                growl.error('From Date should lesser than To Date');
                isValid = false;
              } else if (self.model.fromDate == self.model.toDate) {
                isValid = true;
              }
            if (!self.model.fromDate || !self.model.toDate) {
                growl.error('Enter Valid Date');
                isValid = false;
            }

            return isValid;
        }

        var SelectAllRequests = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestIds = [];
                self.model.consignmentInwardData.forEach(function (inward) {
                    if (!isDisable(inward)) {
                        inward.isSelected = true;
                        self.model.isItemChecked = true;
                        self.model.SelectedRequestIds.push(inward.id);
                    }
                });
            } else {
                self.model.SelectedRequestIds = [];
                self.model.consignmentInwardData.forEach(function (inward) {
                    inward.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }

        }

        var isSelected = function (inward) {
            if (!isDisable(inward))
                return inward.isSelected;
            else
                return true;
        }

        var isDisable = function (inward) {
            if (inward.status != 'New') {
                return true;
            }
            return false;
        }

        var SelectConsignmentInward = function (inward) {
            var inwardIds = self.model.SelectedRequestIds;

            if (inward.isSelected) {
                inwardIds.push(inward.id);
            } else {
                var index = inwardIds.indexOf(inward.id);
                if (index > -1) {
                    inwardIds.splice(index, 1);
                }
            }

            if (self.model.consignmentInwardData.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            self.model.isItemChecked = false;
            self.model.consignmentInwardData.forEach(function (inward) {
                if (inward.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }

        var ApproveInward = function () {
            if (self.model.action == 'Update') {
                var URI = CONSTANTS.PROCUREMENT.CONSIGNMENT.CONSIGNMENT_INWARD.APPROVE_API;

                var data = {
                    "consInwId": self.model.SelectedRequestIds,
                    "statusId": setStatus('Closed'),
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
                        self.model.selectAllRows = false;
                        self.model.isItemChecked = false;
                        ConsignmentInwardSearch();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.msg);
                    });
            } else if (self.model.action == 'Cancel') {
                if (ValidateCancelation()) {
                    var URI = CONSTANTS.PROCUREMENT.CONSIGNMENT.CONSIGNMENT_INWARD.CANCEL_API + self.model.indentId;

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
                            ConsignmentInwardSearch();
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

        function setStatus(x) {
            var status = self.model.statuselect.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }

        var ValidateAndUpdate = function (action, popupName, id) {
            self.model.indentId = id;
            self.model.action = action;
            var popup = angular.element('#' + popupName);
            popup.modal('show');
        }

        var EditConsignmentInward = function (inward) {
            commonDetailService.setDataId(inward.id);
            localStorage.setItem('inwardId', inward.id)
            $state.go('addNewConsignmentInward');
        }

        var ConsignmentInwardDetails = function (inward) {
            localStorage.setItem('inwardId', inward.id)
            commonDetailService.setDataId(inward.id);
            $state.go('detailsConsignmentInward');;
        }

        var ConsignmentInwardAddNew = function () {
            commonDetailService.setDataId(null);
            $state.go('addNewConsignmentInward');;
        }

        /***Chetana**/
        var printConsignmentInwardReport = function (item) {
            var data = {
                "appName": "HIS",
                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                "reportDesignFilePath": "Get_consignment_inward_details.rpt",
                "reportFormat": "PDF",
                "exportFileName": "ConsignmentInward",
                "reportParams": [
                    {
                        "paramName": "id",
                        "paramType": "Integer",
                        "paramValue": item.id
                    }
                ]
            };
            reportService.generate(REPORT_API_URI1, data);
        }
       /*****/ 
        
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("consignmentInward", {
            url: "/consignmentInward",
            templateUrl: 'views/procurement/consignment/consignment-inward.html',
            controller: "ConsignmentInward.Controller",
            title: 'Consignment Inward',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 196);
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
        .controller("ConsignmentInward.Controller", consignmentInwardController);
})(); 