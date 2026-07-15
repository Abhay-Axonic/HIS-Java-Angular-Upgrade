(function () {
  "use strict";

  function detailsPurchaseRequestController($rootScope, $scope, $http, $cookies, CONSTANTS, $state, GenericService, PurchaseRequestService) {
    var self = this;

    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
      $rootScope.loginpage = true;

      self.model = {};
      self.model.NavigateToBack = NavigateToBack;
      self.model.ViewPOUom = ViewPOUom;
      GetPurchaseRequest(PurchaseRequestService.GetPurchaseId());
    }

    var GetPurchaseRequest = function (purchaseId) {
      PurchaseRequestService.SetPurchaseId(null);

      var URI = CONSTANTS.PROCUREMENT.PURCHASE.PURCHASE_REQUEST.SAVE_API + $scope.unitId + '/'+$scope.organizationId + '/' + purchaseId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.prTypeId = response.data.prTypeId;
          self.model.purchaseRequests = response.data;
          self.model.assetTypeId = response.data.assetTypeId;
          if (self.model.assetTypeId == 1) {
            angular.forEach(self.model.purchaseRequests.items, function(item){
              var uomList = item.uom.find(function(uom){
                return item.uomTypeId == uom.uomTypeId;
              });
              if(item.isThreeUOMApplicable == false){
                  item.uomListdata = item.uom;
                  item.uom = [];
                  angular.forEach(item.uomListdata, function(data){
                    if(data.uomType != 'Stock'){
                      item.uom.push(data);
                    }
                  });
                }
              $rootScope.stopLoader();
              item.uomDetails = uomList.uomDetails;
              CalculateStock(item, item.uom, uomList)
            });
          } else {
            angular.forEach(self.model.purchaseRequests.items, function(item){
            	if(item.isThreeUOMApplicable == false){
                    item.uomListdata = item.uom;
                    item.uom = [];
                    angular.forEach(item.uomListdata, function(data){
                      if(data.uomType != 'Stock'){
                        item.uom.push(data);
                      }
                    });
                  }
              item.newQty = item.availableQuantity;
              item.newMaxOrderQuantity = item.maxOrderQuantity;
              item.newMaxStoreQuantity = item.maxStoreQuantity;
            });
          }
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var CalculateStock = function (item, uom, uomList) {
      var stockAvailable = item.availableQuantity;
      var maxOrderQuantity = item.maxOrderQuantity || 0;
      var maxStoreQuantity = item.maxStoreQuantity || 0;

      uomList.uomDetails = uomList.uomDetails.split(' ')[0];
      if(item.isThreeUOMApplicable == false){
        if (uomList.uomDetails == "P") {
          item.newQty = parseInt(stockAvailable / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
          item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
          item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
          item.receivedQtyCon=(item.quantity +"*"+ item.uomListdata[0].conversion);
          item.receivedQtyCalculation = (item.quantity * item.uomListdata[0].conversion);
          item.uomUnitData = item.uomListdata[2].uomUnit;
        } else if (uomList.uomDetails == "S") {
          item.newQty = parseInt(stockAvailable / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
          item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
          item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
        } else if (uomList.uomDetails == "D") {
          item.newQty = parseInt(stockAvailable / (item.uomListdata[2].conversion));
          item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[2].conversion));
          item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[2].conversion));
          item.receivedQtyCon=(item.quantity);
          item.receivedQtyCalculation = (item.quantity);
          item.uomUnitData = item.uomListdata[2].uomUnit;
        }
      }
      else{
        if (uomList.uomDetails == "P") {
          item.newQty = parseInt(stockAvailable / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
          item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
          item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
          item.receivedQtyCon = (item.quantity +"*"+ uom[0].conversion+"*"+ uom[1].conversion);
          item.receivedQtyCalculation = (item.quantity * uom[0].conversion * uom[1].conversion);
          item.uomUnitData = uom[2].uomUnit;
        } else if (uomList.uomDetails == "S") {
          item.newQty = parseInt(stockAvailable / (uom[1].conversion * uom[2].conversion));
          item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[1].conversion * uom[2].conversion));
          item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[1].conversion * uom[2].conversion));
          item.receivedQtyCon=(item.quantity +"*"+ uom[1].conversion);
          item.receivedQtyCalculation = (item.quantity * uom[1].conversion);
          item.uomUnitData = uom[2].uomUnit;
        } else if (uomList.uomDetails == "D") {
          item.newQty = parseInt(stockAvailable / (uom[2].conversion));
          item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[2].conversion));
          item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[2].conversion));
          item.receivedQtyCon=(item.quantity);
          item.receivedQtyCalculation = (item.quantity);
          item.uomUnitData = uom[2].uomUnit;
        }
      }
    }

    var NavigateToBack = function (){
      $state.go('purchaseRequest');
    }

    var ViewPOUom = function (item) {
      let uomList = item.uom.filter(x => x.uomType != "Select UOM");
      self.model.POUoms = uomList;
    }

    initializeController();
  }
  
  function config($stateProvider) {
    $stateProvider.state("detailsPurchaseRequest", {
      url: "/detailsPurchaseRequest",
      templateUrl: 'views/procurement/purchase/details-purchase-request.html',
      controller: "DetailsPurchaseRequest.Controller",
      controllerAs: "vm",
      title: 'Purchase Request'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("DetailsPurchaseRequest.Controller", detailsPurchaseRequestController);
})();