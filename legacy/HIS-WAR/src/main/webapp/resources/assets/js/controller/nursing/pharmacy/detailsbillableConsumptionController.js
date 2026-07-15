(function () {
    "use strict";

    function detailsbillableConsumptionController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, $filter, $state, growl, CurrencyService, commonDetailService) {
        var self = this;
        var selectedItemId;
       
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
          $rootScope.loginpage = true;

          self.model = {};

          //Methods
          CurrencyService.GetCurrency().then(function (currency) {
            self.model.Currency = currency;
          })
           
          self.model.GetBillableConsumptionDetails = GetBillableConsumptionDetails;
          self.model.GetGenericMaster = GetGenericMaster;
          self.model.GetStrengthUnitList = GetStrengthUnitList;
          self.model.ViewCombinationDetails = ViewCombinationDetails;
          GetBillableConsumptionDetails(commonDetailService.getDataId());
        }


        var GetBillableConsumptionDetails = function (itemId) {
          var URI = CONSTANTS.PHARMACY.NURSING.BILLABLE_CONSUMPTION.DETAIL_API + $scope.unitId + '/' + itemId;

          $rootScope.startLoader();
          GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.billableConsumptionDetailslist = response.data;
            angular.forEach(self.model.billableConsumptionDetailslist.items, function(item){
              item.uomList.find(function(d){
                if(d.uomType == item.uom){
                  item.uom = d.uomDetails;
                }
              });
              var uomType = item.uom.split("-")[0];
              if(item.isThreeUOMApplicable == false){
                if(uomType == "P "){
                  item.receivedQtyCon = (item.consumedQuantity +"*"+ item.uomList[0].conversion);
                  item.receivedQtyCalculation = item.consumedQuantity * item.uomList[0].conversion;
                  item.uomUnitData = item.uomList[2].uomUnit;
                }
                else if(uomType == "S "){
                              
                }
                else{
                  item.receivedQtyCon = (item.consumedQuantity);
                  item.receivedQtyCalculation = (item.consumedQuantity);
                  item.uomUnitData = item.uomList[2].uomUnit;
                }
              }
              else{
                if(uomType == "P "){
                  item.receivedQtyCon = (item.consumedQuantity +"*"+ item.uomList[0].conversion +"*"+ item.uomList[1].conversion);
                  item.receivedQtyCalculation = (item.consumedQuantity * item.uomList[0].conversion * item.uomList[1].conversion);
                  item.uomUnitData = item.uomList[2].uomUnit;
                }
                else if(uomType == "S "){
                  item.receivedQtyCon = (item.consumedQuantity +"*"+ item.uomList[1].conversion);
                  item.receivedQtyCalculation = (item.consumedQuantity * item.uomList[1].conversion);
                  item.uomUnitData = item.uomList[2].uomUnit;
                }
                else{
                  item.receivedQtyCon = (item.consumedQuantity);
                  item.receivedQtyCalculation = (item.consumedQuantity);
                  item.uomUnitData = item.uomList[2].uomUnit;
                }
              }      	
            });
          });
          $rootScope.stopLoader();
        }

        //Method to get the list of Generic 
        var GetGenericList = function () {
          var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.GENERIC_MASTER.GENERIC_MASTER_API+ $scope.organizationId;
          $rootScope.startLoader();
          GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.genericList = response.data;
            GetGenericMaster();
            $rootScope.stopLoader();
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
          });
        }
        GetGenericList();
  
        var genericMasterList = [];
        var GetGenericMaster = function () {
          var arraylist = self.model.genericList.filter(x => x.status == true);
          for (let generic of arraylist){
            genericMasterList.push(generic);
          }       
          $rootScope.stopLoader();
        } 

        var GetStrengthUnitList = function () {
          var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY. ITEM_MASTER.UNIT_API + '/'+$scope.organizationId ;
          $rootScope.startLoader();
          GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.strengthUnitList = response.data;
            console.log("Strength Units : ",self.model.strengthUnitList);
            $rootScope.stopLoader();
          },
          function errorCallback(response) {
            console.log("Error");
            console.log("Strength Units : ",self.model.strengthUnitList);
            $rootScope.stopLoader();
          });
        }
        GetStrengthUnitList();
  
        var ViewCombinationDetails = function(item) {
    	    self.model.genericCombinationList = [];
          var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.GENERIC_MASTER.GENERIC_MASTER_IS_COMBINATION_DETAILS_API + item.genericId;
    
          $rootScope.startLoader();
          GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            for(let combination of response.data) {
              let combinationName = genericMasterList.filter(x => x.id == combination.subGenericId);
              combination.name = combinationName.length > 0 ? combinationName[0].name : '-';
              combination.code = item.itemCode;
              combination.genericName = item.itemName;
            }
            self.model.strengthUnitList.forEach(function (strength){
              response.data.forEach(function (data){
                if(data.strength == strength.id){
                  $scope.strength = strength.unit;
                  var obj = {
                    strength: "",
                    code:"",
                    genericId: "",
                    genericName: "",
                    id:"",
                    name:"",
                    strengthUnit:"",
                    subGenericId: "",	
                  }
                  obj.strength = $scope.strength;
                  obj.code = data.code;
                  obj.genericId = data.genericId;
                  obj.genericName = data.genericName;
                  obj.id = data.id;
                  obj.name = data.name;
                  obj.strengthUnit = data.strengthUnit;
                  obj.subGenericId = data.subGenericId;
                  self.model.genericCombinationList.push(obj);
                }
              });
            });
            var popup = angular.element("#combinationDetails");
            popup.modal("show");
            $rootScope.stopLoader();
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
          });
        }

        initializeController();
      }

      function config($stateProvider) {
        $stateProvider.state("detailsbillableConsumption", {
          url: "/detailsbillableConsumption",
          templateUrl:'views/PharmacyInventory/InPatient/details-billable-consumption.html',
          controller: "DetailsbillableConsumption.Controller",
          controllerAs: "vm",
          title: 'Billable Consumption'
        });
      }

      angular
        .module("myApp")
        .config(config)
        .controller("DetailsbillableConsumption.Controller", detailsbillableConsumptionController)
})();
