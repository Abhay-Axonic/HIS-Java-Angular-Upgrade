angular.module('myApp').controller(
				'preOprativeController',
				[
						'$scope',
						'$rootScope',
						'$state',
						'$http',
						'GenericService',
						'$sessionStorage',
						'$cookies',
						'growl',
						'$localStorage',
						'BillingGenericService',
						'$timeout',
						'$stateParams',
						'GenericServiceParamHeader',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService,$timeout,$stateParams,GenericServiceParamHeader) {			
				
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.green = "#008a27";
							$scope.cream = "#dfd724";
							$scope.patientIdentificationStatus = 2 ; //for Pending
							$scope.patientConsentStatus = 2 ; //for Pending
							$scope.whoChecklistStatus = 2; //for pending
							$scope.bloodStatus = 2;
							$scope.icuStatus = 2;
							$scope.anaesthesiaStatus = 2;
							$scope.resultObj = $stateParams.resultCommonObj;
							$scope.roleId = 2;
							$scope.medicationsString = "";
							
							if($scope.resultObj.patientName == undefined){
								$state.go('OTIMSdashboard');
								return false;
							}
							$scope.patientDetails = {};
							$scope.patientDetails.patientName = $scope.resultObj.patientName;
							$scope.patientDetails.uhIdNumber = $scope.resultObj.uhidNumber;
							$scope.patientDetails.wardNameCover = $scope.resultObj.wardName;
							$scope.patientDetails.genderCode = $scope.resultObj.genderCode;
							$scope.patientDetails.age = $scope.resultObj.age;
							$scope.patientDetails.ageStr = $scope.resultObj.age;
							$scope.patientDetails.birthDate = $scope.resultObj.birthDate;
							$scope.patientDetails.admissionId = $scope.resultObj.admissionId;
							$scope.patientDetails.patientId = $scope.resultObj.patientId;
							$scope.patientDetails.visitTypeId = $scope.resultObj.visitTypeId;
							$scope.patientDetails.isTreatingDoctor = 'Y';
							$scope.patientDetails.genderId = $scope.resultObj.genderId;
							$scope.patientDetails.genderName = $scope.resultObj.genderName;
							if($scope.resultObj.doa != null && $scope.resultObj.doa !=""){
							$scope.patientDetails.dateOfAdmission = $scope.resultObj.doa;
							}
							else{
							$scope.patientDetails.dateOfAdmission = $scope.resultObj.datepicker;
							}
							if($scope.resultObj.pdd != null && $scope.resultObj.pdd !=""){
							$scope.patientDetails.patientDischargeDate = $scope.resultObj.pdd;
							}
							
							
							$scope.patientDetails.los = $scope.resultObj.los
							$scope.patientDetails.bedNumber = $scope.resultObj.bedNumber;
							$scope.patientDetails.doctorFullName = $scope.resultObj.doctorName;
							
							
							
							
							if($scope.resultObj.visitTypeId!=undefined && $scope.resultObj.visitTypeId!=1){
								var dateDoa = moment($scope.resultObj.doa, 'DD/MM/YYYY');
								var datePdd = moment($scope.resultObj.pdd, 'DD/MM/YYYY');
								$scope.resultObj.los = $rootScope.dateDiffrence(dateDoa,datePdd);
							} 
							
							console.log("$scope.resultObj",$scope.resultObj);
							
							
							$scope.checklistDetails = [];
							$scope.preOprativeObj = {};
							$scope.getPreOprativeDetails = function() {
								$scope.startLoader();
								if($stateParams.resultCommonObj.proReqId!=undefined){
									$scope.checklistDetails = [];
									$scope.preOprativeObj = {};
								var data = {
									proReqId : $stateParams.resultCommonObj.proReqId,
								};
								var URI = BASE_URL + ROOT_URL + "/api/otims/PreOperativeDetails/getPreOperativeDetailsByProReqId";
								GenericService.serviceAction(METHOD_POST, URI, data)
										.then(function(response) {
											if (response.data.status == "success") {
												if (response.data.object != null) {
													if (response.data.status == "success") {
														$scope.checklistDetails = response.data.object.whoChecklistDtoList;
														$scope.preOprativeObj = response.data.object;
														$scope.patientIdentificationStatus = response.data.object.patientIdentificationStatus ; 
														$scope.patientConsentStatus = response.data.object.patientConsentStatus;
														$scope.whoChecklistStatus = response.data.object.whoChecklistStatus;
														$scope.preOprativeObj.otCompletionTimeSheetId = response.data.object.otCompletionTimeSheetDto.otCompletionTimeSheetId;
														$scope.preOprativeObj.requestReason  = response.data.object.indicationReason;
														if($scope.preOprativeObj.bloodStatus == 1){
															$scope.preOprativeObj.isBloodComponentRequired  = 'Y';
														}
														if($scope.preOprativeObj.icuStatus == 1){
															$scope.preOprativeObj.isIcuRequired = 'Y'
														}
														if($scope.preOprativeObj.anaesthesiaStatus == 1){
															$scope.preOprativeObj.isPacRequired = 'Y'
														}
														
														$scope.bloodStatus = $scope.preOprativeObj.bloodStatus;
														$scope.icuStatus = $scope.preOprativeObj.icuStatus ;
														$scope.anaesthesiaStatus = $scope.preOprativeObj.anaesthesiaStatus;
														
													}
													$scope.stopLoader();
												} else {
												var URI = BASE_URL + ROOT_URL + "/api/otims/dashboard/getPreOparetiveDeta";
													GenericService.serviceAction(METHOD_POST, URI, data)
														.then(function(response) {
															if (response.data.status == "success") {
																$scope.checklistDetails = response.data.listObject;
																$scope.preOprativeObj = response.data.object;
																if($scope.checklistDetails!=null && $scope.checklistDetails!=undefined 
																&& $scope.checklistDetails.length>0){
																	angular.forEach($scope.checklistDetails,function(value,index){
																		if(value.whoStatusId == null){
																			value.whoStatusId = 2 ; //default pending
																		}
																	});
																}
															}
															$scope.stopLoader();
														});
												}
											}
											$scope.stopLoader();
										});
									}
							}
							
							$scope.getPreOprativeDetails();
							$scope.medicationShowFlag = true;
							$scope.activeMedications = [];
							$scope.getAllActiveMedication = function() {
								$scope.startLoader();
								$scope.activeMedications = [];
								var patientId = $stateParams.resultCommonObj.patientId;
								var dateObj = moment(new Date()).format(
								'YYYY-MM-DD');

								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEMEDICATION + "?patientId="
								+ patientId + "&date=" + dateObj+ "&unitId="+ $scope.unitId
								GenericService
								.serviceAction(METHOD_GET, URI)
								.then(
								function(response) {
								if (response.data.status == "success") {
									
									$scope.activeMedications = response.data.listObject;

									angular.forEach($scope.activeMedications,function(value,key){
										value.checkTrue='N';
										$scope.medicationShowFlag = false;
										if($scope.medicationsString == ""){
											$scope.medicationsString = value.itemName;
										}else{
											$scope.medicationsString = $scope.medicationsString +" , "+value.itemName;
										}
									
									});
								}
								$scope.stopLoader();
								});
								}
							
							$scope.getAllActiveMedication();
							
							
							//List Of Continued Medication of medication History
							$scope.getContinuedMedicationHistoryList = function() {
								$scope.startLoader();
								$scope.continuedMedicationHistoryList = [];
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									patientId : $stateParams.resultCommonObj.patientId
								};
								var URI = BASE_URL + ROOT_URL
										+ "/api/opd/getContinuedMedicationList";
								GenericService
										.serviceAction(METHOD_POST, URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.continuedMedicationHistoryList = response.data.listObject;
														angular.forEach($scope.continuedMedicationHistoryList,function(value,key){
								    						value.checkTrue='N';
								    						$scope.medicationShowFlag = false;
								    						if($scope.medicationsString == ""){
																$scope.medicationsString = value.itemName;
															}else{
																$scope.medicationsString = $scope.medicationsString +" , "+value.itemName;
															}
								    					});
													}
													$scope.stopLoader();
												});
							}
							$scope.getContinuedMedicationHistoryList();
							
							
							//set Who checklist status
							$scope.checkWhoStatus = function(){
								var completeStatusCount = 0;
								var pendingStatusCount = 0;
								if($scope.checklistDetails != undefined && $scope.checklistDetails != null && $scope.checklistDetails.length != 0){
									angular.forEach($scope.checklistDetails,function(value,index){
										if(value.whoStatusId == 1){
											completeStatusCount = completeStatusCount +1;
										}else if(value.whoStatusId == 2){
											pendingStatusCount = pendingStatusCount + 1;
										}
										
									});
									
									if(completeStatusCount == $scope.checklistDetails.length){
										$scope.whoChecklistStatus = 1;
									}else if(pendingStatusCount>0){
										$scope.whoChecklistStatus = 2;
									}else{
										$scope.whoChecklistStatus = 3;
									}
								}else{
									$scope.whoChecklistStatus = 4;
								}
							}
							$scope.allergyCurrentList = [];
							$scope.allergyOldList = [];
							$scope.patientDiagnosisList = [];
							$scope.patientHabitList = [];
							$scope.medicationsList = [];
							$scope.implantsList = [];
							
							$scope.setColor = function(flag){
								if (flag == 1) {
									if ($scope.preOprativeObj.requestReason == null
											|| $scope.preOprativeObj.requestReason == "") {
										return {
											"background-color" : $scope.cream
										};
									} else if ($scope.preOprativeObj.requestReason != null
											&& $scope.preOprativeObj.requestReason != "") {
										return {
											"background-color" : $scope.green
										};
									} else {
										return {
											"background-color" : "#f0793a"
										};
									}
								}else if (flag == 2) {
									if ($scope.allergyCurrentList.length>0 || $scope.allergyOldList.length>0
											|| $scope.patientDiagnosisList.length>0 || $scope.patientHabitList.length>0) {
										return {
											"background-color" : $scope.green
										};
									} else  {
										return {
											"background-color" : $scope.cream
										};
									} 
								}else if(flag == 3){
									if($scope.patientIdentificationStatus == 2){
										return {
											"background-color" : "#e90000"
										};
									}else if($scope.patientIdentificationStatus == 1){
										return {
											"background-color" : $scope.green
										};
									}else if($scope.patientIdentificationStatus == 3){
										return {
											"background-color" : "#fff442"
										};
									}else{
										return {
											"background-color" : "#f0793a"
										};
									}
								}else if(flag == 4){
									if(($scope.activeMedications == undefined || $scope.activeMedications == null || $scope.activeMedications.length == 0)
											&& ($scope.continuedMedicationHistoryList == undefined || $scope.continuedMedicationHistoryList == null || $scope.continuedMedicationHistoryList.length == 0)){
										return {
											"background-color" : $scope.cream
										};
									}else{
										return {
											"background-color" : $scope.green
										};
									}
								}else if(flag == 5){
									if($scope.patientConsentStatus == 2){
										return {
											"background-color" : "#e90000"
										};
									}else if($scope.patientConsentStatus == 1){
										return {
											"background-color" : $scope.green
										};
									}else if($scope.patientConsentStatus == 3){
										return {
											"background-color" : "#fff442"
										};
									}else{
										return {
											"background-color" : "#f0793a"
										};
									}
								}else if(flag == 6){
									if($scope.checklistDetails == undefined || $scope.checklistDetails == null || $scope.checklistDetails.length == 0){
										return {
											"background-color" : $scope.cream
										};
									}else if($scope.whoChecklistStatus == 1){
										return {
											"background-color" : $scope.green
										};
									}else if($scope.whoChecklistStatus == 2){
										return {
											"background-color" : "#e90000"
										};
									}else if($scope.whoChecklistStatus == 3){
										return {
											"background-color" : "#fff442"
										};
									}else  if($scope.whoChecklistStatus == 4){
										return {
											"background-color" : $scope.cream
										};
									}else{
										return {
											"background-color" : "#f0793a"
										};
									}
									
								}else if(flag == 7){
									if($scope.preOprativeObj.isBloodComponentRequired == " "){
										return {
											"background-color" : $scope.cream
										};
									}else if($scope.bloodStatus == 1){
										return {
											"background-color" : $scope.green
										};
									}else if($scope.bloodStatus == 2){
										return {
											"background-color" : "#e90000"
										};
									}else if($scope.bloodStatus == 4){
										return {
											"background-color" :  $scope.cream
										};
									}else {
										return {
											"background-color" :  $scope.cream
										};
									}
								}else if(flag == 8){
									if($scope.preOprativeObj.isIcuRequired == " "){
										return {
											"background-color" : $scope.cream
										};
									}else if($scope.icuStatus == 1){
										return {
											"background-color" : $scope.green
										};
									}else if($scope.icuStatus == 2){
										return {
											"background-color" : "#e90000"
										};
									}else if($scope.icuStatus == 4){
										return {
											"background-color" :  $scope.cream
										};
									}else {
										return {
											"background-color" :  $scope.cream
										};
									}
								}else  if (flag == 9) {
									if ($scope.medicationsList.length>0 || $scope.implantsList.length>0) {
										return {
											"background-color" : $scope.green
										};
									} else  {
										return {
											"background-color" : $scope.cream
										};
									} 
								}else if(flag == 10){
									if($scope.preOprativeObj.isPacRequired == " "){
										return {
											"background-color" : $scope.cream
										};
									}else  if($scope.anaesthesiaStatus == 1){
										return {
											"background-color" : $scope.green
										};
									}else if($scope.anaesthesiaStatus == 2){
										return {
											"background-color" : "#e90000"
										};
									}else if($scope.anaesthesiaStatus == 4){
										return {
											"background-color" :  $scope.cream
										};
									}
									else {
										return {
											"background-color" :  $scope.cream
										};
									}
								}else if(flag == 12){
									/*if($scope.wheelInDisabledBtn){
										return {
											"background-color" : $scope.green
										};
									}else{
										return {
											"background-color" : "red"
										};
									}*/
								}
								
								}
							
							$scope.openIndication = function(){
								$("#indicationPopup").modal("show");
							}
						
							$scope.openBloodPopup = function(){
								$("#bloodPopup").modal("show");
							}
							
							$scope.openICUPopup = function(){
								$("#icuPopup").modal("show");
							}
							
							$scope.openAnaesthesiaPopup = function(){
								$("#anaesthesiaCompletedPopup").modal("show");
							}
							
							$scope.openWHOPopup = function(){
								$("#whoChecklistPopup").modal("show");
							}
							
							$scope.openMedicalHistoryPopup = function(){
								$("#medicationHistoryPopup").modal("show");
							}
							
							
