

(function () {
    "use strict";

    function kitTypeMasterController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, growl) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;

            self.model = {};

            //Methods
            self.model.GetKitType = GetKitType;
            self.model.ToggleStatus = ToggleStatus;
            self.model.SaveKitType = SaveKitType;
            self.model.ClearFields = ClearFields;
            self.model.ValidateKitCode = ValidateKitCode;
            self.model.Popup = Popup;
            PopulateDropdown();
        //    GetKitTypeList();
        }

        var PopulateDropdown = function () {
            self.model.store = self.model.storeList[0];
            self.model.contractType = self.model.contractTypes[0];
            self.model.status=self.model.statuses[0];
            var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_REQUEST.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;
      
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
              function (response) {
                self.model.storeList = self.model.storeList.concat(response.data.store);
                self.model.contractTypes = self.model.contractTypes.concat(response.data.contractType);
                self.model.statuses=self.model.statuses.concat(response.data.status);
                $rootScope.stopLoader();
                GetWorkOrderRequests();
              },
              function (err) {
                $rootScope.stopLoader();
              });
          }
          
        //Method to get the list of Kit Type
        var GetKitTypeList = function () {
            var URI = CONSTANTS.MASTER.UNIT.KIT_TYPE_MASTER.GET_ALL;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.kitTypeList = response.data;
                    $rootScope.stopLoader();
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                });
        }

        //Method to populate the data
        var GetKitType = function (typeId) {

            var URI = CONSTANTS.MASTER.UNIT.KIT_TYPE_MASTER.DETAILS_API + typeId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {

                    self.model.kitTypeId = response.data.id;
                    self.model.kitTypeCode = response.data.code;
                    self.model.kitTypeName = response.data.description;
                });
            $rootScope.stopLoader();
        }

        //Method to activate or deactivate Kit Type
        var ToggleStatus = function (kitType) {
            var URI = CONSTANTS.MASTER.UNIT.KIT_TYPE_MASTER.UPDATE_API + kitType.id;

            var data = {
                "status": kitType.status,
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId
            };

            $rootScope.startLoader();
            GenericService.serviceAction("PATCH", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    growl.success('Status changed successfully');
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                    growl.error('Something went wrong');
                });
        }

        //Method for popup
        var Popup = function () {
            var popup = angular.element('#confirmSave');
            popup.modal('show');
        }

        var ValidateKitCode = function () {
            var isValid = true;

            self.model.kitTypeList.forEach(function (element) {
                if (element.code == self.model.kitTypeCode) {
                    growl.error('Duplicate Kit Type code');
                    isValid = false;
                }
            });
            if (!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.kitTypeCode)) {
                growl.error('Code can only be alpha numeric with single hyphen.');
                return;
            }
            return isValid;
        }

       

        //Method to add or update the Kit Type
        var SaveKitType = function () {
            if (Validate()) {
                if (!self.model.kitTypeId) {
                    CreateKitType();
                } else {
                    UpdateKitType();
                }
            }
        };

        //Method to add the Kit Type
        var CreateKitType = function () {
            if (ValidateKitCode()) {
                var URI = CONSTANTS.MASTER.UNIT.KIT_TYPE_MASTER.SAVE_API;

                var data = {
                    "code": self.model.kitTypeCode,
                    "description": self.model.kitTypeName,
                    "status": true,
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId
                }

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        var kitType = {
                            "id": response.data.id,
                            "code": self.model.kitTypeCode,
                            "description": self.model.kitTypeName,
                            "status": true
                        };
                        self.model.kitTypeList.push(kitType);
                        ClearFields();
                        //GetKitTypeList();
                        $rootScope.stopLoader();
                        growl.success(response.data.message);
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoader();
                        growl.error('Something went wrong');
                    });
            }
        };

        //Method to update the Kit Type
        var UpdateKitType = function () {
            if (ValidateKitCode()) {
                var URI = CONSTANTS.MASTER.UNIT.KIT_TYPE_MASTER.UPDATE_API + self.model.kitTypeId;

                var data = {
                    "code": self.model.kitTypeCode,
                    "description": self.model.kitTypeName,
                    "status": self.model.status,
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId
                }

                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        var kitType = self.model.kitTypeList.find(function (type) {
                            return type.id == self.model.kitTypeId;
                        });

                        kitType.code = data.code;
                        kitType.description = data.description;
                        self.model.kitTypeId = undefined;
                        ClearFields();
                      //  GetKitTypeList();
                        $rootScope.stopLoader();
                        growl.success(response.data.message);
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoader();
                        growl.error('Something went wrong');
                    });
            }
        }

        //Method to clear the fileds
        var ClearFields = function () {
            self.model.kitTypeCode = undefined;
            self.model.kitTypeName = undefined;
        }

        //Method to Validate the fileds
        var Validate = function () {
            var isValid = true;

            if (!self.model.kitTypeCode) {
                isValid = false;
                self.model.isKitTypeCodeValid = true;

            }
            if (!self.model.kitTypeName) {
                isValid = false;
                self.model.isKitTypeNameValid = true;
            }

            return isValid;
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("KitTypeMaster", {
            url: "/KitTypeMaster",
            templateUrl: 'views/master/UnitMaster/pharmacy/kit-type-master.html',
            controller: "KitTypeMaster.Controller",
            controllerAs: "vm"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("KitTypeMaster.Controller", kitTypeMasterController)
})();


