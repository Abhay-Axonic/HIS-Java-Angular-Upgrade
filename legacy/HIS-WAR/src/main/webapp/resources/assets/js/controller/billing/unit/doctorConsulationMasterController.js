/**
 * @author Kaustubh
 * @date 03-04-2018
 * 		Controller for doctorConsulationMaster unit level
 */
angular.module("myApp").controller("doctorConsulationMasterController", [ '$scope','$rootScope','BillingGenericService','GenericService','$sessionStorage','PagerService','promiseFactory','$cookies','$state','promiseFactoryWithObject','growl',
	function($scope,$rootScope,BillingGenericService,GenericService,$sessionStorage,PagerService,promiseFactory,$cookies,$state,promiseFactoryWithObject,growl){
	
	
	
	$scope.init = function(){

		var cookieObject = $cookies.getObject('cookieObject');
		if(cookieObject == undefined){
			$state.go('login');
			return;
		}
	$rootScope.loginpage = true;
	$scope.unitId = cookieObject.unitId;
	$scope.organizationId = cookieObject.organizationId;
	$scope.specialityMasterList=[];
	$scope.servicesList =[];
	$scope.doctorList = [];
	$scope.doctorConsultationList=[];
	/**Variables for pagination */
	$scope.offset = 0;
	$scope.noOfRecordsPerPage = 10;
	$scope.unitServiceMapperList = [];
	$scope.totalNoOfRecords;
	$scope.searchKeyword;
	$scope.commonListCount;
	$scope.pager = {};
	$scope.totalPagesForSpeciality;
	$scope.page;
	
	///**Variables for Speciality tab pagination */
	$scope.doctorConsultationSpecialityList=[];
	$scope.offset1 = 0;
	$scope.recordPerPage = 10;
	$scope.searchString;
	$scope.commonListCountForSpeciality;
	$scope.pager1 = {};
	$scope.page1;
	
	$scope.userId = cookieObject.userId;
	$scope.currentDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
	
	$scope.saveBtnFlag = true;
	$scope.updateBtnFlag = false;
	$scope.disableField=false;
	
	$scope.doctorConsultationObj={
			specialityId : 0,
			unitId : $scope.unitId,
			organizationId : $scope.organizationId,
			/*doctorId : null,*/
			newVisitServiceId : 0,
			followupVisitServiceId : 0,
			secondaryVisitServiceId : 0
	}
	
	$scope.doctorConsultationMasterList=[];
	$scope.selectedspecialityMasterList=[];
	$scope.selectedDoctorList=[];
	$scope.selectedPaymentEntList=[];
	
	$scope.selectedspecialityMasterDeptList=[];
	$scope.selectedPaymentEntDeptList=[];
	
	$scope.doctorConsultationDeptList=[];
	 // for popup
    $scope.showModal = false;
    $scope.buttonClicked = "";
    $scope.popUpFlag = true;
    $scope.IsVisibleDoc = false;
    $scope.IsVisibleDept = false;
    // end for popup
    
    $("#saveSpecialityDoc").removeClass("enabled");
	$("#saveSpecialityDoc").addClass("disabled");
	
	$("#saveFrDeptSepciality").removeClass("enabled");
	$("#saveFrDeptSepciality").addClass("disabled");
	
	$("#specialityDivId").removeClass("disabled");
	$("#specialityDivId").addClass("enabled");
	
	$("#doctorSpecialityDivId").removeClass("disabled");
	$("#doctorSpecialityDivId").addClass("enabled");
    
	}
	
    
	$scope.init();
	
	//-----Get doctorConsultationList-----//
	$scope.initDoctorConsultationMasterList = function(offset, noOfRecordsPerPage) {
		
		try{
			$rootScope.startLoader();
			offset = offset  != null ? offset  : 0;
			noOfRecordsPerPage =noOfRecordsPerPage != null ? noOfRecordsPerPage
					: 10;
			
		    var URI = BASE_URL + ROOT_URL + API + BILLING + UNIT + GETDOCTORCONSULTATIONSERVICEMAPPERLIST;
		    
		    var URI1 = BASE_URL + ROOT_URL + API + BILLING + UNIT + "/getDoctorConsultationServiceMapperListCount";
		    
			var obj = {
						organizationId :$scope.organizationId,
						unitId         :$scope.unitId,
						offset         :offset,
						noOfRecordsPerPage :noOfRecordsPerPage,
						tabNumber : 1
				    };
			
			var data1 = {
					organizationId :$scope.organizationId,
					unitId         :$scope.unitId,
					tabNumber : 1
			    };
			
			
			promiseFactoryWithObject.setPromisesWithObject(URI, URI1, "POST", "POST",obj,data1)
			.then(
					function(response) {
						$rootScope.stopLoader();
						$scope.doctorConsultationMasterList = [];
						$scope.doctorConsultationMasterList = response[0].listObject;
						$scope.commonListCount = response[1].object;
						$scope.setPage(1, false);
					});
			
		}catch (e) {
			console.log("Exception",e.message);
		}
	}
	
	$scope.getAssociateCompanyMasterList = function(companyId) {
		$scope.tempcompanyId=companyId;
		$rootScope.startLoader();
		  var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyId:companyId};
		    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getAssociateCompanyMaster"
		    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					 $scope.associateCompanyMasterList = response.data.listObject;
					 // console.log("associateCompanyMasterList",JSON.stringify($scope.associateCompanyMasterList));
				}
				$rootScope.stopLoader();
			});
		  }
	$scope.getCompanyMasterList = function(companyTypeId) {
		if(companyTypeId != ""){
		if(companyTypeId==3){
			
			$scope.insurenceFlag=false;
			$scope.associateCompanyId = null;
    		$scope.companyId = null;
			
		}else{
			$scope.insurenceFlag=true;
		}
		$rootScope.startLoader();
		  var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyTypeId:companyTypeId};
		    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getCompanyMasterList"
		    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					 $scope.companyMasterList = response.data.listObject;
					 //console.log("companyMasterList",JSON.stringify($scope.companyMasterList));
				}
				$rootScope.stopLoader();
			});
		}else{
			$scope.associateCompanyId = '';
    		$scope.companyId = '';
		}
		  }
	
	var idAlready;
	function checkIdExistsAlready(arr, val) {
		return arr
				.some(function(arr) {
					if(val === arr.id)
						{
							return true;
						}
					else
						{
							return false;
						}
				});
	}
	
	 $scope.clickedPaymentEntitlement = function(){
			 
		                console.log('selected: ',$scope.selectedPaymentEntList);
		                if($scope.selectedPaymentEntList.length == 3){
		                	$scope.insurenceFlag = false;
		                	$scope.companyFlag = false;
		                }else if($scope.selectedPaymentEntList.length == 1){
		                	if($scope.selectedPaymentEntList[0].id == 2){
		                		$scope.getCompanyMasterList(2);
		                		$scope.insurenceFlag = true;
			                	$scope.companyFlag = true;
		                	}
		                	if($scope.selectedPaymentEntList[0].id == 3){
		                		$scope.getCompanyMasterList(3);
		                		$scope.insurenceFlag = false;
			                	$scope.companyFlag = true;
		                	}
		                	if($scope.selectedPaymentEntList[0].id == 1){
		                		$scope.insurenceFlag = false;
			                	$scope.companyFlag = false;
		                	}
		                }else if($scope.selectedPaymentEntList.length == 2){
		                	/*idAlready=checkIdExistsAlready($scope.selectedPaymentEntList,1);
		                	 if (!idAlready) {*/
		                		    $scope.insurenceFlag = false;
				                	$scope.companyFlag = false;
		                	/* }else{
		                		 angular.forEach($scope.selectedPaymentEntList,function(value,index) {
		                         	  if(value.id == 2){
		                         		 $scope.getCompanyMasterList(2);
		 		                		$scope.insurenceFlag = true;
		 			                	$scope.companyFlag = true;
		                         	  }
		                         	  if(value.id == 3){
		                         		 $scope.getCompanyMasterList(3);
				                		$scope.insurenceFlag = false;
					                	$scope.companyFlag = true;
		                         	  }
		           					});
		                	 }*/
		                }else{
		                	   $scope.insurenceFlag = false;
			                	$scope.companyFlag = false;
		                }
		    }
	 
	 
	 $scope.clickedPaymentEntitlementDept = function(){
		 
         console.log('selected: ',$scope.selectedPaymentEntDeptList);
         if($scope.selectedPaymentEntDeptList.length == 3){
         	$scope.insurenceFlag = false;
         	$scope.companyFlag = false;
         }else if($scope.selectedPaymentEntDeptList.length == 1){
         	if($scope.selectedPaymentEntDeptList[0].id == 2){
         		$scope.getCompanyMasterList(2);
         		$scope.insurenceFlag = true;
             	$scope.companyFlag = true;
         	}
         	if($scope.selectedPaymentEntDeptList[0].id == 3){
         		$scope.getCompanyMasterList(3);
         		$scope.insurenceFlag = false;
             	$scope.companyFlag = true;
         	}
         	if($scope.selectedPaymentEntDeptList[0].id == 1){
         		$scope.insurenceFlag = false;
             	$scope.companyFlag = false;
         	}
         }else if($scope.selectedPaymentEntDeptList.length == 2){
         	/*idAlready=checkIdExistsAlready($scope.selectedPaymentEntList,1);
         	 if (!idAlready) {*/
         		    $scope.insurenceFlag = false;
	                	$scope.companyFlag = false;
         	/* }else{
         		 angular.forEach($scope.selectedPaymentEntList,function(value,index) {
                  	  if(value.id == 2){
                  		 $scope.getCompanyMasterList(2);
	                		$scope.insurenceFlag = true;
		                	$scope.companyFlag = true;
                  	  }
                  	  if(value.id == 3){
                  		 $scope.getCompanyMasterList(3);
	                		$scope.insurenceFlag = false;
		                	$scope.companyFlag = true;
                  	  }
    					});
         	 }*/
         }else{
         	   $scope.insurenceFlag = false;
             	$scope.companyFlag = false;
         }
}
	 
	$scope.initDoctorConsultationMasterList($scope.offset,$scope.noOfRecordsPerPage);
	
		$scope.getDoctorConsultationMasterList = function(orgId,offset, noOfRecordsPerPage) {
			$rootScope.startLoader();
			$scope.doctorConsultationMasterList = [];
			offset = offset != null ? offset : 0;
			noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
					: 10;
			   var URI = BASE_URL + ROOT_URL + API + BILLING + UNIT + GETDOCTORCONSULTATIONSERVICEMAPPERLIST;
			   var obj = {
						organizationId :$scope.organizationId,
						unitId         :$scope.unitId,
						offset         :offset,
						noOfRecordsPerPage :noOfRecordsPerPage,
						tabNumber : 1
				    };
			GenericService.serviceAction("POST", URI, obj)
					.then(
							function(response) {
								$rootScope.stopLoader();
								$scope.doctorConsultationMasterList = response.data.listObject;
							});
	}
	
		$scope.setPage = function(page, flag) {
		if (page < 1 || page > $scope.pager.totalPages) {
			return;
		}
		$scope.pager = PagerService.GetPager(
				$scope.commonListCount, page,
				$scope.noOfRecordsPerPage);
		if (flag) {
			$scope.getDoctorConsultationMasterList($scope.orgId,$scope.pager.startIndex,$scope.pager.pageSize);
		}
	}
	$scope.setNoOfRecords = function() {
		$scope.initDoctorConsultationMasterList($scope.offset,$scope.noOfRecordsPerPage);
	};
	$scope.specialityMasterList=[];
	$scope.specialityList=[];
	//------------ Get Speciality Master List ------------//
	$scope.getSpecialityMasterList = function(){
		try{
			var URI = BASE_URL + ROOT_URL + GETACTIVESPECIALITY;
			var obj = {
						organizationId : $scope.organizationId,
						unitId         :$scope.unitId
				    }
			BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				console.log(response);
				$scope.deptWiseSpecialityMasterList=[];
				if(response.data.status == "success"){
					$scope.specialityList = response.data.listObject;
					angular.forEach(response.data.listObject,
							function(value,index) {
						
								var data = {};
								data.id = value.specialityId;
								data.label = value.specialityName;
						
						/*	if(value.deptWiseAppointment=='N'){								
								$scope.specialityMasterList.push(data);
							}else if(value.deptWiseAppointment=='Y'){
								$scope.deptWiseSpecialityMasterList.push(data);
							}*/
								if(value.deptWiseAppointment=='N' && value.isClinicalSpeciality=='Y'){
									$scope.specialityMasterList.push(data);
								}
								if(value.deptWiseAppointment=='Y' && value.isClinicalSpeciality=='Y'){
									$scope.deptWiseSpecialityMasterList.push(data);
								}
								
							});
						console.log('specialityMasterList',$scope.specialityMasterList);
				}
					
			});
		}catch (e) {
			console.log("Exception",e.message);
		}
	}
	$scope.getSpecialityMasterList();
	$scope.paymentEntList=
	[
	 
	  { id: 3, label: 'Company' },
	  { id: 2, label: 'Insurance' },
	  { id: 1, label: 'Self' },
	 ];
	//------------ Get Doctors By SpecialityId ----------//
	/*$scope.getDoctorsBySpecialityId = function(specialityId){
		$rootScope.startLoader();
		//$scope.unitService.subSpecialityId="";
		$scope.doctorList=[];
		  var URI = BASE_URL + ROOT_URL + GETDOCTORBYSPECIALITYID;
		  var obj = {
					organizationId : $scope.organizationId,
					unitId         : $scope.unitId,
					specialityId   : specialityId
			    }
		BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			if(response.data.status == "success")
				$scope.doctorList = response.data.listObject;
		});
	}*/
	$scope.getDoctorsBySpecialityId = function(){
	
		if( $scope.selectedspecialityMasterList!=undefined &&  $scope.selectedspecialityMasterList!=null && $scope.selectedspecialityMasterList.length>0){
			console.log("selected speciality",$scope.selectedspecialityMasterList);
		
			
                var speciality = [];
                angular.forEach($scope.selectedspecialityMasterList,function(value,index) {
              	  speciality.push(value.id);
					
					});
                var data = {
              		  organizationId:$scope.organizationId,
              		  unitId:$scope.unitId,
              		  specialityArray:speciality
                }
                console.log(data);
                var URI = BASE_URL + ROOT_URL + GETDOCTORSLISTAGANISTSPECIALITIES;
			BillingGenericService.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_FALSE)
					.then(function(response) {
								console.log(response);
								if (response.data.status == "success")
									{
									$scope.doctorList = [];
									$scope.doctorMasterList=response.data.listObject;
									angular.forEach(response.data.listObject,function(value,index) {
										
												var data = {};
												data.id=value.doctorId+'_'+value.specialityId;
												data.label=value.firstName+' '+value.lastName;
												$scope.doctorList.push(data);
											
											});
																							 
									}
									
							});
            
			
		
		/*$scope.doctorList=[];
		 	$rootScope.startLoader();
		  var URI = BASE_URL + ROOT_URL + GETDOCTORBYSPECIALITYID;
		  var obj = {
					organizationId : $scope.organizationId,
					unitId         : $scope.unitId,
					specialityId   : specialityId
			    }
		BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			if(response.data.status == "success")
				$scope.doctorList = response.data.listObject;
		});*/
	}
	}	
	
	
	//------------- Get All Services by Speciality and sub-speciality id=1000(Consultation) ---------------//
	/*/splId/{splId}/subSplId/{subSplId}*/
	$scope.getServicesBySpecialityId = function(){
		try{
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL+ "/billing/service/consultations/org/"+$scope.organizationId+"/unit/"+$scope.unitId+"/splId/"+0+"/subSplId/"+0;
			BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == "success")
					$scope.servicesList = response.data.listObject;
					console.log('service list',$scope.servicesList)
			});
		}catch (e) {
			console.log("Exception",e.message);
		}
	}
	$scope.getServicesBySpecialityId();
	
	// function for popup ok button call
    $scope.PopupOkBtn = function() {
     $scope.popUpFlag = false;
     $scope.saveDoctorCosultationMaper();
    }
	
	$scope.saveDoctorCosultationMaper= function (){
		try{
	
			
			
		
			if ($scope.popUpFlag) {
		         $scope.showModal = !$scope.showModal;

		        } else {
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + API + BILLING + UNIT + SAVEDOCTORCONSULTATIONSERVICEMAPPER;
				$scope.doctorConsultationObj.createdDate=$scope.currentDate;
				$scope.doctorConsultationObj.updatedDate=$scope.currentDate;
				$scope.doctorConsultationObj.createdBy=$scope.userId;	
				$scope.doctorConsultationObj.updatedBy=$scope.userId;	
				$scope.doctorConsultationObj.status='A';
				$scope.doctorConsultationObj.unitId=$scope.unitId;
				$scope.doctorConsultationObj.organizationId=$scope.organizationId;
				
				if($scope.doctorConsultationList!=undefined && $scope.doctorConsultationList!=null && $scope.doctorConsultationList.length>0){
					$scope.doctorConsultationObj.docConsultationMapperList=$scope.doctorConsultationList;
				}else if($scope.doctorConsultationDeptList!=undefined && $scope.doctorConsultationDeptList!=null && $scope.doctorConsultationDeptList.length>0){
					$scope.doctorConsultationObj.docConsultationMapperList=$scope.doctorConsultationDeptList;
				}
//				$scope.doctorConsultationObj.docConsultationMapperList=$scope.doctorConsultationList;
				console.log("final Obj",$scope.doctorConsultationObj);
				//return false;
				BillingGenericService.serviceAction(METHOD_POST,URI, $scope.doctorConsultationObj, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
					$scope.init();
					$scope.IsVisibleDoc = false;
					$scope.IsVisibleDept = false;
					$scope.getServicesBySpecialityId();
					$scope.getSpecialityMasterList();
						$scope.initDoctorConsultationMasterList($scope.offset,$scope.noOfRecordsPerPage);
						$scope.initDoctorConsultationSpecialityList($scope.offset1,$scope.recordPerPage);
				    $("#doctorSpecialityDivId").removeClass("disabled");
					$("#doctorSpecialityDivId").addClass("enabled");
				
					$scope.updateBtnFlag = false;
					$scope.saveBtnFlag = true;
				}
				);
		       }
	}catch (e) {
		console.log("Exception",e.message);
	}

	}
	$scope.emptyList = function() {
		$scope.doctorConsultationList=[];
		$scope.doctorConsultationDeptList=[];
	    $scope.IsVisibleDoc = false;
	    $scope.IsVisibleDept = false;
	}
	
	// getDetailsById
	$scope.getDetailsById = function(id) {

		$scope.saveBtnFlag = false;
		$scope.updateBtnFlag = true;
		$scope.disableField=true;
		  

		var data = {

				doctorConsultationServiceMapperId : id
		}
		console.log(data);
		var URI = BASE_URL + ROOT_URL + API + BILLING + UNIT
				+ GETDOCTORCONSULTATIONSERVICEMAPPERBYID;
		
		BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			
			if (response.data.status == "success") {
				$scope.doctorConsultationList=[];
				$scope.doctorConsultationDeptList=[];
				console.log("edit ",response.data.listObject);
				$scope.doctorConsultationObj.doctorConsultationServiceMapperId = response.data.listObject[0].doctorConsultationServiceMapperId;
				$scope.doctorConsultationObj.specialityId = response.data.listObject[0].specialityId;
				$scope.getDoctorsBySpecialityId($scope.doctorConsultationObj.specialityId);
				$scope.doctorConsultationObj.doctorId = response.data.listObject[0].doctorId;
				$scope.doctorConsultationObj.newVisitServiceId = response.data.listObject[0].newVisitServiceId;
				$scope.doctorConsultationObj.followupVisitServiceId = response.data.listObject[0].followupVisitServiceId;
				$scope.doctorConsultationObj.secondaryVisitServiceId = response.data.listObject[0].secondaryVisitServiceId;
				if($scope.doctorConsultationObj.doctorId!=null || $scope.doctorConsultationObj.doctorId!=undefined){
					$('#doctorAddNew').modal('show');
					 $scope.IsVisibleDoc = true;
					 
					
					$("#doctorSpecialityDivId").removeClass("enabled");
					$("#doctorSpecialityDivId").addClass("disabled");
					$scope.doctorConsultationList.push(response.data.listObject[0]);
				}else{
					   $scope.IsVisibleDept = true;
					$('#specialtyAddNew').modal('show');
					$("#specialityDivId").removeClass("enabled");
					$("#specialityDivId").addClass("disabled");
					$scope.doctorConsultationDeptList.push(response.data.listObject[0]);
				}
			}
			
		});
	}
	
		
		// updateDoctorCosultation
		$scope.updateDoctorCosultationMaper = function() {
			
			if($scope.doctorConsultationList.length>0){
				$scope.doctorConsultationObj=$scope.doctorConsultationList[0];
		}else{
			$scope.doctorConsultationObj=$scope.doctorConsultationDeptList[0];
		}
		
//			$scope.doctorConsultationObj=$scope.doctorConsultationList[0];
			$scope.doctorConsultationObj.updatedDate=$scope.currentDate;
			$scope.doctorConsultationObj.updatedBy=$scope.userId;
			var URI = BASE_URL + ROOT_URL + API + BILLING + UNIT
					+ UPDATEDOCTORCONSULTATIONSERVICEMAPPER;
			
			BillingGenericService.serviceAction(METHOD_POST,URI, $scope.doctorConsultationObj, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
				if (response.data.status == "success") {
					$scope.init();
					   $scope.IsVisibleDoc = false;
					   $scope.IsVisibleDept = false;
					$scope.getServicesBySpecialityId();
					$scope.getSpecialityMasterList();
						$scope.initDoctorConsultationMasterList($scope.offset,$scope.noOfRecordsPerPage);
						$scope.initDoctorConsultationSpecialityList($scope.offset1,$scope.recordPerPage);
				    $("#doctorSpecialityDivId").removeClass("disabled");
					$("#doctorSpecialityDivId").addClass("enabled");
				
					$scope.updateBtnFlag = false;
					$scope.saveBtnFlag = true;
					
				}else{
					$scope.init();
					$scope.IsVisibleDoc = false;
					$scope.IsVisibleDept = false;
					$scope.getServicesBySpecialityId();
					$scope.getSpecialityMasterList();
						$scope.initDoctorConsultationMasterList($scope.offset,$scope.noOfRecordsPerPage);
						$scope.initDoctorConsultationSpecialityList($scope.offset1,$scope.recordPerPage);
				}
				
			});
		}
		
		$scope.clearFields=function(){
			$scope.selectedPaymentEntList=[];
			$scope.selectedDoctorList=[];
			$scope.selectedspecialityMasterList=[];
			$scope.selectedspecialityMasterDeptList=[];
			$scope.selectedPaymentEntDeptList=[];
			$scope.doctorConsultationObj={
					specialityId : 0,
					unitId : $scope.unitId,
					organizationId : $scope.organizationId,
					/*doctorId : null,*/
					newVisitServiceId : 0,
					followupVisitServiceId : 0,
					secondaryVisitServiceId : 0
			}
		    $scope.IsVisibleDoc = false;
		    $scope.IsVisibleDept = false;
		   // $scope.disableField = false;
		    
		    $("#doctorSpecialityDivId").removeClass("disabled");
			$("#doctorSpecialityDivId").addClass("enabled");
			
			//speciality Id
			$("#specialityDivId").removeClass("disabled");
			$("#specialityDivId").addClass("enabled");
		
			$scope.updateBtnFlag = false;
			$scope.saveBtnFlag = true;
		    $scope.doctorConsultationList=[];
		    $scope.doctorConsultationDeptList=[];
		}
		
		  $scope.getSpecialityName = function(id){
			  
			 var specialityName = $.grep($scope.specialityList, function (spe) {						
		            return spe.specialityId == id;
		        })[0].specialityName;
			 return specialityName;
		   }
		  $scope.getDoctorName = function(id){
			  
			   var doctorName = $.grep($scope.doctorMasterList, function (doc) {						
	                return doc.doctorId == id;
	            })[0].doctorName;
				 return doctorName;
			   }
		  $scope.getPaymentEntName = function(id){
			  
			  var label = $.grep($scope.paymentEntList, function (ent) {						
	                return ent.id == id;
	            })[0].label;
				 return label;
			   }
		  
	
			
			
		//adding combination for doctor consultation for doctor speciality wise
		$scope.doctorConsultationList=[];
		$scope.mapperDoctorConsultationList = [];
		$scope.addConsultationCombination=function(){
			$scope.doctorConsultationList=[];
			$scope.mapperDoctorConsultationList = [];

			if($scope.selectedspecialityMasterList==null || $scope.selectedspecialityMasterList==undefined || $scope.selectedspecialityMasterList.length==0){
				growl.warning("Please select the Specialty",
						{
							title : 'Warning!'
						});
				return ;
			}
			if($scope.selectedDoctorList==null || $scope.selectedDoctorList==undefined || $scope.selectedDoctorList.length==0){
				growl.warning("Please select the doctor",
						{
							title : 'Warning!'
						});
				return ;
			}
			if($scope.selectedPaymentEntList==null || $scope.selectedPaymentEntList==undefined || $scope.selectedPaymentEntList.length==0){
				growl.warning("Please select the payment entitlement",
						{
							title : 'Warning!'
						});
				return ;
			}
			if($scope.selectedPaymentEntList.length == 1){
				if($scope.selectedPaymentEntList[0].id == 2){
					if($scope.companyId > 0){
						if($scope.associateCompanyId == '' || $scope.associateCompanyId == null || $scope.associateCompanyId == undefined){
						growl.warning("Please select insurance compamy..!!",
								{
									title : 'Warning!'
								});
						return ;
						}
					}
				}
			}
			
			if($scope.doctorConsultationObj.newVisitServiceId==0 || $scope.doctorConsultationObj.followupVisitServiceId==0 || $scope.doctorConsultationObj.secondaryVisitServiceId==0) {
				growl.warning("Please select all visit type",
						{
							title : 'Warning!'
						});
				return ;
			}
			
			
		
			$scope.companyName = null;
			$scope.insuranceCompanyName = null;
			
			if($scope.selectedPaymentEntList.length == 1){
				if($scope.selectedPaymentEntList[0].id == 2){
					if($scope.companyId  > 0 && $scope.associateCompanyId){
						$scope.companyName = $.grep($scope.companyMasterList, function (ser) {						
			                return ser.id == $scope.companyId;
			            })[0].name;
						$scope.insuranceCompanyName = $.grep($scope.associateCompanyMasterList, function (ser) {						
			                return ser.id == $scope.associateCompanyId;
			            })[0].name;
					}
				}
				
				if($scope.selectedPaymentEntList[0].id == 3){
					if($scope.companyId  > 0 ){
						$scope.companyName = $.grep($scope.companyMasterList, function (ser) {						
			                return ser.id == $scope.companyId;
			            })[0].name;
					}
				}
				
			}
			//getting  visit name
			$scope.newVisit = $.grep($scope.servicesList, function (ser) {						
                return ser.serviceMasterId == $scope.doctorConsultationObj.newVisitServiceId;
            })[0].serviceStandardName;
			
			$scope.follwupVisit = $.grep($scope.servicesList, function (ser) {						
                return ser.serviceMasterId == $scope.doctorConsultationObj.followupVisitServiceId;
            })[0].serviceStandardName;
			
			$scope.secondaryVisit = $.grep($scope.servicesList, function (ser) {						
                return ser.serviceMasterId == $scope.doctorConsultationObj.secondaryVisitServiceId;
            })[0].serviceStandardName;
			
			
			arraList=cartesian($scope.selectedDoctorList,$scope.selectedPaymentEntList,$scope.selectedspecialityMasterList );
			for(i=0;i<arraList.length;i++){			
				
				
				var tempObj={specialityName:"",specialityId:"",
						doctorName:"",doctorId:"",
						newVisitServiceName:"",newVisitServiceId:"",
						followupVisitServiceName:"",followupVisitServiceId:"",
						secondaryVisitServiceId:"",secondaryVisitServiceName:"",
						paymentEntitlement:"",paymentEntitlementId:"",
							newVisitRate:null, followupVisitRate:null,secondaryVisitRate:null,companyId:null,companyName:null,assoCompanyId:null,insuranceCompanyName : null};
				
				
				
				var tempArr=arraList[i];
				
				var Ids=tempArr[0].id.split('_');
				var specialityid=tempArr[2].id;
				var doctorId=Ids[0];
				
				$scope.paymentEntitlementId =tempArr[1].id;			
				
					
				var newTempObj = angular.copy(tempObj);
				
				newTempObj.specialityId=specialityid; 
				newTempObj.specialityName=$scope.getSpecialityName(newTempObj.specialityId);		
				
				
				newTempObj.paymentEntitlementId=$scope.paymentEntitlementId;
				newTempObj.paymentEntitlement=$scope.getPaymentEntName($scope.paymentEntitlementId);
				
				newTempObj.doctorId=doctorId;
				newTempObj.doctorName=$scope.getDoctorName(doctorId);	
				
				
				newTempObj.secondaryVisitServiceId=$scope.doctorConsultationObj.secondaryVisitServiceId;
				newTempObj.secondaryVisitServiceName=$scope.secondaryVisit;
				
				
				newTempObj.newVisitServiceName= $scope.newVisit;
				newTempObj.newVisitServiceId= $scope.doctorConsultationObj.newVisitServiceId;
				
				newTempObj.followupVisitServiceName=$scope.follwupVisit;				
				newTempObj.followupVisitServiceId=$scope.doctorConsultationObj.followupVisitServiceId;
				
				newTempObj.newVisitRate=null;
				newTempObj.followupVisitRate=null;
				newTempObj.secondaryVisitRate=null;
				
				newTempObj.companyId = $scope.companyId;
				newTempObj.companyName = $scope.companyName;
				newTempObj.assoCompanyId = $scope.associateCompanyId;
				newTempObj.insuranceCompanyName = $scope.insuranceCompanyName;
				
				
				console.log(JSON.stringify(newTempObj))			
				
				/*if($scope.doctorConsultationList.length==0){
					$scope.doctorConsultationList.push(newTempObj);
				}else{
					
					isGroupExistsCap=checkGroupIdExistsOrNotCap($scope.doctorConsultationList,newTempObj)
					  if (!isGroupExistsCap) {
						$scope.doctorConsultationList.push(newTempObj);
					  }
				}
						*/	
				
				if($scope.mapperDoctorConsultationList.length==0){
				$scope.mapperDoctorConsultationList.push(newTempObj);
			}else{
				
				isGroupExistsCap=checkGroupIdExistsOrNotCap($scope.mapperDoctorConsultationList,newTempObj)
				  if (!isGroupExistsCap) {
					$scope.mapperDoctorConsultationList.push(newTempObj);
				  }
			}
			 
			} //FOr
			
			
			
			
			console.log("$scope.mapperDoctorConsultationList",$scope.mapperDoctorConsultationList);
			
			var URI = BASE_URL + ROOT_URL + "/api/billing/unit/doctorCon/checkAlreadyExistOrNot";
			if($scope.mapperDoctorConsultationList!=undefined && $scope.mapperDoctorConsultationList!=null && $scope.mapperDoctorConsultationList.length>0){
				$scope.doctorConsultationObj.docConsultationMapperList=$scope.mapperDoctorConsultationList;
			}else if($scope.mapperDoctorConsultationList!=undefined && $scope.mapperDoctorConsultationList!=null && $scope.mapperDoctorConsultationList.length>0){
				$scope.doctorConsultationObj.docConsultationMapperList=$scope.mapperDoctorConsultationList;
			}
			$scope.DoctorConCount = 0;
			$scope.message = "";
			BillingGenericService.serviceAction(METHOD_POST,URI, $scope.doctorConsultationObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if (response.data.status == "success") {
					
					if(response.data.listObject.length>0){
						$scope.DoctorConCount = response.data.listObject.length;
						$scope.message = "Total "+$scope.DoctorConCount+" record(s) already exist, Do you want to Continue ?";
                            $("#already_exist").modal("show");
					}else{
						
						var i = 1;
                        angular.forEach($scope.mapperDoctorConsultationList,function(value1,index1) {
                        if($scope.doctorConsultationList.length==0){

					        $scope.doctorConsultationList.push(value1);

			    	    }else{
					
					            isGroupExistsCap=checkGroupIdExistsOrNotCap($scope.doctorConsultationList,value1)

					             if (!isGroupExistsCap) {
						            $scope.doctorConsultationList.push(value1);
					            }
				        	}
                        if($scope.doctorConsultationList.length>0 && i == $scope.mapperDoctorConsultationList.length){					
            				$scope.IsVisibleDoc = true;
            				   
            			}
                        i++;
                        });
						
					} 
					

					console.log("$scope.doctorConsultationList",$scope.doctorConsultationList);
				}
			});
			
			
			
		
			console.log("doctor Consultation list",$scope.doctorConsultationList);
		}
		
		$scope.overrideDoctorCon = function(){
			
			$scope.updateBtnFlag = false;
			$scope.saveBtnFlag = true;
            $scope.IsVisibleDoc=true;
			angular.forEach($scope.mapperDoctorConsultationList,function(value1,index1) {
            if($scope.doctorConsultationList.length==0){

		        $scope.doctorConsultationList.push(value1);

    	    }else{
		
		            isGroupExistsCap=checkGroupIdExistsOrNotCap($scope.doctorConsultationList,value1)

		             if (!isGroupExistsCap) {
			            $scope.doctorConsultationList.push(value1);
		            }
	        }
            	
            });
            
		}
		
		
		//adding combination for doctor consultation for dept speciality wise
		$scope.doctorConsultationDeptList=[];
		$scope.addConsultationCombinationDept=function(){
			
			if($scope.selectedspecialityMasterDeptList==undefined || $scope.selectedspecialityMasterDeptList==null || $scope.selectedspecialityMasterDeptList.length==0) {
				growl.warning("Please select speciality",
						{
							title : 'Warning!'
						});
				return ;
			}
			if($scope.selectedPaymentEntDeptList==null || $scope.selectedPaymentEntDeptList==undefined || $scope.selectedPaymentEntDeptList.length==0){
				growl.warning("Please select the payment entitlement",
						{
							title : 'Warning!'
						});
				return ;
			}
			
			if($scope.selectedPaymentEntDeptList.length == 1){
				if($scope.selectedPaymentEntDeptList[0].id == 2){
					if($scope.companyId > 0){
						if($scope.associateCompanyId == '' || $scope.associateCompanyId == null || $scope.associateCompanyId == undefined){
						growl.warning("Please select insurance compamy..!!",
								{
									title : 'Warning!'
								});
						return ;
						}
					}
				}
			}
			if($scope.doctorConsultationObj.newVisitServiceId==0 || $scope.doctorConsultationObj.followupVisitServiceId==0 || $scope.doctorConsultationObj.secondaryVisitServiceId==0) {
				growl.warning("Please select all visit type",
						{
							title : 'Warning!'
						});
				return ;
			}
		
			
			$scope.companyName = null;
			$scope.insuranceCompanyName = null;
			
			if($scope.selectedPaymentEntDeptList.length == 1){
				if($scope.selectedPaymentEntDeptList[0].id == 2){
					if($scope.companyId  > 0 && $scope.associateCompanyId){
						$scope.companyName = $.grep($scope.companyMasterList, function (ser) {						
			                return ser.id == $scope.companyId;
			            })[0].name;
						$scope.insuranceCompanyName = $.grep($scope.associateCompanyMasterList, function (ser) {						
			                return ser.id == $scope.associateCompanyId;
			            })[0].name;
					}
				}
				
				if($scope.selectedPaymentEntDeptList[0].id == 3){
					if($scope.companyId  > 0 ){
						$scope.companyName = $.grep($scope.companyMasterList, function (ser) {						
			                return ser.id == $scope.companyId;
			            })[0].name;
					}
				}
				
			}
			
			
			
			//getting  visit name
			$scope.newVisit = $.grep($scope.servicesList, function (ser) {						
                return ser.serviceMasterId == $scope.doctorConsultationObj.newVisitServiceId;
            })[0].serviceStandardName;
			
			$scope.follwupVisit = $.grep($scope.servicesList, function (ser) {						
                return ser.serviceMasterId == $scope.doctorConsultationObj.followupVisitServiceId;
            })[0].serviceStandardName;
			
			$scope.secondaryVisit = $.grep($scope.servicesList, function (ser) {						
                return ser.serviceMasterId == $scope.doctorConsultationObj.secondaryVisitServiceId;
            })[0].serviceStandardName;
		
				
				
				arraList=cartesian($scope.selectedspecialityMasterDeptList,$scope.selectedPaymentEntDeptList);
				for(i=0;i<arraList.length;i++){	
					
					var tempObj={specialityName:"",specialityId:"",
							newVisitServiceName:"",newVisitServiceId:"",
							followupVisitServiceName:"",followupVisitServiceId:"",
							secondaryVisitServiceId:"",secondaryVisitServiceName:"",
							paymentEntitlement:"",paymentEntitlementId:"",
								newVisitRate:null, followupVisitRate:null,secondaryVisitRate:null,companyId:null,companyName:null,assoCompanyId:null,insuranceCompanyName : null};
					
					var tempArr=arraList[i];
					
					var specialityid=tempArr[0].id;
					
					$scope.paymentEntitlementId =tempArr[1].id;			
					
						
					var newTempObj = angular.copy(tempObj);
					
					newTempObj.specialityId=specialityid;
					newTempObj.specialityName=$scope.getSpecialityName(specialityid);		
					
					
					newTempObj.paymentEntitlementId=$scope.paymentEntitlementId;
					newTempObj.paymentEntitlement=$scope.getPaymentEntName($scope.paymentEntitlementId);
					
							
					
					newTempObj.secondaryVisitServiceId=$scope.doctorConsultationObj.secondaryVisitServiceId;
					newTempObj.secondaryVisitServiceName=$scope.secondaryVisit;
					
					
					newTempObj.newVisitServiceName= $scope.newVisit;
					newTempObj.newVisitServiceId= $scope.doctorConsultationObj.newVisitServiceId;
					
					newTempObj.followupVisitServiceName=$scope.follwupVisit;				
					newTempObj.followupVisitServiceId=$scope.doctorConsultationObj.followupVisitServiceId;
					
					newTempObj.newVisitRate=null;
					newTempObj.followupVisitRate=null;
					newTempObj.secondaryVisitRate=null;
					
					newTempObj.companyId = $scope.companyId;
					newTempObj.companyName = $scope.companyName;
					newTempObj.assoCompanyId = $scope.associateCompanyId;
					newTempObj.insuranceCompanyName = $scope.insuranceCompanyName;
					
					console.log(JSON.stringify(newTempObj))	
					if($scope.doctorConsultationDeptList.length==0){
						$scope.doctorConsultationDeptList.push(newTempObj);
					}else{
						
						isGroupExistsCapSpec=checkGroupIdExistsOrNotCapSpec($scope.doctorConsultationDeptList,newTempObj)
						  if (!isGroupExistsCapSpec) {
							$scope.doctorConsultationDeptList.push(newTempObj);
						  }
					}
				}
				if($scope.doctorConsultationDeptList.length>0){					
					$scope.IsVisibleDept = true;
				}
			
			console.log("doctor Consultation list",$scope.doctorConsultationDeptList);
		}
		
		
		// remove added item from doctor wise speciality list i.e. doctorConsultationList
		$scope.remove=function(index){
			$scope.doctorConsultationList.splice(index,1);
			
			if($scope.doctorConsultationList.length==0){				
				$scope.IsVisibleDoc = false;
			}
			console.log("list after remove",$scope.doctorConsultationList);
		}
		// remove added item from dept  wise speciality list i.e. doctorConsultationDeptList
		$scope.removeDept=function(index){
			$scope.doctorConsultationDeptList.splice(index,1);
			
			if($scope.doctorConsultationDeptList.length==0){
				$scope.IsVisibleDept = false;
			}
			console.log("list after remove",$scope.doctorConsultationDeptList);
		}
		
		$scope.updateStatus=function(id,type){
			$scope.currentDate = new Date();
			$scope.currentDate = moment($scope.currentDate)
					.format('DD-MM-YYYY HH:mm:ss');

			type = (type == "A") ? "A" : "I";

			var data = {
				doctorConsultationServiceMapperId : id,
				status : type,
				updatedBy : $scope.userId,
				updatedDate : $scope.currentDate,
				organizationId : $scope.organizationId
			}
			console.log(data);
			var URI = BASE_URL + ROOT_URL+ "/api/billing/unit/updateDoctorConsultationServiceMapperStatus";
			GenericService.serviceAction("POST", URI, data).then(function(response) {
								if (response.data.status == "success") {
									$scope.init();
									$scope.IsVisibleDoc = false;
									$scope.IsVisibleDept = false;
									$scope.getServicesBySpecialityId();
									$scope.getSpecialityMasterList();
										$scope.initDoctorConsultationMasterList($scope.offset,$scope.noOfRecordsPerPage);
										$scope.initDoctorConsultationSpecialityList($scope.offset1,$scope.recordPerPage);
									growl.success(response.data.message,{title : 'Success!'});
								}else{
									growl.error(response.data.message,{title : 'Error!'});
									$scope.init();
									$scope.IsVisibleDoc = false;
									$scope.IsVisibleDept = false;
									$scope.getServicesBySpecialityId();
									$scope.getSpecialityMasterList();
										$scope.initDoctorConsultationMasterList($scope.offset,$scope.noOfRecordsPerPage);
										$scope.initDoctorConsultationSpecialityList($scope.offset1,$scope.recordPerPage);
								} 
							});
		}
		// for tab navigation
		$scope.firstTabLevel = 1 ;
		$scope.setFirstTabLevel= function(newTab){
			$scope.firstTabLevel = newTab;
			  $scope.insurenceFlag = false;
           	$scope.companyFlag = false;
           	$scope.companyId = null;
           	$scope.associateCompanyId = null;
           	$scope.companyMasterList = [];
           	$scope.associateCompanyMasterList = [];
			$scope.doctorConsultationObj={
					specialityId : 0,
					unitId : $scope.unitId,
					organizationId : $scope.organizationId,
					/*doctorId : null,*/
					newVisitServiceId : 0,
					followupVisitServiceId : 0,
					secondaryVisitServiceId : 0
			}
		};
		$scope.isSetFirstTabLevel = function(tabNum){
			return $scope.firstTabLevel === tabNum;
		};
		
		  function cartesian() {
			    var r = [], arg = arguments, max = arg.length-1;
			    function helper(arr, i) {
			        for (var j=0, l=arg[i].length; j<l; j++) {
			            var a = arr.slice(0); // clone arr
			            a.push(arg[i][j]);
			            if (i==max)
			                r.push(a);
			            else
			                helper(a, i+1);
			        }
			    }
			    helper([], 0);
			    return r;
			}
		  var isGroupExistsCap;
		  function checkGroupIdExistsOrNotCap(arr, val) {
				return arr.some(function(arr) {
						if(val.doctorId === arr.doctorId && val.paymentEntitlementId===arr.paymentEntitlementId && val.specialityId == arr.specialityId)
								{
									return true;
								}
							else
								{
									return false;
								}
						});
			}
		  
		  var isGroupExistsCapSpec;
		  function checkGroupIdExistsOrNotCapSpec(arr, val) {
				return arr.some(function(arr) {
						if(val.specialityId === arr.specialityId && val.paymentEntitlementId===arr.paymentEntitlementId)
								{
									return true;
								}
							else
								{
									return false;
								}
						});
			}
		  
		  //get Speciality tab list
		  $scope.initDoctorConsultationSpecialityList = function(offset, noOfRecordsPerPage) {
				
				try{
					$rootScope.startLoader();
					offset = offset  != null ? offset  : 0;
					noOfRecordsPerPage =noOfRecordsPerPage != null ? noOfRecordsPerPage
							: 10;
					
				    var URI = BASE_URL + ROOT_URL + API + BILLING + UNIT + GETDOCTORCONSULTATIONSERVICEMAPPERLIST;
				    
				    var URI1 = BASE_URL + ROOT_URL + API + BILLING + UNIT + "/getDoctorConsultationServiceMapperListCount";
				    
					var obj = {
								organizationId :$scope.organizationId,
								unitId         :$scope.unitId,
								offset         :offset,
								noOfRecordsPerPage :noOfRecordsPerPage,
								tabNumber :2
						    };
					
					var data1 = {
							organizationId :$scope.organizationId,
							unitId         :$scope.unitId,
							tabNumber :2
					    };
					
					
					promiseFactoryWithObject.setPromisesWithObject(URI, URI1, "POST", "POST",obj,data1)
					.then(
							function(response) {
								$rootScope.stopLoader();
								$scope.doctorConsultationSpecialityList = [];
								$scope.doctorConsultationSpecialityList = response[0].listObject;
								$scope.commonListCountForSpeciality = response[1].object;
								$scope.setPageForSpeciality(1, false);
							});
					
				}catch (e) {
					console.log("Exception",e.message);
				}
			}
			
			$scope.initDoctorConsultationSpecialityList($scope.offset1,$scope.recordPerPage);
			
				$scope.getDoctorConsultationSpecialityList = function(orgId,offset, noOfRecordsPerPage) {
					$rootScope.startLoader();
					$scope.doctorConsultationSpecialityList = [];
					offset = offset != null ? offset : 0;
					noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
							: 10;
					   var URI = BASE_URL + ROOT_URL + API + BILLING + UNIT + GETDOCTORCONSULTATIONSERVICEMAPPERLIST;
					   var obj = {
								organizationId :$scope.organizationId,
								unitId         :$scope.unitId,
								offset         :offset,
								noOfRecordsPerPage :noOfRecordsPerPage,
								tabNumber :2
						    };
					GenericService.serviceAction("POST", URI, obj)
							.then(
									function(response) {
										$rootScope.stopLoader();
										$scope.doctorConsultationSpecialityList = response.data.listObject;
									});
			}
			
				$scope.setPageForSpeciality = function(page, flag) {
				if (page < 1 || page > $scope.pager1.totalPages) {
					return;
				}
				$scope.pager1 = PagerService.GetPager(
						$scope.commonListCountForSpeciality, page,
						$scope.recordPerPage);
				if (flag) {
					$scope.getDoctorConsultationSpecialityList($scope.orgId,$scope.pager1.startIndex,$scope.pager1.pageSize);
				}
			}
			$scope.setRecordsPerPage = function() {
				$scope.initDoctorConsultationSpecialityList($scope.offset1,$scope.recordPerPage);
			};
}]);