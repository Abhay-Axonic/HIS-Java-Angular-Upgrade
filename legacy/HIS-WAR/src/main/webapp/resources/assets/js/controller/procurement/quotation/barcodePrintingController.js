(function () {
  "use strict";

  function barcodePrintingController($scope, $rootScope, $http, $filter, $state, $window, $timeout, $cookies, CONSTANTS,PaginationService, GenericService, CurrencyService, growl) {
    var self = this;

    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
      $rootScope.loginpage = true;

      self.model = {
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
        assetCategories: [{
          "category": "Select Asset Category"
        }],
        productCategories: [{
          "category": "Select Product Category"
        }],
        storeList: [{
          "store": "Select Store"
        }],
        assetType: 2,
        SelectedItems: [],
        SelectedBatches: [],
        items: [],
        options: {
          width: 2,
          height: 32,
          displayValue: false,
          backgroundColor: '#ffffff',
          lineColor: '#000000',
          margin: 0
        },
        fromDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate: new Date()
        },
        toDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate: new Date()
        },
        fromDate: new Date(),
        toDate: new Date(),
        SelectedItemsIds: [],
        selectAllRows: false,
        SelectedItemIds: [],
        NewItemList: [],
        GRNItem:[]
      };

      CurrencyService.GetCurrency().then(function (currency) {
        self.model.Currency = currency;
      })

      //Methods
      self.model.ShowModalPopup = ShowModalPopup;
      self.model.PopulateProductCategory = PopulateProductCategory;
      self.model.PopulateBatchList = PopulateBatchList;
      self.model.SelectAllItems = SelectAllItems;
      self.model.SelectItem = SelectItem;
      self.model.AddItems = AddItems;
      self.model.ShowBarcode = ShowBarcode;
//      self.model.printCount = printCount;
      self.model.SelectAllBatches = SelectAllBatches;
      self.model.SelectBatch = SelectBatch;
      self.model.PrintBarcode = PrintBarcode;
      self.model.SearchVendorNames = SearchVendorNames;
      self.model.AddVendorName = AddVendorName;
      self.model.nameNotFound = nameNotFound;
      self.model.search = '';
      self.model.prevSearch = '';
      self.model.vendorNames = [];
      self.model.againstId;
      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.SelectAllGRNItems = SelectAllGRNItems;
      self.model.SelectGRNItem = SelectGRNItem;
      self.model.searchNewGRN = searchNewGRN;
      self.model.getAllGRNItemDetails = getAllGRNItemDetails;
      self.model.Expand = Expand;
      self.model.clearList = clearList;
      self.model.printSelectedBarcodes = printSelectedBarcodes;
      self.model.printAllBarCodes = printAllBarCodes;
      self.model.PrintBarcodeOnBarcodeCilck = PrintBarcodeOnBarcodeCilck;
      self.model.barcode={
    	        type: "code128",
    	        code: '',
    	        itemCode: '',
              itemName: '',
    	        batchCode: "123456",
              batchNo: '',
    	        hospital : '',
    	        expiry:'',
    	        mrp:'',
              lotNo: '',
              grnNo: '',
              salePrice: '',
              vendorCode: '',
              vendorName: ''
    	      };
      self.model.options =  {
		          width: 3,
		          height: 30,
		          displayValue: false,
		          font: 'monospace',
		          textAlign: 'center',
		          textPosition: 'bottom',
		          fontSize: 6,
		          backgroundColor: '#ffffff',
		          lineColor: '#000000',
		          margin: 0
		        };
			
      PopulateDropdown();
    }

    //Method to populate the list of Purchase Request dropdown
    var PopulateDropdown = function () {
      var barcode = self.model;
      barcode.store = barcode.storeList[0];

      var URI = CONSTANTS.GLOBAL.USER_STORE_API + "/"+$scope.organizationId + "/"+$scope.unitId+ "/"+$scope.userId ;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          barcode.storeList = barcode.storeList.concat(response.data);
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var ShowModalPopup = function (popup) {
      self.model.pagination.data = [];
      if (!!self.model.store.id) {
        if (popup == "item") {
          PopulateAssetCategory();
          var popup = angular.element('#getBarcodeItemList');
          popup.modal('show');
          self.model.batchList = [];
          $scope.itemName = '';
          $scope.itemCode = '';
          $scope.batchNo = '';
        } else if (popup == "grn") {
          var popup = angular.element('#getGRNModal');
          popup.modal('show');
          self.model.NewItemList = [];
        }
      } else {
        growl.error('Please select the store.');
      }
    }

    var PopulateAssetCategory = function () {
      var barcode = self.model;

      var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + "/"+  barcode.assetType + "/" + $scope.unitId + "/" +$scope.organizationId;

      barcode.assetCategory = barcode.assetCategories[0];
      barcode.productCategory = barcode.productCategories[0];

      barcode.assetCategories.splice(1);
      barcode.productCategories.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          barcode.assetCategories = barcode.assetCategories.concat(response.data);
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var PopulateProductCategory = function (assetCategory) {
      var barcode = self.model;

      var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + assetCategory.id + '/'+$scope.organizationId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          barcode.productCategories = barcode.productCategories.concat(response.data);
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }
    $scope.isManualBarcodeRequired=false;
    var PopulateBatchList = function () {
      var barcode = self.model;
      $scope.data = [];
      if($scope.itemName){
        if ($scope.itemName.length < 3){
          self.model.batchList = [];
          if(self.model.batchList.length == 0){            
            PaginationService.searchDataByField(self.model.batchList, self.model.pagination);
            return;
          }
        } 
        if ((!$scope.prevSearch || $scope.prevSearch != $scope.itemName) && ($scope.itemName.length == 3)) {
          $scope.itemListData = [];
          self.model.batchList = [];
          $scope.prevSearch = $scope.itemName;
          var URI = CONSTANTS.PROCUREMENT.QUOTATION.GET_BATCHES_API;
          var data = {
            "assetTypeId": barcode.assetType,
            "assetCategoryId": barcode.assetCategory.id,
            "productCategoryId": barcode.productCategory.id,
            "storeId": barcode.store.id,
            "unitId": $scope.unitId,
            "itemName": $scope.itemName,
            "itemCode": $scope.itemCode,
            "batchNo": $scope.batchNo
          };
          self.model.pagination.data=[];
          self.model.pagination.totalPages=0;
          $rootScope.startLoaderPopup();
          GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            angular.forEach(response.data, function(value){
              if(value.availableQuantity != 0){
                barcode.batchList.push(value);
              }
            });
            angular.forEach( barcode.batchList, function(item){
            	$scope.isManualBarcodeRequired=item.isManualBarcodeRequired;
              $scope.isPrnPrintFlag=item.isPRNRequired;
              if($scope.isPrnPrintFlag==true)
              {
                item["prnCount"]= item.availableQuantity != undefined ? item.availableQuantity : 1;
              }
            	if(item.prePrintedBarcode!='' && item.prePrintedBarcode != undefined)
              {
            		item["prePrintedBarcodeflag"]=true;
              }
            	else
              {
                item["prePrintedBarcodeflag"]=false;
              }
            });
            if(!!response && !!response.data && response.data.length > 0){     
              $scope.itemListData = self.model.batchList;       
              PaginationService.searchDataByField(self.model.batchList, self.model.pagination);
            }
            $rootScope.stopLoaderPopup();
          },
          function (err) {
            $rootScope.stopLoaderPopup();
          });
        }
        else{
          $scope.data = $filter('filter')($scope.itemListData, {
            $: $scope.itemName
          });
          self.model.batchList = $scope.data;
          if(!!self.model.batchList && self.model.batchList.length > 0){            
            PaginationService.searchDataByField(self.model.batchList, self.model.pagination);
          }
        }
      }
      else if($scope.itemCode){
        if ($scope.itemCode.length < 3){
          self.model.batchList = [];
          if(self.model.batchList.length == 0){            
            PaginationService.searchDataByField(self.model.batchList, self.model.pagination);
            return;
          }
        } 
        if ((!$scope.prevSearch || $scope.prevSearch != $scope.itemCode) && ($scope.itemCode.length == 3)) {
          $scope.itemListData = [];
          self.model.batchList = [];
          $scope.prevSearch = $scope.itemCode;
          var URI = CONSTANTS.PROCUREMENT.QUOTATION.GET_BATCHES_API;

          var data = {
            "assetTypeId": barcode.assetType,
            "assetCategoryId": barcode.assetCategory.id,
            "productCategoryId": barcode.productCategory.id,
            "storeId": barcode.store.id,
            "unitId": $scope.unitId,
            "itemName": $scope.itemName,
            "itemCode": $scope.itemCode,
            "batchNo": $scope.batchNo
          };
          self.model.pagination.data=[];
          self.model.pagination.totalPages=0;
          $rootScope.startLoaderPopup();
          GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            angular.forEach(response.data, function(value){
              if(value.availableQuantity != 0){
                barcode.batchList.push(value);
              }
            });
            angular.forEach( barcode.batchList, function(item){
            	$scope.isManualBarcodeRequired=item.isManualBarcodeRequired;
            	$scope.isPrnPrintFlag=item.isPRNRequired;
                if($scope.isPrnPrintFlag==true)
                {
                  item["prnCount"]=1;
                }
            	if(item.prePrintedBarcode!=''&& item.prePrintedBarcode != undefined)
            		{
            		item["prePrintedBarcodeflag"]=true;
            		}
            	else
            		{
            		item["prePrintedBarcodeflag"]=false;
            		}
            });
            if(!!response && !!response.data && response.data.length > 0){    
              $scope.itemListData = self.model.batchList;          
              PaginationService.searchDataByField( self.model.batchList, self.model.pagination);
            }
            $rootScope.stopLoaderPopup();
          },
          function (err) {
            $rootScope.stopLoaderPopup();
          });
        }
        else{
          $scope.data = $filter('filter')($scope.itemListData, {
            $: $scope.itemCode
          });
          self.model.batchList = $scope.data;
          if(!!self.model.batchList && self.model.batchList.length > 0){            
            PaginationService.searchDataByField(self.model.batchList, self.model.pagination);
          }
        }
      }
      else if($scope.batchNo){
        if ($scope.batchNo.length < 3){
          self.model.batchList = [];
          if(self.model.batchList.length == 0){            
            PaginationService.searchDataByField(self.model.batchList, self.model.pagination);
            return;
          }
        } 
        if ((!$scope.prevSearch || $scope.prevSearch != $scope.batchNo) && ($scope.batchNo.length == 3)) {
          $scope.itemListData = [];
          self.model.batchList = [];
          $scope.prevSearch = $scope.batchNo;
          var URI = CONSTANTS.PROCUREMENT.QUOTATION.GET_BATCHES_API;

          var data = {
            "assetTypeId": barcode.assetType,
            "assetCategoryId": barcode.assetCategory.id,
            "productCategoryId": barcode.productCategory.id,
            "storeId": barcode.store.id,
            "unitId": $scope.unitId,
            "itemName": $scope.itemName,
            "itemCode": $scope.itemCode,
            "batchNo": $scope.batchNo
          };
          self.model.pagination.data=[];
          self.model.pagination.totalPages=0;
          $rootScope.startLoaderPopup();
          GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            angular.forEach(response.data, function(value){
              if(value.availableQuantity != 0){
                barcode.batchList.push(value);
              }
            });
            angular.forEach( barcode.batchList, function(item){
              $scope.isManualBarcodeRequired=item.isManualBarcodeRequired;
              $scope.isPrnPrintFlag=item.isPRNRequired;
              if($scope.isPrnPrintFlag==true)
              {
                item["prnCount"]=1;
              }
              if(item.prePrintedBarcode!=''&& item.prePrintedBarcode != undefined)
              {
                item["prePrintedBarcodeflag"]=true;
              }
              else
              {
                item["prePrintedBarcodeflag"]=false;
              }
            });
            if(!!response && !!response.data && response.data.length > 0){        
              $scope.itemListData = self.model.batchList;       
              PaginationService.searchDataByField( self.model.batchList, self.model.pagination);
            }
            $rootScope.stopLoaderPopup();
          },
          function (err) {
            $rootScope.stopLoaderPopup();
          });
        }
        else{
          $scope.data = $filter('filter')($scope.itemListData, {
            $: $scope.batchNo
          });
          self.model.batchList = $scope.data;
          if(!!self.model.batchList && self.model.batchList.length > 0){            
            PaginationService.searchDataByField(self.model.batchList, self.model.pagination);
          }
        }
      }
      else{
        self.model.batchList = [];
        self.model.pagination.data=[];
      }
    }

    var isSelected = function (item) {
      return item.isSelected;
    }

    var SelectAllItems = function () {
      if (self.model.selectAllItems) {
      if(self.model.items.length>0){
          self.model.SelectedItems = self.model.items.map(function (item) {
                item.isSelected = true;
                return item;
          });
      }

        /* start code for resolved HIS-10633 Barcode Printing: All Checkbox not working */
        if(self.model.GRNItem.length>0){
            self.model.SelectedItems = self.model.GRNItem.map(function (item) {
                    item.isSelected = true;
                    return item;
            });
        }
        /* end code for resolved HIS-10633 Barcode Printing: All Checkbox not working */
        
      } else {
        self.model.SelectedItems.splice(0);
        self.model.items.forEach(function (item) {
          item.isSelected = false;
        });
        
        /* start code for resolved HIS-10633 Barcode Printing: All Checkbox not working */
        //self.model.SelectedItems.splice(0);
        self.model.GRNItem.forEach(function (item) {
          item.isSelected = false;
        });
        /* end code for resolved HIS-10633 Barcode Printing: All Checkbox not working */
        
      }
    }
