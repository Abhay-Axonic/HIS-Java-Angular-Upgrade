(function () {
  "use strict";

  function detailsPOController($rootScope, $scope, $http, $cookies, CONSTANTS, $state, BillingGenericService, GenericService, commonDetailService, growl, CurrencyService) {
    var self = this;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
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

      GetPurchaseOrder(commonDetailService.getDataId());
    }

    CurrencyService.GetCurrency().then(function(currency){
      self.model.Currency = currency;
    });

    var GetPurchaseOrder = function (purchaseId) {
      commonDetailService.setDataId(null);
      $scope.grnIdForDoccumentUpload = purchaseId;
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER.SAVE_API + "/"+$scope.unitId+"/"+$scope.organizationId+"/"+purchaseId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.purchaseOrders = response.data;
          self.model.vendorId = response.data.vendorId;
          self.model.assetTypeId = response.data.assetTypeId;
          $rootScope.stopLoader();
          if (response.data.otherCharges) {
        	  self.model.otherCharges = response.data.otherCharges; // added for resolved HIS-9375
        	  //calculateOtherChargesAmountForItem(response.data);
            angular.forEach(self.model.otherCharges, function(d){
              self.model.purchaseOrders.totalOtherAmount = self.model.purchaseOrders.totalOtherAmount;
              // + d.amount
            });
          }

          if(response.data.discountTypeId == 2){
            self.model.purchaseOrders.discount = self.model.purchaseOrders.totalDiscount;
          }
          
          angular.forEach(self.model.purchaseOrders.items, function(item){
            var uomList = null;
            var uomListBonus = null;
            if (self.model.assetTypeId == 1) {
              uomList = item.uom.find(function(uom){
                return item.poUomTypeId == uom.uomTypeId;
              });
              uomListBonus = item.uom.find(function(uom){
                return item.bonusUomTypeId == uom.uomTypeId;
              });
              if(uomListBonus != undefined){
                item.bonusUomType = uomListBonus.uomDetails;
              }
              item.poUomType = uomList.uomDetails;
              if(item.isThreeUOMApplicable == false){
                item.uomListdata = item.uom;
                item.uom = [];
                angular.forEach(item.uomListdata, function(data){
                  if(data.uomType != 'Stock'){
                    item.uom.push(data);
                  }
                });
              }
            }

            item.prQuantity1 = item.prQuantity;
            CalculateStock(item, item.uom, uomList);
          });
          
          $rootScope.stopLoader();
        },
        function (err) {
          growl.error(err.data.message);
          $rootScope.stopLoader();
        });
    }

    var CalculateStock = function (item, uom, uomList) {
      if (!!uomList) {
    	  uomList.uomDetailsData = uomList.uomDetails.split(' ')[0];
    	  if(item.isThreeUOMApplicable == false){
    	        if(uomList.uomDetailsData == "P")
    	        {
    	            item.prQuantity1=item.prQuantity*(item.uomListdata[0].conversion*item.uomListdata[1].conversion*item.uomListdata[2].conversion);
    	        }
    	        if(uomList.uomDetailsData == "S")
    	        {
    	            item.prQuantity1=item.prQuantity*(item.uomListdata[1].conversion*item.uomListdata[2].conversion);
    	        }
    	        if(uomList.uomDetailsData == "D")
    	        {
    	            item.prQuantity1=item.prQuantity*(item.uomListdata[2].conversion);
    	        }
    	      }
    	      else{
    	    	if(uomList.uomDetailsData == "P")
    	        {
    	            item.prQuantity1=item.prQuantity*(item.uom[0].conversion*item.uom[1].conversion*item.uom[2].conversion);
    	        }
    	    	if(uomList.uomDetailsData == "S")
    	        {
    	            item.prQuantity1=item.prQuantity*(item.uom[1].conversion*item.uom[2].conversion);
    	        }
    	    	if(uomList.uomDetailsData == "D")
    	        {
    	            item.prQuantity1=item.prQuantity*(item.uom[2].conversion);
    	        }
    	      }
        var prQuantity = item.prQuantity;
        var prQuantity1 = item.prQuantity1;
        var maxOrderQuantity = item.maxOrderQuantity || 0;
        var maxStoreQuantity = item.maxStoreQuantity || 0; 
        if(item.poQuantity == undefined || item.poQuantity == null || item.poQuantity == ""){
          item.poQuantity = 0;
        }
        if(item.bonusQuantity == undefined || item.bonusQuantity == null || item.bonusQuantity == ""){
          item.bonusQuantity = 0;
        }
        if(item.isThreeUOMApplicable == false){
          if (uomList.uomDetailsData == "P") {
            item.newPRQuantity = parseInt(prQuantity * (item.uomListdata[2].conversion));
            item.prQuantity = parseInt(prQuantity1 / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.receivedQtyCon=(item.poQuantity +"*"+ item.poUom[0].conversion);
            item.receivedQtyCalculation = (item.poQuantity * item.poUom[0].conversion);
            item.uomUnitData = item.uomListdata[2].uomUnit;
//            item.receivedQtyBonusCon=(item.bonusQuantity +"*"+ item.bonusUom[0].conversion);
//            item.receivedQtyBonusCalculation = (item.bonusQuantity * item.bonusUom[0].conversion);
//            item.uomUnitDataBonus = item.uomListdata[2].uomUnit;
          } else if (uomList.uomDetailsData == "S") {
            item.newPRQuantity = parseInt(prQuantity * (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.prQuantity = parseInt(prQuantity1 / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
          } else if (uomList.uomDetailsData == "D") {
            item.prQuantity = parseInt(prQuantity1 / (item.uomListdata[2].conversion));
            item.newPRQuantity = parseInt(prQuantity * (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[2].conversion));
            item.receivedQtyCon=(item.poQuantity);
            item.receivedQtyCalculation = (item.poQuantity);
            item.uomUnitData = item.uomListdata[2].uomUnit;
//            item.receivedQtyBonusCon=(item.bonusQuantity);
//            item.receivedQtyBonusCalculation = (item.bonusQuantity);
//            item.uomUnitDataBonus = item.uomListdata[2].uomUnit;
          }
        }
        else{
          if (uomList.uomDetailsData == "P") {
            item.newPRQuantity = parseInt(prQuantity * (uom[2].conversion));
            item.prQuantity = parseInt(prQuantity1 / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.receivedQtyCon = (item.poQuantity +"*"+ item.poUom[0].conversion+"*"+ item.poUom[1].conversion);
            item.receivedQtyCalculation = (item.poQuantity * item.poUom[0].conversion * item.poUom[1].conversion);
            item.uomUnitData = uom[2].uomUnit;
//            item.receivedQtyBonusCon = (item.bonusQuantity +"*"+ item.bonusUom[0].conversion+"*"+ item.bonusUom[1].conversion);
//            item.receivedQtyBonusCalculation = (item.bonusQuantity * item.bonusUom[0].conversion * item.bonusUom[1].conversion);
//            item.uomUnitDataBonus = uom[2].uomUnit;
          } else if (uomList.uomDetailsData == "S") {
            item.newPRQuantity = parseInt(prQuantity * (uom[1].conversion * uom[2].conversion));
            item.prQuantity = parseInt(prQuantity1 / (uom[1].conversion * uom[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[1].conversion * uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[1].conversion * uom[2].conversion));
            item.receivedQtyCon=(item.poQuantity +"*"+ item.poUom[1].conversion);
            item.receivedQtyCalculation = (item.poQuantity * item.poUom[1].conversion);
            item.uomUnitData = uom[2].uomUnit;
//            item.receivedQtyBonusCon = (item.bonusQuantity +"*"+ item.bonusUom[1].conversion);
//            item.receivedQtyBonusCalculation = (item.bonusQuantity * item.bonusUom[1].conversion);
//            item.uomUnitDataBonus = uom[2].uomUnit;
          } else if (uomList.uomDetailsData == "D") {
            item.prQuantity = parseInt(prQuantity1 / (uom[2].conversion));
            item.newPRQuantity = parseInt(prQuantity * (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[2].conversion));
            item.receivedQtyCon=(item.poQuantity);
            item.receivedQtyCalculation = (item.poQuantity);
            item.uomUnitData = uom[2].uomUnit;
//            item.receivedQtyBonusCon=(item.bonusQuantity);
//            item.receivedQtyBonusCalculation = (item.bonusQuantity);
//            item.uomUnitDataBonus = uom[2].uomUnit;
          }
        }
        if(item.bonusQuantity!=undefined && item.bonusQuantity!=null)
        {
        if(item.bonusUomTypeId==1)
            {
            item.receivedQtyBonusCon = (item.bonusQuantity +"*"+ item.bonusUom[0].conversion+"*"+ item.bonusUom[1].conversion);
            item.receivedQtyBonusCalculation = (item.bonusQuantity * item.bonusUom[0].conversion * item.bonusUom[1].conversion);
            item.uomUnitDataBonus = uom[2].uomUnit;
            }
        else if(item.bonusUomTypeId==2)
            {
            item.receivedQtyBonusCon = (item.bonusQuantity +"*"+ item.bonusUom[1].conversion);
            item.receivedQtyBonusCalculation = (item.bonusQuantity * item.bonusUom[1].conversion);
            item.uomUnitDataBonus = uom[2].uomUnit;
            }
        
        }
      } else {
        item.newPRQuantity = item.prQuantity;
        item.newMaxOrderQuantity = item.maxOrderQuantity || 0;
        item.newMaxStoreQuantity = item.maxStoreQuantity || 0;
      }
    }

    var ViewOtherCharges = function (item){
      self.model.purchaseOrders.items.find(function(x){
        if (item.id == x.id) {
          self.model.otherCharges = x.otherCharges;
        }
        
        if (self.model.purchaseOrders.otherCharges) {
      	  self.model.otherCharges = self.model.purchaseOrders.otherCharges; // added for resolved HIS-9375
      	  //calculateOtherChargesAmountForItem(response.data);
        }
        
      });
    }
    
    var calculateOtherChargesAmountForItem = function (po) { // added for resolved HIS-9375
        angular.forEach(po.items, function(item){
          self.model.otherChargesPer = ((item.netAmount * 100) / po.netAmount); 
          self.model.calculateOtherCharges = (po.totalOtherAmount * (self.model.otherChargesPer / 100));
          item.otherChargesAmount = self.model.calculateOtherCharges;
          $timeout( function(){
            $scope.calculateRoundoff(val);
          },1000);
        })
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
      self.model.POUoms = [];
      self.model.purchaseOrders.items.find(function(x){
        if (item.id == x.id) {
          if(x.isThreeUOMApplicable == false){
            x.poUom.find(function(data){
              if(data.uomTypeId != 2){
                self.model.POUoms.push(data);
              }
            });
          }
          else{
            self.model.POUoms = x.poUom;
          }
        }
      });
    }

    var ViewBonusUom = function (item) {
      self.model.bonusUoms = [];
      self.model.purchaseOrders.items.find(function(x){
        if (item.id == x.id) {
          if(x.isThreeUOMApplicable == false){
            x.bonusUom.find(function(data){
              if(data.uomTypeId != 2){
                self.model.bonusUoms.push(data);
              }
            });
          }
          else{
            self.model.bonusUoms = x.bonusUom;
          }
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
              self.model.warrantyPeriod = x.warrantyPeriod;
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
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER.COP_HISTORY + item.itemId + "/" + self.model.vendorId + "/last/"+item.assetTypeId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.lastOrderDetails = response.data;
          angular.forEach(self.model.lastOrderDetails,function(item,key){
              item.discount=item.discount;
              item.netamount=item.netamount;
              item.otheramount=item.otheramount;
              item.taxamount=item.taxamount;
              item.totalamount=item.totalamount;
              item.totaldiscount=item.totaldiscount;
              });
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var NavigateToBack = function (){
      $state.go('POCreation');
    }

    $scope.getPatientDocumentList = function()
    {  
      $rootScope.startLoader();
      var data =
      {
        "patientId":0,
        "unitId":$scope.unitId,
        "organisationId":$scope.organizationId,
        "userId":undefined,
        "visitTypeId":undefined,
        "admissionId":undefined,
        "poId":$scope.grnIdForDoccumentUpload,
      }
      var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
      BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
        $scope.patientDocumentList = [];
        if (response.data.status == "success") 
        {
       	  $scope.patientDocumentList = response.data.listObject; 
       	  $('#uploadDetails').modal('show');
       	  angular.forEach($scope.patientDocumentList,function(value, key) {
       		  if(value.bucketFlag == 1){
       		  var filepath = value.documentPath
            var arr = filepath.split("/");
            var length=arr.length;
            var fileName1=arr[length-1];
            var arr1 = fileName1.split("_");
            var length1=arr1.length;
            var fileName=arr1[length1-1];
            value.fileName=fileName;
       	  }else{
       		 value.fileName=value.fileName;
       	  }
          });
          $rootScope.stopLoader();
        }
        else if(response.data.status == "error")
        {
       	  $rootScope.stopLoader();
        }
      });
    }

    initializeController();
  }
  
  function config($stateProvider) {
    $stateProvider.state("detailsPOCreation", {
      url: "/detailsPOCreation",
     templateUrl: 'views/procurement/quotation/details-PO-creation.html',
      controller: "DetailsPO.Controller",
      controllerAs: "vm",
      title: 'Purchase Order Creation'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("DetailsPO.Controller", detailsPOController);
})();