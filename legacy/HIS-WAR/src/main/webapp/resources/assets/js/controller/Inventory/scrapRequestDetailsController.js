(function () {
    "use strict";
  
    function scrapRequestDetailsController($scope, $rootScope, $http,commonDetailService,CurrencyService, $cookies, CONSTANTS, GenericService, growl) {
      var self = this;
      
      //Default Constructor
      var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
        $rootScope.loginpage = true;
        self.model = {
         
  
         
      };
  
  self.model.fromScrapRequest = fromScrapRequest;
  fromScrapRequest();
    }
    CurrencyService.GetCurrency().then(function (currency) {
      self.model.Currency = currency;
    })
    function fromScrapRequest() {
        var id = commonDetailService.getDataId();
        var URI = CONSTANTS.INVENTORY.SCRAP.SCRAP_REQUEST.DETAIL_API + id;
        var data = {};
        GenericService.serviceAction("GET", URI, data).then(
          function (response) {
            self.model.scrapDetailData = response.data;
            self.model.remark= self.model.scrapDetailData.remark;
            self.model.scrapDetailData.items.forEach(function (item) {
              item.isDisabled = true;
              item.amountCop = item.amountCop.toFixed(2);
              item.cop = item.cop.toFixed(2);
              item.uomList.find(function(u){
                if(u.uomType == item.uomType){
                  item.uomType = u.uomDetails;
                }
              })
              if(item.isThreeUOMApplicable == false){
                if(item.uomTypeId == 1){
                  item.receivedQtyCon = (item.scrapQuantity +"*"+ item.uomList[0].conversion);
                  item.receivedQtyCalculation = (item.scrapQuantity * item.uomList[0].conversion);
                  item.uomUnitData = item.uomList[2].uomUnit;
                }
                else if(item.uomTypeId == 2){
                    
                }
                else{
                  item.receivedQtyCon = (item.scrapQuantity);
                  item.receivedQtyCalculation = (item.scrapQuantity);
                  item.uomUnitData = item.uomList[2].uomUnit;
                }
              }
              else{
                if(item.uomTypeId == 1){
                  item.receivedQtyCon = (item.scrapQuantity +"*"+ item.uomList[0].conversion +"*"+ item.uomList[1].conversion);
                  item.receivedQtyCalculation = (item.scrapQuantity * item.uomList[0].conversion * item.uomList[1].conversion);
                  item.uomUnitData = item.uomList[2].uomUnit;
                }
                else if(item.uomTypeId == 2){
                  item.receivedQtyCon = (item.scrapQuantity +"*"+ item.uomList[1].conversion);
                  item.receivedQtyCalculation = (item.scrapQuantity * item.uomList[1].conversion);
                  item.uomUnitData = item.uomList[2].uomUnit;
                }
                else{
                  item.receivedQtyCon = (item.scrapQuantity);
                  item.receivedQtyCalculation = (item.scrapQuantity);
                  item.uomUnitData = item.uomList[2].uomUnit;
                }
              }
            });
          });
      }
    
      initializeController();
    }
  
    function config($stateProvider) {
      $stateProvider.state("detailsScrapApproval", {
        url: "/detailsScrapApproval",
        templateUrl: 'views/PharmacyInventory/Store/details-scrap-approval.html',
        controller: "ScrapRequestDetails.Controller",
        controllerAs: "vm",
        title: 'Scrap Request'
      });
    }
  
    angular
      .module("myApp")
      .config(config)
      .controller("ScrapRequestDetails.Controller", scrapRequestDetailsController);
  })();