$scope.itemsList=[];
    var SelectItem = function (item) {
      var items = self.model.SelectedItems;
      if (item.isSelected) {
        items.push(item);
      } else {
        var index = items.indexOf(item);
        if (index > -1) {
          items.splice(index, 1);
        }
      }

      if (self.model.items.every(isSelected)) {
        self.model.selectAllItems = true;
      } else {
        self.model.selectAllItems = false;
      }
      $scope.itemsList=items;
      /* start code for resolved HIS-10633 Barcode Printing: All Checkbox not working */
      if(self.model.GRNItem.length>0){
        if (self.model.GRNItem.every(isSelected)) {
            self.model.selectAllItems = true;
        } else {
            self.model.selectAllItems = false;
        }
      }
      /* end code for resolved HIS-10633 Barcode Printing: All Checkbox not working */
      
    }

    var SelectAllBatches = function () {
      if (self.model.selectAllBatches) {
        self.model.SelectedBatches = self.model.batchList.map(function (batch) {
          batch.isSelected = true;
          return batch;
        });
      } else {
        self.model.SelectedBatches.splice(0);
        self.model.batchList.forEach(function (batch) {
          batch.isSelected = false;
        });
      }
    }

    var SelectBatch = function (batch) {
      var batches = self.model.SelectedBatches;

      if (batch.isSelected) {
        batches.push(batch);
      } else {
        var index = batches.indexOf(batch);
        if (index > -1) {
          batches.splice(index, 1);
        }
      }

      if (self.model.batchList.every(isSelected)) {
        self.model.selectAllBatches = true;
      } else {
        self.model.selectAllBatches = false;
      }
    }

    $scope.CancelItems = function(){
    self.model.SelectedBatches.forEach( function (item){
        if(item.isSelected){
            item.isSelected = false;
        }
    });

        self.model.SelectedBatches = [];
    }


    var AddItems = function () {
    if(self.model.SelectedBatches.length==0){
        growl.error("Please Select Item..!");
        return false;
    }
      var batches = [];
      angular.copy(self.model.SelectedBatches, batches);

      self.model.batchList = [];

      batches.forEach(function (batch) {
        batch.isSelected = false;
        var item = $filter('filter')(self.model.items, {
          batchId: batch.batchId
        }, true);

        if (item.length === 0) {
          self.model.items.push(batch);
        }
      });

      self.model.selectAllBatches = false;
      angular.forEach(self.model.SelectedBatches, function(item){
        if(item.isSelected){
            item.isSelected = false;
        }
      });
      self.model.SelectedBatches = [];
       $('#getBarcodeItemList').modal('hide');
    }

    var ShowBarcode = function (item) {
      var URI = CONSTANTS.MASTER.UNIT.CONSUMABLE_BARCODE_CONFIG.CONSUMABLE_BARCODE_CONFIG_GET_API + $scope.unitId;
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
        function (response) {        
            angular.forEach(response.data, function(barcode){
                $scope.id = barcode.id;
                if(barcode.itemName == true){
                    self.model.isItemName = true;
                    $scope.itemName = item.itemName;
                }
                if(barcode.itemCode == true){
                    self.model.isItemCode = true;
                    $scope.itemCode = item.itemCode;
                }
                if(barcode.batchNo == true){
                    self.model.isBatchNo = true;
                    $scope.batchNo = item.batchNo;
                }
                if(barcode.expiryDate == true){
                    self.model.isExpiryDate = true;
                   // $scope.expiryDate = item.expiryDate;
                    $scope.expiryDate = moment(item.expiry).format('MM/YY');
                }
                if(barcode.lotNo == true){
                    self.model.isLotNo = true;
                    $scope.lotNo = item.lotNo;
                }
                if(barcode.mrp == true){
                    self.model.isMRP = true;
                    $scope.mrp = item.mrp;
                }
                if(barcode.salePrice == true){
                    self.model.isSalesPrice = true;
                    $scope.salePrice = item.salePrice;
                }
                if(barcode.grnNo == true){
                    self.model.isGrnNo = true;
                    $scope.grnNo = item.grnNo;
                }
                if(barcode.vendorName == true){
                    self.model.isVendorName = true;
                    $scope.vendorName = item.vendorName;
                }
                if(barcode.vendorCode == true){
                    self.model.isVendorCode = true;
                    $scope.vendorCode = item.vendorCode;
                }
                if(barcode.hospitalName == true){
                    self.model.isHopspitalName = true;
                    $scope.hospitalName = item.hospitalName
                }
            });
            self.model.barcode = {
                type: "code128",
                code:item.batchId.toString(),
               // code: "156517 2386",
                hospitalName: $scope.hospitalName,
                itemName: $scope.itemName,
                itemCode: $scope.itemCode,
                batchNo: $scope.batchNo,
                lotNo: $scope.lotNo,
                expiry: $scope.expiryDate,
                mrp: $filter('number')($scope.mrp!=undefined ? $scope.mrp :0.0, 2),
                salePrice: $scope.salePrice,
                grnNo: $scope.grnNo,
                vendorCode: $scope.vendorCode,
                vendorName: $scope.vendorName
            }
            var popup = angular.element('#barcodePreview');
            popup.modal('show');
            $rootScope.stopLoader();
        },
        function errorCallback(response) {
            $rootScope.stopLoader();
        });
    }
    $scope.ConfirmRemove = function (index, pendingIssuesDetails, parentindex) {
    	self.model.items.splice(index, 1);
    }
    
    $scope.ConfirmRemoveGrn = function (index, pendingIssuesDetails, parentindex) {
    	self.model.GRNItem.splice(index, 1);
    }
    
    $scope.globalPrePrintedBarcode=[];
    $scope.getPreBarcode=function()
    {
    	 var data = {
 	            
 	            "unitId": $scope.unitId,
 	            "itemName": $scope.itemName,
 	            "itemCode": $scope.itemCode,
 	            "batchNo": $scope.batchNo,
 	            "items":[]
 	          };
 	 
 	var URI = CONSTANTS.PROCUREMENT.QUOTATION.GET_BATCHES_API+"/prePrintedBarcode/search";
     $rootScope.startLoader();
     GenericService.serviceAction("POST", URI, data).then(
     function (response) {        
     	$rootScope.stopLoader();
     	$scope.globalPrePrintedBarcode=response.data;     	 
     });
    }
    $scope.getPreBarcode();
    $scope.getPreBarcode1=function(item)
    {
    	angular.forEach($scope.globalPrePrintedBarcode, function(item1){
    		var preGeneratedBarcode=item1.itemId+" "+item1.batchId;
    		if(item1.prePrintedBarcode==item.prePrintedBarcode && item1.batchId != item.batchId && item1.prePrintedBarcode!=""  )
    			{
    			growl.error("This Barcode Number Already exist");
    			item.prePrintedBarcode="";
    			return false
    			}
    		if(preGeneratedBarcode==item.prePrintedBarcode)
			{
			growl.error("This Barcode Number Already exist");
			item.prePrintedBarcode="";
			return false
			}
    	})
    }
    $scope.editButtonFlag=false;
    $scope.savePrePrintedBarcodes=function(item)
    {
    	if(item.prePrintedBarcode=="")
			{
			growl.error("The Barcode Number Should Not Be Blank");
			return false
			}
    	$scope.getPreBarcode1(item);
    	var barcode=self.model;
    	 var data = {
    	            "assetTypeId": barcode.assetType,
//    	            "assetCategoryId": barcode.assetCategory.id,
//    	            "productCategoryId": barcode.productCategory.id,
    	            "storeId": barcode.store.id,
    	            "unitId": $scope.unitId,
    	            "itemName": $scope.itemName,
    	            "itemCode": $scope.itemCode,
    	            "batchNo": $scope.batchNo,
    	            "items":[]
    	          };
    	 
    		 var dataitems={
    				 "itemId":item.itemId,
    				 "batchId":item.batchId,
    				 "prePrintedBarcode":item.prePrintedBarcode!="" ? item.prePrintedBarcode : "",
    		 }
    		 item.isSelected=false;
    		 data.items.push(dataitems);
    		 var URI = CONSTANTS.PROCUREMENT.QUOTATION.GET_BATCHES_API+"/prePrintedBarcode/save";
    		 if($scope.editButtonFlag==true)
    			 {
    		        $rootScope.startLoader();
    		        GenericService.serviceAction("POST", URI, data).then(
    		        function (response) { 
    		        	if(response.status==200)
    		        		{
    		        		growl.success("Pre Printed Barcode Saved Successfully");
    		        		$scope.getPreBarcode();
    		        		}
    		        	$rootScope.stopLoader();
    		        	$scope.editButtonFlag=false;
    		        	if(item.prePrintedBarcode!="")
    	    			 {
    	    			 item.prePrintedBarcodeflag=true;
    	    			 }else
    	    				 {
    	    				 item.prePrintedBarcodeflag=false;
    	    				 }
//    		        	 $timeout(function () {
//    		        		 $window.location.reload();
//    		        	      }, 200);
    		        });
    			 }
    		 else{
    			 if(item.prePrintedBarcode=="")
    	 			{
    	 			growl.error("The Barcode Number Should Not Be Blank");
    	 			return false
    	 			}
    	 if(item.prePrintedBarcode!=""){
        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
        function (response) { 
        	if(response.status==200)
        		{
        		growl.success("Pre Printed Barcode Saved Successfully");
        		$scope.getPreBarcode();
        		}
        	$rootScope.stopLoader();
        	$scope.editButtonFlag=false;
        	item.prePrintedBarcodeflag=true;
//        	 $timeout(function () {
//        		 $window.location.reload();
//        	      }, 200);
        });
    	 }
    }
    }
    $scope.ShowPrePrintedBarcode = function (item) {
        var URI = CONSTANTS.MASTER.UNIT.CONSUMABLE_BARCODE_CONFIG.CONSUMABLE_BARCODE_CONFIG_GET_API + $scope.unitId;
          $rootScope.startLoader();
          GenericService.serviceAction("GET", URI, {}).then(
          function (response) {        
              angular.forEach(response.data, function(barcode){
                  $scope.id = barcode.id;
                  if(barcode.itemName == true){
                      self.model.isItemName = true;
                      $scope.itemName = item.itemName;
                  }
                  if(barcode.itemCode == true){
                      self.model.isItemCode = true;
                      $scope.itemCode = item.itemCode;
                  }
                  if(barcode.batchNo == true){
                      self.model.isBatchNo = true;
                      $scope.batchNo = item.batchNo;
                  }
                  if(barcode.expiryDate == true){
                      self.model.isExpiryDate = true;
                     // $scope.expiryDate = item.expiryDate;
                      $scope.expiryDate = moment(item.expiry).format('MM/YY');
                  }
                  if(barcode.lotNo == true){
                      self.model.isLotNo = true;
                      $scope.lotNo = item.lotNo;
                  }
                  if(barcode.mrp == true){
                      self.model.isMRP = true;
                      $scope.mrp = item.mrp;
                  }
                  if(barcode.salePrice == true){
                      self.model.isSalesPrice = true;
                      $scope.salePrice = item.salePrice;
                  }
                  if(barcode.grnNo == true){
                      self.model.isGrnNo = true;
                      $scope.grnNo = item.grnNo;
                  }
                  if(barcode.vendorName == true){
                      self.model.isVendorName = true;
                      $scope.vendorName = item.vendorName;
                  }
                  if(barcode.vendorCode == true){
                      self.model.isVendorCode = true;
                      $scope.vendorCode = item.vendorCode;
                  }
                  if(barcode.hospitalName == true){
                      self.model.isHopspitalName = true;
                      $scope.hospitalName = item.hospitalName
                  }
              });
              self.model.barcode = {
                  type: "code128",
                  code: item.prePrintedBarcode,
                 // code: "156517 2386",
                  hospitalName: $scope.hospitalName,
                  itemName: $scope.itemName,
                  itemCode: $scope.itemCode,
                  batchNo: $scope.batchNo,
                  lotNo: $scope.lotNo,
                  expiry: $scope.expiryDate,
                  mrp: $filter('number')($scope.mrp!=undefined ? $scope.mrp :0.0, 2),
                  salePrice: $scope.salePrice,
                  grnNo: $scope.grnNo,
                  vendorCode: $scope.vendorCode,
                  vendorName: $scope.vendorName
              }
              var popup = angular.element('#barcodePreview');
              popup.modal('show');
              $rootScope.stopLoader();
          },
          function errorCallback(response) {
              $rootScope.stopLoader();
          });
      }

    $scope.printRadiologyReport=function(barcodePrintingList,printWithHeader){

        angular.forEach(barcodePrintingList, function(info){
        if(info.bonusBatchFlag){
        			info.grnId = info.grnId != undefined ? info.grnId : 0 ;
        			info.bonusBatchFlag = true;
        		}else{
        			info.bonusBatchFlag = false;
        			info.barcodeScreen = info.barcodeScreen != undefined ? info.barcodeScreen:false;
        			info.grnId = info.grnId != undefined ? info.grnId : 0 ;
        		}
            	info.organizationId = $scope.organizationId;
            	info.unitId =  $scope.unitId;
        });

//    	info.printWithHeader = printWithHeader;
    	var URI = BASE_URL + ROOT_URL + "/LIS/transaction/barcodePrinting";
    	GenericService.serviceAction(METHOD_POST, URI, barcodePrintingList,NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
				{
	
			   if(response.data.status=='success')
			      {
				     self.model.selectAllItems = false;
				     self.model.barcodeList=[];
                     angular.forEach(self.model.SelectedItems, function(item){
                        if(item.isSelected){
                            item.isSelected = false;
                        }
                     });
                   self.model.SelectedItems = [];
                   self.model.itemnew = [];
                   $scope.barcodePrintingList = [];
                   $scope.listForBarcodes = [];
				   if($scope.isPRNPrint!=undefined && $scope.isPRNPrint!=null && $scope.isPRNPrint==true && $scope.prnFormat!=null){
						//JSPrinterManager call
//						print($scope.finalPRN);
						//For PRN Print manager
							$scope.printPRNPrint(response.data.listObject);
					}														

			      }
			   else if(response.data.status=='error'){
				   $rootScope.stopLoader();
			   }
		});
	
    }
    var labConfig  =    $scope.checkLabConfig();
    $scope.prnUrl = labConfig.prnUrl;

    $scope.printPRNPrint= function(list){
        var listForUpdateCount = [];

        angular.forEach(list, function (item, index){
            if(item.templateDesc != ""){
                var newItem={};
                newItem.batchId = item.batchId;
                newItem.printedCount = parseInt(item.prnCount);
                listForUpdateCount.push(newItem);
            }
        });

        var URI = CONSTANTS.PROCUREMENT.QUOTATION.GET_BATCHES_API + "/updatePrintedCount";
            GenericService.serviceAction(METHOD_POST, URI, listForUpdateCount).then(function(response) {

            if(response.data.status = "success"){

                if(self.model.items != undefined && self.model.items.length>0){
                    angular.forEach(self.model.items, function(item, index){
                        angular.forEach(response.data.listObject, function(resItem){
                           if(item.batchId == resItem.batchId && item.batchNo == resItem.batchNo){
                              item.printedCount = resItem.printedCount;
                           }
                        });
                    });
               }
                else if(self.model.GRNItem != undefined && self.model.GRNItem.length>0){
                    angular.forEach(self.model.GRNItem, function(item){
                        angular.forEach(response.data.listObject, function(resItem){
                            if(item.batchId == resItem.batchId && item.batchNo == resItem.batchNo){
                               item.printedCount = resItem.printedCount;
                            }
                        });
                    });
                }
                angular.forEach(list, function(item){
                    console.log(item.templateDesc);
                    $timeout(function () {
                        var url = $scope.prnUrl;

                            var xhr = new XMLHttpRequest();
                            xhr.open("POST", url);

                            xhr.setRequestHeader("Content-Type", "text/plain");

                            xhr.onreadystatechange = function () {
                            if (xhr.readyState === 4) {
                                console.log(xhr.status);
                                console.log(xhr.responseText);
                            }};

                            var data = item.templateDesc;
                            xhr.send(data);

                    }, 3000);
                })
            }
        });
    }

   $scope.barcodePrintingList = [];
    var PrintBarcode = function (item) {
    	if(item.isPRNRequired==true && item.pharmacyPRNFormat!=null && item.pharmacyPRNFormat!="")
    		{
    		$scope.isPRNPrint=item.isPRNRequired;
    		$scope.prnFormat=item.pharmacyPRNFormat;
    		$scope.barcodeScreen = item.barcodeScreen != undefined ? item.barcodeScreen: false;
            $scope.grnId = item.bonusBatchExpiryId1 != undefined ? item.bonusBatchExpiryId1 : null;

            var printCount = item.prnCount != undefined ? item.prnCount : item.prints
            if(printCount == 0 || printCount== undefined){
            	growl.error('No. of prints must be greater than Zero.');growl.error();
            	$scope.barcodePrintingList=[];
            	return false;
            }

    		var info={
					batchId:item.batchId,
					templateDesc:item.pharmacyPRNFormat,
					prnCount:printCount
	           }
    		$scope.barcodePrintingList.push(info);
    		if($scope.listForBarcodes != undefined && $scope.listForBarcodes.length > 0){
                if( $scope.listForBarcodes.length==$scope.barcodePrintingList.length){
            	    $scope.printRadiologyReport($scope.barcodePrintingList,null);
                }
            }
            else{
                    $scope.printRadiologyReport($scope.barcodePrintingList,null);
            }
    		}
    	else
    		{
    		self.model.barcode = {
    		        type: "code128",
    		        code:item.batchId.toString(),
    		        hospitalName: item.hospitalName,
    		        itemName: item.itemName,
    		        itemCode: item.itemCode,
    		        batchNo: item.batchNo,
    		        lotNo: item.lotNo,
    		        expiry: moment(item.expiry).format('MM/YY'),
    		        mrp: $filter('number')(item.mrp!=undefined ? item.mrp :0.0, 2),
    		        salePrice: $scope.salePrice,
    		        grnNo: item.grnNo,
    		        vendorCode: item.vendorCode,
    		        vendorName: item.vendorName
    		      }
    		      $timeout(function () {
    		        var printContents = $window.document.getElementById('barcode').innerHTML;
    		        var popupWin = $window.open('', '_blank', 'width=300,height=400');
    		        popupWin.document.open();
    		        popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()" style="height: 150px;">' + printContents + '</body></html>');
    		        popupWin.document.close();
    		      }, 100);
    		}
    }
    $scope.PrintPrePrintedBarcode = function (item) {
        self.model.barcode = {
          type: "code128",
          code: item.prePrintedBarcode,
          hospitalName: item.hospitalName,
          itemName: item.itemName,
          itemCode: item.itemCode,
          batchNo: item.batchNo,
          lotNo: item.lotNo,
          expiry: moment(item.expiry).format('MM/YY'),
          mrp: $filter('number')(item.mrp!=undefined ? item.mrp :0.0, 2),
          salePrice: item.salePrice,
          grnNo: item.grnNo,
          vendorCode: item.vendorCode,
          vendorName: item.vendorName
        }
        $timeout(function () {
            var printContents = $window.document.getElementById('barcode').innerHTML;
            var popupWin = $window.open('', '_blank', 'width=300,height=400');
            popupWin.document.open();
            popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()" style="height: 150px;">' + printContents + '</body></html>');
            popupWin.document.close();
          }, 100);
    }
    $scope.editFlag=false;
    $scope.editRecords = function (item) {
    	$scope.editFlag=true;
    	$scope.editButtonFlag=true;
    		if($scope.editFlag==true)
    			{
    			item.prePrintedBarcodeflag=false;
    			}
    }
    //search  vendor auto populate
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

    function AddVendorName(vendor) {
      self.model.vendorName = vendor.vendorName;
      self.model.vendorId = vendor.id;
    }


    var OpenFromDate = function () {
      self.model.fromDateOpened = true;
      self.model.toDateOptions.minDate = self.model.fromDate;
    }

    var OpenToDate = function () {
      self.model.toDateOpened = true;
      self.model.toDateOptions.minDate = self.model.fromDate;
    }


    var SelectAllGRNItems = function (newItem,id,flag) {
    	
      if (newItem.selectAllRows) {
          self.model.SelectedItemsIds = newItem.items.map(function (item) {

              item.isSelected = true;
              self.model.isItemChecked = true;
              return item.itemId;
          });
      } else  {
          newItem.SelectedItemsIds = [];
          newItem.items.forEach(function (item) {

              item.isSelected = false;
              self.model.isItemChecked = false;
              newItem.selectAllRows = false;
          });
      }
      
      self.model.grnItem = newItem;

      var grnItemList = [];
      angular.copy(self.model.itemnew, grnItemList);

      newItem.items.forEach(function (item) {
        if(item.isSelected){
//            $scope.selectedIds.push(item.itemId);
            self.model.itemnew.push(item);
        }
        else {

            for (var i = 0; i<self.model.itemnew.length; i++) {
                if(item.itemId === self.model.itemnew[i].itemId){
                    self.model.itemnew.splice(i, 1);
                    break;
                }
            }
        }

      });
  }

    	$scope.grnItems = [];
   var SelectGRNItem = function (item, newItem) {
    var grnItemList =  $scope.grnItems;

    if (item.isSelected) {
       grnItemList.push(item);
    }
    else {
       var index = grnItemList.indexOf(item);
       if (index > -1) {
          grnItemList.splice(index, 1);
       }
    }
      if (newItem.items.every(isSelected)) {
          newItem.selectAllRows = true;
      } else {
          newItem.selectAllRows = false;
      }
      self.model.itemnew = $scope.grnItems;
  }


  function getAllGRNItemDetails() {
    if(self.model.itemnew.length == 0){
        growl.error('Please select the items.!');
        return false;
    }
     var grnItemList = [];
     var bonusBatches = [];
     angular.copy(self.model.itemnew, grnItemList);

       grnItemList.forEach(function (item) {
         item.isSelected = false;
         var boolean = $filter('filter')(self.model.GRNItem, {
         grnDetailId: item.grnDetailId
         }, true);

         if (boolean.length === 0) {
            self.model.GRNItem.push(item);
         }
         else if(item.bonusBatchExpiryId!=undefined){
            bonusBatches.push(item);
         }
       });

       bonusBatches.forEach(function (batchItem){

       var boolean = $filter('filter')(self.model.GRNItem, {
           bonusBatchExpiryId: batchItem.bonusBatchExpiryId
       }, true);
        if (boolean.length === 0) {
           self.model.GRNItem.push(batchItem);
        }

       });
       bonusBatches = [];
       self.model.itemnew.splice(0);
       $scope.selectedIds = [];
        $('#getGRNModal').modal('hide');
}
  
  /*-------Batchwise barcode printing start -----------*/
  
  $scope.GRNItemsForBarcode = [];
  $scope.printCounts = 0;
  $scope.grnBonusBatchExpiry = [];
  $scope.bonusBatchFlag = false;
  $scope.barcodeScreen = false;
  $scope.grnId = 0;
  
  $scope.getItemsForBarcode = function(newItem){
	  
	  $scope.printCounts = newItem.printCount;
	  $scope.PRNRequired = newItem.isPRNRequired;
      $scope.pharmacyPRNFormat = newItem.pharmacyPRNFormat;
	  
	  self.model.itemnew.forEach(function (item) {
	        if (item.isSelected == true) {
	        	
	        	$scope.GRNItemsForBarcode.push(item);
	        }
	    });
	  $scope.openMultiplePopUp(newItem);
  }
  
  $scope.openMultiplePopUp = function(newItem){
	  if($scope.GRNItemsForBarcode.length>0){
		  $scope.GRNItemsForBarcode.forEach(function (item) {

		  if($scope.PRNRequired==true && $scope.pharmacyPRNFormat!=null && $scope.pharmacyPRNFormat!="")
    		{
			  if(item.prints == undefined || item.prints == 0){
				  item.prints = 1;
			  }
			  else{
				  
				   $scope.printCounts = item.prints != undefined ? item.prints:item.prnCount;
			  }
			  $scope.bonusBatchFlag = false;
    		  $scope.isPRNPrint=$scope.PRNRequired;
    		  $scope.prnFormat=$scope.pharmacyPRNFormat;

			  $scope.barcodeScreen = false;
			  if(item.bonusBatchExpiryId != undefined){
				  item.barcodeScreen = true;
				  item.bonusBatchFlag = true;
				  item.grnId = item.bonusBatchExpiryId;
			  }

			  item.templateDesc=$scope.prnFormat;
			  item.prnCount=item.prints;
    		}
	  });
        self.model.NewItemList =  $scope.itemList;
        $scope.data = [];
        $scope.itemList.forEach(function (item){
        	 item.items.forEach(function(dataBarcode){
        		 if(dataBarcode.isSelected == true){
        			 var newData = angular.copy(dataBarcode);
                     newData.multidelete("availableQuantity","batchNo","cop","expiry","grnDate","itemId","itemCode","itemName","itemDetails",
         			  "isThreeUOMApplicable","uomTypeId","uomType","uomUnitId","uomUnit","mrp","lotNo","taxId","taxName","taxPercentage",
         			  "vendorId","vendor","markupStatus","grnDetailId","pendingQuantity","storeId","hospitalName","isManualBarcodeRequired",
         			  "isApprovalPending","store","grnNo","grnDate","prePrintedBarcodeflag","pharmacyPRNFormat","isPRNRequired","isSelected",
         			  "prints","printedCount","bonusBatchExpiryId1");
                     $scope.data.push(newData);
        		 }
     	    })
        })  
        $scope.printRadiologyReport($scope.data,null);
		$scope.GRNItemsForBarcode = [];
		self.model.NewItemList.forEach(function(item){
  			if(newItem.id == item.id){
  				item.selectAllRows = false; 	
  				item.items.forEach(function(data){
  					data.isSelected = false;
  				})
  			}
  		});
		$scope.grnItems = [];
  	} else{
  		self.model.NewItemList.forEach(function(item){
  			if(newItem.id == item.id){
  				if(!item.selectAllRows){
  	  				growl.error('Please select the items.!');
  				}
  			}
  		});
  	}
  }
  
  
  /*-------Batchwise barcode printing end-----------*/
  
