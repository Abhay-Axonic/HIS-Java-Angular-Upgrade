(function() {
	"use strict";

	function addNewConsignmentReturnController($scope, $filter, $rootScope,
			commonDetailService, PaginationService, CurrencyService, $http,
			$state, $cookies, CONSTANTS, GenericService, growl) {
		var self = this;

		// Default Constructor
		var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
		function initializeController() {
			$rootScope.loginpage = true;
			self.model = {
				pagination : {
					currentPage : 1,
					totalPages : 0,
					pageSize : '10',
					search : '',
					searchData : PaginationService.searchData,
					searchDataByField : PaginationService.searchDataByField,
					setTotalPages : PaginationService.setTotalPages,
					getPagesList : PaginationService.getPagesList,
					setPage : PaginationService.setPage,
					setFirstPage : PaginationService.setFirstPage,
					setPreviousPage : PaginationService.setPreviousPage,
					setNextPage : PaginationService.setNextPage,
					setLastPage : PaginationService.setLastPage,
					sortBy : PaginationService.sortBy
				},
				stores : [ {
					"store" : "Select Store"
				} ],

				// added by minesh
				storeList : [ {
					"store" : "Select Store"
				} ],
				assetTypes : [ {
					"type" : "Select GRN Type"
				} ],
				// end added by minesh

				assetTypeId : 1,
				assetCategories : [ {
					"category" : "Select Asset Category"
				} ],
				productCategories : [ {
					"category" : "Select Product Category"
				} ],
				fromDateOptions : {
					formatYear : 'yyyy',
					showWeeks : false
				},
				toDateOptions : {
					formatYear : 'yyyy',
					showWeeks : false
				},
				fromDate : new Date(),
				toDate : new Date(),
				SelectedItemsIds : [],
				//add code by minesh
				SelectedGRNDetailItemsIds : [],
				//end code by minesh
				selectAllRows : false,
				SelectedItemIds : [],
				NewItemList : [],
				// added by minesh
				SelectedOrderIds : [],
				SelectedGRNOrderIds : [],
				selectedItems : [],
				setSelectedItems : [],
				selectedGRN : [],
				setSelectedGRN : [],
				selectAllGRNRows : false,
				totalAmount : 0.00,
				totalTaxAmount : 0.00,
				totalNetAmount : 0.00,
				// end
				itemList : [],
				againstList : [ {
					requestedType : "Select Against"
				} ]
			};
			CurrencyService.GetCurrency().then(function(currency) {
				self.model.Currency = currency;
			})
			self.model.PopulateDropdown = PopulateDropdown;
			self.model.SearchVendorNames = SearchVendorNames;
			self.model.nameNotFound = nameNotFound;
			self.model.AddVendorName = AddVendorName;
			self.model.AddVendorNameGRN = AddVendorNameGRN;
			self.model.search = '';
			self.model.prevSearch = '';
			self.model.vendorNames = [];

			self.model.OpenFromDate = OpenFromDate;
			self.model.OpenToDate = OpenToDate;
			self.model.ConfirmRemove = ConfirmRemove;
			self.model.validatePopup = validatePopup;
			self.model.NavigteToBack = NavigteToBack;
			self.model.SetOldValueStore = SetOldValueStore;
			self.model.ChangeStore = ChangeStore;
			self.model.ConfirmPopup = ConfirmPopup;
			self.model.ModalDismiss = ModalDismiss;
			self.model.addItemModal = addItemModal;

			// minesh
			self.model.SelectAllOrders=SelectAllOrders;//for line selected line item.			
			self.model.GetConsignmentGRNList = GetConsignmentGRNList;
			self.model.SelectItem = SelectItem;
			self.model.SelectItemForGRN = SelectItemForGRN;
			self.model.SelectAllGRNItems = SelectAllGRNItems;
			self.model.SelectAllGRN = SelectAllGRN;
			self.model.SelectGRN = SelectGRN;
			self.model.SetSelectedItem = SetSelectedItem;
			self.model.RemoveItem = RemoveItem;
			self.model.CalculateAmounts = CalculateAmounts;
			self.model.SaveConsignmentStockReturnDetail = SaveConsignmentStockReturnDetail
			self.model.ClearId = ClearId;
			self.model.ViewDetails = ViewDetails;
			
			self.model.Expand=Expand;
			self.model.SelectAllItemsOfGRN=SelectAllItemsOfGRN;

			// self.model.totalAmount;
			// self.model.totalTaxAmount;
			self.model.ConsignmentStockPOHistory=ConsignmentStockPOHistory;
			self.model.ConsignmentReturnDetailHistory=ConsignmentReturnDetailHistory;

			PopulateDropdown();
		}

		var OpenFromDate = function() {
			self.model.fromDateOpened = true;
			self.model.toDateOptions.minDate = self.model.fromDate;
		}

		var OpenToDate = function() {
			self.model.toDateOpened = true;
			self.model.toDateOptions.minDate = self.model.fromDate;
		}

		var ViewDetails = function(batchId, batchNo, lotNo) {

			// var URI =
			// CONSTANTS.PROCUREMENT.QUOTATION.PHYSICAL_INSPECTION.DETAILS_API +
			// "detailsConsignmentStockConversion";
			var URI = CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_CONVERSION.STORE_STOCK_API;

			// $rootScope.startLoader();
			GenericService.serviceAction(
					"GET",
					URI + "/" + self.model.store.id + "/" + batchId + "/"
							+ batchNo + "/" + lotNo).then(function(response) {
				self.model.listData = response.data;
				// $rootScope.stopLoader();
			}, function(err) {
				// $rootScope.stopLoader();
				growl.error(err.data.message);
			});

		}

		/*
		 * var PopulateDropdown = function () { var returnable = self.model; //
		 * returnable.store = returnable.stores[0]; returnable.requestType =
		 * returnable.againstList[0]; var agList = [{ requestTypeId: 7,
		 * requestedType: "Against Expiry" },{ requestTypeId: 6, requestedType:
		 * "Against Non-Moving" },{ requestTypeId: 15, requestedType: "Rejected
		 * Against GRN" }] var URI =
		 * CONSTANTS.INVENTORY.STORE.RETURNABLE_GATEPASS.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;
		 * 
		 * $rootScope.startLoader(); GenericService.serviceAction("GET", URI,
		 * {}).then( function (response) { returnable.stores =
		 * returnable.stores.concat(response.data.store); returnable.againstList =
		 * returnable.againstList.concat(agList); for(var i=0;i<returnable.stores.length;i++) {
		 * if(returnable.stores[i].id==$cookies.getObject('selectedTopStore').storeId) {
		 * returnable.store = returnable.stores[i]; } } $rootScope.stopLoader();
		 * var returnableId = commonDetailService.getDataId();
		 * $rootScope.stopLoader(); if (!!returnableId) {
		 * commonDetailService.setDataId(null);
		 * GetReturnableGatepassDetails(returnableId); } else { return false; } },
		 * function (err) { $rootScope.stopLoader(); }); }
		 */
		var PopulateDropdown = function() {
			self.model.store = self.model.storeList[0];
			self.model.grnType = self.model.assetTypes[0];

			var URI = CONSTANTS.PROCUREMENT.QUOTATION.PHYSICAL_INSPECTION.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

			$rootScope.startLoader();
			GenericService.serviceAction("GET", URI, {}).then(
					function(response) {
						self.model.storeList = self.model.storeList
								.concat(response.data.store);
						// self.model.statuses = self.model.statuses;
						self.model.assetTypes = self.model.assetTypes
								.concat(response.data.assetType);
						// self.model.status = self.model.statuses[0];
						$rootScope.stopLoader();
					}, function(err) {
						$rootScope.stopLoader();
					});
		}

		function SearchVendorNames(search) {
			if (search.length < 1)
				return;

			if ((!self.model.prevSearch || self.model.prevSearch != search)
					&& (search.length == 1)) {
				self.model.prevSearch = search;
				var URI = CONSTANTS.GLOBAL.VENDOR_SEARCH_API + search + "/"+$scope.unitId;
				$rootScope.startLoader();
				return GenericService.serviceAction("GET", URI, {}).then(
						function(response) {
							if (!!response.data && response.data.length > 0) {
								self.model.vendorNames = response.data;
								$rootScope.stopLoader();
								return $filter('filter')(
										self.model.vendorNames, {
											$ : search
										});
							} else {
								$rootScope.stopLoader();
								return nameNotFound(search);
							}
						}, function(err) {
							$rootScope.stopLoader();
							return nameNotFound(search);
						});
			} else {
				if (!!self.model.vendorNames
						&& self.model.vendorNames.length > 0
						&& self.model.vendorNames[0].itemFound != undefined
						&& !self.model.vendorNames[0].itemFound) {
					return nameNotFound(search);
				} else {
					return $filter('filter')(self.model.vendorNames, {
						$ : search
					});
				}
			}
		}

		/*
		 * var GetConsignmentGRNList = function () {
		 * 
		 * if(ValidSearch()){ // var URI =
		 * CONSTANTS.PROCUREMENT.QUOTATION.GRN_APPROVAL.DETAILS_API +
		 * commonDetailService.getDataId(); var URI =
		 * CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_RETURN.GET_CONSIGNMENT_GRN;
		 * 
		 * var data = { "fromDate":
		 * moment(self.model.fromDate).format('YYYY-MM-DD'), "toDate":
		 * moment(self.model.toDate).format('YYYY-MM-DD'), "storeId":
		 * !!self.model.store && !!self.model.store.id ? self.model.store.id :
		 * undefined, "assetTypeId": self.model.grnType ? self.model.grnType.id :
		 * undefined, //"statusId": self.model.status ? self.model.status.id :
		 * undefined, "vendorName": self.model.vendorNameGRN ?
		 * self.model.vendorNameGRN : undefined, "vendorId":
		 * self.model.vendorIdGRN ? self.model.vendorIdGRN : undefined, "grnNo" :
		 * self.model.grnNo ? self.model.grnNo : undefined //"grnNo" :
		 * self.model.search.grnNo ? self.model.vendorId : undefined }
		 * 
		 * $rootScope.startLoaderPopup(); GenericService.serviceAction("POST",
		 * URI, data).then( function (response) { $rootScope.stopLoaderPopup();
		 * self.model.grnList = response.data; const vendorName =
		 * response.data.vendorName;//added by minesh self.model.assetTypeId =
		 * response.data.assetTypeId; self.model.grnTypeId =
		 * response.data.grnTypeId; self.model.requestItems =
		 * JSON.parse(JSON.stringify(response.data.items));
		 * self.model.grnList.items.forEach(function (item) { item.isDisabled =
		 * true; item.vendorName = vendorName;//added by minesh
		 * item.forApproveStageQty = item.grnStagedQuantity;
		 * item.fillRejectQtyFlag = true;
		 * 
		 * if (!!item.rejectedQuantity) { item.isReject = true; }
		 * 
		 * var uomList = null; if (self.model.assetTypeId == 1) { uomList =
		 * item.uom.find(function(uom){ return item.grnUomTypeId ==
		 * uom.uomTypeId; }); }
		 * 
		 * CalculateStock(item, item.uom, uomList); }); $rootScope.stopLoader(); },
		 * function (err) { $rootScope.stopLoaderPopup();
		 * growl.error(err.data.message); }); } }
		 */

		var GetConsignmentGRNList = function() {
			ClearId();
			var URI = CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_RETURN.GET_CONSIGNMENT_ALL_GRN;

			var data = {
				"fromDate" : moment(self.model.fromDate).format('YYYY-MM-DD'),
				"toDate" : moment(self.model.toDate).format('YYYY-MM-DD'),
				"storeId" : !!self.model.store && !!self.model.store.id ? self.model.store.id
						: undefined,
				"assetTypeId" : self.model.grnType ? self.model.grnType.id
						: undefined,
				"vendorName" : self.model.vendorNameGRN ? self.model.vendorNameGRN
						: undefined,
				"vendorId" : self.model.vendorIdGRN ? self.model.vendorIdGRN
						: undefined,
				"grnNo" : self.model.grnNo ? self.model.grnNo : undefined,
				"unitId":$scope.unitId,
				"organizationId":$scope.organizationId
			}

			$rootScope.startLoaderPopup();
			GenericService
					.serviceAction("POST", URI, data)
					.then(
							function(response) {
								$rootScope.stopLoaderPopup();
								self.model.grnList = response.data;
								self.model.grnListItems = [];
								self.model.grnList
										.forEach(function(grn) {
											grn.isDisabled = true;
											grn.items
													.forEach(function(item) {
														ConsignmentStockPOHistory(item);
														item.isDisabled = true;
														item.vendorName = grn.vendorName;// added
														// by
														// minesh
														var uomList = null;
														if (self.model.assetTypeId == 1) {
															uomList = item.uom
																	.find(function(
																			uom) {
																		return item.grnUomTypeId == uom.uomTypeId;
																	});
														}

														CalculateStock(item,
																item.uom,
																uomList);
														self.model.grnListItems
																.push(item);
													});
										});
								$rootScope.stopLoader();
							}, function(err) {
								$rootScope.stopLoaderPopup();
								growl.error(err.data.message);
							});
		}

		var ValidSearch = function() {
			let isValid = true;

			if (!self.model.grnNo || self.model.grnNolength <= 0) {
				growl.error('Please enter valid GRN number');
				isValid = false;
			}
			return isValid;
		}

		var ValidSelectedItem = function() {
			let isValid = true;
			if (selectedItem.returnQuantity > selectedItem.availableBatchQuantity) {
				growl
						.error('return quantity should be less than avalibale quantity');
				isValid = false;
			}
		}

		var CalculateStock = function(item, uom, uomList) {
			if (!!uomList) {
				var prQuantity = item.prQuantity;
				var maxOrderQuantity = item.maxOrderQuantity || 0;
				var maxStoreQuantity = item.maxStoreQuantity || 0;

				uomList.uomDetails = uomList.uomDetails.split(' ')[0];
				if (uomList.uomDetails == "P") {
					item.newMaxOrderQuantity = parseInt(maxOrderQuantity
							/ (uom[0].conversion * uom[1].conversion * uom[2].conversion));
					item.newMaxStoreQuantity = parseInt(maxStoreQuantity
							/ (uom[0].conversion * uom[1].conversion * uom[2].conversion));
				} else if (uomList.uomDetails == "S") {
					item.newMaxOrderQuantity = parseInt(maxOrderQuantity
							/ (uom[1].conversion * uom[2].conversion));
					item.newMaxStoreQuantity = parseInt(maxStoreQuantity
							/ (uom[1].conversion * uom[2].conversion));
				} else if (uomList.uomDetails == "D") {
					item.newMaxOrderQuantity = parseInt(maxOrderQuantity
							/ (uom[2].conversion));
					item.newMaxStoreQuantity = parseInt(maxStoreQuantity
							/ (uom[2].conversion));
				}
			} else {
				item.newMaxOrderQuantity = item.maxOrderQuantity || 0;
				item.newMaxStoreQuantity = item.maxStoreQuantity || 0;
			}
		}

		var CalculateAmounts = function(selectedItem) {
			let totalAmount = 0;
			let totalTaxAmount = 0;
			let totalNetAmount = 0;
			angular.forEach(self.model.selectedItems, function(item, key) {
				if (!!parseInt(item.returnQuantity)) {
					const itemAmmount = item.returnQuantity * item.cop;
					totalAmount += itemAmmount;
					totalTaxAmount += (itemAmmount * item.taxPercentage) / 100;
					totalNetAmount += (totalAmount + totalTaxAmount);
				}
			});
			self.model.totalAmount = totalAmount;
			self.model.totalTaxAmount = totalTaxAmount
			self.model.totalNetAmount = totalNetAmount

			if (selectedItem.returnQuantity > selectedItem.availableBatchQuantity) {
				growl
						.error('return quantity should be less than avalibale quantity');
			}
		}
		var CalculateAmountsForGRN = function(grn) {
			let totalAmount = 0;
			let totalTaxAmount = 0;
			let totalNetAmount = 0;
			angular.forEach(grn.items, function(item, key) {
				if (!!parseInt(item.returnQuantity)) {
					const itemAmmount = item.returnQuantity * item.cop;
					totalAmount += itemAmmount;
					totalTaxAmount += (itemAmmount * item.taxPercentage) / 100;
					totalNetAmount += (totalAmount + totalTaxAmount);
				}
			});
			return {
				totalAmount : totalAmount,
				totalTaxAmount : totalTaxAmount,
				totalNetAmount : totalNetAmount
			};
		}

		/*
		 * var SaveConsignmentStockReturnDetail = function () { var
		 * isValid=true; angular.forEach(self.model.selectedItems, function
		 * (item, key) { if ( (!(!!parseInt(item.returnQuantity))) ||
		 * parseInt(item.returnQuantity) > item.availableBatchQuantity ) {
		 * isValid = false; growl.error('Please Enter Valid Return Quantity'); }
		 * });
		 * 
		 * if (isValid){ var URI =
		 * CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_RETURN.SAVE_CONSIGNMENT_STOCK_RETURN_DETAIL;
		 * 
		 * var data = { "assetType" : self.model.grnList.assetType,
		 * "assetTypeId" : self.model.grnList.assetTypeId, "grnId" :
		 * self.model.grnList.id, "grnType" : self.model.grnList.grnType,
		 * "grnTypeId" : self.model.grnList.grnTypeId, "items": [], "status":
		 * 'Pending', "statusId": 2, "approvalStatus": 'Pending',
		 * "approvalStatusId": 2, "storeId" : self.model.grnList.storeId,
		 * "storeName" : self.model.grnList.storeName, "vendorName":
		 * self.model.grnList.vendorName, "vendorId":
		 * self.model.grnList.vendorId, "isConsignment":
		 * self.model.grnList.isConsignment,
		 * 
		 * "totalAmount": self.model.totalAmount, "totalTaxAmount":
		 * self.model.totalTaxAmount, "totalNetAmount":
		 * self.model.totalNetAmount } angular.forEach(self.model.selectedItems,
		 * function (item, key) { if (item.isSelected &&
		 * (parseInt(item.returnQuantity) > 0) && (parseInt(item.returnQuantity) <=
		 * item.availableBatchQuantity) ) { var itemDetail = {
		 * //ConsignmentStockReturnDetail "itemId":item.itemId,
		 * "itemCode":item.itemCode, "itemName":item.itemName,
		 * "batchNo":item.batchNo, "lotNumber":item.lotNumber,
		 * "grnUomType":item.grnUomType, "grnUomTypeId":item.grnUomTypeId,
		 * "grnUomUnit":item.grnUomUnit, "grnUomUnitId":item.grnUomUnitId,
		 * "expiryDate":item.expiryDate, "cop": item.cop, "mrp": item.mrp,
		 * "totalAmount": item.totalAmount, "remark": item.remark, "statusId":2,
		 * //item.statusId, "status":'Pending', //item.status,
		 * //"returnBatchQuantity":item.returnQuantity,
		 * "returnBatchQuantity":parseInt(item.returnQuantity),
		 * "batchId":item.batchId, "manufacturerId":item.manufacturerId,
		 * "goodReceiptNoteDetailId":item.goodReceiptNoteDetailId, "grnUom" :
		 * item.grnUom //"batchUomBP" : item.grnUom, }
		 * 
		 * var grnUom = []; angular.forEach(item.grnUom, function (uom) {
		 * grnUom.push({ id: uom.id, uomType: uom.uomType, uomTypeId:
		 * uom.uomTypeId, uomUnit: uom.uomUnit, uomUnitId:uom.uomUnitId,
		 * conversion: parseInt(uom.conversion), ipUom: uom.ipUom, opUom:
		 * uom.opUom, storeUom: uom.storeUom, grnDetailId: uom.grnDetailId })
		 * });
		 * 
		 * itemDetail.grnUom = grnUom ; data.items.push(itemDetail); } });
		 * 
		 * $rootScope.startLoader(); GenericService.serviceAction("POST", URI,
		 * data).then( function (response) { NavigteToBack();
		 * $rootScope.stopLoader(); growl.success('Consignment Stock Return
		 * Detail Save Successfully'); }, function (err) {
		 * $rootScope.stopLoader(); growl.error('Error While Saving Consignment
		 * Stock Return Detail'); }); }//valid }
		 */
        $scope.saveBtnDisabled=false;
		var SaveConsignmentStockReturnDetail = function() {
	        $scope.saveBtnDisabled=true;
			var isValid = true;
			angular.forEach(self.model.selectedItems, function(item, key) {
				if ((item.returnQuantity <= 0 /*|| item.returnQuantity > item.availableStockforReturn*/) && item.isSelected) {
					isValid = false;
					growl.error('Please Enter Valid Return Quantity');
				}
				if ((item.returnQuantity <= 0  || item.returnQuantity > ((item.totalConsignmentStock-item.consumedQty)-item.returnedConsignmentStock)-item.storeStockTransfered-item.totalPOCreatedQuantity ) 
						&& item.isSelected){
					isValid = false;
					growl.error('Return Quantity Must be Less Then Available Stock for Return' );
				}
			});
            if(!isValid)
            {
            $scope.saveBtnDisabled=false;
            }
			
			if (self.model.selectedItems.every(isEveryDisabledItem)) {
		          isValid = false;
		          growl.error('Please select Item');
		        }

			/*angular.forEach(self.model.selectedItems, function(item, key) {
				if (item.isSelected & item.returnQuantity > 0) {
					isValid = true;
				} else {
					isValid = false;
					growl.error('Please Select Line Item');
				}
			});*/
			/*
			 * if (self.model.selectedItems.length == 0) { isValid = false;
			 * growl.error('Please Select Item To Save'); }
			 */
			/*
			 * angular.forEach(self.model.selectedGRN.items, function (item,
			 * key) { /*angular.forEach(self.model.selectedItems, function
			 * (item, key) { if ( (!(!!parseInt(item.returnQuantity))) ||
			 * parseInt(item.returnQuantity) > item.availableBatchQuantity ) {
			 * isValid = false; growl.error('Please Enter Valid Return
			 * Quantity'); } });
			 */
			/*
			 * angular.forEach(self.model.selectedGRN.items, function (item,
			 * key) { if ( (!(!!parseInt(item.returnQuantity))) ||
			 * parseInt(item.returnQuantity) > item.availableBatchQuantity ) {
			 * isValid = false; growl.error('Please Enter Valid Return
			 * Quantity'); } });
			 */
			if (isValid) {

				var data = [];

				angular
						.forEach(
								self.model.selectedGRN,
								function(grn, key) {
									if (grn.isSelected) {
										var grnAmmount = CalculateAmountsForGRN(grn);
										var dataCR = {
											"assetType" : grn.assetType,
											"assetTypeId" : grn.assetTypeId,
											"grnId" : grn.id,
											"grnType" : grn.grnType,
											"grnTypeId" : grn.grnTypeId,
											"items" : [],
											"status" : 'Pending',
											"statusId" : 2,
											"approvalStatus" : 'Pending',
											"approvalStatusId" : 2,
											"storeId" : grn.storeId,
											"storeName" : grn.storeName,
											"vendorName" : grn.vendorName,
											"vendorId" : grn.vendorId,
											"isConsignment" : grn.isConsignment,

											"totalAmount" : grnAmmount.totalAmount,
											"totalTaxAmount" : grnAmmount.totalTaxAmount,
											"totalNetAmount" : grnAmmount.totalNetAmount,
											"organizationId": $scope.organizationId,
											"unitId": $scope.unitId,
											"userId": $scope.userId,
											"remark":self.model.remark
										}
										angular
												.forEach(
														grn.items,
														function(item, key) {
															if (item.isSelected
																	&& (parseInt(item.returnQuantity) > 0) /*
																											 * &&
																											 * (parseInt(item.returnQuantity) <=
																											 * item.availableBatchQuantity)
																											 */) {
																var itemDetail = { // ConsignmentStockReturnDetail
																	"itemId" : item.itemId,
																	"itemCode" : item.itemCode,
																	"itemName" : item.itemName,
																	"batchNo" : item.batchNo,
																	"lotNumber" : item.lotNumber,
																	"grnUomType" : item.grnUomType,
																	"grnUomTypeId" : item.grnUomTypeId,
																	"grnUomUnit" : item.grnUomUnit,
																	"grnUomUnitId" : item.grnUomUnitId,
																	"expiryDate" : item.expiryDate,
																	"cop" : item.cop,
																	"mrp" : item.mrp,
																	"totalAmount" : item.totalAmount,
																	"remark" : item.remark,
																	"statusId" : 2, // item.statusId,
																	"status" : 'Pending', // item.status,
																	// "returnBatchQuantity":item.returnQuantity,
																	"returnBatchQuantity" : parseInt(item.returnQuantity),
																	"batchId" : item.batchId,
																	"manufacturerId" : item.manufacturerId,
																	"goodReceiptNoteDetailId" : item.goodReceiptNoteDetailId,
																	"grnUom" : item.grnUom,
																	"batchId" : item.batchId,
																	"returnQuantity" : item.returnQuantity
																// "batchUomBP"
																// :
																// item.grnUom,
																}

																dataCR.items
																		.push(itemDetail);
															}
														});// end
										// ConsignmentStockReturnDetail
										data.push(dataCR);
									}
								}); // end ConsignmentStockReturnDetail

				$rootScope.startLoader();
				var URI = CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_RETURN.SAVE_CONSIGNMENT_STOCK_RETURN_DETAIL;

				GenericService
						.serviceAction("POST", URI, data)
						.then(
								function(response) {
									NavigteToBack();
									$rootScope.stopLoader();
									growl
											.success('Consignment Stock Return Detail Save Successfully');
								},
								function(err) {
									$rootScope.stopLoader();
									growl
											.error('Error While Saving Consignment Stock Return Detail');
								});
			}// valid
		}

		/*
		 * var SelectItem = function (item) { var itemIds =
		 * self.model.SelectedOrderIds; if (item.isSelected) { item.isDisabled =
		 * false; itemIds.push(item.id); self.model.setSelectedItems.push(item); }
		 * else { item.isDisabled = true; var index = itemIds.indexOf(item.id);
		 * if (index > -1) { itemIds.splice(index, 1); var indexOfRemovItem =
		 * self.model.setSelectedItems.findIndex(function(element){ return
		 * element.id===item.id; }) if(indexOfRemovItem !== -1){
		 * self.model.setSelectedItems.splice(indexOfRemovItem, 1) } } } if
		 * (self.model.grnList.items.every(isSelected)) {
		 * self.model.selectAllRows = true; } else { self.model.selectAllRows =
		 * false; } console.log(self.model.setSelectedItems);
		 * console.log(self.model.selectedItems); }
		 */
		var SelectItemForGRN = function(item) {
			var itemIds = self.model.SelectedOrderIds;
			if (item.isSelected) {
				item.isDisabled = false;
				itemIds.push(item.id);
				self.model.setSelectedItems.push(item);
			} else {
				item.isDisabled = true;
				var index = itemIds.indexOf(item.id);
				if (index > -1) {
					itemIds.splice(index, 1);
					var indexOfRemovItem = self.model.setSelectedItems
							.findIndex(function(element) {
								return element.id === item.id;
							})
					if (indexOfRemovItem !== -1) {
						self.model.setSelectedItems.splice(indexOfRemovItem, 1)
					}
				}
			}
			if (self.model.grnListItems.every(isSelectedForGRN)) {
				self.model.selectAllRows = true;
				self.model.selectAllGRNRows = true;
			} else {
				self.model.selectAllRows = false;
				self.model.selectAllGRNRows = false;
			}
			
			//start code for isSelectedAllItems for particular grn
			self.model.grnList.forEach(function(grn) {
				if (grn.items.every(isSelectedForGRN)){
					grn.isSelectedAllItems = true;
				}else{
					grn.isSelectedAllItems = false;
				}
			});
			//end code for isSelectedAllItems for particular grn

			// for grn checkbox releated code start
			// for grn check box
			self.model.grnList.forEach(function(grn) {
				var anyOneSelected = false;
				var grnIds = self.model.SelectedGRNOrderIds;
				grn.items.forEach(function(item) {
					if (item.isSelected) {
						anyOneSelected = true;
					}
				});
				if (anyOneSelected) {
					grn.isSelected = true;
					grn.isDisable = false;
					var index = grnIds.indexOf(grn.id);
					if (index < 0) {
						grnIds.push(grn.id);
						self.model.setSelectedGRN.push(grn);
					}

				} else {
					grn.isSelected = false;
					grn.isDisable = true;

					var index = grnIds.indexOf(grn.id);
					if (index > -1) {
						grnIds.splice(index, 1);
						var indexOfRemovItem = self.model.setSelectedGRN
								.findIndex(function(element) {
									return element.id === grn.id;
								})
						if (indexOfRemovItem !== -1) {
							self.model.setSelectedGRN.splice(indexOfRemovItem,
									1)
						}
					}
				}

			});

			// for grn checkbox releated code end

			console.log(self.model.setSelectedItems);
			console.log(self.model.setSelectedGRN);
			console.log(self.model.selectedItems);
		}

		var isSelectedForGRN = function(item) {
			if (!isDisableForGRN(item))
				return item.isSelected;
			else
				return true;
		}

		var isDisableForGRN = function(item) {
			if (item.status != 'New' && item.isSelected) {
				return true;
			}
			return false;
		}

		/*
		 * var SelectAllGRNItems = function () { if (self.model.selectAllRows) {
		 * self.model.grnList.items.forEach(function (item) { if
		 * (!isDisable(item)) { item.isSelected = true; item.disableFlag =
		 * false; self.model.SelectedOrderIds.push(item.id);
		 * self.model.setSelectedItems.push(item); } }); } else {
		 * self.model.SelectedOrderIds = []; //self.model.selectedItems = [];
		 * self.model.setSelectedItems = [];
		 * self.model.grnList.items.forEach(function (item) { item.isSelected =
		 * false; item.disableFlag = true; }); }
		 * console.log(self.model.setSelectedItems);
		 * console.log(self.model.selectedItems); }
		 */
		var SelectAllGRNItems = function() {
			if (self.model.selectAllRows) {
				self.model.selectAllGRNRows = self.model.selectAllRows;
				self.model.grnListItems.forEach(function(item) {
					if (!isDisableForGRN(item)) {
						item.isSelected = true;
						item.isDisabled = false;
						self.model.SelectedOrderIds.push(item.id);
						self.model.setSelectedItems.push(item);

					}
				});

				self.model.grnList.forEach(function(grn) {
					if (!isDisableForGRN(grn)) {
						grn.isSelected = true;
						grn.isSelectedAllItems = true;
						grn.isDisable = false;
						self.model.SelectedGRNOrderIds.push(grn.id);
						self.model.setSelectedGRN.push(grn);
					}
				});

			} else {
				self.model.selectAllGRNRows = self.model.selectAllRows;
				self.model.SelectedOrderIds = [];
				self.model.SelectedGRNOrderIds = [];
				// self.model.selectedItems = [];
				self.model.setSelectedItems = [];
				self.model.setSelectedGRN = [];
				self.model.grnListItems.forEach(function(item) {
					item.isSelected = false;
					item.isDisabled = true;
				});
				self.model.grnList.forEach(function(grn) {
					grn.isSelected = false;
					grn.isSelectedAllItems = false;
					grn.isDisable = true;
				});
			}
			//console.log(self.model.setSelectedItems);
			console.log(self.model.setSelectedGRN);
			console.log(self.model.selectedItems);
		}

		// start grn check box functionality
		var SelectGRN = function(grn) {
			var itemIds = self.model.SelectedOrderIds;
			var grnIds = self.model.SelectedGRNOrderIds;
			grn.isSelectedAllItems=grn.isSelected;
			if (grn.isSelected) {
				grn.isDisabled = false;
				grn.items.forEach(function(item) {
					if (!isDisableForGRN(item)) {
						item.isSelected = true;
						item.isDisabled = false;
						self.model.SelectedOrderIds.push(item.id);
						self.model.setSelectedItems.push(item);
					}
				});

				self.model.SelectedGRNOrderIds.push(grn.id);
				self.model.setSelectedGRN.push(grn);

			} else {
				grn.isDisabled = true;
				grn.items.forEach(function(item) {
					var index = itemIds.indexOf(item.id);
					if (index > -1) {
						itemIds.splice(index, 1);
						var indexOfRemovItem = self.model.setSelectedItems
								.findIndex(function(element) {
									return element.id === item.id;
								})
						if (indexOfRemovItem !== -1) {
							self.model.setSelectedItems.splice(
									indexOfRemovItem, 1)
						}
					}
					item.isSelected = false;
					item.isDisabled = true;
				});

				var grnIndex = grnIds.indexOf(grn.id);
				if (grnIndex > -1) {
					grnIds.splice(grnIndex, 1);
					var indexOfRemovItem = self.model.setSelectedGRN
							.findIndex(function(element) {
								return element.id === grn.id;
							})
					if (indexOfRemovItem !== -1) {
						self.model.setSelectedGRN.splice(indexOfRemovItem, 1)
					}
				}

			}

			if (self.model.grnListItems.every(isSelectedForGRN)) {
				self.model.selectAllRows = true;
				self.model.selectAllGRNRows = true;
			} else {
				self.model.selectAllRows = false;
				self.model.selectAllGRNRows = false;
			}

			//console.log(self.model.setSelectedItems);
			console.log(self.model.setSelectedGRN);
			console.log(self.model.selectedItems);
		}
		var SelectAllGRN = function() {
			self.model.selectAllRows = self.model.selectAllGRNRows;
			SelectAllGRNItems();

		}
		// end grn check box functionality

		var SetSelectedItem = function() {

			/*
			 * $.each(self.model.setSelectedItems, function(i, el){
			 * if($.inArray(el, self.model.selectedItems) === -1)
			 * self.model.selectedItems.push(el); });
			 */
			self.model.setSelectedItems.forEach(function(item) {
				if (item.isSelected) {
					item.isSelected = false;
					item.isDisabled= true;
					/*item.availableQty = ((item.convertedConsignmentStock - item.consumedQty) - item.returnedConsignmentStock) 
					- item.returnQuantity - item.storeStockTransfered - item.totalPOCreatedQuantity;*/
					item.availableQty = ((item.totalConsignmentStock - item.consumedQty) - item.convertedConsignmentStock) 
					- item.returnedConsignmentStock - item.storeStockTransfered;

					self.model.selectedItems.push(item);
				}
			});
			self.model.setSelectedGRN.forEach(function(grn) {
				if (grn.isSelected) {
					grn.isSelected = true;
					grn.isDisabled= false;
					// grn.availableQty = ((grn.convertedConsignmentStock - grn.consumedQty) - grn.returnedConsignmentStock) 
					// - grn.returnQuantity - grn.storeStockTransfered - grn.totalPOCreatedQuantity;
					self.model.selectedGRN.push(grn);
				}
			});

			console.log(self.model.selectedGRN);
			console.log(self.model.selectedItems);
		}
		
		 var SelectItem = function (item) {
		        let grnDetailItemIds = self.model.SelectedGRNDetailItemsIds;		  
		        if (item.isSelected) {
		        	item.isDisabled =false;
		        	grnDetailItemIds.push(item.goodReceiptNoteDetailId);
		          } else {
		        	  item.isDisabled =true;
		          let index = grnDetailItemIds.indexOf(item.goodReceiptNoteDetailId);
		          if (index > -1) {
		        	  grnDetailItemIds.splice(index, 1);
		          }
		        }
		  
		        if (self.model.selectedItems.every(isSelected)) {
		          self.model.selectAllOrders = true;
		        } else {
		          self.model.selectAllOrders = false;
		        }
		      }
		 
		 var isSelected = function (item) {
		        return item.isSelected;
		      }
		 
		 var isEveryDisabledItem = function (item) {
		        return item.isDisabled;
		      }
		 
		 var SelectAllOrders = function () {
		        if (self.model.selectAllOrders) {
		          self.model.SelectedGRNDetailItemsIds = self.model.selectedItems.map(function (item) {
		            item.isSelected = true;
		            item.isDisabled =false;
		            return item.goodReceiptNoteDetailId;
		          });
		  
		        } else {
		          self.model.SelectedGRNDetailItemsIds = [];
		          self.model.selectedItems.forEach(function (item) {
		            item.isSelected = false;
		            item.isDisabled =true;
		          });
		        }
		        console.log(self.model.SelectedGRNDetailItemsIds);
		        console.log(self.model.selectedItems);
		      }
		 

		function nameNotFound(search) {
			var item = {
				"itemFound" : false,
				"detail" : "Vendor Not found with name " + search
			};
			self.model.vendorNames = [ item ];
			return self.model.vendorNames;
		}

		function AddVendorName(vendor) {
			self.model.vendorName = vendor.vendorName;
			self.model.vendorId = vendor.id;
			self.model.vendorIdGRN = vendor.id;
			self.model.vendorNameGRN = vendor.vendorName;
		}

		function AddVendorNameGRN(vendor) {
			self.model.vendorNameGRN = vendor.vendorName;
			self.model.vendorIdGRN = vendor.id;
			self.model.vendorName = vendor.vendorName;
			self.model.vendorId = vendor.id;
		}

		function ClearId() {
			self.model.vendorIdGRN = (self.model.vendorNameGRN == undefined ? undefined
					: self.model.vendorId);
			self.model.vendorIdGRN = (self.model.vendorNameGRN == "" ? undefined
					: self.model.vendorId);
		}

		var validatePopup = function() {
			var isValid = true;
			if (!self.model.store.id) {
				growl.error("Please select valid store");
				isValid = false;
			} else if (!self.model.vendorId) {
				growl.error("Please Enter Valid Vendor");
				isValid = false;
			} else if (!self.model.assetTypeId) {
				growl.error("Please Enter Valid Asset Type");
				isValid = false;
			} else if (!self.model.requestType) {
				isValid = false;
				growl.error("Please Select Against");
			}

			return isValid;
		}

		var NavigteToBack = function() {
			$state.go('consignmentReturn');
		}

		var SetOldValueStore = function(store) {
			self.model.oldStore = store;
			self.model.store = self.model.oldStore;
		}

		var ChangeStore = function(store) {
			if (!!self.model.itemList.length) {
				self.model.store = self.model.oldStore;
				self.model.storeNew = JSON.parse(JSON.stringify(store));
				$("#storeChange").modal()
			}
		}

		var ConfirmPopup = function() {
			self.model.store = self.model.storeNew;
			self.model.itemList = [];
			ModalDismiss('storeChange');
		}

		var ModalDismiss = function(popupName) {
			var popup = angular.element('#' + popupName);
			popup.modal('hide');
		}

		var ConfirmRemove = function(item) {
			var popup = angular.element('#itemRemovePopup');
			popup.modal('show');
			self.model.itemForRemove = item;
		}

		/*
		 * var RemoveItem = function (item) { var newArray = [];
		 * self.model.selectedItems.map(function (obj) { if (obj.itemId !==
		 * item.itemId) { newArray.push(obj); } }); self.model.selectedItems =
		 * newArray; var popup = angular.element('#itemRemovePopup');
		 * popup.modal('hide'); }
		 */
		var RemoveItem = function(item) {
			/*
			 * var newArray = []; self.model.selectedItems.map(function (obj) {
			 * if (obj.goodReceiptNoteDetailId !== item.goodReceiptNoteDetailId) {
			 * newArray.push(obj); }else{ obj.isSelected=false; obj.isDisabled =
			 * true; } }); self.model.selectedItems = newArray;
			 */

			var newGRN = [];
			self.model.selectedGRN
					.map(function(grnObj) {
						var newItems = [];
						grnObj.items
								.map(function(itemObj) {
									if (itemObj.goodReceiptNoteDetailId !== item.goodReceiptNoteDetailId) {
										newItems.push(itemObj);
									} else {
										itemObj.isSelected = false;
										itemObj.isDisabled = true;
									}
								});
						grnObj.items = newItems;						
						if (grnObj.items.length > 0) {
							newGRN.push(grnObj);							
						} else {
							grnObj.isSelected = false;
							grnObj.isDisabled = true;
						}
					});

			self.model.selectedGRN = newGRN;
			console.log(self.model.selectedGRN);
			//satart code for functionality to remove line item 31-03-2020
			let selectedItemFromGRN=[];
			self.model.selectedGRN.forEach(function(grn) {
				angular.forEach(grn.items, function (grnItem) {
					selectedItemFromGRN.push(grnItem);
				});
			});
			self.model.selectedItems=selectedItemFromGRN; 
			//end code for functionality to remove line item 31-03-2020
			
			var popup = angular.element('#itemRemovePopup');
			popup.modal('hide');
		}

		var ClearGrnModal = function() {
			self.model.grnList = null;
			self.model.grnListItems = null;
			/* self.model.vendorNameGRN = null; */
			self.model.grnNo = null;
			self.model.SelectedOrderIds = [];
			self.model.SelectedGRNOrderIds = [];
			self.model.setSelectedItems = [];
			self.model.setSelectedGRN = [];
			self.model.selectAllRows = false;
			self.model.selectAllGRNRows = false;
			/* start code for resolved HIS-10054 Search field not woring on get GRN pop up in consignment return screen */
			self.model.search='';
			/* end code for resolved HIS-10054 Search field not woring on get GRN pop up in consignment return screen */

		}

		var addItemModal = function() {
			$("#getGRNModal").modal();
			ClearGrnModal();
		}
		
		var Expand = function (grn, grnItemList) {
		      angular.forEach(grnItemList, function (value) {
		        value.Expanded = false;
		      });

		      grn.Expanded = !grn.Expanded;
		    }
		
		var SelectAllItemsOfGRN = function(grn) {
			var itemIds = self.model.SelectedOrderIds;
			var grnIds = self.model.SelectedGRNOrderIds;
			grn.isSelected = grn.isSelectedAllItems;
			if (grn.isSelected) {
				grn.isDisabled = false;
				grn.items.forEach(function(item) {
					if (!isDisableForGRN(item)) {
						item.isSelected = true;
						item.isDisabled = false;
						self.model.SelectedOrderIds.push(item.id);
						self.model.setSelectedItems.push(item);
					}
				});

				self.model.SelectedGRNOrderIds.push(grn.id);
				self.model.setSelectedGRN.push(grn);

			} else {
				grn.isDisabled = true;
				grn.items.forEach(function(item) {
					var index = itemIds.indexOf(item.id);
					if (index > -1) {
						itemIds.splice(index, 1);
						var indexOfRemovItem = self.model.setSelectedItems
								.findIndex(function(element) {
									return element.id === item.id;
								})
						if (indexOfRemovItem !== -1) {
							self.model.setSelectedItems.splice(
									indexOfRemovItem, 1)
						}
					}
					item.isSelected = false;
					item.isDisabled = true;
				});

				var grnIndex = grnIds.indexOf(grn.id);
				if (grnIndex > -1) {
					grnIds.splice(grnIndex, 1);
					var indexOfRemovItem = self.model.setSelectedGRN
							.findIndex(function(element) {
								return element.id === grn.id;
							})
					if (indexOfRemovItem !== -1) {
						self.model.setSelectedGRN.splice(indexOfRemovItem, 1)
					}
				}

			}

			if (self.model.grnListItems.every(isSelectedForGRN)) {
				self.model.selectAllRows = true;
				self.model.selectAllGRNRows = true;
			} else {
				self.model.selectAllRows = false;
				self.model.selectAllGRNRows = false;
			}
		}
		
		var ConsignmentStockPOHistory = function (item) {
	    	  
	    	  var URI = CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_PO_CREATION.HISTORY_API +"/"+item.goodReceiptNoteDetailId + "/" + $scope.unitId;
	    
	          $rootScope.startLoaderPopup();
	          GenericService.serviceAction("GET", URI, {}).then(
	            function (response) {
	              self.model.consignmentPOHistory = response.data;
	              var totalNewApprovedQtyOnPopupClick=0;
					angular.forEach(self.model.consignmentPOHistory, function(item1, key) {
						totalNewApprovedQtyOnPopupClick=totalNewApprovedQtyOnPopupClick+item1.poApprovedQuantity;
					})
					item.totalPOCreatedQuantity=totalNewApprovedQtyOnPopupClick;
	              $rootScope.stopLoaderPopup();
	            },
	            function (err) {
	              $rootScope.stopLoaderPopup();
	            });
	        }
		
		var ConsignmentReturnDetailHistory = function (item,crId) {
			var URI = CONSTANTS.PROCUREMENT.QUOTATION.CONSIGNMENT_STOCK_RETURN.HISTORY_API;
			var param = {
					//"crId": crId,
					//"crDetailId": item.id,
					"grnDetailId":item.goodReceiptNoteDetailId,
					"unitId": $scope.unitId
					}
						
			$rootScope.startLoaderPopup();
			GenericService.serviceAction("POST", URI, param).then(
			function (response) {
				self.model.consignmentReturnDetailHistory = response.data;
				$rootScope.stopLoaderPopup();
			},
			function (err) {
				$rootScope.stopLoaderPopup();
			});
		}

		$scope.availableQtyCalc = function (item){
			if($scope.validate(item)){
				/*item.availableQty = ((item.convertedConsignmentStock - item.consumedQty) - item.returnedConsignmentStock) 
					- item.returnQuantity - item.storeStockTransfered - item.totalPOCreatedQuantity;*/
				item.availableQty = ((item.totalConsignmentStock - item.consumedQty) - item.convertedConsignmentStock) 
				- item.returnQuantity - item.storeStockTransfered-item.consignmentReturnedQuantity;

			}
		}

		$scope.validate = function(item) {
			var isValid = true;
			if(item.returnQuantity > (((item.totalConsignmentStock - item.consumedQty) - item.convertedConsignmentStock) 
				- item.storeStockTransfered-item.consignmentReturnedQuantity)){
				growl.error("Return quantity is grater than converted consignment stock.");
				isValid = false;
				item.returnQuantity = 0;
			/*	item.availableQty = ((item.convertedConsignmentStock - item.consumedQty) - item.returnedConsignmentStock) 
					- item.returnQuantity - item.storeStockTransfered - item.totalPOCreatedQuantity;*/
				item.availableQty = ((item.totalConsignmentStock - item.consumedQty) - item.convertedConsignmentStock) 
				- item.returnQuantity - item.storeStockTransfered-item.consignmentReturnedQuantity;

			}
			return isValid;
		}

		initializeController();
	}

	function config($stateProvider) {
		$stateProvider
				.state(
						"addNewConsignmentReturn",
						{
							url : "/addNewConsignmentReturn",
							templateUrl : 'views/procurement/quotation/add-new-consignment-return.html',
							controller : "addNewConsignmentReturn.Controller",
							controllerAs : "vm",
							title : 'Consignment Return'
						});
	}

	angular.module("myApp").config(config).controller(
			"addNewConsignmentReturn.Controller",
			addNewConsignmentReturnController);
})();
