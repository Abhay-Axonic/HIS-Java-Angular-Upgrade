/**
 * @author Suraj
 * 	Controller to handle Upadate PayeeInformation Controller
 */

angular.module("myApp").controller("updatePayeeInformationController", [ '$scope','$rootScope','$cookies','$state','BillingGenericService','GenericService','$stateParams','$timeout','growl','paymentCollFactory','GenericServiceParamHeader','GenericServiceFileUpload',function($scope,$rootScope,$cookies,$state,BillingGenericService,GenericService,$stateParams,$timeout,growl,paymentCollFactory,GenericServiceParamHeader,GenericServiceFileUpload)
	{
		
	var cookieObject = $cookies.getObject('cookieObject');
	if (cookieObject == undefined) {
		$state.go('login');
		return;
	}
	
	$scope.isOpoShowOrNot = true;
	
	var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
	 if(cookieObjectAppConfig == undefined){
	  $state.go('login');
	  return;
	 }
	 else
	  {
	   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
	   
	  }
	$scope.admissionType = '1';
	$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
	$scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
	$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
	$scope.pharmacyServiceId = cookieObjectAppConfig.pharmacyServiceId;
	$scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
	$scope.init = function() {
		
		
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
		
        //Patient Search DOB Start
        $scope.openDatePickerPatiSearch = function() {
            $scope.datepicker1.opened = true;
        };
        
        $scope.datepicker1 = {
                opened : false
            };
        
        $scope.dateOptions = 
        {
				formatYear : 'yyyy',
				showWeeks : false,
				maxDate:new Date()
			};
        
    

    		$scope.openDatePicker = function() {
    			$scope.datepicker.opened = true;
    		};

    		$scope.datepicker = {
    			opened : false
    		};
        
        $scope.getSexMasterList = function() {
    		var URI = BASE_URL + ROOT_URL + GETACTIVEGENDERLIST;
    		BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
    			$rootScope.sexMasterList = response.data.listObject;
    			console.log($rootScope.sexMasterList);
    		});
    	}
        $scope.getSexMasterList();
    	
        $scope.getIdentificationTypeList = function(){
    		var URI = BASE_URL + ROOT_URL + GETACTIVEIDENTIFICATION;
    		BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
    			if (response.data.status == "success")
    				$rootScope.identificationTypeList = response.data.listObject;
    			console.log($rootScope.identificationTypeList);
    		});
    	}
        $scope.getIdentificationTypeList();
        //Patient Search DOB End

		
		/*$scope.openDatePickerContractEndDate = function() {
			$scope.datepickerConE.opened = true;
		};

		$scope.datepickerConE = {
			opened : false
		};*/
		
		
		$scope.openDatePickerContractEndDate = function(index)
		{

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
		
		
		/*$scope.openDatePickerInsuranceStartDate = function() {
			$scope.datepickerInss.opened = true;
		};

		$scope.datepickerInss = {
			opened : false
		};*/
		
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
		
		$scope.dateOptionsPaymentEntitlement = {
				formatYear : 'yyyy',
				showWeeks : false,
				minDate : new Date()
			};
		$scope.dateOptionsPaymentEntitlementStart = {
				formatYear : 'yyyy',
				showWeeks : false,
				maxDate : new Date()
			};
		
		/*$scope.openDatePickerInsuranceEndDate = function() {
			$scope.datepickerInsE.opened = true;
		};

		$scope.datepickerInsE = {
			opened : false
		};*/
		
		
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
		
	$scope.unitId = cookieObject.unitId;
	$scope.organizationId = cookieObject.organizationId;
	$scope.userId = cookieObject.userId;
	$scope.visitTypeId=1;
	$rootScope.loginpage = true;
	$scope.currentDate = new Date();
	$scope.patientList  = [];
	$scope.patientDetailsList = [];
	$scope.patientDetails = {};
	$scope.payeeInfoByVisitTypeData ={};
	$scope.transferToActiveDeposit = "N";
	
	
	$scope.updatePayeeObj = {
			visitNo:'',
			paymentEntitlementDesc:'',
			paymentEntitlementId:'',
			visitTypeId:'',
			billingBedCategoryId:'',
			bedCategoryId:'',
			patientCategoryId:'',
			pharmacyServiceId:$scope.pharmacyServiceId	
	}
	
	var commonObj = {
			unitId : $scope.unitId,
			organizationId : $scope.organizationId
		};

	$scope.patientSearchObj = {
		      "uhIdNumber"      : "",
		      "patientName"     : "",
		      "genderId"        : "",
		      "mobileNo"        : "",
		      "birthDate"       : "",
		      "identificationNo":"",
		      "patientType"   :"1",
		      "visitTypeId" : "",
		      "unitId"          :$scope.unitId,
		      "organizationId"  :$scope.organizationId,
		      
		    }
	
	$scope.refundCNSearchObj = 
	    {
			  "fromDate" 	: "",
			  "toDate" 		: "",
			  "patientName" : "",
			  "uhidNumber"  : "",
			  "visitTypeId" : "",
			  "depositTypeId" : "",
			  "orgId" 		: $scope.organizationId,
			  "unitId" 		: $scope.unitId,
			  "refundCnNo"  : "",
			  "paymentRefundVoucherNo" :""	  
		}

	$scope.currentDate = new Date();
	$scope.currentDate = moment($scope.currentDate)
			.format('DD-MM-YYYY HH:mm:ss');
	$scope.appointmentMasterObj = {
			"patientSponsorDetailsId":"",
			 "encounterId"			:"", 
			 "patientId"			:"",
			 "visitTypeId"			:"",
			 "doctorId"				:"",
			 "sourceId"				:"",
			 "encounterDate"		: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
			 "encounterTime"		:"",
			 "isReferal"			:"A",
			 "deptId"				:"",
			 "subDeptId"			:"",
			 "remark"				:"",
			 "kinDetailsId"			:"",
			 "paymentEntitlementTypeId":"1",
			 "encounterNumber"		:"",
			 "createdDate"			:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
			 "createdBy"			:$scope.userId,
			 "updatedDate"			:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
			 "updatedBy"			:$scope.userId,
			 "status"				:"A",
			 "unitId"				:$scope.unitId,
			 "organizationId"		:$scope.organizationId,
			 "listKinDetailsDto"	:$scope.kinDetailsList,
			 "isScheduledAppointment":"I",
			 "modalityId":"",
			 "specialityId":"",
			 //"subSpecialityId":"",
			 "systemGeneratedVisitTypeId":"",
			 "userDefinedVisitTypeId":"",
			 "reasonIdForChangingVisitType":"",
			 "remarkForChangingVisitType":"",
			 "encounterTypeId":"",
			 "clinicId":"",
			 "referralTypeId":"",
			 "referralId":"",
			 "referralRemark":"",
			 "defaultSelfTariffId" : $scope.defaultSelfTariffId,
			 "isAppointmentRequired" : $scope.isAppointmentRequired,
			 "currentDate" : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
		}
	 $scope.paymentEntitleMentList=[];
	/*$scope.paymentEntitleMentList=
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
			'priority':1,
			'companyId':1,
			'tariffId':$scope.defaultSelfTariffId,
			'contractId':null
		}];*/
	
	$scope.paymentEntitleMentCorporate=
		[{
			'companyId':"1",
			'contractId':"",
			//'statrDate':"",
			//'endDate':""
		}];
	
	$scope.paymentEntitleMentInsurance=
		[{
			'companyId':"1",
			'associateCompanyId':"",
			'contractId':"",
			//'statrDate':"",
			//'endDate':"",
			'insuranceNumber':"",
			'insuredAmt':""
		}];
	
	var URI = BASE_URL + ROOT_URL
	+ GETACTIVETARIFFMASTERLIST;
GenericService
	.serviceAction("POST", URI, commonObj)
	.then(
			function(response) {
				$scope.tariffList = [];
				if (response.data.status == "success") {
					$scope.tariffList = response.data.listObject;
					console.log("ActiveTariffMasterList",$scope.tariffList);
				}
			});

		$scope.enableDisabledApply=false;	
		$scope.enableDisabledValidated=false;
		
		$scope.initCoEmp=function(){
			$scope.corEmpObj={
					typeOfCoEmp:'1',
					note:""
			};
		}
		$scope.initCoEmp();
		
		$scope.initEmpObj=function(){
			$scope.empObj={
					employeeNumber:""
			};
		}
		$scope.initEmpObj();
		
		
		
		$scope.priorityList = [];

	
    }
	
	$scope.init();
	
	
	$scope.corEmpVerificationFlag=false;
	$scope.getCoEmpVerificationReq = function(){
		try{
			var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/getUnitApplicationBillingConfigList";
			var obj = {
						organizationId : $scope.organizationId,
						unitId         :$scope.unitId
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
		}catch (e) {
			console.log("Exception",e.message);
		}
	}
	// check the corporate verification is required or not
	$scope.getCoEmpVerificationReq();
	
	
	$scope.searchPatient = function(patientSearchObj)
	{
		
		if(patientSearchObj.uhIdNumber == "" && patientSearchObj.patientName == "" && patientSearchObj.genderId == "" && patientSearchObj.mobileNo == ""  && patientSearchObj.identificationNo == "" && patientSearchObj.birthDate=="")
		{
			growl.warning(SearchFilter_REQUIRED,{title : "WARNING!"});
			$scope.patientList=[];
			return false;
		}
		if(patientSearchObj.patientName!="" && patientSearchObj.patientName.length < 3)
		{
			growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
			$scope.patientList = [];
			return false;
		}	
		$scope.updatePayeeObj = {
				visitNo:'',
				paymentEntitlementDesc:'',
				paymentEntitlementId:'',
				visitTypeId:'',
				billingBedCategoryId:'',
				bedCategoryId:'',
				patientClassId:'',
				pharmacyServiceId:''	
		}
		
		 $scope.paymentEntitleMentList=[];
		$scope.patientDetails = {};
		$scope.payeeInfoByVisitTypeData ={};
		$rootScope.startLoader();
		$scope.patientSearchObj.unitId = cookieObject.unitId;
		$scope.patientSearchObj.organizationId = cookieObject.organizationId;
//		var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
		
		
		
		
		if(patientSearchObj.birthDate != "" && typeof(patientSearchObj.birthDate) != "undefined"){
			patientSearchObj.birthDate = moment(patientSearchObj.birthDate).format('YYYY-MM-DD');
		} 
		$scope.patientSearchObj.visitTypeId = $scope.admissionType;
		$scope.patientSearchObj.wardList =[];
		$scope.patientSearchObj.bedList =[];
		
		var patSearchObj = angular.copy($scope.patientSearchObj);
		if($scope.admissionType != 1){
			var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteriaForIPD";
			BillingGenericService.serviceAction(METHOD_POST,URI, patSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				$scope.searchPatientList = [];
				console.log("response",response);
				if (response.data.status == "success"){				
					$scope.patientList = response.data.listObject;
					$rootScope.stopLoader();
				}
				else  if(response.data.listObject==null){
					growl.warning(response.data.message,{
						title : ERROR_MSG
					});
					$scope.patientSearchObj.birthDate="";
					$scope.patientList=[];
					return false;
				}
			 else{
					$rootScope.stopLoader();
				}
				$scope.patientSearchObj.birthDate="";
				patSearchObj = {};
			});

		}
		else{
			var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByForOPBilling";
			BillingGenericService.serviceAction(METHOD_POST,URI,patientSearchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				$scope.searchPatientList = [];
				console.log("response",response);
				if (response.data.status == "success"){				
					$scope.patientList = response.data.listObject;
					$rootScope.stopLoader();
					
				}
				else  if(response.data.listObject==null){
					growl.warning(response.data.message,{
						title : ERROR_MSG
					});
					$scope.patientSearchObj.birthDate="";
					$scope.patientList=[];
					return false;
				}else{
					$rootScope.stopLoader();
				}
				$scope.patientSearchObj.birthDate="";
			
				patSearchObj = {};
				
			});
		}
		
	}
	// check the corporate verification is required or not
	$scope.corEmpVerificationFlag=false;
	$scope.getCoEmpVerificationReq = function(){
		try{
			var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/getUnitApplicationBillingConfigList";
			var obj = {
						organizationId : $scope.organizationId,
						unitId         :$scope.unitId
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
		}catch (e) {
			console.log("Exception",e.message);
		}
	}
	// check the corporate verification is required or not
	$scope.getCoEmpVerificationReq();
	
	
	
	$scope.getEncounterDetails = function(encounterId)
	{
		var data = {};
		var URI = BASE_URL + ROOT_URL + "/api/opd/encounter/"+encounterId;
		BillingGenericService.serviceAction(METHOD_GET,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			if(response.data.status == "success"){
				
				$scope.admissionDetails = response.data.object;
				
				$scope.patientDetails.doctorFullName = $scope.admissionDetails.doctorName;
				$scope.patientDetails.dateOfAdmission = null;
				$scope.patientDetails.patientDischargeDate = null;
				$scope.patientDetails.wardName = "";
				$scope.patientDetails.bedNumber = "";
				$scope.updatePayeeObj.visitNo = $scope.admissionDetails.encounterNumber;
				$scope.updatePayeeObj.paymentEntitlementDesc = $scope.admissionDetails.paymentEntitlementDesc;
				$scope.updatePayeeObj.paymentEntitlementId = $scope.admissionDetails.paymentEntitlementTypeId;
				$scope.updatePayeeObj.billingBedCategoryId = $scope.opdBillingCatId;
				$scope.updatePayeeObj.bedCategoryId = null;
				$scope.updatePayeeObj.patientCategoryId = $scope.admissionDetails.patientCategoryId;
				$scope.updatePayeeObj.referralId = $scope.admissionDetails.referralId;
				$scope.updatePayeeObj.patientClassId = $scope.admissionDetails.patientClassId;
				
				var URI = BASE_URL + ROOT_URL + "/api/opd/sponsoredDetails/admission/0/encounter/"+$scope.admissionDetails.encounterId;
				BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						$scope.paymentEntitleMentList = [];
						 $scope.priorityList = [];
						var i = 1;
						  angular.forEach(response.data.listObject, function (value, key) {
							  
							  if(value.priorityId == 1){
									$scope.updatePayeeObj.paymentEntitlementDesc = value.paymentEntitlementDesc;
									$scope.updatePayeeObj.paymentEntitlementId = value.paymentEntitlementId;
							  }
							  
							  var prioObj = {
									  id:i,
									  value:i
							  }
							  $scope.priorityList.push(prioObj);
							  var newArr = {};
							  newArr.paymentEntitlementName =value.paymentEntitlementDesc ;
							  newArr.paymentEntitlementId=value.paymentEntitlementId ;
							  newArr.companyName=value.companyDesc ;
							  newArr.tariffName=value.tariffDesc ;
							  newArr.contractName=value.contractName ;
							  newArr.insuranceName=value.insuranceDesc ;
							  newArr.insuranceNumber=value.insuranceNumber ;
							  newArr.startDate=value.validityFrom ;
						      newArr.endDate=value.validityTo;
							  newArr.priority=value.priorityId ;
							  newArr.companyId=value.companyId ;
							  newArr.tariffId=value.tariffId ;
							  newArr.contractId=value.contractId ;
							  newArr.typeId=value.paymentEntitlementId ;
							  newArr.insuredAmount=value.insuredAmount ;
							  $scope.paymentEntitleMentList.push(newArr);
							  i++;
							//  newArr.billingBedCategoryId=value. ;
						  });
					}
					$rootScope.stopLoader();
				});
		
			}
		});
	}
	
	
