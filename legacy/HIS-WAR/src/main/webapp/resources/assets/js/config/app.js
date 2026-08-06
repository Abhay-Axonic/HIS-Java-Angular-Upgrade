'use strict';

angular.module('myApp',['ui.router','ngAnimate','ngStorage','ngCookies','angular-growl','ui.bootstrap','ui.mask','datatables','ngSanitize','ui.mask','ui.bootstrap','angularjs-dropdown-multiselect','angularjs-dropdown-multiselect-day','checklist-model','chart.js', 'angular.filter', 'barcode', 'checklist-model','katex','webcam','kendo.directives','ui.tree','angularjs-datetime-picker', 'ivh.treeview','katex','pubnub.angular.service','infinite-scroll'])
/**
 * Fix, fix/session-token-validation-instability -- cookieObjectAppConfig/cookieObjectAppConfigSecond
 * are pure client-side UI config (dozens of fields, never read server-side) that were being stored as
 * real HTTP cookies and round-tripped on every single request. Once their combined size with
 * cookieObject (which holds the real auth token/userId/unitId) crossed the browser's per-domain cookie
 * budget, the browser silently evicted cookieObject to make room -- confirmed live: the request Cookie
 * header dropped from ~4950 bytes to 4 bytes between two consecutive requests, wiping the auth cookie
 * and causing a random mid-session logout, misreported to the user as "Token Expired!".
 * This decorator transparently redirects storage for these two specific cookie names to sessionStorage
 * (same-origin, no HTTP round-trip, much larger quota) -- every existing $cookies.getObject/putObject
 * call site for these two names (251+ read sites across the app) keeps working completely unmodified.
 */
.config(['$provide', function($provide) {
	$provide.decorator('$cookies', ['$delegate', function($delegate) {
		var REDIRECTED_KEYS = ['cookieObjectAppConfig', 'cookieObjectAppConfigSecond'];
		var originalGetObject = $delegate.getObject;
		var originalPutObject = $delegate.putObject;
		var originalRemove = $delegate.remove;

		$delegate.getObject = function(key) {
			if (REDIRECTED_KEYS.indexOf(key) !== -1) {
				var raw = sessionStorage.getItem(key);
				return raw ? JSON.parse(raw) : undefined;
			}
			return originalGetObject.apply($delegate, arguments);
		};

		$delegate.putObject = function(key, value) {
			if (REDIRECTED_KEYS.indexOf(key) !== -1) {
				sessionStorage.setItem(key, JSON.stringify(value));
				return;
			}
			return originalPutObject.apply($delegate, arguments);
		};

		$delegate.remove = function(key) {
			if (REDIRECTED_KEYS.indexOf(key) !== -1) {
				sessionStorage.removeItem(key);
				return;
			}
			return originalRemove.apply($delegate, arguments);
		};

		return $delegate;
	}]);
}])
.run(['$rootScope','$cookies','$state','$window','GenericService','growl','$localStorage','$interval','$location','GenericServiceParamHeader','BillingGenericService','$timeout', '$compile', '$cacheFactory',function ($rootScope,$cookies,$state,$window,GenericService,growl,$localStorage,$interval,$location,GenericServiceParamHeader,BillingGenericService,$timeout,$compile,$cacheFactory) {
	$rootScope.loginpage= false;
	$rootScope.DoctorReferralShareMasterRevenueWise=1;
	$rootScope.selectedPageName= "";
	 $rootScope.selectedPageNameForAdmissionNote= "";
	 $rootScope.secretKeyEmr= "YourSecretKey12345";
	 
	 // ,'oc.lazyLoad'
	// $rootScope.oneClickOnly['ok'].fn(false)            
	 
	 // for cache
    $rootScope.subScriptionPay = () =>{
        GenericService.subScriptionPay()
    }
	$rootScope.checkSubscription = ()=>{
		try{
			var cookieObject = $cookies.getObject('cookieObject')
			var data = {
				unitId : cookieObject.unitId,
				organizationId : cookieObject.organizationId,
				createdBy : cookieObject.userId
			}
			$rootScope.doctorIdFlagId=cookieObject.doctorId;
			var URI = BASE_URL + ROOT_URL + "/unit/subcription/check";
			GenericService.serviceActionForSubscription("POST", URI,data).then((response) =>
			{
				if(response.data.code==='SUB_5001' || response.data.code=='SUB_5002')
				{
					$rootScope.morqueeMsg = response.data.message;
					$rootScope.paymentObj = response.data.resObject;
					growl.warning(response.data.message,{ttl: 10000,title : WARNING_MSG});
				}
                else{
                    $rootScope.morqueeMsg = null;
                    $rootScope.paymentObj = null;
                }
			});
		}catch(e){
			console.log("Exception",e);
		}
	}
	$rootScope.showTwilioNotification = function(){
		var cookieObject = $cookies.getObject('cookieObject');
		
		if(cookieObject != undefined && cookieObject.doctorId != null & cookieObject.doctorId>0){
		var url= BASE_URL+ ROOT_URL+'/api/getVideoataForNotification';
		var totalNotifications = 0;
		 var twiliobaseUrlLocation = window.location.href;
			var twiliotemp = twiliobaseUrlLocation.split('/');
			var twilioBASE_URL_CALL = twiliobaseUrlLocation.replace(twiliotemp[twiliotemp.length-1],"");
		var data =
		{
		    "unitId": cookieObject.unitId,
            "doctorId": cookieObject.doctorId
		}
		var totalNotifications = 0;
		$.ajax({
			type : "post",
			url : url,
			contentType : "application/json",
	        data : JSON.stringify(data),
			success : function(response) {
	
					var status = response.status;
					var list = response.listObject;
					 $('#twilioNotificationDiv').empty();
					 angular.forEach(list, function(val) {
						 
						 totalNotifications = totalNotifications + 1;
				            var src = BASE_URL + ROOT_URL + "resources/assets/images/tempImages/m.jpg";
				            if (val.patientPhoto != null) {
				                src = val.patientPhoto;
				            } else if (val.genderId == 1) {
				                src = BASE_URL + ROOT_URL + "/resources/assets/images/tempImages/m.jpg";
				            } else {
				                src = BASE_URL + ROOT_URL + "/resources/assets/images/tempImages/patient.png";
				            }

				            var checkinbutton = "Join";
				            if (val.appointmentStatusId == 1) {
				                checkinbutton = "Checkin and Join";
				            }

				            var myvar = '<div class="row padding-top10">' +
				                '<div class="notificationBoxContaint padding-top-bottom5"' +
				                'style="padding-left: 0px !important; padding-right: 15px !important; padding-bottom: 10px;">' +
				                '<div class="row padding-top-bottom5">' +
				                '<div class="col-lg-1 col-md-1 col-sm-1">' +
				                '<hr' +
				                'style="margin-top: 7px !important; margin-bottom: 0px !important; border-top: 10px solid rgba(84, 249, 106, 1) !important; width: 10px; border-radius: 20px !important;">' +
				                '</div>' +
				                '<div class="col-lg-6 col-md-8 col-sm-8">' +
				                '<span>Teleconsultation Alert</span>' +
				                '</div>' +
				                '<div class="col-lg-5 col-md-3 col-sm-3 text-right">' +
				                '<span>' + val.appointmentDate + '</span>' +
				                '</div>' +
				                '</div>' +
				                '<div class="row">' +
				                ' <div class="col-lg-3 col-md-3 col-sm-3">'+
								 '					<div class="row text-center" style="padding-top: 4px;">'+
								 '						<img'+
								 '							src="'+src+'"'+
								 '							style="width: 70px; height: 70px; border-radius: 50px;">'+
								 '					</div>'+
								 '				</div>'+
				                '<div class="col-lg-9 col-md-9 col-sm-9 text-right">' +
				                '<div class="row text-center patientDetailsMsgs"' +
				                'style="font-size: 12px !important;">' +
				                '<span class="patientDetailsMsg"' +
				                'style="font-size: 12px !important;">' + val.patientName +
				                '</span> is ready and waiting in the room for the' +
				                '<span class="patientDetailsMsg"' +
				                'style="font-size: 12px !important;">Teleconsultation' +
				                'appointment at ' + val.timeSlot + '</span>' +
				                '</div>' +
				                '<div class="row padding-top10">' +
				                '<button type="button" class="btn btn-primary width-auto" style="border-radius: 10px; min-width: 110px;" ng-click="joinByDoctorByNotification(' + val.appointmentIdMyLife + ',' + val.userChatDetailsId + ');">' + checkinbutton + '</button>' +
				                '</div>' +
				                '</div>' +
				                '</div>' +
				                '</div>' +
				                '</div>';

				            // Append the compiled HTML
				            var compiledElement = $compile(myvar)($rootScope);
				            angular.element(document.getElementById('twilioNotificationDiv')).append(compiledElement);
				           
				        });

				        // Apply the changes to the scope
				        $timeout(function() {
				            $rootScope.$apply();
				        });
				        $('#totalNotifications').text(totalNotifications);
					if(list.length==0){
						 $('#twilioNotificationDiv').append('<div style="text-align: center;">No notification.</div>');
					}
				},error : function(){
					console.log("Error");
					  stopLoader();
				}
			})
		
		}
	}
	 $rootScope.cacheKeys = [];
	 $rootScope.cacheData = '';
	 $rootScope.cacheObject = $cacheFactory("newCacheInstance");

	 $rootScope.add = function (key, value) {            
		 $rootScope.cacheObject.put(key, value);
          $rootScope.cacheKeys.push(key);
     };

     $rootScope.update = function (key, value) {
    	 $rootScope.cacheObject.put(key, value);
      };

      $rootScope.read = function (key) {
    	  $rootScope.cacheData = $scope.cacheObject.get(key);
      };

      $rootScope.remove = function (key) {
    	  $rootScope.cacheObject.remove(key);
      };

      $rootScope.removeAll = function (key) {
    	  $rootScope.cacheObject.removeAll();
      };

      $rootScope.Dispose = function (key) {
    	  $rootScope.cacheObject.destroy();
      };
	 
	 // For Cache
	 
	$rootScope.getMasterPage = function(header){
	// alert('cd');
		// $rootScope.commonType = header;
		 sessionStorage.setItem("commonType",header);
		sessionStorage.setItem('selectedPageName', header);

		$rootScope.$emit('customEvent', header)
		$state.go('commonMaster',{},{ reload: true });
	}
	
	$rootScope.$state = $state;
	// https://stackoverflow.com/questions/23813599/set-page-title-using-ui-router
	
	$rootScope.openDatePickerSearch = function() {
	   $rootScope.datepicker2.opened = true;
	    };
	    $rootScope.datepicker2 = {
			      opened: false
			    };
	// role permission access STARTr
    
   
   /*
	 * $rootScope.$on('$locationChangeStart', function(event) { var permissions =
	 * JSON.parse(localStorage.getItem('permissions')); if(permissions!=null &&
	 * permissions!=undefined){ var arr = permissions.modules.filter(function
	 * (item) { return (item.actionLink == window.location.hash &&
	 * item.moduleType == 1 && window.location.hash!='#/login'&&
	 * window.location.hash!='#/logout'); }); if(arr.length == 0 &&
	 * window.location.hash!='#/login'&& window.location.hash!='#/logout'){
	 * $location.path('/logout'); return; } } });
	 */
	    
	    $timeout( function(){
      	  $rootScope.stopLoader();
      },240000);
	    
	    
	    $rootScope.keyCode = "";
	    $rootScope.keyPressed = function(e) {
	      // alert(e.which);
	     // $rootScope.keyCode = e.which;
	      if(e.which == 19){
	    	  $('#searchActionMaster').val('');
	    	  $rootScope.redirectPopupObject = {};
	    	  angular.element('#favoriteModal').modal('show');
	    	  
	    	  
	    	   $timeout( function(){
	    		   $('#searchActionMaster').focus();
             },1000);
	      }
	      
	      
	    };
	$rootScope.getFavoritePopup = function(){
		$('#searchActionMaster').val('');
  	  $rootScope.redirectPopupObject = {};
  	  angular.element('#favoriteModal').modal('show');
	}
	// role permission access END
	$rootScope.getFormatDate = function(date)
	{
		if(date != "")
			return date = date.getDate() + '-' + (date.getMonth() + 1) + '-' + date.getFullYear();
	}
	
	$rootScope.isValidDate = function(fromDate,toDate)
	{
		if(fromDate != "" && toDate!=""){
			if(fromDate > toDate)
			{
				return false;
			}else{
				return true;
			}
		}
	}
	
	$rootScope.gotBedMgnt = function(){
		$state.go('bedManagement');
	}
	
	
	
	$rootScope.gotoOpBilling = function(){
		$state.go('opdBilling',{"appointmentObj" : ""});
	}
	
	$rootScope.hiddenParam="";
	$rootScope.getUserNameForTop = function()
	{
		var cookieObject = $cookies.getObject('cookieObject');
		if(cookieObject == undefined){
			$state.go('login');
			return;
		}else
			{
			$rootScope.hiddenParam=cookieObject.authToken;
				return cookieObject.firstName;
			}
	}
	
	
	$rootScope.gotToDashboard = function(){
		var cookieObject = $cookies.getObject('cookieObject');
		if(cookieObject != undefined){
			if(cookieObject.dashboard!=null)
				$state.go(cookieObject.dashboard);
			else
				$state.go("home");
		}
		
	}
	
	
	/*
	 * $rootScope.getBiUrl = function () { // var url = BI_URL; var
	 * baseUrlLocation = window.location.href; var temp =
	 * baseUrlLocation.split('//'); var BASE_URL_CALL = temp[1].split('.'); var
	 * isLocalhost = (/:/.test(temp[1]) ||
	 * window.location.hostname.includes("localhost") ); var subdomain =
	 * (isLocalhost==true || BASE_URL_CALL[0]=='smeabc' ||
	 * BASE_URL_CALL[0]=='qa' || BASE_URL_CALL[0]=='intdev' ||
	 * BASE_URL_CALL[0]=='demo' ) ? 'localhost' : BASE_URL_CALL[0];
	 * 
	 * var url = temp[0]+"//"+subdomain+BI_URL;
	 * 
	 * return url; }
	 */

	$rootScope.getBiUrl = function () {

		var url = "";
		var baseUrlLocation = window.location.href;
		var temp = baseUrlLocation.split('//');
		var BASE_URL_CALL = temp[1].split('.');
		var isLocalhost = (/:/.test(temp[1]) || window.location.hostname.includes("localhost") );
		var subdomain = (isLocalhost==true || BASE_URL_CALL[0]=='smeabc' || BASE_URL_CALL[0]=='qa' || BASE_URL_CALL[0]=='intdev' || BASE_URL_CALL[0]=='demo' ) ? 'localhost' : BASE_URL_CALL[0];

		if(isLocalhost==true || BASE_URL_CALL[0]=='smeabc' || BASE_URL_CALL[0]=='qa' || BASE_URL_CALL[0]=='intdev' || BASE_URL_CALL[0]=='demo'){

		url = "https://biqa.his.clinivantage.co.in/";
		}else{
		url = temp[0]+"//"+BASE_URL_CALL[0]+BI_URL;
		}


		return url;
		} 
	
	
		$rootScope.getUnitNameForTop = function()
	{
		var cookieObject = $cookies.getObject('cookieObject');
		if(cookieObject == undefined){
			$state.go('login');
			return;
		}else
			{
			$rootScope.hiddenParam=cookieObject.authToken;
				return cookieObject.unitNme;
			}
	}
		$rootScope.userOtTabs = {
				otDetails:false,
				procedureDetails:false,
				service:false,
				inventry:false,
				equipment:false,
				cssdKit:false,
				notes:false,
				timeSheet:false,
				medication:false,
				implant:false,
				medicalGas:false,
				anesthesiaNotes: false,
				surgeryNotes: false,
				dischargeNotes : false,
				followUpNotes : false,
		};
		
		$rootScope.getUserOtTabs = function(){
			
			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject != undefined){
			
					var URI = BASE_URL+ ROOT_URL+ "/api/global/getUserOtTabsByUserId/"+cookieObject.userId;
					GenericService.serviceAction("GET", URI,{}).then(function(response) {
						if(response.data.listObject != null && response.data.listObject.length>0){
							
							$rootScope.userOtTabs.cssdKit = response.data.listObject[0].cssdKit;
							$rootScope.userOtTabs.equipment = response.data.listObject[0].equipment;
							$rootScope.userOtTabs.inventry = response.data.listObject[0].inventry;
							$rootScope.userOtTabs.notes = response.data.listObject[0].notes;
							$rootScope.userOtTabs.otDetails = response.data.listObject[0].otDetails;
							$rootScope.userOtTabs.procedureDetails = response.data.listObject[0].procedureDetails;
							$rootScope.userOtTabs.service = response.data.listObject[0].service;
							$rootScope.userOtTabs.timeSheet = response.data.listObject[0].timeSheet;
							$rootScope.userOtTabs.medication = response.data.listObject[0].medication;
							$rootScope.userOtTabs.implant = response.data.listObject[0].implant;
							$rootScope.userOtTabs.medicalGas = response.data.listObject[0].medicalGas;
							$rootScope.userOtTabs.anesthesiaNotes = response.data.listObject[0].anesthesiaNotes;
							$rootScope.userOtTabs.surgeryNotes = response.data.listObject[0].surgeryNotes;
							$rootScope.userOtTabs.dischargeNotes  = response.data.listObject[0].dischargeNotes;
							$rootScope.userOtTabs.followUpNotes  = response.data.listObject[0].followUpNotes;
						}
						
					});
		
			}
	}
		$rootScope.getUserOtTabs();
		

	 $rootScope.getRoleNameForTop = function()
		{
			var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
			if(cookieObjectAppConfig == undefined){
				$state.go('login');
				return;
			}else
				{
					return cookieObjectAppConfig.roleName;
					$rootScope.rolename=cookieObjectAppConfig.roleName;
				}
		}
	 
	// CODE ADDED BY GANESH 13.11.2017
	// Sorting column
	/** Code for sorting column data */
	$rootScope.reverse = false; 
	 $rootScope.sortColumn = function(col)
	 {
		 $rootScope.column = col;
	  if($rootScope.reverse)
	  {
		  $rootScope.reverse = false;
		  $rootScope.reverseclass = 'fa fa-caret-up';
		  
	  }else
	  {
		  $rootScope.reverse = true;
		  $rootScope.reverseclass = 'fa fa-caret-down';
	  }
	 };
	
	// CODE ADDED BY GANESH 06.11.2017
	$rootScope.startLoader = function()
	{
		  $('#preloader').show();
		  $( "#sidebar-wrapper" ).addClass( "sidebar-wrapper ");
	}
	$rootScope.stopLoader = function()
	{
		 $('#preloader').hide();
		 $( "#sidebar-wrapper" ).removeClass( "sidebar-wrapper ");
	}
	
	
	$rootScope.startLoaderPopup = function()
	{
		  $('#preloaderPopup').show();
		  $( "#sidebar-wrapper" ).addClass( "sidebar-wrapper ");
	}
	$rootScope.stopLoaderPopup = function()
	{
		 $('#preloaderPopup').hide();
		 $( "#sidebar-wrapper" ).removeClass( "sidebar-wrapper ");
	}
	
	// sneha
	$rootScope.currentHrefDoccument = function(){
		var currentURL = $window.location.href;
		console.log(currentURL);
		var url=currentURL.split(":");
		console.log("d",url);
		if(url[0]=="http"){
			var  href='http://docs.google.com/viewer?embedded=true&url='
				return href;
		}else{
			var  href='https://docs.google.com/viewer?embedded=true&url='
				return href;
		}
	}
	
	
	
	$rootScope.getDateByFormat = function(dateString,flag){
		
		var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
		if(cookieObjectAppConfig == undefined){
			$state.go('login');
			return;
		}
		var dateFormatArray=["DD/MM/YYYY","DD/MMM/YYYY","DD-MM-YYYY","DD-MMM-YYYY","YYYY/MM/DD","YYYY/MMM/DD","YYYY-MM-DD","YYYY-MMM-DD"
// ,"DD/MM/YYYY hh mm ss","DD/MMM/YYYY hh mm ss","DD-MM-YYYY hh mm
// ss","DD-MMM-YYYY hh mm ss","YYYY/MM/DD hh mm ss","YYYY/MMM/DD hh mm
// ss","YYYY-MM-DD hh mm ss","YYYY-MMM-DD hh mm ss"
			];
        // alert(cookieObjectAppConfig.dateFormat);
		if(flag){
			// alert(moment(dateString).format(cookieObjectAppConfig.dateFormat
			// + ' hh:mm:ss'));
			var now;
			var currentDate;
			var checkFlag = false;
			angular.forEach(dateFormatArray,function(value,index){
				 if(checkFlag == false)
					{
						currentDate = moment(dateString,value+" hh mm ss").format(cookieObjectAppConfig.dateFormat.toUpperCase()+' h:mm A');
						 if(currentDate!='Invalid date' && checkFlag == false)
							{
								now=currentDate;
								checkFlag = true;
							}
					}
			});
				return now;
		}else{
			var now;
			var currentDate;
			var checkFlag = false;
			angular.forEach(dateFormatArray,function(value,index){
				 if(checkFlag == false)
					{
						currentDate = moment(dateString,value).format(cookieObjectAppConfig.dateFormat.toUpperCase());
						 if(currentDate!='Invalid date' && checkFlag == false)
							{
								now=currentDate;
								checkFlag = true;
							}
					}
			});
				return now;
		}
		
    }
	
	
	$rootScope.calcDiff = function(firstDate, secondDate){
//		var date2 = new Date(today);
//        var date1 = new Date(date);
        var timeDiff = Math.abs(secondDate.getTime() - firstDate.getTime());
        $rootScope.dayDifference = Math.ceil(timeDiff / (1000 * 3600 * 24));
        return $rootScope.dayDifference;
	}
	
	   $rootScope.dateDiffrence = function(doa,pdd){
	        // var a = moment('1/1/2012', 'DD/MM/YYYY');
	        // var b = moment('2/1/2012', 'DD/MM/YYYY');
	        var days = pdd.diff(doa, 'days');
	        if(days == 0){
	        	days = 1;
	        }
	        return days;
	    }
	   $rootScope.joinByDoctorByNotification = function(appointmentIdMyLife,userChatDetailsId) {
     		$rootScope.twilioAppoId = appointmentIdMyLife;
     		$('#twilioappointmentId').val(appointmentIdMyLife);
     		
     		$('#HeaderTwilioVideoClick').trigger('click');
   		    $('#videoTwillio').modal();
   		
     		var data =
			{
			"userChatDetailsId":userChatDetailsId
			}
			
			var URI = BASE_URL + ROOT_URL + "/api/changeStatusofuserChatDetailsId";
			BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					$scope.patientDocumentListTest = [];
					if (response.data.status == "success") 
					{
					}
					else if(response.data.status == "error")
						{
						$rootScope.stopLoader();
						}
				});
	
            };  
            
            
	$rootScope.getChangedFormatedDate = function(datepickerRes)
	{
		var array = datepickerRes.split('-');
		var newDate = "";
	   return newDate = array[2]+'/'+array[1]+'/'+array[0];
	}
	$rootScope.getChangedFormatedDateShalse = function(datepickerRes)
	{
		var array = datepickerRes.split('/');
		var newDate = "";
	   return newDate = array[0]+'-'+array[1]+'-'+array[2];
	}
	$rootScope.getChangedFormatedDateShalseTwo = function(datepickerRes)
	{
		var array = datepickerRes.split('/');
		var newDate = "";
	   return newDate = array[1]+'-'+array[0]+'-'+array[2];
	}
	$rootScope.getFormatedDate = function(datepickerRes)
	{
		var array = datepickerRes.split('/');
		var newDate = "";
	   return newDate = array[1]+'-'+array[0]+'-'+array[2];
	}
	$rootScope.getFormatedDateeShase = function(datepickerRes)
	{
		var array = datepickerRes.split('/');
		var newDate = "";
	   return newDate = array[1]+'/'+array[0]+'/'+array[2];
	}
	
	$rootScope.getAge = function(dateOfBirth)
	{
		
		// var ageDifMs = Date.now() - new Date(dateOfBirth);
	   // var ageDate = new Date(ageDifMs); // miliseconds from epoch
	   // return Math.abs(ageDate.getUTCFullYear() - 1970);
	  return moment().diff(new Date(moment(dateOfBirth, 'MM/DD/YYYY').format('YYYY-MM-DD')), 'years');
	}
	
	// Sneha
	$rootScope.getAgeEr = function(dateOfBirth)
	{
		
	if(dateOfBirth ==((moment(dateOfBirth, 'MM/DD/YYYY').format('YYYY-MM-DD')), 'years')){
	  return moment().diff(new Date(moment(dateOfBirth, 'MM/DD/YYYY').format('YYYY-MM-DD')), 'years');
	}
	else{
	  return moment().diff(new Date(moment(dateOfBirth, 'DD/MM/YYYY').format('YYYY-MM-DD')), 'years');
	}
	}
     
	

	$rootScope.getAdmissionNote = function(textBoxHideShow){
		sessionStorage.setItem('selectedPageNameForAdmissionNote', textBoxHideShow);
		$state.go('admissionRequest',{},{reload:true});
		$(".selectedPageName").text("Admission Note");
	}

// ArrowCodeStart
$rootScope.emergencyMenuOpen = false;
$rootScope.opdMenuOpen = false;
$rootScope.patientMangeMenuOpen = false;
$rootScope.ipdMenuOpen = false;
$rootScope.admissionMenuOpen = false;
$rootScope.billingMenuOpen = false;
$rootScope.nursingMenuOpen = false;
$rootScope.nursingRosMenuOpen = false;
$rootScope.WorkstationMenuOpen = false;
$rootScope.tranformMoveMenuOpen = false;
$rootScope.hospitalDMenuOpen = false;
$rootScope.pharmacyMenuOpen = false;
$rootScope.inventoryMenuOpen = false;
$rootScope.incidentRMenuOpen = false;
$rootScope.narcoticsStoreMenuOpen = false;
$rootScope.crashCartMenuOpen = false;
$rootScope.houseKMenuOpen = false;
$rootScope.cSSDMenuOpen = false;
$rootScope.lineLMenuOpen = false;
$rootScope.nABHMenuOpen = false;
$rootScope.eHRMenuOpen = false;
$rootScope.trMMenuOpen = false;
$rootScope.hDMenuOpen = false;
$rootScope.newBornMenuOpen = false;
$rootScope.lISOptionMenuOpen = false;
$rootScope.phlebotomyMenuOpen = false;
$rootScope.centralRMenuOpen = false;
$rootScope.consolidatedMenuOpen = false;
$rootScope.biochemistryMenuOpen = false;
$rootScope.serologyMenuOpen = false;
$rootScope.hematologyMenuOpen = false;
$rootScope.clinicalMenuOpen = false;
$rootScope.histopathologyMenuOpen = false;
$rootScope.lisTMenuOpen = false;
$rootScope.microMenuOpen = false;
$rootScope.outSourceMenuOpen = false;
$rootScope.trendAMenuOpen = false;
$rootScope.risMenuOpen = false;
$rootScope.clinicalMenuOpen = false;
$rootScope.hemodialysisMenuOpen = false;
$rootScope.diagnosticCSMenuOpen = false;
$rootScope.physiotherapyMenuOpen = false;
$rootScope.cssdCliMenuOpen = false;
$rootScope.preStMenuOpen = false;
$rootScope.sterilizationPMenuOpen = false;
$rootScope.otimsMenuOpen = false;
$rootScope.dietKMenuOpen = false;
$rootScope.transferOPtionMenuOpen = false;
$rootScope.cssdCliMenuOpenClick = false;
$rootScope.housekeepFuMenuOpen = false;
$rootScope.mortuaryMenuOpen = false;
$rootScope.procurementMenuOpen = false;
$rootScope.purchaseMenuOpen = false;
$rootScope.quotationMenuOpen = false;
$rootScope.workOrderMenuOpen = false;
$rootScope.consignmentMenuOpen = false;
$rootScope.loanInOutMenuOpen = false;
$rootScope.storeInMenuOpen = false;
$rootScope.storeMenuOpenop = false;
$rootScope.stockAdjMenuOpen = false;
$rootScope.stockFunMenuOpen = false;
$rootScope.scrapMenuOpen = false;
$rootScope.pharmacyFunMenuOpen = false;
$rootScope.ipPharmacyMenuOpen = false;
$rootScope.oppharmMenuOpen = false;
$rootScope.narcoticsStoreMenuOpen = false;
$rootScope.inPatientPharmMenuOpen = false;
$rootScope.assetFunMenuOpen = false;
$rootScope.fixiedAssetOBMenuOpen = false;
$rootScope.equipmentsMenuOpen = false;
$rootScope.preventiveMMenuOpen = false;
$rootScope.coreectiveMenuOpen = false;
$rootScope.backOfficeMenuOpen = false;
$rootScope.cashDeskMMenuOpen = false;
$rootScope.fundcollectionMenuOpen = false;
$rootScope.companysettMenuOpen = false;
$rootScope.doctorShareMenuOpen = false;
$rootScope.lineLMenuOpen = false;
$rootScope.bloodBanckFunMenuOpen = false;
$rootScope.donorMenuOpen = false;
$rootScope.bloodBProMenuOpen = false;
$rootScope.ivfMenuOpen = false;
$rootScope.qmsFunMenuOpen = false;
$rootScope.gobalMasterMenuOpen = false;
$rootScope.commonMenuOpen = false;
$rootScope.organizationStruMenuOpen = false;
$rootScope.patientRFunctionMenuOpen = false;
$rootScope.adtFuntionMenuOpen = false;
$rootScope.lisFunMenuOpen = false;
$rootScope.billingFunctionMenuOpen = false;
$rootScope.pharmacyFunctionGMenuOpen = false;
$rootScope.clinicalFunctionGMenuOpen = false;
$rootScope.dietAndKitFunctionMenuOpen = false;
$rootScope.assetMagGMenuOpen = false;
$rootScope.equipmentMasterGMenuOpen = false;
$rootScope.unitMasterMainMenuOpen = false;
$rootScope.organizationStrucUnitMMenuOpen = false;
$rootScope.aDTUnitMasterMainMenuOpen = false;
$rootScope.cSSDUnitMasterMenuOpen = false;
$rootScope.nursingUnitMasterMenuOpen = false;
$rootScope.lISUnitMasterMenuOpen = false;
$rootScope.billingUnitMasterMenuOpen = false;
$rootScope.packageConfMasterMenuOpen = false;
$rootScope.pharmacyUnitMasterMenuOpen = false;
$rootScope.clinicalUnitMasterMenuOpen = false;
$rootScope.oTIMSUnitMasterMenuOpen = false;
$rootScope.assetManageUnitMasterMenuOpen = false;
$rootScope.commonUnitMasterMenuOpen = false;
$rootScope.mortuaryUnitMasterMenuOpen = false;
$rootScope.schedulingUnitMasterMenuOpen = false;
$rootScope.scheduleBlockUnblockMenuOpen = false;
$rootScope.configurationUnitMasterMenuOpen = false;
$rootScope.userManagementUnitMasterMenuOpen = false;
$rootScope.templateUtilityUnitMasterMenuOpen = false;
$rootScope.bloodBankUnitMasterMenuOpen = false;
$rootScope.loyaltyCardUnitMasterMenuOpen = false;
$rootScope.InventoryUnitMasterMenuOpen = false;
$rootScope.fundmanagemeUnitMasterMenuOpen = false;
$rootScope.ambulanceServiceMenuOpen = false;
$rootScope.visitorTypeUnitMasterMenuOpen = false;
$rootScope.qMSUnitMasterMenuOpen = false;
$rootScope.incidentReportingMenuOpen = false;
$rootScope.aIButtonConfigurationMenuOpen = false;






/* commonFunctionClose Start */
$rootScope.commonFunctionClose = function(){
    $rootScope.emergencyMenuOpen = false;
    $rootScope.opdMenuOpen = false;
    $rootScope.patientMangeMenuOpen = false;
    $rootScope.ipdMenuOpen = false;
    $rootScope.admissionMenuOpen = false;
    $rootScope.billingMenuOpen = false;
    $rootScope.nursingMenuOpen = false;
    $rootScope.nursingRosMenuOpen = false;
    $rootScope.WorkstationMenuOpen = false;
    $rootScope.tranformMoveMenuOpen = false;
    $rootScope.hospitalDMenuOpen = false;
    $rootScope.pharmacyMenuOpen = false;
    $rootScope.inventoryMenuOpen = false;
    $rootScope.incidentRMenuOpen = false;
    $rootScope.narcoticsStoreMenuOpen = false;
    $rootScope.crashCartMenuOpen = false;
    $rootScope.houseKMenuOpen = false;
    $rootScope.cSSDMenuOpen = false;
    $rootScope.lineLMenuOpen = false;
    $rootScope.nABHMenuOpen = false;
    $rootScope.eHRMenuOpen = false;
    $rootScope.trMMenuOpen = false;
    $rootScope.hDMenuOpen = false;
    $rootScope.newBornMenuOpen = false;
    $rootScope.lISOptionMenuOpen = false;
    $rootScope.phlebotomyMenuOpen = false;
    $rootScope.centralRMenuOpen = false;
    $rootScope.consolidatedMenuOpen = false;
    $rootScope.biochemistryMenuOpen = false;
    $rootScope.serologyMenuOpen = false;
    $rootScope.hematologyMenuOpen = false;
    $rootScope.clinicalMenuOpen = false;
    $rootScope.histopathologyMenuOpen = false;
    $rootScope.lisTMenuOpen = false;
    $rootScope.microMenuOpen = false;
    $rootScope.outSourceMenuOpen = false;
    $rootScope.trendAMenuOpen = false;
    $rootScope.risMenuOpen = false;
    $rootScope.clinicalMenuOpen = false;
    $rootScope.hemodialysisMenuOpen = false;
    $rootScope.diagnosticCSMenuOpen = false;
    $rootScope.physiotherapyMenuOpen = false;
    $rootScope.cssdCliMenuOpen = false;
    $rootScope.preStMenuOpen = false;
    $rootScope.sterilizationPMenuOpen = false;
    $rootScope.otimsMenuOpen = false;
    $rootScope.dietKMenuOpen = false;
    $rootScope.transferOPtionMenuOpen = false;
    $rootScope.cssdCliMenuOpenClick = false;
    $rootScope.housekeepFuMenuOpen = false;
    $rootScope.mortuaryMenuOpen = false;
    $rootScope.procurementMenuOpen = false;
    $rootScope.purchaseMenuOpen = false;
    $rootScope.quotationMenuOpen = false;
    $rootScope.workOrderMenuOpen = false;
    $rootScope.consignmentMenuOpen = false;
    $rootScope.loanInOutMenuOpen = false;
    $rootScope.storeInMenuOpen = false;
    $rootScope.storeMenuOpenop = false;
    $rootScope.stockAdjMenuOpen = false;
    $rootScope.stockFunMenuOpen = false;
    $rootScope.scrapMenuOpen = false;
    $rootScope.pharmacyFunMenuOpen = false;
    $rootScope.ipPharmacyMenuOpen = false;
    $rootScope.oppharmMenuOpen = false;
    $rootScope.narcoticsStoreMenuOpen = false;
    $rootScope.inPatientPharmMenuOpen = false;
    $rootScope.assetFunMenuOpen = false;
    $rootScope.fixiedAssetOBMenuOpen = false;
    $rootScope.equipmentsMenuOpen = false;
    $rootScope.preventiveMMenuOpen = false;
    $rootScope.coreectiveMenuOpen = false;
    $rootScope.backOfficeMenuOpen = false;
    $rootScope.cashDeskMMenuOpen = false;
    $rootScope.fundcollectionMenuOpen = false;
    $rootScope.companysettMenuOpen = false;
    $rootScope.doctorShareMenuOpen = false;
    $rootScope.lineLMenuOpen = false;
    $rootScope.bloodBanckFunMenuOpen = false;
    $rootScope.donorMenuOpen = false;
    $rootScope.bloodBProMenuOpen = false;
    $rootScope.ivfMenuOpen = false;
    $rootScope.qmsFunMenuOpen = false;
    $rootScope.gobalMasterMenuOpen = false;
    $rootScope.commonMenuOpen = false;
    $rootScope.organizationStruMenuOpen = false;
    $rootScope.patientRFunctionMenuOpen = false;
    $rootScope.adtFuntionMenuOpen = false;
    $rootScope.lisFunMenuOpen = false;
    $rootScope.billingFunctionMenuOpen = false;
    $rootScope.pharmacyFunctionGMenuOpen = false;
    $rootScope.clinicalFunctionGMenuOpen = false;
    $rootScope.dietAndKitFunctionMenuOpen = false;
    $rootScope.assetMagGMenuOpen = false;
    $rootScope.equipmentMasterGMenuOpen = false;
    $rootScope.unitMasterMainMenuOpen = false;
    $rootScope.organizationStrucUnitMMenuOpen = false;
    $rootScope.aDTUnitMasterMainMenuOpen = false;
    $rootScope.cSSDUnitMasterMenuOpen = false;
    $rootScope.nursingUnitMasterMenuOpen = false;
    $rootScope.lISUnitMasterMenuOpen = false;
    $rootScope.billingUnitMasterMenuOpen = false;
    $rootScope.packageConfMasterMenuOpen = false;
    $rootScope.pharmacyUnitMasterMenuOpen = false;
    $rootScope.clinicalUnitMasterMenuOpen = false;
    $rootScope.oTIMSUnitMasterMenuOpen = false;
    $rootScope.assetManageUnitMasterMenuOpen = false;
    $rootScope.commonUnitMasterMenuOpen = false;
    $rootScope.mortuaryUnitMasterMenuOpen = false;
    $rootScope.schedulingUnitMasterMenuOpen = false;
    $rootScope.scheduleBlockUnblockMenuOpen = false;
    $rootScope.configurationUnitMasterMenuOpen = false;
    $rootScope.userManagementUnitMasterMenuOpen = false;
    $rootScope.templateUtilityUnitMasterMenuOpen = false;
    $rootScope.bloodBankUnitMasterMenuOpen = false;
    $rootScope.loyaltyCardUnitMasterMenuOpen = false;
    $rootScope.InventoryUnitMasterMenuOpen = false;
    $rootScope.fundmanagemeUnitMasterMenuOpen = false;
    $rootScope.ambulanceServiceMenuOpen = false;
    $rootScope.visitorTypeUnitMasterMenuOpen = false;
    $rootScope.qMSUnitMasterMenuOpen = false;
    $rootScope.incidentReportingMenuOpen = false;
    $rootScope.aIButtonConfigurationMenuOpen = false;
    }
/* commonFunctionClose End  */







$rootScope.toggleEmergencyMenu = function () {
$rootScope.commonFunctionClose();
$rootScope.emergencyMenuOpen = !$rootScope.emergencyMenuOpen;

};

$rootScope.toggleOpdMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.opdMenuOpen = !$rootScope.opdMenuOpen;
if (!$rootScope.ipdMenuOpen) {
    $rootScope.emergencyMenuOpen = false;
  }
};

$rootScope.togglepatientMange = function () {
    $rootScope.commonFunctionClose();
$rootScope.patientMangeMenuOpen = !$rootScope.patientMangeMenuOpen;
if (!$rootScope.patientMangeMenuOpen) {
    $rootScope.opdMenuOpen = false;
  }
};

$rootScope.toggleipdMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.ipdMenuOpen = !$rootScope.ipdMenuOpen;
$rootScope.patientMangeMenuOpen = false;
$rootScope.opdMenuOpen   = false;
};

$rootScope.toggleadmissionMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.admissionMenuOpen = !$rootScope.admissionMenuOpen;
}

$rootScope.toggleBillingMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.billingMenuOpen = !$rootScope.billingMenuOpen;
$rootScope.ipdMenuOpen   = false;
$rootScope.transferOPtionMenuOpen  = false;
$rootScope.lisFunMenuOpen   = false;
};

$rootScope.togglenursingMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.nursingMenuOpen = !$rootScope.nursingMenuOpen;
};

$rootScope.togglenursingRosMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.nursingRosMenuOpen = !$rootScope.nursingRosMenuOpen;
};

$rootScope.toggleWorkstationMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.WorkstationMenuOpen = !$rootScope.WorkstationMenuOpen;
$rootScope.nursingRosMenuOpen  = false;
};

$rootScope.toggleTransferMoveMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.tranformMoveMenuOpen = !$rootScope.tranformMoveMenuOpen;
$rootScope.WorkstationMenuOpen  = false;
};

$rootScope.togglehospitalDMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.hospitalDMenuOpen = !$rootScope.hospitalDMenuOpen;
$rootScope.tranformMoveMenuOpen  = false;
};

$rootScope.togglePharmacyMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.pharmacyMenuOpen = !$rootScope.pharmacyMenuOpen;
$rootScope.hospitalDMenuOpen  = false;
};

$rootScope.toggleInventoryMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.inventoryMenuOpen = !$rootScope.inventoryMenuOpen;
$rootScope.pharmacyMenuOpen  = false;
};

$rootScope.toggleIncidentRMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.incidentRMenuOpen = !$rootScope.incidentRMenuOpen;
$rootScope.inventoryMenuOpen  = false;
};

$rootScope.toggleNarcoticsStoreMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.narcoticsStoreMenuOpen = !$rootScope.narcoticsStoreMenuOpen;
$rootScope.incidentRMenuOpen  = false;
};

$rootScope.toggleCrashCartMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.crashCartMenuOpen = !$rootScope.crashCartMenuOpen;
$rootScope.narcoticsStoreMenuOpen  = false;
$rootScope.houseKMenuOpen  = false;

};

$rootScope.toggleHouseKeepingMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.houseKMenuOpen = !$rootScope.houseKMenuOpen;
$rootScope.crashCartMenuOpen  = false;
$rootScope.narcoticsStoreMenuOpen  = false;
};

$rootScope.toggleCSSDMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.cSSDMenuOpen = !$rootScope.cSSDMenuOpen;
$rootScope.crashCartMenuOpen  = false;
};

$rootScope.toggleLineLMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.lineLMenuOpen = !$rootScope.lineLMenuOpen;
$rootScope.cSSDMenuOpen  = false;
$rootScope.crashCartMenuOpen  = false;
};

$rootScope.toggleNABHMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.nABHMenuOpen = !$rootScope.nABHMenuOpen;
$rootScope.nursingMenuOpen   = false;
};

$rootScope.toggleEHRMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.eHRMenuOpen = !$rootScope.eHRMenuOpen;
$rootScope.nABHMenuOpen   = false;
};

$rootScope.toggletraMMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.trMMenuOpen = !$rootScope.trMMenuOpen;
$rootScope.eHRMenuOpen = false;
};

$rootScope.toggleHDMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.hDMenuOpen = !$rootScope.hDMenuOpen;
$rootScope.trMMenuOpen = false;
};

$rootScope.togglenewBornMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.newBornMenuOpen = !$rootScope.newBornMenuOpen;
$rootScope.hDMenuOpen  = false;
};

$rootScope.toggleLISOptionMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.lISOptionMenuOpen = !$rootScope.lISOptionMenuOpen;
};

$rootScope.togglePhlebotomyMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.phlebotomyMenuOpen = !$rootScope.phlebotomyMenuOpen;
};

$rootScope.toggleCentralRMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.centralRMenuOpen = !$rootScope.centralRMenuOpen;
$rootScope.phlebotomyMenuOpen  = false;
};

$rootScope.toggleConsolidatedMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.consolidatedMenuOpen = !$rootScope.consolidatedMenuOpen;
$rootScope.centralRMenuOpen  = false;
};

$rootScope.toggleBiochemistryMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.biochemistryMenuOpen = !$rootScope.biochemistryMenuOpen;
$rootScope.consolidatedMenuOpen  = false;
};

$rootScope.toggleSerologyMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.serologyMenuOpen = !$rootScope.serologyMenuOpen;
$rootScope.biochemistryMenuOpen  = false;
};

$rootScope.toggleHematologyMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.hematologyMenuOpen = !$rootScope.hematologyMenuOpen;
$rootScope.serologyMenuOpen  = false;
};

$rootScope.toggleClinicalPMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.clinicalMenuOpen = !$rootScope.clinicalMenuOpen;
$rootScope.hematologyMenuOpen  = false;
};

$rootScope.toggleHistopathologyMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.histopathologyMenuOpen = !$rootScope.histopathologyMenuOpen;
$rootScope.clinicalMenuOpen   = false;
};

$rootScope.toggleLISTemplateMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.lisTMenuOpen = !$rootScope.lisTMenuOpen;
$rootScope.histopathologyMenuOpen  = false;
};

$rootScope.toggleMicrobiologyMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.microMenuOpen = !$rootScope.microMenuOpen;
$rootScope.lisTMenuOpen  = false;
};

$rootScope.toggleOutSourceMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.outSourceMenuOpen = !$rootScope.outSourceMenuOpen;
$rootScope.microMenuOpen  = false;
};

$rootScope.toggleTrendAMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.trendAMenuOpen = !$rootScope.trendAMenuOpen;
$rootScope.outSourceMenuOpen  = false;
};

$rootScope.toggleRISMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.risMenuOpen = !$rootScope.risMenuOpen;
$rootScope.lISOptionMenuOpen  = false;
};

$rootScope.toggleClinicalSMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.clinicalMenuOpen = !$rootScope.clinicalMenuOpen;
$rootScope.risMenuOpen  = false;
};

$rootScope.toggleHemodialysisMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.hemodialysisMenuOpen = !$rootScope.hemodialysisMenuOpen;
};

$rootScope.toggleDiagnosticCSMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.diagnosticCSMenuOpen = !$rootScope.diagnosticCSMenuOpen;
$rootScope.hemodialysisMenuOpen  = false;
};

$rootScope.togglePhysiotherapyMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.physiotherapyMenuOpen = !$rootScope.physiotherapyMenuOpen;
$rootScope.diagnosticCSMenuOpen  = false;
};

$rootScope.toggleCSSDClickMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.physiotherapyMenuOpen = !$rootScope.physiotherapyMenuOpen;
$rootScope.physiotherapyMenuOpen  = false;
};

$rootScope.cssdCliMenuOpen = function () {
    $rootScope.commonFunctionClose();
$rootScope.cssdCliMenuOpen = !$rootScope.cssdCliMenuOpen;
};

$rootScope.togglePreStMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.preStMenuOpen = !$rootScope.preStMenuOpen;
};

$rootScope.toggleSterilizationPMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.sterilizationPMenuOpen = !$rootScope.sterilizationPMenuOpen;
$rootScope.preStMenuOpen  = false;
};

$rootScope.toggleOTIMSMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.otimsMenuOpen = !$rootScope.otimsMenuOpen;
$rootScope.cssdCliMenuOpenClick   = false;
};

$rootScope.toggledietKMenuOpenMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.dietKMenuOpen = !$rootScope.dietKMenuOpen;
$rootScope.otimsMenuOpen   = false;
};

$rootScope.toggleTransfersMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.transferOPtionMenuOpen = !$rootScope.transferOPtionMenuOpen;
$rootScope.admissionMenuOpen =false;
};

$rootScope.toggleCSSDMenuO = function () {
    $rootScope.commonFunctionClose();
$rootScope.cssdCliMenuOpenClick = !$rootScope.cssdCliMenuOpenClick;
$rootScope.physiotherapyMenuOpen  =false;
$rootScope.clinicalMenuOpen   =false;
};

$rootScope.togglehouseKeepingFunMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.housekeepFuMenuOpen = !$rootScope.housekeepFuMenuOpen;
$rootScope.dietKMenuOpen   =false;
};

$rootScope.toggleMortuaryMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.mortuaryMenuOpen = !$rootScope.mortuaryMenuOpen;
$rootScope.housekeepFuMenuOpen    =false;
};

$rootScope.toggleProcurementMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.procurementMenuOpen = !$rootScope.procurementMenuOpen;
$rootScope.mortuaryMenuOpen    =false;
};

$rootScope.togglePurchaseMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.purchaseMenuOpen = !$rootScope.purchaseMenuOpen;
};

$rootScope.toggleQuotationMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.quotationMenuOpen = !$rootScope.quotationMenuOpen;
$rootScope.purchaseMenuOpen    =false;
};

$rootScope.toggleWorkOrderMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.workOrderMenuOpen = !$rootScope.workOrderMenuOpen;
$rootScope.quotationMenuOpen    =false;
};

$rootScope.toggleConsignmentMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.consignmentMenuOpen = !$rootScope.consignmentMenuOpen;
$rootScope.workOrderMenuOpen    =false;
};

$rootScope.toggleLoanINOutMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.loanInOutMenuOpen = !$rootScope.loanInOutMenuOpen;
$rootScope.consignmentMenuOpen    =false;
};

$rootScope.toggleStoreInMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.storeInMenuOpen = !$rootScope.storeInMenuOpen;
$rootScope.loanInOutMenuOpen    =false;
$rootScope.procurementMenuOpen     =false;
};

$rootScope.toggleStoreMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.storeMenuOpenop = !$rootScope.storeMenuOpenop;
$rootScope.storeInMenuOpen    =false;
};

$rootScope.toggleStockAdjMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.stockAdjMenuOpen = !$rootScope.stockAdjMenuOpen;
$rootScope.storeMenuOpenop    =false;
};

$rootScope.toggleStockFunMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.stockFunMenuOpen = !$rootScope.stockFunMenuOpen;
$rootScope.stockAdjMenuOpen    =false;
};

$rootScope.toggleScrapMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.scrapMenuOpen = !$rootScope.scrapMenuOpen;
$rootScope.stockFunMenuOpen    =false;
};

$rootScope.togglePharmacyFunMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.pharmacyFunMenuOpen = !$rootScope.pharmacyFunMenuOpen;
};

$rootScope.toggleIPPharMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.ipPharmacyMenuOpen = !$rootScope.ipPharmacyMenuOpen;
};

$rootScope.toggleOPPharmMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.oppharmMenuOpen = !$rootScope.oppharmMenuOpen;
$rootScope.ipPharmacyMenuOpen    =false;
};

$rootScope.toggleNarcoticsStoreMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.narcoticsStoreMenuOpen = !$rootScope.narcoticsStoreMenuOpen;
$rootScope.oppharmMenuOpen    =false;
};

$rootScope.toggleInPatientPharmMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.inPatientPharmMenuOpen = !$rootScope.inPatientPharmMenuOpen;
$rootScope.narcoticsStoreMenuOpen    =false;
};

$rootScope.toggleAssetsFunMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.assetFunMenuOpen = !$rootScope.assetFunMenuOpen;
$rootScope.inPatientPharmMenuOpen    =false;
$rootScope.pharmacyFunMenuOpen     =false;
};

$rootScope.toggleIndentMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.indentFunMenuOpen = !$rootScope.indentFunMenuOpen;
};

$rootScope.toggleFixedAssetMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.fixiedAssetOBMenuOpen = !$rootScope.fixiedAssetOBMenuOpen;
$rootScope.indentFunMenuOpen =false;
};

$rootScope.toggleEquipmentsMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.equipmentsMenuOpen = !$rootScope.equipmentsMenuOpen;
$rootScope.indentFunMenuOpen =false;
$rootScope.assetFunMenuOpen  =false;
};

$rootScope.togglePreventiveMainMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.preventiveMMenuOpen = !$rootScope.preventiveMMenuOpen;
};

$rootScope.toggleCoreectiveMenuOpenMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.coreectiveMenuOpen = !$rootScope.coreectiveMenuOpen;
$rootScope.preventiveMMenuOpen   =false;
};

$rootScope.toggleBackOfficeMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.backOfficeMenuOpen = !$rootScope.backOfficeMenuOpen;
$rootScope.equipmentsMenuOpen    =false;
};

$rootScope.toggleCashDeskMMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.cashDeskMMenuOpen = !$rootScope.cashDeskMMenuOpen;
};

$rootScope.toggleFundCollectionMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.fundcollectionMenuOpen = !$rootScope.fundcollectionMenuOpen;
$rootScope.cashDeskMMenuOpen =false;
};

$rootScope.toggleCompanysettMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.companysettMenuOpen = !$rootScope.companysettMenuOpen;
$rootScope.fundcollectionMenuOpen  =false
};

$rootScope.toggleDoctorShareMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.doctorShareMenuOpen = !$rootScope.doctorShareMenuOpen;
$rootScope.companysettMenuOpen  =false
};

$rootScope.toggleLineLaundaryMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.lineLMenuOpen = !$rootScope.lineLMenuOpen;
$rootScope.doctorShareMenuOpen   =false
$rootScope.backOfficeMenuOpen    =false
};

$rootScope.toggleBloodBankFunMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.bloodBanckFunMenuOpen = !$rootScope.bloodBanckFunMenuOpen;
$rootScope.lineLMenuOpen     =false
};

$rootScope.toggleDonorMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.donorMenuOpen = !$rootScope.donorMenuOpen;
};

$rootScope.toggleBloodBankProMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.bloodBProMenuOpen = !$rootScope.bloodBProMenuOpen;
$rootScope.donorMenuOpen     =false
};

$rootScope.toggleIVFMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.ivfMenuOpen = !$rootScope.ivfMenuOpen;
$rootScope.bloodBanckFunMenuOpen      =false
};

$rootScope.toggleQMSFunMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.qmsFunMenuOpen = !$rootScope.qmsFunMenuOpen;
$rootScope.ivfMenuOpen      =false
};

$rootScope.toggleGlobalMasterMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.gobalMasterMenuOpen = !$rootScope.gobalMasterMenuOpen;
$rootScope.qmsFunMenuOpen       =false
};

$rootScope.toggleCommonFunMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.commonMenuOpen = !$rootScope.commonMenuOpen;
};

$rootScope.toggleOrganizationStruFunMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.organizationStruMenuOpen = !$rootScope.organizationStruMenuOpen;
$rootScope.commonMenuOpen =false
};

$rootScope.togglePatientRFuncMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.patientRFunctionMenuOpen = !$rootScope.patientRFunctionMenuOpen;
$rootScope.organizationStruMenuOpen =false
};

$rootScope.toggleADTFunctionMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.adtFuntionMenuOpen = !$rootScope.adtFuntionMenuOpen;
$rootScope.patientRFunctionMenuOpen =false
};

$rootScope.toggleLISFunctionMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.lisFunMenuOpen = !$rootScope.lisFunMenuOpen;
$rootScope.adtFuntionMenuOpen =false
};

$rootScope.toggleBillingMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.billingFunctionMenuOpen = !$rootScope.billingFunctionMenuOpen;
$rootScope.ipdMenuOpen   = false;
$rootScope.transferOPtionMenuOpen  = false;
$rootScope.lisFunMenuOpen   = false;
};


$rootScope.togglePharmacyFunctionGMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.pharmacyFunctionGMenuOpen = !$rootScope.pharmacyFunctionGMenuOpen;
$rootScope.billingFunctionMenuOpen   = false;
};

$rootScope.toggleClinicalFunctionGMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.clinicalFunctionGMenuOpen = !$rootScope.clinicalFunctionGMenuOpen;
$rootScope.pharmacyFunctionGMenuOpen   = false;
};

$rootScope.toggleDietAndKitFunctionMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.dietAndKitFunctionMenuOpen = !$rootScope.dietAndKitFunctionMenuOpen;
$rootScope.clinicalFunctionGMenuOpen    = false;
};

$rootScope.toggleAssetMagGMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.assetMagGMenuOpen = !$rootScope.assetMagGMenuOpen;
$rootScope.dietAndKitFunctionMenuOpen     = false;
};

$rootScope.toggleEquipmentMasterGMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.equipmentMasterGMenuOpen = !$rootScope.equipmentMasterGMenuOpen;
$rootScope.assetMagGMenuOpen      = false;
};

$rootScope.toggleUnitMasterMainMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.unitMasterMainMenuOpen = !$rootScope.unitMasterMainMenuOpen;
$rootScope.assetMagGMenuOpen      = false;
$rootScope.gobalMasterMenuOpen       = false;
};

$rootScope.toggleOrganizationStruUnitMMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.organizationStrucUnitMMenuOpen = !$rootScope.organizationStrucUnitMMenuOpen;
};

$rootScope.toggleADTUnitMasterMainMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.aDTUnitMasterMainMenuOpen = !$rootScope.aDTUnitMasterMainMenuOpen;
$rootScope.organizationStrucUnitMMenuOpen        = false;
};

$rootScope.toggleCSSDUnitMasterMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.cSSDUnitMasterMenuOpen = !$rootScope.cSSDUnitMasterMenuOpen;
$rootScope.aDTUnitMasterMainMenuOpen = false;
};

$rootScope.toggleNursingUnitMasterMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.nursingUnitMasterMenuOpen = !$rootScope.nursingUnitMasterMenuOpen;
$rootScope.cSSDUnitMasterMenuOpen = false;
};

$rootScope.toggleLISUnitMasterMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.lISUnitMasterMenuOpen = !$rootScope.lISUnitMasterMenuOpen;
$rootScope.nursingUnitMasterMenuOpen = false;
};

$rootScope.toggleBillingUnitMasterMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.billingUnitMasterMenuOpen = !$rootScope.billingUnitMasterMenuOpen;
$rootScope.lISUnitMasterMenuOpen = false;
};

$rootScope.togglePackageConfMasterMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.packageConfMasterMenuOpen = !$rootScope.packageConfMasterMenuOpen;
$rootScope.billingUnitMasterMenuOpen = false;
};

$rootScope.togglePharmacyUnitMasterMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.pharmacyUnitMasterMenuOpen = !$rootScope.pharmacyUnitMasterMenuOpen;
$rootScope.packageConfMasterMenuOpen = false;
};

$rootScope.toggleClinicalUnitMasterMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.clinicalUnitMasterMenuOpen = !$rootScope.clinicalUnitMasterMenuOpen;
$rootScope.pharmacyUnitMasterMenuOpen = false;
};

$rootScope.toggleOTIMSFunctionMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.oTIMSUnitMasterMenuOpen = !$rootScope.oTIMSUnitMasterMenuOpen;
$rootScope.clinicalUnitMasterMenuOpen = false;
};

$rootScope.toggleAssetMangUnitMasterMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.assetManageUnitMasterMenuOpen = !$rootScope.assetManageUnitMasterMenuOpen;
$rootScope.oTIMSUnitMasterMenuOpen = false;
};

$rootScope.toggleCommonUnitMasterMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.commonUnitMasterMenuOpen = !$rootScope.commonUnitMasterMenuOpen;
$rootScope.assetManageUnitMasterMenuOpen = false;
};

$rootScope.toggleMortuaryUnitMasterMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.mortuaryUnitMasterMenuOpen = !$rootScope.mortuaryUnitMasterMenuOpen;
$rootScope.commonUnitMasterMenuOpen = false;
};

$rootScope.toggleSchedulingUnitMasterMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.schedulingUnitMasterMenuOpen = !$rootScope.schedulingUnitMasterMenuOpen;
$rootScope.mortuaryUnitMasterMenuOpen = false;
};

$rootScope.toggleScheduleBloockUnUbUnitMasterMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.scheduleBlockUnblockMenuOpen = !$rootScope.scheduleBlockUnblockMenuOpen;
$rootScope.schedulingUnitMasterMenuOpen = false;
};

$rootScope.toggleConfigurationUnitMasterMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.configurationUnitMasterMenuOpen = !$rootScope.configurationUnitMasterMenuOpen;
$rootScope.scheduleBlockUnblockMenuOpen = false;
};

$rootScope.toggleUserManagementUnitMasterMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.userManagementUnitMasterMenuOpen = !$rootScope.userManagementUnitMasterMenuOpen;
$rootScope.configurationUnitMasterMenuOpen = false;
};

$rootScope.toggleTemplateUtilityUnitMasterMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.templateUtilityUnitMasterMenuOpen = !$rootScope.templateUtilityUnitMasterMenuOpen;
$rootScope.userManagementUnitMasterMenuOpen = false
};

$rootScope.toggleBloodBankUnitMasterMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.bloodBankUnitMasterMenuOpen = !$rootScope.bloodBankUnitMasterMenuOpen;
$rootScope.templateUtilityUnitMasterMenuOpen = false
};

$rootScope.toggleLoyaltyCardUnitMasterMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.loyaltyCardUnitMasterMenuOpen = !$rootScope.loyaltyCardUnitMasterMenuOpen;
$rootScope.bloodBankUnitMasterMenuOpen = false
};

$rootScope.toggleInventoryUnitMasterMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.InventoryUnitMasterMenuOpen = !$rootScope.InventoryUnitMasterMenuOpen;
$rootScope.loyaltyCardUnitMasterMenuOpen = false
};

$rootScope.togglefundmanagemeUnitMasterMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.fundmanagemeUnitMasterMenuOpen = !$rootScope.fundmanagemeUnitMasterMenuOpen;
$rootScope.InventoryUnitMasterMenuOpen = false
};

$rootScope.toggleAmbulanceServiceMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.ambulanceServiceMenuOpen = !$rootScope.ambulanceServiceMenuOpen;
$rootScope.fundmanagemeUnitMasterMenuOpen = false
};

$rootScope.toggleVisitorTypeUnitMasterMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.visitorTypeUnitMasterMenuOpen = !$rootScope.visitorTypeUnitMasterMenuOpen;
$rootScope.ambulanceServiceMenuOpen = false
};

$rootScope.toggleQMSUnitMasterMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.qMSUnitMasterMenuOpen = !$rootScope.qMSUnitMasterMenuOpen;
$rootScope.visitorTypeUnitMasterMenuOpen = false
};

$rootScope.toggleincidentReportingUnitMasterMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.incidentReportingMenuOpen = !$rootScope.incidentReportingMenuOpen;
$rootScope.qMSUnitMasterMenuOpen = false
};

$rootScope.toggleAIButtonConfigurationMenu = function () {
    $rootScope.commonFunctionClose();
$rootScope.aIButtonConfigurationMenuOpen = !$rootScope.aIButtonConfigurationMenuOpen;
$rootScope.incidentReportingMenuOpen = false
};



// ArrowCodeEnd




	// added for billing
	$rootScope.parseFloatNum = function(number){
		return (number != '' && typeof(number) != 'undefined' && !isNaN(number) && number != null) ? parseFloat(number).toFixed(2) : parseFloat(0).toFixed(2);
	}
	$rootScope.parseFloatNumTmp = function(number){
		return parseFloat(number).toFixed(2);
	}
	
	
	$rootScope.getFormattedDate = function(date,isTime){
		if(date != null && date != ""){
		var tempDate = new Date(date);
		var date = tempDate.getDate().toString().length == 1 ? "0"+tempDate.getDate().toString() : tempDate.getDate().toString();
		var month =  (tempDate.getMonth() + 1).toString().length == 1 ? "0"+(tempDate.getMonth() + 1).toString() : (tempDate.getMonth() + 1).toString();
		var year = tempDate.getFullYear();
		if(isTime)
			return date + "-" + month + "-" + year + " "+tempDate.getHours()+":"+tempDate.getMinutes()+":"+tempDate.getSeconds();
		else
			return date + "-" + month + "-" + year;
		}else{
			return "";
		}
	}
	$rootScope.getFormattedDateMisc = function(date,isTime){
		if(date != null && date != ""){
		var tempDate = new Date(date);
		var date = tempDate.getDate().toString().length == 1 ? "0"+tempDate.getDate().toString() : tempDate.getDate().toString();
		var month =  (tempDate.getMonth() + 1).toString().length == 1 ? "0"+(tempDate.getMonth() + 1).toString() : (tempDate.getMonth() + 1).toString();
		var year = tempDate.getFullYear();
		if(isTime)
			return year + "-" + month + "-" + date + " "+tempDate.getHours()+":"+tempDate.getMinutes()+":"+tempDate.getSeconds();
		else
			return  year + "-" + month + "-" + date;
		}else{
			return "";
		}
	}
	
	$rootScope.getVisitTypeById = function(id){
		var visitType = "";
		if(id == 1)
			visitType = "OP";
		else if (id == 2)
			visitType = "IP";
		else if (id == 3)
			visitType = "Day Care";
		else if (id == 4)
			visitType = "ER";
		
		return visitType;
		
	}
	
	$rootScope.getBillStatusById = function(id){
		var status = "";
		if(id == 1)
			status = "Pending";
		else if (id == 2)
			status = "OnHold";
		else if (id == 3)
			status = "Settled";
		
		return status;
	}
	$rootScope.getDepositStatusById = function(id){
		var status = "";
		if(id == 1)
			status = "Pending";
		else if (id == 2)
			status = "Partially Collected";
		else if (id == 3)
			status = "Collected";
		
		return status;
	}
	
	$rootScope.getRefundStatus = function(id){
		var status = "";
		if(id == 1)
			status = "Pending";
		else if (id == 2)
			status = "Partially Collected";
		else if (id == 3)
			status = "Completed";
		else if (id == 4)
        	status = "Cancelled";
		return status;
	}
	
	$rootScope.getOrderStatus = function(id){
		var status = "";
		if(id == 1)
			status = "Pending";
		else if (id == 2)
			status = "Partially Completed";
		else if (id == 3)
			status = "Completed";
		else if (id == 4)
			status = "Cancelled";
		return status;
	}
	
	$rootScope.getAppointmentStatus = function(id){
		var status = "";
		if(id == 1)
			status = "Pending";
		else if (id == 5)
			status = "Checked In";
		else if (id == 6)
			status = "Completed";
		return status;
	}
	
	$rootScope.capitalize_Words=function(str)
	{
	 return str.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
	}
	$rootScope.updateTemplateId = null;
	/*
	 * Added By Jyoti 04-10-2017
	 */
	
	// SANTOSH FOR COMMAN TAB CLICKS
	 /** ***** START TAB FOR ALL PAGES IN TREE LEVEL ****** */
	/* First Level */
	$rootScope.firstTabLevel = 1 ;
	$rootScope.setFirstTabLevel= function(newTab){
		$rootScope.firstTabLevel = newTab;
	};
	$rootScope.isSetFirstTabLevel = function(tabNum){
		return $rootScope.firstTabLevel === tabNum;
	};
	
	/* Second Level */
	$rootScope.secondTabLevel = 1 ;
	$rootScope.setSecondTabLevel = function(newTab){
		$rootScope.secondTabLevel = newTab;
	};
	$rootScope.isSetSecondTabLevel = function(tabNum){
		return $rootScope.secondTabLevel === tabNum;
	};
	
	/* Third Level */
	$rootScope.thirdTabLevel = 1 ;
	$rootScope.setThirdTabLevel = function(newTab){
		$rootScope.thirdTabLevel = newTab;
	};
	$rootScope.isSetThirdTabLevel = function(tabNum){
		return $rootScope.thirdTabLevel === tabNum;
	};
	
	/* FORTH Level */
	$rootScope.forthTabLevel = 1 ;
	$rootScope.setForthTabLevel = function(newTab){
		$rootScope.forthTabLevel = newTab;
	};
	$rootScope.isSetForthTabLevel = function(tabNum){
		return $rootScope.forthTabLevel === tabNum;
	};
	
	 
	
	
	$rootScope.initCounterObj = function()
	{
   	 $rootScope.counterMstObj = {};
   	 $rootScope.counterMstObj.counterId = "";
   	 $rootScope.counterMstObj.counterDesc = "";
	}
	
	/** @Code Counter Selection */
	$rootScope.selectCounter = function(obj)
	{
		if(obj == null || obj == undefined || obj == ''){
			$rootScope.initCounterObj();
			$rootScope.counterMstObj = null;
			sessionStorage.setItem('counterMstObj', JSON.stringify($rootScope.counterMstObj));
		}else{
		var counterObj = JSON.parse(obj);
		$rootScope.initCounterObj();
		$rootScope.counterMstObj.counterId = counterObj.id;
		$rootScope.counterMstObj.counterDesc =counterObj.name;
		sessionStorage.setItem('counterMstObj', JSON.stringify($rootScope.counterMstObj));
		$rootScope.counterId = counterObj.id;
		var cookieObject = $cookies.getObject('cookieObject');
		cookieObject.counterId = $rootScope.counterId;
		$cookies.putObject('cookieObject',cookieObject);
		}
		
	};
	// $rootScope.counterMstObj =
	$rootScope.getCounterDetails = function()
	{
		    	 
		         $rootScope.counterMstObj = JSON.parse(sessionStorage.getItem('counterMstObj'));
		         $rootScope.counterList = JSON.parse(localStorage.getItem('counterList'));
				 if($rootScope.counterMstObj==undefined||$rootScope.counterMstObj.counterId==null||$rootScope.counterMstObj.counterId=="")
				  {
					 if($rootScope.counterList != undefined && $rootScope.counterList.length !=0){
						 
						 if($rootScope.counterList.length >1)
						 {
							 angular.element('#counterDetailsModal').modal('show');
							 return false;
						 }else{
							    $rootScope.counterMstObj={};
							 	$rootScope.counterMstObj.counterId = $rootScope.counterList[0].id;
						   	    $rootScope.counterMstObj.counterDesc = $rootScope.counterList[0].name;
						   	 return true;
						 }
					 }
					
					
				  }
				 else{
					 return true;
				 }
				 
	};
	
	$rootScope.confirmCounter = function()
	 {
		$rootScope.counterMstObj = JSON.parse(sessionStorage.getItem('counterMstObj'));
		if ($rootScope.counterMstObj != undefined) {
		$rootScope.counterId = $rootScope.counterMstObj.counterId!=""?$rootScope.counterMstObj.counterId:"";
   	    $rootScope.counterDesc = $rootScope.counterMstObj.counterDesc!=""?$rootScope.counterMstObj.counterDesc:"";
   	    }
   	 
	 }
	
/** ***** END TAB FOR ALL PAGES IN TREE LEVEL ****** */
/** COMMON CHECK FOR COOKIES * */
	$rootScope.checkCookies = function()
	{
		var cookieObject = $cookies.getObject('cookieObject');
		if(cookieObject == undefined){
			$state.go('login');
			return [];
		}
		var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
		if(cookieObjectAppConfig == undefined){
			$state.go('applicationConfiguration');
			return [];
		}
		var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
		if (cookieObjectAppConfigSecond != undefined) {
		$rootScope.invoiceCreationInsurance = cookieObjectAppConfigSecond.invoiceCreationInsurance;$rootScope.invoiceCreationCorporate = cookieObjectAppConfigSecond.invoiceCreationCorporate;
		}
		$rootScope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
		$rootScope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
		$rootScope.isAppointmentRequiredForMenu = cookieObjectAppConfig.isAppointmentRequired;
		
		$rootScope.unitId = cookieObject.unitId;
		$rootScope.organizationId = cookieObject.organizationId;
		$rootScope.userId = cookieObject.userId;
		if (cookieObjectAppConfigSecond != undefined) {
		$rootScope.navBarImg = cookieObjectAppConfigSecond.navBarImg;
		$rootScope.accessOfAbdm = cookieObjectAppConfigSecond.accessOfAbdm;
		$rootScope.ayushmanBharatIntegration = cookieObjectAppConfigSecond.ayushmanBharatIntegration;
		}
		return [cookieObject, cookieObjectAppConfig];
	}
	
	

	/** @Code Added By Ganesh Chaudhari 30/07/18 11:18:06 */
	$rootScope.checkLabConfig = function(){
	 var labConfig = (JSON.parse(localStorage.getItem('labConfig'))==undefined||JSON.parse(localStorage.getItem('labConfig'))==null)?{}:JSON.parse(localStorage.getItem('labConfig'));
	 $rootScope.biochemDept = labConfig.biochemistryId;
	 $rootScope.hematologyDept = labConfig.hematologyId;
	 $rootScope.clinicalDept = labConfig.clinicalPathologyId;
	 $rootScope.microbiologyDept = labConfig.microbiologyId;
	 $rootScope.histopathologyDept = labConfig.histopathologyId;
	 $rootScope.cystopathologyId = labConfig.cystopathologyId;
	 $rootScope.serologyId = labConfig.serologyId;
	 $rootScope.isPrebarcodeGenerantion = labConfig.isPrebarcodeGenerantion;
	 $rootScope.isSendToCrApplicable= labConfig.isSendToCrApplicable;
	 $rootScope.isSampleAcceptanceApplicable= labConfig.isSampleAcceptanceApplicable;
   	 $rootScope.isAcceptanceInDepartment= labConfig.isAcceptanceInDepartment;
   	 $rootScope.isWorklistDepartment= labConfig.isWorklistDepartment;
   	 $rootScope.consolidatedResultEntry= labConfig.consolidatedResultEntry;
	 if(labConfig == null||labConfig==undefined){
			$state.go('login');
			return {};
		}
	  return labConfig;
	}
	$rootScope.checkLabConfig();
	
	$rootScope.getUnitByOrganization = function()
	{
		var cookieObject = $cookies.getObject('cookieObject');
		var URI = BASE_URL + ROOT_URL+ "/unit/org/"+cookieObject.organizationId;
		GenericService.serviceAction("GET", URI, {}).then(function(response) 
			{
					if (response.data.status == "success") {
						$rootScope.stopLoader();
						$rootScope.unitMasterList = response.data.listObject;
			}else if (response.data.status == "error") 
			{
				$rootScope.stopLoader();
			}	
		});
	}
	$rootScope.getUserListByRoleType = function(unitId,orgId) {
		var data = {
			unitId : unitId,
			organisationId : orgId,
			roleTypeId : 14 // for pathologist
		};
		var URI = BASE_URL + ROOT_URL+ "/api/global/getUserByRoleType";
		$rootScope.userListByRoleType=[];
		GenericService.serviceAction("POST",URI,data)
				.then(function(response) {
							if (response.data.status == "success"){
								$rootScope.userListByRoleType = response.data.listObject;
							}
		 });
	};	
	
	
	$rootScope.updateBedMagementCount = function()
	{
		var cookieObject = $cookies.getObject('cookieObject');
		var URI = BASE_URL + ROOT_URL+ "/api/global/bedMagement/count/"+cookieObject.unitId;
		GenericService.serviceAction("GET", URI, {}).then(function(response) 
			{});
	}
	 // code start for user login log
	
	$rootScope.allergydata=function(patientDetails){
		var cookieObject = $cookies.getObject('cookieObject');
		var headerObj = {
				unit_id : cookieObject.unitId,
				organization_id : cookieObject.organizationId
			};
		$rootScope.dt = moment(new Date()).format('DD-MM-YYYY');
			var paramObjForPer = {
				patientId :  patientDetails.patientId,
				admissionId: patientDetails.admissionId,
				roleId : 3
			};
			
			var paramObjForHistList = {
					patientId :  patientDetails.patientId,
				};
			var data = {};
		
			var data = {};
		var URI = BASE_URL + ROOT_URL + "/api/opd/getPatientAllergyListForIcon";
	GenericServiceParamHeader.serviceAction(METHOD_GET, URI,data,
				NOTIFICATION_MSG_STATUS_FALSE,paramObjForHistList,headerObj)
		.then(
				function(response) {
					console.log(response);
					if (response.data.status == "success")
						$rootScope.activeAllergyListForIcon = response.data.listObject;
					console.log("list for allergy icon",$rootScope.activeAllergyListForIcon)
					if($rootScope.activeAllergyListForIcon.length > 0)
					{
						$rootScope.allergySymbolList=[];
					   angular.forEach($rootScope.activeAllergyListForIcon,function(value,index){
						   if(value.isNoKnownAllergies !='Y'){
							   $rootScope.allergySymbolList.push(value);
						   }
						   if($rootScope.allergySymbolList.length > 0){
							   $rootScope.allergyFlag = true;
						   }else
						   {
								 $rootScope.allergyFlag = false;
							}
						  
					   });
						
					}else{
						 $rootScope.allergyFlag = false;
					}
					
				});
		}
	function checkUserLogin()
	{
		var cookieMacAdress =localStorage.getItem("macAddress");
		var cookieObject = $cookies.getObject('cookieObject');
		if(cookieMacAdress==undefined || cookieObject==undefined){
			return false;
		}
		var data={
				userId:cookieObject.userId,
				unitId:cookieObject.unitId,
				organizationId:cookieObject.organizationId,
				macAddress:cookieMacAdress
		 }
		console.log("check mac",data);
		var URI = BASE_URL + ROOT_URL+ "/adt/checkOtherLogin";
		GenericService.serviceAction("POST", URI, data).then(function(response) 
			{
					if (response.data.status == "success" && response.data.listObject.length>0) {
						
						var list=response.data.listObject;
						for(var i=0;i<list.length;i++){
							var URI1 = BASE_URL + ROOT_URL+ "/adt/updatePreviousLogin";
							var data1={
									userId:list[i].userId,
									unitId:list[i].unitId,
									macAddress:list[i].macAddress,
									organizationId:list[i].organizationId,
									logId:list[i].logId
								}
							console.log("update Previous log dto",data1);
							GenericService.serviceAction("POST", URI1, data1).then(function(response) 
								{
										if (response.data.status == "success") {
											
											// Fix, fix/session-token-validation-instability -- removed erroneous
											// $cookies.remove("cookieObject") that used to be here. This callback
											// only archives THIS device's own already-closed-out prior login
											// records (CHECK_OTHER_LOGIN filters on this device's own macAddress
											// plus isLogin='N'/isLogout='Y'/status='C', i.e. sessions already
											// logged out) as routine housekeeping every 90s -- clearing the
											// CURRENT session's cookie here silently logged out the active
											// session as a side effect of unrelated archival cleanup, misreported
											// to the user as "Token Expired!".
											
//											$rootScope.stopLoader();
								}else if (response.data.status == "error") 
								{
//									$rootScope.stopLoader();
								}	
							});
						}
				

						
						
			}else if (response.data.status == "error") 
			{
//				$rootScope.stopLoader();
			}	
		});
	}
	 $interval(checkUserLogin, 90000);
	
	 // code end for user login log
	/*
	 * *********************************=====================
	 */
	// Pasword authetication
	$rootScope.emailId = "";
	$rootScope.unitName = "";
	$rootScope.otp = "";
	$rootScope.existingPassword = ""; 
	$rootScope.password = ""; 
	$rootScope.confirmPassword = "";
	$rootScope.showForgetPasswordModal = function()
	{
		$rootScope.existingPassword = ""; 
		$rootScope.password = ""; 
		$rootScope.confirmPassword = "";
		$rootScope.emailId = "";
		$rootScope.otp = "";
		$('#forgotPasswordModal').modal('show');
		
	}
	
	
	/*
	 * $rootScope.getUnitName = function() { $rootScope.startLoader();
	 * $rootScope.unitList = []; if(this.emailId!=null && this.emailId !="") {
	 * var URI = BASE_URL + ROOT_URL+ "/adt/getUnitName"; var data = { emailId :
	 * this.emailId } GenericService.serviceAction("POST", URI,
	 * data).then(function(response) { if (response.data.status == "success") {
	 * $rootScope.stopLoader(); $rootScope.unitList = response.data.listObject;
	 * console.log('$rootScope.unitList',$rootScope.unitList);
	 * $rootScope.stopLoader(); }else if (response.data.status == "error") {
	 * $rootScope.stopLoader(); } }); } }
	 */
	
		
	$rootScope.unitObj = {};
	$rootScope.getUserObj = function(unitID)
	{
		$rootScope.startLoader();
		angular.forEach(this.unitList, function(value, key) {
			if(value.unitId==unitID)
				{
				$rootScope.unitObj =value;
				  console.log('this.unitObj',$rootScope.unitObj);
				  $rootScope.stopLoader();
				}
			else{
				$rootScope.stopLoader();
			}
			});
	}
	
	$rootScope.getOTP = function () 
	{
		if(this.emailId== undefined || this.emailId == null || this.emailId == ""){
			growl.warning("Please enter email Id.",{
				title : "warning"
			});
			return false;
		}
		 var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
   		 var email = this.emailId;
   		    if (!filter.test(email)) {
			    growl.warning("Please enter valid email Id",{
					title : "warning"
				});
			    return false;
   		    }
	
		/*
		 * if(this.unitId=="-" || this.unitId==undefined || this.unitId==null){
		 * growl.warning("Please select unit .",{ title : "warning" }); return
		 * false; }
		 */
		this.$broadcast('show-errors-check-validity');
		if (this.forgetPassworedForm.$valid)
		{
		$rootScope.startLoader();
		if(this.emailId!=null && this.emailId !="") {
// $('#forgotPasswordModal').modal('hide');
		var data = {"emailId": this.emailId}
		var URI = BASE_URL + ROOT_URL + "/adt/checkEmailAvailability";
		GenericService.serviceAction("POST", URI, data).then(function (response) {
			if (response.data.status == "success") 
			{
				growl.success("OTP send Successfully. Please check your email.",{
					title : SUCCESS_MSG
				});
				$('#forgotPasswordModal').modal('hide');
				$('#forgotPasswordModal_enterOTP').modal('show');
				$rootScope.stopLoader();
				// $rootScope.otp="";
				// this.$broadcast('show-errors-reset');
			}
			else{
				growl.warning(response.data.message,{
					title : ERROR_MSG
				});
				$rootScope.stopLoader();
			}

		  });
		}
		}
	}
	
	

	$rootScope.OTPVerification = function () 
	{
		if(this.otp==undefined || this.otp=="" || this.otp == null){
			growl.warning("Please enter valid OTP.",{
				title : "warning"
			});
			return false;
		}
		if(this.otp.length!=4){
			growl.warning("Please enter valid OTP.",{
				title : "warning"
			});
			return false;
		}
		this.$broadcast('show-errors-check-validity');
		if (this.forgot_password_confirmPwd_form1.$valid)
		{
		$rootScope.startLoader();
// $('#forgotPasswordModal_enterOTP').modal('hide');
		var data = {"unitId": $rootScope.unitObj.unitId, 
				"emailId": this.emailId ,
				organizationId:$rootScope.unitObj.organizationId,
				userId:$rootScope.unitObj.userId,
				otp:this.otp}
		var URI = BASE_URL + ROOT_URL + "/adt/otpVerification";
		GenericService.serviceAction("POST", URI, data).then(function (response) {
			var counterList1 = [];
			if (response.data.status == "success") 
			{
				growl.success("OTP Verified Sucessfully.",{
					title : SUCCESS_MSG
				});
				$('#forgotPasswordModal_enterOTP').modal('hide');
				$('#forgotPasswordModal_newPassword').modal('show');
				
				$rootScope.stopLoader();
			
				
				$rootScope.userIdFrogetPwd=response.data.code;
// this.$broadcast('show-errors-reset');
			}
			else if (response.data.status == "error") {
				growl.warning(response.data.message,{
					title : ERROR_MSG
				});
				$rootScope.stopLoader();
			}
	    	});
		}
	}
	
	$rootScope.validatePassword = function(password) {
	      var minLength = 8;
	      var maxLength = 16;
	      var hasUppercase = /[A-Z]/.test(password);
	      var hasLowercase = /[a-z]/.test(password);
	      var hasDigit = /[0-9]/.test(password);
	      var hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);
	      var isLengthValid = password.length >= minLength && password.length <= maxLength;

	      return hasUppercase && hasLowercase && hasDigit && hasSpecialChar && isLengthValid;
	    };
	
	   
	
	$rootScope.forgotPassword = function () 
	{
		if(this.password3==undefined || this.password3=="" ||this.password3==null){
			growl.warning("Please enter password.",{
				title : "warning"
			});
			return false;
		}
		
		if(this.confirmPassword1==undefined || this.confirmPassword1=="" ||this.confirmPassword1==null){
			growl.warning("Please enter confirmation password .",{
				title : "warning"
			});
			return false;
		}
		
		if(this.password3!=this.confirmPassword1){
			growl.warning("Your password and confirmation password do not match.",{
				title : "warning"
			});
			return false;
		}
		
		$rootScope.isPasswordValid = $rootScope.validatePassword(this.password3);
		    
		    if(!$rootScope.isPasswordValid){
		    	
		    	growl.warning("Password must be 8 to 16 characters long and include uppercase letters, lowercase letters, digits, and special characters.",{
					title : ERROR_MSG
				});
		    	
		    	return false;
		    	
		    }
		
		
		$rootScope.startLoader();
		
			var URI = BASE_URL + ROOT_URL + "/adt/forgotPasswordGeneration";
			var data = {
					unitId: $rootScope.unitObj.unitId, 
					emailId: this.emailId ,
					organizationId:$rootScope.unitObj.organizationId,
// userId:$rootScope.unitObj.userId,
					userId:$rootScope.userIdFrogetPwd,
					password:this.password3,
					confirmPassword:this.confirmPassword1
					}
			
			GenericService.serviceAction("POST", URI, data).then(function (response) {
				if (response.data.status == "success") 
				{
					growl.success("Password Updated Successfully.",{
						title : SUCCESS_MSG
					});
					$('#forgotPasswordModal_newPassword').modal('hide');
					$rootScope.stopLoader();
					  window.location.reload();
				}
				else if (response.data.status == "error") {
					growl.warning(response.data.message,{
						title : ERROR_MSG
					});
					$rootScope.stopLoader();
				}
			
			});
	
			}
		
	/*
	 * $rootScope.resetPwd = function () {
	 * 
	 * $rootScope.$broadcast('show-errors-check-validity'); if
	 * ($rootScope.reset_password_confirm_form.$valid) {
	 * $rootScope.startLoader();
	 * 
	 * 
	 * var URI = BASE_URL + ROOT_URL + "/adt/resetPassword"; var userObj =
	 * $cookies.getObject('cookieObject'); var data = {
	 * 
	 * userId:userObj.userId, existingPassword:this.existingPassword,
	 * userPwd:this.password, }
	 * 
	 * GenericService.serviceAction("POST", URI, data).then(function (response) {
	 * 
	 * if (response.data.status == "success") { growl.success("Password Updated
	 * Successfully.",{ title : SUCCESS_MSG }); //
	 * this.$broadcast('show-errors-reset'); $rootScope.stopLoader(); } else if
	 * (response.data.status == "error") { growl.warning(response.data.message,{
	 * title : ERROR_MSG }); $rootScope.stopLoader(); } }); } }
	 */
	
	/** ****************global search *****sneha***** */
	
	
	// For Get Active Gender List
	$rootScope.getSexMasterList = function() {
		var URI = BASE_URL + ROOT_URL + GETACTIVEGENDERLIST;
		BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			$rootScope.sexMasterList = response.data.listObject;
			console.log($rootScope.sexMasterList);
		});
	}
	$rootScope.getIdentificationTypeList = function(){
		var URI = BASE_URL + ROOT_URL + GETACTIVEIDENTIFICATION;
		BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success")
				$rootScope.identificationTypeList = response.data.listObject;
			console.log($rootScope.identificationTypeList);
		});
	}
	$rootScope.initGlobal = function(){
		$rootScope.searchKeyword;
		$rootScope.identificationTypeList = [];
		$rootScope.sexMasterList = [];
	var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
		if(cookieObjectAppConfig != undefined && cookieObjectAppConfig != null){
		$rootScope.getSexMasterList();
		$rootScope.getIdentificationTypeList();
		}
		$rootScope.patientDetailsList = [];
		$rootScope.patientDetails = {}
	/*
	 * $scope.billing = true; $scope.deposit = {}; $scope.paymentDetails = {};
	 * $scope.initPaymentDetailsObj(); $scope.listPaymentDetails = [];
	 * $scope.getCardTypesList(); $scope.dueAmount = parseFloat(0).toFixed(2);
	 * $scope.visitNoList = [];
	 */
	}
	$rootScope.initGlobal();
	
// var unitId = cookieObject.unitId;
// var organizationId = cookieObject.organizationId;
	
	

	$rootScope.getCardTypesList = function(){
		var URI = BASE_URL + ROOT_URL + "/api/billing/cardTypeMaster";
		var param = {orgnisationId:$scope.organizationId,unitId:$scope.unitId};
		BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success"){
				$rootScope.cardTypeList = response.data.listObject;
			}
		});
	}
	var cookieObject = $cookies.getObject('cookieObject');
	$rootScope.patientSearchObj = {
		    	  "patientName" :"",
					"uhIdNumber" : "",
					"birthDate":"",
					"encounterId":"",
					"genderId":"",
					"roleId" :"",
					"patientId":"",
					"doctorId":"",
					"doctorName":"",
					"genderId":"",
					"mobileNo":"",
					"identificationNo":"",
					"patientType"   :"1",
					"unitId": $rootScope.unitId, 
					"visitTypeId" :1,
					"organizationId"  :$rootScope.organizationId
		    }
	
	// -----------//
	$rootScope.dateOptions = {
    		formatYear : 'yyyy',
			showWeeks : false,
//			
		};
	$rootScope.openDatePickerPatiSearch = function() {
		$rootScope.datepicker1.opened = true;
		};

		$rootScope.datepicker1 = {
		opened : false
		};
		
	// -----------//
	
		
	$rootScope.clearSearchData = function(){
		$rootScope.patientList=[];
		$rootScope.patientSearchObj = {
			      "uhIdNumber"      : "",
			      "patientName"     : "",
			      "genderId"        : "",
			      "mobileNo"        : "",
			      "birthDate"       : "",
			      "identificationNo":"",
			      "patientType"   :"1",
			      "unitId"          :$rootScope.unitId,
			      "organizationId"  :$rootScope.organizationId,
			    }
	}
	$rootScope.globalPatientsearch = function()
	{
		
		$rootScope.startLoaderPopup();
		
		$rootScope.patientTemplateList=[];
		// var URI = BASE_URL + ROOT_URL +
		// "/adt/patientSearchByMultipleCriteria";HIS-21596
		var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteriaWithDeathPatient";
		var patSearchObj = angular.copy($rootScope.patientSearchObj);
		patSearchObj.organizationId = $rootScope.organizationId;
		if(patSearchObj.patientName == "" && patSearchObj.birthDate == "" &&
				   patSearchObj.genderId == "" && patSearchObj.identificationNo == "" && patSearchObj.mobileNo == "" &&
				  patSearchObj.uhIdNumber == "" ){
			 growl.warning(
						"Please enter search criteria ",
						{
							title : 'Warning!'
						});
			 $rootScope.stopLoaderPopup();
			 return false;
		}
		if($rootScope.patientSearchObj.birthDate != "" && typeof($rootScope.patientSearchObj.birthDate) != "undefined"){
			var dt = new Date($rootScope.patientSearchObj.birthDate);
			patSearchObj.birthDate = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + dt.getFullYear();
		} 
		/*
		 * BillingGenericService.serviceAction(METHOD_POST,URI,
		 * patSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
		 */
		GenericService.serviceAction(METHOD_POST,URI, patSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			$rootScope.searchPatientList = [];
			if (response.data.status == "success")
			{
				$rootScope.patientList = response.data.listObject;
				patSearchObj = {};
				$rootScope.listPaymentDetails = [];
				$rootScope.patientDetailsList = [];
				$rootScope.patientDetails = {};
				$rootScope.searchPatientList = [];
				$rootScope.stopLoaderPopup();
			} else {
				$rootScope.stopLoaderPopup();
			}
			
		});
	}
	
	// for date
	$rootScope.getFormattedDate = function(date,isTime){
		var tempDate = new Date(date);
		var date = tempDate.getDate().toString().length == 1 ? "0"+tempDate.getDate().toString() : tempDate.getDate().toString();
		var month =  (tempDate.getMonth() + 1).toString().length == 1 ? "0"+(tempDate.getMonth() + 1).toString() : (tempDate.getMonth() + 1).toString();
		var year = tempDate.getFullYear();
		if(isTime)
			return date + "-" + month + "-" + year + " "+tempDate.getHours()+":"+tempDate.getMinutes()+":"+tempDate.getSeconds();
		else
			return date + "-" + month + "-" + year + " 00:00:00";
	}
	
	
	$rootScope.patientId=null;
	$rootScope.getPatientDetailsById = function(patientId){
		$rootScope.patientId=patientId;
		$rootScope.patientDetailsList = [];
		$rootScope.patientDetails = {};
		$rootScope.masterObj = {};
		$rootScope.DischargeSummaryFinalList=[];
		$rootScope.patientTemplateList=[];
		var URI = BASE_URL + ROOT_URL + "/api/global/patient/billing/"+patientId;
		BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success"){
				$rootScope.patientDetailsList = response.data.listObject;
				$rootScope.patientDetails = response.data.listObject[0];
			}
		});
	}
	$rootScope.redirectBookViewUrl = function(encounterObj)
	{
		var cookieObject = 
		{
				"patientName" :encounterObj.patientName,
				"uhIdNumber" : encounterObj.uhIdNumber,
				"genderCode" : encounterObj.genderCode,
				"age":  encounterObj.age,
				"mobileNo":encounterObj.mobileNo,
				"birthDate": encounterObj.birthDate,
				"encounterId":encounterObj.encounterId,
				"genderId":encounterObj.genderId,
				"roleId" :$rootScope.roleId,
				"userId":$rootScope.unitObj.userId,
				"patientId":encounterObj.patientId,
				"doctorId":encounterObj.doctorId,
				"specialityId":encounterObj.specialityId,
				"paymentEntitlementTypeId":encounterObj.paymentEntitlementTypeId,
				"doctorName":encounterObj.doctorName,
				"prefixId":encounterObj.prefixId,
				"genderId":encounterObj.genderId,
				"appointmentId":encounterObj.appointmentId,
				"visitTypeId" :encounterObj.visitTypeId,
				"contractId" : encounterObj.contractId,
				"payeeId" : encounterObj.payeeId,
				"patientCategoryId" : encounterObj.patientCategoryId,
				"admissionId" : encounterObj.admissionId,
				"allowedOverBookingSlots" :encounterObj.allowedOverBookingSlots
		}
		 sessionStorage.setItem('cookieEncounterObject', JSON.stringify(cookieObject));
		/*
		 * console.log("EO",cookieObject); $('#searchModal').modal('hide');
		 */
		// return false;
		$rootScope.patientList=[];
		$rootScope.patientSearchObj.patientName="";
		$('#searchModal').modal('hide');
		$state.go("bookView",	{
			  bookViewObj : encounterObj
			});
	
	}
	

	/** ****************global search end*****sneha***** */

	
	
	
	
	$rootScope.storePopupAcesss = function() {

    // var arr = permissions.modules.filter(function (item) {
    // return item.id == 101 && item.moduleType == 1;
    // });
    // return arr.length > 0 ? true : false;
		
 	// var accessId= modules.length > 0 ? modules[0].id : null;
	// var access=$rootScope.$emit("checkAccess", accessId)
	// if(access) {
	// $rootScope.storePopup();
	// }

	/* Changes done by Bharat On 12-06-2019 - Start */

	var permissions = JSON.parse(localStorage.getItem('permissions'));
	var modules = permissions != undefined ? permissions.modules.filter(x => x.id == 101 && x.moduleType == 1) : [];
	if(modules.length >= 0){
		$rootScope.storePopup();
	}

	/* Changes done by Bharat On 12-06-2019 - End */
}
	

	$rootScope.storePopup = function(){
		var cookieObject = $cookies.getObject('cookieObject');
		if(cookieObject == undefined) {
			$state.go('login');
			return;
		}
	var unitId = cookieObject.unitId;
	var organizationId = cookieObject.organizationId;
	var userId = cookieObject.userId;
		var URI = BASE_URL+ROOT_URL+"/api/global/store/getStoreListById/"+ userId+"/"+cookieObject.unitId;
			GenericService.serviceAction("GET",URI,{}).then(function(response){
			
				var stores=response.data.stores;
				if(stores.length > 1){
					$rootScope.storeList=stores;
					angular.element('#pharmacyPopupBeforLeftexpand').modal('show');
				}
				else if(stores.length==1){
					sessionStorage.setItem('selectedTopStore', JSON.stringify(stores[0]));
					var cookieObject = $cookies.getObject('cookieObject');
					cookieObject.storeId=stores[0].storeId;
					$cookies.putObject('cookieObject',cookieObject);
				}
    });

			var URI = BASE_URL + ROOT_URL
			+ "/api/global/getWardListForER/"+cookieObject.organizationId+"/"+cookieObject.unitId;
	     GenericService.serviceAction("GET", URI).then( function(response) {
			if (response.data.status == "success") {
					 sessionStorage.setItem('nursingStationsWardsListforEr', JSON.stringify(response.data.listObject));
					}
					});
	}

	$rootScope.setStore = function(storeName) {
			if(storeName) {
				sessionStorage.setItem('selectedTopStore', JSON.stringify(storeName));
				$rootScope.getStoreName();
				var storeObj= JSON.parse(sessionStorage.getItem('selectedTopStore'));
				var cookieObject = $cookies.getObject('cookieObject');
			  	cookieObject.storeId=storeObj.storeId;
				$cookies.putObject('cookieObject',cookieObject);
				angular.element('#pharmacyPopupBeforLeftexpand').modal('hide');
			} else {
				growl.warning('Please select store..!!');
			}
	}

	$rootScope.getStoreName=function(){
			var storeObj= JSON.parse(sessionStorage.getItem('selectedTopStore'));
			if(storeObj){
				return storeObj.storeName;
			}
			else{
				return '';
			}
		
	}
	
	// code for registration field configuration start
	$rootScope.getDefaultRegistrationValidationDetails=function(){
		var cookieObject = $cookies.getObject('cookieObject');
		if(cookieObject == undefined){
			$state.go('login');
			return;
	}
	var unitId = cookieObject.unitId;
	var organizationId = cookieObject.organizationId;
		
	var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/getUnitApplicationRegistrationFieldConfig/"+organizationId+"/"+unitId
			
	GenericService.serviceAction("GET", URI).then(function (response) {
		if (response.data.status == 'success') {
			
			$rootScope.fieldConfigList=response.data.listObject;
			if($rootScope.fieldConfigList.length>0)
				{
				$rootScope.registrationFieldObj=$rootScope.fieldConfigList[0];
				 console.log("registrationFieldObj",$rootScope.registrationFieldObj);
				}else{
					$rootScope.registrationFieldObj={
							prefix: "Y",
							firstName: "Y",
							middleName: "N",
							lastName: "Y",
							birthDate: "Y",
							sex: "Y",
							callingCode: "Y",
							mobileNo: "Y",
							mobileDigit:"10",
							nationality:"Y",
							identificationDoc:"Y",
							race:"Y",
							maritalStatus:"Y",
							occupation:"Y",
							patientCategory:"Y",
							email:"N"
					}
				}												

		} else if (response.data.status == 'error') {
			growl.error(response.data.message,
				{
					title: 'Error!'
				});
		}
	});
}

// getting default field configuration start
	/**
	 * **************************For cssd store
	 * selection**************************
	 */
	
	$rootScope.cssdStorePopup = function(){
		var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
		}
		var unitId = cookieObject.unitId;
		var organizationId = cookieObject.organizationId;
		var userId = cookieObject.userId;
		
/*
 * var URI = BASE_URL+ROOT_URL+ STORES
 * +'?organizationId='+organizationId+'&unitId='+unitId+'&userId='+userId;
 */
		var URI = BASE_URL + ROOT_URL+ "/cssd/getCssdStoreList";
		var objForStore={
				organizationId :organizationId,
				unitId : unitId,
				userId :cookieObject.userId
		};
		// var URI = BASE_URL+ROOT_URL+
		// CSSDSTORES
		// +'?organizationId='+organizationId+'&unitId='+unitId+'&userId='+userId;
			// GenericService.serviceAction("GET",URI,{}).then(function(response){
		GenericService.serviceAction("POST",URI,objForStore).then(function(response){
				var stores=response.data.listObject;
				if(stores.length > 0){
					$rootScope.cssdStoreList=stores;
					// angular.element('#cssdStoreSelectionPopup').modal('show');
				}
			});	
	}
	$rootScope.cssdStore22="";	
	$rootScope.setCssdStore = function(){
		if($rootScope.cssdStore22 != undefined && $rootScope.cssdStore22 != null && $rootScope.cssdStore22 != ""){
			sessionStorage.setItem('selectedCssdStore', JSON.stringify($rootScope.cssdStore22));
			var currentURL = $window.location.href;
			console.log(currentURL);
// $("#cssdStoreSelectionPopup").hide();
			angular.element('#cssdStoreSelectionPopup').modal('hide');
// $rootScope.currentCSSDScope.init();
			
			/* $window.location.reload(); */
		}else{
			growl.warning("Please select CSSD store. ");
		}
			
			
	}

	$rootScope.showTwilioNotification = function(){
		var cookieObject = $cookies.getObject('cookieObject');
		var url= BASE_URL+ ROOT_URL+'/api/getVideoataForNotification';
		var totalNotifications = 0;
		 var twiliobaseUrlLocation = window.location.href;
			var twiliotemp = twiliobaseUrlLocation.split('/');
			var twilioBASE_URL_CALL = twiliobaseUrlLocation.replace(twiliotemp[twiliotemp.length-1],"");
		var data =
		{
		    "unitId": cookieObject.unitId,
            "doctorId": cookieObject.doctorId
		}
		var totalNotifications = 0;
		$.ajax({
			type : "post",
			url : url,
			contentType : "application/json",
	        data : JSON.stringify(data),
			success : function(response) {
	
					var status = response.status;
					var list = response.listObject;
					 $('#twilioNotificationDiv').empty();
					 angular.forEach(list, function(val) {
				            var src = BASE_URL + ROOT_URL + "resources/assets/images/tempImages/m.jpg";
				            if (val.patientPhoto != null) {
				                src = val.patientPhoto;
				            } else if (val.genderId == 1) {
				                src = BASE_URL + ROOT_URL + "/resources/assets/images/tempImages/m.jpg";
				            } else {
				                src = BASE_URL + ROOT_URL + "/resources/assets/images/tempImages/patient.png";
				            }

				            var checkinbutton = "Join";
				            if (val.appointmentStatusId == 1) {
				                checkinbutton = "Checkin and Join";
				            }

				            var myvar = '<div class="row padding-top10">' +
				                '<div class="notificationBoxContaint padding-top-bottom5"' +
				                'style="padding-left: 0px !important; padding-right: 15px !important; padding-bottom: 10px;">' +
				                '<div class="row padding-top-bottom5">' +
				                '<div class="col-lg-1 col-md-1 col-sm-1">' +
				                '<hr' +
				                'style="margin-top: 7px !important; margin-bottom: 0px !important; border-top: 10px solid rgba(84, 249, 106, 1) !important; width: 10px; border-radius: 20px !important;">' +
				                '</div>' +
				                '<div class="col-lg-6 col-md-8 col-sm-8">' +
				                '<span>Teleconsultation Alert</span>' +
				                '</div>' +
				                '<div class="col-lg-5 col-md-3 col-sm-3 text-right">' +
				                '<span>' + val.appointmentDate + '</span>' +
				                '</div>' +
				                '</div>' +
				                '<div class="row">' +
				                ' <div class="col-lg-3 col-md-3 col-sm-3">'+
								 '					<div class="row text-center" style="padding-top: 4px;">'+
								 '						<img'+
								 '							src="'+src+'"'+
								 '							style="width: 70px; height: 70px; border-radius: 50px;">'+
								 '					</div>'+
								 '				</div>'+
				                '<div class="col-lg-9 col-md-9 col-sm-9 text-right">' +
				                '<div class="row text-center patientDetailsMsgs"' +
				                'style="font-size: 12px !important;">' +
				                '<span class="patientDetailsMsg"' +
				                'style="font-size: 12px !important;">' + val.patientName +
				                '</span> is ready and waiting in the room for the' +
				                '<span class="patientDetailsMsg"' +
				                'style="font-size: 12px !important;">Teleconsultation' +
				                'appointment at ' + val.timeSlot + '</span>' +
				                '</div>' +
				                '<div class="row padding-top10">' +
				                '<button type="button" class="btn btn-primary width-auto" style="border-radius: 10px; min-width: 110px;" ng-click="joinByDoctorByNotification(' + val.appointmentIdMyLife + ',' + val.userChatDetailsId + ');">' + checkinbutton + '</button>' +
				                '</div>' +
				                '</div>' +
				                '</div>' +
				                '</div>' +
				                '</div>';

				            // Append the compiled HTML
				            var compiledElement = $compile(myvar)($rootScope);
				            angular.element(document.getElementById('twilioNotificationDiv')).append(compiledElement);
				        });

				        // Apply the changes to the scope
				        $timeout(function() {
				            $rootScope.$apply();
				        });
					
					 $('#totalNotifications').text(totalNotifications);
					if(list.length==0){
						 $('#twilioNotificationDiv').append('<div style="text-align: center;">No notification.</div>');
					}
				},error : function(){
					console.log("Error");
					  stopLoader();
				}
			})
		
		
	}

	
	$rootScope.getCssdStoreName=function(){
			var storeObj= JSON.parse(sessionStorage.getItem('selectedCssdStore'));
			if(storeObj){
				return storeObj.storeName;
			}
			else{
				return '';
			}
		
	}
	
	
	
	$rootScope.getTemplateCountByAdmissionOrEncounter=function(patientId,addmissionId,encounterId){
		$rootScope.consentTempCount=0;
		$rootScope.letterTempCount=0;
		$rootScope.certificateTempCount=0;
		$rootScope.complaintTempCount=0;
		$rootScope.generalExamTempCount=0;
		$rootScope.physicalExamTempCount=0;
		$rootScope.diagnosisTempCount=0;
		$rootScope.cytologyTempCount=0;
		$rootScope.personalHistoryTempCount=0;
		$rootScope.nursingNoteTempCount=0;
		$rootScope.initialAssessments =0;
		var tempObj={
				patientId:patientId
		  }
		if(addmissionId!=null){
			tempObj.admissionId=addmissionId;
		}else{
			tempObj.encounterId=encounterId;
		}
		var URI = BASE_URL + ROOT_URL + "/global/templates/getSavedTemplateCount";
				
		GenericService.serviceAction("POST", URI,tempObj).then(function (response) {
			if (response.data.status == 'success') {
				angular.forEach(response.data.listObject,function(value,index){
					if(value.templateTypeId==1){
						$rootScope.consentTempCount=value.templateCount;
					}else if(value.templateTypeId==2){
						$rootScope.letterTempCount=value.templateCount;
					}else if(value.templateTypeId==3){
						$rootScope.certificateTempCount=value.templateCount;
					}else if(value.templateTypeId==4){
						$rootScope.complaintTempCount=value.templateCount;
					}else if(value.templateTypeId==5){
						$rootScope.generalExamTempCount=value.templateCount;
					}else if(value.templateTypeId==6){
						$rootScope.physicalExamTempCount += value.templateCount;
					}else if(value.templateTypeId==17){
						$rootScope.diagnosisTempCount=value.templateCount;
					}else if(value.templateTypeId==19){
						$rootScope.personalHistoryTempCount=value.templateCount;
					}else if(value.templateTypeId== 24){
						$rootScope.nursingNoteTempCount = value.templateCount;
					}else if(value.templateTypeId== 25){
						$rootScope.initialAssessments = value.templateCount;
					}
				});
			}
		});
}
	
/** ********************************************************************************* */	
	
	$rootScope.selectedPatientRadioCommon=function(index){
		$("#patientSelectRadioCommon_"+index).prop("checked",true);
	}
	
	$rootScope.patientDocumnetInformation = function(info) {
		$rootScope.patientDocumentFlipBook ={};
		$rootScope.patientDocumentFlipBook =info;
		
		
			var data =
			{
			"encounterId":$rootScope.patientDocumentFlipBook.encounterId,
			"patientId":$rootScope.patientDocumentFlipBook.patientId,
			"unitId":$rootScope.unitId,
			"organisationId":$rootScope.organizationId,
			"userId":$rootScope.userId
			}
			
			
		var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
		BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			$rootScope.patientDocumentListTest = [];
				if (response.data.status == "success") 
				{
					$rootScope.patientDocumentListTest = response.data.listObject;
					if($rootScope.patientDocumentListTest != null && $rootScope.patientDocumentListTest.length >0){
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

     	
      $rootScope.convertDateToTimestamp = function(dateString) {
            let parts = dateString.split("-");
            let day = parseInt(parts[0], 10);
            let month = parseInt(parts[1], 10) - 1; 
            let year = parseInt(parts[2], 10);
            let date = new Date(year, month, day);
            let timestampInMilliseconds = date.getTime();
            let timestampInSeconds = timestampInMilliseconds / 1000;
            return timestampInSeconds;
        };
        
        $rootScope.generateFlipbookData = function() {
                  let flipbookDatalist = [];

	     			var filteredlist=[];
	     		    angular.forEach($rootScope.patientDocumentListTest,function(filterObj,index){
	     		    	
	     		    	if(filterObj.documentPath != null){
	     				var flipbookDataobj={};
	     				 let createdDate = filterObj.createdDate;
	     	            let timestamp = $rootScope.convertDateToTimestamp(createdDate);
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
                    logo: $rootScope.firstImgaa,
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

/*
 * angular.module('myApp').config([ "$ocLazyLoadProvider",
 * function($ocLazyLoadProvider) { $ocLazyLoadProvider.config({ 'debug': true, //
 * For debugging 'true/false' 'events': true, // For Event 'true/false'
 * 'modules': [{ // Set modules initially name : 'loginController', // State1
 * module files: ['resources/assets/js/controller/login/loginController.js'] }, { //
 * Set modules initially name : 'resetPwdconfirmPwdController', // State1 module
 * files:
 * ['resources/assets/js/controller/master/resetPwdconfirmPwdController.js'] } ]
 * }); }]);
 */
angular.module('myApp').config(['$stateProvider', '$urlRouterProvider','growlProvider', 'ivhTreeviewOptionsProvider', function($stateProvider, $urlRouterProvider,growlProvider, ivhTreeviewOptionsProvider){
	try{
	 growlProvider.globalTimeToLive(3000);
	$urlRouterProvider.otherwise("/login")
	
	ivhTreeviewOptionsProvider.set({
	  idAttribute: 'moduleId',
	  labelAttribute: 'moduleName',
	  twistieCollapsedTpl: '<i class="fa fa-caret-right" aria-hidden="true"></i>',
	  twistieExpandedTpl: '<i class="fa fa-caret-down" aria-hidden="true"></i>',
	  twistieLeafTpl: ' '
	});
	

	
	$stateProvider
	.state('login', {
        url: '/login',
         templateUrl: 'views/login.html',
        //templateUrl: 'views/MRRlogin.html',        
        controller:'loginController'
    })
	/*
	 * .state('login', { url: "/login", views : { "" : {
	 * templateUrl:"views/login.html" } }, resolve: { loadMyCtrl:
	 * ['$ocLazyLoad', function($ocLazyLoad) { return
	 * $ocLazyLoad.load('loginController'); // Resolve promise and load before
	 * view }] } })
	 */
	
	/*
	 * .state('resetPwdconfirmPwd', { url: "/resetPwdconfirmPwd", views : { "" : {
	 * templateUrl:"views/reset-password-confirm-pwd.html" } }, resolve: {
	 * loadMyCtrl: ['$ocLazyLoad', function($ocLazyLoad) { return
	 * $ocLazyLoad.load('resetPwdconfirmPwdController'); // Resolve promise and
	 * load before view }] } })
	 */
	
    .state('resetPwd', {
        url: '/resetPwd',
        templateUrl: 'views/reset-password.html',
        controller:'MasterController',
        title: 'Reset Password'
    })
    .state('dashBoard', {
        url: '/dashBoard',
        templateUrl: 'views/dashboard.html',
		controller:'MasterController',
		title: 'Dashboard'
    })
    
    .state('allergies', {
        url: '/allergies',
        templateUrl: 'views/Nursing/allergies.html',
        controller:'allergiesController',
        title: 'Allergies',
        resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 47 || item.id ==245);
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
        	params: {
		        obj: {}
		    }
    })
    
    .state('bedManagement', {
        url: '/bedManagement',
        templateUrl: 'views/bedManagement/bed-management-er.html',
        controller:'bedManagementController',
        title: 'Map of Ward Bed',
        resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 48);
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
        params: {
	        obj: {}
	    }
    })
   .state('icutypeMaster', {
		 url: '/icutypeMaster',
		 templateUrl: 'views/master/UnitMaster/organizationMaster/icu-type-master.html',
		 controller: "icuTypeController",
		 title: 'ICU Type Master',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 374);
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
			 }
	})
	/*
	 * .state('organizationMaster', { url: '/organizationMaster', templateUrl:
	 * 'views/master/GlobalMaster/hospital-master.html', controller:
	 * "organizationController" })
	 */
	.state('specialityMaster', {
		 url: '/specialityMaster',
		 templateUrl: 'views/master/GlobalMaster/department-master-global.html',
		 controller: "specialityMasterController",
		 title: 'Department Master',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 372);
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
			 }
	})
	.state('subSpecialityMaster', {
		 url: '/subSpecialityMaster',
		 templateUrl: 'views/master/GlobalMaster/sub-department-Master.html',
		 controller: "subSpecialityMasterController",
		 title: 'Sub Department Master',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 373);
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
			 }
	})
	.state('holidayCalenderMaster', {
		 url: '/holidayCalenderMaster',
		 templateUrl: 'views/master/GlobalMaster/holiday-calendar-master.html',
		 controller: "holidayCalenderMasterController",
		 title: 'Holiday Calendar Master',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 375);
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
			 }
	})
	.state('serviceMaster', {
		 url: '/serviceMaster',
		 templateUrl: 'views/master/GlobalMaster/service-master.html',
		 controller: "serviceMasterController",
		 title: 'Service Master',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 376);
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
			 }
	})
	.state('nationalityMaster', {
		 url: '/nationalityMaster',
		 templateUrl: 'views/master/GlobalMaster/nationality-master.html',
		 controller: "nationalityMasterController",
		 title: 'Nationality Master',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 381);
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
			 }
	})
	.state('raceMaster', {
		 url: '/raceMaster',
		 templateUrl: 'views/master/GlobalMaster/race-master.html',
		 controller: "raceMasterController",
		 title: 'Race Master',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 382);
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
			 }
	})
	.state('uhidMergeRequest', {
		 url: '/uhidMergeRequest',
		 templateUrl: 'views/master/GlobalMaster/uhid-merge.html',
		 controller: "uhidMergeRequestController",
		 title: 'UHID Merge Request',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 1048);
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
			 }
		 
	})
		.state('sampleTrackerScreen', {
		 url: '/sampleTrackerScreen',
		 templateUrl: 'views/master/LIS/test-sample-status.html',
		 controller: "barcodeScannerController",
		 title: 'Sample Tracker Screen',
	})
	
	.state('countryMaster', {
		 url: '/countryMaster',
		 templateUrl: 'views/master/GlobalMaster/country-master.html',
		 controller: "countryMasterController",
		 title: 'Country Master',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 383);
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
			 }
	})
	.state('stateMaster', {
		 url: '/stateMaster',
		 templateUrl: 'views/master/GlobalMaster/state-master.html',
		 controller: "stateMasterController",
		 title: 'State Master',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 384);
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
			 }
	})
	.state('districtMaster', {
		 url: '/districtMaster',
		 templateUrl: 'views/master/GlobalMaster/district-master.html',
		 controller: "districtMasterController",
		 title: 'District Master',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 385);
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
			 }
	})
	.state('cityMaster', {
		 url: '/cityMaster',
		 templateUrl: 'views/master/GlobalMaster/city-master.html',
		 controller: "cityMasterController",
		 title: 'City Master',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 386);
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
			 }
	})
	.state('areaMaster', {
		 url: '/areaMaster',
		 templateUrl: 'views/master/GlobalMaster/area-master.html',
		 controller: "areaMasterController",
		 title: 'Area Master',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 387);
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
			 }
	})
	.state('identificationMaster', {
		 url: '/identificationMaster',
		 templateUrl: 'views/master/GlobalMaster/identificationMaster.html',
		 controller: "identificationMasterController",
		 title: 'Identification Card Master',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 388);
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
			 }
	})
	/*
	 * .state('sexMaster', { url: '/sexMaster', templateUrl:
	 * 'views/master/GlobalMaster/sex-master.html', controller:
	 * "sexMasterController", title: 'Sex Master', resolve : {
	 * 
	 * data: function($q, $state, $timeout) { var deferred = $q.defer();
	 * 
	 * $timeout(function() { var arr =
	 * JSON.parse(localStorage.getItem('permissions'))
	 * .modules.filter(function(item){ return (item.id == 389); });
	 * if(arr.length <= 0){ $state.go("accessDenied") deferred.reject(); } else {
	 * deferred.resolve(); } });
	 * 
	 * return deferred.promise; } } })
	 */
	.state('relationMaster', {
		 url: '/relationMaster',
		 templateUrl: 'views/master/GlobalMaster/relation-master.html',
		 controller: "relationMasterController",
		 title: 'Relation Master',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 390);
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
			 }
	})
	.state('maritalstatusMaster', {
		 url: '/maritalstatusMaster',
		 templateUrl: 'views/master/GlobalMaster/marital-status-master.html',
		 controller: "maritalstatusMasterController",
		 title: 'Marital Status Master',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 391);
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
			 }
	})
	
	.state('occupationMaster', {
		 url: '/occupationMaster',
		 templateUrl: 'views/master/GlobalMaster/occupation-master.html',
		 controller: "occupationMasterController",
		 title: 'Occupation Master',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 393);
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
			 }
		 
	})
	
	
	.state('commonMaster', {
		 url: '/commonMaster/:url',
		 templateUrl: 'views/master/common-master-screen.html',
		 resolve : {
		            getController : function($stateParams)
		            {
		                if ($stateParams.url === "sampleMaster")
		                {
		                    return "sampleController"
		                }else if ($stateParams.url === "containerMaster")
		                {
		                    return "containerController"
		                }else if ($stateParams.url === "techinqueMaster")
		                {
		                    return "techinqueController"
		                }else if ($stateParams.url === "prerequisitesMaster")
		                {
		                    return "prerequisitesController"
		                }else if ($stateParams.url === "unitMaster")
		                {
		                    return "unitController"
		                }else if ($stateParams.url === "reagentMaster")
		                {
		                    return "reagentController"
		                }else if ($stateParams.url === "reportTypeMaster")
		                {
		                    return "reportTypeController"
		                }else if ($stateParams.url === "bacteriaShapMaster")
		                {
		                    return "bacteriaShapController"
		                }else if ($stateParams.url === "wtifilmStudyMasterMaster")
		                {
		                    return "wtifilmStudyMasterController"
		                }else if ($stateParams.url === "mediaMaster")
		                {
		                    return "mediaController"
		                }else if ($stateParams.url === "colonyMaster")
		                {
		                    return "colonyController"
		                }else if ($stateParams.url === "organismMaster")
		                {
		                    return "organismMasterController"
		                }
		                else if ($stateParams.url === "organismGroupMaster")
		                {
		                    return "organismGroupMasterController"
		                }else if ($stateParams.url === "stainigMaster")
		                {
		                    return "stainigMasterController"
		                }
		                else if ($stateParams.url === "microbiologyStainMaster")
		                {
		                    return "microbiologyStainMasterController"
		                }else if ($stateParams.url === "antibioticMaster")
		                {
		                    return "antibioticsController"
		                }else if ($stateParams.url === "antibioticsMaster")
		                {
		                    return "antibioticController" 
		                }
		                /*
						 * else if ($stateParams.url === "specimanMaster") {
						 * return "specimanController" }
						 */ else if ($stateParams.url === "incubationTestMaster")
		                {
		                    return "incubationTestController"
		                } else if ($stateParams.url === "antibioticsGroupMaster")
		                {
		                    return "antibioticsGroupController"
		                }/*
							 * else if ($stateParams.url ===
							 * "sampleTypeMediaMappingMaster") { return
							 * "sampleTypeMediaMappingController" }
							 */ else if ($stateParams.url === "sensitivityTypeResultMaster")
		                {
		                    return "sensitivityTypeResultController"
		                } else if ($stateParams.url === "slideProcedureMaster")
		                {
		                    return "slideProcedureMasterController"
		                } else if ($stateParams.url === "bacteriaClassificationMaster")
		                {
		                    return "bacteriaClassficationController"
		                }
		                else if ($stateParams.url === "blockMaster")
		                {
		                    return "blockController"
		                }
		                else if ($stateParams.url === "headerMaster")
		                {
		                    return "headerController"	
		                }
		                else if ($stateParams.url === "serologyTestFindingMaster")
		                {
		                    return "serologyTestFindingController"	
		                }else if ($stateParams.url === "departmentMaster")
		                {
		                    return "departmentController"	
		                }else if ($stateParams.url === "phlebotomyMaster")
		                {
		                    return "unitPhlebotomyController"	
		                }
		                else if ($stateParams.url === "storageLocationMaster")
		                {
		                    return "storageLocationController"	
		                }
		                else if ($stateParams.url === "sexmaster")
		                {
		                    return "sexMasterController"
		                }
		                else if ($stateParams.url === "relationmaster")
		                {
		                    return "relationMasterController"
		                }
		                else if ($stateParams.url === "maritalstatus")
		                {
		                    return "maritalstatusMasterController"
		                }
		                else if ($stateParams.url === "religion")
		                {
		                    return "religionMasterController"
		                }
		                else if ($stateParams.url === "occupation")
		                {
		                    return "occupationMasterController"
		                }
		                else if ($stateParams.url === "patientsource")
		                {
		                    return "patientSourceMasterController"
		                }
		                else if ($stateParams.url === "referraltype")
		                {
		                    return "referralTypeMasterController"
		                }

		                else if ($stateParams.url === "priorityMaster")
		                {
		                    return "priorityMasterController"

		                }
		                else if ($stateParams.url === "visitType")
		                {
		                    return "visitTypeMasterController"

		                }
		                else if ($stateParams.url === "paymentMode")
		                {
		                    return "paymentModeMasterController"
		                }
		                else if ($stateParams.url === "paymentEntitment")
		                {
		                    return "paymentEntitmentMasterController"
		                }
		                else if ($stateParams.url === "consultantType")
		                {
		                    return "consultantTypeMasterController"
		                }		       
		                else if ($stateParams.url === "biochemicalTest")
		                {		                
		                    return "biochemicalTestMasterController"
		                }
		                else if ($stateParams.url === "languageMaster")
		                {
		                    return "languageMasterController"	
		                }
		                else if ($stateParams.url === "scheduleMaster")
		                {		                
		                    return "scheduleMasterController"
		                }
		              
		                /*
						 * else if ($stateParams.url ===
						 * "serologyDetectionMaster") { return
						 * "serologyDetectionMasterController" }
						 */
		            },		            
		            data: function($q, $state, $timeout) {
				        var deferred = $q.defer();
				
				        $timeout(function() {
				        	var arr = JSON.parse(localStorage.getItem('permissions'))
						 	.modules.filter(function(item){
		           return (item.id == 412 || item.id == 413 || item.id == 414 || item.id == 415 || item.id == 417 || item.id == 419 || item.id == 420 || item.id == 392 
		            	|| item.id == 423 || item.id == 424 || item.id == 427 || item.id == 435 || item.id == 436 || item.id == 437 || item.id == 825 || item.id == 430 
		            	|| item.id == 429 || item.id == 425 || item.id == 431 || item.id == 438 || item.id == 440 || item.id == 428 || item.id == 439 || item.id == 561 
		            	|| item.id == 564 || item.id == 557 || item.id == 389 || item.id ==390  || item.id == 391 || item.id == 450 || item.id == 460 || item.id == 492 
		                || item.id == 393 || item.id == 394 || item.id == 395 || item.id == 491 || item.id == 447 || item.id == 448 || item.id == 449 || item.id == 798
		                || item.id == 569 || item.id == 826 || item.id ==426  || item.id ==421  || item.id ==844);
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
		            	controllerProvider: function(getController){
		            	return getController;
		            	},
	})
	.state('referralMaster', {
		 url: '/referralMaster',
		 templateUrl : 'views/master/UnitMaster/organizationMaster/referral-master.html',
		 controller : "referralMasterController",
		 title: 'Referral Master',
			 resolve : {

					data: function($q, $state, $timeout) {
				        var deferred = $q.defer();
				
				        $timeout(function() {
				        	var arr = JSON.parse(localStorage.getItem('permissions'))
						 	.modules.filter(function(item){
			            	   return (item.id == 396);
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
				 }
			 
		})
	.state('deliveryTypeMaster', {
		 url: '/deliveryTypeMaster',
		 templateUrl: 'views/master/GlobalMaster/delivery-type-master.html',
		 controller: "deliveryTypeMasterController",
		 title: 'Delivery Type Master',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 506);
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
			 }
		 
	})
	.state('encounterTypeMaster', {
		 url: '/encounterTypeMaster',
		 templateUrl: 'views/master/GlobalMaster/encounter-type-master.html',
		 controller: "encounterTypeMasterController",
		 title: 'Encounter Type Master',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 397);
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
			 }
		 
	})
	.state('prefixMaster', {
		 url: '/prefixMaster',
		 templateUrl: 'views/master/GlobalMaster/prefix-master.html',
		 controller: "prefixMasterController",
		 title: 'Prefix Master',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 398);
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
			 }
	})
	.state('consentTypeMaster', {
		 url: '/consentTypeMaster',
		 templateUrl: 'views/master/GlobalMaster/consent-type-master.html',
		 controller: "consentTypeMasterController",
		 title: 'Consent Type Master',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 399);
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
			 }
	})
	.state('consentMaster', {
		 url: '/consentMaster',
		 templateUrl: 'views/master/GlobalMaster/consent-master.html',
		 controller: "consentMasterController",
		 title: 'Consent Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 400 || item.id == 527);
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
	   		 }
	
	})
	.state('bedCategoryMaster', {
		 url: '/bedCategoryMaster',
		 templateUrl: 'views/master/GlobalMaster/Adt/bed-category-master-global.html',
		 controller: "bedCategoryMasterController",
		 title: 'Ward Bed Category Master',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 543);
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
			 }
	})
	.state('bedTypeMaster', {
		 url: '/bedTypeMaster',
		 templateUrl: 'views/master/GlobalMaster/Adt/bed-type-master.html',
		 controller: "bedTypeMasterController",
		 title: 'Ward Bed Type Master',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 402);
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
			 }
	})
	/*
	 * .state('admissionTypeMaster', { url: '/admissionTypeMaster', templateUrl:
	 * 'views/master/GlobalMaster/Adt/admission-type-master.html', controller:
	 * "admissionTypeMasterController", resolve : {
	 * 
	 * data: function($q, $state, $timeout) { var deferred = $q.defer();
	 * 
	 * $timeout(function() { var arr =
	 * JSON.parse(localStorage.getItem('permissions'))
	 * .modules.filter(function(item){ return (item.id == 403); });
	 * if(arr.length <= 0){ $state.go("accessDenied") deferred.reject(); } else {
	 * deferred.resolve(); } });
	 * 
	 * return deferred.promise; } } })
	 */
	.state('occupancyUnitMaster', {
		 url: '/occupancyUnitMaster',
		 templateUrl: 'views/master/GlobalMaster/Adt/occupancy-unit-master.html',
		 controller: "occupancyUnitMasterController",
		 title: 'Occupancy Clinical Unit Master',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 404);
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
			 }
	})
	/*
	 * .state('dischargeTypeMaster', { url: '/dischargeTypeMaster', templateUrl:
	 * 'views/master/GlobalMaster/Adt/discharge-type-master.html', controller:
	 * "dischargeTypeMasterController", title: 'Discharge Type Master', resolve : {
	 * 
	 * data: function($q, $state, $timeout) { var deferred = $q.defer();
	 * 
	 * $timeout(function() { var arr =
	 * JSON.parse(localStorage.getItem('permissions'))
	 * .modules.filter(function(item){ return (item.id == 405); });
	 * if(arr.length <= 0){ $state.go("accessDenied") deferred.reject(); } else {
	 * deferred.resolve(); } });
	 * 
	 * return deferred.promise; } } })
	 */
	.state('shortLeaveReasonMaster', {
		 url: '/shortLeaveReasonMaster',
		 templateUrl: 'views/master/GlobalMaster/Adt/short-leave-Reasons-master.html',
		 controller: "shortLeaveReasonMasterController",
		 title: 'Short Leave Reason Master',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 406);
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
			 }
	})
	/*
	 * .state('hierarchyMaster', { url: '/hierarchyMaster', templateUrl:
	 * 'views/master/GlobalMaster/Adt/hierarchy-master.html', controller:
	 * "hierarchyMasterController" })
	 */
	/*
	 * .state('docTypeMaster', { url: '/docTypeMaster', templateUrl:
	 * 'views/master/GlobalMaster/document-type-master.html', controller:
	 * "docTypeMasterController", title: 'Document Type Master', resolve : {
	 * 
	 * data: function($q, $state, $timeout) { var deferred = $q.defer();
	 * 
	 * $timeout(function() { var arr =
	 * JSON.parse(localStorage.getItem('permissions'))
	 * .modules.filter(function(item){ return (item.id == 378); });
	 * if(arr.length <= 0){ $state.go("accessDenied") deferred.reject(); } else {
	 * deferred.resolve(); } });
	 * 
	 * return deferred.promise; } } })
	 */

	.state('admissionRequest', {
		 url: '/admissionRequest',
		 templateUrl: 'views/Admission/admission-request.html',
		 controller: "admissionRequestController",
		 title: 'Admission Request',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 28);
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
		params: {
			screenType:null,
			patientObj:null,
		}
	})
	.state('diagnosisMaster',{
			                   url: '/diagnosis',
			                   templateUrl: 'views/master/GlobalMaster/diagnosis.html',
			                   controller: "diagnosisMasterController",
			                   title: 'Diagnosis',
				           })
	.state('pendingAdmissionDischarge', {
		 url: '/pendingAdmissionDischarge',
		 templateUrl: 'views/Admission/pending-admission-discharge.html',
		 controller: "pendingAdmissionDischargeController",
		 title: 'Pending Admission Request',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 29);
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
			 }
	})
	.state('bedBook', {
		 url: '/bedBook',
		 templateUrl: 'views/bedManagement/bedBook.html',
		 controller: "bedBookController",
		 title: 'Bed Booking'
	})
	.state('bedReservationList', {
		 url: '/bedReservationList',
		 templateUrl: 'views/bedManagement/bed-reservation-list.html',
		 controller: "bedReservationListController",
		 title: 'Ward Bed Reservation List',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 30);
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
			 }
	})
	.state('bedAvailability', {
		 url: '/bedAvailability',
		 templateUrl: 'views/bedManagement/bed-availability.html',
		 controller: "bedAvailabilityController",
		 title: 'Ward Bed Reservation Wait List',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 31);
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
			 }
	})
	.state('bedAllocation', {
		 url: '/bedAllocation',
		 templateUrl: 'views/bedManagement/bed-allocation.html',
		 controller: "bedAllocationController",
		 title: 'Bed Allocation',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 32);
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
		 params: {
		        obj: null
		    }
	})
	.state('admittedPatientList', {
		 url: '/admittedPatientList',
		 templateUrl: 'views/Admission/admitted-patient-list.html',
		 controller: "admittedPatientListController",
		 title: 'Admitted Patient List',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 33);
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
			 }
	})
	.state('insuranceMaster', {
		 url: '/insuranceMaster',
		 templateUrl: 'views/master/UnitMaster/billingMaster/insurance-master.html',
		 controller: "insuranceMasterController",
		 title: 'Insurance Master',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 571);
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
			 }
	})
	.state('passIssued', {
		 url: '/passIssued',
		 templateUrl: 'views/Admission/pass-issued.html',
		 controller: "passIssuedController",
		 title: 'Issued Pass',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 34);
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
			 }
	})
	
	.state('logout', {
        url: '/logout',
        templateUrl: 'views/login.html',
        controller:'logoutController',
      /*
		 * resolve: { removeCookies: function($cookies, $sessionStorage) { var
		 * cookies = $cookies.getAll(); angular.forEach(cookies, function (v, k) {
		 * $cookies.remove(k); }); sessionStorage.removeItem("permissions");
		 * sessionStorage.removeItem("roleIds"); return true; } }
		 */
	
    })
    
    
   /***************************************************************************
	 * Start Added BY Jyoti Date - 04-10-2017
	 **************************************************************************/
    
		   .state('intiateTransferCare', {
	    url: '/intiateTransferCare',
	    templateUrl: 'views/initiate-transfer-care.html',
	    controller: "intiateTransferCareController",
	    title: 'Initiate Transfer/Move of Care',
	    resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 257);
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
		 params: {
	    	  bedManagmentObj: {}
		    }
	  })
	  
	     .state('bloodRequest', {
	    url: '/bloodRequest',
	    templateUrl: 'views/blood-request.html',
	    controller: "MasterController",
	    title: 'Blood Request',
	    	 resolve : {

	 			data: function($q, $state, $timeout) {
	 		        var deferred = $q.defer();
	 		
	 		        $timeout(function() {
	 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	 				 	.modules.filter(function(item){
	 	            	   return (item.id == 251);
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
	 		 }
	 	  })
	     .state('requestInterConsult', {
	    url: '/requestInterConsult',
	    templateUrl: 'views/request-inter-consult.html',
	    controller: "MasterController",
	    title: 'Request Inter Consult'
	  })
	  
	   .state('pendingInterConsult', {
	    url: '/pendingInterConsult',
	    templateUrl: 'views/pending-inter-consult.html',
	    controller: "MasterController",
	    title: 'Pending Inter Consult'
	  })
	  
	     .state('dueForDischarge', {
	    url: '/dueForDischarge',
	    templateUrl: 'views/due-for-discharge.html',
	    controller: "dueForDischargeController",
	    title: 'Due for Hospital Discharge',
		 resolve : {

	 			data: function($q, $state, $timeout) {
	 		        var deferred = $q.defer();
	 		
	 		        $timeout(function() {
	 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	 				 	.modules.filter(function(item){
	 	            	   return (item.id == 262);
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
	 		 params: 
	 		 {
		    	bedManagmentObj: {}
			 }
	  })
	   .state('ERdueForDischarge', {
	    url: '/ERdueForDischarge',
	    templateUrl: 'views/er-due-for-discharge.html',
	    controller: "ERDueForDischargeController",
	    title: 'Due for Hospital Discharge',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 333);
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
 		 }
	  })
		 
	  .state('dayCareDueForDischarge', {
	    url: '/dayCareDueForDischarge',
	    templateUrl: 'views/Transfer/day-care-due-for-discharge.html',
	    controller: "dueForDischargeController",
	    title: 'Due for Discharge',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 781);
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
 		 }
	  })
     /**
		 * ******************************************** START NURSING
		 * ********************************************
		 */
	  
	   /*
		 * .state('consumableIndenting', { url: '/consumableIndenting',
		 * templateUrl: 'views/Nursing/absconding-patient.html', controller:
		 * "MasterController" })
		 */
	   .state('abscondingPatient', {
	    url: '/abscondingPatient',
	    templateUrl: 'views/Nursing/absconding-patient.html',
	    controller: "MasterController",
	    title: 'Absconding Patient'
	  })
	  
	  .state('medicationAcceptance', {
	    url: '/medicationAcceptance',
	    templateUrl: 'views/Nursing/medication-acceptance.html',
	    controller: "MasterController",
	    title: 'Medication Acceptance'
	  })
	  .state('nursingOrderView', {
	    url: '/nursingOrderView',
	    templateUrl: 'views/Nursing/nursing-order-view.html',
	    controller: "nursingOrderViewMasterController",
	    title: 'Nursing Order',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 51);
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
 		 }
	  })
	  .state('nursingOrder', {
	    url: '/nursingOrder',
	    templateUrl: 'views/Nursing/nursing-order.html',
	    controller: "MasterController",
	    title: 'Nursing Order'
	  })
	  .state('pendingNursingOrder', {
	    url: '/pendingNursingOrder',
	    templateUrl: 'views/Nursing/pending-nursing-order.html',
	    controller: "MasterController",
	    title: 'Pending Nursing Order'
	  })
	  .state('sampleCollection', {
	    url: '/patientDiagnostic',
	    templateUrl: 'views/Nursing/sample-collection.html',
	    controller: "sampleCollectionIPController",
	    title: 'Patient Diagnostic',
	    params: {
		        obj: {}
		    }
	  })
	.state('pendingOrders', {
	    url: '/pendingOrders',
	    templateUrl: 'views/Nursing/pending-orders.html',
	    controller: "allPendingOrdersForNursingController",
	    title: 'Pending Orders'
	  })
	  
	  /** ******** added on 9/July/2018 start ********** */
	  /*
		 * .state('periodicityService', { url: '/periodicityService',
		 * templateUrl: 'views/Nursing/periodicity-Service.html', controller:
		 * "MasterController" })
		 */
	  /** ******** added on 9/July/2018 start ********** */
	  /**
		 * ******************************************** END NURSING
		 * ********************************************
		 */
	  /**
		 * ******************************************** START ICU
		 * ********************************************
		 */
	  
	 /*
		 * .state('patientAcceptance', { url: '/patientAcceptance', templateUrl:
		 * 'views/ICU/patient-acceptance.html', controller: "MasterController" })
		 */
	  
	  .state('patientAcceptance', {
	    url: '/patientAcceptance',
	    templateUrl: 'views/ICU/patient-acceptance.html',
	    controller: "patientAcceptanceController",
	    title: 'Patient Acceptance',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 46);
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
 		 }
	  })
	  
	  /*
		 * .state('patientAcceptance', { url : '/patientAcceptance', views : {
		 * '@' : { templateUrl : 'views/ICU/patient-acceptance.html', controller :
		 * 'patientAcceptanceController', } } })
		 */
       
     /*
		 * .state('patientAcceptance.newAdmissionList', { url :
		 * '/newAdmissionList', views : { '@patientAcceptance' : { templateUrl :
		 * 'views/ICU/patient-acceptance.html', controller :
		 * 'patientAcceptanceController', resolve : {
		 * 
		 * data: function($q, $state, $timeout) { var deferred = $q.defer();
		 * 
		 * $timeout(function() { var arr =
		 * JSON.parse(localStorage.getItem('permissions'))
		 * .modules.filter(function(item){ return (item.id == 46); });
		 * if(arr.length <= 0){ $state.go("accessDenied") deferred.reject(); }
		 * else { deferred.resolve(); } });
		 * 
		 * return deferred.promise; } } } } })
		 */
       /*
		 * .state('patientAcceptance.transferList', { url : '/transferList',
		 * views : { '@patientAcceptance' : { templateUrl :
		 * 'views/ICU/transfer-list.html', controller :
		 * 'patientAcceptanceController', } } })
		 */
      /*
		 * .state('patientAcceptance.ICUtransferList', { url :
		 * '/ICUtransferList', views : { '@patientAcceptance' : { templateUrl :
		 * 'views/ICU/icu-transfer-list.html', controller :
		 * 'patientAcceptanceController', } } })
		 */
	  
	   .state('houseKeeping', {
	    url: '/houseKeeping',
	    templateUrl: 'views/ICU/pending-for-house-keeping.html',
	    controller: "houseKeepingController",
	    title: 'House Keeping Worklist',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 360);
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
 		 }
	  })
	  .state('cleanedBedAccept', {
	    url: '/cleanedBedAccept',
	    templateUrl: 'views/ICU/pending-for-bed-cleaning-acceptance.html',
	    controller: "cleanedBedAcceptController",
	    title: 'Cleaned Ward Bed Acceptance',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 84);
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
 		 }
	  })
	   .state('transferToICU', {
	    url: '/transferToICU',
	    templateUrl: 'views/ICU/transeferToICU.html',
	    controller: "transferToICUController",
	    title: 'Transfer/Move to ICU',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 260);
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
 		 params: 
 		 {
	    	bedManagmentObj: {}
		 }
	   
	  })
	  .state('pendingTransferRequest', {
	    url: '/pendingTransferRequest',
	    templateUrl: 'views/ICU/pending-transfer-request.html',
	    controller: "pendingTransferRequestController",
	    title: 'Pending Transfer/Move Request',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 259);
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
 		 params: {
	    	  bedManagmentObj: {}
		    }
	   
	  })
	   .state('acceptanceOfTransferOfCare', {
	    url: '/acceptanceOfTransferOfCare',
	    templateUrl: 'views/ICU/acceptance-of-transfer-of-care.html',
	    controller: "acceptanceOfTransferOfCareController",
	    title: 'Acceptance Transfer/Move of Care',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 258);
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
 		 params: {
	    	  bedManagmentObj: {}
		    }
	   
	  })
	  /* vivek start */
	   .state('pendingConsultOrderRequest', {
	    url: '/pendingConsultOrderRequest',
	    templateUrl: 'views/Nursing/pending-inter-consult-request.html',
	    controller: "pendingInterConsultRequestController",
	    title: 'Pending Consult Order',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 250);
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
 		 }
	   
	  })
	  
	  /* vivek end */
	  
	  /**
		 * ******************************************** END ICU
		 * ********************************************
		 */
    /**
	 * ******************************************** Start MASTER
	 * ********************************************
	 */ 
	   /* LIS */
		  .state('ageGroupMaster', {
		    url: '/ageGroupMaster',
		    templateUrl: 'views/master/LIS/age-group-master.html',
		    controller: "ageGroupController",
		    title: 'Age Group Master',
		    resolve : {

	 			data: function($q, $state, $timeout) {
	 		        var deferred = $q.defer();
	 		
	 		        $timeout(function() {
	 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	 				 	.modules.filter(function(item){
	 	            	   return (item.id == 416);
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
	 		 }
		   
		  })
		  
		  .state('headerMaster', {
		    url: '/headerMaster',
		    templateUrl: 'views/master/LIS/header-master.html',
		    controller: "MasterController",
		    title: 'Header Master'
		  })
		  
		  .state('unknownMaster', {
			    url: '/unknownMaster',
			    templateUrl: 'views/master/LIS/unknown-master.html',
			    controller: "MasterController",
			    title: 'Unknown Master',
			  })
			  
		   .state('parameterMaster', {
		    url: '/parameterMaster',
		    templateUrl: 'views/master/LIS/parameter-master.html',
		    controller: "parameterMasterController",
		    title: 'Parameter Master',
		    resolve : {

	 			data: function($q, $state, $timeout) {
	 		        var deferred = $q.defer();
	 		
	 		        $timeout(function() {
	 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	 				 	.modules.filter(function(item){
	 	            	   return (item.id == 562);
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
	 		 }
		   
		  })
		  
		  
		  .state('outsourceTestMaster', {
		    url: '/outsourceTestMaster',
		    templateUrl: 'views/master/LIS/outsource-test-master.html',
		    controller: "MasterController",
		  })
		  .state('departmentMaster', {
		    url: '/departmentMaster',
		    templateUrl: 'views/master/LIS/department-master.html',
		    controller: "MasterController",
		    title: 'Department Master'
		  })
		  .state('templateMaster', {
		    url: '/templateMaster',
		    templateUrl: 'views/master/LIS/template-master.html',
		    controller: "labTemplateMasterController",
		    title: 'Template Utility',
		    resolve : {

	 			data: function($q, $state, $timeout) {
	 		        var deferred = $q.defer();
	 		
	 		        $timeout(function() {
	 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	 				 	.modules.filter(function(item){
	 	            	   return (item.id == 565);
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
	 		 }
		   
		  })
		  .state('parameterFormulaMaster', {
		    url: '/parameterFormulaMaster',
		    templateUrl: 'views/master/LIS/parameter-formula-master.html',
		    controller: "parameterFormulaController",
		    title: 'Parameter Formula Master',
		    resolve : {

	 			data: function($q, $state, $timeout) {
	 		        var deferred = $q.defer();
	 		
	 		        $timeout(function() {
	 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	 				 	.modules.filter(function(item){
	 	            	   return (item.id == 566);
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
	 		 }
		   
		  })
		  .state('profilepanelTestMaster', {
			    url: '/profilepanelTestMaster',
			    templateUrl: 'views/master/LIS/profile-panel-test-master.html',
			    controller: "profilepanelTestMasterController",
			    title: 'Profile Panel Test Master',
			    resolve : {

		 			data: function($q, $state, $timeout) {
		 		        var deferred = $q.defer();
		 		
		 		        $timeout(function() {
		 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
		 				 	.modules.filter(function(item){
		 	            	   return (item.id == 567);
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
		 		 }
			   
			  })
		  .state('incubationPeriodMaster', {
		    url: '/incubationPeriodMaster',
		    templateUrl: 'views/master/LIS/incubation-period-master.html',
		    controller: "incubationPeriodController",
		    title: 'Incubation Period Master',
		    resolve : {

	 			data: function($q, $state, $timeout) {
	 		        var deferred = $q.defer();
	 		
	 		        $timeout(function() {
	 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	 				 	.modules.filter(function(item){
	 	            	   return (item.id == 422);
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
	 		 }
		   
		  })
		.state('storageLocationMaster', {
		    url: '/storageLocationMaster',
		    templateUrl: 'views/master/LIS/storage_location_master.html',
		    controller: "MasterController",
		    title: 'Storage Location Master for Slide & Blocks'
		  })
		  .state('specimanMapping', {
		    url: '/specimanMapping',
		    templateUrl: 'views/master/LIS/speciman_mapping_with_block.html',
		    controller: "MasterController",
		    title: 'Speciman Mapping With Block & Slide Procedure'
		  })
		  .state('organismMapping', {
		    url: '/organismMapping',
		    templateUrl: 'views/master/LIS/organismGroup_mapping_with_organism.html',
		    controller: "organismGroupOrganismMapperController",
		    title: 'Organism Group Mapping With Organism',
		    resolve : {

	 			data: function($q, $state, $timeout) {
	 		        var deferred = $q.defer();
	 		
	 		        $timeout(function() {
	 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	 				 	.modules.filter(function(item){
	 	            	   return (item.id == 442);
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
	 		 }
		   
		  })
		  .state('slideProcedure', {
		    url: '/slideProcedure',
		    templateUrl: 'views/master/LIS/slide_procedure_master.html',
		    controller: "MasterController",
		    title: 'Slide Procedure Master'
		  })
		  .state('blockMaster', {
		    url: '/blockMaster',
		    templateUrl: 'views/master/LIS/block_master.html',
		    controller: "MasterController",
		    title: 'Block Material Master'
		  })
		 /*
			 * .state('specimanMaster', { url: '/specimanMaster', templateUrl:
			 * 'views/master/LIS/speciman_master.html', controller:
			 * "MasterController" })
			 */
		   .state('sensitivityTestResult', {
		    url: '/sensitivityTestResult',
		    templateUrl: 'views/master/LIS/sensitivity_test_result.html',
		    controller: "MasterController",
		    title: 'Sensitivity Test Result'
		  })
		  .state('antibioticsGroup', {
		    url: '/antibioticsGroup',
		    templateUrl: 'views/master/LIS/antibiotics_group_master.html',
		    controller: "MasterController",
		    title: 'Antibiotics Group Master'
		  })
		  .state('antibioticsClass', {
			 url: '/antibioticsClass',
			 templateUrl: 'views/master/LIS/antibiotic_class.html',
			 controller: "MasterController",
			 title: 'Antibiotics Class Master'
		})
		.state('antibiotic', {
			 url: '/antibiotic',
			 templateUrl: 'views/master/LIS/antibiotic.html',
			 controller: "MasterController",
			 title: 'Antibiotic Master',
		})
		.state('additionofAntibiotic', {
			 url: '/additionofAntibiotic',
			 templateUrl: 'views/master/LIS/antibiotic-addition.html',
			 controller: "antibioticAdditionController",
			 title: 'Addition of Antibiotics Against Antibiotic Class',
			 resolve : {

		 			data: function($q, $state, $timeout) {
		 		        var deferred = $q.defer();
		 		
		 		        $timeout(function() {
		 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
		 				 	.modules.filter(function(item){
		 	            	   return (item.id == 432);
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
		 		 }
			   
		})
		.state('mappingofMicroorganism', {
			 url: '/mappingofMicroorganism',
			 templateUrl: 'views/master/LIS/mapping-of-microorganism.html',
			 controller: "antibioticOrganismMpprController",
			 title: 'Mapping of Micro Organism',
			 resolve : {

		 			data: function($q, $state, $timeout) {
		 		        var deferred = $q.defer();
		 		
		 		        $timeout(function() {
		 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
		 				 	.modules.filter(function(item){
		 	            	   return (item.id == 443);
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
		 		 }
			   
		})
		.state('bacteriaClassification', {
			 url: '/bacteriaClassification',
			 templateUrl: 'views/master/LIS/bacteria_classification.html',
			 controller: "MasterController",
			 title: 'Bacteria Classification Master'
		})
		.state('colonyMaster', {
			 url: '/colonyMaster',
			 templateUrl: 'views/master/LIS/colony_master.html',
			 controller: "MasterController",
			 title: 'Colony Master',
		})
		
		.state('textualReferenceRangeMaster', {
			 url: '/textualReferenceRangeMaster',
			 templateUrl: 'views/master/LIS/textual_reference_range-master.html',
			 controller: "textualReferenceRangeMasterController",
			 title: 'Textual Reference Range Master',
			 resolve : {

		 			data: function($q, $state, $timeout) {
		 		        var deferred = $q.defer();
		 		
		 		        $timeout(function() {
		 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
		 				 	.modules.filter(function(item){
		 	            	   return (item.id == 418);
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
		 		 }
			   
		})
	/**
	 * ******************************************** END MASTER
	 * ********************************************
	 */ 
    
    /**
	 * ******************************************** START ADMISSION DISCHARGE
	 * ********************************************
	 */
	
	  .state('deathRegistration', {
	    url: '/deathRegistration',
	    templateUrl: 'views/AdmissionDischarge/death-registration.html',
	    controller: "deathRegistrationController",
	    title: 'Death Registration',
		 resolve : {

	 			data: function($q, $state, $timeout) {
	 		        var deferred = $q.defer();
	 		
	 		        $timeout(function() {
	 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	 				 	.modules.filter(function(item){
	 	            	   return (item.id == 363);
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
	 		 }
		   
	  })
	   .state('cancelForDischarge', {
	    url: '/cancelForDischarge',
	    templateUrl: 'views/AdmissionDischarge/cancel-for-discharge.html',
	    controller: "cancelForDischargeController",
	    title: 'Cancel Discharge',
		resolve : {

	 			data: function($q, $state, $timeout) {
	 		        var deferred = $q.defer();
	 		
	 		        $timeout(function() {
	 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	 				 	.modules.filter(function(item){
	 	            	   return (item.id == 263);
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
	 		 params: 
	 		 {
		    	bedManagmentObj: {}
			 }
		   
	  })
	  .state('cancelDischarge', {
	    url: '/cancelDischarge',
	    templateUrl: 'views/AdmissionDischarge/cancel-discharge.html',
	    controller: "cancelPatientDischargeController",
	    title: 'Cancel Hospital Discharge',
		 params: 
		 {
	    	bedManagmentObj: {}
		 }
	  })
	  
	  
	    .state('costEstimation', {
	    url: '/costEstimation',
	    templateUrl: 'views/AdmissionDischarge/cost-estimation.html',
	    controller: "costEstimationController",
	    title: 'Bill Estimation',
	    params: {
	    	selectedPatient: {} // default value
          }
	  })
	  
	     
	  
	  .state('erPatientList', {
	    url: '/erPatientList',
	    templateUrl: 'views/AdmissionDischarge/ER/Er-patient-list.html',
	    controller: "erPatientListController",
	    title: 'ER Patient List',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 331);
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
 		 }
	   
	  })
	  
	  .state('erMarkForDischarge', {
	    url: '/erMarkForDischarge',
	    templateUrl: 'views/AdmissionDischarge/ER/Er-mark-for-discharge.html',
	    controller: "erMarkForDischargeController",
	    title: 'ER Mark for Hospital Discharge',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 776);
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
 		 }
	   
	  })
	  
	  
	    .state('bedTypeAllocation', {
	    url: '/bedTypeAllocation',
	    templateUrl: 'views/AdmissionDischarge/ER/bed-type-allocation.html',
	    controller: "bedTypeAllocationERController",
	    title: 'Triage Worklist',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 330);
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
 		 }
	   
	  })
	  
	   .state('patientForDischarge', {
	    url: '/patientForDischarge',
	    templateUrl: 'views/AdmissionDischarge/patient-for-discharge.html',
	    controller: "patientForDischargeController",
	    title: 'Marked for Hospital Discharge',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 71);
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
 		 },params: {
 			bedManagmentObj: {} 
         }
	   
	  })
	  
	   .state('dayCarePatientForDischarge', {
	    url: '/dayCarePatientForDischarge',
	    templateUrl: 'views/AdmissionDischarge/patient-for-discharge.html',
	    controller: "dayCarePatientForDischargeController",
	    title: 'Marked for Discharge',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 785);
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
 		 },params: {
 			bedManagmentObj: {} 
         }
	   
	  })
	  
	  .state('readyForBilling', {
	    url: '/readyForBilling',
	    templateUrl: 'views/AdmissionDischarge/ready-for-billing.html',
	    controller: "readyForBillingController",
	    title: 'Ready for Billing',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 74);
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
 		 },params: {
 			bedManagmentObj: {} 
         }
	   
	  })
	  
	   .state('patientDischargedList', {
	    url: '/patientDischargedList',
	    templateUrl: 'views/AdmissionDischarge/patient-discharged-list.html', 
	    controller: "patientDischargedListController",
	    title: 'Patient Discharge List',
	   
	  })
	  
	   .state('ERreadyForBilling', {
	    url: '/ERreadyForBilling',
	    templateUrl: 'views/AdmissionDischarge/ready-for-billing.html',
	    controller: "ERReadyForBillingController",
	    title: 'Ready for Billing',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 334);
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
 		 }
	   
	  })
	  
	  .state('dayCareReadyForBilling', {
	    url: '/dayCareReadyForBilling',
	    templateUrl: 'views/AdmissionDischarge/ready-for-billing.html',
	    controller: "dayCarReadyForBillingController",
	    title: 'Ready for Billing',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 786);
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
 		 }
	   
	  })
	  
	  .state('finalDischarge', {
	    url: '/finalDischarge',
	    templateUrl: 'views/AdmissionDischarge/final-discharge.html',
	    controller: "finalDischargeController",
	    title: 'Final Hospital Discharge',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 75);
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
 		 },params: {
 			bedManagmentObj: {} 
         }
	   
	  })
	  
	   .state('dischargedPatients', {
	    url: '/dischargedPatients',
	    templateUrl: 'views/AdmissionDischarge/discharged-patients.html',
	    controller: "dischargedPatientsController",
	    title: 'Hospital Discharged Patients List',
	  })
	  
	  .state('updateDischargeSummary', {
		 url: '/updateDischargeSummary',
		 templateUrl: 'views/AdmissionDischarge/update-discharge-summary.html',
		 controller: "updateDischargeSummaryController",
		 title: 'Update Discharge Summary',
			 params: {
				 dischargeSummObj : {}
			 }
		})
					
	  .state('ERfinalDischarge', {
		    url: '/ERfinalDischarge',
		    templateUrl: 'views/AdmissionDischarge/final-discharge.html',
		    controller: "ERfinalDischargeController",
		    title: 'Final Hospital Discharge',
		    resolve : {

	 			data: function($q, $state, $timeout) {
	 		        var deferred = $q.defer();
	 		
	 		        $timeout(function() {
	 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	 				 	.modules.filter(function(item){
	 	            	   return (item.id == 335);
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
	 		 }
		   
		  })
	  /** *****02-05-19*** */
		
		  
		   .state('erInitiateBedTransfer',
    	       {
    	        url : '/erInitiateBedTransfer',
    	        title: 'ER Initiate Bed Transfer',
    	        views :
    	         {
    	          '@' :
    	           {
    	            templateUrl : 'views/Nursing/er-initiate-bed-transfer.html',
    	            controller : 'erInitiateBedTransferController',
    	            resolve : {

    		 			data: function($q, $state, $timeout) {
    		 		        var deferred = $q.defer();
    		 		
    		 		        $timeout(function() {
    		 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
    		 				 	.modules.filter(function(item){
    		 	            	   return (item.id == 789);
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
    		 		 }
    	           }
    	         }
    	       })
    	       
    	      .state('erInitiateBedTransfer.erInitiateRequestTransferBed',
  	       {
    	    	 
    	  
   	        url : '/erInitiateRequestTransferBed',
   	        views :
   	         {
    	          '@erInitiateBedTransfer' :
   	           {
    	            templateUrl : 'views/AdmissionDischarge/er-initiate-request-transfer-bed.html',
    	            controller : 'erInitiateBedTransferController',
    	            resolve : {

    		 			data: function($q, $state, $timeout) {
    		 		        var deferred = $q.defer();
    		 		
    		 		        $timeout(function() {
    		 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
    		 				 	.modules.filter(function(item){
    		 	            	   return (item.id == 789);
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
    		 		 }
   	           }
    	         }
    	       })
    	       
    	       .state('erInitiateBedTransfer.erPhysicalInitiateTransferBed',
  	       {
   	        url : '/erPhysicalInitiateTransferBed',
   	        views :
   	         {
    	          '@erInitiateBedTransfer' :
   	           {
    	            templateUrl : 'views/Nursing/er-initiate-bed-physical.transfer.html',
    	            controller : 'MasterController',
   	           }
    	         }
    	       })
    	       
    	    .state('erTransferRequestAcceptance', {
			 url: '/erTransferRequestAcceptance',
			 templateUrl: 'views/AdmissionDischarge/er-transfer-request-acceptance.html',
			 controller: "erTransferRequestAcceptanceController",
			 title: 'ER Transfer Request Acceptance',
		})
		
		/** ********** */  
		
	  /** *****08-05-19***** */ 
		 /*
			 * .state('dayCareWorklist', { url: '/dayCareWorklist', templateUrl:
			 * 'views/AdmissionDischarge/day-care-worklist.html', controller:
			 * "MasterController" })
			 */
		.state('dayCareWorklist',
       {
        url : '/dayCareWorklist',
        title: 'Patient Acceptance',
        views :
         {
          '@' :
           {
            templateUrl : 'views/AdmissionDischarge/day-care-worklist.html',
            controller : 'dayCareWorklistMasterController',
           }
         }
       })
       
      .state('dayCareWorklist.dayCareNewAdmission',
       {
        url : '/newAdmissionList',
        title: 'Patient Acceptance',
        views :
         {
          '@dayCareWorklist' :
           {
            templateUrl : 'views/ICU/new-admission-list.html',
            controller : 'dayCareNewAdmissionAcceptanceController',
           }
         }
       })
       /*
		 * .state('dayCareWorklist.transferList', { url : '/transferList', views : {
		 * '@dayCareWorklist' : { templateUrl : 'views/ICU/transfer-list.html',
		 * controller : 'MasterController', } } })
		 */
       .state('dayCareWorklist.dayCareAdmittedPatientList',
       {
        url : '/admittedPatientList',
        title: 'Patient Acceptance',
        views :
         {
          '@dayCareWorklist' :
           {
            templateUrl : 'views/ICU/admitted-Patient-List.html',
            controller : 'dayCareAdmittedPatientListController',
            resolve : {

	 			data: function($q, $state, $timeout) {
	 		        var deferred = $q.defer();
	 		
	 		        $timeout(function() {
	 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	 				 	.modules.filter(function(item){
	 	            	   return (item.id == 823);
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
		   
            params: {
				  patientDetailsObj: {} // default value
	            }
            
           }
         }
       })
		/** ********** */  
	  
	  .state('dayCareFinalDischarge', {
	    url: '/dayCareFinalDischarge',
	    templateUrl: 'views/AdmissionDischarge/final-discharge.html',
	    controller: "dayCareFinalDischargeController",
	    title: 'Final Discharge',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 787);
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
 		 }
	  })
	  
	  .state('bedUnderMaintenance', {
	    url: '/bedUnderMaintenance',
	    templateUrl: 'views/AdmissionDischarge/bed-under-maintenance.html',
	    controller: "MasterController",
	    title: 'Ward Bed Under Maintenance',
	  })
	  
	  .state('shortLeaveDischarge', {
	    url: '/shortLeaveDischarge',
	    templateUrl: 'views/AdmissionDischarge/short-leave-discharge.html',
	    controller: "shortLeaveDischargeController",
	    title: 'Request Short Leave',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 77);
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
 		 },params: {
 			bedManagmentObj: {} 
         }
	  })
	  
	   .state('pendingShortLeave', {
	    url: '/pendingShortLeave',
	    templateUrl: 'views/AdmissionDischarge/pending-short-leave.html',
	    controller: "pendingShortLeaveController",
	    title: 'Pending Short Leave',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 264);
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
 		 },params: {
 			bedManagmentObj: {} 
         }
	  })

	/*
	 * .state('dischargeSummary', { url: '/dischargeSummary', templateUrl:
	 * 'views/AdmissionDischarge/discharge-summary.html', controller:
	 * "MasterController" })
	 */
	  .state('dischargeSummary', {
	    url: '/dischargeSummary',
	    templateUrl: 'views/AdmissionDischarge/discharge-summary.html',
	    controller: "masterDischargeSummaryController",
	    title: 'Hospital Discharge Summary',
	    params: {
			 dischargeObj : {}
		 },
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 763);
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
 		 }
	  })
	  
	   .state('dayCareDischargeSummary', {
	    url: '/dayCareDischargeSummary',
	    templateUrl: 'views/AdmissionDischarge/discharge-summary.html',
	    controller: "dayCareDischargeSummaryController",
	    title: 'Discharge Summary',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 783);
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
 		 }
	  })
	  
	  .state('shortLeaveStatus', {
	    url: '/shortLeaveStatus',
	    templateUrl: 'views/AdmissionDischarge/short-leave-status.html',
	    controller: "shortLeaveStatusController",
	    title: 'Short Leave Status',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 78);
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
 		params: {
 			bedManagmentObj: {} 
         }
	  })
	  
	  .state('icuBedBookingStatus', {
	    url: '/icuBedBookingStatus',
	    templateUrl: 'views/AdmissionDischarge/icu-bed-booking-status.html',
	    controller: "MasterController",
	    title: 'ICU Bed Booking Status'
	  })
	  
	  .state('newBornRegistration', {
	    url: '/newBornRegistration',
	    templateUrl: 'views/AdmissionDischarge/new-born-registration.html',
	    controller: "newBornBabyRegController",
	    title: 'New Born Registration',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 266);
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
 		 }
	  })
	  
	   .state('cradleBedAllocation', {
	    url: '/cradleBedAllocation',
	    templateUrl: 'views/AdmissionDischarge/cradle-bed-allocation.html',
	    controller: "MasterController",
	    title: 'Cradle Bed Allocation',
	  })
	  
	  .state('newBornDetails', {
	    url: '/newBornDetails',
	    templateUrl: 'views/AdmissionDischarge/new-born-Details.html',
	    controller: "newBornBabyDisplayController",
	    title: 'New Born Details',
	  })
	 
    
     /**
		 * ******************************************** End ADMISSION DISCHARGE
		 * ********************************************
		 */
	  
	  
	  /**
		 * ******************************************** START MORGUE
		 * ********************************************
		 */
	  
	  .state('morguePendinglist', {
	    url: '/morguePendinglist',
	    templateUrl: 'views/Morgue/morgue-pending-list.html',
	    controller: "morguePendinglistController",
	    title: 'Mortuary Pending list',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 364);
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
 		 }
	  })
	   .state('morgueAvailability', {
	    url: '/morgueAvailability',
	    templateUrl: 'views/Morgue/morgue-availability.html',
	    controller: "morgueAvailabilityController",
	    title: 'Mortuary Availability',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 365);
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
 		 }
	  })
	  .state('morgueReservation', {
	    url: '/morgueReservation',
	    templateUrl: 'views/Morgue/morgue-reservation.html',
	    controller: "morgueReservationController",
	    title: 'Mortuary Reservation',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 366);
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
 		 }
	  })
	   .state('morgueReleased', {
	    url: '/morgueReleased',
	    templateUrl: 'views/Morgue/morgue-released.html',
	    controller: "morgueReleasedController",
	    title: 'Release Mortuary',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 367);
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
 		 }
	  })
	  
	  /**
		 * ******************************************** END MORGUE
		 * ********************************************
		 */
	
	  .state('testMaster', {
	    url: '/testMaster',
	    templateUrl: 'views/LIS/test-master.html',
	    controller: "singleParameterTestController",
	    title: 'Test Master',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 558);
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
 		 }
	  })
	  .state('multiparameterTestMaster', {
	    url: '/multiparameterTestMaster',
	    templateUrl: 'views/LIS/multiparameter-master.html',
	    controller: "multiParameterTestController",
	    title: 'Multiparameter Test Master',
	    resolve : { 

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 563);
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
 		 }
	  })
	  
	  
	  
	  
	  /**
		 * ******************************************** Start LIS Histopathology
		 * ********************************************
		 */
	  
	  .state('addendumReport', {
	    url: '/addendumReport',
	    templateUrl: 'views/LIS/Histopathology/addendum-report.html',
	    controller: "MasterController",
	    title: 'Addendum Report',
	  })
	  .state('blockCreation', {
	    url: '/blockCreation',
	    templateUrl: 'views/LIS/Histopathology/block-creation.html',
	    controller: "blockCreationController",
	    title: 'Block Creation',
	    resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 298);
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
 		 }
	  })
		  

	  	.state('microscopeExamination', {
	    url: '/microscopeExamination',
	    templateUrl: 'views/LIS/Histopathology/microscope-examination.html',
	    controller: "histoMicroScopicWorklistController",
	    title: 'Macroscopic Examination Work List',
	   })
		  	.state('macroscopicExamination', 
		     {
		             url: '/macroscopicExamination',
		             templateUrl: 'views/LIS/Histopathology/macroscopic-examination.html',
		             controller: "histMacroScopicWorklistController",
		             title: 'Macroscopic Examination Work List',
		             params: 
		             {
		            	 microScopeObj: {} // default value
		             }
		    }) 
			.state('viewMacroscopicExamination', 
		     {
	             url: '/viewMacroscopicExamination',
	             templateUrl: 'views/LIS/Histopathology/details-view-macroscopic-examination.html',
	             controller: "masterController",
	             title: 'Macroscopic Examination Work List',
		           
		    }) 
		    .state('detailsMicroscopicExamination', 
		     {
	             url: '/detailsMicroscopicExamination',
	             templateUrl: 'views/LIS/Histopathology/details-microscopic-examination.html',
	             controller: "masterController",
	             title: 'Macroscopic Examination Work List',
		           
		    }) 
	  .state('microscopicExaminiationWorkList', {
		    url: '/microscopicExaminiationWorkList',
		    templateUrl: 'views/LIS/Histopathology/microscopic-examination-worklist.html',
		    controller: "microscopicExaminiationWorkListController",
		    title: 'Microscopic Examination Work List',
		    resolve : {

      			data: function($q, $state, $timeout) {
      		        var deferred = $q.defer();
      		
      		        $timeout(function() {
      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
      				 	.modules.filter(function(item){
      	            	   return (item.id == 300);
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
      		 }
		  })
		  
		 /* Details Microscopic Examiniation date - 26April */
		  
	   .state('detailsMicroscopicExaminiation', {
		    url: '/detailsMicroscopicExaminiation',
		    templateUrl: 'views/LIS/Histopathology/details-microscopic-examination-popup.html',
		    controller: "detailsMicroscopicExaminiationWorkListController",
		    title: 'Microscopic Examination Work List',
		    params: 
	             {
		    	    subSpecimenObj: {} // default value
	             }
		  })
	
		  .state('restainingRequestWorklistDetails',
				  {
			  		url : '/restainingRequestWorklistDetails',
			  		templateUrl : 'views/LIS/Histopathology/re-staining-request-worklist-details.html',
			  		controller : "restainRequestWorklistDetailsController",
			  		title: 'Re-staining Request Worklist',
			  		params: 
			  			{
			  				restainReqMstObj: {} // default value
			  			} 
				  })
		  .state('reportCreationRelease', {
		    url: '/reportCreationRelease',
		    templateUrl: 'views/LIS/Histopathology/report-creation-release.html',
		    controller: "histoReportCreationController",
		    title: 'Report Creation And Release',
		    params: 
	             {
		    		 backFlag : null,
	            	 subSpecimenObj: {}, // default value
	            	 searchObj : {}
	             }		
		  })
		  .state('restainingRequest', {
		    url: '/restainingRequest',
		    templateUrl: 'views/LIS/Histopathology/restaining-request-creation.html',
		    controller: "histoRestainRequestController",
		    title: 'Restaining Request',
		    params: 
	             {
	            	 subSpecimenObj: {} // default value
	             }	
		  })
		  .state('slideCreation', {
		    url: '/slideCreation',
		    templateUrl: 'views/LIS/Histopathology/slide-creation.html',
		    controller: "slideCreationController",
		    title: 'Slide Creation',
		    resolve : {

      			data: function($q, $state, $timeout) {
      		        var deferred = $q.defer();
      		
      		        $timeout(function() {
      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
      				 	.modules.filter(function(item){
      	            	   return (item.id == 299);
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
      		 }
		  })
		  .state('restainingRequestWorklist', {
		    url: '/restainingRequestWorklist',
		    templateUrl: 'views/LIS/Histopathology/re-staining-request-worklist.html',
		    controller: "restainRequestWorklistController",
		    title: 'Re-staining Request Worklist',
		    resolve : {

      			data: function($q, $state, $timeout) {
      		        var deferred = $q.defer();
      		
      		        $timeout(function() {
      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
      				 	.modules.filter(function(item){
      	            	   return (item.id == 301);
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
      		 }
		  })
		  
		  .state('specimenReceipt', {
			    url: '/specimenReceipt',
			    templateUrl: 'views/LIS/Histopathology/specimen-receipt.html',
			    controller: "specimenReceiptController",
			    title: 'Specimen Receipt',
			    resolve : {

	      			data: function($q, $state, $timeout) {
	      		        var deferred = $q.defer();
	      		
	      		        $timeout(function() {
	      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	      				 	.modules.filter(function(item){
	      	            	   return (item.id == 296);
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
	      		 }
			  })
			  
			  .state('requestForSecondOpinion', {
			    url: '/requestForSecondOpinion',
			    templateUrl: 'views/LIS/Histopathology/req-second-opinion.html',
			    controller: "MasterController",
			    title: 'Second Opinion Request',
			  	})
			  
			  	.state('secondOpinionRequest', {
			    url: '/secondOpinionRequest',
			    templateUrl: 'views/LIS/Histopathology/second-opinion-request.html',
			    controller: "MasterController",
			    title: 'Second Opinion Request',
			  	})
			  	
			  	.state('pendingSlideForSecondOpinion', {
			    url: '/pendingSlideForSecondOpinion',
			    templateUrl: 'views/LIS/Histopathology/pending-slide-from-second-opinion.html',
			    controller: "MasterController",
			    title: 'Slides for Second Opinion',
			  	})
			  	
			  	.state('acceptanceForStorage', {
			    url: '/acceptanceForStorage',
			    templateUrl: 'views/LIS/Histopathology/acceptance-for-storage.html',
			    controller: "MasterController",
			    title: 'Acceptance for Storage',
			  	})
	  
		.state('storage', {
		    url: '/storage',
		    templateUrl: 'views/LIS/Histopathology/storage.html',
		    controller: "tStorageController",
		    title: 'Storage',
		    resolve : {

      			data: function($q, $state, $timeout) {
      		        var deferred = $q.defer();
      		
      		        $timeout(function() {
      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
      				 	.modules.filter(function(item){
      	            	   return (item.id == 303);
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
      		 }
		  })
		  .state('frozenSectionRequestList ', {
		    url: '/frozenSectionRequestList',
		    templateUrl: 'views/LIS/Histopathology/frozen-section-request-list.html',
		    controller: "frozenSectionRequestListController",
		    title: 'Frozen Section Request List',
		    resolve : {

      			data: function($q, $state, $timeout) {
      		        var deferred = $q.defer();
      		
      		        $timeout(function() {
      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
      				 	.modules.filter(function(item){
      	            	   return (item.id == 306);
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
      		 }
		  })
		  .state('outSourcedPendinglistAssignment ', {
		    url: '/outSourcedPendinglistAssignment',
		    templateUrl: 'views/LIS/Outsource/outsourced-pendinglist-assignment.html',
		    controller: "outSourcePendingListController",
		    title: 'Out Sourced Pending List Assignment',
		    resolve : {

      			data: function($q, $state, $timeout) {
      		        var deferred = $q.defer();
      		
      		        $timeout(function() {
      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
      				 	.modules.filter(function(item){
      	            	   return (item.id == 317);
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
      		 }
		  })
		   .state('pendinglistForSampleDispatch ', {
		    url: '/pendinglistForSampleDispatch',
		    templateUrl: 'views/LIS/Outsource/pending-list-for-sample-dispatch.html',
		    controller: "pendinglistForSampleDispatchController",
		    title: 'Pending List for Sample Dispatch',
		    resolve : {

      			data: function($q, $state, $timeout) {
      		        var deferred = $q.defer();
      		
      		        $timeout(function() {
      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
      				 	.modules.filter(function(item){
      	            	   return (item.id == 318);
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
      		 }
		  })
		   .state('pendinglistForReportUpload ', {
		    url: '/pendinglistForReportUpload',
		    templateUrl: 'views/LIS/Outsource/pending-list-for-report-upload.html',
		    controller: "pendinglistForReportUploadController",
		    title: 'Pending List for Pending Upload',
		    resolve : {

      			data: function($q, $state, $timeout) {
      		        var deferred = $q.defer();
      		
      		        $timeout(function() {
      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
      				 	.modules.filter(function(item){
      	            	   return (item.id == 319);
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
      		 }
		  })
		  .state('workloadDashboard', {
		    url: '/workloadDashboard',
		    templateUrl: 'views/LIS/workload-dash-board.html',
		    controller: "workloadDashboardController",
		    title: 'Workload Dashboard',
		    resolve : {

      			data: function($q, $state, $timeout) {
      		        var deferred = $q.defer();
      		
      		        $timeout(function() {
      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
      				 	.modules.filter(function(item){
      	            	   return (item.id == 320);
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
      		 }
		  })
	  /**
		 * ******************************************** END Histopathology
		 * ********************************************
		 */
      
	  /**
		 * ******************************************** Start LIS Microbiology
		 * ********************************************
		 */
		  
		  .state('observationDuringIncubation', {
		    url: '/observationDuringIncubation',
		    templateUrl: 'views/LIS/Microbiology/observation-during-incubation.html',
		    controller: "incubationObjservationController",
		    title: 'Observation During Incubation',
		  })
		  .state('sampleAcceptance', {
		    url: '/sampleAcceptance',
		    templateUrl: 'views/LIS/Microbiology/sample-acceptance.html',
		    controller: "microbiologyAcceptancePendingController",
		    title: 'Sample Acceptance',
		    resolve : {

      			data: function($q, $state, $timeout) {
      		        var deferred = $q.defer();
      		
      		        $timeout(function() {
      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
      				 	.modules.filter(function(item){
      	            	   return (item.id == 308);
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
      		 }
		  })
		 .state('sampleProcessingWorkList', {
		    url: '/sampleProcessingWorkList',
		    templateUrl: 'views/LIS/Microbiology/sample-processing-work-list.html',
		    controller: "microbiologyWorklistController",
		    title: 'Work List for Department',
		    resolve : {

      			data: function($q, $state, $timeout) {
      		        var deferred = $q.defer();
      		
      		        $timeout(function() {
      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
      				 	.modules.filter(function(item){
      	            	   return (item.id == 309);
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
      		 }
		  })
		  .state('sensitivityTestingProcess', {
			    url: '/sensitivityTestingProcess',
			    templateUrl: 'views/LIS/Microbiology/sensitivity-testing-process.html',
			    controller: "sensitivityTestingController",
			    title: 'Sensitivity Testing Process',
			    resolve : {

	      			data: function($q, $state, $timeout) {
	      		        var deferred = $q.defer();
	      		
	      		        $timeout(function() {
	      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	      				 	.modules.filter(function(item){
	      	            	   return (item.id == 314);
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
	      		 }
			  })
		  
		.state('biochemicalTesting', {
		    url: '/biochemicalTesting',
		    templateUrl: 'views/LIS/Microbiology/biochemical-testing.html',
		    controller: "biochemicalResultWorklistController",
		    title: 'Biochemical Testing',
		    resolve : {

      			data: function($q, $state, $timeout) {
      		        var deferred = $q.defer();
      		
      		        $timeout(function() {
      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
      				 	.modules.filter(function(item){
      	            	   return (item.id == 312);
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
      		 }
		 })
		 .state('detailsBiochemicalTesting', {
		    url: '/detailsBiochemicalTesting',
		    templateUrl: 'views/LIS/Microbiology/details-biochemical-testing.html',
		    controller: "biochemicalResultDetailsController",
		    title: 'Biochemical Testing',
		    params: 
	        {
		    	biochemicalResultObj: {} // default value
            }	
		 })
		 .state('microscopicExamination', {
		    url: '/microscopicExamination',
		    templateUrl:'views/LIS/Microbiology/microscopic-examination.html',
		    controller: "microscopicExaminationController",
		    title: 'Microscopic Examination',
		    resolve : {

      			data: function($q, $state, $timeout) {
      		        var deferred = $q.defer();
      		
      		        $timeout(function() {
      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
      				 	.modules.filter(function(item){
      	            	   return (item.id == 313);
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
      		 }
		 })
		 .state('detailsMicroscopicExam', {
		    url: '/detailsMicroscopicExam',
		    templateUrl:'views/LIS/Microbiology/details-microscopic-exam-LIS.html',
		    controller: "detailsMicroscopicExaminationController",
		    title: 'Microscopic Examination',
		    params: 
	        {
		    	microExaObj: {} // default value
            }	
		 })
	/**
	 * ******************************************** END LIS Microbiology
	 * ********************************************
	 */
    /**
	 * ******************************************** START LIS Phlebotomy
	 * ********************************************
	 */
	
		.state('patientArrival', {
		    url: '/patientArrival',
		    templateUrl: 'views/LIS/Phlebotomy/patient-arrival.html',
		    controller: "patientArrivalController",
		    title: 'Patient Arrival List',
		    resolve : {

      			data: function($q, $state, $timeout) {
      		        var deferred = $q.defer();
      		
      		        $timeout(function() {
      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
      				 	.modules.filter(function(item){
      	            	   return (item.id == 276);
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
      		 }
		  })
		.state('patientArrivalForRepeatSampleCollection', {
		    url: '/patientArrivalForRepeatSampleCollection',
		    templateUrl: 'views/LIS/Phlebotomy/patient-arrival-for-repeat-sample-collection.html',
		    controller: "patientArrivalForRepeatSampleCollectionController",
		    title: 'Repeat Sample Collection',
		    resolve : {

      			data: function($q, $state, $timeout) {
      		        var deferred = $q.defer();
      		
      		        $timeout(function() {
      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
      				 	.modules.filter(function(item){
      	            	   return (item.id == 277);
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
      		 }
		  })
		.state('phlebotomyWorkList', {
		    url: '/phlebotomyWorkList',
		    templateUrl: 'views/LIS/Phlebotomy/phlebotomy-work-list.html',
		    controller: "phlebotomyController",
		    title: 'Phlebotomy Work List',
		    resolve : {

      			data: function($q, $state, $timeout) {
      		        var deferred = $q.defer();
      		
      		        $timeout(function() {
      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
      				 	.modules.filter(function(item){
      	            	   return (item.id == 274);
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
      		 }
		  })
		 .state('userAllocationForHomecollection', {
    	 url: '/userAllocationForHomecollection',
    	 templateUrl: 'views/LIS/Phlebotomy/user-allocation-for-home-collection.html',
    	 controller: "userAllocationForHomecollectionController",
    	 title: 'User Allocation For Home Collection',
		 })
		 .state('labHomeSampleCollection', {
    	 url: '/labHomeSampleCollection',
    	 templateUrl: 'views/LIS/Phlebotomy/lab-home-sample-collection.html',
    	 controller: "labHomeSampleCollectionController",
    	 title: 'Lab Home Sample Collection',
		 })
		 .state('consolidatedResultEntry', {
    	 url: '/consolidatedResultEntry',
    	 templateUrl: 'views/LIS/Phlebotomy/consolidated_result_entry.html',
    	 controller: "consolidatedResultEntryController",
    	 title: 'Consolidated Result Entry',
    	 params: 
	        {
     			searchObj : {}
	        }	
		 })
		 .state('consolidatedReportValidation', {
    	 url: '/consolidatedReportValidation',
    	 templateUrl: 'views/LIS/Phlebotomy/consolidated_report_validation.html',
    	 controller: "consolidatedReportValidationController",
    	 title: 'Consolidated Report Validation',
    	 params: 
	        {
    		 	searchObj : {}
	        }	
		 })
		 .state('consolidatedReportRelease', {
    	 url: '/consolidatedReportRelease',
    	 templateUrl: 'views/LIS/Phlebotomy/consolidated_report_release.html',
    	 controller: "consolidatedReportReleaseController",
    	 title: 'Consolidated Report Release',
    	 params: 
  		 {
  			searchObj : {}
  		 }
		 })
		.state('sendToCR', {
			    url: '/sendToCR',
			    templateUrl: 'views/LIS/Phlebotomy/send-to-CR.html',
			    controller: "sendToCrController",
			    title: 'Send To CR',
			    resolve : {

	      			data: function($q, $state, $timeout) {
	      		        var deferred = $q.defer();
	      		
	      		        $timeout(function() {
	      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	      				 	.modules.filter(function(item){
	      	            	   return (item.id == 275);
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
	      		 }
			  })

    /**
	 * ******************************************** END LIS Phlebotomy
	 * ********************************************
	 */
	/**
	 * ******************************************** Start LIS Central Area
	 * ********************************************
	 */
			  
		.state('centrifugationWorkList', {
			    url: '/centrifugationWorkList',
			    templateUrl: 'views/LIS/CentralArea/centrifugation-work-list.html',
			    controller: "centrifugationWorklistController",
			    title: 'Centrifugation Work List',
			    resolve : {

	      			data: function($q, $state, $timeout) {
	      		        var deferred = $q.defer();
	      		
	      		        $timeout(function() {
	      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	      				 	.modules.filter(function(item){
	      	            	   return (item.id == 280);
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
	      		 }
			  })
			.state('sampleRecollection', {
			    url: '/sampleRecollection',
			    templateUrl: 'views/LIS/CentralArea/sample-recollection.html',
			    controller: "sampleRecollectionController",
			    title: 'Sample Recollection',
			    resolve : {

	      			data: function($q, $state, $timeout) {
	      		        var deferred = $q.defer();
	      		
	      		        $timeout(function() {
	      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	      				 	.modules.filter(function(item){
	      	            	   return (item.id == 278);
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
	      		 }
			  })		  
			  
	    .state('configurationWorkList', {
			    url: '/configurationWorkList',
			    templateUrl: 'views/LIS/CentralArea/configuration-work-list.html',
			    controller: "MasterController",
			    title: 'Centrifugation Work List',
			  })
		.state('inPatient', {
		 	    url: '/inPatient',
			    templateUrl: 'views/LIS/CentralArea/in-patient.html',
			    controller: "inPatientController",
			    title: 'Sample Collection-In Patient',
			    resolve : {

	      			data: function($q, $state, $timeout) {
	      		        var deferred = $q.defer();
	      		
	      		        $timeout(function() {
	      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	      				 	.modules.filter(function(item){
	      	            	   return (item.id == 282);
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
	      		 }
			  })
	    .state('outPatient', {
		 	    url: '/outPatient',
			    templateUrl : 'views/LIS/CentralArea/out-patient.html',
			    controller: "OutPatientController",
			    title: 'Sample Collection-Out Patient',
			    resolve : {

	      			data: function($q, $state, $timeout) {
	      		        var deferred = $q.defer();
	      		
	      		        $timeout(function() {
	      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	      				 	.modules.filter(function(item){
	      	            	   return (item.id == 281);
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
	      		 }
			  })
			  
	/**
	 * ===============================BIOCHEMISTRY & HEAMOTOLOGY DEPT START
	 * ===============================
	 */		  
	    .state('pendingListForAcceptance', 
	    	{
		 	    url: '/pendingListForAcceptance/:subDeptId',
			    templateUrl: 'views/LIS/CentralArea/pending-list-for-acceptance.html',
			    controller: "sampleAcceptancePendingController",
			    title: 'Acceptance Pending List',
			    resolve: {
			    	data: function($q, $state, $timeout) {
	      		        var deferred = $q.defer();
	      		
	      		        $timeout(function() {
	      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	      				 	.modules.filter(function(item){
	      	            	   return (item.id == 284 || item.id == 290 || item.id == 1041);
	      				 	});
	      					if(arr.length <= 0){
	      						$state.go("accessDenied")
	      						deferred.reject();
	      					 } else {
	      						deferred.resolve();
	      					 }
	      		        });
	      		
	      		        return deferred.promise;
	      		      },
			            subDeptId: function($stateParams, GenericService)
			            {
			                return $stateParams.subDeptId;
			            }
	                }
			  })
	   .state('worklistForDepartment', {
		 	    url: '/worklistForDepartment/:subDeptId',
			    templateUrl: 'views/LIS/CentralArea/department-worklist.html',
			    controller: "biochemistryWorklistController",
			    title: 'Work List for Department',
			    resolve: {
			    	data: function($q, $state, $timeout) {
	      		        var deferred = $q.defer();
	      		
	      		        $timeout(function() {
	      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	      				 	.modules.filter(function(item){
	      	            	   return (item.id == 285 || item.id == 291 || item.id == 1042);
	      				 	});
	      					if(arr.length <= 0){
	      						$state.go("accessDenied")
	      						deferred.reject();
	      					 } else {
	      						deferred.resolve();
	      					 }
	      		        });
	      		
	      		        return deferred.promise;
	      		      },
		            subDeptId: function($stateParams, GenericService)
		            {
		                return $stateParams.subDeptId;
		            }
                }
			  })	  
	   .state('worklistForReportEntry', {
			 	    url: '/worklistForReportEntry/:subDeptId',
				    templateUrl: 'views/LIS/CentralArea/report-entry-worklist.html',
				    controller: "worklistForReportEntryController",
				    title: 'Report Entry Work List',
				    resolve: {
				    	data: function($q, $state, $timeout) {
		      		        var deferred = $q.defer();
		      		
		      		        $timeout(function() {
		      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
		      				 	.modules.filter(function(item){
		      	            	   return (item.id == 286 || item.id == 292 || item.id == 1043);
		      				 	});
		      					if(arr.length <= 0){
		      						$state.go("accessDenied")
		      						deferred.reject();
		      					 } else {
		      						deferred.resolve();
		      					 }
		      		        });
		      		
		      		        return deferred.promise;
		      		      },
			            subDeptId: function($stateParams, GenericService)
			            {
			                return $stateParams.subDeptId;
			            }
	                },
	                params: 
	    	        {
	                	searchObj : {}
	    	        }	
				    
				  })
		.state('deatilsReportEntryWorklist', {
			 	    url: '/deatilsReportEntryWorklist/:subDeptId',
				    templateUrl: 'views/LIS/CentralArea/details-report-entry-worklist.html',
				    controller: "deatilsReportEntryWorklistController",
				    title: 'Report Entry Work List',
				    resolve: 
				    {
			            subDeptId: function($stateParams, GenericService)
			            {
			                return $stateParams.subDeptId;
			            }
	                },
				    params: 
				        {
				    		resultMstList: [], // default value,
	                		searchObj : {},
	                		nextRecordList : []
			            }	
				  })
		.state('worklistForValidation', {
	 	    url: '/worklistForValidation/:subDeptId',
		    templateUrl: 'views/LIS/CentralArea/validation-worklist.html',
		    controller: "worklistForValidationController",
		    title: 'Validation Work List',
		    resolve: {
		    	data: function($q, $state, $timeout) {
      		        var deferred = $q.defer();
      		
      		        $timeout(function() {
      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
      				 	.modules.filter(function(item){
      	            	   return (item.id == 287 || item.id == 293 || item.id == 1044);
      				 	});
      					if(arr.length <= 0){
      						$state.go("accessDenied")
      						deferred.reject();
      					 } else {
      						deferred.resolve();
      					 }
      		        });
      		
      		        return deferred.promise;
      		      },
			            subDeptId: function($stateParams, GenericService)
			            {
			                return $stateParams.subDeptId;
			            }
	                },
	                params: 
	    	        {
	                	searchObj : {}
	    	        }	
		  })
		.state('detailsWorklistForValidation', {
	 	    	url: '/detailsWorklistForValidation/:subDeptId',
	 	    	templateUrl: 'views/LIS/CentralArea/details-validation-worklist.html',
	 	    	controller: "detailsResultValidationController",
	 	    	title: 'Validation Work List',
	 	    	 resolve: 
	 	    	 {
			            subDeptId: function($stateParams, GenericService)
			            {
			                return $stateParams.subDeptId;
			            }
	                },
	 	    	params: 
	 	    	{
	 	    		resultMstList: [], // default value
	 	    		searchObj : {},
                    nextRecordList : []
	 	    	}	
		  })		  
		 .state('worklistForReportRelease', 
		    {
		 	    url: '/worklistForReportRelease/:subDeptId',
			    templateUrl: 'views/LIS/CentralArea/release-report-worklist.html',
			    controller: "worklistForReportReleaseController",
			    title : 'Report Release Work List',
			    resolve: {
			    	data: function($q, $state, $timeout) {
	      		        var deferred = $q.defer();
	      		
	      		        $timeout(function() {
	      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	      				 	.modules.filter(function(item){
	      	            	   return (item.id == 288 || item.id == 294 || item.id == 1045);
	      				 	});
	      					if(arr.length <= 0){
	      						$state.go("accessDenied")
	      						deferred.reject();
	      					 } else {
	      						deferred.resolve();
	      					 }
	      		        });
	      		
	      		        return deferred.promise;
	      		      },
		            subDeptId: function($stateParams, GenericService)
		            {
		                return $stateParams.subDeptId;
		            }
                },
                params:{
                	searchObj : {},
                }
			  })
		.state('detailsWorklistForReportRelease', 
				{
		 	 	url: '/detailsWorklistForReportRelease/:subDeptId',
		 	 	templateUrl: 'views/LIS/CentralArea/details-release-report-worklist.html',
		 	 	controller: "detailsReportReleaseController", 
		 	 	title : 'Report Release Work List',
		 	 	 resolve: {
			            subDeptId: function($stateParams, GenericService)
			            {
			                return $stateParams.subDeptId;
			            }
	                },
		 	 	params: 
				        {
		 	 		 	   backFlag : null,
		 	 		       resultMstList: [], // default value
		 	 		       searchObj : {},
                           nextRecordList : [],
                           isOnHoldParameter :null
			            }	
		  })	  
		/**
		 * ===============================BIOCHEMISTRY & HEAMOTOLOGY DEPT END
		 * ===============================
		 */		  
		.state('worklistForPrinting', {
		 	    url: '/worklistForPrinting',
			    templateUrl: 'views/LIS/CentralArea/printing-worklist.html',
			    controller: "worklistForPrintingController",
			    title: 'Printing Work List',
			    resolve : {

	      			data: function($q, $state, $timeout) {
	      		        var deferred = $q.defer();
	      		
	      		        $timeout(function() {
	      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	      				 	.modules.filter(function(item){
	      	            	   return (item.id == 315);
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
	      		params: 
		        {
	   			 searchObj : {}
	            }
			  })
			  .state('bulkEmailWorklist', {
		 	    url: '/bulkEmailWorklist',
			    templateUrl: 'views/LIS/CentralArea/bulk-email-worklist.html',
			    controller: "bulkEmailWorklistController",
			    title: 'Bulk Email Work List'
			  })
		.state('techniqunitionworklist', {
			 	    url: '/techniqunitionworklist',
				    templateUrl: 'views/LIS/CentralArea/techniqunition-worklist.html',
				    controller: "MasterController",
				    title: 'Work List for Techniqunition',
				  })
   /**
	 * ******************************************** END LIS Central Area
	 * ********************************************
	 */
   /**
	 * ******************************************** Start Pharmacy and Inventory
	 * ********************************************
	 */
	/**
	 * ***************** Pharmacology - In Patient pages controller
	 * start*************************
	 */
	
	
	.state('detailsPatientRejectedRejectionConsumable', {
		 url: '/detailsPatientRejectedRejectionConsumable',
		 templateUrl: 'views/PharmacyInventory/InPatient/details-patient-rejected-rejection-consumable.html',
		 controller: "MasterController",
	})
	/*
	 * .state('topUpOrder', { url: '/topUpOrder', templateUrl:
	 * 'views/PharmacyInventory/InPatient/top-up-order.html', controller:
	 * "MasterController" })
	 */
	
	
	/** **** patient Indent page Added on - 27/03**** */
	.state('topOrderList', {
		 url: '/topOrderList',
		 templateUrl: 'views/PharmacyInventory/InPatient/top-order-up-list.html',
		 title: 'Top Up Order List',
		 controller: "" +
		 		""
	})
	
	.state('storageLocationMasterForSlideBlocks', {
		 url: '/storageLocationMasterForSlideBlocks',
		 templateUrl: 'views/PharmacyInventory/InPatient/storage-location-master-for-slide-blocks.html',
		 controller: "storageLocationMstForSlideBlocksController",
		 title: 'Storage Location Master for Slide Blocks',
		    resolve : {

      			data: function($q, $state, $timeout) {
      		        var deferred = $q.defer();
      		
      		        $timeout(function() {
      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
      				 	.modules.filter(function(item){
      	            	   return (item.id == 568);
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
      		 }
		  })
	.state('encounterQueueManagement', {
		 url: '/encounterQueueManagement',
		 templateUrl: 'views/master/GlobalMaster/Encounter/encounter-queue-management.html',
		 controller: "encounterQueueManagementController",
		 title: 'Nursing Worklist',
		    resolve : {

      			data: function($q, $state, $timeout) {
      		        var deferred = $q.defer();
      		
      		        $timeout(function() {
      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
      				 	.modules.filter(function(item){
      	            	   return (item.id == 702);
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
      		 }
	})
	.state('encounterQueueEHC', {
		 url: '/encounterQueueEHC',
		 templateUrl: 'views/master/GlobalMaster/Encounter/encounter-queue-ehc.html',
		 controller: "encounterQueueEHCController",
		 title: 'Encounter Queue EHC',
		 resolve : {

      			data: function($q, $state, $timeout) {
      		        var deferred = $q.defer();
      		
      		        $timeout(function() {
      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
      				 	.modules.filter(function(item){
      	            	   return (item.id == 686);
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
      		 }
	})
	/** ******** */
	
	.state('dischargeMedicationHandover', {
		url: '/dischargeMedicationHandover',
		templateUrl: 'views/PharmacyInventory/InPatient/discharge-medication-hand-over.html',
		controller: "dischargeMedicationHandoverController",
		title: 'Discharge Medication Hand Over',
		params: 
		{
			patientDetailsObj: {}
		} 
	})
	.state('erDischargeMedicationHandover', {
		 url: '/erDischargeMedicationHandover',
		 templateUrl: 'views/PharmacyInventory/InPatient/er-discharge-medication-hand-over.html',
		 controller: "erDischargeMedicationHandoverController",
		 title: 'ER Discharge Medication Hand Over',
	})
	/*
	 * .state('homecareMedicationEntry', { url: '/homecareMedicationEntry',
	 * templateUrl:
	 * 'views/PharmacyInventory/InPatient/homecare-medication-entry.html',
	 * controller: "MasterController", title: 'Homecare Medication Entry',
	 * resolve : {
	 * 
	 * data: function($q, $state, $timeout) { var deferred = $q.defer();
	 * 
	 * $timeout(function() { var arr =
	 * JSON.parse(localStorage.getItem('permissions'))
	 * .modules.filter(function(item){ return (item.id == 80 ||item.id == 84);
	 * }); if(arr.length <= 0){ $state.go("accessDenied") deferred.reject(); }
	 * else { deferred.resolve(); } });
	 * 
	 * return deferred.promise; } } })
	 */
	.state('homecareMedicationApproval', {
		 url: '/homecareMedicationApproval',
		 templateUrl: 'views/PharmacyInventory/InPatient/homecare-medication-approval.html',
		 controller: "MasterController",
		 title: 'Homecare Medication Approval',
	})
	/** ****** 31 july 2017 added ******** */
	// .state('billSalesReturn', {
	// url: '/billSalesReturn',
	// templateUrl: 'views/PharmacyInventory/InPatient/bill-sales-return.html',
	// controller: "MasterController"
	// })
	// .state('addNewbillSalesReturn', {
	// url: '/addNewbillSalesReturn',
	// templateUrl:
	// 'views/PharmacyInventory/InPatient/add-new-bill-sales-return.html',
	// controller: "MasterController"
	// })
 
	.state('addNewMaterialIssue', {
		 url: '/addNewMaterialIssue',
		 templateUrl: 'views/PharmacyInventory/InPatient/add-new-material-issue.html',
		 controller: "MasterController",
		 title: 'Material Issue',
	})
	.state('topUpIndent', {
		 url: '/topUpIndent',
		 templateUrl: 'views/PharmacyInventory/InPatient/top-up-indent.html',
		 controller: "MasterController",
		 title: 'Patient Indent',
	})
	.state('expiredItemsReturns', {
		 url: '/expiredItemsReturns',
		 templateUrl: 'views/PharmacyInventory/InPatient/expired-items-returns.html',
		 controller: "MasterController",
		 title: 'Expired Items Returns',
	})
	.state('expiredReturnAcceptance', {
		 url: '/expiredReturnAcceptance',
		 templateUrl: 'views/PharmacyInventory/InPatient/expired-return-acceptance.html',
		 controller: "MasterController",
		 title: 'Expired Return Acceptance',
	})
	/**
	 * *******Inventory new pages Added on - 5-April-2018 controller start
	 * ********
	 */
	/**
	 * *******Inventory new pages Added on - 5-April-2018 controller Start
	 * ********
	 */
	.state('consumbleAssetIndent', {
		 url: '/consumbleAssetIndent',
		 templateUrl: 'views/PharmacyInventory/Consumable-Inventory/consumble-asset-indent.html',
		 controller: "MasterController",
		 title: 'Consumble Asset Indent',
	})
	.state('assetIssue', {
		 url: '/assetIssue',
		 templateUrl: 'views/PharmacyInventory/Consumable-Inventory/asset-issue.html',
		 controller: "MasterController",
		 title: 'Asset Issue',
	})
	.state('assetIssueDetails', {
		 url: '/assetIssueDetails',
		 templateUrl: 'views/PharmacyInventory/Consumable-Inventory/asset-issue-details.html',
		 controller: "MasterController",
		 title: 'Asset Issue Details',
	})	
	.state('internalSiteVisit', {
		 url: '/internalSiteVisit',
		 templateUrl: 'views/PharmacyInventory/Consumable-Inventory/internal-site-visit.html',
		 controller: "MasterController",
		 title: 'Internal Site Visit',
	})
	.state('externalSiteVisit', {
		 url: '/externalSiteVisit',
		 templateUrl: 'views/PharmacyInventory/Consumable-Inventory/external-site-visit.html',
		 controller: "MasterController",
		 title: 'External Site Visit',
	})
	
	.state('biochemicalTestMaster', {
		 url: '/biochemicalTestMaster',
		 templateUrl: '',
		 controller: "MasterController",
		 title: 'Biochemical Test Master',
	})
	/**
	 * *******Inventory new pages Added on - 5-April-2018 controller end
	 * ********
	 */
	/**
	 * ***************** In Patient pages controller
	 * end*************************
	 */
	/**
	 * ***************** Stock Adjustment pages controller
	 * start*************************
	 */

	
	.state('detailsStopTransaction', {
		 url: '/detailsStopTransaction',
		 templateUrl: 'views/PharmacyInventory/StockAdjustment/details-stop-transaction.html',
		 controller: "MasterController",
	})
	
	.state('rejectedUploaded', {
		 url: '/rejectedUploaded',
		 templateUrl: 'views/PharmacyInventory/StockAdjustment/rejected-uploaded.html',
		 controller: "MasterController",
		 title: 'Rejected Upload'
	})
	
	
	/**
	 * ***************** Stock Adjustment pages controller
	 * end*************************
	 */
	/**
	 * ***************** Narcotics store pages controller
	 * start*************************
	 */
	
	
	/*
	 * .state('packageMaster', { url: '/packageMaster', templateUrl:
	 * 'views/PharmacyInventory/package-master.html', controller:
	 * "MasterController", title: 'Package Master', })
	 */
	// .state('pharmacyunitMaster', {
	// url: '/pharmacyunitMaster',
	// templateUrl: 'views/PharmacyInventory/pharmacy-unit-master.html',
	// controller: "MasterController"
// })
	.state('dispensingMaster', {
		 url: '/dispensingMaster',
		 templateUrl: 'views/PharmacyInventory/dispensing-master.html',
		 controller: "MasterController",
		 title: 'Dispensing Master',
	 })
	// .state('storageunitMaster', {
	// url: '/storageunitMaster',
	// templateUrl: 'views/PharmacyInventory/storage-unit-master.html',
	// controller: "MasterController"
	// })
	.state('procurementunitMaster', {
		 url: '/procurementunitMaster',
		 templateUrl: 'views/PharmacyInventory/procurement-unit-master.html',
		 controller: "MasterController",
		 title: 'Procurement Unit Master',
	})
	.state('kitMaster', {
		 url: '/kitMaster',
		 templateUrl: 'views/PharmacyInventory/kit-master.html',
		 controller: "MasterController",
		 title: 'Kit Master',
	})
	.state('combinationMedicationMaster', {
		 url: '/combinationMedicationMaster',
		 templateUrl: 'views/PharmacyInventory/combination-medication-master.html',
		 controller: "MasterController",
		 title: 'Combination Medication Master',
	})
	/** ***************** Narcotics pages controller end************************* */
	  
	  /**
		 * ***********************************Global Master
		 * Start*******************************************
		 */
	/*
	 * .state('departmentMasterGlobal', { url: '/departmentMasterGlobal',
	 * templateUrl: 'views/master/GlobalMaster/department-master-global.html',
	 * controller: "MasterController" })
	 * 
	 * .state('subdepartmentMaster', { url: '/subdepartmentMaster', templateUrl:
	 * 'views/master/GlobalMaster/sub-department-Master.html', controller:
	 * "MasterController" })
	 */
	.state('patientCategory', {
		 url: '/patientCategory',
		 templateUrl: 'views/master/GlobalMaster/patient-category-master.html',
		 controller: "patientCategoryController",
		 title: 'Patient Category Master',
		 resolve : {

   			data: function($q, $state, $timeout) {
   		        var deferred = $q.defer();
   		
   		        $timeout(function() {
   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
   				 	.modules.filter(function(item){
   	            	   return (item.id == 446);
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
   		 }
	})
	.state('hospitalMaster', {
		 url: '/hospitalMaster',
		 templateUrl: 'views/master/GlobalMaster/hospital-master.html',
		 controller: "hospitalMasterController",
		 title: 'Hospital Master',
		 resolve : {

	   			data: function($q, $state, $timeout,$cookies) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 371);
	   				 	});
	   					if(arr.length <= 0 || $cookies.getObject('cookieObject').isSuperUser !='Y'){
	   					 	$state.go("accessDenied")
                             deferred.resolve();
	   					  } else {
	   					 	deferred.resolve();
	   					  }
	   		        });
	   		
	   		        return deferred.promise;
	   		      }
	   		 }
	})
	.state('unitMaster', {
		 url: '/unitMaster',
		 templateUrl: 'views/master/GlobalMaster/unit-master.html',
		 controller: "MasterController",
		 title: 'Clinical Unit Master',
	})
	/*
	 * .state('serviceMaster', { url: '/serviceMaster', templateUrl:
	 * 'views/master/GlobalMaster/service-master.html', controller:
	 * "MasterController" })
	 */
	.state('tariffMaster', {
		 url: '/tariffMaster',
		 templateUrl: 'views/master/GlobalMaster/tariff-master.html',
		 controller: "tariffMasterController",
		 title: 'Tariff Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 609);
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
	   		 }
	})
	
	.state('currencyMaster', {
		 url: '/currencyMaster',
		 templateUrl: 'views/master/GlobalMaster/currency-master.html',
		 controller: "currencyMasterController",
		 title: 'Currency Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 451);
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
	   		 }
	})
	
	.state('currencydenomination', {
		 url: '/currencydenomination',
		 templateUrl: 'views/master/GlobalMaster/currency-denomination.html',
		 controller: "currencyDenominationController",
		 title: 'Currency Denomination Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 452);
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
	   		 }
	})
	.state('corporateMasterGlobal', {
		 url: '/corporateMasterGlobal',
		 templateUrl: 'views/master/GlobalMaster/corporate-master-global.html',
		 controller: "corporateMasterController",
		 title: 'Corporate Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 574);
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
	   		 }
	})
	.state('gradeCreationMaster', {
		 url: '/gradeCreationMaster',
		 templateUrl: 'views/master/GlobalMaster/grade-creation-master.html',
		 controller: "gradeCreationMasterController",
		 title: 'Grade Creation Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 575);
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
	   		 }
	})

	.state('InsuranceTPAMapping', {
		 url: '/InsuranceTPAMapping',
		 templateUrl: 'views/master/GlobalMaster/insurance-TPA-mapping.html',
		 controller: "insuranceTPAMappingController",
		 title: 'Insurance & TPA Mapping',
	     resolve : {

		   			data: function($q, $state, $timeout) {
		   		        var deferred = $q.defer();
		   		
		   		        $timeout(function() {
		   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
		   				 	.modules.filter(function(item){
		   	            	   return (item.id == 573);
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
		   		 }
		})
	.state('doctorCategory', {
		 url: '/doctorCategory',
		 templateUrl: 'views/master/GlobalMaster/doctor-category.html',
		 controller: "doctorCategoryController",
		 title: 'Doctor Category',
	     resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 453);
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
	   		 }
	})
	.state('employeeCategory', {
		 url: '/employeeCategory',
		 templateUrl: 'views/master/GlobalMaster/emplyoee-category.html',
		 controller: "employeeCategoryController",
		 title: 'Employee Category',
	     resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 454);
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
	   		 }
	})
	.state('bankBranch', {
		 url: '/bankBranch',
		 templateUrl: 'views/master/GlobalMaster/bank-Branch.html',
		 controller: "bankBranchController",
		 title: 'Bank Branch Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 455);
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
	   		 }
	})
	.state('bankMaster', {
		 url: '/bankMaster',
		 templateUrl: 'views/master/GlobalMaster/bank-master.html',
		 controller: "bankMasterController",
		 title: 'Bank Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 456);
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
	   		 }
	})
	
	.state('clinicMaster', {
		 url: '/clinicMaster',
		 templateUrl: 'views/master/GlobalMaster/clinic-master.html',
		 controller: "clinicMasterController",
		 title: 'Clinic Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 504);
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
	   		 }
	})
	.state('serviceTariffMaster', {
		 url: '/serviceTariffMaster',
		 templateUrl: 'views/master/UnitMaster/billingMaster/service-tariff-master.html',
		 controller: "serviceTariffMasterController",
		 title : 'Service Tariff Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 610);
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
	   		 }
	})
	.state('addNewServiceTariffMaster', {
		 url: '/addNewServiceTariffMaster',
		 templateUrl : 'views/master/UnitMaster/billingMaster/add-new-service-tariff-master.html',
		 controller: "serviceTariffMasterController",
		 title: 'Service Tariff Master',
	})
	.state('OPConsulationConfigurationMaster', {
		 url: '/OPConsulationConfigurationMaster',
		 templateUrl: 'views/master/UnitMaster/billingMaster/op-consultation-configuration-master.html',
		 controller: "OPConsulationConfigurationMasterController",
		 title: 'OP Consultation Configuration Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 611);
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
	   		 }
	})
	.state('doctorConsulationMaster', {
		 url: '/doctorConsulationMaster',
		 templateUrl: 'views/master/UnitMaster/billingMaster/doctor-consultation-master.html',
		 controller: "doctorConsulationMasterController",
		 title: 'Doctor Consultation Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 612);
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
	   		 }
	})
	
	.state('organizationMaster', {
		 url: '/organizationMaster',
		 templateUrl: 'views/master/GlobalMaster/organizationMaster.html',
		 controller: "organizationController",
		 title: 'Organization Master',
		 resolve : {

	   			data: function($q, $state, $timeout,$cookies) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 377);
	   				 	});
	   					if(arr.length <= 0 || $cookies.getObject('cookieObject').isSuperUser!='Y'){
	   						$state.go("accessDenied")
	   						deferred.reject();
	   					 } else {
	   						deferred.resolve();
	   					 }
	   		        });
	   		
	   		        return deferred.promise;
	   		      }
	   		 }
	})
	
	/** ************Asset Management Start***************** */
	
	.state('productCategoryMaster', {
		 url: '/productCategoryMaster',
		 templateUrl: 'views/master/GlobalMaster/AssetManagenent/Master/product-category-master.html',
		 controller: "MasterController",
		 title: 'Product Category Master',
		})
	.state('productstrengthMaster', {
		 url: '/productstrengthMaster',
		 templateUrl: 'views/master/GlobalMaster/product-strength-master.html',
		 controller: "MasterController",
		 title :'Product Category Master',
	})
	.state('nursingOrderMaster', {
		 url: '/nursingOrderMaster',
		 templateUrl: 'views/master/GlobalMaster/nursing-order-master.html',
		 controller: "nursingOrderMasterController",
		 title: 'Nursing Order Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 480);
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
	   		 }
	})
	/**
	 * ******************* Coversheet page and MRD page 2-Dec-2020
	 * ************************
	 */
	

	
	.state('bloodComponent', {
		 url: '/bloodComponent',
		 templateUrl: 'views/master/GlobalMaster/blood-component.html',
		 controller: "MasterController",
		 title: 'Blood Component Master',
	})
	.state('vaccinationMaster', {
		 url: '/vaccinationMaster',
		 templateUrl: 'views/master/GlobalMaster/vaccination-master.html',
		 controller: "MasterController",
		 title: 'Vaccination Master',
	})
	.state('immunizationSchedule', {
		 url: '/immunizationSchedule',
		 templateUrl: 'views/master/GlobalMaster/immunization-schedule.html',
		 controller: "MasterController",
		 title: 'Immunization Schedule',
	})
	.state('immuniztionHistory', {
		 url: '/immuniztionHistory',
		 templateUrl: 'views/master/GlobalMaster/immunization-history.html',
		 controller: "MasterController",
		 title: 'Immunization History',
	})
	.state('rejectionMaster', {
		 url: '/rejectionMaster',
		 templateUrl: 'views/master/GlobalMaster/rejection-master.html',
		 controller: "rejectionMasterController",
		 title: 'Rejection Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 493);
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
	   		 }
	})
	.state('reasonTypeMaster', {
		 url: '/reasonTypeMaster',
		 templateUrl: 'views/master/GlobalMaster/reason-type-master.html',
		 controller: "reasonTypeMasterController",
		 title: 'Reason Type Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 494);
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
	   		 }
	})
	
/*
 * .state('generalInsuranceMaster', { url: '/generalInsuranceMaster',
 * templateUrl:
 * 'views/master/GlobalMaster/AssetManagenent/Master/general-insurance-master.html',
 * controller: "MasterController" })
 */
	/** ************Asset Management End***************** */
	
	/** *****************Organization Master start************************* */
	.state('departmentMasterUnit', {
		 url: '/departmentMasterUnit',
		 templateUrl: 'views/master/UnitMaster/organizationMaster/department-master-unit.html',
		 controller: "departmentMasterUnitController",
		 title: 'Department Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 533);
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
	   		 }
	})
	.state('subDepartmentMasterUnit', {
		 url: '/subDepartmentMasterUnit',
		 templateUrl: 'views/master/UnitMaster/organizationMaster/sub-department-master-unit.html',
		 controller: "subDepartmentMasterUnitController",
		 title: 'Sub Department Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 534);
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
	   		 }
	})
	.state('holidayCalendarMasterUnit', {
		 url: '/holidayCalendarMasterUnit',
		 templateUrl: 'views/master/UnitMaster/organizationMaster/holiday-calendar-master-unit.html',
		 controller: "holidayCalendarMasterUnitController",
		 title: 'Holiday Calendar Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 536);
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
	   		 }
	})
	.state('ICUtypeMasterUnit', {
		 url: '/ICUtypeMasterUnit',
		 templateUrl: 'views/master/UnitMaster/organizationMaster/ICU-type-master-unit.html',
		 controller: "MasterController",
		 title: 'ICU Type Master'
	})
	.state('billingBedCategoryMapper', {
		 url: '/billingBedCategoryMapper',
		 templateUrl: 'views/master/UnitMaster/organizationMaster/bed-category-master-unit.html',
		 controller: "billingBedCategoryMapperController",
		 title: 'Billing Bed Category Mapper',
	})
	.state('billingBedCategoryMaster', {
		 url: '/billingBedCategoryMaster',
		 templateUrl: 'views/master/UnitMaster/organizationMaster/billing-bed-category-master-unit.html',
		 controller: "billingBedCategoryMasterUnitController",
		 title: 'Billing Ward Bed Category Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 544);
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
	   		 }
	})
	.state('bedClassMasterUnit', {
		 url: '/bedClassMasterUnit',
		 templateUrl: 'views/master/UnitMaster/organizationMaster/bed-class-master-unit.html',
		 controller: "MasterController",
		 title: 'Bed Class Master',
	})
	.state('bedClassMappingMasterUnit', {
		 url: '/bedClassMappingMasterUnit',
		 templateUrl: 'views/master/UnitMaster/organizationMaster/bed-class-mapping-master-unit.html',
		 controller: "MasterController",
		 title: 'Bed Class Mapping Master',
	})
	.state('nursingStationMaster', {
		 url: '/nursingStationMaster',
		 templateUrl: 'views/master/UnitMaster/organizationMaster/nursing-station-master.html',
		 controller: "nursingStationMasterController",
		 title: 'Nursing Station Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 547);
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
	   		 }
	})
	.state('nursingStationWardMapping', {
		 url: '/nursingStationWardMapping',
		 templateUrl: 'views/master/UnitMaster/organizationMaster/nursing-station-ward-mapping.html',
		 controller: "nursingStationWardMappingController",
		 title: 'Nursing Station Ward Mapping',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 548);
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
	   		 }
	})
	.state('mealPreferenceMaster', {
		 url: '/mealPreferenceMaster',
		 templateUrl: 'views/master/UnitMaster/organizationMaster/meal-preference-master.html',
		 controller: "mealPreferenceMasterController",
		 title: 'Meal Preference Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 549);
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
	   		 }
	})
	.state('roomMaster', {
		 url: '/roomMaster',
		 templateUrl: 'views/master/UnitMaster/organizationMaster/room-master.html',
		 controller: "roomMasterController",
		 title: 'Room Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 550);
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
	   		 }
	})
	.state('wardMaster', {
		 url: '/wardMaster',
		 templateUrl: 'views/master/UnitMaster/organizationMaster/ward-master.html',
		 controller: "wardMasterController",
		 title: 'Ward Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 545);
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
	   		 }
	})
	.state('bedMaster', {
		 url: '/bedMaster',
		 templateUrl: 'views/master/UnitMaster/organizationMaster/bed-master.html',
		 controller: "bedMasterController",
		 title: 'Ward Bed Master',
		 resolve : {

		   			data: function($q, $state, $timeout) {
		   		        var deferred = $q.defer();
		   		
		   		        $timeout(function() {
		   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
		   				 	.modules.filter(function(item){
		   	            	   return (item.id == 546);
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
		   		 }
		})
	.state('floorMaster', {
		 url: '/floorMaster',
		 templateUrl: 'views/master/UnitMaster/organizationMaster/floor-master.html',
		 controller: "floorMasterController",
		 title: 'Floor Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 537);
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
	   		 }
	})
	.state('floorMapMaster', {
		 url: '/floorMapMaster',
		 templateUrl: 'views/master/UnitMaster/organizationMaster/floor-map-master.html',
		 controller: "MasterController",
		 title: 'Floor Map Master',
	})
	.state('amenityMaster', {
		 url: '/amenityMaster',
		 templateUrl: 'views/master/UnitMaster/organizationMaster/amenity-master.html',
		 controller: "amenityMasterController",
		 title: 'Amenity Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 539);
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
	   		 }
	})
	.state('wingMaster', {
		 url: '/wingMaster',
		 templateUrl: 'views/master/UnitMaster/organizationMaster/wing-master.html',
		 controller: "wingMasterController",
		 title: 'Wing Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 540);
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
	   		 }
	})
	
	/**
	 * ***************added on 19/march/2019 By Pranita start
	 * ***********************
	 */
	.state('smsConfiguration', {
		 url: '/smsConfiguration',
		 templateUrl: 'views/master/GlobalMaster/sms-utility-configuration.html',
		 controller: "smsUtilityConfigurationController",
		 title: 'SMS Configuration',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 774);
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
	   		 }
	})
	
	.state('mailConfiguration', {
		 url: '/mailConfiguration',
		 templateUrl: 'views/master/GlobalMaster/mail-utility-configuration.html',
		 controller: "mailUtilityConfigurationController",
		 title: 'Mail Configuration',
	})
	
	/**
	 * ***************added on 19/march/2019 By Pranita end
	 * ***********************
	 */
	.state('counterMaster', {
		 url: '/counterMaster',
		 templateUrl: 'views/master/UnitMaster/organizationMaster/counter-master.html',
		 controller: "counterMasterController",
		 title: 'Counter Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 704);
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
	   		 }
	})
	.state('cabinMaster', {
		 url: '/cabinMaster',
		 templateUrl: 'views/master/UnitMaster/organizationMaster/cabin-master.html',
		 controller: "cabinMasterController",
		 title: 'Cabin Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 705);
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
	   		 }
	})
	/**
	 * ***************added on 18/sep/2018 By Pranita end
	 * ***********************
	 */
	
	/*
	 * .state('referralMaster', { url: '/referralMaster', templateUrl:
	 * 'views/master/UnitMaster/organizationMaster/referral-master.html',
	 * controller: "MasterController" })
	 */
	/*
	 * .state('deliveryTypeMaster', { url: '/deliveryTypeMaster', templateUrl:
	 * 'views/master/UnitMaster/organizationMaster/delivery-type-master.html',
	 * controller: "MasterController" })
	 */
	/*
	 * .state('icutypeMaster', { url: '/icutypeMaster', templateUrl:
	 * 'views/master/UnitMaster/organizationMaster/icu-type-master.html',
	 * controller: "MasterController" })
	 */
	/*
	 * .state('
	 * 
	 * TypeMaster', { url: '/encounterTypeMaster', templateUrl:
	 * 'views/master/UnitMaster/organizationMaster/encounter-type-master.html',
	 * controller: "MasterController" })
	 */
	
	/** *****************Organization Master End************************* */
	
	
	
	/** *****************Billing Master start************************* */
	
	.state('admissionRejectionList', {
		 url: '/admissionRejectionList',
		 templateUrl: 'views/master/UnitMaster/billingMaster/admission-rejection-list.html',
		 controller: "MasterController",
		 title: 'Admission Rejection Bill',
	})
	
	.state('refund', {
		 url: '/refund',
		 templateUrl: 'views/master/UnitMaster/billingMaster/refund.html',
		 controller: "MasterController",
		 title: 'Refund',
	})
	
	
		.state(
				'refundCRList',
				{
					url : '/refundCRList',
					title: 'Refund CR List',
					views : {
						'@' : {
							templateUrl : 'views/master/UnitMaster/billingMaster/refund-CR-list.html',
							controller : 'MasterController',
						}
					}
				})

		.state(
				'refundCRList.cashForRefund',
				{
					url : '/cashForRefund',
					views : {
						'@refundCRList' : {
							templateUrl : 'views/master/UnitMaster/billingMaster/cash-for-refund-list.html',
							controller : 'MasterController',
						}
					}
				})
				
				.state(
				'refundCRList.chequeForRefund',
				{
					url : '/chequeForRefund',
					views : {
						'@refundCRList' : {
							templateUrl : 'views/master/UnitMaster/billingMaster/cheque-refund-list.html',
							controller : 'MasterController',
						}
					}
				})
	
	
				
				
	.state('deposit', {
		 url: '/deposit',
		 templateUrl: 'views/master/UnitMaster/billingMaster/deposit.html',
		 controller: "depositController",
		 title: 'Deposit',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 12);
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
	   		 }
	})
	.state('franchiseDeposit', {
    		 url: '/franchiseDeposit',
    		 templateUrl: 'views/master/UnitMaster/billingMaster/franchise-deposit.html',
    		 controller: "franchiseDepositController",
    		 title: 'Franchise Deposit',
    		 resolve : {

    	   			data: function($q, $state, $timeout) {
    	   		        var deferred = $q.defer();

    	   		        $timeout(function() {
    	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
    	   				 	.modules.filter(function(item){
    	   	            	   return (item.id == 12);
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
    	   		 }
    	})
	.state('findBillList', {
		 url: '/findBillList',
		 templateUrl: 'views/master/UnitMaster/billingMaster/find-bill-list.html',
		 controller: "MasterController",
		 title: 'Find Bill List',
	})
	.state('updatePayeeInformation', {
		 url: '/updatePayeeInformation',
		 templateUrl: 'views/master/UnitMaster/billingMaster/update-payee-information.html',
		 controller: "updatePayeeInformationController",
		 title: 'Update Payee Information',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 42);
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
	   		 }
	})
	
	.state('chargeConversion', {
		 url: '/chargeConversion',
		 templateUrl: 'views/master/UnitMaster/billingMaster/charge-conversion.html',
		 controller: "chargeConversionController",
		 title: 'Charge Conversion',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 592);
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
	   		 }
	})
	
	.state('entitlementCoversion', {
		 url: '/entitlementCoversion',
		 templateUrl: 'views/master/UnitMaster/billingMaster/entitlement-conversion.html',
		 controller: "MasterController",
		 title: 'Entitlement Conversion',
	})
	
	.state('autoChargeReview', {
		 url: '/autoChargeReview',
		 templateUrl: 'views/master/UnitMaster/billingMaster/auto-charge-review.html',
		 controller: "MasterController",
		 title: 'Auto Charge Review',
	})
	
	.state('writeOff', {
		 url: '/writeOff',
		 templateUrl: 'views/master/UnitMaster/billingMaster/write-off.html',
		 controller: "MasterController",
		 title: 'Write Off',
	})
	
	
	.state('writeOffDetails', {
		 url: '/writeOffDetails',
		 templateUrl: 'views/master/UnitMaster/billingMaster/write-off-details.html',
		 controller: "MasterController",
		 title: 'Write Off Details',
	})
	
	.state('cashOpeningBalance', {
		 url: '/cashOpeningBalance',
		 templateUrl: 'views/master/UnitMaster/billingMaster/cash-opening-balance.html',
		 controller: "cashOpeningBalanceController",
		 title: 'Cash Opening Balance',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 597);
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
	   		 }
	})
	
	.state('interimHandover', {
		 url: '/interimHandover',
		 templateUrl: 'views/master/UnitMaster/billingMaster/interim-handover.html',
		 controller: "interimHandoverController",
		 title: 'Interim Handover',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 598);
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
	   		 }
	})
	
	.state('roundOffConfiguration', {
		 url: '/roundOffConfiguration',
		 templateUrl: 'views/master/UnitMaster/billingMaster/round-off-configuration.html',
		 controller: "MasterController",
		 title: 'Round Off Configuration'
	})
	
	.state('shiftClosing', {
		 url: '/shiftClosing',
		 templateUrl: 'views/master/UnitMaster/billingMaster/shift-closing.html',
		 controller: "shiftClosingController",
		 title: 'Shift Closing',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 600);
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
	   		 }
	})
	
	.state('reminderForNOK', {
		 url: '/reminderForNOK',
		 templateUrl: 'views/master/UnitMaster/billingMaster/reminder-for-nok.html',
		 controller: "MasterController",
		 title: 'Reminder for NOK'
	})
	
	.state('companyInvoiceCreation', {
		 url: '/companyInvoiceCreation',
		 templateUrl: 'views/master/UnitMaster/billingMaster/company-invoice-creation.html',
		 controller: "companyInvoiceCreationController",
		 title: 'Company Invoice Creation',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 602);
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
	   		 }
	})
	.state('detailsCompanyInvoiceCreation', {
		 url: '/detailsCompanyInvoiceCreation',
		 templateUrl: 'views/master/UnitMaster/billingMaster/details-company-invoice-creation.html',
		 controller: "detailsCompanyInvoiceCreationController",
		 title: 'Company Invoice Creation',
			 params: 
		        {
				 detailsCompanyInvoiceList: [] // default value
	            }	
	})
	
	
	.state('companyReminder', {
		 url: '/companyReminder',
		 templateUrl: 'views/master/UnitMaster/billingMaster/company-remainder.html',
		 controller: "companyRemainderController",
		 title: 'Company Reminder',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 603);
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
	   		 }
	})
	
	
	/*
	 * .state('settledInvoices', { url: '/settledInvoices', templateUrl:
	 * 'views/master/UnitMaster/billingMaster/settled-invoices.html',
	 * controller: "settledInvoicesMasterController" })
	 */
	
	
	.state('paymentCollection', {
		 url: '/paymentCollection',
		 templateUrl: 'views/master/UnitMaster/billingMaster/payment-collection.html',
		 controller: "MasterController",
		 title: 'Payment Collection',
	})
	
  
		   .state('paymentCollection.cash',
		  	       {
		   	        url : '/cash',
		   	        views :
		   	         {
		    	          '@paymentCollection' :
		   	           {
		    	            templateUrl : 'views/common/cash.html',
		    	            controller : 'MasterController',
		   	           }
		    	         }
    	       })
    	       
    	        .state('paymentCollection.cheque',
		  	       {
		   	        url : '/cheque',
		   	        views :
		   	         {
		    	          '@paymentCollection' :
		   	           {
		    	            templateUrl : 'views/common/cheque.html',
		    	            controller : 'MasterController',
		   	           }
		    	         }
    	       })
    	       
    	       
    	        .state('paymentCollection.creditDebitCard',
		  	       {
		   	        url : '/creditDebitCard',
		   	        views :
		   	         {
		    	          '@paymentCollection' :
		   	           {
		    	            templateUrl : 'views/common/credit-debit-card.html',
		    	            controller : 'MasterController',
		   	           }
		    	         }
    	       })
				
	
    	       
    	       
    .state('pharmacyPaymentCollection', {
		 url: '/pharmacyPaymentCollection',
		 templateUrl: 'views/PharmacyInventory/pharmacy-payment-collection.html',
		 controller: "MasterController",
		 title: 'Payment Collection',
	})
	
  
		   .state('pharmacyPaymentCollection.cash',
		  	       {
		   	        url : '/cash',
		   	        views :
		   	         {
		    	          '@pharmacyPaymentCollection' :
		   	           {
		    	            templateUrl : 'views/common/cash.html',
		    	            controller : 'MasterController',
		   	           }
		    	         }
    	       })
    	       
    	        .state('pharmacyPaymentCollection.cheque',
		  	       {
		   	        url : '/cheque',
		   	        views :
		   	         {
		    	          '@pharmacyPaymentCollection' :
		   	           {
		    	            templateUrl : 'views/common/cheque.html',
		    	            controller : 'MasterController',
		   	           }
		    	         }
    	       })
    	       
    	       
    	        .state('pharmacyPaymentCollection.creditDebitCard',
		  	       {
		   	        url : '/creditDebitCard',
		   	        views :
		   	         {
		    	          '@pharmacyPaymentCollection' :
		   	           {
		    	            templateUrl : 'views/common/credit-debit-card.html',
		    	            controller : 'MasterController',
		   	           }
		    	         }
    	       })
    	       

	/** ******************** START UNIT SERVICE MASTER ******************* */
	.state('unitServiceMaster', {
		 url: '/unitServiceMaster',
		 templateUrl: 'views/master/UnitMaster/billingMaster/unit-service-master.html',
		 controller: "UnitServiceMasterController",
		 title: 'Clinical Unit Service Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 583);
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
	   		 }
	})
	
	.state('unitServiceMapping', {
		 url: '/unitServiceMapping',
		 templateUrl: 'views/master/UnitMaster/billingMaster/unit-service-mapping.html',
		 controller: "UnitServiceMappingController",
		 title: 'Clinical Unit Service Mapping',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 584);
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
	   		 }
	})
	
	.state('unitServiceBasePrice', {
		 url: '/unitServiceBasePrice',
		 templateUrl: 'views/master/UnitMaster/billingMaster/unit-service-base-price.html',
		 controller: "unitServiceBasePriceController",
		 title: 'Unit Service Base Price',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 585);
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
	   		 }
	})
	
	.state('schedulePriceUnitService', {
		 url: '/schedulePriceUnitService',
		 templateUrl: 'views/master/UnitMaster/billingMaster/schedule-price-unit-service.html',
		 controller: "MasterController",
		 title: 'Schedule Price Unit Service',
	})
	
	
	.state('orderSet', {
		 url: '/orderSet',
		 templateUrl: 'views/master/UnitMaster/billingMaster/order-set.html',
		 controller: "MasterController",
		 title: 'Order Set',
	})
	
	.state('orderSetList', {
		 url: '/orderSetList',
		 templateUrl: 'views/master/UnitMaster/billingMaster/order-set-list.html',
		 controller: "MasterController",
		 title: 'Order Set List',
	})
	
	
	/** ******************** END UNIT SERVICE MASTER ******************* */
	/** ******************** START IPD CHARGES ******************* */
										

		/*
		 * .state( 'IPDCharges', { url : '/IPDCharges', views : { '@' : {
		 * templateUrl :
		 * 'views/master/UnitMaster/billingMaster/IPDCharge/IPD-charge.html',
		 * controller : 'MasterController', } } })
		 */

		.state(
				'IPDCharges.serviceDetailsForIPDCharge',
				{
					url : '/serviceDetailsForIPDCharge',
					title: 'IPD Charge',
					views : {
						'@IPDCharges' : {
							templateUrl : 'views/master/UnitMaster/billingMaster/IPDCharge/service-details-IPD-charge.html',
							controller : 'MasterController',
						}
					}
				})

		.state(
				'IPDCharges.groupWiseForIPDCharge',
				{
					url : '/groupWiseForIPDCharge',
					title: 'IPD Charge',
					views : {
						'@IPDCharges' : {
							templateUrl : 'views/master/UnitMaster/billingMaster/IPDCharge/group-wise-IPD.html',
							controller : 'MasterController',
						}
					}
				})

		.state(
				'IPDCharges.packageForIPDCharge',
				{
					url : '/packageForIPDCharge',
					title: 'IPD Charge',
					views : {
						'@IPDCharges' : {
							templateUrl : 'views/master/UnitMaster/billingMaster/IPDCharge/package-details-IPD.html',
							controller : 'MasterController',
						}
					}
				})

		.state(
				'IPDCharges.transferHistoryForIPDCharge',
				{
					url : '/transferHistoryForIPDCharge',
					title: 'IPD Charge',
					views : {
						'@IPDCharges' : {
							templateUrl : 'views/master/UnitMaster/billingMaster/IPDCharge/transfer-history-IPD.html',
							controller : 'MasterController',
						}
					}
				})

		.state(
				'IPDCharges.cancelServiceForIPDCharge',
				{
					url : '/cancelServiceForIPDCharge',
					title: 'IPD Charge',
					views : {
						'@IPDCharges' : {
							templateUrl : 'views/master/UnitMaster/billingMaster/IPDCharge/cancel-service-IPD.html',
							controller : 'MasterController',
						}
					}
				})

		.state(
				'IPDCharges.procedureDetailsForIPDCharge',
				{
					url : '/procedureDetailsForIPDCharge',
					title: 'IPD Charge',
					views : {
						'@IPDCharges' : {
							templateUrl : 'views/master/UnitMaster/billingMaster/IPDCharge/procedure-details-IPD.html',
							controller : 'MasterController',
						}
					}
				})

		.state(
				'IPDCharges.periodicityDetailsForIPDCharge',
				{
					url : '/periodicityDetailsForIPDCharge',
					title: 'IPD Charge',
					views : {
						'@IPDCharges' : {
							templateUrl : 'views/master/UnitMaster/billingMaster/IPDCharge/periodicity-details-IPD.html',
							controller : 'MasterController',
						}
					}
				})

		.state(
				'IPDCharges.intrimBillForIPDCharge',
				{
					url : '/intrimBillForIPDCharge',
					title: 'IPD Charge',
					views : {
						'@IPDCharges' : {
							templateUrl : 'views/master/UnitMaster/billingMaster/IPDCharge/intrim-bill-IPD.html',
							controller : 'MasterController',
						}
					}
				})

		.state(
				'IPDCharges.drugDetailsForIPDCharge',
				{
					url : '/drugDetailsForIPDCharge',
					title: 'IPD Charge',
					views : {
						'@IPDCharges' : {
							templateUrl : 'views/master/UnitMaster/billingMaster/IPDCharge/drug-details-IPD.html',
							controller : 'MasterController',
						}
					}
				})

		.state(
				'IPDCharges.NOKForIPDCharge',
				{
					url : '/NOKForIPDCharge',
					title: 'IPD Charge',
					views : {
						'@IPDCharges' : {
							templateUrl : 'views/AdmissionDischarge/nok-details-only.html',
							controller : 'MasterController',
						}
					}
				})
				
				.state(
				'IPDCharges.payerDetailsForIPDCharge',
				{
					url : '/payerDetailsForIPDCharge',
					title: 'IPD Charge',
					views : {
						'@IPDCharges' : {
							templateUrl : 'views/master/UnitMaster/billingMaster/IPDCharge/payer-details-IPD.html',
							controller : 'MasterController',
						}
					}
				})
				
				.state(
				'IPDCharges.depositDetailsForIPDCharge',
				{
					url : '/depositDetailsForIPDCharge',
					title: 'IPD Charge',
					views : {
						'@IPDCharges' : {
							templateUrl : 'views/master/UnitMaster/billingMaster/IPDCharge/deposit-details-IPD.html',
							controller : 'MasterController',
						}
					}
				})
				
				

		/**
		 * ***********************END IPD CHARGES********************
		 */

	/** ******************** START ER CHARGES ******************* */
										

		.state(
				'ERCharges',
				{
					url : '/ERCharges',
					title: 'ER Charge',
					views : {
						'@' : {
							templateUrl : 'views/master/UnitMaster/billingMaster/IPDCharge/ER-charge.html',
							controller : 'MasterController',
						}
					}
				})

		.state(
				'ERCharges.serviceDetailsForIPDCharge',
				{
					url : '/serviceDetailsForIPDCharge',
					views : {
						'@IPDCharges' : {
							templateUrl : 'views/master/UnitMaster/billingMaster/IPDCharge/service-details-IPD-charge.html',
							controller : 'MasterController',
						}
					}
				})

		.state(
				'ERCharges.groupWiseForIPDCharge',
				{
					url : '/groupWiseForIPDCharge',
					views : {
						'@IPDCharges' : {
							templateUrl : 'views/master/UnitMaster/billingMaster/IPDCharge/group-wise-IPD.html',
							controller : 'MasterController',
						}
					}
				})

		.state(
				'ERCharges.packageForIPDCharge',
				{
					url : '/packageForIPDCharge',
					views : {
						'@IPDCharges' : {
							templateUrl : 'views/master/UnitMaster/billingMaster/IPDCharge/package-details-IPD.html',
							controller : 'MasterController',
						}
					}
				})

		.state(
				'ERCharges.transferHistoryForIPDCharge',
				{
					url : '/transferHistoryForIPDCharge',
					views : {
						'@IPDCharges' : {
							templateUrl : 'views/master/UnitMaster/billingMaster/IPDCharge/transfer-history-IPD.html',
							controller : 'MasterController',
						}
					}
				})

		.state(
				'ERCharges.cancelServiceForIPDCharge',
				{
					url : '/cancelServiceForIPDCharge',
					views : {
						'@IPDCharges' : {
							templateUrl : 'views/master/UnitMaster/billingMaster/IPDCharge/cancel-service-IPD.html',
							controller : 'MasterController',
						}
					}
				})

		.state(
				'ERCharges.procedureDetailsForIPDCharge',
				{
					url : '/procedureDetailsForIPDCharge',
					views : {
						'@IPDCharges' : {
							templateUrl : 'views/master/UnitMaster/billingMaster/IPDCharge/procedure-details-IPD.html',
							controller : 'MasterController',
						}
					}
				})

		.state(
				'ERCharges.periodicityDetailsForIPDCharge',
				{
					url : '/periodicityDetailsForIPDCharge',
					views : {
						'@IPDCharges' : {
							templateUrl : 'views/master/UnitMaster/billingMaster/IPDCharge/periodicity-details-IPD.html',
							controller : 'MasterController',
						}
					}
				})

		.state(
				'ERCharges.intrimBillForIPDCharge',
				{
					url : '/intrimBillForIPDCharge',
					views : {
						'@IPDCharges' : {
							templateUrl : 'views/master/UnitMaster/billingMaster/IPDCharge/intrim-bill-IPD.html',
							controller : 'MasterController',
						}
					}
				})

		.state(
				'ERCharges.drugDetailsForIPDCharge',
				{
					url : '/drugDetailsForIPDCharge',
					views : {
						'@IPDCharges' : {
							templateUrl : 'views/master/UnitMaster/billingMaster/IPDCharge/drug-details-IPD.html',
							controller : 'MasterController',
						}
					}
				})

		.state(
				'ERCharges.NOKForIPDCharge',
				{
					url : '/NOKForIPDCharge',
					views : {
						'@IPDCharges' : {
							templateUrl : 'views/AdmissionDischarge/nok-details-only.html',
							controller : 'MasterController',
						}
					}
				})
				
				.state(
				'ERCharges.payerDetailsForIPDCharge',
				{
					url : '/payerDetailsForIPDCharge',
					views : {
						'@IPDCharges' : {
							templateUrl : 'views/master/UnitMaster/billingMaster/IPDCharge/payer-details-IPD.html',
							controller : 'MasterController',
						}
					}
				})
				
				.state(
				'ERCharges.depositDetailsForIPDCharge',
				{
					url : '/depositDetailsForIPDCharge',
					views : {
						'@IPDCharges' : {
							templateUrl : 'views/master/UnitMaster/billingMaster/IPDCharge/deposit-details-IPD.html',
							controller : 'MasterController',
						}
					}
				})
				
				

		/**
		 * ***********************END ER CHARGES********************
		 */
	
				
				
	/*
	 * .state('IPDBilling', { url: '/IPDBilling', templateUrl:
	 * 'views/master/UnitMaster/billingMaster/IPD-billing.html', controller:
	 * "MasterController" })
	 */
	
	.state('intermediateBill', {
		 url: '/intermediateBill',
		 title: 'Intermediate Bill',
		 templateUrl: 'views/master/UnitMaster/billingMaster/intermediate-bill.html',
		 controller: "MasterController"
	})
	
	
	/*
	 * .state('billEstimation', { url: '/billEstimation', templateUrl:
	 * 'views/master/UnitMaster/billingMaster/bill-estimation.html', controller:
	 * "MasterController" })
	 */
	
	.state('packageDefinition', {
		 url: '/packageDefinition',
		 templateUrl: 'views/master/UnitMaster/billingMaster/package-definition.html',
		 controller: "MasterController",
		 title: 'Package Definition',
	})
	
	/*
	 * .state('insuranceMaster', { url: '/insuranceMaster', templateUrl:
	 * 'views/master/UnitMaster/billingMaster/insurance-master.html',
	 * controller: "MasterController" })
	 */
	.state('TPAmaster', {
		 url: '/TPAmaster',
		 templateUrl: 'views/master/UnitMaster/billingMaster/TPA-master.html',
		 controller: "TPAMasterController",
		 title: 'TPA Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 572);
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
	   		 }
	})
	.state('billingGroupMaster', {
		 url: '/billingGroupMaster',
		 templateUrl: 'views/master/UnitMaster/billingMaster/billing-group-master.html',
		 controller: "MasterController",
		 title: 'Billing Group Master',
	})
	.state('corporateMasterUnit', {
		 url: '/corporateMasterUnit',
		 templateUrl: 'views/master/UnitMaster/billingMaster/corporate-master-unit.html',
		 controller: "MasterController",
		 title: 'Corporate Master',
	})
	
	.state('addNewPackageWithCAP', {
		 url: '/addNewPackageWithCAP',
		 templateUrl: 'views/master/UnitMaster/billingMaster/package-with-CAP.html',
		 controller: "addNewPackageWithCAPController",
		 title: 'Package With CAP',
	})
	.state('addNewPackageWithCAPDetails', {
		 url: '/addNewPackageWithCAPDetails',
		 templateUrl: 'views/master/UnitMaster/billingMaster/new-package-with-CAP.html',
		 controller: "addNewPackageWithCAPDetailsController",
	     title: 'Package With CAP',
	     params:{
                 newPackageWithCAPObj :{}
              }
	})
	/**
	 * ****** Package With CAP main screen , Added on -28/May/2018
	 * start**********************
	 */
	.state('packageWithCAP', {
		 url: '/packageWithCAP',
		 templateUrl: 'views/master/UnitMaster/billingMaster/package-with-CAP-main.html',
		 controller: "packageWithCAPController",
		 title: 'Package With CAP',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 624);
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
	   		 }
	})
	
	.state('EHCpackage', {
		 url: '/EHCpackage',
		 templateUrl: 'views/master/UnitMaster/billingMaster/EHC-package-main.html',
		 controller: "ehcPackageController",
		 title: 'EHC Package',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 623);
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
	   		 }
	})
	.state('addNewEHCpackage', {
		 url: '/addNewEHCpackage',
		 templateUrl: 'views/master/UnitMaster/billingMaster/add-newEHC-package.html',
		 controller: "addNewEHCpackageController",
		 title: 'EHC Package',
		 params:{
			 newEHCpackageObj :{}
		 }
	})
	
	
	.state('allExclusivePackage', {
		 url: '/allExclusivePackage',
		 templateUrl: 'views/master/UnitMaster/billingMaster/all-exclusive-package-main.html',
		 controller: "allExclusivePackageController",
		 title: 'All Inclusive Package',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 625);
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
	   		 }
	})
	.state('addNewAllExclusivePackage', {
		 url: '/addNewAllExclusivePackage',
		 templateUrl: 'views/master/UnitMaster/billingMaster/add-new-all-exclusive-package-with-CAP.html',
		 controller: "AddNewAllInclusivePackageController",
		 title: 'All Inclusive Package'
	})
	
	.state('eitherOrPackage', {
		 url: '/eitherOrPackage',
		 templateUrl: 'views/master/UnitMaster/billingMaster/either-or-package-main.html',
		 controller: "eitherOrPackageController",
		 title: 'Either Or Package',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 626);
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
	   		 }
	})
	.state('addNewEitherOrPackage', {
		 url: '/addNewEitherOrPackage',
		 templateUrl: 'views/master/UnitMaster/billingMaster/add-new-either-or-package.html',
		 controller: "addEitherOrPackageController",
		 title: 'Either Or Package',
         params:{
         			 newEitherOrPackageObj :{}
         		}
	})
	
	.state('multicounterPackage', {
		 url: '/multicounterPackage',
		 templateUrl: 'views/master/UnitMaster/billingMaster/multicounter-package-main.html',
		 controller: "multicounterPackageController",
		 title: 'Multi Visit Package',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 627);
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
	   		 }
	})
	.state('addNewMulticounterPackage', {
		 url: '/addNewMulticounterPackage',
		 templateUrl: 'views/master/UnitMaster/billingMaster/add-new-multicounter-package.html',
		 controller: "addMultiencounterPackage",
		 title: 'Multi Visit Package',
		 params:{
                  newMulticounterPackageObj :{}
                }
	})
	/**
	 * ****** Package With CAP main screen , Added on -28/May/2018
	 * end**********************
	 */
	.state('packageDefEitherOr', {
		 url: '/packageDefEitherOr',
		 templateUrl: 'views/master/UnitMaster/billingMaster/package-definition-eitheror.html',
		 controller: "MasterController",
		 title: 'Either Or Package',
	})
/*
 * .state('allInclusivePackage', { url: '/allInclusivePackage', templateUrl:
 * 'views/master/UnitMaster/billingMaster/all-inclusive-package.html',
 * controller: "MasterController" })
 */
	.state('packageList', {
		 url: '/packageList',
		 templateUrl: 'views/master/UnitMaster/billingMaster/package-list.html',
		 controller: "MasterController",
		 title: 'Package List',
	})
	
	  /** *********************** Start Variable Pricing ******************** */
    	       .state('variableSequence', {
					 url: '/variableSequence',
					 templateUrl: 'views/master/UnitMaster/billingMaster/variable-sequence.html',
					 controller: "MasterController",
					 title: 'Variable Sequence',
				})
    	        .state('variableSquencePricing',
    	       {
    	        url : '/variableSquencePricing',
    	        views :
    	         {
    	          '@' :
    	           {
    	            templateUrl : 'views/master/UnitMaster/billingMaster/variable-squence-pricing.html',
    	            controller : 'MasterController',
    	           }
    	         },
    	         title: 'Variable Pricing'
    	       })
    	       
    	      .state('variableSquencePricing.sequencePricing',
  	       {
   	        url : '/sequencePricing',
   	        views :
   	         {
    	          '@variableSquencePricing' :
   	           {
    	            templateUrl : 'views/master/UnitMaster/billingMaster/sequence-of-variable.html',
    	            controller : 'MasterController',
   	           }
    	         },
    	         title: 'Variable Pricing'
    	       })
    	       
    	       
    	           .state('variableSquencePricing.variablePricing',
		  	       {
		   	        url : '/variablePricing',
		   	        views :
		   	         {
		    	          '@variableSquencePricing' :
		   	           {
		    	            templateUrl : 'views/master/UnitMaster/billingMaster/variable-pricing.html',
		    	            controller : 'MasterController',
		   	           }
		    	         },
		    	         title: 'Variable Pricing'
    	       })
    	       
     .state('accountPayableMaster', {
		    url: '/accountPayableMaster',
		    templateUrl: 'views/master/UnitMaster/billingMaster/account-payable-master.html',
		    controller: "accountPayableMasterController",
		    title: 'Account Payable Master',
		    resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 579);
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
	   		 }
		  })	     
	
	.state('accountPayable', {
		 url: '/accountPayable',
		 templateUrl: 'views/master/UnitMaster/billingMaster/account-payable.html',
		 controller: "MasterController",
		 title: 'Account Payable',
	})
	
	
	.state('accountPayableComparsion', {
		 url: '/accountPayableComparsion',
		 templateUrl: 'views/master/UnitMaster/billingMaster/account-payable-comparsion.html',
		 controller: "MasterController",
		 title: 'Account Payable Comparsion',
	})
	
	.state('accountPayableStatus', {
		 url: '/accountPayableStatus',
		 templateUrl: 'views/master/UnitMaster/billingMaster/account-payable-status.html',
		 controller: "MasterController",
		 title: 'Account Payable Status',
	})
	
	
	   .state('opdBilling', {
		    url: '/opdBilling/:patientId/:encounterId',
		    templateUrl: 'views/master/UnitMaster/billingMaster/opd-billing.html',
		    controller: "OpdBillingController",
		    params: {
            	encounterObj: {} // default value
            }
		  })
		  
		 /*
			 * .state('opdBill', { url: '/opdBilling', templateUrl:
			 * 'views/master/UnitMaster/billingMaster/opd-billing.html',
			 * controller: "OpdBillingController" })
			 */
		 .state('paymentMode', {
		    url: '/paymentMode',
		    templateUrl: 'views/common/payment-mode.html',
		    controller: "MasterController",
		  }) 
	
		  /*
			 * .state('addendumBill', { url: '/addendumBill', templateUrl:
			 * 'views/master/UnitMaster/billingMaster/addendum-bill.html',
			 * controller: "addendumBillListController", resolve : {
			 * 
			 * data: function($q, $state, $timeout) { var deferred = $q.defer();
			 * 
			 * $timeout(function() { var arr =
			 * JSON.parse(localStorage.getItem('permissions'))
			 * .modules.filter(function(item){ return (item.id == 554); });
			 * if(arr.length <= 0){ $state.go("accessDenied") deferred.reject(); }
			 * else { deferred.resolve(); } });
			 * 
			 * return deferred.promise; } } })
			 */ 
		  /*
			 * .state('detailsAddendumBill', { url: '/detailsAddendumBill',
			 * templateUrl:
			 * 'views/master/UnitMaster/billingMaster/details-addendum-bill.html',
			 * controller: "addendumBillDtlsController" })
			 */
		   .state('opdBilling.cash',
		  	       {
		   	        url : '/cash',
		   	        views :
		   	         {
		    	          '@opdBilling' :
		   	           {
		    	            templateUrl : 'views/common/cash.html',
		    	            controller : 'MasterController',
		   	           }
		    	         }
    	       })
    	       
    	        .state('opdBilling.cheque',
		  	       {
		   	        url : '/cheque',
		   	        views :
		   	         {
		    	          '@opdBilling' :
		   	           {
		    	            templateUrl : 'views/common/cheque.html',
		    	            controller : 'MasterController',
		   	           }
		    	         }
    	       })
    	       
    	       
    	        .state('opdBilling.creditDebitCard',
		  	       {
		   	        url : '/creditDebitCard',
		   	        views :
		   	         {
		    	          '@opdBilling' :
		   	           {
		    	            templateUrl : 'views/common/credit-debit-card.html',
		    	            controller : 'MasterController',
		   	           }
		    	         }
    	       })
    	       
    	 .state('billList', {
		 url: '/billList',
		 templateUrl: 'views/master/UnitMaster/billingMaster/bill-list.html',
		 controller: "billListController",
		 title: 'Bill List',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 13);
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
	   		params: {
	   			appointmentListObject: {} // default value
	            }
    	 })
    	 
    	 
    	 .state('billList.groupDetails',
		  	       {url : '/groupDetails',
    		 		title: 'Bill List',
		   	        views :{
		    	          '@billList' :{
		    	            templateUrl : 'views/master/UnitMaster/billingMaster/common/group-details.html',
		    	            // controller : 'billListController',
		    	            }}
		  	       	})
		  	       	
		  .state('billList.serviceDetails',
		  	       {url : '/serviceDetails',
			  		title: 'Bill List',
		   	        views :{
		    	          '@billList' :{
		    	            templateUrl : 'views/master/UnitMaster/billingMaster/common/service-details.html',
		    	            // controller : 'MasterController',
		    	            }}
		  	       	})
		  	 .state('billList.packageDetails',
		  	       {url : '/packageDetails',
		  		 	title: 'Bill List',
		   	        views :{
		    	          '@billList' :{
		    	            templateUrl : 'views/master/UnitMaster/billingMaster/common/package-details.html',
		    	            // controller : 'MasterController',
		    	            }}
		  	       	})
		  	       	
		  	.state('billList.groupDetailsPrePost',
		  	       {url : '/groupDetailsPrePost',
		  			title: 'Bill List',
		   	        views :{
		    	          '@billList' :{
		    	            templateUrl : 'views/master/UnitMaster/billingMaster/common/group-details-pre-post.html',
		    	            // controller : 'billListController',
		    	            }}
		  	       	}) 	
		  	       	
		  	       	.state('cancelledBillList', {
		 url: '/cancelledBillList',
		 templateUrl: 'views/master/UnitMaster/billingMaster/cancelled-bill-list.html',
		 controller: "cancelledBillListController",
		 title: 'Cancelled Bill List',
    	 })
    	 
    	 
    	 .state('cancelledBillList.groupDetails',
		  	       {url : '/groupDetails',
    		 		title: 'Cancelled Bill List',
		   	        views :{
		    	          '@cancelledBillList' :{
		    	            templateUrl : 'views/master/UnitMaster/billingMaster/common/group-details-cencelled-bill.html',
		    	            }}
		  	       	})
		  	       	
		  .state('cancelledBillList.serviceDetails',
		  	       {url : '/serviceDetails',
			  		title: 'Cancelled Bill List',
		   	        views :{
		    	          '@cancelledBillList' :{
		    	            templateUrl : 'views/master/UnitMaster/billingMaster/common/service-details-cencelled-bill.html',
		    	            }}
		  	       	})
		  	 .state('cancelledBillList.packageDetails',
		  	       {url : '/packageDetails',
		  		 	title: 'Cancelled Bill List',
		   	        views :{
		    	          '@cancelledBillList' :{
		    	            templateUrl : 'views/master/UnitMaster/billingMaster/common/package-details-cencelled-bill.html',
		    	            }}
		  	       	})
		  	       	
		  	.state('cancelledBillList.groupDetailsPrePost',
		  	       {url : '/groupDetailsPrePost',
		  			title: 'Cancelled Bill List',
		   	        views :{
		    	          '@cancelledBillList' :{
		    	            templateUrl : 'views/master/UnitMaster/billingMaster/common/group-details-pre-post-cancelled-bill.html',
		    	            // controller : 'billListController',
		    	            }}
		  	       	}) 	
		  	       	
		  	       	// by dinesh start
		  	 .state('workloadDashboard.serviceDetails',
		  	       {url : '/serviceDetails',
			  		title: 'Workload Dashboard',
		   	        views :{
		    	          '@workloadDashboard' :{
		    	            templateUrl : 'views/master/UnitMaster/billingMaster/common/work-order-service-details.html',
		    	            // controller : 'MasterController',
		    	            }}
		  	       	})
		  	 .state('workloadDashboard.packageDetails',
		  	       {url : '/packageDetails',
		  		 	title: 'Workload Dashboard',
		   	        views :{
		    	          '@workloadDashboard' :{
		    	            templateUrl : 'views/master/UnitMaster/billingMaster/common/work-order-package-details.html',
		    	            // controller : 'MasterController',
		    	            }}
		  	       	})
		  	       	// by dinesh end
		 .state('debitNote', {
		 url: '/debitNote',
		 templateUrl: 'views/master/UnitMaster/billingMaster/debit-note.html',
		 controller: "debitNoteController",
		 title: 'Debit Note',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 14);
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
	   		 }
    	 })
    	 
    	 
    	 
		   .state('debitNote.cash',
		  	       {
		   	        url : '/cash',
		   	        views :
		   	         {
		    	          '@debitNote' :
		   	           {
		    	            templateUrl : 'views/common/cash.html',
		    	            controller : 'MasterController',
		   	           }
		    	         }
    	       })
    	       
    	        .state('debitNote.cheque',
		  	       {
		   	        url : '/cheque',
		   	        views :
		   	         {
		    	          '@debitNote' :
		   	           {
		    	            templateUrl : 'views/common/cheque.html',
		    	            controller : 'MasterController',
		   	           }
		    	         }
    	       })
    	       
    	       
    	        .state('debitNote.creditDebitCard',
		  	       {
		   	        url : '/creditDebitCard',
		   	        views :
		   	         {
		    	          '@debitNote' :
		   	           {
		    	            templateUrl : 'views/common/credit-debit-card.html',
		    	            controller : 'MasterController',
		   	           }
		    	         }
    	       })
    	 
    	 
    	 .state('debitNoteList', {
		 url: '/debitNoteList',
		 templateUrl: 'views/master/UnitMaster/billingMaster/debit-list.html',
		 controller: "debitNoteListController",
		 title: 'Debit Note List',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 15);
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
	   		 }
    	 })
		  	
    	  /*
			 * .state('creditNote', { url: '/creditNote', templateUrl:
			 * 'views/master/UnitMaster/billingMaster/credit-note.html',
			 * controller: "creditNoteController", title: 'Credit Note', resolve : {
			 * 
			 * data: function($q, $state, $timeout) { var deferred = $q.defer();
			 * 
			 * $timeout(function() { var arr =
			 * JSON.parse(localStorage.getItem('permissions'))
			 * .modules.filter(function(item){ return (item.id == 16); });
			 * if(arr.length <= 0){ $state.go("accessDenied") deferred.reject(); }
			 * else { deferred.resolve(); } });
			 * 
			 * return deferred.promise; } } })
			 */
    	  .state('creditNote', {
         		 url: '/creditNote',
         		 templateUrl: 'views/master/UnitMaster/billingMaster/credit-note-new.html',
         		 controller: "creditNoteNewController",
         		 title: 'Credit Note',
         		 resolve : {

         	   			data: function($q, $state, $timeout) {
         	   		        var deferred = $q.defer();

         	   		        $timeout(function() {
         	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
         	   				 	.modules.filter(function(item){
         	   	            	   return (item.id == 16);
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
         	   		 }
             	 })
    	  .state('creditNoteList', {
		 url: '/creditNoteList',
		 templateUrl: 'views/master/UnitMaster/billingMaster/credit-note-list.html',
		 controller: "MasterController",
		 title: 'Credit Note List',
    	 })
    	       
	/** *****************Billing Master End***************** ******** */
	/** *****************asset Management Start************************* */
	// .state('fixedAssetType', {
	// url: '/fixedAssetType',
	// templateUrl:
	// 'views/master/UnitMaster/assetManagement/fixed-asset-type.html',
	// controller: "MasterController"
	// })
	
	.state('assetPMSScheduler', {
		 url: '/assetPMSScheduler',
		 templateUrl: 'views/master/UnitMaster/assetManagement/asset-PMS-scheduler.html',
		 controller: "preventiveMaintenanceController",
		 title: 'Asset PMS Scheduler',
	})
	.state('assetScheduler', {
		 url: '/assetScheduler',
		 templateUrl: 'views/master/UnitMaster/assetManagement/create-asset-scheduler.html',
		 controller: "createAssetSchedulerController",
		 title: 'Asset PMS Scheduler',
		 params: 
			{
			 assetInfoObj: {}
			} 
	})
	.state('viewAssetScheduler', {
		 url: '/viewAssetScheduler',
		 templateUrl: 'views/master/UnitMaster/assetManagement/view-asset-scheduler.html',
		 controller: "viewAssetSchedulerController",
		 title: 'Asset PMS Scheduler',
		 params: { assetDataObj: {}	} 
	})
	.state('PMRequest', {
		url: '/PMRequest',
		templateUrl: 'views/master/UnitMaster/assetManagement/PM-request.html',
		controller: "pmRequestController",
		title: 'PM Request',
	})
	.state('PMSchedule', {
		url: '/PMSchedule',
		templateUrl: 'views/master/UnitMaster/assetManagement/PM-schedule.html',
		controller: "pmScheduleController",
		title: 'PM Schedule',
	})
	.state('scheduleWorklist', {
		url: '/scheduleWorklist',
		templateUrl: 'views/master/UnitMaster/assetManagement/schedule-worklist.html',
		controller: "preventiveScheduleWorklistController",
		title: 'Schedule Worklist',
	})
	.state('detailScheduleWorklist', {
		url: '/detailScheduleWorklist',
		templateUrl: 'views/master/UnitMaster/assetManagement/details-schedule-worklist.html',
		controller: "detailScheduleWorklistController",
		title: 'Schedule Worklist',
		params: {	assetDataObject: {}	}
	})
	.state('correctiveMaintenance', {
		url: '/correctiveMaintenance',
		templateUrl: 'views/master/UnitMaster/assetManagement/corrective-maintenance.html',
		controller: "correctiveMaintenanceController",
		title: 'Corrective Maintenance Request',
	})
	.state('detailsCorrectiveMaintenance', {
		url: '/detailsCorrectiveMaintenance',
		templateUrl: 'views/master/UnitMaster/assetManagement/details-corrective-maintenance.html',
		controller: "detailsCorrectiveMaintenanceController",
		title: 'Corrective Maintenance Request',
		params: {
       	 detailsObj: {},
        }
	})
	.state('addNewCorrectiveMaintenance', {
		url: '/addNewCorrectiveMaintenance',
		templateUrl: 'views/master/UnitMaster/assetManagement/add-new-corrective-maintenance-worklist.html',
		controller: "addNewCorrectiveMaintenanceController",
		title: 'Corrective Maintenance Request',
	})
	.state('correctiveMaintenanceWorklist', {
		url: '/correctiveMaintenanceWorklist',
		templateUrl: 'views/master/UnitMaster/assetManagement/corrective-maintenance-worklist.html',
		controller: "correctiveMaintenanceWorklistController",
		title: 'Corrective Maintenance Worklist',
	})
	.state('detailsCorrectiveMaintenanceWorklist', {
		url: '/detailsCorrectiveMaintenanceWorklist',
		templateUrl: 'views/master/UnitMaster/assetManagement/details-corrective-maintenance-worklist.html',
		controller: "detailsCorrectiveMaintenanceWorklistController",
		title: 'Corrective Maintenance Worklist',
		params:{
			detailsObj : {},
		}
	})
	.state('correctiveMaintenanceScheduleWorklist', {
		url: '/correctiveMaintenanceScheduleWorklist',
		templateUrl: 'views/master/UnitMaster/assetManagement/corrective-maintenance-schedule-worklist.html',
		controller: "correctiveMaintenanceScheduleWorklistController",
		title: 'Corrective Maintenance Schedule Worklist',
	})
	.state('detailsCorrectiveMaintenanceScheduleWorklist', {
		url: '/detailsCorrectiveMaintenanceScheduleWorklist',
		templateUrl: 'views/master/UnitMaster/assetManagement/details-corrective-maintenance-schedule-worklist.html',
		controller: "detailsCorrectiveMaintenanceScheduleWorklistController",
		title: 'Corrective Maintenance Schedule Worklist',
		params:{
			detailsObj : {},
		}
	})
	/** *****************asset Management End************************* */
	
	.state('appointmentSchedulingUnitMaster', {
	  url: '/appointmentSchedulingUnitMaster',
	  title: 'Appointment Scheduling',
	  views :
      {
       '@' :
        {
    	   templateUrl: 'views/master/UnitMaster/Scheduling/appointment-scheduling-unit-master.html',
    		  controller: "appointmentSchedulingUnitMasterController",

   	  resolve : {

    			data: function($q, $state, $timeout) {
    		        var deferred = $q.defer();
    		
    		        $timeout(function() {
    		        	var arr = JSON.parse(localStorage.getItem('permissions'))
    				 	.modules.filter(function(item){
    	            	   return (item.id == 653);
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
    		 }
        }
      }
	})
	
	.state('appointmentSchedulingUnitMaster.year',
    	       {
    	        url : '/year',
    	        title: 'Appointment Scheduling',
    	        views :
    	         {
    	          '@appointmentSchedulingUnitMaster' :
    	           {
    	            templateUrl : 'views/AppointmentScheduling/year-slot-details.html',
    	            // controller : 'appointmentSchedulingController',
    	           }
    	         }
    	       })
    	       
    	       .state('appointmentSchedulingUnitMaster.month',
    	       {
    	        url : '/month',
    	        title: 'Appointment Scheduling',
    	        views :
    	         {
    	          '@appointmentSchedulingUnitMaster' :
    	           {
    	            templateUrl : 'views/AppointmentScheduling/month-slot-details.html',
    	            // controller : 'appointmentSchedulingController',
    	           }
    	         }
    	       })
    	       
    	       
    	        .state('appointmentSchedulingUnitMaster.week',
    	       {
    	        url : '/week',
    	        title: 'Appointment Scheduling',
    	        views :
    	         {
    	          '@appointmentSchedulingUnitMaster' :
    	           {
    	            templateUrl : 'views/AppointmentScheduling/week-slot-details.html',
    	            // controller : 'MasterController',
    	           }
    	         }
    	       })
    	       
    	       
    	         .state('appointmentSchedulingUnitMaster.day',
    	       {
    	        url : '/day',
    	        title: 'Appointment Scheduling',
    	        views :
    	         {
    	          '@appointmentSchedulingUnitMaster' :
    	           {
    	            templateUrl : 'views/AppointmentScheduling/day-slot-details.html',
    	           // controller : 'MasterController',
    	           }
    	         }
    	       })
	.state('modalityTypeMaster', {
	  url: '/modalityTypeMaster',
	  templateUrl: 'views/master/UnitMaster/Scheduling/modality-type-master.html',
	  controller: "modalityTypeMasterController",
	  title: 'Modality Type Master',
	  resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 654);
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
		 }
	})
	.state('modalityMaster', {
	  url: '/modalityMaster',
	  templateUrl: 'views/master/UnitMaster/Scheduling/modality-master.html',
	  controller: "modalityMasterController",
	  title: 'Modality Master',
	  resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 655);
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
		 }
	})
	// .state('consumableBarcodeConfig', {
	// url: '/consumableBarcodeConfig',
	// templateUrl:
	// 'views/master/UnitMaster/assetManagement/consumable-barcode-configuration.html',
	// controller: "MasterController"
	// })
	.state('scheduleWise', {
		url: '/scheduleWise',
		templateUrl: 'views/master/UnitMaster/scheduleBlockUnblock/schedule-wise.html',
		controller: "SlotBlockUnblockControllerScheduleWise",
		title: 'Schedule Wise',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 657);
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
			 }
	})
	.state('doctorWise', {
		url: '/doctorWise',
		templateUrl: 'views/master/UnitMaster/scheduleBlockUnblock/doctor-wise.html',
		controller: "SlotBlockUnblockControllerDoctorWise",
		title: 'Doctor Wise',
		resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 658);
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
			 }
	})
	
	/**
	 * ******************************************** End Unit
	 * master********************************************
	 */
	/** ***************** Mortuary Start ************************* */
		.state('mortuaryBedMaster', {
		 url: '/mortuaryBedMaster',
		 templateUrl: 'views/master/UnitMaster/Mortuary/Mortuary-Bed-Master.html',
		 controller: "mortuaryBedMasterController",
		 title: 'Mortuary Bed Master',
		 resolve : {

				data: function($q, $state, $timeout) {
			        var deferred = $q.defer();
			
			        $timeout(function() {
			        	var arr = JSON.parse(localStorage.getItem('permissions'))
					 	.modules.filter(function(item){
		            	   return (item.id == 649);
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
			 }
	})
	/** ***************** Mortuary Start ************************* */
	/** ***************** Media Master ************************* */
	.state('mediaMaster', {
		 url: '/mediaMaster',
		 templateUrl: 'views/LIS/media-master.html',
		 controller: "MasterController",
		 title: 'Media Master',
	})
	.state('incubationTestMaster', {
		 url: '/incubationTestMaster',
		 templateUrl: 'views/LIS/incubation-test-master.html',
		 controller: "MasterController",
		 title: 'Incubation Test Master',
	})
	.state('sampleTypeMediaMappingMaster', {
		 url: '/sampleTypeMediaMappingMaster',
		 templateUrl: 'views/LIS/sampletype-media-mapping-master.html',
		 controller: "sampleTypeMediaMapperAdditionController",
		 title: 'Sample Type Media Master',
	})
	/** ***************** Media Master ************************* */
	  
	
	
	/*
	 * .state('unknownPatientReg', { url: '/unknownPatientReg', templateUrl:
	 * 'views/AdmissionDischarge/ER/unknown-patient-reg.html', controller:
	 * "unknownPatientRegController" })
	 */
	  
	  
	  .state('unknownPatientReg',
       {
        url : '/unknownPatientReg',
        title: 'ER-Encounter',
        params: {
            patientDataERFlow: null
        },
        views :
         {
          '@' :
           {
            templateUrl : 'views/AdmissionDischarge/ER/unknown-patient-reg.html',
            controller : 'unknownPatientRegController',
           }
         }
       })
       
      .state('unknownPatientReg.personalForUnknown',
       {
        url : '/personalForUnknown',
        title: 'ER-Encounter',
        views :
         {
          '@unknownPatientReg' :
           {
            templateUrl : 'views/AdmissionDischarge/ER/unknown-personal.html',
            controller : 'unknownPatientRegController',
            resolve : {

      			data: function($q, $state, $timeout) {
      		        var deferred = $q.defer();
      		
      		        $timeout(function() {
      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
      				 	.modules.filter(function(item){
      	            	   return (item.id == 329);
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
      		 }
           }
         }
       })
       
        .state('unknownPatientReg.nextKinDetailsForUnknown',
       {
        url : '/nextKinDetailsForUnknown',
        title: 'ER-Encounter',
        views :
         {
          '@unknownPatientReg' :
           {
            templateUrl : 'views/AdmissionDischarge/next-of-kin.html',
            controller : 'unknownPatientRegController',
           }
         }
       })
	  
	
	/** *********************** Start Patient Registration ******************** */
		  
	.state('patientRegistration',
       {
        url : '/patientRegistration',
        title: 'Patient Registration',
        views :
         {
          '@' :
           {
            templateUrl : 'views/AdmissionDischarge/patient-registration.html',
            controller : 'patientRegistrationController',
            resolve : {

      			data: function($q, $state, $timeout) {
      		        var deferred = $q.defer();
      		
      		        $timeout(function() {
      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
      				 	.modules.filter(function(item){
      	            	   return (item.id == 3);
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
      		 }
           }
         },
         params: {
        	 patientRegistrationObj: {},
        	 patientRegistrationObjForAdmission: {},// default value
        	 patientRegistrationObjAbdm:{},
			 screenType:null,
			 patientObj:null,
         }
       })
       
      .state('patientRegistration.personal',
       {
        url : '/personal',
        views :
         {
          '@patientRegistration' :
           {
            templateUrl : 'views/AdmissionDischarge/personal-registration.html',
            // controller : 'patientRegistrationController',
           }
         }
       })
       
       .state('patientRegistration.contact',
       {
        url : '/contact',
        views :
         {
          '@patientRegistration' :
           {
            templateUrl : 'views/AdmissionDischarge/contact-registration.html',
           // controller : 'patientRegistrationController',
           }
         }
       })
       
       .state('patientRegistration.company',
       {
        url : '/company',
        views :
         {
          '@patientRegistration' :
           {
            templateUrl : 'views/AdmissionDischarge/company-patient-registration.html',
           // controller : 'patientRegistrationController',
           }
         }
       })
       /** *********************** End Patient Registration ******************** */
       
       /** *********************** Start Staff Registration ******************** */
       
       /*
		 * .state('staffRegistration', { url : '/staffRegistration', views : {
		 * '@' : { templateUrl :
		 * 'views/AdmissionDischarge/staff-registration.html', controller :
		 * 'MasterController', } } })
		 * 
		 * .state('staffRegistration.personal', { url : '/personal', views : {
		 * '@staffRegistration' : { templateUrl :
		 * 'views/AdmissionDischarge/personal-registration.html', controller :
		 * 'MasterController', } } })
		 * 
		 * .state('staffRegistration.contact', { url : '/contact', views : {
		 * '@staffRegistration' : { templateUrl :
		 * 'views/AdmissionDischarge/contact-registration.html', controller :
		 * 'MasterController', } } })
		 * 
		 * 
		 * .state('staffRegistration.professional', { url : '/professional',
		 * views : { '@staffRegistration' : { templateUrl :
		 * 'views/AdmissionDischarge/professional-staff.html', controller :
		 * 'MasterController', } } })
		 * 
		 * .state('staffRegistration.employment', { url : '/employment', views : {
		 * '@staffRegistration' : { templateUrl :
		 * 'views/AdmissionDischarge/employment-details.html', controller :
		 * 'MasterController', } } })
		 * 
		 * .state('staffRegistration.dependentDetails', { url :
		 * '/dependentDetails', views : { '@staffRegistration' : { templateUrl :
		 * 'views/AdmissionDischarge/next-of-kin.html', controller :
		 * 'MasterController', } } })
		 */
       
       
       
       /** *********************** End Staff Registration ******************** */
       
       
    	       
     /**
		 * *********************** Start Appointment Scheduling
		 * ********************
		 */
    	       .state('appointmentScheduling',
    	       {
    	        url : '/appointmentScheduling',
    	        title: 'Appointment Scheduling',
    	        views :
    	         {
    	          '@' :
    	           {
    	            templateUrl : 'views/AppointmentScheduling/appointment-scheduling.html',
    	            controller : 'appointmentSchedulingController',
    	            resolve : {

    		   			data: function($q, $state, $timeout) {
    		   		        var deferred = $q.defer();
    		   		
    		   		        $timeout(function() {
    		   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
    		   				 	.modules.filter(function(item){
    		   	            	   return (item.id == 4);
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
    		   		 }
    	           }
    	         }, params: {
 	            	appointmentObjectReschedule: {} // default value
 	            }
    	       })
    	       
    	     .state('appointmentScheduling.year',
    	       {
    	        url : '/year',
    	        title: 'Appointment Scheduling',
    	        views :
    	         {
    	          '@appointmentScheduling' :
    	           {
    	            templateUrl : 'views/AppointmentScheduling/year-slot.html',
    	            // controller : 'appointmentSchedulingController',
    	           }
    	         }
    	       })
    	       
    	       .state('appointmentScheduling.month',
    	       {
    	        url : '/month',
    	        title: 'Appointment Scheduling',
    	        views :
    	         {
    	          '@appointmentScheduling' :
    	           {
    	            templateUrl : 'views/AppointmentScheduling/month-slot.html',
    	            // controller : 'appointmentSchedulingController',
    	           }
    	         }
    	       })
    	       
    	       
    	        .state('appointmentScheduling.week',
    	       {
    	        url : '/week',
    	        title: 'Appointment Scheduling',
    	        views :
    	         {
    	          '@appointmentScheduling' :
    	           {
    	            templateUrl : 'views/AppointmentScheduling/week-slot.html',
    	            // controller : 'MasterController',
    	           }
    	         }
    	       })
    	       
    	       
    	         .state('appointmentScheduling.day',
    	       {
    	        url : '/day',
    	        title: 'Appointment Scheduling',
    	        views :
    	         {
    	          '@appointmentScheduling' :
    	           {
    	            templateUrl : 'views/AppointmentScheduling/day-slot.html',
    	           // controller : 'MasterController',
    	           }
    	         }
    	       })
    	      
    	       
    	       
    	       
    	       
    	       
    	       /**
				 * *********************** End Appointment Scheduling
				 * ********************
				 */
    	       
    	       
    	       /**
				 * *********************** Start Appointment Scheduling
				 * ********************
				 */
    	       
    	        .state('encounter',
    	       {
    	        url : '/encounter',
    	        title: 'Patient Visit',
    	        views :
    	         {
    	          '@' :
    	           {
    	            templateUrl : 'views/master/GlobalMaster/Encounter/encounter.html',
    	            controller : 'EncounterController',
    	            resolve : {

    		   			data: function($q, $state, $timeout) {
    		   		        var deferred = $q.defer();
    		   		
    		   		        $timeout(function() {
    		   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
    		   				 	.modules.filter(function(item){
    		   	            	   return (item.id == 5);
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
    		   		 }
    	           }
    	         },
    	        params: {
	            	appointmentObj: {}, // default value
	            	patientVisitDataScreen:null
	            }
    	       })
    	       
    	      .state('encounter.encounterDetails',
  	       {
   	        url : '/encounterDetails',
   	        title: 'Patient Visit',
   	        views :
   	         {
    	          '@encounter' :
   	           {
    	            templateUrl : 'views/master/GlobalMaster/Encounter/encounter-details.html',
    	            // controller : 'EncounterController',
   	           }
    	         }
    	       })
    	       
   	        .state('encounter.nextOfKin',
   	       {
    	        url : '/nextOfKin',
    	        title: 'Patient Visit',
    	        views :
   	         {
    	          '@encounter' :
    	           {
    	            templateUrl : 'views/AdmissionDischarge/next-of-kin.html',
    	            // controller : 'EncounterController',
    	           }
    	         }
    	       })
    	       
    	       
    	       
    	       .state('encounter.paymentEntitlement',
   	       {
    	        url : '/paymentEntitlement',
    	        title: 'Patient Visit',
    	        views :
    	         {
   	          '@encounter' :
    	           {
    	            templateUrl : 'views/master/GlobalMaster/Encounter/paymentEntitlement.html',
     	            // controller : 'MasterController',
     	           }
     	         }
    	     
   	       })
    	       /**
				 * *********************** End Appointment Scheduling
				 * ********************
				 */
    	       
    	       
    	       
	/** ***************** Asset Management ************************* */


	/** ***************** Asset Management ************************* */
	/** ***************** Inventory Page Start ************************* */

	.state('wasteStore', {
		 url: '/wasteStore',
		 templateUrl: 'views/PharmacyInventory/Store/waste-store.html',
		 controller: "MasterController",
		 title: 'Waste Store'
	})

	
	/** ***************** Inventory Page End ************************* */
    
    
    
     /***********************************************************************
		 * END Added BY Jyoti Date - 04-10-2017
		 **********************************************************************/
    
/*******************************************************************************
 * Inventory Page and procurement page start Added by Pranita Date : 06-10-2017
 ******************************************************************************/
	


	.state('addNewItem', {
		 url: '/addNewItem',
		 templateUrl: 'views/PharmacyInventory/Store/add-new-item.html',
		 controller: "MasterController",
		 title: 'Waste Store'
	})
	.state('quotationEnquiryApproval', {
		 url: '/quotationEnquiryApproval',
		 templateUrl: 'views/procurement/quotation/quotation-enquiry-approval.html',
		 controller: "MasterController",
		 title: 'Quotation Enquiry Approval'
	})
	/** *Added on 25-oct-2017 **** */
	.state('itemWiseMarkUp', {
		 url: '/itemWiseMarkUp',
		 templateUrl: 'views/PharmacyInventory/Store/item-wise-markup.html',
		 controller: "MasterController",
		 title: 'Item wise Mark Up',
	}) 
	.state('itemLocationAllocation', {
		 url: '/itemLocationAllocation',
		 templateUrl: 'views/PharmacyInventory/Store/item-location-allocation.html',
		 controller: "MasterController",
		 title: 'Item Location Allocation',
	})
	.state('ETOitemConfig', {
		 url: '/ETOitemConfig',
		 templateUrl: 'views/PharmacyInventory/Store/ETO-item-config.html',
		 controller: "MasterController",
		 title: 'ETO Item Configuration',
	})
	// .state('POCreation', {
	// url: '/POCreation',
	// templateUrl: 'views/procurement/quotation/PO-creation.html',
	// controller: "MasterController"
	// })
	 .state('detailsPOApprovalAssets', {
	   url: '/detailsPOApprovalAssets',
	   templateUrl: 'views/procurement/quotation/details-PO-creation-assets.html',
	   controller: "MasterController"
	 })
	 .state('detailsPOApprovalConsumable', {
	   url: '/detailsPOApprovalConsumable',
	   templateUrl: 'views/procurement/quotation/details-PO-creation-consumables.html',
	   controller: "MasterController"
	 })
	 .state('detailsGRNAssets', {
	   url: '/detailsGRNAssets',
	   templateUrl: 'views/procurement/quotation/details-GRN-assets.html',
	   controller: "MasterController"
	 })
	 .state('detailsGRNConsumable', {
	   url: '/detailsGRNConsumable',
	   templateUrl: 'views/procurement/quotation/details-GRN-consumable.html',
	   controller: "MasterController"
	 })
	 .state('addItemPOCreation', {
	   url: '/addItemPOCreation',
	   templateUrl: 'views/procurement/quotation/add-item-PO-creation.html',
	   controller: "MasterController",
	   title: 'PO Creation',
	 })
	 /** *************** Added on 17-Nov-2017 start************************ */
	 .state('detailsphysicalInspecAssets', {
	   url: '/detailsphysicalInspecAssets',
	   templateUrl: 'views/procurement/quotation/details-physical-inspection-assets.html',
	   controller: "MasterController"
	 })
	 .state('detailsphysicalInspecConsumables', {
	   url: '/detailsphysicalInspecConsumables',
	   templateUrl: 'views/procurement/quotation/details-physical-inspection-consumables.html',
	   controller: "MasterController"
	 })
	 /** *************** Added on 17-Nov-2017 end************************ */
	 // .state('addItemPO', {
	 // url: '/addItemPO',
	 // templateUrl: 'views/procurement/quotation/add-item-PO-creation.html',
	 // controller: "MasterController"
	 // })
	 /**
		 * ***quotation Enquiry Details for design use quotationEDetails for
		 * development *****
		 */
	  .state('quotationEArrovalDetails', {
	   url: '/quotationEArrovalDetails',
	   templateUrl: 'views/procurement/quotation/quotation-enquiry-approval-details.html',
	   controller: "MasterController",
	   title: 'Quotation Enquiry Approval'
	 })
		 
	.state('holdOnFillInDetails', {
		   url: '/holdOnFillInDetails',
		   templateUrl: 'views/procurement/quotation/quotation-fillinHoldon-details.html',
		   controller: "MasterController",
		   title: 'Quotation Fill In Details'
		 })
	 /**
		 * *************************** Diet and Nutrition
		 * **************************************
		 */
		.state('nutritionalMaster', {
			 url: '/nutritionalMaster',
			 templateUrl: 'views/master/GlobalMaster/DietNutrition/nutritional-master.html',
			 controller: "nutritionalMasterController",
			 title: 'Nutritional Master',
			 resolve : {

		   			data: function($q, $state, $timeout) {
		   		        var deferred = $q.defer();
		   		
		   		        $timeout(function() {
		   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
		   				 	.modules.filter(function(item){
		   	            	   return (item.id == 508);
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
		   		 }
		})
		.state('keyIngredientsMaster', {
			 url: '/keyIngredientsMaster',
			 templateUrl: 'views/master/GlobalMaster/DietNutrition/KeyIngredients-master.html',
			 controller: "keyIngredientsMasterController",
			 title: 'Key Ingredients Master',
			 resolve : {

		   			data: function($q, $state, $timeout) {
		   		        var deferred = $q.defer();
		   		
		   		        $timeout(function() {
		   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
		   				 	.modules.filter(function(item){
		   	            	   return (item.id == 509);
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
		   		 }
		})
		.state('dietTypeMaster', {
			 url: '/dietTypeMaster',
			 templateUrl: 'views/master/GlobalMaster/DietNutrition/diet-typemaster.html',
			 controller: "dietTypeMasterController",
			 title: 'Diet Type Master',
			 resolve : {

		   			data: function($q, $state, $timeout) {
		   		        var deferred = $q.defer();
		   		
		   		        $timeout(function() {
		   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
		   				 	.modules.filter(function(item){
		   	            	   return (item.id == 510);
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
		   		 }
		})
		.state('dishMaster', {
			 url: '/dishMaster',
			 templateUrl: 'views/master/GlobalMaster/DietNutrition/dish-master.html',
			 controller: "dishMasterController",
			 title: 'Dish Master',
			 resolve : {

		   			data: function($q, $state, $timeout) {
		   		        var deferred = $q.defer();
		   		
		   		        $timeout(function() {
		   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
		   				 	.modules.filter(function(item){
		   	            	   return (item.id == 511);
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
		   		 }
		})
		.state('mealMaster', {
			 url: '/mealMaster',
			 templateUrl: 'views/master/GlobalMaster/DietNutrition/meal-master.html',
			 controller: "mealMasterController",
			 title: 'Meal Master',
			 resolve : {

		   			data: function($q, $state, $timeout) {
		   		        var deferred = $q.defer();
		   		
		   		        $timeout(function() {
		   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
		   				 	.modules.filter(function(item){
		   	            	   return (item.id == 512 );
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
		   		 }
		})
		.state('daywiseDietTemplate', {
			 url: '/daywiseDietTemplate',
			 templateUrl: 'views/master/GlobalMaster/DietNutrition/daywise-diet-template.html',
			 controller: "dayWiseMasterController",
			 title: 'Day Wise Diet Template',
			 resolve : {

		   			data: function($q, $state, $timeout) {
		   		        var deferred = $q.defer();
		   		
		   		        $timeout(function() {
		   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
		   				 	.modules.filter(function(item){
		   	            	   return (item.id == 513 );
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
		   		 }
			  // redirectTo: '/addnewdaywise-diet-template.html'
		})
		.state('addnewdaywiseDietTemplate', {
			 url: '/addnewdaywiseDietTemplate',
			 templateUrl: 'views/master/GlobalMaster/DietNutrition/addnewdaywise-diet-template.html',
			 controller: "addDayWiseDietController",
			 title: 'Day Wise Diet Template',
		})
		.state('editdaywiseDietTemplate', {
			 url: '/editdaywiseDietTemplate/:dietTemplateId',
			 templateUrl: 'views/master/GlobalMaster/DietNutrition/addnewdaywise-diet-template.html',
			 controller: "addDayWiseDietController",
			 title: 'Day Wise Diet Template',
		})
		//added by chetana -20-02-2023
		.state('dietOrderPatientList', {
			 url: '/dietOrderPatientList',
			 templateUrl: 'views/master/GlobalMaster/DietNutrition/diet-order-patient-list.html',
			 controller: "dietOrderPatientListController",
			 title: 'Diet Order Patient List',
			
		})
		
		.state('dietPatientRequest', {
			 url: '/dietPatientRequest',
			 templateUrl: 'views/master/GlobalMaster/DietNutrition/diet-patient-request.html',
			 controller: "dietPatientRequestTransactionController",
			 title: 'Diet Planning',
			 params: {
		        	patientDetailObj: {} // default value
		        },
			 resolve : {

		   			data: function($q, $state, $timeout) {
		   		        var deferred = $q.defer();
		   		
		   		        $timeout(function() {
		   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
		   				 	.modules.filter(function(item){
		   	            	   return (item.id == 338);
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
		   		 }
			 
		})
		.state('dietOrder', {
			 url: '/dietOrder',
			 templateUrl: 'views/master/GlobalMaster/DietNutrition/diet-order.html',
			 controller: "dietOrderController",
			 title: 'Diet Order',
			 resolve : {

		   			data: function($q, $state, $timeout) {
		   		        var deferred = $q.defer();
		   		
		   		        $timeout(function() {
		   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
		   				 	.modules.filter(function(item){
		   	            	   return (item.id == 339);
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
		   		 }
		})
		.state('dietIssue', {
			 url: '/dietIssue',
			 templateUrl: 'views/master/GlobalMaster/DietNutrition/diet-issue.html',
			 controller: "dietIssueController",
			 title: 'Diet Issue',
			 resolve : {

		   			data: function($q, $state, $timeout) {
		   		        var deferred = $q.defer();
		   		
		   		        $timeout(function() {
		   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
		   				 	.modules.filter(function(item){
		   	            	   return (item.id == 340);
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
		   		 }
		})
		.state('dietAcceptance', {
			 url: '/dietAcceptance',
			 templateUrl: 'views/master/GlobalMaster/DietNutrition/diet-acceptance.html',
			 controller: "dietAcceptanceController",
			 title: 'Diet Acceptance',
			 resolve : {

		   			data: function($q, $state, $timeout) {
		   		        var deferred = $q.defer();
		   		
		   		        $timeout(function() {
		   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
		   				 	.modules.filter(function(item){
		   	            	   return (item.id == 341);
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
		   		 }
		})
		.state('rejectedDietList', {
			 url: '/rejectedDietList',
			 templateUrl: 'views/master/GlobalMaster/DietNutrition/rejected-diet-list.html',
			 controller: "rejectedDietListController",
			 title: 'Rejected Diet List',
			 resolve : {

		   			data: function($q, $state, $timeout) {
		   		        var deferred = $q.defer();
		   		
		   		        $timeout(function() {
		   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
		   				 	.modules.filter(function(item){
		   	            	   return (item.id == 342);
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
		   		 }
		})
		

		/*
		 * .state('transferRequest', { url: '/transferRequest', templateUrl:
		 * 'views/AdmissionDischarge/transfer-request.html', controller:
		 * "transferRequestController" })
		 */
		
		
		 .state('transferRequest',
    	       {
    	        url : '/transferRequest',
    	        title: 'Ward Transfer Request',
    	        views :
    	         {
    	          '@' :
    	           {
    	            templateUrl : 'views/AdmissionDischarge/transfer-request.html',
    	            controller : 'transferRequestController',
    	           }
    	         },
    	        params: {
    	    	  bedManagmentObj: {}
			    }
    	       })
    	       
    	      .state('transferRequest.initiateRequestTransferBed',
  	       {
    	    
   	        url : '/initiateRequestTransferBed',
   	     title: 'Ward Transfer Request',
   	        views :
   	         {
    	          '@transferRequest' :
   	           {
    	            templateUrl : 'views/AdmissionDischarge/transfer-bed-initiate-request.html',
    	            controller : 'transferRequestController',
    	            resolve : {

    		   			data: function($q, $state, $timeout) {
    		   		        var deferred = $q.defer();
    		   		
    		   		        $timeout(function() {
    		   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
    		   				 	.modules.filter(function(item){
    		   	            	   return (item.id == 67);
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
    		   		 }
   	           }
    	         }
    	       })
    	       
    	       
    	       
    	       
    	       .state('transferRequest.physicalTransferBed',
  	       {
   	        url : '/physicalTransferBed',
   	        title: 'Ward Transfer Request',
   	        views :
   	         {
    	          '@transferRequest' :
   	           {
    	            templateUrl : 'views/AdmissionDischarge/transfer-bed-physical.html',
    	            controller : 'transferRequestController',
   	           }
    	         }
    	       })
    	       
    	       // vivek start
    	       
    	/*
		 * .state('transferForInvestigation', { url:
		 * '/transferForInvestigation', templateUrl:
		 * 'views/Nursing/transfer-for-investigation.html', controller:
		 * "transferForInvestigationController" })
		 */
	  
    	        .state('transferForInvestigation',
    	       {
    	        url : '/transferForInvestigation',
    	        title: 'Transfer for Investigation',
    	        views :
    	         {
    	          '@' :
    	           {
    	            templateUrl :'views/Nursing/transfer-for-investigation.html',
    	            controller : 'transferForInvestigationController',
    	           }
    	         }
    	       })
    	       
    	      .state('transferForInvestigation.investigationTransferRequest',
  	       {
   	        url : '/investigationTransferRequest',
   	        views :
   	         {
    	          '@transferForInvestigation' :
   	           {
    	            templateUrl : 'views/Nursing/transfer-modality-initiate-request.html',
    	            controller : 'transferForInvestigationController',
   	           }
    	         }
    	       })
    	       
    	       .state('transferForInvestigation.modalityTransferRequestStatus',
  	       {
   	        url : '/modalityTransferRequestStatus',
   	        views :
   	         {
    	          '@transferForInvestigation' :
   	           {
    	            templateUrl : 'views/Nursing/modality-transfer-request-status.html',
    	            controller : 'transferForInvestigationController',
   	           }
    	         }
    	       })
    	       
    	       /*
				 * .state('transferToOT', { url: '/transferToOT', templateUrl:
				 * 'views/Nursing/transfer-to-OT.html', controller:
				 * "transferToOTController" })
				 */
    	       
    	       .state('transferToOT',
    	       {
    	        url : '/transferToOT',
    	        title: 'OT Transfer',
    	        views :
    	         {
    	          '@' :
    	           {
    	            templateUrl :'views/Nursing/transfer-to-OT.html',
    	            controller : 'transferToOTController',
    	           
    	            resolve : {

    		   			data: function($q, $state, $timeout) {
    		   		        var deferred = $q.defer();
    		   		
    		   		        $timeout(function() {
    		   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
    		   				 	.modules.filter(function(item){
    		   	            	   return (item.id == 68);
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
    		   		 }
    		
    	           }
    	         },
    	         params: 
    	         {
          	    	bedManagmentObj: {}
    			 }
    	       
    	       
    	       })
    	       
    	   .state('transfer', {
					 url: '/transfer',
					 templateUrl: 'views/AdmissionDischarge/direct-transfer.html',
					 controller: "directTransferController",
					 title: 'Ward Bed Transfer/Move'
    	       })
    	       
    	       
    	      .state('transferToOT.OTTransferRequest',
  	       {
   	        url : '/OTTransferRequest',
   	     title: 'OT Transfer',
   	        views :
   	         {
    	          '@transferToOT' :
   	           {
    	            templateUrl : 'views/Nursing/transfer-OT-initiate-request.html',
    	            controller : 'transferToOTController',
    	           
    	            resolve : {

    		   			data: function($q, $state, $timeout) {
    		   		        var deferred = $q.defer();
    		   		
    		   		        $timeout(function() {
    		   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
    		   				 	.modules.filter(function(item){
    		   	            	   return (item.id == 68);
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
    		   		 }
    		
   	           }
    	         }
    	       })
    	       
    	       .state('transferToOT.OTTransferRequestStatus',
  	       {
   	        url : '/OTTransferRequestStatus',
   	     title: 'OT Transfer',
   	        views :
   	         {
    	          '@transferToOT' :
   	           {
    	            templateUrl : 'views/Nursing/OT-Transfer-request-status.html',
    	            controller : 'transferToOTController',
   	           }
    	         }
    	       })
    	       
    	       
    	       .state('OTRequest', {
    	   	    url: '/OTRequest',
    	   	    templateUrl: 'views/Nursing/OT-request.html',
    	   	    controller: "OTRequestController",
    	   	    title: 'OT Request',
    	   	  })
    	       
    	       // Vivek End
    	       
    	       
    	       
    	       
    	        .state('initiateBedTransfer',
    	       {
    	        url : '/initiateBedTransfer',
    	        title: 'Bed Transfer Request',
    	        views :
    	         {
    	          '@' :
    	           {
    	            templateUrl : 'views/Nursing/initiate-bed-transfer.html',
    	            controller : 'initiateBedTransferController',
    	            resolve : {

    		   			data: function($q, $state, $timeout) {
    		   		        var deferred = $q.defer();
    		   		
    		   		        $timeout(function() {
    		   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
    		   				 	.modules.filter(function(item){
    		   	            	   return (item.id == 66);
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
    		   		 }
    		
    	           }
    	         },
    	         params: {
       	    	  bedManagmentObj: {}
   			    }
    	       })
    	       
    	      .state('initiateBedTransfer.initiateRequestTransferBed',
  	       {
   	        url : '/initiateRequestTransferBed',
   	        title: 'Bed Transfer Request',
   	        views :
   	         {
    	          '@initiateBedTransfer' :
   	           {
    	            templateUrl : 'views/Nursing/initiate-request-transfer-bed.html',
    	            controller : 'initiateBedTransferController',
    	            resolve : {

    		   			data: function($q, $state, $timeout) {
    		   		        var deferred = $q.defer();
    		   		
    		   		        $timeout(function() {
    		   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
    		   				 	.modules.filter(function(item){
    		   	            	   return (item.id == 66);
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
    		   		 }
    		
   	           }
    	         }
    	       })
    	       
    	       .state('initiateBedTransfer.physicalInitiateTransferBed',
  	       {
   	        url : '/physicalInitiateTransferBed',
   	        views :
   	         {
    	          '@initiateBedTransfer' :
   	           {
    	            templateUrl : 'views/Nursing/initiate-bed-physical-transfer.html',
    	            controller : 'initiateBedTransferController',
   	           }
    	         }
    	       })
		
    	 
    	       
		
		.state('transferRequestAcceptance', {
			 url: '/transferRequestAcceptance',
			 templateUrl: 'views/AdmissionDischarge/transfer-request-acceptance.html',
			 controller: "transferRequestAcceptanceController",
			 title: 'Transfer Request Acceptance',
			 resolve : {

		   			data: function($q, $state, $timeout) {
		   		        var deferred = $q.defer();
		   		
		   		        $timeout(function() {
		   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
		   				 	.modules.filter(function(item){
		   	            	   return (item.id == 36);
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
		   		 }
		
		})
		
		
	/**
	 * *************************** Diet and Nutrition
	 * **************************************
	 */
/*******************************************************************************
 * OTMS module page start Added by Sid Date : 04-06-2018
 ******************************************************************************/
	.state('facultyMaster', {
			 url: '/facultyMaster',
			 templateUrl: 'views/master/GlobalMaster/OTMS/faculty-master.html',
			 controller: "MasterController",
			 title: 'Faculty Master',
		})
	.state('OTIMSUser', {
			 url: '/OTIMSUser',
			 templateUrl: 'views/master/GlobalMaster/OTMS/OTIMS-user-master.html',
			 controller: "MasterController",
			 title: 'OTIMS User Master',
		})
		.state('uomMaster', {
			 url: '/uomMaster',
			 templateUrl: 'views/master/GlobalMaster/OTMS/uom-master.html',
			 controller: "MasterController",
			 title: 'UOM Master',
		})
		.state('noteMaster', {
			 url: '/noteMaster',
			 templateUrl: 'views/master/GlobalMaster/OTMS/note-master.html',
			 controller: "noteMasterTemplateController",
			 title: 'noteMaster',
			 resolve : {

		   			data: function($q, $state, $timeout) {
		   		        var deferred = $q.defer();
		   		
		   		        $timeout(function() {
		   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
		   				 	.modules.filter(function(item){
		   	            	   return (item.id == 529);
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
		   		 }
		
		})
		
/** ****************************************************************** */
/*******************************************************************************
 * OTMS module page start Added by Pranita Date : 01-06-2018
 ******************************************************************************/
	.state('cancellationMaster', {
		 url: '/cancellationMaster',
		 templateUrl: 'views/master/GlobalMaster/OTIMS/cancellation-reason.html',
		 controller: "MasterController",
		 title: 'Cancellation Master',
	})
	.state('operation_Theatre', {
		 url: '/operation_Theatre',
		 templateUrl: 'views/master/GlobalMaster/OTIMS/operation_Theatre.html',
		 controller: "operationTheatreController",
		 title: 'Operation Theatre',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 519);
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
	   		 }
	
	})
	.state('procedureType', {
		 url: '/procedureType',
		 templateUrl: 'views/master/GlobalMaster/OTIMS/procedure-type.html',
		 controller: "procedureTypeMasterController",
		 title: 'Clinical Service Type',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 520);
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
	   		 }
	
	})
	.state('operationTheatreType', {
		 url: '/operationTheatreType',
		 templateUrl: 'views/master/GlobalMaster/OTIMS/operation-theatre-type.html',
		 controller: "operationTheatreTypeController",
		 title: 'Operation Theatre Type',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 521);
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
	   		 }
	
	})
	.state('operationTable', {
		 url: '/operationTable',
		 templateUrl: 'views/master/GlobalMaster/OTIMS/operation_Table.html',
		 controller: "operationTableMasterController",
		 title: 'Operation Table',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 522);
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
	   		 }
	
	})
	.state('operationTableType', {
		 url: '/operationTableType',
		 templateUrl: 'views/master/GlobalMaster/OTIMS/operation_Table_Type.html',
		 controller: "operationTableTypeMasterController",
		 title: 'Operation Table Type',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 523);
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
	   		 }
	})
	.state('anaesthesiaType', {
		 url: '/anaesthesiaType',
		 templateUrl: 'views/master/GlobalMaster/OTIMS/anaesthesia_type.html',
		 controller: "anaesthesiaTypeController",
		 title: 'Anaesthesia Type',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 524);
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
	   		 }
	})
	.state('checklistItem', {
		 url: '/checklistItem',
		 templateUrl: 'views/master/GlobalMaster/OTIMS/checklist_master.html',
		 controller: "checklistItemMasterController",
		 title: 'Checklist Item',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 525);
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
	   		 }
	
	})
	.state('checklistMaster', {
		 url: '/checklistMaster',
		 templateUrl: 'views/master/GlobalMaster/OTIMS/checklist-item-master.html',
		 controller: "checklistMasterController",
		 title: 'Checklist Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 526);
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
	   		 }
	
	})
	.state('staffRole', {
		 url: '/staffRole',
		 templateUrl: 'views/master/GlobalMaster/OTIMS/staff-role.html',
		 controller: "MasterController",
		 title: 'Staff Role',
	})
	.state('consentType', {
		 url: '/consentType',
		 templateUrl: 'views/master/GlobalMaster/OTIMS/consent-type.html',
		 controller: "MasterController",
		 title: 'Consent Type',
	})
	.state('inventoryItem', {
		 url: '/inventoryItem',
		 templateUrl: 'views/master/GlobalMaster/OTIMS/inventory-item.html',
		 controller: "MasterController",
		 title: 'Inventory Item',
	})
	.state('inventoryItemType', {
		 url: '/inventoryItemType',
		 templateUrl: 'views/master/GlobalMaster/OTIMS/inventory-item-type.html',
		 controller: "MasterController",
		 title: 'Inventory Item Type',
	})
	.state('equipmentType', {
		 url: '/equipmentType',
		 templateUrl: 'views/master/GlobalMaster/OTIMS/equipment-type.html',
		 controller: "MasterController",
		 title: 'Equipment Type',
	})
	.state('serviceType', {
		 url: '/serviceType',
		 templateUrl: 'views/master/GlobalMaster/OTIMS/service-type.html',
		 controller: "MasterController",
		 title: 'Service Type',
	})
	.state('insuranceCompany', {
		 url: '/insuranceCompany',
		 templateUrl: 'views/master/GlobalMaster/OTIMS/insurance_company.html',
		 controller: "MasterController",
		 title: 'Insurance Company',
	})
	.state('typeOfStay', {
		 url: '/typeOfStay',
		 templateUrl: 'views/master/GlobalMaster/OTIMS/type_of_stay.html',
		 controller: "MasterController",
		 title: 'Type of Stay',
	})
	.state('generalPractitioner', {
		 url: '/generalPractitioner',
		 templateUrl: 'views/master/GlobalMaster/OTIMS/general_practitioner.html',
		 controller: "MasterController",
		 title: 'General Practitioner',
	})
	.state('procedureConfigurations', {
		 url: '/procedureConfigurations',
		 templateUrl: 'views/master/GlobalMaster/OTIMS/procedure_configurations.html',
		 controller: "procedureConfigurationsController",
		 title: 'Clinical Service Configuration',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 696);
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
	   		 }
	
		 
	})
										/*
										 * .state('additionalOtTimeConfiguration', {
										 * url:
										 * '/additionalOtTimeConfiguration',
										 * templateUrl:
										 * 'views/master/GlobalMaster/OTIMS/addtionalOtTimeConfig.html',
										 * controller: "MasterController" })
										 */
	.state('surgicalCalendar', {
		 url: '/surgicalCalendar',
		 templateUrl: 'views/master/GlobalMaster/OTIMS/surgical-calender.html',
		 controller: "surgicalCalendarMasterController",
		 title: 'Surgical Calendar',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 710);
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
	   		 }
	
	})
/** ***************** OTMS module page end ************ */		
/**
 * ***************** Equipment module - checklist master start
 * *************************
 */
	.state('checklistEquipmentMaster', {
		url: '/checklistEquipmentMaster',
	    templateUrl: 'views/master/GlobalMaster/EquipmentMaster/checklist-equipment-master.html',
		controller: "checklistEquipmentMasterController",
		title: 'Checklist Equipment Master',
		resolve : {

   			data: function($q, $state, $timeout) {
   		        var deferred = $q.defer();
   		
   		        $timeout(function() {
   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
   				 	.modules.filter(function(item){
   	            	   return (item.id == 526);
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
   		 }
	})
	.state('checklistEquipmentMapper', {
		url: '/checklistEquipmentMapper',
	    templateUrl: 'views/master/GlobalMaster/EquipmentMaster/checklist-equipment-mapper.html',
		controller: "checklistEquipmentMapperMasterController",
		title: 'Checklist Equipment Mapper',
		resolve : {

   			data: function($q, $state, $timeout) {
   		        var deferred = $q.defer();
   		
   		        $timeout(function() {
   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
   				 	.modules.filter(function(item){
   	            	   return (item.id == 526);
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
   		 }
	})
/**
 * ***************** Equipment module - checklist master end
 * *************************
 */
/*******************************************************************************
 * Inventory Page and procurement page end Added by Pranita Date : 06-10-2017
 ******************************************************************************/
 /** ***************************Demo Added By Vivek **************** */
		
		.state('transferRequestDemo', {
			 url: '/transferRequestDemo',
			 templateUrl: 'views/Transfer/transfer-request-demo.html',
			 controller: "transferRequestController",
			 title: 'Transfer Request Demo',
		})
 /** **************************Demo End***************************** */
	
/*******************************************************************************
 * Inventory New Pages start *************************** Added by - Pranita
 * Date:05/12/2017
 ******************************************************************************/		
	
	.state('physicalInventory', {
	 url: '/physicalInventory',
	 templateUrl: 'views/procurement/purchase/physical-inventory.html',
	 controller: "MasterController",
	 title: 'Physical Inventory',
	})
	.state('additemwPhysicalInven', {
	 url: '/additemwPhysicalInven',
	 templateUrl: 'views/procurement/purchase/add-item-physical-inventory.html',
	 controller: "MasterController",
	 title: 'Physical Inventory',
	})
	/** ********** Added by 7-Dec-2017 *********** */

	/*
	 * .state('detailsWorkorder', { url: '/detailsWorkorder', templateUrl:
	 * 'views/procurement/workOrder/details-workorder-request.html', controller:
	 * "MasterController" })
	 */
	
	
	
	.state('workorderQuotationNegotiation', {
		 url: '/workorderQuotationNegotiation',
		 templateUrl: 'views/procurement/workOrder/workorder-quotation-negotiation.html',
		 controller: "MasterController",
		 title: 'Work Order Quotation Negotiation',
	})
	.state('detailsWorkorderQuotationNegotiation', {
		 url: '/detailsWorkorderQuotationNegotiation',
		 templateUrl: 'views/procurement/workOrder/details-workorder-quotation-negotiation.html',
		 controller: "MasterController",
		 title: 'Work Order Quotation Negotiation',
	})

	
	/*
	 * .state('detailsWorkorderCreation', { url: '/detailsWorkorderCreation',
	 * templateUrl:
	 * 'views/procurement/workOrder/details-workorder-creation.html',
	 * controller: "MasterController" })
	 */
	.state('addItemRequestForAssestDisposalWorklist', {
		url: '/addItemRequestForAssestDisposalWorklist',
		templateUrl: 'views/PharmacyInventory/Store/add-item-request-for-assest-disposal-worklist.html',
		controller: "MasterController"
	})

	.state('addItemWasteAssestDisposalWorklist', {
		url: '/addItemWasteAssestDisposalWorklist',
		templateUrl: 'views/PharmacyInventory/Store/add-item-waste-disposal-worklist.html',
		controller: "MasterController",
		title: 'Add Item Waste Assest Disposal Worklist',
	})
	
	.state('assetsSupportStatus', {
	  url: '/assetsSupportStatus',
	  templateUrl: 'views/PharmacyInventory/Store/assets-support-status.html',
	  controller: "MasterController",
	  title: 'Asset Support Status'
	 })
	 
	 /** *********** added on consignmnet module 18-May-2018 start ********* */
	
	// .state('detailsConsignmentInward', {
	// url: '/detailsConsignmentInward',
	// templateUrl:
	// 'views/procurement/consignment/details-consignment-inward.html',
	// controller: "MasterController"
	// })
	 
	 
	  

	
	 /** *********** added on consignmnet module 17-May-2018 end ********* */
	 /** *********** added on Loan In Out module 18-May-2018 start ********* */
	 /** *********** added on Loan In Out module 18-May-2018 start ********* */
	//
	
	/*
	 * .state('appointments', { url: '/appointments', templateUrl:
	 * 'views/common/appointments.html', controller: "MasterController" })
	 */
		
		
		 .state('appointments',
    	       {
    	        url : '/appointments',
    	        title: 'Appointments',
    	        views :
    	         {
    	          '@' :
    	           {
    	        	  templateUrl: 'views/common/appointments/appointments.html',
    	            controller : 'MasterController',
    	           }
    	         }
    	       })
    	       
    	       .state('appointments.home',
  	       {
   	        url : '/home',
   	        views :
   	         {
    	          '@appointments' :
   	           {
    	            templateUrl : 'views/common/appointments/appointments-home.html',
    	            controller : 'MasterController',
   	           }
    	         }
    	       })
    	       
    	      .state('appointments.emergency',
  	       {
   	        url : '/emergency',
   	        views :
   	         {
    	          '@appointments' :
   	           {
    	            templateUrl : 'views/common/appointments/appointments-emergency.html',
    	            controller : 'MasterController',
   	           }
    	         }
    	       })
    	       
    	       .state('appointments.services',
  	       {
   	        url : '/services',
   	        views :
   	         {
    	          '@appointments' :
   	           {
    	            templateUrl : 'views/common/appointments/appointment-services.html',
    	            controller : 'bedManagementController',
   	           }
    	         }
    	       })
    	       
    	      .state('appointments.home.self', {
			        url: '/self',
			        // templateUrl:
					// 'views/common/appointments/appointments-insurance.html',
			    })
    	       .state('appointments.home.insurance', {
			        url: '/insurance',
			        templateUrl: 'views/common/appointments/appointments-insurance.html',
			    })
			    .state('appointments.home.creditCompany', {
			        url: '/creditCompany',
			        templateUrl: 'views/common/appointments/appointments-credit-company.html',
			    })
			    
			    .state('appointments.services.self', {
			        url: '/self',
			        // templateUrl:
					// 'views/common/appointments/appointments-insurance.html',
			    })
			     .state('appointments.services.insurance', {
			        url: '/insurance',
			        templateUrl: 'views/common/appointments/appointments-insurance.html',
			    })
			    .state('appointments.services.creditCompany', {
			        url: '/creditCompany',
			        templateUrl: 'views/common/appointments/appointments-credit-company.html',
			    })
			    
			    /* appointment list */
	    	       
	    	       .state('appointmentList',
		  	       {
		   	        url : '/appointmentList',
		   	        title: 'Patient Visit List',
		   	        views :  
		   	         {
		    	          '@' :
		   	           {
		    	        	templateUrl : 'views/common/appointments/appointment-list.html',
		    	        	
		    	            controller : 'appointmentListController',
		    	            resolve : {

		    		   			data: function($q, $state, $timeout) {
		    		   		        var deferred = $q.defer();
		    		   		
		    		   		        $timeout(function() {
		    		   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
		    		   				 	.modules.filter(function(item){
		    		   	            	   return (item.id == 7);
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
		    		   		 }
		    		
		   	           }
		    	         }
		    	       })
	    	       
	    	       
	    	       .state('appointmentList.home',
		  	       {
		   	        url : '/home',
		   	        views :
		   	         {
		    	          '@appointmentList' :
		   	           {
		    	        	templateUrl : 'views/common/appointments/appointment-list-home.html',
		    	            controller : 'appointmentListController',
		   	           }
		    	         }
		    	       })
	    	       
	    	       
	    	       .state('appointmentList.services',
		  	       {
		   	        url : '/Services',
		   	        views :
		   	         {
		    	          '@appointmentList' :
		   	           {
		    	        	  templateUrl : 'views/common/appointments/appointment-list-services.html',
		    	            controller : 'appointmentListController',
		   	           }
		    	         }
		    	       })
	    	       
	    	       
	    	       .state('appointmentList.missedcall',
		  	       {
		   	        url : '/Missedcall',
		   	        views :
		   	         {
		    	          '@appointmentList' :
		   	           {
		    	        	  templateUrl : 'views/common/appointments/appointment-list-missedcall.html',
		    	            controller : 'appointmentListController',
		   	           }
		    	         }
		    	       })
	//
	
/**
 * *************************** Inventory New Pages end
 * *********************************
 */
/**
 * *********************** Bed management - MEWS modal tabs
 * Start********************
 */
			      .state('bedManagement.MEWShistory',
			       {
			        url : '/MEWShistory',
			        title: 'Map of Bed',
			        views :
			         {
			          '@bedManagement' :
			           {
			            templateUrl : 'views/bedManagement/MEWS-history.html',
			            controller : 'mewsHistoryController',
			            params: {
					        obj: {}
					    }
			           }
			         }
			       })
			       .state('bedManagement.MEWSnew',
			       {
			        url : '/MEWSnew',
			        title: '',
			        views :
			         {
			          '@bedManagement' :
			           {
			            templateUrl : 'views/bedManagement/MEWS-new.html',
			            controller : 'addController',
			            params: {
					        obj1: {}
					    }
			          
			           }
			         }
			       })
//			       .state('bedManagement.nursingScale',
//			       {
//			        url : '/nursingScale',
//			        title: '',
//			        views :
//			         {
//			          '@bedManagement' :
//			           {
//			            templateUrl : 'views/bedManagement/nursing-scale.html',
//			            controller : 'nursingScaleScoreController',
//
//			           }
//			         },
//			         params: {
//                        obj: {}
//                       }
//
//			       })
			       
			       
			       
	    .state('mapOfBedForER', {
        url: '/mapOfBedForER',
        templateUrl: 'views/bedManagement/bed-management-er.html',
        controller:'mapOfBedERController',
        title: 'Map of Ward Bed',
        resolve : {

 			data: function($q, $state, $timeout) {
 		        var deferred = $q.defer();
 		
 		        $timeout(function() {
 		        	var arr = JSON.parse(localStorage.getItem('permissions'))
 				 	.modules.filter(function(item){
 	            	   return (item.id == 332);
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
	  params: {
	        obj: {}
	    }
	   
    })
    
    
			     .state('mapOfBedForER.MEWShistory',
			       {
			        url : '/MEWShistory',
			        views :
			         {
			          '@mapOfBedForER' :
			           {
			            templateUrl : 'views/bedManagement/MEWS-history.html',
			            controller : 'mewsHistoryController',
			            params: {
			            	obj: {}
					    }
			           }
			         }
			       })
			       .state('mapOfBedForER.MEWSnew',
			       {
			        url : '/MEWSnew',
			        views :
			         {
			          '@mapOfBedForER' :
			           {
			            templateUrl : 'views/bedManagement/MEWS-new.html',
			            controller : 'addController',
			            params: {
					        obj1: {}
					    }
			          
			           }
			         }
			       })
/**
 * *********************** Bed management - MEWS modal tabs
 * end********************
 */		
/**
 * *********************** Bed management - GCS modal tabs
 * Start********************
 */
			      .state('bedManagement.GCShistory',
			       {
			        url : '/GCShistory',
			        title : 'Map of Bed',
			        views :
			         {
			          '@bedManagement' :
			           {
			            templateUrl : 'views/bedManagement/GCS-history.html',
			            controller : 'MasterController',
			           }
			         }
			       })
			       .state('bedManagement.GCSnew',
			       {
			        url : '/GCSnew',
			        views :
			         {
			          '@bedManagement' :
			           {
			            templateUrl : 'views/bedManagement/GCS-new.html',
			            controller : 'MasterController',
			           }
			         }
			       })
/**
 * *********************** Bed management - GCS modal tabs
 * end********************
 */		
/**
 * *********************** quotation Enquiry - Expected Terms modal tabs
 * Start********************
 */
			      .state('addItemquotationEnquiry.warrantyPeriod',
			       {
			        url : '/warrantyPeriod',
			        title: 'Quotation Enquiry',
			        views :
			         {
			          '@addItemquotationEnquiry' :
			           {
			            templateUrl : 'views/procurement/quotation/warranty-period.html',
			            controller : 'MasterController',
			           }
			         }
			       })
			       .state('addItemquotationEnquiry.expectedSupportTerms',
			       {
			        url : '/expectedSupportTerms',
			        views :
			         {
			          '@addItemquotationEnquiry' :
			           {
			            templateUrl : 'views/procurement/quotation/expected-support-terms.html',
			            controller : 'MasterController',
			           }
			         }
			       })
			       .state('addItemquotationEnquiry.expectedPaymentTerms',
			       {
			        url : '/expectedPaymentTerms',
			        views :
			         {
			          '@addItemquotationEnquiry' :
			           {
			            templateUrl : 'views/procurement/quotation/expected-payment-terms.html',
			            controller : 'MasterController',
			           }
			         }
			       })
			        .state('addItemquotationEnquiry.expectedTechnicalSpec',
			       {
			        url : '/expectedTechnicalSpec',
			        views :
			         {
			          '@addItemquotationEnquiry' :
			           {
			            templateUrl : 'views/procurement/quotation/expected-technical-specification.html',
			            controller : 'MasterController',
			           }
			         }
			       })
/** ****************************************************************** */
			        .state('quotationEDetails.warrantyPeriod',
			       {
			        url : '/warrantyPeriod',
			        title: 'Quotation Enquiry',
			        views :
			         {
			          '@quotationEDetails' :
			           {
			            templateUrl : 'views/procurement/quotation/warranty-period.html',
			            controller : 'MasterController',
			           }
			         }
			       })
			       .state('quotationEDetails.expectedSupportTerms',
			       {
			        url : '/expectedSupportTerms',
			        views :
			         {
			          '@quotationEDetails' :
			           {
			            templateUrl : 'views/procurement/quotation/expected-support-terms.html',
			            controller : 'MasterController',
			           }
			         }
			       })
			       .state('quotationEDetails.expectedPaymentTerms',
			       {
			        url : '/expectedPaymentTerms',
			        views :
			         {
			          '@quotationEDetails' :
			           {
			            templateUrl : 'views/procurement/quotation/expected-payment-terms.html',
			            controller : 'MasterController',
			           }
			         }
			       })
			        .state('quotationEDetails.expectedTechnicalS',
			       {
			        url : '/expectedTechnicalS',
			        views :
			         {
			          '@addItemquotationEnquiry' :
			           {
			            templateUrl : 'views/procurement/quotation/expected-technical-specification.html',
			            controller : 'MasterController',
			           }
			         }
			       })
/**
 * *********************** quotation Enquiry - Expected Terms modal tabs
 * end********************
 */	
/**
 * *********************** quotation Fill in - Expected Terms modal tabs
 * Start********************
 */
			      .state('addNewquotationFillIn.warrantyPeriodFillin',
			       {
			        url : '/warrantyPeriodFillin',
			        title: 'Quotation Fill In',
			        views :
			         {
			          '@addNewquotationFillIn' :
			           {
			           templateUrl : 'views/procurement/quotation/warranty-period-fillin.html',
			            controller : 'MasterController',
			           }
			         }
			       })
			       
			       .state('addNewquotationFillIn.expectedSupportTermsfillIn',
			       {
			        url : '/expectedSupportTermsfillIn',
			        views :
			         {
			          '@addNewquotationFillIn' :
			           {
			            templateUrl : 'views/procurement/quotation/expected-support-terms-fillin.html',
			            controller : 'MasterController',
			           }
			         }
			       })
			       .state('addNewquotationFillIn.expectedPaymentTermsfillIn',
			       {
			        url : '/expectedPaymentTermsfillIn',
			        views :
			         {
			          '@addNewquotationFillIn' :
			           {
			            templateUrl : 'views/procurement/quotation/expected-payment-terms-fillin.html',
			            controller : 'MasterController',
			           }
			         }
			       })
			        .state('addNewquotationFillIn.expectedTechnicalSfillIn',
			       {
			        url : '/expectedTechnicalSfillIn',
			        views :
			         {
			          '@addNewquotationFillIn' :
			           {
			            templateUrl : 'views/procurement/quotation/expected-technical-specification-fillin.html',
			            controller : 'MasterController',
			           }
			         }
			       })
			        .state('qfillInDetails.warrantyPFillin',
			       {
			        url : '/warrantyPFillin',
			        views :
			         {
			          '@qfillInDetails' :
			           {
			           templateUrl : 'views/procurement/quotation/warranty-period-fillinDetails.html',
			            controller : 'MasterController',
			           }
			         }
			       })
			        .state('qfillInDetails.expectedSTermsfillIn',
			       {
			        url : '/expectedSTermsfillIn',
			        views :
			         {
			          '@qfillInDetails' :
			           {
			            templateUrl : 'views/procurement/quotation/expected-support-terms-fillinDetails.html',
			            controller : 'MasterController',
			           }
			         }
			       })
			       .state('qfillInDetails.expectedPaymentTfillIn',
			       {
			        url : '/expectedPaymentTfillIn',
			        views :
			         {
			          '@qfillInDetails' :
			           {
			            templateUrl : 'views/procurement/quotation/expected-payment-terms-fillinDetails.html',
			            controller : 'MasterController',
			           }
			         }
			       })
			        .state('qfillInDetails.expectedTSpecificationfillIn',
			       {
			        url : '/expectedTSpecificationfillIn',
			        views :
			         {
			          '@qfillInDetails' :
			           {
			            templateUrl : 'views/procurement/quotation/expected-technical-specification-fillinDetails.html',
			            controller : 'MasterController',
			           }
			         }
			       })
/**
 * *********************** quotation Fill in - Expected Terms modal tabs
 * End********************
 */     
   /***************************************************************************
	 * Start Added BY Jyoti Date - 20-12-2017
	 **************************************************************************/
  /**
	 * ******************************************** START OPD COVERSHEET
	 * ********************************************
	 */
 
  .state('OPDCoverSheet', {
			 url: '/OPDCoverSheet',
			 templateUrl: 'views/OPD/CoverSheet/opd-coversheet.html',
			 controller: "OPDCoverSheetController",
			 title: 'OPD Cover Sheet',
			 params: {
		        	appointmentObj: {} // default value
		        }
		})
	 .state('pharman', {
			 url: '/pharman',
			 templateUrl: 'views/AppointmentScheduling/medicineInfo.html',
			 controller: "pharmanController",
			 title: 'Pharman Page',
			 params: {
		        	appointmentObj: {} // default value
		        }
		})	
  .state('advisedVisits', {
			 url: '/advisedVisits',
			 templateUrl: 'views/OPD/advised-visits.html',
			 controller: "advisedVisitsController",
			 title: 'Advised Visits',
		})
		
		.state('SpeechToText', {		
			 url: '/OPDCoverSheet',		
			 templateUrl: 'views/OPD/CoverSheet/opd-coversheet.html',		
			 controller: "SpeechToText",	
			 title: 'OPD Cover Sheet',
			 params: {		
		        	appointmentObj: {} // default value
		        }		
		})
		
		.state('adviseMedication', {
			 url: '/adviseMedication',
			 templateUrl: 'views/OPD/advise-medication.html',
			 controller: "MasterController",
			 title: 'Medication',
		})
		
		.state('interconsultation', {
			 url: '/interconsultation',
			 templateUrl: 'views/OPD/inter-consultation.html',
			 controller: "MasterController",
			 title: 'Consult Order',
		})

		
   /**
	 * ******************************************** END OPD COVERSHEET
	 * ********************************************
	 */   	       
 
			  .state('patientList', {
			  url: '/patientList',
			  templateUrl: 'views/common/appointments/patient-list.html',
			  controller: "patientListController",
			  title: 'Patient List',
				resolve : {

		   			data: function($q, $state, $timeout) {
		   		        var deferred = $q.defer();
		   		
		   		        $timeout(function() {
		   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
		   				 	.modules.filter(function(item){
		   	            	   return (item.id == 8);
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
		   		 }
			 })
			 .state('doctorList', {
			  url: '/doctorList',
			  templateUrl: 'views/common/appointments/doctor-list.html',
			  controller: "MasterController",
			  title: 'Doctor List',
			 })
			 .state('smsConfig', {
			  url: '/smsConfig',
			  templateUrl: 'views/common/appointments/Master Management/sms-config.html',
			  controller: "MasterController",
			  title: 'SMS Configuration',
			 })

			 .state('complaintConfig', {
			  url: '/complaintConfig',
			  templateUrl: 'views/common/appointments/Master Management/complaint-config.html',
			  controller: "MasterController",
			  title: 'complaintConfig',
			 })

			 .state('physicalExaminationConfig', {
			  url: '/physicalExaminationConfig',
			  templateUrl: 'views/common/appointments/Master Management/physical-examination-config.html',
			  controller: "MasterController",
			  title: 'Physical Examination Configuration',
			 })
			 
			 .state('instructionConfig', {
			  url: '/instructionConfig',
			  templateUrl: 'views/common/appointments/Master Management/instruction-config.html',
			  controller: "MasterController",
			  title: 'Instruction Configuration',
			 })
			 
			 .state('diagnosisConfig', {
			  url: '/diagnosisConfig',
			  templateUrl: 'views/common/appointments/Master Management/diagnosis-config.html',
			  controller: "MasterController",
			  title: 'Diagnosis Configuration',
			 })
			 
			 .state('observationConfig', {
			  url: '/observationConfig',
			  templateUrl: 'views/common/appointments/Master Management/observation-config.html',
			  controller: "MasterController",
			  title: 'Observation Configuration',
			 })
			
			 .state('adviceConfig', {
			  url: '/adviceConfig',
			  templateUrl: 'views/common/appointments/Master Management/advice-config.html',
			  controller: "MasterController",
			  title: 'Advice Configuration',
			 })
				
			 .state('historyConfig', {
			  url: '/historyConfig',
			  templateUrl: 'views/common/appointments/Master Management/history-config.html',
			  controller: "MasterController",
			  title: 'History Configuration',
			 })
			 
			 /*
				 * .state('complaintConfig', { url: '/complaintConfig',
				 * templateUrl: 'views/common/appointments/Coversheet
				 * Master/complaint-config.html', controller: "MasterController" })
				 */
			 
			 .state('prescriptionDetails', {
			  url: '/prescriptionDetails',
			  templateUrl: 'views/common/appointments/Master Management/prescription-Details.html',
			  controller: "MasterController",
			  title: 'Prescription Details',
			 })
			 /**
				 * ******************************************** END OPD
				 * COVERSHEET ********************************************
				 */   	       
			 /**
				 * *********************** Bed management - fall risk modal tabs
				 * Start********************
				 */
			 .state('nursingScale', {
			    url: '/nursingScale',
			    templateUrl: 'views/bedManagement/nursing-scale.html',
			    controller:'nursingScaleScoreController',
			    title: 'Nursing Scale',
                 params: {
                    obj: {}
                   }
			})
			  .state('nursingScale.FallRiskHistory',
			   {
			    url : '/FallRiskHistory',
			    title: 'Nursing Scale',
			    views :
			     {
			      '@nursingScale' :
			       {
			        templateUrl : 'views/bedManagement/FallRisk-history.html',
			        controller : 'nursingScaleScoreController',
			       }
			     }
			   })
			   .state('nursingScale.FallRiskNew',
			   {
			    url : '/FallRiskNew',
			    title: 'Nursing Scale',
			    views :
			     {
			      '@nursingScale' :
			       {
			        templateUrl : 'views/bedManagement/FallRisk-new.html',
			        controller : 'MasterController',
			       }
			     }
			   })
			   .state('nursingScale.GCSScoreShow',
			   {
			    url : '/GCSScoreShow',
			    title: 'Nursing Scale',
			    views :
			     {
			      '@nursingScale' :
			       {
			        templateUrl : 'views/bedManagement/GCS-score-show.html',
			        controller : 'MasterController',
			       }
			     }
			   })
			/**
			 * *********************** Bed management - fall risk modal tabs
			 * end********************
			 */
    	       
/**
 * *********************** pharmacy bill list start
 * ********************************
 */
    	 .state('pharmacyBillList.cash',
  	       {
   	        url : '/cash',
   	        views :
	   	         {
	    	          '@pharmacyBillList' :
	   	           {
	    	            templateUrl : 'views/common/cash.html',
	    	            controller : 'MasterController',
	   	           }
	    	    }
  	       })
	        .state('pharmacyBillList.cheque',
	  	       {
	   	        url : '/cheque',
	   	        views :
	   	         {
	    	          '@pharmacyBillList' :
	   	           {
	    	            templateUrl : 'views/common/cheque.html',
	    	            controller : 'MasterController',
	   	           }
	    	     }
	        })
	        .state('pharmacyBillList.creditDebitCard',
	  	       {
	   	        url : '/creditDebitCard',
	   	        views :
	   	         {
	    	          '@pharmacyBillList' :
	   	           {
	    	            templateUrl : 'views/common/credit-debit-card.html',
	    	            controller : 'MasterController',
	   	           }
	    	     }
	        })
/**
 * *********************** pharmacy bill list end
 * ********************************
 */
/**
 * ***************** Global - LIS Start (Added :
 * 24-Jan-208)*************************
 */	        
    .state('antibioticAdditionGroupMaster', {
    	url: '/antibioticAdditionGroupMaster',
    	templateUrl: 'views/master/LIS/antibiotic-addition-group-master.html',
    	controller: "antibioticGroupAddAntibioticMapperController",
    	title: 'Addition of Antibiotic Group Master',
    	resolve : {

   			data: function($q, $state, $timeout) {
   		        var deferred = $q.defer();
   		
   		        $timeout(function() {
   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
   				 	.modules.filter(function(item){
   	            	   return (item.id == 433);
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
   		 }

	})
		.state('colonyTestWithMediaMaster', {
			 url: '/colonyTestWithMediaMaster',
			 templateUrl: 'views/master/LIS/colony-test-with-media-master.html',
			 controller: "mediaColnoyAdditionController",
			 title: 'Colony Test With Media Master',
		})
/**
 * ***************** Global - LIS End (Added :
 * 24-Jan-208)*************************
 */		
/**
 * ***************** Mortuary bed management Start (Added :
 * 24-Jan-208)*************************
 */
	.state('mortuaryBedManagement', {
	 url: '/mortuaryBedManagement',
	 templateUrl: 'views/master/UnitMaster/Mortuary/mortuary-bed-management.html',
	 controller: "mortuaryBedManagementController",
	 title: 'Mortuary Bed Management',
	 resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 368);
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
		 }

})
/**
 * ***************** Mortuary bed management End (Added :
 * 24-Jan-208)*************************
 */
/**
 * ***************** Unit master new page Start (Added :
 * 29-Jan-208)*************************
 */
// .state('storewiseGenericMapper', {
// url: '/storewiseGenericMapper',
// templateUrl:
// 'views/master/UnitMaster/pharmacy/storewise-generic-mapper.html',
// controller: "MasterController"
// })
.state('addstorewiseGenericMapper', {
	 url: '/addstorewiseGenericMapper',
	 templateUrl: 'views/master/UnitMaster/pharmacy/add-new-storewise-generic-mapper.html',
	 controller: "MasterController",
	 title: 'Storewise Generic Mapper Master',
})
/**
 * ***************** Unit master new page End (Added :
 * 29-Jan-208)*************************
 */
            // the main template will be placed here (relatively named)
            /*
			 * '': { templateUrl: 'views/about.html', controller: 'aboutCtrl' },
			 */

            // the child views will be defined here (absolutely named)
            /* 'columnOne@about': { template: 'Look I am a column!' }, */

            // for column two, we'll define a separate controller
          /*
			 * 'columnTwo@about': { templateUrl: 'views/table-data.html',
			 * //controller: 'scotchController' } }
			 */

 /* }); */
 /** *********************** Tax Master ******************************** */	
		// .state('taxComponentMaster', {
		// url: '/taxComponentMaster',
		// templateUrl: 'views/master/GlobalMaster/tax-component-master.html',
		// controller: "MasterController"
		// })
		// .state('taxMaster', {
		// url: '/taxMaster',
		// templateUrl: 'views/master/GlobalMaster/tax-master.html',
		// controller: "MasterController"
		// })
		
/** *********************** Store Wise Mapping ******************************** */
		
		
		
       /**
		 * *********************** Global (Nursing)
		 * ********************************
		 */
		.state('shiftMaster', {
		 url: '/shiftMaster',
		 templateUrl: 'views/Nursing/shift-master.html',
		 controller: "shiftMasterController",
		 title: 'Shift Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 408);
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
	   		 }
	
	})
	.state('rotaCreation', {
		 url: '/rotaCreation',
		 templateUrl: 'views/Nursing/rota-creation.html',
		 controller: "MasterController",
		 title: 'Rota Creation Master',
	})
	.state('addnewRotaCreation', {
		 url: '/addnewRotaCreation',
		 templateUrl: 'views/Nursing/add-new-rota-creation.html',
		 controller: "MasterController",
		 title: 'Rota Creation Master',
	})
	
	.state('addnewDaywiseRotaCreation', {
		 url: '/addnewDaywiseRotaCreation',
		 templateUrl: 'views/Nursing/add-new-day-wise-rota-creation.html',
		 controller: "MasterController",
		 title: 'Day Wise Rota Creation Master',
	})
	 /**
		 * *********************** Unit Master (CSSD)
		 * ********************************
		 */
	.state('sterilizationTypeMaster', {
		 url: '/sterilizationTypeMaster',
		 templateUrl: 'views/master/UnitMaster/CSSD/sterilization-type-master.html',
		 controller: "sterilizationTypeMasterController",
		 title: 'Sterilization Type Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 552);
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
	   		 }
	
	})
	.state('autoclaveMaster', {
		 url: '/autoclaveMaster',
		 templateUrl: 'views/master/UnitMaster/CSSD/autoclave-master.html',
		 controller: "autoClaveMasterController",
		 title: 'Autoclave Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 553);
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
	   		 }
	
	})
	.state('wrapperExpiryMaster', {
		 url: '/wrapperExpiryMaster',
		 templateUrl: 'views/master/UnitMaster/CSSD/wrapper-expiry-master.html',
		 controller: "wrapperExpiryMasterController",
		 title: 'Wrapper Expiry Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 554);
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
	   		 }
	
	})
	.state('preSterilizationProcess', {
		 url: '/preSterilizationProcess',
		 templateUrl: 'views/master/UnitMaster/CSSD/pre-sterilization-process.html',
		 controller: "preSterilizationCleaningController",
		 title: 'Pre-Sterilization Process',
	})
	.state('addNewpreSterilizationProcess',
       {
        url : '/addNewpreSterilizationProcess',
        title: 'Pre-Sterilization Process',
        views :
         {
          '@' :
           {
        	  templateUrl: 'views/master/UnitMaster/CSSD/add-new-pre-sterilization-process.html',
            controller : 'MasterController',
           }
         }
       })
       
      .state('addNewpreSterilizationProcess.instrumentCleaning',
       {
        url : '/instrumentCleaning',
        views :
         {
          '@addNewpreSterilizationProcess' :
           {
            templateUrl : 'views/master/UnitMaster/CSSD/instrumentCleaning.html',
            controller : 'MasterController',
           }
         }
       })
       .state('addNewpreSterilizationProcess.inspection',
       {
        url : '/inspection',
        views :
         {
          '@addNewpreSterilizationProcess' :
           {
            templateUrl : 'views/master/UnitMaster/CSSD/inspection.html',
            controller : 'MasterController',
           }
         }
       })
       .state('addNewpreSterilizationProcess.dis-Infection',
       {
        url : '/dis-Infection',
        views :
         {
          '@addNewpreSterilizationProcess' :
           {
            templateUrl : 'views/master/UnitMaster/CSSD/dis-Infection.html',
            controller : 'MasterController',
           }
         }
       })
       .state('addNewpreSterilizationProcess.kitFormation',
       {
        url : '/kitFormation',
        views :
         {
          '@addNewpreSterilizationProcess' :
           {
            templateUrl : 'views/master/UnitMaster/CSSD/kit-Formation.html',
            controller : 'MasterController',
           }
         }
       })
       .state('addNewpreSterilizationProcess.wrapper',
       {
        url : '/wrapper',
        views :
         {
          '@addNewpreSterilizationProcess' :
           {
            templateUrl : 'views/master/UnitMaster/CSSD/wrapper.html',
            controller : 'MasterController',
           }
         }
       })
       .state('sterilizationProcess', {
		 url: '/sterilizationProcess',
		 templateUrl: 'views/master/UnitMaster/CSSD/sterilization-process.html',
		 controller: "MasterController",
		 title: 'Sterilization Process',
       })
       .state('addNewSterilizationProcess', {
		 url: '/addNewSterilizationProcess',
		 templateUrl: 'views/master/UnitMaster/CSSD/add-new-sterilization-process.html',
		 controller: "MasterController",
		 title: 'Sterilization Process',
       })
       .state('viewSterilizationProcess', {
		 url: '/viewSterilizationProcess',
		 templateUrl: 'views/master/UnitMaster/CSSD/view-sterilization-process.html',
		 controller: "MasterController",
		 title: 'Sterilization Process',
       })
       .state('KitIndent', {
		 url: '/KitIndent',
		 templateUrl: 'views/master/UnitMaster/CSSD/kit-indent.html',
		 controller: "kitIndentController",
		 title: 'Kit Indent',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 89);
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
	   		 }
	
       })
       .state('addNewKitIndent', {
		 url: '/addNewKitIndent',
		 templateUrl: 'views/master/UnitMaster/CSSD/add-new-kit-indent.html',
		 controller: "addNewKitIndentController",
	     title: 'Kit Indent',
       })
       .state('updateKitIndent', {
		 url: '/updateKitIndent/:indentNo',
		 templateUrl: 'views/master/UnitMaster/CSSD/update-kit-indent.html',
		 controller: "updateKitIndentController",
		 title: 'Kit Indent',
       })
       .state('KitIndentApproval', {
		 url: '/KitIndentApproval',
		 templateUrl: 'views/master/UnitMaster/CSSD/kit-indent-approval.html',
		 controller: "kitIndentApprovalController",
		 title: 'Kit Indent Approval',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 90);
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
	   		 }
	
       })
       .state('KitIndentAcceptance', {
    	   url: '/KitIndentAcceptance',
    	   templateUrl: 'views/master/UnitMaster/CSSD/kit-indent-acceptance.html',
    	   controller: "kitIndentAcceptanceController",
    	   title: 'Kit Issuance Acceptance',
    	   resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 91);
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
	   		 }
	
       })
       .state('detailsKitIndentAcceptance', {
    	   url: '/detailsKitIndentAcceptance/:issueNo',
    	   templateUrl: 'views/master/UnitMaster/CSSD/details-kit-indent-acceptance.html',
    	   controller: "detailsKitIndentAcceptanceController",
    	   title: 'Kit Issue Acceptance',
    	})
    	
    	.state('KitReturn', {
			 url: '/KitReturn',
			 templateUrl: 'views/master/UnitMaster/CSSD/kit-return.html',
			 controller: "kitReturnController",
			 title: 'Kit Return',
			 resolve : {

		   			data: function($q, $state, $timeout) {
		   		        var deferred = $q.defer();
		   		
		   		        $timeout(function() {
		   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
		   				 	.modules.filter(function(item){
		   	            	   return (item.id == 92);
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
		   		 }
		
	       })
	       .state('addNewKitReturn', {
			 url: '/addNewKitReturn',
			 templateUrl: 'views/master/UnitMaster/CSSD/add-new-kit-return.html',
			 controller: "addNewKitReturnController",
			 title: 'Kit Return',
	       })
	       .state('detailsKitReturn', {
			 url: '/detailsKitReturn/:returnNo',
			 templateUrl: 'views/master/UnitMaster/CSSD/details-kit-return.html',
			 controller: "detailsKitReturnController",
			 title: 'Kit Return',
	       })
	       .state('updateKitReturn', {
			 url: '/updateKitReturn/:returnNo',
			 templateUrl: 'views/master/UnitMaster/CSSD/update-kit-return.html',
			 controller: "updateKitReturnController",
			 title: 'Kit Return',
	       })
       .state('kitIssue', {
		 url: '/kitIssue',
		 templateUrl: 'views/master/UnitMaster/CSSD/kit-Issue.html',
		 controller: "kitIssueController",
		 title: 'Kit Issue',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 241);
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
	   		 }
	
       })
       .state('addNewkitIssue', {
		 url: '/addNewkitIssue',
		 templateUrl: 'views/master/UnitMaster/CSSD/add-new-kit-Issue.html',
		 controller: "addNewkitIssueController",
		 title: 'Kit Issue',
       })
       .state('detailskitIssue', {
		 url: '/detailskitIssue/:issueNo',
		 templateUrl: 'views/master/UnitMaster/CSSD/details-kit-Issue.html',
		 controller: "detailskitIssueController",
		 title: 'Kit Issue',
       })
       .state('updateKitIssue', {
		 url: '/updateKitIssue/:issueNo',
		 templateUrl: 'views/master/UnitMaster/CSSD/update-kit-issue.html',
		 controller: "updateKitIssueController",
		 title: 'Kit Issue',
       })
       
         /**
			 * *********************** Global Common
			 * ********************************
			 */
         .state('qualificationMaster', {
  		 url: '/qualificationMaster',
  		 templateUrl: 'views/master/GlobalMaster/qualification-master.html',
  		 controller: "qualificationMasterController",
  		 title: 'Qualification Master',
  		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 496);
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
	   		 }
	
         })
         .state('degreeMaster', {
  		 url: '/degreeMaster',
  		 templateUrl: 'views/master/GlobalMaster/degree-master.html',
  		 controller: "degreeMasterController",
  		 title: 'Degree Master',
	  	 resolve : {
	
	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 497);
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
	   		 }

         })
         .state('reasonMaster', {
  		 url: '/reasonMaster',
  		 templateUrl: 'views/master/GlobalMaster/reason-master.html',
  		 controller: "reasonMasterController",
  		 title: 'Reason Master',
  		resolve : {

   			data: function($q, $state, $timeout) {
   		        var deferred = $q.defer();
   		
   		        $timeout(function() {
   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
   				 	.modules.filter(function(item){
   	            	   return (item.id == 495);
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
   		 }

         })
         .state('employeeCategoryMaster', {
  		 url: '/employeeCategoryMaster',
  		 templateUrl: 'views/master/GlobalMaster/employee-category-master.html',
  		 controller: "employeeCategoryMasterController",
  		title: 'Employee Category Master',
  		resolve : {

   			data: function($q, $state, $timeout) {
   		        var deferred = $q.defer();
   		
   		        $timeout(function() {
   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
   				 	.modules.filter(function(item){
   	            	   return (item.id == 498);
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
   		 }

         })
         .state('doctorCategoryMaster', {
  		 url: '/doctorCategoryMaster',
  		 templateUrl: 'views/master/GlobalMaster/doctor-category-master.html',
  		 controller: "doctorCategoryMasterController",
  		 title: 'Doctor Category Master',
  		resolve : {

   			data: function($q, $state, $timeout) {
   		        var deferred = $q.defer();
   		
   		        $timeout(function() {
   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
   				 	.modules.filter(function(item){
   	            	   return (item.id == 499);
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
   		 }

         })

         
         /** ************** StaffRegistration Master **************** */
         .state('staffRegistration', {
  		 url: '/staffRegistration',
  		 templateUrl : 'views/AdmissionDischarge/staff-master.html',
  		 controller: "staffRegistrationController",
  		 title : 'Staff Registration',
  		resolve : {

   			data: function($q, $state, $timeout) {
   		        var deferred = $q.defer();
   		
   		        $timeout(function() {
   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
   				 	.modules.filter(function(item){
   	            	   return (item.id == 650);
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
   		 }

         })
         .state('addNewStaffRegistration', {
  		 url: '/addNewStaffRegistration',
  		 templateUrl : 'views/AdmissionDischarge/add-new-staff-registration.html',
  		 controller: "MasterController",
  		 title: 'Staff Registration',
         })
       
         /** ************** DoctorRegistration Master **************** */
         .state('doctorRegistration', {
  		 url: '/doctorRegistration',
  		 templateUrl : 'views/AdmissionDischarge/doctor-master.html',
  		 controller: "doctorRegistrationController",
  		 title: 'Doctor Registration',
  		resolve : {

   			data: function($q, $state, $timeout) {
   		        var deferred = $q.defer();
   		
   		        $timeout(function() {
   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
   				 	.modules.filter(function(item){
   	            	   return (item.id == 651);
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
   		 }

         })
         .state('addNewDoctorRegistration', {
  		 url: '/addNewDoctorRegistration',
  		 templateUrl : 'views/AdmissionDischarge/add-new-doctor-registration.html',
  		 controller: "MasterController",
  		 title: 'Doctor Registration',
         })
         /**
			 * ************** Speciman Master added by Suraj
			 * 06.03.18****************
			 */
         .state('specimanMaster', {
  		 url: '/specimanMAster',
  		 templateUrl : 'views/master/LIS/speciman_master.html',
  		 controller: "specimanController",
  		 title: 'Specimen Master',
  		resolve : {

   			data: function($q, $state, $timeout) {
   		        var deferred = $q.defer();
   		
   		        $timeout(function() {
   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
   				 	.modules.filter(function(item){
   	            	   return (item.id == 444);
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
   		 }

         })
         /**
			 * ************** Histopathology Test Master added by Pranita
			 * 08.03.18****************
			 */
         .state('histopathologyTest', {
	  		 url: '/histopathologyTest',
	  		 templateUrl : 'views/master/LIS/histopathology-test.html',
	  		 controller: "histopathologyTestController",
	  		 title: 'Histopathology Test',
	  		resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 559);
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
	   		 }
	
         })
          .state('serologyTestMaster', {
	  		 url: '/serologyTestMaster',
	  		 templateUrl : 'views/master/LIS/serology-test.html',
	  		 controller: "serologyTestMasterController",
	  		 title: 'Serology Test Master',
         })
            .state('cytologyTest', {
	  		 url: '/cytologyTest',
	  		 templateUrl : 'views/master/LIS/cytology-test.html',
	  		 controller: "cytologyTestController",
	  		 title: 'Cytology Test Master',
         })
          .state('specimenType', {
	  		 url: '/specimenType',
	  		 templateUrl : 'views/master/LIS/specimen-type.html',
	  		 controller: "MasterController",
	  		 title: 'Specimen Type',
         })
         /** ************** Store Master **************** */
         .state('pendingReturnablegatepassReceive ', {
  		 url: '/pendingReturnablegatepassReceive',
  		 templateUrl: 'views/PharmacyInventory/Store/returnable-gatepass-receive.html',
  		 controller: "MasterController",
         })
         .state('detailsReturnablegatepassReceivePendingList', {
  		 url: '/detailsReturnablegatepassReceivePendingList',
  		 templateUrl: 'views/PharmacyInventory/Store/details-returnable-gatepass-receive-pending-list.html',
  		 controller: "MasterController"
         })
         .state('detailsReturnablegatepassReceiveIssueList', {
  		 url: '/detailsReturnablegatepassReceiveIssueList',
  		 templateUrl: 'views/PharmacyInventory/Store/details-returnable-gatepass-receive-issue-list.html',
  		 controller: "MasterController"
		 })

	         .state('returnableGatepassRequest',
		       {
		        url : '/returnableGatepassRequest',
		        title: 'Returnable Gatepass Request',
		        views :
		         {
		          '@' :
		           {
		        	  templateUrl: 'views/PharmacyInventory/InPatient/returnable-gatepass-request-pending-list.html',
		        	  controller : 'MasterController',
		           }
		         }
		       })
	       
			.state('returnableGatepassRequest.pendingList',
		     {
		        url : '/pendingList',
		        views :
		         {
			          '@returnableGatepassRequest' :
		           {
			            templateUrl : 'views/PharmacyInventory/InPatient/returnable-gatepass-pending-list.html',
			            controller : 'MasterController',
		           }
			         }
			       })
	       .state('returnableGatepassRequest.list',
	       {
	        url : '/list',
	        views :
	         {
		          '@returnableGatepassRequest' :
	           {
		            templateUrl : 'views/PharmacyInventory/InPatient/returnable-gatepass-list.html',
		            controller : 'MasterController',
	           }
		         }
		       })
		       
		       .state('detailsReturnableGatepassRequest', {
		  		 url: '/detailsReturnableGatepassRequest',
		  		 templateUrl: 'views/PharmacyInventory/InPatient/details-returnable-gatepass-pending-list.html',
		  		 controller: "MasterController",
		  		title: 'Returnable Gatepass Request',
		         })
		         .state('detailsReturnableGatepassRequestList', {
		  		 url: '/detailsReturnableGatepassRequestList',
		  		 templateUrl: 'views/PharmacyInventory/InPatient/details-returnable-gatepass-list.html',
		  		 controller: "MasterController",
		  		 title: 'Returnable Gatepass Request',
		         })
		         
				.state('detailsWasteStore', {
					 url: '/detailsWasteStore',
					 templateUrl: 'views/PharmacyInventory/Store/details-waste-store.html',
					 controller: "MasterController",
					 title: 'Waste Store'
				})
		    // .state('otherCharges', {
			// url: '/otherCharges',
			// templateUrl: 'views/master/GlobalMaster/other-charges.html',
			// controller: "MasterController"
			// })
				.state('fixedScroll', {
					 url: '/fixedScroll',
					 templateUrl: 'views/common/fixed-col-scroll.html',
					 controller: "MasterController",
				})
				.state('fixedtheadtfoot', {
					 url: '/fixedtheadtfoot',
					 templateUrl: 'views/common/fixed-thead-tfoot.html',
					 controller: "MasterController"
				})
				.state('fixedtheadtfoot1', {
					 url: '/fixedtheadtfoot1',
					 templateUrl: 'views/common/fixed-thead-tfoot1.html',
					 controller: "MasterController"
				})
				.state('companyContractMaster', {
					 url: '/companyContractMaster',
					 templateUrl: 'views/master/UnitMaster/billingMaster/company-contract-master.html',
					 controller: "companyContractController",
					 title: 'Company Contract Master',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 608);
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
				   		 }
				
				})
				.state('addNewCompanyContract', {
					 url: '/addNewCompanyContract',
					 templateUrl: 'views/master/UnitMaster/billingMaster/add-new-company-contract.html',
					 controller: "addNewCompanyContractController",
					 title: 'Company Contract Master',
					 params: 
						{
						 companyContractObj: {}
						} 
				})
				.state('detailsCompanyContract', {
					 url: '/detailsCompanyContract',
					 templateUrl: 'views/master/UnitMaster/billingMaster/details-company-contract.html',
					 controller: "detailCompanyContractController",
					 title: 'Company Contract Master',
					 params: 
						{
						 companyContractObj: {}
						} 
				})
			
				/* InPatient patient Consumable Issue */
				.state('weekendMaster', {
					 url: '/weekendMaster',
					 templateUrl: 'views/master/UnitMaster/organizationMaster/weekend-master.html',
                     controller: "weekendMasterController",
                     title: 'Weekend Master',
                     resolve : {

         	   			data: function($q, $state, $timeout) {
         	   		        var deferred = $q.defer();
         	   		
         	   		        $timeout(function() {
         	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
         	   				 	.modules.filter(function(item){
         	   	            	   return (item.id == 697);
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
         	   		 }
         	
				})
				
				.state('doctorSlabMaster', {
					 url: '/doctorSlabMaster',
					 templateUrl: 'views/master/UnitMaster/billingMaster/doctor-slab-master.html',
					 controller: "doctorSlabMasterController",
					 title: 'Doctor Slab Master',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 1020);
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
				   		 }
				
				})
				.state('doctorSlab', {
					 url: '/doctorSlab',
					 templateUrl: 'views/master/DoctorShare/doctor-slab.html',
					 controller: "doctorSlabController",
					 title: 'Doctor Slab',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 1023);
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
				   		 }
				
				})
				.state('globalDoctorShare', {
					 url: '/globalDoctorShare',
					 templateUrl: 'views/master/UnitMaster/billingMaster/doctor-share-master.html',
					 controller: "globalDoctorShareMasterController",
					 title: "Global( Doctor's Share)",
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 613);
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
				   		 }
				
				})
				.state('detailsGlobalDoctorShare', {
					 url: '/detailsGlobalDoctorShare',
					 templateUrl: 'views/master/UnitMaster/billingMaster/details-doctor-share-master.html',
					 controller: "detailsGlobalDoctorShareMasterController",
					 title: 'Global Doctor Share',
					 params: 
						{
						 doctorShareObjResult: {}
						} 
				})
				.state('detailsDoctorShareM', {
					 url: '/detailsDoctorShareM',
					 templateUrl: 'views/master/UnitMaster/billingMaster/details-doctor-share-master-temp.html',
					 controller: "MasterController",
					 title: 'Global Doctor Share',
				})
				.state('addNewGlobalDoctorShare', {
					 url: '/addNewGlobalDoctorShare',
					 templateUrl: 'views/master/UnitMaster/billingMaster/add-new-doctor-share-master.html',
					 controller: "addNewGlobalDoctorShareController",
					 title: 'Global Doctor Share',
				})
				.state('doctorWiseShare', {
					 url: '/doctorWiseShare',
					 templateUrl: 'views/master/UnitMaster/billingMaster/doctorwise-share-master.html',
					 controller: "doctorWiseShareMasterController",
					 title: 'Doctorwise Share',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 614);
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
				   		 }
				
				})
				.state('addNewDoctorWiseShare', {
					 url: '/addNewDoctorWiseShare',
					 templateUrl: 'views/master/UnitMaster/billingMaster/add-new-doctorwise-share-master.html',
					 controller: "addNewDoctorWiseShareController",
					 title: 'Doctorwise Share',
				})
				.state('detailsDoctorWiseShare', {
					 url: '/detailsDoctorWiseShare',
					 templateUrl: 'views/master/UnitMaster/billingMaster/details-doctorwise-share-master.html',
					 controller: "detailsDoctorWiseShareMasterController",
					 title: 'Doctorwise Share',
					 params: 
						{
						 doctorShareObj: {}
						} 
				})
				.state('doctorReferralShareMaster', {
					 url: '/doctorReferralShareMaster',
					 templateUrl: 'views/master/UnitMaster/billingMaster/doctor-referrel-share-master.html',
					 controller: "doctorReferralShareMasterController",
					 title: 'Doctor Referral Share Master',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 615);
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
				   		 }
				
				})
				.state('addNewDoctorReferralShareMaster', {
					 url: '/addNewDoctorReferralShareMaster',
					 templateUrl: 'views/master/UnitMaster/billingMaster/add-new-doctor-referrel-share-master.html',
					 controller: "addNewDoctorReferralShareMasterController",
					 title: 'Doctor Referral Share Master',
				})
				.state('detailsDoctorReferralShareMaster', {
					 url: '/detailsDoctorReferralShareMaster',
					 templateUrl: 'views/master/UnitMaster/billingMaster/details-doctor-referrel-share-master.html',
					 controller: "detailsDoctorReferralShareMasterController",
					 title: 'Doctor Referral Share Master',
						 params: {
					        	doctorReferralObj: {} 
					        } 
				})
				.state('ARmaster', {
					 url: '/ARmaster',
					 templateUrl: 'views/master/UnitMaster/billingMaster/AR-master.html',
					 controller: "aRmasterMasterController",
					 title: 'AR Master',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 616);
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
				   		 }
				
				})
				.state('APmaster', {
					 url: '/APmaster',
					 templateUrl: 'views/master/UnitMaster/billingMaster/AP-master.html',
					 controller: "aPmasterMasterController",
					 title: 'AP Master',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 617);
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
				   		 }
				
				})
				.state('GLcode', {
					 url: '/GLcode',
					 templateUrl: 'views/master/UnitMaster/billingMaster/GL-code.html',
					 controller: "gLcodeMasterController",
					 title: 'GL Code',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 618);
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
				   		 }
				
				})
				
				.state('doctorShareProcess', {
					 url: '/doctorShareProcess',
					 templateUrl: 'views/master/DoctorShare/doctor-share-process.html',
					 controller: "doctorShareProcessController",
					 title: "Doctor's Share Process",
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 23);
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
				   		 }
				
				})
				
				.state('referralDoctorShareProcess', {
					 url: '/referralDoctorShareProcess',
					 templateUrl: 'views/master/DoctorShare/referral-doctor-share-process.html',
					 controller: "referralDoctorShareProcessController",
					 title: "Referral Doctor's Share Process",
					 
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 25);
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
				   		 }
				
				})
				.state('systemMaster', {
					 url: '/systemMaster',
					 templateUrl: 'views/master/GlobalMaster/system-master.html',
					 controller: "systemMasterController",
					 title: 'System Master',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 484);
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
				   		 }
				
				})
				.state('systemObservationMaster', {
					 url: '/systemObservationMaster',
					 templateUrl: 'views/master/GlobalMaster/system-observation-master.html',
					 controller: "systemObservationMasterController",
					 title: 'System Observations Master',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 485);
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
				   		 }
				
				})
				// Added on 11-06-2021 (HIS-16097)
				.state('examinationMaster', {
					 url: '/examinationMaster',
					 templateUrl: 'views/master/GlobalMaster/examination-master.html',
					 controller: "examinationMasterController",
					 title: 'Examination Master',
				})
				
				.state('doctorSharePaymentVoucher', {
					 url: '/doctorSharePaymentVoucher',
					 templateUrl: 'views/master/DoctorShare/doctor-share-payment-voucher.html',
					 controller: "doctorSharePaymentVoucherController",
					 title: "Doctor's Share Payment Voucher",
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 24);
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
				   		 }
				
				})
				
				.state('doctorShareProcessedVoucherDetails', {
					 url: '/doctorShareProcessedVoucherDetails',
					 templateUrl: 'views/master/DoctorShare/doctor-share-processed-voucher-details.html',
					 controller: "doctorShareProcessedVoucherDetailsController",
					 title: 'Doctor Share Processed Voucher Details',
				
				})
				
				.state('doctorReferralPaymentVoucher', {
					 url: '/doctorReferralPaymentVoucher',
					 templateUrl: 'views/master/DoctorShare/doctor-referral-payment-voucher.html',
					 controller: "doctorReferralPaymentVoucherController",
					 title: 'Doctor Referral Payment Voucher',
				})
				
				
				/** ******** added on 9/July/2018 start ********** *//*
																		 * .state('periodicityServiceConfig', {
																		 * url:
																		 * '/periodicityServiceConfig',
																		 * templateUrl:
																		 * 'views/master/UnitMaster/billingMaster/periodicity-Service-Config.html',
																		 * controller:
																		 * "MasterController" })
																		 */
				
				/** ******** added on 9/July/2018 end ********** */
				/** ******** added on 16/July/2018 start ********** */
				.state('IPdefaultServiceConfig', {
					url: '/IPdefaultServiceConfig',
					templateUrl: 'views/master/UnitMaster/billingMaster/IP-default-service-config.html',
					controller: "MasterController",
					title: 'IP Default Service Configuration',
				})
				
				.state('depositMatrixConfig', {
					url: '/depositMatrixConfig',
					templateUrl: 'views/master/UnitMaster/billingMaster/deposit-matrix-config.html',
					controller: "depositMatrixConfigController",
					title: 'Deposit Matrix Configuration',
					resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 621);
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
			   		 }
			
				})
				/** ******** added on 16/July/2018 end ********** */
			/** ***** LINEN SCREENS ******** */
			.state('staffExchangelinenIssue', {
				 url: '/staffExchangelinenIssue',
				 templateUrl: 'views/Linen/staff-Exchange-linen-Issue.html',
				 controller: "MasterController",
				 title: 'Staff Exchange Linen Issue',
				})	
			.state('detailStaffExchangelinenIssue', {// pending
				 url: '/detailStaffExchangelinenIssue',
				 templateUrl: 'views/Linen/details-staff-Exchange-linen-Issue.html',
				 controller: "MasterController",
				 title: 'Staff Exchange Linen Issue',
				})	
			.state('detailsIssueListStaffExchange', {
				 url: '/detailsIssueListStaffExchange',
				 templateUrl: 'views/Linen/details-issuelist-staff-Exchange-linen-Issue.html',
				 controller: "MasterController",
				 title: 'Staff Exchange Linen Issue',
				})	
			.state('washingRequest', {
				 url: '/washingRequest',
				 templateUrl: 'views/Linen/washing-request.html',
				 controller: "MasterController",
				 title: 'Washing Request',
				})	
			.state('addNewWashingRequest', {
				 url: '/addNewWashingRequest',
				 templateUrl: 'views/Linen/add-new-washing-request.html',
				 controller: "MasterController",
				 title: 'Washing Request',
				})	
			.state('detailsWashingRequest', {
				 url: '/detailsWashingRequest',
				 templateUrl: 'views/Linen/details-washing-request.html',
				 controller: "MasterController",
				 title: 'Washing Request',
				})	
			.state('acceptanceWashingRequest', {
				 url: '/acceptanceWashingRequest',
				 templateUrl: 'views/Linen/acceptance-of-washing-request.html',
				 controller: "MasterController",
				 title: 'Acceptance Washing Request',
				})	
			.state('detailsAcceptanceWashingRequest', {
				 url: '/detailsAcceptanceWashingRequest',
				 templateUrl: 'views/Linen/details-acceptance-of-washing-request.html',
				 controller: "MasterController",
				 title: 'Acceptance Washing Request',
				})	
			.state('issueWashedLinen', {
				 url: '/issueWashedLinen',
				 templateUrl: 'views/Linen/issue-washed-linen.html',
				 controller: "MasterController",
				 title: 'Issuance Washed Linen',
			})	
			.state('detailsPendingWashedLinen', {
				 url: '/detailsPendingWashedLinen',
				 templateUrl: 'views/Linen/details-pending-issue-washed-linen.html',
				 controller: "MasterController",
				 title: 'Issue Washed Linen',
			})	
			.state('detailsIssueListWashedLinen', {
				 url: '/detailsIssueListWashedLinen',
				 templateUrl: 'views/Linen/details-issue-list-washed-linen.html',
				 controller: "MasterController",
				 title: 'Issue Washed Linen',
			})	
			.state('directIssueWashedLinen', {
				 url: '/directIssueWashedLinen',
				 templateUrl: 'views/Linen/direct-issue-washed-linen.html',
				 controller: "MasterController",
				 title: 'Issue Washed Linen',
			})

				.state('linenIssueDirect', {
					 url: '/linenIssueDirect',
					 templateUrl: 'views/Linen/linen-issue-direct.html',
					 controller: "MasterController",
					 title: 'Direct Issue',
				})

				
				.state('linenIssueListDetails', {
					 url: '/linenIssueListDetails',
					 templateUrl: 'views/Linen/linen-issue-list-details.html',
					 controller: "MasterController",
					 title: 'Linen Issue',
				})
				
		
				.state('linenReturn', {
					 url: '/linenReturn',
					 templateUrl: 'views/Linen/linen-return.html',
					 controller: "MasterController",
					 title: 'Linen Return',
				})
				
				.state('addNewLinenReturn', {
					 url: '/addNewLinenReturn',
					 templateUrl: 'views/Linen/add-new-linen-return.html',
					 controller: "MasterController",
					 title: 'Linen Return',
				})
				
				
				.state('linenReturnDetails', {
					 url: '/linenReturnDetails',
					 templateUrl: 'views/Linen/linen-return-details.html',
					 controller: "MasterController",
					 title: 'Linen Return',
				})
				
				.state('linenReturnAcceptance', {
					 url: '/linenReturnAcceptance',
					 templateUrl: 'views/Linen/linen-return-acceptance.html',
					 controller: "MasterController",
					 title: 'Linen Return Acceptance',
				})
				
				.state('linenReturnAcceptanceDetails', {
					 url: '/linenReturnAcceptanceDetails',
					 templateUrl: 'views/Linen/linen-return-acceptance-details.html',
					 controller: "MasterController",
					 title: 'Linen Return Acceptance',
				})
				
				.state('rejectedLinenAcceptance', {
					 url: '/rejectedLinenAcceptance',
					 templateUrl: 'views/Linen/rejected-linen-acceptance.html',
					 controller: "MasterController",
					 title: 'Rejected Linen Acceptance',
				})
				
				.state('rejectedLinenAcceptanceDetails', {
					 url: '/rejectedLinenAcceptanceDetails',
					 templateUrl: 'views/Linen/rejected-linen-acceptance-details.html',
					 controller: "MasterController",
					 title: 'Rejected Linen Acceptance',
				})
				/*
				 * .state('companyQueryResponseMaster', { url:
				 * '/companyQueryResponseMaster', templateUrl:
				 * 'views/master/UnitMaster/billingMaster/company-query-response-master.html',
				 * controller: "MasterController" })
				 */
				.state('detailsCompanyQueryResponseMaster', {
					 url: '/detailsCompanyQueryResponseMaster',
					 templateUrl: 'views/master/UnitMaster/billingMaster/details-company-query-response-master.html',
					 controller: "MasterController",
					 title: 'Company Query & Response Master',
				})
				// .state('applicationConfiguration', {
				// url: '/applicationConfiguration',
				// templateUrl:
				// 'views/master/UnitMaster/configuration/application-master.html',
				// controller: "MasterController"
				// })
				
				.state('sendToCondemnation', {
					 url: '/sendToCondemnation',
					 templateUrl: 'views/Linen/send-to-condemnate-page.html',
					 controller: "MasterController",
					 title: 'Send To Condemnate',
				})
				
				.state('addNewSendToCondemnation', {
					 url: '/addNewSendToCondemnation',
					 templateUrl: 'views/Linen/add-new-condmnet.html',
					 controller: "MasterController",
					 title: 'Send To Condemnate',
				})
				
				.state('sendToCondemnationDetails', {
					 url: '/sendToCondemnationDetails',
					 templateUrl: 'views/Linen/send-to-condemnation-details.html',
					 controller: "MasterController",
					 title: 'Send To Condemnate',
				})
				
				
				.state('acceptanceOfCondemnation ', {
					 url: '/acceptanceOfCondemnation',
					 templateUrl: 'views/Linen/acceptance-of-condemnation.html',
					 controller: "MasterController",
					 title: 'Acceptance of Condemnation',
				})
				
				.state('acceptanceOfCondemnationDetails', {
					 url: '/acceptanceOfCondemnationDetails',
					 templateUrl: 'views/Linen/acceptance-of-condemnation-details.html',
					 controller: "MasterController"
				})
				
				.state('convertOfCondemnationStock', {
					 url: '/convertOfCondemnationStock',
					 templateUrl: 'views/Linen/convert-of-condemnation-stock.html',
					 controller: "MasterController",
					 title: 'Convert of Condemnation Stock',
						 
				})
				
				.state('convertOfCondemnationStockDetails', {
					 url: '/convertOfCondemnationStockDetails',
					 templateUrl: 'views/Linen/convert-of-condemnation-stock-details.html',
					 controller: "MasterController",
					 title: 'Acceptance of Condemnation',
				})
				
				.state('issueToStaff', {
					 url: '/issueToStaff',
					 templateUrl: 'views/Linen/issue-to-staff.html',
					 controller: "MasterController",
					 title: 'Issuance To Staff',
				})
				
				.state('staffExchangeLinen', {
					 url: '/staffExchangeLinen',
					 templateUrl: 'views/Linen/staff-exchange-linen.html',
					 controller: "MasterController",
					 title: 'Staff Exchange Linen',
				})
				
				.state('addNewStaffExchange', {
					 url: '/addNewStaffExchange',
					 templateUrl: 'views/Linen/add-new-staff-exchange.html',
					 controller: "MasterController",
					 title: 'Staff Exchange Linen',
				})
				
				.state('staffExchangeDetails', {
					 url: '/staffExchangeDetails',
					 templateUrl: 'views/Linen/staff-exchange-detials.html',
					 controller: "MasterController",
					 title: 'Staff Exchange Linen',
				})

				.state('sendtoMicrobiology', {
					 url: '/sendtoMicrobiology',
					 templateUrl: 'views/master/UnitMaster/CSSD/send-to-microbiology.html',
					 controller: "sendtoMicrobiologyController",
					 title: 'Send to Microbiology',
						resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 240);
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
				   		 }
				})
				.state('detailsSendtoMicrobiology', {
					url: '/detailsSendtoMicrobiology/:batchNo',
					templateUrl: 'views/master/UnitMaster/CSSD/details-send-to-microbiology.html',
					controller: "detailsSendtoMicrobiologyController",
					title: 'Send to Microbiology',
				})
				.state('application', {
					url: '/application',
					templateUrl: 'views/master/UnitMaster/CSSD/application.html',
					controller: "MasterController",
					title: 'Application',
				})
				.state('detailsApplication', {
					 url: '/detailsApplication',
					 templateUrl: 'views/master/UnitMaster/CSSD/details-application.html',
					 controller: "MasterController",
					 title: 'Application',
				})
				.state('detailsApplicationKit', {
					url: '/detailsApplicationKit',
					templateUrl: 'views/master/UnitMaster/CSSD/details-application-kit.html',
					controller: "MasterController",
					title: 'Application',
				})

				
				.state('issueDirtyLinen', {
					 url: '/issueDirtyLinen',
					 templateUrl: 'views/Linen/issue-dirty-linen.html',
					 controller: "MasterController",
					 title: 'Issuance Dirty Linen',
				})
				
				.state('addNewDirtyLinenIssue', {
					 url: '/addNewDirtyLinenIssue',
					 templateUrl: 'views/Linen/add-new-dirty-linen-issue.html',
					 controller: "MasterController",
					 title: 'Issue Dirty Linen',
				})
				
				.state('dirtyLinenIssueDetails', {
					 url: '/dirtyLinenIssueDetails',
					 templateUrl: 'views/Linen/dirty-linen-issue-details.html',
					 controller: "MasterController",
					 title: 'Issue Dirty Linen',
				})
				
				.state('acceptanceOfDirtyLinen', {
					 url: '/acceptanceOfDirtyLinen',
					 templateUrl: 'views/Linen/acceptance-of-dirty-linen.html',
					 controller: "MasterController",
					 title:'Dirty Linen Acceptance',
				})
				
				.state('acceptanceOfDirtyLinenDetails', {
					 url: '/acceptanceOfDirtyLinenDetails',
					 templateUrl: 'views/Linen/dirty-linen-issue-acceptance-details.html',
					 controller: "MasterController",
					 title: 'Dirty Linen Acceptance',
				})
				.state('preAuthorizationRequest', {
					 url: '/preAuthorizationRequest',
					 templateUrl: 'views/master/UnitMaster/billingMaster/pre-authorization-request.html',
					 controller: "preAuthorizationRequestController",
					 title: 'Pre Authorization Request',
				})
				.state('addNewPreAuthorizationRequest', {
					 url: '/addNewPreAuthorizationRequest',
					 templateUrl: 'views/master/UnitMaster/billingMaster/add-new-pre-authorizationRequest.html',
					 controller: "MasterController",
					 title: 'Pre Authorization Request',
				})
				.state('surgeryGradeMaster', {
					 url: '/surgeryGradeMaster',
					 templateUrl: 'views/master/GlobalMaster/surgery-grade-master.html',
					 controller: "surgeryGradeMasterController",
					 title: 'Surgery Grade Master',
				})
				.state('surgeryMaster', {
					 url: '/surgeryMaster',
					 templateUrl: 'views/master/GlobalMaster/surgery-master.html',
					 controller: "surgeryMasterController",
					 title: 'Surgery Master',
						resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 487);
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
				   		 }
				})
				.state('kitCreation', {
					 url: '/kitCreation',
					 templateUrl: 'views/master/UnitMaster/CSSD/kit-creation.html',
					 controller: "kitCreationController",
					 title: 'kit Creation',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 555);
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
				   		 }
				
				})
				
				/** * Pre-Sterilization Process(new) ** */
				.state('preSterilizationCleaning', {
					 url: '/preSterilizationCleaning',
					 templateUrl: 'views/master/UnitMaster/CSSD/pre-sterilization-cleaning.html',
					 controller: "preSterilizationCleaningController",
					 title: 'Cleaning Worklist',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 233);
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
				   		 }
				
				})
				.state('addNewPreSterilizationCleaning', {
					 url: '/addNewPreSterilizationCleaning',
					 templateUrl: 'views/master/UnitMaster/CSSD/add-new-pre-sterilization-cleaning.html',
					 controller: "addNewPreSterilizationCleaningController",
					 title: 'Pre Sterilization Cleaning',
				})
				.state('showPreSterilizationCleaning', {
					 url: '/showPreSterilizationCleaning/:kitId',
					 templateUrl: 'views/master/UnitMaster/CSSD/view-pre-sterilization-cleaning.html',
					 controller: "viewPreSterilizationCleaningController",
					 title: 'Pre Sterilization Cleaning',
				})
				.state('viewPendingList', {
					 url: '/viewPendingList',
					 templateUrl: 'views/master/UnitMaster/CSSD/view-pending-list.html',
					 controller: "MasterController",
					 title: 'Pre Sterilization Cleaning',
				})
				.state('viewCleanedList', {
					 url: '/viewCleanedList/:transitionNumber',
					 templateUrl: 'views/master/UnitMaster/CSSD/view-Cleaned-List.html',
					 controller: "viewPreSterilizationCleaningController" 
				})
				.state('viewPendingCleaningList', {
					 url: '/viewPendingCleaningList/:transitionNumber',
					 templateUrl: 'views/master/UnitMaster/CSSD/view-Pending-Cleaning-List.html',
					 controller: "viewPreSterilizationPendingCleaningController"
				})
				.state('preSterilizationInspection', {
					 url: '/preSterilizationInspection',
					 templateUrl: 'views/master/UnitMaster/CSSD/pre-sterilization-inspection.html',
					 controller: "preSterilizationInspectionController",
					 title: 'Pending for Inspection',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 234);
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
				   		 }
				
				})
				.state('showPreSterilizationInspection', {
					 url: '/showPreSterilizationInspection/:transitionNumber',
					 templateUrl: 'views/master/UnitMaster/CSSD/view-pre-sterilization-inspection.html',
					 controller: "viewPreSterilizationInspectionController",
					 title: 'Pre Sterilization Inspection',
				})
				.state('preSterilizationDisInfection', {
					 url: '/preSterilizationDisInfection',
					 templateUrl: 'views/master/UnitMaster/CSSD/pre-sterilization-disinfection.html',
					 controller: "preSterilizationDisInfectionController",
					 title: 'Pending for Dis-Infection',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 235);
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
				   		 }
				
				})
				.state('showPreSterilizationDisInfection', {
					 url: '/showPreSterilizationDisInfection/:transitionNumber',
					 templateUrl: 'views/master/UnitMaster/CSSD/view-pre-sterilization-disinfection.html',
					 controller: "viewPreSterilizationDisInfectionController",
					 title: 'Pending for Dis-Infection',
				})
				.state('preSterilizationKitFormation', {
					 url: '/preSterilizationKitFormation',
					 templateUrl: 'views/master/UnitMaster/CSSD/pre-sterilization-kit-formation.html',
					 controller: "preSterilizationKitFormationController",
					 title: 'Pending for Kit Formation',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 236);
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
				   		 }
				
				})
				.state('showPreSterilizationKitFormation', {
					 url: '/showPreSterilizationKitFormation/:transitionNumber',
					 templateUrl: 'views/master/UnitMaster/CSSD/view-pre-sterilization-kit-formation.html',
					 controller: "viewPreSterilizationKitFormationController",
					 title: 'Pending for Kit Formation',
				})
				/** * Sterilization Process NEW ** */
				.state('sterilizationLoadBatch', {
					 url: '/sterilizationLoadBatch',
					 templateUrl: 'views/master/UnitMaster/CSSD/sterilization-load-batch.html',
					 controller: "sterilizationLoadBatchController",
					 title: 'Sterilization Batch Load',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 238);
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
				   		 }
				
				})
				.state('addNewSterilizationLoadBatch', {
					 url: '/addNewSterilizationLoadBatch',
					 templateUrl: 'views/master/UnitMaster/CSSD/add-new-sterilization-load-batch.html',
					 controller: "addNewSterilizationLoadBatchController",
					 title: 'Sterilization Batch Load',
				})
				.state('showSterilizationLoadBatch', {
					 url: '/showSterilizationLoadBatch/:batchNo',
					 templateUrl: 'views/master/UnitMaster/CSSD/view-sterilization-load-batch.html',
					 controller: "viewSterilizationLoadBatchController",
					 title: 'Sterilization Batch Load',
				})
				.state('sterilizationPendingIndicators', {
					 url: '/sterilizationPendingIndicators',
					 templateUrl: 'views/master/UnitMaster/CSSD/sterilization-pending-indicators.html',
					 controller: "sterilizationPendingIndicatorsController",
					 title: 'Pending for Indicators',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 239);
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
				   		 }
				
				})
				.state('addNewSterilizationPendingIndicators', {
					 url: '/addNewSterilizationPendingIndicators',
					 templateUrl: 'views/master/UnitMaster/CSSD/add-new-sterilization-pending-indicators.html',
					 controller: "MasterController",
					 title: 'Pending for Indicators',
				})
				.state('showSterilizationPendingIndicators', {
					 url: '/showSterilizationPendingIndicators/:batchNo',
					 templateUrl: 'views/master/UnitMaster/CSSD/view-sterilization-pending-indicators.html',
					 controller: "viewSterilizationPendingIndicatorsController",
					 title: 'Pending for Indicators',
				})
				 .state('newOPDBilling', {
					 url: '/newOPDBilling',
					 templateUrl: 'views/master/UnitMaster/billingMaster/new-opd-billing.html',
					 controller: "newOPDBillingController",
					 title: 'OPD Billing',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 11);
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
				
					 params:{
						 newOpdBillObject :{}
					 }
				 })
				 .state('designationMaster', {
					 url: '/designationMaster',
					 templateUrl: 'views/master/GlobalMaster/designation-master.html',
					 controller: "designationMasterController",
					 title: 'Designation Master',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 500);
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
				   		 }
				
				 })
				// .state('pharmacyStrengthUnitMaster', {
				// url: '/pharmacyStrengthUnitMaster',
				// templateUrl:
				// 'views/PharmacyInventory/pharmacy-strength-unit-master.html',
				// controller: "MasterController"
				// })
				 
				 /* Employee Dependent Contract START */
				 .state('employeeDependentContractMaster', {
					 url: '/employeeDependentContractMaster',
					 templateUrl: 'views/master/UnitMaster/billingMaster/employee-dependent-contract-master.html',
					 controller: "MasterController",
					 title: 'Employee Dependent Contract Master',
				})
				.state('addNewEmployeeDependentContractMaster', {
					 url: '/addNewEmployeeDependentContractMaster',
					 templateUrl: 'views/master/UnitMaster/billingMaster/add-new-employee-dependent-contract-master.html',
					 controller: "MasterController",
					 title: 'Employee Dependent Contract Master',
				})
				/* Employee Dependent Contract END */
				
				/*
				 * .state('licenseTypeMaster', { url: '/licenseTypeMaster',
				 * templateUrl:
				 * 'views/master/GlobalMaster/license-type-master.html',
				 * controller: "licenseTypeMasterController", title: 'License
				 * Type Master', resolve : {
				 * 
				 * data: function($q, $state, $timeout) { var deferred =
				 * $q.defer();
				 * 
				 * $timeout(function() { var arr =
				 * JSON.parse(localStorage.getItem('permissions'))
				 * .modules.filter(function(item){ return (item.id == 379); });
				 * if(arr.length <= 0){ $state.go("accessDenied")
				 * deferred.reject(); } else { deferred.resolve(); } });
				 * 
				 * return deferred.promise; } } })
				 */
				
				.state('refundAgainstDeposit', {
					 url: '/refundAgainstDeposit',
					 templateUrl: 'views/master/UnitMaster/billingMaster/refund-against-deposit.html',
					 controller: "refundController",
					 title: 'Refund Against Deposit',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 19);
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
				   		 }
				
				})
				.state('refundList', {
					 url: '/refundList',
					 templateUrl: 'views/master/UnitMaster/billingMaster/refund-list.html',
					 controller: "refundListController",
					 title: 'Refund List',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 20);
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
				   		 }
				
				})
				.state('refundAgainstCR', {
					 url: '/refundAgainstCR',
					 templateUrl: 'views/master/UnitMaster/billingMaster/refund-against-CR.html',
					 controller: "creditNoteRefundController",
					 title: 'Refund Against CR',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 21);
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
				   		 }
				
				})
				.state('indentTemplateMaster', {
					 url: '/indentTemplateMaster',
					 templateUrl: 'views/master/UnitMaster/assetManagement/indent-template-master.html',
					 controller: "MasterController",
					 title: 'Indent Template Master',
				})
				.state('addNewIndentTemplateMaster', {
					 url: '/addNewIndentTemplateMaster',
					 templateUrl: 'views/master/UnitMaster/assetManagement/add-new-indent-template-master.html',
					 controller: "MasterController",
					 title: 'Indent Template Master',
				})
				.state('ETOItemConfiguration', {
					 url: '/ETOItemConfiguration',
					 templateUrl: 'views/master/UnitMaster/assetManagement/ETO-item-configuration.html',
					 controller: "MasterController",
					 title: 'ETO Item Configuration',
				})

				.state('addNewETOItemConfiguration', {
					 url: '/addNewETOItemConfiguration',
					 templateUrl: 'views/master/UnitMaster/assetManagement/add-new-ETO-item-configuration.html',
					 controller: "MasterController",
					 title: 'ETO Item Configuration',
				})
				
				
				/** ******* Added on 20/Dec/2018 end ********* */
				.state('IncubationMaster', {
				    url: '/IncubationMaster',
				    templateUrl: 'views/LIS/Microbiology/incubation-master.html',
				    controller: "incubationObjservationController",
				    title: 'Incubation Observations',
				    resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 310);
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
			   		 }
			
				})
				.state('detailsIncubationMaster', {
				    url: '/detailsIncubationMaster',
				    templateUrl: 'views/LIS/Microbiology/details-incubation-master.html',
				    controller: "incubationDetailsController",
				    title: 'Incubation Observations',
				    params: 
			  			{
				    	    incuObsrvationObj: {} // default value
			  			} 
				})
				.state('macroscopicExaminationMaster', {
				    url: '/macroscopicExaminationMaster',
				    templateUrl: 'views/LIS/Microbiology/macroscopic-examination-master.html',
				    controller: "macroScopicExaminationController",
				    title: 'Macroscopic Examination Master',
				    resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 311);
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
			   		 }
			
				})

			
				/* Radiology Workbench */
				.state('radiologyWorkbench', {
				 url: '/radiologyWorkbench',
				 templateUrl: 'views/master/RadiologyWorkbench/radiology-workbench.html',
				 controller: "radiologyWorkbenchController",
				 title: 'Radiology Workbench',
				 resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
                                    return (item.id == 829||item.id == 270);
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
			   		 }
			
				})
				.state('hemodialysisWorkbench', {
				 url: '/hemodialysisWorkbench',
				 templateUrl: 'views/master/RadiologyWorkbench/nephrology-workbench.html',
				 controller: "nephrologyWorkbenchController",
				 title: 'Hemodialysis Workbench',
					resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 870);
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
			   		 }
				})
				
				/**
				 * ******** Unit - User management module pages added on
				 * 3-July-2018 start *********
				 */
				.state('userRoleManagement', {
					url: '/userRoleManagement',
					templateUrl: 'views/master/UnitMaster/userManagement/user-role-management.html',
					controller: "userRoleManagementMasterController",
					title: 'User Role Management',
					resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 674);
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
			   		 }
			
				})
				.state('roleCreation', {
					url: '/roleCreation',
					templateUrl: 'views/master/UnitMaster/userManagement/role-creation.html',
					controller: "roleCreationMasterController",
					title: 'Role Creation',
					resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 672);
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
			   		 }
			
				})
				.state('roleManagement', {
					url: '/roleManagement',
					templateUrl: 'views/master/UnitMaster/userManagement/role-management.html',
					controller: "roleManagementMasterController",
					title: 'Role Management',
					resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 675);
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
			   		 }
			
				})
				/**
				 * ******** Unit - User management module pages added on
				 * 3-July-2018 end *********
				 */
				
				/** ***************** Blood bank Start ************************* */
				.state('bloodGroupMaster', {
					 url: '/bloodGroupMaster',
					 templateUrl: 'views/master/UnitMaster/bloodBank/blood-group-master.html',
					 controller: "bloodGroupMasterController",
					 title: 'Blood Group Master',
				})
				.state('bloodBagType', {
					 url: '/bloodBagType',
					 templateUrl: 'views/master/UnitMaster/bloodBank/blood-bag-type-master.html',
					 controller: "bloodBagTypeMasterController",
					 title: 'Blood Bag Type Master',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 731);
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
				   		 }
				
				})
				.state('bloodDonortype', {
					 url: '/bloodDonortype',
					 templateUrl: 'views/master/UnitMaster/bloodBank/blood-donor-type-master.html', 
					 controller: "bloodDonorTypeMasterController",
					 title: 'Blood Donor Type Master',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 732);
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
				   		 }
				
				})
				.state('bloodTestResult', {
					 url: '/bloodTestResult',
					 templateUrl: 'views/master/UnitMaster/bloodBank/blood-test-result-master.html', 
					 controller: "bloodTestResultMasterController",
					 title: 'TTD Blood Test Result Master',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 733);
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
				   		 }
				
				})
				.state('bloodTest', { // new name - TTD Blood Test Master
					 url: '/bloodTest',
					 templateUrl: 'views/master/UnitMaster/bloodBank/blood-test-master.html', 
					 controller: "bloodTestMasterController",
					 title: 'Blood Test Master',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 734);
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
				   		 }
				
				})
				.state('bloodBagCapacity', {
					 url: '/bloodBagCapacity',
					 templateUrl: 'views/master/UnitMaster/bloodBank/blood-bag-capacity.html', 
					 controller: "bloodBagCapacityMasterController",
					 title: 'Blood Bag Capacity Master',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 735);
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
				   		 }
				
				})
				
				.state('reasonOfDiscard', {
					 url: '/reasonOfDiscard',
					 templateUrl: 'views/master/UnitMaster/bloodBank/reason-of-discard-blood-master.html', 
					 controller: "reasonOfDiscardMasterController",
					 title: 'Reason of Discard',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 737);
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
				   		 }
				
				})
				
				.state('screeningTest', {    // new name - Screening Test
												// Master
					 url: '/screeningTest',
					 templateUrl: 'views/master/UnitMaster/bloodBank/screening-test-blood-bank.html', 
					 controller: "screeningTestMasterController",
					 title: 'Screening Test Master',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 738);
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
				   		 }
				
				})
				
				.state('donorRejectionMaster', {
					 url: '/donorRejectionMaster',
					 templateUrl: 'views/master/UnitMaster/bloodBank/donor-rejection-master.html', 
					 controller: "donorRejectionMasterController",
					 title: 'Donor Rejection Master',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 740);
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
				   		 }
				
				})
				
				.state('bloodBankMaster', {
					 url: '/bloodBankMaster',
					 templateUrl: 'views/master/UnitMaster/bloodBank/blood-bank-master.html', 
					 controller: "bloodBankMasterController",
					 title: 'Blood Bank Master',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 742);
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
				   		 }
				
				})
				
				.state('bloodBankTypeComponent', {
					 url: '/bloodBankTypeComponent',
					 templateUrl: 'views/master/UnitMaster/bloodBank/blood-bank-type-component-master.html', 
					 controller: "bloodBankTypeComponentMasterController",
					 title: 'Blood Bank Type Component Master',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 739);
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
				   		 }
				
				})
				
				.state('bloodGroupMatchingMaster', {
					 url: '/bloodGroupMatchingMaster',
					 templateUrl: 'views/master/UnitMaster/bloodBank/blood-group-matching-master.html', 
					 controller: "bloodGroupMatchingMasterController",
					 title: 'Blood Group Matching Master',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 741);
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
				   		 }
				
				})
				
				.state('bloodComponentMaster', {
					 url: '/bloodComponentMaster',
					 templateUrl: 'views/master/UnitMaster/bloodBank/blood-component-blood-bank.html', 
					 controller: "bloodComponentMasterController",
					 title: 'Blood Component',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 736);
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
				   		 }
				
				})
				
				
				.state('createTabularDataTemplates', {
					 url: '/createTabularDataTemplates',
					 templateUrl: 'views/template/create-tabular-data-templates.html', 
					 controller: "createTabularDataTemplatesController",
					 title: 'Tabular Data Template',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 736);
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
				   		 }
				
				})
				
				.state('createTabularTemplates', {
					 url: '/createTabularTemplates',
					 templateUrl: 'views/template/create-tabular-templates.html', 
					 controller: "createTabularTemplatesController",
					 title: 'Tabular Template',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 736);
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
				   		 }
				
				})
				/** ***************** Blood bank End ************************* */
				
				/** * Global Common Inspection Master ** */
				.state('instructionMaster', {
					 url: '/instructionMaster',
					 templateUrl: 'views/master/GlobalMaster/instruction-master.html',
					 controller: "inspectionMasterController",
					 title: 'Instruction Master',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 501);
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
				   		 }
				
				 })
				 .state('doseMaster', {
					 url: '/doseMaster',
					 templateUrl: 'views/master/GlobalMaster/dose-master.html',
					 controller: "doseMasterController",
					 title: 'Dose Master',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 502);
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
				   		 }
				
				 })
				 /** * physiotherapy Workbench Master ** */
				 .state('physiotherapyWorkbench', {
				 url: '/physiotherapyWorkbench',
				 templateUrl: 'views/master/PhysiotherapyWorkbench/physiotherapy-workbench.html',
				 controller: "physiotherapyWorkbenchController",
				 title: 'Physiotherapy Workbench',
				 resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 944);
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
			   		 }
			
				})
				/** * allergy Master ** */
				.state('allergyTypeMaster', {
					 url: '/allergyTypeMaster',
					 templateUrl: 'views/master/GlobalMaster/allergy-type-master.html',
					 controller: "allergyTypeMasterController",
					 title: 'Allergy Type Master',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 488);
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
				   		 }
				
				})
				.state('allergyMaster', {
					 url: '/allergyMaster',
					 templateUrl : 'views/master/GlobalMaster/allergy-master.html',
					 controller: "allergyMasterController",
					 title: 'Allergy Master',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 489);
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
				   		 }
				
				})
				/** * Asset Issue ** */
					
				/**
				 * ** Site Of Administration added on #Date - 10/Oct/2018 -                      
				 * start***
				 */
				.state('siteOfAdministration', {
					 url: '/siteOfAdministration',
					 templateUrl : 'views/master/GlobalMaster/site-of-administration.html',
					 controller: "siteOfAdministartionController",
					 title: 'Site of Administration',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 753);
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
				   		 }
				
				})
				
				/**
				 * * Site Of Administration added on #Date - 10/Oct/2018 -
				 * start**
				 */
				
				/** * Asset Return added on #Date - 25/July/2018 - start** */
		
				
			
				// .state('assetIssueIndent', {
				// url: '/assetIssueIndent',
				// templateUrl:
				// 'views/master/UnitMaster/assetManagement/asset-issue.html',
				// controller: "MasterController",
				// title: 'Asset Issue',
				// resolve : {

				// data: function($q, $state, $timeout) {
				// var deferred = $q.defer();
				   		
				// $timeout(function() {
				// var arr = JSON.parse(localStorage.getItem('permissions'))
				// .modules.filter(function(item){
				// return (item.id == 207);
				// });
				// if(arr.length <= 0){
				// $state.go("accessDenied")
				// deferred.reject();
				// } else {
				// deferred.resolve();
				// }
				// });
				   		
				// return deferred.promise;
				// }
				// }
				
				// })
				// .state('directIsuueAssetIssue', {
				// url: '/directIsuueAssetIssue',
				// templateUrl:
				// 'views/master/UnitMaster/assetManagement/direct-issue-asset-issue-indent.html',
				// controller: "MasterController",
				// title: 'Asset Issue',
				// })
			
				
				
			/*
			 * .state('rejectedAssetAcceptance', { url:
			 * '/rejectedAssetAcceptance', templateUrl:
			 * 'views/master/UnitMaster/assetManagement/rejected-asset-acceptance.html',
			 * controller: "MasterController" })
			 */
			/*
			 * .state('detailsRejectedAssetAcceptance', { url:
			 * '/detailsRejectedAssetAcceptance', templateUrl:
			 * 'views/master/UnitMaster/assetManagement/details-rejected-asset-acceptance.html',
			 * controller: "MasterController" })
			 */
				
				/** * Asset Return added on #Date - 25/July/2018 - end ** */
				
				/** * NonReturnableGatePass ** */
				
				/** * NonReturnableGatePass ** */
				/*
				 * .state('OTCompletion', { url: '/OTCompletion', templateUrl:
				 * 'views/master/TheatreManagement/OT-completion.html',
				 * controller: "MasterController" })
				 */
				.state('detailsOTCompletion', {
					url: '/detailsOTCompletion',
					templateUrl: 'views/master/TheatreManagement/details-OT-completion.html',
					controller: "MasterController",
					title: 'OT Completion',
				})
				.state('surgeryNote', {
					url: '/surgeryNote',
					templateUrl: 'views/master/TheatreManagement/surgery-note.html',
					controller: "surgeryNoteMasterController",
					title: 'Surgery Note',
					resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 223 || item.id ==  224);
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
			   		 }
			
				})
				.state('anesthesiaNote', {
					url: '/anesthesiaNote',
					templateUrl: 'views/master/TheatreManagement/anesthesia-note.html',
					controller: "anesthesiaNoteMasterController",
					title: 'Anesthesia Note',
					resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 325);
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
			   		 }
			
				})
			/*
			 * .state('implantIndent', { url: '/implantIndent', templateUrl:
			 * 'views/master/TheatreManagement/implant-indent.html', controller:
			 * "MasterController" })
			 */
		/*
		 * .state('addNewImplantIndent', { url: '/addNewImplantIndent',
		 * templateUrl:
		 * 'views/master/TheatreManagement/add-new-implant-indent.html',
		 * controller: "MasterController" })
		 */
			/*
			 * .state('detailsImplantIndent', { url: '/detailsImplantIndent',
			 * templateUrl:
			 * 'views/master/TheatreManagement/details-implant-indent.html',
			 * controller: "MasterController" })
			 * 
			 * .state('implantIndentAcceptance', { url:
			 * '/implantIndentAcceptance', templateUrl:
			 * 'views/master/TheatreManagement/implant-indent -acceptance.html',
			 * controller: "MasterController" })
			 * .state('detailsImplantIndentAcceptance', { url:
			 * '/detailsImplantIndentAcceptance', templateUrl:
			 * 'views/master/TheatreManagement/details-implant-indent
			 * -acceptance.html', controller: "MasterController" })
			 */
				/*
				 * .state('progressNote', { url: '/progressNote', templateUrl:
				 * 'views/clinicalWorkStation/progress-note.html', controller:
				 * "progressNoteController", title: 'Progress Note', resolve : {
				 * 
				 * data: function($q, $state, $timeout) { var deferred =
				 * $q.defer();
				 * 
				 * $timeout(function() { var arr =
				 * JSON.parse(localStorage.getItem('permissions'))
				 * .modules.filter(function(item){ return (item.id == 269); });
				 * if(arr.length <= 0){ $state.go("accessDenied")
				 * deferred.reject(); } else { deferred.resolve(); } });
				 * 
				 * return deferred.promise; } } })
				 */
				.state('IPDCoverSheet', {
					url: '/IPDCoverSheet',
					templateUrl: 'views/clinicalWorkStation/ipd-CoverSheet.html',
					controller: "IPDCoverSheetController",
					title: 'IPD Doctor Cover Sheet',
					params: {
						  patientDetailsObj: {} // default value
			            }
				})
				
				/*
				 * *********** Vaccination master screens total number 2 : Added
				 * 9/oct/2018 start ************
				 */
				
				
				
				.state('vaccinationMAster', {
					url: '/vaccinationMAster',
					templateUrl: 'views/clinicalWorkStation/vaccination-master.html',
					controller: "vaccinationMasterController",
					title: 'Vaccination Master',
				})
				.state('vaccinationSchedule', {
					url: '/vaccinationSchedule',
					templateUrl: 'views/clinicalWorkStation/vaccination-schedule.html',
					controller: "MasterController",
					title: 'Vaccination Schedule',
				})
				
				/*
				 * *********** Vaccination master screens total number 2 : Added
				 * 9/oct/2018 end ************
				 */
				
					/** * bedCategoryMapper ** */
				.state('bedCategoryMapper', {
					 url: '/bedCategoryMapper',
					 templateUrl: 'views/master/UnitMaster/organizationMaster/bed-category-mapper.html',
					 controller: "MasterController"
				})
				
				.state('inputOutputCharts', {
					url: '/inputOutputCharts',
					templateUrl: 'views/Nursing/input-output-charts.html',
					controller: "inputOutputChartsController",
					title: 'Intake Fluid Output(ml) Charts',
					resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 99);
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
			   		 }
			
				})
				.state('inputOutputChartHistory', {
					url: '/inputOutputChartHistory',
					templateUrl: 'views/Nursing/input-output-chart-history.html',
					controller: "inputOutputChartHistoryController",
					title: 'Intake Fluid Output(ml)Charting History',
					resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 100);
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
			   		 }
			
				})
				.state('subsequentSurgeryConfiguration', {
					url: '/subsequentSurgeryConfiguration',
					templateUrl: 'views/master/TheatreManagement/subsequent-surgery-configuration.html',
					controller: "subsequentSurgeryConfigurationController",
					title: 'Subsequent Surgery Configuration',
					resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 516);
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
			   		 }
			
				})
				.state('resourceConfiguration', {
					url: '/resourceConfiguration',
					templateUrl: 'views/master/TheatreManagement/resource-configuration.html',
					controller: "resourceConfigurationMasterController",
					title: 'Resource configuration',
					resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 517);
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
			   		 }
			
				})
				.state('userAuthentication', {
					url: '/userAuthentication',
					templateUrl: 'views/master/UnitMaster/userManagement/user-authentication.html',
					controller: "userAuthenticationMasterController",
					title: 'User Authentication',
					resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 676);
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
			   		 }
			
				})
				.state('accessControl', {
					url: '/accessControl',
					templateUrl: 'views/common/access-control.html',
					controller: "accessControlController",
					title: 'Access Control',
				})

				.state('newAccessControl', {
					url: '/newAccessControl',
					templateUrl: 'views/common/new-access-control.html',
					controller: "newAccessControlController",
					title: 'Access Control',
					resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 677);
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
			   		 }
			
				})
				.state('IPConsultationConfiguration', {
					url: '/IPConsultationConfiguration',
					templateUrl: 'views/master/UnitMaster/billingMaster/ip-consultation-configuration.html',
					controller: "IPConsultationConfigurationMasterController",
					title: 'IP Consultation Configuration',
					resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 684);
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
			   		 }
			
				})
				.state('userItemBilling', {
				   url: '/userItemBilling',
				   templateUrl: 'views/procurement/consignment/user-item-billing.html',
				   controller: "MasterController",
				   title: 'User Item Billing'  /*
												 * Patient Consignment Issue -
												 * old name -changeOn 09-06-2021
												 */
				 })

				/*
				 * .state('pendingForPOCreation', { url:
				 * '/pendingForPOCreation', templateUrl:
				 * 'views/procurement/consignment/pending-for-po-creation.html',
				 * controller: "MasterController" })
				 */
				 
				 /** ***** Access Control ******** */
				.state('accessControl1', {
					 url:'/accessControl1',
					 templateUrl: 'views/access-control.html',
					 controller: 'accessControlController',
					 title: 'Role Permissions',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 673);
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
				   		 }
				
				})
				.state('accessDenied', {
					 url:'/accessDenied',
					 templateUrl: 'views/access-denied.html',
					 title: 'Access Denied',
				})
				 .state('sterilizationRequest', {
					 url: '/sterilizationRequest',
					 templateUrl: 'views/master/UnitMaster/CSSD/sterilization-request.html',
					 controller: "sterilizationRequestController",
					 title: 'Sterilization Request',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 683);
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
				   		 }
				
			       })
			       .state('sterilizationDirectIssue', {
					 url: '/sterilizationDirectIssue',
					 templateUrl: 'views/master/UnitMaster/CSSD/sterilization-direct-issue.html',
					 controller: "sterilizationDirectIssueController",
					 title: 'Direct Request',
			       })
			       .state('updateSterilizationRequest', {
			        url: '/updateSterilizationRequest/:requestNo',
			        templateUrl: 'views/master/UnitMaster/CSSD/update-sterilization-request.html',
			        controller: "updateSterilizationRequestController",
			        title: 'Direct Request',
			       })
			       .state('detailsSterilizationRequest', {
			        url: '/detailsSterilizationRequest/:requestNo',
			        templateUrl: 'views/master/UnitMaster/CSSD/details-sterilization-request.html',
			        controller: "detailsSterilizationRequestController",
			        title: 'Direct Request',
			       })
			       .state('sterilizationRequestAcceptance', {
					 url: '/sterilizationRequestAcceptance',
					 templateUrl: 'views/master/UnitMaster/CSSD/sterilization-request-acceptance.html',
					 controller: "sterilizationRequestAcceptanceController",
					 title: 'Sterilization Request Acceptance',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 682);
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
				   		 }
				
			       })
			       .state('detailsSterilizationRequestAcceptance', {
					 url: '/detailsSterilizationRequestAcceptance/:requestNo',
					 templateUrl: 'views/master/UnitMaster/CSSD/details-sterilization-request-acceptance.html',
					 controller: "detailsSterilizationRequestAcceptanceController",
					 title: 'Sterilization Request Acceptance',
			       })
			       .state('newAdmissionRequest', {
					 url: '/newAdmissionRequest',
					 templateUrl: 'views/Admission/new-admission-request.html',
					 controller: "MasterController",
					 title: 'New Admission Request',
					})
					
					.state('dischargeSummaryTemplate', {
					 url: '/dischargeSummaryTemplate',
					 templateUrl: 'views/master/UnitMaster/organizationMaster/discharge-summary-template.html',
					 controller: "dischargeSummaryTemplateMasterController",
					 title: 'Hospital Discharge Summary Template',
					})


					
					.state('RMOAllocationMaster', {
					 url: '/RMOAllocationMaster',
					 templateUrl: 'views/master/UnitMaster/organizationMaster/RMO-allocation-master.html',
					 controller: "rmoAllocationMasterController",
					 title: 'RMO Allocation Master',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 824);
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
				   		 }
				
					})

					
					.state('dischargeSummaryFieldMaster', {
					 url: '/dischargeSummaryFieldMaster',
					 templateUrl: 'views/master/UnitMaster/organizationMaster/discharge-summary-field-master.html',
					 controller: "dischargeSummaryFieldMasterController",
					 title: 'Hospital Discharge Summary Field Master',
					})

                    .state('dischargeFieldMapper', {
                      url: '/dischargeFieldMapper',
                       templateUrl: 'views/master/UnitMaster/organizationMaster/discharge-field-mapper.html',
                        controller: "dischargeFieldMapperController",
                        title: 'Hospital Discharge Field Mapper',
                        })


					.state('stockRegister', {
					  url: '/stockRegister',
					  templateUrl: 'views/PharmacyInventory/Store/stock-register.html',
					  controller: "MasterController",
					  title: 'Stock Register',
					  resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 695);
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
				   		 }
				
					 })
					 .state('admission', {
						 url: '/admission',
						 templateUrl: 'views/bedManagement/admission.html',
						 controller: "admissionController",
						 title: 'Admission',
						 resolve : {

					   			data: function($q, $state, $timeout) {
					   		        var deferred = $q.defer();
					   		
					   		        $timeout(function() {
					   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
					   				 	.modules.filter(function(item){
					   	            	   return (item.id == 711);
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
					
						 params: {
				        	 admissionObj: {},
				         }
					})
					.state('acceptanceIssueWashedLinen', {
					 url: '/acceptanceIssueWashedLinen',
					 templateUrl: 'views/Linen/acceptance-issue-washed-linen.html',
					 controller: "MasterController",
					 title: 'Washed Linen Acceptance',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 700);
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
				   		 }
				
					})
					.state('detailsAcceptanceissueWashedLinen', {
					 url: '/detailsAcceptanceissueWashedLinen',
					 templateUrl: 'views/Linen/details-acceptance-issue-washed-linen.html',
					 controller: "MasterController",
					 title: 'Washed Linen Acceptance',
					})
					.state('staffLinenAllocation', {
					 url: '/staffLinenAllocation',
					 templateUrl: 'views/Linen/staff-linen-allocation.html',
					 controller: "MasterController",
					 title: 'Staff Linen Allocation',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 681);
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
				   		 }
				
					})
					.state('formInputs', {
					 url: '/formInputs',
					 templateUrl: 'views/common/form-inputs.html',
					 controller: "MasterController",
					})
					
					.state('formF', {
					 url: '/formF',
					 templateUrl: 'views/common/formF.html',
					 controller: "formFController",
					 params: 
			 	    	{
			 	    		itemObj: {} // default value
			 	    	}	
					})
					
					.state('formFSelfId', {
					 url: '/formFSelfId',
					 templateUrl: 'views/common/formFSelfId.html',
					 controller: "formFSelfIdController",
					 title: 'Form F Self Id',
					 params: 
			 	    	{
			 	    		itemObj: {} // default value
			 	    	}	
					})
					
					
					.state('templateICDCodeMapping', {
					 url: '/templateICDCodeMapping',
					 templateUrl: 'views/template/template-ICD-code-mapping.html',
					 controller: "templateICDCodeMappingMasterController",
					 title: 'Clinical Form ICD Code Mapping',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 688);
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
				   		 }
				
					})
					.state('userNursingStationMapper', {
					 url: '/userNursingStationMapper',
					 templateUrl: 'views/master/UnitMaster/userManagement/user-nursing-station-mapper.html',
					 controller: "userNursingStationMapperController",
					 title: 'User Nursing Station Mapper',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 689);
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
				   		 }
				
					})
					.state('userStoreMapper', {
					 url: '/userStoreMapper',
					 templateUrl: 'views/master/UnitMaster/userManagement/user-store-mapper.html',
					 controller: "userStoreMapperController",
					 title: 'User Store Mapper',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 690);
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
				   		 }
				
					})
					
					.state('departmentNursingStationMapper', {
					 url: '/departmentNursingStationMapper',
					 templateUrl: 'views/master/UnitMaster/userManagement/department-nursing-station-mapper.html',
					 controller: "departmentNursingStationMapperController",
					 title: 'Department Nursing Station Mapper',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 746);
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
				   		 }
				
					})
					
					.state('vitals', {
					 url: '/vitals',
					 templateUrl: 'views/Nursing/vitals.html',
					 controller: "vitalsController",
					 title: 'Vitals',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 691);
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
				   		 }
				
					})
					.state('addNewVitals', {
					 url: '/addNewVitals',
					 templateUrl: 'views/Nursing/add-new-vitals.html',
					 controller: "VitalAddController",
					 title: 'Vitals',
					 params: 
			 	    	{
			 	    		patientObj: {} // default value
			 	    	}	
					})
					.state('serviceRendering', {
				    url: '/serviceRendering',
				    templateUrl: 'views/Nursing/service-rendering.html',
				    controller: "serviceRendering",
				    title: 'Service Rendering',
				  })
				 .state('administrationWorklist', {
				    url: '/administrationWorklist',
				    templateUrl: 'views/Nursing/administration-worklist.html',
				    controller: "administrationWorklistMasterController",
				    title: 'Administration Worklist',
				    resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 777);
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
			   		 }
			
				  })
				  
				  .state('nursingProgressNote', {
				    url: '/nursingProgressNote',
				    templateUrl: 'views/Nursing/nursing-progress-note.html',
				    controller: "MasterController",
				    title: 'Nursing Progress Note',
				  })
				  .state('editViewPreAuthorizationRequest', {
					 url: '/editViewPreAuthorizationRequest',
					 templateUrl: 'views/master/UnitMaster/billingMaster/edit-view-pre-authorization-request.html',
					 controller: "editViewPreAuthorizationRequestController",
					 title: 'Pre Authorization Request',
				})
				.state('detailsPreAuthorizationRequest', {
					 url: '/detailsPreAuthorizationRequest',
					 templateUrl: 'views/master/UnitMaster/billingMaster/details-pre-authorization-request.html',
					 controller: "MasterController",
					 title: 'Pre Authorization Request',
				})
				.state('splitBill', {
					 url: '/splitBill',
					 templateUrl: 'views/master/UnitMaster/billingMaster/split-bill.html',
					 controller: "MasterController",
					 title: 'split Bill',
				})
				.state('growthChartMaster', {
					 url: '/growthChartMaster',
					 templateUrl: 'views/master/UnitMaster/organizationMaster/growth-chart-master.html',
					 controller: "growthChartMasterController",
					 title: 'Growth Chart Master',
				})
				.state('growthChart', {
					 url: '/growthChart',
					 templateUrl: 'views/Nursing/growth-chart.html',
					 controller: "growthChartController",
					 title: 'Growth Chart',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 744);
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
				   		 }
				
				})
				.state('selfRegistrationdemo', {
					 url: '/selfRegistrationdemo',
					 templateUrl: 'views/OPD/self-registrationdemo.html',
					 controller: "selfRegistrationController",
				})
				.state('selfRegistrationhome', {
					 url: '/selfRegistrationhome',
					 templateUrl: 'views/OPD/self-registrationhome.html',
					 controller: "selfRegistrationController",
					 params: {
			        	 selfReg: {},
			         }
				})
				.state('selfRegistration', {
					 url: '/selfRegistration',
					 templateUrl: 'views/OPD/self-registration.html',
					 controller: "selfRegistrationController",
			         params: {
			        	 selfReg: {},
			         }
				})
				.state('counterWorklist', {
					 url: '/counterWorklist',
					 templateUrl: 'views/master/GlobalMaster/Encounter/counter-worklist.html',
					 controller: "counterWorklistController",
					 title: 'Counter Worklist',
				})
				.state('nursingWorklist', {
					 url: '/nursingWorklist',
					 templateUrl: 'views/master/GlobalMaster/Encounter/nursing-worklist.html',
					 controller: "MasterController",
					 title: 'Nursing Worklist',
				})
				.state('stimulationCalendar', {
					 url: '/stimulationCalendar',
					 templateUrl: 'views/IVF/stimulation-calendar.html',
					 controller: "MasterController",
					 title: 'Stimulation Calendar',
				})
				/**
				 * ********************** Mapping master added on 21/sep/2018 by
				 * Chetna start ************************************
				 */
				.state('doctorCabinMapper', {
					 url: '/doctorCabinMapper',
					 templateUrl: 'views/master/UnitMaster/userManagement/doctor-cabin-mapper.html',
					 controller: "doctorCabinMapperController",
					 title: 'Doctor Cabin Mapper',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 714);
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
				   		 }
				
					})
				.state('userCounterMapper', {
					 url: '/userCounterMapper',
					 templateUrl: 'views/master/UnitMaster/userManagement/user-counter-mapper.html',
					 controller: "userCounterMapperController",
					 title: 'User Counter Mapper',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 715);
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
				   		 }
				
					})
				/**
				 * ********************** Mapping master added on 21/sep/2018 by
				 * Chetna end ************************************
				 */	
					
				.state('costingParameter', {
				 url: '/costingParameter',
				 templateUrl: 'views/master/LIS/costing-parameter.html',
				 controller: "MasterController",
				 title: 'Costing Parameter',
			})
			.state('costingSheet', {
				 url: '/costingSheet',
				 templateUrl: 'views/master/LIS/costing-sheet.html',
				 controller: "MasterController",
				 title: 'Costing Sheet',
			})
			.state('doctorDesk', {
				 url: '/doctorDesk',
				 templateUrl: 'views/doctorDesk/doctor-desk.html',
				 controller: "doctorDeskController",
				 title: 'Doctor Desk',
				 resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 758);
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
			   		 }
			
			})
			/*
			 * .state('casualtyWorklist', { url: '/casualtyWorklist',
			 * templateUrl: 'views/doctorDesk/casualty-worklist.html',
			 * controller: "casualtyWorklistController", title:
			 * 'casualtyWorklist', resolve : {
			 * 
			 * data: function($q, $state, $timeout) { var deferred = $q.defer();
			 * 
			 * $timeout(function() { var arr =
			 * JSON.parse(localStorage.getItem('permissions'))
			 * .modules.filter(function(item){ return (item.id == 757); });
			 * if(arr.length <= 0){ $state.go("accessDenied") deferred.reject(); }
			 * else { deferred.resolve(); } });
			 * 
			 * return deferred.promise; } } })
			 */
			/**
			 * ***************** Blood bank -Donor Start
			 * *************************
			 */
			.state('detailsLoanOutBloodBank', {
				 url: '/detailsLoanOutBloodBank',
				 templateUrl: 'views/master/bloodBankDonor/details-loan-out-approval-blood-bank-donor.html',
				 controller: "detailsLoanOutBloodBankController",
				 title: 'Loan Out Approval',
				 params : {
					 loanOutDetailObj : {}
				 }
			})
			.state('detailsLoanInBloodBank', {
				 url: '/detailsLoanInBloodBank',
				 templateUrl: 'views/master/bloodBankDonor/details-loan-in-approval-blood-bank-donor.html',
				 controller: "detailsLoanInApprovalBloodBankController",
				 title: 'Loan In Approval',
				 params: 
					{
					 loanInDetailObj: {}
					} 
			})
			.state('loanOutBloodBankApproval', {
				 url: '/loanOutBloodBankApproval',
				 templateUrl: 'views/master/bloodBankDonor/loan-out-approval-blood-bank-donor.html',
				 controller: "loanOutBloodBankApprovalController",
				 title: 'Loan Out Approval',
			})
			.state('loanInBloodBankApproval', {
				 url: '/loanInBloodBankApproval',
				 templateUrl: 'views/master/bloodBankDonor/loan-in-approval-blood-bank-donor.html',
				 controller: "loanInBloodBankApprovalController",
				 title: 'Loan In Approval',
			})
			.state('loanInBloodBank', {
				 url: '/loanInBloodBank',
				 templateUrl: 'views/master/bloodBankDonor/loan-in-blood-bank-donor.html',
				 controller: "addNewLoanInBloodBankController",
				 title: 'Loan In',
			})
			.state('addNewLoanInBloodBank', {
				 url: '/addNewLoanInBloodBank',
				 templateUrl: 'views/master/bloodBankDonor/add-new-loan-in-blood-bank-donor.html',
				 controller: "addNewLoanInBloodBankController",
				 title: 'Loan In',
			})
			.state('loanOutBloodBank', {
				 url: '/loanOutBloodBank',
				 templateUrl: 'views/master/bloodBankDonor/loan-out-blood-bank-donor.html',
				 controller: "loanOutBloodBankController",
				 title: 'Loan Out',
			})
			.state('addNewLoanOutBloodBank', {
				 url: '/addNewLoanOutBloodBank',
				 templateUrl: 'views/master/bloodBankDonor/add-new-loan-out-blood-bank-donor.html',
				 controller: "addNewLoanOutBloodBankController"
			})
			
			.state('patientRequisitionWorklist', {
				 url: '/patientRequisitionWorklist',
				 templateUrl: 'views/master/bloodBankDonor/patient-requisition-worklist.html',
				 controller: "patientRequisitionWorklistController",
				 title: 'Patient Requisition Worklist',
				 resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 727);
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
			   		 }
			
			})
			.state('detailsPatientRequisition', {
				 url: '/detailsPatientRequisition',
				 templateUrl: 'views/master/bloodBankDonor/patient-requisition.html',
				 controller: "detailsPatientRequisitionController",
				 title: 'Patient Requisition Worklist',
				 params: 
					{
					 patientRequisitionDetailObj: {}
					} 
			})
			
			.state('bloodBagStockUpdation', {
				 url: '/bloodBagStockUpdation',
				 templateUrl: 'views/master/bloodBankDonor/blood-bag-stock-updation.html',
				 controller: "bloodBagStockUpdationController",
				 title: 'Blood Bag Stock Updation',
			})
			.state('donorRegistration', {
				 url: '/donorRegistration',
				 templateUrl: 'views/master/bloodBankDonor/donor-registration.html',
				 controller: "donorRegistrationController",
				 title: 'Donor Registration',
				 resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 719);
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
			   		 }
			
			})
			.state('donorScreeningWorklist', {
				 url: '/donorScreeningWorklist',
				 templateUrl: 'views/master/bloodBankDonor/donor-screening-worklist.html',
				 controller: "donorScreeningWorklistController",
				 title: 'Donor Screening Worklist',
				 resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 720);
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
			   		 }
			
				
			})
			.state('donorScreening', {
				 url: '/donorScreening',
				 templateUrl: 'views/master/bloodBankDonor/donor-screening.html',
				 controller: "donorScreeningWorklistDetailsController",
				 title: 'Donor Screening',
				 params: 
					{
					 donorObjResult: {}
					} 
			})
			.state('bloodBagCollectionWorklist', {
				 url: '/bloodBagCollectionWorklist',
				 templateUrl: 'views/master/bloodBankDonor/blood-bag-collection-donor-worklist.html',
				 controller: "bloodBagCollectionWorklistController",
				 title: 'Blood Collection Worklist',
				 resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 721);
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
			   		 }
			
			})
			.state('bloodBagCollection', {
				 url: '/bloodBagCollection',
				 templateUrl: 'views/master/bloodBankDonor/blood-bag-collection-donor.html',
				 controller: "bloodBagCollectionController",
				 title: 'Blood Bag Collection',
				 params: 
					{
					 bloodBagCollectionResult: {}
					} 
			})
			
			.state('transfusionTransmittedDiseases', {
				 url: '/transfusionTransmittedDiseases',
				 templateUrl: 'views/master/bloodBankDonor/transfusion-transmitted-diseases.html',
				 controller: "transfusionTransmittedDiseasesController",
				 title: 'Transfusion Transmitted Diseases',
				 resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 722);
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
			   		 }
			
				 
			})
			.state('redCellGroupingWorklist', {
				 url: '/redCellGroupingWorklist',
				 templateUrl: 'views/master/bloodBankDonor/red-cell-grouping-worklist.html',
				 controller: "redCellGroupingWorklistController",
				 title: 'Red Cell Grouping Worklist',
			})
			.state('redCellSerumWorklist', {
				 url: '/redCellSerumWorklist',
				 templateUrl: 'views/master/bloodBankDonor/red-cell-serum-worklist.html',
				 controller: "redCellSerumWorklistController",
				 title: 'Red Cell Serum Worklist',
			})
			.state('bloodBankComponentSeparationWorklist', {
				 url: '/bloodBankComponentSeparationWorklist',
				 templateUrl: 'views/master/bloodBankDonor/blood-bank-component-separation-worklist.html',
				 controller: "bloodBankComponentSeparationWorklistController",
				 title: 'Blood Bank Component Separation Worklist',
			})
			/**
			 * ***************** Blood bank - Donor End
			 * *************************
			 */
			/**
			 * ***************** Billing New Screen START
			 * ************************
			 */
			.state('discountRequestWorklist', {
					 url: '/discountRequestWorklist',
					 templateUrl: 'views/master/UnitMaster/billingMaster/discount-request-worklist.html',
					 controller: "discountRequestWorklistController",
					 title: 'Discount Request Worklist',
				})
				.state('financialRequestApproval', {
					 url: '/financialRequestApproval',
					 templateUrl: 'views/master/UnitMaster/billingMaster/discount-request-approval.html',
					 controller: "discountRequestApprovalController",
					 title: 'Financial Request Approval',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 749);
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
				   		 }
				
				})
				.state('discountRights', {
					 url: '/discountRights',
					 templateUrl: 'views/master/UnitMaster/billingMaster/discount-rights.html',
					 controller: "discountRightsMasterController",
					 title: 'Financial Rights',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 745);
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
				   		 }
				
				})
			/** ***************** Billing New Screen END ************************ */
			/**
			 * ***************** Details Blood bank - Donor START
			 * ************************
			 */
			.state('detailsTransfusionTransmittedDiseases', {
				 url: '/detailsTransfusionTransmittedDiseases',
				 templateUrl: 'views/master/bloodBankDonor/details-transfusion-transmitted-diseases.html',
				 controller: "detailsTransfusionTransmittedDiseasesController",
				 title: 'Transfusion Transmitted Diseases',
				 params: 
					{
					 bloodBagObjResult: {}
					} 
				})
			.state('detailsRedCellGroupingWorklist', {
				 url: '/detailsRedCellGroupingWorklist',
				 templateUrl: 'views/master/bloodBankDonor/details-red-cell-grouping-worklist.html',
				 title: 'Red Cell Grouping Worklist',
				 controller: "detailsRedCellGroupingWorklistController",
				 params : {
					 redCellGroupObj : {}
				 }
				})
			.state('detailsRedCellSerumWorklist', {
				 url: '/detailsRedCellSerumWorklist',
				 templateUrl: 'views/master/bloodBankDonor/details-red-cell-serum-worklist.html',
				 title: 'Red Cell Serum Worklist',
				 controller: "detailsRedCellSerumWorklistController",
				 params : {
					 redCellSerumObj : {}
				 }
			})
			/**
			 * ***************** Details Blood bank - Donor END
			 * ************************
			 */
				/*
				 * .state('transactionLog', { url: '/transactionLog',
				 * templateUrl:
				 * 'views/master/GlobalMaster/transaction-log.html', controller:
				 * "MasterController" })
				 */
				/**
				 * ***************** Blood bank - Donor End
				 * *************************
				 */
				/** ***************** Card Creation End ************************* */
				.state('loyaltyCardCreation', {
					 url: '/loyaltyCardCreation',
					 templateUrl: 'views/master/GlobalMaster/loyalty-card-creation.html',
					 controller: "loyaltyCardCreationMasterController",
					 title: 'loyalty Card',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 729);
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
				   		 }
				
				})
				.state('loyaltyCardConfiguration', {
					 url: '/loyaltyCardConfiguration',
					 templateUrl: 'views/master/GlobalMaster/loyalty-card-configuration.html',
					 controller: "loyaltyCardConfigurationMasterController",
					 title: 'Loyalty Card Configuration',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 743);
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
				   		 }
				
				})
				.state('assignloyaltyCard', {
					 url: '/assignloyaltyCard',
					 templateUrl: 'views/master/GlobalMaster/assign-loyalty-card.html',
					 controller: "patientLoyaltiCardMapperController",
					 title: 'Assign Loyalty Card',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 752);
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
				   		 }
				
				})
				/** ***************** Card Creation End ************************* */
				/** ***************** Blood Bank START ************************* */
				.state('detailsBloodBankComponentSeparationWorklist', {
				 url: '/detailsBloodBankComponentSeparationWorklist',
				 templateUrl: 'views/master/bloodBankDonor/details-blood-bank-component-separation-worklist.html',
				 controller: "detailsComponentSeparationWorklistController",
				 title: 'Blood Bank Component Separation Worklist',
				 params : {
					 componentSeparationObj : {}
				 }
				})
				.state('crossMatchRegister', {	
				 url: '/crossMatchRegister',
				 templateUrl: 'views/master/bloodBankDonor/cross-match-register.html',
				 controller: "crossMatchRegisterController",
				 title: 'Cross Match Register',
				})
				.state('detailsCrossMatchRegister', {
				 url: '/detailsCrossMatchRegister',
				 templateUrl: 'views/master/bloodBankDonor/details-cross-match-register.html',
				 controller: "detailsCrossMatchRegisterController",
				 title: 'Cross Match Register',
				 params: 
					{
					 detailObj: {}
					} 
				})
				.state('bloodBagReservation', {
				 url: '/bloodBagReservation',
				 templateUrl: 'views/master/bloodBankDonor/bloodBagReservation.html',
				 controller: "bloodBagReservationController",
				 title: 'Blood Bag Reservation',
				})
				.state('issueBloodBag', {
				 url: '/issueBloodBag',
				 templateUrl: 'views/master/bloodBankDonor/issue-bloodBag.html',
				 controller: "issueBloodBagController",
				 title: 'Issuance Blood Bag',
				})
				.state('detailsIssueBloodBag', {
				 url: '/detailsIssueBloodBag',
				 templateUrl: 'views/master/bloodBankDonor/details-issue-bloodBag.html',
				 controller: "detailsIssueBloodBagController",
				 title: 'Issue Blood Bag',
				 params: 
					{
					 bloodComponentObj: {}
					} 
				 
				})
				.state('tokenDisplay', {
					 url: '/tokenDisplay',
					 templateUrl: 'views/Nursing/token-display.html',
					 controller: "tokenMasterController",
					 title: '',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 747);
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
				   		 }
				
				})
				.state('nursingDisplay', {
					 url: '/nursingDisplay/:orgId/:unitId/:nursingStationId',
					 templateUrl: 'views/Nursing/nursing-assesment.html',
					 controller: "nursingAssesmentDisplayController",
					 title: 'nursingDisplay',
					 resolve : {

						 orgId: function($stateParams, GenericService)
				            {
				                return $stateParams.orgId;
				            },
				            unitId: function($stateParams, GenericService)
				            {
				                return $stateParams.unitId;
				            },
				            nursingStationId: function($stateParams, GenericService)
				            {
				                return $stateParams.nursingStationId;
				            }
				            
				   		 }
				
				})
				
				.state('triageDisplay', {
					 url: '/triageDisplay/:orgId/:unitId/:nursingStationId',
					 templateUrl: 'views/Nursing/triage-display.html',
					 controller: "triageDisplayController",
					 title: 'Triage Display',
					 resolve : {

						 orgId: function($stateParams, GenericService)
				            {
				                return $stateParams.orgId;
				            },
				            unitId: function($stateParams, GenericService)
				            {
				                return $stateParams.unitId;
				            },
				            nursingStationId: function($stateParams, GenericService)
				            {
				                return $stateParams.nursingStationId;
				            }
				            
				   		 }
				
				})
				
				.state('cabinDisplay', {
					 url: '/cabinDisplay/:orgId/:unitId/:doctorId',
					 templateUrl: 'views/Nursing/cabin-display.html',
					 controller: "cabinDisplayController",
					 title: 'cabinDisplay',
					 resolve : {

						 orgId: function($stateParams, GenericService)
				            {
				                return $stateParams.orgId;
				            },
				            unitId: function($stateParams, GenericService)
				            {
				                return $stateParams.unitId;
				            },
				            doctorId: function($stateParams, GenericService)
				            {
				                return $stateParams.doctorId;
				            }
				   		 }
				
				})
				
				
			/*	.state('consultantDisplay', {
					 url: '/cabinDisplay/:orgId/:unitId/:cabinId',
					 templateUrl: 'views/Nursing/consultant-display.html',
					 controller: "consultantDisplayController",
					 title: 'Consultant Display',
					 resolve : {

						 orgId: function($stateParams, GenericService)
				            {
				                return $stateParams.orgId;
				            },
				            unitId: function($stateParams, GenericService)
				            {
				                return $stateParams.unitId;
				            },
				            cabinId: function($stateParams, GenericService)
				            {
				                return $stateParams.cabinId;
				            }
				   		 }
				
				})
				*/
				/* nursing-assesment.html */
				.state('bloodGroupConfirmation', {
					 url: '/bloodGroupConfirmation',
					 templateUrl: 'views/master/bloodBankDonor/blood-group-confirmation.html',
					 controller: "bloodGroupConfirmationController",
					 title: 'Blood Group Confirmation',
				})
				.state('detailsBloodGroupConfirmation', {
					 url: '/detailsBloodGroupConfirmation',
					 templateUrl: 'views/master/bloodBankDonor/details-blood-group-confirmation.html',
					 controller: "detailsBloodGroupConfirmationController",
					 title: 'Blood Group Confirmation',
					 params : {
						bloodConfirmationObj : {}
					}
				})
				/** ***************** Blood Bank END ************************* */
				/** ***************** IVF START ************************* */
				.state('coupleRegistration', {
					 url: '/coupleRegistration',
					 templateUrl: 'views/IVF/couple-registration.html',
					 controller: "MasterController",
					 title: 'Couple Registration',  /*
													 * Blood Group Confirmation
													 * on 09-06-2021
													 */
				})
				.state('addNewCoupleRegistration', {
					 url: '/addNewCoupleRegistration',
					 templateUrl: 'views/IVF/add-new-couple-registration.html',
					 controller: "MasterController",
					 title: 'Couple Registration',
				})
				.state('femaleMedicalHistory', {
					 url: '/femaleMedicalHistory',
					 templateUrl: 'views/IVF/female-medical-history.html',
					 controller: "MasterController",
					 title: 'Female Medical History',
				})
				.state('maleMedicalHistory', {
					 url: '/maleMedicalHistory',
					 templateUrl: 'views/IVF/male-medical-history.html',
					 controller: "MasterController",
					 title: 'Male Medical History',
				})
				/** ***************** IVF END ************************* */

				/**
				 * ***************** Consignment New Screens
				 * *************************
				 */
			 	/*
				 * .state('consignmentPOCreation', { url:
				 * '/consignmentPOCreation', templateUrl:
				 * 'views/procurement/consignment/consignment-po-creation.html',
				 * controller: "MasterController" })
				 */
				/*
				 * .state('detailsConsignmentPOCreation', { url:
				 * '/detailsConsignmentPOCreation', templateUrl:
				 * 'views/procurement/consignment/consignment-details-PO-Creation.html',
				 * controller: "MasterController" })
				 */
				/*
				 * .state('consignmentPOApproval', { url:
				 * '/consignmentPOApproval', templateUrl:
				 * 'views/procurement/consignment/consignment-po-approval.html',
				 * controller: "MasterController" })
				 */
				/*
				 * .state('detailsConsignmentPOApproval', { url:
				 * '/detailsConsignmentPOApproval', templateUrl:
				 * 'views/procurement/consignment/details-consignment-po-approval.html',
				 * controller: "MasterController" })
				 */
				 .state('consignmentManagerDashboard', {
				   url: '/consignmentManagerDashboard',
				   templateUrl: 'views/procurement/consignment/consignment-manager-dashboard.html',
				   controller: "MasterController",
				   title: 'Consignment Manager Dashboard'
				 })
				 .state('detailsConsignmentManagerDashboard', {
				   url: '/detailsConsignmentManagerDashboard',
				   templateUrl: 'views/procurement/consignment/details-consignment-manager-dashboard.html',
				   controller: "MasterController",
				   title: 'Consignment Manager Dashboard'
				 })
				 /**
					 * ************************Cash Opening
					 * Closing**************************
					 */
				 .state('addNewCashOpeningClosingBalance', {
					 url: '/addNewCashOpeningClosingBalance',
					 templateUrl: 'views/master/UnitMaster/billingMaster/add-new-cash-opening-closing-balance.html',
					 controller: "MasterController",
					 title: 'Cash Opening Balance',
				})
				.state('detailsCashOpeningClosingBalance', {
					 url: '/detailsCashOpeningClosingBalance',
					 templateUrl: 'views/master/UnitMaster/billingMaster/details-cash-opening-closing-balance.html',
					 controller: "MasterController",
					 title: 'Cash Opening Balance',
				})
				/**
				 * ***************** Consignment New Screens
				 * *************************
				 */
			/*
			 * .state('patientConsignmentIssue', { url:
			 * '/patientConsignmentIssue', templateUrl:
			 * 'views/procurement/consignment/patient-consignment-issue.html',
			 * controller: "MasterController" })
			 */
			/*
			 * .state('detailsPatientConsignmentIssue', { url:
			 * '/detailsPatientConsignmentIssue', templateUrl:
			 * 'views/procurement/consignment/details-patient-consignment-issue.html',
			 * controller: "MasterController" })
			 */ 
			/*
			 * .state('returnAcceptance', { url: '/returnAcceptance',
			 * templateUrl:
			 * 'views/procurement/consignment/return-acceptance.html',
			 * controller: "MasterController" })
			 */
				 .state('detailsReturnAcceptance', {
				   url: '/detailsReturnAcceptance',
				   templateUrl: 'views/procurement/consignment/details-return-acceptance.html',
				   controller: "MasterController",
				   title: 'Consignment Manager Dashboard',
				 })

				 
				 
				 .state('microbiologyWorkbeanch', {
					 url: '/microbiologyWorkbeanch',
					 templateUrl: 'views/LIS/Microbiology/microbiology-workbeanch.html',
					 controller: "microbiologyWorkbeanchController",
					 title: 'LIS Workbench',
				})

				 .state('microbiologyReportEntry', {
					 url: '/microbiologyReportEntry',
					 templateUrl: 'views/LIS/Microbiology/microbiology-report-entry.html',
					 controller: "microbiologyReportEntryController",
					 title: 'Report Entry',
				})
				
				
				 .state('microbiologyValidation', {
					 url: '/microbiologyValidation',
					 templateUrl: 'views/LIS/Microbiology/microbiology-validation.html',
					 controller: "microbiologyValidationController",
					 title: 'Report Validation',
				})

				 .state('microbiologyRelease', {
					 url: '/microbiologyRelease',
					 templateUrl: 'views/LIS/Microbiology/microbiology-Release.html',
					 controller: "microbiologyReleaseController",
					 title: 'Report Handover',/* Report Release */
				})

				.state('microbiologyReportEntryDetails', {
					 url: '/microbiologyReportEntryDetails',
					 templateUrl: 'views/LIS/Microbiology/microbiology-report-entry-details.html',
					 controller: "microbiologyReportEntryDetailsController",
					 title: ' Report Entry Details',
					 params:{
						 microbiologyReportObject :{}
					 }
				})
				
				 /**
					 * ***************** Theatre Management New Screens
					 * **********************
					 */
				 .state('patientConsumption', {
					url: '/patientConsumption',
					templateUrl: 'views/master/TheatreManagement/patient-consumption.html',
					controller: "MasterController",
					title: 'Patient Consumption',
				})
				 .state('patientConsumptionDetails', {
					url: '/patientConsumptionDetails',
					templateUrl: 'views/master/TheatreManagement/patient-consumption-details.html',
					controller: "MasterController",
					title: 'Patient Consumption',
				})
			/*
			 * .state('implantReturn', { url: '/implantReturn', templateUrl:
			 * 'views/master/TheatreManagement/implant-return.html', controller:
			 * "MasterController" })
			 */
	/*
	 * .state('addNewMedicationPatientImplantReturn', { url:
	 * '/addNewMedicationPatientImplantReturn', templateUrl:
	 * 'views/master/TheatreManagement/add-new-medication-patient-implant-return.html',
	 * controller: "MasterController" })
	 */
				.state('detailsMedicationPatientImplantReturn', {
					url: '/detailsMedicationPatientImplantReturn',
					templateUrl: 'views/master/TheatreManagement/details-medication-patient-implant-return.html',
					controller: "MasterController",
				})
				.state('addNewConsumablePatientImplantReturn', {
					url: '/addNewConsumablePatientImplantReturn',
					templateUrl: 'views/master/TheatreManagement/add-new-consumable-patient-implant-return.html',
					controller: "MasterController",
				})
				.state('detailsConsumablePatientImplantReturn', {
					url: '/detailsConsumablePatientImplantReturn',
					templateUrl: 'views/master/TheatreManagement/details-consumable-patient-implant-return.html',
					controller: "MasterController"
				})
				.state('resourceChargesConfiguration', {
				url: '/resourceChargesConfiguration',
					templateUrl: 'views/master/GlobalMaster/OTIMS/resource-charges-configuration.html',
					controller: "resourceChargesConfigurationController",
					title: 'Resource Charges Configuration',
				})
				
				 /**
					 * ***************** Theatre Management New Screens
					 * **********************
					 */
				/**
				 * ***************** StoretypeMaster New Screens
				 * **********************
				 */
				.state('storeTypeMaster', {
					url: '/storeTypeMaster',
					templateUrl: 'views/master/UnitMaster/Inventory/store-type-master.html',
					controller: "storeTypeController",
					title: 'Store Type Master',
					resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 805);
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
			   		 }
			
				})
				/**
				 * ***************** TestCostHead (Unit -> LIS) New Screens
				 * **********************
				 */
				.state('testCostHead', {
				 url: '/testCostHead',
				 templateUrl: 'views/master/LIS/test-cost-head.html',
				 controller: "testCostHeadMasterController",
				 title: 'Test Cost Head',
				})
				.state('testCosting', {
				 url: '/testCosting',
				 templateUrl: 'views/master/LIS/test-costing.html',
				 controller: "testCostingMasterController",
				 title: 'Test Costing',
				})
				/**
				 * ***************** TestCostHead (Unit -> LIS) New Screens
				 * **********************
				 */
				.state('testResultTrendAnalysis', {
				 url: '/testResultTrendAnalysis',
				 templateUrl: 'views/LIS/test-result-trend-analysis.html',
				 controller: "MasterController",
				 title: 'Test Result Trend Analysis',
				})
				/**
				 * ***************** IMplant Master (Unit -> Inventory) New
				 * Screens **********************
				 */
			/*
			 * .state('implantMaster', { url: '/implantMaster', templateUrl:
			 * 'views/master/UnitMaster/Inventory/implant-master.html', 
			 * controller: "MasterController" })
			 */
			/*
			 * .state('implantOpeningBalance', { url: '/implantOpeningBalance',
			 * templateUrl:
			 * 'views/PharmacyInventory/Store/implant-opening-balance.html',
			 * controller: "MasterController" })
			 */
				/**
				 * ***************** (Unit > lis) New Screens
				 * **********************
				 */
				.state('serviceProfitMargin', {
				 url: '/serviceProfitMargin',
				 templateUrl: 'views/master/LIS/service-profit-margin.html',
				 controller: "serviceProfitMargin",
				 title: 'Service Profit Margin',
				})
			/*
			 * .state('addNewImplantOpeningBalance', { url:
			 * '/addNewImplantOpeningBalance', templateUrl:
			 * 'views/PharmacyInventory/Store/add-new-implant-opening-balance.html',
			 * controller: "MasterController" })
			 * .state('detailsImplantOpeningBalance', { url:
			 * '/detailsImplantOpeningBalance', templateUrl:
			 * 'views/PharmacyInventory/Store/details-implant-opening-balance.html',
			 * controller: "MasterController" })
			 */
			/*
			 * .state('KitTypeMaster', { url: '/KitTypeMaster', templateUrl:
			 * 'views/master/UnitMaster/pharmacy/kit-type-master.html',
			 * controller: "MasterController" })
			 */
				.state('OTManagerDashboard', {
				url: '/OTManagerDashboard',//screen  hide HIS-34803
				templateUrl: 'views/master/TheatreManagement/ot-manager-dashboard.html',
				controller: "otManagerDashboardController",
				controllerAs : "vm",
				title: 'OT Manager Dashboard',
				})
				.state('preOperative', {
				url: '/preOperative',
				templateUrl: 'views/master/TheatreManagement/pre-oprative.html',
				controller: "preOprativeController",
				title: 'Pre-Operative',
				 params: 
					{
					 	resultCommonObj: {},
					}
				})
				.state('intraOperative', {
				url: '/intraOperative',
				templateUrl: 'views/master/TheatreManagement/intra-oprative.html',
				controller: "intraOprativeController",
				title: 'Intra-Operative',
				 params: 
					{
					 	intraCommonObj: {},
					 	intraDetailsObj: {},
					 	resultCommonObj:{}
					}
				})
				.state('post', {
				url: '/post',
				templateUrl: 'views/master/TheatreManagement/post.html',
				controller: "postController",
				title: 'Post Operative',
				 params: 
					{
					    intraCommonObj: {},
					 	intraDetailsObj: {},
					 	resultCommonObj:{},
					 	postCommonObj: {},
					 	postDetailsObj: {}
					}
				})
				.state('postOperativeCare', {
				url: '/postOperativeCare',
				templateUrl: 'views/master/TheatreManagement/post-oprativecare.html',
				controller: "postOprativeCareController",
				title: 'Recovery Care',
				params:
					{
						intraCommonObj: {},
					 	intraDetailsObj: {},
					 	resultCommonObj:{},
					 	postCommonObj: {},
					 	postDetailsObj: {},
						postCareCommonObj: {},
						postCareDetailsObj: {}
					}
				})
				.state('kiosk', {
					 url: '/kiosk',
					 templateUrl: 'views/OPD/kiosk.html',
					 controller: "selfRegistrationController",
					 title: 'kiosk',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 703);
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
					 params: {
			        	 selfReg: {},
			         }
				})
				.state('icpcCodesConfiguration', {
					 url: '/icpcCodesConfiguration',
					 templateUrl: 'views/master/UnitMaster/organizationMaster/icpc-codes-configuration.html',
					 controller: "icpcCodesConfigurationController",
					 title: 'ICPC Codes Configuration',
				})
				.state('icpcCodeTemplateMapping', {
					 url: '/icpcCodeTemplateMapping',
					 templateUrl: 'views/template/icpc-Code-Template-Mapping.html',
					 controller: "icpcCodeTemplateMappingController",
					 title: 'ICPC Code Clinical Form Mapping',
				})
				/** * Procument - QuotationVendor New Screens ** */
				.state('vendorSpecificPODefinition', {
					 url: '/vendorSpecificPODefinition',
					 templateUrl: 'views/procurement/quotation/vendor-specific-po-definition.html',
					 controller: "MasterController",
					 title: 'Vendor Specific PO Definition'
				})
				.state('addNewVendorSpecificPODefinition', {
					 url: '/addNewVendorSpecificPODefinition',
					 templateUrl: 'views/procurement/quotation/add-new-vendor-specific-po-definition.html',
					 controller: "MasterController",
					 title: 'Vendor Specific PO Definition'
				})
				.state('otIndent', {
					url: '/otIndent',
					templateUrl: 'views/master/TheatreManagement/ot-Indent.html',
					controller: "MasterController",
					title: 'OT Indent',
				})
				.state('vendorSpecificPOCreation', {
					 url: '/vendorSpecificPOCreation',
					 templateUrl: 'views/procurement/quotation/vendor-specific-po-creation.html',
					 controller: "MasterController",
					 title: 'Vendor Specific PO Creation'
				})
				.state('addNewVendorSpecificPOCreation', {
					 url: '/addNewVendorSpecificPOCreation',
					 templateUrl: 'views/procurement/quotation/add-new-vendor-specific-po-creation.html',
					 controller: "MasterController",
					 title: 'Vendor Specific PO Creation'
				})
				.state('detailsVendorSpecificPOCreation', {
					 url: '/detailsVendorSpecificPOCreation',
					 templateUrl: 'views/procurement/quotation/details-vendor-specific-po-creation.html',
					 controller: "MasterController",
					 title: 'Vendor Specific PO Creation'
				})
				.state('vendorSpecificPOApproval', {
					 url: '/vendorSpecificPOApproval',
					 templateUrl: 'views/procurement/quotation/vendor-specific-po-approval.html',
					 controller: "MasterController",
					 title: 'vendor Specific PO Approval'
				})
				.state('detailsVendorSpecificPOApproval', {
					 url: '/detailsVendorSpecificPOApproval',
					 templateUrl: 'views/procurement/quotation/details-vendor-specific-po-approval.html',
					 controller: "MasterController",
					 title: 'vendor Specific PO Approval'
				})
				.state('corporateEmployee', {
					 url: '/corporateEmployee',
					 templateUrl: 'views/master/UnitMaster/billingMaster/corporate-employee.html',
					 controller: "MasterController",
					 title: 'Corporate Employee',
				})
				.state('templateServiceMapping', {
					 url: '/templateServiceMapping',
					 templateUrl: 'views/template/template-service-mapping.html',
					 controller: "templateServiceMappingController",
					 title: 'Clinical Form Service Mapping',
					})
			/**
			 * ************** Book view page Created on 16/Jan/2019 start
			 * ********************
			 */
			.state('bookView', {
				url: '/bookView',
				templateUrl: 'views/book-view.html',
				controller: "bookViewMasterController",
				title: 'Book View',
				    params: 
				        {
				    	bookViewObj: {} // default value
			            }	
				  })
			/**
			 * ************** Book view page Created on 16/Jan/2019 end
			 * ********************
			 */	
				.state('selfServiceNew', {
					 url: '/selfServiceNew',
					 templateUrl: 'views/OPD/self-service-new.html',
					 controller: "selfRegistrationController",
				})
				.state('walkinNew', {
					 url: '/walkinNew',
					 templateUrl: 'views/OPD/walk-new.html',
					 controller: "selfRegistrationController",
					 params: {
			        	 selfReg: {},
			         }
				})
				.state('registerFromKios', {
					 url: '/registerFromKios',
					 templateUrl: 'views/OPD/kios-registration-new.html',
					 controller: "selfRegistrationController",
					 params: {
			        	 selfReg: {},
			         }
				})
				.state('walkinAppointment', {
					 url: '/walkinAppointment',
					 templateUrl: 'views/OPD/walkin-appointment.html',
					 controller: "selfRegistrationController",
						 params: {
							  patientDetailsObj: {} // default value
				            }
				})
				.state('paymentInformation', {
					 url: '/paymentInformation',
					 templateUrl: 'views/OPD/payment-information.html',
					 controller: "selfRegistrationController",
					 params: {
						 patientAppoObjPayment: {} // default value
			            }
				})
				
				
				.state('preBooked', {
					 url: '/preBooked',
					 templateUrl: 'views/OPD/pre-booked.html',
					 controller: "selfRegistrationController",
				})

				.state('nurseCheckInfromKios', {
					 url: '/nurseCheckInfromKios',
					 templateUrl: 'views/OPD/nurse-check-in.html',
					 controller: "selfRegistrationController",
				})
				
				.state('patientSelfTriage', {
				 url: '/patientSelfTriage',
				 templateUrl: 'views/doctorDesk/patient-self-triage.html',
				 // controller: "MasterController"
				 controller: "patientSelfTriageMasterController",
				 title: 'Patient Self-Monitoring',
			    })
			    .state('promotionManagement', {
		         url: '/promotionManagement',
	             templateUrl: 'views/master/GlobalMaster/PromotionConfiguration/promotion-management.html',
		         controller: "MasterController",
		         title: 'Promotion Management',
	             })
	             .state('MISReports', {
		         url: '/MISReports',
	             templateUrl: 'views/OtherApps/MIS-reports.html',
		         controller: "MISReportsController",
		         title: 'MIS Report',
		         resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 768);
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
			   		 }
			
	             })
	             
	             /** ******************** Patient Type ********************** */
	             .state('patientType', {
					 url: '/patientType',
					 templateUrl: 'views/master/UnitMaster/billingMaster/patient-type.html',
					 controller: "patientTypeController",
					 title: 'Patient Class',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 764);
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
				   		 }
				
				})
				.state('patientTypeDetails', {
					 url: '/patientTypeDetails',
					 templateUrl: 'views/master/UnitMaster/billingMaster/patient-type-details.html',
					 controller: "patientTypeDetailsController",
					 title: 'Patient Class Details',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 765);
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
				   		 }
				
				})
				.state('franchiseShare', {
                					 url: '/franchiseShare',
                					 templateUrl: 'views/master/UnitMaster/billingMaster/franchise-share.html',
                					 controller: "franchiseShareController",
                					 title: 'Organization share for franchise',
                					 resolve : {

                				   			data: function($q, $state, $timeout) {
                				   		        var deferred = $q.defer();

                				   		        $timeout(function() {
                				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
                				   				 	.modules.filter(function(item){
                				   	            	   return (item.id == 765);
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
                				   		 }

                				})
				
				.state('couponTemplate', {
					 url: '/couponTemplate',
					 templateUrl: 'views/master/UnitMaster/billingMaster/coupon_template.html',
					 controller: "couponTemplateController",
					 title: 'Coupon Template Master',
					 params: 
				        {
			     			searchObj : {}
				        }
				})
				
				.state('couponTemplateMaster', {
					 url: '/couponTemplateMaster',
					 templateUrl: 'views/master/UnitMaster/billingMaster/coupon_template_Master.html',
					 controller: "couponTemplateMasterController",
					 title: 'Coupon Template Master',
						
				})
				
				
				.state('patientCouponTemplateMapper', {
					 url: '/patientCouponTemplateMapper',
					 templateUrl: 'views/master/UnitMaster/billingMaster/patient_coupon_template_mapper.html',
					 controller: "patientCouponTemplateMapperController",
					 title: 'Patient Coupon Template Mapper',
						
				})

				.state('donorScreeningMaster', {
					 url: '/donorScreeningMaster',
					 templateUrl: 'views/master/UnitMaster/bloodBank/donor-screening-master.html',
					 controller: "donorScreeningController",
					 title: 'Donor Screening Master',
				})
				.state('autoServicePostingConfiguration', {
					 url: '/autoServicePostingConfiguration',
					 templateUrl: 'views/master/UnitMaster/configuration/auto-service-posting-configuration.html',
					 controller: "autoServicePostingConfigurationController",
					 title: 'Auto Service Posting Configuration',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 775);
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
				   		 }
				
				})
				.state('campMaster', {
					 url: '/campMaster',
					 templateUrl: 'views/master/UnitMaster/bloodBank/camp-master.html',
					 controller: "campMasterController",
					 title: 'Camp Master',
				})
					.state('bulkRegistration', {
					 url: '/bulkRegistration',
					 templateUrl: 'views/master/UnitMaster/bloodBank/bulk-registration.html',
					 controller: "bulkRegistrationController",
					 title: 'Bulk Registration',
				})
				.state('transfusionPrecheckMaster', {
					 url: '/transfusionPrecheckMaster',
					 templateUrl: 'views/master/UnitMaster/bloodBank/transfusion-precheck-master.html',
					 controller: "transfusionPrecheckMasterController",
					 title: 'Transfusion Precheck Master',
				})
				.state('bloodIssueAcceptanceWorklist', {
				    url: '/bloodIssueAcceptanceWorklist',
				    templateUrl: 'views/Nursing/blood-issue-acceptance-worklist.html',
				    controller: "bloodIssueAcceptanceWorklistController",
				    title: 'Blood Issuance Acceptance Worklist',
				  })
				  .state('drDisallowedAmount', {
		             url: '/drDisallowedAmount',
		             templateUrl: 'views/master/UnitMaster/billingMaster/dr.Disallowed.Amount.html',
		             controller: "MasterController",
		             title: 'Dr Disallowed Amount',
	             })
	             .state('pathologyMachineParametersLinking', {
				 url: '/pathologyMachineParametersLinking',
				 templateUrl: 'views/master/LIS/pathology-Machine-Parameters-Linking.html',
				 controller: "pathologyMachineParametersController",
				 title: 'Pathology Machine Parameter Linking',
					resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 841);
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
			   		 }
				})
				 .state('machineMaster', {
				 url: '/machineMaster',
				 templateUrl: 'views/master/LIS/machine-Master.html',
				 controller: "machineMasterController",
				 title: 'Machine Master',
					resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 840);
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
			   		 }
				})
				 .state('machineParameterMaster', {
				 url: '/machineParameterMaster',
				 templateUrl: 'views/master/LIS/machine-Parameter-Master.html',
				 controller: "machineParameterMasterController",
				 title: 'Machine Parameter Master',
					resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 839);
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
			   		 }
				})
				
/** *==============>Lab Serology */
				.state('reportEntry', {
				 url: '/reportEntry',
				 templateUrl: 'views/LIS/CentralArea/serology-report-Entry.html',
				 controller: "seroReportEntryWorklistController",
				 title: 'Report Entry',
				 resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 771);
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
			   		 params: 
				        {
			   			searchObj : {}
			            }
				})
				.state('detailsSerologyReportEntry', {
				 url: '/detailsSerologyReportEntry',
				 templateUrl: 'views/LIS/CentralArea/details-Report-Entry.html',
				 controller: "detailsSerologyReportEntry",
				 title: 'Report Entry',
				 params: 
			        {
					 resultMstList: [], // default value
					 searchObj : {},
					 nextRecordList : []
		            }
				})
				.state('reportValidation', {
				 url: '/reportValidation',
				 templateUrl: 'views/LIS/CentralArea/serology-report-validation.html',
				 controller: "seroReportValidationWorklistController",
				 title: 'Report Validation',
				 resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 772);
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
			   		params: 
			        {
		    		 	searchObj : {}
			        }	
			
				})
				.state('detailsReportValidation', 
				{
				 url: '/detailsReportValidation',
				 templateUrl: 'views/LIS/CentralArea/details-Report-Validation.html',
				 controller: "detailsSerologyValidationController",
				 title: 'Report Validation',
				 params: 
			        {
					 resultMstList: [], // default value
					 searchObj : {},
	                 nextRecordList : []
		            }
				})
				 .state('reportRelease',
				{
				 url: '/reportRelease',
				 templateUrl: 'views/LIS/CentralArea/serology-report-release.html',
				 controller: "seroReportReportReleaseWorklistController",
				 title: 'Report Release',
				 resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 773);
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
			   		 params: 
				        {
			   			 searchObj : {}
			            }
				})
				.state('detailsReportRelease', {
				 url: '/detailsReportRelease',
				 templateUrl: 'views/LIS/CentralArea/details-Report-Release.html',
				 controller: "detailsSerologyReportReleaseController",
				 title: 'Report Release',
				 params: 
			        {
					 backFlag : null,
					 resultMstList: [], // default value
					 searchObj : {},
					 nextRecordList : []
		            }
					
				})
			
/** *==============>Lab Serology End */				
		/*
		 * .state('patientAcceptance.admittedPatientList', { url :
		 * '/admittedPatientList', views : { '@patientAcceptance' : {
		 * templateUrl : 'views/ICU/admitted-Patient-List.html', controller :
		 * 'patientAcceptanceController' } } })
		 */
	             .state('nursingCoversheet', {
					 url: '/nursingCoversheet',
					 templateUrl: 'views/Nursing/nursing-coversheet.html',
					 controller: "nursingCoverSheetController",
					 title: 'IPD Nursing Cover Sheet',
						 params: {
							  patientDetailsObj: {} // default value
				            }
					
				})
				
				/** **09-05-19******** */
				   .state('erNursingCoversheet', {
					 url: '/erNursingCoversheet',
					 templateUrl: 'views/Nursing/nursing-coversheet.html',
					 controller: "erNursingCoverSheetController",
					 title: 'ER Nursing Cover Sheet',
						 params: {
							  patientDetailsObj: {} // default value
				            }
					
				})
				   .state('dayCareNursingCoversheet', {
					 url: '/dayCareNursingCoversheet',
					 templateUrl: 'views/Nursing/nursing-coversheet.html',
					 controller: "dayCareNurshingCoversheetController",
					 title: 'Daycare Nursing Cover Sheet',
						 params: {
							  patientDetailsObj: {} // default value
				            }
					
				})
				/** ********** */
				.state('dueDepositDashboard', {
					 url: '/dueDepositDashboard',
					 templateUrl: 'views/master/GlobalMaster/due-deposit-dashboard.html',
					 controller: "MasterController",
					 title: 'Due Deposit Dashboard',
				})
	             .state('tubeFeedingProducts', {
					 url: '/tubeFeedingProducts',
					 templateUrl: 'views/master/GlobalMaster/DietNutrition/tube-feeding-products.html',
					 controller: "tubeFeedingProductsController",
					 title: 'Tube Feeding Products',
					  // redirectTo: '/addnewdaywise-diet-template.html'
	             })
	             .state('trustMaster', {
					 url: '/trustMaster',
					 templateUrl: 'views/master/UnitMaster/fundManagement/trust-master.html',
					 controller: "trustMasterController",
					 title: 'Trust Master',
				})

				.state('ambulanceTypeMaster', {
	                 url: '/ambulanceTypeMaster',     
	                 templateUrl: 'views/master/UnitMaster/ambulanceService/ambulance-Type-Master.html',
	                 controller: "ambulanceTypeController",
	                 title: 'Ambulance Type Master',
	                 resolve : {

	     	   			data: function($q, $state, $timeout) {
	     	   		        var deferred = $q.defer();
	     	   		
	     	   		        $timeout(function() {
	     	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	     	   				 	.modules.filter(function(item){
	     	   	            	   return (item.id == 813);
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
	     	   		 }
	     	
	             })
                .state('ambulanceMaster', {
	                 url: '/ambulanceMaster',     
	                 templateUrl: 'views/master/UnitMaster/ambulanceService/ambulance-Master.html',
	                 controller: "ambulanceMasterController",
	                 title: 'Ambulance Master',
	                 resolve : {

	     	   			data: function($q, $state, $timeout) {
	     	   		        var deferred = $q.defer();
	     	   		
	     	   		        $timeout(function() {
	     	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	     	   				 	.modules.filter(function(item){
	     	   	            	   return (item.id == 814);
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
	     	   		 }
	     	
                })
                .state('ambulanceServiceProviderMaster', {
	                 url: '/ambulanceServiceProviderMaster',     
	                 templateUrl: 'views/master/UnitMaster/ambulanceService/ambulance-Service-Provider-Master.html',
	                 controller: "ambulanceServiceProviderController",
	                 title: 'Service Provider Master',
	                 resolve : {

	     	   			data: function($q, $state, $timeout) {
	     	   		        var deferred = $q.defer();
	     	   		
	     	   		        $timeout(function() {
	     	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	     	   				 	.modules.filter(function(item){
	     	   	            	   return (item.id == 815);
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
	     	   		 }
	     	
                })
                .state('ambulanceTarrifMaster', {
	                 url: '/ambulanceTarrifMaster',     
	                 templateUrl: 'views/master/UnitMaster/ambulanceService/ambulance-Tarrif-Master.html',
	                 controller: "ambulanceTarrifController",
	                 title: 'Ambulance Tariff Master',
	                 resolve : {

	     	   			data: function($q, $state, $timeout) {
	     	   		        var deferred = $q.defer();
	     	   		
	     	   		        $timeout(function() {
	     	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	     	   				 	.modules.filter(function(item){
	     	   	            	   return (item.id == 816);
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
	     	   		 }
	     	
                })
                .state('resourceAllocationMaster', {
	                 url: '/resourceAllocationMaster',     
	                 templateUrl: 'views/master/UnitMaster/ambulanceService/resource-Allocation-Master.html',
	                 controller: "resourceAllocationController",
	                 title: 'Resource Allocation Master',
	                 resolve : {

	     	   			data: function($q, $state, $timeout) {
	     	   		        var deferred = $q.defer();
	     	   		
	     	   		        $timeout(function() {
	     	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	     	   				 	.modules.filter(function(item){
	     	   	            	   return (item.id == 817);
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
	     	   		 }
	     	
                })
                .state('ambulanceRouting', {
	                 url: '/ambulanceRouting',     
	                 templateUrl: 'views/master/UnitMaster/ambulanceService/ambulance-Directing.html',
	                 controller: "ambulanceRoutingController",
	                 title: 'Ambulance Directing',
                })
                
                .state('ambulanceFacilitiesMaster', {
	                 url: '/ambulanceFacilitiesMaster',     
	                 templateUrl: 'views/master/UnitMaster/ambulanceService/ambulance-Facilities-Master.html',
	                 controller: "ambulanceFacilitiesController",
	                 title: 'Ambulance Facilities Master',
	                 resolve : {

	     	   			data: function($q, $state, $timeout) {
	     	   		        var deferred = $q.defer();
	     	   		
	     	   		        $timeout(function() {
	     	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	     	   				 	.modules.filter(function(item){
	     	   	            	   return (item.id == 818);
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
	     	   		 }
	     	
                })
                
                .state('ambulanceCharges', {
	                 url: '/ambulanceCharges',     
	                 templateUrl: 'views/master/UnitMaster/ambulanceService/ambulance-Charges.html',
	                 controller: "ambulanceChargesController",
	                 title: 'Ambulance Charges',
	                 resolve : {

	     	   			data: function($q, $state, $timeout) {
	     	   		        var deferred = $q.defer();
	     	   		
	     	   		        $timeout(function() {
	     	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	     	   				 	.modules.filter(function(item){
	     	   	            	   return (item.id == 819);
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
	     	   		 }
	     	
                })

				/*
				 * .state('fundInflow', { url: '/fundInflow', templateUrl:
				 * 'views/master/UnitMaster/fundManagement/fund-inflow.html',
				 * controller: "fundInflowController", title: 'Fund Inflow', })
				 */
				.state('fundReconciliation', {
					 url: '/fundReconciliation',
					 templateUrl: 'views/master/UnitMaster/fundManagement/fund-reconciliation.html',
					 controller: "fundInflowController",
					 title: 'Fund Reconciliation',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 808);
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
				   		 }
				
				})
				.state('fundApproval', {
					 url: '/fundApproval',
					 templateUrl: 'views/master/UnitMaster/fundManagement/fund-approval.html',
					 controller: "MasterController",
					 title: 'Fund Approval',
				})
				.state('fundRequisition', {
					 url: '/fundRequisition',
					 templateUrl: 'views/master/UnitMaster/fundManagement/fund-requisition.html',
					 controller: "MasterController",
					 title: 'Fund Requisition',
				})
				.state('fundApprovedList', {
					 url: '/fundApprovedList',
					 templateUrl: 'views/master/UnitMaster/fundManagement/fund-approved-list.html',
					 controller: "MasterController",
					 title: 'Fund Approved List',
				})
				/*
				 * .state('UHIDMergeRequest', { url: '/UHIDMergeRequest',
				 * templateUrl:
				 * 'views/master/UnitMaster/UHIDMerge/UHID-Merge-request.html',
				 * controller: "MasterController", title: 'UHID Merge Request', })
				 * .state('UHIDMergeReqApproval', { url:
				 * '/UHIDMergeReqApproval', templateUrl:
				 * 'views/master/UnitMaster/UHIDMerge/UHID-Merge-request-approval.html',
				 * controller: "MasterController", title: 'UHID Merge Request
				 * Approval', })
				 */
				.state('discountCategoryMaster', {
					 url: '/discountCategoryMaster',
					 templateUrl: 'views/master/UnitMaster/billingMaster/discount-Category-Master.html',
					 controller: "discountCategoryMasterController",
					 title: 'Discount Category Master',
					 resolve : {

		     	   			data: function($q, $state, $timeout) {
		     	   		        var deferred = $q.defer();
		     	   		
		     	   		        $timeout(function() {
		     	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
		     	   				 	.modules.filter(function(item){
		     	   	            	   return (item.id == 973);
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
		     	   		 }
				})
				.state('passTypeMaster', {
	                 url: '/passTypeMaster',     
	                 templateUrl: 'views/master/UnitMaster/Visit Type/pass-Type-Master.html',
	                 controller: "visitorTypeMasterController",
	                 title: 'Visitor Pass Type Master',
	                 resolve : {

	     	   			data: function($q, $state, $timeout) {
	     	   		        var deferred = $q.defer();
	     	   		
	     	   		        $timeout(function() {
	     	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	     	   				 	.modules.filter(function(item){
	     	   	            	   return (item.id == 821);
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
	     	   		 }
	     	
                })
                .state('passBedCategoryMapper', {
	                 url: '/passBedCategoryMapper',     
	                 templateUrl: 'views/master/UnitMaster/Visit Type/pass-Bed-Category-Mapper.html',
	                 controller: "passBedCategoryMapperController",
	                 title: 'Visitor Pass Ward Bed Category Mapper',
	                 resolve : {

	     	   			data: function($q, $state, $timeout) {
	     	   		        var deferred = $q.defer();
	     	   		
	     	   		        $timeout(function() {
	     	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	     	   				 	.modules.filter(function(item){
	     	   	            	   return (item.id == 822);
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
	     	   		 }
	     	
                })
                 /** ******************** QMS Master ********************** */
	             .state('QMSkioskMaster', {
					 url: '/QMSkioskMaster',
					 templateUrl: 'views/master/QMSMaster/QMS-kiosk-Master.html',
					 controller: "QMSkioskMasterController",
					 title: 'QMS Kiosk Master',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 795);
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
				   		 }
				
				})
				.state('QMSdepartmentMaster', {
					 url: '/QMSdepartmentMaster',
					 templateUrl: 'views/master/QMSMaster/QMS-department-Master.html',
					 controller: "QMSdepartmentMasterController",
					 title: 'QMS Department Master',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 796);
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
				   		 }
				
				})
				.state('QMScounterMaster', {
					 url: '/QMScounterMaster',
					 templateUrl: 'views/master/QMSMaster/QMS-counter-Master.html',
					 controller: "QMScounterMasterController",
					 title: 'QMS Counter Master',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 797);
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
				   		 }
				
				})
                
                .state('OTIMSroleCreation', {
					url: '/OTIMSroleCreation',
					templateUrl: 'views/master/UnitMaster/userManagement/OTIMS-role-creation.html',
					controller: "OTIMSroleCreationController",
					title: 'OTIMS Role Creation',
				})
				.state('call', {
	                 url: '/call',     
	                 templateUrl: 'views/master/UnitMaster/qms-call.html',
	                 controller: "QMSCallMasterController",
	                 title: 'QMS Call',
	                 resolve : {

	     	   			data: function($q, $state, $timeout) {
	     	   		        var deferred = $q.defer();
	     	   		
	     	   		        $timeout(function() {
	     	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	     	   				 	.modules.filter(function(item){
	     	   	            	   return (item.id == 793);
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
	     	   		 }
	     	
                })
                
                 .state('QMSMasterCall', {
	                 url: '/QMSMasterCall',     
	                 templateUrl: 'views/master/QMSMaster/QMS-master-call.html',
	                 controller: "QMSMasterTokenCallController",
	                 title: 'QMS Token Display',
	                 resolve : {

	     	   			data: function($q, $state, $timeout) {
	     	   		        var deferred = $q.defer();
	     	   		
	     	   		        $timeout(function() {
	     	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	     	   				 	.modules.filter(function(item){
	     	   	            	   return (item.id == 794);
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
	     	   		 }
	     	
                })
                
                .state('QMS', {
	                 url: '/QMS',     
	                 templateUrl: 'views/master/QMSMaster/QMS.html',
	                 controller: "QMSMasterController",
	                 title: 'QMS Token'
                })
                .state('display', {
	                 url: '/display/:orgId/:unitId/:cookieskioskId/:userId',     
	                 templateUrl: 'views/master/QMSMaster/display.html',
	                 controller: "QMSTokenDisplayMasterController",
	                 title: 'QMS Token Display',
	                 resolve : {

                         orgId: function($stateParams, GenericService)
                         {
                             return $stateParams.orgId;
                         },
                         unitId: function($stateParams, GenericService)
                         {
                             return $stateParams.unitId;
                         },
                         cookieskioskId: function($stateParams, GenericService)
                         {
                             return $stateParams.cookieskioskId;
                         },
                         userId: function($stateParams, GenericService)
                         {
                             return $stateParams.userId;
                         },
	     	   		 }
	     	
                })
                
                  .state('displayCall', {
	                 url: '/displayCall',     
	                 templateUrl: 'views/master/QMSMaster/QMS-display.html',
	                 controller: "QMSTokenDisplayMasterCallController",
	                 title: 'QMS Token Display',
	                 resolve : {

	     	   			data: function($q, $state, $timeout) {
	     	   		        var deferred = $q.defer();
	     	   		
	     	   		        $timeout(function() {
	     	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	     	   				 	.modules.filter(function(item){
	     	   	            	   return (item.id == 794);
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
	     	   		 }
	     	
                })
                
                .state('MPShahPatientList', {
	                 url: '/MPShahPatientList',     
	                 templateUrl: 'views/master/MPShah Data/MPShah-patient-list.html',
	                 controller: "MasterController",
	                 title: 'Patient List',
                })
                .state('MPShahEncounterList', {
	                 url: '/MPShahEncounterList',     
	                 templateUrl: 'views/master/MPShah Data/MPShah-encounter-list.html',
	                 controller: "MasterController",
	                 title: 'Encounter List',
                })
                .state('MPShahEncounterSummary', {
	                 url: '/MPShahEncounterSummary',     
	                 templateUrl: 'views/master/MPShah Data/MPShah-encounter-summary.html',
	                 controller: "MasterController",
	                 title: 'Encounter Summary',
                })
               /*
				 * .state('RMOAllocationMaster', { url: '/RMOAllocationMaster',
				 * templateUrl: 'views/doctorDesk/RMO-allocation-master.html',
				 * controller: "MasterController" })
				 */
              /*
				 * .state('consentPrint', { url: '/consentPrint', templateUrl:
				 * 'views/master/UnitMaster/billingMaster/common/consentsPrint.html',
				 * controller: "MasterController" })
				 */
                .state('notesMaster', {
	                 url: '/notesMaster',     
	                 templateUrl: 'views/master/GlobalMaster/OTIMS/notes-master.html',
	                 controller: "notesMasterController",
	                 title: 'Notes Master',
                })
                .state('OTIMSdashboard', {
	                 url: '/OTIMSdashboard',     
	                 templateUrl: 'views/master/GlobalMaster/OTIMS/OTIMS-dashboard.html',
	                 controller: "OTIMSdashboardController",
	                 title: 'OTIMS Dashboard',
                })
                .state('OTIMSdashboardInfo', {
	                 url: '/OTIMSdashboardInfo',     
	                 templateUrl: 'views/master/GlobalMaster/OTIMS/OTIMS-dashboard-info.html',
	                 controller: "OTIMSdashboardInfoController",
	                 title: 'OTIMS Dashboard',
	                 params: 
	     				{
	     					procedureObj: {},
	     				}
                })
                .state('plannedProcedure', {
	                 url: '/plannedProcedure',     
	                 templateUrl: 'views/master/GlobalMaster/OTIMS/OTIMS-dashboard-info1.html',
	                 controller: "plannedProcedureController",
	                 title: 'OTIMS Dashboard',
	                 params: 
	     				{
	     					procedureObj: {},
	     				}
                })
                .state('MLCReportingWorklist', {
	                 url: '/MLCReportingWorklist',     
	                 templateUrl: 'views/master/UnitMaster/billingMaster/MLC-reporting-worklist.html',
	                 controller: "MLCReportingWorklistController",
	                 title: 'MLC Reporting Worklist',
                })
                .state('patientAccountReconciliation', {
					 url: '/patientAccountReconciliation',
					 templateUrl: 'views/master/UnitMaster/billingMaster/patient-Account-Reconciliation.html',
					 controller: "MasterController",
					 title: 'Patient Account Reconciliation',
				})
				.state('employeeMasterGlobal', {
					 url: '/employeeMasterGlobal',
					 templateUrl: 'views/master/GlobalMaster/employee-master-global.html',
					 controller: "corporateMasterController",
					 title: 'Patient Account Reconciliation',
				})
				 .state('patientDischarged', {
				    url: '/patientDischarged',
				    templateUrl: 'views/AdmissionDischarge/patient-discharged.html',
				    controller: "patientDischargedMasterController",
				    title: 'Final Discharge',
				    
				  })
				.state('patientDischarge', {
				    url: '/patientDischarge',
				    templateUrl: 'views/AdmissionDischarge/patient-discharge.html',
				    controller: "cleanedBedAcceptController",
				    title: 'Hospital Discharge Patient',
				  })
				 
					.state('nurseAllocationMsaster', {
					 url: '/nurseAllocationMsaster',
					 templateUrl: 'views/Nursing/nurse-Allocation-master.html',
					 controller: "nurseAllocationMsasterController",
					 title: 'Nurse Allocation Master',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 802);
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
				   		 }
				
				})
				.state('nursingAllocationDashboard', {
					 url: '/nursingAllocationDashboard',
					 templateUrl: 'views/Nursing/nursing-Allocation-Dashboard.html',
					 controller: "nurseAllocationDashboardController",
					 title: 'Nursing Allocation Dashboard',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 800);
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
				   		 }
				
				})
				.state('nursingShiftDashboard', {
					 url: '/nursingShiftDashboard',
					 templateUrl : 'views/Nursing/nursing-Shift-Dashboard.html',
					 controller: "nursingShiftDashboardController",
					 title: 'Nursing Shift Dashboard',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 801);
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
				   		 }
				
				})
				.state('procedureTemplate', {
					 url: '/procedureTemplate',
					 templateUrl: 'views/master/GlobalMaster/OTIMS/procedure-template.html',
					 controller: "procedureTemplateController",
					 title: ' Procedure Configuration',
					 params: 
						{
						commonObj: {},
						infoObj : {}
						}
				  })
				.state('radiologyReportReleaseWorkList', {
				    url: '/radiologyReportReleaseWorkList',
				    templateUrl: 'views/master/RadiologyWorkbench/radiology-report-release-worklist.html',
				    controller: "radiologyReportReleaseWorkListController",
				    title: 'Radiology Report Release WorkList',
				  })
				.state('pendingTransfusionList', {
					 url: '/pendingTransfusionList',
					 templateUrl: 'views/master/bloodBankDonor/pending-transfusion-list.html',
					 controller: "pendingTransfusionListController",
					 title: 'Pending Transfusion List',
				})
				/*
				 * .state('ambulanceCharges', { url: '/ambulanceCharges',
				 * templateUrl:
				 * 'views/master/UnitMaster/ambulanceService/ambulance-Charges.html',
				 * controller: "MasterController" })
				 */
                .state('newPeriodicityService', {
				    url: '/newPeriodicityService',
				    templateUrl: 'views/Nursing/new-periodicity-Service.html',
				    controller: "MasterController",
				    title: 'Periodicity Service',
				  })
				 .state('bedsideInvestigationsMaster', {
					 url: '/bedsideInvestigationsMaster',
					 templateUrl: 'views/master/LIS/bedside-investigations-master.html',
					 controller: "bedsideInvestigationsMasterController",
					 title: 'Ward Bedside Investigations Master',
				})
				/*
				 * .state('bloodIssueAcceptanceWorklist', { url:
				 * '/bloodIssueAcceptanceWorklist', templateUrl:
				 * 'views/Nursing/blood-issue-acceptance-worklist.html',
				 * controller: "bloodIssueAcceptanceWorklistController" })
				 */

				.state('departmentStoreMapper', {
					 url: '/departmentStoreMapper',
					 templateUrl: 'views/master/UnitMaster/assetManagement/department-store-mapper.html',
					 controller: "departmentStoreMapperController",
					 title: 'Department Store Mapper',
				})
				.state('wardStoreMapper', {
					url: '/wardStoreMapper',
					templateUrl: 'views/master/UnitMaster/assetManagement/ward-store-mapper.html',
					controller: "wardStoreMapperController",
					title: 'Ward Store Mapper',
			   	})
				.state('radiologyResultEntry', {
				 url: '/radiologyResultEntry',
				 templateUrl: 'views/master/RadiologyWorkbench/radiology-result-entry.html',
				 controller: "radiologyResultEntryController",
				 title: 'Radiology Result Entry',
				 params:{
					 searchObj :{}
				 },
				 resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 830);
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
			   		 }
			
				})
				.state('radiologyReport', {
				 url: '/radiologyReport',
				 templateUrl: 'views/master/RadiologyWorkbench/radiology-report.html',
				 controller: "radiologyReportController",
// title: 'Radiology Result Validation',
				 params:{
					 radiologyReportObject :{},
					 searchObj : {}
				 }
				 
				})
				.state('radiologyWorklistForPrinting', {
		 	    url: '/radiologyWorklistForPrinting',
			    templateUrl: 'views/master/RadiologyWorkbench/radiology-printing-worklist.html',
			    controller: "radiologyWorklistForPrintingController",
			    title: 'Radiology Printing Work List',
			    resolve : {

		   			data: function($q, $state, $timeout) {
		   		        var deferred = $q.defer();
		   		
		   		        $timeout(function() {
		   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
		   				 	.modules.filter(function(item){
		   	            	   return (item.id == 832);
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
		   		 }
		
			  })
				.state('genericScheduleMaster', {
				url: '/genericScheduleMaster',
				templateUrl: 'views/master/GlobalMaster/generic-schedule-master.html',
				controller: "genericScheduleMasterController",
				title: 'Generic Schedule Master',
				resolve : {

		   			data: function($q, $state, $timeout) {
		   		        var deferred = $q.defer();
		   		
		   		        $timeout(function() {
		   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
		   				 	.modules.filter(function(item){
		   	            	   return (item.id == 827);
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
		   		 }
		
				})
				.state('radiologyResultValidation', {
				 url: '/radiologyResultValidation',
				 templateUrl: 'views/master/RadiologyWorkbench/radiology-result-validation.html',
				 controller: "radiologyResultValidationController",
				 title: 'Radiology Result Validation',
				 params:{
					 searchObj :{}
				 },
				 resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 831);
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
			   		 }
			
				})
				.state('radiologyReportValidation', {
				 url: '/radiologyReportValidation',
				 templateUrl: 'views/master/RadiologyWorkbench/radiology-report-validation.html',
				 controller: "MasterController",
				 title: 'Radiology Report Validation',
				})
				
				/** ************************OT COMPLETION********************* */
				.state('otBillingCompletionDetails', {
				url: '/detailsOTBillingCompletion',
				templateUrl: 'views/master/TheatreManagement/details-OT-billing-completion.html',
				controller: "OtBillingDetailsComp.Controller", 
				controllerAs: "vm",
				title: 'Details OT Billing Completion',
				params: 
				{
				resultCommonObj: {},
				resultPatientDetailsObj : {}, 
				otimsProcedureList:{}// default value
				} 
				})
				
		 .state('hospitalAccountMaster', {
		 url: '/hospitalAccountMaster',
		 templateUrl: 'views/master/UnitMaster/billingMaster/hospital-account-master.html',
		 controller: "hospitalAccountMasterController",
		 title: 'Hospital Account Master',
		 resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 788);
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
	   		 }
	
		 
	})
				
				/** ******* Start Donor Registration******* */
				
				
				/** Other App Data *@Code Ganesh Chaudhari 07-04-2019 */
				.state('encounterData', {
			 	    url: '/encounterData',
				    templateUrl: 'views/master/MPShah Data/MPShah-encounter-list.html',
				    controller: "encounterDataController",
				    title: 'Encounter List',
				    params: 
		 	    	{
		 	    		pid: null // default value
		 	    	}	
				  })
				.state('encounterDataDetails', {
			 	    	url: '/encounterDataDetails',
			 	    	templateUrl: 'views/master/MPShah Data/MPShah-encounter-summary.html',
			 	    	controller: "encounterDataDetailsController",
			 	    	title: 'Encounter Summary',
			 	    	params: 
			 	    	{
			 	    		encounterId: null // default value
			 	    	}	
				  })
				 .state('pharmacyTariffMaster', {
					 url: '/pharmacyTariffMaster',
					 templateUrl: 'views/master/UnitMaster/billingMaster/pharmacy-tariff-master.html',
					 controller: "pharmacyTariffMasterController",
					 title: 'Pharmacy Tariff Master',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 803);
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
				   		 }
				
				})
				.state('chemotheraphyTemplateMaster', {
				    url: '/chemotheraphyTemplateMaster',
				    templateUrl: 'views/master/Chemotheraphy/chemotheraphy-template-master.html',
				    controller: "MasterController",
				    title: 'Pharmacy Tariff Master',
				    })
			    .state('addNewChemotheraphyTemplateMaster', {
				    url: '/addNewChemotheraphyTemplateMaster',
				    templateUrl: 'views/master/Chemotheraphy/add-new-chemotheraphy-template-master.html',
				    controller: "MasterController",
				    title: 'Chemotheraphy Template Master',
				    })
				.state('dashboard', {
					url: '/dashboard',
					templateUrl: 'views/common/dashboard/CEO-dashboard.html',
					controller: "CEOdashbordController",
					title: 'Dashboard',
					})
				.state('dashboardInfo', {
					url: '/dashboardInfo',
					templateUrl: 'views/common/dashboard/CEO-dashboard-info.html',
					controller: "dashboardInfoMasterController",
					title: 'Dashboard',
					params: {
						params: {}
				    }
					})
					
				.state('test', {
					url: '/test',
					templateUrl: 'views/common/dashboard-quick-links.html',
					controller: "MasterController",
					title: 'Test',
					})
				.state('nurseDashboard', {
					url: '/nurseDashboard',
					templateUrl: 'views/common/dashboard/nurse-dashboard.html',
					controller: "nurseDashboardMasterController",
					title: 'Dashboard - Nurse',
					})
					
					.state('nurseDashboardInfo', {
					url: '/nurseDashboardInfo',
					templateUrl: 'views/common/dashboard/nurse-dashboard-info.html',
					controller: "dashboardInfoMasterController",
					title: 'Dashboard - Nurse',
					params: {
						params: {}
				    }
					})
					
					.state('doctorDashboard', {
					url: '/doctorDashboard',
					templateUrl: 'views/common/dashboard/doctor-dashboard.html',
					controller: "doctorDashboardController",
					title: 'Dashboard - Doctor',
					})
					.state('diagnosticsDashboard', {
					url: '/diagnosticsDashboard',
					templateUrl: 'views/common/dashboard/finance-dashboard.html',
					controller: "financerDashboardController",
					title: 'Dashboard - Diagnostics',
					})
					.state('home', {
					url: '/home',
					templateUrl: 'views/common/dashboard/home.html',
					controller:'nurseDashboardMasterController',
					title: 'Home',
					})
					
				.state('doctorDashboardInfo', {
					url: '/doctorDashboardInfo',
					templateUrl: 'views/common/dashboard/doctor-dashboard-info.html',
					controller: "dashboardInfoMasterController",
					title: 'Dashboard - Doctor',
					params: {
						params: {}
				    }
					})
					
				.state('laborRoomTransferRequestAcceptance',{
		   	        url : '/laborRoomTransferRequestAcceptance',
		    	    templateUrl : 'views/Nursing/labor-room-transfer-request-acceptance.html',
		    	    controller : "laborRoomTransferRequestAcceptanceController",
		    	    title: 'Transfer/Move Request Acceptance',
		    	    resolve : {

			   			data: function($q, $state, $timeout) {
			   		        var deferred = $q.defer();
			   		
			   		        $timeout(function() {
			   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
			   				 	.modules.filter(function(item){
			   	            	   return (item.id == 834);
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
			   		 }
			

		    	    })
		    	    
		    	 
		    	    
	    	    .state('hemodialysisRoomTransferRequestAcceptance',{
		   	        url : '/hemodialysisRoomTransferRequestAcceptance',
		    	    templateUrl : 'views/Nursing/hemodialysis-room-transfer-request-acceptance.html',
		    	    controller : "hemodialysisRoomTransferRequestAcceptanceController",
		    	    title: 'Transfer Request Acceptance',
		    	    })
				
				.state('cyclicAppointments', {
					url: '/cyclicAppointments',
					templateUrl: 'views/master/GlobalMaster/Encounter/cyclic-appointments.html',
					controller: "MasterController",
					title: 'Cyclic Appointments',
					})
				.state('dueDepositReport', {
					url: '/dueDepositReport',
					templateUrl: 'views/common/billing/due-deposit-report.html',
					controller: "MasterController",
					title: 'Due Deposit Report',
					})
				.state('nursingHandover', {
				    url: '/nursingHandover',
				    templateUrl: 'views/Nursing/nursing-handover.html',
				    controller: "MasterController",
				    title: 'Nursing Handover',
				    })
				.state('roomModulityMapper', {
					url: '/roomModulityMapper',
					templateUrl: 'views/master/UnitMaster/organizationMaster/room-modulity-mapper.html',
					controller: "MasterController",
					title: 'Room Modulity Mapper',
				})
				  
		    	.state('roomModality', {
					 url: '/roomModality',
					 templateUrl: 'views/bedManagement/room-modality.html',
					 controller: "roomModalityController",
					 title: 'Room Modality',
				})
				.state('doctorPayOuts', {
					 url: '/doctorPayOuts',
					 templateUrl: 'views/master/DoctorShare/doctor-payouts.html',
					 controller: "roomModalityController",
					 title: 'Doctor Payouts',
				})
/* .set. */				
				/** ******* Procedure Configuration ******* */
				.state('procedureConfig', {
				 url: '/procedureConfig',
				 templateUrl: 'views/procedureConfiguration/procedure-config.html',
				 controller: "MasterController",
				 title: 'Procedure Configuration',
				})
				/** ******* initial Assessment ****** */
				.state('initialAssessment', {
				 url: '/initialAssessment',
				 templateUrl: 'views/master/initialAssessment.html',
				 controller: "MasterController",
				})
				/** ******* dr creditNote ****** */
				.state('drCreditNote', {
				 url: '/drCreditNote',
				 templateUrl: 'views/master/DoctorShare/dr-creditnote.html',
				 controller: "drCreditNote",
				 title: 'Doctor Credit Note',
				})
				.state('drCreditNoteApproval', {
				 url: '/drCreditNoteApproval',
				 templateUrl: 'views/master/DoctorShare/dr-creditnote-approval.html',
				 controller: "drCreditNoteApproval",
				 title: "Doctor's Credit Note Approval",
				})
				.state('referralCreditNoteApproval', {
				 url: '/referralCreditNoteApproval',
				 templateUrl: 'views/master/DoctorShare/referral-creditnote-approval.html',
				 controller: "drCreditNoteApproval",
				 title: 'Referral Credit Note Approval',
				})
				/*
				 * .state('cssdFixedAssetMaster', { url:
				 * '/cssdFixedAssetMaster', templateUrl:
				 * 'views/master/UnitMaster/CSSD/cssd-fixed-asset-master.html',
				 * controller: "addCssdOpeningStockController" })
				 */
				.state('addNewCssdFixedAssetMaster', {
				 url: '/addNewCssdFixedAssetMaster',
				 templateUrl: 'views/master/UnitMaster/CSSD/add-new-cssd-fixed-asset-master.html',
				 controller: "cssdOpeningStockController",
				 title: 'CSSD Fixed Asset Master',
				 params: 
			      {
					 openingBalanceObj:{} // default value
			      }	
				})
				.state('fundStatusDashboard', {
				 url: '/fundStatusDashboard',
				 templateUrl: 'views/master/UnitMaster/billingMaster/fund-status-dashboard.html',
				 controller: "fundStatusDashboardController",
				 title: 'Fund Status Dashboard',
				})
				.state('addNewExclusive', {
				 url: '/addNewExclusive',
				 templateUrl: 'views/master/UnitMaster/billingMaster/add-new-exclusive.html',
				 controller: "addNewExclusiveController",
				 title: 'All Inclusive Package',
                 		 params:{
                          			 newAllExclusiveObj :{}
                          		 }

				})
				.state('addNewMultiEncounter', {
				 url: '/addNewMultiEncounter',
				 templateUrl: 'views/master/UnitMaster/billingMaster/add-new-multi-encounter.html',
				 controller: "addNewMultiEncounterController",
				 title: 'Multi Visit Package',
				 params:{
                         newMulticounterPackageObj :{}
                      }
				})
				
				.state('transactionTypeMaster', {
				 url: '/transactionTypeMaster',
				 templateUrl: 'views/master/GlobalMaster/transaction-type-master.html',
				 controller: "transactionTypeMasterController",
				 title: 'Transaction Type Master',
				})
				.state('dietUnitMater', {
				 url: '/dietUnitMater',
				 templateUrl: 'views/master/GlobalMaster/DietNutrition/diet-unit-master.html',
				 controller: "dietUnitMasterController",
				 title: 'Diet Unit Master',
				})
				.state('cyclicAppointmentScheduling', {
				url: '/cyclicAppointmentScheduling',
				templateUrl: 'views/master/GlobalMaster/Encounter/cyclic-appointment-scheduling.html',
				controller: "cyclicAppointmentSchedulingController",
				title: 'Cyclic Appointment Scheduling',
				})
				// Added pages by Pranita 06.11.2019 [**************** START
				// ********************]
				
				.state('demoFooter', {        /**
												 * ****** added by Pranita
												 * 20.4.2020*******
												 */
				    url: '/demoFooter',
				    templateUrl: 'views/AdmissionDischarge/demo-footer.html',
				    controller: "MasterController"
				  })
				.state('miscellaneousExpense', {    /**
													 * ****** added by Pranita
													 * 24.3.2020*******
													 */
					 url: '/miscellaneousExpense',
					 templateUrl: 'views/master/UnitMaster/billingMaster/miscellaneous-expense.html',
					 controller: "miscellaneousExpenseController",
					 title: 'Miscellaneous Expense',
				})
				.state('detailsMiscellaneousExpense', {   
					 url: '/detailsMiscellaneousExpense',
					 templateUrl: 'views/master/UnitMaster/billingMaster/details-miscellaneous-expense.html',
					 controller: "MasterController",
					 title: 'Details Miscellaneous Expense',
				})
				.state('addNewMiscellaneousExpense', {   
					 url: '/addNewMiscellaneousExpense',
					 templateUrl: 'views/master/UnitMaster/billingMaster/add-new-miscellaneous-expense.html',
					 controller: "addNewMiscellaneousExpenseController",
					 title: 'Add New Miscellaneous Expense',
					 params: 
				      {
						 miscellaneousObj:{} // default value
				      }	
				})
				.state('companyReceipts', {   
					 url: '/companyReceipts',
					 templateUrl: 'views/master/UnitMaster/billingMaster/company-receipts.html',
					 controller: "companyReceiptsController",
					 title: 'Company Receipts',
				})
				.state('webAppointment', {   
					 url: '/webAppointment',
					 templateUrl: 'views/AppointmentScheduling/web-appointment.html',
					 controller: "webAppointmentController",
					 title: 'Web Appointment',
				})
				.state('duplicate-tab', {   
					 url: '/duplicate-tab',
					 templateUrl: 'views/AppointmentScheduling/duplicate-tab.html',
					 controller: "duplicateTabController",
					 title: 'Duplicate Tab ',
				})
					.state('medicineInfo', {   
					 url: '/medicineInfo',
					 templateUrl: 'views/AppointmentScheduling/medicineInfo.html',
					 controller: "masterController",
					 title: 'Medicine Info',
				})
				.state('webAppointmentBooking', {   
					 url: '/webAppointmentBooking/:orgId/:unitId',
					 templateUrl: 'views/AppointmentScheduling/web-appointment-booking.html',
					 controller: "webAppController",
					 title: 'Web Appointment',
				})
				
				.state('bookAppointment', {
					 url: '/bookAppointment',
					 templateUrl: 'views/AppointmentScheduling/book-appointment.html',
					 controller: "bookAppointmentController",
					 title: 'Book Appointment',
					 params: {
						 webAppDetailObj : {}
					 }
					})
					
				.state('webAppointmentBookingConfirmation', {
					 url: '/webAppointmentBookingConfirmation',
					 templateUrl: 'views/AppointmentScheduling/web-appointment-booking-confirmation.html',
					 controller: "webAppointmentBookingConfirmationController",
					 title: 'Web Appointment Confirmation',
					 params: {
						 webAppConfirmationObj : {}
					 }
					})
					
				.state('addNewCompanyReceipts', {   
					 url: '/addNewCompanyReceipts',
					 templateUrl: 'views/master/UnitMaster/billingMaster/add-new-company-receipts.html',
					 controller: "addNewCompanyReceiptsController",
					 title: 'Add New Company Receipts',
					 params: 
				      {
						 miscellaneousObj:{} // default value
				      }	
				})
				.state('formulationTypeToDosageMapper', {    /**
																 * ****** added
																 * by Pranita
																 * 23.3.2020*******
																 */
					 url: '/formulationTypeToDosageMapper',
					 templateUrl: 'views/master/GlobalMaster/formulation-type-to-dosage-mapper.html',
					 controller: "formulationTypeToDosageTypeMapperController",
					 title: 'Formulation Type to Dosage Mapper',
				})
				.state('fundInflow', {/**
										 * ****** added by Pranita
										 * 16.1.2020*******
										 */
					 url: '/fundInflow',
					 templateUrl: 'views/master/UnitMaster/fundManagement/fund-inflow.html',
					 controller: "fundInflowController",
					 title: 'Fund Inflow',
				})
				
				.state('nursingOrderForNurse', {    /**
													 * ****** added by Pranita
													 * 1.12.2019*******
													 */
					 url: '/nursingOrderForNurse',
					 templateUrl: 'views/Nursing/nursing-order-for-nurse.html',
					 controller: "MasterController",
					 title: 'Nursing Order for Nurse',
						 
				})
				.state('dialysisRecord', {    /**
												 * ****** added by Pranita
												 * 1.12.2019*******
												 */
					 url: '/dialysisRecord',
					 templateUrl: 'views/master/hemodialysis/dialysis-record.html',
					 controller: "hemodialysisRecordController",
					 title: 'Hemodialysis(HD) Record',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 872);
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
				   		 }
				})
				.state('hemodialysisWorkload', {    /**
													 * ****** added by Pranita
													 * 27.11.2019*******
													 */
					 url: '/hemodialysisWorkload',
					 templateUrl: 'views/master/hemodialysis/hemodailysis-workload.html',
					 controller: "hemodialysisWorkloadController",
					 title: 'Hemodialysis Workload',
					 resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 871);
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
				   		 }
				})
				.state('OTreservationCancellation', {/**
														 * ****** added by
														 * Pranita
														 * 29.11.2019*******
														 */
					 url: '/OTreservationCancellation',
					 templateUrl: 'views/master/TheatreManagement/OT-reservation-cancellation.html',
					 controller: "OTreservationCancellationController",
					 title: 'OT Reservation WorkList',
					})
				.state('deatilsWorkorderApproval', {    /**
														 * ****** added by
														 * Pranita
														 * 24.2.2020*******
														 */
					 url: '/deatilsWorkorderApproval',
					 templateUrl: 'views/procurement/workOrder/details-work-order-approval.html',
					 controller: "addNewWorkorderApprovalDetailsController",
					 title: 'Details Work Order Approval',
					 params: 
						{
						 resultworkOrApprovalderDetailsSaveCommonObj: {},
						 resultworkOrApprovalderDetailsUpdateCommonObj:{},
						 resultworkOrApprovalderDetailsReadOnlyCommonObj:{}
						}
				})
				.state('workOrderApproval', {    /**
													 * ****** added by Pranita
													 * 24.2.2020*******
													 */
					 url: '/workOrderApproval',
					 templateUrl: 'views/procurement/workOrder/work-order-approval.html',
					 controller: "addNewWorkorderApprovalListController",
					 title: 'Work Order Approval',
				})
				.state('workOrderCompletion', {    /**
													 * ****** added by Pranita
													 * 24.2.2020*******
													 */
					 url: '/workOrderCompletion',
					 templateUrl: 'views/procurement/workOrder/work-order-completion.html',
					 controller: "workOrderCompletionController",
					 title: 'Work Order Completion',
				})
				.state('workOrderCompletionDetails', {    /**
													 * ****** added by Pranita
													 * 24.2.2020*******
													 */
					 url: '/workOrderCompletionDetails',
					 templateUrl: 'views/procurement/workOrder/details-work-order-completion.html',
					 controller: "workOrderCompletionDetailsController",
					 title: 'Work Order Completion / Invoice',
					 params: 
						{
						 resultworkOrApprovalderDetailsSaveCommonObj: {},
						 resultworkOrApprovalderDetailsUpdateCommonObj:{},
						 resultworkOrApprovalderDetailsReadOnlyCommonObj:{}
						}
				})
				  /** ****** added by Pranita 26.2.2020 demo******* */
				.state('workOrderSettlement', {  
					 url: '/workOrderSettlement',
					 templateUrl: 'views/procurement/workOrder/work-order-Settlement.html',
					 controller: "workOrderSettlementController",
					 title: 'Work Order Settlement',
				})
				
				.state('deatilsWorkOrderSettlement', {    /**
															 * ****** added by
															 * Pranita
															 * 05.3.2020*******
															 */
					 url: '/deatilsWorkOrderSettlement',
					 templateUrl: 'views/procurement/workOrder/details-work-order-settlement.html',
					 controller: "addNewWorkorderApprovalDetailsSettlementController",
					 title: 'Details Work Order Settlement',
					 params: 
						{
						 resultworkOrApprovalderDetailsSaveCommonObj: {},
						 resultworkOrApprovalderDetailsUpdateCommonObj:{},
						 resultworkOrApprovalderDetailsReadOnlyCommonObj:{}
						}
				})
				.state('addNewNabhReporting', {
		url: '/addNewNabhReporting',
		templateUrl: 'views/master/UnitMaster/organizationMaster/add-new-nabh-reporting.html',
		controller: "addNewNABHReportingController", 
		title: 'NABH Reporting',
				})
					.state('editReportedNabh', {
		url: '/editReportedNabh',
		templateUrl: 'views/master/UnitMaster/organizationMaster/edit-reported-nabh.html',
		controller: "editReportedNabhController", 
		title: 'Reported NABH',
				})
				
				/** ******** OT SCREEN ********* */
				.state('OTbillOfMaterial', {
					 url: '/OTbillOfMaterial',
					 templateUrl: 'views/master/TheatreManagement/OT-bill-of-material.html',
					 controller: "billOfMaterialController",
					 title: 'OT Bill of Material',
					})
					
				.state('billOfMaterialMaster', {
					 url: '/billOfMaterialMaster',
					 templateUrl: 'views/master/TheatreManagement/bill-of-material-master.html',
					 controller: "billOfMaterialMasterController",
					 title: 'Bill of Material',
					})
				.state('OTreservation', {
					 url: '/OTreservation',
					 templateUrl: 'views/master/TheatreManagement/OT-reservation.html',
					 controller: "OTreservationController",
					 title: 'OT Reservation',
					 params: {
						 commonObj : {}
					 }
					})
					.state('NewOTreservation', {
					 url: '/NewOTreservation',
					 templateUrl: 'views/master/TheatreManagement/New-OT-reservation.html',
					 controller: "NewOTreservationController",
					 title: 'OT Reservation',
				
					})
					
					.state('OTchecklist', {
					 url: '/OTchecklist',
					 templateUrl: 'views/master/TheatreManagement/OT-checklist.html',
					 controller: "OTchecklistController",
					 title: 'OT Checklist',
					 params: {
						 checklistObj : {}
					 }
					})
					.state('OTchecklistDetails', {
					 url: '/OTchecklistDetails',
					 templateUrl: 'views/master/TheatreManagement/OT-checklist-details.html',
					 controller: "OTchecklistDetailsController",
					 title: 'OT Checklist',
					 params: {
						 checklistDetailsObj : {}
					 }
					})
					
				.state('responsiveIPDCoversheet', {    /**
														 * ****** added by
														 * Pranita
														 * 3.1.2020*******
														 */
					 url: '/responsiveIPDCoversheet',
					 templateUrl: 'views/clinicalWorkStation/responsive-ipd-CoverSheet.html',
					 controller: "MasterController",
					 title: 'IPD Coversheet',
						 
				})
				.state('procedureWorkbench', {    /**
													 * ****** added by Pranita
													 * 19.2.2020*******
													 */
					 url: '/procedureWorkbench',
					 templateUrl: 'views/master/diagnosticProcedure/procedure-workbench.html',
					 controller: "procedureWorkbenchController",
					 title: 'Clinical Service Workbench',
					    resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 874);
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
				   		 }
						 
				})

				.state('diagnosticProcedureHandover', {    /**
															 * ****** added by
															 * Pranita
															 * 21.2.2020*******
															 */
					 url: '/diagnosticProcedureHandover',
					 templateUrl: 'views/master/diagnosticProcedure/diagnostic-procedure-handover.html',
					 controller: "procedureWorklistForPrintingController",
					 title: 'Diagnostic Clinical Service Handover',
					    resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 877);
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
				   		 }
				})
				.state('diagnosticProcedureResultValidation', {    /**
																	 * ******
																	 * added by
																	 * Pranita
																	 * 21.2.2020*******
																	 */
					 url: '/diagnosticProcedureResultValidation',
					 templateUrl: 'views/master/diagnosticProcedure/diagnostic-procedure-result-validation.html',
					 controller: "diagnosticProcedureResultValidationController",
					 title: 'Diagnostic Procedure Result Validation',
					    resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 876);
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
				   		 }
				})
				.state('diagnosticProcedureResultEntry', {    /**
																 * ****** added
																 * by Pranita
																 * 21.2.2020*******
																 */
					 url: '/diagnosticProcedureResultEntry',
					 templateUrl: 'views/master/diagnosticProcedure/diagnostic-procedure-result-entry.html',
					 controller: "procedureResultEntryController",
					 title: 'Diagnostic Clinical Service Result Entry',
					    resolve : {

				   			data: function($q, $state, $timeout) {
				   		        var deferred = $q.defer();
				   		
				   		        $timeout(function() {
				   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				   				 	.modules.filter(function(item){
				   	            	   return (item.id == 875);
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
				   		 }
				})
				.state('diagnosticProcedureReport', {    /**
															 * ****** added by
															 * Pranita
															 * 21.2.2020*******
															 */
					 url: '/diagnosticProcedureReport',
					 templateUrl: 'views/master/diagnosticProcedure/diagnostic-procedure-report.html',
					 controller: "daignosisProcedureReportController",
// title: 'Diagnostic Procedure Report',
					 params:{
						 procedureReportObject :{}
					 }
						 
				})
				// [**************** END ********************]
				.state('drAdvance', {
				 url: '/drAdvance',
				 templateUrl: 'views/master/DoctorShare/dr-advanced.html',
				 controller: "drAdvanceController",
				 title: 'Doctor Advanced',
				})
				.state('drAdvancedApproval', {
				 url: '/drAdvancedApproval',
				 templateUrl: 'views/master/DoctorShare/dr-advanced-approval.html',
				 controller: "drAdvancedApprovalController",
				 title: 'Doctor Advance Approval',
				})
				.state('pesapalProcessPage', {
					url: '/pesapalProcessPage',
					templateUrl: 'views/common/pesapal-success-page.html',
					controller: "pesapalSuccessPageController",
					title: 'Pesapal',
					})
					
				.state('badDebtsList', {
					 url: '/badDebtsList',
					 templateUrl: 'views/master/UnitMaster/billingMaster/bed-debit-list.html',
					 controller: "badDebitListController",
					 title: 'Bad Debts List',
    	 })
    	 
    	 
    	 .state('badDebtsList.groupDetails',
		  	       {url : '/groupDetails',
		   	        views :{
		    	          '@badDebtsList' :{
		    	            templateUrl : 'views/master/UnitMaster/billingMaster/common/group-details-bad-debit.html',
		    	            title: 'Bad Debts List',
		    	            // controller : 'billListController',
		    	            }},
		    	            title: 'Bad Debts List',
		  	       	})
		  	       	
		  .state('badDebtsList.serviceDetails',
		  	       {url : '/serviceDetails',
		   	        views :{
		    	          '@badDebtsList' :{
		    	            templateUrl : 'views/master/UnitMaster/billingMaster/common/service-details-bad-debit.html',
		    	            title: 'Bad Debts List',
		    	            // controller : 'MasterController',
		    	            }},
		    	            title: 'Bad Debts List',
		  	       	})
		  	 .state('badDebtsList.packageDetails',
		  	       {url : '/packageDetails',
		   	        views :{
		    	          '@badDebtsList' :{
		    	            templateUrl : 'views/master/UnitMaster/billingMaster/common/package-details-bad-debit.html',
		    	            title: 'Bad Debts List',
		    	            // controller : 'MasterController',
		    	            }},
		    	            title: 'Bad Debts List',
		  	       	})
		  	       	
		  	.state('badDebtsList.groupDetailsPrePost',
		  	       {url : '/groupDetailsPrePost',
		   	        views :{
		    	          '@badDebtsList' :{
		    	            templateUrl : 'views/master/UnitMaster/billingMaster/common/group-details-pre-post-bad-debit.html',
		    	            title: 'Bad Debts List',
		    	            // controller : 'billListController',
		    	            }},
		    	            title: 'Bad Debts List',
		  	       	}) 	
		  	       	    	 
    	 	.state('billCancellation', {
					 url: '/billCancellation',
					 templateUrl: 'views/master/UnitMaster/billingMaster/bill-cancellation.html',
					 controller: "billCancellationController",
					 title: 'Bill/Receipt Cancellation',
    	 })
    	 
    	/*
		 * .state('addendumBill', { url: '/addendumBill', templateUrl:
		 * 'views/master/UnitMaster/billingMaster/addendum-bill.html',
		 * controller: "addendumBillListController", title: 'Addendum Bill', })
		 * 
		 * .state('addendumBill.groupDetails', {url : '/groupDetails', views :{
		 * '@addendumBill' :{ templateUrl :
		 * 'views/master/UnitMaster/billingMaster/common/group-details-bill-cancellation-addendum.html', //
		 * title: 'Bill Cancellation', //controller : 'billListController', }},
		 * title: 'Bill/Receipt Cancellation', })
		 * 
		 * .state('addendumBill.serviceDetails', {url : '/serviceDetails', views :{
		 * '@addendumBill' :{ templateUrl :
		 * 'views/master/UnitMaster/billingMaster/common/service-details-bill-cancellation-addendum.html', //
		 * title: 'Bill Cancellation', //controller : 'MasterController', }},
		 * title: 'Bill/Receipt Cancellation', })
		 * .state('addendumBill.packageDetails', {url : '/packageDetails', views :{
		 * '@addendumBill' :{ templateUrl :
		 * 'views/master/UnitMaster/billingMaster/common/package-details-bill-cancellation-addendum.html', //
		 * title: 'Bad Debts List', //controller : 'MasterController', }},
		 * title: 'Bill/Receipt Cancellation', })
		 * 
		 * .state('addendumBill.groupDetailsPrePost', {url :
		 * '/groupDetailsPrePost', views :{ '@addendumBill' :{ templateUrl :
		 * 'views/master/UnitMaster/billingMaster/common/group-details-pre-post-bad-debit-addendum.html', //
		 * title: 'Bill Cancellation', //controller : 'billListController', }},
		 * title: 'Bill/Receipt Cancellation', })
		 */
		  	       	
    	 
    	 
    	 .state('billCancellation.groupDetails',
		  	       {url : '/groupDetails',
		   	        views :{
		    	          '@billCancellation' :{
		    	            templateUrl : 'views/master/UnitMaster/billingMaster/common/group-details-bill-cancellation.html',
// title: 'Bill Cancellation',
		    	            // controller : 'billListController',
		    	            }},
		    	            title: 'Bill/Receipt Cancellation',
		  	       	})
		  	       	
		  .state('billCancellation.serviceDetails',
		  	       {url : '/serviceDetails',
		   	        views :{
		    	          '@billCancellation' :{
		    	            templateUrl : 'views/master/UnitMaster/billingMaster/common/service-details-bill-cancellation.html',
// title: 'Bill Cancellation',
		    	            // controller : 'MasterController',
		    	            }},
		    	            title: 'Bill/Receipt Cancellation',
		  	       	})
		  	 .state('billCancellation.packageDetails',
		  	       {url : '/packageDetails',
		   	        views :{
		    	          '@billCancellation' :{
		    	            templateUrl : 'views/master/UnitMaster/billingMaster/common/package-details-bill-cancellation.html',
// title: 'Bad Debts List',
		    	            // controller : 'MasterController',
		    	            }},
		    	            title: 'Bill/Receipt Cancellation',
		  	       	})
		  	       	
		  	.state('billCancellation.groupDetailsPrePost',
		  	       {url : '/groupDetailsPrePost',
		   	        views :{
		    	          '@billCancellation' :{
		    	            templateUrl : 'views/master/UnitMaster/billingMaster/common/group-details-pre-post-bad-debit.html',
// title: 'Bill Cancellation',
		    	            // controller : 'billListController',
		    	            }},
		    	            title: 'Bill/Receipt Cancellation',
		  	       	}) 
		  	       	
		  	       	.state('workOrderCreation', {
						url: '/workOrderCreation',
						templateUrl: 'views/procurement/workOrder/work-order-creation.html',
						controller: "addNewWorkorderLabourListController",
						title: 'Work Order Creation',
					})

						 .state('addNewWorkOrderCreation', {
							url: '/addNewWorkOrderCreation',
							templateUrl: 'views/procurement/workOrder/add-new-workorder-creation.html',
							controller: "addNewWorkorderLabourController",
							title: 'Work Order Creation',
							params: 
							{
								resultworkOrderDetailsCommonObj: {},
								resultworkOrderReadOnlyDetailsCommonObj:{}
							} 
						})
						
					    .state('complaintsTab', {
					         url: '/complaintsTab',
					         templateUrl: 'views/OPD/CoverSheet/opd-coversheet-new.html',
					         controller: "complaintsTabController",
					         title: 'OPD Coversheet',
					         params: {
					                obj: {}
					            }
					    })
					    
					    .state('medicalHistoryTab', {
					         url: '/medicalHistoryTab',
					         templateUrl: 'views/OPD/CoverSheet/opd-coversheet-new.html',
					         controller: "medicalHistoryTabController",
					         title: 'OPD Coversheet',
					         params: {
					                obj: {}
					            }
					    })
					        
					    .state('drugHistoryTab', {
					         url: '/drugHistoryTab',
					         templateUrl: 'views/OPD/CoverSheet/opd-coversheet-new.html',
					         controller: "drugHistoryTabController",
					         title: 'OPD Coversheet',
					         params: {
					                obj: {}
					            }
					    })
					    
					    .state('socialHistoryTab', {
					         url: '/socialHistoryTab',
					         templateUrl: 'views/OPD/CoverSheet/opd-coversheet-new.html',
					         controller: "socialHistoryTabController",
					         title: 'OPD Coversheet',
					         params: {
					                obj: {}
					            }
					    })
					    
					    .state('allergiesTab', {
					         url: '/allergiesTab',
					         templateUrl: 'views/OPD/CoverSheet/opd-coversheet-new.html',
					         controller: "allergiesTabController",
					         title: 'OPD Coversheet',
					         params: {
					                obj: {}
					            }
					    })
					    
					    .state('vitalsTab', {
					         url: '/vitalsTab',
					         templateUrl: 'views/OPD/CoverSheet/opd-coversheet-new.html',
					         controller: "vitalsTabController",
					         title: 'OPD Coversheet',
					         params: {
					                obj: {}
					            }
					    })
					    
					    .state('examinationTab', {
					         url: '/examinationTab',
					         templateUrl: 'views/OPD/CoverSheet/opd-coversheet-new.html',
					         controller: "examinationTabController",
					         title: 'OPD Coversheet',
					         params: {
					                obj: {}
					            }
					    })
					    
					    .state('ordersTab', {
					         url: '/ordersTab',
					         templateUrl: 'views/OPD/CoverSheet/opd-coversheet-new.html',
					         controller: "ordersTabController",
					         title: 'OPD Coversheet',
					         params: {
					                obj: {}
					            }
					    })
					    
					    .state('diagnosisTab', {
					         url: '/diagnosisTab',
					         templateUrl: 'views/OPD/CoverSheet/opd-coversheet-new.html',
					         controller: "diagnosisTabController",
					         title: 'OPD Coversheet',
					         params: {
					                obj: {}
					            }
					    })
					    
					    .state('notesTab', {
					         url: '/notesTab',
					         templateUrl: 'views/OPD/CoverSheet/opd-coversheet-new.html',
					         controller: "notesTabController",
					         title: 'OPD Coversheet',
					         params: {
					                obj: {}
					            }
					    })
					    
					    .state('adviceTab', {
					         url: '/adviceTab',
					         templateUrl: 'views/OPD/CoverSheet/opd-coversheet-new.html',
					         controller: "adviceTabController",
					         title: 'OPD Coversheet',
					         params: {
					                obj: {}
					            }
					    })
					    
					    .state('follwUpTab', {
					         url: '/follwUpTab',
					         templateUrl: 'views/OPD/CoverSheet/opd-coversheet-new.html',
					         controller: "follwUpTabController",
					         title: 'OPD Coversheet',
					         params: {
					                obj: {}
					            }
					    })
					    
					    .state('attachToCasesheetTab', {
					         url: '/attachToCasesheetTab',
					         templateUrl: 'views/OPD/CoverSheet/opd-coversheet-new.html',
					         controller: "attachToCasesheetTabController",
					         title: 'OPD Coversheet',
					         params: {
					                obj: {}
					            }
					    })
					    
					    .state('letterTab', {
					         url: '/letterTab',
					         templateUrl: 'views/OPD/CoverSheet/opd-coversheet-new.html',
					         controller: "letterTabController",
					         title: 'OPD Coversheet',
					         params: {
					                obj: {}
					            }
					    })
					    
					    .state('medicinesAddSub', {
					        url: '/medicinesAddSub',
					        templateUrl: 'views/PharmacyInventory/StockAdjustment/medicinesAddSub.html',
					        controller: "medicinesAddSubController",
					        title: 'Medicines Issue And Return',
					         params: {
					                obj: {}
					            }
					    })
					    
					    .state("scheduledWorklist",
					    		{
							   	 url: '/scheduledWorklist',
								 templateUrl: 'views/master/UnitMaster/billingMaster/scheduled-worklist.html',
								 controller: "cyclicScheduledWorklistController",
								 title: 'Scheduled Worklist',
							    })

						.state('sanctionAllocation', {
							 url: '/sanctionAllocation',
							 templateUrl: 'views/master/UnitMaster/fundManagement/saction-allocation-request-form.html',
							 controller: "fundSanctionController",
							 title: 'Sanction Allocation Request',
						})
						
					.state('schedulingWorking', {
				 url: '/schedulingWorking',
				 templateUrl: 'views/master/UnitMaster/billingMaster/scheduling-working.html',
				 controller: "schedulingWorkingController",
				 title: 'Scheduling WorkList',
			     })
			     
			     .state('addNewIncidentReporting', {
					 url: '/addNewIncidentReporting',
					 templateUrl: 'views/incidentReporting/add-new-incident-reporting.html',
					  controller: "addNewIncidentReportingController", 
					 title: 'Incident Reporting',
				     })
				     
				     .state('EditReportedIncident', {
					 url: '/EditReportedIncident',
					 templateUrl: 'views/incidentReporting/edit-reported-incident.html',
					 controller: "editReportedIncidentController", 
					 title: 'Reported Incident',
				     })

	}
	catch (e) {
		console.log(e);
		// TODO: handle exception
	}
						} ]);





// Promise Factory
// Code Added By Ganesh 04.10.2017
angular.module('myApp').factory('promiseFactory', function($http, $q) {
	var promises = [];
	// var promise2;
	return {
		setPromises : function(URI1, URI2, methodType1, methodType2) {
			var deffered = $q.defer();
			var deffered1 = $q.defer();
			$http({
				method : methodType1,
				url : URI1
			}).success(function(data) {
				deffered.resolve(data);
			});// then(function(data) {return data;});
			$http({
				method : methodType2,
				url : URI2
			}).success(function(data) {
				deffered1.resolve(data);
			});
			promises = [];
			promises.push(deffered.promise);
			promises.push(deffered1.promise);
			return $q.all(promises);

		}
	}
});


	
/*
 * angular.module( "myApp" ).directive( 'input', function
 * bnModelWatchDirective() { // Return the directive configuration object.
 * return({ link: link, restrict: "A" }); // I bind the JavaScript events to the
 * view-model. function link( scope, element, attributes ) { // In this
 * approach, we're just going to watch the ngModel expression // for changes in
 * the same way we would watch any other scope-based // value for changes.
 * scope.$watch( attributes.ngModel, function( newValue, oldValue ) {
 * console.log( "ngModel value changed (A):", newValue ); alert('chhange: '+
 * newValue); } ); } } );
 */


angular.module("myApp").directive('input', function(){
	return {
		restrict: 'E',
		link: function (scope, element, attributes){
			// alert('fsd');
			 scope.$watch(
	                    attributes.ngModel,
	                    function( newValue, oldValue ) {
	                    	if(newValue != ''){
	                    		/*
								 * //alert('add'+ element); addClass(element,
								 * 'dirty')
								 */
	                    		$("input[type=text]").each(function(){
	                    			$(this).addClass('dirty');
	                    		})
	          		        }else{
	          		          /* removeClass(element, 'dirty') */
	          		        }
	                    }
	                );
		}
	}
});

// Code Added By Sharad 02.11.2019
// angular.module('myApp', []).filter('capitalize', function() { return
// capitalize } );

angular.module("myApp").filter('capitalize', function() {
  return function(input) {
    return (angular.isString(input) && input.length > 0) ? input.charAt(0).toUpperCase() + input.substr(1).toLowerCase() : input;
  }
});
angular.module("myApp").directive('select', function(){
	return {
		restrict: 'E',
		link: function (scope, element, attributes){
			// alert('fsd');
			 scope.$watch(
	                    attributes.ngModel,
	                    function( newValue, oldValue ) {
	                    	if(newValue != ''){
	                    		/*
								 * //alert('add'+ element); addClass(element,
								 * 'dirty')
								 */
	                    		$("select").each(function(){
	                    			$(this).addClass('dirty');
	                    		})
	          		        }else{
	          		          /* removeClass(element, 'dirty') */
	          		        }
	                    }
	                );
		}
	}
});

angular.module('app', ['ngRoute']);
angular.module("myApp").directive('textarea', function(){
	return {
		restrict: 'E',
		link: function (scope, element, attributes){
			// alert('fsd');
			 scope.$watch(
	                    attributes.ngModel,
	                    function( newValue, oldValue ) {
	                    	if(newValue != ''){	                    		
	                    		$("textarea").each(function(){
	                    			$(this).addClass('dirty');
	                    		})
	          		        }else{
	          		          
	          		        }
	                    }
	                );
		}
	}
});
angular.module("myApp").directive("moveNextOnMaxlength", function() {
    return {
        restrict: "A",
        link: function($scope, element) {
            element.on("input", function(e) {
                if(element.val().length == element.attr("maxlength")) {
                    var $nextElement = element.next();
                    if($nextElement.length) {
                        $nextElement[0].focus();
                    }
                }
            });
        }
    }
});

    angular.module('myApp').filter('makePositive', function() {
        return function(num) { return Math.abs(num); }
    });

// numbers only directive
angular.module("myApp").directive('numbersOnly', function () {
    return {
        require: 'ngModel',
        link: function (scope, element, attr, ngModelCtrl) {
            function fromUser(text) {
                if (text) {
                    var transformedInput = text.replace(/[^0-9]/g, '');

                    if (transformedInput !== text) {
                        ngModelCtrl.$setViewValue(transformedInput);
                        ngModelCtrl.$render();
                    }
                    return transformedInput;
                }
                return undefined;
            }            
            ngModelCtrl.$parsers.push(fromUser);
        }
    };
});

angular.module("myApp").filter('unique', function() {

	  // Take in the collection and which field
	  // should be unique
	  // We assume an array of objects here
	  // NOTE: We are skipping any object which
	  // contains a duplicated value for that
	  // particular key. Make sure this is what
	  // you want!
	  return function (arr, targetField) {

		  if(arr!=undefined){
	    var values = [],
	        i, 
	        unique,
	        l = arr.length, 
	        results = [],
	        obj;

	    // Iterate over all objects in the array
	    // and collect all unique values
	    for( i = 0; i < arr.length; i++ ) {

	      obj = arr[i];

	      // check for uniqueness
	      unique = true;
	      var v=0;
	      for( v = 0; v < values.length; v++ ){
	        if( obj[targetField] == values[v] ){
	          unique = false;
	        }
	      }

	      // If this is indeed unique, add its
	      // value to our values and push
	      // it onto the returned array
	      if( unique ){
	        values.push( obj[targetField] );
	        results.push( obj );
	      }

	    }
	    return results;
		 }else{
			 return '';
		 }
	  };
	})

angular.module("myApp").directive('chars', ['$timeout', function ($timeout) {

    /*
	 * RegEx Examples: - email: "0-9a-zA-Z@._\-" - numbers only: "0-9" - letters
	 * only: "a-zA-Z" Email Usage Example: <input type="text" name="email"
	 * ng-model="user.email" chars="0-9a-zA-Z@._\-" />
	 */
	/*
	 * <input type="text" chars="only-numbers" /> // Only Numbers <input
	 * type="text" chars="numbers" /> // Numbers + Spaces <input type="text"
	 * chars="only-letters" /> // Only Letters <input type="text"
	 * chars="letters" /> // Letters + Spaces <input type="text" chars="email" /> //
	 * Email Chars <input type="text" chars="alpha-numeric" /> // Alpha Numeric
	 * Chars + Spaces <input type="text" chars="latin-alpha-numeric" />
	 */    'use strict';
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function ($scope, $elem, attrs, $ctrl) {

            var regReplace,
                preset = {
                    'only-numbers': '0-9',
                    'numbers': '0-9\\s',
                    'only-letters': 'A-Za-z',
                    'letters': 'A-Za-z\\s',
                    'email': '\\wÑñ@._\\-',
                    'alpha-numeric': '\\w\\s',
                    'alpha-numeric-hypen': '\\w\\s\\-',
                    'latin-alpha-numeric': '\\w\\sÑñáéíóúüÁÉÍÓÚÜ´¨'
                },
                filter = preset[attrs.chars] || attrs.chars;

            $elem.on('input keyup change', function () {

                var val = $elem.val().toString();

                regReplace = new RegExp('[^' + filter + ']', 'ig');
                $ctrl.$setViewValue(val.replace(regReplace, ''));
                $timeout(function () {

                    $ctrl.$render();

                });

            });

        }
    };

}]);

angular.module('myApp').config(function($httpProvider) {
    $httpProvider.defaults.withCredentials = true;
});


Array.prototype.max = function() {
  return Math.max.apply(null, this);
};

Array.prototype.min = function() {
  return Math.min.apply(null, this);
};
angular.module("myApp").directive('myDatePicker', function () {
    return {
        restrict: 'A',
        require: '?ngModel',
        link: function (scope, element, attrs, ngModelController) {
  
            // Private variables
            var datepickerFormat = 'dd/mm/yyyy',
                momentFormat = 'DD/MM/YYYY',
                datepicker,
                elPicker;
  
            // Init date picker and get objects
			// http://bootstrap-datepicker.readthedocs.org/en/release/index.html
            datepicker = element.datepicker({
                autoclose: true,
                keyboardNavigation: false,
                todayHighlight: true,
                format: datepickerFormat
            });
            elPicker = datepicker.data('datepicker').picker;
  
            // Adjust offset on show
            datepicker.on('show', function (evt) {
                elPicker.css('left', parseInt(elPicker.css('left')) + +attrs.offsetX);
                elPicker.css('top', parseInt(elPicker.css('top')) + +attrs.offsetY);
            });
  
            // Only watch and format if ng-model is present
			// https://docs.angularjs.org/api/ng/type/ngModel.NgModelController
            if (ngModelController) {
                // So we can maintain time
                var lastModelValueMoment;
  
                ngModelController.$formatters.push(function (modelValue) {
                    //
                    // Date -> String
                    //
  
                    // Get view value (String) from model value (Date)
                    var viewValue,
                        m = moment(modelValue);
                    if (modelValue && m.isValid()) {
                        // Valid date obj in model
                        lastModelValueMoment = m.clone(); // Save date (so we
															// can restore time
															// later)
                        viewValue = m.format(momentFormat);
                    } else {
                        // Invalid date obj in model
                        lastModelValueMoment = undefined;
                        viewValue = undefined;
                    }
  
                    // Update picker
                    element.datepicker('update', viewValue);
  
                    // Update view
                    return viewValue;
                });
                
                
 
                ngModelController.$parsers.push(function (viewValue) {
                    //
                    // String -> Date
                    //
  
                    // Get model value (Date) from view value (String)
                    var modelValue,
                        m = moment(viewValue, momentFormat, true);
                    if (viewValue && m.isValid()) {
                        // Valid date string in view
                        if (lastModelValueMoment) { // Restore time
                            m.hour(lastModelValueMoment.hour());
                            m.minute(lastModelValueMoment.minute());
                            m.second(lastModelValueMoment.second());
                            m.millisecond(lastModelValueMoment.millisecond());
                        }
                        modelValue = m.toDate();
                    } else {
                        // Invalid date string in view
                        modelValue = undefined;
                    }
  
                    // Update model
                    return modelValue;
                });
  
                datepicker.on('changeDate', function (evt) {
                    // Only update if it's NOT an <input> (if it's an <input>
					// the datepicker plugin trys to cast the val to a Date)
                    if (evt.target.tagName !== 'INPUT') {
                        ngModelController.$setViewValue(moment(evt.date).format(momentFormat)); // $seViewValue
																								// basically
																								// calls
																								// the
																								// $parser
																								// above
																								// so
																								// we
																								// need
																								// to
																								// pass
																								// a
																								// string
																								// date
																								// value
																								// in
                        ngModelController.$render();
                    }
                });
            }
  
        }
    };
});

/** @Author Ganesh Chaudhari */
angular.module("myApp").directive('quantityCheck', function() {
	  return function(scope, element, attributes) {
	    scope.$watch(attributes.ngModel, function(newQuantity, oldQuantity) 
	    		{
	      if (newQuantity!=undefined && newQuantity!=null && newQuantity>0)
	    	  {
	    	  // element.blur();
	    	  // element.change();
	    	  }
	    });
	  };
	});

/** @Author Ganesh Chaudhari */
angular.module("myApp").directive('autoSelect', function($timeout) {
	  return function(scope, element, attributes) {
	    scope.$watch(attributes.ngModel,function(newBatch, oldBatch) 
	    		{
	      if (newBatch!=undefined && newBatch!=null)
	    	  {
	    	   // element.blur();
	    	  $timeout(element.change());
	    	  }
	    });
	  };
	});
/** @Author Sneha Dalave */
angular.module("myApp").directive("directiveWhenScrolled", function() {
	  return function(scope, elm, attr) {
	    var raw = elm[0];

	    elm.bind('scroll', function() {
	      if (raw.scrollTop + raw.offsetHeight >= raw.scrollHeight) {
	        scope.$apply(attr.directiveWhenScrolled);
	      }
	    });
	  };
	});

angular.module('myApp').directive('whenScrolled', function() {return function(scope, elm, attr) {
	var raw = elm[0];
		elm.bind('scroll', function() {
			if (raw.scrollTop + raw.offsetHeight >= raw.scrollHeight) {
			scope.$apply(attr.whenScrolled);
			}
		});
	};
});

/* Throttle added 100 */
angular.module('infinite-scroll', []).value('THROTTLE_MILLISECONDS', 100).directive('infiniteScroll', [
    '$rootScope', '$window', '$interval', 'THROTTLE_MILLISECONDS', function($rootScope, $window, $interval, THROTTLE_MILLISECONDS) {
        return {
            scope: {
                infiniteScroll: '&',
                infiniteScrollContainer: '=',
                infiniteScrollDistance: '=',
                infiniteScrollDisabled: '=',
                infiniteScrollUseDocumentBottom: '=',
                infiniteScrollListenForEvent: '@'
            },
            link: function(scope, elem, attrs) {
                var changeContainer, checkInterval, checkWhenEnabled, container, handleInfiniteScrollContainer, handleInfiniteScrollDisabled, handleInfiniteScrollDistance, handleInfiniteScrollUseDocumentBottom, handler, height, immediateCheck, offsetTop, pageYOffset, scrollDistance, scrollEnabled, throttle, unregisterEventListener, useDocumentBottom, windowElement,position,scrollPosition;
                windowElement = angular.element($window);
                scrollDistance = null;
                scrollEnabled = null;
                checkWhenEnabled = null;
                container = null;
                immediateCheck = true;
                useDocumentBottom = false;
                unregisterEventListener = null;
                checkInterval = false;
                position = null;
                scrollPosition = null;
                height = function(elem) {
                    elem = elem[0] || elem;
                    if (isNaN(elem.offsetHeight)) {
                        return elem.document.documentElement.clientHeight;
                    } else {
                        return elem.offsetHeight;
                    }
                };
                offsetTop = function(elem) {
                    if (!elem[0].getBoundingClientRect || elem.css('none')) {
                        return;
                    }
                    return elem[0].getBoundingClientRect().top + pageYOffset(elem);
                };
                pageYOffset = function(elem) {
                    elem = elem[0] || elem;
                    if (isNaN(window.pageYOffset)) {
                        return elem.document.documentElement.scrollTop;
                    } else {
                        return elem.ownerDocument.defaultView.pageYOffset;
                    }
                };
                handler = function() {
                    var containerBottom, containerTopOffset, elementBottom, remaining, shouldScroll, currentPosition;
                    currentPosition = container[0].scrollTop;
                    (position === null) && (position = currentPosition);
                    // console.log('currentPosition', currentPosition);
                    // console.log('position', position);

                    if(currentPosition > position){
                        scrollPosition = 0;
                    }else if(currentPosition === position){
                        scrollPosition = scrollPosition;
                    }else{
                        scrollPosition = 1;
                    }
                    position = currentPosition;
                    if(scrollPosition == null){
                        return;
                    }


                    // console.log('position', scrollPosition);
                    if (container === windowElement) {
                        // console.log("windowElement");
                        containerBottom = height(container) + pageYOffset(container[0].document.documentElement);
                        elementBottom = offsetTop(elem) + height(elem);
                        containerTopOffset = offsetTop(container);
                    } else {
                        if(scrollPosition){
                            // console.log('Up', scrollPosition);
                            containerBottom = 0;
                            containerTopOffset = 0;
                            if (offsetTop(container) !== void 0) {
                                containerTopOffset = offsetTop(container);
                            }
                            elementBottom = offsetTop(elem) - 56;
                        }else {
                            // console.log('Down', scrollPosition);
                            containerBottom = height(container);
                            containerTopOffset = 0;
                            if (offsetTop(container) !== void 0) {
                                containerTopOffset = offsetTop(container);
                            }
                            elementBottom = offsetTop(elem) - containerTopOffset + height(elem);
                        }
                    }
                    if (useDocumentBottom) {
                        elementBottom = height((elem[0].ownerDocument || elem[0].document).documentElement);
                    }
                    remaining = scrollPosition ? elementBottom - containerTopOffset   : elementBottom - containerBottom;

                    // console.log('scrollPosition ', remaining);
                    shouldScroll = remaining <= height(container) * scrollDistance + 1;

                    // console.log(shouldScroll);

                    translate = offsetTop(container) - offsetTop(elem);
                    console.log(offsetTop(container) - offsetTop(elem));

                    if (shouldScroll) {
                        checkWhenEnabled = true;
                        if (scrollEnabled) {
        container[0].children[0].style.transform = "translateY("+currentPosition+"px)";
                            if (scope.$$phase || $rootScope.$$phase) {
                                return scope.infiniteScroll({
                                    callback:scrollPosition
                                });
                            } else {
                                return scope.$apply(function (){
                                    scope.infiniteScroll({
                                        callback:scrollPosition
                                    });
                                });
                            }
                        }
                    } else {
                        if (checkInterval) {
                            $interval.cancel(checkInterval);
                        }
                        return checkWhenEnabled = false;
                    }
                };
                throttle = function(func, wait) {
                    var later, previous, timeout;
                    timeout = null;
                    previous = 0;
                    later = function() {
                        previous = new Date().getTime();
                        $interval.cancel(timeout);
                        timeout = null;
                        return func.call();
                    };
                    return function() {
                        var now, remaining;
                        now = new Date().getTime();
                        remaining = wait - (now - previous);
                        if (remaining <= 0) {
                            $interval.cancel(timeout);
                            timeout = null;
                            previous = now;
                            return func.call();
                        } else {
                            if (!timeout) {
                                return timeout = $interval(later, remaining, 1);
                            }
                        }
                    };
                };
                if (THROTTLE_MILLISECONDS != null) {
                    handler = throttle(handler, THROTTLE_MILLISECONDS);
                }
                scope.$on('$destroy', function() {
                    container.unbind('scroll', handler);
                    if (unregisterEventListener != null) {
                        unregisterEventListener();
                        unregisterEventListener = null;
                    }
                    if (checkInterval) {
                        return $interval.cancel(checkInterval);
                    }
                });
                handleInfiniteScrollDistance = function(v) {
                    return scrollDistance = parseFloat(v) || 0;
                };
                scope.$watch('infiniteScrollDistance', handleInfiniteScrollDistance);
                handleInfiniteScrollDistance(scope.infiniteScrollDistance);
                handleInfiniteScrollDisabled = function(v) {
                    scrollEnabled = !v;
                    if (scrollEnabled && checkWhenEnabled) {
                        checkWhenEnabled = false;
                        return handler();
                    }
                };
                scope.$watch('infiniteScrollDisabled', handleInfiniteScrollDisabled);
                handleInfiniteScrollDisabled(scope.infiniteScrollDisabled);
                handleInfiniteScrollUseDocumentBottom = function(v) {
                    return useDocumentBottom = v;
                };
                scope.$watch('infiniteScrollUseDocumentBottom', handleInfiniteScrollUseDocumentBottom);
                handleInfiniteScrollUseDocumentBottom(scope.infiniteScrollUseDocumentBottom);
                changeContainer = function(newContainer) {
                    if (container != null) {
                        container.unbind('scroll', handler);
                    }
                    container = newContainer;
                    if (newContainer != null) {
                        return container.bind('scroll', handler);
                    }
                };
                changeContainer(windowElement);
                if (scope.infiniteScrollListenForEvent) {
                    unregisterEventListener = $rootScope.$on(scope.infiniteScrollListenForEvent, handler);
                }
                handleInfiniteScrollContainer = function(newContainer) {
                    if ((newContainer == null) || newContainer.length === 0) {
                        return;
                    }
                    if (newContainer.nodeType && newContainer.nodeType === 1) {
                        newContainer = angular.element(newContainer);
                    } else if (typeof newContainer.append === 'function') {
                        newContainer = angular.element(newContainer[newContainer.length - 1]);
                    } else if (typeof newContainer === 'string') {
                        newContainer = angular.element(document.querySelector(newContainer));
                    }
                    if (newContainer != null) {
                        return changeContainer(newContainer);
                    } else {
                        throw new Error("invalid infinite-scroll-container attribute.");
                    }
                };
                scope.$watch('infiniteScrollContainer', handleInfiniteScrollContainer);
                handleInfiniteScrollContainer(scope.infiniteScrollContainer || []);
                if (attrs.infiniteScrollParent != null) {
                    changeContainer(angular.element(elem.parent()));
                }
                if (attrs.infiniteScrollImmediateCheck != null) {
                    immediateCheck = scope.$eval(attrs.infiniteScrollImmediateCheck);
                }
                return checkInterval = $interval((function() {
                    if (immediateCheck) {
                        handler();
                    }
                    return $interval.cancel(checkInterval);
                }));
            }
        };
    }
]);

if (typeof module !== "undefined" && typeof exports !== "undefined" && module.exports === exports) {
    module.exports = 'infinite-scroll';
}
   
   


