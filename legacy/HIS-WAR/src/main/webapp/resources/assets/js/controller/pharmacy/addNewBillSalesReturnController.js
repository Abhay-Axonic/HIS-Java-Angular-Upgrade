(function () {
	"use strict";

	function addNewBillSalesReturnController($scope, $rootScope, reportService,$http,  CONSTANTS, GenericService, $filter, growl,
			CurrencyService, $state, StatusService, BillingGenericService, $cookies,stopTransactionService) {
		var self = this;

		//Default Constructor
		var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
		function initializeController() {

			
			$rootScope.loginpage = true;

			$scope.dateOptions = {
				formatYear: 'yyyy',
				showWeeks: false,
                minDate : new Date()
			};
			$scope.openDatePicker = function () {
				$scope.datepicker.opened = true;
			};

			$scope.datepicker = {
				opened: false
			};

			$scope.openDatePickerPatiSearch = function () {
				$scope.datepicker1.opened = true;
			};

			$scope.datepicker1 = {
				opened: false
			};

			$scope.openFromDate = function () {
				$scope.fromDatePicker.opened = true;
			};

			$scope.fromDatePicker = {
				opened: false
			};

			$scope.openToDate = function () {
				$scope.toDatePicker.opened = true;
			};

			$scope.toDatePicker = {
				opened: false
			};

			var cookieObject = $cookies.getObject('cookieObject');
			if (cookieObject == undefined) {
				$state.go('login');
				return;
			}
			
			$scope.dateOptionsTrancs = {
					formatYear : 'yyyy',
					showWeeks : false,
					minDate : new Date()
				};
			
			$scope.dateOptions = {
					formatYear : 'yyyy',
					showWeeks : false
				};
			
			
			 var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
			$scope.unitId = cookieObject.unitId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.storeId=cookieObject.storeId;
			
		      $scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
		      $scope.isAppointmentRequired = cookieObjectAppConfig.isAppointmentRequired;
		      $scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
		      $scope.taxInclusive = cookieObjectAppConfig.taxInclusive;
		      $scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
		      $scope.skipMpesaValidation = false;
		      $scope.isMarkup = cookieObjectAppConfig.isMarkup;
		      $scope.markupUp = cookieObjectAppConfig.markupUp;
			$scope.searchKeyword = "";
			self.model = {
				fromDate: new Date(),
				toDate: new Date(),
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
				salesTypes: [{
					"saleType": "All"
				}],

				isReturnQuantityValid: [],
				isReturnReasonValid: [],
				SelectedRequestItems: [],
				billDetailList: {
					item: []
				}
			};

			//Methods
			CurrencyService.GetCurrency().then(function (currency) {
				self.model.Currency = currency;
			})
			StatusService.GetStatus().then(function (status) {
				self.model.Status = status;
			})
			self.model.OpenFromDate = OpenFromDate;
			self.model.OpenToDate = OpenToDate;
			self.model.SearchpatientNames = SearchpatientNames;
			self.model.SearchUhid = SearchUhid;
			self.model.PatientNameKeyPress = PatientNameKeyPress;
			self.model.uHIDKeyPress = uHIDKeyPress;
			self.model.AddUHID = AddUHID;
			self.model.search = '';
			self.model.prevSearch = '';
			self.model.patientNames = [];
			self.model.GetBillList = GetBillList;
			self.model.GetBillListPopUp = GetBillListPopUp;
			self.model.AddBillToSales = AddBillToSales;
			self.model.SelectBill = SelectBill;
			self.model.SaveBill = SaveBill;
			self.model.ClearFields = ClearFields;
			self.model.SelectAllRequests = SelectAllRequests;
			self.model.SelectSalesReturnItem = SelectSalesReturnItem;
			self.model.NavigatetoSalesReturn = NavigatetoSalesReturn;
			self.model.ValidateBill = ValidateBill;
			self.model.CalculateQuantity = CalculateQuantity;
			self.model.OpenBillListPopUp = OpenBillListPopUp;
			self.model.ValidateSaveButton = ValidateSaveButton;
			self.model.rePrintBillSlip=rePrintBillSlip;
			self.model.removeBill = removeBill;
	        self.model.selectedPatientIds = [];
	        self.model.selectedBillIds = [];
	        self.model.billNo = "";
	        self.model.billDetailListNew =[];
	        self.model.clearList= clearList;
	        self.model.updateBillSalesReturn = updateBillSalesReturn;
	        self.model.totalSelfPaidAmount = 0;
	        
			PopulateSalesTypeDropdown();
			
			/* start code for resolved HIS-10450 After double click on 'Generate Refund' button then bill details showing two times on sales return */
			self.model.generateRefundButtonDisabled = false;
			/* end code for resolved HIS-10450 After double click on 'Generate Refund' button then bill details showing two times on sales return */
			
		}		
		 	        
		 var removeBill = function(item) {      	
	          if(self.model.billDetailListNew.length == 1){
                   $("#billRemove").modal();
	            }else{
	            	  var index = self.model.billDetailListNew.indexOf(item);
	            	self.model.billDetailListNew.splice(index, 1);  
	            	var index1=self.model.SelectedRequestItems.indexOf(item);
	            	self.model.SelectedRequestItems[index1].isSelected = false;
	            }	                          
	     }  

	    var clearList = function () {
         self.model.billDetailList = [];
         self.model.billDetailListNew = [];
         self.model.SelectedRequestItems = [];
         self.model.billNo = ''; 
         self.model.totalSelfPaidAmount = 0;
      }  

		var OpenFromDate = function () {
			self.model.fromDateOpened = true;
			self.model.toDateOptions.minDate = self.model.fromDate;
		}

		var OpenToDate = function () {
			self.model.toDateOpened = true;
			self.model.toDateOptions.minDate = self.model.fromDate;
		}

		var SearchpatientNames = function (search) {
			var data={
	        		unitId : $scope.unitId,
	        		firstName : search,
//	        		lastName : search
	        };
			if (search.length < 1) return;
			if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
				self.model.prevSearch = search;
//				var URI = CONSTANTS.GLOBAL.PATIENT_SEARCH_API + $scope.unitId + "/" + search + "/" + self.model.visitType.id;
		        var URI = BASE_URL + ROOT_URL + "/api/pharmacy/op/sale/return/getPatientNameSearchForSalesReturn";
		        $rootScope.startLoader();
				return GenericService.serviceAction("POST", URI, data)
					.then(function (response) {
						$rootScope.stopLoader();
						if (!!response.data.listObject && response.data.listObject.length > 0) {
							self.model.patientNames = response.data.listObject;
							$rootScope.stopLoader();
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
				if (!!self.model.patientNames && self.model.patientNames.length > 0 &&
					self.model.patientNames[0].itemFound != undefined && !self.model.patientNames[0].itemFound) {
					return PatientNameNotFound(search);
				} else {
					return $filter('filter')(self.model.patientNames, {
						$: search
					});
				}
			}
		}

		var AddUHID = function (item) {
			self.model.patientData = item;
			self.model.uhid = item.uhid;
			self.model.patientId = item.id;
			self.model.patientName = item.patientName;
		}

		var PatientNameNotFound = function (search) {
			var item = {
				"itemFound": false,
				"details": "Patient Not found with name " + search
			};
			self.model.patientNames = [item];
			return self.model.patientNames;
		}

		var PatientNameKeyPress = function (event) {
			var search = $filter('filter')(self.model.patientNames, {
				$: self.model.patientName
			});
			if (search.length != 1) {
				self.model.patientData = null;
				self.model.patientId = null;
				self.model.uhid = null;
			} else {
				self.model.patientData = search[0];
				self.model.patientId = search[0].patientId;
				self.model.uhid = search[0].uhid;
			}
		}

		var uHIDKeyPress = function (event) {
			var search = $filter('filter')(self.model.patientNames, {
				$: self.model.uhid
			});
			if (search.length != 1) {
				self.model.patientData = null;
				self.model.patientId = null;
				self.model.patientName = null;
			} else {
				self.model.patientData = search[0];
				self.model.patientId = search[0].patientId;
				self.model.patientName = search[0].patientName;
			}
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
							$rootScope.stopLoader();
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
		
		 var ChangeVisitType = function () {
	            self.model.patientName = null;
	            self.model.uhid = null;
	        }

		 
		var PopulateSalesTypeDropdown = function () {
			var URI = CONSTANTS.GLOBAL.SALE_TYPE_DROPDOWN_API;
			self.model.salesType = self.model.salesTypes[0];

			$rootScope.startLoader();
			GenericService.serviceAction("GET", URI, {}).then(
				function (response) {

					self.model.salesTypes = self.model.salesTypes.concat(response.data);
					self.model.salesTypeNew=[{
						"saleType": "All"
					}];
					angular.forEach(self.model.salesTypes, function (item, key) {
		            	if (item.id==1 || item.id==2 || item.id==3) {
		            		var data = {};
								var data = {
										id: item.id,
										saleType: item.saleType
								}
								self.model.salesTypeNew.push(data);
							}
		            
		                });

				});
			$rootScope.stopLoader();
		}

		var ValidateDate = function () {
			var isValid = true;

			if (self.model.fromDate > self.model.toDate) {
				growl.error('From Date should lesser than To Date');
				isValid = false;
			}

			if (!self.model.fromDate || !self.model.toDate) {
				growl.error('Enter Valid Date');
				isValid = false;
			}

			return isValid;
		}

		var OpenBillListPopUp = function () {
			self.model.salesReturnBillList = [];
			self.model.fromDate = new Date(); 
			self.model.toDate = new Date(); 
			self.model.selectAllRows = false;
			var popup = angular.element('#getBillsModal');
			popup.modal('show');
		}

		var GetBillListPopUp = function () {
			//if (self.model.billDetailList.item && !self.model.billDetailList.item.length)
			//{
				self.model.fromDate = new Date(); 
				self.model.toDate = new Date(); 
				self.model.salesReturnBillList = [];
				self.model.selectAllRows = false;
				GetBillList();
				
				/* start code for resolved HIS-9585 While searching patient details in search field on get bills list is getting reflected on sales returns screen */
				$scope.search='';
				/* end code for resolved HIS-9585 While searching patient details in search field on get bills list is getting reflected on sales returns screen */
				
				var popup = angular.element('#getBillsModal');
				popup.modal('show');
			/*} else {
				var popup = angular.element('#confirmGetBill');
				popup.modal('show');
			}*/
		}

		var GetBillList = function () {
			var bills = self.model;
			if (ValidateDate()) {
				var URI = CONSTANTS.PHARMACY.OP.SALES_RETURN.DETAIL_SEARCH_API;

				var data = {
					"fromDate": moment(bills.fromDate).format('YYYY-MM-DD'),
					"toDate": moment(bills.toDate).format('YYYY-MM-DD'),
					"patientId": !!bills.patientId ? bills.patientId : undefined,
					"patientName": !!bills.patientName ? bills.patientName : undefined,
					"uhid": !!bills.uhid ? bills.uhid : undefined,
					"saleTypeId": !!bills.salesType.id ? bills.salesType.id : 0,
					"storeId" : $scope.storeId,
					"unitId": $scope.unitId
					// "walkInFlag": !! bills.walkinFlag 
				}
				 $rootScope.startLoaderPopup();
				GenericService.serviceAction("POST", URI, data).then(
					function (response) {
						bills.salesReturnBillList = response.data;

						for (let sales of bills.salesReturnBillList) {
							sales.isChecked = false;
							if(sales.saleTypeId==2 || sales.saleTypeId==3  || sales.saleTypeId==1){	 //HIS-16194
								var a = sales.patientDetail.split("|");
								var name = a[0];
								var age = a[1];
								var gender = a[2];
								if(name == " null "){
									name = " - ";
								}
								if(age == " null "){
									age = " - ";
								}
								if(gender == " null "){
									gender = " - ";
								}
								sales.patientDetail = name + "|" + age + "|" + gender;
							}
						}
					});
				 $rootScope.stopLoaderPopup();
			}
		}

		var SelectBill = function (item) {
			//self.model.billId = item.id;
			
			if(item.isChecked)
			{
		     	 var index = self.model.selectedPatientIds.indexOf(item.patientId);
		     	 var data = self.model.selectedPatientIds.includes(item.patientId);		     	 
		     	 
		     	 if(self.model.selectedPatientIds.length > 0 && index == -1 && data == false){
		     		growl.error('Bills selected should be of the same patient.');
		     		item.isChecked = false;
		     		return false;     		
		     	 } else if(index == -1){
		             self.model.selectedPatientIds.push(item.patientId);
		             self.model.selectedBillIds.push(item.id);
		     	 }else if(self.model.selectedPatientIds.length > 0 && index !== -1 && data == true){
		     	 		self.model.selectedBillIds.push(item.id);
		     	 }			   	    
		     }else{
		     	 var index = self.model.selectedPatientIds.indexOf(item.patientId);
		     	 var Billindex = self.model.selectedBillIds.indexOf(item.id);
              if (index > -1) {
                  self.model.selectedPatientIds.splice(index, 1);
                  self.model.selectedBillIds.splice(index, 1);
              }else if(Billindex === -1){
               	 self.model.selectedBillIds.splice(index, 1);
               }
		    }
		}
		
		$scope.addBtnDisabled = false;
		var AddBillToSales = function (salesReturnBillList) {
			self.model.billNo = null;
			
			if($scope.addBtnDisabled == true){
				return false;
			}
			$scope.addBtnDisabled = true;
		   if(self.model.selectedBillIds.length  == 0){
				growl.error('Please select items to add');
				$scope.addBtnDisabled = false;
			}		
			else {
				
				var URI = CONSTANTS.PHARMACY.OP.SALES_RETURN.ADD_BILL_API + self.model.selectedBillIds;

				$rootScope.startLoader();
				GenericService.serviceAction("GET", URI, {}).then(
					function (response) {
						self.model.totalSelfPaidAmount = 0;
						self.model.billDetailList = response.data;
						self.model.billDetailList.forEach(function(bill){
							//self.model.billDetailList = bill;
							angular.forEach(salesReturnBillList, function(sale){
								if((sale.saleTypeId==2 || sale.saleTypeId==3) && sale.isChecked == true){
									bill.patientDetail = sale.patientDetail;
									bill.uhid = sale.uhid;
									if(!bill.patientId)
									{
										bill.patientId=sale.tempPatId;
									}
								}
							});
							if(bill.tempPatientDatail){
								bill.patientDetail = bill.tempPatientDatail;
							}
							
							self.model.totalSelfPaidAmount =  self.model.totalSelfPaidAmount + bill.selfPaidAmount;
							self.model.billNo = self.model.billNo + ","+bill.billNo;
							self.model.billNo =  (self.model.billNo.replace('null,', ''));
							self.model.storeId = bill.storeId;
							bill.item.forEach(function (item) {
								item.actualDiscount=item.discount;
								if(item.quantity == undefined || item.quantity == null || item.quantity == ""){
									item.quantity = 0;
								}
								if(item.isThreeUOMApplicable == false){
									if(item.uomTypeId == 1){
										item.receivedQtyCon = (item.quantity +"*"+ item.uom[0].conversion);
										item.receivedQtyCalculation = (item.quantity * item.uom[0].conversion);
										item.uomUnitData = item.uom[2].uomUnit;
									}
									else if(item.uomTypeId == 2){
									
									}
									else{
										item.receivedQtyCon = (item.quantity);
										item.receivedQtyCalculation = (item.quantity);
										item.uomUnitData = item.uom[2].uomUnit;
									}
								}
								else{
									if(item.uomTypeId == 1){
										item.receivedQtyCon = (item.quantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
										item.receivedQtyCalculation = (item.quantity * item.uom[0].conversion * item.uom[1].conversion);
										item.uomUnitData = item.uom[2].uomUnit;
									}
									else if(item.uomTypeId == 2){
										item.receivedQtyCon = (item.quantity +"*"+ item.uom[1].conversion);
										item.receivedQtyCalculation = (item.quantity * item.uom[1].conversion);
										item.uomUnitData = item.uom[2].uomUnit;
									}
									else{
										item.receivedQtyCon = (item.quantity);
										item.receivedQtyCalculation = (item.quantity);
										item.uomUnitData = item.uom[2].uomUnit;
									}
								}
								self.model.billDetailListNew.push(item);
								if (item.quantityIssued == item.quantityReturned) {
									item.disableCheckbox = true;
								}
								item.uom.find(function(u){
									if(item.uomTypeId == u.uomTypeId){
										item.uomType = u.uomDetails;
									}
								})
							})
						});
						$scope.addBtnDisabled = false;
						self.model.billId = undefined;
						self.model.selectedBillIds = [];
						self.model.selectedPatientIds = [];
					});


				/* start code for resolved HIS-9585 While searching patient details in search field on get bills list is getting reflected on sales returns screen */
				$scope.search='';
				/* end code for resolved HIS-9585 While searching patient details in search field on get bills list is getting reflected on sales returns screen */
				$scope.addBtnDisabled = false;
				var popup = angular.element('#getBillsModal');
				popup.modal('hide');

				$rootScope.stopLoader();
			}
		}

		var CalculateQuantity = function (item, uom) {
			if((parseInt(item.quantityIssued)-parseInt(item.quantityReturned))<parseInt(item.quantity))
				{
				growl.error("Please Enter Valid Return Quantity");
				item.quantity=0;
				return false;
				}
			if (!!uom) {
				var pUom = $filter('filter')(item.uom, {
					uomTypeId: 1
				}, true)[0];

				var sUom = $filter('filter')(item.uom, {
					uomTypeId: 2
				}, true)[0];

				var dUom = $filter('filter')(item.uom, {
					uomTypeId: 3
				}, true)[0];

				if (item.uomTypeId == 1) {
					item.leaseQuantity = pUom.conversion * sUom.conversion * dUom.conversion * item.quantity;
				} else if (item.uomTypeId == 2) {
					item.leaseQuantity = sUom.conversion * dUom.conversion * item.quantity;
				} else if (item.uomTypeId == 3) {
					item.leaseQuantity = dUom.conversion * item.quantity;
				}
			} else {
				item.leaseQuantity = 0;
			}
			
			if(item.patientClassAmount == undefined || item.patientClassAmount == null || item.patientClassAmount == ''){

                item.patientClassAmount = 0;

            }
			item.discount=item.actualDiscount/item.quantityIssued;
			item.discount=parseFloat(item.discount)*parseInt(item.quantity);
            if($scope.taxInclusive == true){

                var calculateTaxAmt =  (item.refundAmount ) / (1 + (item.taxPercentage/100));

                item.returnTaxAmount = ((item.refundAmount) - calculateTaxAmt).toFixed(2);

            }

            else{

                item.returnTaxAmount = ((((item.refundAmount/item.quantityIssued)*item.quantity)/100)*(item.taxPercentage)).toFixed(2);

            }
			// console.log(item.leaseQuantity);
		}

		var ClearFields = function (item) {

			item.quantity = undefined;
			item.reason = undefined;
		}


		var SelectAllRequests = function () {

			if (self.model.selectAllRows) {
				self.model.SelectedRequestItems = [];
				self.model.billDetailListNew.forEach(function (item) {
					if (!item.disableCheckbox) {
						item.isSelected = true;
						self.model.SelectedRequestItems.push(item);
					}
				});
			} else {
				self.model.SelectedRequestItems = [];
				self.model.billDetailListNew.forEach(function (item) {
					item.isSelected = false;
				});
			}
			// console.log(self.model.SelectedRequestItems);
		}
		var isSelected = function (item) {
			return item.isSelected;
		}

		var SelectSalesReturnItem = function (item) {

			var itemIds = self.model.SelectedRequestItems;

			if (item.isSelected) {
				itemIds.push(item);
			} else {
				var index = itemIds.indexOf(item);
				if (index > -1) {
					itemIds.splice(index, 1);
				}
			}

			if (self.model.billDetailListNew.every(isSelected)) {
				self.model.selectAllRows = true;
			} else {
				self.model.selectAllRows = false;
			}
			// console.log(itemIds);
		}


		var NavigatetoSalesReturn = function () {
			$state.go('billSalesReturn');
		}

		var SetStatus = function (x) {
			var status = self.model.Status.find(function (obj) {
				return obj.status == x;
			});

			return status.id;
		}
		
		var SaveBill = function (bill) {
			$scope.flag=true;
			$scope.patientId=bill[0].patientId;
			/* start code for resolved HIS-10450 After double click on 'Generate Refund' button then bill details showing two times on sales return */
			self.model.generateRefundButtonDisabled=true;
			/* end code for resolved HIS-10450 After double click on 'Generate Refund' button then bill details showing two times on sales return */
			
			if (ValidateBill(bill)) {
				
				 ActualSaveBill(bill);
				}  else {
				/* start code for resolved HIS-10450 After double click on 'Generate Refund' button then bill details showing two times on sales return */
				self.model.generateRefundButtonDisabled = false;
				/* end code for resolved HIS-10450 After double click on 'Generate Refund' button then bill details showing two times on sales return */
			}
		}
		
		var ActualSaveBill = function (bill) {
			
				var URI = CONSTANTS.PHARMACY.OP.SALES_RETURN.SAVE_API;
				
				$scope.TotalRefundAmount = 0;

                bill.forEach(function(billItems){
               
                       billItems.item.forEach(function(item){
                       	if(item.isSelected){
                       	   $scope.saleId = billItems.id;
                       	   $scope.TotalRefundAmount =  $scope.TotalRefundAmount + parseFloat(item.refundAmount);
                       	 }
                       });
                });
                
				var reuquestSaleReturnList = [];
				   var newData = {
				   	"storeId": self.model.storeId,	
                    "saleId" : $scope.saleId,
				   	"statusId": SetStatus('Pending'),
				   	"totalRefundAmount": $scope.TotalRefundAmount.toFixed(2),
                	"reuquestSaleReturnList":[],
                	 "unitId":$scope.unitId,
                     "organizationId":$scope.organizationId,
                     "userId":$scope.userId,
                     "selfPayeeId": $scope.selfPayeeId
                }
				bill.forEach(function(billItems){
                	var data = {
					"storeId": self.model.storeId,								
					"statusId": SetStatus('Completed'),
					"saleIds":[billItems.id],
					"item": [],				
				}
			   newData.reuquestSaleReturnList.push(data);
                angular.forEach(billItems.item, function (item, key) {
            	if (item.isSelected) {
						var billItem = {
							"saleDetailId": item.id,
							"returnQuantity": Number(item.quantity),
							"leaseReturnQuantity": item.leaseQuantity,
							"refundAmount": item.refundAmount.toFixed(2),
							"returnTaxAmount":item.returnTaxAmount,
							"reason": item.reason,
							"saleId":billItems.id,
							"taxPercentage": (item.taxPercentage != undefined && item.taxPercentage != null && item.taxPercentage != "") ? item.taxPercentage :item.taxPercentage,
							"discount": item.discount.toFixed(2)
						}
						data.item.push(billItem);
					}
            
                });
                });     
                
				
//				 console.log(bill);
//				 return false;
				 
				self.model.billDataList = newData;
				$rootScope.startLoader();
				GenericService.serviceAction("POST", URI, newData).then(
					function (response) {
						self.model.returnId=response.data.id;
						
						growl.success('Sales return bill Successfully saved');
						
						if($scope.TotalRefundAmount==0){
						   $scope.payObj={};
						   $scope.payObj.amount="0.0";
						   $scope.payObj.createdBy=$scope.userId;
						   $scope.payObj.paymentModeId=1;
						   $scope.listPaymentDetails.push($scope.payObj);
						   $scope.cnMasterObj.saleId=$scope.saleId;
						   $scope.addRefund();
						}else{
							$scope.listPaymentDetails = [];
							self.model.billDetailList = {};
							self.model.selectAllRows = false;
							$scope.showColletPaymentPopup(self.model.billDataList)
						}
						// NavigatetoSalesReturn();
						//start code for HIS-8183 Validation required in sales return details pop up on sales return screen.
					
						//end code for HIS-8183 Validation required in sales return details pop up on sales return screen.
						
						
						
						$rootScope.stopLoader();
						
						/* start code for resolved HIS-10450 After double click on 'Generate Refund' button then bill details showing two times on sales return */
						self.model.generateRefundButtonDisabled = false;
						/* end code for resolved HIS-10450 After double click on 'Generate Refund' button then bill details showing two times on sales return */
					},
					function (err) {
						
						/* start code for resolved HIS-10450 After double click on 'Generate Refund' button then bill details showing two times on sales return */
						self.model.generateRefundButtonDisabled = false;
						/* end code for resolved HIS-10450 After double click on 'Generate Refund' button then bill details showing two times on sales return */
						
						$rootScope.stopLoader();
						growl.error(err.data.message);
					});			
		}

		var ValidateSaveButton = function () {
			var isValid = false;
			if (self.model.billDetailListNew) {
				self.model.billDetailListNew.find(function (item) {
					if (!!item.isSelected) {
						isValid = true;
					}
					 if (item.quantityIssued == item.quantityReturned) {
						item.disableCheckbox = true;
					}
				});

				return isValid;
			}
		}


		var ValidateBill = function (bill) {
			var isValid = true;
			if (!self.model.SelectedRequestItems || self.model.SelectedRequestItems.length <= 0) {
				isValid = false;
				growl.error("Please add some items in the bill");
			} else {
				self.model.SelectedRequestItems.forEach(function (item) {

					if(item.isSelected==true){
					item['isItemValid'] = false;
					item['isReturnQuantityValid'] = false;
					item['isReasonValid'] = false;

					if (!item.quantity || !item.reason) {
						item['isItemValid'] = true;
						isValid = false;
					}

					if (!item.reason) {
						item['isReasonValid'] = true;
						isValid = false;
					}

					if (!item.quantity) {
						item['isReturnQuantityValid'] = true;
						isValid = false;
						growl.error("Please enter quantity");
					}

					if ((item.quantityIssued < (parseInt(item.quantity) + item.quantityReturned))) {
						item['isReturnQuantityValid'] = true;
						isValid = false;
						growl.error("Please enter quantity less than or equal to" + (parseInt(item.quantityIssued) - item.quantityReturned));
					}

					if (item.quantity == 0) {
						item['isReturnQuantityValid'] = true;
						isValid = false;
						growl.error("Please enter quantity greater than zero");
					}
				   }
				});
				self.model.billDetailListNew.totalRefundAmount=self.model.billDetailListNew.totalRefundAmount.toFixed(2);
				// self.model.totalSelfPaidAmount=self.model.totalSelfPaidAmount.toFixed(2);
				if (parseFloat(self.model.billDetailListNew.totalRefundAmount) > parseFloat(self.model.totalSelfPaidAmount)) {
					isValid = false;
					growl.error("Refund Amount must be less than paid Amount.");
				 }

			}

			return isValid;
		}

		$scope.refundCNSearchObj = {
			"fromDate": "",
			"toDate": "",
			"patientName": "",
			"uhidNumber": "",
			"visitTypeId": "",
			"depositTypeId": "",
			"orgId": $scope.organizationId,
			"unitId": $scope.unitId,
			"refundCnNo": "",
			"paymentRefundVoucherNo": ""

		}

		$scope.getBankList = function () {
			// var URI = BASE_URL + ROOT_URL + "/api/billing/bankMaster";
			var URI = CONSTANTS.ROOT_URL + "/api/billing/bankMaster";
			var param = {
				orgnisationId: $scope.organizationId,
				unitId: $scope.unitId
			};
			BillingGenericService.serviceAction(METHOD_POST, URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function (response) {
				if (response.data.status == "success") {
					$scope.bankList = response.data.listObject;
				}
			});
		}

		$scope.getCardTypesList = function () {
			// var URI = BASE_URL + ROOT_URL + "/api/billing/cardTypeMaster";
			var URI = CONSTANTS.ROOT_URL + "/api/billing/cardTypeMaster";
			var param = {
				orgnisationId: $scope.organizationId,
				unitId: $scope.unitId
			};
			BillingGenericService.serviceAction(METHOD_POST, URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function (response) {
				if (response.data.status == "success") {
					$scope.cardTypeList = response.data.listObject;
				}
			});
		}

		$scope.initPaymentDetailsObj = function () {
			$scope.paymentDetails = angular.copy({
				paymentModeId: 1,
				cashAmount: "",
				bankId: "",
				chequeNumber: "",
				chequeDate: "",
				chequeAmount: "",
				credCardHolderName: "",
				credCardTypeId: "",
				credAmount: "",
				credCardNumber: "",
				credExpiryDate: "",
				debCardHolderName: "",
				debCardTypeId: "",
				debAmount: "",
				debCardNumber: "",
				debExpiryDate: "",
				transactionNo : ""
			});
			
			$scope.eTransferObj={
					transBankId:"",
					transferAmount:"",
					transTransactionDate:"",
					transactionNo:"",
					transactionobj:{
							id:"",
							name:""
					}	
			}
		}

		$scope.initPaymentRefundObj = function () {
			$scope.paymentRefundObj = angular.copy({
				paymentRefundVoucherId: '',
				paymentRefundVoucherNo: '',
				dateOfRefund: '',
				isCnOrDeposit: '',
				tRefundCnId: '',
				creditNoteId: '',
				tPharmacyReturnId: '',
				amountRefunded: 0.0,
				patientId: '',
				receivedFrom: '',
				identificationNo: '',
				nameOfPerson: '',
				status: 'A',
				unitId: $scope.unitId,
				orgnisationId: $scope.organizationId,
				createdDate: '',
				createdBy: $scope.userId,
				updatedDate: ''
			})
		};

		$scope.init = function () {
			/*$scope.identificationTypeList = [];*/
			$scope.sexMasterList = [];
			// $scope.getSexMasterList();
			$scope.listPaymentDetails = [];
			/*	$scope.getIdentificationTypeList();*/
			$scope.initPaymentRefundObj();
			$scope.patientDetailsList = [];
			$scope.patientDetails = {};
			$scope.billing = true;
			$scope.deposit = {};
			$scope.paymentDetails = {};
			$scope.initPaymentDetailsObj();
			$scope.listPaymentDetails = [];
			$scope.getBankList();
			$scope.getCardTypesList();
			$scope.dueAmount = parseFloat(0).toFixed(2);
			$scope.depositSearch = {};
			$scope.visitNoList = [];
			$scope.eTransferObj={
					transBankId:"",
					transferAmount:"",
					transTransactionDate:"",
					transactionNo:"",
					transactionobj:{
							id:"",
							name:""
					}	
			}
			
		}


		$scope.calculateDueAmount = function () {
		
			var collectedAmount = parseFloat(0).toFixed();
			angular.forEach($scope.listPaymentDetails, function (value, key) {
				collectedAmount = parseFloat(collectedAmount) + parseFloat(value.amount);
			});
			$scope.dueAmount = $scope.paymentRefundObj.amountRefunded != '' && typeof ($scope.paymentRefundObj.amountRefunded) != 'undefined' ? parseFloat(parseFloat($scope.paymentRefundObj.amountRefunded) - collectedAmount).toFixed(2) : parseFloat(0).toFixed(2);
			$scope.paymentDetails.cashAmount=$scope.dueAmount;
			$scope.paymentDetails.chequeAmount = $scope.dueAmount;
			$scope.paymentDetails.debAmount = $scope.dueAmount;
			$scope.paymentDetails.credAmount = $scope.dueAmount;
			$scope.paymentDetails.loyaltyCardAmt =  $scope.dueAmount;
			$scope.eTransferObj.transferAmount = $scope.dueAmount;
		}

		//remove payment details
		$scope.removePaymentDetails = function (index) {
			$scope.listPaymentDetails.splice(index, 1);
			$scope.calculateDueAmount();
		}

		$scope.parseFloatNum = function (number) {
			return (number != '' && typeof (number) != 'undefined' && !isNaN(number)) ? parseFloat(number).toFixed(2) : parseFloat(0).toFixed(2);
		}

		$scope.cnMasterObj = {};
		$scope.showColletPaymentPopup = function (cnMasterObj) {
			$scope.cnMasterObj = cnMasterObj;
			$scope.paymentRefundObj.amountRefunded = $scope.cnMasterObj.totalRefundAmount;
			$scope.paymentDetails.cashAmount=$scope.cnMasterObj.totalRefundAmount;
			//start code to resolved HIS-9205 Sales return settlement pop up due amount shows 0 after generating refund
    		$scope.dueAmount  = $scope.paymentDetails.cashAmount ;
    		$scope.calculateDueAmount();
    		//end code to resolved HIS-9205 Sales return settlement pop up due amount shows 0 after generating refund
			angular.element('#refundList_collectRefundModal').modal('show');
		}
		
		$scope.validateAmount = function (){
			var regexp = /^(\d{0,4}(\.\d{0,2})?)$/;
			if($scope.paymentDetails.paymentModeId == 1){
				if(regexp.test(Number($scope.paymentDetails.cashAmount))){
					return false
				}
				else{
					$scope.paymentDetails.cashAmount = $scope.cnMasterObj.totalRefundAmount;
				}
			}
		    if($scope.paymentDetails.paymentModeId == 2){
		    	if(regexp.test(Number($scope.paymentDetails.chequeAmount))){
					return false
				}
				else{
					$scope.paymentDetails.chequeAmount = $scope.cnMasterObj.totalRefundAmount;
				}
			}
		    if($scope.paymentDetails.paymentModeId == 3){
		    	if(regexp.test(Number($scope.paymentDetails.credAmount))){
					return false
				}
		    	else{
		    		$scope.paymentDetails.credAmount = $scope.cnMasterObj.totalRefundAmount;
		    	}
			}
		    if($scope.paymentDetails.paymentModeId == 4){
		    	if(regexp.test(Number($scope.paymentDetails.debAmount))){
					return false
				}
		    	else{
		    		$scope.paymentDetails.debAmount = $scope.cnMasterObj.totalRefundAmount;
		    	}	
			}
	        if($scope.paymentDetails.paymentModeId == 6){
	        	if(regexp.test(Number($scope.paymentDetails.debAmount))){
					return false
				}
		    	else{
		    		$scope.paymentDetails.debAmount = $scope.cnMasterObj.totalRefundAmount;
		    	}
			} 
	        if($scope.paymentDetails.paymentModeId == 8){
	        	if(regexp.test(Number($scope.eTransferObj.transferAmount))){
					return false
				}
				else{
					$scope.eTransferObj.transferAmount = $scope.cnMasterObj.totalRefundAmount;
				}
	        }
	        if($scope.paymentDetails.paymentModeId == 9){
	        	if(regexp.test(Number($scope.paymentDetails.debAmount))){
					return false
				}
				else{
					$scope.paymentDetails.debAmount = $scope.cnMasterObj.totalRefundAmount;
				}
	        }
		}

		$scope.addPaymentDetails = function () {
			
			if(CheckValidPayment()){

			//check amount collected is greater tha deposit amount
			var refundAmount = $scope.cnMasterObj.totalRefundAmount != "" && typeof ($scope.cnMasterObj.totalRefundAmount) != "undefined" ? parseFloat($scope.cnMasterObj.totalRefundAmount) : parseFloat(0);
			$scope.paymentRefundObj.amountRefunded = $scope.cnMasterObj.totalRefundAmount;
			var collectedAmount = 0;
			var tempAmount = 0;

			switch ($scope.paymentDetails.paymentModeId) {
				case 1:
					tempAmount = $scope.paymentDetails.cashAmount != '' && typeof ($scope.paymentDetails.cashAmount) != 'undefined' ? parseFloat($scope.paymentDetails.cashAmount) : parseFloat(0);
					if (parseFloat($scope.paymentDetails.cashAmount) > refundAmount.toFixed(2)) {
						growl.warning("Collected amount cannot greater than Refund amount", {
							title: WARNING_MSG
						});
						return;
					}
					break;
				case 2:
					tempAmount = $scope.paymentDetails.chequeAmount != '' && typeof ($scope.paymentDetails.chequeAmount) != 'undefined' ? parseFloat($scope.paymentDetails.chequeAmount) : parseFloat(0);
					if (parseFloat($scope.paymentDetails.chequeAmount) > refundAmount.toFixed(2)) {
						growl.warning("Collected amount cannot greater than Refund amount", {
							title: WARNING_MSG
						});
						return;
					}
					break;

				case 3:
					tempAmount = $scope.paymentDetails.credAmount != '' && typeof ($scope.paymentDetails.credAmount) != 'undefined' ? parseFloat($scope.paymentDetails.credAmount) : parseFloat(0);
					if (parseFloat($scope.paymentDetails.credAmount) > refundAmount.toFixed(2)) {
						growl.warning("Collected amount cannot greater than Refund amount", {
							title: WARNING_MSG
						});
						return;
					}
					break;

				case 4:
					tempAmount = $scope.paymentDetails.debAmount != '' && typeof ($scope.paymentDetails.debAmount) != 'undefined' ? parseFloat($scope.paymentDetails.debAmount) : parseFloat(0);
					if (parseFloat($scope.paymentDetails.debAmount) > refundAmount.toFixed(2)) {
						growl.warning("Collected amount cannot greater than Refund amount", {
							title: WARNING_MSG
						});
						return;
					}
					break;
					
				case 6:
					tempAmount = $scope.paymentDetails.debAmount != '' && typeof ($scope.paymentDetails.debAmount) != 'undefined' ? parseFloat($scope.paymentDetails.debAmount) : parseFloat(0);
					if (parseFloat($scope.paymentDetails.debAmount) > refundAmount.toFixed(2)) {
						growl.warning("Collected amount cannot greater than Refund amount", {
							title: WARNING_MSG
						});
						return;
					}
					break;
					
				case 8:
					
					tempAmount = $scope.eTransferObj.transferAmount != '' && typeof($scope.eTransferObj.transferAmount) != 'undefined' ? parseFloat($scope.eTransferObj.transferAmount) : parseFloat(0);
					
					if (parseFloat(tempAmount) > refundAmount.toFixed(2)) {
						growl.warning("Collected amount cannot greater than Refund amount", {
							title: WARNING_MSG
						});
						return;
					}
					break;
					
				case 9:
					tempAmount = $scope.paymentDetails.debAmount != '' && typeof ($scope.paymentDetails.debAmount) != 'undefined' ? parseFloat($scope.paymentDetails.debAmount) : parseFloat(0);
					if (parseFloat($scope.paymentDetails.debAmount) > refundAmount.toFixed(2)) {
						growl.warning("Collected amount cannot greater than Refund amount", {
							title: WARNING_MSG
						});
						return;
					}
					break;
			}

			if (tempAmount == 0) {
				growl.warning("Amount cannot be zero", {
					title: WARNING_MSG
				});
				return;
			}

			angular.forEach($scope.listPaymentDetails, function (value, key) {
				collectedAmount = parseFloat(collectedAmount) + parseFloat(value.amount);
			});
			collectedAmount = collectedAmount + tempAmount;
			if (collectedAmount > refundAmount.toFixed(2)) {
				growl.warning("Collected amount cannot greater than Refund amount", {
					title: WARNING_MSG
				});
				return;
			}

			switch ($scope.paymentDetails.paymentModeId) {
				case 1:
					var paymentDetails = {
						paymentModeId: $scope.paymentDetails.paymentModeId,
						amount: parseFloat($scope.paymentDetails.cashAmount).toFixed(2),
						createdBy: $scope.userId
					};
					$scope.listPaymentDetails.push(paymentDetails);
					break;
				case 2:
					var bankName = "";
					//Get Bank Name
					angular.forEach($scope.bankList, function (value, key) {
						if ($scope.paymentDetails.bankId == value.bankId)
							bankName = value.bankName;
					})
					var chequeDate = $scope.getFormattedDate($scope.paymentDetails.chequeDate, false);
					var paymentDetails = {
						paymentModeId: $scope.paymentDetails.paymentModeId,
						amount: parseFloat($scope.paymentDetails.chequeAmount).toFixed(2),
						createdBy: $scope.userId,
						bankId: $scope.paymentDetails.bankId,
						chequeNumber: $scope.paymentDetails.chequeNumber,
						chequeDate: chequeDate,
						bankName: bankName
					};
					$scope.listPaymentDetails.push(paymentDetails);
					break;
				case 3:
					var expiryDate = $scope.getFormattedDate($scope.paymentDetails.credExpiryDate, false);
					var paymentDetails = {
						paymentModeId: $scope.paymentDetails.paymentModeId,
						amount: parseFloat($scope.paymentDetails.credAmount).toFixed(2),
						createdBy: $scope.userId,
						cardHolderName: $scope.paymentDetails.credCardHolderName,
						cardTypeId: $scope.paymentDetails.credCardTypeId,
						cardNo: $scope.paymentDetails.credCardNumber,
						expiryDate: expiryDate,
						transactionNo : $scope.paymentDetails.transactionNo
						
					};
					$scope.listPaymentDetails.push(paymentDetails);
					break;
				case 4:
					var expiryDate = $scope.getFormattedDate($scope.paymentDetails.debExpiryDate, false);
					var paymentDetails = {
						paymentModeId: $scope.paymentDetails.paymentModeId,
						amount: parseFloat($scope.paymentDetails.debAmount).toFixed(2),
						createdBy: $scope.userId,
						cardHolderName: $scope.paymentDetails.debCardHolderName,
						cardTypeId: $scope.paymentDetails.debCardTypeId,
						cardNo: $scope.paymentDetails.debCardNumber,
						expiryDate: expiryDate
					};
					$scope.listPaymentDetails.push(paymentDetails);
					break;
					
				case 6:
					var expiryDate = $scope.getFormattedDate($scope.paymentDetails.debExpiryDate, false);
					var paymentDetails = {
						paymentModeId: $scope.paymentDetails.paymentModeId,
						amount: parseFloat($scope.paymentDetails.debAmount).toFixed(2),
						createdBy: $scope.userId,
					};
					$scope.listPaymentDetails.push(paymentDetails);
					break;
					
				case 8 : 
					var bankName = "";
					angular.forEach($scope.bankList,function(value,key){
						if($scope.eTransferObj.transBankId == value.bankId)
							bankName = value.bankName;
					})
					var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.eTransferObj.transferAmount).toFixed(2), transactionType:$scope.eTransferObj.transactionobj.name, transactionTypeId:$scope.eTransferObj.transactionobj.id,transTransactionDate:  $scope.eTransferObj.transTransactionDate!=null ? moment($scope.eTransferObj.transTransactionDate).format('DD-MM-YYYY HH:mm:ss') : null,bankId:$scope.eTransferObj.transBankId,transactionNo:$scope.eTransferObj.transactionNo,createdBy:$scope.userId,bankName:bankName};
					   $scope.listPaymentDetails.push(paymentDetails);
					break;
					
				case 9:
					var paymentDetails = {
						paymentModeId: $scope.paymentDetails.paymentModeId,
						amount: parseFloat($scope.paymentDetails.debAmount).toFixed(2),
						createdBy: $scope.userId,
					};
					$scope.listPaymentDetails.push(paymentDetails);
					break;
			}
			$scope.initPaymentDetailsObj();
			$scope.calculateDueAmount();
			$scope.setFirstTabLevel(1);
	        $scope.setPaymentModeId(1);
		}// end valid
		}

		$scope.getCardTypeList = function () {
      var obj = {
        "unitId": $scope.unitId,
        "orgnisationId": $scope.organizationId
      }
      $rootScope.startLoader();
      var URI = CONSTANTS.ROOT_URL + "/api/billing" + "/cardTypeMaster";
      BillingGenericService.serviceAction(METHOD_POST, URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function (response) {
        if (response.data.status == "success") {
          $scope.cardTypeList = response.data.listObject;
        }
      });
    }
		
		$scope.setPaymentModeId = function (paymentModeId) {
      $scope.paymentDetails.paymentModeId = paymentModeId;
      // bank list called on click of Cheque tab
      $scope.getBankList();
      // bank list called on click of card tab
      $scope.getCardTypeList();
		}
		
		$scope.flag = true;
		$scope.addRefund = function () {
				$scope.flag = false;
				$rootScope.startLoader();
				$scope.paymentRefundObj.createdBy = $scope.userId;
				$scope.paymentRefundObj.patientId = $scope.patientId;
				$scope.paymentRefundObj.orgnisationId = $scope.organizationId;
				$scope.paymentRefundObj.unitId = $scope.unitId;
				$scope.paymentRefundObj.isCnOrDeposit = 3;
				//$scope.paymentRefundObj.tRefundCnId =$scope.cnMasterObj.saleId;
				$scope.paymentRefundObj.tPharmacyReturnId=$scope.cnMasterObj.saleId;
				$scope.paymentRefundObj.saleReturnId = self.model.returnId
				var depositId = $scope.cnMasterObj.depositId;
				var tRefundCnId = $scope.cnMasterObj.saleId;

				var param = {
					paymentRefundMasterDto: $scope.paymentRefundObj,
					listPaymentRefundDetails: $scope.listPaymentDetails
				};
				
				$scope.refundDetails = angular.copy($scope.paymentRefundObj);
				$scope.paymentrefundDetails = angular.copy($scope.listPaymentDetails);

				var URI = CONSTANTS.ROOT_URL + "/api/billing/refund/pharmacy";
				// console.log("URI", URI);
				// console.log('param', JSON.stringify(param));
				BillingGenericService.serviceAction(METHOD_POST, URI, param, NOTIFICATION_MSG_STATUS_TRUE).then(function (response) {
					if (response.data.status == "success") {
						self.model.updateBillSalesReturn();
					//	rePrintBillSlip(self.model.returnId);
						$scope.initPaymentDetailsObj();
						$scope.listPaymentDetails = [];
						$scope.patientDetailsList = [];
						$scope.patientDetails = {};		
						angular.element('#refundList_collectRefundModal').modal('hide');
						$('.modal-backdrop').remove();
						$(document.body).removeClass("modal-open");
						$scope.flag = false;
						//self.model.NavigatetoSalesReturn();
					}
					$rootScope.stopLoader();
				});
			
			
		}
		/*****loyalty Card******/
		$scope.firstName = "";
		$scope.loyaltyCarNo="";
		$scope.patientLoyaltyCardFlag= true;
		$scope.getLoyaltyCardList = function(){
			$scope.startLoader();
			if($scope.paymentDetails.paymentModeId == 6){
				var data = {
						  "patientId" : $scope.paymentRefundObj.patientId,
						  "organisationId" : $scope.organizationId,
						  "unitId" : $scope.unitId,
						  "status" : "A",
						  "isDeleted" : "N",
						  "isUserDefined":"Y"
				}
				var URI = BASE_URL + ROOT_URL + "/api/loyaltyCard/getLoyaltiCardDetailsByPatientId";
				BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success")
					{
						$scope.send = false;
						$scope.resend = false;
						$scope.stopLoader();
						if(response.data.listObject!=null && response.data.listObject.length>0){
							$scope.loyaltyCardObj = response.data.listObject[0];					
							$scope.paymentDetails.cardBalAmount =  $scope.loyaltyCardObj.cardAmount;
							$scope.paymentDetails.loyaltyCardNo = $scope.loyaltyCardObj.cardNo;
							$scope.loyaltyCarNo = $scope.loyaltyCardObj.cardNo;
							$scope.firstName = $scope.loyaltyCardObj.firstName;
							$scope.patientLoyaltyCardFlag = false;
						}
						
					}
					$scope.stopLoader();
				});
			
			}else{
				$scope.paymentDetails.cardBalAmount = "";
				$scope.paymentDetails.loyaltyCardNo = "";
				$rootScope.stopLoader();
			}
		}
		$scope.getLoyaltyCardNumber = function(number){
			
			$scope.paymentDetails.paymentModeId = number;
			$scope.getLoyaltyCardList();
		}
		$scope.getLoyaltyCardByNo = function(cardNo)
		{
			$scope.patientLoyaltyCardFlag = true;
			$scope.startLoader();
			if(cardNo!="" && cardNo.toString().length==16)
				{
				var data = {
						"patientId" : '',
						  "organisationId" : $scope.organizationId,
						  "unitId" : $scope.unitId,
						  "status" : "A",
						  "isDeleted" : "N",
						  "isUserDefined":"Y",
						  "cardNo":cardNo
				}
				var URI = BASE_URL + ROOT_URL + "/api/loyaltyCard/getPatientDetailsByLoyaltiCardNo";
				BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success")
					{
						$scope.stopLoader();
						if(response.data.listObject!=null && response.data.listObject.length>0)
						{
							$scope.send =false;
							$scope.resend =false;
							var cardAmtToSub = 0;
					    	$scope.loyaltyCardObj = response.data.listObject[0];
						    $scope.paymentDetails.loyaltyCardNo = $scope.loyaltyCardObj.cardNo;
						    if($scope.listPaymentDetails!=null && $scope.listPaymentDetails.length>0)
						    	{
						    	   angular.forEach($scope.listPaymentDetails,function(value,key)
						    	    {
						    		   if(value.paymentModeId==6 && value.cardNumber==cardNo)
						    			   {
						    			     cardAmtToSub = cardAmtToSub + parseFloat(value.amount); 
						    			   }
										
									});
						    	   $scope.firstName = $scope.loyaltyCardObj.firstName;
						    	}
						    	$scope.firstName = $scope.loyaltyCardObj.firstName;
						    	$scope.paymentDetails.cardBalAmount =  $scope.loyaltyCardObj.cardAmount-cardAmtToSub;
						    	$("#loyaltyCardSave").modal("show");
						}else{
							$scope.stopLoader();
							$scope.firstName = "";
							growl.warning("This loyalty card does not Exist",{
								title : WARNING_MSG
							});
						}
					}
					$scope.stopLoader();
				});
				}else{
					$scope.stopLoader();
					$scope.firstName = "";
				}
		}
		
		$scope.loyaltyCardObj ={
				
				"loyaltyCardNo" :"",
				"loyaltyCardBalance" :"",
				"authKey":0
		}
		
		$scope.currentLoyIndex = "";
		$scope.addloyaltyCardPayment = function(){
			
			var loyaltyCardNoFlag = false;
			angular.forEach($scope.listPaymentDetails,function(value,indx){
				
				if(value.cardNo == $scope.paymentDetails.loyaltyCardNo){
					$scope.currentLoyIndex = indx;
					loyaltyCardNoFlag = true;
				}
			});
			
			if(loyaltyCardNoFlag == true){
				
				$("#sameLoyaltyCard").modal("show");
				
			}else{
				
				$scope.addPaymentDetails();
			}
			
		}
		
		
		$scope.changeModeofLoyalty = function(diffNum){
			
			if(diffNum == 1){
				$scope.patientLoyaltyCardFlag = false;
			}
		}
		$scope.sumAmountLoyalty = function(){
			
			$scope.sumLoyaltyAmount = parseFloat($scope.listPaymentDetails[$scope.currentLoyIndex].amount) + parseFloat($scope.paymentDetails.loyaltyCardAmt);
			$scope.listPaymentDetails[$scope.currentLoyIndex].amount = $scope.sumLoyaltyAmount;
			$scope.firstName = "";
			$scope.initPaymentDetailsObj();
			$scope.calculateDueAmount();
			$scope.calculateTotalAmount();
			$scope.patientLoyaltyCardFlag = true;
		}
		
		$scope.totalreturnPaidAmount = 0; 
		var updateBillSalesReturn = function(){			
			var totalAmountRefund = $scope.refundDetails.amountRefunded;
			
			angular.forEach($scope.paymentrefundDetails, function(data){
				$scope.totalreturnPaidAmount = $scope.totalreturnPaidAmount + parseFloat(data.amount);
				$scope.receivedFrom=$scope.refundDetails.receivedFrom;
			});
			// var totalreturnPaidAmount = parseFloat($scope.paymentrefundDetails[0].amount).toFixed(2); 
			var TotalBalancePaid = totalAmountRefund - parseFloat($scope.totalreturnPaidAmount);			
			var statusId = 2; //Pending            
            if(TotalBalancePaid > 0)
                statusId = 18 ; //Partially Completed;
            else if(parseFloat(totalAmountRefund) === parseFloat($scope.totalreturnPaidAmount))
                statusId = 4 ; //Completed;
				
             var data = {
				"storeId": self.model.storeId,								
				"statusId": statusId,		
				"totalreturnPaidAmount" : $scope.totalreturnPaidAmount,
				"receivedFrom":$scope.receivedFrom,
				"patientId":$scope.patientId
			}             
             var URI = CONSTANTS.PHARMACY.OP.SALES_RETURN.UPDATE_RETURN_API +"/"+ $scope.unitId +  "/" + self.model.returnId;
             $rootScope.startLoader();       
             GenericService.serviceAction("PUT", URI, data).then(function (response) {            
                 		rePrintBillSlip(self.model.returnId);
                 		self.model.NavigatetoSalesReturn();
             })         
             	
		}	

		var rePrintBillSlip = function (id) {
			var data = {
			  "appName": "HIS",
			  "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
			  "reportDesignFilePath": "get_salesReturn.rpt",
			  "reportFormat": "PDF",
			  "exportFileName": "SalesReturnBillSlip",
			  "reportParams": [
				{
				  "paramName": "ReturnId",
				  "paramType": "Integer",
				  "paramValue": id
				}
			  ]
			};
		  reportService.generate(REPORT_API_URI1, data);
		}

		initializeController();
		$scope.init();
		
		$scope.eTransferObj={
				transBankId:"",
				transferAmount:"",
				transTransactionDate:"",
				transactionNo:"",
				transactionobj:{
						id:"",
						name:""
				}	
		}
		
		$scope.transactionTypesList=[];
		 $scope.getTransactionTypes = function(){
			var URI = BASE_URL + ROOT_URL + API + BILLING + "/transactiontypes/"+$scope.organizationId+"/"+$scope.unitId;
			BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == "success"){
					$scope.transactionTypesList = response.data.listObject;
				}
				
				console.log("transactionTypesList",$scope.transactionTypesList);
			});
		}
		 
		 $scope.getTransactionTypes();
		 
		
		var CheckValidPayment=function(){
			let isValidPayment = true;
			switch ($scope.paymentDetails.paymentModeId) {
			case 1:
				if (!$scope.paymentDetails.cashAmount || $scope.paymentDetails.cashAmount == '' ||  typeof ($scope.paymentDetails.cashAmount) == undefined){
						growl.error("Enter Amount", {title: ERROR_MSG});
						isValidPayment =false;
					}
				return isValidPayment;
				break;
				
			case 2:
				
				if (!$scope.paymentDetails.bankId ||$scope.paymentDetails.bankId == '' ||typeof ($scope.paymentDetails.bankId) == undefined){
					growl.error("Please Select Bank", {title: ERROR_MSG});
					isValidPayment =false;
				}
				if (!$scope.paymentDetails.chequeNumber || $scope.paymentDetails.chequeNumber == '' || typeof ($scope.paymentDetails.chequeNumber) == undefined){
					growl.error("Please Enter Cheque Number", { title: ERROR_MSG });
					isValidPayment =false;
				}
				if (!$scope.paymentDetails.chequeDate || $scope.paymentDetails.chequeDate == '' || typeof ($scope.paymentDetails.chequeDate) == undefined){
					growl.error("Please Select Cheque Date", {title: ERROR_MSG});
					isValidPayment =false;
				}
				if (!$scope.paymentDetails.chequeAmount || $scope.paymentDetails.chequeAmount == '' || typeof ($scope.paymentDetails.chequeAmount) == undefined){
					growl.error("Please Enter Amount", {title: ERROR_MSG});
					isValidPayment =false;
				}
				return isValidPayment;
				break;
			case 3:
				if (!$scope.paymentDetails.credCardNumber || $scope.paymentDetails.credCardNumber == '' || typeof ($scope.paymentDetails.credCardNumber) == undefined){
					growl.error("Please Enter Card Number", { title: ERROR_MSG });
					isValidPayment =false;
				}
				if(typeof($scope.paymentDetails.transactionNo) == "undefined" || $scope.paymentDetails.transactionNo == null || 
						$scope.paymentDetails.transactionNo == "" ){
						growl.error("Please enter transaction no",{
							title : ERROR_MSG
						});
						return;
					}
				if (!$scope.paymentDetails.credAmount || $scope.paymentDetails.credAmount == '' || typeof ($scope.paymentDetails.credAmount) == undefined){
					growl.error("Please Enter Amount", {title: ERROR_MSG});
					isValidPayment =false;
				}
				return isValidPayment;
				break;
			case 4:
				
				if (!$scope.paymentDetails.debCardHolderName ||$scope.paymentDetails.debCardHolderName == '' ||typeof ($scope.paymentDetails.debCardHolderName) == undefined){
					growl.error("Please Enter Card Holder name", {title: ERROR_MSG});
					isValidPayment =false;
				}
				if (!$scope.paymentDetails.debCardTypeId || $scope.paymentDetails.debCardTypeId == '' || typeof ($scope.paymentDetails.debCardTypeId) == undefined){
					growl.error("Please Select Card", { title: ERROR_MSG });
					isValidPayment =false;
				}
				if (!$scope.paymentDetails.debCardNumber || $scope.paymentDetails.debCardNumber == '' || typeof ($scope.paymentDetails.debCardNumber) == undefined){
					growl.error("Please Enter Card Number", { title: ERROR_MSG });
					isValidPayment =false;
				}
				if (!$scope.paymentDetails.debExpiryDate || $scope.paymentDetails.debExpiryDate == '' || typeof ($scope.paymentDetails.debExpiryDate) == undefined){
					growl.error("Please Select Expire Date", {title: ERROR_MSG});
					isValidPayment =false;
				}
				if (!$scope.paymentDetails.debAmount || $scope.paymentDetails.debAmount == '' || typeof ($scope.paymentDetails.debAmount) == undefined){
					growl.error("Please Enter Amount", {title: ERROR_MSG});
					isValidPayment =false;
				}
				return isValidPayment;
				break;
			case 8 : 
				
				if($scope.eTransferObj.transactionobj.id == ""){
					growl.warning("Please Select Transaction Type",{
						title : WARNING_MSG
					});
					isValidPayment =false;
				}
				if($scope.eTransferObj.transferAmount == 0 || $scope.eTransferObj.transferAmount == null){
					growl.warning("Amount cannot be zero",{
						title : WARNING_MSG
					});
					isValidPayment =false;
				}
			
				if($scope.eTransferObj.transTransactionDate == ""){
					$scope.eTransferObj.transTransactionDate = null;
					isValidPayment =false;
				}
				
				return isValidPayment;
				break;
			case 9:
				
				if (!$scope.paymentDetails.debAmount || $scope.paymentDetails.debAmount == '' || typeof ($scope.paymentDetails.debAmount) == undefined){
					growl.error("Please Enter Amount", {title: ERROR_MSG});
					isValidPayment =false;
				}
				return isValidPayment;
				break;
		}
			
		}//end valid function
		
		$scope.CloseAndBack = function () {
			/*angular.element('#refundList_collectRefundModal').modal('hide');			
			let RefundModal = document.getElementById("refundList_collectRefundModal");	
			RefundModal.style.display = "none";*/
			$('.modal-backdrop').hide();
			$state.go('billSalesReturn');
		}

		$scope.calculateReceivedQty = function (item,index) {
			if(item.quantity == undefined || item.quantity == null || item.quantity == ""){
				item.quantity = 0;
			}
			if(item.isThreeUOMApplicable == false){
				if(item.uomTypeId == 1){
					item.receivedQtyCon = (item.quantity +"*"+ item.uom[0].conversion);
					item.receivedQtyCalculation = (item.quantity * item.uom[0].conversion);
					item.uomUnitData = item.uom[2].uomUnit;
				}
				else if(item.uomTypeId == 2){
				
				}
				else{
					item.receivedQtyCon = (item.quantity);
					item.receivedQtyCalculation = (item.quantity);
					item.uomUnitData = item.uom[2].uomUnit;
				}
			}
			else{
				if(item.uomTypeId == 1){
					item.receivedQtyCon = (item.quantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
					item.receivedQtyCalculation = (item.quantity * item.uom[0].conversion * item.uom[1].conversion);
					item.uomUnitData = item.uom[2].uomUnit;
				}
				else if(item.uomTypeId == 2){
					item.receivedQtyCon = (item.quantity +"*"+ item.uom[1].conversion);
					item.receivedQtyCalculation = (item.quantity * item.uom[1].conversion);
					item.uomUnitData = item.uom[2].uomUnit;
				}
				else{
					item.receivedQtyCon = (item.quantity);
					item.receivedQtyCalculation = (item.quantity);
					item.uomUnitData = item.uom[2].uomUnit;
				}
			}
			$('#reasonId'+index).focus()
		}
	}

	function config($stateProvider) {
		$stateProvider.state("addNewbillSalesReturn", {
			url: "/addNewbillSalesReturn",
			templateUrl: 'views/PharmacyInventory/InPatient/add-new-bill-sales-return.html',
			controller: "AddNewBillSalesReturn.Controller",
			controllerAs: "vm",
			title: 'Sales Return'
		});
	}

	angular
		.module("myApp")
		.config(config)
		.controller("AddNewBillSalesReturn.Controller", addNewBillSalesReturnController);
})();