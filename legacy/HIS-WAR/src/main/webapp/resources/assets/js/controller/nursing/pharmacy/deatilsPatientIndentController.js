(function () {
    "use strict";
    var CACHE = {};
  
    function deatilsPatientIndentController($scope, $state, $rootScope, $http, $filter, $cookies, CONSTANTS, StatusService, GenericService, storeIndentService,GenericServiceParamHeader, growl, commonDetailService) {
      var self = this;
      var mainStoreList = [];
  
      //Default Constructor
      var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
        $rootScope.loginpage = true;
        self.model = {};
  
        StatusService.GetStatus().then(function (status) {
          self.model.Status = status;
        });

        self.model.GetGenericMaster = GetGenericMaster;
        self.model.GetStrengthUnitList = GetStrengthUnitList;
        self.model.ViewCombinationDetails = ViewCombinationDetails;
        self.model.NavigteTo = NavigteTo;
        GetPatientIndentDetails(commonDetailService.getDataId());
        self.model.getAllPatientDiagnosisForIcon = getAllPatientDiagnosisForIcon;
  
      }
  
      var GetPatientIndentDetails = function (itemId) {
  
        var URI = CONSTANTS.PHARMACY.IP.PATIENT_INDENT.DETAIL_API + $scope.unitId + '/' + itemId;
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            $rootScope.startLoader();
            self.model.indentId = response.data.id;
            self.model.patientIndentDetails = response.data;
            self.model.ItemDetailList = response.data.items;
            angular.forEach(self.model.patientIndentDetails.items, function(item){
                var uomData = item.uom.find(function (obj) {
                  return obj.uomType == item.uomType;
                }); 
                var uomTypeId = uomData.uomTypeId;
                item.uom.find(function(d){
                    if(d.uomType == item.uomType){
                        item.uomType = d.uomDetails;
                    }
                });
                if(item.isThreeUOMApplicable == false){
                    if(uomTypeId == 1){
                        item.receivedQtyCon = (item.quantity +"*"+ item.uom[0].conversion);
                        item.receivedQtyCalculation = (item.quantity * item.uom[0].conversion);
                        item.uomUnitData = item.uom[2].uomUnit;
                    }
                    else if(uomTypeId == 2){
                        
                    }
                    else{
                        item.receivedQtyCon = (item.quantity);
                        item.receivedQtyCalculation = (item.quantity);
                        item.uomUnitData = item.uom[2].uomUnit;
                    }
                }
                else{
                    if(uomTypeId == 1){
                        item.receivedQtyCon = (item.quantity +"*"+ item.uom[0].conversion +"*"+ item.uom[1].conversion);
                        item.receivedQtyCalculation = (item.quantity * item.uom[0].conversion * item.uom[1].conversion);
                        item.uomUnitData = item.uom[2].uomUnit;
                    }
                    else if(uomTypeId == 2){
                        item.receivedQtyCon = (item.quantity +"*"+ item.uom[1].conversion);
                        item.receivedQtyCalculation = (item.quantity * item.uom[1].conversion);
                        item.uomUnitData = item.uom[2].uomUnit;
                    }
                    else{
                      item.receivedQtyCon = (item.quantity);
                      item.receivedQtyCalculation = (item.quantity);
                      item.uomUnitData = item.uom[2].uomUnit;
                    }
                }
            });
            $rootScope.allergydata(response.data);
            getAllPatientDiagnosisForIcon(response.data.patientId);
            $rootScope.stopLoader();
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
          });
      
      }

      

      var getAllPatientDiagnosisForIcon= function(patientId){
        self.model.patientDiagnosisListIcon =[];
        var cookieObject = $cookies.getObject('cookieObject');
         var headerObjDia = {
                     'unit_id' : cookieObject.unitId,
                    'organization_id' : cookieObject.organizationId,
                    'patient_id' : patientId,
                   'Content-Type':'application/json',
         };
         var data={};
         var URI = CONSTANTS.ROOT_URL
         + "/api/opd/getPatientDiagnosisByPatientIdForIcon";
         GenericServiceParamHeader
         .serviceAction(METHOD_GET, URI,
            data,
            NOTIFICATION_MSG_STATUS_FALSE,data,headerObjDia)
         .then(
                 function(response) {
                     console.log(response);
                     // $rootScope.startLoader();
                     if (response.data.status == "success")
                     self.model.patientDiagnosisListIcon = response.data.listObject; 
                     console.log("patientDiagnosisListIcon",self.model.patientDiagnosisListIcon);
                         self.model.comorbidityFlag = false;
                         self.model.notifiableFlag = false;
                        
                            if(self.model.patientDiagnosisListIcon.length > 0)
                                {
                                    self.model.complaintDelete=false;
                                    angular.forEach(self.model.patientDiagnosisListIcon,function(value,key) {
                                        if(value.isNotifiable == 'Y')
                                        {
                                            self.model.notifiableFlag = true;
                                        }
                                        else if(value.isComorbidity == 'Y')
                                        {
                                            self.model.comorbidityFlag = true;
                                        }
                                    });
                                }
                     
                 });
        }
      
      
          //Method to get the list of Generic 
    var GetGenericList = function () {

      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.GENERIC_MASTER.GENERIC_MASTER_API + $scope.organizationId;
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

    var NavigteTo = function () {
      var flag =true;
      $state.go('patientIndent',
      {
        obj : flag,
      });
    }
  
      initializeController();
    }

    function config($stateProvider) {
      $stateProvider.state("deatilsPatientIndent", {
        url: "/deatilsPatientIndent",
        templateUrl: 'views/PharmacyInventory/InPatient/details-patient-indent.html',
        controller: "DeatilsPatientIndent.Controller",
        controllerAs: "vm",
        title: 'Consumable Indent'
      });
    }
  
    angular
      .module("myApp")
      .config(config)
      .controller("DeatilsPatientIndent.Controller", deatilsPatientIndentController);
  })();