function searchNewGRN() {

  var URI = CONSTANTS.GLOBAL.GRN_API_FOR_BARCODE;
  const data = {
      "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
      "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
      "storeId": self.model.store.id,
      "vendorId": self.model.vendorId,
      "unitId":$scope.unitId,
      "grnNo": self.model.grnNo,
      "assetTypeID":self.model.assetType
  }
  self.model.NewItemList = [];
  self.model.NewItemList1= [];
  $scope.uniqueItems = [];
  $scope.uniqueItems1 = [];
  $scope.bonusItems = [];
  $scope.bonusItems1 = [];
  
  $rootScope.startLoader();
  GenericService.serviceAction("POST", URI, data).then(
      function (response) {
          $rootScope.stopLoader();
          
          self.model.NewItemList1 = response.data;
          self.model.NewItemList1.forEach(function (value) {
                 	  
 			 self.model.uniqueItems = value.items;
             self.model.uniqueItems.isPRNRequired = value.isPRNRequired != undefined ? value.isPRNRequired : false;
             self.model.uniqueItems.pharmacyPRNFormat = value.pharmacyPRNFormat != undefined ? value.pharmacyPRNFormat : "";
             $scope.isManualBarcodeRequired = value.isManualBarcodeRequired != undefined ? value.isManualBarcodeRequired : false;

 			 self.model.uniqueItems.forEach(function (item){
 				item.grnNo = value.grnNo;
 				item.grnDate = value.grnDate;
                if(item.prePrintedBarcode!=''&& item.prePrintedBarcode != undefined)
                {
                   item["prePrintedBarcodeflag"]=true;
                }
                else
                {
                   item["prePrintedBarcodeflag"]=false;
                }
 				if(item.bonusBatchExpiryId != undefined)
 					{
 						$scope.bonusItems.push(item);
 					}
 			 });
 			$scope.uniqueItems1 = self.model.uniqueItems;
			  
 			 $scope.bonusItems.forEach(function (rem1){
 			  self.model.uniqueItems.forEach(function (rem, index){
 				if(rem.bonusBatchExpiryId == undefined && rem.batchNo == rem1.batchNo && rem.expiry == rem1.expiry) 
					{
 						rem.availableQuantity = rem1.availableQuantity + rem.availableQuantity;
						$scope.bonusItems1.push(rem1);
						rem.barcodeScreen = true;
                        rem.bonusBatchExpiryId1 = rem1.bonusBatchExpiryId;
						
					}
	 			});
 			});

			  $scope.bonusItems1.forEach(function (rem1){
 			  $scope.uniqueItems1.forEach(function (rem, index){
 				if(rem.bonusBatchExpiryId != undefined && rem.batchNo == rem1.batchNo && rem.expiry == rem1.expiry) 
					{
						self.model.uniqueItems.splice(index, 1);
 						
					}
	 			});
 			});

 			self.model.uniqueItems.forEach(function (item){

             	item.prints = item.availableQuantity =! undefined ? item.availableQuantity : 0;
             	item.pharmacyPRNFormat = self.model.uniqueItems.pharmacyPRNFormat;
                item.isPRNRequired = self.model.uniqueItems.isPRNRequired;

            });
			 			 
 	         value.items = self.model.uniqueItems;
 			 $scope.uniqueItems1 = [];
 			  $scope.uniqueItems = [];
           });
            self.model.NewItemList =  self.model.NewItemList1;
            $scope.itemList = self.model.NewItemList; 
      },
      function errorCallback(response) {
          $rootScope.stopLoader();
          growl.error('Error');
      });
}



