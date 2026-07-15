(function () {
    "use strict";

    function nonReturnableGatePassController($scope, $rootScope, $http, $filter, $state, GenericService, commonDetailService, cancelReasonService, rejectReasonService, StatusService, growl, CONSTANTS,$cookies,PaginationService) {
        var self = this;

        var cookieObject = $cookies.getObject('cookieObject');
        if(cookieObject == undefined){ 
        	$state.go('login');
        	return;
        	}
        
        $scope.unitId = cookieObject.unitId;
        $scope.organizationId = cookieObject.organizationId;
        $scope.userId = cookieObject.userId;
        $scope.storeId = cookieObject.storeId;
        
        function initializeController() {
            $rootScope.loginpage = true;

            self.model = {
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
                gatePassList: [],
                storeList: [{
                    "store": "All"
                }],
                statuses: [{
                    "status": "All"
                }],
                pagination: {
                    currentPage: 1,
                    totalPages: 0,
                    pageSize: '10',
                    search: '',
                    searchData: PaginationService.searchData,
                    setTotalPages: PaginationService.setTotalPages,
                    getPagesList: PaginationService.getPagesList,
                    setPage: PaginationService.setPage,
                    setFirstPage: PaginationService.setFirstPage,
                    setPreviousPage: PaginationService.setPreviousPage,
                    setNextPage: PaginationService.setNextPage,
                    setLastPage: PaginationService.setLastPage,
                    sortBy: PaginationService.sortBy
                  }
            };

            rejectReasonService.GetReason().then(function (reason) {
                self.model.rejectReasons = reason;
            });
           
            cancelReasonService.GetReason().then(function(reason) {
                self.model.cancelReasons = reason;
            });

            StatusService.GetStatus().then(function (status) {
                self.model.statusList = status;
            });

            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.SearchVendorNames = SearchVendorNames;
            self.model.AddVendorName = AddVendorName;
            self.model.GetNonReturnableGatePass = GetNonReturnableGatePass;
            self.model.NavigateToAdd = NavigateToAdd;
            self.model.NavigateToDetails = NavigateToDetails;

            self.model.SelectAllRequests = SelectAllRequests;
            self.model.SelectNonReturnableGatePass = SelectNonReturnableGatePass;
            self.model.isDisable = isDisable;
            self.model.UpdateGatePassRequest = UpdateGatePassRequest;
            self.model.ValidateAndUpdate = ValidateAndUpdate;
            self.model.ModalDismiss = ModalDismiss;
            self.model.isEdit = isEdit;
            self.model.isCancel = isCancel;
            self.model.NavigateToEdit =NavigateToEdit;
            PopulateDropdown();
        }
        
        var setStatus = function (x){
            var status = self.model.statusList.find(function(obj){
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
            self.model.store = self.model.storeList[0];
            self.model.status = self.model.statuses[0];

            var URI = CONSTANTS.ASSET.AUCTION_VENDOR_APPROVAL.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.storeList = self.model.storeList.concat(response.data.store);
                    self.model.statuses = self.model.statuses.concat(response.data.status);
                    $rootScope.stopLoader();
                    GetNonReturnableGatePass();
                    
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var ValidateDate = function () {
            var isValid = true;
            if (self.model.fromDate > self.model.toDate) {
                growl.error('From Date should lesser than To Date');
                isValid = false;
            }

            return isValid;
        }

        var SearchVendorNames = function (search) {
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

        var nameNotFound = function (search) {
            var item = {
                "itemFound": false,
                "detail": "Vendor Not found with name " + search
            };
            self.model.vendorNames = [item];
            return self.model.vendorNames;
        }

        var AddVendorName = function (vendor, item) {

            self.model.vendorName = vendor.vendorName;
            self.model.vendorId = vendor.id;
        }

        var GetNonReturnableGatePass = function () {
            if (ValidateDate()) {
                var URI = CONSTANTS.ASSET.NON_RETURNABLE_GATEPASS.SEARCH_API;

                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "storeId": self.model.store.id,
                    "vendorId": self.model.vendorId,
                    "nonReturnableGatepassNo": self.model.gatePassNo,
                    "statusId": self.model.status.id,
                    "unitId":$scope.unitId,
                    "userId":$scope.userId,

                };

                $rootScope.startLoader();
                self.model.selectAllRows=false;
                self.model.SelectedRequestIds = [];
                self.model.isItemChecked = false;
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        
                        
                    	self.model.pagination.data = [];
                    	self.model.gatePassList = response.data;
                        
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.gatePassList , self.model.pagination);
                          }
                        
                        $rootScope.stopLoader();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                    }
                );
            }
        }

        var NavigateToAdd = function () {
            $state.go('addNewNonReturnableGatePass');
        }
        var NavigateToEdit = function (gatePass) {
            commonDetailService.setDataId(gatePass.id);
            $state.go('addNewNonReturnableGatePass');
        }

        var NavigateToDetails = function (gatePass) {
            commonDetailService.setDataId(gatePass.id);
            $state.go('detailsNewNonReturnableGatePass');
        }


        var SelectAllRequests = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestIds = [];
                self.model.gatePassList.forEach(function (gatePass) {
                    if (!isDisable(gatePass)) {
                        gatePass.isSelected = true;
            self.model.isItemChecked = true;
                        self.model.SelectedRequestIds.push(gatePass.id);
                    }
                });
            } else {
                self.model.SelectedRequestIds = [];
                self.model.gatePassList.forEach(function (gatePass) {
                    gatePass.isSelected = false;
                    self.model.isItemChecked = false;


                });
            }
        }

        var isDisable = function (gatePass) {
            if (gatePass.status != 'New') {
                return true;
            }
            return false;
        }

        var isEdit = function (gatePass) {
            if (gatePass.status == 'Pending') {
                return true;
            } else if (gatePass.status == 'New') {
                return false;
            } else if (gatePass.status == 'Closed' || gatePass.status == 'Cancelled') {
                return true;
            }
        }

        var isCancel = function (gatePass) {
            if (gatePass.status == 'Pending') {
                return true;
            } else if (gatePass.status == 'New') {
                return false;
            } else if (gatePass.status == 'Closed' || gatePass.status == 'Cancelled') {
                return true;
            }
        }

        var isSelected = function (gatePass) {
            if (!isDisable(gatePass))
                return gatePass.isSelected;
            else
                return true;
        }

        var SelectNonReturnableGatePass = function (gatePass) {
            var gatePassIds = self.model.SelectedRequestIds;

            if (gatePass.isSelected) {
                gatePassIds.push(gatePass.id);
            } else {
                var index = gatePassIds.indexOf(gatePass.id);
                if (index > -1) {
                    gatePassIds.splice(index, 1);
                }
            }

            if (self.model.gatePassList.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            self.model.isItemChecked = false;
            self.model.gatePassList.forEach(function (item) {
              if (item.isSelected == true) {
                self.model.isItemChecked = true;
                return;
              }
            });
        }

        var ValidateAndUpdate = function (action, popupName, data) {
            self.model.action = action;
            self.model.id = data.id;
            var popup = angular.element('#' + popupName);
            popup.modal('show');
            self.model.cancelReason = "";
            self.model.cancelNote = "";
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

        var UpdateGatePassRequest = function () {
            if (self.model.action == 'Update') {
                if (ValidateStatus()) {
                    var URI = CONSTANTS.ASSET.NON_RETURNABLE_GATEPASS.APPROVE_API;

                    var data = {
                        "ids": self.model.SelectedRequestIds,
                        "statusId": setStatus('Closed'),
                        "approvalStatusId": setStatus("Approved"),
                        "userId":$scope.userId,
                        "organizationId":$scope.organizationId,
    					"unitId":$scope.unitId,
    					"storeId":$scope.storeId,
                    };
                    $rootScope.startLoader();
                    GenericService.serviceAction("PATCH", URI, data).then(
                        function (response) { 
                            GetNonReturnableGatePass();
                            self.model.selectAllRows=false;
                            self.model.SelectedRequestIds = [];
                            self.model.isItemChecked = false;

                            $rootScope.stopLoader();
                            growl.success(response.data.message);
                        },
                        function (err) {
                            $rootScope.stopLoader();
                            growl.error('error');
                        });
                } else {
                    growl.error('Select non returnable gatepass Request to Approve');
                    return;
                }
            } else if (self.model.action == 'Cancel') {
                if (ValidetReason()) {
                    var URI = CONSTANTS.ASSET.NON_RETURNABLE_GATEPASS.CANCEL_API + self.model.id;

                    var data = {
                        "statusId": setStatus('Cancelled'),
                        // "approvalStatusId": setStatus('Cancelled'),
                        "cancelReasonId": parseInt(self.model.cancelReason),
                        "cancelNote": self.model.cancelNote,
                        "userId":$scope.userId,
                    }

                    $rootScope.startLoader();
                    GenericService.serviceAction("PATCH", URI, data).then(
                        function (response) {
                            self.model.SelectedRequestIds = [];

                            ModalDismiss('cancelGatePassRequest');
                            GetNonReturnableGatePass();
                            $rootScope.stopLoader();
                            growl.success(response.data.message);
                        },
                        function (err) {
                            $rootScope.stopLoader();
                            growl.error('error');
                        });
                }
            }
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("nonReturnableGatePass", {
            url: "/nonReturnableGatePass",
            templateUrl: 'views/Asset Management/non-returnable-gatepass.html',
            controller: "NonReturnableGatePass.Controller",
            title: 'Non Returnable Gate Pass',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 212);
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
        .controller("NonReturnableGatePass.Controller", nonReturnableGatePassController);
})();