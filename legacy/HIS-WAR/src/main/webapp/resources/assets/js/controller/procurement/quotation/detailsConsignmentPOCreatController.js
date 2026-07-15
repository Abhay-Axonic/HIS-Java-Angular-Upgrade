(function () {
    "use strict";
  
    function detailsConsignmentPOCreatController($rootScope, $scope, $http, $cookies, CONSTANTS, $state, GenericService, commonDetailService, growl, CurrencyService) {
      var self = this;
  
      var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
      function initializeController() {
        $rootScope.loginpage = true;
  
        self.model = {
          otherCharges: [],
          stagedQuantity: [],
          POUoms: [],
          bonusUoms: [],
          prList: [],
          addedAccessoryItems: []
        };
        self.model.NavigateToBack = NavigateToBack;
        self.model.ViewOtherCharges = ViewOtherCharges;
        self.model.ViewStagedQuantity = ViewStagedQuantity;
        self.model.ViewPOUom = ViewPOUom;
        self.model.ViewBonusUom = ViewBonusUom;
        self.model.OpenPopup = OpenPopup;
        self.model.ViewPRList = ViewPRList;
        self.model.ViewTerms = ViewTerms;
        self.model.ViewItems = ViewItems;
        self.model.ViewHistory = ViewHistory;
        
        //start code by minesh
        self.model.ConsignmentStockPOHistory=ConsignmentStockPOHistory;
        GetImplantIndentDetails(commonDetailService.getDataId());
        //end code by minesh
      }
      
      CurrencyService.GetCurrency().then(function(currency){
        self.model.Currency = currency;
      });
  
      var ViewOtherCharges = function (item){
        self.model.purchaseOrders.items.find(function(x){
          if (item.id == x.id) {
            self.model.otherCharges = x.otherCharges;
          }
        });
      }
  
      var ViewPRList = function (item) {
        self.model.purchaseOrders.items.find(function(x){
          if (item.id == x.id) {
            self.model.prList = x.prId;
          }
        });
      }
  
      var ViewStagedQuantity = function (item) {
        self.model.purchaseOrders.items.find(function(x){
          if (item.id == x.id) {
            self.model.stagedQuantity = x.purchaseOrderStagedDto;
          }
        });
      }
  
      var ViewPOUom = function (item) {
        self.model.purchaseOrders.items.find(function(x){
          if (item.id == x.id) {
            self.model.POUoms = x.poUom;
          }
        });
      }
  
      var ViewBonusUom = function (item) {
        self.model.purchaseOrders.items.find(function(x){
          if (item.id == x.id) {
            self.model.bonusUoms = x.bonusUom;
          }
        });
      }
  
      var ViewItems = function (item) {
        self.model.purchaseOrders.items.find(function(x){
          if (item.id == x.id) {
            self.model.addedAccessoryItems = x.poAccessoriesMapper;
          }
        });
      }
  
      var ViewTerms = function (viewModal, item) {
        self.model.itemTerms = item;
        switch(viewModal){
          case "warrantyPeriodModal":
            self.model.purchaseOrders.items.find(function(x){
              if (item.itemId == x.itemId) {
                self.model.warrantyPeriod = x.warrentyPeriod;
              }
            });
          break;
          case "expectedSuportTermsModal":
            self.model.purchaseOrders.items.find(function(x){
              if (item.itemId == x.itemId) {
                self.model.supportTerms = x.supportTerms;
              }
            });
          break;
          case "expectedPaymentTermsModal":
            self.model.purchaseOrders.items.find(function(x){
              if (item.itemId == x.itemId) {
                self.model.paymentTerms = x.expectedPayment;
              }
            });
          break;
          case "expectedTechSpecificationModal":
            self.model.purchaseOrders.items.find(function(x){
              if (item.itemId == x.itemId) {
                self.model.technicalSpecs = x.technicalSpecification;
              }
            });
          break;
        }
      }
  
      var OpenPopup = function (action, modal) {
        self.model.action = action;
        var popup = angular.element('#' + modal);
        popup.modal('show');
      }
  
      var ViewHistory = function(item){
        var URI = CONSTANTS.GLOBAL.PO_HISTORY + $scope.unitId + '/' + item.itemId + '/' + item.poId;
  
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.lastOrderDetails = response.data;
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
          });
      }
      
      var ConsignmentStockPOHistory = function (item) {
    	  
    	  var URI = CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_PO_CREATION.HISTORY_API +"/"+item.grnDetailId + "/" + $scope.unitId;
    
          $rootScope.startLoaderPopup();
          GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
              self.model.consignmentPOHistory = response.data;
              $rootScope.stopLoaderPopup();
            },
            function (err) {
              $rootScope.stopLoaderPopup();
            });
        }
  
      var NavigateToBack = function (){
        $state.go('consignmentPoCreat');
      }
      
      //added by minesh
      var GetImplantIndentDetails = function (poId) {
         // var URI = CONSTANTS.PROCUREMENT.CONSIGNMENT.CONSIGNMENT_PO_CREATION.DETAILS_API + poId;
          var URI = CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_PO_CREATION.DETAILS_API+ $scope.unitId + '/'+$scope.organizationId + '/'+poId;
          $rootScope.startLoader();
          GenericService.serviceAction("GET", URI, {}).then(
              function (response) {
                  self.model.purchaseOrders = response.data;
                  self.model.purchaseOrders.totalOtherAmount=0;
                  angular.forEach(self.model.purchaseOrders.items, function(poItem){
                	  self.model.purchaseOrders.totalOtherAmount = poItem.totalOtherAmount;
                	  poItem.poId=self.model.purchaseOrders.id;
                  });
                  $rootScope.stopLoader();
              },
              function (err) {
                  $rootScope.stopLoader();
                  growl.error(err.data.message);
              });
      }
      //end code by minesh
  
      initializeController();
    }
    
    function config($stateProvider) {
      $stateProvider.state("detailsConsignmentPOCreat", {
        url: "/detailsConsignmentPOCreat",
       //templateUrl: 'views/procurement/quotation/details-PO-creation.html',
       templateUrl: 'views/procurement/quotation/details-consignment-po-creat.html',
        controller: "detailsConsignmentPOCreat.Controller",
        controllerAs: "vm",
        title: 'Consignment Purchase Order Creation'
      });
    }
  
    angular
      .module("myApp")
      .config(config)
      .controller("detailsConsignmentPOCreat.Controller", detailsConsignmentPOCreatController);
  })();