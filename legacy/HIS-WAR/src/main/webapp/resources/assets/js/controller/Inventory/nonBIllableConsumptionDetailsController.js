(function () {
    "use strict";
  
    function nonBIllableConsumptionDetailsController($scope, $rootScope, $http,commonDetailService, $cookies, CONSTANTS, GenericService, growl) {
      var self = this;
      
      //Default Constructor
      var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
      function initializeController() {
        $rootScope.loginpage = true;
        self.model = {
        };
  
        fromNonBillableConsumption()
      }


      function fromNonBillableConsumption() {
        var id = commonDetailService.getDataId();
        var URI = CONSTANTS.INVENTORY.NURSING.NON_BILLABLE_CONSUMPTION.DETAIL_API  + $scope.unitId + '/'+$scope.organizationId + '/' + id;
        var data = {};
        GenericService.serviceAction("GET", URI, data).then(
        function (response) { 
          self.model.consumptionDetail = response.data;
          angular.forEach(self.model.consumptionDetail.batch, function(item){
            var batch = item.batches.find(function(b){
              return b.batchId = item.batchId;
            });
            batch.uomResponses.find(function(d){
              if(d.uomTypeId == item.uomTypeId){
                item.uomType = d.uomDetails;
              }
            });
            if(item.isThreeUOMApplicable == false){
              if(item.uomTypeId ==1){
                item.receivedQtyCon = (item.consumedQuantity +"*"+ batch.uomResponses[0].conversion);
                item.receivedQtyCalculation = (item.consumedQuantity * batch.uomResponses[0].conversion);
                item.uomUnitData = batch.uomResponses[2].uomUnit;
              }
              else if(item.uomTypeId == 2){
                  
              }
              else{
                item.receivedQtyCon = (item.consumedQuantity);
                item.receivedQtyCalculation = (item.consumedQuantity);
                item.uomUnitData = batch.uomResponses[2].uomUnit;
              }
            }
            else{
              if(item.uomTypeId ==1){
                item.receivedQtyCon = (item.consumedQuantity +"*"+ batch.uomResponses[0].conversion +"*"+ batch.uomResponses[1].conversion);
                item.receivedQtyCalculation = (item.consumedQuantity * batch.uomResponses[0].conversion * batch.uomResponses[1].conversion);
                item.uomUnitData = batch.uomResponses[2].uomUnit;
              }
              else if(item.uomTypeId == 2){
                item.receivedQtyCon = (item.consumedQuantity +"*"+ batch.uomResponses[1].conversion);
                item.receivedQtyCalculation = (item.consumedQuantity * batch.uomResponses[1].conversion);
                item.uomUnitData = batch.uomResponses[2].uomUnit;
              }
              else{
                item.receivedQtyCon = (item.consumedQuantity);
                item.receivedQtyCalculation = (item.consumedQuantity);
                item.uomUnitData = batch.uomResponses[2].uomUnit;
              }
            }
          })
        });
      } 
  
      initializeController();
    }

  
    function config($stateProvider) {
      $stateProvider.state("detailsnonbillableConsumption", {
        url: "/detailsnonbillableConsumption",
        templateUrl: 'views/PharmacyInventory/InPatient/details-non-billable-consumption.html',
        controller: "NonBIllableConsumptionDetails.Controller",
        controllerAs: "vm",
        title: 'Department Consumption'
      });
    }
  
    angular
      .module("myApp")
      .config(config)
      .controller("NonBIllableConsumptionDetails.Controller", nonBIllableConsumptionDetailsController);
  })(); 