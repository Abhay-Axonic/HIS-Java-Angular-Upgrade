(function () {
    "use strict";

    function detailsConsignmentPOCreationController($scope, $rootScope,CurrencyService, $http, $filter, $state, screenDataService, $cookies, CONSTANTS, GenericService, commonDetailService, cancelReasonService, StatusService, growl) {
        var self = this;

        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;
            self.model = {
                fromDate: new Date(),
                toDate: new Date(),
                fromDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date()
                },
                toDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date()
                },
                storeList: [{
                    "store": "Select Store"
                }],
                statuses: [{
                    "status": "All"
                }]
            };
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            });
            GetImplantIndentDetails(commonDetailService.getDataId());
        }

        var GetImplantIndentDetails = function (poId) {
            var URI = CONSTANTS.PROCUREMENT.CONSIGNMENT.CONSIGNMENT_PO_CREATION.DETAILS_API + $scope.unitId + '/' + poId;
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.purchaseOrders = response.data;
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error(err.data.message);
                });
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("detailsConsignmentPOCreation", {
            url: "/detailsConsignmentPOCreation",
            templateUrl: 'views/procurement/consignment/consignment-details-PO-Creation.html',
            controller: "DetailsConsignmentPOCreation.Controller",
            controllerAs: "vm",
            title: 'Consignment PO Creation'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("DetailsConsignmentPOCreation.Controller", detailsConsignmentPOCreationController);
})();