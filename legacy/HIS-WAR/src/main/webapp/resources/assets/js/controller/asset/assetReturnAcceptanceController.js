(function () {
    "use strict";

    function assetReturnAcceptanceController($scope, $rootScope, $http, commonDetailService, $state, $cookies, CONSTANTS, GenericService, growl ,PaginationService,reportService) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;
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
                    "type": "Select Type"
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
                selectAllRows: false,
                SelectedReturnIds: [],
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
            self.model.assetReturnAcceptanceDetails = assetReturnAcceptanceDetails;
            self.model.searchAssetReturnAcceptance = searchAssetReturnAcceptance;
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            getDropdownsList();
            searchAssetReturnAcceptance();

        }

        var OpenFromDate = function () {
            self.model.fromDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }

        var OpenToDate = function () {
            self.model.toDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }

        var getDropdownsList = function () {
            var areturn = self.model;
            areturn.fromStore = areturn.fromStores[0];
            areturn.toStore = areturn.toStores[0];
            areturn.status = areturn.statuses[0];
            areturn.indentType = areturn.indentTypes[0];

            var URI = CONSTANTS.ASSET.ASSET_RETURN_ACCEPTANCE.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    areturn.fromStores = areturn.fromStores.concat(response.data.fromStore);
                    areturn.toStores = areturn.toStores.concat(response.data.toStore);
                    areturn.statuses = areturn.statuses.concat(response.data.status);
                    areturn.indentTypes = areturn.indentTypes.concat(response.data.indentType);
                    // initialCall();
                    $rootScope.stopLoader();
                    // GetStoreIndents();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }
        var searchAssetReturnAcceptance = function () {
        	
            var URI = CONSTANTS.ASSET.ASSET_RETURN_ACCEPTANCE.SEARCH_API;
            var data = {
                "fromStoreId": self.model.fromStore.id ? self.model.fromStore.id : undefined,
                "toStoreId": self.model.toStore.id ? self.model.toStore.id : undefined,
                "statusId": self.model.status.id ? self.model.status.id : undefined,
                "returnNo": self.model.returnNo ? self.model.returnNo : undefined,
                "unitId":$scope.unitId,
                "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                "toDate": moment(self.model.toDate).format('YYYY-MM-DD')
            };
            $rootScope.startLoader()
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    for (var i = 0; i < response.data.length; i++) {
                        response.data[i]["isChecked"] = false;
                    }
                    
                    self.model.pagination.data = [];
                    self.model.searchReturnAcceptanceData = response.data;
                    
                    if(!!response && !!response.data && response.data.length > 0){            
                        PaginationService.searchData(self.model.searchReturnAcceptanceData , self.model.pagination);
                      }
                    
                    
                    self.model.selectAllRows = false;
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                    growl.error('Error');
                });
        }

        var assetReturnAcceptanceDetails = function (areturn) {
            commonDetailService.setDataId(areturn.id);
            $state.go('assetReturnAcceptanceDetails');
        }
        
        $scope.assetReturnAcceptance = function (item) {
            var data = {
                "appName": "HIS",
                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                "reportDesignFilePath": "rptAssetReturnAcceptance.rpt",
                "reportFormat": "PDF",
                "exportFileName": "AssetReturnAcceptance",
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

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("assetReturnAcceptance", {
            url: '/assetReturnAcceptance',
            templateUrl: 'views/master/UnitMaster/assetManagement/asset-return-acceptance.html',
            controller: "AssetReturnAcceptance.Controller",
            title: 'Asset Return Acceptance',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 217);
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
        .controller("AssetReturnAcceptance.Controller", assetReturnAcceptanceController);
})();
