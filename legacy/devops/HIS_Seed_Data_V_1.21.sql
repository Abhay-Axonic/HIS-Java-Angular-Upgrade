
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(477, 6, 50, true, 1, '2018-05-31 17:08:46.000', 1, '2018-05-31 17:08:46.000', false, 1); 


INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(478, 6, 51, true, 1, '2018-05-31 17:08:46.000', 1, '2018-05-31 17:08:46.000', false, 1); 

INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(480, 7, 89, true, 1, '2018-08-29 09:56:27.000', 1, '2018-08-29 09:56:27.000', false, 1);



 UPDATE public.m_report_header
SET  latter_head_img='
<iframe style="border:0px; width:888px;height:125px" src="https://s3.ap-south-1.amazonaws.com/his-docs/ruralhealthcare100/patient/documents/clinical/episodic-documents/op-coversheet/100_100_null_meditestLog.jpg"></iframe>
<div style="text-align :center; font-size: 11px;"><b>@companyDesc</b>  @comAddress
 </div>', lab_header='
<link href="https://fonts.googleapis.com/css?family=Libre Barcode 128" rel="stylesheet">
	<style>
#barcode{
    font-family: "Libre Barcode 128";font-size: 22px;
}<style type="text/css">
<!--
span.cls_002{font-family:"Verdana",serif;font-size:9.5px;color:rgb(0,0,0);font-weight:normal;font-style:normal;text-decoration: none}
div.cls_002{font-family:"Verdana",serif;font-size:9.5px;color:rgb(0,0,0);font-weight:normal;font-style:normal;text-decoration: none}
span.cls_003{font-family:"Verdana Bold",serif;font-size:11.5px;color:rgb(0,0,0);font-weight:bold;font-style:normal;text-decoration: none}
div.cls_003{font-family:"Verdana Bold",serif;font-size:11.5px;color:rgb(0,0,0);font-weight:bold;font-style:normal;text-decoration: none}
span.cls_004{font-family:Arial,serif;font-size:9.2px;color:rgb(0,0,0);font-weight:normal;font-style:normal;text-decoration: none}
div.cls_004{font-family:Arial,serif;font-size:9.2px;color:rgb(0,0,0);font-weight:normal;font-style:normal;text-decoration: none}
span.cls_005{font-family:Arial,serif;font-size:9.9px;color:rgb(0,0,0);font-weight:bold;font-style:normal;text-decoration: none}
div.cls_005{font-family:Arial,serif;font-size:9.9px;color:rgb(0,0,0);font-weight:bold;font-style:normal;text-decoration: none}
span.cls_011{font-family:"Verdana",serif;font-size:9.5px;color:rgb(0,0,0);font-weight:normal;font-style:normal;text-decoration: underline}
div.cls_011{font-family:"Verdana",serif;font-size:9.5px;color:rgb(0,0,0);font-weight:normal;font-style:normal;text-decoration: none}
span.cls_012{font-family:"Verdana Bold",serif;font-size:9.5px;color:rgb(0,0,0);font-weight:bold;font-style:normal;text-decoration: underline}
div.cls_012{font-family:"Verdana Bold",serif;font-size:7.5px;color:rgb(0,0,0);font-weight:bold;font-style:normal;text-decoration: none}
span.cls_006{font-family:Courier New,serif;font-size:6.0px;color:rgb(0,0,0);font-weight:normal;font-style:normal;text-decoration: none}
div.cls_006{font-family:Courier New,serif;font-size:6.0px;color:rgb(0,0,0);font-weight:normal;font-style:normal;text-decoration: none}
span.cls_007{font-family:"Verdana",serif;font-size:6.6px;color:rgb(0,0,0);font-weight:normal;font-style:normal;text-decoration: none}
div.cls_007{font-family:"Verdana",serif;font-size:6.6px;color:rgb(0,0,0);font-weight:normal;font-style:normal;text-decoration: none}
span.cls_008{font-family:"Verdana Bold",serif;font-size:6.6px;color:rgb(0,0,0);font-weight:bold;font-style:normal;text-decoration: none}
div.cls_008{font-family:"Verdana Bold",serif;font-size:6.6px;color:rgb(0,0,0);font-weight:bold;font-style:normal;text-decoration: none}
span.cls_009{font-family:"Verdana",serif;font-size:8.3px;color:rgb(0,0,0);font-weight:normal;font-style:normal;text-decoration: none}
div.cls_009{font-family:"Verdana",serif;font-size:8.3px;color:rgb(0,0,0);font-weight:normal;font-style:normal;text-decoration: none}
span.cls_010{font-family:Arial,serif;font-size:25.1px;color:rgb(0,0,0);font-weight:normal;font-style:normal;text-decoration: none}
div.cls_010{font-family:Arial,serif;font-size:25.1px;color:rgb(0,0,0);font-weight:normal;font-style:normal;text-decoration: none}
-->
</style>
<hr>
<div style="position:absolute;left:55.80px;top:160.45px" class="cls_002"><span class="cls_002">NAME</span></div>
<div style="position:absolute;left:137.15px;top:160.45px" class="cls_002"><span class="cls_002">:</span></div>
<div style="position:absolute;left:142.10px;top:160.45px" class="cls_003"><span class="cls_003">@patient</span></div>
<div style="position:absolute;left:550.70px;top:160.45px" class="cls_002"><span class="cls_002">UHID</span></div>
<div style="position:absolute;left:610.70px;top:160.45px" class="cls_004"><span class="cls_004">:</span></div>
<div style="position:absolute;left:620.20px;top:160.45px" class="cls_003"><span class="cls_003">@uhid</span></div>
<div style="position:absolute;left:55.95px;top:175.45px" class="cls_002"><span class="cls_002">LR NO</span></div>
<div style="position:absolute;left:137.15px;top:175.45px" class="cls_002"><span class="cls_002">:</span></div>
<div style="position:absolute;left:142.10px;top:175.45px" class="cls_003"><span class="cls_002">@orderNo</span></div>
<div style="position:absolute;left:300.95px;top:175.45px" class="cls_002"><span class="cls_002">AGE</span></div>
<div style="position:absolute;left:340.80px;top:175.45px" class="cls_005"><span class="cls_005">:</span></div>
<div style="position:absolute;left:350.20px;top:175.45px" class="cls_002"><span class="cls_002">@age</span></div>
<div style="position:absolute;left:450.75px;top:175.45px" class="cls_002"><span class="cls_002">SEX   </span><span class="cls_005">:</span><span class="cls_002">@gender</span></div>
<div style="position:absolute;left:55.95px;top:190.45px" class="cls_002"><span class="cls_002">COLLECTED ON</span></div>
<div style="position:absolute;left:137.15px;top:190.45px" class="cls_002"><span class="cls_002">:</span></div>
<div style="position:absolute;left:142.10px;top:192.45px" class="cls_003"><span class="cls_003">@date</span></div>
<div style="position:absolute;left:300.20px;top:190.45px" class="cls_002"><span class="cls_002">REGISTERED ON</span></div>
<div style="position:absolute;left:390.60px;top:190.45px" class="cls_003"><span class="cls_003">:</span></div>
<div style="position:absolute;left:395.20px;top:192.45px" class="cls_003"><span class="cls_003">@registerDate</span></div>
<div style="position:absolute;left:550.65px;top:190.45px" class="cls_002"><span class="cls_002">REPORTED ON</span></div>
<div style="position:absolute;left:610.45px;top:190.45px" class="cls_002"><span class="cls_002"></span></div>
<div style="position:absolute;left:620.25px;top:192.45px" class="cls_003"><span class="cls_003">:@releaseDate</span></div>
<div style="position:absolute;left:55.95px;top:205.45px" class="cls_002"><span class="cls_002">REFERRED BY</span></div>
<div style="position:absolute;left:137.15px;top:205.45px" class="cls_002"><span class="cls_002">:</span></div>
<div style="position:absolute;left:142.10px;top:205.45px" class="cls_003"><span class="cls_003">@referral</span></div>
<div style="position:absolute;left:55.95px;top:220.45px" class="cls_011"><span class="cls_011">Report Status</span></div>
<div style="position:absolute;left:137.15px;top:220.45px" class="cls_002"><span class="cls_002">:</span></div>
<div style="position:absolute;left:142.10px;top:220.45px" class="cls_012"><span class="cls_012">@reportStatus</span></div>
<div style="position:absolute;left:300.95px;top:220.45px" class="cls_002"><span class="cls_002">Sample No</span></div>
<div style="position:absolute;left:390.20px;top:220.45px" class="cls_004"><span class="cls_004">:</span></div>
<div style="position:absolute;left:395.45px;top:220.45px" class="cls_003"><span class="cls_003">@sampleNo</span></div>
<div style="position:absolute;left:600.10px;top:220.45px" class="cls_006"><span class="cls_006" id = "barcode">@sampleNo</span></div>
</div>
<br>
<br>
<br>
<br>
<hr>', lab_footer='<iframe style="border:0px; width:790px;height:125px" src="https://s3.ap-south-1.amazonaws.com/his-docs/cht1/patient/documents/clinical/episodic-documents/op-coversheet/1_1_null_footerImg.JPG"></iframe>
'
