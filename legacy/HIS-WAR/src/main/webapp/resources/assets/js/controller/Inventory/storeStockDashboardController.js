(function () {
    "use strict";
  
    function storeStockDashboardController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService,PaginationService, growl, commonDetailService,PagerService) {
      var self = this;
      var stockId ;
      //Default Constructor
      var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
      function initializeController() {
        $rootScope.loginpage = true;
        self.model = {
        		fromDate: new Date(),
                toDate: new Date(),
                fromDateOptions: {
                  formatYear: 'yyyy',
                  showWeeks: false,
                  maxDate : new Date()
                },
                toDateOptions: {
                  formatYear: 'yyyy',
                  showWeeks: false,
                  maxDate : new Date()
                },
          pagination: {
            currentPage: 1,
            totalPages: 0,
            pageSize: '10',
            search: '',
            searchData: PaginationService.searchData,
            searchDataByField:PaginationService.searchDataByField,
            setTotalPages: PaginationService.setTotalPages,
            getPagesList: PaginationService.getPagesList,
            setPage: PaginationService.setPage,
            setFirstPage: PaginationService.setFirstPage,
            setPreviousPage: PaginationService.setPreviousPage,
            setNextPage: PaginationService.setNextPage,
            setLastPage: PaginationService.setLastPage,
            sortBy: PaginationService.sortBy
          },
          stores: [{
        	 "id" :1,
            "store": "Select Store"
          }],
          newStore: [{
         	 "id" :1,
             "store": "Select Store"
           }],
          assetCategories: [{
            "category": "All"
          }],
          productCategories: [{
            "category": "All"
          }],
          assetTypes: [{
            "type": "Select Asset Type"
          }],
          nearExpired: [{
            "expiry": "Select Expiry"
          }],
      };
        $scope.searchKeyword;
      self.model.SetOldValueAssetType = SetOldValueAssetType;
      self.model.ViewStockCheckDetails = ViewStockCheckDetails;
      self.model.ViewExpiredStockCheckDetails = ViewExpiredStockCheckDetails;
      self.model.ViewStockInTransitData = ViewStockInTransitData;
      self.model.OpenFromDate = OpenFromDate;
      self.model.GetProductCategoryList  = GetProductCategoryList;
      self.model.searchStock = searchStock;
      self.model.viewModal = viewModal;
      self.model.viewExpiredModal = viewExpiredModal;
      self.model.viewZeroModal = viewZeroModal;
      self.model.isConsignment = false;
      self.model.stockBatch =stockBatch;  
      self.model.stockBatchDetails =stockBatchDetails;  
      self.model.vtype = 0, 
      self.model.SearchItem = SearchItem;
      self.model.BatchOnChange = BatchOnChange;
      self.model.ValidateDashboard = ValidateDashboard;
      self.model.onNearExpiryChange = onNearExpiryChange;
      self.model.searchSubStoreStock = searchSubStoreStock;
      self.model.clearDetail = clearDetail;
      self.model.onlyStockFlag=false
      self.model.StockWithZeroFlag=true;
      self.model.zeroStockFlag=false;
      self.model.SubstituteItems = SubstituteItems;
      $scope.offset = 0;
	  $scope.noOfRecordsPerPage = 100;
	  $scope.searchItem = "";
      PopulateDropdown();
      GetAssetCategoryList(); 
      GetAssetTypePopulateDropdown();
    }

    var PopulateDropdown = function () {
        var stock = self.model;
        var URI = CONSTANTS.GLOBAL.USER_STORE_API + "/"+$scope.organizationId + "/"+$scope.unitId+ "/"+$scope.userId ;
  
       // stock.store = stock.stores[0];
        stock.stores.splice(1); 
        stock.nearExpiry = stock.nearExpired[0];
        $scope.expiryType = [{"id": 3, "expiry":"3 Months"},{"id": 6,"expiry": "6 Months"}];
        $rootScope.startLoader(); 
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
        	
            stock.stores = stock.stores.concat(response.data);
            stock.newStore = stock.newStore.concat(response.data);
            stock.nearExpired = stock.nearExpired.concat($scope.expiryType);
              for(var i=0;i<stock.stores.length;i++)
              {
                  if(stock.stores[i].id==JSON.parse(sessionStorage.getItem('selectedTopStore')).storeId)
                  {
                      stock.store = stock.stores[i];
                  }
              }
            $rootScope.stopLoader(); 
          },
          function (err) {
            $rootScope.stopLoader();
          });
      }

      var onNearExpiryChange = function () {
        if(self.model.nearExpiryItem.id){
          self.model.expiredFlag = false;
          var URI = CONSTANTS.INVENTORY.STORE.STORE_STOCK_DASHBOARD.ExpiredNear_Months;
          var data = {
            "toDate" : moment(date).format('YYYY-MM-DD'),
            "assetTypeId": self.model.assetType.id,
            "storeId": self.model.store.id,
            //"isConsignment": self.model.isConsignment , // comment consignment flag for resolved HIS-8544 
            "itemNo": self.model.itemNo ? self.model.itemNo : undefined,
            "itemCode":self.model.itemCode ? self.model.itemCode : undefined,
            "itemName":self.model.itemName ? self.model.itemName : undefined,
            "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
            "productCategoryId": self.model.productCategory.id  ,
            "vedId" : self.model.vtype ? self.model.vtype: undefined,
            "unitId":$scope.unitId,
            "organizationId":$scope.organizationId,
            "month": self.model.nearExpiryItem.id,
            "multipleStore":[],
            "offset":$scope.offset,
			"recordPerPage":$scope.noOfRecordsPerPage,
          };
          var item = {
            "id": self.model.store.id
          };
              
          data.multipleStore.push(item);
          $rootScope.startLoader();
          GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            $rootScope.stopLoader();
            self.model.pagination.data=[];
            self.model.searchStockData = response.data;  
          },
        
          function errorCallback(response) {
            $rootScope.stopLoader();
              growl.warning('Please Select Store');
          });
        }
        else{
        	 searchStock($scope.offset,$scope.noOfRecordsPerPage,null);
        }
      }

      var GetAssetTypePopulateDropdown = function () {
        var stock = self.model;
  
        var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN.DROPDOWN_API + "/" + $scope.unitId + "/" + $scope.organizationId + "/" + $scope.userId;
  
        stock.assetType = stock.assetTypes[0];
        
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            stock.assetTypes = stock.assetTypes.concat(response.data.assetType);
            $rootScope.stopLoader(); 
            searchStock($scope.offset,$scope.noOfRecordsPerPage,null);
          },
          function (err) {
            $rootScope.stopLoader();
          });
      }

    var GetAssetCategoryList = function () {
        var stock = self.model;
  
        var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API  + 1+ '/'+$scope.unitId+ '/'+$scope.organizationId;
  
        stock.assetCategory = stock.assetCategories[0];
        stock.productCategory = stock.productCategories[0];
  
        stock.assetCategories.splice(1);
        stock.productCategories.splice(1);
  
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            stock.assetCategories = stock.assetCategories.concat(response.data);
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
          });
      }
      


    var GetProductCategoryList = function (id) {
        var stock = self.model;
  
        //TODO : Add asset category to the uri
        var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API  +  id + '/'+$scope.organizationId;
        stock.productCategory = stock.productCategories[0];
  
        stock.productCategories.splice(1);
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            stock.productCategories = stock.productCategories.concat(response.data);
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
          });
      }
      var date = null;
     
      
      $scope.setNoOfRecords = function() {
    	  $scope.offset=0;
    	  searchStock($scope.offset,$scope.noOfRecordsPerPage,null);
		};

      var searchStock = function (offset,noOfRecordsPerPage,search) {
        if (ValidateDashboard()) {
        	 $scope.offset=offset;
        	var noOfRecordsPerPage = noOfRecordsPerPage;
        	var offset = offset;
        	
          if((self.model.expiredFlag == true )  && (self.model.zeroStockFlag==false || self.model.zeroStockFlag == undefined ))
          {
          
            
            if(self.model.nearExpiryItem!=undefined)
            {
              self.model.nearExpiryItem.id = '';
              self.model.nearExpiryItem.expiry = 'Select Expiry';
            }
            var URI = CONSTANTS.INVENTORY.STORE.STORE_STOCK_DASHBOARD.EXPIRED_STOCK_DATA;
            if(date == null){
              date = self.model.fromDate;
              date.setDate(date.getDate() + 1);	
            }
            else{
              
            }
            var data = {
            
               "toDate" : moment(date).format('YYYY-MM-DD'),
              "assetTypeId": self.model.assetType.id,
              "storeId": self.model.store.id,
              "itemNo": self.model.itemNo ? self.model.itemNo : undefined,
              "itemCode":self.model.itemCode ? self.model.itemCode : undefined,
              "itemName":self.model.itemName ? self.model.itemName : undefined,
              "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
              "productCategoryId": self.model.productCategory.id  ,
              "vedId" : self.model.vtype ? self.model.vtype: undefined,
              "unitId":$scope.unitId,
              "organizationId":$scope.organizationId,
              "multipleStore":[],
              "offset":offset,
			  "recordPerPage":noOfRecordsPerPage,
			  "search":(search == "" || search == null) ? null : search,
            };
            var item = {
              "id": self.model.store.id
            };
            
            data.multipleStore.push(item);
             
            $rootScope.startLoader();
      
            GenericService.serviceAction("POST", URI, data).then(
            function (response) {
            	 $scope.nonexpiredData = false;
                 $scope.expiredData = true;
                 $scope.zeroStockData=false;
              $rootScope.stopLoader();
              self.model.searchStockData = response.data;  
             
            },
      
            function errorCallback(response) {
              $rootScope.stopLoader();
              growl.error('Error');
            });      
          }
          else  if( self.model.zeroStockFlag == true)
          {
            self.model.onlyStockFlag=false;
            self.model.StockWithZeroFlag=false;
            
            var URI = CONSTANTS.INVENTORY.STORE.STORE_STOCK_DASHBOARD.ZERO_STOCK_DATA;
            if(date == null){
              date = self.model.fromDate;
              date.setDate(date.getDate() + 1);	
            }
            else{
              
            }
        
            var data = {
              "fromDate" : moment(date).format('YYYY-MM-DD'),
              "assetTypeId": self.model.assetType.id,
              "storeId": self.model.store.id,
              //"isConsignment": self.model.isConsignment , // comment consignment flag for resolved HIS-8544 
              "itemNo": self.model.itemNo ? self.model.itemNo : undefined,
              "itemCode":self.model.itemCode ? self.model.itemCode : undefined,
              "itemName":self.model.itemName ? self.model.itemName : undefined,
              "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
              "productCategoryId": self.model.productCategory.id  ,
              "vedId" : self.model.vtype ? self.model.vtype: undefined,
              "offset":offset,
        	  "recordPerPage":noOfRecordsPerPage,
        	  "search":(search == "" || search == null) ? null : search,
            };
            $rootScope.startLoader();
      
            GenericService.serviceAction("POST", URI, data).then(
            function (response) {
              $rootScope.stopLoader();
              $scope.nonexpiredData = false;
              $scope.expiredData = false;
              $scope.zeroStockData=true;
              self.model.searchStockData = response.data;  
            },
      
            function errorCallback(response) {
              $rootScope.stopLoader();
              growl.error('Error');
            });    
          
          
          }
          else if(self.model.nearExpiryItem){
            if(self.model.nearExpiryItem.id){
              onNearExpiryChange();
            }
            else{
              if( self.model.onlyStockFlag == true )
        		  {
        		  self.model.zeroStockFlag =false;
                  self.model.StockWithZeroFlag=false;
        		  
        		  
                  var URI = CONSTANTS.INVENTORY.STORE.STORE_STOCK_DASHBOARD.SEARCH_API_DATE;
                  if(date == null){
                    date = self.model.fromDate;
                    date.setDate(date.getDate() + 1);	
                  }
                  else{
                    
                  }
              
                  var data = {
                    "fromDate" : moment(date).format('YYYY-MM-DD'),
                    "assetTypeId": self.model.assetType.id,
                    "storeId": self.model.store.id,
                    //"isConsignment": self.model.isConsignment , // comment consignment flag for resolved HIS-8544 
                    "itemNo": self.model.itemNo ? self.model.itemNo : undefined,
                    "itemCode":self.model.itemCode ? self.model.itemCode : undefined,
                    "itemName":self.model.itemName ? self.model.itemName : undefined,
                    "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                    "productCategoryId": self.model.productCategory.id  ,
                    "vedId" : self.model.vtype ? self.model.vtype: undefined,
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "offset":offset,
      			  "recordPerPage":noOfRecordsPerPage,
      			 "search":(search == "" || search == null) ? null : search,

                  };
                  var item = {
                    "id": self.model.store.id
                  };
                  $rootScope.startLoader();
            
                  GenericService.serviceAction("POST", URI, data).then(
                  function (response) {
                    $rootScope.stopLoader();

          		    $scope.nonexpiredData = true;
                    $scope.expiredData = false;
                    $scope.zeroStockData=false;
                    self.model.searchStockData = response.data;  
                  },
            
                  function errorCallback(response) {
                    $rootScope.stopLoader();
                    growl.error('Error');
                  });
        		  
        		  } 
              else if(self.model.StockWithZeroFlag == true)
        		  {

        		  self.model.zeroStockFlag =false;
                  self.model.onlyStockFlag=false;
        		  
        		  
        		 
                  var URI = CONSTANTS.INVENTORY.STORE.STORE_STOCK_DASHBOARD.ZERO_WITH_STOCK_BATCH_API;
                  if(date == null){
                    date = self.model.fromDate;
                    date.setDate(date.getDate() + 1);	
                  }
                  else{
                    
                  }
              
                  var data = {
                    "fromDate" : moment(date).format('YYYY-MM-DD'),
                    "assetTypeId": self.model.assetType.id,
                    "storeId": self.model.store.id,
                    "itemNo": self.model.itemNo ? self.model.itemNo : undefined,
                    "itemCode":self.model.itemCode ? self.model.itemCode : undefined,
                    "itemName":self.model.itemName ? self.model.itemName : undefined,
                    "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                    "productCategoryId": self.model.productCategory.id  ,
                    "vedId" : self.model.vtype ? self.model.vtype: undefined,
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "offset":offset,
      			  "recordPerPage":noOfRecordsPerPage,
      			 "search":(search == "" || search == null) ? null : search,

                  };
                  var item = {
                    "id": self.model.store.id
                  };
                  $rootScope.startLoader();
            
                  GenericService.serviceAction("POST", URI, data).then(
                  function (response) {
                    $rootScope.stopLoader();
                    $scope.nonexpiredData = true;
                    $scope.expiredData = false;
                    $scope.zeroStockData=false;
                    self.model.searchStockData = response.data;  
                  },
            
                  function errorCallback(response) {
                    $rootScope.stopLoader();
                    growl.error('Error');
                  });
        		  
        		  
        		  }
        	    else if( self.model.onlyStockFlag == false &&  self.model.zeroStockFlag == false )
        		  {
        		 self.model.zeroStockFlag =false;
                  self.model.onlyStockFlag=false;
        		  
        		  
        		 
                  var URI = CONSTANTS.INVENTORY.STORE.STORE_STOCK_DASHBOARD.ZERO_WITH_STOCK_BATCH_API;
                  if(date == null){
                    date = self.model.fromDate;
                    date.setDate(date.getDate() + 1);	
                  }
                  else{
                    
                  }
              
                  var data = {
                    "fromDate" : moment(date).format('YYYY-MM-DD'),
                    "assetTypeId": self.model.assetType.id,
                    "storeId": self.model.store.id,
                    "itemNo": self.model.itemNo ? self.model.itemNo : undefined,
                    "itemCode":self.model.itemCode ? self.model.itemCode : undefined,
                    "itemName":self.model.itemName ? self.model.itemName : undefined,
                    "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                    "productCategoryId": self.model.productCategory.id  ,
                    "vedId" : self.model.vtype ? self.model.vtype: undefined,
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "offset":offset,
      			  "recordPerPage":noOfRecordsPerPage,
      			 "search":(search == "" || search == null) ? null : search,

                  };
                  var item = {
                    "id": self.model.store.id
                  };
                  $rootScope.startLoader();
            
                  GenericService.serviceAction("POST", URI, data).then(
                  function (response) {
                    $rootScope.stopLoader();
                    $scope.nonexpiredData = true;
                    $scope.expiredData = false;
                    $scope.zeroStockData=false;
                    self.model.searchStockData = response.data;  
                  },
            
                  function errorCallback(response) {
                    $rootScope.stopLoader();
                    growl.error('Error');
                  });
        		  }
  
            }
            
          }        
          else{
        	  if( self.model.onlyStockFlag == true )
        		  {
        		  self.model.zeroStockFlag =false;
                  self.model.StockWithZeroFlag=false;
        		  
        		  
                  var URI = CONSTANTS.INVENTORY.STORE.STORE_STOCK_DASHBOARD.SEARCH_API_DATE;
                  if(date == null){
                    date = self.model.fromDate;
                    date.setDate(date.getDate() + 1);	
                  }
                  else{
                    
                  }
              
                  var data = {
                    "fromDate" : moment(date).format('YYYY-MM-DD'),
                    "assetTypeId": self.model.assetType.id,
                    "storeId": self.model.store.id,
                    "itemNo": self.model.itemNo ? self.model.itemNo : undefined,
                    "itemCode":self.model.itemCode ? self.model.itemCode : undefined,
                    "itemName":self.model.itemName ? self.model.itemName : undefined,
                    "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                    "productCategoryId": self.model.productCategory.id  ,
                    "vedId" : self.model.vtype ? self.model.vtype: undefined,
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "offset":offset,
      			  "recordPerPage":noOfRecordsPerPage,
      			 "search":(search == "" || search == null) ? null : search,

                  };
                  var item = {
                    "id": self.model.store.id
                  };
                  $rootScope.startLoader();
            
                  GenericService.serviceAction("POST", URI, data).then(
                  function (response) {
                    $rootScope.stopLoader();
          		    $scope.nonexpiredData = true;
                    $scope.expiredData = false;
                    $scope.zeroStockData=false;
                    self.model.searchStockData = response.data;  
                  },
            
                  function errorCallback(response) {
                    $rootScope.stopLoader();
                    growl.error('Error');
                  });
        		  
        		  }
        	  
        	  
        	  else if(self.model.StockWithZeroFlag == true)
        		  {

        		  self.model.zeroStockFlag =false;
                  self.model.onlyStockFlag=false;
        		  
        		  
        		 
                  var URI = CONSTANTS.INVENTORY.STORE.STORE_STOCK_DASHBOARD.ZERO_WITH_STOCK_BATCH_API;
                  if(date == null){
                    date = self.model.fromDate;
                    date.setDate(date.getDate() + 1);	
                  }
                  else{
                    
                  }
              
                  var data = {
                    "fromDate" : moment(date).format('YYYY-MM-DD'),
                    "assetTypeId": self.model.assetType.id,
                    "storeId": self.model.store.id,
                    "itemNo": self.model.itemNo ? self.model.itemNo : undefined,
                    "itemCode":self.model.itemCode ? self.model.itemCode : undefined,
                    "itemName":self.model.itemName ? self.model.itemName : undefined,
                    "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                    "productCategoryId": self.model.productCategory.id  ,
                    "vedId" : self.model.vtype ? self.model.vtype: undefined,
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "offset":offset,
      			  "recordPerPage":noOfRecordsPerPage,
      			 "search":(search == "" || search == null) ? null : search,

                  };
                  var item = {
                    "id": self.model.store.id
                  };
                  $rootScope.startLoader();
            
                  GenericService.serviceAction("POST", URI, data).then(
                  function (response) {
                    $rootScope.stopLoader();
                    $scope.nonexpiredData = true;
                    $scope.expiredData = false;
                    $scope.zeroStockData=false;
                    self.model.searchStockData = response.data;  
                  },
            
                  function errorCallback(response) {
                    $rootScope.stopLoader();
                    growl.error('Error');
                  });
        		  
        		  
        		  }
        	  
        	  else if( self.model.onlyStockFlag == false &&  self.model.zeroStockFlag == false )
        		  {
        		 self.model.zeroStockFlag =false;
                  self.model.onlyStockFlag=false;
        		  
        		  
        		 
                  var URI = CONSTANTS.INVENTORY.STORE.STORE_STOCK_DASHBOARD.ZERO_WITH_STOCK_BATCH_API;
                  if(date == null){
                    date = self.model.fromDate;
                    date.setDate(date.getDate() + 1);	
                  }
                  else{
                    
                  }
              
                  var data = {
                    "fromDate" : moment(date).format('YYYY-MM-DD'),
                    "assetTypeId": self.model.assetType.id,
                    "storeId": self.model.store.id,
                    "itemNo": self.model.itemNo ? self.model.itemNo : undefined,
                    "itemCode":self.model.itemCode ? self.model.itemCode : undefined,
                    "itemName":self.model.itemName ? self.model.itemName : undefined,
                    "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                    "productCategoryId": self.model.productCategory.id  ,
                    "vedId" : self.model.vtype ? self.model.vtype: undefined,
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "offset":offset,
      			  "recordPerPage":noOfRecordsPerPage,
      			 "search":(search == "" || search == null) ? null : search,

                  };
                  var item = {
                    "id": self.model.store.id
                  };
                  $rootScope.startLoader();
            
                  GenericService.serviceAction("POST", URI, data).then(
                  function (response) {
                    $rootScope.stopLoader();
                    $scope.nonexpiredData = true;
                    $scope.expiredData = false;
                    $scope.zeroStockData=false;
                    self.model.searchStockData = response.data;  
                   
                  },
            
                  function errorCallback(response) {
                    $rootScope.stopLoader();
                    growl.error('Error');
                  });
        		  }
   
          }
        }
      }
    
      $scope.noOfRecordsPerPage=100;
      $scope.offset=0;
      $scope.getRecords = function()
      {
      	$scope.offset=0;
      	searchStock($scope.offset,$scope.noOfRecordsPerPage,null);
      }
      $scope.getPreviousRecords = function()
      {
      	if($scope.offset>=100)
      		{
      		$scope.offset=$scope.offset-100;
      		 searchStock($scope.offset,$scope.noOfRecordsPerPage,null);
      		}
      }
      $scope.getNextRecords = function()
      {
  		$scope.offset=$scope.offset+100;
  		 searchStock($scope.offset,$scope.noOfRecordsPerPage.null);
      }
      
      
      
      
      var viewModal = function (stock) { 
        $("#currentStockViewModal").modal()
        self.model.stockId = stock.id;
        if(self.model.nearExpiryItem){
          if(self.model.nearExpiryItem.id){
            self.model.batchStockData = [];
            var URI = CONSTANTS.INVENTORY.STORE.STORE_STOCK_DASHBOARD.nextexpiredbatch + $scope.unitId + "/"+ self.model.store.id + "/"  + self.model.stockId + "/"  + self.model.nearExpiryItem.id; 
            $rootScope.startLoaderPopup()
            GenericService.serviceAction("GET", URI, {}).then(
              function (response) {
                $rootScope.stopLoaderPopup(); 
                self.model.batchStockData = response.data; 
              },
              function errorCallback(response) {
                $rootScope.stopLoaderPopup();
                growl.error('Error');
              });
          }
          else{
            stockBatch();
          }
        }
        else{
          stockBatch();
        }
      }

      var viewExpiredModal = function (stock) { 
        $("#currentExpiredStockViewModal").modal()
        self.model.stockId = stock.id;
        stockExpiredBatch();
      }

      
      var viewZeroModal = function (stock) { 
          $("#currentExpiredStockViewModal").modal()
          self.model.stockId = stock.id;
          stockZeroBatch();
        }
      
      var OpenFromDate = function () {
          self.model.fromDateOpened = true;
          self.model.toDateOptions.minDate = self.model.fromDate;
        }
      var stockBatch = function () {
        self.model.batchStockData = [];
        var showDetails = false;
        var URI = CONSTANTS.INVENTORY.STORE.STORE_STOCK_DASHBOARD.BATCH_API + $scope.unitId + "/"+ self.model.store.id + "/"  + self.model.stockId + "/"  +showDetails; 
        $rootScope.startLoaderPopup()
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            $rootScope.stopLoaderPopup(); 
            self.model.batchStockData = response.data; 
          },
          function errorCallback(response) {
            $rootScope.stopLoaderPopup();
            growl.error('Error');
          });
      }

      var stockExpiredBatch = function () {
        self.model.batchStockData = [];
        var showDetails = false;
        var URI = CONSTANTS.INVENTORY.STORE.STORE_STOCK_DASHBOARD.Expired_BATCH_API + $scope.unitId + "/" + self.model.store.id + "/"  + self.model.stockId + "/"  +showDetails; 
        $rootScope.startLoaderPopup()
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            $rootScope.stopLoaderPopup(); 
            self.model.batchExpiredStockData = response.data; 
          },
          function errorCallback(response) {
            $rootScope.stopLoaderPopup();
            growl.error('Error');
          });
      }
      var stockZeroBatch = function () {
          self.model.batchStockData = [];
          var showDetails = false;
          var URI = CONSTANTS.INVENTORY.STORE.STORE_STOCK_DASHBOARD.ZERO_STOCK_BATCH_API + $scope.unitId + "/" + self.model.store.id + "/"  + self.model.stockId + "/"  +showDetails; 
          $rootScope.startLoaderPopup()
          GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
              $rootScope.stopLoaderPopup(); 
              self.model.batchExpiredStockData = response.data; 
            },
            function errorCallback(response) {
              $rootScope.stopLoaderPopup();
              growl.error('Error');
            });
        }
      var stockBatchDetails = function () {
    	  $("#currentStockDetailsViewModal").modal()
          self.model.batchStockDetailsData = [];
          var showDetails = true;
          var URI = CONSTANTS.INVENTORY.STORE.STORE_STOCK_DASHBOARD.BATCH_API + $scope.unitId + "/" + self.model.store.id + "/"  + self.model.stockId+ "/"  +showDetails ; 
          $rootScope.startLoaderPopup()
          GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
              $rootScope.stopLoaderPopup(); 
              self.model.batchStockDetailsData = response.data; 
            },
            function errorCallback(response) {
              $rootScope.stopLoaderPopup();
              growl.error('Error');
            });
        }

      var SearchItem = function (search) {

        if (search.length < 1) return;
  
        if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
          self.model.prevSearch = search;
          var URI = CONSTANTS.GLOBAL.ITEM_API +"unit/"+$scope.unitId;
          $rootScope.startLoader();
          var data = {
              "assetTypeId": 1,
              //"assetCategoryId": self.model.assetCategory.id,
              //"productCategoryId": self.model.productCategory.id,
              "fromStoreId": self.model.store.id,
              //"isConsignment" : self.model.isConsignment,
              "itemCode": search,
              //"itemName": '',
              //"vedId": self.model.vtype
            };
          return GenericService.serviceAction("POST", URI, data)
            .then(function (response) {
              $rootScope.stopLoader();
              if (!!response.data && response.data.length > 0) {
                self.model.batchNames = response.data;
                $rootScope.stopLoader();
                //console.log(self.model.patientNames.itemName);
                /*return $filter('filter')(self.model.batchNames, {
                  $: search
                });*/
                return self.model.batchNames;
              } else {
                $rootScope.stopLoader();
                return BatchNotFound(search);
              }
            }, function (err) {
              $rootScope.stopLoader();
              return BatchNotFound(search);
            });
        } else {
          if (!!self.model.batchNames && self.model.batchNames.length > 0 &&
            self.model.batchNames[0].itemFound != undefined && !self.model.batchNames[0].itemFound) {
            return BatchNotFound(search);
          } else {
            // return $filter('filter')(self.model.batchNames, {
            //   $: search
            // });
          
            return self.model.batchNames;
          }
        }
      }
  
      var BatchOnChange = function (item) {
        self.model.itemName = item.itemName;
        self.model.itemId = item.itemId;
        self.model.itemCode = item.itemCode
      }
  
      var BatchNotFound = function (search) {
        var item = {
          "itemFound": false,
          "details": "Batch Not found with name " + search
        };
        self.model.batchNames = [item];
        return self.model.batchNames;
      }
      var ValidateDashboard = function () {
        var isValid = true;
        if (!self.model.store.id) {
          isValid = false;
          growl.warning('Please Select Store');
        }

        return isValid;
      }

      var clearDetail = function()
      {
        self.model.itemName = '';
      }

      // asset type change confirmation
      var SetOldValueAssetType = function(assetType){
        self.model.oldAssetType = assetType;
        self.model.assetType = self.model.oldAssetType;
      }

      var cookieObject = $cookies.getObject('cookieObject');
      $scope.storeId = cookieObject.storeId;

      var ViewStockCheckDetails = function(item) {
        var URI = CONSTANTS.PHARMACY.OP.WALK_IN_SALES.STOCK_CHECK + $scope.unitId + "/"+$scope.organizationId + "/" + item.id + "/" + self.model.store.id;
        $rootScope.startLoaderPopup();
        GenericService.serviceAction("GET", URI, {}).then(function (response) {
            self.model.stockCheckList = response.data
            var popup = angular.element("#stockCheckDetails");
            popup.modal("show");
            $rootScope.stopLoaderPopup();
        },
        function errorCallback(response) {
            $rootScope.stopLoaderPopup();
        });
      }

      var ViewExpiredStockCheckDetails = function(item){
        var URI = CONSTANTS.PHARMACY.OP.WALK_IN_SALES.EXPIRED_STOCK_CHECK + $scope.unitId + "/" + item.id + "/" + self.model.store.id;
        $rootScope.startLoaderPopup();
        GenericService.serviceAction("GET", URI, {}).then(function (response) {
            self.model.stockCheckList = response.data
            var popup = angular.element("#stockCheckDetails");
            popup.modal("show");
            $rootScope.stopLoaderPopup();
        },
        function errorCallback(response) {
            $rootScope.stopLoaderPopup();
        });
      }

      var ViewStockInTransitData = function (item){
        var URI = CONSTANTS.INVENTORY.STORE.STORE_STOCK_DASHBOARD.Stock_in_transit;
        var data = {
          "storeId": self.model.store.id,
          "itemId": item.id,
          "unitId": $scope.unitId,
          "transitQty": item.stockInTransit
        }
        $rootScope.startLoaderPopup();
        GenericService.serviceAction("POST", URI, data).then(function (response) {
          self.model.stockInTransitData = response.data;
          var popup = angular.element("#stockInTransitViewModal");
          popup.modal("show");
          $rootScope.stopLoaderPopup();
        },
        function errorCallback(response) {
          $rootScope.stopLoaderPopup();
        });
      }
      
      /********************************/
    
      var SubstituteItems = function  (item) {
    	  
    	  $scope.genericId1 = item.genericId;
    	  $scope.itemId1 = item.id;
    	  $scope.narcotic1 = item.narcotic;
    	  $scope.genericName = item.genericName;
    	
    	 self.model.chkFlg=false;
           if(item.genericId != undefined && item.genericId != null && item.genericId!=""){
        	   $scope.expStrDate=moment(new Date()).format("YYYY-MM-DD");
               var URI = ROOT_URL +"/api/inventory/stores/stock/dashboard/getSubstituteDetailsForStoreStockDashboard" + "/" +item.genericId +'/' + item.id+ '/' +item.narcotic+ '/' + 0 + '/' + $scope.unitId +   '/' + $scope.expStrDate;
               $rootScope.startLoaderPopup();
               GenericService.serviceAction("GET", URI, {}).then(
                   function (response) {
                       self.model.substituteDetails = response.data;
                       angular.forEach(self.model.stores, function(value, key){
                    	   if(value.store.includes("All Stores"))
                    		   {
                    		   self.model.stores.splice(key,1); 
                    		   }
                       })

                       self.model.stores.splice(1, 0, {
                           "store": "All Stores"
                       }
                       );

//                    	   self.model.store = self.model.stores[2];
                    	   self.model.substituteStore = self.model.store;

                       var popup = angular.element("#substituteDetails");
                       popup.modal("show");
                      
                       $rootScope.stopLoaderPopup();
                   });
           }
           else{
        	   $rootScope.stopLoader();
               growl.warning('Generic is not available for selected item.!');
               return false;
           }

       }
      
      var searchSubStoreStock=function(storeSubId){
    	  self.model.chkFlg=true;
    	 var storeSubId = storeSubId.id !== undefined && storeSubId.id !== null ? storeSubId.id : self.model.substituteStore.id;
//    	 angular.forEach(self.model.substituteDetails, function(value, key){
//    		 if(value.storeId == storeSubId){
//    			 var URI = ROOT_URL +"/api/inventory/stores/stock/dashboard/getSubstituteDetailsForStoreStockDashboard" + "/" +$scope.genericId1 +'/' + $scope.itemId1+ '/' +$scope.narcotic1+ '/' + storeSubId + '/' + $scope.unitId +   '/' + $scope.expStrDate;
//                 $rootScope.startLoaderPopup();
//                 GenericService.serviceAction("GET", URI, {}).then(
//                     function (response) {
//                         self.model.substituteDetailsNew = response.data;
//                         $rootScope.stopLoaderPopup();
//                     });
//    		 }
    		/* else if(storeSubId!=undefined){
    			 self.model.substituteDetailsNew=[];
    		 }*/
    		 if(self.model.substituteStore.store == "All Stores"){
    			 var URI = ROOT_URL +"/api/inventory/stores/stock/dashboard/getSubstituteDetailsForStoreStockDashboard" + "/" +$scope.genericId1 +'/' + $scope.itemId1+ '/' +$scope.narcotic1+ '/' + 0 + '/' + $scope.unitId +   '/' + $scope.expStrDate;
                 $rootScope.startLoaderPopup();
                 GenericService.serviceAction("GET", URI, {}).then(
                     function (response) {
                    	 self.model.substituteDetailsNew = response.data;
//                         var popup = angular.element("#substituteDetails");
//                         popup.modal("show");
                        
                         $rootScope.stopLoaderPopup();
                     });
    			 
    		 }else{
    			 var URI = ROOT_URL +"/api/inventory/stores/stock/dashboard/getSubstituteDetailsForStoreStockDashboard" + "/" +$scope.genericId1 +'/' + $scope.itemId1+ '/' +$scope.narcotic1+ '/' + storeSubId + '/' + $scope.unitId +   '/' + $scope.expStrDate;
                 $rootScope.startLoaderPopup();
                 GenericService.serviceAction("GET", URI, {}).then(
                     function (response) {
                         self.model.substituteDetailsNew = response.data;
                         $rootScope.stopLoaderPopup();
                     });
    		 }
//    	 });
    	 
      }
        $scope.changeToZeroStock = function(offset,noOfRecordsPerPage,search){
            if(self.model.zeroStockFlag){
                self.model.onlyStockFlag = false;
                self.model.zeroStockFlag = true;
                searchStock(offset, noOfRecordsPerPage, search);
            }
            else{
                self.model.onlyStockFlag = false;
                self.model.zeroStockFlag = false;
                searchStock(offset, noOfRecordsPerPage, search);
            }
        };

        $scope.changeToOnlyStock = function (offset,noOfRecordsPerPage,search){
            if(self.model.onlyStockFlag){
                self.model.zeroStockFlag = false;
                self.model.onlyStockFlag = true;
                searchStock(offset, noOfRecordsPerPage, search);
            }
            else{
                self.model.onlyStockFlag = false;
                self.model.zeroStockFlag = false;
                searchStock(offset, noOfRecordsPerPage, search);
            }
        };

      /********************************/

      initializeController();
    }

    function config($stateProvider) {
      $stateProvider.state("storeStockDashboard", {
        url: "/storeStockDashboard",
        templateUrl: 'views/PharmacyInventory/Store/store-stock-dashboard.html',
        controller: "StoreStockDashboard.Controller",
        title: 'Store Stock Dashboard',
    	resolve : {

   			data: function($q, $state, $timeout) {
   		        var deferred = $q.defer();
   		
   		        $timeout(function() {
   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
   				 	.modules.filter(function(item){
   	            	   return (item.id == 135);
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
      .controller("StoreStockDashboard.Controller", storeStockDashboardController);
  })();
