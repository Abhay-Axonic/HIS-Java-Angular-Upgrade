(function () {
    "use strict";

    function outPatientSalesWorklistDetailsController($scope, $rootScope, $http, $window,  CONSTANTS,GenericServiceFileUpload, reportService, GenericService,GenericServiceParamHeader, $filter, growl, CurrencyService, StatusService, salesWorklistService,pharamcyContractService, $state, pharmacyPaymentCollFactory, BillingGenericService, $cookies,$location,loyaltiCardCallFactory, commonDetailService,$timeout,stopTransactionService,$interval) {

        var self = this;
        var discountLimit = 100;
        $scope.mpesaRigths = JSON.parse(localStorage.getItem('mpesaRigths'));
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        $scope.cashCollectedAmount = 0;
        var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
        $scope.cashAmountValidation =  cookieObjectAppConfig.cashAmountValidation;
        $scope.cashAmountLimit =  cookieObjectAppConfig.cashAmountLimit;
        $scope.depositTypeWiseRequired =  cookieObjectAppConfig.depositTypeWiseRequired;
        $scope.payGatewayProviderId = cookieObjectAppConfig.payGatewayProviderId;
        $scope.changePayeeRights = cookieObject.changePayeeRights;
    	if(cookieObjectAppConfig == undefined){
    		$state.go('applicationConfiguration');
    		return;
    	}
    	else
    		{
    			console.log('cookieObjectAppConfig',cookieObjectAppConfig);
    			
    		}
        function initializeController() {

            $rootScope.loginpage = true;
            
            $scope.isSmartApplicable='N';
            $scope.page = "billList";
            $scope.dateOptions = {
                formatYear: 'yyyy',
                showWeeks: false,
                minDate: new Date()
            };
            $scope.openDatePicker = function () {
                $scope.datepicker.opened = true;
            };
            $scope.openDatePickerTo = function () {
                $scope.datepickerTo.opened = true;
            };
            $scope.chequeDateOptions = {
                formatYear: 'yyyy',
                showWeeks: false,
                minDate: new Date().setMonth(new Date().getMonth() - 3)
            };
            $scope.openDatePickerPatiSearch = function () {
                $scope.datepicker1.opened = true;
            };

            $scope.datepicker1 = {
                opened: false
            };

            $scope.datepicker = {
                opened: false
            };
            $scope.datepickerTo = {
                opened: false
            };
            
            
            //INSURANCE
            $scope.dateOptionsStart = {
          	        formatYear: 'yyyy',
          	        showWeeks: false,
          	        maxDate: new Date()
          	      };
            $scope.dateOptionsPaymentEntitlement = {
              formatYear: 'yyyy',
              showWeeks: false,
              minDate: new Date()
            };
            
            $scope.birthDateOptions = {
              formatYear: 'yyyy',
              showWeeks: false,
              maxDate: new Date()
            };
            $scope.chequeDateOptions = {
              formatYear: 'yyyy',
              showWeeks: false,
              minDate: new Date().setMonth(new Date().getMonth() - 3)
            };
            $scope.openDatePicker = function () {
              $scope.datepicker.opened = true;
            };
            $scope.openDatePickerTo = function () {
              $scope.datepickerTo.opened = true;
            };

            $scope.openDatePickerPatiSearch = function () {
              $scope.datepicker1.opened = true;
            };

            $scope.datepicker1 = {
              opened: false
            };

            $scope.datepicker = {
              opened: false
            };
            $scope.datepickerTo = {
              opened: false
            };
            
            self.model = {
                worklistDetails: {
                    id: "1" + (new Date()).getTime(),
                    items: [],
                },
                modelClass: true,
                isBatchValid: [],
                isUOMValid: [],
                isQuantityValid: [],
                isDespensedQuantityValid: [],
                SelectedItems: [],
                discountCategoryList: [{
                    "discountCategory": "Select Discount Category"
                }],
                newQtyGenrated: 0
            };

            var cookieObject = {};
            var cookieObjectAppConfig = {};

            [cookieObject, cookieObjectAppConfig] = $rootScope.checkCookies();
            self.model.userId = cookieObject.userId;

            $scope.cookieObjectskipPharmacySettlement = JSON.parse(localStorage.getItem('isSkipPharmacySettlement'));
            var cookieObject = $cookies.getObject('cookieObject');
            if (cookieObject == undefined) {
                $state.go('login');
                return;
            }

            var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
            if (cookieObjectAppConfig == undefined) {
                $state.go('login');
                return;
            }
            var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
            if (cookieObjectAppConfigSecond != undefined) {
	            $scope.exceededBillAmountSettlement=cookieObjectAppConfigSecond.exceededBillAmountSettlement;
            }
            $scope.cookieObjectStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
    	    $scope.storeName = $scope.cookieObjectStore != null ? $scope.cookieObjectStore.storeName : ''; // store null -> empty header
            // else {
            // console.log('cookieObjectAppConfig', cookieObjectAppConfig);

            // }
            $scope.removeItemFlag = false;
            $scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
            $scope.isAppointmentRequired = cookieObjectAppConfig.isAppointmentRequired;
            $scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
            $scope.taxInclusive = cookieObjectAppConfig.taxInclusive;
            $scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
            $scope.isPinPadRequired = cookieObjectAppConfig.isPinPadRequired;
            $scope.registeredPinPadDevice= cookieObjectAppConfig.registeredPinPadDevice;
            $scope.isConsolidatedBill = cookieObjectAppConfig.isConsolidatedBill;
            $scope.unitId = cookieObject.unitId;
            $scope.userId = cookieObject.userId;
            $scope.organizationId = cookieObject.organizationId;
            $scope.userId = $scope.userId;
            $scope.storeId = cookieObject.storeId;
            $scope.autoBatch = cookieObject.autoBatch;
            $scope.autoPrescribedQty = cookieObject.autoPrescribedQty;
            $scope.mpesaRequired = cookieObject.mpesaRequired;
            $scope.hitMpesaApi = false;
            $scope.appointmentId = 0;
            $scope.encounterId = null;
            $scope.fileObj = {};
			$scope.fileObj.file = null;
			$scope.atCopRequired = cookieObject.atCopRequired;
            //vivek added
            $scope.payeeId = '';
            $scope.consumedAmt = parseFloat(0);
            $scope.receivedFrom = "";
            $scope.identificationNumber = "";
            $scope.DepositObj = {};
    		$scope.DepositObj.userEnterDepositAmount = 0;
            $scope.depositeList = {
                depositeIds: [1]
            };
            $scope.flagForSale=false;
            $scope.flagForPay=false;
            $scope.flagForBharatQr=false;

			$scope.objPosMachine={
					isSale:false, 
					isSMSpay : false,
					isBharatQr:false, 
					posMachineDeviceId:"",
					deviceName:"",
					machineConfigDetails:"",
					mobileNumberForPOS:""
			};
            $scope.depositeDisabledFlag = false;
            $scope.razorPayObject={"razorMobileNo":null,"razorAmount":null,"razorPayRequired":true,"razorUniqueId":null,"razorPaidAmount":null};
          //Code for Initialization Contract
            $scope.initPaymentEntitlement = function(){  
          	 
          	
              $scope.entitlementObj={
                "paymentEntitlementTypeId":1
              }	
          	
        	$scope.paymentEntitleMentList=
      		[{
      			'paymentEntitlementName':"Self",
      			'paymentEntitlementId':"1",
      			'companyName':"Self",
      			'tariffName':"Self Tariff",
      			'contractName':"",
      			'insuranceName':"",
      			'insuranceNumber':"",
      			'startDate':"",
      			'endDate':"",
      			'priority':"1",
      			'companyId':$scope.selfPayeeId,
      			'tariffId':'',
      			'contractId':null,
      			'insuredAmount' : '',
      			'patientSponsorDetailsId':null
      		}];
      	
      	$scope.paymentEntitleMentCorporate=
      		[{
      			'companyId':"",
      			'contractId':"",
      			//'statrDate':"",
      			//'endDate':""
      		}];
      	
      	$scope.paymentEntitleMentInsurance=
      		[{
      			'companyId':"",
      			'associateCompanyId':"",
      			'contractId':"",
      			//'statrDate':"",
      			//'endDate':"",
      			'insuranceNumber':"",
      			'insuredAmt':""
      		}];
            }
            
            $scope.initPaymentEntitlement();
            //Initialzation End
            
            
            //Methods
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })
            StatusService.GetStatus().then(function (status) {
                self.model.Status = status;
            })
            $scope.saveFlag = true;
            self.model.GetWorklistDetails = GetWorklistDetails;
            self.model.BatchChange = BatchChange;
            self.model.UomChange = UomChange;
            self.model.ApplyDiscount = ApplyDiscount;
            self.model.AddItem = AddItem;
            self.model.RemoveItem = RemoveItem;
            self.model.AllocateQuantity = AllocateQuantity;
            self.model.ValidateBill = ValidateBill;
            self.model.SubstituteItems = SubstituteItems;
            self.model.SelectAllItems = SelectAllItems;
            self.model.SelectSubstituteItem = SelectSubstituteItem;
            self.model.Add = Add;
            self.model.AddSubstituteItem = AddSubstituteItem;
            self.model.validateUOMAndBatch = validateUOMAndBatch;
            self.model.RemoveSubstituteItem = RemoveSubstituteItem;
            self.model.selectedQuantity = 0;
            self.model.selectedItemId = 0;
            $scope.transferToActiveDeposit = "N";
            self.model.SaveBill = SaveBill;
            self.model.SavePopup = SavePopup;
            self.model.SetDecimal = SetDecimal;
            self.model.ApplyDiscountValidation = ApplyDiscountValidation;
            self.model.ChangeItemStatus = ChangeItemStatus;
            self.model.RestorePreviousItem = RestorePreviousItem;
            self.model.ConvertQtyOrdered = ConvertQtyOrdered;
            self.model.NavigateToOutPatientSalesWorklist = NavigateToOutPatientSalesWorklist;
            self.model.OnKeyPressQuantity = OnKeyPressQuantity;
            self.model.ValidateMaxOrderQuantity = ValidateMaxOrderQuantity;
            self.model.ValidateBatch = ValidateBatch;
            self.model.ValidateUOM = ValidateUOM;
            self.model.ValidateQuantity = ValidateQuantity;
            self.model.ValidateUniqueBatchAndUom = ValidateUniqueBatchAndUom;
            // self.model.ValidateSaveButton = ValidateSaveButton;
            self.model.rePrintBillSlip = rePrintBillSlip;
            self.model.BarcodeScan = BarcodeScan;
            self.model.AddBatchToDetail = AddBatchToDetail;
            self.model.SetFocus = SetFocus;
            self.model.ClearBarcode = ClearBarcode;
            self.model.ConfirmRemove = ConfirmRemove;
            self.model.CalculateAmount = CalculateAmount;
            self.model.GetGenericMaster = GetGenericMaster;
            self.model.GetStrengthUnitList = GetStrengthUnitList;
            self.model.ViewCombinationDetails = ViewCombinationDetails;
            self.model.ViewStockCheckDetails = ViewStockCheckDetails;
            self.model.searchbyTypeId = 1;
            self.model.SearchItems = SearchItems;
            self.model.SearchGenerics = SearchGenerics;
            self.model.AddItemToBill = AddItemToBill;
            self.model.SearchBatch = SearchBatch;
            self.model.AddBatchToBill = AddBatchToBill;
            self.model.getAllPatientDiagnosisForIcon = getAllPatientDiagnosisForIcon;
            self.model.Calculate = Calculate;
            self.model.onChangeDiscount = onChangeDiscount;
            self.model.ActualSaveBill  = ActualSaveBill;
            self.model.printOutPatientSalesWorklistDetailsReport  = printOutPatientSalesWorklistDetailsReport;
            self.model.calculateAtcop = calculateAtcop;

            SetFocus();
            self.model.issueId = salesWorklistService.getWorklistId();
            $scope.encounterId = pharamcyContractService.billObj.encounterId;
            $scope.patientId= pharamcyContractService.billObj.patientId;
            if(pharamcyContractService.billObj.mobileNumber!=null){
           	 $scope.mobileNumber = parseInt(pharamcyContractService.billObj.mobileNumber);
        	 $scope.countryCallingCode = parseInt(pharamcyContractService.billObj.countryCallingCode);
            	 $scope.razorPayObject.razorMobileNo = pharamcyContractService.billObj.mobileNumber;
            	 $scope.objPosMachine.mobileNumberForPOS = pharamcyContractService.billObj.mobileNumber;
            }
            GetWorklistDetails();
            $scope.saveSponsorFlag=false;
            $scope.validatTextFieldFlag = true;
        }

        $scope.popupStore = function () {
			var popup = angular.element('#storePopUp');
			popup.modal('show');
		}

        $scope.btnAddShow = true;
        $scope.atCopToSave=false;
        $scope.atCopSelected=false;
        var calculateAtcop = function (atCop,flag) {
        	$scope.atCopSelected=atCop;
        	$scope.atCopToSave=$scope.atCopSelected;
        	$scope.globalFlag=flag;
        	var count=0;
            angular.forEach(self.model.worklistDetails.items, function (d){
            	count++;
            	if(d.selectedBatch!=undefined && d.selectedBatch!=null && d.selectedBatch.length!=0)
                {
            	if(d.disabled==false)	
            		{
            		$scope.atCop=$scope.atCopSelected;
            		if($scope.globalFlag==1)
            			{
            			if($scope.atCop == true){
                            d.rate = d.selectedBatch.actualLendingPrice;
                            Calculate(d, self.model.worklistDetails);
                            CalculateAmount(d);
                            $scope.atCopToSave=true;
                        }
                        else{
                            d.rate = d.selectedBatch.rate;
                            Calculate(d, self.model.worklistDetails);
                            CalculateAmount(d);
                            $scope.atCopToSave=false;
                        }
            			if(d.selectedItems != undefined && d.selectedItems != null && d.selectedItems.length > 0){
            				angular.forEach(d.selectedItems, function (sub){
                				if($scope.atCop == true){
                					sub.rate = sub.selectedBatch.actualLendingPrice;
                                    Calculate(sub, d.selectedItems);
                                    CalculateAmount(sub);
                                    $scope.atCopToSave=true;
                                }
                                else{
                                	sub.rate = sub.selectedBatch.rate;
                                    Calculate(sub, d.selectedItems);
                                    CalculateAmount(sub);
                                    $scope.atCopToSave=false;
                                }
                			});
            			}
            			}
            		}
            	else
            		{
            		if(d.disabled==true)
                	{
                	$scope.atCop=d.atCop;
                	if(d.selectedItems != undefined && d.selectedItems != null && d.selectedItems.length > 0){
        				angular.forEach(d.selectedItems, function (sub){
            				if($scope.atCop == true){
            					sub.rate = sub.selectedBatch.actualLendingPrice;
                                Calculate(sub, self.model.worklistDetails);
                                CalculateAmount(sub);
                                $scope.atCopToSave=true;
                            }
                            else{                            	
                            	sub.rate = sub.selectedBatch.rate;
                            	 if(sub.quantity != undefined && sub.quantity != null && sub.quantity != 0 && self.model.worklistDetails.statusId != 4){
                            		 Calculate(sub, self.model.worklistDetails);
                                     CalculateAmount(sub);
                            	 }
                                $scope.atCopToSave=false;
                            }
            			});
        			}
                	}
                	else
                		{
                		$scope.atCop=$scope.atCopSelected;
                		}
                    if($scope.atCop == true){
                        d.rate = d.selectedBatch.actualLendingPrice;
                        Calculate(d, self.model.worklistDetails);
                        CalculateAmount(d);
                        $scope.atCopToSave=true;
                    }
                    else{
                        d.rate = d.selectedBatch.rate;
                        if(d.quantity !=undefined && d.quantity != null && d.quantity != 0 ){
	                        Calculate(d, self.model.worklistDetails);
	                        if(d.batchDetails != undefined && d.batchDetails != null && d.batchDetails != "" && self.model.worklistDetails.statusId != 4 && d.disabled!=true && d.itemDisabled!=true)
	                        {
	                        	CalculateAmount(d);
	                        }
                        }
                        $scope.atCopToSave=false;
                    }
                    if(d.selectedItems != undefined && d.selectedItems != null && d.selectedItems.length > 0){
        				angular.forEach(d.selectedItems, function (sub){
            				if($scope.atCop == true){
            					sub.rate = sub.selectedBatch.actualLendingPrice;
                                Calculate(sub, self.model.worklistDetails);
                                CalculateAmount(sub);
                                $scope.atCopToSave=true;
                            }
                            else{
                            	sub.rate = sub.selectedBatch.rate;
                                Calculate(sub, self.model.worklistDetails);
                                CalculateAmount(sub);
                                $scope.atCopToSave=false;
                            }
            			});
        			}
            		}
            }
            	else
            		{
            		if(d.selectedItems != undefined && d.selectedItems != null && d.selectedItems.length > 0){
        				angular.forEach(d.selectedItems, function (sub){
            				if($scope.atCop == true){
            					sub.rate = sub.selectedBatch.actualLendingPrice;
                                Calculate(sub, d.selectedItems);
                                CalculateAmount(sub);
                                $scope.atCopToSave=true;
                            }
                            else{
                            	sub.rate = sub.selectedBatch.rate;
                                Calculate(sub, d.selectedItems);
                                CalculateAmount(sub);
                                $scope.atCopToSave=false;
                            }
            			});
        			}
            		}
            	
            });
            if(count==self.model.worklistDetails.items.length && $scope.globalFlag!=1)
            	{
            	$scope.atCop=false;
            	}
        }

        $scope.showItemsearch = function () {
            $('#itemsearch').show();
            $('#genericsearch').hide();
        }
    
        $scope.showGenericsearch = function () {
            $('#itemsearch').hide();
            $('#genericsearch').show();
        }

        var SearchItems = function (search) {
	if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            if (search.length < 1) return;
        
            if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
                self.model.prevSearch = search;
        
                var URI = CONSTANTS.GLOBAL.ITEM_API  + $scope.unitId+"/" + true + "/" + search + "/" + $cookies.getObject('cookieObject').storeId;
                $rootScope.startLoader();
                return GenericService.serviceAction("GET", URI, {})
                .then(function (response) {
                    $rootScope.stopLoader();
                    if (!!response.data && response.data.length > 0) {
                    self.model.items = response.data;
                    return $filter('filter')(self.model.items, {
                        $: search
                    });
                    } else {
                    NoItemFound(search);
                    return self.model.items;
                    }
                }, function (err) {
                    NoItemFound(search);
                    $rootScope.stopLoader();
                    return self.model.items;
                });
            } else {
                if (!!self.model.items && self.model.items.length > 0 &&
                self.model.items[0].itemFound != undefined && !self.model.items[0].itemFound) {
                return NoItemFound(search);
                } else {
                return $filter('filter')(self.model.items, {
                    $: search
                });
                }
            }
        }
      
        var SearchGenerics = function (search) {
	if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            if (search.length < 1) return;
        
            if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
                self.model.prevSearch = search;
        
                var URI = CONSTANTS.GLOBAL.GENERIC_API_SEARCH + $scope.unitId +"/" + true + "/" + search + "/" + $cookies.getObject('cookieObject').storeId;
                $rootScope.startLoader();
                return GenericService.serviceAction("GET", URI, {})
                .then(function (response) {
                    $rootScope.stopLoader();
                    if (!!response.data && response.data.length > 0) {
                    self.model.generics = response.data;
                    return $filter('filter')(self.model.generics, {
                        $: search
                    });
                    } else {
                    NoGenericFound(search);
                    return self.model.generics;
                    }
                }, function (err) {
                    NoGenericFound(search);
                    $rootScope.stopLoader();
                    return self.model.generics;
                });
            } else {
                if (!!self.model.generics && self.model.generics.length > 0 &&
                self.model.generics[0].itemFound != undefined && !self.model.generics[0].itemFound) {
                return NoGenericFound(search);
                } else {
                return $filter('filter')(self.model.generics, {
                    $: search
                });
                }
            }
        }
      
        var NoItemFound = function (search) {
            var item = {
                "itemFound": false,
                "details": "No item found with item code or name " + search
            };
            self.model.items = [item];
        }
      
        var NoGenericFound = function (search) {
            var generic = {
                "itemFound": false,
                "details": "No generic found with item code or name " + search
            };
            self.model.generics = [generic];
        }
        
        var AddItemToBill = function (bill, item, model, label) {
        	if (!!item.id) {
        		 ActualAddItemToBill(bill, item, model, label);
        		/*
           	 var stopData = {
    	                "itemIds": [item.id],
    	                "storeId": $scope.storeId 	      		 
    	         };
           	 stopTransactionService.GetstopTransactionData(stopData).then(function(responseData){        
                    var StopTransactionData = responseData;  
                    if(StopTransactionData.length > 0 ){    
                             var startdate=  $filter('date')(StopTransactionData[0].startDate, "dd-MM-yyyy");
                                var stopdate=  $filter('date')(StopTransactionData[0].stopDate, "dd-MM-yyyy");
                             	  growl.warning("Stop Transation is in Process for this Item(s) <br/>Start Date : "+ startdate +" <br/> Stop Date: "+ stopdate +" <br/> Days: "+ StopTransactionData[0].days +" <br/>  Hours: "+ StopTransactionData[0].hours +" <br/>  Minutes: "+ StopTransactionData[0].minutes +"  ");
                             	  $rootScope.stopLoader();                
                             	 ActualAddItemToBill(bill, item, model, label);
                     }
                    else{
                   	 ActualAddItemToBill(bill, item, model, label);
                    }
           	 });        	 
           */}
        } 

        var ActualAddItemToBill = function (bill, item, model, label) {
            if (!!item.id) {
            	
              	 
            	$scope.encounterId = bill.encounterId;
                var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId + '/'+$scope.storeId+'/' + item.id + '/' + $scope.entitlementObj.paymentEntitlementTypeId;
        
                $rootScope.startLoader();
                GenericService.serviceAction("GET", URI, {})
                .then(function (response) {
                	 response.data["itemDisabled"]=false;
                     response.data["disabled"]=false;
                    $scope.data = response.data;
                    $scope.itemSelected = {};
                	$scope.itemSelected = item;
                    //addded by minesh
                    var clubData  = clubBatches(response.data.batch);
                    response.data.batch = clubData;
                    //end added by minesh
                    
                    angular.forEach(response.data.batch, function (value, key) {
                    if(!!this.markUpDownPer){
                        var markUpDown = (value.sellingPrice * (this.markUpDownPer / 100));
        
                        // if (this.markUpDown == 1) {
                        // value.sellingPrice = value.sellingPrice + markUpDown;
                        // } else if (this.markUpDown == 0) {
                        // value.sellingPrice = value.sellingPrice - markUpDown;
                        // }
                    }
                    }, response.data);
        
                    // response.data.batch.unshift({
                    // batchDetails: "Select Batch"
                    // });

                    $scope.patientConsation(response.data);
                    $timeout( function(){
                        var item = {
                        isConsumed:false,
                        itemDisabled:false,
                        disabled:false,
                        addItemTopFlag:true,
                        itemId: response.data.itemId,
                        itemCode: response.data.itemCode,
                        markUpDown: response.data.markUpDown,
                        itemName: response.data.itemName,
                        markUpDownPer: response.data.markUpDownPer,
                        formulationType: response.data.formulationType,
                        stock: response.data.currentStock,
                        genericId: response.data.genericId,
                        genericName: response.data.genericName,
                        manufacturerId: response.data.manufacturerId,
                        manufacturerName: response.data.manufacturerName,
                        strength: response.data.strength,
                        batch: response.data.batch,
                        productCategoryId:response.data.productCategoryId,
                        assetCategoryId:response.data.assetCategoryId,
                        selectedBatch: response.data.batch[0],
                        uom: [{
                            uomDetails: "Select UOM",
                        }],
                        taxId: response.data.taxId,
                        tax: response.data.taxName,
                        taxPercentage: response.data.taxPercent,
                        isOTC: true,
                        isLasa: response.data.isLasa,
                        patientClassPercentage : $scope.patientClassPercentage,
                        saleDetailPayeeMapperDtoList:[],
                        SaleDetailPackageMapperList:[],
                        storageUnitName: response.data.storageUnitName,
                        isThreeUOMApplicable: response.data.isThreeUOMApplicable,
                        onlySubButtonDisable:true
                        };
                        item.colorCode=response.data.colorCode;
                        item.selectedBatch = commonDetailService.getDefaultBatchFromConfig(response.data.batch);
                        if(item.selectedBatch!=undefined)
                        	{
                        	   //item.selectedUom = item.uom[0];
                        	   item.selectedUom = item.selectedBatch.uom.find(function (obj) {
                                   return obj.opUom;
                                   });
                        	   
                        	  
                        	}
                        else{
                        	item.selectedUom = item.uom[0];
                        }
                        item.checkNewItem=true;
                        
                        
                        if($scope.NonFormularyAdded == true){
                        	item.prescriptionDetailsId = $scope.itemSelected.prescriptionDetailsId;
                        	item.prescribedDose = $scope.itemSelected.prescribedDose;
                        	item.oldPrescriptionId = $scope.itemSelected.prescriptionDetailsId;
                        	item.units = $scope.itemSelected.strengthDesc;
                        	item.duration = $scope.itemSelected.duration;
                        	item.durationUnit = $scope.itemSelected.durationUnit;
                        	item.frequencyDesc = $scope.itemSelected.frequencyDesc;
                        	item.qtyPrescribed = $scope.itemSelected.qtyPrescribed;
                        	item.instructionDesc = $scope.itemSelected.instructionDesc;
                        }
                        item.isNarcotic=response.data.isNarcotic;
                        if(item.isNarcotic==true)
                        	{
                        	item.colorCode="red";
                        	}
                        item.addedItemToTop = true;
                        bill.items.push(item);

//                        if(item.isLasa !== undefined && item.isLasa !== null && item.isLasa === true){
//                            growl.warning("Item is LASA drug..!",{
//                                title : WARNING_MSG
//                            });
//                        }

                        ValidateUniqueBatchAndUom(item);
                        if(item.selectedBatch!=undefined)
                    	{
                          BatchChange(bill.items.length-1,self.model.worklistDetails, item.selectedBatch,null);
                    	}
                        if(bill.items.length > 1){
                            $scope.flagRemove = true;
                        }
                        else{
                            $scope.flagRemove = false;
                        }
                   },2000);
                    self.model.search = '';
                    $rootScope.stopLoader();
                    $rootScope.stopLoader();
                
                }, function (err) {
                    growl.error(err.data.message);
                    $rootScope.stopLoader();
                });
            }
        }

        $scope.patientConsation = function (item) {
            var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId + '/'+$scope.storeId + '/' + item.itemId + '/' + $scope.entitlementObj.paymentEntitlementTypeId + '/' + $scope.encounterId;
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(function (response) {
                $scope.patientClassPercentage = response.data.patientClassPercentage;
                $scope.data.patientClassPercentage = $scope.patientClassPercentage;
            });
        }

        var SearchBatch = function (search) {
            if (search.length < 1) return;
        
            if ((!self.model.prevBatchSearch || self.model.prevBatchSearch != search) && (search.length == 1)) {
                self.model.prevBatchSearch = search;
                var URI = CONSTANTS.GLOBAL.WALK_IN_SALES_BATCH_API +  $scope.storeId + "/" + $scope.unitId +  "/" + true + "/" + search;
                $rootScope.startLoader();
                return GenericService.serviceAction("GET", URI, {})
                .then(function (response) {
                    $rootScope.stopLoader();
                    if (!!response.data && response.data.length > 0) {
                    self.model.batches = response.data;
                    return $filter('filter')(self.model.batches, {
                        $: search
                    });
                    } else {
                    NoBatchFound(search);
                    return self.model.batches;
                    }
                }, function (err) {
                    NoBatchFound(search);
                    $rootScope.stopLoader();
                    return self.model.batches;
                });
            } else {
                if (!!self.model.batches && self.model.batches.length > 0 &&
                self.model.batches[0].itemFound != undefined && !self.model.batches[0].itemFound) {
                return NoBatchFound(search);
                } else {
                return $filter('filter')(self.model.batches, {
                    $: search
                });
                }
            }
        }

        var AddBatchToBill = function (bill, item, model, label) {
        	
        	AllocateBatch(bill, item.id, item.batchId);
        	
        	/*
            if (!!item.id && !!item.batchId) {
            	 var stopData = {
     	                "itemIds": [item.id],
     	                "storeId": $scope.storeId 	      		 
     	         };
           	 	stopTransactionService.GetstopTransactionData(stopData).then(function(responseData){        
                    var StopTransactionData = responseData;  
                    if(StopTransactionData.length > 0 ){    
                             var startdate =  $filter('date')(StopTransactionData[0].startDate, "dd-MM-yyyy");
                             var stopdate=  $filter('date')(StopTransactionData[0].stopDate, "dd-MM-yyyy");
                             growl.warning("Stop Transation is in Process for this Item(s) <br/>Start Date : "+ startdate +" <br/> Stop Date: "+ stopdate +" <br/> Days: "+ StopTransactionData[0].days +" <br/>  Hours: "+ StopTransactionData[0].hours +" <br/>  Minutes: "+ StopTransactionData[0].minutes +"  ");
                             $rootScope.stopLoader(); 
                             AllocateBatch(bill, item.id, item.batchId);
                      }
                    else{
                    	AllocateBatch(bill, item.id, item.batchId);
                   }
             });
        }
     */}

        var AllocateBatch = function (bill,itemId, batchId) {
            if(batchId != undefined){
                var URI = CONSTANTS.PHARMACY.OP.WALK_IN_SALES.ALLOCATE_BATCH_API  + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId + '/'+$scope.storeId + '/'+ $scope.entitlementObj.paymentEntitlementTypeId + '/' + bill.id;
                
                if($scope.barcodeScanned== true)
                	{
                	var data = {
                            "batchId": batchId,
                            "barcodeScanned": $scope.barcodeScanned
                        };
                	}
                else
                	{
                	var data = {
                            "batchId": itemId,
                            "barcodeScanned": $scope.barcodeScanned
                        };
                	}
                $rootScope.startLoader();
                GenericService.serviceAction("PUT", URI, data).then(function (response) {
                    if (!!response.data) {
                    	var check = false;
                        angular.forEach(bill.items,function(value,key){
                        	  
                                  if(value.selectedBatch != undefined && batchId == value.selectedBatch.batchId) {
                                      check = true;
                                  }
                                  
                            });
                        
                        if(check==true){
                            growl.error('There should not be the same batch for the same item.'); 
                            $rootScope.stopLoader();
                          return false;
                         }
                        var item = bill.items.find(function (obj) {
                            return obj.allocationId == response.data.allocationId;
                        });
                        // if (!!item) {
                        //     item.quantity = parseInt(item.quantity) + 1;
                        //     CalculateQuantity(item, item.selectedUom, item.uom);
                        //     CalculateAmount(item);
                        // } 
                        // else {
                        angular.forEach(response.data.batch, function (value, key) {
                        }, response.data);
                        var batch = response.data.batch.find(function (obj) {
                            return obj.batchId == batchId;
                        });
                        item = {
                		    itemDisabled:false,
                            disabled:false,
                            addBatchTopFlag: true,
                            // allocationId: response.data.allocationId,
                            itemId: response.data.itemId,
                            itemCode: response.data.itemCode,
                            itemName: response.data.itemName,
                            markUpDown: response.data.markUpDown,
                            markUpDownPer: response.data.markUpDownPer,
                            genericId: response.data.genericId,
                            genericName: response.data.genericName,
                            manufacturerId: response.data.manufacturerId,
                            manufacturerName: response.data.manufacturerName,
                            formulationType: response.data.formulationType,
                            stock: response.data.currentStock,
                            strength: response.data.strength,
                            productCategoryId:response.data.productCategoryId,
                            batch: response.data.batch,
                            // batch: [{
                            //     batchDetails: "Select Batch"
                            // }].concat(response.data.batch),
                            uom: [{
                                uomDetails: "Select UOM",
                            }],
                            taxId: response.data.taxId,
                            taxName: response.data.taxName,
                            taxPercent: response.data.taxPercent,
                            taxPercentage: response.data.taxPercent,
                            isOTC: response.data.isOTC,
                            patientClassPercentage:  response.data.patientClassPercentage,
                            storageUnitName: response.data.storageUnitName,
                            isThreeUOMApplicable: response.data.isThreeUOMApplicable,
                            barcodeItem: true,
                            onlySubButtonDisable: true
                        };
                        item.colorCode=response.data.colorCode;
                        item.isNarcotic=response.data.isNarcotic;
                        if(item.isNarcotic==true)
                        	{
                        	item.colorCode="red";
                        	}
                        if (!!batch) {
                            item.selectedBatch = batch;
                            item.uom = item.uom.concat(batch.uom);
                            item.selectedUom = batch.uom.find(function (obj) {
                                return obj.opUom;
                            });
                            item.quantity = 1;
                        } else {
                            //item.selectedBatch = item.batch[0];
                            item.selectedBatch = commonDetailService.getDefaultBatchFromConfig(item.batch);
                            if(item.selectedBatch!=undefined && item.selectedBatch!=null)
                        	{
                        item.selectedUom = item.selectedBatch.uom.find(function (obj) {
                            return obj.opUom;
                          });
                        	}
                        }
                        item.taxId=item.selectedBatch.taxId;
                        item.taxPercentage=item.selectedBatch.taxPercentage;
                        CalculateQuantity(item, item.selectedUom, item.uom);
                        CalculateRate(item, item.selectedUom, item.uom);
                        Calculate(item, self.model.worklistDetails)
                        CalculateAmount(item);
                        if(item.isThreeUOMApplicable == false){
                            item.selectedBatch.uomListData = item.selectedBatch.uom;
                            item.selectedBatch.uom = [];
                            angular.forEach(item.selectedBatch.uomListData, function(d){
                                if(d.uomTypeId != 2){
                                    item.selectedBatch.uom.push(d);
                                }
                            });
                        }
                        if($scope.handleDoubleClick==false)
                  	   {
                        bill.items.push(item);
                        $scope.handleDoubleClick=true;
                        }
                        if(bill.items.length>1)
                        	{
                        	$scope.flagRemove=true;
                        	}
                        var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.AVAILABLE_QTY_API  + $scope.unitId + "/"+ $scope.storeId+'/'+item.itemId+'/'+ batchId+'/'+item.selectedUom.uomTypeId;
                        GenericService.serviceAction("GET", URI, {}).then(
                        function (response) 
                        {
                        	var availableStockBatchList=[];
                        	angular.forEach(item.batch, function(batch){
                        		if(batch.availableStockQuantity>0)
                        			{
                        			availableStockBatchList.push(batch);
                        			}
                        	});
                        	item.batch=availableStockBatchList;
                            item.stock=response.data.availableqty;
                            item.selectedBatch.availableStockQuantity = response.data.availableqty;
                            //Ganesh
                            item.quantity = commonDetailService.getDefaultQuantity(item,item.qtyPrescribed,response.data.availableqty);
                            item.newStock =response.data.availableqty;
                            if(item.selectedBatch.uom!=undefined && item.selectedBatch.uom!=null)
                            	{
                            item.uom=item.selectedBatch.uom;
                            	}
                            ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);
                            if(self.model.worklistDetails.items.length > 1){
                                $scope.flagRemove = true;
                            }
                            else{
                                $scope.flagRemove = false;
                            }
                            $scope.handleDoubleClick=false;
                            $rootScope.stopLoader();
                        },
                        function (err) 
                        {
                            item.stock = 0;
                            item.newStock = 0;
                            $rootScope.stopLoader();
                        });
                        // }
                        self.model.barcode = "";
                        $rootScope.stopLoader();
                    }
                },
                function (ex) {
                    growl.error(ex.data.message);
                    $rootScope.stopLoader();
                });
            }
        }

        var ClearBarcode = function () {
            self.model.barcode = '';
        }

        var SetStatus = function (x) {
            var status = self.model.Status.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }

        var  clubBatches = function(batches){
        	// added by minesh for total of availableQuantity from all batch and get single Unique Batch
            var batchNo = [];
            var uniqueBatches = [];
        	
        		if (!!batches) {
        			batches.forEach(function (batch) {
        			var index = batchNo.indexOf(batch.batchId);                    			 
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
        		return uniqueBatches;
        	// added by minesh for total of availableQuantity from all  batch and get single Unique Batch
        }
        
        var GetCalculateOrederQty = function(item){
            var issuedQty = 0;
            if(item.qtyPrescribed!=0){
                var found =  item.batch.find(function (x) {
                    return x.isSelected;
                });
         	 
         	    var uomTypeId = item.uomTypeId !=undefined ?item.uomTypeId : (found !=undefined ?found.uomTypeId : undefined );
         	    if(uomTypeId == undefined){
         	        return issuedQty;
         	    }
	         	
                if(item.quantity == undefined || item.quantity == null || item.quantity == ""){
                    item.quantity = 0;
                }
         	    if(uomTypeId != undefined){
                    if(item.isThreeUOMApplicable == false){
                    	if(found.uomListData!=undefined && found.uomListData!=null)
                        {
                            if(item.statusId == 18 || item.statusId == 4){
                                if(uomTypeId == 3)
                                {
                                    issuedQty  = parseInt(item.quantityDispensedTemp == undefined ? (item.newdispQty == undefined ) ?item.newDispQty : item.newdispQty : item.quantityDispensedTemp);    
                                    item.receivedQtyCon = (issuedQty);     
                                    item.receivedQtyCalculation = (issuedQty);
                                    item.uomUnitData = found.uomListData[2].uomUnit;
                                }
                                else if(uomTypeId == 2)
                                { 	
                                    var stripQty = found.uomListData[1].conversion * parseInt(item.quantityDispensedTemp == undefined ? (item.newdispQty == undefined ) ? item.newDispQty : item.newdispQty: item.quantityDispensedTemp);
                                    issuedQty  = stripQty;               
                                }
                                else if(uomTypeId== 1)
                                {
                                    if(found.uomListData[0].conversion == undefined || found.uomListData[0].conversion == null){
                                        var boxQty = found.uomListData[1].conversion *  found.uomListData[2].conversion * parseInt(item.quantityDispensedTemp == undefined ? (item.newdispQty == undefined ) ?item.newDispQty : item.newdispQty : item.quantityDispensedTemp);
                                        issuedQty  = boxQty;
                                    }
                                    else{
                                        var boxQty = found.uomListData[0].conversion *  found.uomListData[1].conversion * parseInt(item.quantityDispensedTemp == undefined ? (item.newdispQty == undefined ) ?item.newDispQty : item.newdispQty : item.quantityDispensedTemp);
                                        issuedQty  = boxQty;
                                    }
                                    item.receivedQtyCon = (issuedQty +"*"+ found.uomListData[0].conversion);     
                                    item.receivedQtyCalculation = (issuedQty * found.uomListData[0].conversion);
                                    item.uomUnitData = found.uomListData[2].uomUnit;
                                }
                            }
                            else{
                                if(uomTypeId == 3)
                                {
                                    issuedQty  = parseInt(item.quantityDispensedTemp == undefined ? (item.newdispQty == undefined ) ?item.newDispQty : item.newdispQty : item.quantityDispensedTemp);    
                                    item.receivedQtyCon = (item.quantity);     
                                    item.receivedQtyCalculation = (item.quantity);
                                    item.uomUnitData = found.uomListData[2].uomUnit;
                                }
                                else if(uomTypeId == 2)
                                { 	
                                    var stripQty = found.uomListData[1].conversion * parseInt(item.quantityDispensedTemp == undefined ? (item.newdispQty == undefined ) ? item.newDispQty : item.newdispQty: item.quantityDispensedTemp);
                                    issuedQty  = stripQty;               
                                }
                                else if(uomTypeId== 1)
                                {
                                    if(found.uomListData[0].conversion == undefined || found.uomListData[0].conversion == null){
                                        var boxQty = found.uomListData[1].conversion *  found.uomListData[2].conversion * parseInt(item.quantityDispensedTemp == undefined ? (item.newdispQty == undefined ) ?item.newDispQty : item.newdispQty : item.quantityDispensedTemp);
                                        issuedQty  = boxQty;
                                    }
                                    else{
                                        var boxQty = found.uomListData[0].conversion *  found.uomListData[1].conversion * parseInt(item.quantityDispensedTemp == undefined ? (item.newdispQty == undefined ) ?item.newDispQty : item.newdispQty : item.quantityDispensedTemp);
                                        issuedQty  = boxQty;
                                    }
                                    item.receivedQtyCon = (item.quantity +"*"+ found.uomListData[0].conversion);     
                                    item.receivedQtyCalculation = (item.quantity * found.uomListData[0].conversion);
                                    item.uomUnitData = found.uomListData[2].uomUnit;
                                }
                            }
                        }
                    	else
                        {
                            if(item.statusId == 18 || item.statusId == 4){
                                if(uomTypeId == 3)
                                {
                                    issuedQty  = parseInt(item.quantityDispensedTemp == undefined ? (item.newdispQty == undefined ) ?item.newDispQty : item.newdispQty : item.quantityDispensedTemp);        
                                    item.receivedQtyCon = (issuedQty);     
                                    item.receivedQtyCalculation = (issuedQty);
                                    item.uomUnitData = found.uom[2].uomUnit;
                                }
                                else if(uomTypeId == 2)
                                { 	
                                    var stripQty = found.uom[1].conversion * parseInt(item.quantityDispensedTemp == undefined ? (item.newdispQty == undefined ) ? item.newDispQty : item.newdispQty: item.quantityDispensedTemp);
                                    issuedQty  = stripQty;               
                                }
                                else if(uomTypeId== 1)
                                {
                                    if(found.uom[0].conversion == undefined || found.uom[0].conversion == null){
                                        var boxQty = found.uom[1].conversion *  found.uom[2].conversion * parseInt(item.quantityDispensedTemp == undefined ? (item.newdispQty == undefined ) ?item.newDispQty : item.newdispQty : item.quantityDispensedTemp);
                                        issuedQty  = boxQty;
                                    }
                                    else{
                                        var boxQty = found.uom[0].conversion *  found.uom[1].conversion * parseInt(item.quantityDispensedTemp == undefined ? (item.newdispQty == undefined ) ?item.newDispQty : item.newdispQty : item.quantityDispensedTemp);
                                        issuedQty  = boxQty;
                                    }
                                    item.receivedQtyCon = (issuedQty +"*"+ found.uom[0].conversion);     
                                    item.receivedQtyCalculation = (issuedQty * found.uom[0].conversion);
                                    item.uomUnitData = found.uom[2].uomUnit;
                                }
                            }
                            else{
                                if(uomTypeId == 3)
                                {
                                    issuedQty  = parseInt(item.quantityDispensedTemp == undefined ? (item.newdispQty == undefined ) ?item.newDispQty : item.newdispQty : item.quantityDispensedTemp);        
                                    item.receivedQtyCon = (item.quantity);     
                                    item.receivedQtyCalculation = (item.quantity);
                                    item.uomUnitData = found.uom[2].uomUnit;
                                }
                                else if(uomTypeId == 2)
                                { 	
                                    var stripQty = found.uom[1].conversion * parseInt(item.quantityDispensedTemp == undefined ? (item.newdispQty == undefined ) ? item.newDispQty : item.newdispQty: item.quantityDispensedTemp);
                                    issuedQty  = stripQty;               
                                }
                                else if(uomTypeId== 1)
                                {
                                    if(found.uom[0].conversion == undefined || found.uom[0].conversion == null){
                                        var boxQty = found.uom[1].conversion *  found.uom[2].conversion * parseInt(item.quantityDispensedTemp == undefined ? (item.newdispQty == undefined ) ?item.newDispQty : item.newdispQty : item.quantityDispensedTemp);
                                        issuedQty  = boxQty;
                                    }
                                    else{
                                        var boxQty = found.uom[0].conversion *  found.uom[1].conversion * parseInt(item.quantityDispensedTemp == undefined ? (item.newdispQty == undefined ) ?item.newDispQty : item.newdispQty : item.quantityDispensedTemp);
                                        issuedQty  = boxQty;   
                                    }
                                    item.receivedQtyCon = (item.quantity +"*"+ found.uom[0].conversion);     
                                    item.receivedQtyCalculation = (item.quantity * found.uom[0].conversion);
                                    item.uomUnitData = found.uom[2].uomUnit;
                                }
                            }
                        }
                    }
                    else{
                        if(item.statusId == 18 || item.statusId == 4){
                            if(uomTypeId == 3)
                            {
                                issuedQty  = parseInt(item.quantityDispensedTemp == undefined ? (item.newdispQty == undefined ) ?item.newDispQty : item.newdispQty : item.quantityDispensedTemp);        
                                item.receivedQtyCon = (item.newDispQty);     
                                item.receivedQtyCalculation = (item.newDispQty);
                                item.uomUnitData = found.uom[2].uomUnit;
                            }
                            else if(uomTypeId == 2)
                            { 	
                                var stripQty = found.uom[1].conversion * parseInt(item.quantityDispensedTemp == undefined ? (item.newdispQty == undefined ) ? item.newDispQty : item.newdispQty: item.quantityDispensedTemp);
                                issuedQty  = stripQty;       
                                item.receivedQtyCon = (item.newDispQty +"*"+ found.uom[1].conversion);     
                                item.receivedQtyCalculation = (item.newDispQty * found.uom[1].conversion);
                                item.uomUnitData = found.uom[2].uomUnit;        
                            }
                            else if(uomTypeId== 1)
                            {
                                var boxQty = found.uom[1].conversion *  found.uom[2].conversion * parseInt(item.quantityDispensedTemp == undefined ? (item.newdispQty == undefined ) ?item.newDispQty : item.newdispQty : item.quantityDispensedTemp);
                                issuedQty  = boxQty;
                                item.receivedQtyCon = (item.newDispQty +"*"+ found.uom[0].conversion +"*"+ found.uom[1].conversion);     
                                item.receivedQtyCalculation = (item.newDispQty * found.uom[0].conversion * found.uom[1].conversion);
                                item.uomUnitData = found.uom[2].uomUnit;
                            }
                        }
                        else{
                            if(uomTypeId == 3)
                            {
                                issuedQty  = parseInt(item.quantityDispensedTemp == undefined ? (item.newdispQty == undefined ) ?item.newDispQty : item.newdispQty : item.quantityDispensedTemp);        
                                item.receivedQtyCon = (item.quantity);     
                                item.receivedQtyCalculation = (item.quantity);
                                item.uomUnitData = found.uom[2].uomUnit;
                            }
                            else if(uomTypeId == 2)
                            { 	
                                var stripQty = found.uom[1].conversion * parseInt(item.quantityDispensedTemp == undefined ? (item.newdispQty == undefined ) ? item.newDispQty : item.newdispQty: item.quantityDispensedTemp);
                                issuedQty  = stripQty;       
                                item.receivedQtyCon = (item.quantity +"*"+ found.uom[1].conversion);     
                                item.receivedQtyCalculation = (item.quantity * found.uom[1].conversion);
                                item.uomUnitData = found.uom[2].uomUnit;        
                            }
                            else if(uomTypeId== 1)
                            {
                                var boxQty = found.uom[1].conversion *  found.uom[2].conversion * parseInt(item.quantityDispensedTemp == undefined ? (item.newdispQty == undefined ) ?item.newDispQty : item.newdispQty : item.quantityDispensedTemp);
                                issuedQty  = boxQty;
                                item.receivedQtyCon = (item.quantity +"*"+ found.uom[0].conversion +"*"+ found.uom[1].conversion);     
                                item.receivedQtyCalculation = (item.quantity * found.uom[0].conversion * found.uom[1].conversion);
                                item.uomUnitData = found.uom[2].uomUnit;
                            }
                        }
                    }  
                }
            }
            return issuedQty;
        }
       
       var  uomConversion = function(item,batchObj,selectedUomId,parentItemQty){
       	if(parentItemQty !=undefined){
       		var quantityOrdered = item.qtyPrescribed  - parentItemQty;
       	}else{
       		var quantityOrdered = item.qtyPrescribed;
       	}
       	// var pickedQty = item.pickedQuantity!=undefined ?item.pickedQuantity : 0;
            if (batchObj && batchObj.uom != undefined) { 
                if (selectedUomId == 1) {
               	 item.newQty = Math.ceil(quantityOrdered / (batchObj.uom[0].conversion * batchObj.uom[1].conversion * batchObj.uom[2].conversion));
                //    item.newPickedQty = Math.ceil(pickedQty / (batchObj.uom[0].conversion * batchObj.uom[1].conversion * batchObj.uom[2].conversion));
                    item.newStock = Math.ceil(batchObj.availableStockQuantity / (batchObj.uom[0].conversion * batchObj.uom[1].conversion * batchObj.uom[2].conversion));
                } else if (selectedUomId == 2) {
                    item.newQty = Math.ceil(quantityOrdered / (batchObj.uom[1].conversion * batchObj.uom[2].conversion));
                  //  item.newPickedQty = Math.ceil(pickedQty / ( batchObj.uom[1].conversion * batchObj.uom[2].conversion));
                    item.newStock = Math.ceil(batchObj.availableStockQuantity / (batchObj.uom[1].conversion * batchObj.uom[2].conversion));
                } else if (selectedUomId == 3) {
                    item.newQty = Math.ceil(quantityOrdered / (batchObj.uom[2].conversion));
                  //  item.newPickedQty = Math.ceil(pickedQty / (batchObj.uom[2].conversion));
                    item.newStock = Math.ceil(batchObj.availableStockQuantity / (batchObj.uom[2].conversion));
                }
            }
            else{
                item.newQty = item.qtyPrescribed;
                item.newStock = 0;
            }
       }
        
       
   	  $scope.showNonFormularyItemList = function(){
			try{
				$scope.savePriscritpionId = self.model.issueId;
				$rootScope.startLoader();
			    var URI = BASE_URL + ROOT_URL + "/api/opd/getNonFormularyItemForWorklistPharmacy/" +self.model.issueId +"/"+ $scope.unitId+"/"+ $scope.organizationId ;
				GenericService.serviceAction("GET",URI).then(function(response){
					if(response.data.status == "success"){
						$scope.nonFormularyItemList = [];
						$scope.nonFormularyItemList = response.data.listObject;
						if($scope.nonFormularyItemList != null && $scope.nonFormularyItemList.length > 0 ){
							$("#nooformularyModal").show();
						}
						$rootScope.stopLoader();
					}else if(response.data.status == 'error'){
						$rootScope.stopLoader();
						growl.warning(response.data.message,
								{
									title : 'Error!'
								});
					}
				});
				
			}catch(e){
				console.log("Exception-",e);
			}
		}

		   	$scope.closePopupsj = function(){
		   		$rootScope.stopLoader();
		   		$("#nooformularyModal").hide();
		   	}
   	  $scope.deleteNonFormulary = function(){
			try{
				
				angular.forEach($scope.servicesListPopup,function(v,k){
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					var data={
						    updatedBy:$scope.userId,
							justificationStop:"Item Added in pharmacy",
							prescriptionDetailsid:v.prescriptionDetailsId,
							
					};
					
					var URI = BASE_URL + ROOT_URL + "/api/opd/deleteMedicationForPharmacy";
					GenericService.serviceAction("POST",URI,data).then(function(response){
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
						
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							
						}
					});
				});
				
				
			}catch(e){
				console.log("Exception-",e);
			}
		}
   	  
   	 $scope.saveDataInPrescription = function( bill,addToBill ){
   		
   			var data = {
  				  "organizationId": $scope.organizationId,
  				  "unitId": $scope.unitId,
  				  "employeeId" : $scope.userId, 
  				  "visitTypeId":1,
  				  "doctorId":self.model.doctorId,
  				  "prescriptionDate":moment(new Date()).format('YYYY-MM-DD  HH:mm:ss'),
  				  "authorisedBy": self.model.doctorId,
  				  "authorisedDate":moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
  				  "addedBy": $scope.userId,
  				  "addedDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
  				  "encounterId":$scope.encounterId,
  				  "prescriptionId":$scope.savePriscritpionId ,
  				  "patientId":$scope.idpatient,
  				  "priorityId":2,
  				  "deptId" : self.model.specialityId,
  				  "isFromOP" :true,
  				  "medicationType":1
  		    };
   		
   		    $scope.medicationDetailList = [];
   		
   			angular.forEach($scope.servicesListPopup,function(value, key) {
					var medicationDetailsObj = {};
					medicationDetailsObj.itemId = value.id;
					medicationDetailsObj.oldPrescriptionId = value.prescriptionDetailsId;
					medicationDetailsObj.storeId = $scope.storeId;
					medicationDetailsObj.uomUnitId = "";
					medicationDetailsObj.isMedicationStop = "N";
					medicationDetailsObj.orderQty =value.orderQty;
					medicationDetailsObj.priorityId =1;
					medicationDetailsObj.prescriptionDate =  moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
					medicationDetailsObj.authorisedDate =  moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
					medicationDetailsObj.startDate =  value.startDate+ " 00:00:00";
					medicationDetailsObj.stopDate =  value.stopDate+ " 00:00:00";
					medicationDetailsObj.authorisedBy =  $scope.userId;
					medicationDetailsObj.dose =  value.prescribedDose;
					var arr = value.details.split("|"); 
					medicationDetailsObj.itemName =  arr[1];
					medicationDetailsObj.genericName =  value.genericName;
					medicationDetailsObj.doseUnitId =  value.doseUnitId;
					medicationDetailsObj.frequencyId =  value.frequencyId;
					medicationDetailsObj.routeId =  value.roa_id;
					medicationDetailsObj.isNonFormulary = "N";
					medicationDetailsObj.statflag = false;
					medicationDetailsObj.duration = value.duration;
					medicationDetailsObj.durationUnit = value.durationUnit;
					medicationDetailsObj.instructionDesc = value.instructionDesc;
					medicationDetailsObj.isDischarge = false;
					medicationDetailsObj.prescribedDose = value.prescribedDose;
					medicationDetailsObj.prescribedDoseUnitId = value.prescribedDoseUnitId;
					medicationDetailsObj.factorId =value.factorId;
					medicationDetailsObj.instructionDesc =value.instructionDesc;
				$scope.medicationDetailList.push(medicationDetailsObj);
			});
   			if ($scope.medicationDetailList.length > 0) {
				data.prescriptionDetailsDtosList = $scope.medicationDetailList;
				console.log("medicationDetailList", data);
				var URI = BASE_URL + ROOT_URL + "/api/opd/saveMedicationPrescriptionDrtailsSkipPrescription";
				GenericService.serviceAction("POST",URI,data).then(function(response){
					if (response.data.status == 'success')
					{
						$scope.medicationObsdhb = [];
						$scope.medicationObsdhb = response.data.object.prescriptionDetailsDtosList;
						$rootScope.stopLoader();
						$scope.deleteNonFormulary();
						angular.forEach($scope.medicationObsdhb,function(b, key) {
						angular.forEach(bill.items,function(value, key) {
							if(value.oldPrescriptionId ==  b.oldPrescriptionId ){
								value.prescriptionDetailsId = b.prescriptionDetailsid;
								value.prescriptionDetailId = b.prescriptionDetailsid;
							}
						});
						});
						ActualSaveBill(bill,addToBill);
						$scope.NonFormularyAdded = false;
					} else if(response.data.status == 'error')
					{
						$rootScope.stopLoader();
						$scope.deleteNonFormulary();
						
					}
				});
				
			}
   	 }
      // chetana
      $scope.item={};
  	$scope.mod="";
  	$scope.labl="";
  	$scope.servicesListPopup =[];
      $scope.addServiceToTempServiceList = function($item, $model, $label,info,freeTextserviceName)
      {
    	  
    	//check duplicate 
    	  var checekkdsk = false;
     	  angular.forEach($scope.servicesListPopup,function(b,j){
             var found =  $scope.servicesListPopup.find(ar=>ar.itemId == $item.id);
			    if(found != undefined){
			    	checekkdsk = true;
			    }
    	  });
    	  
     	  angular.forEach(self.model.worklistDetails.items,function(b,j){
              var found =  self.model.worklistDetails.items.find(ar=>ar.itemId == $item.id);
 			    if(found != undefined){
 			    	checekkdsk = true;
 			    }
     	  });
     	  
     	  if(checekkdsk == true){
     		 growl.error("Medication Already Exist");
     		 return false;
     	  }
     	  
    	  
    	$rootScope.startLoaderPopup();
      	$scope.prescriptionDetailsid = info.prescriptionDetailsid;
      	$scope.freeTextserviceName = freeTextserviceName;
      	$scope.item={};
  		$scope.mod="";
  		$scope.labl="";
  		var nrsdcnsdnc = {};
  		nrsdcnsdnc.assetCategoryId = $item.assetCategoryId;
  		nrsdcnsdnc.details = $item.details;
  		nrsdcnsdnc.formulationTypeId = $item.formulationTypeId;
  		nrsdcnsdnc.id = $item.id;
  		nrsdcnsdnc.isThreeUOMApplicable = $item.isThreeUOMApplicable;
  		nrsdcnsdnc.markUpDown = $item.markUpDown;
  		nrsdcnsdnc.markUpDownPer = $item.markUpDownPer;
  		nrsdcnsdnc.markupStatus = $item.markupStatus;
  		nrsdcnsdnc.patientClassPercentage = $item.patientClassPercentage;
  		nrsdcnsdnc.productCategoryId = $item.productCategoryId;
  		nrsdcnsdnc.searchItem =freeTextserviceName;
  		nrsdcnsdnc.prescriptionDetailsId = info.prescriptionDetailsid;
  		nrsdcnsdnc.oldPrescriptionId = info.prescriptionDetailsid;
  		nrsdcnsdnc.prescribedDose = info.prescribeDose;
  		nrsdcnsdnc.units = info.strengthDesc;
  		nrsdcnsdnc.duration = info.durationCount;
  		nrsdcnsdnc.durationUnit = info.durationUnit;
  		nrsdcnsdnc.frequencyDesc = info.searchItemFre;
  		nrsdcnsdnc.qtyPrescribed = info.quantity;
  		nrsdcnsdnc.orderQty = info.quantity;
  		nrsdcnsdnc.instructionDesc = info.instructionText;
  		nrsdcnsdnc.itemId = $item.id;
  		nrsdcnsdnc.roa_id = info.roa_id;
  		nrsdcnsdnc.frequencyId = info.frequencyId;
  		nrsdcnsdnc.startDate = info.startDate;
  		nrsdcnsdnc.stopDate = info.stopDate;
  		nrsdcnsdnc.isThreeUOMApplicable = $item.isThreeUOMApplicable;
        
    	$scope.encounterId = self.model.worklistDetails.encounterId;
        var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId + '/'+$scope.storeId+'/' + $item.id + '/' + $scope.entitlementObj.paymentEntitlementTypeId;

        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {})
        .then(function (response) {
        	 response.data["itemDisabled"]=false;
             response.data["disabled"]=false;
            $scope.data = response.data;
            //addded by minesh
            var clubData  = clubBatches(response.data.batch);
            response.data.batch = clubData;
            //end added by minesh
            
            angular.forEach(response.data.batch, function (value, key) {
            if(!!this.markUpDownPer){
                var markUpDown = (value.sellingPrice * (this.markUpDownPer / 100));
            }
            }, response.data);


            $scope.patientConsation(response.data);
            $timeout( function(){
            	nrsdcnsdnc.isConsumed = false;
            	nrsdcnsdnc.isConsumed  = false;
            	nrsdcnsdnc.itemDisabled = false;
            	nrsdcnsdnc.disabled = false;
            	nrsdcnsdnc.itemId = response.data.itemId;
            	nrsdcnsdnc.itemCode = response.data.itemCode;
            	nrsdcnsdnc.markUpDown = response.data.markUpDown;
            	nrsdcnsdnc.itemName = response.data.itemName;
            	nrsdcnsdnc.markUpDownPer = response.data.markUpDownPer;
            	nrsdcnsdnc.formulationType = response.data.formulationType;
            	nrsdcnsdnc.stock = response.data.currentStock;
            	nrsdcnsdnc.genericId = response.data.genericId;
            	nrsdcnsdnc.genericName = response.data.genericName;
            	nrsdcnsdnc.manufacturerId = response.data.manufacturerId;
            	nrsdcnsdnc.manufacturerName = response.data.manufacturerName;
            	nrsdcnsdnc.strength = response.data.strength;
            	nrsdcnsdnc.batch = response.data.batch;
            	nrsdcnsdnc.productCategoryId =response.data.productCategoryId;
            	nrsdcnsdnc.assetCategoryId =response.data.assetCategoryId;
            	nrsdcnsdnc.selectedBatch = response.data.batch[0];
            	nrsdcnsdnc.uom= [{
                    uomDetails: "Select UOM",
                }];
            	nrsdcnsdnc.taxId = response.data.taxId;
            	nrsdcnsdnc.tax = response.data.taxName;
            	nrsdcnsdnc.taxPercentage = response.data.taxPercent;
            	nrsdcnsdnc.isOTC= true;
            	nrsdcnsdnc.patientClassPercentage = $scope.patientClassPercentage;
            	nrsdcnsdnc.saleDetailPayeeMapperDtoList =[];
            	nrsdcnsdnc.SaleDetailPackageMapperList =[];
                nrsdcnsdnc.storageUnitName = response.data.storageUnitName;
                nrsdcnsdnc.isThreeUOMApplicable = response.data.isThreeUOMApplicable;
                nrsdcnsdnc.onlySubButtonDisable =false;
                nrsdcnsdnc.colorCode=response.data.colorCode;
                nrsdcnsdnc.selectedBatch = commonDetailService.getDefaultBatchFromConfig(response.data.batch);
                if(nrsdcnsdnc.selectedBatch!=undefined)
                	{
                	nrsdcnsdnc.selectedUom = nrsdcnsdnc.selectedBatch.uom.find(function (obj) {
                           return obj.opUom;
                           });
                	   
                	  
                	}
                else{
                	nrsdcnsdnc.selectedUom = item.uom[0];
                }
                console.log("1",nrsdcnsdnc);
                nrsdcnsdnc.checkNewItem = true;
               /*if(nrsdcnsdnc.selectedBatch!=undefined)
            	{
                  BatchChange(self.model.worklistDetails.items.length-1,self.model.worklistDetails, nrsdcnsdnc.selectedBatch,null);
            	}*/
                
                
                var item = null;
                if($item.isThreeUOMApplicable== false){
                    if(self.model.worklistDetails.statusId != 18){
                        if(nrsdcnsdnc.uomListData == undefined || nrsdcnsdnc.uomListData == null){
                            if(nrsdcnsdnc.selectedBatch.uom[2] == undefined || nrsdcnsdnc.selectedBatch.uom[2] == null){
                            	nrsdcnsdnc.uomListData = nrsdcnsdnc.selectedBatch.uomListData;
                            }
                            else{
                            	nrsdcnsdnc.uomListData = nrsdcnsdnc.selectedBatch.uom;
                            }
                        }
                        if(nrsdcnsdnc.selectedBatch.uomListData == undefined || nrsdcnsdnc.selectedBatch.uomListData == null){
                        	nrsdcnsdnc.selectedBatch.uomListData = nrsdcnsdnc.selectedBatch.uom;
                        }
                    }
                    
                    // bill.items[index].uom = [];
                    if(nrsdcnsdnc.onlySubButtonDisable == true){
                        if(nrsdcnsdnc.uom == undefined || nrsdcnsdnc.uom == null || nrsdcnsdnc.uom[0].conversion == undefined || nrsdcnsdnc.uom[0].conversion == null){
                        	nrsdcnsdnc.uom = nrsdcnsdnc.selectedBatch.uom;
                        }
                    }
                    nrsdcnsdnc.selectedBatch.uom = [];
                    if(nrsdcnsdnc.selectedBatch.uomListData == undefined){
                        if(nrsdcnsdnc.uomListData == undefined){
                        	nrsdcnsdnc.uomListData = nrsdcnsdnc.uom;
                        }
                        angular.forEach(nrsdcnsdnc.uom, function(u){
                            if(u.uomTypeId != 2){
                            	nrsdcnsdnc.selectedBatch.uom.push(u);
                                // bill.items[index].uom.push(u);
                            }
                        })
                    }
                    else{
                        angular.forEach(nrsdcnsdnc.selectedBatch.uomListData, function(u){
                            if(u.uomTypeId != 2){
                            	nrsdcnsdnc.selectedBatch.uom.push(u);
                                // bill.items[index].uom.push(u);
                            }
                        })
                    }
                    item = nrsdcnsdnc;
                }
                else{
                	nrsdcnsdnc.uom = nrsdcnsdnc.selectedBatch.uom;
                    item = nrsdcnsdnc;
                }
            

                //Select Default UOM
                if(nrsdcnsdnc.isThreeUOMApplicable == false){
                    if(nrsdcnsdnc.uomListData == undefined || nrsdcnsdnc.uomListData == null || nrsdcnsdnc.uomListData == ""){
                    	nrsdcnsdnc.selectedUom = $filter('filter')(nrsdcnsdnc.uom, {
                            opUom: true
                        }, true)[0];
                    }
                    else{
                    	nrsdcnsdnc.selectedUom = $filter('filter')(nrsdcnsdnc.uomListData, {
                            opUom: true
                        }, true)[0];
                    }
                }
                else{
                	nrsdcnsdnc.selectedUom = $filter('filter')(nrsdcnsdnc.uom, {
                        opUom: true
                    }, true)[0];
                }

                //"Select UOM" option from UOM dropdown
                if (!nrsdcnsdnc.selectedUom) {
                	nrsdcnsdnc.selectedUom = nrsdcnsdnc.uom[0];
                }
                
              
                
                //Caclulate amount including discount
                /*if(bill.items[index].quantity != 0){
                if(item.quantity !=undefined && item.quantity != 0 ){
                    Calculate(item, bill);
                }*/

               // CalculateAmount(item);

                if(nrsdcnsdnc.selectedBatch.availableStockQuantity != undefined && nrsdcnsdnc.selectedBatch.availableStockQuantity != null){
                    //Convert Ordered Quantity
                    if(nrsdcnsdnc.isThreeUOMApplicable == false){
                        if(nrsdcnsdnc.selectedBatch.uomListData == undefined){
                            ConvertQtyOrdered(nrsdcnsdnc, nrsdcnsdnc.selectedBatch.uomListData, nrsdcnsdnc.selectedUom);
                        }
                        else{
                            ConvertQtyOrdered(nrsdcnsdnc, nrsdcnsdnc.selectedBatch.uom, nrsdcnsdnc.selectedUom);
                        }
                    }
                    else{
                        ConvertQtyOrdered(nrsdcnsdnc, nrsdcnsdnc.selectedBatch.uom, nrsdcnsdnc.selectedUom);
                    }
//                    $scope.selectedUomData = item.selectedUom;
                    //Allocate quantity
                    if(nrsdcnsdnc.quantity !=undefined && nrsdcnsdnc.quantity != 0 ){
                	    // AllocateQuantity(index, bill, parentIndex);
                        Calculate(nrsdcnsdnc, self.model.worklistDetails);
                    }
                    //AllocateQuantity(index, bill, parentIndex);               

                    //Get current stock for the selected batch
                    var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.AVAILABLE_QTY_API  + $scope.unitId + "/"+ $scope.storeId + "/" + item.itemId + "/" + item.selectedBatch.batchId + "/" + item.selectedUom.uomTypeId;
                    $rootScope.startLoader();
                    GenericService.serviceAction("GET", URI, {}).then(
                    function (response) {
                    	nrsdcnsdnc.stock= response.data.availableqty;
                        
                        //Ganesh
                    	nrsdcnsdnc.quantity = commonDetailService.getDefaultQuantity(item,item.qtyPrescribed,response.data.availableqty);
                        
                        
                        //                        item.newStock = response.data.availableqty;
                        if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                            $scope.isValidated = true;
                        }
                        else{
                            $scope.isValidated = false;
                        }
                        if($scope.activePackageList!=null&& $scope.activePackageList.length>0){
                            $scope.isValidated = true;
                        }
                        if($scope.atCop == true){
                        	nrsdcnsdnc.rate = item.selectedBatch.actualLendingPrice;
                          Calculate(nrsdcnsdnc, self.model.worklistDetails);
                        }
                        else{
                            if(nrsdcnsdnc.selectedBatch.rate != undefined || nrsdcnsdnc.selectedBatch.rate != null){
                            	nrsdcnsdnc.rate = nrsdcnsdnc.selectedBatch.rate;
                                Calculate(nrsdcnsdnc, self.model.worklistDetails);
                            }
                            else{
                            	nrsdcnsdnc.rate = nrsdcnsdnc.rate;
                            }
                        }
                        
                        console.log("2",nrsdcnsdnc);
                        if(self.model.worklistDetails.items.length > 1){
                            $scope.flagRemove = true;
                        }
                        else{
                            $scope.flagRemove = false;
                        }
                        var serviceObj1 = $scope.servicesListPopup.find(obj => {return obj.itemId == nrsdcnsdnc.id})
                    	if(serviceObj1 != undefined && $scope.serviceCheckFlag==false){
                    		   
                    		growl.warning("This medication already exist",
            						{
            							title : 'Error!'
            						});
              				 
                    	         return false;
                    	} 
                        console.log("3",nrsdcnsdnc);
//                        nrsdcnsdnc.newQty = item.quantity;
//                        nrsdcnsdnc.newStock = item.availableStockQuantity;
                        nrsdcnsdnc.status ='New';
                        nrsdcnsdnc.selectedItems =[];
                        nrsdcnsdnc.substituteButtonFlag1 =false;
                        nrsdcnsdnc.disableInputField = false;
                        nrsdcnsdnc.isShow = true;
                        nrsdcnsdnc.flagRemove = true;
                        nrsdcnsdnc.onlySubButtonDisable = false;
                        $scope.saveFlag = false; 
                        nrsdcnsdnc.qtyPrescribed = info.quantity;
                        nrsdcnsdnc.checkNewItem = undefined;
                  	$scope.servicesListPopup.push(nrsdcnsdnc);
                  	 $timeout(function() {
                  		BatchChangeForNonFormulary($scope.servicesListPopup.length-1,nrsdcnsdnc, nrsdcnsdnc.selectedBatch,null);
        		      	  $rootScope.stopLoader();
              		  }, 1000);
                	angular.forEach($scope.nonFormularyItemList, function (value, key) {
            			if($scope.prescriptionDetailsid==value.prescriptionDetailsid){
            					value.status = 'I';
            				}
            			});  
                        $rootScope.stopLoader();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                    });
                }
                else{
                    //AllocateQuantity(index, bill, parentIndex);               

                    //Get current stock for the selected batch
                    var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.AVAILABLE_QTY_API  + $scope.unitId + "/"+ $scope.storeId + "/" + item.itemId + "/" + item.selectedBatch.batchId + "/" + item.selectedUom.uomTypeId;
                    $rootScope.startLoader();
                    GenericService.serviceAction("GET", URI, {}).then(
                        function (response) {
                            item.stock= response.data.availableqty;
                            item.selectedBatch.availableStockQuantity = response.data.availableqty;
                            //Ganesh
                            item.quantity = commonDetailService.getDefaultQuantity(item,item.qtyPrescribed,response.data.availableqty);
                        
                            //                        item.newStock = response.data.availableqty;
                            if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                                $scope.isValidated = true;
                            }
                            else{
                                $scope.isValidated = false;
                            }
                            if($scope.activePackageList!=null&& $scope.activePackageList.length>0){
                                $scope.isValidated = true;
                            }
                            if($scope.atCop == true){
                                item.rate = item.selectedBatch.actualLendingPrice;
                                Calculate(item, self.model.worklistDetails);
                            }
                            else{
                                if(item.selectedBatch.rate != undefined || item.selectedBatch.rate != null){
                                    item.rate = item.selectedBatch.rate;
                                    Calculate(item, self.model.worklistDetails);
                                }
                                else{
                                    item.rate = item.rate;
                                }
                            }
                            if(item.isThreeUOMApplicable == false){
                                ConvertQtyOrdered(item, item.selectedBatch.uomListData, item.selectedUom);
                            }
                            else{
                                ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);
                            }
                            $scope.selectedUomData = item.selectedUom;
                            
                            console.log("2",nrsdcnsdnc);
                            if(self.model.worklistDetails.items.length > 1){
                                $scope.flagRemove = true;
                            }
                            else{
                                $scope.flagRemove = false;
                            }
                            var serviceObj1 = $scope.servicesListPopup.find(obj => {return obj.itemId == nrsdcnsdnc.id})
                        	if(serviceObj1 != undefined && $scope.serviceCheckFlag==false){
                        		   
                        		growl.warning("This medication already exist",
                						{
                							title : 'Error!'
                						});
                  				 
                        	         return false;
                        	} 
                            console.log("3",nrsdcnsdnc);
//                            nrsdcnsdnc.newQty = item.quantity;
//                            nrsdcnsdnc.newStock = item.availableStockQuantity;
                            nrsdcnsdnc.status ='New';
                            nrsdcnsdnc.selectedItems =[];
                            nrsdcnsdnc.substituteButtonFlag1 =false;
                            nrsdcnsdnc.disableInputField = false;
                            nrsdcnsdnc.isShow = true;
                            nrsdcnsdnc.flagRemove = true;
                            nrsdcnsdnc.onlySubButtonDisable = false;
                            $scope.saveFlag = false; 
                            nrsdcnsdnc.qtyPrescribed = info.quantity;
                            nrsdcnsdnc.checkNewItem = undefined;
                      	$scope.servicesListPopup.push(nrsdcnsdnc);
                      	 $timeout(function() {
                      		BatchChangeForNonFormulary($scope.servicesListPopup.length-1,nrsdcnsdnc, nrsdcnsdnc.selectedBatch,null);
            		      	  $rootScope.stopLoader();
                  		  }, 1000);
                    	angular.forEach($scope.nonFormularyItemList, function (value, key) {
                			if($scope.prescriptionDetailsid==value.prescriptionDetailsid){
                					value.status = 'I';
                				}
                			});  
                            $rootScope.stopLoader();
                        },
                        function (err) {
                            $rootScope.stopLoader();
                        }
                    );
                }
            
                
               
        	$rootScope.stopLoaderPopup();
           },2000);
        
        }, function (err) {
            growl.error(err.data.message);
            $rootScope.stopLoaderPopup();
        });
    
        
      	
      	
  		       
  	}
      
      $scope.removeTempServicesByIndex = function(index)
      {
    	  
    	  $scope.removeIdPrescriptionId = $scope.servicesListPopup[index].prescriptionDetailsId;
      	angular.forEach($scope.nonFormularyItemList, function (value, key) {
  			if($scope.servicesListPopup[index].prescriptionDetailsId!=undefined && $scope.servicesListPopup[index].prescriptionDetailsId!= null && $scope.servicesListPopup[index].prescriptionDetailsId==value.prescriptionDetailsid){
  					value.status = 'A';
  				}
  			});
      	
  		$scope.servicesListPopup.splice(index, 1);
  		
  		angular.forEach(self.model.worklistDetails.items, function (item1, subindex) {
  			if(item1.prescriptionDetailsId ==  $scope.removeIdPrescriptionId ){
  				self.model.worklistDetails.items.splice(subindex, 1);
  				 $scope.orderQtyTotalSum =  parseFloat($scope.orderQtyTotalSum) - parseFloat(item1.qtyPrescribed);
  			}
  		});
  	}
      $scope.NonFormularyAdded = false;
      $scope.addTempServiesToMedicationMasterList = function(index)
      {
    	  
    	  
    	  $scope.NonFormularyAdded = true;
    	  angular.forEach($scope.servicesListPopup,function(b,j){
    		  b.NonFormularyAddedFlag = true;
    		  if($scope.autoPrescribedQty == true){
  				$scope.calculateReceivedQty1(b);
          		Calculate(b, self.model.worklistDetails);
              }
    		  if(self.model.worklistDetails.items.length == 0){
    			  self.model.worklistDetails.items.push(b);
              }else{
             	 var found =  self.model.worklistDetails.items.find(ar=>ar.prescriptionDetailsId == b.prescriptionDetailsId);
					   if(found == undefined){
						   b.substituteBtn = true;
						   self.model.worklistDetails.items.push(b);
						   $scope.orderQtyTotalSum =  parseFloat($scope.orderQtyTotalSum) + parseFloat(b.qtyPrescribed);
					   }
              }
    	  });
          if(self.model.worklistDetails.items.length>1)
          {
          $scope.flagRemove=true;
          }
    	 console.log("final list",self.model.worklistDetails);
      	$("#nooformularyModal").hide();
      	
  	}
      
   	  
        var GetWorklistDetails = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.DETAILS_API + $scope.unitId + '/'+$scope.storeId + '/' + self.model.issueId;

            self.model.worklistDetails.discountCatgeory = self.model.discountCategoryList[0];

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                	 $rootScope.stopLoader();
                	self.model.remark = response.data.remark;
                    self.model.worklistDetails = response.data;
                    $scope.orderListCount = 0;
                    angular.forEach(self.model.worklistDetails.items, function (item1, subindex) {
                    	if(item1.pricId==0)
                        {
                                item1.duration="";
                                item1.durationUnit="";
                                item1.frequencyDesc="";
                                item1.prescribedDose="";
                        }
                    	if(item1.isNarcotic==true)
                    		{
                    		item1.colorCode="#f20a02"
                    		}
                    	if(self.model.worklistDetails.statusId==4 || self.model.worklistDetails.statusId==8 )
                		{
                    		if(item1.statusId!=9){
                            	item1.statusId=4;
                                item1.status="Completed";
                            	}
                		}                    	

                    	if(self.model.worklistDetails.statusId != 18 && self.model.worklistDetails.statusId != 4){
                    		$scope.batchCount = item1.batch.length;
    	                    var count = 0;
    	                    angular.forEach(item1.batch, function(s) {
    	                            
    	                            if(s.availableStockQuantity == 0){
    	                                    count++;
    	                            }
    	                    });
    	                    if($scope.batchCount == count){
    	                            item1.rate = 0;
    	                            item1.mrp = 0;
    	                            item1.batch = [];
    	                    }
                    	}
                })
                   if(self.model.worklistDetails.statusId == 1){
                	   var itemListSave = [];
                       angular.forEach(self.model.worklistDetails.items, function (item1, subindex) {
                       	if(item1.qtyPrescribed!=0)
                       	{
                       		 itemListSave.push(item1);
                       	}
                       });
                       self.model.worklistDetails.items = itemListSave;
                   }
                   
                    $scope.atCop = self.model.worklistDetails.cop;
                    $scope.billNumber = self.model.worklistDetails.billNumber;
                    if(self.model.worklistDetails.items.length > 1){
                        $scope.flagRemove = true;
                    }
                    else{
                        $scope.flagRemove = false;
                    }
                    if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                        $scope.isValidated = true;
                    }  
                    self.model.encounterId = response.data.encounterId;
                    self.model.doctorId = response.data.doctorId;
                    self.model.specialityId = response.data.specialityId;
                    $rootScope.allergydata(response.data);
                    $scope.idpatient = response.data.patientId;
                    getAllPatientDiagnosisForIcon(response.data.patientId);
                    self.model.worklistDetails.discountCatgeory = self.model.discountCategoryList[0];
                    self.model.discountCategoryList = self.model.discountCategoryList.concat(response.data.discountCategory);

                    self.model.worklistDetails['discountType'] = '1';
                    $scope.getSponsorDetails($scope.encounterId);
                    if (self.model.worklistDetails.items) {
                        $scope.orderQtyTotalSum=0;

                        self.model.worklistDetails.items.forEach(function (item,index) {
                            if(item.startDate && item.stopDate){
                                var startDate = moment(item.startDate).format('YYYY-MM-DD');
                                var stopDate = moment(item.stopDate).format('YYYY-MM-DD');
                            }
                        	if (item.status == 'Completed' ||  item.status == 'Partially Completed' || item.statusId == 9) {
                                item.itemDisabled = true;
                                item.disabled = true;
                            }else if (item.status == 'Pending' || item.status == 'New'){
                            	   item.itemDisabled = false;
                                   item.disabled = false;
                                   var priQty = 0;
                                   if(item.selectedItems !=undefined){
	                                   item.selectedItems.forEach(function (item2) {
	                                   priQty += GetCalculateOrederQty(item2);
                                   });
                                   }
                                   if(item.qtyPrescribed < priQty){
                                       item.qtyPrescribed = 0;
                                   }
                                   else{
                                       item.qtyPrescribed = item.qtyPrescribed - priQty;
                                   }
                                   if(item.qtyPrescribed == 0){
                         	 			item.itemDisabled = true;
                         	            item.disabled = true;
                         	 		}
                                   $scope.saveFlag = false; 
                            }else{
                            	  $scope.saveFlag = false; 
                            }
                            
                            //Ganesh
                            item.isConsumed=false;
                            item.searchAdddedItem=true;
                            item.productCategoryId=(item.productCatId!=null && item.productCatId >0)?item.productCatId :item.genProdCatId;
                            item.saleDetailPayeeMapperDtoList=[];
                            item.finalOrderedQty = item.qtyPrescribed;
                            item.newdispQty = item.quantityDispensedTemp;
                            if(item.totalAmount != undefined){
                        		item.itemtotalAmount = item.totalAmount;
                        		item.itemDiscount = item.discountAmount;
                        		item.discountPerItem = (100*(item.discountAmount/item.totalAmount));
                        	}else{
                        		 item.itemtotalAmount = 0;
                            	 item.itemDiscount = 0;
                            	 item.taxAmount= 0;
                            	 item.netAmount= 0;
                            	 item.roundOff=0;
                        	}
	                      
                            if($scope.autoBatch == true && !item.disabled && !item.itemDisabled){
                                item.selectedBatch = item.batch[0];
                                item.selectedBatch = item.batch.find(function(x) {
                                    return x.isSelected
                                });
                            }
                            
                            if(item.disabled != true && item.statusId != 4 && item.statusId != 9){
                        		$scope.orderListCount++;
                        	}

                            if(item.disabled == true && item.itemDisabled == true){
                                item.selectedBatch = item.batch[0];
                                item.selectedBatch = item.batch.find(function(x) {
                                    return x.isSelected
                                });
                            }
                            if($scope.autoBatch==true && item.selectedBatch==undefined){
                            	item.batchSelected = item.batch;
                            	item.batch = [];
                            	angular.forEach(item.batchSelected, function(b){
                            		if(b.availableStockQuantity != 0){
                            			item.batch.push(b);
                            		}
                            	})
                            	item.selectedBatch = commonDetailService.getDefaultBatchFromConfig(item.batch);
                            }
                            if(item.selectedBatch) {
                            	
                            	 item.selectedUom = $filter('filter')(item.selectedBatch.uom, {
                                     opUom: true
                                 }, true)[0];
                            	 
                            	if(item.selectedBatch!=null && item.selectedBatch!=undefined && item.selectedBatch.selectedUomTypeId!=undefined){
                                item.selectedUom = item.selectedBatch.uom.find(function (obj) {
                                    return obj.uomTypeId == item.selectedBatch.selectedUomTypeId;
                                  });
                            	}
                            	
                            	if(item.selectedUom!=undefined)
                                {
                                    item.quantity = commonDetailService.getDefaultQuantity(item,item.qtyPrescribed,item.selectedBatch.availableStockQuantity);
                                    item.uom = item.selectedBatch.uom;
                                    if(item.isThreeUOMApplicable == false){
                                        item.selectedBatch.uomListData = item.selectedBatch.uom;
                                        item.selectedBatch.uom = [];
                                        angular.forEach(item.selectedBatch.uomListData, function(u){
                                            if(u.uomTypeId != 2){
                                                item.selectedBatch.uom.push(u);
                                            }
                                        });
                                        self.model.ConvertQtyOrdered(item, item.selectedBatch.uomListData, item.selectedUom);
                                    }
                                    else{
                                        self.model.ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);
                                    }
                                }
                            	var sellingPrice = !!item.selectedBatch ? commonDetailService.fixedDecimal(item.selectedBatch.sellingPrice) : 0;
                            	if(item.selectedUom!=undefined && item.selectedUom!=null )
                            		{
                            		item.mrp = commonDetailService.calculateRate(item.selectedBatch.uom, item.selectedUom.uomTypeId, sellingPrice); 
                            		}
                                var dataForRate = {
                                        "mrp": item.mrp,
                                        "isTaxInc": $scope.taxInclusive,
                                         "markup": item.markUpDown,
                                        "markUpDownPer" : item.markUpDownPer,
                                        "discount": self.model.worklistDetails.discount ? self.model.worklistDetails.discount : 0,
                                        "isDiscPercent": !!self.model.worklistDetails.discountType && self.model.worklistDetails.discountType == 1 ? true : false,
                                        "tax": item.taxPercentage,
                                        "qty": (item.quantity!=undefined && item.quantity!=null && item.quantity != 0) ? item.quantity : item.issuedQty,
                                        "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId
                                    }
                        
                                    var responseForRate = commonDetailService.calculateAmount(dataForRate);
                                  item.rate = responseForRate.rate;
                                  item.packageConsumptionAmt= item.packageConsumptionAmt;
                                item.availableStockQuantity = item.selectedBatch.availableStockQuantity;
                                if(item.isThreeUOMApplicable == false){
                                    ConvertQtyOrdered(item,item.selectedBatch.uomListData, item.selectedUom);
                                }
                                else{
                                    ConvertQtyOrdered(item,item.selectedBatch.uom, item.selectedUom);
                                }
                            }
                            if(item.patientClassAmount==0 && item.itemDisabled && item.selectedBatch !=null){
                            	var sellingPrice = !!item.selectedBatch ? commonDetailService.fixedDecimal(item.selectedBatch.sellingPrice) : 0;
                            	item.mrp = commonDetailService.calculateRate(item.selectedBatch.uom, item.selectedUom.uomTypeId, sellingPrice);
									var data = {
										"mrp": item.mrp,
										"isTaxInc": $scope.taxInclusive,
                                         "markup": item.markUpDown,
                                         "markUpDownPer" : item.markUpDownPer,
										"discount": item.discountPerItem,
										"isDiscPercent": !!item.discountType && item.discountType == 1 ? true : false,
										"tax": item.taxPercentage,
										"qty": (item.quantity!=undefined && item.quantity!=null && item.quantity != 0) ? item.quantity : item.issuedQty,
										"paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
										"patientClassPercentage": item.patientClassPercentage,
									}

									var response = commonDetailService.calculateAmount(data);
									item.rate = response.rate;
									if(((item.batchDetails != undefined && item.batchDetails != null && item.batchDetails != "") || item.addCopyItemFlag == true || item.NonFormularyAddedFlag == true || item.addBatchTopFlag == true || item.addItemTopFlag == true || item.statusId == 1 || item.isSelected == true || self.model.worklistDetails.statusId == 1) 
										 && (item.disabled != true && item.itemDisabled != true && item.statusId != 18 && item.statusId != 4)){
										item.taxAmount = response.taxAmount;
										item.responseTaxAmount =  response.taxAmount;
										item.unitDiscountAmount = response.unitDiscountAmount;
										item.itemtotalAmount = response.totalAmount
										item.totalAmount = response.totalAmount;
										item.netAmount = response.netAmount;
										item.packageConsumptionAmt = response.packageConsumptionAmt==0 ?  item.packageConsumptionAmt : response.packageConsumptionAmt;
										item.patientClassAmount = response.patientClassAmount;
										item.unitPatientClassAmount = response.unitPatientClassAmount;
									}
                        }
		                    angular.forEach(item.selectedItems, function (item1, subindex) {
		                    	
		                           	 item1.itemDisabled = true;
		                           	 item1.disabled = true;
		                           	 item1.quantity=0;
		                           	 item1.finalOrderedQty = item1.qtyPrescribed;
		                             item1.frequencyDesc = item.frequencyDesc;
		                             item1.instructionDesc = item.instructionDesc;
		                             item1.colorCode=item.colorCode;
		                           	 if(item1.totalAmount != undefined){
		                           		item1.itemtotalAmount = item1.totalAmount;
		                         		item1.itemDiscount = item1.discountAmount;
		                         		item1.discountPerItem = (100*(item1.discountAmount/item1.totalAmount));
		                         	}else{
		                         		item1.itemtotalAmount = 0;
		                         		item1.itemDiscount = 0;
		                         		item1.taxAmount= 0;
		                         		item1.netAmount= 0;
		                         		item1.roundOff=0;
		                         	}
		                           	 var isBatchFount = item1.batch.find(function (x) {
		                                    return !!x.isSelected;
		                                });
		
		                          	  	var clubData  = clubBatches(item1.batch);
		                          	  	item1.batch = clubData;
		                                if (!!isBatchFount) {
		                              	  item1.selectedBatch = item1.batch.find(function (x) {
		                                        return !!x.isSelected;
		                                    });
		                              	  
		                              	item1.taxId=item1.selectedBatch.taxId;
                                    	item1.taxPercentage=item1.selectedBatch.taxPercentage; 

		                              	item1.availableStockQuantity = item1.selectedBatch.availableStockQuantity;
		                              	item1.selectedUom = item1.selectedBatch.uom.find(function (obj) {
		                                        //  return obj.opUom;
		                                       return obj.uomTypeId === item1.selectedBatch.selectedUomTypeId
		                                       
		                                    });
		                              	  item1.uom = item1.selectedBatch.uom;
		                              	  ConvertQtyOrdered(item1, item1.uom, item1.selectedUom); 
		                                } else {
		                              	  item1.uom = [{
		                                        "uomDetails": "Select UOM"
		                                    }]
		
		                              	if($scope.autoBatch==true && item1.selectedBatch==undefined){
		                                    item1.selectedBatch = commonDetailService.getDefaultBatchFromConfig(item1.batch);
		                                    item1.taxId=item1.selectedBatch.taxId;
	                                    	item1.taxPercentage=item1.selectedBatch.taxPercentage; 

		                              	  }
		                              	  
		                              	     item1.selectedUom = item1.uom[0];
			                            	 var found =  item1.batch.find(function (x) {
			                                     return x.isSelected;
			                                 });
		                                      	
			                            	 if(self.model.worklistDetails.statusId ==undefined && self.model.worklistDetails.statusId ==null && self.model.worklistDetails.statusId ==2){
		                                    	  ConvertQtyOrdered(item1, item1.uom, item1.selectedUom);
		                                      }else{
		                                      	
		                                     	 var found =  item1.batch.find(function (x) {
		                                              return x.isSelected;
		                                          });
		                                        	
		                                        var	parentItemQty = GetCalculateOrederQty(item);
		                                     	
		                                       	if(found == undefined){
		                                       		uomConversion(item1, item1.batch[0],3,parentItemQty);
		                                       	}else{
		                                       		var selectedUomTypeId =  found.uomTypeId;
		                                       		uomConversion(item1, found,selectedUomTypeId,parentItemQty);
		                                       	}	
		                                     	
		                                     } 
			                            	 
		                                }
		                                if(item1.patientClassAmount==0  && item1.itemDisabled && item1.packageConsumptionAmt == 0){
		                                	var sellingPrice = !!item1.selectedBatch ? commonDetailService.fixedDecimal(item1.selectedBatch.sellingPrice) : 0;
		                                	item1.mrp = commonDetailService.calculateRate(item1.selectedBatch.uom, item1.selectedUom.uomTypeId, sellingPrice);
		                                	var data = {
													"mrp": item1.mrp,
													"isTaxInc": $scope.taxInclusive,
                                                     "markup": item.markUpDown,
                                                    "markUpDownPer" : item.markUpDownPer,
													"discount": item1.discountPerItem,
													"isDiscPercent": (!!item1.discountType && item1.discountType == 1) ? true : false,
													"tax": item1.taxPercentage,
													"qty": item1.newDispQty,
													"paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
													"patientClassPercentage": item1.patientClassPercentage,
												}

		                                	
												var response = commonDetailService.calculateAmount(data);
		                                	if(item1.disabled != true){
		                                		item1.rate = response.rate;
		                                		item1.taxAmount = response.taxAmount;
		                                		item1.itemtotalAmount = response.totalAmount;
		                                		item1.netAmount = response.netAmount;
		                                	}
		                                	else{
		                                		item1.rate = item1.rate;
		                                	}
		                                	
		                                	item1.responseTaxAmount =  response.taxAmount;
		                                	item1.unitDiscountAmount = response.unitDiscountAmount;
		                                	item1.totalAmount = response.totalAmount;
		                                	item1.packageConsumptionAmt = response.packageConsumptionAmt;
		                                	item1.patientClassAmount = response.patientClassAmount;
		                                	item1.unitPatientClassAmount = response.unitPatientClassAmount;
												
												
		                                }
		                                
		                       });
                            var uomlist = item.selectedBatch ? item.selectedBatch.uom ? item.selectedBatch.uom : [] : [];
                            if(item.selectedBatch!=undefined){
                             item.quantity = commonDetailService.getDefaultQuantity(item,item.qtyPrescribed,item.selectedBatch.availableStockQuantity);
                             if(item.quantity!=null && item.quantity!=undefined && item.quantity!=0)
                            	 {
                            	//  AllocateQuantity(index, self.model.worklistDetails, null);
                            	 Calculate(item, self.model.worklistDetails);
                            	 if(item.batchDetails != undefined && item.batchDetails != null && item.batchDetails != "" && self.model.worklistDetails.statusId != 4 && item.disabled!=true && item.itemDisabled!=true){
                                	 CalculateAmount(item);
                                 }
                            	// ValidateQuantity(item); 
                            	 }
                            }
                            else{
                            	item.quantity = 0;
                            }
                            BatchChange(index,self.model.worklistDetails, item.selectedBatch,null);
                        });
                        
                        var tempList = [];
                        if(self.model.worklistDetails.statusId ==1){
                        	var canceledItemCount=0
                        	self.model.worklistDetails.items.forEach(function (item) {
                        		if(item.statusId==9)
                        			{
                        			canceledItemCount++;
                        			}
                        	})
                        	
                        	self.model.worklistDetails.items.forEach(function (item) {
                        		if(item.statusId==1)
                        			{
	                        			var check = false;
	                        			self.model.worklistDetails.items.forEach(function (item1) {
	                                		if(item.pricId == item1.pricId &&  item1.statusId==9)
	                                			{
	                                				check = true;
	                                			}
	                                	});
                        			
	                        			if(check == true){
	                        				item.quantity = null;
	                        			}
                        			}
                        	});
                        	if(canceledItemCount>0){
                     	   self.model.worklistDetails.items.forEach(function (item) {
                     		   if(item.status != 'New'){
		                        		   var flag = false;
		                        		   if(item.statusId == 9){
		                        			   flag = false;
		                        		   }else{
		                        			   
		                        			   if(tempList.length >0){
		                            			   tempList.forEach(function (tempItem) {
		                            					if(item.itemId == tempItem.itemId ){
		                            						flag = true;
		                            					}
		                            			   })
		                            		   }
		                        		   }
		                               	var totalQty = 0;
		                               	if(flag == false){
		                               		
		    		                           	  self.model.worklistDetails.items.forEach(function (item1) {
		    		                                	if(item.itemId == item1.itemId && item1.statusId!=9){
		    		                                		totalQty += GetCalculateOrederQty(item1);
		    		                                		
		    		                                		if(item1.selectedItems !=undefined){
		        		                                		item1.selectedItems.forEach(function (item2) {
		        		                                			if(item2.statusId!=9)
		        		                                				{
		        		                                				totalQty += GetCalculateOrederQty(item2);
		        		                                				}
		        		                                	  });
		        		                                	}
		    		                                	}
		    		                                	
		    		                             });
		    		                  		   
		    		                  		   if(item.qtyPrescribed > totalQty &&  item.statusId != 9){
		    		                  			   var newOrderQty = item.qtyPrescribed - totalQty;
		    		                  			   var newArr = {};
		    		                  			   
		    		                  			   
		    		                  			newArr.issueDetailId = null,
		    		                  			newArr.patientClassPercentage = item.patientClassPercentage,
		    		                  			newArr.itemCode = item.itemCode,
		    		                  			newArr.formulationTypeId= item.formulationTypeId,
		    		                  			newArr.isNarcotic= item.isNarcotic,
		    		                  			newArr.itemName= item.itemName,
		    		                  			newArr.itemId = item.itemId,
		    		                  			newArr.storeId = item.storeId,
		    		                  			newArr.genericName = item.genericName,
		    		                  			newArr.genericId = item.genericId,
		    		                  			newArr.manufacturerName = item.manufacturerName,
		    		                  			newArr.productCatId = item.productCatId,
		    		                  			newArr.productCategoryId = item.productCategoryId,
		    		                  			newArr.qtyPrescribed = newOrderQty,
		    		                  			newArr.availableStockQuantity = 0,
		    		                  			newArr.markUpDown = item.markUpDown,
		    		                  			newArr.markUpDownPer = item.markUpDownPer,
		    		                  			newArr.markupStatus = item.markupStatus,
		    		                  			newArr.disabled=false,
		    		                  			newArr.itemDisabled=false,
		    		                  			newArr.batch = item.batch,
		    		                  			newArr.selectedBatch = commonDetailService.getDefaultBatchFromConfig(item.batch),
		    		                  			newArr.uom =  item.uom,
		    		                  			newArr.selectedUom = null,
		    		                  			newArr.storageUnitName = item.storageUnitName,
		                                        newArr.isNarcotic= item.isNarcotic,
		    		                  			newArr.taxId = item.taxId,
		    		                  			newArr.tax = item.tax !=undefined ? item.tax: 0.0,
		    		                  			newArr.taxPercentage = item.taxPercentage!=undefined ? item.taxPercentage :0.0,
		    		                  			newArr.substituteBtn =  false,
		    		                  			newArr.quantitydispensed = 0,
		    		                  			newArr.prescriptionDetailId = item.pricId,
		    		                  			newArr.isShow = true,
		    		                  			newArr.otc	= item.otc,
		    		                  			newArr.interventionFlag  = item.interventionFlag,
		    		                  			newArr.quantitydispensed = item.quantitydispensed!=undefined? item.quantitydispensed :0,
		    		                  			newArr.finalOrderedQty = newOrderQty;
		    		                  			newArr.instructionDesc = item.instructionDesc;
		    		                  			newArr.duration = item.duration;
                                                newArr.durationUnit = item.durationUnit;
                                                newArr.prescribedDose = item.prescribedDose;
                                                newArr.units = item.units;                                               newArr.frequencyDesc = item.frequencyDesc;
                                               newArr.isThreeUOMApplicable = item.isThreeUOMApplicable;
                                               newArr.taxAmount = item.taxAmount;
                                               newArr.batchdata = item.batch;
                                               newArr.colorCode=item.colorCode;
                                               newArr.batch = [];
                                               angular.forEach(newArr.batchdata, function(d){
                                                   if(d.availableStockQuantity != 0){
                                                       newArr.batch.push(d);
                                                   }
                                               })
                                               if(newArr.selectedBatch) {
                                             	  newArr.selectedUom = $filter('filter')(item.selectedBatch.uom, {
			    		                                     opUom: true
			    		                                 }, true)[0];
			    		                  		  }
                                               var PrisExistsOrNotFlag=$scope.checkPrisExistsOrNot(tempList,newArr);
                                               if(!PrisExistsOrNotFlag)
                                             	  {
                                             	  tempList.push(newArr);
                                             	  }
		    		                  		   }
		                               	}
                     		   }
                     		   
                     		  item.batchdata = item.batch;
                     		  item.batch=[];
                     		  angular.forEach(item.batchdata, function(d){
                                  if(d.availableStockQuantity != 0){
                                	  item.batch.push(d);
                                  }
                              });
                     		   
                            });
                        }
                     	   if(tempList.length >0){
                     		   $scope.saveFlag = false;
                 			   tempList.forEach(function (tempItem,subIdx) {
                 				   
                 				   
                 				   self.model.worklistDetails.items.push(tempItem);
                 				   if(tempItem.selectedBatch) {
                     				   BatchChange(self.model.worklistDetails.items.length-1,self.model.worklistDetails, tempItem.selectedBatch,null);
                     				   }
                 			   })
                 		   }
                     
                     	   
                            }
                        if(self.model.worklistDetails.statusId == 18){
                        	
                        	   self.model.worklistDetails.items.forEach(function (item) {
                        		   if(item.status != 'New'){
		                        		   var flag = false;
		                        		   if(item.statusId == 9){
		                        			   flag = false;
		                        		   }else{
		                        			   
		                        			   if(tempList.length >0){
		                            			   tempList.forEach(function (tempItem) {
		                            					if(item.itemId == tempItem.itemId ){
		                            						flag = true;
		                            					}
		                            			   })
		                            		   }
		                        		   }
		                               	var totalQty = 0;
		                               	if(flag == false){
		                               		
		    		                           	  self.model.worklistDetails.items.forEach(function (item1) {
		    		                                	if(item.itemId == item1.itemId && item1.statusId!=9){
		    		                                		totalQty += GetCalculateOrederQty(item1);
		    		                                		
		    		                                		if(item1.selectedItems !=undefined){
		        		                                		item1.selectedItems.forEach(function (item2) {
		        		                                			if(item2.statusId!=9)
		        		                                				{
		        		                                				totalQty += GetCalculateOrederQty(item2);
		        		                                				}
		        		                                	  });
		        		                                	}
		    		                                	}
		    		                                	
		    		                             });
		    		                  		   
		    		                  		   if(item.qtyPrescribed > totalQty &&  item.statusId != 9){
		    		                  			   var newOrderQty = item.qtyPrescribed - totalQty;
		    		                  			   var newArr = {};
		    		                  			newArr.issueDetailId = null;
		    		                  			newArr.patientClassPercentage = item.patientClassPercentage;
		    		                  			newArr.itemCode = item.itemCode;
		    		                  			newArr.formulationTypeId= item.formulationTypeId;
		    		                  			newArr.isNarcotic= item.isNarcotic;
		    		                  			newArr.itemName= item.itemName;
		    		                  			newArr.itemId = item.itemId;
		    		                  			newArr.storeId = item.storeId;
		    		                  			newArr.genericName = item.genericName;
		    		                  			newArr.genericId = item.genericId;
		    		                  			newArr.manufacturerName = item.manufacturerName;
		    		                  			newArr.productCatId = item.productCatId;
		    		                  			newArr.productCategoryId = item.productCategoryId;
		    		                  			newArr.qtyPrescribed = newOrderQty;
		    		                  			newArr.availableStockQuantity = 0;
		    		                  			newArr.markUpDown = item.markUpDown;
		    		                  			newArr.markUpDownPer = item.markUpDownPer;
		    		                  			newArr.markupStatus = item.markupStatus;
		    		                  			newArr.disabled=false;
		    		                  			newArr.itemDisabled=false;
		    		                  			newArr.uom =  item.uom;
		    		                  			newArr.selectedUom = null;
		    		                  			newArr.storageUnitName = item.storageUnitName;
                                                newArr.isNarcotic= item.isNarcotic;
		    		                  			newArr.taxId = item.taxId;
		    		                  			newArr.tax = item.tax !=undefined ? item.tax: 0.0;
		    		                  			newArr.taxPercentage = item.taxPercentage!=undefined ? item.taxPercentage :0.0;
		    		                  			newArr.substituteBtn =  false;
		    		                  			newArr.quantitydispensed = 0;
		    		                  			newArr.prescriptionDetailId = item.pricId;
		    		                  			newArr.isShow = true;
		    		                  			newArr.NonFormularyAddedFlag = true;
		    		                  			newArr.otc	= item.otc;
		    		                  			newArr.interventionFlag  = item.interventionFlag;
		    		                  			newArr.quantitydispensed = item.quantitydispensed!=undefined? item.quantitydispensed :0;
		    		                  			newArr.finalOrderedQty = newOrderQty;
		    		                  			newArr.instructionDesc = item.instructionDesc;
                                                newArr.duration = item.duration;
                                                newArr.durationUnit = item.durationUnit;
                                                newArr.prescribedDose = item.prescribedDose;
                                                newArr.units = item.units;
                                                newArr.frequencyDesc = item.frequencyDesc;
                                                newArr.isThreeUOMApplicable = item.isThreeUOMApplicable;
                                                newArr.taxAmount = item.taxAmount;
                                                newArr.batchdata = item.batch;
                                                newArr.colorCode=item.colorCode;
                                                newArr.batch = [];
                                                angular.forEach(newArr.batchdata, function(d){
                                                    if(d.availableStockQuantity != 0){
                                                        newArr.batch.push(d);
                                                    }
                                                })
                                                if($scope.autoBatch == true){
                                                    newArr.selectedBatch = commonDetailService.getDefaultBatchFromConfig(newArr.batch);
                                                }
                                                if(newArr.selectedBatch) {
                                                    newArr.selectedUom = $filter('filter')(item.selectedBatch.uom, {
                                                        opUom: true
                                                    }, true)[0];
                                                }
                                                var PrisExistsOrNotFlag=$scope.checkPrisExistsOrNot(tempList,newArr);
                                                if(!PrisExistsOrNotFlag)
                                                {
                                                    tempList.push(newArr);
                                                }
		    		                  			   
                                            }
		                               	}
                        		   }
                        		   
                        		   
                        		   
                        		   
                               });
                        	   if(tempList.length >0){
                        		   $scope.saveFlag = false;
                    			   tempList.forEach(function (tempItem,subIdx) {
                    				   if(tempItem.selectedBatch == undefined || tempItem.selectedBatch == null || tempItem.selectedBatch == ""){
                    					  tempItem.taxAmount = 0; 
                    				   }
                    				   self.model.worklistDetails.items.push(tempItem);
                    				   if(tempItem.selectedBatch) {
                        				   BatchChange(self.model.worklistDetails.items.length-1,self.model.worklistDetails, tempItem.selectedBatch,null);
                        				   }
                    			   })
                    		   }
                               }if(self.model.worklistDetails.statusId == 2){
                                  	$scope.saveFlag = false; 
                            
                               }else if(self.model.worklistDetails.statusId == 9){
                               	$scope.saveFlag = true; 
                               }
                    }
                    angular.forEach(self.model.worklistDetails.items, function(val1){
                        if(!val1.itemDisabled && !val1.disabled){
                            $scope.orderQtyTotalSum = $scope.orderQtyTotalSum + val1.finalOrderedQty;    
                        }
                    });
                    calculateAtcop();

                    angular.forEach(self.model.worklistDetails.items, function(item1){
                        if(!item1.itemDisabled && !item1.disabled){
                            if(self.model.worklistDetails.statusId!=4 && self.model.worklistDetails.statusId!=8)
                            {
                                const result = item1.batch.filter(word => word.availableStockQuantity > 0);
                                item1.batch=result;
                                angular.forEach(item1.selectedItems, function(select){
                                    if(!select.itemDisabled && !select.disabled){
                                        const result1 = select.batch.filter(word => word.availableStockQuantity > 0);
                                        select.batch=result1;
                                    }
                                })
                            }
                            else
                            {
                                const result = item1.batch.filter(word => word.availableStockQuantity >= 0);
                                item1.batch=result;
                                angular.forEach(item1.selectedItems, function(select){
                                    if(!select.itemDisabled && !select.disabled){
                                        const result1 = select.batch.filter(word => word.availableStockQuantity >= 0);
                                        select.batch=result1;
                                    }
                                })
                            }
                        }
                        if($scope.autoBatch == true && !item1.disabled && !item1.itemDisabled){
                            item1.selectedBatch = item1.batch.find(function (x) {
                                return !!x.isSelected;
                            });
                        }
                        if(item1.disabled == true && item1.itemDisabled == true){
                            item1.selectedBatch = item1.batch.find(function (x) {
                                return !!x.isSelected;
                            });
                            
                        }
                        if($scope.autoBatch==true && item1.selectedBatch==undefined){
                            item1.selectedBatch = commonDetailService.getDefaultBatchFromConfig(item1.batch);
                        }
                        if(item1.isThreeUOMApplicable == false){
                            if(item1.selectedBatch != undefined && item1.selectedBatch != null){
                                if(item1.selectedBatch.uomListData == undefined){
                                    ConvertQtyOrdered(item1,item1.selectedBatch.uom, item1.selectedUom);
                                }
                                else{
                                    ConvertQtyOrdered(item1,item1.selectedBatch.uomListData, item1.selectedUom);
                                }
                            }
                        }
                        else{
                        	if(item1.selectedItems != undefined && item1.selectedItems.length>0){
                                var disperite =  parseFloat(item1.selectedItems[0].discountPerItem).toFixed(2);
                                    item1.selectedItems[0].discountPerItem = disperite;
                            }
                        	if(item1.selectedBatch!= undefined && item1.selectedBatch.uom != undefined){
                        		 ConvertQtyOrdered(item1,item1.selectedBatch.uom, item1.selectedUom);
                        	}
                           
                            
                        }
                        if(item1.isThreeUOMApplicable == false){
                            if(item1.selectedBatch != undefined && item1.selectedBatch != null){
                                item1.selectedBatch.uomListData = item1.selectedBatch.uom;
                                item1.selectedBatch.uom = [];
                                angular.forEach(item1.selectedBatch.uomListData, function(u){
                                    if(u.uomTypeId != 2){
                                        item1.selectedBatch.uom.push(u);
                                    }
                                });
                            } 
                        }
                        if(item1.quantity >= item1.newQty)
                		{
                        	item1.quantityIssuedFlag=true;
                		}
                        else
                		{
                	    	item1.quantityIssuedFlag = false;
                		}
//                        ValidateQuantity(item1); 
                    });
                    $rootScope.stopLoader();
                    $scope.showNonFormularyItemList();
                    
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }
        $scope.checkPrisExistsOrNot=function (arr, val) {
            return arr.some(function(arr) {
                        if(val.prescriptionDetailId === parseInt(arr.prescriptionDetailId))
                            {
                                return true;
                            }
                        else
                            {
                                return false;
                            }
                    });
        }
        $scope.getGenericScheduleColorMapping = function ()
        {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId + '/genericColorMapper';
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(function (response) {
              $scope.genericColorMapperList=response.data;
            }, function (err) {
            growl.error(err.data.message);
            $rootScope.stopLoader();
          });
        }
        $scope.getGenericScheduleColorMapping();
        $scope.genericColorMapperPopUp = function (item) {
            $('#genericColorMapper').modal('show');
          }
        var BatchChange = function (index, bill, batch, parentIndex) {
            var item = null;

            //Assign Item and UOM
            if (parentIndex == null) {
                if(bill.items[index].isThreeUOMApplicable == false){
                    if(self.model.worklistDetails.statusId != 18){
                        if(bill.items[index].uomListData == undefined || bill.items[index].uomListData == null){
                            if(batch.uom[2] == undefined || batch.uom[2] == null){
                                bill.items[index].uomListData = batch.uomListData;
                            }
                            else{
                                bill.items[index].uomListData = batch.uom;
                            }
                        }
                        if(bill.items[index].selectedBatch.uomListData == undefined || bill.items[index].selectedBatch.uomListData == null){
                            bill.items[index].selectedBatch.uomListData = batch.uom;
                        }
                    }
                    
                    // bill.items[index].uom = [];
                    if(bill.items[index].onlySubButtonDisable == true){
                        if(bill.items[index].uom == undefined || bill.items[index].uom == null || bill.items[index].uom[0].conversion == undefined || bill.items[index].uom[0].conversion == null){
                            bill.items[index].uom = bill.items[index].selectedBatch.uom;
                        }
                    }
                    bill.items[index].selectedBatch.uom = [];
                    if(batch.uomListData == undefined){
                        if(bill.items[index].uomListData == undefined){
                            bill.items[index].uomListData = bill.items[index].uom;
                        }
                        angular.forEach(bill.items[index].uom, function(u){
                            if(u.uomTypeId != 2){
                                bill.items[index].selectedBatch.uom.push(u);
                                // bill.items[index].uom.push(u);
                            }
                        })
                    }
                    else{
                        angular.forEach(batch.uomListData, function(u){
                            if(u.uomTypeId != 2){
                                bill.items[index].selectedBatch.uom.push(u);
                                // bill.items[index].uom.push(u);
                            }
                        })
                    }
                    item = bill.items[index];
                }
                else{
                    bill.items[index].uom = batch!=undefined && batch!=null ? batch.uom :[];
                    item = bill.items[index];
                }
            } else {
                if(bill.items[parentIndex].selectedItems[index].isThreeUOMApplicable == false){
                    bill.items[parentIndex].selectedItems[index].uomListData = batch.uom;
                    bill.items[parentIndex].selectedItems[index].uom = [];
                    angular.forEach(batch.uom, function(u){
                        if(u.uomTypeId != 2){
                            bill.items[parentIndex].selectedItems[index].uom.push(u);
                        }
                    })
                    item = bill.items[parentIndex].selectedItems[index];
                }
                else{
                    bill.items[parentIndex].selectedItems[index].uom = batch.uom;
                    item = bill.items[parentIndex].selectedItems[index];
                    if(item.selectedBatch.rate==undefined)
                    {
                        item.selectedBatch.rate=item.selectedBatch.sellingPrice;
                    }
                }
            }

            if (batch!=undefined && batch!=null && !!batch.batchId) {
                //Select Default UOM
                if(item.isThreeUOMApplicable == false){
                    if(item.uomListData == undefined || item.uomListData == null || item.uomListData == ""){
                        item.selectedUom = $filter('filter')(item.uom, {
                            opUom: true
                        }, true)[0];
                    }
                    else{
                        item.selectedUom = $filter('filter')(item.uomListData, {
                            opUom: true
                        }, true)[0];
                    }
                }
                else{
                    item.selectedUom = $filter('filter')(item.uom, {
                        opUom: true
                    }, true)[0];
                }
                if(item.selectedBatch!=undefined && item.selectedBatch!=null &&  item.selectedBatch.selectedUomTypeId!=undefined &&  item.selectedBatch.selectedUomTypeId!=null){
                    item.selectedUom = item.selectedBatch.uom.find(function (obj) {
                        return obj.uomTypeId == item.selectedBatch.selectedUomTypeId;
                      });
                	}
                //"Select UOM" option from UOM dropdown
                if (!item.selectedUom) {
                    item.selectedUom = item.uom[0];
                }
                
              
                
                //Caclulate amount including discount
                /*if(bill.items[index].quantity != 0){
                if(item.quantity !=undefined && item.quantity != 0 ){
                    Calculate(item, bill);
                }*/

               // CalculateAmount(item);

                if(item.selectedBatch.availableStockQuantity != undefined && item.selectedBatch.availableStockQuantity != null){
                    //Convert Ordered Quantity
                    if(item.isThreeUOMApplicable == false){
                        if(item.selectedBatch.uomListData == undefined){
                            ConvertQtyOrdered(item, item.selectedBatch.uomListData, item.selectedUom);
                        }
                        else{
                            ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);
                        }
                    }
                    else{
                        ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);
                    }
                    $scope.selectedUomData = item.selectedUom;
                    //Allocate quantity
                    if(item.quantity !=undefined && item.quantity != 0 ){
                	    // AllocateQuantity(index, bill, parentIndex);
                    	
                        Calculate(item, bill);
                    }
                    //AllocateQuantity(index, bill, parentIndex);               

                    //Get current stock for the selected batch
                    if(item.selectedUom != undefined && item.selectedUom != null){
                    var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.AVAILABLE_QTY_API  + $scope.unitId + "/"+ $scope.storeId + "/" + item.itemId + "/" + item.selectedBatch.batchId + "/" + item.selectedUom.uomTypeId;
                    $rootScope.startLoader();
                    GenericService.serviceAction("GET", URI, {}).then(
                    function (response) {
                        item.stock= (response.data.availableqty != undefined && response.data.availableqty != null && response.data.availableqty != '') ? response.data.availableqty : item.selectedBatch.availableStockQuantity;
                        //Ganesh
                        item.taxId=item.selectedBatch.taxId;
                        item.taxPercentage=item.selectedBatch.taxPercentage;
                        if(item.disabled != true){
                        	if(response.data.availableqty != undefined && response.data.availableqty != null && response.data.availableqty != ''){
                                item.quantity = commonDetailService.getDefaultQuantity(item,item.qtyPrescribed,response.data.availableqty);
                            }
                            else{
                            	item.quantity = commonDetailService.getDefaultQuantity(item,item.qtyPrescribed,item.selectedBatch.availableStockQuantity);
                            }
                        	if(item.newQty == undefined || item.newQty == null){
                        		item.newQty = 0;
                        	}
                        	if($scope.autoPrescribedQty == true && item.addedItemToTop != true){
    	                        if(item.newStock > item.newQty){
    	                        	item.quantity = item.newQty;
    	    					}
    	    					else{
    	    						item.quantity = item.newStock;
    	    					}
                            }
                        }
                        else{
                        	item.quantity = 0;
                        }
                        
                        $scope.calculateReceivedQty1(item);
                        
                        //                        item.newStock = response.data.availableqty;
                        if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                            $scope.isValidated = true;
                        }
                        else{
                            $scope.isValidated = false;
                        }
                        if($scope.activePackageList!=null&& $scope.activePackageList.length>0){
                            $scope.isValidated = true;
                        }
                        if($scope.atCop == true){
                          item.rate = item.selectedBatch.actualLendingPrice;
                          Calculate(item, self.model.worklistDetails);
                        }
                        else{
                        	if(item.selectedBatch != undefined || item.selectedBatch != null){
                        		if(item.selectedBatch.rate != undefined || item.selectedBatch.rate != null){
                                    item.rate = item.selectedBatch.rate;
                                    Calculate(item, self.model.worklistDetails);
                                }
                        	}
                            else{
                            	if(item.selectedBatch == undefined || item.batch.length == 0)
                            	{
                            		item.rate = 0;
                            	}
                            	else{
                            		item.rate = item.rate;
                            	}
                            }
                        }
                        if(item.quantity >= item.newQty)
                		{
                        	item.quantityIssuedFlag=true;
                		}
                        else
                		{
                    		item.quantityIssuedFlag=false;
                		}
                        $rootScope.stopLoader();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                    });
                }
                }
                else{
                    //AllocateQuantity(index, bill, parentIndex);               

                    //Get current stock for the selected batch
                    var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.AVAILABLE_QTY_API  + $scope.unitId + "/"+ $scope.storeId + "/" + item.itemId + "/" + item.selectedBatch.batchId + "/" + item.selectedUom.uomTypeId;
                    $rootScope.startLoader();
                    GenericService.serviceAction("GET", URI, {}).then(
                        function (response) {
                            item.stock= response.data.availableqty;
                            item.selectedBatch.availableStockQuantity = response.data.availableqty;
                            //Ganesh
                            item.taxId=item.selectedBatch.taxId;
                            item.taxPercentage=item.selectedBatch.taxPercentage;
                            item.quantity = commonDetailService.getDefaultQuantity(item,item.qtyPrescribed,response.data.availableqty);
                            if($scope.autoPrescribedQty == true){
	                            if(item.newStock > item.newQty){
	                            	item.quantity = item.newQty;
	        					}
	                            else{
	        						item.quantity = item.newStock;
	        					}
                            }
                            $scope.calculateReceivedQty1(item);
                            //                        item.newStock = response.data.availableqty;
                            if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                                $scope.isValidated = true;
                            }
                            else{
                                $scope.isValidated = false;
                            }
                            if($scope.activePackageList!=null&& $scope.activePackageList.length>0){
                                $scope.isValidated = true;
                            }
                            if($scope.atCop == true){
                                item.rate = item.selectedBatch.actualLendingPrice;
                                Calculate(item, self.model.worklistDetails);
                            }
                            else{
                                if(item.selectedBatch.rate != undefined || item.selectedBatch.rate != null){
                                    item.rate = item.selectedBatch.rate;
                                    Calculate(item, self.model.worklistDetails);
                                }
                                else{
                                    item.rate = item.rate;
                                }
                            }
                            if(item.isThreeUOMApplicable == false){
                                ConvertQtyOrdered(item, item.selectedBatch.uomListData, item.selectedUom);
                            }
                            else{
                                ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);
                            }
                            $scope.selectedUomData = item.selectedUom;
                            //Allocate quantity
                            // if(item.quantity !=undefined && item.quantity != 0 ){
                            //     // AllocateQuantity(index, bill, parentIndex);
                            //     Calculate(item, bill);
                            // }
                            if(item.quantity >= item.newQty)
                    		{
                            	item.quantityIssuedFlag=true;
                    		}
                            else
                    		{
                        		item.quantityIssuedFlag=false;
                    		}
                            $rootScope.stopLoader();
                        },
                        function (err) {
                            $rootScope.stopLoader();
                        }
                    );
                }
            } else{
                //item.selectedBatch = item.batch[0];
                item.selectedBatch = commonDetailService.getDefaultBatchFromConfig(item.batch);
                item.newStock = 0;
                item.quantity = null;
                item.rate = 0;
                item.itemtotalAmount = 0;
                item.patientClassPercentage = 0;
                item.patientClassAmount = 0;
                item.netAmount = 0;
                item.selfPayable = 0;
                item.selectedUom = item.uom[0];
            }
        }
        
        var BatchChangeForNonFormulary = function (index, bill, batch, parentIndex) {
            var item = null;

            //Assign Item and UOM
            if (parentIndex == null) {
                if(bill.isThreeUOMApplicable == false){
                    if(self.model.worklistDetails.statusId != 18){
                        if(bill.uomListData == undefined || bill.uomListData == null){
                            if(batch.uom[2] == undefined || batch.uom[2] == null){
                                bill.uomListData = batch.uomListData;
                            }
                            else{
                                bill.uomListData = batch.uom;
                            }
                        }
                        if(bill.selectedBatch.uomListData == undefined || bill.selectedBatch.uomListData == null){
                            bill.selectedBatch.uomListData = batch.uom;
                        }
                    }
                    
                    // bill.items[index].uom = [];
                    if(bill.onlySubButtonDisable == true){
                        if(bill.uom == undefined || bill.uom == null || bill.uom[0].conversion == undefined || bill.uom[0].conversion == null){
                            bill.uom = bill.selectedBatch.uom;
                        }
                    }
                    bill.selectedBatch.uom = [];
                    if(batch.uomListData == undefined){
                        if(bill.uomListData == undefined){
                            bill.uomListData = bill.uom;
                        }
                        angular.forEach(bill.uom, function(u){
                            if(u.uomTypeId != 2){
                                bill.selectedBatch.uom.push(u);
                                // bill.items[index].uom.push(u);
                            }
                        })
                    }
                    else{
                        angular.forEach(batch.uomListData, function(u){
                            if(u.uomTypeId != 2){
                                bill.selectedBatch.uom.push(u);
                                // bill.items[index].uom.push(u);
                            }
                        })
                    }
                    item = bill;
                }
                else{
                    bill.uom = batch!=undefined && batch!=null ? batch.uom :[];
                    item = bill;
                }
            } 

            if (batch!=undefined && batch!=null && !!batch.batchId) {
                //Select Default UOM
                if(item.isThreeUOMApplicable == false){
                    if(item.uomListData == undefined || item.uomListData == null || item.uomListData == ""){
                        item.selectedUom = $filter('filter')(item.uom, {
                            opUom: true
                        }, true)[0];
                    }
                    else{
                        item.selectedUom = $filter('filter')(item.uomListData, {
                            opUom: true
                        }, true)[0];
                    }
                }
                else{
                    item.selectedUom = $filter('filter')(item.uom, {
                        opUom: true
                    }, true)[0];
                }
                if(item.selectedBatch!=undefined && item.selectedBatch!=null &&  item.selectedBatch.selectedUomTypeId!=undefined &&  item.selectedBatch.selectedUomTypeId!=null){
                    item.selectedUom = item.selectedBatch.uom.find(function (obj) {
                        return obj.uomTypeId == item.selectedBatch.selectedUomTypeId;
                      });
                	}
                //"Select UOM" option from UOM dropdown
                if (!item.selectedUom) {
                    item.selectedUom = item.uom[0];
                }
                
              
                
                //Caclulate amount including discount
                /*if(bill.items[index].quantity != 0){
                if(item.quantity !=undefined && item.quantity != 0 ){
                    Calculate(item, bill);
                }*/

               // CalculateAmount(item);

                if(item.selectedBatch.availableStockQuantity != undefined && item.selectedBatch.availableStockQuantity != null){
                    //Convert Ordered Quantity
                    if(item.isThreeUOMApplicable == false){
                        if(item.selectedBatch.uomListData == undefined){
                            ConvertQtyOrdered(item, item.selectedBatch.uomListData, item.selectedUom);
                        }
                        else{
                            ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);
                        }
                    }
                    else{
                        ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);
                    }
                    $scope.selectedUomData = item.selectedUom;
                    //Allocate quantity
                    if(item.quantity !=undefined && item.quantity != 0 ){
                	    // AllocateQuantity(index, bill, parentIndex);
                        Calculate(item, bill);
                    }
                    //AllocateQuantity(index, bill, parentIndex);               

                    //Get current stock for the selected batch
                    var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.AVAILABLE_QTY_API  + $scope.unitId + "/"+ $scope.storeId + "/" + item.itemId + "/" + item.selectedBatch.batchId + "/" + item.selectedUom.uomTypeId;
                    $rootScope.startLoader();
                    GenericService.serviceAction("GET", URI, {}).then(
                    function (response) {
                        item.stock= response.data.availableqty;
                        
                        //Ganesh
                        item.quantity = commonDetailService.getDefaultQuantity(item,item.qtyPrescribed,response.data.availableqty);
                        
                        
                        //                        item.newStock = response.data.availableqty;
                        if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                            $scope.isValidated = true;
                        }
                        else{
                            $scope.isValidated = false;
                        }
                        if($scope.activePackageList!=null&& $scope.activePackageList.length>0){
                            $scope.isValidated = true;
                        }
                        if($scope.atCop == true){
                          item.rate = item.selectedBatch.actualLendingPrice;
                          Calculate(item, self.model.worklistDetails);
                        }
                        else{
                            if(item.selectedBatch.rate != undefined || item.selectedBatch.rate != null){
                                item.rate = item.selectedBatch.rate;
                                Calculate(item, self.model.worklistDetails);
                            }
                            else{
                                item.rate = item.rate;
                            }
                        }
                        $rootScope.stopLoader();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                    });
                }
                else{
                    //AllocateQuantity(index, bill, parentIndex);               

                    //Get current stock for the selected batch
                    var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.AVAILABLE_QTY_API  + $scope.unitId + "/"+ $scope.storeId + "/" + item.itemId + "/" + item.selectedBatch.batchId + "/" + item.selectedUom.uomTypeId;
                    $rootScope.startLoader();
                    GenericService.serviceAction("GET", URI, {}).then(
                        function (response) {
                            item.stock= response.data.availableqty;
                            item.selectedBatch.availableStockQuantity = response.data.availableqty;
                            //Ganesh
                            item.quantity = commonDetailService.getDefaultQuantity(item,item.qtyPrescribed,response.data.availableqty);
                        
                            //                        item.newStock = response.data.availableqty;
                            if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                                $scope.isValidated = true;
                            }
                            else{
                                $scope.isValidated = false;
                            }
                            if($scope.activePackageList!=null&& $scope.activePackageList.length>0){
                                $scope.isValidated = true;
                            }
                            if($scope.atCop == true){
                                item.rate = item.selectedBatch.actualLendingPrice;
                                Calculate(item, self.model.worklistDetails);
                            }
                            else{
                                if(item.selectedBatch.rate != undefined || item.selectedBatch.rate != null){
                                    item.rate = item.selectedBatch.rate;
                                    Calculate(item, self.model.worklistDetails);
                                }
                                else{
                                    item.rate = item.rate;
                                }
                            }
                            if(item.isThreeUOMApplicable == false){
                                ConvertQtyOrdered(item, item.selectedBatch.uomListData, item.selectedUom);
                            }
                            else{
                                ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);
                            }
                            $scope.selectedUomData = item.selectedUom;
                            //Allocate quantity
                            // if(item.quantity !=undefined && item.quantity != 0 ){
                            //     // AllocateQuantity(index, bill, parentIndex);
                            //     Calculate(item, bill);
                            // }
                            $rootScope.stopLoader();
                        },
                        function (err) {
                            $rootScope.stopLoader();
                        }
                    );
                }
            } else{
                //item.selectedBatch = item.batch[0];
                item.selectedBatch = commonDetailService.getDefaultBatchFromConfig(item.batch);
                item.newStock = 0;
                item.quantity = null;
                item.rate = 0;
                item.itemtotalAmount = 0;
                item.patientClassPercentage = 0;
                item.patientClassAmount = 0;
                item.netAmount = 0;
                item.selfPayable = 0;
                item.selectedUom = item.uom[0];
            }
        }
        $scope.clearDiscountOnQtyChange=function(){
            var counter=0;
                if(self.model.worklistDetails.discountCatgeory.id!=undefined && self.model.worklistDetails.discountCatgeory.id!=null)
                {
                    counter++;
                }
                if(counter>0)
                    {
                    angular.forEach(self.model.worklistDetails.items, function(value){
                    	self.model.worklistDetails.discount=0;
                    if(value.disabled==false)
                        {
                    	value.discountAmount=0;
                        value.itemDiscount = "0";
                        value.discountPerItem=0;
                        }
                      self.model.ApplyDiscount(self.model.worklistDetails);
                    });
                    }
                if(self.model.worklistDetails.discountCatgeory.id==undefined || self.model.worklistDetails.discountCatgeory.id==null)
                    {
                	self.model.worklistDetails.discountCatgeory=self.model.discountCategoryList[0];
                    }
        };
        var Calculate = function(item, bill){
            if(item.selectedUom == undefined || item.selectedUom == null || item.selectedUom == ""){
                item.selectedUom =  $scope.selectedUomData;
            }
        	if(item.selectedBatch!=undefined && item.selectedBatch!=null && item.selectedBatch.length!=0)
    		{
        	    if($scope.globalFlag==1)
    		    {
    		        $scope.atCop=$scope.atCopSelected;
    		        if(item.disabled==false)
    			    {
    			        if($scope.atCop == true){
                            item.selectedBatch.sellingPrice = item.selectedBatch.actualLendingPrice;
                            item.rate = item.selectedBatch.actualLendingPrice;
                            item.mrp = item.rate;
                        }
                        else{
                            if(item.selectedBatch.rate != undefined || item.selectedBatch.rate != null){
                                item.selectedBatch.sellingPrice = item.selectedBatch.rate;
                                item.rate = item.selectedBatch.rate;
                            }
                            else{
                                item.selectedBatch.sellingPrice = item.rate;
                                item.rate = item.rate;
                            }
                        }
    			    }
    		    }
        	    else
        		{
        		    if(item.disabled==true)
            	    {
            	        $scope.atCop=item.atCop;
            	    }
            	    else
            		{
            		    $scope.atCop=$scope.atCopSelected;
            		}
                    if($scope.atCop == true){
                        item.selectedBatch.sellingPrice = item.selectedBatch.actualLendingPrice;
                        item.rate = item.selectedBatch.actualLendingPrice;
                        item.mrp = item.rate;
                    }
                    else{
                        if(item.selectedBatch.rate != undefined || item.selectedBatch.rate != null){
                            item.selectedBatch.sellingPrice = item.selectedBatch.rate;
                            item.rate = item.selectedBatch.rate;
                        }
                        else{
                            item.selectedBatch.sellingPrice = item.rate;
                            item.rate = item.rate;
                        }
                    }
        		}
                var sellingPrice = !!item.selectedBatch ? commonDetailService.fixedDecimal(item.selectedBatch.sellingPrice) : 0;
                if(item.selectedBatch != undefined && item.selectedBatch != null && item.selectedUom != undefined && item.selectedUom != null){
                	if(item.isThreeUOMApplicable == false){
                        item.mrp = commonDetailService.calculateRate(item.selectedBatch.uomListData, item.selectedUom.uomTypeId, sellingPrice);
                    }
                    else{
                        item.mrp = commonDetailService.calculateRate(item.selectedBatch.uom, item.selectedUom.uomTypeId, sellingPrice);
                    }
                }
            }
            self.model.beforeCalculation = item;
                // if($scope.atCop == true){
                //     item.mrp = item.rate;
                // }
            var data = {
                "mrp": item.mrp,
                "isTaxInc": $scope.taxInclusive,
                // "markup": !!item.markUpDown ? item.markUpDownPer : null,
                // "markdown": !item.markUpDown ? item.markUpDownPer : null,
                "markup": item.markUpDown,
                "markUpDownPer" : item.markUpDownPer,
                // "discount": bill.discount == 0 || bill.discount == undefined ? item.discountPerItem== 0 || bill.discountPerItem == undefined  ? item.discountPerItem : bill.discount : 0,
                "discount": ((bill.discount == 0 || bill.discount == undefined) || (item.discountPerItem== 0 || bill.discountPerItem == undefined))  ? item.discountPerItem : bill.discount,
                "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                "tax": item.taxPercentage,
                "qty": (item.quantity!=undefined && item.quantity!=null && item.quantity != 0) ? item.quantity : item.issuedQty,
                "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                "patientClassPercentage": item.patientClassPercentage,
            }
            if(!data.discount){
                data.discount = 0;
            }
            if(data.qty==undefined || data.qty==null || data.qty==0)
            {
                data.qty=item.newDispQty!=undefined ? item.newDispQty :0;
            }
            var response = commonDetailService.calculateAmount(data);
            item.rate = response.rate;
            if(((item.batchDetails != undefined && item.batchDetails != null && item.batchDetails != "") || item.addCopyItemFlag == true 
            		|| item.NonFormularyAddedFlag == true || item.addBatchTopFlag == true || item.addItemTopFlag == true 
            		|| item.isSelected == true || item.statusId == 1 || self.model.worklistDetails.statusId == 1) 
            		&& (item.disabled != true && item.itemDisabled != true && item.statusId != 18 && item.statusId != 4)){
            	item.taxAmount = response.taxAmount;
                item.responseTaxAmount =  response.taxAmount;
                item.unitDiscountAmount = response.unitDiscountAmount;
                item.itemDiscount =  response.discountAmount;
                item.discountAmount = response.discountAmount;
                item.itemtotalAmount = response.totalAmount
                item.totalAmount = response.totalAmount;
                item.netAmount = response.netAmount;
                if(item.statusId!=4)
                	{
                	item.selfPayable = response.netAmount;
                	}
                item.packageConsumptionAmt = response.packageConsumptionAmt!=undefined && response.packageConsumptionAmt!=null && response.packageConsumptionAmt!=0 ? response.packageConsumptionAmt:item.packageConsumptionAmt;
                item.patientClassAmount = response.patientClassAmount;
                item.unitPatientClassAmount = response.unitPatientClassAmount;
            }
            
            if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                $scope.isValidated = true;
            }
            else{
                $scope.isValidated = false;
            }
            if($scope.activePackageList!=null&& $scope.activePackageList.length>0){
                $scope.isValidated = true;
            }
            if(item.selectedItems != undefined && item.selectedItems != null && item.selectedItems.length > 0){
                angular.forEach(item.selectedItems, function(s){
                	$scope.afterSaveUOM;
                	if(item.selectedBatch!=undefined && item.selectedBatch!=null && item.selectedBatch.length!=0)
            		{
                    	if($scope.globalFlag==1)
                		{
                    		$scope.atCop=$scope.atCopSelected;
            	        	if(s.disabled==false || s.statusId!=4)
            			    {
            			        angular.forEach(s.batch, function(a){
                                    if($scope.atCop == true){
                            	        if(a.isSelected)
                            		    {
                                          a.sellingPrice = a.actualLendingPrice;
                                            a.rate = a.actualLendingPrice;
                                            $scope.afterSaveUOM=a.uom;
                                        }
                            	        if(s.selectedBatch!=undefined)
                            		    {
                            		        s.selectedBatch.sellingPrice = s.selectedBatch.actualLendingPrice;
                                            s.rate = s.selectedBatch.actualLendingPrice;
                            		    }
                                    }
                                    else{
                            	        if(a.isSelected)
                        		        {
                                            a.sellingPrice = a.rate;
                                            a.rate = a.rate;
                                            $scope.afterSaveUOM=a.uom;
                        		        }
                            	        if(s.selectedBatch!=undefined)
                        		        {
                                            s.selectedBatch.sellingPrice = s.selectedBatch.rate!=undefined ? s.selectedBatch.rate : s.selectedBatch.sellingPrice;
                                            s.rate = s.selectedBatch.rate!=undefined ? s.selectedBatch.rate : s.selectedBatch.sellingPrice;
                        		        }
                                    }
                                });
            			    }
            		    }
                	    else
                		{
                	        if(s.disabled==true || s.statusId==4)
                	        {
                	            $scope.atCop=s.atCop;
                            }
                	        else
                		    {
                		        $scope.atCop=$scope.atCopSelected;
                		    }
                            angular.forEach(s.batch, function(a){
                                if($scope.atCop == true){
                        	        if(a.isSelected)
                        		    {
                                        a.sellingPrice = a.actualLendingPrice;
                                        a.rate = a.actualLendingPrice;
                                        $scope.afterSaveUOM=a.uom;
                        		    }
                        	        if(s.selectedBatch!=undefined)
                        		    {
                                        s.selectedBatch.sellingPrice = s.selectedBatch.actualLendingPrice;
                                        s.rate = s.selectedBatch.actualLendingPrice;
                        		    }
                                }
                                else{
                        	        if(a.isSelected)
                    		        {
                                        a.sellingPrice = a.rate;
                                        a.rate = a.rate;
                                        $scope.afterSaveUOM=a.uom;
                    		        }   
                        	        if(s.selectedBatch!=undefined)
                    		        {
                                        s.selectedBatch.sellingPrice = s.selectedBatch.rate!=undefined ? s.selectedBatch.rate : s.selectedBatch.sellingPrice;
                                        s.rate = s.selectedBatch.rate!=undefined ? s.selectedBatch.rate : s.selectedBatch.sellingPrice;
                    		        }
                                }
                            });
                		}
                    	if($scope.autoPrescribedQty == true){
                    		if(s.disabled == true && s.statusId == 4){
                        		s.rate = s.mrp; 
                        	}
                        	else{
                        		var sellingPrice = !!s.selectedBatch ? commonDetailService.fixedDecimal(s.selectedBatch.sellingPrice!=undefined ||s.selectedBatch.sellingPrice!=null ? s.selectedBatch.sellingPrice :s.selectedBatch.rate) : 0;
                                s.mrp = commonDetailService.calculateRate(s.selectedBatch!=undefined ?s.selectedBatch.uom : $scope.afterSaveUOM, s.selectedUom!=undefined ? s.selectedUom.uomTypeId :s.uomTypeId, sellingPrice!=undefined || sellingPrice!=0 ? sellingPrice : s.rate);
                        	}
                    	}
                    	else{
                    		var sellingPrice = !!s.selectedBatch ? commonDetailService.fixedDecimal(s.selectedBatch.sellingPrice!=undefined ||s.selectedBatch.sellingPrice!=null ? s.selectedBatch.sellingPrice :s.selectedBatch.rate) : 0;
                            s.mrp = commonDetailService.calculateRate(s.selectedBatch!=undefined ?s.selectedBatch.uom : $scope.afterSaveUOM, s.selectedUom!=undefined ? s.selectedUom.uomTypeId :s.uomTypeId, sellingPrice!=undefined || sellingPrice!=0 ? sellingPrice : s.rate);
                    	}   
                    }
                    self.model.beforeCalculation = s;
                    var data = {
                        "mrp": s.mrp!=null || s.mrp!=undefined || !isNaN(s.mrp) ? s.mrp : s.selectedBatch!=undefined ? s.selectedBatch.rate:s.rate,
                        "isTaxInc": $scope.taxInclusive,
                        "markup": item.markUpDown,
                        "markUpDownPer" : item.markUpDownPer,
                        "discount": ((bill.discount == 0 || bill.discount == undefined) || (s.discountPerItem== 0 || bill.discountPerItem == undefined))  ? s.discountPerItem : bill.discount,
                        "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                        "tax": s.taxPercentage,
                        "qty": s.quantity ? s.quantity : s.newDispQty,
                        "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                        "patientClassPercentage": s.patientClassPercentage,
                    }
                    
                    if(!data.discount){
                        data.discount = 0;
                    }
                    if(data.qty==undefined || data.qty==null || data.qty==0)
                    {
                        data.qty=s.newDispQty!=undefined ? s.newDispQty :0;
                    }
                    var response = commonDetailService.calculateAmount(data);
                    s.rate = response.rate;
                    if(s.disabled!=true)
                    	{
                    s.responseTaxAmount =  response.taxAmount;
                    s.unitDiscountAmount = response.unitDiscountAmount;
                    s.itemDiscount =  response.discountAmount;
                    s.discountAmount = response.discountAmount;
                    s.itemtotalAmount = response.totalAmount
                    s.totalAmount = response.totalAmount;
                    s.netAmount = response.netAmount;
                    if(s.statusId!=4)
                	{
                    	s.selfPayable = response.netAmount;
                	}
                    s.packageConsumptionAmt = response.packageConsumptionAmt;
                    s.patientClassAmount = response.patientClassAmount;
                    s.unitPatientClassAmount = response.unitPatientClassAmount;
                    if(s.statusId == 4 && s.disabled == true){
                		s.taxAmount = (((s.itemtotalAmount) - (s.patientClassAmount) - (s.itemDiscount)) * (s.taxPercentage / 100)).toFixed(2);
                    }
                    else{
                      s.taxAmount = response.taxAmount;
                    }
                }
                    if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                        $scope.isValidated = true;
                    }
                    else{
                        $scope.isValidated = false;
                    }
                    if($scope.activePackageList!=null&& $scope.activePackageList.length>0){
                        $scope.isValidated = true;
                    }    
                });
            }
            if(item.quantity != undefined && item.quantity != null && item.quantity != 0 && self.model.worklistDetails.statusId != 4 && item.disabled != true && item.itemDisabled != true){
            	if((item.batchDetails != undefined && item.batchDetails != null && item.batchDetails != "") || item.addCopyItemFlag == true || item.NonFormularyAddedFlag == true || item.addBatchTopFlag == true || item.addItemTopFlag == true || item.isSelected == true || self.model.worklistDetails.statusId == 1){
               	 CalculateAmount(item);
               }
            }
        }
        
        var onChangeDiscount = function(item, method){
            if(validateDiscount(item,method)){
                if(method =='linewise'){
                    angular.forEach(item.items, function (value, key) {
                        if(value.discountPerItem > 100){
                            growl.error("Discount should be less than Due amount or zero. ")
                            value.discountPerItem = 0;
                            value.patientClassPercentage = 0;
                        }else{
                            item.discount = 0;
                            self.model.worklistDetails.discountCatgeory.discountCategory = "Select Discount Category";
                            self.model.worklistDetails.discountCatgeory.id = undefined;
                            item.discountType = 1;
                        }
                    }); 
                }
                else{
                    if(self.model.worklistDetails.discountType == 1){
                        angular.forEach(item.items, function (value, key) {
                            // bill.items[key]['unitDiscountAmount'] = unitDiscountAmount;
                            
                        //  value.discountPerItem = item.discount;
                        });
                    }
                }
            }
            //else{
              //  growl.error("Please Enter Valid Discount.")
           // }
        }
        var validateDiscount = function(item, method){
            var regexp = /^(\d{0,4}(\.\d{0,2})?)$/;
            var isValid = true;
            if(method =='linewise'){
                angular.forEach(item.items, function (value, key) {
                   
                     if(regexp.test(Number(value.discountPerItem))){
                        isValid = true;
                        self.model.worklistDetails.discountCatgeory.discountCategory = "Select Discount Category";
                        self.model.worklistDetails.discountCatgeory.id = undefined;
                     }
                     else{
                        isValid = false;
                        value.discountPerItem = 0;
                     }
                     if(value.selectedItems != undefined && value.selectedItems.length != 0){
                    	 angular.forEach(value.selectedItems, function (valueSub, key) {
                    		 if(regexp.test(Number(valueSub.discountPerItem))){
                                 isValid = true;
                                 self.model.worklistDetails.discountCatgeory.discountCategory = "Select Discount Category";
                                 self.model.worklistDetails.discountCatgeory.id = undefined;
                              }
                              else{
                                 isValid = false;
                                 valueSub.discountPerItem = 0;
                              }
                    	 });
                     }
                }); 
            }
            else{
                    // if(regexp.test(Number(item.discount))){
                    //     isValid = true;
                    // }
                    // else{
                    //     isValid = false;
                    //     item.discount = 0;
                    // }
                    if(self.model.worklistDetails.discountType == 1){
                        if(item.discount > 100){
                            isValid = false;
                            item.discount = 0;
                        }
                        else{
                            isValid = true;
                        }
                    }
                    else{
                        isValid = true;
                    }
            }
                return isValid
          }
        var BatchChangeOld = function (index, bill, batch, parentIndex) {
            var item = null;

            if (parentIndex == null) {
                bill.items[index].uom = batch.uom;
                item = bill.items[index];
            } else {
                bill.items[parentIndex].selectedItems[index].uom = batch.uom;
                item = bill.items[parentIndex].selectedItems[index];
            }

            // item.uom = [{
            //     uomDetails: "Select UOM",
            // }];

            if (!!batch.batchId) {
                //item.uom = item.uom.concat(batch.uom);

                item.selectedUom = $filter('filter')(item.uom, {
                    opUom: true
                }, true)[0];

                if (!item.selectedUom) {
                    item.selectedUom = item.uom[0];
                }

                CalculateRate(item, item.selectedUom, item.uom);
                ApplyDiscount(self.model.worklistDetails);
                // AllocateQuantity(index, bill, parentIndex);
                var cookieObject = $cookies.getObject('cookieObject');
                $scope.storeIdNew = cookieObject.storeId;
                var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.AVAILABLE_QTY_API  + $scope.unitId + "/"+ $scope.storeIdNew + "/" + item.itemId + "/" + item.selectedUom.batchId + "/" + item.selectedUom.uomTypeId;

                $rootScope.startLoader();
                GenericService.serviceAction("GET", URI, {}).then(
                    function (response) {
                        item.stock= response.data.availableqty;
                        item.newStock = response.data.availableqty;
                        //Ganesh
                        item.quantity = commonDetailService.getDefaultQuantity(item,response.data.availableqty);
                        $rootScope.stopLoader();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                    });
            
                
            } else {
                // item.uom = [{
                //     uomDetails: "Select UOM",
                // }];
                item.selectedUom = item.uom[0];
                item.quantity = null;
                item.rate = 0;
            }
            ConvertQtyOrdered(item, item.uom, item.selectedUom);
        }

        var ConvertQtyOrdered = function (item, uom, uomList) {
        	if(item.uom==undefined || item.uom==null || item.uom.length<=1)
        		{
        		var counter =0;
        		angular.forEach(item.batch, function (batch, key) {
        			if(counter==0 && batch.uom!=undefined && batch.uom!=null && batch.uom.length!=0 )
        				{
        				item.uom=batch.uom;
        				counter++;
        				}
        		});
        		}
            if(uomList != undefined && uomList != null){
            	item.totalStock=0;
                angular.forEach(item.batch, function (batch, key) {
              	  item.totalStock+=batch.availableStockQuantity!=undefined && batch.availableStockQuantity!=null ? batch.availableStockQuantity:0;
                })
                var newUOM = [];
                if(item.isThreeUOMApplicable == false){
                    if(item.selectedBatch.uomListData == undefined){
                        item.uom.find(function (value) {
                            if (!!value.uomTypeId) {
                                newUOM.push(value);
                            }
                        });
                    }
                    else{
                        item.selectedBatch.uomListData.find(function (value) {
                            if (!!value.uomTypeId) {
                                newUOM.push(value);
                            }
                        });
                    }
                }
                else{
                    uom.find(function (value) {
                        if (!!value.uomTypeId) {
                            newUOM.push(value);
                        }
                    });
                }
                if(item.qtyPrescribed == undefined || item.qtyPrescribed == null || item.qtyPrescribed == ""){
                    item.qtyPrescribed = 0;
                }
                if( uomList != null){
                    var uomDetails = uomList.uomDetails.split(' ')[0];
                    if(item.isThreeUOMApplicable == true)
                    {
                    if (uomDetails == "P") {
                        item.totalStock = Math.floor(item.totalStock / (item.uom[0].conversion * item.uom[1].conversion * item.uom[2].conversion));
                    } else if (uomDetails == "S") {
                        item.totalStock = Math.floor(item.totalStock / (item.uom[1].conversion * item.uom[2].conversion));
                    } else if (uomDetails == "D") {
                        item.totalStock = Math.floor(item.totalStock / (item.uom[2].conversion));
                    }
                    }
                    else
                    	{
                    	if (uomDetails == "P") {
                            item.totalStock = Math.floor(item.totalStock / (item.uom[0].conversion * item.uom[1].conversion));
                        } else if (uomDetails == "D") {
                            item.totalStock = Math.floor(item.totalStock / (item.uom[1].conversion));
                        }
                    	}
                }
                if(uomList != undefined)
                {
                    var qtyPrescribed = item.qtyPrescribed;
                    //item.quantity = 0;
                    if(uomList != null){
                        var uomDetails = uomList.uomDetails.split(' ')[0];
                        if(newUOM[2] == undefined || newUOM[2] == null){
                            if(item.uoms == undefined || item.uoms == null){
                                if(item.uom[2] == undefined || item.uom[2] == null){
                                    if (uomDetails == "P") {
                                        item.newQty = Math.ceil(qtyPrescribed / (item.uomListData[0].conversion * item.uomListData[1].conversion * item.uomListData[2].conversion));
                                        if(item.newQty==0 && qtyPrescribed >0)
                                        {
                                        item.newQty=1;
                                        }                     
                                        item.newStock = Math.floor(item.selectedBatch.availableStockQuantity / (item.uomListData[0].conversion * item.uomListData[1].conversion * item.uomListData[2].conversion));
                                    } 
                                    else if (uomDetails == "S") {
                                        item.newQty = Math.ceil(qtyPrescribed / (item.uomListData[1].conversion * item.uomListData[2].conversion));
                                        if(item.newQty==0 && qtyPrescribed >0)
                                        {
                                            item.newQty=1;
                                        }
                                        item.newStock = Math.floor(item.selectedBatch.availableStockQuantity / (item.uomListData[1].conversion * item.uom[2].conversion));
                                    } 
                                    else if (uomDetails == "D") {
                                        if(item.uom[2] == undefined || item.uom[2] == null){
                                            item.newQty = Math.ceil(qtyPrescribed / (item.uom[1].conversion));
                                            item.newStock = Math.floor(item.selectedBatch.availableStockQuantity / (item.uom[1].conversion));
                                        }
                                        else{
                                            item.newQty = Math.ceil(qtyPrescribed / (item.uom[2].conversion));
                                            item.newStock = Math.floor(item.selectedBatch.availableStockQuantity / (item.uom[2].conversion));
                                        }
                                    }
                                }
                                else{
                                    if (uomDetails == "P") {
                                        item.newQty = Math.ceil(qtyPrescribed / (item.uom[0].conversion * item.uom[1].conversion * item.uom[2].conversion));
                                        if(item.newQty==0 && qtyPrescribed >0)
                                        {
                                        item.newQty=1;
                                        }                     
                                        item.newStock = Math.floor(item.selectedBatch.availableStockQuantity / (item.uom[0].conversion * item.uom[1].conversion * item.uom[2].conversion));
                                    } 
                                    else if (uomDetails == "S") {
                                        item.newQty = Math.ceil(qtyPrescribed / (item.uom[1].conversion * item.uom[2].conversion));
                                        if(item.newQty==0 && qtyPrescribed >0)
                                        {
                                            item.newQty=1;
                                        }
                                        item.newStock = Math.floor(item.selectedBatch.availableStockQuantity / (item.uom[1].conversion * item.uom[2].conversion));
                                    } 
                                    else if (uomDetails == "D") {
                                        if(item.uom[2] == undefined || item.uom[2] == null){
                                            item.newQty = Math.ceil(qtyPrescribed / (item.uom[1].conversion));
                                            item.newStock = Math.floor(item.selectedBatch.availableStockQuantity / (item.uom[1].conversion));
                                        }
                                        else{
                                            item.newQty = Math.ceil(qtyPrescribed / (item.uom[2].conversion));
                                            item.newStock = Math.floor(item.selectedBatch.availableStockQuantity / (item.uom[2].conversion));
                                        }
                                    }
                                }
                            }
                            else{
                                if (uomDetails == "P") {
                                    item.newQty = Math.ceil(qtyPrescribed / (item.uoms[0].conversion * item.uoms[1].conversion * item.uoms[2].conversion));
                                    if(item.newQty==0 && qtyPrescribed >0)
                                    {
                                    item.newQty=1;
                                    }                     
                                    item.newStock = Math.floor(item.selectedBatch.availableStockQuantity / (item.uoms[0].conversion * item.uoms[1].conversion * item.uoms[2].conversion));
                                } 
                                else if (uomDetails == "S") {
                                    item.newQty = Math.ceil(qtyPrescribed / (item.uoms[1].conversion * item.uoms[2].conversion));
                                    if(item.newQty==0 && qtyPrescribed >0)
                                    {
                                        item.newQty=1;
                                    }
                                    item.newStock = Math.floor(item.selectedBatch.availableStockQuantity / (item.uoms[1].conversion * item.uoms[2].conversion));
                                } 
                                else if (uomDetails == "D") {
                                    item.newQty = Math.ceil(qtyPrescribed / (item.uoms[2].conversion));
                                    item.newStock = Math.floor(item.selectedBatch.availableStockQuantity / (item.uoms[2].conversion));
                                }
                            }
                        }
                        else{
                            if (uomDetails == "P") {
                                item.newQty = Math.ceil(qtyPrescribed / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
                                if(item.newQty==0 && qtyPrescribed >0)
                                {
                                item.newQty=1;
                                }                     
                                item.newStock = Math.floor(item.selectedBatch.availableStockQuantity / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
                            } 
                            else if (uomDetails == "S") {
                                item.newQty = Math.ceil(qtyPrescribed / (newUOM[1].conversion * newUOM[2].conversion));
                                if(item.newQty==0 && qtyPrescribed >0)
                                {
                                    item.newQty=1;
                                }
                                item.newStock = Math.floor(item.selectedBatch.availableStockQuantity / (newUOM[1].conversion * newUOM[2].conversion));
                            } 
                            else if (uomDetails == "D") {
                                item.newQty = Math.ceil(qtyPrescribed / (newUOM[2].conversion));
                                item.newStock = Math.floor(item.selectedBatch.availableStockQuantity / (newUOM[2].conversion));
                            }
                        }
                    }
                }
                if(item.checkNewItem==true)
                {
                    if( uomList != null){
                        var uomDetails = uomList.uomDetails.split(' ')[0];
                        if(newUOM[2] == undefined || newUOM[2] == null){
                            if(item.uoms == undefined || item.uoms == null){
                                if (uomDetails == "P") {
                                    item.newStock = Math.floor(item.selectedBatch.availableStockQuantity / (item.uom[0].conversion * item.uom[1].conversion * item.uom[2].conversion));
                                } else if (uomDetails == "S") {
                                    item.newStock = Math.floor(item.selectedBatch.availableStockQuantity / (item.uom[1].conversion * item.uom[2].conversion));
                                } else if (uomDetails == "D") {
                                    item.newStock = Math.floor(item.selectedBatch.availableStockQuantity / (item.uom[2].conversion));
                                }
                            }
                            else{
                                if (uomDetails == "P") {
                                    item.newStock = Math.floor(item.selectedBatch.availableStockQuantity / (item.uoms[0].conversion * item.uoms[1].conversion * item.uoms[2].conversion));
                                } else if (uomDetails == "S") {
                                    item.newStock = Math.floor(item.selectedBatch.availableStockQuantity / (item.uoms[1].conversion * item.uoms[2].conversion));
                                } else if (uomDetails == "D") {
                                    item.newStock = Math.floor(item.selectedBatch.availableStockQuantity / (item.uoms[2].conversion));
                                }
                            }
                        }
                        else{
                            if (uomDetails == "P") {
                                item.newStock = Math.floor(item.selectedBatch.availableStockQuantity / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
                            } else if (uomDetails == "S") {
                                item.newStock = Math.floor(item.selectedBatch.availableStockQuantity / (newUOM[1].conversion * newUOM[2].conversion));
                            } else if (uomDetails == "D") {
                                item.newStock = Math.floor(item.selectedBatch.availableStockQuantity / (newUOM[2].conversion));
                            }
                        }
                    }
                }

                if(item.quantity == undefined || item.quantity == null || item.quantity == ""){
                    item.quantity = 0;
                }

                if(item.newDispQty == undefined || item.newDispQty == null || item.newDispQty == ""){
                    item.newDispQty = 0;
                }

                if($scope.autoPrescribedQty == true && item.statusId != 4 && item.itemDisabled != true && item.disabled != true){
                	if(item.newStock > item.newQty){
                		item.quantity = item.newQty;
                	}
                	else{
                		item.quantity = item.newStock;
                	}
                }

                if(item.isThreeUOMApplicable == false){
                    if(newUOM[2] == undefined || newUOM[2] == null){
                        if(item.uoms == undefined || item.uoms == null){
                            if(item.uom[2] == undefined || item.uom[2] == null){
                                if(item.statusId == 18 || item.statusId == 4){
                                    if(item.issuedQty == undefined || item.issuedQty == null){
                                        if(item.selectedUom.uomTypeId == 1){
                                            item.receivedQtyCon = (item.newDispQty +"*"+ item.uomListData[0].conversion);
                                            item.receivedQtyCalculation = (item.newDispQty * item.uomListData[0].conversion);
                                            item.uomUnitData = item.uomListData[2].uomUnit;
                                        }
                                        else if(item.selectedUom.uomTypeId == 2){
                    
                                        }
                                        else{
                                            item.receivedQtyCon = (item.newDispQty);
                                            item.receivedQtyCalculation = (item.newDispQty);
                                            item.uomUnitData = item.uomListData[2].uomUnit;
                                        }
                                    }
                                    else{
                                        if(item.selectedUom.uomTypeId == 1){
                                            item.receivedQtyCon = (item.issuedQty +"*"+ item.uomListData[0].conversion);
                                            item.receivedQtyCalculation = (item.issuedQty * item.uomListData[0].conversion);
                                            item.uomUnitData = item.uomListData[2].uomUnit;
                                        }
                                        else if(item.selectedUom.uomTypeId == 2){
                    
                                        }
                                        else{
                                            item.receivedQtyCon = (item.issuedQty);
                                            item.receivedQtyCalculation = (item.issuedQty);
                                            item.uomUnitData = item.uomListData[2].uomUnit;
                                        }
                                    }
                                }
                                else{
                                    if(item.selectedUom.uomTypeId == 1){
                                        item.receivedQtyCon = (item.quantity +"*"+ item.uomListData[0].conversion);
                                        item.receivedQtyCalculation = (item.quantity * item.uomListData[0].conversion);
                                        item.uomUnitData = item.uomListData[2].uomUnit;
                                    }
                                    else if(item.selectedUom.uomTypeId == 2){
                
                                    }
                                    else{
                                        item.receivedQtyCon = (item.quantity);
                                        item.receivedQtyCalculation = (item.quantity);
                                        item.uomUnitData = item.uomListData[2].uomUnit;
                                    }
                                }
                            }
                            else{
                                if(item.statusId == 18 || item.statusId == 4){
                                    if(item.issuedQty == undefined || item.issuedQty == null){
                                        if(item.selectedUom.uomTypeId == 1){
                                            item.receivedQtyCon = (item.newDispQty +"*"+ item.uom[0].conversion);
                                            item.receivedQtyCalculation = (item.newDispQty * item.uom[0].conversion);
                                            item.uomUnitData = item.uom[2].uomUnit;
                                        }
                                        else if(item.selectedUom.uomTypeId == 2){
                    
                                        }
                                        else{
                                            item.receivedQtyCon = (item.newDispQty);
                                            item.receivedQtyCalculation = (item.newDispQty);
                                            item.uomUnitData = item.uom[2].uomUnit;
                                        }
                                    }
                                    else{
                                        if(item.selectedUom.uomTypeId == 1){
                                            item.receivedQtyCon = (item.issuedQty +"*"+ item.uom[0].conversion);
                                            item.receivedQtyCalculation = (item.issuedQty * item.uom[0].conversion);
                                            item.uomUnitData = item.uom[2].uomUnit;
                                        }
                                        else if(item.selectedUom.uomTypeId == 2){
                    
                                        }
                                        else{
                                            item.receivedQtyCon = (item.issuedQty);
                                            item.receivedQtyCalculation = (item.issuedQty);
                                            item.uomUnitData = item.uom[2].uomUnit;
                                        }
                                    }
                                }
                                else{
                                    if(item.selectedUom.uomTypeId == 1){
                                        item.receivedQtyCon = (item.quantity +"*"+ item.uom[0].conversion);
                                        item.receivedQtyCalculation = (item.quantity * item.uom[0].conversion);
                                        item.uomUnitData = item.uom[2].uomUnit;
                                    }
                                    else if(item.selectedUom.uomTypeId == 2){
                
                                    }
                                    else{
                                        item.receivedQtyCon = (item.quantity);
                                        item.receivedQtyCalculation = (item.quantity);
                                        item.uomUnitData = item.uom[2].uomUnit;
                                    }
                                }
                            }
                        }
                        else{
                            if(item.statusId == 18 || item.statusId == 4){
                                if(item.issuedQty == undefined || item.issuedQty == null){
                                    if(item.selectedUom.uomTypeId == 1){
                                        item.receivedQtyCon = (item.newDispQty +"*"+ item.uoms[0].conversion);
                                        item.receivedQtyCalculation = (item.newDispQty * item.uoms[0].conversion);
                                        item.uomUnitData = item.uoms[2].uomUnit;
                                    }
                                    else if(item.selectedUom.uomTypeId == 2){
                
                                    }
                                    else{
                                        item.receivedQtyCon = (item.newDispQty);
                                        item.receivedQtyCalculation = (item.newDispQty);
                                        item.uomUnitData = item.uoms[2].uomUnit;
                                    }
                                }
                                else{
                                    if(item.selectedUom.uomTypeId == 1){
                                        item.receivedQtyCon = (item.issuedQty +"*"+ item.uoms[0].conversion);
                                        item.receivedQtyCalculation = (item.issuedQty * item.uoms[0].conversion);
                                        item.uomUnitData = item.uoms[2].uomUnit;
                                    }
                                    else if(item.selectedUom.uomTypeId == 2){
                
                                    }
                                    else{
                                        item.receivedQtyCon = (item.issuedQty);
                                        item.receivedQtyCalculation = (item.issuedQty);
                                        item.uomUnitData = item.uoms[2].uomUnit;
                                    }
                                }
                            }
                            else{
                                if(item.selectedUom.uomTypeId == 1){
                                    item.receivedQtyCon = (item.quantity +"*"+ item.uoms[0].conversion);
                                    item.receivedQtyCalculation = (item.quantity * item.uoms[0].conversion);
                                    item.uomUnitData = item.uoms[2].uomUnit;
                                }
                                else if(item.selectedUom.uomTypeId == 2){
            
                                }
                                else{
                                    item.receivedQtyCon = (item.quantity);
                                    item.receivedQtyCalculation = (item.quantity);
                                    item.uomUnitData = item.uoms[2].uomUnit;
                                }
                            }
                        }
                    }
                    else{
                        if(item.statusId == 18 || item.statusId == 4){
                            if(item.issuedQty == undefined || item.issuedQty == null){
                                if(item.selectedUom.uomTypeId == 1){
                                    item.receivedQtyCon = (item.newDispQty +"*"+ newUOM[0].conversion);
                                    item.receivedQtyCalculation = (item.newDispQty * newUOM[0].conversion);
                                    item.uomUnitData = newUOM[2].uomUnit;
                                }
                                else if(item.selectedUom.uomTypeId == 2){
            
                                }
                                else{
                                    item.receivedQtyCon = (item.newDispQty);
                                    item.receivedQtyCalculation = (item.newDispQty);
                                    item.uomUnitData = newUOM[2].uomUnit;
                                }
                            }
                            else{
                                if(item.selectedUom.uomTypeId == 1){
                                    item.receivedQtyCon = (item.issuedQty +"*"+ newUOM[0].conversion);
                                    item.receivedQtyCalculation = (item.issuedQty * newUOM[0].conversion);
                                    item.uomUnitData = newUOM[2].uomUnit;
                                }
                                else if(item.selectedUom.uomTypeId == 2){
            
                                }
                                else{
                                    item.receivedQtyCon = (item.issuedQty);
                                    item.receivedQtyCalculation = (item.issuedQty);
                                    item.uomUnitData = newUOM[2].uomUnit;
                                }
                            }
                        }
                        else{
                            if(item.selectedUom.uomTypeId == 1){
                                item.receivedQtyCon = (item.quantity +"*"+ newUOM[0].conversion);
                                item.receivedQtyCalculation = (item.quantity * newUOM[0].conversion);
                                item.uomUnitData = newUOM[2].uomUnit;
                            }
                            else if(item.selectedUom.uomTypeId == 2){
        
                            }
                            else{
                                item.receivedQtyCon = (item.quantity);
                                item.receivedQtyCalculation = (item.quantity);
                                item.uomUnitData = newUOM[2].uomUnit;
                            }
                        }
                    }
                }
                else{
                    if(item.statusId == 18 || item.statusId == 4){
                        if(item.issuedQty == undefined || item.issuedQty == null){
                            if(item.selectedUom.uomTypeId == 1){
                                item.receivedQtyCon = (item.newDispQty +"*"+ newUOM[0].conversion+"*"+ newUOM[1].conversion);
                                item.receivedQtyCalculation = (item.newDispQty * newUOM[0].conversion * newUOM[1].conversion);
                                item.uomUnitData = newUOM[2].uomUnit;
                            }
                            else if(item.selectedUom.uomTypeId == 2){
                                item.receivedQtyCon = (item.newDispQty +"*"+ newUOM[1].conversion);
                                item.receivedQtyCalculation = (item.newDispQty * newUOM[1].conversion);
                                item.uomUnitData = newUOM[2].uomUnit;
                            }
                            else{
                                item.receivedQtyCon = (item.newDispQty);
                                item.receivedQtyCalculation = (item.newDispQty);
                                item.uomUnitData = newUOM[2].uomUnit;
                            }
                        }
                        else{
                            if(item.selectedUom.uomTypeId == 1){
                                item.receivedQtyCon = (item.issuedQty +"*"+ newUOM[0].conversion+"*"+ newUOM[1].conversion);
                                item.receivedQtyCalculation = (item.issuedQty * newUOM[0].conversion * newUOM[1].conversion);
                                item.uomUnitData = newUOM[2].uomUnit;
                            }
                            else if(item.selectedUom.uomTypeId == 2){
                                item.receivedQtyCon = (item.issuedQty +"*"+ newUOM[1].conversion);
                                item.receivedQtyCalculation = (item.issuedQty * newUOM[1].conversion);
                                item.uomUnitData = newUOM[2].uomUnit;
                            }
                            else{
                                item.receivedQtyCon = (item.issuedQty);
                                item.receivedQtyCalculation = (item.issuedQty);
                                item.uomUnitData = newUOM[2].uomUnit;
                            }
                        }
                    }
                    else{
                        if(item.selectedUom.uomTypeId == 1){
                            item.receivedQtyCon = (item.quantity +"*"+ newUOM[0].conversion+"*"+ newUOM[1].conversion);
                            item.receivedQtyCalculation = (item.quantity * newUOM[0].conversion * newUOM[1].conversion);
                            item.uomUnitData = newUOM[2].uomUnit;
                        }
                        else if(item.selectedUom.uomTypeId == 2){
                            item.receivedQtyCon = (item.quantity +"*"+ newUOM[1].conversion);
                            item.receivedQtyCalculation = (item.quantity * newUOM[1].conversion);
                            item.uomUnitData = newUOM[2].uomUnit;
                        }
                        else{
                            item.receivedQtyCon = (item.quantity);
                            item.receivedQtyCalculation = (item.quantity);
                            item.uomUnitData = newUOM[2].uomUnit;
                        }
                    }
                }
            }
            if(item.onlySubButtonDisable)
            {
                item.newQty =undefined;
                item.qtyPrescribed=undefined;
            }
            if(item.barcodeItem == true)
            {
                item.newQty =undefined;
                item.qtyPrescribed=undefined;
            }
        }
        
        $scope.calculateReceivedQty1 = function (item) {
            if(item.quantity == undefined || item.quantity == null || item.quantity == ""){
                item.quantity = 0;
            }
            if(item.issuedQty == undefined || item.issuedQty == null || item.issuedQty == ""){
                item.issuedQty = 0;
            }
            if(item.NonFormularyAddedFlag == true){
            	if(item.selectedBatch.availableStockQuantity > item.quantity){
            		item.quantity1 = parseInt(item.quantity);
            		item.quantity = item.quantity1;
            	}
            	else{
            		item.quantity1 = item.selectedBatch.availableStockQuantity;
            		item.quantity = item.quantity1;
            	}
            }
            else{
            	item.quantity1 = parseInt(item.quantity);
            }
            if(item.uoms == undefined || item.uoms == null || item.uoms == ""){
                if(item.isThreeUOMApplicable == false){
                    if(item.uomListData == undefined || item.uomListData == null || item.uomListData == ""){
                    	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                    	if(item.selectedUom.uomTypeId == 1){
                            if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                                item.quantity1=item.quantity*(item.uom[1].conversion);
                            	item.quantity = Math.floor(item.quantity1 / item.uom[1].conversion);
                            	if(item.statusId == 18 || item.statusId == 4){
                            		item.receivedQtyCon = (item.issuedQty +"*"+ item.uom[1].conversion);
                                    item.receivedQtyCalculation = (item.issuedQty * item.uom[1].conversion);
                                    item.uomUnitData = item.uom[3].uomUnit;
	                            }
	                            else{
	                            	item.receivedQtyCon = (item.quantity +"*"+ item.uom[1].conversion);
	                                item.receivedQtyCalculation = (item.quantity * item.uom[1].conversion);
	                                item.uomUnitData = item.uom[3].uomUnit;
	                            }
                            }
                            else{
                                item.quantity1=item.quantity*(item.uom[0].conversion);
                            	item.quantity = Math.floor(item.quantity1 / item.uom[0].conversion);
                            	if(item.statusId == 18 || item.statusId == 4){
                            		item.receivedQtyCon = (item.issuedQty +"*"+ item.uom[0].conversion);
                                    item.receivedQtyCalculation = (item.issuedQty * item.uom[0].conversion);
                                    item.uomUnitData = item.uom[2].uomUnit;
                            	}
                            	else{
                            		item.receivedQtyCon = (item.quantity +"*"+ item.uom[0].conversion);
                                    item.receivedQtyCalculation = (item.quantity * item.uom[0].conversion);
                                    item.uomUnitData = item.uom[2].uomUnit;
                            	}
                            }
                        }
                        else if(item.selectedUom.uomTypeId == 2){
                        
                        }
                        else{
                            if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                            	if(item.statusId == 18 || item.statusId == 4){
                            		item.receivedQtyCon = (item.issuedQty);
                                    item.receivedQtyCalculation = (item.issuedQty);
                                    item.uomUnitData = item.uom[3].uomUnit;
                            	}
                            	else{
                            		item.receivedQtyCon = (item.quantity);
                                    item.receivedQtyCalculation = (item.quantity);
                                    item.uomUnitData = item.uom[3].uomUnit;
                            	}
                            }
                            else{
                            	if(item.statusId == 18 || item.statusId == 4){
                            		item.receivedQtyCon = (item.issuedQty);
                                    item.receivedQtyCalculation = (item.issuedQty);
                                    item.uomUnitData = item.uom[2].uomUnit;
                            	}
                            	else{
                            		item.receivedQtyCon = (item.quantity);
                                    item.receivedQtyCalculation = (item.quantity);
                                    item.uomUnitData = item.uom[2].uomUnit;
                            	}
                            }
                        }
                    }
                    }
                    else{
                    	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                        if(item.selectedUom.uomTypeId == 1){
                            item.quantity1=item.quantity*(item.uomListData[0].conversion);
                        	item.quantity = Math.floor(item.quantity1 / item.uomListData[0].conversion);
                        	if(item.statusId == 18 || item.statusId == 4){
                        		item.receivedQtyCon = (item.issuedQty +"*"+ item.uomListData[0].conversion);
                                item.receivedQtyCalculation = (item.issuedQty * item.uomListData[0].conversion);
                                item.uomUnitData = item.uomListData[2].uomUnit;
                        	}
                        	else{
                        		item.receivedQtyCon = (item.quantity +"*"+ item.uomListData[0].conversion);
                                item.receivedQtyCalculation = (item.quantity * item.uomListData[0].conversion);
                                item.uomUnitData = item.uomListData[2].uomUnit;
                        	}
                        }
                        else if(item.selectedUom.uomTypeId == 2){
                        
                        }
                        else{
                        	if(item.statusId == 18 || item.statusId == 4){
                        		item.receivedQtyCon = (item.issuedQty);
                                item.receivedQtyCalculation = (item.issuedQty);
                                item.uomUnitData = item.uomListData[2].uomUnit;
                        	}
                        	else{
                        		item.receivedQtyCon = (item.quantity);
                                item.receivedQtyCalculation = (item.quantity);
                                item.uomUnitData = item.uomListData[2].uomUnit;
                        	}
                        }
                    }
                    }
                }
                else{
                	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                    if(item.selectedUom.uomTypeId == 1){
                        if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                            item.quantity1=item.quantity*(item.uom[1].conversion * item.uom[2].conversion);
                        	item.quantity = Math.floor(item.quantity1 / (item.uom[1].conversion * item.uom[2].conversion));
                        	if(item.statusId == 18 || item.statusId == 4){
                        		item.receivedQtyCon = (item.issuedQty +"*"+ item.uom[1].conversion+"*"+ item.uom[2].conversion);
                                item.receivedQtyCalculation = (item.issuedQty * item.uom[1].conversion * item.uom[2].conversion);
                                item.uomUnitData = item.uom[3].uomUnit;
                        	}
                        	else{
                        		item.receivedQtyCon = (item.quantity +"*"+ item.uom[1].conversion+"*"+ item.uom[2].conversion);
                                item.receivedQtyCalculation = (item.quantity * item.uom[1].conversion * item.uom[2].conversion);
                                item.uomUnitData = item.uom[3].uomUnit;
                        	}
                        }
                        else{
                            item.quantity1=item.quantity*(item.uom[0].conversion * item.uom[1].conversion);
                        	item.quantity = Math.floor(item.quantity1 / (item.uom[0].conversion * item.uom[1].conversion));
                        	if(item.statusId == 18 || item.statusId == 4){
                        		item.receivedQtyCon = (item.issuedQty +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
                                item.receivedQtyCalculation = (item.issuedQty * item.uom[0].conversion * item.uom[1].conversion);
                                item.uomUnitData = item.uom[2].uomUnit;
                        	}
                        	else{
                        		item.receivedQtyCon = (item.quantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
                                item.receivedQtyCalculation = (item.quantity * item.uom[0].conversion * item.uom[1].conversion);
                                item.uomUnitData = item.uom[2].uomUnit;
                        	}
                        }
                    }
                    else if(item.selectedUom.uomTypeId == 2){
                        if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                            item.quantity1=item.quantity*(item.uom[2].conversion);
                        	item.quantity = Math.floor(item.quantity1 / (item.uom[2].conversion));
                        	if(item.statusId == 18 || item.statusId == 4){
                        		item.receivedQtyCon = (item.issuedQty +"*"+ item.uom[2].conversion);
                                item.receivedQtyCalculation = (item.issuedQty * item.uom[2].conversion);
                                item.uomUnitData = item.uom[3].uomUnit;
                        	}
                        	else{
                        		item.receivedQtyCon = (item.quantity +"*"+ item.uom[2].conversion);
                                item.receivedQtyCalculation = (item.quantity * item.uom[2].conversion);
                                item.uomUnitData = item.uom[3].uomUnit;
                        	}
                        }
                        else{
                            item.quantity1=item.quantity*(item.uom[1].conversion);
                        	item.quantity = Math.floor(item.quantity1 / (item.uom[1].conversion));
                        	if(item.statusId == 18 || item.statusId == 4){
                        		item.receivedQtyCon = (item.issuedQty +"*"+ item.uom[1].conversion);
                                item.receivedQtyCalculation = (item.issuedQty * item.uom[1].conversion);
                                item.uomUnitData = item.uom[2].uomUnit;
                        	}
                        	else{
                        		item.receivedQtyCon = (item.quantity +"*"+ item.uom[1].conversion);
                                item.receivedQtyCalculation = (item.quantity * item.uom[1].conversion);
                                item.uomUnitData = item.uom[2].uomUnit;
                        	}
                        }
                    }
                    else{
                        if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                        	if(item.statusId == 18 || item.statusId == 4){
                        		item.receivedQtyCon = (item.issuedQty);
                                item.receivedQtyCalculation = (item.issuedQty);
                                item.uomUnitData = item.uom[3].uomUnit;
                        	}
                        	else{
                        		item.receivedQtyCon = (item.quantity);
                                item.receivedQtyCalculation = (item.quantity);
                                item.uomUnitData = item.uom[3].uomUnit;
                        	}
                        }
                        else{
                        	if(item.statusId == 18 || item.statusId == 4){
                        		item.receivedQtyCon = (item.issuedQty);
                                item.receivedQtyCalculation = (item.issuedQty);
                                item.uomUnitData = item.uom[2].uomUnit;
                        	}
                        	else{
                        		item.receivedQtyCon = (item.quantity);
                                item.receivedQtyCalculation = (item.quantity);
                                item.uomUnitData = item.uom[2].uomUnit;
                        	}
                        }
                    }
                }
                }   
            }
            else{
                if(item.isThreeUOMApplicable == false){
                    if(item.uomListData == undefined || item.uomListData == null || item.uomListData == ""){
                    	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                    	if(item.selectedUom.uomTypeId == 1){
                            item.quantity1=item.quantity*(item.uoms[0].conversion);
                        	item.quantity = Math.floor(item.quantity1 / (item.uoms[0].conversion));
                        	if(item.statusId == 18 || item.statusId == 4){
                        		item.receivedQtyCon = (item.issuedQty +"*"+ item.uoms[0].conversion);
                                item.receivedQtyCalculation = (item.issuedQty * item.uoms[0].conversion);
                                item.uomUnitData = item.uoms[2].uomUnit;
                        	}
                        	else{
                        		item.receivedQtyCon = (item.quantity +"*"+ item.uoms[0].conversion);
                                item.receivedQtyCalculation = (item.quantity * item.uoms[0].conversion);
                                item.uomUnitData = item.uoms[2].uomUnit;
                        	}
                        }
                        else if(item.selectedUom.uomTypeId == 2){
                        
                        }
                        else{
                        	if(item.statusId == 18 || item.statusId == 4){
                        		item.receivedQtyCon = (item.issuedQty);
                                item.receivedQtyCalculation = (item.issuedQty);
                                item.uomUnitData = item.uoms[2].uomUnit;
                        	}
                        	else{
                        		item.receivedQtyCon = (item.quantity);
                                item.receivedQtyCalculation = (item.quantity);
                                item.uomUnitData = item.uoms[2].uomUnit;
                        	}
                        }
                    }
                    }
                    else{
                    	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                    	if(item.selectedUom.uomTypeId == 1){
                        	item.quantity1=item.quantity*(item.uomListData[0].conversion);
                        	item.quantity = Math.floor(item.quantity1 / (item.uomListData[0].conversion));
                        	if(item.statusId == 18 || item.statusId == 4){
                        		item.receivedQtyCon = (item.issuedQty +"*"+ item.uomListData[0].conversion);
                                item.receivedQtyCalculation = (item.issuedQty * item.uomListData[0].conversion);
                                item.uomUnitData = item.uomListData[2].uomUnit;
                        	}
                        	else{
                        		item.receivedQtyCon = (item.quantity +"*"+ item.uomListData[0].conversion);
                                item.receivedQtyCalculation = (item.quantity * item.uomListData[0].conversion);
                                item.uomUnitData = item.uomListData[2].uomUnit;
                        	}
                        }
                        else if(item.selectedUom.uomTypeId == 2){
                        
                        }
                        else{
                        	if(item.statusId == 18 || item.statusId == 4){
                        		item.receivedQtyCon = (item.issuedQty);
                                item.receivedQtyCalculation = (item.issuedQty);
                                item.uomUnitData = item.uomListData[2].uomUnit;
                        	}
                        	else{
                        		item.receivedQtyCon = (item.quantity);
                                item.receivedQtyCalculation = (item.quantity);
                                item.uomUnitData = item.uomListData[2].uomUnit;
                        	}
                        }
                    }
                    }
                }
                else{
                	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                    if(item.selectedUom.uomTypeId == 1){
                        if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                            item.quantity1=item.quantity*(item.uom[1].conversion * item.uom[2].conversion);
                        	item.quantity = Math.floor(item.quantity1 / (item.uom[1].conversion * item.uom[2].conversion));
                        	if(item.statusId == 18 || item.statusId == 4){
                        		item.receivedQtyCon = (item.issuedQty +"*"+ item.uom[1].conversion+"*"+ item.uom[2].conversion);
                                item.receivedQtyCalculation = (item.issuedQty * item.uom[1].conversion * item.uom[2].conversion);
                                item.uomUnitData = item.uom[3].uomUnit;
                        	}
                        	else{
                        		item.receivedQtyCon = (item.quantity +"*"+ item.uom[1].conversion+"*"+ item.uom[2].conversion);
                                item.receivedQtyCalculation = (item.quantity * item.uom[1].conversion * item.uom[2].conversion);
                                item.uomUnitData = item.uom[3].uomUnit;
                        	}
                        }
                        else{
                        	item.quantity1=item.quantity*(item.uom[0].conversion * item.uom[1].conversion);
                        	item.quantity = Math.floor(item.quantity1 / (item.uom[0].conversion * item.uom[1].conversion));
                        	if(item.statusId == 18 || item.statusId == 4){
                        		item.receivedQtyCon = (item.issuedQty +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
                                item.receivedQtyCalculation = (item.issuedQty * item.uom[0].conversion * item.uom[1].conversion);
                                item.uomUnitData = item.uom[2].uomUnit;
                        	}
                        	else{
                        		item.receivedQtyCon = (item.quantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
                                item.receivedQtyCalculation = (item.quantity * item.uom[0].conversion * item.uom[1].conversion);
                                item.uomUnitData = item.uom[2].uomUnit;
                        	}
                        }
                    }
                    else if(item.selectedUom.uomTypeId == 2){
                        if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                            item.quantity1=item.quantity*(item.uom[2].conversion);
                        	item.quantity = Math.floor(item.quantity1 / (item.uom[2].conversion));
                        	if(item.statusId == 18 || item.statusId == 4){
                        		item.receivedQtyCon = (item.issuedQty +"*"+ item.uom[2].conversion);
                                item.receivedQtyCalculation = (item.issuedQty * item.uom[2].conversion);
                                item.uomUnitData = item.uom[3].uomUnit;
                        	}
                        	else{
                        		item.receivedQtyCon = (item.quantity +"*"+ item.uom[2].conversion);
                                item.receivedQtyCalculation = (item.quantity * item.uom[2].conversion);
                                item.uomUnitData = item.uom[3].uomUnit;
                        	}
                        }
                        else{
                            item.quantity1=item.quantity*(item.uom[1].conversion);
                            if(item.statusId == 18 || item.statusId == 4){
                            	item.receivedQtyCon = (item.issuedQty +"*"+ item.uom[1].conversion);
                                item.receivedQtyCalculation = (item.issuedQty * item.uom[1].conversion);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                            else{
                            	item.receivedQtyCon = (item.quantity +"*"+ item.uom[1].conversion);
                                item.receivedQtyCalculation = (item.quantity * item.uom[1].conversion);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                        }
                    }
                    else{
                        if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                        	if(item.statusId == 18 || item.statusId == 4){
                        		item.receivedQtyCon = (item.issuedQty);
                                item.receivedQtyCalculation = (item.issuedQty);
                                item.uomUnitData = item.uom[3].uomUnit;
                        	}
                        	else{
                        		item.receivedQtyCon = (item.quantity);
                                item.receivedQtyCalculation = (item.quantity);
                                item.uomUnitData = item.uom[3].uomUnit;
                        	}
                        }
                        else{
                        	if(item.statusId == 18 || item.statusId == 4){
                        		item.receivedQtyCon = (item.issuedQty);
                                item.receivedQtyCalculation = (item.issuedQty);
                                item.uomUnitData = item.uom[2].uomUnit;
                        	}
                        	else{
                        		item.receivedQtyCon = (item.quantity);
                                item.receivedQtyCalculation = (item.quantity);
                                item.uomUnitData = item.uom[2].uomUnit;
                        	}
                        }
                    }
                }
                }
            }
        }
        
        $scope.calculateReceivedQtySub = function (item){
        	if(item.quantity == undefined || item.quantity == null || item.quantity == ""){
                item.quantity = 0;
            }
            if(item.uoms == undefined || item.uoms == null || item.uoms == ""){
                if(item.isThreeUOMApplicable == false){
                    if(item.uomListData == undefined || item.uomListData == null || item.uomListData == ""){
                    	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                    	if(item.selectedUom.uomTypeId == 1){
                            if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                            	item.receivedQtyCon = (item.quantity +"*"+ item.uom[1].conversion);
                                item.receivedQtyCalculation = (item.quantity * item.uom[1].conversion);
                                item.uomUnitData = item.uom[3].uomUnit;
                            }
                            else{
                                item.receivedQtyCon = (item.quantity +"*"+ item.uom[0].conversion);
                                item.receivedQtyCalculation = (item.quantity * item.uom[0].conversion);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                        }
                        else if(item.selectedUom.uomTypeId == 2){
                        
                        }
                        else{
                            if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                                item.receivedQtyCon = (item.quantity);
                                item.receivedQtyCalculation = (item.quantity);
                                item.uomUnitData = item.uom[3].uomUnit;
                            }
                            else{
                                item.receivedQtyCon = (item.quantity);
                                item.receivedQtyCalculation = (item.quantity);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                        }
                    }
                    }
                    else{
                    	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                        if(item.selectedUom.uomTypeId == 1){
                            item.receivedQtyCon = (item.quantity +"*"+ item.uomListData[0].conversion);
                            item.receivedQtyCalculation = (item.quantity * item.uomListData[0].conversion);
                            item.uomUnitData = item.uomListData[2].uomUnit;
                        }
                        else if(item.selectedUom.uomTypeId == 2){
                        
                        }
                        else{
                            item.receivedQtyCon = (item.quantity);
                            item.receivedQtyCalculation = (item.quantity);
                            item.uomUnitData = item.uomListData[2].uomUnit;
                        }
                    	}
                    }
                }
                else{
                	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                    if(item.selectedUom.uomTypeId == 1){
                        if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                            item.receivedQtyCon = (item.quantity +"*"+ item.uom[1].conversion+"*"+ item.uom[2].conversion);
                            item.receivedQtyCalculation = (item.quantity * item.uom[1].conversion * item.uom[2].conversion);
                            item.uomUnitData = item.uom[3].uomUnit;
                        }
                        else{
                            item.receivedQtyCon = (item.quantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
                            item.receivedQtyCalculation = (item.quantity * item.uom[0].conversion * item.uom[1].conversion);
                            item.uomUnitData = item.uom[2].uomUnit;
                        }
                    }
                    else if(item.selectedUom.uomTypeId == 2){
                        if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                            item.receivedQtyCon = (item.quantity +"*"+ item.uom[2].conversion);
                            item.receivedQtyCalculation = (item.quantity * item.uom[2].conversion);
                            item.uomUnitData = item.uom[3].uomUnit;
                        }
                        else{
                            item.receivedQtyCon = (item.quantity +"*"+ item.uom[1].conversion);
                            item.receivedQtyCalculation = (item.quantity * item.uom[1].conversion);
                            item.uomUnitData = item.uom[2].uomUnit;
                        }
                    }
                    else{
                        if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                            item.receivedQtyCon = (item.quantity);
                            item.receivedQtyCalculation = (item.quantity);
                            item.uomUnitData = item.uom[3].uomUnit;
                        }
                        else{
                            item.receivedQtyCon = (item.quantity);
                            item.receivedQtyCalculation = (item.quantity);
                            item.uomUnitData = item.uom[2].uomUnit;
                        }
                    }
                }
                }   
            }
            else{
                if(item.isThreeUOMApplicable == false){
                    if(item.uomListData == undefined || item.uomListData == null || item.uomListData == ""){
                    	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                        if(item.selectedUom.uomTypeId == 1){
                            item.receivedQtyCon = (item.quantity +"*"+ item.uoms[0].conversion);
                            item.receivedQtyCalculation = (item.quantity * item.uoms[0].conversion);
                            item.uomUnitData = item.uoms[2].uomUnit;
                        }
                        else if(item.selectedUom.uomTypeId == 2){
                        
                        }
                        else{
                            item.receivedQtyCon = (item.quantity);
                            item.receivedQtyCalculation = (item.quantity);
                            item.uomUnitData = item.uoms[2].uomUnit;
                        }
                    	}
                    }
                    else{
                    	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                        if(item.selectedUom.uomTypeId == 1){
                            item.receivedQtyCon = (item.quantity +"*"+ item.uomListData[0].conversion);
                            item.receivedQtyCalculation = (item.quantity * item.uomListData[0].conversion);
                            item.uomUnitData = item.uomListData[2].uomUnit;
                        }
                        else if(item.selectedUom.uomTypeId == 2){
                        
                        }
                        else{
                            item.receivedQtyCon = (item.quantity);
                            item.receivedQtyCalculation = (item.quantity);
                            item.uomUnitData = item.uomListData[2].uomUnit;
                        }
                    	}
                    }
                }
                else{
                	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                    if(item.selectedUom.uomTypeId == 1){
                        if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                            item.receivedQtyCon = (item.quantity +"*"+ item.uom[1].conversion+"*"+ item.uom[2].conversion);
                            item.receivedQtyCalculation = (item.quantity * item.uom[1].conversion * item.uom[2].conversion);
                            item.uomUnitData = item.uom[3].uomUnit;
                        }
                        else{
                            item.receivedQtyCon = (item.quantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
                            item.receivedQtyCalculation = (item.quantity * item.uom[0].conversion * item.uom[1].conversion);
                            item.uomUnitData = item.uom[2].uomUnit;
                        }
                    }
                    else if(item.selectedUom.uomTypeId == 2){
                        if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                            item.receivedQtyCon = (item.quantity +"*"+ item.uom[2].conversion);
                            item.receivedQtyCalculation = (item.quantity * item.uom[2].conversion);
                            item.uomUnitData = item.uom[3].uomUnit;
                        }
                        else{
                            item.receivedQtyCon = (item.quantity +"*"+ item.uom[1].conversion);
                            item.receivedQtyCalculation = (item.quantity * item.uom[1].conversion);
                            item.uomUnitData = item.uom[2].uomUnit;
                        }
                    }
                    else{
                        if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                            item.receivedQtyCon = (item.quantity);
                            item.receivedQtyCalculation = (item.quantity);
                            item.uomUnitData = item.uom[3].uomUnit;
                        }
                        else{
                            item.receivedQtyCon = (item.quantity);
                            item.receivedQtyCalculation = (item.quantity);
                            item.uomUnitData = item.uom[2].uomUnit;
                        }
                    }
                	}
                }
            }
        }

        $scope.calculateReceivedQty = function (item) {
        	if(self.model.worklistDetails.statusId == 4){
        		if(item.issuedQty == undefined || item.issuedQty == null || item.issuedQty == ""){
                    item.issuedQty = 0;
                }
                if(item.uoms == undefined || item.uoms == null || item.uoms == ""){
                    if(item.isThreeUOMApplicable == false){
                        if(item.uomListData == undefined || item.uomListData == null || item.uomListData == ""){
                        	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                        	if(item.selectedUom.uomTypeId == 1){
                                if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                                	item.receivedQtyCon = (item.issuedQty +"*"+ item.uom[1].conversion);
                                    item.receivedQtyCalculation = (item.issuedQty * item.uom[1].conversion);
                                    item.uomUnitData = item.uom[3].uomUnit;
                                }
                                else{
                                    item.receivedQtyCon = (item.issuedQty +"*"+ item.uom[0].conversion);
                                    item.receivedQtyCalculation = (item.issuedQty * item.uom[0].conversion);
                                    item.uomUnitData = item.uom[2].uomUnit;
                                }
                            }
                            else if(item.selectedUom.uomTypeId == 2){
                            
                            }
                            else{
                                if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                                    item.receivedQtyCon = (item.issuedQty);
                                    item.receivedQtyCalculation = (item.issuedQty);
                                    item.uomUnitData = item.uom[3].uomUnit;
                                }
                                else{
                                    item.receivedQtyCon = (item.issuedQty);
                                    item.receivedQtyCalculation = (item.issuedQty);
                                    item.uomUnitData = item.uom[2].uomUnit;
                                }
                            }
                        }
                        }
                        else{
                        	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                            if(item.selectedUom.uomTypeId == 1){
                                item.receivedQtyCon = (item.issuedQty +"*"+ item.uomListData[0].conversion);
                                item.receivedQtyCalculation = (item.issuedQty * item.uomListData[0].conversion);
                                item.uomUnitData = item.uomListData[2].uomUnit;
                            }
                            else if(item.selectedUom.uomTypeId == 2){
                            
                            }
                            else{
                                item.receivedQtyCon = (item.issuedQty);
                                item.receivedQtyCalculation = (item.issuedQty);
                                item.uomUnitData = item.uomListData[2].uomUnit;
                            }
                        	}
                        }
                    }
                    else{
                    	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                        if(item.selectedUom.uomTypeId == 1){
                            if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                                item.receivedQtyCon = (item.issuedQty +"*"+ item.uom[1].conversion+"*"+ item.uom[2].conversion);
                                item.receivedQtyCalculation = (item.issuedQty * item.uom[1].conversion * item.uom[2].conversion);
                                item.uomUnitData = item.uom[3].uomUnit;
                            }
                            else{
                                item.receivedQtyCon = (item.issuedQty +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
                                item.receivedQtyCalculation = (item.issuedQty * item.uom[0].conversion * item.uom[1].conversion);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                        }
                        else if(item.selectedUom.uomTypeId == 2){
                            if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                                item.receivedQtyCon = (item.issuedQty +"*"+ item.uom[2].conversion);
                                item.receivedQtyCalculation = (item.issuedQty * item.uom[2].conversion);
                                item.uomUnitData = item.uom[3].uomUnit;
                            }
                            else{
                                item.receivedQtyCon = (item.issuedQty +"*"+ item.uom[1].conversion);
                                item.receivedQtyCalculation = (item.issuedQty * item.uom[1].conversion);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                        }
                        else{
                            if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                                item.receivedQtyCon = (item.issuedQty);
                                item.receivedQtyCalculation = (item.issuedQty);
                                item.uomUnitData = item.uom[3].uomUnit;
                            }
                            else{
                                item.receivedQtyCon = (item.issuedQty);
                                item.receivedQtyCalculation = (item.issuedQty);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                        }
                    }
                    }   
                }
                else{
                    if(item.isThreeUOMApplicable == false){
                        if(item.uomListData == undefined || item.uomListData == null || item.uomListData == ""){
                        	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                            if(item.selectedUom.uomTypeId == 1){
                                item.receivedQtyCon = (item.issuedQty +"*"+ item.uoms[0].conversion);
                                item.receivedQtyCalculation = (item.issuedQty * item.uoms[0].conversion);
                                item.uomUnitData = item.uoms[2].uomUnit;
                            }
                            else if(item.selectedUom.uomTypeId == 2){
                            
                            }
                            else{
                                item.receivedQtyCon = (item.issuedQty);
                                item.receivedQtyCalculation = (item.issuedQty);
                                item.uomUnitData = item.uoms[2].uomUnit;
                            }
                        	}
                        }
                        else{
                        	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                            if(item.selectedUom.uomTypeId == 1){
                                item.receivedQtyCon = (item.issuedQty +"*"+ item.uomListData[0].conversion);
                                item.receivedQtyCalculation = (item.issuedQty * item.uomListData[0].conversion);
                                item.uomUnitData = item.uomListData[2].uomUnit;
                            }
                            else if(item.selectedUom.uomTypeId == 2){
                            
                            }
                            else{
                                item.receivedQtyCon = (item.issuedQty);
                                item.receivedQtyCalculation = (item.issuedQty);
                                item.uomUnitData = item.uomListData[2].uomUnit;
                            }
                        	}
                        }
                    }
                    else{
                    	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                        if(item.selectedUom.uomTypeId == 1){
                            if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                                item.receivedQtyCon = (item.issuedQty +"*"+ item.uom[1].conversion+"*"+ item.uom[2].conversion);
                                item.receivedQtyCalculation = (item.issuedQty * item.uom[1].conversion * item.uom[2].conversion);
                                item.uomUnitData = item.uom[3].uomUnit;
                            }
                            else{
                                item.receivedQtyCon = (item.issuedQty +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
                                item.receivedQtyCalculation = (item.issuedQty * item.uom[0].conversion * item.uom[1].conversion);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                        }
                        else if(item.selectedUom.uomTypeId == 2){
                            if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                                item.receivedQtyCon = (item.issuedQty +"*"+ item.uom[2].conversion);
                                item.receivedQtyCalculation = (item.issuedQty * item.uom[2].conversion);
                                item.uomUnitData = item.uom[3].uomUnit;
                            }
                            else{
                                item.receivedQtyCon = (item.issuedQty +"*"+ item.uom[1].conversion);
                                item.receivedQtyCalculation = (item.issuedQty * item.uom[1].conversion);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                        }
                        else{
                            if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                                item.receivedQtyCon = (item.issuedQty);
                                item.receivedQtyCalculation = (item.issuedQty);
                                item.uomUnitData = item.uom[3].uomUnit;
                            }
                            else{
                                item.receivedQtyCon = (item.issuedQty);
                                item.receivedQtyCalculation = (item.issuedQty);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                        }
                    }
                    }
                }
        	}
        	else{
        		if(item.quantity == undefined || item.quantity == null || item.quantity == ""){
                    item.quantity = 0;
                }
                item.quantity1 = item.quantity;
                if(item.uoms == undefined || item.uoms == null || item.uoms == ""){
                    if(item.isThreeUOMApplicable == false){
                        if(item.uomListData == undefined || item.uomListData == null || item.uomListData == ""){
                        	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                        	if(item.selectedUom.uomTypeId == 1){
                                if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                                	item.quantity = Math.floor(item.quantity1 / item.uom[1].conversion);
                                	item.receivedQtyCon = (item.quantity +"*"+ item.uom[1].conversion);
                                    item.receivedQtyCalculation = (item.quantity * item.uom[1].conversion);
                                    item.uomUnitData = item.uom[3].uomUnit;
                                }
                                else{
                                	item.quantity = Math.floor(item.quantity1 / item.uom[0].conversion);
                                    item.receivedQtyCon = (item.quantity +"*"+ item.uom[0].conversion);
                                    item.receivedQtyCalculation = (item.quantity * item.uom[0].conversion);
                                    item.uomUnitData = item.uom[2].uomUnit;
                                }
                            }
                            else if(item.selectedUom.uomTypeId == 2){
                            
                            }
                            else{
                                if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                                    item.receivedQtyCon = (item.quantity);
                                    item.receivedQtyCalculation = (item.quantity);
                                    item.uomUnitData = item.uom[3].uomUnit;
                                }
                                else{
                                    item.receivedQtyCon = (item.quantity);
                                    item.receivedQtyCalculation = (item.quantity);
                                    item.uomUnitData = item.uom[2].uomUnit;
                                }
                            }
                        }
                        }
                        else{
                        	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                            if(item.selectedUom.uomTypeId == 1){
                            	item.quantity = Math.floor(item.quantity1 / item.uomListData[0].conversion);
                                item.receivedQtyCon = (item.quantity +"*"+ item.uomListData[0].conversion);
                                item.receivedQtyCalculation = (item.quantity * item.uomListData[0].conversion);
                                item.uomUnitData = item.uomListData[2].uomUnit;
                            }
                            else if(item.selectedUom.uomTypeId == 2){
                            
                            }
                            else{
                                item.receivedQtyCon = (item.quantity);
                                item.receivedQtyCalculation = (item.quantity);
                                item.uomUnitData = item.uomListData[2].uomUnit;
                            }
                        	}
                        }
                    }
                    else{
                    	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                        if(item.selectedUom.uomTypeId == 1){
                            if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                            	item.quantity = Math.floor(item.quantity1 / (item.uom[1].conversion * item.uom[2].conversion));
                                item.receivedQtyCon = (item.quantity +"*"+ item.uom[1].conversion+"*"+ item.uom[2].conversion);
                                item.receivedQtyCalculation = (item.quantity * item.uom[1].conversion * item.uom[2].conversion);
                                item.uomUnitData = item.uom[3].uomUnit;
                            }
                            else{
                            	item.quantity = Math.floor(item.quantity1 / (item.uom[0].conversion * item.uom[1].conversion));
                                item.receivedQtyCon = (item.quantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
                                item.receivedQtyCalculation = (item.quantity * item.uom[0].conversion * item.uom[1].conversion);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                        }
                        else if(item.selectedUom.uomTypeId == 2){
                            if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                            	item.quantity = Math.floor(item.quantity1 / (item.uom[2].conversion));
                                item.receivedQtyCon = (item.quantity +"*"+ item.uom[2].conversion);
                                item.receivedQtyCalculation = (item.quantity * item.uom[2].conversion);
                                item.uomUnitData = item.uom[3].uomUnit;
                            }
                            else{
                            	item.quantity = Math.floor(item.quantity1 / (item.uom[1].conversion));
                                item.receivedQtyCon = (item.quantity +"*"+ item.uom[1].conversion);
                                item.receivedQtyCalculation = (item.quantity * item.uom[1].conversion);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                        }
                        else{
                            if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                                item.receivedQtyCon = (item.quantity);
                                item.receivedQtyCalculation = (item.quantity);
                                item.uomUnitData = item.uom[3].uomUnit;
                            }
                            else{
                                item.receivedQtyCon = (item.quantity);
                                item.receivedQtyCalculation = (item.quantity);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                        }
                    }
                    }   
                }
                else{
                    if(item.isThreeUOMApplicable == false){
                        if(item.uomListData == undefined || item.uomListData == null || item.uomListData == ""){
                        	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                            if(item.selectedUom.uomTypeId == 1){
                            	item.quantity = Math.floor(item.quantity1 / (item.uoms[0].conversion));
                                item.receivedQtyCon = (item.quantity +"*"+ item.uoms[0].conversion);
                                item.receivedQtyCalculation = (item.quantity * item.uoms[0].conversion);
                                item.uomUnitData = item.uoms[2].uomUnit;
                            }
                            else if(item.selectedUom.uomTypeId == 2){
                            
                            }
                            else{
                                item.receivedQtyCon = (item.quantity);
                                item.receivedQtyCalculation = (item.quantity);
                                item.uomUnitData = item.uoms[2].uomUnit;
                            }
                        	}
                        }
                        else{
                        	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                            if(item.selectedUom.uomTypeId == 1){
                            	item.quantity = Math.floor(item.quantity1 / (item.uomListData[0].conversion));
                                item.receivedQtyCon = (item.quantity +"*"+ item.uomListData[0].conversion);
                                item.receivedQtyCalculation = (item.quantity * item.uomListData[0].conversion);
                                item.uomUnitData = item.uomListData[2].uomUnit;
                            }
                            else if(item.selectedUom.uomTypeId == 2){
                            
                            }
                            else{
                                item.receivedQtyCon = (item.quantity);
                                item.receivedQtyCalculation = (item.quantity);
                                item.uomUnitData = item.uomListData[2].uomUnit;
                            }
                        	}
                        }
                    }
                    else{
                    	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                        if(item.selectedUom.uomTypeId == 1){
                            if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                            	item.quantity = Math.floor(item.quantity1 / (item.uom[1].conversion * item.uom[2].conversion));
                                item.receivedQtyCon = (item.quantity +"*"+ item.uom[1].conversion+"*"+ item.uom[2].conversion);
                                item.receivedQtyCalculation = (item.quantity * item.uom[1].conversion * item.uom[2].conversion);
                                item.uomUnitData = item.uom[3].uomUnit;
                            }
                            else{
                            	item.quantity = Math.floor(item.quantity1 / (item.uom[0].conversion * item.uom[1].conversion));
                                item.receivedQtyCon = (item.quantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
                                item.receivedQtyCalculation = (item.quantity * item.uom[0].conversion * item.uom[1].conversion);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                        }
                        else if(item.selectedUom.uomTypeId == 2){
                            if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                            	item.quantity = Math.floor(item.quantity1 / (item.uom[2].conversion));
                                item.receivedQtyCon = (item.quantity +"*"+ item.uom[2].conversion);
                                item.receivedQtyCalculation = (item.quantity * item.uom[2].conversion);
                                item.uomUnitData = item.uom[3].uomUnit;
                            }
                            else{
                                item.receivedQtyCon = (item.quantity +"*"+ item.uom[1].conversion);
                                item.receivedQtyCalculation = (item.quantity * item.uom[1].conversion);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                        }
                        else{
                            if(item.uom[0].conversion == undefined || item.uom[0].conversion == null || item.uom[0].conversion == ""){
                                item.receivedQtyCon = (item.quantity);
                                item.receivedQtyCalculation = (item.quantity);
                                item.uomUnitData = item.uom[3].uomUnit;
                            }
                            else{
                                item.receivedQtyCon = (item.quantity);
                                item.receivedQtyCalculation = (item.quantity);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                        }
                    }
                    }
                }
        	}
        }

        var UomChange = function (index, bill, uom, parentIndex) {
            var item = null;
            if (parentIndex == null) {
                item = bill.items[index];
            } else {
                item = bill.items[parentIndex].selectedItems[index];
            }

            if(item.selectedUom == undefined || item.selectedUom == null || item.selectedUom == ""){
                item.selectedUom = $scope.selectedUomData;
            }

            if (!!uom) {
            	if($scope.autoPrescribedQty != true){
            		item.quantity = 0;
            		//Caclulate amount including discount
                    Calculate(item, bill);

                    CalculateAmount(item);
//                    CalculateStock(item, item.uom, item.selectedUom);
                  //Convert Ordered Quantity
                    ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);
            	}
            	else{
            		//Convert Ordered Quantity
                    ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);
            		//Caclulate amount including discount
                    Calculate(item, bill);
                    CalculateAmount(item);
            	}
                if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                    $scope.isValidated = true;
                }
                else{
                    $scope.isValidated = false;
                }
                if($scope.activePackageList!=null&& $scope.activePackageList.length>0){
                    $scope.isValidated = true;
                }
            } else {
                item.rate = "";
            }
            if(item.quantity >= item.newQty)
    		{
            	item.quantityIssuedFlag=true;
    		}
            else
    		{
        		item.quantityIssuedFlag=false;
    		}
           // AllocateQuantity(index, bill, parentIndex);
        }
        var CalculateStock = function (item, uom, uomList) {
            if (!!uomList.uomTypeId) {
                if(item.selectedBatch.availableStockQuantity){
                    var stockAvailable = item.selectedBatch.availableStockQuantity;
                }
                else{
                    var stockAvailable = item.stock;
                }
      
              var uomDetails = uomList.uomDetails.split(' ')[0];
              if (uomDetails == "P") {
                item.newStock = parseInt(stockAvailable / (uom[1].conversion * uom[2].conversion * uom[3].conversion));
              } else if (uomDetails == "S") {
                item.newStock = parseInt(stockAvailable / (uom[2].conversion * uom[3].conversion));
              } else if (uomDetails == "D") {
                item.newStock = parseInt(stockAvailable / (uom[3].conversion));
              }
            } else {
              item.stock = 0;
              item.newStock = 0;
            }
          }
        var UomChangeOld = function (index, bill, uom, parentIndex) {
            var item = null;
            if (parentIndex == null) {
                item = bill.items[index];
            } else {
                item = bill.items[parentIndex].selectedItems[index];
            }

            if (!!uom) {
                var uomList = item.uom
                CalculateRate(item, uom, uomList);

            } else {
                item.rate = "";
            }
            // AllocateQuantity(index, bill, parentIndex);
            ApplyDiscount(self.model.worklistDetails);
        }

        var CalculateRate = function (item, uom, uomList) {

            // var sellingPrice = !!item.selectedBatch ? item.selectedBatch.sellingPrice : 0;

            // if (!!uom) {
            //     var pUom = $filter('filter')(item.selectedBatch.uom, {
            //         uomTypeId: 1
            //     }, true)[0];

            //     var sUom = $filter('filter')(item.selectedBatch.uom, {
            //         uomTypeId: 2
            //     }, true)[0];

            //     var dUom = $filter('filter')(item.selectedBatch.uom, {
            //         uomTypeId: 3
            //     }, true)[0];
            //     if ($scope.taxInclusive == true) {
            //         if (uom.uomTypeId == 1) {
            //             var a = pUom.conversion * sUom.conversion * dUom.conversion * sellingPrice;
            //             item.taxAmount = (((a) * (item.taxPercentage)) / (100 + (item.taxPercentage)));
            //             item.rate = (a - (item.taxAmount));
            //         } else if (uom.uomTypeId == 2) {
            //             var b = sUom.conversion * dUom.conversion * sellingPrice;
            //             item.taxAmount = (((b) * (item.taxPercentage)) / (100 + (item.taxPercentage)));
            //             item.rate = (b - (item.taxAmount));
            //         } else if (uom.uomTypeId == 3) {
            //             var c = dUom.conversion * sellingPrice;
            //             item.taxAmount = (((c) * (item.taxPercentage)) / (100 + (item.taxPercentage)));
            //             item.rate = (c - (item.taxAmount));
            //         }
            //     }
            //     else {
            //         if (uom.uomTypeId == 1) {
            //             item.rate = pUom.conversion * sUom.conversion * dUom.conversion * sellingPrice;
            //         } else if (uom.uomTypeId == 2) {
            //             item.rate = sUom.conversion * dUom.conversion * sellingPrice;
            //         } else if (uom.uomTypeId == 3) {
            //             item.rate = dUom.conversion * sellingPrice;
            //         }
            //     }
            // } else {
            //     item.rate = 0;
            // }

        }

        var OnKeyPressQuantity = function (item) {
            if (!item.quantity) {
                item.totalAmount = 0;
                item.itemDiscount = 0;
                item.taxAmount = 0;
                item.netAmount = 0;
            }
            // else{
            //     item.taxAmount = ((item.itemtotalAmount*item.taxPercentage)/100);
            // }
        }

        var SetDecimal = function () {
            if (isNaN(self.model.worklistDetails.discount) || !self.model.worklistDetails.discount) {
                self.model.worklistDetails.discount = 0;
            } else {
                self.model.worklistDetails.discount = parseFloat(self.model.worklistDetails.discount).toFixed(2);
            }
        }

        var CalculateQuantity = function (item, uom, uomList) {
            if (!!uom) {
                item.leaseQuantity = commonDetailService.calculateLeaseQuantity(item.selectedBatch.uom, item.selectedUom.uomTypeId, item.quantity);
            } else {
                item.leaseQuantity = 0;
            }
        }

        var ApplyDiscount = function (bill) {
            if(bill.discountType == '2' && bill.netAmountData == undefined){
                bill.netAmountData = bill.netAmount;
            }
            if(ApplyDiscountValidation(bill) && CheckQuantityForDiscount(bill)){
                var discount = parseFloat(bill.discount);
                $scope.focusInput=true;
                if (!isNaN(discount)) {
                    if (bill.discountType == '2') {
                    	//FOR RESET DISCOUNT
                    	var billAmount = 0;
                    	angular.forEach(bill.items, function (value, key) {
                            if(!value.itemDisabled){
                            if(!value.disabled){
                                var data = {
                                    "mrp": value.mrp,
                                    "isTaxInc": $scope.taxInclusive,
                                    // "markup": !!value.markUpDown ? value.markUpDownPer : null,
                                    // "markdown": !value.markUpDown ? value.markUpDownPer : null,
                                    "markup": value.markUpDown,
                                    "markUpDownPer" : value.markUpDownPer,
                                    "discount":  0,
                                    "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                                    "tax": value.taxPercentage,
                                    "qty": value.quantity,
                                    "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                    "discountPerItem":(discount*100)/bill.totalAmount,
                                    "patientClassPercentage": value.patientClassPercentage
                                }
                              var response = commonDetailService.calculateDisAmount(data);
                              value.rate = response.rate;
                              value.taxAmount = response.taxAmount;
                              value.responseTaxAmount =  response.taxAmount;
                              value.unitDiscountAmount = response.unitDiscountAmount;
                              value.itemDiscount =  response.discountAmount;
                              value.discountAmount = 0;
                              value.itemtotalAmount = response.totalAmount
                              value.totalAmount = response.totalAmount;
                              value.netAmount = response.netAmount;
                              value.selfPayable = response.netAmount;
                              value.discountPerItem= 0;
                              value.itemDiscount  =  0;
                              value.patientClassAmount = response.patientClassAmount;
                              value.creditPayable = response.creditPayable;
                              value.packageConsumptionAmt = response.packageConsumptionAmt;
                              
                              billAmount = billAmount+parseFloat(value.netAmount);
                              if(value.selectedItems){
                                        angular.forEach(value.selectedItems, function(svalue){
                                        	if(!svalue.disabled){
                                        		var data = {
                                                    "mrp": svalue.mrp,
                                                    "isTaxInc": $scope.taxInclusive,
                                                    // "markup": !!svalue.markUpDown ? svalue.markUpDownPer : null,
                                                    // "markdown": !svalue.markUpDown ? svalue.markUpDownPer : null,
                                                    "markup": svalue.markUpDown,
                                                    "markUpDownPer" : svalue.markUpDownPer,
                                                    "discount": 0,
                                                    "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                                                    "tax": svalue.taxPercentage,
                                                    "qty": svalue.quantity,
                                                    "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                                    "discountPerItem":(discount*100)/bill.totalAmount,
                                                    "patientClassPercentage": svalue.patientClassPercentage
                                                }
                                    
                                                var res = commonDetailService.calculateDisAmount(data);
//                                                (line item net amount / total net amount) * discount
                                                svalue.rate = res.rate;
                                                svalue.taxAmount = res.taxAmount;
                                                svalue.responseTaxAmount =  res.taxAmount;
                                                svalue.unitDiscountAmount = res.unitDiscountAmount;
                                                svalue.totalAmount = res.totalAmount;
                                                svalue.itemtotalAmount = res.totalAmount;
                                                svalue.netAmount = res.netAmount;
                                                svalue.itemDiscount =  res.discountAmount;
                                                svalue.discountPerItem = 0;
                                                svalue.discountAmount =   0;
                                                svalue.selfPayable = res.netAmount;
                                                svalue.itemDiscount  =  0;
                                                svalue.patientClassAmount = res.patientClassAmount;
                                                svalue.creditPayable = res.creditPayable;
                	                            svalue.packageConsumptionAmt = res.packageConsumptionAmt;
                	                            
                	                            billAmount = billAmount+parseFloat(svalue.netAmount);
                                        	}
                                        	
                                        });
                                    }
                             }else{

                                 angular.forEach(value.selectedItems, function (value, key) {
                                 	 if(!value.disabled){
                                     var data = {
                                         "mrp": value.mrp,
                                         "isTaxInc": $scope.taxInclusive,
                                        //  "markup": !!value.markUpDown ? value.markUpDownPer : null,
                                        //  "markdown": !value.markUpDown ? value.markUpDownPer : null,
                                        "markup": value.markUpDown,
                                        "markUpDownPer" : value.markUpDownPer,
                                         "discount":  0,
                                         "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                                         "tax": value.taxPercentage,
                                         "qty": value.quantity,
                                         "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                         "discountPerItem":(discount*100)/bill.totalAmount,
                                         "patientClassPercentage": value.patientClassPercentage
                                     }
                         
                                     var response = commonDetailService.calculateDisAmount(data);
                         
                                     value.rate = response.rate;
                                     value.taxAmount = response.taxAmount;
                                     value.responseTaxAmount =  response.taxAmount;
                                     value.unitDiscountAmount = response.unitDiscountAmount;
                                     value.itemDiscount =  0;
                                     value.itemtotalAmount = response.totalAmount
                                     value.totalAmount = response.totalAmount;
                                     value.netAmount = response.netAmount;
                                     value.discountPerItem =0;
                                     value.discountAmount = 0;
                                     value.selfPayable = response.netAmount;
                                     value.patientClassAmount = response.patientClassAmount;
                                     value.creditPayable = response.creditPayable;
 	                                value.packageConsumptionAmt = response.packageConsumptionAmt;
                                 }
                                 	 value.discountPerItem= ((discount / bill.totalAmount)*100).toFixed(2);
                                     value.itemDiscount  =  value.itemtotalAmount * value.discountPerItem / 100;
                                 });
                                
                                 
                                 billAmount = billAmount+parseFloat(value.netAmount);
                             }
                             if(bill.netAmountData == undefined){
                                bill.netAmountData = bill.netAmount;
                            }
                            bill.netAmount = billAmount;
                            }
                            });
                    	//END
                    	
                        if (discount <= bill.totalAmount) {
                            var itemDiscount = discount / bill.totalAmount;

                            angular.forEach(bill.items, function (value, key) {
                                if(!value.itemDisabled){
                                if (value.itemtotalAmount > itemDiscount) {
                                    value.itemDiscount = value.itemtotalAmount * itemDiscount;
                                } else {
                                    value.itemDiscount = 0;
                                }
                                if(!value.disabled){
                                    var data = {
                                        "mrp": value.mrp,
                                        "isTaxInc": $scope.taxInclusive,
                                        // "markup": !!value.markUpDown ? value.markUpDownPer : null,
                                        // "markdown": !value.markUpDown ? value.markUpDownPer : null,
                                        "markup": value.markUpDown,
                                        "markUpDownPer" : value.markUpDownPer,
                                        "discount": bill.discount ? bill.discount : 0,
                                        "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                                        "tax": value.taxPercentage,
                                        "qty": value.quantity,
                                        "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                        "discountPerItem":(discount*100)/bill.totalAmount,
                                        "patientClassPercentage": value.patientClassPercentage
                                    }
                        
                                    var response = commonDetailService.calculateDisAmount(data);
//                                    (line item net amount / total net amount) * discount
                                    value.rate = response.rate;
                                    value.taxAmount = response.taxAmount;
                                    value.responseTaxAmount =  response.taxAmount;
                                    value.unitDiscountAmount = response.unitDiscountAmount;
                                    value.itemDiscount =  response.discountAmount;
                                    value.discountPerItem=  response.discountPerItem;
                                    // value.discountAmount = value.itemtotalAmount * value.discountPerItem * 0.01;
                                    value.discountAmount = response.discountAmount;
                                    value.itemtotalAmount = response.totalAmount
                                    value.totalAmount = response.totalAmount;
                                    value.netAmount = response.netAmount;
                                    value.selfPayable = response.netAmount;
                                    // value.itemDiscount  =  value.itemtotalAmount * value.discountPerItem * 0.01;
                                    value.patientClassAmount = response.patientClassAmount;
                                    value.creditPayable = response.creditPayable;
    	                            value.packageConsumptionAmt = response.packageConsumptionAmt;
                                    if(value.selectedItems){
                                        angular.forEach(value.selectedItems, function(svalue){
                                        	 if(!svalue.disabled){
                                        	var data = {
                                                    "mrp": svalue.mrp,
                                                    "isTaxInc": $scope.taxInclusive,
                                                    // "markup": !!svalue.markUpDown ? svalue.markUpDownPer : null,
                                                    // "markdown": !svalue.markUpDown ? svalue.markUpDownPer : null,
                                                    "markup": svalue.markUpDown,
                                                    "markUpDownPer" : svalue.markUpDownPer,
                                                    "discount": bill.discount ? bill.discount : 0,
                                                    "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                                                    "tax": svalue.taxPercentage,
                                                    "qty": svalue.quantity,
                                                    "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                                    "discountPerItem":(discount*100)/bill.totalAmount,
                                                    "patientClassPercentage": svalue.patientClassPercentage
                                                }
                                    
                                                var res = commonDetailService.calculateDisAmount(data);
//                                                (line item net amount / total net amount) * discount
                                                svalue.rate = res.rate;
                                                svalue.taxAmount = res.taxAmount;
                                                svalue.responseTaxAmount =  res.taxAmount;
                                                svalue.unitDiscountAmount = res.unitDiscountAmount;
                                                svalue.totalAmount = res.totalAmount;
                                                svalue.itemtotalAmount = res.totalAmount;
                                                svalue.netAmount = res.netAmount;
                                                svalue.itemDiscount =  res.discountAmount;
                                                svalue.discountPerItem =  response.discountPerItem;
                                                svalue.discountAmount =   svalue.itemtotalAmount * svalue.discountPerItem * 0.01;
                                                svalue.selfPayable = res.netAmount;
                                                svalue.itemDiscount  =  svalue.itemtotalAmount * svalue.discountPerItem * 0.01;
                                                svalue.patientClassAmount = res.patientClassAmount;
                                                svalue.creditPayable = res.creditPayable;
                	                            svalue.packageConsumptionAmt = res.packageConsumptionAmt;
                                        }
                                        });
                                        value.discountPerItem= ((discount / bill.totalAmount)*100).toFixed(2);
                                        value.itemDiscount  =  value.itemtotalAmount * value.discountPerItem / 100;
                                    }
                                }
                                else{
                                    angular.forEach(value.selectedItems, function (value, key) { 
                                    if(!value.disabled){
                                        var data = {
                                            "mrp": value.mrp,
                                            "isTaxInc": $scope.taxInclusive,
                                            // "markup": !!value.markUpDown ? value.markUpDownPer : null,
                                            // "markdown": !value.markUpDown ? value.markUpDownPer : null,
                                            "markup": value.markUpDown,
                                            "markUpDownPer" : value.markUpDownPer,
                                            "discount": bill.discount ? bill.discount : 0,
                                            "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                                            "tax": value.taxPercentage,
                                            "qty": value.quantity,
                                            "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                            "discountPerItem":(discount*100)/bill.totalAmount,
                                            "patientClassPercentage": value.patientClassPercentage
                                        }
                            
                                        var response = commonDetailService.calculateDisAmount(data);
                            
                                        value.rate = response.rate;
                                        value.taxAmount = response.taxAmount;
                                        value.responseTaxAmount =  response.taxAmount;
                                        value.unitDiscountAmount = response.unitDiscountAmount;
//                                        value.discountAmount = response.discountAmount / bill.items.length;
                                        value.totalAmount = response.totalAmount;
                                        value.itemtotalAmount = response.totalAmount
                                        value.netAmount = response.netAmount;
                                        value.itemDiscount =  response.discountAmount;
                                        value.discountPerItem = ((discount / bill.totalAmount) * 100).toFixed(2);
                                        value.discountAmount = (value.itemtotalAmount)*value.discountPerItem* 0.01;
                                        value.selfPayable = response.netAmount;
                                        value.patientClassAmount = response.patientClassAmount;
                                        value.creditPayable = response.creditPayable;
    	                                value.packageConsumptionAmt = response.packageConsumptionAmt;
                                    }
                                    value.discountPerItem= ((discount / bill.totalAmount)*100).toFixed(2);
                                    value.itemDiscount  =  value.itemtotalAmount * value.discountPerItem / 100;
                                    });
                                }
                                
                                
                                self.model.isDiscountAppliedValid = false;
                                self.model.isDiscountApplied = true;
                                self.model.isAppliedDiscountValid = false; 
                                //var unitDiscountAmount = itemDiscount / value.quantity
                                 var unitDiscountAmount = value.itemDiscount / value.quantity;      //line 2037 added by ganesh
                                bill.items[key]['unitDiscountAmount'] = unitDiscountAmount;
                                CalculateAmount(value);
                                angular.forEach(value.selectedItems, function (subvalue, subkey) {
                                	if(!subvalue.disabled && !subvalue.itemDisabled){
                                    if (subvalue.itemtotalAmount > itemDiscount) {
                                        subvalue.itemDiscount = subvalue.itemtotalAmount * itemDiscount;
                                    } else {
                                        subvalue.itemDiscount = 0;
                                    }

                                    var unitDiscountAmount = subvalue.itemDiscount / subvalue.quantity;
                                    
                                    bill.items[key].selectedItems[subkey]['unitDiscountAmount'] = unitDiscountAmount;
                                	}
                                });
                            }
                            });
                        } else {
                            SetDiscountFlags();
                            // growl.error('Please enter discount amount less than Total Amount');
                        }
                        if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                            $scope.isValidated = true;
                        }
                        else{
                            $scope.isValidated = false;
                        }
                        if($scope.activePackageList!=null&& $scope.activePackageList.length>0){
                            $scope.isValidated = true;
                        }
                    } else if (bill.discountType == '1') {
                    	
                    	//FOR RESET DISCOUNT
                    	var billAmount = 0;
                    	angular.forEach(bill.items, function (value, key) {
                            if(!value.itemDisabled){
                            if(!value.disabled){
                                var data = {
                                    "mrp": value.mrp,
                                    "isTaxInc": $scope.taxInclusive,
                                    // "markup": !!value.markUpDown ? value.markUpDownPer : null,
                                    // "markdown": !value.markUpDown ? value.markUpDownPer : null,
                                    "markup": value.markUpDown,
                                    "markUpDownPer" : value.markUpDownPer,
                                    "discount":  0,
                                    "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                                    "tax": value.taxPercentage,
                                    "qty": value.quantity,
                                    "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                    "discountPerItem":self.model.worklistDetails.discount,
                                    "patientClassPercentage": value.patientClassPercentage
                                }
                              var response = commonDetailService.calculateDisAmount(data);
                              value.rate = response.rate;
                              value.taxAmount = response.taxAmount;
                              value.responseTaxAmount =  response.taxAmount;
                              value.unitDiscountAmount = response.unitDiscountAmount;
                              value.itemDiscount =  response.discountAmount;
                              value.discountAmount = 0;
                              value.itemtotalAmount = response.totalAmount
                              value.totalAmount = response.totalAmount;
                              value.netAmount = response.netAmount;
                              value.selfPayable = response.netAmount;
                              value.discountPerItem= 0;
                              value.itemDiscount  =  0;
                              value.patientClassAmount = response.patientClassAmount;
                              value.creditPayable = response.creditPayable;
                              value.packageConsumptionAmt = response.packageConsumptionAmt;
                              
                              billAmount = billAmount+parseFloat(value.netAmount);
                              if(value.selectedItems){
                                        angular.forEach(value.selectedItems, function(svalue){
                                        	if(!svalue.disabled){
                                        	var data = {
                                                    "mrp": svalue.mrp,
                                                    "isTaxInc": $scope.taxInclusive,
                                                    // "markup": !!svalue.markUpDown ? svalue.markUpDownPer : null,
                                                    // "markdown": !svalue.markUpDown ? svalue.markUpDownPer : null,
                                                    "markup": svalue.markUpDown,
                                                    "markUpDownPer" : svalue.markUpDownPer,
                                                    "discount": 0,
                                                    "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                                                    "tax": svalue.taxPercentage,
                                                    "qty": svalue.quantity,
                                                    "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                                    "discountPerItem":self.model.worklistDetails.discount,
                                                    "patientClassPercentage": svalue.patientClassPercentage
                                                }
                                    
                                                var res = commonDetailService.calculateDisAmount(data);
//                                                (line item net amount / total net amount) * discount
                                                svalue.rate = res.rate;
                                                svalue.taxAmount = res.taxAmount;
                                                svalue.responseTaxAmount =  res.taxAmount;
                                                svalue.unitDiscountAmount = res.unitDiscountAmount;
                                                svalue.totalAmount = res.totalAmount;
                                                svalue.itemtotalAmount = res.totalAmount;
                                                svalue.netAmount = res.netAmount;
                                                svalue.itemDiscount =  res.discountAmount;
                                                svalue.discountPerItem = 0;
                                                svalue.discountAmount =   0;
                                                svalue.selfPayable = res.netAmount;
                                                svalue.itemDiscount  =  0;
                                                svalue.patientClassAmount = res.patientClassAmount;
                                                svalue.creditPayable = res.creditPayable;
                	                            svalue.packageConsumptionAmt = res.packageConsumptionAmt;
                	                            
                	                            billAmount = billAmount+parseFloat(svalue.netAmount);
                                        }
                                        });
                                    }
                             }else{

                                 angular.forEach(value.selectedItems, function (value, key) { 
                                	 if(!value.disabled){
                                     var data = {
                                         "mrp": value.mrp,
                                         "isTaxInc": $scope.taxInclusive,
                                        //  "markup": !!value.markUpDown ? value.markUpDownPer : null,
                                        //  "markdown": !value.markUpDown ? value.markUpDownPer : null,
                                        "markup": value.markUpDown,
                                        "markUpDownPer" : value.markUpDownPer,
                                         "discount":  0,
                                         "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                                         "tax": value.taxPercentage,
                                         "qty": value.quantity,
                                         "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                         "discountPerItem":self.model.worklistDetails.discount,
                                         "patientClassPercentage": value.patientClassPercentage
                                     }
                         
                                     var response = commonDetailService.calculateDisAmount(data);
                         
                                     value.rate = response.rate;
                                     value.taxAmount = response.taxAmount;
                                     value.responseTaxAmount =  response.taxAmount;
                                     value.unitDiscountAmount = response.unitDiscountAmount;
                                     value.itemDiscount =  0;
                                     value.itemtotalAmount = response.totalAmount
                                     value.totalAmount = response.totalAmount;
                                     value.netAmount = response.netAmount;
                                     value.discountPerItem =0;
                                     value.discountAmount = 0;
                                     value.selfPayable = response.netAmount;
                                     value.patientClassAmount = response.patientClassAmount;
                                     value.creditPayable = response.creditPayable;
 	                                value.packageConsumptionAmt = response.packageConsumptionAmt;
 	                               value.discountPerItem= self.model.worklistDetails.discount;
                                   value.itemDiscount  =  value.itemtotalAmount * value.discountPerItem / 100;
                                 }
                                 });
                                 billAmount = billAmount+parseFloat(value.netAmount);
                             }
                            bill.netAmount = billAmount;
                        }
                            });
                    	//END
                    	
                    	
                    	
                        if (self.model.worklistDetails.discount <= discountLimit) {
                            angular.forEach(bill.items, function (value, key) {
                                if(!value.itemDisabled){
                                value.itemDiscount = value.itemtotalAmount * discount * 0.01;

                                var unitDiscountAmount = value.itemDiscount / value.quantity;
                                bill.items[key]['unitDiscountAmount'] = unitDiscountAmount;
                                if(!value.disabled){
                                    var data = {
                                        "mrp": value.mrp,
                                        "isTaxInc": $scope.taxInclusive,
                                        // "markup": !!value.markUpDown ? value.markUpDownPer : null,
                                        // "markdown": !value.markUpDown ? value.markUpDownPer : null,
                                        "markup": value.markUpDown,
                                        "markUpDownPer" : value.markUpDownPer,
                                        "discount": bill.discount ? bill.discount : 0,
                                        "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                                        "tax": value.taxPercentage,
                                        "qty": value.quantity,
                                        "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                        "discountPerItem":self.model.worklistDetails.discount,
                                        "patientClassPercentage": value.patientClassPercentage
                                    }
                        
                                    var response = commonDetailService.calculateDisAmount(data);
                        
                                    value.rate = response.rate;
                                    value.taxAmount = response.taxAmount;
                                    value.responseTaxAmount =  response.taxAmount;
                                    value.unitDiscountAmount = response.unitDiscountAmount;
                                    value.itemDiscount =  response.discountAmount;
                                    value.itemtotalAmount = response.totalAmount
                                    value.totalAmount = response.totalAmount;
                                    value.netAmount = response.netAmount;
                                    value.selfPayable = response.netAmount;
                                   // var discountPer = (discount / bill.totalAmount)*100
                                   value.discountPerItem = discount;
                                   value.discountAmount = (value.totalAmount/100)*value.discountPerItem;
                                   value.itemDiscount  =  (value.itemtotalAmount *  discount) / 100;
                                   value.patientClassAmount = response.patientClassAmount;
                                   if(value.selectedItems){
                                   	
                                       angular.forEach(value.selectedItems, function(svalue){
                                   if(!svalue.disabled){  
                                       	 var data = {
                                        "mrp": svalue.mrp,
                                        "isTaxInc": $scope.taxInclusive,
                                        // "markup": !!svalue.markUpDown ? svalue.markUpDownPer : null,
                                        // "markdown": !svalue.markUpDown ? svalue.markUpDownPer : null,
                                        "markup": svalue.markUpDown,
                                        "markUpDownPer" : svalue.markUpDownPer,
                                        "discount": bill.discount ? bill.discount : 0,
                                        "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                                        "tax": svalue.taxPercentage,
                                        "qty": svalue.quantity,
                                        "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                        "discountPerItem":self.model.worklistDetails.discount,
                                        "patientClassPercentage": svalue.patientClassPercentage
                                    }
                                       	 var response = commonDetailService.calculateDisAmount(data);
                        
                                    svalue.rate = response.rate;
                                    svalue.taxAmount = response.taxAmount;
                                    svalue.responseTaxAmount =  response.taxAmount;
                                    svalue.unitDiscountAmount = response.unitDiscountAmount;
                                    svalue.itemDiscount =  response.discountAmount;
                                    svalue.itemtotalAmount = response.totalAmount
                                    svalue.totalAmount = response.totalAmount;
                                    svalue.netAmount = response.netAmount;
                                    svalue.selfPayable = response.netAmount;
                                    svalue.discountPerItem = discount;
                                    svalue.discountAmount = (svalue.totalAmount/100)*svalue.discountPerItem;
                                    svalue.itemDiscount  =  (svalue.itemtotalAmount *  discount) / 100;
                                   svalue.patientClassAmount = response.patientClassAmount;
                                       }
                                       });
                                   }
                                }
                                else{
                                    angular.forEach(value.selectedItems, function (svalue) { 
                                    	if(!svalue.disabled){
                                    	var data = {
                                                "mrp": svalue.mrp,
                                                "isTaxInc": $scope.taxInclusive,
                                                // "markup": !!svalue.markUpDown ? svalue.markUpDownPer : null,
                                                // "markdown": !svalue.markUpDown ? svalue.markUpDownPer : null,
                                                "markup": svalue.markUpDown,
                                                "markUpDownPer" : svalue.markUpDownPer,
                                                "discount": bill.discount ? bill.discount : 0,
                                                "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                                                "tax": svalue.taxPercentage,
                                                "qty": svalue.quantity,
                                                "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                                "discountPerItem":self.model.worklistDetails.discount,
                                                "patientClassPercentage": svalue.patientClassPercentage
                                        }
                            
                                        var response = commonDetailService.calculateDisAmount(data);
                            
                                        svalue.rate = response.rate;
                                        svalue.taxAmount = response.taxAmount;
                                        svalue.responseTaxAmount =  response.taxAmount;
                                        svalue.unitDiscountAmount = response.unitDiscountAmount;
                                        svalue.itemDiscount =  response.discountAmount;
                                        svalue.itemtotalAmount = response.totalAmount
                                        svalue.totalAmount = response.totalAmount;
                                        svalue.netAmount = response.netAmount;
                                        svalue.selfPayable = response.netAmount;
                                        svalue.discountPerItem = discount;
                                        svalue.discountAmount = (svalue.totalAmount/100)*svalue.discountPerItem;
                                        svalue.itemDiscount  =  (svalue.itemtotalAmount *  discount) / 100;
                                       svalue.patientClassAmount = response.patientClassAmount;
                                    }
                                    });
                                   
                                }
                             
                                angular.forEach(value.selectedItems, function (subvalue, subkey) {
                                	if(!subvalue.disabled && !subvalue.itemDisabled){
                                    subvalue.itemDiscount = subvalue.itemtotalAmount * subvalue.discountPerItem / 100;

                                    var unitDiscountAmount = subvalue.itemDiscount / subvalue.quantity;
                                    bill.items[key].selectedItems[subkey]['unitDiscountAmount'] = unitDiscountAmount;
                                  //  self.model.itemsAfterCalculation.itemtotalAmount = self.model.itemsAfterCalculation.itemtotalAmount - unitDiscountAmount;
                                	}
                                	});
                                }
                            });
                            self.model.isDiscountAppliedValid = false;
                            self.model.isDiscountApplied = true;
                            self.model.isAppliedDiscountValid = false
                        } else {
                            SetDiscountFlags();
                            growl.error('Please enter discount amount less than ' + discountLimit + '%');
                        }
                        if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                            $scope.isValidated = true;
                        }
                        else{
                            $scope.isValidated = false;
                        }
                        if($scope.activePackageList!=null&& $scope.activePackageList.length>0){
                            $scope.isValidated = true;
                        }
                    }
                }
            }
            }
        var SetDiscountFlags = function () {
            self.model.isDiscountAppliedValid = true;
            self.model.isDiscountApplied = false;
            self.model.isAppliedDiscountValid = true
        }

        var CheckQuantityForDiscount = function (bill) {
            var isValid = true;
            bill.items.some(function (item) {
                if($scope.taxInclusive == true){
                    if ((parseFloat(bill.discount) > bill.netAmountData) && (parseFloat(bill.discount) > bill.netAmount) && (bill.discountType == '2')) {
                        growl.error('Please enter discount amount less than Total Amount');
                        isValid = false;
                        SetDiscountFlags();
                    }
                }
                else{
                    if ((parseFloat(bill.discount) > bill.totalAmount) && (bill.discountType == '2')) {
                        growl.error('Please enter discount amount less than Total Amount');
                        isValid = false;
                        SetDiscountFlags();
                    }
                }
            })
            return isValid
        }

        var ApplyDiscountValidation = function (bill) {
          //  if (CheckQuantityForDiscount(bill)) {
            var isValid = true;
                self.model.discountTypeFlag = false 
                if(self.model.worklistDetails.discountCatgeory.discountCategory == "Select Discount Category"){
                if(!self.model.worklistDetails.discountCatgeory.id){
                    growl.error('Please select Discount Catgeory');
                    isValid = false;
                  }else{
                if (!self.model.worklistDetails.discount) {
                    if (bill.discountType == '1') {
                        growl.error('Please enter % discount');
                    }
                    if (bill.discountType == '2') {
                        growl.error('Please enter discount amount');
                    }
                    SetDiscountFlags();
                }
            }
            }
            return isValid
        }

        var ValidateMaxOrderQuantity = function (item, uom, uomList) {
            var isValid = true;
            var totalQuantitys = 0; 
            // var remainingQuantity = 0;

            item.isParentQuantityValid = false;
            var totalLeaseQuantity = 0;

            var list = self.model.worklistDetails.items.filter(x=> x.itemId == item.itemId)
            if(list.length > 0){
                for(let lt of list){
                    if(lt.quantity) {
                        totalQuantitys = totalQuantitys + Number(lt.quantity)
                    }
                }
                // remainingQuantity = item.qtyPrescribed - item.newDispQty;
               /* if(Number(totalQuantitys) > item.qtyPrescribed){
                    item.isQuantityValid = true;
                    growl.error("Enter quantity is more than order quantity");
                    isValid = false;
                    item.quantity = 0;
                } else {
                    item.isQuantityValid = false;
                }*/
                item.isQuantityValid = false;
            }

            //  CalculateQuantity(item, uom, uomList);

            var items = self.model.worklistDetails.items.filter(function (detail) {
                return detail.itemId == item.itemId;
            });

            items.forEach(function (detail) {
                totalLeaseQuantity = totalLeaseQuantity + detail.leaseQuantity;
            });

            if (!item.newDispQty) {
                if(!item.qtyPrescribed){
                    isValid = true;
                }
                else{
                   /* if(item.quantity > item.newQty){
                        item.isParentQuantityValid = true;
                        isValid = false;
                        item.quantity = 0;
                        growl.error("Entered quantity is greater than ordered quantity")
                    }*/
                }
            } else {
               /* if(item.quantity > item.newQty - item.newDispQty){
                    item.isParentQuantityValid = true;
                    isValid = false;
                    item.quantity = 0;
                    growl.error("Entered quantity is greater than ordered quantity")
                }*/
            }

            return isValid;
        }

        var CalculateAmount = function (item) {
        		if ($scope.taxInclusive == true) {
                	item.itemtotalAmount = ((item.rate != undefined ?(item.rate.toFixed(2) ? item.rate.toFixed(2) : 0):0) * (parseInt(item.quantity) ? parseInt(item.quantity) : item.issuedQty ? item.issuedQty :0));
//                	item.taxAmount = (((item.itemtotalAmount) - (item.patientClassAmount) - (item.itemDiscount)) * (item.taxPercentage / 100)).toFixed(2);
                	
                	if(item.packageConsumptionAmt == 0 || isNaN(item.packageConsumptionAmt)){
                		item.netAmount = ((((item.itemtotalAmount!=undefined && item.itemtotalAmount!=null && item.itemtotalAmount!=0) ? item.itemtotalAmount : item.totalAmount) - (item.patientClassAmount) - (item.itemDiscount)));
                	}else{
                		item.netAmount = ((((item.itemtotalAmount!=undefined && item.itemtotalAmount!=null && item.itemtotalAmount!=0) ? item.itemtotalAmount : item.totalAmount) - (item.patientClassAmount) - (item.itemDiscount)));
                		if(item.netAmount > 0){
                		item.netAmount = item.netAmount - item.packageConsumptionAmt;
                		}
                	}
                    
                    item.selfPayable = (isNaN(item.selfPayable)?0:item.selfPayable);
                    item.creditPayable = (isNaN(item.creditPayable)?0:item.creditPayable);
                    item.packageConsumptionAmt = (isNaN(item.packageConsumptionAmt)?0:item.packageConsumptionAmt);
                }
                else {
                    item.itemtotalAmount = ((item.rate ? item.rate : 0) * (item.quantity ? item.quantity : item.issuedQty ? item.issuedQty :0));
                    item.taxAmount = (((item.itemtotalAmount) - (item.patientClassAmount) - (item.itemDiscount)) * (item.taxPercentage / 100)).toFixed(2);
                    item.netAmount = (isNaN(item.netAmount)?0:item.netAmount);
                    item.selfPayable = (isNaN(item.selfPayable)?0:item.selfPayable);
                    item.creditPayable = (isNaN(item.creditPayable)?0:item.creditPayable);
                    item.packageConsumptionAmt = (isNaN(item.packageConsumptionAmt)?0:item.packageConsumptionAmt);
                }
                if(!item.isConsumed)
           	    {
                	if(item.creditPayable==0)
                		{
                		if(item.netAmount==0)
                			{
                			item.selfPayable = item.selfPayable;
                			}
                		else
                		{
                		item.selfPayable = item.netAmount-(item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null ? item.packageConsumptionAmt:0);
                		}
                		}
                	else
                		{
                		item.selfPayable = item.selfPayable;
                		}
           	     
           	    }
        }
        
        //Ganesh
        var CalculateItemSum = function (itemList) 
        {
       	 $scope.totalSelfPayable = parseFloat(0);
    	 $scope.totalCreditPayable = parseFloat(0);
    	 angular.forEach(itemList, function (item, key) 
    {
        if ($scope.taxInclusive == true) 
        {
        	if(!item.itemDisabled && !item.disabled){
          item.itemtotalAmount = ((item.rate.toFixed(2) ? item.rate.toFixed(2) : 0) * (item.quantity ? item.quantity : 0));
//          if(item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null && item.packageConsumptionAmt>0)
//          {
//           item.taxAmount = (((item.itemtotalAmount) - (item.patientClassAmount) - (isNaN(item.itemDiscount) ? 0 : item.itemDiscount)-(!isNaN(item.packageConsumptionAmt) && item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null ? item.packageConsumptionAmt:0)) * (item.taxPercentage / 100));
//          }
        //  item.netAmount = (((item.itemtotalAmount) - (item.itemDiscount)));
          item.netAmount = item.netAmount;
          item.selfPayable = (isNaN(item.selfPayable)?0:item.selfPayable);
          item.creditPayable = (isNaN(item.creditPayable)?0:item.creditPayable);
          item.packageConsumptionAmt = (isNaN(item.packageConsumptionAmt)?0:item.packageConsumptionAmt);
          
          $scope.totalSelfPayable = $scope.totalSelfPayable+ item.selfPayable;
          $scope.totalCreditPayable = $scope.totalCreditPayable+ item.creditPayable;
          
          if (item.selectedItems && !item.itemDisabled && !item.disabled) {
              angular.forEach(item.selectedItems, function (sitem, key) 
            	{
            	  sitem.itemtotalAmount = ((sitem.rate.toFixed(2) ? sitem.rate.toFixed(2) : 0) * (sitem.quantity ? sitem.quantity : 0));
//            	  if(item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null && item.packageConsumptionAmt>0)
//                  {
//                   item.taxAmount = (((item.itemtotalAmount) - (item.patientClassAmount) - (isNaN(item.itemDiscount) ? 0 : item.itemDiscount)-(!isNaN(item.packageConsumptionAmt) && item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null ? item.packageConsumptionAmt:0)) * (item.taxPercentage / 100));
//                  }
            	  sitem.netAmount = sitem.netAmount;
            	  sitem.selfPayable = (isNaN(sitem.selfPayable)?0:sitem.selfPayable);
            	  sitem.creditPayable = (isNaN(sitem.creditPayable)?0:sitem.creditPayable);
            	  sitem.packageConsumptionAmt = (isNaN(sitem.packageConsumptionAmt)?0:sitem.packageConsumptionAmt);
                  $scope.totalSelfPayable = $scope.totalSelfPayable+ sitem.selfPayable;
                  $scope.totalCreditPayable = $scope.totalCreditPayable+ sitem.creditPayable;
              });
          }
        }else if(item.selectedItems && !item.disabled){
       	 
       	 angular.forEach(item.selectedItems, function (sitem, key) 
           	{
       		 if(!sitem.disabled){
       			sitem.itemtotalAmount = ((sitem.rate.toFixed(2) ? sitem.rate.toFixed(2) : 0) * (sitem.quantity ? sitem.quantity : 0));
//       			if(item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null && item.packageConsumptionAmt>0)
//                {
//                 item.taxAmount = (((item.itemtotalAmount) - (item.patientClassAmount) - (isNaN(item.itemDiscount) ? 0 : item.itemDiscount)-(!isNaN(item.packageConsumptionAmt) && item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null ? item.packageConsumptionAmt:0)) * (item.taxPercentage / 100));
//                }
          	  sitem.netAmount = sitem.netAmount;
          	  sitem.selfPayable = (isNaN(sitem.selfPayable)?0:sitem.selfPayable);
          	  sitem.creditPayable = (isNaN(sitem.creditPayable)?0:sitem.creditPayable);
          	  sitem.packageConsumptionAmt = (isNaN(sitem.packageConsumptionAmt)?0:sitem.packageConsumptionAmt);
                $scope.totalSelfPayable = $scope.totalSelfPayable+ sitem.selfPayable;
                $scope.totalCreditPayable = $scope.totalCreditPayable+ sitem.creditPayable;
       		 }
           	 
             });
        }
        	
        }
        else 
         {
        	if(!item.itemDisabled && !item.disabled){
        		  item.patientClassAmount = item.patientClassAmount!=undefined?	item.patientClassAmount :0;	
              item.itemtotalAmount = ((item.rate.toFixed(2) ? item.rate.toFixed(2) : 0) * (item.quantity ? item.quantity : 0));
//              if(item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null && item.packageConsumptionAmt>0)
//              {
//               item.taxAmount = (((item.itemtotalAmount+(parseFloat(item.taxAmount))) - (item.patientClassAmount) - (isNaN(item.itemDiscount) ? 0 : item.itemDiscount)-(!isNaN(item.packageConsumptionAmt) && item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null ? item.packageConsumptionAmt:0)) * (item.taxPercentage / 100));
//              }
              //item.netAmount = (((item.itemtotalAmount) - (item.itemDiscount) + item.taxAmount));
              item.netAmount = item.netAmount;
              item.selfPayable = (isNaN(item.selfPayable)?0:item.selfPayable);
              item.creditPayable = (isNaN(item.creditPayable)?0:item.creditPayable);
              $scope.totalSelfPayable = $scope.totalSelfPayable+ item.selfPayable;
              $scope.totalCreditPayable = $scope.totalCreditPayable+ item.creditPayable;
              
              if (item.selectedItems && !item.itemDisabled && !item.disabled) {
                  angular.forEach(item.selectedItems, function (sitem, key) 
                		  {
                		  	if(!sitem.disabled){
                		  		sitem.patientClassAmount = sitem.patientClassAmount!=undefined?	sitem.patientClassAmount :0;	
                	  sitem.itemtotalAmount = ((sitem.rate.toFixed(2) ? sitem.rate.toFixed(2) : 0) * (sitem.quantity ? sitem.quantity : 0));
//                	  if(item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null && item.packageConsumptionAmt>0)
//                      {
//                       item.taxAmount = (((item.itemtotalAmount+(parseFloat(item.taxAmount))) - (item.patientClassAmount) - (isNaN(item.itemDiscount) ? 0 : item.itemDiscount)-(!isNaN(item.packageConsumptionAmt) && item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null ? item.packageConsumptionAmt:0)) * (item.taxPercentage / 100));
//                      }
                	  sitem.netAmount = sitem.netAmount;
                	  sitem.selfPayable = (isNaN(sitem.selfPayable)?0:sitem.selfPayable);
                	  sitem.creditPayable = (isNaN(sitem.creditPayable)?0:sitem.creditPayable);
                      $scope.totalSelfPayable = $scope.totalSelfPayable+ sitem.selfPayable;
                      $scope.totalCreditPayable = $scope.totalCreditPayable+ sitem.creditPayable;
                		  	}
                  });
              }
             }else if(item.selectedItems && !item.disabled){
            	 
            	 angular.forEach(item.selectedItems, function (sitem, key) 
                	{
            		 if(!sitem.disabled){
            		 	 sitem.itemtotalAmount = ((sitem.rate.toFixed(2) ? sitem.rate.toFixed(2) : 0) * (sitem.quantity ? sitem.quantity : 0));
//            		 	if(item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null && item.packageConsumptionAmt>0)
//                        {
//                         item.taxAmount = (((item.itemtotalAmount+(parseFloat(item.taxAmount))) - (item.patientClassAmount) - (isNaN(item.itemDiscount) ? 0 : item.itemDiscount)-(!isNaN(item.packageConsumptionAmt) && item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null ? item.packageConsumptionAmt:0)) * (item.taxPercentage / 100));
//                        }
                	  sitem.netAmount = sitem.netAmount;
                	  sitem.selfPayable = (isNaN(sitem.selfPayable)?0:sitem.selfPayable);
                	  sitem.creditPayable = (isNaN(sitem.creditPayable)?0:sitem.creditPayable);
                      $scope.totalSelfPayable = $scope.totalSelfPayable+ sitem.selfPayable;
                      $scope.totalCreditPayable = $scope.totalCreditPayable+ sitem.creditPayable;
            		 }
                	 
                  });
             }
         }
        
    	 });
    	
    	 $scope.totalCreditPayable = parseFloat($scope.totalCreditPayable).toFixed(2);
    	 $scope.totalSelfPayable = parseFloat($scope.totalSelfPayable).toFixed(2);}
        

        var ValidateUniqueBatchAndUom = function (item) {
            var isValid = true;

            if (item.substituteBtn == true) {
                item['isBatchValid'] = false;
                item['isUOMValid'] = false;

                var parentItem = self.model.worklistDetails.items.filter(function (parentItem) {
                    return parentItem.genericId == item.genericId;
                })

               /* var items = parentItem[0].selectedItems.filter(function (detail) {
                    return detail.itemId == item.itemId && item.selectedBatch && item.selectedUom && item.selectedUom.uomTypeId > 0 && item.selectedBatch.batchId > 0 && detail.selectedBatch.batchId == item.selectedBatch.batchId && detail.selectedUom.uomTypeId == item.selectedUom.uomTypeId;
                });*/

                 var items1 = parentItem.filter(function (detail1) {
                	if(detail1.selectedItems != undefined){
                	self.model.test = [];
                	self.model.test = angular.copy(detail1.selectedItems);
              /*  	detail1.selectedItems.filter(function (detail) {
                	
                    return detail.itemId == 
                    	item.itemId && item.selectedBatch && 
                    	item.selectedUom && 
                    	item.selectedUom.uomTypeId > 0 &&
                    	item.selectedBatch.batchId > 0 && detail.selectedBatch.batchId == 
                    		item.selectedBatch.batchId && detail.selectedUom.uomTypeId == item.selectedUom.uomTypeId;
                	 });*/
                	}
                });
                var items = self.model.test.filter(function (detail) {
                	  return detail.itemId == 
                      	item.itemId && item.selectedBatch && 
                      	item.selectedUom && 
                      	item.selectedUom.uomTypeId > 0 &&
                      	item.selectedBatch.batchId > 0 && detail.selectedBatch.batchId == 
                      		item.selectedBatch.batchId && detail.selectedUom.uomTypeId == item.selectedUom.uomTypeId && detail.itemDisabled!= true ;
                });

                if (items.length > 1) {
                    isValid = false;
                    item['isBatchValid'] = true;
                    item['isUOMValid'] = true;
                    growl.error('There should not be same batch and same UOM for same item');
                }
            }
            else {
                item['isParentBatchValid'] = false;
                item['isParentUOMValid'] = false;


              /*  var items = self.model.worklistDetails.items.filter(function (detail) {
                    return detail.itemId == item.itemId && item.selectedBatch && item.selectedUom && item.selectedUom.uomTypeId > 0 && item.selectedBatch.batchId > 0 && detail.selectedBatch.batchId == item.selectedBatch.batchId && detail.selectedUom.uomTypeId == item.selectedUom.uomTypeId;
                });*/
                
                var items = self.model.worklistDetails.items.filter(function (detail) {
                	if(detail.selectedUom==undefined || detail.selectedUom==null)
                		{
                		detail.selectedUom=item.selectedUom;
                		}
               	 if (!detail.disabled && !detail.itemDisabled && detail.selectedBatch!=null) {
               		
                   return detail.itemId == item.itemId && item.selectedBatch && item.selectedUom
                   && item.selectedUom.uomTypeId > 0 && item.selectedBatch.batchId > 0 && detail.selectedBatch.batchId
                   == item.selectedBatch.batchId && detail.selectedUom.uomTypeId == item.selectedUom.uomTypeId;}
               });
                
                if (items.length > 1) {
                    isValid = false;
                    item['isParentBatchValid'] = true;
                    item['isParentUOMValid'] = true;
                    growl.error('There should not be same batch and same UOM for same item');
                }
            }
            return isValid;
        }

        var AllocateQuantity = function (index, bill, parentIndex) {
            if (!!bill) {
                // var item = bill.items[index];
                var item = null;
                if (parentIndex == null) {
                    item = bill.items[index];
                    
                    //ganesh
                    item.selfPayable= item.netAmount;
                    item.creditPayable= 0;
                    bill.items[index].selfPayable = item.netAmount;
                    bill.items[index].creditPayable = 0;
                } else {
                    item = bill.items[parentIndex].selectedItems[index];
                    item.selfPayable= item.netAmount;
                    item.creditPayable= 0;
                    //Ganesh
                    bill.items[parentIndex].selectedItems[index].selfPayable = item.netAmount;
                    bill.items[parentIndex].selectedItems[index].creditPayable = 0;
                }

                CalculateQuantity(item, item.selectedUom, item.uom);

                if (CanAllocate(item) && (parseInt(item.quantity) <= (item.stock!=undefined ? item.stock :item.newStock)) && (item.newQty==undefined ? true:item.newQty >= parseInt(item.quantity)) && (parseInt(item.quantity) != 0) ) {
                    var URI = CONSTANTS.PHARMACY.OP.WALK_IN_SALES.ALLOCATE_API  + $scope.unitId + "/"+$scope.organizationId +"/"+$scope.storeId + "/"+$scope.userId+"/"+item.itemId+"/"+ bill.id + "/" + (!!item.allocationIds ? item.allocationIds : "");
                    var data = {
                        "batchId": item.selectedBatch.batchId,
                        "quantity": item.leaseQuantity
                    };

                    $rootScope.startLoader();
                    GenericService.serviceAction("PUT", URI, data).then(
                        function (response) {
                            if (!!response.data) {
                                item.allocationId = response.data.id;
                                item.allocationIds = response.data.ids;
                                
                                item.selectedBatch.availableStockQuantity = response.data.object;
                                item.stock = response.data.object;
                                item.newStock = response.data.object;
                                angular.forEach(item.batch, function (value4, index4) {
                                  	
                                  	if(value4.batchId == item.selectedBatch.batchId){
                                  		value4.availableStockQuantity = response.data.object;
                                  	}
                                });
                                ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);
                                
                                if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                                    $scope.isValidated = true;
                                }
                                else{
                                    $scope.isValidated = false;
                                }
                                if($scope.activePackageList!=null&& $scope.activePackageList.length>0){
                                    $scope.isValidated = true;
                                }
                                StorePreviousValue(item);
                            }
                            
                            $rootScope.stopLoader();
                        },
                        function (ex) {
                            if(parseInt(item.quantity) > (item.stock!=undefined ? item.stock :item.newStock)){
                                growl.warning('No enough quantity');
                                item.quantity = 0;
                           }
                            item.disabled = false;
                            item.itemDisabled = false;
                            RestorePreviousValue(item);
                            item.itemtotalAmount = 0.0 ;
                            item.totalAmount = 0.0;
                            item.discountAmount = 0.0;
                            item.netAmount = 0.0;
                            item.quantity = 0;
                            $rootScope.stopLoader();
                        });
                } else {
                	if(parseInt(item.quantity) > (item.stock!=undefined ? item.stock :item.newStock)){
                		growl.warning('No enough quantity');
                		 item.quantity = 0;
                	} 
                      item.itemtotalAmount = 0.0 ;
                      item.totalAmount = 0.0;
                      item.discountAmount = 0.0;
                      item.netAmount = 0.0;
                      item.quantity = 0;
//                      RestorePreviousValue(item);
                      StorePreviousValue(item);
                      $rootScope.stopLoader();
                }
            }
        }

        var CanAllocate = function (item) {

            if (!!item.selectedBatch && !!item.selectedUom.uomUnitId && (!!item.quantity) && !(item.prevQuantity == item.quantity && item.quantity == '')) {
                return true;
            }

            return false;
        }

        var StorePreviousValue = function (item) {
            item.prevBatch = item.selectedBatch;
            item.prevUom = item.selectedUom;
            item.prevQuantity = item.quantity;

            CalculateRate(item, item.selectedUom, item.uom);
        }

        var RestorePreviousValue = function (item) {
            item.selectedBatch = item.prevBatch;

            if (!!item.selectedBatch) {
                item.selectedUom = item.prevUom;
            } else {
                item.uom = [{
                    uomDetails: "Select UOM"
                }];
                item.selectedUom = item.uom[0];
                item.rate = 0;
            }

            item.quantity = item.prevQuantity;

            CalculateRate(item, item.selectedUom, item.uom);
        }
        var AddItem = function (index, bill, parentIndex) {
            //bill.items.splice(index + 1, 0, CopyItem(index, bill, parentIndex));
            if (parentIndex == undefined) {
            	if(bill.items[index].quantity==undefined || bill.items[index].quantity== null ||bill.items[index].quantity=="" || bill.items[index].quantity==0 || !bill.items[index].quantity){
          		    growl.error('Please Enter Dispensed Quantity')
                    return false;
            	}
                bill.items.splice(index + 1, 0, CopyItem(index, bill));
                bill.items[index]['disableInputField']=true;
                if(bill.items.length >1){
                    $scope.flagRemove = true;
                    bill.items[index].isShow=true;
                }
                if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                    $scope.isValidated = true;
                }
                else{
                    $scope.isValidated = false;
                }
                if($scope.activePackageList!=null&& $scope.activePackageList.length>0){
                    $scope.isValidated = true;
                }
            } else {
                bill.items[parentIndex].selectedItems.splice(index + 1, 0, CopyItem(index, bill, parentIndex));
                bill.items[parentIndex].selectedItems[index]['disableInputField']=true;
                if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                    $scope.isValidated = true;
                }
                else{
                    $scope.isValidated = false;
                }
                if($scope.activePackageList!=null&& $scope.activePackageList.length>0){
                    $scope.isValidated = true;
                }
            }
        }
        
        var CalculateOrederOrIssuedQty = function(item){
       	 var issuedQty = 0;
       	 if(item.selectedUom !=undefined && item.selectedUom.uomTypeId !=undefined)
            {
       	        var quantity = (item.quantity!=null && item.quantity !="")?item.quantity:0;
                if(item.selectedUom != undefined){
                    if(item.isThreeUOMApplicable == false){
                        if(item.uom != undefined && item.uom != null){
                            if(item.uom[2] == undefined || item.uom[2] == null){
                                if(item.selectedUom.uomTypeId == 3)
                                {
                                    issuedQty  = parseInt(quantity);                 
                                }
                                else if(item.selectedUom.uomTypeId == 2)
                                {
                                    if(item.selectedBatch.uomListData == undefined || item.selectedBatch.uomListData == null || item.selectedBatch.uomListData == ""){
                                        if(item.uoms == undefined || item.uoms == null){
                                            var stripQty = item.selectedBatch.uom[1].conversion * parseInt(quantity);
                                            issuedQty  = stripQty;
                                        }
                                        else{
                                            var stripQty = item.uoms[1].conversion * parseInt(quantity);
                                            issuedQty  = stripQty;
                                        } 
                                    }
                                    else{
                                        var stripQty = item.selectedBatch.uomListData[1].conversion * parseInt(quantity);
                                        issuedQty  = stripQty; 
                                    }       
                                }
                                else if(item.selectedUom.uomTypeId == 1)
                                {
                                    if(item.selectedBatch.uomListData == undefined || item.selectedBatch.uomListData == null || item.selectedBatch.uomListData == ""){
                                        if(item.uoms == undefined || item.uoms == null){
                                            var boxQty = item.selectedBatch.uom[1].conversion *  item.selectedBatch.uom[2].conversion * parseInt(quantity);
                                            issuedQty  = boxQty;
                                        }
                                        else{
                                            var boxQty = item.uoms[1].conversion *  item.uoms[2].conversion * parseInt(quantity);
                                            issuedQty  = boxQty;
                                        }
                                    }
                                    else{
                                        if(item.selectedBatch.uomListData[0].conversion == undefined || item.selectedBatch.uomListData[0].conversion == null){
                                            var boxQty = item.selectedBatch.uomListData[1].conversion *  item.selectedBatch.uomListData[2].conversion * parseInt(quantity);
                                            issuedQty  = boxQty;
                                        }
                                        else{
                                            var boxQty = item.selectedBatch.uomListData[0].conversion *  item.selectedBatch.uomListData[1].conversion * parseInt(quantity);
                                            issuedQty  = boxQty;
                                        }
                                    }
                                }
                            }
                            else{
                                if(item.selectedUom.uomTypeId == 3)
                                {
                                    issuedQty  = parseInt(quantity);                 
                                }
                                else if(item.selectedUom.uomTypeId == 2)
                                {
                                    if(item.selectedBatch.uomListData == undefined || item.selectedBatch.uomListData == null || item.selectedBatch.uomListData == ""){
                                        var stripQty = item.uom[1].conversion * parseInt(quantity);
                                        issuedQty  = stripQty; 
                                    }
                                    else{
                                        var stripQty = item.selectedBatch.uomListData[1].conversion * parseInt(quantity);
                                        issuedQty  = stripQty; 
                                    }       
                                }
                                else if(item.selectedUom.uomTypeId == 1)
                                {
                                    if(item.selectedBatch.uomListData == undefined || item.selectedBatch.uomListData == null || item.selectedBatch.uomListData == ""){
                                        if(item.uom[0] == undefined || item.uom[0] == null){
                                            var boxQty = item.uom[1].conversion *  item.uom[2].conversion * parseInt(quantity);
                                            issuedQty  = boxQty;
                                        }
                                        else if(item.uom[0].conversion == undefined || item.uom[0].conversion == null){
                                            var boxQty = item.uom[1].conversion *  item.uom[2].conversion * parseInt(quantity);
                                            issuedQty  = boxQty;
                                        }
                                        else{
                                            var boxQty = item.uom[0].conversion *  item.uom[1].conversion * parseInt(quantity);
                                            issuedQty  = boxQty;
                                        }
                                    }
                                    else{
                                        if(item.selectedBatch.uomListData[0].conversion == undefined || item.selectedBatch.uomListData[0].conversion == null){
                                            var boxQty = item.selectedBatch.uomListData[1].conversion *  item.selectedBatch.uomListData[2].conversion * parseInt(quantity);
                                            issuedQty  = boxQty;
                                        }
                                        else{
                                            var boxQty = item.selectedBatch.uomListData[0].conversion *  item.selectedBatch.uomListData[1].conversion * parseInt(quantity);
                                            issuedQty  = boxQty;
                                        }
                                    }
                                }
                            }
                        }
                        else{
                            if(item.selectedUom.uomTypeId == 3)
                            {
                                issuedQty  = parseInt(quantity);                 
                            }
                            else if(item.selectedUom.uomTypeId == 2)
                            {
                                if(item.selectedBatch.uomListData == undefined || item.selectedBatch.uomListData == null || item.selectedBatch.uomListData == ""){
                                    if(item.uoms == undefined || item.uoms == null){
                                        var stripQty = item.selectedBatch.uom[1].conversion * parseInt(quantity);
                                        issuedQty  = stripQty;
                                    }
                                    else{
                                        var stripQty = item.uoms[1].conversion * parseInt(quantity);
                                        issuedQty  = stripQty;
                                    } 
                                }
                                else{
                                    var stripQty = item.selectedBatch.uomListData[1].conversion * parseInt(quantity);
                                    issuedQty  = stripQty; 
                                }       
                            }
                            else if(item.selectedUom.uomTypeId == 1)
                            {
                                if(item.selectedBatch.uomListData == undefined || item.selectedBatch.uomListData == null || item.selectedBatch.uomListData == ""){
                                    if(item.uoms == undefined || item.uoms == null){
                                        var boxQty = item.selectedBatch.uom[1].conversion *  item.selectedBatch.uom[2].conversion * parseInt(quantity);
                                        issuedQty  = boxQty;
                                    }
                                    else{
                                        var boxQty = item.uoms[1].conversion *  item.uoms[2].conversion * parseInt(quantity);
                                        issuedQty  = boxQty;
                                    }
                                }
                                else{
                                    if(item.selectedBatch.uomListData[0].conversion == undefined || item.selectedBatch.uomListData[0].conversion == null){
                                        var boxQty = item.selectedBatch.uomListData[1].conversion *  item.selectedBatch.uomListData[2].conversion * parseInt(quantity);
                                        issuedQty  = boxQty;
                                    }
                                    else{
                                        var boxQty = item.selectedBatch.uomListData[0].conversion *  item.selectedBatch.uomListData[1].conversion * parseInt(quantity);
                                        issuedQty  = boxQty;
                                    }
                                }
                            }
                        }
                    }
                    else{
                        if(!item.disabled){
                            if(item.selectedUom.uomTypeId == 3)
                            {
                                issuedQty  = parseInt(quantity);                 
                            }
                            else if(item.selectedUom.uomTypeId == 2)
                            {
                                var stripQty = item.selectedBatch.uom[1].conversion * parseInt(quantity);
                                issuedQty  = stripQty;        
                            }
                            else if(item.selectedUom.uomTypeId == 1)
                            {
                                var boxQty = item.selectedBatch.uom[0].conversion *  item.selectedBatch.uom[1].conversion * parseInt(quantity);
                                issuedQty  = boxQty;
                            }
                        }
                    }
                }
            }
       	  
       	    return issuedQty;
        }

        var CopyItem = function (index, bill, parentIndex) {
            // var item = bill.items[index];
            var item = null;
            var totalIssuedQty = 0;
            var finalOrdQty = 0;
            var newOrderQty = undefined;
            if (parentIndex == undefined && bill.items[index].qtyPrescribed != undefined) {
                item = bill.items[index];
                // $scope.flagRemove = true;
                
                angular.forEach(self.model.worklistDetails.items, function (value, index1) {
  	            	
	                	if(bill.items[index].itemId == value.itemId){
	                		
	                		totalIssuedQty += CalculateOrederOrIssuedQty(value);
	                	}
	                	
	            });  
	      	 finalOrdQty = bill.items[index].finalOrderedQty;
                if(self.model.worklistDetails.items.length > 1){
                    $scope.flagRemove = true;
                }
                else{
                    $scope.flagRemove = false;
                }
                
                newOrderQty = finalOrdQty - totalIssuedQty;
            } else if (parentIndex != undefined && bill.items[parentIndex].selectedItems[index].qtyPrescribed != undefined) 
                {
                item = bill.items[parentIndex].selectedItems[index];
                
                finalOrdQty = bill.items[parentIndex].finalOrderedQty;
                if(bill.items[parentIndex].selectedBatch != undefined){
              	  totalIssuedQty += CalculateOrederOrIssuedQty(bill.items[parentIndex]);
              }
             	
             	if(bill.items[parentIndex].selectedItems != null && bill.items[parentIndex].selectedItems.length>0){
                 		
             		angular.forEach(bill.items[parentIndex].selectedItems, function (selectValue, selectIndex) {
             			
             			totalIssuedQty += CalculateOrederOrIssuedQty(selectValue);
            		 	});
               }
                if(self.model.worklistDetails.items.length > 1){
                    $scope.flagRemove = true;
                }
                else{
                    $scope.flagRemove = false;
                }
                
                 newOrderQty = finalOrdQty - totalIssuedQty;
            }else{
            	 item = bill.items[index];
            }
            
            
	      return {
                itemId: item.itemId,
                productCategoryId:item.productCategoryId,
                isConsumed:item.isConsumed,
                itemCode: item.itemCode,
                itemName: item.itemName,
                genericName: item.genericName,
                formulationTypeId: item.formulationTypeId,
                genericId: item.genericId,
                manufacturerName: item.manufacturerName,
                qtyPrescribed: newOrderQty ,
                newQty: newOrderQty ,
                newStock: item.stockAvailable,
                stock: item.stockAvailable,
                batch: item.batch,
                storeId: item.storeId,
                selectedBatch: null,
                isNarcotic:item.isNarcotic,
                uom: item.uom,
                markUpDown: item.markUpDown,
                markUpDownPer: item.markUpDownPer,
                markupStatus: item.markupStatus,
                taxId: item.taxId,
                tax: item.tax,
                taxPercentage: item.taxPercentage,
                statusId: item.statusId,
                status: item.status,
                isShow: true,
                otc	: item.otc,
                duration : item.duration,
                durationUnit : item.durationUnit,
                prescribedDose : item.prescribedDose,
                units : item.units,
                frequencyDesc : item.frequencyDesc,
                substituteBtn: ((parentIndex == undefined && parentIndex == null )? false : true),
                saleDetailPayeeMapperDtoList: item.saleDetailPayeeMapperDtoList,
                finalOrderedQty : item.finalOrderedQty,
                prescriptionDetailId : item.prescriptionDetailId,
                parentItemId : ((item.parentItemId == undefined && item.parentItemId == null )? null : item.parentItemId) ,
                instructionDesc : item.instructionDesc,
                patientClassPercentage : item.patientClassPercentage,
                patientClassAmount : item.patientClassAmount,
                checkNewItem: item.checkNewItem,
                itemDisabled : false,
                disabled : false,
                storageUnitName: item.storageUnitName,
                isThreeUOMApplicable: item.isThreeUOMApplicable,
                onlySubButtonDisable: item.onlySubButtonDisable,
                qtyMainLine: item.qtyPrescribed,
                addCopyItemFlag: true,
                colorCode:item.colorCode
            }
        }
       
        var RemoveItem = function (index, bill,parentIndex)
    
        {
        	
        	//sneha
        	if( $scope.nonFormularyItemList!= undefined && $scope.nonFormularyItemList != null && $scope.nonFormularyItemList.length > 0){
        		angular.forEach($scope.nonFormularyItemList, function (value, key) {
          			if(bill.items[index].prescriptionDetailsId!=undefined && bill.items[index].prescriptionDetailsId!= null && bill.items[index].prescriptionDetailsId==value.prescriptionDetailsid){
          					value.status = 'A';
          					$scope.servicesListPopup.splice(key, 1);
          					 $scope.orderQtyTotalSum =  parseFloat($scope.orderQtyTotalSum) - parseFloat(value.quantity);
          				}
          			});
        	}
          	
          	
        	  var item = null;
              if (parentIndex == undefined &&  parentIndex == null) {
                  item = bill.items[index];
                  var itemCount = 0;
                  angular.forEach(self.model.worklistDetails.items, function (value, index) {
                    	
                    	if(value.itemId == item.itemId){
                    		itemCount +=1;
                    	}
                });  
                  if(itemCount>2){
                  	if((itemCount - 1) > index){
                  		
                  		for(var i = index;i<itemCount - 1;i++){
                  			
                  			var removeItemPickedQty = CalculateOrederOrIssuedQty(item) ;
                      		bill.items[i+1].qtyPrescribed  += removeItemPickedQty ;
                      		ConvertQtyOrdered(bill.items[i+1], bill.items[i+1].uom, bill.items[i+1].selectedUom,1);
                  		}
                  		
                  	}
                  }
                  
              } else {
                  item = bill.items[parentIndex].selectedItems[index];
                  if(bill.items[parentIndex].selectedItems.length>1   ){
                  	
                  	if((bill.items[parentIndex].selectedItems.length - 1) > index){
                  		
                  		for(var i = index;i<bill.items[parentIndex].selectedItems.length - 1;i++){
                  			var removeItemPickedQty = CalculateOrederOrIssuedQty(bill.items[parentIndex].selectedItems[index]) ;
                  			bill.items[parentIndex].selectedItems[i+1].qtyPrescribed += removeItemPickedQty ;
                      		ConvertQtyOrdered(bill.items[parentIndex].selectedItems[i+1], bill.items[parentIndex].selectedItems[i+1].uom, bill.items[parentIndex].selectedItems[i+1].selectedUom,1);
                  		}
                  	}
                  }
                  
              }
              if(parentIndex!=undefined && parentIndex!=null)
            	  {
              if(bill.items[index-1]!=undefined && bill.items[index-1]!=null && bill.items[parentIndex].selectedItems[index-1]!=undefined && bill.items[parentIndex].selectedItems[index-1]!=null && bill.items[parentIndex].selectedItems[index-1].disableInputField!=undefined && bill.items[parentIndex].selectedItems[index-1].disableInputField!=null)
        	  {
            	  bill.items[parentIndex].selectedItems[index-1].disableInputField=false;
            	  bill.items[parentIndex].selectedItems[index-1].quantityIssuedFlag=false;
        	  }
            	  }
              else
            	  {
            	  if(bill.items[index-1]!=undefined && bill.items[index-1]!=null && bill.items[index-1].disableInputField!=undefined && bill.items[index-1].disableInputField!=null)
            	  {
            	  bill.items[index-1].disableInputField = false;
            	  bill.items[index-1].quantityIssuedFlag = false;
            	  }
            	  }
              if (parentIndex == undefined && parentIndex == null) {
            	  
            	  if(bill.items[index].assetCategoryId != undefined &&  bill.items[index].assetCategoryId != null && bill.items[index].parentItemId == undefined){
            		  $scope.removeItemFlag = true;
            	  }
            	   bill.items[index]['disabled'] = false;
                   bill.items[index]['itemDisabled'] = false;
            	   bill.items.splice(index, 1);
                 
              } else {
              	  bill.items[parentIndex].selectedItems.splice(index, 1);
              	if(bill.items[parentIndex].selectedItems.length==0){
                    
              	  bill.items[parentIndex]['disabled'] = false;
                  bill.items[parentIndex]['itemDisabled'] = false;
              	}else{
    		        var isParentSubDisabled=false;
                    angular.forEach(bill.items[parentIndex].selectedItems, function (value, index) {

                               if(value.itemDisabled!=undefined || !value.itemDisabled){
                               	isParentSubDisabled=true;
                               }
                         });
                        if(isParentSubDisabled && bill.items[parentIndex].selectedItems.length < 1){
                                 bill.items[parentIndex]['disabled'] = false;
                                 bill.items[parentIndex]['itemDisabled'] = false;
                        }
              	}
              }
              if(bill.items.length <=1){
                  $scope.flagRemove = false;
                  bill.items[0].isShow=false;
              }
              if(bill.items[parentIndex].selectedItems.length==0)
        	  {
	              bill.items[parentIndex].quantityIssuedFlag =false;
	              bill.items[parentIndex].disableInputField=false;
        	  }else{
				  var countSub = 0 ;
				    angular.forEach(bill.items[parentIndex].selectedItems, function (value, index) {
		                if(value.itemDisabled==undefined || value.itemDisabled==false ){
		                	countSub++;
		                }
		            });
				  if(countSub == 0){
					   bill.items[parentIndex].quantityIssuedFlag =false;
		               bill.items[parentIndex].disableInputField=false;
		               bill.items[parentIndex].disabled=false;
				  }
			  }
              
        }

        var SubstituteItems = function (item) {
        item.substititeFlag=true;
        self.model.substituteDetails = [];
        self.model.SelectedItems = [];
       // self.model.selectedQuantity = item.qtyPrescribed;
        self.model.formulationTypeId = item.formulationTypeId;
        self.model.instructionDesc = item.instructionDesc;
        self.model.selectedItemId = item.id!=undefined ? item.id : item.itemId;
        self.model.productCategoryId = item.productCategoryId;
        self.model.taxId = item.taxId;
        self.model.duration = item.duration + " " + item.durationUnit;
        self.model.frequencyDesc = item.frequencyDesc;
        self.model.selectedParentItemId = item.itemId;
        if(item.selectedUom == undefined){
       	 self.model.selectedQuantity = item.qtyPrescribed;
       }else{
    	   if(item.quantity == undefined || item.quantity == null || item.quantity == 0){

    		   var mulQty = item.finalOrderedQty;
    		   var  newOrderQty =  mulQty;
    	   }
    	   else{
    		   var mulQty = item.selectedUom.conversion * item.quantity;
    		   var  newOrderQty = item.finalOrderedQty - mulQty;
    	   }
           self.model.selectedQuantity = newOrderQty;

       }
        self.model.selectedItemGenericId = item.genericId;
        self.model.taxPercentage = item.taxPercentage;
        self.model.selectAllRows = false;
//        var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.SUBSTITUTE_API + item.genericId + '/' + item.itemId +  '/' + self.model.encounterId;
//        var URI = CONSTANTS.PHARMACY.IP.ASSIGN_PICKER_DISPENSER.SUBSTITUTE_API + item.genericId + '/' + item.itemId+ '/' + item.isNarcotic;
        var URI = CONSTANTS.PHARMACY.IP.ASSIGN_PICKER_DISPENSER.SUBSTITUTE_API + $scope.unitId + '/' + item.genericId + '/' + item.itemId+ '/' + item.isNarcotic+ '/' +  $scope.storeId+'/'+ self.model.formulationTypeId;
        $rootScope.startLoaderPopup();
        GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
                $rootScope.stopLoaderPopup();
                self.model.substituteDetails = response.data;
                if (!self.model.substituteDetails || self.model.substituteDetails == []) 
                {
                    return;
                } else 
                {
                    self.model.substituteDetails.forEach(function (item) 
                    {
                        self.model.selectAllRows = false;
                        item.isSelected = false;
                        // item.batch.unshift({
                        //     "batchDetails": "Select Batch"
                        // });
                        item.uom = [{
                            "uomDetails": "Select UOM"
                        }];
                       // item.selectedBatch = item.batch[0];
                        $scope.resultData = item.batch.filter(word => word.availableStockQuantity > 0);
                        item.batch= $scope.resultData;
                        item.selectedBatch = commonDetailService.getDefaultBatchFromConfig(item.batch);
                        item.selectedUom = item.selectedBatch!=undefined ? item.selectedBatch.uom[0] : item.uom[0];
                    });
                    self.model.substituteDetailsData = self.model.substituteDetails;
                    self.model.substituteDetails = [];
                    angular.forEach(self.model.substituteDetailsData, function(d){
                        if(d.batch.length != 0){
                            self.model.substituteDetails.push(d);
                        }
                    })
                }
            },
            function (err) {
                $rootScope.stopLoaderPopup();
            });
        }

        var SelectAllItems = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedItems = [];
                self.model.substituteDetails.forEach(function (item) {

                    item.isSelected = true;
                    self.model.SelectedItems.push(item);
                    var index = SelectedItems.indexOf(item);
                    if(SelectedItems.length>1)
                        {
                    	SelectedItems[index]['isMultiBatch']=true
                        }
                    $scope.btnAddShow = false;
                });
            } else {
                self.model.SelectedItems = [];
                self.model.substituteDetails.forEach(function (item) {
                    item.isSelected = false;
                });
                $scope.btnAddShow = true;
            }
        }

        var isSelected = function (item) {
            return item.isSelected;
        }

        var SelectSubstituteItem = function (item) {
            var itemIds = self.model.SelectedItems;

            if (item.isSelected) {
                itemIds.push(item);
                var index = itemIds.indexOf(item);
                if(itemIds.length>1)
                    {
                    itemIds[index]['isMultiBatch']=true
                    }
//                $scope.btnAddShow = false;
            } else {
                var index = itemIds.indexOf(item);
                if (index > -1) {
                    itemIds.splice(index, 1);
                }
//                $scope.btnAddShow = true;
            }
            if(self.model.SelectedItems.length > 0){
            	$scope.btnAddShow = false;
            }else{
            	 $scope.btnAddShow = true;
            }
            if (self.model.substituteDetails.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
        }
        
        var validateUOMAndBatch = function (item) {
            var isValid = true;

            if (item) {
                item['isBatchValid'] = false;

                var items = self.model.substituteDetails.filter(function (detail) {
                    return detail.itemCode == item.itemCode && item.selectedBatch && item.selectedBatch.batchId > 0 && detail.selectedBatch.batchId == item.selectedBatch.batchId;
                });

                if (items.length > 1) {
                    isValid = false;
                    item['isBatchValid'] = true;
                    growl.error('There should not be same batch for same item');
                }
            }
            return isValid;
        }
        
        var RestorePreviousItem = function (bill) {

            bill.items.forEach(function (item) {
                if (item.selectedItems) {
                    if (item.selectedItems.length == 0) {
                        item['disabled'] = false;
                    }
                }
            });
        }

       /* var validateSubSelectedBatch=function(listSub){
      	  var isValid=true;
      	  angular.forEach(listSub, function(val , key){
      		  if(val.selectedBatch==undefined){
                    growl.error("Please select batch.");
                    $("#substituteModal").show();
      			  isValid=false;
      		  }
      		  else{
      			  
      			  isValid=true;
      		  }
      	  });
      	  if(isValid==true){
      		  var popup = angular.element('#substituteModal');
  	          popup.modal('hide');
      	  }
      	  return isValid; 
        }*/
        
        var validateSubSelectedBatch=function(listSub){
      	  var isValid=true;
      	  var count=0;
      	  angular.forEach(listSub, function(val , key){
      		  if(val.selectedBatch==undefined){
                    growl.error("Please select batch.");
                    $("#substituteModal").show();
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
        $scope.subFlagChange = function()
        {
      	  self.model.worklistDetails.items.forEach(function (item) {
      		  item.substititeFlag=false;
      	  });
        }
        var Add = function () {
            if (!self.model.SelectedItems || self.model.SelectedItems == undefined || self.model.SelectedItems == '') {
                growl.error('Please select items to add')
                return false;
            } else {
		           if(validateSubSelectedBatch(self.model.SelectedItems)){
		        	   		self.model.SelectedItems.forEach(function (selectedItem) {
		                    selectedItem['qtyPrescribed'] = self.model.selectedQuantity;
		                    selectedItem['newQty'] = self.model.selectedQuantity;
		                    selectedItem['substituteBtn'] = true;
		                    selectedItem['selectedBatch'] = selectedItem.selectedBatch;
		                    selectedItem['parentItemId'] = self.model.selectedParentItemId;
		                    selectedItem['consumedItemId'] = self.model.selectedParentItemId;
		                    selectedItem['consumedPcategroyId'] = null;
		                    selectedItem['selectedUom'] = selectedItem.selectedUom;
		                    selectedItem['genericId'] = self.model.selectedItemGenericId;
		                    selectedItem['uom'] = [];
		                    selectedItem['itemId'] = selectedItem.itemId;
		                    selectedItem['productCategoryId'] = self.model.productCategoryId;
		                    selectedItem['assetCategoryId'] = selectedItem.assetCategoryId;
		                    selectedItem['isConsumed'] = selectedItem.isConsumed;
		                    selectedItem['manufacturerName'] = selectedItem.manufacturerName;
		                    selectedItem['otc'] = selectedItem.isOtc;
		                    selectedItem['taxPercentage'] = selectedItem.taxPercentage;
		                    selectedItem['taxId'] =  selectedItem.taxId;
		                    selectedItem['saleDetailPayeeMapperDtoList'] = [];
		                    selectedItem['SaleDetailPackageMapperList'] = [];
		                    selectedItem['patientClassPercentage'] = 0;
		                    selectedItem['markUpDown'] = selectedItem.markUpDown;
		                    selectedItem['markUpDownPer'] = selectedItem.markUpDownPer;
		                    selectedItem['itemId'] = selectedItem.itemId;
		                    selectedItem['itemDisabled'] = false;
		                    selectedItem['disabled'] = false;
		                    selectedItem['finalOrderedQty'] = self.model.selectedQuantity;
		                    selectedItem['instructionDesc'] = self.model.instructionDesc;
		                    selectedItem['duration'] = self.model.duration;
		                    selectedItem['frequencyDesc'] = self.model.frequencyDesc;
		                });
		
		                self.model.worklistDetails.items.forEach(function (list) 
		                 {
		                	
		                	if(list.batch.length == 0){
		                		list.rate = 0;
		                		list.mrp = 0;
		                	}
		                	 let promise = $timeout();
		                    angular.forEach(self.model.SelectedItems, function(item) {
		                   // self.model.SelectedItems.forEach(function (item) {
		                    	 promise = promise.then(function()
		                         {
			                        item.uom = [{
			                            uomDetails: "Select UOM",
			                        }];
		                        if(item.selectedBatch!=undefined){
		                            if (!!item.selectedBatch.batchId) {
		                                item.stockAvailable=item.selectedBatch.availableStockQuantity;
		                                item.stock=item.stockAvailable;
		                                item.newStock = item.selectedBatch.availableStockQuantity;
		                                if($scope.autoPrescribedQty == true){
			                                if($scope.autoBatch == true && item.stockAvailable > item.newQty){
			    								item.quantity = item.newQty;
			    							}
			    							else{
			    								item.quantity = item.stockAvailable;
			    							}
		                                }
		                                if(item.quantity >= item.newQty)
				                        {
		                                	item.quantityIssuedFlag=true;
				                        }
				                        else
				                        {
				                        	item.quantityIssuedFlag = false;
				                        }
		                                item.uom = item.selectedBatch.uom;
		                                item.selectedUom = $filter('filter')(item.selectedBatch.uom, {
		                                    opUom: true
		                                }, true)[0];
		                            }else {
		                                item.selectedUom = item.uom[0];
		                            }
		                        }
		                        if(item.batch) {
		                            if(item.batch.length > 0) {
		                                self.model.IsEnbaledSave = true;
		                            }
		                        }
		                        // if (!item.selectedUom) item.selectedUom = "";
		                        //for markup and markdown and concession
		                        var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId + '/'+$scope.storeId + '/' + item.itemId + '/' + $scope.entitlementObj.paymentEntitlementTypeId + '/' + $scope.encounterId;
		                        $rootScope.startLoader();
		                        GenericService.serviceAction("GET", URI, {})
		                        .then(function (response) {
		                            $timeout( function(){
		                          	  item.markUpDown =response.data.markUpDown;
		                          	  item.markUpDownPer =response.data.markUpDownPer;
		                          	  item.patientClassPercentage =response.data.patientClassPercentage;
		                              item.consumedItemId = list.itemId;
		                        	  item.consumedPcategroyId =list.productCategoryId;
		                        	  if(item.selectedBatch!=undefined && item.selectedBatch!=null)
										{
											item.taxId=item.selectedBatch.taxId;
											item.taxPercentage=item.selectedBatch.taxPercentage;
										}
		                        	  if(item.genericId==list.genericId)
		                        		  {
		                        	  item.colorCode=list.colorCode;
		                        		  }
                                      angular.forEach(response.data.batch, function(d){
                                    	  if(item.selectedBatch.batchId == d.batchId){
                                              item.selectedBatch.actualLendingPrice = d.actualLendingPrice;
                                              item.selectedBatch.rate = d.rate;
                                            }
                                      });
                                      angular.forEach(item.batch, function(b){
                                    	  if(b.batchId != undefined && b.batchId != null){
                                    		  angular.forEach(response.data.batch, function(b1){
                                    			  if(b.batchId == b1.batchId){
                                            		  b.actualLendingPrice = b1.actualLendingPrice;
                                            		  b.rate = b1.rate;
                                    			  }
                                        	  });
                                    	  }
                                      });
                                      if(item.isThreeUOMApplicable == false){
                                        item.selectedBatch.uomListData = item.uoms;
                                        item.uom = [];
                                        item.selectedBatch.uom = [];
                                        angular.forEach(item.selectedBatch.uomListData, function(d){
                                            if(d.uomTypeId != 2){
                                                item.selectedBatch.uom.push(d);
                                                item.uom.push(d);
                                            }
                                        });
                                      }
                                      $scope.selectedUomData = item.selectedUom;
                                      if(item.isThreeUOMApplicable == false){
                                        ConvertQtyOrdered(item, item.uoms, item.selectedUom);
                                      }
                                      else{
                                        ConvertQtyOrdered(item, item.uom, item.selectedUom);
                                      }
//                                      $scope.calculateReceivedQty(item); 
                                      if($scope.autoPrescribedQty == true){
	                                      if($scope.autoBatch == true && item.newStock > item.newQty){
			    							item.quantity = item.newQty;
		    							  }
			    						  else{
			    							item.quantity = item.newStock;
			    					      }
                                      }
                                      if(item.quantity >= item.newQty)
			                          {
                                    	  item.quantityIssuedFlag=true;
				                      }
				                      else
				                      {
				                          item.quantityIssuedFlag = false;
				                      }
                                      Calculate(item,self.model.worklistDetails);
                                     
                                      $scope.calculateReceivedQtySub(item);
		                          	  $rootScope.stopLoader();
		                           },2000);
		                        }, function (err) {
		                            growl.error('Error while fetching the item details');
		                            $rootScope.stopLoader();
		                        });
		                      });
		                    	   return $timeout(2000);
		                    });
		                    if (self.model.selectedItemId == (list.id!=undefined ? list.id : list.itemId )) {
		                    	if(list.substititeFlag){
		                    	if(list.quantity == null || list.quantity == 0){
		                        list['disabled'] = true;
		                        list.quantity = null;
		                    	}
		                        if (!list['selectedItems'])
		                            list['selectedItems'] = self.model.SelectedItems;
		                        else {
		                            list['selectedItems'] = list['selectedItems'].concat(self.model.SelectedItems);
		                        }
		                        Calculate(list,self.model.worklistDetails);
		                        // list.selectedUom = list.selectedUom[0];
		                        list.substititeFlag=false;
		                        if(list.selectedItems!=undefined && list.selectedItems!=null && list.selectedItems.length>0 )
                            	{
                            	list.quantityIssuedFlag =true;
                            	list.disableInputField=true;
                            	}
		                      }
		                    }
		                });
		
		                self.model.SelectedItems = [];
		            }
        }
           /* var popup = angular.element('#substituteModal');
            popup.modal('hide');*/
            }

//        var AddSubstituteItem = function (index, bill) {
//            bill.items.splice(index + 1, 0, CopySubstituteItem(index, bill));
//        }
//        
        var AddSubstituteItem = function (index, bill) {
            bill.splice(index + 1, 0, CopySubstituteItem(index, bill));
        }
        //For Remove 
        var RemoveSubstituteItem = function (index,bill) {
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
        		
        		
        	})
            
        }

        var CopySubstituteItem = function (index, bill) {
            var item = bill[index];
            return {
            	itemId: item.itemId,
            	itemCode: item.itemCode,
                itemName: item.itemName,
                genericName: item.genericName,
                manufacturerName: item.manufacturerName,
                batch: item.batch,
                selectedBatch: null,
                uom: item.batch.uom,
                selectedUom: null,
                taxId: item.taxId,
                tax: item.tax,
                isOtc:item.isOtc,
                taxPercentage: item.taxPercentage,
                disabled: item.disabled,
                isMultiBatch:true,
                uoms:item.uoms,
                storageUnitName:item.storageUnitName
            }
        }

//        var RemoveSubstituteItem = function (index, bill) {
//            bill.items.splice(index, 1);
//        }

        var ChangeItemStatus = function (item) {
            var dispQty = item.newDispQty ? item.newDispQty : 0
           self.model.newQtyGenrated = self.model.newQtyGenrated == NaN ? 0 : self.model.newQtyGenrated;
           self.model.newQtyGenrated = self.model.newQtyGenrated + item.quantity;
            if (item.qtyPrescribed > ((parseInt(item.quantity)) + dispQty)) {
//                item.statusId = SetStatus('Partially Completed');
            }
//             else if (item.qtyPrescribed == ((parseInt(item.quantity)) + dispQty)) {
//                item.statusId = SetStatus('Completed');
//            }
        }

        // var ValidateSaveButton = function () 
        // {
        //     var isValid = false;
        //     self.model.worklistDetails.items.find(function (item) 
        //     {
        //         if ($scope.activePackageList.length>0||$scope.entitlementObj.paymentEntitlementTypeId!=1) 
        //         {
        //             isValid = true;
        //         }
        //     });
        //     return isValid;
        // }

//        $scope.lasaPopUp = function(bill){
//              if(bill.items !== undefined && bill.items !== null && bill.items.length>0){
//                  var checkLasa = false;
//                  angular.forEach(bill.items, function(item){
//                        if(item.isLasa !== undefined && item.isLasa !== null && item.isLasa === true){
//                           checkLasa = true;
//                        }
//                  });
//
//                  if(checkLasa){
//                       checkLasa = false;
//                      var popup = angular.element('#confirmLasa');
//                      popup.modal('show');
//                  }
//                  else{
//                    SavePopup();
//                  }
//              }
//        }

        $scope.confirmMessage = "Are you sure you want to Save?";
        var SavePopup = function (bill) {
        	 $scope.saveBtnDisabled = false;
	         $scope.btnDisabled = false;
	         var checkLasa = false;
              if(bill.items !== undefined && bill.items !== null && bill.items.length>0){
                  var checkLasa = false;
                  angular.forEach(bill.items, function(item){
                        if(item.isLasa !== undefined && item.isLasa !== null && item.isLasa === true){
                           checkLasa = true;
                        }
                  });
              }
        	if($scope.isConsolidatedBill == 'Y' && ($scope.billNumber != null || $scope.billNumber != '' || $scope.billNumber != undefined) && $scope.paymentEntitleMentList.length > 0)
        	{
        	    $scope.confirmMessage = "Patient has an existing bill ("+ $scope.billNumber +"). Do you want to add Pharmacy Service in the existing bill ? "

        	    if(checkLasa){
        	         checkLasa = false;
        	         $scope.confirmMessage = "List contains LASA drugs. And Patient has an existing bill ("+ $scope.billNumber +"). Do you want to add Pharmacy Service in the existing bill ? "
        	    }

        		var popup = angular.element('#consolidatedConfirmSave');
                popup.modal('show');
        	}else{
        	    if(checkLasa){
                   checkLasa = false;
                   $scope.confirmMessage = "List contains LASA drugs. Are you sure you want to Save?";
                }
                else{
                    $scope.confirmMessage = "Are you sure you want to Save?";
                }
        		var popup = angular.element('#confirmSave');
                popup.modal('show');
        	}
        	
        }

        $scope.btnDisabled = false;
        $scope.saveBtnDisabled=false;
        $scope.saveOPSaleFlag = true;
        var SaveBill = function (bill,addToBill) {
        	 $scope.btnDisabled = true;
        	 if($scope.NonFormularyAdded == false || $scope.servicesListPopup.length == 0){
        		 if($scope.saveOPSaleFlag==true)
        	  	  {
        	        	$scope.saveOPSaleFlag = false;	
        	        	ActualSaveBill(bill,addToBill);
        	  	  }
                 angular.element('#consolidatedConfirmSave').modal('hide');
        	 }else{
        		 $scope.saveDataInPrescription(bill,addToBill);
                 angular.element('#consolidatedConfirmSave').modal('hide');
        	 }
        	

        } 
        
        var ActualSaveBill = function (bill,addToBill) {
        	$scope.saveBtnDisabled=true;
        	$scope.doctorId = bill.doctorId;
        	$scope.encounterId = bill.encounterId;
        	$scope.btnDisabled = true;
        	$scope.totalPriscribedQuantity=0;
            if (ValidateBill(bill)) {
            	  //check contract Ganesh
          	  $scope.totalSelfPayable = 0.00;
          	  $scope.totalCreditPayable = 0.00;
          	  var statusFinalFlag =  false;
                if($scope.entitlementObj.paymentEntitlementTypeId == '3'||$scope.entitlementObj.paymentEntitlementTypeId == '2')
          		{
          		   var found = $scope.paymentEntitleMentList.find(ar=>(ar.paymentEntitlementId==2||ar.paymentEntitlementId==3));
          		   if(found==undefined)
          			   {
          			     growl.error('Please Apply Payment Entitlement.');
          			     return false;
          			   }
          		   
          		}
            	
                var URI = CONSTANTS.PHARMACY.OP.WALK_IN_SALES.SAVE_API;
                var data = {
                    "screenId": 1,
                    "visitTypeId": 1,//FOR OP
                    "prescriptionId": !!bill.id ? bill.id : undefined,
                    "patientId": !!bill.patientId ? bill.patientId : undefined,
                    "encounterId": !!bill.encounterId ? bill.encounterId : undefined,
                    "doctorId": !!bill.doctorId ? bill.doctorId : undefined,
                    "discountTypeId": !!bill.discountType ? bill.discountType : undefined,
                  //  "discountCategoryId": !!bill.discountCatgeory.id ? bill.discountCatgeory.id : undefined,
                    "discount": !!bill.discount ? bill.discount : 0,
                    "totalAmount": 0,
                    "totalDiscountAmount": !!bill.totalDiscountAmount ? Number(bill.totalDiscountAmount.toFixed(2)) : undefined,
                    "netAmount": 0,
                    "roundOff": !!bill.roundOff ? Number(bill.roundOff.toFixed(2)) : 0,
                    "taxAmount": !!bill.totalTaxAmount ? Number(bill.totalTaxAmount.toFixed(2)) : 0,
                    "saleTypeId": 2,
                    "totalSelfPayable":$scope.totalSelfPayable,
                	"totalCreditPayable":$scope.totalCreditPayable,
                	"opSales":true,
                	"isContractChanged":$scope.isContractChanged,
                	"isPayeeUpdated":$scope.isPayeeUpdated,
                	"isSmartApplicable":$scope.isSmartApplicable,
                    "items": [],
                    "consume" :false,
                    "remark": self.model.remark,
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "storeId":$scope.storeId,
                    "addToBill" : addToBill,
                    "atCopToSave":$scope.atCopSelected!=undefined || $scope.atCopSelected!=null ? $scope.atCopSelected : false,
                    "searchPatient":$scope.depositTypeWiseRequired,
                    "entitlementId":$scope.entitlementObj.paymentEntitlementTypeId
                }
                
                $scope.totalOrderedQty=0;
                $scope.finalQuantitySaved=0
                $scope.noOrderQtyTotalSumForUndefined=false;
                var orderedQtyCount=0;
                var issuedQtyCount=0;
                var validateQty = function(item){
               	  var statusCount = 0;
                     
             		if(item.itemId!=null){
             					var totalSame = 0;
             						angular.forEach(bill.items, function (item1, key1) 
                                      {
	              							if(item.itemId == item1.itemId && item1.qtyPrescribed != undefined)
	                                		{
	              								
	              								totalSame += CalculateOrederOrIssuedQty(item1);
	              								
	              								angular.forEach(item1.selectedItems, function (item2, key2) 
		                                                {
					              								totalSame += CalculateOrederOrIssuedQty(item2);
		                                                });
	                                		}
	              							
	              							
                                            if((item1.itemId == item.parentItemId) && (item.parentItemId != undefined) )
                                            {
                                            	if(item1.quantity != null){
                                            		
                                            		totalSame +=  CalculateOrederOrIssuedQty(item1);
                                           		 
                                            	}
                                                
                                                angular.forEach(item1.selectedItems, function (item2, key2) 
                                                         {
                                                                   totalSame += CalculateOrederOrIssuedQty(item2);
                                                         });

                                            }
                                      }); 
             						
                                      if(item.addCopyItemFlag == true){
                                        if (item.qtyMainLine > totalSame) {
                                            statusCount = 1
                                        }
                                      }
                                      else{
                                        if (item.qtyPrescribed > totalSame) {
                                            statusCount = 1
                                        }
                                      } 
                                       if(item.checkNewItem==undefined && item.qtyPrescribed != undefined){
                                    	   var newUOM = [];
                                           if(item.uom == undefined || item.uom == null){
                                            item.uomListData.find(function (value) {
                                                if (!!value.uomTypeId) {
                                                    newUOM.push(value);
                                                }
                                            });
                                           }
                                           else{
                                            item.uom.find(function (value) {
                                                if (!!value.uomTypeId) {
                                                    newUOM.push(value);
                                                }
                                            });
                                           }
                                           if(item.selectedUom != null){
                                           	 var uomDetails = item.selectedUom.uomDetails.split(' ')[0];
                                             if(newUOM[2] == undefined || newUOM[2] == null){
                                                if(item.uoms == undefined || item.uoms == null){
                                                    if (uomDetails == "P") {
                                                        item.finalQuantitySave = Math.ceil(item.quantity * (item.uomListData[0].conversion * item.uomListData[1].conversion * item.uomListData[2].conversion));
                                                    } else if (uomDetails == "S") {
                                                        item.finalQuantitySave = Math.ceil(item.quantity * (item.uomListData[1].conversion * item.uomListData[2].conversion));
                                                    } else if (uomDetails == "D") {
                                                        item.finalQuantitySave = Math.ceil(item.quantity * (item.uomListData[2].conversion));
                                                    }
                                                }
                                                else{
                                                    if (uomDetails == "P") {
                                                        item.finalQuantitySave = Math.ceil(item.quantity * (item.uoms[0].conversion * item.uoms[1].conversion * item.uoms[2].conversion));
                                                    } else if (uomDetails == "S") {
                                                        item.finalQuantitySave = Math.ceil(item.quantity * (item.uoms[1].conversion * item.uoms[2].conversion));
                                                    } else if (uomDetails == "D") {
                                                        item.finalQuantitySave = Math.ceil(item.quantity * (item.uoms[2].conversion));
                                                    }
                                                }
                                             }
                                             else{
                                                if (uomDetails == "P") {
                                                    item.finalQuantitySave = Math.ceil(item.quantity * (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
                                                } else if (uomDetails == "S") {
                                                    item.finalQuantitySave = Math.ceil(item.quantity * (newUOM[1].conversion * newUOM[2].conversion));
                                                } else if (uomDetails == "D") {
                                                    item.finalQuantitySave = Math.ceil(item.quantity * (newUOM[2].conversion));
                                                }
                                             }
                                           }
                                           if(item.finalQuantitySave>item.qtyPrescribed)
                                           {
                                                $scope.finalQuantitySaved+=parseInt(item.qtyPrescribed);   
                                           }
                                           else{
                                                $scope.finalQuantitySaved+=parseInt(item.finalQuantitySave);   
                                           }
                                    	   $scope.totalOrderedQty+=parseInt(item.quantity);
                                    	   

                                       }
                                      

             				}
             		if (statusCount>0) {
							return true;
                     } else if (statusCount == 0) {
                   		return false;
                     }
             	
               }
                angular.forEach(bill.items, function (item, key) {
                    
                	if(item.disabled!=undefined && item.disabled!=null && !item.disabled)
                    {
                		if(!item.parentIssueDetailsId)
                    	{
                                orderedQtyCount++
                    	}
                    }
//                	 && item.selectedBatch!=null
                        if (!item.disabled && !item.itemDisabled) {
                        	if(item.quantity !=undefined && item.quantity !=null && item.quantity > 0 && item.quantity !=""){
                               	issuedQtyCount++;
                               	if(item.selectedBatch != undefined && item.selectedBatch != null){
                               		var result = validateQty(item);
                                    var status;
                                    $scope.totalPriscribedQuantity+=parseInt(item.qtyPrescribed);
                                    if(item.qtyPrescribed != undefined){
                                        if (result) {
                                            status = 'Partially Completed';
                                            statusFinalFlag = true;
                                        } else if (!result) {
                                            status = 'Completed';
                                        }
                                    }else{
                                        status = 'Completed';
                                    	
                                    }
                                    	var billItem = 
                                        {
                                            "itemId": item.itemId,
                                            "statusId": SetStatus(status),
                                            "prescriptionDetailId": item.prescriptionDetailId,
                                            "batchId": item.selectedBatch.batchId,
                                            "uomTypeId": item.selectedUom.uomTypeId,
                                            "uomUnitId": item.selectedUom.uomUnitId,
                                            "rate": Number(item.rate.toFixed(2)),
                                            "quantity": Number(item.quantity),
                                            "leaseRate": Number(item.selectedBatch.sellingPrice.toFixed(2)),
                                            "leaseQuantity": item.leaseQuantity,
                                            "totalAmount": Number(item.itemtotalAmount.toFixed(2)),
                                            "discountAmount": Number(item.itemDiscount.toFixed(2)),
                                            "unitDiscountAmount": item.unitDiscountAmount ? Number(item.unitDiscountAmount.toFixed(2)) : 0.00,
                                            "netAmount": Number(item.netAmount.toFixed(2)),
                                            "taxId": item.taxId,
                                            "taxPercentage": item.taxPercentage ? Number(item.taxPercentage.toFixed(2)) : 0.00,
                                            "taxAmount": Number(item.taxAmount),
                                            "creditPayable": Number(item.creditPayable),
                                            "selfPayable": Number(item.selfPayable.toFixed(2)),
                                            "productCategoryId":item.productCategoryId,
                                            "payeeId":item.payeeId,
                                            "packageConsumptionAmt" : item.packageConsumptionAmt,
                                            "saleDetailPayeeMapperDtoList" :item.saleDetailPayeeMapperDtoList, 
                                            "saleDetailPackageMapperDtoList": item.saleDetailPackageMapperDtoList,
                                            "consessionPercentage": item.patientClassPercentage,
                                            "consessionAmount": item.patientClassAmount,
                                            "unitConsessionAmount": item.unitPatientClassAmount!=null ? item.unitPatientClassAmount.toFixed(2):0,
                                            "consumedItemId" : item.consumedItemId,
                                            "consumedPcategroyId" : item.consumedPcategroyId,
                                            "batchAllocationId":item.allocationId,
                                            "atCop":$scope.atCopSelected!=undefined || $scope.atCopSelected!=null ? $scope.atCopSelected : false
                                        }
                                    	//Ganesh
                                        $scope.totalSelfPayable = $scope.totalSelfPayable+ item.selfPayable;
                                        $scope.totalCreditPayable = $scope.totalCreditPayable+ item.creditPayable;
                                        data.totalAmount = data.totalAmount + billItem.totalAmount;
                                        // data.netAmount = parseFloat(data.netAmount + billItem.netAmount);
                                        data.netAmount = parseFloat(bill.netAmount);
                                        data.items.push(billItem);
                               	}
                        	}
                        }

                        
                        if (item.selectedItems && !item.itemDisabled) {
                        	if(item.disabled!=undefined && item.disabled!=null && item.disabled)
                            {
                            			orderedQtyCount++;
                            }
                            angular.forEach(item.selectedItems, function (item1, key) {
                            	
                                if(!item1.itemDisabled  && item1.selectedBatch!=null){
                                	if(item1.quantity !=undefined && item1.quantity !=null && item1.quantity > 0 && item1.quantity !=""){
                                //ChangeItemStatus(item);
                                		if(item.disabled!=undefined && item.disabled!=null && item.disabled)
                                        {
                                		issuedQtyCount++;
                                        }
                                var result = validateQty(item1);
                                if (result) {
                                    status = 'Partially Completed';
                                    statusFinalFlag = true;
                                } else if (!result) {
                                    status = 'Completed';
                                }
                                var billSubItem = {
                                		 "batchAllocationId":item1.allocationId,
                                        "itemId": item1.itemId,
                                        "statusId": 4,
                                        "prescriptionDetailId": null,
                                        "batchId": item1.selectedBatch.batchId,
                                        "uomTypeId": item1.selectedUom.uomTypeId,
                                        "uomUnitId": item1.selectedUom.uomUnitId,
                                        "rate": item1.rate,
                                        "quantity": Number(item1.quantity),
                                        "leaseRate": item1.selectedBatch.sellingPrice,
                                        "leaseQuantity": item1.leaseQuantity,
                                        "totalAmount": item1.itemtotalAmount,
                                        "discountAmount": item1.itemDiscount,
                                        "unitDiscountAmount": item1.unitDiscountAmount ? parseFloat(item1.unitDiscountAmount).toFixed(2) : 0.00,
                                        "netAmount": item1.netAmount ? parseFloat(item1.netAmount).toFixed(2) : 0,
                                        "taxId": item1.taxId,
                                        "taxPercentage": item1.taxPercentage ? parseFloat(item1.taxPercentage).toFixed(2) : 0,
                                        "taxAmount": item1.taxAmount ? parseFloat(item1.taxAmount).toFixed(2) : 0,
                                        "creditPayable":item1.creditPayable ? parseFloat(item1.creditPayable).toFixed(2) : 0,
                                        "selfPayable":item1.selfPayable ? parseFloat(item1.selfPayable).toFixed(2) : 0,
                                        "productCategoryId":item1.productCategoryId,
                                        "payeeId":(parseFloat(item1.creditPayable)==0)?$scope.selfPayeeId:item1.payeeId,
                                        "packageConsumptionAmt" : item1.packageConsumptionAmt,
                                        "consessionPercentage": item1.patientClassPercentage,
                                        "consessionAmount": item1.patientClassAmount,
                                        "unitConsessionAmount": item1.unitPatientClassAmount.toFixed(2),
                                        "parentItemId": item1.parentItemId,
                                        "saleDetailPayeeMapperDtoList":item1.saleDetailPayeeMapperDtoList,	
                                        "saleDetailPackageMapperDtoList": item1.saleDetailPackageMapperDtoList,
                                        "consumedItemId" : item1.consumedItemId,
                                        "consumedPcategroyId" : item1.consumedPcategroyId,
                                        "atCop":$scope.atCopSelected!=undefined || $scope.atCopSelected!=null ? $scope.atCopSelected : false
                                    }
                                //Ganesh
                                $scope.totalSelfPayable = $scope.totalSelfPayable+ item1.selfPayable;
                                $scope.totalCreditPayable = $scope.totalCreditPayable+ item1.creditPayable;
                                data.totalAmount = data.totalAmount + billSubItem.totalAmount;
                                data.netAmount = parseFloat(bill.netAmount);
                                data.items.push(billSubItem);
                                }
                            }
                            });
                        }
                        //Ganesh
                        data.totalSelfPayable = $scope.totalSelfPayable;
                        data.totalCreditPayable =  $scope.totalCreditPayable;
                  
                });
               
                var billStatus='Completed';

                 
                if($scope.orderQtyTotalSum == $scope.totalOrderedQty){

                	statusFinalFlag = false;
                }
               
                else{
                	statusFinalFlag = true;
                }
                
                if($scope.removeItemFlag){
                	   billStatus ='Partially Completed';
                }else{
                  
                                if (statusFinalFlag == true)
                                {
                                    billStatus ='Partially Completed';
                                }
                                
                }
                if($scope.finalQuantitySaved >= $scope.orderQtyTotalSum && issuedQtyCount >= $scope.orderListCount)
            	{
                	billStatus='Completed';
            	}
                else
            	{
                	billStatus ='Partially Completed';
            	}
                data.statusId=SetStatus(billStatus);
                self.model.WorklistBillDetails = data;
                $rootScope.startLoader();
                console.log("savedData",data);
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        ClearFields(bill);
                        $scope.orderQtyTotalSum=0;
                        $rootScope.stopLoader();
                    	printOutPatientSalesWorklistDetailsReport();
                        if($scope.cookieObjectskipPharmacySettlement == true)
                    	{
                    	growl.success('Sales worklist bill Successfully saved');
                    	 $state.go('outPatientSalesWorklist');
                    	}
                    else
                    	{
                    	if(addToBill==true)
                        {
                            growl.success('Sales worklist bill added Successfully');
                            $state.go('outPatientSalesWorklist');    
                        }
                        else
                        {
                           self.model.worklistBillId = response.data.id;
                           GetBillDetails();     
                        }
                    	}
                        $scope.btnDisabled = false;
                      
                    },
                    function (err) {
                    	$scope.saveOPSaleFlag = true;
                        $rootScope.stopLoader();
                        if(err.data.summary != undefined && err.data.summary != null && err.data.summary == "Stock is not available"){
                            angular.forEach(bill.items, function(b){
                                b.flag = false;
                                if(b.selectedItems){
                                    angular.forEach(b.selectedItems, function(i){
                                        i.flag = false;
                                    });
                                }
                            });
                            angular.forEach(err.data.errors, function(e){
                                angular.forEach(bill.items, function(d){
                                    if(d.selectedItems){
                                        angular.forEach(d.selectedItems, function(s){
                                            if(s.flag == false){
                                                s.summary = '';    
                                                if(s.selectedBatch != undefined && s.selectedBatch != null && s.selectedBatch != "" && s.selectedBatch.batchNo == e.batchNo){
                                                    s.flag = true;
                                                    s.summary = "Enough stock not available";
                                                    s.newStock = e.currentStock;
                                                    $scope.btnDisabled = true;
                                                }
                                            }
                                        });
                                    }
                                    
                                    if(d.flag == false){
                                        d.summary = '';
                                        if(d.selectedBatch != undefined && d.selectedBatch != null && d.selectedBatch != "" && d.selectedBatch.batchNo == e.batchNo){
                                            d.summary = "Enough stock not available";
                                            d.newStock = e.currentStock;
                                            d.flag = true;
                                            $scope.btnDisabled = true;
                                            var popup = angular.element('#confirmSave');
                                            popup.modal('hide');
                                        }	
                                    }
                                });
                            });
                                var popup = angular.element('#confirmSave');
                                popup.modal('hide');
                            }
                            else{
                                if(err.data.summary != undefined && err.data.summary != null){
                                    growl.error('Stock not available for the some of the items.');
                                    $scope.saveBtnDisabled = false;
                                    $scope.btnDisabled = false;
                                }
                                else{
                                	
                                	 if(err.data.message != undefined && err.data.message != null){
                                         growl.error(err.data.message);
                                         $scope.saveBtnDisabled = false;
                                         $scope.btnDisabled = false;
                                     }else{
                                         growl.error('Error while saving sales worklist bill.');
                                         $scope.saveBtnDisabled = false;
                                         $scope.btnDisabled = false;
                                     }
                                }
                            }
                        });
                    }
            else
            	{
            	$scope.saveOPSaleFlag = true;
            	}
                    
		            $scope.saveBtnDisabled = false;
		            $scope.btnDisabled = false;
                }

        var ClearFields = function (bill) {

            bill.Id = "1" + (new Date()).getTime();
            bill.uhid = null;
            bill.patientDetail = null;
            bill.patientDob = null;
            bill.wardName = null;
            bill.bedNo = null;
            bill.prescriptionDate = null;
            bill.doctorDetail = null;
            bill.discountTypeId = null;
            bill.discountCatgeory = self.model.discountCategoryList[0];
            bill.discount = null;
            bill.itemtotalAmount = null;
            bill.totalDiscountAmount = null;
            bill.taxAmount = null;
            bill.netAmount = null;
            bill.remark = self.model.remark;
            bill.items = [];
            bill.searchBatch = null;
            
            //Ganesh
            $scope.totalSelfPayable = 0;
            $scope.totalCreditPayable = 0;
            self.model.patientId = null;
            
            //Ganesh
            $scope.encounterId = null;
            $scope.patientId = null;
            $scope.initPaymentEntitlement();
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

        var ValidateBatch = function (parentItem, item) {
            var isValid = true;
            var count = 0;
            if (!!parentItem) {
                angular.forEach(parentItem.items, function(data){
                	if(!data.disabled && !data.itemDisabled){
                    if(data.selectedBatch == undefined){
                        parentItem['isParentBatchValid'] = true;
                        growl.error("Please Select Batch");
                        isValid = false;
                    }
                    angular.forEach(self.model.worklistDetails.items, function(d){
                        if(d.quantity){
                            count++;
                        }
                    });
                    if(count == 0){    
                        if(data.quantity == 0){
                            parentItem['isParentQuantityValid'] = true;
                            isValid = false;
                            growl.error('Quantity cannot be zero');
                        }
                        // if(data.quantity == ''){
                        //     parentItem['isParentQuantityValid'] = true;
                        //     growl.error("Please enter quantity");
                        //     isValid = false;
                        // }
                    }
                	}else {
                        ValidateUniqueBatchAndUom(item)
                    }
                })
            }
            // if (!!parentItem) {
            //     if(parentItem.selectedBatch) {
            //         if (!parentItem.selectedBatch.batchId) {
            //             parentItem['isParentBatchValid'] = true;
            //             growl.error("Please Select Batch");
            //             isValid = false;
            //         }
            //     }
            // }
            // if (!!item) {
            //     if (!item.selectedBatch.batchId) {
            //         item['isBatchValid'] = true;
            //         growl.error("Please Select Batch");
            //         isValid = false;
            //     }
            // }
            return isValid;
        }

        var ValidateUOM = function (item) {
            var isValid = true;
            if (!!item) {
                if (!item.selectedUom.uomUnitId) {
                    item['isUOMValid'] = true;
                    growl.error("Please Select UOM");
                    isValid = false;
                }
            }
            return isValid;
        }
        
        $scope.substituteButtonFlag = false;
        var ValidateQuantity = function (parentItem, parentIndex) {
        	parentItem.substituteButtonFlag1=false;
            if(parseInt(parentItem.quantity)>= parseInt(parentItem.newQty != undefined && parentItem.newQty != null && parentItem.newQty != 0 ? parentItem.newQty : parentItem.qtyPrescribed))
            {
                parentItem.quantityIssuedFlag =true;      
            }
            else
            {
                parentItem.quantityIssuedFlag =false;      
            }
            if(parentItem.quantity != null  && parseInt(parentItem.newStock)<parseInt(parentItem.quantity) && (parentItem.disabled != true|| parentItem.itemDisabled != true) )
            {
            	parentItem.quantityIssuedFlag =false; 
                growl.error(" Please enter quantity less than or equals to Available stock quantity.");
                parentItem.quantity = "0";
                return false;
            }
        	if(parentItem.quantity==undefined)
        	{
        		parentItem.substituteButtonFlag1=false;
        	}
        	
        	if(parentItem.quantity!=null && parentItem.quantity!=undefined)
            {
                var isValid = true;
                var count = 0;
                $scope.substituteButtonFlag = false;
                if (!!parentItem && parentItem.qtyPrescribed != undefined) {
                    angular.forEach(self.model.worklistDetails.items, function(d){
                        if(d.quantity){
                            count++;
                        }
                        d["substituteButtonFlag1"]=false;
                        if(d.newQty==parseInt(d.quantity)){
                    	    d["substituteButtonFlag1"]=true;
                        }
                    });
                    if(count == 0 && parentItem.disabled==false){
                        if (parseInt(parentItem.quantity) == 0) {
                            parentItem['isParentQuantityValid'] = true;
                            isValid = false;
                            growl.error('Quantity cannot be zero');
                        }
                    }
                
                    if(parentItem.isThreeUOMApplicable == false){
                        if(parentItem.selectedUom.uomTypeId == 3)
                        {
                            $scope.quantityIssuedNew  = parentItem.quantity;
                            parentItem.quantity = parseInt(parentItem.quantity);
                            isValid = true;
                           /* if (parseInt(parentItem.quantity) > parentItem.qtyPrescribed) {
                                parentItem.isParentQuantityValid = true;
                                growl.error("Please enter quantity less than or equal to ordered quantity");
                                parentItem.quantity = "0";
                                Calculate(parentItem, self.model.worklistDetails);
                                isValid = false;
                            } else {
                                parentItem.quantity = parseInt(parentItem.quantity);
                                isValid = true;
                            }  */
                        } 
                        else  if(parentItem.selectedUom.uomTypeId == 2)
                        {
                            if(parentItem.uoms == undefined || parentItem.uoms == null || parentItem.uoms == ""){
                                var stripQty = parentItem.uom[1].conversion * parseInt(parentItem.quantity);
                                $scope.quantityIssuedNew  = stripQty;  
                            }
                            else{
                                var stripQty = parentItem.uoms[1].conversion * parseInt(parentItem.quantity);
                                $scope.quantityIssuedNew  = stripQty;  
                            }  
                            
                            parentItem.quantity = parseInt(parentItem.quantity);
                            isValid = true;
                           /* if(parseInt(parentItem.quantity) > parentItem.newQty){
                                parentItem.isParentQuantityValid = true;
                                growl.error("Please enter quantity less than or equal to ordered quantity");
                                parentItem.quantity = "0";
                                Calculate(parentItem, self.model.worklistDetails);
                                isValid = false;
                            }else{
                                parentItem.quantity = parseInt(parentItem.quantity);
                                isValid = true;
                            }    */
                        }  
                        else  if (parentItem.selectedUom.uomTypeId == 1)
                        {
                            if(parentItem.uoms == undefined || parentItem.uoms == null || parentItem.uoms == ""){
                                if(parentItem.uom == undefined || parentItem.uom == null){
                                    var boxQty = parentItem.uomListData[0].conversion * parentItem.uomListData[1].conversion * parseInt(parentItem.quantity);
                                    $scope.quantityIssuedNew  =  boxQty;
                                }
                                else if(parentItem.uom[2] == undefined || parentItem.uom[2] == null){
                                    var boxQty = parentItem.uomListData[1].conversion * parentItem.uomListData[2].conversion * parseInt(parentItem.quantity);
                                    $scope.quantityIssuedNew  =  boxQty;
                                }
                                else{
                                    var boxQty = parentItem.uom[1].conversion * parentItem.uom[2].conversion * parseInt(parentItem.quantity);
                                    $scope.quantityIssuedNew  =  boxQty;
                                }
                            }
                            else{
                                if(parentItem.uoms[2] == undefined || parentItem.uoms[2] == null){
                                    var boxQty = parentItem.uomListData[1].conversion * parentItem.uomListData[2].conversion * parseInt(parentItem.quantity);
                                    $scope.quantityIssuedNew  =  boxQty;
                                }
                                else{
                                    var boxQty = parentItem.uoms[1].conversion * parentItem.uoms[2].conversion * parseInt(parentItem.quantity);
                                    $scope.quantityIssuedNew  =  boxQty;
                                }
                            }
                            
                            parentItem.quantity = parseInt(parentItem.quantity);
                            isValid = true;
                           /* if (parseInt(parentItem.quantity) > parentItem.newQty) {
                                parentItem.isParentQuantityValid = true;
                                growl.error("Please enter quantity less than or equal to ordered quantity");
                                parentItem.quantity = "0";
                                Calculate(parentItem, self.model.worklistDetails);
                                isValid = false;
                            }
                            else {
                                parentItem.quantity = parseInt(parentItem.quantity);
                                isValid = true;
                            }   */
                        }
                    }
                    else{
                        if(parentItem.selectedUom.uomTypeId == 3)
                        {
                        	parentItem.quantity = parseInt(parentItem.quantity);
                            isValid = true;
                           /* $scope.quantityIssuedNew  = parentItem.quantity;
                            if (parseInt(parentItem.quantity) > parentItem.qtyPrescribed) {
                                parentItem.isParentQuantityValid = true;
                                growl.error("Please enter quantity less than or equal to ordered quantity");
                                parentItem.quantity = "0";
                                Calculate(parentItem, self.model.worklistDetails);
                                isValid = false;
                            } else {
                                parentItem.quantity = parseInt(parentItem.quantity);
                                isValid = true;
                            }  */
                        } 
                        else  if(parentItem.selectedUom.uomTypeId == 2)
                        {
                            var stripQty = parentItem.uom[1].conversion * parseInt(parentItem.quantity);
                            $scope.quantityIssuedNew  = stripQty;    
                            parentItem.quantity = parseInt(parentItem.quantity);
                            isValid = true;
                         /*   if(parseInt(parentItem.quantity) > parentItem.newQty){
                                parentItem.isParentQuantityValid = true;
                                growl.error("Please enter quantity less than or equal to ordered quantity");
                                parentItem.quantity = "0";
                                Calculate(parentItem, self.model.worklistDetails);
                                isValid = false;
                            }else{
                                parentItem.quantity = parseInt(parentItem.quantity);
                                isValid = true;
                            }   */ 
                        }  
                        else  if (parentItem.selectedUom.uomTypeId == 1)
                        {
                            var boxQty = parentItem.uom[1].conversion * parentItem.uom[2].conversion * parseInt(parentItem.quantity);
                            $scope.quantityIssuedNew  =  boxQty; 
                            parentItem.quantity = parseInt(parentItem.quantity);
                            isValid = true;
                            /*if (parseInt(parentItem.quantity) > parentItem.newQty) {
                                parentItem.isParentQuantityValid = true;
                                growl.error("Please enter quantity less than or equal to ordered quantity");
                                parentItem.quantity = "0";
                                Calculate(parentItem, self.model.worklistDetails);
                                isValid = false;
                            }
                            else {
                                parentItem.quantity = parseInt(parentItem.quantity);
                                isValid = true;
                            } */  
                        }
                    }
                     
                
                    if( isValid == true){
                        var finalOrdQty = 0;
                        var totalIssuedQty = 0;
                	  
                	    if (parentIndex == undefined) {
                            angular.forEach(self.model.worklistDetails.items, function (value, index) {
                                if(value.itemId == parentItem.itemId){  		
                                    totalIssuedQty += CalculateOrederOrIssuedQty(value);
                                    if(value.selectedItems != null && value.selectedItems.length>0){
                                        angular.forEach(value.selectedItems, function (selectValue, selectIndex)  {
                                            totalIssuedQty += CalculateOrederOrIssuedQty(selectValue);
                                        });
                                    }
                                } 
                            });  
                            finalOrdQty = parentItem.finalOrderedQty;
                        } 
                        else {	 
                            if(self.model.worklistDetails.items[parentIndex].disabled == undefined){
                                totalIssuedQty += CalculateOrederOrIssuedQty(self.model.worklistDetails.items[parentIndex]);
                            }             	
                            finalOrdQty = self.model.worklistDetails.items[parentIndex].finalOrderedQty;
                            if(self.model.worklistDetails.items[parentIndex].selectedItems != null && self.model.worklistDetails.items[parentIndex].selectedItems.length>0){           		
                                angular.forEach(self.model.worklistDetails.items[parentIndex].selectedItems, function (selectValue, selectIndex) {
                                    totalIssuedQty += CalculateOrederOrIssuedQty(selectValue);
                                });
                            }
                        }
                	    $scope.totalQuantityIssued=0;
                	    $scope.subNewQty=0;
            	        $scope.subIssuedQty=0;
            	        $scope.mainNewQty=0;
          	            $scope.mainIssuedQty=0;
                    	angular.forEach(self.model.worklistDetails.items, function (value, index) {
                    		if(parentItem.parentItemId)
                            {
                    			if(value.issuedQuantity==undefined || value.issuedQuantity==null)
          		        		{
          		           		    value.issuedQuantity=0
          		        		}
                    			if(value.itemId == parentItem.parentItemId && value.issuedQuantity<1){
                    				if(!value.itemDisabled)
          			        		{
                            		    $scope.totalQuantityIssued += parseInt(value.quantity!=null ? value.quantity :0);
                                        $scope.mainIssuedQty=$scope.mainIssuedQty+parseInt(value.quantityIssued);
                                        if(!value.isMultiBatch)
                			        	{
                			            	$scope.mainNewQty=$scope.mainNewQty+value.newQty;
                			        	}
                			          /*  if($scope.mainIssuedQty>$scope.mainNewQty)
              			        	    {
              			        	        growl.warning("  warning !! enter quantity is greater than ordered quantity ");
              			        	        value.quantity = "0";
                  			                return false;
              			        	    }*/
          			        		}
                            		if(value.selectedItems != null && value.selectedItems.length>0){
                                        angular.forEach(value.selectedItems, function (selectValue, selectIndex)  {
                                			if(!value.finalOrderedQty)
                                            {
                                				finalOrdQty = selectValue.finalOrderedQty;
                                            }
                                			if(!selectValue.itemDisabled)
                  			        		{
                                    			$scope.totalQuantityIssued += parseInt(selectValue.quantity!=undefined || selectValue.quantity!=null ? selectValue.quantity :0);
                                	    		$scope.subIssuedQty=$scope.subIssuedQty+parseInt(selectValue.quantity);
                          			            if(!selectValue.isMultiBatch)
                          			        	{
                          			            	$scope.subNewQty=$scope.subNewQty+selectValue.newQty;
                          			        	}
                          			           /* if($scope.subIssuedQty>$scope.subNewQty)
                          			        	{
                          			        	    growl.warning("  warning !! enter quantity is greater than ordered quantity ");
                          			        	    selectValue.quantity = "0";
                              			            return false;
                          			        	}*/
                  			        		}
                               		    });
                                    }
                            	}
                            }
                    		else
                            {
                    			if(value.issuedQuantity==undefined || value.issuedQuantity==null)
          		        		{
          		          		    value.issuedQuantity=0
          		        		}
                    			if(value.itemId == parentItem.itemId && value.issuedQuantity<1){
                    				if(!value.itemDisabled)
          			        		{
                            		    $scope.totalQuantityIssued += parseInt(value.quantity!=null ? value.quantity :0);
                            		    $scope.mainIssuedQty=$scope.mainIssuedQty+parseInt(value.quantity);
                			        if(!value.isMultiBatch)
                                    {
                			        	$scope.mainNewQty=$scope.mainNewQty+value.newQty;
                                    }
                			       /* if($scope.mainIssuedQty>$scope.mainNewQty)
              			        	{
              		    	        	growl.warning("  warning !! enter quantity is greater than ordered quantity ");
              			            	value.quantityIssued = "0";
                  			            return false;
              			        	}*/
                                }
                                if(value.selectedItems != null && value.selectedItems.length>0){          		
                                    angular.forEach(value.selectedItems, function (selectValue, selectIndex)  {
                                        if(!value.finalOrderedQty)
                                        {
                            				finalOrdQty = selectValue.finalOrderedQty;
                                        }
                                        if(!selectValue.itemDisabled)
                                        {
                                			$scope.totalQuantityIssued += parseInt(selectValue.quantity!=undefined || selectValue.quantity!=null ? selectValue.quantity :0);
                                			$scope.subIssuedQty=$scope.subIssuedQty+parseInt(selectValue.quantity);
                          			        if(!selectValue.isMultiBatch)
                                            {
                          			        	$scope.subNewQty=$scope.subNewQty+selectValue.newQty;
                                            }
                          			       /* if($scope.subIssuedQty>$scope.subNewQty)
                                            {
                          			        	growl.warning("  warning !! enter quantity is greater than ordered quantity ");
                          			        	selectValue.quantity = "0";
                              			        return false;
                                            }*/
                                        }
                                    });
                                }
                            }
                        }
                    }); 
                    angular.forEach(self.model.worklistDetails.items, function (value, index) {
                        if(value.quantity != null && value.newdispQty != undefined && parseInt(value.newStock)<parseInt(value.quantity) &&  (value.disabled != true|| value.itemDisabled != true))
                        {
                            value.substituteButtonFlag1=false;
                            growl.error(" Please enter quantity less than or equals to Available stock quantity.");
                            value.quantity = "0";
                        }
                      /*  if(value.checkNewItem==true)
                        {
                            if(parseInt(value.stockAvailable)<parseInt(value.quantity))
                            {
                                value.substituteButtonFlag1=false;
                                growl.warning("  warning !! enter quantity is greater than ordered quantity ");
                                value.quantity = "0";
                            }
                        }
                        if(value.checkNewItem==true)
                        {
                            if(parseInt(value.newStock)<parseInt(value.quantity))
                            {
                                value.substituteButtonFlag1=false;
                                growl.warning("  warning !! enter quantity is greater than ordered quantity ");
                                value.quantity = "0";
                            }
                        }*/
                    });
                    
                    parentItem.quantity = parseInt(parentItem.quantity);
                  /*  if (finalOrdQty < $scope.totalQuantityIssued) {
                        parentItem.isParentQuantityValid = true;
                        growl.warning("  warning !! enter quantity is greater than ordered quantity ");
                        parentItem.quantity = "0";
                        if(parentItem.substituteBtn !=undefined && parentItem.substituteBtn == true){
                            parentItem.quantity = "0";
                        }
                    }else {	
                        parentItem.quantity = parseInt(parentItem.quantity);
                    }*/
                }  
            }
            if(parentItem.newdispQty!= undefined &&  parentItem.newStock<parentItem.quantity && (parentItem.disabled != true|| parentItem.itemDisabled != true))
            {
                growl.error("Please Enter Quantity Less Than Or Equal To Stock Available ");
                parentItem.quantity = "0";
                isValid=false;
            }
            parentItem.substituteButtonFlag1=false;
            return isValid;
        }
        else if(parentItem.quantity==null || parentItem.quantity==undefined)
        {
            $scope.substituteButtonFlag=false
        }
        	parentItem.substituteButtonFlag1=false;
    }

        var ValidateBill = function (bill) {

            var isValid = true;
            var count = 0;

            if (self.model.worklistDetails.discount) {
                if (!self.model.isDiscountApplied) {
                    isValid = false;
                    growl.error('Please apply discount on the bill items');
                }
            }
            if (self.model.worklistDetails.discount) {
                if (!bill.discountCatgeory.id) {
                    isValid = false;
                    self.model.isDiscountCategoryValid = true;
                }

                if (!self.model.isDiscountApplied) {
                    isValid = false;
                    self.model.isDiscountAppliedValid = true;
                    growl.error('Please apply discount on the bill items');
                }

                if (self.model.discountTypeFlag == true) {
                    isValid = false;
                    self.model.isDiscountAppliedValid = true;
                    growl.error('Please apply discount on the bill items');
                }

                if (self.model.isAppliedDiscountValid == true) {
                    isValid = false;
                    growl.error('Please apply discount on the bill items');
                }
            }
            
            if(self.model.worklistDetails.discountCatgeory != undefined && self.model.worklistDetails.discountCatgeory != null){
            	if (self.model.worklistDetails.discountCatgeory.id) {
                    var found = self.model.worklistDetails.items.find(item => item.discountPerItem == 0)
                    if (!self.model.worklistDetails.discount && found!=undefined) {
                        isValid = false;
                        self.model.isDiscountAmountValid = true;
                        growl.error('Please enter discount amount');
                        $scope.btnF = false;
                    }
                    if (!self.model.worklistDetails.discount) {
                        isValid = false;
                        self.model.isDiscountAmountValid = true;
                        growl.error('Please enter discount amount');
                        $scope.btnF = false;
                    }
                }
            }

            if (!bill.items || bill.items.length <= 0) {
                isValid = false;
                growl.error("Please add some items in the bill");
            } else {
                bill.items.forEach(function (billitem) {

                    var totalQtyOfSelectedItems = 0;
                    if (billitem.selectedItems && !billitem.itemDisabled) {
                        billitem.selectedItems.forEach(function (item) {
                        	if(!item.itemDisabled){
                        		// if(item.quantity !=undefined && item.quantity !=null && item.quantity > 0 && item.quantity !=""){
                            item['isItemValid'] = false;
                            item['isBatchValid'] = false;
                            item['isUOMValid'] = false;
                            item['isQuantityValid'] = false;

                            if (!item.selectedBatch.batchId || !item.selectedUom.uomUnitId || !item.quantity) {
                                item['isItemValid'] = true;
                                isValid = false;
                            }
                            if (!item.selectedBatch.batchId) {
                                item['isBatchValid'] = true;
                                isValid = false;
                            }
                            if (!item.selectedUom.uomUnitId) {
                                item['isUOMValid'] = true;
                                isValid = false;
                            }

                            totalQtyOfSelectedItems = totalQtyOfSelectedItems + parseInt(item.quantity);
                            angular.forEach(self.model.worklistDetails.items, function(d){
                                if(d.quantity){
                                    count++;
                                }
                                angular.forEach(d.selectedItems, function(subItem){
                                	if(subItem.quantity){
                                        count++;
                                    }
                                });
                            });
                             if(count == 0){
                                 if (!item.quantity) {
                                     // totalQtyOfSelectedItems = totalQtyOfSelectedItems + parseInt(item.quantity);
                                     item['isQuantityValid'] = true;
                                     isValid = false;
                                     growl.error('Please enter quantity')
                                 }
                             }
    
                            if (!ValidateUniqueBatchAndUom(item)) {
                                isValid = false;
                            }
                        	// }
                        }
                        });
                        
                        billitem.selectedItems.forEach(function (item) {
                            item['isQuantityValid'] = false;
                        });

                       /* if (self.model.selectedQuantity < totalQtyOfSelectedItems) {
                            billitem.selectedItems.forEach(function (item) {
                                item['isQuantityValid'] = true;
                                growl.error('Entered quantity is exceeding the ordered quantity')
                            });
                        } else {
                            billitem.selectedItems.forEach(function (item) {
                                item['isQuantityValid'] = false;
                            });
                        }*/
                    }

                    if (!billitem.disabled && !billitem.itemDisabled) {
                    	// if(billitem.quantity !=undefined && billitem.quantity !=null && billitem.quantity > 0 && billitem.quantity !=""){
                        billitem['isParentItemValid'] = false;
                        billitem['isParentBatchValid'] = false;
                        billitem['isParentUOMValid'] = false;
                        billitem['isParentQuantityValid'] = false;
                        if (!billitem.disabled && !billitem.itemDisabled){
                        	  angular.forEach(self.model.worklistDetails.items, function(d){
                                  if(d.quantity){
                                      count++;
                                  }
                                  angular.forEach(d.selectedItems, function(subItem){
                                  	if(subItem.quantity){
                                          count++;
                                      }
                                  });
                              });
                        }
                         if(count == 0){    
                             if (!billitem.quantity) {
                                 billitem['isParentQuantityValid'] = true;
                                 isValid = false;
                                 growl.error('Please enter quantity');
                             }
                         }
                        if(billitem.selectedBatch){
                            if (!billitem.selectedBatch.batchId || !billitem.selectedUom.uomUnitId) {

                                billitem['isParentItemValid'] = true;
                                isValid = false;
                            }
                            if(billitem.issuedQty){
                                if (!billitem.selectedBatch.batchId) {
                                    billitem['isParentBatchValid'] = true;
                                    isValid = false;
                                    growl.error('Please select batch');
                                }
                            }
                            
                            if (!billitem.selectedUom.uomUnitId) {
                                billitem['isParentUOMValid'] = true;
                                isValid = false;
                                growl.error('Please select uom');
                            }
                        }
                        else{
                        	// if(count == 0){
                        	// 	  billitem['isParentBatchValid'] = true;
                            //       isValid = false;
                            //       growl.error('Please select batch');
                        	// }
                          
                        }
                        // angular.forEach(self.model.worklistDetails.items, function(d){
                            // if(.quantity){
                            //     count++;
                            // }
                        // });
                        
                    // }
                    }

                    if (!ValidateUniqueBatchAndUom(billitem)) {
                        isValid = false;
                    }
                    // if (!ValidateBatch(billitem)) {
                    //     isValid = false;
                    // }
                    // if (!ValidateUOM(billitem)) {
                    //     isValid = false;
                    // }
                    // if (!ValidateQuantity(billitem)) {
                    //     isValid = false;
                    // }
                    // if (!ValidateMaxOrderQuantity(billitem)) {
                    //     isValid = false;
                    // }
                });
            }
            if(!isValid)
        	{
        	$scope.saveBtnDisabled=false;
        	$scope.btnDisabled = false;
        	}
            return isValid;
        }

        var SetFocus = function () {
            self.model.barcode = undefined;
            $window.document.getElementById('barcode').focus();
        }
        
        $scope.barcodeScanned= false;
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
     	            angular.forEach(response.data, function (value, key) {
     	                if(response.data[0].isManualBarcodeRequired==true)
     		            {
     	                	if(response.data[0].isManualBarcodeRequired==true )
     	                	{
     		 	        	  if(value.prePrintedBarcode==self.model.barcode){
     		 	        		 flag=true;
     		 	                self.model.barcode="";
     		 	              $scope.barcodeScanned= true;
     		 	              AllocateBatch(self.model.worklistDetails,null, value.batchId);
     		 	        	  }
     		 	        	  else{
     		 	        		 flag=true;
     		 	        		 self.model.barcode="";
     		 	 				growl.error( "Enter Valid Barcode..!",
     		 	 				{
     		 	 					title: "Error"
     		 	 				});
     		 	        	  }
     		 		          
     		 		        }
     		 	         else {
     		 				flag=true;
     		 				self.model.barcode="";
     		 				growl.error( "Enter Valid Barcode..!",
     		 				{
     		 					title: "Error"
     		 				});
     		 			}
     		            }
     	            });
     	            if(flag==false)
     		        {
                        if (!!barcode) {
//                            var itemId = barcode.split(' ')[0];
                            var batchId = barcode;
                            $scope.barcodeScanned= true;
                            self.model.barcode="";
                            AllocateBatch(self.model.worklistDetails,null, batchId);
                            event.stopPropagation();
                            event.preventDefault();
                            SetFocus();
                        }
                    }
                });
            }
        }

        var AddBatchToDetail = function (worklist, itemId, batchId) {
            var itemId = itemId;
            var batchId = batchId;
            angular.forEach(self.model.worklistDetails.items, function (item, key) {

                let temp = item.batch.find(function (batch) {
                    if (batch.batchId == batchId) {
                        item.selectedBatch = batch;
                        item.selectedUom = item.selectedBatch.uom.find(function (obj) {
                            return obj.opUom;
                        });
                        CalculateRate(item, item.selectedUom, item.uom)
                        item.quantity = item.quantity + 1;
                    }
                });

            });
        }

        var NavigateToOutPatientSalesWorklist = function () {
            document.getElementById('popupClose').click();
            setTimeout(function () {
                $state.go('outPatientSalesWorklist');
            }, 1000);
        }

        var GetBillDetails = function () {

            var URI = CONSTANTS.ROOT_URL + '/api/pharmacy/sales/bills/' + $scope.unitId+"/"+ $scope.selfPayeeId +"/"+self.model.worklistBillId;

            
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    $rootScope.stopLoader();
                    self.model.billDetailList = response.data;
                   
                    if($scope.cookieObjectskipPharmacySettlement==false && self.model.billDetailList.totalSelfPayable>0)
                	{
                      settlementClick();
                	}else{
                		growl.success('Sales worklist bill Successfully saved');
                   	 	$state.go('outPatientSalesWorklist');
                	}
                
                    // var popup = angular.element('#billListDetails');
                    // popup.modal('show');
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        $scope.initBillSearchObj = function () {
            //$scope.billSearch = {fromDate:"",toDate:"",visitTypeId:"",billNo:"",visitNo:"",billStatusId:"",patientId:"",orgId:$scope.organizationId,unitId:$scope.unitId,isOtcReg:false};
            $scope.billSearch = {
                fromDate: new Date(),
                toDate: new Date(),
                visitTypeId: "",
                billNo: "",
                visitNo: "",
                billStatusId: "",
                patientId: "",
                orgId: $scope.organizationId,
                unitId: $scope.unitId,
                isOtcReg: false
            };
        }

        $scope.init = function () {

            $scope.billing = true;
            $scope.billSearch = {};
            $scope.initBillSearchObj();
            $scope.billList = [];
            $scope.payeeList = [];
            $scope.paymentDetailsList = [];
            $scope.listPaymentDetails = [];
            $scope.availableDeposits = [];
            $scope.activeDeposit = 0;
            $scope.billDisabled = false;
            $scope.isFinancialClearance = false;

            //check wether redirected from ipd charges
            // if (typeof ($rootScope.fromWhere) != "undefined" && $rootScope.fromWhere != "") {
            //   var temp = $rootScope.fromWhere.split("#");
            //   if (temp[0] == "ipdCharge") {
            //     $scope.getPatientDetailsById(temp[2]);
            //     $scope.billSearch.patientId = temp[2];
            //    GetBillList($scope.billSearch);
            //   }
            // } else {
            //   GetBillList($scope.billSearch);
            // }
        }

        $scope.initTotalAmounts = function () {
            $scope.totalPaymentAmount = parseFloat(0).toFixed(2);
            $scope.dueAmount = parseFloat(0).toFixed(2);
            $scope.refundAmount = parseFloat(0).toFixed(2);
            $scope.totalAmountSummary = parseFloat(0).toFixed(2);
            $scope.totalConcessionSummary = parseFloat(0).toFixed(2);
            $scope.netPayableSummary = parseFloat(0).toFixed(2);
            $scope.totalPayingAmount = parseFloat(0).toFixed(2);
            
            //Ganesh
            $scope.totalSelfPayable = parseFloat(0).toFixed(2);
            $scope.totalCreditPayable = parseFloat(0).toFixed(2);
        }

        var settlementClick = function () {
        	setTimeout(function(){
           	 $('#setCash').trigger('click')
            }, 300);
        	$scope.flagForSMSpay = false;
    		$scope.flagForBharatQr = false;
    		$scope.flagForSale = false;
    		$scope.smsPay = {};
    		$scope.bharatQr = {};
    		$scope.sale = {};
    		$scope.objPosMachine.deviceName = "";
    		$scope.objPosMachine.isSale = false;
    		$scope.objPosMachine.isBharatQr = false;
    		$scope.objPosMachine.isSMSpay = false;
//    		$scope.setFirstTabLevel(1);
        	$scope.skipMpesaValidation = false;
        	 $scope.pinpadAmounFlag = false;
            var popup = angular.element('#collectPaymentDetails');
            $scope.billDetails();
            $scope.calculateTotalDueRefAmount();
            popup.modal('show');
        }

        $scope.checkAmount = function (amount) {
            if (amount > $scope.dueAmount || amount == 0) {
                growl.error("Amount not allowed greater than Due amount or zero. ", {
                    title: ERROR_MSG
                });
                $scope.paymentDetails.cashAmount = parseFloat($scope.dueAmount).toFixed(2);
                return;

            } else {
                $scope.paymentDetails.cashAmount = parseFloat(amount).toFixed(2);
            }
        }
        $scope.selectedPatientId="";
        $scope.selectedUhid="";
        $scope.billDetails = function () {
            let bill = self.model.billDetailList;
            // console.log("BILL", bill);

            $scope.billDisabled = bill.billStatusId == 3 ? true : false;
            $scope.billId = bill.id;
            $scope.totalAmount = bill.netAmount;
            
            /*$scope.dueAmount = bill.balanceAmt;
            $scope.oldDueAmount = bill.balanceAmt;
            $scope.billDueAmount = bill.balanceAmt;*/
            $scope.selectedUhid = bill.uhid;
            $scope.selectedPatientId =bill.patientId;
            $scope.validatTextFieldFlag = true;
            loyaltiCardCallFactory.getLoyaltyCardList(bill.patientId,$scope);
            $scope.dueAmount = bill.selfDueAmount;
            $scope.oldDueAmount = bill.selfDueAmount;
            $scope.billDueAmount = bill.selfDueAmount;
            $scope.payeeDueAmtFinal = bill.selfDueAmount;
            $scope.payeeDueAmtStatic = bill.selfDueAmount
            
            $scope.currentBill = bill;
            $scope.admissionId = bill.visitTypeId == 2 ? bill.visitId : null;
            $scope.isFinancialClearance = false;
            $('#isFinancialClearance').prop('checked', false);
            //Vivek added
            $scope.encounterId = bill.visitId;
            $scope.appointmentId = bill.appointmentId;
            //End Vivek Added
            //get payer details

            var URI = CONSTANTS.ROOT_URL + '/api/pharmacy/sales/bills/' + $scope.unitId+"/"+ $scope.selfPayeeId +"/"+self.model.worklistBillId;
            BillingGenericService.serviceAction(METHOD_GET, URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function (response) {
                if (response.data.items.length > 0) {
                    // $scope.payeeList = response.data.listObject;
                    $scope.payeeList = response.data;
                    //vivek added
                    // $scope.billServicesList = response.data.object;
                    $scope.billServicesList = response.data.items;
                    var totalAmtQuaBase = parseFloat(0);
                    var totalAmtConcession = parseFloat(0);
                    var totalAmtDiscount = parseFloat(0);
                    var totalAmtGst = parseFloat(0);
                    var totalNetPayableAmt = parseFloat(0);
                    
                    //Ganesh
                    var totalSelfPayable = parseFloat(0);
                    var totalCreditPayable = parseFloat(0);
                    
                    
                    angular.forEach($scope.billServicesList, function (value, key) {
                        // totalAmtQuaBase = totalAmtQuaBase + parseFloat(value.grossAmount);
                        totalAmtQuaBase = totalAmtQuaBase + parseFloat(value.totalAmount);
                        // totalAmtConcession = totalAmtConcession + parseFloat(value.concession);
                        // totalAmtDiscount = totalAmtDiscount + parseFloat(value.discount);
                        totalAmtDiscount = totalAmtDiscount + parseFloat(value.discount);
                        // totalAmtGst = totalAmtGst + parseFloat(value.taxAmount);
                        totalAmtGst = totalAmtGst + parseFloat(value.taxAmount);
                        // totalNetPayableAmt = totalNetPayableAmt + parseFloat(value.netAmt);
                        totalNetPayableAmt = totalNetPayableAmt + parseFloat(value.netAmount);
                        
                        
                        //ganesh
                        totalSelfPayable = totalSelfPayable + parseFloat(value.selfPayable);
                        totalCreditPayable = totalCreditPayable + parseFloat(value.creditPayable);
                    });

                    $scope.totalAmtQuaBase = totalAmtQuaBase;
                    // $scope.totalAmtConcession = totalAmtConcession;
                    $scope.totalAmtDiscount = totalAmtDiscount;
                    $scope.totalAmtGst = (totalAmtGst).toFixed(2);
                    $scope.totalNetPayableAmt = totalNetPayableAmt;
                    
                    
                    //Ganesh
                    $scope.totalSelfPayable = totalSelfPayable;
                    $scope.totalCreditPayable = totalCreditPayable;


                }
            });
            
      		//get available active deposits by patient
			var URI = BASE_URL + ROOT_URL + "/api/billing/unit/deposit/patient/"+$scope.organizationId+"/"+$scope.unitId+"/"+bill.patientId+"/"+0+"/"+0+"/"+0+"/"+$scope.depositTypeWiseRequired;
			BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success")
				{
					
					$scope.consumedAmt=0;
					$scope.availableDeposits = response.data.listObject;
					var activeDeposit = parseFloat(0);
					var activeDepositPatient = parseFloat(0);
					var billAmt = parseFloat($scope.payeeDueAmtStatic);
					var i = 0
					angular.forEach($scope.availableDeposits,function(value,key)
					{
						$scope.availableDeposits[i].oldConsumedDeposit = value.consumedDeposit;
						$scope.availableDeposits[i].oldAvailableDeposit =  value.availableDeposit;
						/*if(value.depositTypeId != '1')
					     {
						    activeDeposit = activeDeposit + parseFloat(value.availableDeposit);
						    
						 if(billAmt >0){ 
						   
						   if(billAmt >= value.availableDeposit)
								{
							        value.consumedDeposit = value.consumedDeposit+value.availableDeposit;
									$scope.availableDeposits[i].depositeFlag = 'Y';
									$scope.transferToActiveDepositeFlag = true;
									$scope.dueAmount = ($scope.dueAmount - value.availableDeposit).toFixed(2);
									billAmt = billAmt-value.availableDeposit;
									$scope.availableDeposits[i].availableDeposit = parseFloat(0);
								}							
							else if(billAmt < value.availableDeposit)
								{
									
									value.availableDeposit = parseFloat(value.availableDeposit)- parseFloat(billAmt);
									value.consumedDeposit = value.consumedDeposit + parseFloat(billAmt);
									$scope.availableDeposits[i].depositeFlag = 'Y';
									$scope.dueAmount = ($scope.dueAmount - billAmt).toFixed(2);
									$scope.payeeDueAmtFinal = 0;
									billAmt = 0;
									//$scope.dueAmount = ($scope.dueAmount -$scope.consumedAmt).toFixed(2);
								}
						    }else{
						    	$scope.availableDeposits[i].depositeFlag = 'N';
						    }
						}
						else
						{*/
							activeDeposit = activeDeposit + parseFloat(value.availableDeposit);
							$scope.availableDeposits[i].depositeFlag = 'N';
							activeDepositPatient = activeDepositPatient + parseFloat(value.availableDeposit);
							/*}*/
						i++;
						
					});
					$scope.activeDeposit = activeDeposit;
					$scope.useActiveDepo = true;
					
				}
				$scope.calculateTotalDueRefAmount();
			});
        }

        
        //Ganesh Start
    	$scope.checkAciveDepo  = function(useActiveDepo)
    	{
    		 $scope.activeDeposit = 0;
    		if(useActiveDepo == true)
    			{
    			   
    				var i = 0;
    				angular.forEach($scope.availableDeposits,function(value,key)
    				{
    					if(value.depositTypeId == '1')
    				   {
    					   $scope.activeDeposit = $scope.activeDeposit + parseFloat(value.availableDeposit);
    					   $scope.availableDeposits[i].depositeFlag = 'N';
    					   if(value.depositeFlag == 'N')
    					   {
    						   value.consumedDeposit = value.oldConsumedDeposit;
    						   value.availableDeposit = value.oldAvailableDeposit;
    					   }
    					}else{
    						$scope.activeDeposit = $scope.activeDeposit + parseFloat(value.depositAmount);
    					}
    					
    					i++;
    				});
    			}
    		else
    			{
    			var i = 0;
    			var consumedAmt= 0;
    				angular.forEach($scope.availableDeposits,function(value,key)
    				{
    					if(value.depositTypeId == '1')
    					{
    						if(value.depositeFlag == 'Y')
    						{
    							consumedAmt = consumedAmt - (value.consumedDeposit);
    						}
    						$scope.availableDeposits[i].availableDeposit = $scope.availableDeposits[i].oldAvailableDeposit;
    						$scope.availableDeposits[i].consumedDeposit =$scope.availableDeposits[i].oldConsumedDeposit
    						$scope.activeDeposit = $scope.activeDeposit + parseFloat(value.oldAvailableDeposit);
    						$scope.availableDeposits[i].depositeFlag = 'N';
    					
    					}else{
    							$scope.activeDeposit = $scope.activeDeposit + parseFloat(value.oldAvailableDeposit);
    					}
    					$scope.dueAmount = (parseFloat($scope.dueAmount)+ parseFloat(consumedAmt)).toFixed(2);
    					i++;
    				});
    			}
    		$scope.calculateTotalDueRefAmount();
    	}
    	/*Ganesh end*/
        $scope.addBtnDisabled = false;
        $scope.depositeSub = function(item,idx)
    	{
        	 $("#pharmacyUserEnterDeposit").modal("hide");
    		if($scope.payeeDueAmtFinal > 0 || item.depositeFlag == 'N'){
    		var consumedAmt = 0;
    		if(item.depositeFlag == 'Y')
    			{
    			
    			 if(item.availableDeposit == 0 || item.availableDeposit == '00' || item.availableDeposit < 0 || parseFloat($scope.DepositObj.userEnterDepositAmount) > parseFloat(item.availableDeposit)){
 					growl.warning("Available deposit amount shuold be greater than user enter amount.",{
 						title : WARNING_MSG
 					});
 					return false;
 				}
    			 $scope.availableDeposits[idx].remark =$scope.DepositObj.remark;
    				if($scope.dueAmount == 0)
    				{
    					$scope.availableDeposits[idx].depositeFlag = 'N';
    				}
    				else if($scope.dueAmount >= item.availableDeposit)
    				{
    					$scope.availableDeposits[idx].consumedDeposit  = parseFloat(parseFloat($scope.availableDeposits[idx].consumedDeposit)  + parseFloat($scope.DepositObj.userEnterDepositAmount)).toFixed(2)// parseFloat(userEnterDepositAmount);
						$scope.availableDeposits[idx].availableDeposit = ($scope.availableDeposits[idx].availableDeposit) - parseFloat($scope.DepositObj.userEnterDepositAmount).toFixed(2);
    					$scope.dueAmount = (parseFloat($scope.dueAmount)) - parseFloat($scope.DepositObj.userEnterDepositAmount).toFixed(2);
    				}
    			else if($scope.dueAmount <= item.availableDeposit)
    				{
    					$scope.availableDeposits[idx].availableDeposit = parseFloat(item.availableDeposit)- parseFloat($scope.DepositObj.userEnterDepositAmount).toFixed(2);
    					$scope.availableDeposits[idx].consumedDeposit =parseFloat(parseFloat(item.consumedDeposit) + parseFloat($scope.DepositObj.userEnterDepositAmount)).toFixed(2);
    					$scope.dueAmount = (parseFloat($scope.dueAmount))- parseFloat($scope.DepositObj.userEnterDepositAmount).toFixed(2);
    				}
    				$scope.DepositObj.remark=""; 
    				$scope.addBtnDisabled = true;
    			}
    		if(item.depositeFlag == 'N')
    		{
				 $scope.availableDeposits[idx].remark =$scope.DepositObj.remark;
    					if($scope.availableDeposits[idx].availableDeposit > 0)
    						{
    						$scope.availableDeposits[idx].availableDeposit = parseFloat(item.oldAvailableDeposit).toFixed(2);
    						$scope.availableDeposits[idx].consumedDeposit = parseFloat(item.oldConsumedDeposit);
    						$scope.dueAmount = (parseFloat($scope.dueAmount)+ parseFloat(item.oldAvailableDeposit-item.availableDeposit)).toFixed(2);
    						}
    					else{
    						$scope.abal = parseFloat(parseFloat(item.consumedDeposit)-parseFloat(item.oldConsumedDeposit)).toFixed(2);
    						$scope.availableDeposits[idx].availableDeposit =  parseFloat(parseFloat($scope.availableDeposits[idx].availableDeposit) +  parseFloat($scope.abal)).toFixed(2);
    						$scope.availableDeposits[idx].consumedDeposit = parseFloat(item.oldConsumedDeposit);
    						$scope.dueAmount = (parseFloat($scope.dueAmount)+ parseFloat($scope.availableDeposits[idx].availableDeposit)).toFixed(2);
    					}
    					
    					
    					var totalListamount = 0;
    					angular.forEach($scope.listPaymentDetails,function(value,key)
    							{
    						totalListamount = totalListamount+parseInt(value.amount);
    							});

    					if(parseInt($scope.totalAmount) >=totalListamount){
    						
    						$scope.excessAmount = parseFloat(0).toFixed(2);
    					}else{
    						var dummyAmount = parseFloat(totalListamount).toFixed(2) - parseFloat($scope.totalAmount).toFixed(2);
    						$scope.excessAmount = parseFloat(dummyAmount).toFixed(2);
    					}
    					$scope.DepositObj.remark="";	
    		}
    		
    		var avalForFlag = false;
    		var i = 1;
    		angular.forEach($scope.availableDeposits,function(value,key){
    			if(value.depositeFlag == 'Y')
    				{
    				avalForFlag = true;
    				}else
    					{
    						if($scope.transferToActiveDepositeFlag == false)
    							{
    							avalForFlag == false;
    							}
    					}
    			if(i == $scope.availableDeposits.length)
    				{
    				$scope.transferToActiveDepositeFlag  = avalForFlag;
    				}
    			i++;
    			
    		})
    		
    		$scope.calculateTotalDueRefAmount();
    		
    	}
    		else
    		{
    			growl.warning("Due amount is zero(0).",{
    				title : WARNING_MSG
    			});
    			$scope.availableDeposits[idx].depositeFlag = 'N';
    			return false;
    		}
    		console.log($scope.availableDeposits);
    	}
        
        
        $scope.depositeObject = undefined;
    	$scope.depositeIndex = undefined;
    	$scope.depositeSubPopup = function(item,idx){
    		$scope.DepositObj.userEnterDepositAmount = 0;
    		if(item.depositeFlag == 'Y' && $scope.dueAmount > 0){
    			$scope.addBtnDisabled = false;
    			if($scope.dueAmount > item.availableDeposit){
					$scope.DepositObj.userEnterDepositAmount = item.availableDeposit;
				}else{
					$scope.DepositObj.userEnterDepositAmount = parseFloat($scope.dueAmount).toFixed(2);
				}
    			
    			$scope.depositeObject = item;
    			$scope.depositeIndex = idx;
    			 $("#pharmacyUserEnterDeposit").modal("show");
    			 
    		}else{
    			$scope.depositeSub(item,idx);
    			 $("#pharmacyUserEnterDeposit").modal("hide");
    		}
    		
    	}
    	
    	$scope.closeDepositPop = function(){
    		$scope.availableDeposits[$scope.depositeIndex].depositeFlag = 'N';
   		 $("#pharmacyUserEnterDeposit").modal("hide");
   	}
    	
    	$scope.checkEnterDepositeAmount = function(){
			
			if(parseFloat($scope.DepositObj.userEnterDepositAmount) > parseFloat($scope.depositeObject.availableDeposit)){
				growl.warning("Enter amount should not be greater available deposit ",{
					title : WARNING_MSG
				});
				$scope.DepositObj.userEnterDepositAmount =0;
				return;
			}
			
			if(parseFloat($scope.DepositObj.userEnterDepositAmount) > parseFloat($scope.dueAmount)){
				growl.warning("Enter amount should not be greater due amount",{
					title : WARNING_MSG
				});
				$scope.DepositObj.userEnterDepositAmount =0;
				return;
				
			}
			
		}
		
        
        //-----------------payment Collection----------------//
        $scope.initPaymentDetails = function () {
            $scope.paymentDetails = {
                "paymentRecieptDetailsId": "",
                "paymentModeId": 1,
                "paymentModeName": "Cash",
                "amount": "",
                "bankId": "",
                "bankName": "",
                "accountNumber": "",
                "checkNumber": "",
                "cardNo": "",
                "cardHolderName": "",
                "dateOfTransaction": new Date().getTime(),
                "expiryDate": "",
                "paymentVoucherNo": "",
                "cvv": "",
                "unitId": $scope.unitId,
                "orgnisationId": $scope.organizationId,
                "status": "A",
                "createdDate": new Date().getTime(),
                "createdBy": $scope.userId,
                "updatedDate": new Date().getTime(),
                "updatedBy": $scope.userId,
                "chequeDate": "",
                "cardTypeId": "",
                "isCancle": "I",
                'transactionNo'         : ""

            }
        }
        $scope.initPaymentDetails();

        $scope.paymentModes = {
            1: "Cash",
            2: "Cheque",
            3: "Credit Card",
            4: "Debit Card",
            /*5: "E - Transfer",
              6: "Loyalty Card"	*/
        }

        $scope.addBtnId=false;
    	$scope.pinpadAmounFlag =false;
        $scope.setPaymentModeId = function (paymentModeId) {
        
        	if(paymentModeId==3 && $scope.isPinPadRequired =='Y'){
    			$scope.addBtnId=true;
    			// $scope.pinpadAmounFlag =true;
    			$scope.objPosMachine.deviceName= $cookies.getObject('deviceName');
    		}else if(paymentModeId==6){
    			$scope.addBtnId=true;
    		}
    		else{
    			$scope.addBtnId=false;
    			 $scope.pinpadAmounFlag =false;
    		}
            $scope.paymentDetails.paymentModeId = paymentModeId;
            $scope.paymentDetails.paymentModeName = $scope.paymentModes[paymentModeId];
            // bank list called on click of Cheque tab
            $scope.getBankList();
            // bank list called on click of card tab
            $scope.getCardTypeList();
        }

        $scope.setBankName = function (bankId) {
            angular.forEach($scope.bankList, function (value, key) {
                if (value.bankId == bankId) {
                    $scope.paymentDetails.bankName = value.bankName;
                    return;
                }
            });

        }

        $scope.getBankList = function () {
            var obj = {
                "unitId": $scope.unitId,
                "orgnisationId": $scope.organizationId
            }
            $rootScope.startLoader();
            var URI = CONSTANTS.ROOT_URL + "/api/billing" + "/bankMaster";

            BillingGenericService.serviceAction(METHOD_POST, URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function (response) {
                if (response.data.status == "success") {
                    $scope.bankList = response.data.listObject;
                }
            });
        }

        $scope.getBankList();

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
        $scope.getCardTypeList();

        $scope.calculateTotalDueRefAmount = function () {

        	var amount = 0;
    		var dueAmount = $rootScope.parseFloatNum($scope.dueAmount);
    		$scope.consumedAmt = 0;
    		var payeeDueAmtFinal = $rootScope.parseFloatNum($scope.payeeDueAmtFinal);
    		
    		angular.forEach($scope.listPaymentDetails,function(value,key){
    			amount = amount + parseFloat(value.amount); 
    		});
    		
    		if($scope.availableDeposits!=null &&$scope.availableDeposits.length>0)
    		{
    			angular.forEach($scope.availableDeposits,function(value,key)
    					{
    						if(value.depositeFlag=='Y')
    							{
    								var con =   parseFloat(parseFloat(value.consumedDeposit) - parseFloat(value.oldConsumedDeposit)).toFixed(2);
    								$scope.consumedAmt = parseFloat( parseFloat($scope.consumedAmt) + parseFloat(con)).toFixed(2);
                                    $scope.saveBtnDisabled = false;
    							}
    					});
    		}
    		var tempDueAmt = parseFloat($scope.payeeDueAmtStatic) - parseFloat(amount) - parseFloat($scope.consumedAmt);
    		var temppayeeDueAmtFinal = $scope.payeeDueAmtStatic - amount - $scope.consumedAmt;
    		$scope.payeeDueAmtFinal = temppayeeDueAmtFinal >= 0 ? temppayeeDueAmtFinal : 0;
    		$scope.payeeDueAmtFinal = parseFloat($scope.payeeDueAmtFinal).toFixed(2);
    		var df = (parseFloat($scope.payeeDueAmtStatic) - (parseFloat(amount) + parseFloat($scope.consumedAmt)));
    		if(df > 0)
    			{
    			$scope.dueAmount = tempDueAmt >= 0 ? tempDueAmt : 0;
    			$scope.paymentDetails.cashAmount = $rootScope.parseFloatNum($scope.dueAmount);
    		    $scope.paymentDetails.chequeAmount = parseFloat($scope.dueAmount).toFixed(2);
    		    $scope.paymentDetails.credAmount = parseFloat($scope.dueAmount).toFixed(2);
    		    $scope.eTransferObj.transferAmount= parseFloat($scope.dueAmount).toFixed(2);
    		    $scope.paymentDetails.loyaltyCardAmt =  parseFloat($scope.dueAmount).toFixed(2);
    		    $scope.razorPayObject.razorAmount =  parseFloat($scope.dueAmount).toFixed(2);
    			}
    		else
    			{
    			$scope.dueAmount = parseFloat(0.00).toFixed(2);
    			$scope.paymentDetails.cashAmount = parseFloat($scope.dueAmount).toFixed(2);
    		    $scope.paymentDetails.chequeAmount = parseFloat($scope.dueAmount).toFixed(2);
    		    $scope.paymentDetails.credAmount = parseFloat($scope.dueAmount).toFixed(2);
    		    $scope.eTransferObj.transferAmount= parseFloat($scope.dueAmount).toFixed(2);
    		    $scope.paymentDetails.loyaltyCardAmt =  parseFloat($scope.dueAmount).toFixed(2);
    		    $scope.razorPayObject.razorAmount =  parseFloat($scope.dueAmount).toFixed(2);
    			}
    		$scope.refundAmount = temppayeeDueAmtFinal >= 0 ? 0 : Math.abs(temppayeeDueAmtFinal);
    		$scope.amountCollected = amount;
    		
    		if($scope.consumedAmt > 0)
    			{
    			  $scope.depositeSettleBtnFlag = false;
    			}
    		else
    			{
    			$scope.depositeSettleBtnFlag = true;
    			}
    		
    	    
    	      $scope.refundAmount = temppayeeDueAmtFinal >= 0 ? 0 : Math.abs(temppayeeDueAmtFinal);
    	      $scope.amountCollected = amount;
        }

        
    	$scope.uploadEncounterDoc = function(Obj)
		{
			 var fd = new FormData();
		        fd.append('organizationId', $scope.organizationId);
		        fd.append('billId',Obj);
		        fd.append('isDeposite',false);
		        fd.append('isPharmacy',true);
		        fd.append('isMiscellaneous',false);
		        fd.append('unitId', $scope.unitId);
		        fd.append('documentName',$scope.fileObj.file.name);
		        fd.append('file',$scope.fileObj.file );
		        var URI1 = BASE_URL + ROOT_URL + "/billing/PanCard/uploadDoc";
		        return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
				{	
		        	$scope.fileObj = {};
					$scope.fileObj.file = null;
					angular.element("input[type='file']").val(null);
					return response;
					
				});
		}	
		
    	$scope.changesStatus = function(){
			$scope.depositeSettleBtnFlag = false;
			$('#uploadpanCardDetailsOutPatient').modal('hide');
		}
		
        
        $scope.settleBill = function () {
        	 $scope.billDisabled = true;
             $scope.depositeSettleBtnFlag = true;
        	var collectedAmount = parseFloat(0).toFixed();
    		angular.forEach($scope.listPaymentDetails,function(value,key){
    			if(value.paymentModeId == 1){
    				collectedAmount = parseFloat(collectedAmount) + parseFloat(value.amount);
    			}
    			
    		});
    		
    		if( $scope.cashAmountValidation != null && collectedAmount >=$scope.cashAmountValidation && $scope.fileObj.file == null){
    			$('#uploadpanCardDetailsOutPatient').modal('show');
    				return false;
    		}
    		$('#uploadpanCardDetailsOutPatient').modal('hide');
        	
        	
           
            var billPayeeDetailsId = "";
            angular.forEach($scope.payeeList, function (value, key) {
                if (value.payeeId == '1')
                    billPayeeDetailsId = value.billPayeeDetailsId;
            })

            $scope.finalAvailableDeposits = [];
            var i = 0;
            angular.forEach($scope.availableDeposits, function (value, key) {
                if (value.depositeFlag == 'Y') {
                    var newArr = {};
                    newArr.availableDeposit = $scope.availableDeposits[i].availableDeposit;
                    newArr.depositAmount = $scope.availableDeposits[i].depositAmount;
                    newArr.depositMasterId = $scope.availableDeposits[i].depositMasterId;
                    newArr.remark =$scope.availableDeposits[i].remark;
                    newArr.organizationId = $scope.organizationId;
                    newArr.unitId = $scope.unitId;
                    newArr.depConsumptionDate = moment(new Date()).format('DD-MM-YYYY HH:MM:SS');
                    newArr.billId = $scope.billId;
                    newArr.billPayeeDetailsId = billPayeeDetailsId;
                    newArr.consumedAmount = (parseFloat($scope.availableDeposits[i].consumedDeposit).toFixed(2) - $scope.availableDeposits[i].oldConsumedDeposit); ;
                    newArr.createdBy = $scope.userId;
                    $scope.finalAvailableDeposits.push(newArr);
                }
                i++;
                
               

            });

            //return false;
            var param = {
                billingMasterId: $scope.billId,
                patientId :$scope.selectedPatientId,
                orgnizationId:  $scope.organizationId,
                unitId:$scope.unitId,
                createdBy: $scope.userId ,
                amountData: (parseFloat($scope.amountCollected) + parseFloat($scope.consumedAmt)),
                paymentReceiptDetailsDtosList: $scope.listPaymentDetails,
                paymentDepositDetailsList: $scope.finalAvailableDeposits,
                selfPayeeId:$scope.selfPayeeId,
                balanceAmount : self.model.billDetailList.balanceAmt,
            }
           

            if (($scope.finalAvailableDeposits.length == 0) && ($scope.listPaymentDetails.length == 0 || $scope.listPaymentDetails == 'undefined')) {
                growl.error("Please select at least one payment mode", {
                    title: ERROR_MSG
                });
                return;
            }

            if (($scope.amountCollected == '' || $scope.amountCollected <= 0) && ($scope.consumedAmt == '' || $scope.consumedAmt <= 0)) {
                growl.error("Amount can't be zero", {
                    title: ERROR_MSG
                });
                return;
            }
            $rootScope.startLoader();
            var URI = CONSTANTS.ROOT_URL + '/api/pharmacy/sales/bills/save/bill';
            BillingGenericService.serviceAction(METHOD_PATCH, URI, param, NOTIFICATION_MSG_STATUS_TRUE).then(function (response) {
                if (response.data.message == "Sale Details saved successfully") {
                	
                	 if($scope.fileObj.file != null){
         				$scope.uploadEncounterDoc($scope.billId);
         			}
                	
            				var data = {
            					"appName" : "HIS",
            					"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
            					"reportDesignFilePath" : "get_salesbillsReceipt.rpt",
            					"reportFormat" : "PDF",
            					"exportFileName" : "BillSlip",
            					"reportParams" : [ {
            						"paramName" : "id",
            						"paramType" : "Integer",
            						"paramValue" : response.data.id
            					} ]
            				};
            				reportService.generate(REPORT_API_URI1, data);
            			
//            		}
            		/** ** */
                    //collectPayment
                    //billListDetails
                	$scope.validatTextFieldFlag = true;		
                    angular.element('#collectPaymentDetails').modal('hide');
                    angular.element('#billListDetails').modal('hide');
                    $scope.billDetails($scope.currentBill);
                    $scope.calculateTotalDueRefAmount();
                    //rePrintBillSlip(response.data);
                    $scope.listPaymentDetails = [];
                    ClearFields(self.model.worklistDetails);
                    // GetBillList($scope.billSearch);
                    NavigateToOutPatientSalesWorklist();
                    GetWorklistDetails();
                }else{
                	growl.error(response.data.message, {
        				title : 'error!!'
        			});
                	 angular.element('#collectPaymentDetails').modal('hide');
                     angular.element('#billListDetails').modal('hide');
                     $scope.calculateTotalDueRefAmount();
                     //rePrintBillSlip(response.data);
                     $scope.listPaymentDetails = [];
                     ClearFields(self.model.worklistDetails);
                     // GetBillList($scope.billSearch);
                     NavigateToOutPatientSalesWorklist();
                     GetWorklistDetails();
                }
            });
            $rootScope.stopLoader();
        }

      /*  $scope.addPaymentDetails = function () {
            $scope.isBillList = 0;
            pharmacyPaymentCollFactory.addPaymentDetails($scope);
        }*/

        $scope.addPaymentDetails = function(){
    		$scope.isBillList = 0;
    		var exAmount = '';
    		$scope.saveBtnDisabled = false;
    		var cashLimit = 0;
			angular.forEach($scope.listPaymentDetails,function(value,key){
				
				if(value.paymentModeId == 1){
					cashLimit = parseFloat(cashLimit) + parseFloat(value.amount);
				}
			});
			
			if($scope.paymentDetails.paymentModeId == 1){
				cashLimit = cashLimit + parseFloat($scope.paymentDetails.cashAmount);
				cashLimit = cashLimit + $scope.cashCollectedAmount;
			}

			if(($scope.cashAmountLimit != undefined && $scope.cashAmountLimit != null) && cashLimit > $scope.cashAmountLimit){
				growl.warning("System can not accept more than "+$scope.cashAmountLimit +" in cash amount",{
					title : WARNING_MSG
				});
				return;
			}

			var trancastionId = false;
			angular.forEach($scope.listPaymentDetails,function(value,key){
				
				if(value.paymentModeId == 12 && value.transactionNo == $scope.razorPayObject.razorUniqueId){
					trancastionId =true;
				}
			});
			
			if(trancastionId){
				growl.warning("Trancastion id should not be same",{
					title : WARNING_MSG
				});
				return;
			}
    		
    		if($scope.paymentDetails.paymentModeId == 1){
    			
    			exAmount = $scope.paymentDetails.cashAmount;
    			
    		}else if($scope.paymentDetails.paymentModeId == 2){
    			
    			exAmount =  $scope.paymentDetails.chequeAmount;
    			
    		}else if($scope.paymentDetails.paymentModeId == 3){
    			
    			exAmount = $scope.paymentDetails.credAmount;
    			
    		}else if($scope.paymentDetails.paymentModeId == 4){
    			
    			exAmount = $scope.paymentDetails.debAmount;
    			
    		}else if($scope.paymentDetails.paymentModeId == 5){
    			
    			exAmount = ($scope.mpesaObject.mpesaAmount != undefined && $scope.mpesaObject.mpesaAmount != null && $scope.mpesaObject.mpesaAmount!= '') ? $scope.mpesaObject.mpesaAmount : 0;
    			
    		}else if($scope.paymentDetails.paymentModeId == 6){
    			
    			exAmount = $scope.paymentDetails.loyaltyCardAmt;
    			
    		}else if($scope.paymentDetails.paymentModeId == 8){
    			
    			exAmount = $scope.eTransferObj.transferAmount;
    		}else if($scope.paymentDetails.paymentModeId == 12){
				
				exAmount = $scope.razorPayObject.razorPaidAmount;
			}
    		if(parseFloat(exAmount) <= parseFloat($scope.payeeDueAmtFinal))
    		{
    				$scope.transferToActiveDepositFact = $scope.transferToActiveDeposit;
    				pharmacyPaymentCollFactory.addPaymentDetails($scope);
    				// $scope.calculateDueAmount();
    		}else{
    			 var fixAmount = parseFloat(exAmount).toFixed(2) - parseFloat($scope.payeeDueAmtFinal).toFixed(2); 
				 $scope.excessAmount = parseFloat(fixAmount).toFixed(2);
                if(fixAmount>0)
                {
                  if($scope.exceededBillAmountSettlement==true)
                      {
                      $("#confirmSaveout").modal("show");
                      }
                  else
                      {
                        growl.warning("Entered amount is greater than current amount");
                        $scope.excessAmount = parseFloat(0.0).toFixed(2);
                        $scope.paymentDetails.chequeAmount= parseFloat($scope.dueAmount).toFixed(2);
                        $scope.paymentDetails.cashAmount = parseFloat($scope.dueAmount).toFixed(2);
                        $scope.paymentDetails.credAmount = parseFloat($scope.dueAmount).toFixed(2);
                        $scope.paymentDetails.loyaltyCardAmt = parseFloat($scope.dueAmount).toFixed(2);
                        $scope.mpesaObject.mpesaAmount = parseFloat($scope.dueAmount).toFixed(2);
                        $scope.eTransferObj.transferAmount = parseFloat($scope.dueAmount).toFixed(2);
                        $scope.razorPayObject.razorAmount = parseFloat($scope.dueAmount).toFixed(2);
                      }
                }
    		}
    	
    		
    	}
    	$scope.addExAmount = function(){
    		$scope.isBillList = 0;
    		$scope.transferToActiveDeposit = 'Y';
    			$scope.transferToActiveDepositFact = $scope.transferToActiveDeposit;
    		
    		pharmacyPaymentCollFactory.addPaymentDetails($scope);
    		$("#confirmSaveout").modal("hide");
    		// $scope.calculateDueAmount();
    	}
        
        
        $scope.removePaymentDetails = function () {
            pharmacyPaymentCollFactory.removePaymentDetails($scope);
            var amount = 0;
            $scope.listPaymentDetails.forEach((elem) => {
                amount = parseFloat(amount) + parseFloat(elem.amount)
            });
            if (amount <= 0 && $scope.currentBill.paidAmt <= 0) {
                $scope.isFinancialClearance = false;
                $('#isFinancialClearance').prop('checked', false);
            }
            if($scope.paymentDetails.cashAmount >=0){
            	
          	  $scope.transferToActiveDeposit = 'N';
            }
        }
        var ConfirmRemove = function (index, assigningPickerDispenserDetails, parentindex) {
            var popup = angular.element('#itemRemovePopup');
            popup.modal('show');
            self.model.itemForRemove = index;
            self.model.pickerDetail = assigningPickerDispenserDetails;
            self.model.parentIndexNew = parentindex;
            if(assigningPickerDispenserDetails.items.length > 1){
                $scope.flagRemove = true;
            }
            else{
                $scope.flagRemove = false;
            }
        }
        $scope.init();
        $scope.initTotalAmounts();

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

      var ViewStockCheckDetails = function(item) {
        var cookieObject = $cookies.getObject('cookieObject');
        $scope.storeIdNew = cookieObject.storeId;
        var URI = CONSTANTS.PHARMACY.OP.WALK_IN_SALES.STOCK_CHECK + $scope.unitId + "/"+$scope.organizationId + "/" + item.itemId + "/" + $scope.storeIdNew;
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(function (response) {
            self.model.stockCheckList = response.data;
            angular.forEach(self.model.stockCheckList,function(value, key) {
                if(value.expdate.toString().includes("-"))
                    {
                    value.stringDate=true;
                    }
            });
            var popup = angular.element("#stockCheckDetails");
            popup.modal("show");
            $rootScope.stopLoader();
        },
        function errorCallback(response) {
            $rootScope.stopLoader();
        });
      }

      
      /***Chetana**/
      var printOutPatientSalesWorklistDetailsReport = function () {
          var data = {
        		  
        		  
        		  "appName": "HIS",
	                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
	                "reportDesignFilePath": "get_PrescriptionForSalesworklist.rpt",
	                "reportFormat": "PDF",
	                "exportFileName": "Salesworklist",
	                "reportParams": [
	                    {
	                        "paramName": "prescriptionid",
	                        "paramType": "Integer",
	                        "paramValue": self.model.issueId
	                    }
	                ]  
          };
          reportService.generate(REPORT_API_URI1, data);
      }
     /*****/ 
      
        initializeController();

        var rePrintBillSlip = function (bill) {
            var data = {
                "appName": "HIS",
                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                "reportDesignFilePath": "get_salesbillsReceipt.rpt",
                "reportFormat": "PDF",
                "exportFileName": "BillSlip",
                "reportParams": [
                    {
                        "paramName": "Id",
                        "paramType": "Integer",
                        "paramValue": bill.id
                    }
                ]
            };
            reportService.generate(REPORT_API_URI1, data);
        }

        
        
        
        //==================================================Ganesh Start
        
        //get sponsor details by encounter or admissionId start
    	$scope.getSponsorDetails=function(encounterId)
    	{
    		$rootScope.startLoader();
    		$scope.activePackageList =[];
    		var param = {
    				     encounterId:encounterId,
    				    };
    		console.log("Id data",param);
    		 var URI = BASE_URL + ROOT_URL + "/global/consumption/getActivePaymentEntitlement"
    		    BillingGenericService.serviceAction(METHOD_POST,URI,param,"").then(function(response) {
    				if (response.data.status == "success")
    				{
    					
    					
    					$scope.paymentEntitleMentList=response.data.listObject;
    					$scope.paymentEntitleMentListDummy =angular.copy($scope.paymentEntitleMentList);
    					
    					if($scope.paymentEntitleMentList!=null && $scope.paymentEntitleMentList.length>0)
    					{
    					  $scope.entitlementObj.paymentEntitlementTypeId = $scope.paymentEntitleMentList[0].paymentEntitlementId;
    					  $scope.isContractChanged = $scope.paymentEntitleMentList[0].isContractChanged;
    					  $scope.isPayeeUpdated = $scope.paymentEntitleMentList[0].isPayeeUpdated;
    					  $scope.paymentEntitlementTypeIdDummy =angular.copy($scope.entitlementObj.paymentEntitlementTypeId);
    					  //$scope.isDiscountApplicable = $scope.paymentEntitleMentList[0].isTariffRateApplicable;
    					  $scope.isDiscountApplicable ='Y';
    					    if(($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3) ){ //&& $scope.isConsumed == false
                                $scope.isValidated = true;
                            }
                            else{
                                $scope.isValidated = false;
                            }
    						angular.forEach($scope.paymentEntitleMentList, function(v,k) {
    							if(v.isSmartApplicable=='Y')
    								{
    								  $scope.isSmartApplicable = v.isSmartApplicable;
    								}
    		    			});
    					}
    					else{
                            $scope.initPaymentEntitlement();
                            $scope.entitlementObj.paymentEntitlementTypeId = $scope.selfPayeeId;
                            $scope.isContractChanged ='N';
                            $scope.isPayeeUpdated ='N';
                            $scope.isDiscountApplicable = 'Y';
                            $scope.paymentEntitlementTypeIdDummy =angular.copy($scope.entitlementObj.paymentEntitlementTypeId);
                            if(($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3) ){//&& $scope.isConsumed == false
                                $scope.isValidated = true;
                            }
                            else{
                                $scope.isValidated = false;
                            }
                        }
    					//for package
    					$scope.isForceClose=true;
    					$scope.activePackageList = response.data.object;
    					if($scope.activePackageList!=null&& $scope.activePackageList.length>0)
						{
						   $scope.isValidated = true;
						   angular.forEach($scope.activePackageList, function(v,k) {
							   if(v.isForceClose==1)
								   {
								   $scope.isForceClose=false;
								   }
							   else
								   {
								   $scope.saveFlag=false;
								   if($scope.paymentEntitleMentList.length>1)
									   {
									   $scope.isValidated = true;
									   $scope.isForceClose=false;
									   }
								   else
									   {
									   $scope.isValidated = false;
									   $scope.isForceClose=false;
									   }
								   }
						   });
						}
    					else if($scope.paymentEntitleMentList.length>1)
    					{
							   $scope.isValidated = true;
							   $scope.isForceClose=false;
							  
    					}
    					else{
    						$scope.isForceClose=true;
    					}
    					$rootScope.stopLoader();
    					document.getElementById("id"+$scope.entitlementObj.paymentEntitlementTypeId).checked = true;
    				}
    				else if(response.data.status == "error")
    				{
    					   $rootScope.stopLoader();
    				}
    			});
    		 document.getElementById("id"+$scope.entitlementObj.paymentEntitlementTypeId).checked = true;
    	}
        
        
    	   
    	   /**@Author Ganesh Chaudhari 16 May 2019 */
    	    
    	    $scope.openDatePickerContractStartDate = function(index) {

    			for (var i = $scope.paymentEntitleMentCorporate.length - 1; i >= 0; i--) {
    				if (i === index) {

    					$scope.datepickerCons.opened[i] = true;
    				} else {
    					$scope.datepickerCons.opened[i] = false;
    				}
    			}
    		};

    		$scope.datepickerCons = {
    			opened : [ false ],

    		};
    	$scope.openDatePickerContractEndDate = function(index) {

    			for (var i = $scope.paymentEntitleMentCorporate.length - 1; i >= 0; i--) {
    				if (i === index) {

    					$scope.datepickerConE.opened[i] = true;
    				} else {
    					$scope.datepickerConE.opened[i] = false;
    				}
    			}
    		};

    		$scope.datepickerConE = {
    			opened : [ false ],

    		};
    	$scope.openDatePickerInsuranceStartDate = function(index) {

    			for (var i = $scope.paymentEntitleMentInsurance.length - 1; i >= 0; i--) {
    				if (i === index) {

    					$scope.datepickerInss.opened[i] = true;
    				} else {
    					$scope.datepickerInss.opened[i] = false;
    				}
    			}
    		};

    		$scope.datepickerInss = {
    			opened : [ false ],

    		};
    	   $scope.openDatePickerInsuranceEndDate = function(index) {

    			for (var i = $scope.paymentEntitleMentInsurance.length - 1; i >= 0; i--) {
    				if (i === index) {

    					$scope.datepickerInsE.opened[i] = true;
    				} else {
    					$scope.datepickerInsE.opened[i] = false;
    				}
    			}
    		};

    		$scope.datepickerInsE = {
    			opened : [ false ],

    		};
    	    
    	    $scope.getDetailsEntitlementValidation = function(){
    	    	if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
  		          $scope.isValidated = true;
  		      } 
    	    }
    	    
    		$scope.getDetailsEntitlement = function(type)
    		{
                $scope.paymentEntitlementId = type;
    			if(type == '2')
    				{
    				$("#paymentEntitlementModalOP").modal('show');
    				var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyTypeId:type};
    			    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getCompanyMasterList"
    			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
    					if (response.data.status == "success"){
    						 $scope.companyMasterInsuranceList = response.data.listObject;
    					}
    				});
    			    document.getElementById("id2").checked = true
    				}
    			if(type == '3')
    				{
    				$("#paymentEntitlementModalOP").modal('show');
    				     var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyTypeId:type};
    					    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getCompanyMasterList"
    					    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
    							if (response.data.status == "success"){
    								 $scope.companyMasterList = response.data.listObject;
    								 console.log("companyMasterList",JSON.stringify($scope.companyMasterList));
    							}
    						});
    					   document.getElementById("id3").checked = true
    				}
    			if(type == '1' && $scope.entitlementObj.paymentEntitlementTypeId != 1)
    			{
    			  //self.model.walkInSale.items = [];
    			  document.getElementById("id1").checked = true
    			  
    			  if($scope.encounterId!=null && $scope.encounterId>0){
    				  $('#payeeChange').modal('show');
    			  }
    			}
    			 $scope.entitlementObj.paymentEntitlementTypeId = type;
    			  if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
    		          $scope.isValidated = true;
    		      } 
    		}
    		
    		$scope.saveSelfSopnsor  = function()
    		{
    			angular.forEach($scope.paymentEntitleMentList, function (value, key) {
    				if(value.paymentEntitlementId!=1)
    					{
    					   value.status = 'I';
    					}
    				else {
    					value.priority = 1;
    				}
    			});
    			$scope.saveSponsorDetails();
    		}
    		
    		$scope.getContractByCompanyId = function(companyId)
    		{
    			$rootScope.startLoader();
    			var companyTypeId = $scope.entitlementObj.paymentEntitlementTypeId;
    			
    			var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyId:companyId,companyTypeId:companyTypeId,visitTypeId:1,billingBedCategoryId:$scope.opdBillingCatId};
    		    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getContractByCompanyId"
    		    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
    				if (response.data.status == "success"){
    					 $scope.contractList = response.data.listObject;
    					 //getting insurance document name start
    					 if(companyId!=null && companyId!="" && companyId!=undefined){
    							var URI = BASE_URL + ROOT_URL+"/api/global/getCompanyMasterListById/unit/"+$scope.unitId+"/org/"+$scope.organizationId+"/company/"+companyId;
    							GenericService.serviceAction("GET", URI, "")
    									.then(function(response) {
    												if (response.data.status == "success") {
    													$scope.CompanyObjDoc=response.data.listObject[0];											
    												} else {
    													alert("Error!!");
    												}
    											});
    						 }
    					//getting insurance document name End
    				}
    				$rootScope.stopLoader();
    			});
    		}
    		
    		$scope.getInsuranceContract = function(insuranceItem)
    		{
    			
    			$rootScope.startLoader();
    			var companyTypeId = $scope.entitlementObj.paymentEntitlementTypeId;
    			var companyId = insuranceItem.companyId;
    				var asscoiatedCompanyId = insuranceItem.associateCompanyId;
    					
    				
    				var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyId:companyId,companyTypeId:companyTypeId,associateCompanyId:asscoiatedCompanyId,visitTypeId:1,billingBedCategoryId:$scope.opdBillingCatId};
    			    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getContractByCompanyId"
    			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
    					if (response.data.status == "success"){
    						 $scope.assoContractList = response.data.listObject;
    						 //getting insurance document name start
    						 if(asscoiatedCompanyId!=undefined && asscoiatedCompanyId!=null && asscoiatedCompanyId != ""){
    							 var URI = BASE_URL + ROOT_URL+ "/api/unit/billing/getInsuranceMasterById/"+asscoiatedCompanyId+"/"+ $scope.organizationId+"/"+$scope.unitId;
    								GenericService.serviceAction("GET", URI, "").then(function(response) {
    													if (response.data.status == "success") {
    														$scope.insuranceObjDoc=response.data.listObject[0];													
    													} else {
    														alert("Error!!");
    													}
    												});
    						 }
    						//getting insurance document name End
    					}
    					$rootScope.stopLoader();
    				});
    		}
    		
    		$scope.getContractDetails = function(item,idx,contId)
    		{
    			//alert(idx);
    			var contractObj = null;
    			contractObj = $.grep($scope.contractList, function (doc) {
    	            return doc.contractId == contId;
    	        })[0];
    			console.log("contractObj",contractObj);
    			if(contractObj != undefined || contractObj != null)
    				{
    				 //alert("Some Values");
    				  $scope.paymentEntitleMentCorporate[0].datepickerCons =contractObj.validityFrom;
    				  $scope.paymentEntitleMentCorporate[0].datepickerConE = contractObj.validityTo;
    				}
    		}
    		
    		$scope.checkDuplicatePriority = function(item,idx,priorityVal)
    		{
    			var newDataList = [];
    			var le = $scope.paymentEntitleMentList.length;
    			var i = 0;
    			angular.forEach($scope.paymentEntitleMentList, function(v) {
    				if (idx != i) {
    					/*if(v.priority > 1){
    					v.priority = v.priority - 1;
    					}*/
    					newDataList.push(v);
    				}
    				i++;

    			});
    			
    			//$scope.paymentEntitleMentList = newDataList;
    			
    			var found = newDataList.find(ar=>ar.priority==priorityVal);
    			  if(found == undefined)
    				  {
    				  	//return false;
    				  }
    			  else
    				  {
    				  $scope.paymentEntitleMentList[idx].priority = 0;
    				  growl.warning("You are aleady assign "+priorityVal+" priority",{
    						title : "WARNING"
    					});
    					return;
    				  }
    		}
    		
    		
    		 var isContractAlreadyExists;
    			function checkContractAlreadyExistsOrNot(arr, val) {
    				/*return arr
    						.some(function(arr) {
    							if(parseInt(arr.paymentEntitlementId) == val)
    								{
    									return true;
    								}
    							else
    								{
    									return false;
    								}
    						});*/
    				  var found = arr.find(ar=>ar.paymentEntitlementId==val);
    				  if(found == undefined)
    					  {
    					  	return false;
    					  }
    				  else
    					  {
    					  	return true;
    					  }
    				  
    			}
    			
    		$scope.applyContractDetails = function(id)
    		{
    			//alert(idx);
    			
    			 var isEmpty = !(Object.values($scope.paymentEntitleMentCorporate[0]).every(x =>(x != null &&  x != '')));
    			 if(isEmpty)
    				 {
    				   growl.warning("Please select all fields.",{
    						title : "WARNING"
    					});
    					return;
    				 }
    			 else{
    		
    			isContractAlreadyExists=checkContractAlreadyExistsOrNot($scope.paymentEntitleMentList,3);
    				  //var found = $scope.array.find(ar=>ar==char);
    			if (isContractAlreadyExists) {
    				growl.warning("Can't add two contract.",{
    					title : "WARNING"
    				});
    				return;
    			}
    			else
    				{
    			var contractObj = null;
    			contractObj = $.grep($scope.contractList, function (doc) {
    	            return doc.contractId == $scope.paymentEntitleMentCorporate[0].contractId;
    	        })[0];
    			
    			var tarrifName = '-';	
    			
    			console.log("contractObj",contractObj);
    			if(contractObj != undefined || contractObj != null)
    			{
    			 // var payEntList = angular.copy($scope.paymentEntitleMentList);
    			  //$scope.paymentEntitleMentList = [];
    			  if($scope.entitlementObj.paymentEntitlementTypeId == '3')
    			  {
    			/*	  var sInsDt =  $rootScope.getChangedFormatedDateShalse($scope.paymentEntitleMentCorporate[0].datepickerCons)+" 00:00:00";
    				  var eInsDt =  $rootScope.getChangedFormatedDateShalse($scope.paymentEntitleMentCorporate[0].datepickerConE)+" 00:00:00";
    				  if(sInsDt > eInsDt)
    					  {
    						  growl.warning("Start date should be less then end date..!!		",{
    								title : "WARNING"
    							});
    							return false;
    					  }else{*/
    			  var newArr = {};
    			  newArr.paymentEntitlementName=contractObj.paymentEntitlementName;
    			  newArr.paymentEntitlementId="3";
    			  newArr.companyName = contractObj.assCompanyName;
    			  newArr.companyId = contractObj.companyId;
    			  newArr.tariffName = tarrifName;
    			  newArr.contractName = contractObj.contractName;
    			  newArr.insuranceName = "";
    			  newArr.insuranceNumber = "";
    			 // newArr.startDate = moment($scope.paymentEntitleMentCorporate[0].datepickerCons).format('DD-MM-YYYY HH:mm:ss');//contractObj.validityFrom;
    			  //newArr.endDate = moment($scope.paymentEntitleMentCorporate[0].datepickerConE).format('DD-MM-YYYY HH:mm:ss');//contractObj.validityTo;datepickerConE
    			  newArr.startDate = $rootScope.getChangedFormatedDateShalse($scope.paymentEntitleMentCorporate[0].datepickerCons)+" 00:00:00";
    			  newArr.endDate =$rootScope.getChangedFormatedDateShalse($scope.paymentEntitleMentCorporate[0].datepickerConE)+" 00:00:00";
    			  newArr.priority = "";
    			  newArr.tariffId = $scope.paymentEntitleMentCorporate[0].tariffId;
    			  newArr.contractId = contractObj.contractId;
    			  newArr.insuredAmount = '';
    			  newArr.typeId = 3;
    			  newArr.patientSponsorDetailsId=null;
    			  if($scope.CompanyObjDoc.companyId!=undefined && $scope.CompanyObjDoc.companyId!=null && $scope.CompanyObjDoc.companyId!="" && $scope.paymentEntitleMentCorporate[0].companyId==$scope.CompanyObjDoc.companyId){
    				  newArr.reportPath=$scope.CompanyObjDoc.reportPath;
    			  }
    			  isInsuranceExists=checkInsuranceExistsOrNot($scope.paymentEntitleMentList,newArr)
    				if (!isInsuranceExists) {
    					var lenCon = $scope.paymentEntitleMentList.length;
    					if($scope.paymentEntitleMentList.length > 0)
    					{
    						angular.forEach($scope.paymentEntitleMentList, function (value, key) {
    							 
    							  if(value.paymentEntitlementId == "1")
    								  {
    								   value.priority = lenCon + 1;
    								  }
    							  
    							  if(lenCon == 1)
    							  {
    							  	newArr.priority = "1";
    							  }
    						  if(lenCon >= 2)
    							  {
    							  angular.forEach($scope.paymentEntitleMentList, function (value1, key1) {
    								  if(value1.paymentEntitlementId != 1)
    								  {
    								  value1.priority =  "";
    								  }
    							  });
    							  	
    							  }
    							  
    							  
    						  });
    					}
    					
    					$scope.paymentEntitleMentList.push(newArr);
    					 
    					if($scope.paymentEntitleMentList.length > 1)
    					{
    						$scope.billTabHide = true;
    					}
    					$scope.paymentEntitleMentCorporate=
    						[{
    							'companyId':"",
    							'contractId':"",
    							//'statrDate':"",
    							//'endDate':""
    						}];
    				}
    				else
    					{
    						growl.warning("Already added in the list",{
    							title : "WARNING"
    						});
    						return;
    					}
    			  
    			  
    			  
    			  }
    					}
    				}
    			 }		
    			
    		}
    		
    		 var isInsuranceExists;
    			function checkInsuranceExistsOrNot(arr, val) {
    				return arr
    						.some(function(arr) {
    							if(val.paymentEntitlementId == parseInt(arr.paymentEntitlementId) && val.contractId == parseInt(arr.contractId))
    								{
    									return true;
    								}
    							else
    								{
    									return false;
    								}
    						});
    			}
    			
    			 var insuranceCount;
    				function checkInsuranceCount(arr) {
    					var countInsurance = 0;
    					angular.forEach($scope.paymentEntitleMentList, function (value, key) {
    						 
    						  if(value.paymentEntitlementId == "2")
    							  {
    							   countInsurance++;
    							  }
    						  
    						  
    					  });
    					return countInsurance;
    				}
    				
    		
    		$scope.applyInsuranceDetails = function()
    		{
    			
    				 var isEmpty = !(Object.values($scope.paymentEntitleMentInsurance[0]).every(x =>(x != null &&  x != '')));
    				 if(isEmpty)
    					 {
    					   growl.warning("Please select all fields.",{
    							title : "WARNING"
    						});
    						return;
    					 }
    				 else{
    					 
    			insuranceCount=checkInsuranceCount($scope.paymentEntitleMentList)
    			if (insuranceCount >= 2) {
    				growl.warning("You can't add greater than two insurance.",{
    					title : "WARNING"
    				});
    				return;
    			}
    			else
    				{
    					
    			var insuranceObj = null;
    			insuranceObj = $.grep($scope.assoContractList, function (doc) {
    	            return doc.contractId == $scope.paymentEntitleMentInsurance[0].contractId;
    	        })[0];
    			var tarrifName = '-';
    			
    			var associatedName = $.grep($scope.associateCompanyMasterList, function (doc) {
    	            return doc.id == $scope.paymentEntitleMentInsurance[0].associateCompanyId;
    	        })[0].name;
    			
    			var insuranceId = $.grep($scope.associateCompanyMasterList, function (doc) 
    					{
    		            return doc.id == $scope.paymentEntitleMentInsurance[0].associateCompanyId;
    		        })[0].id;
    			
    			console.log("$scope.paymentEntitleMentInsurance",$scope.paymentEntitleMentInsurance);
    			
    			if(insuranceObj != undefined || insuranceObj != null)
    			{
    			  //var payEntList = angular.copy($scope.paymentEntitleMentList);
    			 // $scope.paymentEntitleMentList = [];
    			  if($scope.entitlementObj.paymentEntitlementTypeId == '2')
    			  {
    				  var sInsDt =  moment($scope.paymentEntitleMentInsurance[0].datepickerInss).format('DD-MM-YYYY HH:mm:ss');
    				  var eInsDt =  moment($scope.paymentEntitleMentInsurance[0].datepickerInsE).format('DD-MM-YYYY HH:mm:ss');
    				  
    				  var isAfter = moment($scope.paymentEntitleMentInsurance[0].datepickerInsE).isAfter($scope.paymentEntitleMentInsurance[0].datepickerInss);
    				   /* alert(isAfter);
    				    //$('.result2').text('Date is After: ' + isAfter);
    				    return false;*/
    				    
    				  if(!isAfter)
    					  {
    						  growl.warning("Start date should be less then end date..!!",{
    								title : "WARNING"
    							});
    							return false;
    					  }else{
    			  var newArr = {};
    			  newArr.paymentEntitlementName=insuranceObj.paymentEntitlementName;
    			  newArr.paymentEntitlementId="2";
    			  newArr.companyName = insuranceObj.assCompanyName;
    			  newArr.companyId = insuranceObj.companyId;
    			  newArr.tariffName = tarrifName;
    			  newArr.contractName = insuranceObj.contractName;
    			  newArr.insuranceName = associatedName;
    			  newArr.insuranceId = insuranceId;
    			  newArr.insuranceNumber = $scope.paymentEntitleMentInsurance[0].insuranceNumber;
    			  newArr.startDate = moment($scope.paymentEntitleMentInsurance[0].datepickerInss).format('DD-MM-YYYY HH:mm:ss');//contractObj.validityFrom;
    			  newArr.endDate = moment($scope.paymentEntitleMentInsurance[0].datepickerInsE).format('DD-MM-YYYY HH:mm:ss');//contractObj.validityFrom;datepickerInsE
    			  newArr.priority = "";
    			  newArr.tariffId = $scope.paymentEntitleMentInsurance[0].tariffId;
    			  newArr.contractId = insuranceObj.contractId;
    			  newArr.typeId = 2;
    			  newArr.insuredAmount = $scope.paymentEntitleMentInsurance[0].insuredAmt;
    			  newArr.patientSponsorDetailsId=null;
    			  isInsuranceExists=checkInsuranceExistsOrNot($scope.paymentEntitleMentList,newArr)
    			  
    			  if($scope.insuranceObjDoc.insuranceId!=undefined && $scope.insuranceObjDoc.insuranceId!=null  && $scope.paymentEntitleMentInsurance[0].associateCompanyId==$scope.insuranceObjDoc.insuranceId){
    				  newArr.reportPath=$scope.insuranceObjDoc.reportPath;
    			  }
    				if (!isInsuranceExists) {
    					var lenIns = $scope.paymentEntitleMentList.length;
    					if($scope.paymentEntitleMentList.length > 0)
    						{
    							angular.forEach($scope.paymentEntitleMentList, function (value, key) {
    								 
    								  if(value.paymentEntitlementId == "1")
    									  {
    									   value.priority = lenIns + 1;
    									  }
    								  if(lenIns == 1)
    									  {
    									  	newArr.priority = "1";
    									  }
    								  if(lenIns >= 2)
    									  {
    									  angular.forEach($scope.paymentEntitleMentList, function (value1, key1) {
    										  if(value1.paymentEntitlementId != 1)
    										  {
    										  value1.priority =  "";
    										  }
    									  });
    									  	
    									  }
    								  
    								  
    							  });
    						}
    					$scope.paymentEntitleMentList.push(newArr);
    					
    					if($scope.paymentEntitleMentList.length > 1)
    						{
    							$scope.billTabHide = true;
    						}
    					$scope.paymentEntitleMentInsurance=
    						[{
    							'companyId':"",
    							'associateCompanyId':"",
    							'contractId':"",
    							//'statrDate':"",
    							//'endDate':"",
    							'insuranceNumber':"",
    							'insuredAmt':""
    						}];
    				}
    				else
    					{
    						growl.warning("Already added in the list",{
    							title : "WARNING"
    						});
    						return;
    					}
    			  
    			    }
    			  }
    			  
    					}
    				}
    		}
    		
    		}
    		
    		
    		
    		$scope.addContracts = function()
    		{
    			var data = {
    					'companyId':"",
    					'contractId':"",
    					//'statrDate':"",
    					//'endDate':""
    				};
    			
    			$scope.paymentEntitleMentCorporate.push(data);
    			
    		}
    		
    		$scope.removeContract = function(index)
    		{
    			console.log("removeIndex",index);
    			var newDataList = [];
    			var le = $scope.paymentEntitleMentList.length;
    			var i = 1;
    			if(le != i){
    			angular.forEach($scope.paymentEntitleMentList, function(v) {
    				if (index != i) {
    					/*if(v.priority > 1){
    					v.priority = v.priority - 1;
    					}*/
    					newDataList.push(v);
    					
    					if(le == 2)
    						{
    							if(v.paymentEntitlementId == "1")
    								{
    									v.priority = le - 1;
    								}else
    									{
    									 v.priority = 1;
    									}
    						}
    					if(le > 2)
    					{
    						
    						if(v.priority != 1)
    						{
    						 //v.priority = "";
    						 v.priority = v.priority - 1;
    						}
    						
    						/*if(v.paymentEntitlementId == "1")
    							{
    								v.priority = le - 1;
    							}else
    								{
    								v.priority = "";
    								}*/
    					}
    				}
    				i++;
    			});
    			
    			$scope.paymentEntitleMentList = newDataList;
    			if($scope.paymentEntitleMentList.length == 1)
    				{
    				$scope.paymentEntitleMentList[0].priority = "1";
    				}
    			if($scope.paymentEntitleMentList.length == 1)
    				{
    				  $scope.billTabHide = false;
    				}
    			else
    				{
    				  $scope.billTabHide = true;
    				}
    			
    			
    			//$scope.paymentEntitleMentCorporate =[];
    			//$scope.addContracts();
    			}
    			
    			
    			
    		}
    		
    		$scope.addInsurances = function()
    		{
    			var data = {
    					'companyId':"",
    					'associateCompanyId':"",
    					'contractId':"",
    					//'statrDate':"",
    					//'endDate':"",
    					'insuranceNumber':"",
    					'insuredAmt':""
    				};
    			$scope.paymentEntitleMentInsurance.push(data);
    		}
    		
    		$scope.getCompanyMasterList = function(){
    			$rootScope.startLoader();
    			var URI = BASE_URL + ROOT_URL + COMPANY_MASTER + UNIT + SLASH + $scope.unitId + ORG + SLASH + $scope.organizationId;
    			BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
    					$scope.companyMasterList = [];
    					$scope.associatedCompanyList = [];
    					if (response.data.status == "success") {
    						$scope.companyMasterList = response.data.listObject;
    					}
    				});
    		}
    		$scope.getCompanyMasterList();
    		
    		$scope.getAssociatedCompanyMasterListByCompanyId = function(companyId){
    			$rootScope.startLoader();
    			 var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyId:companyId};
    			    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getAssociateCompanyMaster"
    			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
    					if (response.data.status == "success"){
    						 $scope.associateCompanyMasterList = response.data.listObject;
    						  console.log("associateCompanyMasterList",JSON.stringify($scope.associateCompanyMasterList));
    					}
    					$rootScope.stopLoader();
    				});
    			    
    		}
    		
    		//validate And Save Contract
    		
    		$scope.saveAndValidateContract = function(bill)
    		{
    			
    			$scope.ActualsaveAndValidateContract(bill);
    			
    			/*
    			self.model.stopItemIds = [];
           	 angular.forEach(bill.items, function (item, key)  {
                    if (!item.disabled && !item.itemDisabled) 
                       {
                         self.model.stopItemIds.push(item.itemId);
                       }
                 });        	 
       		var stopData = {
      	                "itemIds": self.model.stopItemIds,
      	                "storeId": $scope.storeId   	      		 
      	              };      	    
          	 	stopTransactionService.GetstopTransactionData(stopData).then(function(responseData){        
                   var StopTransactionData = responseData;  
                   if(StopTransactionData.length > 0 ){    
                            $rootScope.stopLoader();
                            var startdate =  $filter('date')(StopTransactionData[0].startDate, "dd-MM-yyyy");
                            var stopdate =  $filter('date')(StopTransactionData[0].stopDate, "dd-MM-yyyy");                    
                            var stopMessage = "Stop Transation is in Process for this Item(s) <br/>Start Date : "+ startdate +" <br/> Stop Date: "+ stopdate +" <br/> Days: "+ StopTransactionData[0].days +" <br/>  Hours: "+ StopTransactionData[0].hours +" <br/>  Minutes: "+ StopTransactionData[0].minutes +"  ";                             
                            growl.warning(stopMessage, {onclose: function() {
                            	var popup = angular.element('#confirmStopValidate');
                             	popup.modal('show');    
                            },});
                    }
                   else{
                	   $scope.ActualsaveAndValidateContract(bill);
                   }
          	  });
    		*/}
        
    		$scope.ActualsaveAndValidateContract = function(bill)
    		{
    		  if (ValidateBill(bill)) { 
    			$rootScope.startLoader();	 
    			
    			//itemReqObject
    			 
    			 
    			 //check contract Ganesh
    	       /*  if($scope.entitlementObj.paymentEntitlementTypeId == '3'||$scope.entitlementObj.paymentEntitlementTypeId == '2')
    	   		{
    	   		   var found = $scope.paymentEntitleMentList.find(ar=>(ar.paymentEntitlementId==2||ar.paymentEntitlementId==3));
    	   		   if(found==undefined)
    	   			   {
    	   			     $rootScope.stopLoader();
    	   			     growl.error('Please Apply Payment Entitlement.');
    	   			     return false;
    	   			   }
    	   		}*/
    			/* 
    			 * code to uncomment ganesh
    			 * angular.forEach($scope.paymentEntitleMentList, function (value, key) {
    				 if(value.patientSponsorDetailsId==""){
    					 $scope.saveSponsorFlag=false;				
    				 }else{
    					 $scope.saveSponsorFlag=true;
    				 }
    			 });	 
    			
    	         if($scope.saveSponsorFlag==false){        	
    	   			     $rootScope.stopLoader();
    	   			     growl.error('Please Save Payment Entitlement.');
    	   			     return false;   			  
    	         }
    			 
    			 if(bill.items==null||bill.items.length==0)
    				 {
    				   $rootScope.stopLoader();
    				   growl.error('Please Add items to bill.');
    				   return false;
    				 }
    			 
    			  $scope.totalSelfPayable = 0.00;
             	  $scope.totalCreditPayable = 0.00;
                   if($scope.entitlementObj.paymentEntitlementTypeId == '3'||$scope.entitlementObj.paymentEntitlementTypeId == '2')
             		{
             		   var found = $scope.paymentEntitleMentList.find(ar=>(ar.paymentEntitlementId==2||ar.paymentEntitlementId==3));
             		   if(found==undefined)
             			   {
             			     growl.error('Please Apply Payment Entitlement.');
             			     return false;
             			   }
            
             		}*/
                   
                   var data = {
                		   "contractId" : '',
                		   "organizationId" : $scope.organizationId,
                		   "unitId" : $scope.unitId,
                		   "visitTypeId" : 1,
                		   "paymentEntitlementId" : $scope.entitlementObj.paymentEntitlementTypeId,
                		   "encounterId" : $scope.encounterId,
                		   "admissionId" : '',
                		   "billingBedCategoryId" : '',
                		   "patientCategoryId" : '',
                		   "defaultSelfTariffId" : '',
                		   "payeeId" : '',
                		   "selfPayeeId" : $scope.selfPayeeId,
                		   "patientClassId" : '',
                		//    "patientClassPercentage" : '',
                		   "patientId" :bill.patientId,
                		   "tariffId" : $scope.defaultSelfTariffId,
                		   "taxInclusive" : $scope.taxInclusive,
                		   "walkInSales" : false,
                		   "opSales" : true,
                		   "doctorId" : bill.doctorId,
                		   "consume" :false,
                		   "itemList" : []
                		 }
             

                    angular.forEach(bill.items, function (item, key) {
                        if (!item.disabled && !item.itemDisabled  && item.selectedBatch!=null) {
                            if(item.quantity !=undefined && item.quantity !=null && item.quantity > 0 && item.quantity !=""){
                                var billItem =  {
                            	   "itemId" : item.itemId,
                            	   "batchId" : item.selectedBatch.batchId,
                            	   "rate" : item.rate,
                            	   "quantity" : Number(item.quantity),
                            	   "selfQuantity" : '',
                            	   "packageConsumptionAmt" : 0,
                            	   "totalAmount" : item.itemtotalAmount,
                            	   "discountAmount" : item.itemDiscount,
                            	   "unitDiscountAmount" : item.unitDiscountAmount,
                            	   "netAmount" : item.netAmount.toFixed(2),
                            	   "taxId" : item.taxId,
                            	   "taxPercentage" : item.taxPercentage,
                            	   "taxAmount" : item.taxAmount,
                            	   "productCategoryId" : item.productCategoryId,
                            	   "payeeId" : (parseFloat(item.creditPayable)==0)?$scope.selfPayeeId:item.payeeId,
                            	   "contractId" : '',
                            	   "creditPayable" : item.creditPayable,
                            	   "selfPayable" : item.selfPayable,
                            	   "index" : key,
                            	   "childIndex" : '',
                                   "consessionPercentage": item.patientClassPercentage,
                                   "consessionAmount": item.patientClassAmount,
                                   "unitConsessionAmount": item.unitPatientClassAmount,
                            	   "saleDetailPayeeMapperDtoList" : !!item.saleDetailPayeeMapperDtoList ? item.saleDetailPayeeMapperDtoList : [],
                            	   "saleDetailPackageMapperDtoList" : !!item.saleDetailPackageMapperDtoList ? item.saleDetailPackageMapperDtoList : [],
                            	   "isConsumed" : false,
                            	   "consumedItemId" : item.consumedItemId,
                            	   "consumedPcategroyId" : item.consumedPcategroyId,
                                }
                                //Ganesh
                                $scope.totalSelfPayable = $scope.totalSelfPayable+ item.selfPayable;
                                $scope.totalCreditPayable = $scope.totalCreditPayable+ item.creditPayable;
                               data.itemList.push(billItem);
                            }
                        }

                       
                        if (item.selectedItems && !item.itemDisabled) {
                            angular.forEach(item.selectedItems, function (item, ckey) 
                        	{
                        	    if(!item.itemDisabled  && item.selectedBatch!=null){
                                    if(item.quantity !=undefined && item.quantity !=null && item.quantity > 0 && item.quantity !=""){
                                        var billSubItem = {
                                            "itemId" : item.itemId,
                                            "batchId" : item.selectedBatch.batchId,
                                            "rate" : item.rate,
                                            "quantity" : Number(item.quantity),
                                            "selfQuantity" : '',
                                            "packageConsumptionAmt" : 0,
                                            "totalAmount" : item.itemtotalAmount,
                                            "discountAmount" : item.itemDiscount,
                                            "unitDiscountAmount" : item.unitDiscountAmount,
                                            "netAmount" : item.netAmount.toFixed(2),
                                            "taxId" : item.taxId,
                                            "taxPercentage" : item.taxPercentage,
                                            "taxAmount" : item.taxAmount,
                                            "productCategoryId" : item.productCategoryId,
                                            "payeeId" : (parseFloat(item.creditPayable)==0)?$scope.selfPayeeId:item.payeeId,
                                            "contractId" : '',
                                            "creditPayable" : item.creditPayable,
                                            "selfPayable" : item.selfPayable,
                                            "index" : key,
                                            "childIndex" : ckey,
                                            "consessionPercentage": item.patientClassPercentage,
                                            "consessionAmount": item.patientClassAmount,
                                            "unitConsessionAmount": item.unitPatientClassAmount,
                                            "saleDetailPayeeMapperDtoList" : !!item.saleDetailPayeeMapperDtoList ? item.saleDetailPayeeMapperDtoList : [],
                                            "saleDetailPackageMapperDtoList" : !!item.saleDetailPackageMapperDtoList ? item.saleDetailPackageMapperDtoList : [],
                                            "isConsumed" : false,
                                            "consumedItemId" : item.consumedItemId,
                                            "consumedPcategroyId" : item.consumedPcategroyId,
                                        }
                                        //Ganesh
                                        $scope.totalSelfPayable = $scope.totalSelfPayable+ item.selfPayable;
                                        $scope.totalCreditPayable = $scope.totalCreditPayable+ item.creditPayable;
                                        data.itemList.push(billSubItem);
                                    }
                        	    }
                            });
                        }
                        //Ganesh
                        //data.totalSelfPayable = $scope.totalSelfPayable;
                        //data.totalCreditPayable =  $scope.totalCreditPayable;
                    });
    			 var URI = BASE_URL + ROOT_URL + "/api/pharmacy/sales/walkins/consumeItemPackageContract"
    			    BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
    					if (response.data.status == "success")
    					{
    						if(response.data.listObject.length>0)
    							{
    							
    							  angular.forEach(bill.items, function (value, key) 
    							   {
    								  angular.forEach(response.data.listObject, function (consumedItems, consumedKey) 
    										   {
    									  			if (!value.disabled && !value.itemDisabled && value.selectedBatch!=null) 
    									  			{
    									  					if(consumedItems.batchId==value.selectedBatch.batchId && consumedItems.itemId==value.itemId && key==consumedItems.index)   
    									  					{
	    									        	    value.creditPayable = Number(parseFloat(consumedItems.creditPayable).toFixed(2));
	    									        	    value.selfPayable = Number(parseFloat(consumedItems.selfPayable).toFixed(2));
	    									        	    value.netAmount =  Number(parseFloat(consumedItems.netAmount).toFixed(2));
	    									        	    value.isConsumed = false;
	    									        	    value.contractId  = consumedItems.contractId;
	    									        	    value.payeeId = consumedItems.payeeId;
	    									        	    value.packageConsumptionAmt = consumedItems.packageConsumptionAmt;
	    									        	    value.saleDetailPayeeMapperDtoList =consumedItems.saleDetailPayeeMapperDtoList;
	    									        	    value.saleDetailPackageMapperDtoList =consumedItems.saleDetailPackageMapperDtoList;
                                                            $scope.isConsumed = value.isConsumed;
                                                            if(value.isConsumed == false){
                                                                $scope.isValidated = false;
                                                            } 
	    									        	   }
    									        	  }
    									           if (value.selectedItems && !value.itemDisabled) 
    									          {
    			    									 angular.forEach(value.selectedItems, function (selectedItem, cIndex) 
    			    	    	    							   {
    			    	    	    								  angular.forEach(response.data.listObject, function (consumedSelectedItems, consumedCKey) 
    			    	    	    										   {
    			    	    	    									          if(consumedSelectedItems.batchId==selectedItem.selectedBatch.batchId && consumedSelectedItems.itemId==selectedItem.itemId 
    			    	    	    									        		  && key==consumedSelectedItems.index && cIndex==consumedSelectedItems.childIndex)   
    			    	    	    									        	  {
    			    	    	    									        	   
    			    	    	    									        	  selectedItem.creditPayable = Number(parseFloat(consumedSelectedItems.creditPayable).toFixed(2));
    			    	    	    									        	  selectedItem.selfPayable =  Number(parseFloat(consumedSelectedItems.selfPayable).toFixed(2));
    			    	    	    									        	  selectedItem.netAmount =  Number(parseFloat(consumedSelectedItems.netAmount).toFixed(2));
    			    	    	    									        	  selectedItem.isConsumed = false;
    			    	    	    									        	  selectedItem.contractId  = consumedSelectedItems.contractId;
    			    	    	    									        	  selectedItem.payeeId = consumedSelectedItems.payeeId;
    			    	    	    									        	  selectedItem.packageConsumptionAmt = consumedSelectedItems.packageConsumptionAmt;
    			    	    	    									        	  selectedItem.saleDetailPayeeMapperDtoList =consumedSelectedItems.saleDetailPayeeMapperDtoList;
    			    	    	    									        	  selectedItem.saleDetailPackageMapperDtoList =consumedSelectedItems.saleDetailPackageMapperDtoList;
                                                                                      $scope.isConsumed = selectedItem.isConsumed;
                                                                                       if(selectedItem.isConsumed == false){
                                                                                            $scope.isValidated = false;
                                                                                        } 
    			    	    	    									        	  }
    			    	    	    									       });
    			    	    	    						          
    			    	    	    						        });
    			    							       }
    									       });
    						          
    						        });
    							   CalculateItemSum(bill.items);
    							}
    						if(response.data.object!=null)
                            {
                                self.model.patientId = response.data.object.patientId;
                            }
    						//$scope.getSponsorDetails($scope.encounterId);
    						$rootScope.stopLoader();
    					}
    					else if(response.data.status == "error")
    					{
    						   $rootScope.stopLoader();
    					}
    				});
              }
    		}
        
        
        //ganesh
    		
    		
    		var groupBy = function(xs, key) {
				  return xs.reduce(function(rv, x) {
				    (rv[x[key]] = rv[x[key]] || []).push(x);
				    return rv;
				  }, {});
				};
    		  $scope.getPayeeCreditDetails = function(itemList)
    		    {
    			  var data ={
    					      payeeDesc: '',
    						  itemId: '',
    						  contractId: '',
    						  payeeId: '',
    						  payeeAmount: 0
    			            }
    		    	$scope.payeeCreditDetailslList = [];
    		    	var list =groupBy(itemList, 'contractId');
    				angular.forEach(list,function(contract,key) 
							{
    							var payeeAmount=0;
    							var obj = angular.copy(data);
								angular.forEach(contract,function(payee,key) 
										{
									       payeeAmount=payeeAmount+payee.payeeAmount;
									       obj.payeeId =payee.payeeId;
									       obj.contractId =payee.contractId;
									       obj.payeeDesc =payee.payeeDesc;
										});
								obj.payeeAmount = payeeAmount;
								$scope.payeeCreditDetailslList.push(obj);
							});
    		    }
        
    		  $scope.getPackageDetails = function(itemList)
  		    {
    			  
  		    	$scope.packageDetailsList = [];
  		    	 var data ={
  		    			  packageName: '',
  		    			  packageCode: '',
						  packageConsumptionMasterId: '',
						  packageId: '',
						  packageAmount: 0,
						  isPackageItem:'N'
			            }
		    	$scope.payeeCreditDetailslList = [];
		    	var list =groupBy(itemList, 'packageConsumptionMasterId');
				angular.forEach(list,function(packageObj,key) 
						{
							var packageAmount=0;
							var obj = angular.copy(data);
							angular.forEach(packageObj,function(pkg,key) 
									{
								       packageAmount=packageAmount+pkg.packageAmount;
								       obj.packageId =pkg.packageId;
								       obj.packageConsumptionMasterId =pkg.packageConsumptionMasterId;
								       obj.packageName =pkg.packageName;
								       obj.packageCode =pkg.packageCode;
								       obj.isPackageItem =pkg.isPackageItem;
									});
							obj.packageAmount = packageAmount;
							$scope.packageDetailsList.push(obj);
						});
  		    }
    		  
    		  /* *************************************loylti card Integration start****************************************** */
    			
    		    $scope.resend =false;
    			$scope.loyaltyCardObj ={
    					"loyaltyCardNo" :"",
    					"loyaltyCardBalance" :"",
    					"authKey":0
    			}
    			$scope.isBillList = 0;
    			
    			$scope.checkAmt = function(amt)
    			{
    				loyaltiCardCallFactory.checkAmt(amt,$scope);
    				
    			}
    			
    			$scope.getLoyaltyCardByNo = function(cardNo)
    			{
    				$scope.resend =false;
    				loyaltiCardCallFactory.getLoyaltyCardByNo(cardNo,$scope);
    				
    			}
    			
    			$scope.sendOTP = function(flag)
    			{
    				loyaltiCardCallFactory.sendOTP(flag,$scope);
    			}
    			
    			$scope.reSendOTP = function(flag)
    			{
    				loyaltiCardCallFactory.sendOTP(flag,$scope);
    			}
    			
    			$scope.clearLoylty = function(){
    				
    				$scope.send = false;
    				$scope.validatTextFieldFlag = true;
    				$scope.paymentDetails.otp ="";
    				$scope.calculateTotalDueRefAmount();
    				loyaltiCardCallFactory.getLoyaltyCardList($scope.selectedPatientId,$scope);
    			}
    			
    			$scope.validateOtp = function()
    			{
    				
    				if($scope.paymentDetails.otp==0||$scope.paymentDetails.otp==''||$scope.paymentDetails.otp==undefined||
    						$scope.paymentDetails.otp==null)
    					{
    					growl.error("Please enter valid OTP",
    							{
    								title : 'Error!'
    							});
    					    return false;
    					 }
    				else{
    				$rootScope.startLoader();
    					var data = {
    							  "patientId" : '',
    							  "organisationId" : $scope.organizationId,
    							  "unitId" : $scope.unitId,
    							  "status" : "A",
    							  "isDeleted" : "N",
    							  "isUserDefined":"Y",
    							  "otp": $scope.paymentDetails.otp,
    							  "cardNo":$scope.paymentDetails.loyaltyCardNo,
    							  "patientLoyaltyCardMpprId":$scope.loyaltyCardObj.patientLoyaltyCardMpprId,
    							  "consumeAmount":$scope.paymentDetails.loyaltyCardAmt,
    							  "authKey":$scope.loyaltyCardObj.authKey
    					}
    					var URI = BASE_URL + ROOT_URL + "/api/loyaltyCard/validateOTP";
    					BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
    						if (response.data.status == "success")
    						{
    								$rootScope.stopLoader();
    								growl.success(response.data.message,
    										{
    											title : 'Success!'
    										});
    									$scope.loyaltyCardObj.authKey = 0;
    									$scope.send=false;
    									$scope.resend=false;
    								 	$scope.validatTextFieldFlag = true;						
    								 	$scope.isBillList = 0;
    									var paymentObj = angular.copy($scope.paymentDetails);
    									 pharmacyPaymentCollFactory.addPaymentDetails($scope);
    									$scope.paymentDetails.loyaltyCardNo = $scope.loyaltyCardObj.cardNo;
    									$scope.paymentDetails.cardBalAmount = paymentObj.cardBalAmount - parseFloat(paymentObj.loyaltyCardAmt);
    								
    							} else if(response.data.status == 'error')
    							{
    								$rootScope.stopLoader();
    								growl.error(response.data.message,
    										{
    											title : 'Error!'
    										});
    						}
    			
    					});
    				}
    			}
    			
    			
    			/* *************************************loylti card Integration END****************************************** */
    			/* *************************************Mpesa Integration start****************************************** */
    			$scope.mpesaObject={"transactionId":null,"mobileNo":null,"mpesaAmount":null,"resTransactionId":null};
    			 $scope.skipMpesaValidation = false;
    			$scope.validateMpesaDetails = function(mpesaObject){
    				console.log("MPesa Object:",mpesaObject);
    				$rootScope.startLoaderPopup();
    				$scope.skipMpesaValidation = true;
    				var data ={
    						organizationId :$scope.organizationId,
    						unitId : $scope.unitId,
    						createdBy : $scope.userId,
    						createdDate : moment(new Date()).format("DD-MM-YYYY"),
    						updatedDate :  moment(new Date()).format("DD-MM-YYYY"),
    						updatedBy:$scope.userId,
    						mpesaTxcode:$scope.mpesaObject.transactionId,
    						uhid: $scope.selectedUhid,
    						mpesaSendermobile:$scope.mpesaObject.mobileNo,
    						mpesaAmount:$scope.mpesaObject.mpesaAmount
    				};
    			
    				if($scope.mpesaObject.transactionId!=null && $scope.mpesaObject.mobileNo!=null){
    				var URI9 = BASE_URL + ROOT_URL + "/Billing/validateAmountByNumber";
    				BillingGenericService.serviceAction(METHOD_POST,URI9, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
    					
    					if (response.data.status == "success")
    						{
    						
    							$scope.mpesaObject.mpesaAmount = response.data.object.mpesaAmount;
    							$scope.mpesaObject.resTransactionId = response.data.object.mpesaTxcode;
    							$scope.addPaymentDetails();
    							$rootScope.stopLoaderPopup();
    						}
    					else if(response.data.status == "error"){
    						
    						growl.error(response.data.message,{
    							title : ERROR_MSG
    						});
    						$scope.mpesaObject.mpesaAmount=null;
    						$rootScope.stopLoaderPopup();
    						$scope.skipMpesaValidation = false;
    					}
    					$rootScope.stopLoaderPopup();
    				});
    				}else{
    					growl.error("Enter both transaction id and mobile number",{
    						title : ERROR_MSG
    					});
    					$scope.skipMpesaValidation = false;
    					$rootScope.stopLoaderPopup();
    				}
    			}
    			
    			$scope.mpesaPaymentRequest = function(validateMpesaAmount,mobileNumber,countryCallingCode){
    				$scope.skipMpesaValidation = true;
    				var data ={
    						organizationId :$scope.organizationId,
    						unitId : $scope.unitId,
    						createdBy : $scope.userId,
    						createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
    						updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
    						updatedBy:$scope.userId,
    						uhidNumber: $scope.selectedUhid,
    						phoneNumber:parseInt(countryCallingCode+""+mobileNumber),
    						partyA:mobileNumber,
    						amount:validateMpesaAmount
    				};
    			
    				if((mobileNumber != undefined && mobileNumber != null && mobileNumber != "") ||
    						(countryCallingCode != undefined && countryCallingCode != null && countryCallingCode != "")){
    					$rootScope.startLoaderPopup();
    				var URIVALI = BASE_URL + ROOT_URL + "/Billing/requestSTKPushPayment";
    				BillingGenericService.serviceAction(METHOD_POST,URIVALI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
    					
    					if (response.data.status == "success")
    						{
    						
    						growl.success(response.data.message,{
    							title : SUCCESS_MSG
    						});
    						$scope.hitMpesaApi = true;
    						$rootScope.stopLoaderPopup();
    						angular.element('#paymentReqModalPhr').modal('hide');
    						}
    					else if(response.data.status == "error"){
    						
    						growl.error(response.data.message,{
    							title : ERROR_MSG
    						});
    						$rootScope.stopLoaderPopup();
    						$scope.skipMpesaValidation = false;
    					}
    					$rootScope.stopLoaderPopup();
    				});
    				}else{
    					growl.error("Please enter mobile code and mobile number. ",{
    						title : ERROR_MSG
    					});
    					$scope.skipMpesaValidation = false;
    					$rootScope.stopLoaderPopup();
    				}
    				
    			}
    			
    			$scope.validateSTKPushPayment = function(){
    				
    				var data ={
    						organizationId :$scope.organizationId,
    						unitId : $scope.unitId,
    						uhidNumber: $scope.selectedUhid
    						
    				};
    				$scope.skipMpesaValidation = true;
    				$rootScope.startLoader();
    				var URIVALI = BASE_URL + ROOT_URL + "/Billing/validateSTKPushPayment";
    				BillingGenericService.serviceAction(METHOD_POST,URIVALI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
    					
    					if (response.data.status == "success")
    						{
    						$scope.mpesaObject.mpesaAmount = response.data.object.amount;
    						$scope.mpesaObject.transactionId = response.data.object.transactionId;
    						$scope.mpesaObject.resTransactionId = response.data.object.transactionId;
    						$scope.mpesaObject.mobileNo = response.data.object.phoneNumber;
    						  if($scope.mpesaObject.transactionId != null ){
    			                    $scope.hitMpesaApi = false;
    			                    $scope.addPaymentDetails();
    			                    $scope.skipMpesaValidation = false;
    			                    }
    						growl.success(response.data.message,{
    							title : SUCCESS_MSG
    						});
    						$rootScope.stopLoader();
    						}
    					else if(response.data.status == "error"){
    						
    						growl.error(response.data.message != null ? response.data.message :"Please wait",{
    							title : ERROR_MSG
    						});
    						$rootScope.stopLoader();
    					}
    					
    				});
    				
    			}
    			
    			$interval(refreshPage,1000000);
    			function refreshPage()
    			{
    				if($scope.hitMpesaApi == true){
    					$scope.validateSTKPushPayment();
    				}

    			}
    			
    			/* *************************************Mpesa Integration END****************************************** */
    			
    			 /******POS machine***********/
    			$scope.getPOSMachineList=function(){
    				if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
    				$scope.pOSMachineList=[];
    				 var data={
    							   organizationId:$scope.organizationId,
    			 			}
    			           var URI = BASE_URL + ROOT_URL + "/api/getPOSMachineDeviceMasterList";
    				 
    					BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
    						
    						$scope.otReservationList = [];
    						if (response.data.status == "success")
    						{
    							$scope.pOSMachineList  = response.data.listObject;
    						}
    					});
    			}
    			$scope.getPOSMachineList();
    		    
    			$scope.selectedDevice=function(obj){
    				
    				if($scope.isPinPadRequired == 'Y' && $scope.registeredPinPadDevice == "POS Machine"){
    				$scope.configDetail=[];
    				angular.forEach($scope.pOSMachineList, function(value, key){
    					if(value.deviceName == obj.deviceName.toString()){
    						$scope.machineConfigDetails=value.machineConfigDetails;
    						$scope.uploadLink=value.uploadLink;
    					}
    				});
    					
    				$scope.configDetail=JSON.parse($scope.machineConfigDetails);
    				
    				$scope.custUserId = $scope.configDetail.custUserId;
    				$scope.deviceid = obj.deviceName;
    				$scope.password = $scope.configDetail.password;
    				}

    			}
    			

    			
    			$scope.getFlagSale=function(){
    				if($scope.objPosMachine.isSale =="isSale"){
    					$scope.objPosMachine.isSale =true;
    					$scope.flagForSale=true;
    					 $scope.flagForSMSpay=false;
    					 $scope.flagForBharatQr=false;
    					 $scope.objPosMachine.isBharatQr ="";
    					 $scope.objPosMachine.isSMSpay=""
    				}
    				
    			}
    		 
    		 $scope.getFlagBharatQr=function(){
    			
    				if($scope.objPosMachine.isBharatQr =="isBharatQr"){
    					$scope.objPosMachine.isBharatQr =true;
    					$scope.flagForBharatQr=true;
    					 $scope.flagForSale=false;
    					 $scope.flagForSMSpay=false;
    					 $scope.objPosMachine.isSale ="";
    					 $scope.objPosMachine.isSMSpay="";
    				}
    			}
    		 $scope.getFlagSMSpay=function(){
    				
    				if($scope.objPosMachine.isSMSpay =="isSMSpay"){
    					$scope.objPosMachine.isSMSpay =true;
    					$scope.flagForBharatQr=false;
    					 $scope.flagForSale=false;
    					 $scope.flagForSMSpay=true;
    					 $scope.objPosMachine.isSale ="";
    					 $scope.objPosMachine.isBharatQr ="";
    				}
    			}
    		 
    			 $scope.sendRequestForPOSMachine=function(){

    					if($scope.objPosMachine.deviceName != undefined && $scope.objPosMachine.deviceName !=null && $scope.objPosMachine.deviceName != "" ){
    						$rootScope.startLoaderPopup();
    						if($scope.flagForSale == true){
    						$scope.sale=
//    						{

    						/*"deviceid":($scope.objPosMachine.deviceName).toString(),
    						 "custUserId":$scope.custUserId,
    						 "password":$scope.password,
    						 "custMobileNo":$scope.patientDetails.mobileNumber,
    						 "invoice":Date.now().toString(),
    						 "amount":$scope.paymentDetails.credAmount,
    						 "transactionType":"Sale"
    						}*/
    								{
    									  "userName": $scope.custUserId,
    									  "password": $scope.password,
    									  "customerMobileNo":$scope.objPosMachine.mobileNumberForPOS != null ? $scope.objPosMachine.mobileNumberForPOS :$scope.mobileNumber,
    									  "customerEmail": $scope.patientDetails.email,
    									  "invoiceNo": Date.now().toString(),
    									  "transactionAmount": $scope.paymentDetails.credAmount,
    									  "transactionType": "1",  
    									  "payerName": $scope.patientDetails.firstName,
    									  "autoInitiateFlag": "N"
    									}


    						$scope.selectedJson=JSON.stringify($scope.sale);
    						
    					}
    					if($scope.flagForBharatQr == true){
    						$scope.bharatQr=
    						{
    								  "userName": $scope.custUserId,
    								  "password": $scope.password,
    								  "customerMobileNo":$scope.objPosMachine.mobileNumberForPOS != null ? $scope.objPosMachine.mobileNumberForPOS :$scope.mobileNumber,
    								  "customerEmail": $scope.patientDetails.email,
    								  "invoiceNo": Date.now().toString(),
    								  "transactionAmount": $scope.paymentDetails.credAmount,
    								  "transactionType": "24",  
    								  "payerName": $scope.patientDetails.firstName,
    								  "autoInitiateFlag": "N"
    								}

    						$scope.selectedJson=JSON.stringify($scope.bharatQr);
    					}
    					if($scope.flagForSMSpay == true){
    						$scope.smsPay=
    						{
    								  "userName": $scope.custUserId,
    								  "password": $scope.password,
    								  "customerMobileNo":$scope.objPosMachine.mobileNumberForPOS != null ? $scope.objPosMachine.mobileNumberForPOS :$scope.mobileNumber,
    								  "customerEmail": $scope.patientDetails.email,
    								  "invoiceNo": Date.now().toString(),
    								  "transactionAmount": $scope.paymentDetails.credAmount,
    								  "transactionType": "19",  
    								  "payerName": $scope.patientDetails.firstName,
    								  "autoInitiateFlag": "N"
    								}

    						$scope.selectedJson=JSON.stringify($scope.smsPay);
    					}
    					var date = new Date();
    					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
    					var data={
    							patientId:$scope.selectedPatientId,
    							transactionAmount:parseInt($scope.paymentDetails.credAmount),
    							organizationId:$scope.organizationId,
    							unitId:$scope.unitId,
    							createdBy:$scope.createdBy,
    							updatedBy:$scope.updatedBy,
    							status:'A',
    							createdDate:$scope.dt,
    							updatedDate:$scope.dt,
    							posMachineDeviceId:$scope.deviceid,
    							selectedPaymentModeJson:$scope.selectedJson,
    						}
    			        var URI = BASE_URL + ROOT_URL + "/api/savePOSMachineDeviceMaster";
    				 
    					BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {

    						if(response.data.status != "success"){
    							growl.error(response.data.message);
    						}else{
    							growl.success(response.data.message);
    						}

    						$rootScope.stopLoaderPopup();
    					});
    			 }else{
    				 growl.warning("Please select device.");
    				 $rootScope.stopLoader();
    			 }
    			 }

    			
    			$scope.checkValidatePinPad = function()
    			{
    				if(($scope.registeredPinPadDevice != undefined || $scope.registeredPinPadDevice != null || $scope.registeredPinPadDevice != "") 
    						&& $scope.registeredPinPadDevice == "POS Machine"){
    					$scope.getResposeList=[];
    					$scope.getGeneratedResponse=[];
    					$rootScope.startLoaderPopup();
    					var data = {
    							patientId : $scope.selectedPatientId,
    							organizationId : $scope.organizationId,
    							unitId : $scope.unitId,
    							transactionAmount:$scope.paymentDetails.credAmount,
    					}
    					
    					
    					var URI = BASE_URL + ROOT_URL + "/api/updatePOSMachineDetails";
    					BillingGenericService.serviceAction(METHOD_POST,URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
    						
    						if (response.data.status == "success")
    							{
    								console.log("saved Card Details");
    								if(response.data.object != null && response.data.object.listObject.length > 0){
    									$scope.getResposeList=response.data.object.listObject;
    									$scope.getGeneratedResponse = JSON.parse($scope.getResposeList[0].generatedReponse);
    									$scope.paymentDetails.credCardHolderName=$scope.getGeneratedResponse.cardHolderName;
//    									$scope.paymentDetails.credCardTypeId=$scope.getGeneratedResponse.cardType;
    									$scope.paymentDetails.credAmount=$scope.getGeneratedResponse.transactionAmount;
    									if($scope.flagForBharatQr == false){
    										$scope.paymentDetails.credCardNumber=$scope.getGeneratedResponse.transactionCardNumber.slice($scope.getGeneratedResponse.transactionCardNumber.length - 4);
    									}else{
    										$scope.paymentDetails.credCardNumber=$scope.getGeneratedResponse.transactionCardNumber;
    									}
    									$scope.paymentDetails.transactionNo=$scope.getGeneratedResponse.transactionID;
    									$scope.paymentDetails.credExpiryDate=moment($scope.getGeneratedResponse.expiryDate).toDate();
    									$scope.paymentDetails.requestedUniqueId=$scope.getGeneratedResponse.billNumber/*$scope.getGeneratedResponse.invoiceNumber*/;
    									$scope.paymentDetails.creditDebitCardType=$scope.getGeneratedResponse.creditDebitCardType;
    									$scope.paymentDetails.transactionTypeName=$scope.getGeneratedResponse.transactionTypeName;
    									$scope.paymentDetails.transactionDetails=($scope.paymentDetails.creditDebitCardType!= null && $scope.paymentDetails.creditDebitCardType!= "" ? $scope.paymentDetails.creditDebitCardType + ' || ':"")
    									+ ($scope.paymentDetails.credCardNumber!= null && $scope.paymentDetails.credCardNumber!= "" ? $scope.paymentDetails.credCardNumber + ' || ':"")+ ($scope.paymentDetails.transactionNo!= null && $scope.paymentDetails.transactionNo!= "" ? $scope.paymentDetails.transactionNo + ' || ':"") + ($scope.paymentDetails.transactionTypeName!= null && $scope.paymentDetails.transactionTypeName!= "" ? $scope.paymentDetails.transactionTypeName:"")
    									
    									$scope.getGeneratedResponse.cardType=angular.lowercase($scope.getGeneratedResponse.cardType);
    									angular.forEach($scope.cardTypeList,function(value, key){
    										value.cardTypeName1=angular.lowercase(value.cardTypeName);
    										if(value.cardTypeName1 == $scope.getGeneratedResponse.cardType){
    											$scope.paymentDetails.credCardTypeId=value.cardTypeId;
    										}
    									});
    									$scope.addPaymentDetails();
    									$rootScope.stopLoaderPopup();
    								}
    								else {
    									
    									growl.error("Please wait user did not paid amount.",{
    										title : ERROR_MSG
    									});
    									$rootScope.stopLoaderPopup();
    								}

    							}
    						else if(response.data.status == "error"){
    							
    							growl.error("Please wait user did not paid amount.",{
    								title : ERROR_MSG
    							});
    							$rootScope.stopLoader();
    						}
    						
    					});
    				}else{
    				//alert($location.host());
    				//var IAddress = $location.host();
    				//alert("IP ADRRESS");
    				//alert(IAddress);
    				var data ={
    						organizationId :$scope.organizationId,
    						unitId : $scope.unitId,
    						createdBy : $scope.userId,
    						createdDate : moment(new Date()).format("DD-MM-YYYY HH:mm:ss"),
    						tillId:'50002',
    						patientId: $scope.selectedPatientId,
    						//ip : IAddress
    				};
    				$scope.pinpadAmounFlag = true;
    				$rootScope.startLoaderPopup();
    				var URI9 = BASE_URL + ROOT_URL + "/Billing/savePinPanDetaild";
    				BillingGenericService.serviceAction(METHOD_POST,URI9, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
    					
    					if (response.data.status == "success")
    						{
    						
    							$scope.uniqueNumber = response.data.object.uniqueNumber;
    							$scope.pinpadTransectionId = response.data.object.pinpadTransectionId;
    							$scope.ipAddress = response.data.object.ipAddress;
    							var dataForPinPad = {
    								    amount: parseFloat($scope.dueAmount) * 100,
    									//"amount":100,
    									//tellerId: "50002",
    									tranKey: $scope.uniqueNumber
    							}
    							
    						/*	
    							$http.post('http://localhost:9991/api/sale',dataForPinPad)
    								    .then(function(response) {
    								            alert();
    								    });*/
    							
    							/* return $http({
    					                headers: {
    					                    "Accept": "application/json;odata=verbose",
    					                    "content-type": "application/json;odata=verbose"
    					                },
    					                method: "POST",
    					                url: "http://localhost:9991/api/sale",
    					                data: {
    					                    __metadata: {
    					                        "type": "SP.Data.SongsListItem"
    					                    },
    					                    "amount": "100",
    					                    "tellerId": "50002",
    					                    "tranKey": "201903282"
    					                }


    					            }).then(saveContact)
    					            .catch(function(message) {
    					                console.log("addQuestion() error:" + message);
    					            });
    							
    							 function saveContact(data, status, headers, config) {
    						            alert("Item added successfully");
    						        }*/
    							
    							/*var URI = "http://localhost:9991/api/sale";
    							var headerObjDia = {
    									'Content-Type':'application/json',
    					     };
    						 var data={};
    		               
    		             GenericServiceParamHeader
    		                 .serviceAction(METHOD_POST, URI,
    		                		 dataForPinPad,
    		                         NOTIFICATION_MSG_STATUS_FALSE,data,headerObjDia)
    		                 .then(
    		                         function(response) {
    		                             console.log(response);
    		                             alert();
    		                         });*/
    		             
    		             
//    							/$scope.ipAddress+
    							var urlprotocol = $location.protocol();
    							//var finalUrl = "http://"+$scope.ipAddress+":9991/api/sale";
    							var finalUrl = "http://127.0.0.1:9991/api/sale";
    							var URI = finalUrl;
    							BillingGenericService.serviceAction(METHOD_POST,URI,dataForPinPad,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
    								/*if (response.data.status == "success"){
    									console.log("ree",response);
    									console.log("reew",response.data);
    									console.log("reer",response.data.rspCode);*/
    								 if (response !=  undefined){	
    									if(response.data.rspCode == '100' || response.data.rspCode == '003' || response.data.rspCode == '000')
    										{
    											$scope.paymentDetails.credCardHolderName = response.data.cardholderName;
    											$scope.paymentDetails.credAmount = parseFloat(response.data.amount)/100;
    											$scope.schema = response.data.scheme;
    											var pan = response.data.pan;
    											var lastFour = pan.substr(pan.length - 4);
    											$scope.paymentDetails.credCardNumber = lastFour;
    											$scope.paymentDetails.transactionNo = response.data.retRefNr;
    											
    											
    											var cardTypeId = $.grep($scope.cardTypeList, function (card) {
    							    	            return card.pinpadScheme == $scope.schema;
    							    	        })[0].cardTypeId;
    											$scope.paymentDetails.credCardTypeId = cardTypeId;
    											
    											$scope.resString = JSON.stringify(response);
    											console.log("resString",$scope.resString); 
    											
    											var dataUPDATE = {
    													response : $scope.resString,
    													pinpadTransectionId : $scope.pinpadTransectionId,
    													patientId : $scope.selectedPatientId,
    													organizationId : $scope.organizationId,
    													unitId : $scope.unitId,
    													createdDate : moment(new Date()).format("YYYY-MM-DD"),
    													createdBy: $scope.userId,
    													status:'A'
    											}
    											
    											
    											var URIUPDATE = BASE_URL + ROOT_URL + "/Billing/updatePinpadDetails";
    											BillingGenericService.serviceAction(METHOD_POST,URIUPDATE, dataUPDATE,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
    												
    												if (response.data.status == "success")
    													{
    														console.log("saved Card Details");
    													    $scope.addPaymentDetails();
    													}
    												else if(response.data.status == "error"){
    													 $scope.pinpadAmounFlag = false;
    													growl.error("Somting wrong with save data of card..",{
    														title : ERROR_MSG
    													});
    													 $rootScope.stopLoaderPopup();
    												}
    												
    											});
    											
    											growl.success(response.data.message,{
    												title : "SUCCESS!"
    											});
    											 $rootScope.stopLoaderPopup();
    										
    										}
    									else
    										{
    										 $scope.pinpadAmounFlag = false;
    											growl.error(response.data.message,{
    												title : ERROR_MSG
    											});
    											 $rootScope.stopLoaderPopup();
    											return false;
    										}
    								 } else{
										 
										 $scope.pinpadAmounFlag = false;
										 growl.warning("Pin Pad Not Configure",{
												title : "Warning"
											});
										 $rootScope.stopLoaderPopup();
											return false;
									 }
    							});
    							
    							 $rootScope.stopLoaderPopup();
    						}
    					else {
    						 $scope.pinpadAmounFlag = false;
    						growl.error("Someting went wrong..!!",{
    							title : ERROR_MSG
    						});
    						 $rootScope.stopLoaderPopup();
    					}
    					
    				});
    			}
    			}
	    
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

                  $scope.paymentEntitlementRecalculate = function () {
                    var URI = BASE_URL + ROOT_URL + "/api/pharmacy/sales/walkins/terrifData";
                    var bill = self.model.worklistDetails;
                     var data = {
                		   "contractId" : '',
                		   "organizationId" : $scope.organizationId,
                		   "unitId" : $scope.unitId,
                		   "visitTypeId" : 1,
                		   "paymentEntitlementId" : $scope.entitlementObj.paymentEntitlementTypeId,
                		   "encounterId" : $scope.encounterId,
                		   "admissionId" : '',
                		   "billingBedCategoryId" : '',
                		   "patientCategoryId" : bill.patientCategoryId,
                		   "defaultSelfTariffId" : '',
                		   "payeeId" : '',
                		   "selfPayeeId" : $scope.selfPayeeId,
                		   "patientClassId" : '',
                		//    "patientClassPercentage" : '',
                		   "patientId" :bill.patientId,
                		   "tariffId" : $scope.defaultSelfTariffId,
                		   "taxInclusive" : $scope.taxInclusive,
                		   "walkInSales" : false,
                		   "opSales" : true,
                		   "doctorId" : bill.doctorId,
                		   "itemList" : []
                		 }
             

                   angular.forEach(bill.items, function (item, key) {
                       if (!item.disabled && !item.itemDisabled) {
                           var billItem =  {
                            	   "itemId" : item.itemId,
                            	   "batchId" : item.selectedBatch.batchId,
                            	   "rate" : item.rate,
                            	   "quantity" : Number(item.quantity),
                            	   "selfQuantity" : '',
                            	   "packageConsumptionAmt" : 0,
                            	   "totalAmount" : item.itemtotalAmount,
                            	   "discountAmount" : item.itemDiscount,
                            	   "unitDiscountAmount" : item.unitDiscountAmount,
                            	   "netAmount" : item.netAmount.toFixed(2),
                            	   "taxId" : item.taxId,
                            	   "taxPercentage" : item.taxPercentage,
                            	   "taxAmount" : item.taxAmount,
                            	   "productCategoryId" : item.productCategoryId,
                            	   "payeeId" : (parseFloat(item.creditPayable)==0)?$scope.selfPayeeId:item.payeeId,
                            	   "contractId" : '',
                            	   "creditPayable" : item.creditPayable,
                            	   "selfPayable" : item.selfPayable,
                            	   "index" : key,
                            	   "childIndex" : '',
                                   "assetCategoryId": item.assetCategoryId,
                                   "consessionPercentage": item.patientClassPercentage,
                                   "consessionAmount": item.patientClassAmount,
                                   "unitConsessionAmount": item.unitPatientClassAmount,
                            	   "saleDetailPayeeMapperDtoList" : !!item.saleDetailPayeeMapperDtoList ? item.saleDetailPayeeMapperDtoList : [],
                            	   "saleDetailPackageMapperDtoList" : !!item.saleDetailPackageMapperDtoList ? item.saleDetailPackageMapperDtoList : [],
                            	   "isConsumed" : false
                            	 }
                           //Ganesh
                           $scope.totalSelfPayable = $scope.totalSelfPayable+ item.selfPayable;
                           $scope.totalCreditPayable = $scope.totalCreditPayable+ item.creditPayable;
                           data.itemList.push(billItem);
                       }

                       
                       if (item.selectedItems && !item.itemDisabled) {
                           angular.forEach(item.selectedItems, function (item, ckey) 
                        	{
                               var billSubItem = {
                            	   "itemId" : item.itemId,
                            	   "batchId" : item.selectedBatch.batchId,
                            	   "rate" : item.rate,
                            	   "quantity" : Number(item.quantity),
                            	   "selfQuantity" : '',
                            	   "packageConsumptionAmt" : 0,
                            	   "totalAmount" : item.itemtotalAmount,
                            	   "discountAmount" : item.itemDiscount,
                            	   "unitDiscountAmount" : item.unitDiscountAmount,
                            	   "netAmount" : item.netAmount.toFixed(2),
                            	   "taxId" : item.taxId,
                            	   "taxPercentage" : item.taxPercentage,
                            	   "taxAmount" : item.taxAmount,
                            	   "productCategoryId" : item.productCategoryId,
                            	   "payeeId" : (parseFloat(item.creditPayable)==0)?$scope.selfPayeeId:item.payeeId,
                            	   "contractId" : '',
                            	   "creditPayable" : item.creditPayable,
                            	   "selfPayable" : item.selfPayable,
                            	   "index" : key,
                            	   "childIndex" : ckey,
                                   "consessionPercentage": item.patientClassPercentage,
                                   "consessionAmount": item.patientClassAmount,
                                   "unitConsessionAmount": item.unitPatientClassAmount,
                            	   "saleDetailPayeeMapperDtoList" : !!item.saleDetailPayeeMapperDtoList ? item.saleDetailPayeeMapperDtoList : [],
                            	   "saleDetailPackageMapperDtoList" : !!item.saleDetailPackageMapperDtoList ? item.saleDetailPackageMapperDtoList : [],
                            	   "isConsumed" : false
                            	 }
                               //Ganesh
                               $scope.totalSelfPayable = $scope.totalSelfPayable+ item.selfPayable;
                               $scope.totalCreditPayable = $scope.totalCreditPayable+ item.creditPayable;
                               data.itemList.push(billSubItem);
                           });
                       }
                   });
                     
                     
                     GenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
                      //  var test = response.data.object.itemList;
                    	
                    	if(response.data.status=="success")
                    		{
                    		 angular.forEach(response.data.object.itemList,function(data,key) {
                                 
                            	 angular.forEach(self.model.worklistDetails.items,function(item,key) {
                                     
                            		 if(data.itemId == item.itemId)
                            			 {
    	                        			 angular.forEach(item.batch,function(batch,key) {
                                                 if(batch.batchId != undefined){
                                                    if(data.batchId == batch.batchId){
                                                        var data1 = {
                                                            "mrp": batch.rate,
                                                            "isTaxInc": $scope.taxInclusive,
                                                            // "markup": !!data.markUpDown ? data.markUpDownPer : null,
                                                            // "markdown": !data.markUpDown ? data.markUpDownPer : null,
                                                             "markup": data.markUpDown,
                                                            "markUpDownPer" : data.markUpDownPer,
                                                            "discount": item.discount == 0 || item.discount == undefined ? item.discountPerItem== 0 || item.discountPerItem == undefined  ? item.discountPerItem : item.discount : 0,
                                                            "isDiscPercent": !!item.discountType && item.discountType == 1 ? true : false,
                                                            "tax": item.taxPercentage,
                                                            "qty": (item.quantity!=undefined && item.quantity!=null && item.quantity != 0) ? item.quantity : item.issuedQty,
                                                            "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                                            "patientClassPercentage": item.patientClassPercentage,
                                                        }

                                                        var response = commonDetailService.calculateAmount(data1);
                                                        item.rate = response.rate;
                                                        item.taxAmount = isNaN(parseFloat(response.taxAmount))?0:response.taxAmount;
                                                        item.responseTaxAmount =  isNaN(parseFloat(response.taxAmount))?0:response.taxAmount;;
                                                        item.unitDiscountAmount = isNaN(parseFloat(response.unitDiscountAmount))?0:response.unitDiscountAmount;;
                                                        item.itemDiscount =  isNaN(parseFloat(response.discountAmount))?0:response.discountAmount;
                                                        item.itemtotalAmount = isNaN(parseFloat(response.totalAmount))?0:response.totalAmount;
                                                        item.totalAmount = isNaN(parseFloat(response.totalAmount))?0:response.totalAmount;
                                                        item.netAmount = isNaN(parseFloat(response.netAmount))?0:response.netAmount;
                                                        item.selfPayable = isNaN(parseFloat(response.netAmount))?0:response.netAmount;
                                                        item.patientClassAmount = isNaN(parseFloat(response.patientClassAmount))?0:response.patientClassAmount;
                                                        item.unitPatientClassAmount = isNaN(parseFloat(response.unitPatientClassAmount))?0:response.unitPatientClassAmount;
                                                        item.creditPayable = isNaN(parseFloat(response.creditPayable))?0:response.creditPayable;
                                                        item.packageConsumptionAmt = isNaN(parseFloat(response.packageConsumptionAmt))?0:response.packageConsumptionAmt;
                                                    }   
                                                 }
    	                                     });
                            			 }
                            		
                                 });
                               });
                    		  CalculateItemSum(self.model.worklistDetails.items);
                    		}
                    	else {
                    	}
                        
                       
                       /* test.forEach(function(data)
                        		{
                            self.model.worklistDetails.forEach(function(item){
                                item.batchList.forEach(function (batch){
                 
                                });
                            });
                        });*/
                        
                    });
                 }
    			 
    			 $scope.saveSponsorDetails=function(){
    				 $rootScope.startLoader();	
        	         if($scope.entitlementObj.paymentEntitlementTypeId == '3'||$scope.entitlementObj.paymentEntitlementTypeId == '2')
        	   		{
        	   		   var found = $scope.paymentEntitleMentList.find(ar=>(ar.paymentEntitlementId==2||ar.paymentEntitlementId==3));
        	   		   if(found==undefined)
        	   			   {
        	   			     $rootScope.stopLoader();
        	   			     growl.error('Please Apply Payment Entitlement.');
        	   			     return false;
        	   			   }
        	   		   
	        	   		var priorityFound = $scope.paymentEntitleMentList.find(ar=>(ar.priority==null||ar.priority==''));
	  			        if(!(priorityFound==undefined))
	  		   			   {
	  		   			     $rootScope.stopLoader();
	  		   			     growl.error('Please select priority');
	  		   			     return false;
	  		   			   }
        	   		}
        	         
        	         var data = {
        					  "walkInSales":false,
        			          "patientId" : $scope.idpatient
        			        }
        	         
        	         if(parseInt($scope.entitlementObj.paymentEntitlementTypeId)!=parseInt($scope.paymentEntitlementTypeIdDummy))
        	        	 {
        	        	   $scope.isPayeeUpdated='Y';
        	        	 }
        	         else if(parseInt($scope.entitlementObj.paymentEntitlementTypeId)==parseInt($scope.paymentEntitlementTypeIdDummy))
        	        	 {
        	        	   if($scope.paymentEntitleMentList.length!=$scope.paymentEntitleMentListDummy.length)
        	        		   {
        	        		     $scope.isContractChanged ='Y';
        	        		   }
        	        	 }
        	         
        	        $scope.currentDate = new Date();
         			$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
         			var sponserList = [];
         			angular.forEach($scope.paymentEntitleMentList, function (value, key) {
         				
         				var newArr = {};
         				
         				newArr.patientId= $scope.idpatient;
         				newArr.patientSponsorDetailsId = value.patientSponsorDetailsId;
         				newArr.organizationId = $scope.organizationId ;
         				newArr.unitId = $scope.unitId;
         				newArr.visitTypeId = 1;
//         				newArr.patientId = $scope.patientId;
         				newArr.paymentEntitlementId =value.paymentEntitlementId;
         				newArr.payeeId =  (value.companyId==null||value.companyId==undefined)?$scope.selfPayeeId:value.companyId;
         				newArr.priorityId = value.priority;
         				newArr.createdBy =$scope.userId;
         				newArr.createdDate = $scope.currentDate;
         				newArr.updatedBy = $scope.userId;
         				newArr.updatedDate = $scope.currentDate;
         				newArr.status = (value.status==undefined||value.status==null||value.status=='')?'A':value.status;
         				newArr.tariffId =value.tariffId;
         				newArr.contractId =value.contractId;
         				newArr.insuranceNumber = value.insuranceNumber;
         				newArr.insuranceId = value.insuranceId;
         				newArr.insuredAmount = value.insuredAmount;
         				newArr.validityTo = value.endDate;
         				newArr.validityFrom = value.startDate;
         				newArr.isPayeeUpdated = $scope.isPayeeUpdated;
         				newArr.isContractChanged = $scope.isContractChanged;
         				sponserList.push(newArr);
         			});
         			var param = {
   					     contractId:'',
   					     patientId: $scope.idpatient,
   					     encounterId: $scope.encounterId,
   					     orgId:$scope.organizationId,
   					     unitId:$scope.unitId,
   					     request:data,
   					     patientSponsorDetailsDtosList:sponserList,
   					     selfPayeeId : $scope.selfPayeeId
   					    };
         			 var URI = BASE_URL + ROOT_URL + "/api/pharmacy/sales/walkins/savePatintAndSponsor"
     			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
     					if (response.data.status == "success")
     					{
                            $scope.saveSponsorFlag=true;
                            $scope.getSponsorDetails($scope.encounterId);
                            $scope.paymentEntitlementRecalculate();
     					}
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

        $scope.addImageDetailsForContract = function(obj)
        { 
            var encounterDetails = angular.copy($scope.encounterDetailsContract);
            encounterDetails.patientId = $scope.patientDetails.patientId;
            //insurance
            if( obj.paymentEntitlementId==2)
            {
            encounterDetails.documentTypeId = '30';	
            encounterDetails.paymentEntitlementId =obj.paymentEntitlementId;
            }
            else if(obj.paymentEntitlementId==3)
            {
            encounterDetails.documentTypeId = '19';	
            encounterDetails.paymentEntitlementId =obj.paymentEntitlementId;	
            }
            else
            {
            encounterDetails.paymentEntitlementId =obj.paymentEntitlementId;	
            }
            $scope.encounterDetailsContractList.push(encounterDetails);
        };

        $scope.removeImageDetailsContract = function(deleteIndex)
        { 
            $scope.encounterDetailsContractList.splice(deleteIndex, 1);
        }

        $scope.openDocument = function(obj)
		{ 
		    $('#fileId').val(undefined);
			$scope.encounterDetailsContract = {
                encounterDocId : "",
                encounterId : 0,
                admissionId:0,
                visitTypeId:1,
                patientId : "",
                documentPath : "",
                documentTypeId : '',
                status : 'A',
                createdDate : "",
                createdBy : $scope.userId,
                updatedDate : "",
                updatedBy : $scope.userId,
                unitId : $scope.unitId,
                organisationId : $scope.organizationId,
                file : {}
            }
		
            if($scope.idpatient == null || $scope.idpatient =='' || $scope.idpatient ==undefined){
                growl.warning(
                    'Please select atleast one patient!!!.',
                {
                    title : 'warning!'
                });
                $scope.encounterDetailsContractList =[];
                return;
            }
				 
            if($scope.paymentEntitlementId==1){						
                $scope.encounterDetailsContractList =[];
                var encounterDetails = angular.copy($scope.encounterDetailsContract);
                encounterDetails.patientId = $scope.idpatient;
                encounterDetails.encounterId = $scope.encounterId;
                encounterDetails.paymentEntitlementId = obj.paymentEntitlementId;
                $scope.encounterDetailsContractList.push(encounterDetails);	
                $scope.getPatientDocumentList(encounterDetails);
                $('#documentUploadModalForOP').modal('show');
            }else if($scope.paymentEntitlementId==2){
                $scope.encounterDetailsContractList =[];
                var encounterDetails = angular.copy($scope.encounterDetailsContract);
                encounterDetails.documentTypeId = '30';	
                encounterDetails.patientId = $scope.idpatient;
                encounterDetails.encounterId = $scope.encounterId;
                encounterDetails.paymentEntitlementId = obj.paymentEntitlementId;
                $scope.encounterDetailsContractList.push(encounterDetails);
                $scope.getPatientDocumentList(encounterDetails);
                $('#documentUploadModalForOP').modal('show');
            }else if($scope.paymentEntitlementId==3){
                $scope.encounterDetailsContractList =[];
                var encounterDetails = angular.copy($scope.encounterDetailsContract);
                encounterDetails.documentTypeId = '19';	
                encounterDetails.patientId = $scope.idpatient;
                encounterDetails.encounterId = $scope.encounterId;
                encounterDetails.paymentEntitlementId = obj.paymentEntitlementId;
                $scope.encounterDetailsContractList.push(encounterDetails);
                $scope.getPatientDocumentList(encounterDetails);		
                $('#documentUploadModalForOP').modal('show');
            }else{
						
            }			
		}

        $scope.getDocumentType = function(){
            $rootScope.startLoader();
            var URI = BASE_URL + ROOT_URL + GETACTIVEDOCUMENTTYPELIST;
            BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                $scope.documentTypeList = [];
                if (response.data.status == "success") {
                    $scope.documentTypeList = response.data.listObject;
                        angular.forEach($scope.documentTypeList, function(value, key) 
                        {  
                        $scope.corporateDocTypeId=value.documentTypeId;
                        });
                }
            });
        }
        $scope.getDocumentType();


        $scope.getPatientDocumentList = function(obj)
        {
            if($scope.idpatient == null || $scope.idpatient == '' || $scope.idpatient == undefined){
			    growl.warning(
					'Please select atleast one patient!!!.',
					{
						title : 'warning!'
					});
				$scope.encounterDetailsContractList =[];
				return;
		    }
            $rootScope.startLoader();
            var data =
            {
                "patientId": $scope.idpatient,
                "unitId":$scope.unitId,
                "organisationId":$scope.organizationId,
                "userId":$scope.userId,
                "visitTypeId":1,
                "encounterId": $scope.encounterId,
            }
            var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
            BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                $scope.patientDocumentList = [];
                if (response.data.status == "success") 
                {

                    $scope.patientAllDocumentList = response.data.listObject;
                    $scope.patientDocumentList=[];
                    angular.forEach($scope.patientAllDocumentList, function(value, key) 
                    {  
                        if($scope.paymentEntitlementId==1){
                            if(value.documentTypeId !=30 && value.documentTypeId!=19){				            				
                                var data={
                                    documentTypeId:value.documentTypeId,
                                    documentName:value.documentName,
                                    documentPath:value.documentPath,
                                    createdDate:value.createdDate,
                                    bucketFlag:value.bucketFlag
                                }
                                $scope.patientDocumentList.push(data);
                            }
                        }else if($scope.paymentEntitlementId==2){
                            if(value.documentTypeId==30){				            				
                                var data={
                                    documentTypeId:value.documentTypeId,
                                    documentName:value.documentName,
                                    documentPath:value.documentPath,
                                    createdDate:value.createdDate,
                                    bucketFlag:value.bucketFlag
                                }
                                $scope.patientDocumentList.push(data);
                            }
                        }else if($scope.paymentEntitlementId==3){
                            if(value.documentTypeId==19){				            				
                                var data={
                                    documentTypeId:value.documentTypeId,
                                    documentName:value.documentName,
                                    documentPath:value.documentPath,
                                    createdDate:value.createdDate,
                                    bucketFlag:value.bucketFlag
                                }
                                $scope.patientDocumentList.push(data);
                            }
                        }else{
                    
                        }	
                    });
                    $rootScope.stopLoader();
                }
                else if(response.data.status == "error")
                {
                    $rootScope.stopLoader();
                }
            });
        }

        $scope.uploadContractDoc = function()
        {
            $scope.countFrValidation = 0;
            angular.forEach($scope.encounterDetailsContractList, function(value, key) {  
                if(value.documentTypeId==undefined || value.documentTypeId=="" || value.documentTypeId==null){
                    growl.warning("Please select document type .",
                    {
                        title : 'warning!'
                    });
                    $rootScope.stopLoader();
                    return false;
                }
                if (angular.equals(value.file, {}) || value.file==undefined || value.file==null) {
                    growl.warning('Please select file.',
                    {
                        title : 'warning!'
                    });
                    $rootScope.stopLoader();
                    return false;
                } 
                var ext = value.file.name.split(".").pop().toLowerCase();
                if($.inArray(ext, ["doc","pdf",'docx',"txt"]) == -1) {
                    growl.warning('You can only upload *.txt, *.pdf, *.docx, *.doc file',
                    {
                        title : 'warning!'
                    });
                    $rootScope.stopLoader();
                    return false;
                }
                $scope.countFrValidation++;
			});
            if($scope.encounterDetailsContractList.length!=$scope.countFrValidation){
                return false;
            }
            $rootScope.startLoader();
            $scope.count = 0;
            angular.forEach($scope.encounterDetailsContractList, function(value, key) 
            {  
                $('#documentUploadModalForOP').modal('hide');
                var promise = $scope.uploadEncounterContractDoc(value);
                promise.then(function(response) 
                {
                    if(response.data.status == "success")
                    {
                        $scope.count++;
                        if($scope.encounterDetailsContractList.length==$scope.count)
                        {
                            growl.success("Document Uploaded Successfully.",
                            {
                                title : 'Success!'
                            });
                            $rootScope.stopLoader();
                            $scope.encounterDetailsContractList = [];
                            $scope.getPatientDocumentList();
                        }
                    }
                    else 
                    {
                        growl.error('Something went wrong.',
                        {
                            title : 'Error!'
                        });
                        $rootScope.stopLoader();
                    }
                });
            });
        }

        $scope.uploadEncounterContractDoc = function(Obj)
		{
            var fd = new FormData();
            fd.append('encounterId', $scope.encounterId);
            fd.append('visitTypeId', Obj.visitTypeId);
            fd.append('admissionId', $scope.encounterId);
            fd.append('patientId',$scope.idpatient);
            fd.append('documentTypeId', Obj.documentTypeId);
            fd.append('createdBy',Obj.createdBy );
            fd.append('updatedBy', Obj.updatedBy);
            fd.append('unitId', Obj.unitId);
            fd.append('organisationId', Obj.organisationId);
            fd.append('file',Obj.file);
            fd.append('expenseId', 0);
            fd.append('grnId',0);
            fd.append('poId',0);
            fd.append('incidentReportingMapperId',0);
            fd.append('companyExpenseId',0);
            fd.append('createdDate',null);
			fd.append('fileEditedName',null);
            fd.append('opbId',0);
            fd.append('vendorId',0);
            var URI1 = BASE_URL + ROOT_URL + UPLOAD_ENCOUNTER_DOCUMENT;
            return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
            {	
                return response;
            });
		}
        $scope.focusInput=false;
        $scope.redirectToItemSearch=function(event)
        {
        	if(event.which == 13 || event.keyCode == 13) // for crossbrowser
        	   {
        		$scope.focusInput=true;
        	    // event.preventDefault(); // this code prevents other buttons triggers use this
        	     // do stuff
        	   }	
        }
        
        $scope.cancelPaymentReqModal = function(){
    		angular.element('#paymentReqModalPhr').modal('hide');
    	}
    	
    	$scope.clearMpesaFields = function(){
    		$scope.skipMpesaValidation = false;
    		$scope.mpesaObject={"transactionId":null,"mobileNo":null,"mpesaAmount":null,"resTransactionId":null};
    	}
    	
    	$scope.clearCardFields =  function(){
    		$scope.pinpadAmounFlag = false;
    		$scope.paymentDetails.credCardHolderName  = "";
    		$scope.paymentDetails.credCardTypeId  = "";
    		$scope.paymentDetails.credCardNumber  = "";
    		$scope.paymentDetails.credExpiryDate  = "";
    		$scope.paymentDetails.transactionNo  = "";
    		$scope.paymentDetails.credAmount  = "";
    		$scope.flagForSale=false;
   		 $scope.flagForSMSpay=false;
   		 $scope.flagForBharatQr=false;
   		 $scope.objPosMachine.deviceName = "";
   		 $scope.objPosMachine.isSale = false;
   		 $scope.objPosMachine.isBharatQr = false;
   		 $scope.objPosMachine.isSMSpay = false;
    		$scope.paymentDetails.credAmount  = $scope.dueAmount.toFixed(2);
    	}

        $scope.formulationPopup = function (item) {
            $scope.formulationList = [];
            var obj = {
                "manufacturerName" : ''
            }
            obj.manufacturerName = item.manufacturerName;
            $scope.formulationList.push(obj);
            $('#formulationType').modal('show');
        }
        
        

   	
   	 $scope.sendPaymentLink = function(){
         if($scope.exceededBillAmountSettlement==false)
         {
         if(parseFloat($scope.dueAmount)<parseFloat($scope.razorPayObject.razorAmount))
             {
             $scope.razorPayObject.razorAmount= parseFloat($scope.dueAmount).toFixed(2);
             growl.warning("Entered amount is greater than current amount");
             return false;
             }
         }
   			if($scope.razorPayObject.razorMobileNo == undefined || $scope.razorPayObject.razorMobileNo == null || $scope.razorPayObject.razorMobileNo =='')
   				{
   				growl.error("Please enter mobile Number",
   						{
   							title : 'Error!'
   						});
   				    return false;
   				 }
   			else if($scope.razorPayObject.razorAmount == undefined || $scope.razorPayObject.razorAmount == null || $scope.razorPayObject.razorAmount =='')
   			{
   				growl.error("Please enter amount",
   						{
   							title : 'Error!'
   						});
   				    return false;
   				 }
   			else{
   			$rootScope.startLoader();
   				var data = {
   						  "organizationId" : $scope.organizationId,
   						  "unitId" : $scope.unitId,
   						  "mobileNo":$scope.razorPayObject.razorMobileNo,
   						  "razorAmount":$scope.razorPayObject.razorAmount,
   						  "callbackUrl":BASE_URL + ROOT_URL + "/getRazerPay",
   						  "email":$scope.email,
   						 "name":self.model.billDetailList.patientName,
   						  
   				}
   				var URI = BASE_URL + ROOT_URL + "/razorPayPaymentLink";
   				BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
   					if (response.data.status == "success")
   					{
   							$rootScope.stopLoader();
   							growl.success(response.data.message,
   									{
   										title : 'Success!'
   									});
   							
   						} else if(response.data.status == 'error')
   						{
   							$rootScope.stopLoader();
   							growl.error(response.data.message,
   									{
   										title : 'Error!'
   									});
   					}
   		
   				});
   			}
   			
   		}
   		
   		
   		
   		$scope.validateRazerPay = function(){
   			

   			if($scope.razorPayObject.razorMobileNo == undefined || $scope.razorPayObject.razorMobileNo == null || $scope.razorPayObject.razorMobileNo =='')
   				{
   				growl.error("Please enter mobile Number",
   						{
   							title : 'Error!'
   						});
   				    return false;
   				 }
   			else {
   			$rootScope.startLoader();
   				var data = {
   						  "organizationId" : $scope.organizationId,
   						  "unitId" : $scope.unitId,
   						  "mobileNo":$scope.razorPayObject.razorMobileNo,
   						  
   				}
   				var URI = BASE_URL + ROOT_URL + "/validateRazorPayPaymentLink";
   				BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
   					if (response.data.status == "success")
   					{
   							$rootScope.stopLoader();
   							$scope.razorPayObject.razorPaidAmount = response.data.object.razorAmount;
   							$scope.razorPayObject.razorUniqueId = response.data.object.uniqueId;
   							 pharmacyPaymentCollFactory.addPaymentDetails($scope);
   							$scope.calculateTotalDueRefAmount();
   						} else if(response.data.status == 'error')
   						{
   							$rootScope.stopLoader();
   							growl.error(response.data.message,
   									{
   										title : 'Error!'
   									});
   					}
   		
   				});
   			}
   		}
   		
   		
   	$scope.PayByRazorPayment = function(){
        if($scope.exceededBillAmountSettlement==false)
        {
        if(parseFloat($scope.dueAmount)<parseFloat($scope.razorPayObject.razorAmount))
            {
            $scope.razorPayObject.razorAmount= parseFloat($scope.dueAmount).toFixed(2);
            growl.warning("Entered amount is greater than current amount");
            return false;
            }
        }
   		if($scope.razorPayObject.razorMobileNo == undefined || $scope.razorPayObject.razorMobileNo == null || $scope.razorPayObject.razorMobileNo =='')
   			{
   			growl.error("Please enter mobile Number",
   					{
   						title : 'Error!'
   					});
   			    return false;
   			 }
   		else if($scope.razorPayObject.razorAmount == undefined || $scope.razorPayObject.razorAmount == null || $scope.razorPayObject.razorAmount =='')
   		{
   			growl.error("Please enter amount",
   					{
   						title : 'Error!'
   					});
   			    return false;
   			 }
   		else{
   		$rootScope.startLoader();
   			var data = {
   					  "organizationId" : $scope.organizationId,
   					  "unitId" : $scope.unitId,
   					  "mobileNo":$scope.razorPayObject.razorMobileNo,
   					  "razorAmount":$scope.razorPayObject.razorAmount,
   					  "callbackUrl":BASE_URL + ROOT_URL + "/getRazerPay",
   					  "email":$scope.email ,
   					 "name":self.model.billDetailList.patientName,
   					  
   			}
   			var URI = BASE_URL + ROOT_URL + "/razorPayPaymentLink";
   			BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
   				if (response.data.status == "success")
   				{
   						$rootScope.stopLoader();
   						$window.open(response.data.object.shortUrl, '_blank');
   						
   					} else if(response.data.status == 'error')
   					{
   						$rootScope.stopLoader();
   						growl.error(response.data.message,
   								{
   									title : 'Error!'
   								});
   				}
   	
   			});
   		}
   	}
   	
   	
   	
   	 $scope.sendPaymentLinkCCAvenue = function(){
   			
   			if($scope.razorPayObject.razorMobileNo == undefined || $scope.razorPayObject.razorMobileNo == null || $scope.razorPayObject.razorMobileNo =='')
   				{
   				growl.error("Please enter mobile Number",
   						{
   							title : 'Error!'
   						});
   				    return false;
   				 }
   			else if($scope.razorPayObject.razorAmount == undefined || $scope.razorPayObject.razorAmount == null || $scope.razorPayObject.razorAmount =='')
   			{
   				growl.error("Please enter amount",
   						{
   							title : 'Error!'
   						});
   				    return false;
   				 }
   			else{
   			$rootScope.startLoader();
   				var data = {
   					 "organizationId" : $scope.organizationId,
  					  "unitId" : $scope.unitId,
  					  "mobileNo":$scope.razorPayObject.razorMobileNo,
  					  "requestAmount":$scope.razorPayObject.razorAmount,
  					  "emailId":$scope.email,
  					 "name":self.model.billDetailList.patientName,
  					  "patientId":  self.model.billDetailList.patientId, 
  					  "createdBy": $scope.userId, 
  					  "updatedBy": $scope.userId,
   						  
   				}
   				var URI = BASE_URL + ROOT_URL + "/sendCCAvenueLink";
   				BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
   					if (response.data.status == "success")
   					{
   							$rootScope.stopLoader();
   							growl.success(response.data.message,
   									{
   										title : 'Success!'
   									});
   							
   						} else if(response.data.status == 'error')
   						{
   							$rootScope.stopLoader();
   							growl.error(response.data.message,
   									{
   										title : 'Error!'
   									});
   					}
   		
   				});
   			}
   			
   		}
   		
   		
   		
   		$scope.validateRazerPayCCAvenue = function(){
   			

   			if($scope.razorPayObject.razorMobileNo == undefined || $scope.razorPayObject.razorMobileNo == null || $scope.razorPayObject.razorMobileNo =='')
   				{
   				growl.error("Please enter mobile Number",
   						{
   							title : 'Error!'
   						});
   				    return false;
   				 }
   			else {
   			$rootScope.startLoader();
   				var data = {
   						  "organizationId" : $scope.organizationId,
   						  "unitId" : $scope.unitId,
   						  "mobileNo":$scope.razorPayObject.razorMobileNo,
   						 "patientId":  self.model.billDetailList.patientId, 
   						  
   				}
   				var URI = BASE_URL + ROOT_URL + "/validateCCAvenuePayment";
   				BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
   					if (response.data.status == "success")
   					{
   							$rootScope.stopLoader();
   							$scope.razorPayObject.razorPaidAmount = response.data.object.paidAmount;
   							$scope.razorPayObject.razorUniqueId = response.data.object.orderNo;
   							// pharmacyPaymentCollFactory.addPaymentDetails($scope);
   							$scope.addPaymentDetails();
   							$scope.calculateTotalDueRefAmount();
   						} else if(response.data.status == 'error')
   						{
   							$rootScope.stopLoader();
   							growl.error(response.data.message,
   									{
   										title : 'Error!'
   									});
   					}
   		
   				});
   			}
   		}
   		
   		
   		
   	$scope.PayByRazorPaymentCCAvenue = function(){
   		
   		if($scope.razorPayObject.razorMobileNo == undefined || $scope.razorPayObject.razorMobileNo == null || $scope.razorPayObject.razorMobileNo =='')
   			{
   			growl.error("Please enter mobile Number",
   					{
   						title : 'Error!'
   					});
   			    return false;
   			 }
   		else if($scope.razorPayObject.razorAmount == undefined || $scope.razorPayObject.razorAmount == null || $scope.razorPayObject.razorAmount =='')
   		{
   			growl.error("Please enter amount",
   					{
   						title : 'Error!'
   					});
   			    return false;
   			 }
   		else{
   		$rootScope.startLoader();
   			var data = {
   					  "organizationId" : $scope.organizationId,
   					  "unitId" : $scope.unitId,
   					  "mobileNo":$scope.razorPayObject.razorMobileNo,
   					  "requestAmount":$scope.razorPayObject.razorAmount,
   					  "emailId":$scope.email,
   					 "name":self.model.billDetailList.patientName,
  					  "patientId":  self.model.billDetailList.patientId, 
   					  "createdBy": $scope.userId, 
   					  "updatedBy": $scope.userId
   			}
   			var URI = BASE_URL + ROOT_URL + "/sendCCAvenueLink";
   			BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
   				if (response.data.status == "success")
   				{
   						$rootScope.stopLoader();
   						$window.open(response.data.object.payLink, '_blank');
   						
   					} else if(response.data.status == 'error')
   					{
   						$rootScope.stopLoader();
   						growl.error(response.data.message,
   								{
   									title : 'Error!'
   								});
   				}
   	
   			});
   		}
   	}
        
   	$scope.getCashCollectedAmount = function(){

		$rootScope.startLoader();
			var data = {
					  "encounterId" : $scope.encounterId
			}
			var URI = BASE_URL + ROOT_URL + "/api/billing/cashPaidAmount";
			BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success")
				{
						$rootScope.stopLoader();
						$scope.cashCollectedAmount = response.data.object;
				} else if(response.data.status == 'error')
				{
					$rootScope.stopLoader();
					$scope.cashCollectedAmount = 0;
				}
	
			});
		
	}
   	
	$scope.getCashCollectedAmount();
   	
	$scope.clearPaymentList = function(){
		$scope.listPaymentDetails = [];
		var popup = angular.element('#collectPaymentDetails');
        popup.modal('hide');
        setTimeout(() => {
        	$state.go('outPatientSalesWorklist');
        	}, 700);
	}

    $scope.clearDiscount = function() {
        angular.forEach(self.model.worklistDetails.items, function(data){
          data.discountPerItem = 0;
          angular.forEach(data.selectedItems, function(sub){
        	  sub.discountPerItem = 0;
        	  sub.discountAmount=0;
          });
          self.model.worklistDetails.discount = 0;
          self.model.worklistDetails.discountCatgeory = self.model.discountCategoryList[0];
          Calculate(data, self.model.worklistDetails);
        });
    }
	    
    }

    function config($stateProvider) {
        $stateProvider.state("outPatientSalesWorklistDetails", {
            url: "/outPatientSalesWorklistDetails",
            templateUrl: 'views/PharmacyInventory/InPatient/out-patient-sales-worklist-details.html',
            controller: "OutPatientSalesWorklistDetails.Controller",
            controllerAs: "vm",
            title: 'Out Patient Sales Worklist'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("OutPatientSalesWorklistDetails.Controller", outPatientSalesWorklistDetailsController)
})();