var Expand = function (item) {
  item.Expanded = !item.Expanded;
}

var clearList = function(){
  self.model.items = [];
  self.model.itemnew =[];
  $scope.grnItems = [];
//  self.model.barcodeList = [];
  self.model.SelectedItems= [];
  
  /* start code for resolved HIS-10633 Barcode Printing: All Checkbox not working */
  self.model.GRNItem =[];
  self.model.selectAllItems = false;
  /* end code for resolved HIS-10633 Barcode Printing: All Checkbox not working */
  

}
var printAllBarCodes = function()
{
  var printContents = $window.document.getElementById('barcodePrintingId').innerHTML;
    var popupWin = $window.open('', '_blank', 'width=300,height=400');
    popupWin.document.open();
    popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()">' + printContents + '</body></html>');
    popupWin.document.close();
}

var printSelectedBarcodes= function(){
	self.model.barcodeList = [];

	if(self.model.SelectedItems.length > 0){

        angular.forEach(self.model.SelectedItems, function (item, key) {

		    if(item.isSelected==true) {

	            self.model.barcode  = {};
	            self.model.barcode.prints = parseInt(item.prints != undefined ? item.prints : item.prnCount);
                self.model.barcode.batchId = item.batchId;
                self.model.barcode.isPRNRequired = item.isPRNRequired != undefined ? item.isPRNRequired : false;
                self.model.barcode.pharmacyPRNFormat = item.pharmacyPRNFormat != undefined ? item.pharmacyPRNFormat : "";
	            self.model.barcodeList.push(self.model.barcode);

	        }
        });
	}
	else{
        angular.forEach(self.model.items, function (item, key) {
    	
            if(item.isSelected==true) {
                self.model.barcode  = {};
            	self.model.barcode.type= "code128";
            	self.model.barcode.code= item.itemId.toString() + " " + item.batchId.toString();
            	self.model.barcode.hospitalName= item.hospitalName;
            	self.model.barcode.itemCode= item.itemCode;
            	self.model.barcode.batchNo= item.batchNo;
            	self.model.barcode.expiry= moment(item.expiry).format('MM/YY');
            	self.model.barcode.mrp= $filter('number')(item.mrp, 2);
            	self.model.barcodeList.push(self.model.barcode);
            }

        });
    }
	console.log('$scope.barcodeList',JSON.stringify(self.model.barcodeList));
	if(self.model.barcodeList.length > 0){
	    $('#sampleBarCode').modal('show');
	}
	else{
	    growl.error('Please select the items to print.');
	}

}


