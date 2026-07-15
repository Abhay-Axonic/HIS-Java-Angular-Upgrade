(function() {
	"use strict";

	function detailsPhysicalInspectionController($scope, $rootScope, $http,
			$filter, $state, $cookies, CONSTANTS, GenericService, commonDetailService,
			cancelReasonService, StatusService, growl) {
		var self = this;
		var rejectData;
		  var cookieObject = $cookies.getObject('cookieObject');

   $scope.grnBtn=false;
   $scope.grnBonusBtn=false;
		// Default Constructor
		var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
			$rootScope.loginpage = true;

			self.model = {
				selectAllRows : false,
				selectedItemId : 0,
				storageDetailsList : [],
				SelectedOrderIds : [],
				selectedItems : [],
				selectedStorageIndex : null,
				selectItemId : null,
				inspectId : null,
				storeList : [ {
					"store" : "Select Store"
				} ],
				statuses : [ {
					"status" : "All"
				} ],
				itemStorageDetails : [],
		        bonusBatchExpiryData:[],
		        bonusSelectedItemBatchExpiryIndex:0,
		        bonusDateOpen:[],
			};

			cancelReasonService.GetReason().then(function(reason) {
				self.model.reasons = reason;
			});

			StatusService.GetStatus().then(function(status) {
				self.model.statusList = status;
			});

			// Methods
			self.model.SelectAllOrders = SelectAllOrders;
			self.model.SelectItem = SelectItem;
			self.model.isDisable = isDisable;
			self.model.OpenPopup = OpenPopup;
			self.model.ModalDismiss = ModalDismiss;
			self.model.RejectPI = RejectPI;
			self.model.RejectPI1 = RejectPI1;
			// self.model.RejectPI2 = RejectPI2;
			self.model.SaveAcceptReject = SaveAcceptReject;
			self.model.SaveApproval = SaveApproval;
			self.model.validate = validate;
			self.model.SaveRejection = SaveRejection;
			self.model.SaveRejection1 = SaveRejection1;
			self.model.selectedApproveRejectList = [];
			self.model.NavigateToBack = NavigateToBack;

			self.model.DetailsClick = DetailsClick;
			self.model.ChangeRack = ChangeRack;
			self.model.AddRack = AddRack;
			self.model.lessRack = lessRack;
			self.model.SaveStorageDetails = SaveStorageDetails;
			self.model.ChangeSelf = ChangeSelf;
			self.model.ViewStorageDetails = ViewStorageDetails;
			self.model.ValidateApprovedQuantity = ValidateApprovedQuantity;
			self.model.ModalDismissRejectionPopup = ModalDismissRejectionPopup;
			// self.model.bonusSaveApproval = bonusSaveApproval;
			self.model.ValidateBonusApprovedQuantity = ValidateBonusApprovedQuantity;
		      self.model.bonusBatchExpiryInfoOnClick=bonusBatchExpiryInfoOnClick;
		      self.model.bonusBatchExpiryModalOnClick=bonusBatchExpiryModalOnClick;

			GetPhysicalInspectionData();
		}

		var ViewStorageDetails = function(rack, selfName) {
			self.model.itemStorageDetails = selfName.items;
		}

		var ChangeSelf = function(rackData, rackShelf) {
			self.model.itemselfName = rackShelf.shelf;
			// self.model.itemStorageDetails = rackShelf.items;
		}
		var SaveStorageDetails = function() {
			// self.model.physicalInspectionData.items.forEach(function (item) {
			// if (self.model.selectItemId == item.id) {
			// self.model.storageDetailsList.forEach(function (x) {
			// item["storageDetails"].push({ rackId: x.rackName.rackId, shelfId:
			// x.selfName.id, remark: x.remark });
			// });
			// }
			// });
			// console.log(self.model.physicalInspectionData.items);
			self.model.physicalInspectionData.items[self.model.storageIndex].storageDetails = [];
			self.model.storageDetailsList
					.forEach(function(x) {
						// self.model.physicalInspectionData.items[self.model.storageIndex].storageDetails.push({
						// rackId: x.rackName.rackId, rackName: x.rackName,
						// shelfId: x.selfName.id, selfName: x.selfName, remark:
						// x.remark });

						if (x.rackName.length > 0 && x.selfName.length > 0) {
							self.model.physicalInspectionData.items[self.model.storageIndex].storageDetails
									.push({
										rackId : x.rackName.rackId,
										rackName : x.rackName,
										shelfId : x.selfName.id,
										selfName : x.selfName,
										remark : x.remark
									});

						} else {
							if (x.rackName.rackId > 0 && x.selfName.length == 0) {
								growl.error("Please select self");
							} else  if(x.rackName.rackId !=undefined &&  x.selfName.id !=undefined){
								self.model.physicalInspectionData.items[self.model.storageIndex].storageDetails
										.push({
											rackId : x.rackName.rackId,
											rackName : x.rackName,
											shelfId : x.selfName.id,
											selfName : x.selfName,
											remark : x.remark
										});

							}

						}
					});
			console.log(self.model.physicalInspectionData.items);
		}

		var ChangeRack = function(rackName) {
			rackName.shelfs = rackName.shelfs;
			self.model.itemRackName = rackName.rack;
		}

		var DetailsClick = function(item, index) {
			console.log("Index : ", index);
			// item.storageDetails = [];
			self.model.selectItemId = item.id;
			self.model.storageIndex = index;
			if (item.storageDetails.length != 0) {
				self.model.storageDetailsList = self.model.physicalInspectionData.items[self.model.storageIndex].storageDetails;
				self.model.storageDetailsList.forEach(function(data) {
					var rack = self.model.rackList.find(function(rackData) {
						return data.rackName.rackId == rackData.rackId;
					});
					var shelf = rack.shelfs.find(function(shelfData) {
						return data.selfName.id == shelfData.id;
					});
				});
			} else {
				self.model.storageDetailsList.forEach(function(item) {
					item.rackName = [], item.selfName = [], item.remark = "",
							item.rackId = "", item.shelfId = ""
				});
			}
		}

		var AddRack = function(idx) {
			self.model.storageDetailsList.push({
				rackId : '',
				shelfId : '',
				remark : ''
			});
		}

		var lessRack = function(idx) {
			self.model.storageDetailsList.splice(idx, 1);
		}

		var setStatus = function(x) {
			var status = self.model.statusList.find(function(obj) {
				return obj.status == x;
			});

			return status.id;
		}

		var GetPhysicalInspectionData = function() {
			var detailsData = commonDetailService.getDataId();
			self.model.inspectId = detailsData.id;
			var URI = CONSTANTS.PROCUREMENT.QUOTATION.PHYSICAL_INSPECTION.DETAILS_API;
			var param = {
				"id" : detailsData.id,
				"storeId" : detailsData.storeId,
				"unitId": $scope.unitId
			}
			$rootScope.startLoader();
			$scope.isGrnBonusBatchExpiry=false;
			GenericService.serviceAction("POST", URI, param).then(
					function(response) {
						self.model.physicalInspectionData = response.data;
						self.model.remark = response.data.remark;
						self.model.requestItems = JSON.parse(JSON
								.stringify(response.data));
						self.model.physicalInspectionData.items
								.forEach(function(item) {
									if(item.grnBonusBatchExpiry.length==0)
						            {
						            	$scope.isGrnBonusBatchExpiry=true;
						            }
						            item.grnBonusBatchExpiryData=item.grnBonusBatchExpiry;
									item.isDisabled = true;
									item.storageDetails = [];
								});
						self.model.rackList = response.data.racks;
						self.model.rejectReasons = self.model.reasons;
						// todo:
						self.model.storageDetailsList = [ {
							rackId : '',
							shelfId : '',
							remark : ''
						} ]
						$rootScope.stopLoader();
					}, function(err) {
						$rootScope.stopLoader();
						growl.error(err.data.message);
					});
		}

		var ValidateApprovedQuantity = function(item) {
			var isValid = true;
			if (item.approvedQuantity > (item.grnApprovedQuantity + item.bonusApprovedQuantity)) {
				item.isApprovedQtyValid = true;
				growl
						.error("Approved quantity should not greater than Received quantity");
				isValid = false;
			}
			return isValid;
		}
		
		$scope.bonusbatchList = [];
	    var bonusBatchExpiryInfoOnClick = function (batchItem, index) {
	        self.model.bonusSelectedItemBatchExpiryIndex = index;
	        var sumOfBonusReceivedQuantity=0;
	        $scope.bonusbatchList = [];
			$scope.bonusbatchList = batchItem.grnBonusBatchExpiry;
	        if(batchItem.grnBonusBatchExpiryData!=undefined && batchItem.grnBonusBatchExpiryData!=null && batchItem.grnBonusBatchExpiryData.length>0)
	            {
	        angular.forEach(batchItem.grnBonusBatchExpiryData, function (grnBonusBatchExpiryCount) {
	            sumOfBonusReceivedQuantity=sumOfBonusReceivedQuantity+parseInt(grnBonusBatchExpiryCount.bonusQuantity);
	            self.model.bonusBatchExpiryData.push({
	                grnBonusBatchExpiryId:grnBonusBatchExpiryCount.grnBonusBatchExpiryId,
	                batchNo: grnBonusBatchExpiryCount.batchNo,
	                expiryDate: grnBonusBatchExpiryCount.expiryDate,
	                bonusQuantity: grnBonusBatchExpiryCount.bonusQuantity
	            });
	          });
	        batchItem.bonusReceivedQuantity=sumOfBonusReceivedQuantity;
	            }
	        else
	            {
	            sumOfBonusReceivedQuantity=0;
	            self.model.bonusBatchExpiryData.push({
	                grnBonusBatchExpiryId:null,
	                batchNo: "",
	                expiryDate: "",
	                bonusQuantity: ""
	            });
	            }
	        
	    }
	    
	    $scope.checkQty = function(bonus){
	    	
	    	if(bonus.bonusQuantity != undefined || bonus.bonusQuantity != "" ){
	    		var qty =0;
	    		 angular.forEach( $scope.bonusbatchList, function (info) {
	    			 if(bonus.grnBonusBatchExpiryId  ==info.grnBonusBatchExpiryId ){
	    				 qty = info.bonusQuantity;
	    			 }
	 	           
	 	          });
	    		 
	    		 if(bonus.bonusQuantity > qty){
	    			 growl.error("Bonus qty should not more than approved qty."); 
	    			 bonus.bonusQuantity = qty;
	    		 }
	    	}else{
	    		bonus.bonusQuantity =0;
	    	}
	    	
	    }
	    
	    
	    var bonusBatchExpiryModalOnClick = function () {
	    	self.model.physicalInspectionData.items[self.model.bonusSelectedItemBatchExpiryIndex].grnBonusBatchExpiryData = [];
	        var modal_list = self.model.bonusBatchExpiryData;
	        modal_list.forEach(function (modal, index) {
	        	self.model.physicalInspectionData.items[self.model.bonusSelectedItemBatchExpiryIndex].grnBonusBatchExpiryData.push({
	                grnBonusBatchExpiryId:modal.grnBonusBatchExpiryId!=null ? modal.grnBonusBatchExpiryId :null ,
	                batchNo: modal.batchNo,
	                expiryDate: modal.expiryDate,
	                bonusQuantity: modal.bonusQuantity
	            });
	          });
	        var sumOfBonusReceivedQuantity=0;
	        if(self.model.physicalInspectionData.items[self.model.bonusSelectedItemBatchExpiryIndex].grnBonusBatchExpiryData!=undefined && self.model.physicalInspectionData.items[self.model.bonusSelectedItemBatchExpiryIndex].grnBonusBatchExpiryData!=null && self.model.physicalInspectionData.items[self.model.bonusSelectedItemBatchExpiryIndex].grnBonusBatchExpiryData.length>0)
	            {
	        angular.forEach(self.model.physicalInspectionData.items[self.model.bonusSelectedItemBatchExpiryIndex].grnBonusBatchExpiryData, function (grnBonusBatchExpiryCount) {
	            sumOfBonusReceivedQuantity=sumOfBonusReceivedQuantity+parseInt(grnBonusBatchExpiryCount.bonusQuantity);
	          });
	            }
	        self.model.physicalInspectionData.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusReceivedQuantity=sumOfBonusReceivedQuantity;
	        self.model.physicalInspectionData.items[self.model.bonusSelectedItemBatchExpiryIndex].bonusApprovedQuantity=sumOfBonusReceivedQuantity;
	        self.model.bonusBatchExpiryData=[];
	        $('#bouns_selectBatchExpiry_modal').modal('hide');
	    }
	    $scope.closeBonusBatchExpiry =function()
	    {
	    	self.model.bonusBatchExpiryData=[];
	    }
		var ValidateBonusApprovedQuantity = function(item) {
			var isValid = true;
			if (item.bonusApprovedQuantity > (item.grnBonusApprovedQuantity)) {
				item.isBonusApprovedQtyValid = true;
				growl .error("Bonus Approved quantity should not greater than Bonus Received quantity");
				isValid = false;
			}
			return isValid;
		}

		var NavigateToBack = function() {
			$state.go('physicalInspec');
		}

		var OpenPopup = function(action, popupName, data) {
			self.model.action = action;
			var popup = angular.element('#' + popupName);
			self.model.id = data.id;
			popup.modal('show');
		}

		var RejectPI = function(action, popupName, data) {
			 $scope.grnBtn=true;
			self.model.onclose = data;
			self.model.selectedItemId = data.id;
			if (data.isReject) {
				self.model.action = action;
				var popup = angular.element('#' + popupName);
				self.model.rejectReasonId = self.model.reasons[0].id;
				if (data.rejectQuantity == undefined) {
					data.rejectQuantity = 0;
				}
				// self.model.rejectQty = data.approvedQuantity -
				// data.rejectQuantity;
				popup.modal('show');
			} else {
				CancelRejection1(data);
				return;
			}
		}

		var RejectPI1 = function(action, popupName, data) {
			self.model.onclose = data;
			self.model.selectedItemId = data.id;
			if (data.isReject) {
				self.model.action = action;
				var popup = angular.element('#' + popupName);
				self.model.rejectReasonId = self.model.reasons[0].id;
				if (data.rejectQuantity == undefined) {
					data.rejectQuantity = 0;
				}
				// self.model.rejectQty = data.approvedQuantity -
				// data.rejectQuantity;
				popup.modal('show');
			} else {
				CancelRejection1(data);
				return;
			}
		}
    //  var RejectPI2 = function(action, popupName, data) {
	// 		self.model.onclose = data;
	// 		self.model.selectedItemId = data.id;
	// 		if (data.isReject) {
	// 			self.model.action = action;
	// 			var popup = angular.element('#' + popupName);
	// 			self.model.rejectReasonId = self.model.reasons[0].id;
	// 			if (data.rejectQuantity == undefined) {
	// 				data.rejectQuantity = 0;
	// 			}
	// 			// self.model.rejectQty = data.approvedQuantity -
	// 			// data.rejectQuantity;
	// 			popup.modal('show');
	// 		} else {
	// 			CancelRejection2(data);
	// 			return;
	// 		}
	// 	}

		var validate = function(item){
			var isValid = true;
			if(item.approvedQuantity > item.grnApprovedQuantity){
				growl.error("Approved quantity is greater than received quantity");
				isValid = false;
				item.approvedQuantity = 0;
			}
			return isValid;
		}

		var SaveApproval = function(item) {

          $scope.grnBtn=true;
         $scope.grnBonusBtn=false;

			var totalGrnApprovedQuantity = item.grnApprovedQuantity;
			var approvedQuantity = !item.approvedQuantity
					|| isNaN(item.approvedQuantity) ? 0
					: parseInt(item.approvedQuantity);
			if (approvedQuantity > totalGrnApprovedQuantity) {
				item.rejectQuantity = 0;
				self.model.rejectQty = 0;
				item.rejectReasonId = self.model.reasons[0].id;
				item.isReject = false;
			} else if (approvedQuantity == 0) {
				item.approvedQuantity = 0
				item.rejectQuantity = totalGrnApprovedQuantity;
				self.model.rejectQty = totalGrnApprovedQuantity;
				item.rejectReasonId = self.model.reasons[0].id;
				item.isReject = true;
				RejectPI1('Open', 'qtyaccepatReject', item);
			} else if (approvedQuantity < totalGrnApprovedQuantity) {
				item.rejectQuantity = totalGrnApprovedQuantity
						- approvedQuantity;
				self.model.rejectQty = item.rejectQuantity;
				item.rejectReasonId = self.model.reasons[0].id;
				item.isReject = true;
				RejectPI1('Open', 'qtyaccepatReject', item);
			} else if (approvedQuantity == totalGrnApprovedQuantity) {
				item.rejectQuantity =  0;
				self.model.rejectQty = item.rejectQuantity;
				item.rejectReasonId = self.model.reasons[0].id;
				item.isReject = false;
				RejectPI1('Open', 'qtyaccepatReject', item);
			}
		}
		// var bonusSaveApproval = function(item) {
		// 	  $scope.grnBtn=false;
        //  $scope.grnBonusBtn=true;
		// 	var totalGrnApprovedQuantity =   item.grnBonusApprovedQuantity;
		// 	var approvedQuantity = !item.bonusApprovedQuantity
		// 			|| isNaN(item.bonusApprovedQuantity) ? 0
		// 			: parseInt(item.bonusApprovedQuantity);
		// 	if (approvedQuantity > totalGrnApprovedQuantity) {
		//     	item.rejectQuantity =  item.rejectQuantity!=undefined ||item.rejectQuantity > 0 || item.rejectQuantity!=null ?   item.rejectQuantity + ( totalGrnApprovedQuantity - approvedQuantity) :  totalGrnApprovedQuantity - approvedQuantity;
				
		// 		self.model.rejectQty = 0;
		// 		item.rejectReasonId = self.model.reasons[0].id;
		// 		item.isReject = false;
		// 	} else if (approvedQuantity == 0) {
		// 	//	item.approvedQuantity = 0
		// 	item.rejectQuantity =  item.rejectQuantity!=undefined ||item.rejectQuantity > 0 || item.rejectQuantity!=null ?   item.rejectQuantity + ( totalGrnApprovedQuantity - approvedQuantity) :  totalGrnApprovedQuantity - approvedQuantity;
				
		// 		self.model.rejectQty = totalGrnApprovedQuantity;
		// 		item.rejectReasonId = self.model.reasons[0].id;
		// 		item.isReject = true;
		// 		RejectPI2('Open', 'qtyaccepatReject', item);
		// 	} else if (approvedQuantity < totalGrnApprovedQuantity) {
		// 		item.rejectQuantity =  item.rejectQuantity!=undefined ||item.rejectQuantity > 0 || item.rejectQuantity!=null ?   item.rejectQuantity + ( totalGrnApprovedQuantity - approvedQuantity) :  totalGrnApprovedQuantity - approvedQuantity;
		// 		self.model.rejectQty = item.rejectQuantity;
		// 		item.rejectReasonId = self.model.reasons[0].id;
		// 		item.isReject = true;
		// 		RejectPI2('Open', 'qtyaccepatReject', item);
		// 	} else if (approvedQuantity == totalGrnApprovedQuantity) {
		// 		item.rejectQuantity =  item.rejectQuantity!=undefined ||item.rejectQuantity > 0 || item.rejectQuantity!=null ?   item.rejectQuantity + ( totalGrnApprovedQuantity - approvedQuantity) :  totalGrnApprovedQuantity - approvedQuantity;
			
		// 		self.model.rejectQty = item.rejectQuantity;
		// 		item.rejectReasonId = self.model.reasons[0].id;
		// 		item.isReject = false;
		// 		RejectPI2('Open', 'qtyaccepatReject', item);
		// 	}
		// }
		var ValidateRejection = function() {
			var isValid = true;
			if (!self.model.rejectReason) {
				isValid = false;
				growl.error('Please select reason to reject');
			}

			return isValid;
		}

		var SaveRejection = function() {
			if (ValidateRejection()) {
				self.model.physicalInspectionData.items
						.forEach(function(item) {
							if (item.id == self.model.selectedItemId) {
								var totalGrnApprovedQuantity = item.grnApprovedQuantity;
								var rejectedQuantity = !self.model.rejectQty
										|| isNaN(self.model.rejectQty) ? 0
										: parseInt(self.model.rejectQty);
								if (rejectedQuantity > totalGrnApprovedQuantity) {
									growl
											.error('Please enter quantity less than or equal to received quantity');
									return false;
								} else if (rejectedQuantity == totalGrnApprovedQuantity) {
									item.rejectQuantity = rejectedQuantity;
									item.approvedQuantity = 0;
									item.rejectReasonId = self.model.rejectReasons[0].id;
								} else if (rejectedQuantity == 0) {
									item.approvedQuantity = totalGrnApprovedQuantity;
									item.rejectQuantity = 0;
									item.rejectReasonId = self.model.rejectReasons[0].id;
									item.isReject = false;
								} else {
									item.rejectQuantity = rejectedQuantity;
									item.rejectNote = self.model.rejectNote;
									item.approvedQuantity = totalGrnApprovedQuantity
											- rejectedQuantity;
									item.rejectReasonId = self.model.rejectReasons[0].id;
								}
								var popup = angular
										.element('#qtyaccepatReject');
								popup.modal('hide');
								// ModalDismiss('qtyaccepatReject');
							}
						});
			}
		}
var SaveRejection1 = function() {
			if (ValidateRejection()) {
				self.model.physicalInspectionData.items
						.forEach(function(item) {
							if (item.id == self.model.selectedItemId) {
								var totalGrnApprovedQuantity = parseInt(item.bonusApprovedQuantity);
								var rejectedQuantity = !self.model.rejectQty
										|| isNaN(self.model.rejectQty) ? 0
										: parseInt(self.model.rejectQty);
								if (item.bonusApprovedQuantity > item.grnBonusApprovedQuantity) {
									growl
											.error('Please enter quantity less than or equal to received quantity');
									return false;
								} else if (rejectedQuantity == totalGrnApprovedQuantity) {
									item.rejectQuantity = rejectedQuantity;
									//item.approvedQuantity = 0;
									item.rejectReasonId = self.model.rejectReasons[0].id;
								} else if (rejectedQuantity == 0) {
									//item.approvedQuantity = totalGrnApprovedQuantity;
									item.rejectQuantity = 0;
									item.rejectReasonId = self.model.rejectReasons[0].id;
									item.isReject = false;
								} else {
									item.rejectQuantity = rejectedQuantity;
									item.rejectNote = self.model.rejectNote;
									//item.approvedQuantity = totalGrnApprovedQuantity - rejectedQuantity;
									item.rejectReasonId = self.model.rejectReasons[0].id;
								}
								var popup = angular
										.element('#qtyaccepatReject');
								popup.modal('hide');
								// ModalDismiss('qtyaccepatReject');
							}
						});
			}
		}
		var ValidateSelectedItems = function() {
			var isValid = true;

			if (!self.model.selectedItems
					|| self.model.selectedItems.length <= 0) {
				growl.error('Select Items to Save');
				isValid = false;
			}
			self.model.selectedItems
					.forEach(function(data) {
						if(data.approvedQuantity == 0 && data.rejectQuantity == 0){
							growl.error("Please enter approved quantity");
							isValid = false;
						}
						
						/*if(data.approvedQuantity == 0){
							growl.error("Please enter approved quantity");
							isValid = false;
						}*/
						if (data.approvedQuantity > data.receivedQuantity
								+ data.receivedBonusQuantity) {
							growl
									.error("Approved quantity should not greater than Received quantity");
							isValid = false;
						}
					})
					            if(!isValid)
					            {
					            $scope.saveBtnDisabled=false;
					            }
								return isValid;
		}
        $scope.saveBtnDisabled=false;
		var SaveAcceptReject = function() {
	        $scope.saveBtnDisabled=true;
			var detailsData = commonDetailService.getDataId();
			if (ValidateSelectedItems()) {
				var URI = CONSTANTS.PROCUREMENT.QUOTATION.PHYSICAL_INSPECTION.SAVE_API;

				var data = {
					"id" : self.model.inspectId,
					"remark" : self.model.remark,
					"statusId" : setStatus('Closed'),
					"storeId" : detailsData.storeId,
					 "userId" :cookieObject.userId,
		             "organizationId" :cookieObject.organizationId,
		             "unitId" :cookieObject.unitId,
					"items" : []
				};

				$rootScope.startLoader();
				self.model.physicalInspectionData.items
						.forEach(function(purchase) {
							if (purchase.isSelected) {
								$scope.storagedata = [];
								if (purchase.storageDetails) {
									angular.forEach(purchase.storageDetails,
											function(sdata) {
												var obj = {
													rackId : '',
													shelfId : '',
													remark : ''
												}
												obj.rackId = sdata.rackId;
												obj.shelfId = sdata.shelfId;
												obj.remark = sdata.remark;
												$scope.storagedata.push(obj);
											});
								}
								if (purchase.isReject) {
									data.items
											.push({
												"id" : purchase.id,
												"approvedQuantity" : parseInt(purchase.approvedQuantity),
												"bonusApprovedQuantity" :  parseInt(purchase.bonusApprovedQuantity),
												"rejectQuantity" : parseInt(purchase.rejectQuantity),
												"rejectReasonId" : purchase.rejectReasonId,
												"rejectNote" : purchase.rejectNote,
												"statusId" : setStatus('Closed'),
												"storageDetails" : $scope.storagedata,
									            "grnBonusBatchExpiry":purchase.grnBonusBatchExpiryData
											});
								} else {
									data.items
											.push({
												"id" : purchase.id,
												"approvedQuantity" : parseInt(purchase.approvedQuantity),
												"bonusApprovedQuantity" :  parseInt(purchase.bonusApprovedQuantity),
												"rejectQuantity" : parseInt(purchase.rejectQuantity),
												"rejectReasonId" : 1,
												"rejectNote" : null,
												"statusId" : setStatus('Closed'),
												"storageDetails" : $scope.storagedata,
									            "grnBonusBatchExpiry":purchase.grnBonusBatchExpiryData
											});
								}
							}
						});
				var count=0;
				self.model.physicalInspectionData.items
				.forEach(function(purchase) {
					         if(purchase.statusId==8)
					        	 {
					        	 count=count+1;
					        	 }
							});
				var count1=0;
				data.items
				.forEach(function(purchase) {
					count1=count1+1;
				});
				if((count+count1)==self.model.physicalInspectionData.items.length)
					{
					data.statusId=8;
					}
				else
					{
					data.statusId=18;
					}
				GenericService.serviceAction("POST", URI, data).then(
						function(response) {
							self.model.SelectedOrderIds = [];
							self.model.selectAllRows = false;
							growl.success(response.data.message);
							NavigateToBack();
							$rootScope.stopLoader();
						}, function(err) {
							growl.error(err.data.message);
							$rootScope.stopLoader();
						});
			}
		}

		var SelectItem = function(purchase) {
			var purchaseIds = self.model.SelectedOrderIds;
			if (purchase.isSelected) {
				purchase.isDisabled = false;
				purchase.approvedQuantity = purchase.grnApprovedQuantity;
				purchaseIds.push(purchase.id);
				self.model.selectedItems.push(purchase);
				if (self.model.physicalInspectionData.items.every(isSelected)) {
					self.model.selectAllRows = true;
				} else {
					self.model.selectAllRows = false;
				}
			} else {
				purchase.isReject = false;
				purchase.isDisabled = true;
				CancelRejection(purchase);
				var index = purchaseIds.indexOf(purchase.id);
				purchase.approvedQuantity = 0;
				if (index > -1) {
					purchaseIds.splice(index, 1);
				}
				if (self.model.physicalInspectionData.items.every(isSelected)) {
					self.model.selectAllRows = true;
				} else {
					self.model.selectAllRows = false;
				}
			}
		}

		var SelectAllOrders = function() {
			if (self.model.selectAllRows) {
				self.model.SelectedOrderIds = [];
				self.model.physicalInspectionData.items.forEach(function(
						purchase) {
					// if (isDisable(purchase)) {
					if(purchase.approvedQuantity==undefined || purchase.approvedQuantity==null || purchase.approvedQuantity<0 || purchase.approvedQuantity==0)
						{
					purchase.isSelected = true;
					purchase.isDisabled = false;
						}
					// purchase.approvedQuantity = purchase.receivedQuantity;
					purchase.approvedQuantity = purchase.grnApprovedQuantity;
					self.model.SelectedOrderIds.push(purchase.id);
					self.model.selectedItems.push(purchase);
					// }
				});
			} else {
				self.model.SelectedOrderIds = [];
				self.model.physicalInspectionData.items.forEach(function(
						purchase) {
					CancelRejection(purchase);
					purchase.isSelected = false;
					purchase.isDisabled = true;
					purchase.isReject = false;
					purchase.approvedQuantity = 0;
					purchase.rejectQuantity = 0;
				});
			}
		}

		var isDisable = function(purchase) {
			// if (purchase.status == 'Closed' || purchase.status == 'Reject' ||
			// purchase.status == 'Approved'){
			// purchase.status !== 'Partially Approved' || purchase.status !==
			// 'Pending' || purchase.status !== 'New' || purchase.status !==
			// 'Partially Completed'
			if (purchase.status != "Pending Approval") {
				return true;
			}
			return false;
		}

		var isSelected = function(purchase) {
			
				return purchase.isSelected;
			
		}

		var ValidateStatus = function() {
			var isValid = true;
			if (!self.model.SelectedOrderIds
					|| self.model.SelectedOrderIds.length == 0) {
				isValid = false;
			}
			return isValid;
		}

		var ValidetReason = function() {
			var isValid = true;

			if (!self.model.cancelReason) {
				self.model.isReasonValid = true;
				isValid = false;
			}

			return isValid;
		}

		var ModalDismiss = function(modal) {
			var popup = angular.element('#' + modal);
			popup.modal('hide');
			self.model.rejectNote = undefined;
			self.model.cancelReason = undefined;
			self.model.onclose.isReject = false;
			self.model.rejectQty = 0;
			self.model.rejectReason = undefined;
		}

		var ModalDismissRejectionPopup = function(item) {
			item.items.forEach(function(data) {
				var prevRequest = self.model.requestItems.items.find(function(
						prevItem) {
					return prevItem.id == data.id;
				});

				if (prevRequest) {
					data.statusId = prevRequest.statusId;
					data.rejectQuantity = prevRequest.rejectQuantity;
					data.approvedQuantity = prevRequest.grnApprovedQuantity;
					// + prevRequest.bonusApprovedQuantity
					data.isReject = false;
				}
			});
			$('#qtyaccepatReject').modal('hide');
		}

		var CancelRejection = function(item) {
			var prevRequest = self.model.requestItems.items.find(function(
					prevItem) {
				return prevItem.id == item.id;
			});

			if (prevRequest) {
				item.statusId = prevRequest.statusId;
				item.rejectQuantity = 0;
				item.approvedQuantity = item.grnApprovedQuantity
						+ item.bonusApprovedQuantity;
			}
		}

			var CancelRejection1 = function(item) {
			var prevRequest = self.model.requestItems.items.find(function(
					prevItem) {
				return prevItem.id == item.id;
			});

			if (prevRequest) {
				item.statusId = prevRequest.statusId;
				item.rejectQuantity = 0;
				item.approvedQuantity = item.grnApprovedQuantity;
			}
		}
var CancelRejection2 = function(item) {
			var prevRequest = self.model.requestItems.items.find(function(
					prevItem) {
				return prevItem.id == item.id;
			});

			if (prevRequest) {
				item.statusId = prevRequest.statusId;
				item.rejectQuantity = item.rejectQuantity !=undefined || item.rejectQuantity > 0 ? item.rejectQuantity :  0;
				item.approvedQuantity = item.approvedQuantity;
			}
		}

		initializeController();
	}

	function config($stateProvider) {
		$stateProvider
				.state(
						"detailsphysicalInspec",
						{
							url : "/detailsphysicalInspec",
							templateUrl : 'views/procurement/quotation/details-physical-inspection.html',
							controller : "PIDetails.Controller",
							controllerAs : "vm",
							title : 'Physical Inspection'
						});
	}

	angular.module("myApp").config(config).controller("PIDetails.Controller",
			detailsPhysicalInspectionController);
})();