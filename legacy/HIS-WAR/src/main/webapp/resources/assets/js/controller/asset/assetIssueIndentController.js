(function () {
    "use strict";

    function assetIssueIndentController($scope, StatusService, cancelReasonService, $state, commonDetailService,screenDataService, $rootScope, $http, $cookies, CONSTANTS, GenericService, growl, reportService ,PaginationService) {
        var self = this;
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;
            $scope.heading = commonDetailService.getDataId();
		    $scope.tabdata = $scope.heading;
            self.model = {
                fromStores: [{
                    "store": "All Stores"
                }],
                toStores: [{
                    "store": "All Stores"
                }],
                statuses: [{
                    "status": "All"
                }],
                indentTypes: [{
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
                fromStores1: [{
                    "store": "All Stores"
                }],
                toStores1: [{
                    "store": "All Stores"
                }],
                statuses1: [{
                    "status": "All"
                }],
                indentTypes1: [{
                    "type": "All"
                }],
                fromDateOptions1: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date(),
                    minDate : moment(new Date()).subtract(180, 'days').toDate()
                    
                },
                toDateOptions1: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date()
                },
                fromDate1: new Date(),
                toDate1: new Date(),
                
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

            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.OpenFromDate1 = OpenFromDate1;
            self.model.OpenToDate1 = OpenToDate1;
            self.model.AssetIssueIndentSearch = AssetIssueIndentSearch;
            self.model.AssetIssueDetails = AssetIssueDetails;
            self.model.AssetIssueListSearch = AssetIssueListSearch;
            self.model.AssetIssueListDetails = AssetIssueListDetails;
            
            PopulateDropdown();
            PopulateDropdown1();
            if($scope.tabdata == 2){
                AssetIssueListSearch();
            }
            else{
                AssetIssueIndentSearch();
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

        var OpenFromDate1 = function () {
            self.model.fromDateOpened1 = true;
            self.model.toDateOptions1.minDate = self.model.fromDate1;
        }

        var OpenToDate1 = function () {
            self.model.toDateOpened1 = true;
            self.model.toDateOptions1.minDate = self.model.fromDate1;
        }

        var PopulateDropdown = function () {
            var assetIssueIndent = self.model;
            assetIssueIndent.fromStore = assetIssueIndent.fromStores[0];
            assetIssueIndent.toStore = assetIssueIndent.toStores[0];
            assetIssueIndent.status = assetIssueIndent.statuses[0];
            assetIssueIndent.indentType = assetIssueIndent.indentTypes[0];

            var URI = CONSTANTS.ASSET.ASEET_ISSUE.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    assetIssueIndent.fromStores = assetIssueIndent.fromStores.concat(response.data.fromStore);
                    assetIssueIndent.toStores = assetIssueIndent.toStores.concat(response.data.toStore);
                    assetIssueIndent.statuses = assetIssueIndent.statuses.concat(response.data.status);
                    assetIssueIndent.indentTypes = assetIssueIndent.indentTypes.concat(response.data.indentType);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var PopulateDropdown1 = function () {
            var assetIssueIndents = self.model;
            assetIssueIndents.fromStore1 = assetIssueIndents.fromStores1[0];
            assetIssueIndents.toStore1 = assetIssueIndents.toStores1[0];
            assetIssueIndents.status1 = assetIssueIndents.statuses1[0];
            assetIssueIndents.indentType1 = assetIssueIndents.indentTypes1[0];

            var URI = CONSTANTS.ASSET.ASEET_ISSUE.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    assetIssueIndents.fromStores1 = assetIssueIndents.fromStores1.concat(response.data.fromStore);
                    assetIssueIndents.toStores1 = assetIssueIndents.toStores1.concat(response.data.toStore);
                    assetIssueIndents.statuses1 = assetIssueIndents.statuses1.concat(response.data.status);
                    assetIssueIndents.indentTypes1 = assetIssueIndents.indentTypes1.concat(response.data.indentType);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var AssetIssueIndentSearch = function () {
            var URI = CONSTANTS.ASSET.ASEET_ISSUE.SEARCH_API;
            var data = {
                "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                "unitId":$scope.unitId,
                "fromStoreId": self.model.fromStore.id,
                "toStoreId": self.model.toStore.id,
                "indentNo": self.model.indentNo,
                "statusId": self.model.status.id,
                "indentTypeId": self.model.indentType.id
            };
            $rootScope.startLoader()
            GenericService.serviceAction("POST", URI, data).then(
            function (response) {
                $rootScope.stopLoader();
                self.model.pagination ={
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
                self.model.pagination.data=[];
                self.model.assetIssueIndentData = response.data;  
                
                if(!!response && !!response.data && response.data.length > 0){            
                    PaginationService.searchData(self.model.assetIssueIndentData, self.model.pagination);
                  }
           	 
                
            },
            function errorCallback(response) {
                $rootScope.stopLoader();
                growl.error('Error');
            }); 
        }

        var AssetIssueDetails = function (asset) {
            commonDetailService.setDataId(asset.id);
            $state.go('detailsAssetIssueIndent');
        }

        var AssetIssueListSearch = function () {
            var URI = CONSTANTS.ASSET.ASEET_ISSUE.ISSUELIST_SEARCH_API;
            var data = {
                "fromDate": moment(self.model.fromDate1).format('YYYY-MM-DD'),
                "toDate": moment(self.model.toDate1).format('YYYY-MM-DD'),
                "unitId":$scope.unitId,
                "fromStoreId": self.model.fromStore1.id,
                "toStoreId": self.model.toStore1.id,
                "indentNo": self.model.indentNumber,
                "statusId": self.model.status1.id,
                "indentTypeId": self.model.indentType1.id
            };
            $rootScope.startLoader()
            GenericService.serviceAction("POST", URI, data).then(
            function (response) {
                $rootScope.stopLoader();
                self.model.pagination ={
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
                self.model.pagination.data=[];
                self.model.assetIssueListData = response.data;   
                if(!!response && !!response.data && response.data.length > 0){            
                    PaginationService.searchData(self.model.assetIssueListData, self.model.pagination);
                  }
           	 
            },
            function errorCallback(response) {
                $rootScope.stopLoader();
                growl.error('Error');
            }); 
        }

        var AssetIssueListDetails = function (data) {
            commonDetailService.setDataId(data.id);
            $state.go('detailsAssetIssueIndentList');
        }
        
        $scope.printAssetIssueReport = function(data){
        	
        	var data = {
                    "appName": "HIS",
                    "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                    "reportDesignFilePath": "AssetIssue.rpt",
                    "reportFormat": "PDF",
                    "exportFileName": "AssetIssueIndent",
                    "reportParams": [
                        {
                            "paramName": "asset_issue_id",
                            "paramType": "Integer",
                            "paramValue": data.id
                        }
                    ]
                };
        	reportService.generate(REPORT_API_URI1, data);
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("assetIssueIndent", {
            url: "/assetIssueIndent",
            templateUrl: 'views/master/UnitMaster/assetManagement/asset-issue.html',
            controller: "assetIssueIndent.Controller",
            title: 'Asset Issue',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 207);
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
        .controller("assetIssueIndent.Controller", assetIssueIndentController);
})(); 