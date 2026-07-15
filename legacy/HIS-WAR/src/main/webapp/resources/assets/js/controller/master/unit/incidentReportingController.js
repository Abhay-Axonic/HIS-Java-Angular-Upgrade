(function () {
    "use strict";
    /**
     * @Author By  Akshay Narale
     */
    function IncidentReporting($scope, $rootScope, $http, $state, commonDetailService,GenericServiceParamHeader, StatusService, rejectReasonService, $cookies, CONSTANTS, GenericService, growl, PagerService,BillingGenericService,GenericServiceFileUpload) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;


        $scope.unitId = cookieObject.unitId;
        $scope.organizationId = cookieObject.organizationId;
        $scope.updateBtnFlag=true;

            $scope.openDatePickerSearch = function() {
                $scope.datepicker5.opened = true;
            };

            $scope.datepicker5 = {
                opened : false
            };

}

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("incidentReportingScreen", {
            url: "/incidentReportingScreen",
            title: 'NABH Incident Reporting',
            templateUrl: 'views/master/UnitMaster/organizationMaster/incident-Reporting-screen.html',
            controller: "IncidentReporting.Controller",
            controllerAs: "vm"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("IncidentReporting.Controller", IncidentReporting);
})();