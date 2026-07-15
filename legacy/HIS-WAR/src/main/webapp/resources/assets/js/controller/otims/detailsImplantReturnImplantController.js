

(function () {
    "use strict";

    function detailsImplantReturnImplantController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, $filter, $state, growl, detailService) {
        var self = this;
        var selectedItemId;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;

            self.model = {};

            //Methods
            self.model.GetPatientImplantReturnImplantDetails = GetPatientImplantReturnImplantDetails;
        

            GetPatientImplantReturnImplantDetails(detailService.getId());
        }

        var GetPatientImplantReturnImplantDetails = function (itemId) {
         
             var URI= CONSTANTS.PROCUREMENT.IMPLANT_INDENT_RETURN.DETAILS_API + itemId + '/' + $scope.unitId ;
              // var URI = CONSTANTS.PHARMACY.NURSING.PATIENT_ISSUE_RETURN.MEDICATION_DETAIL_API + itemId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {

                    self.model.patientImplantReturnImplantDetailList = response.data;

                });
            $rootScope.stopLoader();
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("detailsImplantReturnImplant", {
            url: "/detailsImplantReturnImplant",
            templateUrl: 'views/master/TheatreManagement/details-implant-patient-implant-return.html',
            controller: "DetailsImplantReturnImplantController.Controller",
            controllerAs: "vm"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("DetailsImplantReturnImplantController.Controller", detailsImplantReturnImplantController)
})();

