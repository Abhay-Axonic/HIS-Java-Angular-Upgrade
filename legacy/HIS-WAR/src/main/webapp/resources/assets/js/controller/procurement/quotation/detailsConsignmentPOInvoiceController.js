(function () {
  "use strict";

  function detailsConsignmentPOInvoiceController($scope, $rootScope, reportService ,$http, $filter, $state, screenDataService,PaginationService, $cookies, CONSTANTS, GenericService, commonDetailService, cancelReasonService, StatusService, growl) {
    var self = this;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
      $rootScope.loginpage = true;
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
        invoiceDate: new Date(),
        invoiceDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate : new Date()
        },
        selectAllRows: false,
        invoiceNumber:'',
        assetTypes : [
        	{"id": 0, type: "Select Type"},
        	{"id": 1, type: "Consumable"},
        	{"id": 2, type: "Fixed"},
        ],
        statuses: [
        	{"id": 0,"status": "All"},
            {"id": 2, "status": "Pending"},
            {"id": 8, "status": "Closed"}
        ]
      };
     
      self.model.ModalDismiss = ModalDismiss;
      self.model.poList=[];
      //self.model.selectedPO = [];
      //self.model.selectedPOItems=[];
      self.model.selectedPOList = [];
      self.model.selectedPOItemList=[];
      self.model.OpenInvoiceDate = OpenInvoiceDate;
      self.model.NavigateToBack= NavigateToBack;
      
      let consignmentPOInvoice = commonDetailService.getDataId();
      if (!!consignmentPOInvoice) {
          commonDetailService.setDataId(null);
          GetConsignmentPOInvoiceDetail(consignmentPOInvoice);
        }
      
    }
    
    var GetConsignmentPOInvoiceDetail = function(consignmentPOInvoice){
    	var URI = CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_PO_INVOICE.DETAILS_API;
    	 $rootScope.startLoader();
         GenericService.serviceAction("POST", URI, consignmentPOInvoice).then(
           function (response) {
        	   self.model.cpoiId = response.data.id;
        	   self.model.selectedPOList = response.data;
                 angular.forEach(self.model.selectedPOList.items, function (item) {
                	 item.poNumber=self.model.selectedPOList.poNumber;
 					 item.poDate=self.model.selectedPOList.poDate;
 					 item.grnNumber=self.model.selectedPOList.grnNumber;
                     item.isSelected = false;
                   //item.isDisabled = false;
                     /* start code for resolved HIS-9322 Consignment PO invoice to have editable fields */
                     item.taxAmount = item.taxAmount.toFixed(2);
                     item.netAmount = item.netAmount.toFixed(2);
                     /* end code for resolved HIS-9322 Consignment PO invoice to have editable fields */
                     
                 });
                 self.model.selectedPOItemList = self.model.selectedPOList.items;
                 self.model.vendorName = self.model.selectedPOList.vendorName;
                 self.model.poType = self.model.assetTypes.find(function (assetType) {
                     return assetType.id == self.model.selectedPOList.assetTypeId;
                 });
                 
                 self.model.invoiceNumber = self.model.selectedPOList.invoiceNumber;
                 self.model.invoiceDate = self.model.selectedPOList.invoiceDate;
                 
             $rootScope.stopLoader();
           },
           function (err) {
             $rootScope.stopLoader();
             growl.error(err.data.status);
           });    	
    }   
    var OpenInvoiceDate = function () {
        self.model.invoiceDateOpened = true;
      }    
    
    var  NavigateToBack = function () {      
        $state.go('consignmentPOInvoice');
      }
    var NavigateToAddNew = function () {
      commonDetailService.setDataId(null);
      $state.go('addNewConsignmentPOInvoice');
    }

    var ModalDismiss = function (modal) {
        var popup = angular.element('#' + modal);
        popup.modal('hide');
        self.model.rejectNote = undefined;
        self.model.cancelReason = undefined;
      }
    
    initializeController();
  }

  function config($stateProvider) {
		$stateProvider.state("detailsConsignmentPOInvoice",
						{
							url : "/detailsConsignmentPOInvoice",
							templateUrl : 'views/procurement/quotation/details-consignment-PO-invoice.html',
							controller : "detailsConsignmentPOInvoice.Controller",
							controllerAs : "vm",
							title : 'Details Of Consignment PO Invoice'
						});
	}

	angular.module("myApp").config(config).controller(
			"detailsConsignmentPOInvoice.Controller",
			detailsConsignmentPOInvoiceController);
})();