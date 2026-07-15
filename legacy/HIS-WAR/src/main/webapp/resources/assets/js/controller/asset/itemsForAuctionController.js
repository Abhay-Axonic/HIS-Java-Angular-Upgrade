(function () {
    "use strict";

    function itemsForAuctionController($scope, $rootScope, $http, $filter, $state, GenericService, commonDetailService, cancelReasonService, rejectReasonService, StatusService, growl, CONSTANTS,$cookies ,PaginationService) {
        var self = this;
var auctionId;
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
                self.model.statuselect = status;
            });

            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.ItemForAuctionSearch = ItemForAuctionSearch;
            self.model.NavigateToDetails = NavigateToDetails;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.SelectAuction = SelectAuction;
            self.model.ApproveAuction =ApproveAuction;
            self.model.ValidateAndUpdate =ValidateAndUpdate;
            self.model.isSelected = isSelected;
            self.model.isDisable = isDisable;
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

            var URI = CONSTANTS.ASSET.ITEMS_FOR_AUCTION.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.storeList = self.model.storeList.concat(response.data.store);
                    self.model.statuses = self.model.statuses.concat(response.data.status);
                    $rootScope.stopLoader();
                    ItemForAuctionSearch();
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

        var ItemForAuctionSearch = function () {
            if (ValidateDate()) {
                var URI = CONSTANTS.ASSET.ITEMS_FOR_AUCTION.SEARCH_API;

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
                    	   
                        self.model.ItemsForAuctionList = response.data;
                        
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.ItemsForAuctionList , self.model.pagination);
                          }
                        
                        $rootScope.stopLoader();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                    }
                );
            }
        }

        var NavigateToDetails = function (auctionItem) {
            commonDetailService.setDataId(auctionItem.id);
            $state.go('itemsAuctionDetails');
        }
        var SelectAllRequests = function () {
            if (self.model.selectAllRows) {
              self.model.SelectedRequestIds = [];
              self.model.ItemsForAuctionList.forEach(function (indent) {
                if (!isDisable(indent)) {
                  indent.isSelected = true;
                  self.model.isItemChecked = true;
                  self.model.SelectedRequestIds.push(indent.id);
                }
              });
            } else {
              self.model.SelectedRequestIds = [];
              self.model.ItemsForAuctionList.forEach(function (indent) {
                indent.isSelected = false;
                self.model.isItemChecked = false;
              });
            }
      
          }

          
    var SelectAuction = function (indent) {
        var auctionIds = self.model.SelectedRequestIds;
  
        if (indent.isSelected) {
          auctionIds.push(indent.id);
        } else {
          var index = auctionIds.indexOf(indent.id);
          if (index > -1) {
            auctionIds.splice(index, 1);
          }
        }
  
        if (self.model.ItemsForAuctionList.every(isSelected)) {
          self.model.selectAllRows = true;
        } else {
          self.model.selectAllRows = false;
        }
        self.model.isItemChecked = false;
        self.model.ItemsForAuctionList.forEach(function (indent) {
          if (indent.isSelected == true) {
            self.model.isItemChecked = true;
            return;
          }
        });
      }

      var ApproveAuction = function () {
        if (self.model.action == 'Update') {
          var URI = CONSTANTS.ASSET.ITEMS_FOR_AUCTION.APPROVE_API;
  
          var data = {
            "ids": self.model.SelectedRequestIds,
            "statusId": setStatus('Closed') ,
            "userId":$scope.userId,
            "organizationId":$scope.organizationId,
			"unitId":$scope.unitId,
          };
  
          $rootScope.startLoader();
          GenericService.serviceAction("PATCH", URI, data).then(
            function (response) {
              self.model.SelectedRequestIds = [];
              $rootScope.stopLoader();
              growl.success(response.data.message);
              self.model.selectAllRows = false;
              self.model.isItemChecked = false;
              self.model.SelectedRequestIds = [];
              ItemForAuctionSearch();
  
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
        auctionId = id;
        self.model.action = action;
        var popup = angular.element('#' + popupName);
        popup.modal('show');
      }
  
  
      var isSelected = function (indent) {
        if (!isDisable(indent))
          return indent.isSelected;
        else
          return true;
      }
  
      var isDisable = function (indent) {
        if (indent.status != 'Pending') {
          return true;
        }
        return false;
      }
  
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("itemsForAuction", {
            url: "/itemsForAuction",
            templateUrl: 'views/Asset Management/items-for-auction.html',
            controller: "ItemsForAuction.Controller",
            title: 'Items For Auction',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 210);
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
        .controller("ItemsForAuction.Controller", itemsForAuctionController);
})();