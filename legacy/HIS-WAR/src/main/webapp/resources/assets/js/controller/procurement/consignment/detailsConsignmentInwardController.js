(function () {
    "use strict";

    function detailsConsignmentInwardController($rootScope, $scope, $http, $cookies, CONSTANTS, $state, GenericService, CurrencyService, commonDetailService, growl) {
        var self = this;
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
        function initializeController() {
            $rootScope.loginpage = true;

            self.model = {};
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })
            self.model.NavigateToBack = NavigateToBack;
            if (localStorage.getItem('inwardId')) {
                GetConsignInwardRequests(localStorage.getItem('inwardId'));
            } else {
                var inwardId = commonDetailService.getDataId();
                GetConsignInwardRequests(inwardId);
            }
        }

        var GetConsignInwardRequests = function (inwardId) {
            commonDetailService.setDataId(null);
            var URI = CONSTANTS.PROCUREMENT.CONSIGNMENT.CONSIGNMENT_INWARD.DETAIL_API + $scope.unitId + '/' + inwardId;
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.consignInwardRequestsDetails = response.data;
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error(err.data.message);
                });
        }

        var NavigateToBack = function () {
            $state.go('consignmentInward');
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("detailsConsignmentInward", {
            url: "/detailsConsignmentInward",
            templateUrl: 'views/procurement/consignment/details-consignment-inward.html',
            controller: "DetailsConsignmentInward.Controller",
            controllerAs: "vm",
            title: 'Consignment Inward'
        });

    }

    angular
        .module("myApp")
        .config(config)
        .controller("DetailsConsignmentInward.Controller", detailsConsignmentInwardController);
})();