angular
		.module('myApp')
		.controller(
				'otManagerDashboardController',
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
						'$timeout',
						'PagerService',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,$timeout,PagerService) {
							var self = this;
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$rootScope.loginpage = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.toStoreId = "";
							$scope.searchKeyword = "";
							
							
							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.medicationsAgainstProcedureList  = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;
							
							function init(){
								$rootScope.loginpage = true;
								self.model = {searchObj : {
										fromDate: new Date(),
								        toDate: new Date(),
								        fromDateOptions: {
								          formatYear: 'yyyy',
								          showWeeks: false,
								          maxDate: new Date()
								        },
								        toDateOptions: {
								          formatYear: 'yyyy',
								          showWeeks: false,
								          maxDate: new Date()
								        }
					       }
						};
						self.model.OpenFromDate = OpenFromDate;
					    self.model.OpenToDate = OpenToDate;
						}
							
							var OpenFromDate = function () {
								self.model.fromDateOpened = true;
								self.model.searchObj.fromDateOptions.minDate = self.model.fromDate;
							}

							var OpenToDate = function () {
								self.model.toDateOpened = true;
								self.model.searchObj.toDateOptions.minDate = self.model.toDate;
							}

							init();
							
							//for specialityList
							$scope.specialityList=[];
							var getSpecialityList = function(){
								var URI = BASE_URL + ROOT_URL + GET_ALL_ACTIVE_SPECIALITY_LIST;
								var obj = {organizationId:$scope.organizationId, unitId:$scope.unitId};
								GenericService.serviceAction(METHOD_POST,URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success")
										$scope.specialityList = response.data.listObject;
								});
							}	
						
							getSpecialityList();
					
							//Get Active Operation Theatre List
							$scope.activeOperationTheatreList=[];	
							var getActiveOperationTheatreList = function() {
								var data = {
										orgId:$scope.organizationId, 
										unitId:$scope.unitId
								};
								
								var URI = BASE_URL + ROOT_URL + GET_ALL_ACTIVE_OPERATION_THEATRE_LIST;
								console.log("URI", URI);
								GenericService.serviceAction(METHOD_POST, URI, data).then(function(response) {
									$scope.activeOperationTheatreList = response.data.listObject;
								});
							}
							getActiveOperationTheatreList();
				
							//get DoctorList By speciality
							//http://localhost:8080/HIS-WAR/adt/getDoctorListAganistSpeciality
							$scope.specialityObj= {
									//specialtyId : ''
									
							};
							$scope.getDoctorBySpeciality = function(specialtyId) {
								var data = {
										specialityId :$scope.specialityObj.specialtyId,
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
								};
								var URI = BASE_URL + ROOT_URL + GETDOCTORBYSPECIALITYID;
								GenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									console.log(response);
									$scope.doctorList = [];
									if (response.data.status == "success"){
										$scope.doctorList = response.data.listObject;
									}
								});

							}
							
							$scope.storeList = [];
							
							$scope.getStoreList = function(){
								 $scope.storeList = [];
								 var URI =  BASE_URL+ROOT_URL+"/api/global/getStoreList/"+$scope.organizationId+"/"+$scope.unitId;
						            $rootScope.startLoader();
						            GenericService.serviceAction("GET", URI, {}).then(
						                function (response) {
						                    $scope.storeList = response.data.listObject;
						                    $rootScope.stopLoader();
						                });
							}
							$scope.getStoreList();
						
							$scope.medicationsAgainstProcedureList=[];
							$scope.searchDetails = function(offset,noOfRecordsPerPage){
								$rootScope.startLoader();
								$scope.commonListCount =0;
								$scope.medicationsAgainstProcedureList = [];
								
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								
								var data = {
										organizationId:$scope.organizationId, 
										unitId:$scope.unitId,
										fromDate:moment(self.model.searchObj.fromDate).format('YYYY-MM-DD'),
										toDate:moment(self.model.searchObj.toDate).format('YYYY-MM-DD'),
										specialityId:$scope.specialityObj.specialtyId,
										doctorId:$scope.specialityObj.doctorId,
										procedureName:$scope.specialityObj.procedureName,
										priority:$scope.specialityObj.priority,
										offset : offset,
										noOfRecordsPerPage:noOfRecordsPerPage
								};

								var URI = BASE_URL + ROOT_URL + SEARCH_PROCEDURE_AND_PATIENT_DETAILS_LIST;
								GenericService.serviceAction(METHOD_POST, URI, data).then(function(response) {
									if(response.data.status=="success"){
									console.log(response);
									$scope.medicationsAgainstProcedureList = response.data.listObject;
									$scope.commonListCount = response.data.object;
									$scope.setPage(1,false);
									$rootScope.stopLoader();
									}
									else if(response.data.status=="error"){
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : ERROR_MSG
												});
										}
						
								});
							}
							
							$scope.searchDetails($scope.offset,$scope.noOfRecordsPerPage);
							

							$scope.setNoOfRecords = function() {
								$scope.searchDetails($scope.offset,$scope.noOfRecordsPerPage);
							};
							
							$scope.getDashboardWorklist = function(offset,noOfRecords){
								try {
									$rootScope.startLoader();
									$scope.medicationsAgainstProcedureList = [];
									var data = {
											organizationId:$scope.organizationId, 
											unitId:$scope.unitId,
											fromDate:moment(self.model.searchObj.fromDate).format('YYYY-MM-DD'),
											toDate:moment(self.model.searchObj.toDate).format('YYYY-MM-DD'),
											specialityId:$scope.specialityObj.specialtyId,
											doctorId:$scope.specialityObj.doctorId,
											procedureName:$scope.specialityObj.procedureName,
											priority:$scope.specialityObj.priority,
											offset : offset,
											noOfRecordsPerPage:noOfRecords
									};

									var URI = BASE_URL + ROOT_URL + SEARCH_PROCEDURE_AND_PATIENT_DETAILS_LIST;
									GenericService.serviceAction(METHOD_POST, URI, data).then(function(response) {
										if(response.data.status=="success"){
										$scope.medicationsAgainstProcedureList = response.data.listObject;
										$rootScope.stopLoader();
										}
										else if(response.data.status=="error"){
											$rootScope.stopLoader();
											growl.error(response.data.message,
													{
														title : ERROR_MSG
													});
											}
							
									});
								} catch (e) {
									console.log(e.message)
								}
							}
							
							$scope.pager = {};
							$scope.page;
							$scope.setPage = function(page, flag)
							{
								$scope.pager.totalPages = undefined;
								if (page < 1 || page > $scope.pager.totalPages)
								{
									return;
								}
								
								$scope.pager = PagerService.GetPager($scope.commonListCount, page,$scope.noOfRecordsPerPage);
								
								if (flag) {
									$scope.getDashboardWorklist($scope.pager.startIndex,$scope.pager.pageSize);
								}
							}
							
							$scope.patientDetails = 
							{
								uhIdNumber:"",
								patientName:"",
								genderCode:"",
								age:"",
								birthDate:"",
								encounterId:""
							};
							
							//get medication Details implantDetails
							
							$scope.medicationkitDetails = function(info){
								$rootScope.startLoader();
								$scope.medicationDetails=[];
								if($scope.storeId!=null)
								{
									var storeCheck  = $scope.storeList.find(ar=>ar.storeId == $scope.storeId);
									if(storeCheck == undefined){
										$rootScope.stopLoader();
										growl.warning("Requesting Store not mapped with User",
												{
													title : 'Warning!'
												});
									}
									$scope.patientDetails.patientName = info.patientName;
									$scope.patientDetails.genderCode = info.genderCode;
									$scope.patientDetails.age = info.age;
									$scope.patientDetails.uhIdNumber = info.uhidNumber;
									$scope.patientDetails.birthDate =info.birthDate;
									$scope.patientDetails.dateOfAdmission = info.doa;
									$scope.patientDetails.patientDischargeDate = info.pdd;
									$scope.patientDetails.bedNumber = info.bedNumber;
									$scope.patientDetails.doctorFullName = info.doctorName;
									$scope.patientDetails.wardName = info.wardName;
									$scope.patientDetails.visitTypeId =  info.visitTypeId;
									if($scope.patientDetails.visitTypeId!=1){
										var dateDoa = moment($scope.patientDetails.dateOfAdmission, 'DD/MM/YYYY');
										var datePdd = moment($scope.patientDetails.patientDischargeDate, 'DD/MM/YYYY');
										$scope.patientDetails.los = $rootScope.dateDiffrence(dateDoa,datePdd);
									}
								var data={
										procedureOrderId:info.procedureOrderId,
										organizationId:$scope.organizationId, 
										unitId:$scope.unitId
										
								};
								var URI = BASE_URL + ROOT_URL + "/api/otims/getMedicationDetailsByProcedureId";
								GenericService.serviceAction(METHOD_POST, URI, data).then(function(response) {
									if (response.data.status == "success"){
										if(response.data.listObject.length>0){
									angular.forEach(response.data.listObject,function(value,index) {
									
									var newArr = {};
								
									newArr.medicationDetailsId=value.medicationDetailsId;
									newArr.isConsignment=value.isConsignment;
									newArr.isConsumable=value.isConsumable;
									newArr.isImplant=value.isImplant;
									newArr.isKit=value.isKit;
									newArr.doctorId=  info.doctorId;
									newArr.patientId =info.patientId;
									newArr.toStoreId =$scope.storeId;
									newArr.remark ='';
									newArr.statusId =1;
									newArr.encounterId = info.encounterId;
									newArr.admissionId = info.admissionId;
									newArr.bed = info.bed;
									newArr.ward = info.ward;
									newArr.approvalStatusId = 1;
									newArr.kitId=  value.kitId;
									newArr.itemId =value.itemId;
									newArr.qty = value.qty;
									newArr.itemName = value.itemName;
									newArr.kitItemName = value.kitItemName;
									newArr.uomTypeId = value.uomTypeId;
									newArr.uomUnitId = value.uomUnitId;
									newArr.isActive = value.isActive;
									newArr.visitTypeId = info.visitTypeId;
									newArr.kitItemId = value.kitItemId,
									newArr.itemQuantity = value.itemQuantity,
									newArr.procedureOrderId = value.procedureOrderId;
									$scope.medicationDetails.push(newArr);
									
									});
									$rootScope.stopLoader();
									}else{
										$rootScope.stopLoader();
											growl.warning("No Result Found",
													{
														title : 'Warning!'
													});
										}
									}else if (response.data.status == "error"){
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'error!'
												});
									}
								});
								}
								else
								{
									$rootScope.stopLoader();
									growl.warning("Please Select Theatre",
											{
												title : 'Warning!'
											});
									}
							}
						
							//get implant Details
							
							$scope.implantDetails = function(info){
								$rootScope.startLoader();
								if($scope.storeId!=null)
								{
									var storeCheck  = $scope.storeList.find(ar=>ar.storeId == $scope.storeId);
									if(storeCheck == undefined){
										$rootScope.stopLoader();
										growl.warning("Requesting Store not mapped with User",
												{
													title : 'Warning!'
												});
									}
									$scope.patientDetails.patientName = info.patientName;
									$scope.patientDetails.genderCode = info.genderCode;
									$scope.patientDetails.age = info.age;
									$scope.patientDetails.uhIdNumber = info.uhidNumber;
									$scope.patientDetails.birthDate =info.birthDate;
									$scope.patientDetails.dateOfAdmission = info.doa;
									$scope.patientDetails.patientDischargeDate = info.pdd;
									$scope.patientDetails.bedNumber = info.bedNumber;
									$scope.patientDetails.doctorFullName = info.doctorName;
									$scope.patientDetails.wardName = info.wardName;
									$scope.patientDetails.visitTypeId =  info.visitTypeId;
									if($scope.patientDetails.visitTypeId!=1){
										var dateDoa = moment($scope.patientDetails.dateOfAdmission, 'DD/MM/YYYY');
										var datePdd = moment($scope.patientDetails.patientDischargeDate, 'DD/MM/YYYY');
										$scope.patientDetails.los = $rootScope.dateDiffrence(dateDoa,datePdd);
									}
								$scope.implantDetailsList=[];
								var data={
										procedureOrderId:info.procedureOrderId,
										organizationId:$scope.organizationId, 
										unitId:$scope.unitId
										
								};
								var URI = BASE_URL + ROOT_URL + "/api/otims/getImplantDetailsByProcedureId";
								GenericService.serviceAction(METHOD_POST, URI, data).then(function(response) {
									if (response.data.status == "success"){
										if(response.data.listObject.length>0){
									angular.forEach( response.data.listObject,function(value,index) {
									if(value.procedureOrderId!=null){
									var newArr = {};
								
									newArr.implantDetailsId=value.implantDetailsId;
									newArr.isConsignment=value.isConsignment;
									newArr.isConsumable=value.isConsumable;
									newArr.isImplant=value.isImplant;
									newArr.isKit=value.isKit;
									newArr.doctorId=  info.doctorId;
									newArr.patientId =info.patientId;
									newArr.fromStoreId =$scope.storeId;
									newArr.storeId =$scope.toStoreId;
									newArr.remark ='';
									newArr.statusId =1;
									newArr.encounterId = info.encounterId;
									newArr.admissionId = info.admissionId;
									newArr.bed = info.bed;
									newArr.ward = info.ward;
									newArr.approvalStatusId = 1;
									newArr.kitId=  value.kitId;
									newArr.itemId =value.itemId;
									newArr.qty = value.qty;
									newArr.itemName = value.itemName;
									newArr.kitItemName = value.kitItemName;
									newArr.visitTypeId = info.visitTypeId;
									newArr.kitItemId = value.kitItemId,
									newArr.itemQuantity = value.itemQuantity,
									newArr.procedureOrderId = value.procedureOrderId;
									$scope.implantDetailsList.push(newArr);
									}
									
									});
									$rootScope.stopLoader();
										}else{
											$rootScope.stopLoader();
												growl.warning("No Result Found",
														{
															title : 'Warning!'
														});
											}
										}else if (response.data.status == "error"){
											$rootScope.stopLoader();
											growl.error(response.data.message,
													{
														title : 'error!'
													});
										}
									});
									}
									else
									{
										$rootScope.stopLoader();
										growl.warning("Please Select Theatre",
												{
													title : 'Warning!'
												});
										return false;
										}
							}
							
							//get All Active KitType List
							$scope.kitTypeList=[];
							$scope.getAllKitType = function()
							{
								var URI = BASE_URL + ROOT_URL + GET_ALL_ACTIVE_KIT_TYPE_LIST;
								GenericService.serviceAction(METHOD_GET, URI).then(function(response) {
									
									if (!!response.data && response.data.length > 0){
										$scope.kitTypeList = response.data;
										$scope.finalKitTypeList = [];
										angular
										.forEach($scope.kitTypeList,function(value,index) {
											if(value.status)
											{
												var data = {
														id : value.id,
														description : value.description,
														code : value.code,
														status : value.status,
														kitType : value.kitType
												};
												$scope.finalKitTypeList.push(data);
											}
										});
									}
								});
							}
							$scope.getAllKitType();			
							
						
						//get Store ID by Ot Name
						$scope.storeId ;
						$scope.storeName = "";
						$scope.wardList = [];
						$scope.visitType ;
						$scope.doctorId;
						$scope.getStoreId = function(operationTheatreId,visitTypeId,doctorId){
							$rootScope.startLoader();
							$scope.visitType = visitTypeId;
							$scope.doctorId = doctorId;
							
							var URI = BASE_URL + ROOT_URL + "/api/otims/getOperationTheatreById/"+operationTheatreId+"/"+$scope.organizationId+"/"+$scope.unitId;
							GenericService.serviceAction(METHOD_GET, URI).then(function(response) {
								if(response.data.status == 'success'){
								$scope.wardList = response.data.listObject;
								angular.forEach($scope.wardList,function(value,index) {
									$scope.storeId = value.storeId;
									$scope.storeName = value.storeName;
								});
								$rootScope.stopLoader();
							}
							});
						}
						
						//save patient Indent 
						
						$scope.updateMedicationIsExported = function(list){

							var URI = BASE_URL + ROOT_URL + "/api/otims/updateMedicationDetailsIsExported";
							GenericService.serviceAction(METHOD_POST, URI,list).then(function(response) {

							});
							
						}
						
						$scope.medicationListForUpdate = [];
					$scope.orderPatientIndent = function(list)
					{
						if($scope.toStoreId == ""){
							growl.warning("Please Select To Store",
									{
										title : 'warning!'
									});
							return false;
						}
						
						$rootScope.startLoaderPopup();
						$scope.indentQty;
						$scope.statusId ;
						$scope.uomTypeId;
						$scope.uomUnitId;
						$scope.kitItemId; 
						$scope.medicationItemsFromKit=[];
						$scope.medicationListForUpdate = [];
						angular.forEach(list,function(value,index) {
							if((value.kitId!=null) && ( value.itemId==null)){
								$scope.indentQty = value.itemQuantity;
								if(value.isActive)
									{
										$scope.statusId = 1;
									}else
									{
										$scope.statusId = 0;
									}
								$scope.kitItemId = value.kitItemId;
								$scope.uomTypeId = value.uomTypeId == null ? 3:value.uomTypeId;
								$scope.uomUnitId = value.uomUnitId == null ? 75:value.uomUnitId;
								$scope.indentQty = value.itemQuantity;
								var dataForKitList={
										itemId : $scope.kitItemId,
										uomTypeId : $scope.uomTypeId,
										uomUnitId : $scope.uomUnitId,
										indentQuantity : $scope.indentQty,
										statusId : $scope.statusId	
										
								};
								$scope.medicationItemsFromKit.push(dataForKitList);
							}
							if((value.kitId==null) && ( value.itemId!=null)){
								var dataForKitList={
										itemId : value.itemId,
										uomTypeId :  value.uomTypeId == null ? 3:value.uomTypeId,
										uomUnitId : value.uomUnitId == null ? 75:value.uomUnitId,
										indentQuantity : value.qty,
										statusId : 1	
								};
								$scope.medicationItemsFromKit.push(dataForKitList);
							}
							
							var newArr = {};
							newArr.medicationDetailsId = value.medicationDetailsId;
							newArr.updatedBy = $scope.userId;
							newArr.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
							
							$scope.medicationListForUpdate.push(newArr);
						});
						
						if($scope.medicationItemsFromKit.length>0){
							 dataForList={
										doctorId :list[0].doctorId,
										visitType :	list[0].visitType ,
										patientId : list[0].patientId ,
										consignment : false,  //get consignment by item id  -http://localhost:8081/HIS-WAR/api/globals/items/4
										/*toStoreId : $scope.toStoreId,
										storeId: $scope.storeId,*/
										storeId  : $scope.storeId,
										toStoreId : $scope.toStoreId,
										remark : '',
										statusId : 1,
										encounterId : list[0].encounterId,
										admissionId : list[0].admissionId,
										ward : list[0].ward,
										bed : list[0].bed,
										approvalStatusId : 1,
										items : $scope.medicationItemsFromKit,
										visitType : list[0].visitTypeId,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										userId : $scope.userId
								};
							
							var URI = BASE_URL + ROOT_URL + SAVE_PATIENT_INDENT;
							GenericService.serviceAction(METHOD_POST, URI,dataForList).then(function(response) {
								if(response.data.id!=null)
								{
									//$scope.storeId="";
									$rootScope.stopLoaderPopup();
									growl.success(response.data.message,
											{
												title : 'Success!'
											});
									 
										  $scope.updateMedicationIsExported($scope.medicationListForUpdate);
									
										  $('#detailsMedication').modal('hide');
										  $timeout( function(){
											  $scope.searchDetails();
										  },2000);
									//$scope.orderMedications = true;
									$scope.toStoreId = "";
									$scope.medicationDetails = [];
								}else
									{
									$rootScope.stopLoaderPopup();
									growl.error("Error",
											{
												title : 'error!'
											});
									$('#detailsMedication').modal('hide');
								}
						});
						
						}
					}
					
					//save Implant indent
					$scope.updateIsExported = function(list){

						var URI = BASE_URL + ROOT_URL + "/api/otims/updateImplantDetailsIsExported";
						GenericService.serviceAction(METHOD_POST, URI,list).then(function(response) {
						
						});
						
					}
					
					
					
					
					//save implant indent
					$scope.implantItemsFromKit=[];
					$scope.implantListForUpdate = [];
				$scope.orderImplantIndent = function(list)
				{
					if($scope.toStoreId == ""){
						growl.warning("Please Select To Store",
								{
									title : 'warning!'
								});
						return false;
					}
					
					$rootScope.startLoaderPopup();
					var indentQty;
					var statusId ;
					var uomTypeId;
					var uomUnitId;
					var kitItemId; 
					$scope.implantItemsFromKit=[];
					$scope.implantListForUpdate = [];
					angular.forEach(list,function(value,index) {
						if((value.kitId!=null) && ( value.itemId==null)){
							$scope.indentQty = value.itemQuantity;
							if(value.isActive)
								{
									statusId = 1;
								}else
								{
									statusId = 0;
								}
							kitItemId = value.kitItemId;
							uomTypeId = value.uomTypeId == null ? null:value.uomTypeId;
							uomUnitId = value.uomUnitId == null ? null:value.uomUnitId;
							indentQty = value.itemQuantity;
							var dataForKitList={
									itemId :kitItemId,
									uomTypeId : uomTypeId,
									uomUnitId : uomUnitId,
									indentQuantity : indentQty,
									statusId : statusId	
									
							};
							$scope.implantItemsFromKit.push(dataForKitList);
						}
						if((value.kitId==null) && ( value.itemId!=null)){
							var dataForKitList={
									itemId : value.itemId,
									uomTypeId :  value.uomTypeId == null ? null:value.uomTypeId,
									uomUnitId : value.uomUnitId == null ? null:value.uomUnitId,
									indentQuantity : value.qty,
									statusId : 1	
							};
							$scope.implantItemsFromKit.push(dataForKitList);
						}
						var newArr = {};
						newArr.implantDetailsId = value.implantDetailsId;
						newArr.updatedBy = $scope.userId;
						newArr.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
						
						$scope.implantListForUpdate.push(newArr);
					});
					
					if($scope.implantItemsFromKit.length>0){
						var dataForList={
									doctorId :list[0].doctorId,
									visitType :	list[0].visitType ,
									patientId : list[0].patientId ,
									consignment : false,  //get consignment by item id  -http://localhost:8081/HIS-WAR/api/globals/items/4
									storeId  : $scope.storeId,
									toStoreId : $scope.toStoreId,
									remark : '',
									statusId : 1,
									encounterId : list[0].encounterId,
									admissionId : list[0].admissionId,
									ward : list[0].ward,
									bed : list[0].bed,
									approvalStatusId : 1,
									items : $scope.implantItemsFromKit,
									visitType : list[0].visitTypeId,
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									userId : $scope.userId
							};
						
						var URI = BASE_URL + ROOT_URL + SAVE_PATIENT_INDENT;
						GenericService.serviceAction(METHOD_POST, URI,dataForList).then(function(response) {
							if(response.data.id!=null)
							{
								$rootScope.stopLoaderPopup();
								growl.success(response.data.message,
										{
											title : 'Success!'
										});
								 
									  $scope.updateIsExported($scope.implantListForUpdate);
								$scope.toStoreId = "";
								$scope.implantDetailsList = [];
								$('#detailsImplant').modal('hide');
								 $timeout( function(){
									 $scope.searchDetails();
								 },2000);
							}else
								{
								$rootScope.stopLoaderPopup();
								growl.error("Error",
										{
											title : 'error!'
										});
								$('#detailsImplant').modal('hide');
							}
					});
					
					}
				}
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					/*$scope.saveNonConsignmentIndent = function(nonConsignmentObj)
					{
						if(nonConsignmentObj!=null){
						var URI = BASE_URL + ROOT_URL + SAVE_IMPLANT_PATIENT_INDENT;
						GenericService.serviceAction(METHOD_POST, URI,nonConsignmentObj).then(function(response) {
						if(response.data.id!=null)
							{
							// $scope.storeId="";
								$rootScope.stopLoader();
								growl.success(response.data.message,
										{
											title : 'Success!'
										});
								//$scope.orderImplant = true;
								$scope.updateIsExported($scope.nonConsignmentItems);
								$scope.implantDetailsList = [];
								$scope.searchDetails();
								$('#detailsImplant').modal('hide');
							}
						else
						{
							$rootScope.stopLoader();
							growl.error("Error",
								{
									title : 'error!'
								});
							$('#detailsImplant').modal('hide');
					}
					
					});
						}
					}*/
					/*$scope.saveConsignmentIndent = function(consignmentObj,nonConsignmentObj)
					{
						var URI = BASE_URL + ROOT_URL + SAVE_IMPLANT_PATIENT_INDENT;
						GenericService.serviceAction(METHOD_POST, URI,consignmentObj).then(function(response) {
						if(response.data.id!=null)
							{
							// $scope.storeId="";
								$rootScope.stopLoader();
							//	$scope.orderImplant = true;
								$scope.updateIsExported($scope.consignmentItems);
								$scope.implantDetailsList = [];
								if(nonConsignmentObj.items.length>0){
								$scope.saveNonConsignmentIndent(nonConsignmentObj);
								}else
									{
									growl.success(response.data.message,
											{
												title : 'Success!'
											});
									}
								$scope.searchDetails();
								$('#detailsImplant').modal('hide');
							}
						else
						{
							$rootScope.stopLoader();
							$scope.saveNonConsignmentIndent(nonConsignmentObj.items.length>0);
							growl.error("Error",
								{
									title : 'error!'
								});
							$('#detailsImplant').modal('hide');
					}
					
					});
					}*/
					
					
					
					
					/*$scope.consignmentItems = [];
					$scope.nonConsignmentItems = [];
					
					$scope.orderImplantIndent = function(list)
					{
						$rootScope.startLoader();
						$scope.implantConsignmentObj={
								  "visitType":"",
								  "patientId": "",
								  "doctorId":"",
								  "indentTypeId": 2,
								  "admissionId": "",
								  "encounterId": "",
								  "fromStoreId": $scope.storeId,
								  "storeId" : $scope.toStoreId,
								  "priorityId": 1,
								  "isConsignment":"",
								  "isImplant": "",
								  "isKit": "",
								  "remark": " ",
								  "statusId": 1,
								  "approvalStatusId": 1,
								  "items": []
						};
						
						$scope.implantNonConsignmentObj={
								  "visitType":"",
								  "patientId": "",
								  "doctorId":"",
								  "indentTypeId": 2,
								  "admissionId": "",
								  "encounterId": "",
								  "fromStoreId": $scope.storeId,
								  "storeId" : $scope.toStoreId,
								  "priorityId": 1,
								  "isConsignment":"",
								  "isImplant": "",
								  "isKit": "",
								  "remark": " ",
								  "statusId": 1,
								  "approvalStatusId": 1,
								  "items": []
						};
						
					
						$scope.implantNonConsignmentList=[];
						
						angular.forEach(list,function(value,index) {
							
							if(value.isConsignment){
								$scope.implantConsignmentObj.visitType = list[0].visitTypeId;
								$scope.implantConsignmentObj.patientId = list[0].patientId;
								$scope.implantConsignmentObj.doctorId = list[0].doctorId; 
								$scope.implantConsignmentObj.admissionId = list[0].admissionId;
								$scope.implantConsignmentObj.encounterId = list[0].encounterId;
								$scope.implantConsignmentObj.isConsignment = value.isConsignment;
								$scope.implantConsignmentObj.isImplant = list[0].isImplant;
								$scope.implantConsignmentObj.isKit = list[0].isKit;
								var newdata= {};
								if(value.kitId!=null){
									newdata.itemId = value.kitItemId;
									newdata.indentQuantity = value.itemQuantity;
								}else{
									newdata.itemId = value.itemId;
									newdata.indentQuantity = value.qty;
								}
								newdata.statusId=1;
								$scope.implantConsignmentObj.items.push(newdata);
								
								var newArr = {};
								newArr.implantDetailsId = value.implantDetailsId;
								newArr.updatedBy = $scope.userId;
								newArr.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
								
								$scope.consignmentItems.push(newArr);
								
							}else{
								
								//$scope.implantConsignmentObj.isConsignment=false;
								$scope.implantNonConsignmentObj.visitType = list[0].visitTypeId;
								$scope.implantNonConsignmentObj.patientId = list[0].patientId;
								$scope.implantNonConsignmentObj.doctorId = list[0].doctorId;
								$scope.implantNonConsignmentObj.admissionId = list[0].admissionId;
								$scope.implantNonConsignmentObj.encounterId = list[0].encounterId;
								$scope.implantNonConsignmentObj.isConsignment = value.isConsignment;
								$scope.implantNonConsignmentObj.isImplant = list[0].isImplant;
								$scope.implantNonConsignmentObj.isKit = list[0].isKit;
								var newdata= {};
								  if(value.kitId!=null){
										newdata.itemId = value.kitItemId;
										newdata.indentQuantity = value.itemQuantity;
									}else{
										newdata.itemId = value.itemId;
										newdata.indentQuantity = value.qty;
									}
								newdata.statusId=1;
								$scope.implantNonConsignmentObj.items.push(newdata);
								
								var newArr = {};
								newArr.implantDetailsId = value.implantDetailsId;
								newArr.updatedBy = $scope.userId;
								newArr.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
								
								$scope.nonConsignmentItems.push(newArr);
							
							}
								
						});
						
						if($scope.implantConsignmentObj.items.length>0){
							$scope. saveConsignmentIndent($scope.implantConsignmentObj,$scope.implantNonConsignmentObj);
						}
						else if($scope.implantNonConsignmentObj.items.length>0)
						{
							$scope.saveNonConsignmentIndent($scope.implantNonConsignmentObj);
						}else{
							$rootScope.stopLoader();
							growl.warning("There is no implant for order",
								{
									title : 'warning!'
								});
							$('#detailsImplant').modal('hide');
						}
					}*/
						
			}]);
							