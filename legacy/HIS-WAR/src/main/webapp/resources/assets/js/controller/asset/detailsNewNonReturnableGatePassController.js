(function () {
    "use strict";

    function detailsNewNonReturnableGatePassController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, $filter, $state, growl, commonDetailService) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;

            self.model = {};

            //Methods

            self.model.GetDetailsNewNonReturnableGatePass = GetDetailsNewNonReturnableGatePass;
            self.model.ViewAccessoryDetails = ViewAccessoryDetails;
            self.model.NavigateToNonReturnableGatePass = NavigateToNonReturnableGatePass;
            GetDetailsNewNonReturnableGatePass(commonDetailService.getDataId());
        }

        var GetDetailsNewNonReturnableGatePass = function (itemId) {

            var URI = CONSTANTS.ASSET.NON_RETURNABLE_GATEPASS.DETAILS_API + itemId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {

                    self.model.detailsNewNonReturnableGatePassList = response.data;
                });
            $rootScope.stopLoader();
        }

        var ViewAccessoryDetails = function (item) {
            var popup = angular.element('#accessoryViewModal');
            popup.modal('show');
            self.model.detailsNewNonReturnableGatePassList.items.find(function (x) {
                if (x.itemId == item.itemId) {
                    self.model.itemForAccessoryDetail = item.accessorriesList;
                }
            });
        }

        var NavigateToNonReturnableGatePass = function () {
            $state.go('nonReturnableGatePass');
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("detailsNewNonReturnableGatePass", {
            url: "/detailsNewNonReturnableGatePass",
            templateUrl: 'views/Asset Management/details-non-returnable-gatepass.html',
            controller: "DetailsNewNonReturnableGatePass.Controller",
            controllerAs: "vm",
            title: 'Non Returnable Gate Pass'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("DetailsNewNonReturnableGatePass.Controller", detailsNewNonReturnableGatePassController)
})();