/**
 * PAGE CONTENT HEIGHT
 */
 	var documentHeight= $(window).height();
	var sideBarHeight = documentHeight - 55;
	//var sideBarHeight = documentHeight - 99;
	$("#page-content-wrapper").css("height", sideBarHeight + "px" );