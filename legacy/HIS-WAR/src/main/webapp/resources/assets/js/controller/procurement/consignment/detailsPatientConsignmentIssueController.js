(function () {
    "use strict";

    function detailsPatientConsignmentIssueController($scope, $rootScope, $http, $state, $cookies, CONSTANTS, commonDetailService, GenericService, $filter, growl) {
        var self = this;
        var selectItemList = [];
        var masterId;
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;
            self.model = {

                requestData: [],


            };
            self.model.fromIssueList = fromIssueList;
            fromIssueList();
        }


        function fromIssueList() {

            var issueId = commonDetailService.getDataId();
            var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_ISSUE.MATERIAL_PENDING_ISSUE.NEW_DETAILS_API  +$scope.unitId+"/"+$scope.storeId+"/" + issueId;
            var data = {};
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, data).then(
                function (response) {
                    self.model.issueListDetail = response.data;
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error(err.message);
                });
        }


        initializeController();
    }



    function config($stateProvider) {
        $stateProvider.state("detailsPatientConsignmentIssue", {
            url: '/detailsPatientConsignmentIssue',
            templateUrl: 'views/procurement/consignment/details-patient-indent-issue-list.html',
            controller: "IssueListDetails.Controller",
            controllerAs: 'vm',
            title: 'Patient Consignment Issue'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("IssueListDetails.Controller", detailsPatientConsignmentIssueController)
})(); 
