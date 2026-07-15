(function () {
  "use strict";
  var CACHE = {};

  function storeIndentingDetailsController($scope,  $rootScope,$http, $cookies, CONSTANTS, GenericService, commonDetailService) {
    var self = this;
    var selectItemList = [];

    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
      self.model = {
      };

      fromStoreIndents();
    }

    function fromStoreIndents() {
      var id = commonDetailService.getDataId();
      var URI = CONSTANTS.INVENTORY.STORE.STORE_INDENT.DETAILS_API + $scope.unitId + '/' +$scope.organizationId + '/' + id;

      var data = {};
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, data).then(
        function (response) {
          self.model.indentDetail = response.data;
          angular.forEach(self.model.indentDetail.items, function(item){
            item.selectedUom = item.uom.find(function (uom) {
              return item.uomTypeId == uom.uomTypeId;
            });     
            item.uomType = item.selectedUom.uomDetails;
            if(item.isThreeUOMApplicable == false){
              item.uomListdata = item.uom;
              item.uom = [];
              angular.forEach(item.uomListdata, function(data){
                if(data.uomType != 'Stock'){
                  item.uom.push(data);
                }
              });
            }       

            var uomdata = item.selectedUom.uomDetails.split(' ')[0];
            if(item.isThreeUOMApplicable == false){
              if(uomdata == 'P'){
                item.receivedQtyCon=(item.quantity +"*"+ item.uomListdata[0].conversion);
                item.receivedQtyCalculation = (item.quantity * item.uomListdata[0].conversion);
                item.uomUnitData = item.uomListdata[2].uomUnit;
              }
              else if(uomdata == 'S'){
                
              }
              else{
                item.receivedQtyCon=(item.quantity);
                item.receivedQtyCalculation = (item.quantity);
                item.uomUnitData = item.uomListdata[2].uomUnit;
              }
            } 
            else{
              if(uomdata == 'P'){
                item.receivedQtyCon = (item.quantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
                item.receivedQtyCalculation = (item.quantity * item.uom[0].conversion * item.uom[1].conversion);
                item.uomUnitData = item.uom[2].uomUnit;
              }
              else if(uomdata == 'S'){
                item.receivedQtyCon=(item.quantity +"*"+ item.uom[1].conversion);
                item.receivedQtyCalculation = (item.quantity * item.uom[1].conversion);
                item.uomUnitData = item.uom[2].uomUnit;
              }
              else{
                item.receivedQtyCon=(item.quantity);
                item.receivedQtyCalculation = (item.quantity);
                item.uomUnitData = item.uom[2].uomUnit;
              }
            }
          });
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("DetailsStoreIndenting", {
      url: "/DetailsStoreIndenting",
      templateUrl: 'views/PharmacyInventory/InPatient/details-store-indenting.html',
      controller: "StoreIndentingDetails.Controller",
      controllerAs: "vm",
      title: 'Store Indent'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("StoreIndentingDetails.Controller", storeIndentingDetailsController);
})();