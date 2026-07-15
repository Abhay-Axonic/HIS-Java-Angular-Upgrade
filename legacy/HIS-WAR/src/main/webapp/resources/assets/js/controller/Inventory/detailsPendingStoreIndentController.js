(function () {
    "use strict";
    var CACHE = {};
    function detailsPendingStoreIndentController($scope, $rootScope, growl, $cookies, CONSTANTS, $location, $http, GenericService, commonDetailService) {
      var self = this;
      var selectItemList = [];

      //Default Constructor
      var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
        $rootScope.loginpage = true;
        self.model = {
        };
        GetStoreIndent();
      }

      var GetStoreIndent = function () { 

        var URI = CONSTANTS.INVENTORY.STORE.PENDING_STORE_INDENT.DETAILS_API + $scope.unitId + '/'+$scope.organizationId + '/' + commonDetailService.getDataId();

        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.pendingindent = response.data;
            angular.forEach(self.model.pendingindent.items, function(item){
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
                  item.receivedQtyCon=(item.pendingQuantity +"*"+ item.uomListdata[0].conversion);
                  item.receivedQtyCalculation = (item.pendingQuantity * item.uomListdata[0].conversion);
                  item.uomUnitData = item.uomListdata[2].uomUnit;
                }
                else if(uomdata == 'S'){
                  
                }
                else{
                  item.receivedQtyCon=(item.pendingQuantity);
                  item.receivedQtyCalculation = (item.pendingQuantity);
                  item.uomUnitData = item.uomListdata[2].uomUnit;
                }
              } 
              else{
                if(uomdata == 'P'){
                  item.receivedQtyCon=(item.pendingQuantity +"*"+ item.uom[0].conversion +"*"+ item.uom[1].conversion);
                  item.receivedQtyCalculation = (item.pendingQuantity * item.uom[0].conversion * item.uom[1].conversion);
                  item.uomUnitData = item.uom[2].uomUnit;
                }
                else if(uomdata == 'S'){
                  item.receivedQtyCon=(item.pendingQuantity +"*"+ item.uom[1].conversion);
                  item.receivedQtyCalculation = (item.pendingQuantity * item.uom[1].conversion);
                  item.uomUnitData = item.uom[2].uomUnit;
                }
                else{
                  item.receivedQtyCon=(item.pendingQuantity);
                  item.receivedQtyCalculation = (item.pendingQuantity);
                  item.uomUnitData = item.uom[2].uomUnit;
                }
              }
            });
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
            growl.error('No Data Found');
          });
      }
      
       
      initializeController();
    }

    function config($stateProvider) {
      $stateProvider.state("detailspendingStoreIndents", {
        url: "/detailspendingStoreIndents",
        templateUrl: 'views/PharmacyInventory/InPatient/details-pending-store-indents.html',
        controller: "DetailsPendingStoreIndent.Controller",
        controllerAs: "vm",
        title: 'Pending Store Indent'
      });
    }
    
    angular
      .module("myApp")
      .config(config)
      .controller("DetailsPendingStoreIndent.Controller", detailsPendingStoreIndentController);
  })();