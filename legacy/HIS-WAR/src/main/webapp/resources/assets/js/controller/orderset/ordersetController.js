(function(){
	'use strict';
	
	function ordersetController($scope, $rootScope, BillingGenericService, growl, $cookies, $state,$stateParams, dataService,$location,GenericServiceParamHeader,promiseFactoryWithObject){
		var self = this;
		var labConfig  =    $scope.checkLabConfig();
		console.log("redirected data##############", $stateParams.obj);
		function init(){
			$rootScope.loginpage = true;
			self.model = {};
			self.model.saveBtnFlag = true;
			self.model.updateBtnFlag = false;
			self.model.updateScope = false;
			self.model.orderSetActiveTab = 1;
			self.model.editMedicatonFlag = false;
		    self.model.editInvestFlag = false;
			var cookieObject = {}; var cookieObjectAppConfig = {};
			[cookieObject, cookieObjectAppConfig] = $rootScope.checkCookies();
			self.model.userId = cookieObject.userId;
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			if(cookieObjectAppConfig == undefined){
				$state.go('login');
				return;
			}
			self.model.unitId = cookieObject.unitId;
			self.model.orgId = cookieObject.organizationId;
			self.model.userId = cookieObject.userId;
			self.model.diag = {diagnosisName:"",diagnosisId:""};
			self.model.icpcObj = {icpcDesc:"",icpcId:""};
			self.model.orderset = {ordersetName:"",ordersetTypeId:1,specialityId:null,doctorId:"",status:'A',createdBy:self.model.userId,diagnosisId:"",orgId:self.model.orgId,unitId:self.model.unitId, isDeleted:'N'}
			self.model.investigation = {serviceId:null,specialityId:null,subSpecilaityId:null,orgId:self.model.orgId,
					unitId:self.model.unitId,createdBy:self.model.userId, supSpeciality:"", speciality:"",service:"" };
			self.model.inv = {specialityId:null,subSpecialityId:null,serviceId:null,invSpeciality:"",invSubSpeciality:"",service:""};
			self.model.medication = 
			    {itemMasterId:null,
					brand:"",
					genericName:"",
					dose:"",
					routeId:null,
					doseUnitId:null ,
					frequencyId:null,
					genericId: "",
					formulationTypeId:"",
					frequency:"",
					instructionId:null,
					instruction:"",
					orgId:self.model.orgId,
					unitId:self.model.unitId,
					createdBy:self.model.userId
					};
		
			self.model.medicationEditObj = [];
			self.model.investigationEditObj = [];
			self.model.formulationTypeId;
			self.model.genericId;
			self.model.specialityList = [];
			self.model.investigations = [];
			self.model.medications = [];
			self.model.doseList = [];
			self.model.raoList = [];
			self.model.editedDataList = [];
			$scope.medicationInstructions = [];
			
			self.model.diagnosisSearch = diagnosisSearch;
			self.model.selectDiagnosis = selectDiagnosis;
			self.model.getSpecialityList = getSpecialityList;
			self.model.getDoseList = getDoseList;
			self.model.selectDepartment = selectDepartment;
			self.model.searchDoctorBySpeciality = searchDoctorBySpeciality;
			self.model.selectDoctor = selectDoctor;
			self.model.clearDeptDoc = clearDeptDoc;
			self.model.selectInvSpeciality = selectInvSpeciality;
			self.model.getSubSpecialityBySpecialityId = getSubSpecialityBySpecialityId;
			self.model.selectInvSubSpecialityId = selectInvSubSpecialityId;
			self.model.searchInvestigations = searchInvestigations;
			self.model.selectInvestigation = selectInvestigation;
			self.model.addInvestigation = addInvestigation;
			self.model.removeInvestigation = removeInvestigation;
			self.model.searchMedication = searchMedication;
			self.model.selectMed = selectMed;
			self.model.searchFrequency = searchFrequency;
			self.model.selectFrequency = selectFrequency;
			self.model.getInstrunctionList = getInstrunctionList;
			self.model.addMedication = addMedication;
			self.model.removeMedication = removeMedication;
			self.model.saveOrderSet = saveOrderSet;
			self.model.updateOrderSet = updateOrderSet;
			self.model.detailedOrderSet=detailedOrderSet;
			self.model.editOrderSetMediction = editOrderSetMediction;
			self.model.editOrderSetInvestigation = editOrderSetInvestigation;
			
//			self.model.getSpecialityList();
//			self.model.getInstrunctionList();
//			self.model.getDoseList();
			self.model.getSpecialityList();
			self.model.getInstrunctionList();
			self.model.getDoseList();
			
			self.model.investigations = [];
			self.model.medications = [];
			/**Redirect Page Code*/
			var detailedOrderSet={};
			if ($stateParams.obj && !angular.equals({}, $stateParams.obj) && $stateParams.obj != 1 && $stateParams.obj != 2)
				{
				    self.model.saveBtnFlag = false;
					self.model.updateBtnFlag = true;
					self.model.updateScope = true;
				detailedOrderSet = $stateParams.obj;
				self.model.diagEditObj=detailedOrderSet.ordersetMaster;
				self.model.orderSetActiveTab=detailedOrderSet.ordersetMaster.orgId;
				self.model.orderset.ordersetMasterId=self.model.diagEditObj.ordersetMasterId;
				self.model.diag.diagnosisName = self.model.diagEditObj.diagnosisName;
				self.model.diag.diagnosisId = self.model.diagEditObj.diagnosisId;
				self.model.icpcObj.icpcDesc = self.model.diagEditObj.icpcDesc;
				self.model.icpcObj.icpcId = self.model.diagEditObj.icpcId;
				self.model.orderset.diagnosisId = self.model.diagEditObj.diagnosisId;
				self.model.orderset.ordersetName = self.model.diagEditObj.ordersetName;
				self.model.orderset.ordersetTypeId = self.model.diagEditObj.ordersetTypeId;
				self.model.speciality= self.model.diagEditObj.specialityName;
				self.model.specialityId = self.model.diagEditObj.specialityId;
				self.model.orderset.specialityId = self.model.diagEditObj.specialityId;
				self.model.doctor = self.model.diagEditObj.doctorName;
				self.model.doctorId = self.model.diagEditObj.doctorId;
				self.model.orderset.doctorId=self.model.diagEditObj.doctorId;
				self.model.medicationEditObj = detailedOrderSet.ordersetMedications;
				 angular.forEach(self.model.medicationEditObj, function (value,key) {
					 var newArr={
							 "brand":value.itemName,
							 "genericName":value.genericName,
							 "dose":value.dose,
							 "frequency":value.frequencyDesc,
							 "instruction":value.instructionDesc,
							 "frequencyId":value.frequencyId,
							 "itemMasterId":value.itemMasterId,
							 "doseUnitId":value.doseUnitId,
							 "strengthDesc":value.strengthDesc,
							 "unit":value.strengthDesc,
							 "instructionId":value.instructionId,
							 "genericId":value.genericId,
							 "routeId":value.routeId,
							 "orgId":self.model.orgId,
							"unitId":self.model.unitId,
							"createdBy":self.model.userId,
							"formulationTypeId":value.formulationTypeId
					 };
//					 self.model.editMedicatonFlag = true;
					 self.model.medications.push(newArr);
	             });
				 console.log("medication data", self.model.medications);
				 
				 self.model.investigationEditObj = detailedOrderSet.ordersetInvestigations;
				 angular.forEach(self.model.investigationEditObj,function(value, key){
					 var newArr={
							 "speciality":value.specialityName,
							 "supSpeciality":value.subSpecialityName,
							 "service":value.serviceStandardName,
							 "specialityId":value.specialityId,
							 "subSpecialityId":value.subSpecialityId,
							 "serviceId":value.serviceId,
							 "orgId":self.model.orgId,
							"unitId":self.model.unitId,
							"createdBy":self.model.userId
					 };
//					 self.model.editInvestFlag = true;
					 self.model.investigations.push(newArr);
				 });

				 
				}
				else if( $stateParams.obj && ($stateParams.obj == 1 || $stateParams.obj == 2) ){
                        if ($stateParams.obj !== undefined && $stateParams.obj !== null) {
                            self.model.orderSetActiveTab = Number($stateParams.obj);
                        } else {
                            self.model.orderSetActiveTab = 1;
                        }
				}
		}
		
//		init();
		
		
		var diagnosisSearch = function(diagnosisName){
			var headers = {unit_id:self.model.unitId,organization_id:self.model.orgId};
			var URI = BASE_URL + ROOT_URL + "/api/opd/diagnosisAutoFillSearch?diagnosisName="+diagnosisName;
			return GenericServiceParamHeader.serviceAction("GET",URI, null,NOTIFICATION_MSG_STATUS_FALSE,null,headers).then(function(response) {
				if (response.data.status == "success")
					return response.data.listObject;
				
			});
		}
		
		var selectDiagnosis = function(item, model, label){
			if(self.model.orderSetActiveTab==1){
                self.model.diag.diagnosisId = item.diagnosisId;
                self.model.orderset.diagnosisId = item.diagnosisId;
			}else{
                self.model.icpcObj.icpcDesc=item.icpcDesc;
                self.model.icpcObj.icpcId=item.icpcId;
                self.model.orderset.icpcId = item.icpcId;
			}
		}
		
	  //get speciality list for autocomplete
	  var getSpecialityList = function(){
		  var URI = BASE_URL + ROOT_URL + "/adt/getActiveSpecialityList";
		  var param = {organizationId:self.model.orgId,unitId:self.model.unitId};
		    BillingGenericService.serviceAction("POST", URI, param).then(function(response){
		    	if(response.data.status == "success")
		    		self.model.specialityList = response.data.listObject;
		    });
	  }
	  
	 
	  
	  //get speciality list for autocomplete
	  var getDoseList = function(){
		  var URI =BASE_URL + ROOT_URL +"/api/globals/strength/units/"+self.model.orgId;
		    BillingGenericService.serviceAction("GET", URI, {}).then(function(response){
		    		self.model.doseList = response.data;
		    });
	  }
	 
	  var selectDepartment = function(item, model, label){
		  self.model.orderset.specialityId = item.specialityId
	  }
		
	  var searchDoctorBySpeciality = function(){
		  if(self.model.orderset.specialityId != null && self.model.orderset.specialityId != "" && typeof(self.model.orderset.specialityId) != "undefined"){
			  var URI = BASE_URL + ROOT_URL + "/adt/getDoctorListAganistSpeciality";
			  var param = {specialityId:self.model.orderset.specialityId,unitId:self.model.unitId,organizationId:self.model.orgId};
			  return BillingGenericService.serviceAction("POST", URI, param).then(function(response){
			    	if(response.data.status == "success")
			    		return response.data.listObject;
			    });
		  }else{
			  growl.error("Please select a department",{
				  title : ERROR_MSG
			  })
		  }
	  }
	  
	  var selectDoctor = function(item, model, label){
		  self.model.orderset.doctorId = item.doctorId;
	  }
	  
	  var clearDeptDoc = function(){
		  if(self.model.orderset.ordersetTypeId == 1){
			  self.model.orderset.doctorId = null;
			  self.model.orderset.specialityId = null;
			  self.model.doctor = "";
			  self.model.speciality = "";
		  }
	  }
	  
	  //Investigations section
	  var selectInvSpeciality = function(item, model, label){
		  self.model.inv.specialityId = item.specialityId;
		  self.model.investigation.specialityId = item.specialityId
		  self.model.inv.invSubSpeciality = "";
		  self.model.inv.subSpecialityId = null;
	  }
	  
	  var getSubSpecialityBySpecialityId = function(){
		  if(self.model.inv.specialityId != null && self.model.inv.specialityId != "" && typeof(self.model.inv.specialityId) != "undefined"){
			  var URI = BASE_URL + ROOT_URL + "/adt/subspeciality/orgId/"+self.model.orgId+"/unitId/"+self.model.unitId+"/specialityId/"+self.model.inv.specialityId;
			return BillingGenericService.serviceAction(METHOD_GET,URI, null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success")
						return response.data.listObject;
			});
		  }else{
			  growl.error("Please select a Specialty",{
				  title : ERROR_MSG
			  })
		  }
	  }
	  
	  var selectInvSubSpecialityId = function(item, model, label){
		  self.model.inv.subSpecialityId = item.subSpecialityMasterId;
		  self.model.investigation.subSpecialityId = item.subSpecialityMasterId;
	  }
	  
	  var searchInvestigations = function(keyword){
//		  if(self.model.inv.specialityId != null && self.model.inv.specialityId != null){
			  var URI = BASE_URL + ROOT_URL + "/api/packages/services/orderSet/search";
			  var searchObj = {serviceDescription:keyword, groupId:self.model.inv.specialityId, subGroupId:self.model.inv.subSpecialityId,orgId:self.model.orgId, unitId:self.model.unitId};
			  return BillingGenericService.serviceAction(METHOD_POST,URI, searchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success")
						return response.data.listObject;
				});
//		  }else{
//			  growl.error("Please select a Specialty and Sub-Specialty",{
//				  title : ERROR_MSG
//			  })
//		  }
	  }
	  
		
	    $scope.checkServiceMappedWithTest = function(serviceId,specialityId)
		{
			     $rootScope.startLoader();
			     var labSpecialityId = 0;
			     if(specialityId==labConfig.labSpecialityId)
					{
			    	   labSpecialityId = specialityId;
					}
				var URI = BASE_URL + ROOT_URL + "/global/serviceMaster/checkService/"+labSpecialityId;
				var data = {serviceMasterId:serviceId,orgId:$scope.organizationId,unitId:$scope.unitId};
			    return BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
				{
					if(response.data.status == "success")
					{
						$rootScope.stopLoader();
						return response.data.object;
					}
					else if(response.data.status == "error"){
						  $rootScope.stopLoader();
						  return response.data.object;
					   }
				 });
		}
	  
	  var selectInvestigation = function(item, model, label)
	  {
		  
		  console.log("selected item" ,item);
		  var promise = $scope.checkServiceMappedWithTest(item.serviceMasterId,item.specialityId);
  		promise.then(function(obj) 
  				{
      	             if(obj>0)
      	            	 {
		                   self.model.inv.serviceId = item.serviceMasterId;
		                   self.model.inv.invSpeciality = item.groupDesc;
	                	   self.model.inv.invSubSpeciality = item.subGroup;
	                	   self.model.inv.specialityId =  item.specialityId ;
	                	   self.model.inv.subSpecialityId =  item.subSpecialityMasterId;
      	            	 }
		                   else{
		                	   self.model.inv.serviceId = item.serviceMasterId;
		                	   self.model.inv.invSpeciality = item.groupDesc;
		                	   self.model.inv.invSubSpeciality = item.subGroup;
		                	   self.model.inv.specialityId =  item.specialityId ;
		                	   self.model.inv.subSpecialityId =  item.subSpecialityMasterId; 
		       	            	 growl.warning(
		       	 						'Service is Not mapped with Test!!',
		       	 						{
		       	 							title : 'Warning!'
		       	 						});
       	                 }
   				 });
	  }
	  
	  var addInvestigation = function()
	  {
		  
		  if(!self.model.editInvestFlag){
			  if(self.model.inv.specialityId != null && self.model.inv.subSpecialityId != null && self.model.inv.serviceId != null)
			  {
				  let temp = {serviceId:self.model.inv.serviceId,orgId:self.model.orgId,unitId:self.model.unitId,createdBy:self.model.userId,speciality:self.model.inv.invSpeciality, 
						  	supSpeciality:self.model.inv.invSubSpeciality, service:self.model.inv.service,specialityId:self.model.inv.specialityId,subSpecialityId:self.model.inv.subSpecialityId};
				  self.model.investigations.push(temp);
				  self.model.editInvestFlag = false;
				  self.model.inv = {specialityId:null,subSpecialityId:null,serviceId:null,invSpeciality:"",invSubSpeciality:"",service:""};
			  }else{
				  growl.error("Please select an Investigation",{
					  title : ERROR_MSG
				  })
			  } 
		  }else{
			  if(self.model.inv.specialityId != null && self.model.inv.subSpecialityId != null && self.model.inv.serviceId != null)
			  {
				  let temp = {serviceId:self.model.inv.serviceId,orgId:self.model.orgId,unitId:self.model.unitId,createdBy:self.model.userId,speciality:self.model.inv.invSpeciality, 
						  	supSpeciality:self.model.inv.invSubSpeciality, service:self.model.inv.service,specialityId:self.model.inv.specialityId,subSpecialityId:self.model.inv.subSpecialityId};
				  
				  self.model.investigations.splice(self.model.indexeInv,1);
				  self.model.investigations.push(temp);
				  self.model.editInvestFlag = false;
				  self.model.inv = {specialityId:null,subSpecialityId:null,serviceId:null,invSpeciality:"",invSubSpeciality:"",service:""};
			  }else{
				  growl.error("Please select an Investigation",{
					  title : ERROR_MSG
				  })
			  } 
		  }
	  }
	  
	  var removeInvestigation = function(index){
		  self.model.investigations.splice(index,1);
	  }
	  var editOrderSetInvestigation=function(index){
		    self.model.indexeInv = index; 
		    self.model.editInvestFlag = true;
		   
/*		  if(self.model.investigationEditObj.length > 0){
			  
			  self.model.editInvestFlag = true;
			    self.model.saveBtnFlag = false;
				self.model.updateBtnFlag = true;
				self.model.updateScope = true;
			  self.model.inv.invSpeciality = self.model.investigationEditObj[index].specialityName; 
	  		  self.model.inv.invSubSpeciality = self.model.investigationEditObj[index].subSpecialityName; 
	          self.model.inv.service = self.model.investigationEditObj[index].serviceStandardName; 
	          self.model.inv.specialityId = self.model.investigationEditObj[index].specialityId; 
	          self.model.inv.subSpecialityId = self.model.investigationEditObj[index].subSpecialityId; 
	          self.model.inv.serviceId = self.model.investigationEditObj[index].serviceId;
		  }else{*/
			  
			   self.model.editInvestFlag = true;
//			   self.model.saveBtnFlag = true;
//			   self.model.updateBtnFlag = false;
//			   self.model.updateScope = false;
			  self.model.inv.invSpeciality = self.model.investigations[index].speciality; 
	  		  self.model.inv.invSubSpeciality = self.model.investigations[index].supSpeciality; 
	          self.model.inv.service = self.model.investigations[index].service; 
	          self.model.inv.specialityId = self.model.investigations[index].specialityId; 
	          self.model.inv.subSpecialityId = self.model.investigations[index].subSpecialityId; 
	          self.model.inv.serviceId = self.model.investigations[index].serviceId;
//		  }
			  
	  }
	  //Medication section
	  var searchMedication = function(name){
		  var regex=/[^a-z]/gi;
		  name=name.replace(regex," ");
			
		  var URI = BASE_URL + ROOT_URL + "/api/opd/getDrugList?unitId="+self.model.unitId+"&orgId="+self.model.orgId+"&searchDrug="+name;
		  return BillingGenericService.serviceAction(METHOD_GET,URI, null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success")
					return response.data.listObject;
			});
	  }
	  
	  var selectMed = function(item, model, label){
		  self.model.medication.genericName = item.genericName;
		  self.model.medication.genericName = item.genericName;
		  self.model.medication.itemMasterId = item.drugId;
		  self.model.formulationTypeId = item.formulationTypeId;
		  self.model.genericId = item.genericId;
		  self.model.medication.formulationTypeId = item.formulationTypeId;
		  self.model.medication.genericId = item.genericId;
		  
		  
			var URI = BASE_URL + ROOT_URL + "/api/opd/getRoaList" + "?unitId=" + 
			self.model.unitId + "&orgId=" + self.model.orgId + "&formulationTypeId=" + self.model.formulationTypeId+ "&genericId=" + self.model.genericId+ "&patientId=" +"";
		
			GenericServiceParamHeader.serviceAction(METHOD_GET, URI).then(function(response) {
			if(response.data.status == "success"){
				$rootScope.stopLoader();
				 self.model.raoList = response.data.listObject;
			  }
			});	
				
	  }
	  
	  var searchFrequency = function(name){
		  var URI = BASE_URL + ROOT_URL + "/api/opd/getFrequencyList?unitId="+self.model.unitId+"&orgId="+self.model.orgId+"&searchPar="+name;
		  return BillingGenericService.serviceAction(METHOD_GET,URI, null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success")
					return response.data.listObject;
			});
	  }
	  
	  var selectFrequency = function(item, model, label){
		  self.model.medication.frequencyId = item.frequency_id;
	  }
	  
	  var getInstrunctionList = function(){
			var commonObj = {
  				isMedication : 'Y',
					organizationId : self.model.orgId
				}
				var URI = BASE_URL + ROOT_URL+ GETMEDICATIONINSTRUCTIONS;
				BillingGenericService
						.serviceAction(METHOD_POST, URI,
								commonObj,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) 
								{
									console.log(response);
									if (response.data.status == "success")
										{
										  $scope.medicationInstructions = response.data.listObject;
										}
								});
	  }
	  
	
	  
	  var addMedication = function()
	  {
		  
		  if(!self.model.editMedicatonFlag){
			  let temp = angular.copy(self.model.medication);
			  console.log("addItemMedication",self.model.medication);
			  if(self.model.medication.itemMasterId != null  && self.model.medication.dose != ""
				  && self.model.medication.doseUnitId != null){
				 
				  var instructionList = $scope.medicationInstructions.filter(function(item){ 
					  return item.instructionId== self.model.medication.instructionId})
					  
				   var doseListForDesc = self.model.doseList.filter(function(item){ 
					  return item.id == parseInt(self.model.medication.doseUnitId)})
					  
					  
					   if(self.model.medication.instructionId != null){
						   temp.instruction = instructionList[0].instructionDesc;  
					   }
				  
				  if(self.model.medication.doseUnitId != null){
					   temp.unit = doseListForDesc[0].unit;  
				   }
				  
				  self.model.medications.push(temp);
				  self.model.editMedicatonFlag = false;
				  self.model.medication = {itemMasterId:null,brand:"",genericName:"",dose:"",frequencyId:null,frequency:"",instructionId:null,instruction:"",orgId:self.model.orgId,unitId:self.model.unitId,createdBy:self.model.userId};
			  }else{
				  growl.error("Please enter all mandatory fields.",{
					  title : ERROR_MSG
				  })
			  }
		  }else{
			  let temp = angular.copy(self.model.medication);
			  if(self.model.medication.itemMasterId != null  && self.model.medication.dose != ""
				  && self.model.medication.doseUnitId != null){
				 
				  var instructionList = $scope.medicationInstructions.filter(function(item){ return item.instructionId== self.model.medication.instructionId})
				 if(self.model.medication.instructionId != null){
					   temp.instruction = instructionList[0].instructionDesc;  
				   }
				  
				  var doseListForDesc = self.model.doseList.filter(function(item){ 
					  return item.id == parseInt(self.model.medication.doseUnitId)})
					  
					  if(self.model.medication.doseUnitId != null){
						   temp.unit = doseListForDesc[0].unit;  
					   }  
				  self.model.medications.splice(self.model.indexes,1);
				  self.model.list=[];
				  self.model.list.push(temp);
				  self.model.medications.push(temp);
				  self.model.editMedicatonFlag = false;
				  self.model.medication = {itemMasterId:null,brand:"",genericName:"",dose:"",frequencyId:null,frequency:"",instructionId:null,instruction:"",orgId:self.model.orgId,unitId:self.model.unitId,createdBy:self.model.userId};
			  }else{
				  growl.error("Please enter all mandatory fields.",{
					  title : ERROR_MSG
				  })
			  }
		  }
		
	  }
	  
	  var removeMedication = function(index){
		  self.model.medications.splice(index,1);
	  }
	  
	  var editOrderSetMediction=function(index){
		  self.model.indexes=index; 
		  self.model.editMedicatonFlag = true;
		  
	/*		if(self.model.medicationEditObj.length > 0){
				self.model.saveBtnFlag = false;
				self.model.updateBtnFlag = true;
				self.model.updateScope = true;
					if(!angular.equals({}, self.model.medicationEditObj)){
						itemMasterId
					  self.model.medication.itemMasterId=self.model.medications[index].itemMasterId;
						angular.forEach(self.model.medicationEditObj,function(value,key){
							var found = self.model.medications.find(ar=>ar.itemMasterId==value.itemMasterId);
							   if(found==undefined)
								   {
								   self.model.editMedicatonFlag = false;
								   }else{
									   self.model.editMedicatonFlag = true;
								   }
						})
						
					  self.model.medication.frequencyId=self.model.medications[index].frequencyId;
					  self.model.medication.genericId=self.model.medications[index].genericId;
					  self.model.medication.instructionId=self.model.medications[index].instructionId;
					  self.model.medication.brand=self.model.medications[index].brand;
				  	  self.model.medication.genericName=self.model.medications[index].genericName;
				  	  self.model.medication.dose=self.model.medications[index].dose;
				  	  self.model.medication.frequency=self.model.medications[index].frequency;
				  	  self.model.medication.instruction=self.model.medications[index].instructionDesc;
				  	  self.model.medication.doseUnitId=self.model.medications[index].doseUnitId.toString();
				  	  self.model.medication.routeId=self.model.medications[index].routeId;
		
		
				  	var URI = BASE_URL + ROOT_URL + "/api/opd/getRoaList" + "?unitId=" + 
					self.model.unitId + "&orgId=" + self.model.orgId + "&formulationTypeId=" + self.model.medications[index].formulationTypeId+ "&genericId=" 
					+ self.model.medications[index].genericId+ "&patientId=" +"";
				
					GenericServiceParamHeader.serviceAction(METHOD_GET, URI).then(function(response) {
					if(response.data.status == "success"){
						$rootScope.stopLoader();
						 self.model.raoList = response.data.listObject;
					  }
					});	
					}
			}else{*/
				  self.model.medication.itemMasterId=self.model.medications[index].itemMasterId;
				  self.model.medication.frequencyId=self.model.medications[index].frequencyId;
				  self.model.medication.formulationTypeId=self.model.medications[index].formulationTypeId;
				  self.model.medication.genericId=self.model.medications[index].genericId;
				  self.model.medication.instructionId=self.model.medications[index].instructionId;
				  self.model.medication.brand=self.model.medications[index].itemName;
			  	  self.model.medication.genericName=self.model.medications[index].genericName;
			  	  self.model.medication.dose=self.model.medications[index].dose;
			  	  self.model.medication.frequency=self.model.medications[index].frequencyDesc;
			  	  self.model.medication.instruction=self.model.medications[index].instructionDesc;
				  self.model.medication.routeId=self.model.medications[index].routeId;
				  self.model.medication.doseUnitId=self.model.medications[index].doseUnitId;
				  self.model.medication.frequency=self.model.medications[index].frequency;
				  self.model.medication.brand=self.model.medications[index].brand;

			  	var URI = BASE_URL + ROOT_URL + "/api/opd/getRoaList" + "?unitId=" + 
				self.model.unitId + "&orgId=" + self.model.orgId + "&formulationTypeId=" + self.model.medications[index].formulationTypeId+ "&genericId=" 
				+ self.model.medications[index].genericId+ "&patientId=" +"";
			
				GenericServiceParamHeader.serviceAction(METHOD_GET, URI).then(function(response) {
				if(response.data.status == "success"){
					$rootScope.stopLoader();
					 self.model.raoList = response.data.listObject;
				  }
				});	
			}
