(function () {
    "use strict";

    function varianceApprovalController($scope, $rootScope, $http, CONSTANTS,commonDetailService,$state, StatusService, cancelReasonService, GenericService, growl,$cookies,$filter,reportService,PaginationService) {
        var self = this;
var varianceId;
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
            self.model.VarianceSearch = VarianceSearch;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.SelectVarianceRequest = SelectVarianceRequest;
            self.model.isSelected = isSelected;
            self.model.isDisable = isDisable;
            self.model.ApproveVariance = ApproveVariance;
            self.model.ValidateAndUpdate = ValidateAndUpdate;
            self.model.setStatus = setStatus;
            self.model.VarianceDetails = VarianceDetails;
            self.model.printVarianceReport = printVarianceReport;
            
            PopulateDropdown();
            VarianceSearch();
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
            var variance = self.model;
            variance.store = variance.stores[0];
            variance.status = variance.statuses[0];
            var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.VARIANCE_APPROVAL.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    variance.stores = variance.stores.concat(response.data.store);
                    variance.statuses = variance.statuses.concat(response.data.status);                    
                    try{                        
            		 	self.model.store  = self.model.stores.filter(x=>x.id==$scope.storeId)[0];            		 
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
        var VarianceSearch = function (isSearch) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.VARIANCE_APPROVAL.SEARCH_API;
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
                    
                    self.model.varianceData = response.data;
                    
                    if(!!response && !!response.data && response.data.length > 0){            
                        PaginationService.searchData(self.model.varianceData, self.model.pagination);
                      }
                    
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                    growl.error('Error');
                });
        }
        var SelectAllRequests = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestIds = [];
                self.model.varianceData.forEach(function (variance) {
                    if (!isDisable(variance)) {
                        variance.isSelected = true;
                        self.model.isItemChecked = true;
                        self.model.SelectedRequestIds.push(variance.id);
                    }
                });
            } else {
                self.model.SelectedRequestIds = [];
                self.model.varianceData.forEach(function (variance) {
                    variance.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }

        }

        var isSelected = function (variance) {
            if (!isDisable(variance))
                return variance.isSelected;
            else
                return true;
        }
        var isDisable = function (variance) {
            if (variance.status != 'Pending') {
                return true;
            }
            return false;
        }

        var SelectVarianceRequest = function (variance) {
            var varianceIds = self.model.SelectedRequestIds;

            if (variance.isSelected) {
                varianceIds.push(variance.id);
            } else {
                var index = varianceIds.indexOf(variance.id);
                if (index > -1) {
                    varianceIds.splice(index, 1);
                }
            }

            if (self.model.varianceData.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            self.model.isItemChecked = false;
            self.model.varianceData.forEach(function (variance) {
                if (variance.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }
        
        var printVarianceReport = function (variance) {
            var data = {
                "appName": "HIS",
                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
              //  "reportDesignFilePath": "get_Variance_approval.rpt",
                "reportDesignFilePath": "get_variance_approval.rpt",
                "reportFormat": "PDF",
                "exportFileName": "ComputeStock",
                "reportParams": [
                    {
                        "paramName": "id",
                        "paramType": "Integer",
                        "paramValue": variance.id
                    }
                ]
            };
            reportService.generate(REPORT_API_URI1, data);
        }

        
        var ApproveVariance = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            if (self.model.action == 'Update') {
                var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.VARIANCE_APPROVAL.APPROVE_API;

                var data = {
                    "consumptionId": self.model.SelectedRequestIds,
                    "statusId": setStatus('Pending'),
                    "approvalStatusId": setStatus('Pending Approval'),
                    "unitId": $scope.unitId 
                };

                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        self.model.SelectedRequestIds = [];
                        $rootScope.stopLoader();
                        growl.success(response.data.message);
                        VarianceSearch();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.msg);
                    });
            } else if (self.model.action == 'Cancel') {

                var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.VARIANCE_APPROVAL.CANCEL_API + varianceId;

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
                        VarianceSearch();
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
            varianceId = id;
            self.model.action = action;
            var popup = angular.element('#' + popupName);
            popup.modal('show');
        }

        
    var VarianceDetails = function (variance) {
        commonDetailService.setDataId(variance.id);
        $state.go('detailsVarianceApproval'); 
      }
  
        initializeController();
    }
    
    

    function config($stateProvider) {
        $stateProvider.state("varianceApproval", {
            url: "/varianceApproval",
            templateUrl: 'views/PharmacyInventory/StockAdjustment/variance-approval.html',
            controller: "VarianceApproval.Controller",
            title: 'Variance Approval',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 151);
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
        .controller("VarianceApproval.Controller", varianceApprovalController);
})(); 