(function () {
  "use strict";

  function detailsLocalPRController($rootScope, $scope, $http, $cookies, CONSTANTS, $state, GenericService, commonDetailService, growl) {
    var self = this;

    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
      $rootScope.loginpage = true;

      self.model = {};
      self.model.NavigateToBack = NavigateToBack;
      self.model.ViewPOUom = ViewPOUom;
      GetPurchaseRequest(commonDetailService.getDataId());
    }

    var GetPurchaseRequest = function (purchaseId) {
      commonDetailService.setDataId(null);

      var URI = CONSTANTS.PROCUREMENT.PURCHASE.LOCAL_PURCHASE_REQUEST.SAVE_API + $scope.unitId + '/'+$scope.organizationId + '/' + purchaseId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.localPurchaseRequests = response.data;
          self.model.assetTypeId = response.data.assetTypeId;
          if (self.model.assetTypeId == 1) {
            angular.forEach(self.model.localPurchaseRequests.items, function(item){
              var uomList = item.uom.find(function(uom){
                return item.uomTypeId == uom.uomTypeId;
              });
              item.uomType = uomList.uomDetails;
              if(item.isThreeUOMApplicable == false){
                  item.uomListdata = item.uom;
                  item.uom = [];
                  angular.forEach(item.uomListdata, function(data){
                    if(data.uomType != 'Stock'){
                      item.uom.push(data);
                    }
                  });
                }
              item.uomDetails = uomList.uomDetails;
              CalculateStock(item, item.uom, uomList)
            });
          } else {
            angular.forEach(self.model.localPurchaseRequests.items, function(item){
              item.newQty = item.availableQuantity;
              item.newMaxOrderQuantity = item.maxOrderQuantity;
              item.newMaxStoreQuantity = item.maxStoreQuantity;
            });
          }
          
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
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
    	        if(item.isThreeUOMApplicable == false){
    	          item.receivedQtyCon=(item.quantity +"*"+ item.uomListdata[0].conversion);
    	          item.receivedQtyCalculation = (item.quantity * item.uomListdata[0].conversion);
                item.uomUnitData = item.uomListdata[2].uomUnit;
    	        }
    	        else{
    	          item.receivedQtyCon = (item.quantity +"*"+ item.uomListdata[0].conversion+"*"+ item.uomListdata[1].conversion);
    	          item.receivedQtyCalculation = (item.quantity * item.uomListdata[0].conversion * item.uomListdata[1].conversion);
                item.uomUnitData = item.uomListdata[2].uomUnit;
    	        }
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
      }else{
    	  if (uomList.uomDetails == "P") {
    	        item.newQty = parseInt(stockAvailable / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
    	        item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
    	        item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
    	        if(item.isThreeUOMApplicable == false){
    	          item.receivedQtyCon=(item.quantity +"*"+ uom[0].conversion);
    	          item.receivedQtyCalculation = (item.quantity * uom[0].conversion);
                item.uomUnitData = uom[2].uomUnit;
    	        }
    	        else{
    	          item.receivedQtyCon = (item.quantity +"*"+ uom[0].conversion+"*"+ uom[1].conversion);
    	          item.receivedQtyCalculation = (item.quantity * uom[0].conversion * uom[1].conversion);
                item.uomUnitData = uom[2].uomUnit;
    	        }
    	      } else if (uomList.uomDetails == "S") {
    	        item.newQty = parseInt(stockAvailable / (uom[1].conversion * uom[2].conversion));
    	        item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[1].conversion * uom[2].conversion));
    	        item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[1].conversion * uom[2].conversion));
    	        if(item.isThreeUOMApplicable != false){
    	          item.receivedQtyCon=(item.quantity +"*"+ uom[1].conversion);
    	          item.receivedQtyCalculation = (item.quantity * uom[1].conversion);
                item.uomUnitData = uom[2].uomUnit;
    	        }
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
      $state.go('localPurchhaseRequest');
    }
    var ViewPOUom = function (item) {
        let uomList = item.uom.filter(x => x.uomType != "Select UOM");
        self.model.POUoms = uomList;
      }
    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailslocalPurchhaseRequest", {
      url: "/detailslocalPurchhaseRequest",
      templateUrl: 'views/procurement/purchase/details-local-purchase-request.html',
      controller: "DetailsLocalPR.Controller",
      controllerAs: "vm",
      title: 'Local Purchase Request'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("DetailsLocalPR.Controller", detailsLocalPRController);
})();