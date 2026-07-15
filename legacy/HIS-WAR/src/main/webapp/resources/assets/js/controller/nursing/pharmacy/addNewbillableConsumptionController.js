    (function () {
        "use strict";
        var CACHE = {};

        function addNewbillableConsumptionController($scope, $state,$window,$timeout, $rootScope, $http, $filter, $cookies, CONSTANTS, StatusService, BillingGenericService, PaginationService, GenericService, growl, commonDetailService,stopTransactionService) {
            var self = this;
            var mainStoreList = [];
            var visitNumber = '';
            var cookieObjectAppConfig = {};
            [cookieObjectAppConfig] = $rootScope.checkCookies();

            var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
            $scope.taxInclusive = cookieObjectAppConfig.taxInclusive;
            $scope.nurseRole = cookieObjectAppConfig.nurseRole;	
			$scope.nursingStationCookieShow = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
            $scope.nursingStationId = JSON.parse(sessionStorage.getItem('nursingStationId'));
            var nursingStationsWardsList = JSON.parse(sessionStorage.getItem('nursingStationsWardsList'));
		    $scope.nursingStationsWardsListId = nursingStationsWardsList;

            if (cookieObjectAppConfig == undefined) {
            $state.go('login');
            return;
            }
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
                    storeList: [{
                        "store": "All"
                    }],
                    assetCategories: [{
                        "category": "Select Asset Category"
                    }],
                    productCategories: [{
                        "category": "Select Product Category"
                    }],
                    wardList: [{
                        "wardName": "All"
                    }],
                    visitDetails: [{
                        "visitNo": "Select Visit No"
                    }],
                    visitTypeList: [{
                        "type": "Select VisitType"
                    }],
                    SelectedItemsIds: [],
                    selectAllRows: false,
                    SelectedItemIds: [],
                    ItemDetailList: [],
                    selectedStore: '',
                    // indentId: null,
                    consignment: false,
                    itemSearch: {
                        itemCode : undefined,
                        itemName : undefined,
                        batchNo : undefined
                    }
                };

                self.model.taxInclusive = cookieObjectAppConfig.taxInclusive;

                StatusService.GetStatus().then(function (status) {
                    self.model.Status = status;
                })

                self.model.SearchpatientNames = SearchpatientNames;
                self.model.SearchUhid = SearchUhid;
                self.model.AddPatientDetails = AddPatientDetails;
                self.model.search = '';
                self.model.prevSearch = '';
                self.model.patientNames = [];
                self.model.GetItemList = GetItemList;
                self.model.GetAssetCategoryList = GetAssetCategoryList;
                self.model.GetProductCategoryList = GetProductCategoryList;
                self.model.SearchNewItem = SearchNewItem;
                self.model.BarcodeScan = BarcodeScan;
                self.model.NewItemList = [];
                self.model.NewItemList1 = [];
                
              //added by minesh for search new item function response (i.e : SearchNewItem)
                self.model.NewUniqueItemList = []; 
              //added by minesh for select itemlist based on batchNo
                self.model.getItemFromBatch = getItemFromBatch ; 
                
                self.model.SelectItem = SelectItem;
                self.model.SelectAllItems = SelectAllItems;
                self.model.getAllItemDetails = getAllItemDetails;
                
              //added by minesh for select itemlist having a same id in batches
                self.model.getAllItemDetailsForBatch = getAllItemDetailsForBatch;
                
                self.model.RemoveRowData = RemoveRowData;
                self.model.Validate = Validate;
                self.model.ValidateQuantity = ValidateQuantity;
                self.model.SaveNewbillableConsumption = SaveNewbillableConsumption;
                self.model.NavigteToBillableConsumption = NavigteToBillableConsumption;
                self.model.ChangeVisitNumber = ChangeVisitNumber;
                self.model.ChangeVisitType = ChangeVisitType;
                self.model.PatientNameKeyPress = PatientNameKeyPress;
                self.model.uHIDKeyPress = uHIDKeyPress;
                self.model.ConfirmRemove = ConfirmRemove;
                self.model.PopupAction=PopupAction;
                self.model.CalculateLeaseQuantity=CalculateLeaseQuantity;
                self.model.CalculateStock=CalculateStock;
                self.model.onBatchChange = onBatchChange;
                self.model.ActualgetAllItemDetailsForBatch = ActualgetAllItemDetailsForBatch;
                self.model.ClearBarcode = ClearBarcode;
                self.model.clearList = clearList;

                $scope.patientSearchObj.uhidNumber = '';
                $scope.patientSearchObj.patientName = '';
                $scope.patientSearchObj.genderId = '';
                $scope.birthDate = '';
                $scope.patientSearchObj.identificationTypeId = '';
                $scope.patientSearchObj.identificationNumber = '';

                // self.model.indentId = commonDetailService.getDataId();
                // GetPatientIndentDetails(commonDetailService.getDataId());
                SetFocus();
                PopulateDropdown();
                // GetAssetCategoryList();
            }

            var clearList = function () {
                self.model.ItemDetailList = [];
                self.model.NewItemList1 = [];
            }

            var ClearBarcode = function () {
                self.model.barcode = '';
            }

            $scope.handleDoubleClick=false;
            var BarcodeScan = function (event, barcode) {
                $scope.handleDoubleClick=false;
                
                if(self.model.barcode == undefined || self.model.barcode.length == 0 || self.model.barcode == null)
        		{
                	growl.error("Barcode Number Should Not Be Blank...")
                	return false;
        		}
                if(!barcode) 
        		{
        	  		barcode = self.model.barcode;
    		
    			}
                
                var enterCode = 13;
                
                var data = {
                		
                	"priPrintedBarcodeNo":self.model.barcode,
                    "unitId": $scope.unitId,
                    "items":[]
                };
                if (!!barcode) {
        	        var URI = CONSTANTS.PROCUREMENT.QUOTATION.GET_BATCHES_API+"/prePrintedBarcode/getBarcodes";
                    $rootScope.startLoader();
                    GenericService.serviceAction("POST", URI, data).then(
                    function (response) {        
                        $rootScope.stopLoader();
                        var flag=false;
                        $scope.isManualBarcodeRequiredFlag=false;
                        angular.forEach(response.data, function (value, key) {
                            if(response.data[0].isManualBarcodeRequired==true)
                            {
                            	if(value.prePrintedBarcode==self.model.barcode){
            	        		 flag=true;
            	 	                self.model.barcode="";
            	 	               $scope.isManualBarcodeRequiredFlag=true;
                                   AddItem(value.batchId); 
            	 	        	  }
            	 	        	  else{
            	 	        		 flag=true;
            	 	        		 self.model.barcode = "";
            	 	 				growl.error( "Enter Valid Barcode..!",
            	 	 				{
            	 	 					title: "Error"
            	 	 				});
            	 	        	  }
            	 		      }
                            else {
                            		self.model.barcode = "";
            	 	 				flag=true;
            	 	 				growl.error( "Enter Valid Barcode..!",
            	 	 				{
            	 	 					title: "Error"
            	 	 				});
            	 	 			}
                        });
                        if(flag==false)
                        {
                            if (!!barcode) 
                            {
//                                var itemId = barcode.split(' ')[0];
                                var batchId = barcode;
                                if (!!batchId) 
                                {        	
                                    var URI = CONSTANTS.GLOBAL.BATCH_API + "unit/"+$scope.unitId;
                                    if(self.model.visitType){
                                        if (self.model.visitType.id == 1) {
                                            visitNumber = self.model.visitNumber.visitNo;
                                        } else {
                                            visitNumber = self.model.visitNumberText;
                                        }
                                    }   
        
                                    var data = {
                                        "assetTypeId": 1,
                                        "storeId": self.model.store.id ? self.model.store.id : undefined,
                                        "assetCategoryId": self.model.assetCategory ? self.model.assetCategory.id : undefined,
                                        "productCategoryId": self.model.productCategory ? self.model.productCategory.id : undefined,
                                        "itemCode": self.model.itemSearch.itemCode,
                                        "itemName": self.model.itemSearch.itemName,
                                        "batchName": self.model.itemSearch.batchNo,
                                        "visitTypeId": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                                        "admissionId": self.model.admissionId !=undefined ? self.model.admissionId : undefined,
                                        "encounterId":self.model.encounterId !=undefined ? self.model.encounterId : undefined,
//                                        "itemId":itemId,
                                        "batchId":batchId
                                    }
                                    self.model.NewItemList = [];
                    
                                    //added by minesh for search new item function response (i.e : SearchNewItem)
                                    self.model.NewUniqueItemList = []; 
                                    
                                    if((self.model.admissionId==undefined || self.model.admissionId == null) && (self.model.encounterId==undefined || self.model.encounterId==null)){
                                    	self.model.barcode = "";
                                    	growl.error('Please select patient.');
                                    	return false;
                                    }
                                    
                                    $rootScope.startLoaderPopup();
                                    self.model.barcode = "";
                                    GenericService.serviceAction("POST", URI, data).then(
                                    function (response) {
                                        $rootScope.stopLoaderPopup();
                                        self.model.barcode = "";
                                        self.model.pagination.data=[];
                                        self.model.pagination.totalPages=0;
                                        self.model.NewItemList = response.data;
                                        // added by minesh for search new item function response
                                        // (i.e : SearchNewItem)
                                        self.model.NewUniqueItemList = getUniqueSearchNewItemResponse(response.data);
                                        console.log(self.model.NewUniqueItemList);
                                        // added by minesh for add getitem with same itemId from
                                        // NewItemList
                                        for (let item of self.model.NewUniqueItemList){
                                            item.batches= getItemFromBatch(item.itemCode);
                                            /*Added by Atmiya 20191114 : sort batch by expiry date
                                            * select by default first batch after sort*/
                                            item.batches.sort(item.batches.expiry);
                                            item.selectedBatch = item.batches[0];
                                            /*End Added by Atmiya 20191114*/
                                        } 
                                        // end added by minesh for add getitem with same itemId
                                        // from NewItemList    
                                        //added by minesh 02-01-2020 for single Unique Batch
                        
                                        angular.forEach(self.model.NewUniqueItemList, function(list,key){
                                            var batchNo = [];
                                            var uniqueBatches = [];
                                            list.batches.forEach(function (batch) {
                                                var index = batchNo.indexOf(batch.batchNo);                    			 
                                                if(index === -1)
                                                {
                                                    batchNo.push(batch.batchNo);
                                                    uniqueBatches.push(batch);
                                                }else{
                                                    uniqueBatches[index].availableQuantity += batch.availableQuantity;
                                                    batch.availableQuantity = uniqueBatches[index].availableQuantity ;
                                                }
                                            });
                                        });
                                        AddItem(batchId);
                                        //added by minesh 02-01-2020 for single Unique Batch
                                    },
                                    function errorCallback(response) {
                                        $rootScope.stopLoaderPopup();
                                        growl.error('Error');
                                    });
                                  
                                }
                                event.stopPropagation();
                                event.preventDefault();
                                SetFocus();
                            }
                        }
                    });
                }
            }

            
          $scope.kitMasterList = [];
  		  $scope.kitId = null;
  		  $scope.getKitMasterList = function () {
  	           var URI = CONSTANTS.MASTER.UNIT.KIT_MASTER.GET_ALL + $scope.organizationId + "/" +$scope.unitId ;
  	            $rootScope.startLoader();
  	            GenericService.serviceAction("GET", URI, {}).then(
  	                function (response) {
  	                	
  	                $scope.kitMasterList =[];
  	             	  angular.forEach(response.data, function (x) {
  	             		  if(x.status == true){
  	             			  $scope.kitMasterList.push(x);
  	             		  }
  	             	  });
  	                    
  	                    $rootScope.stopLoader();
  	                },
  	                function (err) {
  	                    $rootScope.stopLoader();
  	                });
  	        }
  		  $scope.getKitMasterList();

  		 $scope.kitMasterItemsList = [];
  		 $scope.kitItemDetails =  function(){$scope.kitMasterItemsList = [];

         var URI = BASE_URL + ROOT_URL + "/api/units/kit/master/" + $scope.kitId + "/" + $cookies.getObject('cookieObject').storeId+ "/" +$scope.unitId ;
          $rootScope.startLoader();
          GenericService.serviceAction("GET", URI, {}).then(
              function (response) {
            	 
            	  $scope.kitMasterItemsList = response.data.listObject;
            	  angular.element('#itemDetailsModalBill').modal('show');
                  $rootScope.stopLoader();
              },
              function (err) {
                  $rootScope.stopLoader();
              });
          }
            
            var SetFocus = function () {
                self.model.barcode = undefined;
                $window.document.getElementById('barcode').focus();
            }

            var barcodeCheck=false;

            var AddItem = function (batchId) {
            	var barcodeCheck=false;
                if(self.model.NewUniqueItemList.length != 0 && $scope.isManualBarcodeRequiredFlag==false){
                    angular.forEach(self.model.NewUniqueItemList, function(data){
                        if(data.batchId == parseInt(batchId)){
                            data.selectedBatch = data.batches.find(function (data) {
                                return data.batchId == data.batchId;
                            });
                            var selectedUOM = data.uomResponses.find(function (uom) {
                                return data.uomTypeId == uom.uomTypeId;
                            });
                            var sellingPrice = !!data.selectedBatch ? commonDetailService.fixedDecimal(data.selectedBatch.mrp) : 0;
                            data.selectedBatch.rate = commonDetailService.calculateRate(data.selectedBatch.uomResponses, data.uomTypeId, sellingPrice); 
                            var data1 = {
                                "mrp": data.selectedBatch.rate,
                                "isTaxInc": self.model.taxInclusive,
                                "markup": data.markUpDown,
                                "markUpDownPer" : data.markUpDownPer,
                                "discount": data.discount == 0 || data.discount == undefined ? data.discountPerItem== 0 || data.discountPerItem == undefined  ? data.discountPerItem : data.discount : 0,
                                "isDiscPercent": !!data.discountType && data.discountType == 1 ? true : false,
                                "tax": data.taxPercentage,
                                "qty": data.selectedBatch.quantity,
                                "patientClassPercentage": data.patientClassPercentage
                            }
                            $scope.patientClassPercentage = data1.patientClassPercentage;
                 
                            var response = commonDetailService.calculateAmount(data1);
                            data.selectedBatch.rate=response.rate;
                            data.rate = response.rate;
                            data.taxAmount = response.taxAmount;
                            data.totalAmount = response.totalAmount;
                            data.netAmount = response.netAmount;
                            data.patientClassAmount = response.patientClassAmount;
                            data.unitPatientClassAmount = response.unitPatientClassAmount;
                            $scope.patientClassAmount = response.patientClassAmount;
                            $scope.unitPatientClassAmount = response.unitPatientClassAmount;
                            var check = false;
                            angular.forEach(self.model.ItemDetailList,function(value,key){
                                  
                                      if(data.batchId == value.selectedBatch.batchId) {
                                          check = true;
                                      }
                                      
                                });
                            
                            if(check==true){
                                growl.error('There should not be the same batch for the same item.');
                                barcodeCheck=true;
                              return false;
                             }
                            
                            if($scope.handleDoubleClick==false)
                      	    {
                            	self.model.ItemDetailList.push(data);
                            $scope.handleDoubleClick=true;
                            }
                            barcodeCheck=true;
                        }
                    });
                    // self.model.barcode = "";
                    if(barcodeCheck==false)
                    {
                        growl.error("Entered Barcode Number Does not Exist")
                    }
                }
                else{
                    $scope.searchItem(batchId);
                }
                
            }

            $scope.searchItem = function (batchId) {
            	var barcodeCheck=false;
                var URI = CONSTANTS.GLOBAL.BATCH_API + "unit/"+$scope.unitId;
                
                if(self.model.visitType){
                    if (self.model.visitType.id == 1) {
                        visitNumber = self.model.visitNumber.visitNo;
                    } else {
                        visitNumber = self.model.visitNumberText;
                    }
                }
                    
                var data = {
                    "assetTypeId": 1,
                    "storeId": self.model.store.id ? self.model.store.id : undefined,
                    "assetCategoryId": self.model.assetCategory ? self.model.assetCategory.id : undefined,
                    "productCategoryId": self.model.productCategory ? self.model.productCategory.id : undefined,
                    "itemCode": self.model.itemSearch.itemCode,
                    "itemName": self.model.itemSearch.itemName,
                    "batchName": self.model.itemSearch.batchNo,
                    "visitTypeId": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                    "admissionId": self.model.admissionId !=undefined ? self.model.admissionId : undefined,
                    "encounterId":self.model.encounterId !=undefined ? self.model.encounterId : undefined,
//                    "itemId":itemId,
                    "batchId":batchId
                }
            
                self.model.NewItemList = [];
                self.model.NewUniqueItemList = [];  
                
                if((self.model.admissionId==undefined || self.model.admissionId == null) && (self.model.encounterId==undefined || self.model.encounterId==null)){
                	self.model.barcode = "";
                	growl.error('Please select patient.');
                	return false;
                }
                $rootScope.startLoaderPopup();
                GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoaderPopup();

                    self.model.pagination.data=[];
                    self.model.pagination.totalPages=0;
                    self.model.NewItemList = response.data;
                    
                    self.model.NewUniqueItemList = response.data;
                    console.log(self.model.NewUniqueItemList);
                    for (let item of self.model.NewUniqueItemList){
                        item.batches= getItemFromBatch(item.itemCode);
                        item.batches.sort(item.batches.expiry);
                        item.selectedBatch = item.batches;
                    } 
                    if(!!response && !!response.data && response.data.length > 0){            
                        PaginationService.searchDataByField(self.model.NewUniqueItemList, self.model.pagination);
                    }
                    self.model.selectAllRows = false;
                    angular.forEach(self.model.NewUniqueItemList, function(data){
                        if(data.batchId == parseInt(batchId)){
                            data.selectedBatch = data.batches.find(function (data) {
                                return data.batchId == parseInt(batchId);
                            });
                            var selectedUOM = data.uomResponses.find(function (uom) {
                                return data.uomTypeId == uom.uomTypeId;
                            });
                            var sellingPrice = !!data.selectedBatch ? commonDetailService.fixedDecimal(data.selectedBatch.mrp) : 0;
                            data.selectedBatch.rate = commonDetailService.calculateRate(data.selectedBatch.uomResponses, data.uomTypeId, sellingPrice);
                            var data1 = {
                                "mrp": data.selectedBatch.rate,
                                "isTaxInc": self.model.taxInclusive,
                                "markup": data.markUpDown,
                                "markUpDownPer" : data.markUpDownPer,
                                "discount": data.discount == 0 || data.discount == undefined ? data.discountPerItem== 0 || data.discountPerItem == undefined  ? data.discountPerItem : data.discount : 0,
                                "isDiscPercent": !!data.discountType && data.discountType == 1 ? true : false,
                                "tax": data.taxPercentage,
                                "qty": data.selectedBatch.quantity,
                                "patientClassPercentage": data.patientClassPercentage
                            }
                            $scope.patientClassPercentage = data1.patientClassPercentage;
                 
                            var response = commonDetailService.calculateAmount(data1);
                            data.selectedBatch.rate=response.rate;
                            data.rate = response.rate;
                            data.taxAmount = response.taxAmount;
                            data.totalAmount = response.totalAmount;
                            data.netAmount = response.netAmount;
                            data.patientClassAmount = response.patientClassAmount;
                            data.unitPatientClassAmount = response.unitPatientClassAmount;
                            $scope.patientClassAmount = response.patientClassAmount;
                            $scope.unitPatientClassAmount = response.unitPatientClassAmount;
                            var check = false;
                            angular.forEach(self.model.ItemDetailList,function(value,key){
                                  
                                      if(data.batchId == value.selectedBatch.batchId) {
                                          check = true;
                                      }
                                      
                                });
                            
                            if(check==true){
                                growl.error('There should not be the same batch for the same item.');
                                barcodeCheck=true;
                              return false;
                             }
                            
                            self.model.ItemDetailList.push(data);
                            barcodeCheck=true;
                        }
                    });
                    self.model.barcode = "";
                    if(barcodeCheck==false)
                    {
                        growl.error("Entered Barcode Number Does not Exist")
                    }
                },
                function errorCallback(response) {
                    $rootScope.stopLoaderPopup();
                    growl.error('Error');
                });
            }
            
            var PopulateDropdown = function () {

                var URI = CONSTANTS.GLOBAL.PATIENT_ISSUE_DROPDOWN_API + "/"+$scope.organizationId +"/"+ $scope.unitId+"/"+$scope.userId;                
                self.model.visitType = self.model.visitTypeList[0];
                self.model.visitNumber = self.model.visitDetails[0];
                 self.model.store = self.model.storeList[0];
                self.model.ward = self.model.wardList[0];

                $rootScope.startLoader();
                GenericService.serviceAction("GET", URI, {}).then(
                    function (response) {
                        self.model.visitTypeList = self.model.visitTypeList.concat(response.data.visitType);
                        self.model.storeList = self.model.storeList.concat(response.data.store);
                        self.model.wardList = self.model.wardList.concat(response.data.ward);
                        var storedata = JSON.parse($cookies.get("cookieObject"));
                        var storename = self.model.storeList.filter(x => x.id == storedata.storeId);
                        self.model.selectedStore = storename[0].store;
                        self.model.storeId = storedata.storeId;
                        self.model.store = self.model.storeList.find(function (data) {
                            return self.model.storeId == data.id;
                        });
                        self.model.visitType = self.model.visitTypeList.find(function (obj) {
                            return obj.id == 2;
                        });
                        $rootScope.stopLoader();
                    });
            }

            // $scope.validatesearch = function () {
            //     var isValid = true;
            //     if(self.model.visitType.id == 0){
            //         growl.error("Please select visit type");
            //         isValid = false;
            //     }
            //     return isValid;
            // }

            var SearchpatientNames = function (search) {
                // if($scope.validatesearch()){
                    if (search.length < 3) return;

                    if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 3) && (self.model.visitType.id)) {
                        self.model.prevSearch = search;

                        var URI = CONSTANTS.GLOBAL.PATIENT_SEARCH_API +$scope.unitId + "/" + search  + "/" +self.model.visitType.id; 

                        $rootScope.startLoader();
                        return GenericService.serviceAction("GET", URI, {})
                            .then(function (response) {
                                $rootScope.stopLoader();
                                if (!!response.data && response.data.length > 0) {
                                    self.model.patientNames = response.data;
                                    // self.model.patientId = response.data.patientId;
                                    // self.model.uhid = response.data.uhid;
                                    // self.model.admissionId = response.data.visitDetails[0].admissionId;
                                    $rootScope.stopLoader();
                                    //console.log(self.model.patientNames.itemName);
                                    return $filter('filter')(self.model.patientNames, {
                                        $: search
                                    });
                                } else {
                                    $rootScope.stopLoader();
                                    return PatientNameNotFound(search);
                                }
                            }, function (err) {
                                $rootScope.stopLoader();
                                return PatientNameNotFound(search);
                            });
                    } else {
                        if(!self.model.visitType.id){
                            growl.error('Please select visit type for fetching the patient name');
                        }
                        if (!!self.model.patientNames && self.model.patientNames.length > 0 &&
                            self.model.patientNames[0].itemFound != undefined && !self.model.patientNames[0].itemFound) {
                            return PatientNameNotFound(search);
                        } else {
                            return $filter('filter')(self.model.patientNames, {
                                $: search
                            });
                        }
                    }
                // }
            }

            $scope.batchExpiryMsgShow = function(item,idex,batches){
                if(item.selectedBatch.quantity == undefined || item.selectedBatch.quantity == null || item.selectedBatch.quantity == 0 || item.selectedBatch.quanity == ""){
                    item.receivedQtyCon = 0;
                    item.receivedQtyCalculation = 0;
                    item.selectedBatch.quantity = 0;
                }
                var date = moment(new Date()).format("MM");
                var year = moment(new Date()).format("YYYY");
                var dateCal = parseInt(date) + 1;
                var expDate = moment(item.selectedBatch.expiry).format("MM");
                var expYear = moment(item.selectedBatch.expiry).format("YYYY");
                if((dateCal == expDate || date == expDate) && year == expYear){
                    growl.warning("Batch No. " + item.selectedBatch.batchNo + " is near expiry");
                }
                // var sellingPrice = !!item.selectedBatch ? commonDetailService.fixedDecimal(item.selectedBatch.mrp) : 0;
                // item.selectedBatch.rate = commonDetailService.calculateRate(item.selectedBatch.uomResponses, item.uomTypeId, sellingPrice);      
                angular.forEach(self.model.ItemDetailList, function (item){
                	angular.forEach(item.batches, function (batch){
                		if(item.selectedBatch.batchId == batch.batchId){
                			item.uomResponses.find(function(u){
                				batch.uomType =u.uomDetails;
                			});
                			 batch.uomType = item.uomDatalist.uomDetails;
				              
                		}
                		
                	});
                });
               }
                  
            $scope.batchClear = function(item,idex){
                var check = false;
                angular.forEach(self.model.ItemDetailList,function(value,key){
                    if(idex != key && item.selectedBatch.batchId == value.selectedBatch.batchId ) {
                        check = true;
                    }
                });
                if(check==true){
                    growl.error('There should not be the same batch for the same item.'); //Enter Todays Encountered Patient Name
                    item.selectedBatch='';
                }
            }
            

        var onBatchChange = function(item){
            var sellingPrice = !!item.selectedBatch ? commonDetailService.fixedDecimal(item.selectedBatch.mrp) : 0;
            var selectedUOM = item.uomResponses.find(function (uom) {
                return item.uomTypeId == uom.uomTypeId;
            });
            item.selectedBatch.rate = commonDetailService.calculateRate(item.selectedBatch.uomResponses, item.selectedBatch.uomTypeId, sellingPrice);
            self.model.beforeCalculation = item;

            var data = {
                "mrp": item.selectedBatch.rate,
                "isTaxInc": self.model.taxInclusive,
                "markup": item.markUpDown,
                "markUpDownPer" : item.markUpDownPer,
                "discount": item.discount == 0 || item.discount == undefined ? item.discountPerItem== 0 || item.discountPerItem == undefined  ? item.discountPerItem : item.discount : 0,
                "isDiscPercent": !!item.discountType && item.discountType == 1 ? true : false,
                "tax": item.taxPercentage,
                "qty": item.selectedBatch.quantity,
                "patientClassPercentage": item.patientClassPercentage,
            }
            $scope.patientClassPercentage = data.patientClassPercentage;

            var response = commonDetailService.calculateAmount(data);
            item.selectedBatch.rate=response.rate;
            item.rate = response.rate;
            item.taxAmount = response.taxAmount;
            item.totalAmount = response.totalAmount;
            item.netAmount = response.netAmount;
            item.patientClassAmount = response.patientClassAmount;
            item.unitPatientClassAmount = response.unitPatientClassAmount;
            $scope.patientClassAmount = response.patientClassAmount;
            $scope.unitPatientClassAmount = response.unitPatientClassAmount;
        }

            var PatientNameNotFound = function (search) {
                var item = {
                    "itemFound": false,
                    "details": "Patient Not found with name " + search
                };
                self.model.patientNames = [item];
                return self.model.patientNames;
            }

            var SearchUhid = function (search) {

                if (search.length < 1) return;

                if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
                    self.model.prevSearch = search;

                    var URI = CONSTANTS.GLOBAL.UHID_SEARCH_API + search +'/' + $scope.organizationId;

                    $rootScope.startLoader();
                    return GenericService.serviceAction("GET", URI, {})
                        .then(function (response) {
                            $rootScope.stopLoader();
                            if (!!response.data && response.data.length > 0) {
                                self.model.patientNames = response.data;
                                self.model.patientId = response.data.patientId;
                                // self.model.uhid = response.data.uhid;
                                $rootScope.stopLoader();
                                //console.log(self.model.patientNames.itemName);
                                return $filter('filter')(self.model.patientNames, {
                                    $: search
                                });
                            } else {
                                $rootScope.stopLoader();
                                return UHIDNotFound(search);
                            }
                        }, function (err) {
                            $rootScope.stopLoader();
                            return UHIDNotFound(search);
                        });
                } else {
                    if (!!self.model.patientNames && self.model.patientNames.length > 0 &&
                        self.model.patientNames[0].itemFound != undefined && !self.model.patientNames[0].itemFound) {
                        return UHIDNotFound(search);
                    } else {
                        return $filter('filter')(self.model.patientNames, {
                            $: search
                        });
                    }
                }
            }

            var UHIDNotFound = function (search) {
                var item = {
                    "itemFound": false,
                    "details": "UHID Not found with search " + search
                };
                self.model.patientNames = [item];
                return self.model.patientNames;
            }

            var ChangeVisitNumber = function (visitData) {
                self.model.encounterId = visitData.encounterId;
                self.model.admissionId = visitData.admissionId;
                self.model.doctorId = visitData.doctorId;
            }
    
            var AddPatientDetails = function (item, index) {
                var radio = document.getElementById('patientSelectRadioCommon_' + index);
			    radio.checked = true;
			    
			    if(item.isFinalBill != null && item.isFinalBill == "Y"){
			    	radio.checked = false;
			    	
			    	 growl.warning("The final bill has been generated for this patient.");
			    	
			    	return false;
			    }
			    
                if (!!item.patientId) {
                    var URI = CONSTANTS.GLOBAL.PATIENT_DETAILS_SEARCH_API +'/' + $scope.organizationId + '/'+$scope.unitId;
    
                    var data = {
                        "visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : 0,
                        "patientId": !!item.patientId ? item.patientId : undefined,
                        // "uhid": !!item.uhid ? item.uhid : undefined,
                    };
    
                    $rootScope.startLoader();
                    GenericService.serviceAction("POST", URI, data)
                        .then(function (response) {
    
                                self.model.patientInfo = response.data,
                                self.model.patientName = response.data.patientName,
                                self.model.patientId = response.data.patientId,
                                self.model.uhid = response.data.uhid,
                                self.model.bedNo = response.data.bedNo,
                                self.model.bedId = response.data.bedId
                            // self.model.encounterId = response.data.encounterId,
                            // self.model.admissionId = response.data.admissionId
                            if(!!response.data.visitDetails.length){
                                if (self.model.visitType.id == 1) {
//                                    self.model.visitDetails = self.model.visitDetails.concat(response.data.visitDetails);

    								angular.forEach(response.data.visitDetails,function(value,key){
                                        self.model.visitDetails = self.model.visitDetails.concat(value);
                                        self.model.visitNumber = self.model.visitDetails[1];
                                        self.model.encounterId = value.encounterId;
                                        self.model.admissionId = value.admissionId;
                                        self.model.doctorId = value.doctorId;
    								})
                                	
                                } else if (self.model.visitType.id != 1) {
                                    self.model.visitNumberText = response.data.visitDetails[0].visitNo;
                                    self.model.encounterId = response.data.visitDetails[0].encounterId,
                                    self.model.admissionId = response.data.visitDetails[0].admissionId,
                                    self.model.doctorId = response.data.visitDetails[0].doctorId
                                }
                            } 
                            self.model.ward = self.model.wardList.find(function (ward) {
                                return response.data.wardId == ward.id;
                            });
    
                            self.model.ItemDetailList = [];
                            $rootScope.stopLoader();
                        }, function (err) {
                            growl.error(err.data.summary ? err.data.summary : err.data.message);
                            $rootScope.stopLoader();
                        });
                }
            }
            
            var ClearPatientInfo = function (){
                self.model.patientData = null;
                self.model.patientId = null;
                self.model.bedNo = null;
                self.model.bedId = null;
                self.model.ward = self.model.wardList[0];
                self.model.visitNumberText = null;
                self.model.encounterId = null;
                self.model.admissionId = null;
                self.model.doctorId = null; 
                self.model.visitNumber = self.model.visitDetails.splice(1);
                self.model.visitNumber = self.model.visitDetails[0];
                self.model.patientInfo = null;
            }
    
            var ChangeVisitType = function(){
                self.model.patientName = null;   
                self.model.uhid = null;
                ClearPatientInfo();
                self.model.ItemDetailList = [];
                self.model.patientNames = [];
            }
            
            var PatientNameKeyPress = function (event) {
                var search = $filter('filter')(self.model.patientNames, {
                    $: self.model.patientName
                });
                if (!self.model.patientName) {      
                    self.model.uhid = null;
                    ClearPatientInfo();
                }
            }
    
            var uHIDKeyPress = function (event) {
                var search = $filter('filter')(self.model.patientNames, {
                    $: self.model.uhid
                });
                if (!self.model.uhid) {
                    self.model.patientName = null;
                    ClearPatientInfo();
                }
            }

            var ValidateToGetItems = function () {
                var isValid = true;

                if (!self.model.storeId) {
                    self.model.isStoreValid = true;
                    isValid = false;
                    growl.error('Select store 1');
                }
                if(!self.model.visitType.id) {
                    self.model.isVisitTypeValid = true;
                    isValid = false;
                    growl.error('Select Visit Type');
                }
                if(self.model.visitType.type == 'Out Patient'){
                    if (!self.model.patientName || !self.model.uhid) {
                        self.model.isPatientNameValid = true;
                        isValid = false;
                        growl.error('Enter Todays Encountered Patient Name');
                    }    
                }
                else{    
                    if(!self.model.patientName || !self.model.uhid) {
                        self.model.isPatientNameValid = true;
                        isValid = false;
                        growl.error('Enter Admitted Patient Name');
                    }
                }
               
                return isValid;
            }

            var GetItemList = function () {
               if(ValidateToGetItems()) {
                GetAssetCategoryList();
                self.model.NewItemList = [];
                
              //added by minesh for search new item function response (i.e : SearchNewItem)
                self.model.NewUniqueItemList = []; 
                
                self.model.selectAllRows = false,
                self.model.itemSearch.itemCode = undefined;
                self.model.itemSearch.itemName = undefined;
                self.model.itemSearch.batchNo = undefined;
                self.model.pagination.data=[];
                self.model.pagination.totalPages = 0;
                self.model.selectAllRows = false;
                self.model.action = 'Open';
                var popup = angular.element('#ItemListPopUp');
                popup.modal('show');
                }
            }

            var GetAssetCategoryList = function () {
                var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + 1 + "/"+$scope.unitId + "/"+$scope.organizationId ;

                self.model.assetCategory = self.model.assetCategories[0];
                self.model.productCategory = self.model.productCategories[0];

                self.model.assetCategories.splice(1);
                self.model.productCategories.splice(1);

                $rootScope.startLoader();
                GenericService.serviceAction("GET", URI, {}).then(
                    function (response) {
                        self.model.assetCategories = self.model.assetCategories.concat(response.data);
                        $rootScope.stopLoader();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                    });
            }

            var GetProductCategoryList = function (id) {

                var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + id + "/"+$scope.organizationId ;
                self.model.productCategory = self.model.productCategories[0];
                self.model.productCategories.splice(1);

                $rootScope.startLoader();
                GenericService.serviceAction("GET", URI, {}).then(
                    function (response) {
                        self.model.productCategories = self.model.productCategories.concat(response.data);
                        $rootScope.stopLoader();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                    });
            }

            
            $scope.addItems = function (){
            	
            		if( self.model.patientId == undefined ||self.model.patientId == null && (self.model.admissionId==undefined || self.model.admissionId == null) && (self.model.encounterId==undefined || self.model.encounterId==null) ){
            			
            			  growl.error(' Please select patient ');
            			  
            			  return false;
            		}
            	
            	
                    var URI = CONSTANTS.GLOBAL.BATCH_API + "unit/"+$scope.unitId;

                    if(self.model.visitType){
                        if (self.model.visitType.id == 1) {
                            visitNumber = self.model.visitNumber.visitNo;
                        } else {
                            visitNumber = self.model.visitNumberText;
                        }
                    }
                	var found = $scope.kitMasterList.find(element => element.id == $scope.kitId);
    				var itemIds = [];
    				
    				angular.forEach(found.items,function(value,key){
    					if(value.status == true){
    						itemIds.push(value.itemId);
    					}
    				})
                        
                    var data = {
                        "assetTypeId": 1,
                        "storeId": self.model.store.id ? self.model.store.id : undefined,
                        "visitTypeId": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                        "admissionId": self.model.admissionId !=undefined ? self.model.admissionId : undefined,
                        "encounterId":self.model.encounterId !=undefined ? self.model.encounterId : undefined,
                        "itemIds" : itemIds,
                        "checkKit" : true
                    }
                
                    $rootScope.startLoaderPopup();
                    GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoaderPopup();
                        
                        if( response.data == undefined || response.data == null || response.data.length == 0 ){
                        	 growl.error(' Stock not available in selected kit items ');
                        }else{
                        	
                        	  var newUniqueKitItemList = response.data;
                              self.model.NewItemList = response.data;
                              $scope.resultData = newUniqueKitItemList.filter(word => word.availableQuantity > 0);
                              newUniqueKitItemList = $scope.resultData;
                              for (let item of newUniqueKitItemList){
                                  item.batches= getItemFromBatch(item.itemCode);
                                  item.batches.sort(item.batches.expiry);
                                  item.selectedBatch = item.batches;
                              } 
                              
                              angular.forEach(found.items,function(value,key){
                              	var check = false;
                              	 angular.forEach(newUniqueKitItemList,function(value1,key1){
                              		 
                              		 if(check == false && value1.itemId == value.itemId && value1.availableQuantity > 0 ){
                              		
                              			 value1.isSelected = true ;
                              			 
                              			 angular.forEach(value1.selectedBatch,function(val,k){
                              				 val.quantity = value.quantity;
                              			 });
                              			 
                              			 self.model.NewItemList1.push(value1);
                              			 check = true;
                              		 }
                   					
                   				})
                   				
              				})
              				getAllItemDetailsForBatch();
                              $scope.kitId = null;
                        }
                        
                      
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoaderPopup();
                        growl.error('Error');
                    });
            	
            }
            
            
            var SearchNewItem = function () { 
                if(self.model.itemSearch.itemName){
                     if(self.model.itemSearch.itemName!=undefined  &&  (self.model.itemSearch.itemName.replace(/\s/g, "").length) == 2 ){
                        var URI = CONSTANTS.GLOBAL.BATCH_API + "unit/"+$scope.unitId;
    
                        if(self.model.visitType){
                            if (self.model.visitType.id == 1) {
                                visitNumber = self.model.visitNumber.visitNo;
                            } else {
                                visitNumber = self.model.visitNumberText;
                            }
                        }
                            
                        var data = {
                            "assetTypeId": 1,
                            "storeId": self.model.store.id ? self.model.store.id : undefined,
                            "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                            "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                            "itemCode": self.model.itemSearch.itemCode,
                            "itemName": self.model.itemSearch.itemName,
                            "batchName": self.model.itemSearch.batchNo,
                            "visitTypeId": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                            "admissionId": self.model.admissionId !=undefined ? self.model.admissionId : undefined,
                            "encounterId":self.model.encounterId !=undefined ? self.model.encounterId : undefined
                        }
                    
                        self.model.NewItemList = [];
                        self.model.NewUniqueItemList = [];  

                        $rootScope.startLoaderPopup();
                        GenericService.serviceAction("POST", URI, data).then(
                        function (response) {
                            $rootScope.stopLoaderPopup();

                            self.model.pagination.data=[];
                            self.model.pagination.totalPages=0;
                            self.model.NewItemList = response.data;
                            
                            self.model.NewUniqueItemList = response.data;
                            console.log(self.model.NewUniqueItemList);
                            $scope.resultData = self.model.NewUniqueItemList.filter(word => word.availableQuantity > 0);
                            self.model.NewUniqueItemList= $scope.resultData;
                            for (let item of self.model.NewUniqueItemList){
                                item.batches= getItemFromBatch(item.itemCode);
                                item.batches.sort(item.batches.expiry);
                                item.selectedBatch = item.batches;
                            } 
                            
                            if(!!response && !!response.data && response.data.length > 0){            
                                PaginationService.searchDataByField(self.model.NewUniqueItemList, self.model.pagination);
                            }
                            self.model.selectAllRows = false;
                        },
                        function errorCallback(response) {
                            $rootScope.stopLoaderPopup();
                            growl.error('Error');
                        });
                    }else{
                           if(self.model.itemSearch.itemName !=undefined &&  (self.model.itemSearch.itemName.replace(/\s/g, "").length) > 0){
                                                                          var list =  $filter('filter')(self.model.NewUniqueItemList, {
                                                                                              itemName: self.model.itemSearch.itemName
                                                                                             });

                                                                            PaginationService.searchDataByField(list, self.model.pagination);
                                                                        }

                                                       	}
                }
                else if(self.model.itemSearch.itemCode){
                     if(self.model.itemSearch.itemCode!=undefined  &&  (self.model.itemSearch.itemCode.replace(/\s/g, "").length) == 2 ){
                        var URI = CONSTANTS.GLOBAL.BATCH_API + "unit/"+$scope.unitId;
    
                        if(self.model.visitType){
                            if (self.model.visitType.id == 1) {
                                visitNumber = self.model.visitNumber.visitNo;
                            } else {
                                visitNumber = self.model.visitNumberText;
                            }
                        }
                            
                        var data = {
                            "assetTypeId": 1,
                            "storeId": self.model.store.id ? self.model.store.id : undefined,
                            "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                            "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                            "itemCode": self.model.itemSearch.itemCode,
                            "itemName": self.model.itemSearch.itemName,
                            "batchName": self.model.itemSearch.batchNo,
                            "visitTypeId": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                            "admissionId": self.model.admissionId !=undefined ? self.model.admissionId : undefined,
                            "encounterId":self.model.encounterId !=undefined ? self.model.encounterId : undefined
                        }
                    
                        self.model.NewItemList = [];
                        self.model.NewUniqueItemList = [];  

                        $rootScope.startLoaderPopup();
                        GenericService.serviceAction("POST", URI, data).then(
                        function (response) {
                            $rootScope.stopLoaderPopup();

                            self.model.pagination.data=[];
                            self.model.pagination.totalPages=0;
                            self.model.NewItemList = response.data;
                            
                            self.model.NewUniqueItemList = getUniqueSearchNewItemResponse(response.data);
                            console.log(self.model.NewUniqueItemList);
                            $scope.resultData = self.model.NewUniqueItemList.filter(word => word.availableQuantity > 0);
                            self.model.NewUniqueItemList= $scope.resultData;
                            for (let item of self.model.NewUniqueItemList){
                                item.batches= getItemFromBatch(item.itemCode);
                                item.batches.sort(item.batches.expiry);
                                item.selectedBatch = item.batches[0];
                            } 
                            angular.forEach(self.model.NewUniqueItemList, function(list,key){
                                var batchNo = [];
                                var uniqueBatches = [];
                                list.batches.forEach(function (batch) {
                                    var index = batchNo.indexOf(batch.batchNo);                    			 
                                    if(index === -1)
                                    {
                                        batchNo.push(batch.batchNo);
                                        uniqueBatches.push(batch);
                                    }else{
                                            uniqueBatches[index].availableQuantity += batch.availableQuantity;
                                            batch.availableQuantity = uniqueBatches[index].availableQuantity ;
                                    }
                                });
                            
                            });
                            if(!!response && !!response.data && response.data.length > 0){            
                                PaginationService.searchDataByField(self.model.NewUniqueItemList, self.model.pagination);
                            }
                            self.model.selectAllRows = false;
                        },
                        function errorCallback(response) {
                            $rootScope.stopLoaderPopup();
                            growl.error('Error');
                        });
                    }else{
                             if(self.model.itemSearch.itemCode !=undefined &&  (self.model.itemSearch.itemCode.replace(/\s/g, "").length) > 0){
                                var list =  $filter('filter')(self.model.NewUniqueItemList, {
                                              itemCode: self.model.itemSearch.itemCode
                                     });

                                     PaginationService.searchDataByField(list, self.model.pagination);
                                    }

                             }
                }
                else{
                    self.model.NewItemList = [];
                    self.model.NewUniqueItemList = [];
                    self.model.pagination.data = [];
                }
            }

          /* added by minesh for dropdown batch */
            //for search new item function response (i.e : SearchNewItem)
            function getUniqueSearchNewItemResponse(responseData){
                var data=angular.copy(responseData);
                const uniqueResponse =  data.map(e=> e['batchId']). 
                // store the keys of the unique objects
                map((e,i,final) =>final.indexOf(e) === i && i) 
                // eliminate the dead keys & return unique objects
               .filter((e)=> data[e]).map(e=>data[e]);
                return uniqueResponse;
            }
            
            //for select dropdownbatch value 
           var  getItemFromBatch = function (itemCode){
                const getItemFromBatch =  self.model.NewItemList.filter(ic =>ic.itemCode == itemCode);
                return getItemFromBatch;
            }
            
            // for call getAllItemDetailsForBatchwise
            var getAllItemDetailsForBatch = function () {   
            	$rootScope.startLoader();
            	var check = false;
                angular.forEach(self.model.ItemDetailList,function(value,key){
                	
                	  angular.forEach( self.model.NewItemList1,function(value1,key1){
                          if(value1.isSelected == true && value1.batchId == value.selectedBatch.batchId) {
                              check = true;
                          }
                          });
                	
                    });
                
                if(check==true){
                    growl.error('There should not be the same batch for the same item.'); //Enter Todays Encountered Patient Name
                    self.model.NewItemList1 = [];
                    $rootScope.stopLoader();
                  return false;
                 }
                 if(self.model.NewItemList1.length > 0){
                    $scope.updateBatchQuantity();
                 }

                var popup = angular.element('#ItemListPopUp');
                popup.modal('hide');
                $rootScope.stopLoader();
          
            }
            
            var ActualgetAllItemDetailsForBatch = function () {
            	// self.model.ItemDetailList=[];
            	var conversionPurchase;
  		      var conversionStock;
  		      var conversionDespense;
                var list = []
                var selectedUom;
                var checkLasa = false;
                self.model.NewItemList1.forEach(function (item) {
                    if(item.isLasa !== undefined && item.isLasa !== null && item.isLasa === true){
                           checkLasa = true;
                    }
                    var resultData1 = item.batches.filter(word => word.availableQuantity > 0);
                    item.batches= resultData1;
                	item.rate=parseInt((item.rate)).toFixed(2);
                    if (item.isSelected == true) {
                    	var isFound = self.model.ItemDetailList.find(function (x) {
							return x.itemId == item.itemId;
						});
                    	if (!isFound) {
                            if(item.uomResponses != undefined){
                                item.uomDatalist = item.uomResponses.find(function (uom) {
					                return item.uomTypeId == uom.uomTypeId;
					              });     
					              item.uomType = item.uomDatalist.uomDetails;
					              
					              if(item.isThreeUOMApplicable == false){
					                  item.uomListdata = item.uomResponses;
					                  item.uomResponses = [];
					                  angular.forEach(item.uomListdata, function(data){
					                    if(data.uomType != 'Stock'){
					                      item.uomResponses.push(data);
					                    }
					                  });
					                } 
					            var checkQty = false;
                                if(item.selectedBatch.quantity == undefined || item.selectedBatch.quantity == null || item.selectedBatch.quanity == ""){
                                    item.selectedBatch.quantity = 0;
                                    if(item.quantity != undefined && item.quantity != null && item.quantity  != "" && item.quantity  != 0){
                                    	checkQty = true;
                                    }
                                }
                                
                         		if(item.isThreeUOMApplicable == false){
                         			item.uomListdata.forEach(function (uom) {
  					                  if (uom.uomTypeId == 1) {
  					                    conversionPurchase = uom.conversion;
  					                  }
  					                  else if (uom.uomTypeId == 2) {
  					                    conversionStock = uom.conversion
  					                  }
  					                  else {
  					                    conversionDespense = uom.conversion
  					                  }
  					                });
  					                if (item.uomTypeId == 1) {
                                        item.receivedQtyCon = ((checkQty == true ? item.quantity : item.selectedBatch.quantity )+"*"+ conversionPurchase);
                                        item.receivedQtyCalculation = ((checkQty == true ? item.quantity : item.selectedBatch.quantity ) * conversionPurchase);
                                        item.uomUnitData = item.uomListdata[1].uomUnit;
  					                }
  					                else if (item.uomTypeId == 2) {
  					                }
  					                else {
  					                    item.receivedQtyCon = ((checkQty == true ? item.quantity : item.selectedBatch.quantity ));
                                        item.receivedQtyCalculation = ((checkQty == true ? item.quantity : item.selectedBatch.quantity ));
                                        item.uomUnitData = item.uomListdata[1].uomUnit;
  					                }
                         		}else{
                                    item.uomResponses.forEach(function (uom) {
                                        if (uom.uomTypeId == 1) {
                                            conversionPurchase = uom.conversion;
                                        }
                                        else if (uom.uomTypeId == 2) {
                                            conversionStock = uom.conversion
                                        }
                                        else {
                                            conversionDespense = uom.conversion
                                        }
                       			    });
                                    if (item.uomTypeId == 1) {
                                        item.receivedQtyCon = ((checkQty == true ? item.quantity : item.selectedBatch.quantity ) +"*"+ conversionPurchase +"*"+ conversionStock);
                                        item.receivedQtyCalculation = ((checkQty == true ? item.quantity : item.selectedBatch.quantity ) * conversionPurchase * conversionStock);
                                        item.uomUnitData = item.uomResponses[2].uomUnit;
   					                }
   					                else if (item.uomTypeId == 2) {
                                        item.receivedQtyCon = ((checkQty == true ? item.quantity : item.selectedBatch.quantity ) +"*"+ conversionStock);
                                        item.receivedQtyCalculation = ((checkQty == true ? item.quantity : item.selectedBatch.quantity ) * conversionStock);
                                        item.uomUnitData = item.uomResponses[2].uomUnit;
   					                }
   					                else {
                                        item.receivedQtyCon = ((checkQty == true ? item.quantity : item.selectedBatch.quantity ));
                                        item.receivedQtyCalculation = ((checkQty == true ? item.quantity : item.selectedBatch.quantity ));
                                        item.uomUnitData = item.uomResponses[2].uomUnit;
   					                }
                         		}
                            }
                    	}
                        if(item.isThreeUOMApplicable == false){
                            if(item.uomListdata){
                                if(self.model.visitType.id == 2 || self.model.visitType.id == 3 || self.model.visitType.id == 4) {
                                    
                                    var isUOM = item.uomListdata.find(function(x){
                                        return !!x.ipUom;
                                    })
                                    if(!!isUOM){
                                        list.push(item)
                                    }
                                } else {
                                    var isUOM = item.uomListdata.find(function(x){
                                        return !!x.opUom;
                                    })
                                    if(!!isUOM){
                                        list.push(item)
                                    }
                                }  
                            }
                        }    
                        else{
                            if(item.uomResponses){
                                if(self.model.visitType.id == 2 || self.model.visitType.id == 3 || self.model.visitType.id == 4) {
                                    
                                    var isUOM = item.uomResponses.find(function(x){
                                        return !!x.ipUom;
                                    })
                                    if(!!isUOM){
                                        list.push(item)
                                    }
                                } else {
                                    var isUOM = item.uomResponses.find(function(x){
                                        return !!x.opUom;
                                    })
                                    if(!!isUOM){
                                        list.push(item)
                                    }
                                }  
                            }
                        }                                       
                    }
                });

                if(checkLasa){
                    checkLasa = false;
                    growl.warning("Selected Items contains LASA drugs..!",{
                      title : WARNING_MSG
                    });
                }

                for(let item of list) {
                    if(item.isBatchRequired == false){
                    }

                    angular.forEach(item.batches, function(batchData,key){
                        if(item.isBatchRequired == false){
                        	if( batchData.batchNo!=undefined)
                            {

                            }
                            else
                            {
                              batchData.batchNo = 'NA';          
                            }
                        }
                        
                        item.selectedBatch = item.batches.find(function (data) {
                            return item.batchId == data.batchId;
                        });
                    });
                    var selectedUOM = item.uomResponses.find(function (uom) {
                        return item.uomTypeId == uom.uomTypeId;
                    });
                    var sellingPrice = !!item.selectedBatch ? commonDetailService.fixedDecimal(item.selectedBatch.mrp) : 0;
                    item.selectedBatch.rate = commonDetailService.calculateRate(item.selectedBatch.uomResponses, item.uomTypeId, sellingPrice);
                    var data = {
                        "mrp": item.selectedBatch.rate,
                        "isTaxInc": self.model.taxInclusive,
                        "markup": item.markUpDown,
                        "markUpDownPer" : item.markUpDownPer,
                        "discount": item.discount == 0 || item.discount == undefined ? item.discountPerItem== 0 || item.discountPerItem == undefined  ? item.discountPerItem : item.discount : 0,
                        "isDiscPercent": !!item.discountType && item.discountType == 1 ? true : false,
                        "tax": item.taxPercentage,
                        "qty": item.selectedBatch.quantity,
                        "patientClassPercentage": item.patientClassPercentage
                    }
                    $scope.patientClassPercentage = data.patientClassPercentage;
         
                    var response = commonDetailService.calculateAmount(data);
                    item.selectedBatch.rate=response.rate;
                    item.rate = response.rate;
                    item.taxAmount = response.taxAmount;
                    item.totalAmount = response.totalAmount;
                    item.netAmount = response.netAmount;
                    item.patientClassAmount = response.patientClassAmount;
                    item.unitPatientClassAmount = response.unitPatientClassAmount;
                    $scope.patientClassAmount = response.patientClassAmount;
                    $scope.unitPatientClassAmount = response.unitPatientClassAmount;
                    self.model.ItemDetailList.push(item);
                }
                
                self.model.SelectedItemsIds = [];
                self.model.NewItemList1 = [];
            }            
            /* end added by minesh for dropdown batch */

            var SelectAllItems = function () {
                if (self.model.selectAllRows) {
                    if(self.model.SelectedItemsIds){
                    	 self.model.NewItemList1 = [];
                      //  self.model.data = self.model.NewItemList.map(function (item) {
                    	self.model.SelectedItemsIds = self.model.pagination.data.map(function (item) {
                            item.isSelected = true;
                            self.model.isItemChecked = true;
                           // self.model.SelectedItemsIds.push(item.itemId);
                            self.model.NewItemList1.push(item);
                            return item.itemId;
                        });
                    }
                    else{
                     //   self.model.SelectedItemsIds = self.model.NewItemList.map(function (item) {
                    	self.model.SelectedItemsIds = self.model.pagination.data.map(function (item) {
                            item.isSelected = true;
                            self.model.isItemChecked = true;
                            self.model.NewItemList1.push(item);
                            return item.itemId;
                        });
                    }
                } else {
                    self.model.SelectedItemsIds = [];
                    self.model.NewItemList1 = [];
                   // self.model.NewItemList.forEach(function (item) {
                    self.model.pagination.data.forEach(function (item) {
                        item.isSelected = false;
                        self.model.isItemChecked = false;
                    });
                }
            }

            var SelectItem = function (item) {
                var itemIds = self.model.SelectedItemsIds;

                if (item.isSelected) {
                    itemIds.push(item.itemId);
                    self.model.NewItemList1.push(item);
                } else {
                    var index = itemIds.indexOf(item.itemId);
                    if (index > -1) {
                        itemIds.splice(index, 1);
                        self.model.NewItemList1.splice(index, 1);
                    }
                }

                if (self.model.pagination.data.every(isSelected)) {
                    self.model.selectAllRows = true;
                } else {
                    self.model.selectAllRows = false;
                }

                self.model.isItemChecked = false;
                self.model.pagination.data.forEach(function (item) {
                    if (item.isSelected == true) {
                        self.model.isItemChecked = true;
                        return;
                    }
                });
            }

            var isSelected = function (item) {
                return item.isSelected;
            }

            var getAllItemDetails = function () {
                var list = []
                self.model.NewItemList1.forEach(function (item) {                    
                    if (item.isSelected == true) {
                        if(self.model.visitType.id == 2 || self.model.visitType.id == 3 || self.model.visitType.id == 4) {
                            var isUOM = item.uomResponses.find(function(x){
                                return !!x.ipUom;
                            })
                            if(!!isUOM){
                                list.push(item)
                            }
                        } else {
                            var isUOM = item.uomResponses.find(function(x){
                                return !!x.opUom;
                            })
                            if(!!isUOM){
                                list.push(item)
                            }
                        }                     
                    }
                });
                for(let item of list) {
                    self.model.ItemDetailList.push(item);
                }
                // self.model.ItemDetailList = list;
            }
            var PopupAction = function (action, modal) {
                var popup = angular.element('#' + modal);
                self.model.action = action;
                if (action == "Open") {
                    popup.modal('show');
                } else {
                    popup.modal('hide');
                }
            }
    
            var ConfirmRemove = function (index) {
                self.model.PopupAction('Open', 'confirmRemove');
                self.model.itemForRemove = index;
            }

            var RemoveRowData = function (index) {
                // var ItemDetailList = self.model.ItemDetailList;
                // self.model.ItemDetailList = ItemDetailList.slice(0, index).concat(ItemDetailList.slice(index + 1));
                self.model.ItemDetailList.splice(index, 1);
                self.model.NewItemList1.splice(index, 1);
                self.model.PopupAction('Close', 'confirmRemove');
            }

            var ValidateQuantity = function (item) {
                var isValid = true;
                item['isQuantityValid'] = false;

                if (!item.quantity) {
                    item['isQuantityValid'] = true;
                    isValid = false;
                    growl.error('Please enter quantity')
                    $scope.saveBtnDis = true;
                }

                if (item.availableQuantity < item.quantity) {
                    item['isQuantityValid'] = true;
                    isValid = false;
                    growl.error('Entered quantity is exceeding the max available quantity');
                    $scope.saveBtnDis = true;
                }

                if (item.quantity == 0) {
                    item['isQuantityValid'] = true;
                    isValid = false;
                    growl.error('Enter quantity greater than zero')
                    $scope.saveBtnDis = true;
                }
                return isValid;
            }
            
            var Validate = function () {
                var isValid = true;

                self.model.isPatientNameValid = false;
                self.model.isVisitTypeValid = false;
                self.model.isVisitNoValid = false;
                self.model.isStoreValid = false;

                if (!self.model.patientName) {
                    self.model.isPatientNameValid = true;
                    isValid = false;
                    growl.error('Enter patient name')
                    $scope.saveBtnDis = true;
                }
                if (!self.model.visitType.id) {
                    self.model.isVisitTypeValid = true;
                    isValid = false;
                    growl.error('Select visit type')
                    $scope.saveBtnDis = true;
                }
                if (!self.model.store.id) {
                    self.model.isStoreValid = true;
                    isValid = false;
                    growl.error('Select Store')
                    $scope.saveBtnDis = true;
                }
                if(self.model.visitType){
                    if (self.model.visitType.id == 1) {
                        if (!self.model.visitNumber.visitNo) {
                            self.model.isVisitNoValid = true;
                            isValid = false;
                            growl.error('Select visit no')
                            $scope.saveBtnDis = true;
                        }
                        else if (self.model.visitNumber.visitNo == 'Select Visit No') {
                            self.model.isVisitNoValid = true;
                            isValid = false;
                            growl.error('Select visit no')
                            $scope.saveBtnDis = true;
                        }
                    } else {
                        if (!self.model.visitNumberText) {
                            self.model.isVisitNoValid = true;
                            isValid = false;
                            growl.error('Select visit no')
                            $scope.saveBtnDis = true;
                        }
                    }
                }

                var ItemDetailList = self.model.ItemDetailList;
                if (!ItemDetailList || ItemDetailList.length <= 0) {
                    isValid = false;
                    growl.error("Please add some items in the bill");
                    $scope.saveBtnDis = true;
                } else {
                    ItemDetailList.forEach(function (item) {

                    	var sItem = item.selectedBatch;
                    	
                    	sItem['isQuantityValid'] = false;

                        if (!sItem.quantity) {
                        	sItem['isQuantityValid'] = true;
                            isValid = false;
                            growl.error('Please enter quantity')
                            $scope.saveBtnDis = true;
                        }

                        if (sItem.availableQuantity < sItem.quantity) {
                        	sItem['isQuantityValid'] = true;
                            isValid = false;
                            growl.error('Entered quantity is exceeding the max available quantity');
                            $scope.saveBtnDis = true;
                        }

                        if (item.quantity == 0) {
                        	sItem['isQuantityValid'] = true;
                            isValid = false;
                            growl.error('Enter quantity greater than zero')
                            $scope.saveBtnDis = true;
                        }

                        if (!ValidateQuantity(sItem)){
                            isValid = false;
                        }
                    });
                }
                if(!isValid)
                {
                $scope.saveBtnDisabled=false;
                }
                return isValid;
            }

            var SetStatus = function (x) {
                var status = self.model.Status.find(function (obj) {
                    return obj.status == x;
                });
    
                return status.id;
            }
            $scope.saveBtnDis = true;
            $scope.saveBtnDisabled=false;
            var SaveNewbillableConsumption = function () {
                $scope.saveBtnDis = false;
                $scope.saveBtnDisabled=true;
                var ItemDetailList = self.model.ItemDetailList;
                
             // added by minesh to pass seleceted data from dropdownBatch
              // var selectedItemDetailList = ItemDetailList.map(ItemDetailList => ItemDetailList.selectedBatch); 
                
                if (Validate()) {
                    $scope.saveBtnDis = false;
                    var URI = CONSTANTS.PHARMACY.NURSING.BILLABLE_CONSUMPTION.SAVE_API;

                    var data = {
                        "patientId": !!self.model.patientId ? self.model.patientId : undefined,
                        "visitTypeId": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                        "admissionId": !!self.model.admissionId ? self.model.admissionId : undefined,
                        "encounterId": !!self.model.encounterId ? self.model.encounterId : undefined,
                        "wardId": !!self.model.ward.id ? self.model.ward.id : undefined,
                        "storeId": !!self.model.store.id ? self.model.store.id : undefined,
                        "bedId": !!self.model.bedId ? self.model.bedId : undefined,
                        "statusId": SetStatus('Completed'),
                        "approvedStatusId": SetStatus('Completed'),
                        "taxInclusive" : $scope.taxInclusive,
                        "items": [],
                        "unitId":$scope.unitId,
                        "organizationId":$scope.organizationId,
                        "userId":$scope.userId
                    }

                    var netAmount = 0;
                    var taxAmount = 0;
                    var totalAmount = 0;
                    var totalDiscountAmount = 0; 

                    // comment by minesh and added new function for save data from selected batch dropdown
                    var i = 1;
                    var checkLasa = false;
                    angular.forEach(ItemDetailList, function (item, key) {
//                        if(item.isLasa !== undefined && item.isLasa !== null && item.isLasa === true){
//                            checkLasa = true;
//                        }
                        var item = {
                            "isBatchReq": item.isBatchRequired,
                            "batchId": (item.selectedBatch.batchId==undefined||item.selectedBatch.batchId==null||item.selectedBatch.batchId=='')?0:item.selectedBatch.batchId,
                            "uomTypeId": item.uomTypeId,
                            "uomUnitId": item.uomUnitId,
                            "consumableQuantity": parseInt(item.selectedBatch.quantity),
                            "statusId": SetStatus('Pending'),
                            "leaseQuantity": item.leaseQuantity,
                            "netAmount": item.netAmount ? item.netAmount : 0,
                            "quantity": parseInt(item.selectedBatch.quantity),
                            "rate": item.rate ? item.rate : 0,
                            "taxAmount": item.taxAmount ? item.taxAmount : 0,
                            "taxId": item.taxId,
                            "taxPercentage": item.taxPercentage,
                            "totalAmount": item.totalAmount ? item.totalAmount : 0,
                            "productCategoryId":item.prodCatId,
                            "itemId":item.itemId,
                            "creditPayable":0,
                            "selfPayable":item.netAmount ? item.netAmount : 0,
                            "consessionPercentage": $scope.patientClassPercentage,
                            "consessionAmount": $scope.patientClassAmount,
                            "unitConsessionAmount": $scope.unitPatientClassAmount.toFixed(2)
                        }

                        netAmount = netAmount + (item.netAmount ? item.netAmount : 0);
                        taxAmount = taxAmount + (item.taxAmount ? item.taxAmount : 0);
                        totalAmount = totalAmount + (item.totalAmount ? item.totalAmount : 0);

                        data.items.push(item);
                        
                        if(i == ItemDetailList.length){
                            data.netAmount = netAmount;
                            data.roundOff = parseFloat((netAmount - Math.round(netAmount)).toFixed(2));
                            data.taxAmount = taxAmount;
                            data.totalAmount = totalAmount;
                            data.totalDiscountAmount = totalDiscountAmount;
                        }
                        i++;
                    });

//                    if(checkLasa){
//                        checkLasa = false;
//                        growl.warning("List contains LASA drugs..!",{
//                          title : WARNING_MSG
//                        });
//                    }

                    $rootScope.startLoader();
                    console.log(data);
                    //return false;
                    GenericService.serviceAction("POST", URI, data).then(
                        function (response) {
                            NavigteToBillableConsumption();
                            $rootScope.stopLoader();
                            growl.success('New billable consumption Successfully saved');
                            $scope.saveBtnDis = true;
                        },
                        function (err) {
                            $rootScope.stopLoader();
                            growl.error('Error while saving new billable consumption ');
                            $scope.saveBtnDis = true;
                        });
                    }
            }

            var CalculateLeaseQuantity = function (item) {
                item.isQuantityValid = false;

                var conversion = 1;
                var flag = false;
                var uomConversion = 1;
                var uomFlag = false;
        
                angular.forEach(item.uomResponses, function (value, key) {
                    if (value.uomTypeId == item.uomTypeId || flag) {
                        conversion = conversion * value.conversion;
                        flag = true;
                    }
                    if(item.selectedUOM != undefined){
                        if (value.uomTypeId == item.selectedUOM.uomTypeId || uomFlag) {
                            uomConversion = uomConversion * value.conversion;
                            uomFlag = true;
                        }
                    }
                    
                });
        
                var leaseQuantity = conversion * parseInt(item.availableQuantity);

                return {conversion:uomConversion,leaseQuantity:leaseQuantity};
                
               /* if(self.model.taxInclusive){
                    item.tax = (item.mrp * item.taxPercentage / 100);
                    item.rate = item.mrp - item.tax;
                } else {                    
                    item.rate = item.mrp;
                    item.tax = (item.mrp * item.taxPercentage / 100);
                }

                item.amount = item.leaseQuantity * item.rate;
                item.taxAmount = item.leaseQuantity * item.tax;
                item.netAmount = item.amount + item.taxAmount;
                item.totalAmount = item.amount + item.taxAmount;*/
            }

            var CalculateStock = function (item, uomList, selectedUom) {
                if (!!selectedUom) {
                  let calculateLeaseQuantity = CalculateLeaseQuantity(item);
                  let calculateStock = calculateLeaseQuantity.leaseQuantity;
                  let leaseStock = calculateLeaseQuantity.leaseQuantity;
                  let conversion = calculateLeaseQuantity.conversion;

                  var uomList = uomList.filter(function (uomList) {
                      if (!!uomList.uomTypeId) {
                        return uomList;
                      }
                    });

                  var newUomConvertedStock;
                  
                  if (selectedUom.uomType == "P" || selectedUom.uomType == "Purchase") {
                    //item.newPRQty = parseInt(prQuantity * (uom[2].conversion));
                	//newUomConvertedStock = parseInt(calculateStock / (uomList[0].conversion * uomList[1].conversion * uomList[2].conversion));
                	  newUomConvertedStock = leaseStock/conversion;
                  } else if (selectedUom.uomType == "S" || selectedUom.uomType == "Stock") {
                    //item.newPRQty = parseInt(prQuantity * (uom[1].conversion * uom[2].conversion));
                	//newUomConvertedStock = parseInt(calculateStock / (uomList[1].conversion * uomList[2].conversion));
                	  newUomConvertedStock = leaseStock/conversion;
                  } else if (selectedUom.uomType == "D" || selectedUom.uomType == "Dispense") {
                    //item.newPRQty = parseInt(prQuantity * (uom[0].conversion * uom[1].conversion * uom[2].conversion));
                	//newUomConvertedStock = parseInt(calculateStock / (uomList[2].conversion));
                	  newUomConvertedStock = leaseStock;
                  }
                } else {
                	newUomConvertedStock = 0;
                }
                console.log(newUomConvertedStock);
                item.uomAvailableQuantity=newUomConvertedStock;
                console.log(self.model.NewUniqueItemList.selectedBatch);
              }
            
            var NavigteToBillableConsumption = function () {        
                    $state.go('billableConsumption');
            }

            $scope.isWardChanged = function()
            {      
                self.model.patientName = "";
                self.model.uhid = "";
                self.model.visitNumber = "";
                self.model.visitNumberText = "";
                $scope.searchPatientList = [];
                $scope.patientSearchObj.uhidNumber = '';
                $scope.patientSearchObj.patientName = '';
                $scope.patientSearchObj.genderId = '';
                $scope.birthDate = '';
                $scope.patientSearchObj.identificationTypeId = '';
                $scope.patientSearchObj.identificationNumber = '';
    
                //start for nursing
                var URI = BASE_URL+ROOT_URL+"/api/global/getDetailedNursingStationList/"+$scope.userId+"/"+$scope.organizationId+"/"+$scope.unitId;
                GenericService.serviceAction("GET",URI,{}).then(function(response){
                    if (response.data.status == "success") {
                        $scope.departmentNursingStationMapperDtoslist = [];			  
                        $scope.departmentNursingStationMapperDtoslist = response.data.listObject;
                        //alert($scope.roleName);
                        if($scope.nurseRole == 1){
                            if($scope.departmentNursingStationMapperDtoslist.length > 0)
                            {
                                if($scope.departmentNursingStationMapperDtoslist.length > 1){
                                    angular.element('#nursingStationModal').modal('show');
                                    //$('#nursingStationModal').modal({backdrop: 'static', keyboard: false});
                                }
                                else
                                {
                                    var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+$scope.departmentNursingStationMapperDtoslist[0].nursingStationId+"/"+$scope.organizationId+"/"+$scope.unitId;
                                    GenericService.serviceAction("GET",URI,{}).then(function(response){
                                        if (response.data.status == "success") {
                                            $scope.departmentNursingStationListC = {};
                                            // console.log("fff",response.data.object);
                                            $scope.departmentNursingStationListC.departmentNursingStationlist = response.data.listObject;					  
                                            sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify($scope.departmentNursingStationListC));
                                            sessionStorage.setItem('specialtyNursingStationCookiesNur', JSON.stringify($scope.departmentNursingStationListC));
                                            sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(response.data.object));
                                            sessionStorage.setItem('nursingStationId', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationId));
                                            //$cookies.putObject('nursingStationDesc',$scope.departmentNursingStationMapperDtoslist.nursingStationDesc);
                                            sessionStorage.setItem('nursingStationDesc', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationDesc));
                                            $scope.wardListForGetBedList = response.data.object;
                                        }
                                    });
                                }
                            }
                        }
                    }
                });        
            }
    
            $scope.departmentNursing = {};
            $scope.selectsetNursingStation = function()
            {
                self.model.wardIdFromService = [];
               
                if($scope.departmentNursing == undefined || $scope.departmentNursing.length == 0){
                    growl.warning('Please select nursing station..!!');
                    return false;
                }else{
                $scope.wardListForGetBedList=[];
                var data = JSON.parse($scope.departmentNursing);
                //console.log("$scope.departmentNursing",$scope.departmentNursing);
                //console.log("$scope.departmentNursing ID",data.nursingStationId);
                var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+data.nursingStationId+"/"+$scope.organizationId+"/"+$scope.unitId;
                    GenericService.serviceAction("GET",URI,{}).then(function(response){
                        if (response.data.status == "success") {
                            $scope.departmentNursingStationListC = {};
                            //console.log('nursingStationsWardsList',response.data.object);
                            $scope.departmentNursingStationListC.departmentNursingStationlist = response.data.listObject;
                            sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify($scope.departmentNursingStationListC));
                            sessionStorage.setItem('specialtyNursingStationCookiesNur', JSON.stringify($scope.departmentNursingStationListC));
                            sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(response.data.object));
                            sessionStorage.setItem('nursingStationDesc', JSON.stringify(data.nursingStationDesc));
                            $scope.nursingStationId = data.nursingStationId;
                            sessionStorage.setItem('nursingStationId', JSON.stringify(data.nursingStationId));
                            $scope.nursingStationId = JSON.parse(sessionStorage.getItem('nursingStationId'));
                            $scope.wardListForGetBedList = response.data.object;
                            self.model.ward.id = $scope.wardListForGetBedList;
                            $scope.nursingStationCookieShow = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
                            self.model.wardIdFromService = $scope.wardListForGetBedList;
                            angular.element('#nursingStationModal').modal('hide');
                        }
                    });
                }
            }
    
            $scope.genderList = function () {
                var data = {};
                var URI = BASE_URL + ROOT_URL + GETACTIVEGENDERLIST;
                BillingGenericService.serviceAction(METHOD_GET, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
                .then(function(response) {
                    console.log(response);
                    $scope.genderList = [];
                    if (response.data.status == "success")
                        $scope.genderList = response.data.listObject;
                });
            }
            $scope.genderList();
    
            $scope.identificationType = function () {
                var data = {};
                var URI = BASE_URL + ROOT_URL + GETACTIVEIDENTIFICATION;
                BillingGenericService.serviceAction(METHOD_GET, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
                .then(function(response) {
                    console.log(response);
                    $scope.identificationList = [];
                    if (response.data.status == "success")
                        $scope.identificationList = response.data.listObject;
                });
            }
            $scope.identificationType();
    
            $scope.openDatePicker = function () {
                $scope.datepicker.opened = true;
            };
    
            $scope.birthDateOptions = {
                formatYear: 'yyyy',
                showWeeks: false,
                maxDate: new Date()
            };
    
            $scope.datepicker = {
                opened: false
            };
    
            $scope.searchPatient = function (patientSearchData) {
                self.model.patientName = "";
                self.model.uhid = "";
                self.model.visitNumber = "";
                self.model.visitNumberText = "";
                $scope.searchPatientList = [];
                if (!!$scope.birthDate) {
                    var birthDate = moment($scope.birthDate).format('YYYY-MM-DD');
                } else {
                    var birthDate = undefined;
                }
                if($scope.wardListForGetBedList == undefined){
                    $scope.wardListForGetBedList = $scope.nursingStationsWardsListId;
                }
                var URI = CONSTANTS.ROOT_URL + '/adt/patientSearchByMultipleCriteria/global';
                var data = {
                    "uhidNumber": patientSearchData.uhidNumber,
                    "patientName": patientSearchData.patientName,
                    "genderId": patientSearchData.genderId,
                    "mobileNumber": patientSearchData.mobileNumber,
                    "birthDate": birthDate,
                    "identificationNumber": patientSearchData.identificationNumber,
                    "identificationTypeId": patientSearchData.identificationTypeId,
                    "unitId": $scope.unitId,
                    "organizationId": $scope.organizationId,
                    "wardList": $scope.wardListForGetBedList,
                    "visitTypeId": self.model.visitType.id
                }
                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $scope.searchPatientList = response.data.listObject; 
                    $rootScope.stopLoader();
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                });
            }

            $scope.calculateReceivedQty = function(item){
                if(item.isThreeUOMApplicable == false){
                    if(item.uomTypeId == 1){
                        item.receivedQtyCon = (item.selectedBatch.quantity +"*"+ item.uomResponses[0].conversion);
                        item.receivedQtyCalculation = (item.selectedBatch.quantity * item.uomResponses[0].conversion);
                        item.uomUnitData = item.uomResponses[2].uomUnit;
                    }
                    else if(item.uomTypeId == 2){
                        
                    }
                    else{
                        item.receivedQtyCon = (item.selectedBatch.quantity);
                        item.receivedQtyCalculation = (item.selectedBatch.quantity);
                        item.uomUnitData = item.uomResponses[2].uomUnit;
                    }
                }
                else{
                    if(item.uomTypeId == 1){
                        item.receivedQtyCon = (item.selectedBatch.quantity +"*"+ item.uomResponses[0].conversion+"*"+ item.uomResponses[1].conversion);
                        item.receivedQtyCalculation = (item.selectedBatch.quantity * item.uomResponses[0].conversion * item.uomResponses[1].conversion);
                        item.uomUnitData = item.uomResponses[2].uomUnit;
                    }
                    else if(item.uomTypeId == 2){
                        item.receivedQtyCon = (item.selectedBatch.quantity +"*"+ item.uomResponses[1].conversion);
                        item.receivedQtyCalculation = (item.selectedBatch.quantity * item.uomResponses[1].conversion);
                        item.uomUnitData = item.uomResponses[2].uomUnit;
                    }
                    else{
                        item.receivedQtyCon = (item.selectedBatch.quantity);
                        item.receivedQtyCalculation = (item.selectedBatch.quantity);
                        item.uomUnitData = item.uomResponses[2].uomUnit;
                    }
                }
            }

          $scope.savePopup = function(){
                if(self.model.ItemDetailList !== undefined && self.model.ItemDetailList !== null && self.model.ItemDetailList.length>0){
                   var checkLasa = false;
                   angular.forEach(self.model.ItemDetailList, function(item){
                       if(item.isLasa !== undefined && item.isLasa !== null && item.isLasa === true){
                          checkLasa = true;
                       }
                   });
                    if(checkLasa){
                         checkLasa = false;
                          var popup = angular.element('#confirmSave');
                          popup.modal('show');
                    }
                    else{
                        SaveNewbillableConsumption();
                    }
                }
          }

        $scope.updateBatchQuantity = function () {
            var URI = BASE_URL + ROOT_URL + "/api/global/items/billableConsumption/updateBatchQuantity";

            var data = {};
            var dataList = [];
            angular.forEach(self.model.NewItemList1, function(item){
//                angular.forEach(item.batches, function(batch){
                    data.batchId = item.batchId;
                    data.storeId =  $cookies.getObject('cookieObject').storeId ;
                    data.availableQuantity = item.availableQuantity;
                    data.unitId = $scope.unitId;
                    data.organizationId = $scope.organizationId;
                    dataList.push(data);
//                });
            });

            if(dataList !== null && dataList.length > 0){
                GenericService.serviceAction("POST", URI, dataList).then(
                    function (response) {
                        $rootScope.stopLoader();
                        ActualgetAllItemDetailsForBatch();
                    },
                    function (err) {
                      $rootScope.stopLoader();
                });
            }
        }

            initializeController();
        }


        function config($stateProvider) {
            $stateProvider.state("addNewbillableConsumption", {
                url: "/addNewbillableConsumption",
                templateUrl: 'views/PharmacyInventory/InPatient/add-new-billable-consumption.html',
                controller: "AddNewbillableConsumption.Controller",
                controllerAs: "vm",
                title: 'Billable Consumption'
            });
        }

        angular
            .module("myApp")
            .config(config)
            .controller("AddNewbillableConsumption.Controller", addNewbillableConsumptionController);
    })();