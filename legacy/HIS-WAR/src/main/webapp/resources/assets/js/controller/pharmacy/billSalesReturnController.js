(function () {
  "use strict";

  function billSalesReturnController($scope, $rootScope,reportService, $http, $cookies, CONSTANTS, GenericService,commonDetailService,PaginationService , $filter, growl, CurrencyService, screenDataService,BillingGenericService,GenericServiceParamHeader) {
    var self = this;

    // Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
    function initializeController() {
      $rootScope.loginpage = true;
      
      $scope.dateOptions = {
				formatYear: 'yyyy',
				showWeeks: false,
              minDate : new Date(),
			};
      
      $scope.openDatePicker = function () {
			$scope.datepicker.opened = true;
		};

		$scope.datepicker = {
			opened: false
		};
      self.model = {
        patientIdFromService :  commonDetailService.getDataIdForBedManagmentSalesRetunPatienId(),
        patientNameFromService :  commonDetailService.getDataIdForBedManagmentSalesRetunPatientName(),
        uhidFromService :  commonDetailService.getDataIdForBedManagmentSalesRetunUHID(),
        fromDate: new Date(),
        toDate: new Date(),
        fromDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate: new Date(),
          minDate : moment(new Date()).subtract(180, 'days').toDate()
        },
        toDateOptions: {
          formatYear: 'yyyy',
          showWeeks: false,
          maxDate: new Date()
        },
        isReturnQuantityValid: [],
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

      // Methods
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
      self.model.PatientNameKeyPress = PatientNameKeyPress;
      self.model.uHIDKeyPress = uHIDKeyPress;
      self.model.GetSalesReturnBillList = GetSalesReturnBillList;
      self.model.SalesReturnBillDetails = SalesReturnBillDetails;
      self.model.rePrintBillSlip=rePrintBillSlip;
      self.model.GetGenericMaster = GetGenericMaster;
      self.model.GetStrengthUnitList = GetStrengthUnitList;
      self.model.ViewCombinationDetails = ViewCombinationDetails;
      self.model.cancelBillPopup = cancelBillPopup;
      self.model.ModalDismiss = ModalDismiss;
      self.model.cancelBill = cancelBill;
      self.model.settlementReturn = settlementReturn;
      self.model.rePrintBillSlipSettle = rePrintBillSlipSettle;
      self.model.updateBillSalesReturn = updateBillSalesReturn;
//      self.model.getPatientSearch = getPatientSearch;
      self.model.getAllPatientDiagnosisForIcon = getAllPatientDiagnosisForIcon;
      initialCall();
    }    
       
    var cancelBillPopup = function (salesReturn) { 
        self.model.returnIdForCancel = salesReturn.id;
        self.model.returnStoreId = salesReturn.storeId;
        var popup = angular.element('#confirmCancel');
        popup.modal('show');
      }
    
    var ModalDismiss = function () {
        var popup = angular.element('#confirmCancel');
        popup.modal('hide');
    }     

    var cancelBill = function () {    	
        var URI = CONSTANTS.PHARMACY.OP.SALES_RETURN.CANCEL_API+ '/' + $scope.unitId +  "/" + self.model.returnIdForCancel  +  "/" + self.model.returnStoreId ;
        $rootScope.startLoader();
        var data = "";
        GenericService.serviceAction("PUT", URI, data).then(function (response) {
          $rootScope.stopLoader();
            ModalDismiss('#confirmCancel');
            initialCall();
          
          growl.success("",{
            title : "Bill Cancel Successfully!"
          });
        })
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
    	 
      $scope.visitTypeId = 1;
      if (search.length < 3) return;
      if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 3)) {
        self.model.prevSearch = search;
//        var URI = CONSTANTS.GLOBAL.PATIENT_SEARCH_API + $scope.unitId + "/" + search + "/" + $scope.visitTypeId;
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
    $scope.cookieObjectStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
    $scope.storeName = $scope.cookieObjectStore != null ? $scope.cookieObjectStore.storeName : ''; // store null -> empty header
    $scope.storeId = $scope.cookieObjectStore != null ? $scope.cookieObjectStore.storeId : undefined; // store null -> undefined (the calls gate on storeReady)
    $scope.popupStore = function () {
        var popup = angular.element('#storePopUp');
        popup.modal('show');
      }
    var PatientNameNotFound = function (search) {
      var item = {
        "itemFound": false,
        "details": "patient Not found with name " + search
      };
      self.model.patientNames = [item];
      return self.model.patientNames;
    }

    var AddUHID = function (item) {
      self.model.patientData = item;
      self.model.uhid = item.uhid;
      self.model.patientId = item.id;
      self.model.patientName = item.patientName;
      
      self.model.patientNameFromService=item.id;
      self.model.patientNameFromService= item.patientName; 
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
        $scope.visitTypeId = 1;
        var URI = CONSTANTS.GLOBAL.UHID_SEARCH_API_SALES_RETURN + search + "/" + $scope.visitTypeId+ "/"+ $scope.organizationId;

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

    var initialCall = function () {
      if (!screenDataService.getScreenData()) {
        GetSalesReturnBillList()
      } else {
        screenDataService.getScreenData().forEach(function (dataobj) {
          if (dataobj.screenName == 'billSalesReturn') {
            var requestData = dataobj.searchrequest;
            self.model.patientId = requestData.patientId
            self.model.patientName = requestData.patientName
            self.model.uhid = requestData.uhid
            self.model.returnNo = requestData.returnNo
            self.model.fromDate = new Date(requestData.fromDate);
            self.model.toDate = new Date(requestData.toDate);
          }
        });
        GetSalesReturnBillList();
      }
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

    var GetSalesReturnBillList = function () {
      if (ValidateDate()) {
        var bills = self.model;
        bills.patientName = bills.patientNameFromService;
        bills.uhidFromService = bills.uhid;

        var URI = CONSTANTS.PHARMACY.OP.SALES_RETURN.SEARCH_API;

        /*var data = {
          "fromDate": moment(bills.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(bills.toDate).format('YYYY-MM-DD'),
          // "patientId": !!bills.patientId ? bills.patientId : undefined,
          "patientId": !!bills.patientIdFromService ? bills.patientIdFromService : undefined,
         // "patientName": !!bills.patientName ? bills.patientName :
			// undefined,
          "patientName": !!bills.patientNameFromService ? bills.patientNameFromService : undefined,
         // "uhid": !!bills.uhid ? bills.uhid : undefined,
           "uhid": !!bills.uhidFromService ? bills.uhidFromService : undefined,
          "returnNo": !!bills.returnNo ? bills.returnNo : undefined,
        }*/
        var data = {
	        		  "fromDate": moment(bills.fromDate).format('YYYY-MM-DD'),
	        		  "toDate": moment(bills.toDate).format('YYYY-MM-DD'),
	        		  "patientId": bills.patientIdFromService != undefined ? bills.patientIdFromService : self.model.patientIdFromService,  
	        		  "patientName": bills.patientNameFromService != undefined ? bills.patientNameFromService : self.model.patientNameFromService,
	        		  "uhid": bills.uhidFromService != undefined ? bills.uhidFromService : self.model.uhidFromService,
	        		  "returnNo": !!bills.returnNo ? bills.returnNo : undefined,
	        		  "storeId":$scope.storeId
        	        }
        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.pagination.data=[];
            bills.salesReturnBillList = response.data; 
			angular.forEach(bills.salesReturnBillList, function(bill){
				if(!bill.patientDetails){
					bill.patientDetails = bill.tempPatientDatails;
				}
				if(!bill.patientId){
					bill.patientId = bill.tempPatId;
				}
			})       
            if(!!response && !!response.data && response.data.length > 0){            
              PaginationService.searchData(self.model.salesReturnBillList, self.model.pagination);
            }
           
            $rootScope.stopLoader();
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
            bills.patientId = undefined;
            growl.error('Error');
          });
        // var screenData = {
        //   searchrequest: {
        //     "fromDate": moment(bills.fromDate).format('YYYY-MM-DD'),
        //     "toDate": moment(bills.toDate).format('YYYY-MM-DD'),
        //     "patientId": bills.patientId,
        //     "patientName": bills.patientName,
        //     "uhid": bills.uhid,
        //     "returnNo": bills.returnNo,
        //   },
        //   screenName: 'billSalesReturn',
        // }
        // screenDataService.setScreenData(screenData);
      }
    }
    $scope.patientId  =  0;
    $scope.returnId = null;
    var SalesReturnBillDetails = function (item) {
    	$scope.returnId = item.id;
      var URI = CONSTANTS.PHARMACY.OP.SALES_RETURN.DETAILS_API+$scope.unitId + "/" + item.id;
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
			angular.forEach(response.data.item, function(item){
				if(item.returnQuantity == undefined || item.returnQuantity == null || item.returnQuantity == ""){
					item.returnQuantity = 0;
				}
				item.uom.find(function(u){
					if(item.uomUnit == u.uomUnit){
						item.uomTypeId = u.uomTypeId;
					}
				})
				if(item.isThreeUOMApplicable == false){
					if(item.uomTypeId == 1){
						item.receivedQtyCon = (item.returnQuantity +"*"+ item.uom[0].conversion);
						item.receivedQtyCalculation = (item.returnQuantity * item.uom[0].conversion);
						item.uomUnitData = item.uom[2].uomUnit;
					}
					else if(item.uomTypeId == 2){
					
					}
					else{
						item.receivedQtyCon = (item.returnQuantity);
						item.receivedQtyCalculation = (item.returnQuantity);
						item.uomUnitData = item.uom[2].uomUnit;
					}
				}
				else{
					if(item.uomTypeId == 1){
						item.receivedQtyCon = (item.returnQuantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
						item.receivedQtyCalculation = (item.returnQuantity * item.uom[0].conversion * item.uom[1].conversion);
						item.uomUnitData = item.uom[2].uomUnit;
					}
					else if(item.uomTypeId == 2){
						item.receivedQtyCon = (item.returnQuantity +"*"+ item.uom[1].conversion);
						item.receivedQtyCalculation = (item.returnQuantity * item.uom[1].conversion);
						item.uomUnitData = item.uom[2].uomUnit;
					}
					else{
						item.receivedQtyCon = (item.returnQuantity);
						item.receivedQtyCalculation = (item.returnQuantity);
						item.uomUnitData = item.uom[2].uomUnit;
					}
				}
			})
          	self.model.SalesReturnBillDetailList = response.data;
          	self.model.storeId = self.model.SalesReturnBillDetailList.storeId;
		  	if(response.data.patientId == undefined || response.data.patientId == null || response.data.patientId == ''){
		  		response.data.patientId = response.data.tempPatId;
		  	}
		  	$scope.patientId = response.data.patientId;
          	$rootScope.allergydata(response.data);
          	getAllPatientDiagnosisForIcon(response.data.patientId);
          	$rootScope.stopLoader();
        });
    }

    var rePrintBillSlip = function (bill) {
        var data = {
          "appName": "HIS",
          "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
          "reportDesignFilePath": "get_salesReturn_listview.rpt",
          "reportFormat": "PDF",
          "exportFileName": "SalesReturnBillSlip",
          "reportParams": [
            {
              "paramName": "ReturnId",
              "paramType": "Integer",
              "paramValue": bill.id
            }
          ]
        };
      reportService.generate(REPORT_API_URI1, data);
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
        
        //Settlement Functionality Start
        var settlementReturn = function(){
        	$scope.saveBtnDisabled=false;
        	$scope.returnId = self.model.SalesReturnBillDetailList.id;
    		$scope.listPaymentDetails = [];
    		$scope.dueAmount  = 0 ;
    		$scope.dueAmount  = $scope.paymentDetails.cashAmount ;
    		$scope.cnMasterObj = self.model.SalesReturnBillDetailList;
			if($scope.cnMasterObj.totalreturnPaidAmount == undefined){
				$scope.cnMasterObj.totalreturnPaidAmount = 0;
			}
			$scope.paymentRefundObj.amountRefunded = $scope.cnMasterObj.totalRefundAmount - $scope.cnMasterObj.totalreturnPaidAmount;
			$scope.paymentDetails.cashAmount = parseFloat($scope.cnMasterObj.totalRefundAmount - $scope.cnMasterObj.totalreturnPaidAmount).toFixed(2);
       
			$scope.calculateDueAmount();
        	$scope.showColletPaymentPopup(self.model.SalesReturnBillDetailList);
        	
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
				loyaltyCardNo:"",
				loyaltyCardAmt:"",
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
		$scope.dateOptionsTrancs = {
				formatYear : 'yyyy',
				showWeeks : false,
				minDate : new Date()
			};
		
		$scope.init = function () {
			$scope.sexMasterList = [];
			$scope.listPaymentDetails = [];
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
		 
		 
		$scope.calculateDueAmount = function () {
			
			var collectedAmount = parseFloat(0).toFixed();
			angular.forEach($scope.listPaymentDetails, function (value, key) {
				collectedAmount = parseFloat(collectedAmount) + parseFloat(value.amount);
			});
			$scope.dueAmount = $scope.paymentRefundObj.amountRefunded != '' && typeof ($scope.paymentRefundObj.amountRefunded) != 'undefined' ? parseFloat(parseFloat($scope.paymentRefundObj.amountRefunded) - collectedAmount).toFixed(2) : parseFloat(0).toFixed(2);
			$scope.paymentDetails.cashAmount=parseFloat($scope.dueAmount);
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
	        	if(regexp.test(Number($scope.paymentDetails.loyaltyCardAmt))){
					return false
				}
				else{
					$scope.paymentDetails.loyaltyCardAmt = $scope.cnMasterObj.totalRefundAmount;
				}
	        }
		}

		$scope.addPaymentDetails = function () {

			if (CheckValidPayment()){
			
			//check amount collected is greater tha deposit amount
			var refundAmount = $scope.cnMasterObj.totalRefundAmount != "" && typeof ($scope.cnMasterObj.totalRefundAmount) != "undefined" ? parseFloat($scope.cnMasterObj.totalRefundAmount) : parseFloat(0);
			refundAmount = refundAmount - $scope.cnMasterObj.totalreturnPaidAmount;			
			$scope.paymentRefundObj.amountRefunded = $scope.cnMasterObj.totalRefundAmount - $scope.cnMasterObj.totalreturnPaidAmount;
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
				case 6 :
					
					tempAmount = $scope.paymentDetails.loyaltyCardAmt != '' && typeof($scope.paymentDetails.loyaltyCardAmt) != 'undefined' ? parseFloat($scope.paymentDetails.loyaltyCardAmt) : parseFloat(0);
					if(typeof($scope.paymentDetails.loyaltyCardAmt) == "undefined" || $scope.paymentDetails.loyaltyCardAmt == "" || $scope.paymentDetails.loyaltyCardNo == ""){
						growl.error("Please fill all details",{
							title : ERROR_MSG
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
					tempAmount = $scope.paymentDetails.loyaltyCardAmt != '' && typeof ($scope.paymentDetails.loyaltyCardAmt) != 'undefined' ? parseFloat($scope.paymentDetails.loyaltyCardAmt) : parseFloat(0);
					if (parseFloat($scope.paymentDetails.loyaltyCardAmt) > refundAmount.toFixed(2)) {
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
						expiryDate: expiryDate
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
						expiryDate: expiryDate,
						transactionNo : $scope.paymentDetails.transactionNo
					};
					$scope.listPaymentDetails.push(paymentDetails);
					break;
				case 6:				
					var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.loyaltyCardAmt).toFixed(2),cardNo:$scope.paymentDetails.loyaltyCardNo,createdBy:$scope.userId};
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
						amount: parseFloat($scope.paymentDetails.loyaltyCardAmt).toFixed(2),
						createdBy: $scope.userId,
					};
					$scope.listPaymentDetails.push(paymentDetails);
					break;
			}
			$scope.initPaymentDetailsObj();
			$scope.calculateDueAmount();
			$scope.setFirstTabLevel(1);
	        $scope.setPaymentModeId(1);
			$scope.firstName = "";
			$scope.patientLoyaltyCardFlag = true;
		}// end check Valid Payment
		
		}// end function

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
	    $scope.saveBtnDisabled=false;
		$scope.addRefund = function () {
			$scope.saveBtnDisabled=true;
			$rootScope.startLoader();
			$scope.paymentRefundObj.createdBy = $scope.userId;
			$scope.paymentRefundObj.orgnisationId = $scope.organizationId;
			$scope.paymentRefundObj.unitId = $scope.unitId;
			$scope.paymentRefundObj.isCnOrDeposit = 3;
			$scope.paymentRefundObj.patientId = $scope.patientId;
			$scope.paymentRefundObj.saleReturnId = $scope.returnId;
			//$scope.paymentRefundObj.tRefundCnId =$scope.cnMasterObj.saleId;
			$scope.paymentRefundObj.tPharmacyReturnId=$scope.cnMasterObj.saleId;

			var depositId = $scope.cnMasterObj.depositId;
			var tRefundCnId = $scope.cnMasterObj.saleId;

			var param = {
				paymentRefundMasterDto: $scope.paymentRefundObj,
				listPaymentRefundDetails: $scope.listPaymentDetails
			};
			
			$scope.refundDetails = angular.copy($scope.paymentRefundObj);
			$scope.paymentrefundDetails = angular.copy($scope.listPaymentDetails);
		
			var URI = CONSTANTS.ROOT_URL + "/api/billing/refund/pharmacy";		
			BillingGenericService.serviceAction(METHOD_POST, URI, param, NOTIFICATION_MSG_STATUS_TRUE).then(function (response) {
				if (response.data.status == "success") {
					self.model.updateBillSalesReturn();
					$scope.initPaymentDetailsObj();
					$scope.listPaymentDetails = [];
					$scope.patientDetailsList = [];
					$scope.patientDetails = {};		
					angular.element('#refundList_collectRefundModal').modal('hide');
					angular.element('#detailsModal').modal('hide');
					//added to resolved HIS-9519 Able to settle bill multiple times for sales return refund bill on sales return details pop up.
					angular.element('#detailsPopup').modal('hide');
					//self.model.NavigatetoSalesReturn();
				}
				$rootScope.stopLoader();
			});
		}
		
		$scope.totalreturnPaidAmount = 0; 
		var updateBillSalesReturn = function(){		
			$scope.totalreturnPaidAmount = 0; 
			var totalAmountRefund = parseFloat($scope.refundDetails.amountRefunded.toFixed(2));
			angular.forEach($scope.paymentrefundDetails, function(data){
				$scope.totalreturnPaidAmount = $scope.totalreturnPaidAmount + parseFloat(data.amount);
			});
			// var totalreturnPaidAmount = parseFloat($scope.paymentrefundDetails[0].amount).toFixed(2); 
			var TotalBalancePaid = totalAmountRefund - parseFloat($scope.totalreturnPaidAmount);			
			var statusId = 2; //Pending            
            if(TotalBalancePaid > 0)
                statusId = 18 ; //Partially Completed;
            else if(totalAmountRefund === parseFloat($scope.totalreturnPaidAmount))
                statusId = 4 ; //Completed;
				
             var data = {
				"storeId": self.model.storeId,								
				"statusId": statusId,		
				"totalreturnPaidAmount" : $scope.totalreturnPaidAmount,
				"patientId" : $scope.patientId
			}             
             var URI = CONSTANTS.PHARMACY.OP.SALES_RETURN.UPDATE_RETURN_API +"/"+ $scope.unitId  +  "/" + $scope.returnId;
             $rootScope.startLoader();       
             GenericService.serviceAction("PUT", URI, data).then(function (response) {             
            	 //rePrintBillSlipSettle($scope.returnId); 
            	 if(response.data.id != -1){
                     initialCall();
                 }else{
                     growl.error("This transaction already processed by other user");
                     initialCall();
                 }
             })         
             	
		}	
		
		var rePrintBillSlipSettle = function (id) {
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
		
        //Settlement Functionality End 
    
    initializeController();
	$scope.init();
	
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
			if (!$scope.paymentDetails.debAmount ||  $scope.paymentDetails.debAmount$scope.paymentDetails.debAmount == '' || typeof ($scope.paymentDetails.debAmount) == undefined){
				growl.error("Please Enter Amount", {title: ERROR_MSG});
				isValidPayment =false;
			}
			return isValidPayment;
			break;
		case 6:
			if ($scope.paymentDetails.loyaltyCardNo == "" ){
					growl.error("Enter Card Number ", {title: ERROR_MSG});
					isValidPayment =false;
				}
			if ($scope.paymentDetails.loyaltyCardAmt == "" ){
				growl.error("Enter Amount ", {title: ERROR_MSG});
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
			
			if ($scope.paymentDetails.loyaltyCardAmt == "" ){
				growl.error("Enter Amount ", {title: ERROR_MSG});
				isValidPayment =false;
			}
			return isValidPayment;
			break;
	}
		
	}//end valid function
	
	
	

	/*======================================================================** loyalty Card****========================================================================*/
	$scope.firstName = "";
	$scope.loyaltyCarNo="";
	$scope.patientLoyaltyCardFlag= true;
	$scope.getLoyaltyCardList = function(){
		$scope.startLoader();
		if($scope.paymentDetails.paymentModeId == 6){
			$scope.resend =false;
			var data = {
					  "patientId" :  $scope.patientId  ,
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
		$scope.resend =false;
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
		$scope.patientLoyaltyCardFlag = true;
	}


	/**=====================================================** loyalty Card****====================================================*/

	
  }

  function config($stateProvider) {
    $stateProvider.state("billSalesReturn", {
      url: "/billSalesReturn",
      templateUrl: 'views/PharmacyInventory/InPatient/bill-sales-return.html',
      controller: "BillSalesReturn.Controller",
      title: 'Sales Return',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 112);
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
    .controller("BillSalesReturn.Controller", billSalesReturnController);
})();