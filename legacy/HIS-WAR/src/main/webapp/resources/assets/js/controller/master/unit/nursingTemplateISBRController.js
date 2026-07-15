(function () {
    "use strict";
    /**
     * @Author By  Akshay Narale
     */
    function NursingTemplateISBR($scope, $rootScope, $http, $state, commonDetailService,GenericServiceParamHeader, StatusService, rejectReasonService, $cookies, CONSTANTS, GenericService, growl, PagerService,BillingGenericService,GenericServiceFileUpload) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;


        $scope.unitId = cookieObject.unitId;
        $scope.organizationId = cookieObject.organizationId;
        $scope.updateBtnFlag=true;

            $scope.reasonHandover=[];
            $scope.anaesthesiaPlanned =[];
            $scope.getISBRJson = function(){
                $http.get('resources/assets/js/nabhJSON/ISBR-NursingTemplate.json')
                   .then(function (response) {
                       $scope.reasonHandover = response.data[0].ISBRList;
                       $scope.anaesthesiaPlanned = response.data[1].ISBRList;
                       $rootScope.stopLoader();
                   })
                   .catch(function (error) {
                       $rootScope.stopLoader();
                       console.error('Error fetching JSON:', error);
                   });
            }
            $scope.getISBRJson();

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
        $stateProvider.state("nursingtemplate", {
            url: "/nursingTemplate",
            title: 'ISBR Nursing Template',
            templateUrl: 'views/master/UnitMaster/organizationMaster/nursing-template-ISBR.html',
            controller: "NursingTemplateISBR.Controller",
            controllerAs: "vm"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("NursingTemplateISBR.Controller", NursingTemplateISBR);
})();