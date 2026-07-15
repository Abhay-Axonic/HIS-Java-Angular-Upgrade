(function () {
    "use strict";

    function consignmentOutwardController($scope, $rootScope, $http, $filter, $state, $cookies, CONSTANTS, GenericService,PaginationService, commonDetailService, cancelReasonService, StatusService, growl, screenDataService) {
        var self = this;

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
                selectAllRows: false,
                SelectedRequestIds: [],
                storeList: [{
                    "store": "All"
                }],
                statuses: [{
                    "status": "All"
                }]

            };

            cancelReasonService.GetReason().then(function (reason) {
                self.model.cancelReasons = reason;
            });

            StatusService.GetStatus().then(function (status) {
                self.model.statusList = status;
            });

            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.GetConsignOutwardRequests = GetConsignOutwardRequests;
            self.model.NavigateToAddNew = NavigateToAddNew;
            self.model.NavigateToDetails = NavigateToDetails;
            self.model.NavigateToEdit = NavigateToEdit;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.SelectConsignOutward = SelectConsignOutward;
            self.model.isDisable = isDisable;
            self.model.UpdateConsignOutward = UpdateConsignOutward;
            self.model.ValidateAndUpdate = ValidateAndUpdate;
            self.model.SearchVendorNames = SearchVendorNames;
            self.model.AddVendorName = AddVendorName;
            self.model.VendorDetails = VendorDetails;
            self.model.ModalDismiss = ModalDismiss;
            self.model.isEdit = isEdit;
            self.model.isCancel = isCancel;

            PopulateDropdown();
        }
        var initialCall = function () {
            if (!screenDataService.getScreenData()) {
                GetConsignOutwardRequests()
            } else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'consignOutward') {
                        var requestData = dataobj.searchrequest;

                        self.model.status = self.model.statuses.find(function (status) {
                            return requestData.status.id == status.id;
                        });
                        self.model.store = self.model.storeList.find(function (store) {
                            return requestData.store.id == store.id;
                        });
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);
                        self.model.vendor = requestData.vendor;
                    }
                });

                GetConsignOutwardRequests();
            }
        }

        var PopulateDropdown = function () {
            self.model.store = self.model.storeList[0];
            self.model.status = self.model.statuses[0];
            //   var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_REQUEST.DROPDOWN_API;
            var URI = CONSTANTS.PROCUREMENT.CONSIGNMENT.CONSIGNMENT_OUTWARD.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.storeList = self.model.storeList.concat(response.data.store);
                    self.model.statuses = self.model.statuses.concat(response.data.status);
                    $rootScope.stopLoader();
                    initialCall();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var VendorDetails = function (vendorDetails) {
            self.model.personName = vendorDetails.contactPerson;
            self.model.address = vendorDetails.address;
            self.model.country = vendorDetails.country;
            self.model.state = vendorDetails.state;
            self.model.city = vendorDetails.city;
            self.model.area = vendorDetails.area;
            self.model.zipCode = vendorDetails.zipCode;
        }

        var setStatus = function (x) {
            var status = self.model.statusList.find(function (obj) {
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

        var GetConsignOutwardRequests = function () {
            if (ValidateDate()) {
                var URI = CONSTANTS.PROCUREMENT.CONSIGNMENT.CONSIGNMENT_OUTWARD.SEARCH_API;
                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "storeId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
                    "statusId": !!self.model.status && !!self.model.status.id ? self.model.status.id : undefined,
                    		"unitId":$scope.unitId
                }

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        self.model.pagination.data=[];
                        self.model.consignOutwardRequests = response.data;
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.consignOutwardRequests, self.model.pagination);
                          }
                        self.model.SelectedRequestIds = [];
                        self.model.selectAllRows = false;
                        $rootScope.stopLoader();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.message);
                    }
                );
                var screenData = {
                    searchrequest: {
                        "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                        "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                        "status": self.model.status,
                        "store": self.model.store,
                        "vendor": self.model.vendor
                    },
                    screenName: 'consignOutward'
                }
                screenDataService.setScreenData(screenData);
            }
        }

        var NavigateToAddNew = function () {
            $state.go('addNewConsignmentOutward');
        }

        var NavigateToDetails = function (workOrder) {
            commonDetailService.setDataId(workOrder.id);
            $state.go('detailsConsignmentOutward');
        }

        var NavigateToEdit = function (workOrder) {
            commonDetailService.setDataId(workOrder.id);
            $state.go('addNewConsignmentOutward');
        }

        var SelectAllRequests = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestIds = [];
                self.model.consignOutwardRequests.forEach(function (datum) {
                    if (!isDisable(datum)) {
                        datum.isSelected = true;
                        self.model.SelectedRequestIds.push(datum.id);
                    }
                });
            } else {
                self.model.SelectedRequestIds = [];
                self.model.consignOutwardRequests.forEach(function (datum) {
                    datum.isSelected = false;
                });
            }
        }

        var isDisable = function (datum) {
            if (datum.status != 'New') {
                return true;
            }
            return false;
        }

        var isEdit = function (datum) {
            if (datum.status == 'Pending') {
                return true;
            } else if (datum.status == 'New') {
                return false;
            } else if (datum.status == 'Closed' || datum.status == 'Cancelled') {
                return true;
            }
        }

        var isCancel = function (datum) {
            if (datum.status == 'Pending') {
                return true;
            } else if (datum.status == 'New') {
                return false;
            } else if (datum.status == 'Closed' || datum.status == 'Cancelled') {
                return true;
            }
        }

        var isSelected = function (datum) {
            if (!isDisable(datum))
                return datum.isSelected;
            else
                return true;
        }

        var SelectConsignOutward = function (datum) {
            var outwardIds = self.model.SelectedRequestIds;

            if (datum.isSelected) {
                outwardIds.push(datum.id);
            } else {
                var index = outwardIds.indexOf(datum.id);
                if (index > -1) {
                    outwardIds.splice(index, 1);
                }
            }

            if (self.model.consignOutwardRequests.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
        }

        var ValidateAndUpdate = function (action, popupName, data) {
            self.model.action = action;
            self.model.id = data.id;
            var popup = angular.element('#' + popupName);
            popup.modal('show');
        }

        var ValidateStatus = function () {
            var isValid = true;

            if (!self.model.SelectedRequestIds || self.model.SelectedRequestIds.length == 0) {
                isValid = false;
            }

            return isValid;
        }

        var ValidetReason = function () {
            var isValid = true;

            if (!self.model.cancelReason) {
                self.model.isReasonValid = true;
                isValid = false;
            }

            return isValid;
        }

        var ModalDismiss = function (modal) {
            var popup = angular.element('#' + modal);
            popup.modal('hide');
            self.model.cancelNote = undefined;
            self.model.cancelReason = undefined;
        }

        var UpdateConsignOutward = function () {
            if (self.model.action == 'Update') {
                if (ValidateStatus()) {
                    var URI = CONSTANTS.PROCUREMENT.CONSIGNMENT.CONSIGNMENT_OUTWARD.APPROVE_API;
                    var data = {
                        "consOutwId": self.model.SelectedRequestIds,
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
                            GetConsignOutwardRequests();
                            self.model.selectAllRows = false;

                            $rootScope.stopLoader();
                            growl.success(response.data.message);
                        },
                        function (err) {
                            $rootScope.stopLoader();
                            growl.error(err.data.message);
                        });
                } else {
                    growl.error('Select Consignment Outward Request to Approve');
                    return;
                }
            }
            else if (self.model.action == 'Cancel') {
                if (ValidetReason()) {
                    var URI = CONSTANTS.PROCUREMENT.CONSIGNMENT.CONSIGNMENT_OUTWARD.CANCEL_API + self.model.id;
                    var data = {
                        "statusId": setStatus('Cancelled'),
                        "cancelReasonId": self.model.cancelReason ? self.model.cancelReason : undefined,
                        "cancelNote": self.model.cancelNote,
                        "unitId":$scope.unitId,
                        "organizationId":$scope.organizationId,
                        "userId":$scope.userId
                    }

                    $rootScope.startLoader();
                    GenericService.serviceAction("PATCH", URI, data).then(
                        function (response) {
                            self.model.SelectedRequestIds = [];

                            ModalDismiss('cancelConsignmentOutward');
                            GetConsignOutwardRequests();
                            $rootScope.stopLoader();
                            growl.success(response.data.message);
                        }, function (err) {
                            $rootScope.stopLoader();
                            growl.error(err.data.message);
                        });
                }
            }
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

        function nameNotFound(search) {
            var item = {
                "itemFound": false,
                "detail": "Vendor Not found with name " + search
            };
            self.model.vendorNames = [item];
            return self.model.vendorNames;
        }

        function AddVendorName(vendor) {
            self.model.vendorName = vendor.vendorName;
            self.model.vendorId = vendor.id;
        }
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state('consignmentOutward', {
            url: '/consignmentOutward',
            templateUrl: 'views/procurement/consignment/consignment-outward.html',
            controller: "ConsignmentOutward.Controller",
            title: 'Consignment Outward',
         	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 197);
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
        .controller("ConsignmentOutward.Controller", consignmentOutwardController);
})();