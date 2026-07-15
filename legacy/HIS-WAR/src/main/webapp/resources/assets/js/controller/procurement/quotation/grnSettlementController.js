(function() {
	"use strict";

	function grnSettlementController($scope, $rootScope,  $http,
			$filter, $state, screenDataService, $cookies, CONSTANTS, reportService,
			GenericService, PaginationService, commonDetailService,
			StatusService, rejectReasonService, growl, paymentCollFactory,
			BillingGenericService) {
		var self = this;
		$scope.heading = commonDetailService.getDataId();
		$scope.tabdata = $scope.heading;
		var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
		function initializeController() {
			$rootScope.loginpage = true;

			/* First Level */
			// $rootScope.firstTabLevel = 1;
			$rootScope.setFirstTabLevel = function(newTab) {
				$rootScope.firstTabLevel = newTab;
			};
			$rootScope.isSetFirstTabLevel = function(tabNum) {
				return $rootScope.firstTabLevel === tabNum;
			};

			/* Second Level */
			$rootScope.secondTabLevel = 1;
			$rootScope.setSecondTabLevel = function(newTab) {
				// searchSettledList();
				$rootScope.secondTabLevel = newTab;
			};
			$rootScope.isSet = function(tabNum) {
				return $rootScope.secondTabLevel === tabNum;
			};

			$scope.tab2List = function() {
				searchSettledList();
			}

			$scope.tab1List = function() {
				GetGRNSettlementList();
			}

			self.model = {
				pagination : {
					currentPage : 1,
					totalPages : 0,
					pageSize : '10',
					search : '',
					searchData : PaginationService.searchData,
					setTotalPages : PaginationService.setTotalPages,
					getPagesList : PaginationService.getPagesList,
					setPage : PaginationService.setPage,
					setFirstPage : PaginationService.setFirstPage,
					setPreviousPage : PaginationService.setPreviousPage,
					setNextPage : PaginationService.setNextPage,
					setLastPage : PaginationService.setLastPage,
					sortBy : PaginationService.sortBy
				},
				fromDate : new Date(),
				toDate : new Date(),
				fromDateOptions : {
					formatYear : 'yyyy',
					showWeeks : false,
					maxDate : new Date(),
					minDate : moment(new Date()).subtract(365, 'days').toDate()
				},
				toDateOptions : {
					formatYear : 'yyyy',
					showWeeks : false,
//					maxDate : new Date()
				},
				fromDateSettled : new Date(),
				toDateSettled : new Date(),
				fromDateOptionsSettled : {
					formatYear : 'yyyy',
					showWeeks : false,
					maxDate : new Date()
				},
				toDateOptionsSettled : {
					formatYear : 'yyyy',
					showWeeks : false,
					maxDate : new Date()
				},
				storeList : [ {
					"store" : "Select Store"
				} ],
				fromstoreList : [ {
					"store" : "Select Store"
				} ],
				assetTypes : [ {
					"type" : "Select GRN Type"
				} ],
				statuses : [ {
					"status" : "All"
				} ],
				paymentStatuses : [ {
					"status" : "Select All"
				} ],
				paymentTypes : [ {
					"status" : "Select"
				} ],
				vendorId : null
			};
			self.model.OpenFromDate = OpenFromDate;
			self.model.OpenFromDateSettled = OpenFromDateSettled;
			self.model.OpenToDate = OpenToDate;
			self.model.OpenToDateSettled = OpenToDateSettled;
			self.model.SearchVendorNames = SearchVendorNames;
			self.model.AddVendorName = AddVendorName;
			self.model.GetGRNSettlementList = GetGRNSettlementList;
			self.model.NavigateToDetails = NavigateToDetails;
			self.model.searchSettledList = searchSettledList;
			self.model.NavigateToDetailsSettled = NavigateToDetailsSettled;
			self.model.SelectAllGRN = SelectAllGRN;
			self.model.SelectGRN = SelectGRN;
			self.model.quantityValidation = quantityValidation;
			self.model.closePopup = closePopup;
			self.model.printGRNSettlementReport = printGRNSettlementReport;
			self.model.selectAllCN = selectAllCN;
			self.model.selectCNItem = selectCNItem;

			GetGRNSettlementList();
			// searchSettledList();
			PopulateDropdown();
			if($scope.tabdata == 2){
				searchSettledList();
			}
			else{
				GetGRNSettlementList();
			}
		}

		if($scope.heading == 2){
			// $rootScope.firstTabLevel = 2;
			$rootScope.isSetFirstTabLevel(2);
		}
		else{
			// $rootScope.firstTabLevel = 1;
			$rootScope.isSetFirstTabLevel(1);
		}

		var cookieObject = $cookies.getObject('cookieObject');
		$scope.unitId = cookieObject.unitId;
		$scope.organizationId = cookieObject.organizationId;
		$scope.userId = cookieObject.userId;
        var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
        $scope.dateFormat = cookieObjectAppConfig.dateFormat;
		
		$scope.initPaymentDetails = function() {
			$scope.paymentDetails = {
				"paymentRecieptDetailsId" : "",
				"paymentModeId" : 1,
				"paymentModeName" : "Cash",
				"amount" : "",
				"bankId" : "",
				"bankName" : "",
				"accountNumber" : "",
				"checkNumber" : "",
				"cardNo" : "",
				"cardHolderName" : "",
				"dateOfTransaction" : new Date().getTime(),
				"expiryDate" : "",
				"paymentVoucherNo" : "",
				"cvv" : "",
				"unitId" : $scope.unitId,
				"orgnisationId" : $scope.organizationId,
				"status" : "A",
				"createdDate" : new Date().getTime(),
				"createdBy" : $scope.userId,
				"updatedDate" : new Date().getTime(),
				"updatedBy" : $scope.userId,
				"chequeDate" : "",
				"cardTypeId" : "",
				"isCancle" : "I",
				"loyaltyCardAmt" : '',
				"cardBalAmount" : '',
				"loyaltyCardNo" : '',
				'otp' : '',
				'hospitalAccountId' : '',
				'transactionNo'         : ""
			}
		};
		$scope.initPaymentDetails();

		var OpenFromDate = function() {
			self.model.fromDateOpened = true;
			self.model.toDateOptions.minDate = self.model.fromDate;
		}

		var OpenFromDateSettled = function() {
			self.model.fromDateOpenedSettled = true;
			self.model.toDateOptions.minDate = self.model.fromDateSettled;
		}

		var OpenToDate = function() {
			self.model.toDateOpened = true;
			self.model.toDateOptions.minDate = self.model.fromDate;
		}

		var OpenToDateSettled = function() {
			self.model.toDateOpenedSettled = true;
			self.model.toDateOptions.minDate = self.model.fromDateSettled;
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
		}

		var PopulateDropdown = function() {
			self.model.store = self.model.storeList[0];
			self.model.fromstore = self.model.fromstoreList[0];
			self.model.status = self.model.statuses[0];
			self.model.grnType = self.model.assetTypes[0];
			$scope.paymentStatuses = [ {
				name : "Select All",
				id : 0
			}, {
				name : " Partially Settled",
				id : 19
			}, {
				name : " Pending ",
				id : 2
			}, {
				name : "Settled",
				id : 20
			} ];
			// $scope.paymentStatuses=0;

			$scope.paymentTypes = [ {
				name : "Cash",
				id : 1
			}, {
				name : "Credit",
				id : 2
			} ];

			var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN.GRN_DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

			$rootScope.startLoader();
			GenericService.serviceAction("GET", URI, {}).then(
					function(response) {
						self.model.storeList = self.model.storeList
								.concat(response.data.deliveryStores);
						self.model.fromstoreList = self.model.fromstoreList
								.concat(response.data.requestingStores);
						self.model.statuses = self.model.statuses
								.concat(response.data.status);
						self.model.assetTypes = self.model.assetTypes
								.concat(response.data.assetType);
						$rootScope.stopLoader();
					}, function(err) {
						growl.error(err.data.message);
						$rootScope.stopLoader();
					});
		}

		var ValidateDate = function() {
			var isValid = true;
			if (self.model.fromDate > self.model.toDate) {
				growl.error('From date should be lesser than to date');
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

		var GetGRNSettlementList = function() {
			if (ValidateDate()) {
				var grn = self.model;

				var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN_SETTLEMENT.SEARCH_GRN_SETTLEMENT_API;

				if (!self.model.vendorName) {
					self.model.vendorId = undefined;
					self.model.vendorName = undefined;
				}

				var data = {
					"fromDate" : moment(grn.fromDate).format('YYYY-MM-DD'),
					"toDate" : moment(grn.toDate).format('YYYY-MM-DD'),
					"storeId" : !!grn.store && !!grn.store.id ? grn.store.id
							: undefined,
					"requestingStoreId" : !!grn.fromstore && !!grn.fromstore.id ? grn.fromstore.id
							: undefined,
					"grnNumber" : grn.grnNumber ? grn.grnNumber : undefined,
					"grnTypeId" : grn.grnType ? grn.grnType.id : undefined,
					"vendorName" : grn.vendorName ? grn.vendorName : undefined,
					"vendorId" : grn.vendorId ? grn.vendorId : undefined,
					"billTypeName" : grn.paymentTypes ? grn.paymentTypes.name
							: undefined,
					"paymentStatuses" : grn.paymentStatuses.id ? grn.paymentStatuses.id
							: 0,
					"unitId": $scope.unitId
				}

				$rootScope.startLoader();
				GenericService.serviceAction("POST", URI, data).then(
						function(response) {
							self.model.pagination.data = [];
							grn.grnSettlementList = response.data;

							// angular each
							self.model.grnSettlementList
									.forEach(function(value) {
							              if(value.invoiceDate.toString().includes("-"))
						                  {
						                  value.stringDate=true;
						                  }
							              if(value.stringDate==true)
							            	  {
		                                  var dateParseInvoice = value.invoiceDate.split('-');
		                                  var dateObjectInvoice = new Date(dateParseInvoice[0], dateParseInvoice[1] - 1, dateParseInvoice[2]); 
		                                  var longValueInvoice = dateObjectInvoice.getTime();
		                                  value.invoiceDate = moment(longValueInvoice).format($scope.dateFormat.toUpperCase());
							            	  }
							              if(value.dcDate != null && value.dcDate.toString().includes("-"))
						                  {
						                  value.stringDcDate=true;
						                  }
							              if(value.stringDcDate==true)
							            	  {
		                                  var dateParseDC = value.dcDate.split('-');
		                                  var dateObjectDC = new Date(dateParseDC[0], dateParseDC[1] - 1, dateParseDC[2]); 
		                                  var longValueDC = dateObjectDC.getTime();
		                                  value.dcDate = moment(longValueDC).format($scope.dateFormat.toUpperCase());
							            	  }
										value.disFlag = false;
										value.netAmount = value.netAmount - value.totalCreditAmount;
										value.balanceAmount = value.balanceAmount - value.totalCreditAmount;
										if(value.netAmount == value.paidAmount){
										   value.paymentStatus ="Settled";
										}
									});

							if (!!response && !!response.data
									&& response.data.length > 0) {
								PaginationService.searchData(
										self.model.grnSettlementList,
										self.model.pagination);
							}
							self.model.SelectedGRNIds = [];
							self.model.selectAllRows = false;
							$rootScope.stopLoader();
						}, function(err) {
							growl.error(err.data.message);
							$rootScope.stopLoader();
						});

				var screenData = {
					searchrequest : {
						"fromDate" : moment(self.model.fromDate).format(
								'YYYY-MM-DD'),
						"toDate" : moment(self.model.toDate).format(
								'YYYY-MM-DD'),
						"status" : self.model.status,
						"grnNumber" : self.model.grnNumber,
						"store" : self.model.store,
						"fromstore" : self.model.fromstore,
						"vendorName" : self.model.vendorName,
						"grnType" : self.model.grnType,
						"vendorId" : self.model.vendorId
					},
					screenName : 'grn'
				}
				screenDataService.setScreenData(screenData);
			}
		}

		var SelectAllGRN = function() {
			if (self.model.selectAllRows) {
				self.model.SelectedGRNIds = [];
				$scope.paymentDetails.cashAmount1 = 0;
				$scope.paymentDetails.cashAmount = 0;
				self.model.grnSettlementList
						.forEach(function(grn) {
							if (grn.paymentStatus != "Settled") {
								grn.isSelected = true;
								self.model.SelectedGRNIds.push(grn.id);
								$scope.settleBtn = false;
								$scope.paymentDetails.cashAmount1 += grn.balanceAmount;
								$scope.paymentDetails.cashAmount = $scope.paymentDetails.cashAmount1
										.toFixed(2);
								$scope.paymentDetails.chequeAmount = $scope.paymentDetails.cashAmount1
										.toFixed(2);
								$scope.eTransferObj.transferAmount = $scope.paymentDetails.cashAmount1
										.toFixed(2);
								$scope.totalamount = $scope.paymentDetails.cashAmount;
								$scope.fundAmount = $scope.totalamount;
								$scope.dueAmount = $scope.paymentDetails.cashAmount;
								$scope.amountPaid = $scope.paymentDetails.cashAmount;
							} else {
								grn.isSelected = false;
								$scope.paymentDetails.cashAmount = 0;
							}
						});
			} else {
				self.model.SelectedGRNIds = [];
				self.model.grnSettlementList.forEach(function(grn) {
					grn.isSelected = false;
					$scope.settleBtn = true;
				});
			}
		}

		var isSelected = function(grn) {
			return grn.isSelected;
		}

		$scope.mpesaObject = {
			"transactionId" : null,
			"mobileNo" : null,
			"mpesaAmount" : null,
			"resTransactionId" : null
		};
		$scope.eTransferObj = {
			transBankId : "",
			transferAmount : "",
			transTransactionDate : "",
			transactionNo : "",
			transactionobj : {
				id : "",
				name : ""
			}
		}
		$scope.settleBtn = true;
		$scope.fundAmount = 0;
		$scope.paymentDetails.cashAmount1 = 0;
		$scope.paymentDetails.chequeAmount1 = 0;
		$scope.mpesaObject.mpesaAmount1 = 0;
		$scope.eTransferObj.transferAmount1 = 0;
		var SelectGRN = function(grn) {
			var purchaseIds = self.model.SelectedGRNIds;

			if (grn.isSelected) {
				purchaseIds.push(grn.id);
				self.model.grnSettlementList.forEach(function(data) {
					if (data.vendorName !== grn.vendorName) {
		                data.isSelected = false; 
		                data.disFlag = true; 
		            } else {
		                data.disFlag = false;
		            }
				});
				$scope.settleBtn = false;
				$scope.paymentDetails.cashAmount1 += grn.balanceAmount;
				$scope.paymentDetails.cashAmount = $scope.paymentDetails.cashAmount1
						.toFixed(2);
				$scope.paymentDetails.chequeAmount = $scope.paymentDetails.cashAmount1
						.toFixed(2);
				$scope.eTransferObj.transferAmount = $scope.paymentDetails.cashAmount1
						.toFixed(2);
				$scope.totalamount = $scope.paymentDetails.cashAmount;
				$scope.fundAmount = $scope.totalamount;
				$scope.dueAmount = $scope.paymentDetails.cashAmount;
				$scope.amountPaid = $scope.paymentDetails.cashAmount;
			} else {
				self.model.grnSettlementList.forEach(function(data) {
					data.disFlag = false;
				});
				var index = purchaseIds.indexOf(grn.id);
				if (index > -1) {
					purchaseIds.splice(index, 1);
				}
				$scope.settleBtn = true;
				// $scope.paymentDetails.cashAmount1 -= grn.netAmount;
				// $scope.paymentDetails.cashAmount =
				// $scope.paymentDetails.cashAmount1.toFixed(2);
			}

			angular.forEach(self.model.grnSettlementList, function(list) {
				if (list.isSelected == true) {
					$scope.settleBtn = false;
				}
			});

			if (self.model.grnSettlementList.every(isSelected)) {
				self.model.selectAllRows = true;
			} else {
				self.model.selectAllRows = false;
			}

			$scope.paymentDetails.chequeAmount1 += grn.balanceAmount;
			$scope.mpesaObject.mpesaAmount1 += grn.balanceAmount;
			$scope.eTransferObj.transferAmount1 += grn.balanceAmount;
			$scope.paymentDetails.chequeAmount = $scope.paymentDetails.chequeAmount1
					.toFixed(2);
			$scope.mpesaObject.mpesaAmount = $scope.mpesaObject.mpesaAmount1
					.toFixed(2);
			$scope.eTransferObj.transferAmount = $scope.eTransferObj.transferAmount1
					.toFixed(2);
			// $scope.totalAmount = $scope.paymentDetails.cashAmount;
		}

		var NavigateToDetails = function(grn) {
			commonDetailService.setDataId(grn.id);
			$state.go('detailsGRNSettlementApproval');
		}

		var ValidateDateSettled = function() {
			var isValid = true;
			if (self.model.fromDateSettled > self.model.toDateSettled) {
				growl.error('From date should be lesser than to date');
				isValid = false;
			} else if (self.model.fromDateSettled == self.model.toDateSettled) {
				isValid = true;
			}

			if (!self.model.fromDateSettled || !self.model.toDateSettled) {
				growl.error('Please enter valid date');
				isValid = false;
			}

			return isValid;
		}

		var searchSettledList = function() {
			if (ValidateDateSettled()) {
				var grn = self.model;

				var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN_SETTLEMENT.SEARCH_GRN_SETTLED_API;

				if (!self.model.vendorName) {
					self.model.vendorId = undefined;
					self.model.vendorName = undefined;
				}

				var data = {
					"fromDate" : moment(grn.fromDateSettled).format(
							'YYYY-MM-DD'),
					"toDate" : moment(grn.toDateSettled).format('YYYY-MM-DD'),
					"vendorName" : grn.vendorName ? grn.vendorName : undefined,
					"vendorId" : grn.vendorId ? grn.vendorId : undefined,
					"receiptNo" : grn.receiptNo ? grn.receiptNo : undefined,
					"unitId": $scope.unitId
				}

				$rootScope.startLoader();
				GenericService.serviceAction("POST", URI, data).then(
						function(response) {
							self.model.pagination.data = [];
							grn.grnSettledList = response.data;
							if (!!response && !!response.data
									&& response.data.length > 0) {
								PaginationService.searchData(
										self.model.grnSettledList,
										self.model.pagination);
							}
							self.model.SelectedGRNIds = [];
							self.model.selectAllRows = false;
							$rootScope.stopLoader();
						}, function(err) {
							growl.error(err.data.message);
							$rootScope.stopLoader();
						});

				var screenData = {
					searchrequest : {
						"fromDate" : moment(self.model.fromDate).format(
								'YYYY-MM-DD'),
						"toDate" : moment(self.model.toDate).format(
								'YYYY-MM-DD'),
						"vendorName" : self.model.vendorName,
						"receiptNo" : self.model.receiptNo
					},
					screenName : 'grn'
				}
				screenDataService.setScreenData(screenData);
			}
		}

		var NavigateToDetailsSettled = function(grn) {
			commonDetailService.setDataId(grn.grnid);
			$state.go('detailsSettle');
		}

		$scope.paymentModes = {
			1 : "Cash",
			2 : "Cheque",
			3 : "M-Pesa",
			8 : "E - Transfer"
		}

		$scope.setPaymentModeId = function(paymentModeId) {
			console.log(paymentModeId)
			$scope.paymentDetails.paymentModeId = paymentModeId;
		}

		$scope.listPaymentDetails = [];
		$scope.totalPaidAmountByPaymentMode = 0.0;
		$scope.addPaymentDetails = function(scope) {
			// $scope.calculateTotalDueRefAmount();
			var amount = 0;
			angular.forEach($scope.listPaymentDetails, function(value, key) {
				console.log(value)
				console.log(value.amount)
				amount = amount + parseFloat(value.amount);
				console.log(amount)
			});

			switch ($scope.paymentDetails.paymentModeId) {
			case 1:
				console.log($scope.paymentDetails.cashAmount)
				if (typeof ($scope.paymentDetails.cashAmount) == "undefined"
						|| $scope.paymentDetails.cashAmount == ""
						|| $scope.paymentDetails.cashAmount == parseFloat(0)
						|| $scope.paymentDetails.cashAmount < parseFloat(0)) {
					growl.error("Please insert an amount", {
						title : ERROR_MSG
					});
					return;
				}

				if ($scope.paymentDetails.cashAmount != null) {
					if(parseInt($scope.paymentDetails.cashAmount) > parseInt(	$scope.dueAmount))
					{
							growl.error(
								"Please insert less than or equal to due amount", {
									title : ERROR_MSG
								});
								return;
					}
					var dueamt = $scope.fundAmount
							- $scope.paymentDetails.cashAmount;
					$scope.totalPaidAmountByPaymentMode = $scope.totalPaidAmountByPaymentMode
							+ parseFloat($scope.paymentDetails.cashAmount);
					$scope.paymentDetails.chequeAmount = dueamt;
					$scope.eTransferObj.transferAmount = dueamt;
					$scope.fundAmount = dueamt;
					$scope.dueAmount = dueamt;
					// $scope.amountPaid = dueamt;
					$scope.amountPaid = $scope.totalPaidAmountByPaymentMode;
					if (dueamt < 0) {
						growl.error(
								"Please insert less than or equal to amount", {
									title : ERROR_MSG
								});
						$scope.paymentDetails.cashAmount = dueamt;
						$scope.paymentDetails.chequeAmount = dueamt;
						$scope.eTransferObj.transferAmount = dueamt;
						$scope.fundAmount = $scope.totalamount;
						$scope.totalAmount = 0;
						$scope.amountPaid = 0;
						$scope.dueAmount = 0;
						// $scope.setSecondTabLevel(1);
						$scope.setPaymentModeId(1);
						return;
					}
					var paymentDetails = {
						paymentModeId : $scope.paymentDetails.paymentModeId,
						amount : parseFloat($scope.paymentDetails.cashAmount)
								.toFixed(2)
					};
					$scope.listPaymentDetails.push(paymentDetails);
					$scope.paymentDetails.cashAmount = dueamt;
					console.log($scope.listPaymentDetails)
					// $scope.paymentDetails.cashAmount = dueamt;
					// $scope.setSecondTabLevel(1);
					$scope.setPaymentModeId(1);
					if (dueamt == 0) {
						$scope.paymentDetails.cashAmount = 0;
						$scope.paymentDetails.chequeAmount = 0;
						$scope.eTransferObj.transferAmount = 0;
					}
				}
				break;
			case 2:
				if (typeof ($scope.paymentDetails.chequeAmount) == "undefined"
						|| $scope.paymentDetails.chequeAmount == ""
						|| $scope.paymentDetails.bankId == 0
						|| $scope.paymentDetails.chequeNumber == ""
						|| typeof ($scope.paymentDetails.chequeNumber) == "undefined"
						|| typeof ($scope.paymentDetails.chequeDate) == "undefined"
						|| $scope.paymentDetails.chequeDate == ""
						|| $scope.paymentDetails.chequeAmount == parseFloat(0)
						|| $scope.paymentDetails.chequeAmount < parseFloat(0)) {
					growl
							.error(
									"Please select bank, chequeno, cheque date and amount",
									{
										title : ERROR_MSG
									});
					return;
				}

				var bankName = "";
				// Get Bank Name
				angular.forEach($scope.bankList, function(value, key) {
					if ($scope.paymentDetails.bankId == value.bankId)
						bankName = value.bankName;
				});

				if ($scope.paymentDetails.chequeAmount != null) {
					if(parseInt($scope.paymentDetails.chequeAmount) > parseInt(	$scope.dueAmount))
					{
							growl.error(
								"Please insert less than or equal to due amount", {
									title : ERROR_MSG
								});
								return;
					}
					var dueamt = $scope.fundAmount
							- $scope.paymentDetails.chequeAmount;
					$scope.totalPaidAmountByPaymentMode = $scope.totalPaidAmountByPaymentMode
							+ parseFloat($scope.paymentDetails.chequeAmount);
					// $scope.paymentDetails.cashAmount = dueamt;
					$scope.eTransferObj.transferAmount = dueamt;
					$scope.fundAmount = dueamt;
					$scope.dueAmount = dueamt;
					$scope.amountPaid = $scope.totalPaidAmountByPaymentMode;
					if (dueamt < 0) {
						growl.error(
								"Please insert less than or equal to amount", {
									title : ERROR_MSG
								});
						$scope.paymentDetails = [];
						$scope.paymentDetails.cashAmount = $scope.totalAmount;
						$scope.paymentDetails.chequeAmount = $scope.totalAmount;
						$scope.eTransferObj.transferAmount = $scope.totalAmount;
						$scope.fundAmount = $scope.totalamount;
						$scope.totalAmount = 0;
						$scope.amountPaid = 0;
						$scope.dueAmount = 0;
						// $scope.setSecondTabLevel(2);
						$scope.setPaymentModeId(2);
						return;
					}
					var chequeDate = $rootScope.getFormattedDate(
							$scope.paymentDetails.chequeDate, false);
					var paymentDetails = {
						paymentModeId : $scope.paymentDetails.paymentModeId,
						amount : parseFloat($scope.paymentDetails.chequeAmount)
								.toFixed(2),
						createdBy : $scope.userId,
						bankId : $scope.paymentDetails.bankId,
						chequeNumber : $scope.paymentDetails.chequeNumber,
						chequeDate : chequeDate,
						bankName : bankName
					};// , bankName:bankName
					$scope.listPaymentDetails.push(paymentDetails);
					// $scope.paymentDetails.chequeAmount = dueamt;
					// $scope.setSecondTabLevel(2);
					$scope.setPaymentModeId(2);
					if (dueamt == 0) {
						$scope.paymentDetails.cashAmount = 0;
						$scope.paymentDetails.chequeAmount = 0;
						$scope.eTransferObj.transferAmount = 0;
					}
				}
				break;

			case 8:
				if (typeof ($scope.eTransferObj.transactionobj == undefined
						|| $scope.eTransferObj.transactionobj == "" || $scope.eTransferObj.transferAmount) == "undefined"
						|| $scope.eTransferObj.transferAmount == ""
						|| $scope.eTransferObj.transactionobj.id == 0
						|| $scope.eTransferObj.transactionNo == 0
						|| $scope.eTransferObj.transactionNo == undefined
						|| $scope.eTransferObj.transBankId == ""
						|| $scope.eTransferObj.transBankId == 0
						|| $scope.eTransferObj.transactionNo == 0
						|| $scope.eTransferObj.transactionNo == ""
						|| typeof ($scope.eTransferObj.transTransactionDate) == "undefined"
						|| $scope.eTransferObj.transTransactionDate == "") {
					growl
							.error(
									"Please select Name, card type,card number expiry date and amount",
									{
										title : ERROR_MSG
									});
					return;
				}

				var bankName = "";
				// Get Bank Name
				angular.forEach($scope.bankList, function(value, key) {
					if ($scope.paymentDetails.transBankId == value.bankId)
						bankName = value.bankName;
				});
				if(parseInt( $scope.eTransferObj.transferAmount) > parseInt(	$scope.dueAmount))
					{
							growl.error(
								"Please insert less than or equal to due amount", {
									title : ERROR_MSG
								});
								return;
					}
				var dueamt = $scope.fundAmount
						- $scope.eTransferObj.transferAmount;
				$scope.totalPaidAmountByPaymentMode = $scope.totalPaidAmountByPaymentMode
						+ parseFloat($scope.eTransferObj.transferAmount);
				// $scope.paymentDetails.cashAmount = dueamt;
				// $scope.paymentDetails.chequeAmount = dueamt;
				$scope.fundAmount = dueamt;
				$scope.dueAmount = dueamt;
				$scope.amountPaid = $scope.totalPaidAmountByPaymentMode;

				if (dueamt < 0) {
					growl.error("Please insert less than or equal to amount", {
						title : ERROR_MSG
					});
					$scope.paymentDetails = [];
					$scope.paymentDetails.cashAmount = $scope.totalAmount;
					$scope.paymentDetails.chequeAmount = $scope.totalAmount;
					$scope.eTransferObj.transferAmount = $scope.totalAmount;
					$scope.fundAmount = $scope.totalamount;
					$scope.totalAmount = 0;
					$scope.amountPaid = 0;
					$scope.dueAmount = 0;
					// $scope.setSecondTabLevel(4);
					$scope.setPaymentModeId(8);
					return;
				}
				var paymentDetails = {
					paymentModeId : $scope.paymentDetails.paymentModeId,
					amount : parseFloat($scope.eTransferObj.transferAmount)
							.toFixed(2),
					transactionTypeId : $scope.eTransferObj.transactionobj.id,
					transTransactionDate : moment(
							$scope.eTransferObj.transTransactionDate).format(
							'DD-MM-YYYY HH:mm:ss'),
					bankId : $scope.eTransferObj.transBankId.bankId,
					transactionNo : $scope.eTransferObj.transactionNo,
					createdBy : $scope.userId
				};
				$scope.listPaymentDetails.push(paymentDetails);
				console.log(paymentDetails)
				// $scope.setSecondTabLevel(4);
				$scope.setPaymentModeId(8);
				if (dueamt == 0) {
					$scope.paymentDetails.cashAmount = 0;
					$scope.paymentDetails.chequeAmount = 0;
					$scope.eTransferObj.transferAmount = 0;
				}
				// $scope.eTransferObj.transferAmount = dueamt;
				break;
			}
			if($scope.listPaymentDetails.length == 0){
				$scope.flag = false;
			}
			else{
				$scope.flag = true;
			}

			// calculate due amount by page
			// switch($scope.page){
			// case "billList":
			// $scope.calculateTotalDueRefAmount();
			// break;
			// case "companyInvoiceSettlement":
			// $scope.validCollectedAmount($scope.listPaymentDetails.length-1);
			// break;
			// }
			// $scope.initPaymentDetailsObj();

			// $scope.paymentDetails.cashAmount=
			// ($scope.fundAmount-$scope.totalamount);
			// $scope.paymentDetails.chequeAmount=($scope.fundAmount-$scope.totalamount);
			// $scope.eTransferObj.transferAmount=($scope.fundAmount-$scope.totalamount);
		}

		// calculate ammount
		$scope.totalAmount;
		$scope.calculateTotalDueRefAmount = function() {
			$scope.totalAmount = 0;
			angular.forEach($scope.listPaymentDetails, function(value, key) {

				$scope.totalAmount = $scope.totalAmount
						+ parseFloat(value.amount);
				$scope.totalAmount = $rootScope
						.parseFloatNum($scope.totalAmount);
			});

			console.log("final Amount ---"
					+ $rootScope.parseFloatNum($scope.totalAmount))

			$scope.fundAmount = $scope.totalAmount;
		};

		$scope.removePaymentDetails = function(payment, index) {
			$scope.listPaymentDetails.splice(index, 1);
			$scope.paymentamount = parseInt(payment.amount);
			/*
			 * $scope.paymentDetails.cashAmount =
			 * ($scope.paymentDetails.cashAmount + $scope.paymentamount);
			 * $scope.paymentDetails.chequeAmount =
			 * ($scope.paymentDetails.chequeAmount + $scope.paymentamount);
			 * $scope.eTransferObj.transferAmount =
			 * ($scope.eTransferObj.transferAmount + $scope.paymentamount);
			 */

			if (payment.paymentModeId == 1) {
				$scope.paymentDetails.cashAmount =$scope.paymentDetails.cashAmount+ parseFloat(payment.amount);
			} else if (payment.paymentModeId == 2) {
				$scope.paymentDetails.chequeAmount = $scope.paymentDetails.chequeAmount+parseFloat(payment.amount);
			} else if (payment.paymentModeId == 8) {
				$scope.eTransferObj.transferAmount = $scope.eTransferObj.transferAmount+parseFloat(payment.amount);
			}
			// $scope.dueAmount = $scope.totalamount;
			// $scope.amountPaid = $scope.totalamount;
			$scope.totalPaidAmountByPaymentMode = $scope.totalPaidAmountByPaymentMode
					- parseFloat(payment.amount);
			$scope.fundAmount = $scope.totalamount
					- parseFloat($scope.totalPaidAmountByPaymentMode)
			$scope.dueAmount = $scope.totalamount
					- parseFloat($scope.totalPaidAmountByPaymentMode);
			$scope.amountPaid = $scope.totalPaidAmountByPaymentMode;
			if($scope.listPaymentDetails.length == 0){
				$scope.flag = false;
			}
			else{
				$scope.flag = true;
			}
		}

		$scope.datepicker = {
			opened : false
		};

		$scope.chequeDateOptions = {
			formatYear : 'yyyy',
			showWeeks : false,
			maxDate : new Date(),
			minDate : moment(new Date()).subtract(90, 'days').toDate(),
		};

		$scope.dateOptionsTrancs = {
			formatYear : 'yyyy',
			showWeeks : false,
			minDate : new Date()
		};

		$scope.openDatePicker = function() {
			$scope.datepicker.opened = true;
		};

		$scope.getBankList = function() {
			var obj = {
				"unitId" : $scope.unitId,
				"orgnisationId" : $scope.organizationId
			}
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + API + BILLING + "/bankMaster";
			BillingGenericService.serviceAction(METHOD_POST, URI, obj,
					NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success") {
					$scope.bankList = response.data.listObject;
				}
			});
		}
		$scope.getBankList();
		$scope.transactionTypesList = [];
		$scope.getTransactionTypes = function() {
			var URI = BASE_URL + ROOT_URL + API + BILLING
					+ "/transactiontypes/" + $scope.organizationId + "/"
					+ $scope.unitId;
			BillingGenericService.serviceAction(METHOD_GET, URI, null,
					NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success") {
					$scope.transactionTypesList = response.data.listObject;
				}
			});
		}
		$scope.getTransactionTypes();

		$scope.CalculateAmount = function(itemList) {
			$scope.creditNoteUsesList=[];
			$scope.totalCreditNoteAmtUse=0;
			$scope.totalPaidAmountByPaymentMode = 0.0;
			$scope.paymentDetails.cashAmount1=0.0;
			itemList
					.forEach(function(data) {
						if (data.isSelected == true) {
							if ($scope.paymentDetails.cashAmount == 0 || $scope.paymentDetails.cashAmount !=0) {
								$scope.paymentDetails.cashAmount1 += data.balanceAmount;
								$scope.paymentDetails.cashAmount = $scope.paymentDetails.cashAmount1
										.toFixed(2)
								$scope.paymentDetails.chequeAmount = $scope.paymentDetails.cashAmount1
										.toFixed(2);
								$scope.eTransferObj.transferAmount = $scope.paymentDetails.cashAmount1
										.toFixed(2);
								$scope.totalamount = $scope.paymentDetails.cashAmount;
								$scope.fundAmount = $scope.totalamount;
								$scope.dueAmount = $scope.paymentDetails.cashAmount;
								$scope.amountPaid = $scope.paymentDetails.cashAmount;
							}
						}
					});
			GetCreditNoteAmount(itemList);
		}

		$scope.disableFlag = true;
		var selectAllCN = function() {
			if ($scope.isCheck == true) {
				$scope.disableFlag = false;
			} else {
				$scope.disableFlag = true;
				self.model.creditNoteAmountList.forEach(function(value,index) {
				       value.isSelectItem=false;
                        selectCNItem(value,index);
                                										});
			}
		}
		$scope.totalCreditNoteAmtUse = 0;
		$scope.creditNoteUsesList = [];
		// $scope.totalamountCn = $scope.totalamount;
		$scope.flag = false;
		$scope.count = 0;
		var selectCNItem = function(info, index) {
			if (info.isSelectItem == true) {
				if($scope.dueAmount==0)
					{
					info.isSelectItem=false;
					return false;
					}
				$scope.totalPaidAmountByPaymentMode=0;
				if(parseInt($scope.dueAmount) > parseInt(info.balance)){   
					$scope.amountCalc = parseFloat($scope.dueAmount) - parseFloat(info.balance);
					$scope.amountCalc=$scope.amountCalc.toFixed(2);
					$scope.totalCreditNoteAmtUse = parseFloat($scope.totalCreditNoteAmtUse)
							+ parseFloat(info.balance);
					info.consumenetamount = info.balance;
					$scope.totalPaidAmountByPaymentMode+=parseFloat(info.consumenetamount);
					info.balance = 0;
					$scope.fundAmount = $scope.amountCalc;
					$scope.dueAmount = $scope.amountCalc;
					$scope.paymentDetails.cashAmount = $scope.dueAmount;
					$scope.paymentDetails.chequeAmount = $scope.dueAmount;
					$scope.eTransferObj.transferAmount = $scope.dueAmount;
				}
				else{
					$scope.amountCalc = parseFloat(info.balance) - parseFloat($scope.dueAmount);
					$scope.amountCalc=$scope.amountCalc.toFixed(2);
					$scope.totalCreditNoteAmtUse = parseFloat($scope.totalCreditNoteAmtUse)
						+ (parseFloat(info.balance)-$scope.amountCalc);
					info.balance = $scope.amountCalc;
					info.consumenetamount = info.creditnetamount-info.balance;
					$scope.fundAmount = 0;
					$scope.dueAmount = 0;
					$scope.totalPaidAmountByPaymentMode += parseFloat($scope.totalCreditNoteAmtUse);
					if($scope.listPaymentDetails!=undefined && $scope.listPaymentDetails!=null && $scope.listPaymentDetails.length>0)
                    {
                    angular.forEach($scope.listPaymentDetails, function(value, key) {
                         $scope.totalPaidAmountByPaymentMode +=parseFloat(value.amount);
                    })
                    }
					$scope.paymentDetails.cashAmount = $scope.dueAmount;
					$scope.paymentDetails.chequeAmount = $scope.dueAmount;
					$scope.eTransferObj.transferAmount = $scope.dueAmount;
				}
				$scope.creditNoteUsesList.push(info);
				// $scope.totalamount = $scope.amountCalc;
			} else {
				if(info.balance == 0){	
					$scope.amountCalc = parseFloat($scope.amountCalc)
							+ parseFloat(info.availablenetamount);
					var checkDueAmount=(parseFloat(info.balanceAmt)-parseFloat(info.balance));
					$scope.totalPaidAmountByPaymentMode = parseFloat($scope.totalPaidAmountByPaymentMode)-parseFloat(checkDueAmount);
					$scope.amountCalc=$scope.paymentDetails.cashAmount1-$scope.totalPaidAmountByPaymentMode;
				}
				else{
					$scope.amountCalc = parseFloat(info.availablenetamount)
						- parseFloat($scope.amountCalc);
					var checkDueAmount=(parseFloat(info.balanceAmt)-parseFloat(info.balance));
					$scope.totalPaidAmountByPaymentMode = parseFloat($scope.totalPaidAmountByPaymentMode)-parseFloat(checkDueAmount);
					$scope.amountCalc=$scope.paymentDetails.cashAmount1-$scope.totalPaidAmountByPaymentMode;
				}
				$scope.paymentDetails.cashAmount = $scope.amountCalc;
				$scope.totalamountCn = $scope.amountCalc;
				$scope.fundAmount = $scope.amountCalc;
				$scope.dueAmount = $scope.amountCalc;
				info.balance = info.availablenetamount;
				$scope.totalCreditNoteAmtUse = parseFloat($scope.totalCreditNoteAmtUse)
						- parseFloat(info.consumenetamount);
				info.consumenetamount = 0;
				if($scope.totalCreditNoteAmtUse<0)
					{
					$scope.totalCreditNoteAmtUse=0;
					}
				$scope.count = 0;
				$scope.creditNoteUsesList.splice(index, 1);
			}
			
			angular.forEach(self.model.creditNoteAmountList, function(c){
				if(c.isSelectItem){
					$scope.count++;
				}
			});
			if($scope.count != 0){
				$scope.flag = true;
			}
			else{
				$scope.flag = false;
			}
		}

		var GetCreditNoteAmount = function(itemList) {
			self.model.creditNoteAmountList = [];
			$scope.totalCNAmount = 0;
			angular.forEach(itemList, function(data) {
				if (data.isSelected == true) {
					if (data.vendorId) {
						$scope.vendorId = data.vendorId;
					} else {
						$scope.vendorId = self.model.vendorId;
					}
				}
			});

			var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_INWARDS.GET_CREDIT_NOTE_AMOUNT_API+ $scope.vendorId;
			$rootScope.startLoaderPopup();
			GenericService
					.serviceAction("GET", URI, {})
					.then(
							function(response) {
								angular
										.forEach(
												response.data,
												function(item) {
													angular
															.forEach(
																	item.rgDetails,
																	function(
																			rgItems) {
																		rgItems.inwordno = item.inwordno;
																		rgItems.mateinwardid = item.mateinwardid;
																		rgItems.replacenumber = item.replacenumber;
																		rgItems.creditnoteamount = item.creditnoteamount;
																		rgItems.consumenetamount = 0;
																		rgItems.availablenetamount = item.availablenetamount;
																		rgItems.creditnetamount = item.creditnetamount;
																		if (item.creditnetamount == undefined
																				|| item.availablenetamount == undefined) {
																			item.creditnetamount = 0;
																			item.availablenetamount = 0;
																		}
																		rgItems.balance = item.availablenetamount;
																		rgItems.balanceAmt = rgItems.balance;
																		if (rgItems.availablenetamount != 0
																				&& rgItems.availablenetamount != undefined) {
																			$scope.totalCNAmount += item.creditnetamount;
																			self.model.creditNoteAmountList
																					.push(rgItems);
																			console
																					.log(
																							"creditNoteAmountList................",
																							self.model.creditNoteAmountList);
																		}
																	});
												});
								var result = [];
								$.each(self.model.creditNoteAmountList, function (i, e) {
								    var matchingItems = $.grep(result, function (item) {
								       return item.mateinwardid === e.mateinwardid;
								    });
								    if (matchingItems.length === 0){
								        result.push(e);
								    }
								});
								self.model.creditNoteAmountList=result;
								$rootScope.stopLoaderPopup();
							}, function(err) {
								$rootScope.stopLoaderPopup();
							});
		}

		$scope.settleBill = function() {
			$('#paymentModePopup').modal("hide");
			$scope.flag = false;
			if (quantityValidation()) {
				self.model.creditNoteAmountListData = [];
				var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN_SETTLEMENT.SETTLE_BILL_API;
				if ($scope.creditNoteUsesList != undefined) {
					$scope.creditNoteUsesList.forEach(function(CNItem) {
						var CNobj = {
							"mateinwardid" : '',
							"creditnetamount" : '',
							"availablenetamount" : '',
							"balance" : ''
						}
						CNobj.mateinwardid = CNItem.mateinwardid;
						CNobj.creditnetamount = CNItem.creditnetamount;
						CNobj.availablenetamount = CNItem.availablenetamount;
						CNobj.balance = CNItem.balance;
						self.model.creditNoteAmountListData.push(CNobj);
					});
				}
				var billListObj = {
					"orgnizationId" : $scope.organizationId,
					"unitId" : $scope.unitId,
					"amountData" : $scope.totalPaidAmountByPaymentMode,
					"totalCreditNoteAmtUse" : $scope.totalCreditNoteAmtUse,
					"paymentReceiptDetailsDtosList" : [],
					"amountReceivedFrom" : $scope.amountReceivedFrom,
					"identificaitonNumber" : $scope.identificaitonNumber,
					"creditNoteAmountList" : self.model.creditNoteAmountListData ? self.model.creditNoteAmountListData
							: [],
					"userId": $scope.userId
				}
				$scope.totalBalanceAmount=0;
				self.model.grnSettlementList
				.forEach(function(item) {
					if(item.isSelected!=undefined && item.isSelected!=null && item.isSelected==true)
						{
					$scope.totalBalanceAmount=$scope.totalBalanceAmount+item.balanceAmount;
						}
				});
				$scope.totalBalanceAmount=$scope.totalBalanceAmount-($scope.totalCreditNoteAmtUse!=undefined && $scope.totalCreditNoteAmtUse!=null ? $scope.totalCreditNoteAmtUse:0)
				self.model.grnSettlementList
						.forEach(function(item) {
							if (item.isSelected == true) {
								if ($scope.listPaymentDetails.length) {
									angular
											.forEach(
													$scope.listPaymentDetails,
													function(data) {
														if (data.paymentModeId == 1) {
															var obj = {
																grnId : '',
																vendorId : '',
																paymentModeId : '',
																amount : '',
																createdBy : ''
															}
															var per=(data.amount*100)/$scope.totalBalanceAmount;
															var amount=((item.balanceAmount-($scope.totalCreditNoteAmtUse!=undefined && $scope.totalCreditNoteAmtUse!=null ? $scope.totalCreditNoteAmtUse:0)) * (per/100)).toFixed(2)
															obj.grnId = item.id;
															obj.vendorId = item.vendorId;
															obj.paymentModeId = data.paymentModeId;
															obj.amount = amount;
															obj.createdBy = $scope.userId;
															billListObj.paymentReceiptDetailsDtosList
																	.push(obj);
														}
														if (data.paymentModeId == 2) {
															var obj2 = {
																grnId : '',
																vendorId : '',
																paymentModeId : '',
																amount : '',
																createdBy : ''
															}
															var per=(data.amount*100)/$scope.totalBalanceAmount;
															var amount=((item.balanceAmount-($scope.totalCreditNoteAmtUse!=undefined && $scope.totalCreditNoteAmtUse!=null ? $scope.totalCreditNoteAmtUse:0)) * (per/100)).toFixed(2)
															obj2.grnId = item.id;
															obj2.vendorId = item.vendorId;
															obj2.paymentModeId = data.paymentModeId;
															obj2.bankId = data.bankId;
															// obj2.bankName =
															// data.bankName;
															obj2.chequeNumber = data.chequeNumber;
															obj2.chequeDate = new Date(
																	data.chequeDate);
															obj2.amount = amount;
															obj2.createdBy = $scope.userId;
															billListObj.paymentReceiptDetailsDtosList
																	.push(obj2);
														}
														if (data.paymentModeId == 8) {
															var obj1 = {
																grnId : '',
																vendorId : '',
																paymentModeId : '',
																amount : '',
																transactionType : '',
																transactionTypeId : '',
																transTransactionDate : '',
																bankId : '',
																transactionNo : '',
																createdBy : '',
																bankName : ''
															}
															var per=(data.amount*100)/$scope.totalBalanceAmount;
															var amount=((item.balanceAmount-($scope.totalCreditNoteAmtUse!=undefined && $scope.totalCreditNoteAmtUse!=null ? $scope.totalCreditNoteAmtUse:0)) * (per/100)).toFixed(2)
															obj1.grnId = item.id;
															obj1.vendorId = item.vendorId;
															obj1.paymentModeId = data.paymentModeId;
															obj1.amount = amount;
															// obj1.transactionType
															// =
															// data.transactionobj.name;
															obj1.transactionTypeId = data.transactionTypeId;
															obj1.transTransactionDate = new Date(
																	data.transTransactionDate);
															obj1.bankId = data.bankId;
															obj1.transactionNo = data.transactionNo;
															obj1.createdBy = $scope.userId;
															billListObj.paymentReceiptDetailsDtosList
																	.push(obj1);
														}
													});
								}
								if ($scope.isCheck == true) {
									// self.model.creditNoteAmountList.forEach(function(CNItem1){
									// if(CNItem1.isSelectItem == true){
									var obj3 = {
										grnId : '',
										vendorId : '',
										paymentModeId : '',
										amount : '',
										createdBy : ''
									}
									obj3.grnId = item.id;
									obj3.vendorId = item.vendorId;
									obj3.paymentModeId = 9;
									obj3.amount = $scope.totalCreditNoteAmtUse;
									obj3.createdBy = $scope.userId;
									if ($scope.creditNoteUsesList.length > 0) {
										billListObj.paymentReceiptDetailsDtosList
												.push(obj3);
									}

									// }
									// });
								}
							}
						});
				$rootScope.startLoaderPopup();
				BillingGenericService
						.serviceAction(METHOD_POST, URI, billListObj,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									if (response.data.message == "Successfully") {
										$rootScope.stopLoaderPopup();
												$scope.paymentDetails.cashAmount = '',
												$scope.paymentDetails.bankId = '',
												$scope.paymentDetails.chequeNumber = '',
												$scope.paymentDetails.chequeDate = '',
												$scope.paymentDetails.chequeAmount = '',
												$scope.eTransferObj.transactionobj = '',
												$scope.eTransferObj.transBankId = '',
												$scope.eTransferObj.transactionNo = '',
												$scope.eTransferObj.transTransactionDate = '',
												$scope.eTransferObj.transferAmount = '',
												$scope.listPaymentDetails = [];
										$scope.settleBtn = true;
										$scope.flag = true;
										$scope.totalamount = '';
										$scope.dueAmount = '';
										$scope.amountPaid = '';
										growl
												.success("Bill Settlement Successfully!!!");
										$('#paymentModePopup').modal("hide");
										GetGRNSettlementList();
										// $state.reload('grnSettlement');
									}else if(response.data.message == "Already settled"){
									    growl.error("This GRN is already settled by other user");
									    $rootScope.stopLoaderPopup();
									}else {
										$rootScope.stopLoaderPopup();
									}
								});
			}
		}

		var quantityValidation = function() {
			var isValid = true;
			if ($scope.paymentDetails.paymentModeId == 1) {
				if ($scope.paymentDetails.cashAmount > $scope.totalAmount) {
					growl.warning("Amount is greater than Net Amount");
					isValid = false;
				}
			}

			if ($scope.paymentDetails.paymentModeId == 2) {
				if ($scope.paymentDetails.chequeAmount > $scope.totalAmount) {
					growl.warning("Amount is greater than Net Amount");
					isValid = false;
				}
			}

			if ($scope.paymentDetails.paymentModeId == 3) {
				if ($scope.mpesaObject.mpesaAmount > $scope.totalAmount) {
					growl.warning("Amount is greater than Net Amount");
					isValid = false;
				}
			}

			if ($scope.paymentDetails.paymentModeId == 8) {
				if ($scope.eTransferObj.transferAmount > $scope.totalAmount) {
					growl.warning("Amount is greater than Net Amount");
					isValid = false;
				}
			}

			return isValid;
		}

		var closePopup = function() {
			$('#paymentModePopup').modal("hide");
			$scope.paymentDetails.cashAmount = 0;
			$scope.paymentDetails.cashAmount1 = 0;
			$scope.paymentDetails.chequeAmount = 0;
			$scope.mpesaObject.mpesaAmount = 0;
			$scope.eTransferObj.transferAmount = 0;
			$scope.listPaymentDetails = [];
		}
		
		/** *Priyanka* */
		$scope.getDepositDetails = function(depositMasterId)
		{
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + "/api/billing/unit/GRN/details/"+$scope.organizationId+"/"+$scope.userId+"/"+depositMasterId;
			//var URI = CONSTANTS.PROCUREMENT.QUOTATION.GRN_SETTLEMENT.SEARCH_GRN_SETTLED_API;
			BillingGenericService.serviceAction(METHOD_GET,URI, {},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
			{
				$scope.depositDetailsList = [];
				console.log("response",response);
				if (response.data.status == "success")
					{
					 $scope.depositDetailsList = response.data.listObject;
					 $('#payment_mode_Modal_deposit').modal('show');
					  $rootScope.stopLoader();
					}
				else{
					 $rootScope.stopLoader();
				   }
			});
		}
		/** ** */
		
		/** *Chetana* */
		var printGRNSettlementReport = function(grn) {
			if (grn.id != null || grn.id != undefined) {
				var data = {
					"appName" : "HIS",
					"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
					"reportDesignFilePath" : "get_GRN_Settlement.rpt",
					"reportFormat" : "PDF",
					"exportFileName" : "GRNSettlement",
					"reportParams" : [ {
						"paramName" : "Id",
						"paramType" : "Integer",
						"paramValue" : grn.id
					} ]
				};
			} else {
				var data = {
					"appName" : "HIS",
					"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
					"reportDesignFilePath" : "get_GRN_Settlement.rpt",
					"reportFormat" : "PDF",
					"exportFileName" : "GRNSettlement",
					"reportParams" : [ {
						"paramName" : "Id",
						"paramType" : "Integer",
						"paramValue" : grn.grnid
					} ]
				};
			}
			reportService.generate(REPORT_API_URI1, data);
		}
		/** ** */
		initializeController();
	}

	function config($stateProvider) {
		$stateProvider.state("grnSettlement", {
			url : "/grnSettlement",
			templateUrl : 'views/procurement/quotation/GRN-Settlement.html',
			controller : "grnSettlement.Controller",
			title : 'Good Receipt Note Settlement',
			resolve : {

				data : function($q, $state, $timeout) {
					var deferred = $q.defer();

					$timeout(function() {
						var arr = JSON.parse(localStorage
								.getItem('permissions')).modules
								.filter(function(item) {
									return (item.id == 845);
								});
						if (arr.length <= 0) {
							$state.go("accessDenied")
							deferred.reject();
						} else {
							deferred.resolve();
						}
					});

					return deferred.promise;
				}
			},
			controllerAs : "vm"
		});
	}

	angular.module("myApp").config(config).controller(
			"grnSettlement.Controller", grnSettlementController);
})();