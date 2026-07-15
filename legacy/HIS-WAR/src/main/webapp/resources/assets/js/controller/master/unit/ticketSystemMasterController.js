(function() {
	"use strict";
	function ticketSystemMasterController($scope, $rootScope, $http, $cookies,
			CONSTANTS, GenericService, GenericServiceFileUpload, PagerService, growl) {
		var self = this;
		//Default Constructor
		var cookieObject = $cookies.getObject('cookieObject');
		if (cookieObject == undefined) {
			$state.go('login');
			return;
		}
		$scope.reportedBy = cookieObject.userName;
		$scope.reportedUnitName = cookieObject.unitNme;
		$scope.unitId = cookieObject.unitId;
		$scope.organizationId = cookieObject.organizationId;
		$scope.userId = cookieObject.userId;
		$scope.storeId = cookieObject.storeId;
		$scope.organizationName =cookieObject.orgNme; 
	    $scope.offset=0;
	    $scope.noOfRecordsPerPage=10;
	    $scope.commonListCount;
	    $scope.ticketMasterId;
	    $scope.updateBtn=true;
	    $scope.priorityType;
	    $scope.issueType;
	    $scope.searchKeyword;
	    function initializeController() {
			console.log("cookies" ,cookieObject );
			
			$rootScope.loginpage = true;
			var data = {
					"unitId" : $scope.unitId,
					"organizationId" : $scope.organizationId,
					"offSet" : $scope.offset,
					"noOfRecordsPerPage" : $scope.noOfRecordsPerPage
				}
			
			$scope.ticketList = [];
			var URI = BASE_URL + ROOT_URL + "/api/ticketsystemmaster/getall"
					+ "/" + $scope.unitId + "/" + $scope.organizationId;
			
			GenericService.serviceAction("GET", URI, {}).then(
					function(response) {
						if (response.data.status == 'success') {
							$scope.ticketList = response.data.listObject;
							$rootScope.stopLoader();
							$scope.commonListCount = response.data.object;
							$scope.setPage(1,false);
						}

					});
			
			
			$scope.issueTypeList = [{type : "Bug",id:"1"}]; 
			
			$scope.priorityList = [/*{type : "High",id:"1"},{type : "Medium",id:"2"},*/{type : "Low",id:"3"}]
		}
	    
	    
		initializeController();
		
		$scope.clearField = function() {
			$scope.reportedBy = cookieObject.userName;
			$scope.contactNumber = "";
			$scope.email = "";
			$scope.issueDescription = "";
			$scope.files = [];
			$scope.priorityType="";
		    $scope.issueType="";
		}
		
		$scope.checkEmail = function(){
	       
			var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
      		 var email = $scope.email;
      		    if (!filter.test(email)) {

      		     $scope.forthTabLevel=1;
				    growl.error(EMAIL_MSG,{
						title : ERROR_MSG

					});
				    $('#email').focus();
				    return false;
	      }
      		    else{
      		    	return true;
      		    }
		}
		//Get Files From UI
		
		$scope.files = [];
		$scope.getFileDetails = function(e) {
			$scope.$apply(function() {
				for (var i = 0; i < e.files.length; i++) {
					var isFileAvailable = false;
					console.log("File Name  " + e.files[i].name);
					for (var j = 0; j < $scope.files.length; j++) {
						if ($scope.files[j].name === e.files[i].name) {
							isFileAvailable = true;
							break;
						}
					}
					if (!isFileAvailable) {
						$scope.files.push(e.files[i]);
					} else {
								growl.warning("file is already available ::"
										+ e.files[i].name,{    
				                    title : "WARNING"
				                });
					}
				}
			});
		}
		
		//Add Ticket Start
		
		$scope.addTicketDetails = function() {
		 	
	          if($scope.email!= undefined && $scope.email!=null && $scope.email!=""){
	        	  if(!$scope.checkEmail()){
	  	        	return false;
	  	          }    
	          }
	          if($scope.issueDescription==undefined || $scope.issueDescription==null || $scope.issueDescription==""){
	            	  growl.warning("Please Enter Issue Description",{    
	                      title : "WARNING"
	                  });  
	            	  return false;
	              }
	         
				var URI = BASE_URL + ROOT_URL + "/api/ticketsystemmaster/save";
				var files = $scope.files;
				var data = {
					"ticketReportedBy" : $scope.reportedBy,
					"ticketReportedUnitName" : $scope.reportedUnitName,
					"ticketContactNumber" : $scope.contactNumber,
					"ticketEmail" : $scope.email,
					"ticketIssueDescription" : $scope.issueDescription,
					"ticketStatus" : 'A',
					"unitId" : $scope.unitId,
					"organizationId" : $scope.organizationId,
					"userId" : $scope.userId,
					"ticketCreatedDate" : new Date(),
					"ticketUpdatedDate" : null,
					"ticketCreatedBy" : $scope.userId,
					"ticketUpdatedBy" : $scope.userId,
	                "ticketIssueType" : $scope.issueType,
			        "ticketPriority" : $scope.priorityType
				};

				console.log("data", data);
				$rootScope.startLoader();
				GenericService
						.serviceAction("POST", URI, data)
						.then(
								function(response) {
									if (response.data.status == "success") {
										$scope.uploadFiles(response.data.object.ticketMasterId,false);
										$scope.clearField();
										angular.element("input[type='file']").val(null); 
									}
									else if (response.data.status == "error") {
										$rootScope.stopLoader();
										growl.error("Somthing went Worng..Data Not Saved.. ",{    
						                    title : "ERROR"
						                });
									};  
								})
			}
		
	    //Add Ticket End
		
	    //Add Attachment Start
		$scope.uploadFiles = function(ticketMasterId,updateFileFlage) {
			
			var URI = BASE_URL + ROOT_URL + "/api/ticketsystemmaster/savefile";
			var fd = new FormData();
			for (var i = 0; i < $scope.files.length; i++) {
				fd.append('files', $scope.files[i]);
			}	
			fd.append('updateFileFlage', updateFileFlage);
			fd.append('ticketMasterId', ticketMasterId);
			fd.append('userId', $scope.userId);
			fd.append('unitId', $scope.unitId);
			fd.append('organizationId', $scope.organizationId);
			fd.append('organizationName', $scope.organizationName);
			GenericServiceFileUpload
					.serviceAction("POST", URI, fd, { 'Content-Type': undefined})
					.then(
							function(response) {
								
								if(response.data.status == "success" || response.data.status == "Not Found")
									{
									if (response.data.status == "success") {
										growl.success(response.data.message ,{    
						                    title : "SUCCESS"
						                });
										}
									if (response.data.status == "Not Found") {
										growl.warning("Data Saved With No Attached ",{    
						                    title : "WARNING"
						                });
									}
									var ticket = {
											"ticketMasterId" : response.data.object.ticketMasterId,
											"ticketReportedBy" : response.data.object.ticketReportedBy,
											"ticketEmail" : response.data.object.ticketEmail,
											"ticketIssueDescription" : response.data.object.ticketIssueDescription,
											"ticketStatus" : response.data.object.ticketStatus,
											"ticketNumber" : response.data.object.ticketNumber,
											"ticketContactNumber" : response.data.object.ticketContactNumber,
											"ticketIssueType" :response.data.object.ticketIssueType,
											"ticketPriority" : response.data.object.ticketPriority
										};
									$scope.ticketList.unshift(ticket);
									$rootScope.stopLoader();
									}
								if (response.data.status == "error") {
									$rootScope.stopLoader();
									growl.error("Somthing went Worng.. File Not Uploaded ",{    
					                    title : "ERROR"
					                });
								};
							})
							
		}
		
		//Add Attachment End
		
		$scope.updateButtonStatus = function(ticket, index) {
			$scope.updateBtn = false;
			$scope.saveBtn = true;
			$scope.ticketMasterId = ticket.ticketMasterId;
			$scope.updateTicketIndex = index;
			$scope.reportedBy = ticket.ticketReportedBy;
			$scope.contactNumber = ticket.ticketContactNumber;
			$scope.email = ticket.ticketEmail;
			$scope.issueDescription = ticket.ticketIssueDescription;
			$scope.issueType = ticket.ticketIssueType;
			$scope.priorityType = ticket.ticketPriority; 
		}

		//Updating Ticket Master
		
		$scope.updateTicketSystem = function() {
			
			if($scope.email!= undefined && $scope.email!=null && $scope.email!=""){
	        	  if(!$scope.checkEmail()){
	  	        	return false;
	  	          }    
	          }
	          
			 if($scope.issueDescription==undefined || $scope.issueDescription==null || $scope.issueDescription==""){
	            	  growl.warning("Please Enter Issue Description",{    
	                      title : "WARNING"
	                  });  
	            	  return false;
	              }
	          
			var URI = BASE_URL + ROOT_URL
					+ "/api/ticketsystemmaster/update"
			var data = {
				"ticketReportedBy" : $scope.reportedBy,
				"ticketReportedUnitName" : $scope.reportedUnitName,
				"ticketContactNumber" : $scope.contactNumber,
				"ticketEmail" : $scope.email,
				"ticketIssueDescription" : $scope.issueDescription,
				"ticketStatus" : 'A',
				"unitId" : $scope.unitId,
				"organizationId" : $scope.organizationId,
				"userId" : $scope.userId,
				"ticketMasterId" : $scope.ticketMasterId,
				"ticketUpdatedDate" : new Date(),
				"ticketCreatedBy" : $scope.userId,
				"ticketUpdatedBy" : $scope.userId,
				"ticketIssueType" : $scope.issueType,
			    "ticketPriority" : $scope.priorityType
			};
			$rootScope.startLoader();
			GenericService
					.serviceAction("PATCH", URI, data)
					.then(
							function(response) {

								if (response.data.status == "success") {
									$rootScope.stopLoader();
									var ticket = {
										"ticketMasterId" : response.data.object.ticketMasterId,
										"ticketReportedBy" : response.data.object.ticketReportedBy,
										"ticketNumber" :$scope.ticketList[$scope.updateTicketIndex].ticketNumber,
										"ticketEmail" : response.data.object.ticketEmail,
										"ticketIssueDescription" : response.data.object.ticketIssueDescription,
										"ticketStatus" : response.data.object.ticketStatus,
										"ticketContactNumber" : response.data.object.ticketContactNumber,
										"ticketIssueType" : response.data.object.ticketIssueType,
										"ticketPriority" : response.data.object.ticketPriority
									};
									if($scope.files.length > 0){
										$scope.uploadFiles(response.data.object.ticketMasterId,true);
									}	
									else{
									growl.success(response.data.message ,{    
					                    title : "SUCCESS"
					                	});
									}
									$scope.ticketList[$scope.updateTicketIndex] = ticket;
									$scope.updateBtn = true;
									$scope.saveBtn = false;
									$scope.clearField();
									angular.element("input[type='file']").val(null);
								}
								else if (response.data.status == "error") {
									$rootScope.stopLoader();
									growl.error("Somthing went Worng",{    
					                    title : "ERROR"
					                });
								};  
								
							});

		}
		
		
		// Update Status Function
		$scope.updateStatus = function(ticket,index) {
			var URI = BASE_URL + ROOT_URL + "/api/ticketsystemmaster/remove" + "/" + ticket.ticketMasterId + "/"+ $scope.unitId + "/"+$scope.organizationId +"/"+ ticket.ticketStatus; 
			$rootScope.startLoader();
			GenericService
					.serviceAction("PATCH", URI, {})
					.then(
							function(response) {
								if (response.data.status == "success") {
									$rootScope.stopLoader();
									$scope.ticketList[index] = ticket; 
									growl.success(response.data.message ,{    
					                    title : "SUCCESS"
					                });
								}
								else if (response.data.status == "No data found") {
									growl.success(response.data.message ,{    
					                    title : "NO DATA"
					                });
								}  
								
								else if (response.data.status == "error") {
									growl.error("Somthing went Worng",{    
					                    title : "ERROR"
					                });
								}  
							});
		}
		// Display Attachment
		$scope.showAllAttachments = function(ticketMasterId) {
			$scope.ticketMasterId = ticketMasterId;
			var URI = BASE_URL + ROOT_URL + "/api/ticketsystemmaster/getfile/byticketid" + "/" + ticketMasterId + "/"+ $scope.unitId + "/"+$scope.organizationId ;
			$rootScope.startLoader();
			GenericService
					.serviceAction("GET", URI, {})
					.then(
							function(response) {
								if (response.data.status == "success") {
									$rootScope.stopLoader();
									$scope.attachmentList = response.data.listObject;
									$('#documentUploadModalTicketSystem').modal('show'); 
								}
								else if (response.data.status == "No data found") {
									$rootScope.stopLoader();
									growl.success(response.data.message ,{    
					                    title : "NO DATA"
					                });
								}  
								
								else if (response.data.status == "error") {
									growl.error("Somthing went Worng",{    
					                    title : "ERROR"
					                });
								}  
								
							});
		}
        $scope.confirmationPopUp= function(info, index){
        	$scope.removeAllBtn = true;
        	$scope.removeBtn = false;
        	$scope.listIndex = index;
            $scope.removeAttachmentFileId = info.fileId;
            angular.element('#confirmationPopUp').modal('show');
        }
        
        $scope.confirmationPopUpForRemoveAll = function(){
        	$scope.removeAllBtn = false;
        	$scope.removeBtn = true;
            angular.element('#confirmationPopUp').modal('show');
        }
        
        $scope.removeAttachment= function(){
            $rootScope.startLoader();
            var URI =  BASE_URL + ROOT_URL +"/api/ticketsystemmaster/removefile"+ "/" + $scope.removeAttachmentFileId + "/" +$scope.unitId + "/"+$scope.organizationId ;
            GenericService.serviceAction("PATCH" , URI , null).then(function(response) {
                if (response.data.status == "success"){
                    $rootScope.stopLoader();
                    $scope.attachmentList.splice($scope.listIndex, 1);
                    growl.success(
                     response.data.message,
                     {
                         title: 'Success!'
                     }
                    )
                }
                else {
                	$rootScope.stopLoader();
                    growl.error(
                         response.data.message,
                         {
                             title: 'Error!'
                         }
                        )
                }
            });
            
        }
        $scope.removeAllAttachment= function(){
            $rootScope.startLoader();
            var URI =  BASE_URL + ROOT_URL +"/api/ticketsystemmaster/removeallfile"+ "/" + $scope.ticketMasterId + "/" +$scope.unitId + "/"+$scope.organizationId ;
            GenericService.serviceAction("PATCH" , URI , null).then(function(response) {
                if (response.data.status == "success"){
                    $rootScope.stopLoader();
                    $scope.attachmentList.splice($scope.listIndex, 1);
                    growl.success(
                     response.data.message,
                     {
                         title: 'Success!'
                     }
                    )
                }
                else {
                    growl.error(
                         response.data.message,
                         {
                             title: 'Error!'
                         }
                        )
                }
                $rootScope.stopLoader();
            });
            
        }
        

		
		//Search With Page And Page Number
		$scope.searchTicketSystemMaster = function() {
			var data = {
				"unitId" : $scope.unitId,
				"organizationId" : $scope.organizationId,
				"offSet" : $scope.offset,
				"noOfRecordsPerPage" : $scope.noOfRecordsPerPage,
				"searchKeyWord" : $scope.searchKeyword
			}
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + "/api/ticketsystemmaster/search";
			
			GenericService.serviceAction("POST", URI, data).then(
					function(response) {
						if (response.data.status == 'success') {
							$scope.ticketList = response.data.listObject;
							$rootScope.stopLoader();
							
							if($scope.pageSizeFlage){
								$scope.setPage(1,false);
								$scope.pageSizeFlage = false;
							}
							
							else if ($scope.pageSearchKey) {
								$scope.searchListCount = response.data.object;
								$scope.pageSearchKey = false;
							}
						}
						else if (response.data.status == "error") {
							$rootScope.stopLoader();
							growl.error("Somthing went Worng. ",{    
			                    title : "ERROR"
			                });
						};  

					});
		}
		$scope.setSearchKey = function(searchKey){
			$scope.searchKeyword = searchKey;
			$scope.offset=0;
			$scope.pageSearchKey = true;
			$scope.searchTicketSystemMaster();
			$scope.pager = PagerService.GetPager($scope.searchListCount,1,$scope.noOfRecordsPerPage);
			if(searchKey==""){
				initializeController();
			}
		}
		
		
	    $scope.setPageSize = function(tempInt){
	    	$scope.noOfRecordsPerPage = tempInt != null ? tempInt : 10;
	    	$scope.offset = 0;
	        $scope.pageSizeFlage = true;
	        $scope.searchKeyword=null;
	        $scope.searchTicketSystemMaster();
	    }
	    
	    $scope.setPageNumber = function(tempInt){
			$scope.offset=tempInt;
			$scope.searchTicketSystemMaster();
		}
	    
	    $scope.pager = {};
	    $scope.page;
	    $scope.setPage = function(page, flag) {
	        $scope.pager.totalPages = undefined;
	        if (page < 1 || page > $scope.pager.totalPages)
	        {
	            return;
	        }
	        
	        $scope.pager = PagerService.GetPager($scope.commonListCount, page,$scope.noOfRecordsPerPage);
	        
	        if (flag) {
	        	if($scope.searchKeyword == undefined || $scope.searchKeyword == null || $scope.searchKeyword == ""){
	        		$scope.setPageNumber($scope.pager.startIndex);
	            }else{
	                $scope.searchByKeyWord();
	            }
	        }
	    }

	}

	function config($stateProvider) {
		$stateProvider
				.state(
						"ticketSystemMaster",
						{
							url : "/ticketSystemMaster",
							templateUrl : "views/master/UnitMaster/organizationMaster/ticket-system-master.html",
							controller : "TicketSystemMaster.Controller",
							title : 'Ticket System Master',

							controllerAs : "vm",
						});
	}

	angular.module("myApp").config(config).controller(
			"TicketSystemMaster.Controller", ticketSystemMasterController)
})();