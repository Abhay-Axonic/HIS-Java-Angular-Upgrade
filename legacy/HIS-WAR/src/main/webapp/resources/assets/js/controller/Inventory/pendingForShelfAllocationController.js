(function () {
    "use strict";

    function pendingForShelfAllocationController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService,PaginationService, growl) {
        var self = this;
        var rackId;
        var shelfId;
        var ItemId;
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
                    "store": "Select Store"
                }],
                racks: [{
                    "rack": "Select Rack"
                }],
                shelfs: [{
                    "shelf": "Select Shelf"
                }],
                assetCategories: [{
                    "category": "Select Asset Category"
                }],
                productCategories: [{
                    "category": "Select Product Category"
                }],
                itemList: [],
                allocationData: []

            };
            self.model.PopulateStoreDropdown = PopulateStoreDropdown;
            self.model.GetAssetCategoryList = GetAssetCategoryList;
            self.model.GetProductCategoryList = GetProductCategoryList;
            self.model.ActiveTab = 1;
            self.model.PendingShelfAllocationSearch = PendingShelfAllocationSearch;
            self.model.UpdateStatus = UpdateStatus;
            self.model.PopulateRackDropdown = PopulateRackDropdown;
            self.model.PopulateShelfDropdown = PopulateShelfDropdown;
            self.model.allocatePopup = allocatePopup;
            self.model.addButtonClick = addButtonClick;
            self.model.onViewClick = onViewClick;
            self.model.activeIndex = null;
            self.model.saveNewPendingShelf = saveNewPendingShelf;
            self.model.clearFields = clearFields;
            self.model.removeRowData = removeRowData;
            self.model.ValidateDashboard = ValidateDashboard;
            self.model.cleardropdown = cleardropdown;


            PopulateStoreDropdown();
            GetAssetCategoryList();
        }
        var cleardropdown = function () {
            self.model.shelfs = '';
        }


        var PopulateStoreDropdown = function () {
            var allocation = self.model;

            var URI = CONSTANTS.GLOBAL.USER_STORE_API + "/"+$scope.organizationId + "/"+$scope.unitId+ "/"+$scope.userId ;
            allocation.store = allocation.stores[0];

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    allocation.stores = allocation.stores.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var PopulateRackDropdown = function (item) {
            var allocation = self.model;

            var URI = CONSTANTS.GLOBAL.RACK_API + item.storeId + "/" + $scope.organizationId;
            allocation.rack = allocation.racks[0];
            allocation.shelf = allocation.shelfs[0];
            allocation.racks.splice(1);
            allocation.shelfs.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    allocation.racks = allocation.racks.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var PopulateShelfDropdown = function () {
            var allocation = self.model;

            var URI = CONSTANTS.GLOBAL.SHELF_API + self.model.rack.id;
            allocation.shelf = allocation.shelfs[0];
            allocation.shelfs.splice(1);
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    allocation.shelfs = allocation.shelfs.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var GetAssetCategoryList = function () {
            var allocation = self.model;

            var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + 1 + "/"+$scope.unitId + "/"+$scope.organizationId ;

            allocation.assetCategory = allocation.assetCategories[0];
            allocation.productCategory = allocation.productCategories[0];
            allocation.assetCategories.splice(1);
            allocation.productCategories.splice(1);


            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    allocation.assetCategories = allocation.assetCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var GetProductCategoryList = function (id) {
            var allocation = self.model;
            var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + id + "/"+$scope.organizationId ;
            allocation.productCategory = allocation.productCategories[0];
            allocation.productCategories.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    allocation.productCategories = allocation.productCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }
        var ValidateDashboard = function () {
            var isValid = true;
            if (!self.model.store.id) {
                isValid = false;
                growl.warning('Please Select Store');
            }

            return isValid;
        }
        var PendingShelfAllocationSearch = function () {
            if (ValidateDashboard()) {
                var URI = CONSTANTS.INVENTORY.STORE.PENDING_SHELF_ALLOCATION.SEARCH_API;
                var data = {
                    "allocated": (self.model.ActiveTab == 1 ? true : false),
                    "storeId": self.model.store.id,
                    "batchNo": self.model.batchNo,
                    "itemCode": self.model.itemCode,
                    "assetCategoryId": self.model.assetCategory.id,
                    "productCategoryId": self.model.productCategory.id,
                    "itemName": self.model.itemName,
                    "unitId":$scope.unitId

                };
                $rootScope.startLoader()
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();

                        if (self.model.ActiveTab == 1) {
                            self.model.pagination.data=[];
                            self.model.allocationData = response.data;
                            if(!!response && !!response.data && response.data.length > 0){            
                                PaginationService.searchData(self.model.allocationData, self.model.pagination);
                              }
                        }
                        else {
                            self.model.pagination.data=[];
                            self.model.pendingShelfAllocationData = response.data;
                            if(!!response && !!response.data && response.data.length > 0){            
                                PaginationService.searchData(self.model.pendingShelfAllocationData, self.model.pagination);
                              }
                        }
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoader();
                        growl.error('Error');
                    });
            }

        }

        var UpdateStatus = function (item) {
            var URI = CONSTANTS.INVENTORY.STORE.PENDING_SHELF_ALLOCATION.STATUS_UPDATE_API + item.id;
            var data = {
                "status": item.status,
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId

            };

            $rootScope.startLoader();
            GenericService.serviceAction("PATCH", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    growl.success(response.data.message);
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error('Something Went Wrong');
                });
        }

        var allocatePopup = function (item) {
            $("#editPendingAllocatedItemModal").modal();
            clearFields();
            PopulateRackDropdown(item);
            self.model.storeId = item.storeId;
            self.model.batchId = item.batchId;
            self.model.ItemId = item.id;
            // Item = item;
            self.model.edit = false;
            if (!!item.id) {
                self.model.itemList = [];
                self.model.edit = true;
                var data = {
                    "rack": item.rack,
                    "shelf": item.shelf,
                    "rackId": item.rackId,
                    "shelfId": item.shelfId,
                    "id": item.id

                }
                self.model.itemList.splice(0);
                self.model.itemList.push(data);
            }
        }
        var addButtonClick = function () {
            var data = {
                "rack": self.model.rack.rack,
                "shelf": self.model.shelf.shelf,
                "rackId": self.model.rack.id,
                "shelfId": self.model.shelf.id

            }
            var isFound = false;
            self.model.itemList.find(function (x) {
                if (x.rackId == data.rackId && x.shelfId == data.shelfId) {
                    isFound = true;
                }
                else
                    isFound = false;
            });
            if (!isFound) {
                self.model.itemList.push(data);
                self.model.rack = self.model.racks[0];
                self.model.shelf = self.model.shelfs[0];
                self.model.shelfs.splice(1);
            }
            else {
                growl.error('Item already present in same rack and shelf');
            }

        }

        function onViewClick(index, item) {


            var URI = CONSTANTS.INVENTORY.STORE.PENDING_SHELF_ALLOCATION.ALLOCATED_ITEMS + $scope.unitId + "/" + self.model.storeId + "/" + item.rackId + "/ " + item.shelfId;

            var data = {};
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, data).then(
                function (response) {
                    self.model.itemDetailList = response.data;
                    $rootScope.stopLoader();
                });
            self.model.activeIndex = (self.model.activeIndex == index ? null : index);
        }

        var saveNewPendingShelf = function () {
            if (!self.model.edit) {
                saveNewShelf();
            }
            else {
                updateShelf();
            }

        }

        var saveNewShelf = function () {

            var URI = CONSTANTS.INVENTORY.STORE.PENDING_SHELF_ALLOCATION.ALLOCATED_ITEMS;

            var data = {
                "storeId": self.model.storeId,
                "batchId": self.model.batchId,
                "shelf": [],
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId
            };

            angular.forEach(self.model.itemList, function (item) {
                var storeItem = {
                    "rackId": item.rackId,
                    "shelfId": item.shelfId,
                }
                data.shelf.push(storeItem);
            });

            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    clearFields();
                    $rootScope.stopLoader();
                    growl.success(response.data.message);
                    PendingShelfAllocationSearch();
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error('Something Went Wrong');
                });
        }

        var updateShelf = function () {

            var URI = CONSTANTS.INVENTORY.STORE.PENDING_SHELF_ALLOCATION.UPDATE_API + self.model.ItemId;
            var data = {
                "storeId": self.model.store.id,
                "shelf": [],
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId

            };
            angular.forEach(self.model.itemList, function (item) {
                var storeItem = {
                    "id": item.id !== undefined && item.id !== null ? item.id : self.model.ItemId,
                    "rackId": item.rackId,
                    "shelfId": item.shelfId,
                }
                // if (self.model.edit == true) {
                //     storeItem.rackId = self.model.rack.id,
                //         storeItem.shelfId = self.model.shelf.id

                // }
                data.shelf.push(storeItem);
            });
            $rootScope.startLoader();
            GenericService.serviceAction("PATCH", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    growl.success(response.data.message);
                    PendingShelfAllocationSearch();
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error('Something Went Wrong');
                });
        }

        var clearFields = function () {
            self.model.rack = [];
            self.model.shelf = [];
            self.model.itemList = [];
            self.model.itemDetailList = [];
        }
        function removeRowData(item) {
            var index = self.model.itemList.indexOf(item);
            self.model.itemList.splice(index, 1);
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("pendingForShelfAllocation", {
            url: "/pendingForShelfAllocation",
            templateUrl: 'views/PharmacyInventory/Store/pending-for-shelf-allocation.html',
            controller: "PendingForShelfAllocation.Controller",
            title: 'Pending For Shelf Allocation',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 136);
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
        .controller("PendingForShelfAllocation.Controller", pendingForShelfAllocationController);
})();

