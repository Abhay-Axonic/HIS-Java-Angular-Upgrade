
/**
 * 
 */


angular.module('myApp')
		.controller("dietOrderPatientListController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','BillingGenericService','$window',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService,$window){
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
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			
			$scope.unitId = cookieObject.unitId;
			$scope.organisationId = cookieObject.organizationId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
		    $scope.nursingStationsWardsList = JSON.parse(sessionStorage.getItem('nursingStationsWardsList'));
			$scope.PopupOkBtn = function() {
				$scope.popUpFlag = false;
				$scope.acceptRequest();

			}

			var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
			 if(cookieObjectAppConfig == undefined){
			  $state.go('login');
			  return;
			 }
			 
			 
			 $scope.dateFormat = cookieObjectAppConfig.dateFormat;
			 $scope.skipHousekeeping = cookieObjectAppConfig.skipHousekeeping;
			 var specialtyNursingStationCookies = JSON.parse(sessionStorage.getItem('specialtyNursingStationCookiesNur'));
				var nursingStationCookie = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
				$scope.nursingStationCookieShow = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
				 if(specialtyNursingStationCookies == undefined){
					 growl.warning(
								'No nursing station and specialty mapped with current user!!',
								{
									title : 'Success!'
								});
				 }
				 else
				  {
				   console.log('specialtyNursingStationCookies',specialtyNursingStationCookies);
				   console.log('nursingStationCookie',nursingStationCookie);
				   $scope.nursingStationDesc=nursingStationCookie;
				   $scope.departmentNursingStationMapperDtoslist = specialtyNursingStationCookies.departmentNursingStationlist;
					console.log("ggg",$scope.departmentNursingStationMapperDtoslist);
				  }
			 
					
				    $scope.isWardChanged = function()
				    {
				       
				      
		            		//start for nursing
		        				var URI = BASE_URL+ROOT_URL+"/api/global/getDetailedNursingStationList/"+$scope.userId+"/"+$scope.organizationId+"/"+$scope.unitId;
		        				  GenericService.serviceAction("GET",URI,{}).then(function(response){
		        					  if (response.data.status == "success") {
		        						  $scope.departmentNursingStationMapperDtoslist = [];
		        						  
		        							$scope.departmentNursingStationMapperDtoslist = response.data.listObject;
		        							//alert($scope.roleName);
		        							if($scope.nurseRole == 1){
		        							if($scope.departmentNursingStationMapperDtoslist.length > 0)
		        								{
		        									if($scope.departmentNursingStationMapperDtoslist.length > 1){
		        										angular.element('#nursingStationModal').modal('show');
		        										//$('#nursingStationModal').modal({backdrop: 'static', keyboard: false});
		        									}
		        									else
		        										{
		        										var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+$scope.departmentNursingStationMapperDtoslist[0].nursingStationId+"/"+$scope.organizationId+"/"+$scope.unitId;
		        										  GenericService.serviceAction("GET",URI,{}).then(function(response){
		        											  if (response.data.status == "success") {
		        												  $scope.departmentNursingStationListC = {};
		        												 // console.log("fff",response.data.object);
		        												  $scope.departmentNursingStationListC.departmentNursingStationlist = response.data.listObject;
		        												  
		        												  sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify($scope.departmentNursingStationListC));
		        												  sessionStorage.setItem('specialtyNursingStationCookiesNur', JSON.stringify($scope.departmentNursingStationListC));
		        												  sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(response.data.object));
		        												  sessionStorage.setItem('nursingStationId', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationId));
		        												  //$cookies.putObject('nursingStationDesc',$scope.departmentNursingStationMapperDtoslist.nursingStationDesc);
		        												  sessionStorage.setItem('nursingStationDesc', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationDesc));
		        												  $scope.wardListForGetBedList = response.data.object;
		        											  }
		        										 });
		        										}
		        								}
		        					  		}
		        						}
		        				 });
				        
				    }
				    
				    $scope.getWardList=function(visitTypeId){
				    	$scope.wardDataList=[];
				    	var data = {
								organizationId:$scope.organizationId,
								unitId:$scope.unitId,
								isEr: visitTypeId == 4 ? 'Y':'N',
								isIpWard: visitTypeId == 2 ? 'Y':'N',
								isDayCare: visitTypeId == 3 ? 'Y':'N'
						};
						
						
						var URI = BASE_URL + ROOT_URL
						+ "/api/adt/getWardByVisitType";											
						
						GenericService.serviceAction("POST",URI,data)
						.then(
								function(response) {
									console.log(response);
									// $rootScope.startLoader();
									if (response.data.status == "success")
										$scope.wardDataList = response.data.listObject;
								});
					}
					
					
				    
				    
				    $scope.departmentNursing = {};
					$scope.selectsetNursingStation = function()
					{
						if($scope.departmentNursing == undefined || $scope.departmentNursing.length == 0){
							growl.warning('Please select nursing station..!!');
							return false;
						}else{
						$scope.wardListForGetBedList=[];
						var data = JSON.parse($scope.departmentNursing);
						//console.log("$scope.departmentNursing",$scope.departmentNursing);
						//console.log("$scope.departmentNursing ID",data.nursingStationId);
						var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+data.nursingStationId+"/"+$scope.organizationId+"/"+$scope.unitId;
						  GenericService.serviceAction("GET",URI,{}).then(function(response){
							  if (response.data.status == "success") {
								  $scope.departmentNursingStationListC = {};
								  //console.log('nursingStationsWardsList',response.data.object);
								    $scope.departmentNursingStationListC.departmentNursingStationlist = response.data.listObject;
									sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify($scope.departmentNursingStationListC));
									sessionStorage.setItem('specialtyNursingStationCookiesNur', JSON.stringify($scope.departmentNursingStationListC));
									sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(response.data.object));
									sessionStorage.setItem('nursingStationDesc', JSON.stringify(data.nursingStationDesc));
									sessionStorage.setItem('nursingStationId', JSON.stringify(data.nursingStationId));
									$scope.wardListForGetBedList = response.data.object;
									angular.element('#nursingStationModal').modal('hide');
//									$state.reload("patientAcceptance");
									var specialtyNursingStationCookies = JSON.parse(sessionStorage.getItem('specialtyNursingStationCookiesNur'));
									var nursingStationCookie = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
									$scope.nursingStationCookieShow = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
									 if(specialtyNursingStationCookies == undefined){
										 growl.warning(
													'No nursing station and specialty mapped with current user!!',
													{
														title : 'Success!'
													});
									 }
									 else
									  {
									   console.log('specialtyNursingStationCookies',specialtyNursingStationCookies);
									   console.log('nursingStationCookie',nursingStationCookie);
									   $scope.nursingStationDesc=nursingStationCookie;
									   $scope.departmentNursingStationMapperDtoslist = specialtyNursingStationCookies.departmentNursingStationlist;
										console.log("ggg",$scope.departmentNursingStationMapperDtoslist);
									  }
									 
									  $scope.nursingWardList =response.data.object; 
									 $scope.getAdmittedPatientList();
									 $scope.getNewAdmissionList();
									 $scope.getTransferListTap();
									 $scope.getICUTransferList();
								}
						 });
					}
					}
			
				
				// For Paginations Start for nursing patient acceptance
				$scope.setNoOfRecords = function() {
					$scope.initAdmittedPatientList($scope.organizationId,$scope.unitId, $scope.offset,$scope.noOfRecordsPerPage);
				};
				$scope.initAdmittedPatientList = function(orgId,unitId,offset, noOfRecordsPerPage) {
					
					var wardIds = [];
                    if($scope.wardId!=undefined){
                        wardIds[0] =parseInt($scope.wardId);
                    }else{
                        wardIds=$scope.nursingStationsWardsList;
                    }

					offset = offset != null ? offset : 0;
					noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;
					var commonObjForTransfer = {
							unitId : $scope.unitId,
							organizationId : $scope.organizationId,
//							wardId :$scope.wardId ,
                            wardIds :wardIds,
							offset : offset,
							noOfRecordsPerPage : noOfRecordsPerPage,
							visitTypeId :$scope.visitTypeId != undefined ? $scope.visitTypeId :2,
//							isFromNursingAcceptancePage:'Y'
							};
					
						var URI = BASE_URL + ROOT_URL+ "/adt/getAdmittedPatientListForDietOrder";
						$rootScope.startLoader();
				GenericService
						.serviceAction("POST", URI, commonObjForTransfer)
						.then(
								function(response) {
									$scope.listOfAdmittedPatientListforAddmitted = [];
									$scope.admittedPatientList = [];

									if (response.data.status == "success") {
										
										if(response.data.listObject.length==0)
											$scope.loadingMsg="No records found...!!";
										$scope.admittedPatientList = response.data.listObject;
										$scope.commonListCount = response.data.object;
										angular.forEach($scope.admittedPatientList,
														function(value,index) {

															var newArr = {};
															newArr.specialityName = value.specialityName;
															newArr.doctorFullName = value.dFirstName;
															newArr.pFirstName = value.pFirstName;
															newArr.reasonDesc = value.reasonDesc;
															newArr.dob = value.dob;
															newArr.datepicker = moment(value.doa,'DD/MM/YYYY').format($scope.dateFormat.toUpperCase());
															newArr.datepickerPdd = moment(value.pdd,'DD/MM/YYYY').format($scope.dateFormat.toUpperCase());
															newArr.genderCode = value.genderCode;
															newArr.genderId = value.genderId;
															newArr.doctorId = value.doctorId;
															newArr.specialityId = value.specialityId;
															newArr.uhidnumber = value.uhidnumber;
															newArr.createdTime = value.createdTime;
															newArr.admissionNumber = value.admissionNumber;
															newArr.patientId = value.patientId;
															newArr.tPatientId = value.tPatientId;
															newArr.paymentEntitlementDesc = value.paymentEntitlementDesc;
															newArr.bedNumber = value.bedNumber;
															newArr.admissionId = value.admissionId;
															newArr.prefixCode = value.prefixCode;
															newArr.identificationNo = value.identificationNo;
															newArr.wardName = value.wardName;
															newArr.concent = value.concent;
															newArr.bedCategoryId=value.bedCategoryId;
															newArr.patientCategoryId=value.patientCategoryId;
															newArr.visitTypeId=value.visitTypeId;
															newArr.paymentEntitlementId=value.paymentEntitlementId;
															newArr.contractId = value.contractId;
															newArr.dischargeStatusId=value.dischargeStatusId;
															newArr.billingBedCategoryId = value.billingBedCategoryId;
															newArr.isCriticalOrder = value.isCriticalOrder;
															newArr.medicoLegalId = value.medicoLegalId;
															newArr.isVip = value.isVip;
                                                            newArr.dischargeId = value.dischargeId;
                                                            newArr.ageStr = value.ageStr;
                                                            newArr.imagePath = value.imagePath;
                                                            newArr.prefixDesc = value.prefixDesc;
                                                            newArr.prefixId = value.prefixId;
                                                            newArr.ageStr = value.ageStr;
                                                            newArr.visitTypeId= value.visitTypeId;
                                                            newArr.wardId = value.wardId;
                                                            newArr.floorName = value.floorName;
                                                            newArr.wardName = value.wardName;
                                                            newArr.roomId = value.roomId;
                                                            newArr.bedId= value.bedId;
                                                            newArr.uuid= value.uuid;
                                                            newArr.isIcuu= value.isIcuu;
                                                            newArr.isFinalBill= value.isFinalBill;
                                                            newArr.referenceNumber= value.referenceNumber;
                                                            console.log("visitTypeData",value.visitTypeId);
                                                            //ip
//                                                            if($scope.visitTypeId == "2" ){
//															if(value.visitTypeId==2){
															$scope.listOfAdmittedPatientListforAddmitted .push(newArr);
//															console.log("list data",$scope.listOfAdmittedPatientListforAddmitted );
////															}
//                                                            }
//                                                            //day care
//                                                            if($scope.visitTypeId == "3" ){
////																if(value.visitTypeId==3){
//																$scope.listOfAdmittedPatientListforAddmitted .push(newArr);
//																console.log("list data",$scope.listOfAdmittedPatientListforAddmitted );
////																}
//                                                                }
//                                                            
															$scope.setPage(1,false);
														});
										$rootScope.stopLoader();
									}else{
										$rootScope.stopLoader();
									}
								});
						

					
				}
				$scope.initAdmittedPatientList($scope.organizationId,$scope.unitId, $scope.offset,$scope.noOfRecordsPerPage);
			
				
				$scope.gettAdmittedPatientList = function(orgId,unitId,offset, noOfRecordsPerPage) {
					console.log("offset", offset);
					var data = "";
					offset = offset != null ? offset : 0;
					noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;
					var URI = BASE_URL + ROOT_URL +  "/adt/getAdmittedPatientListForDietOrder";
					
					var commonObjForTransfer = {
							unitId : $scope.unitId,
							organizationId : $scope.organizationId,
							wardId :$scope.wardId ,
							offset : offset,
							noOfRecordsPerPage : noOfRecordsPerPage,
							visitTypeId :$scope.visitTypeId != undefined ? $scope.visitTypeId :2,
//							isFromNursingAcceptancePage:'Y'
							};
					
					
					if($scope.isShowPatientByBed=='Y'){
						if($scope.nurseBedList.length>0){
							commonObjForTransfer.bedList=$scope.nurseBedList;
							commonObjForTransfer.isShowPatientByBed='Y';
						}else{
						   $scope.nursingWardList=[];
						}
					}
					else{
						commonObjForTransfer.bedList=$scope.nurseBedList;
						commonObjForTransfer.isShowPatientByBed='N';
					}

					if($scope.nurseRole == 1 && $scope.nursingWardList.length > 0){
						
						var URI = BASE_URL + ROOT_URL+  "/adt/getAdmittedPatientListForDietOrder";
						$rootScope.startLoader();
				GenericService
						.serviceAction("POST", URI, commonObjForTransfer)
						.then(
								function(response) {
									$scope.listOfAdmittedPatientListforAddmitted = [];
									$scope.admittedPatientList = [];

									if (response.data.status == "success") {
										$scope.admittedPatientList = response.data.listObject;
										angular.forEach($scope.admittedPatientList,
														function(value,index) {

															var newArr = {};
															newArr.specialityName = value.specialityName;
															newArr.doctorFullName = value.dFirstName;
															newArr.pFirstName = value.pFirstName;
															newArr.reasonDesc = value.reasonDesc;
															newArr.dob = value.dob;
															newArr.datepicker = value.doa;
															newArr.datepickerPdd = value.pdd;
															newArr.genderCode = value.genderCode;
															newArr.genderId = value.genderId;
															newArr.doctorId = value.doctorId;
															newArr.specialityId = value.specialityId;
															newArr.uhidnumber = value.uhidnumber;
															newArr.createdTime = value.createdTime;
															newArr.admissionNumber = value.admissionNumber;
															newArr.patientId = value.patientId;
															newArr.tPatientId = value.tPatientId;
															newArr.paymentEntitlementDesc = value.paymentEntitlementDesc;
															newArr.bedNumber = value.bedNumber;
															newArr.admissionId = value.admissionId;
															newArr.prefixCode = value.prefixCode;
															newArr.identificationNo = value.identificationNo;
															newArr.wardName = value.wardName;
															newArr.concent = value.concent;
															newArr.bedCategoryId=value.bedCategoryId;
															newArr.patientCategoryId=value.patientCategoryId;
															newArr.visitTypeId=value.visitTypeId;
															newArr.paymentEntitlementId=value.paymentEntitlementId;
															newArr.contractId = value.contractId;
															newArr.dischargeStatusId=value.dischargeStatusId;
															newArr.billingBedCategoryId = value.billingBedCategoryId;
															newArr.medicoLegalId = value.medicoLegalId;
															newArr.dischargeId = value.dischargeId;
															newArr.isCriticalOrder = value.isCriticalOrder;
															newArr.isVip = value.isVip;
															newArr.ageStr = value.ageStr;
                                                            newArr.imagePath = value.imagePath;
                                                            newArr.prefixDesc = value.prefixDesc;
                                                            newArr.prefixId = value.prefixId;
                                                            newArr.ageStr = value.ageStr;
                                                            newArr.wardId = value.wardId;
                                                            newArr.floorName = value.floorName;
                                                            newArr.wardName = value.wardName;
                                                            newArr.roomId = value.roomId;
                                                            newArr.bedId= value.bedId;
                                                            newArr.uuid= value.uuid;
                                                            newArr.referenceNumber= value.referenceNumber;
                                                            
//															if(value.visitTypeId==2){
															$scope.listOfAdmittedPatientListforAddmitted.push(newArr);
															
//															}

														});
										$rootScope.stopLoader();
									}else{
										$rootScope.stopLoader();
									}
								});
						}
				}

				$scope.pager = {};
				$scope.page;

				$scope.setPage = function(page, flag) {
					if (page < 1 || page > $scope.pager.totalPages) {
						return;
					}
					$scope.pager = PagerService.GetPager($scope.commonListCount, page,$scope.noOfRecordsPerPage);
					if (flag) {
						$scope.gettAdmittedPatientList($scope.organizationId,$scope.unitId,$scope.pager.startIndex,$scope.pager.pageSize);
					}
				}

