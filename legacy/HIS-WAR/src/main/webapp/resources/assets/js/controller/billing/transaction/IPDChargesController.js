/**
 * 04-07-2018
 * VivekS.
 * IPC Charges Controller
 */

(function(){
	"use strict";
	
	function IPDChargesController($scope,$rootScope,$cookies,$state,BillingGenericService,GenericService,$stateParams,$timeout,$http,growl,paymentCollFactory,GenericServiceParamHeader,packageService,reportService,servicesFactory,GenericServiceFileUpload,$filter){
		var self = this;
		
		/*var callback = function(val){
			//console.log('val : ', val);
		}*/
		
		$scope.admissionType = 2;
		$scope.financialRequestSendTo = "";
		$scope.loadingServiceFlag = false;
		var addService = 0;
		var init = 1;
		var quantity = 2;
		var unitPrice = 3;
		var discountPer = 4;
		var discount = 5;
		var discountCat = 5;
		
		$scope.init = function() {
			
			
			$scope.isBillWiseDiscountApplied = false;
			$scope.disableAddService = true;
            $scope.getDisRiqFlag = true;
            $scope.preBillId = "";
            $scope.financialFlag = 'N';
            $scope.packaged = {};
            $scope.packaged.withPackageDetail = 'N';
            $scope.packaged.withRatePackageDetail = 'N';
            $scope.capExist = false;
            $scope.autoRenderedServicesForSaveAndUpdate=[];
			$scope.renderingDate = [];
            $scope.actualServiceCount =0;			
			$scope.packageApplicableDate = [];
			
			$scope.packageApplicableDateDt = [];
			
			$scope.forceAddOrRemove = false;
			
			$scope.datepickerAddService = function(index) 
			{
				$scope.renderingDate[index].opened = true;
	        };
	        
	        $scope.datepickerApplicableDate = function(index) 
			{
				$scope.packageApplicableDate[index].opened = true;
	        };
	        
	        $scope.datepickerApplicableDateCom = function(index) 
			{
	        	$scope.packageApplicableDateDt.push({opened:false});
				$scope.packageApplicableDateDt[index].opened = true;
				
				//console.log($scope.listPackageOrder.packageApplicableDateDt);
	        };
	        
	      
	        
	      //Patient Search DOB Start
			$scope.openDatePicker = function() {
				$scope.datepicker.opened = true;
			};
			
			$scope.datepicker = {
					opened : false
				};
			//Patient Search DOB End
	        
	        $scope.dateOptionsRendering = 
	        {
				formatYear : 'yyyy',
				showWeeks : false,
				maxDate:new Date()
			};
			
			$scope.isSplitBillFlag = false;
			$scope.isSplitBillApplied = false;
			$scope.serviceViseSplit = false;
			$scope.isFromDischarge = false;
			
			var cookieObject,cookieObjectAppConfig;
			[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
			$rootScope.loginpage = true;
			
			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			
			var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
			if(cookieObjectAppConfig == undefined){
				$state.go('applicationConfiguration');
				return;
			}
			
			
			var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
			if (cookieObjectAppConfigSecond != undefined) {
			$scope.ambulanceChargesServiceId =  cookieObjectAppConfigSecond.ambulanceChargesServiceId;
			}
/*			var labConfig = $cookies.getObject('labConfig');
			$scope.outsourceLabId = labConfig.outsourceLabId;*/
			var labConfig = JSON.parse(localStorage.getItem('labConfig'));
                            if (labConfig) {
                                $scope.outsourceLabId = labConfig.outsourceLabId;
                            }
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			$scope.radiologySpecialityId = cookieObjectAppConfig.radiologySpecialityId;
			$scope.labConfig =labConfig;
			$scope.labSpecialityId = labConfig==undefined ? 0 : labConfig.labSpecialityId;
			$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
			$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
			$scope.isSplitBillUnitFlag = cookieObjectAppConfig.isServiceWiseSplitBill;
			$scope.pharmacyServiceId = cookieObjectAppConfig.pharmacyServiceId;
			$scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
			$scope.surchargeServiceId = cookieObjectAppConfig.surchargeServiceId;
			$scope.pharmacyServiceId = cookieObjectAppConfig.pharmacyServiceId;
			$scope.isRenderedServiceRateEditable = cookieObjectAppConfig.isRenderedServiceRateEditable;
			$scope.isSchemeDiscount = cookieObjectAppConfig.isSchemeDiscount;
			$scope.serviceWiseDoctorListReq = cookieObjectAppConfig.serviceWiseDoctorListReq;
			$scope.isGstVisible = cookieObjectAppConfig.isGstVisible;
		    $scope.defaultDiscountOwnership = cookieObjectAppConfig.defaultDiscountOwnership;
		    $scope.isSmartCardRequired = cookieObjectAppConfig.isSmartCardRequired;
		    $scope.consultationSpecialityId = cookieObjectAppConfig.consultationSpecialityId;
		    $scope.isPackageApplicableDateEditable = cookieObjectAppConfig.isPackageApplicableDateEditable;
		    $scope.isForceUpdateServicePackage = cookieObject.isForceUpdateServicePackage;
			$scope.unitId = cookieObject.unitId;
			$scope.getVirtualStore();
			$scope.isRenderedServiceCancelled = cookieObject.isRenderedServiceCancelled;
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.isUpdateIpdChargesRemark = cookieObject.isUpdateIpdChargesRemark;
			$scope.isBillManipulationAllowedUserLevel = cookieObject.isBillManipulationAllowedUserLevel;
			$scope.isBillManipulationAllowedAppLevel = cookieObjectAppConfigSecond.isBillManipulationAllowedAppLevel;
			$scope.isBillManipulationAllowed = ($scope.isBillManipulationAllowedUserLevel && $scope.isBillManipulationAllowedAppLevel =='Y') ? true : false;
			$scope.neuronPlus = cookieObjectAppConfig.neuronPlus;
			$scope.serviceRateEditable = cookieObject.serviceRateEditable;
			
			 $scope.physiotherapySpecialityId=cookieObjectAppConfig.physiotherapySpecialityId;
			 
			 $scope.hemodialysisSpecialityId=cookieObjectAppConfig.hemodialysisSpecialityId;
			 $scope.dateFormat = cookieObjectAppConfig.dateFormat;
			 $scope.isConsolidatedBill=cookieObjectAppConfig.isConsolidatedBill;
			$scope.ServiceCount=0;
			$scope.visitTypeId='';
			$rootScope.loginpage = true;
			$scope.currentDate = new Date();
			$scope.serviceMasterIdCheck = {};
			$scope.itemSelecteds = {};
			$scope.autoRenderedServicesFinalList = [];
			$scope.removeDiscountList = [];
			$scope.patientList  = [];
			$scope.removeServicesList = [];
			$scope.autoRenderedServicesBillingDetails=[];
			$scope.packageResultObj = {};
			$scope.finalBill = "N";
			$scope.interimBill = "N";
			$scope.isIntermediateBill = "N";
			//$scope.isIntermediateDisableEnableBill = false;
			$scope.isPatientClass = "N";
			$scope.roundOffAmt = parseFloat(0).toFixed(2);
			 $scope.billRemark = '';
			 $scope.discountRadio = 1;
			 $scope.changePayeeRights = cookieObject.changePayeeRights;
		     $scope.groupWiseDisFlag = true;
		     
		     $scope.enableDisabledValidated=false;

		     $scope.filterDummyService = 1;
		     $scope.filterHiddenService = 1;
		     $scope.addServiceIsDummyService = 0;
		     $scope.updatePayee = false;
		     $scope.needToUpdatePharmacyService = false;
		     $scope.applyAuth = false;
		     $scope.recalculation = false;
		     
		      $scope.isAddendumBill = 'N';
		     //for update Payee
		     $scope.paymentEntitlementTypeIdPopup = "1";
		     
		     $scope.paymentEntitleMentCorporate=
		         [{
		             'companyId':"",
		             'contractId':"",
		             'ComApplicableDate':new Date()
		         }];
		     
		     $scope.paymentEntitleMentInsurance=
		         [{
		             'companyId':"",
		             'associateCompanyId':"",
		             'contractId':"",
		             //'statrDate':"",
		             //'endDate':"",
		             'insuranceNumber':"",
		             'insuredAmt':"",
		             'InsApplicableDate':new Date()
		         }];
		     
		     
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
		     
		     $scope.openRenderedDate = function(index) {

		         for (var i = $scope.autoRenderedServicesFinalList.length - 1; i >= 0; i--) {
		             if (i === index) {

		                 $scope.ordRenderDatetime.opened[i] = true;
		             } else {
		                 $scope.ordRenderDatetime.opened[i] = false;
		             }
		         }
		     };
		     
		     $scope.openDatePickerInsurancechangeDateOnApplicableDate = function(index) {

		         for (var i = $scope.paymentEntitleMentInsurance.length - 1; i >= 0; i--) {
		             if (i === index) {

		                 $scope.InsApplicableDate.opened[i] = true;
		             } else {
		                 $scope.InsApplicableDate.opened[i] = false;
		             }
		         }
		     }; 
		     
		     $scope.openDatePickerCorporatechangeDateOnApplicableDate = function(index) {

		         for (var i = $scope.paymentEntitleMentCorporate.length - 1; i >= 0; i--) {
		             if (i === index) {

		                 $scope.ComApplicableDate.opened[i] = true;
		             } else {
		                 $scope.ComApplicableDate.opened[i] = false;
		             }
		         }
		     };
		     
		     $scope.datepickerInss = {
		         opened : [ false ],

		     };
		     $scope.ordRenderDatetime = {
			         opened : [ false ],

			     };
		     $scope.InsApplicableDate = {
			         opened : [ false ],

			     };
		     $scope.ComApplicableDate = {
			         opened : [ false ],

			     };
		 $scope.openDatePickerInsuranceEndDate = function(index) {
			 var i = $scope.paymentEntitleMentInsurance.length - 1;
			 $scope.datepickerInsE.opened[i] = true;
/*
		         for (var i = $scope.paymentEntitleMentInsurance.length - 1; i >= 0; i--) {
		             if (i === index) {

		                 $scope.datepickerInsE.opened[i] = true;
		             } else {
		                 $scope.datepickerInsE.opened[i] = false;
		             }
		         }*/
		     };

		     $scope.datepickerInsE = {
		         opened : [ false ],

		     };
		     //For Update Payee

		     
			 $("#savedisablebtn").removeClass("enabled");
			 $("#savedisablebtn").addClass("disabled");
			 $scope.transferToActiveDeposit = "N";
			 
			 $scope.patientFullName="";
				$scope.uhidNum="";
				$scope.genderCode="";
				$scope.dateOfAdmission = "";
				$scope.patientDischargeDate = "";
				$scope.bedNumber = "";
				$scope.doctorFullName = "";
				$scope.admissionNumber = "";
				$scope.department = "";
				$scope.department ="";
				
			 
			 	$scope.totalAmountSummary = "0.00";
				$scope.totalConcessionSummary = "0.00";
				$scope.netPayableSummary = "0.00";
				$scope.totalDiscountSummary = "0.00";
				$scope.totalGstTaxSummary = "0.00";
				$scope.totalCreditPayble = "0.00";
				$scope.totalSelfPayble = "0.00";
				$scope.totalSelfPaybleTemp = "0.00";
				
			
			var commonObj = {
					unitId : $scope.unitId,
					organizationId : $scope.organizationId
				};
			
			$scope.focusdata6 = function(event,item){
				 var idsearchServiceId = 'searchServiceId';
			        document.getElementById(idsearchServiceId).focus(); 
			 };
			 
			//start for request discount amount DD
			$scope.requestDiscountDiv = false;
			$scope.requestApporivedDiscountDiv = false;
			$scope.discountDisabledFlagForRequest = false;
			//end for discount amount DD
			
			// for popup
			$scope.showModal = false;
			$scope.buttonClicked = "";
			$scope.popUpFlag = true;
			// end for popup
			
			$scope.contractId = 0;
			
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
			
			$scope.patientDetails = {
					"patientId"	  : "",
					"patientName" : "",
					"genderCode"  : "",
					"age"         : "", 
					"uhIdNumber"  : "",
					"birthDate"	  : "",
					"encounterId" : "",
					"genderId":""
			}
				
				$scope.encounterDetails = {
						encounterId:""
				}
				
				$scope.billingMasterObj = {
						"billingMasterId"   :"",
						"encounterId"		:$scope.patientDetails.encounterId,
						"visitTypeId"		:$scope.visitTypeId,
						"patientId"			:$scope.patientDetails.patientId,
						"billNumber"		:"",
						"billDateTime"		:new Date().getTime(),
						"netAmount"			:"",
						"discountAmount"	:"",
						"grossAmount"		:"",
						"concessionAmount"	:"",
						"gst"				:"",
						"totalBillAmount"	:"",
						"outstanding"		:"",
						"roundOff"			:"",	
						"isCreditBill"		:"",
						"unitId"			:$scope.unitId,
						"organisationId"	:$scope.organizationId,
						"status"			:"A",
						"createdDate"		:new Date().getTime(),
						"createdBy"			:$scope.userId,
						"updatedDate"		:new Date().getTime(),
						"updatedBy"			:$scope.userId,
						"bedTypeId":"1",
						"patientTypeId":"1",
						"paymentTypeId":"1",
						"isDiscountInPercentage":'I',
						"isDiscountApplicable":"I",
						"listBillingDetailsDto":[],
						"listAutoRenderedServiesBillingDetails":[],
					//	"orderDetailsDiscountDto":"",
						"orderMasterDto"		:{
								"orderId"		:"",
								"orgId"			:$scope.organizationId,
								"orgUnitId"		:$scope.unitId,
								"orderNo"		:"",
								"orderDate"		:new Date().getTime(),
								"visitTypeId"	:"2",
								"visitAdmId"	:$scope.patientDetails.encounterId,
								"ordDoctorId"	:"",
								"patientId"		:$scope.patientDetails.patientId,
								"priorityId"	:"2",
								"patientType"	:"",
								"wardId"		:"",
								"drCrId"		:"",
								"ordRemarks"	:"",
								"createdBy"		:"1",
								"createdDate"	:new Date().getTime(),
								"updatedBy"		:"1",
								"updatedDate"	:new Date().getTime(),
								"orderStatus"	:"A",
								"orderStatusId"	:"1",
								"listOrderDetailsMasterDto":[]
							}
						
				}
				
				//for userList DD
				
				var URI = BASE_URL + ROOT_URL
				+ "/billing/getUsersDiscRights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/userId/"+$scope.userId;
		BillingGenericService.serviceAction(METHOD_GET, URI,"",
						NOTIFICATION_MSG_STATUS_FALSE)
				.then(function(response) {
							//console.log(response);
							// $rootScope.startLoader();
							if (response.data.status == "success")
								{
								$scope.userDiscountObject ={};
								$scope.userDiscountObject = response.data.object;
								if($scope.userDiscountObject != null){
						    		
									if($scope.userDiscountObject.discountType == 1){
										$scope.userDiscountObject.fromDiscountPer = $scope.userDiscountObject.fromDiscountAmount;
										$scope.userDiscountObject.toDiscountPer = $scope.userDiscountObject.toDiscountAmount;
									}
						    	}
									 
								}
								
						});
				//for UserList DD
		
		
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



				
				var URI = BASE_URL + ROOT_URL
				+ GETACTIVESPECIALITY;
		BillingGenericService.serviceAction(METHOD_POST, URI,commonObj,
						NOTIFICATION_MSG_STATUS_FALSE)
				.then(function(response) {
							//console.log(response);
							// $rootScope.startLoader();
							if (response.data.status == "success")
								{
								$scope.specialityMasterList = response.data.listObject;
									 
								}
								
						});
		
		 var URI = BASE_URL + ROOT_URL +"/api/billing/getPatientType/"+$scope.organizationId+"/"+$scope.unitId;
		    GenericService
		        .serviceAction(METHOD_GET, URI,
		                NOTIFICATION_MSG_STATUS_FALSE)
		        .then(
		                function(response) {
		                    //console.log(response);
		                    if (response.data.status == "success"){
		                        
		                        $scope.patientTypeList = response.data.listObject;
		                        //console.log("list",$scope.patientTypeList);
		                    }
		                    
		                });
		    

		var URI = BASE_URL + ROOT_URL
		+ GETACTIVEREASONLIST;

		BillingGenericService
		.serviceAction(METHOD_POST, URI,
				commonObj,
				NOTIFICATION_MSG_STATUS_FALSE)
		.then(
				function(response) {
					//console.log(response);
					// $rootScope.startLoader();
					if (response.data.status == "success")
						$scope.reasonList = response.data.listObject;
				});

//		var URI = BASE_URL + ROOT_URL+ GETACTIVEDOCTORLIST;
		var URI = BASE_URL + ROOT_URL + "/adt/getActiveDoctorList";
		GenericService
		.serviceAction("POST", URI, commonObj)
		.then(
				function(response) {
					$scope.doctorsList = [];

					if (response.data.status == "success") {
						$scope.doctorsList = response.data.listObject;

					}
				});


		var URI = BASE_URL + ROOT_URL + "/api/billing/discountCategories/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId;
		BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			$scope.discountCategoryList = response.data.listObject;
		});

			$scope.servicesSelectedList = {
			    services: []
			  };
			
			$scope.groupWiseServicesSelectedList = {
					services: []
			};
			
			
			$scope.billWiseDiscountObj = {
					//"discountGivenBy":"",
					"discountGivenBy":$scope.userId,
					"discountCategoryId" : "",
					"discountAmountBillWise" : "",
					"discountPercentageBillWise" : "",
					"approvedAmountBillWise":"",
					"requestUserId":"",
					"requestAmountBillWise" : 0,
					"remark":''
			}
			$scope.finanacial = {
					"financialRequestSendTo":""
			}
			$scope.isFinancialClearence = 'N';
			//yogesh
			$scope.searchPackageList = [];
			$scope.selectedPackageList = [];
			$scope.packageDetailsList = [];
			$scope.admissionDetails = {};
			
			self.model = {
					"unitId"          :$rootScope.unitId,
					"organizationId"  :$rootScope.organizationId,
					"visitTypeId" : 2,
					"groups":[]
			}
			$scope.searchPatient = searchPatient;
			$scope.getPatientDetailsByIndex = getPatientDetailsByIndex;
			$scope.groupWiseSummary = groupWiseSummary;
			$scope.orderedServices = [];
			
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
			
		      $scope.patientDetails = {
					"patientId"	  : "",
					"patientName" : "",
					"genderCode"  : "",
					"age"         : "", 
					"uhIdNumber"  : "",
					"birthDate"	  : "",
					"encounterId" : ""
			}
			
			$scope.activePackagesList = [{
					"orderNo": "",
					"orderDate" : "",
					"packageName" : "",
					"packagePrice": "",
					"packagePrice": "",
					"orderBy": ""
			}];
			
			$scope.transferList=[];
			$scope.medicationIssueList = [];

			$scope.getPackageTypeList($scope.organizationId,$scope.unitId);
			$scope.listPackageOrder = [];
			
			////console.log('abc',this.serviceAddForm.$invalid);
			//console.log('abc',$scope.servicesListPopup.length);
			//console.log('abc',$scope.activePackagesList.length);
			
			$scope.getReferralMasterList = function(referralTypeId){
				var URI = BASE_URL + ROOT_URL + "/adt/getReferralListByOrgId";
				var obj=
				      { 
						 organizationId:$scope.organizationId,
				      };
					BillingGenericService.serviceAction(METHOD_POST, URI, obj).then(function(response) {
							$scope.referralMasterList = [];
							if (response.data.status == "success") {
								$scope.referralMasterList = response.data.listObject;
							}
					});
				};
			 $scope.getReferralMasterList();
			
			}	
		
	
		
		$scope.initPatientSelected = function() {
			 $scope.isBillWiseDiscountApplied = false;
			$scope.disableAddService = true;
			
			
			
			$scope.serviceMasterIdCheck = {};
			$scope.itemSelecteds = {};
			$scope.autoRenderedServicesFinalList = [];
			$scope.autoRenderedServicesForSaveAndUpdate=[];
			$scope.removeDiscountList = [];
			$scope.removeServicesList = [];
			$scope.autoRenderedServicesBillingDetails=[];
			$scope.cancelService = false;
			$scope.finalBill = "N";
			$scope.interimBill = "N";
			$scope.isIntermediateBill = "N";
			//$scope.isIntermediateDisableEnableBill = false;
			$scope.isPatientClass = "N";
			$scope.roundOffAmt = parseFloat(0).toFixed(2);
			 $scope.billRemark = '';
			 $scope.discountRadio = 1;

			 $("#savedisablebtn").removeClass("enabled");
			 $("#savedisablebtn").addClass("disabled");
			 $scope.transferToActiveDeposit = "N";
			 
			 	$scope.totalAmountSummary = "0.00";
				$scope.totalConcessionSummary = "0.00";
				$scope.netPayableSummary = "0.00";
				$scope.totalDiscountSummary = "0.00";
				$scope.totalGstTaxSummary = "0.00";
				$scope.totalCreditPayble = "0.00";
				$scope.totalSelfPayble = "0.00";
			
			
			//start for request discount amount DD
			$scope.requestDiscountDiv = false;
			$scope.requestApporivedDiscountDiv = false;
			$scope.discountDisabledFlagForRequest = false;
			//end for discount amount DD
			
			
			$scope.contractId = 0;
			
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
			
			$scope.patientDetails = {
					"patientId"	  : "",
					"patientName" : "",
					"genderCode"  : "",
					"age"         : "", 
					"uhIdNumber"  : "",
					"birthDate"	  : "",
					"encounterId" : "",
					"genderId":""
			}
				
				$scope.encounterDetails = {
						encounterId:""
				}
				
				$scope.billingMasterObj = {
						"billingMasterId"   :"",
						"encounterId"		:$scope.patientDetails.encounterId,
						"visitTypeId"		:$scope.visitTypeId,
						"patientId"			:$scope.patientDetails.patientId,
						"billNumber"		:"",
						"billDateTime"		:new Date().getTime(),
						"netAmount"			:"",
						"discountAmount"	:"",
						"grossAmount"		:"",
						"concessionAmount"	:"",
						"gst"				:"",
						"totalBillAmount"	:"",
						"outstanding"		:"",
						"roundOff"			:"",	
						"isCreditBill"		:"",
						"unitId"			:$scope.unitId,
						"organisationId"	:$scope.organizationId,
						"status"			:"A",
						"createdDate"		:new Date().getTime(),
						"createdBy"			:$scope.userId,
						"updatedDate"		:new Date().getTime(),
						"updatedBy"			:$scope.userId,
						"bedTypeId":"1",
						"patientTypeId":"1",
						"paymentTypeId":"1",
						"isDiscountInPercentage":'I',
						"isDiscountApplicable":"I",
						"listBillingDetailsDto":[],
						"listAutoRenderedServiesBillingDetails":[],
					//	"orderDetailsDiscountDto":"",
						"orderMasterDto"		:{
								"orderId"		:"",
								"orgId"			:$scope.organizationId,
								"orgUnitId"		:$scope.unitId,
								"orderNo"		:"",
								"orderDate"		:new Date().getTime(),
								"visitTypeId"	:"2",
								"visitAdmId"	:$scope.patientDetails.encounterId,
								"ordDoctorId"	:"",
								"patientId"		:$scope.patientDetails.patientId,
								"priorityId"	:"2",
								"patientType"	:"",
								"wardId"		:"",
								"drCrId"		:"",
								"ordRemarks"	:"",
								"createdBy"		:"1",
								"createdDate"	:new Date().getTime(),
								"updatedBy"		:"1",
								"updatedDate"	:new Date().getTime(),
								"orderStatus"	:"A",
								"orderStatusId"	:"1",
								"listOrderDetailsMasterDto":[]
							}
						
				}
				

			$scope.servicesSelectedList = {
			    services: []
			  };
			
			$scope.groupWiseServicesSelectedList = {
					services: []
			};
			
			
			$scope.billWiseDiscountObj = {
					//"discountGivenBy":"",
					"discountGivenBy":$scope.userId,
					"discountCategoryId" : "",
					"discountAmountBillWise" : "",
					"discountPercentageBillWise" : "",
					"approvedAmountBillWise":"",
					"requestUserId":"",
					"requestAmountBillWise" : 0,
			}
			$scope.finanacial = {
					"financialRequestSendTo":""
			}
			$scope.isFinancialClearence = 'N';
			//yogesh
			$scope.searchPackageList = [];
			$scope.selectedPackageList = [];
			$scope.packageDetailsList = [];
			$scope.admissionDetails = {};
			$scope.filterDummyService = 1;
		    $scope.filterHiddenService = 1;
			$scope.searchPatient = searchPatient;
			$scope.groupWiseSummary = groupWiseSummary;
			$scope.orderedServices = [];
			
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
			
		      $scope.patientDetails = {
					"patientId"	  : "",
					"patientName" : "",
					"genderCode"  : "",
					"age"         : "", 
					"uhIdNumber"  : "",
					"birthDate"	  : "",
					"encounterId" : ""
			}
			
			$scope.activePackagesList = [{
					"orderNo": "",
					"orderDate" : "",
					"packageName" : "",
					"packagePrice": "",
					"packagePrice": "",
					"orderBy": ""
			}];
			
			$scope.transferList=[];
			$scope.medicationIssueList = [];

			$scope.getPackageTypeList($scope.organizationId,$scope.unitId);
			}
		
		$scope.getBasePriceCal = function(itemObj,idx)
	    {

			 var i =0;
	    	   angular.forEach($scope.initialAutoRenderedServicesFinalList, function (value, key) {
		   			if(value.orderDetailsId == itemObj.orderDetailsId){
		   				idx = i;
		   			 }
	   			i++;
	   			});
	    	   $scope.autoRenderedServicesFinalList =[]; 
	    	   $scope.autoRenderedServicesFinalList = $scope.initialAutoRenderedServicesFinalList;
			
			if($scope.autoRenderedServicesFinalList[idx].serviceMasterId != $scope.pharmacyServiceId)
				{
	    	//console.log("OBJ",itemObj);
	    	if(itemObj.basePrice == '' || itemObj.basePrice == undefined)
			{
			  itemObj.basePrice = 0;
			}
	    	if(itemObj.basePrice == '.' )
			{
			  itemObj.basePrice =  itemObj.basePriceOld;
			}
	    	
	    	if(itemObj.maxRateEditable!= null && itemObj.maxRateEditable < itemObj.basePrice){
	    		 growl.warning(" You can edit rate max up to "+itemObj.maxRateEditable,{
						title : ERROR_MSG
					});
	    		 itemObj.basePrice =  itemObj.basePriceOld;
	    		 return false;
	    	}
	    	
	    	if(itemObj.minRateEditable!= null && itemObj.minRateEditable > itemObj.basePrice){
	   		 growl.warning(" You can edit rate min up to "+itemObj.minRateEditable,{
						title : ERROR_MSG
					});
	   		 itemObj.basePrice =  itemObj.basePriceOld;
	   		 return false;
	   	}
	    	
	    	
	    	if(parseFloat(itemObj.basePrice) != parseFloat(itemObj.basePriceRateChange))
	    	{
	    	
		    		if($scope.requestStatusObject != null)
					{
						  $scope.getCancelDiscountReqForDiscount();
					}
		    		
		    		$scope.autoRenderedServicesFinalList[idx].basePrice = parseFloat(itemObj.basePrice);
		    		var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
		    		
		    		$scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);
					$scope.autoRenderedServicesFinalList[idx].concession = ((parseFloat(totalAmt))*(isNaN(itemObj.patientClassPercentage) ? 0: itemObj.patientClassPercentage)*0.01).toFixed(2);
					$scope.autoRenderedServicesFinalList[idx].discountPercentage = parseFloat(itemObj.discountPercentage);
					$scope.autoRenderedServicesFinalList[idx].discount =((parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[idx].concession))*(isNaN(itemObj.discountPercentage) ? 0: itemObj.discountPercentage)*0.01).toFixed(2);
	                
	                var withConDic =parseFloat(totalAmt) - (parseFloat(itemObj.concession) + parseFloat($scope.autoRenderedServicesFinalList[idx].discount));
			    	
	                var txpre = 0;
					if(itemObj.taxPercentage != null || itemObj.taxPercentage > 0){
						$scope.autoRenderedServicesFinalList[idx].taxPercentage = itemObj.taxPercentage;
						txpre = itemObj.taxPercentage;
					}else{
							$scope.autoRenderedServicesFinalList[idx].taxPercentage = 0;
							}
					$scope.autoRenderedServicesFinalList[idx].taxAmt = parseFloat((parseFloat(withConDic) * txpre)*0.01).toFixed(2);
					$scope.autoRenderedServicesFinalList[idx].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[idx].taxAmt)).toFixed(2);
					$scope.autoRenderedServicesFinalList[idx].selfPayable = $scope.autoRenderedServicesFinalList[idx].netPay;
					$scope.autoRenderedServicesFinalList[idx].creditPayable =parseFloat(0);
					//$scope.saveOrderPackage();
					
					$scope.autoRenderedServicesForSaveAndUpdate=[];
		        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
		        	$scope.addOrUpdateServices(idx,null);
					
		           /* }*/
						$scope.billWiseDiscountObj = {
								// "discountGivenBy":"",
								"discountGivenBy":$scope.userId,
								"discountCategoryId" : "",
								"discountAmountBillWise" : "",
								"discountPercentageBillWise" : "",
								"approvedAmountBillWise":"",
								"requestUserId":"",
								"requestAmountBillWise" : 0,
								"remark":''
						}
						$scope.requestDiscountDiv = false;
				    }
	    	
	    	$scope.isFinancialClearence ='N';
	    	$scope.finanacial.financialRequestSendTo = "";
	    }else{
	    	//console.log("This is pharmacy service!!!");
	    	growl.warning("Pharmacy service rate is not editable.",{
				title : 'warning'
			});
	    }
	           }
		
	    $scope.rateEditCancelDiscount = function()
	    {
	    	$rootScope.startLoaderPopup();
			var data = {
					reqDiscId :$scope.requestStatusObject.reqDiscId,
			}
			
			var URI = BASE_URL + ROOT_URL + "/api/billing/cancelRequest";
			
			GenericService
			.serviceAction("POST", URI, data)
			.then(function(response) {
						$rootScope.stopLoaderPopup();
						if (response.data.status == "success") {
							
							 var i =0;
					    	   angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
					   			if(value.status == 'A'){
								   		$scope.autoRenderedServicesFinalList[i].discountCategoryId = "";
								   		$scope.autoRenderedServicesFinalList[i].discountPercentage = 0;
								   		$scope.autoRenderedServicesFinalList[i].discount = 0;
								   		//for remove discount
					   			 }
					   			i++;
					   			});
					    	   
					    	   $scope.calculationTest($scope.autoRenderedServicesFinalList,discountPer);
					    	   
							growl.success("Cancelled !!!!",{
								title : 'success'
							});
							
							$scope.requestApporivedDiscountDiv = false;
							$scope.discountDisabledFlagForRequest = false;
							$scope.requestAppUser = 0;
							$scope.approvedAmtApp = 0;
							$scope.requestAmtApp =0;
							$scope.discTypeDesc = "";
							$scope.statusApp = "";
							$scope.discountRadio = 2; 
							
							$scope.billWiseDiscountObj = {
									//"discountGivenBy":"",
									"discountGivenBy":$scope.userId,
									"discountCategoryId" : "",
									"discountAmountBillWise" : "",
									"discountPercentageBillWise" : "",
									"approvedAmountBillWise":"",
									"requestUserId":"",
									"requestAmountBillWise" : 0,
									"remark":''
							}
							 $scope.requestStatusObject  = null;
							//$scope.totalDiscountSummary = parseFloat(0)
				    		angular.element('#discountRatePopup').modal('hide');
						}
					});
	    }
	    
	    
	    $scope.updateClaimNumber=function(info){
			
			$rootScope.startLoaderPopup();
			if(info.patientSponsorDetailsId == null){
				growl.warning("The record first you have to save then update !!!",
						{
							title : 'Warning!'
						});
				$rootScope.stopLoaderPopup();
				return false;
			}
			var data ={
					
					claimNumber:info.claimNumber,
					patientSponsorDetailsId:info.patientSponsorDetailsId,
			};
		if(info.claimNumber != null && info.claimNumber !="" && info.claimNumber != undefined){
			var URI = BASE_URL + ROOT_URL + "/api/opd/updatePatientSponserDetailsClaimNumber";
			GenericService.serviceAction("POST",URI,data).then(function(response){
				if (response.data.status == 'success')
				{
					$rootScope.stopLoaderPopup();
					growl.success(response.data.message,
							{
								title : 'Success!'
							});
				} else if(response.data.status == 'error')
				{
					$rootScope.stopLoaderPopup();
					growl.warning(response.data.message,
							{
								title : 'Warning!'
							});
				}
			});
			
		}
		else{
			$rootScope.stopLoaderPopup();
			 growl.warning(" Please enter claim number. ",
						{
							title : 'warning!'
						});
		}
			
		}
	    
	    $scope.rateEditCancelDiscountLine = function()
	    {
				$rootScope.startLoaderPopup();
				$scope.autoRenderedServicesFinalList[$scope.serviceAutoRenInd].basePrice = $scope.serviceAutoRenOldBasePrice;
	   		    var autoRenderedServicesList = $scope.autoRenderedServicesFinalList;
	       	    $scope.calculationTest(autoRenderedServicesList,discount);
	       	    $rootScope.stopLoaderPopup();
				angular.element('#discountRatePopup').modal('hide');
	    }
	    
	    
	    $scope.getSexMasterList = function(){
			var URI = BASE_URL + ROOT_URL + "/adt/getActiveGenderList";
			BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				$scope.sexMasterList = response.data.listObject;
			});
		}
		$scope.getSexMasterList();
		
		//Idetification List
		$scope.getIdentificationList = function(){
			var URI = BASE_URL + ROOT_URL+ GETACTIVEIDENTIFICATION;
			GenericService.serviceAction("GET", URI, "").then(function(response) {
				$scope.identificationList = [];
				if (response.data.status == "success") {
					$scope.identificationTypeList = response.data.listObject;

				}
			});
		}
		$scope.getIdentificationList();
		
		$scope.loseSessionForBillEdit = function()
		{
			$scope.newOpdBillObject = "";
			$scope.preBillId = "";
			$scope.isAddendumBill = 'N';
			searchPatient($scope.patientSearchObj);
			
		}
		
		$scope.keepSessionForBillEdit = function()
		{
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
		}
		$scope.ipRenderedDateChangeFlag=true;
		$scope.getIpRenderedDateChangeFlag = function(patientsearchObj)
		{
			var URI = BASE_URL + ROOT_URL + "/adt/getIpRenderedDateChange";
			patientsearchObj.userId=$scope.userId
			
			BillingGenericService.serviceAction(METHOD_POST,URI, patientsearchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.listObject!=null){
					$scope.ipRenderedDateChangeFlag=response.data.listObject[0];
				}
				
			});
		}
		var searchPatient = function(patientsearchObj){
			$scope.getIpRenderedDateChangeFlag(patientsearchObj);
			$scope.requestStatusObject = {};
            $scope.getDisRiqFlag = true;

	    	$scope.requestApporivedDiscountDiv = false;
			$scope.billWiseDiscountObj.discountPercentageBillWise = "";
			$scope.billWiseDiscountObj.discountAmountBillWise ="";
			$scope.billWiseDiscountObj.discountCategoryId ="";
			if($scope.preBillId!=undefined && $scope.preBillId>0)
			{
				angular.element('#billEditSessionLost').modal('show');
				return false;
			}
			$rootScope.startLoader();
			if(patientsearchObj.patientName!="" && patientsearchObj.patientName.length < 3)
			{
				growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
				$scope.patientList = [];
				$rootScope.stopLoader();
				return false;
			}	
			var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteriaForIPD";
			patientsearchObj.visitTypeId = $scope.admissionType;
			patientsearchObj.wardList =[];
			patientsearchObj.bedList =[];
			
			if(patientsearchObj.birthDate != "" && typeof(patientsearchObj.birthDate) != "undefined"){
				patientsearchObj.birthDate = moment(patientsearchObj.birthDate).format('YYYY-MM-DD');
			}
			
			BillingGenericService.serviceAction(METHOD_POST,URI, patientsearchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				$scope.patientList = response.data.listObject;
				
				 if(response.data.listObject==null){
						growl.warning(response.data.message,{
							title : ERROR_MSG
						});
						$scope.patientSearchObj.birthDate="";
						$scope.patientList=[];
						return false;
					}
				 $scope.patientSearchObj.birthDate="";
				$scope.autoRenderedServicesFinalList = [];
				$scope.autoRenderedServicesForSaveAndUpdate=[];
				$scope.patientAdmissionList = [];
				$scope.patientDetails ={};
				$scope.admissionDetails = {};

				////console.log('$scope.patientList : ', JSON.stringify($scope.patientList));
				$scope.patientAdmissionList = [];
				self.model.patientDetails ={};
				self.model.encounterDetails = {};
				self.model.interimBills = {};
				self.model.getInterimBillsByAdmissionId = getInterimBillsByAdmissionId;
				
			});
		}
		$scope.admissionId = '';
		var getPatientDetailsByIndex = function(index){
			
			$scope.lineLevelItem = false;
			$("#patientSelectRadio_"+index).prop("checked",true);
			$scope.searchKeyword = {
					serviceStandardName:""
			}
			$scope.searchKeyword1 = {
					orderDateStringNew:""
			}
			$scope.searchKeyword2 = {
					ordRenderDatetimeString:""
			}
			
			$scope.checkKeywordLength($scope.searchKeyword.serviceStandardName,$scope.searchKeyword.serviceStandardName,$scope.searchKeyword2.ordRenderDatetimeString);
			$scope.getSpecialityByDoctorMain();
			$scope.drawAllCount=0;
			$scope.recordDisplay=1;
			$rootScope.startLoader();
			$scope.depositList = [];
            $scope.getDisRiqFlag = true;
            
         
            $scope.forceAddOrRemove = false;
			$scope.initPatientSelected();
			$scope.isIntermediateBill='N';
		    $scope.isFinancialClearence='N';
		    $scope.finalBill='N';
		    $scope.interimBill = 'N';
		    $rootScope.thirdTabLevel = 1;
		    $scope.changeTab(1);
		   
		    
			$scope.patientDetails.age=($scope.patientList[index].age);
			$scope.patientDetails.patientName=($scope.patientList[index].patientName);
			$scope.patientDetails.genderCode=($scope.patientList[index].genderCode);
			$scope.patientDetails.uhIdNumber=($scope.patientList[index].uhIdNumber);
			$scope.patientDetails.patientId=($scope.patientList[index].patientId);
			$scope.patientDetails.birthDate=($scope.patientList[index].birthDate);
			$scope.patientDetails.genderId = ($scope.patientList[index].genderId);
			$scope.patientDetails.firstName=($scope.patientList[index].firstName)+" "+($scope.patientList[index].middleName)+" "+($scope.patientList[index].lastName);
			$scope.patientDetails.address = ($scope.patientList[index].address);
			$scope.patientDetails.prefixId = ($scope.patientList[index].prefixId);
			$scope.patientDetails.cityName = ($scope.patientList[index].cityName);
			if($scope.patientList[index].mobileNo!=null){
				$scope.mobileNumber = parseInt($scope.patientList[index].mobileNo);
			}
			
			$scope.patientFullName=($scope.patientList[index].patientName)+ " "+$scope.patientList[index].age	+ "/"+ $scope.patientList[index].genderCode;
			$scope.uhidNum=($scope.patientList[index].uhIdNumber);
			$scope.genderCode=($scope.patientList[index].genderCode);
			$scope.admissionId = ($scope.patientList[index].admissionId);
			$scope.visitTypeId = $scope.patientList[index].visitTypeId;
			//console.log($scope.genderCode)
			//self.model.periodicityServ.visitTypeId = $scope.patientList[index].visitTypeId;
			
			 
			$scope.getNewDoctorList();
			//get admission details by id
			if($scope.admissionId != null && typeof($scope.admissionId) != "undefined")
			{
				$scope.loadingServiceFlag = true;
				$rootScope.startLoader();
				if($scope.preBillId > 0 && $scope.preBillId != undefined)
				{
					
					var URI = BASE_URL + ROOT_URL + "/api/ipd/admissionDetails/cancelledBills/"+$scope.admissionId;
					BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						if(response.data.status == "success"){
							$scope.admissionDetails = response.data.object;
                            $scope.admissionDetails.remark = (($scope.admissionDetails.remark == null || $scope.admissionDetails.remark == "null") ? "":$scope.admissionDetails.remark);

							if (angular.equals({},$stateParams.patientDetailsObj)) {
								return;
							} else {
								if($stateParams.patientDetailsObj.isInterimOrFinalBill == 1){
									$scope.isIntermediateBill='Y';
									  $scope.finalBill='N';
								}
								if($stateParams.patientDetailsObj.isInterimOrFinalBill == 2){
									$scope.isIntermediateBill='N';
									  $scope.finalBill='Y';
									  $scope.admissionDetails.dischargeStatusId = 3;
								}
								
						        if($stateParams.patientDetailsObj.billEditType == 1){
						            $scope.isAddendumBill = 'Y';
						        }else{
						            $scope.isAddendumBill = 'N';
						        }

						        
							}
						 
							$scope.isTariffRateApplicablePopup = $scope.admissionDetails.isTariffRateApplicable;
							$scope.getFutureOrders();
							$scope.getFreeTextOrders($scope.admissionDetails.admissionId);
							$scope.patientClassIdPopup = ($scope.admissionDetails.patientClassId > 0 ? $scope.admissionDetails.patientClassId : '');
								$scope.disableAddService = false;
								$("#savedisablebtn").removeClass("disabled");
								 $("#savedisablebtn").addClass("enabled");
							if($scope.admissionDetails.payeeId != $scope.selfPayeeId)
							{
								$scope.contractId = $scope.admissionDetails.contractId;
							}
						else
							{
								$scope.contractId = 0;
							}
							
							$scope.dateOfAdmission = typeof($scope.admissionDetails.doa) != "undefined" ? $scope.admissionDetails.doa : "";
                            var datePartsad = $scope.dateOfAdmission.split('/');
                            var dateObjectad = new Date(datePartsad[2], datePartsad[1] - 1, datePartsad[0]); 
                            var longValuead = dateObjectad.getTime();
                            $scope.doaActual = longValuead;                                    
							
                            $scope.patientDischargeDate = typeof($scope.admissionDetails.pdd) != "undefined" ? $scope.admissionDetails.pdd : "";
                            var datePartspdd = $scope.patientDischargeDate.split('/');
                            var dateObjectpdd = new Date(datePartspdd[2], datePartspdd[1] - 1, datePartspdd[0]); 
                            var longValuepdd = dateObjectpdd.getTime();
                            $scope.pddActual = longValuepdd;
                            
                            $scope.bedNumber = typeof($scope.admissionDetails.bedNo) != "undefined" ? $scope.admissionDetails.bedNo : "";
							$scope.doctorFullName = typeof($scope.admissionDetails.doctorName) != "undefined" ? $scope.admissionDetails.doctorName : "";
							$scope.admissionNumber = typeof($scope.admissionDetails.admissionNumber) != "undefined" ? $scope.admissionDetails.admissionNumber : "";
							$scope.department = typeof($scope.admissionDetails.department) != "undefined" ? $scope.admissionDetails.department : "";
							$scope.specialityName = $scope.department;
							console.log($scope.dateOfAdmission);
							
							var arrayDoa = $scope.dateOfAdmission.split('/');
							var newDateDoa = "";
						   newDateDoa = arrayDoa[1]+'/'+arrayDoa[0]+'/'+arrayDoa[2];
						   
						   
							$scope.dateOptionsRendering.minDate = new Date(newDateDoa);
							getOrderedServices();
							$scope.getActivePackagesList();
							//get interim bills
							self.model.getInterimBillsByAdmissionId($scope.admissionDetails.admissionId);
						}
						 $rootScope.stopLoaderPopup();
						 angular.element('#addServicePopup').modal('hide');
						$rootScope.stopLoader();
					});
					
				}else{
					var URI = BASE_URL + ROOT_URL + "/api/ipd/admissions/patient/"+$scope.admissionId;
					BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						if(response.data.status == "success"){
							$scope.admissionDetails = response.data.object;
							$scope.dischargeStatusId=$scope.admissionDetails.dischargeStatusId;
							$scope.specialityName = $scope.admissionDetails.department;
							$scope.doaActual=$scope.admissionDetails.doaActual;
							$scope.pddActual=$scope.admissionDetails.pddActual;
							 $scope.admissionDetails.remark = (($scope.admissionDetails.remark == null || $scope.admissionDetails.remark == "null") ? "":$scope.admissionDetails.remark);
							
							 if ($scope.preBillId > 0 && $scope.preBillId != undefined) 
							 {
								if($stateParams.patientDetailsObj.isInterimOrFinalBill == 1){
									$scope.isIntermediateBill='Y';
								}
								if($stateParams.patientDetailsObj.isInterimOrFinalBill == 2){
									$scope.isIntermediateBill='N';
									$scope.finalBill='Y';
									$scope.admissionDetails.dischargeStatusId = 3;
								}
							}
							 
							 
							$scope.isTariffRateApplicablePopup = $scope.admissionDetails.isTariffRateApplicable;
							$scope.getFutureOrders();
							$scope.getFreeTextOrders($scope.admissionId);
							$scope.patientClassIdPopup = ($scope.admissionDetails.patientClassId > 0 ? $scope.admissionDetails.patientClassId : '');
							if($scope.admissionDetails.isFinalBill == "Y")
							{
								growl.warning("Final bill is already generated for this admission..!!",{
									title : "WARNING"
								});
								$scope.admissionDetails = null;
								$scope.loadingServiceFlag = false;
								return false
								
							}else{
								$scope.disableAddService = false;
								$("#savedisablebtn").removeClass("disabled");
								 $("#savedisablebtn").addClass("enabled");
							if($scope.admissionDetails.payeeId != $scope.selfPayeeId)
							{
								$scope.contractId = $scope.admissionDetails.contractId;
							}
						else
							{
								$scope.contractId = 0;
							}
							
							$scope.dateOfAdmission = typeof($scope.admissionDetails.doa) != "undefined" ? $scope.admissionDetails.doa : "";
							$scope.patientDischargeDate = typeof($scope.admissionDetails.pdd) != "undefined" ? $scope.admissionDetails.pdd : "";
							$scope.bedNumber = typeof($scope.admissionDetails.bedNo) != "undefined" ? $scope.admissionDetails.bedNo : "";
							$scope.doctorFullName = typeof($scope.admissionDetails.doctorName) != "undefined" ? $scope.admissionDetails.doctorName : "";
							$scope.admissionNumber =typeof($scope.admissionDetails.admissionNumber) != "undefined" ? $scope.admissionDetails.admissionNumber : "";
							$scope.department =typeof($scope.admissionDetails.department) != "undefined" ? $scope.admissionDetails.department : "";
							////console.log($scope.dateOfAdmission);
							
							var arrayDoa = $scope.dateOfAdmission.split('/');
							var newDateDoa = "";
							newDateDoa = arrayDoa[1]+'/'+arrayDoa[0]+'/'+arrayDoa[2];
						   
						   
							$scope.dateOptionsRendering.minDate = new Date(newDateDoa);
							getOrderedServices();
							$scope.getActivePackagesList();
							//get interim bills
							self.model.getInterimBillsByAdmissionId($scope.admissionDetails.admissionId);
						 }
						}
						 $rootScope.stopLoaderPopup();
						 angular.element('#addServicePopup').modal('hide');
						$rootScope.stopLoader();
					});
				}
			
				
				var URI = BASE_URL + ROOT_URL + "/api/opd/sponsoredDetails/admission/"+$scope.admissionId+"/encounter/0";
				BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						 $scope.patientSponsorDetailsDtosListSplit = [];
						 $scope.patientSponsorDetailsDtosListSplit = response.data.listObject;
						 angular.forEach(response.data.listObject,function(value,index)
						 {
					 		if(value.capExist==true)
					 			$scope.capExist = true;
						 });
						 
					}
				});
				
				
		        if($scope.corEmpVerificationFlag){
		            $scope.enableDisabledApply=false;
		        }else{
		            $scope.enableDisabledValidated=false;
		            $scope.enableDisabledApply=false;
		        }
			}
			else
				{
				$rootScope.stopLoader();
				growl.warning("No Record Found..!!",{
					title : ERROR_MSG
				});
				return false
				
				}
			//Assignment to model (Santosh)
			self.model.patientDetails.patientId=($scope.patientList[index].patientId);//4
			//ADMISSION & ENCOUNTER IDS SAME VARIABLE;
			self.model.encounterDetails.encounterId = ($scope.patientList[index].admissionId); 
			/*self.model.visitTypeId = 1;*/
			
			if(self.model.encounterDetails.encounterId != null){
				packageService.getActivePackagesList(self.model, function(activePackageList){
					$scope.activePackagesList = activePackageList;
				});
				
				packageService.getTransferListByAdmissionId(self.model.encounterDetails.encounterId, function(transferList){
					$scope.transferList = transferList;
				});
				packageService.getTransferOfCareListByAdmissionId(self.model.encounterDetails.encounterId, function(transferOfCareDetailsList){
					$scope.transferOfCareDetailsList = transferOfCareDetailsList;
				});
				
				packageService.getDepositListByPatientId(self.model.patientDetails.patientId,$scope.unitId, function(depositList){
					 $timeout(function () {
                        self.model.depoSummary=0;
                        self.model.totalBalance=0;
                        angular.forEach(depositList, function(v,k){
                            if(v.encounterNumber == null) {
                                if (v.admissionNumber == null || v.admissionNumber == $scope.admissionNumber){
                                $scope.depositList.push(v);
                                }
                               }

                        //	$scope.depositList = depositList;
                        });
                    }, 1000);
				});
				packageService.getPeriodicityServicesByAdmissionId(self.model.encounterDetails.encounterId, function(periodicityServiceList){
					//self.model.depoSummary=0;
					$scope.periodicityServiceList = periodicityServiceList;
				});
				self.model.medSummary=0;
				getMedicationIssueListByAdmissionId(self.model.encounterDetails.encounterId);
				
			}
//			if($scope.isFromDischarge)
//				$(".patienttSearch").hide();
//			else{
//				$(".patienttSearchResult").toggle();
//			}
			$(".patienttSearchResult").toggle();
			$rootScope.stopLoader();
		};
		

		$scope.getFreeTextOrders = function(encounterId)
		{
			var data1={
					admissionId:encounterId,
			}
			
			var URI = BASE_URL + ROOT_URL + "/api/billing/getFreeTextOrderServices";
			BillingGenericService .serviceAction(METHOD_POST, URI, data1, NOTIFICATION_MSG_STATUS_FALSE)
					.then(
							function(response) {
								$scope.freeTextServiceList = [];
								if (response.data.status == "success") {
									
									angular.forEach(response.data.listObject,function(value,index){
										value.search="";
										value.status='A';
									});
									
									$scope.freeTextServiceList = response.data.listObject;
									if(response.data.listObject.length > 0){
										 angular.element('#freeTextServicesModal').modal('show');
									}
								}

							});
		}

		$scope.getFutureOrders = function()
		{
			//SNEHA
			var data1={
					patientId:$scope.patientDetails.patientId,
					organisationId:$scope.organizationId,
					unitId : $scope.unitId,
			}
			
			var URI = BASE_URL + ROOT_URL + "/api/opd/getPatientServicesForFutureDate";
			BillingGenericService .serviceAction(METHOD_POST, URI, data1, NOTIFICATION_MSG_STATUS_FALSE)
					.then(
							function(response) {
								//console.log(response);
								$scope.futureServiceList = [];
								// $rootScope.startLoader();
								if (response.data.status == "success") {
									angular.forEach(response.data.listObject,function(value,index){
										value.checkOption='N';
										$scope.futureServiceList.push(value);
									});
//									$scope.futureServiceList = response.data.listObject;
									if(response.data.listObject.length > 0){
										 angular.element('#futureServicesModal').modal('show');
									}
									//console.log("Future service List",$scope.futureServiceList);
								}

							});
		}
		
		var getMedicationIssueListByAdmissionId = function(admissionId)
		{
			self.model.medSummary=0;
			var param = {admissionId:admissionId,unitId:$scope.unitId};
			var URI =  BASE_URL + ROOT_URL +"/api/pharmacy/ip/pending/issues/get/drug/items";
			BillingGenericService.serviceAction(METHOD_POST,URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.status == "200"){
					$scope.medicationIssueList = response.data;
					var list=[];
					if($scope.isBillManipulationAllowed==false)
						{
						angular.forEach($scope.medicationIssueList,function(value,index){
							if(value.isDummyService==false)
								{
								list.push(value);
								}
						})
						$scope.medicationIssueList=list;
						}
				}
			});	
		}
		
		
		$scope.applyPatientClassPopup = function(){
			
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
				
				growl.warning("Please select reason..!!",{
					title : WARNING_MSG
				});
				return false;
				
			}else{
				
				$rootScope.startLoader();0
				$scope.patientClassIdPopup = $('#patientClassIdPopup').val();
				$scope.patientClassCancelReasonIdPopup = $('#patientClassCancelReasonIdPopup').val();
				 $scope.admissionDetails.patientClassId =$scope.patientClassIdPopup;
				 
				var obj={
						visitId:$scope.admissionDetails.admissionId,
						visitTypeId : $scope.visitTypeId,
						reasonIdForChangePatientClass : $scope.patientClassCancelReasonIdPopup,
						organizationId : $scope.organizationId,
						unitId : $scope.unitId,
						patientClassId:$scope.admissionDetails.patientClassId
				}
				var URI = BASE_URL + ROOT_URL + "/api/billing/changePatientClassId";
				BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
						{	
							
							if(response.data.status == "success")
							{
								
								$scope.needToUpdatePharmacyService = true;
								angular.element('#change_patient_class').modal('hide');
								$rootScope.stopLoader();
								
								var i = 1;
					    		angular.forEach($scope.autoRenderedServicesFinalList, function (service, key) 
									{
					    					
					    						$scope.autoRenderedServicesFinalList[key].concession = parseFloat(0);
					   						    $scope.autoRenderedServicesFinalList[key].patientClassPercentage = parseFloat(0);
					   						   // $scope.autoRenderedServicesFinalList[key].discount = 0;
					   						   $scope.autoRenderedServicesFinalList[key].discount = service.discount;
					   						    
						    			        var totalAmt = (parseFloat(service.quantity) * parseFloat(service.basePrice)).toFixed(2);
						    			       
						    			       // $scope.autoRenderedServicesFinalList[key].discountPercentage = 0;
						    			        $scope.autoRenderedServicesFinalList[key].discountPercentage = service.discountPercentage;
						    			        var withConDic =parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[key].discount);
						    			        
						    			    	var txpre = 0;
												if(service.taxPercentage != null || service.taxPercentage > 0){
													$scope.autoRenderedServicesFinalList[key].taxPercentage = service.taxPercentage;
													txpre = service.taxPercentage;
												}else{
														$scope.autoRenderedServicesFinalList[key].taxPercentage = 0;
														}
												$scope.autoRenderedServicesFinalList[key].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
												$scope.autoRenderedServicesFinalList[key].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[key].taxAmt)).toFixed(2);
												$scope.autoRenderedServicesFinalList[key].selfPayable = $scope.autoRenderedServicesFinalList[key].netPay;
												$scope.autoRenderedServicesFinalList[key].creditPayable =parseFloat(0);
					    					
										    
											
											if(i == $scope.autoRenderedServicesFinalList.length){
												//$scope.saveOrderPackage();
												if($scope.requestStatusObject == null){
													$scope.saveOrderPackage();
													$scope.requestDiscountDiv = false;
													$scope.requestApporivedDiscountDiv = false;
													$scope.discountDisabledFlagForRequest = false;
													$scope.requestAppUser = 0;
													$scope.approvedAmtApp = 0;
													$scope.requestAmtApp =0;
													$scope.discTypeDesc = "";
													$scope.statusApp = "";
													$scope.discountRadio = 2; 
													
													$scope.billWiseDiscountObj = {
															// "discountGivenBy":"",
															"discountGivenBy":$scope.userId,
															"discountCategoryId" : "",
															"discountAmountBillWise" : "",
															"discountPercentageBillWise" : "",
															"approvedAmountBillWise":"",
															"requestUserId":"",
															"requestAmountBillWise" : 0,
															"remark":''
													}
													 $scope.requestStatusObject  = null;
												}else{
															$scope.getCancelDiscountReqForDiscount();
															$scope.saveOrderPackage();
													
												}
											}
											i++;
									});
					    		
							}
						});
				
			}
		}
		$scope.checkAllTrue = function() {
			var futureServiceLength = $scope.futureServiceList.length;
			$scope.servicesCount=0;
			angular.forEach($scope.futureServiceList,function(value,index){
				if(value.checkOption == 'Y'){
					$scope.servicesCount++;
				}
			});
			if($scope.servicesCount == futureServiceLength){
				$scope.selectAllPending = 'Y';
			}else{
				$scope.selectAllPending = 'N';
			}
		};
		$scope.selectAllPendingServices = function() {

				if($scope.selectAllPending=='Y'){
					angular.forEach($scope.futureServiceList,function(value,index){
						value.checkOption = 'Y';
					});
				}
	
			
		}
		$scope.saveFutureServicesEncounter = function()
		{
			$scope.encounterDetailsFuture={};
			var listServiceMasterDto=[];
			
			angular.forEach($scope.futureServiceList,function(value,index)
			{
				if(value.checkOption=='Y')
				{
					listServiceMasterDto.push(value); 
				}
			  });
			 if(Object.keys(listServiceMasterDto).length >0)
			 {
				 angular.element('#futureServicesModal').modal('hide');
				 
				 //var encounterDetailsFuture = angular.copy($scope.encounterDetailsFuture);
				 
				 
				 $scope.encounterDetailsFuture.billingBedCategoryId = $scope.admissionDetails.bedBillingCategoryId ;
				 $scope.encounterDetailsFuture.visitTypeId= $scope.admissionDetails.visitTypeId ;
				 $scope.encounterDetailsFuture.patientCategoryId=$scope.admissionDetails.patientCategoryId;
				 $scope.encounterDetailsFuture.contractId=$scope.admissionDetails.contractId;
				 $scope.encounterDetailsFuture.admissionId=$scope.admissionDetails.admissionId;
				 $scope.encounterDetailsFuture.patientId=$scope.admissionDetails.patientId;
				 $scope.encounterDetailsFuture.payeeId = $scope.selfPayeeId
				 $scope.encounterDetailsFuture.defaultSelfTariffId = $scope.defaultSelfTariffId;
				 $scope.encounterDetailsFuture.listServiceMasterDto = listServiceMasterDto;
				 $scope.encounterDetailsFuture.organizationId  = $scope.organizationId;
				 $scope.encounterDetailsFuture.unitId  = $scope.unitId;
				 $scope.encounterDetailsFuture.referralId = $scope.admissionDetails.referralId;
				 $scope.encounterDetailsFuture.paymentEntitlementId = $scope.admissionDetails.paymentEntitlementId
				 $scope.encounterDetailsFuture.wardId = $scope.admissionDetails.wardId;
				 $scope.encounterDetailsFuture.roomId = $scope.admissionDetails.roomId;
				 $scope.encounterDetailsFuture.bedId = $scope.admissionDetails.bedId;
				 $scope.encounterDetailsFuture.dischargeStatusId=$scope.dischargeStatusId;
				 var URI = BASE_URL + ROOT_URL + "/api/opd/encounter/future/services";
					BillingGenericService.serviceAction(METHOD_POST,URI, $scope.encounterDetailsFuture, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						if(response.data.status == "success"){
							growl.success("Service order placed successfully..!!",{
								title : 'success'
							});
							getOrderedServices();
							 
								return
						}
					});
					
			 }else{
				 growl.warning("Please select at least one serivce..!!",{
						title : WARNING_MSG
					});
					return;
			 }
			
//			//console.log("accepted Services",$scope.appointmentMasterObj.listServiceMasterDto);
		}
		
		$scope.checkPackageAmountConsumption = function(){
			
	    	var pacAmount = 0;
			var allpackSerivceAmount = 0;
			angular.forEach($scope.listPackageOrder, function (value, key) {
				pacAmount = value.packagePrice;
				
				if( value.packageTypeId == 4 ){
					
					var param = {
							admissionId:$scope.admissionDetails.admissionId,
							pharmacyServiceId:$scope.pharmacyServiceId,
							packageId :  value.packageId 
						
					};//visit type IP default
					var URI = BASE_URL+ ROOT_URL
					+ "/api/billing/getAllInclusivePackageConsumeAmount";

					GenericService.serviceAction("POST", URI,param).then(function(response) {
							
						$rootScope.stopLoader();
								
								if(response.data.object != null){
									
									var amount = response.data.object.packageAmount;
									
									if( amount != null && amount > pacAmount  ){
										
										var sub = amount - pacAmount;
										$scope.subAmount =  parseFloat(sub).toFixed(2);
					                    growl.warning('Pharmacy bill amount getting exceeded with '+$scope.subAmount+' amount than package amount ',{
					                        title : 'Warning!'
					                    });
									}
								
								}
								
							});
					$rootScope.stopLoader();
				}
				
			
	    	});
			
					
					
					
			
		}
		
		
		$scope.getListPackageOrder = function() {
			if($scope.admissionDetails !== undefined && $scope.admissionDetails !== null){
                if($scope.admissionDetails.patientId === undefined || $scope.admissionDetails.patientId === null || $scope.admissionDetails.patientId ===''){
                    $scope.admissionDetails.patientId = 0;
                }
            }
            else{
                $scope.admissionDetails.patientId = 0;
            }
			var URI = BASE_URL+ ROOT_URL
					+ "/api/packages/getPackageListForIPDPackageOrder/"+$scope.admissionDetails.patientId+"/"+ $scope.admissionDetails.admissionId
					+ "/" + $scope.organizationId + "/"+ $scope.unitId+"/"+$scope.visitTypeId;

			GenericService.serviceAction("GET", URI).then(function(response) {
								$rootScope.stopLoader();
								$scope.listPackageOrder = [];
								$scope.listPackageOrder = response.data.listObject !=null ? response.data.listObject : []  ;
								
								angular.forEach($scope.listPackageOrder, function (value, key) 
										{
											value.packageApplicableDateDt = new Date(value.packageApplicableDateDt);
											
										});
								//console.log("ListPackageOrderresult",$scope.listPackageOrder);
								//$scope.checkPackageAmountConsumption();
							});
		}
		
		$scope.closeSplitBill = function()
		{
			$scope.applyAuth==false;
			$scope.autoRenderedServicesFinalList = $scope.serviceListOLD;
		}
		$scope.ServiceCount=0;		
		var getOrderedServices = function()
		{
			
			$rootScope.startLoader();
			var param = {
					encounterId:$scope.admissionDetails.admissionId,
					visitTypeId:$scope.visitTypeId,
					orgId:$scope.organizationId,
					unitId:$scope.unitId,
					isBillManipulationAllowed : $scope.isBillManipulationAllowed
					};//visit type IP default
			var URI = BASE_URL + ROOT_URL + "/api/billing/serviceMaster/orderedServices";
			BillingGenericService.serviceAction(METHOD_POST,URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == "success"){
					$scope.ServiceCount=0;
					var orderDetailsIdList =[];
					angular.forEach(response.data.listObject,function(value,index){
	                    if(value.discount==undefined || value.discount==null || value.discount==0)
                        {
                        value.discountCategoryId=null;
                        }
//						if(value.isPackageService == 'N'){
							$scope.ServiceCount=$scope.ServiceCount + value.quantity;
//							}
                        if(value.serviceUpdateforPackage != null && value.serviceUpdateforPackage == 1){
                               orderDetailsIdList.push(value.orderDetailsId);
                        }
					});
					$scope.autoRenderedServicesList = response.data.listObject;
					var indexT = response.data.listObject.findIndex(record => record.serviceMasterId === $scope.surchargeServiceId);
					
				    if (indexT >= 0) {
				    	$scope.isPatientClass = "Y";
				    }
				    
					$scope.calculationTest($scope.autoRenderedServicesList,init);
					$scope.setThirdTabLevel(1);
					if($scope.applyAuth==true)
						$scope.showSplitBill();
					  
					$scope.applyAuth = false;
					$scope.loadingServiceFlag = false;
					  angular.element('#addServicePopup').modal('hide');
					  angular.element('#serviceCancellationModal').modal('hide');
					  angular.element('#packageForceAdd').modal('hide');
					  $rootScope.stopLoader();
					  $rootScope.stopLoaderPopup();
					  $scope.loadDataForFilterDummyServices();

					  if(orderDetailsIdList.length>0){
					    $scope.updateOrderdetailsList(orderDetailsIdList);
					  }

		
				}
				else if(response.data.status == "error")
				{
					 $rootScope.stopLoader();
					 $rootScope.stopLoaderPopup();
					}
			});	
			$scope.getListPackageOrder();
		}

		$scope.loadDataForFilterDummyServices = function(){
			 $scope.initialAutoRenderedServicesForDummyServices = [];
			 $scope.initialAutoRenderedServicesForDummyServices=angular.copy($scope.autoRenderedServicesFinalList);
		}


	$scope.updateOrderdetailsList = function(orderDetailsIdList){
	$rootScope.startLoader();
		var indexList = [];
		  $scope.autoRenderedServicesForSaveAndUpdate=[];

		angular.forEach(orderDetailsIdList,function(value,index){
            angular.forEach( $scope.autoRenderedServicesFinalList,function(value1,index1){

                        if(value == value1.orderDetailsId){
                            indexList.push(index1);
                            $scope.autoRenderedServicesForSaveAndUpdate.push(value1);
                        }

                    });
		});
            if(indexList.length>0){
              $scope.addOrUpdateServices(null,indexList);
               $rootScope.stopLoader();
              $timeout(function () {
              				 getOrderedServices();

              	}, 6000);

            }

		}
		
		//get groupWise Summary
		var groupWiseSummary = function(){
			
			if($scope.autoRenderedServicesFinalList.length > 0){
				var groups = [];
				var groupIds = [];
				$scope.totalGrpoupAmt = parseFloat(0);
				$scope.totalConcessionAmt = parseFloat(0);
				$scope.totalDiscountAmt = parseFloat(0);
				$scope.totalGstAmt = parseFloat(0);
				$scope.totalNetPayaAmt = parseFloat(0);
				$scope.autoRenderedServicesFinalList.forEach(function(item,index){
					
						
						
						if(item.isPackageService == 'N')
						{
					     if(groupIds.indexOf(item.serviceSpecialityId) == -1 && item.serviceSpecialityId != null){
						 groupIds.push(item.serviceSpecialityId);
						 var temp = {serviceSpecialityId:item.serviceSpecialityId,specialityName:item.specialityName,serviceAmt:(item.packageConsumptionAmt != 0 ? (item.quantity*item.basePriceOld) : item.totalAmt),totalAmount:$rootScope.parseFloatNum(item.netPay),totalConcession:$rootScope.parseFloatNum(item.concession),
									totalDiscount:$rootScope.parseFloatNum(item.discount),totalGst:$rootScope.parseFloatNum(item.taxAmt),totalNetPay:$rootScope.parseFloatNum(item.netPay)}
						groups.push(temp);
						 var tmpUnitService = parseFloat(0);
						  tmpUnitService = (item.packageConsumptionAmt != 0 ? (item.quantity*item.basePriceOld) : item.totalAmt);
						 $scope.totalGrpoupAmt = (parseFloat($scope.totalGrpoupAmt) + parseFloat(tmpUnitService)).toFixed(2);
						 
						 
						 $scope.totalConcessionAmt = (parseFloat($scope.totalConcessionAmt) + parseFloat(item.concession)).toFixed(2);
						 
						 $scope.totalDiscountAmt = (parseFloat($scope.totalDiscountAmt) + parseFloat(item.discount)).toFixed(2);
						 
						 $scope.totalGstAmt = (parseFloat($scope.totalGstAmt) + parseFloat(item.taxAmt)).toFixed(2);
						 
						 $scope.totalNetPayaAmt = (parseFloat($scope.totalNetPayaAmt) + parseFloat(item.netPay)).toFixed(2);
						 
						 
					}else{
						let obj = groups.find((elem,i) => {
							if(elem.serviceSpecialityId == item.serviceSpecialityId)
							{
								elem.serviceAmt = parseFloat(elem.serviceAmt) + parseFloat(item.packageConsumptionAmt != 0 ? (item.quantity*item.basePriceOld) : item.totalAmt);
								elem.totalAmount = parseFloat(elem.totalAmount) + parseFloat(item.netPay);
								elem.totalConcession = parseFloat(elem.totalConcession) + parseFloat(item.concession);
								elem.totalDiscount = parseFloat(elem.totalDiscount) + parseFloat(item.discount);
								elem.totalGst = parseFloat(elem.totalGst) + parseFloat(item.taxAmt);
								elem.totalNetPay = parseFloat(elem.totalNetPay) + parseFloat(item.netPay);
								groups[i] = elem;
								
								
								var tmpUnitService = parseFloat(0);
								  tmpUnitService = (item.packageConsumptionAmt != 0 ? (item.quantity*item.basePriceOld) : item.totalAmt);
								 $scope.totalGrpoupAmt = (parseFloat($scope.totalGrpoupAmt) + parseFloat(tmpUnitService)).toFixed(2);
								 
								 $scope.totalConcessionAmt = (parseFloat($scope.totalConcessionAmt) + parseFloat(item.concession)).toFixed(2);
								 
								 $scope.totalDiscountAmt = (parseFloat($scope.totalDiscountAmt) + parseFloat(item.discount)).toFixed(2);
								 
								 $scope.totalGstAmt = (parseFloat($scope.totalGstAmt) + parseFloat(item.taxAmt)).toFixed(2);
								 
								 $scope.totalNetPayaAmt = (parseFloat($scope.totalNetPayaAmt) + parseFloat(item.netPay)).toFixed(2);
								 
								 
								return true;
							}
							});
					}
					}
				
				});
				self.model.groups = groups;
			}
			
			
		}
		$scope.AutoRenderednetTotalTaxCalculations=0;
		
		   $scope.checkServiceWiseDoctorsList = function(info, idx){
			   
			   var docListObj={
					   serviceId : info.serviceMasterId,
				   		unitId : $scope.unitId,
				   	  serviceDepartmentId : info.serviceSpecialityId,
				   		organizationId : $scope.organizationId,
				   		billingBedCategoryId : $scope.admissionDetails.bedBillingCategoryId,
				   		visitTypeId : $scope.visitTypeId,
				   		patientTypeId : $scope.admissionDetails.patientCategoryId,
				   		paymentEntitlementId:$scope.admissionDetails.paymentEntitlementId
				   }
				   var URI = BASE_URL + ROOT_URL + "/adt/getDoctorShareWiseDocList";// for
				   																	// HIS-15355

				   GenericService
				   .serviceAction("POST", URI, docListObj)
				   .then(
				   		function(response) {
				   			$scope.doctorsListNew = [];
				   			if (response.data.status == "success") {
				   				$scope.doctorsListNew = response.data.listObject;
				   				angular.forEach($scope.autoRenderedServicesFinalList, function(value, key){
				   				   if(key == idx){
				   					value.docList = $scope.doctorsListNew;
				   				   }
				   			   });
				   			}
				   		});
		   }


		   		   $scope.checkServiceWiseDoctorsListPop = function(info, idx){

           			   var docListObj={
           					   serviceId : info.serviceMasterId,
           				   		unitId : $scope.unitId,
           				   	    serviceDepartmentId : info.serviceSpecialityId,
           				   		organizationId : $scope.organizationId,
           				   		billingBedCategoryId : $scope.admissionDetails.bedBillingCategoryId,
           				   		visitTypeId : $scope.visitTypeId,
           				   		patientTypeId : $scope.admissionDetails.patientCategoryId,
           				   		paymentEntitlementId:$scope.admissionDetails.paymentEntitlementId
           				   }
           				   var URI = BASE_URL + ROOT_URL + "/adt/getDoctorShareWiseDocList";// for
           				   																	// HIS-15355

           				   GenericService
           				   .serviceAction("POST", URI, docListObj)
           				   .then(
           				   		function(response) {
           				   			$scope.doctorsListNewPop = [];
           				   			if (response.data.status == "success") {
           				   				$scope.doctorsListNewPop = response.data.listObject;
           				   				angular.forEach($scope.servicesListPopup, function(value, key){
           				   				   if(key == idx){
           				   					value.docList = $scope.doctorsListNewPop;
           				   				   }
           				   			   });
           				   			}
           				   		});
           		   }
           		  // $scope.checkServiceWiseDoctorsListPop();

           		  $scope.checkServiceWiseDoctorsListPack = function(info, idx,parentIndex){

                             			   var docListObj={
                             					   serviceId : info.serviceMasterId,
                             				   		unitId : $scope.unitId,
                             				   	    serviceDepartmentId : info.serviceSpecialityId,
                             				   		organizationId : $scope.organizationId,
                             				   		billingBedCategoryId : $scope.admissionDetails.bedBillingCategoryId,
                             				   		visitTypeId : $scope.visitTypeId,
                             				   		patientTypeId : $scope.admissionDetails.patientCategoryId,
                             				   		paymentEntitlementId:$scope.admissionDetails.paymentEntitlementId
                             				   }
                             				   var URI = BASE_URL + ROOT_URL + "/adt/getDoctorShareWiseDocList";// for
                             				   																	// HIS-15355

                             				   GenericService
                             				   .serviceAction("POST", URI, docListObj)
                             				   .then(
                             				   		function(response) {
                             				   			$scope.doctorsListNewPack = [];
                             				   			if (response.data.status == "success") {
                             				   				$scope.doctorsListNewPack = response.data.listObject;
                             				   				angular.forEach($scope.selectedPackageList[parentIndex].packageServiceList, function(value, key){
                             				   				   if(key == idx){
                             				   					value.docList = $scope.doctorsListNewPack;
                             				   				   }
                             				   			   });
                             				   			}
                             				   		});
                             		   }
		   
		   $scope.getNewDoctorList=function(){
		    	  var obj={
		    			  unitId : $scope.unitId,
					   		organizationId : $scope.organizationId,
		    	  }
				    	  var URI = BASE_URL + ROOT_URL + "/adt/getDoctorListForOPDBilling";// for
							// HIS-15355
				
				GenericService
				.serviceAction("POST", URI, obj)
				.then(
				function(response) {
				$scope.doctorsList = [];
				
				if (response.data.status == "success") {
				$scope.doctorsList = response.data.listObject;
				
				}
				});
		      }
		   
		   $scope.closePop = function(){
			   $('#packageAmountexceeded').modal('hide');
		   }
		   $scope.subAmount = 0;
		 $scope.calculationTest = function(autoRenderedServicesList,typeId)
		    {
		    	//console.log("AUTO","Kaustubh");
		    	$scope.AutoRenderedTotalAmountSummary = 0;
				$scope.AutoRenderedTotalConcessionSummary = 0;
				$scope.AutoRenderednetPayableSummary = 0;
				$scope.AutoRenderednetTotalDiscountSummary=0;
				$scope.AutoRenderednetTotalTax=0;
				$scope.AutoRenderednetTotalDiscount=0;
				$scope.AutoRenderednetTotalCreditPayble=0;
				$scope.AutoRenderednetTotalSelfPayble=0;
				$scope.AutoRenderednetTotalPackagePayble=0;
				$scope.totalSelfPaybleTemp = 0;
				$scope.totalSelfPayble = 0;
				$scope.roundOffAmt = 0;
		        $scope.totalDisServiceWise = 0;
		    	$scope.totalPackagePaybleTemp = 0;

				//-----calculate amounts
				////console.log(autoRenderedServicesList);
				//return false;
		    	
		    	
				$scope.autoRenderedServicesFinalList = [];
                var mainCount = 1;
                var serviceCount = autoRenderedServicesList.length;

                if(autoRenderedServicesList.length == 0){
                    $scope.getDisRiqFlag = false;
                    $scope.requestStatusObject = null;
               }   
                
				angular.forEach(autoRenderedServicesList, function (value, key) {
					var newArr = {};
					angular.forEach(value.orderDetailsPackageMapperDtosList, function (value1, key) 
							{
						
							if(value1.serviceMasterId != null){
								$scope.checkServiceWiseDoctorsList(value1,key);
							}
						
							value1.serviceAmount = parseFloat(value1.serviceAmount).toFixed(2);
							newArr.bedCategoryId = value.bedCategoryId;
							newArr.wardId = value.wardId;
							newArr.roomId = value.roomId;
							newArr.bedId = value.bedId;
							newArr.medallServiceId = value.medallServiceId;
							if(value.orderDetailsId > 0)
							{
								newArr.orderDetailsId = value.orderDetailsId;
							}
							else
							{
								newArr.orderDetailsId = null;
							}
							newArr.tariffId = value.tariffId > 0 ? value.tariffId : $scope.defaultSelfTariffId;
							if(value.orderId > 0)
							{
								newArr.orderId = value.orderId;
							}
							else
							{
								newArr.orderId = null;
							}
							
							if(value.orderDate!=null){
								newArr.orderDate  = value.orderDate;
							}else if(value.createdDate!=null){
								newArr.orderDate  = value.createdDate;
							}else{
								
							}
							if(value.orderNo != null)
							{
								newArr.orderNo  = value.orderNo;
							}
							else
							{
								newArr.orderNo  = '';
							}
							
							newArr.dummyServiceAmt = value.dummyServiceAmt;
							newArr.priorityId  = value.priorityId;
							newArr.isOutsourced = value.isOutsourced;
							newArr.specialityId = value.specialityId;
							newArr.taxId = value.taxId;
                            newArr.procedureOrderMstId = value.procedureOrderMstId > 0 ? value.procedureOrderMstId  : null;
                            newArr.isProcedureService = value.isProcedureService!=null ? value.isProcedureService  : 'N';
                            newArr.isPostedWhileDischarge = value.isPostedWhileDischarge!=null ? value.isPostedWhileDischarge : 'N';
                            newArr.isChargedHalfDay = value.isChargedHalfDay;
                            newArr.isFromAddservice = value.isFromAddservice!=null ? value.isFromAddservice : 'N';
                            newArr.isDummyService = (value.isDummyService == true) ? 1 : 0;
			        		newArr.isHiddenService = (value.isHiddenService==true) ? 1 : 0;
                            newArr.isFromServiceSerach = value.isFromServiceSerach!= null ? value.isFromServiceSerach : 'N';
                            newArr.orderDateString = value.orderDateString;
                            newArr.orderDateTimeString = value.orderDateTimeString;
							newArr.orderDateTime  = value.orderDateTime;
							if(value.ordCancelled != null)
							{
								newArr.ordCancelled  = value.ordCancelled;
							}
							else
							{
								newArr.ordCancelled  = 'N';
							}
							
							if(value.ordCancelReasonId != null)
							{
								newArr.ordCancelReasonId  = value.ordCancelReasonId;
								let reason = $scope.reasonList.find((elem) => {if(elem.reasonMasterId == value.ordCancelReasonId) return true});
								value.cancelledReason = typeof(reason) != "undefined" ? reason.reasonDesc : value.cancelledReason;
							}
							else
							{
								newArr.ordCancelReasonId  = '';
							}
							
							if(value.ordCancelDatetime != null)
							{
								newArr.ordCancelDatetime  = value.ordCancelDatetime;
							}
							else
							{
								newArr.ordCancelDatetime  = '';
							}
							if(value.ordCancelRemark != null)
							{
								newArr.ordCancelRemark  = value.ordCancelRemark;
							}
							else
							{
								newArr.ordCancelRemark  = '';
							}
							
                            newArr.docSharePer = value.docSharePer !=null ? parseFloat(value.docSharePer).toFixed(2) : 0;
							newArr.hospitalShare = value.hospitalShare !=null ? parseFloat(value.hospitalShare).toFixed(2) : 0;
							newArr.refDocSharePer = value.refDocSharePer !=null ? parseFloat(value.refDocSharePer).toFixed(2) : 0;
                            newArr.discountOwnership = value.discountOwnership > 0 ? value.discountOwnership : 0;
							newArr.isOrderVisibleDepartment = value.isOrderVisibleDepartment;
							newArr.isConsultationService = value.isConsultationService;
							newArr.ordRemarks = value.ordRemarks;
							newArr.nursingServiceOrderFlag =value.nursingServiceOrderFlag;
							newArr.isInvestigationOrder = (value.isInvestigationOrder=='Y')?'Y':'N';
							newArr.patientClassPercentage = value.patientClassPercentage != null ? value.patientClassPercentage  : 0.00;
			        		newArr.markupStatus =value.markupStatus != null ? value.markupStatus  : false;
			        		newArr.packageApplicableDate = value.packageApplicableDate;
			        		newArr.packageApplicableDateDisplay = value.packageApplicableDateDisplay;
			        		newArr.packageApplicableDateDisplayNew = value.packageApplicableDateDisplayNew;
			        		if(newArr.packageApplicableDateDisplay!=undefined && newArr.packageApplicableDateDisplay!=null)
			        			{
			        		var spliteDate=newArr.packageApplicableDateDisplay.split("/");
			        		var mydate = new Date(spliteDate[2]+"-"+spliteDate[1]+"-"+spliteDate[0]);
			        		newArr.packageApplicableDateDisplayString=mydate;
			        			}
							newArr.status  = value.status;
							if(value.serviceRendered != null)
							{
								newArr.serviceRendered  = value.serviceRendered;
							}
							else
								{
									newArr.serviceRendered  = '0';
								}
							newArr.isAutoRender =  value.isAutoRender;
							if(value.oldOrdDtlId != null)
							{
								newArr.oldOrdDtlId  = value.oldOrdDtlId;
							}
							else
							{
								newArr.oldOrdDtlId  = null;
							}
							newArr.isRateEditable = ((value.packageConsumptionAmt>0 && newArr.isRateEditable=='Y') || value.serviceMasterId==$scope.pharmacyServiceId )?'N':value.isRateEditable;
							//newArr.isRateEditable = value.isPackage == true?'N':newArr.isRateEditable;
                            newArr.discountCategoryId = value.discountCategoryId != null ? value.discountCategoryId.toString()  : null;
							newArr.doctorSpecialityId = ((value.doctorSpecialityId != null || value.doctorSpecialityId > 0) ? value.doctorSpecialityId  :  value.specialityId);
							newArr.docList= $scope.doctorsList != null && $scope.doctorsList.length > 0 ? $scope.doctorsList : $scope.docList ;
							newArr.doctorSpecialityList = (value.doctorSpecialityList != undefined ? value.doctorSpecialityList : []);
							
							var tepmSpecialityArray = [];
							if(value.doctorId !=undefined && value.doctorId !=null &&value.doctorId > 0){
								angular.forEach($scope.getInitiallySpecialityList, function (speciality, key) {
									if(speciality.doctorId==value.doctorId)
										{
										tepmSpecialityArray.push(speciality);
										}
								});
								if(value.doctorSpecialityId == '' || value.doctorSpecialityId == null || value.doctorSpecialityId == undefined)
								{
									newArr.doctorSpecialityId = tepmSpecialityArray[0].specialityId;
								}
								if(value.specialityName == '' || value.specialityName == null || value.specialityName == undefined)
								{
									newArr.specialityName = tepmSpecialityArray[0].specialityName;
								}
								newArr.doctorSpecialityList=tepmSpecialityArray;
							}
							
							
							if(value.isRefDoctorShare != null)
							{
								newArr.isRefDoctorShare  = value.isRefDoctorShare;
							}
							else
							{
								newArr.isRefDoctorShare  = null;
							}
							
							if(value.ordRenderDatetime == null || value.ordRenderDatetime == undefined)
								{
								   newArr.ordRenderDatetime  = null;
								}
							else
								{
								  newArr.ordRenderDatetime  = value.ordRenderDatetime;
								}
                            if(value.isDoctorRequired == null || value.isDoctorRequired == undefined)
                            {
                               newArr.isDoctorRequired  = 'N';
                            }
                        else
                            {
                              newArr.isDoctorRequired  = value.isDoctorRequired;
                            }
                            newArr.isOrderVisibleDepartment = value.isOrderVisibleDepartment;
                            newArr.isConsultationService = value.isConsultationService;
                            newArr.ordRemarks = value.ordRemarks;
							newArr.medallServiceId = value.medallServiceId;

                            newArr.nursingServiceOrderFlag =value.nursingServiceOrderFlag;
							newArr.doctorId = value.doctorId;
							newArr.contractId = value.contractId;//Ganesh Change
							newArr.isAutoRender = value.isAutoRender;//Ganesh Change
							newArr.serviceSpecialityId = value.serviceSpecialityId;
							newArr.serviceSubSpecialityId = value.serviceSubSpecialityId;
							newArr.serviceRendered = value.serviceRendered;
							newArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
							newArr.oldNetPay = value.oldNetPay;
							newArr.discountDetailsList = value.discountDetailsList; 
							newArr.serviceMasterId = value.serviceMasterId;
							newArr.serviceStandardCode = value.serviceStandardCode;
							newArr.serviceStandardName = value.serviceStandardName;
                            newArr.userName = value.userName;
                            newArr.orderDateTime = value.orderDateTime;
                            newArr.createdBy = value.createdBy;
                            newArr.totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
                            newArr.isDiscount = value.isDiscountApplicable;
                            newArr.isDiscountApplicable = (value.packageConsumptionAmt!=0) ? 'N':value.isDiscountApplicable; // isDiscountApplicable
                            if(value.serviceMasterId == $scope.surchargeServiceId){
                            	newArr.isDiscountApplicable = 'N';
                            }
							if( value.packageConsumptionAmt!=0 || value.serviceMasterId==$scope.pharmacyServiceId)
							{
								newArr.isRateEditable = 'N';
								newArr.isQuantityEditable = 'N';
							}else{
								newArr.isRateEditable = newArr.isRateEditable;
								newArr.isQuantityEditable = value.isQuantityEditable;
							}
							
							newArr.isTariffRateApplicable = value.isTariffRateApplicable;
							newArr.minRateEditable = value.minRateEditable;
							newArr.maxRateEditable = value.maxRateEditable;
							newArr.specialityName = value.specialityName;
							newArr.doctorName = value.doctorName;
							newArr.discountDisabledFlag = 'N';
							newArr.isPackage = value.isPackage;
							newArr.packageMasterId = typeof(value.packageId) != "undefined" ? value.packageId : value.packageMasterId;
							newArr.packageTypeId = value.packageTypeId;
							newArr.isPackageService = value.isPackageService;
							newArr.apportionedPrice = typeof(value.apportionedPrice) != "undefined" ? value.apportionedPrice : null;
							newArr.isCapService = value.isCapService;
			    			newArr.packageConsumptionAmt = value.packageConsumptionAmt;
			    			
			    			if(value.subSpecialityId==undefined||value.subSpecialityId==null)
		    				{
		    				  newArr.subSpecialityId = value.serviceRenderingDeptId;
		    				}
		    			   else{
		    			     newArr.subSpecialityId = value.subSpecialityId;
		    			     }
			    			
			    			newArr.packageId = value.packageId;
			    			newArr.cancelledReason = value.cancelledReason;
							if(value.specialityName == null)
							{
								newArr.specialityName = "";
								newArr.doctorName = "";
							}
							newArr.quantity = value.quantity;
							newArr.basePrice = parseFloat(value.basePrice).toFixed(2);
							newArr.basePriceOld = value.basePrice;
							
							newArr.basePriceRateChange = parseFloat(value.basePrice).toFixed(2);
							if(parseFloat(value.packageConsumptionAmt) > 0 && value1.isPackageService == 'N')
							{
								newArr.basePriceRateChange = parseFloat(value1.serviceAmount).toFixed(2);
							}
							newArr.oldBasePrice = value.oldBasePrice;
							newArr.concession = parseFloat(value.concession).toFixed(2);
							
							newArr.patientClassPercentage = isNaN(((parseFloat(value.concession)/(parseFloat(value.quantity) * parseFloat(value1.serviceAmount)))*100).toFixed(2)) ? parseFloat(0).toFixed(2) : ((parseFloat(value.concession)/(parseFloat(value.quantity) * parseFloat(value1.serviceAmount)))*100).toFixed(2);
							
							//newArr.discountCategoryId = '';
							newArr.discountPercentage = isNaN(((parseFloat(value.discount)/(parseFloat(value.quantity) * parseFloat(value.basePrice) - parseFloat(value.concession)))*100).toFixed(2)) ? parseFloat(0).toFixed(2) : ((parseFloat(value.discount)/(parseFloat(value.quantity) * parseFloat(value.basePrice) - parseFloat(value.concession)))*100).toFixed(2);
							newArr.patClsPerPriceRateChange = parseFloat(newArr.patientClassPercentage).toFixed(2);
							newArr.isPeriodicityService = value.periodicityServiceOrderId!=null ? 'Y' : 'N' ;
							newArr.discount = value.discount;
							newArr.checked = value.checked;
							var dis = parseFloat(value.discount).toFixed(2);
							var disPer =newArr.discountPercentage;
							//alert($scope.discountDetailsList.length);
							if(value.discount == null || value.discount == 0 )
								{
									newArr.discountPercentage = 0;
									newArr.discount = 0;
									dis = 0;
									disPer = 0;
									newArr.discountDisabledFlag = 'Y';
									
								}

							
								if(value.discountDetailsList == null)
								{
									value.discountDetailsList = [];
									newArr.discountDetailsList = [];
								}
							
							
							newArr.taxName = value.taxName;
							var txpre = 0;
							if(value.taxPercentage != null || value.taxPercentage > 0){
								newArr.taxPercentage = value.taxPercentage;
								txpre = value.taxPercentage;
							}
							else
								{
								 newArr.taxPercentage = 0;
								}
							
							var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
							totalAmt  = parseFloat(totalAmt) - value.packageConsumptionAmt;
							var concession = (parseFloat(value.concession).toFixed(2));
		                    if(newArr.isDiscountApplicable == 'Y' && value1.isPackageService == 'N'  && value.status == 'A')
	                        {
	                         //$scope.totalDisServiceWise  = parseFloat($scope.totalDisServiceWise) + ((parseFloat(value.quantity) * parseFloat(value.basePrice)) - parseFloat(concession));
		                    	$scope.totalDisServiceWise  = parseFloat($scope.totalDisServiceWise) + ((parseFloat(value.quantity) * parseFloat(value.basePrice)));
	                        }

							
							var withConDic =parseFloat(totalAmt - (parseFloat(concession) + parseFloat(dis))).toFixed(2);
							newArr.taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
							var gstAmt =  parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
							newArr.taxAmtOld = value.taxAmtOld;
							newArr.selfPayableOldC = value.selfPayableOldC;
							newArr.creditPayableOldC = value.creditPayableOldC;
							newArr.netPay =   parseFloat(value.netPay).toFixed(2);
							var selfPayableV =   "0";
							var creditPayableV =   "0";
							
						      
						      if(value.payeeId == $scope.selfPayeeId || value.payeeId == null )
						       {	
						    	  newArr.selfPayable =parseFloat(value.selfPayable).toFixed(2);;
							      newArr.creditPayable =parseFloat(value.creditPayable).toFixed(2);;
							      selfPayableV =  parseFloat(newArr.selfPayable).toFixed(2);
							      creditPayableV =  parseFloat(newArr.creditPayable).toFixed(2);
						       }
						      else
						       {
						       newArr.payeeId= value.payeeId;
						       
						       if(value.selfPayable>0)
								{
							    	
							    	if(parseInt(dis) > 0 )
							    		{
							    		newArr.selfPayable =   parseFloat(value.selfPayable).toFixed(2);
							            selfPayableV =   parseFloat(value.selfPayable).toFixed(2);
							    		}
							    	else
							    		{
							    		newArr.selfPayable =parseFloat(value.selfPayableOldC).toFixed(2);
										 selfPayableV =   parseFloat(value.selfPayableOldC).toFixed(2);
							    		}
							    
							    	newArr.creditPayable =parseFloat(value.creditPayable).toFixed(2);
									creditPayableV=parseFloat(value.creditPayable).toFixed(2);
								}
							else{
								newArr.selfPayable = (value.selfPayable==null||value.selfPayable==undefined||value.selfPayable=="")?0:value.selfPayable;
								selfPayableV =   parseFloat(value.selfPayable).toFixed(2);
							   
							    newArr.creditPayable  = value.creditPayable;
							    creditPayableV = parseFloat(value.creditPayable).toFixed(2);
							    
							   }
						       }
						    
							
							
						     if($scope.surchargeServiceId !=  value.serviceMasterId){
						     if(Object.keys(value.orderDetailsPayeeMapperDtosList).length > 0)
						    	 {
						    		angular.forEach(value.orderDetailsPayeeMapperDtosList, function (value1, key) {
										
						    			 if(value1.payeeId == $scope.selfPayeeId)
									       {
						    				 value1.payeeAmount =  newArr.selfPayable;
									       }
										
									});
						    	 }
						     }
						     
						      
						    newArr.saleId = value.saleId;
						    newArr.orderDetailsPayeeMapperDtosList = value.orderDetailsPayeeMapperDtosList;
						    newArr.orderDetailsPackageMapperDtosList = value.orderDetailsPackageMapperDtosList;
						    
						    //package force add
						    newArr.forceAddRemoved = value.forceAddRemoved;
						    newArr.isConsumed = value.isConsumed;
						    
						    var checkPackageConsumtion = false;
						    
						    if(value.isPackageService =='N' && value.quantity == -1 && value1.isPackageService=='Y' && value.orderDetailsPackageMapperDtosList.length == 1){
						    	checkPackageConsumtion = true;
						    }
						    
						    var newPatAmt = parseFloat(parseFloat(newArr.netPay)).toFixed(2);
							if((checkPackageConsumtion == true || value1.isPackageService=='N') && value.status=='A')
							{
								if(	$scope.ambulanceChargesServiceId != undefined && $scope.ambulanceChargesServiceId != null  && $scope.ambulanceChargesServiceId  == value1.serviceId){
									$scope.AutoRenderedTotalAmountSummary=(parseFloat($scope.AutoRenderedTotalAmountSummary) + parseFloat(value.quantity) * parseFloat(value.netPay)).toFixed(2);
								}else{
									$scope.AutoRenderedTotalAmountSummary=(parseFloat($scope.AutoRenderedTotalAmountSummary) + parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
								}
								
								$scope.AutoRenderedTotalConcessionSummary=(parseFloat($scope.AutoRenderedTotalConcessionSummary) + (parseFloat(value.concession))).toFixed(2);
								$scope.AutoRenderednetPayableSummary=(parseFloat($scope.AutoRenderednetPayableSummary) + ( parseFloat(value.netPay))).toFixed(2);
								$scope.AutoRenderednetTotalDiscountSummary=(parseFloat($scope.AutoRenderednetTotalDiscountSummary) + parseFloat(0)).toFixed(2);
								$scope.autoRenderedServicesBillingDetails.push($scope.createBillingDetailsObj(value));
								//$scope.AutoRenderednetTotalTax = (parseFloat($scope.AutoRenderednetTotalTax) +  (value.packageConsumptionAmt==0?parseFloat(gstAmt):0)).toFixed(2);
								$scope.AutoRenderednetTotalTax = (parseFloat($scope.AutoRenderednetTotalTax) +  (parseFloat(gstAmt))).toFixed(2);
								$scope.AutoRenderednetTotalDiscount = (parseFloat($scope.AutoRenderednetTotalDiscount) + parseFloat(newArr.discount)).toFixed(2);
								$scope.AutoRenderednetTotalCreditPayble=(parseFloat($scope.AutoRenderednetTotalCreditPayble) + parseFloat(creditPayableV)).toFixed(2);
								$scope.AutoRenderednetTotalSelfPayble=(parseFloat($scope.AutoRenderednetTotalSelfPayble) + parseFloat(selfPayableV)).toFixed(2);
								$scope.AutoRenderednetTotalPackagePayble= (parseFloat($scope.AutoRenderednetTotalPackagePayble)+ parseFloat(value.packageConsumptionAmt)).toFixed(2);
							}
							newArr.createdBy = value.userId;
							newArr.createdDate = value.createdDate;
							newArr.ordRenderDatetimeString = moment(value.ordRenderDatetime).format('DD/MM/YYYY');
							newArr.orderDateStringNew = moment(value.orderDate).format('DD/MM/YYYY HH:mm');
							newArr.pharmacyDiscountOverride = value.pharmacyDiscountOverride;
						//	$scope.autoRenderedServicesFinalList.push(newArr);
							newArr.hospitalShareNew=parseFloat((newArr.netPay)-(parseFloat(newArr.docSharePer)+parseFloat(newArr.refDocSharePer)+parseFloat(newArr.taxAmt))).toFixed(2);//.toFixed(0)
							newArr.doctorForServiceList=value.doctorList != null ? value.doctorList :value.doctorForServiceList;
							});// end of loop
					
					// chk blank object by dinesh start
                	if(!angular.equals({}, newArr) ) {
                		 $scope.autoRenderedServicesFinalList.push(newArr);
                		 $scope.initialAutoRenderedServicesFinalList=$scope.autoRenderedServicesFinalList;
                     }
  //			       $scope.autoRenderedServicesFinalList.push(newArr); // before
                	// chk blank object by dinesh end
					
                    if($scope.getDisRiqFlag == true && autoRenderedServicesList.length == mainCount){
                        $scope.getDisRiqFlag = false;
                        $scope.requestStatusObject = {};
                           var data1 = {
                                   admissionId : $scope.admissionDetails.admissionId,
                                   requestType : 1
                           }
                           var URI1 = BASE_URL + ROOT_URL + "/api/billing/getDiscReqDtlsForOPBilling";
                           BillingGenericService.serviceAction(METHOD_POST,URI1, data1, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
                               {
                               if(response.data.status == "success"){
                                   if(response.data.listObject.length > 0){
                                   $scope.requestStatusObject = response.data.listObject[0];
                                   if($scope.requestStatusObject.reqStatus == 2)
                                       {
                                       $scope.requestApporivedDiscountDiv = true;
                                       $scope.discountDisabledFlagForRequest = true;
                                       $scope.requestAppUser = $scope.requestStatusObject.requestSentToEmployee;
                                       $scope.approvedAmtApp = $scope.requestStatusObject.approvedDiscAmt;
                                       $scope.requestAmtApp =$scope.requestStatusObject.reqDisAmt;
                                       $scope.statusApp = "Approved.";
                                       $scope.discTypeDesc = $scope.requestStatusObject.discTypeDesc;
                                       $scope.discountRadio = 2; 
                                       $scope.requestAmtRemark =$scope.requestStatusObject.remark;
                                       
                                       
                                       var totalDisPercentage =((parseFloat($scope.requestStatusObject.approvedDiscAmt)/$scope.requestStatusObject.netAmount)*100).toFixed(2);
                                       if(totalDisPercentage > 0){
                                       $scope.billWiseDiscountObj.discountPercentageBillWise = totalDisPercentage;
                                       $scope.billWiseDiscountObj.discountPercentageBillWiseOld = (parseFloat($scope.requestStatusObject.approvedDiscAmt)/$scope.requestStatusObject.netAmount)*100;
                                       $scope.billWiseDiscountObj.discountAmountBillWise = parseFloat($scope.requestStatusObject.approvedDiscAmt).toFixed(2);
                                       $scope.billWiseDiscountObj.discountCategoryId =  $scope.requestStatusObject.discType;
                                   	   if($scope.billWiseDiscountObj.discountAmountBillWise > 0 && $scope.totalDiscountSummary > 0){
                      				     $scope.roundOffAmt = parseFloat($scope.totalDiscountSummary-$scope.billWiseDiscountObj.discountAmountBillWise).toFixed(2);
                      				   }
                                       }
                                       }
                                   else if($scope.requestStatusObject.reqStatus == 1)
                                       {
                                           $scope.discountDisabledFlagForRequest = true;
                                           $scope.requestApporivedDiscountDiv = true;
                                           $scope.requestAppUser = $scope.requestStatusObject.requestSentToEmployee;
                                           $scope.approvedAmtApp = $scope.requestStatusObject.approvedDiscAmt;
                                           $scope.requestAmtApp =$scope.requestStatusObject.reqDisAmt;
                                           $scope.statusApp = "Pending";
                                           $scope.discTypeDesc = $scope.requestStatusObject.discTypeDesc;
                                           $scope.discountRadio = 2;
                                           $scope.requestAmtRemark =$scope.requestStatusObject.remark;
                                       }
                                   else if($scope.requestStatusObject.reqStatus == 3)
                                       {
                                        $scope.requestStatusObject  = null;
                                        
                                        /* var reject =0;
                                          angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
                                              if(value.status == 'A'){
                                                  
                                                  if(parseFloat(value.discount) > 0){
                                                      
                                                          $scope.autoRenderedServicesFinalList[reject].concession = parseFloat(0);
                                                          $scope.autoRenderedServicesFinalList[reject].patientClassPercentage = parseFloat(0);
                                                          
                                                       var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
                                                       $scope.autoRenderedServicesFinalList[reject].discountPercentage = 0;
                                                       $scope.autoRenderedServicesFinalList[reject].discount = parseFloat(0).toFixed(2);
                                                       var withConDic =parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[reject].discount);
                                                       
                                                       var txpre = 0;
                                                       if(value.taxPercentage != null || value.taxPercentage > 0){
                                                           $scope.autoRenderedServicesFinalList[reject].taxPercentage = value.taxPercentage;
                                                           txpre = value.taxPercentage;
                                                       }else{
                                                               $scope.autoRenderedServicesFinalList[reject].taxPercentage = 0;
                                                               }
                                                       $scope.autoRenderedServicesFinalList[reject].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
                                                       $scope.autoRenderedServicesFinalList[reject].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[reject].taxAmt)).toFixed(2);
                                                       $scope.autoRenderedServicesFinalList[reject].selfPayable = $scope.autoRenderedServicesFinalList[reject].netPay;
                                                       $scope.autoRenderedServicesFinalList[reject].creditPayable =parseFloat(0);
                                               
                                                      }
                                                  }
                                              if(reject + 1 == $scope.autoRenderedServicesFinalList.length){
                                                  $scope.saveOrderPackage();
                                              }
                                                  
                                              reject++;
                                              });*/
                                	   
                                	   $scope.finalCancelDiscount();
                                          
                                       }else{
                                    	   $scope.requestStatusObject  = null;
                                    	   $timeout(function () {
   		      								
     		  								  var discountPresent =false;
     		  								  angular.forEach($scope.autoRenderedServicesList,function(value,index){
     		  								    	if(value.discount != undefined && value.discount != '' && value.discount != '.' ){
     		  								    		discountPresent =true;
     		  									     }
     		  								  });
     		  								  if(discountPresent == true ){
     		  									  var lengthofSaved  = $scope.autoRenderedServicesList.length;
     		  									  var objectForDiscount  = {};
     		  									  objectForDiscount  = $scope.autoRenderedServicesList[lengthofSaved-1];
     		  									  $scope.getDiscountCal(objectForDiscount,lengthofSaved-1);
     		  								  }
     		  								  $rootScope.stopLoader();
     		  							}, 1000);
                                       }
                                   
                                   $scope.totalSumDisAmountTemp = parseFloat(0);
                                   if($scope.requestStatusObject != null){
                                   var i = 1;
                                   angular.forEach($scope.requestStatusObject.requestDiscountDetailsDtosList,function(value,index)
                                            {
                                               
                                                      $scope.totalSumDisAmountTemp =$scope.totalSumDisAmountTemp + value.netAmount;
                                                   
                                              
                                               i++;    
                                            });
                                   }
                                   
                                   
                                   }
                                   
                                   else{
                                       $scope.requestStatusObject = null;
                                         
                                       $timeout(function () {
		       								
		     								  var discountPresent =false;
		     								  angular.forEach($scope.autoRenderedServicesList,function(value,index){
		     								    	if(value.discount != undefined && value.discount != '' && value.discount != '.' && value.pharmacyDiscountOverride != 0){
		     								    		discountPresent =true;
		     									     }
		     								  });
		     								  if(discountPresent == true ){
		     									  var lengthofSaved  = $scope.autoRenderedServicesList.length;
		     									  var objectForDiscount  = {};
		     									  objectForDiscount  = $scope.autoRenderedServicesList[lengthofSaved-1];
		     									  $scope.getDiscountCalOnloadForWarningMsg(objectForDiscount,lengthofSaved-1);
		     								  }
		     								  $rootScope.stopLoader();
		     							}, 2000);
		                                 
                                       }
                                   }
                           });
                   }
                   mainCount++;

				});//package end loop
				
				if($scope.updatePayee){
					$scope.billWiseDiscountObj.discountAmountBillWise = $scope.AutoRenderednetTotalDiscount;
				}
				$scope.updatePayee=false; 
				if($scope.roundOffSaved != undefined && $scope.roundOffSaved != null && $scope.roundOffSaved > 0 ){
					$scope.billWiseDiscountObj.discountAmountBillWise =  parseFloat($scope.AutoRenderednetTotalDiscount) +  parseFloat($scope.roundOffSaved);
					$scope.isBillWiseDiscountApplied = true;
					$scope.discountRadio = '2';
				}
				var amt =   isNaN(Number(parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)))?0:$scope.billWiseDiscountObj.discountAmountBillWise;
				if(amt>0 && $scope.AutoRenderednetTotalDiscount>0 && $scope.isBillWiseDiscountApplied == true){
				  $scope.roundOffAmt = parseFloat(amt-$scope.AutoRenderednetTotalDiscount).toFixed(2);
				}
				
				$scope.totalAmountSummary=(parseFloat($scope.AutoRenderedTotalAmountSummary)).toFixed(2);
				$scope.totalConcessionSummary=(parseFloat($scope.AutoRenderedTotalConcessionSummary)).toFixed(2);
				$scope.netPayableSummary=(parseFloat($scope.AutoRenderednetPayableSummary)).toFixed(2);
				$scope.totalDiscountSummary=(parseFloat($scope.AutoRenderednetTotalDiscount)).toFixed(2);
				$scope.totalGstTaxSummary=(parseFloat($scope.AutoRenderednetTotalTax)).toFixed(2);
				
				$scope.totalCreditPayble=(parseFloat($scope.AutoRenderednetTotalCreditPayble)).toFixed(2);
				$scope.totalSelfPayble=(parseFloat($scope.AutoRenderednetTotalSelfPayble)).toFixed(2);
				$scope.totalSelfPaybleTemp=(parseFloat($scope.AutoRenderednetTotalSelfPayble)).toFixed(2);
				
                if(serviceCount == $scope.actualServiceCount && $scope.roundOffAmt!=0)
                {
                    if($scope.totalSelfPayble!=0)
                    {
                        $scope.totalSelfPayble = parseFloat(Number(parseFloat($scope.totalSelfPayble).toFixed(2))-parseFloat($scope.roundOffAmt)).toFixed(2);
                        $scope.totalSelfPaybleTemp = parseFloat(Number(parseFloat($scope.totalSelfPaybleTemp).toFixed(2))-parseFloat($scope.roundOffAmt)).toFixed(2);
                    }else{
                        $scope.totalCreditPayble = parseFloat(Number(parseFloat($scope.totalCreditPayble).toFixed(2))-parseFloat($scope.roundOffAmt)).toFixed(2);
                    }
                    $scope.netPayableSummary = parseFloat(Number(parseFloat($scope.netPayableSummary).toFixed(2))-parseFloat($scope.roundOffAmt)).toFixed(2);
                }else{
                    $scope.roundOffAmt=0;
                }	
                $scope.actualServiceCount = $scope.autoRenderedServicesFinalList.length;
				$scope.totalCreditPaybleSplitBill =$scope.totalCreditPayble ;
				$scope.totalSelfPaybleSplitBill = $scope.totalSelfPayble;
				$scope.totalCreditPaybleSplitBillOld =0 ;
			    $scope.totalSelfPaybleSplitBillOld = 0;
			    $scope.totalCreditPaybleSplitBillOld ==$scope.totalCreditPayble;
			    $scope.totalSelfPaybleSplitBillOld = $scope.totalSelfPayble;
				$scope.totalPackagePaybleTemp=(parseFloat($scope.AutoRenderednetTotalPackagePayble)).toFixed(2);
				$timeout(function () {
					
					 if($scope.listPackageOrder.length != undefined && $scope.listPackageOrder.length != null && $scope.listPackageOrder.length >0 ){
				        	$scope.checkPackageAmountConsumption();
				        }
					
				}, 6000);
				
				return 1;
		    }
		 
		 
		 
		 var unique = function(origArr) {
			    var newArr = [],
			        origLen = origArr.length,
			        found, x, y;

			    for (x = 0; x < origLen; x++) {
			        found = undefined;
			        for (y = 0; y < newArr.length; y++) {
			            if (origArr[x] === newArr[y]) {
			                found = true;
			                break;
			            }
			        }
			        if (!found) {
			            newArr.push(origArr[x]);
			        }
			    }
			    return newArr;
			}
		 
		 var isExists;
		function checkExistsOrNot(arr, val) {
			return arr.some(function(arr) 
					{
						return (val == arr);
					});
		}
		 
		 $scope.checkSingleService= function(service)
		 {
			 $scope.servicesSelectedList.services =[];
			 angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) 
					 {
			  	      if(value.checked =="Y"){
			  	    	  if(value.orderDetailsId == service.orderDetailsId){
			  	    		  
			  	    		var found = $scope.servicesSelectedList.services.find(ar=>ar.orderDetailsId == value.orderDetailsId);
							if(found == undefined){
								$scope.servicesSelectedList.services.push(value.orderDetailsId); 
							}
			  	    	  }
				      }
					 });
			 
			 angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) 
					 {
			  	      if(value.checked =="N"){
			  	    	  if(value.orderDetailsId == service.orderDetailsId){
			  	    		var index = $scope.servicesSelectedList.services.indexOf(value.orderDetailsId);
							$scope.servicesSelectedList.services.splice(index, 1);
			  	    	  }
				      }
					 });
			 
			 
			 var isExists = false;
			 if(service.isPackage)
			 {
				 if(checkExistsOrNot($scope.servicesSelectedList.services,service.orderDetailsId))
				 {
					 angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) 
							 {
							 	angular.forEach(value.orderDetailsPackageMapperDtosList, function (value1, key) 
								{
									if(service.packageId==value1.packageId)
									{
										isExists = checkExistsOrNot($scope.servicesSelectedList.services,value.orderDetailsId);
										if(!isExists  && value.isPackageService == 'Y' ){
											$scope.servicesSelectedList.services.push(value.orderDetailsId);
											value.checked ="Y";
										}
										
									}
								});
							 });
				}else{
					 angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) 
							 {
							 	angular.forEach(value.orderDetailsPackageMapperDtosList, function (value1, key) 
								{
									if(service.packageId==value1.packageId)
									{
										isExists = checkExistsOrNot($scope.servicesSelectedList.services,value.orderDetailsId);
										if(isExists)
										{
											var index = $scope.servicesSelectedList.services.indexOf(value.orderDetailsId);
											$scope.servicesSelectedList.services.splice(index, 1);
										}
											
									}
								});
							 });
				}
			 }
			 console.log($scope.servicesSelectedList.services);
		 }
		 
		 
		 $scope.updateDummyService= function(service,serviceIdx)
		 {
	        var URI = BASE_URL + ROOT_URL + "/api/billing/updateDummyHiddenServiceFlag/is_dummy_service/"+service.orderDetailsId+"/"+(service.isDummyService==1 ? true : false)+"/"+service.isPackage+"/"+(service.packageId!=null ? service.packageId : 0)+"/"+$scope.admissionId;
	        $rootScope.startLoader();
	        BillingGenericService.serviceAction(METHOD_GET,URI,null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
	            if(response.data.status == "success"){
	            	
	            	angular.forEach(response.data.listObject, function (resp, key) 
			            	{
		            			angular.forEach($scope.initialAutoRenderedServicesForDummyServices, function (value, key) 
		            			{
					            	if(service.orderDetailsId==value.orderDetailsId)
				            		{
					            		var index = $scope.initialAutoRenderedServicesForDummyServices.findIndex(x => x.orderDetailsId === resp);	
				            			$scope.initialAutoRenderedServicesForDummyServices[index].isDummyService = service.isDummyService;
				            		}
		   					 	});
			            	});
        			$scope.filterByDummyServices();
	            }});
	        $rootScope.stopLoader();
		 }
		 
		 $scope.updateHiddenService= function(service,serviceIdx)
		 {
			 var URI = BASE_URL + ROOT_URL + "/api/billing/updateDummyHiddenServiceFlag/is_hidden_service/"+service.orderDetailsId+"/"+(service.isHiddenService==1 ? true : false)+"/"+service.isPackage+"/"+(service.packageId!=null ? service.packageId : 0)+"/"+$scope.admissionId;
			 	$rootScope.startLoader();
		        BillingGenericService.serviceAction(METHOD_GET,URI,null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
		            if(response.data.status == "success"){
		            	angular.forEach(response.data.listObject, function (resp, key) 
		            	{
	            			angular.forEach($scope.initialAutoRenderedServicesForDummyServices, function (value, key) 
	            			{
				            	if(service.orderDetailsId==value.orderDetailsId)
			            		{
				            		var index = $scope.initialAutoRenderedServicesForDummyServices.findIndex(x => x.orderDetailsId === resp);	
			            			$scope.initialAutoRenderedServicesForDummyServices[index].isHiddenService = service.isHiddenService;
			            		}
	   					 	});
		            	});
            			$scope.filterByDummyServices();
		            	
		            }});
		        $rootScope.stopLoader();
		 }
		 
		 $scope.filterByDummyServices= function()
		 {
			 //getOrderedServices();
			 $rootScope.startLoader();
			 $scope.autoRenderedServicesFinalList =[]
			 if($scope.filterDummyService==1 && $scope.filterHiddenService==1)//all 
			 {
				 $scope.autoRenderedServicesFinalList = angular.copy($scope.initialAutoRenderedServicesForDummyServices);
			 }else if($scope.filterDummyService==1 && $scope.filterHiddenService==2)//all + hidden
			 {
				 angular.forEach($scope.initialAutoRenderedServicesForDummyServices, function (value, key) 
				 {
					 if(value.isHiddenService==1)
						 $scope.autoRenderedServicesFinalList.push(value);
				 });
			 }else if($scope.filterDummyService==1 && $scope.filterHiddenService==3)//all + show
			 {
				 angular.forEach($scope.initialAutoRenderedServicesForDummyServices, function (value, key) 
				 {
					 if(value.isHiddenService==0)
						 $scope.autoRenderedServicesFinalList.push(value);
				 });
			 }else if($scope.filterDummyService==2 && $scope.filterHiddenService==1)//real + all
			 {
				 angular.forEach($scope.initialAutoRenderedServicesForDummyServices, function (value, key) 
				 {
					 if(value.isDummyService==0)
						 $scope.autoRenderedServicesFinalList.push(value);
				 });
			 }else if($scope.filterDummyService==3 && $scope.filterHiddenService==1)//dummy + all
			 {
				 angular.forEach($scope.initialAutoRenderedServicesForDummyServices, function (value, key) 
				 {
					 if(value.isDummyService==1)
						 $scope.autoRenderedServicesFinalList.push(value);
				 });
			 }else if($scope.filterDummyService==2 && $scope.filterHiddenService==2)//real + hidden
			 {
				 angular.forEach($scope.initialAutoRenderedServicesForDummyServices, function (value, key) 
				 {
					 if(value.isDummyService==0 && value.isHiddenService==1)
						 $scope.autoRenderedServicesFinalList.push(value);
				 });
			 }else if($scope.filterDummyService==2 && $scope.filterHiddenService==3)//real + show
			 {
				 angular.forEach($scope.initialAutoRenderedServicesForDummyServices, function (value, key) 
				 {
					 if(value.isDummyService==0 && value.isHiddenService==0)
						 $scope.autoRenderedServicesFinalList.push(value);
				 });
			 }else if($scope.filterDummyService==3 && $scope.filterHiddenService==2)//dummy + hidden
			 {
				 angular.forEach($scope.initialAutoRenderedServicesForDummyServices, function (value, key) 
				 {
					 if(value.isDummyService==1 && value.isHiddenService==1)
						 $scope.autoRenderedServicesFinalList.push(value);
				 });
			 }else if($scope.filterDummyService==3 && $scope.filterHiddenService==3)//dummy + show
			 {
				 angular.forEach($scope.initialAutoRenderedServicesForDummyServices, function (value, key) 
				 {
					 if(value.isDummyService==1 && value.isHiddenService==0)
						 $scope.autoRenderedServicesFinalList.push(value);
				 });
			 }
			 
			 if(($scope.filterDummyService==1 && $scope.filterHiddenService==1)){
				 $scope.changePayeeRights = true;
				 $scope.disableAddService = false;
			 }else{
				 $scope.disableAddService = true;
				 $scope.changePayeeRights = false;
			 }
			 $scope.calculationTest($scope.autoRenderedServicesFinalList,init);
			 $rootScope.stopLoaderPopup();
			 $rootScope.stopLoader();
		 }
		 
		 $scope.calculationForIntermediate = function()
		    {
			 
			 var autoRenderedServicesList = $scope.autoRenderedServicesFinalList;
			 var typeId = 1;
		    	//console.log("AUTO",autoRenderedServicesList);
		    	$scope.AutoRenderedTotalAmountSummary = 0;
				$scope.AutoRenderedTotalConcessionSummary = 0;
				$scope.AutoRenderednetPayableSummary = 0;
				$scope.AutoRenderednetTotalDiscountSummary=0;
				$scope.AutoRenderednetTotalTax=0;
				$scope.AutoRenderednetTotalDiscount=0;
				$scope.AutoRenderednetTotalCreditPayble=0;
				$scope.AutoRenderednetTotalSelfPayble=0;
				$scope.AutoRenderednetTotalPackagePayble=0;
				$scope.totalSelfPaybleTemp = 0;
				$scope.roundOffAmt = 0;
		        $scope.totalDisServiceWise = 0;
		    	$scope.totalPackagePaybleTemp = 0;
		    	$scope.totalSelfPayble = 0;
				//-----calculate amounts
				////console.log(autoRenderedServicesList);
				//return false;
				
				//var arrUnique = unique($scope.servicesSelectedList.services);
                var autoRenderedServicesListLength = autoRenderedServicesList.length;
				$scope.autoRenderedServicesFinalList = [];
				var len = $scope.servicesSelectedList.services.length;
				//var len = arrUnique.length;
			    	for(var i = 0;i < len;i++)
					{
			    		/*		   //console.log($scope.servicesSelectedList.services);
					   alert($scope.servicesSelectedList.services.length);
					   
					   angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
						   if(value.serviceMasterId == $scope.servicesSelectedList.services[i])
						   {*/
				
				angular.forEach(autoRenderedServicesList, function (value, key) 
				{
				
						   if(value.orderDetailsId == $scope.servicesSelectedList.services[i])
						   {
							   var newArr = {};
								angular.forEach(value.orderDetailsPackageMapperDtosList, function (value1, key) 
										{ 
									 
							//console.log(value.basePrice, "#", value.quantity);
							
							if(value.orderDetailsId > 0)
								{
								newArr.orderDetailsId = value.orderDetailsId;
								
								}
							else
								{
								newArr.orderDetailsId = null;
								}
							newArr.tariffId = value.tariffId;
							if(value.orderId > 0)
							{
							newArr.orderId = value.orderId;
							}
						else
							{
							newArr.orderId = null;
							}
							
							if(value.orderDate!=null){
								newArr.orderDate  = value.orderDate;
							}else if(value.createdDate!=null){
								newArr.orderDate  = value.createdDate;
							}else{
								
							}
								
							newArr.bedCategoryId = value.bedCategoryId;
							newArr.wardId = value.wardId;
							newArr.roomId = value.roomId;
							newArr.bedId = value.bedId;
							if(value.orderNo != null)
							{
								newArr.orderNo  = value.orderNo;
							}
							else
								{
									newArr.orderNo  = '';
								}
							newArr.dummyServiceAmt = value.dummyServiceAmt;
							newArr.priorityId  = value.priorityId;
							newArr.isOutsourced = value.isOutsourced;
							newArr.specialityId = value.specialityId;
							newArr.taxId = value.taxId;
							newArr.saleId = value.saleId;
							
							if(value.ordCancelled != null)
							{
								newArr.ordCancelled  = value.ordCancelled;
							}
							else
								{
									newArr.ordCancelled  = 'N';
								}
							
							if(value.ordCancelReasonId != null)
							{
								newArr.ordCancelReasonId  = value.ordCancelReasonId;
								let reason = $scope.reasonList.find((elem) => {if(elem.reasonMasterId == value.ordCancelReasonId) return true});
								value.cancelledReason = typeof(reason) != "undefined" ? reason.reasonDesc : value.cancelledReason;
							}
							else
								{
									newArr.ordCancelReasonId  = '';
								}
							
							if(value.ordCancelDatetime != null)
							{
								newArr.ordCancelDatetime  = value.ordCancelDatetime;
							}
							else
								{
									newArr.ordCancelDatetime  = '';
								}
							if(value.ordCancelRemark != null)
							{
								newArr.ordCancelRemark  = value.ordCancelRemark;
							}
							else
								{
									newArr.ordCancelRemark  = '';
								}
							
							newArr.isInvestigationOrder = (value.isInvestigationOrder=='Y')?'Y':'N';
							newArr.patientClassPercentage = value.patientClassPercentage != null ? value.patientClassPercentage  : 0.00;
			        		newArr.markupStatus =value.markupStatus != null ? value.markupStatus  : false;
			        		newArr.procedureOrderMstId = value.procedureOrderMstId > 0 ? value.procedureOrderMstId  : null;
			        		newArr.isProcedureService = value.isProcedureService!=null ? value.isProcedureService  : 'N';
			        		newArr.isPostedWhileDischarge = value.isPostedWhileDischarge!=null ? value.isPostedWhileDischarge : 'N';
			        		newArr.isChargedHalfDay = value.isChargedHalfDay;
			        		newArr.isFromAddservice = value.isFromAddservice!=null ? value.isFromAddservice : 'N';
			        		newArr.isDummyService = value.isDummyService;
			        		newArr.isHiddenService = value.isHiddenService;
							newArr.status  = value.status;
							if(value.serviceRendered != null)
							{
								newArr.serviceRendered  = value.serviceRendered;
							}
							else
								{
									newArr.serviceRendered  = '0';
								}
							
							if(value.oldOrdDtlId != null)
							{
								newArr.oldOrdDtlId  = value.oldOrdDtlId;
							}
							else
								{
									newArr.oldOrdDtlId  = null;
								}
							
                            newArr.doctorSpecialityId = ((value.doctorSpecialityId != null || value.doctorSpecialityId > 0) ? value.doctorSpecialityId  :  value.specialityId);

							
                            if(value.isDoctorRequired == null || value.isDoctorRequired == undefined)
                            {
                               newArr.isDoctorRequired  = 'N';
                            }
                        else
                            {
                              newArr.isDoctorRequired  = value.isDoctorRequired;
                            }

							
							if(value.isRefDoctorShare != null)
							{
								newArr.isRefDoctorShare  = value.isRefDoctorShare;
							}
							else
								{
									newArr.isRefDoctorShare  = null;
								}
							
							if(value.ordRenderDatetime == null || value.ordRenderDatetime == undefined)
								{
								   newArr.ordRenderDatetime  = null;
								}
							else
								{
								  newArr.ordRenderDatetime  = value.ordRenderDatetime;
								}
							
							newArr.docSharePer = value.docSharePer !=null ? parseFloat(value.docSharePer).toFixed(2) : 0;
							newArr.hospitalShare = value.hospitalShare !=null ? parseFloat(value.hospitalShare).toFixed(2) : 0;
							newArr.refDocSharePer = value.refDocSharePer !=null ? parseFloat(value.refDocSharePer).toFixed(2) : 0;
	                        newArr.discountOwnership = value.discountOwnership > 0 ? value.discountOwnership : 0;
							newArr.isRateEditable = (value.isRateEditable == 'Y')?'Y':'N';	
							newArr.isRateEditable = (value.packageConsumptionAmt>0 && newArr.isRateEditable=='Y')?'N':value.isRateEditable;
							newArr.isOrderVisibleDepartment = value.isOrderVisibleDepartment;
							newArr.isConsultationService = value.isConsultationService;
							newArr.ordRemarks = value.ordRemarks;
							newArr.nursingServiceOrderFlag =value.nursingServiceOrderFlag;
							newArr.doctorId = value.doctorId;
							newArr.contractId = value.contractId;//Ganesh Change
							newArr.isAutoRender = value.isAutoRender;//Ganesh Change
							newArr.serviceSpecialityId = value.serviceSpecialityId;
							newArr.serviceSubSpecialityId = value.serviceSubSpecialityId;
							newArr.serviceRendered = value.serviceRendered;
							newArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
							newArr.oldNetPay = value.oldNetPay;
							newArr.discountDetailsList = value.discountDetailsList; 
							newArr.serviceMasterId = value.serviceMasterId;
							newArr.serviceStandardCode = value.serviceStandardCode;
							newArr.serviceStandardName = value.serviceStandardName;
                            newArr.discountCategoryId = value.discountCategoryId != null ? value.discountCategoryId.toString()  : null;
                            newArr.totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
                            newArr.isDiscount = value.isDiscountApplicable;
                            newArr.isDiscountApplicable = (value.packageConsumptionAmt != 0) ? 'N':value.isDiscountApplicable; // isDiscountApplicable
							if(($scope.admissionDetails.isTariffRateApplicable == 'N' && parseFloat(value.creditPayable) != 0) || value.packageConsumptionAmt!=0 ){
								newArr.isDiscountApplicable = 'N';
							}else{
								newArr.isDiscountApplicable = newArr.isDiscountApplicable;
							}
							newArr.isQuantityEditable = value.isQuantityEditable;
							newArr.isRateEditable = value.isRateEditable;
							newArr.minRateEditable = value.minRateEditable;
							newArr.maxRateEditable = value.maxRateEditable;
							newArr.specialityName = value.specialityName;
							newArr.doctorName = value.doctorName;
							newArr.discountDisabledFlag = 'N';
							newArr.isPackage = value.isPackage;
							newArr.packageMasterId = typeof(value.packageId) != "undefined" ? value.packageId : value.packageMasterId;
							newArr.packageTypeId = value.packageTypeId;
							newArr.isPackageService = value.isPackageService;
							newArr.apportionedPrice = typeof(value.apportionedPrice) != "undefined" ? value.apportionedPrice : null;
							newArr.isCapService = value.isCapService;
			    			newArr.packageConsumptionAmt = value.packageConsumptionAmt;
			    			if(value.subSpecialityId==undefined||value.subSpecialityId==null)
		    				{
		    				  newArr.subSpecialityId = value.serviceRenderingDeptId;
		    				}
		    			   else{
		    			     newArr.subSpecialityId = value.subSpecialityId;
		    			     }
			    			newArr.packageId = typeof(value.packageId) != "undefined" ? value.packageId : value.packageMasterId;
			    			newArr.cancelledReason = value.cancelledReason;
							if(value.specialityName == null)
							{
								newArr.specialityName = "";
								newArr.doctorName = "";
							}
							newArr.doctorSpecialityId = ((value.doctorSpecialityId != null || value.doctorSpecialityId > 0) ? value.doctorSpecialityId  :  value.specialityId);
							newArr.docList=$scope.doctorsList;
							newArr.doctorSpecialityList = (value.doctorSpecialityList != undefined ? value.doctorSpecialityList : []);
							newArr.quantity = value.quantity;
							newArr.basePrice = parseFloat(value.basePrice).toFixed(2);
							newArr.basePriceOld = value.basePrice;
		                    newArr.oldBasePrice = value.oldBasePrice;
							newArr.concession = parseFloat(value.concession).toFixed(2);
							newArr.discountCategoryId = '';
							newArr.discountPercentage = value.discountPercentage;
							newArr.discount = value.discount;
							var dis = parseFloat(value.discount).toFixed(2);
							var disPer =value.discountPercentage;
							//alert($scope.discountDetailsList.length);
							if(value.discount == null || value.discount == 0 )
								{
									newArr.discountPercentage = 0;
									newArr.discount = 0;
									dis = 0;
									disPer = 0;
									newArr.discountDisabledFlag = 'Y';
									
								}
							
								if(value.discountDetailsList == null)
								{
									value.discountDetailsList = [];
									newArr.discountDetailsList = [];
								}
							
							newArr.taxName = value.taxName;
							//newArr.taxPercentage = value.taxPercentage;
							var txpre = 0;
							//alert(value.taxPercentage);
							if(value.taxPercentage != null || value.taxPercentage > 0){
								newArr.taxPercentage = value.taxPercentage;
								txpre = value.taxPercentage;
							}
							else
								{
								 newArr.taxPercentage = 0;
								}
							
						/*	var totalAmt = (parseFloat(value.quantity) * parseFloat(value1.serviceAmount)).toFixed(2);
							//var concession = (value.packageConsumptionAmt==0 ?parseFloat(value.concession).toFixed(2):	parseFloat(0));
							*/
							var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
							totalAmt  = parseFloat(totalAmt) - value.packageConsumptionAmt;
							var concession = (parseFloat(value.concession).toFixed(2));
							
							var withConDic =parseFloat(totalAmt - (parseFloat(concession) + parseFloat(dis))).toFixed(2);
							newArr.taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
							var gstAmt =  parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
							newArr.taxAmtOld = value.taxAmtOld;
							newArr.selfPayableOldC = value.selfPayableOldC;
							newArr.creditPayableOldC = value.creditPayableOldC;
							newArr.netPay =   parseFloat(value.netPay).toFixed(2);
							var selfPayableV =   "0";
							var creditPayableV =   "0";
							
							  if(value.payeeId == $scope.selfPayeeId || value.payeeId == null )
						       {	
						    	  newArr.selfPayable =parseFloat(value.selfPayable).toFixed(2);;
							      newArr.creditPayable =parseFloat(value.creditPayable).toFixed(2);;
							      selfPayableV =  parseFloat(newArr.selfPayable).toFixed(2);
							      creditPayableV =  parseFloat(newArr.creditPayable).toFixed(2);
						       }
						      else
						       {
						       newArr.payeeId= value.payeeId;
						       
						       if(value.selfPayable>0)
								{
							    	
							    	if(parseInt(dis) > 0 )
							    		{
							    		newArr.selfPayable =   parseFloat(value.selfPayable).toFixed(2);
							            selfPayableV =   parseFloat(value.selfPayable).toFixed(2);
							    		}
							    	else
							    		{
							    		newArr.selfPayable =parseFloat(value.selfPayableOldC).toFixed(2);
										 selfPayableV =   parseFloat(value.selfPayableOldC).toFixed(2);
							    		}
							    
							    	newArr.creditPayable =parseFloat(value.creditPayable).toFixed(2);
									creditPayableV=parseFloat(value.creditPayable).toFixed(2);
								}
							else{
								newArr.selfPayable = (value.selfPayable==null||value.selfPayable==undefined||value.selfPayable=="")?0:value.selfPayable;
								selfPayableV =   parseFloat(value.selfPayable).toFixed(2);
							   
							    newArr.creditPayable  = value.creditPayable;
							    creditPayableV = parseFloat(value.creditPayable).toFixed(2);
							    
							   }
						       }
								
						     if($scope.surchargeServiceId !=  value.serviceMasterId)
						     {
							     if(Object.keys(value.orderDetailsPayeeMapperDtosList).length > 0)
							     {
							    		angular.forEach(value.orderDetailsPayeeMapperDtosList, function (value1, key) {
											
							    			 if(value1.payeeId == $scope.selfPayeeId)
										       {
							    				 value1.payeeAmount =  newArr.selfPayable;
										       }
											
										});
							      }
						     }
						    newArr.orderDetailsPayeeMapperDtosList = value.orderDetailsPayeeMapperDtosList;
						    newArr.orderDetailsPackageMapperDtosList = value.orderDetailsPackageMapperDtosList;
						    
						    //package force add
						    newArr.forceAddRemoved = value.forceAddRemoved;
						    newArr.isConsumed = value.isConsumed;
						    newArr.oldDiscount = value.discount;
						    newArr.checked = value.checked ;
						    
						    if(newArr.isDiscountApplicable == 'Y' && value1.isPackageService == 'N'  && value.status == 'A')
	                        {
		                    	$scope.totalDisServiceWise  = parseFloat($scope.totalDisServiceWise) + ((parseFloat(value.quantity) * parseFloat(value.basePrice)));
	                        }

						    
						  //var newPatAmt = parseFloat(((parseFloat(totalAmt) -(parseFloat(value.concession) + parseFloat(dis))) + parseFloat(gstAmt)-parseFloat(value.packageConsumptionAmt))).toFixed(2);
						    var newPatAmt = parseFloat(parseFloat(newArr.netPay)).toFixed(2);
						    if(value1.isPackageService=='N' && value.status=='A')
							{
						    	if(	$scope.ambulanceChargesServiceId != undefined && $scope.ambulanceChargesServiceId != null  && $scope.ambulanceChargesServiceId  == value1.serviceId){
									$scope.AutoRenderedTotalAmountSummary=(parseFloat($scope.AutoRenderedTotalAmountSummary) + parseFloat(value.quantity) * parseFloat(value.netPay)).toFixed(2);
								}else{
									$scope.AutoRenderedTotalAmountSummary=(parseFloat($scope.AutoRenderedTotalAmountSummary) + parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
								}
								$scope.AutoRenderedTotalConcessionSummary=(parseFloat($scope.AutoRenderedTotalConcessionSummary) + (value.packageConsumptionAmt==0?parseFloat(value.concession):0)).toFixed(2);
								$scope.AutoRenderednetPayableSummary=(parseFloat($scope.AutoRenderednetPayableSummary) + (parseFloat(newPatAmt))).toFixed(2);
								$scope.AutoRenderednetTotalDiscountSummary=(parseFloat($scope.AutoRenderednetTotalDiscountSummary) + parseFloat(0)).toFixed(2);
								$scope.autoRenderedServicesBillingDetails.push($scope.createBillingDetailsObj(value));
								$scope.AutoRenderednetTotalTax = (parseFloat($scope.AutoRenderednetTotalTax) + parseFloat(gstAmt)).toFixed(2);
								$scope.AutoRenderednetTotalDiscount = (parseFloat($scope.AutoRenderednetTotalDiscount) + parseFloat(dis)).toFixed(2);
								$scope.AutoRenderednetTotalCreditPayble=(parseFloat($scope.AutoRenderednetTotalCreditPayble) + parseFloat(creditPayableV)).toFixed(2);
								$scope.AutoRenderednetTotalSelfPayble=(parseFloat($scope.AutoRenderednetTotalSelfPayble) + parseFloat(selfPayableV)).toFixed(2);
								$scope.AutoRenderednetTotalPackagePayble= (parseFloat($scope.AutoRenderednetTotalPackagePayble)+ parseFloat(value.packageConsumptionAmt)).toFixed(2);
							}
							newArr.createdBy = value.userId;
							newArr.createdDate = value.createdDate;
							newArr.medallServiceId = value.medallServiceId;
							newArr.ordRenderDatetimeString = moment(value.ordRenderDatetime).format('DD/MM/YYYY');
							newArr.orderDateStringNew = moment(value.orderDate).format('DD/MM/YYYY HH:mm');
							newArr.pharmacyDiscountOverride = value.pharmacyDiscountOverride;
						  });
						$scope.autoRenderedServicesFinalList.push(newArr);
						$scope.initialAutoRenderedServicesFinalList=$scope.autoRenderedServicesFinalList;
				      }
				   });// end of loop
				}
				
		    	if($scope.updatePayee)
		    	{
					$scope.billWiseDiscountObj.discountAmountBillWise = $scope.AutoRenderednetTotalDiscount;
				}
				$scope.updatePayee=false;
					
				var amt =   isNaN(Number(parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)))?0:$scope.billWiseDiscountObj.discountAmountBillWise;
				if(amt>0 && $scope.AutoRenderednetTotalDiscount>0){
				  $scope.roundOffAmt = parseFloat(amt-$scope.AutoRenderednetTotalDiscount).toFixed(2);
				}
				
				$scope.totalAmountSummary=(parseFloat($scope.AutoRenderedTotalAmountSummary)).toFixed(2);
				$scope.totalConcessionSummary=(parseFloat($scope.AutoRenderedTotalConcessionSummary)).toFixed(2);
				$scope.netPayableSummary=(parseFloat($scope.AutoRenderednetPayableSummary)).toFixed(2);
				$scope.totalDiscountSummary=(parseFloat($scope.AutoRenderednetTotalDiscount)).toFixed(2);
				$scope.totalGstTaxSummary=(parseFloat($scope.AutoRenderednetTotalTax)).toFixed(2);
				
				$scope.totalCreditPayble=(parseFloat($scope.AutoRenderednetTotalCreditPayble)).toFixed(2);
				$scope.totalSelfPayble=(parseFloat($scope.AutoRenderednetTotalSelfPayble)).toFixed(2);
				$scope.totalSelfPaybleTemp=(parseFloat($scope.AutoRenderednetTotalSelfPayble)).toFixed(2);
				
                if(len==autoRenderedServicesListLength && $scope.roundOffAmt!=0)
                {
                    if($scope.totalSelfPayble!=0)
                    {
                        $scope.totalSelfPayble = parseFloat(Number(parseFloat($scope.totalSelfPayble).toFixed(2))-parseFloat($scope.roundOffAmt)).toFixed(2);
                        $scope.totalSelfPaybleTemp = parseFloat(Number(parseFloat($scope.totalSelfPaybleTemp).toFixed(2))-parseFloat($scope.roundOffAmt)).toFixed(2);
                    }else{
                        $scope.totalCreditPayble = parseFloat(Number(parseFloat($scope.totalCreditPayble).toFixed(2))-parseFloat($scope.roundOffAmt)).toFixed(2);
                    }
                    $scope.netPayableSummary = parseFloat(Number(parseFloat($scope.netPayableSummary).toFixed(2))-parseFloat($scope.roundOffAmt)).toFixed(2);
                }else{
                    $scope.roundOffAmt=0;
                }
					
				$scope.totalCreditPaybleSplitBill =$scope.totalCreditPayble ;
				$scope.totalSelfPaybleSplitBill = $scope.totalSelfPayble;
				$scope.totalCreditPaybleSplitBillOld =0 ;
		        $scope.totalSelfPaybleSplitBillOld = 0;
		        $scope.totalCreditPaybleSplitBillOld = $scope.totalCreditPayble;
		        $scope.totalSelfPaybleSplitBillOld = $scope.totalSelfPayble;
				$scope.totalPackagePaybleTemp=(parseFloat($scope.AutoRenderednetTotalPackagePayble)).toFixed(2);
				
				return true;
		    	
		    }
		 
		 $scope.createBillingDetailsObj = function(item){
		    	var obj = {
		    		"billingDetailsId"	:"",
		    		"billingMasterId"	:"",
		    		"serviceId"			:item.serviceMasterId,
		    		"rate"				:"",
		    		"quantity"			:item.quantity,
		    		"concession"		:item.concession,
		    		"concessionAmount"	:"",
		    		"grossAmount"		:(item.basePrice * item.quantity) - item.concession,
		    		"coPayPercentage"	:"",
		    		"unitId"			:$scope.unitId,
		    		"organizationId"	:$scope.organizationId,
		    		"status"			:"A",
		    		"createdBy"			:$scope.userId,
		    		"updatedBy"			:$scope.userId,
		    		"createdDate"		:new Date().getTime(),
		    		"updatedDate"		:new Date().getTime(),
		    		"serviceMasterId"	:item.serviceMasterId,
			    	"serviceStandardName":item.serviceStandardName,
			    	"serviceStandardCode":item.serviceStandardCode,
			    	"specialityId"		:item.specialityId,
			    	"specialityName"	:item.specialityName,
			    	"subSpecialityId"	:item.subSpecialityId,
			    	"subSpecialityName"	:item.subSpecialityName,
			    	"basePrice"			:item.basePrice,
			    	"discount"			:(item.discount == undefined || item.discount == '') ? '0' : item.discount,
			    	"discountPercentage":item.discountPercentage, // direct discount percentage
			    	"discountAmount"	:item.discountAmount, // direct discount amount
			    	"orderDetailsId"	:item.orderDetailsId
		    	}
		    	return obj;
		    }
		 
		 $scope.search="";
			/*$scope.searchServices = function(searchKeyword){
				$scope.servicesList=[];
				searchKeyword=searchKeyword+"---"+$scope.patientDetails.genderId;
				
				var serData = {
						organizationId : $scope.organizationId,
						unitId : $scope.unitId,
						searchKeyword : searchKeyword,
						defaultSelfTariffId : $scope.defaultSelfTariffId,
						paymentEntitlementTypeId : $scope.admissionDetails.paymentEntitlementId,
						visitTypeId : $scope.visitTypeId,
						billingBedCategoryId : $scope.admissionDetails.bedBillingCategoryId,
						patientCategoryId : $scope.admissionDetails.patientCategoryId,
						admissionId : $scope.admissionDetails.admissionId
					}
				var URI = BASE_URL + ROOT_URL + "/api/unit/tariff/serviceMaster";
				return BillingGenericService.serviceAction(METHOD_POST,URI,serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						$scope.servicesList = response.data.listObject;
					}
					return $scope.servicesList;
				});
			}*/
		 
		 $scope.searchServices = function(searchKeyword){
				$scope.servicesList=[];
				searchKeyword=searchKeyword+"---"+$scope.patientDetails.genderId;
				var serData = {
						organizationId : $scope.organizationId,
						unitId : $scope.unitId,
						searchKeyword : searchKeyword
				}
				//var URI = BASE_URL + ROOT_URL + "/global/serviceMaster/"+searchKeyword+"/unit/"+$scope.unitId+"/org/"+$scope.organizationId;
				var URI = BASE_URL + ROOT_URL + "/global/serviceMaster";
				return BillingGenericService.serviceAction(METHOD_POST,URI, serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						$scope.servicesList = response.data.listObject;
					}
					return $scope.servicesList;
				});
			}
		 $('#table-scroll-id').on('scroll', function() {
			 var height = $(this).height();
			  var top = $(this).scrollTop();
			  if($scope.recordDisplay>$scope.autoRenderedServicesFinalList.length)
				  {
				  return false;
				  }
			  if ($(this).get(0).scrollHeight - $(this).height()-Math.floor($(this).scrollTop())<1) 
			  {
				 $scope.recordDisplay+=10;
			  }
			  if ($(this).get(0).scrollHeight - $(this).height()-Math.floor($(this).scrollTop())==1) 
			  {
				  $scope.recordDisplay+=10;
			  }
//			 $scope.recordDisplay+=20;
//			    let div = $(this).get(0);
//			    if(div.scrollBottom + div.clientHeight >= div.scrollHeight) {
//			    	$scope.recordDisplay+=40;
//			    }
			});
		 $scope.updateRenderedDate = function(service)
		 {	var flag = false;
			 var currentDate = new Date();
			              var newdate = moment(currentDate).format('YYYY-MM-DD');
			              var admDate = $scope.admissionDetails.doa;
			              var admDate1=admDate;
			              admDate1=admDate.split('/');
			              var admDate2= admDate1[2]+"-"+admDate1[1]+"-"+admDate1[0];
			              var currentRenderDate =moment(service.ordRenderDatetime).format('YYYY-MM-DD');
			              var d1 = Date.parse(newdate); 
			              var d2 = Date.parse(admDate2);
			              var d3 = Date.parse(currentRenderDate);
			 			  var temp = service.ordRenderDatetime;
			 			 var temp2 = service.ordRenderDatetimeString;
						  var temp1 =temp2;
						   temp1=temp2.split('/');
						  var newStore = temp1[2]+"-"+temp1[1]+"-"+temp1[0];
						  var d4 = Date.parse(newStore);
			 if ( d3 < d2){
				 	
					growl.warning("Rendered date should be Greater than or equal to Admission date",
							{
							title : 'warning!'
							});
					service.ordRenderDatetime = service.orderDate;
					 flag = true;
					return false;
			 }else{
				  service.ordRenderDatetime =Date.parse(temp);
			 }
			 if(d3>d1){
				  growl.warning("Rendered date should be less than or equal to current date.",
							{
							title : 'warning!'
							});
				  service.ordRenderDatetime =service.orderDate;
				  flag = true;
				  return false;
			 }
			 var data1={
					 isPackage : typeof(service.isPackage) != "undefined" ? service.isPackage : false,
					 orderDetailsId:service.orderDetailsId,
				}
			 if(service.isPackage==false)
				 {
				 if(flag == true)
				 {
					 data1.ordRenderDatetime =service.ordRenderDatetime;
				 }else{
					  data1.ordRenderDatetime= Date.parse(temp);
				 }
				 }
			 if(service.isPackage==true)
			 {
			 data1.packageApplicableDate= Date.parse(service.packageApplicableDateDisplayString);
			 }
				var URI = BASE_URL + ROOT_URL + "/api/billing/getFreeTextOrderServices/updateDate";
				BillingGenericService.serviceAction(METHOD_POST, URI, data1, NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
							if(response.data.status == 'success')
							{
								growl.success("Rendered date updated successfully");
							}
						});
		 }
		 $scope.drawAllCount=0;
		 $scope.drawAll=function()
		 {
			 if($scope.drawAllCount==0)
			 {
			 $rootScope.startLoader();
			 $scope.drawAllCount=1;
			 }
			 var rowCount = $('#isTableReady tr').length;
			 if (  rowCount!=undefined && rowCount!=null && rowCount==0 ) {
				 $scope.recordDisplay+=1;
			 }
			 if (  rowCount!=undefined && rowCount!=null && rowCount>0 && rowCount<10 ) {
				 $scope.recordDisplay+=1;
				 if(rowCount>10)
				 {
				 clearInterval(handle);
				 }
			 }
		 }
		 var handle = setInterval($scope.drawAll, 100);
		 $scope.stopLoaderAfterDataLoad=function()
		 {
			 var rowCount = $('#isTableReady tr').length;
		        if(rowCount>=10)
		        	{
		        	$rootScope.stopLoader();
		        	clearInterval(handle1);
		        	}
		 }
	var handle1 = setInterval($scope.stopLoaderAfterDataLoad, 500);
		 $scope.recordDisplay=10;
		 $scope.filterFlag=false;
			$scope.checkKeywordLength=function(keyword,keyword1,keyword2)
			{
				if((keyword!=undefined && keyword!=null && keyword.length>0) || (keyword1!=undefined && keyword1!=null && keyword1.length>0) || (keyword2!=undefined && keyword2!=null && keyword2.length>0))
					{
					$scope.filterFlag=true;
					}
				else
					{
					$scope.filterFlag=false;
					}
				if(keyword!=undefined && keyword.serviceStandardName!=undefined && keyword.serviceStandardName=="")
					{
					keyword=undefined;
					}
				if(keyword1!=undefined && keyword1.orderDateStringNew!=undefined && keyword1.orderDateStringNew=="")
				{
				keyword1=undefined;
				}
				if(keyword2!=undefined && keyword2.ordRenderDatetimeString!=undefined && keyword2.ordRenderDatetimeString=="")
				{
				keyword2=undefined;
				}
				if(keyword!=undefined && keyword!=null && keyword!="")
					{
					$scope.serviceLength=keyword.length;
					}
					if(keyword1!=undefined && keyword1!=null && keyword1!="")
					{
					$scope.orderDateLength=keyword1.length;
					}
					if(keyword2!=undefined && keyword2!=null && keyword2!="")
					{
					$scope.renderedDateLength=keyword2.length;
					}
				$scope.recordDisplay=10;
				if(keyword!=undefined && keyword1==undefined && keyword2==undefined)
				{
			 $scope.data = $filter('filter')($scope.initialAutoRenderedServicesFinalList, {
		            $: keyword
		          });
			 $scope.autoRenderedServicesFinalList = $scope.data;
				}
				
				if(keyword!=undefined && keyword1!=undefined && keyword2==undefined)
				{
			 $scope.data = $filter('filter')($scope.initialAutoRenderedServicesFinalList, {
		            $: keyword,$: keyword1
		          });
			 $scope.autoRenderedServicesFinalList = $scope.data;
				}
				
				if(keyword!=undefined && keyword1!=undefined && keyword2!=undefined)
				{
			 $scope.data = $filter('filter')($scope.initialAutoRenderedServicesFinalList, {
		            $: keyword,$:keyword1,$:keyword2
		          });
			 $scope.autoRenderedServicesFinalList = $scope.data;
				}
				
				if(keyword!=undefined && keyword1==undefined && keyword2!=undefined)
				{
			 $scope.data = $filter('filter')($scope.initialAutoRenderedServicesFinalList, {
		            $: keyword,$:keyword2
		          });
			 $scope.autoRenderedServicesFinalList = $scope.data;
				}
				
				if(keyword==undefined && keyword1==undefined && keyword2!=undefined)
				{
			 $scope.data = $filter('filter')($scope.initialAutoRenderedServicesFinalList, {
		            $:keyword2
		          });
			 $scope.autoRenderedServicesFinalList = $scope.data;
				}
				
				if(keyword==undefined && keyword1!=undefined && keyword2==undefined)
				{
			 $scope.data = $filter('filter')($scope.initialAutoRenderedServicesFinalList, {
				 $:keyword1
		          });
			 $scope.autoRenderedServicesFinalList = $scope.data;
				}
			
//					$scope.recordDisplay+=10;
				    $rootScope.startLoader();
					if($scope.serviceLength!=undefined || $scope.orderDateLength!=undefined  ||$scope.renderedDateLength!=undefined )
					{
						setInterval(function() {
							 var rowCount = $('#isTableReady tr').length;
						        if(rowCount<10)
						        	{
						        	$scope.recordDisplay+=10;
						        	rowCount=rowCount+$scope.recordDisplay;
						        	$rootScope.stopLoader();
						        	}
						        else
						        	{
						        	$rootScope.stopLoader();
						        	}
						    }, 300);
					}
			}
			$scope.getservicesPop = function()
		    {
				$scope.servicetab =1;
		    	$scope.servicesListPopup = [];
		    	$scope.selectedPackageList = [];
		    	$scope.totalAmountSummaryPopup = parseFloat(0);
		    }
			//get cancel services by admission id start
			$scope.getCancelServices=function(){
				$scope.orderCancelList = [];
				if($scope.admissionDetails.admissionId!=undefined && $scope.admissionDetails.admissionId!=""  && $scope.admissionDetails.admissionId!=null){
					$rootScope.startLoader();
			    	var URI = BASE_URL + ROOT_URL + "/billing/getCancelServices/"+$scope.admissionDetails.admissionId+"/"+$scope.admissionDetails.visitTypeId+"/"+$scope.organizationId;
					BillingGenericService.serviceAction(METHOD_GET,URI, {} , NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						if(response.data.status == 'success'){
							$scope.orderCancelList = response.data.listObject;
							//console.log("orderCancelList",$scope.orderCancelList);
						}
						
					});
					$rootScope.stopLoader();
				}
			}
			
			//Check Service in inclusion
			  var isContractExists;
				function checkContractIdExistsOrNot(arr, val) {
					return arr
							.some(function(arr) {
								if(val === arr.contractId)
									{
										return true;
									}
								else
									{
										return false;
									}
							});
				}
				
			//get cancel services by admission id end
				
	
			    $scope.checkServiceMappedWithTest = function(serviceId,specialityId)
				{
					     $rootScope.startLoaderPopup();
					     var labSpecialityId = 0;
					     if(specialityId==$scope.labConfig.labSpecialityId)
							{
					    	   labSpecialityId = specialityId;
							}
						var URI = BASE_URL + ROOT_URL + "/global/serviceMaster/checkService/"+labSpecialityId;
						var data = {serviceMasterId:serviceId,orgId:$scope.organizationId,unitId:$scope.unitId};
					    return BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
						{
					    	if(response.data.status == "success")
							{
								$rootScope.stopLoaderPopup();

								if(response.data.object==0)

								{
								   growl
									.warning('Service is not mapped with test..!!',{
												title : 'Warning!'
											});
								   response.data.object = 1;
							     }
								 return response.data.object;
							}
							else if(response.data.status == "error"){
								  $rootScope.stopLoaderPopup();
								  return response.data.object;
							   }
						 });
				}
			    
			    $scope.serviceCheckFlag=false;
				$scope.checkServiceRenderOrNot = function()
				{
					$scope.serviceCheckFlag=true;
					if($scope.isFromAddServiceToTempServiceList==true){
						$scope.addServiceToTempServiceList($scope.item,$scope.mod, $scope.labl,$scope.freeTextOrderedServicesId,$scope.freeTextserviceName);
					}else{
						 $scope.addServiceFromSearch($scope.item,$scope.mod, $scope.labl, true);
					}
					
				}   
			    
			$scope.forServicePayeeId  = $scope.selfPayeeId;
		    $scope.addServiceToTempServiceList = function($item, $model, $label,freeTextOrderedServicesId,freeTextserviceName)
		    {
		    	$scope.freeTextOrderedServicesId = freeTextOrderedServicesId;
		    	$scope.freeTextserviceName = freeTextserviceName;
		    	$scope.isFromAddServiceToTempServiceList=true;
		    	$scope.item={};
				$scope.mod="";
				$scope.labl="";
				  var serviceObj = $scope.autoRenderedServicesFinalList.find(obj => {return obj.serviceMasterId == $item.serviceMasterId && obj.orderDateString == moment(new Date()).format('YYYY-MM-DD')})
		      	if(serviceObj != undefined && $scope.serviceCheckFlag==false){
		      		 $scope.item=$item;
		 			$scope.mod=$model;
		 			$scope.labl=$label;
		 			angular.element('#pendingServiceAdd').modal('show');
		      	         return false;
		      	} 
		    	
		    	
		    	
		    	var promise = $scope.checkServiceMappedWithTest($item.serviceMasterId,$item.specialityId);
				promise.then(function(obj) 
						{
		    	             if(obj>0)
		    	           {
		    	$scope.forServicePayeeId  = $scope.selfPayeeId;
				//console.log($item);
				
				$scope.search = '';
				$('#searchServiceId').val('');
				if(parseInt($scope.contractId) == 0)
				{
				$scope.contractId = null;
				}
				var obj = {
						"admissionId" :$scope.admissionId,
						"encounterId" :$scope.admissionId,
						"contractId" :$scope.contractId,
						"serviceId" :$item.serviceMasterId,
						"currentDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
						"organizationId":$scope.organizationId,
						"unitId":$scope.unitId,
						"visitTypeId":$scope.visitTypeId,
						"billingBedCategoryId":$scope.admissionDetails.bedBillingCategoryId,
						"patientCategoryId":$scope.admissionDetails.patientCategoryId,
						"defaultSelfTariffId":$scope.defaultSelfTariffId,
					    "selfPayeeId" :$scope.selfPayeeId,
					    "patientClassId":$scope.admissionDetails.patientClassId,
					    "tariffId" :$scope.defaultSelfTariffId,
					    "quantity":1,
					    "patientId":$scope.admissionDetails.patientId,
					    "paymentEntitlementId":$scope.admissionDetails.paymentEntitlementId,
					    "taxPercentage":$item.taxPercentage,
					};
				
				//console.log('Base prise',obj)
				//return false;
				$rootScope.startLoaderPopup();
				var URI = BASE_URL + ROOT_URL + "/global/consumption/getSerivceRateGlobal";
				BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status=="success"){
						//var basePriceObj=response.data.object;
						if(response.data.object != "undefined" && response.data.object != null)
    					{
    						if(response.data.object.doesNotExist==null)
							{
    							$scope.forServicePayeeId = response.data.object.payeeId;
    							$item.basePrice=response.data.object.serviceRate;
    		                    $item.oldBasePrice =response.data.object.serviceRate;
    		                    $item.dummyServiceAmt=response.data.object.serviceRate;
    							$item.quantity=1;
    							$item.selfPayable = response.data.object.selfPayable;
    							$item.creditPayable = response.data.object.creaditPayable;
    							$item.netPay = response.data.object.finalPrice;
    							$item.packageConsumptionAmt = response.data.object.packageConsumptionAmt;
    							$item.contractId = response.data.object.contractId;
    							$item.patientClassPercentage = response.data.object.patientClassPercentage;
    							$item.markupStatus = response.data.object.markupStatus;
    							$item.minRateEditable = response.data.object.minRateEditable;
    							$item.maxRateEditable = response.data.object.maxRateEditable;
    							$item.renderingDate=new Date();
    							$scope.renderingDate.push({opened:false});
    							$item.tariffId=response.data.object.tariffId;
        						$item.ordTotalAmount = response.data.object.ordTotalAmount;
        						$item.concession = response.data.object.concession;
        						$item.isDiscountApplicable = response.data.object.isDiscountApplicable;
        						$item.taxAmt = response.data.object.taxAmt;
        						$item.taxPercentage = response.data.object.taxPercentage;
    							$item.orderDetailsPayeeMapperDtosList=response.data.object.orderDetailsPayeeMapperDtosList;
    							$item.orderDetailsPackageMapperDtosList=response.data.object.orderDetailsPackageMapperDtosList;
    							$item.medallServiceId = response.data.object.medallServiceId;
    							$item.isRateEditable=response.data.object.isRateEditable;
        						$item.isQuantityEditable=response.data.object.isQuantityEditable;
        						$item.bedCategoryId=response.data.object.billingBedCategoryId;
        						$item.wardId = $scope.admissionDetails.wardId;
        						$item.roomId = $scope.admissionDetails.roomId;
        						$item.bedId = $scope.admissionDetails.bedId;
        						$item.isAutoRender = (response.data.object.isAutoRender == 'Y' ? 'Y':'N');
        						$item.serviceRendered = (response.data.object.isAutoRender == 'Y'?1:0);
        						$item.freeTextOrderedServicesId = $scope.freeTextOrderedServicesId;
        						$item.freeTextserviceName = $scope.freeTextserviceName;
        						$item.isDummyService = $scope.addServiceIsDummyService;
    							$scope.servicePopupCalculate($item);
    							angular.forEach($scope.freeTextServiceList, function (value, key) {
        							if(freeTextOrderedServicesId==value.freeTextOrderedServicesId){
        									value.status = 'I';
        								}
        							});
    							$scope.freeTextserviceName = "";
        						$scope.freeTextOrderedServicesId= null;
        						$scope.serviceCheckFlag = false;
    							$rootScope.stopLoaderPopup();
							}else{
								$rootScope.stopLoaderPopup();
    							growl.warning("Service is not mapped with the tariff..!!",{
    								title : WARNING_MSG
    							});
    							$scope.serviceCheckFlag==false
    							return false;
							}
							
							
							}
						else
							{
								$rootScope.stopLoaderPopup();
								growl.warning("Please add base price of this service and then add..!!",{
									title : ERROR_MSG
								});
								$scope.serviceCheckFlag==false
							return false;
							}
						//concession
						//console.log("FINAL ITEM",$item);
						
					}else
						{
							$scope.serviceCheckFlag==false
						   $rootScope.stopLoaderPopup();
						}
				});
				
				$scope.isFromAddServiceToTempServiceList=false;
				
				//console.log($scope.tempServiceDetailsList);
				$scope.search = "";
				
		    	           }
		    	             else{
		    	         	   // $scope.search = '';
		    	         	 growl
		    	     			.warning(
		    	     					'Service is Not mapped with Test!!',
		    	     					{
		    	     						title : 'Warning!'
		    	     					});
		    	              }
						 });    
			}
		    
		    
		    $scope.getRateOnChangedRendringDate = function(obj,indx)
		    {
		    	
				var obj1 = {
						"admissionId" :$scope.admissionId,
						"encounterId" :$scope.admissionId,
						"contractId" :$scope.contractId,
						"serviceId" :obj.serviceMasterId,
						"currentDate" : moment(obj.renderingDate).format('YYYY-MM-DD HH:mm:ss'),
						"organizationId":$scope.organizationId,
						"unitId":$scope.unitId,
						"visitTypeId":$scope.visitTypeId,
						"billingBedCategoryId":$scope.admissionDetails.bedBillingCategoryId,
						"patientCategoryId":$scope.admissionDetails.patientCategoryId,
						"defaultSelfTariffId":$scope.defaultSelfTariffId,
					    "selfPayeeId" :$scope.selfPayeeId,
					    "patientClassId":$scope.admissionDetails.patientClassId,
					    "tariffId" :$scope.defaultSelfTariffId,
					    "quantity":1,
					    "patientId":$scope.admissionDetails.patientId,
					    "paymentEntitlementId":$scope.admissionDetails.paymentEntitlementId,
					    "taxPercentage":obj.taxPercentage,
					};
				
				$rootScope.startLoaderPopup();
				var URI = BASE_URL + ROOT_URL + "/global/consumption/getSerivceRateGlobal";
				BillingGenericService.serviceAction(METHOD_POST,URI, obj1, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status=="success"){
						//var basePriceObj=response.data.object;
						if(response.data.object != "undefined" && response.data.object != null)
    					{
    						if(response.data.object.doesNotExist==null)
							{
    							
    							
    							var newArr = {};
    				            newArr.dummyServiceAmt = response.data.object.serviceRate;
    				    		newArr.contractId = response.data.object.contractId;//Ganesh
    				    		newArr.isTariffRateApplicable = response.data.object.isTariffRateApplicable;
    				        	newArr.serviceSpecialityId = response.data.object.specialityId;
    				    		newArr.serviceSubSpecialityId = response.data.object.subSpecialityId;
    				    		newArr.subSpecialityId = response.data.object.subSpecialityId;
    				        	newArr.taxId = response.data.object.taxId;
    				    		newArr.serviceMasterId = obj.serviceMasterId;
    				    		newArr.serviceStandardCode = obj.serviceStandardCode;
    				    		newArr.serviceStandardName = obj.serviceStandardName;
    				    		newArr.isDiscountApplicable = obj.isDiscountApplicable;
    				    		newArr.isDiscount = obj.isDiscount;
    				    		newArr.isQuantityEditable = obj.isQuantityEditable;
    				    		newArr.isRateEditable = obj.isRateEditable;
    				    		newArr.minRateEditable = obj.minRateEditable;
    				    		newArr.maxRateEditable = obj.maxRateEditable;
    				    		newArr.specialityName = obj.specialityName;
    				    		newArr.subSpecialityName = obj.subSpecialityName;
    				    		newArr.isDoctorRequired = obj.isDoctorRequired;
    				    		newArr.quantity = obj.quantity;
    				    		newArr.basePrice = response.data.object.serviceRate;
    				    		newArr.basePriceOld = response.data.object.serviceRate;
    				            newArr.oldBasePrice =response.data.object.serviceRate;
    				    		newArr.totalAmt = parseFloat(newArr.quantity) * parseFloat(newArr.basePrice).toFixed(2);
    				    		newArr.concession = typeof( response.data.object.concession) != "undefined" ? parseFloat( response.data.object.concession).toFixed(2) : parseFloat(0).toFixed(2);
    				    		newArr.patientClassPercentage = typeof( response.data.object.patientClassPercentage) != "undefined" ? parseFloat( response.data.object.patientClassPercentage).toFixed(2) : parseFloat(0).toFixed(2);
    				    		newArr.discountPercentage = 0;
    				    		newArr.discount =0;
    				    		newArr.doctorId = '';
    				    		newArr.specialityId = response.data.object.specialityId;
    				    		newArr.doctorSpecialityId = '';
    				    		newArr.discountDetailsList = [];
    				    		newArr.taxName = typeof(obj.taxName) != "undefined" ? obj.taxName : "-";
    				    		newArr.taxId = typeof(obj.taxId) != "undefined" ? obj.taxId : null;
    				    		newArr.taxPercentage = typeof(obj.taxPercentage) != "undefined" ? obj.taxPercentage : 0;
    				    		newArr.selfPayable =   response.data.object.selfPayable;
    				    		newArr.creditPayable =  response.data.object.creditPayable;
    				    		newArr.netPay =    response.data.object.netAmount;
    				    		newArr.isAutoRender =   response.data.object.isAutoRender;
    				            newArr.serviceRendered = obj.serviceRendered;
    				    		newArr.serviceRenderingDeptId = obj.subSpecialityId;
    				    		newArr.createdBy  = $scope.userId;
    				    		newArr.createdDate = new Date().getTime();
    				    		newArr.apportionedPrice = (typeof(obj.apportionedPrice) != "undefined" && obj.apportionedPrice != null) ? item.apportionedPrice : null;
    				    		newArr.isPackageService = 'N';
    				    		newArr.packageId = null;
    				    		newArr.packageTypeId = null;
    				    		newArr.packageEitherOrGroupId = null;
    				    		newArr.packageCapDetailsId = null;
    				    		newArr.isCapService = 'N';
    				    		newArr.packageConsumptionAmt =  response.data.object.packageConsumptionAmt;
    				    		newArr.isRefDoctorShare = obj.isRefDoctorShare;
    				    		newArr.payeeId = response.data.object.payeeId;
    				    		newArr.renderingDate =   obj.renderingDate;
    				            newArr.isDoctorRequired = obj.isDoctorRequired;
    				            newArr.isTariffRateApplicable = response.data.object.isTariffRateApplicable; // 12-10-2019
    				    		newArr.markupStatus = response.data.object.markupStatus;
    				    		newArr.tariffId = $scope.defaultSelfTariffId;
    				    		newArr.orderDetailsPayeeMapperDtosList = response.data.object.orderDetailsPayeeMapperDtosList;
    				    		newArr.orderDetailsPackageMapperDtosList = response.data.object.orderDetailsPackageMapperDtosList;
    				    		newArr.medallServiceId = response.data.object.medallServiceId;
    				    		newArr.bedCategoryId = response.data.object.billingBedCategoryId;
    				    		newArr.wardId = $scope.admissionDetails.wardId;
    							newArr.roomId = $scope.admissionDetails.roomId;
    							newArr.bedId = $scope.admissionDetails.bedId;
    							
    							$scope.servicePopupCalculate(newArr,indx);
    							
    							$rootScope.stopLoaderPopup();
							}else{
								$rootScope.stopLoaderPopup();
    							growl.warning("Service is not mapped with the tariff..!!",{
    								title : WARNING_MSG
    							});
    							return false;
							}
							
							
							}
						else
							{
								$rootScope.stopLoaderPopup();
								growl.warning("Please add base price of this service and then add..!!",{
									title : ERROR_MSG
								});
							return false;
							}
						//concession
						//console.log("FINAL ITEM",$item);
						
					}else
						{
						   $rootScope.stopLoaderPopup();
						}
				});
				
				
				
				//console.log($scope.tempServiceDetailsList);
				$scope.search = "";
				
		    	           
			}
		    
		    
		    
		    
		    $scope.addServiceFromSearch = function($item, $model, $label,flag)
		    {
		    	
		    	$scope.isFromAddServiceToTempServiceList=false;
		    	
		    	$scope.item={};
				$scope.mod="";
				$scope.labl="";
		        var serviceObj = $scope.autoRenderedServicesFinalList.find(obj => {return obj.serviceMasterId == $item.serviceMasterId && obj.orderDateString == moment(new Date()).format('YYYY-MM-DD')})
		    	if(serviceObj != undefined && $scope.serviceCheckFlag==false){
		    		  $scope.item=$item;
		    		  if($scope.item.packageTypeId==5)
		    			  {
		    			  return false;
		    			  }
		  			$scope.mod=$model;
		  			$scope.labl=$label;
		  			angular.element('#pendingServiceAdd').modal('show');
		    	         return false;
		    	}
		    	
		    	
		    	
		    	$scope.servicesListPopup = [];
		    	var promise = $scope.checkServiceMappedWithTest($item.serviceMasterId,$item.specialityId);
				promise.then(function(obj) 
						{
		    	             if(obj>0)
		    	           {
		    	$scope.forServicePayeeId  = $scope.selfPayeeId;
				//console.log($item);
				
				$scope.search = '';
				$('#searchServiceId').val('');
				if(parseInt($scope.contractId) == 0)
				{
				$scope.contractId = null;
				}
				var obj = {
						"admissionId" :$scope.admissionId,
						"encounterId" :$scope.admissionId,
						"contractId" :$scope.contractId,
						"serviceId" :$item.serviceMasterId,
						"currentDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
						"organizationId":$scope.organizationId,
						"unitId":$scope.unitId,
						"visitTypeId":$scope.visitTypeId,
						"billingBedCategoryId":$scope.admissionDetails.bedBillingCategoryId,
						"patientCategoryId":$scope.admissionDetails.patientCategoryId,
						"defaultSelfTariffId":$scope.defaultSelfTariffId,
					    "selfPayeeId" :$scope.selfPayeeId,
					    "patientClassId":$scope.admissionDetails.patientClassId,
					    "tariffId" :$scope.defaultSelfTariffId,
					    "quantity":1,
					    "patientId":$scope.admissionDetails.patientId,
					    "paymentEntitlementId":$scope.admissionDetails.paymentEntitlementId,
					    "taxPercentage":$item.taxPercentage,
					};
				
				//console.log('Base prise',obj)
				//return false;
				$rootScope.startLoaderPopup();
				var URI = BASE_URL + ROOT_URL + "/global/consumption/getSerivceRateGlobal";
				BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status=="success"){
						//var basePriceObj=response.data.object;
						if(response.data.object != "undefined" && response.data.object != null)
							{

								if(response.data.object.doesNotExist==null)
								{

									
									if(response.data.object != undefined && response.data.object !=null && response.data.object.serviceRate != null && response.data.object.serviceRate == 0 ){
										
										$rootScope.stopLoaderPopup();
	        							growl.warning("The rate for this service is 0.00. Please use 'Add Service' to set a valid rate before proceeding.",{
	        								title : WARNING_MSG
	        							});
	        							$scope.serviceCheckFlag = false;
	        							return false;
									}
									
									
									if(response.data.object != undefined && response.data.object !=null && response.data.object.serviceRate != null && response.data.object.serviceRate == 0 ){
										
										$rootScope.stopLoaderPopup();
	        							growl.warning("The rate for this service is 0.00. Please use 'Add Service' to set a valid rate before proceeding.",{
	        								title : WARNING_MSG
	        							});
	        							$scope.serviceCheckFlag = false;
	        							return false;
									}
									
									$scope.forServicePayeeId = response.data.object.payeeId;
									var item = response.data.object;
		    						item.quantity=1;
		    						item.isAutoRender = (item.isAutoRender == 'Y' ? 'Y':'N');
		    						item.serviceRendered = (item.isAutoRender == 'Y'?1:0);
		    						
		    						item.renderingDate=new Date();
									$scope.renderingDate.push({opened:false});
		    						item.basePrice=response.data.object.serviceRate;
		    						item.oldBasePrice =response.data.object.serviceRate;
		    						item.dummyServiceAmt=response.data.object.serviceRate;
		    						item.coPayPer=response.data.object.coShare;
		    						item.medallServiceId=$item.medallServiceId;
		    						item.coShare = response.data.object.coShare;
		    						item.selfPayable = response.data.object.selfPayable;
		    						item.creditPayable = response.data.object.creaditPayable;
		    						item.netPay = response.data.object.finalPrice;
		    						item.packageConsumptionAmt = response.data.object.packageConsumptionAmt;
		    						item.contractId = response.data.object.contractId;
		    						item.patientClassPercentage = response.data.object.patientClassPercentage;
		    						item.markupStatus = response.data.object.markupStatus;
		    						item.minRateEditable = response.data.object.minRateEditable;
		    						item.maxRateEditable = response.data.object.maxRateEditable;
		    						item.orderDetailsPayeeMapperDtosList=response.data.object.orderDetailsPayeeMapperDtosList;
		    						item.isRateEditable=response.data.object.isRateEditable;
		    						item.isQuantityEditable=response.data.object.isQuantityEditable;
		    						item.tariffId=response.data.object.tariffId;
		    						item.ordTotalAmount = response.data.object.ordTotalAmount;
		    						item.concession = response.data.object.concession;
		    						item.isDiscountApplicable = response.data.object.isDiscountApplicable;
		    						item.patientClassPercentage = response.data.object.patientClassPercentage;
		    						item.taxAmt = response.data.object.taxAmt;
		    						item.orderDetailsPackageMapperDtosList=response.data.object.orderDetailsPackageMapperDtosList;
		    						item.bedCategoryId=response.data.object.billingBedCategoryId;
		    						item.isAutoRender = (response.data.object.isAutoRender == 'Y' ? 'Y':'N');
		    						item.serviceRendered = (response.data.object.isAutoRender == 'Y'?1:0);
		    						
		    						//test
		    						var serviceId = typeof(item.serviceMasterId) != "undefined" ? item.serviceMasterId : item.serviceId;
		    			    		
		    			    		var newArr = {};
		    			            newArr.dummyServiceAmt = item.dummyServiceAmt;
		    			    		newArr.contractId = item.contractId;//Ganesh
		    			    		newArr.isTariffRateApplicable = item.isTariffRateApplicable;
		    			        	//newArr.specialityId = item.specialityId;
		    			        	newArr.serviceSpecialityId = item.specialityId;
		    			    		newArr.serviceSubSpecialityId = item.subSpecialityId;
		    			    		newArr.subSpecialityId = item.subSpecialityId;
		    			    		newArr.medallServiceId = item.medallServiceId;

		    			        	newArr.taxId = item.taxId;
		    			    		newArr.serviceMasterId = typeof(item.serviceMasterId) != "undefined" ? item.serviceMasterId : item.serviceId;
		    			    		newArr.serviceStandardCode = typeof(item.serviceStandardCode) != "undefined" ? item.serviceStandardCode  : item.serviceCode ;
		    			    		newArr.serviceStandardCode = typeof(item.isPackageService) != "undefined" && item.isPackageService == "Y" ? (newArr.serviceStandardCode + " (P)") : newArr.serviceStandardCode;
		    			    		newArr.serviceStandardName = typeof(item.serviceStandardName) != "undefined" ? item.serviceStandardName : item.serviceName;
		    			    		newArr.isDiscountApplicable = typeof(item.isDiscountApplicable) != "undefined" ? item.isDiscountApplicable : 'N';
		    			    		newArr.isDiscount = typeof(item.isDiscount) != "undefined" ? item.isDiscount : 'N';
		    			    		newArr.isQuantityEditable = typeof(item.isQuantityEditable) != "undefined" ? item.isQuantityEditable : 'N';
		    			    		newArr.isRateEditable = typeof(item.isRateEditable) != "undefined" ? item.isRateEditable : 'N';
		    			    		newArr.minRateEditable = typeof(item.minRateEditable) != "undefined" ? item.minRateEditable : 0;
		    			    		newArr.maxRateEditable = typeof(item.maxRateEditable) != "undefined" ? item.maxRateEditable : 0;
		    			    		newArr.specialityName = item.specialityName;
		    			    		newArr.subSpecialityName = item.subSpecialityName;
		    			    		newArr.isDoctorRequired = typeof(item.isDocReq) != "undefined" ?item.isDocReq:'N';
		    			    		newArr.quantity = typeof(item.quantity) != "undefined" ? item.quantity : 1;
		    			    		newArr.basePrice = typeof(item.basePrice) != "undefined" ? item.basePrice : 0;
		    			    		newArr.basePriceOld = typeof(item.basePrice) != "undefined" ? item.basePrice: 0;
		    			            newArr.oldBasePrice =item.oldBasePrice;
		    			    		newArr.totalAmt = parseFloat(newArr.quantity) * parseFloat(newArr.basePrice).toFixed(2);
		    			    		newArr.concession = typeof(item.concession) != "undefined" ? parseFloat(item.concession).toFixed(2) : parseFloat(0).toFixed(2);
		    			    		newArr.patientClassPercentage = typeof(item.patientClassPercentage) != "undefined" ? parseFloat(item.patientClassPercentage).toFixed(2) : parseFloat(0).toFixed(2);
		    			    		newArr.discountPercentage = 0;
		    			    		newArr.discount =0;
		    			    		newArr.doctorId = '';
		    			    		newArr.specialityId = item.specialityId;
		    			    		newArr.doctorSpecialityId = '';
		    			    		newArr.discountDetailsList = [];
		    			    		newArr.taxName = typeof(item.taxName) != "undefined" ? item.taxName : "-";
		    			    		newArr.taxId = typeof(item.taxId) != "undefined" ? item.taxId : null;
		    			    		newArr.taxPercentage = typeof(item.taxPercentage) != "undefined" ? item.taxPercentage : 0;
		    			    		newArr.selfPayable =  item.selfPayable;
		    			    		newArr.creditPayable = item.creditPayable;
		    			    		newArr.netPay =    item.netPay;
		    			    		//newArr.serviceRendered = "0";
		    			    		newArr.isAutoRender =  item.isAutoRender;
		    			            newArr.serviceRendered = item.serviceRendered;
		    			    		newArr.serviceRenderingDeptId = item.subSpecialityId;
		    			    		newArr.createdBy  = $scope.userId;
		    			    		newArr.createdDate = new Date().getTime();
		    			    		newArr.apportionedPrice = (typeof(item.apportionedPrice) != "undefined" && item.apportionedPrice != null) ? item.apportionedPrice : null;
		    			    		newArr.isPackageService = typeof(item.isPackageService) != "undefined" ? item.isPackageService : "";
		    			    		newArr.packageId = (typeof(item.packageId) != "undefined" && item.packageId != null) ? item.packageId : item.packageMasterId;
		    			    		newArr.packageTypeId = (typeof(item.packageTypeId) != "undefined" && item.packageTypeId != null) ? item.packageTypeId : null; 
		    			    		newArr.packageEitherOrGroupId = (typeof(item.packageEitherOrGroupId) != "undefined" && item.packageEitherOrGroupId != null) ? item.packageEitherOrGroupId : null; 
		    			    		newArr.packageCapDetailsId = item.packageCapDetailsId;
		    			    		newArr.isCapService = item.isCapService;
		    			    		newArr.packageConsumptionAmt = item.packageConsumptionAmt;
		    			    		newArr.isRefDoctorShare = item.isRefReq;
		    			    		newArr.payeeId = $scope.forServicePayeeId;
		    			    		newArr.renderingDate =   new Date().getTime();
		    			            newArr.isDoctorRequired = typeof(item.isDocReq) != "undefined" ?item.isDocReq:'N';
		    			            newArr.isTariffRateApplicable = item.isTariffRateApplicable; // 12-10-2019
		    			    		newArr.markupStatus = item.markupStatus;
		    			    		newArr.tariffId = item.tariffId;
		    			    		newArr.orderDetailsPayeeMapperDtosList = item.orderDetailsPayeeMapperDtosList;
		    			    		newArr.orderDetailsPackageMapperDtosList = item.orderDetailsPackageMapperDtosList;
		    			    		newArr.bedCategoryId = item.bedCategoryId;
		    			    		newArr.wardId = $scope.admissionDetails.wardId;
									newArr.roomId = $scope.admissionDetails.roomId;
									newArr.bedId = $scope.admissionDetails.bedId;
		    			    		$scope.servicesListPopup.push(newArr);
		    			    		$scope.serviceCheckFlag = false;
		    			    		newArr.isDummyService = ($scope.addServiceIsDummyService == true) ? 1 : 0;
					        		newArr.isHiddenService = 0;
		    						//test
		    			    		if($scope.servicesListPopup.length > 0){
		       			    		 $scope.addTempServiesToServiceMasterList();
		       			    		}
								}else{
									$rootScope.stopLoaderPopup();
        							growl.warning("Service is not mapped with the tariff..!!",{
        								title : WARNING_MSG
        							});
        							$scope.serviceCheckFlag = false;
        							return false;
								}
							}
						else
							{
								$rootScope.stopLoaderPopup();
								growl.warning("Please add base price of this service and then add..!!",{
									title : ERROR_MSG
								});
								$scope.serviceCheckFlag = false;
							return false;
							}
						//concession
						//console.log("FINAL ITEM",$item);
						
					}else
						{
						$scope.serviceCheckFlag = false;
						   $rootScope.stopLoaderPopup();
						}
				});
				
				
				
				//console.log($scope.tempServiceDetailsList);
				$scope.search = "";
				
		    	           }
		    	             else{
		    	         	   // $scope.search = '';
		    	         	 growl
		    	     			.warning(
		    	     					'Service is Not mapped with Test!!',
		    	     					{
		    	     						title : 'Warning!'
		    	     					});
		    	              }
						 }); 
				
				$scope.isFinancialClearence ='N';
		    	$scope.finanacial.financialRequestSendTo = "";
			}
		    
		    $scope.removeAllServices = function(){
		    	$scope.servicesListPopup = [];
		    }
		    
		    $scope.removeTempServicesByIndex = function(index){
				
		    	angular.forEach($scope.freeTextServiceList, function (value, key) {
					if($scope.servicesListPopup[index].freeTextOrderedServicesId!=undefined && $scope.servicesListPopup[index].freeTextOrderedServicesId!= null && $scope.servicesListPopup[index].freeTextOrderedServicesId==value.freeTextOrderedServicesId){
							value.status = 'A';
						}
					});
		    	
		    	$scope.addCapAmout($scope.servicesListPopup[index],$scope.autoRenderedServicesFinalList);
		    		
		    	$scope.totalAmountSummaryPopup = parseFloat($scope.totalAmountSummaryPopup -  ($scope.servicesListPopup[index].basePrice * $scope.servicesListPopup[index].quantity)).toFixed(2);
				$scope.servicesListPopup.splice(index, 1);
			}
		    
		    $scope.servicesListPopup = [];
		    $scope.servicePopupCalculate = function(item,index)
		    {
		    	var serviceId = typeof(item.serviceMasterId) != "undefined" ? item.serviceMasterId : item.serviceId;
		    		
		    		var newArr = {};
		            newArr.dummyServiceAmt = item.dummyServiceAmt;
		    		newArr.contractId = item.contractId;//Ganesh
		    		newArr.isTariffRateApplicable = item.isTariffRateApplicable;
		        	//newArr.specialityId = item.specialityId;
		        	newArr.serviceSpecialityId = item.specialityId;
		    		newArr.serviceSubSpecialityId = item.subSpecialityId;
		    		newArr.subSpecialityId = item.subSpecialityId;
		    		
		        	newArr.taxId = item.taxId;
		    		newArr.serviceMasterId = typeof(item.serviceMasterId) != "undefined" ? item.serviceMasterId : item.serviceId;
		    		newArr.serviceStandardCode = typeof(item.serviceStandardCode) != "undefined" ? item.serviceStandardCode  : item.serviceCode ;
		    		newArr.serviceStandardCode = typeof(item.isPackageService) != "undefined" && item.isPackageService == "Y" ? (newArr.serviceStandardCode + " (P)") : newArr.serviceStandardCode;
		    		newArr.serviceStandardName = typeof(item.serviceStandardName) != "undefined" ? item.serviceStandardName : item.serviceName;
		    		newArr.isDiscountApplicable = typeof(item.isDiscountApplicable) != "undefined" ? item.isDiscountApplicable : 'N';
		    		newArr.isDiscount = typeof(item.isDiscount) != "undefined" ? item.isDiscount : 'N';
		    		newArr.isQuantityEditable = typeof(item.isQuantityEditable) != "undefined" ? item.isQuantityEditable : 'N';
		    		newArr.isRateEditable = typeof(item.isRateEditable) != "undefined" ? item.isRateEditable : 'N';
		    		newArr.minRateEditable = typeof(item.minRateEditable) != "undefined" ? item.minRateEditable : 0;
		    		newArr.maxRateEditable = typeof(item.maxRateEditable) != "undefined" ? item.maxRateEditable : 0;
		    		newArr.specialityName = item.specialityName;
		    		newArr.subSpecialityName = item.subSpecialityName;
		    		newArr.isDoctorRequired = typeof(item.isDoctorRequired) != "undefined" ?item.isDoctorRequired:item.isDocReq;
		    		newArr.quantity = typeof(item.quantity) != "undefined" ? item.quantity : 1;
		    		newArr.basePrice = typeof(item.basePrice) != "undefined" ? item.basePrice : 0;
		    		newArr.basePriceOld = typeof(item.basePrice) != "undefined" ? item.basePrice: 0;
		            newArr.oldBasePrice =item.oldBasePrice;
		    		newArr.totalAmt = parseFloat(newArr.quantity) * parseFloat(newArr.basePrice).toFixed(2);
		    		newArr.concession = typeof(item.concession) != "undefined" ? parseFloat(item.concession).toFixed(2) : parseFloat(0).toFixed(2);
		    		newArr.patientClassPercentage = typeof(item.patientClassPercentage) != "undefined" ? parseFloat(item.patientClassPercentage).toFixed(2) : parseFloat(0).toFixed(2);
		    		newArr.discountPercentage = 0;
		    		newArr.discount =0;
		    		newArr.doctorId = '';
		    		newArr.specialityId = '';
		    		newArr.doctorSpecialityId = '';
		    		newArr.discountDetailsList = [];
		    		newArr.taxName = typeof(item.taxName) != "undefined" ? item.taxName : "-";
		    		newArr.taxId = typeof(item.taxId) != "undefined" ? item.taxId : null;
		    		newArr.taxPercentage = typeof(item.taxPercentage) != "undefined" ? item.taxPercentage : 0;
		    		newArr.selfPayable =  item.selfPayable;
		    		newArr.creditPayable = item.creditPayable;
		    		newArr.netPay =    item.netPay;
		    		//newArr.serviceRendered = "0";
		    		newArr.isAutoRender =  item.isAutoRender;
		            newArr.serviceRendered = item.serviceRendered;
		    		newArr.serviceRenderingDeptId = item.subSpecialityId;
		    		newArr.createdBy  = $scope.userId;
		    		newArr.createdDate = new Date().getTime();
		    		newArr.apportionedPrice = (typeof(item.apportionedPrice) != "undefined" && item.apportionedPrice != null) ? item.apportionedPrice : null;
		    		newArr.isPackageService = typeof(item.isPackageService) != "undefined" ? item.isPackageService : "";
		    		newArr.packageId = (typeof(item.packageId) != "undefined" && item.packageId != null) ? item.packageId : item.packageMasterId;
		    		newArr.packageTypeId = (typeof(item.packageTypeId) != "undefined" && item.packageTypeId != null) ? item.packageTypeId : null; 
		    		newArr.packageEitherOrGroupId = (typeof(item.packageEitherOrGroupId) != "undefined" && item.packageEitherOrGroupId != null) ? item.packageEitherOrGroupId : null; 
		    		newArr.packageCapDetailsId = item.packageCapDetailsId;
		    		newArr.isCapService = item.isCapService;
		    		newArr.packageConsumptionAmt = item.packageConsumptionAmt;
		    		newArr.isRefDoctorShare = item.isRefDoctorShare;
		    		newArr.payeeId = $scope.forServicePayeeId;
		            newArr.isDoctorRequired = typeof(item.isDoctorRequired) != "undefined" ?item.isDoctorRequired:'N';
		            newArr.isTariffRateApplicable = item.isTariffRateApplicable; // 12-10-2019
		    		newArr.markupStatus = item.markupStatus;
		    		newArr.tariffId = item.tariffId;
		    		newArr.orderDetailsPayeeMapperDtosList = item.orderDetailsPayeeMapperDtosList;
		    		newArr.orderDetailsPackageMapperDtosList = item.orderDetailsPackageMapperDtosList;
		    		newArr.bedCategoryId = item.bedCategoryId;
		    		newArr.wardId = item.wardId;
					newArr.roomId = item.roomId;
					newArr.bedId = item.bedId;
					newArr.freeTextOrderedServicesId = item.freeTextOrderedServicesId;
					newArr.freeTextserviceName = item.freeTextserviceName;
					newArr.isHiddenService = 1;
					newArr.isDummyService = item.isDummyService;
					if(index != undefined){
						newArr.renderingDate =  new Date(item.renderingDate).getTime(); 
						$scope.servicesListPopup[index] = newArr;
						
					}else{
						newArr.renderingDate =   new Date().getTime();
						$scope.servicesListPopup.push(newArr);
						
					}
		    		
		    		
		    		$scope.servicesListPopup = $scope.servicesListPopup.reverse();
		    		
		    	    $scope.servicePopupCalculateFinal($scope.servicesListPopup); 
		    }
		    
		    
		    $scope.getQytCal = function(itemObj,idx)
		    {
		    	//console.log("OBJ",itemObj);
		    	
		    	 var i =0;
		    	   angular.forEach($scope.initialAutoRenderedServicesFinalList, function (value, key) {
			   			if(value.orderDetailsId == itemObj.orderDetailsId){
			   				idx = i;
			   			 }
		   			i++;
		   			});
		    	   $scope.autoRenderedServicesFinalList =[]; 
		    	   $scope.autoRenderedServicesFinalList = $scope.initialAutoRenderedServicesFinalList;
		    	   
		        if(itemObj.quantity == '' || itemObj.quantity == undefined || itemObj.quantity == 0 || itemObj.quantity == '.')
				{
		            if(itemObj.quantity == 0)
		            {
		                growl.warning("Quantity cannot be zero..!!",{
		                    title : WARNING_MSG
		                });
		            }
		            itemObj.quantity = 1;

				}
		    	
						if($scope.requestStatusObject != null)
						{
							  $scope.getCancelDiscountReqForDiscount();
							
						}

				        itemObj.quantity = itemObj.quantity.replace(".", "");

						//ServiceCount
						$scope.ServiceCount=0;
						angular.forEach($scope.autoRenderedServicesFinalList,function(value,index){
//							if(value.isPackageService == 'N')
//							{
								$scope.ServiceCount=$scope.ServiceCount + parseInt(value.quantity);
//							}
							});
						
						$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(itemObj.concession);
						$scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);
						
						$scope.autoRenderedServicesFinalList[idx].basePrice = parseFloat(itemObj.basePrice);
						
						//$scope.autoRenderedServicesFinalList[idx].discount = parseFloat(itemObj.discount);
			    		$scope.autoRenderedServicesFinalList[idx].discountPercentage = parseFloat(itemObj.discountPercentage);
			    		
			    		var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
			    		 $scope.autoRenderedServicesFinalList[idx].discount =((parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[idx].concession))*(isNaN(itemObj.discountPercentage) ? 0: itemObj.discountPercentage)/100).toFixed(2);
				        //$scope.autoRenderedServicesFinalList[idx].discountPercentage = isNaN(((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * (parseFloat(itemObj.basePrice)- $scope.autoRenderedServicesFinalList[idx].concession)))*100).toFixed(2)) ? parseFloat(0).toFixed(2) : ((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100).toFixed(2);
				       // $scope.autoRenderedServicesFinalList[idx].discount = parseFloat(itemObj.discount).toFixed(2);
				        var withConDic =parseFloat(totalAmt) - (parseFloat(itemObj.concession) + parseFloat($scope.autoRenderedServicesFinalList[idx].discount));
				        
				    	var txpre = 0;
						if(itemObj.taxPercentage != null || itemObj.taxPercentage > 0){
							$scope.autoRenderedServicesFinalList[idx].taxPercentage = itemObj.taxPercentage;
							txpre = itemObj.taxPercentage;
						}else{
								$scope.autoRenderedServicesFinalList[idx].taxPercentage = 0;
								}
						$scope.autoRenderedServicesFinalList[idx].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
						$scope.autoRenderedServicesFinalList[idx].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[idx].taxAmt)).toFixed(2);
						$scope.autoRenderedServicesFinalList[idx].selfPayable = $scope.autoRenderedServicesFinalList[idx].netPay;
						$scope.autoRenderedServicesFinalList[idx].creditPayable =parseFloat(0);
						
						$scope.autoRenderedServicesForSaveAndUpdate=[];
			        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
			        	$scope.addOrUpdateServices(idx,null);
						//$scope.saveOrderPackage();
				     /*}*/

				$scope.billWiseDiscountObj = {
						// "discountGivenBy":"",
						"discountGivenBy":$scope.userId,
						"discountCategoryId" : "",
						"discountAmountBillWise" : "",
						"discountPercentageBillWise" : "",
						"approvedAmountBillWise":"",
						"requestUserId":"",
						"remark":'',
						"requestAmountBillWise" : 0,
				}
				$scope.requestDiscountDiv = false;
				$scope.isFinancialClearence ='N';
		    	$scope.finanacial.financialRequestSendTo = "";
		    }

		    
		    $scope.servicePopupCalculateFinal = function(itemObj)
		    {
		    	$scope.AutoRenderedTotalAmountSummaryPopUp = 0;
		    	angular.forEach($scope.servicesListPopup, function (value, key) {
					
					$scope.AutoRenderedTotalAmountSummaryPopUp=(parseFloat($scope.AutoRenderedTotalAmountSummaryPopUp) + parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
					
				});
				$scope.totalAmountSummaryPopup = $scope.AutoRenderedTotalAmountSummaryPopUp;
		    }
		
		    $scope.addOrUpdateServices = function(index,indexList)
			{
		    	$scope.billRemark = $scope.admissionDetails.remark;
		    	var finalIndexList = [];
		    	var containsPharmacyService = false;
				if(index!=undefined)
					finalIndexList.push(index);
				
				if(indexList!=undefined)
					finalIndexList = indexList;
		    	
		    	$rootScope.startLoaderPopup();
		        $scope.isPatientClass = 'N';

				var data={
						patientId :$scope.admissionDetails.patientId,
						//encounterId :$scope.admissionDetails.admissionId,
						admissionId :$scope.admissionDetails.admissionId,
						createdBy :$scope.userId,
						createdDate :new Date().getTime(),
						updatedBy :$scope.userId,
						updatedDate :new Date().getTime(),
						orderDate :new Date().getTime(),
						organisationId :$scope.organizationId,
						unitId :$scope.unitId,
						isFinalBill : $scope.finalBill,
						ordDoctorId : $scope.admissionDetails.doctorId,
						visitTypeId : $scope.visitTypeId,
						billDateTime : new Date().getTime(),
						netAmount : parseFloat(parseFloat($scope.netPayableSummary)).toFixed(2),
						discountAmount : $scope.totalDiscountSummary,
						concessionAmount : $scope.totalConcessionSummary,
						gst : $scope.totalGstTaxSummary,
						totalBillAmount : $scope.totalAmountSummary,
						roundOff : $scope.roundOffAmt,
						isCreditBill:'N',
						billCancelled : 'N',
						isFinancialClearance : 'N',
						billRemark : $scope.billRemark,
						billStatusId : '1', // 1. Pending,
						selfPayable : $scope.totalSelfPayble,
						creditPayable : $scope.totalCreditPayble,
						cancelledDiscountsList : $scope.removeDiscountList,
						tariffId : $scope.defaultSelfTariffId,
						paymentEntitlementId : $scope.admissionDetails.paymentEntitlementId,
						visitTypeId : $scope.visitTypeId,
						patientTypeId : $scope.admissionDetails.patientCategoryId,
						billingBedCategoryId :$scope.admissionDetails.bedBillingCategoryId, 
						isInterimBill :$scope.interimBill,
						isDrNote:"N",
						contractId:$scope.contractId,
						counterId:$rootScope.counterId,
						isInterimOrFinalBill : $scope.interimBill == 'Y' ? 1 : $scope.finalBill == 'Y' ? 2 : null,
						encounterSpecialityId:$scope.admissionDetails.specialityId,
						lineLevelDiscountOrderList : [],
						patientClassId : $scope.admissionDetails.patientClassId,
						pharmacyServiceId:$scope.pharmacyServiceId,
						referralId : $scope.admissionDetails.referralId,
						selfPayeeId : $scope.selfPayeeId,
						applyAuth : $scope.applyAuth!=null ? $scope.applyAuth : false,
						needToUpdatePharmacyService : $scope.needToUpdatePharmacyService!=null ? $scope.needToUpdatePharmacyService : false,
						updatePayee : $scope.updatePayee!=null ? $scope.updatePayee : false,
						isPreBilling :$scope.isPreOrPostBilling,
						capExist : $scope.capExist,
						packageExist : ($scope.listPackageOrder.length>0)? true : false,
						isBillManipulationAllowed : $scope.isBillManipulationAllowed
				   };
				
				var billPayeeList =[];
				if($scope.totalSelfPayble > 0)
				{
					var billPayeeArr ={
							payeeId : $scope.selfPayeeId,
							billAmount : $scope.totalSelfPayble
					}
					billPayeeList.push(billPayeeArr);
				}
				if($scope.totalCreditPayble > 0)
				{
					var billPayeeArr ={
							payeeId : $scope.encounterDetails.payeeId,
							billAmount : $scope.totalCreditPayble
					}
					billPayeeList.push(billPayeeArr);
				}
				
				data.billPayeeDetailsDtosList = billPayeeList;
				var saveOrderDetailsObj = [];
				angular.forEach($scope.autoRenderedServicesForSaveAndUpdate, function (value, key) {
					
					if(value.serviceMasterId==$scope.pharmacyServiceId)
						containsPharmacyService = true;
					
					var saveArr = {};
					if(value.orderDetailsId > 0){
						saveArr.orderDetailsId = value.orderDetailsId;
					}
					saveArr.discountOwnership = value.discountOwnership;
		            saveArr.isFromServiceSerach = value.isFromServiceSerach;
					saveArr.isOrderVisibleDepartment = value.isOrderVisibleDepartment;
					if(value.isOrderVisibleDepartment == undefined || value.isOrderVisibleDepartment == null || value.isOrderVisibleDepartment == ''){
						saveArr.isOrderVisibleDepartment = 'A';
		               }
					saveArr.isConsultationService = value.isConsultationService;
					saveArr.ordRemarks = value.ordRemarks;
					saveArr.nursingServiceOrderFlag =value.nursingServiceOrderFlag;
					saveArr.markupStatus = value.markupStatus;
					saveArr.patientClassPercentage = value.patientClassPercentage;
					saveArr.orgId = $scope.organizationId;
					saveArr.orgUnitId  = $scope.unitId;
					saveArr.orderId = value.orderId;
					saveArr.orderQty = value.quantity;
					saveArr.serviceId = value.serviceMasterId;
					saveArr.priorityId = value.priorityId;
					saveArr.isOutsourced = value.isOutsourced;
					saveArr.orderDate =   (value.orderDate !=null && value.orderDate!=undefined && value.orderDate!='')?value.orderDate:new Date().getTime();
					saveArr.orderSchDate = new Date().getTime();
					saveArr.serviceAmt = value.basePrice;
					saveArr.dummyServiceAmt = value.dummyServiceAmt;
					saveArr.ordConcession = value.concession;
					saveArr.ordDiscount = value.discount;
					saveArr.ordDocSplId = value.specialityId;
					saveArr.taxId = value.taxId;
					saveArr.taxPer  = value.taxPercentage;
					saveArr.taxAmount = value.taxAmt;
					//saveArr.payeeId = $scope.encounterDetails.payeeId;
					saveArr.payeeId = value.payeeId;
					if(value.tariffId != undefined && value.tariffId != null){
						saveArr.tariffId = value.tariffId;
					}else{
						saveArr.tariffId = $scope.defaultSelfTariffId;
					}
					
					saveArr.billingClassId = '1';
					saveArr.ordDocId = value.doctorId;
					saveArr.serviceRendered = value.serviceRendered;
					saveArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
					saveArr.ordRenderDatetime = value.ordRenderDatetime!=null ? new Date(value.ordRenderDatetime).getTime() : new Date().getTime();
					saveArr.serviceIsBilled = '0';
					//saveArr.status = value.status;
					if($scope.surchargeServiceId == value.serviceMasterId){
						saveArr.status = 'I';
					}
					else{
						saveArr.status = value.status;
					}
					
					saveArr.updatedBy = $scope.userId;
					saveArr.updatedDate =new Date().getTime();
					saveArr.createdBy  = value.createdBy;
					saveArr.createdDate  = value.createdDate;
					saveArr.oldNetPay =   isNaN(value.oldNetPay) ? 0 : parseFloat(value.oldNetPay).toFixed(2); 
					saveArr.ordCancelled = value.ordCancelled;
		            saveArr.procedureOrderMstId = value.procedureOrderMstId > 0 ? value.procedureOrderMstId  : null;
		            saveArr.isProcedureService = value.isProcedureService!=null ? value.isProcedureService  : 'N';
		            saveArr.isPostedWhileDischarge = value.isPostedWhileDischarge!=null ? value.isPostedWhileDischarge : 'N';
		            saveArr.isChargedHalfDay = value.isChargedHalfDay;
					saveArr.ordCancelReasonId  = value.ordCancelReasonId;
					saveArr.ordCancelDatetime  = value.ordCancelDatetime;
					saveArr.ordCancelRemark  = value.ordCancelRemark;
					saveArr.oldOrdDtlId = value.oldOrdDtlId;
					saveArr.serviceAmt =  value.basePrice;
					saveArr.ordTotalAmt = parseFloat(saveArr.serviceAmt) * parseFloat(value.quantity);
					saveArr.taxPer  = value.taxPercentage;
					saveArr.discountCategoryId = value.discountCategoryId;
					var withConDic =parseFloat(parseFloat(saveArr.ordTotalAmt) - parseFloat(value.concession) - parseFloat(value.discount)).toFixed(2);
					saveArr.taxAmount = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
					saveArr.netAmount =   parseFloat(parseFloat(withConDic) +  parseFloat(saveArr.taxAmount)).toFixed(2);
					saveArr.dummyServiceAmt = value.dummyServiceAmt;
					saveArr.ordConcession = value.concession;
					saveArr.ordDiscount = value.discount;
		            saveArr.ordDiscountPer = value.discountPercentage;
					saveArr.isPackage = typeof(value.isPackage) != "undefined" ? value.isPackage : false;
					saveArr.selfPayable =   saveArr.netAmount;
					saveArr.creditPayable = 0;
					saveArr.oldCreditPayable = 0;
					saveArr.oldSelfPayable = 0;
					saveArr.taxId = value.taxId;
					saveArr.isInvestigationOrder = value.isInvestigationOrder;
		            saveArr.isDiscountApplicable = value.isDiscountApplicable;
		            saveArr.isDiscount = value.isDiscount;
					saveArr.orderEmergencyFlag = 'N';
					saveArr.isDrug = 'N';
					saveArr.packageId = typeof(value.packageMasterId) != "undefined" && value.packageMasterId != null ? value.packageMasterId : value.packageId;
					saveArr.packageTypeId = typeof(value.packageTypeId) != "undefined" ? value.packageTypeId : null;
					saveArr.isPackageService = typeof(value.isPackageService) != "undefined" ? value.isPackageService : 'N';
					saveArr.apportionedPrice = typeof(value.apportionedPrice) != "undefined" ? value.apportionedPrice : null;
					saveArr.isCapService = value.isCapService;
					saveArr.packageConsumptionAmt = value.packageConsumptionAmt;
					saveArr.departmentId = value.serviceSpecialityId!=null? value.serviceSpecialityId:value.specialityId;
					saveArr.subDepartmentId = value.serviceSubSpecialityId;
					saveArr.isRefDoctorShare = value.isRefDoctorShare;
					saveArr.isDocReq =  value.isDocReq!=null &&  value.isDocReq!=undefined ? value.isDocReq :value.isDoctorRequired;
					saveArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
                    saveArr.isPeriodicityService = value.isPeriodicityService;
					saveArr.ordDocSplId = value.doctorSpecialityId;
					saveArr.serviceRenderingDeptId = value.subSpecialityId; 
					saveArr.encounterId = $scope.admissionDetails.admissionId;
					saveArr.admissionId = $scope.admissionDetails.admissionId;
					saveArr.contractId = value.contractId;
					saveArr.bedCategoryId = value.bedCategoryId;
					saveArr.wardId = value.wardId;
					saveArr.roomId = value.roomId;
					saveArr.bedId = value.bedId;
					saveArr.discountDetailsList = [];
					saveArr.isFromAddservice = value.isFromAddservice;
					saveArr.debitNoteService = false;
					saveArr.isDummyService = value.isDummyService;
					saveArr.isHiddenService = value.isHiddenService;
					  //package force add
					saveArr.forceAddRemoved = value.forceAddRemoved;
					saveArr.isConsumed = value.isConsumed;			
					saveArr.isLabService = $scope.labConfig.labSpecialityId==saveArr.departmentId ? 'Y' : 'N';
					if(value.discountDetailsList == null)
						{
						value.discountDetailsList = [];
						}
					    saveArr.saleId = value.saleId;
						saveArr.orderDetailsPayeeMapperDtosList  = value.orderDetailsPayeeMapperDtosList;
						saveArr.orderDetailsPackageMapperDtosList  = value.orderDetailsPackageMapperDtosList;
						saveArr.freeTextOrderedServicesId = value.freeTextOrderedServicesId
					    saveOrderDetailsObj.push(saveArr);
					    
					    if(saveArr.isPackage && saveArr.packageTypeId==1)
				    	{
					    	angular.forEach($scope.autoRenderedServicesFinalList, function (ehcPackageList, packKey) 
			    			{
					    		var ehcPackageListObj = {};
					    		angular.forEach(ehcPackageList.orderDetailsPackageMapperDtosList, function (pkgValue, key) 
				    			{
			    					if(value.packageId==pkgValue.packageId)
			    					{	
			    						ehcPackageListObj.orderDetailsId = ehcPackageList.orderDetailsId;
			    						ehcPackageListObj.ordTotalAmt = parseFloat(ehcPackageList.basePrice) * parseFloat(ehcPackageList.quantity);
			    						
			    						if(ehcPackageList.status == 'A' && ehcPackageList.isDiscount == 'Y')
			    						{
			    							ehcPackageListObj.patientClassPercentage = value.patientClassPercentage;
				    						ehcPackageListObj.ordDiscountPer = value.discountPercentage;
			    							ehcPackageListObj.ordConcession = value.patientClassPercentage != 0 ? parseFloat(ehcPackageListObj.ordTotalAmt * value.patientClassPercentage*0.01).toFixed(2) : 0;
			    							ehcPackageListObj.ordDiscount = value.discountPercentage != 0 ? parseFloat(((ehcPackageListObj.ordTotalAmt-ehcPackageListObj.ordConcession) * value.discountPercentage*0.01)).toFixed(2) : 0;;
			    						}else{
			    							ehcPackageListObj.ordConcession = 0.00;
			    							ehcPackageListObj.ordDiscount =0.00;
			    							ehcPackageListObj.patientClassPercentage = 0.00;
				    						ehcPackageListObj.ordDiscountPer = 0.00;
			    						}
			    						var totalPackageAmt = ehcPackageListObj.ordTotalAmt - (ehcPackageListObj.ordConcession + ehcPackageListObj.ordDiscount);
			    						ehcPackageListObj.packageConsumptionAmt = totalPackageAmt;
			    						pkgValue.packageAmount = totalPackageAmt;
			    						finalIndexList.push(packKey);
			    						
			    						ehcPackageListObj.discountOwnership = ehcPackageList.discountOwnership;
			    			            ehcPackageListObj.isFromServiceSerach = ehcPackageList.isFromServiceSerach;
			    						ehcPackageListObj.isOrderVisibleDepartment = ehcPackageList.isOrderVisibleDepartment;
			    						ehcPackageListObj.isConsultationService = ehcPackageList.isConsultationService;
			    						ehcPackageListObj.ordRemarks = ehcPackageList.ordRemarks;
			    						ehcPackageListObj.nursingServiceOrderFlag =ehcPackageList.nursingServiceOrderFlag;
			    						ehcPackageListObj.markupStatus = ehcPackageList.markupStatus;
			    						ehcPackageListObj.patientClassPercentage = ehcPackageList.patientClassPercentage;
			    						ehcPackageListObj.orgId = $scope.organizationId;
			    						ehcPackageListObj.orgUnitId  = $scope.unitId;
			    						ehcPackageListObj.orderId = ehcPackageList.orderId;
			    						ehcPackageListObj.orderQty = ehcPackageList.quantity;
			    						ehcPackageListObj.serviceId = ehcPackageList.serviceMasterId;
			    						ehcPackageListObj.priorityId = ehcPackageList.priorityId;
			    						ehcPackageListObj.isOutsourced = ehcPackageList.isOutsourced;
			    						ehcPackageListObj.orderDate =   (ehcPackageList.orderDate !=null && ehcPackageList.orderDate!=undefined && ehcPackageList.orderDate!='')?ehcPackageList.orderDate:new Date().getTime();
			    						ehcPackageListObj.orderSchDate = new Date().getTime();
			    						ehcPackageListObj.serviceAmt = ehcPackageList.basePrice;
			    						ehcPackageListObj.dummyServiceAmt = ehcPackageList.dummyServiceAmt;
			    						ehcPackageListObj.ordDocSplId = ehcPackageList.specialityId;
			    						ehcPackageListObj.taxId = ehcPackageList.taxId;
			    						ehcPackageListObj.taxPer  = ehcPackageList.taxPercentage;
			    						ehcPackageListObj.taxAmount = ehcPackageList.taxAmt;
			    						//ehcPackageListObj.payeeId = $scope.encounterDetails.payeeId;
			    						ehcPackageListObj.payeeId = ehcPackageList.payeeId;
			    						ehcPackageListObj.tariffId = ehcPackageList.tariffId;
			    						ehcPackageListObj.billingClassId = '1';
			    						ehcPackageListObj.ordDocId = ehcPackageList.doctorId;
			    						ehcPackageListObj.serviceRendered = ehcPackageList.serviceRendered;
			    						ehcPackageListObj.serviceRenderingDeptId = ehcPackageList.serviceRenderingDeptId;
			    						ehcPackageListObj.ordRenderDatetime = ehcPackageList.ordRenderDatetime!=null ? new Date(ehcPackageList.ordRenderDatetime).getTime() : new Date().getTime();
			    						ehcPackageListObj.serviceIsBilled = '0';
			    						//ehcPackageListObj.status = ehcPackageList.status;
			    						if($scope.surchargeServiceId == ehcPackageList.serviceMasterId){
			    							ehcPackageListObj.status = 'I';
			    						}
			    						else{
			    							ehcPackageListObj.status = ehcPackageList.status;
			    						}
			    						ehcPackageListObj.updatedBy = $scope.userId;
			    						ehcPackageListObj.updatedDate =new Date().getTime();
			    						ehcPackageListObj.createdBy  = ehcPackageList.createdBy;
			    						ehcPackageListObj.createdDate  = ehcPackageList.createdDate;
                                        ehcPackageListObj.oldNetPay =   isNaN(ehcPackageList.oldNetPay )|| (ehcPackageList.oldNetPay<0 ) ? 0 : parseFloat(ehcPackageList.oldNetPay).toFixed(2); 
			    						ehcPackageListObj.ordCancelled = ehcPackageList.ordCancelled;
			    			            ehcPackageListObj.procedureOrderMstId = ehcPackageList.procedureOrderMstId > 0 ? ehcPackageList.procedureOrderMstId  : null;
			    			            ehcPackageListObj.isProcedureService = ehcPackageList.isProcedureService!=null ? ehcPackageList.isProcedureService  : 'N';
			    			            ehcPackageListObj.isPostedWhileDischarge = ehcPackageList.isPostedWhileDischarge!=null ? ehcPackageList.isPostedWhileDischarge : 'N';
			    			            ehcPackageListObj.isChargedHalfDay = ehcPackageList.isChargedHalfDay;
			    			            ehcPackageListObj.ordCancelReasonId  = ehcPackageList.ordCancelReasonId;
			    						ehcPackageListObj.ordCancelDatetime  = ehcPackageList.ordCancelDatetime;
			    						ehcPackageListObj.ordCancelRemark  = ehcPackageList.ordCancelRemark;
			    						ehcPackageListObj.oldOrdDtlId = ehcPackageList.oldOrdDtlId;
			    						ehcPackageListObj.serviceAmt =  ehcPackageList.basePrice;
			    						ehcPackageListObj.taxPer  = ehcPackageList.taxPercentage;
			    						ehcPackageListObj.discountCategoryId = ehcPackageList.discountCategoryId;
			    						ehcPackageListObj.taxAmount = 0.00;
			    						ehcPackageListObj.netAmount =  0.00;
			    						ehcPackageListObj.dummyServiceAmt = ehcPackageList.dummyServiceAmt;
			    						ehcPackageListObj.isPackage = typeof(ehcPackageList.isPackage) != "undefined" ? ehcPackageList.isPackage : false;
			    						ehcPackageListObj.selfPayable =   ehcPackageListObj.netAmount;
			    						ehcPackageListObj.creditPayable = 0;
			    						ehcPackageListObj.oldCreditPayable = 0;
			    						ehcPackageListObj.oldSelfPayable = 0;
			    						ehcPackageListObj.taxId = ehcPackageList.taxId;
			    						ehcPackageListObj.isInvestigationOrder = ehcPackageList.isInvestigationOrder;
			    			            ehcPackageListObj.isDiscountApplicable = ehcPackageList.isDiscountApplicable;
			    			            ehcPackageListObj.isDiscount = ehcPackageList.isDiscount;
			    						ehcPackageListObj.orderEmergencyFlag = 'N';
			    						ehcPackageListObj.isDrug = 'N';
			    						ehcPackageListObj.packageId = typeof(ehcPackageList.packageMasterId) != "undefined" && ehcPackageList.packageMasterId != null ? ehcPackageList.packageMasterId : ehcPackageList.packageId;
			    						ehcPackageListObj.packageTypeId = typeof(ehcPackageList.packageTypeId) != "undefined" ? ehcPackageList.packageTypeId : null;
			    						ehcPackageListObj.isPackageService = typeof(ehcPackageList.isPackageService) != "undefined" ? ehcPackageList.isPackageService : 'N';
			    						ehcPackageListObj.apportionedPrice = typeof(ehcPackageList.apportionedPrice) != "undefined" ? ehcPackageList.apportionedPrice : null;
			    						ehcPackageListObj.isCapService = ehcPackageList.isCapService;
			    						ehcPackageListObj.departmentId = ehcPackageList.serviceSpecialityId!=null? ehcPackageList.serviceSpecialityId:ehcPackageList.specialityId;
			    						ehcPackageListObj.subDepartmentId = ehcPackageList.serviceSubSpecialityId;
			    						ehcPackageListObj.isRefDoctorShare = ehcPackageList.isRefDoctorShare;
			    						ehcPackageListObj.isDocReq =  ehcPackageList.isDocReq!=null &&  ehcPackageList.isDocReq!=undefined ? ehcPackageList.isDocReq :ehcPackageList.isDoctorRequired;
			    						ehcPackageListObj.packageEitherOrGroupId = ehcPackageList.packageEitherOrGroupId;
			    	                    ehcPackageListObj.isPeriodicityService = ehcPackageList.isPeriodicityService;
			    						ehcPackageListObj.ordDocSplId = ehcPackageList.doctorSpecialityId;
			    						ehcPackageListObj.serviceRenderingDeptId = ehcPackageList.subSpecialityId; 
			    						ehcPackageListObj.encounterId = $scope.admissionDetails.admissionId;
			    						ehcPackageListObj.admissionId = $scope.admissionDetails.admissionId;
			    						ehcPackageListObj.contractId = ehcPackageList.contractId;
			    						ehcPackageListObj.bedCategoryId = ehcPackageList.bedCategoryId;
			    						ehcPackageListObj.wardId = ehcPackageList.wardId;
			    						ehcPackageListObj.roomId = ehcPackageList.roomId;
			    						ehcPackageListObj.bedId = ehcPackageList.bedId;
			    						ehcPackageListObj.discountDetailsList = [];
			    						ehcPackageListObj.isFromAddservice = ehcPackageList.isFromAddservice;
			    						ehcPackageListObj.debitNoteService = false;
			    						ehcPackageListObj.isDummyService = value.isDummyService;
			    						ehcPackageListObj.isHiddenService = value.isHiddenService;
			    						  //package force add
			    						ehcPackageListObj.forceAddRemoved = ehcPackageList.forceAddRemoved;
			    						ehcPackageListObj.isConsumed = ehcPackageList.isConsumed;			
			    						ehcPackageListObj.isLabService = $scope.labConfig.labSpecialityId==ehcPackageListObj.departmentId ? 'Y' : 'N';
			    						if(ehcPackageList.discountDetailsList == null)
			    						{
			    							ehcPackageList.discountDetailsList = [];
			    						}
			    						ehcPackageListObj.saleId = ehcPackageList.saleId;
			    						ehcPackageListObj.orderDetailsPayeeMapperDtosList  = ehcPackageList.orderDetailsPayeeMapperDtosList;
			    						ehcPackageListObj.orderDetailsPackageMapperDtosList  = ehcPackageList.orderDetailsPackageMapperDtosList;
			    						saveOrderDetailsObj.push(ehcPackageListObj);
			    					}
				                 });
			    				 
			    			 });
				    	}
					    
					    
				    });
				var uniqueServiceArray=[];
								saveOrderDetailsObj.filter(function(item){
									if(item.orderDetailsId!=undefined && item.orderDetailsId!=null && item.orderDetailsId>0)
										{
						              var i = uniqueServiceArray.findIndex(x => (x.orderDetailsId == item.orderDetailsId));
						              if(i <= -1){
						            	  uniqueServiceArray.push(item);
						              }
										}
									else
										{
										uniqueServiceArray.push(item);
										}
						            });
								saveOrderDetailsObj=uniqueServiceArray;
								var uniqueArray = finalIndexList.filter(function(item, pos, self) {
								    return self.indexOf(item) == pos;
								})
								
								finalIndexList=uniqueArray;
				               data.orderDetailsMasterDtosList = saveOrderDetailsObj;
				               //console.log("save order detais",saveOrderDetailsObj);
				              
				               
				               $scope.forceAddOrRemoveForRecalPackage = $scope.forceAddOrRemove;
				               
				               var URI = null;
				               if($scope.forceAddOrRemove==false)
				                   URI = BASE_URL + ROOT_URL +'/api/billing/update/service';
				               else
				                   URI = BASE_URL + ROOT_URL +'/api/billing/forceAddOrRemove';

								BillingGenericService.serviceAction(METHOD_POST,URI, data , NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
								{
									if(response.data.status == 'success' && response.data.listObject.length > 0)
									{
										$scope.needToUpdatePharmacyService = false;
									 	$scope.updatePayee = false;
		                                $scope.applyAuth = false;
		                                if(finalIndexList!=undefined && finalIndexList!=null)
		            					{
		                                	
		                                	//console.log(index);
		            						if(index == null){
		            							//$scope.autoRenderedServicesFinalList[finalIndexList[0]].orderDetailsId = response.data.listObject[0].orderDetailsId;
		            							var tmpIdx = 0;
		            							angular.forEach(response.data.listObject, function (val1, key1) 
		            									{
				            								if(finalIndexList[tmpIdx] != undefined){
																  $scope.autoRenderedServicesFinalList[finalIndexList[tmpIdx]].orderDetailsId = response.data.listObject[tmpIdx].orderDetailsId;
															}
		            								       tmpIdx++;
		            									});
		            						}
		            						
		            						finalIndexList = [];
	            							
	            							angular.forEach(response.data.listObject, function (val, key) 
	            									{
	            								finalIndexList.push($scope.autoRenderedServicesFinalList.findIndex(x => x.orderDetailsId === response.data.listObject[key].orderDetailsId));
	            									});
	            							
                                            console.log(finalIndexList);
                                            
		            						
		            						$scope.reassignValuesAndReCalculate(response.data.listObject,finalIndexList);

		            						var checkMapperDetails = false;
		            						angular.forEach(response.data.listObject, function (val, key)
                                            {
                                                if(val.orderDetailsPackageMapperDtosList == null ||  val.orderDetailsPackageMapperDtosList.length == 0){
                                                    checkMapperDetails = true;
                                                }
                                            });

                                            if(checkMapperDetails){
                                                getOrderedServices();
                                            }

		            					}else{

		            						getOrderedServices();
		            						
		            						$scope.cancelService = false;
		            					}
		                                //getOrderedServices();
		                                $scope.setFirstTabLevel(1);
										//getMedicationIssueListByAdmissionId(self.model.encounterDetails.encounterId);
		                                $scope.isPatientClass = 'N';
		                                $scope.autoRenderedServicesForSaveAndUpdate=[];
										$rootScope.stopLoaderPopup();
					                     if(index !=undefined &&  index != null && ($scope.autoRenderedServicesFinalList[index].discount==undefined || $scope.autoRenderedServicesFinalList[index].discount==null || $scope.autoRenderedServicesFinalList[index].discount==0))
				                         {
				                         $scope.autoRenderedServicesFinalList[index].discountCategoryId=null;
				                         }
										if(containsPharmacyService){
											getMedicationIssueListByAdmissionId(self.model.encounterDetails.encounterId);
										}
										
										 if($scope.forceAddOrRemoveForRecalPackage==true && containsPharmacyService){
											 growl.warning("Package is Recalculating",
									    	    		{
									    					title : WARNING_MSG
									    				});
											 $timeout(function () { 
												 $scope.recalculateServiceCharges(true);	
											}, 2000); 
											 
							               }
										 
										 $scope.forceAddOrRemove = false;
                                         growl.success(response.data.message,
                                         {
                                             title : 'success'
                                         });
									}
									else if(response.data.status == 'error')
									{
										 getOrderedServices();
										 $scope.isPatientClass = 'N';
										 $rootScope.stopLoaderPopup();
										 $scope.forceAddOrRemove = false;
										 $scope.cancelService = false;

                                         growl.error(response.data.message,
                                         {
                                             title : 'error'
                                         });
									}
								});
								
			}
		    
		    $scope.reassignValuesAndReCalculate = function(list,finalIndexList)
			{
		    	var indexT = $scope.autoRenderedServicesFinalList.findIndex(record => record.serviceMasterId === $scope.surchargeServiceId);
				if(indexT > 0)
				{
					var indexObj = $scope.autoRenderedServicesFinalList[indexT];
					$scope.autoRenderedServicesFinalList.splice(indexT, 1);
					
					if(indexObj.orderDetailsId!=undefined && indexObj.orderDetailsId!=null && indexObj.orderDetailsId>0)
						$scope.addCapAmout(indexObj,$scope.autoRenderedServicesFinalList);
						
				}
		    	var count = 0;
				angular.forEach(list, function (value, key) 
				{
					var index = finalIndexList[count];
					angular.forEach(value.orderDetailsPackageMapperDtosList, function (value1, key) 
						{
								value1.serviceAmount = parseFloat(value1.serviceAmount).toFixed(2);
								$scope.autoRenderedServicesFinalList[index].orderDetailsId = value.orderDetailsId > 0 ? value.orderDetailsId : null;
								$scope.autoRenderedServicesFinalList[index].tariffId = value.tariffId > 0 ? value.tariffId : $scope.defaultSelfTariffId;
								$scope.autoRenderedServicesFinalList[index].medallServiceId = value.medallServiceId != null ? value.medallServiceId : '';
								$scope.autoRenderedServicesFinalList[index].isInvestigationOrder = (value.isInvestigationOrder=='Y')?'Y':'N';
								$scope.autoRenderedServicesFinalList[index].orderId = value.orderId > 0 ? value.orderId : null;
								$scope.autoRenderedServicesFinalList[index].orderDate = value.orderDate != null ? value.orderDate : '';
								$scope.autoRenderedServicesFinalList[index].orderNo = value.orderNo != null ? value.orderNo : '';
								$scope.autoRenderedServicesFinalList[index].bedCategoryId = value.bedCategoryId;
								$scope.autoRenderedServicesFinalList[index].isFromServiceSerach = value.isFromServiceSerach!= null ? value.isFromServiceSerach : 'N';
								if(value.isDoctorRequired == null || value.isDoctorRequired == undefined)
								{
								   $scope.autoRenderedServicesFinalList[index].isDoctorRequired  = 'N';
								}
								else
								{
								  $scope.autoRenderedServicesFinalList[index].isDoctorRequired  = value.isDoctorRequired;
								}
								
								$scope.autoRenderedServicesFinalList[index].docSharePer = value.docSharePer !=null ? parseFloat(value.docSharePer).toFixed(2) : 0;
								$scope.autoRenderedServicesFinalList[index].hospitalShare = value.hospitalShare !=null ? parseFloat(value.hospitalShare).toFixed(2) : 0;
								$scope.autoRenderedServicesFinalList[index].refDocSharePer = value.refDocSharePer !=null ? parseFloat(value.refDocSharePer).toFixed(2) : 0;
								$scope.autoRenderedServicesFinalList[index].discountOwnership = value.discountOwnership > 0 && (value.concession!=0 || value.discount!=0)? value.discountOwnership : $scope.defaultDiscountOwnership;
								$scope.autoRenderedServicesFinalList[index].isOrderVisibleDepartment = value.isOrderVisibleDepartment;
								$scope.autoRenderedServicesFinalList[index].isConsultationService = value.isConsultationService;
								$scope.autoRenderedServicesFinalList[index].ordRemarks = value.ordRemarks;
								$scope.autoRenderedServicesFinalList[index].nursingServiceOrderFlag =value.nursingServiceOrderFlag;
								$scope.autoRenderedServicesFinalList[index].dummyServiceAmt = value.dummyServiceAmt;
								$scope.autoRenderedServicesFinalList[index].priorityId  = value.priorityId;
								$scope.autoRenderedServicesFinalList[index].isOutsourced = value.isOutsourced;
							    $scope.autoRenderedServicesFinalList[index].specialityId = value.specialityId;
								$scope.autoRenderedServicesFinalList[index].taxId = value.taxId;
								$scope.autoRenderedServicesFinalList[index].ordCancelled = value.ordCancelled != null ? value.ordCancelled : 'N';
								$scope.autoRenderedServicesFinalList[index].ordCancelReasonId = value.ordCancelReasonId != null ? value.ordCancelReasonId  : '';
								$scope.autoRenderedServicesFinalList[index].ordCancelDatetime = value.ordCancelDatetime != null ? value.ordCancelDatetime  : '';
								$scope.autoRenderedServicesFinalList[index].ordCancelRemark = value.ordCancelRemark != null ? value.ordCancelRemark  : '';
								$scope.autoRenderedServicesFinalList[index].patientClassPercentage = value.patientClassPercentage != null ? value.patientClassPercentage  : 0.00;
				        		$scope.autoRenderedServicesFinalList[index].markupStatus =value.markupStatus != null ? value.markupStatus  : false;
								$scope.autoRenderedServicesFinalList[index].status  = value.status;
								$scope.autoRenderedServicesFinalList[index].serviceRendered = value.serviceRendered != null ? value.serviceRendered  : '0';
								$scope.autoRenderedServicesFinalList[index].oldOrdDtlId = value.oldOrdDtlId != null ? value.oldOrdDtlId  : null;
								$scope.autoRenderedServicesFinalList[index].isRateEditable = (value.isRateEditable == 'Y')?'Y':'N';	
								$scope.autoRenderedServicesFinalList[index].isRateEditable = ((value.packageConsumptionAmt>0 && $scope.autoRenderedServicesFinalList[index].isRateEditable=='Y') || value.serviceMasterId==$scope.pharmacyServiceId )?'N':value.isRateEditable;	
								//$scope.autoRenderedServicesFinalList[index].isRateEditable = value.isPackage == true?'N':$scope.autoRenderedServicesFinalList[index].isRateEditable;
								$scope.autoRenderedServicesFinalList[index].procedureOrderMstId = value.procedureOrderMstId > 0 ? value.procedureOrderMstId  : null;
								$scope.autoRenderedServicesFinalList[index].isProcedureService = value.isProcedureService!=null ? value.isProcedureService  : 'N';
								$scope.autoRenderedServicesFinalList[index].isPostedWhileDischarge = value.isPostedWhileDischarge!=null ? value.isPostedWhileDischarge : 'N';
								$scope.autoRenderedServicesFinalList[index].isChargedHalfDay = value.isChargedHalfDay;
								$scope.autoRenderedServicesFinalList[index].doctorSpecialityId=value.doctorSpecialityId; 
								////console.log("docSpe",value.doctorSpecialityId );
								////console.log("spe",value.specialityId);
								$scope.autoRenderedServicesFinalList[index].isRefDoctorShare = value.isRefDoctorShare != null ? value.isRefDoctorShare  : null;
								$scope.autoRenderedServicesFinalList[index].contractId = ((value.contractId != null || value.contractId > 0) ? value.contractId  : null);
								$scope.autoRenderedServicesFinalList[index].doctorId = value.doctorId==null?0:value.doctorId;
							    $scope.autoRenderedServicesFinalList[index].serviceSpecialityId = value.serviceSpecialityId;
								$scope.autoRenderedServicesFinalList[index].serviceSubSpecialityId = value.serviceSubSpecialityId;
								$scope.autoRenderedServicesFinalList[index].isAutoRender =  value.isAutoRender;
								$scope.autoRenderedServicesFinalList[index].serviceRendered = value.serviceRendered;
								$scope.autoRenderedServicesFinalList[index].serviceRenderingDeptId = value.serviceRenderingDeptId;
								$scope.autoRenderedServicesFinalList[index].oldNetPay = value.oldNetPay;
								$scope.autoRenderedServicesFinalList[index].discountDetailsList = value.discountDetailsList; 
								$scope.autoRenderedServicesFinalList[index].serviceMasterId = value.serviceMasterId;
								$scope.autoRenderedServicesFinalList[index].serviceStandardCode = value.serviceStandardCode;
								$scope.autoRenderedServicesFinalList[index].serviceStandardName = value.serviceStandardName;
								$scope.autoRenderedServicesFinalList[index].totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
								$scope.autoRenderedServicesFinalList[index].isDiscount = value.isDiscountApplicable;
								$scope.autoRenderedServicesFinalList[index].isDiscountApplicable = value.packageConsumptionAmt!=0 ? 'N':value.isDiscountApplicable; // isDiscountApplicable
		                        if(value.serviceMasterId == $scope.surchargeServiceId){
		                            $scope.autoRenderedServicesFinalList[index].isDiscountApplicable = 'N';
		                        }
								if(value.packageConsumptionAmt>0 || value.serviceMasterId==$scope.pharmacyServiceId){
									$scope.autoRenderedServicesFinalList[index].isQuantityEditable = 'N';
									$scope.autoRenderedServicesFinalList[index].isRateEditable = 'N';
								}else{
									$scope.autoRenderedServicesFinalList[index].isRateEditable = $scope.autoRenderedServicesFinalList[index].isRateEditable;
									$scope.autoRenderedServicesFinalList[index].isQuantityEditable = value.isQuantityEditable;
								}
								$scope.autoRenderedServicesFinalList[index].isTariffRateApplicable = value.isTariffRateApplicable;// 12-10-2019
								$scope.autoRenderedServicesFinalList[index].minRateEditable = value.minRateEditable;
								$scope.autoRenderedServicesFinalList[index].maxRateEditable = value.maxRateEditable;
								$scope.autoRenderedServicesFinalList[index].specialityName = value.specialityName;
								$scope.autoRenderedServicesFinalList[index].doctorName = value.doctorName;
								$scope.autoRenderedServicesFinalList[index].discountDisabledFlag = 'N';
								$scope.autoRenderedServicesFinalList[index].isPackage = value.isPackage;
								$scope.autoRenderedServicesFinalList[index].packageMasterId = value.packageMasterId;
								$scope.autoRenderedServicesFinalList[index].packageTypeId = value.packageTypeId;
								$scope.autoRenderedServicesFinalList[index].isPackageService = value.isPackageService;
								$scope.autoRenderedServicesFinalList[index].apportionedPrice = typeof(value.apportionedPrice) != "undefined" ? value.apportionedPrice : null;
								$scope.autoRenderedServicesFinalList[index].isCapService = value.isCapService;
				    			$scope.autoRenderedServicesFinalList[index].packageConsumptionAmt = value.packageConsumptionAmt;
				    			$scope.autoRenderedServicesFinalList[index].doctorSpecialityId !=undefined && $scope.autoRenderedServicesFinalList[index].doctorSpecialityId >0 ? $scope.autoRenderedServicesFinalList[index].docSpecialityList=$scope.specialityMasterList : $scope.autoRenderedServicesFinalList[index].docSpecialityList = [];
				    			
				    			if(value.doctorSpecialityList == undefined || value.doctorSpecialityList ==null){
				    				if( value.doctorId != null && $scope.getInitiallySpecialityList.length>0){
				    					var specialityLsit=[];
					                    angular.forEach($scope.getInitiallySpecialityList, function (service, key) 
					                            {
					                                if(service.doctorId==value.doctorId)
					                                    {
					                                    specialityLsit.push(service);
					                                    }
								                    
					                            });
					                    
					                    value.doctorSpecialityList=[];
					                    value.doctorSpecialityList = specialityLsit;
				    				}
				    				   
				    			}
			                 
			                    
				    			$scope.autoRenderedServicesFinalList[index].doctorSpecialityList = (value.doctorSpecialityList != undefined ? value.doctorSpecialityList : []);
		                        $scope.autoRenderedServicesFinalList[index].docList=value.docList != null ? value.docList:$scope.doctorsList;
				    			$scope.autoRenderedServicesFinalList[index].isFromAddservice = (value.isFromAddservice != null || value.isFromAddservice != '' || value.isFromAddservice != undefined ? value.isFromAddservice :'N') ;
				    			$scope.autoRenderedServicesFinalList[index].isDummyService = value.isDummyService;
				    			$scope.autoRenderedServicesFinalList[index].isHiddenService = value.isHiddenService;
				    			if(value.subSpecialityId==undefined||value.subSpecialityId==null)
				    				{
				    				  $scope.autoRenderedServicesFinalList[index].subSpecialityId = value.serviceRenderingDeptId;
				    				}
				    			else{
				    			     $scope.autoRenderedServicesFinalList[index].subSpecialityId = value.subSpecialityId;
				    			    }
				    			$scope.autoRenderedServicesFinalList[index].packageId = value.packageId;
								if(value.specialityName == null)
								{
									$scope.autoRenderedServicesFinalList[index].specialityName = "";
									$scope.autoRenderedServicesFinalList[index].doctorName = "";
								}
								$scope.autoRenderedServicesFinalList[index].quantity = value.quantity;
								$scope.autoRenderedServicesFinalList[index].basePrice = parseFloat(value.basePrice).toFixed(2);
								$scope.autoRenderedServicesFinalList[index].basePriceRateChange = parseFloat(value.basePrice).toFixed(2);
								if(parseFloat(value.packageConsumptionAmt) > 0 && value1.isPackageService == 'N'){
									$scope.autoRenderedServicesFinalList[index].basePriceRateChange = parseFloat(value1.serviceAmount).toFixed(2);
								}
								$scope.autoRenderedServicesFinalList[index].coPayPer = parseFloat(value.coPayPer).toFixed(2);
								$scope.autoRenderedServicesFinalList[index].basePriceOld = value.basePrice;
								$scope.autoRenderedServicesFinalList[index].oldBasePrice = value.oldBasePrice;
								$scope.autoRenderedServicesFinalList[index].concession = parseFloat(value.concession).toFixed(2);
								$scope.autoRenderedServicesFinalList[index].patientClassPercentage = parseFloat(value.patientClassPercentage).toFixed(2);
								$scope.autoRenderedServicesFinalList[index].patientClassPercentageChange = parseFloat(value.patientClassPercentage).toFixed(2);
								$scope.autoRenderedServicesFinalList[index].discountCategoryId = value.discountCategoryId != null ? value.discountCategoryId.toString()  : null;
								$scope.autoRenderedServicesFinalList[index].discountPercentage = value.discountPercentage;
								$scope.autoRenderedServicesFinalList[index].discount = value.discount;
								$scope.autoRenderedServicesFinalList[index].packageEitherOrGroupId = value.packageEitherOrGroupId;
								$scope.autoRenderedServicesFinalList[index].orderDateString = value.orderDateString;
								$scope.autoRenderedServicesFinalList[index].orderDateTimeString = value.orderDateTimeString;
								$scope.autoRenderedServicesFinalList[index].orderDateTime = value.orderDateTime;
								var dis = parseFloat(value.discount).toFixed(2);
								var disPer =value.discountPercentage;
								if(value.discount == null || value.discount == 0 || value.discount == '')
									{
										$scope.autoRenderedServicesFinalList[index].discountPercentage = 0;
										$scope.autoRenderedServicesFinalList[index].discount = 0;
										dis = 0;
										disPer = 0;
										$scope.autoRenderedServicesFinalList[index].discountDisabledFlag = 'Y';
									}
									
								if(value.discount != 0 && (value.discountPercentage == null || value.discountPercentage == 0 || value.discountPercentage == '')){
									$scope.autoRenderedServicesFinalList[index].discountPercentage = isNaN(((parseFloat(value.discount)/(parseFloat(value.quantity) * parseFloat(value.basePrice) - parseFloat(value.concession)))*100).toFixed(2)) ? parseFloat(0).toFixed(2) : ((parseFloat(value.discount)/(parseFloat(value.quantity) * parseFloat(value.basePrice) - parseFloat(value.concession)))*100).toFixed(2);
									}
								
									if(value.discountDetailsList == null)
									{
										value.discountDetailsList = [];
										$scope.autoRenderedServicesFinalList[index].discountDetailsList = [];
									}
								
								$scope.autoRenderedServicesFinalList[index].taxName = value.taxName;
								var txpre = 0;
								if(value.taxPercentage != null || value.taxPercentage > 0){
									$scope.autoRenderedServicesFinalList[index].taxPercentage = value.taxPercentage;
									txpre = value.taxPercentage;
								}
								else
									{
									 $scope.autoRenderedServicesFinalList[index].taxPercentage = 0;
									}
								
								
								/*var totalAmt = (parseFloat(value.quantity) * parseFloat(value1.serviceAmount)).toFixed(2);*/
								var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
								totalAmt  = parseFloat(totalAmt) - value.packageConsumptionAmt;
								var concession = (parseFloat(value.concession).toFixed(2));
								var withConDic =parseFloat(totalAmt - (parseFloat(concession) + parseFloat(dis))).toFixed(2);
								$scope.autoRenderedServicesFinalList[index].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
								var gstAmt =  parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
								$scope.autoRenderedServicesFinalList[index].taxAmtOld = value.taxAmtOld;
								$scope.autoRenderedServicesFinalList[index].selfPayableOldC = value.selfPayableOldC;
								$scope.autoRenderedServicesFinalList[index].creditPayableOldC = value.creditPayableOldC;
								$scope.autoRenderedServicesFinalList[index].netPay =   parseFloat(value.netPay).toFixed(2);
								$scope.autoRenderedServicesFinalList[index].ordTotalAmount =   parseFloat(value.totalAmt).toFixed(2);
								var selfPayableV =   "0";
								var creditPayableV =   "0";
								
									if(value.payeeId == $scope.selfPayeeId || value.payeeId == null )
									{
									      $scope.autoRenderedServicesFinalList[index].selfPayable =parseFloat(value.selfPayable).toFixed(2);
									      $scope.autoRenderedServicesFinalList[index].creditPayable =parseFloat(value.creditPayable).toFixed(2);
									      selfPayableV =  parseFloat($scope.autoRenderedServicesFinalList[index].selfPayable).toFixed(2);
									      creditPayableV =  parseFloat($scope.autoRenderedServicesFinalList[index].creditPayable).toFixed(2);
									}else{
										
										$scope.autoRenderedServicesFinalList[index].payeeId= value.payeeId;
										    if(value.selfPayable>0)
											{
										    	
										    	if(parseInt(dis) > 0 )
										    		{
										    		$scope.autoRenderedServicesFinalList[index].selfPayable =   parseFloat(value.selfPayable).toFixed(2);
										            selfPayableV =   parseFloat(value.selfPayable).toFixed(2);
										    		}
										    	else
										    		{
										    		 $scope.autoRenderedServicesFinalList[index].selfPayable =parseFloat(value.selfPayableOldC).toFixed(2);
													 selfPayableV =   parseFloat(value.selfPayableOldC).toFixed(2);
										    		}
										    
											
											$scope.autoRenderedServicesFinalList[index].creditPayable =parseFloat(value.creditPayable).toFixed(2);
											creditPayableV=parseFloat(value.creditPayable).toFixed(2);
											}
										else{
											$scope.autoRenderedServicesFinalList[index].selfPayable = (value.selfPayable==null||value.selfPayable==undefined||value.selfPayable=="")?0:value.selfPayable;
											selfPayableV =   parseFloat(value.selfPayable).toFixed(2);
										   
										    $scope.autoRenderedServicesFinalList[index].creditPayable  = value.creditPayable;
										    creditPayableV = parseFloat(value.creditPayable).toFixed(2);
										    
										   }
										
										}
								$scope.autoRenderedServicesFinalList[index].saleId = value.saleId;
							    $scope.autoRenderedServicesFinalList[index].orderDetailsPayeeMapperDtosList = value.orderDetailsPayeeMapperDtosList;
							    $scope.autoRenderedServicesFinalList[index].orderDetailsPackageMapperDtosList = value.orderDetailsPackageMapperDtosList;
							    $scope.autoRenderedServicesFinalList[index].isPeriodicityService = value.periodicityServiceOrderId!=null ? 'Y' : 'N' ;
							    $scope.autoRenderedServicesFinalList[index].forceAddRemoved = value.forceAddRemoved;
							    $scope.autoRenderedServicesFinalList[index].isConsumed = value.isConsumed;
								$scope.autoRenderedServicesFinalList[index].createdBy = value.createdBy;
								$scope.autoRenderedServicesFinalList[index].createdDate = value.createdDate;
								$scope.autoRenderedServicesFinalList[index].discountRateChange = parseFloat($scope.autoRenderedServicesFinalList[index].discount).toFixed(2);
								$scope.autoRenderedServicesFinalList[index].discountPercentageRateChange = parseFloat($scope.autoRenderedServicesFinalList[index].discountPercentage).toFixed(2);
								$scope.autoRenderedServicesFinalList[index].conccesionPriceRateChange = parseFloat($scope.autoRenderedServicesFinalList[index].concession).toFixed(2);
								$scope.autoRenderedServicesFinalList[index].patClsPerPriceRateChange = parseFloat($scope.autoRenderedServicesFinalList[index].patientClassPercentage).toFixed(2);
								$scope.autoRenderedServicesFinalList[index].userName = value.userName;
								$scope.autoRenderedServicesFinalList[index].orderDateTime = value.orderDateTime;
								$scope.autoRenderedServicesFinalList[index].createdBy = value.createdBy;
								$scope.autoRenderedServicesFinalList[index].doctorForServiceList = value.doctorList != null ? value.doctorList :value.doctorForServiceList;
								$scope.autoRenderedServicesFinalList[index].hospitalShareNew=parseFloat((value.netPay)-(parseFloat(gstAmt!=null && gstAmt!=undefined ?gstAmt:0)
										+parseFloat(value.docSharePer!=null?value.docSharePer:0)
										+parseFloat(value.refDocSharePer!=null?value.refDocSharePer:0))).toFixed(2);
						 });// /end service loop
					count++;
					});
				if(count==list.length)
					$scope.resetTotalAmounts();
				
			}
		    
		    
		    $scope.resetTotalAmounts = function()
			{
				
		    	var indexT = $scope.autoRenderedServicesFinalList.findIndex(record => record.serviceMasterId === $scope.surchargeServiceId);
				if(indexT > 0)
				{
					var indexObj = $scope.autoRenderedServicesFinalList[indexT];
					$scope.autoRenderedServicesFinalList.splice(indexT, 1);
					
					if(indexObj.orderDetailsId!=undefined && indexObj.orderDetailsId!=null && indexObj.orderDetailsId>0)
						$scope.addCapAmout(indexObj,$scope.autoRenderedServicesFinalList);
				}
		    	
				$scope.AutoRenderedTotalAmountSummary=0.00;
				$scope.AutoRenderedTotalConcessionSummary=0.00;
				$scope.AutoRenderednetPayableSummary=0.00;
				$scope.AutoRenderednetTotalDiscountSummary=0.00;
				$scope.AutoRenderednetTotalTax = 0.00;
				$scope.AutoRenderednetTotalDiscount = 0.00;
				$scope.AutoRenderednetTotalCreditPayble=0.00;
				$scope.AutoRenderednetTotalSelfPayble= 0.00;
				$scope.AutoRenderednetTotalPackagePayble= 0.00;
				$scope.totalDisServiceWise  = 0.00;
				$scope.totalAmtApplicableForDiscount  = 0.00;
				angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) 
						{
							
									if(value.isPackageService=='N' && value.status=='A')
									{
										if(	$scope.ambulanceChargesServiceId != undefined && $scope.ambulanceChargesServiceId != null  && $scope.ambulanceChargesServiceId  == value.serviceId){
											
											$scope.AutoRenderedTotalAmountSummary=(parseFloat($scope.AutoRenderedTotalAmountSummary) + parseFloat(value.quantity) * parseFloat(value.ordTotalAmount)).toFixed(2);
											
										}else{
											$scope.AutoRenderedTotalAmountSummary=(parseFloat($scope.AutoRenderedTotalAmountSummary) + parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
										}
										
										$scope.AutoRenderedTotalConcessionSummary=(parseFloat($scope.AutoRenderedTotalConcessionSummary) + (parseFloat(value.concession))).toFixed(2);
										$scope.AutoRenderednetPayableSummary=(parseFloat($scope.AutoRenderednetPayableSummary) + (parseFloat( parseFloat(value.netPay)))).toFixed(2);
										$scope.AutoRenderednetTotalDiscountSummary=(parseFloat($scope.AutoRenderednetTotalDiscountSummary) + parseFloat(0)).toFixed(2);
										$scope.AutoRenderednetTotalTax = (parseFloat($scope.AutoRenderednetTotalTax) +  (parseFloat(value.taxAmt))).toFixed(2);
										$scope.AutoRenderednetTotalDiscount = (parseFloat($scope.AutoRenderednetTotalDiscount) + parseFloat(value.discount)).toFixed(2);
										$scope.AutoRenderednetTotalCreditPayble=(parseFloat($scope.AutoRenderednetTotalCreditPayble) + parseFloat(value.creditPayable)).toFixed(2);
										$scope.AutoRenderednetTotalSelfPayble= (parseFloat($scope.AutoRenderednetTotalSelfPayble)+ parseFloat(value.selfPayable)).toFixed(2);
										$scope.AutoRenderednetTotalPackagePayble= (parseFloat($scope.AutoRenderednetTotalPackagePayble)+ parseFloat(value.packageConsumptionAmt)).toFixed(2);
									}
									if(value.isDiscountApplicable == 'Y' && value.status == 'A')
			                    	{
			                    		$scope.totalDisServiceWise  = parseFloat($scope.totalDisServiceWise) + ((parseFloat(value.quantity) * parseFloat(value.basePrice)));
			                    		$scope.totalAmtApplicableForDiscount = parseFloat($scope.totalAmtApplicableForDiscount) + ((parseFloat(value.quantity) * parseFloat(value.basePrice)));
			                    	}
								
						});
				
				$scope.totalAmountSummary=(parseFloat($scope.AutoRenderedTotalAmountSummary)).toFixed(2);
				$scope.totalConcessionSummary=(parseFloat($scope.AutoRenderedTotalConcessionSummary)).toFixed(2);
				$scope.netPayableSummary=(parseFloat($scope.AutoRenderednetPayableSummary)).toFixed(2);
				$scope.totalDiscountSummary=(parseFloat($scope.AutoRenderednetTotalDiscount)).toFixed(2);
				$scope.totalGstTaxSummary=(parseFloat($scope.AutoRenderednetTotalTax)).toFixed(2);
				
				if($scope.billWiseDiscountObj !=null && $scope.billWiseDiscountObj.discountPercentageBillWise >0 && ($scope.cancelService == true || $scope.forceAddOrRemove == true))
					$scope.billWiseDiscountObj.discountAmountBillWise = (($scope.totalAmtApplicableForDiscount - $scope.totalConcessionSummary)*($scope.billWiseDiscountObj.discountPercentageBillWise * 0.01));
				else if ($scope.cancelService == true || $scope.forceAddOrRemove == true)
					$scope.billWiseDiscountObj.discountAmountBillWise = $scope.totalDiscountSummary;
				
				
				$scope.billWiseDiscountObj.discountAmountBillWise = isNaN(parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)) ? parseFloat(0) : (parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)).toFixed(2);
				var amt =   isNaN(Number(parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)))?0:$scope.billWiseDiscountObj.discountAmountBillWise;
				if(amt>0 && $scope.AutoRenderednetTotalDiscount>0 && $scope.isBillWiseDiscountApplied == true)
				{
				  $scope.roundOffAmt = parseFloat(amt-$scope.AutoRenderednetTotalDiscount).toFixed(2);
				}	
				$scope.totalCreditPayble=(parseFloat($scope.AutoRenderednetTotalCreditPayble)).toFixed(2);
				$scope.totalSelfPayble=(parseFloat($scope.AutoRenderednetTotalSelfPayble)).toFixed(2);
				$scope.totalSelfPaybleTemp=(parseFloat($scope.AutoRenderednetTotalSelfPayble)).toFixed(2);
				$scope.totalPackagePaybleTemp=(parseFloat($scope.AutoRenderednetTotalPackagePayble)).toFixed(2);
				
                if($scope.roundOffAmt!=0)
                {
                    if($scope.totalSelfPayble!=0)
                    {
                        $scope.totalSelfPayble = parseFloat(Number(parseFloat($scope.totalSelfPayble).toFixed(2))-parseFloat($scope.roundOffAmt)).toFixed(2);
                        $scope.totalSelfPaybleTemp = parseFloat(Number(parseFloat($scope.totalSelfPaybleTemp).toFixed(2))-parseFloat($scope.roundOffAmt)).toFixed(2);
                    }else{
                        $scope.totalCreditPayble = parseFloat(Number(parseFloat($scope.totalCreditPayble).toFixed(2))-parseFloat($scope.roundOffAmt)).toFixed(2);
                    }
                    $scope.netPayableSummary = parseFloat(Number(parseFloat($scope.netPayableSummary).toFixed(2))-parseFloat($scope.roundOffAmt)).toFixed(2);
                }

				$scope.forceAddOrRemove = false;
				$scope.cancelService = false;
				
				$scope.loadDataForFilterDummyServices();
			}
		    
		    
		    $scope.uncheckButton = function(){
		    	$scope.finalBill = "N";
				$scope.interimBill = "N";
		    }
		    
		    $scope.saveOrderPackage = function(forceInout)
			{
		    	$scope.billRemark = $scope.admissionDetails.remark;
				$rootScope.startLoaderPopup();
		         $scope.isPatientClass = 'N';
		         if($scope.filterFlag==true)
		        	 {
		        	 $scope.autoRenderedServicesFinalList = $scope.initialAutoRenderedServicesFinalList;
		        	 $scope.recordDisplay=0;
		        	 setInterval(function() {
						 var rowCount = $('#isTableReady tr').length;
					        if(rowCount<10)
					        	{
					        	$scope.recordDisplay+=1;
					        	rowCount=rowCount+$scope.recordDisplay;
					        	}
					    }, 2000);
		        	 }
				var data={
						patientId :$scope.admissionDetails.patientId,
						//encounterId :$scope.admissionDetails.admissionId,
						admissionId :$scope.admissionDetails.admissionId,
						createdBy :$scope.userId,
						createdDate :new Date().getTime(),
						updatedBy :$scope.userId,
						updatedDate :new Date().getTime(),
						orderDate :new Date().getTime(),
						organisationId :$scope.organizationId,
						unitId :$scope.unitId,
						isFinalBill : $scope.finalBill,
						ordDoctorId : $scope.admissionDetails.doctorId,
						visitTypeId : $scope.visitTypeId,
						billDateTime : new Date().getTime(),
						netAmount : parseFloat(parseFloat($scope.netPayableSummary)).toFixed(2),
						discountAmount : $scope.totalDiscountSummary,
						concessionAmount : $scope.totalConcessionSummary,
						gst : $scope.totalGstTaxSummary,
						totalBillAmount : $scope.totalAmountSummary,
						roundOff : $scope.roundOffAmt,
						isCreditBill:'N',
						billCancelled : 'N',
						isFinancialClearance : 'N',
						billRemark : $scope.billRemark,
						billStatusId : '1', // 1. Pending,
						selfPayable : $scope.totalSelfPayble,
						creditPayable : $scope.totalCreditPayble,
						cancelledDiscountsList : $scope.removeDiscountList,
						tariffId : $scope.defaultSelfTariffId,
						paymentEntitlementId : $scope.admissionDetails.paymentEntitlementId,
						visitTypeId : $scope.visitTypeId,
						patientTypeId : $scope.admissionDetails.patientCategoryId,
						billingBedCategoryId :$scope.admissionDetails.bedBillingCategoryId, 
						isInterimBill :$scope.interimBill,
						isDrNote:"N",
						contractId:$scope.contractId,
						counterId:$rootScope.counterId,
						isInterimOrFinalBill : $scope.interimBill == 'Y' ? 1 : $scope.finalBill == 'Y' ? 2 : null,
						encounterSpecialityId:$scope.admissionDetails.specialityId,
						lineLevelDiscountOrderList : [],
						patientClassId : $scope.admissionDetails.patientClassId,
						pharmacyServiceId:$scope.pharmacyServiceId,
						referralId : $scope.admissionDetails.referralId,
						selfPayeeId : $scope.selfPayeeId,
						applyAuth : $scope.applyAuth!=null ? $scope.applyAuth : false,
						needToUpdatePharmacyService : $scope.needToUpdatePharmacyService!=null ? $scope.needToUpdatePharmacyService : false,
						updatePayee : $scope.updatePayee!=null ? $scope.updatePayee : false,
						isPreBilling :$scope.isPreOrPostBilling, 
						recalculation : $scope.recalculation,
						isBillManipulationAllowed : $scope.isBillManipulationAllowed
				   };
				if($scope.updatePayee){
					data.discountAmount="0.0";
					
					if($scope.requestStatusObject != null){
						$scope.getCancelDiscountReqForDiscount();
						}
					$scope.billWiseDiscountObj = {
							// "discountGivenBy":"",
							"discountGivenBy":$scope.userId,
							"discountCategoryId" : "",
							"discountAmountBillWise" : "",
							"discountPercentageBillWise" : "",
							"approvedAmountBillWise":"",
							"requestUserId":"",
							"remark":'',
							"requestAmountBillWise" : 0,
					}
					$scope.requestDiscountDiv = false;
				//	$scope.totalDiscountSummary=0.0;
				}
				var billPayeeList =[];
				if($scope.totalSelfPayble > 0)
					{
						var billPayeeArr ={
								payeeId : $scope.selfPayeeId,
								billAmount : $scope.totalSelfPayble
						}
						billPayeeList.push(billPayeeArr);
					}
				if($scope.totalCreditPayble > 0)
					{
						var billPayeeArr ={
								payeeId : $scope.encounterDetails.payeeId,
								billAmount : $scope.totalCreditPayble
						}
						billPayeeList.push(billPayeeArr);
					}
				
				data.billPayeeDetailsDtosList = billPayeeList;
				var saveOrderDetailsObj = [];
				angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
		          /*  if($scope.surchargeServiceId != value.serviceMasterId){*/
					
					var saveArr = {};
					if(value.orderDetailsId > 0){
						saveArr.orderDetailsId = value.orderDetailsId;
					}
					saveArr.discountOwnership = value.discountOwnership;
		            saveArr.isFromServiceSerach = value.isFromServiceSerach;
					saveArr.isOrderVisibleDepartment = value.isOrderVisibleDepartment;
					saveArr.isConsultationService = value.isConsultationService;
					saveArr.ordRemarks = value.ordRemarks;
					saveArr.nursingServiceOrderFlag =value.nursingServiceOrderFlag;
					saveArr.markupStatus = value.markupStatus;
					saveArr.patientClassPercentage = value.patientClassPercentage;
					saveArr.orgId = $scope.organizationId;
					saveArr.orgUnitId  = $scope.unitId;
					saveArr.orderId = value.orderId;
					saveArr.orderQty = value.quantity;
					saveArr.serviceId = value.serviceMasterId;
					saveArr.priorityId = value.priorityId;
					saveArr.isOutsourced = value.isOutsourced;
					saveArr.orderDate =   (value.orderDate !=null && value.orderDate!=undefined && value.orderDate!='')?value.orderDate:new Date().getTime();
					saveArr.orderSchDate = new Date().getTime();
					saveArr.serviceAmt = value.basePrice;
					saveArr.dummyServiceAmt = value.dummyServiceAmt;
					saveArr.ordConcession = value.concession;
					saveArr.ordDiscount = value.discount;
					saveArr.ordDocSplId = value.specialityId;
					saveArr.taxId = value.taxId;
					saveArr.taxPer  = value.taxPercentage;
					saveArr.taxAmount = value.taxAmt;
					//saveArr.payeeId = $scope.encounterDetails.payeeId;
					saveArr.payeeId = value.payeeId;
					saveArr.tariffId = value.tariffId;
					saveArr.billingClassId = '1';
					saveArr.ordDocId = value.doctorId;
					saveArr.serviceRendered = value.serviceRendered;
					saveArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
					saveArr.ordRenderDatetime = value.ordRenderDatetime!=null ? new Date(value.ordRenderDatetime).getTime() : new Date().getTime();
					saveArr.serviceIsBilled = '0';
					//saveArr.status = value.status;
					if($scope.surchargeServiceId == value.serviceMasterId){
						saveArr.status = 'I';
					}
					else{
						saveArr.status = value.status;
					}
					
					saveArr.updatedBy = $scope.userId;
					saveArr.updatedDate =new Date().getTime();
					saveArr.createdBy  = value.createdBy;
					saveArr.createdDate  = value.createdDate;
					saveArr.oldNetPay =   isNaN(value.oldNetPay) ? 0 : parseFloat(value.oldNetPay).toFixed(2); 
					saveArr.ordCancelled = value.ordCancelled;
		            saveArr.procedureOrderMstId = value.procedureOrderMstId > 0 ? value.procedureOrderMstId  : null;
		            saveArr.isProcedureService = value.isProcedureService!=null ? value.isProcedureService  : 'N';
		            saveArr.isPostedWhileDischarge = value.isPostedWhileDischarge!=null ? value.isPostedWhileDischarge : 'N';
		            saveArr.isChargedHalfDay = value.isChargedHalfDay;
					saveArr.ordCancelReasonId  = value.ordCancelReasonId;
					saveArr.ordCancelDatetime  = value.ordCancelDatetime;
					saveArr.ordCancelRemark  = value.ordCancelRemark;
					saveArr.oldOrdDtlId = value.oldOrdDtlId;
					saveArr.serviceAmt =  value.basePrice;
					saveArr.ordTotalAmt = parseFloat(saveArr.serviceAmt) * parseFloat(value.quantity);
					saveArr.taxPer  = value.taxPercentage;
					saveArr.discountCategoryId = value.discountCategoryId;
					var withConDic =parseFloat(parseFloat(saveArr.ordTotalAmt) - parseFloat(value.concession) - parseFloat(value.discount)).toFixed(2);
					saveArr.taxAmount = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
					saveArr.netAmount =   parseFloat(parseFloat(withConDic) +  parseFloat(saveArr.taxAmount)).toFixed(2);
					saveArr.dummyServiceAmt = value.dummyServiceAmt;
					saveArr.ordConcession = value.concession;
					saveArr.ordDiscount = value.discount;
		            saveArr.ordDiscountPer = value.discountPercentage;
					saveArr.isPackage = typeof(value.isPackage) != "undefined" ? value.isPackage : false;
					//saveArr.selfPayable = saveArr.isPackage?value.selfPayable:saveArr.netAmount;
					//saveArr.creditPayable = saveArr.isPackage?value.creditPayable:0;
					saveArr.selfPayable =   value.packageTypeId==1 &&  !saveArr.isPackage ?  0 : saveArr.netAmount;
					//saveArr.selfPayable =   saveArr.netAmount;
					//saveArr.creditPayable = saveArr.isPackage?value.creditPayable:0;
					saveArr.creditPayable = 0;
					saveArr.oldCreditPayable = 0;
					saveArr.oldSelfPayable = 0;
					saveArr.taxId = value.taxId;
					saveArr.isInvestigationOrder = value.isInvestigationOrder;
		            saveArr.isDiscountApplicable = value.isDiscountApplicable;
		            saveArr.isDiscount = value.isDiscount;
					saveArr.orderEmergencyFlag = 'N';
					saveArr.isDrug = 'N';

					//saveArr.isPackage = typeof(value.isPackage) != "undefined" ? value.isPackage : false;

					saveArr.packageId = typeof(value.packageMasterId) != "undefined" && value.packageMasterId != null ? value.packageMasterId : value.packageId;
					saveArr.packageTypeId = typeof(value.packageTypeId) != "undefined" ? value.packageTypeId : null;
					saveArr.isPackageService = typeof(value.isPackageService) != "undefined" ? value.isPackageService : 'N';
					saveArr.apportionedPrice = typeof(value.apportionedPrice) != "undefined" ? value.apportionedPrice : null;
					saveArr.isCapService = value.isCapService;
					saveArr.packageConsumptionAmt = value.packageConsumptionAmt;
					saveArr.departmentId = value.serviceSpecialityId!=null? value.serviceSpecialityId:value.specialityId;
					saveArr.subDepartmentId = value.serviceSubSpecialityId;
					saveArr.isRefDoctorShare = value.isRefDoctorShare;
					saveArr.isDocReq =  value.isDocReq!=null &&  value.isDocReq!=undefined ? value.isDocReq :value.isDoctorRequired;
					saveArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
                    saveArr.isPeriodicityService = value.isPeriodicityService;
					saveArr.ordDocSplId = value.doctorSpecialityId;
					saveArr.serviceRenderingDeptId = value.subSpecialityId; 
					saveArr.encounterId = $scope.admissionDetails.admissionId;
					saveArr.admissionId = $scope.admissionDetails.admissionId;
					saveArr.contractId = value.contractId;
					saveArr.bedCategoryId = value.bedCategoryId;
					saveArr.wardId = value.wardId;
					saveArr.roomId = value.roomId;
					saveArr.bedId = value.bedId;
					saveArr.discountDetailsList = [];
					saveArr.isFromAddservice = value.isFromAddservice;
					saveArr.debitNoteService = false;
					saveArr.isDummyService = value.isDummyService;
					saveArr.isHiddenService = value.isHiddenService;
					//package force add
					saveArr.forceAddRemoved = value.forceAddRemoved;
					saveArr.isConsumed = value.isConsumed;			
					saveArr.isLabService = $scope.labConfig.labSpecialityId==saveArr.departmentId ? 'Y' : 'N';
					
					saveArr.packageApplicableDate = value.packageApplicableDate;
					
					if(value.discountDetailsList == null)
						{
						value.discountDetailsList = [];
						}
					    saveArr.saleId = value.saleId;
						saveArr.orderDetailsPayeeMapperDtosList  = value.orderDetailsPayeeMapperDtosList;
						saveArr.orderDetailsPackageMapperDtosList  = value.orderDetailsPackageMapperDtosList;
						
						if($scope.updatePayee || $scope.recalculation){
							saveArr.ordDiscount = parseFloat(0);
				            saveArr.ordDiscountPer =  parseFloat(0);
						}
					    saveOrderDetailsObj.push(saveArr);
		            	/*}*/
				    });
								if(forceInout == undefined){
									data.orderDetailsMasterDtosList = saveOrderDetailsObj;
								}else{
									
									var updatedList = [];
									angular.forEach(saveOrderDetailsObj, function (value, key) {
										if($scope.pharmacyServiceId == value.serviceId){
											updatedList.push(value);
										}
									});
									data.forceInOut = true;
									data.orderDetailsMasterDtosList = updatedList;
								}
				               
				               
								var URI = BASE_URL + ROOT_URL +'/api/billing/upDatePackage';
								BillingGenericService.serviceAction(METHOD_POST,URI, data , NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
									if(response.data.status == 'success')
									{
										$scope.needToUpdatePharmacyService = false;
										getMedicationIssueListByAdmissionId(self.model.encounterDetails.encounterId);
		                                $scope.isPatientClass = 'N';
		                                if($scope.recalculation == true)
		                                {
		                                	growl.success("Recalculation has been done successfully...!",{
		                						title : 'Success!'
		                					});
		                                	$scope.recalculation = false;
		                                	
		                                	$scope.billWiseDiscountObj = {
		                    						"discountGivenBy":$scope.userId,
		                    						"discountCategoryId" : "",
		                    						"discountAmountBillWise" : "",
		                    						"discountPercentageBillWise" : "",
		                    						"approvedAmountBillWise":"",
		                    						"requestUserId":"",
		                    						"requestAmountBillWise" : 0,
		                    						"remark":'',
		                    				}
		                                }
		                                if($scope.updatePayee)
		                                 {
			                                 growl.success(
														'Payee Updated Successfully..!!',
														{
															title : 'Success!'
														});
		                                 }
		                          	
                                        $scope.getNewDoctorList();
                                        getOrderedServices();
										$rootScope.stopLoaderPopup();
									}else if (response.data.code==409){
										if(response.data.object==true) //if mismatch in bill found
										{
											getOrderedServices();
											getMedicationIssueListByAdmissionId(self.model.encounterDetails.encounterId);
										}else{
											$scope.patientDetails = {};
											$scope.init();
										}
									}
									else if(response.data.status == 'error')
									{
		                                 $scope.isPatientClass = 'N';
										 $rootScope.stopLoaderPopup();
									}
								});
								
			}
		    
		    $scope.getQuantityCalPopup = function(itemObj,idx)
		    {
		    	if(itemObj.quantity == '' || itemObj.quantity ==null || itemObj.quantity == undefined || itemObj.quantity == 0) 
		    	{
		    		$scope.servicesListPopup[idx].quantity = parseFloat(1);
		    		growl.warning("Quantity cannot be zero or empty..!!",
		    	    		{
		    					title : WARNING_MSG
		    				});
		    	    		return false;
		    	}
		    	else{
		    			$scope.servicesListPopup[idx].totalAmt = parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice).toFixed(2);
		    			var autoRenderedServicesList = $scope.servicesListPopup;
		       	 		$scope.servicePopupCalculateFinal(autoRenderedServicesList);
		    	}
		    }
		    
		    $scope.getBasePriceCalPopup = function(itemObj,idx)
		    {
		    			if(itemObj.basePrice == '' || itemObj.basePrice == null || itemObj.basePrice == undefined){
		    				itemObj.basePrice = 0;
		    			}
			        	
		    			
		    			
		    		  	if(itemObj.maxRateEditable!= null && itemObj.maxRateEditable < itemObj.basePrice){
		    	    		 growl.warning(" You can edit rate max up to "+itemObj.maxRateEditable,{
		    						title : ERROR_MSG
		    					});
		    	    		 itemObj.basePrice =  itemObj.basePriceOld;
		    	    		
		    	    	}
		    	    	
		    	    	if(itemObj.minRateEditable!= null && itemObj.minRateEditable > itemObj.basePrice){
		    	   		 growl.warning(" You can edit rate min up to "+itemObj.minRateEditable,{
		    						title : ERROR_MSG
		    					});
		    	   		 itemObj.basePrice =  itemObj.basePriceOld;
		    	   		
		    	   	}
		    			
			        	$scope.servicesListPopup[idx].totalAmt = parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice).toFixed(2);
		    			var autoRenderedServicesList = $scope.servicesListPopup;
		       	 		$scope.servicePopupCalculateFinal(autoRenderedServicesList);
		    		
		    }
		    
		    $scope.addTempServiesToServiceMasterList = function(){
		    	
		    	//For Surgecharge start
		  
		    	 angular.element('#addServicePopup').modal('hide');	
				var indexT = $scope.autoRenderedServicesFinalList.findIndex(record => record.serviceMasterId === $scope.surchargeServiceId);
				
				    if (indexT >= 0) {
				    	if($scope.autoRenderedServicesFinalList[indexT].orderDetailsId > 0){
				    		
				    		var data = {
				    				orderDetailsId : $scope.autoRenderedServicesFinalList[indexT].orderDetailsId,
				    				ordCancelReasonId : null,
				    				userId : $scope.userId
				    		}
				    		
				    		var URI = BASE_URL + ROOT_URL + "/api/billing/bill/service/cancel";
					    	
					    	BillingGenericService.serviceAction(METHOD_POST,URI,data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					    		if(response.data.status == "success"){
                                    growl.success(response.data.message,
                                    {
                                        title : 'success'
                                    });

					    		}else{
                                      growl.error(response.data.message,
                                      {
                                          title : 'error'
                                      });
					    		}
					    	});
				    	
				    }else{
				    	$scope.autoRenderedServicesFinalList.splice(indexT, 1);
				    }
			}
		    	//For Surgecharge end
				    
		    	$rootScope.startLoader();
		    	var pac = 1;
	    		$scope.disLis = {};
	    		var indexList = [];
	    		$scope.servicesListPopup = $scope.servicesListPopup.reverse();
	    		
	    		angular.forEach($scope.servicesListPopup, function (value, key) {
	    			if($scope.autoRenderedServicesFinalList != null){
	                    $scope.ServiceCount = $scope.ServiceCount + parseInt(value.quantity);
	                }
	    			var newArr = {};
                    	    			if(value.serviceSpecialityId==$scope.labConfig.labSpecialityId || value.serviceSpecialityId==$scope.radiologySpecialityId || value.serviceSpecialityId==$scope.physiotherapySpecialityId || value.serviceSpecialityId==$scope.hemodialysisSpecialityId)
                    	    			{
                    	    				var storeIndexOfQtyList = 0;
	                    	    			for(var i=0;i<value.quantity;i++)
	                    					{
	                    	    				if(value.doctorId > 0)
	                    	    				{
	
	                    						if($scope.doctorsList!=null && $scope.doctorsList.length > 0 ){
	                    		    	    	var docName = $.grep($scope.doctorsList, function (doc)
	                    		    	    	{
	                    		    	            return doc.doctorId == value.doctorId;
	                    		    	        })[0].doctorName;
	                    						}else if(value.docList!=null && value.docList.length > 0){
	                    							var docName = $.grep(value.docList, function (doc)
	                    		    	    	 {
	                    		    	               return doc.doctorId == value.doctorId;
	                    		    	          })[0].doctorName;
	                    							newArr.docList =value.docList;
	                    							$scope.docList  =value.docList;
	                    						}
	                    	    			}

						
							    			newArr.priorityId  = '2';
							    			newArr.isOutsourced ='N';
							    			newArr.serviceSpecialityId = value.serviceSpecialityId;
							    			newArr.serviceSubSpecialityId = value.serviceSubSpecialityId;
							    			newArr.doctorId = value.doctorId;
							    			newArr.doctorSpecialityId = value.doctorSpecialityId;
							    			newArr.specialityId = value.doctorSpecialityId;
							    			newArr.bedCategoryId = value.bedCategoryId;
							    			newArr.wardId = value.wardId;
							    			newArr.roomId = value.roomId;
							    			newArr.bedId = value.bedId;
							    			newArr.isAutoRender =  value.isAutoRender;
						                    newArr.serviceRendered = value.serviceRendered;
							    			newArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
							    			newArr.serviceMasterId = value.serviceMasterId;
							    			newArr.serviceStandardCode = value.serviceStandardCode;
							    			newArr.serviceStandardName = value.serviceStandardName;
							    			newArr.isDiscountApplicable = value.isDiscountApplicable;
							    			newArr.isDiscount = value.isDiscount;
							    			newArr.isTariffRateApplicable = value.isTariffRateApplicable;//12-10-2019
							    			newArr.isQuantityEditable = value.isQuantityEditable;
							    			newArr.isRateEditable = value.isRateEditable;
							    			//newArr.isRateEditable = value.isRateEditable;
							    			newArr.minRateEditable = value.minRateEditable;
							    			newArr.maxRateEditable = value.maxRateEditable;
							    			newArr.specialityName = value.specialityName;
							    			newArr.doctorName =docName;
							    			newArr.quantity = 1;
							    			newArr.basePrice = value.basePrice;
							    			newArr.basePriceOld = value.basePrice;
							    			newArr.oldBasePrice =value.oldBasePrice;
							    			newArr.discountDisabledFlag = 'Y';
							    			newArr.totalAmt = parseFloat(1) * parseFloat(value.basePrice).toFixed(2);
							    			newArr.concession = parseFloat(value.concession);
							    			var withConDic =parseFloat(newArr.totalAmt - (parseFloat(newArr.concession))).toFixed(2);
							    			newArr.taxAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
							    			newArr.netPay =   parseFloat(parseFloat(withConDic) +  parseFloat(newArr.taxAmt)).toFixed(2);
							    			newArr.selfPayable = newArr.netPay;
							    			newArr.selfPayableOldC = newArr.netPay;
							    			newArr.creditPayable = 0;
							    			newArr.creditPayableOldC =  0;
							    			newArr.discountPercentage = 0;
							    			newArr.discount = 0;
							    			newArr.packageConsumptionAmt = 0;
							    			newArr.taxId = value.taxId;
							    			newArr.taxName = value.taxName;
							    			newArr.taxPercentage = value.taxPercentage;
							    			newArr.discountDetailsList = [];
							    			newArr.medallServiceId = value.medallServiceId;
							    			newArr.oldNetPay = "0";
							    			newArr.createdBy  = $scope.userId;
							    			newArr.createdDate = new Date().getTime();
							    			newArr.isPackageService = value.isPackageService;
							    			newArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
							    			newArr.isCapService = value.isCapService;
							    			newArr.freeTextOrderedServicesId = value.freeTextOrderedServicesId
							    			newArr.subSpecialityId = value.subSpecialityId;
							    			newArr.packageId = value.packageId;
							    			newArr.packageTypeId = value.packageTypeId;
							    			newArr.ordCancelled = "N";
							    			newArr.status = "A";
							    			newArr.payeeId  = value.payeeId;
							    			newArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
							    			newArr.coShare = value.coShare;
							    			newArr.contractId = value.contractId;
							    			newArr.isRefDoctorShare = value.isRefDoctorShare;
						                    newArr.discountOwnership = $scope.defaultDiscountOwnership;
						                    newArr.ordRenderDatetime = value.renderingDate!=null ? new Date(value.renderingDate).getTime() : new Date().getTime() ;
							    			//for patient class
							    			newArr.patientClassPercentage = value.patientClassPercentage;
							        		newArr.markupStatus =value.markupStatus;
						                    newArr.isFromServiceSerach = value.isFromServiceSerach;
							        		newArr.isFromAddservice = 'Y';
							        		newArr.isDummyService = (typeof(value.isDummyService) != "undefined") ? (value.isDummyService==true ? 1 :0 ) : 0 ;
							        		newArr.isHiddenService = 0;
							    			//for patient class end
							        		newArr.orderDetailsPayeeMapperDtosList = value.orderDetailsPayeeMapperDtosList;
							        		//for pacakge
							        		newArr.orderDetailsPackageMapperDtosList = [{"id":"",
							    				"orderDetailsId":"",
							    				"serviceId":value.serviceMasterId,
							    				"packageId":'',
							    				"payeeId":"",
							    				"serviceAmount":value.basePrice,	
							    				"packageAmount":0,
							    				"packageCode":'',
							    			    "packageName":'',
							    				"isPackageService":"N",
							    				"isCapService":"N",
							    				"saleDetailsId":"",
							    				"itemId":"",
							    				"status":"A",
							    				"unitId":$scope.unitId,
							    				"organizationId": $scope.organizationId}];
							        		
							        		newArr.tariffId = value.tariffId;
							        		newArr.isDoctorRequired = typeof(value.isDoctorRequired) != "undefined" ?value.isDoctorRequired:'N'; 
							        		newArr.dummyServiceAmt = value.dummyServiceAmt;
							        		newArr.medallServiceId = value.medallServiceId;
							        		newArr.ordRenderDatetimeString = moment(value.ordRenderDatetime).format('DD/MM/YYYY');
							        		newArr.orderDateStringNew = moment(value.orderDate).format('DD/MM/YYYY HH:mm');
							        		newArr.pharmacyDiscountOverride = value.pharmacyDiscountOverride;
							    			$scope.autoRenderedServicesFinalList.push(newArr);	 
							    			$scope.initialAutoRenderedServicesFinalList=$scope.autoRenderedServicesFinalList;
							    			if(storeIndexOfQtyList == 0 ){
							    				var serviceIndex = $scope.autoRenderedServicesFinalList.indexOf(newArr);
							    				storeIndexOfQtyList = serviceIndex;
							    			}else{
							    				storeIndexOfQtyList++;
							    			}
							    		
							    			indexList.push(storeIndexOfQtyList);
							    			$scope.autoRenderedServicesForSaveAndUpdate.push(newArr);
							    			if(pac == $scope.servicesListPopup.length)
							    			{
							    				$rootScope.stopLoader();
							    			}
							    			pac++;
                    	}
	    		}else{
	    			
	    			var docName = ''; 
	    			if(value.doctorId > 0){
	    	    	docName = $.grep($scope.doctorsList, function (doc) {
	    	            return doc.doctorId == value.doctorId;
	    	        })[0].doctorName;
	    			}
	    			
	    			var temp = {
							serviceMasterId : $scope.surchargeServiceId
					}
	    			
	    			
	    			var specialityLsit=[];
					angular.forEach($scope.getInitiallySpecialityList, function (service, key) 
							{
								if(service.doctorId==value.doctorId)
									{
									specialityLsit.push(service);
									}
							});
					var newArr = {};
					newArr.doctorSpecialityList=[];
					newArr.doctorSpecialityList = specialityLsit;
	    			newArr.priorityId  = '2';
	    			newArr.isOutsourced ='N';
	    		//	newArr.specialityId = value.specialityId;
	    			newArr.serviceSpecialityId = value.serviceSpecialityId;
	    			newArr.serviceSubSpecialityId = value.serviceSubSpecialityId;
	    			newArr.doctorId = value.doctorId;
	    			newArr.doctorSpecialityId = value.doctorSpecialityId;
	    			newArr.specialityId = value.doctorSpecialityId;
	    			
	    			newArr.bedCategoryId = value.bedCategoryId;
	    			newArr.wardId = value.wardId;
	    			newArr.roomId = value.roomId;
	    			newArr.bedId = value.bedId;
	    			newArr.isAutoRender =  value.isAutoRender;
                    newArr.serviceRendered = value.serviceRendered;
	    			newArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
	    			newArr.serviceMasterId = value.serviceMasterId;
	    			newArr.serviceStandardCode = value.serviceStandardCode;
	    			newArr.serviceStandardName = value.serviceStandardName;
	    			newArr.isDiscountApplicable = value.isDiscountApplicable;
	    			newArr.isDiscount = value.isDiscount;
	    			newArr.isTariffRateApplicable = value.isTariffRateApplicable;//12-10-2019
	    			newArr.isQuantityEditable = value.isQuantityEditable;
	    			newArr.isRateEditable = value.isRateEditable;
	    			//newArr.isRateEditable = value.isRateEditable;
	    			newArr.minRateEditable = value.minRateEditable;
	    			newArr.maxRateEditable = value.maxRateEditable;
	    			newArr.specialityName = value.specialityName;
	    			newArr.doctorName =docName;
	    			newArr.quantity = value.quantity;
	    			newArr.basePrice = value.basePrice;
	    			newArr.basePriceOld = value.basePrice;
	    			newArr.oldBasePrice =value.oldBasePrice;
	    			newArr.discountDisabledFlag = 'Y';
	    			newArr.totalAmt = parseFloat(value.quantity) * parseFloat(value.basePrice).toFixed(2);
	    			newArr.concession = parseFloat(value.concession);
	    			var withConDic =parseFloat(newArr.totalAmt - (parseFloat(newArr.concession))).toFixed(2);
	    			newArr.taxAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
	    			newArr.netPay =   parseFloat(parseFloat(withConDic) +  parseFloat(newArr.taxAmt)).toFixed(2);
	    			newArr.selfPayable = newArr.netPay;
	    			newArr.selfPayableOldC = newArr.netPay;
	    			newArr.creditPayable = 0;
	    			newArr.creditPayableOldC =  0;
	    			newArr.discountPercentage = 0;
	    			newArr.discount = 0;
	    			newArr.packageConsumptionAmt = 0;
	    			newArr.taxId = value.taxId;
	    			newArr.taxName = value.taxName;
	    			newArr.taxPercentage = value.taxPercentage;
	    			newArr.discountDetailsList = [];
	    			
	    			newArr.oldNetPay = "0";
	    			newArr.createdBy  = $scope.userId;
	    			newArr.createdDate = new Date().getTime();
	    			newArr.isPackageService = value.isPackageService;
	    			newArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
	    			newArr.isCapService = value.isCapService;
	    			newArr.freeTextOrderedServicesId = value.freeTextOrderedServicesId
	    			newArr.subSpecialityId = value.subSpecialityId;
	    			newArr.packageId = value.packageId;
	    			newArr.packageTypeId = value.packageTypeId;
	    			newArr.ordCancelled = "N";
	    			newArr.status = "A";
	    			//newArr.ordCancelReasonId  = '';
	    			//newArr.ordCancelDatetime  = new Date().getTime();
	    			//newArr.ordCancelRemark  = '';
	    			newArr.payeeId  = value.payeeId;
	    			newArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
	    			newArr.coShare = value.coShare;
	    			newArr.contractId = value.contractId;
	    			newArr.isRefDoctorShare = value.isRefDoctorShare;
                    newArr.discountOwnership = $scope.defaultDiscountOwnership;
                    newArr.ordRenderDatetime = value.renderingDate!=null ? new Date(value.renderingDate).getTime() : new Date().getTime() ;
	    			//for patient class
	    			newArr.patientClassPercentage = value.patientClassPercentage;
	        		newArr.markupStatus =value.markupStatus;
                    newArr.isFromServiceSerach = value.isFromServiceSerach;
	        		newArr.isFromAddservice = 'Y';
	        		newArr.isDummyService = (typeof(value.isDummyService) != "undefined") ? (value.isDummyService==true ? 1 :0 ) : 0 ;
	        		newArr.isHiddenService = 0;
	    			//for patient class end
	        		newArr.orderDetailsPayeeMapperDtosList = value.orderDetailsPayeeMapperDtosList;
	        		//for pacakge
	        		newArr.orderDetailsPackageMapperDtosList = [{"id":"",
	    				"orderDetailsId":"",
	    				"serviceId":value.serviceMasterId,
	    				"packageId":'',
	    				"payeeId":"",
	    				"serviceAmount":value.basePrice,	
	    				"packageAmount":0,
	    				"packageCode":'',
	    			    "packageName":'',
	    				"isPackageService":"N",
	    				"isCapService":"N",
	    				"saleDetailsId":"",
	    				"itemId":"",
	    				"status":"A",
	    				"unitId":$scope.unitId,
	    				"organizationId": $scope.organizationId}];
	        		
	        		newArr.tariffId = value.tariffId;
	        		newArr.isDoctorRequired = typeof(value.isDoctorRequired) != "undefined" ?value.isDoctorRequired:'N'; 
	        		newArr.dummyServiceAmt = value.dummyServiceAmt;
	        		newArr.medallServiceId = value.medallServiceId;
	        		newArr.ordRenderDatetimeString = moment(value.ordRenderDatetime).format('DD/MM/YYYY');
	        		newArr.orderDateStringNew = moment(value.orderDate).format('DD/MM/YYYY HH:mm');
	        		newArr.pharmacyDiscountOverride = value.pharmacyDiscountOverride;
	    			$scope.autoRenderedServicesFinalList.push(newArr);	
	    			$scope.initialAutoRenderedServicesFinalList=$scope.autoRenderedServicesFinalList;
	    			$scope.autoRenderedServicesForSaveAndUpdate.push(newArr);
	    			var serviceIndex = $scope.autoRenderedServicesFinalList.indexOf(newArr);
	    			indexList.push(serviceIndex);
	    			if(pac == $scope.servicesListPopup.length)
	    			{
	    				$rootScope.stopLoader();
	    			}
	    			pac++;
	    				
	    		}	
	    			
			});
	    		
                    angular.forEach($scope.selectedPackageList, function (value, key) 
                    		{
	    			
		    		var newArr = {};
					newArr.priorityId  = '2';
					newArr.isOutsourced ='N';
					//newArr.specialityId = value.specialityId;
					newArr.doctorSpecialityId = value.doctorSpecialityId;
	    			newArr.specialityId = value.doctorSpecialityId;
					newArr.serviceSpecialityId = value.serviceSpecialityId;
					newArr.serviceSubSpecialityId = value.serviceSubSpecialityId;
					newArr.bedCategoryId = value.bedCategoryId;
					newArr.wardId = value.wardId;
	    			newArr.roomId = value.roomId;
	    			newArr.bedId = value.bedId;
					newArr.serviceRendered = "0";
					newArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
					newArr.serviceMasterId = value.serviceMasterId;
					newArr.serviceStandardCode = value.serviceStandardCode;
					newArr.serviceStandardName = value.serviceStandardName;
					newArr.isDiscountApplicable = value.isDiscountApplicable;
					newArr.isDiscount = value.isDiscount;
					newArr.isQuantityEditable = value.isQuantityEditable;
					newArr.isRateEditable = value.isRateEditable;
					//newArr.isRateEditable = value.isRateEditable;
					newArr.minRateEditable = value.minRateEditable;
					newArr.maxRateEditable = value.maxRateEditable;
					newArr.specialityName = value.specialityName;
					newArr.quantity = value.quantity;
					newArr.basePrice = value.basePrice;
					newArr.basePriceOld = value.basePrice;
	                newArr.oldBasePrice =value.oldBasePrice;
					newArr.discountDisabledFlag = 'Y';
					newArr.totalAmt = parseFloat(value.quantity) * parseFloat(value.basePrice).toFixed(2);
					newArr.concession = parseFloat(value.concession);
					newArr.discountPercentage = value.discountPercentage;
					newArr.discount = value.discount;
					var dis =value.discount;;
					var disPer =value.discountPercentage;
					newArr.taxId = value.taxId;
					newArr.taxName = value.taxName;
					newArr.taxPercentage = value.taxPercentage;
					newArr.discountDetailsList = [];
					/*newArr.selfPayable =  "0";
					newArr.creditPayable =   "0";*/
					newArr.selfPayable = value.selfPayable;
					newArr.creditPayable =   value.creditPayable;
					newArr.netPay =   value.netPay;
					newArr.oldNetPay = "0";
					newArr.createdBy  = $scope.userId;
					newArr.createdDate = new Date().getTime();
					newArr.isPackage = true;
					newArr.packageMasterId = typeof(value.packageId) != "undefined" ? value.packageId : value.packageMasterId;
					newArr.packageTypeId = value.packageTypeId;
					newArr.packageId = typeof(value.packageId) != "undefined" ? value.packageId : value.packageMasterId;
					newArr.isPackageService = value.isPackageService;
					newArr.isCapService = value.isCapService;
	    			newArr.packageConsumptionAmt = value.packageConsumptionAmt;
	    			newArr.subSpecialityId = value.subSpecialityId;
	    			newArr.tariffId = value.tariffId;
	    			//vivek added
	    			newArr.selfPayableOldC = value.selfPayable;
	    			newArr.creditPayableOldC =  value.creditPayable;
	                newArr.payeeId  = value.payeeId;
	                newArr.isFromAddservice = 'Y';
	                newArr.isDummyService = (typeof(value.isDummyService) != "undefined") ? (value.isDummyService==true ? 1 :0 ) : 0 ;
	        		newArr.isHiddenService = 0;
                    newArr.discountOwnership = $scope.defaultDiscountOwnership;
	                newArr.isDoctorRequired = typeof(value.isDoctorRequired) != "undefined" ?value.isDoctorRequired:'N';
					newArr.orderDetailsPayeeMapperDtosList = value.orderDetailsPayeeMapperDtosList;
	                newArr.dummyServiceAmt = value.dummyServiceAmt;
	                newArr.orderDetailsPackageMapperDtosList =(value.orderDetailsPackageMapperDtosList==null|| value.orderDetailsPackageMapperDtosList==undefined)?[]:value.orderDetailsPackageMapperDtosList;
	                newArr.packageApplicableDate = moment(value.packAppDate).format('DD-MM-YYYY');
	                //end
	                newArr.medallServiceId = value.medallServiceId;
	        		newArr.ordRenderDatetimeString = moment(value.ordRenderDatetime).format('DD/MM/YYYY');
	        		newArr.orderDateStringNew = moment(value.orderDate).format('DD/MM/YYYY HH:mm');
	        		newArr.pharmacyDiscountOverride = value.pharmacyDiscountOverride;
					$scope.autoRenderedServicesFinalList.push(newArr);
					$scope.initialAutoRenderedServicesFinalList=$scope.autoRenderedServicesFinalList;
					
					//ServiceCount
					if($scope.autoRenderedServicesFinalList != null){
	                    $scope.ServiceCount = $scope.ServiceCount + parseInt(value.quantity);
	                }
					
					if(typeof(value.packageTypeId) != "undefined" && value.packageTypeId == "1"){
						angular.forEach(value.packageServiceList,function(val,i)
								{
							var docName = ''; 
			    			if(val.doctorId > 0){
			    	    	docName = $.grep($scope.doctorsList, function (doc) {
			                 	            return doc.doctorId == val.doctorId;
			    	        })[0].doctorName;
			    			}
			    			val.doctorName = docName;
			    			$scope.autoRenderedServicesFinalList.push(val);
			    			$scope.initialAutoRenderedServicesFinalList=$scope.autoRenderedServicesFinalList;
						})
	    	 }
					$rootScope.stopLoader();
					
	    		});
                    
                    $scope.billWiseDiscountObj = {
    						// "discountGivenBy":"",
    						"discountGivenBy":$scope.userId,
    						"discountCategoryId" : "",
    						"discountAmountBillWise" : "",
    						"discountPercentageBillWise" : "",
    						"approvedAmountBillWise":"",
    						"requestUserId":"",
    						"requestAmountBillWise" : 0,
    						"remark":'',
    						
    						
    				}
    			 $scope.requestDiscountDiv = false;
                    
                if($scope.selectedPackageList !=null && $scope.selectedPackageList.length>0)
          		{
          			   $rootScope.startLoaderPopup();
          			   $scope.calculationTest($scope.autoRenderedServicesFinalList,addService);
          			   $scope.saveOrderPackage();
          			   $scope.selectedPackageList = [];
          			}
          		else 
          		   {
          			 angular.element('#addServicePopup').modal('hide');
          			 var listServicepopup = $scope.autoRenderedServicesFinalList;
          	    	 $scope.calculationTest(listServicepopup,addService);
          	    	 // $scope.saveOrderPackage();
          	    	 if(indexList.length>0)
          	    		 $scope.addOrUpdateServices(null,indexList);
          		   }  
                
                $scope.isFinancialClearence ='N';
		    	$scope.finanacial.financialRequestSendTo = "";
	    }
		    
		    
		    $scope.wipeOutServices = function(){
		    	if($scope.servicesListPopup.length>0){
		    	$scope.servicesListPopup = [];
				 growl.warning('Added Services are wiped out..!!',{
													title : 'Warning!'
												});
		    	}
		    }
		    
		    $scope.wipeOutPackages = function(){
		    	if($scope.selectedPackageList.length>0){
		    		$scope.selectedPackageList = [];
				 growl.warning('Added Packages are wiped out..!!',{
													title : 'Warning!'
												});
		    	}
		    }
		    
		    
		    
		    $scope.getRemoveDiscountPopup = function(item,idxDis)
		    {
		    	$scope.servicesCancelReasonId = null;
			    $scope.servicesCancelRemark = '';
		    	$scope.objectIndexDiscount = idxDis;
		    	$scope.discountObjectForRemove = item;
		    }
		    
		    $scope.removeDiscount = function()
		    {
		    	var idx  = $scope.objectIndex;
		    	var discIdx = $scope.objectIndexDiscount;
		    	var item = $scope.discountObjectForRemove;
		    	
		    	//return false;
		    	if(item.orderDetailsDiscountId > 0)
		    		{
		    			var disArr = {};
		    			disArr.orderDetailsDiscountId  = item.orderDetailsDiscountId;
		    			disArr.orderDetailsId = item.orderDetailsId;
		    			disArr.discountCategoryId = item.discountCategoryId;
		    			disArr.discountPercentage = item.discountPercentage;
		    			disArr.discountAmount = item.discountAmount;
		    			disArr.isCancel = 'Y';
		    			disArr.cancelBy  = $scope.userId;
		    			disArr.cancelReasonId  = $scope.discountReasonId;
		    			disArr.remark = $scope.discountRemark;
		    			disArr.createdBy  = item.createdBy;
		    			disArr.createdDate  = item.createdDate;
		    			disArr.updatedBy  = $scope.userId;
		    			disArr.updatedDate  = new Date().getTime();
		    			disArr.cancelledDate = new Date().getTime();
		    			disArr.orgId  = $scope.organizationId;
		    			disArr.unitId  = $scope.unitId;
		    			disArr.discountDate = new Date().getTime();
		    			disArr.status = "I";
		    			disArr.isConsessionDiscount = "N";
		    			$scope.removeDiscountList.push(disArr);
		    		}
		    	
		    	
		    	var currDisAmt = $scope.autoRenderedServicesFinalList[idx].discount;
		    	var currDisPer = $scope.autoRenderedServicesFinalList[idx].discountPercentage;
		    	
		    	var finalDisAmt = (parseFloat(currDisAmt) - parseFloat(item.discountAmount)).toFixed(2);
		    	var finalDisPer = (parseFloat(currDisPer) - parseFloat(item.discountPercentage)).toFixed(2);
		    	
		    	$scope.autoRenderedServicesFinalList[idx].discount = finalDisAmt;
		    	$scope.autoRenderedServicesFinalList[idx].discountPercentage = finalDisPer;
		    	
		    	var serviceIndex = $scope.autoRenderedServicesFinalList[idx].discountDetailsList.indexOf(item);
		    	$scope.autoRenderedServicesFinalList[idx].discountDetailsList.splice(serviceIndex, 1);
		    	
		    	if($scope.autoRenderedServicesFinalList[idx].discountDetailsList.length == 0)
		    		{
		    		$scope.autoRenderedServicesFinalList[idx].discountCategoryId = '';
		    		}
		    	
		    	var autoRenderedServicesList = $scope.autoRenderedServicesFinalList;
		    	
		    	$scope.calculationTest(autoRenderedServicesList,init);
		    	$scope.getDiscountListPopup($scope.autoRenderedServicesFinalList,idx);
		    	$scope.discountReasonId = '';
		    	$scope.discountRemark = '';
		    	
		    }
		    
		    
		    $scope.initPackageSearchObj = function(){
				$scope.packageSearchObj = {packageTypeId : "0", serviceCode:"",serviceName:""}
			}
			$scope.initPackageSearchObj();
			
			$scope.getPackageTypeList = function(orgId,unitId) {
				var URI = BASE_URL + ROOT_URL + "/Unit/Service/EHCpackage/getPackgeType/"+orgId+"/"+unitId;
				BillingGenericService.serviceAction("GET", URI, null).then(function(response){
					$scope.packageTypeList = response.data.listObject;
				});
			}
		    
			$scope.searchPackages = function(packageSearchKeyword)
			{
				    var packageSearchObj = {};
					packageSearchObj.organisationId = $scope.organizationId;
					packageSearchObj.unitId = $scope.unitId;
					packageSearchObj.visitTypeId = $scope.visitTypeId;//default ip
					packageSearchObj.bedBillingCategoryId = $scope.admissionDetails.bedBillingCategoryId;
					packageSearchObj.genderId = $scope.patientDetails.genderId;
					packageSearchObj.paymentEntitlementId = $scope.admissionDetails.paymentEntitlementId;
					packageSearchObj.patientTypeId = $scope.admissionDetails.patientCategoryId;
					packageSearchObj.serviceCode = packageSearchKeyword;
					packageSearchObj.serviceName = packageSearchKeyword;
/*					var URI = BASE_URL + ROOT_URL + "/api/packages/bill/services/search";
					return BillingGenericService.serviceAction("POST", URI, packageSearchObj).then(function(response){
						if(response.data.status == "success")
							return response.data.listObject;
					});*/
					return servicesFactory.searchPackageForBilling(packageSearchObj,$scope);
			}
			
		/*	$scope.selectPackageService = function($item, $model, $label){
				var packageIds = [];
				angular.forEach($scope.selectedPackageList,function(value,index){
					packageIds.push(value.packageMasterId);
				});
				if(packageIds.indexOf($item.packageMasterId) == -1){
					$scope.selectedPackageList.push($item);
					$scope.initPackageSearchObj();
				}else{
					growl.error("Package Already Added",{
						title : ERROR_MSG
					});
				}
			}*/
			
			$scope.getDoctorBySpecialityList = function (specialityId) {

				var data = {
					specialityId: specialityId,
					unitId: $scope.unitId,  
					organizationId: $scope.organizationId
				};
				var URI = BASE_URL + ROOT_URL+ GETDOCTORBYSPECIALITYID;

				return BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
					.then(
						function (response) {
							if (response.data.status == "success")
								{
									 return $scope.docLists =  response.data.listObject
									 
								}
							
						});

			}
			

			$scope.selectPackageService = function($item, $model, $label){
				
				var checkFlag = false;
				angular.forEach($scope.listPackageOrder,function(value,index){
					
					if($item.packageMasterId == value.packageId && value.isForceClose == 1){
						checkFlag = true;
						
					}
					
				});
				
				if(checkFlag){
					growl.warning("Mentioned package already added.",{
							title : WARNING_MSG
						});
				}
				
				$scope.packageResultObj =  $item;
				$scope.forServicePayeeId  = $scope.selfPayeeId;
				
				if(parseInt($scope.contractId) == 0)
				{
					$scope.contractId = null;
				}
				$scope.packageResultObj.admissionId = $scope.admissionId;
				$scope.packageResultObj.contractId = $scope.contractId;
				$scope.packageResultObj.visitTypeId = $scope.visitTypeId;
				$scope.packageResultObj.billingBedCategoryId =$scope.admissionDetails.bedBillingCategoryId;
				$scope.packageResultObj.patientCategoryId = $scope.admissionDetails.patientCategoryId;
				$scope.packageResultObj.defaultSelfTariffId = $scope.defaultSelfTariffId;
			    $scope.packageResultObj.selfPayeeId = $scope.selfPayeeId;
			    $scope.packageResultObj.organizationId = $scope.organizationId;
			    $scope.packageResultObj.unitId = $scope.unitId;
			    
				
			    var packageIds = [];
				angular.forEach($scope.selectedPackageList,function(value,index){
					packageIds.push(value.packageMasterId);
				});
				if(packageIds.indexOf($item.packageMasterId) == -1){
					//return false;
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/global/consumption/package";
					BillingGenericService.serviceAction(METHOD_POST,URI, $scope.packageResultObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						if(response.data.status=="success"){
							//console.log("res",response);
							//var basePriceObj=response.data.object;
							if(response.data.object != "undefined" && response.data.object != null)
								{
								$scope.forServicePayeeId = response.data.object.payeeId;
								$item.basePrice=response.data.object.serviceRate;
								$item.quantity=1;
								$item.coPayPer = response.data.object.coShare==null?0:response.data.object.coShare;
								$item.selfPayable = response.data.object.selfPayable;
								$item.creditPayable = response.data.object.creaditPayable;
								$item.packageConsumptionAmt = response.data.object.packageConsumptionAmt;
								$item.contractId = response.data.object.contractId;
								$item.patientClassPercentage = response.data.object.patientClassPercentage;
								$item.markupStatus = response.data.object.markupStatus;
								$item.minRateEditable = response.data.object.minRateEditable;
								$item.maxRateEditable = response.data.object.maxRateEditable;
								$item.netPay = response.data.object.finalPrice;
								$item.orderDetailsPayeeMapperDtosList=response.data.object.orderDetailsPayeeMapperDtosList;
								$item.payeeId=response.data.object.payeeId;
								$item.wardId = $scope.admissionDetails.wardId;
								$item.roomId = $scope.admissionDetails.roomId;
								$item.bedId = $scope.admissionDetails.bedId;
								$item.packAppDate=new Date();
								$item.isDummyService = $scope.addServiceIsDummyService;
								$scope.packageApplicableDate.push({opened:false});
								// for ehc pkg start
								var packageServiceList = [];
								if(typeof($item.packageTypeId) != "undefined" && $item.packageTypeId == "1"){
				    	    		var packageIds = [];
				    	    		packageIds.push($item.packageMasterId);
				    	    		// get ehc package services
				    	    		var URI = BASE_URL + ROOT_URL + "/api/packages/bill/package/"+packageIds.toString();
				    				BillingGenericService.serviceAction("GET", URI, null).then(function(response){
				    					if(response.data.status == "success")
				    					{
				    						var ehcPackageServiceList = response.data.listObject[0].listTPackageServicesDetailsDto;
				    						angular.forEach(ehcPackageServiceList,function(val,i){
				    							var newArr = {};
				    			    			newArr.priorityId  = '2';
				    			    			newArr.isOutsourced ='N';
				    			    			newArr.specialityId = val.specialityId;
				    			    			newArr.serviceSpecialityId = val.specialityId;
				    			    			newArr.serviceSubSpecialityId = val.subSpecialityId;
				    			    			/*if($scope.isPreOrPostBilling == 'N')
				    			    			{
				    			    			newArr.serviceRendered = 1;
				    			    			}
				    			    		   else
				    			    			{
				    			    			 newArr.serviceRendered = "0";
				    			    			}*/
				    			    			newArr.serviceRendered = "0";
				    			    			newArr.serviceMasterId = val.serviceId;
				    			    			newArr.serviceStandardCode = val.serviceCode;
				    			    			newArr.serviceStandardName = val.serviceName;
				    			    			newArr.isDoctorRequired = val.isDocReq;
				    			    			newArr.isRefDoctorShare = val.isRefDoctorShare;
				    			    			newArr.isDiscountApplicable = "N";
				    			    			newArr.isDiscount = "N";
				    			    			newArr.isQuantityEditable = "N";
				    			    			newArr.isRateEditable = "N";
				    			    			newArr.minRateEditable = 0;
				    			    			newArr.maxRateEditable = 0;
				    			    			newArr.specialityName = val.specialityName;
				    			    			newArr.quantity = 1;
				    			    			newArr.basePrice =  val.servicePrice;
				    			    			newArr.serviceAmt = val.servicePrice;
				    			    			newArr.totalAmt = val.servicePrice;
				    			    			newArr.netPay = 0;
				    			    			newArr.packageConsumptionAmt = val.servicePrice;
				    			    			newArr.basePriceOld = 0;
				    			    			newArr.discountDisabledFlag = 'N';
				    			    			newArr.concession = 0;
				    			    			newArr.patientClassPercentage = parseFloat(0);
				    			    			newArr.discountPercentage = 0;
				    			    			newArr.discount = 0;
				    			    			newArr.coPayPer = 0;
				    			    			var dis =0;
				    			    			var disPer =0;
				    			    			newArr.taxPercentage = 0;
				    			    			newArr.discountDetailsList = [];
				    			    			newArr.selfPayable =  "0";
				    			    			newArr.creditPayable =   "0";
				    			    			newArr.oldNetPay = "0";
				    			    			newArr.createdBy  = $scope.userId;
				    			    			newArr.createdDate = new Date().getTime();
				    			    			newArr.packageMasterId = $item.packageMasterId;
				    			    			newArr.packageTypeId = $item.packageTypeId;
				    			    			newArr.packageId = $item.packageMasterId;
				    			    			newArr.isPackageService = 'Y';
				    			    			newArr.apportionedPrice = val.apportionedPrice;
				    			    			newArr.isDummyService = $scope.addServiceIsDummyService;
				    			    			newArr.isHiddenService = 0;
				    			    			newArr.subSpecialityId = val.subSpecialityId; 
				    			    			newArr.ordCancelled = 'N';
				    			    			newArr.isDocReq=val.isDocReq;
				    			    			
				    			    			if(val.isDocReq == 'Y')
				    			    				{
				    			    				 newArr.doctorId = null;
				    			    	    		 newArr.doctorSpecialityId = null;
				    			    				}
				    			    			var dataMappr = [{
				    			    					contractId: null,
				    			    					creditPayable: 0,
				    			    					organizationId: $scope.organizationId,
				    			    					payeeAmount: 0,
				    			    					payeeId : $scope.selfPayeeId,
				    			    					selfPayable: 0,
				    			    					serviceId: val.serviceId,
				    			    					status: 'A',
				    			    					unitId: $scope.unitId
				    			    			 }];
				    			    			
				    			    			if(val.consultationSpecialityId>0)
				    			    			{
				    			    				newArr.docSpecialityList=$scope.specialityMasterList;
				    			    				newArr.doctorSpecialityId=val.consultationSpecialityId;
				    			    				newArr.docList=$scope.getDoctorBySpecialityList(val.consultationSpecialityId);
				    			    			}
				    			    			else{
				    			    				newArr.docList=$scope.doctorsList;
				    			    			}
				    			    			
				    			    			newArr.orderDetailsPayeeMapperDtosList = dataMappr;
				    			    			
				    			    			// for Package
				    			    			var packageMppr = [{"id":"",
				    			    				"orderDetailsId":"",
				    			    				"serviceId":val.serviceId,
				    			    				"packageId":$item.packageMasterId,
				    			    				"payeeId":"",
				    			    				"serviceAmount":val.servicePrice,	
				    			    				"packageAmount":val.servicePrice,
				    			    				"packageCode":$item.serviceStandardCode,
				    			    			    "packageName":$item.serviceStandardName,
				    			    				"isPackageService":"Y",
				    			    				"isCapService":"N",
				    			    				"saleDetailsId":"",
				    			    				"itemId":"",
				    			    				"status":"A",
				    			    				"unitId":$scope.unitId,
				    			    				"organizationId": $scope.organizationId}];
				    			    			
				    			    			  newArr.orderDetailsPackageMapperDtosList = packageMppr;
				    			    			  newArr.bedCategoryId = $item.bedCategoryId,
				    			    			  newArr.wardId = $scope.admissionDetails.wardId;
				    			    			  newArr.roomId = $scope.admissionDetails.roomId;
				    			    			  newArr.bedId = $scope.admissionDetails.bedId;
				    			    			  packageServiceList.push(newArr);
				    						})
				    						
				    						$item.packageServiceList = packageServiceList;
						    				$scope.selectedPackageList.push($item);
				    					}
				    				});
				    	    	}else
								{
									$scope.selectedPackageList.push($item);
								}
								//end
								
								
								
								
								
//								$item.orderDetailsPayeeMapperDtosList=response.data.object.orderDetailsPayeeMapperDtosList;
								$item.orderDetailsPackageMapperDtosList=response.data.object.orderDetailsPackageMapperDtosList;
								$item.isPackage = true;
//								$scope.selectedPackageList.push($item);
								$scope.initPackageSearchObj();
								
								
								}
							else
								{
								growl.warning("Please add base price of this service and then add..!!",{
									title : ERROR_MSG
								});
								return false
								}
							//concession
							//console.log("FINAL ITEM",$item);
							
							
						}
					});
					
					
				}else{
					growl.error("Package Already Added",{
						title : ERROR_MSG
					});
				}	
				
			}
			
			$scope.setRealDummyFlagForPackageServices = function()
			{
				angular.forEach($scope.selectedPackageList,function(pack,key){
					if(pack.packageServiceList!=null)
					{
						angular.forEach(pack.packageServiceList,function(value,key){
							value.isDummyService = pack.isDummyService;
						});
					}
					
				});
				console.log($scope.selectedPackageList);
			}
			
			$scope.removeSelectedPackage = function(index){
				$scope.selectedPackageList.splice(index,1);
			}
			
			$scope.getPackages = function(){
				var packageIds = [];
				var activePackageIds = [];
				$scope.setSecondTabLevel(21);
				$scope.activePackagesList.find((elem) => activePackageIds.push(elem.packageId));
				angular.forEach($scope.autoRenderedServicesFinalList,function(value,key){
					if(value.isPackage && value.isPackageService != 'Y' && packageIds.indexOf(value.packageMasterId) == -1 && activePackageIds.indexOf(typeof(value.packageMasterId) != "undefined" ? value.packageMasterId : value.packageId) == -1 && ($scope.activePackageIds.indexOf(value.packageMasterId) == -1))
						packageIds.push(typeof(value.packageMasterId) != "undefined" ? value.packageMasterId : value.packageId);
				});
				
				//get package details
				if(packageIds.length > 0){
					var URI = BASE_URL + ROOT_URL + "/api/packages/bill/package/"+packageIds.toString();
					BillingGenericService.serviceAction("GET", URI, null).then(function(response){
						if(response.data.status == "success")
							{
						       if($scope.packageDetailsList!=undefined && $scope.packageDetailsList.length>0 && response.data.listObject!=null)
						    	   {
						    	   angular.forEach(response.data.listObject,function(value,key){
						    		   var found = $scope.packageDetailsList.find(ar=>ar.mPackageMasterDto.packageMasterId==value.mPackageMasterDto.packageMasterId);
						    		   if(found==undefined)
									   {
						    			   $scope.packageDetailsList.push(response.data.listObject.mPackageMasterDto);
									   }
									});
						    	   }
						       else{
						    	     $scope.packageDetailsList = response.data.listObject;
						          }
							}
							
					});
				}
			}
			
			$scope.getActivePackagesList = function(){
				$scope.activePackagesList = [];
				$scope.activePackageIds = [];
				if($scope.admissionDetails !== undefined && $scope.admissionDetails !== null){
                    if($scope.admissionDetails.patientId === undefined || $scope.admissionDetails.patientId === null || $scope.admissionDetails.patientId ===''){
                        $scope.admissionDetails.patientId = 0;
                    }
                }
                else{
                    $scope.admissionDetails.patientId = 0;
                }
				var URI =BASE_URL + ROOT_URL + "/api/packages/patientId/"+$scope.admissionDetails.patientId+"/encounterId/"+$scope.admissionDetails.admissionId+"/visitTypeId/"+$scope.visitTypeId;
				var header = {organisationId: $scope.organizationId,unitId:$scope.unitId,packageId:0};
				GenericServiceParamHeader.serviceAction("GET", URI, null,NOTIFICATION_MSG_STATUS_FALSE,null,header).then(function(response){
					if(response.data.status == "success"){
						$scope.activePackagesList = response.data.listObject;
					$scope.activePackagesList.forEach(function(value,index){
						$scope.activePackageIds.push(value.packageId);
					})
					}
				});
			}
			
			$scope.currentActivePackageServices = function(activePack){
				$scope.currentActivePackage = angular.copy(activePack);
			}
			
			
	
			
			//package transaction view start
			$scope.getAllPackageList=function(){
				
				$scope.packageApplicableDateDt.push({opened:false});
				  $('#commonPackageDetailsPopup').modal('show');
			}
			
			$scope.forceCancelPackage=function(obj){
				var data={
						packageConsumptionMasterId:obj.packageConsumptionMasterId,
						orderDetailId:obj.orderDetailId,
						toDate : moment().format('YYYY-MM-DD HH:mm:ss'),
						unitId : $scope.unitId
				}
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + "/api/packages/forceCancelPackage";
				BillingGenericService.serviceAction("POST", URI, data).then(function(response){
					if(response.data.status == "success")
						{
							growl.success("Package closed",{
								title : WARNING_MSG
							});
							$scope.getListPackageOrder();
							$rootScope.stopLoader();
						}else{
							$rootScope.stopLoader();
						}
						
				});
			}
			
			
			$scope.getServiceIndex = function(itemObj , index)
			{
				var i =0;
		    	   angular.forEach($scope.initialAutoRenderedServicesFinalList, function (value, key) {
			   			if(value.orderDetailsId == itemObj.orderDetailsId){
			   				index = i;
			   			 }
		   			i++;
		   			});
		    	   $scope.autoRenderedServicesFinalList =[]; 
		    	   $scope.autoRenderedServicesFinalList = $scope.initialAutoRenderedServicesFinalList;
		    	   
				$scope.serviceIdxPkg = index;
				
				$scope.addMuptipleServToPackage = false;
			}
			
			$scope.addMuptipleServToPackage = false;
			
			$scope.addMultiServInpackage = function(){
				$scope.addMuptipleServToPackage = true;
				angular.element('#packageForceAdd').modal('show');
			}
			
			$scope.addToPackage = function(packageObj)
			{
				
				var checkdis = false;
				angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
					
					if((value.discountPercentage != undefined && value.discountPercentage > 0 ) || (value.discount != undefined && value.discount >0) )
					{
						checkdis = true;
					}
				});
				
				if(checkdis == true){
					
					growl.warning(" Applied discount will get cleared ",{
						title : "Warning"
					});
					
					$scope.finalCancelDiscount();
				}
				
				if($scope.addMuptipleServToPackage == true){
					
					 $rootScope.startLoaderPopup();
					 $scope.forceAddOrRemove = true;
					 var serInxList = [];
					 $scope.autoRenderedServicesForSaveAndUpdate =[];
					 var checkList = false;
					 
					 angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
				    		
							 if(value.checked !=undefined && value.checked !=null &&  value.checked =="Y"){
					    		
								 serInxList.push(key);
								 
								 var packageMppr = [];
						       

								    var packageMppr = [{"id":"",
									"orderDetailsId":"",
									"serviceId":value.serviceId,
									"packageId":packageObj.packageId,
									"payeeId":"",
									"serviceAmount":value.basePrice,	
				                    "packageAmount":value.basePrice * value.quantity,
									"packageCode":"",
								    "packageName":"",
									"isPackageService":"Y",
									"isCapService":"N",
									"saleDetailsId":"",
									"itemId":"",
									"status":"A",
									"unitId":$scope.unitId,
									"organizationId": $scope.organizationId}];
								 
								    value.orderDetailsPackageMapperDtosList = packageMppr;
								    value.forceAddRemoved =1;
						        
						         $scope.autoRenderedServicesForSaveAndUpdate.push(value);
						         checkList = true;
							 }
				    		
				    	});
					 
					 if(checkList == true){
						 $scope.addOrUpdateServices(null,serInxList);
				         angular.element('#packageForceAdd').modal('hide');
					 }else{
						 growl.warning("Please select atleast one service",{
								title : "Warning"
							});
						 $rootScope.stopLoaderPopup();
					 }
					
			        
					
				}else{
					
					 $rootScope.startLoaderPopup();
					 var packageMppr = [];
			         $scope.forceAddOrRemove = true;

					    var packageMppr = [{"id":"",
						"orderDetailsId":"",
						"serviceId":$scope.autoRenderedServicesFinalList[$scope.serviceIdxPkg].serviceId,
						"packageId":packageObj.packageId,
						"payeeId":"",
						"serviceAmount":$scope.autoRenderedServicesFinalList[$scope.serviceIdxPkg].basePrice,	
	                    "packageAmount":$scope.autoRenderedServicesFinalList[$scope.serviceIdxPkg].basePrice * $scope.autoRenderedServicesFinalList[$scope.serviceIdxPkg].quantity,
						"packageCode":"",
					    "packageName":"",
						"isPackageService":"Y",
						"isCapService":"N",
						"saleDetailsId":"",
						"itemId":"",
						"status":"A",
						"unitId":$scope.unitId,
						"organizationId": $scope.organizationId}];
					 
					 $scope.autoRenderedServicesFinalList[$scope.serviceIdxPkg].orderDetailsPackageMapperDtosList = packageMppr;
					 $scope.autoRenderedServicesFinalList[$scope.serviceIdxPkg].forceAddRemoved =1;
			         $scope.autoRenderedServicesForSaveAndUpdate =[];
			         $scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[$scope.serviceIdxPkg]);
			         $scope.addOrUpdateServices($scope.serviceIdxPkg,null);
			         angular.element('#packageForceAdd').modal('hide');
					
					
				}
				
				

			}
			
			$scope.removeMuptipleServToPackage = false;
			
			$scope.removeFromPackage = function(serviceIdx)
			{
				$scope.removeMuptipleServToPackage = false;
				$scope.removeServiceFromPackage(serviceIdx);
			}
			
			$scope.packageShow = false;
			
			$scope.changeTab = function(tab){
				
				if(tab == 1){
					$scope.packageShow = false;
				}else{
					$scope.packageShow = true; 
				}
				
				
			}
			
			$scope.removeMultiServInpackage = function(){
				$scope.removeMuptipleServToPackage = true;
				$scope.removeServiceFromPackage();
			}
			
			
			$scope.removeServiceFromPackage = function(serviceIdx)
			{
				
				var checkdis = false;
				angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
					
					if((value.discountPercentage != undefined && value.discountPercentage > 0 ) || (value.discount != undefined && value.discount >0) )
					{
						checkdis = true;
					}
				});
				
				if(checkdis == true){
					
					growl.warning(" Applied discount will get cleared ",{
						title : "Warning"
					});
					$scope.finalCancelDiscount();
				}
				
				 $scope.forceAddOrRemove = true; 
				 
				 if($scope.removeMuptipleServToPackage == false){
					 
					 $scope.autoRenderedServicesFinalList[serviceIdx].forceAddRemoved =-1;
					 $scope.autoRenderedServicesFinalList[serviceIdx].isPackageService='N';
					 $scope.autoRenderedServicesForSaveAndUpdate =[];
					 $scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[serviceIdx]);
					 $scope.addOrUpdateServices(serviceIdx,null);
					 
				 }else{
					 
					 $scope.autoRenderedServicesForSaveAndUpdate =[];
					 var serInxList = [];
					 var checkList = false;
					 angular.forEach($scope.autoRenderedServicesFinalList, function (value1, key1) {
				    		
						 angular.forEach(value1.orderDetailsPackageMapperDtosList, function (value, key) {
					    		
							 if(value.isPackageService=='Y' && value.checked !=undefined && value.checked !=null &&  value.checked =="Y"){
								 serInxList.push(key1);
								 value1.forceAddRemoved =-1;
								 value1.isPackageService='N';
								 $scope.autoRenderedServicesForSaveAndUpdate.push(value1);
								 checkList = true;
							 }
							 
						 }); 
						 
					 }); 
					 if(checkList == true){
						 $scope.addOrUpdateServices(null,serInxList);
					 }else{
						 growl.warning("Please select atleast one service",{
								title : "Warning"
							});
						 $rootScope.stopLoaderPopup();
					 }
					
					 
				 }
				
			}
			
			$scope.getPackagesDetails = function(packageId,consumptionMasterId)
			{
				 
				var data={};
				data[consumptionMasterId]=packageId;   
			
			//get package details
				var URI = BASE_URL + ROOT_URL + "/api/packages/bill/package";
				BillingGenericService.serviceAction("POST", URI, data).then(function(response){
					if(response.data.status == "success")
						{
						
						   //console.log(response.data.listObject);
						   $scope.packageTypeId = response.data.listObject[0].mPackageMasterDto.packageTypeId;
						   $scope.listPackage = [];
						   $scope.listPackage = response.data.listObject[0];
						   if($scope.packageTypeId==1 || $scope.packageTypeId==5){
							   $scope.setFirstTabLevel(101);
			                   // added by sneha 
				    	    		var packageIds = [];
				    	    		packageIds.push(packageId);
				    	    		// get ehc package services
				    	    		var URI = BASE_URL + ROOT_URL + "/api/packages/bill/package/"+packageIds.toString();
				    				BillingGenericService.serviceAction("GET", URI, null).then(function(response){
				    					if(response.data.status == "success")
				    					{
				    						var ehcPackageServiceList = response.data.listObject[0].listTPackageServicesDetailsDto;
				    						console.log("list",$scope.listPackage);
				    						
				    						$scope.consulationserviceIdlIST  =[];
				    						
						                       angular.forEach(ehcPackageServiceList,function(val,i){
					    								if(val.consultationSpecialityId>0)
						    			    			{
					    									$scope.consulationserviceIdlIST.push(val);
							    			    				   
						    			    			}
					    						 });
						                       
						                       angular.forEach($scope.listPackage.listTPackageServicesDetailsDto,function(val,i){
						                    	   val.pushed = false;
						                       });
						                       
						                       var count = 0;
						                       angular.forEach($scope.consulationserviceIdlIST,function(val1,i1){
					    							
						    						angular.forEach($scope.listPackage.listTPackageServicesDetailsDto,function(val,i){
																if(val1.packageId == val.packageId && val1.serviceId == val.serviceId ){
																	
																	if(count == 0){
																		 if(val1.consultationSpecialityId>0)
											    			    			{
												    			    			if(val1.consultationSpecialityId>0 && val.pushed == false)
												    			    			{
												    			    				val.consultationSpecialityId =val1.consultationSpecialityId;
												    			    				val.pushed = true;
																					count++;
												    			    				   
												    			    			}
											    			    			  }
																	}else{
																		var found = $scope.listPackage.listTPackageServicesDetailsDto.find(ar=>ar.consultationSpecialityId == val1.consultationSpecialityId && ar.serviceId == val1.serviceId);
																		   if(found==undefined)
																			   {
																			   if(val1.consultationSpecialityId>0)
												    			    			{
													    			    			if(val1.consultationSpecialityId>0 && val.pushed == false)
													    			    			{
													    			    				val.consultationSpecialityId =val1.consultationSpecialityId;
													    			    				val.pushed = true;
													    			    				   
													    			    			}
												    			    			  }
											    							   }
																	}
																	
																	}
						    						
						    						    });
						    						});
						                       
						                       angular.forEach($scope.listPackage.listTPackageServicesDetailsDto,function(val,i){
				                                   angular.forEach($scope.specialityMasterList,function(spec,ispec){
				                                       if(val.consultationSpecialityId == spec.specialityId){
				                                           val.specialityName=spec.specialityName;

				                                          }
				                                       });  
					    						    });
				    						
				    						console.log("list3",$scope.listPackage.listTPackageServicesDetailsDto);
				    						$scope.packageDetailsResponse=$scope.listPackage;
				    					}
				    				});
				    				 
				  				     $('#packageServiceDetailsPopup').modal('show');
							   }
						   else if($scope.packageTypeId==2){
							   $scope.setFirstTabLevel(401);
							   $scope.packageDetailsResponse=response.data.listObject[0];
							   $('#packageServiceDetailsPopup').modal('show');
						   }
						   else if($scope.packageTypeId==3){
							   $scope.setFirstTabLevel(201);
							   $scope.packageDetailsResponse=response.data.listObject[0];
							   $('#packageServiceDetailsPopup').modal('show');
						   }else if($scope.packageTypeId==4){
							   $scope.setFirstTabLevel(301);
							   $scope.packageDetailsResponse=response.data.listObject[0];
							   $('#packageServiceDetailsPopup').modal('show');
						   }
						
					    
						}
						
				});
			}
			$scope.eoGroupServiceDtls=[];
			$scope.getGroupServiceDetails=function(list){
				
				$scope.eoGroupServiceDtls=list;
				 $('#qtyDetailsModal').modal('show');
				
			}
			$scope.setPackageOrderTabActive=function(){
				$scope.setFirstTabLevel(2);
				$scope.setSecondTabLevel(22);
			}

			//package transaction view END
			
			$scope.addCurrentPackageServices = function(){
					angular.forEach($scope.currentActivePackage.listPackageConsumptionServiceDetailDto,function(value,index){
						if(value.isSelected){
							var obj = {
									basePrice:0,
									concession:0,
									discount:0,
									isDiscountApplicable:"N",
									isQuantityEditable:"N",
									isRateEditable:"N",
									isTaxApplicable:"N",
									maxRateEditable:0,
									minRateEditable:0,
									packageMasterId:$scope.currentActivePackage.packageId,
									serviceMasterId:value.serviceId,
									serviceStandardCode:value.serviceCode + " (P)",
									serviceStandardName:value.serviceStandardName,
									quantity:1,
									specialityId:value.specialityId,
									specialityName:value.specialityName,
									subSpecialityId:value.subSpecialityId,
									subSpecialityName:value.subSpecialityName,
									taxPercentage:0,
									discountPercentage : 0,
									isPackageService:'Y',
									apportionedPrice : value.apportionedPrice,
									packageId : $scope.currentActivePackage.packageId,
									packageTypeId : $scope.currentActivePackage.packageTypeId
									};
							$scope.servicesListPopup.push(obj);
						}
					});
			}
			
			$scope.getRemoveServicePopup = function(item,idx)
		    {
		    	////console.log("ITEM",item);
				var i =0;
		    	   angular.forEach($scope.initialAutoRenderedServicesFinalList, function (value, key) {
			   			if(value.orderDetailsId == item.orderDetailsId){
			   				idx = i;
			   			 }
		   			i++;
		   			});
		    	   $scope.autoRenderedServicesFinalList =[]; 
		    	   $scope.autoRenderedServicesFinalList = $scope.initialAutoRenderedServicesFinalList;
		    	   
				$scope.cancelPopup = false;
				if(item.serviceMasterId == $scope.surchargeServiceId)
	    		{
	    			$scope.isPatientClass = "N";
	    		}
				
		    	$scope.objectIndexService = idx;
		    	$scope.serciveObjectForRemove = item;
		      angular.element('#serviceCancellationModal').modal('show');
		    	
		    }
			
			
			$scope.addCapAmout = function(info,list)
			{
				if((info.contractId>0 && info.creditPayable>0)||info.orderDetailsPackageMapperDtosList.find(pkg => pkg.isPackageService=='Y')||info.orderDetailsId>0)
		    	 {		
				var data = {
						admissionId : $scope.admissionId,
						specialityId : info.serviceSpecialityId,
						contractId : info.contractId,
						orderDetailsId : info.orderDetailsId,
						orderDetailsPayeeMapperDtosList : info.orderDetailsPayeeMapperDtosList,
						orderDetailsPackageMapperDtosList : info.orderDetailsPackageMapperDtosList,
						serviceId : info.serviceMasterId,
						createdBy : $scope.userId,
						quantity : info.quantity,
						ordCancelReasonId : $scope.servicesCancelReasonId,
		    			ordCancelRemark : $scope.servicesCancelRemark,
		    			unitId : $scope.unitId
				}
				
				if(info.saleId!=null && info.saleId>0)
					data.saleId = info.saleId;
				//console.log('AddCapAmt',data);
				//return false;
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL +  "/global/consumption/service/cancel";
				BillingGenericService
						.serviceAction(METHOD_POST,
								URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									//ServiceCount
									if($scope.autoRenderedServicesFinalList != null){
					                    $scope.ServiceCount = $scope.ServiceCount - info.quantity;
					                }
									$rootScope.stopLoaderPopup();
									if (response.data.status == "success")
									{
										$scope.servicesCancelReasonId = null;
						            	$scope.servicesCancelRemark = '';
						            	//$scope.calculationTest(list,init);
						            	$scope.autoRenderedServicesFinalList=[];
						            	$scope.autoRenderedServicesFinalList = list;
						            	$scope.resetTotalAmounts();
                                         growl.success(response.data.message,
                                         {
                                             title : 'success'
                                         });
									}else{
										$scope.servicesCancelReasonId = null;
	    				            	$scope.servicesCancelRemark = '';	
	    				            	$scope.autoRenderedServicesFinalList=[];
						            	$scope.autoRenderedServicesFinalList = list;
						            	$scope.resetTotalAmounts();
                                        growl.error(response.data.message,
                                        {
                                            title : 'error'
                                        });
									}
								});
		    	 }
			}
			
			/*
			$scope.addCapAmout = function(info)
			{
				var data = {
						admissionId : $scope.admissionId,
						subSpecialityId : info.serviceSubSpecialityId,
						contractId : info.contractId,
						creaditPayable : info.creditPayable
				}
				//console.log('AddCapAmt',data);
				return false;
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL +  "/global/consumption/service/cancel";
				BillingGenericService
						.serviceAction(METHOD_POST,
								URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									if (response.data.status == "success")
									{
									}
								});

			}*/
			
			//FOr Split New
			
		    $scope.getPayeeCreditDetailsSplit = function(itemList,idx)
		    {
		        $scope.payeeeIdxService = idx;
		        $scope.payeeCreditDetailslListSplit = [];
		        //$scope.payeeCreditDetailslListSplit = itemList;
		        
		        angular.forEach(itemList, function (value, key) {
		    		value.creditPayable = parseFloat(value.creditPayable).toFixed(2);
		    		$scope.payeeCreditDetailslListSplit.push(value);
		    	});
		        
		    }
		    $scope.gotoMedicationDetails = function(bill){
		    	bill.isFromIPDCharges=true;
		    	var date=bill.doa.split("/");
		    	var newDate=date[2]+"-"+date[1]+"-"+date[0]; 
		    	bill.visitDate=newDate;
		    	
		    	var pdd=bill.pdd.split("/");
		    	var newPdd=pdd[2]+"-"+pdd[1]+"-"+pdd[0]; 
		    	bill.pdd=newPdd;
				$timeout(function () { 
						$state.go('medicinesAddSub', 
								{  
									obj : bill 
								});
				}, 1000); 
			} 
			
			  $scope.getVirtualStore=function(){
			    	 
				  var obj={
						  unitId : $scope.unitId,
		    	  }
				    	  var URI = BASE_URL + ROOT_URL + "/api/global/getVirtualStoreId";
				
				GenericService.serviceAction("POST", URI, obj).then(function(response) {
				$scope.orderedMedicationList = [];
				
						if (response.data.status == "success") {
							$scope.virtualStoreId = response.data.listObject[0].virtualStoreId;
						
						}
				});
		      }

		    
		    var isItemExists;
			function checkItemIdExistsOrNot(arr, val) {
				return arr
						.some(function(arr) {
							if(val === arr.payeeId )
								{
									return true;
								}
							else
								{
									return false;
								}
						});
			}
			
		    
		    $scope.initForPayeeList = function(itemList,idx){
		    	if(itemList.length != $scope.patientSponsorDetailsDtosListSplit.length)
				{
		    		
		    		if(itemList.length == 1){
		    			
		    			var tmpItemList = angular.copy($scope.serviceList[idx].orderDetailsPayeeMapperDtosList);
		    			$scope.serviceList[idx].orderDetailsPayeeMapperDtosList = [];
						  
		    			var cooount = 1;
						angular.forEach($scope.patientSponsorDetailsDtosListSplit,function(value,index)
								 {
							
									if(value.priorityId == '1')
									{
										var newTmpArr = {};
										
										
										newTmpArr.associateCompanyDesc = value.insuranceDesc;
										newTmpArr.associateCompanyId = value.insuranceId;
										newTmpArr.coShare =  null;
										newTmpArr.contractId = value.contractId;
										newTmpArr.creditPayable = parseFloat(0);
										newTmpArr.id = null;
										newTmpArr.isInvoiceGenerated = "N";
										newTmpArr.isRateEditable = null;
										newTmpArr.isTariffRateApplicable= "N";
										newTmpArr.orderDetailsId = value.orderDetailsId;
										newTmpArr.organizationId = $scope.organizationId;
										newTmpArr.payeeAmount =  parseFloat(0);
										newTmpArr.payeeDesc =  value.companyDesc;
										newTmpArr.payeeId = value.payeeId;
										newTmpArr.selfPayable =  0;
										newTmpArr.serviceId = value.serviceMasterId;
										newTmpArr.spiltBillAmt = parseFloat(0);
										newTmpArr.status =  'A';
										newTmpArr.orderDetailsId =  $scope.serviceList[idx].orderDetailsId;
										newTmpArr.unitId = $scope.unitId;
										$scope.serviceList[idx].orderDetailsPayeeMapperDtosList.push(newTmpArr);
										
										//$scope.payeeCreditDetailslListSplit.push(newTmpArr);
										
									}
									else{
										if(value.payeeId != $scope.selfPayeeId){
											var newTmpArr = {};
											newTmpArr.associateCompanyDesc = value.insuranceDesc;
											newTmpArr.associateCompanyId = value.insuranceId;
											newTmpArr.coShare =  null;
											newTmpArr.contractId = value.contractId;
											newTmpArr.creditPayable = parseFloat(0);
											newTmpArr.id = null;
											newTmpArr.isInvoiceGenerated = "N";
											newTmpArr.isRateEditable = null;
											newTmpArr.isTariffRateApplicable= "N";
											newTmpArr.orderDetailsId = value.orderDetailsId;
											newTmpArr.organizationId = $scope.organizationId;
											newTmpArr.payeeAmount =  parseFloat(0);
											newTmpArr.payeeDesc =  value.companyDesc;
											newTmpArr.payeeId = value.payeeId;
											newTmpArr.selfPayable =  0;
											newTmpArr.serviceId = value.serviceMasterId;
											newTmpArr.spiltBillAmt = parseFloat(0);
											newTmpArr.status =  'A';
											newTmpArr.orderDetailsId =  $scope.serviceList[idx].orderDetailsId;
											newTmpArr.unitId = $scope.unitId;
											$scope.serviceList[idx].orderDetailsPayeeMapperDtosList.push(newTmpArr);
										}
									}
									
									if(cooount == $scope.patientSponsorDetailsDtosListSplit.length){
										
										angular.forEach(tmpItemList,function(value,index)
												 {
											$scope.serviceList[idx].orderDetailsPayeeMapperDtosList.push(value);
												 });
										$scope.calculateSplitBillTotal($scope.serviceList);
									}
									
									cooount++;
									
								});
		    		}
		    		
		    		if(itemList.length > 1){
		    			var cooount = 1;
		    			angular.forEach($scope.patientSponsorDetailsDtosListSplit, function (value, key) {
		    				
		    				if(value.priorityId == 1){
		    					
		    					
		    					isItemExists=checkItemIdExistsOrNot(itemList,value.payeeId)
								  if (!isItemExists) {
									  
									  var tmpItemList = angular.copy($scope.serviceList[idx].orderDetailsPayeeMapperDtosList);
									  $scope.serviceList[idx].orderDetailsPayeeMapperDtosList = [];
									  
									  var newTmpArr = {};
										
										
										newTmpArr.associateCompanyDesc = value.insuranceDesc;
										newTmpArr.associateCompanyId = value.insuranceId;
										newTmpArr.coShare =  null;
										newTmpArr.contractId = value.contractId;
										newTmpArr.creditPayable = parseFloat(0);
										newTmpArr.id = null;
										newTmpArr.isInvoiceGenerated = "N";
										newTmpArr.isRateEditable = null;
										newTmpArr.isTariffRateApplicable= "N";
										newTmpArr.orderDetailsId = value.orderDetailsId;
										newTmpArr.organizationId = $scope.organizationId;
										newTmpArr.payeeAmount =  parseFloat(0);
										newTmpArr.payeeDesc =  value.companyDesc;
										newTmpArr.payeeId = value.payeeId;
										newTmpArr.selfPayable =  0;
										newTmpArr.serviceId = value.serviceMasterId;
										newTmpArr.spiltBillAmt = parseFloat(0);
										newTmpArr.status =  'A';
										newTmpArr.orderDetailsId =  $scope.serviceList[idx].orderDetailsId;
										newTmpArr.unitId = $scope.unitId;
										$scope.serviceList[idx].orderDetailsPayeeMapperDtosList.push(newTmpArr);
										angular.forEach(tmpItemList,function(value,index)
												 {
											$scope.serviceList[idx].orderDetailsPayeeMapperDtosList.push(value);
												 });
									  //$scope.selectedItemList.push(value);
								  }
		    					
		    					
		    					
		    				}else{
		    					isItemExists=checkItemIdExistsOrNot(itemList,value.payeeId)
								  if (!isItemExists) {
									  var newTmpArr = {};
										
										
										newTmpArr.associateCompanyDesc = value.insuranceDesc;
										newTmpArr.associateCompanyId = value.insuranceId;
										newTmpArr.coShare =  null;
										newTmpArr.contractId = value.contractId;
										newTmpArr.creditPayable = parseFloat(0);
										newTmpArr.id = null;
										newTmpArr.isInvoiceGenerated = "N";
										newTmpArr.isRateEditable = null;
										newTmpArr.isTariffRateApplicable= "N";
										newTmpArr.orderDetailsId = value.orderDetailsId;
										newTmpArr.organizationId = $scope.organizationId;
										newTmpArr.payeeAmount =  parseFloat(0);
										newTmpArr.payeeDesc =  value.companyDesc;
										newTmpArr.payeeId = value.payeeId;
										newTmpArr.selfPayable =  0;
										newTmpArr.serviceId = value.serviceMasterId;
										newTmpArr.spiltBillAmt = parseFloat(0);
										newTmpArr.status =  'A';
										newTmpArr.orderDetailsId =  $scope.serviceList[idx].orderDetailsId;
										newTmpArr.unitId = $scope.unitId;
										$scope.serviceList[idx].orderDetailsPayeeMapperDtosList.push(newTmpArr);
								  }
		    				}
		        			
		        			//$scope.encounterDetails.payeeId
		    				if(cooount == $scope.patientSponsorDetailsDtosListSplit.length){
								$scope.calculateSplitBillTotal($scope.serviceList);
							}
							
							cooount++;
		        		
		        		});
		    		}
		    	
				}
		    	
		    }
		    
		    
		    
		    $scope.getSmartCardBalance = function(){
		        
		    }
		    
		    $scope.getAuthorizationSelfUpdate = function(item,idx){
				
				if(item.selfPayable == '' || item.selfPayable == null || item.selfPayable == undefined){
					item.selfPayable = parseFloat(0);
				}
				
				
				
				if(parseFloat(item.selfPayable)  == parseFloat(item.selfPayableOldC)){
					return false;
				}
				
				if(parseFloat(item.selfPayable) > parseFloat(item.netPay)){
					growl.warning("You can't add more than net amount.",{
						title : ERROR_MSG
					});
					item.selfPayable = parseFloat(item.selfPayableOldC);
					return false;
				}
				
				if(parseFloat(item.selfPayable) == parseFloat(item.netPay)){
					
					item.creditPayable = parseFloat(0);
					item.selfPayableOldC = parseFloat(item.selfPayable);
					//item.orderDetailsPayeeMapperDtosList = [];
					angular.forEach(item.orderDetailsPayeeMapperDtosList,function(value,index)
							 {
								if(value.payeeId != $scope.selfPayeeId){
									//item.orderDetailsPayeeMapperDtosList.splice(index, 1);
									value.creditPayable = parseFloat(0);
									value.payeeAmount = parseFloat(0);
									value.spiltBillAmt = parseFloat(0);
								}
							 });
					
					$scope.calculateSplitBillTotal($scope.serviceList);
					return false;
					
				}
				
				
				if(item.orderDetailsPayeeMapperDtosList.length == 1){
					
					var creditAmount = parseFloat(item.netPay) - parseFloat(item.selfPayable);
					item.creditPayable = parseFloat(creditAmount);
					item.selfPayableOldC = parseFloat(item.selfPayable);
					if(parseFloat(creditAmount) > 0){
						
						$scope.serviceList[idx].orderDetailsPayeeMapperDtosList[0].selfPayable = (parseFloat($scope.serviceList[idx].orderDetailsPayeeMapperDtosList[0].selfPayable) - parseFloat(creditAmount)).toFixed(2);
						$scope.serviceList[idx].orderDetailsPayeeMapperDtosList[0].payeeAmount = (parseFloat($scope.serviceList[idx].orderDetailsPayeeMapperDtosList[0].payeeAmount) - parseFloat(creditAmount)).toFixed(2);
						
		                var tmpListPayy = [];
		                tmpListPayy = angular.copy($scope.serviceList[idx].orderDetailsPayeeMapperDtosList);
		                $scope.serviceList[idx].orderDetailsPayeeMapperDtosList = [];
		                var cooount = 1;
						
						angular.forEach($scope.patientSponsorDetailsDtosListSplit,function(value,index)
								 {
									if(value.priorityId == '1')
									{
										var newTmpArr = {};
										//tmpListPayy = angular.copy($scope.serviceList[idx].orderDetailsPayeeMapperDtosList);
										//$scope.serviceList[idx].orderDetailsPayeeMapperDtosList = [];
										newTmpArr.associateCompanyDesc = value.insuranceDesc;
										newTmpArr.associateCompanyId = value.insuranceId;
										newTmpArr.coShare =  null;
										newTmpArr.contractId = value.contractId;
										newTmpArr.creditPayable = parseFloat(creditAmount);
										newTmpArr.id = null;
										newTmpArr.isInvoiceGenerated = "N";
										newTmpArr.isRateEditable = null;
										newTmpArr.isTariffRateApplicable= "N";
										newTmpArr.orderDetailsId = value.orderDetailsId;
										newTmpArr.organizationId = $scope.organizationId;
										newTmpArr.payeeAmount =  parseFloat(creditAmount);
										newTmpArr.payeeDesc =  value.companyDesc;
										newTmpArr.payeeId = value.payeeId;
										newTmpArr.selfPayable =  0;
										newTmpArr.serviceId = value.serviceMasterId;
										newTmpArr.spiltBillAmt = parseFloat(creditAmount);
										newTmpArr.status =  'A';
										newTmpArr.orderDetailsId =  $scope.serviceList[idx].orderDetailsId;
										newTmpArr.unitId = $scope.unitId;
										$scope.serviceList[idx].orderDetailsPayeeMapperDtosList.push(newTmpArr);
										/*angular.forEach(tmpListPayy,function(value,index)
												 {
													$scope.serviceList[idx].orderDetailsPayeeMapperDtosList.push(value);
												 });
										$scope.calculateSplitBillTotal($scope.serviceList);*/
										//$scope.payeeCreditDetailslListSplit.push(newTmpArr);
										
									}
									else{
										if(value.payeeId != $scope.selfPayeeId){
											var newTmpArr = {};
											newTmpArr.associateCompanyDesc = value.insuranceDesc;
											newTmpArr.associateCompanyId = value.insuranceId;
											newTmpArr.coShare =  null;
											newTmpArr.contractId = value.contractId;
											newTmpArr.creditPayable = parseFloat(0);
											newTmpArr.id = null;
											newTmpArr.isInvoiceGenerated = "N";
											newTmpArr.isRateEditable = null;
											newTmpArr.isTariffRateApplicable= "N";
											newTmpArr.orderDetailsId = value.orderDetailsId;
											newTmpArr.organizationId = $scope.organizationId;
											newTmpArr.payeeAmount =  parseFloat(0);
											newTmpArr.payeeDesc =  value.companyDesc;
											newTmpArr.payeeId = value.payeeId;
											newTmpArr.selfPayable =  0;
											newTmpArr.serviceId = value.serviceMasterId;
											newTmpArr.spiltBillAmt = parseFloat(0);
											newTmpArr.status =  'A';
											newTmpArr.orderDetailsId =  $scope.serviceList[idx].orderDetailsId;
											newTmpArr.unitId = $scope.unitId;
											$scope.serviceList[idx].orderDetailsPayeeMapperDtosList.push(newTmpArr);
										}
									}
									
									if(cooount == $scope.patientSponsorDetailsDtosListSplit.length){
										angular.forEach(tmpListPayy,function(value,index)
												 {
													$scope.serviceList[idx].orderDetailsPayeeMapperDtosList.push(value);
												 });
										$scope.calculateSplitBillTotal($scope.serviceList);
									}
									
									cooount++;
								});
						
					}
				}
				else{
					
					//alert(item.orderDetailsPayeeMapperDtosList.length);
					////console.log("dd",item.orderDetailsPayeeMapperDtosList);
					
					var creditAmount = parseFloat(item.netPay) - parseFloat(item.selfPayable);
					item.creditPayable = parseFloat(creditAmount);
					
					if(parseFloat(item.selfPayable) > parseFloat(item.selfPayableOldC)){
						var tmpSelf = parseFloat(item.selfPayable) - parseFloat(item.selfPayableOldC);
						if(parseFloat(item.orderDetailsPayeeMapperDtosList[0].creditPayable) >= parseFloat(tmpSelf)){
							item.orderDetailsPayeeMapperDtosList[0].creditPayable = parseFloat(item.orderDetailsPayeeMapperDtosList[0].creditPayable) - parseFloat(tmpSelf);
							item.orderDetailsPayeeMapperDtosList[0].spiltBillAmt = parseFloat(item.orderDetailsPayeeMapperDtosList[0].spiltBillAmt) - parseFloat(tmpSelf);
							item.orderDetailsPayeeMapperDtosList[0].payeeAmount = parseFloat(item.orderDetailsPayeeMapperDtosList[0].payeeAmount) - parseFloat(tmpSelf);
							item.selfPayableOldC = item.selfPayable;
							
							angular.forEach(item.orderDetailsPayeeMapperDtosList,function(value1,index)
									 {
										//$scope.serviceList[idx].orderDetailsPayeeMapperDtosList.push(value);
										if(value1.payeeId == $scope.selfPayeeId){
											
											value1.creditPayable = parseFloat(0);
											value1.spiltBillAmt =  parseFloat(0);
											value1.selfPayable = (parseFloat(value1.selfPayable) +parseFloat(tmpSelf)).toFixed(2);
											value1.payeeAmount = (parseFloat(value1.payeeAmount) +parseFloat(tmpSelf)).toFixed(2);
											
										}
										
									 });
							
							$scope.calculateSplitBillTotal($scope.serviceList);
						}else{
							
							if(item.orderDetailsPayeeMapperDtosList.length == 2){
								
								item.orderDetailsPayeeMapperDtosList[0].creditPayable = parseFloat(0);
								item.orderDetailsPayeeMapperDtosList[0].spiltBillAmt = parseFloat(0);
								item.orderDetailsPayeeMapperDtosList[0].payeeAmount =parseFloat(0);
								item.selfPayableOldC = item.selfPayable;
								
								angular.forEach(item.orderDetailsPayeeMapperDtosList,function(value1,index)
										 {
											//$scope.serviceList[idx].orderDetailsPayeeMapperDtosList.push(value);
											if(value1.payeeId == $scope.selfPayeeId){
												
												value1.creditPayable = parseFloat(0);
												value1.spiltBillAmt =  parseFloat(0);
												value1.selfPayable = (parseFloat(value1.selfPayable) +parseFloat(tmpSelf)).toFixed(2);
												value1.payeeAmount = (parseFloat(value1.payeeAmount) +parseFloat(tmpSelf)).toFixed(2);
												
											}
											
										 });
								
								$scope.calculateSplitBillTotal($scope.serviceList);
							}
							
							if(item.orderDetailsPayeeMapperDtosList.length > 2){
								var tmpSelfTmp = parseFloat(tmpSelf);
								var count = 1;
								angular.forEach(item.orderDetailsPayeeMapperDtosList,function(value,index)
										 {
											//$scope.serviceList[idx].orderDetailsPayeeMapperDtosList.push(value);
											if(value.payeeId != $scope.selfPayeeId){
												if(parseFloat(tmpSelfTmp) > 0 && parseFloat(value.creditPayable) > 0){
													if(parseFloat(tmpSelfTmp) > parseFloat(value.creditPayable)){
														tmpSelfTmp = parseFloat(tmpSelfTmp) - parseFloat(value.creditPayable);
														value.creditPayable = parseFloat(0);
														value.spiltBillAmt =   parseFloat(0);
														value.payeeAmount = parseFloat(0);
														
													}else{
													
														//value.selfPayable = parseFloat(value.selfPayable) + parseFloat(tmpSelfTmp);
														//value.payeeAmount = parseFloat(value.payeeAmount)+ parseFloat(tmpSelfTmp);
														value.creditPayable = (parseFloat(value.creditPayable) - parseFloat(tmpSelfTmp)).toFixed(2);
														value.spiltBillAmt =  (parseFloat(value.spiltBillAmt) - parseFloat(tmpSelfTmp)).toFixed(2);
														value.payeeAmount =(parseFloat(value.payeeAmount) - parseFloat(tmpSelfTmp)).toFixed(2);
														value.selfPayable = parseFloat(0);
														tmpSelfTmp = parseFloat(0);
														
													}
												
												}
												
											}
											if(count == item.orderDetailsPayeeMapperDtosList.length){
												item.selfPayableOldC = item.selfPayable;
												$scope.calculateSplitBillTotal($scope.serviceList);
											}
											count++;
										 });
								
							}
						}
						
						 
					}
					if(parseFloat(item.selfPayable) < parseFloat(item.selfPayableOldC)){
						var tmpSelf = parseFloat(item.selfPayableOldC) - parseFloat(item.selfPayable);
							item.orderDetailsPayeeMapperDtosList[0].creditPayable = parseFloat(item.orderDetailsPayeeMapperDtosList[0].creditPayable) + parseFloat(tmpSelf);
							item.orderDetailsPayeeMapperDtosList[0].spiltBillAmt = parseFloat(item.orderDetailsPayeeMapperDtosList[0].spiltBillAmt)+ parseFloat(tmpSelf);
							item.orderDetailsPayeeMapperDtosList[0].payeeAmount = parseFloat(item.orderDetailsPayeeMapperDtosList[0].payeeAmount) + parseFloat(tmpSelf);
							item.selfPayableOldC = item.selfPayable;
							angular.forEach(item.orderDetailsPayeeMapperDtosList,function(value1,index)
									 {
										//$scope.serviceList[idx].orderDetailsPayeeMapperDtosList.push(value);
										if(value1.payeeId == $scope.selfPayeeId){
											
											value1.creditPayable = parseFloat(0);
											value1.spiltBillAmt =  parseFloat(0);
											value1.selfPayable = parseFloat(value1.selfPayable) -parseFloat(tmpSelf);
											value1.payeeAmount = parseFloat(value1.payeeAmount) -parseFloat(tmpSelf);
											
										}
										
									 });
							
							$scope.calculateSplitBillTotal($scope.serviceList);
						 
					}
					
					//item.orderDetailsPayeeMapperDtosList
					
				}
				
			}
			
			
		    $scope.getAuthorizationCreditUpdate = function(item){
				//serviceList
			}
		    
		    
		    $scope.selfSlitBillAmount =false;
			$scope.getAuthorizationSelfUpdateTotal = function(selfVal){
				$scope.selfSlitBillAmount =true;
                $scope.newSelfVal = selfVal;
				if(parseFloat(selfVal) > parseFloat($scope.totalSelfPaybleSplitBillOld)){
						var diffSelf = parseFloat(selfVal) - parseFloat($scope.totalSelfPaybleSplitBillOld);
						if(parseFloat(diffSelf) > parseFloat($scope.totalCreditPaybleSplitBill)){
							growl.warning("You can't add more than net amount.("+parseFloat($scope.totalNetAmt)+").",{
								title : WARNING_MSG
							});
							$scope.calculateSplitBillTotal($scope.serviceList);
							//return false;
						}else{
							
							$scope.co = 1;
							angular.forEach($scope.serviceList,function(value,index)
									 {
										 if(parseFloat(value.netPay) > parseFloat(value.selfPayable)){
								 if(parseFloat(value.netPay) > parseFloat(diffSelf) && value.orderDetailsPayeeMapperDtosList.length > 1 && parseFloat(diffSelf) > 0){
									     // value.selfPayable =  parseFloat(value.selfPayable) + parseFloat(diffSelf);
													 if( parseFloat(value.creditPayable) > parseFloat(diffSelf) && parseFloat(diffSelf) > 0){
														 value.selfPayable =  (parseFloat(value.selfPayable) + parseFloat(diffSelf)).toFixed(2);
														 value.creditPayable =  (parseFloat(value.creditPayable) - parseFloat(diffSelf)).toFixed(2);
														 value.creditPayableOldC = parseFloat(value.creditPayable).toFixed(2);
														 value.selfPayableOldC = parseFloat(value.selfPayable).toFixed(2);
														 var diffSelfTmp = parseFloat(diffSelf);
														 angular.forEach(value.orderDetailsPayeeMapperDtosList,function(value1,index1)
																 {
															 		if(value1.payeeId == $scope.selfPayeeId){
															 			value1.payeeAmount = (parseFloat(value1.payeeAmount)  + parseFloat(diffSelfTmp)).toFixed(2); 
															 			value1.selfPayable = (parseFloat(value1.selfPayable) + parseFloat(diffSelfTmp)).toFixed(2);
															 		}else{
															 			if(parseFloat(value1.creditPayable) > parseFloat(diffSelf)){
															 				value1.creditPayable = (parseFloat(value1.creditPayable) - parseFloat(diffSelf)).toFixed(2);
																 			value1.payeeAmount = (parseFloat(value1.payeeAmount) -  parseFloat(diffSelf)).toFixed(2);
																 			value1.spiltBillAmt = (parseFloat(value1.spiltBillAmt) - parseFloat(diffSelf)).toFixed(2);
																 			diffSelf = parseFloat(0);
															 			}else{
															 				value1.creditPayable = parseFloat(0);
																 			value1.payeeAmount = parseFloat(0);
																 			value1.spiltBillAmt = parseFloat(0);
																 			diffSelf = parseFloat(diffSelf) - parseFloat(value1.creditPayable);
															 			}
															 		}
															 
																 });
													 }
													 
													 
	                                                     if( parseFloat(value.creditPayable) <= parseFloat(diffSelf) && parseFloat(diffSelf) > 0){

														 value.selfPayable =  (parseFloat(value.selfPayable) + parseFloat(value.creditPayable)).toFixed(2);
														 diffSelf = parseFloat(diffSelf) - parseFloat(value.creditPayable);
														// value.creditPayable =  parseFloat(0);
														 value.selfPayableOldC = parseFloat(value.selfPayable).toFixed(2);
														 var countList = 1;
														 angular.forEach(value.orderDetailsPayeeMapperDtosList,function(value1,index1)
																 {
															 		if(value1.payeeId == $scope.selfPayeeId){
															 			value1.payeeAmount = (parseFloat(value1.payeeAmount)  + parseFloat(value.creditPayable)).toFixed(2);
															 			value1.selfPayable = (parseFloat(value1.selfPayable) + parseFloat(value.creditPayable)).toFixed(2);
															 		}else{
															 			
															 				value1.creditPayable = parseFloat(0);
																 			value1.payeeAmount = parseFloat(0);
																 			value1.spiltBillAmt = parseFloat(0);
																 			value1.selfPayable = parseFloat(0);
															 			
															 		}
															 		if(value.orderDetailsPayeeMapperDtosList.length == countList){
															 			value.creditPayable =  parseFloat(0);
															 			 value.creditPayableOldC = parseFloat(value.creditPayable);
															 		}
															 		countList ++;
															 
																 });
													 }
												 
											 }
								 if(parseFloat(value.netPay) < parseFloat(diffSelf) && value.orderDetailsPayeeMapperDtosList.length > 1 && parseFloat(diffSelf) > 0){
								     // value.selfPayable =
										// parseFloat(value.selfPayable) +
										// parseFloat(diffSelf);
												 if( parseFloat(value.creditPayable) > parseFloat(diffSelf) && parseFloat(diffSelf) > 0){
													 value.selfPayable =  (parseFloat(value.selfPayable) + parseFloat(diffSelf)).toFixed(2);
													 value.creditPayable =  (parseFloat(value.creditPayable) - parseFloat(diffSelf)).toFixed(2);
													 value.creditPayableOldC = parseFloat(value.creditPayable).toFixed(2);
													 value.selfPayableOldC = parseFloat(value.selfPayable).toFixed(2);
													 var diffSelfTmp = parseFloat(diffSelf);
													 angular.forEach(value.orderDetailsPayeeMapperDtosList,function(value1,index1)
															 {
														 		if(value1.payeeId == $scope.selfPayeeId){
														 			value1.payeeAmount = (parseFloat(value1.payeeAmount)  + parseFloat(diffSelfTmp)).toFixed(2); 
														 			value1.selfPayable = (parseFloat(value1.selfPayable) + parseFloat(diffSelfTmp)).toFixed(2);
														 		}else{
														 			if(parseFloat(value1.creditPayable) > parseFloat(diffSelf)){
														 				value1.creditPayable = (parseFloat(value1.creditPayable) - parseFloat(diffSelf)).toFixed(2);
															 			value1.payeeAmount = (parseFloat(value1.payeeAmount) -  parseFloat(diffSelf)).toFixed(2);
															 			value1.spiltBillAmt = (parseFloat(value1.spiltBillAmt) - parseFloat(diffSelf)).toFixed(2);
															 			diffSelf = parseFloat(0);
														 			}else{
														 				value1.creditPayable = parseFloat(0);
															 			value1.payeeAmount = parseFloat(0);
															 			value1.spiltBillAmt = parseFloat(0);
															 			diffSelf = parseFloat(diffSelf) - parseFloat(value1.creditPayable);
														 			}
														 		}
														 
															 });
												 }
												 
												 
                                                 if( parseFloat(value.creditPayable) <= parseFloat(diffSelf) && parseFloat(diffSelf) > 0){
													 value.selfPayable =  (parseFloat(value.selfPayable) + parseFloat(value.creditPayable)).toFixed(2);
													 diffSelf = parseFloat(diffSelf) - parseFloat(value.creditPayable);
													// value.creditPayable =
													// parseFloat(0);
													 value.selfPayableOldC = parseFloat(value.selfPayable).toFixed(2);
													 var countList = 1;
													 angular.forEach(value.orderDetailsPayeeMapperDtosList,function(value1,index1)
															 {
														 		if(value1.payeeId == $scope.selfPayeeId){
														 			value1.payeeAmount = (parseFloat(value1.payeeAmount)  + parseFloat(value.creditPayable)).toFixed(2);
														 			value1.selfPayable = (parseFloat(value1.selfPayable) + parseFloat(value.creditPayable)).toFixed(2);
														 		}else{
														 			
														 				value1.creditPayable = parseFloat(0);
															 			value1.payeeAmount = parseFloat(0);
															 			value1.spiltBillAmt = parseFloat(0);
															 			value1.selfPayable = parseFloat(0);
														 			
														 		}
														 		if(value.orderDetailsPayeeMapperDtosList.length == countList){
														 			value.creditPayable =  parseFloat(0);
														 			 value.creditPayableOldC = parseFloat(value.creditPayable);
														 		}
														 		countList ++;
														 
															 });
												 }
											 
										 }
										 }
										 
										 
										 if($scope.serviceList.length == $scope.co){
											 $scope.calculateSplitBillTotal($scope.serviceList);
										 }
										 $scope.co ++;
									 });
						}
				}
				
				if(parseFloat($scope.totalSelfPaybleSplitBillOld) > parseFloat(selfVal)){
					var diffSelf =  parseFloat($scope.totalSelfPaybleSplitBillOld) - parseFloat(selfVal);
					$scope.co = 1;
					angular.forEach($scope.serviceList,function(value,index)
							 {
									if(value.orderDetailsPayeeMapperDtosList.length > 1){
                                        if(parseFloat(diffSelf) >= parseFloat(value.selfPayable) && parseFloat(diffSelf) > 0){
											 diffSelf = parseFloat(diffSelf) - parseFloat(value.selfPayable);
											 value.creditPayable = (parseFloat(value.creditPayable) + parseFloat(value.selfPayable)).toFixed(2);
											 value.creditPayable = parseFloat(value.creditPayable).toFixed(2);
											 value.creditPayableOldC = parseFloat(value.creditPayable).toFixed(2);
											 var coChild = 1;
											 angular.forEach(value.orderDetailsPayeeMapperDtosList,function(value1,index1)
													 {
												 		if(value1.payeeId != $scope.selfPayeeId){
												 			value1.creditPayable = (parseFloat(value1.creditPayable) + parseFloat(value.selfPayable)).toFixed(2);
												 			value1.payeeAmount = (parseFloat(value1.payeeAmount) +  parseFloat(value.selfPayable)).toFixed(2);
												 			value1.spiltBillAmt = (parseFloat(value1.spiltBillAmt) + parseFloat(value.selfPayable)).toFixed(2);
												 			value.selfPayable = parseFloat(0);
												 			 value.selfPayableOldC = parseFloat(value.selfPayable);
												 		}else{
												 			value1.payeeAmount = parseFloat(0);
												 			value1.selfPayable = parseFloat(0);
												 			value1.spiltBillAmt = parseFloat(0);
												 			value1.creditPayable = parseFloat(0);
												 		}
												 		if(value.orderDetailsPayeeMapperDtosList.length == coChild)
												 			{
												 				value.selfPayable = parseFloat(0);
												 				 value.selfPayableOldC = parseFloat(value.selfPayable);
												 			}
												 		coChild++;
												 
													 });

										 }
										 
                                         if(parseFloat(diffSelf) == 0 && parseFloat($scope.newSelfVal) != parseFloat($scope.totalSelfPaybleSplitBill)){
											 
			                    	         value.creditPayable = (parseFloat(value.creditPayable) + parseFloat(value.selfPayable));
											 value.creditPayable = parseFloat(value.creditPayable).toFixed(2);
											 value.selfPayable = 0.00;
											 value.selfPayableOldC = parseFloat(value.selfPayable).toFixed(2);
											 value.selfPayable = parseFloat(diffSelf);
											 
											 var coChild = 1;
											 var diffSelfTmp1 =parseFloat(diffSelf); 
											 angular.forEach(value.orderDetailsPayeeMapperDtosList,function(value1,index1)
													 {
												 		if(value1.payeeId != $scope.selfPayeeId){
												 			value1.creditPayable = (parseFloat(value1.creditPayable) + parseFloat(diffSelf)).toFixed(2);
												 			value1.payeeAmount = (parseFloat(value1.payeeAmount) + parseFloat(diffSelf)).toFixed(2);
												 			value1.spiltBillAmt = (parseFloat(value1.spiltBillAmt) + parseFloat(diffSelf)).toFixed(2);
												 			diffSelf = parseFloat(diffSelf) - parseFloat(diffSelf);
												 		}else{
												 			value1.payeeAmount = (parseFloat(value1.payeeAmount) - parseFloat(diffSelfTmp1)).toFixed(2);
												 			value1.selfPayable = (parseFloat(value1.selfPayable)- parseFloat(diffSelfTmp1)).toFixed(2);
												 			value1.creditPayable = parseFloat(0);
												 		}
												 		if(value.orderDetailsPayeeMapperDtosList.length == coChild)
												 			{
												 			   diffSelf = parseFloat(0);
												 			}
												 		coChild++;
												 
													 });
											 
										 }
										 
										 if(parseFloat(diffSelf) < parseFloat(value.selfPayable)){
											// diffSelf = parseFloat(0);
											 value.creditPayable = (parseFloat(value.creditPayable) + parseFloat(diffSelf)).toFixed(2);
											 value.creditPayable = parseFloat(value.creditPayable).toFixed(2);
											 value.selfPayable = (parseFloat(value.selfPayable) - parseFloat(diffSelf)).toFixed(2);
											 value.selfPayable = parseFloat(value.selfPayable).toFixed(2);
											 value.selfPayableOldC = parseFloat(value.selfPayable).toFixed(2);
											 value.creditPayableOldC = parseFloat(value.creditPayable).toFixed(2);
											 var coChild = 1;
											 var diffSelfTmp1 =parseFloat(diffSelf); 
											 angular.forEach(value.orderDetailsPayeeMapperDtosList,function(value1,index1)
													 {
												 		if(value1.payeeId != $scope.selfPayeeId){
												 			value1.creditPayable = (parseFloat(value1.creditPayable) + parseFloat(diffSelf)).toFixed(2);
												 			value1.payeeAmount = (parseFloat(value1.payeeAmount) + parseFloat(diffSelf)).toFixed(2);
												 			value1.spiltBillAmt = (parseFloat(value1.spiltBillAmt) + parseFloat(diffSelf)).toFixed(2);
												 			diffSelf = parseFloat(diffSelf) - parseFloat(diffSelf);
												 		}else{
												 			value1.payeeAmount = (parseFloat(value1.payeeAmount) - parseFloat(diffSelfTmp1)).toFixed(2);
												 			value1.selfPayable = (parseFloat(value1.selfPayable)- parseFloat(diffSelfTmp1)).toFixed(2);
												 			value1.creditPayable = parseFloat(0);
												 		}
												 		if(value.orderDetailsPayeeMapperDtosList.length == coChild)
												 			{
												 			   diffSelf = parseFloat(0);
												 			}
												 		coChild++;
												 
													 });

										 }
									}
								
//								 if($scope.serviceList.length == co){
								
									 $scope.calculateSplitBillTotal($scope.serviceList);
//								 }
									 $scope.co++;
							 });
					
				}
			}
			
			
			$scope.creditSlitBillAmount = false;
		    $scope.getAuthorizationCreditUpdateTotal = function(creditVal){
		    	$scope.creditSlitBillAmount = true;
		    	if(parseFloat(creditVal) > parseFloat($scope.totalCreditPaybleSplitBillOld)){
		    		var diffSelf = parseFloat(creditVal) - parseFloat($scope.totalCreditPaybleSplitBillOld);
		    		
					if(parseFloat(diffSelf) > parseFloat($scope.totalSelfPaybleSplitBill)){
						growl.warning("You can't add more than net amount.("+parseFloat($scope.totalNetAmt)+").",{
							title : WARNING_MSG
						});
						$scope.calculateSplitBillTotal($scope.serviceList);
						//return false;
					}else{
						$scope.co = 1;
						
						angular.forEach($scope.serviceList,function(value,index)
								 {
							 if(parseFloat(value.netPay) > parseFloat(value.creditPayable)){
								 if(parseFloat(value.netPay) >= parseFloat(diffSelf) && value.orderDetailsPayeeMapperDtosList.length > 1 && parseFloat(diffSelf) > 0){
													 if( parseFloat(value.selfPayable) >= parseFloat(diffSelf) && parseFloat(diffSelf) > 0){
														 var tmpDiffSelf = parseFloat(diffSelf);
														 value.creditPayable =  parseFloat(value.creditPayable) + parseFloat(diffSelf);
														 value.selfPayable =  parseFloat(value.selfPayable) - parseFloat(diffSelf);
														 value.creditPayable =  parseFloat(value.creditPayable).toFixed(2);
														 value.selfPayable = parseFloat(value.selfPayable).toFixed(2);
														 value.selfPayableOldC = parseFloat(value.selfPayable).toFixed(2);
														 value.creditPayableOldC = parseFloat(value.creditPayable).toFixed(2);
														 angular.forEach(value.orderDetailsPayeeMapperDtosList,function(value1,index1)
																 {
															 		if(value1.payeeId != $scope.selfPayeeId){
															 			value1.payeeAmount = (parseFloat(value1.payeeAmount)  + parseFloat(diffSelf)).toFixed(2); 
															 			value1.creditPayable = (parseFloat(value1.creditPayable) + parseFloat(diffSelf)).toFixed(2);
															 			value1.spiltBillAmt = (parseFloat(value1.spiltBillAmt) + parseFloat(diffSelf)).toFixed(2);
															 			diffSelf = parseFloat(0);
															 		}else{
															 			value1.payeeAmount = (parseFloat(value1.payeeAmount)  - parseFloat(tmpDiffSelf)).toFixed(2);
															 			value1.selfPayable = (parseFloat(value1.selfPayable) - parseFloat(tmpDiffSelf)).toFixed(2);
															 			//tmpDiffSelf
															 		}
															 
																 });
													 }
													 
													 
													 if( parseFloat(value.selfPayable) < parseFloat(diffSelf) && parseFloat(diffSelf) > 0){
														 value.creditPayable =  parseFloat(value.creditPayable) + parseFloat(value.selfPayable);
														 diffSelf = parseFloat(diffSelf) - parseFloat(value.selfPayable);
														 value.creditPayable = parseFloat(value.creditPayable).toFixed(2)
														 value.creditPayableOldC = parseFloat(value.creditPayable).toFixed(2);
														 
														// value.creditPayable =  parseFloat(0);
														 
														 angular.forEach(value.orderDetailsPayeeMapperDtosList,function(value1,index1)
																 {
															 		if(value1.payeeId == $scope.selfPayeeId){
															 			value1.payeeAmount = parseFloat(0); 
															 			value1.selfPayable = parseFloat(0);
															 		}
																 });
														 
														 value.orderDetailsPayeeMapperDtosList[0].creditPayable = (parseFloat( value.orderDetailsPayeeMapperDtosList[0].creditPayable)+parseFloat(value.selfPayable)).toFixed(2);
														 value.orderDetailsPayeeMapperDtosList[0].payeeAmount = (parseFloat( value.orderDetailsPayeeMapperDtosList[0].payeeAmount)+parseFloat(value.selfPayable)).toFixed(2);
														 value.orderDetailsPayeeMapperDtosList[0].spiltBillAmt = (parseFloat( value.orderDetailsPayeeMapperDtosList[0].spiltBillAmt)+parseFloat(value.selfPayable)).toFixed(2);
														 value.selfPayable =  parseFloat(0);
														 value.selfPayableOldC = parseFloat(value.selfPayable).toFixed(2);
													
													 }
												 
											 }else{
												 if(parseFloat(value.netPay) <= parseFloat(diffSelf) && value.orderDetailsPayeeMapperDtosList.length > 1 && parseFloat(diffSelf) > 0 && parseFloat(value.selfPayable) > 0){
													 value.creditPayable = parseFloat(value.creditPayable) + parseFloat(value.selfPayable);
													 value.creditPayable = parseFloat(value.creditPayable).toFixed(2)
													 value.creditPayableOldC = parseFloat(value.creditPayable).toFixed(2);
													 diffSelf = parseFloat(diffSelf) - parseFloat(value.selfPayable);
													 angular.forEach(value.orderDetailsPayeeMapperDtosList,function(value1,index1)
															 {
														 		if(value1.payeeId != $scope.selfPayeeId && parseFloat(value.selfPayable) > 0){
														 			value1.payeeAmount = (parseFloat(value1.payeeAmount)  + parseFloat(value.selfPayable)).toFixed(2);
														 			value1.creditPayable = (parseFloat(value1.creditPayable) + parseFloat(value.selfPayable)).toFixed(2);
														 			value1.spiltBillAmt = (parseFloat(value1.spiltBillAmt) + parseFloat(value.selfPayable)).toFixed(2);
														 			value.selfPayable =  parseFloat(0);
														 			value.selfPayableOldC = parseFloat(value.selfPayable).toFixed(2);
														 		}else{
														 			value1.payeeAmount = parseFloat(0); 
														 			value1.selfPayable = parseFloat(0);
														 			//tmpDiffSelf
														 		}
														 
															 });
												 }
												 }
										 }
										 if($scope.serviceList.length == $scope.co){
											 $scope.calculateSplitBillTotal($scope.serviceList);
										 }
										 $scope.co++;
								 });
						
					}
		    	}
		    	
		    	if(parseFloat($scope.totalCreditPaybleSplitBillOld) > parseFloat(creditVal)){
                    $scope.newCreditVal = creditVal;
					var diffSelf =  parseFloat($scope.totalCreditPaybleSplitBillOld) - parseFloat(creditVal);
					$scope.co = 1;
					angular.forEach($scope.serviceList,function(value,index)
							 {
									if(value.orderDetailsPayeeMapperDtosList.length > 1){
										 if(parseFloat(diffSelf) >= parseFloat(value.creditPayable)  && parseFloat(diffSelf) > 0){
											 diffSelf = parseFloat(diffSelf) - parseFloat(value.creditPayable);
											 value.selfPayable = parseFloat(value.selfPayable) + parseFloat(value.creditPayable);
											 value.selfPayable = parseFloat(value.selfPayable).toFixed(2);
											 value.selfPayableOldC = parseFloat(value.selfPayable).toFixed(2);
											 var coChild = 1;
											 angular.forEach(value.orderDetailsPayeeMapperDtosList,function(value1,index1)
													 {
												 		if(value1.payeeId != $scope.selfPayeeId){
												 			value1.creditPayable =  parseFloat(0);;
												 			value1.payeeAmount =  parseFloat(0);;
												 			value1.spiltBillAmt =  parseFloat(0);;
												 		}else{
												 			value1.selfPayable =(parseFloat(value.selfPayable) + parseFloat(value.creditPayable)).toFixed(2);
												 			value1.payeeAmount = (parseFloat(value.payeeAmount) + parseFloat(value.creditPayable)).toFixed(2);
												 		}
												 		if(value.orderDetailsPayeeMapperDtosList.length == coChild)
												 			{
												 				value.creditPayable = parseFloat(0);
												 				 value.creditPayableOldC = parseFloat(0);
												 			}
												 		coChild++;
												 
													 });

										 }
                                         if(parseFloat(diffSelf) == 0 && parseFloat($scope.newCreditVal) != parseFloat($scope.totalCreditPaybleSplitBill)){
											 
											 value.selfPayable = (parseFloat(value.selfPayable) + parseFloat(value.creditPayable));
											 value.selfPayable = parseFloat(value.selfPayable).toFixed(2);
											 value.creditPayable = 0.00;
											 value.creditPayableOldC = parseFloat(value.creditPayable).toFixed(2);
											 value.creditPayable = parseFloat(diffSelf);
											 
											 
											 var coChild = 1;
											 var tmpdd = parseFloat(diffSelf);
											 angular.forEach(value.orderDetailsPayeeMapperDtosList,function(value1,index1)
													 {
												 		if(value1.payeeId != $scope.selfPayeeId){
												 			if(parseFloat(value1.creditPayable) >= parseFloat(diffSelf)){
												 				
												 				value1.creditPayable = 0;
													 			value1.payeeAmount =0;
													 			value1.spiltBillAmt = 0;
													 			diffSelf = parseFloat(0);
												 			}else{
												 				diffSelf = parseFloat(diffSelf) - parseFloat(value1.creditPayable);
												 				value1.creditPayable = parseFloat(0);
													 			value1.payeeAmount = parseFloat(0);
													 			value1.spiltBillAmt = parseFloat(0);
													 			
												 			}
												 		}else{
												 			value1.payeeAmount = (parseFloat(value1.payeeAmount) + parseFloat(tmpdd)).toFixed(2);
												 			value1.selfPayable = (parseFloat(value1.selfPayable)+ parseFloat(tmpdd)).toFixed(2);
												 			value1.creditPayable = parseFloat(0);
												 		}
												 		if(value.orderDetailsPayeeMapperDtosList.length == coChild)
												 			{
												 			   diffSelf = parseFloat(0);
												 			}
												 		coChild++;
												 
													 });
											 
										 }
										 
										 if(parseFloat(diffSelf) < parseFloat(value.creditPayable)){
											 
											// diffSelf = parseFloat(0);
											 value.selfPayable = parseFloat(value.selfPayable) + parseFloat(diffSelf);
											 value.selfPayable = parseFloat(value.selfPayable).toFixed(2);
											 value.creditPayable = parseFloat(value.creditPayable) - parseFloat(diffSelf);
											 value.creditPayable = parseFloat(value.creditPayable).toFixed(2);
											 
											 value.selfPayableOldC = parseFloat(value.selfPayable).toFixed(2);
											 value.creditPayableOldC = parseFloat(value.creditPayable).toFixed(2);
											 
											 
											 var coChild = 1;
											 var tmpdd = parseFloat(diffSelf);
											 angular.forEach(value.orderDetailsPayeeMapperDtosList,function(value1,index1)
													 {
												 		if(value1.payeeId != $scope.selfPayeeId){
												 			if(parseFloat(value1.creditPayable) >= parseFloat(diffSelf)){
												 				
												 				value1.creditPayable = (parseFloat(value1.creditPayable) - parseFloat(diffSelf)).toFixed(2);
													 			value1.payeeAmount = (parseFloat(value1.payeeAmount) - parseFloat(diffSelf)).toFixed(2);
													 			value1.spiltBillAmt = (parseFloat(value1.spiltBillAmt) - parseFloat(diffSelf)).toFixed(2);
													 			diffSelf = parseFloat(0);
												 			}else{
												 				diffSelf = parseFloat(diffSelf) - parseFloat(value1.creditPayable);
												 				value1.creditPayable = parseFloat(0);
													 			value1.payeeAmount = parseFloat(0);
													 			value1.spiltBillAmt = parseFloat(0);
													 			
												 			}
												 		}else{
												 			value1.payeeAmount = (parseFloat(value1.payeeAmount) + parseFloat(tmpdd)).toFixed(2);
												 			value1.selfPayable = (parseFloat(value1.selfPayable)+ parseFloat(tmpdd)).toFixed(2);
												 			value1.creditPayable = parseFloat(0);
												 		}
												 		if(value.orderDetailsPayeeMapperDtosList.length == coChild)
												 			{
												 			   diffSelf = parseFloat(0);
												 			}
												 		coChild++;
												 
													 });

										 }
									}
								
//								 if($scope.serviceList.length == co){
								
									 $scope.calculateSplitBillTotal($scope.serviceList);
//								 }
							    $scope.co++;
							 });
					
				}
		    	
			}
		    
		    
		    $scope.getCreditAmountForPayee = function(item,idx){
		    	
		    	if(item.creditPayable == '' || item.creditPayable == null || item.creditPayable == undefined){
					item.creditPayable = parseFloat(0);
				}
		    	//$scope.payeeeIdxService
		    	//serviceList
		    	
		    	
		    	//$scope.payeeCreditDetailslListSplit
		    	var totalCreditAmt = parseFloat(0);
		    	angular.forEach($scope.payeeCreditDetailslListSplit,function(value,index)
						 {
		    					totalCreditAmt = parseFloat(totalCreditAmt) + parseFloat(value.creditPayable);
						 });
		    	
		    	var totalNetTmp = parseFloat($scope.serviceList[$scope.payeeeIdxService].selfPayable) + parseFloat(totalCreditAmt);
		    	
				/*if(parseFloat(totalNetTmp) > parseFloat($scope.serviceList[$scope.payeeeIdxService].netPay)){
							
							growl.warning("You can't add more then net amount.",{
								title : ERROR_MSG
							});
							item.creditPayable = parseFloat(item.spiltBillAmt);
							return false;
						}else{
							item.spiltBillAmt = parseFloat(totalCreditAmt);
							var selfAmountTmp = parseFloat($scope.serviceList[$scope.payeeeIdxService].netPay) - parseFloat(totalCreditAmt);
							$scope.serviceList[$scope.payeeeIdxService].selfPayable = parseFloat(selfAmountTmp);
							$scope.serviceList[$scope.payeeeIdxService].creditPayable = parseFloat(totalCreditAmt);
							$scope.calculateSplitBillTotal($scope.serviceList);
						}*/
		    	
		    	
		    	if(parseFloat(item.creditPayable) > parseFloat(item.spiltBillAmt)){
		    		var tmpCreDiff = parseFloat(item.creditPayable) - parseFloat(item.spiltBillAmt);
		    		if(parseFloat($scope.serviceList[$scope.payeeeIdxService].selfPayable) >= parseFloat(tmpCreDiff)){
		    			$scope.serviceList[$scope.payeeeIdxService].selfPayable  = (parseFloat($scope.serviceList[$scope.payeeeIdxService].selfPayable) - parseFloat(tmpCreDiff)).toFixed(2);
		    			$scope.serviceList[$scope.payeeeIdxService].selfPayableOldC  =  $scope.serviceList[$scope.payeeeIdxService].selfPayable;
		    			item.spiltBillAmt = parseFloat(item.creditPayable);
		    			item.payeeAmount = parseFloat(item.creditPayable);
		    			 $scope.serviceList[$scope.payeeeIdxService].creditPayable = parseFloat($scope.serviceList[$scope.payeeeIdxService].creditPayable) + parseFloat(tmpCreDiff);
		    			 
		    				angular.forEach($scope.serviceList[$scope.payeeeIdxService].orderDetailsPayeeMapperDtosList,function(value,index)
									 {
										if(value.payeeId == $scope.selfPayeeId){
											//item.orderDetailsPayeeMapperDtosList.splice(index, 1);
											value.creditPayable = parseFloat(0);
											value.spiltBillAmt = parseFloat(0);
											value.selfPayable = (parseFloat(value.selfPayable) - parseFloat(tmpCreDiff)).toFixed(2);
											value.payeeAmount = parseFloat(value.selfPayable);
										}
									 });
		    				
		    				
		    			 $scope.calculateSplitBillTotal($scope.serviceList);
		    		}else{
		    			growl.warning("You can add only upto "+parseFloat($scope.serviceList[$scope.payeeeIdxService].selfPayable),{
							title : ERROR_MSG
						});
						item.creditPayable = parseFloat(item.spiltBillAmt);
						return false;
		    		}
		    	}else{
		    		var tmpCreDiff = parseFloat(item.spiltBillAmt) - parseFloat(item.creditPayable);
		    		$scope.serviceList[$scope.payeeeIdxService].selfPayable  = (parseFloat($scope.serviceList[$scope.payeeeIdxService].selfPayable) + parseFloat(tmpCreDiff)).toFixed(2);
		    		$scope.serviceList[$scope.payeeeIdxService].creditPayable = (parseFloat($scope.serviceList[$scope.payeeeIdxService].creditPayable) - parseFloat(tmpCreDiff)).toFixed(2);
		    		$scope.serviceList[$scope.payeeeIdxService].selfPayableOldC  =  $scope.serviceList[$scope.payeeeIdxService].selfPayable;
		    		item.spiltBillAmt = parseFloat(item.creditPayable);
					 item.payeeAmount = parseFloat(item.creditPayable);
					 angular.forEach($scope.serviceList[$scope.payeeeIdxService].orderDetailsPayeeMapperDtosList,function(value,index)
							 {
								if(value.payeeId == $scope.selfPayeeId){
									//item.orderDetailsPayeeMapperDtosList.splice(index, 1);
									value.creditPayable = parseFloat(0);
									value.spiltBillAmt = parseFloat(0);
									value.selfPayable = (parseFloat(value.selfPayable) +  parseFloat(tmpCreDiff)).toFixed(2);
									value.payeeAmount = parseFloat(value.selfPayable);
								}
							 });

					 
					 
					 $scope.calculateSplitBillTotal($scope.serviceList);
		    	}
		    	
		    }
		    

			//For Split New
		    
			//Change Payee
		    $scope.patientSponsorDetailsDtosListPopup = [];
			$scope.getPayeeListForUpdatePayee = function(){
				if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined)
				{
				growl.warning("Please select Patient..",{
					title : ERROR_MSG
					
				});
				return false;
				}else{
					$scope.patientSponsorDetailsDtosListPopup = [];
					$scope.paymentEntitlementTypeIdPopup ='1';
					 $scope.priorityList = [];
					 var i = 1;
					 
					angular.forEach($scope.patientSponsorDetailsDtosListSplit,function(value,index)
							 {
								var newArr = {}	;
								
								newArr.paymentEntitlementId = value.paymentEntitlementId;
								newArr.companyName = value.paymentEntitlementDesc;
								newArr.contractName =value.contractName;
								newArr.medicalCardNo =value.medicalCardNo;
								newArr.employeeName =value.employeeName;
							    newArr.startDate = value.validityFrom;
							    newArr.endDate = value.validityTo;
							    newArr.priority = value.priorityId;
							    newArr.payeeId =value.payeeId;
							    newArr.contractId =value.contractId;
							    newArr.applicableDate = value.applicableDate;
							    newArr.claimNumber =value.claimNumber;
							    newArr.patientSponsorDetailsId =value.patientSponsorDetailsId;
							    	if(value.paymentEntitlementId == 1){
							    		newArr.paymentEntitlementName = "Self";
							  			newArr.companyName = "Self";
							  			newArr.tariffName = "Self Tariff";
							  		    newArr.tariffId = $scope.defaultSelfTariffId;
							  		    newArr.companyId = $scope.selfPayeeIdCookies;
							    	}else if(value.paymentEntitlementId == 2){
							    		newArr.paymentEntitlementName = "Insurance";
							    		newArr.companyName = value.companyDesc;
							  		    newArr.tariffId = value.tariffId;
							  		    newArr.companyId = value.payeeId;
							  		    newArr.insuranceName =	value.insuranceDesc;
							  		    newArr.insuranceId =	value.insuranceId;
										newArr.insuranceNumber = value.insuranceNumber;
										newArr.insuredAmount = value.insuredAmount;
							  		  
							    	}else{
							    		newArr.paymentEntitlementName = "Corporate";
							    		newArr.companyName = value.companyDesc;
							  		    newArr.tariffId = value.tariffId;
							  		    newArr.companyId = value.payeeId;
							  		    newArr.insuranceName = "";
									    newArr.insuranceNumber = "";
									    newArr.insuredAmount = '';
							    	}
							    	var subString=newArr.applicableDate.substr(0,10);
							    	var dt = new Date(subString.split("-")[2]+"/"+subString.split("-")[1]+"/"+subString.split("-")[0]);
							    	newArr.applicableDateNew=dt*1;
							    $scope.patientSponsorDetailsDtosListPopup.push(newArr);	
							    var prioObj = {
										  id:i,
										  value:i,
									  }
									  $scope.priorityList.push(prioObj);
							    i++;
							 });
				
					angular.element('#paymentEntitlementModalForChange').modal('show');
				}
			}
			

			$scope.getDetailsEntitlement = function(type)
			{
				/*if(type == '2')
				{
					
				}*/
				if(type == '2')
				{
					$scope.paymentEntitleMentInsurance[0].InsApplicableDate=new Date($rootScope.getChangedFormatedDateShalseTwo($scope.admissionDetails.doa));
					
					var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyTypeId:type};
				    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getCompanyMasterList"
				    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success"){
							 $scope.companyMasterInsuranceList = response.data.listObject;
						}
					});
				}
				if(type == '3')
					{
						$scope.paymentEntitleMentCorporate[0].ComApplicableDate=new Date($rootScope.getChangedFormatedDateShalseTwo($scope.admissionDetails.doa)); 
						var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyTypeId:type};
						    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getCompanyMasterList"
						    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
								if (response.data.status == "success"){
									 $scope.companyMasterList = response.data.listObject;
									 //console.log("companyMasterList",JSON.stringify($scope.companyMasterList));
								}
							});
						  
					}
			}
			$scope.corporateCompanyId="";
			$scope.getContractByCompanyId = function(companyId)
			{
				$rootScope.startLoader();
				$scope.showNameDetails=false;	
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
				var companyTypeId = $scope.paymentEntitlementTypeIdPopup;
				
				var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyId:companyId,companyTypeId:companyTypeId,visitTypeId:$scope.visitTypeId/*,billingBedCategoryId:$scope.opdBillingCatId*/};
			    
				 $scope.contractList = [];
				 $scope.paymentEntitleMentCorporate[0].contractId = '';
				  $scope.paymentEntitleMentCorporate[0].datepickerCons ='';
				  $scope.paymentEntitleMentCorporate[0].datepickerConE ='';
				  
				var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getContractByCompanyId"
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						 $scope.contractList = response.data.listObject;
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
					}
					$rootScope.stopLoader();
				});
			}
			
			$scope.getInsuranceContract = function(insuranceItem)
			{
				
				$rootScope.startLoader();
				var companyTypeId = $scope.paymentEntitlementTypeIdPopup;
				var companyId = insuranceItem.companyId;
					var asscoiatedCompanyId = insuranceItem.associateCompanyId;
						
					
					var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyId:companyId,companyTypeId:companyTypeId,associateCompanyId:asscoiatedCompanyId,visitTypeId:$scope.visitTypeId/*,billingBedCategoryId:$scope.opdBillingCatId*/};
					$scope.assoContractList = [];
					$scope.paymentEntitleMentInsurance[0].contractId = '';
					
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
				//console.log("contractObj",contractObj);
				if(contractObj != undefined || contractObj != null)
					{
					  $scope.paymentEntitleMentCorporate[0].datepickerCons =contractObj.validityFrom;
					  $scope.paymentEntitleMentCorporate[0].datepickerConE = contractObj.validityTo;
					}
			}
			
			$scope.checkDuplicatePriority = function(item,idx,priorityVal)
			{
				
				var newDataList = [];
				var le = $scope.patientSponsorDetailsDtosListPopup.length;
				var i = 0;
				angular.forEach($scope.patientSponsorDetailsDtosListPopup, function(v) {
					if (idx != i) {
						newDataList.push(v);
					}
					i++;
				});
				
				
				var found = newDataList.find(ar=>ar.priority==priorityVal);
				  if(found == undefined)
					  {
					  	//return false;
					  }
				  else
					  {
					  $scope.patientSponsorDetailsDtosListPopup[idx].priority = 0;
					  growl.warning("You have aleady assigned "+priorityVal+" priority",{
							title : "WARNING"
						});
						return;
					  }
			}
			
			
			 var isContractAlreadyExists;
				function checkContractAlreadyExistsOrNot(arr, val) {
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
				
				 var isEmpty = !(Object.values($scope.paymentEntitleMentCorporate[0]).every(x =>(x != null &&  x != '')));
				 if(isEmpty)
					 {
					 if($scope.paymentEntitleMentCorporate[0].companyId == null || $scope.paymentEntitleMentCorporate[0].companyId == '' || $scope.paymentEntitleMentCorporate[0].companyId == undefined){
						 growl.warning("Please select company.",{
								title : "WARNING"
							});
							return; 
					 }
					 									 
					 if($scope.paymentEntitleMentCorporate[0].contractId == null || $scope.paymentEntitleMentCorporate[0].contractId == '' || $scope.paymentEntitleMentCorporate[0].contractId == undefined){
						 growl.warning("Please select contract.",{
								title : "WARNING"
							});
							return; 
					 }
					 }
				 else{
			
				isContractAlreadyExists=checkContractAlreadyExistsOrNot($scope.patientSponsorDetailsDtosListPopup,3);
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
				
				
				
				//console.log("contractObj",contractObj);
				if(contractObj != undefined || contractObj != null)
				{
				  if($scope.paymentEntitlementTypeIdPopup == '3')
				  {
					  
				/*	  var prioObj = {
							  id:$scope.priorityList.length + 1,
							  value:$scope.priorityList.length + 1,
						  }
						  $scope.priorityList.push(prioObj);*/
				  var newArr = {};
				  newArr.paymentEntitlementName=contractObj.paymentEntitlementName;
				  newArr.paymentEntitlementId="3";
				  if($scope.employeeNumberManual != null || $scope.employeeNumberManual != undefined || $scope.employeeNumberManual != '')
					  newArr.employeeNoManual = $scope.employeeNumberManual;
				  
				  if($scope.employeeName != null && $scope.employeeName != undefined && $scope.employeeName != ''){
					  newArr.employeeName = $scope.employeeName;
				  }
				  
				  if($scope.relation != null && $scope.relation != undefined && $scope.relation != ''){
					  newArr.relation = $scope.relation;
				  }
				  
				  if($scope.medicalCardNo != null && $scope.medicalCardNo != undefined && $scope.medicalCardNo != ''){
						newArr.medicalCardNo = $scope.medicalCardNo;
				  }
				  newArr.companyName = contractObj.assCompanyName;
				  newArr.companyId = contractObj.companyId;
				  newArr.payeeId = contractObj.companyId;
				  newArr.contractName = contractObj.contractName;
				  newArr.insuranceName = "";
				  newArr.insuranceNumber = "";
				  newArr.startDate = $rootScope.getChangedFormatedDateShalse($scope.paymentEntitleMentCorporate[0].datepickerCons)+" 00:00:00";
				  newArr.endDate =$rootScope.getChangedFormatedDateShalse($scope.paymentEntitleMentCorporate[0].datepickerConE)+" 00:00:00";
				  newArr.applicableDate =$scope.paymentEntitleMentCorporate[0].ComApplicableDate != null ? moment($scope.paymentEntitleMentCorporate[0].ComApplicableDate).format('DD-MM-YYYY HH:mm:ss'): null;//contractObj.applicableDate
				  newArr.applicableDateNew = ($scope.paymentEntitleMentCorporate[0].ComApplicableDate != null ? ($scope.paymentEntitleMentCorporate[0].ComApplicableDate*1): null);
				  newArr.priority = "";
				  newArr.tariffId = $scope.paymentEntitleMentCorporate[0].tariffId;
				  newArr.contractId = contractObj.contractId;
				  newArr.insuredAmount = '';
				  newArr.typeId = 3;
				  newArr.claimNumber = $scope.paymentEntitleMentCorporate[0].claimNumber;
				  if($scope.corEmpVerificationFlag && $scope.corEmpObj.typeOfCoEmp==3){
					  newArr.corporateEmpNote=$scope.corEmpObj.note!=undefined || $scope.corEmpObj.note!="" || $scope.corEmpObj.note!=null ? $scope.corEmpObj.note : "";
				  }
				  
				  if($scope.CompanyObjDoc.companyId!=undefined && $scope.CompanyObjDoc.companyId!=null && $scope.CompanyObjDoc.companyId!="" && $scope.paymentEntitleMentCorporate[0].companyId==$scope.CompanyObjDoc.companyId){
					  newArr.reportPath=$scope.CompanyObjDoc.reportPath;
				  }

				  isInsuranceExists=checkInsuranceExistsOrNot($scope.patientSponsorDetailsDtosListPopup,newArr)
					if (!isInsuranceExists) {
						$scope.enableDisabledApply=false;	
						  var prioObj = {
								  id:$scope.priorityList.length + 1,
								  value:$scope.priorityList.length + 1,
							  }
							  $scope.priorityList.push(prioObj);
						var lenCon = $scope.patientSponsorDetailsDtosListPopup.length;
						if($scope.patientSponsorDetailsDtosListPopup.length > 0)
						{
							angular.forEach($scope.patientSponsorDetailsDtosListPopup, function (value, key) {
								 
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
								  angular.forEach($scope.patientSponsorDetailsDtosListPopup, function (value1, key1) {
									  if(value1.paymentEntitlementId != 1)
									  {
									  value1.priority =  "";
									  }
								  });
								  	
								  }
								  
								  
							  });
						}
						
						$scope.patientSponsorDetailsDtosListPopup.push(newArr);
						
						if($scope.patientSponsorDetailsDtosListPopup.length > 1)
						{
							$scope.billTabHide = true;
						}
						$scope.paymentEntitleMentCorporate=
							[{
								'companyId':"",
								'contractId':"",
							}];
						$scope.employeeNumberManual= '';
						$scope.employeeName= '';
						$scope.relation= '';
						$scope.medicalCardNo= '';
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
						angular.forEach($scope.patientSponsorDetailsDtosListPopup, function (value, key) {
							 
							  if(value.paymentEntitlementId == "2")
								  {
								   countInsurance++;
								  }
							  
							  
						  });
						return countInsurance;
					}
					
			
			$scope.applyInsuranceDetails = function()
			{
				var objTmp = angular.copy($scope.paymentEntitleMentInsurance[0]);
				 objTmp.multidelete("insuranceNumber","datepickerInss","datepickerInsE","insuredAmt");
					 var isEmpty = !(Object.values(objTmp).every(x =>(x != null &&  x != '')));
					 if(isEmpty)
						 {
						 if($scope.paymentEntitleMentInsurance[0].companyId == null || $scope.paymentEntitleMentInsurance[0].companyId == '' || $scope.paymentEntitleMentInsurance[0].companyId == undefined){
							 growl.warning("Please select company.",{
									title : "WARNING"
								});
								return; 
						 }
						 
						 if($scope.paymentEntitleMentInsurance[0].associateCompanyId == null || $scope.paymentEntitleMentInsurance[0].associateCompanyId == '' || $scope.paymentEntitleMentInsurance[0].associateCompanyId == undefined){
							 growl.warning("Please select associate company.",{
									title : "WARNING"
								});
								return; 
						 }
						 
						 if($scope.paymentEntitleMentInsurance[0].contractId == null || $scope.paymentEntitleMentInsurance[0].contractId == '' || $scope.paymentEntitleMentInsurance[0].contractId == undefined){
							 growl.warning("Please select contract.",{
									title : "WARNING"
								});
								return; 
						 }
						 
						 }
					 else{
						 
				insuranceCount=checkInsuranceCount($scope.patientSponsorDetailsDtosListPopup)
				if (insuranceCount >= 2) {
					growl.warning("You can't add more than two insurance.",{
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
				var associatedName = $.grep($scope.associateCompanyMasterList, function (doc) {
		            return doc.id == $scope.paymentEntitleMentInsurance[0].associateCompanyId;
		        })[0].name;
				
				var insuranceId = $.grep($scope.associateCompanyMasterList, function (doc) 
						{
			            return doc.id == $scope.paymentEntitleMentInsurance[0].associateCompanyId;
			        })[0].id;
				
				//console.log("$scope.paymentEntitleMentInsurance",$scope.paymentEntitleMentInsurance);
				
				if(insuranceObj != undefined || insuranceObj != null)
				{
				  if($scope.paymentEntitlementTypeIdPopup == '2')
				  {
					  var isAfter = true;
					    
					  if(!isAfter)
						  {
							  growl.warning("Start date should be less than end date..!!",{
									title : "WARNING"
								});
								return false;
						  }else{
							  
							 /* var prioObj = {
									  id:$scope.priorityList.length + 1,
									  value:$scope.priorityList.length + 1,
								  }
								  $scope.priorityList.push(prioObj);
							  */
				  var newArr = {};
				  newArr.paymentEntitlementName=insuranceObj.paymentEntitlementName;
				  newArr.paymentEntitlementId="2";
				  newArr.companyName = insuranceObj.assCompanyName;
				  newArr.companyId = insuranceObj.companyId;
				  newArr.payeeId = insuranceObj.companyId;
				//  newArr.tariffName = tarrifName;
				  newArr.employeeNoManual = '';
				  newArr.contractName = insuranceObj.contractName;
				  newArr.insuranceName = associatedName;
				  newArr.insuranceId = insuranceId;
				  newArr.insuranceNumber = $scope.paymentEntitleMentInsurance[0].insuranceNumber;
				  newArr.startDate = $scope.paymentEntitleMentInsurance[0].datepickerInss != null ?moment($scope.paymentEntitleMentInsurance[0].datepickerInss).format('DD-MM-YYYY HH:mm:ss'): null;//contractObj.validityFrom;
				  newArr.endDate = $scope.paymentEntitleMentInsurance[0].datepickerInsE != null ? moment($scope.paymentEntitleMentInsurance[0].datepickerInsE).format('DD-MM-YYYY HH:mm:ss'): null;//contractObj.validityFrom;datepickerInsE
				  newArr.applicableDate = $scope.paymentEntitleMentInsurance[0].InsApplicableDate != null ? moment($scope.paymentEntitleMentInsurance[0].InsApplicableDate).format('DD-MM-YYYY HH:mm:ss'): null;//contractObj.applicableDate
				  newArr.applicableDateNew = ($scope.paymentEntitleMentInsurance[0].InsApplicableDate != null ? ($scope.paymentEntitleMentInsurance[0].InsApplicableDate*1): null);
				  newArr.priority = "";
				  newArr.tariffId = $scope.paymentEntitleMentInsurance[0].tariffId;
				  newArr.contractId = insuranceObj.contractId;
				  newArr.typeId = 2;
				  newArr.insuredAmount = $scope.paymentEntitleMentInsurance[0].insuredAmt;
				  newArr.claimNumber = $scope.paymentEntitleMentInsurance[0].claimNumber;
				  if($scope.insuranceObjDoc.insuranceId!=undefined && $scope.insuranceObjDoc.insuranceId!=null  && $scope.paymentEntitleMentInsurance[0].associateCompanyId==$scope.insuranceObjDoc.insuranceId){
					  newArr.reportPath=$scope.insuranceObjDoc.reportPath;
				  }
				  
				  isInsuranceExists=checkInsuranceExistsOrNot($scope.patientSponsorDetailsDtosListPopup,newArr)
					if (!isInsuranceExists) {
						 var prioObj = {
								  id:$scope.priorityList.length + 1,
								  value:$scope.priorityList.length + 1,
							  }
							  $scope.priorityList.push(prioObj);
						  
						var lenIns = $scope.patientSponsorDetailsDtosListPopup.length;
						if($scope.patientSponsorDetailsDtosListPopup.length > 0)
							{
								angular.forEach($scope.patientSponsorDetailsDtosListPopup, function (value, key) {
									 
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
										  angular.forEach($scope.patientSponsorDetailsDtosListPopup, function (value1, key1) {
											  if(value1.paymentEntitlementId != 1)
											  {
											  value1.priority =  "";
											  }
										  });
										  	
										  }
									  
									  
								  });
							}
						
						$scope.patientSponsorDetailsDtosListPopup.push(newArr);
						
						if($scope.patientSponsorDetailsDtosListPopup.length > 1)
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
								'insuredAmt':"",
								'InsApplicableDate':new Date($rootScope.getChangedFormatedDateShalseTwo($scope.admissionDetails.doa))
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
				
				$scope.paymentEntitleMentInsurance[0].InsApplicableDate=new Date($rootScope.getChangedFormatedDateShalseTwo($scope.admissionDetails.doa));
				$scope.paymentEntitleMentCorporate[0].ComApplicableDate=new Date($rootScope.getChangedFormatedDateShalseTwo($scope.admissionDetails.doa)); 
				
				//console.log("removeIndex",index);
				var newDataList = [];
				var tmpprio;
				
				var le = $scope.patientSponsorDetailsDtosListPopup.length;
				
				var tmpObj = angular.copy($scope.patientSponsorDetailsDtosListPopup);
				var i = 1;
				if(le != i){
					
				angular.forEach(tmpObj, function(value,key) {
					
					if (index != i) {
						newDataList.push(value);
						
						if(le == 2)
							{
								if(value.paymentEntitlementId == "1")
									{
									value.priority = le - 1;
									}else
										{
										value.priority = 1;
										}
							}
						if(le > 2)
						{
							if(tmpprio != 3){
							if(value.priority != 1)
							{
								value.priority =value.priority - 1;
							}
							}
						}
					}else{
						tmpprio = value.priority;
						$scope.patientSponsorDetailsDtosListPopup.splice(index - 1,1);
					}
					
					if(i == le)
						{
							if(newDataList.length == 1){
							}
						}
					
					if($scope.corEmpVerificationFlag){
						$scope.enableDisabledValidated=false;
						$scope.enableDisabledApply=true;	
					}else{
						$scope.enableDisabledValidated=false;
						$scope.enableDisabledApply=true;
					}
					i++;
				});
				
				var len = $scope.priorityList.length;
				 $scope.priorityList.splice(len-1,1);
				 
				$scope.patientSponsorDetailsDtosListPopup = newDataList;
				if($scope.patientSponsorDetailsDtosListPopup.length == 1)
					{
					$scope.patientSponsorDetailsDtosListPopup[0].priority = "1";
					}
				if($scope.patientSponsorDetailsDtosListPopup.length == 1)
					{
					  $scope.billTabHide = false;
					}
				else
					{
					  $scope.billTabHide = true;
					}
				
				
				}
				
				$scope.enableDisabledApply=true;	
				
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
		/*	$scope.getCompanyMasterList = function(){
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
			*/
			$scope.getAssociatedCompanyMasterListByCompanyId = function(companyId){
				$rootScope.startLoader();
				 var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyId:companyId};
				 $scope.associateCompanyMasterList = [];
				 $scope.paymentEntitleMentInsurance[0].associateCompanyId = '';
				 
				    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getAssociateCompanyMaster"
				    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success"){
							 $scope.associateCompanyMasterList = response.data.listObject;
							  //console.log("associateCompanyMasterList",JSON.stringify($scope.associateCompanyMasterList));
						}
						$rootScope.stopLoader();
					});
				    
			}
			
			$scope.checkDatePack = function(item,idx){
				console.log(item);
				
				var pcDt = moment(
						item.packageApplicableDateDt)
						.format('DD-MM-YYYY HH:mm:ss');
				var nDT = moment(new Date(item.packageApplicableDateDtold))
						.format('DD-MM-YYYY HH:mm:ss');
				//console.log("dateOfPdd",dateOfPdd);
				//console.log("dateOfAdmission",dateOfAdmission);
				//&& dateOfPdd >= dateOfAdmission
				//if(dateOfAdmission > dateOfPdd )
				if(pcDt != nDT)
					{
					  $rootScope.stopLoader();
					var data = {
							packageConsumptionMasterId :item.packageConsumptionMasterId,
							packageApplicableDate : moment(item.packageApplicableDateDt).format("YYYY-MM-DD"),
							packageId : item.packageId 
					}
					
				  	var URI = BASE_URL + ROOT_URL + "/api/billing/updatePackageEffectiveDate";
					
					GenericService
					.serviceAction("POST", URI, data)
					.then(function(response) {
								$rootScope.stopLoaderPopup();
								if (response.data.status == "success") {
									$scope.listPackageOrder[idx].packageApplicableDateDtold = moment(item.packageApplicableDateDt)
									.format('MM/DD/YYYY');
									 growl.success("Package applicable date changed successfully.",
												{
													title : 'Success!'
												});
									   $rootScope.stopLoader();
								}
							});
					}
				
				
			}
			 $scope.uploadEncounterContractDoc = function(Obj)
				{
					 var fd = new FormData();
				        fd.append('encounterId', Obj.admissionId);
				        fd.append('visitTypeId', Obj.visitTypeId);
				        fd.append('admissionId', Obj.admissionId);
				        fd.append('patientId',Obj.patientId );
				        fd.append('documentTypeId', Obj.documentTypeId);
				        fd.append('createdBy',Obj.createdBy );
				        fd.append('updatedBy', Obj.updatedBy);
				        fd.append('unitId', Obj.unitId);
				        fd.append('organisationId', Obj.organisationId);
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
//					  if (value.file == undefined || value.file == "" || value.file == null ) {
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
				 if($scope.patientDetails.patientId > 0){
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
										   $scope.encounterDetailsContractList = [];
										   //$scope.getPatientDocumentList();
										   
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
			    }else{
			    	 $('#documentUploadModalForEncounter').modal('hide');
			    	 $rootScope.stopLoader();
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
		      
		      if($scope.patientDetails.patientId > 0){

		    	  $rootScope.startLoader();
		       var data =
		        {
		        "patientId":$scope.patientDetails.patientId,
		        "unitId":$scope.unitId,
		        "organisationId":$scope.organizationId,
		        "userId":$scope.userId,
		        "visitTypeId":$scope.visitTypeId,
		        "admissionId":$scope.admissionId,
		       }
		      var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
		      BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
		        $scope.patientDocumentList = [];
		        if (response.data.status == "success") 
		        {
		        	$scope.patientDocumentList = response.data.listObject;  
		            $scope.patientAllDocumentList = response.data.listObject;
		            $scope.patientDocumentList=[];
		            angular.forEach($scope.patientAllDocumentList, function(value, key) 
							{  
		            if(obj.paymentEntitlementId==1){
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
		            }else if(obj.paymentEntitlementId==2){
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
		            }else if(obj.paymentEntitlementId==3){
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
				
						 
						 if(obj.paymentEntitlementId==1){			
								
							 $scope.encounterDetailsContractList =[];
								var encounterDetails = angular.copy($scope.encounterDetailsContract);
								encounterDetails.patientId = $scope.patientDetails.patientId;
								encounterDetails.paymentEntitlementId = obj.paymentEntitlementId;
								$scope.encounterDetailsContractList.push(encounterDetails);	
								$scope.getPatientDocumentList(encounterDetails);
								$('#documentUploadModalForEncounter').modal('show');
							}else if(obj.paymentEntitlementId==2){
								
								$scope.encounterDetailsContractList =[];
								var encounterDetails = angular.copy($scope.encounterDetailsContract);
								encounterDetails.documentTypeId = '30';	
								encounterDetails.patientId = $scope.patientDetails.patientId;
								encounterDetails.paymentEntitlementId = obj.paymentEntitlementId;
								$scope.encounterDetailsContractList.push(encounterDetails);
								 $scope.getPatientDocumentList(encounterDetails);
								 $('#documentUploadModalForEncounter').modal('show');
								
							}else if(obj.paymentEntitlementId==3){
								$scope.encounterDetailsContractList =[];
								var encounterDetails = angular.copy($scope.encounterDetailsContract);
								encounterDetails.documentTypeId = '19';	
								encounterDetails.patientId = $scope.patientDetails.patientId;	
								encounterDetails.paymentEntitlementId = obj.paymentEntitlementId;
								$scope.encounterDetailsContractList.push(encounterDetails);
								 $scope.getPatientDocumentList(encounterDetails);		
								$('#documentUploadModalForEncounter').modal('show');
							}else{
								
							}
					
				}
			 
			 var isPriorityZeroExists;
				function checkPriorityZeroOrNot(arr) {
					return arr
							.some(function(arr) {
								if(parseInt(arr.priority) == 0 || arr.priority == "" || arr.priority == -1)
									{
										return true;
									}
								else
									{
										return false;
									}
							});
				}
				
			  
			 $scope.savePatintAndSponsor = function(){
				 
				 isPriorityZeroExists=checkPriorityZeroOrNot($scope.patientSponsorDetailsDtosListPopup);
				  if (isPriorityZeroExists) {
					
					  growl.warning("Please assign priority for payment entitlements.",{	
						title : "WARNING"
					});
					return false;
					
				  }
				  else{
					 // $rootScope.startLoader();
					  
					 
					  var data = {
							  patientCategoryId:$scope.admissionDetails.patientCategoryId,
							  visitTypeId:$scope.visitTypeId,
							  selfPayeeId:$scope.selfPayeeId,
							  defaultSelfTariffId:$scope.defaultSelfTariffId,
							  admissionId:$scope.admissionDetails.admissionId,
							  encounterId :null,
							  billingBedCategoryId:$scope.admissionDetails.bedBillingCategoryId,
							  isPreBilling:$scope.isPreOrPostBilling,
							  patientId:$scope.patientDetails.patientId,
							  orgId: $scope.organizationId,
							  unitId:  $scope.unitId,
							  referralId: $scope.admissionDetails.referralId,
							  patientClassId:  $scope.admissionDetails.patientClassId,
							  pharmacyServiceId : $scope.pharmacyServiceId
					  };
						//console.log('$scope.paymentEntitleMentList',$scope.patientSponsorDetailsDtosListPopup);
						var sponserList = [];
						angular.forEach($scope.patientSponsorDetailsDtosListPopup, function (value, key) {
							var newArr = {};
							newArr.organizationId = $scope.organizationId ;
							newArr.unitId = $scope.unitId;
							newArr.visitTypeId = $scope.visitTypeId;
							newArr.paymentEntitlementId =value.paymentEntitlementId;
							newArr.payeeId = value.payeeId;
							newArr.priorityId = value.priority;
							newArr.createdBy =$scope.userId;
							newArr.updatedBy = $scope.userId;
							//createdDate : moment(new Date()).format("DD-MM-YYYY"),
							//updatedDate :  moment(new Date()).format("DD-MM-YYYY"),
							newArr.status = 'A';
							newArr.tariffId =value.tariffId;
							newArr.contractId =value.contractId;
							newArr.insuranceNumber = value.insuranceNumber;
							newArr.insuredAmount = value.insuredAmount;
							newArr.insuranceId = value.insuranceId;
							if(value.paymentEntitlementId != 1){
								newArr.validityTo = (value.endDate != null || value.endDate != undefined) ? value.endDate+" 00:00:00" : null;
			                    newArr.validityFrom = (value.startDate != null || value.startDate != undefined) ? value.startDate+" 00:00:00" : null; 
			                    newArr.applicableDate = (value.applicableDate != null || value.applicableDate != undefined) ? value.applicableDate : null;
							}else{
								newArr.validityTo = null;
								newArr.validityFrom = null;
							}
							
							if(value.priority == 1)
								{
									data.contractId = value.contractId;
									data.paymentEntitlementId = value.paymentEntitlementId;
								}
							newArr.employeeNoManual = value.employeeNoManual;
							newArr.employeeName = value.employeeName;
							newArr.relation = value.relation;
							newArr.medicalCardNo = value.medicalCardNo;
							newArr.claimNumber =value.claimNumber;
							sponserList.push(newArr);
						});
						data.patientSponsorDetailsDtoList = sponserList;
						
						//console.log("data",data);
						//return false;
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL
								+ "/api/billing/updatePayee";
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											angular.element('#paymentEntitlementModalForChange').modal('hide');
											if (response.data.status == "success") {
												
												if($scope.preBillId!=undefined && $scope.preBillId>0)
												{
													
													var URI = BASE_URL + ROOT_URL + "/api/ipd/admissionDetails/cancelledBills/"+$scope.admissionId;
													BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
														if(response.data.status == "success"){
															$scope.admissionDetails = response.data.object;
															 $scope.admissionDetails.remark = (($scope.admissionDetails.remark == null || $scope.admissionDetails.remark == "null") ? "":$scope.admissionDetails.remark);
															if($scope.admissionDetails.payeeId != $scope.selfPayeeId)
															{
																$scope.contractId = $scope.admissionDetails.contractId;
															}
														else
															{
																$scope.contractId = 0;
															}
															
															var URI2 = BASE_URL + ROOT_URL + "/api/opd/sponsoredDetails/admission/"+$scope.admissionId+"/encounter/0";
															BillingGenericService.serviceAction(METHOD_GET,URI2, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
																if(response.data.status == "success"){
																	 $scope.patientSponsorDetailsDtosListSplit = [];
																	 $scope.patientSponsorDetailsDtosListSplit = response.data.listObject;

																	 	$rootScope.stopLoader();
																	 	$scope.needToUpdatePharmacyService = true;
																	 	$scope.updatePayee = true;
																		$scope.saveOrderPackage();
																}
															});
															
														}
													});
												}else
												{
												var URI1 = BASE_URL + ROOT_URL + "/api/ipd/admissions/patient/"+$scope.admissionId;
												BillingGenericService.serviceAction(METHOD_GET,URI1, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
													if(response.data.status == "success"){
														$scope.admissionDetails = response.data.object;
														$scope.dischargeStatusId=$scope.admissionDetails.dischargeStatusId;
														$scope.specialityName = $scope.admissionDetails.department;
														 $scope.admissionDetails.remark = (($scope.admissionDetails.remark == null || $scope.admissionDetails.remark == "null") ? "":$scope.admissionDetails.remark);
														if($scope.admissionDetails.payeeId != $scope.selfPayeeId)
														{
															$scope.contractId = $scope.admissionDetails.contractId;
														}
													else
														{
															$scope.contractId = 0;
														}
														
														var URI2 = BASE_URL + ROOT_URL + "/api/opd/sponsoredDetails/admission/"+$scope.admissionId+"/encounter/0";
														BillingGenericService.serviceAction(METHOD_GET,URI2, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
															if(response.data.status == "success"){
																 $scope.patientSponsorDetailsDtosListSplit = [];
																 $scope.patientSponsorDetailsDtosListSplit = response.data.listObject;

																 	$rootScope.stopLoader();
																 	$scope.needToUpdatePharmacyService = true;
																 	$scope.updatePayee = true;
																	$scope.saveOrderPackage();
																	$scope.getAuthrizationDetails();
																	angular.element('#paymentEntitlementModalForChange').modal('hide');
																
															}
														});
														
													}
														
												});
											}
												//
											
												$scope.getNewDoctorList();	
											} else
												{
												  $rootScope.stopLoader();
												}

										});
				  }
			 }
			 
			//CHange Payee
			 $scope.cancelPopup = false;
			 $scope.cancelSevices = function()
			    {
				 $rootScope.startLoaderPopup();
				 $scope.cancelService = true;
				 $scope.cancelPopup = true;
				 angular.element('#serviceCancellationModal').modal('hide');
				 if($scope.servicesCancelReasonId > 0){
					 
				    	var idx  = $scope.objectIndexService;
				    	var item = $scope.serciveObjectForRemove;
				    	var itemForNagetive = angular.copy($scope.serciveObjectForRemove);
				    	
				   	if(item.isPackage) 
					   	{
					   		growl.warning(" Services with updated rates will be removed from the package. Please update the rate accordingly. ",{
								title : ERROR_MSG
							});
				   		
					   	}
				    	
				    	if(item.orderDetailsId > 0)
				    		{
				    			
				    		itemForNagetive.basePrice = parseFloat(-($scope.serciveObjectForRemove.basePrice));
				        	itemForNagetive.concession = parseFloat(-($scope.serciveObjectForRemove.concession));
				        	itemForNagetive.orderDetailsId = 0;
				        	itemForNagetive.orderId = 0;
				        	itemForNagetive.discountDetailsList = [];
				        	itemForNagetive.discountPercentage = parseFloat(0);
				        	itemForNagetive.discount = parseFloat(0);
				        	itemForNagetive.ordCancelled  = 'Y';
				        	itemForNagetive.oldOrdDtlId = item.orderDetailsId;
				        	itemForNagetive.status = "I";
				        	itemForNagetive.isPackage = item.isPackage;
				        	itemForNagetive.ordCancelReasonId  = $scope.servicesCancelReasonId;
				        	itemForNagetive.ordCancelDatetime  = new Date().getTime();
				        	itemForNagetive.ordCancelRemark  = $scope.servicesCancelRemark ;
				        	itemForNagetive.isPackageService = item.isPackageService;
				        	itemForNagetive.isCapService = item.isCapService;
				        	itemForNagetive.creditPayable = item.creditPayable*-1;
				        	itemForNagetive.selfPayable = item.selfPayable*-1;
				        	let reason = $scope.reasonList.find((elem) => {if(elem.reasonMasterId == $scope.servicesCancelReasonId) return true});
				        	itemForNagetive.cancelledReason = reason.reasonDesc;
				        	 //console.log("AFTERRRR",itemForNagetive);
				    		
				    			if(item.discountDetailsList.length > 0){
				    				angular.forEach(item.discountDetailsList, function (value, key) {
				    					if(value.orderDetailsDiscountId > 0)
				    		    		{
				    		    			var disArr = {};
				    		    			disArr.orderDetailsDiscountId  = value.orderDetailsDiscountId;
				    		    			disArr.orderDetailsId = value.orderDetailsId;
				    		    			disArr.discountCategoryId = value.discountCategoryId;
				    		    			disArr.discountPercentage = value.discountPercentage;
				    		    			disArr.discountAmount = value.discountAmount;
				    		    			disArr.isCancel = 'Y';
				    		    			disArr.cancelBy  = $scope.userId;
				    		    			disArr.cancelReasonId  = "";
				    		    			disArr.remark ="";
				    		    			disArr.createdBy  = value.createdBy;
				    		    			disArr.createdDate  = value.createdDate;
				    		    			disArr.updatedBy  = $scope.userId;
				    		    			disArr.updatedDate  = new Date().getTime();
				    		    			disArr.cancelledDate = new Date().getTime();
				    		    			disArr.orgId  = $scope.organizationId;
				    		    			disArr.unitId  = $scope.unitId;
				    		    			disArr.discountDate = new Date().getTime();
				    		    			disArr.status = "I";
				    		    			disArr.isConsessionDiscount = "N";
				    		    			$scope.removeDiscountList.push(disArr);
				    		    		}
				    	    			
				    	    			
				    	    		});
				    			}
				    	
				    			//$scope.autoRenderedServicesFinalList.push(itemForNagetive);
				    			$scope.autoRenderedServicesFinalList[idx].discountPercentage = parseFloat(0);
				    			$scope.autoRenderedServicesFinalList[idx].discount = parseFloat(0);
				    			$scope.autoRenderedServicesFinalList[idx].ordCancelled = 'Y';
				    			$scope.autoRenderedServicesFinalList[idx].discountDetailsList = [];
				    			$scope.autoRenderedServicesFinalList[idx].ordCancelReasonId  = $scope.servicesCancelReasonId;
				    			$scope.autoRenderedServicesFinalList[idx].ordCancelDatetime  = new Date().getTime();
				    			$scope.autoRenderedServicesFinalList[idx].ordCancelRemark  = $scope.servicesCancelRemark ;
				    			$scope.autoRenderedServicesFinalList[idx].status  = 'I' ;
				        		var autoRenderedServicesList = $scope.autoRenderedServicesFinalList;
				            	
				        		//console.log("AFTER",autoRenderedServicesList);
				            	//$scope.calculationTest(autoRenderedServicesList,init);
				            	$scope.removeServicesList = [];
				            	
				    		}else
				    	      {
				    		      var serviceIndex = $scope.autoRenderedServicesFinalList.indexOf(item);
				    		      $scope.autoRenderedServicesFinalList.splice(serviceIndex, 1);
				    		      var autoRenderedServicesList = $scope.autoRenderedServicesFinalList;
				    		      $scope.calculationTest(autoRenderedServicesList,init);
				    		         
						      }
				    	
				    	if($scope.requestStatusObject != null){
							$scope.getCancelDiscountReqForDiscount();
							$scope.billWiseDiscountObj = {
									// "discountGivenBy":"",
									"discountGivenBy":$scope.userId,
									"discountCategoryId" : "",
									"discountAmountBillWise" : "",
									"discountPercentageBillWise" : "",
									"approvedAmountBillWise":"",
									"requestUserId":"",
									"requestAmountBillWise" : 0,
									"remark":'',
							}
						 $scope.requestDiscountDiv = false;
						 $scope.lineItemDiscountAmt = parseFloat(0);
						 $scope.billWiseDiscountObj.discountAmountBillWise =  parseFloat(0);
						 $scope.billWiseDiscountObj.requestAmountBillWise =   parseFloat(0);
							}
						 
				    	
				    	
				     /*if(parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise)>0 && $scope.isBillWiseDiscountApplied)
				   	 {
				   		 $scope.funDiscountPercentageBillWise();
				   	     $scope.applyDiscount();
				   	 }*/
			      	if(item.isPackage) 
				   	{
			      		angular.element('#serviceCancellationModal').modal('hide');
			      		$scope.objectIndexService = '';
				    	$scope.serciveObjectForRemove = {};
			      		if($scope.requestStatusObject != null)
			      		{
							$scope.getCancelDiscountReqForDiscount();
			      		}	
						$scope.billWiseDiscountObj = {
								// "discountGivenBy":"",
								"discountGivenBy":$scope.userId,
								"discountCategoryId" : "",
								"discountAmountBillWise" : "",
								"discountPercentageBillWise" : "",
								"approvedAmountBillWise":"",
								"requestUserId":"",
								"requestAmountBillWise" : 0,
								"remark":'',
						 }
						 $scope.requestDiscountDiv = false;
						 $scope.lineItemDiscountAmt = parseFloat(0);
						 $scope.billWiseDiscountObj.discountAmountBillWise =  parseFloat(0);
						 $scope.billWiseDiscountObj.requestAmountBillWise =   parseFloat(0);
			   		   $scope.saveOrderPackage();
					}else{
						var serviceIndex = $scope.autoRenderedServicesFinalList.indexOf(item);
					    $scope.autoRenderedServicesFinalList.splice(serviceIndex, 1);
						if(parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise)>0 && $scope.isBillWiseDiscountApplied)
					   	 {
					   		 $scope.funDiscountPercentageBillWise();
					   	     $scope.applyDiscount();
					   	 }
						$scope.addCapAmout(item,$scope.autoRenderedServicesFinalList);
			 			angular.element('#serviceCancellationModal').modal('hide');
					}
			      	angular.element('#serviceCancellationModal').modal('hide');
		            $scope.isFinancialClearence ='N';
		            $scope.finanacial.financialRequestSendTo = "";

			    }
				 else
			    	{
				    	growl.warning("Please select reason..!!",{
							title : ERROR_MSG
						});
				    	$rootScope.stopLoaderPopup();
						return false;
			    	}
		            $scope.isFinancialClearence ='N';
		            $scope.finanacial.financialRequestSendTo = "";

			    }
			
			//getPackageDetails by serviceConsumptionDetailsId Start
				$scope.getServiceConsumptionDetailsId=function(id){
					$scope.serviceConsumptionDetailsList=[];
					var URI = BASE_URL + ROOT_URL+ "/api/packages/getPackageLog/"+id;
					BillingGenericService.serviceAction(METHOD_POST, URI,"",NOTIFICATION_MSG_STATUS_FALSE)
					.then(function(response) {
								if (response.data.status == "success")
									$scope.serviceConsumptionDetailsList = response.data.object;
								    //console.log("$scope.serviceConsumptionDetailsList",$scope.serviceConsumptionDetailsList);
							});
				}
				//getPackageDetails by serviceConsumptionDetailsId End
				
				$scope.stopLoader = function()
				{
				//	alert();
					if($scope.isIntermediateBill == 'Y')
						{
						  $scope.autoRenderedServicesFinalList = angular.copy($scope.intermediateServiceTemp);
						  $scope.calculationTest($scope.autoRenderedServicesFinalList,discountPer);
						}
				}
				
				
				// for skip invoice creation
				  $scope.saveCompanyInvoiceCreation = function(){
					  
								var totalAmount = 0;
							    var dueAmount = 0;
							    var crNetAmount = 0;
								angular.forEach($scope.companyInvoiceList,function(value,index){
									if (value.companyInvoiceSelected == true) {
											totalAmount = parseFloat(totalAmount) + (isNaN(value.billAmount) ? 0 : parseFloat(value.billAmount));
											dueAmount = parseFloat(dueAmount) + (isNaN(value.billAmount) ? 0 : parseFloat(value.billAmount));
											crNetAmount = parseFloat(crNetAmount) + (isNaN(value.crNetAmount) ? 0 : parseFloat(value.crNetAmount));
											$scope.billNumber = value.billNumber;
											
									}
									
								});
								  $scope.mCompanyInvoiceMasterDto={
										  "companyInvoiceId":"",
										  "invoiceNumber":"1",
										  "paymentEntitlementId":$scope.paymentEntitlementIdInvoice,
										  "payeeId":$scope.admissionDetails.payeeId,
										  "associateCompanyId":$scope.associateCompanyIdInvoice,
										  "totalAmount":totalAmount+crNetAmount,
										  "dueAmount":dueAmount,
										  "crNetAmount":crNetAmount,
										  "unitId":$scope.unitId,
										  "orgId":$scope.organizationId,
										  "createdBy":$scope.userId,
										  "updatedBy":"",
										  "invoiceDate":"",
										  "createdDate":"",
										  "updatedDate":"",
										  "billNumber":$scope.billNumber,
										  "invoiceCreationInsurance":$scope.unitConfigurationObject.invoiceCreationInsurance,
										  "invoiceCreationCorporate":$scope.unitConfigurationObject.invoiceCreationCorporate,
										
										}
								
								var listTCompanyInvoiceDetailsDto = [];
								angular.forEach($scope.companyInvoiceList,function(value,index){
									if (value.companyInvoiceSelected == true) 
									{
										var temp ={
												   "companyInvoiceDetailId":"",
												   "companyInvoiceId":"",
												   "billPayeeId":value.payeeId,
												   "billId":(value.pharamcy==false)?value.billId:'',
												   "tSaleId":(value.pharamcy==true)?value.billId:'',
												   "payableAmount":value.billAmount+value.crNetAmount,//amount from bill payee details
												   "dueAmount":value.billAmount,//amount from bill payee details
												   "billAmount":value.netAmount,//amount from bill master
												   "crNetAmount":value.crNetAmount,
												   "unitId":$scope.unitId,
												   "orgId":$scope.organizationId,
												   "createdBy":$scope.userId,
												   "updatedBy":"",
												   "createdDate":"",
												   "updatedDate":"",
												   "billPayeeDetailsId":value.billPayeeDetailsId,
												   "associateCompanyId":value.associateCompanyIdInvoice
												}
										listTCompanyInvoiceDetailsDto.push(temp);
									}
									
								});
							
							
									$rootScope.startLoader();
									var param = {
									        mCompanyInvoiceMasterDto:$scope.mCompanyInvoiceMasterDto,
									        listTCompanyInvoiceDetailsDto:listTCompanyInvoiceDetailsDto,
											orgId:$scope.organizationId,
											unitId:$scope.unitId,
											isConsolidatedBill : $scope.isConsolidatedBill 
									    }
									 console.log("param",JSON.stringify(param));
									
									var URI = BASE_URL + ROOT_URL + "/api/billing/saveCompanyInvoiceCreationList";
									BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										if (response.data.status == "success"){
											//alert("invoice created");
										}else if (response.data.status == "error"){
										}
										$rootScope.stopLoader();
									});
				  }					    
				
				$scope.getBillForCompanyInvoice = function() {
					
					angular.forEach($scope.patientSponsorDetailsDtosListSplit,function(value,index)
							 {
								if(value.priorityId == '1')
								{
									$scope.associateCompanyDescInvoice = value.insuranceDesc;
									$scope.associateCompanyIdInvoice = value.insuranceId;
									$scope.paymentEntitlementIdInvoice = value.paymentEntitlementId;
									
								}
							 });
					var skipInvoive =false;
					if(($scope.paymentEntitlementIdInvoice == 2)){
						if(($scope.unitConfigurationObject.invoiceCreationInsurance == 'N')){
							skipInvoive =true;
						}
					}
					
					if(($scope.paymentEntitlementIdInvoice == 3)){
						if(($scope.unitConfigurationObject.invoiceCreationCorporate == 'N')){
							skipInvoive =true;
						}
					}
					if(skipInvoive == true){
							
					$scope.companyInvoiceList = [];
					  var param = {
							  "orgId":$scope.organizationId,
							  "orgUnitId":$scope.unitId,
							  "payeeId":$scope.admissionDetails.payeeId,
							  "associateCompanyId":$scope.associateCompanyIdInvoice,
							  "visitTypeId" :$scope.visitTypeId,
							  "billId" :$scope.billIdInvoice
						};
							  
					    var URI = BASE_URL + ROOT_URL + "/api/billing/companyInvoiceCreationListByBillId"
					    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							if (response.data.status == "success"){
								 $scope.companyInvoiceList = response.data.listObject;
								 angular.forEach($scope.companyInvoiceList,function(v,k){
									 v.companyInvoiceSelected =true;
								 });
								 $scope.saveCompanyInvoiceCreation();
							}
							$rootScope.stopLoader();
						});
					}
				  }
				
			// function for popup ok button call
				$scope.PopupOkBtn = function() {
					$scope.popUpFlag = false;
					$scope.saveBillings();
					$('.patienttSearchResult').toggle();
				}
				
				$scope.lineItemDiscountAmt = parseFloat(0);
				$scope.saveBillings = function()
				{
					if(Object.keys($scope.autoRenderedServicesFinalList).length == 0)
					{	
						growl.warning(" No service to save..Please add service..!!",{
							title : ERROR_MSG
						});
						return false;
						
					}
				else{
					if($scope.billWiseDiscountObj.requestAmountBillWise > 0 && ($scope.requestStatusObject != null || $scope.requestDiscountDiv == true)){
					  if($scope.billWiseDiscountObj.requestUserId == "")
						{
						  $scope.finalBill = 'N';
							growl.warning("Please select user for Discount Request.",{
								title : ERROR_MSG
							});
							return false;
						}
					else
						{
						$scope.saveBillngNew();
						}
					
				}else{
					
					if($scope.userDiscountObject == null || $scope.userDiscountObject == undefined)
					{
						
						if($scope.isFinancialClearence=='Y' && $scope.financialFlag == 'N'){
							if($scope.finanacial.financialRequestSendTo == undefined || $scope.finanacial.financialRequestSendTo == null || $scope.finanacial.financialRequestSendTo == ""){
								growl.warning("Please select user to send financial clearance  request",{
									title : WARNING_MSG
								});
								return false;
							}
						}
						
						
						if($scope.requestStatusObject != null || $scope.requestStatusObject != undefined){
								if($scope.requestStatusObject.reqStatus == 1) // Already
																				// Pending
									{
									$scope.finalBill = 'N';
									 growl.warning("Discount request is pending..!!",{
											title : ERROR_MSG
										});
							    		return false;
									
									}else if($scope.requestStatusObject.reqStatus == 2){
										var userGivenDiscount = $scope.totalDiscountSummary !== undefined && $scope.totalDiscountSummary !== null ? parseFloat($scope.totalDiscountSummary) : 0;
                                        var approvedDiscount = $scope.requestStatusObject.approvedDiscAmt !== undefined && $scope.requestStatusObject.approvedDiscAmt !== null ? parseFloat($scope.requestStatusObject.approvedDiscAmt) : 0;
                                        var discountDiff = userGivenDiscount - approvedDiscount;
//										if($scope.totalDiscountSummary > $scope.requestStatusObject.approvedDiscAmt)
										if(!(discountDiff < 1.00 && discountDiff > -1.00))
                                        {
                                             growl.warning("Discount approved amount is "+$scope.requestStatusObject.approvedDiscAmt+".",{
                                                    title : ERROR_MSG
                                             });
                                             return false;
                                        }
										else
                                        {
                                            $scope.saveBillngNew();
                                        }
								
							}
					}
						else{
							
							
							if($scope.isFinancialClearence=='Y' && $scope.financialFlag == 'N'){
								if($scope.finanacial.financialRequestSendTo == undefined || $scope.finanacial.financialRequestSendTo == null || $scope.finanacial.financialRequestSendTo == ""){
									growl.warning("Please select user to send financial clearance  request",{
										title : WARNING_MSG
									});
									return false;
								}
							}
							if(($scope.userDiscountObject == null || $scope.userDiscountObject == undefined) &&  ($scope.totalDiscountSummary == 0 ||$scope.totalDiscountSummary == 0.0 || $scope.totalDiscountSummary == '' || $scope.totalDiscountSummary == null))
									{
								         $scope.saveBillngNew();
									}
							else{
									if($scope.totalDiscountSummary > 0 && ($scope.billWiseDiscountObj.requestUserId == null || $scope.billWiseDiscountObj.requestUserId == '' || $scope.billWiseDiscountObj.requestUserId == undefined)){
										$scope.listOfOldDisAmt = [];
										 $scope.isBillWiseDiscountApplied = true;
										 $scope.lineItemDiscountAmt = $scope.totalDiscountSummary;
										 //$scope.billWiseDiscountObj.discountAmountBillWise = $scope.lineItemDiscountAmt;
										 $scope.billWiseDiscountObj.discountAmountBillWise =0;
										 $scope.billWiseDiscountObj.discountPercentageBillWise =0;
										 
										 var disPerForUser = (parseFloat($scope.totalDiscountSummary)/parseFloat($scope.totalDisServiceWise) *100).toFixed(2);
										 
								    		var URI = BASE_URL + ROOT_URL
								    		+ "/billing/getUsersListHavingDiscRights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/amt/"+$scope.totalDiscountSummary+"/per/"+disPerForUser;
								    BillingGenericService.serviceAction(METHOD_GET, URI,"",
								    				NOTIFICATION_MSG_STATUS_FALSE)
								    		.then(function(response) {
								    					//console.log(response);
								    					// $rootScope.startLoader();
								    					if (response.data.status == "success")
								    						{
								    						  $scope.userDiscountList =[];
								    						  $scope.userDiscountList = response.data.listObject;
								    						}
								    						
								    				});
								    
								           $scope.finalBill = 'N';
										   $scope.requestDiscountDiv = true;
										   $scope.billWiseDiscountObj.requestAmountBillWise =  $scope.lineItemDiscountAmt;
										   growl.warning("Please send discount request to user",{
												title : ERROR_MSG
											});
								    		return false;
									
									}else{
										$scope.saveBillngNew();
									}
							    		
						  }
						}
				    		
					}
					else if($scope.userDiscountObject != null || $scope.userDiscountObject != undefined)
						{	
						
						if($scope.isFinancialClearence=='Y' && $scope.financialFlag == 'N'){
							if($scope.finanacial.financialRequestSendTo == undefined || $scope.finanacial.financialRequestSendTo == null || $scope.finanacial.financialRequestSendTo == ""){
								growl.warning("Please select user to send financial clearance  request",{
									title : WARNING_MSG
								});
								return false;
							}
						}
								// if(approvedAmtApp)
						if($scope.userDiscountObject != null){
				    		
							if($scope.userDiscountObject.discountType == 1){
								var fromDisAmt = (parseFloat($scope.totalDisServiceWise) * parseFloat($scope.userDiscountObject.fromDiscountPer))/100;
								var toDisAmt = (parseFloat($scope.totalDisServiceWise) * parseFloat($scope.userDiscountObject.toDiscountPer))/100;
								$scope.userDiscountObject.fromDiscountAmount = fromDisAmt;
								$scope.userDiscountObject.toDiscountAmount = toDisAmt;
							}
				    	}
						
								if(($scope.userDiscountObject.toDiscountAmount >= $scope.totalDiscountSummary) && ($scope.listOfOldDisAmt == undefined || Object.keys($scope.listOfOldDisAmt).length == 0))
									{
										 if($scope.billWiseDiscountObj.requestAmountBillWise == 0)
											 {
												 $scope.listOfOldDisAmt = [];
												 $scope.isBillWiseDiscountApplied = false;
												 $scope.requestDiscountDiv = false;
												 $scope.lineItemDiscountAmt = parseFloat(0);
												 //$scope.billWiseDiscountObj.discountAmountBillWise =  parseFloat(0);
												 //$scope.billWiseDiscountObj.requestAmountBillWise =   parseFloat(0);
												 $scope.discountRadio = 2;
												 $scope.saveBillngNew();
											 }
										 else{
											 $scope.discountRadio = 2;
											 $scope.saveBillngNew();
										 }
								    		
									  
									}
								else if(($scope.userDiscountObject.toDiscountAmount >= $scope.totalDiscountSummary) && $scope.listOfOldDisAmt.length > 0)
									{
									   $scope.saveBillngNew();
									}
								else
									{
										if($scope.requestStatusObject != null || $scope.requestStatusObject != undefined)
										{
												
											
											if($scope.requestStatusObject.reqStatus == 1) // Already
																							// Pending
											{
											
											 growl.warning("Discount request is pending..!!",{
													title : ERROR_MSG
												});
									    		return false;
											
											}else if($scope.requestStatusObject.reqStatus == 2){
												
												if($scope.totalDiscountSummary > $scope.requestStatusObject.approvedDiscAmt)
													{
												    	   
														 growl.warning("Discount approved amount is "+$scope.requestStatusObject.approvedDiscAmt+".",{
																title : ERROR_MSG
															});
												    		return false;
													}
												else if($scope.totalDiscountSummary <= $scope.requestStatusObject.approvedDiscAmt)
													{
													   $scope.saveBillngNew();
													}
										
									}
										
										}else
											{
											if($scope.totalDiscountSummary > 0 && ($scope.billWiseDiscountObj.requestUserId == null || $scope.billWiseDiscountObj.requestUserId == '' || $scope.billWiseDiscountObj.requestUserId == undefined)){
												$scope.listOfOldDisAmt = [];
												 $scope.isBillWiseDiscountApplied = true;
												 $scope.lineItemDiscountAmt = $scope.totalDiscountSummary;
												 $scope.billWiseDiscountObj.discountAmountBillWise = $scope.lineItemDiscountAmt;
												 $scope.billWiseDiscountObj.discountPercentageBillWise =  (parseFloat($scope.lineItemDiscountAmt)/(parseFloat($scope.totalDisServiceWise))*100).toFixed(2)
												// $scope.funDiscountPercentageBillWise();
												 $scope.discountRadio = 2;
										    	   
										    		var URI = BASE_URL + ROOT_URL
										    		+ "/billing/getUsersListHavingDiscRights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/amt/"+$scope.billWiseDiscountObj.discountAmountBillWise+"/per/"+$scope.billWiseDiscountObj.discountPercentageBillWise;
										    BillingGenericService.serviceAction(METHOD_GET, URI,"",
										    				NOTIFICATION_MSG_STATUS_FALSE)
										    		.then(function(response) {
										    					//console.log(response);
										    					// $rootScope.startLoader();
										    					if (response.data.status == "success")
										    						{
										    						  $scope.userDiscountList =[];
										    						  $scope.userDiscountList = response.data.listObject;
										    						}
										    						
										    				});
										    if($scope.requestStatusObject != null || $scope.requestStatusObject != undefined){
										           $scope.finalBill = 'N';
												   $scope.requestDiscountDiv = true;
												   $scope.billWiseDiscountObj.requestAmountBillWise =  $scope.lineItemDiscountAmt;
												   growl.warning("Please send discount request to user",{
														title : ERROR_MSG
													});
										    		return false;
												}else{
													$scope.saveBillngNew();
												}
											}
											else{
												$scope.saveBillngNew();
											}
									}
									}	
								}
					else
						{
						$scope.saveBillngNew();
						}
					
				}// for DD
			  }
				}
				
				 var isGroupExistsCap;
				  function checkGroupIdExistsOrNotCap(arr, val) {
						return arr.some(function(arr) {
								if(val.orderId === arr.orderId)
										{
											return true;
										}
									else
										{
											return false;
										}
								});
					}
				  
				$scope.intermediateServiceTemp = [];
				$scope.saveBillngNew = function()
				{
					//alert($scope.finanacial.financialRequestSendTo);
					
					////console.log('$scope.financialRequestSendTo',$scope.financialRequestSendTo);
					$scope.billRemark = $scope.admissionDetails.remark;
					if($scope.requestStatusObject !=null &&  $scope.requestStatusObject.reqStatus == 1 && $scope.isIntermediateBill == 'Y')
					{
						$scope.isIntermediateBill ='N';
						$scope.checkAllFlag = false;
						$scope.checkAll();
						growl.warning("Bill can't generate, discount request is still pending..!!",{
							title : ERROR_MSG
						});
			    		return false;
					}
					
					if($scope.billWiseDiscountObj.requestAmountBillWise > 0 && $scope.isIntermediateBill == 'Y'){
						
						$scope.isIntermediateBill = 'N';
						$scope.checkAllFlag = false;
						$scope.checkAll();
						growl.warning("Bill can't generate, need to cancel discount and generate bill..!!",{
							title : ERROR_MSG
						});
			    		return false;
					}
					
					
					if($scope.requestStatusObject !=null &&  $scope.requestStatusObject.reqStatus == 1 && $scope.finalBill == 'Y')
					{
						$scope.finalBill ='N';
						growl.warning("Bill can't generate, discount request is still pending..!!",{
							title : ERROR_MSG
						});
			    		return false;
					}
					
					if($scope.billWiseDiscountObj.requestAmountBillWise > 0 && $scope.finalBill == 'Y'){
						
						$scope.finalBill = 'N';
						growl.warning("Bill can't generate, need to cancel discount and generate bill..!!",{
							title : ERROR_MSG
						});
			    		return false;
					}
					
					var discountDetailForLine = [];
					//console.log("SAVE ORDER DETAILS",$scope.autoRenderedServicesFinalList);
					
					//console.log("tttt",$scope.removeDiscountList);
				
					if($scope.isFinancialClearence == 'Y' && $scope.financialFlag == 'N' && ($scope.finanacial.financialRequestSendTo == "" || $scope.finanacial.financialRequestSendTo == undefined || $scope.finanacial.financialRequestSendTo < 0))
						{
							 growl.warning("Please select user for financial request.",{
									title : ERROR_MSG
								});
					    		return false;
						}
					else{
					//return false;
					
					
					var saveOrderDetailsObj = [];
					
					if($scope.isIntermediateBill == 'Y' && $scope.servicesSelectedList.services.length == 0)
						{
							 growl.warning("Please select service for intermediate.",{
									title : ERROR_MSG
								});
					    		return false;
						}
					else if($scope.isIntermediateBill == 'Y' && $scope.servicesSelectedList.services.length > 0)
						{
						$scope.intermediateServiceTemp = angular.copy($scope.autoRenderedServicesFinalList);

						if(!$scope.saveFromSplitBill)
                        {
                            var promise = $scope.calculationForIntermediate();
                        }

						        var data={
								patientId :$scope.admissionDetails.patientId,
								//encounterId : $scope.admissionDetails.admissionId,
								admissionId : $scope.admissionDetails.admissionId,
								createdBy :$scope.userId,
								createdDate :new Date().getTime(),
								updatedBy :$scope.userId,
								updatedDate :new Date().getTime(),
								orderDate :new Date().getTime(),
								organisationId :$scope.organizationId,
								unitId :$scope.unitId,
								isFinalBill : $scope.finalBill,
								isInterimBill : $scope.interimBill,
								isIntermediateBill :$scope.isIntermediateBill,
								ordDoctorId :$scope.admissionDetails.doctorId,
								visitTypeId : $scope.visitTypeId,
								billDateTime : new Date().getTime(),
								netAmount : parseFloat(parseFloat($scope.netPayableSummary)).toFixed(2),
								discountAmount : $scope.totalDiscountSummary,
								concessionAmount : $scope.totalConcessionSummary,
								gst : $scope.totalGstTaxSummary,
								totalBillAmount : $scope.totalAmountSummary,
								roundOff : $scope.roundOffAmt,
								isCreditBill:'N',
								billCancelled : 'N',
								billRemark : $scope.billRemark,
								billStatusId : '1', // 1. Pending,
								selfPayable : $scope.totalSelfPayble,
								creditPayable : $scope.totalCreditPayble,
								cancelledDiscountsList : $scope.removeDiscountList,
								tariffId : $scope.defaultSelfTariffId,
								paymentEntitlementId : $scope.admissionDetails.paymentEntitlementId,
								visitTypeId : $scope.visitTypeId,
								isFinancialClearance : $scope.isFinancialClearence,
								financialRequestSendTo:$scope.finanacial.financialRequestSendTo,
								patientTypeId : $scope.admissionDetails.patientCategoryId,//default op
								billingBedCategoryId : $scope.admissionDetails.bedBillingCategoryId, 
								isInterimOrFinalBill : $scope.interimBill == 'Y' ? 1 : $scope.finalBill == 'Y' ? 2 : null,
								lineLevelDiscountOrderList : discountDetailForLine,
							    saveFromSplitBill :	$scope.saveFromSplitBill,
			                    isAddendumBill : $scope.isAddendumBill,
			                    isBillManipulationAllowed : $scope.isBillManipulationAllowed
						  };
						        
						        if($scope.isBillWiseDiscountApplied == true)
								{
									data.isDiscountApplied = 'Y';
								}
						
						if($scope.requestStatusObject !=null && ($scope.requestStatusObject.reqStatus == 2 || $scope.requestStatusObject.reqStatus == 1))
						{
							data.reqDiscId = $scope.requestStatusObject.reqDiscId;
						}
					
						//add ucc
						data.reqDisAmt = null;
						data.requestSentTo = null;
						data.discType = null;
						$scope.listOfOldDisAmt = [];
						
						var billPayeeList =[];
						if($scope.totalSelfPayble > 0)
							{
								var billPayeeArr ={
										payeeId : $scope.selfPayeeId,
										billAmount : $scope.totalSelfPayble
								}
								billPayeeList.push(billPayeeArr);
							}
						if($scope.totalCreditPayble > 0)
							{
								var billPayeeArr ={
										payeeId : $scope.admissionDetails.payeeId,
										billAmount : $scope.totalCreditPayble
								}
								billPayeeList.push(billPayeeArr);
							}
						
						data.billPayeeDetailsDtosList = billPayeeList;
						
						
						//var arrUnique = unique($scope.servicesSelectedList.services);
						//var len = arrUnique.length;
						
						var len = $scope.servicesSelectedList.services.length;
				    	for(var i = 0;i < len;i++)
						{
						   //console.log($scope.servicesSelectedList.services);
						   //alert($scope.servicesSelectedList.services.length);
						   
						   angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
							   if(value.orderDetailsId == $scope.servicesSelectedList.services[i])
							   {
								var saveArr = {};
								if(value.orderDetailsId > 0){
									saveArr.orderDetailsId = value.orderDetailsId;
									saveArr.isFromAddservice = value.isFromAddservice;
								}
								else
									{
										saveArr.isFromAddservice = 'Y';
									}
								saveArr.discountOwnership = value.discountOwnership;
								saveArr.isDocReq = value.isDoctorRequired;
								saveArr.markupStatus = value.markupStatus;
								saveArr.patientClassPercentage = value.patientClassPercentage;
								saveArr.orgId = $scope.organizationId;
								saveArr.orgUnitId  = $scope.unitId;
								saveArr.orderId = value.orderId;
								saveArr.orderQty = value.quantity;
								saveArr.serviceId = value.serviceMasterId;
								saveArr.priorityId = value.priorityId;
								saveArr.isOutsourced = value.isOutsourced;
								//saveArr.orderDate = new Date().getTime();
								saveArr.orderDate =   (value.orderDate !=null && value.orderDate!=undefined && value.orderDate!='')?value.orderDate:new Date().getTime();
								saveArr.orderSchDate = new Date().getTime();
								saveArr.serviceAmt = value.basePrice;
								saveArr.ordConcession = value.concession;
								saveArr.ordDiscount = value.discount;
								saveArr.netAmount = value.netPay;
								//saveArr.ordDocSplId = value.specialityId;
				                saveArr.ordDocSplId = value.doctorSpecialityId;
								saveArr.taxId = value.taxId;
								saveArr.taxPer  = value.taxPercentage;
								saveArr.taxAmount = value.taxAmt;
								saveArr.selfPayable = value.selfPayable;
								saveArr.creditPayable = value.creditPayable;
								saveArr.payeeId = $scope.admissionDetails.payeeId;
								saveArr.ordTotalAmt =value.totalAmt;
								//saveArr.tariffId = $scope.defaultSelfTariffId;
								saveArr.tariffId = value.tariffId;
								saveArr.billingClassId = '1';
								saveArr.ordDocId = value.doctorId;
								saveArr.ordDocSplId = value.doctorSpecialityId;
								saveArr.serviceRendered = value.serviceRendered;//Ganesh
								saveArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
								saveArr.ordRenderDatetime = value.ordRenderDatetime;
								saveArr.serviceIsBilled = '0';
								saveArr.status = value.status;
								saveArr.updatedBy = $scope.userId;
								saveArr.updatedDate =new Date().getTime();
					            saveArr.createdBy  = value.createdBy != null ? value.createdBy : $scope.userId;
								saveArr.createdDate  = value.createdDate;
								saveArr.oldNetPay =  parseFloat(value.oldNetPay).toFixed(2);
								saveArr.ordCancelled = value.ordCancelled;
								saveArr.isOrderVisibleDepartment = value.isOrderVisibleDepartment;
								saveArr.isConsultationService = value.isConsultationService;
								saveArr.ordRemarks = value.ordRemarks;
								saveArr.nursingServiceOrderFlag =value.nursingServiceOrderFlag;
								saveArr.ordCancelReasonId  = value.ordCancelReasonId;
								saveArr.ordCancelDatetime  = value.ordCancelDatetime;
								saveArr.ordCancelRemark  = value.ordCancelRemark;
								saveArr.oldOrdDtlId = value.oldOrdDtlId;
					            saveArr.dummyServiceAmt = value.dummyServiceAmt;
					            saveArr.discountCategoryId = value.discountCategoryId;
								//Ganesh
								saveArr.oldCreditPayable = value.creditPayableOldC;
								saveArr.oldSelfPayable = value.selfPayableOldC;
								saveArr.isInvestigationOrder = value.isInvestigationOrder;
								saveArr.isDiscountApplicable = value.isDiscountApplicable;
								saveArr.isDiscount = value.isDiscount;
								saveArr.orderEmergencyFlag = 'N';
								saveArr.isDrug = 'N';
								saveArr.isPackage = typeof(value.isPackage) != "undefined" ? value.isPackage : false;
								saveArr.packageId = typeof(value.packageMasterId) != "undefined" && value.packageMasterId != null ? value.packageMasterId : value.packageId;
								saveArr.packageTypeId = typeof(value.packageTypeId) != "undefined" ? value.packageTypeId : null;
								saveArr.isPackageService = typeof(value.isPackageService) != "undefined" ? value.isPackageService : 'N';
								saveArr.apportionedPrice = typeof(value.apportionedPrice) != "undefined" ? value.apportionedPrice : null;
								saveArr.isCapService = value.isCapService;
								saveArr.packageConsumptionAmt = value.packageConsumptionAmt;
								saveArr.departmentId = value.serviceSpecialityId!=null? value.serviceSpecialityId:value.specialityId;
								saveArr.subDepartmentId = value.subSpecialityId;
								saveArr.encounterId = $scope.encounterDetails.encounterId;
								saveArr.admissionId = $scope.admissionDetails.admissionId;
								saveArr.procedureOrderMstId = value.procedureOrderMstId > 0 ? value.procedureOrderMstId  : null;
								saveArr.isProcedureService = value.isProcedureService!=null ? value.isProcedureService  : 'N';
								saveArr.isPostedWhileDischarge = value.isPostedWhileDischarge!=null ? value.isPostedWhileDischarge : 'N';
								saveArr.isChargedHalfDay = value.isChargedHalfDay;
								saveArr.contractId = value.contractId;
								saveArr.discountDetailsList = [];
								saveArr.serviceAmt = value.basePrice;
								saveArr.isRefDoctorShare = value.isRefDoctorShare;
								saveArr.discountDetailsList = [];
								saveArr.debitNoteService = false;
								
								  //package force add
								saveArr.forceAddRemoved = value.forceAddRemoved;
								saveArr.isConsumed = value.isConsumed;
								
					            saveArr.isPeriodicityService = value.isPeriodicityService;
					            saveArr.bedCategoryId = value.bedCategoryId;
					            saveArr.wardId = value.wardId;
					            saveArr.roomId = value.roomId;
					            saveArr.bedId = value.bedId;
					            saveArr.isDummyService = value.isDummyService;
								saveArr.isHiddenService = value.isHiddenService;
								if(value.discountDetailsList == null)
									{
									value.discountDetailsList = [];
									}
							
								saveArr.reqDisAmt = null;
							//For DD end for ucc
								saveArr.orderDetailsPayeeMapperDtosList  = value.orderDetailsPayeeMapperDtosList;
								saveArr.orderDetailsPackageMapperDtosList  = value.orderDetailsPackageMapperDtosList;
								saveOrderDetailsObj.push(saveArr);
						   }
							   else{
								if((value.orderDetailsId == null || value.orderDetailsId == undefined) && $scope.popUpFlag == false){

										if(value.contractId > 0)
								    	{
								    		if(value.creditPayable>0)
											{
								    			$scope.addCapAmout(value);
											}
								    		
								    	}
									}
							   }
							   
							});
						   
						}
				    	 
						}
					else{
						
						var data={
								
								patientId :$scope.admissionDetails.patientId,
								//encounterId : $scope.encounterDetails.admissionId,
								admissionId : $scope.admissionDetails.admissionId,
								createdBy :$scope.userId,
								createdDate :new Date().getTime(),
								updatedBy :$scope.userId,
								updatedDate :new Date().getTime(),
								orderDate :new Date().getTime(),
								organisationId :$scope.organizationId,
								unitId :$scope.unitId,
								isFinalBill : $scope.finalBill,
								isInterimBill : $scope.interimBill,
								isIntermediateBill :$scope.isIntermediateBill,
								ordDoctorId :$scope.admissionDetails.doctorId,
								visitTypeId : $scope.visitTypeId,
								billDateTime : new Date().getTime(),
								netAmount : parseFloat(parseFloat($scope.netPayableSummary)).toFixed(2),
								discountAmount : $scope.totalDiscountSummary,
								concessionAmount : $scope.totalConcessionSummary,
								gst : $scope.totalGstTaxSummary,
								totalBillAmount : $scope.totalAmountSummary,
								roundOff : $scope.roundOffAmt,
								isCreditBill:'N',
								billCancelled : 'N',
								billRemark : $scope.billRemark,
								billStatusId : '1', // 1. Pending,
								selfPayable : $scope.totalSelfPayble,
								creditPayable : $scope.totalCreditPayble,
								cancelledDiscountsList : $scope.removeDiscountList,
								tariffId : $scope.defaultSelfTariffId,
								paymentEntitlementId : $scope.admissionDetails.paymentEntitlementId,
								visitTypeId : $scope.visitTypeId,
								isFinancialClearance : $scope.isFinancialClearence,
								financialRequestSendTo:$scope.finanacial.financialRequestSendTo,
								patientTypeId : $scope.admissionDetails.patientCategoryId,//default op
								billingBedCategoryId : $scope.admissionDetails.bedBillingCategoryId, 
								isInterimOrFinalBill : $scope.interimBill == 'Y' ? 1 : $scope.finalBill == 'Y' ? 2 : null,
								lineLevelDiscountOrderList : discountDetailForLine,
								saveFromSplitBill :	$scope.saveFromSplitBill,
			                    isAddendumBill : $scope.isAddendumBill,
			                    isBillManipulationAllowed : $scope.isBillManipulationAllowed

						};
						
						 if($scope.isBillWiseDiscountApplied == true)
							{
								data.isDiscountApplied = 'Y';
							}
						
						 if($scope.requestStatusObject !=null )
							{
								data.reqDiscId = $scope.requestStatusObject.reqDiscId;
							}
					
					//for DD
					if($scope.billWiseDiscountObj.requestAmountBillWise > 0 && $scope.finalBill == 'N'){
						data.reqDisAmt = $scope.billWiseDiscountObj.requestAmountBillWise - (parseFloat($scope.roundOffAmt));
						data.requestSentTo = $scope.billWiseDiscountObj.requestUserId;
						data.discType = $scope.billWiseDiscountObj.discountCategoryId;
						data.remark = $scope.billWiseDiscountObj.remark;
					}
					//FOR DD
						
						var billPayeeList =[];
						if($scope.totalSelfPayble > 0)
							{
								var billPayeeArr ={
										payeeId : $scope.selfPayeeId,
										billAmount : $scope.totalSelfPayble
								}
								billPayeeList.push(billPayeeArr);
							}
						if($scope.totalCreditPayble > 0)
							{
								var billPayeeArr ={
										payeeId : $scope.admissionDetails.payeeId,
										billAmount : $scope.totalCreditPayble
								}
								billPayeeList.push(billPayeeArr);
							}
						
						data.billPayeeDetailsDtosList = billPayeeList;
					angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
						var saveArr = {};
						if(value.orderDetailsId > 0){
							saveArr.orderDetailsId = value.orderDetailsId;
							saveArr.isFromAddservice = value.isFromAddservice;
						}else
						{
							saveArr.isFromAddservice = 'Y';
						}
						saveArr.discountOwnership = value.discountOwnership;
						saveArr.isDocReq = value.isDoctorRequired;
						saveArr.markupStatus = value.markupStatus;
						saveArr.patientClassPercentage = value.patientClassPercentage;
						saveArr.orgId = $scope.organizationId;
						saveArr.orgUnitId  = $scope.unitId;
						saveArr.ordRemarks = value.ordRemarks;
						saveArr.orderId = value.orderId;
						saveArr.orderQty = value.quantity;
						saveArr.serviceId = value.serviceMasterId;
						saveArr.priorityId = value.priorityId;
						saveArr.isOutsourced = value.isOutsourced;
						//saveArr.orderDate = new Date().getTime();
						saveArr.orderDate =   (value.orderDate !=null && value.orderDate!=undefined && value.orderDate!='')?value.orderDate:new Date().getTime();
						saveArr.orderSchDate = new Date().getTime();
						saveArr.serviceAmt = value.basePrice;
						saveArr.ordConcession = value.concession;
						saveArr.ordDiscount = value.discount;
						saveArr.netAmount = value.netPay;
						 saveArr.ordDocSplId = value.doctorSpecialityId;
						saveArr.taxId = value.taxId;
						saveArr.taxPer  = value.taxPercentage;
						saveArr.taxAmount = value.taxAmt;
						saveArr.selfPayable = value.selfPayable;
						saveArr.creditPayable = value.creditPayable;
						saveArr.payeeId = $scope.admissionDetails.payeeId;
						saveArr.ordTotalAmt =value.totalAmt;
						//saveArr.tariffId = $scope.defaultSelfTariffId;
						saveArr.tariffId = value.tariffId;
						saveArr.billingClassId = '1';
						saveArr.ordDocId = value.doctorId;
						saveArr.ordDocSplId = value.doctorSpecialityId;
						saveArr.serviceRendered = value.serviceRendered;
						saveArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
						saveArr.ordRenderDatetime = value.ordRenderDatetime;
						saveArr.serviceIsBilled = '0';
						saveArr.status = value.status;
						saveArr.updatedBy = $scope.userId;
						saveArr.updatedDate =new Date().getTime();
						saveArr.createdBy  = value.createdBy;
						saveArr.createdDate  = value.createdDate;
						saveArr.oldNetPay =  parseFloat(value.oldNetPay).toFixed(2);
						saveArr.ordCancelled = value.ordCancelled;
			            saveArr.dummyServiceAmt = value.dummyServiceAmt;
						saveArr.ordCancelReasonId  = value.ordCancelReasonId;
						saveArr.ordCancelDatetime  = value.ordCancelDatetime;
						saveArr.ordCancelRemark  = value.ordCancelRemark;
						saveArr.oldOrdDtlId = value.oldOrdDtlId;
						saveArr.discountCategoryId = value.discountCategoryId;
						//Ganesh
						saveArr.oldCreditPayable = value.creditPayableOldC;
						saveArr.oldSelfPayable = value.selfPayableOldC;
						saveArr.isInvestigationOrder = value.isInvestigationOrder;
						
						saveArr.orderEmergencyFlag = 'N';
						saveArr.isDrug = 'N';
						saveArr.isDiscountApplicable = value.isDiscountApplicable;
						saveArr.isDiscount = value.isDiscount;
						saveArr.isPackage = typeof(value.isPackage) != "undefined" ? value.isPackage : false;
						saveArr.packageId = typeof(value.packageMasterId) != "undefined" && value.packageMasterId != null ? value.packageMasterId : value.packageId;
						saveArr.packageTypeId = typeof(value.packageTypeId) != "undefined" ? value.packageTypeId : null;
						saveArr.isPackageService = typeof(value.isPackageService) != "undefined" ? value.isPackageService : 'N';
						saveArr.apportionedPrice = typeof(value.apportionedPrice) != "undefined" ? value.apportionedPrice : null;
						saveArr.isCapService = value.isCapService;
						saveArr.packageConsumptionAmt = value.packageConsumptionAmt;
						saveArr.departmentId = value.serviceSpecialityId!=null? value.serviceSpecialityId:value.specialityId;
						saveArr.subDepartmentId = value.subSpecialityId;
						saveArr.encounterId = $scope.encounterDetails.encounterId;
						saveArr.procedureOrderMstId = value.procedureOrderMstId > 0 ? value.procedureOrderMstId  : null;
						saveArr.isProcedureService = value.isProcedureService!=null ? value.isProcedureService  : 'N';
						saveArr.isPostedWhileDischarge = value.isPostedWhileDischarge!=null ? value.isPostedWhileDischarge : 'N';
						saveArr.isChargedHalfDay = value.isChargedHalfDay;
						saveArr.admissionId = $scope.admissionDetails.admissionId,
						saveArr.contractId = value.contractId;
						saveArr.discountDetailsList = [];
						saveArr.serviceAmt =  value.basePrice;
						saveArr.isRefDoctorShare = value.isRefDoctorShare;
						saveArr.discountDetailsList = [];
						saveArr.bedCategoryId = value.bedCategoryId;
						saveArr.wardId = value.wardId;
				        saveArr.roomId = value.roomId;
				        saveArr.bedId = value.bedId;
						saveArr.debitNoteService = false;
						saveArr.isDummyService = value.isDummyService;
						saveArr.isHiddenService = value.isHiddenService;
						 //package force add
						saveArr.forceAddRemoved = value.forceAddRemoved;
						saveArr.isConsumed = value.isConsumed;
						
						if(value.discountDetailsList == null)
							{
							value.discountDetailsList = [];
							}
						
						//for DD
						if($scope.billWiseDiscountObj.requestAmountBillWise > 0 && value.isDiscountApplicable == 'Y' && $scope.finalBill == 'N')
							{
							saveArr.reqDisAmt  = value.discount;
							}
					//For DD
						saveArr.orderDetailsPayeeMapperDtosList  = value.orderDetailsPayeeMapperDtosList;
						saveArr.orderDetailsPackageMapperDtosList  = value.orderDetailsPackageMapperDtosList;
						saveOrderDetailsObj.push(saveArr);
					 });
					}
					data.orderDetailsMasterDtosList = saveOrderDetailsObj;
					data.cancelOrderDetailsList = $scope.removeServicesList;
					data.referralId = $scope.admissionDetails.referralId;

					data.againstBillId = null;
			       if($scope.preBillId > 0 && $scope.preBillId != undefined){
			    	   data.againstBillId = $scope.preBillId;
			       	}

					
					
					//console.log("finalSaveData",data);
						//return false;
					
						if($scope.finalBill == 'Y')
						{
							if ($scope.popUpFlag) {
								$scope.showModal = !$scope.showModal;
							
							}else{
									$scope.popUpFlag = true;
									$("#savedisablebtn").addClass("disabled");
									//console.log("finalSaveData",data);
									//console.log(JSON.stringify(data));
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + SAVEBILL;
									BillingGenericService.serviceAction(METHOD_POST,URI, data , NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
										////console.log(response)
										if(response.data.status == 'success'){
											$scope.billIdInvoice = response.data.object;
										
											$scope.getBillForCompanyInvoice();
											if($scope.outsourceLabId == 1){		
												
												$scope.ordersIdListNeuron = [];
											angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
												 if(value.serviceSpecialityId == $scope.labConfig.labSpecialityId)
													{
														var saveNuroPlus = {};
														saveNuroPlus.branchName = "NphMain";
														saveNuroPlus.orderId = value.orderId.toString();
														saveNuroPlus.IPOPNo = $scope.admissionNumber;
														saveNuroPlus.UHID = $scope.uhidNum;
														var orderDate =   (value.orderDate !=null && value.orderDate!=undefined && value.orderDate!='')?value.orderDate:new Date().getTime();
														var orderSchDate = new Date().getTime();
														saveNuroPlus.orderTs =value.orderDateString+"T"+value.orderDateTime;
															//"2022-02-15T13:24:00"; 
														if($scope.visitTypeId == 2){
															var tyy= "IPD";
														}else if($scope.visitTypeId == 3){
															var tyy= "DC";
														}else if($scope.visitTypeId == 4){
															var tyy= "ER";
														}
														saveNuroPlus.ipoptype = tyy;
														saveNuroPlus.salutation =  $scope.patientDetails.genderId.toString();
														saveNuroPlus.patientName =  $scope.patientDetails.firstName;
														saveNuroPlus.gender = $scope.patientDetails.genderCode;
													     var Birth = moment($scope.patientDetails.birthDate,"DD/MM/YYYY").toDate();
													     var Bx = moment(Birth).format('YYYY-MM-DD');
														saveNuroPlus.birthDate = Bx+"T00:00:00";
														saveNuroPlus.ward ="";
														saveNuroPlus.patMobileNo = $scope.mobileNumber.toString();
														saveNuroPlus.patEmailId = $scope.patientDetails.email;
														if($scope.patientDetails.address != ""){
															saveNuroPlus.patAddress = $scope.patientDetails.address;
														}else{
															saveNuroPlus.patAddress = "";
														}
														
														saveNuroPlus.patCity = $scope.patientDetails.cityName;
														saveNuroPlus.refDrCode = value.doctorName;
														saveNuroPlus.refDrName = value.doctorName;
														saveNuroPlus.feesPaid = false;
														saveNuroPlus.orderTests=[];
														if($scope.ordersIdListNeuron.length==0){
														$scope.ordersIdListNeuron.push(saveNuroPlus);
													}else{
														isGroupExistsCap=checkGroupIdExistsOrNotCap($scope.ordersIdListNeuron,saveNuroPlus)
														  if (!isGroupExistsCap) {
															$scope.ordersIdListNeuron.push(saveNuroPlus);
														  }
													}
											     }
											});
											
											angular.forEach($scope.ordersIdListNeuron, function (value, key) {
											    angular.forEach($scope.autoRenderedServicesFinalList, function (value1, key1) {
												    if(value.orderId == value1.orderId){
												    	if(value1.serviceSpecialityId == $scope.labConfig.labSpecialityId)
														{
												    		var newAer = {};
												    		newAer.testRecordId = value1.serviceMasterId.toString();
												    		newAer.testName = value1.serviceStandardName;
												    		newAer.testCode = value1.serviceStandardCode;
												    		value.orderTests.push(newAer);
														}
												    }
												});
											});
											console.log("nuronlist",$scope.ordersIdListNeuron);
											angular.forEach($scope.ordersIdListNeuron, function (value, key) {
												var data ={};
												data=value;
												var url = 'https://ngr.dyndns.org/NeuronPlusHosp/Order';
												var config ={
														  "Access-Control-Allow-Origin":'*',
														  "Access-Control-Allow-Methods":"HEAD, GET, POST, PUT, PATCH, OPTIONS ,DELETE",
														  "Access-Control-Allow-Headers":'Access-Control-*, Origin, X-Requested-With, Content-Type, Accept,Origin, X-Auth-Token',
														  'Allow' :'GET, POST, PUT, DELETE, OPTIONS, HEAD',
														  'accept' :'application/json, text/javascript, *',
														  'content-type' :'application/json',
														  'scheme' :'https',
												};

												$http.post(url, data, config).then(function (response) {

												// This function handles success
//													alert("success");
													console.log(response);
													if(response.data.statusCode==200){
														var masse=response.data.message;
														var arr = masse.split(' ');
														var URI = BASE_URL + ROOT_URL + "/api/global/saveNeuronPlusorderedServices";
														var data ={
																"orgId" : $scope.organizationId,
																 "orgUnitId" :$scope.unitId,
																"orderId":arr[3],
						    									"neuronPlusUrl":response.data.reportUrl,
						    									
															}
															GenericService.serviceAction("POST", URI, data).then(function (response) {
																if (response.data.status == "success") 
																{
													            }
															});
													}

												}, function (response) {
//													alert("error");
												// this function handles error

												});

											});
											}
											if($scope.outsourceLabId == 2){	
												
												$scope.ordersIdListMedall= [];
											angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
												 if(value.serviceSpecialityId == $scope.labConfig.labSpecialityId)
													{
													 if($scope.patientDetails.genderId.toString() == 1){
														 $scope.patientDetails.genderId="0"
													 }else if($scope.patientDetails.genderId.toString() == 2){
														 $scope.patientDetails.genderId="1"
													 }else{
														 $scope.patientDetails.genderId="2"
													 }
													 var centerid = ($scope.medallUnitObj.medallCenterId != undefined && $scope.medallUnitObj.medallCenterId != null ? $scope.medallUnitObj.medallCenterId.toString() : "1478");
														
													 if($scope.visitTypeId == 2){
															var tyy= "IPD";
														}else if($scope.visitTypeId == 3){
															var tyy= "DC";
														}else if($scope.visitTypeId == 4){
															var tyy= "ER";
														}
													 
													 var Birth = moment($scope.patientDetails.birthDate,"DD/MM/YYYY").toDate();
													 var Bx = moment(Birth).format('YYYY-MM-DD');
													 var medallObj =
													 {
														    "name": $scope.patientDetails.firstName,
														    "gender": $scope.patientDetails.genderId,
														    "mobile":  $scope.mobileNumber.toString(),
														    "pincode":  $scope.patientDetails.zipCode != null ? $scope.patientDetails.zipCode :600020,
														    "address": $scope.patientDetails.address != null ? $scope.patientDetails.address:"",
														    "state": $scope.patientDetails.stateName!= null ? $scope.patientDetails.stateName:"",
														    "district": $scope.patientDetails.districtName != null ? $scope.patientDetails.districtName : "",
														    "passport": null,
														    "srfid": null,
														    //"referalid": null,
														    "relationid": "1",
														   // "salutationid": $scope.patientDetails.prefixId != null ?$scope.patientDetails.prefixId:"",
														    "nationality": "Indian",
														    "dob": Bx+" 00:00:00",
														    "centerid": centerid,
														    "appointmentid": null,
														    "externalid":  $scope.patientDetails.uhIdNumber,
														    "emailid": $scope.patientDetails.email != null ? $scope.patientDetails.email:"",
														    "orderid":value.orderId,
														    "sourceid":"10019471",
														    "hospitaltype":tyy,
														    "studyinfo": []
														}
													 
													 var prefixId = "2513";
													 
														angular.forEach($scope.medallPrifixList, function (val, inx) {
															
															if($scope.patientDetails.prefixId == val.prefixId && val.medallSalutationIdPrefixId != null){
																prefixId = val.medallSalutationIdPrefixId;
															}
															
														});
														
														medallObj.salutationid = prefixId.toString();
														
														if(value.doctorId != undefined && value.doctorId != null && value.doctorId != 0 ){
															
															var check = true;
															angular.forEach($scope.medallDoctorList, function (val, inx) {
																
																if(value.doctorId == val.doctorId && val.medallReferalId != null){
																	medallObj.referalid = val.medallReferalId;
																	check = false;
																}
																
															});
															
															if(check == true){
																medallObj.referalid = '5001195';
															}
														}else if( $scope.admissionDetails.referralId != undefined &&  $scope.admissionDetails.referralId != null &&  $scope.admissionDetails.referralId != 0 ){
															
															var check = true;
															angular.forEach($scope.referralMasterList, function (val, inx) {
																
																if( $scope.admissionDetails.referralId == val.referralId && val.medallReferralId != null){
																	medallObj.referalid = val.medallReferralId;
																	check = false;
																}
																
															});
															
															if(check == true){
																medallObj.referalid = '5001195';
															}
														}else{
															medallObj.referalid = '5001195';
														}

													 	if($scope.ordersIdListMedall.length==0){
														 $scope.ordersIdListMedall.push(medallObj);
														}
											     }
											});
											var count=0;
											angular.forEach($scope.ordersIdListMedall, function (value, key) {
											    angular.forEach($scope.autoRenderedServicesFinalList, function (value1, key1) {
												    if(value.orderid == value1.orderId){
												    	if(value1.serviceSpecialityId == $scope.labConfig.labSpecialityId)
														{
												    		if(value1.medallServiceId != undefined && value1.medallServiceId != null && value1.medallServiceId != ""){
												    			count++;
													    			var newAer = {};
														    		newAer.billingamount="0";
														    		newAer.billingid="0";
														    		newAer.billingtype="";
														    		newAer.corpbillingid=(value1.medallServiceId).toString();
														    		newAer.discount="0";
														    		newAer.paidamount="0";
														    		newAer.topaid="0";
														    		newAer.extraamount="0";
														    		value.studyinfo.push(newAer);
													    		}
															}
												    }
												});
											});
											if($scope.ordersIdListMedall != null && $scope.ordersIdListMedall.length >0 ){
												if( $scope.ordersIdListMedall[0].studyinfo != undefined && $scope.ordersIdListMedall[0].studyinfo != null && $scope.ordersIdListMedall[0].studyinfo.length >0){
												
												angular.forEach($scope.ordersIdListMedall, function (value, key) {
													var data ={};
													data=value;
													var url = 'https://medinfra.medallcorp.in/MedallBRviewcommonAPI/api/register';
													config ={
															  "Access-Control-Allow-Origin":'*',
															  "Access-Control-Allow-Methods":"HEAD, GET, POST, PUT, PATCH, OPTIONS ,DELETE",
															  "Access-Control-Allow-Headers":'Access-Control-*, Origin, X-Requested-With, Content-Type, Accept,Origin, X-Auth-Token',
															  'Allow' :'GET, POST, PUT, DELETE, OPTIONS, HEAD',
															  'accept' :'application/json, text/javascript, *',
															  'content-type' :'application/json',
															  'scheme' :'https',
															  headers: {
															      'Authorization': 'Basic ' + btoa('svh:medallsvh') // Replace with your actual credentials
															    }
													};

														$http.post(url, data, config).then(function (response) {
		
														// This function handles success
		//													alert("success");
															console.log(response);
															if(response.data.status=="success"){
																growl.success("success");
																
																var URI = BASE_URL + ROOT_URL + "/lab/saveMedallRequestResponse";
																var data ={
																		admissionId:$scope.patientDetails.admissionId,
																		patientId:$scope.patientDetails.patientId,
																		requestedPayload:JSON.stringify($scope.ordersIdListMedall),
																		returnedResponse:JSON.stringify(response.data),
																		status:'A',
																		organizationId:$scope.organizationId,
																		unitId:$scope.unitId,
																		addedDate:moment(new Date(),'DD-MM-YYYY HH:mm:ss'),
																		addedBy:$scope.userId,
																		updatedDate:moment(new Date(),'DD-MM-YYYY HH:mm:ss'),
																		updatedBy:$scope.userId
																	}
																	GenericService.serviceAction("POST", URI, data).then(function (response) {
																		if (response.data.status == "success") 
																		{
															            }
																	});
															}else{
																growl.warning(response.data.status);
															}
														}, function (response) {
		
														});
		
													});
												}else{
													growl.warning("Service is not mapped with Medall.");
												}
											}else{
												growl.warning("Service is not mapped with Medall.");
											}
										}
											$scope.patientFullName = "";
											$scope.uhidNum = "";
											$scope.dateOfAdmission = "";
											$scope.patientDischargeDate = "";
											$scope.bedNumber = "";
											$scope.doctorFullName = "";
											$scope.admissionNumber ="";
											$scope.department ="";
											$scope.preBillId = "";
											
											
											if($scope.isUpdateIpdChargesRemark  == true)
											{
												var updateData = 
												{ 
														"admissionId": $scope.admissionDetails.admissionId,
														"remark":$scope.admissionDetails.remark
												};
												
												var URI = BASE_URL + ROOT_URL + "/adt/updateRemarkAdmissionDetailsByAdissionId";
			    								GenericService.serviceAction("POST",URI,updateData).then(function(response){
			    									if (response.data.status == 'success')
			    									{
			    										
			    									} 
			    								});
												
											}
											
											if($scope.finalBill == 'Y' || $scope.isIntermediateBill == 'Y')
											{
												if($scope.groupService=1){
												var data =  {
											  			
													                 "appName": "HIS",
													                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
													                 "reportDesignFilePath": "Ipdbill.rpt",
													                 "reportFormat": "PDF",
													                 "exportFileName": "IPDBillReciept",
													                 "reportParams": [
																	                	 {
																			                  "paramName": "Billid",
																			                  "paramType": "Integer",
																			                  "paramValue": response.data.object
																	                	 },
																	                	 {
																			                  "paramName": "@patient_id",
																			                  "paramType": "Integer",
																			                  "paramValue": $scope.patientDetails.patientId
																		                },
																		                {
																			                  "paramName": "IsSummary",
																			                  "paramType": "String",
																			                  "paramValue": "N"
																		                },
																		                {
																			                  "paramName": "@isDuplicate",
																			                  "paramType": "Integer",
																			                  "paramValue": 0
																			            }
																	                 ]
												};
												}else{
														var data =  {
												  			
														                 "appName": "HIS",
														                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
														                 "reportDesignFilePath": "Ipdbill.rpt",
														                 "reportFormat": "PDF",
														                 "exportFileName": "IPDBillReciept",
														                 "reportParams": [
														                	 {
																                  "paramName": "Billid",
																                  "paramType": "Integer",
																                  "paramValue": response.data.object
															                },
															                {
																                  "paramName": "@patient_id",
																                  "paramType": "Integer",
																                  "paramValue": $scope.patientDetails.patientId
															                },
															                {
																                  "paramName": "IsSummary",
																                  "paramType": "String",
																                  "paramValue": "N"
															                },
															                {
																                  "paramName": "@isDuplicate",
																                  "paramType": "Integer",
																                  "paramValue": 0
																            }
									                 ]
													};
												}
												reportService.generate(REPORT_API_URI1,data);
												$scope.patientDetails = {};
												
												$rootScope.fromWhere = "ipdCharge#"+response.data.object+"#"+$scope.admissionDetails.patientId;
												if($scope.isSplitBillFlag)
												{
												var preAuthReqIdList = [];
												angular.forEach($scope.addNewRequestList,function(value,index)
														{
													preAuthReqIdList.push(value.preauthRequestId);
												});
												var splitBillData = 
												{ 
														"preAuthReqIds":preAuthReqIdList,
														"admissionId": $scope.admissionDetails.admissionId
												};
												
												var URI = BASE_URL + ROOT_URL + "/api/ipd/updateIsSplitStatusByPreAuthorizationId";
												BillingGenericService.serviceAction(METHOD_POST,URI, splitBillData , NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
													if(response.data.status == 'success')
													{
														
													}
												});
												
												
												  $state.go('billList');
												}
											  else
											   {
												  $state.go('billList');
											   }
												
											}
											
											$rootScope.stopLoader();
										}else if (response.data.code==409){
											if(response.data.object==true) //if mismatch in bill found
											{
												getOrderedServices();
												getMedicationIssueListByAdmissionId(self.model.encounterDetails.encounterId);
											}else{
												$scope.patientDetails = {};
												$scope.init();
											}
										}else
											{
											$("#savedisablebtn").addClass("enabled");
											 $scope.patientDetails = {};
											   $scope.init();
											}
										
									});
									
									
							}
						}
						else
							{
							if ($scope.popUpFlag) {
								$scope.showModal = !$scope.showModal;

							}
							else{
								//alert("Direct");
								
								$("#savedisablebtn").addClass("disabled");
								//console.log("finalSaveData",data);
								
								var URI = BASE_URL + ROOT_URL + SAVEBILL;
								$rootScope.startLoader();
							
								BillingGenericService.serviceAction(METHOD_POST,URI, data , NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
									//console.log(response)
									if(response.data.status == 'success')
									{
										$scope.billIdInvoice = response.data.object;
										$scope.getBillForCompanyInvoice();
										if($scope.outsourceLabId == 1){		
											
											$scope.ordersIdListNeuron = [];
										angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
											 if(value.serviceSpecialityId == $scope.labConfig.labSpecialityId)
												{
													var saveNuroPlus = {};
													saveNuroPlus.branchName = "NphMain";
													saveNuroPlus.orderId = value.orderId.toString();
													saveNuroPlus.IPOPNo = $scope.admissionNumber;
													saveNuroPlus.UHID = $scope.uhidNum;
													var orderDate =   (value.orderDate !=null && value.orderDate!=undefined && value.orderDate!='')?value.orderDate:new Date().getTime();
													var orderSchDate = new Date().getTime();
													saveNuroPlus.orderTs =value.orderDateString+"T"+value.orderDateTime;
														//"2022-02-15T13:24:00"; 
													if($scope.visitTypeId == 2){
														var tyy= "IPD";
													}else if($scope.visitTypeId == 3){
														var tyy= "DC";
													}else if($scope.visitTypeId == 4){
														var tyy= "ER";
													}
													saveNuroPlus.ipoptype = tyy;
													saveNuroPlus.salutation =  $scope.patientDetails.genderId.toString();;
													saveNuroPlus.patientName =  $scope.patientDetails.firstName;
													saveNuroPlus.gender = $scope.patientDetails.genderCode;
												     var Birth = moment($scope.patientDetails.birthDate,"DD/MM/YYYY").toDate();
												     var Bx = moment(Birth).format('YYYY-MM-DD');
													saveNuroPlus.birthDate = Bx+"T00:00:00";
													saveNuroPlus.ward ="";
													saveNuroPlus.patMobileNo = $scope.mobileNumber.toString();
													saveNuroPlus.patEmailId = $scope.patientDetails.email;
													if($scope.patientDetails.address != ""){
														saveNuroPlus.patAddress = $scope.patientDetails.address;
													}else{
														saveNuroPlus.patAddress = "";
													}
													saveNuroPlus.patCity = $scope.patientDetails.cityName;
													saveNuroPlus.refDrCode = value.doctorName;
													saveNuroPlus.refDrName = value.doctorName;
													saveNuroPlus.feesPaid = false;
													saveNuroPlus.orderTests=[];
													if($scope.ordersIdListNeuron.length==0){
													$scope.ordersIdListNeuron.push(saveNuroPlus);
												}else{
													isGroupExistsCap=checkGroupIdExistsOrNotCap($scope.ordersIdListNeuron,saveNuroPlus)
													  if (!isGroupExistsCap) {
														$scope.ordersIdListNeuron.push(saveNuroPlus);
													  }
												}
										     }
										});
										
										angular.forEach($scope.ordersIdListNeuron, function (value, key) {
										    angular.forEach($scope.autoRenderedServicesFinalList, function (value1, key1) {
											    if(value.orderId == value1.orderId){
											    	if(value1.serviceSpecialityId == $scope.labConfig.labSpecialityId)
													{
											    		var newAer = {};
											    		newAer.testRecordId = value1.serviceMasterId.toString();
											    		newAer.testName = value1.serviceStandardName;
											    		newAer.testCode = value1.serviceStandardCode;
											    		value.orderTests.push(newAer);
													}
											    }
											});
										});
										console.log("nuronlist",$scope.ordersIdListNeuron);
										angular.forEach($scope.ordersIdListNeuron, function (value, key) {
											var data ={};
											data=value;
											var url = 'https://ngr.dyndns.org/NeuronPlusHosp/Order';
											var config ={
													  "Access-Control-Allow-Origin":'*',
													  "Access-Control-Allow-Methods":"HEAD, GET, POST, PUT, PATCH, OPTIONS ,DELETE",
													  "Access-Control-Allow-Headers":'Access-Control-*, Origin, X-Requested-With, Content-Type, Accept,Origin, X-Auth-Token',
													  'Allow' :'GET, POST, PUT, DELETE, OPTIONS, HEAD',
													  'accept' :'application/json, text/javascript, *',
													  'content-type' :'application/json',
													  'scheme' :'https',
											};

											$http.post(url, data, config).then(function (response) {

											// This function handles success
//												alert("success");
												console.log(response);
												if(response.data.statusCode==200){
													var masse=response.data.message;
													var arr = masse.split(' ');
													var URI = BASE_URL + ROOT_URL + "/api/global/saveNeuronPlusorderedServices";
													var data ={
															"orgId" : $scope.organizationId,
															 "orgUnitId" :$scope.unitId,
															"orderId":arr[3],
					    									"neuronPlusUrl":response.data.reportUrl,
					    									
														}
														GenericService.serviceAction("POST", URI, data).then(function (response) {
															if (response.data.status == "success") 
															{
												            }
														});
												}

											}, function (response) {
//												alert("error");
											// this function handles error

											});

										});
										}
										if($scope.outsourceLabId == 2){	
											
											$scope.ordersIdListMedall= [];
										angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
											 if(value.serviceSpecialityId == $scope.labConfig.labSpecialityId)
												{
												 if($scope.patientDetails.genderId.toString() == 1){
													 $scope.patientDetails.genderId="0"
												 }else if($scope.patientDetails.genderId.toString() == 2){
													 $scope.patientDetails.genderId="1"
												 }else{
													 $scope.patientDetails.genderId="2"
												 }
												 if($scope.medallUnitObj != undefined && $scope.medallUnitObj != null){
													 var centerid = ($scope.medallUnitObj.medallCenterId != undefined && $scope.medallUnitObj.medallCenterId != null ? $scope.medallUnitObj.medallCenterId.toString() : "1478");
												 }else{
													 var centerid = "1478";
												 }
												 
													
												 if($scope.visitTypeId == 2){
														var tyy= "IPD";
													}else if($scope.visitTypeId == 3){
														var tyy= "DC";
													}else if($scope.visitTypeId == 4){
														var tyy= "ER";
													}
												 
												 var Birth = moment($scope.patientDetails.birthDate,"DD/MM/YYYY").toDate();
												 var Bx = moment(Birth).format('YYYY-MM-DD');
												 var medallObj =
												 {
													    "name": $scope.patientDetails.firstName,
													    "mobile":  $scope.mobileNumber.toString(),
													    "gender": $scope.patientDetails.genderId,
													    "pincode":  $scope.patientDetails.zipCode != null ? $scope.patientDetails.zipCode :600020,
													    "address": $scope.patientDetails.address != null ? $scope.patientDetails.address:"",
													    "state": $scope.patientDetails.stateName!= null ? $scope.patientDetails.stateName:"",
													    "district": $scope.patientDetails.districtName != null ? $scope.patientDetails.districtName : "",
													    "passport": null,
													    "srfid": null,
													   // "referalid": null,
													    "relationid": "1",
													    //"salutationid": $scope.patientDetails.prefixId != null ?$scope.patientDetails.prefixId:"",
													    "nationality": "Indian",
													    "dob": Bx+" 00:00:00",
													    "centerid": centerid,
													    "appointmentid": null,
													    "externalid":  $scope.patientDetails.uhIdNumber,
													    "emailid": $scope.patientDetails.email != null ? $scope.patientDetails.email:"",
													    "orderid":value.orderId,
													    "sourceid":"10019471",
													    "hospitaltype":tyy,
													    "studyinfo": []
													}
												 
												 var prefixId = "2513";
												 
													angular.forEach($scope.medallPrifixList, function (val, inx) {
														
														if($scope.patientDetails.prefixId == val.prefixId && val.medallSalutationIdPrefixId != null){
															prefixId = val.medallSalutationIdPrefixId;
														}
														
													});
													
													medallObj.salutationid = prefixId.toString();
													
													if(value.doctorId != undefined && value.doctorId != null && value.doctorId != 0 ){
														
														var check = true;
														angular.forEach($scope.medallDoctorList, function (val, inx) {
															
															if(value.doctorId == val.doctorId && val.medallReferalId != null){
																medallObj.referalid = val.medallReferalId;
																check = false;
															}
															
														});
														
														if(check == true){
															medallObj.referalid = '5001195';
														}
													}else if( $scope.admissionDetails.referralId != undefined &&  $scope.admissionDetails.referralId != null &&  $scope.admissionDetails.referralId != 0 ){
														
														var check = true;
														angular.forEach($scope.referralMasterList, function (val, inx) {
															
															if( $scope.admissionDetails.referralId == val.referralId && val.medallReferralId != null){
																medallObj.referalid = val.medallReferralId;
																check = false;
															}
															
														});
														
														if(check == true){
															medallObj.referalid = '5001195';
														}
													}else{
														medallObj.referalid = '5001195';
													}
													
												 	if($scope.ordersIdListMedall.length==0){
													 $scope.ordersIdListMedall.push(medallObj);
													}
										     }
										});
										$scope.checkAutoRenderedServicesForMedall = [];
										
										angular.forEach($scope.ordersIdListMedall, function (value, key) {
										    angular.forEach($scope.autoRenderedServicesFinalList, function (value1, key1) {
											    if(value.orderid == value1.orderId){
											    	if(value1.serviceSpecialityId == $scope.labConfig.labSpecialityId)
													{
												    	$scope.checkAutoRenderedServicesForMedall.push(value1);
													}
											    }
											});
										});
										
										angular.forEach($scope.ordersIdListMedall, function (value, key) {
										    angular.forEach($scope.checkAutoRenderedServicesForMedall, function (value1, key1) {
											    if(value.orderid == value1.orderId){
											    	if(value1.serviceSpecialityId == $scope.labConfig.labSpecialityId)
													{
											    		if(value1.medallServiceId != undefined && value1.medallServiceId != null && value1.medallServiceId != ""){
												    			var newAer = {};
													    		newAer.billingamount="0";
													    		newAer.billingid="0";
													    		newAer.billingtype="";
													    		newAer.corpbillingid=(value1.medallServiceId).toString();
													    		newAer.discount="0";
													    		newAer.paidamount="0";
													    		newAer.topaid="0";
													    		newAer.extraamount="0";
													    		value.studyinfo.push(newAer);
												    		}
														}
											    }
											});
										});
										if($scope.ordersIdListMedall != null && $scope.ordersIdListMedall.length >0 ){
											if( $scope.ordersIdListMedall[0].studyinfo != undefined && $scope.ordersIdListMedall[0].studyinfo != null && $scope.ordersIdListMedall[0].studyinfo.length >0){
														angular.forEach($scope.ordersIdListMedall, function (value, key) {
														var data ={};
														data=value;
														var url = 'https://medinfra.medallcorp.in/MedallBRviewcommonAPI/api/register'
														config ={
																  "Access-Control-Allow-Origin":'*',
																  "Access-Control-Allow-Methods":"HEAD, GET, POST, PUT, PATCH, OPTIONS ,DELETE",
																  "Access-Control-Allow-Headers":'Access-Control-*, Origin, X-Requested-With, Content-Type, Accept,Origin, X-Auth-Token',
																  'Allow' :'GET, POST, PUT, DELETE, OPTIONS, HEAD',
																  'accept' :'application/json, text/javascript, *',
																  'content-type' :'application/json',
																  'scheme' :'https',
																  headers: {
																      'Authorization': 'Basic ' + btoa('svh:medallsvh') // Replace with your actual credentials
																    }
														};
			
														$http.post(url, data, config).then(function (response) {
			
														// This function handles success
			//												alert("success");
															console.log(response);
															if(response.data.status=="success"){
																growl.success("success");
																
																var URI = BASE_URL + ROOT_URL + "/lab/saveMedallRequestResponse";
																var data ={
																		admissionId:$scope.patientDetails.admissionId,
																		patientId:$scope.patientDetails.patientId,
																		requestedPayload:JSON.stringify($scope.ordersIdListMedall),
																		returnedResponse:JSON.stringify(response.data),
																		status:'A',
																		organizationId:$scope.organizationId,
																		unitId:$scope.unitId,
																		addedDate:moment(new Date(),'DD-MM-YYYY HH:mm:ss'),
																		addedBy:$scope.userId,
																		updatedDate:moment(new Date(),'DD-MM-YYYY HH:mm:ss'),
																		updatedBy:$scope.userId
																	}
																	GenericService.serviceAction("POST", URI, data).then(function (response) {
																		if (response.data.status == "success") 
																		{
															            }
																	});
															}else{
																growl.warning(response.data.status);
															}
														}, function (response) {
			
														});
			
													});
											}else{
												growl.warning("Service is not mapped with Medall.");
											}
										}else{
											growl.warning("Service is not mapped with Medall.");
										}
									}
										$scope.patientFullName = "";
										$scope.uhidNum = "";
										$scope.dateOfAdmission = "";
										$scope.patientDischargeDate = "";
										$scope.bedNumber = "";
										$scope.doctorFullName = "";
										$scope.admissionNumber = "";
										$scope.department = "";
										$scope.preBillId = "";
								
										if($scope.isUpdateIpdChargesRemark  == true)
										{
											var updateData = 
											{ 
													"admissionId": $scope.admissionDetails.admissionId,
													"remark":$scope.admissionDetails.remark
											};
											
											var URI = BASE_URL + ROOT_URL + "/adt/updateRemarkAdmissionDetailsByAdissionId";
		    								GenericService.serviceAction("POST",URI,updateData).then(function(response){
		    									if (response.data.status == 'success')
		    									{
		    										
		    									} 
		    								});
											
										}
										
										if($scope.finalBill == 'Y' || $scope.isIntermediateBill == 'Y')
											{
											$rootScope.fromWhere = "ipdCharge#"+response.data.object+"#"+$scope.admissionDetails.patientId;
											
											var data =  {
										  			
									                 "appName": "HIS",
									                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
									                 "reportDesignFilePath": "Ipdbill.rpt",
									                 "reportFormat": "PDF",
									                 "exportFileName": "IPDBillReciept",
									                 "reportParams": [
									                	 {
											                  "paramName": "Billid",
											                  "paramType": "Integer",
											                  "paramValue": response.data.object
										                },
										                {
											                  "paramName": "@patient_id",
											                  "paramType": "Integer",
											                  "paramValue": $scope.patientDetails.patientId
										                },
										                {
											                  "paramName": "IsSummary",
											                  "paramType": "String",
											                  "paramValue": "N"
										                },
										                {
											                  "paramName": "@isDuplicate",
											                  "paramType": "Integer",
											                  "paramValue": 0
											            }
								                 ]
												};
											reportService.generate(REPORT_API_URI1,data);
											
											if($scope.packaged.withPackageDetail == 'Y'){
												var data5 =  {
											  			
										                 "appName": "HIS",
										                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
										                 "reportDesignFilePath": "Ipdbill.rpt",
										                 "reportFormat": "PDF",
										                 "exportFileName": "IPDBillReciept",
										                 "reportParams": [
										                	 {
												                  "paramName": "Billid",
												                  "paramType": "Integer",
												                  "paramValue": response.data.object
											                },
											                {
												                  "paramName": "@patient_id",
												                  "paramType": "Integer",
												                  "paramValue": $scope.patientDetails.patientId
											                },
											                {
												                  "paramName": "IsSummary",
												                  "paramType": "String",
												                  "paramValue": "N"
											                },
											                {
												                  "paramName": "@isDuplicate",
												                  "paramType": "Integer",
												                  "paramValue": 0
												            }
									                 ]
													
												};
												 reportService.generate(REPORT_API_URI1,data5);
											}
											
											if($scope.packaged.withRatePackageDetail == 'Y'){
												var data6 =  {
											  			
										                 "appName": "HIS",
										                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
										                 "reportDesignFilePath": "Ipdbill_PackageRate.rpt",
										                 "reportFormat": "PDF",
										                 "exportFileName": "IPDBillReciept",
										                 "reportParams": [
										                	 {
												                  "paramName": "Billid",
												                  "paramType": "Integer",
												                  "paramValue": response.data.object
											                },
											                {
												                  "paramName": "@patient_id",
												                  "paramType": "Integer",
												                  "paramValue": $scope.patientDetails.patientId
											                },
											                {
												                  "paramName": "IsSummary",
												                  "paramType": "String",
												                  "paramValue": "N"
											                },
											                {
												                  "paramName": "@isDuplicate",
												                  "paramType": "Integer",
												                  "paramValue": 0
												            }
									                 ]
													};
												 reportService.generate(REPORT_API_URI1,data6);
											}
											
											if($scope.isSplitBillFlag)
											{
											 var preAuthReqIdList = [];
											 angular.forEach($scope.addNewRequestList,function(value,index)
													{
												preAuthReqIdList.push(value.preauthRequestId);
											});
											 var splitBillData = 
											 { 
													 "preAuthReqIds":preAuthReqIdList,
													 "admissionId": $scope.admissionDetails.admissionId
											 };
											
											
											 var URI = BASE_URL + ROOT_URL + "/api/ipd/updateIsSplitStatusByPreAuthorizationId";
											 BillingGenericService.serviceAction(METHOD_POST,URI, splitBillData , NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
												if(response.data.status == 'success')
												 {
													
												 }
											  });
											   $state.go('billList');
											 }
										     else
										       {
											     $state.go('billList');
										       }
											$scope.patientDetails = {};
											}
										else{
											if($scope.isSplitBillFlag)
											{
											var preAuthReqIdList = [];
											angular.forEach($scope.addNewRequestList,function(value,index)
													{
												preAuthReqIdList.push(value.preauthRequestId);
											});
											var splitBillData = 
											{  "preAuthReqIds":preAuthReqIdList,
											   "admissionId": $scope.admissionDetails.admissionId
											 };
											
											var URI = BASE_URL + ROOT_URL + "/api/ipd/updateIsSplitStatusByPreAuthorizationId";
											BillingGenericService.serviceAction(METHOD_POST,URI, splitBillData , NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
												if(response.data.status == 'success')
												{
													$scope.patientDetails ={};
												}
											});
										 }
											
											$scope.init();
										    }
											
										$rootScope.stopLoader();
										
									}else if (response.data.code==409){
										if(response.data.object==true) //if mismatch in bill found
										{
											getOrderedServices();
											getMedicationIssueListByAdmissionId(self.model.encounterDetails.encounterId);
										}else{
											$scope.patientDetails = {};
											$scope.init();
										}
									}
									else
										{
										   $("#savedisablebtn").addClass("enabled");
										   $scope.patientDetails = {};
										   $scope.init();
										   
										}
									
									
								});
								
							}
								
							}
				}
				}
				
				
				$scope.medallPrifixList =[];
				$scope.medallUnitObj ={};
				$scope.medallDoctorList =[];
				$scope.getMedalDdetails = function(){
					var data = {
							  "organizationId" : $scope.organizationId,
							  "unitId" : $scope.unitId,
					}
					var URI = BASE_URL + ROOT_URL + "/adt/getPrefixMasterAndMedallPrefixMaster";
					BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success")
						{
							$scope.medallPrifixList =response.data.listObject;
							$scope.medallUnitObj =response.data.object;
							$scope.medallDoctorList =response.data.resObject;
							
						}
					});
				}
				$scope.getMedalDdetails();
				
				$scope.roundOffSaved = 0;
				var getInterimBillsByAdmissionId = function(admissionId){
					var URI = BASE_URL + ROOT_URL + "/api/billing/intermediate/admission/"+admissionId;
					BillingGenericService.serviceAction(METHOD_GET,URI, null , NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						if(response.data.status == 'success'){
							self.model.interimBills = response.data.listObject;
							if(response.data.listObject.length > 0){
								$scope.roundOffSaved =response.data.listObject[0].roundOff;
							}else{
								$scope.roundOffSaved =0;
							}
						}
					});
				}
				
				
				$scope.getBillListDetails = function(billId)
				{
					var URI = BASE_URL + ROOT_URL + "/api/billing/payee/bill/"+billId;
					BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success"){
							//$scope.payeeList = response.data.listObject;
							$scope.billServicesList = response.data.object.listObject;
							/*$scope.paymentReceiptsDetailsList=response.data.object.object;
							var totalAmtQuaBase = parseFloat(0);
							var totalAmtConcession = parseFloat(0);
							var totalAmtDiscount = parseFloat(0);
							var totalAmtGst = parseFloat(0);
							var totalNetPayableAmt = parseFloat(0);
							angular.forEach($scope.billServicesList,function(value,key){
								totalAmtQuaBase = totalAmtQuaBase + parseFloat(value.grossAmount);
								totalAmtConcession = totalAmtConcession + parseFloat(value.concession);
								totalAmtDiscount = totalAmtDiscount + parseFloat(value.discount);
								totalAmtGst = totalAmtGst + parseFloat(value.taxAmount);
								totalNetPayableAmt = totalNetPayableAmt + parseFloat(value.netAmt);
							});
							
							$scope.totalAmtQuaBase =totalAmtQuaBase;
							$scope.totalAmtConcession = totalAmtConcession;
							$scope.totalAmtDiscount =totalAmtDiscount;
							$scope.totalAmtGst = (totalAmtGst).toFixed(2);
							$scope.totalNetPayableAmt = totalNetPayableAmt;
							
							angular.forEach($scope.payeeList,function(value1,key)
							{
								if(value1.payeeId == '1')
									{
									$scope.payeeDueAmtFinal = value1.dueAmount;
									$scope.payeeDueAmtStatic = value1.dueAmount;
									}
							});*/
							
							//vivek added
						}
					});
				}
			
					$scope.printBill = function(bill)
					{
						
							var data =  {
						  			
					                 "appName": "HIS",
					                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
					                 "reportDesignFilePath": "Ipdbill_Cancelled.rpt",
					                 "reportFormat": "PDF",
					                 "exportFileName": "IPDBillReciept",
					                 "reportParams": [
					                	 {
							                  "paramName": "Billid",
							                  "paramType": "Integer",
							                  "paramValue": bill.billingMasterId
						                },
						                {
							                  "paramName": "@patient_id",
							                  "paramType": "Integer",
							                  "paramValue": $scope.patientDetails.patientId
						                },
						                {
							                  "paramName": "IsSummary",
							                  "paramType": "String",
							                  "paramValue": "N"
						                },
						                {
							                  "paramName": "@isDuplicate",
							                  "paramType": "Integer",
							                  "paramValue": 0
							            }
			                ]
							};
							if(bill.billCancelled=='N')
								{
								data.reportDesignFilePath="Ipdbill.rpt";
								}
						
						reportService.generate(REPORT_API_URI1,data);
					
					}
				
					   $scope.getCancelDiscountReq = function()
						{
							
						    var indexList =[];
							$rootScope.startLoaderPopup();
							var data = {
									reqDiscId :$scope.requestStatusObject.reqDiscId,
							}
							
							var URI = BASE_URL + ROOT_URL + "/api/billing/cancelRequest";
							
							GenericService
							.serviceAction("POST", URI, data)
							.then(function(response) {
										$rootScope.stopLoaderPopup();
										if (response.data.status == "success") {
											
											 var i =0;
									    	   angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
									   			if(value.status == 'A'){
									   				
									   				if(parseFloat(value.discount) != 0){
									   					
											   				$scope.autoRenderedServicesFinalList[i].concession = parseFloat(0);
								   						    $scope.autoRenderedServicesFinalList[i].patientClassPercentage = parseFloat(0);
								   						    
									    			        var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
									    			        $scope.autoRenderedServicesFinalList[i].discountPercentage = 0;
									    			        $scope.autoRenderedServicesFinalList[i].discount = parseFloat(0).toFixed(2);
									    			        var withConDic =parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[i].discount);
									    			        
									    			    	var txpre = 0;
															if(value.taxPercentage != null || value.taxPercentage > 0){
																$scope.autoRenderedServicesFinalList[i].taxPercentage = value.taxPercentage;
																txpre = value.taxPercentage;
															}else{
																	$scope.autoRenderedServicesFinalList[i].taxPercentage = 0;
																	}
															$scope.autoRenderedServicesFinalList[i].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
															$scope.autoRenderedServicesFinalList[i].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[i].taxAmt)).toFixed(2);
															$scope.autoRenderedServicesFinalList[i].selfPayable = $scope.autoRenderedServicesFinalList[i].netPay;
															$scope.autoRenderedServicesFinalList[i].creditPayable =parseFloat(0);
															$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[i]);	
												    		indexList.push(i);
													
									   					}
									   				}
									   			if(i+1 == $scope.autoRenderedServicesFinalList.length && indexList.length>0){
									   				/*$scope.saveOrderPackage();*/
									   				$scope.addOrUpdateServices(null,indexList);
									   			}
									    	   		
									   			i++;
									   			});
									    	   
									    	   
											growl.success("Cancelled !!!!",{
												title : 'success'
											});
											
											$scope.requestApporivedDiscountDiv = false;
											$scope.discountDisabledFlagForRequest = false;
											$scope.requestAppUser = 0;
											$scope.approvedAmtApp = 0;
											$scope.requestAmtApp =0;
											$scope.discTypeDesc = "";
											$scope.statusApp = "";
											$scope.discountRadio = 2; 
											
											$scope.billWiseDiscountObj = {
													// "discountGivenBy":"",
													"discountGivenBy":$scope.userId,
													"discountCategoryId" : "",
													"discountAmountBillWise" : "",
													"discountPercentageBillWise" : "",
													"approvedAmountBillWise":"",
													"requestUserId":"",
													"requestAmountBillWise" : 0,
											}
											 $scope.requestStatusObject  = null;
								    		angular.element('#discountCancellationModalReq').modal('hide');
										}
									});
							
						}
				/*$scope.getSpecialityByDoctor = function(docId){
					var specDocObj = {unitId:$scope.unitId,organizationId:$scope.organizationId,doctorId:docId};
				    var URI = BASE_URL + ROOT_URL + GETSPECIALITYLISTBYDOCTORID;
					BillingGenericService.serviceAction(METHOD_POST, URI,specDocObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										//console.log(response);
										// $rootScope.startLoader();
										if (response.data.status == "success"){
												//$scope.docSpecialityList = response.data.listObject;
											$scope.servicesListPopup[idx].docSpecialityList = response.data.listObject;
										}
									});
					}*/
				$scope.getSpecialityByDoctor = function(docId,idx)
				{
					 var specDocObj = {
				    			unitId:$scope.unitId,
				    			organizationId:$scope.organizationId,
				    			doctorId:docId
				    }
					 var specialityLsit=[];
						angular.forEach($scope.getInitiallySpecialityList, function (service, key) 
								{
									if(service.doctorId==docId)
										{
										specialityLsit.push(service);
										}
								});
		                if(specialityLsit.length == 0){
		                	$scope.servicesListPopup[idx].doctorId ="";
		                       growl.warning("Selected Doctor Department and Sub Department not mapped in the unit masters !! ",{
		                            title : "WARNING"
		                        });
		                        return false; 
		                }
		                
					 $scope.servicesListPopup[idx].docSpecialityList = specialityLsit;
						
						$scope.docSplObj = $.grep($scope.doctorsList, function (docList) {
				            return docList.doctorId == docId;
				           })[0];
						
						
						 if($scope.servicesListPopup[idx].doctorSpecialityId==undefined || $scope.servicesListPopup[idx].doctorSpecialityId==null || $scope.servicesListPopup[idx].doctorSpecialityId=="" || $scope.servicesListPopup[idx].specialityId=="")
			                {
						if(specialityLsit.length == 1){
							$scope.servicesListPopup[idx].doctorSpecialityId =specialityLsit[0].specialityId;
						}else{
							$scope.servicesListPopup[idx].doctorSpecialityId =specialityLsit[0].specialityId;
						}
			                }

			}
				// Clear Doctor Selection
				$scope.clearDoctor = function(item,$index){
					// console.log(item);
					// return false;
					item=$scope.autoRenderedServicesFinalList[$index];
					var data = {
					    "visitTypeId" : 1,
					    "billingBedCategoryId" : $scope.opdBillingCatId,
					    "patientTypeId" : $scope.patientDetails.patientTypeId,
					    "paymentEntitlementTypeId" :  $scope.encounterDetails.paymentEntitlementTypeId,
					    "referralId" : $scope.encounterDetails.referralId,
					    listOrderDetailsMasterDto : []
					};
					var newArr = {
							ordDiscount:item.discount,
							ordConcession : item.concession,
							ordDocId : null,
							ordDocSplId : null,
							encounterId : $scope.encounterDetails.encounterId,
							orderDetailsId : item.orderDetailsId,
							orgId : $scope.organizationId,
							orgUnitId : $scope.unitId,
							serviceId : item.serviceMasterId,
							departmentId : item.serviceSpecialityId,
							ordTotalAmt : item.ordTotalAmount,
							tariffId : item.tariffId,
							isDocReq : item.isDoctorRequired,
							isRefDoctorShare:item.isRefDoctorShare ,
							discountOwnership: parseInt(item.discountOwnership),
							orderDate : item.orderDate,
							
					};
					var orderDetailsList = [];
					orderDetailsList.push(newArr);
					data.listOrderDetailsMasterDto = orderDetailsList;
					
					
					var URI = BASE_URL + ROOT_URL + "/api/billing/order/docShare/clearDoctor";
			    	if(item.doctorId>0)
			    		{
			    		$rootScope.startLoader();
			    	BillingGenericService.serviceAction(METHOD_POST,URI,data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			    		if(response.data.status == "success"){
			    			item.doctorSpecialityId=null;
			    			item.doctorId=null;
			    			 $scope.calculateShareAmount(item,$index);
			    			growl.success("Doctor cancelled successfully");
			    		}else{
			    			$rootScope.stopLoader();
			    		}
			    	});
			    		}
				}
			    $scope.clearDoctorBtn= function(item,$index){
			     
			        	if(item.doctorId>0)
			        		{
			        
			        			item.doctorSpecialityId=null;
			        			item.doctorId=null;
			        			growl.success("Doctor cancelled successfully");
			
			
			        		}
			    }
				$scope.clearDoctorService=function(item,$index){
					if(item.doctorId>0)
	        		{
	        
	        			item.doctorSpecialityId=null;
	        			item.doctorId=null;
	        			growl.success("Doctor cancelled successfully");
	
	
	        		}
				}




				// Share Development start
			    $scope.calculateShareAmount = function(item,$index){
			        //console.log(item);
			        //return false;
			        var data = {
			            "visitTypeId" : $scope.visitTypeId,
			            "billingBedCategoryId" : $scope.admissionDetails.bedBillingCategoryId,
			            "patientTypeId" : $scope.admissionDetails.patientCategoryId,
			            "paymentEntitlementTypeId" :  $scope.admissionDetails.paymentEntitlementId,
			            "referralId" : $scope.admissionDetails.referralId,
			            listOrderDetailsMasterDto : []
			        };
			        var newArr = {
			                ordDiscount:item.discount,
			                ordConcession : item.concession,
			                ordDocId : item.doctorId,
			                ordDocSplId : item.doctorSpecialityId,
			                admissionId : $scope.admissionDetails.admissionId,
			                orderDetailsId : item.orderDetailsId,
			                orgId : $scope.organizationId,
			                orgUnitId : $scope.unitId,
			                serviceId : item.serviceMasterId,
			                departmentId : item.serviceSpecialityId,
			                ordTotalAmt : item.totalAmt,
			                tariffId : item.tariffId,
			                isDocReq : item.isDoctorRequired,
			                isRefDoctorShare:item.isRefDoctorShare ,
			                discountOwnership: parseInt(item.discountOwnership),
			                orderDate : item.orderDate,
			                
			        };
			        var orderDetailsList = [];
			        orderDetailsList.push(newArr);
			        data.listOrderDetailsMasterDto = orderDetailsList;
			        
			        
			        $rootScope.startLoader();
			        var URI = BASE_URL + ROOT_URL + "/api/billing/order/docShare";
			        
			        BillingGenericService.serviceAction(METHOD_POST,URI,data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			            if(response.data.status == "success"){
			                $scope.autoRenderedServicesFinalList[$index].docSharePer = response.data.object.listOrderDetailsMasterDto[0].docSharePer !=null ?response.data.object.listOrderDetailsMasterDto[0].docSharePer: 0;
			                $scope.autoRenderedServicesFinalList[$index].hospitalShare = response.data.object.listOrderDetailsMasterDto[0].hospitalShare  !=null ?response.data.object.listOrderDetailsMasterDto[0].hospitalShare:0;
			                $scope.autoRenderedServicesFinalList[$index].hospitalShareNew=$scope.autoRenderedServicesFinalList[$index].hospitalShare;
			                $scope.autoRenderedServicesFinalList[$index].refDocSharePer = response.data.object.listOrderDetailsMasterDto[0].refDocSharePer  !=null ? response.data.object.listOrderDetailsMasterDto[0].refDocSharePer : 0;
			                $scope.autoRenderedServicesFinalList[$index].discountOwnership = (response.data.object.listOrderDetailsMasterDto[0].discountOwnership>0 && ($scope.autoRenderedServicesFinalList[$index].concession != 0 || $scope.autoRenderedServicesFinalList[$index].discount != 0)) > 0 ? response.data.object.listOrderDetailsMasterDto[0].discountOwnership : $scope.defaultDiscountOwnership;
			                //$rootScope.stopLoader();
			            }else{
			                $rootScope.stopLoader();
			            }
			        });
			    }
			    
			// SHare Development end

			    $scope.getInitiallySpecialityList=[];
				$scope.getSpecialityByDoctorMain = function()
				{
//					if(docId > 0){
				var specDocObj = {
				    			unitId:$scope.unitId,
				    			organizationId:$scope.organizationId,
//				    			doctorId:docId
				    }
				    var URI = BASE_URL + ROOT_URL
					+ GETSPECIALITYLISTBYDOCTORID;
			BillingGenericService.serviceAction(METHOD_POST, URI,specDocObj,
							NOTIFICATION_MSG_STATUS_FALSE)
					.then(function(response) {
								//console.log(response);
								
								// $rootScope.startLoader();
								if (response.data.status == "success")
									{
									$scope.getInitiallySpecialityList=response.data.listObject;

									}
							});
//				}
				
			 }
				$scope.getSpecialityByDoctorMainChange = function(docId,idx)
				{
					$scope.autoRenderedServicesFinalList[idx].doctorSpecialityId = null;
				var specDocObj = {
				    			unitId:$scope.unitId,
				    			organizationId:$scope.organizationId,
				    			doctorId:docId
				    }
				var specialityLsit=[];
				angular.forEach($scope.getInitiallySpecialityList, function (service, key) 
						{
							if(service.doctorId==docId)
								{
								specialityLsit.push(service);
								}
						});
				
				if(specialityLsit.length == 0){
					$scope.autoRenderedServicesFinalList[idx].doctorId ="";
					   growl.warning("Selected Doctor Department and Subdepartment not mapped in the unit masters !! ",{
	                        title : "WARNING"
	                    });
	                    return false; 
				}
				
				
				$scope.autoRenderedServicesFinalList[idx].doctorSpecialityList=[];
				// $scope.docSpecialityList =
				// response.data.listObject;autoRenderedServicesFinalList
				
				$scope.autoRenderedServicesFinalList[idx].doctorSpecialityList = specialityLsit;
				$scope.autoRenderedServicesFinalList[idx].doctorSpecialityId = specialityLsit[0].specialityId;
				$scope.autoRenderedServicesFinalList[idx].specialityName = specialityLsit[0].specialityName;
				$scope.calculateShareAmount($scope.autoRenderedServicesFinalList[idx],idx)

			 }
				
				
				$scope.cancelPatientClassPopup = function () {
                    $rootScope.startLoaderPopup(); // Start loader

                    $scope.patientClassIdPopup = null;
                    $scope.patientClassCancelReasonIdPopup = null;
                    $scope.admissionDetails.patientClassId = null;

                    var obj = {
                        visitId: $scope.admissionDetails.admissionId,
                        visitTypeId: $scope.visitTypeId,
                        reasonIdForChangePatientClass: $scope.patientClassCancelReasonIdPopup,
                        organizationId: $scope.organizationId,
                        unitId: $scope.unitId,
                        patientClassId: null
                    };

                    var URI = BASE_URL + ROOT_URL + "/api/billing/changePatientClassId";

                    BillingGenericService.serviceAction(METHOD_POST, URI, obj, NOTIFICATION_MSG_STATUS_FALSE)
                        .then(function (response) {
                            if (response.data.status === "success") {
                                $scope.admissionDetails.patientClassId = null;
                                $scope.needToUpdatePharmacyService = true;

                                angular.element('#change_patient_class').modal('hide');
                                angular.element('#confirmCancelPatientClass').modal('hide');

                                var i = 1;
                                angular.forEach($scope.autoRenderedServicesFinalList, function (service, key) {
                                    $scope.autoRenderedServicesFinalList[key].concession = parseFloat(0);
                                    $scope.autoRenderedServicesFinalList[key].patientClassPercentage = parseFloat(0);
                                    $scope.autoRenderedServicesFinalList[key].discount = service.discount;

                                    var totalAmt = (parseFloat(service.quantity) * parseFloat(service.basePrice)).toFixed(2);
                                    $scope.autoRenderedServicesFinalList[key].discountPercentage = service.discountPercentage;
                                    var withConDic = parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[key].concession) + parseFloat($scope.autoRenderedServicesFinalList[key].discount));

                                    var txpre = service.taxPercentage ? service.taxPercentage : 0;
                                    $scope.autoRenderedServicesFinalList[key].taxPercentage = txpre;
                                    $scope.autoRenderedServicesFinalList[key].taxAmt = parseFloat((parseFloat(withConDic) * txpre) / 100).toFixed(2);
                                    $scope.autoRenderedServicesFinalList[key].netPay = (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[key].taxAmt)).toFixed(2);
                                    $scope.autoRenderedServicesFinalList[key].selfPayable = $scope.autoRenderedServicesFinalList[key].netPay;
                                    $scope.autoRenderedServicesFinalList[key].creditPayable = parseFloat(0);

                                    if (i === $scope.autoRenderedServicesFinalList.length) {
                                        if ($scope.requestStatusObject == null) {
                                            $scope.saveOrderPackage();
                                            $scope.requestDiscountDiv = false;
                                            $scope.requestApporivedDiscountDiv = false;
                                            $scope.discountDisabledFlagForRequest = false;
                                            $scope.requestAppUser = 0;
                                            $scope.approvedAmtApp = 0;
                                            $scope.requestAmtApp = 0;
                                            $scope.discTypeDesc = "";
                                            $scope.statusApp = "";
                                            $scope.discountRadio = 2;

                                            $scope.billWiseDiscountObj = {
                                                "discountGivenBy": $scope.userId,
                                                "discountCategoryId": "",
                                                "discountAmountBillWise": "",
                                                "discountPercentageBillWise": "",
                                                "approvedAmountBillWise": "",
                                                "requestUserId": "",
                                                "requestAmountBillWise": 0,
                                            };
                                            $scope.requestStatusObject = null;
                                        } else {
                                            $scope.getCancelDiscountReqForDiscount();
                                            $scope.saveOrderPackage();
                                        }
                                    }
                                    i++;
                                });

                                $rootScope.stopLoaderPopup(); // Stop loader only after successful operation
                            } else {
                                angular.element('#change_patient_class').modal('hide');
                                angular.element('#confirmCancelPatientClass').modal('hide');
                                $rootScope.stopLoaderPopup(); // Stop loader if API call fails
                            }
                        })
                        .catch(function (error) {
                            angular.element('#change_patient_class').modal('hide');
                            angular.element('#confirmCancelPatientClass').modal('hide');
                            $rootScope.stopLoaderPopup(); // Stop loader in case of network error
                        });
                };

				
				$scope.cancelPatientClassClosePopup = function(){
					angular.element('#confirmCancelPatientClass').modal('hide');
				}
				
				//Ganesh
				$scope.getSpecialityByDoctorService = function(docId,idx)
				{
				var specDocObj = {
				    			unitId:$scope.unitId,
				    			organizationId:$scope.organizationId,
				    			doctorId:docId
				    }
				var specialityLsit=[];
				angular.forEach($scope.getInitiallySpecialityList, function (service, key) 
						{
							if(service.doctorId==docId)
								{
								specialityLsit.push(service);
								}
						});
				
                if(specialityLsit.length == 0){
                    $scope.autoRenderedServicesFinalList[idx].doctorId ="";
                       growl.warning("Selected Doctor Department and Sub Department not mapped in the unit masters !! ",{
                            title : "WARNING"
                        });
                        return false; 
                }
                
				$scope.autoRenderedServicesFinalList[idx].docSpecialityList = specialityLsit;
				$scope.autoRenderedServicesFinalList[idx].doctorSpecialityList = specialityLsit;
				$scope.docSplObj = $.grep($scope.doctorsList, function (docList) {
		            return docList.doctorId == docId;
		           })[0];
                if($scope.autoRenderedServicesFinalList[idx].doctorSpecialityId==undefined || $scope.autoRenderedServicesFinalList[idx].doctorSpecialityId==null || $scope.autoRenderedServicesFinalList[idx].doctorSpecialityId==0 )
                /*	||$scope.autoRenderedServicesFinalList[idx].docSpecialityList[0].specialityId!=$scope.autoRenderedServicesFinalList[idx].doctorSpecialityId*/
                {
					if(specialityLsit.length == 1){
						$scope.autoRenderedServicesFinalList[idx].doctorSpecialityId =specialityLsit[0].specialityId;
					}else{
						$scope.autoRenderedServicesFinalList[idx].doctorSpecialityId =specialityLsit[0].specialityId;
					}
                }
                else if(specialityLsit.length==1 && $scope.autoRenderedServicesFinalList[idx].doctorId>0)
                	{
                	$scope.autoRenderedServicesFinalList[idx].doctorSpecialityId =specialityLsit[0].specialityId;
                	}
					$scope.calculateShareAmount($scope.autoRenderedServicesFinalList[idx],idx);
				

			 }	
				
				
				$scope.init();
				
				/***=========================Payer tab=========================***/
				

			    $scope.corEmpVerificationFlag=false;
			 
			    
			    $scope.initCoEmp=function(){
			        $scope.corEmpObj={
			                typeOfCoEmp:'1',
//			                employeeNumber:"",
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
			    $rootScope.getDefaultRegistrationValidationDetails();
			    
			    
			    $scope.clearCoEmpDetails=function(){
			       
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
			    $scope.empNoShowHide=true; // for empNo text field hide show
			    $scope.noteShowHide=false; // for note text area hide show
			    $scope.dependTblShowHode=false; // for table hide show
			    $scope.showHidevalidate=true; //for validate buttton     
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
			        //for staff or employee 
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
//			                    genderId:$scope.patientDetails.genderId,
//			                    birthDate:$scope.patientDetails.birthDate
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
			                         //console.log("$scope.corEmplist",$scope.corEmpObj);
//			                         $scope.initCoEmp();
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
//			                            angular.element('#validationEmployeeModel').modal('hide');
			                        }else{
			                              $scope.color="red";
			                              $scope.showNameDetails=true;
			                              $scope.name="record not found.."
			                              $scope.showHideAcceptRejectBtn=false;
			                              $scope.showHideOkBtn=true;
			                        }
			                        
			                        
			                    }
//			                    $scope.initCoEmp();
			                });
			        }
			        
			        $scope.getEmpDependantDetailsByIndex=function(index){
			            $scope.showNameDetails=false;
			                 if($scope.patientDetails.firstName==$scope.corEmpDependantList[index].firstName) {
//			                     $scope.name=$scope.corEmpDependantList[index].firstName+" "+$scope.corEmpDependantList[index].middleName+" "+$scope.corEmpDependantList[index].lastName;
			                     if($scope.patientDetails.genderId==$scope.corEmpDependantList[index].genderId){
			                               
//			                                $scope.color="green";
			                                $scope.enableDisabledValidated=false;                                        
			                                $scope.showHideAcceptRejectBtn=true;
			                            
			                     }else{
//			                            $scope.color="red";
			                            $scope.enableDisabledValidated=false;                                        
			                            $scope.showHideAcceptRejectBtn=true;
			                        }
			                 }else{
//			                       $scope.color="red";
			                        $scope.enableDisabledValidated=false;                                        
			                        $scope.showHideAcceptRejectBtn=true;
			                    }
			                 $scope.showNameDetails=true;
//			                 $scope.initCoEmp();
			            
			        }
			        
			        
			    }
			    
			    $scope.closePopupValidate = function()
			    {
			        angular.element('#validationEmployeeModel').modal('hide');
			    }
			    
			    $scope.saveMannualDetails=function(){
			        if($scope.empObj.employeeNumber==undefined || $scope.empObj.employeeNumber=="" || $scope.empObj.employeeNumber==null)
			        {
			            growl.warning("Please enter employee number.",{
			                title : WARNING_MSG
			            });
			            return;
			        
			        }
			    /*else 
			        if($scope.corEmpObj.note==undefined || $scope.corEmpObj.note=="" || $scope.corEmpObj.note==null){
			            growl.warning("Please enter note",{
			                title : WARNING_MSG
			            });
			            return;
			        }*/
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
			    $scope.verifyCorEmp=function(type){
			        if(type=='A'){
			            $scope.enableDisabledApply=true;    
			            $scope.enableDisabledValidated=false;
			        }else{
			            $scope.enableDisabledApply=false;    
			            $scope.enableDisabledValidated=true;
			        }
			        $scope.initCoEmp();
			        $scope.initEmpObj();
			        
			    }
			    $scope.clearCoEmpDetails=function(){
			    /*    $scope.corEmpDependantList=[];
			        $scope.showNameDetails=false;
			         $scope.showHideAcceptRejectBtn=false;
			          $scope.showHideOkBtn=false;*/
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

				$scope.getPatientClassService = function()
				{
					// alert($scope.isPatientClass);
					
					if($scope.isPatientClass == 'Y' && $scope.admissionDetails.patientClassId > 0 )
						{
								var data = {
										organizationId:$scope.organizationId,
										unitId : $scope.unitId,
										patientCategoryId : $scope.admissionDetails.patientCategoryId,
										patientClassId : $scope.admissionDetails.patientClassId,
										tariffId : $scope.defaultSelfTariffId,
										serviceIds : [],
										saleIds : [],
										admissionId : $scope.admissionDetails.admissionId,
										visitTypeId : $scope.visitTypeId,
										pharmacyServiceId:$scope.pharmacyServiceId,
										paymentEntitlementTypeId:$scope.admissionDetails.paymentEntitlementId
								}
						angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
							data.serviceIds.push(value.serviceMasterId);
							if(value.saleId!=null)
							    data.saleIds.push(value.saleId);
							});
						    $rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL + "/billing/surcharge/service/details";
					    	
					    	BillingGenericService.serviceAction(METHOD_POST,URI,data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					    		$rootScope.stopLoader();
					    		if(response.data.status == "success"){
					    			
					    			var newArr = {};
					    			newArr.priorityId  = '2';
					    			newArr.isOutsourced ='N';
					    			newArr.specialityId = null;
					    			newArr.serviceSpecialityId =response.data.listObject[0].specialityId;
					    			newArr.serviceSubSpecialityId = response.data.listObject[0].subSpecialityId;
					    			newArr.doctorId = null;
					    			newArr.doctorSpecialityId = null;
	    			    			newArr.serviceRendered = 1;
					    			newArr.serviceRenderingDeptId = null;
					    			newArr.serviceMasterId = response.data.listObject[0].serviceMasterId;
					    			newArr.serviceStandardCode = response.data.listObject[0].serviceStandardCode;
					    			newArr.serviceStandardName = response.data.listObject[0].serviceStandardName;
					    			newArr.isDiscountApplicable = 'N';
					    			newArr.isDiscount = 'N';
					    			newArr.isQuantityEditable ='N';
					    			newArr.isRateEditable = 'N';
					    			newArr.minRateEditable = response.data.listObject[0].minRateEditable;
					    			newArr.maxRateEditable = response.data.listObject[0].maxRateEditable;
					    			newArr.specialityName = response.data.listObject[0].specialityName;
					    			newArr.doctorName ='';
					    			newArr.quantity = 1;
					    			newArr.discountDisabledFlag = 'N';
					    			newArr.concession = parseFloat(0);
					    			newArr.discountPercentage = parseFloat(0);
					    			newArr.discount = parseFloat(0);
					    			newArr.taxId = response.data.listObject[0].taxId;
					    			newArr.taxName = response.data.listObject[0].taxName;
					    			newArr.taxPercentage = response.data.listObject[0].taxPercentage;
					    			newArr.discountDetailsList = [];
					    			newArr.creditPayable =parseFloat(0);
					    			newArr.creditPayableOldC = parseFloat(0);
					    			newArr.oldNetPay = "0";
					    			newArr.createdBy  = $scope.userId;
					    			newArr.createdDate = new Date().getTime();
					    			newArr.isPackageService = "N";
					    			newArr.isCapService = "N";
					    			newArr.subSpecialityId = response.data.listObject[0].subSpecialityId;
					    			newArr.ordCancelled = "N";
					    			newArr.status = "A";
					    			/*newArr.ordCancelReasonId  = '';
					    			newArr.ordCancelDatetime  = new Date().getTime();
					    			newArr.ordCancelRemark  = '';*/
					    			newArr.payeeId  = $scope.selfPayeeId;
					    			newArr.coShare =0;
					    			newArr.coPayPer =0;
					    			newArr.contractId = 0;
					    			newArr.isRefDoctorShare = "N";
									newArr.medallServiceId = response.data.listObject[0].medallServiceId != null ? response.data.listObject[0].medallServiceId : '';

					    			var patientClassTotal = parseFloat(0);
					    			angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
					    				var stopFlag = true;
					    				angular.forEach(value.orderDetailsPackageMapperDtosList, function (pkgValue, key) 
								    			{
							    					if(pkgValue.isPackageService=='N')
					    					    	   {
									    				   angular.forEach(response.data.listObject[0].discountDetailsList, function (value1, key1) {
									    					if(value.serviceMasterId == value1.serviceId && stopFlag == true)
									    					{
									    						stopFlag = false;
									    					    var totalAmt = (parseFloat(value.quantity) * parseFloat(pkgValue.serviceAmount)).toFixed(2);
									    					    var calDicAmt = (parseFloat(totalAmt)*value1.discountPercentage/100).toFixed(2);	
									    					    patientClassTotal = parseFloat(patientClassTotal) + parseFloat(calDicAmt);
									    					 }
									    				  });
					    					    	   }
								               });
					    			      });
					    			newArr.basePrice = patientClassTotal;
					    			newArr.basePriceOld = patientClassTotal;
					    			newArr.selfPayable = patientClassTotal;
					    		    newArr.selfPayableOldC = patientClassTotal;
					    		    newArr.totalAmt =  patientClassTotal;
					    		    newArr.netPay  = patientClassTotal;
					    		    newArr.packageConsumptionAmt  = parseFloat(0);
					    		    newArr.concession = parseFloat(0);
									newArr.patientClassPercentage = parseFloat(0);
					    		    
					    			var dataMappr = [{
				    					contractId: null,
				    					creditPayable: 0,
				    					organizationId: $scope.organizationId,
				    					payeeAmount: patientClassTotal,
				    					payeeId:$scope.selfPayeeId,
				    					selfPayable: patientClassTotal,
				    					serviceId: response.data.listObject[0].serviceMasterId,
				    					status: 'A',
				    					unitId: $scope.unitId
				    			}];
				    			// ganesh
				    			newArr.orderDetailsPayeeMapperDtosList = dataMappr;
				    			newArr.ordRenderDatetimeString = moment(new Date()).format('DD/MM/YYYY');
				    			newArr.orderDateStringNew = moment(new Date()).format('DD/MM/YYYY HH:mm');
				    			newArr.pharmacyDiscountOverride = value.pharmacyDiscountOverride;
				    			
				    			// for Package
				    			var packageMppr = [{"id":"",
				    				"orderDetailsId":"",
				    				"serviceId":response.data.listObject[0].serviceMasterId,
				    				"packageId":"",
				    				"payeeId":"",
				    				"serviceAmount":patientClassTotal,	
				    				"packageAmount":0,
				    				"packageCode":"",
				    			    "packageName":"",
				    				"isPackageService":"N",
				    				"isCapService":"N",
				    				"saleDetailsId":"",
				    				"itemId":"",
				    				"status":"A",
				    				"unitId":$scope.unitId,
				    				"organizationId": $scope.organizationId}];
				    			
				    			  newArr.orderDetailsPackageMapperDtosList = packageMppr;
				    			
				    			
				    			  if(parseFloat(patientClassTotal) > 0 ){
					    		      $scope.autoRenderedServicesFinalList.push(newArr);
					    		      $scope.initialAutoRenderedServicesFinalList=$scope.autoRenderedServicesFinalList;
					    		      $scope.calculationTest($scope.autoRenderedServicesFinalList,1);
					    		    }
					    		    
					    		   /* $scope.autoRenderedServicesFinalList.push(newArr);
					    		    $scope.calculationTest($scope.autoRenderedServicesFinalList,1);*/
					    		}
					    	});
						}
					else
						{
							var temp = {
									serviceMasterId : $scope.surchargeServiceId
							}
							
							var indexT = $scope.autoRenderedServicesFinalList.findIndex(record => record.serviceMasterId === $scope.surchargeServiceId);
							
							
							    if (indexT >= 0) {
							    	if($scope.autoRenderedServicesFinalList[indexT].orderDetailsId > 0){
							    		
							    		var data = {
							    				orderDetailsId : $scope.autoRenderedServicesFinalList[indexT].orderDetailsId,
							    				ordCancelReasonId : null,
							    				userId : $scope.userId
							    		}
							    		
							    		var URI = BASE_URL + ROOT_URL + "/api/billing/bill/service/cancel";
								    	
								    	BillingGenericService.serviceAction(METHOD_POST,URI,data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
								    		if(response.data.status == "success"){
								    			$scope.autoRenderedServicesFinalList.splice(indexT, 1);
										    	$scope.calculationTest($scope.autoRenderedServicesFinalList,1);
												$rootScope.stopLoader();
												$scope.saveOrderPackage();
								    		}else{
								    			$rootScope.stopLoader();
								    		}
								    	});
							    	
							    }else{
							    	$scope.autoRenderedServicesFinalList.splice(indexT, 1);
							    	$scope.calculationTest($scope.autoRenderedServicesFinalList,1);
									$rootScope.stopLoader();
							    }
						}
					
						}
			    	
				}
				
				$scope.searchOrderServiceByName = function(searchKeyword){
					var param = {encounterId:$scope.admissionDetails.admissionId,visitTypeId:$scope.visitTypeId,searchKeyword:searchKeyword};//visit type IP default
					var URI = BASE_URL + ROOT_URL + "/api/billing/serviceMaster/orderedServices";
					BillingGenericService.serviceAction(METHOD_POST,URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						if(response.data.status == "success"){
							$scope.autoRenderedServicesList = [];
							$scope.autoRenderedServicesList = response.data.listObject;
							
							var indexT = response.data.listObject.findIndex(record => record.serviceMasterId === $scope.surchargeServiceId);
							
						    if (indexT >= 0) {
						    	$scope.isPatientClass = "Y";
						    }
						    
							$scope.calculationTest($scope.autoRenderedServicesList,init);
							$scope.loadingServiceFlag = false;

							$rootScope.stopLoader();
							  $rootScope.stopLoaderPopup();
							  angular.element('#addServicePopup').modal('hide');
							  angular.element('#serviceCancellationModal').modal('hide');
							  angular.element('#packageForceAdd').modal('hide');
							  $rootScope.stopLoader();
							  $rootScope.stopLoaderPopup();
							  $scope.loadDataForFilterDummyServices();
						}
						else if(response.data.status == "error")
						{
							 $rootScope.stopLoader();
							 $rootScope.stopLoaderPopup();
							}
					});	
					$scope.getListPackageOrder();
				}
				
				
				$scope.newRequestCalculates = function(){
					var totalSummary=0;
					var totalAuthSummary=0;
					angular.forEach($scope.addNewRequestList,function(value,index){
						totalSummary = parseFloat(totalSummary) + (isNaN(value.requestAmount) ? 0 : parseFloat(value.requestAmount));
						totalAuthSummary = parseFloat(totalAuthSummary) + (isNaN(value.authorizationAmount) ? 0 : parseFloat(value.authorizationAmount));
					});
					$scope.summary = $rootScope.parseFloatNum(totalSummary);
					$scope.authsummary = $rootScope.parseFloatNum(totalAuthSummary);
					$scope.authsummarySplitBill = $scope.authsummary;
			  }
				
				$scope.calculateSplitBillTotal = function(inputList)
				{
					$scope.totalAmountSummarySplitBill = 0;
					$scope.totalCreditPaybleSplitBill =0 ;
					$scope.totalSelfPaybleSplitBill = 0;
			        $scope.totalNetAmt = 0;
			        $scope.totalCreditPaybleSplitBillOld =0 ;
			        $scope.totalSelfPaybleSplitBillOld = 0;
			    	$scope.totalDiscountSplit = 0;
					$scope.totalConsessionSplit = 0;
					$scope.totalGstSplit = 0;


					angular.forEach(inputList,function(value,index)
					{
						angular.forEach(value.orderDetailsPackageMapperDtosList, function (value1, key) 
								{
									if(value1.isPackageService=='N' && value.status=='A')
									{
										value.selfPayable = (isNaN(value.selfPayable)||value.selfPayable==undefined)?0:value.selfPayable;
										value.creditPayable = (isNaN(value.creditPayable)||value.creditPayable==undefined)?0:value.creditPayable;
										$scope.totalAmountSummarySplitBill = (parseFloat($scope.totalAmountSummarySplitBill)+parseFloat(value.totalAmt)).toFixed(2);
										$scope.totalCreditPaybleSplitBill =(parseFloat($scope.totalCreditPaybleSplitBill)+parseFloat(value.creditPayable)).toFixed(2);
										$scope.totalSelfPaybleSplitBill = (parseFloat($scope.totalSelfPaybleSplitBill)+parseFloat(value.selfPayable)).toFixed(2);
							            $scope.totalCreditPaybleSplitBillOld =(parseFloat($scope.totalCreditPaybleSplitBillOld) + parseFloat(value.creditPayable)).toFixed(2);
							            $scope.totalSelfPaybleSplitBillOld = (parseFloat($scope.totalSelfPaybleSplitBillOld) +parseFloat(value.selfPayable)).toFixed(2);
							            $scope.totalNetAmt =(parseFloat($scope.totalNetAmt)+parseFloat(value.netPay)).toFixed(2);
							            $scope.totalDiscountSplit = (parseFloat($scope.totalDiscountSplit)+ parseFloat(value.discount)).toFixed(2);
							            $scope.totalConsessionSplit = (parseFloat($scope.totalConsessionSplit)+ parseFloat(value.concession)).toFixed(2);
							            $scope.totalGstSplit = (parseFloat($scope.totalGstSplit)+ parseFloat(value.taxAmt)).toFixed(2);
									}
								});
							 });
					$scope.totalAmountSummarySplitBill = parseFloat($scope.totalAmountSummarySplitBill).toFixed(2);
					$scope.totalCreditPaybleSplitBill =parseFloat($scope.totalCreditPaybleSplitBill).toFixed(2);
					$scope.totalSelfPaybleSplitBill = parseFloat($scope.totalSelfPaybleSplitBill).toFixed(2);
					this.$parent.totalCreditPaybleSplitBill =parseFloat($scope.totalCreditPaybleSplitBill).toFixed(2);
					this.$parent.totalSelfPaybleSplitBill = parseFloat($scope.totalSelfPaybleSplitBill).toFixed(2);
					$scope.totalCreditPaybleSplitBillOld =parseFloat($scope.totalCreditPaybleSplitBillOld).toFixed(2);
					$scope.totalSelfPaybleSplitBillOld = parseFloat($scope.totalSelfPaybleSplitBillOld).toFixed(2);
					$scope.totalNetAmt = parseFloat($scope.totalNetAmt).toFixed(2);
					
	                
	                 /*if($scope.resetvalue == true){
	                	 var checkAmount2 = parseFloat(Number(parseFloat($scope.totalAmountSummarySplitBill).toFixed(2))-Number(parseFloat($scope.totalConsessionSplit).toFixed(2))-Number(parseFloat($scope.totalDiscountSplit).toFixed(2))).toFixed(2);
		                 var checkAmount = parseFloat(Number(parseFloat($scope.totalSelfPaybleSplitBill).toFixed(2))+Number(parseFloat($scope.totalCreditPaybleSplitBill).toFixed(2))).toFixed(2);
		                
	                 if(parseFloat(checkAmount2) > parseFloat(checkAmount)){
	                     var amounjj = 0 ;
	                     amounjj = parseFloat(Number(parseFloat(checkAmount2).toFixed(2))-Number(parseFloat(checkAmount).toFixed(2))).toFixed(2);
	                        if($scope.roundOffAmt!=0 && $scope.totalSelfPaybleSplitBill!=0)
	                        {
	                            $scope.totalSelfPaybleSplitBill = parseFloat(Number(parseFloat($scope.totalSelfPaybleSplitBill).toFixed(2))+parseFloat(amounjj)).toFixed(2);
	                        }else{
	                            $scope.totalCreditPaybleSplitBill = parseFloat(Number(parseFloat($scope.totalCreditPaybleSplitBill).toFixed(2))+parseFloat(amounjj)).toFixed(2);
	                        }
	                }else{
	                     var amounjj = 0 ;
	                     amounjj = parseFloat(Number(parseFloat(checkAmount).toFixed(2))-Number(parseFloat(checkAmount2).toFixed(2))).toFixed(2);
	                        if($scope.roundOffAmt!=0 && $scope.totalSelfPaybleSplitBill!=0)
	                        {
	                            $scope.totalSelfPaybleSplitBill = parseFloat(Number(parseFloat($scope.totalSelfPaybleSplitBill).toFixed(2))-parseFloat(amounjj)).toFixed(2);
	                        }else{
	                            $scope.totalCreditPaybleSplitBill = parseFloat(Number(parseFloat($scope.totalCreditPaybleSplitBill).toFixed(2))-parseFloat(amounjj)).toFixed(2);
	                        }
	                }
	                 $scope.resetvalue = false;
	               }
					
	 				if($scope.serviceList != undefined  && $scope.selfSlitBillAmount == true && $scope.serviceList.length == $scope.co ){
		                var checkAmount2 = parseFloat(Number(parseFloat($scope.totalAmountSummarySplitBill).toFixed(2))-Number(parseFloat($scope.totalConsessionSplit).toFixed(2))-Number(parseFloat($scope.totalDiscountSplit).toFixed(2))).toFixed(2);
		                 var checkAmount = parseFloat(Number(parseFloat($scope.totalSelfPaybleSplitBill).toFixed(2))+Number(parseFloat($scope.totalCreditPaybleSplitBill).toFixed(2))).toFixed(2);
		                    
		                 if(parseFloat(checkAmount2) > parseFloat(checkAmount)){
		                     var amounjj = 0 ;
		                     amounjj = parseFloat(Number(parseFloat(checkAmount2).toFixed(2))-Number(parseFloat(checkAmount).toFixed(2))).toFixed(2);
		                            $scope.totalCreditPaybleSplitBill = parseFloat(Number(parseFloat($scope.totalCreditPaybleSplitBill).toFixed(2))+parseFloat(amounjj)).toFixed(2);
		                }else{
		                     var amounjj = 0 ;
		                     amounjj = parseFloat(Number(parseFloat(checkAmount).toFixed(2))-Number(parseFloat(checkAmount2).toFixed(2))).toFixed(2);
		                            $scope.totalCreditPaybleSplitBill = parseFloat(Number(parseFloat($scope.totalCreditPaybleSplitBill).toFixed(2))-parseFloat(amounjj)).toFixed(2);
		                }
		                 $scope.selfSlitBillAmount =false;
						}
						
						if($scope.serviceList != undefined &&  $scope.creditSlitBillAmount == true && $scope.serviceList.length == $scope.co){
			                var checkAmount2 = parseFloat(Number(parseFloat($scope.totalAmountSummarySplitBill).toFixed(2))-Number(parseFloat($scope.totalConsessionSplit).toFixed(2))-Number(parseFloat($scope.totalDiscountSplit).toFixed(2))).toFixed(2);
			                 var checkAmount = parseFloat(Number(parseFloat($scope.totalSelfPaybleSplitBill).toFixed(2))+Number(parseFloat($scope.totalCreditPaybleSplitBill).toFixed(2))).toFixed(2);
			                    
			                 if(parseFloat(checkAmount2) > parseFloat(checkAmount)){
			                     var amounjj = 0 ;
			                     amounjj = parseFloat(Number(parseFloat(checkAmount2).toFixed(2))-Number(parseFloat(checkAmount).toFixed(2))).toFixed(2);
			                            $scope.totalSelfPaybleSplitBill = parseFloat(Number(parseFloat($scope.totalSelfPaybleSplitBill).toFixed(2))+parseFloat(amounjj)).toFixed(2);
			                }else{
			                     var amounjj = 0 ;
			                     amounjj = parseFloat(Number(parseFloat(checkAmount).toFixed(2))-Number(parseFloat(checkAmount2).toFixed(2))).toFixed(2);
			                            $scope.totalSelfPaybleSplitBill = parseFloat(Number(parseFloat($scope.totalSelfPaybleSplitBill).toFixed(2))-parseFloat(amounjj)).toFixed(2);
			                }
			                 
			                 $scope.creditSlitBillAmount = false;
							}*/
					return inputList;
				};
			  
				
				$scope.getAuthrizationDetails = function()
				{
					if($scope.patientDetails.patientId==null)
						{
						growl.warning("Please select patient...!!",{
							title : ERROR_MSG
							
						});
						return false;
						}
					if($scope.admissionDetails.payeeId==null)
						{
						growl.warning("Please select payer...!!",{
							title : ERROR_MSG
						});
						return false;
						}
					$rootScope.startLoader();
					 $scope.addNewRequestList = [];
				    var param = {
					    		"organisationId":$scope.organizationId,
					    		"unitId":$scope.unitId,
					    		"patientId":$scope.patientDetails.patientId, 
					    		"isSplitStatus": "N",
					    		"authStatusId": 1,
					    		"admissionId" : $scope.admissionDetails.admissionId
				    		};
				    var URI = BASE_URL + ROOT_URL + "/api/ipd/getPreAuthorizationRequestListByPayeeIdIPDCharges"
				    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				  if (response.data.status == "success"){
				    $scope.addNewRequestList = response.data.listObject;
				    $scope.newRequestCalculates();
				  }
				     $rootScope.stopLoader();
				 });
				}
				$scope.getProceduresByAdmission = function()
			    {
					if($scope.admissionId > 0){
					$scope.proceduresList = [];
			    	var URI = BASE_URL + ROOT_URL + "/api/procedure/procedures/"+$scope.admissionId+"/0";
					BillingGenericService.serviceAction(METHOD_GET,URI, {} , NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						//console.log(response)
						if(response.data.status == 'success'){
							$scope.proceduresList = response.data.listObject;
						}
						
					});
			       }
			    }
				$rootScope.setThirdTabLevel = function(newTab){
					$rootScope.thirdTabLevel = newTab;
					if(newTab==10)
						{
						  $scope.getAuthrizationDetails();
						}
						if(newTab==5)
						{
							$scope.getCancelServices();
						}
						if(newTab==6)
						{
							$scope.getProceduresByAdmission();
						}
						/*if(newTab==12)
						{
							if($scope.autoRenderedServicesFinalList != null){
								$scope.ServiceCount = $scope.autoRenderedServicesFinalList.length;
							}
						}*/
				};
				$scope.showSplitBill = function()
				{
					$scope.intermediateServiceTemp = angular.copy($scope.autoRenderedServicesFinalList);
					var saveOrderDetailsObj = [];
                    $scope.actualServiceCount = $scope.autoRenderedServicesFinalList.length;

					if($scope.isIntermediateBill == 'Y' && $scope.servicesSelectedList.services.length == 0)
						{
							 growl.warning("Please select service for intermediate.",{
									title : ERROR_MSG
								});
					    		return false;
						}else{
							if($scope.isIntermediateBill == 'Y' && $scope.servicesSelectedList.services.length  > 0){
								
								 $scope.calculationForIntermediate();
								$scope.serviceList = [];
								//$scope.intermediateServiceTemp = angular.copy($scope.autoRenderedServicesFinalList);
								
								 var len = $scope.servicesSelectedList.services.length;
									//var len = arrUnique.length;
								    	for(var i = 0;i < len;i++)
										{	
								    		angular.forEach($scope.intermediateServiceTemp, function (value, key) {
								    			
								    			if(value.orderDetailsId == $scope.servicesSelectedList.services[i])
												   {
								    				$scope.serviceList.push(value);
												   }
								    		});
								    		if(i == len-1){
								    			
								    			$scope.getAuthrizationDetails();
								    			$scope.serviceListOLD = angular.copy($scope.serviceList);
								    			$scope.resetSplitBill();
												$('#splitBillModal').modal('show');
								    		}
								    		
										}
							}
							if($scope.finalBill == 'Y'){
								
					
								$scope.getAuthrizationDetails();
								$scope.serviceList = angular.copy($scope.autoRenderedServicesFinalList);
								$scope.serviceListOLD = angular.copy($scope.autoRenderedServicesFinalList);
								$scope.resetSplitBill();
								$('#splitBillModal').modal('show');
								
							}
							
						}
					
				}
				
				
			  
				
				$scope.applySplitBill = function()
				{
					$scope.applyAuth = true;
					$scope.saveOrderPackage();
				}
				
				$scope.resetToActualValuesAfterClosePopup = function()
				{
					if($scope.isSplitBillFlag==true)
					{
						$scope.authsummarySplitBill = angular.copy($scope.authsummary);
						$scope.serviceList = angular.copy($scope.serviceListOLD);
						$scope.isSplitBillFlag =false;
						$scope.isSplitBillApplied = false;
						$scope.autoRenderedServicesFinalList = $scope.serviceListOLD;
					}
					
				}
				
				$scope.resetvalue = false;
				$scope.resetSplitBill = function()
				{
					$scope.authsummarySplitBill = angular.copy($scope.authsummary);
					$scope.serviceList = angular.copy($scope.serviceListOLD);
					$scope.resetvalue = true;
					$scope.calculateSplitBillTotal($scope.serviceList);
					$scope.isSplitBillFlag =false;
					$scope.isSplitBillApplied = false;
				}
				
				$scope.saveSplitBill = function()
				{
					$scope.autoRenderedServicesFinalList = $scope.serviceList;
					$scope.saveFromSplitBill = true;
			        var res = $scope.calculationTest($scope.autoRenderedServicesFinalList,1);
			        if(res == 1)
			        {
			        	$scope.popUpFlag = false;
			        	$scope.saveBillngNew();
			        }

				}
				
				 $scope.getClearData = function(type)
				    {
					
				    	$scope.admissionType = type;
				    	//$scope.initPatientSelected();
				    	$scope.init();
				    	$scope.patientList = [];
				    	$scope.autoRenderedServicesFinalList =[];
				    	$scope.autoRenderedServicesForSaveAndUpdate=[];
				    	
				    }
				
			
				  
				   $scope.getDiscountPerCal = function(itemObj,idx){
					   var i =0;
			    	   angular.forEach($scope.initialAutoRenderedServicesFinalList, function (value, key) {
				   			if(value.orderDetailsId == itemObj.orderDetailsId){
				   				idx = i;
				   			 }
			   			i++;
			   			});
			    	   $scope.autoRenderedServicesFinalList =[]; 
			    	   $scope.autoRenderedServicesFinalList = $scope.initialAutoRenderedServicesFinalList;
					   
				    	if(itemObj.discountPercentage == '' || itemObj.discountPercentage == undefined || itemObj.discountPercentage == '.' ){
				    		itemObj.discountPercentage = 0;
				    		itemObj.discount = 0;
				    		$scope.autoRenderedServicesFinalList[idx].discount = parseFloat(0);
				    		$scope.autoRenderedServicesFinalList[idx].discountPercentage = parseFloat(0);
				    	}
				    	if(parseFloat(itemObj.discountPercentage) > 100)
						{
				    		$scope.autoRenderedServicesFinalList[idx].discountCategoryId=$scope.discountCategoryList[0].id;
							//$scope.autoRenderedServicesFinalList[idx].discount = parseFloat(0);
				    		
				            $scope.autoRenderedServicesFinalList[idx].concession = parseFloat(itemObj.concession);
				            $scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);
				            
				            $scope.autoRenderedServicesFinalList[idx].discount = parseFloat(0);

				            
				    		$scope.autoRenderedServicesFinalList[idx].discountPercentage = parseFloat(0);
				    		
				    		var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
					        $scope.autoRenderedServicesFinalList[idx].discountPercentage = isNaN(((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100).toFixed(2)) ? parseFloat(0).toFixed(2) : ((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100).toFixed(2);
					        $scope.autoRenderedServicesFinalList[idx].discount = parseFloat(itemObj.discount).toFixed(2);
					        //var withConDic =parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[idx].discount);
				            var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[idx].concession) + parseFloat($scope.autoRenderedServicesFinalList[idx].discount));

					        
					    	var txpre = 0;
							if(itemObj.taxPercentage != null || itemObj.taxPercentage > 0){
								$scope.autoRenderedServicesFinalList[idx].taxPercentage = itemObj.taxPercentage;
								txpre = itemObj.taxPercentage;
							}else{
									$scope.autoRenderedServicesFinalList[idx].taxPercentage = 0;
									}
							$scope.autoRenderedServicesFinalList[idx].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
							$scope.autoRenderedServicesFinalList[idx].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[idx].taxAmt)).toFixed(2);
							$scope.autoRenderedServicesFinalList[idx].selfPayable = $scope.autoRenderedServicesFinalList[idx].netPay;
							$scope.autoRenderedServicesFinalList[idx].creditPayable =parseFloat(0);
							//$scope.saveOrderPackage();
							$scope.autoRenderedServicesForSaveAndUpdate=[];
				        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
				        	$scope.addOrUpdateServices(idx,null);
				        	
							//flagDis = false;
							growl.warning("You can't give discount more than 100 %.",{
								title : ERROR_MSG
							});
							
							$scope.billWiseDiscountObj = {
									// "discountGivenBy":"",
									"discountGivenBy":$scope.userId,
									"discountCategoryId" : "",
									"discountAmountBillWise" : "",
									"discountPercentageBillWise" : "",
									"approvedAmountBillWise":"",
									"requestUserId":"",
									"requestAmountBillWise" : 0,
							}
							$scope.requestDiscountDiv = false;
					    	$scope.isFinancialClearence = 'N';
							return false;
						}
				    	else
				    	{
				    		$scope.autoRenderedServicesFinalList[idx].discountCategoryId=$scope.discountCategoryList[0].id;
				    		$scope.billWiseDiscountObj = {
									// "discountGivenBy":"",
									"discountGivenBy":$scope.userId,
									"discountCategoryId" : "",
									"discountAmountBillWise" : "",
									"discountPercentageBillWise" : "",
									"approvedAmountBillWise":"",
									"requestUserId":"",
									"requestAmountBillWise" : 0,
							}
							$scope.requestDiscountDiv = false;
					    	$scope.isFinancialClearence = 'N';
					    	
					    	
				    		var i = 1;
				    		var discountAmtTmp = parseFloat($scope.totalDiscountSummary) - parseFloat(itemObj.oldDiscount) ;
				    		/*angular.forEach($scope.autoRenderedServicesFinalList, function (service, key) 
								{*/
		    					if(parseFloat(itemObj.discountPercentage) >= 0)
		    					{
		    						//$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(0);
		   						   // $scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(0);
		    						
		    						$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(itemObj.concession);
		   						    $scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);
		   						    
		   						    
			    			        var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
			    			       
			    			       // $scope.autoRenderedServicesFinalList[idx].discount =((parseFloat(totalAmt))*(isNaN(itemObj.discountPercentage) ? 0: itemObj.discountPercentage)/100).toFixed(2);
		                            $scope.autoRenderedServicesFinalList[idx].discount =((parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[idx].concession))*(isNaN(itemObj.discountPercentage) ? 0: itemObj.discountPercentage)/100).toFixed(2);

		                            
		                            discountAmtTmp = discountAmtTmp + $scope.autoRenderedServicesFinalList[idx].discount;
		                            
			    			        $scope.autoRenderedServicesFinalList[idx].discountPercentage = parseFloat(itemObj.discountPercentage).toFixed(2);
			    			        //var withConDic =parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[idx].discount);
		                            var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[idx].concession) + parseFloat($scope.autoRenderedServicesFinalList[idx].discount));

			    			    	var txpre = 0;
									if(itemObj.taxPercentage != null || itemObj.taxPercentage > 0){
										$scope.autoRenderedServicesFinalList[idx].taxPercentage = itemObj.taxPercentage;
										txpre = itemObj.taxPercentage;
									}else{
											$scope.autoRenderedServicesFinalList[idx].taxPercentage = 0;
											}
									$scope.autoRenderedServicesFinalList[idx].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
									$scope.autoRenderedServicesFinalList[idx].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[idx].taxAmt)).toFixed(2);
									$scope.autoRenderedServicesFinalList[idx].selfPayable = $scope.autoRenderedServicesFinalList[idx].netPay;
									$scope.autoRenderedServicesFinalList[idx].creditPayable =parseFloat(0);
		    						}
							    
								
								/*if(i == $scope.autoRenderedServicesFinalList.length){*/
									//$scope.saveOrderPackage();
									if($scope.requestStatusObject == null){
										//$scope.saveOrderPackage();
										$scope.autoRenderedServicesForSaveAndUpdate=[];
							        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
							        	$scope.addOrUpdateServices(idx,null);
									}else{
										if($scope.requestStatusObject.reqStatus == 1){
												$scope.getCancelDiscountReqForDiscount();
												//$scope.saveOrderPackage();
												$scope.autoRenderedServicesForSaveAndUpdate=[];
									        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
									        	$scope.addOrUpdateServices(idx,null);
										}
										else{
											if(parseFloat($scope.requestStatusObject.approvedDiscAmt) > parseFloat(discountAmtTmp)){
												//$scope.saveOrderPackage();
												$scope.autoRenderedServicesForSaveAndUpdate=[];
									        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
									        	$scope.addOrUpdateServices(idx,null);
											}else{
												$scope.getCancelDiscountReqForDiscount();
												//$scope.saveOrderPackage();
												$scope.autoRenderedServicesForSaveAndUpdate=[];
									        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
									        	$scope.addOrUpdateServices(idx,null);
											}
										}
									}
								/*}
								i++;*/
						/*});*/
						}
				        if($scope.autoRenderedServicesFinalList[idx].discount==undefined || $scope.autoRenderedServicesFinalList[idx].discount==null || $scope.autoRenderedServicesFinalList[idx].discount==0)
                        {
                        $scope.autoRenderedServicesFinalList[idx].discountCategoryId=null;
                        }
				        
				        $scope.billWiseDiscountObj.requestAmountBillWise =0;
				    	   $scope.billWiseDiscountObj.discountAmountBillWise =0;
				    	   angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
				    		   
				    		   $scope.billWiseDiscountObj.discountAmountBillWise  += parseFloat(value.discount);
				    		   $scope.billWiseDiscountObj.requestAmountBillWise   += parseFloat(value.discount);
				    	   });
				    	   
				    	   if($scope.userDiscountObject != null){
					    		
								if($scope.userDiscountObject.discountType == 1){
									
									var fromDisAmt = (parseFloat($scope.totalSumDisAmount) * parseFloat($scope.userDiscountObject.fromDiscountPer))/100;
									var toDisAmt = (parseFloat($scope.totalSumDisAmount) * parseFloat($scope.userDiscountObject.toDiscountPer))/100;
									$scope.userDiscountObject.fromDiscountAmount = fromDisAmt;
									$scope.userDiscountObject.toDiscountAmount = toDisAmt;
								}
					    	}
				    	   
				    	   $scope.funDiscountAmountBillWise();
				    	   var checkrightsAmount = false;
				    	   if((parseFloat($scope.billWiseDiscountObj.discountAmountBillWise) < parseFloat($scope.userDiscountObject.fromDiscountAmount))){
				    		   checkrightsAmount = true;
				    	   }
				    	   if((parseFloat($scope.billWiseDiscountObj.discountAmountBillWise) > parseFloat($scope.userDiscountObject.toDiscountAmount))){
				    		   checkrightsAmount = true;
				    	   }
				    	   
				    	   if(checkrightsAmount == true){
					    		var URI = BASE_URL + ROOT_URL
					    		+ "/billing/getUsersListHavingDiscRights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/amt/"+$scope.billWiseDiscountObj.discountAmountBillWise+"/per/"+$scope.billWiseDiscountObj.discountPercentageBillWise;
					    BillingGenericService.serviceAction(METHOD_GET, URI,"",
					    				NOTIFICATION_MSG_STATUS_FALSE)
					    		.then(function(response) {
					    					//console.log(response);
					    					// $rootScope.startLoader();
					    					if (response.data.status == "success")
					    						{
					    						  $scope.userDiscountList =[];
					    						  $scope.userDiscountList = response.data.listObject;
					    						}
					    						
					    				});
					            $scope.lineLevelItem = true;
							   $scope.requestDiscountDiv = true;
							   $scope.isBillWiseDiscountApplied = true;
							   $scope.finalBill = 'N';
							   growl.warning("Please send discount request to user",{
									title : ERROR_MSG
								});
							   
					    	}else
					    		{
					    		 $scope.requestDiscountDiv = false;
					    		 $scope.billWiseDiscountObj = {
											"discountGivenBy":$scope.userId,
											"discountCategoryId" : "",
											"discountAmountBillWise" : "",
											"discountPercentageBillWise" : "",
											"approvedAmountBillWise":"",
											"requestUserId":"",
											"requestAmountBillWise" : 0,
									}
									$scope.requestDiscountDiv = false;
							    	$scope.isFinancialClearence = 'N';
					    		}
				    	   
				    }
				    
				   
				   $scope.getConcessionCal = function(itemObj,idx)
				   {
				    	
					   var i =0;
			    	   angular.forEach($scope.initialAutoRenderedServicesFinalList, function (value, key) {
				   			if(value.orderDetailsId == itemObj.orderDetailsId){
				   				idx = i;
				   			 }
			   			i++;
			   			});
			    	   $scope.autoRenderedServicesFinalList =[]; 
			    	   $scope.autoRenderedServicesFinalList = $scope.initialAutoRenderedServicesFinalList;
			    	   
						if(parseFloat(itemObj.patientClassPercentage) != parseFloat(itemObj.patClsPerPriceRateChange))
					 	{
						 
					    	if(itemObj.patientClassPercentage == '' || itemObj.patientClassPercentage == undefined || itemObj.patientClassPercentage == '.'){
					    		itemObj.patientClassPercentage = 0;
					    		itemObj.concession = 0;
					    		$scope.autoRenderedServicesFinalList[idx].patientClassPercentage = 0;
					    		$scope.autoRenderedServicesFinalList[idx].concession = 0;
					    	}
					    	if(parseFloat(itemObj.patientClassPercentage) > 100)
							{
					    		$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(0);
								$scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(0);
					    		
					    		$scope.autoRenderedServicesFinalList[idx].discount = parseFloat(itemObj.discount);
					    		$scope.autoRenderedServicesFinalList[idx].discountPercentage = parseFloat(itemObj.discountPercentage);
					    		
					    		var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
						        $scope.autoRenderedServicesFinalList[idx].discountPercentage = isNaN(((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100).toFixed(2)) ? parseFloat(0).toFixed(2) : ((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100).toFixed(2);
						        $scope.autoRenderedServicesFinalList[idx].discount = parseFloat(itemObj.discount).toFixed(2);
						        var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[idx].concession) + parseFloat($scope.autoRenderedServicesFinalList[idx].discount));
						        
						    	var txpre = 0;
								if(itemObj.taxPercentage != null || itemObj.taxPercentage > 0){
									$scope.autoRenderedServicesFinalList[idx].taxPercentage = itemObj.taxPercentage;
									txpre = itemObj.taxPercentage;
								}else{
										$scope.autoRenderedServicesFinalList[idx].taxPercentage = 0;
										}
								$scope.autoRenderedServicesFinalList[idx].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
								$scope.autoRenderedServicesFinalList[idx].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[idx].taxAmt)).toFixed(2);
								$scope.autoRenderedServicesFinalList[idx].selfPayable = $scope.autoRenderedServicesFinalList[idx].netPay;
								$scope.autoRenderedServicesFinalList[idx].creditPayable =parseFloat(0);
								$scope.autoRenderedServicesForSaveAndUpdate=[];
					        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
					        	$scope.addOrUpdateServices(idx,null);
								
								//flagDis = false;
								growl.warning("You can't give concession more than 100 %.",{
									title : ERROR_MSG
								});
								return false;
					    		
							}
					    	else
					    	{
					    		var i = 1;
					    		//var discountAmtTmp = parseFloat(0);
					    	/*	angular.forEach($scope.autoRenderedServicesFinalList, function (service, key) 
								{*/
					    			if(parseFloat(itemObj.patientClassPercentage) >= 0)
			    					{
			    						 var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
			    						 
			    						$scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);
			    						var conAmt =  ((parseFloat(totalAmt)*parseFloat(itemObj.patientClassPercentage))/100).toFixed(2);
			    						$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(conAmt);
			    						
			    						
				    			        $scope.autoRenderedServicesFinalList[idx].discount =((parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[idx].concession))*(isNaN(itemObj.discountPercentage) ? 0: itemObj.discountPercentage)/100).toFixed(2);
				    			       // discountAmtTmp = discountAmtTmp + parseFloat($scope.autoRenderedServicesFinalList[idx].discount);
				    			        $scope.autoRenderedServicesFinalList[idx].discountPercentage = parseFloat(itemObj.discountPercentage).toFixed(2);
				    			        var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[idx].concession) + parseFloat($scope.autoRenderedServicesFinalList[idx].discount));
				    			        
				    			    	var txpre = 0;
										if(itemObj.taxPercentage != null || itemObj.taxPercentage > 0){
											$scope.autoRenderedServicesFinalList[idx].taxPercentage = itemObj.taxPercentage;
											txpre = itemObj.taxPercentage;
										}else{
												$scope.autoRenderedServicesFinalList[idx].taxPercentage = 0;
												}
										$scope.autoRenderedServicesFinalList[idx].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
										$scope.autoRenderedServicesFinalList[idx].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[idx].taxAmt)).toFixed(2);
										$scope.autoRenderedServicesFinalList[idx].selfPayable = $scope.autoRenderedServicesFinalList[idx].netPay;
										$scope.autoRenderedServicesFinalList[idx].creditPayable =parseFloat(0);
			    						}
								    
									
									/*if(i == $scope.autoRenderedServicesFinalList.length){*/
										//$scope.saveOrderPackage();
										if($scope.requestStatusObject == null){
											/*if($scope.capExist==false && $scope.listPackageOrder.length==0)
									        {*/	
									        	$scope.autoRenderedServicesForSaveAndUpdate=[];
									        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
									        	$scope.addOrUpdateServices(idx);
									        /*}else
									        	$scope.saveOrderPackage();*/
										}else{
											if($scope.requestStatusObject.reqStatus == 1)
											{
													$scope.getCancelDiscountReqForDiscount();
													
													/*if($scope.capExist==false && $scope.listPackageOrder.length==0)
											        {*/	
											        	$scope.autoRenderedServicesForSaveAndUpdate=[];
											        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
											        	$scope.addOrUpdateServices(idx);
											        /*}else
											        	$scope.saveOrderPackage();*/
											}
											else{
												if(parseFloat($scope.requestStatusObject.approvedDiscAmt) > parseFloat(discountAmtTmp)){
													/*if($scope.capExist==false && $scope.listPackageOrder.length==0)
											        {*/	
											        	$scope.autoRenderedServicesForSaveAndUpdate=[];
											        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
											        	$scope.addOrUpdateServices(idx);
											        /*}else
											        	$scope.saveOrderPackage();*/
												}else{
													$scope.getCancelDiscountReqForDiscount();
													/*if($scope.capExist==false && $scope.listPackageOrder.length==0)
											        {*/	
											        	$scope.autoRenderedServicesForSaveAndUpdate=[];
											        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
											        	$scope.addOrUpdateServices(idx);
											        /*}else
											        	$scope.saveOrderPackage();*/
												}
											}
										}
										/*}
									i++;
								});*/
							}
					    	
					    	$scope.billWiseDiscountObj = {
									// "discountGivenBy":"",
									"discountGivenBy":$scope.userId,
									"discountCategoryId" : "",
									"discountAmountBillWise" : "",
									"discountPercentageBillWise" : "",
									"approvedAmountBillWise":"",
									"requestUserId":"",
									"requestAmountBillWise" : 0,
							}
							$scope.requestDiscountDiv = false;
					 	}
					}
				   $scope.lineLevelItem = false;
				   
				   
				   
				   $scope.getDiscountCalOnloadForWarningMsg = function(itemObj,idx)
				    {
					   
					   $scope.billWiseDiscountObj = {
								"discountGivenBy":$scope.userId,
								"discountCategoryId" : "",
								"discountAmountBillWise" : "",
								"discountPercentageBillWise" : "",
								"approvedAmountBillWise":"",
								"requestUserId":"",
								"requestAmountBillWise" : 0,
						}
						$scope.requestDiscountDiv = false;
				    	$scope.isFinancialClearence = 'N';
					   var i =0;
			    	   angular.forEach($scope.initialAutoRenderedServicesFinalList, function (value, key) {
				   			if(value.orderDetailsId == itemObj.orderDetailsId){
				   				idx = i;
				   			 }
			   			i++;
			   			});
			    	   $scope.autoRenderedServicesFinalList =[]; 
			    	   $scope.autoRenderedServicesFinalList = $scope.initialAutoRenderedServicesFinalList;
			    	   
			    		if($scope.userDiscountObject != null){
				    		
							if($scope.userDiscountObject.discountType == 1){
								
								var fromDisAmt = (parseFloat($scope.totalSumDisAmount) * parseFloat($scope.userDiscountObject.fromDiscountPer))/100;
								var toDisAmt = (parseFloat($scope.totalSumDisAmount) * parseFloat($scope.userDiscountObject.toDiscountPer))/100;
								$scope.userDiscountObject.fromDiscountAmount = fromDisAmt;
								$scope.userDiscountObject.toDiscountAmount = toDisAmt;
							}
				    	}
			    		
			    	   $scope.billWiseDiscountObj.requestAmountBillWise =0;
			    	   $scope.billWiseDiscountObj.discountAmountBillWise =0;
			    	   angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
			    		   
			    		   $scope.billWiseDiscountObj.discountAmountBillWise  += parseFloat(value.discount);
			    		   $scope.billWiseDiscountObj.requestAmountBillWise   += parseFloat(value.discount);
			    	   });
			    	   $scope.billWiseDiscountObj.discountAmountBillWise  = $scope.billWiseDiscountObj.discountAmountBillWise.toFixed();
			    	   $scope.billWiseDiscountObj.requestAmountBillWise  = $scope.billWiseDiscountObj.requestAmountBillWise.toFixed();
			    	   
			    	   $scope.funDiscountAmountBillWise();
			    	   var checkrightsAmount = false;
			    	   if($scope.userDiscountObject != null){
				    	   if((parseFloat($scope.billWiseDiscountObj.discountAmountBillWise) < parseFloat($scope.userDiscountObject.fromDiscountAmount))){
				    		   checkrightsAmount = true;
				    	   }
				    	   if((parseFloat($scope.billWiseDiscountObj.discountAmountBillWise) > parseFloat($scope.userDiscountObject.toDiscountAmount))){
				    		   checkrightsAmount = true;
				    	   }
				    	  }else{
				    		  checkrightsAmount = true;
				    	  }
			    	   if(checkrightsAmount == true){
				    		var URI = BASE_URL + ROOT_URL
				    		+ "/billing/getUsersListHavingDiscRights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/amt/"+$scope.billWiseDiscountObj.discountAmountBillWise+"/per/"+$scope.billWiseDiscountObj.discountPercentageBillWise;
				    BillingGenericService.serviceAction(METHOD_GET, URI,"",
				    				NOTIFICATION_MSG_STATUS_FALSE)
				    		.then(function(response) {
				    					//console.log(response);
				    					// $rootScope.startLoader();
				    					if (response.data.status == "success")
				    						{
				    						  $scope.userDiscountList =[];
				    						  $scope.userDiscountList = response.data.listObject;
				    						}
				    						
				    				});
				            $scope.lineLevelItem = true;
						   $scope.requestDiscountDiv = true;
						   $scope.isBillWiseDiscountApplied = true;
						   $scope.finalBill = 'N';
						   growl.warning("Please send discount request to user",{
								title : ERROR_MSG
							});
						   
				    	}else
				    		{
				    		 $scope.requestDiscountDiv = false;
				    		 $scope.billWiseDiscountObj = {
										"discountGivenBy":$scope.userId,
										"discountCategoryId" : "",
										"discountAmountBillWise" : "",
										"discountPercentageBillWise" : "",
										"approvedAmountBillWise":"",
										"requestUserId":"",
										"requestAmountBillWise" : 0,
								}
								$scope.requestDiscountDiv = false;
						    	$scope.isFinancialClearence = 'N';
				    		}
			    	   
				    	if(itemObj.discount == '' || itemObj.discount == undefined || itemObj.discount == '.'){
				    		itemObj.discount = 0;
				    		itemObj.discountPercentage = 0;
				    		$scope.autoRenderedServicesFinalList[idx].discount = 0;
				    		$scope.autoRenderedServicesFinalList[idx].discountPercentage = 0;
				    	}
				      	var totalServiceAmount = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
				      	
				    	if(parseFloat(itemObj.discount) > 0 && parseFloat(itemObj.discount) > parseFloat(totalServiceAmount))
						{
				            itemObj.discountCategoryId=$scope.discountCategoryList[0].id;
							$scope.autoRenderedServicesFinalList[idx].discount = parseFloat(0);
				    		$scope.autoRenderedServicesFinalList[idx].discountPercentage = parseFloat(0);
				    		
				    		$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(itemObj.concession);
							$scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);
				    		
				    		var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
					        $scope.autoRenderedServicesFinalList[idx].discountPercentage = isNaN((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100) ? parseFloat(0).toFixed(2) : (parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100;
					        $scope.autoRenderedServicesFinalList[idx].discount = parseFloat(itemObj.discount).toFixed(2);
					        //var withConDic =parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[idx].discount);
				            var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[idx].concession) + parseFloat($scope.autoRenderedServicesFinalList[idx].discount)+ parseFloat($scope.autoRenderedServicesFinalList[idx].packageConsumptionAmt));

					    	var txpre = 0;
							if(itemObj.taxPercentage != null || itemObj.taxPercentage > 0){
								$scope.autoRenderedServicesFinalList[idx].taxPercentage = itemObj.taxPercentage;
								txpre = itemObj.taxPercentage;
							}else{
									$scope.autoRenderedServicesFinalList[idx].taxPercentage = 0;
									}
							$scope.autoRenderedServicesFinalList[idx].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
							$scope.autoRenderedServicesFinalList[idx].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[idx].taxAmt)).toFixed(2);
							$scope.autoRenderedServicesFinalList[idx].selfPayable = $scope.autoRenderedServicesFinalList[idx].netPay;
							$scope.autoRenderedServicesFinalList[idx].creditPayable =parseFloat(0);
							
							$scope.autoRenderedServicesForSaveAndUpdate=[];
				        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
				        //	$scope.addOrUpdateServices(idx,null);
							
							//flagDis = false;
							growl.warning("You can't give discount amount more than service amount..",{
								title : ERROR_MSG
							});
							
							$scope.billWiseDiscountObj = {
									// "discountGivenBy":"",
									"discountGivenBy":$scope.userId,
									"discountCategoryId" : "",
									"discountAmountBillWise" : "",
									"discountPercentageBillWise" : "",
									"approvedAmountBillWise":"",
									"requestUserId":"",
									"requestAmountBillWise" : 0,
							}
							//$scope.requestDiscountDiv = false;
					    	$scope.isFinancialClearence = 'N';
							return false;
						}
				    	else
				    	{
				    		
				    		
					    	
				    		var i = 1;
				    		var discountAmtTmp = parseFloat($scope.totalDiscountSummary) - parseFloat(itemObj.oldDiscount) ;
				    		/*angular.forEach($scope.autoRenderedServicesFinalList, function (service, key) 
								{*/
		    					
		    					if(parseFloat(itemObj.discount) >= 0)
		    					{
		    						discountAmtTmp = discountAmtTmp + parseFloat(itemObj.discount);
		    						//$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(0);
		   						   // $scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(0);
		    						
		    						$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(itemObj.concession);
		   						    $scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);
		   						    
			    			        var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
			    			       // $scope.autoRenderedServicesFinalList[idx].discountPercentage = isNaN(((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100).toFixed(2)) ? parseFloat(0).toFixed(2) : ((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100).toFixed(2);
		                            $scope.autoRenderedServicesFinalList[idx].discountPercentage = isNaN((parseFloat(itemObj.discount)/((parseFloat(itemObj.quantity) * (parseFloat(itemObj.basePrice)  - parseFloat(itemObj.concession) )) ))*100) ? parseFloat(0).toFixed(2) : (parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * (parseFloat(itemObj.basePrice) - parseFloat(itemObj.concession) )))*100 ;

			    			        $scope.autoRenderedServicesFinalList[idx].discount = parseFloat(itemObj.discount).toFixed(2);
			    			        //var withConDic =parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[idx].discount);
		                            var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[idx].concession) + parseFloat($scope.autoRenderedServicesFinalList[idx].discount)+ parseFloat($scope.autoRenderedServicesFinalList[idx].packageConsumptionAmt));

			    			        
			    			    	var txpre = 0;
									if(itemObj.taxPercentage != null || itemObj.taxPercentage > 0){
										$scope.autoRenderedServicesFinalList[idx].taxPercentage = itemObj.taxPercentage;
										txpre = itemObj.taxPercentage;
									}else{
											$scope.autoRenderedServicesFinalList[idx].taxPercentage = 0;
											}
									$scope.autoRenderedServicesFinalList[idx].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
									$scope.autoRenderedServicesFinalList[idx].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[idx].taxAmt)).toFixed(2);
										/*$scope.autoRenderedServicesFinalList[idx].selfPayable = $scope.autoRenderedServicesFinalList[idx].netPay;
										$scope.autoRenderedServicesFinalList[idx].creditPayable =parseFloat(0);*/
		    						}
							    
								
								//if(i == $scope.autoRenderedServicesFinalList.length){
									if($scope.requestStatusObject == null){
										/*$scope.autoRenderedServicesForSaveAndUpdate=[];
							        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);*/
							        	//$scope.addOrUpdateServices(idx,null);
									}else{
										if($scope.requestStatusObject.reqStatus == 1){
												$scope.getCancelDiscountReqForDiscount();
												
												$scope.autoRenderedServicesForSaveAndUpdate=[];
									        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
									        	//$scope.addOrUpdateServices(idx,null);
										}
										else{
											if(parseFloat($scope.requestStatusObject.approvedDiscAmt) > parseFloat(discountAmtTmp)){
												$scope.autoRenderedServicesForSaveAndUpdate=[];
									        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
									        //	$scope.addOrUpdateServices(idx,null);
											}else{
												//$scope.getCancelDiscountReqForDiscount();
												
												$scope.autoRenderedServicesForSaveAndUpdate=[];
									        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
									        //	$scope.addOrUpdateServices(idx,null);
											}
										}
									}
									
									
								/*}
								i++;
						});*/
					
									}
				    	if(itemObj.discountPercentage==undefined || itemObj.discountPercentage==null || itemObj.discountPercentage==0)
			            {
			            itemObj.discountCategoryId=null;
			            }
				         }
				   
				   
				   
				   $scope.getDiscountCal = function(itemObj,idx)
				    {
					   
					   $scope.billWiseDiscountObj = {
								"discountGivenBy":$scope.userId,
								"discountCategoryId" : "",
								"discountAmountBillWise" : "",
								"discountPercentageBillWise" : "",
								"approvedAmountBillWise":"",
								"requestUserId":"",
								"requestAmountBillWise" : 0,
						}
						$scope.requestDiscountDiv = false;
				    	$scope.isFinancialClearence = 'N';
					   var i =0;
			    	   angular.forEach($scope.initialAutoRenderedServicesFinalList, function (value, key) {
				   			if(value.orderDetailsId == itemObj.orderDetailsId){
				   				idx = i;
				   			 }
			   			i++;
			   			});
			    	   $scope.autoRenderedServicesFinalList =[]; 
			    	   $scope.autoRenderedServicesFinalList = $scope.initialAutoRenderedServicesFinalList;
			    	   
			    		if($scope.userDiscountObject != null){
				    		
							if($scope.userDiscountObject.discountType == 1){
								
								var fromDisAmt = (parseFloat($scope.totalSumDisAmount) * parseFloat($scope.userDiscountObject.fromDiscountPer))/100;
								var toDisAmt = (parseFloat($scope.totalSumDisAmount) * parseFloat($scope.userDiscountObject.toDiscountPer))/100;
								$scope.userDiscountObject.fromDiscountAmount = fromDisAmt;
								$scope.userDiscountObject.toDiscountAmount = toDisAmt;
							}
				    	}
			    		
			    	   $scope.billWiseDiscountObj.requestAmountBillWise =0;
			    	   $scope.billWiseDiscountObj.discountAmountBillWise =0;
			    	   angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
			    		   
			    		   $scope.billWiseDiscountObj.discountAmountBillWise  += parseFloat(value.discount);
			    		   $scope.billWiseDiscountObj.requestAmountBillWise   += parseFloat(value.discount);
			    	   });
			    	   $scope.billWiseDiscountObj.discountAmountBillWise  = $scope.billWiseDiscountObj.discountAmountBillWise.toFixed();
			    	   $scope.billWiseDiscountObj.requestAmountBillWise  = $scope.billWiseDiscountObj.requestAmountBillWise.toFixed();
			    	   
			    	   $scope.funDiscountAmountBillWise();
			    	   var checkrightsAmount = false;
			    	   if($scope.userDiscountObject != null){
				    	   if((parseFloat($scope.billWiseDiscountObj.discountAmountBillWise) < parseFloat($scope.userDiscountObject.fromDiscountAmount))){
				    		   checkrightsAmount = true;
				    	   }
				    	   if((parseFloat($scope.billWiseDiscountObj.discountAmountBillWise) > parseFloat($scope.userDiscountObject.toDiscountAmount))){
				    		   checkrightsAmount = true;
				    	   }
				    	  }else{
				    		  checkrightsAmount = true;
				    	  }
			    	   if(checkrightsAmount == true){
				    		var URI = BASE_URL + ROOT_URL
				    		+ "/billing/getUsersListHavingDiscRights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/amt/"+$scope.billWiseDiscountObj.discountAmountBillWise+"/per/"+$scope.billWiseDiscountObj.discountPercentageBillWise;
				    BillingGenericService.serviceAction(METHOD_GET, URI,"",
				    				NOTIFICATION_MSG_STATUS_FALSE)
				    		.then(function(response) {
				    					//console.log(response);
				    					// $rootScope.startLoader();
				    					if (response.data.status == "success")
				    						{
				    						  $scope.userDiscountList =[];
				    						  $scope.userDiscountList = response.data.listObject;
				    						}
				    						
				    				});
				            $scope.lineLevelItem = true;
						   $scope.requestDiscountDiv = true;
						   $scope.isBillWiseDiscountApplied = true;
						   $scope.finalBill = 'N';
						   growl.warning("Please send discount request to user",{
								title : ERROR_MSG
							});
						   
				    	}else
				    		{
				    		 $scope.requestDiscountDiv = false;
				    		 $scope.billWiseDiscountObj = {
										"discountGivenBy":$scope.userId,
										"discountCategoryId" : "",
										"discountAmountBillWise" : "",
										"discountPercentageBillWise" : "",
										"approvedAmountBillWise":"",
										"requestUserId":"",
										"requestAmountBillWise" : 0,
								}
								$scope.requestDiscountDiv = false;
						    	$scope.isFinancialClearence = 'N';
				    		}
			    	   
				    	if(itemObj.discount == '' || itemObj.discount == undefined || itemObj.discount == '.'){
				    		itemObj.discount = 0;
				    		itemObj.discountPercentage = 0;
				    		$scope.autoRenderedServicesFinalList[idx].discount = 0;
				    		$scope.autoRenderedServicesFinalList[idx].discountPercentage = 0;
				    	}
				      	var totalServiceAmount = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
				      	
				    	if(parseFloat(itemObj.discount) > 0 && parseFloat(itemObj.discount) > parseFloat(totalServiceAmount))
						{
				            itemObj.discountCategoryId=$scope.discountCategoryList[0].id;
							$scope.autoRenderedServicesFinalList[idx].discount = parseFloat(0);
				    		$scope.autoRenderedServicesFinalList[idx].discountPercentage = parseFloat(0);
				    		
				    		$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(itemObj.concession);
							$scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);
				    		
				    		var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
					        $scope.autoRenderedServicesFinalList[idx].discountPercentage = isNaN((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100) ? parseFloat(0).toFixed(2) : (parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100;
					        $scope.autoRenderedServicesFinalList[idx].discount = parseFloat(itemObj.discount).toFixed(2);
					        //var withConDic =parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[idx].discount);
				            var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[idx].concession) + parseFloat($scope.autoRenderedServicesFinalList[idx].discount));

					    	var txpre = 0;
							if(itemObj.taxPercentage != null || itemObj.taxPercentage > 0){
								$scope.autoRenderedServicesFinalList[idx].taxPercentage = itemObj.taxPercentage;
								txpre = itemObj.taxPercentage;
							}else{
									$scope.autoRenderedServicesFinalList[idx].taxPercentage = 0;
									}
							$scope.autoRenderedServicesFinalList[idx].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
							$scope.autoRenderedServicesFinalList[idx].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[idx].taxAmt)).toFixed(2);
							$scope.autoRenderedServicesFinalList[idx].selfPayable = $scope.autoRenderedServicesFinalList[idx].netPay;
							$scope.autoRenderedServicesFinalList[idx].creditPayable =parseFloat(0);
							
							$scope.autoRenderedServicesForSaveAndUpdate=[];
				        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
				        	$scope.addOrUpdateServices(idx,null);
							
							//flagDis = false;
							growl.warning("You can't give discount amount more than service amount..",{
								title : ERROR_MSG
							});
							
							$scope.billWiseDiscountObj = {
									// "discountGivenBy":"",
									"discountGivenBy":$scope.userId,
									"discountCategoryId" : "",
									"discountAmountBillWise" : "",
									"discountPercentageBillWise" : "",
									"approvedAmountBillWise":"",
									"requestUserId":"",
									"requestAmountBillWise" : 0,
							}
							//$scope.requestDiscountDiv = false;
					    	$scope.isFinancialClearence = 'N';
							return false;
						}
				    	else
				    	{
				    		
				    		
					    	
				    		var i = 1;
				    		var discountAmtTmp = parseFloat($scope.totalDiscountSummary) - parseFloat(itemObj.oldDiscount) ;
				    		/*angular.forEach($scope.autoRenderedServicesFinalList, function (service, key) 
								{*/
		    					
		    					if(parseFloat(itemObj.discount) >= 0)
		    					{
		    						discountAmtTmp = discountAmtTmp + parseFloat(itemObj.discount);
		    						//$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(0);
		   						   // $scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(0);
		    						
		    						$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(itemObj.concession);
		   						    $scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);
		   						    
			    			        var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
			    			       // $scope.autoRenderedServicesFinalList[idx].discountPercentage = isNaN(((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100).toFixed(2)) ? parseFloat(0).toFixed(2) : ((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100).toFixed(2);
		                            $scope.autoRenderedServicesFinalList[idx].discountPercentage = isNaN((parseFloat(itemObj.discount)/((parseFloat(itemObj.quantity) * (parseFloat(itemObj.basePrice)  - parseFloat(itemObj.concession) )) ))*100) ? parseFloat(0).toFixed(2) : (parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * (parseFloat(itemObj.basePrice) - parseFloat(itemObj.concession) )))*100 ;

			    			        $scope.autoRenderedServicesFinalList[idx].discount = parseFloat(itemObj.discount).toFixed(2);
			    			        //var withConDic =parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[idx].discount);
		                            var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[idx].concession) + parseFloat($scope.autoRenderedServicesFinalList[idx].discount));

			    			        
			    			    	var txpre = 0;
									if(itemObj.taxPercentage != null || itemObj.taxPercentage > 0){
										$scope.autoRenderedServicesFinalList[idx].taxPercentage = itemObj.taxPercentage;
										txpre = itemObj.taxPercentage;
									}else{
											$scope.autoRenderedServicesFinalList[idx].taxPercentage = 0;
											}
									$scope.autoRenderedServicesFinalList[idx].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
									$scope.autoRenderedServicesFinalList[idx].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[idx].taxAmt)).toFixed(2);
									$scope.autoRenderedServicesFinalList[idx].selfPayable = $scope.autoRenderedServicesFinalList[idx].netPay;
									$scope.autoRenderedServicesFinalList[idx].creditPayable =parseFloat(0);
		    						}
							    
								
								//if(i == $scope.autoRenderedServicesFinalList.length){
									if($scope.requestStatusObject == null){
										$scope.autoRenderedServicesForSaveAndUpdate=[];
							        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
							        	$scope.addOrUpdateServices(idx,null);
									}else{
										if($scope.requestStatusObject.reqStatus == 1){
												$scope.getCancelDiscountReqForDiscount();
												
												$scope.autoRenderedServicesForSaveAndUpdate=[];
									        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
									        	$scope.addOrUpdateServices(idx,null);
										}
										else{
											if(parseFloat($scope.requestStatusObject.approvedDiscAmt) > parseFloat(discountAmtTmp)){
												$scope.autoRenderedServicesForSaveAndUpdate=[];
									        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
									        	$scope.addOrUpdateServices(idx,null);
											}else{
												$scope.getCancelDiscountReqForDiscount();
												
												$scope.autoRenderedServicesForSaveAndUpdate=[];
									        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
									        	$scope.addOrUpdateServices(idx,null);
											}
										}
									}
									
									
								/*}
								i++;
						});*/
					
									}
				    	if(itemObj.discountPercentage==undefined || itemObj.discountPercentage==null || itemObj.discountPercentage==0)
			            {
			            itemObj.discountCategoryId=null;
			            }
				         }
				    
			    
			    $scope.getDiscountListForInfo  = function(itemObj,idx)
			    {
			    	//console.log(itemObj);
			    	$scope.disPerPopup = '';
					$scope.disAmtPopup = '';
					$scope.disCategoryId = '';
					
			    	$scope.serciveNamePop = itemObj.serviceStandardName;
			    	$scope.totalAmtPopup =(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
			    	var totalAmtPopup = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
			    	$scope.totalDiscountAmtPopup = ((parseFloat(totalAmtPopup) - parseFloat(itemObj.concession))*itemObj.discountPercentage/100).toFixed(2);
			    	
			    	$scope.discountDetailsArrayListPopup = itemObj.discountDetailsList;
			    	
			    	$scope.objectIndex = idx;
			    }
			    $scope.getDiscountList  = function(itemObj,idx)
			    {
			    	$scope.disPerPopup = '';
					$scope.disAmtPopup = '';
					$scope.disCategoryId = '';
					
			    	$scope.serciveNamePop = itemObj.serviceStandardName;
			    	$scope.totalAmtPopup =(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
			    	var totalAmtPopup = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
			    	$scope.totalDiscountAmtPopup = ((parseFloat(totalAmtPopup) - parseFloat(itemObj.concession))*itemObj.discountPercentage/100).toFixed(2);
			    	
			    	$scope.discountDetailsArrayListPopup = itemObj.discountDetailsList;
			    	
			    	$scope.objectIndex = idx;
			    	//alert($scope.objectIndex);
			    	//alert();
			    }
			    $scope.getSpecialityByDoctorForPackage = function(packageIndex,docId,idx)
				{
					var specDocObj = {
				    			unitId:$scope.unitId,
				    			organizationId:$scope.organizationId,
				    			doctorId:docId
				    }
					var specialityLsit=[];
					angular.forEach($scope.getInitiallySpecialityList, function (service, key) 
							{
								if(service.doctorId==docId)
									{
									specialityLsit.push(service);
									}
							});
					 if(specialityLsit.length == 0){
						 $scope.selectedPackageList[packageIndex].packageServiceList[idx].doctorId ="";
			                growl.warning("Selected Doctor Department and Sub Department not mapped in the unit masters !! ",{
			                     title : "WARNING"
			                 });
			                 return false; 
			         }
					 
					 
					$scope.selectedPackageList[packageIndex].packageServiceList[idx].docSpecialityList = specialityLsit;
				 if($scope.selectedPackageList[packageIndex].packageServiceList[idx].doctorSpecialityId==undefined || $scope.selectedPackageList[packageIndex].packageServiceList[idx].doctorSpecialityId==null || $scope.selectedPackageList[packageIndex].packageServiceList[idx].doctorSpecialityId=="" || $scope.selectedPackageList[packageIndex].packageServiceList[idx].docSpecialityList[0].specialityId!=$scope.selectedPackageList[packageIndex].packageServiceList[idx].doctorSpecialityId)
			     {
					 $scope.selectedPackageList[packageIndex].packageServiceList[idx].doctorSpecialityId = specialityLsit[0].specialityId;
			     }
				}
			    $scope.getDisPerPopup = function(disPer)
			    {
			    	var idx  = $scope.objectIndex;
			    	//alert(idx);
			    	//alert($scope.autoRenderedServicesFinalList[idx].totalAmt);
			    	var totalAmtPopup = $scope.autoRenderedServicesFinalList[idx].totalAmt;
			    	var concessionPopup = $scope.autoRenderedServicesFinalList[idx].concession;
			    	var calDicAmt = ((parseFloat(totalAmtPopup) - parseFloat(concessionPopup))*disPer/100).toFixed(2);	
					$scope.disAmtPopup = calDicAmt;
					
			    }
			    
			    $scope.getDisAmtPopup = function(disAmt)
			    {
			    	var idx  = $scope.objectIndex;
			    	var totalAmtPopup = $scope.autoRenderedServicesFinalList[idx].totalAmt;
			    	var concessionPopup = $scope.autoRenderedServicesFinalList[idx].concession;
					var disPer = ((parseFloat(disAmt)/(totalAmtPopup - (parseFloat(concessionPopup))))*100).toFixed(2);
					$scope.disPerPopup = disPer;
					
			    }
			    $scope.addDiscountInDiscountList = function()
			    {
			    	if($scope.disPerPopup == '' || $scope.disAmtPopup == '')
			    	{
			    		growl.warning("Please Enter Discount",{
							title : ERROR_MSG
						});
			    	}
			    	if($scope.disPerPopup !== ''  && $scope.disCategoryId == '')
			    	{
			    		growl.warning("Please Select Discount Category",{
							title : ERROR_MSG
						});
			    	}
			    	if($scope.disAmtPopup !== ''  && $scope.disCategoryId == '')
			    	{
			    		growl.warning("Please Select Discount Category",{
							title : ERROR_MSG
						});
			    	}
			    	var idx  = $scope.objectIndex;
			    	//console.log(idx);
			    	//console.log("hhh",$scope.autoRenderedServicesFinalList[idx]);
			    	//alert($scope.disPerPopup);
			    	
			    	var disCatName = $.grep($scope.discountCategoryList, function (disCat) {
			            return disCat.id == $scope.disCategoryId;
			        })[0].type;
			    	
			    	var popupData = {
			    			"discountPercentage":$scope.disPerPopup,
			    			"discountAmount":(parseFloat($scope.disAmtPopup)).toFixed(2),
			    			"discountCategory":disCatName,
			    			"discountCategoryId":$scope.disCategoryId,
			    			"createdBy" : $scope.userId,
							"createdDate" :new Date().getTime()
			    			
			    	}
			    	
			    	$scope.autoRenderedServicesFinalList[idx].discountDetailsList.push(popupData);
			    	var autoRenderedServicesList = $scope.autoRenderedServicesFinalList;
			    	//$scope.getDiscountList();
			    	//console.log("ff",$scope.autoRenderedServicesFinalList);
			    	$scope.calculationTest(autoRenderedServicesList,init);
			    	
			    	$scope.getDiscountListPopup($scope.autoRenderedServicesFinalList,idx);
			    	
			    	$scope.disPerPopup = '';
					$scope.disAmtPopup = '';
					$scope.disCategoryId = '';
			    }
			    
			    $scope.groupWiseCheckAll= function(chkFlg) {
					$scope.disPerPopupForGroup = '';
					$scope.disAmtPopupForGroup = '';
					$scope.disCategoryIdForGroup = '';
					$scope.disPerPopupForGroupFull = 0;
					if(chkFlg == 'Y'){
						$scope.groupWiseServicesSelectedList.services = $scope.groupWiseDiscountServicesList.map(function(item) { return item.orderDetailsId; });
						}else{
							$scope.groupWiseDisFlag = true;
							$scope.groupWiseServicesSelectedList.services = [];
						}
			 };
			
			    
			 $scope.funDiscountPercentageBillWise = function()
			    {
			    	$scope.totalSumDisAmount = parseFloat(0);
					var i = 1;
					angular.forEach($scope.autoRenderedServicesFinalList,function(value,index)
							 {
						        if(value.status == 'A' && value.isDiscountApplicable == 'Y'  && value.serviceMasterId != $scope.surchargeServiceId)
						        	{
				    					
						        	var totalAmt = ((parseFloat(value.quantity) * parseFloat(value.basePrice))- parseFloat(value.concession)).toFixed(2);
				    					$scope.totalSumDisAmount =parseFloat($scope.totalSumDisAmount) + parseFloat(totalAmt);
				    					
						        	  
						        	}
						         if(i == $scope.autoRenderedServicesFinalList.length)
						        	{
						        		var totalDisAmount =(((parseFloat($scope.totalSumDisAmount))*(parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise)/100))).toFixed(2);
							    		if(totalDisAmount > 0){
							    		$scope.billWiseDiscountObj.discountPercentageBillWise = parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise).toFixed(2);
							    		$scope.billWiseDiscountObj.discountAmountBillWise = totalDisAmount;
							    		
							    		if($scope.totalSumDisAmount > 0){

							    		    if($scope.billWiseDiscountObj.discountPercentageBillWise != undefined && parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise) > parseFloat(100)){
							    		        growl.warning("You can only give discount upto 100%",{
                                                    title : WARNING_MSG
                                                });
                                                $scope.billWiseDiscountObj.discountAmountBillWise=0;
                                                $scope.billWiseDiscountObj.discountPercentageBillWise=0;
							    		    }
							    			if($scope.billWiseDiscountObj.discountAmountBillWise > $scope.totalSumDisAmount){
											growl.warning("You can give discount on only "+$scope.totalSumDisAmount,{
												title : WARNING_MSG
											});
											$scope.billWiseDiscountObj.discountAmountBillWise=0;
											$scope.billWiseDiscountObj.discountPercentageBillWise=0;
									
							    			}
											}else{
													growl.warning("You have no service for discount ",{
														title : WARNING_MSG
													});
													$scope.billWiseDiscountObj.discountAmountBillWise=0;
													$scope.billWiseDiscountObj.discountPercentageBillWise=0;
											}
							    		
							    		 }
							    		else
							    			{
								    			$scope.billWiseDiscountObj.discountPercentageBillWise =0;
									    		$scope.billWiseDiscountObj.discountAmountBillWise =0;

									    		if($scope.billWiseDiscountObj.discountPercentageBillWise != undefined && parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise) > parseFloat(100)){
                                                    growl.warning("You can only give discount upto 100%",{
                                                        title : WARNING_MSG
                                                    });
                                                    $scope.billWiseDiscountObj.discountAmountBillWise=0;
                                                    $scope.billWiseDiscountObj.discountPercentageBillWise=0;
                                                }
									    		if($scope.totalSumDisAmount > 0){
									    			if($scope.billWiseDiscountObj.discountAmountBillWise > $scope.totalSumDisAmount){
													growl.warning("You can give discount only of amount "+$scope.totalSumDisAmount,{
														title : WARNING_MSG
													});
													$scope.billWiseDiscountObj.discountAmountBillWise=0;
													$scope.billWiseDiscountObj.discountPercentageBillWise=0;
									    			}
													}else{
															growl.warning("You have no service for discount ",{
																title : WARNING_MSG
															});
															$scope.billWiseDiscountObj.discountAmountBillWise=0;
															$scope.billWiseDiscountObj.discountPercentageBillWise=0;
													}
							    			}
						        	}
								i++;	
							 });
			    }
			 
			 
			 $scope.getCancelDiscountReqForDiscount = function()
				{
					
					
					$rootScope.startLoaderPopup();
					var data = {
							reqDiscId :$scope.requestStatusObject.reqDiscId,
					}
					
					var URI = BASE_URL + ROOT_URL + "/api/billing/cancelRequest";
					
					GenericService
					.serviceAction("POST", URI, data)
					.then(function(response) {
								$rootScope.stopLoaderPopup();
								if (response.data.status == "success") {
								
									$scope.requestApporivedDiscountDiv = false;
									$scope.discountDisabledFlagForRequest = false;
									$scope.requestAppUser = 0;
									$scope.approvedAmtApp = 0;
									$scope.requestAmtApp =0;
									$scope.discTypeDesc = "";
									$scope.statusApp = "";
									$scope.discountRadio = 2; 
									
									$scope.billWiseDiscountObj = {
											// "discountGivenBy":"",
											"discountGivenBy":$scope.userId,
											"discountCategoryId" : "",
											"discountAmountBillWise" : "",
											"discountPercentageBillWise" : "",
											"approvedAmountBillWise":"",
											"requestUserId":"",
											"requestAmountBillWise" : 0,
									}
									 $scope.requestStatusObject  = null;
						    		angular.element('#discountCancellationModalReq').modal('hide');
								}
							});
					
				}
			 
			 $scope.funDiscountAmountBillWise = function()
			    {
			    	$scope.totalSumDisAmount = parseFloat(0);
					var i = 1;
					angular.forEach($scope.autoRenderedServicesFinalList,function(value,index)
						 {
					        if(value.status == 'A' && value.isDiscountApplicable == 'Y' && value.serviceMasterId != $scope.surchargeServiceId)
				        	{
					        	var totalAmt = ((parseFloat(value.quantity) * parseFloat(value.basePrice))- parseFloat(value.concession)).toFixed(2);
					        	$scope.totalSumDisAmount =parseFloat($scope.totalSumDisAmount) + parseFloat(totalAmt);
				        	}
					         if(i == $scope.autoRenderedServicesFinalList.length)
					        	{
					        	 var totalDisPercentage =((parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)/(parseFloat($scope.totalSumDisAmount)))*100).toFixed(2);
						    		if(totalDisPercentage >= 0)
						    		{
							    		$scope.billWiseDiscountObj.discountPercentageBillWise = totalDisPercentage;
							    		$scope.billWiseDiscountObj.discountAmountBillWise = parseFloat($scope.billWiseDiscountObj.discountAmountBillWise).toFixed(2);
							    		if(Number($scope.billWiseDiscountObj.discountPercentageBillWise)>100)
										{
											if($scope.totalSumDisAmount > 0)
											{
												if($scope.billWiseDiscountObj.discountAmountBillWise > $scope.totalSumDisAmount)
												{
													growl.warning("You can give discount only of amount "+$scope.totalSumDisAmount,{
														title : WARNING_MSG
													});
													$scope.billWiseDiscountObj.discountAmountBillWise=0;
													$scope.billWiseDiscountObj.discountPercentageBillWise=0;
												}
											}else{
												/*growl.warning("You have no service for discount ",{
													title : WARNING_MSG
												});*/
												$scope.billWiseDiscountObj.discountAmountBillWise=0;
												$scope.billWiseDiscountObj.discountPercentageBillWise=0;
											}
											return false;
										}
						    		}else{
									
										if($scope.totalSumDisAmount > 0){
											if($scope.billWiseDiscountObj.discountAmountBillWise > $scope.totalSumDisAmount){
										growl.warning("You can give discount only of amount "+$scope.totalSumDisAmount,{
											title : WARNING_MSG
										});
										$scope.billWiseDiscountObj.discountAmountBillWise=0;
										$scope.billWiseDiscountObj.discountPercentageBillWise=0;
									
											}
										}else{
												growl.warning("You have no service for discount ",{
													title : WARNING_MSG
												});
												$scope.billWiseDiscountObj.discountAmountBillWise=0;
												$scope.billWiseDiscountObj.discountPercentageBillWise=0;
										}
							    		return false;
						    		}
					        	}
							i++;	
						 });
			    }
			 
			 $scope.checkPackageBalance = false;
			 $scope.checkPackageBalanceAmount = function()
				{
				 $scope.checkPackageBalance = true;
					$rootScope.startLoaderPopup();
					var data = {
							admissionId :$scope.admissionId,
					}
					
					var URI = BASE_URL + ROOT_URL + "/api/billing/checkCapWithPackageBalanceAmount";
					
					GenericService
					.serviceAction("POST", URI, data)
					.then(function(response) {
						$scope.checkPackageBalance = false;
								$rootScope.stopLoaderPopup();
								if (response.data.status == "success") {
									
									if (response.data.object == true) {
										
										angular.element('#checkPackageBalancePopup').modal('show');
									}
								}
							});
					
				}
			    
			    $scope.checkIntermidiatBill = function()
			    {
			    	
			    	if($scope.isIntermediateBill=='Y')
					{
				    		 $scope.checkPackageBalanceAmount();
			    		
						$scope.finalBill = "N";
						$scope.isFinancialClearence="N";
						$scope.interimBill = "N";
					}else{				
						$scope.finalBill = "N";
						$scope.isFinancialClearence="N"
						$scope.interimBill = "N";
					}
			    }
			    
			     	
			    
			    $scope.checkFinancialClearance = function() {
			    	$scope.billRemark = $scope.admissionDetails.remark;
					if($scope.isFinancialClearence=='Y')
						{
							if(($scope.userDiscountObject != null &&  parseFloat($scope.userDiscountObject.fromFinancialClearanceAmount) <= $scope.netPayableSummary && parseFloat($scope.userDiscountObject.toFinancialClearanceAmount) >=  $scope.netPayableSummary) )
					    	{
								 $scope.financialFlag = 'Y';
					    	}else{
					    		$scope.financialFlag = 'N';
					    		$scope.isFinancialClearence ='Y'
					    			
					    			if($scope.userDiscountObject != null){
					    			growl.warning("User have rights for approving financial clearance from "+$scope.userDiscountObject.fromFinancialClearanceAmount+" to "+$scope.userDiscountObject.toFinancialClearanceAmount+"..!!",{
										title : WARNING_MSG
									});
					    			}
					    	}
						    $scope.getUsersForFinancialClearanceRights();
							$scope.finalBill = 'Y';
							$scope.isIntermediateBill = "N";
							$scope.interimBill = "N";
						}else{				
							$scope.isIntermediateBill = "N";
							$scope.finalBill = 'N';
							$scope.interimBill = "N";
						}
					}
			    
			    $scope.checkIntermBill = function(){
			    	
			    	if($scope.interimBill == 'Y'){
			    		$scope.checkPackageBalanceAmount();
			    	}
			    }
			    
			    $scope.checkFinalBill = function() {
					if($scope.finalBill=='Y')
						{
						$scope.checkPackageBalanceAmount();
							$scope.isIntermediateBill = "N";
							$scope.interimBill = "N";
						}else{	
							$scope.isFinancialClearence = "N";
						}
					}
			    $scope.generatePrintInterim = function()
				{
					if($scope.interimBill == 'Y')
						{
						console.log("1",$scope.packaged.withPackageDetail);
						console.log("2",$scope.packaged.withRatePackageDetail);
						$scope.billRemark = $scope.admissionDetails.remark;
						//START
						//return false
						var data={
							
								patientId :$scope.admissionDetails.patientId,
								//encounterId : $scope.encounterDetails.admissionId,
								admissionId : $scope.admissionDetails.admissionId,
								createdBy :$scope.userId,
								createdDate :new Date().getTime(),
								updatedBy :$scope.userId,
								updatedDate :new Date().getTime(),
								orderDate :new Date().getTime(),
								organisationId :$scope.organizationId,
								unitId :$scope.unitId,
								isFinalBill : $scope.finalBill,
								isInterimBill : $scope.interimBill,
								isIntermediateBill :$scope.isIntermediateBill,
								ordDoctorId :$scope.admissionDetails.doctorId,
								visitTypeId : $scope.visitTypeId,
								billDateTime : new Date().getTime(),
								netAmount : parseFloat(parseFloat($scope.netPayableSummary)).toFixed(2),
								discountAmount : $scope.totalDiscountSummary,
								concessionAmount : $scope.totalConcessionSummary,
								gst : $scope.totalGstTaxSummary,
								totalBillAmount : $scope.totalAmountSummary,
								roundOff : $scope.roundOffAmt,
								isCreditBill:'N',
								billCancelled : 'N',
								billRemark : $scope.billRemark,
								billStatusId : '1', // 1. Pending,
								selfPayable : $scope.totalSelfPayble,
								creditPayable : $scope.totalCreditPayble,
								cancelledDiscountsList : $scope.removeDiscountList,
								tariffId : $scope.defaultSelfTariffId,
								paymentEntitlementId : $scope.admissionDetails.paymentEntitlementId,
								visitTypeId : $scope.visitTypeId,
								isFinancialClearance : $scope.isFinancialClearence,
								financialRequestSendTo:$scope.finanacial.financialRequestSendTo,
								patientTypeId : $scope.admissionDetails.patientCategoryId,//default op
								billingBedCategoryId : $scope.admissionDetails.bedBillingCategoryId, 
								isInterimOrFinalBill : $scope.interimBill == 'Y' ? 1 : $scope.finalBill == 'Y' ? 2 : null
						};
						 if($scope.isBillWiseDiscountApplied == true)
							{
								data.isDiscountApplied = 'Y';
							}
						
						if($scope.requestStatusObject !=null && ($scope.requestStatusObject.reqStatus == 2 || $scope.requestStatusObject.reqStatus == 1))
						{
							data.reqDiscId = $scope.requestStatusObject.reqDiscId;
						}
					
					//for DD
					if($scope.billWiseDiscountObj.requestAmountBillWise > 0){
						data.reqDisAmt = $scope.billWiseDiscountObj.requestAmountBillWise;
						data.requestSentTo = $scope.billWiseDiscountObj.requestUserId;
						data.discType = $scope.billWiseDiscountObj.discountCategoryId;
					}
					//FOR DD
						
						var billPayeeList =[];
						if($scope.totalSelfPayble > 0)
							{
								var billPayeeArr ={
										payeeId : $scope.selfPayeeId,
										billAmount : $scope.totalSelfPayble
								}
								billPayeeList.push(billPayeeArr);
							}
						if($scope.totalCreditPayble > 0)
							{
								var billPayeeArr ={
										payeeId : $scope.admissionDetails.payeeId,
										billAmount : $scope.totalCreditPayble
								}
								billPayeeList.push(billPayeeArr);
							}
						
						data.billPayeeDetailsDtosList = billPayeeList;
						
						var saveOrderDetailsObj = [];
						angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
							var saveArr = {};
							if(value.orderDetailsId > 0){
								saveArr.orderDetailsId = value.orderDetailsId;
							}
							
							saveArr.markupStatus = value.markupStatus;
							saveArr.patientClassPercentage = value.patientClassPercentage;
							saveArr.orgId = $scope.organizationId;
							saveArr.orgUnitId  = $scope.unitId;
							saveArr.orderId = value.orderId;
							saveArr.orderQty = value.quantity;
							saveArr.serviceId = value.serviceMasterId;
							saveArr.priorityId = value.priorityId;
							saveArr.isOutsourced = value.isOutsourced;
							saveArr.orderDate = new Date().getTime();
							saveArr.orderSchDate = new Date().getTime();
							saveArr.serviceAmt = value.basePrice;
							saveArr.ordConcession = value.concession;
							saveArr.ordDiscount = value.discount;
							saveArr.netAmount = value.netPay;
							saveArr.ordDocSplId = value.specialityId;
							saveArr.taxId = value.taxId;
							saveArr.taxPer  = value.taxPercentage;
							saveArr.taxAmount = value.taxAmt;
							saveArr.selfPayable = value.selfPayable;
							saveArr.creditPayable = value.creditPayable;
							saveArr.payeeId = $scope.admissionDetails.payeeId;
							saveArr.ordTotalAmt =value.totalAmt;
							saveArr.tariffId = $scope.defaultSelfTariffId;
							saveArr.billingClassId = '1';
							saveArr.ordDocId = value.doctorId;
							saveArr.ordDocSplId = value.doctorSpecialityId;
							saveArr.serviceRendered = value.serviceRendered;
							saveArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
							saveArr.ordRenderDatetime = value.ordRenderDatetime;
							saveArr.serviceIsBilled = '0';
							saveArr.status = value.status;
							saveArr.updatedBy = $scope.userId;
							saveArr.updatedDate =new Date().getTime();
							saveArr.createdBy  = value.createdBy;
							saveArr.createdDate  = value.createdDate;
							saveArr.oldNetPay =  parseFloat(value.oldNetPay).toFixed(2);
							saveArr.ordCancelled = value.ordCancelled;
							
							saveArr.ordCancelReasonId  = value.ordCancelReasonId;
							saveArr.ordCancelDatetime  = value.ordCancelDatetime;
							saveArr.ordCancelRemark  = value.ordCancelRemark;
							saveArr.oldOrdDtlId = value.oldOrdDtlId;
							
							//Ganesh
							saveArr.oldCreditPayable = value.creditPayableOldC;
							saveArr.oldSelfPayable = value.selfPayableOldC;
							saveArr.isInvestigationOrder = value.isInvestigationOrder;
							
							saveArr.orderEmergencyFlag = 'N';
							saveArr.isDrug = 'N';
							saveArr.isPackage = typeof(value.isPackage) != "undefined" ? value.isPackage : false;
							saveArr.packageId = typeof(value.packageMasterId) != "undefined" && value.packageMasterId != null ? value.packageMasterId : value.packageId;
							saveArr.packageTypeId = typeof(value.packageTypeId) != "undefined" ? value.packageTypeId : null;
							saveArr.isPackageService = typeof(value.isPackageService) != "undefined" ? value.isPackageService : 'N';
							saveArr.apportionedPrice = typeof(value.apportionedPrice) != "undefined" ? value.apportionedPrice : null;
							saveArr.isCapService = value.isCapService;
							saveArr.packageConsumptionAmt = value.packageConsumptionAmt;
							saveArr.departmentId = value.specialityId;
							saveArr.subDepartmentId = value.subSpecialityId;
							saveArr.encounterId = $scope.encounterDetails.encounterId;
							saveArr.admissionId = $scope.admissionDetails.admissionId,
							saveArr.contractId = value.contractId;
							saveArr.discountDetailsList = [];
							saveArr.serviceAmt =  value.basePrice;
							saveArr.isRefDoctorShare = value.isRefDoctorShare;
							saveArr.discountDetailsList = [];
							saveArr.debitNoteService = false;
							saveArr.orderDetailsPayeeMapperDtosList = value.orderDetailsPayeeMapperDtosList;
							saveArr.orderDetailsPackageMapperDtosList = value.orderDetailsPackageMapperDtosList;
							saveArr.bedCategoryId = value.bedCategoryId;
							saveArr.wardId = value.wardId;
					        saveArr.roomId = value.roomId;
					        saveArr.bedId = value.bedId;
							if(value.discountDetailsList == null)
								{
								value.discountDetailsList = [];
								}
							
							if(Object.keys(value.discountDetailsList).length > 0)
								{
								angular.forEach(value.discountDetailsList, function (value1, key1) {
									var discounntArr = {};
									if(value1.orderDetailsDiscountId > 0)
										{
											discounntArr.orderDetailsDiscountId = value1.orderDetailsDiscountId;
											discounntArr.orderDetailsId  = value1.orderDetailsId;
										}
									discounntArr.discountCategoryId = value1.discountCategoryId;
									discounntArr.discountPercentage = value1.discountPercentage;
									discounntArr.discountAmount  = value1.discountAmount;
									discounntArr.discountDate = new Date().getTime();
									discounntArr.status = 'A';
									discounntArr.updatedBy = $scope.userId;
									discounntArr.updatedDate =new Date().getTime();
									discounntArr.createdBy  = value1.createdBy;
									discounntArr.createdDate  = value1.createdDate;
									saveArr.discountDetailsList.push(discounntArr);
								});
								
								}
							else
								{
									if(value.discount > 0)
										{
											var discounntArr = {};
											
											discounntArr.discountCategoryId = value.discountCategoryId;
											discounntArr.discountPercentage = value.discountPercentage;
											discounntArr.discountAmount  = value.discount;
											discounntArr.discountDate = new Date().getTime();
											discounntArr.status = 'A';
											discounntArr.updatedBy = $scope.userId;
											discounntArr.updatedDate =new Date().getTime();
											discounntArr.createdDate = new Date().getTime();
											discounntArr.createdBy = $scope.userId;
											saveArr.discountDetailsList.push(discounntArr);
									
										}
								}
							//for DD
							if($scope.billWiseDiscountObj.requestAmountBillWise > 0)
								{
								  var calDicAmt = ((parseFloat(value.totalAmt) - parseFloat(value.concession))*$scope.billWiseDiscountObj.discountPercentageBillWise/100).toFixed(2);	
								  saveArr.reqDisAmt  = calDicAmt;
								}
						//For DD
							saveOrderDetailsObj.push(saveArr);
						});
						data.orderDetailsMasterDtosList = saveOrderDetailsObj;
						data.cancelOrderDetailsList = $scope.removeServicesList;
						
						//console.log("finalSaveData",data);
						

						
						//console.log("finalSaveData",data);
						//console.log(JSON.stringify(data));
						
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL + "/api/billing/intrimBill";
						BillingGenericService.serviceAction(METHOD_POST,URI, data , NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
							////console.log(response)
							if(response.data.status == 'success'){
								
								//alert($scope.admissionId);
								if($scope.groupService==1){
									
									
									var data1 =  {
							                 "appName": "HIS",
							                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
							                 "reportDesignFilePath": "IpdIntriamBill.rpt",
							                 "reportFormat": "PDF",
							                 "exportFileName": "IPDIntrimBill",
							                 "reportParams": [
										                {
											                  "paramName": "@admisssion_id",
											                  "paramType": "Integer",
											                  "paramValue": $scope.admissionId
										                },
										                {
											                  "paramName": "@patient_id",
											                  "paramType": "Integer",
											                  "paramValue": $scope.patientDetails.patientId
										                },
										                {
											                  "paramName": "IsSummary",
											                  "paramType": "String",
											                  "paramValue": "Y"
										                }
					                 ]
									};
									 reportService.generate(REPORT_API_URI1,data1);
									
									if($scope.packaged.withPackageDetail == 'Y'){
										var data2 =  {
								                 "appName": "HIS",
								                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
								                 "reportDesignFilePath": "IpdIntriamBill_PackagewithoutRate.rpt",
								                 "reportFormat": "PDF",
								                 "exportFileName": "IPDIntrimBill",
								                 "reportParams": [
											                {
												                  "paramName": "@admisssion_id",
												                  "paramType": "Integer",
												                  "paramValue": $scope.admissionId
											                },
											                {
												                  "paramName": "@patient_id",
												                  "paramType": "Integer",
												                  "paramValue": $scope.patientDetails.patientId
											                },
											                {
												                  "paramName": "IsSummary",
												                  "paramType": "String",
												                  "paramValue": "Y"
											                }
						                 ]
										};
										 reportService.generate(REPORT_API_URI1,data2);
									}
									
									if($scope.packaged.withRatePackageDetail == 'Y'){
										var data3 =  {
								                 "appName": "HIS",
								                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
								                 "reportDesignFilePath": "IpdIntriamBill_PackagewithRate.rpt",
								                 "reportFormat": "PDF",
								                 "exportFileName": "IPDIntrimBill",
								                 "reportParams": [
											                {
												                  "paramName": "@admisssion_id",
												                  "paramType": "Integer",
												                  "paramValue": $scope.admissionId
											                },
											                {
												                  "paramName": "@patient_id",
												                  "paramType": "Integer",
												                  "paramValue": $scope.patientDetails.patientId
											                },
											                {
												                  "paramName": "IsSummary",
												                  "paramType": "String",
												                  "paramValue": "Y"
											                }
						                 ]
										};
										 reportService.generate(REPORT_API_URI1,data3);
									}
								}else{
										var data4 =  {
							                 "appName": "HIS",
							                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
							                 "reportDesignFilePath": "IpdIntriamBill.rpt",
							                 "reportFormat": "PDF",
							                 "exportFileName": "IPDIntrimBill",
							                 "reportParams": [
									                {
										                  "paramName": "@admisssion_id",
										                  "paramType": "Integer",
										                  "paramValue": $scope.admissionId
									                },
									                {
										                  "paramName": "@patient_id",
										                  "paramType": "Integer",
										                  "paramValue": $scope.patientDetails.patientId
									                },
									                {
										                  "paramName": "IsSummary",
										                  "paramType": "String",
										                  "paramValue": "N"
									                }
									                ]
										};
										 reportService.generate(REPORT_API_URI1,data4);
										 
										if($scope.packaged.withPackageDetail == 'Y'){
											var data5 =  {
									                 "appName": "HIS",
									                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
									                 "reportDesignFilePath": "IpdIntriamBill_PackagewithoutRate.rpt",
									                 "reportFormat": "PDF",
									                 "exportFileName": "IPDIntrimBill",
									                 "reportParams": [
												                {
													                  "paramName": "@admisssion_id",
													                  "paramType": "Integer",
													                  "paramValue": $scope.admissionId
												                },
												                {
													                  "paramName": "@patient_id",
													                  "paramType": "Integer",
													                  "paramValue": $scope.patientDetails.patientId
												                },
												                {
													                  "paramName": "IsSummary",
													                  "paramType": "String",
													                  "paramValue": "N"
												                }
							                 ]
											};
											 reportService.generate(REPORT_API_URI1,data5);
										}
										
										if($scope.packaged.withRatePackageDetail == 'Y'){
											var data6 =  {
									                 "appName": "HIS",
									                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
									                 "reportDesignFilePath": "IpdIntriamBill_PackagewithRate.rpt",
									                 "reportFormat": "PDF",
									                 "exportFileName": "IPDIntrimBill",
									                 "reportParams": [
												                {
													                  "paramName": "@admisssion_id",
													                  "paramType": "Integer",
													                  "paramValue": $scope.admissionId
												                },
												                {
													                  "paramName": "@patient_id",
													                  "paramType": "Integer",
													                  "paramValue": $scope.patientDetails.patientId
												                },
												                {
													                  "paramName": "IsSummary",
													                  "paramType": "String",
													                  "paramValue": "N"
												                }
							                 ]
											};
											 reportService.generate(REPORT_API_URI1,data6);
										}
									}
								  
							   
							   //New Interim bill report for Pharmacy bill
							   if($scope.medicationIssueList != null && $scope.medicationIssueList.length > 0){
							   var data =  {
							  			
						                 "appName": "HIS",
						                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
						                 "reportDesignFilePath": "IPDSaleforInterim.rpt",
						                 "reportFormat": "PDF",
						                 "exportFileName": "InterimBillReciept",
						                 "reportParams": [
						                	 {
								                  "paramName": "intrim_bill_id",
								                  "paramType": "Integer",
								                  "paramValue": response.data.object.object
							                },
							                {
								                  "paramName": "admission_id",
								                  "paramType": "Integer",
								                  "paramValue": $scope.admissionDetails.admissionId
							                }
					                 ]
									};
								reportService.generate(REPORT_API_URI1,data);
							   }
								$rootScope.stopLoader();
							}else{
								
								$scope.searchKeyword = {
										serviceStandardName:""
								}
								$scope.searchKeyword1 = {
										orderDateStringNew:""
								}
								$scope.searchKeyword2 = {
										ordRenderDatetimeString:""
								}
								$scope.checkKeywordLength($scope.searchKeyword.serviceStandardName,$scope.searchKeyword.serviceStandardName,$scope.searchKeyword2.ordRenderDatetimeString);
								
								getOrderedServices();
								$scope.getActivePackagesList();
								self.model.getInterimBillsByAdmissionId($scope.admissionDetails.admissionId);
								
					    		return false;
							}
							
						});
									
								
									
						//END
						var data={
								
								patientId :$scope.admissionDetails.patientId,
								//encounterId : $scope.encounterDetails.admissionId,
								admissionId : $scope.admissionDetails.admissionId,
								createdBy :$scope.userId,
								createdDate :new Date().getTime(),
								updatedBy :$scope.userId,
								updatedDate :new Date().getTime(),
								orderDate :new Date().getTime(),
								organisationId :$scope.organizationId,
								unitId :$scope.unitId,
								ordDoctorId :$scope.admissionDetails.doctorId,
								visitTypeId : $scope.visitTypeId,
								billDateTime : new Date().getTime(),
								netAmount : parseFloat(parseFloat($scope.netPayableSummary)).toFixed(2),
								discountAmount : $scope.totalDiscountSummary,
								concessionAmount : $scope.totalConcessionSummary,
								gst : $scope.totalGstTaxSummary,
								totalBillAmount : $scope.totalAmountSummary,
								roundOff : $scope.roundOffAmt,
								billRemark : $scope.billRemark,
								selfPayable : $scope.totalSelfPayble,
								creditPayable : $scope.totalCreditPayble,
								tariffId : $scope.defaultSelfTariffId,
								paymentEntitlementId : $scope.admissionDetails.paymentEntitlementId,
								patientTypeId : $scope.admissionDetails.patientCategoryId,//default op
								billingBedCategoryId : $scope.admissionDetails.bedBillingCategoryId
						};
						var saveOrderDetailsObj = [];
						angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
							var saveArr = {};
							if(value.orderDetailsId > 0){
								saveArr.orderDetailsId = value.orderDetailsId;
							}
							
							saveArr.markupStatus = value.markupStatus;
							saveArr.patientClassPercentage = value.patientClassPercentage;
							saveArr.orgId = $scope.organizationId;
							saveArr.orgUnitId  = $scope.unitId;
							saveArr.orderId = value.orderId;
							saveArr.orderQty = value.quantity;
							saveArr.serviceId = value.serviceMasterId;
							saveArr.priorityId = value.priorityId;
							saveArr.isOutsourced = value.isOutsourced;
							saveArr.orderDate = new Date().getTime();
							saveArr.orderSchDate = new Date().getTime();
							saveArr.serviceAmt = value.basePrice;
							saveArr.ordConcession = value.concession;
							saveArr.ordDiscount = value.discount;
							saveArr.netAmount = value.netPay;
							saveArr.ordDocSplId = value.specialityId;
							saveArr.taxId = value.taxId;
							saveArr.taxPer  = value.taxPercentage;
							saveArr.taxAmount = value.taxAmt;
							saveArr.selfPayable = value.selfPayable;
							saveArr.creditPayable = value.creditPayable;
							saveArr.payeeId = $scope.admissionDetails.payeeId;
							saveArr.ordTotalAmt =value.totalAmt;
							saveArr.tariffId = $scope.defaultSelfTariffId;
							saveArr.billingClassId = '1';
							saveArr.ordDocId = value.doctorId;
							saveArr.ordDocSplId = value.doctorSpecialityId;
							saveArr.serviceRendered = value.serviceRendered;
							saveArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
							saveArr.ordRenderDatetime = value.ordRenderDatetime;
							saveArr.serviceIsBilled = '0';
							saveArr.status = value.status;
							saveArr.updatedBy = $scope.userId;
							saveArr.updatedDate =new Date().getTime();
							saveArr.createdBy  = value.createdBy;
							saveArr.createdDate  = value.createdDate;
							saveArr.oldNetPay =  parseFloat(value.oldNetPay).toFixed(2);
							saveArr.ordCancelled = value.ordCancelled;
							
							saveArr.ordCancelReasonId  = value.ordCancelReasonId;
							saveArr.ordCancelDatetime  = value.ordCancelDatetime;
							saveArr.ordCancelRemark  = value.ordCancelRemark;
							saveArr.oldOrdDtlId = value.oldOrdDtlId;
							
							//Ganesh
							saveArr.oldCreditPayable = value.creditPayableOldC;
							saveArr.oldSelfPayable = value.selfPayableOldC;
							saveArr.isInvestigationOrder = value.isInvestigationOrder;
							
							saveArr.orderEmergencyFlag = 'N';
							saveArr.isDrug = 'N';
							saveArr.isPackage = typeof(value.isPackage) != "undefined" ? value.isPackage : false;
							saveArr.packageId = typeof(value.packageMasterId) != "undefined" && value.packageMasterId != null ? value.packageMasterId : value.packageId;
							saveArr.packageTypeId = typeof(value.packageTypeId) != "undefined" ? value.packageTypeId : null;
							saveArr.isPackageService = typeof(value.isPackageService) != "undefined" ? value.isPackageService : 'N';
							saveArr.apportionedPrice = typeof(value.apportionedPrice) != "undefined" ? value.apportionedPrice : null;
							saveArr.isCapService = value.isCapService;
							saveArr.packageConsumptionAmt = value.packageConsumptionAmt;
							saveArr.departmentId = value.specialityId;
							saveArr.subDepartmentId = value.subSpecialityId;
							saveArr.encounterId = $scope.encounterDetails.encounterId;
							saveArr.admissionId = $scope.admissionDetails.admissionId,
							saveArr.contractId = value.contractId;
							saveArr.discountDetailsList = [];
							saveArr.serviceAmt =   value.basePrice;
							saveArr.isRefDoctorShare = value.isRefDoctorShare;
							saveArr.discountDetailsList = [];
							saveArr.debitNoteService = false;
							saveOrderDetailsObj.push(saveArr);
						});
						data.orderDetailsMasterDtosList = saveOrderDetailsObj;
						
						
						}
				}
			    $scope.generateDetailPrintInterim=function(){
			    	$scope.billRemark = $scope.admissionDetails.remark;
					if($scope.interimBill == 'Y')
						{
						
						//START
						//return false;
						var data={
							
								patientId :$scope.admissionDetails.patientId,
								//encounterId : $scope.encounterDetails.admissionId,
								admissionId : $scope.admissionDetails.admissionId,
								createdBy :$scope.userId,
								createdDate :new Date().getTime(),
								updatedBy :$scope.userId,
								updatedDate :new Date().getTime(),
								orderDate :new Date().getTime(),
								organisationId :$scope.organizationId,
								unitId :$scope.unitId,
								isFinalBill : $scope.finalBill,
								isInterimBill : $scope.interimBill,
								isIntermediateBill :$scope.isIntermediateBill,
								ordDoctorId :$scope.admissionDetails.doctorId,
								visitTypeId : $scope.visitTypeId,
								billDateTime : new Date().getTime(),
								netAmount : parseFloat(parseFloat($scope.netPayableSummary)).toFixed(2),
								discountAmount : $scope.totalDiscountSummary,
								concessionAmount : $scope.totalConcessionSummary,
								gst : $scope.totalGstTaxSummary,
								totalBillAmount : $scope.totalAmountSummary,
								roundOff : $scope.roundOffAmt,
								isCreditBill:'N',
								billCancelled : 'N',
								billRemark : $scope.billRemark,
								billStatusId : '1', // 1. Pending,
								selfPayable : $scope.totalSelfPayble,
								creditPayable : $scope.totalCreditPayble,
								cancelledDiscountsList : $scope.removeDiscountList,
								tariffId : $scope.defaultSelfTariffId,
								paymentEntitlementId : $scope.admissionDetails.paymentEntitlementId,
								visitTypeId : $scope.visitTypeId,
								isFinancialClearance : $scope.isFinancialClearence,
								financialRequestSendTo:$scope.finanacial.financialRequestSendTo,
								patientTypeId : $scope.admissionDetails.patientCategoryId,//default op
								billingBedCategoryId : $scope.admissionDetails.bedBillingCategoryId, 
								isInterimOrFinalBill : $scope.interimBill == 'Y' ? 1 : $scope.finalBill == 'Y' ? 2 : null
						};
						 if($scope.isBillWiseDiscountApplied == true)
							{
								data.isDiscountApplied = 'Y';
							}
						
						if($scope.requestStatusObject !=null && ($scope.requestStatusObject.reqStatus == 2 || $scope.requestStatusObject.reqStatus == 1))
						{
							data.reqDiscId = $scope.requestStatusObject.reqDiscId;
						}
					
					//for DD
					if($scope.billWiseDiscountObj.requestAmountBillWise > 0){
						data.reqDisAmt = $scope.billWiseDiscountObj.requestAmountBillWise;
						data.requestSentTo = $scope.billWiseDiscountObj.requestUserId;
						data.discType = $scope.billWiseDiscountObj.discountCategoryId;
					}
					//FOR DD
						
						var billPayeeList =[];
						if($scope.totalSelfPayble > 0)
							{
								var billPayeeArr ={
										payeeId : $scope.selfPayeeId,
										billAmount : $scope.totalSelfPayble
								}
								billPayeeList.push(billPayeeArr);
							}
						if($scope.totalCreditPayble > 0)
							{
								var billPayeeArr ={
										payeeId : $scope.admissionDetails.payeeId,
										billAmount : $scope.totalCreditPayble
								}
								billPayeeList.push(billPayeeArr);
							}
						
						data.billPayeeDetailsDtosList = billPayeeList;
						
						var saveOrderDetailsObj = [];
						angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
							var saveArr = {};
							if(value.orderDetailsId > 0){
								saveArr.orderDetailsId = value.orderDetailsId;
							}
							
							saveArr.markupStatus = value.markupStatus;
							saveArr.patientClassPercentage = value.patientClassPercentage;
							saveArr.orgId = $scope.organizationId;
							saveArr.orgUnitId  = $scope.unitId;
							saveArr.orderId = value.orderId;
							saveArr.orderQty = value.quantity;
							saveArr.serviceId = value.serviceMasterId;
							saveArr.priorityId = value.priorityId;
							saveArr.isOutsourced = value.isOutsourced;
							saveArr.orderDate = new Date().getTime();
							saveArr.orderSchDate = new Date().getTime();
							saveArr.serviceAmt = value.basePrice;
							saveArr.ordConcession = value.concession;
							saveArr.ordDiscount = value.discount;
							saveArr.netAmount = value.netPay;
							saveArr.ordDocSplId = value.specialityId;
							saveArr.taxId = value.taxId;
							saveArr.taxPer  = value.taxPercentage;
							saveArr.taxAmount = value.taxAmt;
							saveArr.selfPayable = value.selfPayable;
							saveArr.creditPayable = value.creditPayable;
							saveArr.payeeId = $scope.admissionDetails.payeeId;
							saveArr.ordTotalAmt =value.totalAmt;
							saveArr.tariffId = $scope.defaultSelfTariffId;
							saveArr.billingClassId = '1';
							saveArr.ordDocId = value.doctorId;
							saveArr.ordDocSplId = value.doctorSpecialityId;
							saveArr.serviceRendered = value.serviceRendered;
							saveArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
							saveArr.ordRenderDatetime = value.ordRenderDatetime;
							saveArr.serviceIsBilled = '0';
							saveArr.status = value.status;
							saveArr.updatedBy = $scope.userId;
							saveArr.updatedDate =new Date().getTime();
							saveArr.createdBy  = value.createdBy;
							saveArr.createdDate  = value.createdDate;
							saveArr.oldNetPay =  parseFloat(value.oldNetPay).toFixed(2);
							saveArr.ordCancelled = value.ordCancelled;
							
							saveArr.ordCancelReasonId  = value.ordCancelReasonId;
							saveArr.ordCancelDatetime  = value.ordCancelDatetime;
							saveArr.ordCancelRemark  = value.ordCancelRemark;
							saveArr.oldOrdDtlId = value.oldOrdDtlId;
							
							//Ganesh
							saveArr.oldCreditPayable = value.creditPayableOldC;
							saveArr.oldSelfPayable = value.selfPayableOldC;
							saveArr.isInvestigationOrder = value.isInvestigationOrder;
							
							saveArr.orderEmergencyFlag = 'N';
							saveArr.isDrug = 'N';
							saveArr.isPackage = typeof(value.isPackage) != "undefined" ? value.isPackage : false;
							saveArr.packageId = typeof(value.packageMasterId) != "undefined" && value.packageMasterId != null ? value.packageMasterId : value.packageId;
							saveArr.packageTypeId = typeof(value.packageTypeId) != "undefined" ? value.packageTypeId : null;
							saveArr.isPackageService = typeof(value.isPackageService) != "undefined" ? value.isPackageService : 'N';
							saveArr.apportionedPrice = typeof(value.apportionedPrice) != "undefined" ? value.apportionedPrice : null;
							saveArr.isCapService = value.isCapService;
							saveArr.packageConsumptionAmt = value.packageConsumptionAmt;
							saveArr.departmentId = value.specialityId;
							saveArr.subDepartmentId = value.subSpecialityId;
							saveArr.encounterId = $scope.encounterDetails.encounterId;
							saveArr.admissionId = $scope.admissionDetails.admissionId,
							saveArr.contractId = value.contractId;
							saveArr.discountDetailsList = [];
							saveArr.serviceAmt =  value.basePrice;
							saveArr.isRefDoctorShare = value.isRefDoctorShare;
							saveArr.discountDetailsList = [];
							saveArr.debitNoteService = false;
							saveArr.orderDetailsPayeeMapperDtosList = value.orderDetailsPayeeMapperDtosList;
							saveArr.orderDetailsPackageMapperDtosList = value.orderDetailsPackageMapperDtosList;
							saveArr.bedCategoryId = value.bedCategoryId;
							saveArr.wardId = value.wardId;
					        saveArr.roomId = value.roomId;
					        saveArr.bedId = value.bedId;
							if(value.discountDetailsList == null)
								{
								value.discountDetailsList = [];
								}
							
							if(Object.keys(value.discountDetailsList).length > 0)
								{
								angular.forEach(value.discountDetailsList, function (value1, key1) {
									var discounntArr = {};
									if(value1.orderDetailsDiscountId > 0)
										{
											discounntArr.orderDetailsDiscountId = value1.orderDetailsDiscountId;
											discounntArr.orderDetailsId  = value1.orderDetailsId;
										}
									discounntArr.discountCategoryId = value1.discountCategoryId;
									discounntArr.discountPercentage = value1.discountPercentage;
									discounntArr.discountAmount  = value1.discountAmount;
									discounntArr.discountDate = new Date().getTime();
									discounntArr.status = 'A';
									discounntArr.updatedBy = $scope.userId;
									discounntArr.updatedDate =new Date().getTime();
									discounntArr.createdBy  = value1.createdBy;
									discounntArr.createdDate  = value1.createdDate;
									saveArr.discountDetailsList.push(discounntArr);
								});
								
								}
							else
								{
									if(value.discount > 0)
										{
											var discounntArr = {};
											
											discounntArr.discountCategoryId = value.discountCategoryId;
											discounntArr.discountPercentage = value.discountPercentage;
											discounntArr.discountAmount  = value.discount;
											discounntArr.discountDate = new Date().getTime();
											discounntArr.status = 'A';
											discounntArr.updatedBy = $scope.userId;
											discounntArr.updatedDate =new Date().getTime();
											discounntArr.createdDate = new Date().getTime();
											discounntArr.createdBy = $scope.userId;
											saveArr.discountDetailsList.push(discounntArr);
									
										}
								}
							//for DD
							if($scope.billWiseDiscountObj.requestAmountBillWise > 0)
								{
								  var calDicAmt = ((parseFloat(value.totalAmt) - parseFloat(value.concession))*$scope.billWiseDiscountObj.discountPercentageBillWise/100).toFixed(2);	
								  saveArr.reqDisAmt  = calDicAmt;
								}
						//For DD
							saveOrderDetailsObj.push(saveArr);
						});
						data.orderDetailsMasterDtosList = saveOrderDetailsObj;
						data.cancelOrderDetailsList = $scope.removeServicesList;
						
						//console.log("finalSaveData",data);
						

						
						//console.log("finalSaveData",data);
						//console.log(JSON.stringify(data));
						
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL + "/api/billing/intrimBill";
						BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
							////console.log(response)
							if(response.data.status == 'success'){

								
								//alert($scope.admissionId);
								if($scope.groupService==1){
									var data1 =  {
							                 "appName": "HIS",
							                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
							                 "reportDesignFilePath": "IpdIntriamBill_Details.rpt",
							                 "reportFormat": "PDF",
							                 "exportFileName": "IPDIntrimBill",
							                 "reportParams": [
										                {
											                  "paramName": "@admisssion_id",
											                  "paramType": "Integer",
											                  "paramValue": $scope.admissionId
										                },
										                {
											                  "paramName": "@patient_id",
											                  "paramType": "Integer",
											                  "paramValue": $scope.patientDetails.patientId
										                },
										                {
											                  "paramName": "IsSummary",
											                  "paramType": "String",
											                  "paramValue": "Y"
										                }
					                 ]
									};
									  reportService.generate(REPORT_API_URI1,data1);
									  
									if($scope.packaged.withPackageDetail == 'Y'){
										var data5 =  {
								                 "appName": "HIS",
								                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
								                 "reportDesignFilePath": "IpdIntriamBill_PackagewithoutRate_detail.rpt",
								                 "reportFormat": "PDF",
								                 "exportFileName": "IPDIntrimBill",
								                 "reportParams": [
											                {
												                  "paramName": "@admisssion_id",
												                  "paramType": "Integer",
												                  "paramValue": $scope.admissionId
											                },
											                {
												                  "paramName": "@patient_id",
												                  "paramType": "Integer",
												                  "paramValue": $scope.patientDetails.patientId
											                },
											                {
												                  "paramName": "IsSummary",
												                  "paramType": "String",
												                  "paramValue": "Y"
											                }
						                 ]
										};
										 reportService.generate(REPORT_API_URI1,data5);
									}
									
									if($scope.packaged.withRatePackageDetail == 'Y'){
										var data6 =  {
								                 "appName": "HIS",
								                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
								                 "reportDesignFilePath": "IpdIntriamBill_PackagewithRate_detail.rpt",
								                 "reportFormat": "PDF",
								                 "exportFileName": "IPDIntrimBill",
								                 "reportParams": [
											                {
												                  "paramName": "@admisssion_id",
												                  "paramType": "Integer",
												                  "paramValue": $scope.admissionId
											                },
											                {
												                  "paramName": "@patient_id",
												                  "paramType": "Integer",
												                  "paramValue": $scope.patientDetails.patientId
											                },
											                {
												                  "paramName": "IsSummary",
												                  "paramType": "String",
												                  "paramValue": "Y"
											                }
						                 ]
										};
										 reportService.generate(REPORT_API_URI1,data6);
									}
									
								}else{
										var data1 =  {
							                 "appName": "HIS",
							                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
							                 "reportDesignFilePath": "IpdIntriamBill_Details.rpt",
							                 "reportFormat": "PDF",
							                 "exportFileName": "IPDIntrimBill",
							                 "reportParams": [
									                {
										                  "paramName": "@admisssion_id",
										                  "paramType": "Integer",
										                  "paramValue": $scope.admissionId
									                },
									                {
										                  "paramName": "@patient_id",
										                  "paramType": "Integer",
										                  "paramValue": $scope.patientDetails.patientId
									                },
									                {
										                  "paramName": "IsSummary",
										                  "paramType": "String",
										                  "paramValue": "N"
									                }
									                ]
										};
										  reportService.generate(REPORT_API_URI1,data1);
										  
										if($scope.packaged.withPackageDetail == 'Y'){
											var data5 =  {
									                 "appName": "HIS",
									                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
									                 "reportDesignFilePath": "IpdIntriamBill_PackagewithoutRate_detail.rpt",
									                 "reportFormat": "PDF",
									                 "exportFileName": "IPDIntrimBill",
									                 "reportParams": [
												                {
													                  "paramName": "@admisssion_id",
													                  "paramType": "Integer",
													                  "paramValue": $scope.admissionId
												                },
												                {
													                  "paramName": "@patient_id",
													                  "paramType": "Integer",
													                  "paramValue": $scope.patientDetails.patientId
												                },
												                {
													                  "paramName": "IsSummary",
													                  "paramType": "String",
													                  "paramValue": "N"
												                }
							                 ]
											};
											 reportService.generate(REPORT_API_URI1,data5);
										}
										
										if($scope.packaged.withRatePackageDetail == 'Y'){
											var data6 =  {
									                 "appName": "HIS",
									                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
									                 "reportDesignFilePath": "IpdIntriamBill_PackagewithRate_detail.rpt",
									                 "reportFormat": "PDF",
									                 "exportFileName": "IPDIntrimBill",
									                 "reportParams": [
												                {
													                  "paramName": "@admisssion_id",
													                  "paramType": "Integer",
													                  "paramValue": $scope.admissionId
												                },
												                {
													                  "paramName": "@patient_id",
													                  "paramType": "Integer",
													                  "paramValue": $scope.patientDetails.patientId
												                },
												                {
													                  "paramName": "IsSummary",
													                  "paramType": "String",
													                  "paramValue": "N"
												                }
							                 ]
											};
											 reportService.generate(REPORT_API_URI1,data6);
										}
									}
								  
							 
							   
							   
							   
							   //New Interim bill report for Pharmacy bill
							   if($scope.medicationIssueList != null && $scope.medicationIssueList.length > 0){
							   var data =  {
							  			
						                 "appName": "HIS",
						                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
						                 "reportDesignFilePath": "IPDSaleforInterim.rpt",
						                 "reportFormat": "PDF",
						                 "exportFileName": "InterimBillReciept",
						                 "reportParams": [
						                	 {
								                  "paramName": "intrim_bill_id",
								                  "paramType": "Integer",
								                  "paramValue": response.data.object.object
							                },
							                {
								                  "paramName": "admission_id",
								                  "paramType": "Integer",
								                  "paramValue": $scope.admissionDetails.admissionId
							                }
					                 ]
									};
								reportService.generate(REPORT_API_URI1,data);
							   }
								$rootScope.stopLoader();
								
							}else{
								
								$scope.searchKeyword = {
										serviceStandardName:""
								}
								$scope.searchKeyword1 = {
										orderDateStringNew:""
								}
								$scope.searchKeyword2 = {
										ordRenderDatetimeString:""
								}
								
								$scope.checkKeywordLength($scope.searchKeyword.serviceStandardName,$scope.searchKeyword.serviceStandardName,$scope.searchKeyword2.ordRenderDatetimeString);
								getOrderedServices();
								$scope.getActivePackagesList();
								self.model.getInterimBillsByAdmissionId($scope.admissionDetails.admissionId);
								
					    		return false;
							}
							
						});
									
								
									
						//END
						
						var data={
								
								patientId :$scope.admissionDetails.patientId,
								//encounterId : $scope.encounterDetails.admissionId,
								admissionId : $scope.admissionDetails.admissionId,
								createdBy :$scope.userId,
								createdDate :new Date().getTime(),
								updatedBy :$scope.userId,
								updatedDate :new Date().getTime(),
								orderDate :new Date().getTime(),
								organisationId :$scope.organizationId,
								unitId :$scope.unitId,
								ordDoctorId :$scope.admissionDetails.doctorId,
								visitTypeId : $scope.visitTypeId,
								billDateTime : new Date().getTime(),
								netAmount : parseFloat(parseFloat($scope.netPayableSummary)).toFixed(2),
								discountAmount : $scope.totalDiscountSummary,
								concessionAmount : $scope.totalConcessionSummary,
								gst : $scope.totalGstTaxSummary,
								totalBillAmount : $scope.totalAmountSummary,
								roundOff : $scope.roundOffAmt,
								billRemark : $scope.billRemark,
								selfPayable : $scope.totalSelfPayble,
								creditPayable : $scope.totalCreditPayble,
								tariffId : $scope.defaultSelfTariffId,
								paymentEntitlementId : $scope.admissionDetails.paymentEntitlementId,
								patientTypeId : $scope.admissionDetails.patientCategoryId,//default op
								billingBedCategoryId : $scope.admissionDetails.bedBillingCategoryId
						};
						var saveOrderDetailsObj = [];
						angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
							var saveArr = {};
							if(value.orderDetailsId > 0){
								saveArr.orderDetailsId = value.orderDetailsId;
							}
							
							saveArr.markupStatus = value.markupStatus;
							saveArr.patientClassPercentage = value.patientClassPercentage;
							saveArr.orgId = $scope.organizationId;
							saveArr.orgUnitId  = $scope.unitId;
							saveArr.orderId = value.orderId;
							saveArr.orderQty = value.quantity;
							saveArr.serviceId = value.serviceMasterId;
							saveArr.priorityId = value.priorityId;
							saveArr.isOutsourced = value.isOutsourced;
							saveArr.orderDate = new Date().getTime();
							saveArr.orderSchDate = new Date().getTime();
							saveArr.serviceAmt = value.basePrice;
							saveArr.ordConcession = value.concession;
							saveArr.ordDiscount = value.discount;
							saveArr.netAmount = value.netPay;
							saveArr.ordDocSplId = value.specialityId;
							saveArr.taxId = value.taxId;
							saveArr.taxPer  = value.taxPercentage;
							saveArr.taxAmount = value.taxAmt;
							saveArr.selfPayable = value.selfPayable;
							saveArr.creditPayable = value.creditPayable;
							saveArr.payeeId = $scope.admissionDetails.payeeId;
							saveArr.ordTotalAmt =value.totalAmt;
							saveArr.tariffId = $scope.defaultSelfTariffId;
							saveArr.billingClassId = '1';
							saveArr.ordDocId = value.doctorId;
							saveArr.ordDocSplId = value.doctorSpecialityId;
							saveArr.serviceRendered = value.serviceRendered;
							saveArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
							saveArr.ordRenderDatetime = value.ordRenderDatetime;
							saveArr.serviceIsBilled = '0';
							saveArr.status = value.status;
							saveArr.updatedBy = $scope.userId;
							saveArr.updatedDate =new Date().getTime();
							saveArr.createdBy  = value.createdBy;
							saveArr.createdDate  = value.createdDate;
							saveArr.oldNetPay =  parseFloat(value.oldNetPay).toFixed(2);
							saveArr.ordCancelled = value.ordCancelled;
							
							saveArr.ordCancelReasonId  = value.ordCancelReasonId;
							saveArr.ordCancelDatetime  = value.ordCancelDatetime;
							saveArr.ordCancelRemark  = value.ordCancelRemark;
							saveArr.oldOrdDtlId = value.oldOrdDtlId;
							
							//Ganesh
							saveArr.oldCreditPayable = value.creditPayableOldC;
							saveArr.oldSelfPayable = value.selfPayableOldC;
							saveArr.isInvestigationOrder = value.isInvestigationOrder;
							
							saveArr.orderEmergencyFlag = 'N';
							saveArr.isDrug = 'N';
							saveArr.isPackage = typeof(value.isPackage) != "undefined" ? value.isPackage : false;
							saveArr.packageId = typeof(value.packageMasterId) != "undefined" && value.packageMasterId != null ? value.packageMasterId : value.packageId;
							saveArr.packageTypeId = typeof(value.packageTypeId) != "undefined" ? value.packageTypeId : null;
							saveArr.isPackageService = typeof(value.isPackageService) != "undefined" ? value.isPackageService : 'N';
							saveArr.apportionedPrice = typeof(value.apportionedPrice) != "undefined" ? value.apportionedPrice : null;
							saveArr.isCapService = value.isCapService;
							saveArr.packageConsumptionAmt = value.packageConsumptionAmt;
							saveArr.departmentId = value.specialityId;
							saveArr.subDepartmentId = value.subSpecialityId;
							saveArr.encounterId = $scope.encounterDetails.encounterId;
							saveArr.admissionId = $scope.admissionDetails.admissionId,
							saveArr.contractId = value.contractId;
							saveArr.discountDetailsList = [];
							saveArr.serviceAmt =   value.basePrice;
							saveArr.isRefDoctorShare = value.isRefDoctorShare;
							saveArr.discountDetailsList = [];
							saveArr.debitNoteService = false;
							saveOrderDetailsObj.push(saveArr);
						});
						data.orderDetailsMasterDtosList = saveOrderDetailsObj;
						
						
						}
				
		       }
			    $("#interimBillCheckbox" ).click(function() {
					/*if($scope.interimBill=='Y')
						{
							$("#savedisablebtn").removeClass("disabled");
							$("#savedisablebtn").addClass("enabled");
						}else{				
							
							$("#savedisablebtn").removeClass("enabled");
							$("#savedisablebtn").addClass("disabled");
						}*/
					});
			    $scope.getApplyDiscountPopup = function()
			    {
			    	
			    	if($scope.billWiseDiscountObj.discountPercentageBillWise === '')
			    		{
				    		growl.warning("Discount Percentage is required.",{
								title : ERROR_MSG
							});
				    		return false;
			    			
			    		}

			    	else if($scope.billWiseDiscountObj.discountAmountBillWise === '')
			    		{
				    		growl.warning("Discount Amount is required.",{
								title : ERROR_MSG
							});
				    		return false;
			    		}
			    	else if($scope.billWiseDiscountObj.discountAmountBillWise >= 0 && $scope.billWiseDiscountObj.discountPercentageBillWise >= 0 && $scope.billWiseDiscountObj.discountCategoryId === '')
		    		{
			    		growl.warning("Discount Category is required.",{
							title : ERROR_MSG
						});
			    		return false;
		    		}
		    	
		    	else
		    		{
		    		if($scope.billWiseDiscountObj.discountAmountBillWise >= 0 && $scope.billWiseDiscountObj.discountPercentageBillWise >= 0 && $scope.billWiseDiscountObj.discountCategoryId > 0)
		    		{
		    			//if($scope.totalSumDisAmount > 0)
		    			angular.element('#applyDiscountPopup').modal('show');
		    		}
		    		}
			    }
			    
			    $scope.applyDiscount  = function()
			    {
			    	$scope.isFinancialClearence = 'N';
			    	var indexList =[];
			    	//if(parseFloat($scope.totalDisServiceWise) > parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)){
					    	$scope.listOfOldDisAmt = [];
					    	$scope.isBillWiseDiscountApplied = true;
					    	$scope.autoRenderedServicesForSaveAndUpdate=[];
					    	if($scope.userDiscountObject != null){
					    		
								if($scope.userDiscountObject.discountType == 1){
									
									var fromDisAmt = (parseFloat($scope.totalSumDisAmount) * parseFloat($scope.userDiscountObject.fromDiscountPer))/100;
									var toDisAmt = (parseFloat($scope.totalSumDisAmount) * parseFloat($scope.userDiscountObject.toDiscountPer))/100;
									$scope.userDiscountObject.fromDiscountAmount = fromDisAmt;
									$scope.userDiscountObject.toDiscountAmount = toDisAmt;
								}
					    	}
					    	
					    	
					    	if(($scope.requestStatusObject == null) && ($scope.userDiscountObject == null || parseFloat($scope.userDiscountObject.fromDiscountAmount) < $scope.billWiseDiscountObj.discountAmountBillWise && parseFloat($scope.userDiscountObject.toDiscountAmount) < $scope.billWiseDiscountObj.discountAmountBillWise) )
					    	{
					    	   $scope.billWiseDiscountObj.requestAmountBillWise = $scope.billWiseDiscountObj.discountAmountBillWise;
					    	   var i =0;
					    	   angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
					   			if(value.status == 'A' && value.isDiscountApplicable == 'Y'  && value.serviceMasterId != $scope.surchargeServiceId){
					                var totalAmt = (parseFloat(1) * parseFloat(value.basePrice)).toFixed(2);
					
					   				/*$scope.autoRenderedServicesFinalList[i].discountCategoryId = "";
							   		$scope.autoRenderedServicesFinalList[i].discountPercentage = 0;
							   		$scope.autoRenderedServicesFinalList[i].discount = 0;
							   		
							   		$scope.autoRenderedServicesFinalList[i].taxAmt = parseFloat((parseFloat(totalAmt) * $scope.autoRenderedServicesFinalList[i].taxPercentage)/100).toFixed(2);
									$scope.autoRenderedServicesFinalList[i].netPay =(parseFloat(totalAmt) + parseFloat($scope.autoRenderedServicesFinalList[i].taxAmt)).toFixed(2);
									$scope.autoRenderedServicesFinalList[i].selfPayable =(parseFloat(totalAmt) + parseFloat($scope.autoRenderedServicesFinalList[i].taxAmt)).toFixed(2);*/
					                
					                $scope.autoRenderedServicesFinalList[i].discountCategoryId = $scope.billWiseDiscountObj.discountCategoryId.toString();
			    		    		if($scope.discountRadio == 1)
			    					{
			    		    			
			    		    			$scope.autoRenderedServicesFinalList[i].concession = parseFloat(value.concession);
				    		    		$scope.autoRenderedServicesFinalList[i].patientClassPercentage = parseFloat(value.patientClassPercentage);
				    		    		
			    		    			$scope.autoRenderedServicesFinalList[i].discountPercentage = $scope.billWiseDiscountObj.discountPercentageBillWise;
			    		    			var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
			    		    			//$scope.disAmtCal = ((parseFloat(totalAmt)*parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise))/100).toFixed(2);
			                            $scope.disAmtCal = (((parseFloat(totalAmt) - parseFloat(value.concession) ) *parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise))/100).toFixed(2);

			    		    			//$scope.autoRenderedServicesFinalList[i].discount = $scope.disAmtCal;

			                            $scope.autoRenderedServicesFinalList[i].discount =((parseFloat(totalAmt) - parseFloat(value.concession))*(isNaN($scope.billWiseDiscountObj.discountPercentageBillWise) ? 0: $scope.billWiseDiscountObj.discountPercentageBillWise)/100).toFixed(2);

			    		    			  //var withConDic =parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[i].discount);
			                              var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[i].concession) + parseFloat($scope.autoRenderedServicesFinalList[i].discount));

				  	    		  	    	var txpre = 0;
				  	    		  			if(value.taxPercentage != null || value.taxPercentage > 0){
				  	    		  				$scope.autoRenderedServicesFinalList[i].taxPercentage = value.taxPercentage;
				  	    		  				txpre = value.taxPercentage;
				  	    		  			}else{
				  	    		  					$scope.autoRenderedServicesFinalList[i].taxPercentage = 0;
				  	    		  					}
				  	    		  			$scope.autoRenderedServicesFinalList[i].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
				  	    		  			$scope.autoRenderedServicesFinalList[i].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[i].taxAmt)).toFixed(2);
				  	    		  			$scope.autoRenderedServicesFinalList[i].selfPayable = $scope.autoRenderedServicesFinalList[i].netPay;
				  	    		  			$scope.autoRenderedServicesFinalList[i].creditPayable =parseFloat(0);
			    		    			
			    		    			
			    		    			
			    					}
			    					else{
			    						
			    						$scope.autoRenderedServicesFinalList[i].concession = parseFloat(value.concession);
				    		    		$scope.autoRenderedServicesFinalList[i].patientClassPercentage = parseFloat(value.patientClassPercentage);
				    		    		
			    						var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
			    						//$scope.disAmtCal = ((parseFloat(totalAmt)*parseFloat($scope.billWiseDiscountObj.discountAmountBillWise))/$scope.totalDisServiceWise).toFixed(2);
			                           // $scope.disAmtCal = ((( parseFloat(totalAmt) - parseFloat(value.concession) ) * parseFloat($scope.billWiseDiscountObj.discountAmountBillWise))/$scope.totalDisServiceWise).toFixed(2);

			    						//$scope.autoRenderedServicesFinalList[i].discountPercentage = ((parseFloat($scope.disAmtCal)/parseFloat(totalAmt))*100).toFixed(2);
			    						//$scope.autoRenderedServicesFinalList[i].discount = $scope.disAmtCal;
			    						
			    						var disPerTmp = parseFloat(0);
			    						disPerTmp = (parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)/parseFloat($scope.totalDisServiceWise-$scope.AutoRenderedTotalConcessionSummary))*100;
			    						//$scope.autoRenderedServicesFinalList[i].discount = (parseFloat(totalAmt) * disPerTmp)/100;
			    						$scope.autoRenderedServicesFinalList[i].discountPercentage = disPerTmp;

			    						 $scope.autoRenderedServicesFinalList[i].discount =((parseFloat(totalAmt) - parseFloat(value.concession))*(isNaN(disPerTmp) ? 0: disPerTmp)/100).toFixed(2);	

			    						//var withConDic =parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[i].discount);
			                             var withConDic =parseFloat(totalAmt) - ( parseFloat($scope.autoRenderedServicesFinalList[i].concession) + parseFloat($scope.autoRenderedServicesFinalList[i].discount));
  
				  	    		  	    	var txpre = 0;
				  	    		  			if(value.taxPercentage != null || value.taxPercentage > 0){
				  	    		  				$scope.autoRenderedServicesFinalList[i].taxPercentage = value.taxPercentage;
				  	    		  				txpre = value.taxPercentage;
				  	    		  			}else{
				  	    		  					$scope.autoRenderedServicesFinalList[i].taxPercentage = 0;
				  	    		  					}
				  	    		  			$scope.autoRenderedServicesFinalList[i].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
				  	    		  			$scope.autoRenderedServicesFinalList[i].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[i].taxAmt)).toFixed(2);
				  	    		  			$scope.autoRenderedServicesFinalList[i].selfPayable = $scope.autoRenderedServicesFinalList[i].netPay;
				  	    		  			$scope.autoRenderedServicesFinalList[i].creditPayable =parseFloat(0);
			    					}
			    		    		$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[i]);
			    		    		indexList.push(i);
					   			 }
					   			if(i+1 == $scope.autoRenderedServicesFinalList.length && indexList.length>0){
					   				$scope.addOrUpdateServices(null,indexList);
					   				//$scope.saveOrderPackage();
					   			}
					   			i++;
					   			});
					    	   
					    	   if($scope.billWiseDiscountObj.requestAmountBillWise > 0){
					    		var URI = BASE_URL + ROOT_URL
					    		+ "/billing/getUsersListHavingDiscRights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/amt/"+$scope.billWiseDiscountObj.discountAmountBillWise+"/per/"+$scope.billWiseDiscountObj.discountPercentageBillWise;
					    BillingGenericService.serviceAction(METHOD_GET, URI,"",
					    				NOTIFICATION_MSG_STATUS_FALSE)
					    		.then(function(response) {
					    					//console.log(response);
					    					// $rootScope.startLoader();
					    					if (response.data.status == "success")
					    						{
					    						  $scope.userDiscountList =[];
					    						  $scope.userDiscountList = response.data.listObject;
					    						}
					    						
					    				});
					    
							   $scope.requestDiscountDiv = true;
							   $scope.isBillWiseDiscountApplied = true;
							   $scope.finalBill = 'N';
							   growl.warning("Please send discount request to user",{
									title : ERROR_MSG
								});
					    	}else
					    		{
					    		 $scope.requestDiscountDiv = false;
					    		}
					    		return false;
							}
					    	else{
					    		
					    		if($scope.requestStatusObject != null){
					    		    var userGivenDiscount = $scope.billWiseDiscountObj.discountAmountBillWise !== undefined && $scope.billWiseDiscountObj.discountAmountBillWise !== null ? parseFloat($scope.billWiseDiscountObj.discountAmountBillWise) : 0;
					    		    var approvedDiscount = $scope.requestStatusObject.approvedDiscAmt !== undefined && $scope.requestStatusObject.approvedDiscAmt !== null ? parseFloat($scope.requestStatusObject.approvedDiscAmt) : 0;
					    		    var discountDiff = userGivenDiscount - approvedDiscount;
//					    			if($scope.requestStatusObject.reqStatus  == 2 && parseFloat($scope.requestStatusObject.approvedDiscAmt) >= parseFloat($scope.billWiseDiscountObj.discountAmountBillWise))
					    			if(($scope.requestStatusObject.reqStatus  == 2) && (discountDiff < 1.00 && discountDiff > -1.00))
						    		{
					    				// for DD
						    		$scope.billWiseDiscountObj.requestAmountBillWise = 0;
						    		$scope.billWiseDiscountObj.requestUserId = "";
						    		$scope.requestDiscountDiv = false;
						    		// For DD
							    	var i = 0;
							    	angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
							    			if(value.status == 'A' && value.isDiscountApplicable == 'Y'  && value.serviceMasterId != $scope.surchargeServiceId)
							    			{
					    					$scope.requestDiscountDiv = false;
											$scope.billWiseDiscountObj.requestAmountBillWise = 0;
											$scope.billWiseDiscountObj.requestUserId = '';
											
					    					$scope.autoRenderedServicesFinalList[i].discountCategoryId = $scope.billWiseDiscountObj.discountCategoryId.toString();
					    		    		if($scope.discountRadio == 1)
					    					{
					    		    			
					    		    			$scope.autoRenderedServicesFinalList[i].concession = parseFloat(value.concession);
						    		    		$scope.autoRenderedServicesFinalList[i].patientClassPercentage = parseFloat(value.patientClassPercentage);
						    		    		
					    		    			$scope.autoRenderedServicesFinalList[i].discountPercentage = $scope.billWiseDiscountObj.discountPercentageBillWise;
					    		    			var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
					    		    			//$scope.disAmtCal = ((parseFloat(totalAmt)*parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise))/100).toFixed(2);
			                                    $scope.disAmtCal = (((parseFloat(totalAmt)-parseFloat(value.concession))*parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise))/100).toFixed(2);

					    		    			//$scope.autoRenderedServicesFinalList[i].discount = $scope.disAmtCal;
			                                    $scope.autoRenderedServicesFinalList[i].discount =((parseFloat(totalAmt) - parseFloat(value.concession))*(isNaN($scope.billWiseDiscountObj.discountPercentageBillWise) ? 0: $scope.billWiseDiscountObj.discountPercentageBillWise)/100).toFixed(2);
					    		    			 // var withConDic =parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[i].discount);
				                                  var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[i].concession) + parseFloat($scope.autoRenderedServicesFinalList[i].discount));

						  	    		  	    	var txpre = 0;
						  	    		  			if(value.taxPercentage != null || value.taxPercentage > 0){
						  	    		  				$scope.autoRenderedServicesFinalList[i].taxPercentage = value.taxPercentage;
						  	    		  				txpre = value.taxPercentage;
						  	    		  			}else{
						  	    		  					$scope.autoRenderedServicesFinalList[i].taxPercentage = 0;
						  	    		  					}
						  	    		  			$scope.autoRenderedServicesFinalList[i].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
						  	    		  			$scope.autoRenderedServicesFinalList[i].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[i].taxAmt)).toFixed(2);
						  	    		  			$scope.autoRenderedServicesFinalList[i].selfPayable = $scope.autoRenderedServicesFinalList[i].netPay;
						  	    		  			$scope.autoRenderedServicesFinalList[i].creditPayable =parseFloat(0);
						  	    		  			
					    					}
					    					else{
					    						
			                                    $scope.autoRenderedServicesFinalList[i].concession = parseFloat(value.concession);
			                                    $scope.autoRenderedServicesFinalList[i].patientClassPercentage = parseFloat(value.patientClassPercentage);

			                                    
					    						var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
					    						//$scope.disAmtCal = ((parseFloat(totalAmt)*parseFloat($scope.billWiseDiscountObj.discountAmountBillWise))/$scope.totalDisServiceWise).toFixed(2);
			                                    //$scope.disAmtCal = (((parseFloat(totalAmt)-parseFloat(value.concession))*parseFloat($scope.billWiseDiscountObj.discountAmountBillWise))/$scope.totalDisServiceWise).toFixed(2);

					    						//$scope.autoRenderedServicesFinalList[i].discountPercentage = ((parseFloat($scope.disAmtCal)/parseFloat(totalAmt))*100).toFixed(2);
					    						//$scope.autoRenderedServicesFinalList[i].discount = $scope.disAmtCal;

					    						var disPerTmp = parseFloat(0);
					    						disPerTmp = (parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)/parseFloat($scope.totalDisServiceWise-$scope.AutoRenderedTotalConcessionSummary))*100;
					    						//disPerTmp = (parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)/parseFloat($scope.totalDisServiceWise))*100;
					    						//$scope.autoRenderedServicesFinalList[i].discount = (parseFloat(totalAmt) * disPerTmp)/100;
					    						$scope.autoRenderedServicesFinalList[i].discountPercentage = disPerTmp;
					    						 $scope.autoRenderedServicesFinalList[i].discount =((parseFloat(totalAmt) - parseFloat(value.concession))*(isNaN(disPerTmp) ? 0: disPerTmp)/100).toFixed(2);	
					    						 // var withConDic =parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[i].discount);
				                                  var withConDic =parseFloat(totalAmt) - ( parseFloat($scope.autoRenderedServicesFinalList[i].concession) + parseFloat($scope.autoRenderedServicesFinalList[i].discount));

						  	    		  	    	var txpre = 0;
						  	    		  			if(value.taxPercentage != null || value.taxPercentage > 0){
						  	    		  				$scope.autoRenderedServicesFinalList[i].taxPercentage = value.taxPercentage;
						  	    		  				txpre = value.taxPercentage;
						  	    		  			}else{
						  	    		  					$scope.autoRenderedServicesFinalList[i].taxPercentage = 0;
						  	    		  					}
						  	    		  			
						  	    		  			$scope.autoRenderedServicesFinalList[i].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
						  	    		  			$scope.autoRenderedServicesFinalList[i].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[i].taxAmt)).toFixed(2);
						  	    		  			$scope.autoRenderedServicesFinalList[i].selfPayable = $scope.autoRenderedServicesFinalList[i].netPay;
						  	    		  			$scope.autoRenderedServicesFinalList[i].creditPayable =parseFloat(0);
					    					}
					    		    		//$scope.autoRenderedServicesFinalList[i].concession = parseFloat(0);
					    		    		//$scope.autoRenderedServicesFinalList[i].concessionPer = parseFloat(0);
					    		    			$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[i]);
					    		    			indexList.push(i);
								    	     }
								    			if(i+1 == $scope.autoRenderedServicesFinalList.length && indexList.length>0){
								    				$scope.addOrUpdateServices(null,indexList);
									   				//$scope.saveOrderPackage();
								    			}
								    		i++;
					    		
							    		});
						    		}
					    			else{
					    				  growl.warning("You have approved only "+parseFloat($scope.requestStatusObject.approvedDiscAmt).toFixed(2),{
					  						title : ERROR_MSG
					  					});
					    				  return false;
					    			}
					    	}else{
					    		// for DD
					    		$scope.billWiseDiscountObj.requestAmountBillWise = 0;
					    		$scope.billWiseDiscountObj.requestUserId = "";
					    		$scope.requestDiscountDiv = false;
					    		// For DD
						    	var i = 0;
						    	angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
						    			if(value.status == 'A' && value.isDiscountApplicable == 'Y'  && value.serviceMasterId != $scope.surchargeServiceId)
						    			{
				    					$scope.requestDiscountDiv = false;
										$scope.billWiseDiscountObj.requestAmountBillWise = 0;
										$scope.billWiseDiscountObj.requestUserId = '';
										
				    					$scope.autoRenderedServicesFinalList[i].discountCategoryId = $scope.billWiseDiscountObj.discountCategoryId.toString();
				    		    		if($scope.discountRadio == 1)
				    					{
				    		    			
				    		    			$scope.autoRenderedServicesFinalList[i].concession = parseFloat(value.concession);
					    		    		$scope.autoRenderedServicesFinalList[i].patientClassPercentage = parseFloat(value.patientClassPercentage);
					    		    		
				    		    			$scope.autoRenderedServicesFinalList[i].discountPercentage = $scope.billWiseDiscountObj.discountPercentageBillWise;
				    		    			var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
				    		    			//$scope.disAmtCal = ((parseFloat(totalAmt)*parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise))/100).toFixed(2);
			                                $scope.disAmtCal = (((parseFloat(totalAmt)-parseFloat(value.concession))*parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise))/100).toFixed(2);

				    		    			//$scope.autoRenderedServicesFinalList[i].discount = $scope.disAmtCal;
			                                $scope.autoRenderedServicesFinalList[i].discount =((parseFloat(totalAmt) - parseFloat(value.concession))*(isNaN($scope.billWiseDiscountObj.discountPercentageBillWise) ? 0: $scope.billWiseDiscountObj.discountPercentageBillWise)/100).toFixed(2);
			                                
				    		    			  var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[i].concession) + parseFloat($scope.autoRenderedServicesFinalList[i].discount));
					  	    		  	        
					  	    		  	    	var txpre = 0;
					  	    		  			if(value.taxPercentage != null || value.taxPercentage > 0){
					  	    		  				$scope.autoRenderedServicesFinalList[i].taxPercentage = value.taxPercentage;
					  	    		  				txpre = value.taxPercentage;
					  	    		  			}else{
					  	    		  					$scope.autoRenderedServicesFinalList[i].taxPercentage = 0;
					  	    		  					}
					  	    		  			$scope.autoRenderedServicesFinalList[i].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
					  	    		  			$scope.autoRenderedServicesFinalList[i].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[i].taxAmt)).toFixed(2);
					  	    		  			$scope.autoRenderedServicesFinalList[i].selfPayable = $scope.autoRenderedServicesFinalList[i].netPay;
					  	    		  			$scope.autoRenderedServicesFinalList[i].creditPayable =parseFloat(0);
					  	    		  			
				    					}
				    					else{
				    						
				    						$scope.autoRenderedServicesFinalList[i].concession = parseFloat(value.concession);
					    		    		$scope.autoRenderedServicesFinalList[i].patientClassPercentage = parseFloat(value.patientClassPercentage);
					    		    		
				    						var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
				    						//$scope.disAmtCal = ((parseFloat(totalAmt)*parseFloat($scope.billWiseDiscountObj.discountAmountBillWise))/$scope.totalDisServiceWise).toFixed(2);
				    						//$scope.disAmtCal = (((parseFloat(totalAmt)-parseFloat(value.concession))*parseFloat($scope.billWiseDiscountObj.discountAmountBillWise))/$scope.totalDisServiceWise).toFixed(2);
				    						
				    						//$scope.autoRenderedServicesFinalList[i].discountPercentage = ((parseFloat($scope.disAmtCal)/parseFloat(totalAmt))*100).toFixed(2);
				    						//$scope.autoRenderedServicesFinalList[i].discount = $scope.disAmtCal;
				    						var disPerTmp = parseFloat(0);
				    						if($scope.cancelService)
				    							disPerTmp = $scope.billWiseDiscountObj.discountPercentageBillWise;
				    						else
				    							disPerTmp = (parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)/parseFloat($scope.totalDisServiceWise-$scope.AutoRenderedTotalConcessionSummary))*100;
				    						//disPerTmp = (parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)/parseFloat($scope.totalDisServiceWise))*100;
				    					//	$scope.autoRenderedServicesFinalList[i].discount = (parseFloat(totalAmt) * disPerTmp)/100;
				    						$scope.autoRenderedServicesFinalList[i].discountPercentage = disPerTmp;
				    						
				    						 var withConDic =parseFloat(totalAmt) - ( parseFloat($scope.autoRenderedServicesFinalList[i].concession) + parseFloat($scope.autoRenderedServicesFinalList[i].discount));
					  	    		  	        
					  	    		  	    	var txpre = 0;
					  	    		  			if(value.taxPercentage != null || value.taxPercentage > 0){
					  	    		  				$scope.autoRenderedServicesFinalList[i].taxPercentage = value.taxPercentage;
					  	    		  				txpre = value.taxPercentage;
					  	    		  			}else{
					  	    		  					$scope.autoRenderedServicesFinalList[i].taxPercentage = 0;
					  	    		  					}
					  	    		  		    $scope.autoRenderedServicesFinalList[i].discount =((parseFloat(totalAmt) - parseFloat(value.concession))*(isNaN(disPerTmp) ? 0: disPerTmp)/100).toFixed(2);	
					  	    		  			$scope.autoRenderedServicesFinalList[i].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
					  	    		  			$scope.autoRenderedServicesFinalList[i].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[i].taxAmt)).toFixed(2);
					  	    		  			$scope.autoRenderedServicesFinalList[i].selfPayable = $scope.autoRenderedServicesFinalList[i].netPay;
					  	    		  			$scope.autoRenderedServicesFinalList[i].creditPayable =parseFloat(0);
					  	    		  			
				    					}
				    		    		//$scope.autoRenderedServicesFinalList[i].concession = parseFloat(0);
				    		    		//$scope.autoRenderedServicesFinalList[i].concessionPer = parseFloat(0);
				    		    			indexList.push(i);
				    		    			$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[i]);
							    	     }
							    			if(i+1 == $scope.autoRenderedServicesFinalList.length && indexList.length>0){
							    				$scope.addOrUpdateServices(null,indexList);
								   				//$scope.saveOrderPackage();
							    			}
							    		i++;
				    		
						    		});
					    	}
					       }
			    	
			    }
			    
			    
			    $scope.specialityMasterListForGroup = [];
			    //Check Product Wise Category
				  var isGroupExists;
					function checkGroupExistsOrNot(arr, val) {
						return arr
								.some(function(arr) {
									return (val.specialityId === arr.specialityId);
								});
					}
					
					
					$scope.groupWiseDiscountPopup = function()
				    {
						$scope.groupWiseDisFlag = true;
				    	$scope.disPerPopupForGroup = '';
						$scope.disAmtPopupForGroup = '';
						$scope.disCategoryIdForGroup = '';
						$scope.disPerPopupForGroupFull = 0;
				    	//console.log($scope.autoRenderedServicesFinalList);
				    	$scope.groupWiseDiscountServicesList = [];
				    	angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
				    		
				    		if(value.isDiscountApplicable == 'Y'){
				    		var temp = {specialityId :value.serviceSpecialityId,specialityName:value.specialityName};
				    		if($scope.specialityMasterListForGroup.length > 0)
				    			{
							    	isGroupExists=checkGroupExistsOrNot($scope.specialityMasterListForGroup,temp)
									if (!isGroupExists) {
										$scope.specialityMasterListForGroup.push(temp);
									}
				    	       }
				    		else
				    			{
				    			   $scope.specialityMasterListForGroup.push(temp);
				    			}
				    		var newArrPop = angular.copy(value);
				    		$scope.groupWiseDiscountServicesList.push(newArrPop);
				    	}
				    		
				    	});
				    	
				    	$scope.groupWiseServicesSelectedList.services = [];
				    	$scope.chkFlgGroup = 'N';
				    	
				    }
			    
					 $scope.serachGroupWiseServicesClear = function()
					    {
					    	$scope.disPerPopupForGroup = '';
							$scope.disAmtPopupForGroup = '';
							$scope.disCategoryIdForGroup = '';
					    	$scope.serviceSpecialityIdForGroup = '';
					    	$scope.serviceSubSpecialityIdForGroup = '';
					    	$scope.disPerPopupForGroupFull = 0;
					    	$scope.chkFlgGroup = 'N';
					    	$scope.groupWiseServicesSelectedList.services = [];
					    	$scope.groupWiseDiscountServicesList  = [];
					    	
					    	
					    	angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
					    		
					    		if(value.isDiscountApplicable == 'Y'){
					    		var temp = {specialityId :value.serviceSpecialityId,specialityName:value.specialityName};
					    		if($scope.specialityMasterListForGroup.length > 0)
					    			{
								    	isGroupExists=checkGroupExistsOrNot($scope.specialityMasterListForGroup,temp)
										if (!isGroupExists) {
											$scope.specialityMasterListForGroup.push(temp);
										}
					    	       }
					    		else
					    			{
					    			   $scope.specialityMasterListForGroup.push(temp);
					    			}
					    		var newArrPop = angular.copy(value);
					    		$scope.groupWiseDiscountServicesList.push(newArrPop);
					    	}
					    		
					    	});
					    	
					    }
			    
					  $scope.serachGroupWiseServices = function()
					    {
					    	$scope.disPerPopupForGroup = '';
							$scope.disAmtPopupForGroup = '';
							$scope.disCategoryIdForGroup = '';
							$scope.disPerPopupForGroupFull = 0;
					    	$scope.groupWiseServicesSelectedList.services = [];
					    	
					    	
					    	$scope.groupWiseDiscountServicesList = [];
					    	var v = 1;
					    	angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
					    		
					    		if(value.isDiscountApplicable == 'Y'){
					    		var temp = {specialityId :value.serviceSpecialityId,specialityName:value.specialityName};
					    		if($scope.specialityMasterListForGroup.length > 0)
					    			{
								    	isGroupExists=checkGroupExistsOrNot($scope.specialityMasterListForGroup,temp)
										if (!isGroupExists) {
											$scope.specialityMasterListForGroup.push(temp);
										}
					    	       }
					    		else
					    			{
					    			   $scope.specialityMasterListForGroup.push(temp);
					    			}
					    		var newArrPop = angular.copy(value);
					    		$scope.groupWiseDiscountServicesList.push(newArrPop);
					    	}
					    		if(v == $scope.autoRenderedServicesFinalList.length){
					    			if($scope.serviceSpecialityIdForGroup > 0 && $scope.serviceSubSpecialityIdForGroup > 0)
					        		{
					        			$scope.groupWiseDiscountServicesList = [];
					        			var i = 0;
					        			angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
					            			if(value.serviceSpecialityId == $scope.serviceSpecialityIdForGroup && value.serviceSubSpecialityId == $scope.serviceSubSpecialityIdForGroup && value.isDiscountApplicable == 'Y')
					            				{
					            				var newArrPop = angular.copy($scope.autoRenderedServicesFinalList[i]);
					            	    		$scope.groupWiseDiscountServicesList.push(newArrPop);
					            				}
					        				
					        	    		i++;
					        	    	});
					        			
					        			
					        			
					        		}
					        	else if($scope.serviceSpecialityIdForGroup > 0)
					        		{
					    	    		$scope.groupWiseDiscountServicesList = [];
					    				var i = 0;
					    				angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
					    	    			if(value.serviceSpecialityId == $scope.serviceSpecialityIdForGroup && value.isDiscountApplicable == 'Y')
					    	    				{
					    	    				var newArrPop = angular.copy($scope.autoRenderedServicesFinalList[i]);
					            	    		$scope.groupWiseDiscountServicesList.push(newArrPop);
					    	    				}
					    					
					    		    		i++;
					    		    	});
					        		}
					    		}
					    		v++;
					    	});
					    }
					  $scope.getDisPerPopupForGroup = function(disPer)
					    {
						  
						  if(disPer == "" || disPer == null || disPer == undefined){
					    		disPer = 0;
					    	}
						  
					    	var len = $scope.groupWiseServicesSelectedList.services.length;
					    	if(len == 0)
							{
								growl.warning("Please select at least one service!",{
									title : ERROR_MSG
								});
								$scope.disPerPopupForGroup = '';
								$scope.disPerPopupForGroupFull = 0;
					    		return false;
							}
					    	else if(parseFloat(disPer) > 100){
					    		growl.warning("Percentage should be less than or equal to 100 %.",{
									title : ERROR_MSG
								});
					    		$scope.disPerPopupForGroup = '';
								$scope.disAmtPopupForGroup  = '';
								$scope.disCategoryIdForGroup = '';
								$scope.disPerPopupForGroupFull = 0;
					    		return false;
					    	}
					    	else
					    		{
					    		$scope.totalAmtPopupAllServices = 0;
					    		for(var i = 0;i < len;i++)
					    		{
					    			// alert($scope.groupWiseServicesSelectedList.services[i]);
						    		angular.forEach($scope.groupWiseDiscountServicesList, function (value, key) {
						    			if(value.orderDetailsId == $scope.groupWiseServicesSelectedList.services[i])
						    				{
						    				var finalTotalAmt = parseFloat(value.totalAmt) - parseFloat(value.concession);
						    				    $scope.totalAmtPopupAllServices =(parseFloat($scope.totalAmtPopupAllServices) + parseFloat(finalTotalAmt)).toFixed(2);
						    				}
										
							    	});
					    		}
					    	}
					    		
					    	var calDicAmt = ($scope.totalAmtPopupAllServices*disPer/100).toFixed(2);	
							$scope.disAmtPopupForGroup = calDicAmt;
							$scope.disPerPopupForGroup = disPer;
							$scope.disPerPopupForGroupFull = disPer;
					    }
			    
					  $scope.getDisAmtPopupForGroup = function(disAmt)
					    {
						  if(disAmt == "" || disAmt == null || disAmt == undefined){
					    		disAmt = 0;
					    	}
					    	var len = $scope.groupWiseServicesSelectedList.services.length;
					    	if(len == 0)
							{
								growl.warning("Please select at least one service!",{
									title : ERROR_MSG
								});
								$scope.disAmtPopupForGroup  = '';
					    		return false;
							}
					    	else
					    		{
					    		
					    		$scope.totalAmtPopupAllServices = 0;
					    		for(var i = 0;i < len;i++)
					    		{
					    			// alert($scope.groupWiseServicesSelectedList.services[i]);
						    		angular.forEach($scope.groupWiseDiscountServicesList, function (value, key) {
						    			if(value.orderDetailsId == $scope.groupWiseServicesSelectedList.services[i])
						    				{
						    				var finalTotalAmt = parseFloat(value.totalAmt) - parseFloat(value.concession);
						    					$scope.totalAmtPopupAllServices =(parseFloat($scope.totalAmtPopupAllServices) + parseFloat(finalTotalAmt)).toFixed(2);
						    				}
										
							    	});
						    		
						    		if(i+1 == len){
						    			if(parseFloat(disAmt) > parseFloat($scope.totalAmtPopupAllServices)){
						    				growl.warning("Amount should be less than or equal to total amount..",{
						    					title : ERROR_MSG
						    				});
						    				$scope.disAmtPopupForGroup  = '';
						    				$scope.disPerPopupForGroup = '';
						    				$scope.disCategoryIdForGroup = '';
						    				$scope.disPerPopupForGroupFull = 0;
						    	    		return false;
						    			}
						    		}
					    		}
					    		var disPer = ((parseFloat(disAmt)/$scope.totalAmtPopupAllServices)*100).toFixed(2);
					    		$scope.disPerPopupForGroup = disPer;
					    		$scope.disAmtPopupForGroup = disAmt;
					    		$scope.disPerPopupForGroupFull = (parseFloat(disAmt)/$scope.totalAmtPopupAllServices)*100;
					    		}
					    	
							
					    }
			    
			    $scope.clearDiscount = function()
			    {
			    	$scope.disPerPopupForGroup = '';
					$scope.disAmtPopupForGroup = '';
					$scope.disCategoryIdForGroup = '';
					$scope.disPerPopupForGroupFull = 0;
					if(!$scope.checked){
						$scope.chkFlgGroup = 'N';
					}
					if($scope.groupWiseServicesSelectedList.services.length == 0){
						 $scope.groupWiseDisFlag  = true;
					}
			    }
			    
			    $scope.addDiscountInDiscountListForGroup = function()
			    {
			    	if($scope.disPerPopupForGroup == undefined || $scope.disPerPopupForGroup == '' )
			    		{
				    		growl.warning("All fields are required.",{
								title : ERROR_MSG
							});
				    		return false;
			    		}
			    	else if($scope.disAmtPopupForGroup == undefined || $scope.disAmtPopupForGroup == '')
			    		{
				    		growl.warning("All fields are required.",{
								title : ERROR_MSG
							});
				    		return false;
			    		}
			    	else if($scope.disCategoryIdForGroup == undefined || $scope.disCategoryIdForGroup == '')
					{
			    		growl.warning("All fields are required.",{
							title : ERROR_MSG
						});
			    		return false;
					}
			    	else
			    		{
			    		var len = $scope.groupWiseServicesSelectedList.services.length;
			    			if(len == 0)
			    				{
				    				growl.warning("Please select at least one service!",{
				    					title : ERROR_MSG
				    				});
				    	    		return false;
			    				}
			    			else
			    				{
			    				$scope.groupWiseDisFlag = false;
			    				var tmpTotalDis = parseFloat(0);
			    				for(var i = 0;i < len;i++)
			    	    		{
			    					var j = 0;
				    				angular.forEach($scope.groupWiseDiscountServicesList, function (value, key) {
				    					var totalAmtPopupForGroup =0;
				    					var concessionPopupForGroup =0;
				    					var finalTotalAmtForGroup = 0;
				    					var calDicAmtForPopup = 0;
				    	    			if(value.orderDetailsId == $scope.groupWiseServicesSelectedList.services[i])
				    	    				{
				    	    					
				    	    					//test
				    	    				
				    	    				//$scope.groupWiseDiscountServicesList[j].concession = 0;
				    	    				//$scope.groupWiseDiscountServicesList[j].patientClassPercentage = 0;
				    	    				
			                                $scope.groupWiseDiscountServicesList[j].concession =value.concession;
			                                $scope.groupWiseDiscountServicesList[j].patientClassPercentage = value.patientClassPercentage;

			                                
				    						
				    	    				    var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
				    	    				    $scope.groupWiseDiscountServicesList[j].discountCategoryId = $scope.disCategoryIdForGroup;
				    	    				    
				    	    				    
				    	    				    $scope.groupWiseDiscountServicesList[j].discountPercentage = $scope.disPerPopupForGroup;
				    	    				    $scope.groupWiseDiscountServicesList[j].discountPercentageFull = $scope.disPerPopupForGroupFull;
				        		    			//var disAmtCal = ((parseFloat(totalAmt)*parseFloat($scope.disPerPopupForGroup))/100).toFixed(2);
			                                    var disAmtCal = (((parseFloat(totalAmt) - parseFloat(value.concession)) *parseFloat($scope.disPerPopupForGroupFull))/100).toFixed(2);
			                                    tmpTotalDis = parseFloat(tmpTotalDis) + parseFloat(disAmtCal);
				    	    				    $scope.groupWiseDiscountServicesList[j].discount = disAmtCal;
				        		    			
				        		    			
				        		    			// var withConDic =parseFloat(totalAmt) - parseFloat(disAmtCal);
			                                     var withConDic =parseFloat(totalAmt) - ( parseFloat(value.concession) + parseFloat(disAmtCal));

					    	    			        
					    	    			    	var txpre = 0;
					    							if(value.taxPercentage != null || value.taxPercentage > 0){
					    								$scope.groupWiseDiscountServicesList[j].taxPercentage = value.taxPercentage;
					    								txpre = value.taxPercentage;
					    							}else{
					    								  $scope.groupWiseDiscountServicesList[j].taxPercentage = 0;
					    									}
					    						$scope.groupWiseDiscountServicesList[j].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
				    	    					//$scope.groupWiseDiscountServicesList[j].netPay = parseFloat((totalAmt -(parseFloat(disAmtCal)) + parseFloat(value.taxAmt))).toFixed(2);
			                                    $scope.groupWiseDiscountServicesList[j].netPay = parseFloat((totalAmt -(parseFloat(value.concession) + parseFloat(disAmtCal)) + parseFloat(value.taxAmt))).toFixed(2);
				    	    					$scope.groupWiseDiscountServicesList[j].selfPayable = $scope.groupWiseDiscountServicesList[j].netPay;
				    	    					$scope.groupWiseDiscountServicesList[j].creditPayable =parseFloat(0);
				    							
				    							
				    	    					
				    	    					//test
				    	    				}
				    	    			
				    	    			
				    	    			j++;
				    		    	});
				    				
				    				/*if(i+1 == $scope.groupWiseDiscountServicesList.length){
			    	    				if(parseFloat(tmpTotalDis) > parseFloat($scope.disAmtPopupForGroup)){
			    	    					var dif = (parseFloat(tmpTotalDis) - parseFloat($scope.disAmtPopupForGroup)).toFixed(2);
			    	    					 $scope.groupWiseDiscountServicesList[$scope.groupWiseDiscountServicesList.length -1].discount =  ($scope.groupWiseDiscountServicesList[$scope.groupWiseDiscountServicesList.length -1].discount - dif).toFixed(2);
			    	    				}
			    	    				
			    	    				if(parseFloat(tmpTotalDis) < parseFloat($scope.disAmtPopupForGroup)){
			    	    					var dif = (parseFloat($scope.disAmtPopupForGroup) - parseFloat(tmpTotalDis)).toFixed(2);
			    	    					 $scope.groupWiseDiscountServicesList[$scope.groupWiseDiscountServicesList.length -1].discount =  ($scope.groupWiseDiscountServicesList[$scope.groupWiseDiscountServicesList.length -1].discount + dif).toFixed(2);
			    	    				}
			    	    			}*/
				    				
			    				}
			    			}
			    		}
			    	
			    	
			    }
			    
			    
			    $scope.getPayeeCreditDetails = function(itemList)
			    {
			    	$scope.payeeCreditDetailslList = [];
			    	$scope.payeeCreditDetailslList = itemList;
			    }
			    
			    /**********HIS-19871************/
				 $scope.getSpecialityIdWiseDoctorList = function(id, idx) {
					   
						var data = {
							specialityId : id,
							unitId : $scope.unitId,
							organizationId : $scope.organizationId
						};
						var URI = BASE_URL + ROOT_URL
								+ GETDOCTORBYSPECIALITYID;
						
						BillingGenericService
								.serviceAction(METHOD_POST, URI, data,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											console.log(response);
											
											// $rootScope.startLoader();
											if (response.data.status == "success"){
												$scope.doctorList = response.data.listObject;
												$scope.autoRenderedServicesFinalList[idx].doctorForServiceList = response.data.listObject;
//												if($scope.autoRenderedServicesFinalList[idx].doctorSpecialityId == '' || $scope.autoRenderedServicesFinalList[idx].doctorSpecialityId == null || $scope.autoRenderedServicesFinalList[idx].doctorSpecialityId == undefined)
//												{
												$scope.autoRenderedServicesFinalList[idx].doctorSpecialityId = id;
												angular.forEach($scope.specialityMasterList, function(value, key){
													if(value.specialityId == id){
														$scope.specialityName=value.specialityName;
													}
												});
												$scope.autoRenderedServicesFinalList[idx].specialityName = $scope.specialityName;
//												}
											}
											
										});

				   
					}
		      
				$scope.getDoctorMainChangeForDocShare = function(docId,idx, docSpecialityId)
				{
				 
					$scope.autoRenderedServicesFinalList[idx].doctorSpecialityId = null;
					var specDocObj = {
				    			unitId:$scope.unitId,
				    			organizationId:$scope.organizationId,
				    			doctorId:docId
				    }
				    var URI = BASE_URL + ROOT_URL
					+ GETSPECIALITYLISTBYDOCTORID;
					BillingGenericService.serviceAction(METHOD_POST, URI,specDocObj,
							NOTIFICATION_MSG_STATUS_FALSE)
					.then(function(response) {
								if (response.data.status == "success")
									{
										$scope.autoRenderedServicesFinalList[idx].doctorSpecialityList = response.data.listObject;
										$scope.autoRenderedServicesFinalList[idx].doctorSpecialityId = docSpecialityId;
										angular.forEach($scope.specialityMasterList, function(value, key){
											if(value.specialityId == docSpecialityId){
												$scope.specialityName=value.specialityName;
											}
										});
										$scope.autoRenderedServicesFinalList[idx].specialityName = $scope.specialityName;
										$scope.calculateShareAmount($scope.autoRenderedServicesFinalList[idx],idx);
									}
							});
				
				}
								/*************************************/
			    
			    $scope.saveGroupDiscountToFinalList = function()
			    {
			    	var indexList = [];
			    	var len = $scope.groupWiseServicesSelectedList.services.length;
			    	$scope.autoRenderedServicesForSaveAndUpdate=[];
					if(len == 0)
						{
							growl.warning("Please select at least one service!",{
								title : ERROR_MSG
							});
				    		return false;
						}
					else if($scope.disPerPopupForGroup == undefined || $scope.disPerPopupForGroup == '' )
					{
			    		growl.warning("All fields are required.",{
							title : ERROR_MSG
						});
			    		return false;
					}
					else if($scope.disAmtPopupForGroup == undefined || $scope.disAmtPopupForGroup == '')
						{
				    		growl.warning("All fields are required.",{
								title : ERROR_MSG
							});
				    		return false;
						}
					else if($scope.disCategoryIdForGroup == undefined || $scope.disCategoryIdForGroup == '')
					{
						growl.warning("All fields are required.",{
							title : ERROR_MSG
						});
						return false;
					}
					else
						{
						
			    	for(var i = 0;i < len;i++)
					{
						angular.forEach($scope.groupWiseDiscountServicesList, function (value, key) 
						{
						
			    			if(value.orderDetailsId == $scope.groupWiseServicesSelectedList.services[i])
			    				{
			    				angular.forEach($scope.autoRenderedServicesFinalList, function (mainVal, mainKey) 
			    				{
			    					if(value.orderDetailsId == mainVal.orderDetailsId)
			    					{
			    						//$scope.autoRenderedServicesFinalList[mainKey].concession = 0;
			    						//$scope.autoRenderedServicesFinalList[mainKey].patientClassPercentage = 0;
			    						$scope.autoRenderedServicesFinalList[mainKey].concession =value.concession;
			    						$scope.autoRenderedServicesFinalList[mainKey].patientClassPercentage = value.patientClassPercentage;
			    						$scope.autoRenderedServicesFinalList[mainKey].discount = value.discount;
			    						$scope.autoRenderedServicesFinalList[mainKey].discountPercentage = value.discountPercentageFull;
			    						$scope.autoRenderedServicesFinalList[mainKey].discountCategoryId = value.discountCategoryId;
			    						$scope.autoRenderedServicesFinalList[mainKey].totalAmt = value.totalAmt;
			    						$scope.autoRenderedServicesFinalList[mainKey].netPay =value.netPay;
			    						$scope.autoRenderedServicesFinalList[mainKey].discountDetailsList = [];
			    						
			    						$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[mainKey]);
			    						indexList.push(mainKey);
			    					}
			    				});
			    				}
				    	});
						if(i == len-1 && indexList.length>0)
						{
							//$scope.saveOrderPackage();
		    	            $scope.addOrUpdateServices(null,indexList);
							if($scope.requestStatusObject != null){
								$scope.getCancelDiscountReqForDiscount();
								}
							 $scope.billWiseDiscountObj = {
										// "discountGivenBy":"",
										"discountGivenBy":$scope.userId,
										"discountCategoryId" : "",
										"discountAmountBillWise" : "",
										"discountPercentageBillWise" : "",
										"approvedAmountBillWise":"",
										"requestUserId":"",
										"requestAmountBillWise" : 0,
								}
							 $scope.requestDiscountDiv = false;
							 $scope.lineItemDiscountAmt = parseFloat(0);
							 $scope.billWiseDiscountObj.discountAmountBillWise =  parseFloat(0);
							 $scope.billWiseDiscountObj.requestAmountBillWise =   parseFloat(0);
							 
						
						}
						
						}
			    	angular.element('#groupWiseDiscountModal').modal('hide');
			    	
			    	   $scope.billWiseDiscountObj.requestAmountBillWise =0;
			    	   $scope.billWiseDiscountObj.discountAmountBillWise =0;
			    	   angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
			    		   
			    		   $scope.billWiseDiscountObj.discountAmountBillWise  += parseFloat(value.discount);
			    		   $scope.billWiseDiscountObj.requestAmountBillWise   += parseFloat(value.discount);
			    	   });
			    	   $scope.billWiseDiscountObj.discountAmountBillWise  = $scope.billWiseDiscountObj.discountAmountBillWise.toFixed();
			    	   $scope.billWiseDiscountObj.requestAmountBillWise  = $scope.billWiseDiscountObj.requestAmountBillWise.toFixed();
			    	   $scope.billWiseDiscountObj.discountCategoryId  =  $scope.disCategoryIdForGroup;
			    	   
			    	   $scope.funDiscountAmountBillWise();
			    	   var checkrightsAmount = false;
			    	   if($scope.userDiscountObject != null){
				    	   if((parseFloat($scope.billWiseDiscountObj.discountAmountBillWise) < parseFloat($scope.userDiscountObject.fromDiscountAmount))){
				    		   checkrightsAmount = true;
				    	   }
				    	   if((parseFloat($scope.billWiseDiscountObj.discountAmountBillWise) > parseFloat($scope.userDiscountObject.toDiscountAmount))){
				    		   checkrightsAmount = true;
				    	   }
				    	  }else{
				    		  checkrightsAmount = true;
				    	  }
			    	   if(checkrightsAmount == true){
				    		var URI = BASE_URL + ROOT_URL
				    		+ "/billing/getUsersListHavingDiscRights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/amt/"+$scope.billWiseDiscountObj.discountAmountBillWise+"/per/"+$scope.billWiseDiscountObj.discountPercentageBillWise;
				    BillingGenericService.serviceAction(METHOD_GET, URI,"",
				    				NOTIFICATION_MSG_STATUS_FALSE)
				    		.then(function(response) {
				    					//console.log(response);
				    					// $rootScope.startLoader();
				    					if (response.data.status == "success")
				    						{
				    						  $scope.userDiscountList =[];
				    						  $scope.userDiscountList = response.data.listObject;
				    						}
				    						
				    				});
				            $scope.lineLevelItem = true;
						   $scope.requestDiscountDiv = true;
						   $scope.isBillWiseDiscountApplied = true;
						   $scope.finalBill = 'N';
						   growl.warning("Please send discount request to user",{
								title : ERROR_MSG
							});
						   
				    	}else
				    		{
				    		 $scope.requestDiscountDiv = false;
				    		 $scope.billWiseDiscountObj = {
										"discountGivenBy":$scope.userId,
										"discountCategoryId" : "",
										"discountAmountBillWise" : "",
										"discountPercentageBillWise" : "",
										"approvedAmountBillWise":"",
										"requestUserId":"",
										"requestAmountBillWise" : 0,
								}
								$scope.requestDiscountDiv = false;
						    	$scope.isFinancialClearence = 'N';
				    		}
			    	   
			    	   
			    	}	
			    }
			    
				$scope.addImageDetailsForContract = function(obj)
				{
					var encounterDetails = angular.copy($scope.encounterDetailsContract);
					encounterDetails.patientId = $scope.patientDetails.patientId;
					// insurance
				if( obj.paymentEntitlementId==2){
					encounterDetails.documentTypeId = '30';
					encounterDetails.paymentEntitlementId =obj.paymentEntitlementId;

				}else if(obj.paymentEntitlementId==3){
					encounterDetails.documentTypeId = '19';
					encounterDetails.paymentEntitlementId =obj.paymentEntitlementId;
				}else{
					encounterDetails.paymentEntitlementId =obj.paymentEntitlementId;
				}

				$scope.encounterDetailsContractList.push(encounterDetails);
				};



				$scope.removeImageDetailsContract = function(deleteIndex)
				{
					$scope.encounterDetailsContractList.splice(deleteIndex, 1);
				}
			  //chetana
				 $scope.printBedTransferReport = function(info)
				    {
					

					var data = {
						"appName" : "HIS",
						"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
						"reportDesignFilePath" : "BedTransferPrint.rpt",
						"reportFormat" : "PDF",
						"exportFileName" : "BedTransferPrint",
						"reportParams" : [ {
							"paramName" : "transfer_id",
							"paramType" : "Integer",
							"paramValue" : info.transferId

						} ]
					};
					reportService.generate(REPORT_API_URI1, data);

				}

				$scope.recalculateServiceCharges= function(forceInout)
				{
				 	$scope.needToUpdatePharmacyService = true;
				 	$scope.updatePayee = false;
				 	$scope.recalculation = true;
				 	
				 	var indexT = $scope.autoRenderedServicesFinalList.findIndex(record => record.serviceMasterId === $scope.surchargeServiceId);
					if(indexT>=0)
						$scope.autoRenderedServicesFinalList.splice(indexT, 1);
				 	
					if(forceInout == undefined){
						$scope.saveOrderPackage();
					}else{
						$scope.saveOrderPackage(forceInout);
					}
					
					
				}  
				
			    $scope.getSubSpecialityListById = function(specialityId){
			    	$scope.serviceSubSpecialityIdForGroup = '';
					if(specialityId != ""){
					var URI = BASE_URL + ROOT_URL + "/adt/subspeciality/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/specialityId/"+specialityId;
					BillingGenericService.serviceAction(METHOD_GET,URI, null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						$scope.subSpecialityMaterList = [];
						if (response.data.status == "success")
							$scope.subSpecialityMaterList = response.data.listObject;
					});
					}
				}
			    
			    $scope.cancelDiscountForServices = function()
			    {
			    	var i = 1;
			    	var flag = false;
			    	angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
			    		if(parseFloat(value.discount) > 0){
			    			flag = true;
			    		}
			    		if(i == $scope.autoRenderedServicesFinalList.length && flag == true){
			    			angular.element('#cancelDiscountPopup').modal('show');
			    		}
			    		i++;
			    	});
			    	
			    }
			    
			    $scope.checkAll = function()
			    {
			    	$scope.servicesSelectedList.services = [];
			    	if($scope.checkAllFlag == true)
			    		{
			    	angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
			    		
			    		if(value.isPackage == undefined || value.isPackage == null ||  value.isPackage == false ){
			    			value.checked ="Y";
				    		$scope.servicesSelectedList.services.push(value.orderDetailsId);
			    		}
			    		
			    	});
			    		
			    	}
			    	else
			    		{
			    		angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
				    		value.checked ="N";
				    	});
			    		$scope.servicesSelectedList.services = [];
			    		}
			    	
			    }
			    
			    $scope.finalCancelDiscount = function()
			    {
			    	var cancelDiscountObjectList = [];
			    	var cancelRemoveDiscountList = [];
			    	var indexList = [];
			    	$scope.roundOffAmt = 0;
			    	$scope.lineLevelItem = false;
			    	$scope.autoRenderedServicesForSaveAndUpdate=[];
			    	
						var j = 0;
						angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
							
							var totalAmt = 0;
							var dis = 0;
							var gstAmt = 0;
							var withConDic = 0;
							if($scope.autoRenderedServicesFinalList[j].discountPercentage > 0)
							{
			    				$scope.autoRenderedServicesFinalList[j].discountPercentage = 0;
								$scope.autoRenderedServicesFinalList[j].discountCategoryId = '';
								$scope.autoRenderedServicesFinalList[j].discount = 0;
								totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
								
								withConDic =parseFloat(totalAmt - (parseFloat(dis))).toFixed(2);
								gstAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);

								$scope.autoRenderedServicesFinalList[j].taxAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
								$scope.autoRenderedServicesFinalList[j].netPay =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
								$scope.autoRenderedServicesFinalList[j].selfPayable =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
								$scope.autoRenderedServicesFinalList[j].totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
					    		$scope.autoRenderedServicesFinalList[j].discountDetailsList = [];
					    		
					    		$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[j]);	
					    		indexList.push(j);
							}	
			    			if(j+1 == $scope.autoRenderedServicesFinalList.length && indexList.length>0){
			    				if($scope.requestStatusObject != null){
									$scope.getCancelDiscountReqForDiscount();
									}
			    				 //$scope.saveOrderPackage();
			    				 $scope.addOrUpdateServices(null,indexList);
			    				 $scope.billWiseDiscountObj = {
											// "discountGivenBy":"",
											"discountGivenBy":$scope.userId,
											"discountCategoryId" : "",
											"discountAmountBillWise" : "",
											"discountPercentageBillWise" : "",
											"approvedAmountBillWise":"",
											"requestUserId":"",
											"requestAmountBillWise" : 0,
									}
			    				 $scope.requestDiscountDiv = false;
								 $scope.lineItemDiscountAmt = parseFloat(0);
								 $scope.billWiseDiscountObj.discountAmountBillWise =  parseFloat(0);
								 $scope.billWiseDiscountObj.requestAmountBillWise =   parseFloat(0);
			    			}
			    			j++;
				    	});
			    }
			
			    $scope.getPharmacyDetails = function(orderId)
				{
					$scope.medicationIssueListByOrderId = [];
					$scope.medTotalAmt=0;
					$scope.medTotalConcession=0;
					$scope.medTotalDiscount=0;
					$scope.medTotalPackageAmt=0;
					$scope.medNetAmount=0;
					$scope.medTaxAmount=0;
					$scope.medTotalSelf=0;
					$scope.medTotalCredit=0;
					angular.forEach($scope.medicationIssueList, function (value, key) 
					{
						if(value.orderId == orderId)
						{
							$scope.medTotalAmt = $scope.medTotalAmt + value.totalAmount;
							$scope.medTotalConcession = $scope.medTotalConcession + value.ordConcession;
							$scope.medTotalDiscount=$scope.medTotalDiscount + value.ordDiscount;
							$scope.medNetAmount=$scope.medNetAmount + value.netAmount;
							$scope.medTaxAmount=0;
							$scope.medTotalSelf=$scope.medTotalSelf + value.selfPayable;
							$scope.medTotalCredit=$scope.medTotalCredit + value.creditPayable;
							$scope.medTotalPackageAmt = $scope.medTotalPackageAmt + value.packageConsumptionAmt;
							$scope.medicationIssueListByOrderId.push(value);
						}
						
					});
				}
			    
			    $scope.changeSelfAmount =function(item,idx)
			    {
			        
			        if(item.netPay>0)
			            {
			               if(parseFloat(item.selfPayable)>item.netPay)
			                   {
			                   $scope.autoRenderedServicesFinalList[idx].creditPayable = $scope.autoRenderedServicesFinalList[idx].oldCreditPayable;
			                   $scope.autoRenderedServicesFinalList[idx].selfPayable =$scope.autoRenderedServicesFinalList[idx].oldSelfPayable;
			                   }
			               else{
			                   $scope.serviceViseSplit = true;
			                   $scope.autoRenderedServicesFinalList[idx].creditPayable = (parseFloat(item.netPay) - parseFloat(item.selfPayable)).toFixed(2);
			                   $scope.autoRenderedServicesFinalList[idx].selfPayable = parseFloat(item.selfPayable).toFixed(2);
			                  }
			               
			            }
			          $scope.calculationTest($scope.autoRenderedServicesFinalList,1);
			          //console.log("Self",$scope.autoRenderedServicesFinalList);
			    }
			    
			    
			    $scope.getUsersForFinancialClearanceRights =function()
			    {
			        
			    	var URI = BASE_URL + ROOT_URL + "/billing/getUsersListHavingFinancialClearanceRights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/amt/"+$scope.netPayableSummary;
					BillingGenericService.serviceAction(METHOD_GET,URI, {} , NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						//console.log(response)
						if(response.data.status == 'success'){
							$scope.getUsersForFinancialClearanceList = response.data.listObject;
						}
						
					});
			    }
			    


			   /* $scope.getDoctorBySpeciality = function(id, idx) {


					var data = {
						specialityId : id,
						unitId : $scope.unitId,
						organizationId : $scope.organizationId
					};
					var URI = BASE_URL + ROOT_URL
							+ GETDOCTORBYSPECIALITYID;
					
					BillingGenericService
							.serviceAction(METHOD_POST, URI, data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										console.log(response);
										$scope.doctorList = [];
										// $rootScope.startLoader();
										if (response.data.status == "success")
											$scope.doctorList = response.data.listObject;
									});

				}*/
			    
			$scope.changeCreditAmount =function(item,idx){
			    if(item.netPay>0)
			    {
			       if(parseFloat(item.creditPayable)>item.netPay)
			           {
			           $scope.autoRenderedServicesFinalList[idx].creditPayable = $scope.autoRenderedServicesFinalList[idx].oldCreditPayable;
			           $scope.autoRenderedServicesFinalList[idx].selfPayable =$scope.autoRenderedServicesFinalList[idx].oldSelfPayable;
			           }
			       else{
			           $scope.serviceViseSplit = true;
			           $scope.autoRenderedServicesFinalList[idx].selfPayable =(parseFloat(item.netPay) - parseFloat(item.creditPayable)).toFixed(2);
			           $scope.autoRenderedServicesFinalList[idx].creditPayable = parseFloat(item.creditPayable).toFixed(2);
			           
			          }
			    }
			  $scope.calculationTest($scope.autoRenderedServicesFinalList,1);
			  //console.log("credit",$scope.autoRenderedServicesFinalList);
			}
			$scope.groupService ="";
			
			if (angular.equals({},$stateParams.patientDetailsObj)) {
				return;
			} else {
				//console.log('$stateParams.patientDetailsObj ddddddd',$stateParams.patientDetailsObj);
		        if($stateParams.patientDetailsObj.billingMasterId != undefined && $stateParams.patientDetailsObj.billingMasterId > 0){
		            $scope.preBillId = $stateParams.patientDetailsObj.billingMasterId;
		        }
				$scope.patientList=[];
				$scope.patientAdmissionList = [];
				$scope.patientDetails ={};
				$scope.admissionDetails = {};
				$scope.patientAdmissionList = [];
				self.model.patientDetails ={};
				self.model.encounterDetails = {};
				self.model.interimBills = {};
				 $scope.isFromDischarge=true;
				$scope.admissionType=$stateParams.patientDetailsObj.visitTypeId;
				$scope.admissionDetails.admissionId=$stateParams.patientDetailsObj.admissionId;
				self.model.getInterimBillsByAdmissionId = getInterimBillsByAdmissionId;
				$scope.patientList.push($stateParams.patientDetailsObj);
				getPatientDetailsByIndex('0');
			}
			
			
			/*$scope.stopLoader = function()
			{
				alert();
				if($scope.isIntermediateBill == 'Y')
					{
					  $scope.autoRenderedServicesFinalList = angular.copy($scope.intermediateServiceTemp);
					}
			}*/
			
				/***=========================Payer tab=========================***/
			//Thumb search
			
			
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
			           				$scope.patientList = response.data.listObject;
			        				$scope.patientAdmissionList = [];
			        				$scope.patientDetails ={};
			        				$scope.admissionDetails = {};

			        				////console.log('$scope.patientList : ', JSON.stringify($scope.patientList));
			        				$scope.patientAdmissionList = [];
			        				self.model.patientDetails ={};
			        				self.model.encounterDetails = {};
			        				self.model.interimBills = {};
			        				self.model.getInterimBillsByAdmissionId = getInterimBillsByAdmissionId;
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
			
			
			
			
	}
	

	function config($stateProvider) {
	    $stateProvider.state("IPDCharges", {
	      url: "/IPDCharges",
	      templateUrl: 'views/master/UnitMaster/billingMaster/IPDCharge/IPD-charge.html',
	      controller: "IPDChargesController.Controller",
	      title: 'IPD Charges',
	      controllerAs: "vm",
	      resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 39);
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
	      params: {
			  patientDetailsObj: {} //default value
        }
	    });
	  }
	
	  angular
	    .module("myApp")
	    .config(config)
	    .controller("IPDChargesController.Controller", IPDChargesController);
})();

/*angular.module("myApp").controller('IPDChargesController', [ '$scope','$rootScope','$cookies','$state','BillingGenericService','GenericService','$stateParams','$timeout','growl','paymentCollFactory','GenericServiceParamHeader',function(c)
{
	
}]);*/
