'use strict';

/**
 * @Author By  Sidheshwar
 * @name myApp.controller:dischargedPatientsController
 * @description #dischargedPatientsController Controller of the myApp
 */

angular.module('myApp').controller('dischargedPatientsController',
	['$scope','$http','$localStorage','$sessionStorage','$cookies','$rootScope','GenericService','growl','$state','$stateParams','PagerService','reportService',
		'BillingGenericService','$window',
		function($scope, $http, $localStorage, $sessionStorage,$cookies, $rootScope, GenericService, growl,$state,$stateParams,PagerService,reportService,
				BillingGenericService, $window) {
			
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
				$scope.isUpdateDisSummary=cookieObject.updateDischargeSummary;
				$scope.unitId = cookieObject.unitId;
				$scope.organizationId = cookieObject.organizationId;
				$scope.userId = cookieObject.userId;
				$rootScope.loginpage = true;
				$scope.doctorList=[];
				$scope.admissionTypeList = [];
				$scope.dischargeTypeList = [];
				
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
			    $scope.offset = 0;
				$scope.noOfRecordsPerPage = 10;
				$scope.totalNoOfRecords;
				$scope.searchKeyword;
				$scope.commonListCount;
				
				$scope.visitTypeList=[];
				$scope.visitTypeList.push({visitTypeId:0,visitType:'All'});
				$scope.visitTypeList.push({visitTypeId:3,visitType:'Day Care'});
				$scope.visitTypeList.push({visitTypeId:4,visitType:'ER'});
				$scope.visitTypeList.push({visitTypeId:2,visitType:'IP'});
				
				$scope.searchObj={};
				$scope.searchObj.doctorId="";
				$scope.searchObj.admissionTypeId="";
				$scope.searchObj.dischargeTypeId="";
				$scope.searchObj.visitTypeId=0;
				$scope.searchObj.searchKey="";
				
				$scope.fromDate = moment().toDate();
			    $scope.toDate = moment().toDate();
				var date = new Date();
				$scope.dateOptions =
				{
					formatYear : 'yyyy',
					showWeeks : false,
					maxDate : new Date(),
					minDate : date.setDate(($scope.toDate).getDate() - 90),
				};

				
				$scope.dateOptionsChange = function()
				{
					$scope.dateOptions.minDate = moment($scope.toDate).subtract(90, 'days');
					$scope.dateOptions.maxDate =$scope.toDate;
				};

				
				$scope.open = function() {
					$scope.popup.opened = true;
				};

				$scope.popup =
				{
					opened : false
				};
				
				$scope.popup1 =
				{
					opened : false
				};
				
				$scope.open1 = function() 
				{
					$scope.popup1.opened = true;
				};
				
				
			    
				
				
				var data = {};
			
				var commonObj = {
						unitId : $scope.unitId,
						organizationId : $scope.organizationId,
						offset:$scope.offset != null ? $scope.offset : 0,
						noOfRecordsPerPage: $scope.noOfRecordsPerPage != null ? $scope.noOfRecordsPerPage : 10,
				};
												
				
			    var reqObjForDocList = {
						organizationId:$scope.organizationId,
						unitId:$scope.unitId,
						flag:true,//$("#allDoctors").prop("checked"),
						specialityId:1
				}
			   
				var URI = BASE_URL + ROOT_URL+ "/api/global/webappointment/getDoctors";
				GenericService.serviceAction("POST", URI, reqObjForDocList).then(function(response) 
					{
				    if (response.data.status == "success") {
								$scope.stopLoader();
								$scope.doctorList = response.data.listObject;
					}
				    if($scope.doctorList.length==0){
						$scope.loadingMsg="No Doctors Available";
					}
				});	
				
				var URI = BASE_URL + ROOT_URL+ GETACTIVEADMISSIONTYPELIST+"/"+$scope.organizationId;
		       GenericService.serviceAction("GET", URI, {}).then(function(response) {
							if (response.data.status == "success") {
								$scope.admissionTypeList = response.data.listObject;
								console.log("$scope.admissionTypeList",$scope.admissionTypeList);

							}
						});

				var reqDisTypeList = {
						organizationId : $scope.organizationId
				};
		   	var URI = BASE_URL + ROOT_URL+ GETACTIVEDISCHARGETYPELIST;
	     GenericService.serviceAction("POST", URI, reqDisTypeList).then(function(response) {
						if (response.data.status == "success") {
							$scope.dischargeTypeList = response.data.listObject;

						}
					});
								
			}

			$scope.init();
			/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
				$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
			};
			$scope.loadingMsg="Loading...";
			$scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage,unitId)
			{
			$scope.dischargedPatientList = [];
			$scope.loadingMsg="Loading...";
			$rootScope.startLoader();
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;		
			
			var commonObj = {
					unitId : $scope.unitId,
					organizationId : $scope.organizationId,
					offset:offset,
					noOfRecordsPerPage:noOfRecordsPerPage,
			};
					
			if($scope.searchObj.doctorId!=0 && $scope.searchObj.doctorId!=""){
				commonObj.doctorId=$scope.searchObj.doctorId;
			}
			if($scope.searchObj.dischargeTypeId!=0 && $scope.searchObj.dischargeTypeId!=""){
				commonObj.dischargeTypeId=$scope.searchObj.dischargeTypeId;
			}
			
			if($scope.searchObj.admissionTypeId!=0 && $scope.searchObj.admissionTypeId!=""){
				commonObj.admissionTypeId=$scope.searchObj.admissionTypeId;
			}
			if($scope.searchObj.searchKey!=""){
				commonObj.searchKey=($scope.searchObj.searchKey).trim();
			}
			
			commonObj.visitTypeId=$scope.searchObj.visitTypeId;
			commonObj.fromDate = $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD");
			commonObj.toDate =   $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD");
			
				var URI = BASE_URL + ROOT_URL+ "/adt/getDischargedPatientsList";
				GenericService.serviceAction("POST", URI, commonObj).then(function(response) {
					
					if (response.data.status == "success") {
						$scope.dischargedPatientList  = response.data.listObject;
						if($scope.dischargedPatientList.length==0){
							$scope.loadingMsg="No record found";
						}
						$scope.admittedPatientCount = response.data.object;
						$scope.setPage(1,false);
					}
				});
				
				console.log("list data",$scope.dischargedPatientList);
				$rootScope.stopLoader();
		}
			
		$scope.masterList = function(organizationId,offset, noOfRecordsPerPage,unitId) {
			$scope.dischargedPatientList = [];
			$scope.loadingMsg="Loading...";
			$rootScope.startLoader();
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;	
				
			var commonObj = {
					unitId : $scope.unitId,
					organizationId : $scope.organizationId,
					offset:offset,
					noOfRecordsPerPage:noOfRecordsPerPage,
			};
			
			if($scope.searchObj.doctorId!=0 && $scope.searchObj.doctorId!=""){
				commonObj.doctorId=$scope.searchObj.doctorId;
			}
			if($scope.searchObj.dischargeTypeId!=0 && $scope.searchObj.dischargeTypeId!=""){
				commonObj.dischargeTypeId=$scope.searchObj.dischargeTypeId;
			}
			
			if($scope.searchObj.admissionTypeId!=0 && $scope.searchObj.admissionTypeId!=""){
				commonObj.admissionTypeId=$scope.searchObj.admissionTypeId;
			}
			
			if($scope.searchObj.searchKey!=""){
				commonObj.searchKey=($scope.searchObj.searchKey).trim();
			}
			
				commonObj.visitTypeId=$scope.searchObj.visitTypeId;
		
				
				commonObj.fromDate = $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD");
				commonObj.toDate =   $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD");
						 
				
				
			var URI = BASE_URL + ROOT_URL+ "/adt/getDischargedPatientsList";
				GenericService.serviceAction("POST", URI, commonObj).then(function(response) {
					if (response.data.status == "success") {
						$scope.dischargedPatientList = response.data.listObject;
						if($scope.dischargedPatientList.length==0){
							$scope.loadingMsg="No record found";
						}
						$scope.admittedPatientCount = response.data.object;
					}
				});
				$rootScope.stopLoader();
				console.log("list data",$scope.dischargedPatientList);
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
			
			$scope.searchDischargedPatient=function(){
				$scope.searchObj.searchKey="";
				$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
			}
			
			$scope.searchPatients= function(searchKeyword){
				if(searchKeyword!='' && searchKeyword.length>3){
					$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
				}
			}
			
			$scope.editDischargeSummary=function(obj){
				$scope.detailObj={};
				$scope.detailObj.organizationId=$scope.organizationId;
				$scope.detailObj.unitId=$scope.unitId;
//				$scope.detailObj.specialityId=obj.specialityId;
				$scope.detailObj.dischargeSummaryId=obj.dischargeSummaryId;
				$scope.detailObj.templateId=obj.dischargeTemplateId;
				$scope.detailObj.admissionId=obj.admissionId;
				$scope.detailObj.patientId=obj.patientId;
				$scope.detailObj.doctorId=obj.doctorId;
				$scope.detailObj.genderId=obj.genderId;
				if(obj.dischargeSummaryId==null){
					/*growl.error("Discharge Summary is not created for this patient!!",
							{
								title : 'Error!'
							});
					return false;*/
					$state.go('dischargeSummary',
							{
								dischargeObj: obj
							});
				}else{
					
					$state.go('updateDischargeSummary',
							{
								dischargeSummObj: $scope.detailObj
							});
				}
				
			}
			/**List And Pagination End**/
			
			/***************Discharge Summary Report***********************/
			$scope.printDischargeSumReport=function(obj)
			{
				//$scope.save();
				$scope.dischargeSummaryId=obj.dischargeSummaryId;
				$scope.admissionId=obj.admissionId;
				if($scope.dischargeSummaryId!=null && $scope.dischargeSummaryId!=undefined){
					if(obj.isHtml!= null && obj.isHtml == 'Y'){
						$("#printHeaderReportModal").show();
					}else{
						$scope.sendDischargeSummaryToMyLife($scope.dischargeSummaryId, obj);
						var data =  
						{
					            "appName": "HIS",
					            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
					            "reportDesignFilePath": "Dischargesummary.rpt",
					            "reportFormat": "PDF",
					            "exportFileName": "pathologyReport",
					            "reportParams": [
					              {
						             "paramName": "discharge_summary_id",
						             "paramType": "String",
						             "paramValue": $scope.dischargeSummaryId
					              }
					              ,
					              {
							             "paramName": "selectedHeader",
							             "paramType": "Integer",
							             "paramValue": 1
						              }
					            ]
					      };
						reportService.generate(REPORT_API_URI1,data);

					}
					
					/*
	
					if($scope.dischargeSummaryId!=null){ 
						var elementId = document.getElementById('dischargeCardId');
					    if(elementId!=null)
					    	{
					    	 document.body.removeChild(elementId);
					    	}
					   	
					var URI = BASE_URL + ROOT_URL + "/adt/discharge/dischargeSummaryCard";
				     var form = null;
				     form = document.createElement("form");
				     form.setAttribute("id", "dischargeCardId");
				     form.setAttribute("name", "Discharge Card");
				     form.setAttribute("method", "post");
				     form.setAttribute("action", URI);
				     form.setAttribute("target", "_blank");
				     
				     
				     var hiddenField1 = document.createElement("input"); 
				     hiddenField1.setAttribute("type", "hidden");
				     hiddenField1.setAttribute("name", "admissionId");
				     hiddenField1.setAttribute("value",$scope.admissionId);
				     
				     var hiddenField2 = document.createElement("input"); 
				     hiddenField2.setAttribute("type", "hidden");
				     hiddenField2.setAttribute("name", "dischargeSummaryId");
				     hiddenField2.setAttribute("value",$scope.dischargeSummaryId);
				     
				     var hiddenField3 = document.createElement("input"); 
				     hiddenField3.setAttribute("type", "hidden");
				     hiddenField3.setAttribute("name", "orgUnitId");
				     hiddenField3.setAttribute("value",$scope.unitId);
				     
				     var hiddenField4 = document.createElement("input"); 
				     hiddenField4.setAttribute("type", "hidden");
				     hiddenField4.setAttribute("name", "orgId");
				     hiddenField4.setAttribute("value", $scope.organizationId);
				     
				     var hiddenField5 = document.createElement("input"); 
                     hiddenField5.setAttribute("type", "hidden");
                     hiddenField5.setAttribute("name", "headerType");
                     hiddenField5.setAttribute("value", $scope.selectedHeader);
                     
				     form.appendChild(hiddenField1);
				     form.appendChild(hiddenField2);
				     form.appendChild(hiddenField3);
				     form.appendChild(hiddenField4);
				     form.appendChild(hiddenField5);
				     
				     document.body.appendChild(form);
				     $("form[name='Discharge Card']").submit();
				     }
					else{
						growl.error("can not print the report",
								{
									title : 'Error!'
								});
						}
					
				
				*/}
				else{
					growl.error("Discharge Summary Not Saved",
							{
								title : 'Error!'
							});
					}
				
		}
					
			
			$scope.printDischargedReport= function(){
				if($scope.dischargeSummaryId!=null && $scope.dischargeSummaryId!=undefined){

					if($scope.dischargeSummaryId!=null){ 
						var elementId = document.getElementById('dischargeCardId');
					    if(elementId!=null)
					    	{
					    	 document.body.removeChild(elementId);
					    	}
					   	
					var URI = BASE_URL + ROOT_URL + "/adt/discharge/dischargeSummaryCard";
				     var form = null;
				     form = document.createElement("form");
				     form.setAttribute("id", "dischargeCardId");
				     form.setAttribute("name", "Discharge Card");
				     form.setAttribute("method", "post");
				     form.setAttribute("action", URI);
				     form.setAttribute("target", "_blank");
				     
				     
				     var hiddenField1 = document.createElement("input"); 
				     hiddenField1.setAttribute("type", "hidden");
				     hiddenField1.setAttribute("name", "admissionId");
				     hiddenField1.setAttribute("value",$scope.admissionId);
				     
				     var hiddenField2 = document.createElement("input"); 
				     hiddenField2.setAttribute("type", "hidden");
				     hiddenField2.setAttribute("name", "dischargeSummaryId");
				     hiddenField2.setAttribute("value",$scope.dischargeSummaryId);
				     
				     var hiddenField3 = document.createElement("input"); 
				     hiddenField3.setAttribute("type", "hidden");
				     hiddenField3.setAttribute("name", "orgUnitId");
				     hiddenField3.setAttribute("value",$scope.unitId);
				     
				     var hiddenField4 = document.createElement("input"); 
				     hiddenField4.setAttribute("type", "hidden");
				     hiddenField4.setAttribute("name", "orgId");
				     hiddenField4.setAttribute("value", $scope.organizationId);
				     
				     var hiddenField5 = document.createElement("input"); 
                     hiddenField5.setAttribute("type", "hidden");
                     hiddenField5.setAttribute("name", "headerType");
                     hiddenField5.setAttribute("value", $scope.selectedHeader);
                     
				     form.appendChild(hiddenField1);
				     form.appendChild(hiddenField2);
				     form.appendChild(hiddenField3);
				     form.appendChild(hiddenField4);
				     form.appendChild(hiddenField5);
				     
				     document.body.appendChild(form);
				     $("form[name='Discharge Card']").submit();
				     $("#printHeaderReportModal").hide();
				     }
					else{
						growl.error("can not print the report",
								{
									title : 'Error!'
								});
						}
					
				
				}
				else{
					growl.error("can not print the report",
							{
								title : 'Error!'
							});
					}
				
			}
			$scope.hideModel=function(){
				$("#printHeaderReportModal").hide();
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

			$scope.sendDischargeSummaryToMyLife = function(dischargeSummaryId, obj){
				var data = {};
				data.patientId = obj.patientId;
				data.organizationId = $scope.organizationId;
				data.unitId = $scope.unitId;
				data.dischargeSummaryId = dischargeSummaryId;

				var URI = BASE_URL + ROOT_URL+ "/adt/discharge/sendDischargeSummaryReportToMyLife";
				GenericService.serviceAction("POST", URI, data).then(function(response) {
					if (response.data.status == "success") {
						$scope.dischargedPatientList = response.data.listObject;
						if($scope.dischargedPatientList.length==0){
							$scope.loadingMsg="No record found";
						}
						$scope.admittedPatientCount = response.data.object;
					}
				});
				$rootScope.stopLoader();
			}
			
					} ]);
