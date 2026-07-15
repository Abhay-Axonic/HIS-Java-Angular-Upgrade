(function () {
    "use strict";
  
    function detailsConsignmentStockConversionController($scope, $rootScope, $http, $filter, $state, $cookies, CONSTANTS, GenericService, commonDetailService, cancelReasonService, StatusService, growl) {
      var self = this;
  
      //Default Constructor
      var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
      function initializeController() {
        $rootScope.loginpage = true;
  
        self.model = {
          selectAllRows: false,
          selectedItemId: 0,
          storageDetailsList: [],
          SelectedOrderIds: [],
          selectedItems: [],
          selectedStorageIndex: null,
          selectItemId: null,
          inspectId: null,
          storeList: [{
            "store": "Select Store"
          }],
          statuses: [{
            "status": "All"
          }],
          itemStorageDetails: []
        };
  
        cancelReasonService.GetReason().then(function (reason) {
          self.model.reasons = reason;
        });
  
        StatusService.GetStatus().then(function (status) {
          self.model.statusList = status;
        });
  
        //Methods
        self.model.ModalDismiss = ModalDismiss;
        self.model.NavigateToBack = NavigateToBack;
        self.model.SelectItem = SelectItem;
        self.model.SelectAllOrders = SelectAllOrders;
        self.model.SaveDetailsConsignmentStockConversion= SaveDetailsConsignmentStockConversion;
        self.model.GetConsignmentStockConversionData= GetConsignmentStockConversionData;
        self.model.ViewDetails = ViewDetails;
        
        GetConsignmentStockConversionData();
        
      }
      
      var GetConsignmentStockConversionData = function () {
          var detailsData = commonDetailService.getDataId();
          self.model.inspectId = detailsData.id;
          //var URI = CONSTANTS.PROCUREMENT.QUOTATION.PHYSICAL_INSPECTION.DETAILS_API + "detailsConsignmentStockConversion";
          var URI = CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_CONVERSION.STOCK_CONVERSION_DETAILS_API;
          var param = {
            "id": detailsData.id,
            "storeId": detailsData.storeId,
            "unitId": $scope.unitId
          }
          $rootScope.startLoader();
          GenericService.serviceAction("POST", URI, param).then(
            function (response) {
              self.model.physicalInspectionData = response.data;
              console.log(self.model.physicalInspectionData);
              self.model.remark = response.data.remark;
              self.model.requestItems = JSON.parse(JSON.stringify(response.data));
              self.model.physicalInspectionData.items.forEach(function (item) {
                item.isDisabled = true;
                item.storageDetails = [];
                item.approvedQuantity = ((((item.receivedQuantity- item.consumedQty) - item.returnedQuantity )- item.storeStockTransfered)-item.convertedConsignmentStock);        
              });
              self.model.rackList = response.data.racks;
              self.model.rejectReasons = self.model.reasons;
              // todo:
              self.model.storageDetailsList = [{ rackId: '', shelfId: '', remark: '' }]
              $rootScope.stopLoader();
            },
            function (err) {
              $rootScope.stopLoader();
              growl.error(err.data.message);
            });
        }
      
      
      var ViewDetails = function(storeId, batchId, batchNo, lotNo)
      {

          //var URI = CONSTANTS.PROCUREMENT.QUOTATION.PHYSICAL_INSPECTION.DETAILS_API + "detailsConsignmentStockConversion";
          var URI = CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_CONVERSION.STORE_STOCK_API;
          
          $rootScope.startLoader();
          GenericService.serviceAction("GET", URI+"/"+storeId+"/"+ batchId+"/"+batchNo+"/"+lotNo).then(
            function (response) {
              self.model.listData = response.data;
              $rootScope.stopLoader();
            },
            function (err) {
              $rootScope.stopLoader();
              growl.error(err.data.message);
            });
        
    	  
    	  
      }
      $scope.saveBtnDisabled=false;
      var SaveDetailsConsignmentStockConversion = function () {
          $scope.saveBtnDisabled=true;
          if (ValidateSelectedItems()) {
              var URI = CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_CONVERSION.SAVE_API;
              var data = {
            		  "assetType" : self.model.physicalInspectionData.assetType,
            		  "assetTypeId" : self.model.physicalInspectionData.assetTypeId,            		  
            		  "grnType" : self.model.physicalInspectionData.grnType,
            		  "grnTypeId" : self.model.physicalInspectionData.grnTypeId,
            		  "id" : self.model.physicalInspectionData.id,
            		  "items": [],
            		  "status": self.model.physicalInspectionData.status,
            		  "statusId": self.model.physicalInspectionData.statusId,
            		  "storeId" : self.model.physicalInspectionData.storeId,
            		  "storeName" : self.model.physicalInspectionData.storeName,             		  
            		  "vendor": self.model.physicalInspectionData.vendorId,
            		  "vendorId": self.model.physicalInspectionData.vendorId,
            		  "unitId":$scope.unitId,
                  "organizationId":$scope.organizationId,
                  "userId":$scope.userId,
                  "remarks":self.model.remark,
              }
              angular.forEach(self.model.selectedItems, function (item, key) {
            	  var item = {
            			  
            			  "id": item.id,
            			  "itemId":item.itemId,
            			  "itemCode":item.itemCode,
            			  "itemName":item.itemName,
            			  "batchNo":item.batchNo,
            			  "grnUomTypeId":item.grnUomTypeId,
            			  "grnUomType":item.grnUomType,
            			  "lotNo":item.lotNo,
            			  "statusId":item.statusId,
            			  "status":item.status,
            			  "totalConsignmentStock":item.totalConsignmentStock,
            			  "availableConsignmentStock":item.availableConsignmentStock,
            			  "convertedConsignmentStock":item.convertedConsignmentStock,
            			  "conversionQuantity":parseInt(item.conversionQuantity),
            			  "availableConvertedStock":item.availableConvertedStock,
            			  "batchId":item.batchId,
            			  "approvedQuantity":item.approvedQuantity,
                    "receivedQuantity": item.receivedQuantity
                      }
                  data.items.push(item);
                  var getLength=self.model.physicalInspectionData.items.length;
                  var count=0;
                  angular.forEach(self.model.physicalInspectionData.items, function (item) {
                	  var conversionQty=item.conversionQuantity!=undefined ? item.conversionQuantity:0;
                      if(item.currentQty-conversionQty==0)
                          {
                          count++;
                          }
                  });
//                  angular.forEach(self.model.selectedItems, function (item) {
//                	  var conversionQty=item.conversionQuantity!=undefined ? item.conversionQuantity:0;
//                      if(item.currentQty-conversionQty==0)
//                          {
//                          count++;
//                          }
//                  });
                  if(getLength==count)
                  {
                    data.statusId = 4;
                  }else
                      {
                      data.statusId = 18;
                      }
              });
                  
              $rootScope.startLoader();
              GenericService.serviceAction("POST", URI, data).then(
                  function (response) {
                    $rootScope.stopLoader();
                    var popup = angular.element('#convertPopup');
                    popup.modal('hide');
                    $('.modal-backdrop').remove();
                    $(document.body).removeClass("modal-open");
                    growl.success('stock converted successfully');
                    NavigateToBack();                      
                  },
                  function (err) {
                      $rootScope.stopLoader();
                      growl.error('Error while converting stock');
                  });
              }
      }
      
      var SelectItem = function (item) {
    	  
          var itemIds = self.model.SelectedOrderIds;
          if (item.isSelected) {
        	  item.isDisabled = false;
        	  itemIds.push(item.id);
        	  self.model.selectedItems.push(item);
        	  if (self.model.physicalInspectionData.items.every(isSelected)) {
        		  self.model.selectAllRows = true;
        		  } else {
        			  self.model.selectAllRows = false;
        			  }
        	  } else {
        		  item.isDisabled = true;
        		  var index = itemIds.indexOf(item.id);
        		  if (index > -1) {
        			  itemIds.splice(index, 1);
        			  var indexOfRemovItem = self.model.selectedItems.findIndex(function(element){
        				  return element.id===item.id;
        				  })
        				  if(indexOfRemovItem !== -1){
        					  self.model.selectedItems.splice(indexOfRemovItem, 1)
        					  }
        			  }
        		  if (self.model.physicalInspectionData.items.every(isSelected)) {
        			  self.model.selectAllRows = false;
        			  } else {
        				  self.model.selectAllRows = true;
        				  }
        		  }
          console.log(self.model.selectedItems);
          /*var selectedItems1 = self.model.selectedItems.items.filter(function(item){
        	  return item.isSelected === true;
        	  });*/
        }
     
      var SelectAllOrders = function () {
          if (self.model.selectAllRows) {
            self.model.SelectedOrderIds = [];
            self.model.physicalInspectionData.items.forEach(function (item) {
              if (isDisable(item) && item.availableConsignmentStock >= 0 ) {
            	  item.isSelected = true;
            	  item.isDisabled = false;
            	  self.model.SelectedOrderIds.push(item.id);
            	  self.model.selectedItems.push(item);
              }
            });
          } else {
            self.model.SelectedOrderIds = [];
            self.model.selectedItems = [];
            self.model.physicalInspectionData.items.forEach(function (item) {
              item.isSelected = false;
              item.isDisabled = true;
            });
          }
        }

      $scope.availableQty = function (item) {
        item.approvedQuantity = (((((item.receivedQuantity- item.consumedQty) - item.returnedQuantity )- item.storeStockTransfered)-item.convertedConsignmentStock)- item.conversionQuantity);
      }
      
      var ValidateSelectedItems = function () {
    	  
          let isValid = true;
          
          if (!self.model.selectedItems || self.model.selectedItems.length <= 0) {
            growl.error('Select Items to Convert');
            isValid = false;
          }
          self.model.selectedItems.forEach(function (item) {
        	  if( isNaN(parseInt(item.conversionQuantity)) ){
        		  isValid = false;
        		  growl.error("Error Please Enter valid conversionQuantity");
        	  }
        	  if(parseInt(item.conversionQuantity) <= 0){
        		  isValid = false;
        		  growl.error("Error Please Enter conversionQuantity greater than 0");
        	  }
        	  if(parseInt(item.conversionQuantity) > (((item.totalConsignmentStock - item.consumedQty) - item.returnedQuantity )- item.storeStockTransfered)-item.convertedConsignmentStock){
//        	  if (parseInt(item.conversionQuantity) > ((((item.receivedQuantity- item.consumedQty) - item.returnedQuantity )- item.storeStockTransfered)-item.convertedConsignmentStock)) {
            // (((item.totalConsignmentStock- item.consumedQty) - item.returnedQuantity )- item.storeStockTransfered)-item.convertedConsignmentStock) {
        		  growl.error("Conversion quantity should not greater than Available Consignment Stock");
        		  isValid = false;  
        	  }
        	  if (parseInt(item.conversionQuantity) > (((item.totalConsignmentStock- item.consumedQty) - item.returnedQuantity )- item.storeStockTransfered)-item.convertedConsignmentStock) {
        		  growl.error("Conversion quantity should not be greater than Available Conversion Stock");
        		  isValid = false;
        		  }
          });
           /*angular.forEach(self.model.physicalInspectionData.items, function (item, key) {    		  
    		  if(parseInt(item.conversionQuantity) > item.availableConsignmentStock){
    			  conversionQuantity = false;
    		  }    		  
    	  });*/
          
          ModalDismiss('convertPopup');
          if(!isValid)
          {
          $scope.saveBtnDisabled=false;
          }
          return isValid;
        }
      
      var isDisable = function (item) {
          if (item.status == "Closed") {
            return true;
          }
          return false;
        }

      var isSelected = function (item) {
          if (!isDisable(item))
            return item.isSelected;
          else
            return true;
        }
      
      var NavigateToBack = function () {
        $state.go('consignmentStockConversion');
      }

      var ModalDismiss = function (modal) {
        var popup = angular.element('#' + modal);
        popup.modal('hide');
      }
  
      initializeController();
    }
  
    function config($stateProvider) {
      $stateProvider.state("detailsConsignmentStockConversion", {
        url: "/detailsConsignmentStockConversion",
        templateUrl: 'views/procurement/quotation/details-consignment-stock-conversion.html',
        controller: "detailsConsignmentStockConversion.Controller",
        controllerAs: "vm",
        title: 'Consignment Stock Conversion'
      });
    }
  
    angular
      .module("myApp")
      .config(config)
      .controller("detailsConsignmentStockConversion.Controller", detailsConsignmentStockConversionController);
  })();