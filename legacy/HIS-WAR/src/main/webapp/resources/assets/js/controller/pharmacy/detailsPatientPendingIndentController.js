(function () {
    "use strict";
    function detailsPatientPendingIndentController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, $window, $filter, $state, growl, CurrencyService, commonDetailService, BillingGenericService, StatusService, cancelReasonService,pharamcyContractService,stopTransactionService,$timeout) {
        var self = this;
        var itemId;

        var cookieObjectAppConfig = {};
        [cookieObjectAppConfig] = $rootScope.checkCookies();

        var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
        $scope.taxInclusive = cookieObjectAppConfig.taxInclusive;
        $scope.statusId = 4; 
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
        function initializeController() {
            $rootScope.loginpage = true;

            self.model = {
                carrierList: [{
                    "carrierName": "Select carrier"
                }],
            };

            self.model.taxInclusive = cookieObjectAppConfig.taxInclusive;

            //Methods
            StatusService.GetStatus().then(function (status) {
                self.model.Status = status;
            })

            self.model.batchOnChange = batchOnChange;
            self.model.onChange = onChange;
            self.model.AddDuplicateRow = AddDuplicateRow;
            self.model.AddDuplicateRowSubstitute=AddDuplicateRowSubstitute;
            self.model.RemoveRowData = RemoveRowData;
            self.model.RemoveRowDataSubstitute = RemoveRowDataSubstitute;
            self.model.SavePendingIndent = SavePendingIndent;
            self.model.AddBatchToDetail = AddBatchToDetail;
            GetDetailsPatientPendingIndent(commonDetailService.getDataId());
            $scope.patientId = pharamcyContractService.billObj.patientId;
            self.model.ValidateIssueQty = ValidateIssueQty;
            self.model.Validate = Validate;
            self.model.ClearBill = ClearBill;
            self.model.GetGenericMaster = GetGenericMaster;
            self.model.GetStrengthUnitList = GetStrengthUnitList;
            self.model.ViewCombinationDetails = ViewCombinationDetails;
            // self.model.SetFocus = SetFocus;
            self.model.substituteDetails = [];
            self.model.BarcodeScan = BarcodeScan;
            self.model.SubstituteItems = SubstituteItems;
            self.model.Add = Add;
            self.model.isItemChecked = false;
            self.model.SelectSubstituteItem = SelectSubstituteItem;
            self.model.SelectAllItems = SelectAllItems;
            self.model.AddDuplicateRowSubstituteForPlus = AddDuplicateRowSubstituteForPlus;
            self.model.AddSubstituteItem = AddSubstituteItem;
            self.model.RemoveSubstituteItem = RemoveSubstituteItem;

            // SetFocus();
            
            
            $scope.entitlementObj={
          		  "paymentEntitlementTypeId":1
            }	
        }
        
        $scope.autoBatch = cookieObject.autoBatch;
       // $scope.savedSubItem=false;
        $scope.batchFlag=false;
        var GetDetailsPatientPendingIndent = function (itemId) {
            self.model.carrier = self.model.carrierList[0];
            var URI = CONSTANTS.PHARMACY.IN_PATIENT_PHARMACY.PATIENT_CONSUMABLE_ISSUE.DETAIL_API + $scope.unitId + "/"+ $scope.storeId + "/" + itemId +"/1";
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                  
                    self.model.patientPendingIndentDetailList = response.data;
                    $scope.getDischargeStatusId(response.data.admissionId);
                    $scope.length = response.data.items.length;
                    $scope.storeId = self.model.patientPendingIndentDetailList.storeId;
                    $scope.issueingtoreId = self.model.patientPendingIndentDetailList.issuingStoreId;
                    self.model.carrierList = self.model.carrierList.concat(response.data.carrier);
                    $scope.orderQtyTotalSum=0;
                    $scope.batchIssuedArray=[];
                    angular.forEach(self.model.patientPendingIndentDetailList.items, function (value, key) {
                    	angular.forEach(value.batch,function(item){
                    		if(value.batchNo == item.batchNo){
                    			item.uom.find(function(uoml){
                                    if(uoml.uomTypeId== value.uomTypeId){
                                 	   value.uomType = uoml.uomDetails;
                                    }
                               });
                    		}
                  	});
                    	
                    	//if(value.parentIssueDetailId == null){
                        	// added by minesh for total of availableQuantity from
    						// all batch and get single Unique Batch
                    	value["previousIssuedQuantity"]=value.issueQuantity;
                            if(value.indentQuantity == value.issueQuantity){
                                value.flag = true;
                            }
                            else{
                                value.flag = false;
                            }
                          /*  var batchNo = [];
                            var uniqueBatches = [];
                        	
                        		if (!!value.batch) {
                        		value.batch.forEach(function (batch) {
                        			var index = batchNo.indexOf(batch.batchNo);                    			 
                                	if(index === -1)
                                	{
                                		batchNo.push(batch.batchNo);
                                		uniqueBatches.push(batch);
                                	}else{
                                			uniqueBatches[index].availableStockQuantity += batch.availableStockQuantity;
                        					//batch.availableStockQuantity = uniqueBatches[index].availableStockQuantity ;
                                	}
                        		});
                        		}                	
                        	value.batch = uniqueBatches;*/
                        	// added by minesh for total of availableQuantity from all  batch and get single Unique Batch
                            $scope.patientClassPercentage = value.patientClassPercentage;
                            var batchArray=[];
                            if(value.batchIssued){
                            value.batchIssued.forEach(function(batch2,index) {
                            value.batch.forEach(function(batch,index) {
                            	if(batch.batchId==batch2.batchId)
                            		{
                            		batch2["batchNo"]=batch.batchNo;
                            		batch2["expiry"]=batch.expiry;
                            		batch2["uomType"]=batch.uomType;
                            		batch2["uomUnit"]=batch.uomUnit;
                            		batch2["availableStockQuantity"]=batch.availableStockQuantity;
                            		batch2["isSelected"]=batch.isSelected;
                            		batch2["test"]=batch.test;
                            		batch2["isSelectedP"]=batch.isSelectedP;
                            		if(batch2.isSelected==true)
                        			{
                        			 if(batch2.batchNo == undefined){
                                         batch2.batchNo = batch.batchDetails;
                                     }
                                     if (!!value.markUpDownPer) {
                                         var markUpDown = (batch.sellingPrice * (value.markUpDownPer / 100));

                                         if (value.markUpDown == 1) {
                                             batch.sellingPrice = batch.sellingPrice + markUpDown;
                                         } else if (value.markUpDown == 0) {
                                             batch.sellingPrice = batch.sellingPrice - markUpDown;
                                         }
                                     }
                                     if(batch2.isSelected == true && batch2.issueQuantity){
                                         batch2.test = true;
                                     }
                                     batch2["issueQuantity1"] = batch2.issueQuantity;
                        			}
//                            		$scope.batchIssuedArray.push(batch2);
                            		}
                            	
                            	
                            })
                            })
                    }
                            value.batch.forEach(function(batch,index) {
                                if(batch.batchNo == undefined){
                                    batch.batchNo = batch.batchDetails;
                                }
                                if (!!value.markUpDownPer) {
                                    var markUpDown = (batch.sellingPrice * (value.markUpDownPer / 100));

                                    if (value.markUpDown == 1) {
                                        batch.sellingPrice = batch.sellingPrice + markUpDown;
                                    } else if (value.markUpDown == 0) {
                                        batch.sellingPrice = batch.sellingPrice - markUpDown;
                                    }
                                }
                                if(batch.isSelected == true && batch.issueQuantity){
                                    batch.test = true;
                                }
                                batch.issueQuantity1 = batch.issueQuantity;
                                if(batch.availableStockQuantity!=0)
                                	{
                                	batchArray.push(batch);
                                	}
                            });
                            value["batch1"]=batchArray;
                            // var tempBatch = value.batch.find(function(x){
                            //     return !!x.isSelected;
                            // });
                            value['batchList'] = [value.batch[0]];
                            value['tempIssueQuantity'] = value.issueQuantity;
                            
                           if(value.selectedItems!= undefined && value.selectedItems!=null && value.selectedItems.length > 0){
                        	  // $scope.savedSubItem=true;
                        	   angular.forEach(value.selectedItems, function(value1, key1){

                               	// added by minesh for total of availableQuantity from
           						// all batch and get single Unique Batch
                                   if(value1.indentQuantity == value1.issueQuantity){
                                       value1.flag = true;
                                   }
                                   else{
                                       value1.flag = false;
                                   }
                                
                               	// added by minesh for total of availableQuantity from all  batch and get single Unique Batch
                               	
                                   $scope.patientClassPercentage = value1.patientClassPercentage;
                                   value1.savedBatchList=[];
                                   value1.newBatch.forEach(function(batch) {
                                       if(batch.batchNo == undefined){
                                           batch.batchNo = batch.batchDetails;
                                       }
                                       if (!!value1.markUpDownPer) {
                                           var markUpDown = (batch.sellingPrice * (value1.markUpDownPer / 100));

                                           if (value1.markUpDown == 1) {
                                               batch.sellingPrice = batch.sellingPrice + markUpDown;
                                           } else if (value.markUpDown == 0) {
                                               batch.sellingPrice = batch.sellingPrice - markUpDown;
                                           }
                                       }
                                       if(batch.isSelected == true && batch.issueQuantity){
                                           batch.test = true;
                                       }
                                       batch.issueQuantity1 = batch.issueQuantity;
                                   });
                                   value1['batchList'] = [value1.newBatch[0]];
                                   value1['batch'] = [value1.newBatch[0]];
                                   value1['tempIssueQuantity'] = value1.issueQuantity;
                                   value1['savedSubItem'] = true;
                                   value1['savedBatchList']=value1.newBatch;
                                   value1['uoms'] = value1.newBatch[0].uom;
                               }); 
                           }
                           
                           if(value.batchList[0] == undefined){
                        	   value.newIndentQuantity = value.indentQuantity;
                           }else{
                        	   if (value.uomTypeId == 1) {
                            	   value.indentQuantity=value.indentQuantity*(value.batchList[0].uom[0].conversion * value.batchList[0].uom[1].conversion * value.batchList[0].uom[2].conversion);
               	                } else if (value.uomTypeId == 2) {
               	            	    value.indentQuantity= value.indentQuantity*(value.batchList[0].uom[1].conversion * value.batchList[0].uom[2].conversion);
               	                } else if (value.uomTypeId == 3) {
               	            	    value.indentQuantity=value.indentQuantity*(value.batchList[0].uom[2]!=undefined && value.batchList[0].uom[2]!=null ? value.batchList[0].uom[2].conversion:1);
               	                }
                                $scope.convertAllBatchStock(value.batchList[0],value);   
                           }
                           
                            
                    	//}
                           value['batchList'] = [value.batch1[0]];
                    });
                    //chetana
                  /*  angular.forEach(self.model.patientPendingIndentDetailList.items, function(val1){
                        $scope.orderQtyTotalSum = $scope.orderQtyTotalSum + val1.indentQuantity;	
                    });*/
                    angular.forEach(self.model.patientPendingIndentDetailList.items, function(val1){
                        $scope.orderQtyTotalSum = $scope.orderQtyTotalSum + val1.indentQuantity;
                        if(val1.selectedItems!=undefined && val1.selectedItems.length > 0){
                        	 angular.forEach(val1.selectedItems, function(valSub){
                       	        $scope.orderQtyTotalSum = $scope.orderQtyTotalSum - valSub.indentQuantity;
                               });
                        }

                         if(val1.indentQuantity == val1.issueQuantity){
                          // val1.disableParentItem=true;
                           val1.substititeFlag=true;
                           val1.subFlagOnly=true;
                         }
                    });
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error('Something Went Wrong');
                });
        }
        
        $scope.getDischargeStatusId = function (admissionId) {
        	var URI = BASE_URL + ROOT_URL + "/api/ipd/admissions/patient/"+ admissionId;
    		BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
    			if(response.data.status == "success"){
    				if(response.data.object.dischargeStatusId == 1){
    					growl.warning("Patient discharge is marked...!!",{
    						  title : WARNING_MSG
    				    });
    				}
    			}
    		});
        }

    	$scope.convertAllBatchStock = function(selectedBatch,item)
		{
			 var newUOM = [];
			 if(item.isThreeUOMApplicable == false){
				 selectedBatch.uom.find(function (value) {
		                if (!!value.uomTypeId) {
		                    newUOM.push(value);
		                }
		            });
			 }else{
				  if( item.uoms == undefined || item.uoms == null){
					  item.uoms =  selectedBatch.uom;
	                }
				  item.uoms.find(function(value){
					 if (!!value.uomTypeId) {
		                    newUOM.push(value);
		                }
				 });
			 }
             if(item.issueQuantity == undefined || item.issueQuantity == null || item.issueQuantity == ""){
                item.issueQuantity = 0;
             }
	        	var quanity=item.availableStockQuantity;
                if(item.selectedItems != undefined){
                    if(item.selectedItems.length != 0){
                        angular.forEach(item.selectedItems, function(s){
                            s.uoms.find(function(uomd){
                                if(uomd.uomTypeId== item.uomTypeId){
                                   s.uomType = uomd.uomDetails;
                                }
                            });
                            angular.forEach(s.savedBatchList, function(b){
                                if(item.isThreeUOMApplicable == false){
                                    if(item.uomTypeId == 1){
                                        b.receivedQtyCon = (b.issueQuantity +"*"+ b.uom[0].conversion);
                                        b.receivedQtyCalculation = (b.issueQuantity * b.uom[0].conversion);
                                        b.uomUnitData = b.uom[2].uomUnit;
                                        s.receivedQtyCon = (b.issueQuantity +"*"+ b.uom[0].conversion);
                                        s.receivedQtyCalculation = (b.issueQuantity * b.uom[0].conversion);
                                        s.uomUnitData = b.uom[2].uomUnit;
                                    }
                                    else if(item.uomTypeId == 2){
        
                                    }
                                    else{
                                        b.receivedQtyCon = (b.issueQuantity);
                                        b.receivedQtyCalculation = (b.issueQuantity);
                                        b.uomUnitData = b.uom[2].uomUnit;
                                        s.receivedQtyCon = (b.issueQuantity);
                                        s.receivedQtyCalculation = (b.issueQuantity);
                                        s.uomUnitData = b.uom[2].uomUnit;
                                    }
                                }
                                else{
                                    if(item.uomTypeId == 1){
                                        b.receivedQtyCon = (b.issueQuantity +"*"+ b.uom[0].conversion+"*"+ b.uom[1].conversion);
                                        b.receivedQtyCalculation = (b.issueQuantity * b.uom[0].conversion * b.uom[1].conversion);
                                        b.uomUnitData = b.uom[2].uomUnit;
                                        s.receivedQtyCon = (b.issueQuantity +"*"+ b.uom[0].conversion+"*"+ b.uom[1].conversion);
                                        s.receivedQtyCalculation = (b.issueQuantity * b.uom[0].conversion * b.uom[1].conversion);
                                        s.uomUnitData = b.uom[2].uomUnit;
                                    }
                                    else if(item.uomTypeId == 2){
                                        b.receivedQtyCon = (b.issueQuantity +"*"+ b.uom[1].conversion);
                                        b.receivedQtyCalculation = (b.issueQuantity * b.uom[1].conversion);
                                        b.uomUnitData = b.uom[2].uomUnit;
                                        s.receivedQtyCon = (b.issueQuantity +"*"+ b.uom[1].conversion);
                                        s.receivedQtyCalculation = (b.issueQuantity * b.uom[1].conversion);
                                        s.uomUnitData = b.uom[2].uomUnit;
                                    }
                                    else{
                                        b.receivedQtyCon = (b.issueQuantity);
                                        b.receivedQtyCalculation = (b.issueQuantity);
                                        b.uomUnitData = b.uom[2].uomUnit;
                                        s.receivedQtyCon = (b.issueQuantity);
                                        s.receivedQtyCalculation = (b.issueQuantity);
                                        s.uomUnitData = b.uom[2].uomUnit;
                                    }
                                }
                            })
                        })
                    }
                }
                if(item.batchIssued != undefined){
                    if(item.batchIssued.length != 0){
                        angular.forEach(item.batchIssued, function(d){
                            selectedBatch.uom.find(function(uomd){
                                if(uomd.uomTypeId== item.uomTypeId){
                                   d.uomType = uomd.uomDetails;
                                }
                            });
                            if(item.isThreeUOMApplicable == false){
                                if(item.uomTypeId == 1){
                                    d.receivedQtyCon = (d.issueQuantity1 +"*"+ newUOM[0].conversion);
                                    d.receivedQtyCalculation = (d.issueQuantity1 * newUOM[0].conversion);
                                    d.uomUnitData = newUOM[2].uomUnit;
                                }
                                else if(item.uomTypeId == 2){
    
                                }
                                else{
                                    d.receivedQtyCon = (d.issueQuantity);
                                    d.receivedQtyCalculation = (d.issueQuantity);
                                    d.uomUnitData = newUOM[2].uomUnit;
                                }
                            }
                            else{
                                if(item.uomTypeId == 1){
                                    d.receivedQtyCon = (d.issueQuantity +"*"+ newUOM[0].conversion+"*"+ newUOM[1].conversion);
                                    d.receivedQtyCalculation = (d.issueQuantity * newUOM[0].conversion * newUOM[1].conversion);
                                    d.uomUnitData = newUOM[2].uomUnit;
                                }
                                else if(item.uomTypeId == 2){
                                    d.receivedQtyCon = (d.issueQuantity +"*"+ newUOM[1].conversion);
                                    d.receivedQtyCalculation = (d.issueQuantity * newUOM[1].conversion);
                                    d.uomUnitData = newUOM[2].uomUnit;
                                }
                                else{
                                    d.receivedQtyCon = (d.issueQuantity);
                                    d.receivedQtyCalculation = (d.issueQuantity);
                                    d.uomUnitData = newUOM[2].uomUnit;
                                }
                            }
                        })
                    }
                }
	        	if(item.isThreeUOMApplicable == false){
	        	 	 if (item.uomTypeId == 1) {
	                       item.newStock = Math.ceil(item.availableStockQuantity / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
	                       item.newIndentQuantity = Math.ceil(item.indentQuantity / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
	                       item.receivedQtyCon = (item.issueQuantity +"*"+ newUOM[0].conversion);
                           item.receivedQtyCalculation = (item.issueQuantity * newUOM[0].conversion);
                           item.uomUnitData = newUOM[2].uomUnit;
	                       if(item.newStock==0 && quanity >0)
	                      	{
	                    	   item.newStock=1;
	                      	}
	                   } else if (item.uomTypeId == 2) {
	                	   item.newStock = Math.ceil(item.availableStockQuantity / (newUOM[1].conversion * newUOM[2].conversion));
	                	   item.newIndentQuantity = Math.ceil(item.indentQuantity / (newUOM[1].conversion * newUOM[2].conversion));
	                       if(item.newStock==0 && quanity >0)
	                      	{
	                    	   item.newStock=1;
	                      	}
	                   } else if (item.uomTypeId == 3) {
	                	   item.newStock = Math.ceil(item.availableStockQuantity / (newUOM[2].conversion));
	                	   item.newIndentQuantity = Math.ceil(item.indentQuantity / (newUOM[2].conversion));
	                	   item.receivedQtyCon = (item.issueQuantity);
                           item.receivedQtyCalculation = (item.issueQuantity);
                           item.uomUnitData = newUOM[2].uomUnit;
	                   }
	        	}else{
	        	 	 if (item.uomTypeId == 1) {
	                       item.newStock = Math.ceil(item.availableStockQuantity / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
	                       item.newIndentQuantity = Math.ceil(item.indentQuantity / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
	                       item.receivedQtyCon = (item.issueQuantity +"*"+ newUOM[0].conversion+"*"+ newUOM[1].conversion);
                           item.receivedQtyCalculation = (item.issueQuantity * newUOM[0].conversion * newUOM[1].conversion);
                           item.uomUnitData = newUOM[2].uomUnit;
	                       if(item.newStock==0 && quanity >0)
	                      	{
	                    	   item.newStock=1;
	                      	}
	                   } else if (item.uomTypeId == 2) {
	                	   item.newStock = Math.ceil(item.availableStockQuantity / (newUOM[1].conversion * newUOM[2].conversion));
	                	   item.newIndentQuantity = Math.ceil(item.indentQuantity / (newUOM[1].conversion * newUOM[2].conversion));
	                       if(item.newStock==0 && quanity >0)
	                      	{
	                    	   item.newStock=1;
	                      	}
	                       item.receivedQtyCon = (item.issueQuantity +"*"+ newUOM[1].conversion);
                           item.receivedQtyCalculation = (item.issueQuantity * newUOM[1].conversion);
                           item.uomUnitData = newUOM[2].uomUnit;
	                   } else if (item.uomTypeId == 3) {
	                	   item.newStock = Math.ceil(item.availableStockQuantity / (newUOM[2]!=undefined && newUOM[2]!=null ? newUOM[2].conversion:1));
	                	   item.newIndentQuantity = Math.ceil(item.indentQuantity / (newUOM[2]!=undefined && newUOM[2]!=null ? newUOM[2].conversion:1));
	                	   item.receivedQtyCon = (item.issueQuantity);
                           item.receivedQtyCalculation = (item.issueQuantity);
                           item.uomUnitData = newUOM[2]!=undefined && newUOM[2]!=null ? newUOM[2].uomUnit:item.uomUnit;
	                   }
	        	}
	             
	              	item.availableStockQuantity=item.newStock;
		}
    	$scope.convertQtyOrdered = function (selectedBatch,item) {
       	 var newUOM = [];
       	 if(selectedBatch!=undefined && selectedBatch!=null)
       		 {
       	selectedBatch.uom.find(function (value) {
               if (!!value.uomTypeId) {
                   newUOM.push(value);
               }
           });
       	var quanity=selectedBatch.availableStockQuantity;
        if(selectedBatch.issueQuantity == undefined || selectedBatch.issueQuantity == null || selectedBatch.issueQuantity == ""){
            selectedBatch.issueQuantity = 0;
        }
       	if(item.isThreeUOMApplicable == false){
       	 if (item.uomTypeId == 1) {
      		selectedBatch["newStock"] = Math.round(selectedBatch.availableStockQuantity / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
            selectedBatch.receivedQtyCon = (selectedBatch.issueQuantity +"*"+ newUOM[0].conversion);
            selectedBatch.receivedQtyCalculation = (selectedBatch.issueQuantity * newUOM[0].conversion);
            selectedBatch.uomUnitData = newUOM[2].uomUnit;
//               if(selectedBatch.newStock==0 && quanity >0)
//              	{
//            	   selectedBatch.newStock=1;
//              	}
           } else if (item.uomTypeId == 2) {
        	   selectedBatch["newStock"] = Math.round(selectedBatch.availableStockQuantity / (newUOM[1].conversion * newUOM[2].conversion));
//               if(selectedBatch.newStock==0 && quanity >0)
//              	{
//            	   selectedBatch.newStock=1;
//              	}
           } else if (item.uomTypeId== 3) {
        	   selectedBatch["newStock"] = Math.round(selectedBatch.availableStockQuantity / (newUOM[2].conversion));
        	selectedBatch.receivedQtyCon = (selectedBatch.issueQuantity);
            selectedBatch.receivedQtyCalculation = (selectedBatch.issueQuantity);
            selectedBatch.uomUnitData = newUOM[2].uomUnit;
           }
       	}else{
       	 if (item.uomTypeId == 1) {
      		selectedBatch["newStock"] = Math.round(selectedBatch.availableStockQuantity / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
      		selectedBatch.receivedQtyCon = (selectedBatch.issueQuantity +"*"+ newUOM[0].conversion+"*"+ newUOM[1].conversion);
            selectedBatch.receivedQtyCalculation = (selectedBatch.issueQuantity +"*"+ newUOM[0].conversion+"*"+ newUOM[1].conversion);
            selectedBatch.uomUnitData = newUOM[2].uomUnit;
//               if(selectedBatch.newStock==0 && quanity >0)
//              	{
//            	   selectedBatch.newStock=1;
//              	}
           } else if (item.uomTypeId == 2) {
        	   selectedBatch["newStock"] = Math.round(selectedBatch.availableStockQuantity / (newUOM[1].conversion * newUOM[2].conversion));
        	   selectedBatch.receivedQtyCon = (selectedBatch.issueQuantity +"*"+ newUOM[1].conversion);
               selectedBatch.receivedQtyCalculation = (selectedBatch.issueQuantity * newUOM[1].conversion);
               selectedBatch.uomUnitData = newUOM[2].uomUnit;
//               if(selectedBatch.newStock==0 && quanity >0)
//              	{
//            	   selectedBatch.newStock=1;
//              	}
           } else if (item.uomTypeId== 3) {
        	   selectedBatch["newStock"] = Math.round(selectedBatch.availableStockQuantity / (newUOM[2]!=undefined && newUOM[2]!=null ? newUOM[2].conversion : 1));
        	selectedBatch.receivedQtyCon = (selectedBatch.issueQuantity);
            selectedBatch.receivedQtyCalculation = (selectedBatch.issueQuantity);
            selectedBatch.uomUnitData = newUOM[2]!=undefined && newUOM[2]!=null ? newUOM[2].uomUnit : selectedBatch.uomUnit ;
           }
       	}
    	}
       };

    $scope.calculateReceivedQty = function (item, batch) {
        if(item.isThreeUOMApplicable == false){
            if(item.uomTypeId == 1){
                item.receivedQtyCon = (item.issueQuantity +"*"+ batch.uom[0].conversion);
                item.receivedQtyCalculation = (item.issueQuantity * batch.uom[0].conversion);
                item.uomUnitData = batch.uom[2].uomUnit;
                batch.receivedQtyCon = (batch.issueQuantity +"*"+ batch.uom[0].conversion);
                batch.receivedQtyCalculation = (batch.issueQuantity * batch.uom[0].conversion);
                batch.uomUnitData = batch.uom[2].uomUnit;
            }
            else if(item.uomTypeId == 2){
            
            }
            else{
                item.receivedQtyCon = (item.issueQuantity);
                item.receivedQtyCalculation = (item.issueQuantity);
                item.uomUnitData = batch.uom[2].uomUnit;
                batch.receivedQtyCon = (batch.issueQuantity);
                batch.receivedQtyCalculation = (batch.issueQuantity);
                batch.uomUnitData = batch.uom[2].uomUnit;
            }
        }
        else{
            if(item.uomTypeId == 1){
                item.receivedQtyCon = (item.issueQuantity +"*"+ batch.uom[0].conversion+"*"+ batch.uom[1].conversion);
                item.receivedQtyCalculation = (item.issueQuantity * batch.uom[0].conversion * batch.uom[1].conversion);
                item.uomUnitData = batch.uom[2].uomUnit;
                batch.receivedQtyCon = (batch.issueQuantity +"*"+ batch.uom[0].conversion+"*"+ batch.uom[1].conversion);
                batch.receivedQtyCalculation = (batch.issueQuantity * batch.uom[0].conversion * batch.uom[1].conversion);
                batch.uomUnitData = batch.uom[2].uomUnit;
            }
            else if(item.uomTypeId == 2){
                item.receivedQtyCon = (item.issueQuantity +"*"+ batch.uom[1].conversion);
                item.receivedQtyCalculation = (item.issueQuantity * batch.uom[1].conversion);
                item.uomUnitData = batch.uom[2].uomUnit;
                batch.receivedQtyCon = (batch.issueQuantity +"*"+ batch.uom[1].conversion);
                batch.receivedQtyCalculation = (batch.issueQuantity * batch.uom[1].conversion);
                batch.uomUnitData = batch.uom[2].uomUnit;
            }
            else{
                item.receivedQtyCon = (item.issueQuantity);
                item.receivedQtyCalculation = (item.issueQuantity);
                item.uomUnitData = batch.uom[2].uomUnit;
                batch.receivedQtyCon = (batch.issueQuantity);
                batch.receivedQtyCalculation = (batch.issueQuantity);
                batch.uomUnitData = batch.uom[2].uomUnit;
            }
        }
    }

    $scope.calculateReceivedQtySub = function (item, batch) {
        if(item.issueQuantity == undefined || item.issueQuantity == null){
            item.issueQuantity = 0;
        }
        if(batch.issueQuantity == undefined || batch.issueQuantity == null){
            batch.issueQuantity = 0;
        }
        if(item.isThreeUOMApplicable == false){
            if(item.uomTypeId == 1){
                item.receivedQtyCon = (item.issueQuantity +"*"+ batch.uom[0].conversion);
                item.receivedQtyCalculation = (item.issueQuantity * batch.uom[0].conversion);
                item.uomUnitData = batch.uom[2].uomUnit;
                batch.receivedQtyCon = (batch.issueQuantity +"*"+ batch.uom[0].conversion);
                batch.receivedQtyCalculation = (batch.issueQuantity * batch.uom[0].conversion);
                batch.uomUnitData = batch.uom[2].uomUnit;
            }
            else if(item.uomTypeId == 2){
            
            }
            else{
                item.receivedQtyCon = (item.issueQuantity);
                item.receivedQtyCalculation = (item.issueQuantity);
                item.uomUnitData = batch.uom[2].uomUnit;
                batch.receivedQtyCon = (batch.issueQuantity);
                batch.receivedQtyCalculation = (batch.issueQuantity);
                batch.uomUnitData = batch.uom[2].uomUnit;
            }
        }
        else{
            if(item.uomTypeId == 1){
                item.receivedQtyCon = (item.issueQuantity +"*"+ batch.uom[0].conversion+"*"+ batch.uom[1].conversion);
                item.receivedQtyCalculation = (item.issueQuantity * batch.uom[0].conversion * batch.uom[1].conversion);
                item.uomUnitData = batch.uom[2].uomUnit;
                batch.receivedQtyCon = (batch.issueQuantity +"*"+ batch.uom[0].conversion+"*"+ batch.uom[1].conversion);
                batch.receivedQtyCalculation = (batch.issueQuantity * batch.uom[0].conversion * batch.uom[1].conversion);
                batch.uomUnitData = batch.uom[2].uomUnit;
            }
            else if(item.uomTypeId == 2){
                item.receivedQtyCon = (item.issueQuantity +"*"+ batch.uom[1].conversion);
                item.receivedQtyCalculation = (item.issueQuantity * batch.uom[1].conversion);
                item.uomUnitData = batch.uom[2].uomUnit;
                batch.receivedQtyCon = (batch.issueQuantity +"*"+ batch.uom[1].conversion);
                batch.receivedQtyCalculation = (batch.issueQuantity * batch.uom[1].conversion);
                batch.uomUnitData = batch.uom[2].uomUnit;
            }
            else{
                item.receivedQtyCon = (item.issueQuantity);
                item.receivedQtyCalculation = (item.issueQuantity);
                item.uomUnitData = batch.uom[2].uomUnit;
                batch.receivedQtyCon = (batch.issueQuantity);
                batch.receivedQtyCalculation = (batch.issueQuantity);
                batch.uomUnitData = batch.uom[2].uomUnit;
            }
        }
    }

        var validateSubSelectedBatch=function(listSub){
      	  var isValid=true;
      	  var count=0;
      	  angular.forEach(listSub, function(val , key){
      		  if(val.selectedBatch==undefined){
                    growl.error("Please select batch.");
                    var popup = angular.element('#substituteModal');
                    popup.modal('show');
//                    $("#substituteModal").show();
      			  isValid=false;
      			  count++;
      		  }else{
      		  	
      			 // var popup = angular.element('#substituteModal');
      	          //popup.modal('hide');
      			  isValid=true;
      		  }
      	  });
      	  if(isValid=true && (count==0)){
      		var popup = angular.element('#substituteModal');
            popup.modal('hide');
      	  }
      	  return isValid; 
        }
        
        var AddSubstituteItem = function (index, bill) {
            bill.splice(index + 1, 0, CopySubstituteItem(index, bill));
        }

        var CopySubstituteItem = function (index, bill) {
            var item = bill[index];
            return {
            	addedItem: item.addedItem,
            	batch: item.batch,
            	genericId: item.genericId,
            	genericName: item.genericName,
            	interventionFlag: item.interventionFlag,
            	isOtc: item.isOtc,
            	isSelected: item.isSelected,
            	itemCode: item.itemCode,
            	itemId: item.itemId,
            	itemName: item.itemName,
            	manufacturerName: item.manufacturerName,
            	markupStatus: item.markupStatus,
            	narcoticFlag: item.narcoticFlag,
            	productCategoryId: item.productCategoryId,
            	rackNo:  item.rackNo,
            	selectedBatch: null,
            	stockAvailable: item.stockAvailable,
            	taxId: item.taxId,
            	selectedUom: item.uoms[0],
            	taxPercentage: item.taxPercentage,
            	uoms: item.uoms,
            	
            }
        }

        var RemoveSubstituteItem = function (index, bill) {
         //   bill.splice(index, 1);
          	angular.forEach(bill,function(value,indx){
        		if(index ==indx){
        			if(value.isSelected == false || value.isSelected == undefined){
            			bill.splice(index, 1);
            		}else{
            			growl.warning("Please de-select the item",
    							{
    								title : 'Success!'
    							});
            		}
        		}
        	});
        }

        
        var Add = function () {
        	//$scope.savedSubItem=false;
            if (!self.model.SelectedItems || self.model.SelectedItems == undefined || self.model.SelectedItems == '') {
                growl.error('Please select items')
                return false;
            } else {
            	 if(validateSubSelectedBatch(self.model.SelectedItems)){
                self.model.SelectedItems.forEach(function (selectedItem) {
                	 selectedItem['isThreeUOMApplicable'] = selectedItem.isThreeUOMApplicable;
                	selectedItem['subBatchList'] = [];
                	selectedItem['itemName'] = selectedItem.itemName;
                    selectedItem['itemCode'] = selectedItem.itemCode;
                     selectedItem['genericName'] = selectedItem.genericName;
                    selectedItem['dose'] = selectedItem.dose;
                    selectedItem['indentQuantity'] = self.model.substituteNewQty;
                    selectedItem['id'] = self.model.selectedParentId;
                    selectedItem['substituteNewQty'] = self.model.substituteNewQty;
                    selectedItem['substituteBtn'] = true;
                    selectedItem['parentItemId'] = self.model.selectedItemId;
                    selectedItem['selectedUom'] = selectedItem.selectedUom;
                    selectedItem['selectedBatch'] = selectedItem.selectedBatch;
                    selectedItem['productCategoryId'] = self.model.productCategoryId;
                    selectedItem['taxAmount'] = self.model.taxAmount;
                    selectedItem['taxId'] = selectedItem.taxId;
                    selectedItem['taxPercentage'] = selectedItem.taxPercentage;
                    selectedItem['issueQuantity'] = selectedItem.issueQuantity;
                    selectedItem['patientClassPercentage'] = 0;
                    selectedItem['savedSubItem'] = false;
                    selectedItem['markUpDown'] = 0;
                    selectedItem['markUpDownPer'] = 0;
                    selectedItem['itemId'] = selectedItem.itemId;
                    selectedItem['genericId'] =selectedItem.genericId;
                    selectedItem['currentStock'] = selectedItem.stockAvailable;
                    selectedItem['availableStockQuantity']= selectedItem.stockAvailable;
                    selectedItem['productCategoryId'] = selectedItem.productCategoryId;
//                    selectedItem['uomUnitId'] = selectedItem.uoms[2].uomUnitId;
//                    selectedItem['uomTypeId'] = selectedItem.uoms[2].uomTypeId;
//                    selectedItem['uomUnit'] = selectedItem.uoms[2].uomUnit;
//                    selectedItem['uomType'] = selectedItem.uoms[2].uomType;
                    selectedItem.batch.forEach(function (batch) {
                         selectedItem['rate'] = batch.sellingPrice;
                        });
                    selectedItem['subBatchList']=[selectedItem.batch[0]];
                    selectedItem.subBatchList[0]['selectedBatch']=selectedItem.selectedBatch;
                    selectedItem.uoms.forEach(function (uoms) {
                       if(uoms.ipUom==true){
                        selectedItem['uomUnitId'] = uoms.uomUnitId;
                        selectedItem['uomTypeId'] = uoms.uomTypeId;
                        selectedItem['uomUnit'] = uoms.uomUnit;
                        selectedItem['uomType'] = uoms.uomType;
                       }
                    });
                    if(isNaN(selectedItem.indentQuantity))
                    	{
                    	selectedItem.indentQuantity=0;
                    	}
                    selectedItem.uomDatalist = selectedItem.uoms.find(function (uom) {
                        return selectedItem.uomTypeId == uom.uomTypeId;
                      });     
                    selectedItem.selectedBatch.uomType = selectedItem.uomDatalist.uomDetails;
                    selectedItem.uomType = selectedItem.uomDatalist.uomDetails;
                    $scope.convertQtyOrdered(selectedItem.selectedBatch,selectedItem);
                    $scope.convertAllBatchStock(selectedItem.selectedBatch,selectedItem);
                });
                $rootScope.startLoader();
                self.model.patientPendingIndentDetailList.items.forEach(function (list) {
              	  
                	  if(!list.itemDisabled){
                    self.model.SelectedItems.forEach(function (item) {
                        item.uom = [{
                            uomDetails: "Select UOM",
                        }];
                        if(item.selectedBatch != undefined){    
                          if (!!item.selectedBatch.batchId) {
                        	  item.availableStockQuantity=item.currentStock;
                              item.stockAvailable=item.selectedBatch.availableStockQuantity;
                              item.substituteNewQty = item.selectedBatch.availableStockQuantity;
                              item.uom = item.selectedBatch.uom;
                              item.selectedUom = $filter('filter')(item.selectedBatch.uom, {
                                  opUom: true
                              }, true)[0];
                              angular.forEach(item.batch, function(batData){
                                if(batData.batchId == item.selectedBatch.batchId){
                                    batData.isSelected = true;
                                }
                              })
                          } else {
                              item.selectedUom = item.uom[0];
                              item.selectedBatch.batchNo = item.selectedBatch.batchDetails;
                          }
                        }
                        if(item.batch) {
                            if(item.batch.length > 0) {
                                self.model.IsEnbaledSave = true;
                            }
                        }
                        
                      //for markup and markdown and concession
//                        var URI = CONSTANTS.GLOBAL.DETAILS_API_IP + '/' + $scope.unitId  + '/' + item.itemId + '/' + $scope.entitlementObj.paymentEntitlementTypeId + '/' + self.model.patientPendingIndentDetailList.admissionId + '/' + self.model.patientPendingIndentDetailList.issuingStoreId;
                        if(self.model.patientPendingIndentDetailList.encounterId!=null && self.model.patientPendingIndentDetailList.encounterId > 0){
                        	var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId + '/'+self.model.patientPendingIndentDetailList.issuingStoreId  + '/' + item.itemId + '/' + $scope.entitlementObj.paymentEntitlementTypeId + '/' + self.model.patientPendingIndentDetailList.encounterId;
                       }
                       else if(self.model.patientPendingIndentDetailList.admissionId != null && self.model.patientPendingIndentDetailList.admissionId > 0){
                    	   var URI = CONSTANTS.GLOBAL.DETAILS_API_IP + '/' + $scope.unitId  + '/' + item.itemId + '/' + $scope.entitlementObj.paymentEntitlementTypeId + '/' + self.model.patientPendingIndentDetailList.admissionId + '/' + self.model.patientPendingIndentDetailList.issuingStoreId ;
                     	  }
                       else{
                    	   var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId + '/'+self.model.patientPendingIndentDetailList.issuingStoreId  + '/' + item.itemId + '/' + $scope.entitlementObj.paymentEntitlementTypeId + '/' + self.model.patientPendingIndentDetailList.encounterId;
                       }    
                        
                        $rootScope.startLoader();
                        GenericService.serviceAction("GET", URI, {})
                        .then(function (response) {
                            $timeout( function(){
                          	  item.markUpDown =response.data.markUpDown;
                          	  item.markUpDownPer =response.data.markUpDownPer;
                          	  item.patientClassPercentage =response.data.patientClassPercentage;
                          	  item.consumedItemId = list.itemId;
                          	  item.consumedPcategroyId =list.productCategoryId;
                          	  $rootScope.stopLoader();
                           },2000);
                        }, function (err) {
                            growl.error('Error while fetching the item details');
                            $rootScope.stopLoader();
                        });
                    });

                    if (self.model.selectedGenricId  == list.genericId) {
                      	if(list.substititeFlag){
                      	if(list.quantityIssued == null || list.quantityIssued == 0){
                          list['disabled'] = true;
                          list.quantityIssued = null;
                      	}
                         
                         
                          // list.selectedUom = list.selectedUom[0];
                          
                          if (!list['selectedItems'])
                              list['selectedItems'] = self.model.SelectedItems;
                          else {
                              list['selectedItems'] = list['selectedItems'].concat(self.model.SelectedItems);
                          }
                          if((list.issueQuantity==undefined || list.issueQuantity==null || list.issueQuantity==0) && list.selectedItems.length>0)
                        	  {
                        	  self.model.activeItem="";
                        	  }
                          list.substititeFlag=false;
                          console.log("selectedSubList", list);
                          $rootScope.stopLoader();
                      }
                      }
                }
                });
  			self.model.patientPendingIndentDetailList.items[self.model.parIndx].isShow = false;
                self.model.SelectedItems = [];
            }
            }
            /*var popup = angular.element('#substituteModal');
            popup.modal('hide');*/
        }
        
        var SubstituteItems = function  (item,index) {
            // self.model.parentIndex=parentIndex;

            self.model.selectedIntemdd=item;
            self.model.selectedIndexxdd=index;
            var popup = angular.element('#substituteModal');
            popup.modal('show');
        	if(item.issueQuantity!=undefined && item.issueQuantity!=null && item.issueQuantity==0 )
            {
        		item['disableParentItem']=true;
            }
        	else
            {
        		item['disableParentItem']=false;
            }
            item.substititeFlag=true;
            item.subFlagOnly=true;
       	    self.model.SelectedItems = [];
            self.model.parIndx = index;
            self.model.formulationTypeId = item.formulationTypeId;
            self.model.selectedParentId = item.id;
            self.model.instructionDesc = item.instructionDesc;
            self.model.duration = item.duration;
            self.model.frequencyDesc = item.frequencyDesc;
            self.model.selectedItemId = item.itemId;
            self.model.selectedGenricId = item.genericId;
            self.model.itemName = item.itemName;
            self.model.dose = item.dose;
            self.model.doseUnit = (item.doseUnit != undefined && item.doseUnit != null) ? item.doseUnit : " ";
            self.model.genericName = item.genericName;
            self.model.productCategoryId = item.productCategoryId;
            $scope.parentIssueDetailsId = item.id!=null ? item.id:item.parentIssueDetailsId;
            $scope.isFromAssignPicker = item.isFromAssignPicker;
            if(item.selectedUom == undefined || item.selectedUom.uomTypeId == undefined){
                self.model.selectedQuantity = item.quantityOrdered;
            }else{
                var mulQty = item.selectedUom.conversion * item.quantityIssued;
                var  newOrderQty =  item.quantityOrdered - mulQty;
                self.model.selectedQuantity = newOrderQty;
            }
            // self.model.selectedQuantity = item.quantityOrdered;
            //             $scope.convertQtyOrdered(item.batchList[0],item);
            var toConvertedQty=item.newIndentQuantity-item.issueQuantity;
            if(item.uoms != undefined && item.uoms != null){
                if(item.uomTypeId==1)
                {
                    toConvertedQty=toConvertedQty*(item.uoms[0].conversion*item.uoms[1].conversion*item.uoms[2].conversion);
                }
                if(item.uomTypeId==2)
                {
                    toConvertedQty=toConvertedQty*(item.uoms[1].conversion*item.uoms[2].conversion);
                }
                if(item.uomTypeId==3)
                {
                    toConvertedQty=toConvertedQty*(item.uoms[2].conversion);
                }
            }
            else{
            	angular.forEach(item.batch, function(d){
            		if(item.uom==undefined || item.uom==null || d.uom.length==0 )
            			{
            			if(d.uom!=undefined && d.uom!=null && d.uom.length!=0 )
            				{
            				item.uom=d.uom;
            				}
            			}
            	});
            	if(item.batch!=undefined && item.batch!=null && item.batch.length>0 )
            		{
                angular.forEach(item.batchList, function(d){
                	d.uom=item.uom;
                	if(d!=undefined && d!=null)
            		{
                	if(d!=undefined && d!=null && d.uom!=undefined && d.uom!=null)
                		{
                    if(item.uomTypeId==1)
                    {
                        toConvertedQty=toConvertedQty*(d.uom[0].conversion*d.uom[1].conversion*d.uom[2].conversion);
                    }
                    if(item.uomTypeId==2)
                    {
                        toConvertedQty=toConvertedQty*(d.uom[1].conversion*d.uom[2].conversion);
                    }
                    if(item.uomTypeId==3)
                    {
                        toConvertedQty=toConvertedQty*(d.uom[2].conversion);
                    }   
                		}
            		}
                })
            }
            }
             
            self.model.substituteNewQty = toConvertedQty;
            self.model.taxAmount = item.taxAmount;
            self.model.taxId = item.taxId;
            self.model.taxPercentage = item.taxPercentage;
            self.model.issuedQuantity = item.issuedQuantity;
            self.model.pendingIssueQuantity = item.pendingIssueQuantity;
            self.model.prescriptionDetailId = item.id!=null ? item.id:item.parentIssueDetailsId;
            self.model.frequency = item.frequency;
            self.model.dose = item.dose+' '+ self.model.doseUnit ;
            self.model.startDate = item.startDate;
            self.model.stopDate = item.stopDate;
            self.model.substituteDetails = [];
            self.model.selectAllRows = false;
            self.model.isItemChecked = false;
            self.model.pickedQuantity =item.pickedQuantity!=undefined ? item.pickedQuantity :0;
            item.isNarcotic = false;
            item.substititeFlag=true;
             
//             var URI = CONSTANTS.PHARMACY.IP.ASSIGN_PICKER_DISPENSER.SUBSTITUTE_API + item.genericId + '/' + item.itemId+ '/' + item.isNarcotic;
            var URI = CONSTANTS.PHARMACY.IP.ASSIGN_PICKER_DISPENSER.SUBSTITUTE_API + $scope.unitId + '/' + item.genericId + '/' + item.itemId+ '/' + item.isNarcotic+ '/' +  self.model.patientPendingIndentDetailList.issuingStoreId+'/'+ self.model.formulationTypeId;
            $rootScope.startLoaderPopup();
            GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
                self.model.substituteDetails = response.data;

                if (!self.model.substituteDetails || self.model.substituteDetails == []) {
                    return;
                } else {
                    self.model.substituteDetails.forEach(function (item) {
                        self.model.selectAllRows = false;
                        item.isSelected = false;
                        /* item.batch.unshift({
                                "batchDetails": "Select Batch"
                                // "batchType": "Select Batch"
                            });
                            item.uom = [{
                                "uomDetails": "Select UOM"
                            }],*/

                            //item.selectedBatch = item.batch[0];
                        item.selectedBatch = commonDetailService.getDefaultBatchFromConfig(item.batch);
                            //item.selectedUom = item.uom[1];
                    });
                }
                $rootScope.stopLoaderPopup();
            });
        }
        
        
        var isSelected = function (item) {
            return item.isSelected;
        }
        
        
        var SelectSubstituteItem = function (item) {
            var itemIds = self.model.SelectedItems;

            if (item.isSelected) {
                itemIds.push(item);
            } else {
                var index = itemIds.indexOf(item);
                if (index > -1) {
                    itemIds.splice(index, 1);
                }
            }

            if (self.model.substituteDetails.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            self.model.isItemChecked = false;

            self.model.substituteDetails.forEach(function (item) {
                if (item.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }
        
      //to select all items on substitute popup
        var SelectAllItems = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedItems = [];
                self.model.substituteDetails.forEach(function (item) {

                    item.isSelected = true;
                    self.model.isItemChecked = true;
                    self.model.SelectedItems.push(item);

                });
            } else {
                self.model.SelectedItems = [];
                self.model.substituteDetails.forEach(function (item) {
                    item.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }
        }
        
        // $scope.sum = 0;
        // $scope.issueQuantityChange = function (systolic,oldValue){
        //     $scope.systolic=systolic;
        //     if(oldValue!=""){
        //         $scope.sum -= parseInt(oldValue);
        //     }
        //     $scope.sum += parseInt(systolic);
        //     console.log("sum", $scope.sum);
        // }

        $scope.calculateAtCop = function(item){
        	if(item.substituteBtn==true)
            {
        		angular.forEach(item.newBatch, function(b){
                    if($scope.atCop == true){
                    	var sellingPrice = !!b ? commonDetailService.fixedDecimal(b.selectedBatch.actualLendingPrice) : 0;
                		if(b.selectedBatch != undefined){
                            b.mrp = commonDetailService.calculateRate(b.selectedBatch.uom, item.uomTypeId, sellingPrice); 
                            var data = {
                                "mrp": b.mrp,
                                "isTaxInc": self.model.taxInclusive,	
                                "markup": item.markUpDown,
                                "markUpDownPer" : item.markUpDownPer,	
                                "discount": !!b.discount || b.discount == 0  ? (!!b.discountPerItem ||b.discountPerItem== 0)  ? b.discountPerItem : b.discount : 0,
                                "isDiscPercent": !!b.discountType && b.discountType == 1 ? true : false,
                                "tax": b.selectedBatch.taxPercentage,
                                "qty": item.issueQuantity,
                                "patientClassPercentage": item.patientClassPercentage
                            }
                            var response = commonDetailService.calculateAmount(data);
                            item.rate = response.rate;
                            b.rate = response.rate;
                            item.taxAmount = response.taxAmount;
                            item.totalAmount = response.totalAmount;
                            item.netAmount = response.netAmount;
                            item.patientClassAmount = response.patientClassAmount;
                            item.unitPatientClassAmount = isNaN(response.unitPatientClassAmount)?0:response.unitPatientClassAmount;
                        }
                    }
                    else{
                    	var sellingPrice = !!b ? commonDetailService.fixedDecimal((b.mrp == undefined || b.mrp == null || b.mrp == '') ? b.rate : b.mrp) : 0;
            			if(b.selectedBatch != undefined){
                            b.mrp = commonDetailService.calculateRate(b.selectedBatch.uom, b.uomTypeId, sellingPrice); 
                            var data = {
                                "mrp": b.mrp,
                                "isTaxInc": self.model.taxInclusive,	
                                "markup": item.markUpDown,
                                "markUpDownPer" : item.markUpDownPer,	
                                "discount": !!b.discount || b.discount == 0  ? (!!b.discountPerItem ||b.discountPerItem== 0)  ? b.discountPerItem : b.discount : 0,
                                "isDiscPercent": !!b.discountType && b.discountType == 1 ? true : false,
                                "tax": b.selectedBatch.taxPercentage,
                                "qty": item.issueQuantity,
                                "patientClassPercentage": item.patientClassPercentage
                            }
                            var response = commonDetailService.calculateAmount(data);
                            item.rate = response.rate;
                            b.rate = response.rate;
                            item.taxAmount = response.taxAmount;
                            item.totalAmount = response.totalAmount;
                            item.netAmount = response.netAmount;
                            item.patientClassAmount = response.patientClassAmount;
                            item.unitPatientClassAmount = isNaN(response.unitPatientClassAmount)?0:response.unitPatientClassAmount;
                        }
                    }
                });
            }
        	else
            {
                angular.forEach(item.batchList, function(b){
                	if(b!=undefined && b!=null)
            		{
                    if($scope.atCop == true){
                	    var sellingPrice = !!b ? commonDetailService.fixedDecimal(b.actualLendingPrice) : 0;
                        if(b.selectedBatch != undefined){
                            b.mrp = commonDetailService.calculateRate(b.selectedBatch.uom, b.uomTypeId, sellingPrice); 
                            var data = {
                                "mrp": b.mrp,
                                "isTaxInc": self.model.taxInclusive,	
                                "markup": item.markUpDown,
                                "markUpDownPer" : item.markUpDownPer,	
                                "discount": !!b.discount || b.discount == 0  ? (!!b.discountPerItem ||b.discountPerItem== 0)  ? b.discountPerItem : b.discount : 0,
                                "isDiscPercent": !!b.discountType && b.discountType == 1 ? true : false,
                                "tax": b.selectedBatch.taxPercentage,
                                "qty": item.issueQuantity,
                                "patientClassPercentage": item.patientClassPercentage
                            }
                            var response = commonDetailService.calculateAmount(data);
                            item.rate = response.rate;
                            b.rate = response.rate;
                            item.taxAmount = response.taxAmount;
                            item.totalAmount = response.totalAmount;
                            item.netAmount = response.netAmount;
                            item.patientClassAmount = response.patientClassAmount;
                            item.unitPatientClassAmount = isNaN(response.unitPatientClassAmount)?0:response.unitPatientClassAmount;
                        }
                    }
                    else{
                	    var sellingPrice = !!b ? commonDetailService.fixedDecimal(b.mrp) : 0;
        			    if(b.selectedBatch != undefined){
                            b.mrp = commonDetailService.calculateRate(b.selectedBatch.uom, item.uomTypeId, sellingPrice); 
                            var data = {
                                "mrp": b.mrp,
                                "isTaxInc": self.model.taxInclusive,	
                                "markup": item.markUpDown,
                                "markUpDownPer" : item.markUpDownPer,	
                                "discount": !!b.discount || b.discount == 0  ? (!!b.discountPerItem ||b.discountPerItem== 0)  ? b.discountPerItem : b.discount : 0,
                                "isDiscPercent": !!b.discountType && b.discountType == 1 ? true : false,
                                "tax": b.selectedBatch.taxPercentage,
                                "qty": item.issueQuantity,
                                "patientClassPercentage": item.patientClassPercentage
                            }
                            var response = commonDetailService.calculateAmount(data);
                            item.rate = response.rate;
                            b.rate = response.rate;
                            item.taxAmount = response.taxAmount;
                            item.totalAmount = response.totalAmount;
                            item.netAmount = response.netAmount;
                            item.patientClassAmount = response.patientClassAmount;
                            item.unitPatientClassAmount = isNaN(response.unitPatientClassAmount)?0:response.unitPatientClassAmount;
                        }
                    }
            		}
                });
            }
        }

        var onChange = function (selectedBatch, batch,item){
        		var totalIssuedQty=0;
        		angular.forEach(item.selectedItems, function(d){
        			totalIssuedQty=totalIssuedQty+d.issueQuantity
        		})
        	
        		var sellingPrice;
        		if($scope.atCop==true)
        			{
        			sellingPrice = !!batch ? commonDetailService.fixedDecimal(batch.actualLendingPrice) : 0;
        			}
        		else
        			{
        			sellingPrice = !!batch ? commonDetailService.fixedDecimal(batch.sellingPrice) : 0;
        			}
            	item.mrp = commonDetailService.calculateRate(selectedBatch.uom, item.uomTypeId, sellingPrice); 
        	if(item!=undefined){
             var data = {
               "mrp": item.mrp!=undefined ?  item.mrp : batch.sellingPrice,
               "isTaxInc": self.model.taxInclusive,
          
             "markup": item.markUpDown,
                "markUpDownPer" : item.markUpDownPer,	
               "discount": !!batch.discount || batch.discount == 0  ? (!!batch.discountPerItem ||batch.discountPerItem== 0)  ? batch.discountPerItem : batch.discount : 0,
               "isDiscPercent": !!batch.discountType && batch.discountType == 1 ? true : false,
               "tax": batch.taxPercentage,
               "qty": batch.issueQuantity,
               "patientClassPercentage": item.patientClassPercentage,
           
            }
      
            item.rate=batch.rate!=undefined ?  batch.rate : batch.sellingPrice;
            var response = commonDetailService.calculateAmount(data);
            item.rate = response.rate;
            batch.rate = response.rate;
            item.taxAmount = response.taxAmount;
            item.totalAmount = response.totalAmount;
            item.netAmount = response.netAmount;
            item.patientClassAmount = response.patientClassAmount;
            item.unitPatientClassAmount = isNaN(response.unitPatientClassAmount)?0:response.unitPatientClassAmount;
            if(self.model.patientPendingIndentDetailList.status == 'Pending'){
                angular.forEach(self.model.patientPendingIndentDetailList.items, function(d){
                	if(d.issueQuantity!=undefined && d.issueQuantity!=null){

                    	
                        if(d.itemId == item.itemId){
                        
                                var issueQty1=0;
                              
                            	//Added on 17-08-2021 for calulate issue quantity
                            	angular.forEach(d.batchList, function(bdata){
                                    if(bdata.selectedBatch!=undefined){
                                        
                                        issueQty1 += parseInt(bdata.selectedBatch.issueQuantity); 
                                    }
                                });
                                d.issueQuantity = d.previousIssuedQuantity+issueQty1
                               
                        }
                        if(d.selectedItems!=undefined){
                        	 angular.forEach(d.selectedItems, function(subdata){
                        	if(subdata.itemId == item.itemId){
                               
                                   	
                                       var issueQty1=0;
                                    
                                   	//Added on 17-08-2021 for calulate issue quantity
                                   	angular.forEach(subdata.subBatchList, function(bdata){
                                           if(bdata.selectedBatch!=undefined){
                                               
                                               issueQty1 += parseInt(bdata.selectedBatch.issueQuantity); 
                                           }
                                       });
                                       subdata.issueQuantity = issueQty1
                                       if(issueQty1 > subdata.indentQuantity)
                                       {
                                            growl.error("Please enter valid quantity");    
                                       }
                                  // });
                               }
                        });
                        }
                    
                	}else{

                   	 angular.forEach(d.selectedItems, function(subdata){
                        	if(subdata.itemId == item.itemId){
                               
                                       var issueQty1=0;
                                      
                                   	//Added on 17-08-2021 for calulate issue quantity
                                   	angular.forEach(subdata.subBatchList, function(bdata){
                                           if(bdata.selectedBatch!=undefined){
                                               
                                               issueQty1 += parseInt(bdata.selectedBatch.issueQuantity); 
                                           }
                                       });
                                       subdata.issueQuantity = issueQty1
                                       if(issueQty1 > subdata.indentQuantity)
                                       {
                                            growl.error("Please enter valid quantity");    
                                       }
                                  // });
                               }
                        });
                   
                		
                	}
                }); 
            } else{
            	angular.forEach(self.model.patientPendingIndentDetailList.items,function(d){
//            		if(d.issueQuantity!=undefined && d.issueQuantity!= null){

                    	
            		if(d.itemId == item.itemId){
                        var issueQty=0;
                        if(d.batchList!=undefined){
                        if(d.batchList){
                          
                        	//Added on 19-08-2021 for calulate issue quantity
                           	angular.forEach(d.batchList, function(bdata){
                                   if(bdata.selectedBatch!=undefined){
                                       
                                	   issueQty += parseInt(bdata.selectedBatch.issueQuantity); 
                                   }
                               });
                        }
                      
                        angular.forEach(d.batch, function(bdata){
                        if(( bdata.isSelected == true || bdata.isSelectedP == true || bdata.test == true) && (item.itemId == d.itemId)  
                        		&& (bdata.issueQuantity1 != undefined && bdata.issueQuantity1 != null && bdata.issueQuantity1 != 0) && bdata.issueQuantity==0){
                     
                            if(bdata.issueQuantity1 == undefined){
                                bdata.issueQuantity1 = bdata.issueQuantity;
                            }
                            issueQty =  parseInt(selectedBatch.issueQuantity!=undefined ? selectedBatch.issueQuantity : bdata.issueQuantity1);


                        }
                        });
                        d.issueQuantity = d.previousIssuedQuantity+issueQty
                        if(issueQty > d.indentQuantity)
                        {
                             growl.error("Please enter valid quantity"); 
                             angular.forEach(d.batchList, function(bdata){
                                 if(bdata.selectedBatch!=undefined){
                                   bdata.selectedBatch.issueQuantity=0;
                              	   issueQty = 0; 
                                 }
                             });
                             d.issueQuantity = d.previousIssuedQuantity+issueQty
                        }
            		}
                    }
                		if(d.selectedItems!=undefined && d.selectedItems!=null){

                			angular.forEach(d.selectedItems, function(subItems){

                                if(subItems.issueQuantity==undefined || subItems.issueQuantity==null || subItems.issueQuantity==0){
	
                        		if(subItems.itemId==item.itemId){

                        		var issueQty = 0;
                        		if(subItems.batch)
                        		{
                        			
                        			//Added on 19-08-2021 for calulate issue quantity
                                   	angular.forEach(subItems.subBatchList, function(bdata){
                                           if(bdata.selectedBatch!=undefined){
                                               
                                        	   issueQty += parseInt(bdata.selectedBatch.issueQuantity); 
                                           }
                                       });
                        			
                        		}
                        		
                        	subItems.issueQuantity = issueQty
                        	if(issueQty > subItems.indentQuantity)
                        	{
                        		growl.error("Please enter valid quantity");
                        	}
                        	}
                			}else{

                				
                        		if(subItems.itemId==item.itemId){

                        		var issueQty = 0;
                        		if(subItems.batch)
                        		{
                        			
                        			//Added on 19-08-2021 for calulate issue quantity
                                   	angular.forEach(subItems.subBatchList, function(bdata){
                                           if(bdata.selectedBatch!=undefined){
                                               
                                        	   issueQty += parseInt(bdata.selectedBatch.issueQuantity); 
                                           }
                                       });
                        		}
                        	subItems.issueQuantity = issueQty
                        	if(issueQty > subItems.indentQuantity)
                        	{
                        		growl.error("Please enter valid quantity");
                        	}
                        	}
                			
                			}
                        });
                		
                		}
                	
            	});
            }
            
        }
        }

        $scope.validate = function (selectedBatch , itemId, index, batchurrentIndex) {
            var isValid1 = true;
            angular.forEach(self.model.patientPendingIndentDetailList.items, function(data){
               if(data.itemId==itemId)
                {
                    $scope.isValid= true;
                    if(self.model.patientPendingIndentDetailList.status == 'Pending'){
                        angular.forEach(data.batch, function(bdata, idx){
                            if(bdata.isSelected == true || bdata.isSelectedP == true){
                                if(data.batchList.length > 1){
                                    if(selectedBatch.batchNo == bdata.batchNo && batchurrentIndex!=idx){
                                        growl.error("Please select different batch");
                                        $scope.saveBtnDisabled = false;
                                        $scope.isValid = false;
                                        AddDuplicateRow(0,data);
                                        data.batchList.splice(index, 1);
                                    }
                                }
                            }
                        });
                    }
                    else{
                    	if(selectedBatch!=undefined || selectedBatch!=null){
                        if((data.batchList!=undefined && data.batchList.length > 1)){
                            if(data.batch!=undefined && data.batch != null){
                            	angular.forEach(data.batch, function(d, idx){
                                    if(d.test == true && (d.isSelected == true || d.isSelectedP==true)){
                                        if(d.batchNo == selectedBatch.batchNo && batchurrentIndex!=idx){
                                            growl.error("Please select different batch");
                                            $scope.saveBtnDisabled = false;
                                            $scope.isValid = false;
                                            AddDuplicateRow(0,data);
                                            data.batch.splice(index, 1);
                                        }
                                    }
                                });
                            }
                        }
                        if(data.batch!=undefined  && data.batch.length > 1 && (data.selectedItems!=null && data.selectedItems.length > 0)){
                            	angular.forEach(data.batch, function(d, idx){
                                    if(d.test == true && d.isSelected == true){
                                        if(d.batchNo == selectedBatch.batchNo && batchurrentIndex!=idx){
                                            growl.error("Please select different batch");
                                            $scope.saveBtnDisabled = false;
                                            $scope.isValid = false;
                                            AddDuplicateRow(0,data);
                                            data.batchList.splice(index, 1);
                                        }
                                    }
                                });
                            }
//                        }
                        if(selectedBatch.availableStockQuantity == 0 || selectedBatch.availableStockQuantity == undefined){
                            growl.error("Stock is not available. Please select different batch");
                            $scope.isValid = false;
                            $scope.saveBtnDisabled = false;
                            AddDuplicateRow(0,data);
                            data.batchList.splice(index, 1);
                        }
                    }
                }
                }
               
               else{
                   angular.forEach(data.selectedItems, function(data1){
                    if(data1.itemId==itemId)
		           {
                    	
		               $scope.isValid= true;
		               data1.newBatch=[];
		               AddDuplicateRowSubstitute(0,data1);
		//               data1.newBatch=data1.batch[1];
		               /*if(data1.status == 'Pending'){
		                   angular.forEach(data1.batch, function(bdata){
		                       if(bdata.isSelected == true || bdata.isSelectedP == true){
		                           if(data1.batch.length > 1){
		                               if(selectedBatch.batchNo == bdata.batchNo){
		                                   growl.error("Please select different batch");
		                                   $scope.isValid = false;
		                                   AddDuplicateRow(0,data1);
		                                   data1.batch.splice(index, 1);
		                               }
		                           }
		                       }
		                   });
		               }*/
		              /* else{
		                   if(data1.batch.length > 1){
		                       angular.forEach(data1.batch, function(d){
		                           if(d.test == true && d.isSelected == true){
		                               if(d.batchNo == selectedBatch.batchNo){
		                                   growl.error("Please select different batch");
		                                   $scope.isValid = false;
		                                   AddDuplicateRow(0,data1);
		                                   data1.batch.splice(index, 1);
		                               }
		                           }
		                       });
		                   }
		                   if(selectedBatch.availableStockQuantity == 0 || selectedBatch.availableStockQuantity == undefined){
		                       growl.error("Stock is not available. Please select different batch");
		                       $scope.isValid = false;
		                       AddDuplicateRow(0,data);
		                       data1.batch.splice(index, 1);
		                   }
		               }*/
		               
		           }
                   });
           }
            });
            return isValid1 = $scope.isValid;
        }

        $scope.batchExpiryMsgShow = function(item){
            var date = moment(new Date()).format("MM");
            var year = moment(new Date()).format("YYYY");
            var dateCal = parseInt(date) + 1;
            var expDate = moment(item.selectedBatch.expiry).format("MM");
            var expYear = moment(item.selectedBatch.expiry).format("YYYY");
            if((dateCal == expDate || date == expDate) && year == expYear){
                growl.warning("Batch No. " + item.selectedBatch.batchNo + " is near expiry");
            }  
        }
        
        var batchOnChange = function (selectedBatch, batch, itemId, index , item) {
            item.taxId=selectedBatch.taxId;
            item.taxPercentage=selectedBatch.taxPercentage;
        	$scope.convertQtyOrdered(selectedBatch,item);
            if(selectedBatch!=undefined || selectedBatch!=null){
                selectedBatch.issueQuantity=0;	
                if(batch.issueQuantity == 0 || batch.issueQuantity == undefined){
                    batch.isSelected=false;
                }
                if(batch.test == false || batch.test == undefined){
                    batch.issueQuantity = 0;
                    angular.forEach(item.batch, function(val1,idx1){
                	    val1.isSelected=false;
                    });
                    angular.forEach(item.batchList , function(value, index){
                    	if(value!=undefined && value!=null)
                		{
                        angular.forEach(item.batch, function(val, idx){
                            if(value.selectedBatch.batchId==val.batchId){
                                val.isSelected=true;
                            }
                        });
                		}
                    });
                    if(item.subBatchList != undefined && item.subBatchList.length > 0 ){
                	    angular.forEach(item.subBatchList , function(value, index){ 
                            angular.forEach(item.batch, function(val, idx){
                                if(value.selectedBatch.batchId==val.batchId){
                                    val.isSelected=true;
                                }
                            });
                        });
                    }
                }
                if(!batch.batchId && item.batch.length == 1){
                    selectedBatch.isSelected = false;
                    selectedBatch.issueQuantity = 0;
                }
           
                if(item.substituteBtn==undefined){
           	        if(item.batchList.length == 1){
                        angular.forEach(item.batch, function(b){
                            if(b.batchNo != selectedBatch.batchNo){
                                b.issueQuantity = 0;
                                item.issueQuantity = item.issueQuantity;
                                b.isSelected = false;
                            }
                        })
                    }
                }
                else{
           	        if(item.batch.length == 1){
                        angular.forEach(item.batch, function(b){
                            if(b.batchNo != selectedBatch.batchNo){
                                b.issueQuantity = 0;
                                item.issueQuantity = item.issueQuantity;
                                b.isSelected = false;
                            }
                        })
                    }
                }
           
                if(selectedBatch == null){
                    angular.forEach(item.batch, function(value, key){
                        if(key == index){
                            value.isSelected = false;
                            value.issueQuantity = 0;
                        }
                    });
                }
                batch.isSelectedP = false;   
                if(item.newBatch==undefined){
            	    var batchurrentIndex = item.batch.indexOf(selectedBatch)
            	    if($scope.validate(selectedBatch , itemId, index, batchurrentIndex)){
                        var conversionPurchase;
                        var conversionStock;
                        var conversionDespense;

                        var ipUom = selectedBatch.uom.find(function (obj) {
                            return obj.ipUom == true;
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

                        if(ipUom)
                        {
                            if (ipUom.uomTypeId == 1) {
                                batch.selectedBatch.displayQuantity = Math.round((((selectedBatch.availableStockQuantity / conversionPurchase) / conversionStock) / conversionDespense));
                                batch.selectedBatch.newStock = batch.selectedBatch.displayQuantity;
                   		    }
                            else if (ipUom.uomTypeId == 2) {
                                batch.selectedBatch.displayQuantity = Math.round((selectedBatch.availableStockQuantity / conversionStock) / conversionDespense);
                                batch.selectedBatch.newStock = batch.selectedBatch.displayQuantity;
                            }
                            else {
                                batch.selectedBatch.displayQuantity = Math.round(selectedBatch.availableStockQuantity / conversionDespense);
                                batch.selectedBatch.newStock = batch.selectedBatch.displayQuantity;
                            } 
                       
                            batch.selectedBatch.uomType = item.uomType;
                            batch.selectedBatch.uomTypeId = item.uomTypeId;
                            batch.selectedBatch.uomUnit = item.uomUnit;
                            batch.selectedBatch.uomUnitId = item.uomUnitId; 
                        }
                        else
                        {
                            batch.selectedBatch.displayQuantity = Math.round(selectedBatch.availableStockQuantity);
                            batch.selectedBatch.newStock = batch.selectedBatch.displayQuantity;
                        }
                        if(self.model.patientPendingIndentDetailList.status == 'Pending'){
                            selectedBatch.isSelected = true; 
                        }
                        else{
                            selectedBatch.isSelectedP = true;
                            selectedBatch.isSelected=true;
                        }
                        if(self.model.patientPendingIndentDetailList.status == 'Pending'){
                            angular.forEach(self.model.patientPendingIndentDetailList.items, function(d){
                                if(d.itemId == item.itemId){
                                    var issueQty1=0;
                                     
                                 	//Added on 17-08-2021 for calulate issue quantity
                                 	angular.forEach(d.batchList, function(bdata){
                                        if(bdata.selectedBatch!=undefined){                
                                            issueQty1 += parseInt(bdata.selectedBatch.issueQuantity); 
                                        }
                                    });
                                    d.issueQuantity = d.previousIssuedQuantity+issueQty1
                                    if(issueQty1 > d.indentQuantity)
                                    {
                                        growl.error("Please enter valid quantity");    
                                    }
                                }
                                else{
                             	    angular.forEach(d.selectedItems, function(d1){    
                                        var issueQty1=0;
                                    
                                     	//Added on 17-08-2021 for calulate issue quantity
                                     	angular.forEach(d1.batchList, function(bdata){
                                            if(bdata.selectedBatch!=undefined){             
                                                issueQty1 += parseInt(bdata.selectedBatch.issueQuantity); 
                                            }
                                        });
                                        d1.issueQuantity = d1.previousIssuedQuantity+issueQty1
                                        if(issueQty1 > d1.indentQuantity)
                                        {
                                            growl.error("Please enter valid quantity");    
                                        }
                                    });
                                }
                            }); 
                        } 
                        else
                        {
                            angular.forEach(self.model.patientPendingIndentDetailList.items, function(d){
                         	    if(d.parentIssueDetailId==null){
                                    if(d.itemId == item.itemId){
                                        var issueQty=0;
                                        if(d.batchList){
                                            angular.forEach(d.batchList, function(b){
                                               issueQty = issueQty + parseInt(b.issueQuantity == undefined ? b.selectedBatch.issueQuantity : b.issueQuantity);
                                            });
                                        }
                                        angular.forEach(d.batch, function(bdata){
                                            if(( bdata.isSelected == true || bdata.isSelectedP == true || bdata.test == true) && (item.itemId == d.itemId)){
                                                if(bdata.issueQuantity == undefined){
                                                    bdata.issueQuantity = 0;
                                                }
                                                issueQty = issueQty + parseInt(bdata.issueQuantity==undefined ? bdata.selectedBatch.issueQuantity : bdata.issueQuantity);
                                            }
                                        });
                                        d.issueQuantity = d.previousIssuedQuantity+issueQty
                                        if(issueQty > d.indentQuantity)
                                        {
                                            growl.error("Please enter valid quantity");    
                                        }
                                    }else{
                                	    angular.forEach(d.selectedItems, function(d1){ 
                                	        if(d1.flag==undefined){   
                                                var issueQty1=0;
                                    	       //Added on 17-08-2021 for calulate issue quantity
                                    	        angular.forEach(d.batchList, function(bdata){
                                                    if(bdata.selectedBatch!=undefined){
                                                        issueQty1 += parseInt(bdata.selectedBatch.issueQuantity); 
                                                    }
                                                });
                                       
                                                d1.issueQuantity = d1.previousIssuedQuantity+issueQty1
                                                if(issueQty1 > d1.indentQuantity)
                                                {
                                                    growl.error("Please enter valid quantity");    
                                                }
                                	        }
                                        });  
                                    }
                                }
                            });
                        }
                    }
                }
                else
                {
           	        if(true){
                        var conversionPurchase;
                        var conversionStock;
                        var conversionDespense;

                        var ipUom = selectedBatch.uom.find(function (obj) {
                            return obj.ipUom == true;
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

                        if(ipUom)
                        {
                            if (ipUom.uomTypeId == 1) {
                                batch.selectedBatch.displayQuantity = Math.round((((selectedBatch.availableStockQuantity / conversionPurchase) / conversionStock) / conversionDespense));
                            }
                            else if (ipUom.uomTypeId == 2) {
                                batch.selectedBatch.displayQuantity = Math.round((selectedBatch.availableStockQuantity / conversionStock) / conversionDespense);
                            }
                            else {
                                batch.selectedBatch.displayQuantity = Math.round(selectedBatch.availableStockQuantity / conversionDespense);
                            }
                            batch.selectedBatch.uomType = item.uomType;
                            batch.selectedBatch.uomTypeId = item.uomTypeId;
                            batch.selectedBatch.uomUnit = item.uomUnit;
                            batch.selectedBatch.uomUnitId = item.uomUnitId;
                        }
                        else
                        {
                            batch.selectedBatch.displayQuantity = Math.round(selectedBatch.availableStockQuantity);
                        }
                        if(self.model.patientPendingIndentDetailList.status == 'Pending'){
                            selectedBatch.isSelected = true; 
                        }
                        else{
                            selectedBatch.isSelectedP = true;
                        }
                        if(self.model.patientPendingIndentDetailList.status == 'Pending'){
                            angular.forEach(self.model.patientPendingIndentDetailList.items, function(d){
                                if(d.itemId == item.itemId){        
                                    var issueQty1=0;
                                    
                                	//Added on 17-08-2021 for calulate issue quantity
                                	angular.forEach(d.batchList, function(bdata){
                                        if(bdata.selectedBatch!=undefined){
                                            
                                            issueQty1 += parseInt(bdata.selectedBatch.issueQuantity); 
                                        }
                                    });
                                    d.issueQuantity = d.previousIssuedQuantity+issueQty1
                                    if(issueQty1 > d.indentQuantity)
                                    {
                                        growl.error("Please enter valid quantity");    
                                    }
                                }   
                                else{
                            	    angular.forEach(d.selectedItems, function(d1){    
                                        var issueQty1=0;
                                    
                                    	//Added on 17-08-2021 for calulate issue quantity
                                    	angular.forEach(d1.subBatchList, function(bdata){
                                            if(bdata.selectedBatch!=undefined){
                                                
                                                issueQty1 += parseInt(bdata.selectedBatch.issueQuantity); 
                                            }
                                        });
                                        d1.issueQuantity = d1.previousIssuedQuantity+issueQty1
                                        if(issueQty1 > d1.indentQuantity)
                                        {
                                            growl.error("Please enter valid quantity");    
                                        }
                                    });
                                }
                            }); 
                        } 
                        else
                        {
                            angular.forEach(self.model.patientPendingIndentDetailList.items, function(d){
                                if(d.itemId == item.itemId){
                                    var issueQty=0;
                                    if(d.batchList){
                                        angular.forEach(d.batchList, function(b){
                                            issueQty = issueQty + parseInt(b.issueQuantity == undefined ? b.selectedBatch.issueQuantity : b.issueQuantity);
                                        });
                                    }
                                    angular.forEach(d.batch, function(bdata){
                                        if(( bdata.isSelected == true || bdata.isSelectedP == true || bdata.test == true) && (item.itemId == d.itemId)){
                                            if(bdata.issueQuantity == undefined){
                                                bdata.issueQuantity = 0;
                                            }
                                            issueQty = issueQty + parseInt(bdata.issueQuantity==undefined ? bdata.selectedBatch.issueQuantity : bdata.issueQuantity);
                                        }
                                    });
                                    d.issueQuantity =d.previousIssuedQuantity+issueQty
                                    if(issueQty > d.indentQuantity)
                                    {
                                        growl.error("Please enter valid quantity");    
                                    }
                                }
                                else{
                            	    angular.forEach(d.selectedItems, function(d1){    
                                        var issueQty1=0;
                                    
                                	    //Added on 17-08-2021 for calulate issue quantity
                                	    angular.forEach(d.batchList, function(bdata){
                                            if(bdata.selectedBatch!=undefined){
                                                issueQty1 += parseInt(bdata.selectedBatch.issueQuantity); 
                                            }
                                        });
                                        d1.issueQuantity = d1.previousIssuedQuantity+issueQty1
                                        if(issueQty1 > d1.indentQuantity)
                                        {
                                            growl.error("Please enter valid quantity");    
                                        }
                                    });
                                }
                            });
                        }
                    }
                }
            }
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
 	                //AddBatchToDetail(self.model.materialPickerDetail, itemId, batchId);
 	            }

 	            event.stopPropagation();
 	            event.preventDefault();

 	            // SetFocus();
 	        }
 	    }
 	   var AddDuplicateRow = function (index, item) {
           var batch = angular.copy(item.batchList[0]);
           batch.expiry = '';
           batch.uomType = '';
           batch.uomTypeId = '';
           batch.uomUnit = '';
           batch.uomUnitId = '';
           batch.displayQuantity = '';
           batch.batchId = '';
           batch.batchNo = '';
           batch.batchName = 
           batch.batchDetails = '';
           batch.issueQuantity = '';
           if(batch.selectedBatch){    
               batch.selectedBatch.availableStockQuantity = '';
               batch.selectedBatch.expiry = '';
               batch.selectedBatch.uomType = '';
               batch.selectedBatch.uomUnit = '';
               batch.selectedBatch.issueQuantity = 0;
           }
           batch.issueQuantity = 0;
           
           var data = {
                   "mrp": batch.rate,
                   "isTaxInc": self.model.taxInclusive,
                //    "markup": !!item.markUpDown ? item.markUpDownPer : null,
                //    "markdown": !item.markUpDown ? item.markUpDownPer : null,
                 "markup": item.markUpDown,
                "markUpDownPer" : item.markUpDownPer,		
                   "discount": !!batch.discount || batch.discount == 0  ? (!!batch.discountPerItem ||batch.discountPerItem== 0)  ? batch.discountPerItem : batch.discount : 0,
                   "isDiscPercent": !!batch.discountType && batch.discountType == 1 ? true : false,
                   "tax": !!batch.taxAmount?batch.taxAmount:0,
                   "qty": batch.issueQuantity,
                   "patientClassPercentage": item.patientClassPercentage,
                  // "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId

                }
                if($scope.atCop == true){
                    data.mrp = batch.actualLendingPrice;
                }
                else{
                    data.mrp = batch.mrp;
                }
                var response = commonDetailService.calculateAmount(data);
                batch.rate = response.rate;
                batch.taxAmount = response.taxAmount;
                batch.totalAmount = response.totalAmount;
                batch.netAmount = response.netAmount;
                batch.patientClassAmount = response.patientClassAmount;
                batch.unitPatientClassAmount = isNaN(response.unitPatientClassAmount)?0:response.unitPatientClassAmount;
           item.batchList.push(batch);
       }
 	   
        var AddDuplicateRowSubstitute = function (index, item) {
            var batch = angular.copy(item.batch[0]);
            batch.expiry = '';
            batch.uomType = '';
            batch.uomTypeId = '';
            batch.uomUnit = '';
            batch.uomUnitId = '';
            batch.displayQuantity = '';
            batch.batchId = '';
            batch.batchNo = '';
            batch.batchName = 
            batch.batchDetails = '';
            batch.issueQuantity = '';
            if(batch.selectedBatch){    
                batch.selectedBatch.availableStockQuantity = '';
                batch.selectedBatch.expiry = '';
                batch.selectedBatch.uomType = '';
                batch.selectedBatch.uomUnit = '';
            }
            batch.issueQuantity = 0;
            
            var data = {
                    "mrp":  batch.rate!=undefined ?  batch.rate : batch.sellingPrice,
                    "isTaxInc": self.model.taxInclusive,
                    // "markup": !!item.markUpDown ? item.markUpDownPer : null,
                    // "markdown": !item.markUpDown ? item.markUpDownPer : null,	
                     "markup": item.markUpDown,
                "markUpDownPer" : item.markUpDownPer,	
                    "discount": !!batch.discount || batch.discount == 0  ? (!!batch.discountPerItem ||batch.discountPerItem== 0)  ? batch.discountPerItem : batch.discount : 0,
                    "isDiscPercent": !!batch.discountType && batch.discountType == 1 ? true : false,
                    "tax": !!batch.taxAmount?batch.taxAmount:0,
                    "qty": batch.issueQuantity,
                    "patientClassPercentage": item.patientClassPercentage,
                   // "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId

                 }
                if($scope.atCop == true){
                    data.mrp = batch.actualLendingPrice;
                }
                else{
                    data.mrp = batch.rate;
                }
                 var response = commonDetailService.calculateAmount(data);
                 batch.rate = response.rate;
                 batch.taxAmount = response.taxAmount;
                 batch.totalAmount = response.totalAmount;
                 batch.netAmount = response.netAmount;
                 batch.patientClassAmount = response.patientClassAmount;
                 batch.unitPatientClassAmount = isNaN(response.unitPatientClassAmount)?0:response.unitPatientClassAmount;
            item.newBatch.push(batch);
        }

        var AddDuplicateRowSubstituteForPlus = function (index, item) {
            var batch = angular.copy(item.subBatchList[0]);
            batch.expiry = '';
            batch.uomType = '';
            batch.uomTypeId = '';
            batch.uomUnit = '';
            batch.uomUnitId = '';
            batch.displayQuantity = '';
            batch.batchId = '';
            batch.batchNo = '';
            batch.batchName = 
            batch.batchDetails = '';
            batch.issueQuantity = '';
            if(batch.selectedBatch){    
                batch.selectedBatch.availableStockQuantity = '';
                batch.selectedBatch.expiry = '';
                batch.selectedBatch.uomType = '';
                batch.selectedBatch.uomUnit = '';
            }
            batch.issueQuantity = 0;
            
            var data = {
                    "mrp":  batch.rate!=undefined ?  batch.rate : batch.sellingPrice,
                    "isTaxInc": self.model.taxInclusive,
                     "markup": item.markUpDown,
                     "markUpDownPer" : item.markUpDownPer,
                    "discount": !!batch.discount || batch.discount == 0  ? (!!batch.discountPerItem ||batch.discountPerItem== 0)  ? batch.discountPerItem : batch.discount : 0,
                    "isDiscPercent": !!batch.discountType && batch.discountType == 1 ? true : false,
                    "tax": !!batch.taxAmount?batch.taxAmount:0,
                    "qty": batch.issueQuantity,
                    "patientClassPercentage": item.patientClassPercentage,
                 }
                if($scope.atCop == true){
                    data.mrp = batch.actualLendingPrice;
                }
                else{
                    data.mrp = batch.rate;
                }
                 var response = commonDetailService.calculateAmount(data);
                 batch.rate = response.rate;
                 batch.taxAmount = response.taxAmount;
                 batch.totalAmount = response.totalAmount;
                 batch.netAmount = response.netAmount;
                 batch.patientClassAmount = response.patientClassAmount;
                 batch.unitPatientClassAmount = isNaN(response.unitPatientClassAmount)?0:response.unitPatientClassAmount;
            item.subBatchList.push(batch);
        }
        
        var DuplicateRowItems = function (batches) {
            batches.batch = '',
                batches.batch.expiry = '';
            batches.batch.uomType = '';
            batches.batch.uomUnit = '';
            batches.batch.displayQuantity = '';
            batches.batch.issueQuantity = '';
        }

        var RemoveRowData = function (item, batch , issueQty) {
        	item.subFlag = false;
            item.addFlag = false;
            if(batch.selectedBatch){
                batch.selectedBatch.isSelected = false;
                batch.selectedBatch.isSelectedP = false;
                batch.selectedBatch.issueQuantity=undefined;
            }
            var index = item.batchList.indexOf(batch);
            item.issueQuantity=item.issueQuantity- issueQty;
            item.batchList.splice(index, 1);
        }
        
        var RemoveRowDataSubstitute = function (item, batch , issueQty) {
            if(batch.selectedBatch){
                batch.selectedBatch.isSelected = false;
                batch.selectedBatch.isSelectedP = false;
                batch.selectedBatch.issueQuantity=undefined;
            }
            var index = item.subBatchList.indexOf(batch);
            item.issueQuantity=item.issueQuantity- issueQty;
            item.subBatchList.splice(index, 1);
        }
        
        $scope.removeItem = function(item,index){
        	item.disableParentItem=false;
	        	item.subFlag = false;
	            item.addFlag = false;
        		self.model.patientPendingIndentDetailList.items.splice(index, 1)
            	$scope.statusId = 18;
        }
        
        $scope.removeSubstituteItem=function(index,parentItemId){
        	angular.forEach(self.model.patientPendingIndentDetailList.items, function(value , index){
        		if(parentItemId==value.itemId)
    			{
    			if((value.selectedItems!=undefined && value.selectedItems!= null)){
    				angular.forEach(value.selectedItems, function(val1, ind1){
    					value.disableParentItem=false;
    					value.subFlagOnly=false;
    					if(val1.savedSubItem!=undefined){
    						if(val1.savedSubItem==false){
    						value.selectedItems.splice(ind1, 1);
    					}
    					}
    				});
    			}
        	}
    		});
        }
        
        var SetStatus = function (x) {
            var status = self.model.Status.find(function (obj) {
                return obj.status == x;
            });
            return status.id;
        }

        var ChangeItemStatus = function (item) {
            if (item.indentQuantity > parseInt(item.issueQuantity)) {
                item.statusId = SetStatus('Partially Completed');
            } else if (item.indentQuantity == parseInt(item.issueQuantity)) {
                item.statusId = SetStatus('Completed');
            }
        }
        $scope.saveBtnDisabled=false;
        var SavePendingIndent = function () {
            
        	$scope.saveBtnDisabled=true;
        	
        	 if(self.model.carrier.id!=undefined && self.model.carrier.id>0)
   		  {
       		  ActualSavePendingIndent();
   		  }else{
   			  growl.warning(" Please select carrier !");
   			$scope.saveBtnDisabled=false;
   		  }
        	
}

        var ActualSavePendingIndent = function () {
        	$scope.saveBtnDisabled=true;
            var URI = CONSTANTS.PHARMACY.IN_PATIENT_PHARMACY.PATIENT_CONSUMABLE_ISSUE.SAVE_API;
            if (!!Validate()) {
                var data = {
                	"id": commonDetailService.getDataId(),
                    "patientIndentId": self.model.patientPendingIndentDetailList.patientIndentId,
                    "visitTypeId": self.model.patientPendingIndentDetailList.visitTypeId,
                    "admissionId": self.model.patientPendingIndentDetailList.admissionId,
                    "doctorId": self.model.patientPendingIndentDetailList.doctorId,
                    "storeId": self.model.patientPendingIndentDetailList.items[0].storeId,
                    "carrierId": self.model.carrier.id,
                    "remark": self.model.remark,
                    "statusId": $scope.statusId,
                    "patientId":$scope.patientId,
                    "items": [],
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "encounterId":self.model.patientPendingIndentDetailList.encounterId,
                    "atCop": $scope.atCop != undefined ? $scope.atCop : false 
                };

                $scope.storedTotalSum=0;
                var validateQty = function(item){
               	    var statusCount = 0;
             		if(item.itemId!=null){
                        var totalSame = 0;		
                        angular.forEach(self.model.patientPendingIndentDetailList.items, function (item1, key1) 
                        {
                            if(item.parentIssueDetailId == undefined && item.issueQuantity!=0){
                                if(item.id == item1.id && item1.parentIssueDetailId == undefined)
                                {            							
                                    if(item1.issueQuantity != undefined && item1.issueQuantity != null ){
                                        totalSame += CalculateOrederOrIssuedQty(item1);
                                    }           		
                                    angular.forEach(item1.selectedItems, function (item2, key2) 
                                    {
                                        totalSame += CalculateOrederOrIssuedQty(item2);
                                    });
                                }					
                            }
             								
                            if(item.id == item1.id && item1.parentIssueDetailId != undefined){
                                if(item1.issueQuantity != undefined && item1.issueQuantity != null ){
                                    totalSame += CalculateOrederOrIssuedQty(item1);
                                }
                            }
                        }); 
             							
                        if (item.indentQuantity > totalSame) {
                            statusCount = 1
                        } 
                        $scope.storedTotalSum += totalSame;
                    }
                    if (statusCount>0) {
                        return true;
                    } else if (statusCount == 0) {
                   		return false;
                    }
                }
                angular.forEach(self.model.patientPendingIndentDetailList.items, function (item, key) {
                    if(item.indentQuantity!=undefined && item.indentQuantity!=0 && (item.parentIssueDetailId == undefined || item.parentIssueDetailId == null) && item.issueQuantity !=0){
                        var result = validateQty(item);
                      
                    	
                    	if(item.selectedItems!=undefined && item.selectedItems!=null  && item.selectedItems.length > 0){
                   		    angular.forEach(item.selectedItems, function (itemSub1, keySub1) {
                   			    if(itemSub1.issueDetailId == undefined || itemSub1.issueDetailId == null){
                                    var result = validateQty(itemSub1);
                                  
                   		        }
                       	    });
                   	    }
                    }
                    else if(item.indentQuantity!=undefined && item.indentQuantity!=0 && (item.parentIssueDetailId != undefined || item.parentIssueDetailId != null) && item.issueQuantity !=0){
                        var result = validateQty(item);
                                            	                    	
                    }
                    else{
                    	if(item.selectedItems!=undefined && item.selectedItems!=null){
                            angular.forEach(item.selectedItems, function (itemSub1, keySub1) {
                                if(itemSub1.issueDetailId==null){
                                    var result = validateQty(itemSub1);
                                    
                    		    }
                            });
                    	}
                    }
                    var itemStatus="";
                    if(self.model.patientPendingIndentDetailList.status == 'Partially Completed'){
                    	
                        if(item.flag == false){
                            $scope.calculateAtCop(item);
                            var items = {
                                "id": item.id,
                                "batchList": [],
                                "isBatchReq": item.isBatchRequired,
                                "batchId": (item.batchList[0] == undefined || item.batchList[0].batchId==undefined||item.batchList[0].batchId==null||item.batchList[0].batchId=='')?0:item.batchList[0].batchId,
                                "uomTypeId": item.uomTypeId,
                                "uomUnitId": item.uomUnitId,
                                "consumableQuantity": item.issueQuantity,
                                "statusId":  itemStatus!="" ? SetStatus(itemStatus):SetStatus('Pending'),
                                "leaseQuantity": item.leaseQuantity,
                                "netAmount": item.netAmount ? item.netAmount : 0,
                                "quantity": item.issueQuantity,
                                "rate": item.rate ? item.rate : 0,
                                "taxAmount": item.taxAmount ? item.taxAmount : 0,
                                "taxId": item.taxId ? item.taxId : 0,
                                "taxPercentage": item.taxPercentage ? item.taxPercentage : 0,
                                "totalAmount": item.totalAmount ? item.totalAmount : 0,
                                "productCategoryId":item.productCategoryId,
                                "itemId":item.itemId,
                                "creditPayable":0,
                                "selfPayable": item.netAmount ? item.netAmount : 0,
                                "consessionPercentage": item.patientClassPercentage,
                                "consessionAmount": item.patientClassAmount,
                                "unitConsessionAmount": item.unitPatientClassAmount ? item.unitPatientClassAmount.toFixed(2) : 0,
                                "payeeId":cookieObjectAppConfig.selfPayeeId
                            }
                            
                            if(item.selectedItems!=null && item.selectedItems.length > 0){
                        		angular.forEach(item.selectedItems, function (item1, key1) {
                        			$scope.calculateAtCop(item1);
                        			if(item1.savedSubItem==undefined || item1.savedSubItem==false){
                        		    	if(item1.issueDetailId == undefined || item1.issueDetailId == null){
                                            var subItems = {
                                                "id": null ,
                                                "parentId":item1.id,
                                                "batchList": [],
                                                "isBatchReq": item1.isBatchRequired,
                                                "batchId": (item1.newBatch==undefined || item1.newBatch==null || item1.newBatch[0].selectedBatch.batchId==undefined||item1.newBatch[0].selectedBatch.batchId==null||item1.newBatch[0].selectedBatch.batchId=='')?0:item1.newBatch[0].selectedBatch.batchId,
                                                "uomTypeId": item1.uomTypeId,
                                                "uomUnitId": item1.uomUnitId,
                                                "consumableQuantity": item1.issueQuantity,
                                                "statusId": data.statusId==4 ? SetStatus('Completed') : SetStatus('Pending'),
                                                "leaseQuantity": item1.leaseQuantity,
                                                "netAmount": item1.netAmount ? item1.netAmount : 0,
                                                "quantity": item1.issueQuantity,
                                                "rate": item1.rate ? item1.rate : 0,
                                                "taxAmount": item1.taxAmount ? item1.taxAmount : 0,
                                                "taxId": item1.taxId ? item1.taxId : 0,
                                                "taxPercentage": item1.taxPercentage ? item1.taxPercentage : 0,
                                                "totalAmount": item1.totalAmount ? item1.totalAmount : 0,
                                                "productCategoryId":item1.productCategoryId,
                                                "itemId":item1.itemId,
                                                "creditPayable":0,
                                                "selfPayable": item1.netAmount ? item1.netAmount : 0,
                                                "consessionPercentage": item1.patientClassPercentage,
                                                "consessionAmount": item1.patientClassAmount,
                                                "unitConsessionAmount": item1.unitPatientClassAmount ? item1.unitPatientClassAmount.toFixed(2) : 0,
                                                "payeeId":cookieObjectAppConfig.selfPayeeId,
                                                "indentQuantity":item1.newIndentQuantity
                                            }
                        			 
                                            angular.forEach(item1.batch, function (batch) {
                                                if(item.selected == true){
                                                    if((batch.isSelectedP == true) && (batch.issueQuantity != undefined)){    
                                                        var batchList = {
                                                            "batchId": parseInt(batch.batchId),
                                                            "batchNo": batch.batchNo,//added for resolved HIS-8703
                                                            "uomTypeId": batch.uomTypeId,
                                                            "uomUnitId": batch.uomUnitId,
                                                            "issueQuantity": parseInt(batch.issueQuantity),
                                                            "rate":  batch.rate ?  batch.rate : 0,
                                                        }
                                                        subItems.batchList.push(batchList);
                                                    }
                                                }
                                                else{
                                                    if((batch.isSelected == true) && (batch.issueQuantity != undefined)){    
                                                        var batchList = {
                                                            "batchId": parseInt(batch.batchId),
                                                            "batchNo": batch.batchNo,//added for resolved HIS-8703
                                                            "uomTypeId": batch.uomTypeId,
                                                            "uomUnitId": batch.uomUnitId,
                                                            "issueQuantity": parseInt(batch.issueQuantity),
                                                            "rate":  batch.rate ?  batch.rate : 0,
                                                        }
                                                        subItems.batchList.push(batchList);
                                                    }
                                                }
                                            });
                                            data.items.push(subItems);
                        		        }
                        		    }
                        		});
                        	}
                            
                            if(item.patientIndentDetailId!=undefined || item.patientIndentDetailId!=null){
                                angular.forEach(item.batch, function (batch) {
                                    if(item.selected == true){
                                        if((batch.isSelectedP == true && batch.isSelected == true) && (batch.issueQuantity != undefined)){
                                            angular.forEach(batch.uom, function(u){
                                                if(batch.uomType == u.uomType){
                                                    batch.uomTypeId = u.uomTypeId;
                                                }
                                                if(batch.uomUnit == u.uomUnit){
                                                    batch.uomUnitId = u.uomUnitId;
                                                }
                                            });
                                            var batchList = {
                                                "batchId": parseInt(batch.batchId),
                                                "batchNo": batch.batchNo,//added for resolved HIS-8703
                                                "uomTypeId": batch.uomTypeId,
                                                "uomUnitId": batch.uomUnitId,
                                                "issueQuantity": parseInt(batch.issueQuantity),
                                                "rate":  batch.rate ?  batch.rate : 0,
                                            }
                                            items.batchList.push(batchList);
                                        }
                                    }
                                    else{
                                        if((batch.isSelected == true && batch.isSelectedP == true) && (batch.issueQuantity != undefined)){   
                                            angular.forEach(batch.uom, function(u){
                                                if(batch.uomType == u.uomType){
                                                    batch.uomTypeId = u.uomTypeId;
                                                }
                                                if(batch.uomUnit == u.uomUnit){
                                                    batch.uomUnitId = u.uomUnitId;
                                                }
                                            }); 
                                            var batchList = {
                                                "batchId": parseInt(batch.batchId),
                                                "batchNo": batch.batchNo,//added for resolved HIS-8703
                                                "uomTypeId": batch.uomTypeId,
                                                "uomUnitId": batch.uomUnitId,
                                                "issueQuantity": parseInt(batch.issueQuantity),
                                                "rate":  batch.rate ?  batch.rate : 0,
                                            }
                                            items.batchList.push(batchList);
                                        }        
                                    }
                                });
                            }
                            if($scope.length != self.model.patientPendingIndentDetailList.items.length){
                                items.statusId = SetStatus('Partially Completed');
                            }
                            if(item.issueQuantity != undefined && item.issueQuantity !=0 && (item.id !=undefined|| item.id !=null)){
                                data.items.push(items);
                                data.roundOff = parseFloat((items.netAmount - Math.round(items.netAmount)).toFixed(2));
                                }
                        }
                    }
                    else{
                    	if(item.issueQuantity != undefined && item.issueQuantity !=0 && (item.id !=undefined|| item.id !=null)){
                            $scope.calculateAtCop(item);
                            var items = {
                                "id": item.id,
                                "batchList": [],
                                "isBatchReq": item.isBatchRequired,
                                "batchId": (item.batchList[0].batchId==undefined||item.batchList[0].batchId==null||item.batchList[0].batchId=='')?0:item.batchList[0].batchId,
                                "uomTypeId": item.uomTypeId,
                                "uomUnitId": item.uomUnitId,
                                "consumableQuantity": item.issueQuantity,
                                "statusId": SetStatus('Pending'),
                                "leaseQuantity": item.leaseQuantity,
                                "netAmount": item.netAmount ? item.netAmount : 0,
                                "quantity": item.issueQuantity,
                                "rate": item.rate ? item.rate : 0,
                                "taxAmount": item.taxAmount ? item.taxAmount : 0,
                                "taxId": item.taxId ? item.taxId : 0,
                                "taxPercentage": item.taxPercentage ? item.taxPercentage : 0,
                                "totalAmount": item.totalAmount ? item.totalAmount : 0,
                                "productCategoryId":item.productCategoryId,
                                "itemId":item.itemId,
                                "creditPayable":0,
                                "selfPayable": item.netAmount ? item.netAmount : 0,
                                "consessionPercentage": item.patientClassPercentage,
                                "consessionAmount": item.patientClassAmount,
                                "unitConsessionAmount": item.unitPatientClassAmount ? item.unitPatientClassAmount.toFixed(2) : 0,
                                "payeeId":cookieObjectAppConfig.selfPayeeId
                            }
                            
                            if(item.selectedItems!=null && item.selectedItems.length > 0){
                        		angular.forEach(item.selectedItems, function (item1, key1) {
                        			$scope.calculateAtCop(item1);
                        			if(item1.savedSubItem==undefined || item1.savedSubItem==false){
                        			    var subItems = {
                                            "id": null ,
                                            "parentId":item1.id,
                                            "batchList": [],
                                            "isBatchReq": item1.isBatchRequired,
                                            "batchId": (item1.newBatch[0].selectedBatch.batchId==undefined||item1.newBatch[0].selectedBatch.batchId==null||item1.newBatch[0].selectedBatch.batchId=='')?0:item1.newBatch[0].selectedBatch.batchId,
                                            "uomTypeId": item1.uomTypeId,
                                            "uomUnitId": item1.uomUnitId,
                                            "consumableQuantity": item1.issueQuantity,
                                            "statusId": data.statusId==4 ? SetStatus('Completed') : SetStatus('Pending'),
                                            "leaseQuantity": item1.leaseQuantity,
                                            "netAmount": item1.netAmount ? item1.netAmount : 0,
                                            "quantity": item1.issueQuantity,
                                            "rate": item1.rate ? item1.rate : 0,
                                            "taxAmount": item1.taxAmount ? item1.taxAmount : 0,
                                            "taxId": item1.taxId ? item1.taxId : 0,
                                            "taxPercentage": item1.taxPercentage ? item1.taxPercentage : 0,
                                            "totalAmount": item1.totalAmount ? item1.totalAmount : 0,
                                            "productCategoryId":item1.productCategoryId,
                                            "itemId":item1.itemId,
                                            "creditPayable":0,
                                            "selfPayable": item1.netAmount ? item1.netAmount : 0,
                                            "consessionPercentage": item1.patientClassPercentage,
                                            "consessionAmount": item1.patientClassAmount,
                                            "unitConsessionAmount": item1.unitPatientClassAmount ? item1.unitPatientClassAmount.toFixed(2) : 0,
                                            "payeeId":cookieObjectAppConfig.selfPayeeId,
                                            "indentQuantity":item1.newIndentQuantity
                                        }
                                  
                        			 
                        			    angular.forEach(item1.batch, function (batch) {
                                            if(batch.isSelected == true){
                                                if((batch.isSelected == true) && (batch.issueQuantity != undefined && batch.issueQuantity != 0)){    
                                                    var batchList = {
                                                        "batchId": parseInt(batch.batchId),
                                                        "batchNo": batch.batchNo,//added for resolved HIS-8703
                                                        "uomTypeId": batch.uomTypeId,
                                                        "uomUnitId": batch.uomUnitId,
                                                        "issueQuantity": parseInt(batch.issueQuantity),
                                                        "rate":  batch.rate!=undefined ?  batch.rate : batch.sellingPrice,
                                                    }
                                                    subItems.batchList.push(batchList);
                                                }
                                            }
                                            else{
                                                if((batch.isSelected == true) && (batch.issueQuantity != undefined && batch.issueQuantity != 0)){    
                                                    var batchList = {
                                                        "batchId": parseInt(batch.batchId),
                                                        "batchNo": batch.batchNo,//added for resolved HIS-8703
                                                        "uomTypeId": batch.uomTypeId,
                                                        "uomUnitId": batch.uomUnitId,
                                                        "issueQuantity": parseInt(batch.issueQuantity),
                                                        "rate":  batch.rate!=undefined ?  batch.rate : batch.sellingPrice,
                                                    }
                                                    subItems.batchList.push(batchList);
                                                } 
                                            }
                                        });
                        			    data.items.push(subItems);
                        		    }
                        		});
                        	}
                            if(item.parentIssueDetailId==undefined || item.parentIssueDetailId==null){
                                angular.forEach(item.batch, function (batch) {
                                    if(item.selected == true){
                                        if((batch.isSelectedP == true) && (batch.issueQuantity != undefined && batch.issueQuantity != 0)){    
                                            var batchList = {
                                                "batchId": parseInt(batch.batchId),
                                                "batchNo": batch.batchNo,//added for resolved HIS-8703
                                                "uomTypeId": batch.uomTypeId,
                                                "uomUnitId": batch.uomUnitId,
                                                "issueQuantity": parseInt(batch.issueQuantity),
                                                "rate":  batch.rate ?  batch.rate : 0,
                                            }
                                            items.batchList.push(batchList);
                                        }
                                    }
                                    else{
                                        if((batch.isSelected == true) && (batch.issueQuantity != undefined && batch.issueQuantity != 0 )){    
                                            var batchList = {
                                                "batchId": parseInt(batch.batchId),
                                                "batchNo": batch.batchNo,//added for resolved HIS-8703
                                                "uomTypeId": batch.uomTypeId,
                                                "uomUnitId": batch.uomUnitId,
                                                "issueQuantity": parseInt(batch.issueQuantity),
                                                "rate":  batch.rate ?  batch.rate : 0,
                                            }
                                            items.batchList.push(batchList);
                                        }
                                    }
                                });
                    	    }
                            if($scope.length != self.model.patientPendingIndentDetailList.items.length){
                                items.statusId = SetStatus('Partially Completed');
                            }
                            data.items.push(items);
                            data.roundOff = parseFloat((items.netAmount - Math.round(items.netAmount)).toFixed(2));
                    	}
                        else
                        {
                    		if(item.selectedItems!=null && item.selectedItems.length > 0){
                    	    	angular.forEach(item.selectedItems, function (item1, key1) {
                                    if(item1.newBatch == undefined || item1.newBatch == null){
                                        item1.newBatch = [];
                                        item1.newBatch.push(item1.selectedBatch);
                                        if(item1.newBatch[0].selectedBatch == undefined || item1.newBatch[0].selectedBatch == null){
                                            item1.newBatch[0].selectedBatch = item1.selectedBatch;
                                        }
                                    }
                    	    		$scope.calculateAtCop(item1);
                    		    	if(item1.savedSubItem==undefined || item1.savedSubItem==false){
                                        var subItems = {
                                            "id": null ,
                                            "parentId":item1.id,
                                            "batchList": [],
                                            "isBatchReq": item1.isBatchRequired,
                                            "batchId": (item1.newBatch[0].selectedBatch.batchId==undefined||item1.newBatch[0].selectedBatch.batchId==null||item1.newBatch[0].selectedBatch.batchId=='')?0:item1.newBatch[0].selectedBatch.batchId,
                                            "uomTypeId": item1.uomTypeId,
                                            "uomUnitId": item1.uomUnitId,
                                            "consumableQuantity": item1.issueQuantity,
                                            "statusId": data.statusId==4 ? SetStatus('Completed') : SetStatus('Partially Completed'),
                                            "leaseQuantity": item1.leaseQuantity,
                                            "netAmount": item1.netAmount ? item1.netAmount : 0,
                                            "quantity": item1.issueQuantity,
                                            "rate": item1.rate ? item1.rate : 0,
                                            "taxAmount": item1.taxAmount ? item1.taxAmount : 0,
                                            "taxId": item1.taxId ? item1.taxId : 0,
                                            "taxPercentage": item1.taxPercentage ? item1.taxPercentage : 0,
                                            "totalAmount": item1.totalAmount ? item1.totalAmount : 0,
                                            "productCategoryId":item1.productCategoryId,
                                            "itemId":item1.itemId,
                                            "creditPayable":0,
                                            "selfPayable": item1.netAmount ? item1.netAmount : 0,
                                            "consessionPercentage": item1.patientClassPercentage,
                                            "consessionAmount": item1.patientClassAmount,
                                            "unitConsessionAmount": item1.unitPatientClassAmount ? item1.unitPatientClassAmount.toFixed(2) : 0,
                                            "payeeId":cookieObjectAppConfig.selfPayeeId,
                                            "indentQuantity":item1.newIndentQuantity
                                        }
                              
                    			        angular.forEach(item1.batch, function (batch) {
                                            if(batch.isSelected == true){
                                                if((batch.isSelected == true) && (batch.issueQuantity != undefined && batch.issueQuantity != 0)){    
                                                    var batchList = {
                                                        "batchId": parseInt(batch.batchId),
                                                        "batchNo": batch.batchNo,//added for resolved HIS-8703
                                                        "uomTypeId": batch.uomTypeId,
                                                        "uomUnitId": batch.uomUnitId,
                                                        "issueQuantity": parseInt(batch.issueQuantity),
                                                        "rate":  batch.rate!=undefined ?  batch.rate : batch.sellingPrice,
                                                    }
                                                    subItems.batchList.push(batchList);
                                                }
                                            }
                                        });
                   			            data.items.push(subItems);
                    		        }
                    		    });
                    	    }
                    	}
                    }
                });
                
                
            	var testSumMain=0;
                var testSumSub=0;
            	var finalSum=0;
            	angular.forEach(self.model.patientPendingIndentDetailList.items, function(value, key){
            		testSumMain+=value.issueQuantity;
            		
                    if(value.selectedItems !=undefined && value.selectedItems.length > 0){
                    	angular.forEach(value.selectedItems, function(valueSub, keySub){
                    		testSumMain+=valueSub.issueQuantity;		
            		    });
                    }
            	});
            	 if($scope.orderQtyTotalSum == $scope.storedTotalSum){
          	        data.statusId = 4;
                 }else if($scope.orderQtyTotalSum > $scope.storedTotalSum){
          	        data.statusId = 18;
                 }  
            	
                if($scope.validateFinalQtyWithSubQty()){
                	var checkQty = false;
                	if (data.items.length > 0 ) {
                		angular.forEach(data.items, function(valueSub, keySub){
                			if(valueSub.quantity == undefined || valueSub.quantity == null ||  valueSub.quantity ==0 || valueSub.batchList == null || valueSub.batchList.length ==0 ){
                				checkQty = true;
                			}
            		    });
                	}
                	
                	if(checkQty == true){
                		 growl.error("Please enter quantity");
                		   $scope.saveBtnDisabled = false;
                		 return false;
                	}
                	
                	if (data.items.length > 0) {
                        $rootScope.startLoader();
                        GenericService.serviceAction("POST", URI, data).then(
                            function (response) {
                                // ClearRequests();
                                $rootScope.stopLoader();
                                NavigteToPatientConsumableIssue();
                                $scope.saveBtnDisabled = false;
                                growl.success(response.data.message);
                            },
                            function (err) {
                                $rootScope.stopLoader();
                                $scope.saveBtnDisabled = false;
                                growl.error(err.data.message);
                            });
                    } else {
                        $rootScope.stopLoader();
                        NavigteToPatientConsumableIssue();
                    }
                }
            }
        }

       /* $scope.validateFinalQtyWithSubQty=function(){
        	var isValid=true;
        	angular.forEach(self.model.patientPendingIndentDetailList.items, function(value, key){
        		if( $scope.storedTotalSum > value.indentQuantity){
        			 growl.error("Please enter valid quantity.");
        			 isValid=false;
        		}
        	});
        	return isValid;
        }*/
        
        $scope.validateFinalQtyWithSubQty=function(){
        	
       
        	/*var isValid=true;
        	angular.forEach(self.model.patientPendingIndentDetailList.items, function(value, key){
        		if(value.selectedItems !=undefined && value.selectedItems.length > 0){
        			if( $scope.storedTotalSum > value.indentQuantity) {
        				growl.error("Please enter valid quantity.");
           			 isValid=false;
        			}
        		}else{
        			isValid=true;
        		}
        	});
        	return isValid;*/
        
        	
        /**For checking sum of main item and sub item**/
        	var isValid=true;
        	var testSumMain=0;
        	var testSumSub=0;
        	var finalSum=0;
        	angular.forEach(self.model.patientPendingIndentDetailList.items, function(value, key){
        		testSumMain+=value.issueQuantity;
        		
                if(value.selectedItems !=undefined && value.selectedItems.length > 0){
                	angular.forEach(value.selectedItems, function(valueSub, keySub){
                		testSumSub+=valueSub.issueQuantity;		
        		    });
                }
                if(testSumSub > value.indentQuantity){
                    growl.error("Please enter valid quantity.");
                    isValid=false;
                    $scope.saveBtnDisabled = false;
                }else{
					isValid=true;
				}

                finalSum = testSumMain + testSumSub;   
                if(finalSum > value.indentQuantity) {
                    growl.error("Please enter valid quantity.");
                    isValid=false;							
                    $scope.saveBtnDisabled = false;
                }else{
                	testSumMain=0;
                    testSumSub=0;
                    isValid=true;
                }
        	});
            
        	return isValid;
        }
        
        var Validate = function () {
            var isValid = true;
            var count=0;
            if (!self.model.patientPendingIndentDetailList.items || self.model.patientPendingIndentDetailList.items.length <= 0) {
                isValid = false;
                growl.error("Please add some items in the list");
                $scope.saveBtnDisabled = false;
            } else {
                self.model.patientPendingIndentDetailList.items.forEach(function (item) {
                	if(item.parentIssueDetailId== undefined && item.parentIssueDetailId ==null){
                		// if((item.issueQuantity==undefined || item.issueQuantity==0) && item.selectedItems == undefined){
                            item['isCarrierValid'] = false;

                            if (!self.model.carrier.id && isValid == true) {
                                item['isCarrierValid'] = true;
                                isValid = false;
                                growl.error("Please select carrier");
                                $scope.saveBtnDisabled = false;
                            }
                            if (item.issueQuantity > item.indentQuantity && isValid == true)  {
                                isValid = false;
                                growl.error("Please Enter Valid Issue Quantity");
                                $scope.saveBtnDisabled = false;
                            }
                            if(!item.issueQuantity && (item.selectedItems == undefined || item.selectedItems ==null) && isValid == true ){
                                isValid = false;
                                growl.error("Please enter issued quantity");
                                $scope.saveBtnDisabled = false;
                            }
                            var isBatchSelected = false;
                            if(( item.selectedItems == undefined || item.selectedItems.length == 0) && item.batchList.length > 0){
                                item.batchList.forEach(function (batch) {
                                	if(batch!=undefined && batch!=null)
                                		{
                                    batch['isBatchValid'] = false;
                                    batch['isIssueQuantityValid'] = false;
                                    if (!isBatchSelected && (batch.isSelected || (batch.isSelectedP!=undefined ? batch.isSelectedP :true)) || (batch.selectedBatch.isSelected || batch.selectedBatch.isSelectedP)) {
                                        isBatchSelected = true;
                                    }
                                    if(batch.selectedBatch != undefined && batch.selectedBatch != null){
                                        if ((((!batch.selectedBatch.availableStockQuantity) > (batch.selectedBatch.displayQuantity)) || (!batch.selectedBatch.availableStockQuantity) || (batch.selectedBatch.availableStockQuantity == 0)) && isValid == true ) {
                                        isValid = false;
                                            batch['isIssueQuantityValid'] = true;
                                            growl.error("Please enter issue quantity");
                                            $scope.saveBtnDisabled = false;
                                        }
                                        if(batch.selectedBatch.issueQuantity==undefined || batch.selectedBatch.issueQuantity==null || batch.selectedBatch.issueQuantity==0)
                                        	{
                                        	count++;
                                        	}
                                    }   
                                		}else if(item.selectedItems!=undefined && item.selectedItems!=null && item.selectedItems.length>0)
                                			{
                                			isBatchSelected = true;
                                			}
                                });
                            }
                            else{
                                isBatchSelected = true;
                            }
                        
                            if (!isBatchSelected && isValid == true) {
                                growl.error("Please Select Batch");
                                isValid = false;
                                $scope.saveBtnDisabled = false;
                            }
                        
                	    // }else 
                        if(item.selectedItems!=undefined){
                            item.selectedItems.forEach(function (itemSelected) {
                                if(itemSelected.issueQuantity == undefined || itemSelected.issueQuantity == null || itemSelected.issueQuantity == 0){
                                itemSelected['isCarrierValid'] = false;

                                if (!self.model.carrier.id && isValid == true ) {
                                    itemSelected['isCarrierValid'] = true;
                                    isValid = false;
                                    growl.error("Please select carrier");
                                    $scope.saveBtnDisabled = false;
                                }
                                if (itemSelected.issueQuantity > itemSelected.indentQuantity && isValid == true) {
                                    isValid = false;
                                    growl.error("Please Enter Valid Issue Quantity");
                                    $scope.saveBtnDisabled = false;
                                }
                                if(!itemSelected.issueQuantity && isValid == true){
                                    isValid = false;
                                    growl.error("Please enter issued quantity");
                                    $scope.saveBtnDisabled = false;
                                }
                                var isBatchSelected = false;
                                if(itemSelected.batch.length > 0){
                                    
                                    itemSelected.batch.forEach(function (batch) {
                                        batch['isBatchValid'] = false;
                                        batch['isIssueQuantityValid'] = false;
                                        if (!isBatchSelected && batch.isSelected || batch.isSelectedP) {
                                            isBatchSelected = true;
                                        }

                                        if ((((!batch.availableStockQuantity) > (batch.displayQuantity)) || (!batch.availableStockQuantity) || (batch.availableStockQuantity == 0))&& isValid == true) {
                                            isValid = false;
                                            batch['isIssueQuantityValid'] = true;
                                            growl.error("Please enter issue quantity");
                                            $scope.saveBtnDisabled = false;
                                        }
                                        if(batch.issueQuantity==undefined || batch.issueQuantity==null || batch.issueQuantity==0)
                                    	{
                                    	count++;
                                    	}

                                    });
                                }else{
                                    isBatchSelected = true;
                                }
                                
                                
                                if (!isBatchSelected && isValid == true) {
                                    growl.error("Please Select Batch");
                                    isValid = false;
                                    $scope.saveBtnDisabled = false;
                                }
                                
                            }
                        });
                	}
                }
                });
            }
            if(count>0 && isValid == true )
            	{
            	growl.error("Please Enter Quantity");
            	$scope.saveBtnDisabled=false;
            	isValid = false;
            	}
            return isValid;
        }

        var NavigteToPatientConsumableIssue = function () {
            $state.go('patientConsumableIssue');
        }

        $scope.selectItemIdListForAutoBatch=[];
        
        $scope.selectBatchofSelectedItem=function (ItemObject,indexValue) {
        	if(ItemObject.batchList!= null && ItemObject.batchList.length > 0){
        	 if(self.model.patientPendingIndentDetailList.status == 'Pending'){
        		 //block  is  for  only Pending  patientPendingIndentDetailList
             if($scope.selectItemIdListForAutoBatch.length==0){
        		
        		if(ItemObject.batchList!=undefined && ItemObject.batchList!=null &&  ItemObject.batchList.length>0 ){
                    var ItemBatchObject=ItemObject.batchList[0];
                    if(ItemObject.batchList[0]!=undefined && ItemObject.batchList[0]!=null && $scope.autoBatch == true)
                    	{
                    ItemBatchObject.selectedBatch=ItemObject.batchList[0];
                    	}
                    $scope.selectItemIdListForAutoBatch.push(ItemObject.itemId)
                    batchOnChange(ItemBatchObject, ItemBatchObject, ItemObject.itemId, indexValue , ItemObject)
                    		$scope.convertQtyOrdered(ItemBatchObject,ItemObject);
                 } 
        	}else{
        		var alreadyExist=false;
        		angular.forEach($scope.selectItemIdListForAutoBatch,function(value,index){
        			if(value==ItemObject.itemId){
        				alreadyExist=true;
        			}
        		});
        		if(!alreadyExist){
        			if(ItemObject.batchList!=undefined && ItemObject.batchList!=null &&  ItemObject.batchList.length>0 ){
                        var ItemBatchObject=ItemObject.batchList[0];
                        if($scope.autoBatch == true){
                        	ItemBatchObject.selectedBatch=ItemObject.batchList[0];
                        }
                        $scope.selectItemIdListForAutoBatch.push(ItemObject.itemId)
                        batchOnChange(ItemBatchObject, ItemBatchObject, ItemObject.itemId, indexValue , ItemObject)
                        $scope.convertQtyOrdered(ItemBatchObject,ItemObject);
                     } 
        		}
        	}
        	 }
        }
        	
        	if((ItemObject.batch!= null && ItemObject.batch.length > 0) && ItemObject.newBatch==undefined && (ItemObject.selected==undefined || ItemObject.selected==false)){
        		$scope.batchFlag=false;
              	 if(self.model.patientPendingIndentDetailList.status == 'Pending'){
           		 //block  is  for  only Pending  patientPendingIndentDetailList
                if($scope.selectItemIdListForAutoBatch.length==0){
           		
           		if(ItemObject.batch!=undefined && ItemObject.batch!=null &&  ItemObject.batch.length>0 ){
                       var ItemBatchObject=ItemObject.batch[0];
                       ItemBatchObject.selectedBatch=ItemObject.batch[0];
                       $scope.selectItemIdListForAutoBatch.push(ItemObject.itemId)
                       batchOnChange(ItemBatchObject, ItemBatchObject, ItemObject.itemId, indexValue , ItemObject)
                       $scope.convertQtyOrdered(ItemBatchObject,ItemObject);
                    } 
           	}else{
           		var alreadyExist=false;
           		angular.forEach($scope.selectItemIdListForAutoBatch,function(value,index){
           			if(value==ItemObject.itemId){
           				alreadyExist=true;
           			}
           		});
           		if(!alreadyExist){
           			if(ItemObject.batch!=undefined && ItemObject.batch!=null &&  ItemObject.batch.length>0 ){
           			 var ItemBatchObject=ItemObject.batch[0];
                     ItemBatchObject.selectedBatch=ItemObject.batch[0];
                     $scope.selectItemIdListForAutoBatch.push(ItemObject.itemId)
                           batchOnChange(ItemBatchObject, ItemBatchObject, ItemObject.itemId, indexValue , ItemObject)
                           $scope.convertQtyOrdered(ItemBatchObject,ItemObject);
                        } 
           		}
           	}
           	 }
           }
        	else if(ItemObject.newBatch!= null && ItemObject.newBatch.length > 0){
        		 $scope.batchFlag=true;
        	 }
        	angular.forEach(ItemObject.batchIssued,function(value,index){
        		var ItemBatchObject=value;
        		if(ItemBatchObject.uom==undefined || ItemBatchObject.uom==null || ItemBatchObject.uom.length==0)
        			{
        			ItemBatchObject.uom=ItemObject.uoms;
        			}
        		$scope.convertQtyOrdered(ItemBatchObject,ItemObject);
        	})
        }
        
        // var SetFocus = function () {
        //     self.model.barcode = undefined;
        //     $window.document.getElementById('barcode').focus();
        // }

        var AddBatchToDetail = function (worklist, itemId, batchId) {
            var itemId = itemId;
            var batchId = batchId;

        }

//        $scope.subFlag = true;
//        $scope.addFlag = true;

        var ValidateIssueQty = function(batch, indentQuantity, item) {
            var isValid = true;
            item.subFlag=true;
            item.addFlag=true; 
            if(batch.selectedBatch.rate==0)
        	{
        	batch.selectedBatch.rate=batch.selectedBatch.sellingPrice;
        	}
            
            var indentTotal =parseInt(0);
			angular.forEach(item.batchList, function(d){
				if(d!=undefined && d!=null)
        		{
    			indentTotal=indentTotal+parseInt(d.selectedBatch.issueQuantity);
        		}
    		});
		
            
            if(parseInt(batch.selectedBatch.issueQuantity) == 0 )
            {
                batch.isIssueQuantityValid = true;
                batch.issueQuantity = 0;
                batch.selectedBatch.issueQuantity = 0;
                isValid = false;
                growl.error('Please enter valid quantity')   
            }
           
            if (parseInt(batch.selectedBatch.issueQuantity) > batch.selectedBatch.newStock) {
                batch.isIssueQuantityValid = true;
                batch.issueQuantity = 0;
                batch.selectedBatch.issueQuantity = 0;
                isValid = false;
                growl.error('Issue quantity must be less than or equal to available stock quantity')
            }
            if (indentTotal > item.newIndentQuantity) {
                batch.isIssueQuantityValid = true;
                batch.issueQuantity = 0;
                batch.selectedBatch.issueQuantity = 0;
                isValid = false;
                growl.error('Issue quantity must be less than or equal to Indent quantity')
            }else{
                batch.isIssueQuantityValid = false;
                isValid = true;
            }
            
            if(item.issueQuantity == item.newIndentQuantity && parseInt(batch.selectedBatch.issueQuantity)>0 && parseInt(batch.selectedBatch.issueQuantity) < batch.selectedBatch.newStock){
            	item.subFlag = true;
                item.addFlag = true;
            }
            else{
            	item.subFlag = false;
                item.addFlag = false;
            }

            return isValid;
        }

        $scope.validateTotalIssuedQty = function(selectedBatch, batch,item, index){
        	$scope.totalIssuedQty= 0;
        	
        	angular.forEach(self.model.patientPendingIndentDetailList.items, function(value, key){
        		if(item.id == value.id){
        			value.subFlag = false;
            		value.addFlag = false;
            		$scope.totalIssuedQty += value.issueQuantity;
            		if($scope.totalIssuedQty == value.indentQuantity){
            			value.subFlag = true;
                        value.addFlag = true;
            		}
            		if(value.selectedItems != null && value.selectedItems.length > 0){
            			angular.forEach(value.selectedItems, function(value1, key1){
                    		value.subFlag = false;
                    		value.addFlag = false;
                    		$scope.totalIssuedQty += value1.issueQuantity;
                    		if($scope.totalIssuedQty == value.indentQuantity){
                    			value.subFlag = true;
                                value.addFlag = true;
                    			value1.subFlag = true;
                                value1.addFlag = true;
                    		}
                    	});
            		}
        		}
        		
        	});
        }
        
        var ClearBill = function() {
            self.model.barcode = "";
        }

         //Method to get the list of Generic 
    var GetGenericList = function () {

        var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.GENERIC_MASTER.GENERIC_MASTER_API+ $scope.organizationId;
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.genericList = response.data;
            GetGenericMaster();
            $rootScope.stopLoader();
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
          });
      }
      GetGenericList();
  
      var genericMasterList = [];
      var GetGenericMaster = function () {
        var arraylist = self.model.genericList.filter(x => x.status == true);
        $scope.startLoader();
        for (let generic of arraylist){
          genericMasterList.push(generic);
        }       
        $rootScope.stopLoader();
      } 

      var GetStrengthUnitList = function () {

        var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY. ITEM_MASTER.UNIT_API + '/'+$scope.organizationId ;
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
              self.model.strengthUnitList = response.data;
              console.log("Strength Units : ",self.model.strengthUnitList);
              $rootScope.stopLoader();
            },
            function errorCallback(response) {
              console.log("Error");
              console.log("Strength Units : ",self.model.strengthUnitList);
              $rootScope.stopLoader();
            });
      }
      GetStrengthUnitList();
  
      var ViewCombinationDetails = function(item) {
        self.model.genericCombinationList = [];
        var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.GENERIC_MASTER.GENERIC_MASTER_IS_COMBINATION_DETAILS_API + item.genericId;
  
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            for(let combination of response.data) {
              let combinationName = genericMasterList.filter(x => x.id == combination.subGenericId);
              combination.name = combinationName.length > 0 ? combinationName[0].name : '-';
              combination.code = item.code;
              combination.genericName = item.name;
            }
            self.model.strengthUnitList.forEach(function (strength){
                response.data.forEach(function (data){
                  if(data.strength == strength.id){
                    $scope.strength = strength.unit;
                    var obj = {
                      strength: "",
                      code:"",
                      genericId: "",
                      genericName: "",
                      id:"",
                      name:"",
                      strengthUnit:"",
                      subGenericId: "",
                    }
                    obj.strength = $scope.strength;
                    obj.code = data.code;
                    obj.genericId = data.genericId;
                    obj.genericName = data.genericName;
                    obj.id = data.id;
                    obj.name = data.name;
                    obj.strengthUnit = data.strengthUnit;
                    obj.subGenericId = data.subGenericId;
                    self.model.genericCombinationList.push(obj);
                  }
                });
              });
            var popup = angular.element("#combinationDetails");
            popup.modal("show");
            $rootScope.stopLoader();
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
          });
      }

    //to calculate
    var CalculateOrederOrIssuedQty = function(item){
        var issuedQty = 0;
        var issuedQtyNew = item.issueQuantity != undefined ? item.issueQuantity : 0;
        if(item.uomType !=undefined && item.uomTypeId !=undefined)
        {
            if(item.uoms == undefined || item.uoms == null){
                angular.forEach(item.batchList, function(b){
                	if(b!=undefined && b!=null)
            		{
                    if(item.uomTypeId == 3)
                    {
                        issuedQty  = parseInt(issuedQtyNew) * b.selectedBatch.uom[2].conversion;
                    }
                    else if(item.uomTypeId == 2)
                    {
                        var stripQty = parseInt(issuedQtyNew) * b.selectedBatch.uom[1].conversion * b.selectedBatch.uom[2].conversion;
                        issuedQty  = stripQty;     
                    }
                    else if(item.uomTypeId == 1)
                    {
                        var boxQty = parseInt(issuedQtyNew) * b.selectedBatch.uom[0].conversion * b.selectedBatch.uom[1].conversion * b.selectedBatch.uom[2].conversion;
                        issuedQty  = boxQty;
                    }
            		}
                })
            }
            else{
                if(item.uomTypeId == 3)
                {
                    issuedQty  = parseInt(issuedQtyNew) * item.uoms[2].conversion;
                }
                else if(item.uomTypeId == 2)
                {
                    var stripQty = parseInt(issuedQtyNew) * item.uoms[1].conversion * item.uoms[2].conversion;
                    issuedQty  = stripQty;     
                }
                else if(item.uomTypeId == 1)
                {
                    var boxQty = parseInt(issuedQtyNew) * item.uoms[0].conversion * item.uoms[1].conversion * item.uoms[2].conversion;
                    issuedQty  = boxQty;
                }
            }  
        }
        return issuedQty;
    }

    $scope.confirmMessage = "Are you sure you want to Save?";
    $scope.SavePopup = function () {
          if(self.model.patientPendingIndentDetailList.items !== undefined && self.model.patientPendingIndentDetailList.items !== null && self.model.patientPendingIndentDetailList.items.length>0){
              var checkLasa = false;
              angular.forEach(self.model.patientPendingIndentDetailList.items, function(item){
                    if(item.isLasa !== undefined && item.isLasa !== null && item.isLasa === true){
                       checkLasa = true;
                    }
              });

              if(checkLasa){
                   checkLasa = false;
                  $scope.confirmMessage = "List contains LASA drugs. Are you sure you want to Save?";
                       var popup = angular.element('#confirmSave');
                       popup.modal('show');
              }
              else{
                SavePendingIndent();
              }
          }

    }

      $scope.validateSubQty=function(){
        	/*var isValid=true;
        	var mainSum=0;
        	var subSum=0;
        	$scope.indentQty=0;
        	$scope.totSum=0;
        	angular.forEach(self.model.patientPendingIndentDetailList.items, function(value, key){
              $scope.indentQty+=value.indentQuantity
        		mainSum +=value.issueQuantity;
        		
        		angular.forEach(value.selectedItems, function(valueSub, keySub){
        			subSum+=valueSub.issueQuantity;
        		});
        		$scope.totSum=mainSum+subSum;
        		
        		if($scope.totSum > $scope.indentQty) {
    				growl.error("Please enter valid quantity.");
       			 isValid=false;
    			}
  			else{
  				isValid=true;
  			}
        	});
        	return isValid;
        }*/
    	  $scope.indentQty=0;
      	var isValid=true;
      	var mainSum=0;
      	var subSum=0;
      	$scope.indentQty=0;
      	$scope.totSum=0;
      	angular.forEach(self.model.patientPendingIndentDetailList.items, function(value, key){
      	
           if(value.parentIssueDetailId==undefined){
      		$scope.indentQty+=value.indentQuantity
      		mainSum +=value.issueQuantity;
      		
      		angular.forEach(value.selectedItems, function(valueSub, keySub){
      			if(valueSub.savedSubItem==false){
      				subSum+=valueSub.issueQuantity;
      			}
      			
      		});
      		$scope.totSum=mainSum+subSum;
      		
      		if($scope.totSum > $scope.indentQty) {
  				growl.error("Please enter valid quantity.");
     			 isValid=false;
                  $scope.saveBtnDisabled = false;
  			}
			else{
				isValid=true;
			}
      	}
      	});
      	return isValid;
      }

      $scope.subFlagChange = function()
      {

        self.model.patientPendingIndentDetailList.items.forEach(function (item,index) {
                if(self.model.selectedIndexxdd==index){
                            item.disableParentItem=false;
                            item.substititeFlag=false;
                            item.subFlagOnly=false;
                }
          });

          /*self.model.patientPendingIndentDetailList.items.forEach(function (item) {
              item.substititeFlag=false;
              item.disableParentItem=false;
              
          });*/
      }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("detailsPatientPendingIndent", {
            url: '/detailsPatientPendingIndent',
            templateUrl: 'views/PharmacyInventory/InPatient/details-patient-pending-Indent.html',
            controller: "DetailsPatientPendingIndent.Controller",
            controllerAs: "vm",
            title: 'Patient Consumable Issue'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("DetailsPatientPendingIndent.Controller", detailsPatientPendingIndentController)
})();
