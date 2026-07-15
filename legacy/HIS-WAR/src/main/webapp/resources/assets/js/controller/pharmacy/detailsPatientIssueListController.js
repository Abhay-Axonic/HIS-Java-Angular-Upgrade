(function () {
    "use strict";

    function detailsPatientIssueListController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, $filter, $state, growl, CurrencyService, commonDetailService, StatusService, cancelReasonService) {
        var self = this;
        var itemId;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;

            self.model = {};

            //Methods
            StatusService.GetStatus().then(function (status) {
                self.model.Status = status;
            })
            self.model.batchOnChange = batchOnChange;
            self.model.RemoveRowData = RemoveRowData;
            self.model.GetGenericMaster = GetGenericMaster;
            self.model.GetStrengthUnitList = GetStrengthUnitList;
            self.model.ViewCombinationDetails = ViewCombinationDetails;
            GetDetailsPatientIssueList(commonDetailService.getDataId());

        }

        var GetDetailsPatientIssueList = function (itemId) {
          var URI = CONSTANTS.PHARMACY.IN_PATIENT_PHARMACY.PATIENT_CONSUMABLE_ISSUE.ISSUE_DETAIL_API + $scope.unitId + "/" + itemId;
          $rootScope.startLoader();
          GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
              self.model.patientIssueDetailList = response.data;
              self.model.patientIssueDetailList.items.forEach(function(item){
                  item.uomList.find(function(d){
                      if(d.uomType == item.uomType){
                          item.uomType = d.uomDetails;
                      }
                  });
                  if(item.uomTypeId!=undefined && item.uomTypeId!=null && item.uomTypeId ==1){
                	  item.availableStockQuantity=Math.floor((item.availableStockQuantity / (item.uomList[0].conversion * item.uomList[1].conversion * item.uomList[2].conversion)));
                  }
                  else if(item.uomTypeId!=undefined && item.uomTypeId!=null && item.uomTypeId ==2){
                	  item.availableStockQuantity = Math.floor((item.availableStockQuantity / (item.uomList[1].conversion * item.uomList[2].conversion)));
                  }
                  else
                	  {
                	  if(item.uomTypeId!=undefined && item.uomTypeId!=null){
                		  item.availableStockQuantity = Math.floor((item.availableStockQuantity / item.uomList[2].conversion));
                      }
                	  }
                  if(item.isThreeUOMApplicable == false){
                      if(item.uomTypeId ==1){
                          item.receivedQtyCon = (item.issueQuantity +"*"+ item.uomList[0].conversion);
                          item.receivedQtyCalculation = (item.issueQuantity * item.uomList[0].conversion);
                          item.uomUnitData = item.uomList[2].uomUnit;
                      }
                      else if(item.uomTypeId == 2){
                          
                      }
                      else{
                          item.receivedQtyCon = (item.issueQuantity);
                          item.receivedQtyCalculation = (item.issueQuantity);
                          item.uomUnitData = item.uomList[2].uomUnit;
                      }
                  }
                  else{
                      if(item.uomTypeId ==1){
                          item.receivedQtyCon = (item.issueQuantity +"*"+ item.uomList[0].conversion +"*"+ item.uomList[1].conversion);
                          item.receivedQtyCalculation = (item.issueQuantity * item.uomList[0].conversion * item.uomList[1].conversion);
                          item.uomUnitData = item.uomList[2].uomUnit;
                      }
                      else if(item.uomTypeId == 2){
                          item.receivedQtyCon = (item.issueQuantity +"*"+ item.uomList[1].conversion);
                          item.receivedQtyCalculation = (item.issueQuantity * item.uomList[1].conversion);
                          item.uomUnitData = item.uomList[2].uomUnit;
                      }
                      else{
                          item.receivedQtyCon = (item.issueQuantity);
                          item.receivedQtyCalculation = (item.issueQuantity);
                          item.uomUnitData = item.uomList[2].uomUnit;
                      }
                  }
              });
              $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
          });
        }

        var batchOnChange = function (selectedBatch, batch) {
            batch.expiry = selectedBatch.expiry;
            //batch.availableStockQuantity = selectedBatch.availableStockQuantity;
            // batch.uomList = selectedBatch.uom;

            // batch.displayQuantity = batch.availableStockQuantity / 
            // (batch.conversion ? batch.conversion : 1);
            var ipUom = selectedBatch.uom.find(function (obj) {
                return obj.ipUom == true;
            });
            batch.uomType = ipUom.uomType;
            batch.uomTypeId = ipUom.uomTypeId;
            batch.uomUnit = ipUom.uomUnit;
            batch.uomUnitId = ipUom.uomUnitId;
            batch.displayQuantity = selectedBatch.availableStockQuantity / ipUom.conversion;
        }

        var RemoveRowData = function (item) {
            var index = item.batches.indexOf(item);
            item.batches.splice(index, 1);
        }

        var NavigteToMaterialPending = function () {
            $state.go('patientConsumableIssue');
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
        $rootScope.startLoader();
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
              combination.code = item.code;
              combination.genericName = item.name;
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
        $stateProvider.state("detailsPatientIssueList", {
            url: '/detailsPatientIssueList',
            templateUrl: 'views/PharmacyInventory/InPatient/details-patient-issue-list.html',
            controller: "DetailsPatientIssueList.Controller",
            controllerAs: "vm",
            title: 'Patient Consumable Issue'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("DetailsPatientIssueList.Controller", detailsPatientIssueListController)
})();
