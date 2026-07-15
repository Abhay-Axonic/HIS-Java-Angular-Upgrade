/************EDITED ************************/
'use strict';

/**
 * @Author Sneha Dalave
 * @Author Chetna Dhokne 
 * @name myApp.controller:OPDCoverSheetController
 * @description #OPDCoverSheetController Controller of the myApp
 */
angular.module('myApp').controller('drugHistoryTabController',['$scope','$cookies','$rootScope','GenericService',
	'BillingGenericService','$state','$timeout','$interval','$stateParams','GenericServiceParamHeader','growl','$compile',
	'reportService','GenericServiceFileUpload','servicesFactory','CONSTANTS','coversheetTemplateService',
	function($scope, $cookies, $rootScope, GenericService,BillingGenericService, $state, $timeout,$interval,$stateParams,
		GenericServiceParamHeader,growl,$compile,reportService,GenericServiceFileUpload,servicesFactory,CONSTANTS,coversheetTemplateService) {
						
		var cookieObject, cookieObjectAppConfig;
		[ cookieObject, cookieObjectAppConfig ] = $scope.checkCookies();
					
		$scope.init = function() 
		{					

			$scope.dateLetter = new Date();
			$scope.deptWiseAppointment='N';
					
			$scope.medicationObj = 
			{
				itemId : "",
				searchItem :"",
				searchItemFre:"",
				favorite_status : false,
				medicationStartDate : new Date(),
				roaId : '',
				dose : '',
				doseUnitId : '',
				durationCount : "",
				durationUnit:'',
				instructionId : '',
				medicationEndDate : '',
				frequencyId : '',
				quantity : '',
				uomTypeId : '',
				conversions : '',
				strength : '',
				priorityId : 2,
				isStat : false,
				priorityCode: 'NR',
				isGeneric:'N',
				isInstruction:'N',
				nonFormularyAdvice:'N',
				rate:'',
				prescribedDosageUnitList:[]
			};
								
			$scope.initMedicationObj = function() 
			{
				$scope.searchItemFre = "";
				$scope.searchItem = "";
				$scope.registrationNumber = "";
				$scope.saveBtnFlag = true;
				$scope.updateScope = true;
				$scope.updateBtnFlag = false;
				$scope.roa_options=[];
				$scope.medicationObj = 
				{
					itemId : "",
					searchItem : "",
					formulationTypeId : '',
					favorite_status : false,
					medicationStartDate : new Date(),
					roaId : '',
					dose : '',
					doseUnitId : '',
					durationCount : '',
					durationUnit : 'Day',
					instructionId : '',
					medicationEndDate : '',
					frequencyId : '',
					quantity : '',
					uomTypeId : '',
					conversions : '',
					strength : '',
					priorityId : '2',
					priorityCode : 'NR',
					isGeneric:'N',
					isInstruction:'N',
					nonFormularyAdvice:'N',
					searchItemFre :"",
					isStat : false,
					favorite_status : false,
					isDischarge : false,
					nonFormularyAdvice:'N',
					prescribedDosageUnitList:[]
				};
			}
								
			$scope.medicationDetails = [];
			$scope.favouritMedi = {};
			$scope.itemSelecteds = {};
							
			//$scope.medicationHistoryDetails =[];
			//to show empty row in Medication History 
			$scope.emptyMedicationHistoryRow = 
			{
				itemId:"",
				medicationStartDate:'',
				genericName:'',
				itemName:'',
				genericId:'',
				doctorId:'',
				roaId:'',
				durationCount:'',
				medicationName:'',
				dose:'',
				doseUnitId:'',
				durationUnit:'Day',
				instructionId:'',
				medicationEndDate:'',
				frequencyId:'',
				name:'',
				status:'A',
				authorisedDate:'',
				quantity:'',
				storeId:'',
				nonFormulary:'N',
				genericName:"",
				searchItem:"",
				ongingMedHist:false,
				isMedicationContinue:'N',
				isMedicationStopHis:'N'
			};
			$scope.medicationHistoryDetails = [$scope.emptyMedicationHistoryRow];
			$scope.msgRecord="No records found...!!";
			$scope.itemSelectedList = {
				items: []
			};
							
			$scope.itemSelectedListActiveMed = {
				itemsMed: [],
				selectAllActMedi:'N'
			};
							
			$scope.allergyShowFlag = true;
			$scope.admissionObj={};
			//$scope.admissionObj.bedOptionFlexible = "N";
			$scope.admissionObj.admissionType = "medical";
			$scope.admissionObj.bedOptionFlexible = "N";
			$scope.searchType = "search";
			$scope.currentTab = "investigation";
			$scope.currentOrganizationList = {"organizationId":1,"organizationName":"RubbyHall"}
			$scope.currentUnitList = {"unitId":1,"unitName":"Rubby Unit1"}
			$scope.selectedValues = {unitId : '',doctorId:'',specialityId:''};
			$scope.cur=moment(new Date()).subtract(1, 'days').toDate();
			$scope.currentDateMedication = moment($scope.cur).format('YYYY-MM-DD')
			$scope.currentDate = moment(new Date()).format('YYYY-MM-DD');
									
			var cookieObject = $cookies.getObject('cookieObject');
			if (cookieObject == undefined) 
			{
				$state.go('login');
				return;
			}
							
			$scope.unitId = cookieObject.unitId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.employeeId = cookieObject.employeeId;
							
			var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
			if(cookieObjectAppConfig == undefined)
			{
				$state.go('applicationConfiguration');
				return;
			}
			else
			{
				console.log('cookieObjectAppConfig',cookieObjectAppConfig);						
			}
			$scope.diagnosisRequireAppConfig = cookieObjectAppConfig.diagnosisRequire;
			$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
			$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
			$scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
			$scope.allergyButtonFlag = true;
			$scope.paediatricsSpecialityId = cookieObjectAppConfig.paediatricsSpecialityId;
			$scope.radiologySpecialityId=cookieObjectAppConfig.radiologySpecialityId;
			$scope.isAdviceAccess=cookieObjectAppConfig.isAdviceTabVisible;
			$scope.roleName = cookieObjectAppConfig.roleName;
			$scope.nurseRole = cookieObjectAppConfig.nurseRole;	 
			$scope.doctorRole = cookieObjectAppConfig.doctorRole;
			$scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
			$scope.isAppointmentRequired = cookieObjectAppConfig.isAppointmentRequired;
			$scope.configureConsultationSpecialityId=cookieObjectAppConfig.consultationSpecialityId;
			$scope.pharmacyServiceId = cookieObjectAppConfig.pharmacyServiceId;
			var year = new Date().getFullYear();
			var range = [];
			range.push(year);
			$scope.patientDetails = 
			{
				uhIdNumber:"",
				patientName:"",
				genderCode:"",
				age:"",
				birthDate:"",
				encounterId:""
			};
							
			$scope.checkAppointment=function(){
			$scope.encounterDetailsObj = JSON.parse(sessionStorage.getItem('cookieEncounterObject')) || {};
			console.log("Encounter Object",$scope.encounterDetailsObj);
								
			if ($scope.encounterDetailsObj  != null) {
				$scope.billIdForCheck = $scope.encounterDetailsObj.billId;
				$scope.patientDetails.patientName = $scope.encounterDetailsObj.patientName;
				$scope.patientDetails.specialityName = $scope.encounterDetailsObj.specialityName;
				$scope.patientDetails.uhIdNumber = $scope.encounterDetailsObj.uhidNumber;
				$scope.patientDetails.genderCode = $scope.encounterDetailsObj.genderCode;
				$scope.patientDetails.genderId = $scope.encounterDetailsObj.genderId;
				$scope.patientDetails.age = $scope.encounterDetailsObj.age;
				$scope.patientDetails.ageStr = $scope.encounterDetailsObj.age;
				$scope.patientDetails.visitTypeId = $scope.encounterDetailsObj.visitTypeId;
				$scope.patientDetails.birthDate = $scope.encounterDetailsObj.dob;
				$scope.patientDetails.visitDate = $scope.encounterDetailsObj.encounterDate;
				$scope.patientDetails.visitTime = $scope.encounterDetailsObj.consultationStartTime;
				$scope.patientDetails.encounterId = $scope.encounterDetailsObj.encounterId;
				$scope.roleId = $scope.encounterDetailsObj.roleId;
				$scope.patientDetails.patientCategoryId =$scope.encounterDetailsObj.patientCategoryId;
				// $scope.isCritical = $scope.encounterDetailsObj.isCritical;
				$scope.encounterIdVital =  $scope.encounterDetailsObj.encounterId;
				$scope.patientDetails.doctorFullName =  $scope.encounterDetailsObj.doctorName;
				if($scope.encounterDetailsObj.doctorName!=null)
				$scope.patientDetails.dFirstName  =  $scope.encounterDetailsObj.doctorName;
				$scope.patientDetails.consultationStartTime =  $scope.encounterDetailsObj.consultationStartTime;
				
				$scope.encounterDetailsObj.treatingDoctorSpecialityId = "";
				$scope.encounterDetailsObj.treatingDoctorId = "";
				$scope.encounterDetailsObj.isTreatingDoctor = 'Y';
										
				//For year dropdown
				var ya = new Date().getFullYear()
				var BirthData= $scope.patientDetails.birthDate.split('/');
				$scope.birthYear=BirthData[2];
				var birthMonth=BirthData[1].split('');
				$scope.birthMonth=birthMonth[1];
				$scope.YearDifference = ya-$scope.birthYear;
				for (var i = 1; i < 100; i++) 
				{
					range.push(year - i);
				}
										  
				$scope.yearss = range;
				$scope.years=[];
				angular.forEach($scope.yearss,function(value){
					if(parseInt($scope.birthYear) <= value)
					{
						$scope.years.push(value);
					}
				});
				console.log("KI",$scope.yearss);
				console.log("$scope.yearsss",$scope.years);
				//For Contract
				if($scope.encounterDetailsObj.contractId != $scope.selfPayeeId)
				{
					$scope.contractId = $scope.encounterDetailsObj.contractId;
				}
				$scope.showHeadCircuVital=false;
				if($scope.patientDetails.age >= 1 &&  $scope.patientDetails.age <=3 )
				{
					$scope.showHeadCircuVital=true;
				}						
			}
			else
			{
				$state.go('encounterQueueManagement');
				return;
			}						
		}
		$scope.checkAppointment();
							
		$rootScope.loginpage = true;
		$scope.saveBtnFlag = false;
		$scope.updateBtnFlag = true;

		// for popup
		$scope.showModal = false;
		$scope.buttonClicked = "";
		$scope.popUpFlag = true;
		
		// end for popup					
		var commonObj = {
			unitId : $scope.unitId,
			organizationId : $scope.organizationId
		};
		var data = {};					
		var dataDose = {
			organizationId:$scope.organizationId
		}
		// For Get Active Speciaity List
		var URI = BASE_URL + ROOT_URL + GETACTIVESPECIALITY;
		BillingGenericService.serviceAction(METHOD_POST, URI,commonObj,NOTIFICATION_MSG_STATUS_FALSE).then(
		function(response) {
			console.log(response);
			// $rootScope.startLoader();
			if (response.data.status == "success")
				$scope.specialityList = response.data.listObject;
		});
							     
		var headerObj = {
			unit_id : $scope.unitId,
			organization_id : $scope.organizationId
		};
                   
							     
		//for Complaint and Diagnosis Template
		$scope.templateListForSave=[];
		var tempObj={};
		tempObj.templateObject={};
		tempObj.diagonisIdforTemplate=0;
		tempObj.icpcSystemId=0;
		tempObj.icpcId=0;
		$scope.templateListForSave.push(tempObj);
	                        	 
		$scope.common = 
		[{
			diagnosisId : '',
			icpcSystemId : '',
		}];
							
		if ($scope.encounterDetailsObj == undefined) {
			return;
		}
		var patientId = $scope.encounterDetailsObj.patientId;
		var encounterId = $scope.encounterDetailsObj.encounterId;
					    		
		if (angular.equals({},$stateParams.obj)) 
		{
			$state.go('OPDcoversheetNew');
			return;
		} 
		else 
		{
			if($stateParams.obj.newCasesheetList != undefined)
			{
				$scope.newCasesheetList=$stateParams.obj.newCasesheetList;
			}
			if($stateParams.obj.objPrint!= undefined)
			{
				$scope.objPrint = $stateParams.obj.objPrint;
				$('#coversheetPdfFilePath1').attr('src',"data:application/pdf;base64,"+$scope.objPrint);
			}
			/*if($stateParams.obj.vitalsList != undefined)
			{
				$scope.vitalsList=$stateParams.obj.vitalsList;
			}*/
			if($stateParams.obj.currentEncounterOrderLists!= undefined)
			{
				$scope.currentEncounterOrderLists = $stateParams.obj.currentEncounterOrderLists;
			}
			if($stateParams.obj.familyHistoryCurrentListDoctor != undefined)
			{
				$scope.familyHistoryCurrentListDoctor=$stateParams.obj.familyHistoryCurrentListDoctor;
			}
			if($stateParams.obj.habitFinalList!= undefined)
			{
				$scope.habitFinalList = $stateParams.obj.habitFinalList;
			}
			if($stateParams.obj.patientHabitOldList != undefined)
			{
				$scope.patientHabitOldList=$stateParams.obj.patientHabitOldList;
			}
			if($stateParams.obj.templateList!= undefined)
			{
				$scope.templateList = $stateParams.obj.templateList;
			}
			if($stateParams.obj.icpcSystemList != undefined)
			{
				$scope.icpcSystemList=$stateParams.obj.icpcSystemList;
			}
			if($stateParams.obj.templateTransactionList!= undefined)
			{
				$scope.templateTransactionList = $stateParams.obj.templateTransactionList;
			}
			if($stateParams.obj.detailedMedicationsHistory != undefined)
			{
				$scope.detailedMedicationsHistory=$stateParams.obj.detailedMedicationsHistory;
			}
			if($stateParams.obj.detailedMedications!= undefined)
			{
				$scope.detailedMedications = $stateParams.obj.detailedMedications;
			}
			if($stateParams.obj.allergySymbolList != undefined)
			{
				$scope.allergySymbolList=$stateParams.obj.allergySymbolList;
			}
			if($stateParams.obj.allergyOldList!= undefined)
			{
				$scope.allergyOldList = $stateParams.obj.allergyOldList;
			}
			if($stateParams.obj.patientDiagnosisListIcon != undefined)
			{
				$scope.patientDiagnosisListIcon=$stateParams.obj.patientDiagnosisListIcon;
			}
			if($stateParams.obj.patientDiagnosisList!= undefined)
			{
				$scope.patientDiagnosisList = $stateParams.obj.patientDiagnosisList;
			}
			if($stateParams.obj.complaintListByPatientId != undefined)
			{
				$scope.complaintListByPatientId=$stateParams.obj.complaintListByPatientId;
			}
			if($stateParams.obj.pastComplaintListByPatientId!= undefined )
			{
				$scope.pastComplaintListByPatientId = $stateParams.obj.pastComplaintListByPatientId;
			}
			if($stateParams.obj.complaintsPatientListDoctor != undefined)
			{
				$scope.complaintsPatientListDoctor=$stateParams.obj.complaintsPatientListDoctor;
			}
			if($stateParams.obj.vitalFinalList!= undefined )
			{
				$scope.vitalFinalList = $stateParams.obj.vitalFinalList;
			}
			if($stateParams.obj.patientViatalList != undefined)
			{
				$scope.patientViatalList=$stateParams.obj.patientViatalList;
			}
			if($stateParams.obj.patientViatalListTrends!= undefined )
			{
				$scope.patientViatalListTrends = $stateParams.obj.patientViatalListTrends;
			}
			if($stateParams.obj.vitalDataTrends != undefined)
			{
				$scope.vitalDataTrends=$stateParams.obj.vitalDataTrends;
			}
			if($stateParams.obj.vitalLabel!= undefined )
			{
				$scope.vitalLabel = $stateParams.obj.vitalLabel;
			}
			if($stateParams.obj.habitTypeList != undefined)
			{
				$scope.habitTypeList=$stateParams.obj.habitTypeList;
			}
			if($stateParams.obj.patientHabitList!= undefined )
			{
				$scope.patientHabitList = $stateParams.obj.patientHabitList;
			}
			if($stateParams.obj.complaintsPatientList!= undefined )
			{
				$scope.complaintsPatientList = $stateParams.obj.complaintsPatientList;
			}
			if($stateParams.obj.medicalHistoryCurrentList!= undefined )
			{
				$scope.medicalHistoryCurrentList = $stateParams.obj.medicalHistoryCurrentList;
			}
		}
	}
		
		 /****For tab access*****/
        var tabsList = JSON.parse(localStorage.getItem('mappedDataCoversheetByUser'));
		 
		$scope.mappedDataCoversheetByUserss = [];
		 $scope.mappedDataCoversheetByUserss =tabsList;
		 
			$scope.checkAccessCover = function (id) 
			{
					if (!$scope.mappedDataCoversheetByUserss || $scope.mappedDataCoversheetByUserss.length == 0) {
						return true;
					}
					var arr = $scope.mappedDataCoversheetByUserss.filter(function (item) 
					{
						return item.headerId == id;
					});
					return (arr.length > 0) ? true : false;
		
		    }
		$scope.test = [];
		$scope.checkAccessCoverSubTab = function (id,headerId) 
		{
				if (!$scope.mappedDataCoversheetByUserss || $scope.mappedDataCoversheetByUserss.length == 0) {
					return true;
				}
				var arr = $scope.mappedDataCoversheetByUserss.filter(function (item) 
				{
					if(headerId == item.headerId){
						if(item.selectedSubTabId.length > 0 ){
							$scope.test = [];
							$scope.test = angular.copy(item.selectedSubTabId);
						}
					}
				});
				if($scope.test.length > 0){
					var arr1 = $scope.test.filter(function (item1) {
						return item1.subTabId == id;
					});
					return (arr1.length > 0) ? true : false;
				}
		}
	$scope.init();
						
	/*************************Init End****************************/	


	/** ***** END TAB FOR ALL PAGES IN TREE LEVEL ****** */
	/**FOR SET CODE START****** */
	$scope.tab = 3;
	$scope.isSet = function(tabNum)
	{
		return $scope.tab === tabNum;
	};
				
	$scope.gotoNextSectionLevelOne = function(Tab)
	{
		var obj={
			newCasesheetList:$scope.newCasesheetList,
			objPrint :$scope.objPrint,
			vitalsList:$scope.vitalsList,
			currentEncounterOrderLists : $scope.currentEncounterOrderLists,
			familyHistoryCurrentListDoctor : $scope.familyHistoryCurrentListDoctor,
			habitFinalList : $scope.habitFinalList,
			patientHabitOldList : $scope.patientHabitOldList,
			templateList : $scope.templateList,
			icpcSystemList :$scope.icpcSystemList,
			templateTransactionList: $scope.templateTransactionList,
			detailedMedicationsHistory: $scope.detailedMedicationsHistory,
			detailedMedications : $scope.detailedMedications,
			allergySymbolList :$scope.allergySymbolList,
			allergyOldList : $scope.allergyOldList,
			patientDiagnosisListIcon :$scope.patientDiagnosisListIcon,
			patientDiagnosisList: $scope.patientDiagnosisList,
			complaintListByPatientId : $scope.complaintListByPatientId,
			pastComplaintListByPatientId : $scope.pastComplaintListByPatientId,
			complaintsPatientListDoctor :$scope.complaintsPatientListDoctor,
			vitalFinalList :$scope.vitalFinalList,
			patientViatalList :$scope.patientViatalList,
			patientViatalListTrends :$scope.patientViatalListTrends,
			vitalDataTrends : $scope.vitalDataTrends,
			vitalLabel :$scope.vitalLabel,
			habitTypeList :$scope.habitTypeList,
			patientHabitList :$scope.patientHabitList,
			complaintsPatientList : $scope.complaintsPatientList,
			medicalHistoryCurrentList :$scope.medicalHistoryCurrentList
		};
		var id='';
		if(Tab == 1)
		{
			var id= "complaint_section";
			$state.go('complaintsTab', {obj : obj});
		}
		if(Tab == 2)
		{
			var id= "past_history_section";
			$state.go('medicalHistoryTab', {obj : obj});
		}
		if(Tab == 3)
		{
			var id= "drug_history_tab";
			$state.go('drugHistoryTab', {obj : obj});
		}
		if(Tab == 4)
		{
			var id= "social_history";
			$state.go('socialHistoryTab', {obj : obj});
		}
		if(Tab == 5)
		{
			var id= "allergies_section";
			$state.go('allergiesTab', {obj : obj});
		}
		if(Tab == 6)
		{
			var id= "vitals_section";
			$state.go('vitalsTab', {obj : obj});
		}
		if(Tab == 7)
		{
			var id= "examination_tab";
			$state.go('examinationTab', {obj : obj});
		}
		if(Tab == 8)
		{
			var id= "diagnosis_section";
			$state.go('diagnosisTab', {obj : obj});
		}
		if(Tab == 9)
		{
			var id= "orders_section";
			$state.go('ordersTab', {obj : obj});
		}
		if(Tab == 10)
		{
			var id= "notes_section";
			$state.go('notesTab', {obj : obj});
		}
		if(Tab == 11)
		{
			var id= "advice_section";
			$state.go('adviceTab', {obj : obj});
		}
		if(Tab == 12)
		{
			var id= "followUp_section";
			$state.go('follwUpTab', {obj : obj});
		}
		if(Tab == 13)
		{
			var id= "attach_to_casesheet";
			$state.go('attachToCasesheetTab', {obj : obj});
		}
		if(Tab == 14)
		{
			var id= "letters_section";
			$state.go('letterTab', {obj : obj});
		}
							
	}
	/**FOR SET CODE END*******/
						
	$scope.encounterDetailsList = [];
	$scope.encounterDetails = 
	{
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
		file : {}
	}
						
						
	/*************Patient General Examination list**************/					
	var commonObj = JSON.parse(sessionStorage.getItem('cookieEncounterObject'));
	// for added complaints
	// Search Vital By Dates
	$scope.msg="No records found...!!";
	// Selected TypeHead on Complaints					
	$scope.selectPatientIdUpdate = function(info, model, label,ind) 
	{
		console.log(info);
		$scope.complaintsUpdateObj.diagnosisId= info.icpcId;
						
		if($scope.complaintsUpdateObj.icpcSystemId == null || $scope.complaintsUpdateObj.icpcSystemId == "")
		{
			$scope.searchICPCByCodeUpdate()
		}
	}
	//Chetana
	
	$scope.updateEncounterDetailsMultilingual=function(info){
		var headerObjS={
				'organization_id': $scope.organizationId,
				'unit_id':$scope.unitId,
				'Content-Type':'application/json'
		};
		var paramObjForPer={};
		var URI = BASE_URL + ROOT_URL
		+ "/api/opd/updateEncounterDetailsMultilingual";
		GenericServiceParamHeader.serviceAction(METHOD_POST, URI, info,NOTIFICATION_MSG_STATUS_TRUE,paramObjForPer,headerObjS)
			.then( function(response) {
						if (response.data.status == "success")
						{
							$rootScope.stopLoader();
						}
					});
	}
	
	$scope.completeConsultation=function(roleId)
	{	
		var cookieWObject = JSON.parse(sessionStorage.getItem('cookieEncounterObject'));
		if (cookieWObject == undefined) {
			return;
		}
		// alert(cookieWObject.encounterId);
		var headerObj=
		{
			'organization_id': $scope.organizationId,
			'unit_id':$scope.unitId,
			'Content-Type':'application/json'
		};
		
		$rootScope.startLoader();
		if(roleId == 3)
		{
			var data = 
			{
				'assignmentStopTime':moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
				'isAssignmentCompStatus':'Y',
				'assignmentStopBy': $scope.userId,
				'assignmentStopRoleId': roleId,
				'encounterId': cookieWObject.encounterId,
				"patientId": cookieWObject.patientId,
				"appointmentId":cookieWObject.appointmentId,
				"updatedBy":$scope.userId,
				"childEncounterId":cookieWObject.childEncounterId
			}
		}
		else
		{
			
			/*if($scope.diagnosisRequireAppConfig == 'Y'){
				if($scope.patientDiagnosisList.length == 0){
					 growl.error("Please add Diagnosis",
							 {
						 title : 'Error!'
							 });
					 return false;
				}
			}*/
			var data = 
			{
				'isConsultationStatus': "Y",
				'consultationStopTime': moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
				'consultationStopBy': $scope.userId,
				'consultationStopRoleId': roleId,
				'encounterId': cookieWObject.childEncounterId,
				"patientId": cookieWObject.patientId,
				"appointmentId":cookieWObject.appointmentId,
				"updatedBy":$scope.userId,
				"isFromCoverSheet":'Y',
				"childEncounterId":cookieWObject.childEncounterId
			}
			
		}
		
		console.log('SaveCoversheet',data)	
		
		var datatvgsvx  = data;

		var paramObjForPer={};
		var URI = BASE_URL + ROOT_URL + UPDATEENCOUNTERASSIGNMENT;
		GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_TRUE,paramObjForPer,headerObj).then(
		function(response) 
		{
			console.log(response);
			// $rootScope.startLoader();
			if (response.data.status == "success")
			{	
				// alert("success");						
				/*if(roleId==2)
				{
					var data =  {
						"appName": "HIS",
						"appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
						"reportDesignFilePath": "OPDCaseSheet.rpt",
						"reportFormat": "PDF",
						"exportFileName": "OPDCoversheet",
						"reportParams": [
								{
									"paramName": "@EncounterId",
									"paramType": "Integer",
									"paramValue": cookieWObject.encounterId
								}					               
						]
					};
					reportService.generate(REPORT_API_URI1,data);
				}*/
				$rootScope.stopLoader();
				$scope.printCaseSheet();
				$scope.updateEncounterDetailsMultilingual(datatvgsvx);
				sessionStorage.removeItem('cookieEncounterObject');
				if(roleId == 2)
				{
					$state.go("doctorDesk");
					$rootScope.stopLoaderPopup();
					}
					else{
						$state.go("encounterQueueManagement");
						$rootScope.stopLoaderPopup();
					}					
			}
		});
	}


	$scope.onClick = function () 
	{
		$scope.myChartForSearch = false;
		$scope.vitalnameGraphList=[];
		$scope.vitalname=[];
		$scope.createdTime=[];
		$scope.vitalData=[];
		$scope.vitalTime=[];
		$scope.vitalLabel=[];
		//for label
		if($scope.patientViatalList.length > 0)
		{
			angular.forEach($scope.patientViatalList, function(value,key) {
				$scope.flag=false;
				if($scope.vitalnameGraphList.length > 0){
					angular.forEach($scope.vitalnameGraphList, function(value3,key) {
						if(value3.vitalName == value.vitalName){
							$scope.flag = true;
						}
					});
				}
				if(!$scope.flag){
					var newArr={};
					newArr.vitalName=value.vitalName;
					newArr.unitDesc=value.unitDesc;
					$scope.vitalnameGraphList.push(newArr);
				}
				console.log("$scope.vitalnameGraphList",$scope.vitalnameGraphList);
			});
			//for time array
			angular.forEach($scope.patientViatalList, function(value,key) {
				$scope.flagTime=false;
				if($scope.createdTime.length > 0)
				{
					angular.forEach($scope.createdTime, function(value3,key) {
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
					$scope.createdTime.push(newArr);
				}
				console.log("$scope.createdTime",$scope.createdTime);									 
			});
										 
			for (var i = 0; i < $scope.createdTime.length; i++) {
				$scope.vitalLabel.push($scope.createdTime[i].createdTime);
			}
			console.log("graph label",$scope.vitalLabel);
							
			//for data
			angular.forEach($scope.createdTime, function(value1,index) {
				$scope.vitalDataTrends=[];
				angular.forEach($scope.patientViatalList, function(value2,index) {
					if(value1.createdDate == value2.createdDate){
						$scope.vitalDataTrends.push(value2);
					}
				});
				value1.vitalDataTrends = $scope.vitalDataTrends;
			});
			console.log("d",$scope.createdTime);
			$scope.finalVitalObjFromAllVitalList = {};
			var hh =  $scope.createdTime.length;
			angular.forEach( $scope.createdTime[0].vitalDataTrends, function( value,key) {
				if(value.roleId == 2)
				{
					if(value.vitalId == 6)
					{
						if(parseInt(value.value) != 0)
						{
							$scope.finalVitalObjFromAllVitalList.temp = value.value;
							$scope.finalVitalObjFromAllVitalList.createdDatetemp = value.createdDate;
							$scope.finalVitalObjFromAllVitalList.createdTimetemp = value.createdTime;
							$scope.finalVitalObjFromAllVitalList.unitDesctemp = value.unitDesc;
						}                            		
					}
					if(value.vitalId == 4)
					{
						//pulse rate
						if(parseInt(value.value) != 0)
						{
							$scope.finalVitalObjFromAllVitalList.pulseRate = value.value;
							$scope.finalVitalObjFromAllVitalList.createdDatepulseRate = value.createdDate;
							$scope.finalVitalObjFromAllVitalList.createdTimepulseRate = value.createdTime;
							$scope.finalVitalObjFromAllVitalList.unitDescpulseRate = value.unitDesc;
						}
					}
					if(value.vitalId == 3)
					{
						//BP
						if(parseInt(value.value) != 0)
						{
							$scope.finalVitalObjFromAllVitalList.bps = value.value;
							$scope.finalVitalObjFromAllVitalList.createdDatebps = value.createdDate;
							$scope.finalVitalObjFromAllVitalList.createdTimebps = value.createdTime;
							$scope.finalVitalObjFromAllVitalList.unitDescbps = value.unitDesc;
						}
					}
					if(value.vitalId == 14)
					{
						//SPo2
						if(parseInt(value.value) != 0)
						{
							$scope.finalVitalObjFromAllVitalList.bloodOxygen = value.value;
							$scope.finalVitalObjFromAllVitalList.createdDatebloodOxygen = value.createdDate;
							$scope.finalVitalObjFromAllVitalList.createdTimebloodOxygen = value.createdTime;
							$scope.finalVitalObjFromAllVitalList.unitDescbloodOxygen = value.unitDesc;
						}
					}
					if(value.vitalId == 7){
//                		SPo2
                		if(parseInt(value.value) != 0){
                		$scope.finalVitalObjFromAllVitalList.bpd = value.value;
                		$scope.finalVitalObjFromAllVitalList.createdDatebloodOxygen = value.createdDate;
                		$scope.finalVitalObjFromAllVitalList.createdTimebloodOxygen = value.createdTime;
                		$scope.finalVitalObjFromAllVitalList.unitDescbloodOxygen = value.unitDesc;
                		}
                	}
				}                             	
			});
		}
	};
				                    
	$scope.finalVitalObjFromAllVitalList=
	{
		"temp" :"",
		"createdDatetemp" :"",
		"createdTimetemp" :"",
		"unitDesctemp" :"",
		"pulseRate" :"",
		"createdDatepulseRate" :"",
		"createdTimepulseRate" :"",
		"unitDescpulseRat":"",
		"bps" :"",
		"bpd" :"",
		"createdDatebps" :"",
		"createdTimebps" :"",
		"unitDescbps" :"",
	}
									
	$scope.checkFocus = function()
	{
		$timeout(function() {
			document.getElementById("vitalssss_0").focus();
		}, 500);
	} 
						                    
	$scope.k = function()
	{
		var headerObj = 
		{
			unit_id : $scope.unitId,
			organization_id : $scope.organizationId
		};
						  			          				                    	
		var paramObjForVital = {			                       				
			//encounterId : $scope.encounterIdVital,
			unitId:$scope.unitId,
			organizationId:$scope.organizationId,
			patientId:$scope.encounterDetailsObj.patientId,
			//roleId:$scope.roleId,
			//flag:'N' 
		}
		var data=
		{
			unitId:$scope.unitId,
			organizationId:$scope.organizationId,
			patientId:$scope.encounterDetailsObj.patientId,
		}
		console.log("jj",paramObjForVital);
		$scope.finalVitalObjFromAllVitalList = {};
		// return false;
		var URI = BASE_URL + ROOT_URL + "/api/opd/getListOfVitalsByPatientIdForCoversheet";
		GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_FALSE,data, 
		paramObjForVital,headerObj).then(function(response) {
			console.log(response);        
			if (response.data.status == "success")
				$scope.vitalFinalList = [];
				$scope.patientViatalListTrends=[];
				$scope.patientViatalList  = response.data.listObject;
				angular.forEach( $scope.patientViatalList, function( value,key) {
					var newArr = {};
					newArr.createdDate = value.createdDate;
					newArr.createdTime = value.createdTime;
					newArr.data = {
						"vitalId" : value.vitalId,
						"value"   : value.value,
					}
					$scope.patientViatalListTrends.push(newArr);
				});
				$scope.onClick();
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
							$scope.newArr.checkQualifier = true;
							if($scope.roleId == 2)
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
						newArr.vitalValue = value.vitalValue;
						//newArr.vitalValue = 0;
						newArr.isReviewedFlag = 'N';
						newArr.vitalOrderOpd  = value.vitalOrderOpd;
						newArr.checkQualifier = true;
						if($scope.roleId == 2)
						{
							newArr.appointmentVitalMapperId ='';
						}
						$scope.vitalFinalList.push(newArr);
					}	                            	       
				});
						                             		                            
				console.log("Final Vital Arr",JSON.stringify($scope.vitalFinalList));
				var datalistVital = $scope.vitalFinalList;
				for (var i = 0; i < datalistVital.length; i++) {
					$scope.vitalFinalList[i].vitalValue = '';
				}		                                                                                      
			});
			$scope.checkFocus();			                    
		}
		$scope.k();


		//	NIKITA
		// Start code
		
  		$scope.searchMedicationItems = function(searchKeyword)
		{
			if(searchKeyword.length >= 3){
				$scope.itemList=[];
				var unitId = $scope.unitId
				var orgId = $scope.organizationId
				var data={};
				
				var Arr = searchKeyword.split("|");
				searchKeyword = Arr[0];
				searchKeyword = Arr[0];
				var regex=/[^a-z]/gi;
				searchKeyword=searchKeyword.replace(regex," ");
					var URI = BASE_URL + ROOT_URL + GETMEDICATIONDETAILS + "?unitId=" + 
					unitId + "&orgId=" + orgId + "&searchDrug=" + searchKeyword
				return BillingGenericService.serviceAction(METHOD_GET,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						$scope.itemList = response.data.listObject;
						console.log("$scope.itemList",$scope.itemList);
					}
					return $scope.itemList;
				});
			}
		}		

		// get ROA for non formulary start
		$scope.getROAForNonFormulary=function(index){
			var URI = BASE_URL + ROOT_URL + "/api/opd/getRoaListNonFormulary" + "?unitId=" + 
			$scope.unitId + "&orgId=" + $scope.organizationId +"&patientId=" + $scope.encounterDetailsObj.patientId				
			GenericServiceParamHeader.serviceAction(METHOD_GET, URI).then(function(response) {
				if(response.data.status == "success"){
					$scope.roa_options1 = response.data.listObject;
					console.log("non formulary ROA="+$scope.roa_options);
				}
			});
		}
		$scope.getROAForNonFormulary();

		$scope.closeMedicationHistory = function(){
			$("#CheckNosignMedication").hide();
		}

		$scope.isNoSignificantMedication = 'N';

		$scope.addMedicationHistoryItemList = function($item, $model, $label,idx){
    		console.log("itemList",$item);
    		$scope.CheckNosignMedication = true;
    		$rootScope.startLoader();
    		idx = 0;
    		if($scope.isNoSignificantMedication =="N"){
				angular.forEach($scope.detailedMedicationsHistory, function(value, key) {
					if (value.isNoSignificantStatus == 'Y') {
						$scope.CheckNosignSurgical = false;
					}
				});
			}
			if($scope.CheckNosignSurgical){
				if($item == undefined){
					$scope.medicationHistoryDetails[idx].medicationName = $scope.medication.searchItem;
					$scope.medicationHistoryDetails[idx].isNonFormulary = 'Y';
					$scope.medicationHistoryDetails[idx].nonFormulary = 'Y';
					$scope.savePatienMedicationHistory();
				}
				else{
					$scope.medicationHistoryDetails[idx].itemId = $item.drugId;
					$scope.medicationHistoryDetails[idx].genericName = "";
					$scope.medicationHistoryDetails[idx].genericId = $item.genericId;
					$scope.medicationHistoryDetails[idx].medicationName = $item.medicationName;
					var formulationTypeId = $item.formulationTypeId;
					var URI = BASE_URL + ROOT_URL + "/api/opd/getRoaList" + "?unitId=" + 
					$scope.unitId + "&orgId=" + $scope.organizationId + "&formulationTypeId=" + formulationTypeId+ "&genericId=" + $item.genericId+ "&patientId=" +$scope.encounterDetailsObj.patientId;
				
					GenericServiceParamHeader.serviceAction(METHOD_GET, URI).then(function(response) {
						if(response.data.status == "success"){
							$rootScope.stopLoader();
							var responseList = response.data.listObject;
							$scope.medicaRoaList = responseList;
							if(response.data.object == 0)
							{
								if(response.data.listObject.length !== 0) {
									
									$scope.medicationHistoryDetails[idx].roaList = responseList;
								}
								$scope.showMedicationOptions = true
								$scope.medicationHistoryDetails[idx].genericName = $item.genericName;
								$scope.medicationHistoryDetails[idx].formulationTypeId = $item.formulationTypeId;
								$scope.medicationHistoryDetails[idx].strength = $item.strength;
								$scope.medicationHistoryDetails[idx].favorite_status = false;
							}
							else{
								$scope.checkgenericObj = {
									genericName:$item.genericName,
									formulationTypeId :$item.formulationTypeId, 
									strength :$item.strength,
									medicationIndx : idx
								}
								angular.element('#confirmAddItemPopup').modal('show');
							}
							$scope.savePatienMedicationHistory();
						}
					});
				}
				$scope.showMedicationOptions = false;
			}else{
				$scope.medicationHistoryDetails[idx].itemId = $item.drugId;
				$scope.medicationHistoryDetails[idx].genericName = "";
				$scope.medicationHistoryDetails[idx].genericId = $item.genericId;
				$scope.medicationHistoryDetails[idx].medicationName = $item.medicationName;
				$("#CheckNosignMedication").show();
			}
    	}

		$scope.removeMedicationHistory = function(medication,index) {			
			if($scope.medicationHistoryDetails.length == 1)
			{
				$scope.medicationHistoryDetails[index].searchItem="",
				$scope.medicationHistoryDetails[index].genericName='',
				$scope.medicationHistoryDetails[index].roaId="",
				$scope.medicationHistoryDetails[index].dose="",
				$scope.medicationHistoryDetails[index].searchItemFre="",
				$scope.medicationHistoryDetails[index].doseUnitId="",
				$scope.medicationHistoryDetails[index].durationUnit="Day",
				$scope.medicationHistoryDetails[index].medicationStartDate="",
				$scope.medicationHistoryDetails[index].medicationEndDate="",
				$scope.medicationHistoryDetails[index].quantity="",
				$scope.medicationHistoryDetails[index].instructionId="",
				$scope.medicationHistoryDetails[index].durationCount=""
			} 
			else if($scope.medicationHistoryDetails.length > 1)
			{		
				var indexOfMedication = $scope.medicationHistoryDetails.indexOf(medication);
				$scope.medicationHistoryDetails.splice(indexOfMedication,1);
			}
		}

		$scope.addEmptyRowHistory = function() {
			console.log("medicationHistoryDetails",$scope.medicationHistoryDetails);
			//to show empty row in Medication History 
			$scope.emptyMedicationHistoryRow = {
				itemId:"",
				medicationStartDate:'',
				genericName:'',
				itemName:'',
				genericId:'',
				doctorId:'',
				roaId:'',
				duration:'',
				dose:'',
				doseUnitId:'',
				durationUnit:'Day',
				durationCount:"",
				instructionId:'',
				medicationEndDate:'',
				medicationName:'',
				frequencyId:'',
				name:'',
				status:'A',
				authorisedDate:'',
				quantity:'',
				storeId:'',
				nonFormulary:'N',
				isStat:false,
				genericName:"",
				searchItem:"",
				isMedicationContinue:'N',
				isMedicationStopHis:'N'
			};
			$scope.medicationHistoryDetails.push($scope.emptyMedicationHistoryRow);
		}

		$scope.searchMedicationFreItemsHistory = function(searchKeyword,ind)
		{
			$scope.itemList=[];
			var unitId = $scope.unitId
			var orgId = $scope.organizationId
			
			var URI = BASE_URL + ROOT_URL + "/api/opd/getFrequencyList" + "?unitId=" + 
				unitId + "&orgId=" + orgId + "&searchPar=" + searchKeyword
			return BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == "success"){
					$scope.itemList = response.data.listObject;
				}
				if($scope.itemList.length > 0)
					{
					return $scope.itemList;
					}else{
						$scope.medicationHistoryDetails[ind].searchItemFre='';
						$scope.medicationHistoryDetails[ind].frequencyId='';
					}
				
			});
		}

		$scope.addFrequencyHistoryItemList = function($item, $model, $label,idx,info){
			console.log($item);
			$scope.storeItemHistory= $item;
			$scope.freQua = 0;
			$scope.freQua = $item.noOfAdministrationTime;
			var dur = 0;
			if(info.durationCount == '')
			{
				dur = 0;
			}
			else
			{			    			
				if(info.durationUnit == "Day")
				{
					dur = info.durationCount;
				}
				else if(info.durationUnit == "Week")
				{
					var  caldur = info.durationCount;
					dur = caldur * 7;
				}
				else if(info.durationUnit == "Month")
				{
					var  caldur = info.durationCount;
					dur = caldur * 30;
				}
				else if(info.durationUnit == "Year")
				{
					var  caldur = info.durationCount;
					dur = caldur * 365;
				}
				//$scope.medicationHistoryDetails[idx].quantity = $item.noOfAdministrationTime * dur;
			}
			info.frequencyId = ($item.frequencyId != undefined || $item.frequencyId != undefined) ? $item.frequencyId : $item.frequency_id;
			info.searchItem = $item.frequency;
			info.noOfAdministrationTime = $item.noOfAdministrationTime;
			$scope.calculateHistoryFre(idx);
			// $scope.showUpdateMedicationHistory(info);
			//$scope.medicationHistoryDetails[idx].quantity = $item.noOfAdministrationTime * dur;			    	
		}

		$scope.onStartDateChangeHistory = function(medication, index) {
			$scope.calculateHistoryEndDate(medication, index)
		}

		$scope.onDurationCountChangeHistory = function(medication) {
			$scope.calculateHistoryEndDate(medication)
		}

		$scope.calculateHistoryEndDate = function(medication, index) {
			var currentMedication = medication;
			var medicationStartDate = currentMedication.medicationStartDate;
			//$scope.start=currentMedication.medicationStartDate;
			var indexOfCurrentMedication = index;
			var durationUnit = currentMedication["durationUnit"]
								
			if(durationUnit == "Day") {
				var durationCount = parseInt(currentMedication["durationCount"]);
			}
			else if(durationUnit == "Week") {
				var durationCount = parseInt(currentMedication["durationCount"])
				var durationCount = durationCount* 7;
			}
			else if(durationUnit == "Month") {
				var durationCount = parseInt(currentMedication["durationCount"])
				var durationCount = durationCount* 30;
			}
			else if(durationUnit == "Year") {
				var durationCount = parseInt(currentMedication["durationCount"])
				var months = durationCount * 12
				var durationCount = months* 30;
			}
													
			if(durationCount && durationUnit && medicationStartDate) {
				if(durationUnit == "Day") {
					var endDate = moment(medicationStartDate).add(durationCount-1, 'days').format('YYYY-MM-DD');
				}
				else if(durationUnit == "Week") {
					var days = durationCount * 7
					var endDate = moment(medicationStartDate).add(durationCount, 'days').format('YYYY-MM-DD');
				}
				else if(durationUnit == "Month") {
					var days = durationCount * 30
					var endDate = moment(medicationStartDate).add(durationCount, 'days').format('YYYY-MM-DD');
				}
				else if(durationUnit == "Year") {
					var days = durationCount * 365;
					var endDate = moment(medicationStartDate).add(durationCount, 'days').format('YYYY-MM-DD');
				}
									
				$scope.historyDate=moment(new Date()).format('YYYY-MM-DD');
				if(currentMedication.medicationEndDate !="" && currentMedication.medicationEndDate != null){
					currentMedication["ongingMedHist"] = true;
				}else{
					currentMedication["ongingMedHist"] = false;
				}
							
				if(endDate < $scope.historyDate){			
					//currentMedication["medicationEndDate"] = endDate;
										
					if(currentMedication.medicationEndDate != "" && currentMedication.medicationEndDate != null){		
						if(currentMedication.medicationEndDate < medicationStartDate){					
							growl.warning("Enter end date greater than start date according to duration..!",{
								title : 'warning!'
							});
							currentMedication["ongingMedHist"] = false;
							$scope.medicationHistoryDetails[indexOfCurrentMedication].quantity ="";
							$scope.medicationHistoryDetails[indexOfCurrentMedication].medicationEndDate='';
							return false;
						}
										
						if(endDate != moment(currentMedication.medicationEndDate).format('YYYY-MM-DD')){
							$scope.showEndDate = moment(endDate).toDate();
							growl.warning("According to your duration,start date and quantity end date must be  "+moment($scope.showEndDate).format('DD/MM/YYYY')+"...!",{
								title : 'warning!'
							});	
							currentMedication["ongingMedHist"] = false;
							$scope.medicationHistoryDetails[indexOfCurrentMedication].quantity ="";
							$scope.medicationHistoryDetails[indexOfCurrentMedication].medicationEndDate=moment(endDate).toDate();
							return false;
						}
					}else{
						$scope.medicationHistoryDetails[indexOfCurrentMedication].quantity ="";
					}
					currentMedication["startDate"] = medicationStartDate;
					currentMedication["isMedicationContinue"] = 'N';
					//currentMedication.start= moment(medicationStartDate).format('YYYY-MM-DD');
					$scope.medicationHistoryDetails[indexOfCurrentMedication] = currentMedication
					$scope.model=currentMedication.searchItemFre;
					$scope.label=currentMedication.searchItemFre;
					$scope.idx=indexOfCurrentMedication;
					// $scope.addFrequencyHistoryItemList(currentMedication,$scope.model,$scope.label,$scope.idx,medication);
				}
				else{
					if(currentMedication.medicationEndDate != "" && currentMedication.medicationEndDate != null){						
						if(currentMedication.medicationEndDate < medicationStartDate){
							growl.warning("Enter end date greater than start date according to duration..!",{
								title : 'warning!'
							});
							currentMedication["ongingMedHist"] = false;
							$scope.medicationHistoryDetails[indexOfCurrentMedication].quantity ="";
							$scope.medicationHistoryDetails[indexOfCurrentMedication].medicationEndDate='';
							return false;
						}
					}
					currentMedication["ongingMedHist"] = false;
					// $scope.medicationHistoryDetails[indexOfCurrentMedication].isMedicationContinue ='Y';
					//$scope.medicationHistoryDetails[indexOfCurrentMedication].quantity ="";
					//$scope.medicationHistoryDetails[indexOfCurrentMedication].medicationStartDate ="";
				}						
			}
		}

		$scope.calculateHistoryFre = function(idx)
		{
			if($scope.detailedMedicationsHistory[idx].medicationEndDate != "" && $scope.detailedMedicationsHistory[idx].medicationEndDate != null){
				$scope.detailedMedicationsHistory[idx].quantity = $scope.detailedMedicationsHistory[idx].noOfAdministrationTime * $scope.detailedMedicationsHistory[idx].durationCount;
				$scope.showUpdateMedicationHistory($scope.detailedMedicationsHistory[idx]);
			}
		}

		$scope.instructionList = function() {
			var commonObj = {
				isMedication : 'Y',
				organizationId : $scope.organizationId
			}

			var URI = BASE_URL + ROOT_URL + GETMEDICATIONINSTRUCTIONS;
	
			BillingGenericService.serviceAction(METHOD_POST, URI, commonObj,NOTIFICATION_MSG_STATUS_FALSE).then(
			function(response) {
				console.log(response);
				// $rootScope.startLoader();
				if (response.data.status == "success")
					$scope.medicationInstructions = response.data.listObject;
			});
		}
		$scope.instructionList();

		$scope.doseListData = function() {
			var URI =BASE_URL + ROOT_URL +"/api/globals/strength/units/"+$scope.organizationId;
			GenericService.serviceAction("GET", URI, {}).then(
			function(response) {
				$scope.doseList = [];
				$scope.doseList = response.data;
			});
		}
		$scope.doseListData();

		$scope.noSignificantMedicationHist = "N";
		$scope.savePatienMedicationHistory = function() {	
			$rootScope.startLoader();
			console.log("medicationHistoryDetails",$scope.medicationHistoryDetails);				
			var medicationHistoryDetails = $scope.medicationHistoryDetails;
			var medicationHistoryList = [];
			var data = 
			{
				"organizationId": $scope.organizationId,
				"unitId": $scope.unitId,
				"encounterId":$scope.encounterDetailsObj.encounterId,
				"patientId":$scope.encounterDetailsObj.patientId,
			};
													
			if($scope.noSignificantMedicationHist == "N"){						
				for (var i=0 ; i< medicationHistoryDetails.length; i++){
					if( medicationHistoryDetails.length > 0 )
					{	
						if(medicationHistoryDetails[i].medicationName== "" && medicationHistoryDetails[i].nonFormulary == "N"){
							growl.warning("Please select Brand Name and Route Of Administration ...!!",{
								title : 'Warning!'
							});
							$rootScope.stopLoader();
							return;
						}
										 
						if(medicationHistoryDetails[i].medicationName == "" && medicationHistoryDetails[i].nonFormulary == "Y"){
							growl.warning("Please Enter Brand Name..!",{
								title : 'Warning!'
							});
							$rootScope.stopLoader();
							return;
						}
						var medicationStartDate = moment(medicationHistoryDetails[i].medicationStartDate).format('DD-MM-YYYY');
						var instructionId = medicationHistoryDetails[i].instructionId;
									
						var medicationDetailsHistoryObj = {};
						medicationDetailsHistoryObj["itemId"] = medicationHistoryDetails[i].itemId;
						medicationDetailsHistoryObj["storeId"] = 1; 
						medicationDetailsHistoryObj["orderQty"] = medicationHistoryDetails[i].quantity;
						medicationDetailsHistoryObj["doctorId"] = $scope.encounterDetailsObj.doctorId;
						medicationDetailsHistoryObj["genericName"] = medicationHistoryDetails[i].genericName;
						medicationDetailsHistoryObj["genericId"] = medicationHistoryDetails[i].genericId;
						medicationDetailsHistoryObj["duration"] = medicationHistoryDetails[i].durationCount;
						medicationDetailsHistoryObj["itemName"] = medicationHistoryDetails[i].medicationName;
						medicationDetailsHistoryObj["authorisedDate"] = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
						medicationDetailsHistoryObj["doseId"] = medicationHistoryDetails[i].dose;
						medicationDetailsHistoryObj["doseUnitId"] = medicationHistoryDetails[i].doseUnitId;
						medicationDetailsHistoryObj["frequencyId"] = medicationHistoryDetails[i].frequencyId;
						medicationDetailsHistoryObj["durationUnit"] = medicationHistoryDetails[i].durationUnit;
						medicationDetailsHistoryObj["roaId"] = medicationHistoryDetails[i].roaId;
						medicationDetailsHistoryObj["startDate"] = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
						medicationDetailsHistoryObj["endDate"] = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
						medicationDetailsHistoryObj["createdDate"] = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
						medicationDetailsHistoryObj["updatedDate"] = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
						medicationDetailsHistoryObj["createdBy"] =$scope.userId;
						medicationDetailsHistoryObj["updatedBy"] = $scope.userId;
						medicationDetailsHistoryObj["status"] = 'A';
						medicationDetailsHistoryObj["instructionId"] = medicationHistoryDetails[i].instructionId;
						medicationDetailsHistoryObj["isNonFormulary"] = medicationHistoryDetails[i].nonFormulary;
						// medicationDetailsHistoryObj["nonFormulary"] = medicationHistoryDetails[i].nonFormulary;
						medicationDetailsHistoryObj["isMedicationContinue"] = medicationHistoryDetails[i].isMedicationContinue;
						medicationDetailsHistoryObj["isMedicationStopHis"] = medicationHistoryDetails[i].isMedicationStopHis;
						medicationDetailsHistoryObj["isNoSignificantMedication"] = $scope.noSignificantMedicationHist;
						medicationHistoryList.push(medicationDetailsHistoryObj);
					}
					else
					{
						growl.warning("Please Enter all fields",{
							title : 'Warning!'
						});
						$rootScope.stopLoader();
						return;
					}
				}
			}
			else
			{		       		
				$scope.significantMedicationHistory = true;
				angular.forEach($scope.detailedMedicationsHistory , function(value,key){
					if(value.isNoSignificantMedication == 'Y'){
						$scope.significantMedicationHistory = false;
					}
				});
								
				if($scope.significantMedicationHistory){	
					for (var i=0 ; i< medicationHistoryDetails.length; i++){
						var medicationStartDate = moment(medicationHistoryDetails[i].medicationStartDate).format('DD-MM-YYYY');
						var instructionId = medicationHistoryDetails[i].instructionId
						var medicationDetailsHistoryObj = {};
						medicationDetailsHistoryObj["itemId"] = medicationHistoryDetails[i].itemId;
						medicationDetailsHistoryObj["storeId"] = 1; 
						medicationDetailsHistoryObj["orderQty"] = medicationHistoryDetails[i].quantity;
						medicationDetailsHistoryObj["doctorId"] = $scope.encounterDetailsObj.doctorId;
						medicationDetailsHistoryObj["genericName"] = medicationHistoryDetails[i].genericName;
						medicationDetailsHistoryObj["genericId"] = medicationHistoryDetails[i].genericId;
						medicationDetailsHistoryObj["duration"] = medicationHistoryDetails[i].durationCount;
						medicationDetailsHistoryObj["itemName"] = medicationHistoryDetails[i].medicationName;
						medicationDetailsHistoryObj["authorisedDate"] = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
						medicationDetailsHistoryObj["doseId"] = medicationHistoryDetails[i].dose;
						medicationDetailsHistoryObj["doseUnitId"] = medicationHistoryDetails[i].doseUnitId;
						medicationDetailsHistoryObj["frequencyId"] = medicationHistoryDetails[i].frequencyId;
						medicationDetailsHistoryObj["durationUnit"] = medicationHistoryDetails[i].durationUnit;
						medicationDetailsHistoryObj["roaId"] = medicationHistoryDetails[i].roaId;
						medicationDetailsHistoryObj["startDate"] = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
						medicationDetailsHistoryObj["endDate"] = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
						medicationDetailsHistoryObj["createdDate"] = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
						medicationDetailsHistoryObj["updatedDate"] = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
						medicationDetailsHistoryObj["createdBy"] =$scope.userId;
						medicationDetailsHistoryObj["updatedBy"] = $scope.userId;
						medicationDetailsHistoryObj["status"] = 'A';
						medicationDetailsHistoryObj["instructionId"] = medicationHistoryDetails[i].instructionId;
						medicationDetailsHistoryObj["isNonFormulary"] = medicationHistoryDetails[i].nonFormulary;
						// medicationDetailsHistoryObj["nonFormulary"] = medicationHistoryDetails[i].nonFormulary;
						medicationDetailsHistoryObj["isMedicationContinue"] = medicationHistoryDetails[i].isMedicationContinue;
						medicationDetailsHistoryObj["isMedicationStopHis"] = medicationHistoryDetails[i].isMedicationStopHis;
						medicationDetailsHistoryObj["isNoSignificantMedication"] = $scope.noSignificantMedicationHist;
						medicationHistoryList.push(medicationDetailsHistoryObj);
					}
				}
				else
				{
					growl.warning("You have already saved.!!!",
					{
						title : 'Warning!'
					});
					$rootScope.stopLoader();
					$scope.noSignificantMedicationHist = "N";
					return false;
				}
			}
							
			data.medicationHistoryList = medicationHistoryList;
			console.log("medicationHistoryList",JSON.stringify(data));
			var URI = BASE_URL + ROOT_URL + "/api/opd/saveMedicationHistory"
								
			GenericServiceParamHeader.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_TRUE)
			.then(function(response) {
				if(response.data.status == "success"){
					$scope.getDetailedMedicationHistoryList();
					$scope.getContinuedMedicationHistoryList();
					$scope.noSignificantVaccination='N';
					$("#CheckNosignMedication").hide();
					$scope.medication.searchItem = "";
					$scope.getAllActiveMedication();
					$scope.getMedicationListForMedicalHistory();
					// $scope.setSecondTabLevel(4);
					$rootScope.stopLoader();
												
				}
			});
		}

		$scope.getDetailedMedicationHistoryList = function()
		{
			$scope.detailedMedicationsHistory = [];
			var patientId = $scope.encounterDetailsObj.patientId;
			var dateObj = moment(new Date()).format('YYYY-MM-DD');
			var data={
				organizationId:$scope.organizationId,
				unitId:$scope.unitId,
				patientId:$scope.encounterDetailsObj.patientId		
			};
			var URI = BASE_URL + ROOT_URL + "/api/opd/getMedicationHistoryList";
			GenericServiceParamHeader.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success") {
					angular.forEach(response.data.listObject, function(l) {
						l.medicationStartDate = moment(l.startDate, "DD-MM-YYYY").toDate();
						l.medicationEndDate = moment(l.endDate, "DD-MM-YYYY").toDate();
					});
					$scope.detailedMedicationsHistory = response.data.listObject;
					$scope.roaList();
					$scope.getROAForNonFormulary();
				}
			});
		}
		$scope.getDetailedMedicationHistoryList();

		$scope.roaList = function() {
			angular.forEach($scope.detailedMedicationsHistory, function(d) {
				if(d.isNoSignificantMedication == "Y"){
					d.itemName = "No Significant Medication History";
				}
				d.dose = d.doseId;
				d.searchItemFre = d.frequencyDesc;
				d.durationCount = d.duration;
				d.quantity = d.orderQty;
				if(d.instructionId){
					d.instructionId = d.instructionId.toString();
				}
				if(d.doseUnitId){
					d.doseUnitId = d.doseUnitId.toString();
				}
				if(d.formulationTypeId){
					var URI = BASE_URL + ROOT_URL + "/api/opd/getRoaList" + "?unitId=" + 
					$scope.unitId + "&orgId=" + $scope.organizationId + "&formulationTypeId=" + d.formulationTypeId+ "&genericId=" + d.genericId+ "&patientId=" +$scope.encounterDetailsObj.patientId;
					GenericServiceParamHeader.serviceAction(METHOD_GET, URI).then(function(response) {
						if(response.data.status == "success"){
							$rootScope.stopLoader();
							var responseList = response.data.listObject;
							$scope.medicaRoaList = responseList;
							d.roaList = responseList;
						}
					});
				}
			});
		}

		$scope.getFrequentlyUsedMedications = function(){
			// $rootScope.startLoader();

			var data = {
				doctorId: $scope.encounterDetailsObj.doctorId,
				unitId: $scope.unitId,
				organizationId:$scope.organizationId,
			};
			$scope.getFrequentlyUsedMedications=[];
			var URI = BASE_URL + ROOT_URL + "/api/opd/getFrequentlyUsedMedicationList";
			console.log("URI", URI);
			GenericService.serviceAction("POST", URI,data).then(function(response) {
				if(response.data.status == "success"){
					$scope.frequentlyUsedMedicationList = response.data.listObject;
					// $rootScope.stopLoader();
				}
			});
		}
		$scope.getFrequentlyUsedMedications();

		$scope.getContinuedMedicationHistoryList = function() {
			$scope.continuedMedicationHistoryList = [];
			var data = {
				organizationId:$scope.organizationId,
				unitId:$scope.unitId,
				patientId:$scope.encounterDetailsObj.patientId,
			};
			var URI = BASE_URL + ROOT_URL + "/api/opd/getContinuedMedicationList";
			GenericServiceParamHeader.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(
			function(response) {
				if (response.data.status == "success") {
					$scope.continuedMedicationHistoryList = response.data.listObject;
					angular.forEach($scope.continuedMedicationHistoryList,function(value,key){
						value.checkTrue='N';
					});
				}
			});
		}
		$scope.getContinuedMedicationHistoryList();

		$scope.getAllActiveMedication = function()
        {
        	$scope.activeMedications = [];
        	var patientId = $scope.encounterDetailsObj.patientId;
    		var dateObj = moment(new Date()).format('YYYY-MM-DD');
    		/*
			 * var date = ("0" + dateObj.getDate()).slice(-2) + "-" +
			 * ("0"+(dateObj.getMonth()+1)).slice(-2) +"-" +
			 * dateObj.getFullYear();
			 */
    		
    		var URI = BASE_URL + ROOT_URL + GETACTIVEMEDICATION + "?patientId=" + patientId + "&date=" + dateObj+ "&unitId="+ $scope.unitId
    		GenericServiceParamHeader.serviceAction(METHOD_GET, URI).then(function(response) {
				if (response.data.status == "success") {
					$scope.activeMedications = response.data.listObject;
					angular.forEach($scope.activeMedications,function(value,key){
						value.checkTrue='N';
						if(value.strengthDesc == null){
							value.strengthDesc  = "";
						}
					});
				}
			});
        }
        $scope.getAllActiveMedication();

		$scope.deleteMedicationHistory = function(info){
			$rootScope.startLoader();
			try{
				var data={
					medicationHistoryId:info.medicationHistoryId,
					organizationId:$scope.organizationId,
					unitId:$scope.unitId,
				};
									
				var URI = BASE_URL + ROOT_URL + "/api/opd/deleteMedicationHistory";
				GenericService.serviceAction("POST",URI,data).then(function(response){
					if (response.data.status == 'success')
					{
						$rootScope.stopLoader();
						growl.success(response.data.message,
						{
							title : 'Success!'
						});
						$scope.getDetailedMedicationHistoryList();
						$scope.getContinuedMedicationHistoryList();
						// $scope.noSignificantVaccination='N';
						$scope.getAllActiveMedication();
						// $scope.$broadcast('show-errors-reset');
					} 
					else if(response.data.status == 'error')
					{
						$rootScope.stopLoader();
						growl.warning(response.data.message,
						{
							title : 'Warning!'
						});
					}
				});				
			}catch(e){
				console.log("Exception",e);
			}
		}

		$scope.getDoctorBySpecialityMedication = function(id) {
        	//alert();
			var data = {
				specialityId : id,
				unitId : $scope.unitId,
				organizationId : $scope.organizationId
			};
			var URI = BASE_URL + ROOT_URL + GETDOCTORBYSPECIALITYID;
			BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(
			function(response) {
				console.log(response);
				$scope.doctorListMedication = [];
				// $rootScope.startLoader();
				if (response.data.status == "success")
				$scope.doctorListMedication = response.data.listObject;
			});
		}

		$scope.getSpecialityId=function(medicationSpecialityId){
			$scope.spl=medicationSpecialityId;
		}

		$scope.getDoctorId=function(medicationDoctorId){
			$scope.doc=medicationDoctorId;
		}

		$scope.searchMedicationEncounter = function()
		{
			$scope.medicationEncounterListSearch=[];
			$rootScope.startLoader();
			if($scope.medicationVisitTypeId==1)
			{
				$scope.visitTypeDesc = "OP";
			}
			if($scope.medicationVisitTypeId==2)
			{
				$scope.visitTypeDesc = "IP";
			}
			if($scope.medicationVisitTypeId==3)
			{
				$scope.visitTypeDesc = "Day Care";
			}
			if($scope.medicationVisitTypeId==4)
			{
				$scope.visitTypeDesc = "ER";
			}
        	
			var medicationData = {
				organizationId:$scope.organizationId,
				unitId : $scope.unitId,
				visitTypeId : $scope.medicationVisitTypeId,
				specialityId : $scope.spl,
				doctorId : $scope.doc,
				encounterId : $scope.encounterDetailsObj.encounterId,
				patientId : $scope.encounterDetailsObj.patientId,
			}
			
        	// For Get Medication Encounter
			/*var URI = BASE_URL + ROOT_URL
				+ GETMEDICATIONENCOUNTERLIST;*/
			var URI = BASE_URL + ROOT_URL + "/api/opd/getListOfLastEncounterAndAdmission";
			BillingGenericService.serviceAction(METHOD_POST, URI, medicationData, NOTIFICATION_MSG_STATUS_FALSE)
			.then(function(response) {
				console.log(response);
				// $rootScope.startLoader();
				if (response.data.status == "success")
					$scope.medicationEncounterListSearch = response.data.listObject;
					$rootScope.stopLoader();
					//console.log("gg",$scope.medicationEncounterList);
			});
        }

		$scope.getMedicationListByEncounter = function(info)
        {
        	var item =info;
			//console.log(item);
        	$scope.medicationHistoryList = [];
        	var URI = BASE_URL + ROOT_URL + "/api/opd/getMedicationHistoryByEncounterId" + "?encounterId=" + item.encounterId + "&stopDate=" + moment(new Date()).format('DD-MM-YYYY');
    		BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
    			if(response.data.status == "success"){
    				$scope.medicationListByEncounter = response.data.listObject;
    				console.log("mmmmmmmm",$scope.medicationListByEncounter);
    			}
    		});    		
        }

		$scope.validate = function(info) {
			var isValid = true;
			if(!info.searchItemFre){
				growl.error("Please enter frequency..!");
				isValid = false;
			}
			if(!info.durationCount || !info.durationUnit){
				growl.error("Please enter duration..!");
				isValid = false;
			}
			if(!info.medicationStartDate){
				growl.error("Please enter start date..!");
				isValid = false;
			}
			return isValid;
		}

		$scope.showUpdateMedicationHistory = function(info) {
			if($scope.validate(info)){
				$scope.medicationHistoryDetails = {
					itemId: info.itemId,
					medicationStartDate: info.medicationStartDate,
					genericName: info.genericName,
					itemName: info.itemName,
					genericId: info.genericId,
					doctorId: info.doctorId,
					roaId: info.roaId,
					durationCount: info.durationCount,
					medicationName: info.medicationName,
					dose: info.dose,
					doseUnitId: info.doseUnitId,
					durationUnit: info.durationUnit,
					instructionId: info.instructionId,
					medicationEndDate: info.medicationEndDate,
					frequencyId: info.frequencyId,
					name: info.name,
					status:'A',
					authorisedDate:'',
					quantity: info.quantity,
					storeId:'',
					nonFormulary: info.isNonFormulary,
					genericName: info.genericName,
					searchItem: info.searchItem,
					ongingMedHist:false,
					isMedicationContinue: info.isMedicationContinue,
					isMedicationStopHis:info.isMedicationStopHis,
					medicationHistoryId: info.medicationHistoryId,
					noOfAdministrationTime: info.noOfAdministrationTime
				}
				$scope.updatePatienMedicationHistory();
			}
		}

		$scope.updatePatienMedicationHistory = function() {
			$rootScope.startLoader();
			var medicationHistoryDetails = $scope.medicationHistoryDetails;
			var medicationHistoryList = [];
			var data = {
				"organizationId": $scope.organizationId,
				"unitId": $scope.unitId,
				"encounterId":$scope.encounterDetailsObj.encounterId,
				"patientId":$scope.encounterDetailsObj.patientId,
			};

			$scope.significantMedicationHistory = true;
			angular.forEach($scope.detailedMedicationsHistory , function(value,key){
				if(value.isNoSignificantMedication == 'N'){
					var medicationStartDate = moment(medicationHistoryDetails.medicationStartDate).format('DD-MM-YYYY');
					var instructionId = medicationHistoryDetails.instructionId
					var medicationDetailsHistoryObj = {};
					medicationDetailsHistoryObj["medicationHistoryId"] = medicationHistoryDetails.medicationHistoryId;
					medicationDetailsHistoryObj["itemId"] = medicationHistoryDetails.itemId;
					medicationDetailsHistoryObj["storeId"] = 1;
					medicationDetailsHistoryObj["orderQty"] = medicationHistoryDetails.quantity;
					medicationDetailsHistoryObj["doctorId"] = $scope.encounterDetailsObj.doctorId;
					medicationDetailsHistoryObj["genericName"] = medicationHistoryDetails.genericName;
					medicationDetailsHistoryObj["genericId"] = medicationHistoryDetails.genericId;
					medicationDetailsHistoryObj["duration"] = medicationHistoryDetails.durationCount;
					medicationDetailsHistoryObj["itemName"] = medicationHistoryDetails.itemName;
					medicationDetailsHistoryObj["authorisedDate"] = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
					medicationDetailsHistoryObj["doseId"] = medicationHistoryDetails.dose;
					medicationDetailsHistoryObj["doseUnitId"] = medicationHistoryDetails.doseUnitId;
					medicationDetailsHistoryObj["frequencyId"] = medicationHistoryDetails.frequencyId;
					medicationDetailsHistoryObj["durationUnit"] = medicationHistoryDetails.durationUnit;
					medicationDetailsHistoryObj["roaId"] = medicationHistoryDetails.roaId;
					medicationDetailsHistoryObj["startDate"] = moment(medicationHistoryDetails.medicationStartDate).format('DD-MM-YYYY HH:mm:ss');
					medicationDetailsHistoryObj["endDate"] = moment(medicationHistoryDetails.medicationEndDate).format('DD-MM-YYYY HH:mm:ss');
					medicationDetailsHistoryObj["createdDate"] = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
					medicationDetailsHistoryObj["updatedDate"] = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
					medicationDetailsHistoryObj["createdBy"] =$scope.userId;
					medicationDetailsHistoryObj["updatedBy"] = $scope.userId;
					medicationDetailsHistoryObj["status"] = 'A';
					medicationDetailsHistoryObj["instructionId"] = medicationHistoryDetails.instructionId;
					medicationDetailsHistoryObj["isNonFormulary"] = medicationHistoryDetails.nonFormulary;
					medicationDetailsHistoryObj["isMedicationContinue"] = medicationHistoryDetails.isMedicationContinue;
					medicationDetailsHistoryObj["isMedicationStopHis"] = medicationHistoryDetails.isMedicationStopHis;
					medicationDetailsHistoryObj["isNoSignificantMedication"] = $scope.noSignificantMedicationHist;
					medicationHistoryList.push(medicationDetailsHistoryObj);
				}
				// else{
				// 	growl.warning("You have already saved.!!!",
				// 	{
				// 		title : 'Warning!'
				// 	});
				// 	$rootScope.stopLoader();
				// 	$scope.noSignificantMedicationHist = "N";
				// 	return false;
				// }
			});
			data.medicationHistoryList = medicationHistoryList;
			console.log("medicationHistoryList",JSON.stringify(data));
			var URI = BASE_URL + ROOT_URL + "/api/opd/updateMedicationHistory"
			GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data, NOTIFICATION_MSG_STATUS_TRUE)
			.then(function(response) {
				if(response.data.status == "success"){
					$scope.medicationHistoryDetails = [];
					$scope.emptyMedicationHistoryRow = {
						itemId:"",
						medicationStartDate:'',
						genericName:'',
						itemName:'',
						genericId:'',
						doctorId:'',
						roaId:'',
						duration:'',
						dose:'',
						doseUnitId:'',
						durationUnit:'Day',
						durationCount:"",
						instructionId:'',
						medicationEndDate:'',
						medicationName:'',
						frequencyId:'',
						name:'',
						status:'A',
						authorisedDate:'',
						quantity:'',
						storeId:'',
						nonFormulary:'N',
						genericName:"",
						searchItem:"",
						isMedicationContinue:'N',
						isMedicationStopHis:'N'
					};
					$scope.medicationHistoryDetails.push($scope.emptyMedicationHistoryRow);
					$scope.getDetailedMedicationHistoryList();
					$scope.getContinuedMedicationHistoryList();
					$scope.noSignificantVaccination='N';
					$scope.getAllActiveMedication();
					// $scope.setSecondTabLevel(4);
					$rootScope.stopLoader();
				}
			});
		}

		$scope.onDurationUnitChangeHistory = function(medication) {
			$scope.calculateHistoryEndDate(medication)
		}

		$scope.openDatePickerMedicationHistory = function(index) {
			$scope.datepickerMedicationHistory = {
				opened : [ false ],
			}
			if($scope.detailedMedicationsHistory != undefined){
				angular.forEach($scope.detailedMedicationsHistory, function(val) {
					$scope.datepickerMedicationHistory.opened.push(false);
				});
			}
			for (var i = $scope.detailedMedicationsHistory.length - 1; i >= 0; i--) {
				if (i === index) {
					$scope.datepickerMedicationHistory.opened[i] = true;
				} else {
					$scope.datepickerMedicationHistory.opened[i] = false;
				}
			}
		}

		$scope.openDatePickerMedicationHistoryEndDate = function(index) {
			$scope.datepickerMedicationHistoryEndDate = {
				opened : [ false ],
			}
			if($scope.detailedMedicationsHistory != undefined){
				angular.forEach($scope.detailedMedicationsHistory, function(val) {
					$scope.datepickerMedicationHistoryEndDate.opened.push(false);
				});
			}
			for (var i = $scope.detailedMedicationsHistory.length - 1; i >= 0; i--) {
				if (i === index) {

					$scope.datepickerMedicationHistoryEndDate.opened[i] = true;
				} else {
					$scope.datepickerMedicationHistoryEndDate.opened[i] = false;
				}
			}
		}

		$scope.dateOptionsMedicationHistoryEndDate = {
			formatYear : 'yyyy',
			showWeeks : false,
			maxDate:new Date(), //max 
		};

		$scope.dateOptionsMedicationHistory = {
			formatYear : 'yyyy',
			showWeeks : false,
			maxDate:new Date(), //max 
		};

		$scope.stopMedication=function(item){
			$scope.prescriptionDetailsid=item.prescriptionDetailsid;	
		}

		$scope.stopMedicationHis=function(item){
			$scope.medicationHistoryId=item.medicationHistoryId;	
		}
		
		$scope.cancelMedication = function(justificationStop)
		{
			$scope.justificationStop=justificationStop;
			$rootScope.startLoader();
			try{
				//$rootScope.startLoader();
				var date = new Date();
				$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
				var data={
					updatedBy:$scope.userId,
					updatedDate:$scope.dt,
					isMedicationStop:'Y',
					justificationStop:$scope.justificationStop,
					prescriptionDetailsid:$scope.prescriptionDetailsid,				
				};
				
				if(justificationStop != null && justificationStop !="" && justificationStop != undefined)
				{
					var URI = BASE_URL + ROOT_URL + "/api/opd/saveJustificationForMedicationStop";
					GenericService.serviceAction("POST",URI,data).then(function(response)
					{
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
							{
								title : 'Success!'
							});
							$scope.getAllActiveMedication();
							$scope.getDetailedMedication();
							$scope.getMedicationListForMedicalHistory();
							$scope.justificationStop=" ";
							//$scope.$broadcast('show-errors-reset');
						} 
						else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.warning(response.data.message,
							{
								title : 'Warning!'
							});
						}
					});
				}else{
					growl.warning("Please enter reason to discontinue medication.  ",
					{
						title : 'warning!'
					});
					$rootScope.stopLoader();
				}
			}catch(e){
				console.log("Exception",e);
			}
		}
		// end cancel

		$scope.getMedicationListForMedicalHistory = function()
		{
			$scope.medicationListForMedicalHistoryList = [];
	      	var headerObj = {
				unitId : $scope.unitId,
				organizationId : $scope.organizationId,
				prescriptionDate : moment(new Date()).format('YYYY-MM-DD')
			};
		
			var paramObjForPer = {
				patientId : $scope.encounterDetailsObj.patientId,
				encounterId : $scope.encounterDetailsObj.encounterId,
			};
			 
			var URI = BASE_URL + ROOT_URL + "/api/opd/getMedicationDetailsForMedicationHistory";
			GenericServiceParamHeader.serviceAction(METHOD_GET, URI, {} ,NOTIFICATION_MSG_STATUS_FALSE,paramObjForPer,headerObj)
			.then(function(response) {
				if (response.data.status == "success") {
					$scope.medicationListForMedicalHistoryList = response.data.listObject;
					angular.forEach($scope.medicationListForMedicalHistoryList, function(value) {
						value.searchItem = value.itemName;
					});
					console.log("medicationListForMedicalHistoryList", $scope.medicationListForMedicalHistoryList)
				}
			});
		}
		$scope.getMedicationListForMedicalHistory();

		$scope.cancelMedicationHistory = function(justificationStopHis){
			$scope.justificationStopHis=justificationStopHis;
			$rootScope.startLoader();
			try{
				//$rootScope.startLoader();
				var date = new Date();
				$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
				var data={
					updatedBy:$scope.userId,
					updatedDate:$scope.dt,
					isMedicationStopHis:'Y',
					justificationStopHis:$scope.justificationStopHis,
					medicationHistoryId:$scope.medicationHistoryId,	
				};
				
				if(justificationStopHis != null && justificationStopHis !="" && justificationStopHis != undefined){
					var URI = BASE_URL + ROOT_URL + "/api/opd/saveJustificationForMedicationStopForHistory";
					GenericService.serviceAction("POST",URI,data).then(function(response){
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
							{
								title : 'Success!'
							});
							$scope.getAllActiveMedication();
							$scope.getDetailedMedication();
							$scope.getMedicationListForMedicalHistory();
							$scope.getContinuedMedicationHistoryList();
							$scope.getDetailedMedicationHistoryList();
							$scope.justificationStopHis="";
						} 
						else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.warning(response.data.message,
							{
								title : 'Warning!'
							});
						}
					});
				}
				else{
					growl.warning(" Please enter reason to discontinue medication. ",
					{
						title : 'warning!'
					});
					$rootScope.stopLoader();
				}
			}catch(e){
				console.log("Exception",e);
			}
		}
		// end cancel history
		  //for vital growth chart graph
		  $scope.getVitalList = function() {
				$scope.startLoader();
				
				  var headerObj = {
                        unit_id : $scope.unitId,
                        organization_id : $scope.organizationId
                    };
                
                var paramObjForPer = {
                };
                
                var data ={};
				
				var URI = BASE_URL + ROOT_URL
						+ GETVITALMASTERLIST;
				GenericServiceParamHeader
              .serviceAction(METHOD_GET, URI, data,
                      NOTIFICATION_MSG_STATUS_FALSE,
                      paramObjForPer, headerObj)
						.then(
								function(response) {
									console.log(response);
									// $rootScope.startLoader();
									if (response.data.status == "success")
										$scope.vitalsList = response.data.listObject;
									$scope.printListForVitals();
									console.log("Vital List", $scope.vitalsList);
									
								});
				$scope.stopLoader();
			}
			$scope.getVitalList();
		  $scope.printListForVitals=function(){
            $scope.vitalsListForPrint=[];
              angular.forEach($scope.vitalsList, function(val, key){
                  if(val.vitalId == 1 || val.vitalId == 2 || val.vitalId == 15){
                      $scope.vitalsListForPrint.push(val); 
                  }
              });
              console.log( "hhhhhhhh",$scope.vitalsListForPrint);
        }
         
         $scope.printVitalGraph=function(){
            $('#myIframe').removeAttr('src');
            var data = {
                     "appName": "HIS",
                     "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                     "reportDesignFilePath": "GrowthChart.rpt",
                     "reportFormat": "PDF",
                     "exportFileName": "Coversheet",
                     "reportParams": [
                         {
                              "paramName": "vital_id",
                              "paramType": "String",
                              "paramValue": $scope.printVitalId.toString()
                        },
                        {
                            "paramName": "patient_id",
                            "paramType": "Integer",
                            "paramValue": $scope.encounterDetailsObj.patientId
                      }
             ]
            
  };
            var URI =REPORT_API_URI1;
             var form = null;
             
             form = document.createElement("form");
             form.setAttribute("id", "reportId");
             form.setAttribute("name", "formReportBook");
             form.setAttribute("method", "post");
             form.setAttribute("action", URI);
             form.setAttribute("target", "myIframeGrowth");
             
             var hiddenField1 = document.createElement("input"); 
             hiddenField1.setAttribute("type", "hidden");
             hiddenField1.setAttribute("name", "reportParam");
             hiddenField1.setAttribute("value", JSON.stringify(data));
             
             var hiddenField2 = document.createElement("input"); 
             hiddenField2.setAttribute("type", "hidden");
             hiddenField2.setAttribute("name", "orgId");
             hiddenField2.setAttribute("value",$scope.organizationId);
             
             var hiddenField3 = document.createElement("input"); 
             hiddenField3.setAttribute("type", "hidden");
             hiddenField3.setAttribute("name", "unitId");
             hiddenField3.setAttribute("value", $scope.unitId);
             
             form.appendChild(hiddenField1);
             form.appendChild(hiddenField2);
             form.appendChild(hiddenField3);
             
            
             document.body.appendChild(form);
             $("form[name='formReportBook']").submit();
             $rootScope.stopLoader();
        }
         
         $timeout( function(){
          	
        	 var headerObj = {
					    unit_id : $scope.unitId,
						organization_id : $scope.organizationId
					     };
        	 
        	 var paramObjForVital = {
           				
                		   unitId:$scope.unitId,
               				organizationId:$scope.organizationId,
               				patientId:$scope.encounterDetailsObj.patientId,
           		}
        	 var data={
    			 	   unitId:$scope.unitId,
       				   organizationId:$scope.organizationId,
       				   patientId:$scope.encounterDetailsObj.patientId,
    	 			}
           		console.log("jj",paramObjForVital);
           		// return false;
                   var URI = BASE_URL + ROOT_URL
                   + "/api/opd/getListOfVitalsByPatientIdForCoversheet";
            GenericServiceParamHeader
                .serviceAction(METHOD_POST, URI,
                        data,
                        NOTIFICATION_MSG_STATUS_FALSE,data, paramObjForVital,headerObj)
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
                    	// $scope.onClick();
                console.log("patientVitalList",response.data.listObject);
                console.log("vitalList",$scope.vitalsList);
                
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
								$scope.newArr.checkQualifier = true;
                    		 	if($scope.roleId == 2)
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
                                    		 	if(value.vitalValue != undefined ){
                                    		 		newArr.vitalValue = value.vitalValue;
                                    		 	}else{
                                    		 		newArr.vitalValue =""
                                    		 	}
//newArr.vitalValue = "";
                                    		 	newArr.isReviewedFlag = 'N';
                                    		 	newArr.vitalOrderOpd  = value.vitalOrderOpd;
                                    			newArr.checkQualifier = true;
                                    		 	if($scope.roleId == 2)
                                		 		{
                                    		 		newArr.appointmentVitalMapperId ='';
                                		 		}
												$scope.vitalFinalList.push(newArr);
											}
                	       
                    });
                 
                
                 console.log("Final Vital Arr",JSON.stringify($scope.vitalFinalList));
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
                    var datalistVital = $scope.vitalFinalList;
					for (var i = 0; i < datalistVital.length; i++) {
						if($scope.vitalFinalList[i].vitalId != 2){
							$scope.vitalFinalList[i].vitalValue = '';
						}
					}
					console.log("refreshlist",$scope.vitalFinalList);
               
                                                                          
                        });
        
        },4000);
         
         $scope.onClick = function () {
			   $scope.myChartForSearch = false;
			   $scope.vitalnameGraphList=[];
          	 $scope.vitalname=[];
          	 $scope.createdTime=[];
				 $scope.vitalData=[];
				 $scope.vitalTime=[];
				 $scope.vitalLabel=[];
				 //for label
				 if($scope.patientViatalList.length > 0){
				 angular.forEach($scope.patientViatalList, function(value,key) {
					 $scope.flag=false;
					  if($scope.vitalnameGraphList.length > 0){
							angular.forEach($scope.vitalnameGraphList, function(value3,key) {
								if(value3.vitalName == value.vitalName){
									$scope.flag = true;
								}
							});
						}
						 if(!$scope.flag){
							var newArr={};
							 newArr.vitalName=value.vitalName;
							 newArr.unitDesc=value.unitDesc;
							 $scope.vitalnameGraphList.push(newArr);
						 }
						 console.log("$scope.vitalnameGraphList",$scope.vitalnameGraphList);
						 
				});
				// for data
					
				 angular.forEach($scope.vitalnameGraphList, function(value1,index) {
					$scope.vitalData=[];
					 angular.forEach($scope.patientViatalList, function(value2,index) {
						 
						if(value1.vitalName == value2.vitalName){
							$scope.vitalData.push(value2.value);
							
						}
					 });
					value1.vitalData = $scope.vitalData;
				 });
			
				 
				$scope.colorArray = [];
				 var letters =[ '#803690', '#00ADF9', '#DCDCDC', '#46BFBD', '#FDB45C', '#949FB1', '#4D5360'] ;  // "0F";
			    $scope.colorArray=[ '#803690', '#00ADF9', '#BE2439', '#46BFBD', '#FDB45C', '#006698', '#6A93B0','#992572','#F5FF00','#FF0000','#154889'] ;
				console.log("$scope.vitalname",$scope.vitalnameGraphList);
				console.log("colorArray",$scope.colorArray);
				$scope.dataForDataset=[];
				angular.forEach($scope.vitalnameGraphList, function(value,index) {
					var newArr = {};
					newArr.label = value.vitalName+" ("+ value.unitDesc+")";
					newArr.data = value.vitalData;
					newArr.backgroundColor =$scope.colorArray[index];
					newArr.borderColor =$scope.colorArray[index];
					newArr.fill = false;
					newArr.borderWidth = 2;
					$scope.dataForDataset.push(newArr);
				});
				console.log("$scope.dataForDatasetVital",$scope.dataForDataset);
            var ctx = document.getElementById('myChart').getContext('2d');
            var myChart = new Chart(ctx, {
          	  type: 'line',
          	  data: {
          		    labels: $scope.vitalLabel,
          		    datasets:$scope.dataForDataset
          		  },
          		  options: {
          			  title:{
          				  display: true,
          				  text: 'Vital Graph'
          			  },
          		        legend: {
          		        	 display: true,
          		            position: "top"
          		        },
          		        scales: {
          		        	animation:true,
          		            yAxes: [{
          		                ticks: {
          		                    fontColor: "rgba(0,0,0,0.5)",
          		                    fontStyle: "bold",
          		                    beginAtZero: true,
// maxTicksLimit: 5,
          		                    padding: 10
          		                },
          		                gridLines: {
          		                    drawTicks: false,
          		                    display: false
          		                }

          		            }],
          		            xAxes: [{
          		                gridLines: {
          		                    zeroLineColor: "transparent"
          		                },
          		                ticks: {
          		                    padding: 10,
          		                    fontColor: "rgba(0,0,0,0.5)",
          		                    fontStyle: "bold"
          		                }
          		            }]
          		        }
          		    }

          	});
				 //for time array
				 angular.forEach($scope.patientViatalList, function(value,key) {
					 $scope.flagTime=false;
					  if($scope.createdTime.length > 0){
							angular.forEach($scope.createdTime, function(value3,key) {
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
							 $scope.createdTime.push(newArr);
						 }
						 console.log("$scope.createdTime",$scope.createdTime);
						 
				});
				 
				for (var i = 0; i < $scope.createdTime.length; i++) {
					$scope.vitalLabel.push($scope.createdTime[i].createdTime);
				}
				console.log("graph label",$scope.vitalLabel);
				
				//for data
				 angular.forEach($scope.createdTime, function(value1,index) {
						$scope.vitalDataTrends=[];
						 angular.forEach($scope.patientViatalList, function(value2,index) {
							 
							if(value1.createdDate == value2.createdDate){
								$scope.vitalDataTrends.push(value2);
							}
						 });
						value1.vitalDataTrends = $scope.vitalDataTrends;
					 });
				 console.log("d",$scope.createdTime);
				$scope.finalVitalObjFromAllVitalList = {};
				var hh =  $scope.createdTime.length;
	             angular.forEach( $scope.createdTime[0].vitalDataTrends, function( value,key) {
                  	if(value.roleId == 2){
                  		if(value.vitalId == 6){
                  			if(parseInt(value.value) != 0){
                  				
                  				$scope.finalVitalObjFromAllVitalList.temp = value.value;
                          		$scope.finalVitalObjFromAllVitalList.createdDatetemp = value.createdDate;
                          		$scope.finalVitalObjFromAllVitalList.createdTimetemp = value.createdTime;
                          		$scope.finalVitalObjFromAllVitalList.unitDesctemp = value.unitDesc;
                  			}
                      		
                      	}
                      	if(value.vitalId == 4){
//                      		pulse rate
                      		if(parseInt(value.value) != 0){
                      		$scope.finalVitalObjFromAllVitalList.pulseRate = value.value;
                      		$scope.finalVitalObjFromAllVitalList.createdDatepulseRate = value.createdDate;
                      		$scope.finalVitalObjFromAllVitalList.createdTimepulseRate = value.createdTime;
                      		$scope.finalVitalObjFromAllVitalList.unitDescpulseRate = value.unitDesc;
                      		}
                      	}
                      	if(value.vitalId == 3){
//                      		BP
                      		if(parseInt(value.value) != 0){
                      		$scope.finalVitalObjFromAllVitalList.bps = value.value;
                      		$scope.finalVitalObjFromAllVitalList.createdDatebps = value.createdDate;
                      		$scope.finalVitalObjFromAllVitalList.createdTimebps = value.createdTime;
                      		$scope.finalVitalObjFromAllVitalList.unitDescbps = value.unitDesc;
                      		}
                      	}
                      	if(value.vitalId == 14){
//                      		SPo2
                      		if(parseInt(value.value) != 0){
                      		$scope.finalVitalObjFromAllVitalList.bloodOxygen = value.value;
                      		$scope.finalVitalObjFromAllVitalList.createdDatebloodOxygen = value.createdDate;
                      		$scope.finalVitalObjFromAllVitalList.createdTimebloodOxygen = value.createdTime;
                      		$scope.finalVitalObjFromAllVitalList.unitDescbloodOxygen = value.unitDesc;
                      		}
                      	}
                    	if(value.vitalId == 7){
//                    		SPo2
                    		if(parseInt(value.value) != 0){
                    		$scope.finalVitalObjFromAllVitalList.bpd = value.value;
                    		$scope.finalVitalObjFromAllVitalList.createdDatebloodOxygen = value.createdDate;
                    		$scope.finalVitalObjFromAllVitalList.createdTimebloodOxygen = value.createdTime;
                    		$scope.finalVitalObjFromAllVitalList.unitDescbloodOxygen = value.unitDesc;
                    		}
                    	}
                  	}
                  	
                  });
				 }
				 };

				   /****************Save coversheet as template*******************/
		            $scope.coversheetTemplateName=""; 
					$scope.saveCoversheetAsTemplate=function(){
						$rootScope.startLoaderPopup();
			    		var data={
			    				patientId :$scope.encounterDetailsObj.patientId,
								encounterId :$scope.encounterDetailsObj.encounterId,
								createdBy :$scope.userId,
								createdDate :moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
								templateName:$scope.coversheetTemplateName,
								doctorId :$scope.encounterDetailsObj.doctorId,
								updatedBy :$scope.userId,
								updatedDate :moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
								organizationId :$scope.organizationId,
								unitId :$scope.unitId,
			    				status:"A",
			    		};
			    		
			    		var URI = BASE_URL + ROOT_URL
						+ "/api/opd/saveCoversheetAsTemaplate";
				GenericService
						.serviceAction("POST", URI, data)
						.then(
								function(response) {
									if (response.data.status == "success") {
										$scope.completeConsultation($scope.encounterDetailsObj.roleId);
										 
//										$rootScope.stopLoader();
									}
									
								});
					}
					
					//coversheet As temp list
					  $scope.getListOfCoversheetAStemplate= function(){
				        	$scope.listOfCoversheetAStemplate=[];
				        	var data={
				        			organizationId:$scope.organizationId,
				        			unitId:$scope.unitId
				        	};
							var URI = BASE_URL + ROOT_URL + "/api/opd/getCoversheetAsTemaplateList" 
							
				    		return GenericService.serviceAction("POST",URI,data).then(function(response){
				    			if(response.data.status == "success"){
				    				$scope.listOfCoversheetAStemplate = response.data.listObject;
				    			}
				    			console.log("listOfCoversheetAStemplate", $scope.listOfCoversheetAStemplate);
				    			return $scope.listOfCoversheetAStemplate;
				    		});
				    	}
					  $scope.getListOfCoversheetAStemplate();
					  
					//coversheet As temp list
					/*  $scope.getCovListByTemplateId= function(templateId){
				        	$scope.covListByTemplateId=[];
				        	var data={
				        			covTemplateId:templateId,
				        			organizationId:$scope.organizationId,
				        			unitId:$scope.unitId
				        	};
							var URI = BASE_URL + ROOT_URL + "/api/opd/getAllCoversheetListsByTemplateId" 
							
				    		GenericService.serviceAction("POST",URI,data).then(function(response){
				    			if(response.data.status == "success"){
				    				$scope.covListByTemplateId = response.data.object;
				    				$rootScope.stopLoader();
				    			}
				    			console.log("covListByTemplateId", $scope.covListByTemplateId);
				    				$('#exampleModal').modal('show');
				    			});
				    	}*/
					  
					  
					  
					  $scope.selectedTemplate=function(covTemplateId){
						  $scope.covListByTemplateId=[];
						  if(covTemplateId == 'custom'){
							  $('#customeEmr').modal('show');
							  return false;
						  }
						  else {
							  var data={
					        			covTemplateId:covTemplateId,
					        			organizationId:$scope.organizationId,
					        			unitId:$scope.unitId
					        	};
								var URI = BASE_URL + ROOT_URL + "/api/opd/getAllCoversheetListsByTemplateId" 
								
					    		GenericService.serviceAction("POST",URI,data).then(function(response){
					    			if(response.data.status == "success"){
					    				$scope.covListByTemplateId = response.data.object;
					    				$scope.getCustomeList();
					    				$scope.getCustomePatientId();
					    				$rootScope.stopLoader();
					    			}
					    			console.log("covListByTemplateId", $scope.covListByTemplateId);
					    				$('#templateCoversheteetModal').modal('show');
					    			});
						  }
						  
					  }
					  
					  $scope.confirmSave=function(info){
						  $('#templateCoversheteetModal').modal('hide');
						  $('#confirmSave').modal('show');
						  
					  }
					  
					  //for template lists save
					  $scope.covTempObject={
							  complaintTempList:[],
							  medicalTempList:[],
							 surgicalTempList:[],
							  lifeTempStyleList:[],
							   allergyTempList:[],
							   medicationHistoryTempList:[],
							   treatmentTempList:[],
							   familyTempList:[],
							   personalTempList:[],
							   vaccinationTempList:[],
							   travelTempList:[],
							   vitalTempList:[],
							   generalExaminationTempList:[],
							   systemicExaminationTempList:[],
							   diagnosisTempList:[],
							   diagnosticsTempList:[],
							   medicationTempList:[],
							   adviceTempList:[],
							   consultOrderTempList:[],
							   observationTempList:[],
							   ceratedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
								updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
								updatedBy:$scope.userId,
								createdBy:$scope.userId,
								status :'A',
								roleId : $scope.roleId,
								encounterId : $scope.encounterDetailsObj.encounterId,
								patientId : $scope.encounterDetailsObj.patientId,
								doctorId:$scope.encounterDetailsObj.doctorId
					  };
					  
					  $scope.saveListDataOfCovTemplate=function(info){
						  
						  $scope.covTempObject.ceratedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
						  $scope.covTempObject.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
						  $scope.covTempObject.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
						  $scope.covTempObject.updatedBy = $scope.userId,
						  $scope.covTempObject.createdBy = $scope.userId,
						  $scope.covTempObject.status = 'A',
						  $scope.covTempObject.roleId = $scope.roleId,
						  $scope.covTempObject.doctorId = $scope.encounterDetailsObj.doctorId,
						  $scope.covTempObject.encounterId = $scope.encounterDetailsObj.encounterId,
						  $scope.covTempObject.patientId = $scope.encounterDetailsObj.patientId
						  $scope.covTempObject.complaintTempList=info.complaintsPatientList;
						  $scope.covTempObject.medicalTempList=info.medicalTempList;
						  $scope.covTempObject.surgicalTempList=info.surgicalTempList;
						  $scope.covTempObject.lifeTempStyleList=info.lifeTempStyleList;
						  $scope.covTempObject. allergyTempList=info.allergyTempList;
						  $scope.covTempObject.medicationHistoryTempList=info.medicationHistoryTempList;
						  $scope.covTempObject.treatmentTempList=info.treatmentTempList;
						  $scope.covTempObject.familyTempList=info.familyTempList;
						  $scope.covTempObject.personalTempList=info.personalTempList;
						  $scope.covTempObject.vaccinationTempList=info.vaccinationTempList;
						  $scope.covTempObject.travelTempList=info.travelTempList;
						  $scope.covTempObject.vitalTempList=info.vitalTempList;
						  $scope.covTempObject.generalExaminationTempList=info.generalExaminationTempList;
						  $scope.covTempObject.systemicExaminationTempList=info.systemicExaminationTempList;
						  $scope.covTempObject.diagnosisTempList=info.diagnosisTempList;
						  $scope.covTempObject.diagnosticsTempList=info.diagnosticsTempList;
						  $scope.covTempObject.medicationTempList=info.medicationTempList;
						  $scope.covTempObject.adviceTempList=info.adviceTempList;
						  $scope.covTempObject.consultOrderTempList=info.consultOrderTempList;
						  $scope.covTempObject.observationTempList=info.observationTempList;
						 
						  
						  var response = coversheetTemplateService.saveCovTempListData($scope.covTempObject);
						  if(response == true){
							  growl.success("Added successfully.!");
							  $('#templateCoversheteetModal').modal('show');
						  }
					  }
					  
					  $scope.printCaseSheet=function(){

						  var data =  {
						                 "appName": "HIS",
						                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
						                 "reportDesignFilePath": "NewCoverSheet.rpt",
						                 "reportFormat": "PDF",
						                 "exportFileName": "Coversheet",
						                 "reportParams": [
						                	 {
								                  "paramName": "Patientid",
								                  "paramType": "Integer",
								                  "paramValue": $scope.encounterDetailsObj.patientId
							                },
							                {
								                  "paramName": "Unitid",
								                  "paramType": "Integer",
								                  "paramValue": $scope.unitId
							                },
							                {
									             "paramName": "Encounter_id",
									             "paramType": "Integer",
									             "paramValue": $scope.encounterDetailsObj.encounterId
										      }
				                 ]
				      };
						    reportService.generate(REPORT_API_URI1,data);
						}
					  
						 //previous casesheet
     				 $scope.getEncounterListForNewCasesheet=function(){
							$scope.newCasesheetList =[];
							var sdate = new Date();
							sdate.setDate(sdate.getDate() - 1);
							$scope.yesterday=sdate;

							var encounterObj={

							unitId :$scope.unitId,
							organizationId :$scope.organizationId,
							patientId : $scope.encounterDetailsObj.patientId ,
							toDate:moment($scope.yesterday).format('YYYY-MM-DD')
							}

							var URI = BASE_URL + ROOT_URL + "/api/opd/getEncounterDetailsForCoversheet";

							BillingGenericService.serviceAction(METHOD_POST,URI,encounterObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){

							$scope.newCasesheetList = response.data.listObject;
							if($scope.newCasesheetList.length > 0){
								$scope.index = $scope.newCasesheetList.length;
								$scope.encounterIdPdf = $scope.newCasesheetList[$scope.index-1].encounterId;
							}
							// if($scope.newCasesheetList.length == 0){
							$scope.previousCaseSheetDate=new Date();
							$scope.previousCoversheet();
							// }
							console.log("$scope.encounterListttttttt", $scope.newCasesheetList);
							});


							}
							$scope.getEncounterListForNewCasesheet();
							
							$scope.objPrint="";
							$scope.previousCoversheet=function(){
							try{
							// index = index - 1;
							if($scope.index > 0){
							$rootScope.startLoader();
							$scope.index = $scope.index - 1;
							$scope.previousCaseSheetDate=$scope.newCasesheetList[$scope.index].encounterDateString;

							var data={
							"encounterId": $scope.encounterIdPdf,
							"encounterDateString": moment($scope.previousCaseSheetDate).format("YYYY-MM-DD"),
							"organizationId":$scope.organizationId,
							"unitId":$scope.unitId
							}
							var URI=BASE_URL + ROOT_URL + "/api/billing/unit/getPrintDateWiseCoversheet";
							GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data).then(function(response) {
							if(response.data.status == "success"){
							$scope.objPrint = response.data.object;
							$('#coversheetPdfFilePath1').attr('src',"data:application/pdf;base64,"+$scope.objPrint);
							$rootScope.stopLoader();

							}
							});
							}
							}catch(e){
							console.log(e.message);
							}
							}


							$scope.getNextCaseShhet=function(){
							try{
							// index = (index + 1) <= listCoversheet.length ?
							// (index + 1): listCoversheet.length;
							if($scope.index < $scope.newCasesheetList.length-1 ){
							$rootScope.startLoader();
							$scope.index = $scope.index + 1
							$scope.previousCaseSheetDate=$scope.newCasesheetList[$scope.index].encounterDateString;
							var data={
							"encounterId": $scope.encounterIdPdf,
							"encounterDateString": moment($scope.previousCaseSheetDate).format("YYYY-MM-DD"),
							"organizationId":$scope.organizationId,
							"unitId":$scope.unitId
							}
							var URI=BASE_URL + ROOT_URL + "/api/billing/unit/getPrintDateWiseCoversheet";
							GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data).then(function(response) {
							if(response.data.status == "success"){
							$scope.objPrint = response.data.object;
							$('#coversheetPdfFilePath1').attr('src',"data:application/pdf;base64,"+$scope.objPrint);
							$rootScope.stopLoader();
							}
							});
							}
							}catch(e){
							console.log(e);
							}
							}
							 $scope.dateOptionsNewCov =
								{
									formatYear : 'yyyy',
									showWeeks : false,
									maxDate : new Date()
								};
							$scope.openDatePickerNewCov = function() {
								$scope.datepickerNewCov.opened = true;
							};
	
							$scope.datepickerNewCov = {
									opened : false
							};

							$scope.getDateForCaseSheet=function(date){
								$rootScope.startLoader();
								var flag=false;
								angular.forEach($scope.newCasesheetList ,function(val, i){
									var selectDate=moment(date).format("DD-MM-YYYY");
									var existDate=moment(val.encounterDateString).format("DD-MM-YYYY");
									if(selectDate==existDate){
										
										var data={
							"encounterId": val.encounterId,
							"encounterDateString": selectDate,
							"organizationId":$scope.organizationId,
							"unitId":$scope.unitId
							}
							var URI=BASE_URL + ROOT_URL + "/api/billing/unit/getPrintDateWiseCoversheet";
							GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data).then(function(response) {
							if(response.data.status == "success"){
								$rootScope.stopLoader();
							$scope.objPrint = response.data.object;
//							$scope.previousCaseSheetDate=val.encounterDateString;
							$('#coversheetPdfFilePath1').attr('src',"data:application/pdf;base64,"+$scope.objPrint);
							
							

							}else{
								$rootScope.stopLoader();
							}
//							$rootScope.stopLoader();
							});
										
//										flag=true;
									}else{
										$rootScope.stopLoader();
									}
//									
								
								});
								
								
							}
							
							$scope.confirmSaveTemplate=function(){
								 $('#templateCoversheteetModal').modal('hide');
								 $('#confirmSaveCovTemp').modal('show');
							}
							
							  /************************Custom EMR Code********************************************************************/
							  //For currentcasesheet
							  $scope.getCustomeList=function(){
									 $rootScope.startLoader();
									var data = {
											'doctorId' : $scope.encounterDetailsObj.doctorId,
											'unitId':$scope.unitId,
											'organizationId':$scope.organizationId,
											'patientId' : $scope.encounterDetailsObj.patientId,

										};
									//console.log(headers);
									var paramObj={};
									var headers={};
									$scope.customList = [];
						    		var encounterId = $scope.encounterDetailsObj.encounterId;
						    		var URI = BASE_URL + ROOT_URL + "/api/global/unit/getCustomEMRCategorySubcategoryList" ;

						    		GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_FALSE, paramObj,
											headers)
						    		.then(
						    			function(response) {
						    				if (response.data.status == "success")
						    					$scope.customList = response.data.listObject;
						    			});
								}
								$scope.getCustomeList();
								
								//Update Custom 
								$scope.updateCustom = function(info, index) {
									var URI = BASE_URL + ROOT_URL + "/api/global/unit/updateCustomEMRCategorySubcategory";
									var data = {
											customEmrConfigDetailsId:info.customEmrConfigDetailsId,
											createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedBy:$scope.userId,
											createdBy:$scope.userId,
											status:'A',
											patientId : $scope.encounterDetailsObj.patientId,
											doctorId :$scope.encounterDetailsObj.doctorId,
											encounterId: $scope.encounterDetailsObj.encounterId,
											organizationId:$scope.organizationId,
											cusEmrCategoryId:info.cusEmrCategoryId,
											cusEmrSubCategoryId:info.cusEmrSubCategoryId,
											unitId:$scope.unitId,
											remark:info.remark,
											
										
									}
									/*console.log("data",data);
									 return false;*/
									 
									$rootScope.startLoader();
									GenericService.serviceAction("POST", URI, data).then(function (response) {
										if (response.data.status == "success")
											
										{
											$scope.getCustomeList();
											$scope.getCustomePatientId();
											$rootScope.stopLoader();
											growl.success("Updated Successfully..!");
											
										}
										else if (response.data.status == "error") {
											growl.warning(response.data.message,{
												title : ERROR_MSG
											});
											$rootScope.stopLoader();
										}
									});
								}
								///
								$scope.getCustomePatientId = function(){

									$rootScope.startLoader();
									$scope.listOfAllCustomEMRData=[];
									try{

									var data = {

											patientId : $scope.encounterDetailsObj.patientId,
										    organizationId:$scope.organizationId,
										    unitId:$scope.unitId,

										};

									
									var URI = BASE_URL + ROOT_URL + "/api/global/unit/getAllListoCustomEMRByPatientId";
									GenericService.serviceAction("POST",URI,data).then(function(response){
									if (response.data.status == 'success')
									{
										$scope.listOfAllCustomEMRData=response.data.listObject;
									$rootScope.stopLoader();
									$scope.getCustomeList();
									
									} else if(response.data.status == 'error')
									{
									$rootScope.stopLoader();
									growl.warning(response.data.message,
									{
									title : 'Warning!'
									});
									}
									});
									

									}catch(e){
									console.log("Exception",e);
									}
									// }
								}
								$scope.getCustomePatientId();
								/********************Custom EMR code end*******************************/
	}
	]);

