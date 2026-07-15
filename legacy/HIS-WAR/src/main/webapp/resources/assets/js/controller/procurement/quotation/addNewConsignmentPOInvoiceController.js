(function () {
  "use strict";

  function addNewConsignmentPOInvoiceController($scope, $rootScope, reportService ,$http, $filter, $state, screenDataService,PaginationService, $cookies, CONSTANTS, GenericService, commonDetailService, cancelReasonService, StatusService, growl, $timeout) {
    var self = this;

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
          maxDate : new Date()
        },
        toDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate : new Date()
        },
        invoiceDate: new Date(),
        invoiceDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate : new Date()
        },
        selectAllRows: false,
        invoiceNumber:'',
        SelectedOrderIds: [],
        toStoreList: [{
          "store": "Select Delivery Store"
        }],
        fromStoreList: [{
          "store": "Select Requesting Store"
        }],
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

      cancelReasonService.GetReason().then(function(reason) {
        self.model.reasons = reason;
      });
      
      StatusService.GetStatus().then(function(status) {
        self.model.statusList = status;
      });

      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.GetPurchaseOrderList = GetPurchaseOrderList;
      
      self.model.isDisable = isDisable;
      self.model.ModalDismiss = ModalDismiss;
      self.model.SearchVendorNames = SearchVendorNames;
      self.model.AddVendorName = AddVendorName;
      self.model.search = '';
      self.model.prevSearch = '';
      self.model.vendorNames = [];
      //start new code
      self.model.GetPOPopup = GetPOPopup;
      self.model.GetApprovedConsignmentPOList=GetApprovedConsignmentPOList;
      self.model.Expand=Expand;
      self.model.selectAllPO = false;
      self.model.SelectAllPO=SelectAllPO;
      self.model.SelectPO = SelectPO;
      self.model.SelectAllItemsOfPO=SelectAllItemsOfPO;
      self.model.SelectItem=SelectItem;
      self.model.AddSelectedPO=AddSelectedPO;
      self.model.poList=[];
      //self.model.selectedPO = [];
      //self.model.selectedPOItems=[];
      self.model.selectedPOList = [];
      self.model.selectedPOItemList=[];
      self.model.ConfirmRemove=ConfirmRemove;
      self.model.RemoveItem=RemoveItem;
      self.model.OpenInvoiceDate = OpenInvoiceDate;
      self.model.SaveConsignmentPOInvoice=SaveConsignmentPOInvoice;
      self.model.NavigateToBack= NavigateToBack;

      PopulateDropdown();
      
      /* start code for resolved HIS-9322 Consignment PO invoice to have editable fields */
      GetTaxList();
      self.model.SelectTax=SelectTax;
      self.model.SelectTaxId=SelectTaxId;
      self.model.AddItemDiscount=AddItemDiscount;
      /* end code for resolved HIS-9322 Consignment PO invoice to have editable fields */
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
      self.model.toStore = self.model.toStoreList[0];
      self.model.fromStore = self.model.fromStoreList[0];
      self.model.status = self.model.statuses[0];
      self.model.poType = self.model.assetTypes[0];

      var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.toStoreList = self.model.toStoreList.concat(response.data.deliveryStores);
          self.model.fromStoreList = self.model.fromStoreList.concat(response.data.requestingStores);
          //self.model.poType =self.model.assetTypes.concat(response.data.assetType);
          $rootScope.stopLoader();
          
          var consignmentPOInvoice = commonDetailService.getDataId();
          if (!!consignmentPOInvoice) {
              commonDetailService.setDataId(null);
              GetConsignmentPOInvoiceDetail(consignmentPOInvoice);
            }
          
          //initialCall();
        },
        function (err) {
          $rootScope.stopLoader();
        });
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
                 });
                 self.model.selectedPOItemList = self.model.selectedPOList.items;
                 
                 self.model.vendorName = self.model.selectedPOList.vendorName;
                 self.model.vendorId = self.model.selectedPOList.vendorId
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

    var initialCall = function () {
        if (!screenDataService.getScreenData()) {
            GetPurchaseOrderList()
        } else {
          screenDataService.getScreenData().forEach(function (dataobj) {
            if (dataobj.screenName == 'poCreation'){
              var requestData = dataobj.searchrequest;
              
              self.model.status = self.model.statuses.find(function (status) {
                  return requestData.status.id == status.id;
              });
              self.model.store = self.model.storeList.find(function (store) {
                  return requestData.store.id == store.id;
              });
              self.model.fromstore = self.model.fromstoreList.find(function (store) {
                return requestData.fromstore.id == store.id;
              });
              self.model.fromDate = new Date(requestData.fromDate);
              self.model.toDate = new Date(requestData.toDate);
              self.model.vendorId = requestData.vendorId;
              self.model.vendorName = requestData.vendorName;
              self.model.purchaseOrderNo = requestData.purchaseOrderNo;
            }
          });

          GetPurchaseOrderList();
        }
      }

    var OpenInvoiceDate = function () {
        self.model.invoiceDateOpened = true;
      }
    
    var GetApprovedConsignmentPOList = function () {
        if (ValidateDate() && ValidateSearchItems()) {        	
          //var URI = CONSTANTS.GLOBAL.AGAINST_PO_SEARCH + self.model.store.id;
          var URI = CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_PO_INVOICE.GET_APPROVED_PO_API;          
          if (!self.model.vendorNameForPO) {
            self.model.vendorIdForPO = undefined;
          }
          var data = {
            // "isConsignment": self.model.isConsignment,
            "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
            "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
            "vendorId": self.model.vendorId ? self.model.vendorId : undefined,
            "vendorName": self.model.vendorName ? self.model.vendorName : undefined,
            "assetTypeId": self.model.poType.id ? self.model.poType.id : undefined,
            "poNumber": self.model.poNumber ? self.model.poNumber : undefined,
            "unitId": $scope.unitId
          }
          $rootScope.startLoaderPopup();
          GenericService.serviceAction("POST", URI, data).then(
            function (response) {
              self.model.poListTemporary = [];
              self.model.poList=[];
              $rootScope.stopLoaderPopup();
              self.model.poListTemporary = response.data;
              angular.forEach(self.model.poListTemporary, function (value,key) {//HIS-HIS-20190
            	  if(value.items != undefined && value.items != null && value.items.length > 0) {
            	  self.model.poList.push(value);
            	  }
              });
//              self.model.poList = response.data;
              var uniquePoList = [];
              self.model.poList.filter(function(item){
                var i = uniquePoList.findIndex(x => (x.id == item.id));
                if(i <= -1){
                	uniquePoList.push(item);
                }
              });
              self.model.poList=uniquePoList;
              angular.forEach(self.model.poList, function (po) {
                po.isSelected = false;
                //po.isDisabled = true;
                angular.forEach(po.items, function (item) {
                  item.isSelected = false;
                  //item.isDisabled = false;
                  
                  /* start code for resolved HIS-9322 Consignment PO invoice to have editable fields */
                 /* item.selectedTax = self.model.taxList.find(function(obj){
                      return obj.taxId == item.taxId;
                    });*/
                  /* end code for resolved HIS-9322 Consignment PO invoice to have editable fields */
                  
                });
              });
             
            },
            function (err) {
              $rootScope.stopLoaderPopup();
              self.model.poList = [];
              growl.error(err.data.message);
            });
        }
      }

    var Expand = function (grn, grnItemList) {
	      angular.forEach(grnItemList, function (value) {
	        value.Expanded = false;
	      });

	      grn.Expanded = !grn.Expanded;
	    }
    
    var SelectAllPO = function(){
	    	if(self.model.selectAllPO){
	    	self.model.poList.forEach(function(po){
	    		po.isSelected =true;
	    		po.isSelectedAllItems = true;
	    		angular.forEach(po.items,function (item){
	    			item.isSelected = true;
	    			//item.isDisabled = false;
	    			});
	    		});
	    	} else {
	    		self.model.poList.forEach(function(po){
		    		po.isSelected =false;
		    		po.isSelectedAllItems = false;
		    		angular.forEach(po.items,function (item){
		    			item.isSelected = false;
		    			//item.isDisabled = true;
		    			});
		    		});	    		
	    	}
    	}
    
    var SelectPO = function (po){
    	po.isSelectedAllItems = po.isSelected;
    	if(po.isSelected){
    		angular.forEach(po.items,function (item){
    			item.isSelected = true;
    			//item.isDisabled = false;
    			});  
    		}else{
    			angular.forEach(po.items,function (item){
    				item.isSelected = false;
    				//item.isDisabled = true;
    				});
    			}
    	
    	if (self.model.poList.every(isEverySelectedItem)) {
    		self.model.selectAllPO=true;
	        } else {
	        	self.model.selectAllPO = false;
	        }
    	
    	}
    
    var SelectAllItemsOfPO = function (po){
    	po.isSelected = po.isSelectedAllItems;
    	SelectPO(po);
    }
    
    var SelectItem = function (item,po){
    	if (item.isSelected){
    	 if (po.items.every(isEverySelectedItem)) {
	          po.isSelected = true;
	          po.isSelectedAllItems = true;
	        } else {
	        	 po.isSelected = false;
		         po.isSelectedAllItems = false;
	        }
    	} else {
    		 po.isSelected = false;
	         po.isSelectedAllItems = false;
    	}
    	
    	if (self.model.poList.every(isEverySelectedItem)) {
    		self.model.selectAllPO=true;
	        } else {
	        	self.model.selectAllPO = false;
	        }
    	
    }
    var isEverySelectedItem = function (item) {
        return item.isSelected;
      }
    
    /*var AddSelectedPO = function(){
    	self.model.poList.forEach(function(po){
    		if(po.isSelected){
    			self.model.selectedPO.push(po);
    			angular.forEach(po.items,function (item){
    				if(item.isSelected) {
    					item.poNumber=po.poNumber
    					item.poDate=po.poDate
    					item.grnNumber=po.grnNumber
    					
    					self.model.selectedPOItems.push(item)
    					
    				}
    				});
    			self.model.selectedPO.items=self.model.selectedPOItems;
    			}
    		});
    	self.model.poList=[];
    	self.model.selectAllPO = false;
    	console.log(self.model.selectedPO);
    	}*/
    var AddSelectedPO = function(){
    	self.model.poList.forEach(function(po){
    		let existPO=false;
    		var existPoItem =false;
    		var existObj =undefined;

    		if(po.isSelected){
    			if(self.model.selectedPOList.some(selectedPO => selectedPO.id === po.id )){
    	    		 console.log("Object is already in list");
    	    		 //var obj = $.grep(self.model.selectedPO, function(obj){return obj.id === 3;})[0];
    	    		 existObj = $filter('filter')(self.model.selectedPOList, {id: po.id })[0];
    	    		 existPO = true;
    	    	    } else{
    	    	    	//self.model.selectedPOList.push(po);
    	    	    }
    			
    			let selectedItem = [];
    			angular.forEach(po.items,function (item){
    				if(item.isSelected) {
    					item.poNumber=po.poNumber;
    					item.poDate=po.poDate;
    					item.grnNumber=po.grnNumber;    					
    					
    					 if(self.model.selectedPOItemList.some(selectedPOItems => selectedPOItems.id === item.id )){
    			    		 console.log("Object is already in list");
    			    		 existPoItem = true;
    			    	    } else{
    			    	    	selectedItem.push(item);
    			    	    }
    					 }
    				});//end po item loop
    			if(existPO && !existPoItem){
    				existObj.items.concat(selectedItem)
    			}else if (!existPO && !existPoItem){
    				po.items=selectedItem;
    				self.model.selectedPOList.push(po);
    			}
    			}//end po loop
    		});
    	
    	//set all selectedItemsPo for user view Only
    	self.model.selectedPOItemList = [];
    	self.model.selectedPOList.forEach(function(po){
    		angular.forEach(po.items,function (item){
    			self.model.selectedPOItemList.push(item);
    			});
    		});
    	console.log(self.model.selectedPOList);
    	console.log(self.model.selectedPOItemList);
    	}
    
    var existSelectedItem = function (item,itemList) {
    	 if(itemList.some(itemObj => itemObj.id === item.id )){
    		 console.log("Object is already in list");
    	    } else{
    	    	itemList.push(item);
    	    }
      }
    
    var ConfirmRemove = function(item) {
		var popup = angular.element('#itemRemovePopup');
		popup.modal('show');
		self.model.itemForRemove = item;
	}
    
    var RemoveItem = function(item) {
    	var newPO = [];
    	self.model.selectedPOList.map(function(poObj) {
					var newItems = [];
					poObj.items.map(function(itemObj) {
								if (itemObj.id !== item.id) {
									newItems.push(itemObj);
								} else {
									itemObj.isSelected = false;
									//itemObj.isDisabled = true;
									poObj.isSelectedAllItems = false;
								}
							});
					poObj.items = newItems;						
					if (poObj.items.length > 0) {
						newPO.push(poObj);							
					} else {
						poObj.isSelected = false;
						//poObj.isDisabled = true;
					}
				});

    	self.model.selectedPOList = newPO;
    	
		let selectedItemFromPO = [];
		self.model.selectedPOList.forEach(function(po) {
			angular.forEach(po.items, function (poItem) {
				selectedItemFromPO.push(poItem);
			});
		});
		self.model.selectedPOItemList=selectedItemFromPO;
		
		var popup = angular.element('#itemRemovePopup');
		popup.modal('hide');
    }
    
    var ValidateSearchItems = function () {
        var isValidItem = true;
        if (!self.model.poType || !self.model.poType.id) {
              isValidItem = false;
              growl.error('Please select Type');
            }
        if (!self.model.vendorName || !self.model.vendorId) {
            growl.error('Please select Vendor');
            isValidItem = false;
            self.model.isVendorValid = true;
          }
  
        return isValidItem;
      }
    
    var ValidateSaveItems = function () {
        var isValidItem = true;
        if (!self.model.poType || !self.model.poType.id) {
          isValidItem = false;
          growl.error('Please select Type');
        }
        if (!self.model.invoiceNumber|| self.model.invoiceNumber == '') {
        	isValidItem = false;
        	growl.error('Please Enter Invoice Number');
        }
        if (!self.model.vendorName || !self.model.vendorId) {
          growl.error('Please select Vendor');
          isValidItem = false;
          self.model.isVendorValid = true;
        }
        if (self.model.selectedPOItemList.length==0) {
            growl.error('Please Add Item For PO Invoice');
            isValidItem = false;
          }
        angular.forEach(self.model.selectedPOItemList, function(item){
          if (parseFloat(item.discountAmount) > (parseFloat(item.totalAmount))) {
            growl.error('Discount amount should be less than total amount');
            item.discountAmount = 0;
          }
          if (parseFloat(item.discount) > 100) {
            growl.error('Discount must be less than 100% of total amount');
          }
        });
  
        return isValidItem;
      }
    
    var SaveConsignmentPOInvoice = function () {
        if (ValidateSaveItems()) {
          if (!self.model.cpoiId) {
        	  CreateConsignmentPOInvoice();
          } else {
        	  UpdateConsignmentPOInvoice();
          }
        }
      }
    
    
    var CreateConsignmentPOInvoice = function () {
    	var data = [];
    	
    	angular.forEach(self.model.selectedPOList,
						function(po) {
							if (po.isSelected) {
								var dataPO = {
									"id" : po.id,//po id
									"grnId" : po.grnId,
									"requestingStoreId" : po.requestingStoreId,
									"issuingStoreId" : po.storeId,
									"vendorId" :  self.model.vendorId ? self.model.vendorId : undefined,
									"assetTypeId" : self.model.poType.id ? self.model.poType.id : undefined,
									"invoiceNumber": self.model.invoiceNumber ?self.model.invoiceNumber : undefined,
									"invoiceDate" : moment(self.model.invoiceDate).format('YYYY-MM-DD'),
									"items" : [],
                  "organizationId": $scope.organizationId,
                  "userId": $scope.userId,
                  "unitId": $scope.unitId
								}
								angular.forEach(po.items,
												function(item) {
													if (item.isSelected) {
														var itemDetail = {													
															"grnDetailId" : item.grnDetailId,
															"id" : item.id,//po detail id
															"specification" : item.specification,															
															"remark" : item.remark,
															/* start code for resolved HIS-9322 Consignment PO invoice to have editable fields */
															"cop" : item.cop,
															"mrp" : item.mrp,
															"taxId" : item.taxId,
															"taxPercentage" : item.taxPercentage,
															"discount" : item.discount,
															"totalAmount" : item.totalAmount,
															"discountAmount" : item.discountAmount,
															"taxAmount" :  parseFloat(item.taxAmount).toFixed(2),
															"totalOtherAmount" : item.totalOtherAmount,
															"netAmount" : parseFloat( item.netAmount).toFixed(2)
															/* end code for resolved HIS-9322 Consignment PO invoice to have editable fields */
															}
														
														dataPO.items.push(itemDetail);
														}
												});
								data.push(dataPO);
							}
						}); 

		$rootScope.startLoader();
		var URI = CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_PO_INVOICE.SAVE_API;

		GenericService.serviceAction("POST", URI, data).then(
						function(response) {
							NavigateToBack();
							$rootScope.stopLoader();
							growl.success('Consignment PO Invoice Detail Save Successfully');
						},
						function(err) {
							$rootScope.stopLoader();

							if(err.data.summary == "Invoice number already exists"){
								growl.error(err.data.summary);// HIS-14839	
							}else{
								growl.error('Error While Saving Consignment PO Invoice Detail');
							}
							
						});
    }
    
    var UpdateConsignmentPOInvoice = function () {
    	let data;
    	let  selectedPOInvoiceItem= [];
    	angular.forEach(self.model.selectedPOList.items,function(item) {
    		if (item.isSelected || true) {
    			var itemDetail = {
    					//"grnDetailId" : item.grnDetailId,
    					"id" : item.id,//po detail id
    					"specification" : item.specification,
    					"remark" : item.remark,
    					/* start code for resolved HIS-9322 Consignment PO invoice to have editable fields */
						"cop" : item.cop,
						"mrp" : item.mrp,
						"taxId" : item.taxId,
						"taxPercentage" : item.taxPercentage,
						"discount" : item.discount,
						"totalAmount" : item.totalAmount,
						"discountAmount" : item.discountAmount,
						"taxAmount" :  parseFloat(item.taxAmount).toFixed(2),
						"totalOtherAmount" : item.totalOtherAmount,
						"netAmount" : parseFloat( item.netAmount).toFixed(2),
                        
						/* end code for resolved HIS-9322 Consignment PO invoice to have editable fields */
    					}
    			selectedPOInvoiceItem.push(itemDetail);
    			}
    		});
    	
    	self.model.selectedPOList.items=selectedPOInvoiceItem;
    	self.model.selectedPOList.unitId=$scope.unitId;
         self.model.selectedPOList.userId= $scope.userId;
         self.model.selectedPOList.invoiceNumber=self.model.invoiceNumber;
    	data = self.model.selectedPOList;
		$rootScope.startLoader();
		var URI = CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_PO_INVOICE.UPDATE_DETAIL_API;

		GenericService.serviceAction("PATCH", URI, data).then(
						function(response) {
							NavigateToBack();
							$rootScope.stopLoader();
							growl.success('Consignment PO Invoice Details Update Successfully');
						},
						function(err) {
							$rootScope.stopLoader();
							growl.error('Error While Updating Consignment PO Invoice Details');
						});
    }
    
    var  NavigateToBack = function () {      
        $state.go('consignmentPOInvoice');
      }
    
    function SearchVendorNames(search) {
      if (search.length < 1) return;

      if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
        self.model.prevSearch = search;
        var URI = CONSTANTS.GLOBAL.VENDOR_SEARCH_API + search + "/"+$scope.unitId;
        $rootScope.startLoaderPopup();
        return GenericService.serviceAction("GET", URI, {})
        .then(function (response) {
          if (!!response.data && response.data.length > 0) {
            self.model.vendorNames = response.data;
            $rootScope.stopLoaderPopup();
            return $filter('filter')(self.model.vendorNames, {
              $: search
            });
          } else {
            $rootScope.stopLoaderPopup();
            return nameNotFound(search);
          }
        }, function (err) {
          $rootScope.stopLoaderPopup();
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

    var GetPurchaseOrderList = function () {
      if (ValidateDate()) {
        var URI = CONSTANTS.PROCUREMENT.QUOTATION.PURCHSE_ORDER.GET_PO_API;

        if (!self.model.vendorName) {
          self.model.vendorId = undefined;
          self.model.vendorName = undefined;
        }

        var data = {
          "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
          "storeId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
          "vendorId": self.model.vendorId,
          "vendorName": self.model.vendorName ? self.model.vendorName : undefined,
          "poNumber": self.model.purchaseOrderNo ? self.model.purchaseOrderNo : undefined,
          "requestingStoreId": !!self.model.fromstore && !! self.model.fromstore.id ? self.model.fromstore.id : undefined,
          "statusId": !!self.model.status && !!self.model.status.id ? self.model.status.id : undefined,
          "unitId": $scope.unitId
        }

        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.pagination.data=[];
            self.model.purchaseOrders = response.data;
            if(!!response && !!response.data && response.data.length > 0){            
              PaginationService.searchData( self.model.purchaseOrders, self.model.pagination);
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
            "store": self.model.store,
            "fromstore": self.model.fromstore,
            "vendorId": self.model.vendorId,
            "vendorName": self.model.vendorName,
            "purchaseOrderNo": self.model.purchaseOrderNo
          },
          screenName: 'poCreation'
        }
        screenDataService.setScreenData(screenData);
      }
    }

    var NavigateToAddNew = function () {
      commonDetailService.setDataId(null);
      $state.go('addNewConsignmentPOInvoice');
    }

    var isDisable = function (purchase) {
      if (purchase.status != 'New') {
        return true;
      }
      return false;
    }

    var isSelected = function (purchase) {
      if (!isDisable(purchase))
        return purchase.isSelected;
      else
        return true;
    }
    
    var GetPOPopup = function (requestType) {
          	self.model.action = 'Open';
            //self.model.vendorNameForPO = self.model.vendorName;
            //self.model.vendorIdForPO = self.model.vendorId;
            var popup = angular.element('#getApprovedPO');
            popup.modal('show');
            self.model.searchPO = undefined;
            self.model.fromDate = new Date();
            self.model.toDate = new Date();
            self.model.poList=[];
        	self.model.selectAllPO = false;
      }
    
    var ModalDismiss = function (modal) {
        var popup = angular.element('#' + modal);
        popup.modal('hide');
        self.model.rejectNote = undefined;
        self.model.cancelReason = undefined;
      }
    
    /* start code for resolved HIS-9322 Consignment PO invoice to have editable fields */
    var GetTaxList = function () {
          var URI = CONSTANTS.ROOT_URL+"/api/procurements/quotation/consignmentstockpoinvoice/taxList/"+$scope.organizationId;
          GenericService.serviceAction("GET", URI, "").then(
                  function (response) {
                	  self.model.taxList = response.data;
                  },
                  function (err) {
                      $rootScope.stopLoader();
                      growl.error(err.data.status);
                    });
          }
    
    var SelectTax = function (tax, item) {
        if (!!tax.taxId) {
          item.selectedTaxPercent = tax.taxPercent.toFixed(2);
          item.taxPercentage = tax.taxPercent.toFixed(2);
          $timeout( function(){
            $scope.calculateRoundoff(item);
          },1000);
        }
        else {
          item.selectedTaxPercent = "";
          item.taxPercentage = "";
        }
      }
    var SelectTaxId = function (taxId, item) {
        if (!!taxId) {
        	item.taxPercentage = self.model.taxList.find(function(obj){
                return obj.taxId == taxId;
              }).taxPercent.toFixed(2);
        	
          $timeout( function(){
            $scope.calculateRoundoff(item);
          },1000);
        }
        else {
          item.taxPercentage = "";
        }
      }
    var AddItemDiscount = function (type, item, value, index) {
        if (type == 'percent') {
          item.discount = parseFloat(item.discount).toFixed(2);
          if (isNaN(item.discount) || !item.discount || item.discount == 0) {
            item.discount = 0;
            item.discountAmount = 0;
            $timeout( function(){
              $scope.calculateRoundoff(item);
            },1000);
          } else {
            if (parseFloat(item.discount) > 100) {
              //self.model.discountPercent[index] = true;
              growl.error('Discount must be less than 100% of total amount');
            } else {
            	//item.poAmount=item.totalAmount
              if (!item.totalAmount) {
                item.discountAmount = 0;
              } else {
                item.discountAmount = (item.totalAmount) * (item.discount / 100);
                item.discountAmount = parseFloat(item.discountAmount).toFixed(2);
                $timeout( function(){
                  $scope.calculateRoundoff(item);
                },1000);
              }
            }
          }

        } else if (type == 'amount') {
          item.discountAmount = parseFloat(item.discountAmount).toFixed(2);
          if (isNaN(item.discountAmount) || !item.discountAmount || item.discountAmount == 0) {
            item.discountAmount = 0;
            item.discountPercent = 0;
            item.discount = 0;
            $timeout( function(){
              $scope.calculateRoundoff(item);
            },1000);
          } else {
            if (parseFloat(item.discountAmount) > (parseFloat(item.totalAmount))) {
              growl.error('Discount amount should be less than total amount');
              item.discountAmount = 0;
            } else {
              if (!item.totalAmount) {
                item.discount = 0;
              } else {
                item.discount = ((parseFloat(item.discountAmount) / item.totalAmount) * 100);
                item.discount = parseFloat(item.discount).toFixed(2);
                $timeout( function(){
                  $scope.calculateRoundoff(item);
                },1000);
              }
            }
          }
        }
      }
    
    $scope.amountround1 = 0;
    $scope.calculateRoundoff = function (item) {
        /*if(item.otherChargesAmount == undefined)
        {
          item.otherChargesAmount = 0;
        }
        $scope.amountround1 = $scope.amountround1 + (item.poAmount - parseInt(item.discountAmount) + item.taxAmount + item.otherChargesAmount);
        $scope.amountround = $scope.amountround1.toFixed();
        $scope.amount1 = parseFloat(self.model.totalNetAmount).toFixed(2);
        $scope.roundOff1 = parseInt($scope.amountround) - $scope.amount1;
        if($scope.roundOff1 < 0.50){
          self.model.totalNetAmount1 = $scope.amountround;
          $scope.roundOff = parseInt($scope.amountround) - $scope.amount1;
        }
        if($scope.roundOff1 >= 0.50){
          self.model.totalNetAmount1 = math.round($scope.amount1);
          $scope.roundOff = self.model.totalNetAmount1 - $scope.amount1;
        }*/
      }
    /* end code for resolved HIS-9322 Consignment PO invoice to have editable fields */

    initializeController();
  }

  function config($stateProvider) {
		$stateProvider.state("addNewConsignmentPOInvoice",
						{
							url : "/addNewConsignmentPOInvoice",
							templateUrl : 'views/procurement/quotation/add-new-consignment-PO-invoice.html',
							controller : "addNewConsignmentPOInvoice.Controller",
							controllerAs : "vm",
							title : 'Add New Consignment PO Invoice'
						});
	}

	angular.module("myApp").config(config).controller(
			"addNewConsignmentPOInvoice.Controller",
			addNewConsignmentPOInvoiceController);
})();