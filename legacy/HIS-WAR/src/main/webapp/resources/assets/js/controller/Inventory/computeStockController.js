(function () {
    "use strict";

    function computeStockController($scope, $rootScope, $http, StatusService,commonDetailService, $state, cancelReasonService, CONSTANTS, GenericService, growl,$cookies,$filter,reportService,PaginationService) {
        var self = this;
        var computeId;
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
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
            
            var selectedTopStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
	 	$scope.storeId = selectedTopStore != null ? selectedTopStore.storeId : undefined; // store null -> undefined (the calls gate on storeReady)    

            self.model.PopulateDropdown = PopulateDropdown;
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.ComputeStockSearch = ComputeStockSearch;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.SelectComputeRequest = SelectComputeRequest;
            self.model.isDisable = isDisable;
            self.model.isSelected = isSelected;
            self.model.setStatus = setStatus;
            self.model.ApproveComputeStock = ApproveComputeStock;
            self.model.ValidateAndUpdate = ValidateAndUpdate;
            self.model.ComputeStockDetails = ComputeStockDetails;            
            self.model.printComputeStockReport = printComputeStockReport;
            PopulateDropdown();
            ComputeStockSearch(); 
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
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var compute = self.model;
            compute.store = compute.stores[0];
            compute.status = compute.statuses[0];
            var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.COMPUTE_STOCK.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    compute.stores = compute.stores.concat(response.data.store);
                    compute.statuses = compute.statuses.concat(response.data.status);
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
        var ComputeStockSearch = function (isSearch) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.COMPUTE_STOCK.SEARCH_API;
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
            $rootScope.startLoader()
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    for (var i = 0; i < response.data.length; i++) {
                        response.data[i]["isChecked"] = false;
                    }
                    self.model.pagination.data=[];
                    self.model.computeStockData = response.data;
                
                    if(!!response && !!response.data && response.data.length > 0){            
                        PaginationService.searchData(self.model.computeStockData, self.model.pagination);
                      }
                    self.model.selectAllRows = false;
                    self.model.isItemChecked = false;
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                    growl.error('Error');
                });
        }
        var SelectAllRequests = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestIds = [];
                self.model.computeStockData.forEach(function (compute) {
                    if (!isDisable(compute)) {
                        compute.isSelected = true;
                        self.model.isItemChecked = true;
                        self.model.SelectedRequestIds.push(compute.id);
                    }
                });
            } else {
                self.model.SelectedRequestIds = [];
                self.model.computeStockData.forEach(function (compute) {
                    compute.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }

        }

        var isSelected = function (compute) {
            if (!isDisable(compute))
                return compute.isSelected;
            else
                return true;
        }
        var isDisable = function (compute) {
            if (compute.status != 'Pending') {
                return true;
            }
            return false;
        }

        var SelectComputeRequest = function (compute) {
            var computeIds = self.model.SelectedRequestIds;

            if (compute.isSelected) {
                computeIds.push(compute.id);
            } else {
                var index = computeIds.indexOf(compute.id);
                if (index > -1) {
                    computeIds.splice(index, 1);
                }
            }

            if (self.model.computeStockData.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            self.model.isItemChecked = false;
            self.model.computeStockData.forEach(function (compute) {
                if (compute.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }

        function setStatus(x) {
            var status = self.model.statuselect.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }

        var printComputeStockReport = function (compute) {
            var data = {
                "appName": "HIS",
                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                "reportDesignFilePath": "get_Compute_stock.rpt",
                "reportFormat": "PDF",
                "exportFileName": "ComputeStock",
                "reportParams": [
                    {
                        "paramName": "id",
                        "paramType": "Integer",
                        "paramValue": compute.id
                    }
                ]
            };
            reportService.generate(REPORT_API_URI1, data);
        }

        var ApproveComputeStock = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            if (self.model.action == 'Update') {
                var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.COMPUTE_STOCK.APPROVE_API;

                var data = {
                    "computeStockId": self.model.SelectedRequestIds, 
                    "statusId": setStatus('Approved'),
                    "approvalStatusId": setStatus('Pending Approval'),
                    "organizationId": $scope.organizationId,
                    "unitId": $scope.unitId,
                    "userId": $scope.userId
                };

                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        self.model.SelectedRequestIds = [];
                        $rootScope.stopLoader();
                        if(response.data.code == "false")
                            growl.warning(response.data.message);                           
                       else
                            growl.success(response.data.message); 
                        ComputeStockSearch();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.msg);
                    });
            } else if (self.model.action == 'Cancel') {

                var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.COMPUTE_STOCK.CANCEL_API + computeId;

                var data = {
                    "cancelReasonId": self.model.cancelReason,
                    "cancelNote": self.model.cancelNote,
                    "statusId": setStatus('Cancelled'),
                    "userId":$scope.userId
                };

                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        self.model.SelectedRequestIds = [];
                        $rootScope.stopLoader();
                        growl.success(response.data.message);
                        ComputeStockSearch();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.msg);
                    });
            }

        }
        var ValidateAndUpdate = function (action, popupName, id) {
            computeId = id;
            self.model.action = action;
            var popup = angular.element('#' + popupName);
            popup.modal('show');
        }

        
      var ComputeStockDetails = function (compute) {
        commonDetailService.setDataId(compute.id);
        $state.go('detailsComputeStock'); 
      }
  
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("computeStock", {
            url: "/computeStock",
            templateUrl: 'views/PharmacyInventory/StockAdjustment/compute-stock.html',
            controller: "ComputeStock.Controller",
            title: 'Compute Stock',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 150);
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
        .controller("ComputeStock.Controller", computeStockController);
})(); 