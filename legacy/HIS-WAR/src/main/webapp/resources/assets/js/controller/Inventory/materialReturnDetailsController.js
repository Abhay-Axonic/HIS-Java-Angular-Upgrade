(function () {
  "use strict";

  function materialReturnDetailsController($scope, $rootScope, $http, $cookies, CONSTANTS, commonDetailService, GenericService, growl) {
    var self = this;

    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
      $rootScope.loginpage = true;
      self.model = {
        requestData: [],


      };
      self.model.fromMaterialReturn = fromMaterialReturn;
      fromMaterialReturn()
    }

    function fromMaterialReturn() {
      var id = commonDetailService.getDataId();
      var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_RETURN.DETAILS_API + $scope.unitId + '/' + id;
      var data = {};
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, data).then(
        function (response) {
          self.model.materialReturnDetail = response.data;
          angular.forEach(self.model.materialReturnDetail.items, function(val){
            val.uom.find(function (d) {
              if(d.uomTypeId == val.uomTypeId){
                val.uomType = d.uomDetails;
              }
            });
            if(val.isThreeUOMApplicable == false){
              if(val.returnQuantity == undefined || val.returnQuantity == null || val.returnQuantity == 0){
                if(val.uomTypeId == 1){
                  val.receivedQtyCon = (val.totalReturnedQty +"*"+ val.uom[0].conversion);
                  val.receivedQtyCalculation = (val.totalReturnedQty * val.uom[0].conversion);
                  val.uomUnitData = val.uom[2].uomUnit;
                }
                else if(val.uomTypeId == 2){
  
                }
                else{
                  val.receivedQtyCon = (val.totalReturnedQty);
                  val.receivedQtyCalculation = (val.totalReturnedQty);
                  val.uomUnitData = val.uom[2].uomUnit;
                }
              }
              else{
                if(val.uomTypeId == 1){
                  val.receivedQtyCon = (val.returnQuantity +"*"+ val.uom[0].conversion);
                  val.receivedQtyCalculation = (val.returnQuantity * val.uom[0].conversion);
                  val.uomUnitData = val.uom[2].uomUnit;
                }
                else if(val.uomTypeId == 2){
  
                }
                else{
                  val.receivedQtyCon = (val.returnQuantity);
                  val.receivedQtyCalculation = (val.returnQuantity);
                  val.uomUnitData = val.uom[2].uomUnit;
                }
              }
            }
            else{
              if(val.returnQuantity == undefined || val.returnQuantity == null || val.returnQuantity == 0){
                if(val.uomTypeId == 1){
                  val.receivedQtyCon = (val.totalReturnedQty +"*"+ val.uom[0].conversion +"*"+ val.uom[1].conversion);
                  val.receivedQtyCalculation = (val.totalReturnedQty * val.uom[0].conversion * val.uom[1].conversion);
                  val.uomUnitData = val.uom[2].uomUnit;
                }
                else if(val.uomTypeId == 2){
                  val.receivedQtyCon = (val.totalReturnedQty +"*"+ val.uom[1].conversion);
                  val.receivedQtyCalculation = (val.totalReturnedQty * val.uom[1].conversion);
                  val.uomUnitData = val.uom[2].uomUnit;
                }
                else{
                  val.receivedQtyCon = (val.totalReturnedQty);
                  val.receivedQtyCalculation = (val.totalReturnedQty);
                  val.uomUnitData = val.uom[2].uomUnit;
                }
              }
              else{
                if(val.uomTypeId == 1){
                  val.receivedQtyCon = (val.returnQuantity +"*"+ val.uom[0].conversion +"*"+ val.uom[1].conversion);
                  val.receivedQtyCalculation = (val.returnQuantity * val.uom[0].conversion * val.uom[1].conversion);
                  val.uomUnitData = val.uom[2].uomUnit;
                }
                else if(val.uomTypeId == 2){
                  val.receivedQtyCon = (val.returnQuantity +"*"+ val.uom[1].conversion);
                  val.receivedQtyCalculation = (val.returnQuantity * val.uom[1].conversion);
                  val.uomUnitData = val.uom[2].uomUnit;
                }
                else{
                  val.receivedQtyCon = (val.returnQuantity);
                  val.receivedQtyCalculation = (val.returnQuantity);
                  val.uomUnitData = val.uom[2].uomUnit;
                }
              }
            }
          })
          $rootScope.stopLoader();
        });
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailsMaterialReturn", {
      url: "/detailsMaterialReturn",
      templateUrl: 'views/PharmacyInventory/InPatient/details-material-return.html',
      controller: "MaterialReturnDetails.Controller",
      controllerAs: "vm",
      title: 'Material Return'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("MaterialReturnDetails.Controller", materialReturnDetailsController);
})();