//	$scope.getPatientDetailsById = function(patientId){
	$scope.getPatientDetailsByIndex = function(index){
		$("#patientSelectRadio_"+index).prop("checked",true);
		$scope.payeeInfoByVisitTypeData ={};
		//$rootScope.startLoader();
		$scope.patientDetailsList = [];
		$scope.patientDetails = {}
		/*var URI = BASE_URL + ROOT_URL + "/api/global/patient/billing/"+patientId;
		BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success"){
				$scope.patientDetailsList = response.data.listObject;
				$scope.patientDetails = response.data.listObject[0];
				//$scope.deposit.patientId = $scope.patientDetails.patientId;
			}
		});*/
		var i = 0;
		  angular.forEach($scope.patientList, function (value, key) {
			   if(value.patientId == $scope.patientList[index].patientId)
				   {
				   	 if(value.admissionId != null || value.admissionId != undefined)
				   		 {
				   		   $scope.patientDetails = $scope.patientList[i];
				   		   
				   		$scope.patientDetails.age=($scope.patientList[i].age);
						$scope.patientDetails.uhidNumber=($scope.patientList[i].uhIdNumber);
						$scope.patientDetails.dob=($scope.patientList[i].birthDate);
						$scope.updatePayeeObj.visitTypeId = $scope.patientList[i].visitTypeId;
						$scope.updatePayeeObj.patientId= $scope.patientList[i].patientId;
				   		
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL + "/api/ipd/admissions/patient/updatePayee/"+$scope.patientDetails.admissionId;
							BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
								if(response.data.status == "success"){
									$scope.admissionDetails = response.data.object;
									$scope.patientDetails.doctorFullName = $scope.admissionDetails.doctorName;
									$scope.patientDetails.dateOfAdmission = $scope.admissionDetails.doa;
									$scope.patientDetails.patientDischargeDate = $scope.admissionDetails.pdd;
									$scope.patientDetails.wardName = $scope.admissionDetails.wardNo;
									$scope.patientDetails.bedNumber = $scope.admissionDetails.bedNo;
									$scope.updatePayeeObj.visitNo = $scope.admissionDetails.admissionNumber;
									$scope.updatePayeeObj.paymentEntitlementDesc = $scope.admissionDetails.paymentEntitlement;
									$scope.updatePayeeObj.paymentEntitlementId = $scope.admissionDetails.paymentEntitlementId;
									$scope.updatePayeeObj.billingBedCategoryId = $scope.admissionDetails.bedBillingCategoryId;
									$scope.updatePayeeObj.bedCategoryId = $scope.admissionDetails.bedCategoryId;
									$scope.updatePayeeObj.patientCategoryId = $scope.admissionDetails.patientCategoryId;
									$scope.updatePayeeObj.referralId = $scope.admissionDetails.referralId;
									$scope.updatePayeeObj.patientClassId = $scope.admissionDetails.patientClassId;
									//$scope.getPayeeInfoByVisitType();
								}
								$rootScope.stopLoader();
							});
							
							$rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL + "/api/opd/sponsoredDetails/admission/"+$scope.patientDetails.admissionId+"/encounter/0";
							BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
								if(response.data.status == "success"){
									$scope.paymentEntitleMentList = [];
									 $scope.priorityList = [];
									var i = 1;
									  angular.forEach(response.data.listObject, function (value, key) {
										  
										  var prioObj = {
												  id:i,
												  value:i
										  }
										  $scope.priorityList.push(prioObj);
										  var newArr = {};
										  newArr.paymentEntitlementName =value.paymentEntitlementDesc ;
										  newArr.paymentEntitlementId=value.paymentEntitlementId ;
										  newArr.companyName=value.companyDesc ;
										  newArr.tariffName=value.tariffDesc ;
										  newArr.contractName=value.contractName ;
										  newArr.insuranceName=value.insuranceDesc ;
										  newArr.insuranceNumber=value.insuranceNumber ;
										  newArr.startDate=value.validityFrom ;
									      newArr.endDate=value.validityTo;
										  newArr.priority=value.priorityId ;
										  newArr.companyId=value.companyId ;
										  newArr.tariffId=value.tariffId ;
										  newArr.contractId=value.contractId ;
										  newArr.typeId=value.paymentEntitlementId ;
										  newArr.insuredAmount=value.insuredAmount ;
										  $scope.paymentEntitleMentList.push(newArr);
										  i++;
										//  newArr.billingBedCategoryId=value. ;
									  });
									/*$scope.paymentEntitleMentList=
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
											'companyId':'1',
											'tariffId':$scope.defaultSelfTariffId,
											'contractId':null,
											'typeId' : 1,
											'insuredAmount' : '',
											'billingBedCategoryId':null
										}];*/
								}
								$rootScope.stopLoader();
							});
				   		 }
						else  if(value.admissionId == null || value.admissionId == undefined) {
							

					   	    $scope.patientDetails = $scope.patientList[i];
					   		   
					   		$scope.patientDetails.age=($scope.patientList[i].age);
							$scope.patientDetails.uhidNumber=($scope.patientList[i].uhIdNumber);
							$scope.patientDetails.dob=($scope.patientList[i].birthDate);
							$scope.updatePayeeObj.visitTypeId =1;
							$scope.updatePayeeObj.patientId= $scope.patientList[i].patientId;
							
							
							var data = {};
							var URI = BASE_URL + ROOT_URL + "/api/opd/encounters/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/patientId/"+$scope.patientList[index].patientId;
							
							BillingGenericService.serviceAction(METHOD_GET,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
								if(response.data.status == "success"){
									console.log("Encouner List",response.data.listObject);
									$scope.patientEncounterList = response.data.listObject;
									$scope.updatePayeeObj.encounterId= $scope.patientEncounterList [0].encounterId;
									$scope.getEncounterDetails($scope.updatePayeeObj.encounterId);
									
								
								}
							});
						}

				   	 else{
				   		growl.warning("Patient not admitted.",{
			    			title : WARNING_MSG
			    		});
						return false;
				   	 }
				    
				   }
			   i++;
	      });
		//console.log("aas",$scope.patientDetailTemp);
		$rootScope.stopLoader();
	}
	
	  $scope.getVisitTypeList = function() {
		  var param = {organizationId: cookieObject.organizationId,unitId:$scope.unitId};
		    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getVisitTypeList"
		    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					 $scope.visityTypeList = response.data.listObject;
				}
			});
		  }
		$scope.getVisitTypeList();
	
	$scope.getPayeeInfoByVisitType = function(){
		if($scope.patientDetails.patientId== undefined ||$scope.patientDetails.patientId == ''){
			growl.error("Please Select Patient ",{
    			title : ERROR_MSG
    		});
			return;
		}else{
			$rootScope.startLoader();
			var param={
					"patientId": $scope.patientDetails.patientId,
					"visitTypeId":$scope.updatePayeeObj.visitTypeId,
			}
			var URI = BASE_URL + ROOT_URL + "/api/billing/getPayeeInfoByVisitType";
			BillingGenericService.serviceAction("POST",URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					$scope.payeeInfoByVisitTypeData =  response.data.listObject[0];
					if(response.data.listObject!=null){
						 $scope.paymentEntitleMentList=[];
						 angular.forEach(response.data.listObject, function (value, key) {
						   
								  var newArr = {};
								  newArr.paymentEntitlementName=value.paymentEntitlementDesc;
								  newArr.paymentEntitlementId="1";
								  newArr.patientSponsorDetailsId=value.patientSponsorDetailsId;
								  newArr.companyName = value.companyName;
								  newArr.companyId =value.companyId;
								  newArr.tariffName = value.tariffDesc;
								  newArr.contractName = value.contractName;
								  newArr.insuranceName = "";
								  newArr.insuranceNumber = "";
								  newArr.startDate = value.validityFrom;
								  newArr.endDate = value.validityTo;
								  newArr.priority =  value.priorityId;
								  newArr.tariffId = value.tariffId;
								  $scope.paymentEntitleMentList.push(newArr);
							  
						  });
					}
					 
				}
			});
			$rootScope.stopLoader();
		}
		
	}

	$scope.getDetailsEntitlement = function(type)
	{
		/*if(type == '2')
		{
			
		}*/
		if(type == '2')
			{
			
			var param = {organizationId:$scope.orgId,unitId:$scope.unitId,companyTypeId:type};
		    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getCompanyMasterList"
		    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					 $scope.companyMasterInsuranceList = response.data.listObject;
				}
			});
			}
		if(type == '3')
			{
			     var param = {organizationId:$scope.orgId,unitId:$scope.unitId,companyTypeId:type};
				    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getCompanyMasterList"
				    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success"){
							 $scope.companyMasterList = response.data.listObject;
							 console.log("companyMasterList",JSON.stringify($scope.companyMasterList));
						}
					});
				  
			}
	}
	

	
	
	
	$scope.getContractByCompanyId = function(companyId)
	{
		console.log("getContractByCompanyId ",companyId);
		/*if(companyId!=undefined && companyId!="" && companyId!=null && !isNaN(companyId)){
				$scope.corporateCompanyId=companyId;
				$scope.enableDisabledValidated=true;
				$scope.enableDisabledApply=false;
				$scope.showHidevalidate=true;
		}else{
			$scope.corporateCompanyId="";
			$scope.enableDisabledValidated=false;
			$scope.enableDisabledApply=true;
		}*/
		var companyTypeId = $scope.appointmentMasterObj.paymentEntitlementTypeId;
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
		var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyId:companyId,companyTypeId:companyTypeId,visitTypeId:$scope.updatePayeeObj.visitTypeId};
		 $scope.contractList = [];
		 $scope.paymentEntitleMentCorporate[0].contractId = '';
		  $scope.paymentEntitleMentCorporate[0].datepickerCons ='';
		  $scope.paymentEntitleMentCorporate[0].datepickerConE ='';
		console.log("param ",param);
	    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getContractByCompanyId";
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
			 console.log("companyTypeId",JSON.stringify(response));
		});
	}
	$scope.getInsuranceContract = function(insuranceItem)
	{
		var companyTypeId = $scope.appointmentMasterObj.paymentEntitlementTypeId;
		var companyId = insuranceItem.companyId;
			var asscoiatedCompanyId = insuranceItem.associateCompanyId;
				
				//var param = {organizationId:$scope.orgId,unitId:$scope.unitId,companyId:companyId};
				var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyId:companyId,companyTypeId:companyTypeId,associateCompanyId:asscoiatedCompanyId,visitTypeId:$scope.updatePayeeObj.visitTypeId};
				$scope.assoContractList = [];
				$scope.paymentEntitleMentInsurance[0].contractId = '';
				
				var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getContractByCompanyId"
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						 $scope.assoContractList = response.data.listObject;
						 console.log("companyMasterList",JSON.stringify($scope.assoContractList));
					}
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
		
