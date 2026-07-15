/**
 * 
 */

angular.module('myApp')
		.controller("resourceConfigurationMasterController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','BillingGenericService',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService){
	
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.popUpFlag = true;
	
			/**Variables for pagination */
			
			
			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			$rootScope.loginpage = true;
			$scope.orgUnitId = cookieObject.unitId;
			$scope.orgId  = cookieObject.organizationId;
			$scope.createdBy = cookieObject.userId;
			$scope.updatedBy = cookieObject.userId;
			
			
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
			$scope.selectedVisitType=[];
			/*$scope.orgId = 1;
			$scope.orgUnitId = 1;
			$scope.createdBy =1 ;
			$scope.updatedBy =1 ;*/


			$scope.initMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
					specialityId:"",
					surgeryGradeId:"",
					doctorGradeId:"",
					paymentEntitlementId:"",
					bedCategoryId:"",
					surgeonDependentOn:"",
					asstSurgeonDependentOn:"",
					anesthesiologistDependentOn:"",
					otDependentOn:"",
					surgeonPercentage:"",
					asstSurgeonPercentage:"",
					anesthesiologistPercentage:"",
					otPercentage:"",
					surgeryResourceConfigId:"",
				};
				console.log("Common-init",$scope.common);
			}
			$scope.initMaster();
	/*		$scope.dependantOnList = [

				{
					id:1,
					name:"Procedure"
				},
				{
					id:2,
					name:"Surgeon"
				},
				{
					id:3,
					name:"Asst. Surgeon"
				}
				,
				{
					id:4,
					name:"Anaesthetist"
				}
				,
				{
					id:5,
					name:"OT"
				}
			]*/
			console.log("dependantOnList",$scope.dependantOnList);
			/**Crud Operations Start**/

			$scope.getActiveSpecialityAtUnitLevel=function(){
				try{
						var URI = BASE_URL + ROOT_URL + GETACTIVESPECIALITY;
						var obj = {
								organizationId :$scope.orgId,
								unitId         :$scope.orgUnitId
						    }
						BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						if(response.data.status == "success"){
							$scope.specialityMasterList = response.data.listObject;
							console.log('splList',$scope.specialityMasterList);
						}
					});
				}catch(e){
					console.log("Exception-",e);
				}
			}
			$scope.getActiveSpecialityAtUnitLevel();
			$scope.activePaymentEntitlement=function(){
				var data = {
						organizationId : $scope.orgId,
						unitId : $scope.orgUnitId
					}
				var URI = BASE_URL + ROOT_URL
				+ GETACTIVEPAYMENTENTITLEMENT;
		GenericService.serviceAction("GET", URI, data)
				.then(
						function(response) {
							$scope.paymentEntitlementList = [];

							if (response.data.status == "success") {
								$scope.paymentEntitlementList=response.data.listObject;
								console.log('paymentEntitlementList',$scope.paymentEntitlementList);
							}
						});
			}
			$scope.activePaymentEntitlement();

			$scope.getBillOfMaterialServicesDetails = function(){
				$scope.isBillOfMaterialDetailsList = [];
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL+ "/api/procedure/BillOfMaterial/getBillOfMaterialServices"
				var param = {
						unitId : $scope.orgUnitId,
						organizationId : $scope.orgId
				}
				BillingGenericService.serviceAction(METHOD_POST, URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					$rootScope.stopLoader();
					if(response.data.status == 'success'){
						$scope.isBillOfMaterialDetailsList = response.data.listObject;
						}
				});
		}

		$scope.getBillOfMaterialServicesDetails();

			$scope.activeTarrifList = function(){
				var data = {
						organizationId : $scope.orgId,
						unitId : $scope.orgUnitId
					}
				var URI = BASE_URL + ROOT_URL
				+ "/api/unit/getActiveTariffMasterList";
		GenericService.serviceAction("POST", URI, data)
				.then(
						function(response) {
							$scope.activeTariffList = [];

							if (response.data.status == "success") {
								$scope.activeTariffList=response.data.listObject;
								console.log('activeTariffList',$scope.activeTariffList);
							}
						});
			}
			$scope.activeTarrifList();

			$scope.visitTypeList = function(){
				var data = {};
				var URI = BASE_URL + ROOT_URL
				+ "/api/global/visitTypeMaster/unit/"+$scope.orgUnitId+"/org/"+$scope.orgId;
		GenericService.serviceAction("GET", URI, data)
				.then(
						function(response) {
							$scope.activeVisitTypeList = [];

							if (response.data.status == "success") {
								$scope.activeVisitTypeList=response.data.listObject;
								console.log('activeTariffList',$scope.activeTariffList);
								angular.forEach(response.data.listObject,
								function(value,index){
								    var data = {};
								    data.id = value.visitTypeId;
								    data.label = value.visitTypeName;
                                    $scope.selectedVisitType.push(data);
								});
							}
						});
			}
			$scope.visitTypeList();
	    	console.log('selectedVisitType list',$scope.selectedVisitType);


			$scope.patientCategory = function(){
				var data = {};
				var URI = BASE_URL + ROOT_URL
				+ GETACTIVEPATIENTCATEGORY;
		GenericService
				.serviceAction("GET", URI, data)
				.then(
						function(response) {
							$scope.patientCategoryList = [];

							if (response.data.status == "success") {
								$scope.patientCategoryList = response.data.listObject;

							}
						});
			}
			$scope.patientCategory();

	/*		$scope.getResourceBillOfServicesMasterList = function(){
				var data = {};
				var URI = BASE_URL + ROOT_URL
				+ "/api/otims/getResourceBillOfServicesMaster";
		GenericService
				.serviceAction("POST", URI, data)
				.then(
						function(response) {
							$scope.resourceBillOfServicesMaster = [];

							if (response.data.status == "success") {
								$scope.resourceBillOfServicesMaster = response.data.listObject;

							}
						});
			}
			$scope.getResourceBillOfServicesMasterList();*/

