
(function () {
    "use strict";

    function detailsImplantReturnConsignmentController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, $filter, $state, growl, detailService) {
        var self = this;
        var selectedItemId;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;

            self.model = {};

            //Methods
            self.model.GetPatientImplantReturnConsignmentDetails = GetPatientImplantReturnConsignmentDetails;
        

            GetPatientImplantReturnConsignmentDetails(detailService.getId());
        }

        var GetPatientImplantReturnConsignmentDetails = function (itemId) {
               
         var URI= CONSTANTS.PROCUREMENT.IMPLANT_INDENT_RETURN.DETAILS_API + itemId + '/' + $scope.unitId ;
          //  var URI = CONSTANTS.PHARMACY.NURSING.PATIENT_ISSUE_RETURN.MEDICATION_DETAIL_API + itemId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {

                    self.model.patientImplantReturnConsignmentnDetaillist = response.data;

                });
            $rootScope.stopLoader();
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("detailsImplantReturnConsignment", {
            url: "/detailsImplantReturnConsignment",
            templateUrl: 'views/master/TheatreManagement/details-consignment-patient-implant-return.html',
            controller: "DetailsImplantReturnConsignment.Controller",
            controllerAs: "vm"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("DetailsImplantReturnConsignment.Controller", detailsImplantReturnConsignmentController)
})();
