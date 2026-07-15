
/**
 * 04-07-2018
 * VivekS.
 * IPCCharges Controller
 */

(function(){
	"use strict";
	
	function consentPrintController($scope,$rootScope,$cookies,$state,BillingGenericService,GenericService,$stateParams,$timeout,growl,paymentCollFactory,GenericServiceParamHeader,packageService,reportService,servicesFactory,GenericServiceFileUpload,$window){
		var self = this;
		
		
		$scope.admissionType = 1;
		$scope.isOpoShowOrNot = true;
		$scope.loadingServiceFlag = false;
		$scope.encounterObj = {"encounterId" : "","encounterNumber": "" };
		$scope.consentTransactionId = null;
		
		$scope.init = function() {
			
			
	        $scope.dateOptions = 
	        {
					formatYear : 'yyyy',
					showWeeks : false,
					maxDate:new Date()
				};
	        
	        
	      //Patient Search DOB Start
			$scope.openDatePicker = function() {
				$scope.datepicker.opened = true;
			};
			
			$scope.datepicker = {
					opened : false
				};
			//Patient Search DOB End
	
			
			
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
			else
				{
					console.log('cookieObjectAppConfig',cookieObjectAppConfig);
					
				}
			
			var labConfig = JSON.parse(localStorage.getItem('labConfig'));
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
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
			$scope.isGstVisible = cookieObjectAppConfig.isGstVisible;
		    $scope.defaultDiscountOwnership = cookieObjectAppConfig.defaultDiscountOwnership;
			
			$scope.unitId = cookieObject.unitId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.visitTypeId='';
			$rootScope.loginpage = true;
			$scope.currentDate = new Date();
			$scope.itemSelecteds = {};
			$scope.patientList  = [];
			 

			 
			 $scope.patientFullName="";
				$scope.uhidNum="";
				$scope.genderCode="";
				$scope.dateOfAdmission = "";
				$scope.patientDischargeDate = "";
				$scope.bedNumber = "";
				$scope.doctorFullName = "";
				
			 
				
			
			var commonObj = {
					unitId : $scope.unitId,
					organizationId : $scope.organizationId
				};
			
			
			
			// for popup
			$scope.showModal = false;
			$scope.buttonClicked = "";
			$scope.popUpFlag = true;
			// end for popup
			
			
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
		
		    


			self.model = {
					"unitId"          :$rootScope.unitId,
					"organizationId"  :$rootScope.organizationId,
					"visitTypeId" : 2,
					"groups":[]
			}
			$scope.searchPatient = searchPatient;
			$scope.getPatientDetailsByIndex = getPatientDetailsByIndex;
			$scope.encounterObj = {"encounterId" : "","encounterNumber": "" };
			
			}	
		
		$scope.initPatientSelected = function() {
			$scope.encounterObj = {"encounterId" : "","encounterNumber": "" };
			$scope.itemSelecteds = {};
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
				
			$scope.searchPatient = searchPatient;
			
		      $scope.patientDetails = {
					"patientId"	  : "",
					"patientName" : "",
					"genderCode"  : "",
					"age"         : "", 
					"uhIdNumber"  : "",
					"birthDate"	  : "",
					"encounterId" : ""
			}
			
			
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
		
		var searchPatient = function(patientSearchObj) {
			if(patientSearchObj.uhIdNumber == "" && patientSearchObj.patientName == "" && patientSearchObj.genderId == "" && patientSearchObj.mobileNo == ""  && patientSearchObj.identificationNo == "" && patientSearchObj.birthDate=="")
			{
				growl.warning(SearchFilter_REQUIRED,{title : "WARNING!"});
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
			
			if(patientSearchObj.birthDate != "" && typeof(patientSearchObj.birthDate) != "undefined"){
				patientSearchObj.birthDate = moment(patientSearchObj.birthDate).format('YYYY-MM-DD');
			} 
			var patSearchObj = {};
			patSearchObj.uhidNumber = patientSearchObj.uhIdNumber;
			patSearchObj.birthDate = patientSearchObj.birthDate;
			patSearchObj.genderId = patientSearchObj.genderId;
			patSearchObj.identificationNumber = patientSearchObj.identificationNo;
			patSearchObj.mobileNo = patientSearchObj.mobileNo;
			patSearchObj.organizationId = $scope.organizationId;
			patSearchObj.patientName = patientSearchObj.patientName;
			patSearchObj.patientCategoryId = patientSearchObj.patientType;
			patSearchObj.unitId = $scope.unitId;
			patSearchObj.visitTypeId = patientSearchObj.visitTypeId;
			patSearchObj.registrationTypeIdList  = [1,2,3,4,6];
			
			var URI = BASE_URL + ROOT_URL
					+ GETTEMPORYPATIENTFORPREREGISTRATION;
			BillingGenericService.serviceAction(METHOD_POST, URI,
					patSearchObj,
							NOTIFICATION_MSG_STATUS_FALSE)
					.then(
							function(response) {
								$scope.patientList = [];
								angular.forEach(response.data.listObject,function(value,index){
									if($scope.admissionType == 1 && (value.encounterCount!=null && value.encounterCount>0)){
										value.uhIdNumber = value.uhidNumber;
										value.patientName = value.prefixCode + " "+ value.firstName + " "+value.middleName+" "+value.lastName;
										value.genderName = value.genderCode;
										value.mobileNo = value.mobileNumber;
										value.visitNumber = value.admissionNumber;
										value.identificationNo = value.identificationNumber;
										$scope.patientList.push(value); // op patient
									}else if($scope.admissionType == 2 && value.admissionId!=null){
										value.uhIdNumber = value.uhidNumber;
										value.patientName = value.prefixCode + " "+ value.firstName + " "+value.middleName+" "+value.lastName;
										value.genderName = value.genderCode;
										value.mobileNo = value.mobileNumber;
										value.visitNumber = value.admissionNumber;
										value.identificationNo = value.identificationNumber;
										$scope.patientList.push(value); // ip 
									}else if($scope.admissionType == 3 && value.admissionId!=null){
										value.uhIdNumber = value.uhidNumber;
										value.patientName = value.prefixCode + " "+ value.firstName + " "+value.middleName+" "+value.lastName;
										value.genderName = value.genderCode;
										value.mobileNo = value.mobileNumber;
										value.visitNumber = value.admissionNumber;
										value.identificationNo = value.identificationNumber;
										$scope.patientList.push(value); // DC 
									}else if($scope.admissionType == 4 && value.admissionId!=null){
										value.uhIdNumber = value.uhidNumber;
										value.patientName = value.prefixCode + " "+ value.firstName + " "+value.middleName+" "+value.lastName;
										value.genderName = value.genderCode;
										value.mobileNo = value.mobileNumber;
										value.visitNumber = value.admissionNumber;
										value.identificationNo = value.identificationNumber;
										$scope.patientList.push(value); // ER
									}
									$scope.patientSearchObj.birthDate="";
									
									patSearchObj = {};
								});
								console.log("PatientList",$scope.patientList);
								if($scope.patientList.length == 0){
									growl.warning("No Result Found!",{
										title : 'Warning!'
									});
								}
							});
		}
		
		$scope.getconsentListId = function(admissionId,encounterId){
			$scope.consentList = [];
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + "/adt/getConsentListByAdmissionAndEncounterId";
			var obj = {
					admissionId :admissionId,
					encounterId : encounterId
					};
			BillingGenericService.serviceAction(METHOD_POST,URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					$rootScope.stopLoader();
					$scope.consentDetailList =[];
					if(response.data.listObject != null &&  response.data.listObject.length > 0){
						
						angular.forEach(response.data.listObject,function(value,index){
							value.visiteTypeId=$scope.admissionType;
							$scope.consentDetailList.push(value);
						});
					}
				}
			});
		}
		
		
		$scope.admissionId  = null;
		var getPatientDetailsByIndex = function(index){
			$("#patientSelectRadio_"+index).prop("checked",true);
			$rootScope.startLoader();
			$scope.depositList = [];
            $scope.getDisRiqFlag = true;

			$scope.initPatientSelected();
			$scope.isIntermediateBill='N';
		    $scope.isFinancialClearence='N';
		    $scope.finalBill='N';
		    $scope.interimBill = 'N';
		    $rootScope.thirdTabLevel = 1;
		    
			$scope.patientDetails.age=($scope.patientList[index].ageString);
			$scope.patientDetails.patientName=($scope.patientList[index].patientName);
			$scope.patientDetails.genderCode=($scope.patientList[index].genderCode);
			$scope.patientDetails.uhIdNumber=($scope.patientList[index].uhIdNumber);
			$scope.patientDetails.patientId=($scope.patientList[index].patientId);
			$scope.patientDetails.birthDate=($scope.patientList[index].birthDate);
			$scope.patientDetails.genderId = ($scope.patientList[index].genderId);
			$scope.patientDetails.visitTypeId = $scope.admissionType;
			$scope.patientDetails.genderName = ($scope.patientList[index].genderName);
			$scope.patientDetails.mobileNumber = ($scope.patientList[index].mobileNumber);
			$scope.patientFullName=($scope.patientList[index].patientName)+ " "+$scope.patientList[index].ageString	+ "/"+ $scope.patientList[index].genderCode;
			$scope.uhidNum=($scope.patientList[index].uhIdNumber);
			$scope.genderCode=($scope.patientList[index].genderCode);
			$scope.visitTypeId = $scope.patientList[index].visitTypeId;
			console.log($scope.genderCode)
			
			//get admission details by id
			if($scope.patientList[index].admissionId != null && typeof($scope.patientList[index].admissionId) != "undefined")
			{
				if($scope.admissionType!=1){
					$scope.admissionId = ($scope.patientList[index].admissionId);
					$scope.encounterObj.encounterNumber = ($scope.patientList[index].admissionNumber);
					$scope.getconsentListId($scope.admissionId, null);
				}
				$scope.loadingServiceFlag = true;
				$rootScope.startLoader();
				
				
				$scope.patientDetails.dateOfAdmission = $scope.patientList[index].doa;
				$scope.patientDetails.patientDischargeDate = $scope.patientList[index].pdd;
				$scope.patientDetails.bedNumber = $scope.patientList[index].bedNumber;
				$scope.patientDetails.doctorFullName = $scope.patientList[index].doctorName;
				$scope.patientDetails.wardName = $scope.patientList[index].wardName;
				
				var dateDoa = moment($scope.patientList[index].doa, 'DD/MM/YYYY');
				var datePdd = moment($scope.patientList[index].pdd, 'DD/MM/YYYY');

				$scope.patientDetails.los = $rootScope.dateDiffrence(dateDoa,datePdd);
			}else if($scope.patientList[index].encounterCount>0)
			{
				$scope.visitTypeId = 1;
				$scope.patientVisitTypeId = 1;
				$scope.encounterCount = $scope.patientList[index].encounterCount;
				var data = {};
				var URI = BASE_URL + ROOT_URL + "/api/opd/encounters/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/patientId/"+$scope.patientList[index].patientId;
				BillingGenericService.serviceAction(METHOD_GET,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						if(response.data.listObject.length>0){
							$scope.encounterList = response.data.listObject;
							if($scope.encounterList.length==1)
							{
								if($scope.admissionType == 1){
								$scope.encounterObj.encounterId =  $scope.encounterList[0].encounterId ;
								$scope.visitTypeId =1;
								$scope.encounterObj.encounterNumber = $scope.encounterList[0].encounterNumber;
								$scope.getconsentListId(null,$scope.encounterObj.encounterId);
								}
							}
						}
					}
				});
			}
			else
				{
				$rootScope.stopLoader();
				growl.warning("No Record Found..!!",{
					title : ERROR_MSG
				});
				return false
				
				}
			
			$rootScope.stopLoader();
		};
		
		
				
			// function for popup ok button call
				$scope.PopupOkBtn = function() {
					$scope.popUpFlag = false;
					$scope.saveBillings();
					$('.patienttSearchResult').toggle();
				}
				
				
				$scope.init();
				
				/***=========================Payer tab=========================***/
				
				
				$rootScope.setThirdTabLevel = function(newTab){
					$rootScope.thirdTabLevel = newTab;
					
				};
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

			        				//console.log('$scope.patientList : ', JSON.stringify($scope.patientList));
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
			
			$scope.consentTypeObj={
					consentTypeMasterId:"",
					consentTypeMasterName:""
				};
				$scope.consentObj = {
						consentMasterId:"",
						consentMasterName:"",
						filePath:"",
						templateType:"",
						template:"",
						documentName:""
				};
				
				$scope.consentTypeList = [];
				$scope.getConsentTypes = function(){
					var URI = BASE_URL + ROOT_URL + "/adt/getActiveConsentTypeList/"+$scope.organizationId+"/"+$scope.unitId;
					BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success")
							$scope.consentTypeList = response.data.listObject;
					});
				}
				
				$scope.getConsentTypes();
				
				$scope.consentList = [];
				$scope.getconsentListByTypeId = function(obj){
					$scope.consentList = [];
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/adt/getConsentListByConsentTypeId";
					var obj = {
							consentTypeMasterId:obj.consentTypeMasterId,
							unitId : $scope.unitId
							};
					BillingGenericService.serviceAction(METHOD_POST,URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success"){
							$rootScope.stopLoader();
							$scope.consentList = response.data.listObject;
						console.log("$scope.consentList",$scope.consentList);
						}
					});
				}
				
				
				$scope.getTemplateHtmlData = function(){
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/adt/replaceConsentAnotations";
					var obj = {
							};
					
					obj.consentMasterId = $scope.consentObj.consentMasterId;
					obj.htmlTemplateData = $scope.consentObj.template;
					obj.consentTypeId =$scope.consentTypeObj.consentTypeMasterId;
					obj.admissionId = $scope.admissionId;
					obj.encounterId = $scope.encounterObj.encounterId;
					obj.patientId = $scope.patientDetails.patientId;
					
					BillingGenericService.serviceAction(METHOD_POST,URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success"){
							$rootScope.stopLoader();
							$scope.templateObj = "";
							$scope.templateObj = response.data.object;
							angular.element('#veiwChecklistTemplate').modal('show');						
							}
						$rootScope.stopLoader();
					});
				}
				
				
				$scope.templateObj = "";
				$scope.documentName = "";
				$scope.filePath = "";
				$scope.addConsent = function(){
					if($scope.consentObj.templateType == 1){
						
					/*	$scope.templateObj = "";
						$scope.templateObj = $scope.consentObj.template;
						angular.element('#veiwChecklistTemplate').modal('show');*/
						
						$scope.getTemplateHtmlData();
						
					}else if($scope.consentObj.templateType == 2) {
						
						$scope.documentName = "";
						$scope.filePath = "";
						$scope.documentName=$scope.consentObj.documentName,
						$scope.filePath=$scope.consentObj.documentPath,
						angular.element('#procedureTemplateUploadedModal').modal('show');
					}else if($scope.consentObj.templateType == 3){
						$scope.saveConsentDetails();
					}
					
				}

				$scope.openImage = function()
                { 
					$rootScope.startLoader();
					var imageUrl = $scope.filePath;
					$window.open(imageUrl, '_blank');
				/*	var href = $rootScope.currentHrefDoccument();
					$.fancybox.open({
						width: 600, // or whatever
						height: 320,
						href:href+imageUrl,
						type: 'iframe'
					});*/
						$rootScope.stopLoader();
          }
				$scope.openImageForView = function(){
					$rootScope.startLoader();
					var imageUrl = 	$scope.consentObj.documentPath;
					var href = $rootScope.currentHrefDoccument();
					$.fancybox.open({
						width: 600, // or whatever
						height: 320,
						href:href+imageUrl,
						type: 'iframe'
					});
						$rootScope.stopLoader();
				}
				
				$scope.consentDetailList = [];
				$scope.isConsentExist=false;
				$scope.consentAdded=false;
				$scope.editIndex = null;
				$scope.addConsentDetails = function(consentTransactionId){
					try{
						$rootScope.startLoader();
								if(!$scope.consentAdded){
									if($scope.editIndex !=null){
										$scope.consentDetailList[$scope.editIndex].consentTransactionId = consentTransactionId;
										$scope.consentDetailList[$scope.editIndex].visitNumber = $scope.encounterObj.encounterNumber;
										$scope.consentDetailList[$scope.editIndex].consentTypeMasterName = $scope.consentTypeObj.consentTypeMasterName;
										$scope.consentDetailList[$scope.editIndex].consentTypeId = $scope.consentTypeObj.consentTypeMasterId;
										$scope.consentDetailList[$scope.editIndex].consentMasterId = $scope.consentObj.consentMasterId;
										$scope.consentDetailList[$scope.editIndex].consentMasterName = $scope.consentObj.consentMasterName;
										$scope.consentDetailList[$scope.editIndex].organizationId=$scope.organizationId;
										$scope.consentDetailList[$scope.editIndex].unitId=$scope.unitId;
										$scope.consentDetailList[$scope.editIndex].status="A";
										$scope.consentDetailList[$scope.editIndex].createdBy=$scope.userId;
										$scope.consentDetailList[$scope.editIndex].templateType=$scope.consentObj.templateType;
										$scope.consentDetailList[$scope.editIndex].documentName=$scope.consentObj.documentName;
										$scope.consentDetailList[$scope.editIndex].templateDesc=$scope.templateObj;//$scope.consentObj.template,
										$scope.consentDetailList[$scope.editIndex].documentPath=$scope.consentObj.filePath;
										$scope.consentDetailList[$scope.editIndex].createdDate=$scope.dt;
										$scope.consentDetailList[$scope.editIndex].updatedBy=$scope.userId;
										$scope.consentDetailList[$scope.editIndex].updatedDate=$scope.dt;
										$scope.consentDetailList[$scope.editIndex].visiteTypeId=$scope.admissionType;
										$scope.consentDetailList[$scope.editIndex].admissionId=$scope.admissionId;
										$scope.consentDetailList[$scope.editIndex].encounterId=$scope.encounterObj.encounterId;
										$scope.consentDetailList[$scope.editIndex].formTemplateId = $scope.consentObj.formTemplateId;
									}else{
								var newArr = {};
								newArr.consentTransactionId = consentTransactionId;
								newArr.visitNumber = $scope.encounterObj.encounterNumber;
								newArr.consentTypeMasterName = $scope.consentTypeObj.consentTypeMasterName;
								newArr.consentTypeId = $scope.consentTypeObj.consentTypeMasterId;
								newArr.consentMasterId = $scope.consentObj.consentMasterId;
								newArr.consentMasterName = $scope.consentObj.consentMasterName;
								newArr.organizationId=$scope.organizationId,
								newArr.unitId=$scope.unitId,
								newArr.status="A",
								newArr.createdBy=$scope.userId,
								newArr.templateType=$scope.consentObj.templateType,
								newArr.documentName=$scope.consentObj.documentName,
								newArr.templateDesc=$scope.templateObj,//$scope.consentObj.template,
								newArr.documentPath=$scope.consentObj.filePath,
								newArr.createdDate=$scope.dt,
								newArr.updatedBy=$scope.userId,
								newArr.updatedDate=$scope.dt,
								newArr.visiteTypeId=$scope.admissionType,
								newArr.admissionId = $scope.admissionId ,
								newArr.encounterId = $scope.encounterObj.encounterId ,
								newArr.formTemplateId = $scope.consentObj.formTemplateId;
								$scope.consentDetailList.push(newArr);
									}
								}
						$scope.consentTypeObj.consentTypeMasterId = "";
						$scope.consentTypeObj.consentTypeMasterName = "";
						$scope.consentObj.consentMasterId = "";
						$scope.consentObj.consentMasterName = "";
						$scope.consentList = [];
						$scope.editIndex = null;
						if($scope.consentObj.templateType == 1){
							
							$scope.templateObj = "";
							angular.element('#veiwChecklistTemplate').modal('hide');
						}else{
							
							$scope.documentName = "";
							$scope.filePath = "";
							angular.element('#procedureTemplateUploadedModal').modal('hide');
						}
						
						
						$rootScope.stopLoader();
					}catch(e){
						$rootScope.stopLoader();
						console.log("Exception",e);
					}
				}
				
				$scope.getEncounterNumber = function(obj){
					$scope.encounterObj =obj;
				}
				//save consent
				$scope.saveConsentDetails = function(){
					try{
						$rootScope.startLoader();
						$scope.isConsentExist=false;
						$scope.consentAdded=false;
						
						if($scope.consentDetailList!=null && $scope.consentDetailList.length>0){
						angular.forEach($scope.consentDetailList,function(value,index){
							if(index!=$scope.editIndex){
							if($scope.consentObj.consentMasterId == value.consentMasterId && value.status == 'A'){
								$scope.isConsentExist=true;
							}else if($scope.consentObj.consentMasterId == value.consentMasterId && value.status == 'I'){
								value.status='A';
								
								$scope.consentAdded=true;
								}
							}
						});
						}
						
						if($scope.consentTypeObj.consentTypeMasterId==""){
							$rootScope.stopLoader();
							growl.warning("Please select Consent Type",
									{
										title : 'warning!'
									});
							return false;
						}else if($scope.consentObj.consentMasterId==""){
							$rootScope.stopLoader();
							growl.warning("Please select Consent",
									{
										title : 'warning!'
									});
							return false;
						}else{
							
							if($scope.isConsentExist){
								growl.warning("Consent Already Exist",
										{
											title : 'warning!'
										});
								$rootScope.stopLoader();
								return false;
							}else{
								$scope.dt = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
								var list = [];
								var URI = BASE_URL + ROOT_URL + "/adt/patientConsent";
								var obj = {
										consentTransactionId : $scope.consentTransactionId,
										consentMasterId : $scope.consentObj.consentMasterId ,
										admissionId : $scope.admissionId ,
										encounterId : $scope.encounterObj.encounterId ,
										templateDesc : $scope.templateObj , 
										status : 'A' ,
										createdBy : $scope.userId ,
										createdDate : $scope.dt ,
										updatedBy : $scope.userId ,
										updatedDate : $scope.dt ,
										unitId : $scope.unitId ,
										organizationId : $scope.organizationId , 
										consentTypeId : $scope.consentTypeObj.consentTypeMasterId,
										isFromConsentPrint : true,
										documentPath : $scope.consentObj.filePath ,
										documentName : $scope.consentObj.documentName ,
										formTemplateId : ($scope.consentObj.formTemplateId == null || $scope.consentObj.formTemplateId == "") ? null : $scope.consentObj.formTemplateId,
										age : $scope.patientDetails.age,
										genderName : $scope.patientDetails.genderName,
										};
								 list.push(obj);
								 console.log("list",list);
								GenericService.serviceAction(METHOD_POST,URI, list).then(function(response) {
									if (response.data.status == "success"){
										$rootScope.stopLoader();
										if(response.data.listObject!=null && response.data.listObject.length>0){
												$scope.addConsentDetails(response.data.listObject[0]);
										}
										var list = [];
										var obj = {};
										$scope.consentTransactionId = null;
									}
								});
								
							}
						}
						
						$rootScope.stopLoader();
					}catch(e){
						$rootScope.stopLoader();
						console.log("Exception",e);
					}
				}
				
				
				$scope.documentDetailsList = [];
				$scope.documentDetails = {
					  documentDetailsId : "",
					  documentTypeList : $scope.documentTypeList,
					  consentTransactionId:"",
					  documentPath : "",
					  documentTypeId : "",
					  status : 'A',
					  createdDate : "",
					  createdBy : $scope.userId,
					  updatedDate : "",
					  updatedBy : $scope.userId,
					  unitId : $scope.unitId,
					  organisationId : $scope.organizationId,
					  file : {}
					}
				
				
				$scope.documentTypeList = [];
				  $scope.getDocumentType = function(){
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL + GETACTIVEDOCUMENTTYPELIST;
						BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
								$scope.documentTypeList = [];
								if (response.data.status == "success") {
									$scope.documentTypeList = response.data.listObject;
								}
							});
					}
					$scope.getDocumentType();
					$scope.savedDocumentList = []
				$scope.getSavedConsentDocumentList = function(obj){
					  $rootScope.startLoader();
				      var data =
				        {
				        "consentTransactionId":obj.consentTransactionId,
				        "unitId":$scope.unitId,
				        "organizationId":$scope.organizationId,
				       }
				      var URI = BASE_URL + ROOT_URL + "/adt/getConsentDocsListByMapperId";
				      BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				        $scope.savedDocumentList = [];
				        if (response.data.status == "success") 
				        {
				        	$scope.savedDocumentList = response.data.listObject;  
				           
				            $rootScope.stopLoader();
				        }
				        else if(response.data.status == "error")
				         {
				        	$rootScope.stopLoader();
				         }
				       });
				}	
				
				$scope.currentIndexDoc = "";
				$scope.currentIndexVideo = "";
				
				$scope.getCurrentIndexForDoc = function(index,obj){
					$scope.currentIndexDoc = index;
					 $('#fileId').val(undefined);
						$scope.documentDetailsList=[];
						$scope.documentDetails.consentTransactionId = obj.consentTransactionId;	
						if($scope.documentDetailsList.length>0)
						{
						  $('#docUploadPopup').modal('show');
						}
					else{
						 $('#docUploadPopup').modal('show');
						var docDetails = angular.copy($scope.documentDetails);
						docDetails.consentTransactionId = obj.consentTransactionId;
						$scope.documentDetailsList.push(docDetails);
					}
						$scope.getSavedConsentDocumentList(obj);
				}
				
				$scope.addDocDetails = function()
				{ 
					var docDetails = angular.copy($scope.documentDetails);
					$scope.documentDetailsList.push(docDetails);
				};

			  $scope.removeDocDetails = function(deleteIndex)
				{ 
				   $scope.documentDetailsList.splice(deleteIndex, 1);
			    }	
				

				 $scope.uploadConsentDocs = function()
				    {
					  $scope.finalDocUploadList=[];
					  var count=0;
					 angular.forEach($scope.documentDetailsList, function(value, key) {  
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
								 console.log("final list",$scope.finalDoc);
					});
					 
					 if($scope.documentDetailsList.length==$scope.finalDocUploadList.length){
						   $('#docUploadPopup').modal('hide');
					
					 $rootScope.startLoader();
					  $scope.count = 0;
					 angular.forEach($scope.documentDetailsList, function(value, key) 
						{
							var promise = $scope.uploadSingleDoc(value);
							promise.then(function(response) 
									{
									  if(response.data.status == "success")
										  {
										    $scope.count++;
										    if($scope.documentDetailsList.length==$scope.count)
											 {
											   growl.success("Document Uploaded Successfully.",
														{
															title : 'Success!'
														});
											   $scope.finalDocUploadList=[];
											   $scope.documentDetailsList = [];
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
			  
				 $scope.uploadSingleDoc = function(Obj)
					{
						 var fd = new FormData();
						 
						  var fd = new FormData();
					        fd.append('consentTransactionId',Obj.consentTransactionId );
					        fd.append('documentName', Obj.file.name );
					        fd.append('documentTypeId', Obj.documentTypeId);
					        fd.append('createdBy',Obj.createdBy );
					        fd.append('updatedBy', Obj.updatedBy);
					        fd.append('unitId', Obj.unitId);
					        fd.append('organizationId', Obj.organisationId);
					        fd.append('file',Obj.file );
					    	var URI1 = BASE_URL + ROOT_URL + "/adt/uploadpatientConsentDoc";
					        return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
							{	
								return response;
							});
					}
				 $scope.uploadedVideo = [];
				    
				    $scope.getVideoListById = function(obj){
				    	 var data =
					        {
					        "consentTransactionId":obj.consentTransactionId,
					        "unitId":$scope.unitId,
					        "organizationId":$scope.organizationId,
					       }
					      var URI = BASE_URL + ROOT_URL + "/adt/getConsentVideoListByMapperId";
					      BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					        $scope.uploadedVideo = [];
					        if (response.data.status == "success") 
					        {
					        	$scope.uploadedVideo = response.data.listObject;  
					           
					            $rootScope.stopLoader();
					        }
					        else if(response.data.status == "error")
					         {
					        	$rootScope.stopLoader();
					         }
					       });

				    }
			  
			  
				$scope.getCurrentIndexForVideo = function(index,Obj){
					$scope.currentIndexVideo = index;
					$scope.getVideoListById(Obj);
				}
					
					
				$scope.docObj = {};
				/*$scope.uploadDocument = function(){
					try{
						$rootScope.startLoader();
							        var fd = new FormData();
							        fd.append('consentTransactionId',$scope.consentDetailList[$scope.currentIndexDoc].consentTransactionId );
							        fd.append('documentName', $scope.docObj.file.name );
							        fd.append('organizationId', $scope.organizationId);
							        fd.append('file',  $scope.docObj.file );
								
								var URI1 = BASE_URL + ROOT_URL + "/adt/uploadpatientConsentDoc";
								GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
								{	
									if (response.data.status == 'success')
									{
										growl.success("Uploaded Successfully ",
												{
													title : 'success!'
												});
										angular.element('#docUploadPopup').modal('hide');
										//$scope.docObj = {};
										$rootScope.stopLoader();
									} else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
									}
								});
						$rootScope.stopLoader();
					}catch(e){
						$rootScope.stopLoader();
						console.log("Exception",e);
					}
				}*/
				
				 $scope.getClearData = function(type)
				    {
				    	$scope.admissionType = type;
				    	$scope.init();
				    	$scope.patientList = [];
				    	  $scope.newVideoForUpload = {};
				    	  $scope.consentTypeObj.consentTypeMasterId = "";
							$scope.consentTypeObj.consentTypeMasterName = "";
							$scope.consentObj.consentMasterId = "";
							$scope.consentObj.consentMasterName = "";
							$scope.consentList = [];
							$scope.editIndex = null;
							$scope.consentDetailList = [];
							$scope.documentName = "";
							$scope.filePath = "";
							$scope.templateObj = "";
							$scope.consentTransactionId = null;
				    	
				    }
			//video recording code start
				 var ROI_X = 150;
				 var ROI_Y = 100;
				 var ROI_WIDTH = 350;
				 var ROI_HEIGHT = 250;
				    
				 var FPS = 25;
				 
				 var cameraStream = null;
				 var processingStream = null;
				 var mediaRecorder = null;
				 var mediaChunks = null;
				 var processingPreviewIntervalId = null;
				 
				   $scope.newVideoForUpload = {};
				   $scope.processFrame = function() {
				        var cameraPreview = document.getElementById("cameraPreview");
				    }
				   
				   $scope.blobToFile = function(theBlob, fileName){
				        //A Blob() is almost a File() - it's just missing the two properties below which we will add
				        theBlob.lastModifiedDate = new Date();
				        theBlob.name = fileName;
				        return theBlob;
				    }
				   
				    var generateRecordingPreview = function() {
				        var mediaBlob = new Blob(mediaChunks, { type: "video/webm" });
				        var mediaBlobUrl = URL.createObjectURL(mediaBlob);
				        
				        $scope.newVideoForUpload = {};
				        $scope.newVideoForUpload.video = $scope.blobToFile(mediaBlob,moment().toDate().getTime()+".webm");
				    }
				    
				 
				 $scope.startCapture = function() {
				        var constraints = {audio: {
				            echoCancellation: {exact: false}
				        },
				        video: {
				          width: 1280, height: 720
				        }};
				        navigator.mediaDevices.getUserMedia(constraints).then((stream) => {
				            cameraStream = stream;
				            
				            var cameraPreview = document.getElementById("cameraPreview");
				            cameraPreview.srcObject = stream;
				            var options = {
				            		audioBitsPerSecond: 2500000,
				            		videoBitsPerSecond: 2500000,
				            		mimeType: 'video/webm;codecs=vp9'
				            		}
				            
				            mediaRecorder = new MediaRecorder(stream,options);
				            mediaChunks = []
				            
				            mediaRecorder.ondataavailable = function(event) {
				                mediaChunks.push(event.data);
				                if(mediaRecorder.state == "inactive") {
				                    generateRecordingPreview();
				                }
				            };
				            mediaRecorder.start();
				            processingPreviewIntervalId = setInterval($scope.processFrame(), 1000 / FPS);
				        })
				        .catch((err) => {
				            alert("No media device found!");
				            console.log(err);
				        });
				    };
				    
				    $scope.stopCapture = function() {
				        if(cameraStream != null) {
				            cameraStream.getTracks().forEach(function(track) {
				                track.stop();
				            });
				        }
				        
				        if(processingStream != null) {
				            processingStream.getTracks().forEach(function(track) {
				                track.stop();
				            });
				        }
				        
				        if(mediaRecorder != null) {
				            if(mediaRecorder.state == "recording") {
				                mediaRecorder.stop();
				            }
				        }
				        
				        if(processingPreviewIntervalId != null) {
				            clearInterval(processingPreviewIntervalId);
				            processingPreviewIntervalId = null;
				        }
				    };
				    
				   
				    
				    $scope.uploadVideo = function()
				    {
				    	var fd = new FormData();
				    	
				    	   fd.append('consentTransactionId',$scope.consentDetailList[$scope.currentIndexVideo].consentTransactionId );
					        fd.append('videoName', $scope.newVideoForUpload.video.name );
					        fd.append('createdBy',$scope.userId );
					        fd.append('updatedBy', $scope.userId);
					        fd.append('unitId', $scope.unitId);
					        fd.append('organizationId', $scope.organizationId);
					      fd.append('file',$scope.newVideoForUpload.video);
				    	var URI = BASE_URL + ROOT_URL + "/adt/uploadpatientConsentVideo";
				    	GenericServiceFileUpload.serviceAction("POST",URI,fd,{ 'Content-Type': undefined}).then(function(response)
				    			{	
							if (response.data.status == 'success')
							{
								$rootScope.stopLoader();
								growl.success("Uploaded Successfully ",
										{
											title : 'success!'
										});
								//$scope.newVideoForUpload = {};
								
								angular.element('#videoUploadPopup').modal('hide');
							} else if(response.data.status == 'error')
							{
								$rootScope.stopLoader();
							}
						});
				    }
				    
				   
				    $scope.editConsentDetails = function(obj,idx){
				    	$scope.editIndex =  idx;
				    	$scope.consentTypeObj.consentTypeMasterId = obj.consentTypeId;
				    	$scope.consentTypeObj.consentTypeMasterName = obj.consentTypeMasterName;
				    	$scope.getconsentListByTypeId($scope.consentTypeObj);
				    	
				    	$timeout(function () {
				    		$scope.consentObj.consentMasterId = obj.consentMasterId;
					    	$scope.consentObj.consentMasterName = obj.consentMasterName;
					    	$scope.consentObj.templateType = obj.templateType;
					    	$scope.consentObj.documentPath = obj.documentPath;
					    	$scope.consentObj.template = obj.templateDesc;
					    	$scope.consentObj.documentName = obj.documentName;
					    	$scope.consentObj.formTemplateId = obj.formTemplateId;
					    	$scope.consentObj.documentName = obj.documentName;
					    	$scope.consentTransactionId = obj.consentTransactionId
				    		},2000);
				    	
				    	
				    	
				    }
				    
				    $scope.cancelRequest = function(obj){
				    	
				     	var URI = BASE_URL + ROOT_URL + "/adt/cancelConsentPrint";
				     	var data = {
				     			consentTransactionId : obj.consentTransactionId
				     	};
				     	GenericService.serviceAction("POST",URI,data).then(function(response) {
							if (response.data.status == "success"){
								if($scope.admissionId != null){
									
									$scope.getconsentListId($scope.admissionId, null);
									
								}else{
									
									$scope.getconsentListId(null,$scope.encounterObj.encounterId);
								}
								
							}
						});
						
				    }
				    
				    $scope.openImage1 = function(filePath)
	                { 
						$rootScope.startLoader();
						var imageUrl = filePath;
						var href = $rootScope.currentHrefDoccument();
						$.fancybox.open({
							width: 600, // or whatever
							height: 320,
							href:href+imageUrl,
							type: 'iframe'
						});
						$rootScope.stopLoader();
	                }
				    
				    $scope.getClassForColor = function(info){
				    	
				    	if(info.status != undefined && info.status !=null && info.status == 'I' ){
				    		
				    		return "td-bg-gray";
				    	}
				    }
				    
				   
				    $scope.deactiveVideoById = function(obj){
				    	 var data =
					        {
					        "videoDetailsId":obj.videoDetailsId,
					        
					       }
					      var URI = BASE_URL + ROOT_URL + "/adt/deactiveConsentVideo";
					      BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					        if (response.data.status == "success") 
					        {
					        	growl.success("Video Remove Successfully ",
										{
											title : 'success!'
										});
					        	$scope.getVideoListById($scope.consentDetailList[$scope.currentIndexVideo]);
					        }
					        else if(response.data.status == "error")
					         {
					        	growl.error("Video Remove Failed ",
										{
											title : 'error!'
										});
					         }
					       });

				    }
				    
				    
				    $scope.deactiveDocById = function(obj){
				    	 var data =
					        {
					        "documentDetailsId":obj.documentDetailsId,
					       }
					      var URI = BASE_URL + ROOT_URL + "/adt/deactiveConsentDocs";
					      BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					        if (response.data.status == "success") 
					        {
					        	growl.success("Document Remove Successfully ",
										{
											title : 'success!'
										});
					        	$scope.getSavedConsentDocumentList($scope.consentDetailList[$scope.currentIndexDoc]);
					        }
					        else if(response.data.status == "error")
					         {
					        	growl.error("Documnet Remove Failed ",
										{
											title : 'error!'
										});
					         }
					       });

				    }
				    
				    
				    $scope.templateConfigObject = {};
					 $scope.getTemplateConfig = function(){
						$rootScope.startLoader();
						var obj = {};
						obj.unitId = $scope.unitId;
						var URI = BASE_URL + ROOT_URL + "/api/global/templateConfig";
						BillingGenericService.serviceAction(METHOD_POST,URI,obj).then(function(response) {
							if (response.data.status == "success"){
								 $scope.templateConfigObject = response.data.object;
								 $rootScope.stopLoader();
							}
							else if(response.data.status == "error")
							{
								 $rootScope.stopLoader();	
							}
						});
					}
					
					 $scope.getTemplateConfig();
				    
					 $scope.templateHTML ={};
				    $scope.consentPrintReport = function(info,index){
				    	if(info.templateType == 1){/*
				    		if(info.consentTransactionId!=null){ 
					    		var elementId = document.getElementById('consentTransactionId');
							    if(elementId!=null)
							    	{
							    	 document.body.removeChild(elementId);
							    	}
							   	
							var URI = BASE_URL + ROOT_URL + "/adt/patientConsentPrintReport";
						     var form = null;
						     form = document.createElement("form");
						     form.setAttribute("id", "consentTransactionId");
						     form.setAttribute("name", "Consent Print");
						     form.setAttribute("method", "post");
						     form.setAttribute("action", URI);
						     form.setAttribute("target", "_blank");
						     
						     
						     var hiddenField1 = document.createElement("input"); 
						     hiddenField1.setAttribute("type", "hidden");
						     hiddenField1.setAttribute("name", "consentTransactionId");
						     hiddenField1.setAttribute("value",info.consentTransactionId);
						     
						     var hiddenField2 = document.createElement("input"); 
						     hiddenField2.setAttribute("type", "hidden");
						     hiddenField2.setAttribute("name", "admissionId");
						     if(info.visiteTypeId!=1){
						    	 hiddenField2.setAttribute("value",info.admissionId !=null ? info.admissionId: 0);
						     }else{
						    	 hiddenField2.setAttribute("value", 0);
						     }
						     var hiddenField3 = document.createElement("input"); 
						     hiddenField3.setAttribute("type", "hidden");
						     hiddenField3.setAttribute("name", "encounterId");
						     if(info.visiteTypeId==1){
						    	 hiddenField3.setAttribute("value",info.encounterId!=null ? info.encounterId:0);
						     }else{
						    	 hiddenField3.setAttribute("value",0);
						     }
						     
						     form.appendChild(hiddenField1);
						     form.appendChild(hiddenField2);
						     form.appendChild(hiddenField3);
						     
						     document.body.appendChild(form);
						     $("form[name='Consent Print']").submit();
						     }
							else{
								growl.error("Consent Report not generated",
										{
											title : 'Error!'
										});
								}
				    	*/
				    		
				    		var data =  {
			    		            "appName": "HIS",
			    		            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
			    		            "reportDesignFilePath": "ConsentTemplate.rpt",
			    		            "reportFormat": "PDF",
			    		            "exportFileName": "ConsentTemplate",
			    		            "reportParams": [
			    		              {
			    			             "paramName": "consent_transaction_id",
			    			             "paramType": "Integer",
			    			             "paramValue": info.consentTransactionId
			    		              }
			    		            ]
			    		    };
			    	           reportService.generate(REPORT_API_URI1,data);
							
				    	
				    	}else if(info.templateType == 2){
				    		$scope.openImage1(info.documentPath);
				    	}else if(info.templateType == 3){
				    		/*var landingUrl =   $scope.templateConfigObject.templateForm + info.formTemplateId +"&formType=consent";
					 		window.open( landingUrl,  '_blank'  );*/
				    		
				    		$rootScope.startLoader();
							var obj = {};
							obj.consentTransactionId = info.consentTransactionId;
							var URI = BASE_URL + ROOT_URL + "/adt/getConsentHtmlString";
							BillingGenericService.serviceAction(METHOD_POST,URI,obj).then(function(response) {
								if (response.data.status == "success"){
									 $scope.templateHTML = response.data.object;
									 $rootScope.stopLoader();
									  var printWindow = window.open('', '', 'height=500,width=700');
										 printWindow.document.write('<html><head>');
										 printWindow.document.write('</head><body>');
										 printWindow.document.write($scope.templateHTML.htmlTemplateData != null ?$scope.templateHTML.htmlTemplateData : "" );
										 printWindow.document.write('</body></html>');
										 printWindow.document.close(); 
									 
								}
								else if(response.data.status == "error")
								{
									 $rootScope.stopLoader();	
								}
							});
				    		
				    		
				    	}
				    }
				    
				    $scope.triggerMail =  function(info){
				    	
				    	$rootScope.startLoader();
				    	var obj = {};
				    	obj.consentMasterName=info.consentMasterName;
				    	obj.consentTransactionId=info.consentTransactionId;
				    	obj.consentTypeId=info.consentTypeId;
				    	obj.consentTypeMasterName=info.consentTypeMasterName;
				    	obj.createdBy=info.createdBy;
				    	obj.organizationId=$scope.organizationId;
				    	obj.unitId=$scope.unitId;
				    	obj.templateType=info.templateType;
				    	obj.formTemplateId=info.formTemplateId;
				    	obj.documentPath=info.documentPath;
				    	obj.documentName=info.documentName;
				    	obj.visitNumber=info.visitNumber;
				    	obj.admissionId=info.admissionId;
				    	obj.age = $scope.patientDetails.age;
				    	obj.genderName = $scope.patientDetails.genderName;
				    	obj.patientId = $scope.patientDetails.patientId;
				    	obj.visitTypeId=$scope.admissionType;
				    	obj.mobileNumber=$scope.patientDetails.mobileNumber;
						var URI = BASE_URL + ROOT_URL + "/adt/triggerConsentMail";
						BillingGenericService.serviceAction(METHOD_POST,URI,obj).then(function(response) {
							if (response.data.status == "success"){
								 $rootScope.stopLoader();
							}
							else if(response.data.status == "error")
							{
								 $rootScope.stopLoader();	
							}
						});
				    	
				    }
				    
	}
	
	function config($stateProvider) {
	    $stateProvider.state("consentPrint", {
	      url: "/consentPrint",
	      templateUrl: 'views/master/UnitMaster/billingMaster/common/consentsPrint.html',
	      controller: "consentPrintController.Controller",
	      title: 'Consent Print',
	      controllerAs: "vm",
	    });
	  }
	
	  angular
	    .module("myApp")
	    .config(config)
	    .controller("consentPrintController.Controller", consentPrintController);
})();