//			$scope.activeBedCategory=function(){
//				var data = {
//						organizationId : $scope.orgId,
//						unitId : $scope.orgUnitId
//					}
//				var URI = BASE_URL + ROOT_URL+ "/adt/getBedCategoryListByOrgUnit";
//		GenericService.serviceAction("POST", URI, data)
//				.then(
//						function(response) {
//							$scope.bedCategoryList = [];
//
//							if (response.data.status == "success") {
//								$scope.bedCategoryList=response.data.listObject;
//								console.log('bedCategoryList',$scope.bedCategoryList);
//							}
//						});
//			}
//			$scope.activeBedCategory();
			/*$scope.doctorGradeList = [

				{
					doctorGradeId:1,
					gradeDesc:"TP"
				},
				{
					doctorGradeId:2,
					gradeDesc:"ML"
				},
				{
					doctorGradeId:3,
					gradeDesc:"LO"
				}
			]
			console.log('doctorGradeList',$scope.doctorGradeList);*/

			$scope.activeDoctorList=function(){
				var data = {
						orgUnitId:$scope.orgUnitId,
							orgId:$scope.orgId
				}
				var URI = BASE_URL + ROOT_URL+ "/global/billing/getActiveDoctorGradeList";
				GenericService.serviceAction("POST", URI, data)
				.then(
						function(response) {
							$scope.doctorList = [];

							if (response.data.status == "success") {
								$scope.doctorGradeList=response.data.listObject;
								console.log('doctorGradeList',$scope.doctorGradeList);
							}
						});
			}
			$scope.activeDoctorList();

			$scope.activeSurgeryGradeList=function(){
				var data = {

					}
				var URI = BASE_URL + ROOT_URL+ "/api/global/getActiveSurgeryGradeList";
		GenericService.serviceAction("GET", URI, data)
				.then(
						function(response) {
							$scope.surgeryGradeList = [];

							if (response.data.status == "success") {
								$scope.surgeryGradeList=response.data.listObject;
								console.log('surgeryGradeList',$scope.surgeryGradeList);
							}
						});
			}
			$scope.activeSurgeryGradeList();
			$scope.selectedResourceBillOfServicesMaster = [];
			$scope.add = function(){
/*
				var searchObj = {
						organizationId: $scope.orgId,
						unitId: $scope.orgUnitId,
						billingBedCatId:parseInt($scope.common.bedCategoryId),
						procedureId:$scope.common.serviceId
					};*/
				var searchObj = {
						organizationId: $scope.organizationId,
						unitId: $scope.unitId,
						tariffId: $scope.common.tariffId,
						billingBedCatId:parseInt($scope.common.bedCategoryId),
						paymentEntitlementId: $scope.common.paymentEntitlementId,
						visitTypeId: $scope.common.visitTypeId,
						patientCategoryId: $scope.common.patientCategoryId,
					};
					var URI = BASE_URL + ROOT_URL + "/api/procedure/BillOfMaterial/getBillOfMaterialListByProcedureId";
					 BillingGenericService.serviceAction(METHOD_POST, URI, searchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function (response) {
						if (response.data.status == "success"){
							var proRate = 0;
							if(response.data.listObject.length > 0){
								var found = response.data.listObject.find(ar=>ar.serviceId == parseInt($scope.common.componenetServicesId) && ar.amount != 0);
								if(found == undefined){
									var found = $scope.selectedResourceBillOfServicesMaster.find(ar=>ar.componenetServicesId == $scope.common.componenetServicesId);
									if(found == undefined){
									var newArr = {};
									newArr.componenetServicesId = $scope.common.componenetServicesId;
									angular.forEach($scope.isBillOfMaterialDetailsList,function(value,key){
										if(value.serviceId == $scope.common.componenetServicesId){
											newArr.componenetServicesDesc = value.serviceName;
										}
									});
									newArr.dependentOn = "";
									newArr.dependentOnPercentage = "";
									$scope.selectedResourceBillOfServicesMaster.push(newArr);
									}else{
										growl.warning("Already Added In List",
												{
													title : 'Warning!'
												});
									}
							   }else{
									growl.warning("Already Added Amount In Bill Of Material",
											{
												title : 'Warning!'
											});
									return false;
								}
							}else{
								growl.warning("Please Configure Bill Of Material For The Same Criteria",
										{
											title : 'Warning!'
										});
								return false;
							}

						}
					});

			}
			/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
				$scope.initMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage,$scope.orgUnitId);
			};

			$scope.initMasterList = function(orgId,offset, noOfRecordsPerPage,unitId)
			{
				$rootScope.startLoader();

				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				var data1 = {
								orgId:orgId,
								offset:offset,
								noOfRecordsPerPage:noOfRecordsPerPage,
								unitId:unitId
							};

				var data2 = {
						      orgId:orgId,
						      unitId:unitId
							};

				var URI1 = BASE_URL + ROOT_URL +"/api/otims/getSurgeryResourceConfigList";
				var URI2 = BASE_URL + ROOT_URL + "/api/otims/getSurgeryResourceConfigListCount";


				GenericService.serviceAction("POST",URI1,data1).then(function(response){
					if(response.data.status == "success"){
					$rootScope.stopLoader();
					$scope.commonList = response.data.listObject;
					console.log("commonList",$scope.commonList);
					}
				});

				GenericService.serviceAction("POST",URI2,data2).then(function(response){
					if(response.data.status == "success"){
						$rootScope.stopLoader();
						$scope.commonListCount = response.data.object;
						$scope.setPage(1,false);
						console.log("commonListCount",response.data.object);
					}
				});

			}
