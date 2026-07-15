(function () {
    "use strict";

    function returnableGatepassController($scope, $state, $rootScope, $http,PaginationService, StatusService, reportService,cancelReasonService,screenDataService, commonDetailService, $filter, $cookies, CONSTANTS, GenericService, growl) {
        var self = this;
        var returnableId;
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
                    "status": "All Status"
                }],
                assetTypes: [{
                    "type": "All Asset Type"
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
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.PopulateDropdown = PopulateDropdown;
            self.model.SearchVendorNames = SearchVendorNames;
            self.model.nameNotFound = nameNotFound;
            self.model.AddVendorName = AddVendorName;
            self.model.search = '';
            self.model.prevSearch = '';
            self.model.vendorNames = [];
            self.model.againstId;
            self.model.ReturnableGatepassSearch = ReturnableGatepassSearch;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.SelectGatepassRequest = SelectGatepassRequest;
            self.model.ApproveReturnableGatepass = ApproveReturnableGatepass;
            self.model.isSelected = isSelected;
            self.model.isDisable = isDisable;
            self.model.ValidateAndUpdate = ValidateAndUpdate;
            self.model.setStatus = setStatus;
            self.model.AddReturnable = AddReturnable;
            self.model.EditReturnable = EditReturnable;
            self.model.ReturnableDetails = ReturnableDetails;
            self.model.printReport=printReport;

            PopulateDropdown();
        }
        
        var printReport = function (data) {
            var data = {
              "appName": "HIS",
              "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
              "reportDesignFilePath": "get_GRN_Return.rpt",
              "reportFormat": "PDF",
              "exportFileName": "BillSlip",
              "reportParams": [
                {
                  "paramName": "GReturnId",
                  "paramType": "Integer",
                  "paramValue": data.id
                },
                {
                    "paramName": "Storeid",
                    "paramType": "Integer",
                    "paramValue": data.storeId
                  }
              ]
            };
            reportService.generate(REPORT_API_URI1, data);
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
            var returnable = self.model;
            returnable.store = returnable.stores[0];
            returnable.status = returnable.statuses[0];
            returnable.assetType = returnable.assetTypes[0];
            var URI = CONSTANTS.INVENTORY.STORE.RETURNABLE_GATEPASS.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    returnable.stores = returnable.stores.concat(response.data.store);
                    returnable.statuses = returnable.statuses.concat(response.data.status);
                    returnable.assetTypes = returnable.assetTypes.concat(response.data.assetType);
                    initialCall();
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
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

        var initialCall = function () {
            if (!screenDataService.getScreenData()) {
                ReturnableGatepassSearch()
            }
            else {
              screenDataService.getScreenData().forEach(function (dataobj) {
                if (dataobj.screenName == 'Returnable Gatepass') {
      
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
                  self.model.gatepassNo = requestData.gatepassNo
                  //self.model.fromDate = new Date(requestData.fromDate);
                  self.model.fromDate = new Date();
                  self.model.toDate = new Date();
                  //self.model.toDate = new Date(requestData.toDate);
                  self.model.vendorId = requestData.vendorId;
                  self.model.vendorName = requestData.vendorName;
                  self.model.againstId = requestData.againstId; 
                }
              });
              ReturnableGatepassSearch();
            }
      
          }
      
        var ValidateDate = function () {
            var isValid = true;

            if (!self.model.fromDate || !self.model.toDate) {
                growl.error('Enter Valid Date');
                isValid = false;
            }

            return isValid;
        }

        var ReturnableGatepassSearch = function () {
            if (ValidateDate()) {
                var URI = CONSTANTS.INVENTORY.STORE.RETURNABLE_GATEPASS.SEARCH_API;
                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "storeId": self.model.store.id,
                    "statusId": self.model.status.id,
                    "vendorId": self.model.vendorId,
                    "vendor": self.model.vendorName,
                    "assetTypeId": self.model.assetType.id,
                    "againstId": self.model.againstId,
                    "gatepassNo": self.model.gatepassNo,
                    "unitId":$scope.unitId }; 
                $rootScope.startLoader()
                 if(data.vendor==undefined || data.vendor==null || data.vendor=="")
                    {
                         data.vendorId='';   
                    }
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        for (var i = 0; i < response.data.length; i++) {
                            response.data[i]["isChecked"] = false;
                        }
                        self.model.pagination.data=[];
                        self.model.returnableGatepassData = response.data;
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.returnableGatepassData, self.model.pagination);
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
                        "vendorId": self.model.vendorId,
                        "vendor": self.model.vendorName,
                        "assetType": self.model.assetType,
                        "againstId": self.model.againstId,
                        "gatepassNo": self.model.gatepassNo,
                    },
                    screenName: 'Returnable Gatepass',
                }
                screenDataService.setScreenData(screenData);
            }
        }
        var SelectAllRequests = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestIds = [];
                self.model.returnableGatepassData.forEach(function (returnable) {
                    if (!isDisable(returnable)) {
                        returnable.isSelected = true;
                        self.model.isItemChecked = true;
                        self.model.SelectedRequestIds.push(returnable.id);
                    }
                });
            } else {
                self.model.SelectedRequestIds = [];
                self.model.returnableGatepassData.forEach(function (returnable) {
                    returnable.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }

        }

        var isSelected = function (returnable) {
            if (!isDisable(returnable))
                return returnable.isSelected;
            else
                return true;
        }
        var isDisable = function (returnable) {
            if (returnable.status != 'New') {
                return true;
            }
            return false;
        }

        var SelectGatepassRequest = function (returnable) {
            var returnableIds = self.model.SelectedRequestIds;

            if (returnable.isSelected) {
                returnableIds.push(returnable.id);
            } else {
                var index = returnableIds.indexOf(returnable.id);
                if (index > -1) {
                    returnableIds.splice(index, 1);
                }
            }

            if (self.model.returnableGatepassData.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            self.model.isItemChecked = false;
            self.model.returnableGatepassData.forEach(function (returnable) {
                if (returnable.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }
        $scope.saveBtnDisabled=false;
        var ApproveReturnableGatepass = function () {
        	$scope.saveBtnDisabled=true;
            if (self.model.action == 'Update') {
                var URI = CONSTANTS.INVENTORY.STORE.RETURNABLE_GATEPASS.APPROVE_API;

                var data = {
                    "gatepassId": self.model.SelectedRequestIds,
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
                        $scope.saveBtnDisabled=false;
                        ReturnableGatepassSearch();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.msg);
                    });
            } else if (self.model.action == 'Cancel') {

                var URI = CONSTANTS.INVENTORY.STORE.RETURNABLE_GATEPASS.CANCEL_API + returnableId;

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
                        $scope.saveBtnDisabled=false;
                        growl.success(response.data.message);
                        ReturnableGatepassSearch();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.msg);
                    });
            }

        }
        var ValidateAndUpdate = function (action, popupName, id) {
            returnableId = id;
            self.model.action = action;
            var popup = angular.element('#' + popupName);
            popup.modal('show');
        }
        var AddReturnable = function () {
            commonDetailService.setDataId(null);
            $state.go('addNewReturnableGatepass');
        }

        var EditReturnable = function (returnable) {
            commonDetailService.setDataId(returnable.id);
            $state.go('addNewReturnableGatepass');
        }

        var ReturnableDetails = function (returnable) {
            commonDetailService.setDataId(returnable.id);
            $state.go('detailsReturnableGatepass');
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("returnableGatepass", {
            url: "/returnableGatepass",
            templateUrl: 'views/PharmacyInventory/InPatient/returnable-gatepass.html',
            controller: "ReturnableGatepass.Controller",
            title: 'Returnable Gatepass Master',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 144);
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
        .controller("ReturnableGatepass.Controller", returnableGatepassController);
})();
