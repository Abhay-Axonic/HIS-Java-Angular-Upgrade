
(function() {
             "use strict";
         function favouriteMedicationTemplateController(
         $scope,
         $rootScope,
         $http,
         $cookies,
         CONSTANTS,
         GenericService,
         BillingGenericService,
         GenericServiceFileUpload,
         GenericServiceParamHeader,
         $filter,
         PagerService,
         $stateParams,
         $timeout,
         growl){

             var self = this;
            //Default Constructor
              $rootScope.loginpage = true;
             var cookieObject = $cookies.getObject('cookieObject');
             var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
             if (cookieObject == undefined) {
                $state.go('login');
                return;
             }
             $scope.unitId = cookieObject.unitId;
             $scope.organizationId = cookieObject.organizationId;
             $scope.userId = cookieObject.userId;
             $scope.storeId = cookieObject.storeId;
             if (cookieObjectAppConfigSecond != undefined) {
             $scope.dosageConfiguration = cookieObjectAppConfigSecond.dosage;
             }
             function initializeController() {

             }
            $scope.init = function(){

                $scope.medicationObj = {
                    itemId : "",
                    searchItem :"",
                    searchItemFre:"",
                    favorite_status : false,
                    roaId : '',
                    dose : '',
                    doseUnitId : '',
                    durationCount : "",
                    durationUnit:'Day',
                    instructionId : '',
                    frequencyId : '',
                    quantity : '',
                    uomTypeId : '',
                    conversions : '',
                    strength : '',
                    priorityId : 2,
                    isStat : false,
                    priorityCode: 'NR',
                    isGeneric:'N',
                    isInstruction:'N',
                    nonFormularyAdvice:'N',
                    rate:'',
                    prescribedDosageUnitList:[

                    ]

                };

                var commonObjInst = {
                    isMedication : 'Y',
                    organizationId : $scope.organizationId
                }

                var URI = BASE_URL + ROOT_URL
                        + GETMEDICATIONINSTRUCTIONS;

                BillingGenericService
                    .serviceAction(METHOD_POST, URI,
                            commonObjInst,
                            NOTIFICATION_MSG_STATUS_FALSE)
                    .then(function(response) {
                        console.log(response);
                        if (response.data.status == "success")
                            $scope.medicationInstructions = response.data.listObject;
                    });

            }

            $scope.init();

            $scope.initMedicationObj = function() {
                $scope.searchItemFre = "";
                $scope.searchItem = "";
                $scope.registrationNumber = "";
                $scope.saveBtnFlag = false;
                $scope.updateScope = true;
                $scope.updateBtnFlag = false;
                $scope.roa_options=[];
                 $scope.duplicateMedication = false;
                $scope.medicationObj = {
                    itemId : "",
                    searchItem : "",
                    formulationTypeId : '',
                    favorite_status : false,
                    medicationStartDate : new Date(),
                    roaId : '',
                    dose : '',
                    doseUnitId : '',
                    durationCount : '',
                    durationUnit : 'Day',
                    instructionId : '',
                    medicationEndDate : '',
                    frequencyId : '',
                    quantity : '',
                    uomTypeId : '',
                    conversions : '',
                    strength : '',
                    priorityId : '2',
                    priorityCode : 'NR',
                    isGeneric:'N',
                    isInstruction:'N',
                    nonFormularyAdvice:'N',
                    searchItemFre :"",
                    isStat : false,
                    favorite_status : false,
                    nonFormularyAdvice:'N',
                    prescribedDosageUnitList:[]

                };
            }

             $scope.searchGenericItems = function(searchKeyword){

                    $scope.genericList=[];
                    var unitId = $scope.unitId
                    var orgId = $scope.organizationId
                     var data={};
                    var Arr = searchKeyword.split("|");
                    searchKeyword = Arr[0];

                    var URI = BASE_URL + ROOT_URL + "/api/opd/getGenericListForGenericSearch" + "?unitId=" +
                        unitId + "&orgId=" + orgId + "&searchDrug=" + searchKeyword
                    return BillingGenericService.serviceAction(METHOD_GET,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
                        if(response.data.status == "success"){
                            $scope.genericList = response.data.listObject;
                            console.log("$scope.genericList",$scope.genericList);
                        }
                        return $scope.genericList;
                    });
             }


            $scope.getPriorityList=function(){
                var URI = BASE_URL + ROOT_URL+ "/adt/getActivePriorityList";
                GenericService.serviceAction("GET", URI, {})
                .then(function(response) {
                    $scope.priorityList = [];
                    $scope.priorityList = response.data.listObject;
                    console.log("$scope.priorityList",$scope.priorityList);
                });
            }

            $scope.getPriorityList();

			$scope.genericAddSubstitueList = [];
			$scope.getGenericAddSubstitue = function() {
				var data = {
				};

				var URI = BASE_URL + ROOT_URL+"/api/opd/getGenericListForBrandNameSearch?unitId="+$scope.unitId+"&orgId="+$scope.organizationId+"&genericId="+$scope.medicationObj.genericId;

				GenericServiceParamHeader
                    .serviceAction(METHOD_GET, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then( function(response) {
                        if (response.data.status == "success") {
                            $scope.genericAddSubstitueList = [];
                            $scope.genericAddSubstitueList = response.data.listObject;

                        }
                    });
			}

			$scope.genericAddSubstitueList = [];
			$scope.getGenericAddSubstitue = function() {
				var data = {
				};

				var URI = BASE_URL + ROOT_URL+"/api/opd/getGenericListForBrandNameSearch?unitId="+$scope.unitId+"&orgId="+$scope.organizationId+"&genericId="+$scope.medicationObj.genericId;

				GenericServiceParamHeader
						.serviceAction(METHOD_GET, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									if (response.data.status == "success") {
										$scope.genericAddSubstitueList = [];
										$scope.genericAddSubstitueList = response.data.listObject;

									}
								});
			}


            $scope.getGenericRadioSubstitute = function(item) {

                //$scope.genericAddSubstitueList = response.data.listObject;
                $scope.medicationObj.genericId = item.genericId;
                $scope.medicationObj.itemId = item.drugId;
                $scope.medicationObj.searchItem = item.medicationName +"| Qty :"+item.qty;
                var arrayDoa = item.genericName.split('|');
                var newDateDoa = "";
                newDateDoa = arrayDoa[1]+'|'+arrayDoa[0]+'|'+arrayDoa[2];
                $scope.medicationObj.searchGeneric=arrayDoa[0]+"|"+item.medicationName +"|"+arrayDoa[1];

            }
//
//            $scope.getFavoritesMedication = function() {
//                var data = {
//                    doctorId : $scope.doctorId,
//                    organizationId : $scope.organizationId,
//                    unitId : $scope.unitId
//                };
//
//                var URI = BASE_URL + ROOT_URL
//                        + GETFAVOURITEMEDICATION;
//
//                GenericServiceParamHeader
//                    .serviceAction(METHOD_POST, URI, data,
//                            NOTIFICATION_MSG_STATUS_FALSE)
//                    .then(
//                        function(response) {
//                            if (response.data.status == "success") {
//                                $scope.favouriteMedications = response.data.listObject;
//                                if($scope.favouriteMedications!=null && $scope.favouriteMedications.length>0){
//                                    angular.forEach($scope.favouriteMedications,function(value,index){
//                                        if(value.strengthDesc == null ){
//                                            value.strengthDesc = "";
//                                        }
//                                        if(value.doseDesc == null){
//                                            value.doseDesc = "";
//                                        }
//                                    });
//                                }
//                            }
//                        });
//            }


            $scope.getGenericAddSubstitute=function(){
                 var popup = angular.element('#substituteModal');
                popup.modal('hide');
                if($scope.medicationObj){

                    if($scope.medicationObj.genericName.split("|")[1]==$scope.medicationObj.searchGeneric.split("|")[1])
                    growl.warning("Please select medication ",
                            {
                                title : 'Warning!'
                            });
                    if($scope.medicationObj.genericName.split("|")[1] !=$scope.medicationObj.searchGeneric.split("|")[1]){
                        $scope.medicationObj.genericName = $scope.medicationObj.searchGeneric;
                    }

                    $rootScope.stopLoader();
                }
            }


            $scope.getActiveSpecialityList = function(){

                var commonObj = {
                    unitId : $scope.unitId,
                    organizationId : $scope.organizationId
                };

                var URI = BASE_URL + ROOT_URL
                        + GETACTIVESPECIALITY;
                BillingGenericService
                    .serviceAction(METHOD_POST, URI,
                            commonObj,
                            NOTIFICATION_MSG_STATUS_FALSE)
                    .then(
                        function(response) {
                            console.log(response);
                            if (response.data.status == "success")
                                $scope.specialityList = response.data.listObject;
                                $scope.specialityMasterListCopy = response.data.listObject;
                        });
            }
            $scope.getActiveSpecialityList();


            $scope.getDoctorBySpeciality = function(id) {
                var speciality = [];
                speciality.push(id);
                 $scope.doctorList = [];
                 $scope.doctorId = "";
                 $scope.favouriteMedications=[];
                $rootScope.startLoader();

                var data = {
                    organizationId : $scope.organizationId,
                    unitId : $scope.unitId,
                    specialityArray : speciality
                }
                console.log(data);
                var URI = BASE_URL + ROOT_URL
                        + GETDOCTORSLISTAGANISTSPECIALITIES;
                BillingGenericService
                    .serviceAction(METHOD_POST, URI, data,
                            NOTIFICATION_MSG_STATUS_FALSE)
                    .then(
                        function(response) {
                            console.log(response);
                            $scope.doctorList = [];
                            $scope.commonListDoctorModality = [];

                            // $rootScope.startLoader();
                            if (response.data.status == "success") {
                                $scope.doctorList = response.data.listObject;

                                $rootScope.stopLoader();
                            }
                        });
            }

            $scope.saveBtnFlag = false;
            $scope.saveOrUpdate = function(medication){
                $scope.saveBtnFlag = true;
                if($scope.validateData(medication)){

                    if(medication.favouriteMedicationId == undefined || medication.favouriteMedicationId == null){
                        $scope.saveMedicationFavourite(medication);
                    }
                    else{
                        $scope.updateFavouriteMedication(medication);
                    }
                }
            }


            $scope.validateData = function(medication){
                if(($scope.specialityId == undefined || $scope.specialityId == null || $scope.specialityId == '')){
                    $scope.saveBtnFlag = false;
                    growl.warning('Please Select Speciality..!');
                    return false;
                }
//                if(($scope.doctorId == undefined || $scope.doctorId == null || $scope.doctorId == '')){
//                    $scope.saveBtnFlag = false;
//                    growl.warning('Please Select Doctor..!');
//                    return false;
//                }
                if(medication.nonFormularyAdvice == "N"){
                    if(medication.searchItem == undefined || medication.searchItem == '' ||
                            medication.itemId == undefined || medication.itemId =='' ){
                        $scope.saveBtnFlag = false;
                        growl.warning('Please Enter Brand Name..!');
                        return false;
                    }
                    if(medication.searchGeneric == undefined || medication.searchGeneric == ''){
                        $scope.saveBtnFlag = false;
                        growl.warning('Please Enter Generic Name..!');
                        return false;
                    }
    //                if(medication.formulationCounterIndex == undefined || medication.formulationCounterIndex == ''){
    //                    $scope.saveBtnFlag = false;
    //                    growl.warning('Please Select Prescribed Dosage Unit!');
    //                    return false;
    //                }

                    if(medication.frequencyId == undefined || medication.frequencyId == ''){
                        $scope.saveBtnFlag = false;
                        growl.warning('Please Enter Frequency..!');
                        return false;
                    }
                    if(medication.durationCount == undefined || medication.durationCount == ''){
                        $scope.saveBtnFlag = false;
                        growl.warning('Please Enter Duration..!');
                        return false;
                    }
                    if(medication.durationUnit == undefined || medication.durationUnit == ''){
                        $scope.saveBtnFlag = false;
                        growl.warning('Please Enter Duration Unit..!');
                        return false;
                    }
                    if(medication.quantity == undefined || medication.quantity == ''){
                        $scope.saveBtnFlag = false;
                        growl.warning('Please Enter Quantity..!');
                        return false;
                    }
                    else{
                        return true;
                    }
                }
                else{
                     if($scope.dosageConfiguration == 'Y'){
                         if(medication.dose == null || medication.dose == undefined || medication.dose == '' || isNaN(medication.dose)){
                             growl.warning(
                                     'Please enter all mandatory fields.',
                                        {
                                            title : 'Warning!'
                                        });
                            return false;
                         }

                         if(medication.doseUnitId == null || medication.doseUnitId == undefined || medication.doseUnitId == '' || isNaN(medication.doseUnitId)){
                             growl.warning(
                                     'Please enter all mandatory fields.',
                                        {
                                            title : 'Warning!'
                                        });
                            return false;
                         }
                         else{
                            return true;
                         }
                     }
                     else{
                         return true;
                     }
                }
            }

            $scope.saveMedicationFavourite = function(medication) {
                console.log(medication);
                // return false;
                $rootScope.startLoader();
                var data = {
//                    itemId : medication.itemId,
                    doctorId : parseInt($scope.doctorId),
                    specialityId : parseInt($scope.specialityId),
                    createdBy : $scope.userId,
                    updatedBy : $scope.userId,
                    createdDate : moment(new Date()).format(
                            'YYYY-MM-DD HH:mm:ss'),
                    updatedDate : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
                    organizationId : $scope.organizationId,
                    unitId : $scope.unitId,
                };
                if(medication.nonFormularyAdvice == "N"){
                    data.itemId = medication.itemId;
                    data.isNonFormulary = 'N';
                }
                else{
                     data.itemName = medication.medicationName;
                     data.genericName = medication.genericName;
                     data.dose = medication.dose;
                     data.doseUnitId = medication.doseUnitId;
                     data.isNonFormulary = 'Y';
                }
                if(medication.favorite_status == false)
                {
                     data.roaId = medication.roaId;

                     data.orderQty = medication.quantity;
                     data.duration = medication.durationCount;
                     data.isMedicationStop = 'N';

                     data.prescriptionDate = moment(new Date()).format("DD-MM-YYYY HH:mm:ss");
                     data.strength = medication.strength;
                     data.dose = medication.dose;
                     data.doseUnitId = medication.doseUnitId;
                     data.durationUnit = medication.durationUnit;
                     data.frequencyId = medication.frequencyId;

                     data.status = 'A';
                     data.statflag = medication.isStat!=null?medication.isStat:false;

                     if(medication.instructionId != '' && medication.instructionId != undefined  && medication.instructionId != null ){
                           data.instructionId =  medication.instructionId;
                     }
                     else{
                            data.instructionDesc = medication.isInstructionText;
                     }

                    if(medication.prescribedDosageUnitList != undefined){
                        if(medication.prescribedDosageUnitList.length>0 && medication.formulationCounterIndex!="" && medication.formulationCounterIndex!=null
                                && medication.formulationCounterIndex!=undefined){
                             var list=$.grep(medication.prescribedDosageUnitList, function (obj) {
                                return medication.formulationCounterIndex === obj.counterIndex;
                            });
                             if(list.length>0){
                                 if(list[0].formulationFactorId==1){
                                     data.prescribedDose=medication.prescribeDose;
                                     data.prescribedDoseUnitId=medication.prescribedDoseUnitId;
                                     data.factorId=list[0].formulationFactorId;
                                     data.formulationDosageMappId=list[0].formulationDosageMappId;
                                 }else{
                                     data.prescribedDose=medication.prescribeDose;
                                     data.prescribedDoseUnitId=medication.prescribedDoseUnitId;
                                     data.factorId=list[0].formulationFactorId;
                                     data.formulationDosageMappId=list[0].formulationDosageMappId;
                                 }
                             }
                        }
                    }
                    else{
                        data.prescribedDose=medication.prescribeDose != undefined ? medication.prescribeDose :null;
                        data.prescribedDoseUnitId=medication.prescribedDoseUnitId!= undefined ? medication.prescribedDoseUnitId :null;
                    }
                }
                else
                {
                    data.favouriteMedicationId = medication.favouriteMedicationId;
                    data.status = 'I';
                }

                console.log("data", data);
                // return false;
                var URI = BASE_URL + ROOT_URL+ SAVEDOCTORFAVOURITEMEDICATION;


                GenericServiceParamHeader
                    .serviceAction(METHOD_POST, URI, data,
                            NOTIFICATION_MSG_STATUS_TRUE)
                    .then(
                        function(response) {
                            console.log("d", response);
                            if (response.data.status == "success") {
                                 $scope.saveBtnFlag = false;
//                                  var medicationObj =  medication;
//                                  medicationObj["favorite_status"] =  !medicationObj["favorite_status"];
//                                  var indexOfMedication =  $scope.medicationDetails.indexOf(medication);
//                                  $scope.medicationDetails[indexOfMedication] = medicationObj;
                                  $scope.initMedicationObj();
                                  $scope.getFavoritesMedication();
                                  $rootScope.stopLoader();
                            }
                            else{
                                 $scope.saveBtnFlag = false;
//                                 growl.warning(response.data.message);
                            }

                        });
            }


            $scope.updateFavouriteMedication = function(medication) {
                $scope.saveBtnFlag = true;
                $rootScope.startLoader();
                var data = {
                    favouriteMedicationId : medication.favouriteMedicationId,
                    doctorId : parseInt($scope.doctorId),
                    specialityId : parseInt($scope.specialityId),
                    updatedBy : $scope.userId,
                    updatedDate : moment(new Date()).format(
                            'YYYY-MM-DD HH:mm:ss'),
                    organizationId : $scope.organizationId,
                    unitId : $scope.unitId,
                };
                if(medication.isInstruction == 'Y') {
                    medication.instructionId = null;
                    medication.instructionDesc = null;
                }
                 data.roaId = medication.roaId;
                 data.dose = medication.dose;
                 data.doseUnitId = medication.doseUnitId;
                 data.orderQty = medication.quantity;
                 data.duration = medication.durationCount;
                 data.isMedicationStop = 'N';
                 data.isNonFormulary = 'N';
                 data.strength = medication.strength;
                 data.durationUnit = medication.durationUnit;
                 data.frequencyId = medication.frequencyId;
                 data.status = 'A';
                 data.statflag = medication.isStat!=null?medication.isStat:false;

                 if(medication.instructionId != '' && medication.instructionId != undefined  && medication.instructionId != null ){
                       data.instructionId =  medication.instructionId;
                 } else if(medication.isInstruction == 'Y') {
                      data.instructionDesc = medication.isInstructionText;
                      data.instructionText = medication.isInstructionText;

                 }else {
                      $scope.medicationObj.instructionDesc = "No instruction";
                 }

                 if(medication.nonFormularyAdvice == "N"){
                    data.itemId = medication.itemId;
                    data.isNonFormulary = 'N';
                 }
                 else{
                     data.itemName = medication.medicationName;
                     data.genericName = medication.genericName;
                     data.dose = medication.dose;
                     data.doseUnitId = medication.doseUnitId;
                     data.isNonFormulary = 'Y';
                 }
                if(medication.prescribedDosageUnitList != undefined){
                    if(medication.prescribedDosageUnitList.length>0 && medication.formulationCounterIndex!="" && medication.formulationCounterIndex!=null
                            && medication.formulationCounterIndex!=undefined){
                         var list=$.grep(medication.prescribedDosageUnitList, function (obj) {
                            return medication.formulationCounterIndex === obj.counterIndex;
                        });
                         if(list.length>0){
                             if(list[0].formulationFactorId==1){
                                 data.prescribedDose=medication.prescribeDose;
                                 data.prescribedDoseUnitId=medication.prescribedDoseUnitId;
                                 data.factorId=list[0].formulationFactorId;
                                 data.formulationDosageMappId=list[0].formulationDosageMappId;
                             }else{
                                 data.prescribedDose=medication.prescribeDose;
                                 data.prescribedDoseUnitId=medication.prescribedDoseUnitId;
                                 data.factorId=list[0].formulationFactorId;
                                 data.formulationDosageMappId=list[0].formulationDosageMappId;
                             }
                         }
                    }
                }
                else{
                    data.prescribedDose=medication.prescribeDose != undefined ? medication.prescribeDose :null;
                    data.prescribedDoseUnitId=medication.prescribedDoseUnitId!= undefined ? medication.prescribedDoseUnitId :null;
                }

                console.log("data", data);
                var URI = BASE_URL + ROOT_URL+ UPDATEDOCTORFAVOURITEMEDICATION;


                GenericServiceParamHeader
                    .serviceAction(METHOD_POST, URI, data,
                            NOTIFICATION_MSG_STATUS_TRUE)
                    .then(
                        function(response) {
                            console.log("d", response);
                            if (response.data.status == "success") {
                                  $scope.getFavoritesMedication();

                                  $scope.initMedicationObj();
                                  $rootScope.stopLoader();
                            }
                            else{
                                 $scope.saveBtnFlag = false;
//                                 growl.warning(response.data.message);
                            }

                        });
            }

            $scope.getValuesForUpdate = function(medication) {
                if(medication.isNonFormulary == 'Y'){
                    $scope.getOtherUpdateValues(medication);
                }
                else{

                console.log(medication);
                var index = "";
                var searchKeyword = medication.itemName.substring(0, 3);
               $scope.itemListMedication = [];
               var unitId = $scope.unitId
               var orgId = $scope.organizationId
               var Arr = searchKeyword.split("|");
               searchKeyword = Arr[0];
               var URI = BASE_URL + ROOT_URL
                       + GETMEDICATIONDETAILS + "?unitId="
                       + unitId + "&orgId=" + orgId
                       + "&searchDrug=" + searchKeyword
               return BillingGenericService
                       .serviceAction(METHOD_GET, URI, "",
                               NOTIFICATION_MSG_STATUS_FALSE)
                       .then(
                               function(response) {
                                   if (response.data.status == "success") {
                                       $scope.itemListMedication = response.data.listObject;
                                       if($scope.itemListMedication != undefined && $scope.itemListMedication.length>0){
                                           angular.forEach($scope.itemListMedication, function(item){
                                               if(parseInt(item.drugId) == parseInt(medication.itemId)){
                                                    item.durationCount = medication.durationCount;
                                                    item.durationUnit = medication.durationUnit;
                                                    item.instructionDesc = medication.instructionDesc;
                                                    item.isInstruction = medication.isInstruction;
                                                    item.instructionId = medication.instructionId;
                                                    item.instructionText = medication.instructionText;
                                                    item.quantity = medication.quantity;
                                                    $scope.addMedicationItemList(item, item.medicationName, item.medicationName, index);
//                                                    $timeout( function(){
                                                    $scope.getOtherUpdateValues(medication);
//                                                    },3000);
                                               }
                                           });
                                       }
                                       else{
                                               growl.warning("unable to update Item" );
                                       }
                                   }
                                   return $scope.itemListMedication;
                               });
               }

            }

            $scope.getOtherUpdateValues = function(medication){

                $scope.doctorId =$scope.doctorId.toString();
                $scope.specialityId = $scope.specialityId.toString();
                $scope.medicationObj.searchItem = medication.itemName;
                $scope.medicationObj.searchGeneric = medication.genericName;
                $scope.medicationObj.favouriteMedicationId = medication.favouriteMedicationId;
                $scope.medicationObj.roaId = medication.roaId != undefined && medication.roaId != null ? medication.roaId.toString():'';
                 $scope.medicationObj.quantity = medication.quantity;
                 $scope.medicationObj.durationCount = medication.durationCount;
                 $scope.medicationObj.durationUnit = medication.durationUnit;
                 $scope.medicationObj.isMedicationStop = medication.isMedicationStop;
                 $scope.medicationObj.nonFormularyAdvice = medication.isNonFormulary;
                 $scope.medicationObj.strength = medication.strength;
                 $scope.medicationObj.durationUnit = medication.durationUnit;
                 $scope.medicationObj.frequencyId = medication.frequencyId.toString();
                 $scope.medicationObj.isStat = medication.isStat!=null?medication.isStat:false;

                $scope.medicationObj.isInstruction='N';
                if(medication.instructionDesc != null){
                    $scope.medicationObj.instructionDesc=medication.instructionDesc;
                    $scope.medicationObj.isInstruction='N';
                    $scope.medicationObj.instructionId=medication.instructionId;
                }else{
                    $scope.medicationObj.isInstructionText=medication.instructionText;
                    $scope.medicationObj.isInstruction='Y';
                    $scope.medicationObj.instructionId='';
                }
                if(medication.instructionId == ''&& medication.isInstructionText == null){
                    $scope.medicationObj.isInstruction='N';
                }

                if(medication.prescribedDosageUnitList != undefined){
                    if(medication.prescribedDosageUnitList.length>0 && medication.formulationCounterIndex!="" && medication.formulationCounterIndex!=null
                            && medication.formulationCounterIndex!=undefined){
                         var list=$.grep(medication.prescribedDosageUnitList, function (obj) {
                            return medication.formulationCounterIndex === obj.counterIndex;
                        });
                         if(list.length>0){
                             if(list[0].formulationFactorId==1){
                                 $scope.medicationObj.prescribeDose=medication.prescribeDose;
                                 $scope.medicationObj.prescribedDoseUnitId=medication.prescribedDoseUnitId;
                                 $scope.medicationObj.factorId=list[0].formulationFactorId.toString();
                                 $scope.medicationObj.formulationDosageMappId=list[0].formulationDosageMappId.toString();
                             }else{
                                 $scope.medicationObj.prescribeDose=medication.prescribeDose;
                                 $scope.medicationObj.prescribedDoseUnitId=medication.prescribedDoseUnitId.toString();
                                 $scope.medicationObj.formulationFactorId=list[0].formulationFactorId.toString();
                                 $scope.medicationObj.formulationDosageMappId=list[0].formulationDosageMappId.toString();
                             }
                         }
                    }
                }
                else if(medication.isNonFormulary == 'Y' && medication.doseUnitId != undefined && medication.doseUnitId != null){
                    $scope.medicationObj.dose = medication.dose;
                    $scope.medicationObj.doseUnitId = medication.doseUnitId.toString();
                    $scope.medicationObj.roaId = medication.roaId != undefined && medication.roaId != null ? medication.roaId.toString():'';
                    $scope.medicationObj.medicationName = medication.itemName;
                    $scope.medicationObj.genericName = medication.genericName;
                    $scope.medicationObj.searchItemFre = medication.frequencyDesc;
                    $scope.medicationObj.frequencyId = medication.frequencyId;
                    $scope.frequency_id = medication.frequencyId;
                    $scope.frequency = $scope.medicationObj.searchItemFre;
                    $scope.noOfAdministrationTime = medication.noOfAdministrationTime;
                    $scope.freQua = medication.noOfAdministrationTime;
                    $scope.item={
                            frequency_id:$scope.frequency_id,
                            frequency:$scope.medicationObj.searchItemFre,
                            noOfAdministrationTime:$scope.noOfAdministrationTime
                    }
                    $scope.model=medicationObj.frequencyDesc;
                    $scope.label=medicationObj.frequencyDesc;
                    $scope.addFrequencyItemList($scope.item, $scope.model, $scope.label);
                }
                else{
                    $scope.medicationObj.prescribeDose=medication.prescribeDose != undefined ? medication.prescribeDose :null;
                    $scope.medicationObj.prescribedDoseUnitId=medication.prescribedDoseUnitId != undefined ? medication.prescribedDoseUnitId.toString() :null;
                }
            }

            $scope.searchMedicationItems = function(searchKeyword) {
                if (searchKeyword.length < 3) return;
                if ((!$scope.prevSearch || $scope.prevSearch != searchKeyword) && (searchKeyword.length == 3)) {
                    $scope.prevSearch = searchKeyword;
                    $scope.itemListMedication = [];
                    var unitId = $scope.unitId
                    var orgId = $scope.organizationId
                    var Arr = searchKeyword.split("|");
                    searchKeyword = Arr[0];
                    var URI = BASE_URL + ROOT_URL
                            + GETMEDICATIONDETAILS + "?unitId="
                            + unitId + "&orgId=" + orgId
                            + "&searchDrug=" + searchKeyword
                    return BillingGenericService
                            .serviceAction(METHOD_GET, URI, "",
                                    NOTIFICATION_MSG_STATUS_FALSE)
                            .then(
                                    function(response) {
                                        if (response.data.status == "success") {
                                            $scope.itemListMedication = response.data.listObject;
                                            console
                                                    .log(
                                                            "$scope.itemList ",
                                                            $scope.itemListMedication);
                                        }
                                        return $scope.itemListMedication;
                                    });
                }
                else {
                    if (!!$scope.itemListMedication && $scope.itemListMedication.length > 0 &&
                        $scope.itemListMedication[0].itemFound != undefined && !$scope.itemListMedication[0].itemFound) {
                        return $scope.itemListMedication;
                    } else {
                        return $filter('filter')($scope.itemListMedication, {
                            $: searchKeyword
                        });
                    }
                }
            }

            $scope.checkgenericObj = {};
            $scope.itemStrength="";
            $scope.prescribedDosageUnitList=[];

            $scope.addMedicationItemList = function($item,
                    $model, $label, idx) {
                console.log($item);
                $scope.itemStrength="";
                $rootScope.startLoader();
                var arr = $item.medicationName.split("|");
                var dggd =arr[1].split(":");
                $scope.genericQuantityCheck = true;
                if(dggd[1] == 0){
                    growl.warning("Stock not available for the selected Brand" );
                    if(dggd[1] == 0)
                    {
                        $scope.genericQuantityCheck = true;
                    }
                }
                $scope.medicationObj.isInstruction='N';
                $scope.medicationObj.quantity = $item.quantity;
                $scope.medicationObj.durationCount = $item.durationCount != undefined && $item.durationCount != null && $item.durationCount !== "" ? $item.durationCount : 0;
                $scope.medicationObj.durationUnit = $item.durationUnit != undefined && $item.durationUnit != null && $item.durationUnit !== "" ? $item.durationUnit : "Day";
                if($item.instructionDesc != null){
                    $scope.medicationObj.instructionDesc=$item.instructionDesc;
                    $scope.medicationObj.isInstruction='N';
                    $scope.medicationObj.instructionId=$item.instructionId;
                }else{
                    $scope.medicationObj.isInstructionText=$item.instructionText;
                    $scope.medicationObj.isInstruction='Y';
                    $scope.medicationObj.instructionId='';
                }
                if($item.instructionId == ''&& $item.isInstructionText == null){
                    $scope.medicationObj.isInstruction='N';
                }
                $scope.medicationObj.itemStrength=$item.strength+" "+($item.unitDesc!=null ? $item.unitDesc : " ") ;
                $scope.medicationObj.doseUnitId=$item.strengthUnitId;
                $scope.medicationObj.dose=$item.strength;
                $scope.medicationObj.isDoseCalculationApplicable = $item.isDoseCalculationApplicable;
                $scope.medicationObj.doseCalculationApplicableData = $item.doseCalculationApplicableData;
                $scope.medicationObj.itemId = $item.drugId;
                $scope.medicationObj.genericName = "";
                $scope.medicationObj.genericId = $item.genericId;
                $scope.medicationObj.searchGeneric= $item.genericName;
                $scope.medicationObj.prescribedDosageUnitList=[];
                $scope.medicationObj.formulationCounterIndex="";
                $scope.medicationObj.rate=$item.rate;
                $scope.medicationObj.cimsCode=$item.cimsCode;
                $scope.medicationObj.cimsType=$item.cimsType;
                $scope.cimsCode=$item.cimsCode;
                $scope.cimsType=$item.cimsType;

               $scope.favAvailableflag = false;

                if($scope.favouriteMedications!=undefined && $scope.favouriteMedications.length > 0){
                    var found = $scope.favouriteMedications.find(ar=>ar.itemId == $scope.medicationObj.itemId);
                       if(found==undefined)
                           {
                              console.log("testOrderSet" ,$scope.medicationDetails);
                           }
                       else{
                           $scope.favAvailableflag = true;
                       }
                }

                var formulationTypeId = $item.formulationTypeId;
                var URI = BASE_URL
                        + ROOT_URL
                        + "/api/opd/getRoaList"
                        + "?unitId="
                        + $scope.unitId
                        + "&orgId="
                        + $scope.organizationId
                        + "&formulationTypeId="
                        + formulationTypeId
                        + "&genericId="
                        + $item.genericId
                        + "&patientId=0";

                GenericServiceParamHeader
                        .serviceAction(METHOD_GET, URI)
                        .then(
                                function(response) {
                                    if (response.data.status == "success") {
                                        $rootScope.stopLoader();
                                        var responseList = response.data.listObject;
                                        $scope.medicaRoaList = responseList;
                                        if (response.data.object == 0) {
                                            if (response.data.listObject.length !== 0) {

                                                $scope.roa_options = responseList;
                                            }
                                            $scope.medicationObj.genericName = $item.genericName;
                                            $scope.medicationObj.formulationTypeId = $item.formulationTypeId;
                                            $scope.medicationObj.strength = $item.strength;
                                        } else {
                                            $scope.checkgenericObj = {
                                                genericName : $item.genericName,
                                                formulationTypeId : $item.formulationTypeId,
                                                strength : $item.strength,
                                            }
                                            angular.element( '#confirmAddItemPopup').modal('show');
                                        }
                                    }
                                    //get formuation dosage list
                                    var dataobj = {
                                            organizationId : $scope.organizationId,
                                            unitId : $scope.unitId,
                                            formulationTypeId:$item.formulationTypeId
                                    };
                                    if($scope.medicationObj.isDoseCalculationApplicable == true){
                                        $scope.medicationObj.prescribeDose=($scope.medicationObj.prescribeDose !=undefined && $scope.medicationObj.prescribeDose!= null && $scope.medicationObj.prescribeDose != "") ? $scope.medicationObj.prescribeDose: "";

                                    }else{
                                        $scope.medicationObj.prescribeDose="";
                                    }
                                    var URI = BASE_URL + ROOT_URL+ "/api/global/getFormulationTypeToDosageMapperListByFormulationTypeId";
                                    GenericService.serviceAction("POST", URI, dataobj).then(function(response) {
                                                $rootScope.stopLoader();
                                                $scope.medicationObj.prescribedDosageUnitList=[];
                                                $scope.counterFormulation=1;

                                                angular.forEach(response.data.listObject,function(value,index){
                                                    var unitObj={};
                                                    unitObj.formulationDosageFromUnit=value.fromConversionUnit;
                                                    unitObj.formulationDosageFromUnitId=value.fromConversionFactorUnit;
                                                    unitObj.formulationDosageToUnit=value.toConversionUnit;
                                                    unitObj.formulationDosageToUnitId=value.toConversionFactorUnit;
                                                    unitObj.counterIndex=$scope.counterFormulation++;
                                                    unitObj.formulationDosageFromConversionValue=value.fromConversionFactor;
                                                    unitObj.formulationDosageToConversionValue=value.toConversionFactor;
                                                    unitObj.formulationFactorId=value.factorId;
                                                    unitObj.formulationDosage=value.dosageForm;
                                                    unitObj.formulationDosageMappId=value.formulationDosageMappId;
                                                    $scope.medicationObj.prescribedDosageUnitList.push(unitObj);
                                                });

                                                angular.forEach(response.data.listObject,function(value,index){
                                                    var unitObj={};
                                                    if(value.factorId==1){
                                                        unitObj.formulationDosageFromUnit=value.toConversionUnit;
                                                        unitObj.formulationDosageFromUnitId=value.toConversionFactorUnit;
                                                        unitObj.formulationDosageToUnit=value.fromConversionUnit;
                                                        unitObj.formulationDosageToUnitId=value.fromConversionFactorUnit;
                                                        unitObj.counterIndex=$scope.counterFormulation++;
                                                        unitObj.formulationDosageFromConversionValue=value.toConversionFactor;
                                                        unitObj.formulationDosageToConversionValue=value.fromConversionFactor;
                                                        unitObj.formulationFactorId=value.factorId;
                                                        unitObj.formulationDosage=value.dosageForm;
                                                        unitObj.formulationDosageMappId=value.formulationDosageMappId;
                                                        $scope.medicationObj.prescribedDosageUnitList.push(unitObj);
                                                    }
                                                });
                                                var list=[];
                                                if($scope.medicationObj.prescribedDosageUnitList.length>0 && $scope.medicationObj.prescribedDosageUnitList[0].formulationFactorId==1){
                                                     list=$.grep($scope.medicationObj.prescribedDosageUnitList, function (obj) {
                                                        return $scope.medicationObj.doseUnitId === obj.formulationDosageFromUnitId;
                                                    });
                                                    if(list.length>0){
                                                        $scope.medicationObj.formulationCounterIndex=list[0].counterIndex;
                                                        $scope.medicationObj.prescriptionDoseUnit=list[0].formulationDosageFromUnit;
                                                    }
                                                }

                                            /*	if($scope.prescribedDosageUnitList.length>0){
                                                  $scope.medicationObj.prescribedDoseUnitId=$scope.medicationObj.doseUnitId;
                                                }*/
                                    });

                                });


                if($scope.favAvailableflag == true){
                    var idxFav = $scope.favouriteMedications.findIndex(item => item.itemId == $scope.medicationObj.itemId);

                    $scope.tempDurationCount=$scope.medicationObj.durationCount;
                    $scope.medicationObj = {};
                    $scope.medicationObj.rate=$item.rate;
                    $scope.medicationObj.itemId = $scope.favouriteMedications[idxFav].itemId;
                    $scope.medicationObj.doctorFavoriteId=$scope.favouriteMedications[idxFav].doctorFavoriteId;
                    $scope.searchItem = $scope.favouriteMedications[idxFav].itemName;
                    $scope.medicationObj.genericName = $scope.favouriteMedications[idxFav].genericName;
                    $scope.medicationObj.searchGeneric = $scope.favouriteMedications[idxFav].genericName;
                    $scope.medicationObj.genericId = $scope.favouriteMedications[idxFav].genericId;
                    $scope.medicationObj.formulationTypeId = $scope.favouriteMedications[idxFav].formulationTypeId;
                    $scope.medicationObj.isGeneric ='N';
                    $scope.medicationObj.nonFormularyAdvice ='N';
                    $scope.medicationObj.isInstruction='N';
                    $scope.medicationObj.priorityCode='NR';
                    $scope.medicationObj.searchItem =$scope.favouriteMedications[idxFav].itemName;;
                    $scope.medicationObj.searchItemFre = $scope.favouriteMedications[idxFav].frequencyDesc;
                    $scope.medicationObj.frequencyId = $scope.favouriteMedications[idxFav].frequencyId;
                    $scope.medicationObj.prescribedDosageUnitList=[];
                    $scope.frequency_id=$scope.favouriteMedications[idxFav].frequencyId;
                    $scope.frequency=$scope.medicationObj.searchItemFre;
                    $scope.noOfAdministrationTime=$scope.favouriteMedications[idxFav].noOfAdministrationTime;
                    $scope.item={
                            frequency_id:$scope.frequency_id,
                            frequency:$scope.medicationObj.searchItemFre,
                            noOfAdministrationTime:$scope.noOfAdministrationTime
                    }

                    $scope.model=$scope.favouriteMedications[idxFav].frequencyDesc;
                    $scope.label=$scope.favouriteMedications[idxFav].frequencyDesc;
                    $scope.medicationObj.favorite_status =false;
                    $scope.addFrequencyItemList($scope.item, $scope.model, $scope.label);
                    var formulationTypeId = $scope.favouriteMedications[idxFav].formulationTypeId;
                    var URI = BASE_URL
                            + ROOT_URL
                            + "/api/opd/getRoaList"
                            + "?unitId="
                            + $scope.unitId
                            + "&orgId="
                            + $scope.organizationId
                            + "&formulationTypeId="
                            + formulationTypeId
                            + "&genericId="
                            + $scope.favouriteMedications[idxFav].genericId
                            + "&patientId=0";

                    GenericServiceParamHeader
                            .serviceAction(METHOD_GET, URI)
                            .then(
                                    function(response) {
                                        if (response.data.status == "success") {
                                            if (response.data.listObject.length !== 0) {
                                                var responseList = response.data.listObject;
                                                $scope.roa_options = responseList;
                                            }
                                        }
                                    });

                     if($scope.favouriteMedications[idxFav].roaId != null){
                         $scope.medicationObj.roaId =( $scope.favouriteMedications[idxFav].roaId)
                            .toString();
                     }
                    $scope.medicationObj.dose = $scope.favouriteMedications[idxFav].dose;
                    $scope.medicationObj.doseUnitId = $scope.favouriteMedications[idxFav].doseUnitId!=null?($scope.favouriteMedications[idxFav].doseUnitId).toString():0;
                    $scope.medicationObj.itemStrength=$scope.favouriteMedications[idxFav].dose+" "+$scope.favouriteMedications[idxFav].doseDesc;
                    $scope.medicationObj.unit = $scope.favouriteMedications[idxFav].doseDesc;


                    //get formuation dosage list
                    var dataobj = {
                            organizationId : $scope.organizationId,
                            unitId : $scope.unitId,
                            formulationTypeId:$scope.favouriteMedications[idxFav].formulationTypeId
                    };
                    if($scope.medicationObj.isDoseCalculationApplicable == true){
                        $scope.medicationObj.prescribeDose=($scope.medicationObj.prescribeDose !=undefined && $scope.medicationObj.prescribeDose!= null && $scope.medicationObj.prescribeDose != "") ? $scope.medicationObj.prescribeDose: "";

                    }else{
                        $scope.medicationObj.prescribeDose="";
                    }
                    var URI = BASE_URL + ROOT_URL+ "/api/global/getFormulationTypeToDosageMapperListByFormulationTypeId";
                    GenericService.serviceAction("POST", URI, dataobj).then(function(response) {
                                $rootScope.stopLoader();
                                $scope.medicationObj.prescribedDosageUnitList=[];
                                $scope.counterFormulation=1;

                                angular.forEach(response.data.listObject,function(value,index){
                                    var unitObj={};
                                    unitObj.formulationDosageFromUnit=value.fromConversionUnit;
                                    unitObj.formulationDosageFromUnitId=value.fromConversionFactorUnit;
                                    unitObj.formulationDosageToUnit=value.toConversionUnit;
                                    unitObj.formulationDosageToUnitId=value.toConversionFactorUnit;
                                    unitObj.counterIndex=$scope.counterFormulation++;
                                    unitObj.formulationDosageFromConversionValue=value.fromConversionFactor;
                                    unitObj.formulationDosageToConversionValue=value.toConversionFactor;
                                    unitObj.formulationFactorId=value.factorId;
                                    unitObj.formulationDosage=value.dosageForm;
                                    unitObj.formulationDosageMappId=value.formulationDosageMappId;
                                    $scope.medicationObj.prescribedDosageUnitList.push(unitObj);
                                });

                                angular.forEach(response.data.listObject,function(value,index){
                                    var unitObj={};
                                    if(value.factorId==1){
                                        unitObj.formulationDosageFromUnit=value.toConversionUnit;
                                        unitObj.formulationDosageFromUnitId=value.toConversionFactorUnit;
                                        unitObj.formulationDosageToUnit=value.fromConversionUnit;
                                        unitObj.formulationDosageToUnitId=value.fromConversionFactorUnit;
                                        unitObj.counterIndex=$scope.counterFormulation++;
                                        unitObj.formulationDosageFromConversionValue=value.toConversionFactor;
                                        unitObj.formulationDosageToConversionValue=value.fromConversionFactor;
                                        unitObj.formulationFactorId=value.factorId;
                                        unitObj.formulationDosage=value.dosageForm;
                                        unitObj.formulationDosageMappId=value.formulationDosageMappId;
                                        $scope.medicationObj.prescribedDosageUnitList.push(unitObj);
                                    }
                                });
                                var list=[];
                                if($scope.medicationObj.prescribedDosageUnitList.length>0 ){
                                     list=$.grep($scope.medicationObj.prescribedDosageUnitList, function (obj) {
                                         return $scope.favouriteMedications[idxFav].formulationDosageMappId === obj.formulationDosageMappId;
                                    });
                                    if(list.length>0){
                                        $scope.medicationObj.formulationCounterIndex=list[0].counterIndex;
                                    }
                                }
                    });

                    $scope.medicationObj.durationCount = $scope.tempDurationCount;
                    $scope.medicationObj.durationUnit = 'Day';
                    $scope.medicationObj.isGeneric ='N';
                    $scope.medicationObj.nonFormularyAdvice ='N';
                    $scope.medicationObj.medicationStartDate = new Date();
                    $scope.medicationObj.medicationEndDate = '';
                    $scope.medicationObj.instructionId = $scope.favouriteMedications[idxFav].instructionId;
                    $scope.medicationObj.priorityId ='2';
                    $scope.medicationObj.rate=$item.rate;
                    console.log('svdfvdfv', $scope.medicationObj);

                }

                $timeout(function() {
                     document.getElementById("medicationFreq435").focus();
                }, 1000);

                if($scope.medicationObj.isDoseCalculationApplicable == true){
                    $scope.medicationObj.prescribeDose= $scope.dosePerServing;
                    $scope.calculationByDoseCalculation($scope.medicationObj);
                }
            }

            $scope.calculateEndDate = function(medicationObj) {
                var currentMedication = medicationObj;
                var medicationStartDate = currentMedication.medicationStartDate;
                var durationCount = parseInt(currentMedication["durationCount"]);
                var durationUnit = currentMedication["durationUnit"];

                if(durationUnit == "Day") {
                    var durationCount = parseInt(currentMedication["durationCount"]);
                }
                else if(durationUnit == "Week") {
                    var durationCount = parseInt(currentMedication["durationCount"])
                    var durationCount = durationCount* 7;
                }
                else if(durationUnit == "Month") {
                    var durationCount = parseInt(currentMedication["durationCount"])
                    var durationCount = durationCount* 30;
                }
                else if(durationUnit == "Year") {
                    var durationCount = parseInt(currentMedication["durationCount"])
                    var months = durationCount * 12
                    var durationCount = months* 30;
                }

                if (durationCount && durationUnit) {
                    $scope.model=currentMedication.searchItemFre;
                    $scope.label=currentMedication.searchItemFre;
                    $scope.idx=undefined;
                    $scope.calculateFre($scope.idx);
                }else{
                    $scope.medicationObj.quantity ="";
                }

            }

            $scope.getFavoritesMedication = function() {
                if(($scope.specialityId == undefined || $scope.specialityId == null) && ($scope.doctorId == undefined || $scope.doctorId == null)){
                    growl.warning('Please Select Speciality and Doctor..!');
                    return false;
                }

                $rootScope.startLoader();
                var data = {
                    doctorId : parseInt($scope.doctorId),
                    specialityId : parseInt($scope.specialityId),
                    organizationId : $scope.organizationId,
                    unitId : $scope.unitId
                };

                var URI = BASE_URL + ROOT_URL
                        + GETDOCTORFAVOURITEMEDICATION;

                GenericServiceParamHeader
                .serviceAction(METHOD_POST, URI, data,
                        NOTIFICATION_MSG_STATUS_FALSE)
                .then(function(response) {
                    if (response.data.status == "success") {
                        $rootScope.stopLoader();
                        $scope.favouriteMedications = response.data.listObject;
                        if($scope.favouriteMedications!=null && $scope.favouriteMedications.length>0){
                            angular.forEach($scope.favouriteMedications,function(value,index){
                                value.doctorName = "";
                                value.specialityName = "";
                                const doctor = $scope.doctorList.find(doc => doc.doctorId === value.doctorId);
                                value.doctorName = doctor ? doctor.doctorName : '';
                                const speciality = $scope.specialityList.find(sp => sp.specialityId === value.specialityId);
                                value.specialityName = speciality ? speciality.specialityName : '';
                                if(value.strengthDesc == null ){
                                    value.strengthDesc = "";
                                }
                                if(value.doseDesc == null){
                                    value.doseDesc = "";
                                }
                            });
                        }
                    }
                    else{
                            $rootScope.stopLoader();
                            growl.warning('Unable to Get records..!');
                    }
                });
            }

            $scope.removeFavouriteMedication = function(medication, index) {
                if(medication.favouriteMedicationId != undefined && medication.favouriteMedicationId != null){
                    var data = {};
                    data.specialityId = parseInt($scope.specialityId);
                    data.doctorId = parseInt($scope.doctorId);
                    data.updatedBy = $scope.userId;
                    data.itemId = medication.itemId;
                    data.favouriteMedicationId = medication.favouriteMedicationId;
                    data.organizationId = $scope.organizationId;
                    data.updatedDate = moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
                    data.unitId = $scope.unitId;

                    console.log("data", data);
                    var URI = BASE_URL + ROOT_URL+ REMOVEDOCTORFAVOURITEMEDICATION;


                    GenericServiceParamHeader
                        .serviceAction(METHOD_POST, URI, data,
                                NOTIFICATION_MSG_STATUS_TRUE)
                        .then(
                            function(response) {
                                console.log("res ", response);
                                if (response.data.status == "success") {
                                     $scope.favouriteMedications.splice(index,1);
                                      $scope.getFavoritesMedication();
                                      $scope.initMedicationObj();
                                      $rootScope.stopLoader();
                                }
                                else{
                                     $scope.saveBtnFlag = false;
                                     growl.warning(response.data.message);
                                }

                            });
                }
            }

            $scope.searchMedicationFreItems = function(
                    searchKeyword) {

                $scope.itemList = [];
                var unitId = $scope.unitId
                var orgId = $scope.organizationId

                var URI = BASE_URL + ROOT_URL
                        + "/api/opd/getFrequencyList"
                        + "?unitId=" + unitId + "&orgId="
                        + orgId + "&searchPar=" + searchKeyword
                return BillingGenericService
                        .serviceAction(METHOD_GET, URI, "",
                                NOTIFICATION_MSG_STATUS_FALSE)
                        .then(
                                function(response) {
                                    if (response.data.status == "success") {
                                        $scope.itemList = response.data.listObject;
                                    }
                                    if($scope.itemList.length > 0)
                                        {
                                        return $scope.itemList;
                                        }
                                    else{
                                        $scope.medicationObj.searchItemFre='';
                                        $scope.medicationObj.frequencyId='';
                                    }

                                });
            }



                $scope.checkDoseCal=true;
            $scope.addFrequencyItemList = function($item, $model, $label, idx) {

                console.log($item);
                $scope.storeItemOrder=$item;
                $scope.medicationObj.searchItemFre=$item.frequency;
                $scope.freQua = 0;
                $scope.medicationObj.frequencyId = $item.frequency_id;
                $scope.freQua = $item.noOfAdministrationTime;
                if($scope.medicationObj.durationCount == ""){
                $scope.medicationObj.durationCount = '';
                }
                var dur = 0;
                if ($scope.medicationObj.durationCount == '') {
                    dur = 0;
                } else {
                     if($scope.medicationObj.durationUnit == "Day")
                        {
                         dur = $scope.medicationObj.durationCount;
                         }
                     else if($scope.medicationObj.durationUnit == "Week")
                        {
                          var  caldur = $scope.medicationObj.durationCount;
                            dur = caldur * 7;
                        }
                    else if($scope.medicationObj.durationUnit == "Month")
                        {
                          var  caldur = $scope.medicationObj.durationCount;
                            dur = caldur * 30;
                        }
                        else if($scope.medicationObj.durationUnit == "Year")
                        {

                            var  caldur = $scope.medicationObj.durationCount;
                            dur = caldur * 365;
                        }
                }
                $scope.medicationObj.quantity = $item.noOfAdministrationTime * dur;
                $scope.calculateFre(idx);

                $timeout(function() {
                    document.getElementById("Duration435").focus();
                }, 1000);
            }

            $scope.removeTagOnBackspace = function (event) {
                if (event.keyCode === 8) {
                    console.log('here!');
                    $scope.medicationObj.frequencyId = "";
                }

                if (event.keyCode === 84) {
                    console.log('here!');
                    $scope.medicationObj.frequencyId = "";
                }
                if (event.keyCode === 79) {
                    console.log('here!');
                    $scope.medicationObj.frequencyId = "";
                }
            };


            $scope.calculateFre = function(idx) {
                var list=$.grep($scope.medicationObj.prescribedDosageUnitList, function (obj) {
                    return $scope.medicationObj.formulationCounterIndex === obj.counterIndex;
                });
                var totalDuration="";
                if($scope.medicationObj.durationUnit=="Day"){
                    totalDuration=parseInt($scope.medicationObj.durationCount)*1;
                }else if($scope.medicationObj.durationUnit=="Week"){
                    totalDuration=parseInt($scope.medicationObj.durationCount)*7;
                }else if($scope.medicationObj.durationUnit=="Month"){
                    totalDuration=parseInt($scope.medicationObj.durationCount)*30;
                }else{
                    totalDuration=parseInt($scope.medicationObj.durationCount)*30*12;
                }
                if(list.length>0){
                    var presDoseObj=list[0];
                    if(list.length>0  && presDoseObj.formulationFactorId==2){
                        $scope.medicationObj.prescriptionDoseUnit=presDoseObj.formulationDosage;
                    }else{
                        $scope.medicationObj.prescriptionDoseUnit=presDoseObj.formulationDosageFromUnit;
                    }
                    $scope.medicationObj.prescribedDoseUnitId=presDoseObj.formulationDosageFromUnitId;
                }
                if($scope.medicationObj.isDoseCalculationApplicable == undefined || $scope.medicationObj.isDoseCalculationApplicable == false){
                    if(list.length>0  && $scope.medicationObj.prescribeDose!="" && $scope.medicationObj.prescribeDose!=undefined && $scope.medicationObj.prescribeDose!=null
                            && $scope.medicationObj.formulationCounterIndex!="" && $scope.medicationObj.formulationCounterIndex!=undefined && $scope.medicationObj.formulationCounterIndex!=null){
                        //for conversion factor
                            if($scope.medicationObj.prescribedDoseUnitId==$scope.medicationObj.doseUnitId && $scope.medicationObj.dose!=0){
                                $scope.medicationObj.quantity =Math.ceil(($scope.freQua* totalDuration*$scope.medicationObj.prescribeDose)/$scope.medicationObj.dose);
                            }else{
                                if(list.length>0 && $scope.medicationObj.dose!=0){
                                if(presDoseObj.formulationDosageToUnitId==$scope.medicationObj.doseUnitId){
                                    var convValue=Math.round(($scope.medicationObj.prescribeDose*presDoseObj.formulationDosageToConversionValue)/presDoseObj.formulationDosageFromConversionValue);
                                     $scope.medicationObj.quantity = Math.ceil(($scope.freQua* totalDuration*convValue)/$scope.medicationObj.dose);
                                }else{
                                  $scope.medicationObj.quantity = Math.ceil(($scope.freQua* totalDuration*$scope.medicationObj.prescribeDose)/$scope.medicationObj.dose);
                                }
                              }else{
                                  $scope.medicationObj.quantity = Math.ceil(($scope.freQua
                                            * totalDuration));
                              }

                            }
                    }
                    else{
                        //using frequency factor
                        if(list.length>0  && presDoseObj.formulationFactorId==2){
                            var splitString=(presDoseObj.formulationDosage).split(" ");
                            var convValue=0;
                            var i = 0 ;
                            for(i=0;i<splitString.length;i++){
                                var b=parseFloat(splitString[i]);
                                if(Number.isNaN(b)==false){
                                    convValue=convValue+(math.number(math.fraction(splitString[i])));
                                }
                            }
                             $scope.medicationObj.quantity = Math.ceil($scope.freQua
                                        * totalDuration*(convValue==0?1:convValue));
                        }else{
                            $scope.medicationObj.quantity = Math.ceil(($scope.freQua * totalDuration));
                            if($scope.medicationObj.quantity==null || isNaN($scope.medicationObj.quantity)){
                                $scope.medicationObj.quantity=0;
                            }
                        }
                    }

                    /*$timeout(function() {
                         document.getElementById("quantitytest").focus();
                    }, 500);*/

                }else{

                    $scope.patientWeightListNew=[];
                    if($scope.patientViatalListNew.length > 0){
                        angular.forEach($scope.patientViatalListNew, function(val, key){
                            if((val.vitalId != null && val.vitalId == 1) && (val.vitalName != null && val.vitalName == "Weight")){
                                if(val.value != undefined && val.value != null && val.value != "" && val.value != 0){
                                    $scope.patientWeightListNew.push(val);
                                }

                            }
                        });
                    }else{
                        growl.warning("Please enter patient's vital.!");
                        return false;
                    }

                    $scope.patientWeightListFinal=[];
                    if($scope.patientWeightListNew != undefined && $scope.patientWeightListNew.length > 0){
                        $scope.patientWeightListFinal.push($scope.patientWeightListNew[0]);
                    }else{
                            growl.warning("Please enter patient's weight in vitals.!");
                            return false;
                        }

                    if($scope.patientWeightListFinal.length > 0){
                        angular.forEach($scope.patientWeightListFinal, function(val1, key1){
                            if((val1.vitalId != null && val1.vitalId == 1) && (val1.vitalName != null && val1.vitalName == "Weight")){
                                if(val1.value != undefined && val1.value != null && val1.value != "" && val1.value != 0){
                                    $scope.patWeight=val1.value;
                                }else{
                                    growl.warning("Please enter patient's weight in vitals.!");
                                }

                            }
                        });
                    }else{
                        growl.warning("Please enter patient's vital.!");
                    }

                    $scope.medicationObj.noOfAdministrationTimeForDoseCal=($scope.freQua != 0) ? $scope.freQua : $scope.medicationObj.noOfAdministrationTimeForDoseCal;
                    $scope.medicationObj.durationCountForDoseCal= ($scope.medicationObj.durationCount != undefined && $scope.medicationObj.durationCount !=null && $scope.medicationObj.durationCount !="")? $scope.medicationObj.durationCount
                                                                     :$scope.medicationObj.durationCountForDoseCal;

                    $scope.doseReqInMg = $scope.patWeight * $scope.medicationObj.doseForDoseCal;
    //							        $scope.doseReqInMg = Math.ceil($scope.doseReqInMg);

                    $scope.doseReqInMl = $scope.doseReqInMg / $scope.medicationObj.strengthForDoseCal;
    //							        $scope.doseReqInMl = Math.ceil($scope.doseReqInMl);

                    $scope.dosePerServing = $scope.doseReqInMl / $scope.medicationObj.noOfAdministrationTimeForDoseCal;
                    $scope.dosePerServing = Math.round($scope.dosePerServing * 10) / 10
    //							        $scope.dosePerServing = Math.ceil($scope.dosePerServing);

                    $scope.claculatedQuantity = ($scope.dosePerServing * $scope.medicationObj.noOfAdministrationTimeForDoseCal * $scope.medicationObj.durationCountForDoseCal)/ $scope.medicationObj.quantityForDoseCal;
                    $scope.claculatedQuantity = Math.ceil($scope.claculatedQuantity);

                    $scope.medicationObj.quantity = $scope.claculatedQuantity;
                    $scope.medicationObj.prescribeDose= $scope.dosePerServing;

                }
            }

            $scope.getROAForNonFormulary=function(){
                $scope.medicationObj.prescribedDosageUnitList=[];
                var URI = BASE_URL + ROOT_URL + "/api/opd/getRoaListNonFormulary" + "?unitId=" +
                $scope.unitId + "&orgId=" + $scope.organizationId +"&patientId=0";

                GenericServiceParamHeader.serviceAction(METHOD_GET, URI).then(function(response) {
                    if(response.data.status == "success"){
                            $scope.roa_options1 = response.data.listObject;
                            console.log("non formulary ROA="+$scope.roa_options1);

                    }
                });
            }
            $scope.getROAForNonFormulary();
            $scope.genericQuantityCheck = false;
            $scope.addMedicationGenericList = function($item,
                    $model, $label, idx) {
                console.log($item);

                $rootScope.startLoader();

                $scope.medicationObj.itemId = $item.drugId;
                $scope.medicationObj.genericName = "";
                $scope.medicationObj.genericId = $item.genericId;
                $scope.medicationObj.searchItem = $item.medicationName;
                $scope.medicationObj.searchGeneric= $item.genericName
                $scope.medicationObj.genericName= $item.genericName;
                $scope.medicationObj.rate=$item.rate;

                $scope.genericQuantityCheck = false;
                var arr = $item.medicationName.split("|");
                var dggd =arr[1].split(":");
                if(dggd[1] == " 0"){
                    growl.warning("Stock not available for the selected Generic Name" );
                    $scope.genericQuantityCheck = true;
                   // return false;
                }

                var formulationTypeId = $item.formulationTypeId;
                var URI = BASE_URL
                        + ROOT_URL
                        + "/api/opd/getRoaList"
                        + "?unitId="
                        + $scope.unitId
                        + "&orgId="
                        + $scope.organizationId
                        + "&formulationTypeId="
                        + formulationTypeId
                        + "&genericId="
                        + $item.genericId
                        + "&patientId=0";

                GenericServiceParamHeader
                        .serviceAction(METHOD_GET, URI)
                        .then(
                                function(response) {
                                    if (response.data.status == "success") {
                                        $rootScope.stopLoader();
                                        var responseList = response.data.listObject;
                                        $scope.medicaRoaList = responseList;
                                        if (response.data.object == 0) {
                                            if (response.data.listObject.length !== 0) {

                                                $scope.roa_options = responseList;
                                            }
                                            $scope.medicationObj.genericName = $item.genericName;
                                            $scope.medicationObj.formulationTypeId = $item.formulationTypeId;
                                            $scope.medicationObj.strength = $item.strength;
                                        } else {
                                            $scope.checkgenericObj = {
                                                genericName : $item.genericName,
                                                formulationTypeId : $item.formulationTypeId,
                                                strength : $item.strength,
                                            }
                                            angular
                                                    .element(
                                                            '#confirmAddItemPopup')
                                                    .modal(
                                                            'show');
                                        }
                                    }
                                });

                var itemId = $item.drugId;
                var URI = BASE_URL + ROOT_URL
                        + "/api/opd/getUOMList" + "?unitId="
                        + $scope.unitId + "&orgId="
                        + $scope.organizationId + "&itemId="
                        + itemId;

                GenericServiceParamHeader
                        .serviceAction(METHOD_GET, URI)
                        .then(
                                function(response) {
                                    if (response.data.status == "success") {
                                        if (response.data.listObject.length !== 0) {
                                            // var responseList
                                            // =
                                            // response.data.listObject;
                                            // $scope.roa_options
                                            // = responseList;
                                            $scope.medicationObj.uomTypeId = response.data.listObject[0].uomTypeId;
                                            $scope.medicationObj.conversions = response.data.listObject[0].conversions;
                                        }
                                    }
                                });

                $scope.showMedicationOptions = false;
                // $scope.emptyMedicationRow["medicationName"] =
                // "";
                $scope.search = "";

                //get formuation dosage list
                var dataobj = {
                        organizationId : $scope.organizationId,
                        unitId : $scope.unitId,
                        formulationTypeId:formulationTypeId
                };
                $scope.medicationObj.doseUnitId=$item.strengthUnitId;
                $scope.medicationObj.dose=$item.strength;

                $scope.medicationObj.prescribeDose="";
                $scope.medicationObj.itemStrength=$item.strength+" "+$item.unitDesc;
                var URI = BASE_URL + ROOT_URL+ "/api/global/getFormulationTypeToDosageMapperListByFormulationTypeId";
                GenericService.serviceAction("POST", URI, dataobj).then(function(response) {
                            $rootScope.stopLoader();
                            $scope.medicationObj.prescribedDosageUnitList=[];
                            $scope.counterFormulation=1;
                            $scope.medicationObj.formulationCounterIndex="";
                            angular.forEach(response.data.listObject,function(value,index){
                                var unitObj={};
                                unitObj.formulationDosageFromUnit=value.fromConversionUnit;
                                unitObj.formulationDosageFromUnitId=value.fromConversionFactorUnit;
                                unitObj.formulationDosageToUnit=value.toConversionUnit;
                                unitObj.formulationDosageToUnitId=value.toConversionFactorUnit;
                                unitObj.counterIndex=$scope.counterFormulation++;
                                unitObj.formulationDosageFromConversionValue=value.fromConversionFactor;
                                unitObj.formulationDosageToConversionValue=value.toConversionFactor;
                                unitObj.formulationFactorId=value.factorId;
                                unitObj.formulationDosage=value.dosageForm;
                                unitObj.formulationDosageMappId=value.formulationDosageMappId;
                                $scope.medicationObj.prescribedDosageUnitList.push(unitObj);
                            });

                            angular.forEach(response.data.listObject,function(value,index){
                                var unitObj={};
                                if(value.factorId==1){
                                    unitObj.formulationDosageFromUnit=value.toConversionUnit;
                                    unitObj.formulationDosageFromUnitId=value.toConversionFactorUnit;
                                    unitObj.formulationDosageToUnit=value.fromConversionUnit;
                                    unitObj.formulationDosageToUnitId=value.fromConversionFactorUnit;
                                    unitObj.counterIndex=$scope.counterFormulation++;
                                    unitObj.formulationDosageFromConversionValue=value.toConversionFactor;
                                    unitObj.formulationDosageToConversionValue=value.fromConversionFactor;
                                    unitObj.formulationFactorId=value.factorId;
                                    unitObj.formulationDosage=value.dosageForm;
                                    unitObj.formulationDosageMappId=value.formulationDosageMappId;
                                    $scope.medicationObj.prescribedDosageUnitList.push(unitObj);
                                }
                            });
                            var list=[];
                            if($scope.medicationObj.prescribedDosageUnitList.length>0 && $scope.medicationObj.prescribedDosageUnitList[0].formulationFactorId==1){
                                 list=$.grep($scope.medicationObj.prescribedDosageUnitList, function (obj) {
                                    return $scope.medicationObj.doseUnitId === obj.formulationDosageFromUnitId;
                                });
                                if(list.length>0){
                                    $scope.medicationObj.formulationCounterIndex=list[0].counterIndex;
                                }
                            }
                });
            }



            $scope.medicationClear = function () {
                $scope.medicationObj.searchItem = '';
            }
            $scope.genericClear = function () {
                $scope.medicationObj.searchGeneric = '';
            }
            $scope.clearDoctors = function(){
                $scope.doctorId = '';
                $scope.specialityId = '';
                $scope.favouriteMedications = [];
            }

            $scope.getDoseList = function(){
                var URI = BASE_URL + ROOT_URL + "/api/globals/strength/units/"+$scope.organizationId;
                GenericService.serviceAction("GET", URI, {})
                .then(function(response) {
                    $scope.doseList = [];
                    $scope.doseList = response.data;

                });
            }
            $scope.getDoseList();
        }
              function config($stateProvider) {
        $stateProvider
                .state(
                        "favouriteMedicationTemplate",
                        {
                            url : "/favouriteMedicationTemplate",
                            templateUrl : "views/doctorDesk/favourite-mediacation-template.html",
                            controller : "favouriteMedicationTemplate.Controller",
                            title : 'Favourite Medication Template',

                            controllerAs : "vm",
                        });
    }

angular.module("myApp").config(config).controller(
            "favouriteMedicationTemplate.Controller", favouriteMedicationTemplateController)
})();