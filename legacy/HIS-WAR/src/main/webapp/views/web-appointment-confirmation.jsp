<html ><!-- xmlns:th="http://www.thymeleaf.org" -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false" %>
<title>HIS</title>
<!-- <title>SD Global Technologies</title> -->
    <meta charset="UTF-8">
    <meta name="viewport" content="initial-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- Remove Tap Highlight on Windows Phone IE -->
    <meta name="msapplication-tap-highlight" content="no"/>
    
	<link rel="stylesheet" href="resources/assets/css/bootstrap.min.css" media="all">
<!--    <link rel="stylesheet" type="text/css" href="resources/assets/css/param.common.master.css" /> -->
	<link rel="stylesheet" href="../resources/assets/css/font-awesome-4.7.0/css/font-awesome.min.css">
	<link rel="stylesheet" type="text/css" href="resources/assets/css/book-appointment.css" />
	<link rel="stylesheet" type="text/css" href="resources/assets/css/datepicker3.min.css" />
	
	<!-- for tables -->
	<!-- <link rel="stylesheet" href="resources/assets/css/datatables.bootstrap.css"> -->
	<link rel="stylesheet" type="text/css" href="resources/assets/js/docviewer/jquery.fancybox.css" />
<style>
.thstyle {
	border: none !important;
    cursor: pointer !important;
    width: 65px;
}
.labeltopadd{
	font-size: 16.00px !important;
    font-weight: 700 !important;
    font-style: normal;
    color: rgba(101, 96, 96, 1);
    font-family: Lato, Arial;
}
.trstyle {
	border: none !important;
    cursor: pointer !important;
    width: 65px;
	padding: 0px !important;
}
.patientName {
	letter-spacing: 0.00px !important;
    color: rgba(162, 162, 162, 1) !important;
    -webkit-text-stroke: 1.00px rgba(0, 0, 0, 0) !important;
    font-size: 14.00px !important;
    font-weight: 700 !important;
    font-style: normal !important;
    font-family: Raleway, Arial !important;
    text-decoration: none !important;
}
.polymer-form label.placeholder {
	letter-spacing: 0.00px !important;
    color: rgba(162, 162, 162, 1) !important;
    -webkit-text-stroke: 1.00px rgba(0, 0, 0, 0) !important;
    font-size: 14.00px !important;
    font-weight: 700 !important;
    font-style: normal !important;
    font-family: Raleway, Arial !important;
    text-decoration: none !important;
}
.padding-left-right2 {
	padding-left: 2% !important;
    padding-right: 2% !important;
}
.btnConfirmApp{
	transform-origin: center;
    opacity: 1;
    cursor: default;
    margin-top: 0.00px;
    margin-right: 0.00px;
    margin-bottom: 0.00px;
    margin-left: 0.00px;
    padding-top: 0.00px;
    padding-right: 0.00px;
    padding-bottom: 0.00px;
    padding-left: 0.00px;
    background-color: rgba(55, 188, 223, 1) !important;
    border-bottom-color: rgba(112, 112, 112, 1) !important;
    border-bottom-style: none !important;
    border-bottom-width: 1.00px !important;
    border-left-color: rgba(112, 112, 112, 1) !important;
    border-left-style: none !important;
    border-left-width: 1.00px !important;
    border-right-color: rgba(112, 112, 112, 1) !important;
    border-right-style: none !important;
    border-right-width: 1.00px !important;
    border-top-color: rgba(112, 112, 112, 1) !important;
    border-top-style: none !important;
    border-top-width: 1.00px;
    border-top-left-radius: 0.00px;
    border-top-right-radius: 0.00px;
    border-bottom-left-radius: 0.00px;
    border-bottom-right-radius: 0.00px;
    -webkit-filter: drop-shadow(0px 3px 6px rgba(0, 0, 0, 0.1607843137254902));
    filter: drop-shadow(0px 3px 6px rgba(0, 0, 0, 0.1607843137254902));
    width: 360.00px;
    height: 57.00px;
    left: 0.00px;
    top: 0.00px;
    font-size: 22.00px !important;
    font-weight: 700 !important;
    font-family: Raleway, Arial !important;
}
.visitTypelabel {
	font-size: 17.00px !important;
    font-weight: 700 !important;
    letter-spacing: 0.00px;
    color: rgba(112, 112, 112, 1);
}
ul.a {
  list-style-position: outside;
}

}