//	  }
	  
	  
	  var saveOrderSet = function(){
		  
		  if( (self.model.diag.diagnosisName == "" && self.model.icpcObj.icpcDesc == "") || self.model.orderset.ordersetName == "" ){
			  growl.error("Please add details",
						 {
					 title : 'Error!'
						 });
				 $rootScope.stopLoader();
				 return false;
		  }
		  if(self.model.orderset.ordersetTypeId == 2){
			  if(self.model.speciality == undefined || self.model.doctor == undefined){
				  growl.error("Please add details",
							 {
						 title : 'Error!'
							 });
					 $rootScope.stopLoader();
					 return false;
			  }
		  }
		  var medications = angular.copy(self.model.medications);
		  medications.forEach((elem) => {elem.multidelete("brand","frequency","genericName","instruction")});
		  
		  var investigations = angular.copy(self.model.investigations);
		  investigations.forEach((elem) => {elem.multidelete("service","speciality","supSpeciality")});
		  
		  var param = {ordersetMaster:self.model.orderset,ordersetMedications:medications,ordersetInvestigations:investigations};
		  
		  console.log('param',JSON.stringify(param));
		  var URI = BASE_URL + ROOT_URL + "/api/orderset/create";
		  $rootScope.startLoader();
		  BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
				if (response.data.status == "success")
					init();
				self.model.orderset={};
				self.model.doctor="";
				self.model.orderset.ordersetName="";
				self.model.orderset.speciality="";
				self.model.orderset.doctor="";
				self.model.inv={};
				self.model.medication={};
				self.model.investigations = [];
				self.model.medications = [];
				self.model.diag={};
				$state.go('orderSetMaster');
				$rootScope.stopLoader();
			});
	  }
	  
	  var updateOrderSet = function(){
	      self.model.orderset.orderSetActiveTab = self.model.orderSetActiveTab;
	      self.model.orderset.icpcId = self.model.icpcObj.icpcId;
		  var medications = angular.copy(self.model.medications);
		  medications.forEach((elem) => {elem.multidelete("brand","frequency","genericName","instruction")});
		  
		  var investigations = angular.copy(self.model.investigations);
		  investigations.forEach((elem) => {elem.multidelete("service","speciality","supSpeciality")});
		  
		  var param = {ordersetMaster:self.model.orderset,ordersetMedications:medications,ordersetInvestigations:investigations};
		  
		  console.log('param',JSON.stringify(param));
		  $rootScope.startLoader();
		  var URI = BASE_URL + ROOT_URL + "/api/orderset/updateOrderSetsDetails";
		  
		  BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
				if (response.data.status == "success")
					init();
				self.model.orderset.ordersetName="";
				self.model.orderset.speciality="";
				self.model.orderset.doctor="";
				self.model.inv={};
				self.model.medication={};
				self.model.investigations = [];
				self.model.medications = [];
				self.model.diag={};
				$state.go('orderSetMaster');
				$rootScope.stopLoader();
				  self.model.editMedicatonFlag = false;
			});
	  }


	  							 $scope.searchICPCBySystemId = function(searchKeyword,index){
      						        	$scope.icpcMasterList=[];
      						        	$scope.searchKeywordCodeIndex = index;
      						        	//var icpcSystemId = $scope.dataListComplaints[index].icpcSystemId;
      						        	//var icpcId = $scope.dataListComplaints[index].diagnosisId;
      									var data={
      						        			searchICPC:searchKeyword
      						        	};
      									var URI = BASE_URL + ROOT_URL + "/api/opd/getMasterListOfICPCSystemForCoversheet"

      						    		return BillingGenericService.serviceAction("POST",URI,data).then(function(response){
      						    			if(response.data.status == "success"){
      						    				$scope.icpcMasterList = response.data.listObject;
      						    				if($scope.icpcMasterList.length == 0){
      						    					$scope.icpcId = 0;
      						    					growl.warning("No complaint found with the selected ICPC system",
      						    							{
      						    							title : 'Warning!'
      						    							});
      						    							$rootScope.stopLoader();
      						    							return false;
      						    				}
      						    			}
      						    			return $scope.icpcMasterList;
      						    		});
      						    	}

	  
	  init();
	}
	
	function config($stateProvider){
		$stateProvider.state('addNewOrderSetMaster', {
			 url: '/addNewOrderSetMaster',
			 templateUrl: 'views/master/UnitMaster/organizationMaster/add-new-orderset-master.html',
			 controller: "Orderset.Controller",
			 controllerAs : "vm",
			 title: 'Order Set Master',
			  params: {
			        obj: {}
			    }
	          
			})
	}
	
	angular.module("myApp").config(config).controller("Orderset.Controller",ordersetController);
})();