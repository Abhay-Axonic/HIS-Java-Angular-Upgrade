(function () {
    "use strict";
  
    function walkinPrescriptionController($scope, $rootScope, $window, $http, $filter, CONSTANTS, reportService, GenericService, growl, CurrencyService, StatusService, $state, pharmacyPaymentCollFactory, BillingGenericService, $cookies,$location,loyaltiCardCallFactory, commonDetailService,GenericServiceParamHeader,$timeout) {
      var self = this;
      var initialStore;
  
      var cookieObject = {};
      var cookieObjectAppConfig = {};
      var cookieObjectStore = {};
  
      [cookieObject, cookieObjectAppConfig] = $rootScope.checkCookies();
  
      var cookieObject = $cookies.getObject('cookieObject');
      if (cookieObject == undefined) {
        $state.go('login');
        return;
      }
      
      $scope.counterMstObj = JSON.parse(sessionStorage.getItem('counterMstObj'));  
      var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
      if (cookieObjectAppConfig == undefined) 
      {
        $state.go('login');
        return;
      }
      
      var cookieObjectStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
      if(cookieObjectStore==undefined){
        $rootScope.storePopup();
        angular.element('#pharmacyPopupBeforLeftexpand').modal('show');
      }
    
      var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
        $rootScope.loginpage = true;
        $scope.page = "billList";
        
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
        $scope.medicationObj = {
				itemId : "",
				searchItem :"",
				searchItemFre:"",
				favorite_status : false,
				medicationStartDate:new Date(),
				roaId : '',
				dose : '',
				doseUnitId : '',
				durationCount : "",
				durationUnit :'Day',
				instructionId : '',
				medicationEndDate : '',
				frequencyId : '',
				quantity : '',
				uomTypeId : '',
				conversions : '',
				strength : '',
				priorityId : 2,
				isStat : false,
				favorite_status : false,
				priorityCode: 'NR',
				isGeneric:'N',
				isInstruction:'N',
				nonFormularyAdvice:'N',
				prescribedDosageUnitList :[]

			};
        /* start code for resolved HIS-10582 Calendar  is not appearing on Walk In Prescription screen */
        $scope.openDatePicker = function () {
        	$scope.datepicker.opened = true;
        	};
        	
        $scope.datepicker = {
        		opened: false
        		};
          /* end code for resolved HIS-10582 Calendar  is not appearing on Walk In Prescription screen */
       
        $scope.openDatePickerPatiSearch = function () {
          $scope.datepicker1.opened = true;
        };
  
        $scope.datepicker1 = {
          opened: false
        };
  
        $scope.isValidated = false;
  
        self.model = {
          isOTC: false,
          walkInSale: {
            id: "1" + (new Date()).getTime(),
            items: [],
            discountType: '2'
          },
          isQuantityValid: [],
          prefixList: [{
            "prefix": "Select"
          }],
          patientGenderList: [{
            "gender": "Select"
          }],
          patientSearchFlag: true,
          discountCategoryList: [{
              "discountCategory": "Select Discount Category"
            }]
        };
        
        $scope.unitId = cookieObject.unitId;
        $scope.userId = cookieObject.userId;
        $scope.organizationId = cookieObject.organizationId;
        $scope.userId = $scope.userId;
        $scope.employeeId = cookieObject.employeeId;
        $scope.appointmentId = 0;
        $scope.encounterId = null;
  
        //Methods 
        CurrencyService.GetCurrency().then(function (currency) {
          self.model.Currency = currency;
        })
        StatusService.GetStatus().then(function (status) {
          self.model.Status = status;
        })
  
        self.model.OtcFlag = OtcFlag;
        self.model.SearchItems = SearchItems;
        self.model.SearchGenerics = SearchGenerics;
        self.model.AddItemToBill = AddItemToBill;
        self.model.isStoreChanged=isStoreChanged;
        self.model.AddItem = AddItem;
        self.model.RemoveItem = RemoveItem;
        self.model.SavePopup = SavePopup;
        self.model.ConfirmationPopup = ConfirmationPopup;
        self.model.StoreChangePopup=StoreChangePopup;
        self.model.resetStore=resetStore;
        self.model.WalkInSalesFlagStatus = WalkInSalesFlagStatus;
        self.model.BarcodeScan = BarcodeScan;
        self.model.searchPatient = searchPatient;
        self.model.SelectPatientById = SelectPatientById;
        self.model.ValidateQuantity = ValidateQuantity;
        self.model.GetGenericMaster = GetGenericMaster;
        self.model.GetStrengthUnitList = GetStrengthUnitList;
        self.model.tempPatientId = null;
        self.model.checkAgeValid = checkAgeValid;
        self.model.ClearBill = ClearBill;
        
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

        $scope.entitlementObj={
          "paymentEntitlementTypeId":1,
          "searchbyTypeId":1
        }
        
        SetFocus();
  
        PopulateDropdown();
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
          self.model.isDoctorNameValid = false;
          self.model.isDoctorAddressValid = false;
        }
      }
  
      var PopulateDropdown = function () {
        var URI = CONSTANTS.PHARMACY.OP.WALK_IN_SALES.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId;
  
        self.model.walkInSale.prefix = self.model.prefixList[0];
        self.model.walkInSale.patientGender = self.model.patientGenderList[0];
  
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.prefixList = self.model.prefixList.concat(response.data.prefix);
            self.model.patientGenderList = self.model.patientGenderList.concat(response.data.gender);
            self.model.discountCategoryList = self.model.discountCategoryList.concat(response.data.discountCategory);
            $rootScope.stopLoader();
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
          }
        }
      }
      var searchPatient = function () {
    	  $scope.patientDetails = {};
        if (!!self.model.patientSearchObj.birthDate) {
          var birthDate = moment(self.model.patientSearchObj.birthDate).format('YYYY-MM-DD');
        } else {
          var birthDate = undefined;
        }
        if(self.model.patientSearchObj.patientName!="" && self.model.patientSearchObj.patientName.length < 3)
		{
			growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
			self.model.patientSearchData=[];
			return false;
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
        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            $rootScope.stopLoader();
            self.model.patientSearchData = response.data.listObject;
            if(response.data.listObject==null){
				growl.warning(response.data.message,{
					title : ERROR_MSG
				});
				self.model.patientSearchObj.birthDate="";
				self.model.patientSearchObj.gender=null;
				self.model.patientSearchData=[];
				return false;
			}
            self.model.patientSearchObj.birthDate="";
			self.model.patientSearchObj.gender=null;
            $rootScope.stopLoader();
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
            growl.error('Error');
          });
        self.model.patientSearchObj.birthDate="";
        self.model.patientSearchData=[];
      }
  
      
    //get sponsor details by encounter or admissionId start
      $scope.getSponsorDetails=function(admissionId,encounterId,patientId){
          $rootScope.startLoader();
          var param = {
                       encounterId:encounterId,
                       admissionId:admissionId,
                       patientId:patientId
                      };
          console.log("Id data",param);
           var URI = BASE_URL + ROOT_URL + "/global/consumption/getActivePaymentEntitlement"
              BillingGenericService.serviceAction(METHOD_POST,URI,param,"").then(function(response) {
                  if (response.data.status == "success")
                  {
                      $scope.paymentEntitleMentList=response.data.listObject;
                      if($scope.paymentEntitleMentList!=null && $scope.paymentEntitleMentList.length>0)
                      {
                         $scope.entitlementObj.paymentEntitlementTypeId = $scope.paymentEntitleMentList[0].paymentEntitlementId;					 	
                      }					
                      else{
                            
                            $scope.entitlementObj.paymentEntitlementTypeId = 1;
                       }
                      
                      $rootScope.stopLoader();
                  }
                  else if(response.data.status == "error")
                  {
                         $rootScope.stopLoader();
                  }
                  
              });
      }
      //get sponsor details by encounter or admissionId end
      $scope.saleTypeIdAgainstEncounter = 3;
      $scope.patientSaleTypeIdFlag = false; 
      var SelectPatientById = function (patient) {
    	  if(patient.admissionId==undefined || patient.admissionId==null || patient.admissionId==0)
		  {
    		$scope.kitMasterList=[];
        	growl.warning("Patient Is Not Admitted");
		  } 
    	  $scope.getCurrentAppointmentVitalMapper(patient);
    	  $scope.patientDetails = {};
			$scope.patientDetails.patientName = patient.patientName;
			$scope.patientDetails.uhIdNumber = patient.uhIdNumber;
			$scope.patientDetails.genderCode = patient.genderCode;
			$scope.patientDetails.ageStr = patient.ageStr;
			$scope.patientDetails.dateOfAdmission = patient.doa;
			$scope.patientDetails.patientDischargeDate = patient.pdd;
			
			var dateDoa = moment($scope.patientDetails.dateOfAdmission, ['DD-MM-YYYY', 'MM-DD-YYYY','YYYY-MM-DD']);
			var datePdd = moment($scope.patientDetails.patientDischargeDate, ['DD-MM-YYYY', 'MM-DD-YYYY','YYYY-MM-DD']);
			
			$scope.patientDetails.los = $rootScope.dateDiffrence(dateDoa,datePdd);
			if(isNaN($scope.patientDetails.los) || $scope.patientDetails.los==undefined || $scope.patientDetails.los==null)
				{
				$scope.patientDetails.los='';
				}
			$scope.patientDetails.bedNumber = patient.bedNumber;
			var doctorName='';
			if(patient.doctorName!=undefined && patient.doctorName!=null && patient.doctorName!='')
				{
				doctorName=patient.doctorName.split('Dr.')[1];
				}
			$scope.patientDetails.doctorFullName = doctorName;
			$scope.patientDetails.wardNameCover=patient.wardName;
			$scope.IsFinalBillGenerated='N';
			if(patient.isFinalBill  == 'Y') {
				$scope.IsFinalBillGenerated='Y';
			}
          $rootScope.startLoader();
          $scope.visitNumberFlag=false;
          self.model.visitNumbers = [];
          $scope.encounterId = null;
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
      	  if(patient.mobileNo!=null){
      		  $scope.mobileNumber = parseInt(patient.mobileNo);
      		  $scope.countryCallingCode = parseInt(patient.countryCallingCode);
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
    	  	  $scope.getKitMasterList();
    	  	 
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
                  $scope.getKitMasterList();
                  growl.warning("Patient is admitted..!!",{
                    title : WARNING_MSG
                  });
    					    	  
                  var URI = BASE_URL + ROOT_URL + "/api/billing/getBillData/admission/"+$scope.admissionId;
                  BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                    $scope.billNumber =response.data.billNumber;
                    $scope.isInvoiceGenerated =response.data.isInvoiceGenerated;
                  });
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
                i++;
              });
              $scope.activeDeposit = activeDeposit;
              $scope.useActiveDepo = true;
            }
    		  }); 
          self.model.walkInSale.doctorName = patient.doctorName;
        }
      $scope.getVisitNo = function(admisisonId,encounterId)
      {
   	    if(encounterId>0){
   	    	$scope.encounterId=encounterId;
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
               });
   						self.model.visitNumber = $scope.patientEncounterList[0].encounterNumber;
   						self.model.visitNumber = self.model.visitNumbers.find(function (obj) {
   					          return obj;
   					        });
   				   }
   				}
   			});
   	    }
   	    else if(admisisonId>0){
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
   						$scope.deptId=$scope.admissionDetails.departmentId;
   						if(self.model.visitNumber.length>0){
   				    		  $scope.visitNumberFlag=true;
   				    	  }
   						$scope.commonDoctorId=$scope.admissionDetails.doctorId;
   				     }
   				   }
   				});
   	    }
   		
      }
      $scope.getCurrentAppointmentVitalMapper=function(patient){
			
			$rootScope.startLoader();
			var paramObjForVital = {                        				
					patientId : patient.patientId,
					encounterId : patient.admissionId,
  				roleId:3,
  				flag:'Y' //for admission
  		}
			var headerObj={
					"unit_id" : $scope.unitId,
					"organization_id" : $scope.organizationId
			};
			var data={
					
					unitId:$scope.unitId,
     				   organizationId:$scope.organizationId,
     				   patientId:patient.patientId,
			};
//  		console.log("jj",paramObjForVital);
  		//return false;
          var URI = BASE_URL + ROOT_URL
//          + GETCURRENTAPPOINTMENTVITALMAPPER;
          + "/api/opd/getListOfVitalsByPatientIdForCoversheet";
      GenericServiceParamHeader
          .serviceAction(METHOD_POST, URI,
                  data,
                  NOTIFICATION_MSG_STATUS_FALSE,paramObjForVital,headerObj)
          .then(
                  function(response) {
                      console.log(response);
           
          
          if (response.data.status == "success")
              $scope.vitalFinalList = [];
              $scope.patientViatalList  = response.data.listObject;
              
              $scope.vitalDateList = [];
              $scope.vitalNameList = [];
              

              angular.forEach($scope.patientViatalList, function(value,key) {
					 $scope.flagTime=false;
					  if($scope.vitalDateList.length > 0){
							angular.forEach($scope.vitalDateList, function(value3,key) {
								if(value3.createdTime1 == value.postFactoTime){
									$scope.flagTime = true;
								}
							});
						}
						 if(!$scope.flagTime){
							var newArr={};
							newArr.createdTime=value.createdDate +"   "+value.postFactoTime;
							 newArr.createdTime1=value.postFactoTime;
							 newArr.createdDate=value.createdDate;
							 value.sortedDateTime =value.createdDate +"   "+value.postFactoTime;
							$scope.vitalDateList.push(newArr);
						 }
						 console.log("$scope.createdTime",$scope.vitalDateList);
						 
				});
              
              $scope.vitalsListTest=[];
              angular.forEach($scope.vitalsList, function(value1 , key1) {
              angular.forEach($scope.patientViatalList, function(value,key) {
                    if(value1.vitalId == value.vitalId){
                  	  var newArr = {};
                  	  newArr.value = value.value;
                  	  newArr.vitalId = value.vitalId;
                  	  newArr.appointmentVitalMapperId = value.appointmentVitalMapperId;
                  	  newArr.sortedDateTime =value.createdDate +"   "+value.postFactoTime;
                  	  $scope.vitalsListTest.push(newArr);
                    }
              });
              });
              
              console.log("sortedDataList",$scope.vitalsListTest);
              
              if($scope.forthTabLevel == 12){
					$scope.onClick();
				}
           //   $scope.onClick();
//          console.log("patientVitalList",response.data.listObject);
//          console.log("vitalList",$scope.vitalsList);
              
              $scope.vitalsListFOR=[];
				if($scope.showHeadCircuVital == false){
					angular.forEach( $scope.vitalsList, function( value,key) {
						if(value.vitalId != 15){
							$scope.vitalsListFOR.push(value);	
						}
					});
					$scope.vitalsList=[];
					$scope.vitalsList=$scope.vitalsListFOR;
				}
				
           angular.forEach($scope.vitalsList, function(value , key) {
          	 var checkFlag = false;
               angular.forEach($scope.patientViatalList, function(value1 , key1) {
                   	
              	
              	 if(value.vitalId == value1.vitalId)
              		 {
              		 	checkFlag = true;
              		 	$scope.newArr = {};
              		 	$scope.newArr.vitalId = value.vitalId;
              		 	$scope.newArr.minValue = value.minValue;
              		 	$scope.newArr.maxValue = value.maxValue;
              		 	$scope.newArr.vitalName = value.vitalName;
              		 	$scope.newArr.vitalIconPath = value.vitalIconPath;
              		 	$scope.newArr.unit = value.unit;
              		 	$scope.newArr.vitalValue = value1.value;
              		 	$scope.newArr.isReviewedFlag = value1.isReviewedFlag;
              		 	$scope.newArr.vitalOrderOpd  = value.vitalOrderOpd;
              		 	$scope.newArr.isRequired = value.isRequired;
							$scope.newArr.checkQualifier = true;
              		 	if($scope.roleId == 3)
          		 		{
              		 		$scope.newArr.appointmentVitalMapperId = value1.appointmentVitalMapperId;
          		 		}
              		 	
                         
              		 }
              	
               		});
									if (checkFlag == true) {
											$scope.vitalFinalList.push($scope.newArr);
										} else {
											// alert("fdvfd");
											var newArr = {};
											newArr.vitalId = value.vitalId;
                              		 	newArr.minValue = value.minValue;
                              		 	newArr.maxValue = value.maxValue;
                              		 	newArr.vitalName = value.vitalName;
                              		 	newArr.vitalIconPath = value.vitalIconPath;
                              		 	newArr.unit = value.unit;
                              		 	newArr.isRequired = value.isRequired;
//                              		 	newArr.vitalValue = 0;
//                              		 	newArr.vitalValue = value.vitalValue;
                              		 	if(value.vitalValue != undefined ){
                              		 		newArr.vitalValue = value.vitalValue;
                              		 	}else{
                              		 		newArr.vitalValue =""
                              		 	}
                              		 	newArr.isReviewedFlag = 'N';
                              		 	newArr.vitalOrderOpd  = value.vitalOrderOpd;
                              			newArr.checkQualifier = true;
                              		 	if($scope.roleId == 3)
                          		 		{
                              		 		newArr.appointmentVitalMapperId ='';
                          		 		}
											$scope.vitalFinalList.push(newArr);
										}
          	       
              });
           var counter=0;
              angular.forEach($scope.vitalFinalList,function(value,key){
              	if( counter == 0){
              		if(value.vitalId == 2){
                      	angular.forEach($scope.patientViatalList, function(value1 , key1) {
                      		 if(value.vitalId == value1.vitalId)
                         		 { 
                      			 if(counter == 0){
                      			 if(value1.value != "0"){
                      				 value.vitalValue = value1.value;
                          			 counter++;
                      			 }
                      			 }
                         		 }
                      	 });
                      	}
              	}
              	
              });
              var counterWt=0;
              	if( counterWt == 0){
              		
                      	angular.forEach($scope.patientViatalList, function(value1 , key1) {
                      		 if(value1.vitalId == 1)
                         		 { 
                      			 if(counterWt == 0){
                      			 if(value1.value != "0"){
                      				 $scope.patientDetails.weight = value1.value;
	                                	$scope.patientDetails.vitalUnit = value1.unitDesc;
	                                	counterWt++;
                      			 }
                      			 }
                         		 }
                      	 });
              	}
              var datalistVital = $scope.vitalFinalList;
				for (var i = 0; i < datalistVital.length; i++) {
					if($scope.vitalFinalList[i].vitalId != 2){
						$scope.vitalFinalList[i].vitalValue = '';
					}
				}
				console.log("refreshlist",$scope.vitalFinalList);
//           console.log("Final Vital Arr",JSON.stringify($scope.vitalFinalList));
					
					$rootScope.stopLoader();
                                                        
                  });		
		}
      $scope.thumbSearch = function()
      {
           try {
                  $rootScope.startLoader();
                     var quality = 60; //(1 to 100) (recommanded minimum 55)
                     var timeout = 10; // seconds (minimum=10(recommanded), maximum=60, unlimited=0 )
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
      
  
      var SearchItems = function (search) {
          if (!self.model.patientId) {
              growl.warning("Please select a patient");
              return;
          }
          
        if ($scope.admissionId == undefined || $scope.admissionId == null || $scope.admissionId == 0) {
              growl.warning("Patient Is Not Admitted");
              return;
          }
        if (search.length < 1) return;
  
        if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
			var regex=/[^a-z]/gi;
		  search=search.replace(regex," ");

		  var URI = BASE_URL + ROOT_URL + "/api/opd/getDrugList?unitId="+$scope.unitId+"&orgId="+$scope.organizationId+"&searchDrug="+search;
		  return BillingGenericService.serviceAction(METHOD_GET,URI, null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					 $rootScope.stopLoader();
            if (response && response.data && response.data.listObject) {
                self.model.items = response.data.listObject;
                angular.forEach(self.model.items, function (item){
                    item.details = "";
                    item.id = 0;
                    item.details = item.medicationName;
                    item.id = item.drugId;
                });
                if (search) {
                        return $filter('filter')(self.model.items, { $: search });
                } else {
                    return self.model.items;
                }
            }else{
                NoItemFound(search);
                return self.model.items;
            }
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
  
          var URI = CONSTANTS.GLOBAL.GENERIC_API_SEARCH + $scope.unitId +"/" + self.model.isOTC + "/" + search + "/" + $cookies.getObject('cookieObject').storeId;
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
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
        if (!!item.id) {
          var storeId1 = $scope.storeId !== undefined && $scope.storeId !== null ? $scope.storeId : 0;
          var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId+ '/'+storeId1 + '/' + item.id + '/' + $scope.entitlementObj.paymentEntitlementTypeId +'/walkInPrescription';
  
          $rootScope.startLoader();
          GenericService.serviceAction("GET", URI, {})
            .then(function (response) {
             var selectedBatch = response.data.batch.find(element => element.batchId == 0);
              var item = {
                isConsumed:false,		
                itemId: response.data.itemId,
                itemCode: response.data.itemCode,
                itemName: response.data.itemName,
                formulationType: response.data.formulationType,
                stock: response.data.currentStock,
                genericId: response.data.genericId,
                genericName: response.data.genericName,
                manufacturerId: response.data.manufacturerId,
                manufacturerName: response.data.manufacturerName,
                strength: response.data.strength,
                productCategoryId:response.data.productCategoryId,
                isOTC: self.model.isOTC,
                isLasa: response.data.isLasa,
                durationCount:1,
                durationUnit:"Day",
                selectedBatch: !!selectedBatch?selectedBatch:commonDetailService.getDefaultBatchFromConfig(response.data.batch),
                uom: [{
                    uomDetails: "Select UOM",
                }],
                quantity:1
              };

              if($scope.frequencyList!=undefined && $scope.frequencyList!=null && $scope.frequencyList.length!=0)
            	  {
              $scope.frequencyList.forEach(function (freq) {
            	  if(freq.frequency.toUpperCase()=='OD')
            		  {
            		  item.searchItem=freq.frequency
            		  item.frequencyId = freq.frequency_id;
            		  item.noOfAdministrationTime=freq.noOfAdministrationTime;
            		  }
              });
            	  }

              
              var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.ITEM_MASTER.DETAILS_API  +$scope.organizationId + '/' + response.data.itemId;
              var data = {};
               GenericService.serviceAction("GET", URI, data).then(function (response) {
              	      self.model.itemObjectd ={};
                     if(response.data.unitOfMeasurementDetailsResponse!=null)
                     {
                     	response.data.unitOfMeasurementDetailsResponse.forEach(function(item){
           				self.model.itemObjectd.uomList = item.uomList;
                        $scope.selectedUom ={};
           				angular.forEach($scope.uomUnits,function(v,k){
           					if(self.model.itemObjectd["uomList"][2].uomUnitId == v.id){
           						$scope.selectedUom.id = v.id;
           						$scope.selectedUom.unit = v.unit;
           						
           					}
           				});
           				
           		
             	          });
                     	
                        $timeout( function(){
                        	item.selectedUom = {};
                        	item.selectedUom = $scope.selectedUom;
                     	    bill.items.push(item);
                            var resArr = [];
                            bill.items.filter(function(item1){
                              var i = resArr.findIndex(x => (x.itemId == item1.itemId));
                              if(i <= -1){
                                    resArr.push(item1);
                              }
                            });
                              if(bill.items.length!=resArr.length)
                              	{
                              	growl.error("Duplicate medication can not be added");
                              	}
                              bill.items=resArr;
                            self.model.search = '';
                        },2000);
                     	}else{

                        	item.selectedUom = {};
                        	item.selectedUom = $scope.selectedUom;
                     	    bill.items.push(item);
                            var resArr = [];
                            bill.items.filter(function(item1){
                              var i = resArr.findIndex(x => (x.itemId == item1.itemId));
                              if(i <= -1){
                                    resArr.push(item1);
                              }
                            });
                              if(bill.items.length!=resArr.length)
                              	{
                              	growl.error("Duplicate medication can not be added");
                              	}
                              bill.items=resArr;
                            self.model.search = '';
                        
                     	}
               });
               

               if(item.isLasa !== undefined && item.isLasa !== null && item.isLasa === true){
                   growl.warning("Item is LASA drug..!",{
                      title : WARNING_MSG
                   });
               }
               
              $rootScope.stopLoader();
            }, function (err) {
              growl.error(err.data.message);
              $rootScope.stopLoader();
            });
        }
        else{
          var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId + '/'+$scope.storeId + '/' + item + '/' + $scope.entitlementObj.paymentEntitlementTypeId;
  
          $rootScope.startLoader();
          GenericService.serviceAction("GET", URI, {})
            .then(function (response) {
              var item = {
                isConsumed:false,		
                itemId: response.data.itemId,
                itemCode: response.data.itemCode,
                itemName: response.data.itemName,
                formulationType: response.data.formulationType,
                stock: response.data.currentStock,
                genericId: response.data.genericId,
                genericName: response.data.genericName,
                manufacturerId: response.data.manufacturerId,
                manufacturerName: response.data.manufacturerName,
                strength: response.data.strength,
                productCategoryId:response.data.productCategoryId,
                isOTC: self.model.isOTC,
                isLasa : response.data.isLasa,
                durationCount:1,
                durationUnit:"Day",
                quantity:1
              };
              if($scope.frequencyList!=undefined && $scope.frequencyList!=null && $scope.frequencyList.length!=0)
        	  {
          $scope.frequencyList.forEach(function (freq) {
        	  if(freq.frequency.toUpperCase()=='OD')
        		  {
        		  item.searchItem=freq.frequency
        		  item.frequencyId = freq.frequency_id;
        		  item.noOfAdministrationTime=freq.noOfAdministrationTime;
        		  }
          });
        	  }

             if(item.isLasa !== undefined && item.isLasa !== null && item.isLasa === true){
                 growl.warning("Item is LASA drug..!",{
                    title : WARNING_MSG
                 });
             }

              bill.items.push(item);
              self.model.search = '';
              $rootScope.stopLoader();
            }, function (err) {
              growl.error(err.data.message);
              $rootScope.stopLoader();
            });
        }
      }
      var isStoreChanged = function()
      {
          console.log("WalkInSale : ",self.model.walkInSale.items);
          if(self.model.walkInSale.items.length==0)
          {
              $rootScope.storePopup();
          }
          else
          {
              StoreChangePopup();
          }
      }

      $scope.GetUOMUnitList = function () {
          var URI = CONSTANTS.GLOBAL.UOM_UNITS + "/"+ $scope.organizationId;

          $rootScope.startLoader();
          GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
              $scope.uomUnits = response.data;
            },
            function (err) {
              $rootScope.stopLoader();
            });
        }
       $scope.GetUOMUnitList();

      var AddItem = function (index, bill) {
        bill.items.push(CopyItem(index, bill));
        SetFocus();
      }
  
      var CopyItem = function (index, bill) {
        var item = bill.items[index];
        return {
          itemCode: item.itemCode,
          itemName: item.itemName,
          itemId: item.itemId,
          genericName: item.genericName,
          manufacturerName: item.manufacturerName,
        //  batch: item.batch,
        //  selectedBatch: item.batch[0],
       //   uom: item.uom,
       //   selectedUom: item.uom[0],
       //   taxId: item.taxId,
      //    taxName: item.taxName,
       //   taxPercent: item.taxPercent,
          formulationType: item.formulationType,
          stock: item.stock,
          strength: item.strength,
          saleDetailPayeeMapperDtoList: []
        }
      }
  
      var RemoveItem = function (index)
      
      {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
        var item = self.model.walkInSale.items[index];
        var tempList = [];
        if (!!item.allocationIds) {
          var URI = CONSTANTS.PHARMACY.OP.WALK_IN_SALES.ALLOCATE_API + $scope.unitId + "/"+$scope.organizationId +"/" +$scope.storeId + "/" +bill.id + "/" + item.allocationIds;
  
          $rootScope.startLoader();
          GenericService.serviceAction("DELETE", URI, {}).then(
            function (response) {
              if (!!response.data) {
                bill.items.splice(index, 1);
              }
              $rootScope.stopLoader();
              growl.success(response.data.message);
  
            },
            function (ex) {
              $rootScope.stopLoader();
              growl.error('Error while deleting allocated quantity');
            });
          
          //Remove From Contract
          if(item.contractId!=null&&item.contractId!=undefined&&item.contractId!='')
              {
              
              var itemObj ={
              
                  //admissionId : $scope.admissionId,
                  encounterId : $scope.encounterId,
                  patientId : $scope.patientId,
                  creditPayable : item.creditPayable,
                  contractId : item.contractId,
                  payeeId :item.payeeId,
                  creditItemQuantity:item.creditItemQuantity,
                  productCategoryId:item.productCategoryId
              }
                var URI = CONSTANTS.PHARMACY.OP.WALK_IN_SALES.REMOVE_FROM_CONTRACT;
                GenericService.serviceAction("POST", URI, itemObj).then(
                  function (response) 
                  {
                    if (response.data.status=="success") 
                    {
                        bill.items.splice(index, 1);
                        CalculateItemSum(bill.items);
                    }
                  },
                  function (ex) {
                    growl.error('Error while deleting allocated quantity');
                  });  
              
              }
          
        } else {
                angular.forEach(self.model.walkInSale.items, function(value){
                    if(item.itemId !== value.itemId){
                        tempList.push(value);
                    }
                });
                self.model.walkInSale.items = [];
                console.log(self.model.walkInSale.items);
                self.model.walkInSale.items = tempList;
        }
        self.model.walkInSale.items.forEach(function (billitem) {
           console.log(billitem.itemName);
          billitem['isBatchValid'] = false;
          billitem['isUOMValid'] = false;
          billitem['isQuantityValid'] = false;
          billitem['isItemValid'] = false;
        });
        console.log(self.model.walkInSale.items);
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

      $scope.confirmMessage = "Are you sure you want to Save?";

      $scope.saveMedicationPopUp = function(){
    	  if (ValidateBill(self.model.walkInSale)) {
                if(self.model.walkInSale.items !== undefined && self.model.walkInSale.items !== null && self.model.walkInSale.items.length>0){
                   var checkLasa = false;
                   angular.forEach(self.model.walkInSale.items, function(item){
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
                }
              var popup = angular.element('#confirmSave');
              popup.modal('show');
          }
      }
      var SavePopup = function () {
        if (ValidateBill(self.model.walkInSale)) {
          var popup = angular.element('#confirmSave');
          popup.modal('show');
        }
      }
  
        var StoreChangePopup = function () {
            var popup = angular.element('#storeChangePopUp');
            popup.modal('show');
        }
  
        var resetStore = function()
        {
           // ClearFields(self.model.walkInSale.items);
            self.model.walkInSale.items=[];
            $rootScope.storePopup();
        }
  
      var ConfirmationPopup = function () {
        var popup = angular.element('#confirmationPopUp');
        popup.modal('show');
      }
      
      $scope.patientSaleTypeId = 1;
  
      $scope.isWalkInSales = false;
    
      var WalkInSalesFlagStatus = function (bill) {
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
          //dinesh
        
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
          //dinesh
          $scope.encounterId = undefined;
          $scope.patientSaleTypeId = 3;
          $scope.patientSaleTypeIdFlag = false; 
          $scope.isWalkInSales = self.model.walkInSale.isWalkInSales;
        }
      }
  
      var ValidateQuantity = function (item) {
        var isValid = true;
//        if(item.dose==undefined || item.dose==0){
//        	growl.error('Please Enter Dose');
//        	isValid = false;
//        }else if(item.doseUnit == undefined){
//        	growl.error('Please Select Dose Unit');
//             isValid = false;
//        }else 
        	if(item.frequencyId== undefined || item.frequencyId == null || item.frequencyId == ""){
        	growl.error('Please Enter Frequency');
        	isValid = false;
        }else if(item.durationCount==undefined || item.durationCount==0){
        	growl.error('Please Enter Duration');
        	isValid = false;
        }else if(item.durationUnit==undefined){
        	growl.error('Please Select Duration Unit');
        	isValid = false;
        } else if (isNaN(parseFloat(item.quantity))) {
          item['isQuantityValid'] = true;
          growl.error("Please enter quantity");
          isValid = false;
        } else if (parseInt(item.quantity) == 0) {
          item['isQuantityValid'] = true;
          isValid = false;
          growl.error('Quantity cannot be zero');
        } else {
          item.quantity = parseInt(item.quantity);
        }
        return isValid;
      }
  
      var ValidateBill = function (bill) {
        var isValid = true;
        if($scope.admissionId==undefined || $scope.admissionId==null || $scope.admissionId==0)
		  {
        	$scope.kitMasterList=[];
        	growl.warning("Patient Is Not Admitted");
		  }  
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
          }
          if (!bill.patientName) {
            isValid = false;
            self.model.isPatientNameValid = true;
          }
          if (!bill.patientAddress) {
            isValid = false;
            self.model.isPatientAddressValid = true;
          }
          if (!bill.patientGender.id) {
            isValid = false;
            self.model.isPatientGenderNewValid = true;
          }
          if (!bill.age) {
            isValid = false;
            self.model.isAgeValid = true;
          }
          if (!bill.mobileNumber) {
              isValid = false;
              self.model.isPatientMobileNumberValid = true;
            }
        }
  
        if (!self.model.isOTC) {
          if (!bill.doctorName) {
            isValid = false;
            self.model.isDoctorNameValid = true;
          }
        }
  
        if (!bill.items || bill.items.length <= 0) {
          isValid = false;
          growl.error("Please add items in the bill");
        } else {
          bill.items.forEach(function (billitem) {
            if (!billitem.isOTC) {
              if (!bill.doctorName) {
                isValid = false;
                self.model.isDoctorNameValid = true;
              }
//              if (!bill.doctorAddress) {
//                isValid = false;
//                self.model.isDoctorAddressValid = true;
//              }
            }
            billitem['isItemValid'] = false;
            billitem['isQuantityValid'] = false;
            if (!billitem.quantity) {
              billitem['isQuantityValid'] = true;
              isValid = false;
            }
  
            if (!ValidateQuantity(billitem)) {
              isValid = false;
            }
          });
          return isValid;
        }
      }
  
      var ClearFields = function (bill) {
  
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
        bill.Id = "1" + (new Date()).getTime();
        bill.mobileNumber = null;
        
        //Ganesh
        $scope.isValidated = false;
        
        self.model.patientSearchData = [];
        self.model.patientId = null;
        self.model.admissionId = null;
        //self.model.patientSearchObj = null;
        
        //Ganesh
        $scope.encounterId = null;
        $scope.admissionId = null;
        $scope.patientId = null;
        bill.items = [];
      }

      var ClearBill = function (bill) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
        self.model.walkInSale.prefix = self.model.prefixList[0];
        self.model.walkInSale.patientName = undefined;
        self.model.walkInSale.patientAddress = undefined;
        self.model.walkInSale.patientGender = self.model.patientGenderList[0];
        self.model.walkInSale.age = undefined;
        self.model.walkInSale.doctorName = undefined;
        self.model.walkInSale.doctorAddress = undefined;
        self.model.walkInSale.mobileNumber = undefined;
        self.model.search = undefined;
        self.model.barcode = undefined;
       
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
        self.model.admissionId = null;
        self.model.patientSearchObj = null;
        $scope.isValidated = false;
        
        self.model.walkInSale.mobileNumber = undefined;
        self.model.walkInSale.items = [];
  
        // if (bill.items.length > 0) {
        //   var URI = CONSTANTS.PHARMACY.OP.WALK_IN_SALES.ALLOCATE_API + $scope.unitId + "/"+$scope.organizationId +"/"+$scope.storeId +"/0/"+ bill.id;
  
        //   $rootScope.startLoader();
        //   GenericService.serviceAction("DELETE", URI, {}).then(
        //     function (response) {
        //       if (!!response.data) {
        //         bill.items = [];
        //       }
        //       $rootScope.stopLoader();
        //       growl.success('Bill cleared successfully');
        //     },
        //     function (ex) {
  
        //       $rootScope.stopLoader();
        //       growl.error('Error while clearing the bill');
        //     });
        // }
      }
  
  
      var SetFocus = function () {
        self.model.barcode = undefined;
//        $window.document.getElementById('barcode').focus();
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
            AddItemToBill(self.model.walkInSale, itemId);
          }
  
          event.stopPropagation();
          event.preventDefault();
  
          SetFocus();
        }
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
        for (let generic of arraylist){
          genericMasterList.push(generic);
        }       
        $rootScope.stopLoader();
      } 
  
      initializeController();
      
      $scope.doseList = function (){
        var URI =BASE_URL + ROOT_URL +"/api/globals/strength/units/"+$scope.organizationId;
        GenericService.serviceAction("GET", URI, {}).then(
        function(response) {
        $scope.doseList = [];
        $scope.doseList = response.data;

        });
      }
      $scope.doseList();

      $scope.medicationInstructions = [];
      $scope.instructionData = function () {
        var URI = BASE_URL + ROOT_URL + '/api/common/getActiveInspectionMasterList';
        var commonObj = {
          isMedication : 'Y',
          organizationId : $scope.organizationId
        }
        BillingGenericService.serviceAction(METHOD_POST, URI,commonObj,NOTIFICATION_MSG_STATUS_FALSE).then(
          function(response) {
            console.log(response);
            if (response.data.status == "success")
              $scope.medicationInstructions = response.data.listObject;
          });
      }
      $scope.instructionData();

      $scope.searchMedicationFreItems = function(searchKeyword){
        $scope.searchItemList=[];
        var unitId = $scope.unitId;
        var orgId = $scope.organizationId;

        var URI = BASE_URL + ROOT_URL + "/api/opd/getFrequencyList" + "?unitId=" + 
          unitId + "&orgId=" + orgId + "&searchPar=" + searchKeyword
        return BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
          if(response.data.status == "success"){
            $scope.searchItemList = response.data.listObject;
            if(searchKeyword=='')
            	{
            	$scope.frequencyList=response.data.listObject;
            	}
          }
          return $scope.searchItemList;
        });
      }
      $scope.searchMedicationFreItems('');
      $scope.addFrequencyHistoryItemList = function($item, $model, $label,index){
        console.log($item);
        self.model.walkInSale.items[index].frequencyId = $item.frequency_id;
        $scope.frequencyId = $item.frequency_id;
        self.model.walkInSale.items[index].noOfAdministrationTime = $item.noOfAdministrationTime;
        self.model.walkInSale.items[index].durationUnit=undefined;
      }

      $scope.addDurationItemList = function($item, $model, $label,index){
        console.log($item);
        $scope.storeItemHistory= $item;
        $scope.freQua = 0;
        var medicationStartDate=new Date();
        self.model.walkInSale.items[index].frequencyId = $item.frequencyId;
        self.model.walkInSale.items[index].noOfAdministrationTime = $item.noOfAdministrationTime;
        $scope.freQua = $item.noOfAdministrationTime;
        var dur = 0;
        var endDate=new Date();
        if(self.model.walkInSale.items[index].durationCount == '')
        {
          dur = 0;
        }
        else
        {
          if(self.model.walkInSale.items[index].durationUnit == "Day")
          {
             dur = self.model.walkInSale.items[index].durationCount;
             endDate = moment(medicationStartDate).add(dur-1, 'days');
          }
          else if(self.model.walkInSale.items[index].durationUnit == "Week")
          {
             var  caldur = self.model.walkInSale.items[index].durationCount;
             dur = caldur * 7;
             endDate = moment(medicationStartDate).add(dur, 'days');
          }
          else if(self.model.walkInSale.items[index].durationUnit == "Month")
          {
             var  caldur = self.model.walkInSale.items[index].durationCount;
             dur = caldur * 30;
             endDate = moment(medicationStartDate).add(dur, 'days');
          }
          else if(self.model.walkInSale.items[index].durationUnit == "Year")
          {    
            var  caldur = self.model.walkInSale.items[index].durationCount;
            dur = caldur * 365;
            endDate = moment(medicationStartDate).add(dur, 'days');
          }
          self.model.walkInSale.items[index].quantity = $item.noOfAdministrationTime * dur;
          self.model.walkInSale.items[index].startDate = medicationStartDate;
          self.model.walkInSale.items[index].endDate = endDate;
          
        }
        self.model.walkInSale.items[index].quantity = $item.noOfAdministrationTime * dur;
        self.model.walkInSale.items[index].startDate = medicationStartDate;
        self.model.walkInSale.items[index].endDate = endDate;
      }
      
      $scope.saveMedicationForIp = function(items) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.popUpFlag = true;
			$rootScope.startLoader();
			var medicationDetails = items;
			$scope.medicationDetails=self.model.walkInSale;
			var medicationDetailList = [];

			var data = {
				"organizationId" : $scope.organizationId,
				"unitId" : $scope.unitId,
				"visitTypeId" : $scope.visitTypeId,
				"employeeId" : $scope.employeeId,
				"doctorId" : $scope.commonDoctorId,
				"prescriptionDate" : moment(new Date())
						.format('YYYY-MM-DD HH:mm:ss'),
				"authorisedBy" : $scope.commonDoctorId,
				"authorisedDate" : moment(new Date())
						.format('YYYY-MM-DD HH:mm:ss'),
				"addedBy" : $scope.userId,
				"addedDate" : moment(new Date()).format(
						'YYYY-MM-DD HH:mm:ss'),
				"admissionId" : $scope.admissionId,
				"patientId" : self.model.patientId,
				"priorityId" : 2,
				"isDischarge" : false,
				"deptId" : $scope.deptId,
				 "isFromOP" :false,
				 "medicationType":1,
				 "prescriptionDetailsDtosList":[]
				};
			var flag = true;
			angular
					.forEach(
							medicationDetails,
							function(value, key) {

								var instructionId = value.instructionId;
								if (value.medicationStartDate != ''
										&& value.medicationEndDate != ''
										&& value.durationCount != ''
										&& value.durationUnit != ''
//										&& value.instructionId != ''
										&& value.quantity != '') {

									var medicationDetailsObj = {};
									
									medicationDetailsObj["authorisedBy"]=$scope.commonDoctorId;
									medicationDetailsObj["authorisedDate"]=moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
									medicationDetailsObj["dose"]=value.dose;
									medicationDetailsObj["doseUnitId"]=value.doseUnit !=undefined && value.doseUnit!=null ? parseInt(value.doseUnit) : undefined;
									medicationDetailsObj["duration"]=value.durationCount;
									medicationDetailsObj["durationUnit"]=value.durationUnit;
									medicationDetailsObj["factorId"]=2;
									medicationDetailsObj["frequencyId"]=value.frequencyId;
									medicationDetailsObj["genericName"]=value.genericName;
									medicationDetailsObj["instructionDesc"]="";
									medicationDetailsObj["isDischarge"]=false;
									medicationDetailsObj["isMedicationStop"]='N';
									medicationDetailsObj["isNonFormulary"]='N';
									medicationDetailsObj["itemId"]=value.itemId;
									medicationDetailsObj["itemName"]=value.itemName;
									medicationDetailsObj["orderQty"]=value.quantity;
									medicationDetailsObj["prescribedDose"]=value.dose!=undefined && value.dose!=null ? value.dose.toString() : undefined ;
									medicationDetailsObj["prescriptionDate"]=moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
									medicationDetailsObj["priorityId"]=2;
									medicationDetailsObj["routeId"]="";
									medicationDetailsObj["startDate"]=moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
									medicationDetailsObj["statflag"]=false;
									medicationDetailsObj["stopDate"]=(value.endDate != undefined && value.endDate != null && value.endDate != '') ? value.endDate.format('YYYY-MM-DD HH:MM:SS') : moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
									medicationDetailsObj["storeId"]=$scope.storeId;
									medicationDetailsObj["strength"]=parseInt(value.strength!=undefined && value.strength!=null ? value.strength.split(" ")[0]:"");
									medicationDetailsObj["uomUnitId"]="";
									data.prescriptionDetailsDtosList.push(medicationDetailsObj);
								} else {
									flag = false;
								}
							});
			if (data.prescriptionDetailsDtosList.length > 0) {
				console.log("medicationDetailList", data);
					var URI = BASE_URL + ROOT_URL
					+ "/api/pharmacy/ip/pharmacology/validation/savePresWithouthPharmaValidation";
					GenericServiceParamHeader
						.serviceAction(METHOD_POST, URI,
								data,
								NOTIFICATION_MSG_STATUS_TRUE)
						.then(
								function(response) {
									if (response.data.status == "success") {
										$scope.medicationDetails = [];
										self.model.walkInSale.items=[];
										self.model.patientSearchData = [];
                                        $scope.patientDetails = {};
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
                                        $scope.showItemsearch();
                                        $scope.entitlementObj={
                                          "paymentEntitlementTypeId":1,
                                          "searchbyTypeId":1
                                        }
                                        var data = {
                                    	        "appName": "HIS",
                                    	        "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                                    	        "reportDesignFilePath": "prescrption.rpt",
                                    	        "reportFormat": "PDF",
                                    	        "exportFileName": "WalkInPrescriptionReport",
                                    	        "reportParams": [
                                    	          {
                                    	            "paramName": "id",
                                    	            "paramType": "Integer",
                                    	            "paramValue": response.data.object.prescriptionId
                                    	          }
                                    	        ]
                                    	      };
                                    	      reportService.generate(REPORT_API_URI1, data);
										/*setTimeout(function () {
											location.reload();
							            }, 1000);*/
										$rootScope.stopLoader();
									} else {
										$rootScope.stopLoader();
									}
								});
			} else {
				growl
						.error(
								"Please add Medication and then save !!",
								{
									title : ERROR_MSG
								});
				$scope.medicationDetailList = [];
				$rootScope.stopLoader();
				return false;
			}
		}
      $scope.saveMedicationForOp = function(items) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.popUpFlag = true;
			$rootScope.startLoader();
			var medicationDetails = items;
			$scope.medicationDetails=self.model.walkInSale;
			var medicationDetailList = [];

			var data = {
				"organizationId" : $scope.organizationId,
				"unitId" : $scope.unitId,
				"visitTypeId" : $scope.visitTypeId,
				"employeeId" : $scope.employeeId,
				"doctorId" : $scope.commonDoctorId,
				"prescriptionDate" : moment(new Date())
						.format('YYYY-MM-DD HH:mm:ss'),
				"authorisedBy" : $scope.commonDoctorId,
				"authorisedDate" : moment(new Date())
						.format('YYYY-MM-DD HH:mm:ss'),
				"addedBy" : $scope.userId,
				"addedDate" : moment(new Date()).format(
						'YYYY-MM-DD HH:mm:ss'),
				"encounterId" : $scope.encounterId,
				"patientId" : self.model.patientId,
				"priorityId" : 2,
				"deptId" : $scope.deptId,
				 "isFromOP" :true,
				 "medicationType":1,
				 "prescriptionDetailsDtosList":[]
				};
			var flag = true;
			angular
					.forEach(
							medicationDetails,
							function(value, key) {

								var instructionId = value.instructionId;
								if (value.medicationStartDate != ''
										&& value.medicationEndDate != ''
										&& value.durationCount != ''
										&& value.durationUnit != ''
//										&& value.instructionId != ''
										&& value.quantity != '') {

									var medicationDetailsObj = {};
									
									medicationDetailsObj["authorisedBy"]=$scope.commonDoctorId;
									medicationDetailsObj["authorisedDate"]=moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
									medicationDetailsObj["dose"]=value.dose;
									medicationDetailsObj["doseUnitId"]=parseInt(value.doseUnit);
									medicationDetailsObj["duration"]=value.durationCount;
									medicationDetailsObj["durationUnit"]=value.durationUnit;
									medicationDetailsObj["factorId"]=2;
									medicationDetailsObj["frequencyId"]=$scope.frequencyId;
									medicationDetailsObj["genericName"]=value.genericName;
									medicationDetailsObj["instructionDesc"]="";
									medicationDetailsObj["isDischarge"]=false;
									medicationDetailsObj["isMedicationStop"]='N';
									medicationDetailsObj["isNonFormulary"]='N';
									medicationDetailsObj["itemId"]=value.itemId;
									medicationDetailsObj["itemName"]=value.itemName;
									medicationDetailsObj["orderQty"]=value.quantity;
									medicationDetailsObj["prescribedDose"]=value.dose.toString();
									medicationDetailsObj["prescriptionDate"]=moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
									medicationDetailsObj["priorityId"]=2;
									medicationDetailsObj["routeId"]="";
									medicationDetailsObj["startDate"]=moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
									medicationDetailsObj["statflag"]=false;
									medicationDetailsObj["stopDate"]=moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
									medicationDetailsObj["storeId"]=$scope.storeId;
									medicationDetailsObj["strength"]=parseInt(value.strength.split(" ")[0]);
									medicationDetailsObj["uomUnitId"]="";
									data.prescriptionDetailsDtosList.push(medicationDetailsObj);
								} else {
									flag = false;
								}
							});
			if (data.prescriptionDetailsDtosList.length > 0) {
				console.log("medicationDetailList", data);
				var URI = BASE_URL + ROOT_URL
				+ SAVEPATIENTMEDICATION;
					GenericServiceParamHeader
						.serviceAction(METHOD_POST, URI,
								data,
								NOTIFICATION_MSG_STATUS_TRUE)
						.then(
								function(response) {
									if (response.data.status == "success") {
										$scope.medicationDetails = [];

										$rootScope.stopLoader();
									} else {
										$rootScope.stopLoader();
									}
								});
			} else {
				growl
						.error(
								"Please add Medication and then save !!",
								{
									title : ERROR_MSG
								});
				$scope.medicationDetailList = [];
				$rootScope.stopLoader();
				return false;
			}
		}
      $scope.saveWalkInPrescription = function () {
        var URI = BASE_URL + ROOT_URL + "/api/pharmacy/sales/walkins/save/prescription";
        var bill = self.model.walkInSale;
        var flag=false;
        if(self.model.patientAge != undefined){
          var age = self.model.patientAge.split('Y');
        }
        if($scope.visitTypeId!=2)
        	{
        	growl.warning("Patient Is Not Admitted");
        	}
        if($scope.admissionId>0 && $scope.visitTypeId==2)
        	{
        	if($scope.IsFinalBillGenerated!='Y')
        		{
        		$scope.saveMedicationForIp(bill.items);
        		}
        	else
        		{
        		growl.warning("Patient's final bill already been generated");
        		return false;
        		}
        	}
        else if($scope.encounterId>0 && $scope.visitTypeId==1)
        	{
        	console.log("don't call")
//        	$scope.saveMedicationForOp(bill.items);
        	}
        else if(flag==true)
        	{
        if(bill.prefix.id == undefined || bill.patientName == undefined || bill.mobileNumber == undefined || bill.age == undefined || bill.patientGender.id == undefined){
          bill.prefix.id = self.model.prefixId;
          bill.age = age[0];
          bill.patientName = self.model.patientName;
          bill.mobileNumber = self.model.mobileNumber;
          bill.patientGender.id = self.model.genderId;
        }
      
        var data = {
          "visitTypeId":1,
          "tempPatientId": null,
          "screenId": 1,
          "prefixId": bill.prefix.id ? bill.prefix.id : undefined,
          "patientId": self.model.patientId ? self.model.patientId : undefined,
          "patientName": bill.patientName ? bill.patientName : undefined,
          "patientAddress": bill.patientAddress ? bill.patientAddress : undefined,
          "mobileNumber": bill.mobileNumber ? bill.mobileNumber : undefined,
          "patientAge": bill.age ? bill.age : undefined,
          "patientGenderId": bill.patientGender.id ? bill.patientGender.id : undefined,
          "doctorName": bill.doctorName ? bill.doctorName : undefined,
          "doctorAddress": bill.doctorAddress ? bill.doctorAddress : undefined,
          "items": [],
          "statusId": 1,
          "unitId":$scope.unitId,
          "organizationId":$scope.organizationId,
          "userId":$scope.userId
        }
        if(bill.patientAddress == undefined){
            data.patientAddress = null;
           }
          if(bill.doctorName == undefined){
                data.doctorName = null;
              }
          if(bill.doctorAddress == undefined){
               data.doctorAddress = null;  
          }
//        var checkLasa = false;
        angular.forEach(bill.items, function (item, key) 
        {
//            if(item.isLasa !== undefined && item.isLasa !== null && !item.isLasa == true){
//                checkLasa = true;
//            }
          var billItem = {
            "itemId":item.itemId,
            "instructionId":item.instruction,
            "dose":item.dose,
            "doseUnitId":item.doseUnit,
            "frequencyId":$scope.frequencyId,
            "quantity":item.quantity
          }
          data.items.push(billItem);
        });
//        if(checkLasa){
//            checkLasa = false;
//            growl.warning("List contains LASA drugs..!",{
//              title : WARNING_MSG
//            });
//        }
        if(data.patientId == undefined){
          data.patientId = null;
        }
        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            ClearFields(bill);
            $rootScope.stopLoader();
            self.model.walkInSalesBillId = response.data.id;
            growl.success(response.data.message);
          },
          function (err) {
            $rootScope.stopLoader();
            growl.error('Error while saving walk-in sales bill');
          });
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
     // $scope.getKitMasterList();

    $scope.addKitItems = function(bill, item, model, label){
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)

            var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/getUOMForCoversheet';
