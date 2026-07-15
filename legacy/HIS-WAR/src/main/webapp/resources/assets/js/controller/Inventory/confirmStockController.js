(function () {
    "use strict";

    function confirmStockController($scope, $rootScope, $http, CONSTANTS, StatusService, cancelReasonService, commonDetailService, $state, GenericService, growl,$cookies,$filter,reportService ,PaginationService) {
        var self = this;
        var stockId;
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;
            self.model = {
                stores: [{
                    "store": "All Stores"
                }],
                statuses: [{
                    "status": "All Status"
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
                   // maxDate: new Date() 
                },
                fromDate: new Date(),
                toDate: new Date(),
                SelectedRequestIds: [],
                selectAllRows: false,
                
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
            StatusService.GetStatus().then(function (status) {
                self.model.statuselect = status;
            });

            cancelReasonService.GetReason().then(function (reason) {
                self.model.reasons = reason;
            });
            
            var selectedTopStore = JSON.parse(sessionStorage.getItem('selectedTopStore')); 
		 	$scope.storeId = selectedTopStore.storeId;        
		 
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.PopulateDropdown = PopulateDropdown;
            self.model.ConfirmStockSearch = ConfirmStockSearch;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.SelectConfirmRequest = SelectConfirmRequest;
            self.model.isSelected = isSelected;
            self.model.isDisable = isDisable;
            self.model.ApproveConfirmStock = ApproveConfirmStock;
            self.model.ValidateAndUpdate = ValidateAndUpdate;
            self.model.setStatus = setStatus;
            self.model.ConfirmStockDetails = ConfirmStockDetails;
            self.model.printConfirmReport = printConfirmReport;
            PopulateDropdown()
            ConfirmStockSearch();
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
            var stock = self.model;
            stock.store = stock.stores[0];
            stock.status = stock.statuses[0];
            var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.CONFIRM_STOCK.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    stock.stores = stock.stores.concat(response.data.store);
                    stock.statuses = stock.statuses.concat(response.data.status);
                    try{                        
            		 	self.model.store  =self.model.stores.filter(x=>x.id==$scope.storeId)[0];            		 
                    }catch (e) {
                        console.log("error!",e);          
                    }  
                    $rootScope.stopLoader();
                    // GetStoreIndents();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var ConfirmStockSearch = function (isSearch) {
            var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.CONFIRM_STOCK.SEARCH_API;
            var data = {
                "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                "storeId": self.model.store.id,
                "statusId": self.model.status.id,
                "unitId": $scope.unitId
            };
            try{
            	if(self.model.store.id  == undefined && isSearch == undefined)
            		data.storeId = $scope.storeId;
            	else if(self.model.store.id  == undefined && isSearch != undefined){
           	 			data.storeId= "";
            	}else{
                    data.storeId = self.model.store.id;            		
            	}}catch(e){} 
            $rootScope.startLoader();            
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    for (var i = 0; i < response.data.length; i++) {
                        response.data[i]["isChecked"] = false;
                    }
                    
                    self.model.pagination.data=[];
                    
                    self.model.confirmStockData = response.data;
                    
                    if(!!response && !!response.data && response.data.length > 0){            
                        PaginationService.searchData(self.model.confirmStockData, self.model.pagination);
                      }
               	 
                    
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                    growl.error('Error');
                });
        }

        var printConfirmReport = function (stock) {
            var data = {
                "appName": "HIS",
                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                "reportDesignFilePath": "get_Confirm_stock.rpt",
                "reportFormat": "PDF",
                "exportFileName": "ConfirmStock",
                "reportParams": [
                    {
                        "paramName": "id",
                        "paramType": "Integer",
                        "paramValue": stock.id
                    }
                ]
            };
            reportService.generate(REPORT_API_URI1, data);
        }

        var SelectAllRequests = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestIds = [];
                self.model.confirmStockData.forEach(function (stock) {
                    if (!isDisable(stock)) {
                        stock.isSelected = true;
                        self.model.isItemChecked = true;
                        self.model.SelectedRequestIds.push(stock.id);
                    }
                });
            } else {
                self.model.SelectedRequestIds = [];
                self.model.confirmStockData.forEach(function (stock) {
                    stock.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }

        }

        var isSelected = function (stock) {
            if (!isDisable(stock))
                return stock.isSelected;
            else
                return true;
        }

        var isDisable = function (stock) {
            if (stock.status != 'Pending') {
                return true;
            }
            return false;
        }

        var SelectConfirmRequest = function (stock) {
            var stockIds = self.model.SelectedRequestIds;

            if (stock.isSelected) {
                stockIds.push(stock.id);
            } else {
                var index = stockIds.indexOf(stock.id);
                if (index > -1) {
                    stockIds.splice(index, 1);
                }
            }

            if (self.model.confirmStockData.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            self.model.isItemChecked = false;
            self.model.confirmStockData.forEach(function (stock) {
                if (stock.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }

        var ApproveConfirmStock = function () {
            if (self.model.action == 'Update') {
                var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.CONFIRM_STOCK.APPROVE_API;

                var data = {
                    "confirmStockIds": self.model.SelectedRequestIds,
                    "statusId": 5,
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "storeId":$scope.storeId
                };

                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        self.model.SelectedRequestIds = [];
                        $rootScope.stopLoader();
                        growl.success(response.data.message);
                        ConfirmStockSearch();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.msg);
                    });
            } else if (self.model.action == 'Cancel') {

                var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.CONFIRM_STOCK.CANCEL_API + stockId;

                var data = {
                    "cancelReasonId": self.model.cancelReason,
                    "cancelNote": self.model.cancelNote,
                    "statusId": setStatus('Cancelled'),
                    "unitId": $scope.unitId,
                    "userId": $scope.userId
                };

                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        self.model.SelectedRequestIds = [];
                        $rootScope.stopLoader();
                        growl.success(response.data.message);
                        ConfirmStockSearch();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.msg);
                    });
            }

        }

        function setStatus(x) {
            var status = self.model.statuselect.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }

        var ValidateAndUpdate = function (action, popupName, id) {
            stockId = id;
            self.model.action = action;
            var popup = angular.element('#' + popupName);
            popup.modal('show');
        }


        var ConfirmStockDetails = function (stock) {
            commonDetailService.setDataId(stock.id);
            $state.go('detailsConfirmStock');
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("confirmStock", {
            url: "/confirmStock",
            templateUrl: 'views/PharmacyInventory/StockAdjustment/confirm-stock.html',
            controller: "ConfirmStock.Controller",
            title: 'Confirm Stock',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 152);
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
        .controller("ConfirmStock.Controller", confirmStockController);
})();
