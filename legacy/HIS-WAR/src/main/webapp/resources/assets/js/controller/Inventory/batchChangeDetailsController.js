(function () {
  "use strict";

  function batchChangeDetailsController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, CurrencyService, commonDetailService, growl) {
    var self = this;

    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
      $rootScope.loginpage = true;
      self.model = {



      };
      CurrencyService.GetCurrency().then(function (currency) {
        self.model.Currency = currency;
      })
      self.model.fromBatchChange = fromBatchChange;
      fromBatchChange();
    }

    function fromBatchChange() {
      var id = commonDetailService.getDataId();
      var URI = CONSTANTS.INVENTORY.STORE.BATCH_CHANGE.DETAILS_API + id;
      var data = {};
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, data).then(
        function (response) {
          self.model.batchChangeDetail = response.data;

          angular.forEach(self.model.batchChangeDetail.batch, function (item) {
            item.oldMrp = item.oldMrp.toFixed(2);
            item.newMrp = item.newMrp.toFixed(2);
            item.uomList.find(function(d){
              if(d.uomType == item.uomType){
                item.uomType = d.uomDetails;
              }
              if(d.uomTypeId == item.mrpUomTypeId){
                item.mrpUomType = d.uomDetails;
              }
            });
          });
          $rootScope.stopLoader();
        });

    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailBatchChange", {
      url: "/detailBatchChange",
      templateUrl: 'views/PharmacyInventory/Store/details-batch-change.html',
      controller: "BatchChangeDetails.Controller",
      controllerAs: "vm",
      title: 'Batch/MRP/Expiry Date Change'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("BatchChangeDetails.Controller", batchChangeDetailsController);
})();