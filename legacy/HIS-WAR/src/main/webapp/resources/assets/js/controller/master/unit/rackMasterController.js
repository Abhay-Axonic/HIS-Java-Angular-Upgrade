(function () {
    "use strict";
    function rackMasterController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService,PaginationService, growl) {
        var self = this;
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
        function initializeController() {
            $rootScope.loginpage = true;
            self.model = {
                Stores: [{
                    "store": "Select Store"
                }],
                shelf_details: [
                    {
                        shelfId: undefined,
                        shelfCode: '',
                        shelfName: '',
                    }
                ],
                isShelfCodeValid: [],
                isShelfNameValid: [],
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
            //Methods 
            self.model.ToggleStatus = ToggleStatus;
            self.model.SaveRackMaster = SaveRackMaster;
            self.model.ClearFields = ClearFields;
            self.model.PopulateRackMaster = PopulateRackMaster;
            self.model.ValidateRack = ValidateRack;
            self.model.AddDuplicateRow = AddDuplicateRow;
            self.model.RemoveRowData = RemoveRowData;
            self.model.SaveShelfDetails = SaveShelfDetails;
            self.model.validShelf = validShelf;
            self.model.isOk = false;
            self.model.openPopup = openPopup;
            self.model.SetOldStore = SetOldStore;
            self.model.ChangeStore = ChangeStore;
            self.model.ConfirmStorePopup = ConfirmStorePopup;
            getRacks();
            PopulateStore();
        }

        var SaveRackMaster = function () {
            if (ValidateRack()) {
                if (!self.model.id) {
                    CreateRack();
                } else {
                    UpdateRack();
                }
            }
        }

        //Method to Populate Store Dropdown
        var PopulateStore = function () {
            var rackMaster = self.model;

            var URI = CONSTANTS.GLOBAL.USER_STORE_API + "/"+$scope.organizationId + "/"+$scope.unitId+ "/"+$scope.userId ;;

            rackMaster.store = rackMaster.Stores[0];

            rackMaster.Stores.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    rackMaster.Stores = rackMaster.Stores.concat(response.data);
                    $rootScope.stopLoader();
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                });
        }

        var GetEmptyData = function (index) {
            return {
                shelfId: undefined,
                shelfCode: '',
                shelfName: '',
            }
        }

        var AddDuplicateRow = function (index) {
            self.model.isOk = false;
            self.model.shelf_details.splice(index + 1, 0, GetEmptyData(index));
        }

        var RemoveRowData = function (index) {
            self.model.isOk = false;
            var shelf_details = self.model.shelf_details;
            self.model.shelf_details = shelf_details.slice(0, index).concat(shelf_details.slice(index + 1))
        }

        var SaveShelfDetails = function () {
            if ((find_duplicate_in_array(self.model.shelf_details)) && (validShelf())) {
                self.model.isOk = true;
                var shelfArray = [];
                angular.forEach(self.model.shelf_details, function (x) {
                    shelfArray.push({
                        "shelfCode": x.shelfCode,
                        "shelfName": x.shelfName,
                    })
                });
                self.model.shelfArray = shelfArray;
                var popup = angular.element("#addShelfDetails");
                popup.modal('hide');
            }
        }

        var validShelf = function () {
            var isValid = true;
            for (var i = 0; i < self.model.shelf_details.length; i++) {
                if (!self.model.shelf_details[i].shelfCode) {
                    self.model.isShelfCodeValid[i] = true;
                    isValid = false;
                }

                if (!self.model.shelf_details[i].shelfName) {
                    self.model.isShelfNameValid[i] = true;
                    isValid = false;
                }
                if (!self.model.shelf_details[i].shelfName) {
                    isValid = false
                    growl.error('Fill Shelf Name');
                }
                if (!self.model.shelf_details[i].shelfCode) {
                    isValid = false
                    growl.error('Fill Shelf Code');
                }
            }
            return isValid;
        }

        //Method to get the list of Rack
        var getRacks = function () {
            var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.RACK_MASTER.RACK_MASTER_API+ $scope.unitId + '/'+$scope.organizationId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                 
                    self.model.RackList = response.data;
                    if(!!response && !!response.data && response.data.length > 0){            
                        PaginationService.searchData(self.model.RackList, self.model.pagination);
                      }
                    $rootScope.stopLoader();
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                });
        }

        //Method to populate Rack  for editing
        function findInRecordInArray(array, key, value) {
            for (var i = 0; i < array.length; i++) {
                if (array[i][key] == value) {
                    return array[i];
                }
            }
            return null;
        }

        function getRackById(id) {
            return findInRecordInArray(self.model.RackList, 'id', id)
        }

        function PopulateRackMaster(id) {
            self.model.ispharmacyUnitCodeValid = false;
            self.model.ispharmacyDescriptionValid = false;
            self.model.activeRackId = id;
            var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.RACK_MASTER.RACK_MASTER_API + id;
            var data = {};
            GenericService.serviceAction("GET", URI, data).then(
                function (response) {
                    //   var rack = getRackById(id);
                    //    rack = response.data[0];
                    var store = self.model.Stores.find(function (stores) {
                        return response.data.storeId == stores.id;
                    });
                    self.model.rackCode = response.data.rackCode;
                    self.model.rackName = response.data.rackName;
                    self.model.shelf_details = response.data.items;
                    self.model.store = store;
                    self.model.isActive = response.data.status;
                    self.model.id = response.data.id;
                    self.model.isEdit = true;
                });
        };

        //Method to activate or deactivate Rack
        var ToggleStatus = function (rack) {
            var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.RACK_MASTER.RACK_MASTER_API + rack.id;

            var data = {
                "status": rack.status,
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId
            };
            if (rack.status == false) {
                ClearFields();
            }
            GenericService.serviceAction("PATCH", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    growl.success('Status changed successfully');
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                    rack.status = !rack.status;
                    growl.error('Something went wrong');
                });
        }

        var ValidateCode = function () {
            var isValid = true;

            self.model.RackList.forEach(function (element) {
                if (element.rackCode == self.model.rackCode) {
                    growl.error('Duplicate rack code');
                    isValid = false;
                }
            });
            if (!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.rackCode)) {
                growl.error('Code can only be alpha numeric with single hypen.');
                return;
            }
            if (self.model.isOk == false) {
                growl.error("Save Items in Shelf Details");
                return;
            }
            return isValid;
        }

        var ValidateCodeForUpdate = function () {
            var isValid = true;

            self.model.RackList.forEach(function (element) {
                if (element.id != self.model.activeRackId) {
                    if (element.rackCode == self.model.rackCode) {
                        growl.error('Duplicate rack code');
                        isValid = false;
                    }
                }
            });
            if (self.model.isOk == false) {
                growl.error("Save Items in Shelf Details");
                return;
            }
            return isValid;
        }

        function find_duplicate_in_array(shelf_details) {
            var isValid = true;
            var object = {};
            var result = [];

            shelf_details.forEach(function (item) {
                if (!object[item.shelfCode])
                    object[item.shelfCode] = 0;
                object[item.shelfCode] += 1;
                if (!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(item.shelfCode)) {
                    growl.error('Code can only be alpha numeric with single hypen.');
                    isValid = false;
                }
            })

            for (var prop in object) {
                if (object[prop] >= 2) {
                    result.push(prop);
                }
            }

            if (!!result.length) {
                isValid = false;
                growl.error("Duplicate Shelf code - " + result.toString());
            }
            // if (self.model.isOk == false) {
            //     growl.error("Save Items in Frequency Details");
            //     return;
            //   }
            return isValid;
        }

        //Method to add the Rack
        var CreateRack = function () {
            if (ValidateCode() && validShelf()) {
                var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.RACK_MASTER.RACK_MASTER_API;
                var data = {
                    "storeId": self.model.store.id,
                    "rackCode": self.model.rackCode,
                    "rackName": self.model.rackName,
                    "status": "true",
                    "items": self.model.shelfArray,
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId
                }
                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        var store = self.model.Stores.find(function (stores) {
                            return stores.id == self.model.store.id;
                        });
                        var rack = {
                            "id": response.data.id,
                            "storeName": store.store,
                            "rackCode": self.model.rackCode,
                            "rackName": self.model.rackName,
                            "status": true
                        };
                        self.model.RackList.push(rack);
                        PaginationService.searchData(self.model.RackList, self.model.pagination);
                        PaginationService.setLastPage(self.model.pagination);
                        self.model.shelf_details = [];
                        ClearFields();
                        $rootScope.stopLoader();
                        growl.success(response.data.message);
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoader();
                        growl.error('Something went wrong');
                    });
            }
        };

        //Method to update the Rack
        var UpdateRack = function () {
            if (ValidateCodeForUpdate()) {
                var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.RACK_MASTER.RACK_MASTER_API + self.model.id;
                var data = {
                    //"id": self.model.id,
                    "storeId": self.model.store.id,
                    // "storeName": self.model.store.store,
                    "rackCode": self.model.rackCode,
                    "rackName": self.model.rackName,
                    "items": self.model.shelf_details,
                    "status": true,
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId
                }
                // $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        var store = self.model.Stores.find(function (stores) {
                            return stores.id == self.model.store.id;
                        });
                        var rack = self.model.RackList.find(function (racks) {
                            return racks.id == self.model.id;
                        });
                        rack.storeName = store.store;
                        rack.rackCode = data.rackCode;
                        rack.rackName = data.rackName;

                        ClearFields();
                        $rootScope.stopLoader();
                        growl.success(response.data.message);
                        self.model.activeRackId = null;
                        self.model.isEdit = false;
                        self.model.shelf_details = [];

                    },
                    function errorCallback(response) {
                        $rootScope.stopLoader();
                        growl.error('Something went wrong');
                    });
            }
        };

        //Method to clear the fileds
        function ClearFields() {
            self.model.pagination.search = '';
            self.model.id = null;
            self.model.rackCode = "";
            self.model.rackName = "";
            self.model.isOk = false;
            self.model.store = self.model.Stores[0];
            self.model.shelf_details = [{
                shelfId: undefined,
                shelfCode: '',
                shelfName: '',
            }];
            self.model.search = '';
        }
        var clearSearch = function () {
            self.model.search = "";
            PaginationService.searchData(self.model.RackList, self.model.pagination);
        }
        var ValidateRack = function () {
            var isValid = true;
            if (self.model.shelf_details.length <= 0) {
                isValid = false;
            }
            else {
                if (!self.model.store.id) {
                    isValid = false;
                    self.model.isstoreValid = true;
                }
                if (!self.model.rackCode) {
                    isValid = false;
                    self.model.israckCodeValid = true;
                }
                if (!self.model.rackName) {
                    isValid = false;
                    self.model.israckNameValid = true;
                }
            }
            return isValid;
        }
        var openPopup = function () {
            if (!self.model.store.id) {
                growl.error('Please Select Store')
            }
            else if (!self.model.rackCode) {
                growl.error('Please Enter Rack Code')
            }
            else if (!self.model.rackName) {
                growl.error('Please Enter Rack Name')
            }
            else {
                $("#addShelfDetails").modal();
            }

        }


        var SetOldStore = function (store) {
            self.model.oldStore = store;
            self.model.store = self.model.oldStore;
        }

        var ChangeStore = function (store) {
            if (self.model.rackCode || self.model.rackName) {
                self.model.store = self.model.oldStore;
                self.model.storeNew = JSON.parse(JSON.stringify(store));
                $("#storeChange").modal()
            }
        }

        var ConfirmStorePopup = function () {
            self.model.store = self.model.storeNew;
            self.model.rackCode = '';
            self.model.rackName = ''; 
            ModalDismiss('storeChange');
        }
        var ModalDismiss = function (popupName) {
            var popup = angular.element('#' + popupName);
            popup.modal('hide');
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("rackMaster", {
            url: "/rackMaster",
            templateUrl: "views/PharmacyInventory/StockAdjustment/rack-master.html",
            controller: "RackMaster.Controller",
            title: 'Rack Master',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 630);
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
            controllerAs: "vm",
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("RackMaster.Controller", rackMasterController)
})();