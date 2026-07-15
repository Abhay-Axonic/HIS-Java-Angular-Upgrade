(function () {
    "use strict";
  
    function detailsConsignmentReturnController($scope, $rootScope, $http, $filter, $state, $cookies, CONSTANTS, GenericService, commonDetailService, cancelReasonService, StatusService, growl) {
      var self = this;
  
      //Default Constructor
      var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
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
        self.model.SelectAllCRDItems = SelectAllCRDItems;
        
        self.model.GetConsignmentStockReturnData=GetConsignmentStockReturnData;
        self.model.UpdateConsignmentStockReturnData=UpdateConsignmentStockReturnData;
        self.model.ViewDetails=ViewDetails;
        self.model.ConsignmentReturnDetailHistory=ConsignmentReturnDetailHistory;
        
        self.model.ConsignmentStockPOHistory=ConsignmentStockPOHistory
        
        GetConsignmentStockReturnData();
      }
  
      var GetConsignmentStockReturnData = function () {
          var detailsData = commonDetailService.getDataId();
          self.model.inspectId = detailsData.id;
          
          var URI = CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_RETURN.STOCK_RETURN_DETAILS_API;
          
          var param = {
            "id": detailsData.id,
            "storeId": detailsData.storeId,
            "unitId": $scope.unitId
          }
          $rootScope.startLoader();
          GenericService.serviceAction("POST", URI, param).then(
            function (response) {
              self.model.consignmentStockReturnData = response.data;
              console.log(self.model.consignmentStockReturnData);
              self.model.remark = response.data.remark;
              self.model.requestItems = JSON.parse(JSON.stringify(response.data));
              self.model.consignmentStockReturnData.items.forEach(function (item) {
                item.isDisabled = true;
                //item.returnQuantity = 0;
                item.returnQuantity = item.receivedQuantity ? angular.copy(item.receivedQuantity) : 0;//added after comment return qty  for request i.e. Returned Consignment Stock
                item.totalReturnConsignmentStock = item.totalReturnedQuantity - (item.receivedQuantity-item.returnQuantity);
                //item.availableQty = ((((item.convertedConsignmentStock- item.consumedQty) - (item.totalReturnedQuantity-(item.receivedQuantity-item.returnQuantity)) )- item.storeStockTransfered)-item.totalPOCreatedQuantity);
                item.availableQty = ((item.totalConsignmentStock - item.consumedQty) - item.convertedConsignmentStock) 
				- item.totalReturnConsignmentStock - item.storeStockTransfered;

              });
             
              $rootScope.stopLoader();
            },
            function (err) {
              $rootScope.stopLoader();
              growl.error(err.data.message);
            });
        }
      var ViewDetails = function(storeId, batchId, batchNo, lotNo )
      {

          //var URI = CONSTANTS.PROCUREMENT.QUOTATION.PHYSICAL_INSPECTION.DETAILS_API + "detailsConsignmentStockConversion";
          var URI = CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_CONVERSION.STORE_STOCK_API;
          
        //  $rootScope.startLoader();
          GenericService.serviceAction("GET", URI+"/"+storeId+"/"+batchId+"/"+batchNo+"/"+lotNo).then(
            function (response) {
              self.model.listData = response.data;
         //     $rootScope.stopLoader();
            },
            function (err) {
         //     $rootScope.stopLoader();
              growl.error(err.data.message);
            });
        
    	  
    	  
      }
      var ValidUpdateItem = function(){
    	  var isValid=true;
    	  
    	  if(self.model.selectedItems.length == 0){
    		  isValid = false;
   			 growl.error('Please Select Item To Update');
    	  }
    	  
    	  angular.forEach(self.model.selectedItems, function(item, key) {
				  if (item.returnQuantity > item.availableQty && item.isSelected){
					  isValid = false;
					  growl.error('Return Quantity Must be Less Then Available Stock for Return' );
				  }
			  });
    	  
    	  return isValid;
      }

      $scope.calQty = function(item) {
        if(ValidUpdateItem()){
          item.availableQty = item.availableQty - item.returnQuantity;
        }
      }
      
      var UpdateConsignmentStockReturnData = function () {
    	  
    	  if(ValidUpdateItem()) {
    	  
    	  var URI =  CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_RETURN.UPDATE_CONSIGNMENT_STOCK_RETURN_DETAIL;
    	  
    	  var data = [];
          self.model.selectedItems.forEach(function (crdItem) {
            if (crdItem.isSelected && (crdItem.returnQuantity > 0 )  ) {
                data.push({
                  "id": crdItem.id,
                  "returnBatchQuantity" : crdItem.returnQuantity,
                  "userId": $scope.userId,
                  "unitId": $scope.unitId                   
                  /*"remark" : crdItem.remark,
                  "batchId" : crdItem.batchId,
                  "itemId" : crdItem.itemId,
                  "batchNo" : crdItem.batchNo,
                  "lotNo" : crdItem.lotNo*/
                  });
                }
          });
          $rootScope.startLoader();
          GenericService.serviceAction("POST", URI, data).then(
            function (response) {
            	
            	self.model.SelectedOrderIds = [];
            	self.model.selectedItems = [];
            	self.model.selectAllRows = false;
            	self.model.consignmentStockReturnData.items.forEach(function (item) {
                     item.isDisabled = true;
                     item.isSelected = false;
                   });
            	
              $rootScope.stopLoader();
              growl.success('Consignment Return Detail successfully Updated');
              console.log(response.data);
              NavigateToBack();
            },
            function (err) {
              $rootScope.stopLoader();
              growl.error(err.data.message);
            });
    	  
    	  }//valid
          
      }
      
      var SelectItem = function (item) {
          var itemIds = self.model.SelectedOrderIds;
          if (item.isSelected) {
        	  item.isDisabled = false;
        	  itemIds.push(item.id);
        	  self.model.selectedItems.push(item);
        	  /*if (self.model.consignmentStockReturnData.items.every(isSelected)) {
        		  self.model.selectAllRows = true;
        		  } else {
        			  self.model.selectAllRows = false;
        			  }*/
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
        		  /*if (self.model.consignmentStockReturnData.items.every(isSelected)) {
        			  self.model.selectAllRows = false;
        			  } else {
        				  self.model.selectAllRows = true;
        				  }*/
        		  }
          if (self.model.consignmentStockReturnData.items.every(isSelected)) {
    		  self.model.selectAllRows = true;
    		  } else {
    			  self.model.selectAllRows = false;
    			  }
          console.log(self.model.selectedItems);
        }
      
      var isSelected = function (item) {
          if (!isDisable(item))
            return item.isSelected;
          else
            return true;
        }
      
      var isDisable = function (item) {
          if (item.status == "Closed" && item.isSelected) {
            return true;
          }
          return false;
        }

      var SelectAllCRDItems = function () {
          if (self.model.selectAllRows) {
            //self.model.SelectedOrderIds = [];
            self.model.consignmentStockReturnData.items.forEach(function (items) {
              if (!isDisable(items)) {
            	  items.isSelected = true;
            	  items.isDisabled = false;
                self.model.SelectedOrderIds.push(items.id);
                self.model.selectedItems.push(items);
              }
            });
          } else {
            self.model.SelectedOrderIds = [];
            self.model.selectedItems = [];
            self.model.consignmentStockReturnData.items.forEach(function (items) {
            	items.isSelected = false;
            	items.isDisabled = true;
            });
          }
          console.log(self.model.selectedItems);
        }
      
      
      
      	var ConsignmentReturnDetailHistory = function (item,crId) {
    	  
    	  var URI = CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_RETURN.HISTORY_API;
    	  var param = {
    	            "crId": crId,
    	            "crDetailId": item.id,
    	            "grnDetailId":item.grnDetailId,
                  "unitId": $scope.unitId
    	          }
    	             
          $rootScope.startLoaderPopup();
          GenericService.serviceAction("POST", URI, param).then(
            function (response) {
              self.model.consignmentReturnDetailHistory = response.data;
              $rootScope.stopLoaderPopup();
            },
            function (err) {
              $rootScope.stopLoaderPopup();
            });
        }
  
      var NavigateToBack = function (){
        $state.go('consignmentPoCreat');
      }
      
      
      var NavigateToBack = function () {
        $state.go('consignmentReturn');
      }

      var ModalDismiss = function (modal) {
        var popup = angular.element('#' + modal);
        popup.modal('hide');
        self.model.rejectNote = undefined;
        self.model.cancelReason = undefined;
        self.model.onclose.isReject = false;
        self.model.rejectQty = 0;
        self.model.rejectReason = undefined;
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
  
      initializeController();
    }
  
    function config($stateProvider) {
      $stateProvider.state("detailsConsignmentReturn", {
        url: "/detailsConsignmentReturn",
        templateUrl: 'views/procurement/quotation/details-consignment-return.html',
        controller: "detailsConsignmentReturn.Controller",
        controllerAs: "vm",
        title: 'Consignment Return'
      });
    }
  
    angular
      .module("myApp")
      .config(config)
      .controller("detailsConsignmentReturn.Controller", detailsConsignmentReturnController);
  })();