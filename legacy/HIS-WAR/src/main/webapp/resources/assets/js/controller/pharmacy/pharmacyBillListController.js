(function () {
  "use strict";

  function pharmacyBillListController($scope, $rootScope, $http, CONSTANTS, reportService, GenericService, PaginationService, $filter, growl, CurrencyService, screenDataService, pharmacyPaymentCollFactory, BillingGenericService, $cookies,$location,loyaltiCardCallFactory,$interval,GenericServiceFileUpload,$window) {
    var self = this;
    $scope.mpesaRigths = JSON.parse(localStorage.getItem('mpesaRigths'));


    // Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
    function initializeController() {

      
      $rootScope.loginpage = true;
      $scope.page = "billList";
      $scope.dateOptions = {
        formatYear: 'yyyy',
        showWeeks: false,

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
      $scope.dateOptionsTrancs = {
  			formatYear : 'yyyy',
  			showWeeks : false,
  			minDate : new Date()
  		};
      self.model = {
        fromDate: new Date(),
        toDate: new Date(),
        fromDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate: new Date(),
//          minDate : moment(new Date()).subtract(180, 'days').toDate()
        },
        toDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate: new Date()
        },
        salesTypeList: [],
        	
        	
        statusDataList: [],
        salesTypeId: null,
        userId: null,
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
        }
      };
//      self.model.salesTypeList.push({value:1, type:'All'});
//      self.model.salesTypeList.push({value:1, type:'OP'});
//      console.log("salestypelist",self.model.salesTypeList);
      var cookieObject = {};
      var cookieObjectAppConfig = {};

      [cookieObject, cookieObjectAppConfig] = $rootScope.checkCookies();
      self.model.userId = cookieObject.userId;

      $scope.skipMpesaValidation = false;
      var cookieObject = $cookies.getObject('cookieObject');
      if (cookieObject == undefined) {
        $state.go('login');
        return;
      }
      $scope.cashCollectedAmount = 0;
      var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
      var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
      if (cookieObjectAppConfigSecond != undefined) {
	      $scope.cashAmountValidation =  cookieObjectAppConfig.cashAmountValidation;
	      $scope.cashAmountLimit =  cookieObjectAppConfig.cashAmountLimit;
	      $scope.payGatewayProviderId = cookieObjectAppConfig.payGatewayProviderId;
	      $scope.depositTypeWiseRequired =  cookieObjectAppConfig.depositTypeWiseRequired;
	      $scope.exceededBillAmountSettlement=cookieObjectAppConfigSecond.exceededBillAmountSettlement;
      }
      if (cookieObjectAppConfig == undefined) {
        $state.go('login');
        return;
      } else {
        // console.log('cookieObjectAppConfig', cookieObjectAppConfig);

      }
      $scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
      $scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
      $scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
      $scope.mpesaRequired = cookieObject.mpesaRequired;
      $scope.excessAmount = parseFloat(0).toFixed(2);	
      $scope.unitId = cookieObject.unitId;
      $scope.userId = cookieObject.userId;
      $scope.organizationId = cookieObject.organizationId;
      $scope.userId = $scope.userId;
      $scope.transferToActiveDeposit = "N";
      $scope.validatTextFieldFlag = true;
      $scope.hitMpesaApi = false;
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
      $scope.fileObj = {};
		$scope.fileObj.file = null;
  		$scope.isPinPadRequired = cookieObjectAppConfig.isPinPadRequired;
  		$scope.registeredPinPadDevice= cookieObjectAppConfig.registeredPinPadDevice;
  		// $scope.pinpadAmounFlag = false;

      $scope.appointmentId = 0;
      $scope.encounterId = 0;

      // vivek added
      $scope.payeeId = '';
      $scope.consumedAmt = parseFloat(0);
      $scope.receivedFrom = "";
      $scope.identificationNumber = "";
      $scope.DepositObj = {};
		$scope.DepositObj.userEnterDepositAmount = 0;
      $scope.depositeList = {
        depositeIds: [1]
      };

      $scope.depositeDisabledFlag = false;

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
      
      //Methods
      CurrencyService.GetCurrency().then(function (currency) {
        self.model.Currency = currency;
      })
      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.SearchpatientNames = SearchpatientNames;
      self.model.SearchUhid = SearchUhid;
      self.model.AddUHID = AddUHID;
      self.model.search = '';
      self.model.prevSearch = '';
      self.model.patientNames = [];
      self.model.billListForClaims = [];
      self.model.GetBillDetails = GetBillDetails;
      self.model.getClaimDetails = getClaimDetails;
      self.model.sendToSmartCard = sendToSmartCard;
      self.model.GetBillList = GetBillList;
      self.model.PatientNameKeyPress = PatientNameKeyPress;
      self.model.uHIDKeyPress = uHIDKeyPress;
      self.model.initialCall = initialCall;
      self.model.settlementClick = settlementClick;
      self.model.rePrintBillSlip = rePrintBillSlip;
      self.model.cancelBill = cancelBill;
      self.model.GetGenericMaster = GetGenericMaster;
      self.model.GetStrengthUnitList = GetStrengthUnitList;
      self.model.ViewCombinationDetails = ViewCombinationDetails;
      self.model.cancelBillPopup = cancelBillPopup;
      self.model.ModalDismiss = ModalDismiss;

      PopulateDropdown();
    }

    var OpenFromDate = function () {
      self.model.fromDateOpened = true;
      self.model.toDateOptions.minDate = self.model.fromDate;
    }

     $scope.selectedDate = new Date();
     $scope.sixMonthsLater = new Date();

     $scope.calculateSixMonthsLater = function() {
         var selectedMoment = moment(self.model.fromDate);
         var curMoment = moment(); // Use moment.js for handling dates and time
         var diff = curMoment.diff(selectedMoment, 'months', true); // Calculate the difference in months with decimal precision
           if(diff>=6)
                 {
                 if (selectedMoment.isValid()) {
                     $scope.sixMonthsLater = selectedMoment.add(6, 'months').toDate();
                      $scope.sixMonthsLater = selectedMoment.subtract(1, 'day').toDate();
                       self.model.toDate=$scope.sixMonthsLater
                 }
                 }
                 if(diff<6)
                 {
                 self.model.toDate=new Date();
                 }
     }

    var OpenToDate = function () {
      self.model.toDateOpened = true;
      self.model.toDateOptions.minDate = self.model.fromDate;
    }

    var SearchpatientNames = function (search) {

      if (search.length < 3) return;
      var data={
      		unitId : $scope.unitId,
      		firstName : search,
//      		lastName : search
      };
      if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 3)) {
        self.model.prevSearch = search;
//        var URI = CONSTANTS.GLOBAL.PATIENT_SEARCH_API + $scope.unitId + "/" + search + "/" + 1;
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

    var PatientNameNotFound = function (search) {
      var item = {
        "itemFound": false,
        "details": "Patient Not found with name " + search
      };
      self.model.patientNames = [item];
      return self.model.patientNames;
    }

    var AddUHID = function (item) {
      self.model.patientData = item;
      self.model.uhid = item.uhid;
      self.model.patientId = item.patientId;
      self.model.patientName = item.patientName;
      self.model.patientIdFromService = item.patientId;
      self.model.patientNameFromService = item.patientName; 
      self.model.uhidFromService= item.uhid;
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

    var uHIDKeyPress = function (event) 
    {
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

    var PopulateDropdown = function () {
      var URI = CONSTANTS.PHARMACY.OP.BILL_LIST.DROPDOWN_API;
      self.model.saleType = "0";
      self.model.status ="0";
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          // response.data.salesType.unshift({
          // "type": "All"
          // });
          // response.data.status.unshift({
          // "status": "All"
          // });
          self.model.salesTypeList = self.model.salesTypeList.concat(response.data.salesType);
		  angular.forEach(response.data.status, function(val){
			  if(val.status == 'Partially Completed'){
				  val.status = 'On Hold';
			  }
		  });
          self.model.statusDataList = self.model.statusDataList.concat(response.data.status);
          console.log("salestypelist",self.model.salesTypeList);
          $rootScope.stopLoader();

          initialCall();

        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

   
    var initialCall = function () {
      if (!screenDataService.getScreenData()) {
        GetBillList();
      } else {
        screenDataService.getScreenData().forEach(function (dataobj) {
          if (dataobj.screenName == 'pharmacyBillList') {
            var requestData = dataobj.searchrequest;
            self.model.saleType = self.model.salesTypeList.find(function (saleType) {
              return requestData.saleType.id == saleType.id;
            });
            self.model.status = self.model.statusDataList.find(function (status) {
              return requestData.status.id == status.id;
            });
            self.model.patientId = requestData.patientId
            self.model.patientName = requestData.patientName
            self.model.billNo = requestData.billNo
            self.model.visitNo = requestData.visitNo
            self.model.uhid = requestData.wardName
            self.model.fromDate = new Date(requestData.fromDate);
            self.model.toDate = new Date(requestData.toDate);
          }
        });
        GetBillList();
      }
    }

    $scope.userRolePharmacyObj = {};
    $scope.userRolePharmacy = function(){
    	
    	var URI = BASE_URL + ROOT_URL
		+ "/api/global/getUserRoleAccess/"+$scope.userId+"/"+$scope.unitId;
		BillingGenericService.serviceAction(METHOD_GET, URI,"",
				NOTIFICATION_MSG_STATUS_FALSE)
		.then(function(response) {
					console.log(response);
					// $rootScope.startLoader();
					if (response.data.status == "success")
						{
						
						if(response.data.listObject!=null && response.data.listObject.length>0){ 
							 $scope.userRolePharmacyObj = 	response.data.listObject[0];
						}
							 
						}
						
				});

    	
    }
    $scope.userRolePharmacy();

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

    var GetBillList = function () {
      if (ValidateDate()) {
        var bills = self.model;
        var URI = CONSTANTS.PHARMACY.OP.BILL_LIST.SEARCH_API;

/*        var data = {
          "fromDate": moment(bills.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(bills.toDate).format('YYYY-MM-DD'),
          "uhid": !!bills.uhid ? bills.uhid : undefined,
          "patientId": !!bills.patientId ? bills.patientId : undefined,
          "patientName": !!bills.patientName ? bills.patientName : undefined,
          "salesTypeId": !!bills.saleType && !!bills.saleType.id ? bills.saleType.id : undefined,
          "billNo": !!bills.billNo ? bills.billNo : undefined,
          "visitNo": !!bills.visitNo ? bills.visitNo : undefined,
          "statusId": !!bills.status && !!bills.status.id ? bills.status.id : undefined
        }*/
        /*self.model.patientNameFromService=item.id;
        self.model.patientNameFromService= item.patientName; 
        self.model.uhidFromService= item.uhid;*/
        var data = {
                "fromDate": moment(bills.fromDate).format('YYYY-MM-DD'),
                "toDate": moment(bills.toDate).format('YYYY-MM-DD'),
                "uhid": !!bills.uhid ? bills.uhid : self.model.uhidFromService,
                "patientId": !!bills.patientId ? bills.patientId : self.model.patientIdFromService,
                "patientName": !!bills.patientName ? bills.patientName : self.model.patientNameFromService,
                "doctorName": !!bills.doctorName ? bills.doctorName : undefined,
                "salesTypeId": !!bills.saleType && !!bills.saleType.id ? bills.saleType.id : self.model.saleType,
                "billNo": !!bills.billNo ? bills.billNo : undefined,
                "visitNo": !!bills.visitNo ? bills.visitNo : undefined,
                "statusId": !!bills.status && !!bills.status.id ? bills.status.id : self.model.status,
                 "unitId":$scope.unitId,
                 "storeId":$scope.storeId

              }
        
        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            $rootScope.stopLoader();
            let tempList = [];
            for(let bill of response.data) {
            //  if(bill.salesType != "IP") {
            // if(bill.salesType != "IP" || (bill.salesType == "IP" && bill.status=="Completed")) {
            	angular.forEach($scope.reasonList,function(value,key)
      					{
            				if(bill.cancelledReasonId==value.reasonMasterId)
            					{
            					bill.reasonDesc=value.reasonDesc;
            					}
            				if(bill.cancelledRemark==undefined || bill.cancelledRemark==null || bill.cancelledRemark=="null")
            					{
            					bill.cancelledRemark="";
            					}
      					});
                var totalAmount=0;
                var netAmount=0;
               /* for(let bill1 of bill.items) {
                	totalAmount=totalAmount+bill1.totalAmount;
                	netAmount=netAmount+bill1.netAmount;
                }*/
                bill["totalAmount1"]=bill.totalAmount;
                bill["netAmount1"]=bill.netAmount;
                tempList.push(bill);
              // }
              if(bill.patientDetails == undefined || bill.patientDetails == '' || bill.patientDetails == null){
                bill.patientDetails = bill.patientName+" | "+bill.genderCode +" | "+0 ;
              }
            }
            self.model.pagination.data=[];
            bills.bills = tempList;
            if(!!response && !!response.data && response.data.length > 0){            
              PaginationService.searchData(bills.bills, self.model.pagination);
            }
            self.model.patientId = undefined;
            $rootScope.stopLoader();
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
            self.model.patientId = undefined;
            growl.error('Error');
          });
        // var screenData = {
        //   searchrequest: {
        //     "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
        //     "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
        //     "uhid": self.model.uhid,
        //     "patientId": self.model.patientId,
        //     // "visitType": self.model.visitType,
        //     "patientId": self.model.patientId,
        //     "patientName": self.model.patientName,
        //     "saleType": self.model.saleType,
        //     "billNo": self.model.billNo,
        //     "visitNo": self.model.visitNo,
        //     "status": self.model.status
        //   },
        //   screenName: 'pharmacyBillList',
        // }
        // screenDataService.setScreenData(screenData);

      }
    }
    $scope.amountPaid ="";
    $scope.balanceAmount = 0;
    $scope.patientSaleTypeId = undefined;
    $scope.totalSelfPayable =0;
	  $scope.totalCreditPayable =0;
	  $scope.pharmacyReceiptList  = [];
	 $scope.getReceiptDetails = function(saleId){
		 
			var URI = BASE_URL + ROOT_URL + "/api/billing/Pharmacy/payee/bill/"+saleId;
			BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			
				if (response.data.status == "success")
					{
						$scope.pharmacyReceiptList  = [];
						$scope.pharmacyReceiptList = response.data.listObject;
					
					}				
							
					});
		 
	 } 
	 
	 
	 $scope.printOPDBillReceipt = function(bill){
		 
		 var data = {
			        "appName": "HIS",
			        "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
			        "reportDesignFilePath": "pharmacyBillReceipt.rpt",
			        "reportFormat": "PDF",
			        "exportFileName": "BillSlip",
			        "reportParams": [
			          {
			            "paramName": "Id",
			            "paramType": "Integer",
			            "paramValue": bill.paymentRecieptId
			          },
			          {
				            "paramName": "ReceiptType",
				            "paramType": "Integer",
				            "paramValue": bill.receiptType
				       }
			        ]
			      };
			      reportService.generate(REPORT_API_URI1, data);
		 
		 
		 
	 }
	 
	 $scope.saveBtnDisabled=false;
	 $scope.cancelBillReceipt = function(info){
		    $scope.saveBtnDisabled=true;
			$rootScope.startLoader();
				var data = {
						  "amount" : info.amount,
						  "paymentRecieptId" : info.paymentRecieptId,
						  "receiptType" : info.receiptType,
						  "payeeId" :  $scope.selfPayeeId,
						  "saleId" :  $scope.saleId,
						  "createdBy": $scope.userId,
				}
				var URI = BASE_URL + ROOT_URL + "/api/billing/cancelPharmacyReceipt";
				BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success")
					{
							
						$rootScope.stopLoader();
						var popup = angular.element('#billListDetails');
				          popup.modal('hide');
						growl.success("Receipt cancelled successfully",{
							title : "SUCCESS!"
						});
						
							$scope.getReceiptDetails($scope.saleId);
							GetBillList();
							
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
	  $scope.saleId = 0;
    var GetBillDetails = function (bill) {
    	$scope.saveBtnDisabled=false;
    	$scope.pharmacyReceiptCancellation1();
      $scope.skipMpesaValidation = false;
      $scope.totalSelfPayable =0;
	  $scope.totalCreditPayable =0;
      $scope.pinpadAmounFlag = false;
      $scope.saleId = bill.id;
      $scope.getReceiptDetails(bill.id);
     if(bill.mobileNumber != null ){
    	 $scope.mobileNumber = parseInt(bill.mobileNumber);
    	 $scope.countryCallingCode = parseInt(bill.countryCallingCode);
    	 $scope.razorPayObject.razorMobileNo = bill.mobileNumber;
         $scope.objPosMachine.mobileNumberForPOS =bill.mobileNumber;

     } 
     $scope.totalSelfPayable =bill.totalSelfPayable;
	
      var bills = self.model;
      var URI = CONSTANTS.PHARMACY.OP.BILL_LIST.DETAILS_API + $scope.unitId + "/"+$scope.selfPayeeId + "/" + bill.id;
      bill.billDetailList = [];

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          $rootScope.stopLoader();
          bills.billDetailList = response.data;
          $scope.balanceAmount = response.data.balanceAmt>0 ?  response.data.balanceAmt : 0;
          $scope.totalCreditPayable = response.data.creditDueAmount;
          if(bills.billDetailList==undefined ||   bills.billDetailList.patientDetails==undefined)
        	  {
        	  bills.billDetailList.patientDetails = bill.patientDetails;
        	  }
          
//          if(bills.billDetailList.discount){
//            angular.forEach(bills.billDetailList.items, function(d) {
//              d.discountPercentage = bills.billDetailList.discount;
//            });
//          }
          
          if(response.data.registrationTypeId == undefined){
           	 $scope.patientSaleTypeId = 3;
           }else{
           	   $scope.patientSaleTypeId = 2
           }
          $scope.consolidatedBill = bills.billDetailList.consolidatedBill;
          $scope.validatTextFieldFlag = true;
          loyaltiCardCallFactory.getLoyaltyCardList(bill.patientId,$scope);
          $scope.availableDeposits = [];
        	  
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
        	  
  		
          
      		$scope.amountPaid = response.data.paidAmt
          var popup = angular.element('#billListDetails');
          popup.modal('show');
        },
        function (err) {
          $rootScope.stopLoader();
        });
      
    }
    
  //Ganesh Start
	$scope.checkAciveDepo  = function(useActiveDepo)
	{
		$scope.addBtnDisabled = false;
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
			 $scope.availableDeposits[idx].remark = $scope.DepositObj.remark;
				if($scope.dueAmount == 0)
				{
					$scope.availableDeposits[idx].depositeFlag = 'N';
				}
				else if($scope.dueAmount >= item.availableDeposit)
				{
					$scope.availableDeposits[idx].consumedDeposit  = parseFloat(parseFloat($scope.availableDeposits[idx].consumedDeposit)  + parseFloat($scope.DepositObj.userEnterDepositAmount)).toFixed(2)// parseFloat(userEnterDepositAmount);
					$scope.availableDeposits[idx].availableDeposit = parseFloat(parseFloat($scope.availableDeposits[idx].availableDeposit).toFixed(2) - parseFloat($scope.DepositObj.userEnterDepositAmount).toFixed(2)).toFixed(2);
					$scope.dueAmount = (parseFloat($scope.dueAmount)) - parseFloat($scope.DepositObj.userEnterDepositAmount).toFixed(2);
				}
			else if($scope.dueAmount <= item.availableDeposit)
				{
					$scope.availableDeposits[idx].availableDeposit = parseFloat(parseFloat(item.availableDeposit).toFixed(2) - parseFloat($scope.DepositObj.userEnterDepositAmount).toFixed(2)).toFixed(2);
					$scope.availableDeposits[idx].consumedDeposit =parseFloat(parseFloat(item.consumedDeposit) + parseFloat($scope.DepositObj.userEnterDepositAmount)).toFixed(2);
					$scope.dueAmount = (parseFloat($scope.dueAmount))- parseFloat($scope.DepositObj.userEnterDepositAmount).toFixed(2);
				}
				$scope.DepositObj.remark="";  
				$scope.addBtnDisabled = true;
			}
		if(item.depositeFlag == 'N')
		{
			 $scope.availableDeposits[idx].remark = $scope.DepositObj.remark;
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

//		$scope.addBtnDisabled = false;

		
	}
    
    $scope.depositeObject = undefined;
	$scope.depositeIndex = undefined;
	$scope.depositeSubPopup = function(item,idx){
		$scope.addBtnDisabled = false;
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
	
    
    var getClaimDetails = function (bill) 
    {
        var bills = self.model;
        var URI = CONSTANTS.PHARMACY.OP.BILL_LIST.DETAILS_API + $scope.unitid + "/"+$scope.defaultSelfTariffId + "/" + bill.id;
        bill.billDetailList = [];

        $scope.claimBill = {};
        $scope.claimBill = bill;
        
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
        	self.model.billListForClaims = [];
            $rootScope.stopLoader();
            
            bills.billDetailList = response.data;
            bills.radio = bills.billDetailList.discountTypeId;
            self.model.billListForClaims = response.data.items;
            
            $scope.totalSelfPayableAmtClaim = 0;
            $scope.totalCreditPayableAmtClaim=0;
            $scope.totalNetPayableAmt=0;
            angular.forEach(self.model.billListForClaims, function(value, key) 
            	{
            	  $scope.totalSelfPayableAmtClaim=  $scope.totalSelfPayableAmtClaim+parseFloat(value.selfPayable);
				  $scope.totalCreditPayableAmtClaim=  $scope.totalSelfPayableAmtClaim+parseFloat(value.creditPayable);
				  $scope.totalNetPayableAmt=  $scope.totalSelfPayableAmtClaim+parseFloat(value.netAmount);
            	});
            
            console.log('bill',JSON.stringify(bill));
            var popup = angular.element('#billListDetailsForClaim');
            popup.modal('show');
          },
          function (err) {
            $rootScope.stopLoader();
          });
      }
    
    var sendToSmartCard = function()
    {
    	$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL + "/api/global/patient/billing/"+$scope.claimBill.patientId;
		BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success"){
				$scope.patientDetailsListForClaim = response.data.listObject;

				var data = {
						billDate : moment($scope.claimBill.billDate).format('DD-MM-YYYY HH:MM:SS'),
						billNumber : $scope.claimBill.billNo,
						billingMasterId : $scope.claimBill.id,
						visitDate : '',
						visitNo : '',
						visitId : '',
						uhidNumber : $scope.patientDetailsListForClaim[0].uhidNumber,
						patientId : $scope.claimBill.patientId,
						visitTypeId : '',
						totalSelfPayableAmtClaim : $scope.totalSelfPayableAmtClaim,
						totalCreditPayableAmtClaim : $scope.totalCreditPayableAmtClaim,
						totalNetPayableAmount : $scope.totalNetPayableAmt,
						firstName : $scope.patientDetailsListForClaim[0].firstName,
						middleName : $scope.patientDetailsListForClaim[0].middleName,
						lastName : $scope.patientDetailsListForClaim[0].lastName,
						dob : $scope.patientDetailsListForClaim[0].dob,
						genderName :$scope.patientDetailsListForClaim[0].genderName,
						unitId:$scope.unitId,
						organizationId:$scope.organizationId,
						pharamcy:true
				}
				
				var saveOrderDetailsObj = [];
				angular.forEach(self.model.billListForClaims, function (value, key) {
					var saveArr = {};
					saveArr.netAmount = value.netAmount;
					saveArr.itemName = value.itemName;
					saveArr.code = value.code;
					saveArr.itemId = value.itemId;
					saveArr.quantity = value.quantity;
					saveOrderDetailsObj.push(saveArr);
				});
				
				if($scope.totalSelfPayableAmtClaim > 0)
					{
					var saveArr = {};
					saveArr.netAmount = parseFloat(0)-parseFloat($scope.totalSelfPayableAmtClaim);
					saveArr.quantity = "1";
					saveArr.itemName ="COPAY";
					saveArr.code ="COPAY";
					saveOrderDetailsObj.push(saveArr);
					}
				data.items = saveOrderDetailsObj;
				
				console.log("data",data);
				
				var URL = BASE_URL + ROOT_URL+"/api/billing/submitSmartCard";
				GenericService.serviceAction("POST", URL,data).then(function(response) {
					if(response.data.status == "success"){
						
						growl.success("Data sent to smartcard system.",{
							title : "SUCCESS!"
						});
						 $rootScope.stopLoader();
						 $('#billListDetailsForClaim').modal('hide');
						}else if(response.data.status == "error")
						{
							growl.warning("There is no data available!",{
							title : ERROR_MSG
							
						});
							 $rootScope.stopLoader();
					    }
				});
				
			}
		});
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
      $scope.status = '';

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
    }

    $scope.initTotalAmounts = function () {
      $scope.totalPaymentAmount = parseFloat(0).toFixed(2);
      $scope.dueAmount = parseFloat(0).toFixed(2);
      $scope.refundAmount = parseFloat(0).toFixed(2);
      $scope.totalAmountSummary = parseFloat(0).toFixed(2);
      $scope.totalConcessionSummary = parseFloat(0).toFixed(2);
      $scope.netPayableSummary = parseFloat(0).toFixed(2);
      $scope.totalPayingAmount = parseFloat(0).toFixed(2);
    }

    var settlementClick = function () {
    	setTimeout(function(){
          	 $('#setCash').trigger('click')
           }, 300);
    	$scope.checkSave=0;
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
//		$scope.setFirstTabLevel(1);
      $scope.payeeDueAmtStatic = parseFloat(0).toFixed(2);
      $scope.paymentDetails.cashAmount = parseFloat(0).toFixed(2);
      $scope.dueAmount = parseFloat(0).toFixed(2);
      $scope.totalAmount = parseFloat(0).toFixed(2);
      $scope.payeeDueAmtFinal = parseFloat(0).toFixed(2);
      $scope.listPaymentDetails = [];
      var popup = angular.element('#collectPaymentDetails');
      $scope.transferToActiveDeposit = 'N'
	  $scope.excessAmount = parseFloat(0).toFixed(2);
	  $scope.send=false;
	  $scope.resend=false;
      $scope.billDetails();
      $scope.calculateTotalDueRefAmount();
      
      if($scope.consolidatedBill==false)
    	  {
    	     popup.modal('show');
    	  }
      else{
    	  growl.warning("Bill is Consolidated. Please settle through OP bill list.",{
				title : ERROR_MSG
				
			});
    	  return false;
      }
      
    }

    
	//Ganesh Start
    $scope.BalanceAmount;
	$scope.calculateTotalDueRefAmount = function()
	{
		var amount = 0;
		var dueAmount = $rootScope.parseFloatNum($scope.dueAmount);
		$scope.BalanceAmount=dueAmount;
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
		$scope.BalanceAmount=$scope.payeeDueAmtStatic;
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
    
 /*   $scope.calculateTotalDueRefAmount = function () 
    {
      var amount = 0;
      var payeeDueAmtFinal = $rootScope.parseFloatNum($scope.payeeDueAmtFinal);
      angular.forEach($scope.listPaymentDetails, function (value, key) {
        amount = amount + parseFloat(value.amount);
      });
      var tempDueAmt = $scope.payeeDueAmtStatic - amount - $scope.consumedAmt;
      var temppayeeDueAmtFinal = $scope.payeeDueAmtStatic - amount - $scope.consumedAmt;
      $scope.payeeDueAmtFinal = temppayeeDueAmtFinal >= 0 ? temppayeeDueAmtFinal : 0;

      $scope.payeeDueAmtFinal = parseFloat($scope.payeeDueAmtFinal).toFixed(2);
      var df = (parseInt($scope.payeeDueAmtStatic) - (parseInt(amount) + parseInt($scope.consumedAmt)));
      if (df > 0) {
        $scope.dueAmount = tempDueAmt >= 0 ? tempDueAmt : 0;
        $scope.paymentDetails.cashAmount = $rootScope.parseFloatNum($scope.dueAmount);
      }
      else {
        $scope.dueAmount = parseInt($scope.billDueAmount) - parseInt($scope.payeeDueAmtStatic);
        $scope.paymentDetails.cashAmount = $rootScope.parseFloatNum($scope.dueAmount);
      }
      // Ganesh
      $scope.paymentDetails.cashAmount = parseFloat($scope.dueAmount).toFixed(2);
      $scope.paymentDetails.chequeAmount = parseFloat($scope.dueAmount).toFixed(2);
      $scope.paymentDetails.credAmount = parseFloat($scope.dueAmount).toFixed(2);
      $scope.eTransferObj.transferAmount= parseFloat($scope.dueAmount).toFixed(2);
      
      $scope.refundAmount = temppayeeDueAmtFinal >= 0 ? 0 : Math.abs(temppayeeDueAmtFinal);
      $scope.amountCollected = amount;
    }*/

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
		/*$scope.resend = (flag=='N')?true:false;
		$scope.send =false;*/
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
	
	
	/* * *************************************loylti card Integration * END****************************************** */
    
	$scope.getCashCollectedAmount = function(visitId,id){

		$rootScope.startLoader();
			
			var data = {};
			
			if(visitId ==1 ){
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
	
	
    $scope.isBillList = 0;
	
    $scope.selectedUhid = "";
    $scope.selectedPatientId = "";
    $scope.billDetails = function () {
      let bill = self.model.billDetailList;
       console.log("BILL", bill);
      $scope.status = bill.status;
      $scope.billDisabled = bill.billStatusId == 3 ? true : false;
      $scope.billId = bill.id;
      $scope.totalAmount = bill.netAmount;
      $scope.selectedUhid = bill.uhid;
      $scope.selectedPatientId = bill.patientId;
      $scope.transferToActiveDeposit = 'N'
	  $scope.excessAmount = parseFloat(0).toFixed(2);
	  $scope.send=false;
	  $scope.resend=false;
	  $scope.validatTextFieldFlag = true;
  	  loyaltiCardCallFactory.getLoyaltyCardList(bill.patientId,$scope);
  	
  /*
	 * $scope.dueAmount = bill.balanceAmt; $scope.payeeDueAmtFinal =
	 * bill.balanceAmt; $scope.payeeDueAmtStatic = bill.balanceAmt;
	 * $scope.oldDueAmount = bill.balanceAmt; $scope.billDueAmount =
	 * bill.balanceAmt;
	 */
      
      $scope.dueAmount = $scope.balanceAmount;
      $scope.payeeDueAmtFinal = $scope.balanceAmount;
      $scope.payeeDueAmtStatic = $scope.balanceAmount;
      $scope.oldDueAmount = $scope.balanceAmount;
      $scope.billDueAmount = $scope.balanceAmount;
  	  
  	  // changed discuss with purva, divyesh
  	  //  $scope.dueAmount = bill.netAmount;
      //  $scope.payeeDueAmtFinal = bill.netAmount;
      //  $scope.payeeDueAmtStatic = bill.netAmount;
      //  $scope.oldDueAmount = bill.netAmount;
      //  $scope.billDueAmount = bill.netAmount;
       
      
      $scope.currentBill = bill;
      $scope.admissionId = bill.admissionId !=undefined  ? bill.admissionId : null;
      $scope.isFinancialClearance = false;
      $('#isFinancialClearance').prop('checked', false);
      // Vivek added
      $scope.encounterId =  bill.encounterId !=undefined  ? bill.encounterId : null;
      $scope.appointmentId = bill.appointmentId;
      // End Vivek Added
      // get payer details
      if($scope.encounterId!= null){
      	bill.visitTypeId = 1;
      	bill.visitId = $scope.encounterId;
    }
     if($scope.admissionId!= null){
      	bill.visitTypeId = 2;
      	bill.visitId = $scope.admissionId;
    }
     
     if(bill.visitTypeId != undefined && bill.visitTypeId != null){
         $scope.getCashCollectedAmount(bill.visitTypeId,bill.visitId); 
     }
     
      var URI = CONSTANTS.ROOT_URL + '/api/pharmacy/sales/bills/' + $scope.unitId + "/"+$scope.selfPayeeId + "/" +bill.id;
      $rootScope.startLoader();
      BillingGenericService.serviceAction(METHOD_GET, URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function (response) {
        if (response.data.items.length > 0) {
          // $scope.payeeList = response.data.listObject;
          $scope.payeeList = response.data;
          
        	//   $scope.totalCreditPayable=$scope.payeeList.totalCreditPayable;
        	//   $scope.amountPaid=$scope.payeeList.paidAmt;
        	//   $scope.payeeDueAmtFinal=($scope.payeeList.totalSelfPayable-$scope.payeeList.paidAmt).toFixed(2);
        	//   $scope.paymentDetails.cashAmount=parseFloat($scope.payeeList.totalSelfPayable-$scope.payeeList.paidAmt).toFixed(2);
        	//   $scope.dueAmount = parseFloat($scope.payeeList.totalSelfPayable-$scope.payeeList.paidAmt).toFixed(2);
          
          // vivek added
          // $scope.billServicesList = response.data.object;
          $scope.billServicesList = response.data.items;
          var totalAmtQuaBase = parseFloat(0);
          var totalAmtConcession = parseFloat(0);
          var totalAmtDiscount = parseFloat(0);
          var totalAmtGst = parseFloat(0);
          var totalNetPayableAmt = parseFloat(0);
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
          });

          $scope.totalAmtQuaBase = totalAmtQuaBase;
          // $scope.totalAmtConcession = totalAmtConcession;
          $scope.totalAmtDiscount = totalAmtDiscount;
          $scope.totalAmtGst = (totalAmtGst).toFixed(2);
          $scope.totalNetPayableAmt = totalNetPayableAmt;

          /*
			 * $scope.payeeDueAmtFinal = response.data.balanceAmt;
			 * $scope.payeeDueAmtStatic = response.data.balanceAmt;
			 */
          // vivek added
        }
        $rootScope.stopLoader();
      });

    }

    // -----------------payment Collection----------------//
    $scope.initPaymentDetails = function () {
      $scope.paymentDetails = {
        "paymentRecieptDetailsId": "",
        "paymentModeId": 1,
        "d": "Cash",
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
    /*
	 * 5: "E - Transfer", 6: "Loyalty Card"
	 */
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
      
      $scope.getTransactionTypes();
      $scope.getBankList();
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
			$('#uploadpanCardDetailsPharBill').modal('hide');
		}
		
        $scope.PharmacyReceiptCancellation=true;
        $scope.pharmacyReceiptCancellation1=function()
        {
            var data = {
                     "organizationId":$scope.organizationId,
                     "unitId" :$scope.unitId,
                     "userId" : $scope.userId

           }
            var URI = CONSTANTS.PHARMACY.OP.BILL_LIST.DETAILS_API +"/pharmacyReceiptCancellation"
           GenericService.serviceAction(METHOD_POST, URI, data).then(
                   function(response) {
                       $rootScope.stopLoader();
                           $scope.PharmacyReceiptCancellation=response.data[0].pharmacyReceiptCancellation;
                   });
        }
        $scope.pharmacyReceiptCancellation1();
        $scope.saveBtnDisabled=false;
        $scope.checkSave=0;
    $scope.settleBill = function () {
        $scope.saveBtnDisabled=true;
    	 $scope.billDisabled = true;
         $scope.depositeSettleBtnFlag = true;
    	var collectedAmount = parseFloat(0).toFixed();
		angular.forEach($scope.listPaymentDetails,function(value,key){
            value.receivedFrom=$scope.listPaymentDetails.amountReceivedFrom;
			if(value.paymentModeId == 1){
				collectedAmount = parseFloat(collectedAmount) + parseFloat(value.amount);
			}
			
		});
		$scope.receivedFrom=$scope.listPaymentDetails.amountReceivedFrom;
		if($scope.cashAmountValidation != null && collectedAmount >=$scope.cashAmountValidation && $scope.fileObj.file == null){
			$('#uploadpanCardDetailsPharBill').modal('show');
				return false;
		}
		$('#uploadpanCardDetailsPharBill').modal('hide');
    	
     
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
          newArr.consumedAmount = parseFloat(parseFloat($scope.availableDeposits[i].consumedDeposit).toFixed(2) - $scope.availableDeposits[i].oldConsumedDeposit).toFixed(2);
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
        amountData: (parseFloat($scope.amountCollected) + parseFloat($scope.consumedAmt)).toFixed(2),
        paymentReceiptDetailsDtosList: $scope.listPaymentDetails,
        paymentDepositDetailsList: $scope.finalAvailableDeposits,
        balanceAmount:$scope.BalanceAmount,
        selfPayeeId:$scope.selfPayeeId,
        receivedFrom:$scope.receivedFrom
      }
   

      if (($scope.finalAvailableDeposits.length == 0) && ($scope.listPaymentDetails.length == 0 || $scope.listPaymentDetails == 'undefined')) {
    	  $scope.saveBtnDisabled=false;
        growl.error("Please select at least one payment", {
          title: ERROR_MSG
        });
        return;
      }

      if (($scope.amountCollected == '' || $scope.amountCollected <= 0) && ($scope.consumedAmt == '' || $scope.consumedAmt <= 0)) {
    	  $scope.saveBtnDisabled=false;
        growl.error("Amount can't be zero", {
          title: ERROR_MSG
        });
        return;
      }
      if($scope.checkSave==1)
 	 {
 	 return false;
 	 }
      $rootScope.startLoader();
      var URI = CONSTANTS.ROOT_URL + '/api/pharmacy/sales/bills/save/bill';
      BillingGenericService.serviceAction(METHOD_PATCH, URI, param, NOTIFICATION_MSG_STATUS_TRUE).then(function (response){
        if (response.data.message == "Sale Details saved successfully") {
        	$scope.checkSave=1;
        	$scope.saveBtnDisabled=false;
        	growl.success("Payment collected successfully !!!", {
				title : 'Success!!'
			});
          angular.element('#collectPaymentDetails').modal('hide');
          angular.element('#billListDetails').modal('hide');
          if($scope.fileObj.file != null){
				$scope.uploadEncounterDoc($scope.billId);
			}
          $scope.billDetails($scope.currentBill);
          $scope.calculateTotalDueRefAmount();
          $scope.listPaymentDetails = [];
          $scope.excessAmount = parseFloat(0).toFixed(2);
		  $scope.transferToActiveDeposit = 'N';
		  $scope.validatTextFieldFlag = true;
		  $scope.patientSaleTypeId = undefined;
          $scope.paymentDetails.cashAmount = parseFloat(0).toFixed(2);
          $scope.dueAmount = parseFloat(0).toFixed(2);
          
          // Ganesh
          $scope.paymentDetails.cashAmount = parseFloat($scope.dueAmount).toFixed(2);
          $scope.paymentDetails.chequeAmount = parseFloat($scope.dueAmount).toFixed(2);
          $scope.paymentDetails.credAmount = parseFloat($scope.dueAmount).toFixed(2);
          $scope.eTransferObj.transferAmount= parseFloat($scope.dueAmount).toFixed(2);
          
          
          $scope.totalAmount = parseFloat(0).toFixed(2);
          $scope.payeeDueAmtFinal = parseFloat(0).toFixed(2);
          $scope.payeeDueAmtStatic = parseFloat(0).toFixed(2);
          GetBillList();
        }else{
        	$scope.saveBtnDisabled=false;
        	growl.error(response.data.message, {
				title : 'error!!'
			});
        	angular.element('#collectPaymentDetails').modal('hide');
            angular.element('#billListDetails').modal('hide');
        	 GetBillList();
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
     /* $scope.dueAmount = $scope.deposit.depositAmount != '' && typeof ($scope.deposit.depositAmount) != 'undefined' ? parseFloat(parseFloat($scope.deposit.depositAmount) - collectedAmount).toFixed(2) : parseFloat(0).toFixed(2);
      $scope.paymentDetails.cashAmount = $scope.dueAmount;
      $scope.paymentDetails.chequeAmount = parseFloat($scope.dueAmount).toFixed(2);
      $scope.paymentDetails.credAmount = parseFloat($scope.dueAmount).toFixed(2);
      $scope.eTransferObj.transferAmount= parseFloat($scope.dueAmount).toFixed(2);
      $scope.paymentDetails.loyaltyCardAmt =  parseFloat($scope.dueAmount).toFixed(2);*/
      if($scope.deposit==undefined || $scope.deposit.depositAmount == undefined)
      {
    	  
      }
      else{
        $scope.dueAmount = $scope.deposit.depositAmount != '' && typeof ($scope.deposit.depositAmount) != 'undefined' ? parseFloat(parseFloat($scope.deposit.depositAmount) - collectedAmount).toFixed(2) : parseFloat(0).toFixed(2);
    	$scope.paymentDetails.cashAmount = $scope.dueAmount;
      $scope.paymentDetails.chequeAmount = parseFloat($scope.dueAmount).toFixed(2);
      $scope.paymentDetails.credAmount = parseFloat($scope.dueAmount).toFixed(2);
      $scope.eTransferObj.transferAmount= parseFloat($scope.dueAmount).toFixed(2);
      $scope.paymentDetails.loyaltyCardAmt =  parseFloat($scope.dueAmount).toFixed(2);
      }
    }

	//Check isActiveSelected
	  var isActiveSelected;
		function checkActiveSelected(arr) {
			return arr
					.some(function(arr) {
						if(arr.depositeFlag === 'Y')
							{
								return true;
							}
						else
							{
								return false;
							}
					});
		}
//Deposit
    $scope.addPaymentDetailsSAlesType = function () {
      $scope.isBillList = 0;
  	  $scope.transferToActiveDeposit = 'N';
  	  $scope.transferToActiveDepositFact= 'N';
      pharmacyPaymentCollFactory.addPaymentDetails($scope);
      $scope.calculateDueAmount();
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
				 $scope.calculateDueAmount();
		}else{
			isActiveSelected=checkActiveSelected($scope.availableDeposits)
			 if (!isActiveSelected) {
				  if(exAmount == undefined || exAmount == '' || exAmount == 0 || exAmount == null){
					  growl.warning("Amount should be greater than ZERO(0)..!! ",{
							title : WARNING_MSG
						});
						return false;
				  }
				  else{
			 var fixAmount = parseFloat(exAmount) - parseFloat($scope.payeeDueAmtFinal);
			  $scope.excessAmount = parseFloat(fixAmount).toFixed(2);
			  if(fixAmount>0)
				{
				  if($scope.exceededBillAmountSettlement==true)
					  {
					  $("#confirmSavebill").modal("show");
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
    }  else
	  {
        $scope.paymentDetails.cashAmount = $scope.dueAmount;
		  growl.warning("You can select either transfer to active deposit or consume from deposit..!!",{
				title : WARNING_MSG
			});
			return false;
			
 }
  }
		
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

    $scope.init();
    $scope.initTotalAmounts();

    var rePrintBillSlip = function (bill) {
      var data = {
        "appName": "HIS",
        "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
        "reportDesignFilePath": "get_salesbillsReceipt_listview.rpt",
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

    $scope.isCancelBillPopup= false;
    var screenId = 9;
    var cancelBillPopup = function (bill) { 
    	$scope.isCancelBillPopup= true;
      self.model.billIdForCancel = bill.id;
        $scope.cancelReasonId = '';
		$scope.cancelRemark = '';
      $("#accepatReject").show();
//      var popup = angular.element('#confirmCancel');
//      popup.modal('show');
    }
    $scope.closeSubPopup = function(type)
    {
			$("#accepatReject").hide();
	}
    $scope.getReasonList = function(){
		var commonObj = {
				unitId : $scope.unitId,
				organizationId : $scope.organizationId
			};
		
		var ReasonURI = BASE_URL + ROOT_URL
		+ GETACTIVEREASONLIST;
		GenericService.serviceAction("POST", ReasonURI, commonObj)
		.then(
				function(response) {
					$scope.reasonList = [];
					if (response.data.status == "success") {
						$scope.reasonList = response.data.listObject;
					}
		});
	}
	$scope.getReasonList();
    var ModalDismiss = function () {
      var popup = angular.element('#confirmCancel');
      popup.modal('hide');
    } 
    
    $scope.cancelBillButton=false;
    var cancelBill = function (bill) {
    	$rootScope.startLoader();
    	$scope.cancelBillButton=false;
    	if($scope.cancelReasonId==undefined || $scope.cancelReasonId==null || $scope.cancelReasonId=="")
  	  {
  	   growl.error("Please select Reason");
  	   return false;
  	  }
    	if($scope.cancelRemark==undefined || $scope.cancelRemark==null || $scope.cancelRemark=="")
    		{
    		$scope.cancelRemark=null;
    		}
    	$scope.cancelReasonId=parseInt($scope.cancelReasonId);
    	$("#accepatReject").hide();
      var URI = CONSTANTS.PHARMACY.OP.BILL_LIST.CANCEL_API + $scope.unitId + "/" + self.model.billIdForCancel + "/" + 9 +"/"+ $scope.cancelRemark +"/"+ $scope.cancelReasonId+"/"+$scope.userId;
      $rootScope.startLoader();
      var data = "";
      GenericService.serviceAction("PUT", URI, data).then(function (response) {
        $rootScope.stopLoader();
        $scope.cancelBillButton=false;
        GetBillList();
        growl.success("",{
          title : "Bill Cancelled  Successfully!"
        });
        $rootScope.stopLoader();
      }, function errorCallback(response) {
    	  $rootScope.stopLoader();
    	  growl.error("",{
              title : response.data.summary
            });
      });
    }

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

    initializeController();
    
	/*
	 * *************************************Mpesa Integration
	 * start******************************************
	 */
	$scope.mpesaObject={"transactionId":null,"mobileNo":null,"mpesaAmount":null,"resTransactionId":null};
	$scope.validateMpesaDetails = function(mpesaObject){
		$rootScope.startLoaderPopup();
		console.log("MPesa Object:",mpesaObject);
		
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
			growl.error("Enter both transaction id and mobile number",{
				title : ERROR_MSG
			});
			$rootScope.stopLoaderPopup();
		}
	}
	
	$scope.mpesaPaymentRequest = function(validateMpesaAmount,mobileNumber,countryCallingCode){
	
		if(validateMpesaAmount == undefined || validateMpesaAmount == null){
			
			growl.warning("Please Enter Amount",{
				title : "warning!!!"
			});
			
		}else if(validateMpesaAmount > $scope.payeeDueAmtFinal){
			
			growl.warning("Mpesa amount is should be equel or less than due amount ",{
				title : "warning!!!"
			});
			
		}else{
			$scope.skipMpesaValidation = true;
				var data ={
						organizationId :$scope.organizationId,
						unitId : $scope.unitId,
						createdBy : $scope.userId,
						createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
						updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
						updatedBy:$scope.userId,
						uhidNumber: $scope.selectedUhid,
						phoneNumber:parseInt(countryCallingCode+""+mobileNumber) ,
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
				uhidNumber: $scope.selectedUhid
				
		};
		$scope.skipMpesaValidation = true;
		$rootScope.startLoaderPopup();
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
				$rootScope.stopLoaderPopup();
				}
			else if(response.data.status == "error"){
				
				growl.error(response.data.message != null ? response.data.message :"Please wait",{
					title : ERROR_MSG
				});
				$rootScope.stopLoaderPopup();
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
	/*
	 * *************************************Mpesa Integration
	 * END******************************************
	 */
	
	

	
	$scope.checkValidatePinPad = function()
	{
		if(($scope.registeredPinPadDevice != undefined || $scope.registeredPinPadDevice != null || $scope.registeredPinPadDevice != "") 
				&& $scope.registeredPinPadDevice == "POS Machine"){
			$scope.getResposeList=[];
			$scope.getGeneratedResponse=[];
			$rootScope.startLoaderPopup();
			var data = {
					patientId : self.model.billDetailList.patientId,
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
//							$scope.paymentDetails.credCardTypeId=$scope.getGeneratedResponse.cardType;
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
					 * console.log("addQuestion() error:" + message); });
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
					 * function(response) { console.log(response); alert(); });
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
						 * console.log("ree",response);
						 * console.log("reew",response.data);
						 * console.log("reer",response.data.rspCode);
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
	
	
	$scope.getPandCard = function(billId){

		var URI = BASE_URL + ROOT_URL + "/billing/PanCard/url/"+billId+"/"+2; //1 for Deposit screen
		BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			if (response.data.status == "success")
			{
				if(response.data.object != null ){
					var imageUrl = response.data.object.docUrl;
					$window.open(imageUrl, '_blank');
				}else{
					growl.warning("pancard is not uploaded",
							{
								title : 'Warning!'
							});
				}
				
			} 

		});
	
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
						  "email":$scope.patientEmail,
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
					  "email":$scope.patientEmail,
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
						  "emailId":$scope.patientEmail,
						  "name":self.model.billDetailList.patientName,
						  "patientId": self.model.billDetailList.patientId, 
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
						  "patientId": self.model.billDetailList.patientId, 
						  
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
		
		
		/*******For POS machine***********/
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
				$scope.flagForSale=true;
				 $scope.flagForSMSpay=false;
				 $scope.flagForBharatQr=false;
				 $scope.objPosMachine.isBharatQr ="";
				 $scope.objPosMachine.isSMSpay=""
			}
			
		}
	 
	 $scope.getFlagBharatQr=function(){
		
			if($scope.objPosMachine.isBharatQr =="isBharatQr"){
				$scope.flagForBharatQr=true;
				 $scope.flagForSale=false;
				 $scope.flagForSMSpay=false;
				 $scope.objPosMachine.isSale ="";
				 $scope.objPosMachine.isSMSpay="";
			}
		}
	 $scope.getFlagSMSpay=function(){
			
			if($scope.objPosMachine.isSMSpay =="isSMSpay"){
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
//					{

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
						patientId:self.model.billDetailList.patientId,
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
		 /*********************/
		 
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
					  "emailId":$scope.patientEmail,
					  "name":self.model.billDetailList.patientName,
					  "patientId": self.model.billDetailList.patientId, 
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
	}
	
	
  }

  function config($stateProvider) {
    $stateProvider.state("pharmacyBillList", {
      url: "/pharmacyBillList",
      templateUrl: 'views/PharmacyInventory/InPatient/pharmacy-bill-list.html',
      controller: "PharmacyBillList.Controller",
      title: 'Pharmacy Bill List',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 113);
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

  angular
    .module("myApp")
    .config(config)
    .controller("PharmacyBillList.Controller", pharmacyBillListController);
})();