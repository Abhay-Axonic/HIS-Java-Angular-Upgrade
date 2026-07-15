(function () {
  "use strict";

  function detailsQuotationController($rootScope, $scope, $http, $cookies, CONSTANTS, $state, GenericService, commonDetailService, growl) {
    var self = this;

    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
      $rootScope.loginpage = true;

      self.model = {
        prDetailList: [],
        expectedAccessories: [],
        termsVisible: 1
      };
      self.model.NavigateToBack = NavigateToBack;
      self.model.ViewPRList = ViewPRList;
      self.model.ViewTerms = ViewTerms;
      self.model.ViewAccessories = ViewAccessories;

      GetQuotationEnquiry(commonDetailService.getDataId());
    }

    var GetQuotationEnquiry = function (quotationId) {
      commonDetailService.setDataId(null);

      var URI = CONSTANTS.PROCUREMENT.QUOTATION.QUOTATION_ENQUIRY.SAVE_API + quotationId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.requestTypeId = response.data.requestTypeId;
          self.model.assetTypeId = response.data.assetTypeId;
          self.model.quotationList = response.data;
          angular.forEach(self.model.quotationList.items, function(item){

            if (self.model.assetTypeId == 1) {
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
              CalculateStock(item, item.uom, uomList);
            } else {
              angular.forEach(self.model.quotationList.items, function(item){
                item.newMaxOrderQuantity = item.maxOrderQuantity;
                item.newMaxStoreQuantity = item.maxStoreQuantity;
              });
            }
          });
          $rootScope.stopLoader();
        },
        function (err) {
          growl.error(err.data.message);
          $rootScope.stopLoader();
        });
    }

    var ViewPRList = function (item) {
      self.model.quotationList.items.find(function(value){
        if (item.id == value.id) {
          self.model.prDetailList = value.prDetails;
        }
      });
    }

    var ViewTerms = function (item) {
      self.model.quotationList.items.find(function(value){
        if (item.id == value.id) {
          self.model.warrantyPeriod = value.warrantyPeriod;
          self.model.supportTerms = value.supportTerms;
          self.model.paymentTerms = value.paymentTerms;
          self.model.technicalSpecification = value.technicalSpecification;
        }
      });
    }

    var ViewAccessories = function (item) {
      self.model.quotationList.items.find(function(value){
        if (item.id == value.id) {
          self.model.expectedAccessories = value.expectedAccessories;
        }
      });
    }

    var CalculateStock = function (item, uom, uomList) {
      if (!!uomList) {
        var maxOrderQuantity = item.maxOrderQuantity || 0;
        var maxStoreQuantity = item.maxStoreQuantity || 0;

        uomList.uomDetailsData = uomList.uomDetails.split(' ')[0];
        if(item.isThreeUOMApplicable == false){
          if (uomList.uomDetailsData == "P") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.receivedQtyCon=(item.quantity +"*"+ item.uomListdata[0].conversion);
            item.receivedQtyCalculation = (item.quantity * item.uomListdata[0].conversion);
            item.uomUnitData = item.uomListdata[2].uomUnit;
          } else if (uomList.uomDetailsData == "S") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
          } else if (uomList.uomDetailsData == "D") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[2].conversion));
            item.receivedQtyCon=(item.quantity);
            item.receivedQtyCalculation = (item.quantity);
            item.uomUnitData = item.uomListdata[2].uomUnit;
          }
        }
        else{
          if (uomList.uomDetailsData == "P") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.receivedQtyCon = (item.quantity +"*"+ uom[0].conversion+"*"+ uom[1].conversion);
            item.receivedQtyCalculation = (item.quantity * uom[0].conversion * uom[1].conversion);
            item.uomUnitData = uom[2].uomUnit;
          } else if (uomList.uomDetailsData == "S") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[1].conversion * uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[1].conversion * uom[2].conversion));
            item.receivedQtyCon=(item.quantity +"*"+ uom[1].conversion);
            item.receivedQtyCalculation = (item.quantity * uom[1].conversion);
            item.uomUnitData = uom[2].uomUnit;
          } else if (uomList.uomDetailsData == "D") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[2].conversion));
            item.receivedQtyCon=(item.quantity);
            item.receivedQtyCalculation = (item.quantity);
            item.uomUnitData = uom[2].uomUnit;
          }
        }
      } else {
        item.newMaxOrderQuantity = 0;
        item.newMaxStoreQuantity = 0;
      }
    }

    var NavigateToBack = function (){
      $state.go('quotationEnquiry');
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("quotationEDetails", {
      url: "/quotationEDetails",
      templateUrl: 'views/procurement/quotation/quotation-enquiry-details.html',
      controller: "DetailsQuotation.Controller",
      controllerAs: "vm",
      title: 'Quotation Enquiry'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("DetailsQuotation.Controller", detailsQuotationController);
})();