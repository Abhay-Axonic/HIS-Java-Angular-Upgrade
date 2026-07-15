(function () {
    "use strict";

    function auctionVendorApprovalController($scope, $rootScope, $http, $filter, $state, GenericService, commonDetailService, cancelReasonService, rejectReasonService, StatusService, growl, CONSTANTS,$cookies ,PaginationService) {
        var self = this;

        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
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

            cancelReasonService.GetReason().then(function (reason) {
                self.model.reasons = reason;
            });

            StatusService.GetStatus().then(function (status) {
                self.model.statusList = status;
            });

            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.GetAuctionVendorApproval = GetAuctionVendorApproval;
            self.model.NavigateToDetails = NavigateToDetails;
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
            self.model.store = self.model.storeList[0];
            self.model.status = self.model.statuses[0];

            var URI = CONSTANTS.ASSET.AUCTION_VENDOR_APPROVAL.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.storeList = self.model.storeList.concat(response.data.store);
                    self.model.statuses = self.model.statuses.concat(response.data.status);
                    $rootScope.stopLoader();
                    GetAuctionVendorApproval();
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

        var GetAuctionVendorApproval = function () {
            if (ValidateDate()) {
                var URI = CONSTANTS.ASSET.AUCTION_VENDOR_APPROVAL.SEARCH_API;

                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "storeId": self.model.store.id,
                    "statusId": self.model.status.id,
                    "unitId":$scope.unitId,
                    "userId":$scope.userId,
                };

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        
                    	
                    	self.model.pagination.data = [];
                        self.model.AuctionVendorApprovalList = response.data;
                        
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.AuctionVendorApprovalList , self.model.pagination);
                          }
                        
                        $rootScope.stopLoader();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                    }
                );
            }
        }

        var NavigateToDetails = function (approvalItem) {
            commonDetailService.setDataId(approvalItem.id);
            $state.go('detailsAuctionVendorApproval');
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("auctionVendorApproval", {
            url: "/auctionVendorApproval",
            templateUrl: 'views/Asset Management/auction-vendor-approval.html',
            controller: "AuctionVendorApproval.Controller",
            title: 'Auction Vendor Approval',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 211);
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
        .controller("AuctionVendorApproval.Controller", auctionVendorApprovalController);
})();