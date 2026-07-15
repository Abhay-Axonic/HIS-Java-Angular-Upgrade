(function () {
    "use strict";

    function returnableGatepassApprovalController($scope, $rootScope, $http, $state, $filter,PaginationService, commonDetailService, screenDataService, $cookies, CONSTANTS, GenericService, growl) {
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


            };
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
            self.model.ReturnableApprovalDetails = ReturnableApprovalDetails;
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
                ReturnableGatepassSearch()
            }
            else {
              screenDataService.getScreenData().forEach(function (dataobj) {
                if (dataobj.screenName == 'Returnable Gatepass Approval') {
      
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
                  //self.model.toDate = new Date(requestData.toDate);
                  self.model.fromDate = new Date();
                  self.model.toDate = new Date();
                  self.model.vendorId = requestData.vendorId;
                  self.model.vendorName = requestData.vendorName;
                  self.model.againstId = requestData.againstId; 
                }
              });
              ReturnableGatepassSearch();
            }
      
          }
      
        var PopulateDropdown = function () {
            var returnable = self.model;
            returnable.store = returnable.stores[0];
            returnable.status = returnable.statuses[0];
            returnable.assetType = returnable.assetTypes[0];
            var URI = CONSTANTS.INVENTORY.STORE.RETURNABLE_GATEPASS_APPROVAL.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;

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
                var URI = CONSTANTS.INVENTORY.STORE.RETURNABLE_GATEPASS_APPROVAL.SEARCH_API;
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
                    "id": self.model.gatepassId,
                    "unitId":$scope.unitId
                };
                $rootScope.startLoader()
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        self.model.pagination.data=[];
                        self.model.returnableGatepassApprovalData = response.data;
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.returnableGatepassApprovalData, self.model.pagination);
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
                        screenName: 'Returnable Gatepass Approval',
                    }
                    screenDataService.setScreenData(screenData);
            }
        }

        var ReturnableApprovalDetails = function (returnable) {
            commonDetailService.setDataId(returnable.id);
            $state.go('detailsreturnaleGatepassApproval');
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("returnaleGatepassApproval", {
            url: "/returnaleGatepassApproval",
            templateUrl: 'views/PharmacyInventory/Store/returnable-gatepass-approval.html',
            controller: "ReturnableGatepassApproval.Controller",
            title: 'Returnable Gatepass Approval Master',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 134);
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
        .controller("ReturnableGatepassApproval.Controller", returnableGatepassApprovalController);
})(); 