var PrintBarcodeOnBarcodeCilck = function () {
    self.model.barcode = {
      type: "code128",
      code: self.model.barcode.code,
      hospitalName: $scope.hospitalName,
      itemCode: $scope.itemCode,
      itemName: $scope.itemName,
      batchNo: $scope.batchNo,
      batchCode: $scope.batchCode,
      expiry: $scope.expiryDate,
      mrp: $scope.mrp,
      lotNo: $scope.lotNo,
      salePrice: $scope.salePrice,
      grnNo: $scope.grnNo,
      vendorName: $scope.vendorName,
      vendorCode: $scope.vendorCode
    }

    $timeout(function () {
      var printContents = $window.document.getElementById('barcode').innerHTML;
      var popupWin = $window.open('', '_blank', 'width=300,height=400');
      popupWin.document.open();
      popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()">' + printContents + '</body></html>');
      popupWin.document.close();
    }, 100);
  }

    $scope.listForBarcodes = [];
  $scope.getMultipleItemsForBarcode = function(barcodeList){

    barcodeList.forEach(function (item) {
        item.printCount = item.prnCount != undefined ? item.prnCount : item.prints
        if(item.printCount == 0 || item.printCount == undefined){
            growl.error('No. of prints must be greater than Zero.');growl.error();
            $scope.listForBarcodes = [];
            return false;
        }
    });
    $scope.listForBarcodes = barcodeList;
    barcodeList.forEach(function (item) {
        item.prnCount = parseInt(item.prints != undefined ? item.prints:item.prnCount);
        self.model.PrintBarcode(item);
    });
    ;
  }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("barcodePrinting", {
      url: "/barcodePrinting",
      templateUrl: 'views/procurement/quotation/barcode-printing.html',
      controller: "BarcodePrinting.Controller",
      title: 'Barcode Printing',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 183);
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
    .controller("BarcodePrinting.Controller", barcodePrintingController);
})();