//        var URI = BASE_URL + ROOT_URL + "/api/global/items/details/walkin/getKitItemsOnWalkInSales";

        var found = $scope.kitMasterList.find(element => element.id == $scope.kitId);
		var itemIds = [];
        var endDate=new Date();
        var medicationStartDate=new Date();
        var list = [];

		angular.forEach(found.items,function(value,key){
			if(value.status == true){
				itemIds.push(value.itemId);
			}

		})


        var data = {
            "storeId": $cookies.getObject('cookieObject').storeId,
//            "admissionId": ($scope.admissionId!=null && $scope.admissionId>0) ? $scope.admissionId: null,
//            "encounterId":($scope.encounterId!=null && $scope.encounterId>0) ? $scope.encounterId: null,
            "itemIds" : itemIds,
            "paymentEntitlementId" : $scope.entitlementObj.paymentEntitlementTypeId,
            "unitId":$scope.unitId ,
            "organizationId": $scope.organizationId,
//            "fromWalkInPrescription" : true,
        }

         $rootScope.startLoader();
         GenericService.serviceAction("POST", URI, data).then(function (response) {
            var list = response.data;
            angular.forEach(list, function(resItem){

                 var selectedBatch = resItem.batch.find(element => element.batchId == 0);
                  var item = {
                    isConsumed:false,
                    itemId: resItem.itemId,
                    itemCode: resItem.itemCode,
                    itemName: resItem.itemName,
                    formulationType: resItem.formulationType,
                    stock: resItem.currentStock,
                    genericId: resItem.genericId,
                    genericName: resItem.genericName,
                    manufacturerId: resItem.manufacturerId,
                    manufacturerName: resItem.manufacturerName,
                    strength: resItem.strength,
                    isLasa: resItem.isLasa,
                    productCategoryId:resItem.productCategoryId,
                    isOTC: self.model.isOTC,
                    durationCount:1,
                    durationUnit:"Day",
                    selectedBatch: !!selectedBatch?selectedBatch:commonDetailService.getDefaultBatchFromConfig(resItem.batch),
                    uom: [{
                        uomDetails: "Select UOM",
                    }],
                    quantity:1
                  };
                  item.startDate = medicationStartDate;
                  item.endDate = moment(medicationStartDate).add(item.durationCount-1, 'days');
                  if($scope.frequencyList!=undefined && $scope.frequencyList!=null && $scope.frequencyList.length!=0)
                  {
                      $scope.frequencyList.forEach(function (freq) {
                          if(freq.frequency.toUpperCase()=='OD')
                              {
                              item.searchItem=freq.frequency
                              item.frequencyId = freq.frequency_id;
                              item.noOfAdministrationTime=freq.noOfAdministrationTime;
                              }
                      });
                  }

                  if(item.selectedBatch != undefined && item.selectedBatch != null){

                       item.uom = item.selectedBatch.uom;
                       item.selectedUom = $scope.uomUnits.find(function(units){
                          return item["uom"][2].uomUnitId == units.id;
                        });

                  }
                  bill.items.push(item);
        	});
            
            angular.forEach(bill.items,function(v,k){
            	   var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.ITEM_MASTER.DETAILS_API  +$scope.organizationId + '/' + v.itemId;
                   var data = {};
                    GenericService.serviceAction("GET", URI, data).then(function (response) {
                   	      self.model.itemObjectd ={};
                          if(response.data.unitOfMeasurementDetailsResponse!=null)
                          {
                          	response.data.unitOfMeasurementDetailsResponse.forEach(function(item){
                				self.model.itemObjectd.uomList = item.uomList;
                               $scope.selectedUom ={};
                				angular.forEach($scope.uomUnits,function(v,k){
                					if(self.model.itemObjectd["uomList"][2].uomUnitId == v.id){
                						$scope.selectedUom.id = v.id;
                						$scope.selectedUom.unit = v.unit;
                						
                					}
                				});
                  	          });
                          	
                             	v.selectedUom = {};
                             	v.selectedUom = $scope.selectedUom;
                     
                          	}
                    });
            });
         
             
             
             var resArr = [];
             bill.items.filter(function(item1){
               var i = resArr.findIndex(x => (x.itemId == item1.itemId));
               if(i <= -1){
                     resArr.push(item1);
               }
             });
            if(bill.items.length!=resArr.length)
            {
                growl.error("Duplicate medication can not be added");
            }
               bill.items=resArr;
               self.model.search = '';
               $scope.kitId = null;
                $rootScope.stopLoader();
                

           }, function (err) {
           angular.forEach(bill.items, function (value, key) {
             if(key == idx){
               value.itemName="";
             }
           });
           growl.error(err.data.message);
           $rootScope.stopLoader();
         });

    }

	  $scope.kitMasterItemsList = [];
	 $scope.kitItemDetails =  function(){
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
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

      var BatchChange = function (index, bill, batch, parentIndex) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
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

    }
  
    function config($stateProvider) {
      $stateProvider.state("walkinPrescription", {
        url: "/walkinPrescription",
        templateUrl: 'views/PharmacyInventory/InPatient/walkin-prescription.html',
        controller: "walkinPrescription.Controller",
        title: 'Walk In Prescription',
        resolve : {
  
              data: function($q, $state, $timeout) {
                  var deferred = $q.defer();
          
                  $timeout(function() {
                      var arr = JSON.parse(localStorage.getItem('permissions'))
                       .modules.filter(function(item){
                         return (item.id == 862);
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
      .controller("walkinPrescription.Controller", walkinPrescriptionController);
  
  })();