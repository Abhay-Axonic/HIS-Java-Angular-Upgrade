(function () {
    "use strict";

    function uploadStockDetailsController($scope, $rootScope, $http, commonDetailService, CurrencyService,  CONSTANTS, $state, GenericService, growl,BillingGenericService,reportService,$cookies,fileReader,GenericServiceFileUpload) { 
        var self = this;
        var uploadId;
        var stopTransactionId;
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;
            self.model = {
                stores: [{
                    "store": "Select Store"
                }],                
                startDateTime: new Date(),
                startDateTimeOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                   // maxDate: new Date() 
                },

                stopDateTime: new Date(),
                stopDateTimeOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                  //  maxDate: new Date() 
                },              
              

            };
            
            $scope.filepreview = "";
			// file upload object 
			
			$scope.fileObj={
					csvfile:'',
			}
			
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })
                     
            self.model.OpenStopDateTime = OpenStopDateTime;
            self.model.OpenStartDateTime = OpenStartDateTime;
            self.model.dateTimeCheck = dateTimeCheck;
            self.model.ActiveTab = 1;
            self.model.PopulateDropdown = PopulateDropdown;
            self.model.searchNewItem = searchNewItem;
            self.model.AddItems = AddItems;         
            self.model.openAddItem = openAddItem;           
            self.model.SelectedItemsIds = [];
            self.model.itemDetailList = [];
            self.model.SaveNewUploadStock = SaveNewUploadStock;
            self.model.NavigateToBack = NavigateToBack;
            self.model.SaveUploadStock = SaveUploadStock;
            self.model.UpdateUploadStock = UpdateUploadStock;
            self.model.validateSave = validateSave;
            self.model.ValidateQuantity = ValidateQuantity;
            self.model.clearList = clearList;
            self.model.openUploadStockNew  = openUploadStockNew;
                 
            self.model.GetBatch = GetBatch;             
            self.model.itemDetailListOldBatch = [];
            self.model.getPreview = getPreview;             
            self.model.getPreviewOldBatch = getPreviewOldBatch; 
            
            PopulateDropdown();

        }               
        
        var OpenStartDateTime = function () {
            self.model.startDateOpened = true;
            self.model.stopDateTimeOptions.minDate = self.model.startDateTime;
        }

        var OpenStopDateTime = function () {
            self.model.stopDateOpened = true;
            self.model.stopDateTimeOptions.minDate = self.model.startDateTime;
        }

        var dateTimeCheck = function (data) {
            if (!data) {
                growl.error('Enter Valid Date and Time');
                return;
            }
        }
        var PopulateDropdown = function () {
            var upload = self.model;

            var URI = CONSTANTS.GLOBAL.USER_STORE_API + "/"+$scope.organizationId + "/"+$scope.unitId+ "/"+$scope.userId ;
            upload.store = upload.stores[0];

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    upload.stores = upload.stores.concat(response.data);
                    try{
                        var selectedTopStore = JSON.parse(sessionStorage.getItem('selectedTopStore'));
            		 	$scope.storeId = selectedTopStore.storeId;
            		 	self.model.store  =upload.stores.filter(x=>x.id==$scope.storeId)[0];            		 
                    }catch (e) {
                        console.log("error!",e);          
                    }                     
                    $rootScope.stopLoader();
                    var uploadData = commonDetailService.getDataId();
                    
                  if(uploadData !=null){
                    uploadId = uploadData.id;
                    self.model.isDetail = uploadData.isDetail;
                    $rootScope.stopLoader();
                    if(uploadData.isDetail){
                    if (!!uploadId) {
                        commonDetailService.setDataId(null);
                        GetUploadStockDetails(uploadId);
                    } else {
                        return false;
                    }
                    }
                  }
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }
        $scope.statusId=1;
        var GetUploadStockDetails = function (uploadId) {
            // var opbId = openingBalanceService.getOpeningBalanceId();
            var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.UPLOAD_STOCK.DETAIL_API + uploadId + "/" + $scope.unitId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.uploadId = response.data.id;
                    self.model.startDateTime = response.data.requestStartDate;
                    self.model.stopDateTime = response.data.requestStopDate;
                   $scope.statusId= response.data.statusId;
                    self.model.store = self.model.stores.find(function (upload) {
                        return response.data.storeId == upload.id;
                        //self.model.store.id = response.data.storeId;
                    });

//                  self.model.itemDetailList = response.data.items;
                    self.model.itemDetailList = [];
                    self.model.itemDetailListOldBatch=[]; 

                    var CurrentBatchItems = response.data.items.filter(function(e) { return e.quantity > 0 })                     
                    var oldBatchItems = response.data.items.filter(function(e) { return e.quantity == 0 }) 

                   self.model.itemDetailList = CurrentBatchItems;
                   self.model.itemDetailListOldBatch = oldBatchItems; 
                    
                    
                    self.model.requestNo = response.data.requestNo;
                    self.model.itemDetailList.forEach(function (item) {
                        item.mrp = item.mrp.toFixed(2);
                        item.cop = item.cop.toFixed(2);
                        stopTransactionId = item.stopTransactionId;
                    })
                    self.model.itemDetailListOldBatch.forEach(function (item) {
                        item.mrp = item.mrp.toFixed(2);
                        item.cop = item.cop.toFixed(2);
                        stopTransactionId = item.stopTransactionId;
                    }) 
                    
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var openAddItem = function () {
            if (self.model.itemDetailList.length > 0) {
                $("#uploadStock").modal()
            }
            else {
                self.model.selectedItem = '';
                $("#getRequestModal").modal();
                searchNewItem();
            }
        }
        
        var openUploadStockNew = function () {
        	  $("#uploadStockNew").modal();
        }

        function searchNewItem() {

            var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.UPLOAD_STOCK.GET_REQUEST;

            const data = {
                "fromDate": moment(self.model.startDateTime).format('YYYY-MM-DD'),
                "toDate": moment(self.model.stopDateTime).format('YYYY-MM-DD'),
                "storeId": self.model.store.id,
                "unitId": $scope.unitId
            }
            self.model.searchNewItemList = [];
            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    self.model.searchNewItemList = response.data;
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                    growl.error('Error');
                });
        }


        var clearList = function () {
            self.model.itemDetailList = [];
            self.model.itemDetailListOldBatch = [];
            self.model.requestNo = '';
            self.model.selectedItem = '';
            $("#getRequestModal").modal();
            searchNewItem();
        }    
      
        
        var getPreview = function (){		
			$scope.finalArr = [];
			if(self.model.stopTransactionId == undefined)
					self.model.stopTransactionId = stopTransactionId;
			var actionNameRPT = "get_StopTransactionRequest_list.rpt";
			$scope.finalArr=[
					{paramName: "store_id", paramType: "integer", paramValue: self.model.store.id},
					{paramName: "stop_transaction_id", paramType: "integer", paramValue: self.model.stopTransactionId},
							];									
			var data =  { 
					 "appName": "HIS",
	                 "appSecretKey": "f306b6e1ccd9d5069d2dc71afe139271a",
	                 "reportDesignFilePath":actionNameRPT,
	                 "reportFormat": "PDF",
	                 "exportFileName": "admission",
	                 "reportParams": $scope.finalArr
		 };			
		   reportService.generate(REPORT_API_URI1,data);			
        }       

        var getPreviewOldBatch = function (){		
			$scope.finalArr = [];
			if(self.model.stopTransactionId == undefined)
					self.model.stopTransactionId = stopTransactionId;
			var actionNameRPT = "get_StopTransactionRequest_listQtyzero.rpt";
			$scope.finalArr=[
					{paramName: "store_id", paramType: "integer", paramValue: self.model.store.id},
					{paramName: "stop_transaction_id", paramType: "integer", paramValue: self.model.stopTransactionId},
							];									
			var data =  { 
					 "appName": "HIS",
	                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
	                 "reportDesignFilePath":actionNameRPT,
	                 "reportFormat": "PDF",
	                 "exportFileName": "admission",
	                 "reportParams": $scope.finalArr
		 };			
		   reportService.generate(REPORT_API_URI1,data);			
        } 

        var AddItems = function (SelectedItemsIds) {
            self.model.SelectedItemsIds.push(SelectedItemsIds.id);
            self.model.stopTransactionId = SelectedItemsIds.id;
            
            var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.UPLOAD_STOCK.ADD_ITEM;
            var data = {
                "stopTransactionsId": self.model.SelectedItemsIds,
                "storeId":self.model.store.id,
                "unitId": $scope.unitId
            };
            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    self.model.requestNo = response.data.requestNo;
                    self.model.itemDetailList = response.data.items;
                    self.model.isDetail = true                    
                    //var CurrentBatchItems = response.data.items.filter(function(e) { return e.quantity > 0 })
                   // self.model.itemDetailList = CurrentBatchItems;
                    self.model.itemDetailList.forEach(function (item) {
                        item.mrp = item.mrp.toFixed(2);
                        item.cop = item.cop.toFixed(2);
                    })
                    $rootScope.stopLoader();
                    // getAllItemDetails();
                    self.model.searchNewItemList = [];
                    self.model.SelectedItemsIds = [];

                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error(err.data.message);
                });
            
            /* start code for resolved HIS-10549 Sub Module:  created stock transaction for zero items but in upload stock approval its not showing */
            if(self.model.ActiveTab == 2){
            	GetBatch();
            }else {
            	self.model.getBatchFlag = true;
            }
            /* end code for resolved HIS-10549 Sub Module:  created stock transaction for zero items but in upload stock approval its not showing */
        }

        var GetBatch = function () { 
        	 if(self.model.stopTransactionId ){
            var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.UPLOAD_STOCK.GET_OLD_BATCH;
            var data = {
                "stopTransactionsId": [self.model.stopTransactionId],
                "storeId":$scope.storeId,
                "unitId": $scope.unitId
            };
            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    self.model.requestNo = response.data.requestNo;
                    self.model.itemDetailListOldBatch = response.data.items;
                    self.model.itemDetailListOldBatch.forEach(function (item) {
                        item.mrp = item.mrp.toFixed(2);
                        item.cop = item.cop.toFixed(2);
                    })
                    $rootScope.stopLoader();
                    // getAllItemDetails();
                    self.model.searchNewItemList = [];
                    self.model.SelectedItemsIds = [];
                    
                    /* start code for resolved HIS-10549 Sub Module:  created stock transaction for zero items but in upload stock approval its not showing */
                    self.model.getBatchFlag = false;
                    /* end code for resolved HIS-10549 Sub Module:  created stock transaction for zero items but in upload stock approval its not showing */

                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error(err.data.message);
                });
        	 } else{
        		 //growl.warning("Please get request first for Upload");
        	 }
        }
        
