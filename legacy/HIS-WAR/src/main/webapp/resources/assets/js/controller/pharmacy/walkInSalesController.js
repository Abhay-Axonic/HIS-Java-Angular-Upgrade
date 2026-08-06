(function () {
  "use strict";

  function walkInSalesController($scope, $rootScope, $window, $http, $filter, $cookies, CONSTANTS, reportService, GenericServiceFileUpload, GenericService, growl, CurrencyService, StatusService, $state, pharmacyPaymentCollFactory, BillingGenericService ,$location,loyaltiCardCallFactory, commonDetailService,$timeout,stopTransactionService, GenericServiceParamHeader,$interval) {
    var self = this;
    var discountLimit = 100;
    var initialStore;
    $scope.mpesaRigths = JSON.parse(localStorage.getItem('mpesaRigths'));
    var cookieObject = {};
    var cookieObjectAppConfig = {};
    var cookieObjectStore = {};
    var cookieObject = $cookies.getObject('cookieObject');
    $scope.storeId = cookieObject.storeId;
    $scope.cookieObjectskipPharmacySettlement = JSON.parse(localStorage.getItem('isSkipPharmacySettlement'));

    [cookieObject, cookieObjectAppConfig] = $rootScope.checkCookies();

    var cookieObject = $cookies.getObject('cookieObject');
    if (cookieObject == undefined) {
      $state.go('login');
      return;
    }
    $scope.cashCollectedAmount= 0;
    var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
    var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
    if (cookieObjectAppConfigSecond != undefined) {
	    $scope.cashAmountValidation =  cookieObjectAppConfig.cashAmountValidation;
	    $scope.cashAmountLimit =  cookieObjectAppConfig.cashAmountLimit;
	$scope.payGatewayProviderId = cookieObjectAppConfig.payGatewayProviderId;
	 $scope.depositTypeWiseRequired =  cookieObjectAppConfig.depositTypeWiseRequired;
	 $scope.exceededBillAmountSettlement=cookieObjectAppConfigSecond.exceededBillAmountSettlement;
    }
    if (cookieObjectAppConfig == undefined) 
    {
      $state.go('login');
      return;
    }
    $scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
    $scope.isPinPadRequired = cookieObjectAppConfig.isPinPadRequired;
    $scope.registeredPinPadDevice= cookieObjectAppConfig.registeredPinPadDevice;
    $scope.isConsolidatedBill = cookieObjectAppConfig.isConsolidatedBill;
		// $scope.pinpadAmounFlag = false;
    $scope.counterMstObj = JSON.parse(sessionStorage.getItem('counterMstObj'));
    $scope.cookieObjectStore = $rootScope.getStoreObj(); // store-accessor pilot: null-safe parse + popup trigger, one owner
    $scope.storeName = $scope.cookieObjectStore != null ? $scope.cookieObjectStore.storeName : ''; // store null -> empty header, no {{storeName}} placeholder
    $scope.changePayeeRights = cookieObject.changePayeeRights;
    
    $scope.fileObj = {};
	$scope.fileObj.file = null;
    
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    $scope.atCopRequired = cookieObject.atCopRequired;
    function initializeController() {
      $rootScope.loginpage = true;
      $scope.page = "billList";
      
      // INSURANCE
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
      
      $scope.dateOptionsTrancs = {
  			formatYear : 'yyyy',
  			showWeeks : false,
  			minDate : new Date()
  		};
      
      $scope.dateOptions = {
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
      $scope.activePackageListWithoutForceClose=[];
		angular.forEach($scope.activePackageList, function (item, key)  {
			if(item.isForceClose==1)
				{
				$scope.activePackageListWithoutForceClose.push(item);
				}
		});
       if($scope.activePackageListWithoutForceClose!=null&& $scope.activePackageListWithoutForceClose.length>0){
          $scope.isValidated = true;
          $('#validateButton').show();
        }
        else{
          $scope.isValidated = false;
          $('#validateButton').hide();
        }
       $scope.isInvoiceGenerated ='N';
      // Code for Initialization Contract
    $scope.initPaymentEntitlement = function(){  
    	 
    	
        $scope.entitlementObj={
          "paymentEntitlementTypeId":1,
          "searchbyTypeId":1
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
			// 'statrDate':"",
			// 'endDate':""
		}];
	
	$scope.paymentEntitleMentInsurance=
		[{
			'companyId':"",
			'associateCompanyId':"",
			'contractId':"",
			// 'statrDate':"",
			// 'endDate':"",
			'insuranceNumber':"",
			'insuredAmt':""
		}];
  }
     
      $scope.initPaymentEntitlement();
      // Initialzation End

      self.model = {
        isOTC: false,
        walkInSale: {
          id: "1" + (new Date()).getTime(),
          items: [],
          discountType: '1'
        },
        isBatchValid: [],
        isUOMValid: [],
        isQuantityValid: [],
        SelectedItems: [],
        prefixList: [{
          "prefix": "Select"
        }],
        patientGenderList: [{
          "gender": "Select"
        }],
        discountCategoryList: [{
          "discountCategory": "Select Discount Category"
        }],
        patientSearchFlag: true
      };
      
      self.model.walkInSale.patientName = "";
      

     
      // else {
      // //console.log('cookieObjectAppConfig', cookieObjectAppConfig);

      // }
      $scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
      $scope.isAppointmentRequired = cookieObjectAppConfig.isAppointmentRequired;
      $scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
      $scope.taxInclusive = cookieObjectAppConfig.taxInclusive;
      $scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
      $scope.skipMpesaValidation = false;
      $scope.isMarkup = cookieObjectAppConfig.isMarkup;
      $scope.markupUp = cookieObjectAppConfig.markupUp;

      $scope.excessAmount = parseFloat(0).toFixed(2);	
      $scope.transferToActiveDeposit = "N";
      $scope.unitId = cookieObject.unitId;
      $scope.userId = cookieObject.userId;
      $scope.organizationId = cookieObject.organizationId;
      $scope.userId = $scope.userId;
      $scope.storeId = cookieObject.storeId;
      $scope.hitMpesaApi = false;
      $scope.appointmentId = 0;
      $scope.encounterId = null;
      $scope.admissionId = null;
      $scope.visitTypeId =null;
      // vivek added
      $scope.payeeId = '';
      $scope.searchPatient = false;
      $scope.consumedAmt = parseFloat(0);
      $scope.receivedFrom = "";
      $scope.identificationNumber = "";
      $scope.mpesaRequired = cookieObject.mpesaRequired;
      $scope.depositeList = {
        depositeIds: [1]
      };
      $scope.DepositObj = {};
	  $scope.DepositObj.userEnterDepositAmount = 0;
      $scope.depositeDisabledFlag = false;
      $scope.validatTextFieldFlag = true;
      // Methods
      CurrencyService.GetCurrency().then(function (currency) {
        self.model.Currency = currency;
      })
      StatusService.GetStatus().then(function (status) {
        self.model.Status = status;
      })

      self.model.cashSettlement = false;
      self.model.saveCashSettlement = saveCashSettlement;
      self.model.OtcFlag = OtcFlag;
      self.model.SearchItems = SearchItems;
      self.model.SearchGenerics = SearchGenerics;
      self.model.AddItemToBill = AddItemToBill;
      self.model.BatchChange = BatchChange;
      self.model.isStoreChanged=isStoreChanged;
      self.model.UomChange = UomChange;
      self.model.ApplyDiscount = ApplyDiscount;
      self.model.ApplyDiscountValidation = ApplyDiscountValidation;
      self.model.AddItem = AddItem;
      self.model.AddItem1 = AddItem1;
      self.model.RemoveItem = RemoveItem;
      self.model.AllocateQuantity = AllocateQuantity;
      self.model.SaveBill = SaveBill;
      self.model.SavePopup = SavePopup;
      self.model.ConfirmationPopup = ConfirmationPopup;
      self.model.StoreChangePopup=StoreChangePopup;
      self.model.resetStore=resetStore;
      self.model.WalkInSalesFlagStatus = WalkInSalesFlagStatus;
      self.model.ClearBill = ClearBill;
      self.model.SetDecimal = SetDecimal;
      self.model.BarcodeScan = BarcodeScan;
      self.model.SearchBatch = SearchBatch;
      self.model.AddBatchToBill = AddBatchToBill;
      self.model.OnKeyPressQuantity = OnKeyPressQuantity;
      self.model.searchPatient = searchPatient;
      self.model.searchPatientByType = searchPatientByType;
      self.model.searchPatientByTypeForMobileNo = searchPatientByTypeForMobileNo;
      self.model.SelectPatientByIdType = SelectPatientByIdType;
      self.model.SelectPatientById = SelectPatientById;
      self.model.GetBillDetails = GetBillDetails;
      self.model.GetBillDetailsForCash = GetBillDetailsForCash;
      self.model.NavigateToBillList = NavigateToBillList;
      self.model.ValidateBatch = ValidateBatch;
      self.model.ValidateUOM = ValidateUOM;
      self.model.ValidateQuantity = ValidateQuantity;
      self.model.ValidateUniqueBatchAndUom = ValidateUniqueBatchAndUom;
      self.model.CalculateAmount = CalculateAmount;
      self.model.rePrintBillSlip = rePrintBillSlip;
      self.model.GetGenericMaster = GetGenericMaster;
      self.model.GetStrengthUnitList = GetStrengthUnitList;
      self.model.ViewCombinationDetails = ViewCombinationDetails;
      self.model.tempPatientId = null;
      self.model.ViewStockCheckDetails = ViewStockCheckDetails;
      self.model.checkAgeValid = checkAgeValid;
      self.model.Calculate = Calculate;
      self.model.Calculate1 = Calculate1;
      self.model.Calculate2 = Calculate2;
      self.model.ConvertQtyOrdered = ConvertQtyOrdered;
      self.model.onChangeDiscount = onChangeDiscount;      
      self.model.visitNumbers = [];
      self.model.visitNumber = '-';
      self.model.ActualSaveBill = ActualSaveBill;
      self.model.calculateAtcop = calculateAtcop;
      self.model.clear = clear;
      self.model.SubstituteItems = SubstituteItems;
      self.model.Add = Add;
      self.model.SelectSubstituteItem = SelectSubstituteItem;
      self.model.AddSubstituteItem = AddSubstituteItem;
      self.model.RemoveSubstituteItem = RemoveSubstituteItem;
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
      var obj = {
        itemCode: '',
        itemName: '',
        itemId: '',
        genericName: '',
        manufacturerName: '',
        batch: '',
        selectedBatch: '',
        uom: '',
        selectedUom: '',
        markUpDown: '',
        markUpDownPer: '',
        taxId: '',
        taxName: '',
        taxPercent: '',
        formulationType: '',
        stock: '',
        strength: '',
        productCategoryId: '',
        assetCategoryId: '',
        patientClassPercentage: '',
        patientClassAmount: '',
        saleDetailPayeeMapperDtoList: []
      };
      self.model.walkInSale = {
        "id": self.model.walkInSale.id,
        "isWalkInSales": false,
        "items" : []
      }
      self.model.walkInSale.items.push(obj);
            
      self.model.patientSearchObj = {
				"uhIdNumber" : "",
				"patientName" : "",
				"genderId" : "",
				"mobileNo" : "",
				"birthDate" : "",
				"identificationNo" : "",
				"patientType" : 1,
				"unitId" : $scope.unitId,
				"organizationId" : $scope.organizationId
			}
      
      // Ganesh
      $scope.totalSelfPayable = 0;
      $scope.totalCreditPayable = 0;
      SetFocus();

      PopulateDropdown();
    }
    $scope.atCopToSave=false;
    var calculateAtcop = function () {
      angular.forEach(self.model.walkInSale.items, function (d){
        if($scope.atCop == true){
          d.rate = d.selectedBatch.actualLendingPrice;
          d.itemtotalAmount = d.rate * d.quantity;
          Calculate2(d, self.model.walkInSale);
          $scope.atCopToSave=true;
        }
        else{
          d.rate = (d.selectedBatch.rate != undefined && d.selectedBatch.rate != null )? d.selectedBatch.rate :d.selectedBatch.sellingPrice;
          // d.rate = d.selectedBatch.sellingPrice;
          d.itemtotalAmount = d.rate * d.quantity;
          Calculate2(d, self.model.walkInSale);
          $scope.atCopToSave=false;
        }
      });
    }

    var clear = function(item){
      item.itemCode = '';
      item.itemName ='';
      item.itemId = '';
      item.genericName = '';
      item.manufacturerName = '';
      item.batch = '';
      item.selectedBatch = '';
      item.uom = '';
      item.itemDiscount = '';
      item.discountPerItem = '';
      item.selectedUom = '';
      item.markUpDown = '';
      item.markUpDownPer = '';
      item.taxId = '';
      item.taxName = '';
      item.taxPercent = '';
      item.formulationType = '';
      item.stock = '';
      item.strength = '';
      item.productCategoryId = '';
      item.assetCategoryId = '';
      item.storageUnitName = '';
      item.instructionId = '';
      item.creditPayable ='';
      item.packageConsumptionAmt ='';
      item.patientClassPercentage = '';
      item.patientClassAmount = '';
      item.saleDetailPayeeMapperDtoList = [];
      item.itemtotalAmount = '';
      item.leaseQuantity = '';
      item.mrp = '';
      item.netAmount = '';
      item.newStock = ''
      item.quantity = '';
      item.rate = '';
      item.responseTaxAmount = '';
      item.selfPayable = '';
      item.taxAmount= '';
      item.unitDiscountAmount = '';
      item.receivedQtyCon = 0;
      item.receivedQtyCalculation = 0;
      item.uomUnitData = "";
      item.summary = "";
      item.totalStock = "";
    }
    
    
	$scope.getCashCollectedAmount = function(visitId,id){

		$rootScope.startLoader();
			
			var data = {};
			
			if(visitId == 1 ){
				data.encounterId = id;
			}else{
				data.admissionId = id;
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

    
    $scope.visitNumberFlag=false;
    $scope.visitDoctorName = function(){
      angular.forEach($scope.patientEncounterList, function(value, key) {
    	  if(self.model.visitNumber.length>0 && $scope.encounterId != null && $scope.encounterId >0 ){
    		  $scope.visitNumberFlag=true;
    	  }
        if(self.model.visitNumber.split("|")[0] == value.encounterNumber){
          self.model.walkInSale.doctorName = value.doctorName;
          $scope.commonDoctorId=value.doctorId;
          if($scope.encounterId != null && $scope.encounterId >0 ){
            	$scope.encounterId = value.encounterId;
            	$scope.getCashCollectedAmount(1,$scope.encounterId);
            	 $scope.getSponsorDetails($scope.encounterId,$scope.admissionId ,$scope.patientId);
            	}
        }
      });
    }

   $scope.getVisitNo = function(admisisonId,encounterId)
   {
	    if(encounterId>0){
	    	$scope.getCashCollectedAmount(1,encounterId);
	    	var URI = BASE_URL + ROOT_URL + "/api/opd/encounters/walkinSales/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/patientId/"+$scope.patientId;
			BillingGenericService.serviceAction(METHOD_GET,URI, {}, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == "success"){
					$scope.patientEncounterList = [];
					$scope.patientEncounterList = response.data.listObject;

					var validEncounterList = []

                    angular.forEach( $scope.patientEncounterList , function(value,key){
                        if(value.statusId != 9 && value.statusId != 2 && value.statusId != 7){
                            validEncounterList.push(value);
                        }
                    });

                    $scope.patientEncounterList = validEncounterList;

					self.model.visitNumbers = [];
					if($scope.patientEncounterList!=null && $scope.patientEncounterList.length>0){
						angular.forEach($scope.patientEncounterList, function(value, key) {
							self.model.visitNumbers.push(value.encounterNumber+""+"|"+""+value.encounterDateString);
				              self.model.walkInSale.doctorName = value.doctorName;
				              $scope.commonDoctorId = value.doctorId;
				            });
						
						self.model.visitNumber = $scope.patientEncounterList[0].encounterNumber;
						self.model.visitNumber = self.model.visitNumbers.find(function (obj) {
					          return obj;
					        });
            $scope.visitDoctorName();
				   }
				}
			});
	    }
	    else if(admisisonId>0){
	    	$scope.getCashCollectedAmount(2,admisisonId);
	    	var URI = BASE_URL + ROOT_URL + "/api/ipd/admissions/patient/"+$scope.admissionId;
			BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == "success"){
					self.model.visitNumbers = [];
					$scope.admissionDetails = response.data.object;
					if(response.data.object.dischargeStatusId == 1){
						growl.warning("Patient discharge is marked...!!",{
							  title : WARNING_MSG
					    });
					}
					if($scope.admissionDetails!=null){
						$scope.commonDoctorId=$scope.admissionDetails.doctorId;
						self.model.visitNumbers.push($scope.admissionDetails.admissionNumber);
						self.model.visitNumber = $scope.admissionDetails.admissionNumber;
						if(self.model.visitNumber.length>0){
				    		  $scope.visitNumberFlag=true;
				    	  }
						$scope.commonDoctorId=$scope.admissionDetails.doctorId;
				     }
				   }
				});
	    }
		
   }
   $scope.allDoctorList;
    $scope.getDoctorList=function()
    {
    	var data = {};
        var paramObj = {
          doctor_name : ''
        };
        var headerObj = {
          unit_id : $scope.unitId,
          organization_id : $scope.organizationId
        }
        // console.log(data);
        var URI = BASE_URL + ROOT_URL + GETDOCTORMASTERLISTAUTOFILLSERACH+"/doctorList";
        return GenericServiceParamHeader.serviceAction(METHOD_GET, URI, data, NOTIFICATION_MSG_STATUS_FALSE,paramObj,headerObj)
        .then(function(response) {
          // console.log(response);
          if (response.data.status == "success")
        	  $scope.allDoctorList=response.data.listObject;
            return response.data.listObject;
        });
    }
    $scope.getDoctorList();
    // Search Surgen with Typehead
    $scope.doctorSearch = function(keyword) {						
      var data = {};
      $scope.commonDoctorId=null;
      angular.forEach($scope.allDoctorList , function(value,key){
    	  if(keyword.toUpperCase()==value.doctorName.toUpperCase())
    		  {
    		  $scope.commonDoctorId=value.doctorId;
    		  }
        });
  	self.model.walkInSale.doctorType=null;
      var paramObj = {
        doctor_name : keyword
      };
      var headerObj = {
        unit_id : $scope.unitId,
        organization_id : $scope.organizationId
      }
      // console.log(data);
      var URI = BASE_URL + ROOT_URL + GETDOCTORMASTERLISTAUTOFILLSERACH;
      return GenericServiceParamHeader.serviceAction(METHOD_GET, URI, data, NOTIFICATION_MSG_STATUS_FALSE,paramObj,headerObj)
      .then(function(response) {
        // console.log(response);
        if (response.data.status == "success")
          return response.data.listObject;
      });
		}

    $scope.showItemsearch = function () {
      $('#itemsearch').show();
      $('#genericsearch').hide();
    }

    $scope.showGenericsearch = function () {
      $('#itemsearch').hide();
      $('#genericsearch').show();
    }

    var OtcFlag = function () {
      self.model.search = '';
      self.model.prevSearch = '';
      self.model.items = [];

      if (!!self.model.isOTC) {
        self.model.isDoctorNameValid = true;
        self.model.isDoctorAddressValid = true;
      }
    }

    var SetStatus = function (x) {
      var status = self.model.Status.find(function (obj) {
        return obj.status == x;
      });

      return status.id;
    }

    $scope.focusInput=false;
    $scope.redirectToPlusButton=function(event, idx)
    {
    	if( event.which == 9)
      {
      	var idxdata = idx + 1;
        var addrBtn = 'addrBtn_'+ idxdata;
        document.getElementById(addrBtn).focus();
        event.preventDefault();
      }
    	if(event.which == 13 || event.keyCode == 13  ) // for crossbrowser
      {
        AddItem1(idx,self.model.walkInSale);
        $scope.focusInput=true;
    		
        // event.preventDefault(); // this code prevents other buttons
        // triggers use this
        // do stuff
      }	
    }

    $scope.redirectToItemSearch=function(event, idx)
    {
    	if( event.which == 9)
      {
        var itemSearch = 'item-name_'+self.model.walkInSale.items.length;
        document.getElementById(itemSearch).focus();
        event.preventDefault();
      }
    	// if(event.which == 13 || event.keyCode == 13) // for crossbrowser
      // {
      // AddItem1(idx,self.model.walkInSale);
    	// $scope.focusInput=true;
      // }
    }

    $scope.redirectToPrefix = function(event, bill)
    {
      if(event.which == 1){
        document.getElementById('idPrefix').focus();
        event.preventDefault();
        $scope.focusInput=true;
      }
    }

    $scope.redirectToQty=function(event, index, item)
    {
    	if( event.which == 9)
      {
        var idx = index + 1;
        var idQty = 'qtyId_'+ idx;
        document.getElementById(idQty).focus();
        item.quantity = '';
    		event.preventDefault();
      }	
    } 

    $scope.redirectToBatch=function(event)
    {
    	if( event.which == 9)
    		{
    		document.getElementById('batchIdf').focus();
    		event.preventDefault();
    		}
    	 	
    } 
    
    var PopulateDropdown = function () {
      var URI = CONSTANTS.PHARMACY.OP.WALK_IN_SALES.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId;

      self.model.walkInSale.discountCatgeory = self.model.discountCategoryList[0];
      self.model.walkInSale.prefix = self.model.prefixList[0];
      self.model.walkInSale.patientGender = self.model.patientGenderList[0];

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.prefixList = self.model.prefixList.concat(response.data.prefix);
          self.model.patientGenderList = self.model.patientGenderList.concat(response.data.gender);
          self.model.discountCategoryList = self.model.discountCategoryList.concat(response.data.discountCategory);
          $rootScope.stopLoader();
          GetPrefixWithGender();          
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
        });
    }    
    
var checkAgeValid = function (){
  if(self.model.walkInSale.age){
    if(self.model.walkInSale.age >= 125)
    {
      growl.error('Please enter age less than 125');
       self.model.isAgeValid = true;
      
    }
    else{
      self.model.isAgeValid = false;
    }
  }
 
}
		$scope.loadMoreData = function()
		{
		    $scope.offset+=200;
		    $scope.loadMoreFlag=true;
		    searchPatient($scope.patientSearchObj);
		}
		$scope.offset=0;
		$scope.loadMoreFlag=false;
		$scope.noOfRecordsPerPage1=200;
    var searchPatient = function () {
    	self.model.patientSearchData=[];
      if (!!self.model.patientSearchObj.birthDate) {
        var birthDate = moment(self.model.patientSearchObj.birthDate).format('YYYY-MM-DD');
      } else {
        var birthDate = undefined;
      }
      if(self.model.patientSearchObj.patientName != undefined){
        if(self.model.patientSearchObj.patientName!="" && self.model.patientSearchObj.patientName.length < 3)
        {
          growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
          self.model.patientSearchData=[];
          return false;
        }
      }
      
      var patientData = self.model.patientSearchObj
      var URI = CONSTANTS.ROOT_URL + '/adt/patientSearchByMultipleCriteria';

      var data = {
        "uhIdNumber": !!patientData.uhIdNumber ? patientData.uhIdNumber : undefined,
        "patientName": !!patientData.patientName ? patientData.patientName : undefined,
        "genderId": !!patientData.gender && !!patientData.gender.id ? patientData.gender.id : undefined,
        "mobileNo": !!patientData.mobileNo ? patientData.mobileNo : undefined,
        "birthDate": birthDate,
        "identificationNo": !!patientData.identificationNo ? patientData.identificationNo : undefined,
        "patientType": 1,
        "unitId": !!$scope.unitId ? $scope.unitId : undefined,
        "organizationId": !!$scope.organizationId ? $scope.organizationId : undefined
      }
      if($scope.loadMoreFlag==true)
      {
    	  data.offset=$scope.offset;
      $scope.loadMoreFlag=false;
      }
  else
      {
  	  data.offset=0;
      $scope.loadMoreFlag=false;
      self.model.patientSearchData=[];
      }
      data.noOfRecordsPerPage=$scope.noOfRecordsPerPage1;
      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          angular.forEach(response.data.listObject,function(value,index)
					{
		        	  $scope.searchPatientList = self.model.patientSearchData.find(function(obj){
					        return obj.patientId == value.patientId;
				      });
		        	  if($scope.searchPatientList == undefined || $scope.searchPatientList == null){
		        		  self.model.patientSearchData .push(value);
		        	  }
					})
          if(response.data.listObject==null){
				growl.warning(response.data.message,{
					title : ERROR_MSG
				});
				self.model.patientSearchObj.birthDate="";
				self.model.patientSearchObj.gender=null;
				self.model.patientSearchData=[];
				return false;
			}
          // self.model.patientSearchData.forEach(function(){

          // })
          // self.model.patientId = response.data.listObject[0].patientId;
// self.model.patientSearchObj.birthDate="";
// self.model.patientSearchObj.gender=null;
          $rootScope.stopLoader();
//          $scope.searchFlag = false;
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
          growl.error('Error');
        });
// self.model.patientSearchObj.birthDate="";
// self.model.patientSearchData=[];
      self.model.visitNumbers = [];
    }

    var searchPatientByTypeForMobileNo = function (search) { 
    	// $scope.walkInPatient = null;
      $scope.availableDeposits = [];
      var birthDate = undefined;
      var callApi = false;
      if(self.model.walkInSale.mobileNumber != undefined){
	      // if(self.model.walkInSale.patientName != "" &&
			// self.model.walkInSale.patientName.length < 2)
	      // {
		    // growl.warning("Please enter atleast 3 characters..!",{title :
			// "WARNING!"});
		    // self.model.patientSearchData=[];
		    // return false;
	      // }else
        if(self.model.walkInSale.mobileNumber != ""){
	    	  callApi = true;
	      }
      } 
    
      if(callApi == true && $scope.walkInPatient != null)
      {
	    	self.model.patientSearchDataWalkInSale =[]; 
        var URI = CONSTANTS.ROOT_URL + '/adt/patientSearchByMultipleCriteriaForWalkInSales';
		    var data = {
          "patientName": !!self.model.walkInSale.patientName ? self.model.walkInSale.patientName : undefined,
          "mobileNo": !!self.model.walkInSale.mobileNumber ? self.model.walkInSale.mobileNumber : undefined,
          "patientType": 1,
          "unitId": !!$scope.unitId ? $scope.unitId : undefined,
          "organizationId": !!$scope.organizationId ? $scope.organizationId : undefined
		    }
		    $rootScope.startLoader();
		    return GenericService.serviceAction("POST", URI, data)
		    .then(function (response) {
          $rootScope.stopLoader();
          if (!!response.data.listObject && response.data.listObject.length > 0) 
          {    
            self.model.patientSearchDataWalkInSale = response.data.listObject;	    
            angular.forEach( self.model.patientSearchDataWalkInSale , function(value,key){
              value.showname = value.patientName +'|' +value.mobileNo +'|'+value.uhIdNumber;
            });
            $rootScope.stopLoader();
            return $filter('filter')(self.model.patientSearchDataWalkInSale, {
              $: search
            });
            callApi = false;
          }else{
            self.model.patientId = null;
            $scope.walkInPatient = null;
            // self.model.walkInSale.patientName = "";
            self.model.walkInSale.patientAddress = "";
            self.model.walkInSale.age = "";
            self.model.walkInSale.doctorName = "";
            self.model.walkInSale.mobileNumber = "";
            $scope.patientCategoryId = "";
            self.model.encountertId = "";
            $scope.patientId = self.model.patientId;
            $scope.walkInPatient = self.model.patientId;
				    // $scope.patientName = "";
            $scope.email = ""
            $scope.id = self.model.patientId;
            callApi = false;
          }
        }, function (err) {
      });	    
    }
    if(callApi == true && (self.model.walkInSale.patientName == null || self.model.walkInSale.patientName == ''))
    {
      self.model.patientSearchDataWalkInSale =[]; 
      var URI = CONSTANTS.ROOT_URL + '/adt/patientSearchByMultipleCriteriaForWalkInSales';
      var data = {
        "patientName": !!self.model.walkInSale.patientName ? self.model.walkInSale.patientName : undefined,
        "mobileNo": !!self.model.walkInSale.mobileNumber ? self.model.walkInSale.mobileNumber : undefined,
        "patientType": 1,
        "unitId": !!$scope.unitId ? $scope.unitId : undefined,
        "organizationId": !!$scope.organizationId ? $scope.organizationId : undefined
      }
      $rootScope.startLoader();
      return GenericService.serviceAction("POST", URI, data)
      .then(function (response) {
        $rootScope.stopLoader();
        if (!!response.data.listObject && response.data.listObject.length > 0) 
        {    
          self.model.patientSearchDataWalkInSale = response.data.listObject;	    
          angular.forEach( self.model.patientSearchDataWalkInSale , function(value,key){
            value.showname = value.patientName +'|' +value.mobileNo +'|'+value.uhIdNumber;
          });
          $rootScope.stopLoader();
          return $filter('filter')(self.model.patientSearchDataWalkInSale, {
            $: search
          });
          callApi = false;
        }else{
          self.model.patientId = null;
          $scope.walkInPatient = null;
          // self.model.walkInSale.patientName = "";
          self.model.walkInSale.patientAddress = "";
          self.model.walkInSale.age = "";
          self.model.walkInSale.doctorName = "";
          self.model.walkInSale.mobileNumber = "";
          $scope.patientCategoryId = "";
          self.model.encountertId = "";
          $scope.patientId = self.model.patientId;
          $scope.walkInPatient = self.model.patientId;
          // $scope.patientName = "";
          $scope.email = ""
          $scope.id = self.model.patientId;
          callApi = false;
        }
      }, function (err) {
    });	    
  }
  }

    var searchPatientByType = function (search) { 
    	$scope.walkInPatient = null;
      $scope.availableDeposits = [];
      var birthDate = undefined;
      var callApi = false;
      if(self.model.walkInSale.patientName != undefined || self.model.walkInSale.mobileNumber != undefined){
	      if(self.model.walkInSale.patientName != "" && self.model.walkInSale.patientName.length < 2)
	      {
		      growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
		      self.model.patientSearchData=[];
		      return false;
	      }else if(self.model.walkInSale.patientName != "" || self.model.walkInSale.mobileNumber != ""){
	    	  callApi = true;
	      }
      } 
    
      if(callApi == true)
      {
	    	self.model.patientSearchDataWalkInSale =[]; var URI = CONSTANTS.ROOT_URL + '/adt/patientSearchByMultipleCriteriaForWalkInSales';
		    var data = {
          "patientName": !!self.model.walkInSale.patientName ? self.model.walkInSale.patientName : undefined,
          "uhIdNumber": !!self.model.walkInSale.patientName ? self.model.walkInSale.patientName : undefined,
          "mobileNo": !!self.model.walkInSale.mobileNumber ? self.model.walkInSale.mobileNumber : undefined,
          "patientType": 1,
          "unitId": !!$scope.unitId ? $scope.unitId : undefined,
          "organizationId": !!$scope.organizationId ? $scope.organizationId : undefined
		    }
		    $rootScope.startLoader();
		    return GenericService.serviceAction("POST", URI, data)
		    .then(function (response) {
          $rootScope.stopLoader();
          if (!!response.data.listObject && response.data.listObject.length > 0) 
          {    
            self.model.patientSearchDataWalkInSale = response.data.listObject;	    
            angular.forEach( self.model.patientSearchDataWalkInSale , function(value,key){
              value.showname = value.patientName +'|' +value.mobileNo +'|'+value.uhIdNumber;
            });
            $rootScope.stopLoader();
            return $filter('filter')(self.model.patientSearchDataWalkInSale, {
              $: search
            });
            callApi = false;
          }else{
            self.model.patientId = null;
            $scope.walkInPatient = null;
            // self.model.walkInSale.patientName = "";
            self.model.walkInSale.patientAddress = "";
            self.model.walkInSale.age = "";
            self.model.walkInSale.doctorName = "";
            self.model.walkInSale.mobileNumber = "";
            $scope.patientCategoryId = "";
            self.model.encountertId = "";
            $scope.patientId = self.model.patientId;
            $scope.walkInPatient = self.model.patientId;
				    // $scope.patientName = "";
            $scope.email = ""
            $scope.id = self.model.patientId;
            callApi = false;
          }
        }, function (err) {
      });	    
    }
  }

    $scope.walkInPatient = null;
    $scope.SelectDoctorByIdType =function(doctor)
    {
    	$scope.commonDoctorId=doctor.doctorId;
    	self.model.walkInSale.doctorType=doctor.doctorType;
    }
    var SelectPatientByIdType = function (patient) {
    	self.model.walkInSale.items=[];
    	var obj = {
    	        itemCode: '',
    	        itemName: '',
    	        itemId: '',
    	        genericName: '',
    	        manufacturerName: '',
    	        batch: '',
    	        selectedBatch: '',
    	        uom: '',
    	        selectedUom: '',
    	        markUpDown: '',
    	        markUpDownPer: '',
    	        taxId: '',
    	        taxName: '',
    	        taxPercent: '',
    	        formulationType: '',
    	        stock: '',
    	        strength: '',
    	        productCategoryId: '',
    	        assetCategoryId: '',
    	        patientClassPercentage: '',
    	        patientClassAmount: '',
    	        saleDetailPayeeMapperDtoList: []
    	      };
    	self.model.walkInSale.items.push(obj);
    	self.model.visitNumbers = [];
    	$scope.encounterId = null;
    	$scope.admissionId = null;
    	$scope.visitTypeId =null;
    	self.model.patientId = patient.patientId;
    	$scope.walkInPatient = patient.patientId;
    	$scope.getLastEncounterDetails(patient);
// self.model.walkInSale.prefix = patient.prefixId;
    	self.model.walkInSale.patientName = patient.patientName;
    	self.model.walkInSale.patientAddress = patient.address ;
// self.model.walkInSale.patientGender = patient.genderId.toString();
    	 self.model.walkInSale.patientGender = self.model.patientGenderList.find(function(obj){
			        return obj.id == patient.genderId;
			      });
    	self.model.walkInSale.age = patient.age;
    	self.model.walkInSale.doctorName = patient.doctorName;
    	self.model.walkInSale.mobileNumber = patient.mobileNo;
    	self.model.walkInSale.prefix = self.model.prefixList.find(function(obj){
		        return obj.id == patient.prefixId;
		      });
    	$scope.patientCategoryId = patient.patientCategoryId;
    	self.model.encountertId = patient.encountertId;
    	$scope.patientId = self.model.patientId;
    	$scope.walkInPatient = self.model.patientId;
    	$scope.patientName = patient.patientName;
    	$scope.email = patient.email
    	$scope.id = self.model.patientId;
    	self.model.walkInSale.discount = null;
    	self.model.walkInSale.discountCatgeory = self.model.discountCategoryList[0];

    	if(patient.mobileNo!=null){
    	$scope.mobileNumber = parseInt(patient.mobileNo);
    	 $scope.countryCallingCode = parseInt(patient.countryCallingCode);
    	$scope.razorPayObject.razorMobileNo = patient.mobileNo;
    	$scope.objPosMachine.mobileNumberForPOS =patient.mobileNo;
    	}

    	if(patient.admissionId!=undefined && patient.admissionId!=null && patient.admissionId>0)
    	{
			    	$scope.admissionId = patient.admissionId;
			    	$scope.encounterId = null;
			    	$scope.visitTypeId = patient.visitTypeId;
			    	if($scope.visitTypeId == 1){
			    	$scope.saleTypeIdAgainstEncounter = 2;
			    	}
			    	if($scope.visitTypeId == 2){
			    	$scope.saleTypeIdAgainstEncounter = 1;
			    	}
			    	if($scope.visitTypeId == 3){
			    	$scope.saleTypeIdAgainstEncounter = 4;
			    	}
			    	if($scope.visitTypeId == 4){
			    	$scope.saleTypeIdAgainstEncounter = 5;
			    	}
			    	
			    	$scope.getVisitNo($scope.admissionId, 0);
			    	
			    
    	}else if(patient.encounterId!=undefined && patient.encounterId!=null && patient.encounterId>0){
    		$scope.getVisitNo(0, patient.encounterId);
    	}
    	
	}


   $scope.getSelfPaidConsultation = function(encounterId,admissionId,patientId){
              var param = {encounterId:encounterId,admissionId:admissionId,patientId:patientId};
		 var URI = BASE_URL + ROOT_URL + "/global/consumption/checkConsultation"
		    BillingGenericService.serviceAction(METHOD_POST,URI,param,"").then(function(response)
		    {
                 	$scope.consultationSelfPaid=response.data.object > 0 ? true : false;
		    });
   }
    
  // get sponsor details by encounter or admissionId start
	$scope.getSponsorDetails=function(encounterId,admissionId,patientId)
	{
		$scope.activePackageList =[];
		$rootScope.startLoader();
		var param = {
				     encounterId:encounterId,
				     admissionId:admissionId,
				     patientId:patientId,
				    };
		// console.log("Id data",param);
		 var URI = BASE_URL + ROOT_URL + "/global/consumption/getActivePaymentEntitlement"
		    BillingGenericService.serviceAction(METHOD_POST,URI,param,"").then(function(response) 
		    {
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
            // $scope.isDiscountApplicable =
			// $scope.paymentEntitleMentList[0].isTariffRateApplicable;
            $scope.isDiscountApplicable ='Y';
            if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                $scope.isValidated = true;
                $('#validateButton').show();
            }
            else{
                $scope.isValidated = false;
                $('#validateButton').hide();
            }	 	
					}					
					else{
						  $scope.initPaymentEntitlement();
						  // $scope.entitlementObj.paymentEntitlementTypeId =
							// 1;
              $scope.entitlementObj.paymentEntitlementTypeId = $scope.selfPayeeId;
              $scope.isContractChanged ='N';
              $scope.isPayeeUpdated ='N';
              $scope.isDiscountApplicable = 'Y';
              $scope.paymentEntitlementTypeIdDummy =angular.copy($scope.entitlementObj.paymentEntitlementTypeId);
              if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                  $scope.isValidated = true;
                  $('#validateButton').show();
              }
              else{
                  $scope.isValidated = false;
                  $('#validateButton').hide();
              }
					 }
			  // for package
			  $scope.activePackageList = response.data.object;
			  $scope.activePackageListWithoutForceClose=[];
				angular.forEach($scope.activePackageList, function (item, key)  {
					if(item.isForceClose==1)
						{
						$scope.activePackageListWithoutForceClose.push(item);
						}
				});
					  if($scope.activePackageListWithoutForceClose!=null&& $scope.activePackageListWithoutForceClose.length>0){
		                    $scope.isValidated = true	;
                        $('#validateButton').show();
		                }
					$rootScope.stopLoader();
					document.getElementById("id"+$scope.entitlementObj.paymentEntitlementTypeId).checked = true;
				}
				else if(response.data.status == "error")
				{
					   $rootScope.stopLoader();
				}
				 $scope.getSelfPaidConsultation(encounterId,admissionId,patientId);
			});
	}
	// get sponsor details by encounter or admissionId end
	$scope.saleTypeIdAgainstEncounter = 3;
	$scope.patientSaleTypeIdFlag = false; 
	
