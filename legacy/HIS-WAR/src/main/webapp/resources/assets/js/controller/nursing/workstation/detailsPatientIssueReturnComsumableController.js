

(function () {
    "use strict";

    function detailsPatientIssueReturnComsumableController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, $filter, $state, growl, detailService) {
        var self = this;
        var selectedItemId;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
        function initializeController() {
            $rootScope.loginpage = true;

            self.model = {};

            //Methods
            self.model.GetPatientIssueReturnConsumableDetails = GetPatientIssueReturnConsumableDetails;
        

            GetPatientIssueReturnConsumableDetails(detailService.getId());
        }

        var GetPatientIssueReturnConsumableDetails = function (itemId) {

            var URI = CONSTANTS.PHARMACY.NURSING.PATIENT_ISSUE_RETURN.MEDICATION_DETAIL_API + $scope.unitId + '/' + itemId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {

                    self.model.patientIssueReturnConsumableDetailList = response.data;
                    self.model.patientIssueReturnMedicationDetaillist.items.forEach(function (returnIssueDetail) {
                    	 var uomNew = returnIssueDetail.uomList.find(function(d){
                         	return returnIssueDetail.uom == d.uomType;
                     	 });
                     	 returnIssueDetail.uom = uomNew.uomDetails;
                     	CalculateStock(returnIssueDetail, returnIssueDetail.uomList,uomNew);
                    });
                });
            $rootScope.stopLoader();
        }
    	var CalculateStock = function (returnIssueDetail,uomList, uomNew) {
   		 if (!!uomNew) {
   			uomNew.uomDetailsData = uomNew.uomDetails.split(' ')[0];
		        if(returnIssueDetail.isThreeUOMApplicable == false){
		          if (uomNew.uomDetailsData == "P") {
		        	  returnIssueDetail.receivedQtyCon=(returnIssueDetail.uomList[0].conversion+"*"+ returnIssueDetail.uomList[2].conversion);
		          } else if (uomNew.uomDetailsData == "S") {
		          } else if (uomNew.uomDetailsData == "D") {
		        	  returnIssueDetail.receivedQtyCon=(returnIssueDetail.uomList[2].conversion);
		          }
		        }
		        else{
		          if (uomNew.uomDetailsData == "P") {
		        	  returnIssueDetail.receivedQtyCon=(uomList[0].conversion+"*"+ uomList[1].conversion +"*"+ uomList[2].conversion);
		          } else if (uomNew.uomDetailsData == "S") {
		        	  returnIssueDetail.receivedQtyCon=(uomList[1].conversion +"*"+ uomList[2].conversion);
		          } else if (uomNew.uomDetailsData == "D") {
		        	  returnIssueDetail.receivedQtyCon=(uomList[2].conversion);
		          }
		        }
   		 		}
		    }
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("detailsPatientIssueReturnComsumable", {
            url: "/detailsPatientIssueReturnComsumable",
            templateUrl: 'views/PharmacyInventory/InPatient/details-patient-issue-consumable.html',
            controller: "DetailsPatientIssueReturnComsumable.Controller",
            controllerAs: "vm",
            title: 'Patient Issue Return'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("DetailsPatientIssueReturnComsumable.Controller", detailsPatientIssueReturnComsumableController)
})();