//        var SaveUploadStock = function () {
//            if (validateSave()) {
//                if (!self.model.uploadId) {
//                    SaveNewUploadStock();
//                } else {
//                    UpdateUploadStock();
//                }
//            }
//        }

        var SaveUploadStock = function () {           
                if (!self.model.uploadId) {
                    SaveNewUploadStock();
                } else {
                    UpdateUploadStock();
                }          
        }
        
        
        var UpdateUploadStock = function () {

            var upload = self.model;
            var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.UPLOAD_STOCK.UPDATE_API + upload.uploadId;
            var data = {
            	"userId":$scope.userId,
                "items": []
            };

            angular.forEach(self.model.itemDetailList, function (item, key) {
                var storeItem = {
                    "id": item.id,
                    "quantity": item.quantity,
                    
                    /* start change as per discuss with Satishbhai and Dilipbhai for editable upload stock */
                    "uploadedQuantity": parseInt(item.uploadedQuantity)
                    /* end change as per discuss with Satishbhai and Dilipbhai for editable upload stock */
                }                
                if (item.id == '' || item.id == null) {
                    storeItem.batchId = item.batchId,
                        storeItem.uomTypeId = item.uomTypeId,
                        storeItem.uomUnitId = item.uomUnitId,
                        storeItem.quantity = item.quantity,
                        storeItem.statusId = 1,
                        /* start change as per discuss with Satishbhai and Dilipbhai for editable upload stock */
                        storeItem.uploadedQuantity=parseInt(item.uploadedQuantity)
                        /* end change as per discuss with Satishbhai and Dilipbhai for editable upload stock */
                }
                data.items.push(storeItem);
            });
            
            angular.forEach(self.model.itemDetailListOldBatch, function (item, key) {
                var storeItem = {
                    "id": item.id,
                    "quantity": item.quantity
                }                
                if (item.id == '' || item.id == null) {
                    storeItem.batchId = item.batchId,
                        storeItem.uomTypeId = item.uomTypeId,
                        storeItem.uomUnitId = item.uomUnitId,
                        storeItem.quantity = item.quantity,
                        storeItem.statusId = 1
                }
                data.items.push(storeItem);
            });
            
            $rootScope.startLoader();
            GenericService.serviceAction("PATCH", URI, data).then(
                function (response) {
                    NavigateToBack();
                    $rootScope.stopLoader();
                    growl.success(response.data.message);
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error('Something Went Wrong');
                });
        }

        	//For Download and Upload CSV START
        $scope.fileObj={
    			csvfile:$scope.csvfile,
    	}
        
        $scope.filepreview = "";
    	$scope.filepreview = $scope.csvfile;
    	$scope.csvfile;
    	
        $scope.uploadfile = function() {
			try {
			            
			   $rootScope.startLoader();
				var fileName = $scope.csvfile;
		        var data = new FormData();
		        data.append('file',fileName );
		        console.log("listData", data);
		        var store = self.model;
		        var config = {
		        	   	transformRequest: angular.identity,
		        	   	transformResponse: angular.identity,
		    	   		headers : {
		    	   			unitId:$scope.unitId,
							organizationId:$scope.organizationId,
							updatedBy : $scope.userId,
							createdBy : $scope.userId,
		    	   			'Content-Type': undefined,
		    	   			storeId: store.store.id,
		    	   			statusId: 1,
			                approvalStatusId: 1,
			                stopTransactionId: self.model.stopTransactionId,
		    	   	    }
		           }
			

                if($scope.csvfile){
					var url=BASE_URL + ROOT_URL + "/api/inventory/stock/adjusments/upload/stock/updateStockAdjustment";
			          $http.post(url, data, config)
			           .then(function (response)  
			        	{
			        	   $rootScope.stopLoader();
			        	   var response= JSON.parse(response.data);
			        	   if(response.status == 'success'){
			        		   $('#csvfile').val(undefined);
			        		var successCount=response.object.successCount;
                           var message="";
			        			message="All records saved successfully with count="+successCount;
			        			growl.success(message,
										{
											title : 'SUCCESS'
												
										});
			        			NavigateToBack();
			        	   }
			        	   else if(response.status== 'error'){
			        			growl.error(response.message,
										{
											title : ''
												
										});
				           }
					});
		
				
                }
                else
              	  {
              	  growl.error("Please choose .CSV file",
								{
									title : 'ERROR'
										
								});
              	   $rootScope.stopLoader();
              	  }
			} catch (e) {
				console.log("Exception-", e);
			}
		}
        
        
        $scope.convertToCSV = function(objArray) {
            var array = typeof objArray != 'object' ? JSON.parse(objArray) : objArray;
            var str = '';

            for (var i = 0; i < array.length; i++) {
                var line = '';
                for (var index in array[i]) {
                    if (line != '') line += ','

                    line += array[i][index];
                }

                str += line + '\r\n';
            }

            return str;
        }
        $scope.exportCSVFile = function(headers, items, fileTitle){
        	 if (headers) {
        	        items.unshift(headers);
        	    }

        	    // Convert Object to JSON
        	    var jsonObject = JSON.stringify(items);

        	    var csv = this.convertToCSV(jsonObject);

        	    var exportedFilenmae = fileTitle + '.csv' || 'export.csv';

        	    var blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
        	    if (navigator.msSaveBlob) { // IE 10+
        	        navigator.msSaveBlob(blob, exportedFilenmae);
        	    } else {
        	        var link = document.createElement("a");
        	        if (link.download !== undefined) { // feature detection
        	            // Browsers that support HTML5 download attribute
        	            var url = URL.createObjectURL(blob);
        	            link.setAttribute("href", url);
        	            link.setAttribute("download", exportedFilenmae);
        	            link.style.visibility = 'hidden';
        	            document.body.appendChild(link);
        	            link.click();
        	            document.body.removeChild(link);
        	        }
        	    }
        }
        
        $scope.downloadCSV = function(){
        	console.log(self.model.itemDetailList);
        	//return false;

        	  var headers = {
        	      id: 'Id', // remove commas to avoid errors
        	     /* BatchId: "Batch Id",
                  UomTypeId: "UOM Type Id",
                  UomUnitId: "UOM Unit Id",*/
        	      AssetCategory: "Asset Category",
        	      ProductCategory: "Product Category",
        	      ItemCode: "Item Code",
        	      ItemName :"Item Name",
        	      UOM : "UOM",
        	      UOMUnit:"UOM Unit",
        	      BatchNo:"Batch No",
        	      ExpiryDate:"Expiry Date",
        	      COP:"COP",
        	      MRP:"MRP",
        	      Qty:"Qty",
        	      uploadQty:"upload Qty"



        	  };
        	  
        	  var itemsNotFormatted = [];
        	  angular.forEach(self.model.itemDetailList, function (value, key) {
        		  
        		  var newArr = {};
        		  newArr.id = value.id;
        		  newArr.AssetCategory = value.assetCategory;
        		  newArr.ProductCategory = value.productCategory;
        		  newArr.ItemCode = value.itemCode;
        		  newArr.ItemName = value.itemName;
        		  newArr.UOM = value.uom;
        		  newArr.UOMUnit = value.uomUnit;
        		  newArr.BatchNo = (value.batchNo != undefined && value.batchNo != null) ?  value.batchNo : " ";
        		  newArr.ExpiryDate = (value.expiryDate != undefined && value.expiryDate != null) ?  value.expiryDate : " ";
        		  newArr.COP = value.cop;
        		  newArr.MRP = value.mrp;
        		  newArr.Qty = value.quantity;  
        		  newArr.uploadQty = value.uploadedQuantity;
        		  itemsNotFormatted.push(newArr);
        		  
      		});
        	  
        	  var itemsFormatted = [];

        	  // format the data
        	  itemsNotFormatted.forEach((item) => {
        	      itemsFormatted.push({
        	    	  id: item.id, // remove commas to avoid errors,
        	    	  AssetCategory: item.AssetCategory,
        	    	  ProductCategory: item.ProductCategory,
        	    	  ItemCode: item.ItemCode,
        	    	  ItemName: item.ItemName, // remove commas to avoid errors,
        	    	  UOM: item.UOM,
        	    	  UOMUnit: item.UOMUnit,
        	    	  BatchNo: item.BatchNo,
        	    	  ExpiryDate: item.ExpiryDate, // remove commas to avoid errors,
        	    	  COP: item.COP,
        	    	  MRP: item.MRP,
        	    	  Qty: item.Qty,
        	    	  uploadQty: item.uploadQty,
        	      });
        	  });

        	  var fileTitle = 'stocks Adjustment'; // or 'my-unique-title'

        	  $scope.exportCSVFile(headers, itemsFormatted, fileTitle); // call the exportCSVFile() function to process the JSON and trigger the download
        	  
        	  
        }