$scope.getLastEncounterDetails=function(info){
		
		var sdate = new Date();
	 	sdate.setDate(sdate.getDate() - 1);
	 	$scope.yesterday=sdate;
	 	
		
		$scope.encounterDetailsObjLast={
				
	    		 patientId : info.patientId,
	    		 selfPayeeId : $scope.selfPayeeId
		}
		
		if(info.patientId==null || info.patientId==''){
				growl.warning("Please select patient !!",{
					title : ERROR_MSG
				});
				return false;
		}
		
		var URI = BASE_URL + ROOT_URL + "/api/billing/getDataOfPatientToCheckDueAmountOfPharmacyService";
		BillingGenericService.serviceAction(METHOD_POST,URI,$scope.encounterDetailsObjLast, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			$scope.encounterList = response.data.listObject;
			
			if(response.data.object != null ){
				$scope.totalOutstanding = 0;
				$scope.totalOutstanding = $scope.totalOutstanding+response.data.object.dueAmount;
				if($scope.totalOutstanding>0)
				{
					angular.element('#outStandingPopup').modal('show');
					return false;
				}
			}
		});
	}

	$scope.checkCapWithPackage = false;
    var SelectPatientById = function (patient) {
      $rootScope.startLoader();
      $scope.visitNumberFlag=false;
      self.model.visitNumbers = [];
      $scope.encounterId = null;
      $scope.checkCapWithPackage = false;
      $scope.admissionId = null;
      $scope.visitTypeId =null;
      self.model.patientId = patient.patientId;
      $scope.patientCategoryId = patient.patientCategoryId;
      self.model.encountertId = patient.encountertId;
      $scope.patientId = self.model.patientId;
      $scope.walkInPatient = self.model.patientId;
      $scope.patientName = patient.patientName;
      $scope.email = patient.email
      $scope.id = self.model.patientId; 
      $scope.searchPatient = true;
      $scope.patientSaleTypeIdFlag = true; 
      $scope.patientClassIdPopup = null;
	  $scope.patientClassCancelReasonIdPopup = null;
	  $scope.patientClassId =null;
      // $scope.isValidated = false;
      self.model.walkInSale.items = [];
      self.model.walkInSale.discount = null; 
      self.model.walkInSale.discountCatgeory = self.model.discountCategoryList[0];
      $scope.consultationSelfPaid = false;
      $scope.getLastEncounterDetails(patient);
  	  if(patient.mobileNo!=null){
  		  $scope.mobileNumber = parseInt(patient.mobileNo);
  		  $scope.countryCallingCode = parseInt(patient.countryCallingCode);
  		  $scope.razorPayObject.razorMobileNo = patient.mobileNo;
  		$scope.objPosMachine.mobileNumberForPOS =patient.mobileNo;
  	  }
  	  
      if(patient.admissionId!=undefined && patient.admissionId!=null && patient.admissionId>0)
	    {
	  	  $scope.admissionId = patient.admissionId;
	  	  $scope.encounterId = null;
	  	  $scope.visitTypeId = patient.visitTypeId;
        if($scope.visitTypeId == 1){
          $scope.saleTypeIdAgainstEncounter = 2;
        }
        if($scope.visitTypeId == 2){
          $scope.saleTypeIdAgainstEncounter = 1;
        }
        if($scope.visitTypeId == 3){
          $scope.saleTypeIdAgainstEncounter = 4;
        }
        if($scope.visitTypeId == 4){
          $scope.saleTypeIdAgainstEncounter = 5;
        }
	  	  $scope.getSponsorDetails(null,$scope.admissionId,$scope.patientId);
	  	  $scope.getVisitNo($scope.admissionId,0);
	  	 $scope.checkCapWithPackage = true;
		    growl.warning("Patient is admitted..!!",{
				  title : WARNING_MSG
		    });
		    
		    
		    if(patient.isFinalBill  == 'Y') {
		    	  growl.warning(
							"Patient's final bill already been generated",
							{
								title : WARNING_MSG
							}); 
				
			}
		    
	  	  var URI = BASE_URL + ROOT_URL + "/api/billing/getBillData/admission/"+$scope.admissionId;
        BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					$scope.billNumber =response.data.billNumber;
			  });
	    } else if(patient.encounterId!=null && patient.encounterId>0){
    	    $scope.encounterId = patient.encounterId;
    	    $scope.visitTypeId = 1;
    	    $scope.admissionId = null
    	    $scope.saleTypeIdAgainstEncounter = 2;
    	    $scope.getVisitNo(0,$scope.encounterId);
    	    $scope.getSponsorDetails($scope.encounterId,null,$scope.patientId);
    	    
    	    var URI = BASE_URL + ROOT_URL + "/api/billing/getBillData/"+$scope.encounterId;
    		  BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
    				$scope.billNumber =response.data.billNumber;
    				$scope.isInvoiceGenerated =response.data.isInvoiceGenerated;
    				
    		  });
    	}else if((patient.encounterId==undefined || patient.encounterId==null)){	
		      var data = {};
				  var URI = BASE_URL + ROOT_URL + "/api/opd/encounters/walkinSales/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/patientId/"+$scope.patientId;
				  BillingGenericService.serviceAction(METHOD_GET,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						// console.log("Encouner
						// List",response.data.listObject);
						$scope.patientEncounterList = response.data.listObject;
						var validEncounterList = []

						angular.forEach( $scope.patientEncounterList , function(value,key){
                            if(value.statusId != 9 && value.statusId != 2 && value.statusId != 7){
                                validEncounterList.push(value);
                            }
                        });

                        $scope.patientEncounterList = validEncounterList;

						$scope.commonDoctorId = null;
						if($scope.patientEncounterList!=null && $scope.patientEncounterList.length > 0)
						{
							$scope.commonDoctorId=$scope.patientEncounterList[0].doctorId;
							 $scope.encounterId = $scope.patientEncounterList[0].encounterId;
							 $scope.visitTypeId = 1;
							 $scope.admissionId = null;
							 $scope.getSponsorDetails($scope.patientEncounterList[0].encounterId,null,$scope.patientId);
							 
							  if(($scope.encounterId!=undefined && $scope.encounterId!=null)){
								  $scope.saleTypeIdAgainstEncounter = 2;
							  }else{
								  $scope.saleTypeIdAgainstEncounter = 3;
							  }
							  $scope.getVisitNo(0,$scope.encounterId);
						}
            else if(patient.admissionId!=undefined && patient.admissionId!=null && patient.admissionId>0)
            {
              $scope.admissionId = patient.admissionId;
              $scope.encounterId = null;
              $scope.visitTypeId = patient.visitTypeId;
              $scope.saleTypeIdAgainstEncounter = 1;// ip/er/dc
              $scope.getSponsorDetails(null,$scope.admissionId,$scope.patientId);
              $scope.getVisitNo($scope.admissionId,0);
              $scope.checkCapWithPackage = true;
              growl.warning("Patient is admitted..!!",{
                title : WARNING_MSG
              });
					    	  
              var URI = BASE_URL + ROOT_URL + "/api/billing/getBillData/admission/"+$scope.admissionId;
              BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                $scope.billNumber =response.data.billNumber;
                $scope.isInvoiceGenerated =response.data.isInvoiceGenerated;
              });
            } else{
              $scope.initPaymentEntitlement();
              $scope.entitlementObj.paymentEntitlementTypeId = 1;
              document.getElementById("id"+$scope.entitlementObj.paymentEntitlementTypeId).checked = true;
              $scope.isValidated = false;
              $('#validateButton').hide();
				}
				$rootScope.stopLoader();
			}
		});
      }
      
      		var data1patientClass = {};
     
		      data1patientClass.admissionId =  $scope.admissionId;
		      data1patientClass.encounterId =  $scope.encounterId;
         
          
          var URI = BASE_URL + ROOT_URL + "/api/opd/getPatientClassIdByEncounter";
          BillingGenericService .serviceAction(METHOD_POST, URI, data1patientClass, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
            if(response.data.object != null){
                  $scope.patientClassIdPopup = response.data.object.patientClassId;
            }
          });
        
      
      
      
      // get available active deposits by patient
	  	var URI = BASE_URL + ROOT_URL + "/api/billing/unit/deposit/patient/"+$scope.organizationId+"/"+$scope.unitId+"/"+patient.patientId+"/"+0+"/"+0+"/"+0+"/"+$scope.depositTypeWiseRequired;
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
          /*
			 * if(value.depositTypeId != '1') { activeDeposit = activeDeposit +
			 * parseFloat(value.availableDeposit);
			 * 
			 * if(billAmt >0){
			 * 
			 * if(billAmt >= value.availableDeposit) { value.consumedDeposit =
			 * value.consumedDeposit+value.availableDeposit;
			 * $scope.availableDeposits[i].depositeFlag = 'Y';
			 * $scope.transferToActiveDepositeFlag = true; $scope.dueAmount =
			 * ($scope.dueAmount - value.availableDeposit).toFixed(2); billAmt =
			 * billAmt-value.availableDeposit;
			 * $scope.availableDeposits[i].availableDeposit = parseFloat(0); }
			 * else if(billAmt < value.availableDeposit) {
			 * value.availableDeposit = parseFloat(value.availableDeposit)-
			 * parseFloat(billAmt); value.consumedDeposit =
			 * value.consumedDeposit + parseFloat(billAmt);
			 * $scope.availableDeposits[i].depositeFlag = 'Y'; $scope.dueAmount =
			 * ($scope.dueAmount - billAmt).toFixed(2); $scope.payeeDueAmtFinal =
			 * 0; billAmt = 0; //$scope.dueAmount = ($scope.dueAmount
			 * -$scope.consumedAmt).toFixed(2); } }else{
			 * $scope.availableDeposits[i].depositeFlag = 'N'; } } else {
			 */
              activeDeposit = activeDeposit + parseFloat(value.availableDeposit);
              $scope.availableDeposits[i].depositeFlag = 'N';
              activeDepositPatient = activeDepositPatient + parseFloat(value.availableDeposit);
           /* } */
            i++;
          });
          $scope.activeDeposit = activeDeposit;
          $scope.useActiveDepo = true;
        }
        $scope.calculateTotalDueRefAmount();
		  }); 
      var obj = {
        itemCode: '',
        itemName: '',
        itemId: '',
        genericName: '',
        manufacturerName: '',
        batch: '',
        selectedBatch: '',
        uom: '',
        selectedUom: '',
        markUpDown: '',
        markUpDownPer: '',
        taxId: '',
        taxName: '',
        taxPercent: '',
        formulationType: '',
        stock: '',
        strength: '',
        productCategoryId: '',
        assetCategoryId: '',
        patientClassPercentage: '',
        patientClassAmount: '',
        saleDetailPayeeMapperDtoList: []
      };
      self.model.walkInSale = {
        "id": self.model.walkInSale.id,
        "items" : []
      }
      self.model.walkInSale.doctorName = patient.doctorName;
      self.model.walkInSale.prefix = self.model.prefixList.find(function(obj){
                                    return obj.id == patient.prefixId;
                                  });
      self.model.walkInSale.patientName = patient.patientName;
      self.model.walkInSale.patientAddress = patient.address;
      self.model.walkInSale.mobileNumber = patient.mobileNo;
      self.model.walkInSale.patientGender = self.model.patientGenderList.find(function(obj){
                                            return obj.id == patient.genderId;
                                          });
      self.model.walkInSale.age = patient.age;
      self.model.walkInSale.doctorAddress = patient.doctorAddress;
      self.model.walkInSale.items.push(obj);
      $timeout(function(){
        var itemSearch = 'item-name_'+self.model.walkInSale.items.length;
        document.getElementById(itemSearch).focus();
      },20);
    }

  // Ganesh Start
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
	/* Ganesh end */
    
    $scope.thumbSearch = function()
	{
		 try {
			    $rootScope.startLoader();
	           	var quality = 60; // (1 to 100) (recommanded minimum 55)
	               var timeout = 10; // seconds (minimum=10(recommanded),
										// maximum=60, unlimited=0 )
	               var res = CaptureFinger(quality, timeout);	
	               if (res.httpStaus) 
	               {
	            	   if (res.data.ErrorCode == "0") 
	                   {
	                       $scope.thumbImage = res.data.BitmapData;
	                       var URI = BASE_URL + ROOT_URL + "/api/global/searchByFingerPrint";
	                       
	                       var param = 
	                       {
	                    		   organizationId:$scope.organizationId,
	                    		   unitId:$scope.unitId,
	                    		   thumbImage:res.data.BitmapData
	                    		  }; 
	                       }
	            	   BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
	           			$scope.searchPatientList = [];
	           			if (response.data.status == "success")
	           				{
	           				self.model.patientSearchData = response.data.listObject;
	           				$rootScope.stopLoader();
	           				}
	           			else if(response.data.status == "error"){
	           				$rootScope.stopLoader();
	           			}
	           			
	           		});
	                   
	               }
	               else 
	               {
	                   alert(res.err);
	                   $rootScope.stopLoader();
	               }
	           }
	           catch (e) {
	               alert(e);
	               $rootScope.stopLoader();
	           }
	}
    

    var SubstituteItems = function  (item,index) {
    	$scope.mainIndex=index;
   	  item.substititeFlag=true;
         self.model.parIndx = index;
         self.model.instructionDesc = item.instructionDesc;
         self.model.duration = item.duration;
         self.model.formulationTypeId = (item.formulationTypeId != undefined && item.formulationTypeId != null ? item.formulationTypeId:0);
         self.model.frequencyDesc = item.frequencyDesc;
         self.model.selectedItemId = item.itemId;
         self.model.selectedGenricId = item.genericId;
         self.model.itemName = item.itemName;
         self.model.dose = item.dose;
         self.model.strength = item.strength;
         self.model.doseUnit = item.doseUnit;
         self.model.genericName = item.genericName;
         self.model.productCategoryId = item.productCategoryId;
         self.model.dose = item.dose;
         if(item.selectedUom == undefined || item.selectedUom.uomTypeId == undefined){
        	 self.model.selectedQuantity = item.quantityOrdered;
        }else{
        	  var mulQty = item.selectedUom.conversion * item.quantityIssued;
              var  newOrderQty =  item.quantityOrdered - mulQty;
              self.model.selectedQuantity = newOrderQty;
        }
         self.model.substituteNewQty = item.newQty;
         self.model.taxAmount = item.taxAmount;
         self.model.taxId = item.taxId;
         self.model.taxPercentage = item.taxPercentage;
         self.model.issuedQuantity = item.issuedQuantity;
         self.model.frequency = item.frequency;
         self.model.dose = item.dose+' '+item.doseUnit ;
         self.model.startDate = item.startDate;
         self.model.stopDate = item.stopDate;
         self.model.substituteDetails = [];
         self.model.selectAllRows = false;
         self.model.isItemChecked = false;
         item.isNarcotic = false;
         item.substititeFlag=true;
         
         var URI = CONSTANTS.PHARMACY.IP.ASSIGN_PICKER_DISPENSER.SUBSTITUTE_API + $scope.unitId + '/' + item.genericId + '/' + item.itemId+ '/' + item.isNarcotic+ '/' +  $cookies.getObject('cookieObject').storeId +'/'+ self.model.formulationTypeId;
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
                         item.batch.unshift({
                             "batchDetails": "Select Batch"
                         });
                         item.uom = [{
                             "uomDetails": "Select UOM"
                         }],
                         $scope.resultData = item.batch.filter(word => word.availableStockQuantity > 0);
                        item.batch= $scope.resultData;
                         item.selectedBatch = commonDetailService.getDefaultBatchFromConfig(item.batch);
                         item.selectedUom = item.uom[0];
                         item.itemName = item.details;
                     });
                     self.model.substituteDetailsData = self.model.substituteDetails;
                    self.model.substituteDetails = [];
                    angular.forEach(self.model.substituteDetailsData, function(d){
                        if(d.batch.length != 0){
                            self.model.substituteDetails.push(d);
                        }
                    })
                 }
                 $rootScope.stopLoaderPopup();
             });

     }
    var isSelected = function (item) {
        return item.isSelected;
    }
    
    var SelectSubstituteItem = function (item, index) {
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
    
    var CopySubstituteItem = function (index, bill) {
        var item = bill[index];
        return {
             	itemId: item.itemId,
             	genericId: item.genericId,
                itemCode: item.itemCode,
                itemName: item.itemName,
                narcoticFlag: item.narcoticFlag,
                genericName: item.genericName,
                manufacturerName: item.manufacturerName,
                batch: item.batch,
                selectedBatch: null,
                uom: item.uom,
                selectedUom: null,
                stockAvailable: item.stockAvailable,
                rackNo: item.rackNo,
                disabled: item.disabled,
                storageUnitName: item.storageUnitName,
                taxId:item.taxId,
                taxPercentage:item.taxPercentage
        }
    }
    
    var AddSubstituteItem = function (index, bill) {
        bill.splice(index + 1, 0, CopySubstituteItem(index, bill));
    }
    // For Remove
    var RemoveSubstituteItem = function (index, bill) {
      // bill.splice(index, 1);
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
  			  isValid=true;
  		  }
  	  });
  	  if(isValid=true && (count==0)){
  	  	 var popup = angular.element('#substituteModal');
  	          popup.modal('hide');
  	  }
  	  return isValid; 
    }
    
    var Add = function () {
        if (!self.model.SelectedItems || self.model.SelectedItems == undefined || self.model.SelectedItems == '') {
            growl.error('Please select items')
            return false;
        } else {
      	  if(validateSubSelectedBatch(self.model.SelectedItems)){
            self.model.SelectedItems.forEach(function (selectedItem) {
                selectedItem['dose'] = self.model.dose;
                selectedItem['quantityOrdered'] = self.model.selectedQuantity;
                selectedItem['newQty'] = self.model.selectedQuantity;
                selectedItem['substituteNewQty'] = self.model.substituteNewQty;
                selectedItem['id'] = selectedItem.itemId;
                selectedItem['substituteBtn'] = true;
                selectedItem['selectedUom'] = selectedItem.selectedUom;
                selectedItem['selectedBatch'] = selectedItem.selectedBatch;
                selectedItem['frequency'] = self.model.frequency;
                selectedItem['productCategoryId'] = self.model.productCategoryId;
                selectedItem['taxAmount'] = self.model.taxAmount;
                selectedItem['taxId'] = selectedItem.taxId;
                selectedItem['taxPercent'] = selectedItem.taxPercentage;
                selectedItem['saleDetailPayeeMapperDtoList'] = [];
                selectedItem['SaleDetailPackageMapperList'] = [];
                selectedItem['patientClassPercentage'] = 0;
                selectedItem['markUpDown'] = 0;
                selectedItem['markUpDownPer'] = 0;
                selectedItem['itemId'] = selectedItem.itemId;
                selectedItem['itemDisabled'] = false;
                selectedItem['disabled'] = false;
                selectedItem['finalOrderedQty'] = self.model.selectedQuantity;
                selectedItem['productCategoryId'] = selectedItem.productCategoryId;
                selectedItem['instructionDesc'] = self.model.instructionDesc
                selectedItem['duration'] = self.model.duration;
                selectedItem['frequencyDesc'] = self.model.frequencyDesc;
            });
            $rootScope.startLoader();
            /*self.model.walkInSale.items.forEach(function (list) {*/
            	angular.forEach(self.model.walkInSale.items,function(list,indx){
            		
          	  if(!list.itemDisabled && $scope.mainIndex == indx){
                self.model.SelectedItems.forEach(function (item) {
                    item.uom = [{
                        uomDetails: "Select UOM",
                    }];
                    if(item.selectedBatch != undefined){
                      if (!!item.selectedBatch.batchId) {
                          item.stockAvailable=item.selectedBatch.availableStockQuantity;
                          item.substituteNewQty = item.selectedBatch.availableStockQuantity;
                          item.uom = item.selectedBatch.uom;
                          item.selectedUom = $filter('filter')(item.selectedBatch.uom, {
                              opUom: true
                          }, true)[0];
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
                    var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId + '/'+ $cookies.getObject('cookieObject').storeId  + '/' + item.itemId + '/' + $scope.entitlementObj.paymentEntitlementTypeId;
                    $scope.startLoader();
                    GenericService.serviceAction("GET", URI, {})
                    .then(function (response) {
                      $scope.stopLoader();
                      item.markUpDown =response.data.markUpDown;
                      item.markUpDownPer =response.data.markUpDownPer;
                      angular.forEach(response.data.batch, function(d){
                        if(item.selectedBatch.batchId == d.batchId){
                          item.selectedBatch.actualLendingPrice = d.actualLendingPrice;
                          item.selectedBatch.rate = d.rate;
                        }
                      });
                      if(item.genericId==list.genericId)
                      {
                  item.colorCode=list.colorCode;
                      }
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
                        item.selectedBatch.uomListData = item.selectedBatch.uom;
                        item.uom = [];
                        angular.forEach(item.selectedBatch.uomListData, function(u){
                            if(u.uomType != 'Stock'){
                                item.uom.push(u);
                            }
                        })
                      }

                      if(item.quantity == undefined || item.quantity == null || item.quantity == ""){
                        item.quantity = 0;
                      }

                      if(item.isThreeUOMApplicable == false){
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
                      else{
                        if(item.selectedUom.uomTypeId == 1){
                          item.receivedQtyCon = (item.quantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
                          item.receivedQtyCalculation = (item.quantity * item.uom[0].conversion * item.uom[1].conversion);
                          item.uomUnitData = item.uom[2].uomUnit;
                        }
                        else if(item.selectedUom.uomTypeId == 2){
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
                     
                      Calculate(item, self.model.walkInSale);
                    });
                });

                if (self.model.selectedGenricId  == list.genericId) {
                  	if(list.substititeFlag){
                  	if(list.quantityIssued == null || list.quantityIssued == 0){
                      list['disabled'] = false;
                      list.quantityIssued = null;
                  	}
                      
                      if (!list['selectedItems'])
                          list['items'] = self.model.SelectedItems;
                      else {
                          list['items'] = list['items'].concat(self.model.SelectedItems);
                      }
                      list.substititeFlag=false;
                      $rootScope.stopLoader();
                  }
                  }
            }
            });
// ActualAddItemToBill(self.model.walkInSale, self.model.SelectedItems[0],
// self.model.SelectedItems[0].itemName, self.model.SelectedItems[0].itemName,
// 0);
           // self.model.walkInSale.items=self.model.SelectedItems;
            
           // self.model.walkInSale.items.push(self.model.SelectedItems);
            
            self.model.walkInSale.items.splice($scope.mainIndex ,1);
			  angular.forEach(self.model.SelectedItems, function(val, ind){
					self.model.walkInSale.items.push(val);
				  
			  });
			  
			  angular.forEach(self.model.walkInSale.items, function(value, ind){
				  ActualAddItemToBill(self.model.walkInSale, value, value.itemName, value.itemName, self.model.walkInSale.items.length);
			  });
/*            var count=0;
            angular.forEach(self.model.walkInSale.items, function(val, ind){
            	// count++;
            	if($scope.mainIndex != undefined && ind == $scope.mainIndex){
            	  if(self.model.SelectedItems!=undefined && self.model.SelectedItems!= null){
            		  angular.forEach(self.model.SelectedItems, function(value, index){
                    if(!val.quantity || val.quantity == undefined){
                      if(index==0){
                 			  self.model.walkInSale.items[$scope.mainIndex]=self.model.SelectedItems[0];
                 		  }
                    }
                    else{
                      if(index==0){
                        self.model.walkInSale.items.push(value);
                 		  }
                    }
            			  if(index!=0 && self.model.walkInSale.items.length==1){
	         		 	    	count++;
                      ActualAddItemToBill(self.model.walkInSale, value, value.itemName, value.itemName, ind+count);
                    }
            		    if(index!=0 && self.model.walkInSale.items.length > 1){
            		    	ActualAddItemToBill(self.model.walkInSale, value, value.itemName, value.itemName, self.model.walkInSale.items.length);
            		    }
                    });
            	}
            	} 
            });*/
          /*
			 * if(self.model.SelectedItems!=undefined &&
			 * self.model.SelectedItems.length > 0){
			 * if(self.model.SelectedItems.length == 1){
			 * ActualAddItemToBill(self.model.walkInSale, value, value.itemName,
			 * value.itemName, self.model.SelectedItems[0]); }
			 * angular.forEach(self.model.SelectedItems , function(value,
			 * index){ if(index!=0){ ActualAddItemToBill(self.model.walkInSale,
			 * value, value.itemName, value.itemName, index); } }); }
			 */
            // shital
          /*
			 * if(self.model.SelectedItems!=undefined &&
			 * self.model.SelectedItems.length > 0){
			 * if(self.model.SelectedItems.length == 1){
			 * //ActualAddItemToBill(self.model.walkInSale, value,
			 * value.itemName, value.itemName, self.model.SelectedItems[0]);
			 * self.model.walkInSale.items[$scope.mainIndex]=self.model.SelectedItems[0];
			 * }else{ angular.forEach(self.model.SelectedItems , function(value,
			 * index){ if(index!=0){ ActualAddItemToBill(self.model.walkInSale,
			 * value, value.itemName, value.itemName,
			 * self.model.walkInSale.items.length); } if(index==0){
			 * self.model.walkInSale.items[$scope.mainIndex]=self.model.SelectedItems[0]; }
			 * }); }
			 *  }
			 */
            angular.forEach(self.model.walkInSale.items, function (item, key) {
            	  if($scope.mainIndex == key){
            		  item.totalStock=0;
					  angular.forEach(item.batch, function (batch, key) {
              	      item.totalStock+=batch.availableStockQuantity!=undefined && batch.availableStockQuantity!=null ? batch.availableStockQuantity:0;
					  });
            	  }
            	     
				  });
            self.model.SelectedItems = [];
        }
    }
    }
    
    var SearchItems = function (search) {
       if ((!self.model.prevSearch || self.model.prevSearch != search )  && (search.length == 2) ) {
        self.model.prevSearch = search;

        var URI = CONSTANTS.GLOBAL.ITEM_API  + $scope.unitId+ "/" + self.model.isOTC + "/" + search + "/" + $cookies.getObject('cookieObject').storeId;
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
      if (search.length < 1) return;

      if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
        self.model.prevSearch = search;

        var URI = CONSTANTS.GLOBAL.GENERIC_API_SEARCH + $scope.unitId +"/" + self.model.isOTC + "/" + search+"/"+$cookies.getObject('cookieObject').storeId;
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
    
    var AddItemToBill = function (bill, item, model, label, idx) {
        if(item.isLasa !== undefined && item.isLasa !== null && item.isLasa === true){
            growl.warning("Item is LASA drug..!",{
              title : WARNING_MSG
            });
        }
      ActualAddItemToBill(bill, item, model, label, idx);
    }    
    $scope.getGenericScheduleColorMapping = function ()
    {
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
    var ActualAddItemToBill = function (bill, item, model, label, idx) {
      if (!!item.id) {
        var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId + '/'+ $cookies.getObject('cookieObject').storeId  + '/' + item.id + '/' + $scope.entitlementObj.paymentEntitlementTypeId;
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(function (response) {
           if(response.status == 200){
            if (response.data && response.data.code) {
               angular.forEach(self.model.walkInSale.items, function (value, key) {
                 if(key == idx){
                   value.itemName="";
                 }
               });
               var obj = {
                       itemCode: '',
                       itemName: '',
                       itemId: '',
                       genericName: '',
                       manufacturerName: '',
                       batch: '',
                       selectedBatch: '',
                       uom: '',
                       selectedUom: '',
                       markUpDown: '',
                       markUpDownPer: '',
                       taxId: '',
                       taxName: '',
                       taxPercent: '',
                       formulationType: '',
                       stock: '',
                       strength: '',
                       productCategoryId: '',
                       assetCategoryId: '',
                       patientClassPercentage: '',
                       patientClassAmount: '',
                       saleDetailPayeeMapperDtoList: []
                     };
               bill.items[idx] = obj;
               growl.error(response.data.message);
               $rootScope.stopLoader();
            }else{
              var batchNo = [];
              var uniqueBatches = [];
              $scope.bill = [];
              angular.forEach(response.data.batch, function (value, key) {
                if(!!this.markUpDownPer){
                  var markUpDown = commonDetailService.fixedDecimal((value.sellingPrice * (this.markUpDownPer / 100)));
                }
              }, response.data);

              response.data.batch.unshift({
                batchDetails: "Select Batch"
              });

              $scope.patientConsation(response.data);
              var selectedBatch = response.data.batch.find(element => element.batchId == 0);
              $timeout( function(){
                var item = {
                  isConsumed:false,
                  itemDisabled: false,
                  disabled: false,
                  itemId: response.data.itemId,
                  itemCode: response.data.itemCode,
                  markUpDown: response.data.markUpDown,
                  itemName: response.data.itemCode + " | " + response.data.itemName,
                  markUpDownPer: response.data.markUpDownPer,
                  formulationType: response.data.formulationType,
                  formulationTypeId: response.data.formulationTypeId,
                  stock: response.data.currentStock,
                  genericId: response.data.genericId,
                  genericName: response.data.genericName,
                  manufacturerId: response.data.manufacturerId,
                  manufacturerName: response.data.manufacturerName,
                  strength: response.data.strength,
                  batch: response.data.batch,
                  productCategoryId:response.data.productCategoryId,
                  assetCategoryId:response.data.assetCategoryId,
                  selectedBatch: !!selectedBatch?selectedBatch:commonDetailService.getDefaultBatchFromConfig(response.data.batch),
                  uom: [{
                    uomDetails: "Select UOM",
                  }],
                  taxId: response.data.taxId,
                  taxName: response.data.taxName,
                  taxPercent: response.data.taxPercent,
                  isOTC: self.model.isOTC,
                  patientClassPercentage : $scope.patientClassPercentage,
                  storageUnitName : response.data.storageUnitName,
                  saleDetailPayeeMapperDtoList:[],
                  isThreeUOMApplicable :response.data.isThreeUOMApplicable,
                  colorCode:response.data.colorCode,
                  isNarcotic:response.data.isNarcotic,
                  isOTC:response.data.isOTC,
                  isLasa : response.data.isLasa !== undefined || response.data.isLasa !== null ? response.data.isLasa : false
                };
                if(item.isNarcotic==true)
                    {
                    item.colorCode="red";
                    }
                if(item.selectedBatch != undefined && item.selectedBatch != null){

                    item.uom = item.selectedBatch.uom;
                    item.selectedUom = item.selectedBatch.uom.find(function (obj) {
                      return obj.opUom == true;
                    });

                }

    //            if(bill.items.length == 1){
                  angular.forEach(bill.items, function(i,key){
                    if(key == idx){
                        bill.items[idx] = item;
                    }
                  });

                 // bill.items[idx] = item;
    //            }
                if(bill.items[idx].batch==undefined || bill.items[idx].batch==null || bill.items[idx].batch==''|| bill.items[idx].batch.length==0)
                {
                    bill.items[idx] = item;
                }
                $scope.formulationList = [];
                var obj = {
                  "formulationType" : '',
                  "manufacturerName" : ''
                }
                obj.formulationType = item.formulationType;
                obj.manufacturerName = item.manufacturerName;
                $scope.formulationList.push(obj);
                if(!!selectedBatch){
                  BatchChange(index, bill, selectedBatch)
                }
                self.model.search = '';
                if(item.selectedBatch != undefined && item.selectedBatch != null){
                    item.taxId=item.selectedBatch.taxId;
                    item.taxPercent=item.selectedBatch.taxPercentage;
                }
                CalculateStock(item, item.uom, item.selectedUom);
              },200);
              $rootScope.stopLoader();
              $timeout( function(){
                $scope.eleidx = idx + 1;
                var idbatch = 'batchIdf_'+ $scope.eleidx;
                document.getElementById(idbatch).focus();
              },300);
          }
          }
        }, function (err) {
        angular.forEach(self.model.walkInSale.items, function (value, key) {
          if(key == idx){
            value.itemName="";
          }    	  
        });
        var obj = {
                itemCode: '',
                itemName: '',
                itemId: '',
                genericName: '',
                manufacturerName: '',
                batch: '',
                selectedBatch: '',
                uom: '',
                selectedUom: '',
                markUpDown: '',
                markUpDownPer: '',
                taxId: '',
                taxName: '',
                taxPercent: '',
                formulationType: '',
                stock: '',
                strength: '',
                productCategoryId: '',
                assetCategoryId: '',
                patientClassPercentage: '',
                patientClassAmount: '',
                saleDetailPayeeMapperDtoList: []
              };
        bill.items[idx] = obj;
        growl.error(err.data.message);
        $rootScope.stopLoader();
      });
    }
  }

    
    $scope.addKitItems = function(){

        var URI = BASE_URL + ROOT_URL + "/api/global/items/details/walkin/getKitItemsOnWalkInSales";
        
        var found = $scope.kitMasterList.find(element => element.id == $scope.kitId);
		var itemIds = [];
		
		angular.forEach(found.items,function(value,key){
			if(value.status == true){
				itemIds.push(value.itemId);
			}
			
		})
           
		
        var data = {
            "storeId": $cookies.getObject('cookieObject').storeId,
            "admissionId": ($scope.admissionId!=null && $scope.admissionId>0) ? $scope.admissionId: null,
            "encounterId":($scope.encounterId!=null && $scope.encounterId>0) ? $scope.encounterId: null,
            "itemIds" : itemIds,
            "paymentEntitlementId" : $scope.entitlementObj.paymentEntitlementTypeId,
            "unitId":$scope.unitId ,
            "organizationId": $scope.organizationId,
        }
        
         $rootScope.startLoader();
         GenericService.serviceAction("POST", URI, data).then(function (response) {
            
        	 $scope.kitItemAndBatchList = response.data;
        	 
        	   if(self.model.walkInSale.items.length == 1){
		             angular.forEach(self.model.walkInSale.items, function(i,key){
			               if(i.itemId == '' && $scope.kitItemAndBatchList.length>0){
			            	   self.model.walkInSale.items.splice(0, 1);
			               }
		             	});	
        	   }
        	 
        	 
        	 
        	 angular.forEach($scope.kitItemAndBatchList, function (value, key) {
              	 
                     var selectedBatch = value.batch.find(element => element.batchId == 0);
                    
                       var item = {
                         isConsumed:false,		
                         itemDisabled: false,
                         disabled: false,
                         itemId: value.itemId,
                         itemCode: value.itemCode,
                         markUpDown: value.markUpDown,
                         itemName: value.itemCode + " | " + value.itemName,
                         markUpDownPer: value.markUpDownPer,
                         formulationType: value.formulationType,
                         formulationTypeId: value.formulationTypeId,
                         stock: value.currentStock,
                         genericId: value.genericId,
                         genericName: value.genericName,
                         manufacturerId: value.manufacturerId,
                         manufacturerName: value.manufacturerName,
                         strength: value.strength,
                         batch: value.batch,
                         productCategoryId:value.productCategoryId,
                         assetCategoryId:value.assetCategoryId,
                         selectedBatch: !!selectedBatch?selectedBatch:commonDetailService.getDefaultBatchFromConfig(value.batch),
                         uom: [{
                           uomDetails: "Select UOM",
                         }],
                         taxId: value.taxId,
                         taxName: value.taxName,
                         taxPercent: value.taxPercent,
                         isOTC: self.model.isOTC,
                         isLasa: value.isLasa,
                         patientClassPercentage : $scope.patientClassPercentage,
                         storageUnitName : value.storageUnitName,
                         saleDetailPayeeMapperDtoList:[],
                         isThreeUOMApplicable :value.isThreeUOMApplicable,
                         patientClassPercentage : value.patientClassPercentage
                       };
                       item.colorCode=value.colorCode;
                       if(item.selectedBatch != undefined && item.selectedBatch != null){
           			
                       	item.uom = item.selectedBatch.uom;
           		        item.selectedUom = item.selectedBatch.uom.find(function (obj) {
           	              return obj.opUom == true;
           	            });
           		        
                       }
                       
                       $scope.formulationList = [];
                       var obj = {
                         "formulationType" : '',
                         "manufacturerName" : ''
                       }
                       obj.formulationType = item.formulationType;
                       obj.manufacturerName = item.manufacturerName;
                       $scope.formulationList.push(obj);
                       item.totalStock=0;
                       angular.forEach(item.batch, function (batch, key) {
                     	  item.totalStock+=batch.availableStockQuantity!=undefined && batch.availableStockQuantity!=null ? batch.availableStockQuantity:0;
                       })
                       self.model.walkInSale.items.splice(0, 0, item)                       
                       var leg =0;
                       
                       if(!!selectedBatch){
                           BatchChange(leg, self.model.walkInSale, selectedBatch)
                         }
                       
                       CalculateStock(self.model.walkInSale.items[leg], self.model.walkInSale.items[leg].uom, self.model.walkInSale.items[leg].selectedUom);
              	
        	 });
        	 
        	   angular.forEach(found.items,function(value,key){
               	var check = false;
               	 angular.forEach(self.model.walkInSale.items,function(value1,key1){
               		 
               		 if(check == false && value1.itemId == value.itemId && value1.newStock > 0 && value1.newStock >= value.quantity ){
               			
               			 value1.quantity = value.quantity;
               			 
               			 check = true;
               		 }
    					
    				})
    				
				})
				 $scope.kitId = null;
             $rootScope.stopLoader();    
            
           }, function (err) {
           angular.forEach(self.model.walkInSale.items, function (value, key) {
             if(key == idx){
               value.itemName="";
             }    	  
           });
           growl.error(err.data.message);
           $rootScope.stopLoader();
         });
     
    }
    
    
    $(document).on('keydown', function (e) {
      if (e.keyCode === 27) { // ESC
        angular.element('#formulationType').modal('hide');
      }
    });

    $scope.genericColorMapperPopUp = function (item) {
      $('#genericColorMapper').modal('show');
    }
    $scope.formulationPopup = function (item) {
        $('#formulationType').modal('show');
        if(!item.itemId){
          $scope.formulationList = [];
        }
      }
    
    $scope.goToBatch=function(itemCode,event)
    {
    	// document.getElementById('textField-'+itemCode).focus();
    	// event.preventDefault();
    }
    $scope.patientConsation = function (item) {
      if(($scope.encounterId==undefined || $scope.encounterId==null)){
        $scope.encounterId = 0;
      }
       var URI = null;
      if($scope.encounterId!=null && $scope.encounterId>0){
    	   URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId + '/'+$cookies.getObject('cookieObject').storeId  + '/' + item.itemId + '/' + $scope.entitlementObj.paymentEntitlementTypeId + '/' + $scope.encounterId;
      }
      else if($scope.admissionId!=null && $scope.admissionId>0){
    	    URI = CONSTANTS.GLOBAL.DETAILS_API_IP + '/' + $scope.unitId  + '/' + item.itemId + '/' + $scope.entitlementObj.paymentEntitlementTypeId + '/' + $scope.admissionId + '/' + $cookies.getObject('cookieObject').storeId ;
    	  }
      else{
    	   URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId + '/'+$cookies.getObject('cookieObject').storeId  + '/' + item.itemId + '/' + $scope.entitlementObj.paymentEntitlementTypeId + '/' + $scope.encounterId;
      }
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(function (response) {
          $scope.patientClassPercentage = response.data.patientClassPercentage;
          angular.forEach(self.model.walkInSale.items, function (value, key) {
        	  value.patientClassPercentage=$scope.patientClassPercentage;
        	  });
          // $scope.data.patientClassPercentage =
			// $scope.patientClassPercentage;
      });
    }

    $scope.popupStore = function () {
      var popup = angular.element('#storePopUp');
      popup.modal('show');
    }

    var isStoreChanged = function()
    {
        // console.log("WalkInSale : ",self.model.walkInSale.items);
        if(self.model.walkInSale.items.length==0)
        {
            $rootScope.storePopup();
        }
        else
        {
            StoreChangePopup();
        }
        /*
		 * if(initialStore.storeId==$cookies.getObject('selectedTopStore').storeId) {
		 * //console.log("Stores Matched"); //console.log("Initial Store Id :
		 * ",initialStore.storeId); //console.log("Current Store Id :
		 * ",$cookies.getObject('selectedTopStore').storeId);
		 * //console.log("initial store : ",initialStore); //console.log("Cookie
		 * Selected Store : ",$cookies.getObject('selectedTopStore')); //
		 * isValid=false; // growl.error('You have changed the store after
		 * selecting batch/items,Please check'); }
		 * if((initialStore.storeId!=$cookies.getObject('selectedTopStore').storeId)) {
		 * //console.log("Stores Not Matched"); //console.log("Initial Store Id :
		 * ",initialStore.storeId); //console.log("Current Store Id :
		 * ",$cookies.getObject('selectedTopStore').storeId);
		 * //console.log("initial store : ",initialStore); //console.log("Cookie
		 * Selected Store : ",$cookies.getObject('selectedTopStore'));
		 * growl.error('Are you sure to change the store'); //isValid=false;
		 * storePopup(); }
		 */
    }
    var BatchChangeOld = function (index, bill, batch) 
    {
        initialStore=JSON.parse(sessionStorage.getItem('selectedTopStore'));
        // console.log("Initial Store : ",initialStore);
       // //console.log("Current Store :
		// ",$cookies.getObject('selectedTopStore'));
        // console.log("Index : ",index);
      var item = bill.items[index];
     
      var markup;
      item.uom = [{
        uomDetails: "Select UOM",
      }];

      if (!!batch.batchId) {
        item.uom = item.uom.concat(batch.uom);

        item.selectedUom = item.uom.find(function (obj) {
          return obj.opUom;
        });

        if ($scope.isMarkup) 
        {
          markup = batch.markup;
          self.model.markupPercent = markup;
        }
       
        
      } else {
        item.selectedUom = item.uom[0];
        item.quantity = null;
        item.rate = 0;
        
       // CalculateRate(item, item.selectedUom, item.uom);
        // AllocateQuantity(index, bill);
        CalculateAmount(item);
        ApplyDiscount(self.model.walkInSale);
        
      }
       // $("#storeSelection *").attr("disabled", "disabled").off('click');
      var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.AVAILABLE_QTY_API  + $scope.unitId + "/"+ $cookies.getObject('cookieObject').storeId +'/'+item.itemId+'/'+item.selectedBatch.batchId+'/'+item.selectedUom.uomTypeId;
      GenericService.serviceAction("GET", URI, {}).then(
              function (response) 
              {
                  item.stock=response.data.availableqty;
                  item.newStock =response.data.availableqty;
              	// item.stock = 10;
                 // CalculateRate(item, item.selectedUom, item.uom);
                 // AllocateQuantity(index, bill);
                 // CalculateAmount(item);
               // ApplyDiscount(self.model.walkInSale);
                  $rootScope.stopLoader();
              },
              function (err) 
              {
                item.stock = 0;
                item.newStock = 0;
                  $rootScope.stopLoader();
               // CalculateRate(item, item.selectedUom, item.uom);
                // AllocateQuantity(index, bill);
                // CalculateAmount(item);
                 // ApplyDiscount(self.model.walkInSale);
              });
    
    }
    
      var BatchChange = function (index, bill, batch, parentIndex) {
        var item = null;

        // Assign Item and UOM
        if (parentIndex == null) {
          if(bill.items[index].isThreeUOMApplicable == false){
            if(batch){
              if(batch.uom){
                if(bill.items[index].uomListData == undefined || bill.items[index].uomListData == null){  
                  bill.items[index].uomListData = batch.uom;
                  bill.items[index].selectedBatch.uomListData = batch.uom;
                  bill.items[index].uom = [];
                  bill.items[index].selectedBatch.uom = [];
                  angular.forEach(batch.uomListData, function(u){
                    if(u.uomType != 'Stock'){
                      bill.items[index].selectedBatch.uom.push(u);
                      bill.items[index].uom.push(u);
                    }
                  })
                }
                item = bill.items[index];
              }
            }
          }
          else{
            if(batch){
              if(batch.uom){
                bill.items[index].uom = batch.uom;
                item = bill.items[index];
              }
            }
          }
        } else {
            if(bill.items[parentIndex].selectedItems[index].isThreeUOMApplicable == false){
              if(bill.items[index].uomListData == undefined || bill.items[index].uomListData == null){  
                bill.items[parentIndex].selectedItems[index].uomListData = batch.uom;
                bill.items[parentIndex].selectedItems[index].uom = [];
                angular.forEach(batch.uom, function(u){
                	 if(u.uomType != 'Stock'){
                        bill.items[parentIndex].selectedItems[index].uom.push(u);
                    }
                })
              }
              item = bill.items[parentIndex].selectedItems[index];
            }
            else{
                bill.items[parentIndex].selectedItems[index].uom = batch.uom;
                item = bill.items[parentIndex].selectedItems[index];
            }
        }
            // item.quantity = 0 ;
        if(batch){
          if (!!batch.batchId ||batch.batchId==0) {
            // Select Default UOM
            if(item.selectedUom == undefined){
              item.selectedUom = $filter('filter')(item.uom, {
                opUom: true
              }, true)[0];
            }
            $scope.selectedUomData = item.selectedUom;
            // "Select UOM" option from UOM dropdown
            if (!item.selectedUom) {
              item.selectedUom = item.uom[0];
            }
            bill.items[index].taxId=bill.items[index].selectedBatch.taxId;
        	bill.items[index].taxPercent=bill.items[index].selectedBatch.taxPercentage;
            // Caclulate amount including discount
            if(bill.items[index].quantity != undefined || bill.items[index].quantity != 0){
              Calculate(item, bill);
            }
            // CalculateAmount(item);

            // Get current stock for the selected batch
            var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.AVAILABLE_QTY_API  + $scope.unitId + "/"+ $cookies.getObject('cookieObject').storeId + "/" + item.itemId + "/" + item.selectedBatch.batchId + "/" + item.selectedUom.uomTypeId;
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
              item.stock= response.data.availableqty;
              item.newStock = response.data.availableqty;
              if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                $scope.isValidated = true;
                $('#validateButton').show();
              }
              else{
                $scope.isValidated = false;
                $('#validateButton').hide();
              }
              $scope.activePackageListWithoutForceClose=[];
				angular.forEach($scope.activePackageList, function (item, key)  {
					if(item.isForceClose==1)
						{
						$scope.activePackageListWithoutForceClose.push(item);
						}
				});
              if($scope.activePackageListWithoutForceClose!=null&& $scope.activePackageListWithoutForceClose.length>0){
                $scope.isValidated = true;
                $('#validateButton').show();
              }
              // if(item.quantity == 0 || item.quantity == undefined){
              // item.rate = 0;
              // }
              if(item.selectedUom == undefined || item.selectedUom == null){
                item.selectedUom = $scope.selectedUomData;
              }
              if($scope.atCop == true){
                item.rate = item.selectedBatch.actualLendingPrice;
                Calculate(item, self.model.walkInSale);
              }
              else{
                if(item.rate){
                  item.rate = item.rate;
                }
                else{
                  item.rate = item.selectedBatch.rate;
                  Calculate2(item, self.model.walkInSale);
                }
              }
              $rootScope.stopLoader();
              ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);
              // item.quantity = 0;
            },
            function (err) {
              $rootScope.stopLoader();
            }
          );
        }
        else{
          if(item){    
            if(item.uom){
              item.uom = [{
              uomDetails: "Select UOM",
            }];
            item.selectedUom = item.uom[0];
          }
          item.quantity = null;
          item.rate = 0;
          item.stock = null;
          item.newStock = null;
        }
      }
    } 
  }

  $scope.clearDiscount = function() {
    angular.forEach(self.model.walkInSale.items, function(data){
      data.discountPerItem = 0;
      self.model.walkInSale.discount = 0;
      self.model.walkInSale.discountCatgeory = self.model.discountCategoryList[0];
      Calculate(data, self.model.walkInSale);
    });
  }

        var Calculate = function(item, bill){
          if($scope.atCop == true){
            item.selectedBatch.sellingPrice = item.selectedBatch.actualLendingPrice;
            item.rate = item.selectedBatch.actualLendingPrice;
            item.mrp = item.rate;
          }
          var sellingPrice = !!item.selectedBatch ? commonDetailService.fixedDecimal(item.selectedBatch.sellingPrice) : 0;
          item.mrp = commonDetailService.calculateRate(item.selectedBatch.uom, item.selectedUom.uomTypeId, sellingPrice);
          self.model.beforeCalculation = item;
          // if($scope.atCop == true){
          // item.mrp = item.rate;
          // }
          var data = {
              "mrp": item.mrp,
              "isTaxInc": $scope.taxInclusive,
              // "markup": !!item.markUpDown ? item.markUpDownPer : null,
              // "markdown": !item.markUpDown ? item.markUpDownPer : null,
              "markup": item.markUpDown,
              "markUpDownPer" : item.markUpDownPer,
             // "discount": !!bill.discount ? bill.discount :
				// item.discountPerItem ? item.discountPerItem : 0,
              "discount":  item.discountPerItem ? item.discountPerItem : 0,
              "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
              "tax": (item.taxPercent != undefined && item.taxPercent != null && item.taxPercent != "") ? item.taxPercent :item.taxPercentage,
              "qty": item.quantity,
              "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
              "patientClassPercentage": item.patientClassPercentage,
              "patientClassAmount":item.patientClassAmount!=undefined && item.patientClassAmount!=null ? item.patientClassAmount:0
          }

          var response = commonDetailService.calculateAmount(data);

          // item.rate = response.rate;
          // item.taxAmount = response.taxAmount;
          // self.model.responseTaxAmount = item.taxAmount;
          // item.unitDiscountAmount = response.discountAmount;
          // item.itemtotalAmount = response.totalAmount;
          // item.netAmount = response.netAmount;
          // item.beforeCalculationRate = response.rate;
          // self.model.itemsAfterCalculation = item;
          item.rate = response.rate;
          item.taxAmount = response.taxAmount;
          item.responseTaxAmount =  response.taxAmount;
          item.unitDiscountAmount = response.unitDiscountAmount;
          item.itemDiscount =  response.discountAmount;
          item.itemtotalAmount = response.totalAmount;
          item.netAmount = response.netAmount;
          item.selfPayable = response.netAmount;
          item.creditPayable = response.creditPayable;
          item.packageConsumptionAmt = response.packageConsumptionAmt;
          item.patientClassAmount = response.patientClassAmount;
          $scope.patientClassAmount = response.patientClassAmount;
          item.unitPatientClassAmount = response.unitPatientClassAmount;
          if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
            $scope.isValidated = true;
            $('#validateButton').show();
          }
          else{
            $scope.isValidated = false;
            $('#validateButton').hide();
          }
          $scope.activePackageListWithoutForceClose=[];
			angular.forEach($scope.activePackageList, function (item, key)  {
				if(item.isForceClose==1)
					{
					$scope.activePackageListWithoutForceClose.push(item);
					}
			});
          if($scope.activePackageListWithoutForceClose!=null&& $scope.activePackageListWithoutForceClose.length>0){
            $scope.isValidated = true;
            $('#validateButton').show();
          }
          
          // Cash settlement ganesh
          CalculateItemSum(bill.items);
      }

      var Calculate2 = function(item, bill){
        if($scope.atCop == true){
          item.selectedBatch.sellingPrice = item.selectedBatch.actualLendingPrice;
          item.rate = item.selectedBatch.actualLendingPrice;
          item.mrp = item.rate;
        }
        else{
          item.selectedBatch.sellingPrice =(item.selectedBatch.rate != undefined && item.selectedBatch.rate != null )? item.selectedBatch.rate : item.selectedBatch.sellingPrice;
          // item.selectedBatch.sellingPrice =
			// item.selectedBatch.sellingPrice;
          item.rate = (item.selectedBatch.rate != undefined && item.selectedBatch.rate != null )? item.selectedBatch.rate : item.selectedBatch.sellingPrice;
          // item.rate = item.selectedBatch.sellingPrice;
          item.mrp = item.rate;
        }
        var sellingPrice = !!item.selectedBatch ? commonDetailService.fixedDecimal(item.selectedBatch.sellingPrice) : 0;
        item.mrp = commonDetailService.calculateRate(item.selectedBatch.uom, item.selectedUom.uomTypeId, sellingPrice);
        self.model.beforeCalculation = item;

          var data = {
              "mrp": item.mrp,
              "isTaxInc": $scope.taxInclusive,
              // "markup": !!item.markUpDown ? item.markUpDownPer : null,
              // "markdown": !item.markUpDown ? item.markUpDownPer : null,
               "markup": item.markUpDown,
                "markUpDownPer" : item.markUpDownPer,
             // "discount": !!bill.discount ? bill.discount :
				// item.discountPerItem ? item.discountPerItem : 0,
              "discount":  item.discountPerItem ? item.discountPerItem : 0,
              "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
              "tax": item.taxPercent,
              "qty": item.quantity,
              "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
              "patientClassPercentage": item.patientClassPercentage,
              "patientClassAmount":item.patientClassAmount!=undefined && item.patientClassAmount!=null ? item.patientClassAmount:0
          }

          var response = commonDetailService.calculateAmount(data);
          item.rate = response.rate;
          item.taxAmount = response.taxAmount;
          item.responseTaxAmount =  response.taxAmount;
          item.unitDiscountAmount = response.unitDiscountAmount;
          item.itemDiscount =  response.discountAmount;
          item.itemtotalAmount = response.totalAmount;
          item.netAmount = response.netAmount;
          item.selfPayable = response.netAmount;
          item.creditPayable = response.creditPayable;
          item.packageConsumptionAmt = response.packageConsumptionAmt;
          item.patientClassAmount = response.patientClassAmount;
          $scope.patientClassAmount = response.patientClassAmount;
          item.unitPatientClassAmount = response.unitPatientClassAmount;
          if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
            $scope.isValidated = true;
            $('#validateButton').show();
          }
          else{
            $scope.isValidated = false;
            $('#validateButton').hide();
          }
          $scope.activePackageListWithoutForceClose=[];
			angular.forEach($scope.activePackageList, function (item, key)  {
				if(item.isForceClose==1)
					{
					$scope.activePackageListWithoutForceClose.push(item);
					}
			});
          if($scope.activePackageListWithoutForceClose!=null&& $scope.activePackageListWithoutForceClose.length>0){
            $scope.isValidated = true;
            $('#validateButton').show();
          }
        }

      var onChangeDiscount = function(item, method){
        if(method == 'linewise'){
         item.discountType = 1;
        }
        if(validateDiscount(item,method)){
        if(method =='linewise'){
          angular.forEach(item.items, function (value, key) {
          if(value.discountPerItem > 100){
            growl.error("Discount should be less than Due amount or zero. ")
            value.discountPerItem = 0;
            value.patientClassPercentage = 0;
          }else{
                item.discount = 0;
                item.discountType = 1;
           
          }

          if(value.discountPerItem != ""){
            value.patientClassPercentage = $scope.patientClassPercentage;
            value.patientClassAmount = $scope.patientClassAmount;
          }
          else{
            value.patientClassPercentage =  $scope.patientClassPercentage;
            value.patientClassAmount = $scope.patientClassAmount;
          }
        }); 
        }
        else{
            angular.forEach(item.items, function (value, key) {
                // bill.items[key]['unitDiscountAmount'] = unitDiscountAmount;
                
              // value.discountPerItem = item.discount;
               
             });
        }
      }
      // else{
      // growl.error("Please Enter Valid Discount.")
      // }
      calculateAtcop();
    }
    var validateDiscount = function(item, method){
      var regexp = /^(\d{0,3}(\.\d{0,2})?)$/;
      var isValid = true;
      if(method =='linewise'){
          angular.forEach(item.items, function (value, key) {
             
               if(regexp.test(Number(value.discountPerItem))){
                  isValid = true;
               }
               else{
                  isValid = false;
                  value.discountPerItem = 0;
               }
          }); 
          }
          else{
              // if(regexp.test(Number(item.discount))){
              // isValid = true;
              // }
              // else{
              // isValid = false;
              // item.discount = 0;
              // }
            if(self.model.walkInSale.discountType == 1){
                if(item.discount > 100){
                  isValid = false;
                  item.discount = 0;
                  growl.error("Please enter valid discount");
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
    
    var ConvertQtyOrdered = function (item, uom, uomList) {
    	if(item.quantity == undefined || item.quantity == null || item.quantity == ""){
            item.quantity = 0;
          }
        var newUOM = [];
        item.selectedUom = item.selectedUom;
        if(item.isThreeUOMApplicable == false){
        	 item.uomListData.find(function (value) {
                 if (!!value.uomTypeId) {
                     newUOM.push(value);
                 }
             });
        }
        else{
        	 uom.find(function (value) {
                 if (!!value.uomTypeId) {
                     newUOM.push(value);
                 }
             });
        }
       
        if(uomList!=undefined){
        var newQty = item.quantity;
        // item.quantity = 0;
        var uomDetails = uomList.uomDetails.split(' ')[0];
        if(item.isThreeUOMApplicable == false){
          if(item.selectedBatch.uomListData == undefined || item.selectedBatch.uomListData == null){
            if (uomDetails == "P") {
              // item.quantity = Math.ceil(newQty / (newUOM[0].conversion *
				// newUOM[1].conversion * newUOM[2].conversion));
              item.newStock = parseInt(item.newStock / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
              item.receivedQtyCon = (item.quantity +"*"+ newUOM[0].conversion);
              item.receivedQtyCalculation = (item.quantity * newUOM[0].conversion);
              item.uomUnitData = newUOM[2].uomUnit;
            } else if (uomDetails == "S") {
                // item.quantity = Math.ceil(newQty / (newUOM[1].conversion *
				// newUOM[2].conversion));
                item.newStock = parseInt(item.newStock / (newUOM[1].conversion * newUOM[2].conversion));
            } else if (uomDetails == "D") {
                // item.quantity = Math.ceil(newQty / (newUOM[2].conversion));
                item.newStock = parseInt(item.newStock / (newUOM[2].conversion));
                item.receivedQtyCon = (item.quantity);
                item.receivedQtyCalculation = (item.quantity);
                item.uomUnitData = newUOM[2].uomUnit;
            }
          }
          else if(item.selectedBatch.uomListData[2] == undefined || item.selectedBatch.uomListData[2] == null){
            if (uomDetails == "P") {
              item.newStock = parseInt(item.newStock / (item.uomListData[0].conversion * item.uomListData[1].conversion * item.uomListData[2].conversion));
              item.receivedQtyCon = (item.quantity +"*"+ item.uomListData[0].conversion);
              item.receivedQtyCalculation = (item.quantity * item.uomListData[0].conversion);
              item.uomUnitData = item.uomListData[2].uomUnit;
            } else if (uomDetails == "S") {
              item.newStock = parseInt(item.newStock / (item.uomListData[1].conversion * item.uomListData[2].conversion));
            } else if (uomDetails == "D") {
              item.newStock = parseInt(item.newStock / (item.uomListData[2].conversion));
              item.receivedQtyCon = (item.quantity);
              item.receivedQtyCalculation = (item.quantity);
              item.uomUnitData = item.uomListData[2].uomUnit;
            }
          }
          else{
            if (uomDetails == "P") {
              // item.quantity = Math.ceil(newQty / (newUOM[0].conversion *
				// newUOM[1].conversion * newUOM[2].conversion));
              item.newStock = parseInt(item.newStock / (item.selectedBatch.uomListData[0].conversion * item.selectedBatch.uomListData[1].conversion * item.selectedBatch.uomListData[2].conversion));
              item.receivedQtyCon = (item.quantity +"*"+ item.selectedBatch.uomListData[0].conversion);
              item.receivedQtyCalculation = (item.quantity * item.selectedBatch.uomListData[0].conversion);
              item.uomUnitData = item.selectedBatch.uomListData[2].uomUnit;
            } else if (uomDetails == "S") {
              // item.quantity = Math.ceil(newQty / (newUOM[1].conversion *
				// newUOM[2].conversion));
              item.newStock = parseInt(item.newStock / (item.selectedBatch.uomListData[1].conversion * item.selectedBatch.uomListData[2].conversion));
            } else if (uomDetails == "D") {
              // item.quantity = Math.ceil(newQty / (newUOM[2].conversion));
              item.newStock = parseInt(item.newStock / (item.selectedBatch.uomListData[2].conversion));
              item.receivedQtyCon = (item.quantity);
              item.receivedQtyCalculation = (item.quantity);
              item.uomUnitData = item.selectedBatch.uomListData[2].uomUnit;
            }
          }
        }
        else{
          if (uomDetails == "P") {
            // item.quantity = Math.ceil(newQty / (newUOM[0].conversion *
			// newUOM[1].conversion * newUOM[2].conversion));
            item.newStock = parseInt(item.newStock / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
            item.receivedQtyCon = (item.quantity +"*"+ newUOM[0].conversion+"*"+ newUOM[1].conversion);
            item.receivedQtyCalculation = (item.quantity * newUOM[0].conversion * newUOM[1].conversion);
            item.uomUnitData = newUOM[2].uomUnit;
          } else if (uomDetails == "S") {
            // item.quantity = Math.ceil(newQty / (newUOM[1].conversion *
			// newUOM[2].conversion));
            item.newStock = parseInt(item.newStock / (newUOM[1].conversion * newUOM[2].conversion));
            item.receivedQtyCon = (item.quantity +"*"+ newUOM[1].conversion);
            item.receivedQtyCalculation = (item.quantity * newUOM[1].conversion);
            item.uomUnitData = newUOM[2].uomUnit;
          } else if (uomDetails == "D") {
            // item.quantity = Math.ceil(newQty / (newUOM[2].conversion));
            item.newStock = parseInt(item.newStock / (newUOM[2].conversion));
            item.receivedQtyCon = (item.quantity);
            item.receivedQtyCalculation = (item.quantity);
            item.uomUnitData = newUOM[2].uomUnit;
          }
        }
      }
        if(item.quantity == undefined || item.quantity == null || item.quantity == "" || item.quantity == 0){
            item.quantity = "";
          }
    }


    var UomChange = function (index, bill, uom) {
    	$scope.selectedUom=uom;
      var item = bill.items[index];
      // item.quantity =0;
      if (!!uom) {
        // var uomList = item.uom
        // CalculateRate(item, uom, uomList);
           // Caclulate amount including discount
           item.quantity = 0;
           Calculate(item, bill);

          // CalculateAmount(item);
           CalculateStock(item, item.uom, item.selectedUom);
           if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
              $scope.isValidated = true;
              $('#validateButton').show();
          }
          else{
              $scope.isValidated = false;
              $('#validateButton').hide();
          }
           $scope.activePackageListWithoutForceClose=[];
			angular.forEach($scope.activePackageList, function (item, key)  {
				if(item.isForceClose==1)
					{
					$scope.activePackageListWithoutForceClose.push(item);
					}
			});
          if($scope.activePackageListWithoutForceClose!=null&& $scope.activePackageListWithoutForceClose.length>0){
              $scope.isValidated = true;
              $('#validateButton').show();
          }

      } else {
        item.rate = 0;
      }
      // CalculateAmount(item);
     // ApplyDiscount(self.model.walkInSale);
    }

    var CalculateStock = function (item, uom, uomList) {
      if(item.quantity == undefined || item.quantity == null || item.quantity == ""){
        item.quantity = 0;
      }
      item.totalStock=0;
      angular.forEach(item.batch, function (batch, key) {
    	  item.totalStock+=batch.availableStockQuantity!=undefined && batch.availableStockQuantity!=null ? batch.availableStockQuantity:0;
      })
      if (uomList != undefined && !!uomList.uomTypeId) {
        var stockAvailable = item.stock;
        var uomDetails = uomList.uomDetails.split(' ')[0];
        if(item.isThreeUOMApplicable == false){
          if(item.uomListData == undefined || item.uomListData == null || item.uomListData == ""){
            if (uomDetails == "P") {
              item.newStock = parseInt(stockAvailable / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
              item.totalStock=parseInt(item.totalStock / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
              item.receivedQtyCon = (item.quantity +"*"+ uom[0].conversion);
              item.receivedQtyCalculation = (item.quantity * uom[0].conversion);
              item.uomUnitData = uom[2].uomUnit;      
            } 
            else if (uomDetails == "S") {
              item.newStock = parseInt(stockAvailable / (uom[1].conversion * uom[2].conversion));
              item.totalStock=parseInt(item.totalStock / (uom[1].conversion * uom[2].conversion));
            } 
            else if (uomDetails == "D") {
              item.newStock = parseInt(stockAvailable / (uom[2].conversion));
              item.totalStock=parseInt(item.totalStock / ( uom[2].conversion));
              item.receivedQtyCon = (item.quantity);
              item.receivedQtyCalculation = (item.quantity);
              item.uomUnitData = uom[2].uomUnit;    
            } 
          }
          else{
            if (uomDetails == "P") {
              item.newStock = parseInt(stockAvailable / (item.uomListData[0].conversion * item.uomListData[1].conversion * item.uomListData[2].conversion)); 
              item.totalStock=parseInt(item.totalStock / (item.uomListData[0].conversion * item.uomListData[1].conversion * item.uomListData[2].conversion));
              item.receivedQtyCon = (item.quantity +"*"+ item.uomListData[0].conversion);
              item.receivedQtyCalculation = (item.quantity * item.uomListData[0].conversion);
              item.uomUnitData = item.uomListData[2].uomUnit;      
            } 
            else if (uomDetails == "S") {
              item.newStock = parseInt(stockAvailable / (item.uomListData[1].conversion * item.uomListData[2].conversion));
              item.totalStock=parseInt(item.totalStock / (item.uomListData[1].conversion * item.uomListData[2].conversion));
            } 
            else if (uomDetails == "D") {
              item.newStock = parseInt(stockAvailable / (item.uomListData[2].conversion));
              item.totalStock=parseInt(item.totalStock / (item.uomListData[2].conversion));
              item.receivedQtyCon = (item.quantity);
              item.receivedQtyCalculation = (item.quantity);
              item.uomUnitData = item.uomListData[2].uomUnit;    
            }
          }
        }
        else{
          if (uomDetails == "P") {
            item.newStock = parseInt(stockAvailable / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.totalStock=parseInt(item.totalStock / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.receivedQtyCon = (item.quantity +"*"+ uom[0].conversion +"*"+ uom[1].conversion);
            item.receivedQtyCalculation = (item.quantity * uom[0].conversion * uom[1].conversion);
            item.uomUnitData = uom[2].uomUnit;           
          } 
          else if (uomDetails == "S") {
            item.newStock = parseInt(stockAvailable / (uom[1].conversion * uom[2].conversion));
            item.totalStock=parseInt(item.totalStock / (uom[1].conversion * uom[2].conversion));
            item.receivedQtyCon = (item.quantity +"*"+ uom[0].conversion);
            item.receivedQtyCalculation = (item.quantity * uom[0].conversion);
            item.uomUnitData = uom[2].uomUnit;  
          } 
          else if (uomDetails == "D") {
            item.newStock = parseInt(stockAvailable / (uom[2].conversion));
            item.totalStock=parseInt(item.totalStock / ( uom[2].conversion));
            item.receivedQtyCon = (item.quantity);
            item.receivedQtyCalculation = (item.quantity);
            item.uomUnitData = uom[2].uomUnit;
          }
        }
      } 
      else {
        item.stock = 0;
        item.newStock = 0;
      }
    }

    var CalculateRate = function (item, uom, uomList) {
      if (!!uom) {
        var sellingPrice = !!item.selectedBatch ? commonDetailService.fixedDecimal(item.selectedBatch.sellingPrice) : 0;

        var conversion = 1;
        var flag = false;

        angular.forEach(uomList, function (value, key) {
          if (value.uomTypeId == uom.uomTypeId || flag) {
            conversion = conversion * value.conversion;
            flag = true;
          }
        });
        if ($scope.taxInclusive == true) {
          item.excludeRate = conversion * sellingPrice;
          item.quantity = item.quantity ? item.quantity : 0;
          item.taxAmount = (((item.excludeRate) * (item.taxPercent)) / (100 + (item.taxPercent))) * (item.quantity);
          item.rate = commonDetailService.fixedDecimal(conversion * sellingPrice);

          if (self.model.markupPercent) {
            if ($scope.markupUp) {
              item.rate = commonDetailService.fixedDecimal(item.rate + (item.rate / parseFloat(self.model.markupPercent)));
            }
            else {
              item.rate = commonDetailService.fixedDecimal(item.rate - (item.rate / parseFloat(self.model.markupPercent)));
            }
          }
        }
        else {
          item.excludeRate = item.rate = commonDetailService.fixedDecimal(conversion * sellingPrice);

          if (self.model.markupPercent) {
            if ($scope.markupUp) {
              item.excludeRate = item.rate = commonDetailService.fixedDecimal(item.rate + (item.rate / parseFloat(self.model.markupPercent)));
            }
            else {
              item.excludeRate = item.rate = commonDetailService.fixedDecimal(item.rate - (item.rate / parseFloat(self.model.markupPercent)));
            }
          }
        }
      } else {
        item.rate = 0;
      }
    } 

    var CalculateAmount = function (item) {
    	
    	 $scope.totalSelfPayable = parseFloat(0);
    	 $scope.totalCreditPayable = parseFloat(0);
      if ($scope.taxInclusive == true) {
        if(item.rate){
          item.totalAmount = ((item.rate ? item.rate : 0) * (item.quantity ? item.quantity : 0));
          item.discountPercentage = ((item.discount / (item.totalAmount - item.patientClassPercentage)) * 100).toFixed(2);
          item.netAmount = (((item.totalAmount) - (item.patientClassAmount) - (isNaN(item.discount)?0:item.discount)));
          item.selfPayable = (isNaN(item.selfPayable)?0:item.selfPayable);
          item.creditPayable = (isNaN(item.creditPayable)?0:item.creditPayable);
        }
        else{
          item.totalAmount = ((item.selectedBatch.rate ? item.selectedBatch.rate : 0) * (item.quantity ? item.quantity : 0));
          item.discountPercentage = ((item.discount / (item.totalAmount - item.patientClassPercentage)) * 100).toFixed(2);
          item.netAmount = (((item.totalAmount) - (item.patientClassAmount) - (isNaN(item.discount)?0:item.discount)));
          item.selfPayable = (isNaN(item.selfPayable)?0:item.selfPayable);
          item.creditPayable = (isNaN(item.creditPayable)?0:item.creditPayable);
        }

       
        
      }
      else {
        if(item.rate){
          item.totalAmount = ((item.rate ? item.rate : 0) * (item.quantity ? item.quantity : 0));
          item.taxAmount = (((item.totalAmount) - (item.patientClassAmount) - (isNaN(item.itemDiscount) ? 0 : item.itemDiscount)) * (item.taxPercent / 100));
          item.discountPerItem = ((item.itemDiscount / (item.totalAmount - item.patientClassAmount)) * 100).toFixed(2);
          item.discountPerItem = (isNaN(item.discountPerItem)?0:item.discountPerItem);
          item.netAmount = (((item.totalAmount) - (item.patientClassAmount) -  (isNaN(item.itemDiscount) ? 0 : item.itemDiscount) + item.taxAmount));
          item.selfPayable = (isNaN(item.selfPayable)?0:item.selfPayable);
          item.creditPayable = (isNaN(item.creditPayable)?0:item.creditPayable);
        }
        else{
          item.totalAmount = ((item.selectedBatch.rate ? item.selectedBatch.rate : 0) * (item.quantity ? item.quantity : 0));
          item.taxAmount = (((item.totalAmount) - (item.patientClassAmount) - (isNaN(item.itemDiscount) ? 0 : item.itemDiscount)) * (item.taxPercent / 100));
          item.discountPerItem = ((item.itemDiscount / (item.totalAmount - item.patientClassAmount)) * 100).toFixed(2);
          item.netAmount = (((item.totalAmount) - (item.patientClassAmount) - (isNaN(item.itemDiscount) ? 0 : item.itemDiscount) + item.taxAmount));
          item.selfPayable = (isNaN(item.selfPayable)?0:item.selfPayable);
          item.creditPayable = (isNaN(item.creditPayable)?0:item.creditPayable);
          item.discountPerItem = (isNaN(item.discountPerItem)?0:item.discountPerItem);
        }
        

      }
      if(!item.isConsumed)
  	   {
  	     item.selfPayable = item.netAmount;
  	   }
    }
    
    
    
    // Ganesh
    var CalculateItemSum = function (itemList) 
    {
    	 $scope.totalSelfPayable = parseFloat(0);
    	 $scope.totalCreditPayable = parseFloat(0);
    	 angular.forEach(itemList, function (item, key) 
    {
        if ($scope.taxInclusive == true) 
        {
          item.itemtotalAmount = ((item.rate ? item.rate : 0) * (item.quantity ? item.quantity : 0));
        // item.netAmount = (((item.itemtotalAmount) - (item.itemDiscount)));
          item.netAmount = item.netAmount;
//          if(item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null && item.packageConsumptionAmt>0)
//        	  {
//          item.taxAmount = (((item.itemtotalAmount) - (item.patientClassAmount) - (isNaN(item.itemDiscount) ? 0 : item.itemDiscount)-(!isNaN(item.packageConsumptionAmt) && item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null ? item.packageConsumptionAmt:0)) * (item.taxPercent / 100));
//        	  }
          item.selfPayable = (isNaN(item.selfPayable)?0:item.selfPayable);
          item.creditPayable = (isNaN(item.creditPayable)?0:item.creditPayable);
          item.packageConsumptionAmt = (isNaN(item.packageConsumptionAmt)?0:item.packageConsumptionAmt);
          
          $scope.totalSelfPayable = $scope.totalSelfPayable+ item.selfPayable;
          $scope.totalCreditPayable = $scope.totalCreditPayable+ item.creditPayable;
          
          if (item.selectedItems && !item.itemDisabled) {
              angular.forEach(item.selectedItems, function (sitem, key) 
            		  {
            	  sitem.itemtotalAmount = ((sitem.rate.toFixed(2) ? sitem.rate.toFixed(2) : 0) * (sitem.quantity ? sitem.quantity : 0));
//            	  if(item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null && item.packageConsumptionAmt>0)
//            	  {
//              item.taxAmount = (((item.itemtotalAmount) - (item.patientClassAmount) - (isNaN(item.itemDiscount) ? 0 : item.itemDiscount)-(!isNaN(item.packageConsumptionAmt) && item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null ? item.packageConsumptionAmt:0)) * (item.taxPercent / 100));
//            	  }
            	  sitem.netAmount = (((sitem.itemtotalAmount) - (sitem.itemDiscount)));
            	  sitem.selfPayable = (isNaN(sitem.selfPayable)?0:sitem.selfPayable);
            	  sitem.creditPayable = (isNaN(sitem.creditPayable)?0:sitem.creditPayable);
            	  sitem.packageConsumptionAmt = (isNaN(sitem.packageConsumptionAmt)?0:sitem.packageConsumptionAmt);
                  $scope.totalSelfPayable = $scope.totalSelfPayable+ sitem.selfPayable;
                  $scope.totalCreditPayable = $scope.totalCreditPayable+ sitem.creditPayable;
              });
          }
        }
        else 
         {
            if(item.rate){
              item.itemtotalAmount = ((item.rate.toFixed(2) ? item.rate.toFixed(2) : 0) * (item.quantity ? item.quantity : 0));
            }
          // item.taxAmount = (((item.itemtotalAmount) - (item.itemDiscount))
			// * (item.taxPercentage / 100));
          // item.netAmount = (((item.itemtotalAmount) - (item.itemDiscount) +
			// item.taxAmount));
//            if(item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null && item.packageConsumptionAmt>0)
//      	  {
//        item.taxAmount = (((item.itemtotalAmount+parseFloat(item.taxAmount)) - (item.patientClassAmount) - (isNaN(item.itemDiscount) ? 0 : item.itemDiscount)-(!isNaN(item.packageConsumptionAmt) && item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null ? item.packageConsumptionAmt:0)) * (item.taxPercent / 100));
//      	  }
          item.taxAmount = item.taxAmount;
          item.netAmount = item.netAmount;
          item.selfPayable = (isNaN(item.selfPayable)?0:item.selfPayable);
          item.creditPayable = (isNaN(item.creditPayable)?0:item.creditPayable);
          $scope.totalSelfPayable = $scope.totalSelfPayable+ item.selfPayable;
          $scope.totalCreditPayable = $scope.totalCreditPayable+ item.creditPayable;
          
          if (item.selectedItems && !item.itemDisabled) {
              angular.forEach(item.selectedItems, function (sitem, key) 
            		  {
            	  sitem.itemtotalAmount = ((sitem.rate.toFixed(2) ? sitem.rate.toFixed(2) : 0) * (sitem.quantity ? sitem.quantity : 0));
//            	  if(item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null && item.packageConsumptionAmt>0)
//              	  {
//                item.taxAmount = (((item.itemtotalAmount+parseFloat(item.taxAmount)) - (item.patientClassAmount) - (isNaN(item.itemDiscount) ? 0 : item.itemDiscount)-(!isNaN(item.packageConsumptionAmt) && item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null ? item.packageConsumptionAmt:0)) * (item.taxPercent / 100));
//              	  }
            	  sitem.taxAmount = (((sitem.itemtotalAmount) - (sitem.itemDiscount)) * (sitem.taxPercentage / 100));
            	  sitem.netAmount = (((sitem.itemtotalAmount) - (sitem.itemDiscount) + sitem.taxAmount));
            	  sitem.selfPayable = (isNaN(sitem.selfPayable)?0:sitem.selfPayable);
            	  sitem.creditPayable = (isNaN(sitem.creditPayable)?0:sitem.creditPayable);
                  $scope.totalSelfPayable = $scope.totalSelfPayable+ item.selfPayable;
                  $scope.totalCreditPayable = $scope.totalCreditPayable+ item.creditPayable;
              });
          }
         }
        
    	 });
    	
    	 $scope.totalCreditPayable = parseFloat($scope.totalCreditPayable).toFixed(2);
    	 $scope.totalSelfPayable = parseFloat($scope.totalSelfPayable).toFixed(2);
      }
    
    

    $scope.checkAmount = function (amount) {
      if (amount > $scope.dueAmount || amount == 0) {
        growl.error("Amount not allowed greater than Due amount or zero. ", {
          title: ERROR_MSG
        });
        $scope.paymentDetails.cashAmount = parseFloat($scope.dueAmount).toFixed(2);
        $scope.paymentDetails.chequeAmount = parseFloat($scope.dueAmount).toFixed(2);
        $scope.paymentDetails.credAmount = parseFloat($scope.dueAmount).toFixed(2);
        $scope.eTransferObj.transferAmount= parseFloat($scope.dueAmount).toFixed(2);
        return;

      } else {
        $scope.paymentDetails.cashAmount = parseFloat(amount).toFixed(2);
        $scope.paymentDetails.chequeAmount = parseFloat(amount).toFixed(2);
        $scope.paymentDetails.credAmount = parseFloat(amount).toFixed(2);
        $scope.eTransferObj.transferAmount= parseFloat(amount).toFixed(2);
      }
    }

    var OnKeyPressQuantity = function (item) {
      if (!item.quantity) {
        item.totalAmount = 0;
        item.itemDiscount = 0;
        item.taxAmount = 0;
        item.netAmount = 0;
      }
    }

    var SetDecimal = function () {
      if (isNaN(self.model.walkInSale.discount) || !self.model.walkInSale.discount) {
        self.model.walkInSale.discount = 0;
      } else {
        self.model.walkInSale.discount = parseFloat(self.model.walkInSale.discount).toFixed(2);
      }
    }

    var ApplyDiscount = function (bill) {
    	$scope.totalSelfPayable = 0;
      if(bill.discountType == '2' && bill.netAmountData == undefined){
        bill.netAmountData = bill.netAmount;
      }
      if(ApplyDiscountValidation(bill) && CheckQuantityForDiscount(bill)){
    	  $scope.focusInput=true;
      var item = bill.items;
      var discount = parseFloat(bill.discount);
      if (!isNaN(discount)) {
        if (bill.discountType == '2') {
          if (discount <= bill.totalAmount) {
            var itemDiscount = discount / bill.totalAmount;

            angular.forEach(bill.items, function (value, key) {
              if (value.totalAmount > itemDiscount) {
                value.itemDiscount = value.totalAmount * itemDiscount;
              } else {
                value.itemDiscount = 0;
              }

              var unitDiscountAmount = value.itemDiscount / value.quantity;
              bill.items[key]['unitDiscountAmount'] = unitDiscountAmount;
              value.discountPerItem= ((discount / (bill.totalAmount-(value.patientClassAmount!=undefined ? value.patientClassAmount:0)))*100).toFixed(2);
              value.itemDiscount  =  value.itemtotalAmount * value.discountPerItem / 100;
// value.discountPerItem= ((discount / bill.totalAmount)*100).toFixed(2);
              CalculateAmount(value);
              var data = {
                "mrp": value.mrp,
                "isTaxInc": $scope.taxInclusive,
                // "markup": value.markUpDown == 1 ? value.markUpDownPer : null,
                // "markdown": value.markUpDown == 0 ? value.markUpDownPer :
				// null,
                 "markup": value.markUpDown,
                "markUpDownPer" : value.markUpDownPer,
                "discount": !!value.discountPerItem ? value.discountPerItem : 0,
                "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                "tax": value.taxPercent,
                "qty": value.quantity,
                "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                "patientClassPercentage": value.patientClassPercentage,
                "patientClassAmount":value.patientClassAmount!=undefined && value.patientClassAmount!=null ? value.patientClassAmount:0
            }
            var response = commonDetailService.calculateAmount(data);
            value.rate = response.rate;
            value.taxAmount = response.taxAmount;
            value.responseTaxAmount =  response.taxAmount;
            value.unitDiscountAmount = response.unitDiscountAmount;
            value.itemDiscount =  response.discountAmount;
            value.itemtotalAmount = response.totalAmount;
            value.netAmount = response.netAmount;
            value.selfPayable = response.netAmount;
            value.patientClassAmount = response.patientClassAmount;
            value.patientClassPercentage = response.patientClassPercentage;
            value.creditPayable = response.creditPayable;
	          value.packageConsumptionAmt = response.packageConsumptionAmt;
            $scope.totalSelfPayable = $scope.totalSelfPayable + value.selfPayable;
            // CalculateAmount(value);
            });
            self.model.isDiscountAppliedValid = false;
            self.model.isDiscountApplied = true;
            self.model.isAppliedDiscountValid = false
          } else {
            SetDiscountFlags();
            // growl.error('Please enter discount amount less than Total
			// Amount');
          }
          if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
            $scope.isValidated = true;
            $('#validateButton').show();
          }
          else{
            $scope.isValidated = false;
            $('#validateButton').hide();
          }
          $scope.activePackageListWithoutForceClose=[];
			angular.forEach($scope.activePackageList, function (item, key)  {
				if(item.isForceClose==1)
					{
					$scope.activePackageListWithoutForceClose.push(item);
					}
			});
          if($scope.activePackageListWithoutForceClose!=null&& $scope.activePackageListWithoutForceClose.length>0){
            $scope.isValidated = true;
            $('#validateButton').show();
          }
        } else if (bill.discountType == '1') {
          if (self.model.walkInSale.discount <= discountLimit) {
            angular.forEach(bill.items, function (value, key) {
              // value.itemDiscount = value.totalAmount * discount * 0.01;

              // var unitDiscountAmount = value.itemDiscount / value.quantity;
              // bill.items[key]['unitDiscountAmount'] = unitDiscountAmount;
              // CalculateAmount(value);
              var data = {
                "mrp": value.mrp,
                "isTaxInc": $scope.taxInclusive,
                // "markup": value.markUpDown == 1 ? value.markUpDownPer : null,
                // "markdown": value.markUpDown == 0 ? value.markUpDownPer :
				// null,
                 "markup": value.markUpDown,
                "markUpDownPer" : value.markUpDownPer,
                "discount": !!bill.discount ? bill.discount : 0,
                "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                "tax": value.taxPercent,
                "qty": value.quantity,
                "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                "patientClassPercentage": value.patientClassPercentage,
                "patientClassAmount":value.patientClassAmount!=undefined && value.patientClassAmount!=null ? value.patientClassAmount:0
                
            }
            var response = commonDetailService.calculateAmount(data);
            value.rate = response.rate;
            value.taxAmount = response.taxAmount;
            value.responseTaxAmount =  response.taxAmount;
            value.unitDiscountAmount = response.unitDiscountAmount;
            value.itemDiscount =  response.discountAmount;
            value.itemtotalAmount = response.totalAmount;
            value.netAmount = response.netAmount;
            value.discountPerItem= discount;
            value.selfPayable = response.netAmount;
            value.patientClassAmount = response.patientClassAmount;
            value.patientClassPercentage = response.patientClassPercentage;
            value.creditPayable = response.creditPayable;
	          value.packageConsumptionAmt = response.packageConsumptionAmt;
            $scope.totalSelfPayable = $scope.totalSelfPayable + value.selfPayable;
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
            $('#validateButton').show();
          }
          else{
            $scope.isValidated = false;
            $('#validateButton').hide();
          }
          $scope.activePackageListWithoutForceClose=[];
			angular.forEach($scope.activePackageList, function (item, key)  {
				if(item.isForceClose==1)
					{
					$scope.activePackageListWithoutForceClose.push(item);
					}
			});
          if($scope.activePackageListWithoutForceClose!=null&& $scope.activePackageListWithoutForceClose.length>0){
            $scope.isValidated = true;
            $('#validateButton').show();
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
          if ((parseFloat(bill.discount) > bill.totalAmount) && (parseFloat(bill.discount) > bill.netAmountData) && (parseFloat(bill.discount) > bill.netAmount) && (bill.discountType == '2')) {
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
      });
      return isValid
    }

    var ApplyDiscountValidation = function (bill) {
      var isValid = true;
     // if (CheckQuantityForDiscount(bill)) {
        self.model.discountTypeFlag = false
        // if(!self.model.walkInSale.discountCatgeory.id){
        // growl.error('Please select Discount Catgeory');
        // isValid = false;
        // }else{
          if (!self.model.walkInSale.discount) {
            if (bill.discountType == '1') {
              growl.error('Please enter % discount');
              isValid = false;
            }
            if (bill.discountType == '2') {
              growl.error('Please enter discount amount');
              isValid = false;
            }
            SetDiscountFlags();
          }
        // }
      
     // }
      return isValid
    }

    var AddItem = function (index, bill) {
     // bill.items.splice(index + 1, 0, CopyItem(index, bill));
    	
    	 bill.items.push(CopyItem(index, bill));
       if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
          $scope.isValidated = true;
          $('#validateButton').show();
      }
      else{
          $scope.isValidated = false;
          $('#validateButton').hide();
      }
       $scope.activePackageListWithoutForceClose=[];
		angular.forEach($scope.activePackageList, function (item, key)  {
			if(item.isForceClose==1)
				{
				$scope.activePackageListWithoutForceClose.push(item);
				}
		});
      if($scope.activePackageListWithoutForceClose!=null&& $scope.activePackageListWithoutForceClose.length>0){
          $scope.isValidated = true;
          $('#validateButton').show();
      }
      SetFocus();
    }

    var AddItem1 = function (index, bill) {
     // bill.items.splice(index + 1, 0, CopyItem(index, bill));
    	        var item=CopyItem1(index, bill);
		        bill.items.splice(0, 0, item)
       if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
          $scope.isValidated = true;
          $('#validateButton').show();
      }
      else{
          $scope.isValidated = false;
          $('#validateButton').hide();
      }
		        $scope.activePackageListWithoutForceClose=[];
				angular.forEach($scope.activePackageList, function (item, key)  {
					if(item.isForceClose==1)
						{
						$scope.activePackageListWithoutForceClose.push(item);
						}
				});
      if($scope.activePackageListWithoutForceClose!=null&& $scope.activePackageListWithoutForceClose.length>0){
          $scope.isValidated = true;
          $('#validateButton').show();
      }
      angular.forEach(bill.items,function(val, key){
      	if(!val.itemCode && !val.itemName){
      		$scope.key = key;
      	}
      });
      var elementIndex= $scope.key + 1;
      // var elementIndex= index + 1;
      var itemFeildId="item-name_"+elementIndex+"";
      setTimeout(() => {
      document.getElementById(""+itemFeildId).focus();
      // event.preventDefault();
      }, 200);
    }

    var CopyItem = function (index, bill) {
      var item = bill.items[index];
      return {
        itemCode: item.itemCode,
        itemName: item.itemName,
        itemId: item.itemId,
        genericName: item.genericName,
        genericId: item.genericId,
        formulationTypeId: item.formulationTypeId,
        manufacturerName: item.manufacturerName,
        batch: item.batch,
        // selectedBatch:
		// commonDetailService.getDefaultBatchFromConfig(item.batch),
        selectedBatch: item.selectedBatch,
        uom: item.uom,
        selectedUom: item.selectedUom,
        markUpDown: item.markUpDown,
        markUpDownPer: item.markUpDownPer,
        taxId: item.taxId,
        taxName: item.taxName,
        taxPercent: item.taxPercent,
        formulationType: item.formulationType,
        stock: item.newStock,
        totalStock: item.totalStock,
        strength: item.strength,
        productCategoryId: item.productCategoryId,
        assetCategoryId: item.assetCategoryId,
        patientClassPercentage: item.patientClassPercentage,
        patientClassAmount: item.patientClassAmount,
        saleDetailPayeeMapperDtoList: [],
        itemDisabled: false,
        disabled: false,
        storageUnitName : item.storageUnitName,
        receivedQtyCon: item.receivedQtyCon,
        receivedQtyCalculation: item.receivedQtyCalculation,
        uomUnitData: item.uomUnitData,
        isThreeUOMApplicable: item.isThreeUOMApplicable,
        uomListData: item.uomListData,
        quantity: item.quantity,
        colorCode:item.colorCode
      }
    }

    var CopyItem1 = function (index, bill) {
      var item = bill.items[index];
      return {
        itemCode: '',
        itemName: '',
        itemId: '',
        genericName: '',
        manufacturerName: '',
        batch: '',
        selectedBatch: '',
        uom: '',
        selectedUom: '',
        markUpDown: '',
        markUpDownPer: '',
        taxId: '',
        taxName: '',
        taxPercent: '',
        formulationType: '',
        stock: '',
        strength: '',
        productCategoryId: '',
        assetCategoryId: '',
        patientClassPercentage: '',
        patientClassAmount: '',
        saleDetailPayeeMapperDtoList: [],
        itemDisabled: false,
        disabled: false,
        storageUnitName: '',
        colorCode:''
      }
    }

    var RemoveItem = function (index, bill)
    
    {
      var item = bill.items[index];

      if (item!=undefined && !!item.allocationId) {
        var URI = CONSTANTS.PHARMACY.OP.WALK_IN_SALES.ALLOCATE_API + $scope.unitId + "/"+$scope.organizationId +"/"+$cookies.getObject('cookieObject').storeId +"/"+$scope.userId +"/" + bill.id + "/" + item.allocationId;

        $rootScope.startLoader();
        GenericService.serviceAction("DELETE", URI, {}).then(
          function (response) {
            if (!!response.data) {
              bill.items.splice(index, 1);
              self.model.walkInSale.discount = undefined;
              self.model.walkInSale.discountCatgeory = self.model.discountCategoryList[0];
            }
            if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
              $scope.isValidated = true;
              $('#validateButton').show();
            }
            else{
              $scope.isValidated = false;
              $('#validateButton').hide();
            }
            $scope.activePackageListWithoutForceClose=[];
			angular.forEach($scope.activePackageList, function (item, key)  {
				if(item.isForceClose==1)
					{
					$scope.activePackageListWithoutForceClose.push(item);
					}
			});
            if($scope.activePackageListWithoutForceClose!=null&& $scope.activePackageListWithoutForceClose.length>0){
              $scope.isValidated = true;
              $('#validateButton').show();
            }
            $rootScope.stopLoader();
            growl.success(response.data.message);

          },
          function (ex) {
            $rootScope.stopLoader();
            growl.error('Error while deleting allocated quantity');
          });
          CalculateItemSum(bill.items);
      } else {
        bill.items.splice(index, 1);
      }
      bill.items.forEach(function (billitem) {
        item['isBatchValid'] = false;
        item['isUOMValid'] = false;
        item['isQuantityValid'] = false;
        item['isItemValid'] = false;
      });
    }

    var AllocateQuantity = function (index, bill) {
      if(ValidateQuantity(bill.items[index])){
         if (!!bill) {
          var item = bill.items[index];

          CalculateQuantity(item, item.selectedUom, item.uom);

          if (CanAllocate(item)) {
            var URI = CONSTANTS.PHARMACY.OP.WALK_IN_SALES.ALLOCATE_API + $scope.unitId + "/"+$scope.organizationId +"/"+$cookies.getObject('cookieObject').storeId  +"/"+$scope.userId+"/"+item.itemId+"/"+ bill.id + "/" + (!!item.allocationIds && item.allocationIds[0] >0 ? item.allocationIds : "");
              var data = {
              "batchId": item.selectedBatch.batchId,
              "quantity": item.leaseQuantity
            };

            if(parseInt(item.quantity) > 0 ){
              $rootScope.startLoader();
                GenericService.serviceAction("PUT", URI, data).then(
                  function (response) {
                    $scope.isValidated = false;
                    if (!!response.data) 
                    {
                    // item.allocationId = response.data.id;
                    item.allocationId = response.data.id;
                      item.allocationIds = response.data.ids;
                     // if(response.data.object){
                      if(response.data.object!=undefined && response.data.object!=null){  
                        item.stock = response.data.object;
                        item.newStock = response.data.object;
                      }
                      ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);
                      
                      if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                          $scope.isValidated = true;
                          $('#validateButton').show();
                      }
                      else{
                          $scope.isValidated = false;
                          $('#validateButton').hide();
                      }
                      $scope.activePackageListWithoutForceClose=[];
          			angular.forEach($scope.activePackageList, function (item, key)  {
          				if(item.isForceClose==1)
          					{
          					$scope.activePackageListWithoutForceClose.push(item);
          					}
          			});
                      if($scope.activePackageListWithoutForceClose!=null&& $scope.activePackageListWithoutForceClose.length>0){
                          $scope.isValidated = true;
                          $('#validateButton').show();
                      }
                      StorePreviousValue(item);
                    }
                    item.summary = '';
                    $rootScope.stopLoader();
                  },
                  function (ex) {

                    growl.error(ex.data.summary);
                    $scope.isValidated = true;
                    // RestorePreviousValue(item);
                    $rootScope.stopLoader();
                  });
            }
          
          } else {
            StorePreviousValue(item);
          }
        }
      }
    }

    var CanAllocate = function (item) {
      if (!!item.selectedBatch && !!item.selectedUom &&
        (!!item.quantity) && !(item.prevQuantity == item.quantity && item.quantity == '')) {
        return true;
      }

      return false;
    }

    var StorePreviousValue = function (item) {
      item.prevBatch = item.selectedBatch;
      item.prevUom = item.selectedUom;
      item.prevQuantity = item.quantity;

    // CalculateRate(item, item.selectedUom, item.uom);
    }

    var RestorePreviousValue = function (item) {
      item.selectedBatch = item.prevBatch;

      if (!!item.selectedBatch) {
        item.selectedUom = item.prevUom;
      } else {
        item.uom = [{
          uomDetails: "Select UOM",
        }];
        item.selectedUom = item.uom[0];
        item.rate = 0;
      }

      item.quantity = item.prevQuantity;

   // CalculateRate(item, item.selectedUom, item.uom);
    }

    var CalculateQuantity = function (item, uom, uomList) {
      if (!!uom) {
        var conversion = 1;
        var flag = false;

        angular.forEach(uomList, function (value, key) {
          if (value.uomTypeId == uom.uomTypeId || flag) {
            conversion = conversion * value.conversion;
            flag = true;
          }
        });

        item.leaseQuantity = conversion * item.quantity;
      } else {
        item.leaseQuantity = 0;
      }
    }

    var GetStrengthUnitList = function () {

      var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY. ITEM_MASTER.UNIT_API + '/'+$scope.organizationId ;
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.strengthUnitList = response.data;
            // console.log("Strength Units : ",self.model.strengthUnitList);
            $rootScope.stopLoader();
          },
          function errorCallback(response) {
            // console.log("Error");
            // console.log("Strength Units : ",self.model.strengthUnitList);
            $rootScope.stopLoader();
          });
    }
    GetStrengthUnitList();

   
    var ViewStockCheckDetails = function(item) {
      var URI = CONSTANTS.PHARMACY.OP.WALK_IN_SALES.STOCK_CHECK + $scope.unitId + "/"+$scope.organizationId + "/" + item.itemId + "/" + $cookies.getObject('cookieObject').storeId ;
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(function (response) {
          self.model.stockCheckList = response.data
          var popup = angular.element("#stockCheckDetails");
          popup.modal("show");
          $rootScope.stopLoader();
      },
      function errorCallback(response) {
          $rootScope.stopLoader();
      });
    }
    $scope.saveWalkInSaleFlag=true;
    $scope.confirmMessage = "Are you sure you want to Save?";
    var SavePopup = function () {
    	$scope.saveWalkInSaleFlag=true;
      if (ValidateBill(self.model.walkInSale)) 
      {
    	  var isNonOtcCounter=0;
    	  var checkLasa = false;
    	  angular.forEach(self.model.walkInSale.items, function (item, key)
          {
            if(item.isOTC==false)
                {
                    isNonOtcCounter++;
                }
                if(item.isLasa !== undefined && item.isLasa !== null && item.isLasa === true){
                    checkLasa = true;
                }

          });

          if(checkLasa){
               checkLasa = false;
               $scope.confirmMessage = "List contains LASA drugs. Are you sure you want to Save?";
          }
          else{
              $scope.confirmMessage = "Are you sure you want to Save?";
          }

    	  if(isNonOtcCounter>0 && (self.model.walkInSale.doctorName==undefined || self.model.walkInSale.doctorName==null || self.model.walkInSale.doctorName==''))
    			  {
    		  		growl.error("Please Select Doctor");
    		  		return false;
    			  }
    	if($scope.isConsolidatedBill == 'Y'  &&  ($scope.billNumber != null || $scope.billNumber != '' || $scope.billNumber != undefined) 
    			&& $scope.paymentEntitleMentList.length > 0 && $scope.encounterId > 0 && self.model.cashSettlement == false)
      	{
    			 var popup = angular.element('#confirmSaveIp');
                 popup.modal('show'); 
      		
      	}else if($scope.admissionId > 0){
      		var popup = angular.element('#confirmSave');
            popup.modal('show');
      	}
      	else{
      		var popup = angular.element('#confirmSave');
            popup.modal('show');
      	}
      }
    }

      var StoreChangePopup = function () {
          var popup = angular.element('#storeChangePopUp');
          popup.modal('show');
      }

      var resetStore = function()
      {
         // ClearFields(self.model.walkInSale.items);
           var obj = {
        itemCode: '',
        itemName: '',
        itemId: '',
        genericName: '',
        manufacturerName: '',
        batch: '',
        selectedBatch: '',
        uom: '',
        selectedUom: '',
        markUpDown: '',
        markUpDownPer: '',
        taxId: '',
        taxName: '',
        taxPercent: '',
        formulationType: '',
        stock: '',
        strength: '',
        productCategoryId: '',
        assetCategoryId: '',
        patientClassPercentage: '',
        patientClassAmount: '',
        saleDetailPayeeMapperDtoList: []
      };
      self.model.walkInSale = {
        "id": self.model.walkInSale.id,
        "items" : []
      }
      self.model.walkInSale.items.push(obj);
          $rootScope.storePopup();
      }

    var ConfirmationPopup = function () {
      var popup = angular.element('#confirmationPopUp');
      popup.modal('show');
    }
    
    $scope.saveBtn = true;
    $scope.addBtnDisabled = false;
    
    var SaveBill = function (bill,addToBill){
    	$scope.saveBtn = false;
        $scope.addBtnDisabled = true;
        if($scope.saveWalkInSaleFlag==true)
  	  {
        	$scope.saveWalkInSaleFlag = false;	
        	ActualSaveBill(bill,addToBill);
  	  }
        
    	
    }
    
    $scope.patientSaleTypeId = 1;
    $scope.idsToAddServicesInExistingBill=[];

    var ActualSaveBill = function (bill,addToBill) 
    {
      $scope.saveBtn = false;
      $scope.addBtnDisabled = true;
      if (ValidateBill(bill)) 
      {
    	  $scope.saveBtn = false;
        $scope.addBtnDisabled = true;
    	  self.model.patientSearchFlag = true;
    	    // check contract Ganesh
    	  $scope.totalSelfPayable = 0.00;
    	  $scope.totalCreditPayable = 0.00;
          if($scope.entitlementObj.paymentEntitlementTypeId == '3'||$scope.entitlementObj.paymentEntitlementTypeId == '2')
    		{
    		   var found = $scope.paymentEntitleMentList.find(ar=>(ar.paymentEntitlementId==2||ar.paymentEntitlementId==3));
    		   if(found==undefined)
    			   {
    			     growl.error('Please Apply Payment Entitlement.');
               $scope.addBtnDisabled = false;
               $scope.saveBtn = true;
    			     return false;
    			   }
    		
    		}
    
        var URI = CONSTANTS.PHARMACY.OP.WALK_IN_SALES.SAVE_API;
        if(bill.discountCatgeory == undefined){
          bill.discountCatgeory = bill.discountCatgeory;
        }
        else{
          bill.discountCatgeory = bill.discountCatgeory.id;
        }

         var tempAge  = (bill.isWalkInSales && !!bill.age) ? bill.age :null ;
		  var myArray = [];
		  var AgeF = null;
		  if(tempAge != null){
			  myArray = tempAge.split("Y");
			  AgeF = myArray[0];
		  }
		  

        var data = {
          "tempBillId": !!bill.id ? bill.id : undefined,
          "tempPatientId":self.model.tempPatientId,		  
          "screenId": 1,
          "prefixId": (bill.isWalkInSales && !!bill.prefix) ? bill.prefix.id : undefined,
          "patientId": (self.model.patientId != undefined && self.model.patientId != null) ? self.model.patientId : undefined,
          "admissionId":(self.model.walkInSale.isWalkInSales==false || self.model.walkInSale.isWalkInSales== undefined)? $scope.admissionId :undefined, 
          "encounterId":(self.model.walkInSale.isWalkInSales==false || self.model.walkInSale.isWalkInSales== undefined)? (($scope.encounterId == undefined ||  $scope.encounterId== null || $scope.encounterId==0 ) ? null: $scope.encounterId ): undefined ,	  
         // "admissionId": $scope.admissionId,
         // "encounterId": ($scope.encounterId == undefined ||
			// $scope.encounterId== null || $scope.encounterId==0 ) ? null:
			// $scope.encounterId ,
           "visitTypeId":(self.model.walkInSale.isWalkInSales==false || self.model.walkInSale.isWalkInSales== undefined)? $scope.visitTypeId :undefined, 
         // "visitTypeId": $scope.visitTypeId,
          "patientName": (bill.isWalkInSales && !!bill.patientName) ? bill.patientName : undefined,
          "patientAddress": (bill.isWalkInSales && !!bill.patientAddress) ? bill.patientAddress : undefined,
          "mobileNumber": (bill.isWalkInSales && !!bill.mobileNumber) ? bill.mobileNumber : undefined,
          "patientAge": AgeF,
          "patientGenderId": (bill.isWalkInSales && !!bill.patientGender) ? bill.patientGender.id : undefined,
          "doctorId": (!bill.isWalkInSales && !!bill.doctorId) ? bill.doctorId : $scope.commonDoctorId,
          "doctorName": !!bill.doctorName ? bill.doctorName : undefined,
          "doctorAddress": !!bill.doctorAddress ? bill.doctorAddress : undefined,
          "discountTypeId": !!bill.discountType ? bill.discountType : undefined,
          "discountCategoryId": !!bill.discountCatgeory ? bill.discountCatgeory : undefined,
          "discount": !!bill.discount ? bill.discount : 0,
          "totalAmount": !!bill.totalAmount ? Number((bill.totalAmount).toFixed(2)) : 0,
          "totalDiscountAmount": !!bill.totalDiscountAmount ? Number((bill.totalDiscountAmount).toFixed(2)) : 0,
          "netAmount": !!bill.netAmount ? parseFloat(bill.netAmount).toFixed(2) : 0,
          "roundOff": !!bill.roundOff ? Number((bill.roundOff).toFixed(2)) : 0,
          "taxAmount": !!bill.totalTaxAmount ? Number((bill.totalTaxAmount).toFixed(2)) : 0,
         // "saleTypeId":
			// ($scope.visitTypeId==null)?3:$scope.saleTypeIdAgainstEncounter,
          "saleTypeId": (bill.isWalkInSales==true || (($scope.visitTypeId==2 || $scope.visitTypeId==3 || $scope.visitTypeId==4) && self.model.cashSettlement ))?3:$scope.saleTypeIdAgainstEncounter,
          // "statusId": SetStatus('Completed'),
          "totalSelfPayable":$scope.totalSelfPayable,
      	  "totalCreditPayable":$scope.totalCreditPayable,
          "items": [],          
          "paymentEntitlementId":$scope.paymentEntitleMentList.length==1 ? 1 : null,
          "remark": (bill.remark != undefined &&  bill.remark != null ) ? bill.remark : self.model.remark,
          "isContractChanged":$scope.isContractChanged,
          "isPayeeUpdated":$scope.isPayeeUpdated,
          "consume" :false,
          "walkInSales":true,
          "unitId":$scope.unitId,
          "organizationId":$scope.organizationId,
          "userId":$scope.userId,
          "storeId":$cookies.getObject('cookieObject').storeId,
          "addToBill" : addToBill,
          "cashSettlement":  self.model.cashSettlement,
          "searchPatient":$scope.searchPatient,
          "doctorType":(self.model.walkInSale.doctorType!=null) ? self.model.walkInSale.doctorType : undefined,
          "atCopToSave":$scope.atCopToSave,
          "isConsolidatedFlag":false 
        }

        
        
    	var checkLasa = false;
        angular.forEach(bill.items, function (item, key) 
        {
            if(item.isLasa !== undefined && item.isLasa !== null && !item.isLasa == true){
                checkLasa = true;
            }
          var billItem = {
            "statusId": item.statusId,
            "batchId": item.selectedBatch.batchId,
            "uomTypeId": item.selectedUom.uomTypeId,
            "uomUnitId": item.selectedUom.uomUnitId,
            "rate": item.rate ? Number((item.rate).toFixed(2)) : 0.00,
            "quantity": Number(item.quantity),
            "leaseRate": item.selectedBatch.sellingPrice ? Number((item.selectedBatch.sellingPrice).toFixed(2)) : 0.00,
            "leaseQuantity": item.leaseQuantity,
            "totalAmount": item.itemtotalAmount ? Number((item.itemtotalAmount).toFixed(2)) : 0.00,
            "discountAmount": item.itemDiscount ? Number((item.itemDiscount).toFixed(2)) : 0.00,
            "unitDiscountAmount": item.unitDiscountAmount ? Number((item.unitDiscountAmount).toFixed(2)) : 0.00,
            "netAmount": item.netAmount ? Number((item.netAmount).toFixed(2)) : 0.00,
            "taxId": item.taxId,
            "itemId": item.itemId,
            "taxPercentage": item.taxPercent ? Number((item.taxPercent).toFixed(2)) : 0.00,
            "taxAmount": (!isNaN(item.taxAmount) && item.taxAmount) ? Number((item.taxAmount).toFixed(2)) : 0.00,
            "creditPayable": item.creditPayable ? Number((item.creditPayable).toFixed(2)) : 0.00,
            "selfPayable": item.selfPayable ? Number((item.selfPayable).toFixed(2)) : 0.00,
            "payeeId":item.payeeId,
            "productCategoryId": item.productCategoryId,
            "packageConsumptionAmt" : item.packageConsumptionAmt,
            "saleDetailPayeeMapperDtoList":item.saleDetailPayeeMapperDtoList,
            "saleDetailPackageMapperDtoList": item.saleDetailPackageMapperDtoList,
            "consessionPercentage": item.patientClassPercentage,
            "consessionAmount": item.patientClassAmount,
            "unitConsessionAmount": item.unitPatientClassAmount.toFixed(2),
            "batchAllocationId":item.allocationId
          }

          if(item.isInstruction!=undefined && item.isInstruction == 'Y'){
        	  billItem.instructionDesc = item.instructionDesc;
          }else{
        	  billItem.instructionId = item.instructionId;
          }
          $scope.totalSelfPayable = parseFloat($scope.totalSelfPayable) + parseFloat(item.selfPayable);
          $scope.totalCreditPayable = parseFloat($scope.totalCreditPayable) + parseFloat(item.creditPayable);
          data.items.push(billItem);
        });

        if(checkLasa){
            checkLasa = false;
            growl.warning("List contains LASA drugs!",{
              title : WARNING_MSG
            });
        }
        data.totalSelfPayable = parseFloat($scope.totalSelfPayable).toFixed(2);
        data.totalCreditPayable =  parseFloat($scope.totalCreditPayable).toFixed(2);
        
        if(($scope.cashAmountLimit != undefined && $scope.cashAmountLimit !=null) && parseFloat($scope.totalSelfPayable) > $scope.cashAmountLimit && self.model.cashSettlement){
			growl.warning("System can not accept more than "+$scope.cashAmountLimit +" in cash amount",{
				title : WARNING_MSG
			});
			return false;
		}
        
        
        data.items.forEach(function (key) 
        {
          var itemStatusList = [];
          itemStatusList.push(key.statusId);

          if (itemStatusList.indexOf(18) !== -1) { // partially Completed
													// status id
            data.statusId = 18; // partially Completed stutus
          } else {
            data.statusId = 4; // Completed stutus
          }
        });
        $rootScope.startLoader();
        if($scope.saveBtn == false){
          
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            $rootScope.stopLoader();
            $scope.saveBtn = true;
            $scope.saveWalkInSaleFlag = true;	
            $scope.checkCapWithPackage = false;
            self.model.walkInSalesBillId = response.data.id;
            $scope.patientIdDoc=null;
            angular.forEach(response.data.listObject, function(e){
              $scope.idsToAddServicesInExistingBill.push(e);
            });
            rePrintBillSlip(response.data);
            if(self.model.cashSettlement){
              saveCashSettlement();
            }
            else if($scope.cookieObjectskipPharmacySettlement == false && data.netAmount>0 && data.totalSelfPayable != 0 && addToBill==false){
              ConfirmSettlement();
            }
            	
            else 
            {
            	  $scope.saveWalkInSaleFlag = true;	
              growl.success('Walk-in sales Successfully saved');
            }
            // }
            ClearFields(bill);
            $scope.addBtnDisabled = false;
          },
          function (err) {
            $rootScope.stopLoader();
            $scope.saveWalkInSaleFlag = true;	
            $scope.checkCapWithPackage = false;
            if(err.data.summary != undefined && err.data.summary != null && err.data.summary == "Stock is not available"){
              angular.forEach(bill.items, function(b){
                b.flag = false;
              });
              angular.forEach(err.data.errors, function(e){
                angular.forEach(bill.items, function(d){
                  if(d.flag == false){
                    d.summary = '';
                    if(d.selectedBatch.batchNo == e.batchNo){
                      d.summary = "Enough stock not available";
                      // d.newStock = e.newStock;
                      d.flag = true;
                      var popup = angular.element('#confirmSave');
                      popup.modal('hide');
                    }	
                  }
                });
              });
            }
            else{
            	 $scope.checkCapWithPackage = false;
            	  $scope.saveWalkInSaleFlag = true;	
              if(err.data.summary != undefined && err.data.summary != null){
                growl.error('Stock not available for the some of the items.');
              }
              else{
                growl.error('Error while saving walk-in sales bill.');
              }
            }
            $scope.addBtnDisabled = false;
            $scope.saveBtn = true;
          });
        }
      }else{
    	  $scope.saveWalkInSaleFlag = true;	
      }
    }


    $scope.isWalkInSales = false;
  
    var WalkInSalesFlagStatus = function (bill, event) {
    	 $scope.availableDeposits = [];
    	 $scope.kitId = null;
    	 $scope.patientClassIdPopup = null;
		    $scope.patientClassCancelReasonIdPopup = null;
		    $scope.patientClassId =null;
      if(self.model.walkInSale.isWalkInSales == true){
        $('#registerPatient').hide();
        $scope.checkCapWithPackage = false;
        $scope.searchPatient = false;
      }
      else{      
        $('#registerPatient').show();
        $scope.searchPatient = true;
        $scope.isWalkInSales = false;
      }
      
      var obj = {
              itemCode: '',
              itemName: '',
              itemId: '',
              genericName: '',
              manufacturerName: '',
              batch: '',
              selectedBatch: '',
              uom: '',
              selectedUom: '',
              markUpDown: '',
              markUpDownPer: '',
              taxId: '',
              taxName: '',
              taxPercent: '',
              formulationType: '',
              stock: '',
              strength: '',
              productCategoryId: '',
              assetCategoryId: '',
              patientClassPercentage: '',
              patientClassAmount: '',
              saleDetailPayeeMapperDtoList: []
            };
            self.model.walkInSale.items = [];
            self.model.walkInSale.items.push(obj);
      
      $scope.initPaymentEntitlement();
      document.getElementById("id1").checked = true;
      if (!self.model.walkInSale.isWalkInSales) {
        self.model.patientId = undefined;
        self.model.admissionId = undefined;
        self.model.patientSearchFlag = true;
        self.model.isPatientNameValid = false;
        self.model.isPrefixValid = false;
        self.model.isPatientAddressValid = false;
        self.model.isPatientGenderNewValid = false;
        self.model.isAgeValid = false;
        self.model.isDoctorNameValid = false;
        self.model.isDoctorAddressValid = false;
        self.model.isPatientMobileNumberValid = false;
        self.model.walkInSale.prefix = self.model.prefixList[0];
        self.model.walkInSale.patientName = undefined;
        self.model.walkInSale.patientAddress = undefined;
        self.model.walkInSale.patientGender = self.model.patientGenderList[0];
        self.model.walkInSale.age = undefined;
        self.model.walkInSale.doctorName = undefined;
        self.model.walkInSale.doctorAddress = undefined;
        self.model.walkInSale.mobileNumber = undefined;
        self.model.visitNumber = '';
        $scope.patientId = null;
        $scope.walkInPatient = null;
        $scope.patientName = null;
        $scope.isValidated = false;
        // dinesh
      // $scope.initPaymentEntitlement();
        $scope.entitlementObj.paymentEntitlementTypeId = 1;
        $scope.entitlementObj.searchbyTypeId = 1;
        if($scope.entitlementObj.searchbyTypeId == 1){
          $('#itemsearch').show();
          $('#genericsearch').hide();
        }
		    $scope.patientSaleTypeId = 1;
		    $scope.patientSaleTypeIdFlag = true; 
      } else {
        self.model.patientSearchFlag = false;
        // dinesh
        self.model.walkInSale.prefix = self.model.prefixList[0];
        self.model.walkInSale.patientName = undefined;
        self.model.walkInSale.patientAddress = undefined;
        self.model.walkInSale.patientGender = self.model.patientGenderList[0];
        self.model.walkInSale.age = undefined;
        self.model.walkInSale.doctorName = undefined;
        self.model.walkInSale.doctorAddress = undefined;
        self.model.walkInSale.mobileNumber = undefined;
        self.model.visitNumber = '';
        self.model.patientId = null;
        self.model.admissionId = null;
        $scope.patientId = null;
        $scope.id = null;
        $scope.walkInPatient = null;
        $scope.patientName = null;
        $scope.entitlementObj.paymentEntitlementTypeId = 1;
        $scope.isValidated = false;
        $scope.encounterId = undefined;
        self.model.visitNumber = '';
        $scope.patientSaleTypeId = 3;
        $scope.patientSaleTypeIdFlag = false; 
        $scope.isWalkInSales = self.model.walkInSale.isWalkInSales;
        $scope.activePackageList = [];
        if(event.which == 1){
          document.getElementById('idPrefix').focus();
          // event.preventDefault();
          // $scope.focusInput=true;
        }
      }
    }

    var ValidateBatch = function (item) {
      var isValid = true;
      if(item.selectedBatch){
        if (!item.selectedBatch.batchId && item.selectedBatch.batchId>0) {
          item['isBatchValid'] = true;
          $scope.saveBtn = true;
          growl.error("Please Select Batch");
          isValid = false;
        }
      }
      return isValid;
    }

    var ValidateUOM = function (item) {
      var isValid = true;
      if (!item.selectedUom.uomUnitId) {
        item['isUOMValid'] = true;
        growl.error("Please Select UOM");
        isValid = false;
      }
      return isValid;
    }

    var ValidateQuantity = function (item) {
      var isValid = true;
      if (isNaN(parseFloat(item.quantity))) {
        item['isQuantityValid'] = true;
        growl.error("Please enter quantity");
        isValid = false;
        $scope.saveBtn = true;
      } else if (parseInt(item.quantity) == 0) {
        item['isQuantityValid'] = true;
        isValid = false;
        $scope.saveBtn = true;
        growl.error('Quantity cannot be zero');
      } else {
        item.quantity = parseInt(item.quantity);
      }

      if(item.newStock != 0){
          if(item.quantity > item.newStock){
          isValid = false;
          $scope.saveBtn = true;
         /* growl.error("Quantity is greater than available stock") */
          growl.error("No enough stock quantity")
          item.quantity = "";
          }
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

    var ValidateUniqueBatchAndUom = function (item) {
      var isValid = true;

      var items = self.model.walkInSale.items.filter(function (detail) {
        return detail.itemId == item.itemId && item.selectedBatch && item.selectedUom && item.selectedUom.uomTypeId > 0 && item.selectedBatch.batchId > 0 && detail.selectedBatch.batchId == item.selectedBatch.batchId && detail.selectedUom.uomTypeId == item.selectedUom.uomTypeId;
      });

      if (items.length > 1) {
        isValid = false;
        item['isBatchValid'] = true;
        item['isUOMValid'] = true;
        $scope.saveBtn = true;
        growl.error('There should not be same batch and same UOM for same item');
      } else {
        // item['isBatchValid'] = false;
        // item['isUOMValid'] = false;
      }
      return isValid;
    }
    
    var ValidateSponsor = function (bill) {
        var isValid = true;

        if (!bill.isWalkInSales) {
          if (!self.model.patientId) {
            isValid = false;
            self.model.isPatientIdValid = true;
            growl.error('Please select patient details');
          }
        }

        if (!!bill.isWalkInSales) {
          if (!bill.prefix.id) {
            isValid = false;
            self.model.isPrefixValid = true;
            growl.error('Please select prefix');
          }
          if (!bill.patientName) {
            isValid = false;
            self.model.isPatientNameValid = true;
            growl.error('Please select patient name');
          }
          // if (!bill.patientAddress) {
          // isValid = false;
          // self.model.isPatientAddressValid = true;
          // }
          // if (!bill.patientGender.id) {
          // isValid = false;
          // self.model.isPatientGenderNewValid = true;
          // }
          // if (!bill.age || bill.isAgeValid) {
          // isValid = false;
          // self.model.isAgeValid = true;
          // }
          // if (!bill.mobileNumber) {
          // isValid = false;
          // self.model.isPatientMobileNumberValid = true;
          // }

          // if (!bill.doctorName) {
          // isValid = false;
          // self.model.isDoctorNameValid = true;
          // }
        }
        return isValid;
    }

    var ValidateBill = function (bill) {
      var isValid = true;

      if (!bill.isWalkInSales) {
        if (!self.model.patientId) {
          isValid = false;
          self.model.isPatientIdValid = true;
          growl.error('Please select patient details');
          $scope.saveBtn = true;
          $scope.addBtnDisabled = false;
        }
      }

      if (!!bill.isWalkInSales) {
        if (!bill.prefix.id) {
          isValid = false;
          self.model.isPrefixValid = true;
        }
        if (!bill.patientName) {
          isValid = false;
          self.model.isPatientNameValid = true;
        }
   
      }
      
      if (self.model.walkInSale.discount) {
      
        if (!self.model.isDiscountApplied) {
          isValid = false;
          self.model.isDiscountAppliedValid = true;
          growl.error('Please apply discount on the bill items');
          $scope.saveBtn = true;
          $scope.addBtnDisabled = false;
        }

        if (self.model.discountTypeFlag == true) {
          isValid = false;
          self.model.isDiscountAppliedValid = true;
          growl.error('Please apply discount on the bill items');
          $scope.saveBtn = true;
          $scope.addBtnDisabled = false;
        }

        if (self.model.isAppliedDiscountValid == true) {
          isValid = false;
          growl.error('Please apply discount on the bill items');
          $scope.saveBtn = true;
          $scope.addBtnDisabled = false;
        }
      }

      if(self.model.walkInSale.discountCatgeory != undefined && self.model.walkInSale.discountCatgeory != null){
    	  if (self.model.walkInSale.discountCatgeory.id) {
              var found = self.model.walkInSale.items.find(item => item.discountPerItem == 0)
              if (!self.model.walkInSale.discount && found!=undefined) {
                  isValid = false;
                  self.model.isDiscountAmountValid = true;
                  growl.error('Please enter discount amount');
                  $scope.btnF = false;
              }
              if (!self.model.walkInSale.discount) {
                  isValid = false;
                  self.model.isDiscountAmountValid = true;
                  growl.error('Please enter discount amount');
                  $scope.btnF = false;
              }
          }  
      }

      if (!bill.items || bill.items.length <= 0) {
        isValid = false;
        growl.error("Please add items in the bill");
        $scope.saveBtn = true;
        $scope.addBtnDisabled = false;
      } else {
        bill.items.forEach(function (billitem) {
//          if (billitem.isOTC) {
//            if (!bill.doctorName) {
//              isValid = false;
//              self.model.isDoctorNameValid = true;
//            }
//            if (!bill.doctorAddress) {
//              isValid = false;
//              self.model.isDoctorAddressValid = true;
//            }
//          }
          
          
          billitem['isItemValid'] = false;
          billitem['isBatchValid'] = false;
          billitem['isUOMValid'] = false;
          billitem['isQuantityValid'] = false;
          if ((!billitem.selectedBatch.batchId || !billitem.selectedUom.uomUnitId || !billitem.quantity) && billitem.selectedBatch.batchId>0) {
            billitem['isItemValid'] = true;
            isValid = false;
          }
          if (!billitem.selectedBatch.batchId && billitem.selectedBatch.batchId>0) {
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
          }

          if (!ValidateUniqueBatchAndUom(billitem)) {
            isValid = false;
          }
          if (!ValidateBatch(billitem)) {
            isValid = false;
          }
          if (!ValidateUOM(billitem)) {
            isValid = false;
          }
          if (!ValidateQuantity(billitem)) {
            isValid = false;
          }
           
            if(initialStore==JSON.parse(sessionStorage.getItem('selectedTopStore')))
            {
               
            }

        });
        return isValid;
      }
    }

    $scope.clearAllVariables = function(){
    	
    	$scope.validatTextFieldFlag = true;
        $scope.listPaymentDetails = [];
        $scope.calculateTotalDueRefAmount();
    	ClearFields(self.model.walkInSale);
    	$scope.saleTypeIdAgainstEncounter = 3;
        $scope.patientSaleTypeIdFlag = false; 
    }
    
    var ClearFields = function (bill) {
      $scope.atCop = false;
      self.model.patientSearchObj.uhIdNumber = '';
      self.model.patientSearchObj.patientName = '';
      self.model.walkInSale.isWalkInSales = false;
      self.model.isOTC = false;
      bill.patientId = null;
      bill.prefix = self.model.prefixList[0];
      bill.patientName = null;
      bill.patientAddress = null;
      bill.age = null;
      bill.patientGender = self.model.patientGenderList[0];
      bill.doctorName = null;
      bill.doctorAddress = null;
      self.model.search = null;
      self.model.searchBatch = null;
      bill.Id = "1" + (new Date()).getTime();
      bill.discountTypeId = null;
      bill.discountCatgeory = self.model.discountCategoryList[0];
      bill.discount = null;
      bill.totalAmount = null;
      bill.totalDiscountAmount = null;
      bill.taxAmount = null;
      bill.netAmount = null;
      
      bill.mobileNumber = null;
      
      // Ganesh
      // $scope.isValidated = false;
      $scope.totalSelfPayable = 0;
      $scope.totalCreditPayable = 0;
      
      self.model.patientSearchData = [];
      self.model.patientId = null;
      $scope.patientIdDoc = null;
      self.model.admissionId = null;
      // self.model.patientSearchObj = null;
      
      // Ganesh
      $scope.encounterId = null;
      $scope.admissionId = null;
      $scope.patientId = null;
      self.model.remark = '';
      $scope.initPaymentEntitlement();
      bill.items = [];
      self.model.visitNumbers = [];
       var obj = {
        itemCode: '',
        itemName: '',
        itemId: '',
        genericName: '',
        manufacturerName: '',
        batch: '',
        selectedBatch: '',
        uom: '',
        selectedUom: '',
        markUpDown: '',
        markUpDownPer: '',
        taxId: '',
        taxName: '',
        taxPercent: '',
        formulationType: '',
        stock: '',
        strength: '',
        productCategoryId: '',
        assetCategoryId: '',
        patientClassPercentage: '',
        patientClassAmount: '',
        saleDetailPayeeMapperDtoList: []
      };
      self.model.walkInSale = {
        "id": self.model.walkInSale.id,
        "items" : []
      }
      self.model.walkInSale.items.push(obj);
    }

    var ClearBill = function (bill) {
      self.model.walkInSale.prefix = self.model.prefixList[0];
      self.model.walkInSale.patientName = undefined;
      self.model.walkInSale.patientAddress = undefined;
      self.model.walkInSale.patientGender = self.model.patientGenderList[0];
      self.model.walkInSale.age = undefined;
      self.model.walkInSale.doctorName = undefined;
      self.model.walkInSale.doctorAddress = undefined;
      self.model.walkInSale.mobileNumber = undefined;
      self.model.search = undefined;
      self.model.searchBatch = undefined;
      self.model.barcode = undefined;
      self.model.walkInSale.discount = undefined;
      self.model.walkInSale.discountCatgeory = self.model.discountCategoryList[0];
      self.model.isPrefixValid = false;
      self.model.isPatientNameValid = false;
      self.model.isPatientAddressValid = false;
      self.model.isPatientGenderNewValid = false;
      self.model.isAgeValid = false;
      self.model.isDoctorNameValid = false;
      self.model.isDoctorAddressValid = false;
      self.model.isPatientMobileNumberValid = false;
      self.model.patientSearchData = [];
      self.model.patientId = null;
      $scope.patientIdDoc = null;
      self.model.admissionId = null;
      self.model.patientSearchObj = null;
      self.model.visitNumber = '';
      // $scope.isValidated = false;
      
      self.model.walkInSale.mobileNumber = undefined;

  // if (bill.items.length > 0) {
    	  
  // $scope.allocationIdItemWise="";
  // $scope.callAPI=false;
  // angular.forEach(bill.items, function (item, key)
  // {
  // if(item.allocationIds!=undefined)
  // {
  // $scope.callAPI=true;
  // $scope.allocationIdItemWise=$scope.allocationIdItemWise+item.allocationIds+","
  // }


  // });
  // if ( $scope.callAPI == true) {

  // var URI = CONSTANTS.PHARMACY.OP.WALK_IN_SALES.ALLOCATE_API +
	// $scope.unitId + "/"+$scope.organizationId
	// +"/"+$cookies.getObject('cookieObject').storeId +"/" +$scope.userId+"/"
	// +bill.id +"/"+ $scope.allocationIdItemWise;
  // $rootScope.startLoader();
  // GenericService.serviceAction("DELETE", URI, {}).then(
  // function (response) {
  // if (!!response.data) {
  // bill.items = [];
  // }
  // $rootScope.stopLoader();
  // growl.success('Bill cleared successfully');
  // },
  // function (ex) {

  // $rootScope.stopLoader();
  // growl.error('Error while clearing the bill');
  // });
  // }
  // }
      var obj = {
        itemCode: '',
        itemName: '',
        itemId: '',
        genericName: '',
        manufacturerName: '',
        batch: '',
        selectedBatch: '',
        uom: '',
        selectedUom: '',
        markUpDown: '',
        markUpDownPer: '',
        taxId: '',
        taxName: '',
        taxPercent: '',
        formulationType: '',
        stock: '',
        strength: '',
        productCategoryId: '',
        assetCategoryId: '',
        patientClassPercentage: '',
        patientClassAmount: '',
        saleDetailPayeeMapperDtoList: []
      };
      self.model.walkInSale = {
        "id": self.model.walkInSale.id,
        "items" : []
      }
      self.model.walkInSale.items.push(obj);
    }

    var SearchBatch = function (search) {
      if (search.length < 1) return;

      if ((!self.model.prevBatchSearch || self.model.prevBatchSearch != search) && (search.length == 1)) {
        self.model.prevBatchSearch = search;
        var URI = CONSTANTS.GLOBAL.WALK_IN_SALES_BATCH_API + $cookies.getObject('cookieObject').storeId  + "/" + $scope.unitId +  "/"  + self.model.isOTC + "/" + search;
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

    var NoBatchFound = function (search) {
      var item = {
        "itemFound": false,
        "details": "No batch found with batch number " + search
      };
      self.model.batches = [item];
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
//      if (!barcode && event.which === enterCode) {
//        barcode = self.model.barcode;
//      }
     
      var data = {
    		  
       "priPrintedBarcodeNo":self.model.barcode,
        "unitId": $scope.unitId,
        "items":[]
      };
      if(self.model.walkInSale.items.length == 1){
      if(self.model.walkInSale.items[0].itemId=="")
    	  {
    	  self.model.walkInSale.items.splice(0, 1);
    	  }
      }
      if (!!barcode) {
	      var URI = CONSTANTS.PROCUREMENT.QUOTATION.GET_BATCHES_API+"/prePrintedBarcode/getBarcodes";
        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(function (response) {        
 	        $rootScope.stopLoader();
 	        var flag=false;
 	        angular.forEach(response.data, function (value, key) {
 	          if(response.data[0].isManualBarcodeRequired==true )
 		        {
 	        	  if(value.prePrintedBarcode==self.model.barcode){
 	        		 flag=true;
 	                self.model.barcode="";
 	               $scope.barcodeScanned= true;
 	               AllocateBatch(self.model.walkInSale, value.batchId);  
 	        	  }
 	        	  else{
 	        		 flag=true;
 	 				growl.error( "Enter Valid Barcode..!",
 	 				{
 	 					title: "Error"
 	 				});
 	        	  }
 		          
 		        }
 	         else {
 				flag=true;
 				growl.error( "Enter Valid Barcode..!",
 				{
 					title: "Error"
 				});
 			}
 	      
 	        });
 	        if(flag==false)
 		      {
 		        if (!!barcode) {
// var itemId = barcode.split(' ')[0];
// var batchId = barcode.split(' ')[1];
 		        	var batchId = barcode;
 		        	self.model.barcode = ""
 		        	$scope.barcodeScanned= true;
 			        AllocateBatch(self.model.walkInSale, batchId);
 	            event.stopPropagation();
 	            event.preventDefault();
 	            SetFocus();
 	          }
 		      }
        });
      }
    }

    var AddBatchToBill = function (bill, item, model, label) {
    	
    	AllocateBatch(bill, item.id, item.batchId);
    	
    	/*
		 * if (!!item.id && !!item.batchId) { var stopData = { "itemIds":
		 * [item.id], "storeId":$cookies.getObject('cookieObject').storeId };
		 * stopTransactionService.GetstopTransactionData(stopData).then(function(responseData){
		 * var StopTransactionData = responseData; if(StopTransactionData.length >
		 * 0 ){ var startdate =
		 * $filter('date')(StopTransactionData[0].startDate, "dd-MM-yyyy"); var
		 * stopdate= $filter('date')(StopTransactionData[0].stopDate,
		 * "dd-MM-yyyy"); $rootScope.stopLoader(); var stopMessage = "Stop
		 * Transation is in Process for this Item(s) <br/>Start Date : "+
		 * startdate +" <br/> Stop Date: "+ stopdate +" <br/> Days: "+
		 * StopTransactionData[0].days +" <br/> Hours: "+
		 * StopTransactionData[0].hours +" <br/> Minutes: "+
		 * StopTransactionData[0].minutes +" "; growl.warning(stopMessage,
		 * {onclose: function() { AllocateBatch(bill, item.id, item.batchId);
		 * },}); } else{ AllocateBatch(bill, item.id, item.batchId); } }); }
		 */}

    var AllocateBatch = function (bill, batchId) {
      if(batchId != undefined){
        var URI = CONSTANTS.PHARMACY.OP.WALK_IN_SALES.ALLOCATE_BATCH_API + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId + '/'+$cookies.getObject('cookieObject').storeId + '/' + $scope.entitlementObj.paymentEntitlementTypeId + '/' + bill.id;
        var data = {
// "itemId": itemId,
          "batchId": batchId,
          "barcodeScanned": $scope.barcodeScanned
        };

        $rootScope.startLoader();
        GenericService.serviceAction("PUT", URI, data).then(
          function (response) {
            if (!!response.data) {
              var item = bill.items.find(function (obj) {
                return obj.allocationId == response.data.allocationId;
              });

              // if (!!item) {
              // item.quantity = parseInt(item.quantity) + 1;
              // CalculateQuantity(item, item.selectedUom, item.uom);
              // CalculateAmount(item);
              // }
              // else {
              angular.forEach(response.data.batch, function (value, key) {
                if(!!this.markUpDownPer){
                  var markUpDown = commonDetailService.fixedDecimal((value.sellingPrice * (this.markUpDownPer / 100)));
  
                  // if (this.markUpDown == 1) {
                  // value.sellingPrice =
                  // commonDetailService.fixedDecimal(value.sellingPrice +
                  // markUpDown);
                        // } else if (this.markUpDown == 0) {
                        // value.sellingPrice =
                  // commonDetailService.fixedDecimal(value.sellingPrice -
                  // markUpDown);
                  // }
                }
              }, response.data);

              var batch = response.data.batch.find(function (obj) {
                return obj.batchId == batchId;
              });

              item = {
                itemDisabled:false,
                disabled:false,
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
                productCategoryId: response.data.productCategoryId,
                strength: response.data.strength,
                batch: [{
                  batchDetails: "Select Batch"
                }].concat(response.data.batch),
                uom: [{
                  uomDetails: "Select UOM",
                }],
                taxId: response.data.taxId,
                taxName: response.data.taxName,
                taxPercent: response.data.taxPercent,
                isOTC: response.data.isOTC,
                patientClassPercentage:  response.data.patientClassPercentage,
                storageUnitName: response.data.storageUnitName,
                isThreeUOMApplicable: response.data.isThreeUOMApplicable
              };
              item.colorCode=response.data.colorCode;
              if (!!batch) {
                item.selectedBatch = batch;
                item.uom = item.uom.concat(batch.uom);
                item.selectedUom = batch.uom.find(function (obj) {
                  return obj.opUom;
                });
                item.quantity = 1;
              } else {
                // item.selectedBatch = item.batch[0];
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
              // CalculateRate(item, item.selectedUom, item.uom);
              Calculate(item, self.model.walkInSale);
              if($scope.handleDoubleClick==false)
            	  {
            	  item.totalStock=0;
                  angular.forEach(item.batch, function (batch, key) {
                	  item.totalStock+=batch.availableStockQuantity!=undefined && batch.availableStockQuantity!=null ? batch.availableStockQuantity:0;
                  })
                  bill.items.splice(0, 0, item)  
                  $scope.handleDoubleClick=true;
                  }
              var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.AVAILABLE_QTY_API  + $scope.unitId + "/"+ $cookies.getObject('cookieObject').storeId +'/'+itemId+'/'+ batchId+'/'+item.selectedUom.uomTypeId;
              GenericService.serviceAction("GET", URI, {}).then(
                function (response) 
                {
                  item.stock=response.data.availableqty;
                  item.newStock =response.data.availableqty;
                  item.selectedBatch.availableStockQuantity = response.data.availableqty;
                  ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);
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
            }
            self.model.barcode = "";
            $rootScope.stopLoader();
          },
          function (ex) {
            growl.error(ex.data.message);
            $rootScope.stopLoader();
          });
        }
      }

    $scope.pinpadAmounFlag = false;
    var GetBillDetails = function () {
    	$scope.skipMpesaValidation = false;
    	$scope.pinpadAmounFlag = false;
      var URI = CONSTANTS.PHARMACY.OP.BILL_LIST.DETAILS_API + $scope.unitId + "/"+ $scope.selfPayeeId + "/" + self.model.walkInSalesBillId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          $rootScope.stopLoader();
          self.model.billDetailList = response.data;
          $scope.tempPatId=response.data.patientId;
          $scope.selectedUhid = response.data.uhid;
          settlementClick();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }
    var GetBillDetailsForCash = function () {
    	$scope.skipMpesaValidation = false;
    	$scope.pinpadAmounFlag = false;
      var URI = CONSTANTS.PHARMACY.OP.BILL_LIST.DETAILS_API + $scope.unitId + "/"+ $scope.selfPayeeId + "/" + self.model.walkInSalesBillId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
        	if(response.status==200)
        		{
        		  self.model.billDetailList = response.data;
                  $scope.selectedUhid = response.data.uhid;
                  $scope.billDetails();
                  $scope.calculateTotalDueRefAmount();
                  $scope.addPaymentDetails();
                  $scope.settleBill();
                  $rootScope.stopLoader();
                  self.model.cashSettlement = false;
        		}
        	else if(response.status!=200)
        		{
        		 $rootScope.stopLoader();
        		}
        
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }
    
    
    
    var ConfirmSettlement = function () {
      var popup = angular.element('#confirmSettlement');
      popup.modal('show');
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
// $scope.setFirstTabLevel(1);
    // if($scope.visitTypeId == null || $scope.visitTypeId == 1)
    // {
	    	 var popup = angular.element('#collectPaymentDetails');
	         $scope.billDetails();
	         $scope.calculateTotalDueRefAmount();
	         popup.modal('show');
    	// }
    }

    var NavigateToBillList = function () {
      document.getElementById('popupClose').click();
      console.log(" self.model.walkInSalesBillId", self.model.walkInSalesBillId);
      var obj={
      	"saleTypeId":self.model.walkInSalesBillId
      }
      var serviceIdList =  [];
		angular.forEach($scope.idsToAddServicesInExistingBill,function(value,index){
			
				serviceIdList.push(value);
			
		});
		console.log('sdcsd',serviceIdList);
		if(serviceIdList.length > 0)
		{
			var URI = BASE_URL + ROOT_URL
			+ "/api/billing/post/bill/service";
  		GenericService.serviceAction("POST", URI, serviceIdList)
			.then(
					function(response) {
						if (response.data.status == "success") {
                          console.log("comming....................")
                           
						}
							
							
					});
		}
		 var URI = CONSTANTS.PHARMACY.OP.WALK_IN_SALES.SAVE_API+"/updateSaleId";
		 GenericService.serviceAction("POST", URI, obj).then(
		  function (response) {
		     if(response.data.status=="success")
		     {
		  	   setTimeout(function () {
		  	        $state.go('pharmacyBillList');
		  	      }, 1000);
		     }
		  }
		  );
      
    }

    $scope.initBillSearchObj = function () {
      // $scope.billSearch =
		// {fromDate:"",toDate:"",visitTypeId:"",billNo:"",visitNo:"",billStatusId:"",patientId:"",orgId:$scope.organizationId,unitId:$scope.unitId,isOtcReg:false};
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

      // check wether redirected from ipd charges
      // if (typeof ($rootScope.fromWhere) != "undefined" &&
		// $rootScope.fromWhere != "") {
      // var temp = $rootScope.fromWhere.split("#");
      // if (temp[0] == "ipdCharge") {
      // $scope.getPatientDetailsById(temp[2]);
      // $scope.billSearch.patientId = temp[2];
      // GetBillList($scope.billSearch);
      // }
      // } else {
      // GetBillList($scope.billSearch);
      // }
      
      $scope.corEmpVerificationFlag=false;
      var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/getUnitApplicationBillingConfigList";
      var obj = {
      			organizationId :$scope.organizationId,
      			unitId :$scope.unitId
      	    }
      BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
      	if(response.data.status == "success"){
      		$scope.unitConfigurationObject = response.data.listObject[0];
      		if($scope.unitConfigurationObject.isCorporateEmpVerificationReq=='Y'){
      			$scope.corEmpVerificationFlag=true;
      			$scope.enableDisabledApply=false;	
      			$scope.enableDisabledValidated=false;

      		}else{
      			$scope.corEmpVerificationFlag=false;
      			$scope.enableDisabledApply=true;	
      			$scope.enableDisabledValidated=false;

      		}
      	}
      });
    }

    $scope.initTotalAmounts = function () {
      $scope.totalPaymentAmount = parseFloat(0).toFixed(2);
      $scope.dueAmount = parseFloat(0).toFixed(2);
      $scope.refundAmount = parseFloat(0).toFixed(2);
      $scope.totalAmountSummary = parseFloat(0).toFixed(2);
      $scope.totalConcessionSummary = parseFloat(0).toFixed(2);
      $scope.netPayableSummary = parseFloat(0).toFixed(2);
      $scope.totalPayingAmount = parseFloat(0).toFixed(2);
      
      // Ganesh
      $scope.totalSelfPayable = parseFloat(0).toFixed(2);
      $scope.totalCreditPayable = parseFloat(0).toFixed(2);
    }
    
    
    
	/*
	 * *************************************loylti card Integration
	 * start******************************************
	 */
	
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
	
	
	/*
	 * *************************************loylti card Integration
	 * END******************************************
	 */
    
    
    
    
	
    $scope.selectedPatientId="";
    $scope.patientType = null;
    $scope.billDetails = function () 
    {
      $scope.payeeDueAmtFinal = 0;
      $scope.payeeDueAmtStatic = 0;
      let bill = self.model.billDetailList;
      $scope.status = bill.status;
      // console.log("BILL", bill);
      $scope.billDisabled = bill.billStatusId == 3 ? true : false;
      $scope.billId = bill.id;
      $scope.totalAmount = bill.netAmount;
      $scope.selectedUhid = bill.uhid;
      $scope.selectedPatientId = bill.patientId;
      $scope.validatTextFieldFlag = true;
      if(bill.patientId !=undefined){
    	  loyaltiCardCallFactory.getLoyaltyCardList(bill.patientId,$scope);
    	  
      }
      if( $scope.isWalkInSales == true &&  $scope.patientSaleTypeIdFlag == false){
    	  $scope.patientSaleTypeId = 3;
      }
      else{
    	  $scope.patientSaleTypeId = 2;
      }
      
      $scope.transferToActiveDeposit = 'N'
    	  $scope.excessAmount = parseFloat(0).toFixed(2);
    	  $scope.send=false;
    	  $scope.resend=false;
      /*
		 * $scope.dueAmount = bill.balanceAmt; $scope.oldDueAmount =
		 * bill.balanceAmt; $scope.billDueAmount = bill.balanceAmt;
		 * $scope.payeeDueAmtFinal = bill.balanceAmt; $scope.payeeDueAmtStatic =
		 * bill.balanceAmt;
		 */
      
     /*
		 * $scope.dueAmount = bill.selfDueAmount; $scope.oldDueAmount =
		 * bill.selfDueAmount; $scope.billDueAmount = bill.selfDueAmount;
		 * $scope.payeeDueAmtFinal = bill.selfDueAmount;
		 * $scope.payeeDueAmtStatic = bill.selfDueAmount
		 */
    	  
    	  $scope.dueAmount = bill.totalSelfPayable ? bill.totalSelfPayable : bill.balanceAmt;
          $scope.oldDueAmount =bill.totalSelfPayable ? bill.totalSelfPayable : bill.balanceAmt;
          $scope.billDueAmount = bill.totalSelfPayable ? bill.totalSelfPayable : bill.balanceAmt;
          $scope.payeeDueAmtFinal =bill.totalSelfPayable ? bill.totalSelfPayable : bill.balanceAmt;
          $scope.payeeDueAmtStatic =bill.totalSelfPayable ? bill.totalSelfPayable : bill.balanceAmt;
      
      $scope.currentBill = bill;
      $scope.admissionId = (bill.visitTypeId == 2||bill.visitTypeId==3||bill.visitTypeId==3) ? bill.visitId : null;
      $scope.isFinancialClearance = false;
      $('#isFinancialClearance').prop('checked', false);
      // Vivek added
      $scope.encounterId = bill.visitId;
      $scope.appointmentId = bill.appointmentId;
      // End Vivek Added
      // get payer details

      var URI = CONSTANTS.ROOT_URL + '/api/pharmacy/sales/bills/' +  $scope.unitId + "/"+ $scope.selfPayeeId + "/" + self.model.walkInSalesBillId;
      $rootScope.startLoader();
      BillingGenericService.serviceAction(METHOD_GET, URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function (response) {
        if (response.data.items.length > 0) {
          // $scope.payeeList = response.data.listObject;
          $scope.payeeList = response.data;
          // vivek added
          // $scope.billServicesList = response.data.object;
          $scope.patientType = response.data.saleTypeId;
          $scope.billServicesList = response.data.items;
          var totalAmtQuaBase = parseFloat(0);
          var totalAmtConcession = parseFloat(0);
          var totalAmtDiscount = parseFloat(0);
          var totalAmtGst = parseFloat(0);
          var totalNetPayableAmt = parseFloat(0);
          var totalSelfPayable = parseFloat(0);
          var totalCreditPayable = parseFloat(0);
          
          angular.forEach($scope.billServicesList, function (value, key) {
            // totalAmtQuaBase = totalAmtQuaBase +
			// parseFloat(value.grossAmount);
            totalAmtQuaBase = totalAmtQuaBase + parseFloat(value.totalAmount);
            // totalAmtConcession = totalAmtConcession +
			// parseFloat(value.concession);
            // totalAmtDiscount = totalAmtDiscount + parseFloat(value.discount);
            totalAmtDiscount = totalAmtDiscount + parseFloat(value.discount);
            // totalAmtGst = totalAmtGst + parseFloat(value.taxAmount);
            totalAmtGst = totalAmtGst + parseFloat(value.taxAmount);
            // totalNetPayableAmt = totalNetPayableAmt +
			// parseFloat(value.netAmt);
            totalNetPayableAmt = totalNetPayableAmt + parseFloat(value.netAmount);
            
            totalSelfPayable = totalSelfPayable + parseFloat(value.selfPayable);
            totalCreditPayable = totalCreditPayable + parseFloat(value.creditPayable);
            
          });

          $scope.totalAmtQuaBase = totalAmtQuaBase;
          // $scope.totalAmtConcession = totalAmtConcession;
          $scope.totalAmtDiscount = totalAmtDiscount;
          $scope.totalAmtGst = (totalAmtGst).toFixed(2);
          $scope.totalNetPayableAmt = totalNetPayableAmt;
          
          $scope.totalSelfPayable = totalSelfPayable;
          $scope.totalCreditPayable = totalCreditPayable;

       /*
		 * $scope.payeeDueAmtFinal = response.data.balanceAmt;
		 * $scope.payeeDueAmtStatic = response.data.balanceAmt;
		 */
          // vivek added
        }
        $rootScope.stopLoader();
      });

    }
    
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
			 $scope.availableDeposits[idx].remark = $scope.DepositObj.remark;
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
	

    // -----------------payment Collection----------------//
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
      5: "Mpesa",
      8: "E - Transfer",
     /* 6: "Loyalty Card" */
    }

     $scope.addBtnId=false;
 	$scope.pinpadAmounFlag =false;
    $scope.setPaymentModeId = function (paymentModeId) {
    	
    	if(paymentModeId==3 && $scope.isPinPadRequired =='Y'){
			$scope.addBtnId=true;
			// $scope.pinpadAmounFlag =true;
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
			
			// console.log("transactionTypesList",$scope.transactionTypesList);
		});
	}
	 
	 $scope.getTransactionTypes();
	 
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
    
  // Ganesh Start
	$scope.calculateTotalDueRefAmount = function()
	{
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
    
    
/*
 * $scope.calculateTotalDueRefAmount = function () { var amount = 0; var
 * payeeDueAmtFinal = $rootScope.parseFloatNum($scope.payeeDueAmtFinal);
 * angular.forEach($scope.listPaymentDetails, function (value, key) { amount =
 * amount + parseFloat(value.amount); }); var tempDueAmt =
 * $scope.payeeDueAmtStatic - amount - $scope.consumedAmt; var
 * temppayeeDueAmtFinal = $scope.payeeDueAmtStatic - amount -
 * $scope.consumedAmt; $scope.payeeDueAmtFinal = temppayeeDueAmtFinal >= 0 ?
 * temppayeeDueAmtFinal : 0;
 * 
 * $scope.payeeDueAmtFinal = parseFloat($scope.payeeDueAmtFinal).toFixed(2); var
 * df = (parseInt($scope.payeeDueAmtStatic) - (parseInt(amount) +
 * parseInt($scope.consumedAmt))); if (df > 0) { $scope.dueAmount = tempDueAmt >=
 * 0 ? tempDueAmt : 0; $scope.paymentDetails.cashAmount =
 * $rootScope.parseFloatNum($scope.dueAmount); } else { $scope.dueAmount =
 * parseInt($scope.billDueAmount) - parseInt($scope.payeeDueAmtStatic);
 * $scope.paymentDetails.cashAmount =
 * $rootScope.parseFloatNum($scope.dueAmount); }
 * 
 * //Ganesh $scope.paymentDetails.cashAmount =
 * parseFloat($scope.dueAmount).toFixed(2); $scope.paymentDetails.chequeAmount =
 * parseFloat($scope.dueAmount).toFixed(2); $scope.paymentDetails.credAmount =
 * parseFloat($scope.dueAmount).toFixed(2); $scope.eTransferObj.transferAmount=
 * parseFloat($scope.dueAmount).toFixed(2);
 * 
 * 
 * $scope.refundAmount = temppayeeDueAmtFinal >= 0 ? 0 :
 * Math.abs(temppayeeDueAmtFinal); $scope.amountCollected = amount; }
 */

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
		$('#uploadpanCardDetailswalkInSale').modal('hide');
	}
	
    $scope.settleBill = function () {
    	
    	  $scope.billDisabled=true;
          $scope.depositeSettleBtnFlag = true;
    	var collectedAmount = parseFloat(0).toFixed();
		angular.forEach($scope.listPaymentDetails,function(value,key){
			value.receivedFrom=$scope.listPaymentDetails.amountReceivedFrom!=undefined ? $scope.listPaymentDetails.amountReceivedFrom :'';
			if(value.paymentModeId == 1){
				collectedAmount = parseFloat(collectedAmount) + parseFloat(value.amount);
			}
		});
		
		if($scope.cashAmountValidation != null && collectedAmount >=$scope.cashAmountValidation && $scope.fileObj.file == null){
			$('#uploadpanCardDetailswalkInSale').modal('show');
				return false;
		}
		$('#uploadpanCardDetailswalkInSale').modal('hide');
    
      var billPayeeDetailsId = "";
      
      // Get Bank Name
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
          newArr.consumedAmount = ($scope.availableDeposits[i].consumedDeposit - $scope.availableDeposits[i].oldConsumedDeposit);
          newArr.createdBy = $scope.userId;
          $scope.finalAvailableDeposits.push(newArr);
        }
        i++;

      });


      // return false;
      var param = {
        billingMasterId: $scope.billId,
        patientId :$scope.selectedPatientId,
        orgnizationId:  $scope.organizationId,
        unitId:$scope.unitId,
        createdBy: $scope.userId ,
        amountData: (parseFloat($scope.amountCollected) + parseFloat($scope.consumedAmt)),
        paymentReceiptDetailsDtosList: $scope.listPaymentDetails,
        paymentDepositDetailsList: $scope.finalAvailableDeposits,
        cashSettlement: self.model.cashSettlement !=undefined ? self.model.cashSettlement :false,
        selfPayeeId:$scope.selfPayeeId,
        balanceAmount : self.model.billDetailList.balanceAmt,
      }
        

      if (($scope.finalAvailableDeposits.length == 0) && ($scope.listPaymentDetails.length == 0 || $scope.listPaymentDetails == 'undefined')) {
        growl.error("Please select at least one payment", {
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
      // var URI = BASE_URL + HOST + API + BILLING + "/payments";
      BillingGenericService.serviceAction(METHOD_PATCH, URI, param, NOTIFICATION_MSG_STATUS_TRUE).then(function (response) {

        if (response.data.message == "Sale Details saved successfully") {
          // collectPayment
          // billListDetails
        	
          if($scope.fileObj.file != null){
				$scope.uploadEncounterDoc($scope.billId);
		  }
          angular.element('#collectPaymentDetails').modal('hide');
          angular.element('#billListDetails').modal('hide');
         // $scope.billDetails($scope.currentBill);
          $scope.validatTextFieldFlag = true;
          $scope.listPaymentDetails = [];
          $scope.calculateTotalDueRefAmount();
          rePrintBillSlip(response.data);
          ClearFields(self.model.walkInSale);
          $scope.saleTypeIdAgainstEncounter = 3;
          $scope.patientSaleTypeIdFlag = false; 

         growl.success(response.data.message ,{    title : SUCCESS_MSG});

        }else{
        	growl.error(response.data.message, {
				title : 'error!!'
			});
        	angular.element('#collectPaymentDetails').modal('hide');
            angular.element('#billListDetails').modal('hide');
           // $scope.billDetails($scope.currentBill);
            $scope.validatTextFieldFlag = true;
            $scope.listPaymentDetails = [];
            $scope.calculateTotalDueRefAmount();
            ClearFields(self.model.walkInSale);
            $scope.saleTypeIdAgainstEncounter = 3;
            $scope.patientSaleTypeIdFlag = false; 
        }
      });
      $rootScope.stopLoader();
    }


    $scope.calculateDueAmount = function () {
      // calculate due amount
      var collectedAmount = parseFloat(0).toFixed();
      angular.forEach($scope.listPaymentDetails, function (value, key) {
        collectedAmount = parseFloat(collectedAmount) + parseFloat(value.amount);
      });
      $scope.dueAmount = $scope.deposit.depositAmount != '' && typeof ($scope.deposit.depositAmount) != 'undefined' ? parseFloat(parseFloat($scope.deposit.depositAmount) - collectedAmount).toFixed(2) : parseFloat(0).toFixed(2);
      $scope.paymentDetails.cashAmount = $scope.dueAmount;
      $scope.paymentDetails.chequeAmount = parseFloat($scope.dueAmount).toFixed(2);
      $scope.paymentDetails.credAmount = parseFloat($scope.dueAmount).toFixed(2);
      $scope.eTransferObj.transferAmount= parseFloat($scope.dueAmount).toFixed(2);
      $scope.paymentDetails.loyaltyCardAmt =  parseFloat($scope.dueAmount).toFixed(2);
    }

   
    
    
    $scope.addPaymentDetails = function(){
		$scope.isBillList = 0;
		var exAmount = '';
		
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
			
			exAmount = $scope.mpesaObject.mpesaAmount;
			
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
			
			if(($scope.walkInPatient == undefined || $scope.walkInPatient == null) &&  $scope.patientType == 3){
				
				growl.warning("Patient is not permanently register in the system.",{
					title : "WARNING"
				});
				return;
				
			}else{
				
				 var fixAmount = parseFloat(exAmount) - parseFloat($scope.payeeDueAmtFinal);
				  $scope.excessAmount = parseFloat(fixAmount).toFixed(2);
				  if(fixAmount>0)
					{
					  if($scope.exceededBillAmountSettlement==true)
						  {
						  $("#confirmSavewalk").modal("show");
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
	}
    
    $scope.addPaymentDetailsSAlesType = function () {
    	$scope.addPaymentDetails();
      }
    
    var parentChild = 0;
	$scope.addExAmount = function(){
		$scope.isBillList = 0;
		if( parentChild == 0 ){
			$scope.transferToActiveDeposit = 'Y';
			$scope.transferToActiveDepositFact = $scope.transferToActiveDeposit;
		}else{
			
			$scope.transferToActiveDeposit = 'Y';
			$scope.transferToActiveDepositFact = $scope.transferToActiveDeposit;
		}
		
		  pharmacyPaymentCollFactory.addPaymentDetails($scope);
		$("#confirmSave").modal("hide");
		parentChild += 1;
	}
	
	$scope.changeExAmount = function(){
		$scope.excessAmount = parseFloat(0.0).toFixed(2);
		$scope.paymentDetails.cashAmount = parseFloat($scope.dueAmount).toFixed(2);
	}
	
    $scope.removePaymentDetails = function (index) {
        pharmacyPaymentCollFactory.removePaymentDetails($scope,index);
        var amount = 0;
        $scope.listPaymentDetails.forEach((elem) => {
          amount = parseFloat(amount) + parseFloat(elem.amount)
        });
        if (amount <= 0 && $scope.currentBill.paidAmt <= 0) {
          $scope.isFinancialClearance = false;
          $('#isFinancialClearance').prop('checked', false);
        }
  	var totalListamount = 0;
  	angular.forEach($scope.listPaymentDetails,function(value,key)
  			{
  				totalListamount = totalListamount+parseInt(value.amount);
  			});
  	
  	
  	if(parseInt($scope.totalAmount) >=totalListamount){
		$scope.transferToActiveDeposit = 'N';
		$scope.excessAmount = parseFloat(0).toFixed(2);
	}else{
		var dummyAmount = parseFloat(totalListamount).toFixed(2) - parseFloat($scope.totalAmount).toFixed(2);
		$scope.excessAmount = parseFloat(dummyAmount).toFixed(2);
	}
      }
    
    
    var saveCashSettlement  = function()
    {
    	$scope.paymentDetails.paymentModeId = 1;
    	GetBillDetailsForCash();
    }

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

    $scope.init();
    $scope.initTotalAmounts();

    // Method to get the list of Generic
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

    initializeController();
    
    
   /** @Author Ganesh Chaudhari 16 May 2019 */
    
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
	          $('#validateButton').show();
		}  
    }
    
	$scope.getDetailsEntitlement = function(type)
	{
		$scope.paymentEntitlementId = type;
    $('#validateButton').hide();
		if(type == '2')
			{
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
			     var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyTypeId:type};
				    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getCompanyMasterList"
				    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success"){
							 $scope.companyMasterList = response.data.listObject;
							 // console.log("companyMasterList",JSON.stringify($scope.companyMasterList));
						}
					});
				    document.getElementById("id3").checked = true
			}
		if(type == '1' && $scope.entitlementObj.paymentEntitlementTypeId != 1)
			{
			  // self.model.walkInSale.items = [];
			  document.getElementById("id1").checked = true
			  
			  if($scope.encounterId!=null && $scope.encounterId>0){
				  $('#payeeChange').modal('show');
			  }
			}
		  $scope.entitlementObj.paymentEntitlementTypeId = type;
		  if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
	          $scope.isValidated = true;
            $('#validateButton').show();
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
		$scope.savePatintAndSponsor();
	}
	
	$scope.getContractByCompanyId = function(companyId)
	{
		$rootScope.startLoader();
		
		if(companyId!=undefined && companyId!="" && companyId!=null && !isNaN(companyId)){
			$scope.corporateCompanyId=companyId;
			if($scope.corEmpVerificationFlag){
				$scope.enableDisabledValidated=true;
				$scope.enableDisabledApply=false;
			}else{
				$scope.enableDisabledValidated=false;
				$scope.enableDisabledApply=true;
			}
		}else{
			$scope.corporateCompanyId="";
			$scope.enableDisabledValidated=false;
		}
		
		var companyTypeId = $scope.entitlementObj.paymentEntitlementTypeId;
		
		var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyId:companyId,companyTypeId:companyTypeId,visitTypeId:1,billingBedCategoryId:$scope.opdBillingCatId};
	    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getContractByCompanyId"
	    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success"){
				 $scope.contractList = response.data.listObject;
				 // getting insurance document name start
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
				// getting insurance document name End
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
					 // getting insurance document name start
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
					// getting insurance document name End
				}
				$rootScope.stopLoader();
			});
	}
	
	$scope.getContractDetails = function(item,idx,contId)
	{
		// alert(idx);
		var contractObj = null;
		contractObj = $.grep($scope.contractList, function (doc) {
            return doc.contractId == contId;
        })[0];
		// console.log("contractObj",contractObj);
		if(contractObj != undefined || contractObj != null)
			{
			 // alert("Some Values");
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
				/*
				 * if(v.priority > 1){ v.priority = v.priority - 1; }
				 */
				newDataList.push(v);
			}
			i++;
		});
		
		// $scope.paymentEntitleMentList = newDataList;
		
		var found = newDataList.find(ar=>ar.priority==priorityVal);
		  if(found == undefined)
			  {
			  	// return false;
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
			/*
			 * return arr .some(function(arr) {
			 * if(parseInt(arr.paymentEntitlementId) == val) { return true; }
			 * else { return false; } });
			 */
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
		// alert(idx);
		
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
			  // var found = $scope.array.find(ar=>ar==char);
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
		
		// console.log("contractObj",contractObj);
		if(contractObj != undefined || contractObj != null)
		{
		 // var payEntList = angular.copy($scope.paymentEntitleMentList);
		  // $scope.paymentEntitleMentList = [];
		  if($scope.entitlementObj.paymentEntitlementTypeId == '3')
		  {
		/*
		 * var sInsDt =
		 * $rootScope.getChangedFormatedDateShalse($scope.paymentEntitleMentCorporate[0].datepickerCons)+"
		 * 00:00:00"; var eInsDt =
		 * $rootScope.getChangedFormatedDateShalse($scope.paymentEntitleMentCorporate[0].datepickerConE)+"
		 * 00:00:00"; if(sInsDt > eInsDt) { growl.warning("Start date should be
		 * less then end date..!! ",{ title : "WARNING" }); return false; }else{
		 */
		  var newArr = {};
		  newArr.patientSponsorDetailsId = null;
		  newArr.paymentEntitlementName=contractObj.paymentEntitlementName;
		  newArr.paymentEntitlementId="3";
		  newArr.companyName = contractObj.assCompanyName;
		  newArr.companyId = contractObj.companyId;
		  newArr.tariffName = tarrifName;
		  newArr.contractName = contractObj.contractName;
		  newArr.insuranceName = "";
		  newArr.insuranceNumber = "";
		 // newArr.startDate =
			// moment($scope.paymentEntitleMentCorporate[0].datepickerCons).format('DD-MM-YYYY
			// HH:mm:ss');//contractObj.validityFrom;
		  // newArr.endDate =
			// moment($scope.paymentEntitleMentCorporate[0].datepickerConE).format('DD-MM-YYYY
			// HH:mm:ss');//contractObj.validityTo;datepickerConE
		  newArr.startDate = $rootScope.getChangedFormatedDateShalse($scope.paymentEntitleMentCorporate[0].datepickerCons)+" 00:00:00";
		  newArr.endDate =$rootScope.getChangedFormatedDateShalse($scope.paymentEntitleMentCorporate[0].datepickerConE)+" 00:00:00";
		  newArr.priority = "";
		  newArr.tariffId = $scope.paymentEntitleMentCorporate[0].tariffId;
		  newArr.contractId = contractObj.contractId;
		  newArr.insuredAmount = '';
		  newArr.typeId = 3;
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
						// 'statrDate':"",
						// 'endDate':""
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
			
			$scope.clearPaymentEntitlementData = function(){
				$scope.paymentEntitleMentInsurance[0].companyId = "";
				$scope.paymentEntitleMentInsurance[0].associateCompanyId = "";
				$scope.paymentEntitleMentInsurance[0].datepickerInsE = "";
				$scope.paymentEntitleMentInsurance[0].datepickerInss = "";
				$scope.paymentEntitleMentInsurance[0].contractId = "";
				$scope.paymentEntitleMentInsurance[0].insuranceNumber = "";
				$scope.paymentEntitleMentInsurance[0].insuredAmt = "";
				$scope.paymentEntitleMentCorporate[0].companyId = "";
				$scope.paymentEntitleMentCorporate[0].contractId = "";
				$scope.paymentEntitleMentCorporate[0].datepickerCons = "";
				$scope.paymentEntitleMentCorporate[0].datepickerConE = "";
				$scope.entitlementObj.paymentEntitlementTypeId = 1;
				document.getElementById("id1").checked = true;
				if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
					$scope.isValidated = true;
		            $('#validateButton').show();
				}
				else{
					$scope.isValidated = false;
					$('#validateButton').hide();
				}
				angular.element('#paymentEntitlementModal').modal('hide');
			}
			
	
	$scope.applyInsuranceDetails = function()
	{
		
			// var isEmpty =
			// !(Object.values($scope.paymentEntitleMentInsurance[0]).every(x
			// =>(x != null && x != '')));
		var objTmp = angular.copy($scope.paymentEntitleMentInsurance[0]);
		 objTmp.multidelete("insuranceNumber","datepickerInss","datepickerInsE","insuredAmt");
			 var isEmpty = !(Object.values(objTmp).every(x =>(x != null &&  x != '')));		 
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
		
		// console.log("$scope.paymentEntitleMentInsurance",$scope.paymentEntitleMentInsurance);
		
		if(insuranceObj != undefined || insuranceObj != null)
		{
		  // var payEntList = angular.copy($scope.paymentEntitleMentList);
		 // $scope.paymentEntitleMentList = [];
		  if($scope.entitlementObj.paymentEntitlementTypeId == '2')
		  {
			  var isAfter = true;
			 /*
				 * var sInsDt =
				 * moment($scope.paymentEntitleMentInsurance[0].datepickerInss).format('DD-MM-YYYY
				 * HH:mm:ss'); var eInsDt =
				 * moment($scope.paymentEntitleMentInsurance[0].datepickerInsE).format('DD-MM-YYYY
				 * HH:mm:ss');
				 * 
				 * var isAfter =
				 * moment($scope.paymentEntitleMentInsurance[0].datepickerInsE).isAfter($scope.paymentEntitleMentInsurance[0].datepickerInss);
				 */
			   /*
				 * alert(isAfter); //$('.result2').text('Date is After: ' +
				 * isAfter); return false;
				 */
			    
			  if(!isAfter)
				  {
					  growl.warning("Start date should be less then end date..!!",{
							title : "WARNING"
						});
						return false;
				  }else{
		  var newArr = {};
		  newArr.patientSponsorDetailsId=null;
		  newArr.paymentEntitlementName=insuranceObj.paymentEntitlementName;
		  newArr.paymentEntitlementId="2";
		  newArr.companyName = insuranceObj.assCompanyName;
		  newArr.companyId = insuranceObj.companyId;
		  newArr.tariffName = tarrifName;
		  newArr.contractName = insuranceObj.contractName;
		  newArr.insuranceName = associatedName;
		  newArr.insuranceId = insuranceId;
		  newArr.insuranceNumber = $scope.paymentEntitleMentInsurance[0].insuranceNumber;
		  // newArr.startDate =
			// moment($scope.paymentEntitleMentInsurance[0].datepickerInss).format('DD-MM-YYYY
			// HH:mm:ss');// contractObj.validityFrom;
		  // newArr.endDate =
			// moment($scope.paymentEntitleMentInsurance[0].datepickerInsE).format('DD-MM-YYYY
			// HH:mm:ss');// contractObj.validityFrom;datepickerInsE
		  newArr.startDate = $scope.paymentEntitleMentInsurance[0].datepickerInss != null ?moment($scope.paymentEntitleMentInsurance[0].datepickerInss).format('DD-MM-YYYY HH:mm:ss'): null;// contractObj.validityFrom;
		  newArr.endDate = $scope.paymentEntitleMentInsurance[0].datepickerInsE != null ? moment($scope.paymentEntitleMentInsurance[0].datepickerInsE).format('DD-MM-YYYY HH:mm:ss'): null;// contractObj.validityFrom;datepickerInsE
		  newArr.priority = "";
		  newArr.tariffId = $scope.paymentEntitleMentInsurance[0].tariffId;
		  newArr.contractId = insuranceObj.contractId;
		  newArr.typeId = 2;
		  newArr.insuredAmount = $scope.paymentEntitleMentInsurance[0].insuredAmt;
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
						// 'statrDate':"",
						// 'endDate':"",
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
				// 'statrDate':"",
				// 'endDate':""
			};
		
		$scope.paymentEntitleMentCorporate.push(data);
		
	}
	
	$scope.removeContract = function(index)
	{
		// console.log("removeIndex",index);
		var newDataList = [];
		var le = $scope.paymentEntitleMentList.length;
		var i = 1;
		if(le != i){
		angular.forEach($scope.paymentEntitleMentList, function(v) {
			if (index != i) {
				/*
				 * if(v.priority > 1){ v.priority = v.priority - 1; }
				 */
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
					 // v.priority = "";
					 v.priority = v.priority - 1;
					}
					
					/*
					 * if(v.paymentEntitlementId == "1") { v.priority = le - 1;
					 * }else { v.priority = ""; }
					 */
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
		
		
		// $scope.paymentEntitleMentCorporate =[];
		// $scope.addContracts();
		}
		
		
		
	}
	
	$scope.addInsurances = function()
	{
		var data = {
				'companyId':"",
				'associateCompanyId':"",
				'contractId':"",
				// 'statrDate':"",
				// 'endDate':"",
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
					  // console.log("associateCompanyMasterList",JSON.stringify($scope.associateCompanyMasterList));
				}
				$rootScope.stopLoader();
			});
		    
	}
	
	// validate And Save Contract
	
	$scope.saveAndValidateContract = function()
	{
		$scope.ActualsaveAndValidateContract();
		
		/*
		 * var bill = self.model.walkInSale; self.model.stopItemIds = [];
		 * angular.forEach(bill.items, function (item, key) { if (!item.disabled &&
		 * !item.itemDisabled) { self.model.stopItemIds.push(item.itemId); } });
		 * var stopData = { "itemIds": self.model.stopItemIds, "storeId":
		 * $cookies.getObject('cookieObject').storeId };
		 * stopTransactionService.GetstopTransactionData(stopData).then(function(responseData){
		 * var StopTransactionData = responseData; if(StopTransactionData.length >
		 * 0 ){ $rootScope.stopLoader(); var startdate =
		 * $filter('date')(StopTransactionData[0].startDate, "dd-MM-yyyy"); var
		 * stopdate= $filter('date')(StopTransactionData[0].stopDate,
		 * "dd-MM-yyyy"); var stopMessage = "Stop Transation is in Process for
		 * this Item(s) <br/>Start Date : "+ startdate +" <br/> Stop Date: "+
		 * stopdate +" <br/> Days: "+ StopTransactionData[0].days +" <br/>
		 * Hours: "+ StopTransactionData[0].hours +" <br/> Minutes: "+
		 * StopTransactionData[0].minutes +" "; growl.warning(stopMessage,
		 * {onclose: function() { var popup =
		 * angular.element('#confirmStopValidate'); popup.modal('show'); },}); }
		 * else{ $scope.ActualsaveAndValidateContract(); } });
		 */}
	
	$scope.ActualsaveAndValidateContract = function()
	{

	                if ($scope.consultationSelfPaid == true) {
                          growl.warning('Consultation amount is paid my patient.');
                        }
		$scope.totalSelfPayable = 0.00;
   	$scope.totalCreditPayable = 0.00;
		
		// itemReqObject
    var bill = self.model.walkInSale;
    if (ValidateBill(bill)) {
      $rootScope.startLoader();	 
      var data = {
        "contractId" : '',
        "organizationId" : $scope.organizationId,
        "unitId" : $scope.unitId,
        "visitTypeId" : $scope.visitTypeId,
        "paymentEntitlementId" : $scope.entitlementObj.paymentEntitlementTypeId,
        "encounterId" : $scope.encounterId,
        "admissionId" : $scope.admissionId,
        "billingBedCategoryId" : '',
        "patientCategoryId" : '',
        "defaultSelfTariffId" : '',
        "payeeId" : '',
        "selfPayeeId" : $scope.selfPayeeId,
        "patientClassId" : '',
        // "patientClassPercentage" : '',
        "patientId" : $scope.id,
        "tariffId" : $scope.defaultSelfTariffId,
        "taxInclusive" : $scope.taxInclusive,
        "walkInSales" : false,
        "opSales" : true,
        "doctorId" : $scope.doctorId,
        "consume" :false,
        "itemList" : []
      }
		       
      angular.forEach(bill.items, function (item, key) {
        if (!item.disabled && !item.itemDisabled) {
          if(isNaN(item.taxAmount)){
            item.taxAmount = 0.00;
          }
          var billItem =  {
            "itemId" : item.itemId,
            "batchId" : item.selectedBatch.batchId,
            "rate" : item.rate.toFixed(2),
            "quantity" : Number(item.quantity),
            "selfQuantity" : '',
            "packageConsumptionAmt" : 0,
            "totalAmount" : item.itemtotalAmount.toFixed(2),
            "discountAmount" : item.itemDiscount!=null ? item.itemDiscount.toFixed(2):0.00,
            "unitDiscountAmount" : item.unitDiscountAmount!=null ? item.unitDiscountAmount.toFixed(2):0.00,
            "netAmount" : item.netAmount.toFixed(2),
            "taxId" : item.taxId,
            "taxPercentage" : item.taxPercent!=null ? item.taxPercent.toFixed(2) : 0.00,
            "taxAmount" : item.taxAmount!=null ? item.taxAmount.toFixed(2):0.00,
            "productCategoryId" : item.productCategoryId,
            "payeeId" : (parseFloat(item.creditPayable)==0)?$scope.selfPayeeId:item.payeeId,
            "contractId" : '',
            "creditPayable" : !!item.creditPayable ? item.creditPayable.toFixed(2) : 0,
            "selfPayable" : !!item.selfPayable ? item.selfPayable.toFixed(2) : item.netAmount.toFixed(2),
            "index" : key,
            "childIndex" : '',
            "consessionPercentage": item.patientClassPercentage != undefined ?  item.patientClassPercentage.toFixed(2):0 ,
            "consessionAmount": item.patientClassAmount!=null ? item.patientClassAmount.toFixed(2): 0.00,
            "unitConsessionAmount": item.unitPatientClassAmount!=null ? item.unitPatientClassAmount.toFixed(2):0.00,
            "saleDetailPayeeMapperDtoList" : !!item.saleDetailPayeeMapperDtoList ? item.saleDetailPayeeMapperDtoList : [],
            "saleDetailPackageMapperDtoList" : !!item.saleDetailPackageMapperDtoList ? item.saleDetailPackageMapperDtoList : [],
            "isConsumed" : false
          }
          // Ganesh
          $scope.totalSelfPayable = $scope.totalSelfPayable+ item.selfPayable;
          $scope.totalCreditPayable = $scope.totalCreditPayable+ item.creditPayable;
          data.itemList.push(billItem);
        }
      });
		
		  $scope.currentDate = new Date();
		  $scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
		
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
                if(consumedItems.batchId==value.selectedBatch.batchId && consumedItems.itemId==value.itemId && key==consumedItems.index)   
                {
                  if (!value.disabled && !value.itemDisabled) 
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
                    value.taxAmount = consumedItems.taxAmount;
                    if(value.isConsumed == false){
                        $scope.isValidated = false;
                        $('#validateButton').hide();
                    } 
                  }
                }
                else if (value.selectedItems && !value.itemDisabled) 
                {
                  angular.forEach(value.selectedItems, function (selectedItem, index) 
                  {
                    angular.forEach(response.data.listObject, function (consumedSelectedItems, consumedKey) 
                    {
                      if(consumedSelectedItems.batchId==selectedItem.selectedBatch.batchId && consumedSelectedItems.itemId==selectedItem.itemId && key==consumedItems.index)   
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
                        $scope.isConsumed = value.isConsumed;
                        if(value.isConsumed == false){
                            $scope.isValidated = false;
                            $('#validateButton').hide();
                        } 
                      }
                    }); 
                  });
                }
              });
            });
            var totalSelfPayableAfterFullyInsurance=0;
            angular.forEach(bill.items, function (item, index) 
                    {
            	totalSelfPayableAfterFullyInsurance=totalSelfPayableAfterFullyInsurance+item.selfPayable;
                    });
                if(totalSelfPayableAfterFullyInsurance==0)
            	{
            	self.model.cashSettlement = false;
            	}
            CalculateItemSum(bill.items);
          }
          if(response.data.object!=null)
          {
              self.model.patientId = response.data.object.patientId;
          }
          
          if(response.data.resObject!=null)
          {
          	growl.warning('Pharmacy bill amount getting exceeded with '+response.data.resObject+' amount than package amount ',{
                  title : 'Warning!'
              });
          }
          
          // $scope.isValidated = true;
          $rootScope.stopLoader();
        }
        else if(response.data.status == "error")
        {
          $rootScope.stopLoader();
        }
      });
    }
	}
	
  $scope.openInsuranaceImage = function(documentPath)
  { 
    if(documentPath!==""){
      var imageUrl = documentPath;
      var href = $rootScope.currentHrefDoccument();
      $.fancybox.open({
        width: 600, // or whatever
        height: 320,
        href:href+imageUrl,
        type: 'iframe'
      });
    }else{
      growl.error("No image found",
      {
        title : 'Error!'
      });
    }									
  }

	
	/*
	 * *************************************Mpesa Integration
	 * start******************************************
	 */
	$scope.mpesaObject={"transactionId":null,"mobileNo":null,"mpesaAmount":null,"resTransactionId":null};
	$scope.validateMpesaDetails = function(mpesaObject){
		$rootScope.startLoaderPopup();
		// console.log("MPesa Object:",mpesaObject);
		
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
			$scope.skipMpesaValidation = false;
			$rootScope.stopLoaderPopup();
			growl.error("Enter both transaction id and mobile number",{
				title : ERROR_MSG
			});
		}
	}
	
	$scope.checkoutRequestId= null;
	$scope.mpesaPaymentRequest = function(validateMpesaAmount,mobileNumber,countryCallingCode){
		
		if(validateMpesaAmount == undefined || validateMpesaAmount == null){
			
			growl.warning("Please Enter Amount",{
				title : "warning!!!"
			});
			
		}else if(validateMpesaAmount > $scope.payeeDueAmtFinal){
			
			growl.warning("Mpesa amount is should be equel or less than due amount ",{
				title : "warning!!!"
			});
			
		}
		else{
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
							$scope.checkoutRequestId = response.data.object.checkoutRequestId;
							growl.success(response.data.message,{
								title : SUCCESS_MSG
							});
							$scope.hitMpesaApi = true;
							$rootScope.stopLoaderPopup();
							angular.element('#paymentReqModalPhr').modal('hide');
							}
						else if(response.data.status == "error"){
							
							growl.error(response.data.message != null ? response.data.message : "There is problem in Mpesa payment gatway",{
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
	}
	
	$scope.validateSTKPushPayment = function(){
		
		var data ={
				organizationId :$scope.organizationId,
				unitId : $scope.unitId,
				uhidNumber: $scope.selectedUhid,
				checkoutRequestId:$scope.checkoutRequestId
				
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
    
    	$scope.clearCoEmpDetails=function(){
			/*
			 * $scope.corEmpDependantList=[]; $scope.showNameDetails=false;
			 * $scope.showHideAcceptRejectBtn=false; $scope.showHideOkBtn=false;
			 */
			if($scope.paymentEntitleMentCorporate[0].companyId == null || $scope.paymentEntitleMentCorporate[0].companyId == '' || $scope.paymentEntitleMentCorporate[0].companyId == undefined){
				 growl.warning("Please select company.",{
						title : "WARNING"
					});
					return; 
			 }
			 else if($scope.paymentEntitleMentCorporate[0].contractId == null || $scope.paymentEntitleMentCorporate[0].contractId == '' || $scope.paymentEntitleMentCorporate[0].contractId == undefined){
				 growl.warning("Please select contract.",{
						title : "WARNING"
					});
					return; 
			 }
			 else{
				 $scope.corEmpDependantList=[];
					$scope.showNameDetails=false;
					$scope.showHideAcceptRejectBtn=false;
					$scope.showHideOkBtn=false;
					$scope.initCoEmp();
					$scope.initEmpObj();
					$scope.getTypeOfCoEmp(3);
					angular.element('#validationEmployeeModel').modal('show');
			 }
		}
    	
    	$scope.initCoEmp=function(){
    		$scope.corEmpObj={
    				typeOfCoEmp:'3',
    				// employeeNumber:"",
    				note:"",
    				employeeName:"",
    				relation:"",
    				medicalCardNo:""
    					
    		}
    	}
    	$scope.initCoEmp();
    	
    	$scope.initEmpObj=function(){
    		$scope.empObj={
    				employeeNumber:""
    					
    		}
    	}
    	$scope.initEmpObj();	
    	
    	$scope.empNoShowHide=true; // for empNo text field hide show
    	$scope.noteShowHide=false; // for note text area hide show
    	$scope.dependTblShowHode=false; // for table hide show
    	$scope.showHidevalidate=true; // for validate buttton
    	$scope.showHideSaveBtn=false; // for mannual co-emp verification
    	$scope.showHideAcceptRejectBtn=false;
    	$scope.showNameDetails=false;
    	$scope.showHideOkBtn=false;
    	$scope.getTypeOfCoEmp=function(id){
    		$scope.empObj.employeeNumber = '';
    		$scope.corEmpObj.employeeName = '';
    		$scope.corEmpObj.relation = '';
    		$scope.corEmpObj.medicalCardNo = '';
    		$scope.corEmpObj.note = '';
    		  if(id=="1"){
    			  $scope.corEmpObj.typeOfCoEmp="1";
    			  $scope.dependTblShowHode=false;
    			  $scope.noteShowHide=false;
    			  $scope.showHidevalidate=true;
    			  $scope.showHideSaveBtn=false; 
    			  $scope.showNameDetails=false;
    			  $scope.showHideOkBtn=false;
    		  }else if(id=="2"){
    			  $scope.corEmpObj.typeOfCoEmp="2";
    			  $scope.dependTblShowHode=true;
    			  $scope.showHidevalidate=true;
    			  $scope.noteShowHide=false;
    			  $scope.showHideSaveBtn=false; 
    			  $scope.showNameDetails=false;
    			  $scope.showHideOkBtn=false;
    		  }else if(id=="3"){
    			  $scope.corEmpObj.typeOfCoEmp="3";
    			  $scope.dependTblShowHode=false;
    			  $scope.noteShowHide=true; 
    			  $scope.showHidevalidate=false; 
    			  $scope.showHideSaveBtn=true; 
    			  $scope.showNameDetails=false;
    			  $scope.showHideAcceptRejectBtn=false;
    			  $scope.showHideOkBtn=false;
    		  }else{
    			  
    		  }
        }
    	
    	$scope.getTypeOfCoEmp(3);
    	$scope.name="";
    	$scope.displayMsg="";
    	$scope.empObj.employeeNumber="";
    	$scope.validateCorporateEmp=function(){
    		$scope.name="";
    		if($scope.corEmpObj.typeOfCoEmp==undefined || $scope.corEmpObj.typeOfCoEmp=="" || $scope.corEmpObj.typeOfCoEmp==null){
    			growl.warning("Please type of verification",{
    				title : WARNING_MSG
    			});
    			return;
    		}
    			if($scope.patientDetails.patientId==undefined || $scope.patientDetails.patientId=="" || $scope.patientDetails.patientId==null){
    				growl.warning("Please select patient",{
    					title : WARNING_MSG
    				});
    				return;
    			}
    			if($scope.corporateCompanyId==undefined || $scope.corporateCompanyId=="" || $scope.corporateCompanyId==null){
    				growl.warning("Please select company name",{
    					title : WARNING_MSG
    				});
    				return;
    			}
    		// for staff or employee
    			$scope.corEmpListObj=[];
    			$scope.corEmpDependantList = [];
    			$scope.displayMsg="";
    		if($scope.corEmpObj.typeOfCoEmp=='1'){
    			if($scope.empObj.employeeNumber==undefined || $scope.empObj.employeeNumber=="" || $scope.empObj.employeeNumber==null){
    				growl.warning("Please enter employee number",{
    					title : WARNING_MSG
    				});
    				return;
    			}
    			var data={
    					typeOfEmp:$scope.corEmpObj.typeOfCoEmp,
    					employeeNumber:$scope.empObj.employeeNumber,
    					organizationId:$scope.organizationId,
    					unitId:$scope.unitId,
    					firstName:$scope.patientDetails.firstName,
    					middleName:$scope.patientDetails.middleName,
    					lastName:$scope.patientDetails.lastName,
    					corporateId:$scope.corporateCompanyId
    // genderId:$scope.patientDetails.genderId,
    // birthDate:$scope.patientDetails.birthDate
    			    }
    			var URI = BASE_URL + ROOT_URL + "/api/global/verifyCorporateEmployee";
    			BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
    					if (response.data.status == "success") {
    					
    						if(response.data.object!=null && response.data.listObject!=null){
    							
    							$scope.corEmpObj = response.data.object;
    							$scope.corEmpListObj=response.data.listObject[0];
    							
    							$scope.name=$scope.corEmpListObj.firstName+" "+$scope.corEmpListObj.middleName+" "+$scope.corEmpListObj.lastName;
    							if($scope.corEmpObj==1){
    								
    									$scope.color="green";
    									$scope.enableDisabledValidated=false;											
    									$scope.showHideAcceptRejectBtn=true;
    									
    								}else if($scope.corEmpObj==2){
    									
    									$scope.color="red";		
    									$scope.showHideAcceptRejectBtn=true;
    								}else{
    								}
    							$scope.showHideOkBtn=false;
    						}else{
    							$scope.color="red";	
    							$scope.name="record not found.."
    							$scope.showHideAcceptRejectBtn=false;
    							$scope.showHideOkBtn=true;
    						}
    						$scope.showNameDetails=true;
    						 // console.log("$scope.corEmplist",$scope.corEmpObj);
    // $scope.initCoEmp();
    						 angular.element('#validationEmployeeModel').modal('hide');
    					}
    				});
    		
    		}else if($scope.corEmpObj.typeOfCoEmp=='2'){
    			if($scope.empObj.employeeNumber==undefined || $scope.empObj.employeeNumber=="" || $scope.empObj.employeeNumber==null){
    				growl.warning("Please enter employee number",{
    					title : WARNING_MSG
    				});
    				return;
    			}
    			var data={
    					typeOfEmp:$scope.corEmpObj.typeOfCoEmp,
    					employeeNumber:$scope.empObj.employeeNumber,
    					organizationId:$scope.organizationId,
    					unitId:$scope.unitId,
    					corporateId:$scope.corporateCompanyId
//    					
    			    }
    			var URI = BASE_URL + ROOT_URL + "/api/global/verifyCorporateEmployee";
    			BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
    					$scope.corEmpDependantList = [];
    					if (response.data.status == "success") {
    					
    						if(response.data.listObject!=null && response.data.listObject.length>0){
    							$scope.corEmpDependantList = response.data.listObject;
    							$scope.showNameDetails=true;
    							angular.forEach($scope.corEmpDependantList, function (value, key) {
    								if(value.employeeNumber != null && value.employeeNumber != undefined && value.employeeNumber != ""){
    									$scope.name = value.empName;
    									return;
    								}
    							});
    // angular.element('#validationEmployeeModel').modal('hide');
    						}else{
    							  $scope.color="red";
    							  $scope.showNameDetails=true;
    							  $scope.name="record not found.."
    							  $scope.showHideAcceptRejectBtn=false;
    							  $scope.showHideOkBtn=true;
    						}
    						
    						
    					}
    // $scope.initCoEmp();
    				});
    		}
    		
    		$scope.getEmpDependantDetailsByIndex=function(index){
    			$scope.showNameDetails=false;
    			     if($scope.patientDetails.firstName==$scope.corEmpDependantList[index].firstName) {
    // $scope.name=$scope.corEmpDependantList[index].firstName+"
    // "+$scope.corEmpDependantList[index].middleName+"
    // "+$scope.corEmpDependantList[index].lastName;
    			    	 if($scope.patientDetails.genderId==$scope.corEmpDependantList[index].genderId){
    			    		       
    // $scope.color="green";
    								$scope.enableDisabledValidated=false;										
    								$scope.showHideAcceptRejectBtn=true;
    							
    			    	 }else{
    // $scope.color="red";
    							$scope.enableDisabledValidated=false;										
    							$scope.showHideAcceptRejectBtn=true;
    						}
    			     }else{
    // $scope.color="red";
    						$scope.enableDisabledValidated=false;										
    						$scope.showHideAcceptRejectBtn=true;
    					}
    			 	$scope.showNameDetails=true;
    // $scope.initCoEmp();
    			
    		}
    		
    		
    	}
    	
    	$scope.saveMannualDetails=function(){
    		if($scope.empObj.employeeNumber==undefined || $scope.empObj.employeeNumber=="" || $scope.empObj.employeeNumber==null)
    		{
    			growl.warning("Please enter employee number.",{
    				title : WARNING_MSG
    			});
    			return;
    		
    		}
    	/*
    	 * else if($scope.corEmpObj.note==undefined || $scope.corEmpObj.note=="" ||
    	 * $scope.corEmpObj.note==null){ growl.warning("Please enter note",{ title :
    	 * WARNING_MSG }); return; }
    	 */
    		if($scope.corEmpObj.employeeName==undefined || $scope.corEmpObj.employeeName=="" || $scope.corEmpObj.employeeName==null){
    			growl.warning("Please enter Employee Name",{
    				title : WARNING_MSG
    			});
    			return;
    		}
    		
    		if($scope.corEmpObj.relation==undefined || $scope.corEmpObj.relation=="" || $scope.corEmpObj.relation==null){
    			growl.warning("Please enter Relation",{
    				title : WARNING_MSG
    			});
    			return;
    		}
    		$scope.enableDisabledApply=true;	
    		$scope.enableDisabledValidated=false;
    		angular.element('#validationEmployeeModel').modal('hide');
    		$scope.employeeNumberManual = $scope.empObj.employeeNumber;
    		$scope.employeeName = $scope.corEmpObj.employeeName;
    		$scope.relation = $scope.corEmpObj.relation;
    		$scope.medicalCardNo = $scope.corEmpObj.medicalCardNo;
    		$scope.initEmpObj();
    		
    		
    	}
    
	/*
	 * *************************************Mpesa Integration
	 * END******************************************
	 */

  var groupBy = function(xs, key) {
    return xs.reduce(function(rv, x) {
      (rv[x[key]] = rv[x[key]] || []).push(x);
      return rv;
    }, {});
  };

	 // ganesh
	  $scope.getPayeeCreditDetails = function(itemList)
	    {
	    	// $scope.payeeCreditDetailslList = [];
	    	// $scope.payeeCreditDetailslList = itemList;
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



	$scope.checkValidatePinPad = function()
	{
		if(($scope.registeredPinPadDevice != undefined || $scope.registeredPinPadDevice != null || $scope.registeredPinPadDevice != "") 
				&& $scope.registeredPinPadDevice == "POS Machine"){
			$scope.getResposeList=[];
			$scope.getGeneratedResponse=[];
			$rootScope.startLoaderPopup();
			var data = {
					patientId : ($scope.tempPatId != undefined || $scope.tempPatId != null || $scope.tempPatId != "") ? $scope.tempPatId : $scope.walkInPatient,
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
// $scope.paymentDetails.credCardTypeId=$scope.getGeneratedResponse.cardType;
							$scope.paymentDetails.credAmount=$scope.getGeneratedResponse.transactionAmount;
							if($scope.flagForBharatQr == false){
								$scope.paymentDetails.credCardNumber=$scope.getGeneratedResponse.transactionCardNumber.slice($scope.getGeneratedResponse.transactionCardNumber.length - 4);
							}else{
								$scope.paymentDetails.credCardNumber=$scope.getGeneratedResponse.transactionCardNumber;
							}
							$scope.paymentDetails.transactionNo=$scope.getGeneratedResponse.transactionID;
							$scope.paymentDetails.credExpiryDate=moment($scope.getGeneratedResponse.expiryDate).toDate();
							$scope.paymentDetails.requestedUniqueId=$scope.getGeneratedResponse.billNumber/* $scope.getGeneratedResponse.invoiceNumber */;
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
		// alert($location.host());
		// var IAddress = $location.host();
		// alert("IP ADRRESS");
		// alert(IAddress);
		var data ={
				organizationId :$scope.organizationId,
				unitId : $scope.unitId,
				createdBy : $scope.userId,
				createdDate : moment(new Date()).format("DD-MM-YYYY HH:mm:ss"),
				tillId:'50002',
				patientId: $scope.selectedPatientId,
				// ip : IAddress
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
							// "amount":100,
							// tellerId: "50002",
							tranKey: $scope.uniqueNumber
					}
					
				/*
				 * $http.post('http://localhost:9991/api/sale',dataForPinPad)
				 * .then(function(response) { alert(); });
				 */
					
					/*
					 * return $http({ headers: { "Accept":
					 * "application/json;odata=verbose", "content-type":
					 * "application/json;odata=verbose" }, method: "POST", url:
					 * "http://localhost:9991/api/sale", data: { __metadata: {
					 * "type": "SP.Data.SongsListItem" }, "amount": "100",
					 * "tellerId": "50002", "tranKey": "201903282" }
					 * 
					 * 
					 * }).then(saveContact) .catch(function(message) {
					 * //console.log("addQuestion() error:" + message); });
					 * 
					 * function saveContact(data, status, headers, config) {
					 * alert("Item added successfully"); }
					 */
					
					/*
					 * var URI = "http://localhost:9991/api/sale"; var
					 * headerObjDia = { 'Content-Type':'application/json', };
					 * var data={};
					 * 
					 * GenericServiceParamHeader .serviceAction(METHOD_POST,
					 * URI, dataForPinPad,
					 * NOTIFICATION_MSG_STATUS_FALSE,data,headerObjDia) .then(
					 * function(response) { //console.log(response); alert();
					 * });
					 */
             
             
// /$scope.ipAddress+
					var urlprotocol = $location.protocol();
					// var finalUrl =
					// "http://"+$scope.ipAddress+":9991/api/sale";
					var finalUrl = "http://127.0.0.1:9991/api/sale";
					var URI = finalUrl;
					BillingGenericService.serviceAction(METHOD_POST,URI,dataForPinPad,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						/*
						 * if (response.data.status == "success"){
						 * //console.log("ree",response);
						 * //console.log("reew",response.data);
						 * //console.log("reer",response.data.rspCode);
						 */
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
									// console.log("resString",$scope.resString);
									
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
												// console.log("saved Card
												// Details");
											    $scope.addPaymentDetails();
											}
										else if(response.data.status == "error"){
											
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
					}else{
						 
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

	$scope.savePatintAndSponsor=function(){
		
		  if ($scope.admissionId > 0 || $scope.isWalkInSales === true || $scope.visitNumberFlag === true) {
	             return;
	         }
		 var bill = self.model.walkInSale;
		 
         if($scope.entitlementObj.paymentEntitlementTypeId == '3'||$scope.entitlementObj.paymentEntitlementTypeId == '2')
   		{
        	 var found = $scope.paymentEntitleMentList.find(ar=>(ar.paymentEntitlementId==2||ar.paymentEntitlementId==3));
					if(found==undefined)
		   			   {
		   			     $rootScope.stopLoader();
		   			     growl.error('Please Apply Payment Entitlement.');
                 $scope.saveBtn = true;
		   			     return false;
		   			   }
			var priorityFound = $scope.paymentEntitleMentList.find(ar=>(ar.priority==null||ar.priority==''));
			      if(!(priorityFound==undefined))
		   			   {
		   			     $rootScope.stopLoader();
		   			     growl.error('Please select priority');
                $scope.saveBtn = true;
		   			     return false;
		   			   }
   		}
		    var data = {
		      "walkInSales":bill.isWalkInSales,
		      "screenId": 1,
		      "prefixId": (bill.isWalkInSales && !!bill.prefix) ? bill.prefix.id : undefined,
		      "patientId": self.model.patientId,
		      "admissionId": (!bill.isWalkInSales && !!self.model.admissionId) ? self.model.admissionId : undefined,
		      "encounterId": $scope.encounterId,
		      "patientName": (bill.isWalkInSales && !!bill.patientName) ? bill.patientName : undefined,
		      "patientAddress": (bill.isWalkInSales && !!bill.patientAddress) ? bill.patientAddress : undefined,
		      "mobileNumber": (bill.isWalkInSales && !!bill.mobileNumber) ? bill.mobileNumber : undefined,
		      "patientAge": ((self.model.patientId == undefined || self.model.patientId ==null) &&  !!bill.age) ? bill.age : undefined,
		      "patientGenderId": (bill.isWalkInSales && !!bill.patientGender) ? bill.patientGender.id : undefined,
		      "doctorId": (!bill.isWalkInSales && !!bill.doctorId) ? bill.doctorId : undefined,
		      "doctorName": !!bill.doctorName ? bill.doctorName : undefined,
		      "doctorAddress": !!bill.doctorAddress ? bill.doctorAddress : undefined,
		      "saleTypeId":  $scope.saleTypeIdAgainstEncounter,
		      "paymentEntitlementId":$scope.entitlementObj.paymentEntitlementTypeId,
		    }

		$scope.currentDate = new Date();
		$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
		var sponserList = [];
		angular.forEach($scope.paymentEntitleMentList, function (value, key) {
			
			var newArr = {};
			newArr.patientId= $scope.patientId;
			newArr.patientSponsorDetailsId = value.patientSponsorDetailsId;
			newArr.organizationId = $scope.organizationId ;
			newArr.unitId = $scope.unitId;
			newArr.visitTypeId = 1;
			newArr.patientId = $scope.patientId;
			newArr.paymentEntitlementId =value.paymentEntitlementId;
			newArr.payeeId = value.companyId;
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
	      patientId:data.patientId,
	      encounterId: $scope.encounterId,
	      admissionId:$scope.admissionId,
	      orgId:$scope.organizationId,
	      unitId:$scope.unitId,
	      request:data,
	      patientSponsorDetailsDtosList:sponserList,
	      selfPayeeId : $scope.selfPayeeId
	    };
		
		
		  if (ValidateSponsor(bill)) {
			  $rootScope.startLoader();	 
    var URI = BASE_URL + ROOT_URL + "/api/pharmacy/sales/walkins/savePatintAndSponsor"
    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
      if (response.data.status == "success")
      {
        if(response.data.object!=null)
        {
          self.model.tempPatientId = response.data.object.tempPatientId;
          $scope.patientId=response.data.object.patientId;
          $scope.patientId=response.data.object.patientId;
          $scope.patientIdDoc=response.data.object.patientId;
          $scope.id = response.data.object.patientId;
          $scope.encounterId = response.data.object.encounterId;
          $scope.admissionId = response.data.object.admissionId;
        }
        $scope.getSponsorDetails($scope.encounterId,$scope.admissionId ,$scope.patientId);
        $scope.paymentEntitlementRecalculate();
        $rootScope.stopLoader();
      }
      else if(response.data.status == "error")
      {
        $rootScope.stopLoader();
      }
         });
	 }
  }

     $scope.paymentEntitlementRecalculate = function () {
                    var URI = BASE_URL + ROOT_URL + "/api/pharmacy/sales/walkins/terrifData";
                    var bill = self.model.walkInSale;
                     var data = {
                		   "contractId" : '',
                		   "organizationId" : $scope.organizationId,
                		   "unitId" : $scope.unitId,
                		   "visitTypeId" : 1,
                		   "paymentEntitlementId" : $scope.entitlementObj.paymentEntitlementTypeId,
                		   "encounterId" : $scope.encounterId,
                		   "admissionId" : $scope.admissionId,
                		   "billingBedCategoryId" : '',
                		   "patientCategoryId" : bill.patientCategoryId,
                		   "defaultSelfTariffId" : '',
                		   "payeeId" : '',
                		   "selfPayeeId" : $scope.selfPayeeId,
                		   "patientClassId" : '',
                		// "patientClassPercentage" : '',
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
                           // Ganesh
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
                               // Ganesh
                               $scope.totalSelfPayable = $scope.totalSelfPayable+ item.selfPayable;
                               $scope.totalCreditPayable = $scope.totalCreditPayable+ item.creditPayable;
                               data.itemList.push(billSubItem);
                           });
                       }
                   });
                     
                     
                     GenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
                      // var test = response.data.object.itemList;
                    	
                    	if(response.data.status=="success")
                    		{
                    		 angular.forEach(response.data.object.itemList,function(data,key) {
                                 
                            	 angular.forEach(self.model.walkInSale.items,function(item,key) {
                                     
                            		 if(data.itemId == item.itemId)
                            			 {
    	                        			 angular.forEach(item.batch,function(batch,key) {
                                                 if(batch.batchId != undefined){
                                                    if(data.batchId == batch.batchId){
                                                        item.taxPercentage = item.taxPercent;
                                                        var data1 = {
                                                            "mrp": batch.rate,
                                                            "isTaxInc": $scope.taxInclusive,
                                                            // "markup":
															// !!data.markUpDown
															// ?
															// data.markUpDownPer
															// : null,
                                                            // "markdown":
															// !data.markUpDown
															// ?
															// data.markUpDownPer
															// : null,
                                                             "markup": data.markUpDown,
                "markUpDownPer" : data.markUpDownPer,
                                                            "discount": item.discount == 0 || item.discount == undefined ? item.discountPerItem== 0 || item.discountPerItem == undefined  ? item.discountPerItem : item.discount : 0,
                                                            "isDiscPercent": !!item.discountType && item.discountType == 1 ? true : false,
                                                            "tax": item.taxPercentage,
                                                            "qty": item.quantity,
                                                            "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                                            "patientClassPercentage": item.patientClassPercentage,
                                                            "patientClassAmount":item.patientClassAmount!=undefined && item.patientClassAmount!=null ? item.patientClassAmount:0
                                                        }

                                                        var response = commonDetailService.calculateAmount(data1);
                                                        item.rate = response.rate;
                                                        item.taxAmount = isNaN(parseFloat(response.taxAmount))?0:response.taxAmount;
                                                        item.responseTaxAmount =  isNaN(parseFloat(response.taxAmount))?0:response.taxAmount;
                                                        item.unitDiscountAmount = isNaN(parseFloat(response.unitDiscountAmount))?0:response.unitDiscountAmount;
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
                    		 CalculateItemSum(self.model.walkInSale.items);
                    		}
                    	else {
                    		// alert("test");
                    	}
                    });
                 }
     
     // start code by minesh for select gender as per prefix HIS-8926
     var GetPrefixWithGender = function () {
    	    var URI = BASE_URL + ROOT_URL+ GETACTIVEPREFIXLIST;
    	    $rootScope.startLoader();
    	    GenericService.serviceAction(METHOD_GET, URI, {},NOTIFICATION_MSG_STATUS_FALSE).then(
    				function(response) {
    					// console.log(response);
    					self.model.prefixListWithGender = [];
    					if (response.data.status == "success"){
    						self.model.prefixListWithGender = response.data.listObject;
    					}				
    					 $rootScope.stopLoader();
    				});
    	    }
     
     $scope.selectGender = function(prefix)
		{
    	 let id = prefix.id;
			if(id > 0){
			var genId = $.grep(self.model.prefixListWithGender, function (doc) {
	            return doc.prefixId == id;
	        })[0].id;
			
		    self.model.walkInSale.patientGender = self.model.patientGenderList.find(function(obj){
		        return obj.id == genId;
		      });
			}
			else
				{
				$scope.patientObj.prefixId = '';
				$scope.patientObj.genderId = '';
				}
			
		}
   // start code by minesh for select gender as per prefix HIS-8926
     

  $scope.addImageDetailsForContract = function(obj)
  { 
    var encounterDetails = angular.copy($scope.encounterDetailsContract);
    encounterDetails.patientId = $scope.patientId;
    // insurance
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
		    
		    
		
		    if(self.model.patientId !=null  ){
	    		 $scope.patientIdDoc = self.model.patientId;
	    	 }
		    if($scope.patientIdDoc==null || $scope.patientIdDoc=='' || $scope.patientIdDoc==undefined){
           	 growl.warning(
					 	'If patient is walk in Please save Payment Entitlement first then upload document or please select patient from search',
					 	{
					 		title : 'warning!'
					 	});
					 return;
		    }
            if(self.model.patientIdDoc !=null ){
	    		 $scope.patientIdDoc = $scope.patientIdDoc;
	    	 }
			$scope.encounterDetailsContract = {
                encounterDocId : "",
                encounterId : 0,
                admissionId:0,
                visitTypeId:1,
                patientId : $scope.patientIdDoc,
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
		
       
				 
            if($scope.entitlementObj.paymentEntitlementTypeId == 1){						
                $scope.encounterDetailsContractList =[];
                var encounterDetails = angular.copy($scope.encounterDetailsContract);
                // encounterDetails.patientId = obj.patientId;
                // encounterDetails.encounterId = obj.encounterId;
                encounterDetails.paymentEntitlementId = $scope.entitlementObj.paymentEntitlementId;
                $scope.encounterDetailsContractList.push(encounterDetails);	
                $scope.getPatientDocumentList(encounterDetails);
                $('#documentUploadModalForEncounter').modal('show');
            }else if($scope.entitlementObj.paymentEntitlementTypeId == 2){
                $scope.encounterDetailsContractList =[];
                var encounterDetails = angular.copy($scope.encounterDetailsContract);
                encounterDetails.documentTypeId = '30';	
                // encounterDetails.patientId = obj.patientId;
                // encounterDetails.encounterId = obj.encounterId;
                encounterDetails.paymentEntitlementId = $scope.entitlementObj.paymentEntitlementId;
                $scope.encounterDetailsContractList.push(encounterDetails);
                $scope.getPatientDocumentList(encounterDetails);
                $('#documentUploadModalForEncounter').modal('show');
            }else if($scope.entitlementObj.paymentEntitlementTypeId == 3){
                $scope.encounterDetailsContractList =[];
                var encounterDetails = angular.copy($scope.encounterDetailsContract);
                encounterDetails.documentTypeId = '19';	
                // encounterDetails.patientId = obj.patientId;
                // encounterDetails.encounterId = obj.encounterId;
                encounterDetails.paymentEntitlementId = $scope.entitlementObj.paymentEntitlementId;
                $scope.encounterDetailsContractList.push(encounterDetails);
                $scope.getPatientDocumentList(encounterDetails);		
                $('#documentUploadModalForEncounter').modal('show');
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
        // if(obj.patientId==null || obj.patientId=='' ||
		// obj.patientId==undefined){
			  // growl.warning(
				// 'Please select atleast one patient!!!.',
				// {
				// title : 'warning!'
				// });
				// $scope.encounterDetailsContractList =[];
				// return;
		    // }
            $rootScope.startLoader();
            var data =
            {
                "patientId": $scope.patientIdDoc,
                "unitId":$scope.unitId,
                "organisationId":$scope.organizationId,
                "userId":$scope.userId,
                "visitTypeId":1,
                "encounterId": '',
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
        	$rootScope.startLoaderPopup();
            $scope.countFrValidation = 0;
            angular.forEach($scope.encounterDetailsContractList, function(value, key) {  
                if(value.documentTypeId==undefined || value.documentTypeId=="" || value.documentTypeId==null){
                    growl.warning("Please select document type .",
                    {
                        title : 'warning!'
                    });
                    $rootScope.stopLoader();
                    $rootScope.stopLoaderPopup();
                    return false;
                }
                if (angular.equals(value.file, {}) || value.file==undefined || value.file==null) {
                    growl.warning('Please select file.',
                    {
                        title : 'warning!'
                    });
                    $rootScope.stopLoader();
                    $rootScope.stopLoaderPopup();
                    return false;
                } 
                var ext = value.file.name.split(".").pop().toLowerCase();
                if($.inArray(ext, ["doc","pdf",'docx',"txt"]) == -1) {
                    growl.warning('You can only upload *.txt, *.pdf, *.docx, *.doc file',
                    {
                        title : 'warning!'
                    });
                    $rootScope.stopLoader();
                    $rootScope.stopLoaderPopup();
                    return false;
                }
                $scope.countFrValidation++;
			});
            if($scope.encounterDetailsContractList.length!=$scope.countFrValidation){
            	$rootScope.stopLoaderPopup();
                return false;
            }
            $rootScope.startLoader();
            $scope.count = 0;
            angular.forEach($scope.encounterDetailsContractList, function(value, key) 
            {  
                $('#documentUploadModalForEncounter').modal('hide');
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
                            $rootScope.stopLoaderPopup();
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
          fd.append('encounterId', Obj.encounterId);
          fd.append('visitTypeId', Obj.visitTypeId);
          fd.append('admissionId', Obj.encounterId);
          fd.append('patientId',Obj.patientId);
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

      $scope.getActivePatientTypeList = function()
	    {
        var URI = BASE_URL + ROOT_URL +"/api/billing/getPatientType/"+$scope.organizationId+"/"+$scope.unitId;
        GenericService.serviceAction(METHOD_GET, URI,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
          // console.log(response);
          if (response.data.status == "success"){                
            $scope.patientTypeList = response.data.listObject;
            // console.log("list",$scope.patientTypeList);
          }         
        });
	    }
      $scope.getActivePatientTypeList();
      
      $scope.showDocUPloadPoup   = function(encounterObj)
    	{
    	  
    	  if(self.model.patientId !=null  ){
	    		 $scope.patientIdDoc = self.model.patientId;
	    	 }
		    if($scope.patientIdDoc==null || $scope.patientIdDoc=='' || $scope.patientIdDoc==undefined){
        	 growl.warning(
					 	'If patient is walk in Please save Payment Entitlement first then upload doccument or please select patient from search',
					 	{
					 		title : 'warning!'
					 	});
					 return;
		    }
         if(self.model.patientIdDoc !=null ){
	    		 $scope.patientIdDoc = $scope.patientIdDoc;
	    	 }
    	 $scope.forView = false;
    	 $scope.encounterDetails={};
    		 $('#fileId').val(undefined);
    		$scope.encounterDetailsList=[];
    		$scope.encounterDetails.patientId = $scope.patientIdDoc;	
    		$scope.encounterDetails.visitTypeId = $scope.visitTypeId;	
    		$scope.encounterDetails.createdBy = $scope.userId;
    		$scope.encounterDetails.updatedBy = $scope.userId;
    		$scope.encounterDetails.unitId = $scope.unitId;
    		$scope.encounterDetails.organisationId = $scope.organizationId;
    		$scope.encounterDetails.uploadDoccumentDate=new Date();
    		if($scope.encounterDetailsList.length>0)
    		{
    		  $('#uploadDetailsNew').modal('show');
    		}
    	else{
    		 $('#uploadDetailsNew').modal('show');
    		var encounterDetails = angular.copy($scope.encounterDetails);
    		encounterDetails.patientId = $scope.patientIdDoc;
    		$scope.encounterDetailsList.push(encounterDetails);
    	}
    			
    	}
      
      $scope.addImageDetails = function()
		{ 
			var encounterDetails = angular.copy($scope.encounterDetails);
			$scope.encounterDetailsList.push(encounterDetails);
		};

	  $scope.removeImageDetails = function(deleteIndex)
		{ 
		   $scope.encounterDetailsList.splice(deleteIndex, 1);
	    }	
	  
	  
		$scope.uploadEncounterDocNew = function(Obj)
		{
			 var fd = new FormData();
		        fd.append('encounterId', 0);
		        fd.append('visitTypeId', Obj.visitTypeId);
		        fd.append('admissionId', 0);
		        fd.append('patientId',Obj.patientId );
		        fd.append('documentTypeId', Obj.documentTypeId);
		        fd.append('createdBy',Obj.createdBy );
		        fd.append('updatedBy', Obj.updatedBy);
		        fd.append('unitId', Obj.unitId);
		        fd.append('organisationId', Obj.organisationId);
		        fd.append('uploadDoccumentDate', moment(Obj.uploadDoccumentDate).format('DD-MM-YYYY HH:mm:ss'));
		        fd.append('file',Obj.file );
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
	  
		 $scope.uploadPatientImage = function()
		    {
			  $scope.finalDocUploadList=[];
			  var count=0;
			  $scope.forView = false;
			 angular.forEach($scope.encounterDetailsList, function(value, key) {  
				  if(value.documentTypeId==undefined || value.documentTypeId=="" || value.documentTypeId==null){
						 growl.warning("Please select document type .",
													{
														title : 'warning!'
													});
										   $rootScope.stopLoader();
						return false;
					}
				  if (angular.equals(value.file, {}) || value.file==undefined) {
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
						$scope.finalDocUploadList.push(value);
						 // console.log("final list",$scope.finalDoc);
			});
			 
			 if($scope.encounterDetailsList.length==$scope.finalDocUploadList.length){
				   $('#uploadDetailsNew').modal('hide');
			
			 $rootScope.startLoader();
			  $scope.count = 0;
			 angular.forEach($scope.encounterDetailsList, function(value, key) 
				{
					var promise = $scope.uploadEncounterDocNew(value);
					promise.then(function(response) 
							{
							  if(response.data.status == "success")
								  {
								    $scope.count++;
								    if($scope.encounterDetailsList.length==$scope.count)
									 {
									   growl.success("Document Uploaded Successfully.",
												{
													title : 'Success!'
												});
									   $scope.finalDocUploadList=[];
									   $scope.encounterDetailsList = [];
									   $rootScope.stopLoader();
									 }
							      }
							  else 
								{
									growl.error('Something went wrong.',
											{
												title : 'Error!'
											});
								}
						    });
						
				});
			 }
			
		    }

      $scope.reasonList = function () {
        var commonObj = {
          unitId : $scope.unitId,
          organizationId : $scope.organizationId
        };
        var URI = BASE_URL + ROOT_URL + GETACTIVEREASONLIST;
        BillingGenericService.serviceAction(METHOD_POST, URI,commonObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
          // console.log(response);
          if (response.data.status == "success")
          $scope.reasonList = response.data.listObject;
        });
      } 

      $scope.reasonList();

    $scope.defaultPatientCategoryId = cookieObjectAppConfig.defaultPatientCategoryId;

    $scope.patientClass = function () {
      if(($('#patientClassIdPopup').val() == '' || $('#patientClassIdPopup').val() == null || $('#patientClassIdPopup').val() == undefined) && ($('#patientClassCancelReasonIdPopup').val() == '' || $('#patientClassCancelReasonIdPopup').val() == null || $('#patientClassCancelReasonIdPopup').val() == undefined))
			{
        growl.warning("Please select patient class and reason.",{
          title : WARNING_MSG
        });
        return false;
			}
		  else if($('#patientClassIdPopup').val() == '' || $('#patientClassIdPopup').val() == null || $('#patientClassIdPopup').val() == undefined){
			  growl.warning("Please select patient class.",{
				  title : WARNING_MSG
			  });
			  return false;
		  }else if($('#patientClassCancelReasonIdPopup').val() == '' || $('#patientClassCancelReasonIdPopup').val() == null || $('#patientClassCancelReasonIdPopup').val() == undefined){
			  growl.warning("Please select reason.",{
				  title : WARNING_MSG
			  });
			  return false;
		  }
      else{
        if($scope.patientCategoryId == undefined){
          $scope.patientCategoryId = $scope.defaultPatientCategoryId;
        }
        if($scope.admissionId != null || $scope.encounterId != null){
          $rootScope.startLoaderPopup();
          $scope.patientClassIdPopup = $('#patientClassIdPopup').val();
          var obj={
            visitId:$scope.admissionId != null ? $scope.admissionId  :(!!$scope.encounterId ? $scope.encounterId : null),
            visitTypeId : $scope.visitTypeId,
            reasonIdForChangePatientClass : $scope.patientClassCancelReasonIdPopup,
            organizationId : $scope.organizationId,
            unitId : $scope.unitId,
            patientClassId: $scope.patientClassIdPopup
          }
          var URI = BASE_URL + ROOT_URL + "/api/billing/changePatientClassId";
          BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
          {	
            if(response.data.status == "success")
            {
              $rootScope.stopLoaderPopup();
                growl.success("Patient Class Updated Successfully.",
              												{
              													title : 'Success!'
              												});
              angular.forEach(self.model.walkInSale.items, function(obj){

                  if(obj.itemId != undefined && obj.itemId!= null && obj.itemId!= ""){

                   var data = {
                                    "itemId": obj.itemId,
                                    "encounterId": $scope.admissionId != null ? $scope.admissionId  :(!!$scope.encounterId ? $scope.encounterId : null),
                                    "patientCategoryId": $scope.patientCategoryId,
                                    "patientClassId": $scope.patientClassIdPopup,
                                    "entitlementId": $scope.entitlementObj.paymentEntitlementTypeId,
                                    "unitId": $scope.unitId,
                                    "organizationId": $scope.organizationId,
                                    "storeId":$cookies.getObject('cookieObject').storeId
                                  }

                                  var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API_WALKIN;
                                                  $rootScope.startLoader();
                                                  GenericService.serviceAction("POST", URI, data).then(function (response) {
                                                    $scope.patientClassPercentage = response.data.patientClassPercentage;
                                                    obj.patientClassPercentage = $scope.patientClassPercentage;
                                                    Calculate1(obj,self.model.walkInSale);
                                                  });
                  }

                $rootScope.stopLoader();
                angular.element('#change_patient_class').modal('hide');
                var popup = angular.element('#confirmCancelPatientClass');
                popup.modal('hide');
              });	
            }
          });
        }
        else{

          angular.forEach(self.model.walkInSale.items, function(obj){

            if(obj.itemId != undefined && obj.itemId!= null && obj.itemId!= ""){

                $scope.patientClassIdPopup = $('#patientClassIdPopup').val();

                        var data = {
                          "itemId": obj.itemId,
                          "encounterId": $scope.admissionId != null ? $scope.admissionId  :(!!$scope.encounterId ? $scope.encounterId : null),
                          "patientCategoryId": $scope.patientCategoryId,
                          "patientClassId": $scope.patientClassIdPopup,
                          "entitlementId": $scope.entitlementObj.paymentEntitlementTypeId,
                          "unitId": $scope.unitId,
                          "organizationId": $scope.organizationId,
                          "storeId": $cookies.getObject('cookieObject').storeId
                        }
                        var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API_WALKIN;
                        $rootScope.startLoader();
                        GenericService.serviceAction("POST", URI, data).then(function (response) {
                          $scope.patientClassPercentage = response.data.patientClassPercentage;
                          obj.patientClassPercentage = $scope.patientClassPercentage;
                          Calculate1(obj,self.model.walkInSale);
                        });


            }

          });
           $rootScope.stopLoader();
              growl.success("Patient Class Updated Successfully.",
                  {
                      title : 'Success!'
                  });
           var popup = angular.element('#change_patient_class');
                      popup.modal('hide');
        }
      }
    }

    $scope.cancelPatientClassClosePopup = function(){
      angular.element('#confirmCancelPatientClass').modal('hide');
    }

    $scope.cancelClass = function(){
      var popup = angular.element('#change_patient_class');
      popup.modal('hide');
      var popup = angular.element('#confirmCancelPatientClass');
      popup.modal('show');
    }

    $scope.cancelPatientClassPopup = function(){
      if($scope.patientCategoryId == undefined){
        $scope.patientCategoryId = $scope.defaultPatientCategoryId;
      }
       if($scope.admissionId != null || $scope.encounterId != null){
		    $rootScope.startLoaderPopup();
		    $scope.patientClassIdPopup = null;
		    $scope.patientClassCancelReasonIdPopup = null;
		    $scope.patientClassId =null;
	
        var obj={
          visitId:$scope.admissionId != null ? $scope.admissionId  :(!!$scope.encounterId ? $scope.encounterId : null),
          visitTypeId : $scope.visitTypeId,
          reasonIdForChangePatientClass : $scope.patientClassCancelReasonIdPopup,
          organizationId : $scope.organizationId,
          unitId : $scope.unitId,
          patientClassId:null
        }
        var URI = BASE_URL + ROOT_URL + "/api/billing/changePatientClassId";
        BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
        {	
          if(response.data.status == "success")
          {
            $rootScope.stopLoaderPopup();

             growl.success("Patient Class Cancelled Successfully.",
                              {
                                  title : 'Success!'
                              });
            $scope.patientClassId = null;
            angular.forEach(self.model.walkInSale.items, function(obj){
                 if(obj.itemId != undefined && obj.itemId!= null && obj.itemId!= ""){
                          var data = {
                            "itemId": obj.itemId,
                            "encounterId": $scope.admissionId != null ? $scope.admissionId  :(!!$scope.encounterId ? $scope.encounterId : null),
                            "patientCategoryId": $scope.patientCategoryId,
                            "patientClassId": null,
                            "entitlementId": $scope.entitlementObj.paymentEntitlementTypeId,
                            "unitId": $scope.unitId,
                            "organizationId": $scope.organizationId,
                            "storeId": $cookies.getObject('cookieObject').storeId
                          }
                          var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API_WALKIN;
                          $rootScope.startLoader();
                              GenericService.serviceAction("POST", URI, data).then(function (response) {
                                $scope.patientClassPercentage = response.data.patientClassPercentage;
                                obj.patientClassPercentage = $scope.patientClassPercentage;
                                Calculate1(obj,self.model.walkInSale);
                              });
                  }

            });
              $rootScope.stopLoader();
                          angular.element('#change_patient_class').modal('hide');
                          var popup = angular.element('#confirmCancelPatientClass');
                          popup.modal('hide');
          }
        });
      }
      else{
        angular.forEach(self.model.walkInSale.items, function(obj){

         if(obj.itemId != undefined && obj.itemId!= null && obj.itemId!= ""){
              var data = {
                "itemId": obj.itemId,
                "encounterId": $scope.admissionId != null ? $scope.admissionId  :(!!$scope.encounterId ? $scope.encounterId : null),
                "patientCategoryId": $scope.patientCategoryId,
                "patientClassId": null,
                "entitlementId": $scope.entitlementObj.paymentEntitlementTypeId,
                "unitId": $scope.unitId,
                "organizationId": $scope.organizationId,
                "storeId": $cookies.getObject('cookieObject').storeId
              }
                  var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API_WALKIN;
                  $rootScope.startLoader();
                  GenericService.serviceAction("POST", URI, data).then(function (response) {
                    $scope.patientClassPercentage = response.data.patientClassPercentage;
                    obj.patientClassPercentage = $scope.patientClassPercentage;
                    Calculate1(obj,self.model.walkInSale);
                  });
          }

        });

            growl.success("Patient Class Cancelled Successfully.",
                                        {
                                            title : 'Success!'
                                        });
          $rootScope.stopLoader();
          angular.element('#change_patient_class').modal('hide');
          var popup = angular.element('#confirmCancelPatientClass');
          popup.modal('hide');
      }
	  }

    
    
    
    var Calculate1 = function(item, bill){
// var discountAmount= (((bill.totalAmount) *
// item.discountPerItem)/100).toFixed(2);
      var data = {
          "mrp": item.mrp,
          "isTaxInc": $scope.taxInclusive,
          // "markup": !!item.markUpDown ? item.markUpDownPer : null,
          // "markdown": !item.markUpDown ? item.markUpDownPer : null,
                "markUpDownPer" : item.markUpDownPer,
          "markup": !!item.markUpDown ? item.markUpDownPer : null,
          "markdown": !item.markUpDown ? item.markUpDownPer : null,
          "discount":  item.discountPerItem ? item.discountPerItem : 0,
          "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
          "tax": item.taxPercent,
          "qty": item.quantity,
          "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
          "patientClassPercentage": item.patientClassPercentage,
          "patientClassAmount":item.patientClassAmount!=undefined && item.patientClassAmount!=null ? item.patientClassAmount:0
      }

      var response = commonDetailService.calculateAmount(data);
      item.rate = response.rate;
      item.taxAmount = response.taxAmount;
      item.responseTaxAmount =  response.taxAmount;
      item.unitDiscountAmount = response.unitDiscountAmount;
      item.itemDiscount =  response.discountAmount;
      item.itemtotalAmount = response.totalAmount;
      item.netAmount = response.netAmount;
      item.selfPayable = response.netAmount;
      item.creditPayable = response.creditPayable;
      item.packageConsumptionAmt = response.packageConsumptionAmt;
      item.patientClassAmount = response.patientClassAmount;
      $scope.patientClassAmount = response.patientClassAmount;
      item.unitPatientClassAmount = response.unitPatientClassAmount;
      if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
        $scope.isValidated = true;
        $('#validateButton').show();
      }
      else{
        $scope.isValidated = false;
        $('#validateButton').hide();
      }
      $scope.activePackageListWithoutForceClose=[];
		angular.forEach($scope.activePackageList, function (item, key)  {
			if(item.isForceClose==1)
				{
				$scope.activePackageListWithoutForceClose.push(item);
				}
		});
      if($scope.activePackageListWithoutForceClose!=null&& $scope.activePackageListWithoutForceClose.length>0){
        $scope.isValidated = true;
        $('#validateButton').show();
      }
    }
    
    $scope.medicationInstructions = [];
    $scope.getInstructionList = function(){
    	 $scope.medicationInstructions = [];
    	var commonObj = {
				isMedication : 'Y',
				organizationId : $scope.organizationId
			}

		var URI = BASE_URL + ROOT_URL+ GETMEDICATIONINSTRUCTIONS;
	
		BillingGenericService.serviceAction(METHOD_POST, URI,
						commonObj,
						NOTIFICATION_MSG_STATUS_FALSE).then(
						function(response) {
							// console.log(response);
							if (response.data.status == "success")
								$scope.medicationInstructions = response.data.listObject;
						});
    }
    
    $scope.getInstructionList();
    

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
    
	
	 $scope.razorPayObject={"razorMobileNo":null,"razorAmount":null,"razorPayRequired":true,"razorUniqueId":null,"razorPaidAmount":null};
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
						  "name":($scope.patientName != undefined && $scope.patientName != null) ? $scope.patientName : self.model.billDetailList.patientName,
						  
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
					  "name":($scope.patientName != undefined && $scope.patientName != null) ? $scope.patientName : self.model.billDetailList.patientName,
					  
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
						  "name":($scope.patientName != undefined && $scope.patientName != null) ? $scope.patientName : self.model.billDetailList.patientName,
						  "patientId": ($scope.patientId != undefined && $scope.patientId != null) ? $scope.patientId : self.model.billDetailList.patientId, 
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
						  "patientId": ($scope.patientId != undefined && $scope.patientId != null) ? $scope.patientId : self.model.billDetailList.patientId, 
						  
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
		
		/** *****For POS machine********** */
		$scope.getPOSMachineList=function(){
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
				$scope.objPosMachine.isSale = true;
				$scope.flagForSale=true;
				 $scope.flagForSMSpay=false;
				 $scope.flagForBharatQr=false;
				 $scope.objPosMachine.isBharatQr ="";
				 $scope.objPosMachine.isSMSpay=""
			}
			
		}
	 
	 $scope.getFlagBharatQr=function(){
		
			if($scope.objPosMachine.isBharatQr =="isBharatQr"){
				$scope.objPosMachine.isBharatQr = true;
				$scope.flagForBharatQr=true;
				 $scope.flagForSale=false;
				 $scope.flagForSMSpay=false;
				 $scope.objPosMachine.isSale ="";
				 $scope.objPosMachine.isSMSpay="";
			}
		}
	 $scope.getFlagSMSpay=function(){
			
			if($scope.objPosMachine.isSMSpay =="isSMSpay"){
				$scope.objPosMachine.isSMSpay = true;
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
// {

					/*
					 * "deviceid":($scope.objPosMachine.deviceName).toString(),
					 * "custUserId":$scope.custUserId,
					 * "password":$scope.password,
					 * "custMobileNo":$scope.patientDetails.mobileNumber,
					 * "invoice":Date.now().toString(),
					 * "amount":$scope.paymentDetails.credAmount,
					 * "transactionType":"Sale" }
					 */
							{
								  "userName": $scope.custUserId,
								  "password": $scope.password,
								  "customerMobileNo":$scope.objPosMachine.mobileNumberForPOS != "" ? $scope.objPosMachine.mobileNumberForPOS :$scope.mobileNumber,
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
							  "customerMobileNo":$scope.objPosMachine.mobileNumberForPOS != "" ? $scope.objPosMachine.mobileNumberForPOS :$scope.mobileNumber,
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
							  "customerMobileNo":$scope.objPosMachine.mobileNumberForPOS != "" ? $scope.objPosMachine.mobileNumberForPOS :$scope.mobileNumber,
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
						patientId:($scope.tempPatId != undefined || $scope.tempPatId != null || $scope.tempPatId != "") ? $scope.tempPatId : $scope.walkInPatient,
						transactionAmount:parseInt($scope.paymentDetails.credAmount),
						organizationId:$scope.organizationId,
						unitId:$scope.unitId,
						createdBy:$scope.userId,
						updatedBy:$scope.userId,
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
		 /** ****************** */
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
					  "name":($scope.patientName != undefined && $scope.patientName != null) ? $scope.patientName : self.model.billDetailList.patientName,
					  "patientId":($scope.patientId != undefined && $scope.patientId != null) ? $scope.patientId : self.model.billDetailList.patientId, 
					  "createdBy": $scope.userId, 
					  "updatedBy": $scope.userId,
					  
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
	
	$scope.clearPaymentList = function(){
		  $scope.listPaymentDetails = [];
		  $scope.validatTextFieldFlag = true;
		  $scope.calculateTotalDueRefAmount();
		  ClearFields(self.model.walkInSale);
		  $scope.saleTypeIdAgainstEncounter = 3;
          $scope.patientSaleTypeIdFlag = false; 
	}

  $scope.calculateReceivedQty = function (item) {
    if(item.quantity == undefined || item.quantity == null || item.quantity == ""){
      item.quantity = 0;
    }
    if(item.isThreeUOMApplicable == false){
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
    else{
      if(item.selectedUom.uomTypeId == 1){
        item.receivedQtyCon = (item.quantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
        item.receivedQtyCalculation = (item.quantity * item.uom[0].conversion * item.uom[1].conversion);
        item.uomUnitData = item.uom[2].uomUnit;
      }
      else if(item.selectedUom.uomTypeId == 2){
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
	 $scope.kitItemDetails =  function(){
		 $scope.kitMasterItemsList = [];

         var URI = BASE_URL + ROOT_URL + "/api/units/kit/master/" + $scope.kitId + "/" + $cookies.getObject('cookieObject').storeId+ "/" +$scope.unitId ;
          $rootScope.startLoader();
          GenericService.serviceAction("GET", URI, {}).then(
              function (response) {
            	 
            	  $scope.kitMasterItemsList = response.data.listObject;
            	  angular.element('#itemDetailsModalWalkIn').modal('show');
                  $rootScope.stopLoader();
              },
              function (err) {
                  $rootScope.stopLoader();
              });
      
	 }
  
	
  }
  
  function config($stateProvider) {
    $stateProvider.state("walkInSales", {
      url: "/walkInSales",
      templateUrl: 'views/PharmacyInventory/InPatient/walk-in-sales.html',
      controller: "WalkInSales.Controller",
      title: 'Walk In Sales',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 114);
				 	});
					if(arr.length <= 0){
						$state.go("accessDenied")
						deferred.reject();
					 } else {
						deferred.resolve();
					 }
		        });
		
		        return deferred.promise;
		      }
		 },
      controllerAs: "vm"
    });
  }

  angular.module("myApp")
    .config(config)
    .controller("WalkInSales.Controller", walkInSalesController);

})();

angular.module("myApp").directive('focusMe', function($timeout) {
	  return {
	    scope: { trigger: '=focusMe' },
	    link: function(scope, element) {
	      scope.$watch('trigger', function(value) {
	        if(value === true) { 
	          // //console.log('trigger',value);
	          // $timeout(function() {
	            element[0].focus();
	            scope.trigger = false;
	          // });
	        }
	      });
	    }
	  };
	});
