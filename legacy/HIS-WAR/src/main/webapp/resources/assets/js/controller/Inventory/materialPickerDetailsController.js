(function () {
    "use strict";

    function materialPickerDetailsController($scope, $rootScope, $http, $state, $cookies, CONSTANTS, StatusService, commonDetailService, GenericService, $filter, growl, $window,stopTransactionService) {
        var self = this;
        // Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
        function initializeController() {
            $rootScope.loginpage = true;
            self.model = {
                id: '',
                requestData: [],


            };
            self.model.fromMaterialPicker = fromMaterialPicker;
            self.model.ApprovePicker = ApprovePicker;
            self.model.ApprovedQuantity = ApprovedQuantity;
            self.model.uomSelectEvent = uomSelectEvent;
            self.model.materialPickerDetail = [];
            self.model.batchOnChange = batchOnChange;
            self.model.validatePicker = validatePicker;
            self.model.AddDuplicateRow = AddDuplicateRow;
            self.model.RemoveRowData = RemoveRowData;
            self.model.setStatus = setStatus;
            self.model.smartIssueMaterialPicker = smartIssueMaterialPicker;
            self.model.ValidateQuantity = ValidateQuantity;
            self.model.ConfirmRemove = ConfirmRemove;
            self.model.SetFocus = SetFocus;
            self.model.BarcodeScan = BarcodeScan;
            self.model.AddBatchToDetail = AddBatchToDetail;
            self.model.getListByUserName = getListByUserName;
            fromMaterialPicker();
        }

        $scope.common = {
            userId:"",
            empCode:"",
            empName:"",    
        };

        var getListByUserName = function(name){	
            var URI = BASE_URL + ROOT_URL + "/api/global/searchEmployeeByName";
            var data = {
                organizationId: $scope.organizationId,
                unitId: $scope.unitId,
                firstName: name
            }
            $rootScope.startLoader();
            return GenericService.serviceAction("POST",URI,data).then(function(response){
                if (response.data.status == "success")
                    $rootScope.stopLoader();
                    console.log("getListByUserName", response.data.listObject);
                return response.data.listObject;
            });
        }

        $scope.selectUserId = function(info, model,label) {
            console.log(info);
            $scope.common.userId = info.userId;
            $scope.common.empName = info.empName + '-' + info.empCode;
        }

        var batchOnChange = function (selectedBatch, batch, item) {

            var conversionPurchase;
            var conversionStock;
            var conversionDespense;

            batch.selectedBatch.batchId = selectedBatch.batchId;
            batch.selectedBatch.expiry = selectedBatch.expiry;
            batch.expiry= selectedBatch.expiry;
            batch.batchId= selectedBatch.batchId;

            var storeUom = selectedBatch.uom.find(function (obj) {
                return obj.storeUom == true;
            });

            selectedBatch.uom.forEach(function (uom) {
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
                batch.selectedBatch.displayQuantity = Math.round((((selectedBatch.originalStck / conversionPurchase) / conversionStock) / conversionDespense));
            }
            else if (item.uomTypeId == 2) {
                batch.selectedBatch.displayQuantity = Math.round((selectedBatch.originalStck / conversionStock) / conversionDespense);
            }
            else {
                batch.selectedBatch.displayQuantity = Math.round(selectedBatch.originalStck / conversionDespense);
            }

            batch.selectedBatch.uomType = item.uomType;
            batch.selectedBatch.uomTypeId = item.uomTypeId;
            batch.selectedBatch.uomUnit = item.uomUnit;
            batch.selectedBatch.uomUnitId = item.uomUnitId;
            batch.uomType = item.uomType;
            batch.uomTypeId = item.uomTypeId;
            batch.uomUnit = item.uomUnit;
            batch.uomUnitId = item.uomUnitId;
            batch.availableStockQuantity= batch.selectedBatch.displayQuantity ;
            // batch.selectedBatch.displayQuantity =
			// Math.round(selectedBatch.availableStockQuantity /
			// storeUom.conversion);
        }

        function fromMaterialPicker() {
            var id = commonDetailService.getDataId();

            var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_PICKER.DETAILS_API + $scope.unitId + '/'+$scope.organizationId + '/' + id;

            var data = {};
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, data).then(
                function (response) {
                    self.model.materialPickerDetail = response.data;
                    if(self.model.materialPickerDetail.pickerId != undefined){
                        self.model.picker = self.model.materialPickerDetail.pickers.find(function (item) {
                            return response.data.pickerId == item.id;
                        });
                        angular.forEach(self.model.materialPickerDetail.pickers, function(pic, key){
                            if(self.model.materialPickerDetail.pickerId == pic.id){
                                self.model.index = key;
                            }
                        });
                        
                        self.model.id = self.model.materialPickerDetail.pickers[self.model.index];
                    }
                    self.model.remark = response.data.remark;
                    self.model.materialPickerDetail.items.forEach(function (item) {
                        if( self.model.materialPickerDetail.status == 'Pending')
                        {
                            if (!!item.batch) {
                                item['batches'] = [item.batch[0]];
                                item['batches']['uomList'] = [];
                            }
                            if(item.batch){
                                item.batch.forEach(function (b) {
                                    b.originalStck=b.availableStockQuantity;
                                });
                            }
                        }
                        if(self.model.materialPickerDetail.status == 'Completed'){
                            angular.forEach(item.batches, function(bat, key){
                                                 
                        var conversionPurchase;
                        var conversionStock;
                        var conversionDespense;
                                angular.forEach(bat.uom, function(uom){
                      
                                     if (uom.uomTypeId == 1) {
                                         conversionPurchase = uom.conversion;
                                      }
                             else if (uom.uomTypeId == 2) {
                                       conversionStock = uom.conversion
                                      }
                                   else {
                                           conversionDespense = uom.conversion
                                     }

                                      if(item.uomTypeId == uom.uomTypeId){
                                    	 bat.uomType = uom.uomType;
                                         bat.uomUnit = item.uomUnit;
                                         bat.uomTypeId = uom.uomTypeId;
                                         bat.uomUnitId = uom.uomUnitId;
                                          
                                    }

                                    bat.pickerQuantity = item.pickerQuantity;
                                });

                                                       
             if (item.uomTypeId == 1) {
                bat.availableStockQuantity = Math.round(((( bat.availableStockQuantity / conversionPurchase) / conversionStock) / conversionDespense));
            }
            else if (item.uomTypeId == 2) {
                 bat.availableStockQuantity = Math.round(( bat.availableStockQuantity / conversionStock) / conversionDespense);
            }
            else {
                bat.availableStockQuantity = Math.round( bat.availableStockQuantity / conversionDespense);
            }
                            });
                        }
                         if(self.model.materialPickerDetail.status == 'Partially Completed'){
                            angular.forEach(item.batches, function(bat, key){
                                bat.selectedBatch = item.batches.find(function (item) {
                                    return bat.batchId == item.batchId;
                                });
                                angular.forEach(bat.uom, function(uom){
                                    if(item.uomTypeId == uom.uomTypeId){
                                       bat.uomType = uom.uomType;
                                       bat.uomUnit = uom.uomUnit;
                                       bat.uomTypeId = uom.uomTypeId;
                                       bat.uomUnitId = uom.uomUnitId;
                                    }
                                    bat.pickerQuantity = item.pickerQuantity;
                                });
                            });
                        }
                        item['tempPickerQuantity'] = item.pickerQuantity;
                        if (item.stockAvailable == 0)
                            growl.error("Batch not available ",item.itemName);
                    });
                    
                    // added by minesh for total of availableQuantity from all
					// batch and get single Unique Batch
                /*
				 * var batchNo = []; var uniqueBatches = [];
				 * angular.forEach(self.model.materialPickerDetail.items,
				 * function(list,key){ if (!!list.batch) {
				 * list.batches.forEach(function (batch) { var index =
				 * batchNo.indexOf(batch.batchNo); if(index === -1) {
				 * batchNo.push(batch.batchNo); uniqueBatches.push(batch);
				 * }else{ uniqueBatches[index].availableStockQuantity +=
				 * batch.availableStockQuantity; batch.availableStockQuantity =
				 * uniqueBatches[index].availableStockQuantity ; } }); } });
				 */                    
                    // added by minesh for total of availableQuantity from all
					// batch and get single Unique Batch
                    
                    $rootScope.stopLoader();
                });
        }


        var uomSelectEvent = function (batch) {
            batch.uom.forEach(function (uom) {
                if (uom.uomTypeId == batch.uomTypeId) batch.selectedUom = uom;
            });
        }
        var SetFocus = function () {
            self.model.barcode = undefined;
            $window.document.getElementById('barcode').focus();
        }
        var BarcodeScan = function (event, barcode) {
            var enterCode = 13;
            if (!barcode && event.which === enterCode) {
                barcode = self.model.barcode;
            }

            if (!!barcode) {
                var itemId = barcode.split(' ')[0];
                var batchId = barcode.split(' ')[1];

                if (!!itemId && !!batchId) {                	
                    AddBatchToDetail(self.model.materialPickerDetail, itemId, batchId);                
                }

                event.stopPropagation();
                event.preventDefault();

                SetFocus();
            }
        }

        var AddBatchToDetail = function (worklist, itemId, batchId) {
            var itemId = itemId;
            var batchId = batchId;
            angular.forEach(self.model.materialPickerDetail.items, function (item, key) {

            	 angular.forEach(item.batch, function (batches, key) {
            		 if(batches.batchId == batchId){
            			 
            			// batchOnChange(batches, item.batchList);
            		 }
            		 
            	 });
            	
            	
            	
                /*
				 * let temp = item.batch.find(function (batch) { if
				 * (batch.batchId == batchId) {
				 * 
				 * 
				 * batchOnChange(batch, batch); } });
				 */

            });
        }
        var ApprovedQuantity = function (selectedBatch,batch, item) {
            if (batch.pickerQuantity > selectedBatch.availableStockQuantity) {
                console.log("Picker Qty : ",batch.pickerQuantity);
                console.log("Available Qty : ",selectedBatch.availableStockQuantity);

                // Invalid
                batch.pickerQuantity = '';
                growl.error("Picker Qty can not be greater than Available Stock Qty");
            }
            if (item.pickerQty > item.pendingQuantity) {
                batch.pickerQuantity = '';
                growl.error("Picker Qty can not be greater than Pending Indent Qty");
            }
        }

        var ApprovePicker = function () {
        	 if (validatePicker()) {
        		 
        		 ActualApprovePicker();
        		 /*
        	self.model.selectedStopIds = [];
        	angular.forEach(self.model.materialPickerDetail.items, function (item, key) {        	
        			  self.model.selectedStopIds.push(item.itemId);        		            			
        	  });
        	
        	 var stopData = {
 	                "itemIds": self.model.selectedStopIds,
 	                "storeId": self.model.materialPickerDetail.toStoreId	      		 
 	              };  	    
  	 	  stopTransactionService.GetstopTransactionData(stopData).then(function(responseData){        
           var StopTransactionData = responseData;  
           if(StopTransactionData.length > 0 ){    
                    var startdate =  $filter('date')(StopTransactionData[0].startDate, "dd-MM-yyyy");
                    var stopdate=  $filter('date')(StopTransactionData[0].stopDate, "dd-MM-yyyy");
                    growl.warning("Stop Transation is in Process for this Item(s) <br/>Start Date : "+ startdate +" <br/> Stop Date: "+ stopdate +" <br/> Days: "+ StopTransactionData[0].days +" <br/>  Hours: "+ StopTransactionData[0].hours +" <br/>  Minutes: "+ StopTransactionData[0].minutes +"  ");                   
                    $rootScope.stopLoader(); 
                    self.model.selectedStopIds = [];                  
                    return false;
            }
           else{
        	   ActualApprovePicker();
           }
        });
       */}
      }

        var ActualApprovePicker = function () {
            var store = self.model;
            var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_PICKER.SAVE_API;
            var completedItems = 0;
            if (validatePicker()) {
                var data = {
                    "id": commonDetailService.getDataId(),
                    "statusId": setStatus('Pending'),
                    // "approvalStatusId": 1,
                    "fromStoreId": self.model.materialPickerDetail.fromStoreId,
                    "toStoreId": self.model.materialPickerDetail.toStoreId,
                    "pickerId": $scope.common.userId,
                    "remark": self.model.remark,
                    "items": [],
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "storeId":$scope.storeId

                };

                var statusId = 0;
                let  totalPickerQuantity = 0; // added for resolved HIS-9408
												// Material picker screen saves
												// the data but status does not
												// get change
                angular.forEach(store.materialPickerDetail.items, function (item, key) {
                	// if (item.pickerQuantity > 0) {
                    if (item.pickerQuantity > 0) {
                        var storeItem = {
                            "id": item.id,
                            // "statusId": 17,
                            "pickerQuantity": parseInt(item.pickerQuantity),
                            "batches": []
                        }

                        angular.forEach(item.batches, function (batch) {
                            var batch = {
                                "batchId": batch.selectedBatch.batchId,
                                "batchNo" :batch.selectedBatch.batchNo,
                                "uomTypeId": batch.selectedBatch.uomTypeId,
                                "uomUnitId": batch.selectedBatch.uomUnitId,

                                "pickerQuantity": parseInt(batch.pickerQuantity)
                            }
                            storeItem.batches.push(batch);
                        });

                        /*
						 * if (data.statusId == setStatus('Pending') &&
						 * parseInt(item.pickerQuantity) > 0 &&
						 * parseInt(item.pickerQuantity) < item.pendingQuantity) {
						 * data.statusId = setStatus('Partially Completed'); }
						 */
                         if (data.statusId == setStatus('Pending') && parseInt(item.pickerQuantity) == item.pendingQuantity) {
                           // data.statusId = setStatus('Completed');
                            completedItems++;
                        }

                        data.items.push(storeItem);
                        // added code for resolved HIS-9408 Material picker
						// screen saves the data but status does not get change
                        totalPickerQuantity= totalPickerQuantity + (item.pickerQuantity ? item.pickerQuantity : 0 );
                        // end code for resolved HIS-9408 Material picker screen
						// saves the data but status does not get change
                    }
                });

                if (data.items.length > 0){
                    var total=self.model.materialPickerDetail.items.length;
                    console.log("Completed Items : ",completedItems);
                    console.log("Total Items : ",total);
                    console.log("Status : ",data.statusId);
                    if (completedItems == total )
                    {
                        data.statusId = setStatus('Completed');
                    }
                    if (completedItems < total )
                    {
                        data.statusId = setStatus('Partially Completed');
                    }
                    if (completedItems == 0 && totalPickerQuantity == 0 )
                    {
                        data.statusId = setStatus('Pending');
                    }
                    console.log("Final Status : ",data.statusId);
                    data.pickerId =self.model.id.id;
                    $rootScope.startLoader();
                    GenericService.serviceAction("POST", URI, data).then(
                        function (response) {
                            $rootScope.stopLoader();
                            NavigteToMaterialPicker();
                            growl.success(response.data.message);
                        },
                        function (err) {
                            $rootScope.stopLoader();
                            growl.error('Something Went Wrong');
                        });
                }
                else {
                    $rootScope.stopLoader();
                    NavigteToMaterialPicker();
                }
            }
        }

        function setStatus(x) {
            var status = self.model.statuses.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }

        StatusService.GetStatus().then(function (status) {
            self.model.statuses = status;
        });


        var NavigteToMaterialPicker = function () {
            $state.go('materialPicker');
        }

        var AddDuplicateRow = function (item) {
            var data = angular.copy(item.batches[0].selectedBatch);
            data.availableStockQuantity = "";
            data.pickerQuantity = '';
            data.batchDetails = '';
            data.batchId = '';
            data.batchNo = '';
            data.displayQuantity = '';
            data.expiry = '';
            data.sellingPrice = '';
            data.uomType = '';
            data.uomTypeId = '';
            data.uomUnit = '';
            data.uomUnitId = '';
            data.pickerQuantity = '';
            item.batches.push(data);
        }


        var RemoveRowData = function (item) {
            var index = item.batches.indexOf(item);
            item.batches.splice(index, 1);
        }

        function validatePicker() {

            var isValid = true;
            var materialPickerDetail = self.model.materialPickerDetail.items;
            if (materialPickerDetail.length < 1) {
                growl.error("Please validate all required fields");
                isValid = false;
            }

            var isBatchSelected = false;
            for (let item of materialPickerDetail) {
                if (!ValidateQuantity(item)) {
                    isValid = false;
                }
                for (let batch of item.batches) {
                    if (!isBatchSelected && batch.selectedBatch) {
                        isBatchSelected = true;
                    }
                }
            }


            if (!isBatchSelected) {
                growl.error("Please Select Batch");
                isValid = false;
            }

            for (let pickerDetail of materialPickerDetail) {
                if (pickerDetail.pickerQuantity > pickerDetail.pendingQuantity || pickerDetail.pickerQuantity > pickerDetail.displayQuantity) {
                    growl.error("Please Enter Valid Picker Quantity");
                    isValid = false;
                }
            }

            for (let pickerDetail of materialPickerDetail) {
                for (let picker of pickerDetail.batches) {
                    if (picker.pickerQuantity == 0 || picker.pickerQuantity == undefined) {
                        growl.error("Please Enter Valid Picker Quantity In batch");
                        isValid = false;
                    }


                }
            }

            return isValid;
        }



        var smartIssueMaterialPicker = function () {

            var URI = CONSTANTS.GLOBAL.SMART_ISSUE;

            var data = {
                "storeId": self.model.materialPickerDetail.toStoreId,
                "items": []
            };
            angular.forEach(self.model.materialPickerDetail.items, function (item, key) {
                var storeItem = {
                    "itemId": item.itemId,
                    "issueQuantity": item.pendingQuantity
                }
                data.items.push(storeItem);
            });
            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    self.model.smartIssueData = response.data;
                    self.model.smartIssueData.forEach(function (smart) {
                        angular.forEach(self.model.materialPickerDetail.items, function (item, key) {
                            if (smart.itemId == item.itemId) {
                                item.batches.forEach(function (batch) {
                                    smart.batch.forEach(function (smartBatch) {
                                        if (batch.batchId == smartBatch.batchId) {
                                            batch.pickerQuantity = smartBatch.issueQuantity;
                                            batch.selectedBatch = batch;
                                            batchOnChange(batch.selectedBatch, batch);
                                            self.model.activeItem = smart.itemId;
                                        }
                                    });

                                });
                            }
                        });
                    });
                    $rootScope.stopLoader();
                    growl.success(response.data.message);


                },

                function (err) {
                    $rootScope.stopLoader();
                    growl.error('Something Went Wrong');
                });
        }



        var ValidateQuantity = function (item) {
            var isValid = true;
            if (parseInt(item.pickerQuantity) > item.pendingQuantity) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error('Entered picker quantity is exceeding the pending indent quantity');
            }

            return isValid;
        }

        var ConfirmRemove = function (index) {
            self.model.materialPickerDetail.items.splice(index, 1);
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("materialPickerDetails", {
            url: '/detailsMaterialPicker',
            templateUrl: 'views/PharmacyInventory/InPatient/details-material-picker.html',
            controller: "MaterialPickerDetails.Controller",
            controllerAs: 'vm',
            title: 'Material Picker'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("MaterialPickerDetails.Controller", materialPickerDetailsController)

    angular
        .module("myApp")
        .filter('customSumBy', function () {
            return function (collection, column, value) {
                var total = 0; if (!value || value >0) value = 0;
                if (collection) {
                    collection.forEach(function (item) {
                        if (item[column])
                            total += parseFloat(item[column]);
                    });
                    total = total + value;
                }
                else total = value;
                return total;
            };
        });
})();
