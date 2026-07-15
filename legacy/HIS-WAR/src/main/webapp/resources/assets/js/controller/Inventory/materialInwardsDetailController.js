(function () {
  "use strict";

  function materialInwardsDetailController($scope, $rootScope, $http, $state, commonDetailService,CurrencyService, $cookies, CONSTANTS, GenericService, growl) {
    var self = this;

    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
      $rootScope.loginpage = true;
      self.model = {



      };
      CurrencyService.GetCurrency().then(function (currency) {
        self.model.Currency = currency;
      })
      self.model.fromMaterialInward = fromMaterialInward;
      fromMaterialInward();
    }


    function fromMaterialInward() {
      var id = commonDetailService.getDataId();
      var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_INWARDS.DETAIL_API + $scope.unitId + '/'+$scope.organizationId + '/' + id;
      var data = {};
      GenericService.serviceAction("GET", URI, data).then(
        function (response) {
          self.model.inwardDetailData = response.data;
          self.model.inwardDetailData.items.forEach(function (item) {
            item.cop = item.cop.toFixed(2);
            item.taxPercentage = item.taxPercentage;
            item.totalAmount = item.totalAmount.toFixed(2);
            item.taxAmount = item.taxAmount.toFixed(2);
            item.netAmount = item.netAmount.toFixed(2);
            item.creditNetAmount = item.creditNetAmount;
            item.creditNoteAmount = item.creditNoteAmount;
            item.creditTaxAmount = item.creditTaxAmount;
            if(item.uom!=undefined && item.uom!=null && item.uom.length!=0)
            	{
            item.uom.find(function(u){
              if(u.uomType == item.uomType){
                item.uomType = u.uomDetails;
              }
            })
            if(item.isThreeUOMApplicable == false){
              item.uomListData = item.uom;
              item.uom = [];
              angular.forEach(item.uomListData, function(data){
                if(data.uomType != 'Stock'){
                  item.uom.push(data);
                }
              });
              if(item.uomTypeId == 1){
                item.receivedQtyCon = (item.receivedQuantity +"*"+ item.uomListData[0].conversion);
                item.receivedQtyCalculation = (item.receivedQuantity * item.uomListData[0].conversion);
                item.uomUnitData = item.uomListData[2].uomUnit;
              }
              else if(item.uomTypeId == 2){
                  
              }
              else{
                item.receivedQtyCon = (item.receivedQuantity);
                item.receivedQtyCalculation = (item.receivedQuantity);
                item.uomUnitData = item.uomListData[2].uomUnit;
              }
            }
            else{
              if(item.uomTypeId == 1){
                item.receivedQtyCon = (item.receivedQuantity +"*"+ item.uom[0].conversion +"*"+ item.uom[1].conversion);
                item.receivedQtyCalculation = (item.receivedQuantity * item.uom[0].conversion * item.uom[1].conversion);
                item.uomUnitData = item.uom[2].uomUnit;
              }
              else if(item.uomTypeId == 2){
                item.receivedQtyCon = (item.receivedQuantity +"*"+ item.uom[1].conversion);
                item.receivedQtyCalculation = (item.receivedQuantity * item.uom[1].conversion);
                item.uomUnitData = item.uom[2].uomUnit;
              }
              else{
                item.receivedQtyCon = (item.receivedQuantity);
                item.receivedQtyCalculation = (item.receivedQuantity);
                item.uomUnitData = item.uom[2].uomUnit;
              }
            }
          }
            if (item.netAmount == 0)
              item.isDisable = true;
          });

          self.model.inwardDetailData.totalAmount = self.model.inwardDetailData.totalAmount.toFixed(2);
          self.model.inwardDetailData.totalNetAmount = self.model.inwardDetailData.totalNetAmount.toFixed(2);
          self.model.inwardDetailData.totalTaxAmount = self.model.inwardDetailData.totalTaxAmount.toFixed(2);
        });
      }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailsMaterialInwards", {
      url: "/detailsMaterialInwards",
      templateUrl: 'views/PharmacyInventory/Store/details-material-inwords.html',
      controller: "MaterialInwardsDetail.Controller",
      controllerAs: "vm",
      title: 'Material Inwards'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("MaterialInwardsDetail.Controller", materialInwardsDetailController);
})();
