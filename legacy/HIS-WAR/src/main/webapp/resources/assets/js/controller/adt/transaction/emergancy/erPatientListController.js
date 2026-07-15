'use strict';

/**
 * @Author By Sneha 
 */
angular.module('myApp')
.controller("erPatientListController",
['$scope','$rootScope','$state','$cookies','GenericService','PagerService','$window','promiseFactory','$sessionStorage','growl','BillingGenericService','GenericServiceForWithoutLogin','$localStorage','GenericServiceParamHeader',
function($scope,$rootScope,$state, $cookies, GenericService,PagerService,$window,promiseFactory,$sessionStorage,growl,BillingGenericService,GenericServiceForWithoutLogin,$localStorage,GenericServiceParamHeader){
									$rootScope.loginpage = true;
									$scope.saveBtnFlag = true;
									$scope.updateBtnFlag = false;
									$scope.updateScope = false;
									$scope.popUpFlag = true;
								
									/**Variables for pagination */
									$scope.offset = 0;
									$scope.noOfRecordsPerPage = 10;
									$scope.commonList = [];
									$scope.totalNoOfRecords;
									$scope.searchKeyword;
									$scope.commonListCount;
								
								
									var cookieObject = $cookies.getObject('cookieObject');
									var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
									var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
									if(cookieObject == undefined){
										$state.go('login');
										return;
									}
									
									$scope.isRMO=cookieObject.isRMO;

									var rmoDoctorOP=JSON.parse(localStorage.getItem('rmoMappedOPDoctorList'));
									var rmoDoctorIP=JSON.parse(localStorage.getItem('rmoMappedIPWard'));
									 rmoDoctorOP= rmoDoctorOP != undefined ? rmoDoctorOP : [];
									 rmoDoctorIP= rmoDoctorIP != undefined ? rmoDoctorIP : [];
									 
									$scope.rmoVisitType=$cookies.getObject('rmoVisit');
									$scope.cookieDoctorId = cookieObject.doctorId;
									$scope.dFirstName=cookieObject.firstName;
									$scope.rmoMappedOpDoctorList=[];
									$scope.rmoMappedWard=null;
									angular.element('#specialitySelectionPpuopForPharmacy').modal('hide');
									$(".modal-backdrop").hide();
									if($scope.isRMO=='Y' && (rmoDoctorOP==null || rmoDoctorIP==null)){
										 growl.warning(
												 'RMO roster is not defined !!',
													{
														title : 'Warning!'
													});
										return false;
									}
									else{
										$scope.rmoMappedOpDoctorList= rmoDoctorOP!=undefined ? rmoDoctorOP :[];
										$scope.rmoMappedWard= rmoDoctorIP!=undefined ? rmoDoctorIP: null;
									}

									
									var cookieObject, cookieObjectAppConfig;
									[ cookieObject, cookieObjectAppConfig ] = $scope
											.checkCookies();
								
									$scope.unitId = cookieObject.unitId;
									$scope.organisationId = cookieObject.organizationId;
									$scope.organizationId = cookieObject.organizationId;
									$scope.userId = cookieObject.userId;
									$scope.createdBy =cookieObject.userId;
									$scope.updatedBy =cookieObject.userId;
									$scope.dateFormat=cookieObjectAppConfig.dateFormat;
									$scope.nurseRole = cookieObjectAppConfig.nurseRole;
                                    $scope.doctorRole = cookieObjectAppConfig.doctorRole;


									$scope.specialityId = cookieObjectAppConfig.specialityId;
									$scope.role = cookieObjectAppConfig.roleName;
									
									 $scope.catogryList = [];
								        $scope.catogryList.push({value:1, text:'Red'});
							            $scope.catogryList.push({value:2, text:'Yellow'});
							            $scope.catogryList.push({value:3, text:'Green'});
							            $scope.catogryList.push({value:4, text:'Black'});

									//for SAVE data
						                $scope.getmappedDataCoversheetByUser = function(){
											
											var URI = BASE_URL + ROOT_URL + "/api/opd/getMappedCoversheetDataForUser";
											var data = {
													  userId:$scope.userId,
												      unitId:$scope.unitId
											}
											GenericService.serviceAction("POST",URI,data).then(function(response){
												if(response.data.status == "success"){
												$rootScope.stopLoader();					
												$scope.mappedDataCoversheetByUser = [];
												$scope.mappedDataCoversheetByUser = JSON.parse(response.data.listObject[0].tabAccessIds);
												localStorage.setItem("mappedDataCoversheetByUser", JSON.stringify($scope.mappedDataCoversheetByUser));
												console.log("mappedDataCoversheetByUser",$scope.mappedDataCoversheetByUser);
												}
											});
											}
						                $scope.getmappedDataCoversheetByUser(); 
						                
						                
						                $scope.updateDoctorIdAndSpecialityId = function(info){
											
						                	
											var URI = BASE_URL + ROOT_URL + "/api/billing/updateDoctorIdAndSpecialityId";
											var data = {
													doctorId : $scope.cookieDoctorId,
													specialityId : $scope.specialityId,
													admissionId : info.admissionId,
													unitId : $scope.unitId
											}
											GenericService.serviceAction("POST",URI,data).then(function(response){
												
												if(response.data.status == "success"){
												
												}
											});
											}
						                
						                
						                
							
							        	$scope.generateIpdCoverSheetUrl = function(info)
										{
											//$rootScope.getTemplateCountByAdmissionOrEncounter(info.patientId,info.admissionId,null);
											
											if($scope.role=='nurse'){
											if(info!=null)
											  {
												info.doa=info.datepicker;
												info.pdd=info.datepickerPdd;
												info.startConsultanceStartTime = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'); 
//												info.mappedDataCoversheetByUserss = [];
//												info.mappedDataCoversheetByUserss = $scope.mappedDataCoversheetByUser, 
												console.log("patientDetails",JSON.stringify(info));
															$state.go('erNursingCoversheet',
																{
																
																patientDetailsObj :info
																});
											  }
										  else
										     {
											    growl.error("Something went Wrong.",{title : 'Error!'});
										     }
											}
											else{
												if(info!=null)
												  {
													if(info.doctorId==null || info.doctorId==0)
														 info.doctorId = $scope.cookieDoctorId;
													     info.dFirstName = $scope.dFirstName;
													     info.specialityId=$scope.specialityId;
													     $scope.updateDoctorIdAndSpecialityId(info);
													info.startConsultanceStartTime = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'); 
//													info.mappedDataCoversheetByUserss = [];
//													info.mappedDataCoversheetByUserss = $scope.mappedDataCoversheetByUser, 
													info.doa=info.datepicker;
													info.pdd=info.datepickerPdd;
													console.log("patientDetails",JSON.stringify(info));
																$state.go('IPDCoverSheet',
																	{
																	patientDetailsObj :info
																	});
												  }
											  else
											     {
												    growl.error("Something went Wrong.",{title : 'Error!'});
											     }
												
											}
										}
							

							$scope.getClassForPDD = function(item)
							{
								var pickUpDate = moment(new Date()).format('YYYY-MM-DD');
								var array = item.datepickerPdd.split('/');
								var alignFillDate = "";
								alignFillDate = array[2] + '-' + array[1] + '-'+ array[0];
								
								   if (pickUpDate > alignFillDate) {
									    return 'true';
									  }
								
							}
							
							$scope.getERPatientList=function(){
								
									var commonObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
									};
								/*	if($scope.isRMO=='Y'){
										commonObj.wardId=$scope.rmoMappedWard;
									}*/
								
     				if($scope.rmoVisitType==4){	
     					$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL
								+ "/adt/getListOfERPatients";
						GenericService
								.serviceAction("POST", URI, commonObj)
								.then(
										function(response) {
											$scope.listOfAdmittedPatientListforAddmitted = [];
											$scope.admittedPatientList = [];

											if (response.data.status == "success") {
												$rootScope.stopLoader();
												$scope.admittedPatientList = response.data.listObject;
												angular
														.forEach(
																$scope.admittedPatientList,
																function(
																		value,
																		index) {

																	var newArr = {};
																	newArr.admissionDetailsId = value.admissionDetailsId;
																	newArr.currentTriage = value.currentTriage != null ?  value.currentTriage : value.triageCategoryId;
																	newArr.specialityName = value.specialityName;
																	newArr.doctorFullName = value.dFirstName;
																	newArr.pFirstName = value.pFirstName;
																	newArr.reasonDesc = value.reasonDesc;
																	newArr.dob = value.dob;
																	// newArr.datepicker = moment(value.doa,'DD/MM/YYYY HH:mm').format(('dd/MM/yyyy HH:mm').toUpperCase());
																	// newArr.datepickerPdd = moment(value.pdd,'DD/MM/YYYY HH:mm').format(('dd/MM/yyyy HH:mm').toUpperCase());
																	newArr.datepicker = value.doa;
																	newArr.datepickerPdd = value.pdd;

																	console.log("$scope.dateFormat ",$scope.dateFormat);

																	console.log("PDD ",moment(value.pdd,'DD/MM/YYYY HH:mm').format(('dd/MM/yyyy HH:mm').toUpperCase()));
																	
																	newArr.genderCode = value.genderCode;
																	newArr.genderId = value.genderId;
																	newArr.doctorId = value.doctorId;
																	newArr.prefixId = value.prefixId;
																	newArr.specialityId = value.specialityId;
																	newArr.uhidnumber = value.uhidnumber;
																	newArr.patientId = value.patientId;
																	newArr.tPatientId = value.tPatientId;
																	newArr.tPatientId = value.tPatientId;
																	newArr.paymentEntitlementDesc = value.paymentEntitlementDesc;
																	newArr.bedNumber = value.bedNumber;
																	newArr.admissionId = value.admissionId;
																	newArr.prefixCode = value.prefixCode;
																	newArr.identificationNo = value.identificationNo;
																	newArr.wardName = value.wardName;
																	newArr.imagePath = value.imagePath;
																	newArr.visitTypeId = value.visitTypeId;
																	newArr.concent = value.concent;
																	newArr.bedCategoryId=value.bedCategoryId;
																	newArr.paymentEntitlementId=value.paymentEntitlementId;
																	newArr.billingBedCategoryId=value.billingBedCategoryId;
																	newArr.patientCategoryId=value.patientCategoryId;
																	newArr.triageCategoryId=value.triageCategoryId;
																	newArr.isIcuu=value.isIcuu;
																	newArr.bedId=value.bedId;
																	newArr.wardId=value.wardId;
																	newArr.uuid=value.uuid;
																	newArr.isFinalBill= value.isFinalBill;
																	 switch (true) {
																		case value.stemi === 'Y':
																			newArr.code = 'STEMI';
																			break;

																		case value.stroke === 'Y':
																			newArr.code = 'Stroke';
																			break;

																		case value.truma === 'Y':
																			newArr.code = 'Truma';
																			break;

																		case value.sepsis === 'Y':
																			newArr.code = 'Sepsis';
																			break;

																		case value.shock === 'Y':
																			newArr.code = 'Shock';
																			break;

																		default:
																			newArr.code = null;
																	}
																	if(value.triageCategoryId==1){
																		newArr.triageCategory = "Resuscitation";
																	}else if(value.triageCategoryId==2){
																		newArr.triageCategory = "Emergent/Immediate";
																	}else if(value.triageCategoryId==2){
																		newArr.triageCategory = "Emergent/Immediate";
																	}else if(value.triageCategoryId==3){
																		newArr.triageCategory = "Urgent/Delayed";
																	}else if(value.triageCategoryId==4){
																		newArr.triageCategory = "Less Urgent/Minimal";
																	}else if(value.triageCategoryId==5){
																		newArr.triageCategory = "Non Urgent";
																	}else if(value.triageCategoryId==6){
																		newArr.triageCategory = "Dead/Expectant";
																	}
																	newArr.dischargeStatusId=value.dischargeStatusId;
																	newArr.medicoLegalId=value.medicoLegalId;
																	newArr.bedCategoryDesc=value.bedCategoryDesc;
																	newArr.bedNumber=value.bedNumber;
																	newArr.roomDesc=value.roomDesc;
																	newArr.roomId=value.roomId;
																	newArr.bedCode=value.bedCode;
																	newArr.erCategoryId=value.erCategoryId;
																	newArr.contractId=value.contractId;
																	newArr.ageStr=value.ageStr;
																	newArr.interventionRequired = value.interventionRequired;
																	newArr.interventionCompleted = value.interventionCompleted;
																	$scope.listOfAdmittedPatientListforAddmitted
																			.push(newArr);
																	

																});
												angular.element('#specialitySelectionPpuopForPharmacy').modal('hide');
												$(".modal-backdrop").hide();

											}
										});
					  }
							}
							
							$scope.getDoctorBySpeciality = function() {
								
								var data = {
									specialityId : $scope.specialityId,
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								var URI = BASE_URL + ROOT_URL + GETDOCTORBYSPECIALITYID;
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
													console.log(response);
													$scope.doctorList = [];
													$scope.List =[];
													// $rootScope.startLoader();
													if (response.data.status == "success"){
														$scope.doctorList = response.data.listObject;
														
														
														angular.forEach($scope.doctorList,function(value,key){
																	if(value.userId==$scope.userId){
																		var newArr ={};
																		newArr.doctorId=value.doctorId;
																		newArr.userId=value.userId;
																		$scope.List.push(newArr);
																	}
																	
																});
														
														if($scope.isRMO=='Y' && $scope.isRMO !=undefined ){
																$scope.getERPatientList();
														}
														else if($scope.List.length==0){
															growl.warning("Patient Accessible to ER Doctor Only",
																	{
																       title : 'warning!'
																	});
														}
														else {
															$scope.rmoVisitType=4;
															$scope.getERPatientList();
															
														}
													
															
															
															
														
														
												}
								});

							}
							
								if($scope.role=='doctor'){
									$scope.getDoctorBySpeciality();
								}
								else if($scope.role=='Admin' || $scope.role=='admin'){
									$scope.getDoctorBySpeciality();
								}
								else{
									$scope.rmoVisitType=4;
									$scope.getERPatientList();
							    }
							
							
								$scope.patientDocumnetInformation = function(info) {
									$scope.patientDocumentFlipBook ={};
									$scope.patientDocumentFlipBook =info;
									
									
										var data =
										{
										"encounterId":$scope.patientDocumentFlipBook.encounterId,
										"patientId":$scope.patientDocumentFlipBook.patientId,
										"unitId":$scope.unitId,
										"organisationId":$scope.organizationId,
										"userId":$scope.userId
										}
										
										
									var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
									BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
											$scope.patientDocumentListTest = [];
											if (response.data.status == "success") 
											{
												$scope.patientDocumentListTest = response.data.listObject;
												if($scope.patientDocumentListTest != null && $scope.patientDocumentListTest.length >0){
													$('#patientDocumentOption').modal('show');
												}
												$rootScope.stopLoader();
											}
											else if(response.data.status == "error")
												{
												$rootScope.stopLoader();
												}
										});
							
						          };

							     	
						          $scope.convertDateToTimestamp = function(dateString) {
						                let parts = dateString.split("-");
						                let day = parseInt(parts[0], 10);
						                let month = parseInt(parts[1], 10) - 1; 
						                let year = parseInt(parts[2], 10);
						                let date = new Date(year, month, day);
						                let timestampInMilliseconds = date.getTime();
						                let timestampInSeconds = timestampInMilliseconds / 1000;
						                return timestampInSeconds;
						            };
						            
							     		$scope.generateFlipbookData = function() {
							                  let flipbookDatalist = [];

								     			var filteredlist=[];
								     		    angular.forEach($scope.patientDocumentListTest,function(filterObj,index){
								     		    	
								     		    	if(filterObj.documentPath != null){
								     				var flipbookDataobj={};
								     				 let createdDate = filterObj.createdDate;
								     	            let timestamp = $scope.convertDateToTimestamp(createdDate);
								     				flipbookDataobj.timestamp=timestamp;
								     				flipbookDataobj.url=filterObj.documentPath;
								     				flipbookDataobj.tags=[];
								     				flipbookDataobj.tags.push("Document");
								     				flipbookDataobj.title=filterObj.documentName != null ? filterObj.documentName :"Lab Report" ; 
								     				flipbookDatalist.push(flipbookDataobj);
								     		    	}
								     				
								     			});

							                let data = {
							                    flipbookData: flipbookDatalist,
							                    logo: $scope.firstImgaa,
							                    privacy: "public"
							                };

							                let baseUrlLocation = $window.location.href;
							                let temp = baseUrlLocation.split('/');
							                let BASE_URL_CASESHEET = baseUrlLocation.replace(temp[temp.length - 1], "");

							                let generateFlipbookurl = "https://feqituyulh.execute-api.ap-south-1.amazonaws.com/dev/api/generateFlipbook";
							                
							        		try {
							        			var param={};
							        				$.ajax({
							        			        url : generateFlipbookurl,
							        					type : "POST",
							        					headers: {'X-Api-Key': "apikey_zHOw4R758ELytSeHFII99YibXj2sWHR6u9mxKdipN/g="},
							        					contentType : "application/json",
							        					dataType : "json",
							        					data : JSON.stringify(data),
							        			        //headers:getHeaders(),
							        					success : function(response){
							        						if(response.flipbookUrl!=undefined &&  response.flipbookUrl!=null  &&  response.flipbookUrl!="" ){
							        							  window.open(''+response.flipbookUrl, '_blank');
							        						}else{
							        							$.notify("Error occurred","error");
							        						}
							        			    	}
							        			});
							        	     } catch (e) {
							        	 		console.log(e );
							        		}
							            };
						     	
							            
							         	$scope.firstImgaa;
			                            $scope.imageBackground = function(){
			                                            var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/screen-images/"+$scope.organizationId+"/2"
			                                                GenericServiceForWithoutLogin.serviceAction("GET", URI).then(function(response) {
			                                                if(response.data.object != null){
			                                                $scope.firstImgaa=response.data.object.imageURL;
			                                                console.log($scope.firstImgaa);
			                                                }
			                                                });
			                                            }
			                                         $scope.imageBackground();
			                                         
			                                         
			                                         
			                                         $scope.set_color = function(triageCategoryId) {
			             							    if(triageCategoryId == 1){
			             							        return {	"height": "22px",
			             							        	"width": "22px",
			             							        	"margin-left": "278px",
			             							        	"margin-top":"-26px",
			             							        	"background-color": "red"};
			             							    }
			             							    else if(triageCategoryId  == 2 ){
			             							        return {	"height": "22px",
			             							        	"width": "22px",
			             							        	"margin-left": "278px",
			             							        	"margin-top":"-26px",
			             							        	"background-color": "red"};
			             							        
			             							    }
			             							    else if(triageCategoryId  == 3 ){
			             							        return {
			             							       	"height": "22px",
		             							        	"width": "22px",
		             							        	"margin-left": "278px",
		             							        	"margin-top":"-26px",
		             							        	"background-color": "Yellow"
			             							        };
			             							        
			             							    }
			             							    else if(triageCategoryId == 4 ){
			             							        return {
			             							       	"height": "22px",
		             							        	"width": "22px",
		             							        	"margin-left": "278px",
		             							        	"margin-top":"-26px",
		             							        	"background-color": "Aquamarine"};
			             							        
			             							    }
			             							    else if(triageCategoryId == 5 ){
			             							        return {
			             							       	"height": "22px",
		             							        	"width": "22px",
		             							        	"margin-left": "278px",
		             							        	"margin-top":"-26px",
		             							        	"background-color": "Aquamarine"};
			             							        
			             							    }else if(triageCategoryId == 6){
			             							        return {
			             							       	"height": "22px",
		             							        	"width": "22px",
		             							        	"margin-left": "278px",
		             							        	"margin-top":"-26px",
		             							        	"background-color": "grey"};
			             							        
			             							    }
													
			             							};

													  $scope.set_color_er_list = function(triageCategoryId) {
			             							    if(triageCategoryId == 1){
			             							        return {	"height": "22px",
			             							        	"width": "22px",
			             							        	"margin-left": "9px",
			             							        	"margin-top":"6px",
																"display": "inline-block",
			             							        	"background-color": "red"};
			             							    }
			             							    else if(triageCategoryId  == 2 ){
			             							        return {	"height": "22px",
			             							        	"width": "22px",
																 "display": "inline-block",
			             							        	"margin-left": "9px",
			             							        "margin-top":"6px",
			             							        	"background-color": "red"};
			             							        
			             							    }
			             							    else if(triageCategoryId  == 3 ){
			             							        return {
			             							       	"height": "22px",
		             							        	"width": "22px",
															 "display": "inline-block",
		             							        	"margin-left": "9px",
		             							        	"margin-top":"6px",
		             							        	"background-color": "Yellow"
			             							        };
			             							        
			             							    }
			             							    else if(triageCategoryId == 4 ){
			             							        return {
			             							       	"height": "22px",
		             							        	"width": "22px",
															 "display": "inline-block",
		             							        	"margin-left": "9px",
		             							        	"margin-top":"6px",
		             							        	"background-color": "Aquamarine"};
			             							        
			             							    }
			             							    else if(triageCategoryId == 5 ){
			             							        return {
			             							       	"height": "22px",
		             							        	"width": "22px",
															 "display": "inline-block",
		             							        	"margin-left": "9px",
		             							        	"margin-top":"6px",
		             							        	"background-color": "Aquamarine"};
			             							        
			             							    }else if(triageCategoryId == 6){
			             							        return {
			             							       	"height": "22px",
		             							        	"width": "22px",
															 "display": "inline-block",
		             							        	"margin-left": "9px",
		             							        	"margin-top":"6px",
		             							        	"background-color": "grey"};
			             							        
			             							    }
													
			             							};

													$scope.triagePriority = function(info) {
														return info.triageCategoryId; 
													};

                    $scope.medicationTypeName = "Medication Details";
					$scope.zeroMedicationMessage="No Medication ordered against this Encounter !!";
                    $scope.patientDetails.visitTypeId=0;
					$scope.openInvestigationOrderPopup = function(info){
						$scope.patientDetails.visitTypeId=info.visitTypeId;

						$scope.getInvetigation(info.patientId,info.visitTypeId,info.admissionId);						
						$('#investigationOrderPopup').modal('show');
					}

					$scope.openMidicationPopup = function(info){
						$scope.medicationTypeName = "Medication Details";
						$scope.zeroMedicationMessage="No Medication ordered against this Encounter !!";
						$scope.getDetailedMedication(info.patientId,info.admissionId,info.doctorId);
						$('#medicationPopup').modal('show');
					}

				$scope.openActiveMidicationPopup = function(info){
						$scope.getAllActiveMedication(info.patientId,info.admissionId,info.doctorId);
						$scope.medicationTypeName = "Active Medication Details";
						$scope.zeroMedicationMessage="No Active Medication ordered against this Encounter !!";
						$('#medicationPopup').modal('show');
					}


					$scope.currentEncounterOrderLists = [];
					$scope.getInvetigation = function (patientId,visitTypeId,admissionId) {
                            $rootScope.startLoader();
                            var data = {};
                            var paramObj = {};
                            var headers = {
                              patientId: patientId,
                              flag: false,
                              "Content-Type": "application/json",
                              unitId: $scope.unitId,
                            };

                            var URI =
                              BASE_URL +
                              ROOT_URL +
                              "/api/opd/getInvestigationsListForCoversheet";
                            GenericServiceParamHeader.serviceAction(
                              METHOD_GET,
                              URI,
                              data,
                              NOTIFICATION_MSG_STATUS_FALSE,
                              paramObj,
                              headers
                            ).then(function (response) {
                              if (response.data.status == "success") {
                                $scope.currentEncounterOrderLists =
                                  response.data.listObject;



								  $scope.currentEncounterOrderLists =
									$scope.currentEncounterOrderLists.filter(function (item) {
										return item.admissionId === admissionId;
									});


								  angular.forEach($scope.currentEncounterOrderLists,function(value,index)
					    							{
					    							var createdDate1 = moment(value.createdDate,'DD-MM-YYYY HH:mm').format('DD-MMM-YYYY HH:mm');
					    							var orderSchDate1 = moment(value.orderSchDate,'DD-MM-YYYY').format('DD-MMM-YYYY');
					    							var sampleGenDatetime1 = moment(value.sampleGenDatetime,'DD-MM-YYYY HH:mm').format('DD-MMM-YYYY HH:mm');
					    							if(createdDate1=='Invalid date')
					    								{
					    								createdDate1="";
					    								}else
					    									{
					    									createdDate1=createdDate1;
					    									}
					    							if(orderSchDate1=='Invalid date')
				    								{
					    								orderSchDate1="";
				    								}else
				    									{
				    									orderSchDate1=orderSchDate1;
				    									}
					    							if(sampleGenDatetime1=='Invalid date')
				    								{
					    								sampleGenDatetime1="";
				    								}else
				    									{
				    									sampleGenDatetime1=sampleGenDatetime1;
				    									}
					    							value["createdDate1"]=createdDate1;
					    							value["orderSchDate1"]=orderSchDate1;
					    							value["sampleGenDatetime1"]=sampleGenDatetime1;
													value["visitTypeId"]=visitTypeId;

                                                    




					    							});
                              }
                            });
                          };	

                        $scope.detailedMedications = [];
						$scope.getDetailedMedication = function (
                                    patientId,
                                    admissionId,
                                    doctorId
                                  ) {

									$scope.detailedMedications = [];
                                   
                                    var headerObj = {
                                      unitId: $scope.unitId,
                                      organizationId: $scope.organizationId,
                                    };

                                    var paramObjForPer = {
                                      patientId: patientId,
                                      admissionId: admissionId,
                                      doctorId: doctorId,
                                    };

                                    var data = {};

                                    var URI =
                                      BASE_URL +
                                      ROOT_URL +
                                      "/api/opd/getMedicationDetailsByDoctorId";
                                    GenericServiceParamHeader.serviceAction(
                                      METHOD_GET,
                                      URI,
                                      data,
                                      NOTIFICATION_MSG_STATUS_FALSE,
                                      paramObjForPer,
                                      headerObj
                                    ).then(function (response) {
                                      if (response.data.status == "success") {
                                        $scope.detailedMedications =
                                          response.data.listObject;
                                        angular.forEach(
                                          $scope.detailedMedications,
                                          function (value, index) {
                                            var addedDate1 = moment(
                                              value.addedDate,
                                              "DD-MM-YYYY"
                                            ).format("DD-MMM-YYYY");
                                            var startDate1 = moment(
                                              value.startDate,
                                              "DD-MM-YYYY"
                                            ).format("DD-MMM-YYYY");
                                            var endDate1 = moment(
                                              value.endDate,
                                              "DD-MM-YYYY"
                                            ).format("DD-MMM-YYYY");
                                            if (addedDate1 == "Invalid date") {
                                              addedDate1 = "";
                                            } else {
                                              addedDate1 = addedDate1;
                                            }
                                            if (startDate1 == "Invalid date") {
                                              startDate1 = "";
                                            } else {
                                              startDate1 = startDate1;
                                            }
                                            if (endDate1 == "Invalid date") {
                                              endDate1 = "";
                                            } else {
                                              endDate1 = endDate1;
                                            }
                                            value["addedDate1"] = addedDate1;
                                            value["startDate1"] = startDate1;
                                            value["endDate1"] = endDate1;
                                          }
                                        );
                                        if (
                                          $scope.detailedMedications != null &&
                                          $scope.detailedMedications.length > 0
                                        ) {
                                          angular.forEach(
                                            $scope.detailedMedications,
                                            function (value, index) {
                                              if (value.doseDesc == null) {
                                                value.doseDesc = "";
                                              }
                                              if (value.strengthDesc == null) {
                                                value.strengthDesc = "";
                                              }
											  value.addedTime = moment(value.addedTime, "HH:mm").format("hh:mm A");
                                            }
                                          );
                                        }
                                      }
                                    });
                                  };



						// $scope.getAllActiveMedication = function (patientId, admissionId, doctorId) {

						// 		$scope.detailedMedications = [];

						// 		var headerObj = {
						// 			unitId: $scope.unitId,
						// 			organizationId: $scope.organizationId
						// 		};

						// 		var paramObjForPer = {
						// 			patientId: patientId,
						// 			admissionId: admissionId,
						// 			doctorId: doctorId
						// 		};

						// 		var data = {};

						// 		var URI =
						// 			BASE_URL +
						// 			ROOT_URL +
						// 			"/api/opd/getMedicationDetailsByDoctorId";

						// 		GenericServiceParamHeader.serviceAction(
						// 			METHOD_GET,
						// 			URI,
						// 			data,
						// 			NOTIFICATION_MSG_STATUS_FALSE,
						// 			paramObjForPer,
						// 			headerObj
						// 		).then(function (response) {

						// 			if (response.data.status == "success") {

						// 			var now = moment(); // current date & time

						// 			/* ---------------- ACTIVE MEDICATION FILTER ---------------- */
						// 			$scope.detailedMedications = response.data.listObject.filter(function (value) {

						// 				if (!value.startDate || !value.endDate) {
						// 				return false;
						// 				}

						// 				// combine startDate + addedTime
						// 				var startDateTime = moment(
						// 				value.startDate + ' ' + (value.addedTime || '00:00'),
						// 				"DD/MM/YYYY HH:mm"
						// 				);

						// 				// end date till end of day
						// 				var endDateTime = moment(
						// 				value.endDate,
						// 				"DD/MM/YYYY"
						// 				).endOf('day');

						// 				return (
						// 				now.isSameOrAfter(startDateTime) &&
						// 				now.isSameOrBefore(endDateTime) &&
						// 				value.isMedicationStop === "N"
						// 				);
						// 			});

						// 			/* ---------------- DATE FORMATTING ---------------- */
						// 			angular.forEach($scope.detailedMedications, function (value) {

						// 				var addedDate1 = moment(value.addedDate, "DD/MM/YYYY").format("DD-MMM-YYYY");
						// 				var startDate1 = moment(value.startDate, "DD/MM/YYYY").format("DD-MMM-YYYY");
						// 				var endDate1 = moment(value.endDate, "DD/MM/YYYY").format("DD-MMM-YYYY");

						// 				value.addedDate1 = (addedDate1 === "Invalid date") ? "" : addedDate1;
						// 				value.startDate1 = (startDate1 === "Invalid date") ? "" : startDate1;
						// 				value.endDate1 = (endDate1 === "Invalid date") ? "" : endDate1;

						// 				if (value.doseDesc == null) {
						// 				value.doseDesc = "";
						// 				}
						// 				if (value.strengthDesc == null) {
						// 				value.strengthDesc = "";
						// 				}

						// 				value.addedTime = moment(value.addedTime, "HH:mm").format("hh:mm A");

						// 			});
						// 			}
						// 		});
						// 		};




								 $scope.activeMedications = [];
							$scope.getAllActiveMedication = function(patientId, admissionId, doctorId) {
								$scope.detailedMedications = [];
								$scope.activeMedications = [];
								var patientId = patientId;
								var dateObj = moment(new Date()).format(
										'YYYY-MM-DD');

								var URI = BASE_URL + ROOT_URL
										+ "/api/opd/getActiveMedication" + "?patientId="
										+ patientId + "&date=" + dateObj + "&unitId="+ $scope.unitId
								GenericServiceParamHeader
										.serviceAction(METHOD_GET, URI)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.activeMedications = response.data.listObject;
													
														angular.forEach($scope.activeMedications,function(value,key){
								    						value.checkTrue='N';
								    						if(value.strengthDesc == null ){
																value.strengthDesc = "";
															}
															if(value.doseDesc == null){
																value.doseDesc = "";
															}
															 value.addedTime = moment(value.addedTime, "HH:mm").format("hh:mm A");
								    					});
														console.log("activeMedication",$scope.activeMedications);
														$scope.detailedMedications=$scope.activeMedications
													}
												});
							}

        $scope.goToPOCWorklist = function(mapList){
            if(mapList!=null)
            {
                cookieObjectAppConfigSecond.pocWorklistObj = {};
                cookieObjectAppConfigSecond.pocWorklistObj = mapList;
                localStorage.setItem('cookieObjectAppConfigSecond', JSON.stringify(cookieObjectAppConfigSecond));

                $state.go('pocWorklist',
                {
                    obj : mapList,
                });
            }
            else
           {
                growl.error("Something went Wrong.",{title : 'Error!'});
           }
        }


		$scope.defaultAllocationSel = function(info){
			console.log('Old ====================================',info.oldTriageCategoryId,"  New =====================>",info.currentTriage);
			console.log(info);
			 $scope.selectedInfo = info;
			$scope.patientNameforModel = info.pFirstName;
			$scope.admissionDetailsIdforModel = info.admissionDetailsId;
			$scope.traiageCategoryIdForModel = info.currentTriage;
			console.log('====================================');
			angular.element('#confirmTriageChange').modal('show');
		}

		$scope.storeOldValue = function(info) {
    		info.oldTriageCategoryId = info.currentTriage;
		};

		$scope.confirmTriageChange = function() {

					var commonObj = {
						admissionDetailsId: $scope.admissionDetailsIdforModel,
						triageCategoryId: $scope.traiageCategoryIdForModel,
						userId:$scope.userId
					};

					$rootScope.startLoader();

					var URI = BASE_URL + ROOT_URL + "/adt/saveTriageCurrentDetails"; // your API

					GenericService.serviceAction("POST", URI, commonObj)
						.then(function(response) {

							$rootScope.stopLoader();

							if (response.data.status == "success") {

							
								$scope.defaultAllocationSel($scope.selectedInfo);

								angular.element('#confirmTriageChange').modal('hide');

							} else {
								console.log("Error in response");
								angular.element('#confirmTriageChange').modal('hide');
							}

						}).catch(function(error) {
							$rootScope.stopLoader();
							console.log("API Error:", error);
							angular.element('#confirmTriageChange').modal('hide');
						});
				};


			$scope.cancelTriageChange = function(){

				if ($scope.selectedInfo) {
					$scope.selectedInfo.currentTriage = $scope.selectedInfo.oldTriageCategoryId;
				}
				angular.element('#confirmTriageChange').modal('hide');			
				$scope.$applyAsync();
				};

            $scope.getActiveComplaints=function(erPatient){
                 if(erPatient.patientId==null || erPatient.patientId=='' || erPatient.patientId==undefined)
                {
                    growl.warning("Please select Patient..",{
                        title : ERROR_MSG

                    });
                    return false;
                }
                $scope.complaintsPatientList=[];
                var headerObj = {
                    organization_id : $scope.organizationId
                };

                var paramObj = {
                    patientId : erPatient.patientId,
                };
                var data = {};
                var URI = BASE_URL + ROOT_URL + "/api/opd/getComplaintAppointmentByPatientId";
                GenericServiceParamHeader.serviceAction(METHOD_GET, URI, data,NOTIFICATION_MSG_STATUS_FALSE,paramObj, headerObj)
                    .then(
                        function(response) {
                            console.log(response);
                            if (response.data.status == "success")
                                $scope.complaintsPatientList = response.data.listObject;
                            console.log("complaintsPatientList", $scope.complaintsPatientList);
                    });
            }

            $scope.getComplaintTemplateByIcpcId=function(templateTypeId,obj){

                $rootScope.startLoader();
                var data = {
                    orgId : $scope.organizationId,
                    unitId : $scope.unitId,
                    templateTypeId : templateTypeId,
                    patientId : $scope.patientDetails.patientId,
                    mIcpcId :obj.diagnosisId
                }

                console.log('data', JSON.stringify(data));
                var URI = BASE_URL + ROOT_URL
                        + "/global/petient/template";
                BillingGenericService
                    .serviceAction(METHOD_POST, URI, data,
                        NOTIFICATION_MSG_STATUS_FALSE)
                        .then(
                            function(response) {

                                    if (response.data.status == 'success') {
                                        $scope.templateTransactionList = response.data.listObject;
                                        $('#viewtemplateModal').modal('show');
                                        $rootScope.stopLoader();
                                    } else if (response.data.status == 'error') {
                                        $rootScope.stopLoader();
                                    }
                        });

            }
																
							
							} ]);