/************************* COMORBIDITY BUTTON START ********************************************************/
							
							//get Lifestyle List
							
							$scope.months = [];
							$scope.months.push({
								value : 1,
								text : 'January'
							});
							$scope.months.push({
								value : 2,
								text : 'February'
							});
							$scope.months.push({
								value : 3,
								text : 'March'
							});
							$scope.months.push({
								value : 4,
								text : 'April'
							});
							$scope.months.push({
								value : 5,
								text : 'May'
							});
							$scope.months.push({
								value : 6,
								text : 'June'
							});
							$scope.months.push({
								value : 7,
								text : 'July'
							});
							$scope.months.push({
								value : 8,
								text : 'August'
							});
							$scope.months.push({
								value : 9,
								text : 'September'
							});
							$scope.months.push({
								value : 10,
								text : 'October'
							});
							$scope.months.push({
								value : 11,
								text : 'November'
							});
							$scope.months.push({
								value : 12,
								text : 'December'
							});
							
							
							$scope.getLifeStyleList = function(){
								$scope.startLoader();
								$scope.patientHabitList = [];
								var headerObj = {
										unit_id : $scope.unitId,
										organization_id : $scope.organizationId
									};

									var paramObjForPer = {
										patientId : $scope.resultObj.patientId,
										admissionId :$scope.resultObj.admissionId,
										roleId : $scope.roleId
									};
									 var data = {};
								var URI = BASE_URL + ROOT_URL + GETCURRENTPATIENTHABITDETAILS;
						GenericServiceParamHeader
								.serviceAction(METHOD_GET, URI, data,
										NOTIFICATION_MSG_STATUS_FALSE,
										paramObjForPer, headerObj).then(
										function(response) {
											if (response.data.status == "success")
												$scope.patientHabitList = response.data.listObject;
											
											$scope.stopLoader();
											});
							}
							
							$scope.getLifeStyleList();
							
						//get allergy list 
							
							$scope.getAllergyList = function() {
								$scope.allergyCurrentList = [];
								$scope.startLoader();

								var headerObj = {
									unit_id : $scope.unitId,
									organization_id : $scope.organizationId
								};

								var paramObjForHistList = {
									patientId : $scope.resultObj.patientId,
									created_date : moment(new Date()).format('DD-MM-YYYY')
								};

								var URI = BASE_URL
										+ ROOT_URL
										+ "/api/opd/getCurrentAllergyPatientMapperForCoversheet";
								GenericServiceParamHeader
										.serviceAction(METHOD_GET, URI, "",
												NOTIFICATION_MSG_STATUS_FALSE,
												paramObjForHistList, headerObj)
										.then(
												function(response) {
													if (response.data.status == "success")
														$scope.allergyCurrentList = response.data.listObject;
													$scope.stopLoader();
												});
							}	
							$scope.getAllergyList();
							
							
							$scope.getAllergyOldList = function() {
								$scope.allergyOldList = [];
								$scope.startLoader();
								var headerObj = {
									unit_id : $scope.unitId,
									organization_id : $scope.organizationId
								};

								var paramObjForHistList = {
									patientId : $scope.resultObj.patientId,
									created_date : moment(new Date()).format('DD-MM-YYYY')
								};

								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL
										+ GETOLDALLERGYPATIENTMAPPER;
								GenericServiceParamHeader
										.serviceAction(METHOD_GET, URI, "",
												NOTIFICATION_MSG_STATUS_FALSE,
												paramObjForHistList, headerObj)
										.then(
												function(response) {
													if (response.data.status == "success")
														$scope.stopLoader();
													$scope.allergyOldList = response.data.listObject;
													
												});
							}
							$scope.getAllergyOldList();
							
							//getPatientDiagnosis 
							
							$scope.getPatientDiagnosis = function() {
								$scope.startLoader();
								$scope.patientDiagnosisList = [];
								var data = {};
								var headerObjDia = {
									'unit_id' : $scope.unitId,
									'organization_id' : $scope.organizationId,
									'patient_id' : $scope.resultObj.patientId,
									'Content-Type' : 'application/json',
								};
								var URI = BASE_URL + ROOT_URL
										+ GETPATIENTDIAGNOSISBYPATIENTID;
								GenericServiceParamHeader
										.serviceAction(METHOD_GET, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE,
												data, headerObjDia).then(
												function(response) {
													$scope.stopLoader();
													if (response.data.status == "success")
														$scope.patientDiagnosisList = response.data.listObject;
													
												});
							}
							
							$scope.getPatientDiagnosis();
