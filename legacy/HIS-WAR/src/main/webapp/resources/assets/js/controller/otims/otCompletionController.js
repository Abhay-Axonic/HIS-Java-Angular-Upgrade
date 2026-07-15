(function () {
	'use strict';

	function otCompletionController($scope, $rootScope, GenericService,
		BillingGenericService, growl, $cookies, dataService, $state, $http,
		$stateParams,$timeout ,PaginationService,reportService,$window) {
		var self = this;

		function init() {
			$rootScope.loginpage = true;
			self.model = {
				searchObj: {
					fromDate: new Date(),
					toDate: new Date(),
					fromDateOptions: {
						formatYear: 'yyyy',
						showWeeks: false
						//maxDate: new Date()
					},
					toDateOptions: {
						formatYear: 'yyyy',
						showWeeks: false
						//maxDate: new Date()
					},
					visitTypeId:"2"
				},
			
			 pagination: {
                 currentPage: 1,
                 totalPages: 0,
                 pageSize: '10',
                 search: '',
                 searchData: PaginationService.searchData,
                 setTotalPages: PaginationService.setTotalPages,
                 getPagesList: PaginationService.getPagesList,
                 setPage: PaginationService.setPage,
                 setFirstPage: PaginationService.setFirstPage,
                 setPreviousPage: PaginationService.setPreviousPage,
                 setNextPage: PaginationService.setNextPage,
                 setLastPage: PaginationService.setLastPage,
                 sortBy: PaginationService.sortBy
               }
			};
			var cookieObject = $cookies.getObject('cookieObject');
			if (cookieObject == undefined) {
				$state.go('login');
				return;
			}
			var cookieObjectAppConfig = $cookies
				.getObject('cookieObjectAppConfig');
			if (cookieObjectAppConfig == undefined) {
				$state.go('login');
				return;
			}
			self.model.searchObj.unitId = cookieObject.unitId;
			self.model.searchObj.orgId = cookieObject.organizationId;
			self.model.userId = cookieObject.userId;
			self.model.searchOtBill = searchOtBill;
			self.model.OpenFromDate = OpenFromDate;
			self.model.OpenToDate = OpenToDate;
			self.model.setProcedureOrderId = setProcedureOrderId;
			console.log("self.model.searchObj.visitTypeId",self.model.searchObj.visitTypeId);
		}

		var OpenFromDate = function () {
			self.model.fromDateOpened = true;
			self.model.searchObj.toDateOptions.minDate = self.model.fromDate;
		}

		var OpenToDate = function () {
			self.model.toDateOpened = true;
			self.model.searchObj.toDateOptions.minDate = self.model.fromDate;
		}


		var searchOtBill = function () {
			var URIservice = BASE_URL + ROOT_URL + "/api/procedure/getprocedureOrderbyVisitandDate";
			var dataservice = {
				fromDate: moment(self.model.searchObj.fromDate).format('YYYY-MM-DD'),
				toDate: moment(self.model.searchObj.toDate).format('YYYY-MM-DD'),
				visitTypeId: self.model.searchObj.visitTypeId,
				organizationId: self.model.searchObj.orgId,
				unitId : self.model.searchObj.unitId,
			}
			$rootScope.startLoader();
			GenericService.serviceAction("POST", URIservice, dataservice).then(
				function (response) {
					if (response.data.status == "success") {
						self.model.pagination.data=[];
						self.model.otBillServiceList = response.data.listObject;
						
						 if( response.data.listObject.length > 0){            
	                         PaginationService.searchData(self.model.otBillServiceList, self.model.pagination);
	                       }
	                	 
					}
					$rootScope.stopLoader();
				});

		}
		

		$scope.userOtTabs = {
				otDetails:false,
				procedureDetails:false,
				service:false,
				inventry:false,
				equipment:false,
				cssdKit:false,
				notes:false,
				timeSheet:false
		};
		$scope.tabAcces = false;
		
		$scope.getUserOtTabs = function(){
			$rootScope.startLoader();
			var URI = BASE_URL+ ROOT_URL+ "/api/global/getUserOtTabsByUserId/"+self.model.userId;
			console.log("URI", URI);
			GenericService.serviceAction("GET", URI,{}).then(function(response) {
				$rootScope.stopLoader();
				if(response.data.listObject != null && response.data.listObject.length>0){
					
					$scope.userOtTabs.cssdKit = response.data.listObject[0].cssdKit;
					$scope.userOtTabs.equipment = response.data.listObject[0].equipment;
					$scope.userOtTabs.inventry = response.data.listObject[0].inventry;
					$scope.userOtTabs.notes = response.data.listObject[0].notes;
					$scope.userOtTabs.otDetails = response.data.listObject[0].otDetails;
					$scope.userOtTabs.procedureDetails = response.data.listObject[0].procedureDetails;
					$scope.userOtTabs.service = response.data.listObject[0].service;
					$scope.userOtTabs.timeSheet = response.data.listObject[0].timeSheet;
					
				}
				if($scope.userOtTabs.cssdKit == false && $scope.userOtTabs.equipment ==false && $scope.userOtTabs.inventry == false
						&& $scope.userOtTabs.notes == false && $scope.userOtTabs.otDetails == false &&
						$scope.userOtTabs.procedureDetails == false && $scope.userOtTabs.service == false 
						&& $scope.userOtTabs.timeSheet == false){
					$scope.tabAcces = true;
				}
			});
		}
		var setProcedureOrderId = function (otBillObj) {

			if($scope.tabAcces){
				growl.warning("Access denied..!!",
						{
							title: 'Warning!'
						});
					return false;
			}else{
			if(otBillObj!=undefined){
			if(otBillObj.admissionId == null && otBillObj.encounterId == null && otBillObj.emergency == false){
				growl.warning("Patient is not admitted or not visited yet..!!",
					{
						title: 'Warning!'
					});
				return false
			}
			otBillObj.renderedFromDashboard = 0;
			}/*else{
				
				otBillObj = {};
			}
			otBillObj.cssdKit = $scope.userOtTabs.cssdKit;
			otBillObj.equipment = $scope.userOtTabs.equipment;
			otBillObj.inventry = $scope.userOtTabs.inventry;
			otBillObj.notes = $scope.userOtTabs.notes;
			otBillObj.otDetails = $scope.userOtTabs.otDetails;
			otBillObj.procedureDetails = $scope.userOtTabs.procedureDetails;
			otBillObj.service = $scope.userOtTabs.service;
			otBillObj.timeSheet = $scope.userOtTabs.timeSheet;*/
			$state.go('otBillingCompletionDetails',
					{
						resultCommonObj: otBillObj
					});
			}		
		}

	
		init();
		searchOtBill();
		$timeout(function() {
			$scope.getUserOtTabs();
		}, 2000);
		
		$scope.printOTReport = function(obj){
			var data =  
			{
		            "appName": "HIS",
		            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
		            "reportDesignFilePath": "OTsummaryprint.rpt",
		            "reportFormat": "PDF",
		            "exportFileName": "OTsummaryprint",
		            "reportParams": [
		              {
			             "paramName": "pro_req_id",
			             "paramType": "Integer",
			             "paramValue": obj.proReqId
		              }
		            ]
		      };
			  reportService.generate(REPORT_API_URI1,data);
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
	 

	}

	function config($stateProvider) {
		$stateProvider.state('OTCompletion', {
			url: '/OTCompletion',
			templateUrl: 'views/master/TheatreManagement/OT-completion.html',
			controller: "otCompletion.Controller",
			title: 'OT Completion',
			resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 322);
	   				 	});
	   					if(arr.length <= 0){
	   						$state.go("accessDenied")
	   						deferred.reject();
	   					 } else {
	   						deferred.resolve();
	   					 }
	   		        });
	   		
	   		        return deferred.promise;
	   		      }
	   		 },
			controllerAs: "vm"
		})
	}

	angular.module("myApp").config(config).controller(
		"otCompletion.Controller", otCompletionController);

})();