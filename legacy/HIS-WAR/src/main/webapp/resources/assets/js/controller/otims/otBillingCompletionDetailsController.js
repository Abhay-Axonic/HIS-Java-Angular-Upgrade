(function () {
	'use strict';
	function otBillDetailsCompletionController($scope, $rootScope, BillingGenericService, growl, $cookies, CONSTANTS, GenericService, $filter, $state, dataService, $location, $timeout, $http, $stateParams,GenericServiceParamHeader,reportService, commonDetailService) {
		var self = this;
		
		
		var cookieObject = $cookies.getObject('cookieObject'); 
	    if(cookieObject == undefined){ 
	         $state.go('login'); 
	          return;
	    }
	    $scope.unitId = cookieObject.unitId;
	    $scope.organizationId = cookieObject.organizationId;
	    $scope.userId = cookieObject.userId;
	    $scope.storeId = cookieObject.storeId; 
	    
		$scope.showEmergencyflag = false;
		function init() {
			$rootScope.loginpage = true;
			self.model = {
				medicationItems: [],
				implantItems: [],
				consumableItems: [],
				active: 1,
			};
			
			self.model = {
			        fromDateOptions: {
			          formatYear: 'yyyy',
			          showWeeks: false,
			          minDate: new Date()
			        },
			        toDateOptions:{
			        	formatYear: 'yyyy',
			        	showWeeks: false,
                        minDate: new Date()
			        }
			}
			var cookieObject = $cookies.getObject('cookieObject');
			if (cookieObject == undefined) {
				$state.go('login');
				return;
			}
			$scope.unitId = cookieObject.unitId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.storeName = "";
			
			
			var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
			if (cookieObjectAppConfig == undefined) {
				$state.go('login');
				return;
			}
			$scope.taxInclusive = cookieObjectAppConfig.taxInclusive;
			$scope.gradeWiseSeviceApplicable=cookieObjectAppConfig.gradeWiseSeviceApplicable;
			$scope.cookieObjectAppConfigObj = cookieObjectAppConfig;
			$scope.openCalendar = function (e, date) {
				this.open[date] = true;
			};
			  $scope.labConfig = JSON.parse(localStorage.getItem('labConfig'));
			 if($scope.labConfig == null||$scope.labConfig==undefined){
					$state.go('login');
					return {};
				}
			 $scope.openDatePicker = function() {
					$scope.datepicker.opened = true;
				};

				$scope.datepicker = {
					opened : false
				};
				 var OpenFromDate = function () {
				      self.model.fromDateOpened = true;
				    }
				 var OpenToDate = function () {
				      self.model.toDateOpened = true;
				    }
				 
			//state go result from otcompletion controller to details controller
				 $scope.newStrScheduleDate = "";
				 $scope.patientId = "";
			console.log("$stateParams.resultCommonObj",$stateParams.resultCommonObj);
			$scope.common = $stateParams.resultCommonObj;
			$scope.saveFlag = true;
            $scope.patientDetails = {};
            $scope.patientDetails.isFromOtCompletion = true;
			$scope.patientDetails.patientName = $scope.common.patientName;
			if($scope.common.patientName!=undefined){
				
				if($scope.common.proceduerStatusId == 2){
					$scope.saveFlag = true;
					self.model.isCompleted = 'Y';
				}else{
					$scope.saveFlag = false;
				}
				
				$scope.showEmergencyflag = true;
				if($scope.showEmergencyflag == false){
					$scope.otDetalsObj.emergancy = true;
				}else{
					$scope.otDetalsObj.emergancy =false;
				}
				$scope.encounterObj = {
						encounterId:0,
						encounterNumber:""
				};
				$scope.admissionId = $scope.common.admissionId ;
				$scope.encounterId = $scope.common.encounterId ;
				$scope.visitTypeId = $scope.common.visitTypeId;
				$scope.patientId =  $scope.common.patientId;
				$scope.patientTypeId = $scope.common.patientTypeId;
				$scope.preferedDateTime = $scope.common.preferedDateTime;
				$scope.proReqId = $scope.common.proReqId;
				
				$scope.otDetalsObj.anesthesiaTypeId = $scope.common.anesthesiaTypeId;	
				$scope.otDetalsObj.billingBedCategoryId = $scope.common.billingBedCategoryId;	
				$scope.newStrScheduleDate = $scope.common.scheduleDate.replace(/-/g, "/");
				$scope.newStrScheduleDate = moment($scope.newStrScheduleDate).format('DD/MM/YYYY');
				
				$scope.patientDetails.patientName = $scope.common.patientName;
				$scope.patientDetails.genderCode = $scope.common.genderCode;
				$scope.patientDetails.age = $scope.common.age;
				$scope.patientDetails.uhIdNumber = $scope.common.uhidNumber;
				$scope.patientDetails.birthDate = $scope.common.birthDate;
				$scope.patientDetails.dateOfAdmission = $scope.common.doa;
				$scope.patientDetails.patientDischargeDate = $scope.common.pdd;
				$scope.patientDetails.bedNumber = $scope.common.bedNumber;
				$scope.patientDetails.doctorFullName = $scope.common.doctorName;
				$scope.patientDetails.wardName = $scope.common.wardName;
				$scope.patientDetails.visitTypeId = $scope.common.visitTypeId;
				if($scope.patientDetails.visitTypeId!=1){
					var dateDoa = moment($scope.patientDetails.dateOfAdmission, 'DD/MM/YYYY');
					var datePdd = moment($scope.patientDetails.patientDischargeDate, 'DD/MM/YYYY');
					$scope.patientDetails.los = $rootScope.dateDiffrence(dateDoa,datePdd);
				}
				
				$scope.patientVisitTypeId = $scope.common.visitTypeId;
				
			}else{
				$scope.otDetalsObj.emergancy =true;
			}
			
			
			$scope.otimsProcedureList = $stateParams.otimsProcedureList;
			//$scope.storeId=cookieObjectStore.storeId;

			// END of State GO self.model.order.billingBedCategoryId
			self.model.order = {billingBedCategoryId:"",patientId:"",paymentEntitlementId:"",visitTypeId:"",admissionId:"",patientTypeId:"",contractId:0,encounterId:""};
			self.model.periodicityServ = {fromDate: new Date(),toDate :new Date(), unitId:self.model.unitId,createdBy:self.model.userId},
			self.model.OpenFromDate = OpenFromDate;
			self.model.OpenToDate = OpenToDate;
			self.model.initClockPicker = initClockPicker;
			self.model.unitId = cookieObject.unitId;
			self.model.organizationId = cookieObject.organizationId;
			self.model.userId = cookieObject.userId;
			self.model.tariffId = cookieObjectAppConfig.defaultSelfTariffId;
			self.model.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
			self.model.getOrderDetailsById = getOrderDetailsById;
			self.model.getSpecialityList = getSpecialityList;
			self.model.getProcedureTypes = getProcedureTypes;
			self.model.getDoctorSpecialityList = getDoctorSpecialityList;
			self.model.getDoctorBySpeciality = getDoctorBySpeciality;
			self.model.getRoleListByDoctorCharges = getRoleListByDoctorCharges;
			self.model.searchProcServices = searchProcServices;
			self.model.selectProcService = selectProcService;
			self.model.addProcedureDetails = addProcedureDetails;
			self.model.searchServices = searchServices;
			self.model.selectService = selectService;
			//self.model.addDoctorCharges = addDoctorCharges;
			self.model.getDoctorNameByDocId = getDoctorNameByDocId;
			self.model.getRoleNameByRoleId = getRoleNameByRoleId;
			self.model.addService = addService;
			self.model.UomChange = UomChange;
			self.model.ConvertQtyOrdered = ConvertQtyOrdered;
			self.model.resetCurrentProcedures = resetCurrentProcedures;
			self.model.saveProcedureOrders = saveProcedureOrders;
			self.model.procedureOrdersByReqId = procedureOrdersByReqId;
			self.model.procDtls = { specialityId: "", procedureTypeId: "", service: "" };
			self.model.docCharge = { specialityId: "", doctorId: "", roleId: "", doctorName: "", roleName: "" ,serviceId:"",serviceName:"", specialityName:"",doctorObj:{},specialityObj:{}};
			self.model.serv = { specialityId: "", service: "" };
			self.model.timeSheet = { status: 'A', orgId: self.model.organizationId, unitId: self.model.unitId, createdBy: self.model.userId, createdDate: $rootScope.getFormattedDate(new Date().getTime(), true) };
			self.model.procedureDetails = [];
			self.model.doctorCharges = [];
			self.model.serviceDetails = [];
			self.model.currentProcedures = [];
			self.model.doctorList = [];
			self.model.otAddTimeRate = 0;
			self.model.doctorList = [];
			self.model.consumableItems = [];
			self.model.implantItems = [];
			self.model.medicationItems = [];
			self.model.periodicityServOrdersList = [];
			if($scope.common.proReqId != undefined){
				self.model.getOrderDetailsById($scope.common.proReqId, self.model.tariffId);// HIS procedure request id 

			}
			self.model.AllocateQuantity = AllocateQuantity;
			self.model.getSpecialityList();
			self.model.getProcedureTypes();
			self.model.getDoctorSpecialityList();
			self.model.getRoleListByDoctorCharges();
			self.model.SearchItems = SearchItems;
			self.model.SearchItemsImp = SearchItemsImp;
			self.model.AddItemToBill = AddItemToBill;
			self.model.RemoveMedicationItem = RemoveMedicationItem;
			self.model.RemoveImplantItem = RemoveImplantItem;
			self.model.BatchChange = BatchChange;
 			self.model.cleartable = cleartable;
			self.model.PopupAction = PopupAction;
			self.model.ConfirmRemoveMedication = ConfirmRemoveMedication;
			self.model.ConfirmRemoveImplant = ConfirmRemoveImplant;
			self.model.Calculate = Calculate;
			$scope.procedureReqMaster = {
		      		  proReqId : null,
		      		  primaryProcedureId : "",
		      		  patientId : "",
		      		  admissionId : "",
		      		  encounterId : "",
		      		  visitTypeId : "",
		      		  preferedDateTime : "",
		      		  preferedTime:"",
		      		  requestDate : "",
		      		  requestBy :"",
		      		  procedureStatusId : 4,
		      		  otCompletionDateTime : "",
		      		  otCompletionBy : "",
		      		  otBillCompletionDateTime : "",
		      		  otBillCompletionBy : "",
		      		  orgId : "",
		      		  unitId : "",
		      		  createdBy : "",
		      		  createdDate : "",
		      		  updatedBy : "",
		      		  updatedDate : ""
		      		}
			$scope.admissionId = 0;
			$scope.preferedDateTime = undefined;
			$scope.visitTypeId = "";
			$scope.encounterId = 0;
			$scope.patientTypeId= "";
			$scope.proReqId = 0 ;
			$scope.prodedures = {billOfresList:[],oTCompletionNoteList:[]};
		}
		
		$scope.openDatePickerSearch = function() {
			   $scope.datepickerSearch.opened = true;
			    };
			    $scope.datepickerSearch = {
					      opened: false
					    };
		$scope.getIdentificationTList = function(){
			var URI = BASE_URL + ROOT_URL + GETACTIVEIDENTIFICATION;
			BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success")
					$rootScope.identificationList =[];
					$rootScope.identificationList = response.data.listObject;
				console.log($rootScope.identificationList);
			});
		}
		$scope.getIdentificationTList();
		//********************************************** Pateint Search Start ****************************************/
		$scope.isTransferToWard=false;
		$scope.otToWardTransfer=false;
		$scope.otDetalsObj = {
				otCompletionDetailsId : null,
				billingBedCategoryId :"",
				otTableId :"",
				anesthesiaTypeId : "",
				otDate : "",
				totalHrs : "",
				otSheetNo : "",
				organizationId : $scope.organizationId,
				unitId : $scope.unitId,
				createdBy : $scope.userId,
				updatedBy : $scope.userId,
				status : 'A',
				createdDate : $scope.dt,
				updatedDate : $scope.dt,
				remark : "",
				emergancy : "",
				duration :0,
				encounterId :0,
				encounterNumber:"",
				otEmergancyFlow:false
			}
		
		
		$scope.otDatepickerDateOptions = {
				formatYear : 'yyyy',
				showWeeks : false,
				minDate : new Date(),
				
			};

			$scope.otDatepicker = {
				opened : false
			};

			$scope.openOtDatepicker= function() {
				$scope.otDatepicker.opened = true;
			};
		
			
			$scope.otDatepickerWheelInDateOptions = {
				formatYear : 'yyyy',
				showWeeks : false,
				minDate : new Date(),
			};

			$scope.otDatepickerWheelInDatePicker = {
				opened : false
			};

			$scope.otDatepickerWheelInDatePicker1= function() {
				$scope.otDatepickerWheelInDatePicker.opened = true;
			};
			
			//2
			$scope.tableOnDatePickerOptions = {
					formatYear : 'yyyy',
					showWeeks : false,
					minDate : new Date(),
				};

				$scope.tableOnDatePicker = {
					opened : false
				};

				$scope.tableOnDatePicker= function() {
					$scope.tableOnDatePicker.opened = true;
				};
			//3
				$scope.anesthesiaDatePickerOptions = {
					formatYear : 'yyyy',
					showWeeks : false,
					minDate : new Date(),
				};

				$scope.anesthesiaDatePicker = {
					opened : false
				};

				$scope.anesthesiaDatePicker= function() {
					$scope.anesthesiaDatePicker.opened = true;
				};
			//4
				$scope.procedureStartDatePickerOptions = {
					formatYear : 'yyyy',
					showWeeks : false,
					minDate : new Date(),
				};

				$scope.procedureStartDatePicker = {
					opened : false
				};

				$scope.procedureStartDatePicker= function() {
					$scope.procedureStartDatePicker.opened = true;
				};	
				
			//5
				$scope.procedureEndDatePickerOptions = {
					formatYear : 'yyyy',
					showWeeks : false,
					minDate : new Date(),
				};

				$scope.procedureEndDatePicker = {
					opened : false
				};

				$scope.procedureEndDatePicker= function() {
					$scope.procedureEndDatePicker.opened = true;
				};	
			//6
			$scope.tableOutDatePickerOptions = {
				formatYear : 'yyyy',
				showWeeks : false,
				minDate : new Date(),
			};

			$scope.tableOutDatePicker = {
				opened : false
			};

			$scope.tableOutDatePicker= function() {
				$scope.tableOutDatePicker.opened = true;
			};
			
			//7
			
			$scope.wheelOutDatePickerOptions = {
				formatYear : 'yyyy',
				showWeeks : false,
				minDate : new Date(),
			};

			$scope.wheelOutDatePicker = {
				opened : false
			};

			$scope.wheelOutDatePicker= function() {
				$scope.wheelOutDatePicker.opened = true;
			};
			
			//8
			/*$scope.scheduleDischargeDatePickerOptions = {
				formatYear : 'yyyy',
				showWeeks : false,
				minDate : new Date(),
			};

			$scope.scheduleDischargeDatePicker = {
				opened : false
			};

			$scope.scheduleDischargeDatePicker= function() {
				$scope.scheduleDischargeDatePicker.opened = true;
			};*/
			
			//9 
			$scope.actualDischargeDatePickerOptions = {
				formatYear : 'yyyy',
				showWeeks : false,
				minDate : new Date(),
			};

			$scope.actualDischargeDatePicker = {
				opened : false
			};

			$scope.actualDischargeDatePicker= function() {
				$scope.actualDischargeDatePicker.opened = true;
			};
			
	    var initClockPicker = function() {
			$('.clockpicker').clockpicker();
		}
		var date = new Date();
		$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
		$scope.patientId =  0;
		$scope.patientSearchObj = {
				"uhidNumber" : "",
				"patientName" : "",
				"genderId" : "",
				"mobileNumber" : "",
				"dob" : "",
				"identificationNumber" : "",
				"identificationTypeId" : "",
				"unitId" : $scope.unitId,
				"organizationId" : $scope.organizationId,
				"registrationTypeIdList" : [1,2,3,4,6]
			}
		
		$scope.patientList = [];
		$rootScope.searchPatient = function(patientsearchObj) {
			$rootScope.startLoader();
			if($scope.patientSearchObj.uhidNumber == '' && $scope.patientSearchObj.patientName == '' && $scope.patientSearchObj.genderId == '' && $scope.patientSearchObj.mobileNumber == '' && $('#datepickerSearch').val() == '' && $scope.patientSearchObj.identificationTypeId == '' && $scope.patientSearchObj.identificationNumber == '')
			{
				growl.warning(SearchFilter_REQUIRED,{title : "Warning!"
				});
				return false;
			}
				
			else{
					$scope.saveFlag = true;
				}
			patientsearchObj.dob =	$('#datepickerSearch').val()!="" && $('#datepickerSearch').val()!=undefined ? $rootScope.getChangedFormatedDateShalse($('#datepickerSearch').val()) : "";
			var URI = BASE_URL + ROOT_URL
					+ GETTEMPORYPATIENTFORPREREGISTRATION;
			BillingGenericService
					.serviceAction(METHOD_POST, URI,
							patientsearchObj,
							NOTIFICATION_MSG_STATUS_FALSE)
					.then(
							function(response) {
								$scope.patientList = [];
								//$scope.patientList = response.data.listObject;
							//	console.log("PatientList",$scope.patientList);
								angular.forEach(response.data.listObject,function(value,index){
									if(value.admissionId!=null || (value.encounterCount!=null && value.encounterCount>0)){
										$scope.patientList.push(value);
									}
								});
								console.log("PatientList",$scope.patientList);
								if($scope.patientList.length == 0){
									growl.warning("No Result Found!",{
										title : 'Warning!'
									});
								}
							});
			$scope.otDetalsObj.billingBedCategoryId='';
			$scope.otDetalsObj.otDate='';
			$scope.otDetalsObj.totalHrs='';
			$scope.otDetalsObj.otSheetNo='';
			$scope.otDetalsObj.otTableId='';
			$scope.otDetalsObj.anesthesiaTypeId='';
			$scope.otDetalsObj.remark='';
			self.model.procDtls.specialityId="";
			self.model.procDtls.procedureTypeId="";
			self.model.procDtls.service="";
			self.model.procedureDetails = [];
			$scope.prodedures.billOfresList=[];
			self.model.doctorCharges = [];
			self.model.docCharge = {};
			self.model.serviceDetails = [];
			$scope.currentEncounterOrderLists="";
			$scope.equipmentDetailsList = [];
			self.model.medicationItems = [];
			self.model.consumableItems = [];
			self.model.implantItems = [];
			self.model.medicationItems = [];
			self.model.periodicityServOrdersList = [];
			$scope.CssdKitList = [];
			$scope.prodedures.oTCompletionNoteList=[];
			self.model.timeSheet.wheelInTime1 = "";
			self.model.timeSheet.tableOnTime1= "";
			self.model.timeSheet.anesthesiaTime1 = "";
			self.model.timeSheet.procedureStartTime1 = "";
			self.model.timeSheet.procedureEndTime1 ="";
			self.model.timeSheet.tableOutTime1 =""; 
			self.model.timeSheet.wheelOutTime1 ="";
			//self.model.timeSheet.scheduleDischarge1 = ""; 
			self.model.timeSheet.actualDischarge1 ="";
			self.model.timeSheet.wheelInTime="";
			self.model.timeSheet.tableOnTime= "";
			self.model.timeSheet.anesthesiaTime = "";
			self.model.timeSheet.procedureStartTime = "";
			self.model.timeSheet.procedureEndTime ="";
			self.model.timeSheet.tableOutTime =""; 
			self.model.timeSheet.wheelOutTime ="";
			//self.model.timeSheet.scheduleDischarge = ""; 
			self.model.timeSheet.actualDischarge ="";
			$scope.billOfMaterialServicesList = [];
		}
				
				//Function For Selected Patient assign to patient Details Object
				$scope.encounterList = [];
				$scope.encounterCount = 0;
				$scope.getPatientDetailsByIndex = function(index) {
					$rootScope.startLoader();
					$scope.encounterList = [];
					$scope.admissionId = $scope.patientList[index].admissionId ;
					$scope.encounterId = 0;
					$scope.visitTypeId = $scope.patientList[index].visitTypeId ;
					$scope.patientVisitTypeId = $scope.patientList[index].visitTypeId ;
					$scope.patientTypeId = $scope.patientList[index].patientCategoryId ;
				    console.log("selected Patient",$scope.patientList[index]);
				    $scope.patientId = $scope.patientList[index].patientId;
				    
				    $scope.patientDetails.patientName = $scope.patientList[index].prefixCode+" "+$scope.patientList[index].firstName+" "+
				    $scope.patientList[index].middleName+" "+$scope.patientList[index].lastName;
					$scope.patientDetails.genderCode = $scope.patientList[index].genderCode
					$scope.patientDetails.age = $scope.patientList[index].ageString;
					$scope.patientDetails.uhIdNumber = $scope.patientList[index].uhidNumber;
					$scope.patientDetails.birthDate = $scope.patientList[index].dob;
					$scope.patientDetails.visitTypeId = $scope.patientList[index].visitTypeId;
					$scope.procedureReqMaster.patientId = $scope.patientList[index].patientId ;
					$scope.procedureReqMaster.admissionId = $scope.patientList[index].admissionId;
					//$scope.procedureReqMaster.visitTypeId = $scope.patientList[index].visitTypeId;
					self.model.order.patientId = $scope.patientList[index].patientId ;
					self.model.order.paymentEntitlementId = 1;
					self.model.order.visitTypeId = $scope.patientList[index].visitTypeId;
					self.model.order.admissionId= $scope.patientList[index].admissionId;
					self.model.order.patientTypeId = $scope.patientList[index].patientCategoryId ;
					
					$scope.patientVisitTypeId = $scope.patientList[index].visitTypeId;
				    
					if($scope.patientList[index].admissionId!= undefined && $scope.patientList[index].admissionId != null)
					{
					
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL + "/api/ipd/admissions/patient/"+$scope.patientList[index].admissionId;
						BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
							if(response.data.status == "success"){
								$scope.admissionDetails = response.data.object;
								
								$rootScope.stopLoader();
								if($scope.admissionDetails != null)
								{
									
									if($scope.admissionDetails.isFinalBill == "Y")
										{
											growl.warning("Final bill is already generated for selected patient..!!",{
												title : "Warning!"
											});
											$scope.patientList = [];
											$scope.patientId = null;
											$scope.patientDetails = {};
											 $scope.patientDetails.isFromOtCompletion = true;
											$scope.saveFlag = true;
											return false;
										}else{
											$scope.saveFlag = false;
										}
									
									$scope.uhidNum = $scope.admissionDetails.uhidNumber;
									$scope.dateOfAdmission = $scope.admissionDetails.doa;
									$scope.patientDischargeDate = $scope.admissionDetails.pdd;
									$scope.bedNumber = $scope.admissionDetails.bedNo;
									$scope.doctorFullName = $scope.admissionDetails.doctorName;
									
									$scope.patientDetails.dateOfAdmission = $scope.admissionDetails.doa;
									$scope.patientDetails.patientDischargeDate = $scope.admissionDetails.pdd;
									$scope.patientDetails.bedNumber =  $scope.admissionDetails.bedNo;
									$scope.patientDetails.wardName =$scope.admissionDetails.wardNo;
									$scope.patientDetails.doctorFullName = $scope.admissionDetails.doctorName;
									if($scope.patientDetails.visitTypeId!=1){
										var dateDoa = moment($scope.patientDetails.dateOfAdmission, 'DD/MM/YYYY');
										var datePdd = moment($scope.patientDetails.patientDischargeDate, 'DD/MM/YYYY');
										$scope.patientDetails.los = $rootScope.dateDiffrence(dateDoa,datePdd);
									}
									
									$scope.otDetalsObj.billingBedCategoryId = $scope.admissionDetails.bedBillingCategoryId;
									$scope.patientTypeId = $scope.admissionDetails.patientCategoryId;
									self.model.order.billingBedCategoryId =  $scope.admissionDetails.bedBillingCategoryId;
									self.model.order.paymentEntitlementId  =  $scope.admissionDetails.paymentEntitlementId;
									$scope.procedureReqMaster.visitTypeId = $scope.admissionDetails.visitTypeId;
									$scope.encounterCount = 0;
									if($scope.admissionDetails.payeeId != self.model.selfPayeeId)
									{
										if($scope.admissionDetails.contractId != null){
									
											self.model.order.contractId = $scope.admissionDetails.contractId;
										}
									}
								}
							}
							$rootScope.stopLoader();
						});
						
					}else if($scope.patientList[index].encounterCount>0)
					{
						$rootScope.startLoader();
						$scope.otDetalsObj.billingBedCategoryId = $scope.cookieObjectAppConfigObj.opdBillingCatId;
						$scope.patientTypeId = $scope.cookieObjectAppConfigObj.patientCategoryId;
						$scope.visitTypeId = 1;
						$scope.patientVisitTypeId = 1;
						self.model.order.visitTypeId = 1;
						self.model.order.billingBedCategoryId = $scope.cookieObjectAppConfigObj.opdBillingCatId;
						self.model.order.paymentEntitlementId  = 1;
						$scope.encounterCount = $scope.patientList[index].encounterCount;
						var data = {};
						var URI = BASE_URL + ROOT_URL + "/api/opd/encounters/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/patientId/"+$scope.patientList[index].patientId;
						BillingGenericService.serviceAction(METHOD_GET,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
							if(response.data.status == "success"){
								if(response.data.listObject.length>0){
									$scope.encounterList = response.data.listObject;
									if($scope.encounterList.length==1)
									{
										$scope.procedureReqMaster.encounterId = $scope.encounterList[0].encounterId;
										$scope.encounterId =  $scope.encounterList[0].encounterId ;
										self.model.order.encounterId  = $scope.encounterList[0].encounterId;
										self.model.order.visitTypeId = 1;
										$scope.procedureReqMaster.visitTypeId = 1;
										$scope.visitTypeId =1;
										$scope.otDetalsObj.encounterId = $scope.encounterList[0].encounterId;
										$scope.otDetalsObj.encounterNumber = $scope.encounterList[0].encounterNumber;
										$scope.encounterObj = {
												encounterId:0,
												encounterNumber:""
										};
										$scope.encounterObj.encounterId = $scope.encounterList[0].encounterId;
										$scope.encounterObj.encounterNumber = $scope.encounterList[0].encounterNumber;
										$scope.saveFlag = false;
									}
								}
							}
						});
						
					}
					
					/*if($scope.encounterId != 0)
					{
						
						var data = {};
						var URI = BASE_URL + ROOT_URL + "/api/opd/encounter/"+$scope.encounterId;
						BillingGenericService.serviceAction(METHOD_GET,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
							if(response.data.status == "success"){
								
								angular.forEach(response.data.object.patientSponsorDetailsDtosList,function(value,index){
									if(value.priorityId == '1')
									{
										if(value.payeeId != self.model.selfPayeeId)
											{
											if(value.contractId != null){
												self.model.order.contractId = value.contractId;
											}
										}
									}
								});
							}
						});
					}*/
					$scope.otDetalsObj.billingBedCategoryId='';
					$scope.otDetalsObj.otDate='';
					$scope.otDetalsObj.totalHrs='';
					$scope.otDetalsObj.otSheetNo='';
					$scope.otDetalsObj.otTableId='';
					$scope.otDetalsObj.anesthesiaTypeId='';
					$scope.otDetalsObj.remark='';
					self.model.procDtls.specialityId="";
					self.model.procDtls.procedureTypeId="";
					self.model.procDtls.service="";
					self.model.procedureDetails = [];
					$scope.prodedures.billOfresList=[];
					self.model.doctorCharges = [];
					self.model.docCharge = {};
					self.model.serviceDetails = [];
					$scope.currentEncounterOrderLists="";
					$scope.equipmentDetailsList = [];
					self.model.medicationItems = [];
					self.model.consumableItems = [];
					self.model.implantItems = [];
					self.model.medicationItems = [];
					self.model.periodicityServOrdersList = [];
					$scope.CssdKitList = [];
					$scope.prodedures.oTCompletionNoteList=[];
					self.model.timeSheet.wheelInTime1 = "";
					self.model.timeSheet.tableOnTime1= "";
					self.model.timeSheet.anesthesiaTime1 = "";
					self.model.timeSheet.procedureStartTime1 = "";
					self.model.timeSheet.procedureEndTime1 ="";
					self.model.timeSheet.tableOutTime1 =""; 
					self.model.timeSheet.wheelOutTime1 ="";
					//self.model.timeSheet.scheduleDischarge1 = ""; 
					self.model.timeSheet.actualDischarge1 ="";
					self.model.timeSheet.wheelInTime="";
					self.model.timeSheet.tableOnTime= "";
					self.model.timeSheet.anesthesiaTime = "";
					self.model.timeSheet.procedureStartTime = "";
					self.model.timeSheet.procedureEndTime ="";
					self.model.timeSheet.tableOutTime =""; 
					self.model.timeSheet.wheelOutTime ="";
					//self.model.timeSheet.scheduleDischarge = ""; 
					self.model.timeSheet.actualDischarge ="";
					$scope.billOfMaterialServicesList = [];
					$scope.getBillOfMaterialServices();
				}
				
				$scope.getEncounterDetailsByEncounterId = function(encObj){
					if(encObj.encounterId!= undefined && encObj.encounterId != null){
						$scope.otDetalsObj.encounterId = encObj.encounterId;
						$scope.otDetalsObj.encounterNumber = encObj.encounterNumber;
						$scope.procedureReqMaster.encounterId = encObj.encounterId;
						$scope.encounterId = encObj.encounterId ;
						$scope.procedureReqMaster.visitTypeId = 1;
						self.model.order.visitTypeId = 1;
						$scope.visitTypeId =1;
						self.model.order.encounterId  = encObj.encounterId;
						var data = {};
						var URI = BASE_URL + ROOT_URL + "/api/opd/encounter/"+encObj.encounterId;
						BillingGenericService.serviceAction(METHOD_GET,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
							if(response.data.status == "success"){
								
								angular.forEach(response.data.object.patientSponsorDetailsDtosList,function(value,index){
									if(value.priorityId == '1')
									{
										if(value.payeeId != self.model.selfPayeeId)
											{
											if(value.contractId != null){
												self.model.order.contractId = value.contractId;
											}
										}
									}
								});
							}
						});
					}
				}
		//********************************************** Pateint Search End ****************************************/
				
				$scope.storeId = 0;
				$scope.selectStoreFromCoockies  = function(){
					
					var cookieObjectStore = JSON.parse(sessionStorage.getItem('selectedTopStore'));
					if(cookieObjectStore==undefined){
						$rootScope.storePopup();
						if($rootScope.storeList !=null && $rootScope.storeList.length > 0){
							
							angular.element('#pharmacyPopupBeforLeftexpand').modal('show');
							
							$scope.storeName = cookieObjectStore.storeName;
							$scope.storeId = cookieObjectStore.storeId;
							
						}else{
							
							growl.warning("This User Not Mapped With Store",
			                        {
			                            title : 'Warning!'
			                        });
			                return false;
						}
						
					}else{

						$scope.storeName = cookieObjectStore.storeName;
						$scope.storeId = cookieObjectStore.storeId;
					}
					
				}
				
				$scope.selectStoreFromCoockies();
				
		//****************GET DETAILS FROM OTIMS LIST START*************/



		var getOrderDetailsById = function (proReqId, tariffId) {
			$rootScope.startLoader();
			if (proReqId != 0 && proReqId != null) 
			{
				var URI = BASE_URL + ROOT_URL + "/api/procedure/order/req/" + proReqId;
				BillingGenericService.serviceAction(METHOD_GET, URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function (response) {
					if (response.data.status == "success") 
					{
						//	self.model.order = response.data.object;
						self.model.order = response.data.listObject[0];
						$scope.newOrderDateTime = self.model.order.orderDateTime.replace(/-/g, "/");
						
						$scope.patientDetails = {
							uhIdNumber: self.model.order.uhidNumber, patientName: self.model.order.patientName,
							genderCode: self.model.order.genderCode, age: self.model.order.age, birthDate: self.model.order.dob,
							dateOfAdmission : self.model.order.doa , patientDischargeDate : self.model.order.pdd ,
						bedNumber : self.model.order.bedNumber , doctorFullName : self.model.order.doctorName!=null ? self.model.order.doctorName : $scope.common.doctorName ,
						wardName : self.model.order.wardName ,visitTypeId :  self.model.order.visitTypeId
						};
						if($scope.patientDetails.visitTypeId!=1){
							var dateDoa = moment($scope.patientDetails.dateOfAdmission, 'DD/MM/YYYY');
							var datePdd = moment($scope.patientDetails.patientDischargeDate, 'DD/MM/YYYY');
							$scope.patientDetails.los = $rootScope.dateDiffrence(dateDoa,datePdd);
						}
						 $scope.patientDetails.isFromOtCompletion = true;
						var promise = self.model.procedureOrdersByReqId(proReqId, tariffId);
						 promise.then(function(response) 
									{
									  if(response.data.status == "success")
										  {
											$rootScope.stopLoader();
										  }
									  else{
										  $rootScope.stopLoader();
										  growl.error("Something went wrong..",{
												title : ERROR_MSG
											});
											return;
									   }
								    });
					}
					else if(response.data.status == "error")
						{
						   $rootScope.stopLoader();
						}
				});
			}
		}


		var getSpecialityList = function () {
			var URI = BASE_URL + ROOT_URL + "/adt/getActiveSpecialityList";
			var obj = { organizationId: self.model.organizationId, unitId: self.model.unitId };
			BillingGenericService.serviceAction(METHOD_POST, URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function (response) {
				if (response.data.status == "success")
					self.model.specialityList = response.data.listObject;
			});
		}

		var getProcedureTypes = function () {
			var URI = BASE_URL + ROOT_URL + "/api/procedure/types/" + self.model.organizationId + "/" + self.model.unitId;
			BillingGenericService.serviceAction(METHOD_GET, URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function (response) {
				if (response.data.status == "success")
					self.model.procedureTypes = response.data.listObject;
			});
		}



		var getDoctorSpecialityList = function () {
			var URI = BASE_URL + ROOT_URL
				+ GETACTIVESPECIALITY;
			$rootScope.startLoader();
			var obj = { organizationId: self.model.organizationId, unitId: self.model.unitId };
			BillingGenericService.serviceAction(METHOD_POST, URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function (response) {
				if (response.data.status == "success")
					{
					  self.model.doctorSpecialityList = response.data.listObject;
					}
				else if (response.data.status == "error")
				{
					$rootScope.stopLoader();
				}
			});

		}



		var getDoctorBySpeciality = function (id) {

			var data = {
				specialityId: id,
				unitId: self.model.unitId,
				organizationId: self.model.organizationId
			};
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL
				+ GETDOCTORBYSPECIALITYID;

			BillingGenericService
				.serviceAction(METHOD_POST, URI, data,
					NOTIFICATION_MSG_STATUS_FALSE)
				.then(
					function (response) {
						if (response.data.status == "success")
							{
							self.model.doctorList = response.data.listObject;
							}
						else if (response.data.status == "error")
						{
							$rootScope.stopLoader();
						}
					});

		}


		var getRoleListByDoctorCharges = function () {
			var URI = BASE_URL + ROOT_URL + "/api/procedure/getRoleList/" + self.model.organizationId + "/" + self.model.unitId;
			BillingGenericService.serviceAction(METHOD_GET, URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function (response) {
				if (response.data.status == "success")
					self.model.roleListByDoctorCharges = response.data.listObject;
			});
		}

		var searchProcServices = function (serviceName) {
			

				var searchObj = {
					orgId: self.model.organizationId,
					unitId: self.model.unitId,
					serviceName: serviceName,
					specialityId: self.model.procDtls.specialityId,
					tariffId: self.model.tariffId,
					paymentEntitlementId: self.model.order.paymentEntitlementId,
					visitTypeId: self.model.order.visitTypeId,
					patientTypeId: self.model.order.patientTypeId,
					billingBedCategoryId: self.model.order.billingBedCategoryId,
					procedureTypeId: self.model.procDtls.procedureTypeId,
					isProcedure: 'Y'
				};
				var URI = BASE_URL + ROOT_URL + "/api/procedure/order/service/search";
				return BillingGenericService.serviceAction(METHOD_POST, URI, searchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function (response) {
					if (response.data.status == "success")
						return response.data.listObject;
				});
			
		}
		
		$scope.getSubsequentDetailList = function(procedureObj,index){
			var searchObj = {
					orgId: self.model.organizationId,
					unitId: self.model.unitId,
					specialityId:procedureObj.specialityId
				};
				var URI = BASE_URL + ROOT_URL + "/api/otims/getSubSequentDetailsAndInfoListBySpecialityId";
				 BillingGenericService.serviceAction(METHOD_POST, URI, searchObj).then(function (response) {
					if (response.data.status == "success"){
						self.model.procedureDetails[index].subSequentDetailsList =  response.data.listObject;
						
							self.model.procedureDetails[index].originalAmount = self.model.procedureDetails[index].amount != null ?  self.model.procedureDetails[index].amount  : self.model.procedureDetails[index].rate ;
									
									if(self.model.procedureDetails[index].subSequentDetailsList != null && self.model.procedureDetails[index].subSequentDetailsList.length>0){
										
										var subSeqObj =  self.model.procedureDetails[index].subSequentDetailsList.find(obj => {return obj.sequenceId == self.model.procedureDetails[index].procedureSequence})
										
										if(subSeqObj != undefined){
											
											if(subSeqObj.discPercentage !=null && subSeqObj.discPercentage != 0){
												
												self.model.procedureDetails[index].amount = (self.model.procedureDetails[index].originalAmount * subSeqObj.discPercentage)/100;
												self.model.procedureDetails[index].rate  = self.model.procedureDetails[index].amount;
												
												$scope.prodedures.billOfresList.forEach(function (billvalue, billIndex){
													
													//billvalue.totalAmount = billvalue.amount;
													
													if(self.model.procedureDetails[index].serviceId == billvalue.procedureId && billvalue.billOfMaterialId!=null){
														
														billvalue.amount = (billvalue.originalAmount * subSeqObj.discPercentage)/100;
													}
												});
												
											}else{
												var amt = 0.0 ;
												$scope.prodedures.billOfresList.forEach(function (billvalue, billIndex){
													
													//billvalue.totalAmount = billvalue.amount;
													
													if(self.model.procedureDetails[index].serviceId == billvalue.procedureId && billvalue.billOfMaterialId!=null){
														
														var infoObj = subSeqObj.subSequentDetailsInfoList.find(obj => {return obj.serviceId == billvalue.serviceId});
														
														if(infoObj!=undefined){
															
															if(infoObj.discPercentage!=null && infoObj.discPercentage!=0){
																amt = amt + (billvalue.originalAmount * infoObj.discPercentage)/100;
																billvalue.amount = (billvalue.originalAmount * infoObj.discPercentage)/100;
															}
														}
														
													}
													
												});
												
												self.model.procedureDetails[index].amount = amt;
												self.model.procedureDetails[index].rate  = amt; 
											}
										}
										
									}
					}
				});
		}

		$scope.getBillOdMaterialListByProcId = function(obj,index){
			var searchObj = {
					organizationId: self.model.organizationId,
					unitId: self.model.unitId,
					billingBedCatId:obj.billingBedCategoryId,
					procedureId:obj.serviceId,
					tariffId: self.model.tariffId,
					paymentEntitlementId: self.model.order.paymentEntitlementId,
					visitTypeId: self.model.order.visitTypeId,
					patientCategoryId: self.model.order.patientTypeId,
				};
				var URI = BASE_URL + ROOT_URL + "/api/procedure/BillOfMaterial/getBillOfMaterialListByProcedureId";
				 BillingGenericService.serviceAction(METHOD_POST, URI, searchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function (response) {
					if (response.data.status == "success"){
						var proRate = 0;
						angular.forEach(response.data.listObject,function(value,index){
							value.specilityList =  self.model.doctorSpecialityList;
							value.doctorList = [];
							value.isEditable = false;
							proRate += value.amount;
						});
						if(response.data.listObject.length>0){
							angular.forEach(self.model.procedureDetails,function(value,index){
								if(value.serviceId == response.data.listObject[0].procedureId){
									value.rate = proRate; 
								}
							});
						}
						
						$scope.prodedures.billOfresList =  $scope.prodedures.billOfresList.concat(response.data.listObject);
						console.log(" BOM ", response.data.listObject);
						
						//get subsequent list by speciality id
						if(index!=undefined){
							$scope.getSubsequentDetailList(obj,index);
						}
						
						 
						 var searchObja = {
								    orgId: self.model.organizationId,
									unitId: self.model.unitId,
									bedCategoryId:obj.billingBedCategoryId,
									serviceId:obj.serviceId,
									tariffId: self.model.tariffId,
									paymentEntitlementId: self.model.order.paymentEntitlementId,
									visitTypeId: self.model.order.visitTypeId,
									patientCategoryId: self.model.order.patientTypeId,
								};
						 
							var URI = BASE_URL + ROOT_URL + "/api/otims/getSurgeryResourceConfigListByProcedureId";
							 BillingGenericService.serviceAction(METHOD_POST, URI, searchObja, NOTIFICATION_MSG_STATUS_FALSE).then(function (response) {
								if (response.data.status == "success"){
									//var proRate = 0;
									$scope.getSurgeryResourceConfigListByProcedureId = [];
									$scope.getSurgeryResourceConfigListByProcedureIdBilling = [];
									$scope.getSurgeryResourceConfigListByProcedureId = response.data.listObject;
									$scope.getSurgeryResourceConfigListByProcedureIdBilling = response.data.listObject;
									$scope.getSurgeryResourceConfigList = [];
									
									$scope.rateCalculationResourceService = 0;
									// depend on logic calculate amount
									console.log("bill oflit",$scope.prodedures.billOfresList);
									angular.forEach(self.model.procedureDetails,function(value,index){
											angular.forEach($scope.getSurgeryResourceConfigListByProcedureId,function(value1,index1){
												if(value1.serviceId == value.serviceId){
													angular.forEach(value1.listSelectedResourceBillOfServicesMapperDto,function(value2,index2){
													   angular.forEach( $scope.prodedures.billOfresList,function(value3,index3){
														   
														if($scope.rateCalculationResourceService == 0){
															if(parseInt(value2.dependentOn) == value3.serviceId){
																value2.valuePercentage =  (value3.amount * (value2.dependentOnPercentage / 100));
																proRate += value2.valuePercentage;
																$scope.rateCalculationResourceService = value2.valuePercentage;
															}
														}else{
															value2.valuePercentage = ($scope.rateCalculationResourceService * (value2.dependentOnPercentage / 100));
															proRate += value2.valuePercentage;
															$scope.rateCalculationResourceService = value2.valuePercentage;
														}
													});
												   });
												  }
										     	});
											  });
									// depend on logic calculate amount
									angular.forEach(self.model.procedureDetails,function(value,index){
										angular.forEach($scope.getSurgeryResourceConfigListByProcedureId,function(value1,index1){
											if(value1.serviceId == value.serviceId){
												angular.forEach(value1.listSelectedResourceBillOfServicesMapperDto,function(value2,index2){
												   angular.forEach( $scope.prodedures.billOfresList,function(value3,index3){
													if(value2.componenetServicesId == value3.serviceId){
														var value3amount = 0;
														var value3amount = value2.valuePercentage;
														value3.amount = value3amount;
													}
												});
											   });
											  }
									     	});
										  });
									
									
									/*angular.forEach(self.model.procedureDetails,function(value,index){
										angular.forEach($scope.getSurgeryResourceConfigListByProcedureId,function(value1,index1){
											if(value1.serviceId == value.serviceId){
												angular.forEach(value1.listSelectedResourceBillOfServicesMapperDto,function(value2,index2){
												   angular.forEach( $scope.prodedures.billOfresList,function(value3,index3){
													if(parseInt(value2.dependentOn) == value3.serviceId){
														value2.splice = true;
													}
												});
											   });
											  }
									     	});
										  });*/
									
								
									//extra services 
									angular.forEach(self.model.procedureDetails,function(value,index){
										angular.forEach($scope.getSurgeryResourceConfigListByProcedureId,function(value1,index1){
											if(value1.serviceId == value.serviceId){
												angular.forEach(value1.listSelectedResourceBillOfServicesMapperDto,function(value2,index2){
													if(value2.splice != true){
													    var newArr = {};
													    newArr.specilityList =  self.model.doctorSpecialityList;
													    newArr.specilityId =  value.specialityId;
													    newArr.procedureId =  value.serviceId;
													    newArr.tariffId =  value1.tariffId;
													    newArr.visitTypeId =  value1.visitTypeId;
													    newArr.serviceId =  value2.componenetServicesId;
													    newArr.organizationId =  self.model.organizationId;
													    newArr.unitId =  self.model.unitId;
													    newArr.billingBedCatId =  value1.bedCategoryId;
													    newArr.isRefDoctorShare = "N";
													    newArr.serviceName =    value2.componenetServicesDesc;
													    newArr.doctorList = [];
													    newArr.isEditable = false;
													    newArr.isGrade = false;
														newArr.amount = value2.valuePercentage;
														newArr.originalAmount = value2.valuePercentage;
														newArr.patientCategoryId =  value1.patientCategoryId;
														newArr.paymentEntitlementId =  value1.paymentEntitlementId;
														$scope.getSurgeryResourceConfigList.push(newArr);
													}
												 });
											}
										  });
										});
										
										angular.forEach(self.model.procedureDetails,function(value,index){
											if(value.serviceId == $scope.getSurgeryResourceConfigListByProcedureId[0].serviceId){
												     value.rate = proRate; 
											}
										});
									if($scope.getSurgeryResourceConfigList.length>0){
									$scope.prodedures.billOfresList =  $scope.prodedures.billOfresList.concat($scope.getSurgeryResourceConfigList);
									}
									console.log("bill last resource",$scope.prodedures.billOfresList);
								}
							});
					}
				});
		
		}
		
		$scope.getProcedureConfigurationDetails = function(obj){
			
			var proTime = $scope.common.preferTime != undefined ? $scope.common.preferTime : moment(new Date()).format('HH:mm:ss');
			var proDate = self.model.order.preferedDateTime!= undefined ? self.model.order.preferedDateTime : $scope.dt;
			var totalD = 0;
			self.model.procedureDetails.forEach(function (value, key) {
				
				totalD += value.procedureDuration;
				
			});
			
			$scope.calTim(proTime,totalD);
			$scope.finalSum;
			
			var admissionId  =(self.model.order.admissionId!=null && self.model.order.admissionId!="")?self.model.order.admissionId:0;
			var encounterId = (self.model.order.encounterId!=null && self.model.order.encounterId!="")?self.model.order.encounterId:0;
			
			var formattedToDate = moment(self.model.periodicityServ.toDate).format('DD-MM-YYYY HH:mm:ss');

			var URI = BASE_URL + ROOT_URL + "/api/otims/getProcedureConfigureOnOTBillingCompletion/"+admissionId+"/"+encounterId+"/"+obj.serviceId+"/"+self.model.organizationId+"/"+self.model.unitId+"/"
				                              +proDate+"/"+proTime+"/"+$scope.finalSum+"/"+formattedToDate;
				 BillingGenericService.serviceAction(METHOD_GET, URI,{}, NOTIFICATION_MSG_STATUS_FALSE).then(function (response) {
					if (response.data.status == "success")

						//inventory Items
						var implantFlag = false;
						var medFlag = false;
						if(response.data.object!=null && response.data.object.inventoryItemList!=null && response.data.object.inventoryItemList.length>0){
							angular.forEach(response.data.object.inventoryItemList,function(value,index){
								if(value.implant){
									if(self.model.implantItems.length>0){
										if(!implantFlag){
										angular.forEach(self.model.implantItems,function(val,idx){
											if(val.itemId != value.itemId){
												value.orgId = self.model.organizationId;
												value.unitId = self.model.unitId;
												value.storeId = $scope.storeId;
												value.createdBy =self.model.userId;
												value.updatedBy = self.model.userId;
												value.taxId = value.salesTaxId;
												value.productCategoryId = value.impProductId;
												self.model.implantItems.push(value);
											}
										});
										}else{
											implantFlag = true;
											value.orgId = self.model.organizationId;
											value.unitId = self.model.unitId;
											value.storeId = $scope.storeId;
											value.createdBy =self.model.userId;
											value.updatedBy = self.model.userId;
											value.taxId = value.salesTaxId;
											value.productCategoryId = value.impProductId;
											self.model.implantItems.push(value);
										}
									}else{
										implantFlag = true;
										value.orgId = self.model.organizationId;
										value.unitId = self.model.unitId;
										value.storeId = $scope.storeId;
										value.createdBy =self.model.userId;
										value.updatedBy = self.model.userId;
										value.taxId = value.salesTaxId;
										value.productCategoryId = value.impProductId;
										self.model.implantItems.push(value);
									}
								}else{
									if(self.model.medicationItems.length>0){
										if(!medFlag){
										angular.forEach(self.model.medicationItems,function(val,idx){
											if(val.itemId != value.itemId){
												value.orgId = self.model.organizationId;
												value.unitId = self.model.unitId;
												value.storeId = $scope.storeId;
												value.createdBy =self.model.userId;
												value.updatedBy = self.model.userId;
												value.taxId = value.salesTaxId;
												value.productCategoryId = value.medProductId;
												self.model.medicationItems.push(value);
											}
										});
										}else{
											medFlag = true;
											value.orgId = self.model.organizationId;
											value.unitId = self.model.unitId;
											value.storeId = $scope.storeId;
											value.createdBy =self.model.userId;
											value.updatedBy = self.model.userId;
											value.taxId = value.salesTaxId;
											value.productCategoryId = value.medProductId;
											self.model.medicationItems.push(value);
										}
									}else{
										medFlag = true;
										value.orgId = self.model.organizationId;
										value.unitId = self.model.unitId;
										value.storeId = $scope.storeId;
										value.createdBy =self.model.userId;
										value.updatedBy = self.model.userId;
										value.taxId = value.salesTaxId;
										value.productCategoryId = value.medProductId;
										self.model.medicationItems.push(value);
									}

								}
							});
						}
					
					 
					// notes
					var noteFlag = false;
					if(response.data.object!=null && response.data.object.noteList.length>0){
						angular.forEach(response.data.object.noteList,function(value,index){
							if($scope.prodedures.oTCompletionNoteList.length>0){
								if(!noteFlag){
								angular.forEach($scope.prodedures.oTCompletionNoteList,function(val,idx){
									if(value.noteMasterId != val.noteMasterId && value.noteType == val.noteTypeId){
										var newdata ={}
										newdata.description =value.description;
										newdata.template = value.template;
										newdata.noteMasterId = value.noteMasterId;
										newdata.noteTypeId = value.noteType;
										newdata.organizationId=self.model.organizationId;
										newdata.unitId=self.model.unitId;
										newdata.createdBy=self.model.userId;
										newdata.updatedBy=self.model.userId;
										newdata.status='A';
										newdata.createdDate=$scope.dt;
										newdata.updatedDate=$scope.dt;
										newdata.procedureOrderId=value.serviceId;
										$scope.prodedures.oTCompletionNoteList.push(newdata);
									}
								});
								}else{
									noteFlag = true;
									var newdata ={}
									newdata.description =value.description;
									newdata.template = value.template;
									newdata.noteMasterId = value.noteMasterId;
									newdata.noteTypeId = value.noteType;
									newdata.organizationId=self.model.organizationId;
									newdata.unitId=self.model.unitId;
									newdata.createdBy=self.model.userId;
									newdata.updatedBy=self.model.userId;
									newdata.status='A';
									newdata.createdDate=$scope.dt;
									newdata.updatedDate=$scope.dt;
									newdata.procedureOrderId=value.serviceId;
									$scope.prodedures.oTCompletionNoteList.push(newdata);								}
							}else{
								noteFlag = true;
								var newdata ={}
								newdata.description =value.description;
								newdata.template = value.template;
								newdata.noteMasterId = value.noteMasterId;
								newdata.noteTypeId = value.noteType;
								newdata.organizationId=self.model.organizationId;
								newdata.unitId=self.model.unitId;
								newdata.createdBy=self.model.userId;
								newdata.updatedBy=self.model.userId;
								newdata.status='A';
								newdata.createdDate=$scope.dt;
								newdata.updatedDate=$scope.dt;
								newdata.procedureOrderId=value.serviceId;
								$scope.prodedures.oTCompletionNoteList.push(newdata);							}
						});
					} 
					
					//serviceList
					var serviceFlag = false;
					if(response.data.object!=null && response.data.object.serviceList.length>0){
						angular.forEach(response.data.object.serviceList,function(value,index){
							if(self.model.serviceDetails.length>0){
								if(!serviceFlag){
								angular.forEach(self.model.serviceDetails,function(val,idx){
									if(val.serviceId != value.serviceId){
										var tempServiceObj = {};
										tempServiceObj.otServiceId = null;
										tempServiceObj.serviceId = value.serviceId;
										tempServiceObj.amount = value.rate;
										tempServiceObj.rate = value.rate;
										tempServiceObj.status = 'A';
										tempServiceObj.orgId = self.model.organizationId;
										tempServiceObj.unitId = self.model.unitId;
										tempServiceObj.createdBy = self.model.userId;
										tempServiceObj.updatedBy = self.model.userId;
										tempServiceObj.createdDate = $scope.dt;
								    	tempServiceObj.updatedDate = $scope.dt;
										tempServiceObj.speciality = value.specialityName;
										tempServiceObj.serviceName = value.serviceStandardName;
										tempServiceObj.procedureTypeId = null;
										tempServiceObj.procedureOrderId = null;
										tempServiceObj.proReqId = $scope.common.procReqId != undefined ?$scope.common.procReqId : null ;
										tempServiceObj.procedureId = value.procConfigServiceId;
										tempServiceObj.isEditable = 1;
										tempServiceObj.specialityId = value.specialityId;
										tempServiceObj.subSpecialityId = value.subSpecialityId;
										self.model.serviceDetails.push(tempServiceObj);

									}
								});
								}else{
									serviceFlag = true;
									var tempServiceObj = {};
									tempServiceObj.otServiceId = null;
									tempServiceObj.serviceId = value.serviceId;
									tempServiceObj.amount = value.rate;
									tempServiceObj.rate = value.rate;
									tempServiceObj.status = 'A';
									tempServiceObj.orgId = self.model.organizationId;
									tempServiceObj.unitId = self.model.unitId;
									tempServiceObj.createdBy = self.model.userId;
									tempServiceObj.updatedBy = self.model.userId;
									tempServiceObj.createdDate = $scope.dt;
							    	tempServiceObj.updatedDate = $scope.dt;
									tempServiceObj.speciality = value.specialityName;
									tempServiceObj.serviceName = value.serviceStandardName;
									tempServiceObj.procedureTypeId = null;
									tempServiceObj.procedureOrderId = null;
									tempServiceObj.proReqId = $scope.common.procReqId != undefined ?$scope.common.procReqId : null ;
									tempServiceObj.procedureId = value.procConfigServiceId;
									tempServiceObj.isEditable = 1;
									tempServiceObj.specialityId = value.specialityId;
									tempServiceObj.subSpecialityId = value.subSpecialityId;
									self.model.serviceDetails.push(tempServiceObj);
								}
							}else{
								serviceFlag = true;
								var tempServiceObj = {};
								tempServiceObj.otServiceId = null;
								tempServiceObj.serviceId = value.serviceId;
								tempServiceObj.amount = value.rate;
								tempServiceObj.rate = value.rate;
								tempServiceObj.status = 'A';
								tempServiceObj.orgId = self.model.organizationId;
								tempServiceObj.unitId = self.model.unitId;
								tempServiceObj.createdBy = self.model.userId;
								tempServiceObj.updatedBy = self.model.userId;
								tempServiceObj.createdDate = $scope.dt;
						    	tempServiceObj.updatedDate = $scope.dt;
								tempServiceObj.speciality = value.specialityName;
								tempServiceObj.serviceName = value.serviceStandardName;
								tempServiceObj.procedureTypeId = null;
								tempServiceObj.procedureOrderId = null;
								tempServiceObj.proReqId = $scope.common.procReqId != undefined ?$scope.common.procReqId : null ;
								tempServiceObj.procedureId = value.procConfigServiceId;
								tempServiceObj.isEditable = 1;
								tempServiceObj.specialityId = value.specialityId;
								tempServiceObj.subSpecialityId = value.subSpecialityId;
								self.model.serviceDetails.push(tempServiceObj);

							}
						});
					}
					
					//equipmentDetailsList
					if(response.data.object!=null && response.data.object.equipmentDetailList.length>0){
						angular.forEach(response.data.object.equipmentDetailList,function(value,index){
							if($scope.equipmentDetailsList.length>0){
								var equipFlag = false ;
								angular.forEach($scope.equipmentDetailsList,function(val,idx){

									if(value.itemId==val.itemId){
										equipFlag = true;
									}
								});
								
									if(equipFlag == false){
										var newArr = {};
										newArr.otCompletionEquipmentId = null;
										newArr.equipmentTypeId = value.equipmentTypeId;
										newArr.equipmentTypeName = value.equipmentTypeName;
										newArr.itemId = value.itemId;
										newArr.equipmentName = value.itemName;
										newArr.batchId = null;
										newArr.fromTime = value.fromTime;
										newArr.toTime = value.toTime;
										newArr.fromDate = value.fromDate;
										newArr.toDate = value.toDate;
										newArr.amount= value.amount;
										newArr.serviceId= value.serviceId;
										newArr.status="A";
										newArr.organizationId = self.model.organizationId;
										newArr.unitId = self.model.unitId;
										newArr.createdBy = self.model.userId;
										newArr.updatedBy = self.model.userId;
										newArr.createdDate = $scope.dt;
										newArr.updatedDate = $scope.dt;
										newArr.procedureOrderId = value.procConfigServiceId;;
										newArr.isEditable = 1;

										$scope.equipmentDetailsList.push(newArr);
										
									}
								
							}else{
								var newArr = {};
								newArr.otCompletionEquipmentId = null;
								newArr.equipmentTypeId = value.equipmentTypeId;
								newArr.equipmentTypeName = value.equipmentTypeName;
								newArr.itemId = value.itemId;
								newArr.equipmentName = value.itemName;
								newArr.batchId = null;
								newArr.fromTime = value.fromTime;
								newArr.toTime = value.toTime;
								newArr.fromDate = value.fromDate;
								newArr.toDate = value.toDate;
								newArr.amount= value.amount;
								newArr.serviceId= value.serviceId;
								newArr.status="A";
								newArr.organizationId = self.model.organizationId;
								newArr.unitId = self.model.unitId;
								newArr.createdBy = self.model.userId;
								newArr.updatedBy = self.model.userId;
								newArr.createdDate = $scope.dt;
								newArr.updatedDate = $scope.dt;
								newArr.procedureOrderId = value.procConfigServiceId;;
								newArr.isEditable = 1;

								$scope.equipmentDetailsList.push(newArr);

							}
						});
					}
				});
				 
			
		}
		$scope.getProcedureServiceRate = function(procedureDetails,index){
			//$scope.list = [];
			
			/*$scope.list.push(procedureDetails);
				var URI = BASE_URL + ROOT_URL + "/api/procedure/getProcedureServiceRate";
				BillingGenericService.serviceAction(METHOD_POST, URI, $scope.list, NOTIFICATION_MSG_STATUS_FALSE).then(function (response) {
					if(response.data.status=="success"){
					$scope.list = [];
					if(response.data.listObject.length>0){
						self.model.procedureDetails[self.model.procedureDetails.length-1].rate = response.data.listObject[0].rate;
						$scope.getBillOdMaterialListByProcId(procedureDetails,index);
						$scope.getProcedureConfigurationDetails(procedureDetails);
					}
					
					}
				});	*/
			
			$scope.getBillOdMaterialListByProcId(procedureDetails,index);
			$scope.getProcedureConfigurationDetails(procedureDetails);
		}
		
	  	$scope.procedureReqMaster = {
      		  proReqId : "",
      		  primaryProcedureId : "",
      		  patientId : "",
      		  admissionId : "",
      		  encounterId : "",
      		  visitTypeId : "",
      		  preferedDateTime : "",
      		  preferedTime:"",
      		  requestDate : "",
      		  requestBy :$scope.userId,
      		  procedureStatusId : "",
      		  otCompletionDateTime : "",
      		  otCompletionBy : "",
      		  otBillCompletionDateTime : "",
      		  otBillCompletionBy : "",
      		  orgId : $scope.organizationId,
      		  unitId : $scope.unitId,
      		  createdBy : $scope.userId,
      		  createdDate : "",
      		  updatedBy : $scope.userId,
      		  updatedDate : "",
      		  emergency : true
      		}
	  	
		$scope.procedureSequenceMasterList = [
			{
				id : 1,
				sequenceName : "Primary"
			},
			{
				id : 2,
				sequenceName : "Secondary"
			},
			{
				id : 3,
				sequenceName : "Tertiary"
			},
			{
				id : 4,
				sequenceName : "Quaternary"
			},
			{
				id : 5,
				sequenceName : "Quinary"
			}
			];
		
		$scope.procedureSequenceList = [
			{
				id : 0,
				sequenceName : " "
			}
			];
	  	
		$scope.procedureDetlsForRate = [];
		var addProcedureDetails = function () {
			
			if($scope.patientId == "" ||$scope.patientId == undefined || $scope.patientId==null){
				
				 growl.warning("Please select patient..!!",
	                        {
	                            title : 'Warning!'
	                        });
				 	$scope.saveFlag = true;
	                $rootScope.stopLoader();
	                return false;
			}
			
            if(self.model.procDtl.specialityId == "" || self.model.procDtl.specialityId == undefined){
                growl.warning("Please select speciality..!!",
                        {
                            title : 'Warning!'
                        });
                $rootScope.stopLoader();
                return false;
           }
           
           if(self.model.procDtl.procedureTypeId == "" || self.model.procDtl.procedureTypeId == undefined){
                growl.warning("Please select procedure type..!!",
                       {
                           title : 'Warning!'
                       });
               $rootScope.stopLoader();
               return false;
           }
           
           if(self.model.procDtl.serviceId == "" || self.model.procDtl.serviceId == undefined){
                growl.warning("Please select procedure..!!",
                       {
                           title : 'Warning!'
                       });
               $rootScope.stopLoader();
               return false;
           }

           if(self.model.order.billingBedCategoryId == "" || self.model.order.billingBedCategoryId == undefined){
               growl.warning("Please select billing bed category..!!",
                      {
                          title : 'Warning!'
                      });
              $rootScope.stopLoader();
              return false;
          } 
			
			if(self.model.procedureDetails.length>0){
                var found = self.model.procedureDetails.find(ar=>ar.serviceId == self.model.procDtl.serviceId && ar.status!='I');
                if(found!=undefined)
                    {
                    growl.warning("Procedure already exists..!!",
                             {
                                 title : 'Warning!'
                             });
                     $rootScope.stopLoader();
                     return false;
                    }
                self.model.procDtl.isPrimarySecondary = "N";
			}else{
				if($scope.common.proReqId == undefined){
					$scope.procedureReqMaster.primaryProcedureId = self.model.procDtl.serviceId;
					$scope.procedureReqMaster.preferedDateTime = $scope.dt;
					$scope.procedureReqMaster.preferedTime = moment(new Date()).format('HH:mm:ss');
					$scope.procedureReqMaster.requestDate = $scope.dt
					$scope.procedureReqMaster.requestBy = self.model.userId;
					$scope.procedureReqMaster.orgId = self.model.organizationId;
					$scope.procedureReqMaster.unitId = self.model.unitId;
					$scope.procedureReqMaster.procedureStatusId =4;
					$scope.procedureReqMaster.otBillCompletionDateTime = $scope.dt
					$scope.procedureReqMaster.createdBy = self.model.userId;
					$scope.procedureReqMaster.updatedBy = self.model.userId;
					$scope.procedureReqMaster.createdDate = $scope.dt;
					$scope.procedureReqMaster.updatedDate = $scope.dt;
                    $scope.procedureReqMaster.emergency = true;

				}
				self.model.procDtl.isPrimarySecondary = "Y";
			}
			
			 var isServiceExist=undefined ;
			 var procedureSequenceIdx ;
			 
			if(self.model.procedureDetails!=null && self.model.procedureDetails.length>0){
				  angular.forEach(self.model.procedureDetails,function(val,idx){
						if(val.serviceId == self.model.procDtl.serviceId && val.status=='I'){
							isServiceExist = val ;
							procedureSequenceIdx = idx;
						} 
					 });
			}
			
			if(isServiceExist!=undefined){
				 isServiceExist.status = 'A';
				 
				 angular.forEach(self.model.procedureDetails,function(val,idx){
					if(isServiceExist.procedureSequence <= val.procedureSequence && val.status=='A' && idx!=procedureSequenceIdx){
						val.procedureSequence = val.procedureSequence +1; 
						//val.rate = val.originalAmount;
						//val.amount = val.originalAmount;
					} 
				 });
				 
				 var cnt = 0;
				 if(self.model.procedureDetails!=null && self.model.procedureDetails.length>0){
					angular.forEach(self.model.procedureDetails,function(v,i){
						if(v.status == 'A'){
							cnt = cnt +1;
						}
					});
				 }	
					$scope.procedureSequenceList.push($scope.procedureSequenceMasterList[cnt-1]);
					/*
					if(cnt>0){
						$scope.getProcedureServiceRate(self.model.procDtl,cnt-1);
					}else{
						$scope.getProcedureServiceRate(self.model.procDtl,cnt);
					}*/
					self.model.currentProcedures = [];
					self.model.procedureDetails.forEach(function (value, key) {
						self.model.currentProcedures.push({ procedureId: value.serviceId, procedure: value.serviceName });
					});
					
					self.model.procDtls.specialityId = "";
					self.model.procDtls.procedureTypeId = "";
					self.model.procDtls.service = "";
					
					//active removed Bill of material
					$scope.bomIdxs = [];
					angular.forEach($scope.removeBillOfresList,function(val,idx){
						if(val.procedureId == isServiceExist.serviceId && val.status=='I'){
							val.status = 'A';
							$scope.prodedures.billOfresList.push(val);
							$scope.bomIdxs.push(idx);
						}
					});
				
					angular.forEach($scope.bomIdxs,function(idx){
						$scope.removeBillOfresList.splice(idx,1);
					});
					
					//active removed services
					angular.forEach(self.model.serviceDetails,function(val,idx){
						if(val.procedureOrderId == isServiceExist.serviceId && val.status=='I'){
							val.status = 'A';
						}
					});
					
					//active removed equipment equipmentDetailsList
					angular.forEach($scope.equipmentDetailsList,function(val,idx){
						if(val.procedureOrderId == isServiceExist.serviceId && val.status=='I'){
							val.status = 'A';
						}
					});
					
					//active Removed Implant
					angular.forEach(self.model.implantItems,function(val,idx){
						if(val.procedureOrderId == isServiceExist.serviceId && val.status=='I'){
							val.status = 'A';
						}
					});
					
					//active Removed medicationItems
					angular.forEach(self.model.medicationItems,function(val,idx){
						if(val.procedureOrderId == isServiceExist.serviceId && val.status=='I'){
							val.status = 'A';
						}
					});
					
					//remove notes 
					angular.forEach($scope.prodedures.oTCompletionNoteList,function(val,idx){
						if(val.procedureOrderId == isServiceExist.serviceId && val.status=='I'){
							val.status = 'A';
						}
					});
					
					
					$rootScope.startLoader();
					$timeout(function() {
						$rootScope.startLoader();
						self.model.procedureDetails.forEach(function (value, index)
								{
									
									if(procedureSequenceIdx != index){
										
										//value.originalAmount = value.amount != null ?  value.amount  : value.rate ;
										
										if(value.originalAmount == 0){
											$scope.prodedures.billOfresList.forEach(function (billvalue, billIndex){
												
												if(value.serviceId == billvalue.procedureId && billvalue.billOfMaterialId!=null){
													
													value.originalAmount = value.originalAmount + billvalue.originalAmount ;
												}
												
											});
										}
										
										if(value.subSequentDetailsList != null && value.subSequentDetailsList.length>0){
											
											var subSeqObj =  value.subSequentDetailsList.find(obj => {return obj.sequenceId == value.procedureSequence})
											
											if(subSeqObj != undefined){
												
												if(subSeqObj.discPercentage !=null && subSeqObj.discPercentage != 0){
													
													value.amount = (value.originalAmount * subSeqObj.discPercentage)/100;
													value.rate  = value.amount;
													
													$scope.prodedures.billOfresList.forEach(function (billvalue, billIndex){
														
														//billvalue.totalAmount = billvalue.amount;
														
														if(value.serviceId == billvalue.procedureId && billvalue.billOfMaterialId!=null){
															
															billvalue.amount = (billvalue.originalAmount * subSeqObj.discPercentage)/100;
														}
														
													});
													
												}else{
													var amt = 0.0 ;
													$scope.prodedures.billOfresList.forEach(function (billvalue, billIndex){
														
													//	billvalue.totalAmount = billvalue.amount;
														
														if(value.serviceId == billvalue.procedureId && billvalue.billOfMaterialId!=null){
															
															var infoObj = subSeqObj.subSequentDetailsInfoList.find(obj => {return obj.serviceId == billvalue.serviceId});
															
															if(infoObj!=undefined){
																
																if(infoObj.discPercentage!=null && infoObj.discPercentage!=0){
																	amt = amt + (billvalue.originalAmount * infoObj.discPercentage)/100;
																	billvalue.amount = (billvalue.originalAmount * infoObj.discPercentage)/100;
																}
															}
															
														}
														
													});
													
													value.amount = amt;
													value.rate  = amt; 
												}
											}
											
										}
									}
									
							
								});	
						$rootScope.stopLoader();
						
					}, 3000);
					
					
			}else{
			self.model.procDtl.preferedDateTime = self.model.order.preferedDateTime!= undefined ? self.model.order.preferedDateTime : moment(new Date()).format('YYYY-MM-DD');
			self.model.procDtl.orgId = self.model.organizationId;
			self.model.procDtl.unitId = self.model.unitId;
			self.model.procDtl.createdBy = self.model.userId;
			self.model.procDtl.updatedBy = self.model.userId;
			self.model.procDtl.createdDate = $scope.dt;
			self.model.procDtl.updatedDate = $scope.dt;
			self.model.procDtl.patientId = self.model.order.patientId;
			self.model.procDtl.paymentEntitlementId = self.model.order.paymentEntitlementId;
			self.model.procDtl.visitTypeId = self.model.order.visitTypeId;
			self.model.procDtl.admissionId = self.model.order.admissionId;
			self.model.procDtl.patientTypeId = self.model.order.patientTypeId;
			self.model.procDtl.billingBedCategoryId = self.model.order.billingBedCategoryId;
			self.model.procDtl.proReqId = self.model.order.proReqId != undefined ? self.model.order.proReqId:null;
			self.model.procDtl.rate = 0;
			self.model.procDtl.status = "A";
			self.model.procDtl.tariffId = self.model.tariffId;
			self.model.procDtl.urgency = "0";
			self.model.procDtl.requestReason = "";
			self.model.procDtl.isIcuRequired = "N";
			self.model.procDtl.isBloodComponentRequired ="N";
			self.model.procDtl.isPacRequired = "N";
			self.model.procDtl.subSequentDetailsList = [];
			
			self.model.procedureDetails.push(self.model.procDtl);			
			
			var cnt = 0;
			if(self.model.procedureDetails!=undefined && self.model.procedureDetails.length>0){
			angular.forEach(self.model.procedureDetails,function(v,i){
				if(v.status == 'A'){
					cnt = cnt +1;
				}
			});
			}
			/*if(self.model.procedureDetails!=null && self.model.procedureDetails.length>1){
				self.model.procDtl.procedureSequence = cnt+1;
				$scope.procedureSequenceList.push($scope.procedureSequenceMasterList[cnt]);
			}else{
				self.model.procDtl.procedureSequence = 1;
				$scope.procedureSequenceList.push($scope.procedureSequenceMasterList[cnt-1]);
			}*/
			
			self.model.procDtl.procedureSequence = cnt;
			$scope.procedureSequenceList.push($scope.procedureSequenceMasterList[cnt-1]);
		
		/*	if(cnt>0){
				$scope.getProcedureServiceRate(self.model.procDtl,cnt-1);
			}else{
				$scope.getProcedureServiceRate(self.model.procDtl,cnt);
			}*/
			
			$scope.getProcedureServiceRate(self.model.procDtl,self.model.procedureDetails.length -1);
			
			self.model.currentProcedures = [];
			self.model.procedureDetails.forEach(function (value, key) {
				self.model.currentProcedures.push({ procedureId: value.serviceId, procedure: value.serviceName });
			});
			
			self.model.procDtls.specialityId = "";
			self.model.procDtls.procedureTypeId = "";
			self.model.procDtls.service = "";
			}
		}

		$scope.removeBillOfresList = [];
		$scope.removeProcedure = function(index){
			
			if($scope.prodedures.billOfresList.length>0){
				var temprr = [];
				angular.forEach($scope.prodedures.billOfresList,function(value,idx){
					if(value.procedureId!=self.model.procedureDetails[index].serviceId){
						
						temprr.push(value);
					}else{
						if(value.otCompletionStaffId!=null){
							
							if(value.isEditable == false) {
                                value.status="I";
                                $scope.removeBillOfresList.push(value);
							}else{
								value.status="A";
								temprr.push(value);
							}
							    
						}
					}
				});
					$scope.prodedures.billOfresList = temprr;
                }
			
			//remove services
			$scope.removedServiceList = [];
			if(self.model.serviceDetails.length>0){
				var temprr = [];
				$scope.removedServiceList = [];
				angular.forEach(self.model.serviceDetails,function(value,idx){
					if(value.procedureId!=self.model.procedureDetails[index].serviceId){
						
						temprr.push(value);
					}else{
							$scope.removeService(idx);
					}
				});
				self.model.serviceDetails = temprr;
				self.model.serviceDetails = self.model.serviceDetails.concat($scope.removedServiceList);
                }
			
			//remove equipment
			$scope.equipemtnRemovedList = [];
			if($scope.equipmentDetailsList.length>0){
				var temprr = [];
				$scope.equipemtnRemovedList = [];
				angular.forEach($scope.equipmentDetailsList,function(value,idx){
					if(value.procedureOrderId!=self.model.procedureDetails[index].serviceId){
						
						temprr.push(value);
					}else{
						$scope.removeEquipmentDetails(idx);
					}
				});
				$scope.equipmentDetailsList = temprr;
				$scope.equipmentDetailsList = $scope.equipmentDetailsList.concat($scope.equipemtnRemovedList);
                }
			
			//remove implant
			if(self.model.implantItems.length>0){
				var temprr = [];
				angular.forEach(self.model.implantItems,function(value,idx){
					if(value.serviceId!=self.model.procedureDetails[index].serviceId){
						
						temprr.push(value);
					}else{
						self.model.implantItems.splice(idx, 1);
					}
				});
				self.model.implantItems = temprr;
                }
			
			//remove Medication
			if(self.model.medicationItems.length>0){
				var temprr = [];
				angular.forEach(self.model.medicationItems,function(value,idx){
					if(value.serviceId!=self.model.procedureDetails[index].serviceId){
						
						temprr.push(value);
					}else{
						self.model.medicationItems.splice(idx, 1);
					}
				});
				self.model.medicationItems = temprr;
                }
			
			//remove notes 
			$scope.removedNotesList = [];
			if($scope.prodedures.oTCompletionNoteList.length>0){
				var temprr = [];
				$scope.removedNotesList = [];
				angular.forEach($scope.prodedures.oTCompletionNoteList,function(value,idx){
					if(value.procedureOrderId!=self.model.procedureDetails[index].serviceId){
						
						temprr.push(value);
					}else{
						$scope.removeNote(idx);
					}
				});
				$scope.prodedures.oTCompletionNoteList = temprr;
				$scope.prodedures.oTCompletionNoteList = $scope.prodedures.oTCompletionNoteList.concat($scope.removedNotesList);
                }
			
			angular.forEach(self.model.currentProcedures,function(val,idx){
				if(val.procedureId==self.model.procedureDetails[index].serviceId){
					self.model.currentProcedures.splice(idx,1);
				}
			});
			
			if(self.model.procedureDetails[index].procedureOrderId==null){
					var procedureSequence = self.model.procedureDetails[index].procedureSequence;
					$scope.procedureSequenceList.splice(self.model.procedureDetails.length,1);
					self.model.procedureDetails.splice(index,1);
					angular.forEach(self.model.procedureDetails,function(value,idx){
						if(value.procedureSequence > procedureSequence){
							value.procedureSequence = value.procedureSequence -1;
						}
					});
				}else{
					self.model.procedureDetails[index].status='I';

					var procedureSequence = self.model.procedureDetails[index].procedureSequence;
					$scope.procedureSequenceList.splice(self.model.procedureDetails.length,1);
					angular.forEach(self.model.procedureDetails,function(value,idx){
						if(value.procedureSequence > procedureSequence){
							value.procedureSequence = value.procedureSequence -1;
						}
					});
				}
			
			
			$rootScope.startLoader();
			$timeout(function() {
				$rootScope.startLoader();
				self.model.procedureDetails.forEach(function (value, index)
						{
							//value.originalAmount = value.amount != null ?  value.amount  : value.rate ;
							if(value.status == 'A'){
								if(value.originalAmount == 0){
									$scope.prodedures.billOfresList.forEach(function (billvalue, billIndex){
										
										if(value.serviceId == billvalue.procedureId && billvalue.billOfMaterialId!=null){
											
											value.originalAmount = value.originalAmount + billvalue.originalAmount ;
										}
										
									});
								}
								
								if(value.subSequentDetailsList != null && value.subSequentDetailsList.length>0){
									
									var subSeqObj =  value.subSequentDetailsList.find(obj => {return obj.sequenceId == value.procedureSequence})
									
									if(subSeqObj != undefined){
										
										if(subSeqObj.discPercentage !=null && subSeqObj.discPercentage != 0){
											
											value.amount = (value.originalAmount * subSeqObj.discPercentage)/100;
											value.rate  = value.amount;
											
											$scope.prodedures.billOfresList.forEach(function (billvalue, billIndex){
												
												//billvalue.totalAmount = billvalue.amount;
												
												if(value.serviceId == billvalue.procedureId && billvalue.billOfMaterialId!=null){
													
													billvalue.amount = (billvalue.originalAmount * subSeqObj.discPercentage)/100;
												}
												
											});
											
										}else{
											var amt = 0.0 ;
											$scope.prodedures.billOfresList.forEach(function (billvalue, billIndex){
												
											//	billvalue.totalAmount = billvalue.amount;
												
												if(value.serviceId == billvalue.procedureId && billvalue.billOfMaterialId!=null){
													
													var infoObj = subSeqObj.subSequentDetailsInfoList.find(obj => {return obj.serviceId == billvalue.serviceId});
													
													if(infoObj!=undefined){
														
														if(infoObj.discPercentage!=null && infoObj.discPercentage!=0){
															amt = amt + (billvalue.originalAmount * infoObj.discPercentage)/100;
															billvalue.amount = (billvalue.originalAmount * infoObj.discPercentage)/100;
														}
													}
													
												}
												
											});
											
											value.amount = amt;
											value.rate  = amt; 
										}
									}
									
								}
							}
						});	
				$rootScope.stopLoader();
				
			}, 3000);
			}
		var selectProcService = function (item, model, label) {
			self.model.procDtl = item;
		}

		$scope.getSelectedSpecialityID = function (specialityId) {
			$scope.specialityId = specialityId;
		}

		var searchServices = function (serviceName) {

			$scope.spllist=[];
			$scope.spllist.push($scope.specialityId);
			
			var data = 
			{
			"orgnisationId":$scope.organizationId,
			"unitId":$scope.unitId,
		//	"specialityIdList":$scope.spllist,
			"searchKeyword":serviceName
			}

			//var URI = BASE_URL + ROOT_URL + "/global/searchUnitServiceByMultipleSpecialityAndSubSpeciality";
			var URI = BASE_URL + ROOT_URL + "/global/searchServiceByNameOrCode";
			console.log("update status",data);
			return GenericService.serviceAction("POST",URI,data).then(function(response){
			if (response.data.status == 'success')
			{
					return response.data.listObject;
				}
			});

		}

		var selectService = function ($item, model, label) 
		{
			if($scope.patientId == "" ||$scope.patientId == undefined || $scope.patientId==null)
			{
				growl.warning("Please Select Patient..!!", {
					title: 'Warning!'
				});
				self.model.serv.service = ""; 
				return false
			}else{
				
				$item.orgId = self.model.organizationId;
				$item.unitId = self.model.unitId;
				$item.createdBy = self.model.createdBy;
				console.log($item);
				$scope.forServicePayeeId = 1;
				$scope.search = '';
				$('#searchServiceId').val('');
				
					var obj = {
						"unitId": self.model.unitId,
						"organizationId": self.model.organizationId,
						"visitTypeId": self.model.order.visitTypeId,
						"billingBedCategoryId": self.model.order.billingBedCategoryId,
						"patientCategoryId": self.model.order.patientCategoryId !=undefined?self.model.order.patientCategoryId:self.model.order.patientTypeId,
						"paymentEntitlementId": self.model.order.paymentEntitlementId,
						"tariffId": self.model.tariffId,
						"currentDate": moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
						"admissionId":self.model.order.admissionId !=null?self.model.order.admissionId:self.model.order.encounterId,
						"encounterId":self.model.order.encounterId !=null?self.model.order.encounterId:self.model.order.admissionId,
						"serviceId":$item.serviceId
					}
					console.log('Base prise', obj)
					//return false;
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/api/packages/getSerivceRateGlobal";
					BillingGenericService.serviceAction(METHOD_POST, URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function (response) {
						if (response.data.status == "success") {
							
							if(response.data.object != "undefined" && response.data.object != null)
							{
								
							$item.rate=response.data.object.netAmount;
								$item.quantity = 1;
							}
							else {
								growl.warning("Please add base price of this service and then add..!!", {
									title: 'Warning!'
								});
								return false
							}
							//concession
							console.log("FINAL ITEM", $item);


						}
					});
				
				self.model.srvDtl = $item;
			}
			
		
		}
		
		

		var addService = function () {
			
			/*if(self.model.serv == undefined || self.model.serv.specialityId == ""){
				growl.warning("Please select speciality ..!!", {
					title:'Warning!'G
				});
				return false
			}*/
			
			if(self.model.serv.service == ""){
				growl.warning("Please select  service..!!", {
					title: 'Warning!'
				});
				return false
			}
			
			var tempServiceObj = {}
			tempServiceObj.otServiceId = null;
			tempServiceObj.serviceId = self.model.srvDtl.serviceId;
			tempServiceObj.amount = self.model.srvDtl.rate;
			tempServiceObj.rate = self.model.srvDtl.rate;
			tempServiceObj.status = 'A';
			tempServiceObj.orgId = self.model.organizationId;
			tempServiceObj.unitId = self.model.unitId;
			tempServiceObj.createdBy = self.model.userId;
			tempServiceObj.updatedBy = self.model.userId;
			tempServiceObj.createdDate = $scope.dt;
	    	tempServiceObj.updatedDate = $scope.dt;
			tempServiceObj.speciality = self.model.srvDtl.specialityName;
			tempServiceObj.serviceName = self.model.srvDtl.serviceStandardName;
			tempServiceObj.procedureTypeId = null;
			tempServiceObj.procedureOrderId = null;
			tempServiceObj.proReqId = $scope.common.procReqId != undefined ?$scope.common.procReqId : null ;
			if(self.model.currentProcedures !=undefined && self.model.currentProcedures != null && self.model.currentProcedures.length == 1){
				tempServiceObj.procedureId  = self.model.currentProcedures[0].procedureId;
			}else{
				tempServiceObj.procedureId = null;
			}
			tempServiceObj.isEditable = 1;
			tempServiceObj.specialityId = self.model.srvDtl.specialityId;
			tempServiceObj.subSpecialityId = self.model.srvDtl.subSpecialityId;
			self.model.serviceDetails.push(tempServiceObj);
			self.model.serv.service = ""; 
			self.model.serv.specialityId = "";
		}

		$scope.removeService = function(index){
			if(self.model.serviceDetails[index].otServiceId!=null){
				self.model.serviceDetails[index].status = "I";
				$scope.removedServiceList.push(self.model.serviceDetails[index]);
			}else{
				self.model.serviceDetails.splice(index,1);
			}
		}
		
			$scope.selectProcedure = function (srv,idx) {
			$scope.procObj = $.grep(self.model.procedureDetails, function (doc) {
				return doc.serviceId == srv.serviceId;
			})[0];
			
           /* self.model.serviceDetails[idx].procedureTypeId = $scope.procObj.procedureTypeId;
			self.model.serviceDetails[idx].procedureOrderId = $scope.procObj.procedureOrderId;
			self.model.serviceDetails[idx].procedureId = $scope.procObj.serviceId;

			var obj = {
				"unitId": self.model.unitId,
				"organizationId": self.model.organizationId,
				"visitTypeId": self.model.order.visitTypeId,
				"billingBedCategoryId": self.model.order.billingBedCategoryId, 
				"patientTypeId": self.model.order.patientCategoryId, 
				"paymentEntitlementId": self.model.order.paymentEntitlementId,
				"tariffId": self.model.tariffId,
				"orderDate": moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
				"serviceList": [self.model.serviceDetails[idx].procedureId]

			}
			console.log('Base prise', obj)
			//return false;
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + "/api/unit/getBasePriceOfServicesFromTariffMasterByServiceList";
			BillingGenericService.serviceAction(METHOD_POST, URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function (response) {
				if (response.data.status == "success") {
					var basePriceObj = response.data.object;
					if (typeof (basePriceObj) != "undefined" && basePriceObj != null) {
							if(basePriceObj.rate!=null)
						{
						self.model.serviceDetails[idx].rate = basePriceObj.rate;
						}else{
							self.model.serviceDetails[idx].rate = 0.0;
						}

					}
					else {
						growl.warning("Please add base price of this service and then add..!!", {
							title: ERROR_MSG
						});
						return false
					}
				}
			});*/
		}


		var getDoctorNameByDocId = function (id) {
			for (var i = 0; i < self.model.doctorList.length; i++) {
				if (self.model.doctorList[i].doctorId == id)
					return self.model.doctorList[i].firstName + ' ' + self.model.doctorList[i].lastName;
			}
		}

		var getRoleNameByRoleId = function (id) {
			for (var i = 0; i < self.model.roleListByDoctorCharges.length; i++) {
				if (self.model.roleListByDoctorCharges[i].id == id)
					return self.model.roleListByDoctorCharges[i].name;
			}
		}

		$scope.addDoctorCharges = function () {
			var tempObj = {
				specialityId: self.model.docCharge.specialityId,
				doctorName: self.model.getDoctorNameByDocId(self.model.docCharge.doctorId),
				roleName: self.model.getRoleNameByRoleId(self.model.docCharge.roleId),
				procedureRoleId: self.model.docCharge.roleId,
				userId: self.model.docCharge.doctorId,
				status: 'A',
				orgId: self.model.organizationId,
				unitId: self.model.unitId,
				createdBy: self.model.userId,
				updatedBy: self.model.userId,
				procedureId: '',
				procReqId: $scope.common.procReqId,
			}

			self.model.doctorCharges.push(tempObj);
			console.log("doctorCharges---", self.model.doctorCharges);
		}


		
		//GANESH FOR UOM CHANGE
	    var UomChange = function (index, bill, uom) {
			$scope.selectedUom=uom;
	      	var item = self.model.medicationItems[index];
	      	if (!!uom) {
	    		self.model.medicationItems[index].uomTypeId = $scope.selectedUom.uomTypeId;
				if(item.usedQty == undefined || item.usedQty == null || item.usedQty == ""){
					item.usedQty = 0;
				}
				if(item.isThreeUOMApplicable == false){
					if(bill.selectedBatch.uomListData == undefined || bill.selectedBatch.uomListData == null || bill.selectedBatch.uomListData == ""){
						if($scope.selectedUom.uomTypeId == 1){
							item.receivedQtyCon = (item.usedQty +"*"+ bill.uomListData[0].conversion);
							item.receivedQtyCalculation = (item.usedQty * bill.uomListData[0].conversion);
							item.uomUnitData = bill.uomListData[2].uomUnit;
						}
						else if(item.uomTypeId == 2){
							
						}
						else{
							item.receivedQtyCon = (item.usedQty);
							item.receivedQtyCalculation = (item.usedQty);
							item.uomUnitData = bill.uomListData[2].uomUnit;
						}	
					}
					else{
						if($scope.selectedUom.uomTypeId == 1){
							item.receivedQtyCon = (item.usedQty +"*"+ bill.selectedBatch.uomListData[0].conversion);
							item.receivedQtyCalculation = (item.usedQty * bill.selectedBatch.uomListData[0].conversion);
							item.uomUnitData = bill.selectedBatch.uomListData[2].uomUnit;
						}
						else if(item.uomTypeId == 2){
							
						}
						else{
							item.receivedQtyCon = (item.usedQty);
							item.receivedQtyCalculation = (item.usedQty);
							item.uomUnitData = bill.selectedBatch.uomListData[2].uomUnit;
						}
					}
				}
				else{
					if(item.uomTypeId == 1){
						item.receivedQtyCon = (item.usedQty +"*"+ bill.selectedBatch.uom[0].conversion +"*"+ bill.selectedBatch.uom[1].conversion);
						item.receivedQtyCalculation = (item.usedQty * bill.selectedBatch.uom[0].conversion * bill.selectedBatch.uom[1].conversion);
						item.uomUnitData = bill.selectedBatch.uom[2].uomUnit;
					}
					else if(item.uomTypeId == 2){
						item.receivedQtyCon = (item.usedQty +"*"+ bill.selectedBatch.uom[1].conversion);
						item.receivedQtyCalculation = (item.usedQty * bill.selectedBatch.uom[1].conversion);
						item.uomUnitData = bill.selectedBatch.uom[2].uomUnit;
					}
					else{
						item.receivedQtyCon = (item.usedQty);
						item.receivedQtyCalculation = (item.usedQty);
						item.uomUnitData = bill.selectedBatch.uom[2].uomUnit;
					}
				}
	           Calculate(item, self.model.medicationItems);
	           // Convert Ordered Quantity
	           ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);
	      	} else {
	        	item.rate = 0;
	      	}
	    }
	    
		var ConvertQtyOrdered = function (item, uom, uomList,flag) {
	         // var qtyPrescribed = item.usedQty;
	    	 if(flag == undefined){
	    		 item.usedQty = item.usedQty>0?item.usedQty:0;
	    	 }
	    	
	          if(uomList) {
	              var uomDetails = uomList.uomDetails.split(' ')[0];
	              if (uomList.uomTypeId == 1) {
	               
	                   item.newAvailableStockQuantity = Math.ceil(item.selectedBatch.availableStockQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));	
	            	  
 	              } else if (uomList.uomTypeId == 2) {
	                
	                  item.newAvailableStockQuantity = Math.ceil(item.selectedBatch.availableStockQuantity  / (uom[1].conversion * uom[2].conversion));
	            	  
	              } else if (uomList.uomTypeId == 3) {
	              
	                   item.newAvailableStockQuantity = Math.ceil(item.selectedBatch.availableStockQuantity  / (uom[2].conversion));	
	              }
	          }
	      }
		var resetCurrentProcedures = function () {
			self.model.currentProcedures = [];
			self.model.procedureDetails.forEach(function (value, key) {
				self.model.currentProcedures.push({ procedureId: value.serviceId, procedure: value.serviceName });
			});
		}

		$scope.getRate = function (item, idx) {
			var dataForRate = {
				orgId: self.model.organizationId,
				unitId: self.model.unitId,
				roleId: item.procedureRoleId,
				specialityId: item.specialityId,
				serviceId: item.procedureId,
			}
			var URI = BASE_URL + ROOT_URL + "/api/otims/getResourceChareges";
			BillingGenericService.serviceAction(METHOD_POST, URI, dataForRate, NOTIFICATION_MSG_STATUS_FALSE).then(function (response) {
				if (response.data.status == "success") 
				{
					let obj = self.model.doctorCharges.find(o => o.procedureId == item.procedureId);
					var procedureOrderId = obj.procedureOrderId;
					self.model.doctorCharges[idx].procedureOrderId = procedureOrderId;
					if (response.data.listObject.length > 0) {
						self.model.doctorCharges[idx].amount = response.data.listObject[0].amount;
					}
					else {
						self.model.doctorCharges[idx].amount = 0;
					}

				}
				else {
					self.model.doctorCharges[idx].amount = 0;
				}
			});


		}
		
		$scope.rePrintInvestigation = function(labObj, idx,printFlag) {
			if (labObj.sampleStatusId == 8) {

				if ($scope.labConfig.biochemistryId == labObj.subDeptId
						|| $scope.labConfig.clinicalPathologyId == labObj.subDeptId
						|| $scope.labConfig.hematologyId == labObj.subDeptId) {
					var data = {
						"appName" : "HIS",
						"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
						"reportDesignFilePath" : "pathologyReport.rpt",
						"reportFormat" : "PDF",
						"exportFileName" : "pathologyReport",
						"reportParams" : [ {
							"paramName" : "labtesresid",
							"paramType" : "Integer",
							"paramValue" : labObj.labTestResId
						},{
				             "paramName": "isOriginal",
				             "paramType": "Integer",
				             "paramValue": printFlag
			           } ]
					};
					reportService.generate(REPORT_API_URI1, data);
				} else if ($scope.labConfig.microbiologyId == labObj.subDeptId) {
					var data = {
						"appName" : "HIS",
						"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
						"reportDesignFilePath" : "pathologyReport_Sensitivity.rpt",
						"reportFormat" : "PDF",
						"exportFileName" : "pathologyReport",
						"reportParams" : [ {
							"paramName" : "lab_sample_dtls_id",
							"paramType" : "Integer",
							"paramValue" : labObj.labSampleDtlsId
						},{
				             "paramName": "isOriginal",
				             "paramType": "Integer",
				             "paramValue": printFlag
			           } ]
					};
					reportService.generate(REPORT_API_URI1,
							data);
				} else if ($scope.labConfig.histopathologyId == labObj.subDeptId
						|| $scope.labConfig.cystopathologyId == labObj.subDeptId) {
					var data = {
						"appName" : "HIS",
						"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
						"reportDesignFilePath" : "GetHistopathologyReport.rpt",
						"reportFormat" : "PDF",
						"exportFileName" : "pathologyReport",
						"reportParams" : [ {
							"paramName" : "labtesresid",
							"paramType" : "Integer",
							"paramValue" : labObj.labSampleDtlsId
						},{
				             "paramName": "isOriginal",
				             "paramType": "Integer",
				             "paramValue": printFlag
			           } ]
					};
					reportService.generate(REPORT_API_URI1,data);
				}else if($scope.labConfig.serologyId==labObj.subDeptId)
				{
					var data =  
					{
				            "appName": "HIS",
				            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
				            "reportDesignFilePath": "SerologyReport.rpt",
				            "reportFormat": "PDF",
				            "exportFileName": "pathologyReport",
				            "reportParams": [
				              {
					             "paramName": "lab_sample_dtls_id",
					             "paramType": "Integer",
					             "paramValue": labObj.labSampleDtlsId
				              },
				              {
						             "paramName": "isOriginal",
						             "paramType": "Integer",
						             "paramValue": printFlag
					           }
				            ]
				      };
					reportService.generate(REPORT_API_URI1,data);
					}
			}else if(labObj.templateTransactionId!=null &&labObj.templateTransactionId>0)
			{
    			var data =  {
    		            "appName": "HIS",
    		            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
    		            "reportDesignFilePath": "RadiologyReport.rpt",
    		            "reportFormat": "PDF",
    		            "exportFileName": "RadiologyReport",
    		            "reportParams": [
    		              {
    			             "paramName": "template_transaction_id",
    			             "paramType": "Integer",
    			             "paramValue": labObj.templateTransactionId
    		              },{
				        		"paramName": "isOriginal",
				        		"paramType": "Integer",
				        		"paramValue": 1
				        		},{
				        		"paramName": "isHeader",
				        		"paramType": "Integer",
				        		"paramValue": 0
				        		}
    		            ]
    		    };
    	           reportService.generate(REPORT_API_URI1,data);
				}
			
			else {
				growl.error("Final Report not released!!",
						{
							title : 'Error!'
						});

			}
		}
		
		$scope.checkReport=function(labObj,idx,printFlag)
		{
			if(labObj.sampleStatusId==8)
			{
				if($scope.labConfig.biochemistryId==labObj.subDeptId||$scope.labConfig.clinicalPathologyId==labObj.subDeptId||$scope.labConfig.hematologyId==labObj.subDeptId)
				{
					 return true;
				}
				else if($scope.labConfig.microbiologyId==labObj.subDeptId)
					{
					 return true;
					}
				else if($scope.labConfig.histopathologyId==labObj.subDeptId || $scope.labConfig.cystopathologyId==labObj.subDeptId)
				{ 
					return true;
				}
				else if($scope.labConfig.serologyId==labObj.subDeptId)
					{
					  return true;
					}
		   }
			else if(labObj.templateTransactionId!=null &&labObj.templateTransactionId>0)
				{
    	         return true;
				}
		 else
		 {
		     return false;
		  }
	 }
		
		$scope.getInvestigationDetails = function(invObj) {
			$rootScope.startLoader();
			var paramObj = {};
			$('#panelDetailsModal').modal('show');
			var headers = {
				'panelId' : invObj.panelId,
				'orderDetailsId' : invObj.orderDetailsId,
				'Content-Type' : 'application/json'
			};
			var URI = BASE_URL + ROOT_URL
					+ GETCURRENTINVESTIGATIONDETAILS;
			GenericServiceParamHeader
					.serviceAction(METHOD_GET, URI, {},
							NOTIFICATION_MSG_STATUS_FALSE,
							paramObj, headers)
					.then(
							function(response) {
								if (response.data.status == "success") {
									$scope.inveDetails = response.data.listObject;
									$rootScope.stopLoader();
								} else if (response.data.status == "error") {
									$rootScope.stopLoader();
								}
							});
		}
		
		
		$scope.getInvetigation = function(visitId,flag) {
			 $rootScope.startLoader();
			var data = {};
			var paramObj = {};
			var headers = {
				//'patientId' : $scope.admissionId,
				'visitId' :visitId,	
				'flag' : flag,
				'procedureId':$scope.proReqId!= undefined ?$scope.proReqId : null,
				'Content-Type' : 'application/json'
			};
			// var encounterId =
			// $scope.encounterDetailsObj.encounterId;
			var URI = BASE_URL + ROOT_URL + "/api/opd/getCurrentInvestigationsByProcedureIdAndVisitId/"
				//	+ GETCURRENTINVESTIGATION;
			GenericServiceParamHeader
					.serviceAction(
							METHOD_GET,
							URI,
							data,
							NOTIFICATION_MSG_STATUS_FALSE,
							paramObj, headers)
					.then(
							function(response) {
								if (response.data.status == "success") {
									$scope.currentEncounterOrderLists = response.data.listObject;
									console.log("current order list=" + $scope.currentEncounterOrderLists);
									 $rootScope.stopLoader();
								}
							});
		}
		
		$scope.patientVisitTypeId = 0;
		$scope.equipmentDetailsList = [];
		var procedureOrdersByReqId = function (proReqId, tariffId) 
		{
			if (proReqId != 0) {
				 $rootScope.startLoader();
				 $scope.equipmentDetailsList = [];
				console.log("$scope.storeId",$scope.storeId);
				var URI = BASE_URL + ROOT_URL + "/api/procedure/order/" + proReqId + "/" + tariffId + "/"+$scope.storeId;
				return BillingGenericService.serviceAction(METHOD_GET, URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function (response) {
					if (response.data.status == "success") {
						 $rootScope.stopLoader();
						var procedureOrder = response.data.object;
						$scope.prodedures = response.data.object;
						$scope.equipmentDetailsList = response.data.object.equipmentlist;
						if(response.data.object.otCompletionDetailsDto != null){
							$scope.otDetalsObj = response.data.object.otCompletionDetailsDto;
							if($scope.otDetalsObj.emergancy && $scope.otDetalsObj.encounterId!=null && $scope.otDetalsObj.encounterId!=0){
								$scope.encounterObj.encounterId = $scope.otDetalsObj.encounterId;
								$scope.encounterObj.encounterNumber = $scope.otDetalsObj.encounterNumber;
								$scope.encounterCount = 1;
								$scope.encounterList = [];
								$scope.encounterList.push($scope.encounterObj);
							}
							//$scope.otDetalsObj.otDate  = moment(moment(response.data.object.otCompletionDetailsDto.otDate,'YYYY-MM-DD')).toDate();
							$scope.otDetalsObj.otDate  = moment(response.data.object.otCompletionDetailsDto.otDate,'DD-MM-YYYY HH:mm:ss').toDate();
							self.model.timeSheet.wheelInTime = $scope.otDetalsObj.otDate;
							self.model.timeSheet.tableOnTime = $scope.otDetalsObj.otDate;
							self.model.timeSheet.anesthesiaTime = $scope.otDetalsObj.otDate;
							self.model.timeSheet.procedureStartTime = $scope.otDetalsObj.otDate;
							self.model.timeSheet.procedureEndTime = $scope.otDetalsObj.otDate;
							self.model.timeSheet.tableOutTime = $scope.otDetalsObj.otDate;
							self.model.timeSheet.wheelOutTime = $scope.otDetalsObj.otDate;
							//self.model.timeSheet.scheduleDischarge = $scope.otDetalsObj.otDate;
							self.model.timeSheet.actualDischarge = $scope.otDetalsObj.otDate;
							
							var totalMinutes = $scope.otDetalsObj.duration;
							$scope.otDetalsObj.totalHrs = "";
							self.model.order.billingBedCategoryId = response.data.object.otCompletionDetailsDto.billingBedCategoryId;
							$scope.otDetalsObj.billingBedCategoryId = response.data.object.otCompletionDetailsDto.billingBedCategoryId;
							   var hours = Math.floor(totalMinutes / 60);          
							   var minutes = totalMinutes % 60;
							   var totalhrs = (hours<10?"0"+hours:hours)+":"+(minutes<10?"0"+minutes:minutes);
							   $scope.otDetalsObj.totalHrs = totalhrs;
						}else{
							$scope.otDetalsObj.otDate  = moment($scope.common.scheduleDate).toDate();
							
							self.model.timeSheet.wheelInTime = $scope.otDetalsObj.otDate;
							self.model.timeSheet.tableOnTime = $scope.otDetalsObj.otDate;
							self.model.timeSheet.anesthesiaTime = $scope.otDetalsObj.otDate;
							self.model.timeSheet.procedureStartTime = $scope.otDetalsObj.otDate;
							self.model.timeSheet.procedureEndTime = $scope.otDetalsObj.otDate;
							self.model.timeSheet.tableOutTime = $scope.otDetalsObj.otDate;
							self.model.timeSheet.wheelOutTime = $scope.otDetalsObj.otDate;
							//self.model.timeSheet.scheduleDischarge = $scope.otDetalsObj.otDate;
							self.model.timeSheet.actualDischarge = $scope.otDetalsObj.otDate;
							
							
							var totalMinutes = $scope.common.totalDuration;
							var hours = Math.floor(totalMinutes / 60);          
							var minutes = totalMinutes % 60;
							var totalhrs = (hours<10?"0"+hours:hours)+":"+(minutes<10?"0"+minutes:minutes);
							$scope.otDetalsObj.totalHrs = totalhrs;
						}
					
						if(response.data.object.otCompletionMedicalGasList !=null ){
							
							if(response.data.object.otCompletionMedicalGasList.length>0){
								
                                angular.forEach(response.data.object.otCompletionMedicalGasList,function(value,index){
                                    value.fromDateString = moment(moment(value.fromDate,'DD-MM-YYYY').toDate()).format('DD-MM-YYYY') ;
                                    
                                    value.toDateString = moment(moment(value.toDate,'DD-MM-YYYY').toDate()).format('DD-MM-YYYY')
                                });

								
								self.model.periodicityServOrdersList = response.data.object.otCompletionMedicalGasList;
							}
						}
						if(response.data.object.otCompletionCssdKitList !=null ){
							
							if(response.data.object.otCompletionCssdKitList.length>0){
								
								$scope.CssdKitList = response.data.object.otCompletionCssdKitList;
							}
						}
						
					
						var listProcedureOrders = procedureOrder.procedureOrders;
						//procedures
						
						angular.forEach($scope.prodedures.billOfresList,function(value,key){
							if(value.isEditable == null){
								value.isEditable = false;
							}
							if(value.otCompletionStaffId!=null){
								value.specilityList =  self.model.doctorSpecialityList;
							}else{
								value.specilityList =  self.model.doctorSpecialityList;
								value.doctorList = [];
							}
						});
						
						
						if(listProcedureOrders.length > 0 ){
							
							if(listProcedureOrders[0].procedureStatusId == 3 ){
								
								listProcedureOrders.forEach(function (value, index)
										{
											value.originalAmount = value.amount != null ?  value.amount  : value.rate ;
											
											if(value.subSequentDetailsList != null && value.subSequentDetailsList.length>0){
												
												var subSeqObj =  value.subSequentDetailsList.find(obj => {return obj.sequenceId == value.procedureSequence})
												
												if(subSeqObj != undefined){
													
													if(subSeqObj.discPercentage !=null && subSeqObj.discPercentage != 0){
														
														value.amount = (value.originalAmount * subSeqObj.discPercentage)/100;
														value.rate  = value.amount;
														
														$scope.prodedures.billOfresList.forEach(function (billvalue, billIndex){
															
															//billvalue.totalAmount = billvalue.amount;
															
															if(value.serviceId == billvalue.procedureId){
																
																billvalue.amount = (billvalue.originalAmount * subSeqObj.discPercentage)/100;
																	
															}
															
														});
														
													}else{
														var amt = 0.0 ;
														$scope.prodedures.billOfresList.forEach(function (billvalue, billIndex){
															
															//billvalue.totalAmount = billvalue.amount;
															
															if(value.serviceId == billvalue.procedureId){
																
																var infoObj = subSeqObj.subSequentDetailsInfoList.find(obj => {return obj.serviceId == billvalue.serviceId});
																
																if(infoObj!=undefined){
																	
																	if(infoObj.discPercentage!=null && infoObj.discPercentage!=0){
																		amt = amt + (billvalue.originalAmount * infoObj.discPercentage)/100;
																		billvalue.amount = (billvalue.originalAmount * infoObj.discPercentage)/100;
																	}
																}
																
															}
															
														});
														
														value.amount = amt;
														value.rate  = amt; 
													}
												}
												
											}
									
										});
							}else{
								
								listProcedureOrders.forEach(function (value, index)
										{
									var originalA = 0.0;
										$scope.prodedures.billOfresList.forEach(function (billvalue, billIndex){
										
										if(value.serviceId == billvalue.procedureId){
											
											originalA = originalA + billvalue.originalAmount ;
												
										}
										
									});

											
											value.originalAmount = value.amount != null ?  value.amount  : value.rate ;
										}	
								);
							}
						}
								
						
						
								$scope.proSequenceIds = [];
						
						listProcedureOrders.forEach(function (value, index)
								{
							$scope.admissionId = value.admissionId;
							$scope.encounterId = value.encounterId; 
							if(value.patientTypeId != null){
								self.model.order.patientTypeId = value.patientTypeId;
								$scope.patientTypeId = self.model.order.patientTypeId;
							}
							
							if(value.paymentEntitlementId != null){
								self.model.order.paymentEntitlementId = value.paymentEntitlementId;
							}
							if(value.visitTypeId != null){
								$scope.visitTypeId = value.visitTypeId;
								self.model.order.visitTypeId = value.visitTypeId;
								$scope.patientVisitTypeId = value.visitTypeId;
							}
							if(value.admissionId!=null && $scope.otDetalsObj.billingBedCategoryId == undefined && $scope.otDetalsObj.billingBedCategoryId !=null){
								self.model.order.billingBedCategoryId = value.billingBedCategoryId;
								$scope.otDetalsObj.billingBedCategoryId = value.billingBedCategoryId;
							}/*else if(value.admissionId!=null){
								self.model.order.billingBedCategoryId = value.billingBedCategoryId;
								$scope.otDetalsObj.billingBedCategoryId = value.billingBedCategoryId;
							}*//*else if(value.encounterId!=null){
								self.model.order.billingBedCategoryId = cookieObjectAppConfig.opdBillingCatId;
								$scope.otDetalsObj.billingBedCategoryId = cookieObjectAppConfig.opdBillingCatId;
							}*/
							//
							
							$scope.proReqId = value.proReqId;
							
							$scope.otDetalsObj.billingBedCategoryId = value.billingBedCategoryId;
							var procDtl = { //amount: value.amount   rate: value.amount
								rate: value.rate,doctorId:value.doctorId, amount: value.rate, proReqId: value.proReqId, serviceCode: value.serviceCode, procedureOrderId: value.procedureOrderId, admissionId: value.admissionId,encounterId:value.encounterId, patientId: value.patientId, procedureTypeId: value.procedureTypeId,
								serviceId: value.serviceId, serviceName: value.service, speciality: value.speciality, preferedDateTime: value.preferedDateTime, paymentEntitlementId: value.paymentEntitlementId,
								specialityId: value.specialityId, procedureType: value.procedureType, isPrimarySecondary: value.isPrimarySecondary, orgId: value.orgId, unitId: value.unitId, visitTypeId: value.visitTypeId,
								createdBy: self.model.createdBy, subSpecialityId: value.subSpecialityId, patientTypeId: self.model.order.patientTypeId, billingBedCategoryId: value.billingBedCategoryId,status:'A',
								procedureDuration:value.duration ,procedureSequence : value.procedureSequence , subSequentDetailsList :  value.subSequentDetailsList ,originalAmount : value.originalAmount
							};
							self.model.newAdmissionId = value.admissionId;
							self.model.currentProcedures.push({ procedureId: value.serviceId, procedure: value.service });
							
							self.model.procedureDetails.push(procDtl);
							var arr = {};
							arr.procedureSequence = value.procedureSequence;
							$scope.proSequenceIds.push(arr);
							 $rootScope.stopLoader();
						});
						
						//form procedure Sequence list 
						if($scope.proSequenceIds.length>0){
							$scope.proSequenceIds = $filter('orderBy')($scope.proSequenceIds, 'procedureSequence', false) ;
							angular.forEach($scope.proSequenceIds , function(val , idx){
								if(val.procedureSequence == 1){
									var newArr = {
											id : 1,
											sequenceName : "Primary"
									};
									if($scope.procedureSequenceList.length>0){
										var obj = $scope.procedureSequenceList.find(ar=>ar.id == 1);
										if(obj == undefined){
											$scope.procedureSequenceList.push(newArr);
										}
									}else{
										$scope.procedureSequenceList.push(newArr);
									}
								}else if(val.procedureSequence == 2){
									var newArr = {
											id : 2,
											sequenceName : "Secondary"
									};
									if($scope.procedureSequenceList.length>0){
										var obj = $scope.procedureSequenceList.find(ar=>ar.id == 2);
										if(obj == undefined){
											$scope.procedureSequenceList.push(newArr);
										}
									}else{
										$scope.procedureSequenceList.push(newArr);
									}
								}else if(val.procedureSequence == 3){
									var newArr = {
											id : 3,
											sequenceName : "Tertiary"
									};
									if($scope.procedureSequenceList.length>0){
										var obj = $scope.procedureSequenceList.find(ar=>ar.id == 3);
										if(obj == undefined){
											$scope.procedureSequenceList.push(newArr);
										}
									}else{
										$scope.procedureSequenceList.push(newArr);
									}
								}else if(val.procedureSequence == 4){
									var newArr = {
											id : 4,
											sequenceName : "Quaternary"
									};
									if($scope.procedureSequenceList.length>0){
										var obj = $scope.procedureSequenceList.find(ar=>ar.id == 4);
										if(obj == undefined){
											$scope.procedureSequenceList.push(newArr);
										}
									}else{
										$scope.procedureSequenceList.push(newArr);
									}
								}else if(val.procedureSequence == 5){
									var newArr = {
											id : 5,
											sequenceName : "Quinary"
									};
									if($scope.procedureSequenceList.length>0){
										var obj = $scope.procedureSequenceList.find(ar=>ar.id == 5);
										if(obj == undefined){
											$scope.procedureSequenceList.push(newArr);
										}
									}else{
										$scope.procedureSequenceList.push(newArr);
									}
								}
							});
						}
						
						
						//ot services 
						self.model.serviceDetails = procedureOrder.otServices;
						self.model.doctorCharges = procedureOrder.otCompletionStaff;
						if(procedureOrder.timeSheet!=null && procedureOrder.timeSheet!=undefined){
							self.model.timeSheet = procedureOrder.timeSheet;
							self.model.timeSheet.wheelInTime = moment(procedureOrder.timeSheet.wheelInTime,'DD-MM-YYYY HH:mm:ss').toDate();
							self.model.timeSheet.tableOnTime = moment(procedureOrder.timeSheet.tableOnTime,'DD-MM-YYYY HH:mm:ss').toDate();
							self.model.timeSheet.anesthesiaTime = moment(procedureOrder.timeSheet.anesthesiaTime,'DD-MM-YYYY HH:mm:ss').toDate();
							self.model.timeSheet.procedureStartTime = moment(procedureOrder.timeSheet.procedureStartTime,'DD-MM-YYYY HH:mm:ss').toDate();
							self.model.timeSheet.procedureEndTime = moment(procedureOrder.timeSheet.procedureEndTime,'DD-MM-YYYY HH:mm:ss').toDate();
							self.model.timeSheet.tableOutTime = moment(procedureOrder.timeSheet.tableOutTime,'DD-MM-YYYY HH:mm:ss').toDate();
							self.model.timeSheet.wheelOutTime = moment(procedureOrder.timeSheet.wheelOutTime,'DD-MM-YYYY HH:mm:ss').toDate();
							//self.model.timeSheet.scheduleDischarge = moment(procedureOrder.timeSheet.scheduleDischarge,'DD-MM-YYYY HH:mm:ss').toDate();
							self.model.timeSheet.actualDischarge = moment(procedureOrder.timeSheet.actualDischarge,'DD-MM-YYYY HH:mm:ss').toDate();
							if(self.model.timeSheet.actualDischarge=='Invalid Date' || self.model.timeSheet.actualDischarge==undefined || self.model.timeSheet.actualDischarge== null || self.model.timeSheet.actualDischarge==""){
								self.model.timeSheet.actualDischarge=new Date();
							}
							
							//for clockpickers
							self.model.timeSheet.wheelInTime1 = moment(moment(procedureOrder.timeSheet.wheelInTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('HH:mm');
							self.model.timeSheet.tableOnTime1=  moment(moment(procedureOrder.timeSheet.tableOnTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('HH:mm');
							self.model.timeSheet.anesthesiaTime1 =  moment(moment(procedureOrder.timeSheet.anesthesiaTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('HH:mm');
							self.model.timeSheet.procedureStartTime1 = moment(moment(procedureOrder.timeSheet.procedureStartTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('HH:mm');
							self.model.timeSheet.procedureEndTime1 = moment(moment(procedureOrder.timeSheet.procedureEndTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('HH:mm');
							self.model.timeSheet.tableOutTime1 =  moment(moment(procedureOrder.timeSheet.tableOutTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('HH:mm');
							self.model.timeSheet.wheelOutTime1 =  moment(moment(procedureOrder.timeSheet.wheelOutTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('HH:mm');
							//self.model.timeSheet.scheduleDischarge1 =  moment(moment(procedureOrder.timeSheet.scheduleDischarge,'DD-MM-YYYY HH:mm:ss').toDate()).format('HH:mm');
							self.model.timeSheet.actualDischarge1 = moment(moment(procedureOrder.timeSheet.actualDischarge,'DD-MM-YYYY HH:mm:ss').toDate()).format('HH:mm');
						}
						
						
						
						
						if(procedureOrder.otcCompletionInvMedication != null){
							self.model.medicationItems = procedureOrder.otcCompletionInvMedication;
						}
						if(procedureOrder.otcCompletionInvImplants != null){
							self.model.implantItems = procedureOrder.otcCompletionInvImplants;
						}
						
						//self.model.consumableItems = procedureOrder.otCompletionInvConsumable;
						
						angular.forEach(self.model.medicationItems,function(value,key)
						{
							if(value.storeId == undefined || value.storeId == null){
								value.storeId = $scope.storeId;
							}
							value.taxPercent = value.taxPercentage;
							//added by minesh for calling clubBatches function
							let clubBatchData  = clubBatches(value.batch);
							value.batch = clubBatchData;
							//code end by minesh for calling clubBatches function
							
							var batchId=value.batchId;
							var uomTypeId=value.uomTypeId;
							value.selectedBatch = value.batch.find(o => (o.batchId!=null && o.batchId ==batchId));
							if(value.selectedBatch!=undefined){
								value.availableStockQuantity = value.selectedBatch.availableStockQuantity;
								value.selectedUom = value.selectedBatch.uom.find(o => (o!=null && o.uomTypeId ==uomTypeId));
								ConvertQtyOrdered(value, value.selectedBatch.uom, value.selectedUom,1);
								Calculate(value);
								value.uomList.find(function(u){
									if(u.uomTypeId == value.uomTypeId){
									value.uomType = u.uomDetails;
									}
								})
								if(value.usedQty == undefined || value.usedQty == null || value.usedQty == ""){
									value.usedQty = 0;
								}
								if(value.isThreeUOMApplicable == false){
									value.uomListData = value.selectedBatch.uom;
									value.selectedBatch.uom = [];
									angular.forEach(value.uomListData, function(data){
										if(data.uomType != 'Stock'){
											value.selectedBatch.uom.push(data);
										}
									});
									if(value.uomTypeId == 1){
										value.receivedQtyCon = (value.usedQty +"*"+ value.uomListData[0].conversion);
										value.receivedQtyCalculation = (value.usedQty * value.uomListData[0].conversion);
										value.uomUnitData = value.uomListData[2].uomUnit;
									}
									else if(value.uomTypeId == 2){
										
									}
									else{
										value.receivedQtyCon = (value.usedQty);
										value.receivedQtyCalculation = (value.usedQty);
										value.uomUnitData = value.uomListData[2].uomUnit;
									}
								}
								else{
									if(value.uomTypeId == 1){
										value.receivedQtyCon = (value.usedQty +"*"+ value.uomList[0].conversion +"*"+ value.uomList[1].conversion);
										value.receivedQtyCalculation = (value.usedQty * value.uomList[0].conversion * value.uomList[1].conversion);
										value.uomUnitData = value.uomList[2].uomUnit;
									}
									else if(value.uomTypeId == 2){
										value.receivedQtyCon = (value.usedQty +"*"+ value.uomList[1].conversion);
										value.receivedQtyCalculation = (value.usedQty * value.uomList[1].conversion);
										value.uomUnitData = value.uomList[2].uomUnit;
									}
									else{
										value.receivedQtyCon = (value.usedQty);
										value.receivedQtyCalculation = (value.usedQty);
										value.uomUnitData = value.uomList[2].uomUnit;
									}
								}
							}
						
							$rootScope.stopLoader();
						});
						
						angular.forEach(self.model.implantItems,function(value,key){

							if(value.storeId == undefined || value.storeId == null){
								value.storeId = $scope.storeId;
							}
							value.taxPercent = value.taxPercentage;
							//added by minesh for calling clubBatches function
							let clubBatchData  = clubBatches(value.batch);
							value.batch = clubBatchData;
							//code end by minesh for calling clubBatches function
							
							var batchId=value.batchId;
							if(batchId!=undefined){
							value.selectedBatch = value.batch.find(o => (o.batchId!=null && o.batchId ==batchId));
							if(value.selectedBatch!=undefined){
								value.availableStockQuantity = value.selectedBatch.availableStockQuantity;
								Calculate(value);
								}
							}
							
						});
					
						
						if (self.model.timeSheet == null) {
							self.model.timeSheet = { status: 'A', orgId: self.model.organizationId, unitId: self.model.unitId, createdBy: self.model.userId, createdDate: $rootScope.getFormattedDate(new Date().getTime(), true) };
							self.model.timeSheet.wheelInTime = $scope.otDetalsObj.otDate;
							self.model.timeSheet.tableOnTime = $scope.otDetalsObj.otDate;
							self.model.timeSheet.anesthesiaTime = $scope.otDetalsObj.otDate;
							self.model.timeSheet.procedureStartTime = $scope.otDetalsObj.otDate;
							self.model.timeSheet.procedureEndTime = $scope.otDetalsObj.otDate;
							self.model.timeSheet.tableOutTime = $scope.otDetalsObj.otDate;
							self.model.timeSheet.wheelOutTime = $scope.otDetalsObj.otDate;
							//self.model.timeSheet.scheduleDischarge = $scope.otDetalsObj.otDate;
							self.model.timeSheet.actualDischarge = $scope.otDetalsObj.otDate;
						}
						if(self.model.timeSheet.mongoId!=null && self.model.timeSheet.mongoId!=undefined){
							$scope.mongoId = self.model.timeSheet.mongoId;
						}
						if($scope.admissionId!= undefined && $scope.admissionId!= null){
							$scope.getInvetigation($scope.admissionId,false);
						}else if($scope.encounterId!= undefined && $scope.encounterId!= null)
						{
							$scope.getInvetigation($scope.encounterId,true);
						}
					}
					else if(response.data.status == "error")
						{
						
						 growl.warning(response.data.message, {
			                    title: "Warning"
			                });
						  return false
						  $rootScope.stopLoader();
						}
					console.log("self.model.timeSheet.wheelInTime",self.model.timeSheet.wheelInTime);
					 $rootScope.stopLoader();
				     return	response;
				});
			}
		}

		var SearchItems = function (search) {
			self.model.items = [];
			/*$scope.patientClassPer = '';
			$scope.markUpDownPer = '';
			$scope.markUpDown = '';*/
			var isMedication = false;
			var isConsumable = false;
			var isImplant = false;
			if (search.length < 1) return;

		//	if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 3)) {
			if (search.length >= 1) {
				self.model.prevSearch = search;
				self.model.isOTC = false;
				
				if (self.model.active == 4) {
					isImplant = true;
				}else if(self.model.active == 1){
					isImplant = false;
				}
				if($scope.patientTypeId == undefined || $scope.patientTypeId == null || $scope.patientTypeId == ""){
					$scope.patientTypeId = (self.model.order.patientTypeId !=undefined && self.model.order.patientTypeId !=null && self.model.order.patientTypeId!="") ? self.model.order.patientTypeId : 0;
				}
				
				var aId = (self.model.order.admissionId!=null && self.model.order.admissionId!="")?self.model.order.admissionId: $scope.admissionId!=null && $scope.admissionId!=undefined? $scope.admissionId : 0;
				var eId = (self.model.order.encounterId!=null && self.model.order.encounterId!="")?self.model.order.encounterId: $scope.encounterId!=null && $scope.encounterId!=undefined? $scope.encounterId : 0;
				var URI = BASE_URL + ROOT_URL + "/api/units/kit/master/" + isImplant + "/" + search + "/"+self.model.order.paymentEntitlementId+
				"/"+aId+"/"+eId+"/"+$scope.patientTypeId;
				$rootScope.startLoader();
				return GenericService.serviceAction("GET", URI, {})
					.then(function (response) {
						
						if (!!response.data && response.data.listObject.length > 0) {
							self.model.items = response.data.listObject;
							/*angular.forEach(self.model.items, function(item1){
								//$scope.patientClassPer = item1.patientClassPercentage;
								$scope.markUpDownPer = item1.markUpDownPer;
								$scope.markUpDown = item1.markUpDown;
							})*/
							$rootScope.stopLoader();
							return $filter('filter')(self.model.items, {
								$: search
							});
						
						} else {
							NoItemFound(search);
							$rootScope.stopLoader();
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
					$rootScope.stopLoader();
					return NoItemFound(search);
				} else {
					$rootScope.stopLoader();
					return $filter('filter')(self.model.items, {
						$: search
					});
				}
			}
		}
		var SearchItemsImp = function (search) {
			var isMedication = false;
			var isConsumable = false;
			var isImplant = true;
			if (search.length < 1) return;

//			if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 3)) {
			if (search.length >= 1) {
				self.model.prevSearch = search;
				self.model.isOTC = false;
				/*if (self.model.active == 2) {
					isConsumable = true;
				}*/
				 if (self.model.active == 4) {
					isImplant = true;
				}
				var URI = CONSTANTS.OPERATION_THEATRE.SEARCH_API + isConsumable + "/" + isImplant + "/" + search + "/" + $scope.storeId + "/" + $scope.unitId;
				$rootScope.startLoader();
				return GenericService.serviceAction("GET", URI, {})
					.then(function (response) {

						if (!!response.data && response.data.length > 0) {
							self.model.items = response.data;
							$rootScope.stopLoader();
							return $filter('filter')(self.model.items, {
								$: search
							});

						} else {
							NoItemFound(search);
							$rootScope.stopLoader();
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
					$rootScope.stopLoader();
					return NoItemFound(search);
				} else {
					$rootScope.stopLoader();
					return $filter('filter')(self.model.items, {
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

		var PopupAction = function (action, modal) {
			var popup = angular.element('#' + modal);
			self.model.action = action;
			if (action == "Open") {
				popup.modal('show');
			} else {
				popup.modal('hide');
			}
		}
		$scope.medicationIndex = undefined;
		var ConfirmRemoveMedication = function (item, index) {
			$scope.medicationIndex = index;
			self.model.PopupAction('Open', 'confirmRemoveMedication');
			self.model.itemForRemove = item;
		}
		$scope.implantIndex = undefined;
		var ConfirmRemoveImplant = function (item, index) {
			$scope.implantIndex = index;
			self.model.PopupAction('Open', 'confirmRemoveImplant');
			self.model.itemForRemove = item;
		}

		
		var RemoveMedicationItem = function () {
			var newArray = [];
			if ($scope.medicationIndex != undefined) {
				self.model.medicationItems.splice($scope.medicationIndex, 1);
				self.model.PopupAction('Close', 'confirmRemoveMedication');
			}
		}
		
		var RemoveImplantItem = function () {
			var newArray = [];
			if ($scope.implantIndex != undefined) {
				self.model.implantItems.splice($scope.implantIndex, 1);
				self.model.PopupAction('Close', 'confirmRemoveImplant');
			}
		}

		//create by minesh for club batch if batchNo is same.
		var  clubBatches = function(batches){
        	// added by minesh for total of availableQuantity from all batch and get single Unique Batch
            var batchNo = [];
            var uniqueBatches = [];
        	
        		if (!!batches) {
        			batches.forEach(function (batch) {
        			var index = batchNo.indexOf(batch.batchNo);                    			 
                	if(index === -1)
                	{
                		batchNo.push(batch.batchNo);
                		uniqueBatches.push(batch);
                	}else{
                			uniqueBatches[index].availableStockQuantity += batch.availableStockQuantity;
                		}
        		});
        		}                	
        		return uniqueBatches;
        	// added by minesh for total of availableQuantity from all  batch and get single Unique Batch
        }
		//end code by minesh for club batch if batchNo is same.
		
				$scope.validateUsedQty = function(idex,flag){
							
							var tesr= 0;
				
							if(flag == 1){
								if(self.model.medicationItems[idex].bookedQty !=undefined){
				
								  if(self.model.medicationItems[idex].bookedQty < parseInt(self.model.medicationItems[idex].usedQty)){
				
									  growl.warning(" Used Quantity should be less than book Quantity", {
						                    title: "Warning"
						                });
						                self.model.medicationItems[idex].usedQty = 0;
						                self.model.medicationItems[idex].netAmount = 0;
							        }    
							    }else if(self.model.medicationItems[idex].newAvailableStockQuantity < parseInt(self.model.medicationItems[idex].usedQty)){
				
									  growl.warning(" Used Quantity should be less than available Stock Quantity", {
						                    title: "Warning"
						                });
						                self.model.medicationItems[idex].usedQty = 0;
						                self.model.medicationItems[idex].netAmount = 0;
							        }   
							}
							
							if(flag == 0){
								if(self.model.implantItems[idex].bookedQty !=undefined){
				
								  if(self.model.implantItems[idex].bookedQty < parseInt(self.model.implantItems[idex].usedQty)){
				
									  growl.warning(" Used Quantity should be less than book Quantity", {
						                    title: "Warning"
						                });
						                self.model.implantItems[idex].usedQty = 0;
							        }    
							    }else  if(self.model.implantItems[idex].availableStockQuantity < parseInt(self.model.implantItems[idex].usedQty)){
				
									  growl.warning(" Used Quantity should be less than available Stock Quantity", {
						                    title: "Warning"
						                });
						                self.model.implantItems[idex].usedQty = 0;
							        }   
							}
							
						}
		
		
		var AddItemToBill = function (itm, model, label) {
			if (!!itm.id) {
				//	var URI = CONSTANTS.GLOBAL.ITEM_API +"unit/"+$scope.unitId; + item.id+'/store/1/0';
				var URI = CONSTANTS.GLOBAL.ITEM_API  + itm.id + '/store/'+$scope.storeId+'/' + $scope.unitId;

				$rootScope.startLoader();
				GenericService.serviceAction("GET", URI, {})
					.then(function (response) {
						
						//added by minesh for calling clubBatches function
						let clubBatchData  = clubBatches(response.data.batch);
						response.data.batch = clubBatchData;  
						//code end by minesh for calling clubBatches function

						response.data.batch.unshift({
							batchDetails: "Select Batch"
						});
						var item = {
							itemId: response.data.itemId,
							itemCode: response.data.itemCode,
							itemName: response.data.itemName,
							manufacturerId: response.data.manufacturerId,
							manufacturerName: response.data.manufacturerName,
							productCategoryId: response.data.productCategoryId,
							batch: response.data.batch,
							selectedBatch: response.data.batch[0],
							taxId: response.data.taxId,
							taxName: response.data.taxName,
							taxPercent: response.data.taxPercent,
							//isOTC: self.model.isOTC,
							createdBy: self.model.userId,
							updatedBy: self.model.userId,
							orgId: self.model.organizationId,
							unitId: self.model.unitId,
							storeId:$scope.storeId,
							issueId : null,
							issueDetailsBatchMpprId : null,
							patientClassPercentage : itm.patientClassPercentage,
							markUpDown : itm.markUpDown,
							// patientClassPercentage : response.data.patientClassPercentage,
							markUpDownPer : itm.markUpDownPer,
							isThreeUOMApplicable : itm.isThreeUOMApplicable,
							isLasa : response.data.isLasa
						};
						self.model.search = '';

                         if(item.isLasa !== undefined && item.isLasa != null && item.isLasa === true){
                            growl.warning("Item is LASA drug..!",{
                                title : WARNING_MSG
                            });
                         }

						if (self.model.active == 1) {
							// item.uom = [{
							// 	uomDetails: "Select UOM",
							// }];
							// item.selectedUom = item.uom[0];
							item.genericId = response.data.genericId;
							item.genericName = response.data.genericName;
						}

						if (self.model.active == 1) {
							self.model.medicationItems.push(item);
						}
						else if (self.model.active == 2) {
							self.model.consumableItems.push(item);
						}
						else if (self.model.active == 4) {
							self.model.implantItems.push(item);
						}
						//	console.log("medicationItems-----", self.model.medicationItems);
						$rootScope.stopLoader();
					}, function (err) {
						growl.error('Error while fetching the item details');
						$rootScope.stopLoader();
					});
			}
		}

		var Calculate = function(item, bill){
			
			if(item.selectedBatch.batchId !=undefined){
				
				var sellingPrice = !!item.selectedBatch ? commonDetailService.fixedDecimal(item.selectedBatch.sellingPrice) : 0;
				if(item.selectedUom != undefined){
					item.mrp = commonDetailService.calculateRate(item.selectedBatch.uom, item.selectedUom.uomTypeId, sellingPrice);
				}
	           	self.model.beforeCalculation = item;

	            var data = {
	                "mrp": item.mrp,
	                "isTaxInc": $scope.taxInclusive,
	                // "markup": !!item.markUpDown ? item.markUpDownPer : null,
	                // "markdown": !item.markUpDown ? item.markUpDownPer : null,
					"markup": item.markUpDown,
                	"markUpDownPer" : item.markUpDownPer,
	                "discount":  0,
	                "isDiscPercent": false,
	                "tax": item.taxPercent,
	                "qty": item.usedQty,
	                "paymentEntitlementTypeId":  self.model.order.paymentEntitlementId,
	                "patientClassPercentage": isNaN(item.patientClassPercentage)?0:item.patientClassPercentage,
	            }

	            var response = commonDetailService.calculateAmount(data);
	            item.selectedBatch.rate = response.rate;
	            item.mrp = response.rate;
	            item.taxAmount = isNaN(response.taxAmount)?0:response.taxAmount;
	            item.responseTaxAmount =  isNaN(response.taxAmount)?0:response.taxAmount;
	            // item.unitDiscountAmount = response.unitDiscountAmount;
	            // item.itemDiscount =  response.discountAmount;
	            item.itemtotalAmount = isNaN(response.totalAmount)?0:response.totalAmount
	            item.totalAmount = isNaN(response.totalAmount)?0:response.totalAmount;
	            item.netAmount = isNaN(response.netAmount)?0:response.netAmount;
	            // item.selfPayable = response.netAmount;
	            // item.creditPayable = response.creditPayable;
	            // item.packageConsumptionAmt = response.packageConsumptionAmt;
	            item.patientClassAmount = isNaN(response.patientClassAmount)?0:response.patientClassAmount;
	            item.unitPatientClassAmount = isNaN(response.unitPatientClassAmount)?0:response.unitPatientClassAmount;
			}else{
				
				  growl.warning(" please select Batch ", {
	                    title: "Warning"
	                });
				  return false
			}
			
            
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

		var BatchChange = function (index, sitem, batch) {
			var item = sitem;

			if (!!batch.batchId) {
				
				if (self.model.active == 1) {
					self.model.medicationItems[index].batchId = batch.batchId;
					self.model.medicationItems[index].mrp = batch.rate;
					self.model.medicationItems[index].availableStockQuantity = batch.availableStockQuantity;
				}
				else if (self.model.active == 4) {
					self.model.implantItems[index].batchId = batch.batchId;
					self.model.implantItems[index].mrp = batch.rate;
					self.model.implantItems[index].availableStockQuantity = batch.availableStockQuantity;
					
				}
				
				item.selectedUom = batch.uom.find(function (obj) {
					return obj.opUom;
				});

				if(item.selectedUom!=undefined && item.selectedUom!=null)
				{
				    item.uomTypeId = item.selectedUom.uomTypeId;
				   	item.uomUnitId = item.selectedUom.uomUnitId;
					if (self.model.active == 1) {
						ConvertQtyOrdered(item, batch.uom, item.selectedUom);
					}
				}
				if(item.usedQty != 0){
					Calculate(sitem);
				}

				if(self.model.active == 1){
					angular.forEach(self.model.medicationItems, function(item, idx){
						if(index == idx){
							if(item.isThreeUOMApplicable == false){
								item.selectedBatch.uomListData = item.selectedBatch.uom;
								item.selectedBatch.uom = [];
								angular.forEach(item.selectedBatch.uomListData, function(data){
									if(data.uomType != 'Stock'){
										item.selectedBatch.uom.push(data);
									}
								});
								if(item.usedQty == undefined || item.usedQty == null || item.usedQty == ""){
									item.usedQty = 0;
								}
								if(item.uomTypeId == 1){
									item.receivedQtyCon = (item.usedQty +"*"+ item.selectedBatch.uomListData[0].conversion);
									item.receivedQtyCalculation = (item.usedQty * item.selectedBatch.uomListData[0].conversion);
									item.uomUnitData = item.selectedBatch.uomListData[2].uomUnit;
								}
								else if(item.uomTypeId == 2){
									
								}
								else{
									item.receivedQtyCon = (item.usedQty);
									item.receivedQtyCalculation = (item.usedQty);
									item.uomUnitData = item.selectedBatch.uomListData[2].uomUnit;
								}
							}
							else{
								if(item.uomTypeId == 1){
									item.receivedQtyCon = (item.usedQty +"*"+ item.selectedBatch.uom[0].conversion +"*"+ item.selectedBatch.uom[1].conversion);
									item.receivedQtyCalculation = (item.usedQty * item.selectedBatch.uom[0].conversion * item.selectedBatch.uom[1].conversion);
									item.uomUnitData = item.selectedBatch.uom[2].uomUnit;
								}
								else if(item.uomTypeId == 2){
									item.receivedQtyCon = (item.usedQty +"*"+ item.selectedBatch.uom[1].conversion);
									item.receivedQtyCalculation = (item.usedQty * item.selectedBatch.uom[1].conversion);
									item.uomUnitData = item.selectedBatch.uom[2].uomUnit;
								}
								else{
									item.receivedQtyCon = (item.usedQty);
									item.receivedQtyCalculation = (item.usedQty);
									item.uomUnitData = item.selectedBatch.uom[2].uomUnit;
								}
							}		
						}
					});
				}
			}
		}

		 var AllocateQuantity = function (index, bill,flag) {/*
	            if (!!bill) {
	            	var item  ;
	            		 
	            	item = bill[index];
	            	bill[index].selfPayable = item.netAmount == undefined ? 0 : item.netAmount ;
	                bill[index].creditPayable = 0;
	                item.selfPayable= item.netAmount == undefined ? 0 : item.netAmount ;
	                item.creditPayable= 0;
	                
	                if (CanAllocate(item,flag) && (item.bookedQty == undefined || item.bookedQty == null || item.bookedQty == 0) ) {
	                    var URI = CONSTANTS.PHARMACY.OP.WALK_IN_SALES.ALLOCATE_API + $scope.unitId + "/"+$scope.organizationId +"/"+$scope.storeId +"/"+$scope.userId+ "/"+item.itemId+"/"+ $scope.proReqId + "/" + (!!item.allocationId ? item.allocationId : "");
	                    var data = {
	                        "batchId": item.selectedBatch.batchId,
	                        "quantity": parseInt(item.usedQty),
	                        "isFromOt":true
	                    };

	                    $rootScope.startLoader();
	                    GenericService.serviceAction("PUT", URI, data).then(
	                        function (response) {
	                            if (!!response.data) {
	                                item.allocationId = response.data.ids[0];
	                               // item.allocationIds = response.data.ids;
	                                
	                                item.selectedBatch.availableStockQuantity = response.data.object;
	                                item.availableStockQuantity = response.data.object;
	                                item.newAvailableStockQuantity = response.data.object;
	                                angular.forEach(item.batch, function (value4, index4) {
	                                  	
	                                  	if(value4.batchId == item.selectedBatch.batchId){
	                                  		value4.availableStockQuantity = response.data.object;
	                                  	}
	                              });
	                                if(flag == false){
	                                	 ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);
	                                }
	                             
	                                StorePreviousValue(item);
	                            }
	                            $rootScope.stopLoader();
	                        },

	                        function (err) {
	                            $rootScope.stopLoader();
	                            growl.error(err.data.summary ? err.data.summary : err.data.message);
	                            RestorePreviousValue(item);
	                            item.itemtotalAmount = 0.0 ;
	                            item.totalAmount = 0.0;
	                            item.netAmount = 0.0;
	                            item.usedQty = 0;
	                        });
	                } else {
	                    StorePreviousValue(item);
	                }
	            }
	        */}

		
		    var CanAllocate = function (item,flag) {
		    	if(flag == false){
		    		
		    		 if (!!item.selectedBatch && !!item.selectedUom &&
		 	                (!!item.usedQty) && !(item.prevQuantity == item.usedQty && item.usedQty == '')) {
		 	                return true;
		 	            }
		    	}else{
		    		
		    		 if (!!item.selectedBatch && (!!item.usedQty) && !(item.prevQuantity == item.usedQty && item.usedQty == '')) {
				                return true;
				     }
		    	}
	           
	            return false;
	        }

	        var StorePreviousValue = function (item) {
	            item.prevBatch = item.selectedBatch;
	            item.prevUom = item.selectedUom == undefined ? null : item.selectedUom ;
	            item.prevQuantity = item.usedQty;

	           // CalculateRate(item, item.selectedUom, item.uom);
	        }
		
	        var RestorePreviousValue = function (item) {
	            item.selectedBatch = item.prevBatch;

	            if (!!item.selectedBatch) {
	                item.selectedUom = item.prevUom;
	            } else {
	                item.uom = [{
	                    uomType: "Select UOM"
	                }];
	                item.selectedUom = item.uom[0];
	                item.rate = 0;
	            }

	            item.usedQty = item.prevQuantity;

	           // CalculateRate(item, item.selectedUom, item.uom);
	        }

	     $scope.confirmMessage = "Upon completion, you will not be able to add any changes. Do you wish to proceed?";
	     $scope.confirmLasa = function(){
             if(self.model.medicationItems !== undefined && self.model.medicationItems !== null && self.model.medicationItems.length>0){
                var checkLasa = false;
                  angular.forEach(self.model.medicationItems, function(item){
                      if(item.isLasa !== undefined && item.isLasa !== null && item.isLasa === true){
                         checkLasa = true;
                      }
                 });

                 if(checkLasa){
                     checkLasa = false;
                     var popup = angular.element('#confirmSave');
                     popup.modal('show');
                 }
                 else{
                      saveProcedureOrders();
                 }
             }
             else{
                 saveProcedureOrders();
             }
	     }


		var saveProcedureOrders = function () {
			 $rootScope.startLoader();
			 if(self.model.procedureDetails!=null && self.model.procedureDetails.length>0){
				 angular.forEach(self.model.procedureDetails,function(value,index){
						var proctime = value.preferedDateTime.split('');
						if(proctime.length<12){
							value.preferedDateTime = moment(moment(value.preferedDateTime +" 00:00:00").toDate()).format('DD-MM-YYYY HH:mm:ss');
						}
					}); 
			 }
			$scope.procedureDetails = angular.copy(self.model.procedureDetails);
			
			if($scope.procedureDetails !=null && $scope.procedureDetails.length>0){
				var saveprocFlag = false;
				angular.forEach($scope.procedureDetails,function(value,index){
					
					if(value.status == "A"){
						 saveprocFlag = true;
					}
				});
				if( saveprocFlag == true){
					$scope.finalSave();
				}else{
					$rootScope.stopLoader();
	                growl.warning(" please add procedure", {
	                    title: "Warning"
	                });
	                return false
				}
			
			}else{
				$rootScope.stopLoader();
                growl.warning(" please add procedure", {
                    title: "Warning"
                });
                return false
			}
		}
		$scope.medicationList = [];
		$scope.implantList = [];
		var timesheetSaveobj ={};
		$scope.finalSave = function () {
			if(self.model.isCompleted == 'Y'){
                if($scope.otDetalsObj.billingBedCategoryId ==""){
                    $rootScope.stopLoader();
                    growl.warning("Please Select Billing Bed Category ", {
                        title: "Warning"
                    });
                    return false
            }
                
			  if($scope.otDetalsObj.otTableId ==""){
	                $rootScope.stopLoader();
	                growl.warning("Please Select OT Table ", {
	                    title: "Warning"
	                });
	                return false
	            }
	            
	            if($scope.otDetalsObj.anesthesiaTypeId ==""){
	                $rootScope.stopLoader();
	                growl.warning("Please Select Anesthesia Type Id", {
	                    title: "Warning"
	                });
	                return false
	            }
	            var found =false;
	            if($scope.prodedures.billOfresList.length>0){
	            	angular.forEach($scope.prodedures.billOfresList,function(value,index){
	            		if(value.isRefDoctorShare=='Y'){
	            			if((value.specilityId == "" || value.specilityId ==undefined || value.specilityId == null) || (value.doctorId == "" || value.doctorId ==undefined || value.doctorId == null) ){
	            				found = true;
	            			}
	            		}
	            		if(value.procedureId == "" || value.procedureId ==undefined || value.procedureId == null){
	            			found = true;
	            		}
	            	});
	            }
	        	if($scope.otDetalsObj.totalHrs == undefined || $scope.otDetalsObj.totalHrs == "" || $scope.otDetalsObj.totalHrs ==null){
					$rootScope.stopLoader();
					  growl.warning("Please Select Ot Total Hours", {
		                    title: "Warning"
		                });
		                return false;
				}
	        	
	        	if($scope.otDetalsObj.otDate == undefined || $scope.otDetalsObj.otDate == "" || $scope.otDetalsObj.otDate ==null){
					$rootScope.stopLoader();
					  growl.warning("Please Select Ot Date", {
		                    title: "Warning"
		                });
		                return false;
				}

	        	if(found){
	                  $rootScope.stopLoader();
	                  growl.warning("Please Fill Bill Of Material Details ", {
	                      title: "Warning"
	                  });
	                  return false;
	              }
	              
	        	if(self.model.serviceDetails.length>0){
	              var serv = self.model.serviceDetails.find(ar=>(ar.procedureId == "" || ar.procedureId ==undefined || ar.procedureId == null) );
	              if(serv!=undefined){
	                  $rootScope.stopLoader();
	                  growl.warning("Please Fill Service Details ", {
	                      title: "Warning"
	                  });
	                  return false;
	              }
	        	}
	        	
	              if( $scope.equipmentDetailsList.length>0){
	              var equip =  $scope.equipmentDetailsList.find(ar=>(ar.procedureOrderId == "" || ar.procedureOrderId ==undefined || ar.procedureOrderId == null) );
	              if(equip!=undefined){
	                  $rootScope.stopLoader();
	                  growl.warning("Please Fill Equipment Details ", {
	                      title: "Warning"
	                  });
	                  return false;
	              }
	              }
	              
	              if($scope.CssdKitList.length>0){
	              var cssd =   $scope.CssdKitList.find(ar=>(ar.kitTransitionNo == "" || ar.kitTransitionNo ==undefined || ar.kitTransitionNo == null) );
	              if(cssd!=undefined){
	                  $rootScope.stopLoader();
	                  growl.warning("Please Fill CSSD Details ", {
	                      title: "Warning"
	                  });
	                  return false
	              }
	              }
			
	             if(self.model.timeSheet.wheelInTime == undefined){
	            	  $rootScope.stopLoader();
	            	  growl.warning("Please Enter WheelInTime ", {
                      title: "Warning"
	            	  });
                  return false
	             }
	             
	             if(self.model.timeSheet.tableOnTime == undefined){
	            	  $rootScope.stopLoader();
	            	  growl.warning("Please Enter tableOnTime ", {
                     title: "Warning"
	            	  });
                 return false
	             }
	             
	             if(self.model.timeSheet.anesthesiaTime == undefined){
	            	  $rootScope.stopLoader();
	            	  growl.warning("Please Enter anesthesiaTime ", {
                     title: "Warning"
	            	  });
                 return false
	             }
	             
	             if(self.model.timeSheet.procedureStartTime == undefined){
	            	  $rootScope.stopLoader();
	            	  growl.warning("Please Enter procedureStartTime ", {
                     title: "Warning"
	            	  });
                 return false
	             }
	             
	             if(self.model.timeSheet.procedureEndTime == undefined){
	            	  $rootScope.stopLoader();
	            	  growl.warning("Please Enter procedureEndTime ", {
                     title: "Warning"
	            	  });
                 return false
	             }
	             
	             if(self.model.timeSheet.tableOutTime == undefined){
	            	  $rootScope.stopLoader();
	            	  growl.warning("Please Enter tableOutTime ", {
                     title: "Warning"
	            	  });
                 return false
	             }
	             
	             if(self.model.timeSheet.wheelOutTime == undefined){
	            	  $rootScope.stopLoader();
	            	  growl.warning("Please Enter wheelOutTime ", {
                     title: "Warning"
	            	  });
                 return false
	             }
	             
	           /*  if(self.model.timeSheet.scheduleDischarge == undefined){
	            	  $rootScope.stopLoader();
	            	  growl.warning("Please Enter scheduleDischarge ", {
                    title: "Warning"
	            	  });
                return false
	             }*/
	             
	             if(self.model.timeSheet.actualDischarge == undefined){
	            	  $rootScope.stopLoader();
	            	  growl.warning("Please Enter actualDischarge ", {
                    title: "Warning"
	            	  });
                return false
	             }
	             var wheelInDate = moment(moment(self.model.timeSheet.wheelInTime,'DD-MM-YYYY').toDate()).format('YYYY-MM-DD');
	            	 
	             var wheelOutDate = moment(moment(self.model.timeSheet.wheelOutTime,'DD-MM-YYYY').toDate()).format('YYYY-MM-DD');
	             
	             if(wheelInDate == wheelOutDate){
	            	 var wheelInTime = self.model.timeSheet.wheelInTime1;
	            	 var wheelOutTime = self.model.timeSheet.wheelOutTime1;
                   if(wheelInTime != "00:00" && wheelOutTime != "00:00" )
	            	 if(wheelInTime >= wheelOutTime){
	            		 $rootScope.stopLoader();
	            		 growl.warning("Wheel out Time should be Greater than wheel in time ", {
	            		 title: "Warning"
	            		 });
	            		 return false
	            	 }
	             }else if(wheelInDate > wheelOutDate){
	            	 $rootScope.stopLoader();
            		 growl.warning("Wheel out Time should be Greater than wheel in time ", {
            		 title: "Warning"
            		 });
            		 return false
	             }
	             
	             var wheelInCompTime = moment(moment(self.model.timeSheet.wheelInTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('YYYY-MM-DD HH:mm');
	             
	             var procOrderTime = $scope.newOrderDateTime !=undefined ? moment(moment($scope.newOrderDateTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('YYYY-MM-DD HH:mm') : moment(moment(new Date(),'DD-MM-YYYY HH:mm:ss').toDate()).format('YYYY-MM-DD HH:mm');
	             var procOrderDate = $scope.newOrderDateTime !=undefined ? moment(moment($scope.newOrderDateTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('YYYY-MM-DD') :moment(moment(new Date(),'DD-MM-YYYY HH:mm:ss').toDate()).format('YYYY-MM-DD');
		        
		           
	             var  tableOnDate  =  moment(moment(self.model.timeSheet.tableOnTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('YYYY-MM-DD');
	             var  anesthesiaDate  =  moment(moment(self.model.timeSheet.anesthesiaTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('YYYY-MM-DD');
	             var  procedureStartDate  =  moment(moment(self.model.timeSheet.procedureStartTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('YYYY-MM-DD');
	             var  procedureEndDate  =  moment(moment(self.model.timeSheet.procedureEndTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('YYYY-MM-DD');
	             var  tableOutDate  =  moment(moment(self.model.timeSheet.tableOutTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('YYYY-MM-DD');
	             var  wheelOutDate  =  moment(moment(self.model.timeSheet.wheelOutTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('YYYY-MM-DD');
	            // var  scheduleDischargeDate  =  moment(moment(self.model.timeSheet.scheduleDischarge,'DD-MM-YYYY HH:mm:ss').toDate()).format('YYYY-MM-DD');
	             var  actualDischargeDate  =  moment(moment(self.model.timeSheet.actualDischarge,'DD-MM-YYYY HH:mm:ss').toDate()).format('YYYY-MM-DD');
	            // var scheduleDate = moment(moment(self.model.timeSheet.scheduleDischarge,'DD-MM-YYYY').toDate()).format('YYYY-MM-DD');
	             
	        
	             
	             var  tableOnTime  =  self.model.timeSheet.tableOnTime1;
	             var  anesthesiaTime  =  self.model.timeSheet.anesthesiaTime1;
	             var  procedureStartTime  =  self.model.timeSheet.procedureStartTime1;
	             var  procedureEndTime  =  self.model.timeSheet.procedureEndTime1;
	             var  tableOutTime  =  self.model.timeSheet.tableOutTime1;
	             var  wheelOutTime  =  self.model.timeSheet.wheelOutTime1;
	             //var  scheduleDischargeTime  =  self.model.timeSheet.scheduleDischarge1;
	             var  actualDischargeTime  =  self.model.timeSheet.actualDischarge1;
		          
	           //1
	             if(wheelInDate == tableOnDate){
	            	 if(tableOnTime != "00:00" && wheelInTime != "00:00" )
	            	 if(tableOnTime<=wheelInTime){
	            		 $rootScope.stopLoader();
	            		 growl.warning("table On Time should be greater than Wheel In Time ", {
	            		 title: "Warning"
	            		 });
	            		 return false
	            	 }
	             }
	            //2
	             if( tableOnDate == anesthesiaDate){
	            	 if(anesthesiaTime != "00:00" && tableOnTime != "00:00" )
	            	 if(anesthesiaTime<=tableOnTime){
	            		 $rootScope.stopLoader();
	            		 growl.warning("anesthesia Time should be greater than Table On Time ", {
	            		 title: "Warning"
	            		 });
	            		 return false	
	            	 }
	             }
	             //3
	             if( anesthesiaDate == procedureStartDate){
	            	 if(procedureStartTime != "00:00" && anesthesiaTime != "00:00" )
	            	 if(procedureStartTime<=anesthesiaTime){
	            		 $rootScope.stopLoader();
	            		 growl.warning("procedure Start Time should be greater than anesthesia Time ", {
	            		 title: "Warning"
	            		 });
	            		 return false
	            	 }
	             }
	             //4
	             if( procedureStartDate == procedureEndDate){
	            	 if(procedureEndTime != "00:00" && procedureStartTime != "00:00" )
	            	 if(procedureEndTime<=procedureStartTime){
	            		 $rootScope.stopLoader();
	            		 growl.warning("procedure End Time should be greater than procedure Start Time ", {
	            		 title: "Warning"
	            		 });
	            		 return false
	            	 }
	             }
	             //5
	             if( procedureEndDate == tableOutDate){
	            	 if(tableOutTime != "00:00" && procedureEndTime != "00:00" )
	            	 if(tableOutTime<=procedureEndTime){
	            		 $rootScope.stopLoader();
	            		 growl.warning("table Out Time should be greater than procedure End Time ", {
	            		 title: "Warning"
	            		 });
	            		 return false
	            	 }
	             }
	             //6
	             if( tableOutDate == wheelOutDate){
	            	 if(wheelOutTime != "00:00" && tableOutTime != "00:00" )
	            	 if(wheelOutTime<=tableOutTime){
	            		 $rootScope.stopLoader();
	            		 growl.warning("wheelOutTime should be greater than table Out Time ", {
	            		 title: "Warning"
	            		 });
	            		 return false
	            	 }
	             }
	           //7
	            /* if(wheelOutDate == scheduleDischargeDate){
	            	 if(scheduleDischargeTime != "00:00" && wheelOutTime != "00:00" )
	            	 if(scheduleDischargeTime<=wheelOutTime){
	            		 $rootScope.stopLoader();
	            		 growl.warning("schedule Discharge Time should be greater than wheel Out Time ", {
	            		 title: "Warning"
	            		 });
	            		 return false
	            	 }
	             }*/
	             //8
	             if(wheelOutDate == actualDischargeDate){
	            	 if(actualDischargeTime != "00:00" && wheelOutTime != "00:00" )
	            	 if(actualDischargeTime<=wheelOutTime){
	            		 $rootScope.stopLoader();
	            		 growl.warning("actual Discharge Time should be greater than wheel Out Time ", {
	            		 title: "Warning"
	            		 });
	            		 return false
	            	 }
	             }
	             
	             timesheetSaveobj = self.model.timeSheet;
	             timesheetSaveobj.wheelInTime = moment(wheelInDate + " "+self.model.timeSheet.wheelInTime1+":00").format('DD-MM-YYYY HH:mm:ss');
	             timesheetSaveobj.wheelOutTime = moment(wheelOutDate + " "+self.model.timeSheet.wheelOutTime1+":00").format('DD-MM-YYYY HH:mm:ss');
	             timesheetSaveobj.tableOnTime = moment(tableOnDate + " "+self.model.timeSheet.tableOnTime1+":00").format('DD-MM-YYYY HH:mm:ss');
	             timesheetSaveobj.anesthesiaTime = moment(anesthesiaDate + " "+self.model.timeSheet.anesthesiaTime1+":00").format('DD-MM-YYYY HH:mm:ss');
	             timesheetSaveobj.procedureStartTime = moment(procedureStartDate + " "+self.model.timeSheet.procedureStartTime1+":00").format('DD-MM-YYYY HH:mm:ss');
	             timesheetSaveobj.procedureEndTime = moment(procedureEndDate + " "+self.model.timeSheet.procedureEndTime1+":00").format('DD-MM-YYYY HH:mm:ss');
	             timesheetSaveobj.tableOutTime = moment(tableOutDate + " "+self.model.timeSheet.tableOutTime1+":00").format('DD-MM-YYYY HH:mm:ss');
	             //timesheetSaveobj.scheduleDischarge = moment(scheduleDate + " "+self.model.timeSheet.scheduleDischarge1+":00").format('DD-MM-YYYY HH:mm:ss');
	             timesheetSaveobj.actualDischarge = moment(actualDischargeDate + " "+self.model.timeSheet.actualDischarge1+":00").format('DD-MM-YYYY HH:mm:ss');
	             timesheetSaveobj.status= self.model.timeSheet.status;
	             timesheetSaveobj.orgId = self.model.timeSheet.orgId;
	             timesheetSaveobj.unitId= self.model.timeSheet.unitId;
	             timesheetSaveobj.createdBy= self.model.timeSheet.createdBy;
	             timesheetSaveobj.createdDate = self.model.timeSheet.createdDate;
	             timesheetSaveobj.updatedBy = self.model.timeSheet.updatedBy;
	             timesheetSaveobj.multidelete("wheelInTime1", "wheelOutTime1","tableOnTime1",
							"anesthesiaTime1","procedureStartTime1","procedureEndTime1"
							,"tableOutTime1","actualDischarge1");
	         	     
	        if(self.model.medicationItems.length>0){
	        	 var medBatch =   self.model.medicationItems.find(ar=>(ar.batchId == undefined || ar.batchId ==null || ar.batchId == "") );
	             if(medBatch!=undefined){
	                 $rootScope.stopLoader();
	                 growl.warning("Please Select Batch", {
	                     title: "Warning"
	                 });
	                 return false;
	             }
	             
            var medItems =   self.model.medicationItems.find(ar=>(ar.usedQty == undefined || ar.usedQty ==null || ar.usedQty == "") );
            if(medItems!=undefined){
                $rootScope.stopLoader();
                growl.warning("Please Fill Medication Used Quantity", {
                    title: "Warning"
                });
                return false;
            }
           
	        }
	             
            if(self.model.implantItems.length>0){
            	 var implantBatch =   self.model.implantItems.find(ar=>(ar.batchId == undefined || ar.batchId ==null || ar.batchId == "") );
		            if(implantBatch!=undefined){
		                $rootScope.stopLoader();
		                growl.warning("Please Select Batch", {
		                    title: "Warning"
		                });
		                return false;
		            }	
            	
            var implantItems =   self.model.implantItems.find(ar=>(ar.usedQty == undefined || ar.usedQty ==null || ar.usedQty == "") );
            if(implantItems!=undefined){
                $rootScope.stopLoader();
                growl.warning("Please Fill Implant Used Quantity", {
                    title: "Warning"
                });
                return false;
            }
            }
			}else{
				
                if($scope.otDetalsObj.billingBedCategoryId ==""){
                    $rootScope.stopLoader();
                    growl.warning("Please Select Billing Bed Category ", {
                        title: "Warning"
                    });
                    return false
                }
					 if($scope.otDetalsObj.otTableId ==""){
			                $rootScope.stopLoader();
			                growl.warning("Please Select OT Table ", {
			                    title: "Warning"
			                });
			                return false
			            }
			            
			            if($scope.otDetalsObj.anesthesiaTypeId ==""){
			                $rootScope.stopLoader();
			                growl.warning("Please Select Anesthesia Type Id", {
			                    title: "Warning"
			                });
			                return false
			            }
			           
			        	if($scope.otDetalsObj.totalHrs == undefined || $scope.otDetalsObj.totalHrs == "" || $scope.otDetalsObj.totalHrs ==null){
							$rootScope.stopLoader();
							  growl.warning("Please Select Ot Total Hours", {
				                    title: "Warning"
				                });
				                return false;
						}
			        	
			        	if($scope.otDetalsObj.otDate == undefined || $scope.otDetalsObj.otDate == "" || $scope.otDetalsObj.otDate ==null){
							$rootScope.stopLoader();
							  growl.warning("Please Select Ot Date", {
				                    title: "Warning"
				                });
				                return false;
						}
			        	$rootScope.userOtTabs.inventry
				if($rootScope.userOtTabs.procedureDetails){
					 var found =false;
			            if($scope.prodedures.billOfresList.length>0){
			            	angular.forEach($scope.prodedures.billOfresList,function(value,index){
			            		if(value.isRefDoctorShare=='Y'){
			            			if((value.specilityId == "" || value.specilityId ==undefined || value.specilityId == null) || (value.doctorId == "" || value.doctorId ==undefined || value.doctorId == null) ){
			            				found = true;
			            			}
			            		}
			            		if(value.procedureId == "" || value.procedureId ==undefined || value.procedureId == null){
			            			found = true;
			            		}
			            	});
			            }
			            
			            if(found){
			                  $rootScope.stopLoader();
			                  growl.warning("Please Fill Bill Of Material Details ", {
			                      title: "Warning"
			                  });
			                  return false
			              }
				}
				if($rootScope.userOtTabs.service){
					if(self.model.serviceDetails.length>0){
					 var serv = self.model.serviceDetails.find(ar=>(ar.procedureId == "" || ar.procedureId ==undefined || ar.procedureId == null) );
		              if(serv!=undefined){
		                  $rootScope.stopLoader();
		                  growl.warning("Please Fill Service Details ", {
		                      title: "Warning"
		                  });
		                  return false
		              }
					}
				}
				
				if($rootScope.userOtTabs.equipment){
					if($scope.equipmentDetailsList.length>0){
					var equip =  $scope.equipmentDetailsList.find(ar=>(ar.procedureOrderId == "" || ar.procedureOrderId ==undefined || ar.procedureOrderId == null) );
		              if(equip!=undefined){
		                  $rootScope.stopLoader();
		                  growl.warning("Please Fill Equipment Details ", {
		                      title: "Warning"
		                  });
		                  return false
		              }
					}
				}
				
				if($rootScope.userOtTabs.inventry){
					if($rootScope.userOtTabs.medication){
					if(self.model.medicationItems.length>0){

						 var medBatch =   self.model.medicationItems.find(ar=>(ar.batchId == undefined || ar.batchId ==null || ar.batchId == "") );
				            if(medBatch!=undefined){
				                $rootScope.stopLoader();
				                growl.warning("Please Select Medication Batch", {
				                    title: "Warning"
				                });
				                return false;
				            }
				            
					 var medItems =   self.model.medicationItems.find(ar=>(ar.usedQty == undefined || ar.usedQty ==null || ar.usedQty == "" || ar.usedQty == 0 ) );
			            if(medItems!=undefined){
			                $rootScope.stopLoader();
			                growl.warning("Please Fill Medication Used Quantity", {
			                    title: "Warning"
			                });
			                return false
			            }

					}
					}
					if($rootScope.userOtTabs.implant){
					if(self.model.implantItems.length>0){
						 var implantBatch =   self.model.implantItems.find(ar=>(ar.batchId == undefined || ar.batchId ==null || ar.batchId == "") );
				            if(implantBatch!=undefined){
				                $rootScope.stopLoader();
				                growl.warning("Please Select Implant Batch", {
				                    title: "Warning"
				                });
				                return false;
				            }
				            
			            var implantItems =   self.model.implantItems.find(ar=>(ar.usedQty == undefined || ar.usedQty ==null || ar.usedQty == "" || ar.usedQty ==0 ) );
			            if(implantItems!=undefined){
			                $rootScope.stopLoader();
			                growl.warning("Please Fill Implant Used Quantity", {
			                    title: "Warning"
			                });
			                return false
			            }
			            
					}
					}
				}
				if($rootScope.userOtTabs.cssdKit){
					if($scope.CssdKitList.length>0){
					 var cssd =   $scope.CssdKitList.find(ar=>(ar.kitTransitionNo == "" || ar.kitTransitionNo ==undefined || ar.kitTransitionNo == null) );
		              if(cssd!=undefined){
		                  $rootScope.stopLoader();
		                  growl.warning("Please Fill CSSD Details ", {
		                      title: "Warning"
		                  });
		                  return false
		              }
					}
				}
				
				if($rootScope.userOtTabs.timeSheet){
					   if(self.model.timeSheet.wheelInTime == undefined){
			            	  $rootScope.stopLoader();
			            	  growl.warning("Please Enter WheelInTime ", {
		                      title: "Warning"
			            	  });
		                  return false
			             }
			             
			             if(self.model.timeSheet.tableOnTime == undefined){
			            	  $rootScope.stopLoader();
			            	  growl.warning("Please Enter tableOnTime ", {
		                     title: "Warning"
			            	  });
		                 return false
			             }
			             
			             if(self.model.timeSheet.anesthesiaTime == undefined){
			            	  $rootScope.stopLoader();
			            	  growl.warning("Please Enter anesthesiaTime ", {
		                     title: "Warning"
			            	  });
		                 return false
			             }
			             
			             if(self.model.timeSheet.procedureStartTime == undefined){
			            	  $rootScope.stopLoader();
			            	  growl.warning("Please Enter procedureStartTime ", {
		                     title: "Warning"
			            	  });
		                 return false
			             }
			             
			             if(self.model.timeSheet.procedureEndTime == undefined){
			            	  $rootScope.stopLoader();
			            	  growl.warning("Please Enter procedureEndTime ", {
		                     title: "Warning"
			            	  });
		                 return false
			             }
			             
			             if(self.model.timeSheet.tableOutTime == undefined){
			            	  $rootScope.stopLoader();
			            	  growl.warning("Please Enter tableOutTime ", {
		                     title: "Warning"
			            	  });
		                 return false
			             }
			             
			             if(self.model.timeSheet.wheelOutTime == undefined){
			            	  $rootScope.stopLoader();
			            	  growl.warning("Please Enter wheelOutTime ", {
		                     title: "Warning"
			            	  });
		                 return false
			             }
			             
			           /*  if(self.model.timeSheet.scheduleDischarge == undefined){
			            	  $rootScope.stopLoader();
			            	  growl.warning("Please Enter scheduleDischarge ", {
		                    title: "Warning"
			            	  });
		                return false
			             }*/
			             
			             if(self.model.timeSheet.actualDischarge == undefined){
			            	  $rootScope.stopLoader();
			            	  growl.warning("Please Enter actualDischarge ", {
		                    title: "Warning"
			            	  });
		                return false
			             }
			             var wheelInDate = moment(moment(self.model.timeSheet.wheelInTime,'DD-MM-YYYY').toDate()).format('YYYY-MM-DD');
			            	 
			             var wheelOutDate = moment(moment(self.model.timeSheet.wheelOutTime,'DD-MM-YYYY').toDate()).format('YYYY-MM-DD');
			             
			             var tableOnDate = moment(moment(self.model.timeSheet.tableOnTime,'DD-MM-YYYY').toDate()).format('YYYY-MM-DD');
			             var anesthesiaDate = moment(moment(self.model.timeSheet.anesthesiaTime,'DD-MM-YYYY').toDate()).format('YYYY-MM-DD');
			             var procedureStartDate = moment(moment(self.model.timeSheet.procedureStartTime,'DD-MM-YYYY').toDate()).format('YYYY-MM-DD');
			             var procedureEndDate = moment(moment(self.model.timeSheet.procedureEndTime,'DD-MM-YYYY').toDate()).format('YYYY-MM-DD');
			             var tableOutDate = moment(moment(self.model.timeSheet.tableOutTime,'DD-MM-YYYY').toDate()).format('YYYY-MM-DD');
			             //var scheduleDate = moment(moment(self.model.timeSheet.scheduleDischarge,'DD-MM-YYYY').toDate()).format('YYYY-MM-DD');
			             var actualDischargeDate = moment(moment(self.model.timeSheet.actualDischarge,'DD-MM-YYYY').toDate()).format('YYYY-MM-DD');
			             
			             
			             if(wheelInDate == wheelOutDate){
                            var wheelInTime = self.model.timeSheet.wheelInTime1 !== undefined && self.model.timeSheet.wheelInTime1 !== "" && self.model.timeSheet.wheelInTime1 !== '00:00' ? self.model.timeSheet.wheelInTime1 : undefined;
                            var wheelOutTime = self.model.timeSheet.wheelOutTime1 !== undefined && self.model.timeSheet.wheelOutTime1 !== "" && self.model.timeSheet.wheelOutTime1 !== '00:00' ? self.model.timeSheet.wheelOutTime1 : undefined;
//			            	 var wheelInTime = self.model.timeSheet.wheelInTime1;
//			            	 var wheelOutTime = self.model.timeSheet.wheelOutTime1;
		  
			            	 if(wheelInTime >= wheelOutTime){
			            		 $rootScope.stopLoader();
			            		 growl.warning("Wheel out Time should be Greater than wheel in time ", {
			            		 title: "Warning"
			            		 });
			            		 return false
			            	 }
			             }else if(wheelInDate > wheelOutDate){
			            	 $rootScope.stopLoader();
		            		 growl.warning("Wheel out Time should be Greater than wheel in time ", {
		            		 title: "Warning"
		            		 });
		            		 return false
			             }
			             
			             var wheelInCompTime = moment(moment(self.model.timeSheet.wheelInTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('YYYY-MM-DD HH:mm');
			             
			             var procOrderTime = $scope.newOrderDateTime !=undefined ? moment(moment($scope.newOrderDateTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('YYYY-MM-DD HH:mm') : moment(moment(new Date(),'DD-MM-YYYY HH:mm:ss').toDate()).format('YYYY-MM-DD HH:mm');
			             var procOrderDate = $scope.newOrderDateTime !=undefined ? moment(moment($scope.newOrderDateTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('YYYY-MM-DD') :moment(moment(new Date(),'DD-MM-YYYY HH:mm:ss').toDate()).format('YYYY-MM-DD');
				           
			             var  tableOnDate  =  moment(moment(self.model.timeSheet.tableOnTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('YYYY-MM-DD');
			             var  anesthesiaDate  =  moment(moment(self.model.timeSheet.anesthesiaTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('YYYY-MM-DD');
			             var  procedureStartDate  =  moment(moment(self.model.timeSheet.procedureStartTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('YYYY-MM-DD');
			             var  procedureEndDate  =  moment(moment(self.model.timeSheet.procedureEndTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('YYYY-MM-DD');
			             var  tableOutDate  =  moment(moment(self.model.timeSheet.tableOutTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('YYYY-MM-DD');
			             var  wheelOutDate  =  moment(moment(self.model.timeSheet.wheelOutTime,'DD-MM-YYYY HH:mm:ss').toDate()).format('YYYY-MM-DD');
			             //var  scheduleDischargeDate  =  moment(moment(self.model.timeSheet.scheduleDischarge,'DD-MM-YYYY HH:mm:ss').toDate()).format('YYYY-MM-DD');
			             var  actualDischargeDate  =  moment(moment(self.model.timeSheet.actualDischarge,'DD-MM-YYYY HH:mm:ss').toDate()).format('YYYY-MM-DD');
				           
			             var tableOnTime = self.model.timeSheet.tableOnTime1 !== undefined && self.model.timeSheet.tableOnTime1 !== "" && self.model.timeSheet.tableOnTime1 !== '00:00' ? self.model.timeSheet.tableOnTime1 : undefined;
			             var anesthesiaTime = self.model.timeSheet.anesthesiaTime1 !== undefined && self.model.timeSheet.anesthesiaTime1 !== "" && self.model.timeSheet.anesthesiaTime1 !== '00:00' ? self.model.timeSheet.anesthesiaTime1 : undefined;
			             var procedureStartTime = self.model.timeSheet.procedureStartTime1 !== undefined && self.model.timeSheet.procedureStartTime1 !== "" && self.model.timeSheet.procedureStartTime1 !== '00:00' ? self.model.timeSheet.procedureStartTime1 : undefined;
			             var procedureEndTime = self.model.timeSheet.procedureEndTime1 !== undefined && self.model.timeSheet.procedureEndTime1 !== "" && self.model.timeSheet.procedureEndTime1 !== '00:00' ? self.model.timeSheet.procedureEndTime1 : undefined;
			             var tableOutTime = self.model.timeSheet.tableOutTime1 !== undefined && self.model.timeSheet.tableOutTime1 !== "" && self.model.timeSheet.tableOutTime1 !== '00:00' ? self.model.timeSheet.tableOutTime1 : undefined;
			             var wheelOutTime = self.model.timeSheet.wheelOutTime1 !== undefined && self.model.timeSheet.wheelOutTime1 !== "" && self.model.timeSheet.wheelOutTime1 !== '00:00' ? self.model.timeSheet.wheelOutTime1 : undefined;
			             var actualDischargeTime = self.model.timeSheet.actualDischarge1 !== undefined && self.model.timeSheet.actualDischarge1 !== "" && self.model.timeSheet.actualDischarge1 !== '00:00' ? self.model.timeSheet.actualDischarge1 : undefined;
			             //var  scheduleDischargeTime  =  self.model.timeSheet.scheduleDischarge1;

			           //1
			             if(wheelInDate == tableOnDate){
			            	 if(tableOnTime<=wheelInTime){
			            		 $rootScope.stopLoader();
			            		 growl.warning("table On Time should be greater than Wheel In Time ", {
			            		 title: "Warning"
			            		 });
			            		 return false
			            	 }
			             }
			            //2
			             if( tableOnDate == anesthesiaDate){
			            	 if(anesthesiaTime<=tableOnTime){
			            		$rootScope.stopLoader();
			            		 growl.warning("anesthesia Time should be greater than Table On Time ", {
			            		 title: "Warning"
			            		 });
			            		 return false	
			            	 }
			             }
			             //3
			             if( anesthesiaDate == procedureStartDate){
			            	 if(procedureStartTime<=anesthesiaTime){
			            		 $rootScope.stopLoader();
			            		 growl.warning("procedure Start Time should be greater than anesthesia Time ", {
			            		 title: "Warning"
			            		 });
			            		 return false
			            	 }
			             }
			             //4
			             if( procedureStartDate == procedureEndDate){
			            	 if(procedureEndTime<=procedureStartTime){
			            		 $rootScope.stopLoader();
			            		 growl.warning("procedure End Time should be greater than procedure Start Time ", {
			            		 title: "Warning"
			            		 });
			            		 return false
			            	 }
			             }
			             //5
			             if( procedureEndDate == tableOutDate){
			            	 if(tableOutTime<=procedureEndTime){
			            		 $rootScope.stopLoader();
			            		 growl.warning("table Out Time should be greater than procedure End Time ", {
			            		 title: "Warning"
			            		 });
			            		 return false
			            	 }
			             }
			             //6
			             if( tableOutDate == wheelOutDate){
			            	 if(wheelOutTime<=tableOutTime){
			            		 $rootScope.stopLoader();
			            		 growl.warning("wheelOutTime should be greater than table Out Time ", {
			            		 title: "Warning"
			            		 });
			            		 return false
			            	 }
			             }
			           //7
			           /*  if(wheelOutDate == scheduleDischargeDate){
			            	 if(scheduleDischargeTime<=wheelOutTime){
			            		 $rootScope.stopLoader();
			            		 growl.warning("schedule Discharge Time should be greater than wheel Out Time ", {
			            		 title: "Warning"
			            		 });
			            		 return false
			            	 }
			             }*/
			             //8
			             if(wheelOutDate == actualDischargeDate){
			            	 if(actualDischargeTime<=wheelOutTime){
			            		 $rootScope.stopLoader();
			            		 growl.warning("actual Discharge Time should be greater than wheel Out Time ", {
			            		 title: "Warning"
			            		 });
			            		 return false
			            	 }
			             }
			            	 
			             timesheetSaveobj = self.model.timeSheet;
			             timesheetSaveobj.wheelInTime = moment(wheelInDate + " "+self.model.timeSheet.wheelInTime1+":00").format('DD-MM-YYYY HH:mm:ss');
			             timesheetSaveobj.wheelOutTime = moment(wheelOutDate + " "+self.model.timeSheet.wheelOutTime1+":00").format('DD-MM-YYYY HH:mm:ss');
			             timesheetSaveobj.tableOnTime = moment(tableOnDate + " "+self.model.timeSheet.tableOnTime1+":00").format('DD-MM-YYYY HH:mm:ss');
			             timesheetSaveobj.anesthesiaTime = moment(anesthesiaDate + " "+self.model.timeSheet.anesthesiaTime1+":00").format('DD-MM-YYYY HH:mm:ss');
			             timesheetSaveobj.procedureStartTime = moment(procedureStartDate + " "+self.model.timeSheet.procedureStartTime1+":00").format('DD-MM-YYYY HH:mm:ss');
			             timesheetSaveobj.procedureEndTime = moment(procedureEndDate + " "+self.model.timeSheet.procedureEndTime1+":00").format('DD-MM-YYYY HH:mm:ss');
			             timesheetSaveobj.tableOutTime = moment(tableOutDate + " "+self.model.timeSheet.tableOutTime1+":00").format('DD-MM-YYYY HH:mm:ss');
			             //timesheetSaveobj.scheduleDischarge = moment(scheduleDischargeDate + " "+self.model.timeSheet.scheduleDischarge1+":00").format('DD-MM-YYYY HH:mm:ss');
			             timesheetSaveobj.actualDischarge = moment(actualDischargeDate + " "+self.model.timeSheet.actualDischarge1+":00").format('DD-MM-YYYY HH:mm:ss');
			             timesheetSaveobj.status= self.model.timeSheet.status;
			             timesheetSaveobj.orgId = self.model.timeSheet.orgId;
			             timesheetSaveobj.unitId= self.model.timeSheet.unitId;
			             timesheetSaveobj.createdBy= self.model.timeSheet.createdBy;
			             timesheetSaveobj.createdDate = self.model.timeSheet.createdDate;
			             timesheetSaveobj.updatedBy = self.model.timeSheet.updatedBy;
			             timesheetSaveobj.multidelete("wheelInTime1", "wheelOutTime1","tableOnTime1",
									"anesthesiaTime1","procedureStartTime1","procedureEndTime1"
									,"tableOutTime1","actualDischarge1");
				}
				
			}
            
	        var serviceDetails = angular.copy(self.model.serviceDetails);
			serviceDetails.forEach(function (value, index) {
				value.amount = value.rate;
				value.multidelete("procedureType", "rate", "serviceCode", "serviceName", "speciality", "specialityId", "subSpecialityId");
				serviceDetails[index] = value;
			});
			$scope.finalBillOfMaterialList = angular.copy($scope.prodedures.billOfresList);
			$scope.finalBillOfMaterialList.forEach(function (value, index) {
				value.multidelete("specilityList", "doctorList");
			});
			
			$scope.CssdKitList.forEach(function (value, index) {
				value.multidelete("batchList");
			});
			
			timesheetSaveobj.proReqId = self.model.order.proReqId;
			timesheetSaveobj.updatedBy = self.model.userId;
			timesheetSaveobj.procedureDuration = self.model.order.duration;
			timesheetSaveobj.procedureTypeId = self.model.procDtls.procedureTypeId;

			var otDate = moment($scope.otDetalsObj.otDate).format('DD-MM-YYYY HH:mm:ss');
			$scope.otDetalsObj.otDate = otDate;
			

			var tm = $scope.otDetalsObj.totalHrs.split(":");
			var OTduration = (tm[0]*60);
			OTduration = OTduration  + parseInt(tm[1]);
			$scope.otDetalsObj.duration = OTduration;
			$scope.removeBillOfresList.forEach(function (value, index) {
				value.multidelete("specilityList", "doctorList");
			});
			
			$scope.otDetalsObj.procedureScheduleId = $scope.common.procedureScheduleId;
			$scope.finalBillOfMaterialList =$scope.finalBillOfMaterialList.concat($scope.removeBillOfresList);
			
			if($scope.equipmentDetailsList.length>0){
				angular.forEach($scope.equipmentDetailsList,function(val , idx){
					val.fromDate = moment(moment(val.fromDate,'yyyy-mm-dd').toDate()).format('YYYY-MM-DD');
					val.toDate = moment(moment(val.toDate,'yyyy-mm-dd').toDate()).format('YYYY-MM-DD');
					/*var fromDt = val.fromDate.split('-');
					val.fromDate=fromDt[2]+"-"+fromDt[1]+"-"+fromDt[0];
					var toDt = val.toDate.split('-');
					val.toDate=toDt[2]+"-"+toDt[1]+"-"+toDt[0];*/
				});
			}
			
			$scope.medicationList = [];
			if(self.model.medicationItems.length>0){
				angular.forEach(self.model.medicationItems,function(value,index){
					var item = {};
					item.otcCompletionInvMedicationId = value.otcCompletionInvMedicationId!=null?value.otcCompletionInvMedicationId:null,
					item.itemId =  value.itemId,
				//	item.itemCode = value.itemCode,
				//	item.itemName = value.itemName,
					item.manufacturerId = value.manufacturerId,
				//	item.manufacturerName = value.manufacturerName,
					item.productCategoryId = value.productCategoryId,
					item.taxId = value.taxId,
					// item.taxName = value.taxName,
					item.taxPercent = value.taxPercent,
					item.taxAmount = value.taxAmount,
					item.createdBy =  self.model.userId,
					item.updatedBy = self.model.userId,
					item.orgId =  self.model.organizationId,
					item.unitId =  self.model.unitId,
					//item.usedQty = value.usedQty,
					item.usedQty = value.usedQty ,
					item.remainingQty = value.remainingQty,
					item.totalAmount = value.totalAmount,
					item.mrp = value.mrp,
					item.batchId = value.batchId,
					item.storeId = $scope.storeId,
					item.uomTypeId = value.uomTypeId,
					item.uomUnitId = value.uomUnitId,
					item.issueId = value.issueId,
					item.issueDetailsBatchMpprId = value.issueDetailsBatchMpprId,
					item.bookedQty = value.bookedQty,
					item.consessionPercentage = value.patientClassPercentage,
					item.consessionAmount = value.patientClassAmount,
					item.unitConsessionAmount = value.unitPatientClassAmount,
					item.netAmount = value.netAmount,
					item.allocationId = (value.allocationId !=undefined && value.allocationId !=null) ? value.allocationId : null,
					item.fromOT = (value.fromOT != undefined || value.fromOT !=null ? value.fromOT : null)
					$scope.medicationList.push(item);
				});
			}
			
			
			$scope.implantList = [];
			if(self.model.implantItems.length>0){
				angular.forEach(self.model.implantItems,function(value,index){
					var item = {};
					item.otcCompletionInvImplantsId = value.otcCompletionInvImplantsId!=null?value.otcCompletionInvImplantsId:null,
					item.itemId =  value.itemId,
					item.manufacturerId = value.manufacturerId,
					item.productCategoryId = value.productCategoryId,
					item.taxId = value.taxId,
					item.taxPercent = value.taxPercent,
					item.taxAmount = value.taxAmount,
					item.createdBy =  self.model.userId,
					item.updatedBy = self.model.userId,
					item.orgId =  self.model.organizationId,
					item.unitId =  self.model.unitId,
					item.usedQty = value.usedQty,
					item.remainingQty = value.remainingQty,
					item.totalAmount = value.totalAmount,
					item.mrp = value.mrp,
					item.batchId = value.batchId,
					item.storeId = $scope.storeId,
					item.uomTypeId = value.uomTypeId,
					item.uomUnitId = value.uomUnitId,
					item.bookedQty = value.bookedQty,
					item.issueId = value.issueId,
					item.issueDetailsBatchMpprId = value.issueDetailsBatchMpprId,
					item.consessionPercentage = value.patientClassPercentage,
					item.consessionAmount = value.patientClassAmount,
					item.unitConsessionAmount = value.unitPatientClassAmount,
					item.netAmount = value.netAmount,
					item.allocationId = (value.allocationId !=undefined && value.allocationId !=null) ? value.allocationId : null,
					item.fromOT = (value.fromOT != undefined || value.fromOT !=null ? value.fromOT : null)
					$scope.implantList.push(item);
				});
			}
			console.log('note list ', $scope.prodedures.oTCompletionNoteList);
			var param = {
				otCompletionDetailsDto : $scope.otDetalsObj,
				procedureOrders : self.model.procedureDetails,
				procedureReqMasterDto : $scope.procedureReqMaster,
				otServices : serviceDetails,
				billOfresList : $scope.finalBillOfMaterialList,
				otCompletionMedicalGasList : self.model.periodicityServOrdersList,
				oTCompletionNoteList : $scope.prodedures.oTCompletionNoteList,
				otCompletionCssdKitList : $scope.CssdKitList,
				equipmentlist : $scope.equipmentDetailsList,
				timeSheet : timesheetSaveobj,
				isCompleted : self.model.isCompleted,
				payeeId : self.model.selfPayeeId,
				otToWardTransfer :  $scope.otToWardTransfer,
				transferRequestId : $scope.transferRequestId,
				otcCompletionInvMedication: $scope.medicationList,
				otcCompletionInvImplants : $scope.implantList
			};
			console.log('param', JSON.stringify(param));
		//	return false;
			$timeout(function () {
				console.log('param', JSON.stringify(param));
				var URI = BASE_URL + ROOT_URL + "/api/procedure/order";
				BillingGenericService.serviceAction(METHOD_POST, URI, param, NOTIFICATION_MSG_STATUS_TRUE).then(function (response) {
					if (response.data.status == "success") {
		                $scope.patientDetails = {};
		                $scope.patientDetails.isFromOtCompletion = true;
						$rootScope.stopLoader();
						$scope.removeBillOfresList = [];
						$location.path('OTCompletion');
					}
					else if (response.data.status == "error")
						{	$scope.removeBillOfresList = [];
					     	$rootScope.stopLoader();
						} 
					$rootScope.stopLoader();
				});
			}, 3000);
			
			/*var serviceDetails = angular.copy(self.model.serviceDetails);
			serviceDetails.forEach(function (value, index) {
				value.amount = value.rate;
				value.multidelete("procedureType", "rate", "serviceCode", "serviceName", "speciality", "specialityId", "subSpecialityId");
				serviceDetails[index] = value;
			});

			self.model.timeSheet.proReqId = self.model.order.proReqId;
			self.model.timeSheet.updatedBy = self.model.userId;
			self.model.timeSheet.procedureDuration = self.model.order.duration;
			self.model.timeSheet.procedureTypeId = self.model.procDtls.procedureTypeId;

			//End Medication
			//oTCompletionRequest:[], //$scope.finalInventoryList,
			var param = {
				procedureOrders: $scope.procedureDetails,
				otServices: serviceDetails,
				otCompletionStaff: self.model.doctorCharges,
				timeSheet: self.model.timeSheet,
				isCompleted: self.model.isCompleted,
				otcCompletionInvMedication: self.model.medicationItems,
				otcCompletionInvImplants: self.model.implantItems
				otCompletionInvConsumableDto:  self.model.ConsumableItems,
				otcCompletionInvMedication: $scope.finalMedicationList,
				oTCompletionRequest: $scope.finalInventoryList
			};
			//console.log(param);

			$timeout(function () {
				console.log('param', JSON.stringify(param));
				var URI = BASE_URL + ROOT_URL + "/api/procedure/order";
				BillingGenericService.serviceAction(METHOD_POST, URI, param, NOTIFICATION_MSG_STATUS_TRUE).then(function (response) {
					if (response.data.status == "success") {
							
						$rootScope.stopLoader();

						$location.path('OTCompletion');
					}
					else if (response.data.status == "error")
						{
					     	$rootScope.stopLoader();
						} 
				});
			}, 3000);*/

		}

        $scope.openConfirmPopUp = function(){
             if(self.model.medicationItems !== undefined && self.model.medicationItems !== null && self.model.medicationItems.length>0){
                var checkLasa = false;
                  angular.forEach(self.model.medicationItems, function(item){
                      if(item.isLasa !== undefined && item.isLasa !== null && item.isLasa === true){
                         checkLasa = true;
                      }
                 });

                 if(checkLasa){
                     checkLasa = false;
                     $scope.confirmMessage = "List contains LASA drugs. Upon completion, you will not be able to add any changes. Do you wish to proceed?";
                 }
                 else{
                      $scope.confirmMessage = "Upon completion, you will not be able to add any changes. Do you wish to proceed?";
                 }
             }
            if(self.model.isCompleted == 'Y'){
                $('#confirmSaveProcedure').modal('show');
            }
            else{
                $('#confirmSaveProcedure').modal('hide');
            }
        }

        $scope.closeConfirmationPopUp = function(){
            if(self.model.isCompleted == 'Y'){
               self.model.isCompleted = 'N';
            }
            $('#confirmSaveProcedure').modal('hide');
        }

		var cleartable = function () {
			self.model.search = '';
		}
		init();
		if($stateParams.resultCommonObj!=undefined && $stateParams.resultCommonObj.encounterId!=null){
			$scope.otDetalsObj.billingBedCategoryId = $scope.cookieObjectAppConfigObj.opdBillingCatId;
			$scope.patientTypeId = $scope.cookieObjectAppConfigObj.patientCategoryId;
			$scope.visitTypeId = 1;
			$scope.patientVisitTypeId = 1;
			self.model.order.billingBedCategoryId = $scope.cookieObjectAppConfigObj.opdBillingCatId;
			self.model.order.paymentEntitlementId = 1;
		}
		$scope.otDetalsObj.billingBedCategoryId = $stateParams.resultCommonObj.bedBillingCategoryId;
	/****************************************************************  Bill Of Material Tab Start    *****************************************************************/
		$scope.otRentServiceId = $scope.cookieObjectAppConfigObj.otRentServiceId;
		$scope.billOfMaterialServicesList = [];
		$scope.getBillOfMaterialServices = function(id){
			$rootScope.startLoader();
			var data = {
					organizationId:self.model.organizationId,
					unitId:self.model.unitId,
					};
			
			var URI = BASE_URL + ROOT_URL + "/api/procedure/BillOfMaterial/getBillOfMaterialServices";
			GenericService.serviceAction("POST", URI, data).then(function(response) {
				$rootScope.stopLoader();
				
				if(response.data.listObject != null ){
					
					angular.forEach(response.data.listObject,function(value,index){
						if($scope.otRentServiceId != value.serviceId){
							$scope.billOfMaterialServicesList.push(value);	
						}
						
					});
					
					$rootScope.stopLoader();
				}
			
				$rootScope.stopLoader();
			});
			
		}
		$scope.getBillOfMaterialServices();
		
		

		$scope.getDoctorBySpecialityInGrid = function (id,indx) {

			var data = {
				specialityId: id,
				unitId: self.model.unitId,
				organizationId: self.model.organizationId
			};
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL
				+ GETDOCTORBYSPECIALITYID;

			BillingGenericService
				.serviceAction(METHOD_POST, URI, data,
					NOTIFICATION_MSG_STATUS_FALSE)
				.then(
					function (response) {
						if (response.data.status == "success")
							{
								$scope.prodedures.billOfresList[indx].doctorList = response.data.listObject;
							}
						else if (response.data.status == "error")
						{
							$rootScope.stopLoader();
						}
					});

		}
		$scope.addBillOfMaterialDetails = function(){
			
            if(self.model.order.visitTypeId == ""){
                growl.warning("Please Select Patient..!!", {
                    title: 'Warning!'
                });
                return false
            }else{

				if(self.model.docCharge.serviceName!=undefined && self.model.docCharge.serviceName!=""){
				var newArr = {};
				newArr.serviceName = self.model.docCharge.serviceName;
				newArr.serviceId = self.model.docCharge.serviceId;
				if(self.model.docCharge.specialityObj!=undefined){
			    	newArr.specilityId = self.model.docCharge.specialityObj.specialityId;
			    	newArr.specialityName = self.model.docCharge.specialityObj.specialityName;	
				}
				if(self.model.docCharge.doctorObj!=undefined){
					newArr.doctorId = self.model.docCharge.doctorObj.doctorId;
				    newArr.firstName = self.model.docCharge.doctorObj.firstName;
				    newArr.lastName = self.model.docCharge.doctorObj.lastName;
				}
				
	            if(self.model.currentProcedures !=undefined && self.model.currentProcedures != null && self.model.currentProcedures.length == 1){
	            	newArr.procedureId  = self.model.currentProcedures[0].procedureId;
	            }else{
	            	newArr.procedureId  = null;
	            }
				newArr.doctorList = self.model.doctorList;
				newArr.specilityList = self.model.doctorSpecialityList;
				newArr.amount = 0;
				newArr.billOfMaterialId = null;
				newArr.billOfMaterialList = null;
				newArr.billingBedCatId = $scope.otDetalsObj.billingBedCategoryId; 
				newArr.createdBy = self.model.userId;
				newArr.createdDate = $scope.dt;
				newArr.organizationId = self.model.organizationId;
				newArr.status = "A" ;
				newArr.tariffId = self.model.tariffId;
				newArr.unitId = self.model.unitId;
				newArr.updatedBy = self.model.userId;
				newArr.updatedDate = $scope.dt;
				newArr.isEditable = true;
				$scope.prodedures.billOfresList.push(newArr);
				self.model.docCharge = {};
				}else{
					growl.warning("Please select Bill Of Material Details",
							{
								title : 'warning!'
							});
					return false;
				}
			
            }
		}
		
		$scope.removeBillOfMaterialDetails = function(index){
            if($scope.prodedures.billOfresList[index].otCompletionStaffId!=undefined || $scope.prodedures.billOfresList[index].otCompletionStaffId!=null){
				$scope.prodedures.billOfresList[index].status = 'I';
			}else{
				$scope.prodedures.billOfresList.splice(index,1);
			}
		}
		
		$scope.getBillOfMaterialServiceAmount = function(info,index){
			try{
				$rootScope.startLoader();
				
				var dataList = [];
				var data = {};
				data.procedureId = info.procedureId;
				data.serviceId = info.serviceId,
				data.billingBedCatId = info.billingBedCatId;
				
				dataList.push(data);
				
				var URI = BASE_URL + ROOT_URL + "/api/procedure/BillOfMaterial/getBillOfMaterialServiceAmount"; 
				BillingGenericService.serviceAction(METHOD_POST,URI,dataList,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					$rootScope.stopLoader();
					if(response.data.listObject.length>0){
					if(dataList.length>0){
						if(dataList.length>1){
							
						}else{
							$scope.prodedures.billOfresList[index].amount  = 	response.data.listObject[0].amount;
						}
					}
				}
					console.log("getBillOfMaterialServiceAmount list ",response.data.listObject);
				});
				
				
				$rootScope.stopLoader();
			}catch(e){
				$rootScope.stopLoader();
				console.log("Exception",e);
			}
		}
		/****************************************************************  Bill Of Material Tab end    *****************************************************************/
		
		
		/****************************************************************  Note Tab Start    *****************************************************************/
		
		$scope.ckValue = "";
		$scope.noteTypeList = [];
		$scope.ckObj = {};
		$scope.ckObjtest = {};
		$scope.ckObj1 = {};
		
		$scope.getNoteType = function(id){
			$scope.ckObj = {};
			$rootScope.startLoader();
			$scope.templateTypeId = id;
			var data = {
					organizationId:self.model.organizationId,
					unitId:self.model.unitId,
					noteType: id
					};
			
			var URI = BASE_URL + ROOT_URL + "/global/otims/getNotesMasterListByNoteTypeId";
			GenericService.serviceAction("POST", URI, data).then(function(response) {
				$rootScope.stopLoader();
				$scope.noteTypeList = response.data.listObject;
				console.log("noteTypeList",$scope.noteTypeList);
				$rootScope.stopLoader();
			});
			
		}
		
		
		$scope.getTemplateTypes = function(){
			var obj = {};
			obj.unitId = $scope.unitId;
			var URI = BASE_URL + ROOT_URL + "/api/global/templateConfig";
			BillingGenericService.serviceAction(METHOD_POST,URI,obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					 $scope.templateConfigObject = response.data.object;
				}
				else if(response.data.status == "error")
				{
					 $rootScope.stopLoader();	
				}
			});
		}
		$scope.getTemplateTypes();
		
		
	  $scope.selectNoteAnes = function(ckObj){
		    $scope.ckObj = ckObj;
			$scope.ckValue = $scope.ckObj.template;
			$scope.ckValuetitle = $scope.ckObj.title;
			
		}
	 
	  $scope.addTemplate = function()
  	{
  		
					   try{
						   if($scope.ckObj.templateFormatId != null && ($scope.ckObj.templateFormatId== 3)){
							   $rootScope.startLoaderPopup();
								    var templateTransaction = {};
									templateTransaction.templateId = $scope.ckObj.templateId;
									templateTransaction.title = $scope.ckValuetitle;
									templateTransaction.specialityId = $scope.ckObj.specialityId;
									templateTransaction.subSpecialityId = $scope.ckObj.subSpecialityId;
									templateTransaction.templateTypeId = $scope.ckObj.templateTypeId;
									templateTransaction.encounterId=$scope.common.encounterId;
									templateTransaction.admissionId = $scope.common.admissionId;
									templateTransaction.doctorId = $scope.common.doctorId;
									templateTransaction.patientId = $scope.common.patientId;
									templateTransaction.diagnosisId = 0;
									templateTransaction.mIcpcSystemId = 0;
									templateTransaction.createdBy =  $scope.userId;
									templateTransaction.unitId = $scope.unitId;
									templateTransaction.updatedBy =  $scope.userId;
									templateTransaction.orgId = $scope.organizationId;
									templateTransaction.templateId =$scope.ckObj.templateId;
                                    templateTransaction.patientName = $scope.common.patientName !== undefined && $scope.common.patientName !== null ? $scope.common.patientName : "";
                                    templateTransaction.doctorName = $scope.common.doctorName !== undefined && $scope.common.doctorName !== null ? $scope.common.doctorName : "";
                                    templateTransaction.uhidNumber = $scope.common.uhidnumber !== undefined && $scope.common.uhidnumber !== null ? $scope.common.uhidnumber : "";
                                    templateTransaction.dob = $scope.common.birthDate !== undefined && $scope.common.birthDate !== null ? $scope.common.birthDate : "";
                                    templateTransaction.ageString = $scope.common.age !== undefined && $scope.common.age !== null ? $scope.common.age : $scope.common.ageYear;
                                    templateTransaction.doa = $scope.common.createdDate !== undefined && $scope.common.createdDate !== null ? $scope.common.createdDate : "";
                                    templateTransaction.pdd = $scope.common.datepickerPdd !== undefined && $scope.common.datepickerPdd !== null ? $scope.common.datepickerPdd : "";
                                    templateTransaction.admissionNumber = $scope.common.admissionNumber !== undefined && $scope.common.admissionNumber !== null ? $scope.common.admissionNumber : "";
                                    templateTransaction.gender = $scope.common.genderCode !== undefined && $scope.common.genderCode !== null ? $scope.common.genderCode : "";
									
								  var URI = BASE_URL + ROOT_URL + NEW_TEMPLATE_SAVE;
									BillingGenericService.serviceAction(METHOD_POST, URI,templateTransaction,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(function(response) {
												if (response.data.status == "success"){
													window.open(response.data.object+"&formType=digital&patientName="+$scope.common.patientName+
                                                          "&doctorName="+$scope.common.doctorName+
                                                          "&uhid="+$scope.common.uhidnumber+
                                                          "&dob="+$scope.common.birthDate+
                                                          "&age="+$scope.common.age+
                                                          "&gender="+$scope.common.genderCode+
                                                          "&admissionNo="+$scope.common.admissionNumber+
                                                          "&admissionDate="+$scope.common.createdDate+
                                                          "&dischargeDate="+$scope.common.datepickerPdd,'_blank');
													 $rootScope.stopLoaderPopup();
												}
												else if(response.data.status == "error")
													{
													  $('#addtemplateModal').modal('hide');
													  $rootScope.stopLoaderPopup();
													}
												
											});	
									 }
							}catch(e){
								console.log(e.message);
							}
  	 
		}
	
	  
	  
  	$scope.viewTemplate = function(template)
	{
        $scope.editorData=template.templateObject;
        $scope.ckObj.templateFormatId=template.templateFormatId;
		$scope.saveTrasactionFlag = false;
		$scope.updateTrasactionFlag = false;
		$scope.obj = {};
		$('#viewtemplateModal').modal('hide');
		$('#addviewTemplateModal').modal('show');
        var lines = template.templateObject.split('\n');
        var formattedContent = lines.map(line => `<div class="styled-html">${line}</div>`).join('');
	    document.getElementById("htmlContent").innerHTML =formattedContent;
	}
  	
  	
	$scope.getTemplateListForList = function(info) 
	{
		$rootScope.startLoader();
    	var data={
    		  orgId : $scope.organizationId,
    		  unitId : $scope.unitId,
    		  doctorId : $scope.common.doctorId,
    		  specialityId : $scope.common.specialityId,
    		  templateTypeId : info.templateTypeId,
    		  patientId : $scope.common.patientId,
    		  visitTypeId: $scope.common.visitTypeId,
    		  appointmentId:$scope.common.appointmentId,
    		  templateId : info.templateId,
    		  encounterId:$scope.common.encounterId != null ? $scope.common.encounterId:null,
              admissionId:$scope.common.admissionId != null ? $scope.common.admissionId:null,
    		}
    	
    	console.log('data',JSON.stringify(data));
		var URI = BASE_URL + ROOT_URL + "/global/petient/template/otims";
		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
				{
	
			   if(response.data.status=='success')
			      {
				   $scope.templateTransactionList =[];
				   	$scope.templateTransactionList = response.data.listObject;
				   	$rootScope.stopLoader();
			      }
			   else if(response.data.status=='error'){
				   $rootScope.stopLoader();
			   }
		});
	}
	
	$scope.dismissModal = function() {
	    $('#addtemplateModalViewAddedList').modal('hide'); // Adjust this selector to your modal's ID

	};
	   	$scope.getTemplateList = function() 
    	{
    		$rootScope.startLoader();
    		
        	var data={
        		  orgId : $scope.organizationId,
        		  unitId : $scope.unitId,
        		  doctorId : $scope.common.doctorId,
        		  specialityId : $scope.common.specialityId,
        		  templateTypeId : $scope.ckObj.templateTypeId,
        		  patientId : $scope.common.patientId,
        		  visitTypeId: $scope.common.visitTypeId,
        		  appointmentId:$scope.common.appointmentId,
        		  templateId : $scope.ckObj.templateId,
        		  encounterId:$scope.common.encounterId != null ? $scope.common.encounterId:null,
        	      admissionId:$scope.common.admissionId != null ? $scope.common.admissionId:null,
        		}
        	
        	console.log('data',JSON.stringify(data));
    		var URI = BASE_URL + ROOT_URL + "/global/petient/template/otims";
    		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
    				{
    	
    			   if(response.data.status=='success')
    			      {
    				   $scope.templateTransactionList =[];
    				   	$scope.templateTransactionList = response.data.listObject;
                        if($scope.templateTransactionList != null && $scope.templateTransactionList.length > 0){
                            $scope.ckValue = $scope.templateTransactionList[0].templateObject;
                            $scope.ckObj.templateTransactionId = $scope.templateTransactionList[0].templateTransactionId;
                              $scope.addNote();
                        }else{
                            growl.warning("Please view and submit template first and then add ",
                                 {
                                     title : 'warning!'
                                 });
                        }
    				 
    				   	$rootScope.stopLoader();
    			      }
    			   else if(response.data.status=='error'){
    				   $rootScope.stopLoader(); 
    					growl.warning("This note is already added ",
    							{
    								title : 'warning!'
    							});
    			   }
    		});
    	}
	   	
	   	
	   	
        $scope.printTemp=function(info){
        	  
            var data = {
                    "appName" : "HIS",
                    "appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
                    "reportDesignFilePath" : "CoversheetTemplate.rpt",
                    "reportFormat" : "PDF",
                    "exportFileName" : "Template",
                    "reportParams" : [ {
                        "paramName" : "template_transaction_id",
                        "paramType" : "Integer",
                        "paramValue" : info.templateTransactionId
                    } ]
                };
                reportService.generate(REPORT_API_URI1,
                        data);
    }
       
		$scope.selectNote = function(){
			
			$scope.ckValue = $scope.ckObj.template;
			$scope.ckValuetitle = $scope.ckObj.title;
			
		}
		$scope.addeditsNote = function (){
			
			console.log( $scope.noteList);
			$scope.prodedures.oTCompletionNoteList[$scope.indx].template = $scope.ckValueShow;
			
			$scope.ckValueShow = "";
			$scope.editObj = {};
		}
		
		$scope.ckValueShow = "";
		$scope.editObj = {};
		$scope.indx="";
		$scope.showNote = function(info,indx){
			
			$scope.ckValueShow = info.template;
			$scope.editObj = info;
			
			$scope.indx = indx;
		}
		
		
		$scope.isNoteExist = false;
		$scope.noteAdded = false;
		$scope.addNote = function(){
			$scope.noteAdded = false;
			if($scope.ckObj.noteMasterId == undefined || $scope.ckObj.noteMasterId == ""){
				
				growl.warning("Please select note",
						{
							title : 'warning!'
						});
				
			}else{
				
				$scope.ckflag = false;
				angular.forEach($scope.prodedures.oTCompletionNoteList,function(value,key){
					
					if(value.noteMasterId == $scope.ckObj.noteMasterId && value.status == 'A'){
						$scope.ckflag = true;
					}else if(value.noteMasterId == $scope.ckObj.noteMasterId && value.status == 'I'){
						value.status = 'A'
						$scope.noteAdded = true;
					}
					
				});
				if(!$scope.noteAdded){
				if($scope.ckflag == false){
				var newdata ={}
					newdata.description =$scope.ckObj.description;
					newdata.template = $scope.ckValue;
					newdata.noteMasterId = $scope.ckObj.noteMasterId;
					newdata.noteTypeId = $scope.ckObj.noteType;
					newdata.templateFormatId = $scope.ckObj.templateFormatId;
					newdata.templateTypeId = $scope.ckObj.templateTypeId;
					newdata.templateId = $scope.ckObj.templateId;
					newdata.organizationId=self.model.organizationId;
					newdata.unitId=self.model.unitId;
					newdata.createdBy=self.model.userId;
					newdata.updatedBy=self.model.userId;
					newdata.status='A';
					newdata.createdDate=$scope.dt;
					newdata.updatedDate=$scope.dt;
					newdata.templateTransactionId = $scope.ckObj.templateTransactionId
					$scope.prodedures.oTCompletionNoteList.push(newdata);
					$scope.ckObj = {};
					$scope.ckObj = null;
					$scope.ckObjtest = {};
					$scope.ckValue ="";
					 if(!$scope.$$phase) {
				            $scope.$apply();
				        }
				}
				else{
					growl.warning("This note is already added ",
							{
								title : 'warning!'
							});
					
				}
				}
				$scope.ckObj = {};
				$scope.ckObjtest = {};
				$scope.ckValue ="";
				console.log( "$scope.noteList",$scope.noteList);
			}
		}
		
		$scope.removeNote = function(idex){
			
			if($scope.prodedures.oTCompletionNoteList[idex].otCompletionNoteId!=null){
				
					$scope.prodedures.oTCompletionNoteList[idex].status="I";
					$scope.removedNotesList.push($scope.prodedures.oTCompletionNoteList[idex]);
					
			}else{
				
					$scope.prodedures.oTCompletionNoteList.splice(idex,1);
			}
			
		}
		/****************************************************************  Note Tab End    *****************************************************************/
		
		
		
		/**************************************************************** CSSD Tab Start    *****************************************************************/
		
		$scope.CssdKitList = [];
		$scope.getCSSDKit = function(searchKeyword){
			$rootScope.startLoader();
			var data = {
					organizationId:self.model.organizationId,
					unitId:self.model.unitId,
					searchKeyword:searchKeyword,
					storeId:$scope.storeId
					};
			
			var URI = BASE_URL + ROOT_URL + "/cssd/searchSterilizedKitByKitName";
			return GenericService.serviceAction("POST", URI, data).then(function(response) {
				$rootScope.stopLoader();
				//$scope.CssdKitList = response.data.listObject;
				//console.log("CssdKitList",$scope.CssdKitList);
				$rootScope.stopLoader();
				return response.data.listObject;
			});
			
		}	
		
		$scope.selectKit = function(item){
			console.log("selected kit",item)

			$rootScope.startLoader();
			var data = {
					organizationId:self.model.organizationId,
					unitId:self.model.unitId,
					kitId:item.kitId,
					storeId:$scope.storeId
					};
			
			var URI = BASE_URL + ROOT_URL + "/cssd/getSterilizedKitBatchList";
			 GenericService.serviceAction("POST", URI, data).then(function(response) {
				$rootScope.stopLoader();
				if(response.data.object.kitCode!=null){
					$rootScope.stopLoader();
					$scope.CssdKitList.push(response.data.object);
					console.log("CssdKitList",$scope.CssdKitList);
					self.model.search = "";
				}else{
					$rootScope.stopLoader();
					growl.warning("Kit Not Available",{
						title : "Warning !"
					});
				}
			});
			
			
		}
		
		
		$scope.getExpiryDate = function(kitTransitionNo ,index){
			
			 var found =  $scope.CssdKitList[index].batchList.find(ar=>ar.kitTransitionNo == parseInt(kitTransitionNo));
              if(found!=undefined){
            	  $scope.CssdKitList[index].kitId = found.kitId;
            	  $scope.CssdKitList[index].kitTransitionNo = found.kitTransitionId;
            	  $scope.CssdKitList[index].organizationId = self.model.organizationId;
            	  $scope.CssdKitList[index].unitId = self.model.unitId;
            	  $scope.CssdKitList[index].status = 'A';
            	  $scope.CssdKitList[index].createdBy =self.model.userId;
            	  $scope.CssdKitList[index].updatedBy =self.model.userId;
            	  $scope.CssdKitList[index].updatedDate = $scope.dt;
            	  $scope.CssdKitList[index].createdDate = $scope.dt;
            	  $scope.CssdKitList[index].storeId = $scope.storeId;
            	  
              }
       
			$scope.CssdKitList[index].kitExpiryDate = $scope.CssdKitList[index].batchList[0].kitExpiryDate;
		}
		
		$scope.removeCssdKit = function(info,idx){
			if($scope.CssdKitList[idx].otCompletionCssdKitId!=null){
				
				$scope.CssdKitList[idx].status="I";
		}else{
			
			$scope.CssdKitList.splice(idx,1);
		}
		
		}
		/**************************************************************** CSSD Tab End    *****************************************************************/
		
		/**************************************************************** OT Details Tab Start    *****************************************************************/
		
		
		$scope.listBedCategory = [];
		$scope.billingBedCategory = "";
		$scope.getBedCategory = function(){
			$scope.listBedCategory = [];
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL+ "/adt/getActiveBillingBedCategoryList/orgId/"+self.model.organizationId+"/unitId/"+self.model.unitId;
			var param = {
				
			}
			BillingGenericService.serviceAction(METHOD_GET, URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				$rootScope.stopLoader();
				if(response.data.status == 'success'){
					if(response.data.listObject.length != 0 )
						$scope.listBedCategory = response.data.listObject;
					console.log("listBedCategory",$scope.listBedCategory);
				}
			});
		}
		$scope.getBedCategory();
		
		$scope.anesthesiaTypes = [];
		$scope.getAnesthesiaTypes = function(){
			$scope.anesthesiaTypes = [];
			var URI = BASE_URL + ROOT_URL + "/api/procedure/anesthesia/types/"+self.model.organizationId+"/"+self.model.unitId;
			BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success")
					$scope.anesthesiaTypes = response.data.listObject;
				console.log("anesthesiaTypes",$scope.anesthesiaTypes);
			});
		}
		
		$scope.getAnesthesiaTypes();
		
		
		$scope.otTable = [];
		$scope.getotTable = function(){
			$scope.otTable = [];
			
			var data = {
					orgId:self.model.organizationId,
					unitId:self.model.unitId,
					};
			
			var URI = BASE_URL + ROOT_URL + "/api/otims/getActiveOperationTableList";
			BillingGenericService.serviceAction("POST", URI, data).then(function(response) {
				if (response.data.status == "success")
					$scope.otTable = response.data.listObject;
				console.log("otTable",$scope.otTable);
			});
		}
		
		$scope.getotTable();
		$scope.enActiveBillOfMaterialList = [];
		$scope.changeRateByBillingBedCat = function(billingBedCategoryId)
		{
			if($scope.patientId == "" ||$scope.patientId == undefined || $scope.patientId==null){
				
				 growl.warning("Please select patient..!!",
	                        {
	                            title : 'Warning!'
	                        });
				 	$scope.saveFlag = true;
	                return false;
			}
			$rootScope.startLoader();
			self.model.order.billingBedCategoryId = billingBedCategoryId;
			if(self.model.procedureDetails.length>0){
				angular.forEach(self.model.procedureDetails,function(value,index){
					value.billingBedCategoryId = billingBedCategoryId;
					value.tariffId = self.model.tariffId;
				});
				
				var URI = BASE_URL + ROOT_URL + "/api/procedure/getProcedureServiceRate";
				BillingGenericService.serviceAction(METHOD_POST, URI, self.model.procedureDetails, NOTIFICATION_MSG_STATUS_FALSE).then(function (response) {
					if(response.data.status=="success"){
					if(response.data.listObject.length>0){
				/*		angular.forEach(self.model.procedureDetails,function(value1,index1){
							angular.forEach(response.data.listObject,function(value,index){
								if(value1.procedureOrderId == value.procedureOrderId){
								//	value1.amount = value.amount;
								//	value1.rate = value.amount;
								}
							});
						});*/
					}
					
					}
				});	
				$rootScope.stopLoader();
			}
				
				var newList = [];
				if(self.model.procedureDetails.length>0){
					angular.forEach(self.model.procedureDetails,function(value,index){
						var newArr = {};
						newArr.billingBedCatId = billingBedCategoryId;
						newArr.procedureId = value.serviceId;
						newList.push(newArr);
					});
				
					var URI = BASE_URL + ROOT_URL + "/api/procedure/BillOfMaterial/getBillOfMaterialServiceAmount"; 
					BillingGenericService.serviceAction(METHOD_POST,URI,newList,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						$rootScope.stopLoader();
						if(response.data.listObject != null && response.data.listObject.length>0){
							
							if($scope.prodedures.billOfresList != null && $scope.prodedures.billOfresList.length>0){
								
								var tempBillList = [];
								angular.forEach($scope.prodedures.billOfresList,function(value1,index1){
									
									var compareflag = true;
									
									angular.forEach(response.data.listObject,function(value,index){
										
										if(value1.serviceId == value.serviceId && value1.procedureId == value.procedureId &&  value1.isEditable != true ){
												value1.amount = value.amount;
												compareflag = false;
												tempBillList.push(value1);
										}
										
										if(value1.serviceId == value.serviceId && value1.procedureId == value.procedureId && value1.isEditable == true){
												compareflag = false;
												tempBillList.push(value1);
										}
										
									});
									
									if(compareflag){
										
										if(value1.isEditable == false ){
											value1.status = 'I';
											$scope.enActiveBillOfMaterialList.push(value1);
										}
									}
									
								});
								
								$scope.prodedures.billOfresList = tempBillList;
								
								var tempRemoveList = [];
								var indexList = [];
								
								angular.forEach(response.data.listObject,function(value,index){
									
									var remainingCompareFlag = false;
									
									angular.forEach($scope.prodedures.billOfresList,function(value1,index1){	
										
										if(value1.serviceId == value.serviceId && value1.procedureId == value.procedureId && ( value.isEditable == null || value.isEditable == false) ){
											remainingCompareFlag = true;
										}
										
									});
									
									angular.forEach($scope.enActiveBillOfMaterialList,function(value2,index2){		
										
										if(value2.serviceId == value.serviceId && value2.procedureId == value.procedureId && ( value2.isEditable == null || value2.isEditable == false) ){
											
											value2.status = 'A';
											tempRemoveList.push(value2);
											remainingCompareFlag = true;
											var newarr = {};
											newarr.indexVal = index2;
											indexList.push(newarr);
										}
									});
									
									if(remainingCompareFlag == false){
										
										$scope.prodedures.billOfresList.push(value);
									}
									
								});
								$scope.prodedures.billOfresList = $scope.prodedures.billOfresList.concat(tempRemoveList);
								
								angular.forEach(indexList,function(value,index){	
									
									$scope.enActiveBillOfMaterialList.splice(value.indexVal,1);	
									
								});
							}else{
								
								$scope.prodedures.billOfresList = response.data.listObject;
							}
						
					}else{
						
						var tempBillList = [];
						angular.forEach($scope.prodedures.billOfresList,function(value,index){
							
								if(value.isEditable == true){
									 tempBillList.push(value);
								}else{
									if(value.isEditable == false ){
										value.status = 'I';
										$scope.enActiveBillOfMaterialList.push(value);
									}
								}
							});
						
						$scope.prodedures.billOfresList = tempBillList;
						
					}
					});
					
					//calculate procedure and bill of material charges using subsequentlist  self.model.procedureDetails
					$rootScope.startLoader();
					$timeout(function() {
						$rootScope.startLoader();
						self.model.procedureDetails.forEach(function (value, index)
								{
									//value.originalAmount = value.amount != null ?  value.amount  : value.rate ;
									
									//if(value.originalAmount == 0){
										$scope.prodedures.billOfresList.forEach(function (billvalue, billIndex){
											
											if(value.serviceId == billvalue.procedureId && billvalue.billOfMaterialId!=null){
												
												value.originalAmount = value.originalAmount + billvalue.originalAmount ;
											}
											
										});
									//}
									
									if(value.subSequentDetailsList != null && value.subSequentDetailsList.length>0){
										
										var subSeqObj =  value.subSequentDetailsList.find(obj => {return obj.sequenceId == value.procedureSequence})
										
										if(subSeqObj != undefined){
											
											if(subSeqObj.discPercentage !=null && subSeqObj.discPercentage != 0){
												
												value.amount = (value.originalAmount * subSeqObj.discPercentage)/100;
												value.rate  = value.amount;
												
												$scope.prodedures.billOfresList.forEach(function (billvalue, billIndex){
													
													//billvalue.totalAmount = billvalue.amount;
													
													if(value.serviceId == billvalue.procedureId && billvalue.billOfMaterialId!=null){
														
														billvalue.amount = (billvalue.originalAmount * subSeqObj.discPercentage)/100;
													}
													
												});
												
											}else{
												var amt = 0.0 ;
												$scope.prodedures.billOfresList.forEach(function (billvalue, billIndex){
													
												//	billvalue.totalAmount = billvalue.amount;
													
													if(value.serviceId == billvalue.procedureId && billvalue.billOfMaterialId!=null){
														
														var infoObj = subSeqObj.subSequentDetailsInfoList.find(obj => {return obj.serviceId == billvalue.serviceId});
														
														if(infoObj!=undefined){
															
															if(infoObj.discPercentage!=null && infoObj.discPercentage!=0){
																amt = amt + (billvalue.originalAmount * infoObj.discPercentage)/100;
																billvalue.amount = (billvalue.originalAmount * infoObj.discPercentage)/100;
															}
														}
														
													}
													
												});
												
												value.amount = amt;
												value.rate  = amt; 
											}
										}
										
									}
							
									
									 var searchObja = {
											 /*
											    orgId: self.model.organizationId,
												unitId: self.model.unitId,
												bedCategoryId:billingBedCategoryId,
												serviceId:value.serviceId*/
												
												 orgId: self.model.organizationId,
													unitId: self.model.unitId,
													bedCategoryId:billingBedCategoryId,
													serviceId:value.serviceId,
													tariffId: self.model.tariffId,
													paymentEntitlementId: self.model.order.paymentEntitlementId,
													visitTypeId: self.model.order.visitTypeId,
													patientCategoryId: self.model.order.patientTypeId,
											};
									 
										var URI = BASE_URL + ROOT_URL + "/api/otims/getSurgeryResourceConfigListByProcedureId";
										 BillingGenericService.serviceAction(METHOD_POST, URI, searchObja, NOTIFICATION_MSG_STATUS_FALSE).then(function (response) {
											if (response.data.status == "success"){
												var proRate = 0;
												$scope.getSurgeryResourceConfigListByProcedureId = [];
												$scope.getSurgeryResourceConfigListByProcedureId = response.data.listObject;
												$scope.getSurgeryResourceConfigList = [];
												if($scope.getSurgeryResourceConfigListByProcedureId.length > 0 ){
													
												$scope.rateCalculationResourceService = 0;
												// depend on logic calculate amount
												console.log("bill oflit",$scope.prodedures.billOfresList);
												angular.forEach(self.model.procedureDetails,function(value,index){
														angular.forEach($scope.getSurgeryResourceConfigListByProcedureId,function(value1,index1){
															if(value1.serviceId == value.serviceId){
																angular.forEach(value1.listSelectedResourceBillOfServicesMapperDto,function(value2,index2){
																   angular.forEach( $scope.prodedures.billOfresList,function(value3,index3){
																		if($scope.rateCalculationResourceService == 0){
																			if(parseInt(value2.dependentOn) == value3.serviceId){
																				value2.valuePercentage =  (value3.amount * (value2.dependentOnPercentage / 100));
																				proRate += value2.valuePercentage;
																				$scope.rateCalculationResourceService = value2.valuePercentage;
																			}
																		}else{
																			value2.valuePercentage = ($scope.rateCalculationResourceService * (value2.dependentOnPercentage / 100));
																			proRate += value2.valuePercentage;
																			$scope.rateCalculationResourceService = value2.valuePercentage;
																		}
																	});
															   });
															  }
													     	});
														  });
												// depend on logic calculate amount
												angular.forEach(self.model.procedureDetails,function(value,index){
													angular.forEach($scope.getSurgeryResourceConfigListByProcedureId,function(value1,index1){
														if(value1.serviceId == value.serviceId){
															angular.forEach(value1.listSelectedResourceBillOfServicesMapperDto,function(value2,index2){
															   angular.forEach( $scope.prodedures.billOfresList,function(value3,index3){
																if(value2.componenetServicesId == value3.serviceId){
																	var value3amount = 0;
																	var value3amount = value2.valuePercentage + value3.amount;
																	value3.amount = value3amount;
																}
															});
														   });
														  }
												     	});
													  });
												
												
												//extra services 
											angular.forEach(self.model.procedureDetails,function(value,index){
												angular.forEach($scope.getSurgeryResourceConfigListByProcedureId,function(value1,index1){
													if(value1.serviceId == value.serviceId){
														angular.forEach(value1.listSelectedResourceBillOfServicesMapperDto,function(value2,index2){
															if(value2.splice != true){
															    var newArr = {};
															    newArr.specilityList =  self.model.doctorSpecialityList;
															    newArr.specilityId =  value.specialityId;
															    newArr.procedureId =  value.serviceId;
															    newArr.tariffId =  value1.tariffId;
															    newArr.visitTypeId =  value1.visitTypeId;
															    newArr.serviceId =  value2.componenetServicesId;
															    newArr.organizationId =  self.model.organizationId;
															    newArr.unitId =  self.model.unitId;
															    newArr.billingBedCatId =  value1.bedCategoryId;
															    newArr.isRefDoctorShare = "N";
															    newArr.serviceName =    value2.componenetServicesDesc;
															    newArr.doctorList = [];
															    newArr.isEditable = false;
															    newArr.isGrade = false;
																newArr.amount = value2.valuePercentage;
																newArr.originalAmount = value2.valuePercentage;
																newArr.patientCategoryId =  value1.patientCategoryId;
																newArr.paymentEntitlementId =  value1.paymentEntitlementId;
																$scope.getSurgeryResourceConfigList.push(newArr);
															}
														 });
													}
												  });
												});
												
													
												if($scope.getSurgeryResourceConfigList.length>0){
												$scope.prodedures.billOfresList =  $scope.prodedures.billOfresList.concat($scope.getSurgeryResourceConfigList);
												}
												console.log("bill last resource",$scope.prodedures.billOfresList);
												var proAmount = 0;
												angular.forEach(self.model.procedureDetails,function(value,index){
															   angular.forEach( $scope.prodedures.billOfresList,function(value3,index3){
																   if(value3.procedureId == value.serviceId){
																	proAmount += value3.amount;
																	value.rate = proAmount;
																   }
															});
													  });
												}else{
													var proAmount = 0;
													angular.forEach(self.model.procedureDetails,function(value,index){
																   angular.forEach( $scope.prodedures.billOfresList,function(value3,index3){
																	   if(value3.procedureId == value.serviceId){
																		proAmount += value3.amount;
																		value.rate = proAmount;
																	   }
																});
														  });
												}
											}
										});
										 
								});	
		
						$rootScope.stopLoader();
						
					}, 8000);
					
				}
				console.log("bill last resource",$scope.prodedures.billOfresList);
		
			$rootScope.stopLoader();
		}
		
		/**************************************************************** OT Details Tab End    *****************************************************************/
		
		/**************************************************************** Medical Gas Tab Start    *****************************************************************/
		$scope.finalSum = "";
		$scope.finalToDate ="";
		$scope.calTim = function(fromTime ,duration){
			$scope.finalSum = "";
			var num = duration; //$scope.common.totalDuration;
			var hours = (num / 60);
			var rhours = Math.floor(hours);
			var minutes = (hours - rhours) * 60;
			var rminutes = Math.round(minutes);
			var sOtTime =rhours+":" + rminutes;
			console.log("___________",rhours+":" + rminutes);
			
			var toTime = fromTime ;//$scope.common.preferedTime;
			//var selectedToTime = ;
			 var selectedToTimeRes = toTime.split(":");
			
			 var selectedSwitchTimeRes = sOtTime.split(":");
			
			 var sumofHrs = parseInt(selectedToTimeRes[0]) + parseInt(selectedSwitchTimeRes[0]);
			 var sumOfMin =  parseInt(selectedToTimeRes[1]) + parseInt(selectedSwitchTimeRes[1]);
			 $scope.finalSum;
			
			 if(sumOfMin>=60){
				 
				 var newsumofHrs = sumofHrs+":"+"00";
				 
				 var num = sumOfMin ;
					var hours = (num / 60);
					var rhours = Math.floor(hours);
					var minutes = (hours - rhours) * 60;
					var rminutes = Math.round(minutes);
					var sOtTime1 =rhours+":" + rminutes;
					
					 var selectedToTimeRes1 = newsumofHrs.split(":");
						
					 var selectedSwitchTimeRes1 = sOtTime1.split(":");
					
					 var sumofHrs1 = parseInt(selectedToTimeRes1[0]) + parseInt(selectedSwitchTimeRes1[0]);
					 var sumOfMin1 =  parseInt(selectedToTimeRes1[1]) + parseInt(selectedSwitchTimeRes1[1]);
					
					 $scope.finalSum = (sumofHrs1<10?"0"+sumofHrs1:sumofHrs1)+":"+(sumOfMin1<10?"0"+sumOfMin1:sumOfMin1);
			 }else{
				 
				 $scope.finalSum = (sumofHrs<10?"0"+sumofHrs:sumofHrs)+":"+(sumOfMin<10?"0"+sumOfMin:sumOfMin);
				 
			 }
			 
			 console.log("finalSum",$scope.finalSum);
			 var finalTestTime = $scope.finalSum.split(":");
			 if(finalTestTime[0] >= 24){
				 var testHr = finalTestTime[0] -24 ; 
				 if(testHr <= 9){
					 testHr = "0"+testHr;
				 }
				 testHr = testHr + ":" +finalTestTime[1];
				 $scope.finalSum =  testHr ;
				self.model.periodicityServ.toDate =  moment().add(1,'days').toDate();
				$scope.finalToDate = moment().add(1,'days').toDate();
			 }else{
				 self.model.periodicityServ.toDate = new Date();
			 }
			 $scope.finalSum =  $scope.finalSum +":"+"00";
		}
		
		if($scope.showEmergencyflag){
		$scope.calTim($scope.common.preferTime,$scope.common.totalDuration) ;
		self.model.periodicityServ.fromTime =  $scope.common.preferTime;
		self.model.periodicityServ.toTime =  $scope.finalSum;
		//self.model.periodicityServ.toDate = moment($scope.common.scheduleDate).toDate();
		self.model.periodicityServ.fromDate = moment($scope.common.scheduleDate).toDate();
		}
		
		$scope.getItemList = function(assetsCatId,prodCatId){
			var assetsCatIds = [];
			var prodCatIds = [];
			assetsCatIds.push(assetsCatId);
			prodCatIds.push(prodCatId);
		var data = {
				assetCategoryList : assetsCatIds,
				productCategoryList : prodCatIds,
				orgId:self.model.organizationId,
				unitId : self.model.unitId
		}
		$rootScope.startLoader();
			var URI = BASE_URL+ ROOT_URL+ "/unit/CompanyContract/getItemListByAssetAndProductCategory";
		GenericService.serviceAction("POST", URI, data)
				.then(function(response) {
					$scope.searchItemList = [];
							if (response.data.status == "success") {
								$scope.searchItemList = response.data.listObject;
							}
							$rootScope.stopLoader();
						});
		}
		
		$scope.searchPServices = function(serviceName)
		{
			var data={
					"visitTypeId" : $scope.patientVisitTypeId,
					"unitIdId" : self.model.unitId,
					"serviceName" : serviceName
			};
			
			var URI = BASE_URL + ROOT_URL + "/api/global/search/services/periodicity";
				return BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					return response.data.listObject;
				});
			}
		    
		  $scope.selectPService = function(item, model, label){
		    	
		    	//check service config available or not
		    	var URI = BASE_URL + ROOT_URL + "/api/periodicity/config/avail/"+item.serviceId+"/"+self.model.organizationId+"/"+self.model.unitId+"/"+$scope.patientVisitTypeId;
		    	$rootScope.startLoader();
		    	BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.object != null){
						self.model.periodicityServ.serviceName = item.serviceName;
				    	self.model.periodicityServ.serviceId = item.serviceId;
						self.model.periodicityServ.toTime =  $scope.finalSum;
						self.model.periodicityServ.fromDate = moment($scope.common.scheduleDate).toDate();
				    	self.model.periodicityServ.perServConfig = response.data.object;
				    	if(response.data.object.assetCategoryId > 0 && response.data.object.productCategoryId > 0){
				    	$scope.getItemList(response.data.object.assetCategoryId,response.data.object.productCategoryId);
				    	}
				    	
					}else{
						growl.error("Configuration for service not available please add",{
							title : 'Warning!'
						});
						self.model.periodicityServ.serviceName = "";
				    	self.model.periodicityServ.serviceId = "";
					}
					$rootScope.stopLoader();
				});
			}
		    
		    var initClockPicker = function() {
				$('.clockpicker').clockpicker();
			}
		    
		    $scope.addPeriodicityServOrder = function(periodicityServ){
		    	if(periodicityServ.serviceId == '' || periodicityServ.serviceId == null)
	    		{
		    		growl.error("Please select service",{
						title : 'Warning!'
					});
		    		return false;
	    		}
		    	else if(periodicityServ.fromDate == undefined || periodicityServ.fromDate == null || periodicityServ.fromDate == '')
	    		{
		    		growl.error("Please select from time",{
						title : 'Warning!'
					});
		    		return false;
	    		}
		    	else if(periodicityServ.toDate != null && periodicityServ.toTime == null)
		    	{
		    		growl.error("Please select to time",{
						title : 'Warning!'
					});
		    		return false;
		    	}
		    	else{
		    		periodicityServ.otCompletionMedicalGasId = null;
		    		periodicityServ.procedureOrderId = null;
		    		periodicityServ.status='A';	
		    		periodicityServ.orgId = self.model.organizationId;
		    		periodicityServ.unitId = self.model.unitId;
		    		periodicityServ.createdBy = self.model.userId;
		    		periodicityServ.createdDate = $scope.dt;
		    		periodicityServ.updatedBy = self.model.userId;
		    		periodicityServ.updatedDate = $scope.dt;
		    		periodicityServ.proReqId = $scope.proReqId;
		    		periodicityServ.fromDateString =  moment(periodicityServ.fromDate).format('DD-MM-YYYY');
		    		periodicityServ.toDateString =  moment(periodicityServ.toDate).format('DD-MM-YYYY');
		    		periodicityServ.fromDate = moment(periodicityServ.fromDate).format('DD-MM-YYYY HH:mm:ss');
		    		periodicityServ.toDate = moment(periodicityServ.toDate).format('DD-MM-YYYY HH:mm:ss');
		    		periodicityServ.multidelete("perServConfig");
		    		var newArr = angular.copy(periodicityServ);
			    	
			    self.model.periodicityServOrdersList.push(newArr);
		    	self.model.periodicityServ.serviceName = null;
		    	self.model.periodicityServ.serviceId = null;
		    	self.model.periodicityServ.fromDate = moment($scope.common.scheduleDate).toDate();
		    	self.model.periodicityServ.fromTime =  $scope.common.preferTime;
				self.model.periodicityServ.toTime =  $scope.finalSum;
				self.model.periodicityServ.toDate = moment($scope.common.scheduleDate).toDate();
		    	}
		    }
		    
		    $scope.removePeriodicityServOrder = function(index){ 
		    	
		    	if(self.model.periodicityServOrdersList[index].otCompletionMedicalGasId!=null){
		    		self.model.periodicityServOrdersList[index].status='I';
		    	}else{
		    	 self.model.periodicityServOrdersList.splice(index,1);
		    	}
		    }
		
		
		
		/**************************************************************** Medical Gas Tab End    *****************************************************************/
		
		/**************************************************************** Equipment Tab START    *****************************************************************/
		   $scope.equipmentObj = {
				   equipmentTypeObj : {},
				   equipmentNameObj : {},
				   fromTime : "",
				   toTime : "",
				   fromDate : "",
				   toDate: ""
		   }; 
		   
		   $scope.equipmentTypeList = [];
			$scope.getEquipmentTypeList = function () {
				var URI = BASE_URL + ROOT_URL + "/api/global/assets/categories/2/"+$scope.unitId+"/"+$scope.organizationId; //2 for Fixed asset type
				BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					$scope.equipmentTypeList = response.data;
					console.log("$scope.equipmentTypeList",$scope.equipmentTypeList);
					
				});
			   }
			
			$scope.getEquipmentTypeList();
			
			
			$scope.equipmentNameList = [];
			$scope.equipmentBasicNameList = [];
			
			$scope.getEquipmentNamesByEquipmentId = function (obj) {
				$rootScope.startLoader();
				var data = {
						unitId:self.model.unitId,
						assetCategoryId:obj.id
				};
				var URI = BASE_URL + ROOT_URL + "/api/global/items/otims/getItemAndBatchList"; 
				BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					$scope.equipmentBasicNameList = response.data;
				
						angular.forEach($scope.equipmentBasicNameList,
								function(value,index) {
									var data = {};
									data.id = value.batchId;
									data.itemName = value.itemName;
									data.itemId = value.itemId;
									$scope.equipmentNameList.push(data);
								});
					$rootScope.stopLoader();	
				});
				
			   }
		    
			$scope.getEquipmentRate = function(procedureOrderDto){
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + "/api/procedure/getEquipmentRate/fromContract"; 
				BillingGenericService.serviceAction(METHOD_POST,URI,procedureOrderDto,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					$scope.equipmentDetailsList[$scope.equipmentDetailsList.length-1].amount = response.data.object.amount;
					$scope.equipmentDetailsList[$scope.equipmentDetailsList.length-1].serviceId = response.data.object.serviceId;
					$rootScope.stopLoader();	
				});
			}
			
			
			
			$scope.addEquipmentDetails = function(){
				
				try{
					$rootScope.startLoader();
			
					$scope.isEquipmentExist = false;
					
					if($scope.equipmentDetailsList.length>0){
						angular.forEach($scope.equipmentDetailsList,function(value,index){
							if(value.batchId == $scope.equipmentObj.equipmentNameObj.id){
								$scope.isEquipmentExist = true;
							}
						});
					}
					
				/*	if($scope.equipmentBatchList.length>0){
					angular.forEach($scope.equipmentBatchList,function(value,index){
						if(value.selectedItemBatchObj!=undefined){
						if(value.selectedItemBatchObj.batchId == $scope.equipmentNameObj.id){
							$scope.isEquipmentExist = true;
						}
						}
					});
					}
				*/	if($scope.equipmentObj.equipmentTypeObj.id==""){
						$rootScope.stopLoader();
						growl.warning("Please select Equipment Type",
								{
									title : 'warning!'
								});
						return false;
					}else if($scope.equipmentObj.equipmentNameObj.id==""){
						$rootScope.stopLoader();
						growl.warning("Please Select Equipment Name",
								{
									title : 'warning!'
								});
						return false;
					}else if($scope.equipmentObj.fromTime==""){
						$rootScope.stopLoader();
						growl.warning("Please Select From Time",
								{
									title : 'warning!'
								});
						return false;
					}else if($scope.equipmentObj.toTime==""){
						$rootScope.stopLoader();
						growl.warning("Please Select toTime",
								{
									title : 'warning!'
								});
						return false;
					}else if($scope.equipmentObj.fromDate==""){
						$rootScope.stopLoader();
						growl.warning("Please Select From Date",
								{
									title : 'warning!'
								});
						return false;
					}else if($scope.equipmentObj.toDate==""){
						$rootScope.stopLoader();
						growl.warning("Please Select To Date",
								{
									title : 'warning!'
								});
						return false;
					}else{
						if($scope.isEquipmentExist){
							growl.warning("Equipment Already Exist",
									{
										title : 'warning!'
									});
							$rootScope.stopLoader();
							return false;
							
						}else{
						
							  if($scope.equipmentObj.fromTime >= $scope.equipmentObj.toTime){
									$rootScope.stopLoader();
									growl.warning("From time Shouble be less  than toTime",
											{
												title : 'warning!'
											});
									return false;
								}
									
					var newArr = {};
					newArr.otCompletionEquipmentId = null;
					newArr.equipmentTypeId = $scope.equipmentObj.equipmentTypeObj.id;
					newArr.equipmentTypeName = $scope.equipmentObj.equipmentTypeObj.category;
					newArr.itemId = $scope.equipmentObj.equipmentNameObj.itemId;
					newArr.equipmentName =$scope.equipmentObj.equipmentNameObj.itemName;
					newArr.batchId =$scope.equipmentObj.equipmentNameObj.id;
					newArr.fromTime = $scope.equipmentObj.fromTime;
					newArr.toTime = $scope.equipmentObj.toTime;
					newArr.fromDate = moment($scope.equipmentObj.fromDate).format('DD-MM-YYYY');
					newArr.toDate = ($scope.finalToDate == "") ? moment($scope.equipmentObj.toDate).format('DD-MM-YYYY') :moment($scope.finalToDate).format('DD-MM-YYYY'); 
					newArr.amount= 0;
					newArr.serviceId= 0;
					newArr.status="A";
					newArr.organizationId = self.model.organizationId;
					newArr.unitId = self.model.unitId;
					newArr.createdBy = self.model.userId;
					newArr.updatedBy = self.model.userId;
					newArr.createdDate = $scope.dt;
					newArr.updatedDate = $scope.dt;
					if(self.model.currentProcedures !=undefined && self.model.currentProcedures != null && self.model.currentProcedures.length == 1){
						newArr.procedureOrderId  = self.model.currentProcedures[0].procedureId;
					}else{
						newArr.procedureOrderId = 0;
					}
				
					newArr.isEditable = 1;
					$scope.equipmentDetailsList.push(newArr);
					
					var time = moment.utc(moment($scope.equipmentObj.toTime, "HH:mm:ss").diff(moment($scope.equipmentObj.fromTime, "HH:mm:ss"))).format("HH:mm");
					var tm = time.split(":");
					
					$scope.duration = (tm[0]*60);
					$scope.duration = $scope.duration  + parseInt(tm[1]);
					
					var data = {};
					data.admissionId = self.model.order.admissionId;
					data.equipmentTypeId = $scope.equipmentObj.equipmentTypeObj.id;
					data.orgId = self.model.organizationId;
					data.unitId = self.model.unitId;
					data.preferedDateTime = $scope.preferedDateTime !=undefined ? $scope.preferedDateTime : $scope.dt;
					data.fromTime = $scope.equipmentObj.fromTime;
					data.toTime = $scope.equipmentObj.toTime;
					data.visitTypeId = self.model.order.visitTypeId;
					data.patientTypeId = $scope.patientTypeId;
					data.billingBedCategoryId = $scope.otDetalsObj.billingBedCategoryId;
					data.payeeId = self.model.selfPayeeId;
					data.tariffId = self.model.tariffId;
					data.duration = $scope.duration;
					data.encounterId = self.model.order.encounterId;
					data.contractId = self.model.order.contractId;
					data.equipmentId = $scope.equipmentObj.equipmentNameObj.itemId;
					$scope.getEquipmentRate(data);
					
					$scope.equipmentObj.equipmentTypeObj.id = "";
					$scope.equipmentObj.equipmentTypeObj.category = "";
					$scope.equipmentObj.equipmentNameObj.id = "";
					$scope.equipmentObj.equipmentNameObj.name = "";
					$scope.equipmentNameList = [];
					
						}
					}
			
					$scope.equipmentObj = {};
					$scope.equipmentNameList = [];
					$rootScope.stopLoader();
				}catch(e){
					$rootScope.stopLoader();
					console.log("Exception",e);
				}
			}
			
			$scope.removeEquipmentDetails = function(index){
				
				if($scope.equipmentDetailsList[index].otCompletionEquipmentId!=null){
					$scope.equipmentDetailsList[index].status="I";
					$scope.equipemtnRemovedList.push($scope.equipmentDetailsList[index]);
				}else{
					$scope.equipmentDetailsList.splice(index,1);	
				}
				
			}
			
			//added by chetana for Grade Search
			$scope.getGradeList = function() {
				var data = {
//						orgnisationId : $scope.organizationId,
//						unitId :$scope.unitId,
						};
				
				var URI = BASE_URL + ROOT_URL + "/api/global/getActiveSurgeryGradeList";
				console.log("URI", URI);
				$scope.gradeLis=[];
				GenericService.serviceAction("GET", URI).then(function(response) {
					$scope.gradeList = response.data.listObject;
					console.log("gradeList",$scope.gradeList);
				});
			}
			$scope.getGradeList();
			$scope.tempList = $scope.prodedures.billOfresList;
			$scope.changeGradeWiseProcedureRate=function(procedure, index){
				$scope.idx=index;
				$scope.procedureId=procedure.serviceId;
				$scope.prodedures.billOfresList=[];
				 $scope.list = [];
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL+ "/api/procedure/BillOfMaterial/getBillOfMaterialListByProcedureId"
				var param = {
						unitId : $scope.unitId,
						billingBedCatId:procedure.billingBedCategoryId,
						organizationId : $scope.organizationId,
						tariffId: self.model.tariffId,
						paymentEntitlementId: self.model.order.paymentEntitlementId,
						visitTypeId: self.model.order.visitTypeId,
						patientCategoryId: self.model.order.patientTypeId,
						isGrade : true
//						procedureId : procedureId
				};
				
				if(procedure.surgeryGradeId != null && procedure.surgeryGradeId != "0"){
					param.surgeryGradeId = procedure.surgeryGradeId;

				}else{
					param.procedureId = procedure.serviceId;
					param.isGrade =false;
				}
				BillingGenericService.serviceAction(METHOD_POST, URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					$rootScope.stopLoader();
					if(response.data.status == 'success'){
//						if(procedure.surgeryGradeId != "0" ){
							if(response.data.listObject.length != 0){
								$scope.list = response.data.listObject;
								$scope.totalAmount = 0;
							  angular.forEach($scope.list,function(value1,index1){
												   $scope.totalAmount += value1.amount;
													
													value1.procedureId=procedure.serviceId;
													value1.procedure=procedure.serviceName;
													$scope.prodedures.billOfresList.push(value1);
										  
						  });
							procedure.rate = $scope.totalAmount;
							
							angular.forEach($scope.prodedures.billOfresList,function(value,index){
								value.specilityList =  self.model.doctorSpecialityList;
								value.doctorList = [];
							});
							}else{
								procedure.rate = 0;
								growl.warning("Selected Grade is not mapped with selected Billing Bed Category.")
							}
							 
//						}
						/*else{
							self.model.procedureDetails.splice($scope.idx, 1);
							addProcedureDetails();
						}*/
							
						console.log("$scope.serviceAmtList",$scope.serviceAmtList);
						}
				});
			
			}
	    /**************************************************************** Equipment Tab End    *****************************************************************/
		
			$scope.firstTabLevel =13;
			$scope.secondLevelTab = "";
			$scope.isSetFirstTabLevel = function(tab){
				return $scope.firstTabLevel === tab;
			}
			
			$scope.isSetSecondTabLevel = function(tab){
				return $scope.secondLevelTab === tab;
			}
			
			$scope.setSecondTabLevel = function(tabNumber){
				$scope.secondLevelTab = tabNumber;
				if($scope.secondLevelTab==31){
                    $scope.getNoteType(1);
                }
				if($scope.secondLevelTab == 1){
					self.model.active = 1;
                }
				if($scope.secondLevelTab == 4){
					self.model.active = 4;
                }
			}
			
			$scope.setFirstTabLevel = function(tabNumber){
				$scope.firstTabLevel = tabNumber;
				if(tabNumber == 8){
					$scope.setSecondTabLevel(21);
				}
				if(tabNumber == 4){
					$scope.setSecondTabLevel(1);
				}
				if(tabNumber == 9){
					$scope.setSecondTabLevel(33);
				}
			}
			$scope.setFirstTabLevel(13);
			
			$scope.getOtTableByScheduleId = function(scheduledId){
				$rootScope.startLoader();
				var data = {
						organizationId:$scope.organizationId,
						unitId:$scope.unitId,
						procedureScheduleId:scheduledId
				};
				var URI = BASE_URL + ROOT_URL + "/api/otims/dashboard/getOtTableByScheduleId"; 
				BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if(response.data.listObject.length>0){
						$scope.otDetalsObj.otTableId = response.data.listObject[0].tableId;		
					}
					$rootScope.stopLoader();
				});
			}
			if($scope.common.procedureScheduleId!=undefined){
				$scope.getOtTableByScheduleId($scope.common.procedureScheduleId);
			}
			
			$scope.otTransferStatus=0;
			$scope.getOtTransferDetails = function(admissionId){
				$rootScope.startLoader();
				var data = {
						admissionId:admissionId
				};
				var URI = BASE_URL + ROOT_URL + "/api/otims/OTTransfer/getOTTransferByAdmissionId"; 
				BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if(response.data.object!=null){
						if(response.data.object.otTransferStatus==6){
						$scope.transferRequestId = response.data.object.transferRequestId;
						$scope.isTransferToWard=true;		
						}
						if(response.data.object.otTransferStatus==8){
							$scope.transferRequestId = response.data.object.transferRequestId;
							$scope.otTransferStatus = response.data.object.otTransferStatus;
							$scope.isTransferToWard=true;	
							$scope.otToWardTransfer=true;
						}
					}
					$rootScope.stopLoader();
				});
			}
			
			if($scope.common.admissionId!=undefined && $scope.common.admissionId!=null){
				$scope.getOtTransferDetails($scope.common.admissionId);
			}
			
			$scope.goToBack = function(){
				if($scope.common.renderedFromDashboard!=undefined && $scope.common.renderedFromDashboard!=null){
					if($scope.common.renderedFromDashboard==1){
						$state.go('OTIMSdashboard');
					}else{
						$state.go('OTCompletion');
					}
				}else{
					$state.go('OTCompletion');
				}
			}
			
			$scope.medicationObj = {
					isItemOrKit:'N'
			};
			$scope.isItemOrKit = "N";
			$scope.selectItemOrKit = function(isItemOrKit) {

				$scope.isItemOrKit = isItemOrKit;
			}
			
			
			
			
			
			
			
			
			
			
			$scope.getListByName = function(name) {

				$scope.items = [];
				if ($scope.isItemOrKit == 'Y') {

					var URI = BASE_URL + ROOT_URL + "/api/units/kit/master/search/" + name;

					return GenericService.serviceAction("GET", URI).then(
									function(response) {
										if (!!response.data&& response.data.length > 0)
											$scope.kitDetails = [];
										angular.forEach(response.data,function(value,key) {

															var newArr = {};
															newArr.kitId = value.id;
															newArr.kitName = value.description;
															newArr.items = value.items;
															newArr.isItemOrKit = $scope.isItemOrKit;
															newArr.kitTypeId = value.kitTypeId;
															$scope.kitDetails.push(newArr);
														});
										console.log("kitDetails",$scope.kitDetails);
										return $scope.kitDetails;
									});
				} else {
					var URI = BASE_URL + ROOT_URL + "/api/globals/items/item/search/" + name;

					return GenericService.serviceAction("GET", URI).then(
									function(response) {
										if (!!response.data&& response.data.length > 0)
											console.log("getListByName",response.data);
										return response.data;

									});
				}
			}
			
			$scope.selectMedication = function(info, model,label) {
				var date = new Date();
				$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
			
				if (info.isItemOrKit == 'Y') {
				    var patientTypeId = $scope.patientTypeId !== undefined && $scope.patientTypeId !== null && $scope.patientTypeId !== "" ? $scope.patientTypeId : 0;
					var aId  =(self.model.order.admissionId!=null && self.model.order.admissionId!="")?self.model.order.admissionId:0;
					var eId = (self.model.order.encounterId!=null && self.model.order.encounterId!="")?self.model.order.encounterId:0;
					var URI = BASE_URL + ROOT_URL + "/api/units/kit/master/getKitDetails/"+$scope.storeId+"/"+info.kitId+"/"+info.kitTypeId+"/"+self.model.organizationId+"/"+self.model.unitId
					+"/"+self.model.order.paymentEntitlementId+
					"/"+aId+"/"+eId+"/"+patientTypeId;

					$rootScope.startLoader();
					GenericService.serviceAction("GET", URI, {})
						.then(function (response) {
							if (response.data.status == "success") {
								console.log(response.data.listObject)
								if(response.data.listObject.length>0){
									
									angular.forEach(response.data.listObject,function(value,index){
										
										//added by minesh for calling clubBatches function
										let clubBatchData  = clubBatches(value.batch);
										value.batch = clubBatchData;
										//code end by minesh for calling clubBatches function
										
										value.batch.unshift({
											batchDetails: "Select Batch",
										});
										var item = {};
										item.itemId =  value.itemId,
										item.itemCode = value.itemCode,
										item.itemName = value.itemName,
										item.manufacturerId = value.manufacturerId,
										item.manufacturerName = value.manufacturerName,
										item.productCategoryId = value.productCategoryId,
										item.batch = value.batch,
										item.isLasa = value.isLasa,
										item.selectedBatch = value.batch[0],
										item.taxId = value.taxId,
										item.taxName = value.taxName,
										item.taxPercent = value.taxPercentage,
										item.createdBy =  self.model.userId,
										item.updatedBy = self.model.userId,
										item.orgId =  self.model.organizationId,
										item.unitId =  self.model.unitId,
										item.storeId = $scope.storeId,
										item.usedQty = value.qty
										item.markUpDown = value.markUpDown,
										item.patientClassPercentage = value.patientClassPercentage,
										item.markUpDownPer = value.markUpDownPer,
										item.selectedUom =null;
										self.model.medicationItems.push(item);
										$scope.medicationObj.kitName = "";
								});
							}else{
								 $rootScope.stopLoader();
				                  growl.warning("In this Kit does not have Items. ", {
				                      title: "Warning"
				                  });
							}
							$rootScope.stopLoader();
						
						}
				
				});

				}
			}
			
			$scope.implantObj = {
					isItemOrKit:'N'
			};
			$scope.implantKitDetails = [];
			$scope.getImplantListByName = function(name) {
				$scope.implantItems = [];
				if ($scope.implantObj.isItemOrKit == 'Y') {
					var data = {
						kitName : name,
						organizationId : $scope.organizationId,
						unitId : $scope.unitId
					};
					var URI = BASE_URL + ROOT_URL + IMPLANT_SEARCH_KIT;

					return GenericService.serviceAction(METHOD_POST, URI,data).then(
									function(response) {
										if (response.data.status == 'success')
											$scope.implantKitDetails = [];
										angular.forEach(response.data.listObject,function(value,key) {
															var newArr = {};
															newArr.kitId = value.kitId;
															newArr.kitName = value.kitName;
															newArr.isItemOrKit = $scope.implantObj.isItemOrKit;
															newArr.kitTypeId = value.kitTypeId;
															$scope.implantKitDetails.push(newArr);
														});
										
										return $scope.implantKitDetails;
									});
				} else {

					var URI = BASE_URL + ROOT_URL + IMPLANT_SEARCH_ITEMS;
					var data = {
						itemName : name,
						organizationId : $scope.organizationId,
						unitId : $scope.unitId
					};

					return GenericService
							.serviceAction(METHOD_POST, URI,
									data)
							.then(
									function(response) {
										if (response.data.status == "success")
											return response.data.listObject;
									});
				}
			}
			
			$scope.selectImplant = function(info, model,label) {
			
				if (info.isItemOrKit == 'Y') {
					//$scope.storeId need ot storeid
					var aId  = (self.model.order.admissionId!=null && self.model.order.admissionId!="")?self.model.order.admissionId:0;
					var eId  = (self.model.order.encounterId!=null && self.model.order.encounterId!= "")?self.model.order.encounterId:0;
					
					var URI = BASE_URL + ROOT_URL + "/api/units/kit/master/getKitDetails/"+$scope.storeId+"/"+info.kitId+"/"+info.kitTypeId+"/"+self.model.organizationId+"/"+self.model.unitId
					+"/"+self.model.order.paymentEntitlementId+
					"/"+aId+"/"+eId+"/"+$scope.patientTypeId;

					$rootScope.startLoader();
					GenericService.serviceAction("GET", URI, {})
						.then(function (response) {
							if (response.data.status == "success") {
								console.log(response.data.listObject)
								if(response.data.listObject.length>0){
									angular.forEach(response.data.listObject,function(value,index){
										
										//added by minesh for calling clubBatches function
										let clubBatchData  = clubBatches(value.batch);
										value.batch = clubBatchData;
										//code end by minesh for calling clubBatches function
										
										value.batch.unshift({
											batchDetails: "Select Batch",
										});
										var item = {};
										item.itemId =  value.itemId,
										item.itemCode = value.itemCode,
										item.itemName = value.itemName,
										item.manufacturerId = value.manufacturerId,
										item.manufacturerName = value.manufacturerName,
										item.productCategoryId = value.productCategoryId,
										item.batch = value.batch,
										item.selectedBatch = value.batch[0],
										item.taxId = value.taxId,
										item.taxName = value.taxName,
										item.taxPercent = value.taxPercent,
										item.createdBy =  self.model.userId,
										item.updatedBy = self.model.userId,
										item.orgId =  self.model.organizationId,
										item.unitId =  self.model.unitId,
										item.storeId = $scope.storeId,
										item.usedQty = value.qty
										item.selectedUom = null;
										
										self.model.implantItems.push(item);
										$scope.implantObj.kitName = "";
								});
							}else{
								 $rootScope.stopLoader();
				                  growl.warning("In this Kit does not have Items. ", {
				                      title: "Warning"
				                  });
							}
							$rootScope.stopLoader();
						
						}
				
				});

				}
			}
			
			//get outSource service  Report 
			$scope.patientDocumentList = [];
			 $scope.getPatientDocumentList = function(labSampleDtlsId){
			    $rootScope.startLoader();
			    var data ={
				  "unitId":$scope.unitId,
				  "organisationId":$scope.organizationId,
				  "labSampleDtlsId":labSampleDtlsId
			    }
				var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
				BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				$scope.patientDocumentList = [];
				if (response.data.status == "success") 
				{
					$scope.patientDocumentList = response.data.listObject;				            
				        
				    $rootScope.stopLoader();
				}
				else if(response.data.status == "error")
				{
					$rootScope.stopLoader();
				}
			  });
			}						
					
			$scope.getOutsourceServiceReport = function(obj){
				$('#outSourceServiceModal').modal('show');
				$scope.getPatientDocumentList(obj.labSampleDtlsId);
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
				$timeout(function() {
					$rootScope.stopLoader();
				}, 5000);
            } 
			
			$scope.addItem = function(index,item,type){
				if(type == 1){
					
					var data = {};
					data.itemId =  item.itemId,
					data.itemCode = item.itemCode,
					data.itemName = item.itemName,
					data.manufacturerId = item.manufacturerId,
					data.manufacturerName = item.manufacturerName,
					data.productCategoryId = item.productCategoryId,
					data.batch = item.batch,
					data.selectedBatch = null,
					data.taxId = item.taxId,
					data.taxName = item.taxName,
					data.taxPercent = item.taxPercent,
					data.createdBy =  self.model.userId,
					data.updatedBy = self.model.userId,
					data.orgId =  self.model.organizationId,
					data.unitId =  self.model.unitId,
					data.storeId = $scope.storeId,
					data.usedQty = 0,
					data.markUpDown = item.markUpDown,
					data.patientClassPercentage = item.patientClassPercentage,
					data.markUpDownPer = item.markUpDownPer,
					data.selectedUom =null;
					self.model.medicationItems.push(data);
					
				}else if(type == 2){
					
					var data = {};
					data.itemId =  item.itemId,
					data.itemCode = item.itemCode,
					data.itemName = item.itemName,
					data.manufacturerId = item.manufacturerId,
					data.manufacturerName = item.manufacturerName,
					data.productCategoryId = item.productCategoryId,
					data.batch = item.batch,
					data.selectedBatch = null,
					data.taxId = item.taxId,
					data.taxName = item.taxName,
					data.taxPercent = item.taxPercent,
					data.createdBy =  self.model.userId,
					data.updatedBy = self.model.userId,
					data.orgId =  self.model.organizationId,
					data.unitId =  self.model.unitId,
					data.storeId = $scope.storeId,
					data.usedQty = 0,
					data.selectedUom = null;
					
					self.model.implantItems.push(data);
				}
				
			}
			
		       $scope.validateUniqueBatchAndUom = function (item,type) {
		            var isValid = true;
		            	item['isParentBatchValid'] = false;
		                item['isParentUOMValid'] = false;
		            	var items = {};
		                if(type == 1){
		                	items = self.model.medicationItems.filter(function (detail) {
		               		
		                   return detail.itemId == item.itemId && item.selectedBatch && item.selectedUom
		                   && item.selectedUom.uomTypeId > 0 && item.selectedBatch.batchId > 0 && detail.selectedBatch.batchId
		                   == item.selectedBatch.batchId && detail.selectedUom.uomTypeId == item.selectedUom.uomTypeId;
		               });
		                }else if(type == 2){
		                	items = self.model.implantItems.filter(function (detail) {
				                   return detail.itemId == item.itemId && item.selectedBatch && item.selectedBatch.batchId > 0 
				                   && detail.selectedBatch.batchId  == item.selectedBatch.batchId;
				               });
		                }
		                if (items.length > 1) {
		                    isValid = false;
		                    item['isParentBatchValid'] = true;
		                    item['isParentUOMValid'] = true;
		                    growl.error('There should not be same batch and same UOM for same item');
		                    item.selectedBatch = null;
		                    item.selectedUom = null;
		                }
		            return isValid;
		        }
		       
				//change procedure sequence
				$scope.changeProcedureSequence = function(index){
						var isDuplicatePrimary = false;
						var isDuplicateSecondary = false;
						var isDuplicateTertiary = false;
						var isDuplicateQuaternary = false;
						var isDuplicateQuinary = false;
						
						angular.forEach(self.model.procedureDetails,function(value,idx){
							if(idx!=index && self.model.procedureDetails[index].procedureSequence==1){
								if(value.procedureSequence == 1){
									isDuplicatePrimary = true;
								}
							}else if(idx!=index && self.model.procedureDetails[index].procedureSequence==2){
								if(value.procedureSequence == 2){
									isDuplicateSecondary = true;
								}
							}else if(idx!=index && self.model.procedureDetails[index].procedureSequence==3){
								if(value.procedureSequence == 3){
									isDuplicateTertiary = true;
								}
							}else if(idx!=index && self.model.procedureDetails[index].procedureSequence==4){
								if(value.procedureSequence == 4){
									isDuplicateQuaternary = true;
								}
							}else if(idx!=index && self.model.procedureDetails[index].procedureSequence==5){
								if(value.procedureSequence == 5){
									isDuplicateQuinary = true;
								}
							}
						});
						
						if(isDuplicatePrimary){
							 growl.warning("Primary Procedure Is Already Exist", {
                                   title : 'warning!'
                               });
							 self.model.procedureDetails[index].amount = 0.0;
							 self.model.procedureDetails[index].rate = 0.0;
							 self.model.procedureDetails[index].procedureSequence = $scope.procedureSequenceList[0].id;
							return false;
						}
						
						if(isDuplicateSecondary){
							 growl.warning("Secondary Procedure Is Already Exist", {
                                   title : 'warning!'
                               });
							 self.model.procedureDetails[index].amount = 0.0;
							 self.model.procedureDetails[index].rate = 0.0;
							 self.model.procedureDetails[index].procedureSequence = $scope.procedureSequenceList[0].id;
							return false;
						}
						
						if(isDuplicateTertiary){
							 growl.warning("Tertiary Procedure Is Already Exist", {
                                   title : 'warning!'
                               });
							 self.model.procedureDetails[index].amount = 0.0;
							 self.model.procedureDetails[index].rate = 0.0;
							 self.model.procedureDetails[index].procedureSequence = $scope.procedureSequenceList[0].id;
							return false;
						}
						
						if(isDuplicateQuaternary){
							 growl.warning("Quaternary Procedure Is Already Exist", {
                                   title : 'warning!'
                               });
							 self.model.procedureDetails[index].amount = 0.0;
							 self.model.procedureDetails[index].rate = 0.0;
							 self.model.procedureDetails[index].procedureSequence = $scope.procedureSequenceList[0].id;
							return false;
						}
						
						if(isDuplicateQuinary){
							 growl.warning("Quinary Procedure Is Already Exist", {
                                   title : 'warning!'
                               });
							 self.model.procedureDetails[index].amount = 0.0;
							 self.model.procedureDetails[index].rate = 0.0;
							 self.model.procedureDetails[index].procedureSequence = $scope.procedureSequenceList[0].id;
							return false;
						}
						self.model.procedureDetails[index].procedureSequence = parseInt(self.model.procedureDetails[index].procedureSequence);
						if(self.model.procedureDetails[index].procedureSequence == 0){
							 self.model.procedureDetails[index].amount = 0.0;
							 self.model.procedureDetails[index].rate = 0.0;
						}
						if(self.model.procedureDetails[index].procedureSequence == 1){
							self.model.procedureDetails[index].isPrimarySecondary = 'Y';
						}else{
							self.model.procedureDetails[index].isPrimarySecondary = 'N';
						}
						
						//change SubSequent charges
									if(self.model.procedureDetails[index].subSequentDetailsList != null && self.model.procedureDetails[index].subSequentDetailsList.length>0){
										
										var subSeqObj =  self.model.procedureDetails[index].subSequentDetailsList.find(obj => {return obj.sequenceId == self.model.procedureDetails[index].procedureSequence})
										
										if(subSeqObj != undefined){
											
											if(subSeqObj.discPercentage !=null && subSeqObj.discPercentage != 0){
												
												self.model.procedureDetails[index].amount = (self.model.procedureDetails[index].originalAmount * subSeqObj.discPercentage)/100;
												self.model.procedureDetails[index].rate  = self.model.procedureDetails[index].amount;
												
												$scope.prodedures.billOfresList.forEach(function (billvalue, billIndex){
													
													if(self.model.procedureDetails[index].serviceId == billvalue.procedureId && billvalue.billOfMaterialId!=null){
														
														billvalue.amount = (billvalue.originalAmount * subSeqObj.discPercentage)/100;
													}
												});
											}else{
												var amt = 0.0 ;
												$scope.prodedures.billOfresList.forEach(function (billvalue, billIndex){
													
													if(self.model.procedureDetails[index].serviceId == billvalue.procedureId && billvalue.billOfMaterialId!=null){
														
														var infoObj = subSeqObj.subSequentDetailsInfoList.find(obj => {return obj.serviceId == billvalue.serviceId});
														
														if(infoObj!=undefined){
															
															if(infoObj.discPercentage!=null && infoObj.discPercentage!=0){
																amt = amt + (billvalue.originalAmount * infoObj.discPercentage)/100;
															}
														}
													}
												});
												self.model.procedureDetails[index].amount = amt;
												self.model.procedureDetails[index].rate  = amt; 
											}
										}
									}
				}
				
				$scope.assignToTimesheet = function(){
					
					if($scope.otDetalsObj.otDate!=undefined && $scope.otDetalsObj.otDate!=null){
						self.model.timeSheet.wheelInTime = $scope.otDetalsObj.otDate;
						self.model.timeSheet.tableOnTime = $scope.otDetalsObj.otDate;
						self.model.timeSheet.anesthesiaTime = $scope.otDetalsObj.otDate;
						self.model.timeSheet.procedureStartTime = $scope.otDetalsObj.otDate;
						self.model.timeSheet.procedureEndTime = $scope.otDetalsObj.otDate;
						self.model.timeSheet.tableOutTime = $scope.otDetalsObj.otDate;
						self.model.timeSheet.wheelOutTime = $scope.otDetalsObj.otDate;
						//self.model.timeSheet.scheduleDischarge = $scope.otDetalsObj.otDate;
						self.model.timeSheet.actualDischarge = $scope.otDetalsObj.otDate;
					}
				}

				$scope.calculateReceivedQty = function (item) {
					if(item.usedQty == undefined || item.usedQty == null || item.usedQty == ""){
						item.usedQty = 0;
					}
					if(item.isThreeUOMApplicable == false){
						if(item.uomTypeId == 1){
							item.receivedQtyCon = (item.usedQty +"*"+ item.selectedBatch.uomListData[0].conversion);
							item.receivedQtyCalculation = (item.usedQty * item.selectedBatch.uomListData[0].conversion);
							item.uomUnitData = item.selectedBatch.uomListData[2].uomUnit;
						}
						else if(item.uomTypeId == 2){
						
						}
						else{
							item.receivedQtyCon = (item.usedQty);
							item.receivedQtyCalculation = (item.usedQty);
							item.uomUnitData = item.selectedBatch.uomListData[2].uomUnit;
						}
					}
					else{
						if(item.uomTypeId == 1){
							item.receivedQtyCon = (item.usedQty +"*"+ item.selectedBatch.uom[0].conversion+"*"+ item.selectedBatch.uom[1].conversion);
							item.receivedQtyCalculation = (item.usedQty * item.selectedBatch.uom[0].conversion * item.selectedBatch.uom[1].conversion);
							item.uomUnitData = item.selectedBatch.uom[2].uomUnit;
						}
						else if(item.uomTypeId == 2){
							item.receivedQtyCon = (item.usedQty +"*"+ item.selectedBatch.uom[1].conversion);
							item.receivedQtyCalculation = (item.usedQty * item.selectedBatch.uom[1].conversion);
							item.uomUnitData = item.selectedBatch.uom[2].uomUnit;
						}
						else{
							item.receivedQtyCon = (item.usedQty);
							item.receivedQtyCalculation = (item.usedQty);
							item.uomUnitData = item.selectedBatch.uom[2].uomUnit;
						}
					}
				}
				
				$("#orderModal").hide();
				$('.modal-backdrop').remove();
				$('body').removeClass("modal-open");
				
	}

	function config($stateProvider) {
		$stateProvider.state('detailsOTBillingCompletion', {
			url: '/detailsOTBillingCompletion',
			templateUrl: 'views/master/TheatreManagement/details-OT-billing-completion.html',
			controller: "OtBillingDetailsComp.Controller",
			controllerAs: "vm"
		})
	}


	angular.module("myApp").config(config).controller("OtBillingDetailsComp.Controller", otBillDetailsCompletionController);



})();