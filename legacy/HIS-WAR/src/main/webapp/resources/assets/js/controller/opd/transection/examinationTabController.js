/************EDITED ************************/
'use strict';

/**
 * @Author Sneha Dalave
 * @Author Chetna Dhokne 
 * @name myApp.controller:OPDCoverSheetController
 * @description #OPDCoverSheetController Controller of the myApp
 */
angular.module('myApp').controller('examinationTabController',
	['$scope','$cookies','$rootScope','GenericService','BillingGenericService','$state','$timeout','$interval','$stateParams',
	'GenericServiceParamHeader','growl','$compile','reportService','GenericServiceFileUpload','servicesFactory','CONSTANTS','coversheetTemplateService',
		function($scope, $cookies, $rootScope, GenericService,BillingGenericService, $state, $timeout,$interval,
		$stateParams,GenericServiceParamHeader,growl,$compile,reportService,GenericServiceFileUpload,servicesFactory,CONSTANTS) 
		{
			/***************tab wise code start****************/				   
			console.log('$stateParams.appointmentObj',$stateParams.appointmentObj);
						
			/* init() function for form object create. */			    
			//For cylic
			var cookieObject, cookieObjectAppConfig;
			[ cookieObject, cookieObjectAppConfig ] = $scope.checkCookies();
				
			$scope.init = function() {		
			
				$scope.dateLetter = new Date();
				$scope.deptWiseAppointment='N';			
				$scope.medicationObj = {
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
								
				$scope.initMedicationObj = function() {
					$scope.searchItemFre = "";
					$scope.searchItem = "";
					$scope.registrationNumber = "";
					$scope.saveBtnFlag = true;
					$scope.updateScope = true;
					$scope.updateBtnFlag = false;
					$scope.roa_options=[];
					$scope.medicationObj = {
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
				$scope.emptyMedicationHistoryRow = {
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
				$scope.secondTabLevel = 1;
				// $scope.setCurrentTab(1);
							
				// $scope.itemSelectedList.items=[];
				$scope.itemSelectedList = {
					items: []
				};
							
				$scope.itemSelectedListActiveMed = {
					itemsMed: [],
					selectAllActMedi:'N'
				};
							
				$scope.instructionSelectedList = {
					items: []
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
				if (cookieObject == undefined) {
					$state.go('login');
					return;
				}
							
				$scope.unitId = cookieObject.unitId;
				$scope.organizationId = cookieObject.organizationId;
				$scope.userId = cookieObject.userId;
				$scope.employeeId = cookieObject.employeeId;
							
				var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
				if(cookieObjectAppConfig == undefined){
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
				
				if($scope.isAppointmentRequired  == undefined || $scope.isAppointmentRequired == null)
				{
					$scope.isAppointmentRequired = "N";
				}
				else
				{
					$scope.isAppointmentRequired = $scope.isAppointmentRequired;
				}
				// Start get 50 Previous Year with Current Year
				// and month
							
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
										
				// -------------- function to check create opd
				// cover sheet based on appointment
				// -------------//
							
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
						//$scope.isCritical = $scope.encounterDetailsObj.isCritical;
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
						for (var i = 1; i < 100; i++) {
							range.push(year - i);
						}
										  
						$scope.yearss = range;
						$scope.years=[];
						angular.forEach($scope.yearss,function(value){
							if(parseInt($scope.birthYear) <= value){
								$scope.years.push(value);
							}
						})
						console.log("KI",$scope.yearss);
						console.log("$scope.yearsss",$scope.years);
						//For Contract
						if($scope.encounterDetailsObj.contractId != $scope.selfPayeeId)
						{
							$scope.contractId = $scope.encounterDetailsObj.contractId;
						}
						$scope.showHeadCircuVital=false;
						if($scope.patientDetails.age >= 1 &&  $scope.patientDetails.age <=3 ){
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
				BillingGenericService.serviceAction(METHOD_POST, URI,commonObj,NOTIFICATION_MSG_STATUS_FALSE)
				.then(function(response) {
					console.log(response);
					// $rootScope.startLoader();
					if (response.data.status == "success")
						$scope.specialityList = response.data.listObject;
				});
							     
				var headerObj = {
					unit_id : $scope.unitId,
					organization_id : $scope.organizationId
				};
				     
				// object for Complaints
				$scope.dataListComplaints = [ {
					diagnosisId : '',
					icpcSystemId : '',
					since :'',
					duration:'Day',
					remark:'',
					isReviewedFlag : 'N',
					complaintAppoId : '0',
					systemName:	'',
					diagnosisName:'',
					icpcCodeText:'',
					nos:'N',
				} ];
				
				//for Complaint and Diagnosis Template
				$scope.templateListForSave=[];
				var tempObj={};
				tempObj.templateObject={};
				tempObj.diagonisIdforTemplate=0;
				tempObj.icpcSystemId=0;
				tempObj.icpcId=0;
				$scope.templateListForSave.push(tempObj);
				
				$scope.common = [ {
					diagnosisId : '',
					icpcSystemId : '',
				} ];
							     
				if ($scope.encounterDetailsObj == undefined) {
					return;
				}
				var patientId = $scope.encounterDetailsObj.patientId;
				var encounterId = $scope.encounterDetailsObj.encounterId;
				if (angular.equals({},$stateParams.obj)) {
					$state.go('OPDcoversheetNew');
					return;
				} else {
				if($stateParams.obj.newCasesheetList != undefined){
					$scope.newCasesheetList=$stateParams.obj.newCasesheetList;
				}
				if($stateParams.obj.objPrint!= undefined ){
					$scope.objPrint = $stateParams.obj.objPrint;
					$('#coversheetPdfFilePath1').attr('src',"data:application/pdf;base64,"+$scope.objPrint);
				}
				if($stateParams.obj.vitalsList != undefined){
					$scope.vitalsList=$stateParams.obj.vitalsList;
				}
				if($stateParams.obj.currentEncounterOrderLists!= undefined ){
					$scope.currentEncounterOrderLists = $stateParams.obj.currentEncounterOrderLists;
				}
				if($stateParams.obj.familyHistoryCurrentListDoctor != undefined){
					$scope.familyHistoryCurrentListDoctor=$stateParams.obj.familyHistoryCurrentListDoctor;
				}
				if($stateParams.obj.habitFinalList!= undefined ){
					$scope.habitFinalList = $stateParams.obj.habitFinalList;
				}
				if($stateParams.obj.patientHabitOldList != undefined){
					$scope.patientHabitOldList=$stateParams.obj.patientHabitOldList;
				}
				if($stateParams.obj.templateList!= undefined ){
					$scope.templateList = $stateParams.obj.templateList;
				}
				if($stateParams.obj.icpcSystemList != undefined){
					$scope.icpcSystemList=$stateParams.obj.icpcSystemList;
				}
				if($stateParams.obj.templateTransactionList!= undefined ){
					$scope.templateTransactionList = $stateParams.obj.templateTransactionList;
				}
				if($stateParams.obj.detailedMedicationsHistory != undefined){
					$scope.detailedMedicationsHistory=$stateParams.obj.detailedMedicationsHistory;
				}
				if($stateParams.obj.detailedMedications!= undefined ){
					$scope.detailedMedications = $stateParams.obj.detailedMedications;
				}
				if($stateParams.obj.allergySymbolList != undefined){
					$scope.allergySymbolList=$stateParams.obj.allergySymbolList;
				}
				if($stateParams.obj.allergyOldList!= undefined ){
					$scope.allergyOldList = $stateParams.obj.allergyOldList;
				}
				if($stateParams.obj.patientDiagnosisListIcon != undefined){
					$scope.patientDiagnosisListIcon=$stateParams.obj.patientDiagnosisListIcon;
				}
				if($stateParams.obj.patientDiagnosisList!= undefined ){
					$scope.patientDiagnosisList = $stateParams.obj.patientDiagnosisList;
				}
				if($stateParams.obj.complaintListByPatientId != undefined){
					$scope.complaintListByPatientId=$stateParams.obj.complaintListByPatientId;
				}
				if($stateParams.obj.pastComplaintListByPatientId!= undefined ){
					$scope.pastComplaintListByPatientId = $stateParams.obj.pastComplaintListByPatientId;
				}
				if($stateParams.obj.complaintsPatientListDoctor != undefined){
					$scope.complaintsPatientListDoctor=$stateParams.obj.complaintsPatientListDoctor;
				}
				if($stateParams.obj.vitalFinalList!= undefined ){
					$scope.vitalFinalList = $stateParams.obj.vitalFinalList;
				}
				if($stateParams.obj.patientViatalList != undefined){
					$scope.patientViatalList=$stateParams.obj.patientViatalList;
				}
				if($stateParams.obj.patientViatalListTrends!= undefined ){
					$scope.patientViatalListTrends = $stateParams.obj.patientViatalListTrends;
				}
				if($stateParams.obj.vitalDataTrends != undefined){
					$scope.vitalDataTrends=$stateParams.obj.vitalDataTrends;
				}
				if($stateParams.obj.vitalLabel!= undefined ){
					$scope.vitalLabel = $stateParams.obj.vitalLabel;
				}
				if($stateParams.obj.habitTypeList != undefined){
					$scope.habitTypeList=$stateParams.obj.habitTypeList;
				}
				if($stateParams.obj.patientHabitList!= undefined ){
					$scope.patientHabitList = $stateParams.obj.patientHabitList;
				}
				if($stateParams.obj.complaintsPatientList!= undefined ){
					$scope.complaintsPatientList = $stateParams.obj.complaintsPatientList;
				}
				if($stateParams.obj.medicalHistoryCurrentList!= undefined ){
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
		$scope.tab = 7;
		$scope.isSet = function(tabNum){
			return $scope.tab === tabNum;
		};

		$scope.gotoNextSectionLevelOne = function(Tab){
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
			if(Tab == 1){
				var id= "complaint_section";
				$state.go('complaintsTab', {obj : obj});
			}
			if(Tab == 2){
				var id= "past_history_section";
				$state.go('medicalHistoryTab', {obj : obj});
			}
			if(Tab == 3){
				var id= "drug_history_tab";
				$state.go('drugHistoryTab', {obj : obj});
			}
			if(Tab == 4){
				var id= "social_history";
				$state.go('socialHistoryTab', {obj : obj});
			}
			if(Tab == 5){
				var id= "allergies_section";
				$state.go('allergiesTab', {obj : obj});
			}
			if(Tab == 6){
				var id= "vitals_section";
				$state.go('vitalsTab', {obj : obj});
			}
			if(Tab == 7){
				var id= "examination_tab";
				$state.go('examinationTab', {obj : obj});
			}
			if(Tab == 8){
				var id= "diagnosis_section";
				$state.go('diagnosisTab', {obj : obj});
			}
			if(Tab == 9){
				var id= "orders_section";
				$state.go('ordersTab', {obj : obj});
			}
			if(Tab == 10){
				var id= "notes_section";
				$state.go('notesTab', {obj : obj});
			}
			if(Tab == 11){
				var id= "advice_section";
				$state.go('adviceTab', {obj : obj});
			}
			if(Tab == 12){
				var id= "followUp_section";
				$state.go('follwUpTab', {obj : obj});
			}
			if(Tab == 13){
				var id= "attach_to_casesheet";
				$state.go('attachToCasesheetTab', {obj : obj});
			}
			if(Tab == 14){
				var id= "letters_section";
				$state.go('letterTab', {obj : obj});
			}		
		}
		/**FOR SET CODE END*******/
						
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
			file : {}
		}
					  
		/************Complete consultation**************/
		
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
		$scope.completeConsultation=function(roleId){
			var cookieWObject = JSON.parse(sessionStorage.getItem('cookieEncounterObject'));
			if (cookieWObject == undefined) {
				return;
			}
			var headerObj={
				'organization_id': $scope.organizationId,
				'unit_id':$scope.unitId,
				'Content-Type':'application/json'
			};
									
			$rootScope.startLoader();
			if(roleId == 3)
			{
				var data = {
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
				var data = {
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
										
			var paramObjForPer={};
			var datatvgsvx  = data;
			

			var URI = BASE_URL + ROOT_URL + UPDATEENCOUNTERASSIGNMENT;
			GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_TRUE,paramObjForPer,headerObj)
			.then(function(response) {
				console.log(response);
				if (response.data.status == "success")
				{	
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
							//pulse rate
							if(parseInt(value.value) != 0){
								$scope.finalVitalObjFromAllVitalList.pulseRate = value.value;
								$scope.finalVitalObjFromAllVitalList.createdDatepulseRate = value.createdDate;
								$scope.finalVitalObjFromAllVitalList.createdTimepulseRate = value.createdTime;
								$scope.finalVitalObjFromAllVitalList.unitDescpulseRate = value.unitDesc;
							}
						}
						if(value.vitalId == 3){
							//BP
							if(parseInt(value.value) != 0){
								$scope.finalVitalObjFromAllVitalList.bps = value.value;
								$scope.finalVitalObjFromAllVitalList.createdDatebps = value.createdDate;
								$scope.finalVitalObjFromAllVitalList.createdTimebps = value.createdTime;
								$scope.finalVitalObjFromAllVitalList.unitDescbps = value.unitDesc;
							}
						}
						if(value.vitalId == 14){
							//SPo2
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
		                    
		$scope.finalVitalObjFromAllVitalList={
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
								
		$scope.checkFocus = function(){
			$timeout(function() {
					document.getElementById("vitalssss_0").focus();
			}, 500);
		} 

		$scope.k = function(){
				  			          
		var headerObj = {
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
		var data={
			unitId:$scope.unitId,
			organizationId:$scope.organizationId,
			patientId:$scope.encounterDetailsObj.patientId,
		}
		console.log("jj",paramObjForVital);
		$scope.finalVitalObjFromAllVitalList = {};
		// return false;
		var URI = BASE_URL + ROOT_URL + "/api/opd/getListOfVitalsByPatientIdForCoversheet";
		GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data, NOTIFICATION_MSG_STATUS_FALSE,data, paramObjForVital,headerObj)
		.then(function(response) {
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

		$scope.typeId = "1";
		$scope.getExaminationListData = [];
		$scope.addExaminationList = function() {
			var data = {
				organizationId: $scope.organizationId,
				unitId: $scope.unitId,
				typeId: parseInt($scope.typeId),
				patientId: $scope.encounterDetailsObj.patientId,
				encounterId: $scope.encounterDetailsObj.encounterId,
				admissionId: null,
				createdBy: $scope.userId,
				updatedBy: $scope.userId,
				updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
				createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
			}
			var URI = BASE_URL + ROOT_URL + "/api/opd/getGeneralAndSystemicExaminationMapperList";
			$rootScope.startLoader();
			GenericService.serviceAction("POST", URI, data).then(function(response) {
				if (response.data.status == "success"){
					$rootScope.stopLoader();
					$scope.getExaminationListData = response.data.listObject;
//					$scope.getExaminationList();
					$scope.init();
				}
			});
		}

		$scope.getExaminationList = function() {
			var data = {
				organizationId: $scope.organizationId,
				unitId: $scope.unitId,
				typeId: parseInt($scope.typeId),
				patientId: $scope.encounterDetailsObj.patientId,
				status: 'A'
			}
			var URI = BASE_URL + ROOT_URL + "/api/opd/getGenAndSysExaminMapperPatDetailstForNewCov";
			$rootScope.startLoader();
			GenericService.serviceAction("POST", URI, data).then(function(response) {
				if (response.data.status == "success"){
					$rootScope.stopLoader();
					$scope.getExaminationListData1 = response.data.listObject;
					angular.forEach($scope.getExaminationListData1, function(d) {
						$scope.sysDescriptiveExamination = d.descriptiveExamination;
					});
				}
			});
		}
		$scope.getExaminationList();

		$scope.updateExaminationList = function(item) {
			var data = {
				organizationId: $scope.organizationId,
				unitId: $scope.unitId,
				typeId: parseInt($scope.typeId),
				patientId: $scope.encounterDetailsObj.patientId,
				status: 'A',
				encounterId: $scope.encounterDetailsObj.encounterId,
				admissionId: null,
				createdBy: $scope.userId,
				descriptiveExamination: null,
				isNADValue: item.isNADValue == "\u0000" ? 'N':item.isNADValue,
				remark: item.remark,
				status: 'A',
				systemId: item.systemId,
				systemObervationPropertyId: item.systemObervationPropertyId,
				systemObervationId: item.systemObervationId,
				updatedBy: $scope.userId,
				updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
				createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
				generalExamAppoMapperId: item.generalExamAppoMapperId
			}
			var URI = BASE_URL + ROOT_URL + "/api/opd/saveGeneralAndSystemicExaminationMapperForNewCoversheet";
			$rootScope.startLoader();
			GenericService.serviceAction("POST", URI, data).then(function(response) {
				if (response.data.status == "success"){
					$rootScope.stopLoader();
					$scope.getExaminationListData = response.data.listObject;
					growl.success("Updated Successfully..!");
					$scope.getExaminationList();
				}
			});
		}

		$scope.removeGenAndSysExamin = function(item) {
			var URI = BASE_URL + ROOT_URL + "/api/opd/removeGenAndSysExaminMapperPatDetailstForNewCov";
			var data = {
				"status":"I",
				"generalExamAppoMapperId": item.generalExamAppoMapperId,
				"updatedBy": $scope.userId,
				"updatedDate": moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
			}
			$rootScope.startLoader();
			GenericService.serviceAction("POST", URI, data).then(function (response) {
				if (response.data.status == "success")
				{
					$rootScope.stopLoader();
					growl.success("Deleted Successfully..!");
					$scope.getExaminationList();
				}
				else if (response.data.status == "error") {
					growl.warning(response.data.message,{
						title : ERROR_MSG
					});
					$rootScope.stopLoader();
				}
			});
		}

		$scope.validateExaminationSystemic = function(data) {
			var URI = BASE_URL + ROOT_URL + "/api/opd/updateDescriptiveExamForNewCov";
			var data = {
				descriptiveExamination: data,
				organizationId: $scope.organizationId,
				unitId: $scope.unitId,
				patientId: $scope.encounterDetailsObj.patientId
			}
			$rootScope.startLoader();
			GenericService.serviceAction("POST", URI, data).then(function (response) {
				if (response.data.status == "success")
				{
					$rootScope.stopLoader();
					growl.success("Updated Successfully..!");
					$scope.getExaminationList();
				}
				else if (response.data.status == "error") {
					growl.warning(response.data.message,{
						title : ERROR_MSG
					});
					$rootScope.stopLoader();
				}
			});
		}

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
								 
//								$rootScope.stopLoader();
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
//					$scope.previousCaseSheetDate=val.encounterDateString;
					$('#coversheetPdfFilePath1').attr('src',"data:application/pdf;base64,"+$scope.objPrint);
					
					

					}else{
						$rootScope.stopLoader();
					}
//					$rootScope.stopLoader();
					});
								
//								flag=true;
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
					//sj added by current casesheet
					 /*******General Examn for Casesheet *******/
			        $scope.getGenericExaminationForCaseSheeet=function(){
			        	 var data = {
			        			 organizationId:$scope.organizationId,
			        			 patientId:$scope.encounterDetailsObj.patientId,
			        			 typeId:1
			        	 };
						var URI = BASE_URL + ROOT_URL
						+ "/api/opd/getListOfSystemsAndObservationForCaseSheet";
				        GenericServiceParamHeader
				            .serviceAction(METHOD_POST, URI,
				                    data,
				                    NOTIFICATION_MSG_STATUS_FALSE)
				            .then(
				                    function(response) {
				                        console.log(response);
				                        if (response.data.status == "success")
											$scope.getSavedListOfGeneralExam = response.data.listObject;
										angular.forEach($scope.getSavedListOfGeneralExam, function(value,key) {
				                        	$scope.descriptiveExamination=value.descriptiveExamination;
				                        });
				                        console.log("getSavedListOfGeneralExam", $scope.getSavedListOfGeneralExam);
				                    });
				        }
			        $scope.getGenericExaminationForCaseSheeet();
					
					/*******Systemic Examn for Casesheet *********/
			        $scope.getSystemicExaminationForCaseSheeet=function(){
			        	 var data = {
			        			 organizationId:$scope.organizationId,
			        			 patientId:$scope.encounterDetailsObj.patientId,
			        			 typeId:2
			        	 };
						var URI = BASE_URL + ROOT_URL
						+ "/api/opd/getListOfSystemsAndObservationForCaseSheet";
				        GenericServiceParamHeader
				            .serviceAction(METHOD_POST, URI,
				                    data,
				                    NOTIFICATION_MSG_STATUS_FALSE)
				            .then(
				                    function(response) {
				                        console.log(response);
				                        if (response.data.status == "success")
				                            $scope.getSavedListOfSystemicExam = response.data.listObject;
				                        angular.forEach($scope.getSavedListOfSystemicExam, function(value,key) {
				                        	$scope.sysDescriptiveExamination=value.descriptiveExamination;
				                        });
				                        console.log("getSavedListOfSystemicExam", $scope.getSavedListOfSystemicExam);
				                    });
				        }
			        $scope.getSystemicExaminationForCaseSheeet();
			      
			 /*****************************************************/ 
			        
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
							$scope.customList = [];
				    		var encounterId = $scope.encounterDetailsObj.encounterId;
				    		var URI = BASE_URL + ROOT_URL + "/api/global/unit/getCustomEMRCategorySubcategoryList" ;

				    		GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_FALSE)
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


