(function () {
    "use strict";
  
    function drugRecallDetailsController($scope, $rootScope, commonDetailService, $http, $cookies, CONSTANTS, GenericService, growl) {
      var self = this;
      
      //Default Constructor
      var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
        $rootScope.loginpage = true;
        self.model = {
         
  
         
      };
      self.model.fromDrugRecall = fromDrugRecall
      fromDrugRecall();
    }

    function fromDrugRecall() { 
      var id = commonDetailService.getDataId();
      var URI = CONSTANTS.INVENTORY.STORE.DRUG_RECALL.DETAIL_API + id;
      var data = {};
      GenericService.serviceAction("GET", URI, data).then(
          function (response) { 
              self.model.drugRecallDetail = response.data; 
          });
  }

    
      initializeController();
    }
  
    function config($stateProvider) {
      $stateProvider.state("detailsDrugRecall", {
        url: "/detailsDrugRecall",
        templateUrl: 'views/PharmacyInventory/Store/details-drug-recall.html',
        controller: "DrugRecallDetails.Controller",
        controllerAs: "vm",
        title: 'Drug Recall'
      });
    }
  
    angular
      .module("myApp")
      .config(config)
      .controller("DrugRecallDetails.Controller", drugRecallDetailsController);
  })();
 