//For Download and upload CSV END
        var SaveNewUploadStock = function () {
            var store = self.model;
            var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.UPLOAD_STOCK.SAVE_API;
            var data = {
                "storeId": store.store.id,
                "statusId": 1,
                "approvalStatusId": 1,
                "stopTransactionId": self.model.stopTransactionId,
                "items": [],
                "organizationId": $scope.organizationId,
                "unitId": $scope.unitId,
                "userId": $scope.userId
            };

            angular.forEach(store.itemDetailList, function (item, key) {
                var storeItem = {
                    "batchId": item.batchId,
                    "uomTypeId": item.uomTypeId,
                    "uomUnitId": item.uomUnitId,
                    "quantity": parseInt(item.quantity),
                    "uploadedQuantity": parseInt(item.uploadedQuantity), 
                    "statusId": 1
                }
                data.items.push(storeItem);

            });
            
            
            angular.forEach(store.itemDetailListOldBatch, function (item, key) {
                var storeItem = {
                    "batchId": item.batchId,
                    "uomTypeId": item.uomTypeId,
                    "uomUnitId": item.uomUnitId,
                    "quantity": parseInt(item.quantity),
                    "uploadedQuantity": parseInt(item.uploadedQuantity), 
                    "statusId": 1
                }
                data.items.push(storeItem);

            });
            
            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    // ClearRequests();
                    $rootScope.stopLoader();
                    NavigateToBack();
                    growl.success(response.data.message);
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error('Something Went Wrong');
                });
        }
        var validateSave = function () {
            var isValid = true;
            var ItemDetailList = self.model.itemDetailList;
            // !self.model.fromStore.id || !self.model.toStore.id || !self.model.priority.id || 
            if (ItemDetailList.length < 1) {
                growl.error('Error', {
                    title: "Please validate all required fields"
                });
                isValid = false;
            }
            for (let ItemDetail of ItemDetailList) {
                if (!ValidateQuantity(ItemDetail)) {
                    isValid = false;
                }
            }
            return isValid;
        }
        var NavigateToBack = function () {
            $state.go('uploadStock');
        }

        var ValidateQuantity = function (item) {
            var isValid = true;

            if (isNaN(parseFloat(item.uploadedQuantity))) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error("Please Enter Quantity");
            } else if (parseInt(item.uploadedQuantity) == 0) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error('Quantity Cannot Be Zero');
            }
            else if (parseInt(item.quantity) > item.availableQuantity) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error('Entered quantity is exceeding the max order quantity');
            } else {
                item.quantity = parseInt(item.quantity);
                isValid = true;
            }
            return isValid;
        }


        initializeController(); 
    }

    function config($stateProvider) {
        $stateProvider.state("detailsUploadStock", {
            url: "/detailsUploadStock",
            templateUrl: 'views/PharmacyInventory/StockAdjustment/details-upload-stock.html',
            controller: "UploadStockDetails.Controller",
            controllerAs: "vm",
            title: 'Upload Stock'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("UploadStockDetails.Controller", uploadStockDetailsController);
})();