/*		var tarrifName = $.grep($scope.tariffList, function (doc) {
            return doc.tariffId == $scope.paymentEntitleMentCorporate[0].tariffId;
        })[0].tariffDesc;*/
		
		
		console.log("contractObj",contractObj);
		if(contractObj != undefined || contractObj != null)
		{
			/*if(contractObj.billingBedCategoryId != $scope.updatePayeeObj.billingBedCategoryId){
				
				angular.element('#transferredPopup').modal('show');
				return false;
				
				}
		            else{*/
						 $scope.getBedPopUpFlag = false;
					 // var payEntList = angular.copy($scope.paymentEntitleMentList);
					  //$scope.paymentEntitleMentList = [];
					  if($scope.appointmentMasterObj.paymentEntitlementTypeId == '3')
					  {
						  
						  var prioObj = {
								  id:$scope.priorityList.length + 1,
								  value:$scope.priorityList.length + 1,
							  }
							  $scope.priorityList.push(prioObj);
						  
					  var newArr = {};
					  newArr.paymentEntitlementName=contractObj.paymentEntitlementName;
					  newArr.paymentEntitlementId="3";
					  newArr.companyName = contractObj.assCompanyName;
					  newArr.companyId = contractObj.companyId;
					 // newArr.tariffName = tarrifName;
					  newArr.contractName = contractObj.contractName;
					  newArr.insuranceName = "";
					  newArr.insuranceNumber = "";
					  if($scope.employeeNumberManual != null || $scope.employeeNumberManual != undefined || $scope.employeeNumberManual != '')
						  newArr.employeeNoManual = $scope.employeeNumberManual;
					  //newArr.startDate = moment($scope.paymentEntitleMentCorporate[0].datepickerCons).format('DD-MM-YYYY HH:mm:ss');//contractObj.validityFrom;
					  //newArr.endDate = moment($scope.paymentEntitleMentCorporate[0].datepickerConE).format('DD-MM-YYYY HH:mm:ss');//contractObj.validityTo;datepickerConE
					  newArr.startDate = $rootScope.getChangedFormatedDateShalse($scope.paymentEntitleMentCorporate[0].datepickerCons);
					  newArr.endDate =$rootScope.getChangedFormatedDateShalse($scope.paymentEntitleMentCorporate[0].datepickerConE);
					  newArr.priority = "";
					  newArr.tariffId = $scope.paymentEntitleMentCorporate[0].tariffId;
					  newArr.contractId = contractObj.contractId;
					  newArr.insuredAmount = '';
					  newArr.typeId = 3;
					  newArr.billingBedCategoryId = contractObj.billingBedCategoryId;
					  
			
					  isInsuranceExists=checkInsuranceExistsOrNot($scope.paymentEntitleMentList,newArr)
						if (!isInsuranceExists) {
							$scope.enableDisabledApply=false;
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
							angular.forEach($scope.paymentEntitleMentList, function(value) {
								if(value.priority == 1){
									$scope.updatePayeeObj.paymentEntitlementDesc = value.paymentEntitlementName;
								}
								else{
									$scope.updatePayeeObj.paymentEntitlementDesc = '';
								}
							});
							$scope.paymentEntitleMentCorporate=
								[{
									'companyId':"",
									'contractId':"",
									//'statrDate':"",
									//'endDate':""
								}];
			                $scope.employeeNumberManual= '';

						}
						else
							{
								growl.warning("Already added in the list",{
									title : "WARNING"
								});
								return;
							}
					  
					  
					  
					  
					  }
				/*}*/
		  
				}
			}
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
		  angular.forEach($scope.paymentEntitleMentList, function(value) {
				if(value.priority == 1){
					$scope.updatePayeeObj.paymentEntitlementDesc = value.paymentEntitlementName;
				}
				/*else{
					$scope.updatePayeeObj.paymentEntitlementDesc = '';
				}*/
			});
	}
	/*$scope.gotoTransferPage = function()
	{
		angular.element('#transferredPopup').modal('hide');
		$state.go("initiateBedTransfer.initiateRequestTransferBed");
	}*/
	
	
	
	/*$scope.getContractDetails = function(id,idx)
	{
		if($scope.patientDetails.patientId== undefined ||$scope.patientDetails.patientId == ''){
			growl.error("Please Select Patient ",{
    			title : ERROR_MSG
    		});
			return;
		}else{
			
			//alert(idx);
			var contractObj = null;
			contractObj = $.grep($scope.contractList, function (doc) {
	            return doc.contractId == id;
	        })[0];
			console.log("contractObj",contractObj);
			if(contractObj != undefined || contractObj != null)
				{
				 //alert("Some Values");
				  $scope.paymentEntitleMentCorporate[0].statrDate = contractObj.validityFrom;
				  $scope.paymentEntitleMentCorporate[0].endDate = contractObj.validityTo;
				  var payEntList = angular.copy($scope.paymentEntitleMentList);
				  $scope.paymentEntitleMentList = [];
				  if($scope.appointmentMasterObj.paymentEntitlementTypeId == '3')
				  {
				  var newArr = {};
				  newArr.paymentEntitlementName=contractObj.paymentEntitlementName;
				  newArr.patientSponsorDetailsId=contractObj.patientSponsorDetailsId;
				  newArr.paymentEntitlementId="3";
				  newArr.companyName = contractObj.assCompanyName;
				  newArr.companyId = contractObj.companyId;
				  newArr.tariffName = contractObj.tariffName;
				  newArr.contractName = contractObj.contractName;
				  newArr.insuranceName = "";
				  newArr.insuranceNumber = "";
				  newArr.startDate = contractObj.validityFrom;
				  newArr.endDate = contractObj.validityTo;
				  newArr.priority = 1;
				  newArr.tariffId = contractObj.tariffId;
				  newArr.contractId = contractObj.contractId;
				  if($scope.CompanyObjDoc.companyId!=undefined && $scope.CompanyObjDoc.companyId!=null && $scope.CompanyObjDoc.companyId!="" && $scope.paymentEntitleMentCorporate[0].companyId==$scope.CompanyObjDoc.companyId){
					  newArr.reportPath=$scope.CompanyObjDoc.reportPath;
				  }
				  $scope.paymentEntitleMentList.push(newArr);
				  
				  }
				  angular.forEach(payEntList, function (value, key) {
					  if(value.paymentEntitlementId == "1")
						  {
						  var newArr = {};
						  //var tempPriority=value.priority+1;
						  newArr.paymentEntitlementName=value.paymentEntitlementName;
						  newArr.patientSponsorDetailsId=value.patientSponsorDetailsId;
						  newArr.paymentEntitlementId="1";
						  newArr.companyName = value.companyName;
						  newArr.companyId = value.companyId;
						  newArr.tariffName = value.tariffName;
						  newArr.contractName = value.contractName;
						  newArr.insuranceName = "";
						  newArr.insuranceNumber = "";
						  newArr.startDate = value.startDate;
						  newArr.endDate = value.endDate;
						  newArr.priority = value.priority;
						  newArr.tariffId = value.tariffId;
						  newArr.contractId = value.contractId;
						  $scope.paymentEntitleMentList.push(newArr);
						  }
					  
				  });
				  
				}
			
		}
		
		
	}*/
	
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
		console.log($scope.paymentEntitleMentInsurance[0]);
			 //var isEmpty = !(Object.values($scope.paymentEntitleMentInsurance[0]).every(x =>(x != null &&  x != '')));
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
		
		var associatedName = $.grep($scope.associateCompanyMasterList, function (doc) 
			{
            return doc.id == $scope.paymentEntitleMentInsurance[0].associateCompanyId;
        })[0].name;
		
		var insuranceId = $.grep($scope.associateCompanyMasterList, function (doc) 
				{
	            return doc.id == $scope.paymentEntitleMentInsurance[0].associateCompanyId;
	        })[0].id;
		console.log("$scope.paymentEntitleMentInsurance",$scope.paymentEntitleMentInsurance);
		
		if(insuranceObj != undefined || insuranceObj != null)
		{
				/*if(insuranceObj.billingBedCategoryId != $scope.updatePayeeObj.billingBedCategoryId){
					
					angular.element('#transferredPopup').modal('show');
					return false;
				}*/
				/*else{*/
						 $scope.getBedPopUpFlag = false;
					  if($scope.appointmentMasterObj.paymentEntitlementTypeId == '2')
					  {
						  var isAfter = true;
						  /*var sInsDt =  moment($scope.paymentEntitleMentInsurance[0].datepickerInss).format('DD-MM-YYYY HH:mm:ss');
						  var eInsDt =  moment($scope.paymentEntitleMentInsurance[0].datepickerInsE).format('DD-MM-YYYY HH:mm:ss');
						  
						  var isAfter = moment($scope.paymentEntitleMentInsurance[0].datepickerInsE).isAfter($scope.paymentEntitleMentInsurance[0].datepickerInss);
						 */
						  if(!isAfter)
							  {
								  growl.warning("Start date should be less then end date..!!",{
										title : "WARNING"
									});
									return false;
							  }else{
								  
								  var prioObj = {
									  id:$scope.priorityList.length + 1,
									  value:$scope.priorityList.length + 1,
								  }
								  $scope.priorityList.push(prioObj);
					  var newArr = {};
					  newArr.paymentEntitlementName=insuranceObj.paymentEntitlementName;
					  newArr.paymentEntitlementId="2";
					  newArr.companyName = insuranceObj.assCompanyName;
					  newArr.companyId = insuranceObj.companyId;
					  newArr.contractName = insuranceObj.contractName;
					  newArr.insuranceName = associatedName;
					  newArr.insuranceId = insuranceId;
			          newArr.employeeNoManual = '';
					  newArr.insuranceNumber = $scope.paymentEntitleMentInsurance[0].insuranceNumber;
					//  newArr.startDate = moment($scope.paymentEntitleMentInsurance[0].datepickerInss).format('DD-MM-YYYY');//contractObj.validityFrom;
					 // newArr.endDate = moment($scope.paymentEntitleMentInsurance[0].datepickerInsE).format('DD-MM-YYYY');//contractObj.validityFrom;datepickerInsE
					  newArr.startDate = $scope.paymentEntitleMentInsurance[0].datepickerInss != null ?moment($scope.paymentEntitleMentInsurance[0].datepickerInss).format('DD-MM-YYYY HH:mm:ss'): null;//contractObj.validityFrom;
					  newArr.endDate = $scope.paymentEntitleMentInsurance[0].datepickerInsE != null ? moment($scope.paymentEntitleMentInsurance[0].datepickerInsE).format('DD-MM-YYYY HH:mm:ss'): null;//contractObj.validityFrom;datepickerInsE/ newArr.endDate = moment($scope.paymentEntitleMentInsurance[0].datepickerInsE).format('DD-MM-YYYY HH:mm:ss');//contractObj.validityFrom;datepickerInsE
					  newArr.priority = "";
					  newArr.tariffId = $scope.paymentEntitleMentInsurance[0].tariffId;
					  newArr.contractId = insuranceObj.contractId;
					  newArr.typeId = 2;
					  newArr.insuredAmount = $scope.paymentEntitleMentInsurance[0].insuredAmt;
					  newArr.billingBedCategoryId = insuranceObj.billingBedCategoryId;
					  
					  isInsuranceExists=checkInsuranceExistsOrNot($scope.paymentEntitleMentList,newArr)
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
							angular.forEach($scope.paymentEntitleMentList, function(value) {
								if(value.priority == 1){
									$scope.updatePayeeObj.paymentEntitlementDesc = value.paymentEntitlementName;
									}
								else{
									$scope.updatePayeeObj.paymentEntitlementDesc = '';
								}
							});
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
		        /* }	 */ 
		  
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
	
	/*$scope.removeContract = function(index)
	{
		console.log("removeIndex",index);
		var newDataList = [];
		var le = $scope.paymentEntitleMentList.length;
		var i = 1;
		if(le != i){
		angular.forEach($scope.paymentEntitleMentList, function(v) {
			if (index != i) {
				if(v.priority > 1){
				v.priority = v.priority - 1;
				}
				newDataList.push(v);
			}
			i++;
		});
		
		$scope.paymentEntitleMentList = newDataList;
		$scope.paymentEntitleMentCorporate =[];
		$scope.addContracts();
		}
	}
	*/
	/*$scope.removeContract = function(index,obj){
		if(obj.patientSponsorDetailsId ==null ||obj.patientSponsorDetailsId== undefined ||obj.patientSponsorDetailsId == ''){
			$scope.paymentEntitleMentList.splice(index,1);
		}
		
	}*/
	
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
		/*	if($scope.corEmpVerificationFlag){
				$scope.enableDisabledValidated=false;
				$scope.enableDisabledApply=true;	
			}else{
				$scope.enableDisabledValidated=false;
				$scope.enableDisabledApply=true;
			}*/
			$scope.enableDisabledApply=false;	
			$scope.enableDisabledValidated=false;
			
			$scope.paymentEntitleMentCorporate=
				[{
					'companyId':"",
					'contractId':"",
					//'statrDate':"",
					//'endDate':""
				}];
            $scope.employeeNumberManual= '';
		});
		
		var len = $scope.priorityList.length;
		 $scope.priorityList.splice(len-1,1);
		
		$scope.paymentEntitleMentList = newDataList;
		angular.forEach($scope.paymentEntitleMentList, function(value) {
			if(value.priority == 1){
				$scope.updatePayeeObj.paymentEntitlementDesc = value.paymentEntitlementName;
			}
			else{
				$scope.updatePayeeObj.paymentEntitlementDesc = '';
			}
		});
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
		 var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyId:companyId};
		 
		 $scope.associateCompanyMasterList = [];
		 $scope.paymentEntitleMentInsurance[0].associateCompanyId = '';
		 
		    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getAssociateCompanyMaster"
		    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					 $scope.associateCompanyMasterList = response.data.listObject;
					  console.log("associateCompanyMasterList",JSON.stringify($scope.associateCompanyMasterList));
				}
			});
	}
	
	 var isPriorityZeroExists;
		function checkPriorityZeroOrNot(arr) {
			return arr
					.some(function(arr) {
						if(parseInt(arr.priority) == 0 || arr.priority == "")
							{
								return true;
							}
						else
							{
								return false;
							}
					});
		}
		
		$scope.savePatientSponsorDetailsDtosListPopup= function(){
			
			isPriorityZeroExists=checkPriorityZeroOrNot($scope.paymentEntitleMentList);
			  if (isPriorityZeroExists) {
				
				  growl.warning("Please select priority of all paymententitlement list.",{	
					title : "WARNING"
				});
				// $scope.tab = 3;
				return;
				
			  }else if($scope.updatePayeeObj.patientId==undefined || $scope.updatePayeeObj.patientId==null || $scope.updatePayeeObj.patientId=='' || $scope.updatePayeeObj.patientId==0){
				  growl.warning("Please select patient.",{	
						title : "WARNING"
					});
			  }
			  else{
					angular.element('#saveConfirmPopup').modal('show');
			  }
		}
		
	$scope.savePatientSponsorDetailsDtosList= function(){
		
	/*	isPriorityZeroExists=checkPriorityZeroOrNot($scope.paymentEntitleMentList);
		  if (isPriorityZeroExists) {
			
			  growl.warning("Please select priority of all paymententitlement list.",{	
				title : "WARNING"
			});
			// $scope.tab = 3;
			return;
			
		  }
		  else */
		  {
				
			 /* $scope.updatePayeeObj = {
						visitNo:'',
						paymentEntitlementDesc:'',
						paymentEntitlementId:'',
						visitTypeId:'',
						billingBedCategoryId:'',
						bedCategoryId:''
				}*/
			  
			  angular.element('#saveConfirmPopup').modal('hide');
			  var data = {
					  patientCategoryId:$scope.updatePayeeObj.patientCategoryId,
					  visitTypeId:$scope.updatePayeeObj.visitTypeId,
					  selfPayeeId:$scope.selfPayeeId,
					  defaultSelfTariffId:$scope.defaultSelfTariffId,
					  admissionId:$scope.patientDetails.admissionId,
					  encounterId : $scope.updatePayeeObj.encounterId,
					  billingBedCategoryId:$scope.updatePayeeObj.billingBedCategoryId,
					  isPreBilling:$scope.isPreOrPostBilling,
					  patientId:$scope.updatePayeeObj.patientId,
					  orgId: $scope.organizationId,
					  unitId: $scope.unitId,
					  referralId: $scope.updatePayeeObj.referralId,
					  patientClassId: $scope.updatePayeeObj.patientClassId,
					  pharmacyServiceId : $scope.pharmacyServiceId
			  };
				console.log('$scope.paymentEntitleMentList',$scope.paymentEntitleMentList);
				var sponserList = [];
				angular.forEach($scope.paymentEntitleMentList, function (value, key) {
					var newArr = {};
					newArr.organizationId = $scope.organizationId ;
					newArr.unitId = $scope.unitId;
					newArr.visitTypeId = $scope.updatePayeeObj.visitTypeId;
					newArr.paymentEntitlementId =value.paymentEntitlementId;
					newArr.payeeId = value.companyId;
					newArr.priorityId = value.priority;
					newArr.createdBy =$scope.userId;
					newArr.createdDate = $scope.currentDate;
					newArr.updatedBy = $scope.userId;
					newArr.updatedDate = $scope.currentDate;
					newArr.status = 'A';
					newArr.tariffId =value.tariffId;
					newArr.contractId =value.contractId;
					newArr.insuranceNumber = value.insuranceNumber;
					newArr.insuredAmount = value.insuredAmount;
					newArr.insuranceId = value.insuranceId;
					if(value.paymentEntitlementId != 1){
						newArr.validityTo = (value.endDate != null || value.endDate != undefined) ? value.endDate+" 00:00:00" : null;
	                    newArr.validityFrom = (value.startDate != null || value.startDate != undefined) ? value.startDate+" 00:00:00" : null; 
					}else{
						newArr.validityTo = value.endDate;
						newArr.validityFrom = value.startDate;
					}
					
					if(value.priority == 1)
						{
							data.contractId = value.contractId;
							data.paymentEntitlementId = value.paymentEntitlementId;
						}
					newArr.employeeNoManual = value.employeeNoManual;
					sponserList.push(newArr);
				});
				data.patientSponsorDetailsDtoList = sponserList;
				
				console.log("data",data);
				//return false;
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL
						+ "/api/billing/changePayee";
				GenericService
						.serviceAction("POST", URI, data)
						.then(
								function(response) {

									if (response.data.status == "success") {
										growl
												.success(
														'Updated Successfully..!!',
														{
															title : 'Success!'
														});
										
										$rootScope.stopLoader();
										$scope.init();
										
									} else
										{
										  $rootScope.stopLoader();
										}

								});
				
				}
		
		/*var sponserList = [];
		angular.forEach($scope.paymentEntitleMentList, function (value, key) {
			var newArr = {};
			newArr.organizationId = $scope.organizationId ;
			newArr.unitId = $scope.unitId;
			newArr.visitTypeId = 1;
			//newArr.encounterId =$scope.appointmentMasterObj.encounterId;....patientSponsorDetailsDtosList
			newArr.paymentEntitlementId =value.paymentEntitlementId;
			newArr.patientSponsorDetailsId=value.patientSponsorDetailsId;
			newArr.payeeId = value.companyId;
			newArr.priorityId = value.priority;
			newArr.createdBy =$scope.userId;
			newArr.createdDate = $scope.currentDate;
			newArr.updatedBy = $scope.userId;
			newArr.updatedDate = $scope.currentDate;
			newArr.status = 'A';
			newArr.tariffId = value.tariffId;
			newArr.contractId = value.contractId;
			newArr.encounterId=$scope.payeeInfoByVisitTypeData.encounterId
			sponserList.push(newArr);
		});
		$scope.appointmentMasterObj.patientSponsorDetailsDtosList = sponserList;
		JSON.stringify(sponserList)
		console.log("sponserList",JSON.stringify(sponserList));
		//return false;
		 $rootScope.startLoader();
		 var URI = BASE_URL + ROOT_URL + "/api/opd/addPatientSponsorDetails"
		BillingGenericService.serviceAction(METHOD_POST, URI, sponserList, NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
					if (response.data.status == "success") {
						console.log("response",JSON.stringify(response));
					}
	
			});*/
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
	//====================code for  document=========================
	//adding and removing images
	$scope.addImageDetails = function(obj)
	{ console.log("add image ",obj);
		var encounterDetails = angular.copy($scope.encounterDetails);
		encounterDetails.admissionId = obj.admissionId;
		encounterDetails.patientId = obj.patientId;
		if( obj.paymentEntitlementId==2){
			encounterDetails.documentTypeId = '30';	
			
		}else if(obj.paymentEntitlementId==3){
			encounterDetails.documentTypeId = '19';	
		}else{
			
		}
		$scope.encounterDetailsList.push(encounterDetails);
	};

	$scope.removeImageDetails = function(deleteIndex)
	{ 
	   $scope.encounterDetailsList.splice(deleteIndex, 1);
    }	
	//getting document list
	 $scope.getPatientDocumentList = function(obj)
     {
      $rootScope.startLoader();
      var data =
        {
        "patientId":obj.patientId,
        "unitId":$scope.unitId,
        "organisationId":$scope.organizationId,
        "userId":$scope.userId,
        "visitTypeId":2,
        "admissionId":obj.admissionId,
       }
      var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
      BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
        $scope.patientAllDocumentList = [];
        if (response.data.status == "success") 
        {
         $scope.patientAllDocumentList = response.data.listObject;
         $scope.patientDocumentList=[];
         angular.forEach($scope.patientAllDocumentList, function(value, key) 
					{  
        	 
        	 			/*if(obj.paymentEntitlementId==1){
        	 				if(value.documentTypeId!=30 && value.documentTypeId!=19){				            				
    		         			
    	            			var data={
    	            					documentTypeId:value.documentTypeId,
    	            					documentName:value.documentName,
    	            					documentPath:value.documentPath
    	            			}
    	            			$scope.patientDocumentList.push(data);
             			    }
        	 			}else if(obj.paymentEntitlementId==2){
        	 				// for insurance document
        	 				if(value.documentTypeId==30){				            				
    		         			
    	            			var data={
    	            					documentTypeId:value.documentTypeId,
    	            					documentName:value.documentName,
    	            					documentPath:value.documentPath
    	            			}
    	            			$scope.patientDocumentList.push(data);
             			    }
        	 			}else if(obj.paymentEntitlementId==3){
        	 				//for corporate document
        	 				if(value.documentTypeId==19){	            				
        	         			
		            			var data={
		            					documentTypeId:value.documentTypeId,
		            					documentName:value.documentName,
		            					documentPath:value.documentPath
		            			}
		            			$scope.patientDocumentList.push(data);
	         			  }
        	 			}*/
			        	 if(value.documentTypeId==19){				            				
			     			
			     			var data={
			     					documentTypeId:value.documentTypeId,
			     					documentName:value.documentName,
			     					documentPath:value.documentPath,
			     					createdDate:value.createdDate
			     			}
			     			$scope.patientDocumentList.push(data);
			 			}
			 			if(value.documentTypeId==30){				            				
			     			
			     			var data={
			     					documentTypeId:value.documentTypeId,
			     					documentName:value.documentName,
			     					documentPath:value.documentPath,
			     					createdDate:value.createdDate
			     			}
			     			$scope.patientDocumentList.push(data);
			 			}
			 			if(value.documentTypeId !=30 && value.documentTypeId!=19){				            				
			     			
			     			var data={
			     					documentTypeId:value.documentTypeId,
			     					documentName:value.documentName,
			     					documentPath:value.documentPath,
			     					createdDate:value.createdDate
			     			}
			     			$scope.patientDocumentList.push(data);
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
	
	$scope.openDocument=function(obj){
		console.log("payment ent obj",obj);
		$('#fileId').val(undefined);
		$scope.encounterDetailsList = [];
		$scope.encounterDetails = {
			  encounterDocId : "",
			  encounterId : "",
			  admissionId:"",
			  visitTypeId:2,
			  patientId : "",
			  documentPath : "",
			  documentTypeId : "",
			  status : 'A',
			  createdDate : "",
			  createdBy : $scope.userId,
			  updatedDate : "",
			  updatedBy : $scope.userId,
			  unitId : $scope.unitId,
			  organisationId : $scope.organizationId,
			  paymentEntitlementId : "",
			  file : {}
			}
		//1-self,2-insurance,3-corporate 
		if(obj.paymentEntitlementId==1){			
			
			$scope.encounterDetailsList =[];
			
			var encounterDetails = angular.copy($scope.encounterDetails);
			encounterDetails.admissionId = $scope.patientDetails.admissionId;
			encounterDetails.patientId = $scope.patientDetails.patientId;
			encounterDetails.paymentEntitlementId = obj.paymentEntitlementId;
			$scope.encounterDetailsList.push(encounterDetails);			
			$('#commonUploadDocuments').modal('show');
			$scope.paymentEntId=obj.paymentEntitlementId;
			$scope.getPatientDocumentList(encounterDetails);
		}else if(obj.paymentEntitlementId==2){
			
			$scope.encounterDetailsList =[];			
			var encounterDetails = angular.copy($scope.encounterDetails);
			encounterDetails.admissionId = $scope.patientDetails.admissionId;
			encounterDetails.patientId = $scope.patientDetails.patientId;	
			encounterDetails.paymentEntitlementId = obj.paymentEntitlementId;
			encounterDetails.documentTypeId = '30';	
			$scope.encounterDetailsList.push(encounterDetails);			
			$('#commonUploadDocuments').modal('show');
			$scope.paymentEntId=obj.paymentEntitlementId;
			$scope.getPatientDocumentList(encounterDetails);
			
		}else if(obj.paymentEntitlementId==3){
			$scope.encounterDetailsList =[];			
			var encounterDetails = angular.copy($scope.encounterDetails);
			encounterDetails.admissionId = $scope.patientDetails.admissionId;
			encounterDetails.patientId = $scope.patientDetails.patientId;	
			encounterDetails.paymentEntitlementId = obj.paymentEntitlementId;
			encounterDetails.documentTypeId = '19';	
			$scope.encounterDetailsList.push(encounterDetails);			
			$('#commonUploadDocuments').modal('show');
			$scope.paymentEntId=obj.paymentEntitlementId;
			$scope.getPatientDocumentList(encounterDetails);
		}else{
			
		}
	}
	
	$scope.uploadEncounterDoc = function(Obj)
	{
		$('#commonUploadDocuments').modal('hide');
		 var fd = new FormData();
	        fd.append('encounterId', 0);
	        fd.append('visitTypeId', Obj.visitTypeId);
	        fd.append('admissionId', Obj.admissionId !=null ?Obj.admissionId:0);
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
	
	 $scope.uploadDoc = function()
	    {
		 $rootScope.startLoader();
		 $scope.finalDocUploadList=[];
		  angular.forEach($scope.encounterDetailsList, function(value, key) {  
			  if(value.documentTypeId==undefined || value.documentTypeId=="" || value.documentTypeId==null){
					 growl.warning("Please select document type .",
												{
													title : 'warning!'
												});
									   $rootScope.stopLoader();
					return false;
				}
//			  if (value.file == undefined || value.file == "" || value.file == null ) {
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
		});
		  if($scope.encounterDetailsList.length==$scope.finalDocUploadList.length){
			  
		
		  $scope.count = 0;
		 angular.forEach($scope.encounterDetailsList, function(value, key) 
			{  
				var promise = $scope.uploadEncounterDoc(value);
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
								   $rootScope.stopLoader();
								   $scope.encounterDetailsList = [];
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
	    }
		$scope.openImage = function(index)
        { 
			$rootScope.startLoader();
			var imageUrl = $scope.patientDocumentList[index].documentPath;
			var href = $rootScope.currentHrefDoccument();
			$.fancybox.open({
				width: 600, // or whatever
				height: 320,
				href:href+imageUrl,
				type: 'iframe'
			});
			$rootScope.stopLoader();
        }
		
	
		
		
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
					angular.element('#validationEmployeeModel').modal('show');
			 }
		}
		$scope.closePopupValidate = function()
		{
			angular.element('#validationEmployeeModel').modal('hide');
		}
		
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
//						genderId:$scope.patientDetails.genderId,
//						birthDate:$scope.patientDetails.birthDate
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
							 console.log("$scope.corEmplist",$scope.corEmpObj);
//							 $scope.initCoEmp();
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
								
							}else{
								  $scope.color="red";
								  $scope.showNameDetails=true;
								  $scope.name="record not found.."
								  $scope.showHideAcceptRejectBtn=false;
								  $scope.showHideOkBtn=true;
							}
						}
//						$scope.initCoEmp();
					});
			}
			
			$scope.getEmpDependantDetailsByIndex=function(index){
				$scope.showNameDetails=false;
				     if($scope.patientDetails.firstName==$scope.corEmpDependantList[index].firstName) {
//				    	 $scope.name=$scope.corEmpDependantList[index].firstName+" "+$scope.corEmpDependantList[index].middleName+" "+$scope.corEmpDependantList[index].lastName;
				    	 if($scope.patientDetails.genderId==$scope.corEmpDependantList[index].genderId){
				    		       
//				    		        $scope.color="green";
									$scope.enableDisabledValidated=false;										
									$scope.showHideAcceptRejectBtn=true;
								
				    	 }else{
//				    		    $scope.color="red";
								$scope.enableDisabledValidated=false;										
								$scope.showHideAcceptRejectBtn=true;
							}
				     }else{
//				    	   $scope.color="red";
							$scope.enableDisabledValidated=false;										
							$scope.showHideAcceptRejectBtn=true;
						}
				 	$scope.showNameDetails=true;
//				     $scope.initCoEmp();
			}
			
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
			
			$scope.saveMannualDetails=function(){
				if($scope.empObj.employeeNumber==undefined || $scope.empObj.employeeNumber=="" || $scope.empObj.employeeNumber==null)
				{
					growl.warning("Please enter employee number.",{
						title : WARNING_MSG
					});
					return;
				
				}
			else if($scope.corEmpObj.note==undefined || $scope.corEmpObj.note=="" || $scope.corEmpObj.note==null){
					growl.warning("Please enter note",{
						title : WARNING_MSG
					});
					return;
				}
				$scope.enableDisabledApply=true;	
				$scope.enableDisabledValidated=false;
				$('#validationEmployeeModel').modal('hide');
                $scope.employeeNumberManual = $scope.empObj.employeeNumber;
				$scope.initEmpObj();
			}
			
			$scope.getTypeOfCoEmp=function(id){
				console.log("getTypeOfCoEmp id ",id);
				$scope.empObj.employeeNumber = '';
				$scope.corEmpObj.note = '';
				  if(id=="1"){
					  $scope.corEmpObj.typeOfCoEmp="1";
					  $scope.dependTblShowHode=false;
					  $scope.noteShowHide=false;
					  $scope.showHidevalidate=true;
					  $scope.showHideSaveBtn=false; 
					  $scope.showNameDetails=false;
					  $scope.showHideOkBtn=false;
				  }
				  if(id=="2"){
					  $scope.corEmpObj.typeOfCoEmp="2";
					  $scope.dependTblShowHode=true;
					  $scope.showHidevalidate=true;
					  $scope.noteShowHide=false;
					  $scope.showHideSaveBtn=false; 
					  $scope.showNameDetails=false;
					  $scope.showHideOkBtn=false;
				  }
				  if(id=="3"){
					  $scope.corEmpObj.typeOfCoEmp="3";
					  $scope.dependTblShowHode=false;
					  $scope.noteShowHide=true; 
					  $scope.showHidevalidate=false; 
					  $scope.showHideSaveBtn=true; 
					  $scope.showNameDetails=false;
					  $scope.showHideAcceptRejectBtn=false;
					  $scope.showHideOkBtn=false;
				  }
			}
			
			
			 $scope.getClearData = function(type)
			    {
				
			    	$scope.admissionType = type;
			    	//$scope.initPatientSelected();
			    	$scope.init();
			    	$scope.patientList = [];
			    	$scope.autoRenderedServicesFinalList =[];
			    	
			    }
		

}]);
	