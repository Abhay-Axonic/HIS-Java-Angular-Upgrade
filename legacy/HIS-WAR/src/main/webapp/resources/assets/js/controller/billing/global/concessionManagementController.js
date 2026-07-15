(function () {
	"use strict";
	function concessionManagementController($scope,$rootScope,BillingGenericService,$cookies,$state,growl,paymentCollFactory,reportService,GenericService,$location,loyaltiCardCallFactory){
	try{
		$rootScope.loginpage = true;
		$scope.page = "billList";
		$scope.dateOptions = {
				formatYear : 'yyyy',
				showWeeks : false,
				//minDate : new Date()
			};
		
		$scope.dateOptionsTrancs = {
				formatYear : 'yyyy',
				showWeeks : false,
				minDate : new Date()
			};
		
		
		$scope.dateOptionsTo = {
				formatYear : 'yyyy',
				showWeeks : false,
				minDate : new Date()
			};
		
		
		
		$scope.cardExpiryOptions = {
				formatYear : 'yyyy',
				showWeeks : false,
				minDate : new Date(),
			};
		$scope.chequedDateOptions = {
				formatYear : 'yyyy',
				showWeeks : false,
				minDate : moment(new Date()).subtract(90, 'days').toDate(),
			};
		$scope.openDatePicker = function() { 
			$scope.datepicker.opened = true;
		};
		$scope.openDatePickerTo = function() {
			$scope.datepickerTo.opened = true;
		};
		
		$scope.openDatePickerPatiSearch = function() {
			$scope.datepicker1.opened = true;
		};

		$scope.datepicker1 = {
				opened : false
			};
		
		$scope.datepicker = {
			opened : false
		};
		$scope.datepickerTo = {
				opened : false
			};
		
		var cookieObject = $cookies.getObject('cookieObject');
		if (cookieObject == undefined) {
			$state.go('login');
			return;
		}
		
		var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
		 if(cookieObjectAppConfig == undefined){
		  $state.go('login');
		  return;
		 }
		 else
		  {
		   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
		   
		  }
		$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
		$scope.isAppointmentRequired = cookieObjectAppConfig.isAppointmentRequired;
		$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
		$scope.selfPayeeIdCookies = cookieObjectAppConfig.selfPayeeId;
		$scope.isSchemeDiscount = cookieObjectAppConfig.isSchemeDiscount;
	
		$scope.excessAmount = parseFloat(0).toFixed(2);	
		$scope.unitId = cookieObject.unitId;
		$scope.userId = cookieObject.userId;
		$scope.organizationId = cookieObject.organizationId;
		$scope.userId =$scope.userId;
		$scope.appointmentId = 0;
		$scope.encounterId = 0;
		$scope.billing = true;
		$scope.saveBtnDisable = false;
		$scope.searchServiceFilter="";
		$scope.searchKeyword = "";
		/**================================COUNTER CODE START======================================*/
		$scope.isCounterApplicable = JSON.parse(sessionStorage.getItem('isCounterApplicable'));
		$scope.isUserCounterApplicable = JSON.parse(sessionStorage.getItem('isUserCounterApplicable'));
		if($scope.isCounterApplicable=='Y' && $scope.isUserCounterApplicable=='Y')
			{
			     var flag = $scope.getCounterDetails();
			     if(flag)
			    	 {
			    	 	$rootScope.counterId = $rootScope.counterMstObj.counterId!=""?$rootScope.counterMstObj.counterId:"";
			    	 	$rootScope.counterDesc = $rootScope.counterMstObj.counterDesc!=""?$rootScope.counterMstObj.counterDesc:"";
			    	 }
			     else
			     {
			    	 $rootScope.counterList = JSON.parse(localStorage.getItem('counterList'));
			     }
			}else{
				$rootScope.counterId = 0;
			}
		/*$scope.confirmCounter = function()
		 {
			$scope.counterMstObj = $cookies.getObject('counterMstObj');
			$rootScope.counterId = $scope.counterMstObj.counterId!=""?$scope.counterMstObj.counterId:"";
		 }*/
		
		/**================================COUNTER CODE END======================================*/
		
	
		
		//vivek added
		
		$scope.patientSearchObj = {
				"uhIdNumber"      : "",
				"patientName"     : "",
				"genderId"        : "",
				"mobileNo"        : "",
				"birthDate"       : "",
				"identificationNo":"",
				"patientType"	  :"1",
				"unitId"          :$scope.unitId,
				"organizationId"  :$scope.organizationId
		}
		
		
		
		$scope.init = function(){
			
			$scope.billing = true;
			$scope.billList = [];
			
			$scope.patientSearchObj = {
					"uhIdNumber"      : "",
					"patientName"     : "",
					"genderId"        : "",
					"mobileNo"        : "",
					"birthDate"       : "",
					"identificationNo":"",
					"unitId"          :$scope.unitId,
					"organizationId"  :$scope.organizationId,
			}
			
			var URI = BASE_URL + ROOT_URL
			+ GETACTIVEIDENTIFICATION;
	GenericService
			.serviceAction("GET", URI, {})
			.then(
					function(response) {
						$scope.identificationTypeList = [];

						if (response.data.status == "success") {
							$scope.identificationTypeList = response.data.listObject;
							

						}
					});
	
				$scope.disflg = 1;
				$scope.discountRadio = 1;
				$scope.concessionRadio =1;
				$scope.discountPercentageBillWise = '';
				$scope.discountAmountBillWise = '';
				$scope.concessionPercentageBillWise = '';
				$scope.concessionAmountBillWise = '';
				$scope.patientList = [];
				$scope.patientDetails = {};
				$scope.billServicesList = [];
				$scope.totalDiscountAmt = parseFloat(0);
				$scope.totalConcessionAmt = parseFloat(0);
				$scope.totalAmtQuaBase = 0;
				$scope.totalAmtConcession = 0;
				$scope.totalAmtDiscount = 0;
				$scope.totalAmtGst = 0;
				$scope.totalNetPayableAmt = 0;
				$scope.totalPackageConsumptionAmt = 0;
				$scope.roundOffAmt = 0;
				$scope.totalSelfPayable = 0;
				$scope.totalCreditPayable=0;
				$scope.totalCreditPayble = [];
				$scope.billObjTmp = {};
		}
		$scope.init();
		
		$scope.firstTabLevel = 1 ;
		
		$scope.setFirstTabLevel= function(newTab)
		{
			$scope.firstTabLevel = newTab;
		};
		$scope.isSetFirstTabLevel = function(tabNum)
		{
			return $scope.firstTabLevel === tabNum;
		};
		
		$rootScope.searchPatient = function(patientsearchObj){
			if($scope.patientSearchObj.uhidNumber == '' && $scope.patientSearchObj.patientName == '' )
			{
				//alert();
				growl.warning(SearchFilter_REQUIRED,{title : "WARNING!"
				});
				return false;
			}
				
			else{
					//alert("else AlERT");
				}
			if(patientsearchObj.patientName!="" && patientsearchObj.patientName.length < 3)
			{
				growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
				$scope.patientList = [];
				return false;
			}	
			if(patientsearchObj.birthDate != "" && typeof(patientsearchObj.birthDate) != "undefined"){
				patientsearchObj.birthDate = moment(patientsearchObj.birthDate).format('YYYY-MM-DD');
			}
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
			var patSearchObj = angular.copy($scope.patientSearchObj);
			patSearchObj.patientType = 1;
			if($scope.patientSearchObj.dob != "" && typeof($scope.patientSearchObj.dob) != "undefined"){
				var dt = new Date($scope.patientSearchObj.dob);
				patSearchObj.birthDate = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + dt.getFullYear();
			} 
			BillingGenericService.serviceAction(METHOD_POST,URI, patSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				$scope.searchPatientList = [];
				if (response.data.status == "success")
					$scope.patientList = response.data.listObject;
				
				if(response.data.listObject==null){
					growl.warning(response.data.message,{
						title : ERROR_MSG
					});
					$scope.patientSearchObj.birthDate="";
					$scope.patientList=[];
					return false;
				}
				
				if($scope.patientList.length == 0){
					//alert();
					growl.warning("No Result Found!",{
						title : ERROR_MSG
					});
					$scope.patientSearchObj.birthDate="";
					$scope.patientList=[];
				}
				$scope.patientSearchObj.birthDate="";
				patSearchObj = {};
			});
		}
		

	    $scope.getBillListByPatientId = function(patientId) {
	        $scope.billList = [];
	        $scope.billServicesList = [];
	        $rootScope.startLoader();
	        var URI = BASE_URL + ROOT_URL + "/api/billing/concessionManager/billDetails/" + $scope.organizationId + "/" + $scope.unitId + "/" + patientId;
	        BillingGenericService.serviceAction(METHOD_GET, URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
	            if (response.data.status == "success") {
	                $scope.billList = response.data.listObject;
	            }
	            if ($scope.billList != null && $scope.billList.length > 0){
                    $(".anchor-tag").parents().next(".old-encounter-details").toggle();
                }
	            $rootScope.stopLoader();
	        });
	    }
	    
		$scope.getPatientDetailsById = function(patientId){
			$rootScope.startLoader();
			$scope.patientDetailsList = [];
			$scope.patientDetails = {}
			var URI = BASE_URL + ROOT_URL + "/api/global/patient/billing/"+patientId;
			BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					$scope.getBillListByPatientId(patientId);
					$scope.patientDetailsList = response.data.listObject;
					$scope.patientDetails = response.data.listObject[0];
					$scope.patientDetails.patientName = $scope.patientDetails.firstName + " " + $scope.patientDetails.lastName;
					$scope.patientDetails.genderCode = $scope.patientDetails.genderName.charAt(0).toUpperCase();
					$scope.patientDetails.age = $scope.patientDetails.ageString
					$scope.patientDetails.mobileNo = $scope.patientDetails.mobileNumber;
					$('.patienttSearchResult').toggle();
					// for billList to be set blank
					
					$scope.billList=[];
				}
			});
			$rootScope.stopLoader();
		}
		
		$scope.calculation = function(servicesList){
			$scope.billServicesList = [];
			
			var servicesLists = servicesList;
			
			$scope.totalDiscountAmt = parseFloat(0);
			$scope.totalConcessionAmt = parseFloat(0);
			
			$scope.totalAmtQuaBase = 0;
			$scope.totalAmtConcession = 0;
			$scope.totalAmtDiscount = 0;
			$scope.totalAmtGst = 0;
			$scope.totalNetPayableAmt = 0;
			$scope.totalPackageConsumptionAmt = 0;
			$scope.roundOffAmt = 0;
			$scope.totalSelfPayable = 0;
			$scope.totalCreditPayable=0;
			
			
			var totalAmtQuaBase = parseFloat(0);
			var totalAmtConcession = parseFloat(0);
			var totalAmtDiscount = parseFloat(0);
			var totalAmtGst = parseFloat(0);
			var totalNetPayableAmt = parseFloat(0);
			var discountRoundOff = parseFloat(0);
			var totalSelfPayable = parseFloat(0);
			var totalCreditPayable = parseFloat(0);
			var totalPackageConsumptionAmt = parseFloat(0);
			
			
			var i = 1;
			angular.forEach(servicesLists,function(value,key)
					{
				angular.forEach(value.orderDetailsPackageMapperDtosList,function(value1,key)
						{
						  if(value1.isPackageService=='N')
						  {
							  value.isDiscountApplicable = (value.packageConsumptionAmt > 0) ? 'N':value.isDiscountApplicable;
							  if(value.isDiscountApplicable == 'Y'){
								  $scope.totalDiscountAmt = parseFloat($scope.totalDiscountAmt)+ (parseFloat(value.unitPrice * value.quantity) - parseFloat(value.concession));
								  $scope.totalConcessionAmt = parseFloat($scope.totalConcessionAmt) +  parseFloat(value.unitPrice * value.quantity);
							  	}
							  
							  	value.concessionOld = value.concession;
							  	value.discountOld = value.discount;
							  	value.concessionPercentageOld = parseFloat(isNaN(value.concessionPercentage) ? 0 : value.concessionPercentage).toFixed(2);
							  	value.discountPercentageOld = parseFloat(isNaN(value.discountPercentage) ? 0 : value.discountPercentage).toFixed(2);
							  	value.concessionPercentage = parseFloat(isNaN(value.concessionPercentage) ? 0 : value.concessionPercentage).toFixed(2);
							  	value.discountPercentage = parseFloat(isNaN(value.discountPercentage) ? 0 : value.discountPercentage).toFixed(2);
							  	value.taxPercentage = parseFloat(isNaN(value.taxPercentage) ? 0 : value.taxPercentage).toFixed(2);
							  	/*if(value.creditPayable != 0)
						  		{
							  		value.coPayPer = parseFloat((value.selfPayable/value.netAmt)*100).toFixed(2);
						  		}else{
						  			value.coPayPer = parseFloat(100);
						  		}*/

							  	value.coPayPer = value.coPayPer > 0 ? parseFloat(value.coPayPer) : parseFloat((value.selfPayable/value.netAmt)*100);
							    totalAmtQuaBase = totalAmtQuaBase + parseFloat(value.grossAmount);
								totalAmtConcession = totalAmtConcession + parseFloat(value.concession);
								totalAmtDiscount = totalAmtDiscount + parseFloat(value.discount);
								totalAmtGst = totalAmtGst + parseFloat(value.taxAmount);
								totalNetPayableAmt = totalNetPayableAmt + parseFloat(value.netAmt);
							    totalSelfPayable = totalSelfPayable + parseFloat(value.selfPayable);
								totalCreditPayable = totalCreditPayable + parseFloat(value.creditPayable);
								//discountRoundOff = parseFloat(value.roundOff);
							  }
						});
				if(i == servicesLists.length ){
					$scope.billServicesList = servicesLists;
				}
				i++;
			});
			
			$scope.totalSelfPayable = parseFloat(totalSelfPayable);
			$scope.totalCreditPayable = parseFloat(totalCreditPayable);
			
			$scope.totalAmtQuaBase =totalAmtQuaBase;
			$scope.totalAmtConcession = totalAmtConcession;
			$scope.totalAmtDiscount =totalAmtDiscount+Number(discountRoundOff);
			$scope.totalAmtGst = (totalAmtGst).toFixed(2);
			$scope.totalNetPayableAmt = totalNetPayableAmt+(Number(discountRoundOff));
			$scope.totalPackageConsumptionAmt = totalPackageConsumptionAmt;
			$scope.roundOffAmt = $scope.discountAmountBillWise>0 ? parseFloat($scope.discountAmountBillWise-$scope.totalAmtDiscount).toFixed(2) : 0;
			$scope.totalCreditPayble=(parseFloat($scope.totalCreditPayable)).toFixed(2);
			$scope.totalSelfPayble=(parseFloat($scope.totalSelfPayable)).toFixed(2);
			
			if($scope.roundOffAmt!=0 && $scope.totalSelfPayble!=0)
			{
				$scope.totalSelfPayble = parseFloat(Number(parseFloat($scope.totalSelfPayble).toFixed(2))+parseFloat($scope.roundOffAmt)).toFixed(2);
			}else{
				$scope.totalCreditPayble = parseFloat(Number(parseFloat($scope.totalCreditPayble).toFixed(2))+parseFloat($scope.roundOffAmt)).toFixed(2);
			}
			
		}
		
		//fetch Bill Details(services list)
		 $scope.getBillDetailsByBillId = function(billObj)
			{
			 
			 $scope.admissionId = null;
			 $scope.isFinalBill ='N';
			 
			 $scope.admissionId = billObj.admissionId;
			 $scope.isFinalBill = billObj.isFinalBill;
		    	$scope.billMasterIds = [];
		    	$scope.billMasterIds.push(billObj.billingMasterId);
		    	$scope.billObjTmp = {};
		    	$scope.billObjTmp = angular.copy(billObj);
		    	$scope.disflg = 1;
				$scope.discountRadio = 1;
				$scope.concessionRadio =1;
				$scope.discountPercentageBillWise = '';
				$scope.discountAmountBillWise = '';
				$scope.concessionPercentageBillWise = '';
				$scope.concessionAmountBillWise = '';
				$scope.billPayeeList = [];
				$scope.selfDueAmount = 0.00;
				$scope.creditDueAmount = 0.00;
				$scope.splitPercentage = 0.00;
		    	var URI = BASE_URL + ROOT_URL + "/api/billing/payee/bill/"+billObj.billingMasterId;
				BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success")
					{
						$scope.billServicesListTemp = response.data.object.listObject;
						$scope.calculation($scope.billServicesListTemp);
						$scope.billPayeeList = response.data.listObject;
						
						angular.forEach($scope.billPayeeList, function (payee, key) 
						{
							if(payee.payeeId==$scope.selfPayeeIdCookies)
								$scope.selfDueAmount = payee.dueAmount;
							else
								$scope.creditDueAmount = payee.dueAmount;
						});
						
						
					}
				});
			}
		 
		 
		 $scope.commonSetDefaultVal = function(){
			 
			 	$scope.discountPercentageBillWise = parseFloat(0);
				$scope.discountAmountBillWise = parseFloat(0);
				$scope.discountPercentageBillWiseActual =parseFloat(0);
				$scope.concessionPercentageBillWise =  parseFloat(0);
				$scope.concessionPercentageBillWiseActual =  parseFloat(0);
				$scope.concessionAmountBillWise =  parseFloat(0);
			 
				var i = 1;
	    		angular.forEach($scope.billServicesList, function (service, key) 
					{
	    						
		    			        var totalAmt = (parseFloat(service.quantity) * parseFloat(service.unitPrice)).toFixed(2);
	   						   
		    			        $scope.billServicesList[key].concessionPercentage = parseFloat(service.concessionPercentageOld);
	   						    $scope.billServicesList[key].concession = (parseFloat(totalAmt) * parseFloat($scope.billServicesList[key].concessionPercentageOld*0.01)).toFixed(2);
		    			        $scope.billServicesList[key].discount =((parseFloat(totalAmt) - parseFloat($scope.billServicesList[key].concessionOld))*(isNaN(service.discountPercentageOld) ? 0: (service.discountPercentageOld*0.01))).toFixed(2);
		    			       
		    			        $scope.billServicesList[key].discountPercentage = parseFloat(service.discountPercentageOld).toFixed(2);
		    			        var withConDic =parseFloat(totalAmt) - (parseFloat($scope.billServicesList[key].concession) + parseFloat($scope.billServicesList[key].discount));
		    			    	var txpre = 0;
								if(service.taxPercentage != null || service.taxPercentage > 0){
									$scope.billServicesList[key].taxPercentage = service.taxPercentage;
									txpre = service.taxPercentage;
								}else{
										$scope.billServicesList[key].taxPercentage = 0;
								}
								$scope.billServicesList[key].taxAmount = parseFloat((parseFloat(withConDic) * (txpre*0.01))).toFixed(2);
								$scope.billServicesList[key].netAmt =   (parseFloat(withConDic) + parseFloat($scope.billServicesList[key].taxAmount)).toFixed(2);
									$scope.billServicesList[key].selfPayable = (parseFloat($scope.billServicesList[key].netAmt) * (parseFloat(service.coPayPer*0.01))).toFixed(2);
									$scope.billServicesList[key].creditPayable = (parseFloat($scope.billServicesList[key].netAmt) - parseFloat($scope.billServicesList[key].selfPayable)).toFixed(2);
								
	    						
								if(i == $scope.billServicesList.length){
									$scope.calculation($scope.billServicesList);
								}
								i++;
					});
		 }
		 
		 $scope.commonFinalFunction = function(){
			 

				var i = 1;
	    		angular.forEach($scope.billServicesList, function (service, key) 
					{
	    					if(parseFloat(service.concessionPercentage) >= 0)
	    					{
	    						
		    			        var totalAmt = (parseFloat(service.quantity) * parseFloat(service.unitPrice)).toFixed(2);
	   						    $scope.billServicesList[key].concessionPercentage = parseFloat(service.concessionPercentage);
	   						    $scope.billServicesList[key].concession = (parseFloat(totalAmt) * parseFloat($scope.billServicesList[key].concessionPercentage*0.01)).toFixed(2);
		    			        $scope.billServicesList[key].discount =((parseFloat(totalAmt) - parseFloat($scope.billServicesList[key].concession))*(isNaN(service.discountPercentage) ? 0: (service.discountPercentage*0.01))).toFixed(2);
		    			        
		    			        if(parseFloat($scope.billServicesList[key].discount) != 0)
		    			        { 
		    			        	var tmpDisPer = service.discountPercentage;
		    			        	$scope.billServicesList[key].discountPercentageFull = parseFloat(tmpDisPer);
		    			    	   $scope.billServicesList[key].discountPercentage = parseFloat(service.discountPercentage).toFixed(2);
		    			    	  
		    			        }else{
		    			        	$scope.billServicesList[key].discountPercentage = parseFloat(0);
		    			        	  $scope.billServicesList[key].discountPercentageFull = parseFloat(0);
		    			        }
		    			        
		    			        $scope.billServicesList[key].discountPercentage = parseFloat(service.discountPercentage).toFixed(2);
		    			        var withConDic =parseFloat(totalAmt) - (parseFloat($scope.billServicesList[key].concession) + parseFloat($scope.billServicesList[key].discount));
		    			    	var txpre = 0;
								if(service.taxPercentage != null || service.taxPercentage > 0){
									$scope.billServicesList[key].taxPercentage = service.taxPercentage;
									txpre = service.taxPercentage;
								}else{
										$scope.billServicesList[key].taxPercentage = 0;
								}
								$scope.billServicesList[key].taxAmount = parseFloat((parseFloat(withConDic) * (txpre*0.01))).toFixed(2);
								$scope.billServicesList[key].netAmt =   (parseFloat(withConDic) + parseFloat($scope.billServicesList[key].taxAmount)).toFixed(2);
								
								$scope.billServicesList[key].selfPayable = (parseFloat($scope.billServicesList[key].netAmt) * (parseFloat(service.coPayPer*0.01))).toFixed(2);
								$scope.billServicesList[key].creditPayable = (parseFloat($scope.billServicesList[key].netAmt) - parseFloat($scope.billServicesList[key].selfPayable)).toFixed(2);
								
	    					}
							if(i == $scope.billServicesList.length){
								$scope.calculation($scope.billServicesList);
							}
							i++;
					});
		 }
		 
		 $scope.commonFinalFunctionBillDisPer = function(){
			 

				var i = 1;
	    		angular.forEach($scope.billServicesList, function (service, key) 
					{
	    			 if(service.isDiscountApplicable == 'Y'){
	    						var totalAmt = (parseFloat(service.quantity) * parseFloat(service.unitPrice)).toFixed(2);
	    						$scope.billServicesList[key].discountPercentage = parseFloat($scope.discountPercentageBillWise).toFixed(2);
	    						$scope.billServicesList[key].discountPercentageFull = parseFloat($scope.discountPercentageBillWise);
			    			    $scope.billServicesList[key].discount = ((parseFloat(totalAmt) - parseFloat($scope.billServicesList[key].concession))*(isNaN($scope.discountPercentageBillWiseActual) ? 0: ($scope.discountPercentageBillWiseActual*0.01))).toFixed(2);
		    			        var withConDic =parseFloat(totalAmt) - (parseFloat($scope.billServicesList[key].concession) + parseFloat($scope.billServicesList[key].discount));
		    			    	var txpre = 0;
								if(service.taxPercentage != null || service.taxPercentage > 0){
									$scope.billServicesList[key].taxPercentage = service.taxPercentage;
									txpre = service.taxPercentage;
								}else{
										$scope.billServicesList[key].taxPercentage = 0;
								}
								$scope.billServicesList[key].taxAmount = parseFloat((parseFloat(withConDic) * (txpre*0.01))).toFixed(2);
								$scope.billServicesList[key].netAmt =   (parseFloat(withConDic) + parseFloat($scope.billServicesList[key].taxAmount)).toFixed(2);
								$scope.billServicesList[key].selfPayable = (parseFloat($scope.billServicesList[key].netAmt) * (parseFloat(service.coPayPer*0.01))).toFixed(2);
								$scope.billServicesList[key].creditPayable = (parseFloat($scope.billServicesList[key].netAmt) - parseFloat($scope.billServicesList[key].selfPayable)).toFixed(2);
								
	    					}
							if(i == $scope.billServicesList.length){
								$scope.calculation($scope.billServicesList);
							}
							i++;
					});
		 }
		 
		 $scope.commonFinalFunctionBillConPer = function(){
			 

			 var i=1;
		       angular.forEach($scope.billServicesList, function (service, key) 
				{
  					
  					if(service.isDiscountApplicable == 'Y')
  					{
  						var totalAmt = (parseFloat(service.quantity) * parseFloat(service.unitPrice)).toFixed(2);
  						
  						 $scope.billServicesList[key].concession = ((parseFloat(totalAmt) * parseFloat($scope.concessionPercentageBillWise*0.01))).toFixed(2);
		    			        $scope.billServicesList[key].concessionPercentage = parseFloat($scope.concessionPercentageBillWise).toFixed(2);
	    			        
		    			        $scope.billServicesList[key].discount = ((parseFloat(totalAmt) - parseFloat($scope.billServicesList[key].concession))*(isNaN($scope.billServicesList[key].discountPercentage) ? 0: ($scope.billServicesList[key].discountPercentage*0.01))).toFixed(2);
	    			           var withConDic =parseFloat(totalAmt) - (parseFloat($scope.billServicesList[key].concession) + parseFloat($scope.billServicesList[key].discount));
	    			        
	    			    	var txpre = 0;
							if(service.taxPercentage != null || service.taxPercentage > 0){
								$scope.billServicesList[key].taxPercentage = service.taxPercentage;
								txpre = service.taxPercentage;
							}else{
									$scope.billServicesList[key].taxPercentage = 0;
									}
							$scope.billServicesList[key].taxAmount = parseFloat((parseFloat(withConDic) * (txpre*0.01))).toFixed(2);
							$scope.billServicesList[key].netAmt =   (parseFloat(withConDic) + parseFloat($scope.billServicesList[key].taxAmount)).toFixed(2);
							$scope.billServicesList[key].selfPayable = (parseFloat($scope.billServicesList[key].netAmt) * (parseFloat(service.coPayPer*0.01))).toFixed(2);
							$scope.billServicesList[key].creditPayable = (parseFloat($scope.billServicesList[key].netAmt) - parseFloat($scope.billServicesList[key].selfPayable)).toFixed(2);
							
  						}
					    
						
						if(i == $scope.billServicesList.length){
							$scope.calculation($scope.billServicesList);
						}
						i++;
				});
		 }
		 
		 $scope.commonFinalFunctionBillDisAmt = function(){
			 

				var i = 1;
	    		angular.forEach($scope.billServicesList, function (service, key) 
					{
	    					if(parseFloat(service.concessionPercentage) >= 0)
	    					{
	    						
	    						
	    						var totalAmt = (parseFloat(service.quantity) * parseFloat(service.unitPrice)).toFixed(2);
	    						$scope.billServicesList[key].discountPercentage = parseFloat($scope.discountPercentageBillWise).toFixed(2);
	    						$scope.billServicesList[key].discountPercentageFull = parseFloat($scope.discountPercentageBillWise);
			    			    $scope.billServicesList[key].discount = ((parseFloat(totalAmt) - parseFloat($scope.billServicesList[key].concession))*(isNaN($scope.discountPercentageBillWiseActual) ? 0: ($scope.discountPercentageBillWiseActual*0.01))).toFixed(2);
		    			        var withConDic =parseFloat(totalAmt) - (parseFloat($scope.billServicesList[key].concession) + parseFloat($scope.billServicesList[key].discount));
		    			    	var txpre = 0;
								if(service.taxPercentage != null || service.taxPercentage > 0){
									$scope.billServicesList[key].taxPercentage = service.taxPercentage;
									txpre = service.taxPercentage;
								}else{
										$scope.billServicesList[key].taxPercentage = 0;
								}
								$scope.billServicesList[key].taxAmount = parseFloat((parseFloat(withConDic) * (txpre*0.01))).toFixed(2);
								$scope.billServicesList[key].netAmt =   (parseFloat(withConDic) + parseFloat($scope.billServicesList[key].taxAmount)).toFixed(2);
									$scope.billServicesList[key].selfPayable = (parseFloat($scope.billServicesList[key].netAmt) * (parseFloat(service.coPayPer*0.01))).toFixed(2);
									$scope.billServicesList[key].creditPayable = (parseFloat($scope.billServicesList[key].netAmt) - parseFloat($scope.billServicesList[key].selfPayable)).toFixed(2);
								
	    						}
								if(i == $scope.billServicesList.length){
									$scope.calculation($scope.billServicesList);
								}
								i++;
					});
		 }
		 
		 
		 $scope.commonFunctionBillDis = function(){
			 
			 if($scope.discountRadio == 1){
			 	var i = 1;
	    		angular.forEach($scope.billServicesListForCheck, function (service, key) 
					{
	    		 	    if(service.isDiscountApplicable == 'Y')
	    					{
	    						var totalAmt = (parseFloat(service.quantity) * parseFloat(service.unitPrice)).toFixed(2);
	    						$scope.billServicesListForCheck[key].discountPercentage = parseFloat($scope.discountPercentageBillWise).toFixed(2);
	    						$scope.billServicesListForCheck[key].discountPercentageFull = parseFloat($scope.discountPercentageBillWise);
			    			    $scope.billServicesListForCheck[key].discount = ((parseFloat(totalAmt) - parseFloat($scope.billServicesListForCheck[key].concession))*(isNaN($scope.discountPercentageBillWiseActual) ? 0: ($scope.discountPercentageBillWiseActual*0.01))).toFixed(2);

			    			    var withConDic =parseFloat(totalAmt) - (parseFloat($scope.billServicesListForCheck[key].concession) + parseFloat($scope.billServicesListForCheck[key].discount));
		    			    	var txpre = 0;
								if(service.taxPercentage != null || service.taxPercentage > 0){
									$scope.billServicesListForCheck[key].taxPercentage = service.taxPercentage;
									txpre = service.taxPercentage;
								}else{
										$scope.billServicesListForCheck[key].taxPercentage = 0;
								}
								$scope.billServicesListForCheck[key].taxAmount = parseFloat((parseFloat(withConDic) * (txpre*0.01))).toFixed(2);
								$scope.billServicesListForCheck[key].netAmt =   (parseFloat(withConDic) + parseFloat($scope.billServicesListForCheck[key].taxAmount)).toFixed(2);
								$scope.billServicesListForCheck[key].selfPayable = (parseFloat($scope.billServicesListForCheck[key].netAmt) * (parseFloat(service.coPayPer*0.01))).toFixed(2);
								$scope.billServicesListForCheck[key].creditPayable = (parseFloat($scope.billServicesListForCheck[key].netAmt) - parseFloat($scope.billServicesListForCheck[key].selfPayable)).toFixed(2);
								
	    					}
								if(i == $scope.billServicesListForCheck.length){
									
									var totalNetPayableAmtTmp = parseFloat(0);
									var totalSelfPayableTmp = parseFloat(0);
									var totalCreditPayableTmp = parseFloat(0);
									var totalAmtConcessionTmp = parseFloat(0);
									var totalAmtDiscountTmp = parseFloat(0);
									
									var k = 1;
									angular.forEach($scope.billServicesListForCheck,function(value,key)
											{
										angular.forEach(value.orderDetailsPackageMapperDtosList,function(value1,key)
												{
												  if(value1.isPackageService=='N')
													  {
													  
													  if(value.isDiscountApplicable == 'Y'){
														  $scope.totalDiscountAmt = parseFloat($scope.totalDiscountAmt)+ (parseFloat(value.unitPrice * value.quantity) - parseFloat(value.concession));
														  $scope.totalConcessionAmt = parseFloat($scope.totalConcessionAmt) +  parseFloat(value.unitPrice * value.quantity);
													  }
														totalNetPayableAmtTmp = totalNetPayableAmtTmp + parseFloat(value.netAmt);
														totalSelfPayableTmp = totalSelfPayableTmp + parseFloat(value.selfPayable);
														totalCreditPayableTmp = totalCreditPayableTmp + parseFloat(value.creditPayable);
														totalAmtConcessionTmp = totalAmtConcessionTmp + parseFloat(value.concession);
														totalAmtDiscountTmp = totalAmtDiscountTmp + parseFloat(value.discount);
													  }
												});
										if(k == $scope.billServicesListForCheck.length ){
											var totalDisCon = parseFloat(totalAmtConcessionTmp) + parseFloat(totalAmtDiscountTmp);
											var netPayAmtTmp = parseFloat(totalNetPayableAmtTmp) - parseFloat($scope.billObjTmp.amountPaid);
												if(parseFloat($scope.billObjTmp.amountPaid) > parseFloat(totalSelfPayableTmp) && parseFloat($scope.billObjTmp.amountPaid) > 0)
												{
													$scope.commonSetDefaultVal();
													growl.warning("Self amount cannot be less than self due amount i.e "+parseFloat($scope.selfDueAmount),{
														title : ERROR_MSG
													});
												return false;
												
											}
											else  if(totalDisCon > netPayAmtTmp && parseFloat($scope.billObjTmp.amountPaid) > 0){
												$scope.commonFinalFunctionBillDisPer();
												
											}
											else{
												$scope.commonFinalFunctionBillDisPer();
											}
										}
										k++;
									});
								}
								i++;
					});
		 }
		if($scope.discountRadio == 2){
			 
			var i = 1;
    		angular.forEach($scope.billServicesListForCheck, function (service, key) 
				{
    			 if(service.isDiscountApplicable == 'Y')
					{
    			
    			var totalAmt = (parseFloat(service.quantity) * parseFloat(service.unitPrice)).toFixed(2);
    			
    			$scope.billServicesListForCheck[key].discountPercentage = parseFloat($scope.discountPercentageBillWise).toFixed(2);
    			$scope.billServicesListForCheck[key].discountPercentageFull = parseFloat($scope.discountPercentageBillWise);
			    $scope.billServicesListForCheck[key].discount = ((parseFloat(totalAmt) - parseFloat($scope.billServicesListForCheck[key].concession))*(isNaN($scope.discountPercentageBillWiseActual) ? 0: ($scope.discountPercentageBillWiseActual*0.01))).toFixed(2);
		    			    var withConDic =parseFloat(totalAmt) - (parseFloat($scope.billServicesListForCheck[key].concession) + parseFloat($scope.billServicesListForCheck[key].discount));
	    			    	var txpre = 0;
							if(service.taxPercentage != null || service.taxPercentage > 0){
								$scope.billServicesListForCheck[key].taxPercentage = service.taxPercentage;
								txpre = service.taxPercentage;
							}else{
									$scope.billServicesListForCheck[key].taxPercentage = 0;
							}
							$scope.billServicesListForCheck[key].taxAmount = parseFloat((parseFloat(withConDic) * (txpre*0.01))).toFixed(2);
							$scope.billServicesListForCheck[key].netAmt =   (parseFloat(withConDic) + parseFloat($scope.billServicesListForCheck[key].taxAmount)).toFixed(2);
								$scope.billServicesListForCheck[key].selfPayable = (parseFloat($scope.billServicesListForCheck[key].netAmt) * (parseFloat(service.coPayPer*0.01))).toFixed(2);
								$scope.billServicesListForCheck[key].creditPayable = (parseFloat($scope.billServicesListForCheck[key].netAmt) - parseFloat($scope.billServicesListForCheck[key].selfPayable)).toFixed(2);
						}
							if(i == $scope.billServicesListForCheck.length){
								
								var totalNetPayableAmtTmp = parseFloat(0);
								var totalSelfPayableTmp = parseFloat(0);
								var totalCreditPayableTmp = parseFloat(0);
								var totalAmtConcessionTmp = parseFloat(0);
								var totalAmtDiscountTmp = parseFloat(0);
								
								var k = 1;
								angular.forEach($scope.billServicesListForCheck,function(value,key)
										{
									angular.forEach(value.orderDetailsPackageMapperDtosList,function(value1,key)
											{
											  if(value1.isPackageService=='N')
												  {
												  
												  if(value.isDiscountApplicable == 'Y'){
													  $scope.totalDiscountAmt = parseFloat($scope.totalDiscountAmt)+ (parseFloat(value.unitPrice * value.quantity) - parseFloat(value.concession));
													  $scope.totalConcessionAmt = parseFloat($scope.totalConcessionAmt) +  parseFloat(value.unitPrice * value.quantity);
												  }
													totalNetPayableAmtTmp = totalNetPayableAmtTmp + parseFloat(value.netAmt);
													totalSelfPayableTmp = totalSelfPayableTmp + parseFloat(value.selfPayable);
													totalCreditPayableTmp = totalCreditPayableTmp + parseFloat(value.creditPayable);
													totalAmtConcessionTmp = totalAmtConcessionTmp + parseFloat(value.concession);
													totalAmtDiscountTmp = totalAmtDiscountTmp + parseFloat(value.discount);
												  }
											});
									if(k == $scope.billServicesListForCheck.length ){
										var totalDisCon = parseFloat(totalAmtConcessionTmp) + parseFloat(totalAmtDiscountTmp);
										var netPayAmtTmp = parseFloat(totalNetPayableAmtTmp) - parseFloat($scope.billObjTmp.amountPaid);
										if(parseFloat($scope.billObjTmp.amountPaid) > parseFloat(totalSelfPayableTmp) && parseFloat($scope.billObjTmp.amountPaid) > 0){
											
											$scope.commonSetDefaultVal();
											growl.warning("Self amount cannot be less than self due amount i.e "+parseFloat($scope.selfDueAmount),{
												title : ERROR_MSG
											});
											return false;
											
										}
										else if(totalDisCon > netPayAmtTmp && parseFloat($scope.billObjTmp.amountPaid) > 0){
											$scope.commonFinalFunctionBillDisPer();
											
										}
										else{
											$scope.commonFinalFunctionBillDisPer();
										}
									}
									k++;
								});
							}
							i++;
				});
    		
		 }
	    		
		 }
		 
		 
 $scope.commonFunctionBillCon = function(){
			 
			 if($scope.concessionRadio == 1){
			 	var i = 1;
	    		angular.forEach($scope.billServicesListForCheck, function (service, key) 
					{
	    			 if(service.isDiscountApplicable == 'Y')
 					{
	    			
	    			var totalAmt = (parseFloat(service.quantity) * parseFloat(service.unitPrice)).toFixed(2);
					
					 $scope.billServicesListForCheck[key].concession = ((parseFloat(totalAmt) * parseFloat($scope.concessionPercentageBillWiseActual*0.01))).toFixed(2);
   			        $scope.billServicesListForCheck[key].concessionPercentage = parseFloat($scope.concessionPercentageBillWise).toFixed(2);
   			        $scope.billServicesListForCheck[key].discount = ((parseFloat(totalAmt) - parseFloat($scope.billServicesListForCheck[key].concession))*(isNaN($scope.billServicesListForCheck[key].discountPercentage) ? 0: ($scope.billServicesListForCheck[key].discountPercentage*0.01))).toFixed(2);

			    			    var withConDic =parseFloat(totalAmt) - (parseFloat($scope.billServicesListForCheck[key].concession) + parseFloat($scope.billServicesListForCheck[key].discount));
		    			    	var txpre = 0;
								if(service.taxPercentage != null || service.taxPercentage > 0){
									$scope.billServicesListForCheck[key].taxPercentage = service.taxPercentage;
									txpre = service.taxPercentage;
								}else{
										$scope.billServicesListForCheck[key].taxPercentage = 0;
								}
								$scope.billServicesListForCheck[key].taxAmount = parseFloat((parseFloat(withConDic) * (txpre*0.01))).toFixed(2);
								$scope.billServicesListForCheck[key].netAmt =   (parseFloat(withConDic) + parseFloat($scope.billServicesListForCheck[key].taxAmount)).toFixed(2);
									$scope.billServicesListForCheck[key].selfPayable = (parseFloat($scope.billServicesListForCheck[key].netAmt) * (parseFloat(service.coPayPer*0.01))).toFixed(2);
									$scope.billServicesListForCheck[key].creditPayable = (parseFloat($scope.billServicesListForCheck[key].netAmt) - parseFloat($scope.billServicesListForCheck[key].selfPayable)).toFixed(2);
 					}
								if(i == $scope.billServicesListForCheck.length){
									
									var totalNetPayableAmtTmp = parseFloat(0);
									var totalSelfPayableTmp = parseFloat(0);
									var totalCreditPayableTmp = parseFloat(0);
									var totalAmtConcessionTmp = parseFloat(0);
									var totalAmtDiscountTmp = parseFloat(0);
									
									var k = 1;
									angular.forEach($scope.billServicesListForCheck,function(value,key)
											{
										angular.forEach(value.orderDetailsPackageMapperDtosList,function(value1,key)
												{
												  if(value1.isPackageService=='N')
													  {
													  
													  if(value.isDiscountApplicable == 'Y'){
														  $scope.totalDiscountAmt = parseFloat($scope.totalDiscountAmt)+ (parseFloat(value.unitPrice * value.quantity) - parseFloat(value.concession));
														  $scope.totalConcessionAmt = parseFloat($scope.totalConcessionAmt) +  parseFloat(value.unitPrice * value.quantity);
													  }
														totalNetPayableAmtTmp = totalNetPayableAmtTmp + parseFloat(value.netAmt);
														totalSelfPayableTmp = totalSelfPayableTmp + parseFloat(value.selfPayable);
														totalCreditPayableTmp = totalCreditPayableTmp + parseFloat(value.creditPayable);
														totalAmtConcessionTmp = totalAmtConcessionTmp + parseFloat(value.concession);
														totalAmtDiscountTmp = totalAmtDiscountTmp + parseFloat(value.discount);
													  }
												});
										if(k == $scope.billServicesListForCheck.length ){
											var totalDisCon = parseFloat(totalAmtConcessionTmp) + parseFloat(totalAmtDiscountTmp);
											var netPayAmtTmp = parseFloat(totalNetPayableAmtTmp) - parseFloat($scope.billObjTmp.amountPaid);
												if(parseFloat($scope.billObjTmp.amountPaid) > parseFloat(totalSelfPayableTmp) && parseFloat($scope.billObjTmp.amountPaid) > 0 ){
												
												$scope.commonSetDefaultVal();
												growl.warning("Self amount cannot be less than self due amount i.e "+parseFloat($scope.selfDueAmount),{
													title : ERROR_MSG
												});
												return false;
												
											}
											else  if(totalDisCon > netPayAmtTmp && parseFloat($scope.billObjTmp.amountPaid) > 0){
												$scope.commonFinalFunctionBillConPer();
												
											}
											else{
												$scope.commonFinalFunctionBillConPer();
											}
										}
										k++;
									});
								}
								i++;
					});
		 }
		if($scope.concessionRadio == 2){
			 
			var i = 1;
    		angular.forEach($scope.billServicesListForCheck, function (service, key) 
				{
    			
    			 if(service.isDiscountApplicable == 'Y')
					{
    			var totalAmt = (parseFloat(service.quantity) * parseFloat(service.unitPrice)).toFixed(2);
				
				$scope.billServicesListForCheck[key].concession = (parseFloat(service.unitPrice) * parseFloat($scope.concessionPercentageBillWiseActual*0.01)).toFixed(2);
		        $scope.billServicesListForCheck[key].concessionPercentage = parseFloat($scope.concessionPercentageBillWise).toFixed(2);
		        $scope.billServicesListForCheck[key].discount = ((parseFloat(totalAmt) - parseFloat($scope.billServicesListForCheck[key].concession))*(isNaN($scope.billServicesListForCheck[key].discountPercentage) ? 0: ($scope.billServicesListForCheck[key].discountPercentage*0.01))).toFixed(2);
		    			    var withConDic =parseFloat(totalAmt) - (parseFloat($scope.billServicesListForCheck[key].concession) + parseFloat($scope.billServicesListForCheck[key].discount));
	    			    	var txpre = 0;
							if(service.taxPercentage != null || service.taxPercentage > 0){
								$scope.billServicesListForCheck[key].taxPercentage = service.taxPercentage;
								txpre = service.taxPercentage;
							}else{
									$scope.billServicesListForCheck[key].taxPercentage = 0;
							}
							$scope.billServicesListForCheck[key].taxAmount = parseFloat((parseFloat(withConDic) * (txpre*0.01))).toFixed(2);
							$scope.billServicesListForCheck[key].netAmt =   (parseFloat(withConDic) + parseFloat($scope.billServicesListForCheck[key].taxAmount)).toFixed(2);
							$scope.billServicesListForCheck[key].selfPayable = (parseFloat($scope.billServicesListForCheck[key].netAmt) * (parseFloat(service.coPayPer*0.01))).toFixed(2);
							
				}
							if(i == $scope.billServicesListForCheck.length){
								
								var totalNetPayableAmtTmp = parseFloat(0);
								var totalSelfPayableTmp = parseFloat(0);
								var totalCreditPayableTmp = parseFloat(0);
								var totalAmtConcessionTmp = parseFloat(0);
								var totalAmtDiscountTmp = parseFloat(0);
								
								var k = 1;
								angular.forEach($scope.billServicesListForCheck,function(value,key)
										{
									angular.forEach(value.orderDetailsPackageMapperDtosList,function(value1,key)
											{
											  if(value1.isPackageService=='N')
												  {
												  
												  if(value.isDiscountApplicable == 'Y'){
													  $scope.totalDiscountAmt = parseFloat($scope.totalDiscountAmt)+ (parseFloat(value.unitPrice * value.quantity) - parseFloat(value.concession));
													  $scope.totalConcessionAmt = parseFloat($scope.totalConcessionAmt) +  parseFloat(value.unitPrice * value.quantity);
												  }
													totalNetPayableAmtTmp = totalNetPayableAmtTmp + parseFloat(value.netAmt);
													totalSelfPayableTmp = totalSelfPayableTmp + parseFloat(value.selfPayable);
													totalCreditPayableTmp = totalCreditPayableTmp + parseFloat(value.creditPayable);
													totalAmtConcessionTmp = totalAmtConcessionTmp + parseFloat(value.concession);
													totalAmtDiscountTmp = totalAmtDiscountTmp + parseFloat(value.discount);
												  }
											});
									if(k == $scope.billServicesListForCheck.length ){
										var totalDisCon = parseFloat(totalAmtConcessionTmp) + parseFloat(totalAmtDiscountTmp);
										var netPayAmtTmp = parseFloat(totalNetPayableAmtTmp) - parseFloat($scope.billObjTmp.amountPaid);
										if(parseFloat($scope.billObjTmp.amountPaid) > parseFloat(totalSelfPayableTmp) && parseFloat($scope.billObjTmp.amountPaid) > 0){
											
											$scope.commonSetDefaultVal();
											growl.warning("Self amount cannot be less than self due amount i.e "+parseFloat($scope.selfDueAmount),{
												title : ERROR_MSG
											});
											return false;
											
										}
										else if(totalDisCon > netPayAmtTmp && parseFloat($scope.billObjTmp.amountPaid) > 0){
											$scope.commonFinalFunctionBillConPer();
										}
										else{
											$scope.commonFinalFunctionBillConPer();
										}
									}
									k++;
								});
							}
							i++;
				});
    		
		 }
	    		
		 }
		 
		 $scope.commonFunction = function(){
			 	var i = 1;
	    		angular.forEach($scope.billServicesListForCheck, function (service, key) 
					{
	    						 var totalAmt = (parseFloat(service.quantity) * parseFloat(service.unitPrice)).toFixed(2);
	   						    $scope.billServicesListForCheck[key].concessionPercentage = parseFloat(service.concessionPercentage);
	   						    $scope.billServicesListForCheck[key].concession = (parseFloat(totalAmt) * parseFloat($scope.billServicesListForCheck[key].concessionPercentage*0.01)).toFixed(2);
		    			        $scope.billServicesListForCheck[key].discount =((parseFloat(totalAmt) - parseFloat($scope.billServicesListForCheck[key].concession))*(isNaN(service.discountPercentage) ? 0: service.discountPercentage*0.01)).toFixed(2);
		    			        $scope.billServicesListForCheck[key].discountPercentage = parseFloat(service.discountPercentage).toFixed(2);
		    			        var withConDic =parseFloat(totalAmt) - (parseFloat($scope.billServicesListForCheck[key].concession) + parseFloat($scope.billServicesListForCheck[key].discount));
		    			    	var txpre = 0;
								if(service.taxPercentage != null || service.taxPercentage > 0){
									$scope.billServicesListForCheck[key].taxPercentage = service.taxPercentage;
									txpre = service.taxPercentage;
								}else{
										$scope.billServicesListForCheck[key].taxPercentage = 0;
								}
								$scope.billServicesListForCheck[key].taxAmount = parseFloat((parseFloat(withConDic) * (txpre*0.01))).toFixed(2);
								$scope.billServicesListForCheck[key].netAmt =   (parseFloat(withConDic) + parseFloat($scope.billServicesListForCheck[key].taxAmount)).toFixed(2);
								$scope.billServicesListForCheck[key].selfPayable = (parseFloat($scope.billServicesListForCheck[key].netAmt) * (parseFloat(service.coPayPer*0.01))).toFixed(2);
								$scope.billServicesListForCheck[key].creditPayable = (parseFloat($scope.billServicesListForCheck[key].netAmt) - parseFloat($scope.billServicesListForCheck[key].selfPayable)).toFixed(2);
								if(i == $scope.billServicesListForCheck.length){
									
									var totalNetPayableAmtTmp = parseFloat(0);
									var totalSelfPayableTmp = parseFloat(0);
									var totalCreditPayableTmp = parseFloat(0);
									var totalAmtConcessionTmp = parseFloat(0);
									var totalAmtDiscountTmp = parseFloat(0);
									
									var k = 1;
									angular.forEach($scope.billServicesListForCheck,function(value,key)
											{
										angular.forEach(value.orderDetailsPackageMapperDtosList,function(value1,key)
												{
												  if(value1.isPackageService=='N')
													  {
													  
													  if(value.isDiscountApplicable == 'Y'){
														  $scope.totalDiscountAmt = parseFloat($scope.totalDiscountAmt)+ (parseFloat(value.unitPrice * value.quantity) - parseFloat(value.concession));
														  $scope.totalConcessionAmt = parseFloat($scope.totalConcessionAmt) +  parseFloat(value.unitPrice * value.quantity);
													  }
														totalNetPayableAmtTmp = totalNetPayableAmtTmp + parseFloat(value.netAmt);
														totalSelfPayableTmp = totalSelfPayableTmp + parseFloat(value.selfPayable);
														totalCreditPayableTmp = totalCreditPayableTmp + parseFloat(value.creditPayable);
														totalAmtConcessionTmp = totalAmtConcessionTmp + parseFloat(value.concession);
														totalAmtDiscountTmp = totalAmtDiscountTmp + parseFloat(value.discount);
													  }
												});
										if(k == $scope.billServicesListForCheck.length )
										{
											var totalDisCon = parseFloat(totalAmtConcessionTmp) + parseFloat(totalAmtDiscountTmp);
											var netPayAmtTmp = parseFloat(totalNetPayableAmtTmp) - parseFloat($scope.billObjTmp.amountPaid);
											
											if(parseFloat($scope.billObjTmp.amountPaid) > parseFloat(totalSelfPayableTmp) && parseFloat($scope.billObjTmp.amountPaid) > 0){
												
												$scope.commonSetDefaultVal();
												growl.warning("Self amount cannot be less than self due amount i.e "+parseFloat($scope.selfDueAmount),{
													title : ERROR_MSG
												});
												return false;
												
											}else if(totalDisCon >= netPayAmtTmp && parseFloat($scope.billObjTmp.amountPaid) > 0)
											{
												$scope.commonFinalFunction();
												
											}else{
												$scope.commonFinalFunction();
											}
										}
										k++;
									});
								}
								i++;
					});
	    		
		 }
		 
		 $scope.getConcessionPerCal = function(itemObj,idx){
			 
			 if(itemObj.concessionPercentage == '' || itemObj.concessionPercentage == undefined){
		    		itemObj.concessionPercentage = 0;
		    		itemObj.concession = 0;
		    		$scope.billServicesList[idx].concession = parseFloat(0);
		    		$scope.billServicesList[idx].concessionPercentage = parseFloat(0);
		    	}
			 
			 if(parseFloat(itemObj.concessionPercentage) > 100)
				{
					
		    		$scope.billServicesList[idx].concession = parseFloat(0);
					$scope.billServicesList[idx].concessionPercentage = parseFloat(0);
		    		
		    		$scope.billServicesList[idx].discount = parseFloat(itemObj.discount);
		    		$scope.billServicesList[idx].discountPercentage = parseFloat(itemObj.discountPercentage);
		    		
		    		var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.unitPrice)).toFixed(2);
			        $scope.billServicesList[idx].discountPercentage = isNaN(((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.unitPrice)) - parseFloat($scope.billServicesList[idx].concession))*100).toFixed(2)) ? parseFloat(0).toFixed(2) : ((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.unitPrice)) - parseFloat($scope.billServicesList[idx].concession))*100).toFixed(2);
			        $scope.billServicesList[idx].discount = parseFloat(itemObj.discount).toFixed(2);
			        var withConDic =parseFloat(totalAmt) - (parseFloat($scope.billServicesList[idx].concession) + parseFloat($scope.billServicesList[idx].discount));
			        
			    	var txpre = 0;
					if(itemObj.taxPercentage != null || itemObj.taxPercentage > 0){
						$scope.billServicesList[idx].taxPercentage = itemObj.taxPercentage;
						txpre = itemObj.taxPercentage;
					}else{
							$scope.billServicesList[idx].taxPercentage = 0;
							}
					
					
					$scope.billServicesList[idx].taxAmount = parseFloat((parseFloat(withConDic) * (txpre*0.01))).toFixed(2);
					$scope.billServicesList[idx].netAmt =   (parseFloat(withConDic) + parseFloat($scope.billServicesList[idx].taxAmount)).toFixed(2);
					
					$scope.billServicesList[idx].selfPayable = (parseFloat($scope.billServicesList[idx].netAmt) * (parseFloat(itemObj.coPayPer*0.01))).toFixed(2);
					$scope.billServicesList[idx].creditPayable = (parseFloat($scope.billServicesList[idx].netAmt) - parseFloat($scope.billServicesList[idx].selfPayable)).toFixed(2);
					$scope.calculation($scope.billServicesList);
					growl.warning("You can't give concession more than 100 %.",{
						title : ERROR_MSG
					});
					return false;
				}
			 else
		    	{
				 $scope.billServicesListForCheck = [];
				 $scope.billServicesListForCheck = angular.copy($scope.billServicesList);
				 $scope.commonFunction();
				}
			 
		 }
		 
		  $scope.getDiscountPerCal = function(itemObj,idx){
			  
		    	if(itemObj.discountPercentage == '' || itemObj.discountPercentage == undefined){
		    		itemObj.discountPercentage = 0;
		    		itemObj.discount = 0;
		    		$scope.billServicesList[idx].discount = parseFloat(0);
		    		$scope.billServicesList[idx].discountPercentage = parseFloat(0);
		    	}
		    	if(parseFloat(itemObj.discountPercentage) > 100)
				{
					
		    		$scope.billServicesList[idx].concession = parseFloat(itemObj.concession);
					$scope.billServicesList[idx].concessionPercentage = parseFloat(itemObj.concessionPercentage);
		    		
		    		$scope.billServicesList[idx].discount = parseFloat(0);
		    		$scope.billServicesList[idx].discountPercentage = parseFloat(0);
		    		$scope.billServicesList[idx].discountPercentageFull = parseFloat(0);
		    		
		    		var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.unitPrice)).toFixed(2);
			        $scope.billServicesList[idx].discountPercentage = isNaN(((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.unitPrice)))*100).toFixed(2)) ? parseFloat(0).toFixed(2) : ((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.unitPrice)))*100).toFixed(2);
			        $scope.billServicesList[idx].discount = parseFloat(itemObj.discount).toFixed(2);
			        var withConDic =parseFloat(totalAmt) - (parseFloat($scope.billServicesList[idx].concession) + parseFloat($scope.billServicesList[idx].discount));
			        
			    	var txpre = 0;
					if(itemObj.taxPercentage != null || itemObj.taxPercentage > 0){
						$scope.billServicesList[idx].taxPercentage = itemObj.taxPercentage;
						txpre = itemObj.taxPercentage;
					}else{
							$scope.billServicesList[idx].taxPercentage = 0;
							}
					
					
					$scope.billServicesList[idx].taxAmount = parseFloat((parseFloat(withConDic) * (txpre*0.01))).toFixed(2);
					$scope.billServicesList[idx].netAmt =   (parseFloat(withConDic) + parseFloat($scope.billServicesList[idx].taxAmount)).toFixed(2);
					
					$scope.billServicesList[idx].selfPayable = (parseFloat($scope.billServicesList[idx].netAmt) * (parseFloat(itemObj.coPayPer*0.01))).toFixed(2);
					$scope.billServicesList[idx].creditPayable = (parseFloat($scope.billServicesList[idx].netAmt) - parseFloat($scope.billServicesList[idx].selfPayable)).toFixed(2);
						
					$scope.calculation($scope.billServicesList);
					growl.warning("You can't give discount more than 100 %.",{
						title : ERROR_MSG
					});
					return false;
				}
		    	else
		    	{
		    		 $scope.billServicesListForCheck = [];
					 $scope.billServicesListForCheck = angular.copy($scope.billServicesList);
					 $scope.commonFunction();
		    		
				}
		    }
		    
		    
		    $scope.getDiscountCal = function(itemObj,idx)
		    {
		    	if(itemObj.discount == '' || itemObj.discount == undefined){
		    		itemObj.discount = 0;
		    		itemObj.discountPercentage = 0;
		    		$scope.billServicesList[idx].discount = 0;
		    		$scope.billServicesList[idx].discountPercentage = 0;
		    	}
		    	if(parseFloat(itemObj.discount) > parseFloat(itemObj.unitPrice))
				{
					
					$scope.billServicesList[idx].discount = parseFloat(0);
		    		$scope.billServicesList[idx].discountPercentage = parseFloat(0);
		    		$scope.billServicesList[idx].discountPercentageFull = parseFloat(0);
		    		

					$scope.billServicesList[idx].concession = parseFloat(itemObj.concession);
					$scope.billServicesList[idx].concessionPercentage = parseFloat(itemObj.concessionPercentage);
					    
		    		
		    		var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.unitPrice)).toFixed(2);
			        $scope.billServicesList[idx].discountPercentage = isNaN(((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.unitPrice)))*100).toFixed(2)) ? parseFloat(0).toFixed(2) : ((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.unitPrice)))*100).toFixed(2);
			        $scope.billServicesList[idx].discount = parseFloat(itemObj.discount).toFixed(2);
			        var withConDic =parseFloat(totalAmt) - (parseFloat($scope.billServicesList[idx].concession) + parseFloat($scope.billServicesList[idx].discount));
			        
			    	var txpre = 0;
					if(itemObj.taxPercentage != null || itemObj.taxPercentage > 0){
						$scope.billServicesList[idx].taxPercentage = itemObj.taxPercentage;
						txpre = itemObj.taxPercentage;
					}else{
							$scope.billServicesList[idx].taxPercentage = 0;
							}
					
					
					$scope.billServicesList[idx].taxAmount = parseFloat((parseFloat(withConDic) * (txpre*0.01))).toFixed(2);
					$scope.billServicesList[idx].netAmt =   (parseFloat(withConDic) + parseFloat($scope.billServicesList[idx].taxAmount)).toFixed(2);
					$scope.billServicesList[idx].selfPayable = (parseFloat($scope.billServicesList[idx].netAmt) * (parseFloat(itemObj.coPayPer*0.01))).toFixed(2);
					$scope.billServicesList[idx].creditPayable = (parseFloat($scope.billServicesList[idx].netAmt) - parseFloat($scope.billServicesList[idx].selfPayable)).toFixed(2);
					$scope.calculation($scope.billServicesList);
					growl.warning("Discount cannot be more than service amount !",{
						title : ERROR_MSG
					});
					return false;
				}
		    	else
		    	{
		    		if((parseFloat(itemObj.quantity) * parseFloat(itemObj.unitPrice)) == parseFloat(itemObj.concession)){
		    			 $scope.billServicesList[idx].discountPercentage = parseFloat(0);
		    		}else{
		    			 $scope.billServicesList[idx].discountPercentage = isNaN((parseFloat(itemObj.discount)/((parseFloat(itemObj.quantity) * parseFloat(itemObj.unitPrice))-parseFloat(itemObj.concession)))*100) ? parseFloat(0) : ((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.unitPrice)-parseFloat(itemObj.concession))))*100;	
		    		}
		    		
		    		 $scope.billServicesListForCheck = [];
					 $scope.billServicesListForCheck = angular.copy($scope.billServicesList);
					 $scope.commonFunction();
					}
		         }
		    
		    
		    $scope.funDiscountPercentageBillWise = function(){
		    	
		    	if($scope.discountPercentageBillWise == '' || $scope.discountPercentageBillWise == null || $scope.discountPercentageBillWise == undefined){
		    		$scope.discountPercentageBillWise = 0;
		    	}
		    	
		    	if(parseFloat($scope.discountPercentageBillWise) > 100){
		    		$scope.discountPercentageBillWise = parseFloat(0);
					$scope.discountAmountBillWise = parseFloat(0);
					$scope.discountPercentageBillWiseActual =parseFloat(0);
		    		growl.warning("Percentage should be equal or less then 100%.",{
						title : ERROR_MSG
					});
					return false;
		    	}
		    	else{
		    		if(parseFloat($scope.totalDiscountAmt) > 0)
		    		{
		    		 $scope.discountAmountBillWise = (parseFloat($scope.totalDiscountAmt) * parseFloat($scope.discountPercentageBillWise*0.01)).toFixed(2);
		    		 $scope.discountPercentageBillWiseActual =$scope.discountPercentageBillWise;
		    		}else{
		    			$scope.discountPercentageBillWise = parseFloat(0);
						$scope.discountAmountBillWise = parseFloat(0);
						$scope.discountPercentageBillWiseActual =parseFloat(0);
		    		}
		    	}
		    }
		    
		    $scope.getApplyDiscountPopup = function(){
		    	
		    	if($scope.discountRadio == 1 && ($scope.discountPercentageBillWise == '' || $scope.discountPercentageBillWise == null)){
		    		growl.warning("Discount percentage is required.",{
						title : ERROR_MSG
					});
					return false;
		    	}
		    	else if($scope.discountRadio == 2 && ($scope.discountAmountBillWise == '' || $scope.discountAmountBillWise == null)){
		    		growl.warning("Discount amount is required.",{
						title : ERROR_MSG
					});
					return false;
		    	}else{
		    		if($scope.discountAmountBillWise >= 0 && $scope.discountPercentageBillWise >= 0)
		    		{
		    			angular.element('#applyDiscountPopup').modal('show');
		    		}
		    	}
		    	
		    }
		    
		    $scope.applyDiscount = function(){
		    	
		    	if($scope.discountRadio == 1){
		    		
		    		 $scope.billServicesListForCheck = [];
					 $scope.billServicesListForCheck = angular.copy($scope.billServicesList);
					 $scope.commonFunctionBillDis();
			    }
		    	if($scope.discountRadio == 2){
		    		
		    		 $scope.billServicesListForCheck = [];
					 $scope.billServicesListForCheck = angular.copy($scope.billServicesList);
					 $scope.commonFunctionBillDis();
		    	}
		    }
		    
		    $scope.funDiscountAmountBillWise = function(){
		    	

		    	if($scope.discountAmountBillWise == '' || $scope.discountAmountBillWise == null || $scope.discountAmountBillWise == undefined){
		    		$scope.discountAmountBillWise = 0;
		    	}
		    	
		    	if(parseFloat($scope.discountAmountBillWise) > parseFloat($scope.totalDiscountAmt)){
		    		
		    		$scope.discountPercentageBillWise = parseFloat(0);
					$scope.discountAmountBillWise = parseFloat(0);
					$scope.discountPercentageBillWiseActual =parseFloat(0);
					var amount = ($scope.totalDiscountAmt - $scope.billObjTmp.amountPaid) > 0 ? ($scope.totalDiscountAmt - $scope.billObjTmp.amountPaid) : 0;
		    		growl.warning("Amount applicable for discount is : "+(parseFloat(amount).toFixed(2)),{
						title : ERROR_MSG
					});
					return false;
		    	}
		    	else{
		    		
		    		if(parseFloat($scope.totalDiscountAmt) > 0)
		    		{
		    			$scope.discountPercentageBillWiseActual =(parseFloat($scope.discountAmountBillWise)/parseFloat($scope.totalDiscountAmt) * 100);
		    			$scope.discountPercentageBillWise =(parseFloat($scope.discountAmountBillWise)/parseFloat($scope.totalDiscountAmt) * 100).toFixed(2);
		    		}else{
		    			$scope.discountPercentageBillWise = parseFloat(0);
						$scope.discountAmountBillWise = parseFloat(0);
						$scope.discountPercentageBillWiseActual =parseFloat(0);
		    		}
		    		
		    		
		    	}
		    }
		    
		    $scope.funConcessionPercentageBillWise = function(){
		    	
		    	
		    	if($scope.concessionPercentageBillWise == '' || $scope.concessionPercentageBillWise == null || $scope.concessionPercentageBillWise == undefined){
		    		$scope.concessionPercentageBillWise = 0;
		    	}
		    	
		    	if(parseFloat($scope.concessionPercentageBillWise) > 100){
		    		$scope.concessionPercentageBillWise=parseFloat(0);
			    	$scope.concessionPercentageBillWiseActual=parseFloat(0);
			    	$scope.concessionAmountBillWise=parseFloat(0);
		    		growl.warning("Percentage should be equal or less then 100%.",{
						title : ERROR_MSG
					});
					return false;
		    	}
		    	else{
		    		
		    		if(parseFloat($scope.totalConcessionAmt) > 0){
		    			$scope.concessionAmountBillWise = (parseFloat($scope.totalConcessionAmt) * parseFloat($scope.concessionPercentageBillWise*0.01)).toFixed(2);
		    			$scope.concessionPercentageBillWiseActual = $scope.concessionPercentageBillWise;
		    		}else{
		    			$scope.concessionPercentageBillWise=parseFloat(0);
				    	$scope.concessionPercentageBillWiseActual=parseFloat(0);
				    	$scope.concessionAmountBillWise=parseFloat(0);
			    		}
		    		
		    		
		    		
		    	}
		    }
		    
		    
		    $scope.funConcessionAmountBillWise = function(){
		    	
		    	
		    	if($scope.concessionAmountBillWise == '' || $scope.concessionAmountBillWise == null || $scope.concessionAmountBillWise == undefined){
		    		$scope.concessionAmountBillWise = 0;
		    	}
		    	
		    	
		    	if(parseFloat($scope.concessionAmountBillWise) > parseFloat($scope.totalConcessionAmt)){
		    		$scope.concessionPercentageBillWise=parseFloat(0);
			    	$scope.concessionPercentageBillWiseActual=parseFloat(0);
			    	$scope.concessionAmountBillWise=parseFloat(0);
		    		growl.warning("Amount applicable for concession is : "+parseFloat($scope.totalConcessionAmt),{
						title : ERROR_MSG
					});
					return false;
		    	}
		    	else{
		    		
		    		if(parseFloat($scope.totalConcessionAmt) > 0){
		    			$scope.concessionPercentageBillWise =(parseFloat($scope.concessionAmountBillWise)/parseFloat($scope.totalConcessionAmt) * 100).toFixed(2);
		    			$scope.concessionPercentageBillWiseActual =(parseFloat($scope.concessionAmountBillWise)/parseFloat($scope.totalConcessionAmt) * 100);
			    		}else{
			    			$scope.concessionPercentageBillWise=parseFloat(0);
					    	$scope.concessionPercentageBillWiseActual=parseFloat(0);
					    	$scope.concessionAmountBillWise=parseFloat(0);
			    		}
		    		
		    		
		    	}
		    }
		    
		    
		    
		    $scope.getApplyConcessionPopup = function(){
		    	
		    	if($scope.concessionRadio == 1 && ($scope.concessionPercentageBillWise == '' || $scope.concessionPercentageBillWise == null)){
		    		growl.warning("Concession percentage is required.",{
						title : ERROR_MSG
					});
					return false;
		    	}
		    	else if($scope.concessionRadio == 2 && ($scope.concessionAmountBillWise == '' || $scope.concessionAmountBillWise == null)){
		    		growl.warning("Concession amount is required.",{
						title : ERROR_MSG
					});
					return false;
		    	}else{
		    		if($scope.concessionPercentageBillWise >= 0 && $scope.concessionAmountBillWise >= 0)
		    		{
		    			angular.element('#applyConcessionPopup').modal('show');
		    		}
		    	}
		    	
		    }
		    
		    
		    $scope.applyConcession = function(){
		    	
		    	if($scope.concessionRadio == 1){
		    		$scope.billServicesListForCheck = [];
					 $scope.billServicesListForCheck = angular.copy($scope.billServicesList);
					 $scope.commonFunctionBillCon();
			    }
		    	if($scope.concessionRadio == 2){
		    		
		    		$scope.billServicesListForCheck = [];
					 $scope.billServicesListForCheck = angular.copy($scope.billServicesList);
					 $scope.commonFunctionBillCon();
		    	}
		    }
		 
		    $scope.cancelDiscountForServices = function()
		    {
		    	var i = 1;
		    	var flag = false;
		    	angular.forEach($scope.billServicesList, function (value, key) {
		    		if(parseFloat(value.discount) > 0){
		    			flag = true;
		    		}
		    		if(i == $scope.billServicesList.length && flag == true){
		    			angular.element('#cancelDiscountPopup').modal('show');
		    		}
		    		i++;
		    	});
		    	
		    }
		    
		    $scope.cancelConcessionForServices = function(){
		    	var i = 1;
		    	var flag = false;
		    	angular.forEach($scope.billServicesList, function (value, key) 
		    	{
		    		if(parseFloat(value.concession) != 0){
		    			flag = true;
		    		}
		    		if(i == $scope.billServicesList.length && flag == true){
		    			angular.element('#cancelConcessionPopup').modal('show');
		    		}
		    		i++;
		    	});
		    }
		    
		    $scope.finalCancelDiscount = function()
		    {
		    	$scope.discountPercentageBillWise = parseFloat(0);
				$scope.discountAmountBillWise = parseFloat(0);
				$scope.discountPercentageBillWiseActual =parseFloat(0);
		    	var j = 0;
					angular.forEach($scope.billServicesList, function (value, key) {
						
						var totalAmt = 0;
						var dis = 0;
						var gstAmt = 0;
						var withConDic = 0;
		    		
		    				$scope.billServicesList[j].discountPercentage = 0;
							$scope.billServicesList[j].discount = 0;
							totalAmt = (parseFloat(value.quantity) * parseFloat(value.unitPrice)).toFixed(2);
							
							withConDic =parseFloat(totalAmt - (parseFloat(dis) + parseFloat(value.concession))).toFixed(2);
							gstAmt = parseFloat((parseFloat(withConDic) * (value.taxPercentage*0.01))).toFixed(2);
							
							$scope.billServicesList[j].taxAmount = parseFloat((parseFloat(withConDic) * (value.taxPercentage*0.01))).toFixed(2);
							$scope.billServicesList[j].netAmt =   parseFloat((totalAmt -(parseFloat(dis) + parseFloat(value.concession)) + parseFloat(gstAmt))).toFixed(2);
							$scope.billServicesList[j].grossAmount = (parseFloat(value.quantity) * parseFloat(value.unitPrice)).toFixed(2);
							$scope.billServicesList[key].selfPayable = (parseFloat($scope.billServicesList[key].netAmt) * (parseFloat(value.coPayPer*0.01))).toFixed(2);
							$scope.billServicesList[key].creditPayable = (parseFloat($scope.billServicesList[key].netAmt) - parseFloat($scope.billServicesList[key].selfPayable)).toFixed(2);
							
		    			if(j+1 == $scope.billServicesList.length){
		    				
		    				 $scope.calculation($scope.billServicesList);
		    			}
		    			j++;
			    	});
		    }
		    
		    $scope.finalCancelConcession = function()
		    {
		    	$scope.concessionPercentageBillWise=parseFloat(0);
		    	$scope.concessionPercentageBillWiseActual=parseFloat(0);
		    	$scope.concessionAmountBillWise=parseFloat(0);
		    	var j = 0;
				angular.forEach($scope.billServicesList, function (value, key) {
					
					var totalAmt = 0;
					var concession = 0;
					var gstAmt = 0;
					var withConDic = 0;
					var discount = 0;
	    				$scope.billServicesList[j].concession = 0;
						$scope.billServicesList[j].concessionPercentage = 0;
						
						totalAmt = (parseFloat(value.quantity) * parseFloat(value.unitPrice)).toFixed(2);
						discount = (parseFloat(value.discountPercentage*0.01*totalAmt)).toFixed(2);
						withConDic =parseFloat(totalAmt - discount).toFixed(2);
						
						$scope.billServicesList[j].discount = discount;
						gstAmt = parseFloat((parseFloat(withConDic) * (value.taxPercentage*0.01))).toFixed(2);
						
						$scope.billServicesList[j].taxAmount = parseFloat((parseFloat(withConDic) * (value.taxPercentage*0.01))).toFixed(2);
						$scope.billServicesList[j].netAmt =   parseFloat((totalAmt -(parseFloat(value.discount) + parseFloat(concession)) + parseFloat(gstAmt))).toFixed(2);
						$scope.billServicesList[j].grossAmount = (parseFloat(value.quantity) * parseFloat(value.unitPrice)).toFixed(2);
			    		
						$scope.billServicesList[key].selfPayable = (parseFloat($scope.billServicesList[key].netAmt) * (parseFloat(value.coPayPer*0.01))).toFixed(2);
						$scope.billServicesList[key].creditPayable = (parseFloat($scope.billServicesList[key].netAmt) - parseFloat($scope.billServicesList[key].selfPayable)).toFixed(2);
						
	    			if(j+1 == $scope.billServicesList.length){
	    				
	    				 $scope.calculation($scope.billServicesList);
	    			}
	    			j++;
		    	});
		    }
		    
		 $scope.updateBillingPopup = function(){
			 
			 if($scope.billServicesList.length > 0)
				 {
				 	$scope.saveBtnDisable = false;
				 	angular.element('#saveConfirmPopup').modal('show');
				 }
		 }
		 
		 $scope.updateBill = function(){
			 angular.element('#saveConfirmPopup').modal('hide');
			 $scope.saveBtnDisable = true;
			 var data={
						updatedBy :$scope.userId,
						updatedDate :new Date().getTime(),
						organisationId :$scope.organizationId,
						unitId :$scope.unitId,
						billingMasterId : $scope.billMasterIds[0],
						orderDetailsMasterDtosList : [],
						selfPayeeId : $scope.selfPayeeIdCookies,
						admissionId : $scope.admissionId,
						isFinalBill: $scope.isFinalBill,
						roundOff : $scope.roundOffAmt,
						selfPayable : $scope.totalSelfPayble,
						creditPayable : $scope.totalCreditPayble,
						netAmount : parseFloat(parseFloat($scope.totalNetPayableAmt)+parseFloat($scope.roundOffAmt)).toFixed(2)
				};
			 $scope.billDetails=[];
			 angular.forEach($scope.billServicesList,function(value,key)
						{
				 			if(value.packageConsumptionAmt == 0)
				 			{
				 				var newArr  = {};
					 			newArr.billDetailsId = value.billingDetailsId;
					 			newArr.discountPer = value.discountPercentage;
					 			newArr.concessionPer = value.concessionPercentage;
					 			newArr.ordConcession = value.concession;
					 			newArr.ordDiscount = value.discount;
					 			newArr.selfPayable = value.selfPayable;
					 			newArr.creditPayable = value.creditPayable;
					 			newArr.netAmount = value.netAmt;
					 			newArr.taxAmount = value.taxAmount;
					 			$scope.billDetails.push(newArr);
				 			}
				 			
						});
			 data.orderDetailsMasterDtosList = $scope.billDetails;
			 console.log("$scope.billDetails",$scope.billDetails);
			// return false;
			 $rootScope.startLoader();
			 var URI = BASE_URL + ROOT_URL + "/api/billing/concessionManager/finalUpdate";
			 BillingGenericService.serviceAction(METHOD_POST,URI, data , NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					// console.log(response)
					if(response.data.status == 'success'){
						$rootScope.stopLoader();
						$scope.totalSelfPayble = 0;
						$scope.init();
						$scope.billRemark="";
						growl.success('Bill updated Successfully..!!',
								{
									title : 'Success!'
								});
						
					}
			 });
			 
		 }
		
		
		
	}catch(e){
		console.log(e.message);
	}
}
function config($stateProvider) {
	$stateProvider.state('concessionManager', {
		url: '/concessionManager',
		templateUrl: 'views/master/UnitMaster/billingMaster/concession-management.html',
		controller: "concessionManagementController",
		title: 'Concession Manager',
	})
}

angular
.module("myApp")
.config(config)
.controller("concessionManagementController", concessionManagementController);
})();