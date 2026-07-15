(function () {
  "use strict";

  function returnableGatepassDetailsController($scope, commonDetailService, $rootScope, $http, $cookies, CONSTANTS, GenericService, growl) {
    var self = this;

    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
      $rootScope.loginpage = true;
      self.model = {
              bonusBatchExpiryData:[],
              bonusSelectedItemBatchExpiryIndex:0,


      };
      self.model.returnableGatepassData = [];
      self.model.accessoryModal = accessoryModal;
      fromReturnableGatepass();
      self.model.bonusBatchExpiryInfoOnClick=bonusBatchExpiryInfoOnClick;
      self.model.bonusBatchExpiryModalOnClick=bonusBatchExpiryModalOnClick;
    }
    $scope.requestTypeToCheck;
    function fromReturnableGatepass() {
      var id = commonDetailService.getDataId();
      var URI = CONSTANTS.INVENTORY.STORE.RETURNABLE_GATEPASS.DETAIL_API  + $scope.unitId + '/'+$scope.organizationId + '/' + id;
      var data = {};
      GenericService.serviceAction("GET", URI, data).then(
        function (response) {
          self.model.returnableGatepassData = response.data;
          angular.forEach(self.model.returnableGatepassData.items, function(value){
        	 
        	  value.cop = value.cop.toFixed(2);
        	  
        	  if(value.uomTypeId ==1){
              	value.cop = (value.cop *( value.uomList[0].conversion * value.uomList[1].conversion));
              }
              else if(value.uomTypeId == 2){
              	value.cop = (value.cop *(value.uomList[1].conversion));
              }
            value.uomList.find(function(d){
              if(d.uomTypeId == value.uomTypeId){
                value.uomType = d.uomDetails;
              }
              if(value.bonusUomTypeId == d.uomTypeId){
                value.bonusUom = d.uomDetails;
              }
            });
        	  value.grnBonusBatchExpiryData=value.returnableBonusBatchExpiry;
        	  var bonusReturnQuantity=0;
        	  var bonusReturnedQuantity=0;
        	  angular.forEach(value.returnableBonusBatchExpiry, function(bonus){
        		  bonusReturnQuantity=bonusReturnQuantity+parseInt(bonus.returnQuantity);
        		  bonusReturnedQuantity=bonusReturnedQuantity+parseInt(bonus.returnedQuantity);
              bonus.bonusUom = value.bonusUom;
              bonus.bonusQuantityLease = bonus.bonusQuantity;
              if(value.bonusUomTypeId == 1){
                  bonus.bonusQuantity = (bonus.bonusQuantity / (value.uomList[0].conversion * value.uomList[1].conversion * value.uomList[2].conversion)); 
              }
              else if(value.bonusUomTypeId == 2){
                  bonus.bonusQuantity = (bonus.bonusQuantity / (value.uomList[1].conversion * value.uomList[2].conversion));
              }
              else{
                  bonus.bonusQuantity = (bonus.bonusQuantity / (value.uomList[2].conversion));
              }
              bonusReturnQuantity = parseInt(bonus.returnQuantity);
              if(bonus.returnedQuantity != undefined && bonus.returnedQuantity != null && bonus.returnedQuantity != 0){
                  if(value.bonusUomTypeId == 1){
                    bonus.returnedQuantity = (bonus.returnedQuantity / (value.uomList[0].conversion * value.uomList[1].conversion * value.uomList[2].conversion)); 
                    bonusReturnedQuantity = (bonusReturnedQuantity / (value.uomList[0].conversion * value.uomList[1].conversion * value.uomList[2].conversion)); 
                  }
                  else if(value.bonusUomTypeId == 2){
                    bonus.returnedQuantity = (bonus.returnedQuantity / (value.uomList[1].conversion * value.uomList[2].conversion));
                    bonusReturnedQuantity = (bonusReturnedQuantity / (value.uomList[1].conversion * value.uomList[2].conversion));
                  }
                  else{
                    bonus.returnedQuantity = (bonus.returnedQuantity / (value.uomList[2].conversion));
                    bonusReturnedQuantity = (bonusReturnedQuantity / (value.uomList[2].conversion));
                  }
                  if(bonusReturnQuantity==undefined || bonusReturnQuantity==null || bonusReturnQuantity == 0){
                      bonusReturnQuantity = parseInt(bonus.bonusQuantity) - bonusReturnedQuantity;
                      bonus.returnQuantity = parseInt(bonus.bonusQuantity) - bonusReturnedQuantity;
                  }
                  
                }
        	  })
        	  value.bonusReturnedQuantity=bonusReturnedQuantity!=undefined && bonusReturnedQuantity!=null ? bonusReturnedQuantity:0;
        	  value.bonusReturnQuantity=bonusReturnQuantity!=undefined && bonusReturnQuantity!=null ? bonusReturnQuantity:0;
            if(value.isThreeUOMApplicable == false){
              if(value.uomTypeId ==1){
                value.receivedQtyCon = (value.returnQuantity +"*"+ value.uomList[0].conversion);
                value.receivedQtyCalculation = (value.returnQuantity * value.uomList[0].conversion);
              }
              else if(value.uomTypeId == 2){
                  
              }
              else{
                value.receivedQtyCon = (value.returnQuantity);
                value.receivedQtyCalculation = (value.returnQuantity);
              }
            }
            else{
              if(value.uomTypeId ==1){
                value.receivedQtyCon = (value.returnQuantity +"*"+ value.uomList[0].conversion +"*"+ value.uomList[1].conversion);
                value.receivedQtyCalculation = (value.returnQuantity * value.uomList[0].conversion * value.uomList[1].conversion);
              }
              else if(value.uomTypeId == 2){
                value.receivedQtyCon = (value.returnQuantity +"*"+ value.uomList[1].conversion);
                value.receivedQtyCalculation = (value.returnQuantity * value.uomList[1].conversion);
              }
              else{
                value.receivedQtyCon = (value.returnQuantity);
                value.receivedQtyCalculation = (value.returnQuantity);
              }
            }
          })
          $scope.requestTypeToCheck=self.model.returnableGatepassData.requestedType;
          $scope.calculateRateConversion();
        });
    }
    $scope.closeBonusBatchExpiry =function()
    {
        self.model.bonusBatchExpiryData=[];
    }
    $scope.lessBonusQuantity = function (item) {
        var index = self.model.bonusBatchExpiryData.indexOf(item);
        self.model.bonusBatchExpiryData.splice(index, 1);
      }
    var bonusBatchExpiryInfoOnClick = function (batchItem, index) {
        self.model.bonusSelectedItemBatchExpiryIndex = index;
        var sumOfBonusReceivedQuantity=0;
        if(batchItem.grnBonusBatchExpiryData!=undefined && batchItem.grnBonusBatchExpiryData!=null && batchItem.grnBonusBatchExpiryData.length>0)
            {
        angular.forEach(batchItem.grnBonusBatchExpiryData, function (grnBonusBatchExpiryCount) {
            sumOfBonusReceivedQuantity=sumOfBonusReceivedQuantity+parseInt(grnBonusBatchExpiryCount.bonusQuantity);
            self.model.bonusBatchExpiryData.push({
                returnableBonusBatchExpiryId:grnBonusBatchExpiryCount.returnableBonusBatchExpiryId,
                batchNo: grnBonusBatchExpiryCount.batchNo,
                expiryDate: grnBonusBatchExpiryCount.expiryDate,
                batchId:grnBonusBatchExpiryCount.batchId,
                bonusQuantity: grnBonusBatchExpiryCount.bonusQuantity,
                bonusReturnedQuantity: grnBonusBatchExpiryCount.returnedQuantity,
                bonusReturnQuantity: grnBonusBatchExpiryCount.returnQuantity,
                bonusUom: grnBonusBatchExpiryCount.bonusUom
            });
          });
        batchItem.bonusReceivedQuantity=sumOfBonusReceivedQuantity;
            }
        else
            {
            sumOfBonusReceivedQuantity=0;
            self.model.bonusBatchExpiryData.push({
                returnableBonusBatchExpiryId:null,
                batchId:null,
                batchNo: "",
                expiryDate: "",
                bonusQuantity: "",
                bonusReturnedQuantity: "",
                bonusReturnQuantity: "",
            });
            }
        
    }
    var bonusBatchExpiryModalOnClick = function () {
        self.model.itemList[self.model.bonusSelectedItemBatchExpiryIndex].grnBonusBatchExpiryData = [];
        var modal_list = self.model.bonusBatchExpiryData;
        modal_list.forEach(function (modal, index) {
            self.model.itemList[self.model.bonusSelectedItemBatchExpiryIndex].grnBonusBatchExpiryData.push({
                returnableBonusBatchExpiryId:modal.returnableBonusBatchExpiryId!=null ? modal.returnableBonusBatchExpiryId :null ,
                batchNo: modal.batchNo,
                expiryDate: modal.expiryDate,
                bonusQuantity: modal.bonusQuantity,
                batchId:modal.batchId,
                returnedQuantity: modal.returnedQuantity,
                returnQuantity: modal.bonusReturnQuantity
                
            });
          });
//        var sumOfBonusReceivedQuantity=0;
//        if(self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].grnBonusBatchExpiryData!=undefined && self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].grnBonusBatchExpiryData!=null && self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].grnBonusBatchExpiryData.length>0)
//            {
//        angular.forEach(self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].grnBonusBatchExpiryData, function (grnBonusBatchExpiryCount) {
//            sumOfBonusReceivedQuantity=sumOfBonusReceivedQuantity+parseInt(grnBonusBatchExpiryCount.bonusQuantity);
//          });
//            }
//        self.model.grnList.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusReceivedQuantity=sumOfBonusReceivedQuantity;
        self.model.bonusBatchExpiryData=[];
        $('#bouns_selectBatchExpiry_modal').modal('hide');
    }
    $scope.closeBonusBatchExpiry =function()
    {
        self.model.bonusBatchExpiryData=[];
    }
    $scope.calculateRateConversion = function () {
      angular.forEach(self.model.returnableGatepassData.items, function(item){
        if (item.uomType == "Purchase") {
          item.cop = parseFloat(item.cop * (item.uomList[0].conversion * item.uomList[1].conversion * item.uomList[2].conversion)).toFixed(2);
        } else if (item.uomType == "Stock") {
          item.cop = parseFloat(item.cop * (item.uomList[1].conversion * item.uomList[2].conversion)).toFixed(2);
        } else if (item.uomType == "Dispense") {
          item.cop = parseFloat(item.cop * (item.uomList[2].conversion)).toFixed(2);
        }
      });
    }


    var accessoryModal = function (asset) {
      $("#viewDetailsAccessoriesModal").modal()
      self.model.accessoryList = [];
      self.model.accessoryList = asset.accessoriesResponse;
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("detailsReturnableGatepass", {
      url: "/detailsReturnableGatepass",
      templateUrl: 'views/PharmacyInventory/InPatient/details-returnable-gatepass.html',
      controller: "ReturnableGatepassDetails.Controller",
      controllerAs: "vm",
      title: 'Returnable GatePass Master'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("ReturnableGatepassDetails.Controller", returnableGatepassDetailsController);
})();
