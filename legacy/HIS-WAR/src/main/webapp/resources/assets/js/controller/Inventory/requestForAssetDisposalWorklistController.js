(function () {
    "use strict";

    function requestForAssetDisposalWorklistController($scope, $state,
        $rootScope, $http, $cookies, CONSTANTS, commonDetailService,PaginationService, GenericService, growl, reportService) {
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
                    "type": "All Types"
                }],
                scrapTypes: [
                    { "id": 1, "type": "Scrap" },
                    {  "id": 2, "type": "Auction" }
                   ],
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
            self.model.AssetDisposalSearch = AssetDisposalSearch;
            self.model.AssetDisposalDetails = AssetDisposalDetails;
            self.model.printRequestForAssetDisposalWorklistReport = printRequestForAssetDisposalWorklistReport;

            PopulateDropdown();
            AssetDisposalSearch();
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
            scrap.assetType = scrap.assetTypes[0];
            scrap.status = scrap.statuses[0];
            var URI = CONSTANTS.INVENTORY.SCRAP.REQUEST_FOR_ASSET_DISPOSAL.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    scrap.stores = scrap.stores.concat(response.data.store);
                    scrap.statuses = scrap.statuses.concat(response.data.status);
                    scrap.assetTypes = scrap.assetTypes.concat(response.data.assetType);
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
        var AssetDisposalSearch = function () {
            if (ValidateDate()) {
                var URI = CONSTANTS.INVENTORY.SCRAP.REQUEST_FOR_ASSET_DISPOSAL.SEARCH_API;
                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "storeId": self.model.store.id,
                    "statusId": self.model.status.id,
                    "assetTypeId": 2,
                    "scrapType": 1,
                    "unitId": $scope.unitId
                };
                $rootScope.startLoader()
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        for (var i = 0; i < response.data.length; i++) {
                            response.data[i]["isChecked"] = false;
                        }
                        self.model.pagination.data=[];
                        self.model.assetDisposalSearchData = response.data;
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.assetDisposalSearchData, self.model.pagination);
                          }
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoader();
                        growl.error('Error');
                    });
            }
        }

        var AssetDisposalDetails = function (disposal) {
            commonDetailService.setDataId(disposal.id);
            $state.go('detailsRequestForAssestDisposalWorklist');
        }

        /***Chetana**/
        var printRequestForAssetDisposalWorklistReport = function (disposal) {
            var data = {
                "appName": "HIS",
                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                "reportDesignFilePath": "get_Requestforassetdisposal.rpt",
                "reportFormat": "PDF",
                "exportFileName": "RequestForAssetDisposalWorklist",
                "reportParams": [
                    {
                        "paramName": "id",
                        "paramType": "Integer",
                        "paramValue": disposal.id
                    }
                ]
            };
            reportService.generate(REPORT_API_URI1, data);
        }
       /*****/ 
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("requestForAssetDisposalWorklist", {
            url: "/requestForAssetDisposalWorklist",
            templateUrl: 'views/PharmacyInventory/Store/request-for-assest-disposal-worklist.html',
            controller: "RequestForAssetDisposalWorklist.Controller",
            title: 'Request For Asset Disposal Worklist',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 158);
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
        .controller("RequestForAssetDisposalWorklist.Controller", requestForAssetDisposalWorklistController);
})();
