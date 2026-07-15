(function () {
  "use strict";

  function consignmentPOInvoiceController($scope, $rootScope, reportService ,$http, $filter, $state, screenDataService,PaginationService, $cookies, CONSTANTS, GenericService, commonDetailService, cancelReasonService, StatusService, growl) {
    var self = this;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
      $rootScope.loginpage = true;
      var date = new Date();
      self.model = {
        pagination: {
          currentPage: 1,
          totalPages: 0,
          pageSize: '10',
          search: '',
          searchData: PaginationService.searchData,
          setTotalPages: PaginationService.setTotalPages,
          getPagesList: PaginationService.getPagesList,
          setPage: PaginationService.setPage,
          setFirstPage: PaginationService.setFirstPage,
          setPreviousPage: PaginationService.setPreviousPage,
          setNextPage: PaginationService.setNextPage,
          setLastPage: PaginationService.setLastPage,
          sortBy: PaginationService.sortBy
        },
        fromDate: new Date(),
        toDate: new Date(),
        fromDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate : new Date(),
          minDate : moment(new Date()).subtract(180, 'days').toDate()
//          minDate : date.setDate((new Date()).getDate() - 90),
        },
        toDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate : new Date()
        },
        selectAllRows: false,
        SelectedOrderIds: [],
        storeList: [{
            "store": "Select Store"
          }],
        toStoreList: [{
          "store": "Select Issueing Store"
        }],
        fromStoreList: [{
          "store": "Select Requesting Store"
        }],
        statuses: [
        	{"id": 0,"status": "All"},
            {"id": 9, "status": " Cancelled "},
            {"id": 8, "status": "Closed"},
            {"id": 2, "status": " Pending "}
        ]
      };

      cancelReasonService.GetReason().then(function(reason) {
        self.model.reasons = reason;
      });
      
      StatusService.GetStatus().then(function(status) {
        self.model.statusList = status;
      });

      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      //self.model.GetPurchaseOrderList = GetPurchaseOrderList;
      self.model.NavigateToAddNew = NavigateToAddNew;
      self.model.NavigateToDetails = NavigateToDetails;
      self.model.NavigateToEdit = NavigateToEdit;
      self.model.SelectAllOrders = SelectAllOrders;
      //self.model.SelectPurchaseOrder = SelectPurchaseOrder;      
      self.model.isDisable = isDisable;
      //self.model.UpdatePurchaseOrder = UpdatePurchaseOrder;
      self.model.ValidateAndUpdate = ValidateAndUpdate;
      self.model.ModalDismiss = ModalDismiss;
      self.model.SearchVendorNames = SearchVendorNames;
      self.model.AddVendorName = AddVendorName;
      self.model.rePrintBillSlip=rePrintBillSlip;
      self.model.search = '';
      self.model.prevSearch = '';
      self.model.vendorNames = [];

      PopulateDropdown();
      self.model.GetConsignmentPOInvoiceList = GetConsignmentPOInvoiceList;
      self.model.SelectConsignmentPOInvoice = SelectConsignmentPOInvoice;
      self.model.UpdateConsignmentPOInvoice=UpdateConsignmentPOInvoice;
      self.model.CancelConsignmentPOInvoice=CancelConsignmentPOInvoice;
    }

    var setStatus = function (x){
      var status = self.model.statusList.find(function(obj){
        return obj.status == x;
      });
      
      return status.id;
    }

    var OpenFromDate = function () {
      self.model.fromDateOpened = true;
      self.model.toDateOptions.minDate = self.model.fromDate;
    }

    var OpenToDate = function () {
      self.model.toDateOpened = true;
      self.model.toDateOptions.minDate = self.model.fromDate;
    }

    
    var PopulateDropdown = function () {
      //self.model.store = self.model.storeList[0];
      self.model.issuingStoreList = self.model.toStoreList[0];      
      //self.model.fromstore = self.model.fromstoreList[0];
      self.model.requestingStore = self.model.fromStoreList[0];
      
      self.model.status = self.model.statuses[0];

      var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.toStoreList = self.model.toStoreList.concat(response.data.deliveryStores);
          self.model.fromStoreList = self.model.fromStoreList.concat(response.data.requestingStores);
          //self.model.statuses = self.model.statuses.concat(response.data.status);
          $rootScope.stopLoader();
          GetConsignmentPOInvoiceList();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }
    
    var GetConsignmentPOInvoiceList = function () {
    	      if (ValidateDate()) {
    	        var URI = CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_PO_INVOICE.SEARCH_API;

    	        if (!self.model.vendorName) {
    	          self.model.vendorId = undefined;
    	          self.model.vendorName = undefined;
    	        }

    	        var data = {
    	          "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
    	          "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
    	          //"storeId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
    	          "issuingStoreId": !!self.model.issuingStore && !!self.model.issuingStore.id ? self.model.issuingStore.id : undefined,
    	          "vendorId": self.model.vendorId,
    	          "vendorName": self.model.vendorName ? self.model.vendorName : undefined,
    	          "poNumber": self.model.poNumber ? self.model.poNumber : undefined,
    	          //"requestingStoreId": !!self.model.fromstore && !! self.model.fromstore.id ? self.model.fromstore.id : undefined,
    	          "requestingStoreId": !!self.model.requestingStore && !! self.model.requestingStore.id ? self.model.requestingStore.id : undefined,
    	          "statusId": !!self.model.status && !!self.model.status.id ? self.model.status.id : undefined,
                "unitId": $scope.unitId
    	        }

    	        $rootScope.startLoader();
    	        GenericService.serviceAction("POST", URI, data).then(
    	          function (response) {
    	            self.model.pagination.data=[];
    	            //self.model.purchaseOrders = response.data;
    	            self.model.consignmentPOInvoiceList = response.data;
    	            if(!!response && !!response.data && response.data.length > 0){            
    	              PaginationService.searchData( self.model.consignmentPOInvoiceList, self.model.pagination);
    	            }
    	            self.model.SelectedOrderIds = [];
    	            self.model.selectAllRows = false;
    	            $rootScope.stopLoader();
    	          },
    	          function (err) {
    	            $rootScope.stopLoader();
    	            growl.error(err.data.status);
    	          });

    	        var screenData = {
    	          searchrequest: {
    	            "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
    	            "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
    	            "status": self.model.status,
    	            //"store": self.model.store,
    	            "issuingStore": self.model.issuingStore,
    	            "requestingStore": self.model.requestingStore,
    	            "vendorId": self.model.vendorId,
    	            "vendorName": self.model.vendorName,
    	            "poNumber": self.model.poNumber
    	          },
    	          screenName: 'ConsignmentPOInvoice'
    	        }
    	        screenDataService.setScreenData(screenData);
    	      }
    }
    
    var isEverySelectedItem = function (item) {
        return item.isSelected;
      }
    
    var UpdateConsignmentPOInvoice = function() {
    	 if (ValidateStatus()) {
    	var data = [];    	
    	angular.forEach(self.model.consignmentPOInvoiceList,
    					function(consignmentPOInvoice) {
    						if (consignmentPOInvoice.isSelected) {
    							var dataPO = {
    								"id" : consignmentPOInvoice.id,//invoiceId
    								"cancelReasonId" : self.model.cancelReasonId ? self.model.cancelReasonId : undefined ,
    								"cancelNote" : consignmentPOInvoice.cancelNote ? consignmentPOInvoice.cancelNote :undefined,
    								"statusId" : !self.model.cancelReasonId ? 8 : 9
    							}    							
    							data.push(dataPO);
    						}
    					});
    	$rootScope.startLoader();
    	var URI = CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_PO_INVOICE.UPDATE_API;
		GenericService.serviceAction("PATCH", URI, data).then(
				function (response) {
		              self.model.SelectedOrderIds = [];
		              self.model.selectAllRows = false;
		              ModalDismiss('confirmSave');
		              $rootScope.stopLoader();
		              GetConsignmentPOInvoiceList();
		              growl.success(response.data.message);
		            },
		            function (err) {
		              ModalDismiss('confirmSave');
		              $rootScope.stopLoader();
		              growl.error(err.data.message);
		            });
		        } else {
		          growl.error('Please select Consignment PO Invoice to Close');
		          ModalDismiss('confirmSave');
		          return;
		        }
    }
    
    var CancelConsignmentPOInvoice = function(){
    	if (ValidetReason()) {
    		self.model.consignmentPOInvoiceList.forEach(function (consignmentPOInvoice) {
          if(consignmentPOInvoice.id == self.model.cpoiId) {
            consignmentPOInvoice.cancelReasonId = self.model.cancelReasonId;
            consignmentPOInvoice.cancelNote = self.model.cancelNote;
          }
        });
        var data ={
          "statusId": 9,
          "approvalStatusId": 9,
          "cancelReasonId": self.model.cancelReasonId,
          "cancelNote": self.model.cancelNote,
          "userId": $scope.userId
        }
        var url = CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_PO_INVOICE.Cancel_Api + self.model.cpoiId;
        GenericService.serviceAction("PATCH", url, data).then(
				function (response) {
          GetConsignmentPOInvoiceList();
          ModalDismiss("cancelConsignmentPOInvoice");
          growl.success(response.data.message);
        },
        function (err) {
          ModalDismiss("cancelConsignmentPOInvoice");
          growl.error(err.data.message);
        });
      }
    }
    
    var NavigateToAddNew = function () {
        commonDetailService.setDataId(null);
        $state.go('addNewConsignmentPOInvoice');
      }

      var NavigateToDetails = function (consignmentPOInvoice) {
        commonDetailService.setDataId(consignmentPOInvoice);
        $state.go('detailsConsignmentPOInvoice');
      }

      var NavigateToEdit = function (consignmentPOInvoice) {
        commonDetailService.setDataId(consignmentPOInvoice);
        $state.go('addNewConsignmentPOInvoice');
      }

    function SearchVendorNames(search) {
      if (search.length < 1) return;

      if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
        self.model.prevSearch = search;
        var URI = CONSTANTS.GLOBAL.VENDOR_SEARCH_API + search + "/"+$scope.unitId;
        $rootScope.startLoader();
        return GenericService.serviceAction("GET", URI, {})
        .then(function (response) {
          if (!!response.data && response.data.length > 0) {
            self.model.vendorNames = response.data;
            $rootScope.stopLoader();
            return $filter('filter')(self.model.vendorNames, {
              $: search
            });
          } else {
            $rootScope.stopLoader();
            return nameNotFound(search);
          }
        }, function (err) {
          $rootScope.stopLoader();
          return nameNotFound(search);
        });
      } else {
        if (!!self.model.vendorNames && self.model.vendorNames.length > 0 && self.model.vendorNames[0].itemFound != undefined && !self.model.vendorNames[0].itemFound) {
          return nameNotFound(search);
        } else {
          return $filter('filter')(self.model.vendorNames, {
            $: search
          });
        }
      }
    }

    function nameNotFound(search) {
      var item = {
        "itemFound": false,
        "detail": "Vendor Not found with name " + search
      };
      self.model.vendorNames = [item];
      return self.model.vendorNames;
    }

    function AddVendorName (vendor) {
      self.model.vendorName = vendor.vendorName;
      self.model.vendorId = vendor.id;
    }

    var ValidateDate = function () {
      var isValid = true;
      if (self.model.fromDate > self.model.toDate) {
        growl.error('From date should be less than to Date');
        isValid = false;
      } else if (self.model.fromDate == self.model.toDate) {
        isValid = true;
      }
      
      if (!self.model.fromDate || !self.model.toDate) {
        growl.error('Please enter valid date');
        isValid = false;
      }

      return isValid;
    }

    var SelectAllOrders = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedOrderIds = [];
        self.model.consignmentPOInvoiceList.forEach(function (consignmentPOInvoice) {
          if (!isDisable(consignmentPOInvoice)) {
        	  consignmentPOInvoice.isSelected = true;
            self.model.SelectedOrderIds.push(consignmentPOInvoice.id);
          }
        });
      } else {
        self.model.SelectedOrderIds = [];
        self.model.consignmentPOInvoiceList.forEach(function (consignmentPOInvoice) {
        	consignmentPOInvoice.isSelected = false;
        });
      }
    }

    var isDisable = function (consignmentPOInvoice) {
      /*if (consignmentPOInvoice.status != 'New') {
        return true;
      }*/
      if (consignmentPOInvoice.status != 'Pending') {
    	  return true;
      }
      return false;
    }

    var isSelected = function (consignmentPOInvoice) {
      if (!isDisable(consignmentPOInvoice))
        return consignmentPOInvoice.isSelected;
      else
        return true;
    }

    var SelectPurchaseOrder = function (purchase) {
      var purchaseIds = self.model.SelectedOrderIds;

      if (purchase.isSelected) {
        purchaseIds.push(purchase.id);
      } else {
        var index = purchaseIds.indexOf(purchase.id);
        if (index > -1) {
          purchaseIds.splice(index, 1);
        }
      }

      if (self.model.purchaseOrders.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
    }
    
    var SelectConsignmentPOInvoice = function (consignmentPOInvoice) {
        var consignmentPOInvoiceIds = self.model.SelectedOrderIds;

        if (consignmentPOInvoice.isSelected) {
        	consignmentPOInvoiceIds.push(consignmentPOInvoice.id);
        } else {
          var index = consignmentPOInvoiceIds.indexOf(consignmentPOInvoice.id);
          if (index > -1) {
        	  consignmentPOInvoiceIds.splice(index, 1);
          }
        }

        if (self.model.consignmentPOInvoiceList.every(isSelected)) {
          self.model.selectAllRows = true;
        } else {
          self.model.selectAllRows = false;
        }
      }

    var ValidateAndUpdate = function (action, popupName, data) {
      self.model.action = action;
      var popup = angular.element('#' + popupName);
      self.model.cpoiId = data.id;
      popup.modal('show');
    }

    var ValidateStatus = function (){
      var isValid = true;
      if (!self.model.SelectedOrderIds.length) {
        isValid = false;
      }
      return isValid;
    }

    var ValidetReason = function () {
      var isValid = true;

      if (!self.model.cancelReasonId) {
        self.model.isReasonValid = true;
        isValid = false;
      }
      
      return isValid;
    }

    var ModalDismiss = function (modal) {
      var popup = angular.element('#' + modal);
      popup.modal('hide');
      self.model.rejectNote = undefined;
      self.model.cancelReason = undefined;
    }

    var UpdatePurchaseOrder = function () {
      if (self.model.action == 'Update') {
        if (ValidateStatus()) {
          var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER.APPROVE_PO_API;

          var data = {
            "poIdList": self.model.SelectedOrderIds,
            "statusId": setStatus('Pending'),
            "approvalStatusId": setStatus('Pending Approval'),
            "userId": $scope.userId
          };

          $rootScope.startLoader();
          GenericService.serviceAction("PATCH", URI, data).then(
            function (response) {
              self.model.SelectedOrderIds = [];
              ModalDismiss('confirmSave');
              $rootScope.stopLoader();
              GetPurchaseOrderList();
              self.model.selectAllRows = false;
              growl.success(response.data.message);
            },
            function (err) {
              ModalDismiss('confirmSave');
              $rootScope.stopLoader();
              growl.error(err.data.message);
            });
        } else {
          growl.error('Please select PO to approve');
          ModalDismiss('confirmSave');
          return;
        }
      } else if (self.model.action == 'Cancel') {
        if (ValidetReason()) {
          var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER.CANCEL_PO_API + self.model.id;
          
          var data = {
            "statusId": setStatus('Closed'),
            "approvalStatusId": setStatus('Cancelled'),
            "cancelReasonId": self.model.cancelReason ? self.model.cancelReason : undefined,
            "cancelNote": self.model.rejectNote
          }

          $rootScope.startLoader();         
          GenericService.serviceAction("PATCH", URI, data).then(
            function (response) {
              self.model.SelectedOrderIds = [];
              ModalDismiss('cancelPO');
              GetPurchaseOrderList();

              $rootScope.stopLoader();
              growl.success(response.data.message);
            }, function (err) {
              $rootScope.stopLoader();
              growl.error(err.data.message);
            });
        }
      }
    }

    var rePrintBillSlip = function (grn) {
      var data = {
        "appName": "HIS",
        "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
        "reportDesignFilePath": "get_PO.rpt",
        "reportFormat": "PDF",
        "exportFileName": "Grn",
        "reportParams": [
          {
            "paramName": "POId",
            "paramType": "Integer",
            "paramValue": grn.poId
          },
          {
            "paramName": "StoreId",
            "paramType": "Integer",
            "paramValue": grn.issueingStoreId
          }
        ]
      };
      reportService.generate(REPORT_API_URI1, data);
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("consignmentPOInvoice", {
      url: "/consignmentPOInvoice",
      templateUrl: 'views/procurement/quotation/consignment-PO-invoice.html',
      controller: "consignmentPOInvoice.Controller",
      title: 'Consignment Purchase Order Invoice',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 885);
				 	});
					if(arr.length <= 0){
						$state.go("accessDenied")
						deferred.reject();
					 } else {
						deferred.resolve();
					 }
		        });
		
		        return deferred.promise;
		      }
		 },
      controllerAs: "vm"
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("consignmentPOInvoice.Controller", consignmentPOInvoiceController);
})();