/************************* COMORBIDITY BUTTON END ********************************************************/
			
/************************* SPECIAL KIT BUTTON START ********************************************************/	

							$scope.thirdTabLevel = 1;
							$scope.isSetThirdTabLevel = function(tabNum) {
								return $scope.thirdTabLevel === tabNum;
							};
							
							$scope.setThirdTabLevel = function(newTab) {
								$scope.thirdTabLevel = newTab;
							};
							
							$scope.setThirdTabLevel(1);
							
							$scope.procedureOrderId = "";
							$scope.getProcedureOrderId = function(){
								$scope.startLoader();
								var data = {
										proReqId  : $scope.resultObj.proReqId
								};
								var URI = BASE_URL + ROOT_URL+ "/api/otims/PreOperativeDetails/getProcedureOrderIdByProReqId";
								GenericService.serviceAction(METHOD_POST, URI, data).then(
												function(response) {
													if (response.data.status == "success") {
														$scope.procedureOrderId = response.data.object.procedureOrderId;
													}
													$scope.stopLoader();
												});
							}
							
							if($scope.resultObj.proReqId!=null && $scope.resultObj.proReqId!=undefined){
								$scope.getProcedureOrderId();
							}
							//medication Tab START
							$scope.medicationObj = {
									isItemOrKit : 'N',
									kitName : "",
									itemName : ""
									
							}
							
							$scope.isItemOrKit = "N";
							$scope.selectItemOrKit = function(isItemOrKit) {
								$scope.isItemOrKit = isItemOrKit;
							}
							
							
							$scope.kitDetails = [];
							$scope.medicationDetailsList = [];

							$scope.getListByName = function(name) {
								$scope.items = [];
								if ($scope.isItemOrKit == 'Y') {
									var URI = BASE_URL + ROOT_URL + "/api/units/kit/master/search/" + name;
									return GenericService.serviceAction("GET", URI)
											.then(function(response) {
														if (!!response.data&& response.data.length > 0)
															$scope.kitDetails = [];
														angular.forEach(response.data,function(value,key) {
																			var newArr = {};
																			newArr.kitId = value.id;
																			newArr.kitName = value.description;
																			newArr.isItemOrKit = $scope.isItemOrKit;
																			$scope.kitDetails.push(newArr);
																		});
														return $scope.kitDetails;
													});
								} else {
									var URI = BASE_URL + ROOT_URL + "/api/globals/items/item/search/" + name;

									return GenericService.serviceAction("GET", URI).then(function(response) {
														if (!!response.data && response.data.length > 0)
														return response.data;
													});
								}
							}
							
							$scope.selectMedication = function(info, model,label) {
								$scope.dt = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
								if (info.isItemOrKit == 'Y') {
									var newArr = {};
									newArr.medicationDetailsId = null;
									newArr.kitId = info.kitId;
									newArr.itemName = info.itemName;
									newArr.qty = 1;
									newArr.isItemOrKit = info.isItemOrKit;
									newArr.admissionId = $scope.resultObj.admissionId;
									newArr.patientId = $scope.resultObj.patientId;
									newArr.organizationId =  $scope.organizationId;
									newArr.unitId = $scope.unitId;
									newArr.status ="A";
									newArr.createdBy = $scope.userId;
									newArr.createdDate = $scope.dt;
									newArr.updatedBy = $scope.userId;
									newArr.kitName = info.kitName;
									newArr.updatedDate = $scope.dt;
									newArr.isExtra = true;
									$scope.medicationDetailsList.push(newArr);
									$scope.medicationObj.kitName = '';
								} else {
									var newArr = {};
									newArr.medicationDetailsId = null;
									newArr.itemId = info.itemId;
									newArr.itemName = info.itemName;
									newArr.qty = 1;
									newArr.isItemOrKit = info.isItemOrKit;
									newArr.admissionId = $scope.resultObj.admissionId;
									newArr.patientId = $scope.resultObj.patientId;
									newArr.organizationId =  $scope.organizationId;
									newArr.unitId = $scope.unitId;
									newArr.status ="A";
									newArr.createdBy = $scope.userId;
									newArr.createdDate = $scope.dt;
									newArr.updatedBy = $scope.userId;
									newArr.updatedDate = $scope.dt;
									newArr.isExtra = true;
									$scope.medicationDetailsList.push(newArr);
									$scope.medicationObj.itemName = '';
								}

							}

							$scope.removeFromAddedKitList = function(index) {
									$scope.medicationDetailsList.splice(index,1);
							}
							
							
							//medication Tab END
							
							//Implant Tab START
							
							$scope.implantObj ={
									isItemOrKit : 'N',
									itemName : "",
									kitName : ""
							};
							$scope.implantItems = []; 
							$scope.implantKitDetails = [];
							
							$scope.selectImplantItemOrKit = function(isItemOrKit) {
								$scope.implantObj.isItemOrKit = isItemOrKit;
							}

							$scope.getImplantListByName = function(name) {
								$scope.implantItems = [];
								if ($scope.implantObj.isItemOrKit == 'Y') {
									var data = {
										kitName : name,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId
									};
									var URI = BASE_URL + ROOT_URL + IMPLANT_SEARCH_KIT;

									return GenericService.serviceAction(METHOD_POST, URI,data)
											.then(function(response) {
														if (response.data.status == 'success')
															$scope.implantKitDetails = [];
														angular.forEach(response.data.listObject,function(value,key) {

																			var newArr = {};
																			newArr.kitId = value.kitId;
																			newArr.kitName = value.kitName;
																			newArr.isItemOrKit = $scope.implantObj.isItemOrKit;
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

									return GenericService.serviceAction(METHOD_POST, URI,data)
											.then(function(response) {
														if (response.data.status == "success")
															return response.data.listObject;
													});
								}
							}
							
							$scope.implantDetailsList = [];
							$scope.selectImplant = function(info, model, label) {
								$scope.dt = moment( new Date()).format('DD-MM-YYYY HH:mm:ss');
								if (info.isItemOrKit == 'Y') {
									var newArr = {};
									newArr.implantDetailsId = null;
									newArr.itemName = info.itemName;
									newArr.kitId = info.kitId;
									newArr.qty = 1;
									newArr.isItemOrKit = info.isItemOrKit;
									newArr.admissionId = $scope.resultObj.admissionId;
									newArr.patientId =$scope.resultObj.patientId;
									newArr.organizationId =  $scope.organizationId;
									newArr.unitId = $scope.unitId;
									newArr.status ="A";
									newArr.createdBy = $scope.userId;
									newArr.createdDate = $scope.dt;
									newArr.updatedBy = $scope.userId;
									newArr.updatedDate = $scope.dt;
									newArr.isExtra = true;
									newArr.kitName =  info.kitName ;
									$scope.implantDetailsList.push(newArr);
									$scope.implantObj.kitName = '';
									
								} else {
									
									var newArr = {};
									newArr.implantDetailsId = null;
									newArr.itemId = info.itemId;
									newArr.itemName = info.itemName;
									newArr.qty = 1;
									newArr.isItemOrKit = info.isItemOrKit;
									newArr.admissionId = $scope.resultObj.admissionId;
									newArr.patientId = $scope.resultObj.patientId;
									newArr.organizationId =  $scope.organizationId;
									newArr.unitId = $scope.unitId;
									newArr.status ="A";
									newArr.createdBy = $scope.userId;
									newArr.createdDate = $scope.dt;
									newArr.updatedBy = $scope.userId;
									newArr.updatedDate = $scope.dt;
									newArr.isExtra = true;
									$scope.implantDetailsList.push(newArr);
									$scope.implantObj.itemName = '';
								}
							}

							$scope.removeFromAddedImplantList = function(index) {
									$scope.implantDetailsList.splice(index,1);
							}
							
							
							
							

							$scope.getMedicationAndImplant = function() {
								$scope.startLoader();
								
								var data = {
										proReqId : $stateParams.resultCommonObj.proReqId,
									};
								
								var URI = BASE_URL + ROOT_URL + "/api/procedure/getMedicationsAndImplantsByPeoReqId";
								GenericService.serviceAction("POST", URI, data).then(
												function(response) {
													$scope.stopLoader();
													if (response.data.status == "success"){
														
														angular.forEach(response.data.listObject,function(value,key) {
															
															if(value.medicationDetailsList.length>0){
																angular.forEach(value.medicationDetailsList,function(med,key1) {
																	
																	$scope.medicationsList.push(med);
																	
																});
															}
															
															if(value.implantDetailsList.length>0){
																angular.forEach(value.implantDetailsList,function(imp,key1) {
																	
																	$scope.implantsList.push(imp);
																	
																});
															}
															
														});
													}
													
													
												});
							}
							
							$scope.getMedicationAndImplant();
							

/************************* SPECIAL KIT BUTTON END ********************************************************/							
							
							$scope.detailsObj = {};
							$scope.savePreoeprativeData = function(){
								$scope.startLoader();
								$scope.detailsObj = {};
								//timesheet obj
								var timeSheetDto = {
									otCompletionTimeSheetId : $scope.preOprativeObj.otCompletionTimeSheetId !=undefined ? $scope.preOprativeObj.otCompletionTimeSheetId : null,	
									orgId : $scope.organizationId,
									unitId : $scope.unitId,
									proReqId : $stateParams.resultCommonObj.proReqId,
									status: 'A',
									createdBy : $scope.userId,
									updatedBy : $scope.userId,
									updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									wheelInTime : moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
								};
							
								//who checklist
								$scope.whoChecklistDtoList = [];
								if($scope.checklistDetails != undefined && $scope.checklistDetails != null && $scope.checklistDetails.length != 0){
									angular.forEach($scope.checklistDetails,function(value,index){
									
										var newArr = {
												whoChecklistId : (value.whoChecklistId!=undefined && value.whoChecklistId!=null )? value.whoChecklistId : null,
												preOperativeId : (value.preOperativeId!=undefined && value.preOperativeId!=null )? value.preOperativeId : null,
												checklistMasterId: value.checklistMasterId,
												remark: value.remark ,
												whoStatusId : value.whoStatusId,
												status: 'A',
												intraOperative: null,
												organizationId:$scope.organizationId,
												unitId : $scope.unitId,
												createdBy : $scope.userId,
												updatedBy : $scope.userId,
												updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
												createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
										};
										$scope.whoChecklistDtoList.push(newArr);
									});
								}
								 
								if($scope.whoChecklistStatus == 2  && $scope.whoChecklistDtoList.length >0 ){
									growl.error("Please Complete/Override All Who Checklist Status",
											{
												title : 'error'
											});
									$scope.stopLoader();
									return false;
								}
								
								if($scope.patientIdentificationStatus == 2){
									growl.error("Please Complete/Override Patient Identification Status",
											{
												title : 'error'
											});
									$scope.stopLoader();
									return false;
								}
								
								if($scope.patientConsentStatus == 2){
									growl.error("Please Complete/Override Patient Consent Status",
											{
												title : 'error'
											});
									$scope.stopLoader();
									return false;
								}
								
								if($scope.bloodStatus == 2){
									growl.error("Please Complete/Override Blood Status",
											{
												title : 'error'
											});
									$scope.stopLoader();
									return false;
								}
								
								
								if($scope.icuStatus == 2){
									growl.error("Please Complete/Override ICU Status",
											{
												title : 'error'
											});
									$scope.stopLoader();
									return false;
								}
								
								if($scope.anaesthesiaStatus == 2){
									growl.error("Please Complete/Override Anaesthesia Status",
											{
												title : 'error'
											});
									$scope.stopLoader();
									return false;
								}
								
								
								 var data = {
										  preOperativeId : $scope.preOprativeObj.preOperativeId!=undefined ? $scope.preOprativeObj.preOperativeId : null ,
										  proReqId :  $scope.resultObj.proReqId ,
										  procedureOrderId : $scope.procedureOrderId,
										  indicationReason : $scope.preOprativeObj.requestReason ,
										  patientIdentificationStatus : $scope.patientIdentificationStatus,
										  patientConsentStatus : $scope.patientConsentStatus ,
										  whoChecklistStatus :($scope.whoChecklistDtoList != undefined && $scope.whoChecklistDtoList != null && $scope.whoChecklistDtoList.length != 0) ? $scope.whoChecklistStatus : 4 ,
										  bloodStatus : $scope.bloodStatus ,
										  icuStatus : $scope.icuStatus,
										  anaesthesiaStatus : $scope.anaesthesiaStatus,
										  status :'A',
										  organizationId : $scope.organizationId,
										  unitId : $scope.unitId,
										  createdBy : $scope.userId,
										  updatedBy : $scope.userId,
										  updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										  createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										  whoChecklistDtoList : $scope.whoChecklistDtoList,
										  otCompletionTimeSheetDto : timeSheetDto,
										  medicationDetailsList : $scope.medicationDetailsList ,
										  implantDetailsList : $scope.implantDetailsList
								 };
								 
								 console.log("saveData",data);
								var URI = BASE_URL + ROOT_URL + "/api/otims/PreOperativeDetails/savePreOperativeDetails";
								GenericService.serviceAction(METHOD_POST, URI, data).then(
												function(response) {
													if (response.data.status == "success") {
														if(response.data.object!=null){
															data.otCompletionTimeSheetDto.otCompletionTimeSheetId = response.data.object.otCompletionTimeSheetId;
														}
														growl.success(response.data.message,
																{
																	title : 'Success!'
																});
														$scope.detailsObj = data;
														$scope.goToIntraOperative();
													}
													$scope.stopLoader();
												});
							}
							
							
							//procedure Reschedule
							$scope.goToPlannedProcedure = function(){
								 $timeout(function () { 
									 $state.go('plannedProcedure',
												{
													procedureObj: $scope.resultObj
												});
								 }, 3000);
							}
							
							$scope.goToIntraOperative = function(){
								 $timeout(function () { 
									 $state.go('intraOperative',
												{
										 		intraCommonObj: $scope.resultObj,
										 		intraDetailsObj : $scope.detailsObj,
										 		resultCommonObj : $stateParams.resultCommonObj 
												});
								 }, 3000);
							}		
							
							/*$scope.getMedicationDetails = function(){
								
							}
							
							$scope.getMedicationDetails();
							
							$scope.getImplantDetails = function(){
								
							}
							$scope.getImplantDetails();*/
							
							
							$scope.allergyDataString = "";
							 $scope.allergyFlag = false;
								$scope.allergydata=function(){ 
								$scope.dt = moment(new Date()).format('DD-MM-YYYY');
								var headerObj = {
										unit_id : $scope.unitId,
										organization_id : $scope.organizationId
									};

									var paramObjForHistList = {
										patientId : $scope.resultObj.patientId,
									};
							var data = {};
							var URI = BASE_URL + ROOT_URL + "/api/opd/getPatientAllergyListForIcon";
								GenericServiceParamHeader.serviceAction(METHOD_GET, URI,data,
								NOTIFICATION_MSG_STATUS_FALSE,paramObjForHistList,headerObj) .then(
								function(response) {
									console.log(response);
									if (response.data.status == "success")
									$scope.activeAllergyListForIcon = response.data.listObject;
									console.log("list for allergy icon",$scope.activeAllergyListForIcon)
									if($scope.activeAllergyListForIcon.length > 0)
									{
										$scope.allergySymbolList=[];
										 var  i = 0;
									   angular.forEach($scope.activeAllergyListForIcon,function(value,index){
										   if(value.isNoKnownAllergies !='Y'){
											   $scope.allergySymbolList.push(value);
											  
											   if(i ==0 ){
												   $scope.allergyDataString = (value.allergyDesc!= null ?  value.allergyDesc: value.genericName);
												   i=1;
											   }else{
												   $scope.allergyDataString =   $scope.allergyDataString+", "+(value.allergyDesc!= null ?  value.allergyDesc: value.genericName);
											   }
											  
										   }
										   if($scope.allergySymbolList.length > 0){
											   $scope.allergyFlag = true;
										   }
										   else{
											   $scope.allergyFlag = false;
										   }
									   });
										
									}else{
										   $scope.allergyFlag = false;
									   }
									
								});
					}
								$scope.allergydata();
								
				
								 $scope.comorbidityFlag = false;
									$scope.getPatientDiagnosis = function() {
										var data = {};
										var headerObjDia = {
											'unit_id' : $scope.unitId,
											'organization_id' : $scope.organizationId,
											'patient_id' : $scope.resultObj.patientId,
											'Content-Type' : 'application/json',
										};
										var URI = BASE_URL + ROOT_URL
												+ GETPATIENTDIAGNOSISBYPATIENTID;
										GenericServiceParamHeader
												.serviceAction(METHOD_GET, URI, data,
														NOTIFICATION_MSG_STATUS_FALSE,
														data, headerObjDia)
												.then(
														function(response) {
															console.log(response);
															// $rootScope.startLoader();
															if (response.data.status == "success")
																$scope.patientDiagnosisList = response.data.listObject;
															console.log( "patientDiagnosisList", $scope.patientDiagnosisList);
															 $scope.complaintDelete=true;
															if($scope.patientDiagnosisList.length > 0)
															{
															  $scope.complaintDelete=false;
															  $scope.comorbidityFlag = true;
															}
															
														     $scope.finalString="";
					                                         angular.forEach($scope.patientDiagnosisList,function(value,index){
					                                        	 if(index == 0){
					                                        		 if(value.type == 1){
					                                        			 var ext =  "(Prov)";
					                                        		 }else{
					                                        			 var ext =  "(F)";
					                                        		 }
					                                        		 if(value.diagnosisType == 1){
						                                        			var ext2 = "(P)" ;
						                                        		 }else{
						                                        			 var ext2 = "(S)" ;
						                                        		 }
					                                        		 
					                                        	   var String = value.diagnosisName +""+ext+"" +ext2;
					                                        	   $scope.finalString = $scope.finalString.concat(String);
					                                        	 }
					                                        	 else{
					                                        		 if(value.type == 1){
					                                        			 var ext =  "(Prov)";
					                                        		 }else{
					                                        			 var ext =  "(F)";
					                                        		 }
					                                        		 if(value.diagnosisType == 1){
						                                        			var ext2 = "(P)" ;
						                                        		 }else{
						                                        			 var ext2 = "(s)" ;
						                                        		 }
					                                        		   var String = value.diagnosisName +""+ext+"" +ext2;
						                                        	   $scope.finalString = $scope.finalString.concat(","+String);
					                                        	 }
					                                         });
					                                         
														});
									}
									$scope.getPatientDiagnosis();
									
									$scope.backScreen = function(){
										$state.go('OTIMSdashboard');
									}
							
				}]);
		