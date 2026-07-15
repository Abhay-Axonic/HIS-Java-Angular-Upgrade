(function() {
	"use strict";

	function detailsCrashCartRequestApprovalController($scope, $cookies,
			$rootScope, $http, CONSTANTS, GenericService, $filter, growl,
			CurrencyService, StatusService, commonDetailService, GenericServiceParamHeader, $state,
			reportService) {
		var self = this;
		var discountLimit = 50;
		var cookieObjectAppConfig = {};
		[ cookieObjectAppConfig ] = $rootScope.checkCookies();

		var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
		$scope.taxInclusive = cookieObjectAppConfig.taxInclusive;

		var cookieObject = $cookies.getObject('cookieObject');
		$scope.storeId = cookieObject.storeId;

		// Default Constructor
		var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
		function initializeController() {
			$rootScope.loginpage = true;

			self.model = {
				isBatchValid : [],
				isUOMValid : [],
				isQuantityValid : [],
			};

			// Methods
			CurrencyService.GetCurrency().then(function(currency) {
				self.model.Currency = currency;
			})
			StatusService.GetStatus().then(function(status) {
				self.model.Status = status;
			})

			self.model.taxInclusive = cookieObjectAppConfig.taxInclusive;
			self.model.GetCrashCartRequestApprovalDetails = GetCrashCartRequestApprovalDetails;
			 self.model.getAllPatientDiagnosisForIcon = getAllPatientDiagnosisForIcon;
			self.model.BatchChange = BatchChange;
			self.model.onChange = onChange;
			self.model.AddItem = AddItem;
			self.model.RemoveItem = RemoveItem;
			self.model.ValidateBill = ValidateBill;
			self.model.SaveBill = SaveBill;
			self.model.SavePopup = SavePopup;
			self.model.NavigateToCrashCartRequestApproval = NavigateToCrashCartRequestApproval;
			self.model.ValidateUniqueBatchAndUom = ValidateUniqueBatchAndUom;
			self.model.printDetailsCrashCartApprovalReport = printDetailsCrashCartApprovalReport;

			GetCrashCartRequestApprovalDetails(commonDetailService.getDataId());
		}

		var SetStatus = function(x) {
			var status = self.model.Status.find(function(obj) {
				return obj.status == x;
			});

			return status.id;
		}

		// create by minesh for club batch if batchNo is same.
		var clubBatches = function(batches) {
			// added by minesh for total of availableQuantity from all batch and
			// get single Unique Batch
			var batchNo = [];
			var uniqueBatches = [];

			if (!!batches) {
				batches
						.forEach(function(batch) {
							var index = batchNo.indexOf(batch.batchNo);
							if (index === -1) {
								batchNo.push(batch.batchNo);
								uniqueBatches.push(batch);
							} else {
								uniqueBatches[index].availableStockQuantity += batch.availableStockQuantity;
							}
						});
			}
			return uniqueBatches;
			// added by minesh for total of availableQuantity from all batch and
			// get single Unique Batch
		}
		// end code by minesh for club batch if batchNo is same.

		var GetCrashCartRequestApprovalDetails = function(issueId) {
			self.model.printId = issueId;
			var URI = CONSTANTS.PHARMACY.NURSING.CRASH_CART_REQUEST_APPROVAL.DETAIL_API + $scope.unitId + '/'+$scope.organizationId + '/'+ issueId;
			$rootScope.startLoader();
			GenericService.serviceAction("GET", URI, {}).then(
			function(response) {
				self.model.crashCartRequestApprovalDetails = response.data;
				$rootScope.allergydata(response.data);
				getAllPatientDiagnosisForIcon(response.data.patientId);
				if (self.model.crashCartRequestApprovalDetails.items) {
					self.model.crashCartRequestApprovalDetails.items.forEach(function(item) {
						$scope.patientClassPercentage = item.patientClassPercentage;
						// added by minesh for calling
						// clubBatches function
						let clubBatchData = clubBatches(item.batch);
						item.batch = clubBatchData;
						// code end by minesh for
						// calling clubBatches function

						item.batch.unshift({
							"batchNo" : "Select Batch"
						});
						item.batchlist = item.batch;
						item.batch = [];
						angular.forEach(item.batchlist, function(b){
							if(b.availableStockQuantity != 0){
								item.batch.push(b);
							}
						})
						item.selectedBatch = item.batch.find(function(x) {
							return x.isSelected
						});
						if (item.selectedBatch) {
							item.selectedUom = item.selectedBatch.uom.find(function(obj) {
								return obj.uomTypeId == item.selectedBatch.uomTypeId;
							});
						}
						// initial it is undefined (stock based on batch selection) and after approved not undefined HIS-12159
						if(item.selectedBatch!=undefined)
						{
							item.availableqty = item.selectedBatch.availableStockQuantity;	
						}
						item.newRequiredQuantity = item.requiredQuantity;
						if(item.isThreeUOMApplicable == false){
							if(item.selectedBatch != undefined){
								if(item.selectedBatch.uomTypeId ==1){
									item.newAvailableqty = math.floor(item.availableqty/(item.selectedBatch.uom[0].conversion*item.selectedBatch.uom[1].conversion*item.selectedBatch.uom[2].conversion));
									item.receivedQtyCon = (item.approvedQuantity +"*"+ item.selectedBatch.uom[0].conversion);
									item.receivedQtyCalculation = (item.approvedQuantity * item.selectedBatch.uom[0].conversion);
									item.uomUnitData = item.selectedBatch.uom[2].uomUnit;
								}
								else if(item.selectedBatch.uomTypeId == 2){
									item.newAvailableqty = math.floor(item.availableqty/(item.selectedBatch.uom[1].conversion*item.selectedBatch.uom[2].conversion));
								}
								else{
									item.newAvailableqty = math.floor(item.availableqty/(item.selectedBatch.uom[2].conversion));
									item.receivedQtyCon = (item.approvedQuantity);
									item.receivedQtyCalculation = (item.approvedQuantity);
									item.uomUnitData = item.selectedBatch.uom[2].uomUnit;
								}
							}
						}
						else{
							if(item.selectedBatch != undefined){
								if(item.selectedBatch.uomTypeId ==1){
									item.newAvailableqty = math.floor(item.availableqty/(item.selectedBatch.uom[0].conversion*item.selectedBatch.uom[1].conversion*item.selectedBatch.uom[2].conversion));
									item.receivedQtyCon = (item.approvedQuantity +"*"+ item.selectedBatch.uom[0].conversion+"*"+ item.selectedBatch.uom[1].conversion);
									item.receivedQtyCalculation = (item.approvedQuantity * item.selectedBatch.uom[0].conversion * item.selectedBatch.uom[1].conversion);
									item.uomUnitData = item.selectedBatch.uom[2].uomUnit;
								}
								else if(item.selectedBatch.uomTypeId == 2){
									item.newAvailableqty = math.floor(item.availableqty/(item.selectedBatch.uom[1].conversion*item.selectedBatch.uom[2].conversion));
									item.receivedQtyCon = (item.approvedQuantity +"*"+ item.selectedBatch.uom[1].conversion);
									item.receivedQtyCalculation = (item.approvedQuantity * item.selectedBatch.uom[1].conversion);
									item.uomUnitData = item.selectedBatch.uom[2].uomUnit;
								}
								else{
									item.newAvailableqty = math.floor(item.availableqty/(item.selectedBatch.uom[2].conversion));
									item.receivedQtyCon = (item.approvedQuantity);
									item.receivedQtyCalculation = (item.approvedQuantity);
									item.uomUnitData = item.selectedBatch.uom[2].uomUnit;
								}
							}
						}
					});
				}
				$rootScope.stopLoader();
			},
			function(err) {
				$rootScope.stopLoader();
				growl.error(err.data.summary);
			});
		}

	      var getAllPatientDiagnosisForIcon= function(patientId){
	          self.model.patientDiagnosisListIcon =[];
	          var cookieObject = $cookies.getObject('cookieObject');
	           var headerObjDia = {
	                       'unit_id' : cookieObject.unitId,
	                      'organization_id' : cookieObject.organizationId,
	                      'patient_id' : patientId,
	                     'Content-Type':'application/json',
	           };
	           var data={};
	           var URI = CONSTANTS.ROOT_URL
	           + "/api/opd/getPatientDiagnosisByPatientIdForIcon";
	           GenericServiceParamHeader
	           .serviceAction(METHOD_GET, URI,
	              data,
	              NOTIFICATION_MSG_STATUS_FALSE,data,headerObjDia)
	           .then(
	                   function(response) {
	                       console.log(response);
	                       // $rootScope.startLoader();
	                       if (response.data.status == "success")
	                       self.model.patientDiagnosisListIcon = response.data.listObject; 
	                       console.log("patientDiagnosisListIcon",self.model.patientDiagnosisListIcon);
	                           self.model.comorbidityFlag = false;
	                           self.model.notifiableFlag = false;
	                          
	                              if(self.model.patientDiagnosisListIcon.length > 0)
	                                  {
	                                      self.model.complaintDelete=false;
	                                      angular.forEach(self.model.patientDiagnosisListIcon,function(value,key) {
	                                          if(value.isNotifiable == 'Y')
	                                          {
	                                              self.model.notifiableFlag = true;
	                                          }
	                                          else if(value.isComorbidity == 'Y')
	                                          {
	                                              self.model.comorbidityFlag = true;
	                                          }
	                                      });
	                                  }
	                       
	                   });
	          }
	      
		var onChange = function(data) {
			if(parseInt(data.quantity) > parseInt(data.requiredQuantity))
			{
				growl.error('Please enter quantity less than or equal to  required quantity');
				data.isQuantityValid=true;
			}
			else
			{
				data.isQuantityValid=false;
			}
			var data1 = {
				"mrp" : data.selectedBatch.rate,
				"isTaxInc" : self.model.taxInclusive,
				// "markup" : !!data.markUpDown ? data.markUpDownPer : null,
				// "markdown" : !data.markUpDown ? data.markUpDownPer : null,
				"markup": data.markUpDown,
                "markUpDownPer" : data.markUpDownPer,
				"discount" : data.discount == 0 || data.discount == undefined ? data.discountPerItem == 0
					|| data.discountPerItem == undefined ? data.discountPerItem
					: data.discount : 0,
				"isDiscPercent" : !!data.discountType && data.discountType == 1 ? true : false,
				"tax" : ((data.taxAmount != undefined || data.taxAmount != null) ? data.taxAmount: 0),
				"qty" : data.quantity,
				"patientClassPercentage" : $scope.patientClassPercentage,
				// "paymentEntitlementTypeId":
				// $scope.entitlementObj.paymentEntitlementTypeId
			}
			$scope.patientClassPercentage = data.patientClassPercentage;

			var response = commonDetailService.calculateAmount(data1);
			data.rate = response.rate;
			data.taxAmount = response.taxAmount;
			data.totalAmount = response.totalAmount;
			data.netAmount = response.netAmount;
			data.patientClassAmount = response.patientClassAmount;
			data.unitPatientClassAmount = response.unitPatientClassAmount;
			$scope.patientClassAmount = response.patientClassAmount;
			$scope.unitPatientClassAmount = response.unitPatientClassAmount;

			angular.forEach(self.model.crashCartRequestApprovalDetails.items,function(value, key) {
				if (value.itemId == data.itemId) {
					value.taxAmount = response.taxAmount;
					value.totalAmount = response.totalAmount;
					value.netAmount = response.netAmount;
				}
			});
		}

		var BatchChange = function(index, bill, batch) {
			var item = bill.items[index];

			item.uom = [ {
				uomDetails : "Select UOM",
			} ];

			if (!!batch.batchId) {
				item.uom = item.uom.concat(batch.uom);

				item.selectedUom = item.uom.find(function(obj) {
					return obj.opUom;
				});
			var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.AVAILABLE_QTY_API  + $scope.unitId + "/"+ bill.storeId + "/" + item.itemId + "/" + item.selectedBatch.batchId + "/" + item.selectedUom.uomTypeId;
           
/*               var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.AVAILABLE_QTY_API  + $scope.unitId + "/"+ $cookies.getObject('cookieObject').storeId + "/" + item.itemId + "/" + item.selectedBatch.batchId + "/" + item.selectedUom.uomTypeId;
*/				$rootScope.startLoader();
                GenericService.serviceAction("GET", URI, {}).then(
                    function (response) {
                        item.availableqty = response.data.availableqty;
						if(item.isThreeUOMApplicable == false){
							item.selectedBatch.uomListdata = item.selectedBatch.uom;
							item.selectedBatch.uom = [];
							angular.forEach(item.selectedBatch.uomListdata, function(data){
							  if(data.uomType != 'Stock'){
								item.selectedBatch.uom.push(data);
							  }
							});
						}
						if(item.isThreeUOMApplicable == false){
							if(item.quantity != undefined && item.quantity != null && item.quantity != 0){
								if(item.selectedUom.uomTypeId ==1){
									item.receivedQtyCon = (item.quantity +"*"+ item.uom[1].conversion);
									item.receivedQtyCalculation = (item.quantity * item.uom[1].conversion);
									item.uomUnitData = item.uom[3].uomUnit;
								}
								else if(item.selectedUom.uomTypeId == 2){
									
								}
								else{
									item.receivedQtyCon = (item.quantity);
									item.receivedQtyCalculation = (item.quantity);
									item.uomUnitData = item.uom[3].uomUnit;
								}
							}
							else{
								if(item.selectedUom.uomTypeId ==1){
									item.receivedQtyCon = (item.requiredQuantity +"*"+ item.uom[1].conversion);
									item.receivedQtyCalculation = (item.requiredQuantity * item.uom[1].conversion);
									item.uomUnitData = item.uom[3].uomUnit;
								}
								else if(item.selectedUom.uomTypeId == 2){
									
								}
								else{
									item.receivedQtyCon = (item.requiredQuantity);
									item.receivedQtyCalculation = (item.requiredQuantity);
									item.uomUnitData = item.uom[3].uomUnit;
								}
							}
						}
						else{
							if(item.quantity != undefined && item.quantity != null && item.quantity != 0){
								if(item.selectedUom.uomTypeId ==1){
									item.receivedQtyCon = (item.quantity +"*"+ item.uom[1].conversion+"*"+ item.uom[2].conversion);
									item.receivedQtyCalculation = (item.quantity * item.uom[1].conversion * item.uom[2].conversion);
									item.uomUnitData = item.uom[3].uomUnit;
								}
								else if(item.selectedUom.uomTypeId == 2){
									item.receivedQtyCon = (item.quantity +"*"+ item.uom[2].conversion);
									item.receivedQtyCalculation = (item.quantity * item.uom[2].conversion);
									item.uomUnitData = item.uom[3].uomUnit;
								}
								else{
									item.receivedQtyCon = (item.quantity);
									item.receivedQtyCalculation = (item.quantity);
									item.uomUnitData = item.uom[3].uomUnit;
								}
							}
							else{
								if(item.selectedUom.uomTypeId ==1){
									item.receivedQtyCon = (item.requiredQuantity +"*"+ item.uom[1].conversion+"*"+ item.uom[2].conversion);
									item.receivedQtyCalculation = (item.requiredQuantity * item.uom[1].conversion * item.uom[2].conversion);
									item.uomUnitData = item.uom[3].uomUnit;
								}
								else if(item.selectedUom.uomTypeId == 2){
									item.receivedQtyCon = (item.requiredQuantity +"*"+ item.uom[2].conversion);
									item.receivedQtyCalculation = (item.requiredQuantity * item.uom[2].conversion);
									item.uomUnitData = item.uom[3].uomUnit;
								}
								else{
									item.receivedQtyCon = (item.requiredQuantity);
									item.receivedQtyCalculation = (item.requiredQuantity);
									item.uomUnitData = item.uom[3].uomUnit;
								}
							}
						}
						$scope.convertQuantityOnUomChange(item);
                        $rootScope.stopLoader();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                    }
                );

			} else {
				item.selectedUom = item.uom[0];
				item.quantity = null;
			}
		}

		var AddItem = function(index, bill) {
			bill.items.splice(index + 1, 0, CopyItem(index, bill));
			// self.model.crashCartRequestApprovalDetails.items.splice(index +
			// 1, 0, CopyItem(index, bill));
		}

		var CopyItem = function(index, bill) {
			var item = bill.items[index];

			return {
				id : item.id,
				itemCode : item.itemCode,
				itemName : item.itemName,
				genericName : item.genericName,
				batch : item.batch,
				selectedBatch : item.batch[0],
				uom : item.uom,
				selectedUom : item.uom[0],
				requiredQuantity : item.requiredQuantity - parseInt(item.quantity==undefined ? 0:item.quantity),
				approvedQuantity : item.approvedQuantity,
				quantity : item.quantity,
				itemId : item.itemId,
				prodCatId : item.prodCatId,
				isThreeUOMApplicable:item.isThreeUOMApplicable,
				uomTypeId:item.uomTypeId
			}
		}

		var RemoveItem = function(index, bill) {
			var item = bill.items[index];
			bill.items.splice(index, 1);
		}

		var SavePopup = function() {
			var popup = angular.element('#confirmSave');
			popup.modal('show');
		}
		$scope.saveBtnDisabled=false;
		var SaveBill = function(bill) {
			$scope.saveBtnDisabled=true;
			if (ValidateBill(bill)) {
				var URI = CONSTANTS.PHARMACY.NURSING.CRASH_CART_REQUEST_APPROVAL.SAVE_API;
				var data = {
					"id" : self.model.crashCartRequestApprovalDetails.id,
					"visitTypeId" : self.model.crashCartRequestApprovalDetails.visitTypeId,
					"patientId" : self.model.crashCartRequestApprovalDetails.patientId,
					"admissionId" : !!self.model.crashCartRequestApprovalDetails.admissionId ? self.model.crashCartRequestApprovalDetails.admissionId
							: undefined,
					"encounterId" : !!self.model.crashCartRequestApprovalDetails.encounterId ? self.model.crashCartRequestApprovalDetails.encounterId
							: undefined,
					//"storeId" : $scope.storeId,
					//changed storeId code for resolved HIS-10354 Crash Cart Request Approval - 
					"storeId" : self.model.crashCartRequestApprovalDetails.storeId,
					"statusId" : SetStatus('Approved'),
					"items" : [],
					"unitId":$scope.unitId,
		            "organizationId":$scope.organizationId,
		            "userId":$scope.userId,
//		            "storeId":$scope.storeId
				}
				angular
						.forEach(
								bill.items,
								function(item, key) {
									if (!item.disabled) {
										var billItem = {
											"id" : item.id,
											"batchId" : item.selectedBatch.batchId,
											"uomTypeId" : item.selectedUom.uomTypeId,
											"uomUnitId" : item.selectedUom.uomUnitId,
											"consumedQuantity" : Number(item.quantity),
											"isBatchReq" : item.isBatchRequired,
											"statusId" : SetStatus('Pending'),
											"leaseQuantity" : item.leaseQuantity,
											"netAmount" : item.netAmount ? item.netAmount
													: 0,
											"quantity" : parseInt(item.quantity),
											"rate" : item.rate ? item.rate : 0,
											"taxAmount" : item.taxAmount ? item.taxAmount
													: 0,
											"taxId" : item.taxId ? item.taxId
													: 0,
											"taxPercentage" : item.taxPercentage ? item.taxPercentage
													: 0,
											"totalAmount" : item.totalAmount ? item.totalAmount
													: 0,
											"productCategoryId" : item.prodCatId,
											"itemId" : item.itemId,
											"creditPayable" : 0,
											"selfPayable" : item.netAmount ? item.netAmount
													: 0,
											"consessionPercentage" : $scope.patientClassPercentage,
											"consessionAmount" : $scope.patientClassAmount,
											"unitConsessionAmount" : $scope.unitPatientClassAmount
													.toFixed(2)
										}
										data.items.push(billItem);
									}
								});
				console.log(data)

				$rootScope.startLoader();
				GenericService
						.serviceAction("POST", URI, data)
						.then(
								function(response) {
									ClearFields(bill);
									growl.success('Crash cart request approval Successfully saved');
									NavigateToCrashCartRequestApproval();
									$rootScope.stopLoader();
								},
								function(err) {
									$rootScope.stopLoader();
									//growl.error('Error while saving crash cart request approval');
									//added below if-else code for resolved HIS-10354 Crash Cart Request Approval - 
									if(err.data.message!=undefined && err.data.code == "HIS-1682" ){										
										growl.error(err.data.message);
										$scope.saveBtnDisabled=false;
										var popup = angular.element('#confirmSave');
										popup.modal('hide');
									} else {
										growl.error('Error while saving crash cart request approval');
									}
								});
			}
		}

		var ClearFields = function(bill) {

			bill.Id = "1" + (new Date()).getTime();
			bill.uhid = null;
			bill.patientDetail = null;
			bill.wardName = null;
			bill.pdd = null;
			bill.doa = null;
			bill.bedNo = null;
			bill.prescriptionDate = null;
			bill.doctorDetail = null;
			bill.items = [];
		}

		var ValidateBill = function(bill) {

			var isValid = true;

			if (!bill.items || bill.items.length <= 0) {
				isValid = false;
				growl.error("Please add some items in the bill");
			} else {
				bill.items.forEach(function(billitem) {
					billitem['isItemValid'] = false;
					billitem['isBatchValid'] = false;
					billitem['isUOMValid'] = false;
					billitem['isQuantityValid'] = false;

					if (!billitem.selectedBatch.batchId
							|| !billitem.selectedUom.uomUnitId
							|| !billitem.quantity
							|| (billitem.approvedQuantity < billitem.quantity)) {

						billitem['isItemValid'] = true;
						isValid = false;
					}

					if (!billitem.selectedBatch.batchId) {
						billitem['isBatchValid'] = true;
						isValid = false;
					}

					if (!billitem.selectedUom.uomUnitId) {
						billitem['isUOMValid'] = true;
						isValid = false;
					}

					if (!billitem.quantity) {

						billitem['isQuantityValid'] = true;
						isValid = false;
						growl.error('Enter valid quantity')
					}

					if (billitem.approvedQuantity < billitem.quantity) {

						billitem['isQuantityValid'] = true;
						isValid = false;
						growl.error('Enter quantity less than approved quantity')
					}

					if (!ValidateUniqueBatchAndUom(billitem)) {
						isValid = false;
					}
					if(parseInt(billitem.quantity) > parseInt(billitem.requiredQuantity))
					{
						growl.error('Please enter quantity less than or equal to  required quantity');
						billitem.isQuantityValid=true;
						isValid = false;
					}

					if(billitem.availableqty == 0 || billitem.availableqty == undefined){
						growl.error("Available quantity not available");
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

		var ValidateUniqueBatchAndUom = function(item) {
			var isValid = true;

			var items = self.model.crashCartRequestApprovalDetails.items
			.filter(function(detail) {
				return detail.itemCode == item.itemCode
				&& item.selectedBatch
				&& item.selectedUom
				&& item.selectedUom.uomTypeId > 0
				&& item.selectedBatch.batchId > 0
				&& detail.selectedBatch.batchId == item.selectedBatch.batchId
				&& detail.selectedUom.uomTypeId == item.selectedUom.uomTypeId;
			});

			angular.forEach(items, function(d){
				if(d.isThreeUOMApplicable == false){
					if(d.selectedBatch != undefined){
						if(item.quantity != undefined && item.quantity != null && item.quantity != 0){
							if(d.selectedUom.uomTypeId ==1){
								d.receivedQtyCon = (d.quantity +"*"+ d.selectedBatch.uom[0].conversion);
								d.receivedQtyCalculation = (d.quantity * d.selectedBatch.uom[0].conversion);
								d.uomUnitData = d.selectedBatch.uom[1].uomUnit;
							}
							else if(d.selectedUom.uomTypeId == 2){
								
							}
							else{
								d.receivedQtyCon = (d.quantity);
								d.receivedQtyCalculation = (d.quantity);
								d.uomUnitData = d.selectedBatch.uom[1].uomUnit;
							}
						}
						else{
							if(d.selectedUom.uomTypeId ==1){
								d.receivedQtyCon = (d.newRequiredQuantity +"*"+ d.selectedBatch.uom[0].conversion);
								d.receivedQtyCalculation = (d.newRequiredQuantity * d.selectedBatch.uom[0].conversion);
								d.uomUnitData = d.selectedBatch.uom[1].uomUnit;
							}
							else if(d.selectedUom.uomTypeId == 2){
								
							}
							else{
								d.receivedQtyCon = (d.newRequiredQuantity);
								d.receivedQtyCalculation = (d.newRequiredQuantity);
								d.uomUnitData = d.selectedBatch.uom[1].uomUnit;
							}
						}
					}
				}
				else{
					if(d.selectedBatch != undefined){
						if(item.quantity != undefined && item.quantity != null && item.quantity != 0){
							if(d.selectedUom.uomTypeId ==1){
								d.receivedQtyCon = (d.quantity +"*"+ d.selectedBatch.uom[0].conversion+"*"+ d.selectedBatch.uom[1].conversion);
								d.receivedQtyCalculation = (d.quantity * d.selectedBatch.uom[0].conversion * d.selectedBatch.uom[1].conversion);
								d.uomUnitData = d.selectedBatch.uom[2].uomUnit;
							}
							else if(item.selectedUom.uomTypeId == 2){
								d.receivedQtyCon = (d.quantity +"*"+ d.selectedBatch.uom[1].conversion);
								d.receivedQtyCalculation = (d.quantity * d.selectedBatch.uom[1].conversion);
								d.uomUnitData = d.selectedBatch.uom[2].uomUnit;
							}
							else{
								d.receivedQtyCon = (d.quantity);
								d.receivedQtyCalculation = (d.quantity);
								d.uomUnitData = d.selectedBatch.uom[2].uomUnit;
							}
						}
						else{
							if(d.selectedUom.uomTypeId ==1){
								d.receivedQtyCon = (d.newRequiredQuantity +"*"+ d.selectedBatch.uom[0].conversion+"*"+ d.selectedBatch.uom[1].conversion);
								d.receivedQtyCalculation = (d.newRequiredQuantity * d.selectedBatch.uom[0].conversion * d.selectedBatch.uom[1].conversion);
								d.uomUnitData = d.selectedBatch.uom[2].uomUnit;
							}
							else if(item.selectedUom.uomTypeId == 2){
								d.receivedQtyCon = (d.newRequiredQuantity +"*"+ d.selectedBatch.uom[1].conversion);
								d.receivedQtyCalculation = (d.newRequiredQuantity * d.selectedBatch.uom[1].conversion);
								d.uomUnitData = d.selectedBatch.uom[2].uomUnit;
							}
							else{
								d.receivedQtyCon = (d.newRequiredQuantity);
								d.receivedQtyCalculation = (d.newRequiredQuantity);
								d.uomUnitData = d.selectedBatch.uom[2].uomUnit;
							}
						}
					}
				}
			});

			if (items.length > 1) {
				isValid = false;
				item['isBatchValid'] = true;
				item['isUOMValid'] = true;
				growl.error('There should not be same batch and same UOM for same item');
			} else {

			}
			return isValid;
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

		var NavigateToCrashCartRequestApproval = function() {
			document.getElementById('popupClose').click();
			setTimeout(function() {
				$state.go('crashCartRequestApproval');
			}, 1000);
		}

		/** *Chetana* */
		var printDetailsCrashCartApprovalReport = function() {
			var data = {
				"appName" : "HIS",
				"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
				"reportDesignFilePath" : "get_Crash_Cart_Request.rpt",
				"reportFormat" : "PDF",
				"exportFileName" : "CrashCartRequestApproval",
				"reportParams" : [ {
					"paramName" : "id",
					"paramType" : "Integer",
					"paramValue" : self.model.printId
				} ]
			};
			reportService.generate(REPORT_API_URI1, data);
		}
		/** ** */
		$scope.convertQuantityOnUomChange = function(item){
			item.selectedBatch.uomListdata = item.selectedBatch.uom;
			if(item.isThreeUOMApplicable == false){
				item.selectedBatch.uom = [];
				angular.forEach(item.selectedBatch.uomListdata, function(data){
				  if(data.uomType != 'Stock'){
					item.selectedBatch.uom.push(data);
				  }
				});
			}
			if(item.isThreeUOMApplicable==true)
			{
				if(item.uomTypeId==1)
				{
				item.requiredQuantityBeforeChange=item.requiredQuantity*(item.selectedBatch.uomListdata[0].conversion*item.selectedBatch.uomListdata[1].conversion*item.selectedBatch.uomListdata[2].conversion);
				}
				if(item.uomTypeId==2)
				{
				item.requiredQuantityBeforeChange=item.requiredQuantity*(item.selectedBatch.uomListdata[1].conversion*item.selectedBatch.uomListdata[2].conversion);
				}
				if(item.uomTypeId==3)
				{
				item.requiredQuantityBeforeChange=item.requiredQuantity*(item.selectedBatch.uomListdata[2].conversion);
				}
			}
			else
				{
				if(item.uomTypeId==1)
				{
				item.requiredQuantityBeforeChange=item.requiredQuantity*(item.selectedBatch.uom[0].conversion*item.selectedBatch.uom[1].conversion);
				}
				if(item.uomTypeId==3)
				{
				item.requiredQuantityBeforeChange=item.requiredQuantity*(item.selectedBatch.uom[1].conversion);
				}
				}
			if(item.isThreeUOMApplicable==true)
				{
				if(item.selectedUom.uomTypeId==1)
					{
					item.newRequiredQuantity=math.floor(item.requiredQuantityBeforeChange/(item.selectedBatch.uomListdata[0].conversion*item.selectedBatch.uomListdata[1].conversion*item.selectedBatch.uomListdata[2].conversion));
					item.newAvailableqty=math.floor(item.availableqty/(item.selectedBatch.uomListdata[0].conversion*item.selectedBatch.uomListdata[1].conversion*item.selectedBatch.uomListdata[2].conversion));
					}
				if(item.selectedUom.uomTypeId==2)
				{
				item.newRequiredQuantity=math.floor(item.requiredQuantityBeforeChange/(item.selectedBatch.uomListdata[1].conversion*item.selectedBatch.uomListdata[2].conversion));
				item.newAvailableqty=math.floor(item.availableqty/(item.selectedBatch.uomListdata[1].conversion*item.selectedBatch.uomListdata[2].conversion));
				}
				if(item.selectedUom.uomTypeId==3)
				{
				item.newRequiredQuantity=math.floor(item.requiredQuantityBeforeChange/(item.selectedBatch.uomListdata[2].conversion));
				item.newAvailableqty=math.floor(item.availableqty/(item.selectedBatch.uomListdata[2].conversion));
				}
				}
			else
			{
			if(item.selectedUom.uomTypeId==1)
				{
				item.newRequiredQuantity=math.floor(item.requiredQuantityBeforeChange/(item.selectedBatch.uom[0].conversion*item.selectedBatch.uom[1].conversion));
				item.newAvailableqty=math.floor(item.availableqty/(item.selectedBatch.uom[0].conversion*item.selectedBatch.uom[1].conversion));
				}
				if(item.selectedUom.uomTypeId==3)
				{
				item.newRequiredQuantity=math.floor(item.requiredQuantityBeforeChange/(item.selectedBatch.uom[1].conversion));
				item.newAvailableqty=math.floor(item.availableqty/(item.selectedBatch.uom[1].conversion));
				}
			}
		}
		$scope.calculateReceivedQty = function(item){
			if(item.quantity == undefined || item.quantity == null || item.quantity == ""){
				item.quantity = 0;
			}
			if(item.isThreeUOMApplicable == false){
				if(item.selectedUom.uomTypeId ==1){
					item.receivedQtyCon = (item.quantity +"*"+ item.uom[1].conversion);
					item.receivedQtyCalculation = (item.quantity * item.uom[1].conversion);
					item.uomUnitData = item.uom[3].uomUnit;
				}
				else if(item.selectedUom.uomTypeId == 2){
					
				}
				else{
					item.receivedQtyCon = (item.quantity);
					item.receivedQtyCalculation = (item.quantity);
					item.uomUnitData = item.uom[3].uomUnit;
				}
			}
			else{
				if(item.selectedUom.uomTypeId ==1){
					item.receivedQtyCon = (item.quantity +"*"+ item.uom[1].conversion+"*"+ item.uom[2].conversion);
					item.receivedQtyCalculation = (item.quantity * item.uom[1].conversion * item.uom[2].conversion);
					item.uomUnitData = item.uom[3].uomUnit;
				}
				else if(item.selectedUom.uomTypeId == 2){
					item.receivedQtyCon = (item.quantity +"*"+ item.uom[2].conversion);
					item.receivedQtyCalculation = (item.quantity * item.uom[2].conversion);
					item.uomUnitData = item.uom[3].uomUnit;
				}
				else{
					item.receivedQtyCon = (item.quantity);
					item.receivedQtyCalculation = (item.quantity);
					item.uomUnitData = item.uom[3].uomUnit;
				}
			}
		}

		initializeController();
	}

	function config($stateProvider) {
		$stateProvider
				.state(
						"detailsCrashCartRequestApproval",
						{
							url : "/detailsCrashCartRequestApproval",
							templateUrl : 'views/PharmacyInventory/InPatient/details-crash-cart-request-approval.html',
							controller : "DetailsCrashCartRequestApproval.Controller",
							controllerAs : "vm",
							title : 'Crash Cart Request Approval'
						});
	}

	angular.module("myApp").config(config).controller(
			"DetailsCrashCartRequestApproval.Controller",
			detailsCrashCartRequestApprovalController)
})();