//			$scope.getBillingBedCategory = function(){
//				$scope.listBedCategory = [];
//				$rootScope.startLoader();
//				var URI = BASE_URL + ROOT_URL+ "/adt/getActiveBillingBedCategoryList/orgId/"+$scope.orgId+"/unitId/"+$scope.orgUnitId;
//				var param = {
//
//				}
//				BillingGenericService.serviceAction(METHOD_GET, URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
//					$rootScope.stopLoader();
//					if(response.data.status == 'success'){
//						if(response.data.listObject.length != 0 )
//							$scope.listBedCategory = response.data.listObject;
//						console.log("listBedCategory",$scope.listBedCategory);
//					}
//				});
//			}

	$scope.getBillingBedCategory = function(visitTypeId,flag,info)
		{
        console.log('visitType.visitTypeId ',visitTypeId);
			$scope.selectedBillingCategory = [];
				var URI = BASE_URL+ ROOT_URL+ "/adt/getBillingBedCategoryByVistTypeId/"+visitTypeId+"/"+$scope.organizationId+"/"+$scope.unitId;
			GenericService.serviceAction("GET", URI, "")
					.then(function(response) {
								if (response.data.status == "success") {
								$scope.selectedBillingCategory  = response.data.listObject;
//									angular.forEach(response.data.listObject,
//													function(value,index) {
//														var data = {};
//														data.id = value.billingBedCategoryId;
//														data.label = value.billingBedDesc;
//														//$scope.billingCategoryAdd.push(data);
//														var found = $scope.selectedBillingCategory.find(ar=>ar.id == value.billingBedCategoryId );
//														if(found == undefined){
//															$scope.selectedBillingCategory.push(data);
//															//$scope.billingCategoryAdd.push(data);
//														}
//														var found1 = $scope.billingCategory.find(ar=>ar.id == value.billingBedCategoryId );
//														if(found1 == undefined){
//															$scope.billingCategory.push(data);
//														}
//													});
									//$scope.activeBillingBedCategory();
									console.log('selectedBillingCategory list',$scope.selectedBillingCategory);
									if(flag != undefined && flag == true){
									    $scope.showUpdateBtnData(info);
									}

								}
							});
            console.log('$scope.selectedVisitType.length ',$scope.selectedVisitType.length)
			if($scope.selectedVisitType.length == 0){
				$scope.selectedBillingCategory = [];
			}
		}

			$scope.masterList = function(orgId,offset, noOfRecordsPerPage,unitId) {
				$rootScope.startLoader();
				console.log("offset", offset);
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;

				var data = {
						orgId:orgId,
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage,
						unitId:unitId
						};

				var URI = BASE_URL + ROOT_URL + "/api/otims/getSurgeryResourceConfigList";
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI, data).then(function(response) {
					$rootScope.stopLoader();
					$scope.commonList = response.data.listObject;
					console.log("commonList",$scope.commonList);
				});
			}

			$scope.pager = {};
			$scope.page;

			$scope.setPage = function(page, flag) {
				if (page < 1 || page > $scope.pager.totalPages)
				{
					return;
				}
				$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage,$scope.orgUnitId);
				if (flag)
				{
					$scope.masterList($scope.orgId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.orgUnitId);
				}
			}

			$scope.initMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage,$scope.orgUnitId);
			
			/**List And Pagination End**/
			/*this function is defined for showing the details of dependant-on
			  STRAT*/
			$scope.dependantData={};
			$scope.getDependant=function(data){
				angular.forEach(data.listSelectedResourceBillOfServicesMapperDto,function(value,key){
					var found = $scope.isBillOfMaterialDetailsList.find(ar=>ar.serviceId == value.dependentOn);
					if(found == undefined){
					}else{
						value.dependentOn = value.dependentOn.toString();
						value.name = found.serviceName;
					}
					
				});
				$scope.listSelectedResourceBillOfServicesMapperDto = [];
				$scope.listSelectedResourceBillOfServicesMapperDto = data.listSelectedResourceBillOfServicesMapperDto;
			}
			$scope.getDependantNameById=function(id){
				if(id==1){
					return "Procedure";
				}else if(id==2){
					return "Surgeon";
				}else if(id==3){
					return "Asst. Surgeon";
				}else if(id==4){
					return "Anaesthetist";
				}else{
					return "OT";
				}
			}
			//END 
			
			// function for popup ok button call
			$scope.PopupOkBtn = function() {
				$scope.popUpFlag = false;
				$scope.save();
			}
			
			$scope.save = function(){
				try{
					
					$scope.$broadcast('show-errors-check-validity'); //dilip					
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					if($scope.selectedResourceBillOfServicesMaster.length == 0){
						growl.warning("Please Add Component List",
								{
									title : 'Warning!'
								});
						return false;
					}
					
					$scope.common.createdBy=$scope.createdBy,
					$scope.common.updatedBy=$scope.updatedBy,
					$scope.common.createdDate=$scope.dt,
					$scope.common.status='A',
					$scope.common.unitId=$scope.orgUnitId,
					$scope.common.orgId=$scope.orgId,
					$scope.common.listSelectedResourceBillOfServicesMapperDto = [],
					$scope.common.listSelectedResourceBillOfServicesMapperDto = $scope.selectedResourceBillOfServicesMaster,
					$scope.common.updatedDate=$scope.dt
					if ($scope.resourceConfigform.$valid) {		//dilip
					if ($scope.popUpFlag) {
						$scope.showModal = !$scope.showModal;

					} else {
						$rootScope.startLoader(); //dilip
					var URI = BASE_URL + ROOT_URL + "/api/otims/saveSurgeryResourceConfig";
					console.log("Save-URI-",URI);
					console.log(" save OBJ",$scope.common); 
					
					GenericService.serviceAction("POST",URI,$scope.common).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							/*$scope.$broadcast('show-errors-reset');*/
							$scope.selectedResourceBillOfServicesMaster = [];
							$scope.initMaster();
							$scope.initMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage,$scope.orgUnitId);
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
					}
					}
				}catch(e){
					console.log("Exception-",e);
				}
			}

		$scope.showUpdateBtn = function(info){
		    $scope.getBillingBedCategory(info.visitTypeId,true,info);
		}
			$scope.showUpdateBtnData = function(info){
				try{
					$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateScope = true;
					$scope.updateBtnFlag = true;
					$rootScope.startLoader();

					var URI = BASE_URL + ROOT_URL +"/api/otims/getSurgeryResourceConfigListById/"+info.surgeryResourceConfigId;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET",URI).then(function(response){
						console.log("Edit",response);
						
						if(response.data.status == "success"){
							$rootScope.stopLoader();
							$scope.common = response.data.listObject[0];
							$scope.common.paymentEntitlementId=response.data.listObject[0].paymentEntitlementId.toString();
//							$scope.common.bedCategoryId=response.data.listObject[0].bedCategoryId.toString();
							$scope.common.tariffId=response.data.listObject[0].tariffId.toString();
							$scope.common.visitTypeId=response.data.listObject[0].visitTypeId.toString();
							$scope.common.patientCategoryId=response.data.listObject[0].patientCategoryId.toString();
							$scope.common.bedCategoryId=response.data.listObject[0].bedCategoryId.toString();

							angular.forEach(info.listSelectedResourceBillOfServicesMapperDto,function(value,key){
								var found = $scope.isBillOfMaterialDetailsList.find(ar=>ar.serviceId == value.dependentOn);
								if(found == undefined){
								}else{
									value.dependentOn = value.dependentOn.toString();
									value.name = found.name;
								}
							});
							$scope.selectedResourceBillOfServicesMaster = [];
							$scope.selectedResourceBillOfServicesMaster = info.listSelectedResourceBillOfServicesMapperDto;
						
							
						}else if(response.data.status == 'error'){
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
					
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			
			$scope.update = function(){
				try{
					/*$scope.$broadcast('show-errors-check-validity');*/
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');			
					$scope.common.updatedBy=$scope.updatedBy;
					$scope.common.updatedDate=$scope.dt;
					$scope.common.unitId=$scope.orgUnitId;
					$scope.common.orgId=$scope.orgId;
					$scope.common.listSelectedResourceBillOfServicesMapperDto = [];
					$scope.common.listSelectedResourceBillOfServicesMapperDto = $scope.selectedResourceBillOfServicesMaster;
					
					/*if ($scope.resourceConfigform.$valid) {				*/
					var URI = BASE_URL + ROOT_URL + "/api/otims/updateSurgeryResourceConfig";
					console.log("Update-URI-",URI);
					console.log("Update OBJ",$scope.common); 
					$rootScope.startLoader(); 
					GenericService.serviceAction("POST",URI,$scope.common).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
						/*	$scope.$broadcast('show-errors-reset');*/
							$scope.selectedResourceBillOfServicesMaster = [];
							$scope.initMaster();
							$scope.initMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage,$scope.orgUnitId);
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
					/*}*/
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			$scope.updateStatus = function(surgeryResourceConfigId,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					$scope.common.updatedBy=$scope.updatedBy,
					$scope.common.updatedDate=$scope.dt,
					$scope.common.status=status,
					$scope.common.surgeryResourceConfigId=surgeryResourceConfigId,
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/api/otims/updateSurgeryResourceConfigStatus";
					console.log("update status",$scope.common);
					GenericService.serviceAction("POST",URI,$scope.common).then(function(response){
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
//							$scope.$broadcast('show-errors-reset');
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
				}catch(e){
					console.log("Exception",e);
				}
			}
			
			
			$scope.procedureSearch = '';
			$scope.procedureDate;
			$scope.searchProcedures = function(searchKeyword) {

				$scope.procedureMasterList = [];
				var selectedSubServiceType = $scope.procedureSubSpeciality;

				 $scope.regex = /^[A-Za-z0-9 ]+$/;
				 
					var isValid = $scope.regex.test(searchKeyword);
					if (!isValid) {
						
						$scope.procedureSearch = '';
						return false;
						
					} else {
				
				
						/*var URI = BASE_URL + ROOT_URL
								+ "/global/procedureSearch/"
								+ searchKeyword + "/unit/"
								+ $scope.unitId + "/org/"
								+ $scope.organizationId
								+ "/speciality/"
								+ selectedSubServiceType.specialityId*/
						var URI = BASE_URL + ROOT_URL + "/api/procedure/searchProcedureByNameOrCode";
						var data = {
								unitId : $scope.unitId,
								organizationId : $scope.organizationId,
								serviceName : searchKeyword
						};
					
						console.log('URI', URI);
						return BillingGenericService
								.serviceAction(METHOD_POST, URI, data)
								.then(
										function(response) {
											$scope.procedureMasterList = [] ;
										if (response.data.status == "success") {
												angular.forEach(response.data.listObject,function(value, key) {
													if(value.procedureTypeId != 3 ){
														value.cptCode = value.serviceStandardCode;
														$scope.procedureMasterList.push(value);
													}
													if(value.procedureTypeId == 3 && value.procedureConfigurationId != null){
														$scope.procedureMasterList.push(value);
													}
												});
											}
											return $scope.procedureMasterList;
										});
					}
			}
			$scope.selectProcedureName = function($item,$model, $label) {
				$scope.common.serviceId = $item.serviceMasterId;
				$scope.common.procedureName = $item.serviceStandardName;
				$scope.common.specialityName =  $item.specialityName;
                $scope.common.specialityId =  $item.specialityId;
				
			}
			$scope.selectProcService = function (item, model, label) {
				self.model.procDtl = item;
			}
			
			$('#filterme1').on('input', function() {
				  this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');
				});
			
			$('#filterme2').on('input', function() {
				  this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');
				});
			
			$('#filterme3').on('input', function() {
				  this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');
				});
			
			$('#filterme4').on('input', function() {
				  this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');
				});
			/**Crud Operations End**/
			
			$scope.validationDependOn = function (index,info) {
				if($scope.selectedResourceBillOfServicesMaster.length > 1){
					var ind = index - 1;
					angular.forEach($scope.selectedResourceBillOfServicesMaster,function(v,k){
						if(ind == k){
							$scope.conpId = v.componenetServicesId;
							$scope.dependOnId =v.dependentOn;
							$scope.componenetServicesDesce =v.componenetServicesDesc;
						}
					});
					if(info.dependentOn != $scope.conpId){
						growl.warning("Please select "+$scope.componenetServicesDesce,
								{
									title : 'warning!'
								});
						$scope.selectedResourceBillOfServicesMaster[index].dependentOn = '';
					}
				}
				
			}
}]);