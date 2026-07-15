
(function () {
    "use strict";

    function detailsPatientIssueReturnMedicationController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, $filter, $state, growl, detailService) {
        var self = this;
        var selectedItemId;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
        function initializeController() {
            $rootScope.loginpage = true;

            self.model = {};

            //Methods
            self.model.GetPatientIssueReturnMedicationDetails = GetPatientIssueReturnMedicationDetails;
        

            GetPatientIssueReturnMedicationDetails(detailService.getId());
        }

        var GetPatientIssueReturnMedicationDetails = function (itemId) {

            var URI = CONSTANTS.PHARMACY.NURSING.PATIENT_ISSUE_RETURN.MEDICATION_DETAIL_API + $scope.unitId + '/' + itemId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {

                    self.model.patientIssueReturnMedicationDetaillist = response.data;
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
		            	returnIssueDetail.issuedQuantity = (returnIssueDetail.issuedQuantity /(returnIssueDetail.uomList[0].conversion * returnIssueDetail.uomList[1].conversion));
                        returnIssueDetail.receivedQtyCon=(returnIssueDetail.returnedQuantity +"*"+ returnIssueDetail.uomList[0].conversion);
                        returnIssueDetail.receivedQtyCalculation=(returnIssueDetail.returnedQuantity * returnIssueDetail.uomList[0].conversion);
                        returnIssueDetail.uomUnitData = returnIssueDetail.uomList[2].uomUnit;
		            } 
                    else if (uomNew.uomDetailsData == "S") 
                    {
		            } 
                    else if (uomNew.uomDetailsData == "D") {
		        	    returnIssueDetail.receivedQtyCon=(returnIssueDetail.returnedQuantity);
                        returnIssueDetail.receivedQtyCalculation=(returnIssueDetail.returnedQuantity);
                        returnIssueDetail.uomUnitData = returnIssueDetail.uomList[2].uomUnit;
		            }
		        }
		        else{
		            if (uomNew.uomDetailsData == "P") {
		            	returnIssueDetail.issuedQuantity = (returnIssueDetail.issuedQuantity /(uomList[0].conversion * uomList[1].conversion));
		        	    returnIssueDetail.receivedQtyCon=(returnIssueDetail.returnedQuantity +"*"+ uomList[0].conversion+"*"+ uomList[1].conversion);
                        returnIssueDetail.receivedQtyCalculation=(returnIssueDetail.returnedQuantity * uomList[0].conversion * uomList[1].conversion);
                        returnIssueDetail.uomUnitData = uomList[2].uomUnit;
		            } else if (uomNew.uomDetailsData == "S") {
		            	returnIssueDetail.issuedQuantity = (returnIssueDetail.issuedQuantity /(uomList[1].conversion));
		        	    returnIssueDetail.receivedQtyCon=(returnIssueDetail.returnedQuantity +"*"+ uomList[1].conversion);
                        returnIssueDetail.receivedQtyCalculation=(returnIssueDetail.returnedQuantity * uomList[1].conversion);
                        returnIssueDetail.uomUnitData = uomList[2].uomUnit;
		            } else if (uomNew.uomDetailsData == "D") {
		            	returnIssueDetail.issuedQuantity = (returnIssueDetail.issuedQuantity /(returnIssueDetail.uomList[2].conversion));
		        	    returnIssueDetail.receivedQtyCon=(returnIssueDetail.returnedQuantity);
                        returnIssueDetail.receivedQtyCalculation=(returnIssueDetail.returnedQuantity);
                        returnIssueDetail.uomUnitData = uomList[2].uomUnit;
		            }
		        }
            }
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("detailsPatientIssueReturnMedication", {
            url: "/detailsPatientIssueReturnMedication",
            templateUrl: 'views/PharmacyInventory/InPatient/details-patient-issue-medication.html',
            controller: "DetailsPatientIssueReturnMedication.Controller",
            controllerAs: "vm",
            title: 'Patient Issue Return'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("DetailsPatientIssueReturnMedication.Controller", detailsPatientIssueReturnMedicationController)
})();
