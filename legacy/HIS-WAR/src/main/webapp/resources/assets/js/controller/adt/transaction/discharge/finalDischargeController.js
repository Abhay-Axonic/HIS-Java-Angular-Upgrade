'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:finalDischargeController
 * @description #finalDischargeController Controller of the myApp
 */

angular.module('myApp').controller('finalDischargeController',
	['$scope','$http','$localStorage','$sessionStorage','$cookies','$rootScope','GenericService','growl','$state','$stateParams','PagerService',
		'BillingGenericService','$window',
		function($scope, $http, $localStorage, $sessionStorage,$cookies, $rootScope, GenericService, growl,$state,$stateParams,PagerService,BillingGenericService,$window) {
			/* init() function for form object create. */
			$scope.visitTypeIdflag ='2';
			$scope.dropdown = 'N';
			
			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.admittedPatientCount;
			
			$scope.init = function() {
				var cookieObject = $cookies.getObject('cookieObject');
				if(cookieObject == undefined){
					$state.go('login');
					return;
				}
				$scope.unitId = cookieObject.unitId;
				$scope.organizationId = cookieObject.organizationId;
				$scope.userId = cookieObject.userId;
				$scope.isEr=false;
				$rootScope.loginpage = true;
					
				var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig'); 
				if(cookieObjectAppConfig == undefined){
					$state.go('login');
					return;
				}
				else
				{
					console.log('cookieObjectAppConfig',cookieObjectAppConfig);
				}				 
				$scope.roleName = cookieObjectAppConfig.roleName;
				$scope.nurseRole = cookieObjectAppConfig.nurseRole;	 
				$scope.doctorRole = cookieObjectAppConfig.doctorRole;
				$scope.isShowPatientByBed=cookieObjectAppConfig.isShowPatientByBed;
			    $scope.offset = 0;
				$scope.noOfRecordsPerPage = 10;
				$scope.totalNoOfRecords;
				$scope.searchKeyword;
				$scope.commonListCount;

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
				$scope.wardList = [];
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
					$scope.wardList = nursingStationsWardsList; 
					/*console.log('nursingStationCookie',nursingStationCookie);
					$scope.nursingStationDesc=nursingStationCookie;
					$scope.departmentNursingStationMapperDtoslist = specialtyNursingStationCookies.departmentNursingStationlist;
					console.log("departmentNursingStationMapperDtoslist",$scope.departmentNursingStationMapperDtoslist);*/
				}
				$scope.nurseBedList = [];
				var mappedNurseBedList = JSON.parse(sessionStorage.getItem('mappedNurseBedList'));
				if( (mappedNurseBedList == undefined || mappedNurseBedList.length==0) && $scope.isShowPatientByBed=='Y' ){
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
				var data = {};
				//offset = offset != null ? offset : 0;
				//noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;		
				if($scope.visitTypeIdflag =='2'){
						$scope.visitTypeId=2;
				}
				
				if($scope.visitTypeIdflag =='3'){
					$scope.visitTypeId=3;
				}
				var commonObj = {
						unitId : $scope.unitId,
						organizationId : $scope.organizationId,
						visitTypeId: $scope.visitTypeId,
						wardList : $scope.wardList,
						offset:$scope.offset != null ? $scope.offset : 0,
						noOfRecordsPerPage: $scope.noOfRecordsPerPage != null ? $scope.noOfRecordsPerPage : 10,
				};
												
				if ($scope.isShowPatientByBed == 'Y') {
					if($scope.nurseBedList.length>0){
						commonObj.bedList=$scope.nurseBedList;
					}else{
						$scope.wardList=[];	
					}
				}
								
				$scope.visitTypeId= 2;
				//patient redirected from bed management
							
				if(!angular.equals({},$stateParams.bedManagmentObj))
				{
					$scope.bedManagmentCookiesObj=$stateParams.bedManagmentObj;
					commonObj.admissionId=$scope.bedManagmentCookiesObj.admissionId;
				}
								
				/*if ($cookies.get('bedManagmentCookies') != undefined) {
				$scope.bedManagmentCookiesObj = JSON.parse($cookies.get('bedManagmentCookies'));
						
				$cookies.remove('bedManagmentCookies');
				}*/
				//end
				if($scope.nurseRole == 1 && $scope.wardList.length > 0){
					/*var URI = BASE_URL + ROOT_URL
					+ "/adt/getPatientFinalDischargeListNewForListAndCount";*/
					GenericService.serviceAction("POST", "", commonObj).then(function(response) {
//						$scope.listOfAdmittedPatientList = [];
						$scope.admittedPatientList = [];

						if (response.data.status == "success") {
							$scope.admittedPatientList = response.data.listObject;
							angular.forEach($scope.admittedPatientList,function(value,index) {

								var newArr = {};
								newArr.specialityName = value.specialityName;
								newArr.dFirstName = value.dFirstName;
								newArr.pFirstName = value.pFirstName;
								newArr.reasonDesc = value.reasonDesc;
								newArr.dob = value.dob;
								newArr.datepicker = value.doa;
								newArr.datepickerPdd = value.pdd;
								newArr.genderCode = value.genderCode;
								newArr.uhidnumber = value.uhidnumber;
								newArr.patientId = value.patientId;
								newArr.paymentEntitlementDesc = value.paymentEntitlementDesc;
								newArr.bedNumber = value.bedNumber;
								newArr.admissionId = value.admissionId;
								newArr.prefixCode = value.prefixCode;
								newArr.identificationNo = value.identificationNo;
								newArr.dischargeId = value.dischargeId;
								newArr.futureDischargeDate = value.futureDischargeDate;
								newArr.dischargeStatusId = value.dischargeStatusId;
								newArr.dischargeTypeId = value.dischargeTypeId;
								newArr.floorName = value.floorName;
								newArr.dischargeTypeName = value.dischargeTypeName;
								newArr.dischargeDesc= value.dischargeDesc;
								newArr.bedId = value.bedId;
								newArr.wardId  = value.wardId;
								newArr.wardName  = value.wardName;
								newArr.doctorId  = value.doctorId;
								newArr.nursingStationId = value.nursingStationId;
								newArr.floorId = value.floorId;
								newArr.roomId = value.roomId;
								newArr.bedCategoryId = value.bedCategoryId;
								newArr.isIcu = value.isIcu;
								newArr.billStatusCount = value.billStatusCount;
								newArr.orderDetailsCount = value.orderDetailsCount;
								newArr.isFinancialClearance = value.isFinancialClearance;
								newArr.referalDocId = value.referalDocId;
								newArr.imagePath = value.imagePath;
								newArr.age = value.ageStr;
								newArr.prefixDesc = value.prefixDesc;
								newArr.visitTypeId=value.visitTypeId;
								newArr.admissionNumber = value.admissionNumber;
								if($scope.visitTypeIdflag =='2'){
									if(value.visitTypeId == 2 ){
										$scope.listOfAdmittedPatientList.push(newArr);
									}
								}
								
								if($scope.visitTypeIdflag =='3'){
									if(value.visitTypeId == 3 ){
										$scope.listOfAdmittedPatientList.push(newArr);
									}
								}

							});

						}
					});
					//$scope.setPage(1,false);
					console.log("list data",$scope.listOfAdmittedPatientList);
				}
			}

			$scope.init();
			/*chetana*/
			/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
				$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
			};
			
			$scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage,unitId)
			{
			$rootScope.startLoader();
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;		
			if($scope.visitTypeIdflag =='2'){
				$scope.visitTypeId=2;
			}
			
			if($scope.visitTypeIdflag =='3'){
				$scope.visitTypeId=3;
			}
			var commonObj = {
					unitId : $scope.unitId,
					organizationId : $scope.organizationId,
					visitTypeId: $scope.visitTypeId,
					wardList : $scope.wardList,
					offset:offset,
					noOfRecordsPerPage:noOfRecordsPerPage,
			};
											
			if ($scope.isShowPatientByBed == 'Y') {
				if($scope.nurseBedList.length>0){
					commonObj.bedList=$scope.nurseBedList;
				}else{
					$scope.wardList=[];	
				}
			}
							
			$scope.visitTypeId= 2;
			//patient redirected from bed management
						
			if(!angular.equals({},$stateParams.bedManagmentObj))
			{
				$scope.bedManagmentCookiesObj=$stateParams.bedManagmentObj;
				commonObj.admissionId=$scope.bedManagmentCookiesObj.admissionId;
			}
							
			/*if ($cookies.get('bedManagmentCookies') != undefined) {
			$scope.bedManagmentCookiesObj = JSON.parse($cookies.get('bedManagmentCookies'));
					
			$cookies.remove('bedManagmentCookies');
			}*/
			//end
			if($scope.nurseRole == 1 && $scope.wardList.length > 0){
				var URI = BASE_URL + ROOT_URL
				+ "/adt/getPatientFinalDischargeListNewForListAndCount";
				GenericService.serviceAction("POST", URI, commonObj).then(function(response) {
					$scope.listOfAdmittedPatientList = [];
					$scope.admittedPatientList = [];

					if (response.data.status == "success") {
						$scope.admittedPatientList = response.data.listObject;
						$scope.admittedPatientCount = response.data.object;
						angular.forEach($scope.admittedPatientList,function(value,index) {

							var newArr = {};
							newArr.specialityName = value.specialityName;
							newArr.dFirstName = value.dFirstName;
							newArr.pFirstName = value.pFirstName;
							newArr.reasonDesc = value.reasonDesc;
							newArr.dob = value.dob;
							newArr.datepicker = value.doa;
							newArr.datepickerPdd = value.pdd;
							newArr.genderCode = value.genderCode;
							newArr.uhidnumber = value.uhidnumber;
							newArr.patientId = value.patientId;
							newArr.paymentEntitlementDesc = value.paymentEntitlementDesc;
							newArr.bedNumber = value.bedNumber;
							newArr.admissionId = value.admissionId;
							newArr.prefixCode = value.prefixCode;
							newArr.identificationNo = value.identificationNo;
							newArr.dischargeId = value.dischargeId;
							newArr.futureDischargeDate = value.futureDischargeDate;
							newArr.dischargeStatusId = value.dischargeStatusId;
							newArr.dischargeTypeId = value.dischargeTypeId;
							newArr.floorName = value.floorName;
							newArr.dischargeTypeName = value.dischargeTypeName;
							newArr.dischargeDesc= value.dischargeDesc;
							newArr.bedId = value.bedId;
							newArr.wardId  = value.wardId;
							newArr.wardName  = value.wardName;
							newArr.doctorId  = value.doctorId;
							newArr.nursingStationId = value.nursingStationId;
							newArr.floorId = value.floorId;
							newArr.roomId = value.roomId;
							newArr.bedCategoryId = value.bedCategoryId;
							newArr.isIcu = value.isIcu;
							newArr.billStatusCount = value.billStatusCount;
							newArr.orderDetailsCount = value.orderDetailsCount;
							newArr.isFinancialClearance = value.isFinancialClearance;
							newArr.referalDocId = value.referalDocId;
							newArr.imagePath = value.imagePath;
							newArr.age = value.ageStr;
							newArr.prefixDesc = value.prefixDesc;
							newArr.visitTypeId=value.visitTypeId;
							newArr.visitType=value.visitTypeName;
							newArr.admissionNumber = value.admissionNumber;
							if($scope.visitTypeIdflag =='2'){
								if(value.visitTypeId == 2 ){
									$scope.listOfAdmittedPatientList.push(newArr);
								}
							}
							
							if($scope.visitTypeIdflag =='3'){
								if(value.visitTypeId == 3 ){
									$scope.listOfAdmittedPatientList.push(newArr);
								}
							}

						});
						$scope.setPage(1,false);
					}
				});
				
				console.log("list data",$scope.listOfAdmittedPatientList);
				$rootScope.stopLoader();
			}
			}
			
			$scope.masterList = function(organizationId,offset, noOfRecordsPerPage,unitId) {
			$rootScope.startLoader();
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;	
				
			if($scope.visitTypeIdflag =='2'){
				$scope.visitTypeId=2;
			}
			
			if($scope.visitTypeIdflag =='3'){
				$scope.visitTypeId=3;
			}
			var commonObj = {
					unitId : $scope.unitId,
					organizationId : $scope.organizationId,
					visitTypeId: $scope.visitTypeId,
					wardList : $scope.wardList,
					offset:offset,
					noOfRecordsPerPage:noOfRecordsPerPage,
			};
											
			if ($scope.isShowPatientByBed == 'Y') {
				if($scope.nurseBedList.length>0){
					commonObj.bedList=$scope.nurseBedList;
				}else{
					$scope.wardList=[];	
				}
			}
							
			$scope.visitTypeId= 2;
			//patient redirected from bed management
						
			if(!angular.equals({},$stateParams.bedManagmentObj))
			{
				$scope.bedManagmentCookiesObj=$stateParams.bedManagmentObj;
				commonObj.admissionId=$scope.bedManagmentCookiesObj.admissionId;
			}
							
			/*if ($cookies.get('bedManagmentCookies') != undefined) {
			$scope.bedManagmentCookiesObj = JSON.parse($cookies.get('bedManagmentCookies'));
					
			$cookies.remove('bedManagmentCookies');
			}*/
			//end
			if($scope.nurseRole == 1 && $scope.wardList.length > 0){
				var URI = BASE_URL + ROOT_URL
				+ "/adt/getPatientFinalDischargeListNewForListAndCount";
				GenericService.serviceAction("POST", URI, commonObj).then(function(response) {
					$scope.listOfAdmittedPatientList = [];
					$scope.admittedPatientList = [];

					if (response.data.status == "success") {
						$scope.admittedPatientList = response.data.listObject;
						$scope.admittedPatientCount = response.data.object;
						angular.forEach($scope.admittedPatientList,function(value,index) {

							var newArr = {};
							newArr.specialityName = value.specialityName;
							newArr.dFirstName = value.dFirstName;
							newArr.pFirstName = value.pFirstName;
							newArr.reasonDesc = value.reasonDesc;
							newArr.dob = value.dob;
							newArr.datepicker = value.doa;
							newArr.datepickerPdd = value.pdd;
							newArr.genderCode = value.genderCode;
							newArr.uhidnumber = value.uhidnumber;
							newArr.patientId = value.patientId;
							newArr.paymentEntitlementDesc = value.paymentEntitlementDesc;
							newArr.bedNumber = value.bedNumber;
							newArr.admissionId = value.admissionId;
							newArr.prefixCode = value.prefixCode;
							newArr.identificationNo = value.identificationNo;
							newArr.dischargeId = value.dischargeId;
							newArr.futureDischargeDate = value.futureDischargeDate;
							newArr.dischargeStatusId = value.dischargeStatusId;
							newArr.dischargeTypeId = value.dischargeTypeId;
							newArr.floorName = value.floorName;
							newArr.dischargeTypeName = value.dischargeTypeName;
							newArr.dischargeDesc= value.dischargeDesc;
							newArr.bedId = value.bedId;
							newArr.wardId  = value.wardId;
							newArr.wardName  = value.wardName;
							newArr.doctorId  = value.doctorId;
							newArr.nursingStationId = value.nursingStationId;
							newArr.floorId = value.floorId;
							newArr.roomId = value.roomId;
							newArr.bedCategoryId = value.bedCategoryId;
							newArr.isIcu = value.isIcu;
							newArr.billStatusCount = value.billStatusCount;
							newArr.orderDetailsCount = value.orderDetailsCount;
							newArr.isFinancialClearance = value.isFinancialClearance;
							newArr.referalDocId = value.referalDocId;
							newArr.imagePath = value.imagePath;
							newArr.age = value.ageStr;
							newArr.prefixDesc = value.prefixDesc;
							newArr.visitTypeId=value.visitTypeId;
							newArr.visitType=value.visitTypeName;
							newArr.admissionNumber = value.admissionNumber;
							if($scope.visitTypeIdflag =='2'){
								if(value.visitTypeId == 2 ){
									$scope.listOfAdmittedPatientList.push(newArr);
								}
							}
							
							if($scope.visitTypeIdflag =='3'){
								if(value.visitTypeId == 3 ){
									$scope.listOfAdmittedPatientList.push(newArr);
								}
							}

						});

					}
				});
				$rootScope.stopLoader();
				console.log("list data",$scope.listOfAdmittedPatientList);
			}
			}
			$scope.pager = {};
			$scope.page;

			$scope.setPage = function(page, flag) {
				$scope.pager.totalPages=undefined;
				if (page < 1 || page > $scope.pager.totalPages)
				{
					return;
				}
				$scope.pager = PagerService.GetPager($scope.admittedPatientCount, page, $scope.noOfRecordsPerPage,$scope.unitId);
				if (flag)
				{
					$scope.masterList($scope.organizationId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
				}
			}

			$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
			
			/**List And Pagination End**/

						$scope.vocateBedAgainstPatient = function(item)
						{
							$rootScope.startLoader();
							console.log(item);
							$scope.currentDate = new Date();
							$scope.currentDate = moment($scope.currentDate)
									.format('DD-MM-YYYY HH:mm:ss');
							
							var data={
									organizationId :$scope.organizationId,
									unitId : $scope.unitId,
									dischargeId : item.dischargeId,
									patientId : item.patientId,
									doctorId : item.doctorId,
									bedNumber : item.bedNumber,
									admissionId :item.admissionId,
									bedId : item.bedId,
									updatedDate: $scope.currentDate,
									updatedBy : $scope.unitId,
									wardId  : item.wardId,
									wardName  : item.wardName,
									nursingStationId : item.nursingStationId,
									floorId : item.floorId,
									roomId : item.roomId,
								    bedCategoryId :item.bedCategoryId,
								    isIcu : item.isIcu,
								    referalDocId : item.referalDocId,
								    dischargeTypeId : item.dischargeTypeId,
								    visitTypeId:item.visitTypeId,
								    visitType:item.visitType,
								    admissionNumber : item.admissionNumber
							}
							
							console.log(data);
							//return false; 
							var URI = BASE_URL + ROOT_URL
									+ PATIENTFINALDISCHARGE;
							GenericService
									.serviceAction("POST", URI, data)
									.then(
											function(response) {

												if (response.data.status == "success") {
													$rootScope.stopLoader();
													growl
															.success(
																	'Patient Discharged Successfully...!!',
																	{
																		title : 'Success!'
																	});
													$scope.init();
													$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
												} else {
													$rootScope.stopLoader();
													growl
															.error(
																	'Something went wrong...!!',
																	{
																		title : 'Error!'
																	});
												}
											});
							
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

						$scope.departmentNursing = {};
						$scope.selectsetNursingStation = function()
						{
							if($scope.departmentNursing == undefined || $scope.departmentNursing.length == 0){
								growl.warning('Please select nursing station..!!');
								return false;
							}else{
								$scope.wardListForGetBedList=[];
								$scope.data = JSON.parse($scope.departmentNursing);
								//console.log("$scope.departmentNursing",$scope.departmentNursing);
								//console.log("$scope.departmentNursing ID",data.nursingStationId);
								var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+ $scope.data.nursingStationId+"/"+$scope.organizationId+"/"+$scope.unitId;
								GenericService.serviceAction("GET",URI,{}).then(function(response){
									if (response.data.status == "success") {
										$scope.departmentNursingStationListC = {};
										//console.log('nursingStationsWardsList',response.data.object);
										$scope.departmentNursingStationListC.departmentNursingStationlist = response.data.listObject;
										sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify($scope.departmentNursingStationListC));
										sessionStorage.setItem('specialtyNursingStationCookiesNur', JSON.stringify($scope.departmentNursingStationListC));
										sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(response.data.object));
										sessionStorage.setItem('nursingStationDesc', JSON.stringify($scope.data.nursingStationDesc));
										sessionStorage.setItem('nursingStationId', JSON.stringify($scope.data.nursingStationId));
										$scope.wardListForGetBedList = response.data.object;
										angular.element('#nursingStationModal').modal('hide');
										//	$state.reload("patientAcceptance");
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
										// $scope.getAdmittedPatientList();
										// $scope.getNewAdmissionList();
										// $scope.getTransferListTap();
										// $scope.getICUTransferList();
										if($scope.nurseRole == 1 && $scope.nursingWardList.length > 0){
											var data = {};
											var commonObj = {
												unitId : $scope.unitId,
												organizationId : $scope.organizationId,
												//	visitTypeId: 2,
												wardList : $scope.nursingWardList
											};
											var URI = BASE_URL + ROOT_URL + GETFINALDISCHARGEPATIENTLIST;
											GenericService.serviceAction("POST", URI, commonObj).then(function(response) {
												$scope.listOfAdmittedPatientList = [];
												$scope.admittedPatientList = [];

												if (response.data.status == "success") {
													$scope.admittedPatientList = response.data.listObject;
													angular.forEach($scope.admittedPatientList,function(value,index) {

														var newArr = {};
														newArr.specialityName = value.specialityName;
														newArr.dFirstName = value.dFirstName;
														newArr.pFirstName = value.pFirstName;
														newArr.reasonDesc = value.reasonDesc;
														newArr.dob = value.dob;
														newArr.datepicker = value.doa;
														newArr.datepickerPdd = value.pdd;
														newArr.genderCode = value.genderCode;
														newArr.uhidnumber = value.uhidnumber;
														newArr.patientId = value.patientId;
														newArr.paymentEntitlementDesc = value.paymentEntitlementDesc;
														newArr.bedNumber = value.bedNumber;
														newArr.admissionId = value.admissionId;
														newArr.prefixCode = value.prefixCode;
														newArr.identificationNo = value.identificationNo;
														newArr.dischargeId = value.dischargeId;
														newArr.futureDischargeDate = value.futureDischargeDate;
														newArr.dischargeStatusId = value.dischargeStatusId;
														newArr.dischargeTypeId = value.dischargeTypeId;
														newArr.floorName = value.floorName;
														newArr.dischargeTypeName = value.dischargeTypeName;
														newArr.dischargeDesc= value.dischargeDesc;
														newArr.bedId = value.bedId;
														newArr.wardId  = value.wardId;
														newArr.wardName  = value.wardName;
														newArr.doctorId  = value.doctorId;
														newArr.nursingStationId = value.nursingStationId;
														newArr.floorId = value.floorId;
														newArr.roomId = value.roomId;
														newArr.bedCategoryId = value.bedCategoryId;
														newArr.isIcu = value.isIcu;
														newArr.billStatusCount = value.billStatusCount;
														newArr.orderDetailsCount = value.orderDetailsCount;
														newArr.isFinancialClearance = value.isFinancialClearance;
														newArr.referalDocId = value.referalDocId;
														newArr.imagePath = value.imagePath;
														newArr.age = value.ageStr;
														newArr.prefixDesc = value.prefixDesc;
														newArr.visitTypeId=value.visitTypeId;
														newArr.admissionNumber = value.admissionNumber;
														if($scope.visitTypeIdflag =='2'){
															if(value.visitTypeId == 2 ){
																$scope.listOfAdmittedPatientList.push(newArr);
															}
														}
														
														if($scope.visitTypeIdflag =='3'){
															if(value.visitTypeId == 3 ){
																$scope.listOfAdmittedPatientList.push(newArr);
															}
														}

													});

												}
											});
											console.log("list data",$scope.listOfAdmittedPatientList);
										}
									}
								});
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
										//console.log($scope.patientDocumentListTest.length);
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

						
					} ]);
