(function () {
    "use strict";

    function rejectedAssetAcceptanceController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, $filter, $state, growl, CurrencyService, commonDetailService, screenDataService , PaginationService,reportService) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
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
                fromStoreList: [{
                    "store": "All Stores"
                }],
                toStoreList: [{
                    "store": "All Stores"
                }],
                statusList: [{
                    "status": "All Statuses"
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

            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })
            self.model.searchAssetReturnAcceptance = searchAssetReturnAcceptance;
           
            
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.GetRejectedAssetAcceptance = GetRejectedAssetAcceptance;
            self.model.NavigateToDetail = NavigateToDetail;
            self.model.initialCall = initialCall;

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

        var PopulateDropdown = function () {

            var URI = CONSTANTS.ASSET.REJECTED_ASSET_ACCEPTANCE.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

            self.model.fromStore = self.model.fromStoreList[0];
            self.model.toStore = self.model.toStoreList[0];
            self.model.status = self.model.statusList[0];
           

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.fromStoreList = self.model.fromStoreList.concat(response.data.fromStore);
                    self.model.toStoreList = self.model.toStoreList.concat(response.data.toStore);
                    self.model.statusList = self.model.statusList.concat(response.data.status);
                    initialCall();
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
        $scope.assetReturnAcceptance = function (item) {
            var data = {
                "appName": "HIS",
                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                "reportDesignFilePath": "rptRejectAssetReturnAcceptance.rpt",
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

        var initialCall = function () {
            if (!screenDataService.getScreenData()) {
                GetRejectedAssetAcceptance()
            } else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'rejectedAssetAcceptance') {
                        var requestData = dataobj.searchrequest;
                        self.model.fromStore = self.model.fromStoreList.find(function (fromStore) {
                            return requestData.fromStore.id == fromStore.id;
                        });
                        self.model.toStore = self.model.toStoreList.find(function (toStore) {
                            return requestData.toStore.id == toStore.id;
                        });
                    
                        self.model.status = self.model.statusList.find(function (status) {
                            return requestData.status.id == status.id;
                        }); 
                        self.model.returnNo = requestData.returnNo
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);
                    }
                });
                GetRejectedAssetAcceptance();
            }
        }

        var ValidateDate = function () {
            var isValid = true;

            if (self.model.fromDate > self.model.toDate) {
                growl.error('From Date should lesser than To Date');
                isValid = false;
            }

            if (!self.model.fromDate || !self.model.toDate) {
                growl.error('Enter Valid Date');
                isValid = false;
            }

            return isValid;
        }

        var GetRejectedAssetAcceptance = function () {
            if (ValidateDate()) {
                  
                var URI = CONSTANTS.ASSET.REJECTED_ASSET_ACCEPTANCE.SEARCH_API;
                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "fromStoreId": !!self.model.fromStore && !!self.model.fromStore.id ? self.model.fromStore.id : undefined,
                    "toStoreId": !!self.model.toStore && !!self.model.toStore.id ? self.model.toStore.id : undefined,
                    "statusId": !!self.model.status && !!self.model.status.id ? self.model.status.id : undefined,
                    "returnNo": !!self.model.returnNo ? self.model.returnNo : undefined,
            		"unitId":$scope.unitId,
            		"userId":$scope.userId,
                   
                };

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        self.model.pagination.data = [];
                        self.model.rejectedAssetAcceptanceList = response.data;
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.rejectedAssetAcceptanceList , self.model.pagination);
                          }
                        
                        $rootScope.stopLoader();
                    },
                    function errorCallback(response) {
                    	 self.model.pagination.data = [];
                         self.model.rejectedAssetAcceptanceList = response.data;
                         if(!!response && !!response.data && response.data.length > 0){            
                             PaginationService.searchData(self.model.rejectedAssetAcceptanceList , self.model.pagination);
                           }
                         
                        $rootScope.stopLoader();
                        growl.error('Error');
                    });
                var screenData = {
                    searchrequest: {
                        "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                        "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                        "fromStore": self.model.fromStore,
                        "toStore": self.model.toStore,
                        "status": self.model.status,
                        "returnNo": self.model.returnNo,
                    },
                    screenName: 'rejectedAssetAcceptance',
                }
                screenDataService.setScreenData(screenData);

            }
        }

        var NavigateToDetail = function (item) {
            commonDetailService.setDataId(item.id);
            $state.go('detailsRejectedAssetAcceptance');
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("rejectedAssetAcceptance", {
            url: "/rejectedAssetAcceptance",
            templateUrl: 'views/master/UnitMaster/assetManagement/rejected-asset-acceptance.html',
            controller: "RejectedAssetAcceptance.Controller",
            title: 'Rejected Asset Acceptance',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 218);
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
        .controller("RejectedAssetAcceptance.Controller",rejectedAssetAcceptanceController)
})();