//				$scope.initAdmittedPatientList($scope.organizationId,$scope.unitId,$scope.offset, $scope.noOfRecordsPerPage);

			    	// For Paginations End
			
			/* init() function for form object create. */
			$scope.init = function() 
			{
				
				var cookieObject = $cookies.getObject('cookieObject');
				if(cookieObject == undefined){
					$state.go('login');
					return;
				}
				$scope.unitId = cookieObject.unitId;
				$scope.organizationId = cookieObject.organizationId;
				$scope.userId = cookieObject.userId;
				$scope.storeId = cookieObject.storeId;
				
				var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
				 if(cookieObjectAppConfig == undefined){
				  $state.go('login');
				  return;
				 }
				 else
				  {
				   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
				   
				  }
				 
					/** Variables for pagination */
					$scope.offset = 0;
					$scope.noOfRecordsPerPage = 10;
					$scope.totalNoOfRecords;
					$scope.searchKeyword;
					$scope.commonListCount;
				 
				$scope.roleName = cookieObjectAppConfig.roleName;
				$scope.nurseRole = cookieObjectAppConfig.nurseRole;	 
				$scope.doctorRole = cookieObjectAppConfig.doctorRole;
				$scope.skipAdmissionAcceptance = cookieObjectAppConfig.skipAdmissionAcceptance;
				$scope.doctorRole = cookieObjectAppConfig.doctorRole;
				
				$scope.isShowPatientByBed=cookieObjectAppConfig.isShowPatientByBed;
				
				$scope.nursingWardList = [];
				var nursingStationsWardsList = JSON.parse(sessionStorage.getItem('nursingStationsWardsList'));
				 if(nursingStationsWardsList == undefined){
					 growl.warning(
								'No ward mapped with selected nursing station !!',
								{
									title : 'Success!'
								});
				 }
				 else
				  {
				   console.log('nursingStationsWardsList',nursingStationsWardsList);
				   $scope.nursingWardList =nursingStationsWardsList; 
				  
				   console.log('nursingWardList',$scope.nursingWardList);
				   
				  }
				
				    $scope.nurseBedList = [];
					var mappedNurseBedList = JSON.parse(sessionStorage.getItem('mappedNurseBedList'));
					 if( (mappedNurseBedList == undefined || mappedNurseBedList.length==0) && $scope.isShowPatientByBed=='Y'){
						 growl.warning(
									'No bed mapped with nurse !!',
									{
										title : 'Success!'
									});
					 }
					 else
					  {
					   $scope.nurseBedList =mappedNurseBedList; 
					  console.log('Nurse Bed List',$scope.nurseBedList);
					  }
					
				
				$rootScope.loginpage = true;
				$scope.saveBtnFlag = false;
				$scope.updateBtnFlag = true;

				// for popup
				$scope.showModal = false;
				$scope.buttonClicked = "";
				$scope.popUpFlag = true;
				// end for popup

				$scope.admittedPatient = {};
				$scope.itemSelecteds = {};
				$rootScope.itemSelectedsAccept = {};
				$scope.bedTr = {};
				$scope.patientIcuTransfer = {};
				$scope.beditemSelecteds = {};

				var data = {};
				var commonObj = {
					unitId : $scope.unitId,
					organizationId : $scope.organizationId,
				};
				
				var commonObjForTransfer = {
						unitId : $scope.unitId,
						organizationId : $scope.organizationId,
						wardList : $scope.nursingWardList
						};
				
				
				if($scope.isShowPatientByBed=='Y'){
					if($scope.nurseBedList.length>0){
						commonObjForTransfer.bedList=$scope.nurseBedList;
						commonObjForTransfer.isShowPatientByBed='Y';
					}else{
					   $scope.nursingWardList=[];
					}
				}
				else{
					commonObjForTransfer.bedList=$scope.nurseBedList;
					commonObjForTransfer.isShowPatientByBed='N';
				}

				var URI = BASE_URL + ROOT_URL
						+ GETACTIVEREASONLIST;
				GenericService
						.serviceAction("POST", URI, commonObj)
						.then(
								function(response) {
									$scope.reasonList = [];

									if (response.data.status == "success") {
										$scope.reasonList = response.data.listObject;

									}
								});
				var URI = BASE_URL + ROOT_URL
						+ GETACTIVEBEDCATEGORYLISTBYUNIT;
				GenericService
						.serviceAction("POST", URI, commonObj)
						.then(
								function(response) {
									$scope.bedCategoryList = [];

									if (response.data.status == "success") {
										$scope.bedCategoryList = response.data.listObject;

									}
								});
				
				//$scope.getNewAdmissionList();
			}
			
			$scope.init();
			
			
			
			$scope.goToDietPlanning=function(info){
				
				 sessionStorage.setItem('cookieDietObject', JSON.stringify(info));	
				$state.go('dietPatientRequest', {patientDetailObj : info});
			}
			
			
			// List--------//
			$scope.getwardList = function() {
				var data={
							organizationId: $scope.organizationId,
							unitId : $scope.unitId,
				};
				var URI = BASE_URL+ ROOT_URL
						+ "/api/adt/getActiveWardMasterList";

				GenericService.serviceAction("POST", URI, data).then(function(response) {
									$rootScope.stopLoader();
									$scope.listofWard = [];
									$scope.listofWard = response.data.listObject;
								});
			}
			$scope.getwardList();
			
			$scope.getClassForColor = function(info)
			{
				  if(info.isCriticalOrder==1){ 
					   return 'td-bg-red';
				   }else if (info.isVip=='Y') 
				   {
					    return 'td-bg-blue';
				   }else if(info.appointmentTypeId==6){
					   return 'td-bg-green';
				   }
			}
			//search filter
			$scope.loadingMsg="";
			$scope.universalSearchForAdmittedPatientList = function(searchString) {
				$scope.listOfAdmittedPatientListforAddmitted = [];
				$scope.admittedPatientList = [];
				$scope.loadingMsg="Loading...";
				if(searchString!=''){
				var commonObjForTransfer = {
						unitId : $scope.unitId,
						organizationId : $scope.organizationId,
						wardList : $scope.nursingWardList,
						offset : $scope.offset,
						noOfRecordsPerPage : $scope.noOfRecordsPerPage,
						patientName : searchString,
                        visitTypeId : parseInt($scope.visitTypeIdflag) 
						};
				
				if($scope.isShowPatientByBed=='Y'){
					if($scope.nurseBedList.length>0){
						commonObjForTransfer.bedList=$scope.nurseBedList;
						commonObjForTransfer.isShowPatientByBed='Y';
					}else{
					   $scope.nursingWardList=[];
					}
				}
				else{
					commonObjForTransfer.bedList=$scope.nurseBedList;
					commonObjForTransfer.isShowPatientByBed='N';
				}

				if($scope.nurseRole == 1 && $scope.nursingWardList.length > 0){
					
					var URI = BASE_URL + ROOT_URL+  "/adt/admittedPatientListUniversalSearchForDietOrder";
					$rootScope.startLoader();
			GenericService
					.serviceAction("POST", URI, commonObjForTransfer)
					.then(
							function(response) {
								$scope.admittedPatientList=[];
								$scope.listOfAdmittedPatientListforAddmitted=[];
								if (response.data.status == "success") {
									if(response.data.listObject.length==0){
										$scope.loadingMsg="No records found...!!";
										$rootScope.stopLoader();
									}else{
									$scope.admittedPatientList = response.data.listObject;
									angular.forEach($scope.admittedPatientList,
													function(value,index) {

														var newArr = {};
														newArr.specialityName = value.specialityName;
														newArr.doctorFullName = value.dFirstName;
														newArr.pFirstName = value.pFirstName;
														newArr.reasonDesc = value.reasonDesc;
														newArr.dob = value.dob;
//														newArr.datepicker = value.doa;
														newArr.datepicker = moment(value.doa,'DD/MM/YYYY').format($scope.dateFormat.toUpperCase());
														newArr.datepickerPdd = moment(value.pdd,'DD/MM/YYYY').format($scope.dateFormat.toUpperCase());
														newArr.createdTime = value.createdTime;
														newArr.admissionNumber = value.admissionNumber;
//														newArr.datepickerPdd = value.pdd;
														newArr.genderCode = value.genderCode;
														newArr.genderId = value.genderId;
														newArr.doctorId = value.doctorId;
														newArr.specialityId = value.specialityId;
														newArr.uhidnumber = value.uhidnumber;
														newArr.patientId = value.patientId;
														newArr.tPatientId = value.tPatientId;
														newArr.paymentEntitlementDesc = value.paymentEntitlementDesc;
														newArr.bedNumber = value.bedNumber;
														newArr.admissionId = value.admissionId;
														newArr.prefixCode = value.prefixCode;
														newArr.identificationNo = value.identificationNo;
														newArr.wardName = value.wardName;
													    newArr.floorName = value.floorName;
														newArr.concent = value.concent;
														newArr.bedCategoryId=value.bedCategoryId;
														newArr.patientCategoryId=value.patientCategoryId;
														newArr.visitTypeId=value.visitTypeId;
														newArr.paymentEntitlementId=value.paymentEntitlementId;
														newArr.contractId = value.contractId;
														newArr.dischargeStatusId=value.dischargeStatusId;
														newArr.billingBedCategoryId = value.billingBedCategoryId;
														newArr.medicoLegalId = value.medicoLegalId;
														newArr.dischargeId=value.dischargeId;
														newArr.prefixDesc = value.prefixDesc;
													    newArr.imagePath = value.imagePath;
														newArr.ageStr = value.ageStr;
														newArr.wardId = value.wardId;
                                                        newArr.roomId = value.roomId;
                                                        newArr.bedId= value.bedId;
                                                        newArr.uuid= value.uuid;
                                                        newArr.isIcuu= value.isIcuu;
                                                        newArr.isFinalBill= value.isFinalBill;
                                                    	newArr.isCriticalOrder = value.isCriticalOrder;
														newArr.isVip = value.isVip;
                                                        newArr.referenceNumber= value.referenceNumber;
//														if(value.visitTypeId==2){
														$scope.listOfAdmittedPatientListforAddmitted .push(newArr);
//														}
														
													});
									}
									$rootScope.stopLoader();
								}else{
									$rootScope.stopLoader();
									$scope.loadingMsg="No records found...!!";
								}
							});
					}
			}else{
				$scope.initAdmittedPatientList($scope.organizationId,$scope.unitId, $scope.offset,$scope.noOfRecordsPerPage);
			}
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
			
}]);