</style>
 <div id="page-content-wrapper">
  <div class="container-fluid row " id="">
  		<div class="row rectangle65" style="width: -webkit-fill-available; border-bottom: groove;">
        <div class="col-md-4 col-sm-4 col-xs-4" style="padding-left: 6% !important;">
	  	<!-- 	<img id="logo" width="80" height="70" /> --> <!-- class="unnamed" -->
	  	</div>
	  	<div class="col-md-4 col-sm-4 col-xs-4 text-center" style="margin-top: 2%;">
	  		<label class="addressLable labeltopadd">${unitAddress}</label>
	  	</div>
	  	<div class="col-md-4 col-sm-4 col-xs-4 text-center" style="padding-top: 7;">
	  		<div data-layer="c3417a13-20b4-4f5a-a628-b347578346f1" class="poweredBy">Powered by</div>
	  		 <img src="resources/assets/images/icon/bed/temp/CHT_logo_2July.png" style="width: 212px !important;margin-left: 96px;"> <!-- class="image17" -->
	  	</div>
	</div>
	<div class="row" style="margin-left: 5% !important; margin-right: 5% !important;">
		<div class="col-md-5 col-sm-5 col-xs-12 padding-left-right-set0">
		 <div class="row">
		  	   <c:choose>
				      		<c:when test="${not empty doctorImage}">
				      		  <img ng-if="doctorImage!=null && doctorImage!='' " id="doctorProfileImagePath" src="${doctorImage}" class="rectangle59"  style="width: 100%; height: 387px;" />
				      		</c:when>
				      		<c:otherwise>
				      		  <img ng-if="doctorImage==null || doctorImage=='' " id="doctorProfileImagePath" src="resources/assets/images/tempImages/Dr2 copy.png" class="rectangle59"  style="width: 100%; height: 387px;" />
				      		</c:otherwise>
				</c:choose>
		<%--   <img ng-if="doctorImage==null || doctorImage=='' " id="doctorProfileImagePath" src="resources/assets/images/tempImages/Dr2 copy.png" class="rectangle59"  style="width: 100%; height: 387px;" />
		     <img ng-if="doctorImage!=null && doctorImage!='' " id="doctorProfileImagePath" src="${doctorImage}" class="rectangle59"  style="width: 100%; height: 387px;" /> --%>
		 </div>
		 
		 <div class="row">
		 <img src="resources/assets/images/icon/bed/temp/rectangle43.png" class="rectangle60"  style="width: 100%; height: 300px; 20px;">
		 </div>
		 <div class="row">
		  <div class="rectangle61">
		  	<div class="row" style="text-align: center !important; padding-top: 5px !important;"><label class="drName">Dr.${doctorName}</label><br>
		  	<!-- <label class="generalMedicine">${webAppResDto.docDepartmentName}</label> -->
		  	</div>`
		  	 <div class="row" style="margin-left: 5% !important; margin-right: 5% !important;">
		  		<div class="selectTypeOfAppointment">Area Of Expertise</div>
		  		<div class="row">
		  		<ul class="a" id="expertise">${areaOfExperty}
 				 </ul>
 			   </div>
 			   <div class="row">
	 			   <div class="col-md-4 col-sm-4 col-xs-4">
	 			    <div class="selectTypeOfAppointment">Qualification</div>
	 			  	 <div class="areapractice">${doctorDegrees}</div>
	 			   </div>
	 			<!--     <div class="col-md-4 col-sm-4 col-xs-4">
	 			    <div class="selectTypeOfAppointment">Years Of Practice</div>
	 			  	 <div class="areapractice">${webAppResDto.docExprience}+ Years</div>
	 			   </div> -->
	 			    <div class="col-md-4 col-sm-4 col-xs-4">
	 			    <div class="selectTypeOfAppointment">Visit Fee</div>
	 			  	 <div class="areapractice"><!-- ${webAppResDto.docCurrency} --> ${visitFee}</div>
 			  		 </div>
 			  	</div>	 
 			<!--    <div class="selectTypeOfAppointment padding-top10">About the Doctor </div>
 			   <div class="aboutDoctor"><p>${webAppResDto.aboutDoctor}</p></div> -->
		  </div>
		 </div> 
		</div>
		</div>
		<div class="col-md-7 col-sm-7 col-xs-12 padding-left-right-set0">
		 <div class="row"  style="margin-top: 9%; margin-left: 5% !important;">
		 <div class="row text-center">
		 <div class="col-md-2 col-sm-12 col-xs-12" style="margin-top: -48px !important;">
		  <img src="resources/assets/images/icon/bed/temp/confirm.png" class="androidApp" /> 	</div>
		  <div class="col-md-8 col-sm-12 col-xs-12">
		  <div class="" style="color: rgba(249, 198, 34, 1) !important; font-size: 37.00px;font-weight: 700; font-style: normal; font-family: Raleway, Arial;">Appointment Confirmed </div>
		   <div class="availableDates" style="color: rgba(112, 112, 112, 1) !important; font-family: Lato, Arial !important; text-align: center !important;">An email regarding the same has been sent to you </div>
		  </div></div>
		</div>
		 <div class="row" style="padding-top: %;">
			<div class="rectangle45" style="height: auto !important;">
				<div class="row" style="margin-left: 5% !important; margin-right: 5% !important;">
					
				</div>	
				<div class="row" style="margin-left: 5% !important; margin-right: 5% !important;margin-bottom: 3% !important;">	
				<div class="selectTypeOfAppointment" style="margin-top: 3%; margin-left: 5% !important;font-size: 20px !important;">Appointment Location</div>
				<div class="visitTypelabel" style="margin-top: 	%; margin-left: 5% !important;">${unitAddress}</div>
				<div class="selectTypeOfAppointment" style="margin-top: 3%; margin-left: 5% !important;font-size: 20px !important;">Appointment Details</div>
				<div class="visitTypelabel" style="margin-top: 	%; margin-left: 5% !important;">${patientVisit} on ${scheduledDate},${fromTime}</div>
				<div class="row" style="margin-top: 2% !important;">
				<div class="col-md-6 col-sm-6 col-xs-6">
					<div class="selectTypeOfAppointment" style="margin-top: 3%; margin-left: 5% !important;font-size: 20px !important;">Visit Fee:</div>
					<div class="visitTypelabel" style="margin-top: 	%; margin-left: 5% !important;"><!-- ${webAppResDto.docCurrency} --> ${visitFee} for a ${visitDuration} min session</div>
				</div>
				<div class="col-md-6 col-sm-6 col-xs-6">
					<div class="selectTypeOfAppointment" style="margin-top: 3%; margin-left: 5% !important;font-size: 20px !important;">Consulting for:</div>
					<div class="visitTypelabel" style="margin-top: 	%; margin-left: 5% !important;">${consultingSpeciality}</div>
				</div>
				</div>
				<div class="row" style="margin-top: 2% !important;">
				<div class="col-md-6 col-sm-6 col-xs-6">
					<div class="selectTypeOfAppointment" style="margin-top: 3%; margin-left: 5% !important;font-size: 20px !important;">Patient Details</div>
					<div class="visitTypelabel" style="margin-top: 	%; margin-left: 5% !important;">${patientName}<br><!-- ${webAppResDto.patConCode} --> ${mobileNumber} <br> ${emailId}</div>
				</div>
				<div class="col-md-6 col-sm-6 col-xs-6">
					<div class="selectTypeOfAppointment" style="margin-top: 3%; margin-left: 5% !important;font-size: 20px !important;">Purpose of Appointment</div>
					<div class="visitTypelabel" style="margin-top: 	%; margin-left: 5% !important;">${appointmentReason}</div>
				</div>
				</div>
			</div>
		  </div>
		 </div>  
		<!--  <div class="row text-center myLife"> Now manage your Appointments, your Reports <br> and much more from  <br> the &nbsp;<label class="myLife1" >CliniQ App</label> </div>
          <div class="row text-center"><img src="${pageContext.request.contextPath}/resources/assets/common/images/logo/onlink_to_ukcliniq_small.png" class="androidApp" /> 	</div>
 -->

</html>
 
  
 
  