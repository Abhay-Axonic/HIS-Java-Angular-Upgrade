package com.param.global.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.dozer.DozerFactory;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.CommonDto;
import com.param.global.dto.FormsEngineTransactionDto;
import com.param.global.dto.SpecialityMasterDto;
import com.param.global.dto.TemplateMasterDto;
import com.param.global.dto.TemplateReqDto;
import com.param.global.dto.TemplateResponseDto;
import com.param.global.dto.TemplateTransactionDto;
import com.param.global.dto.UnitTemplateConfigDto;
import com.param.global.model.FormsEngineTransactionMaster;
import com.param.global.model.TemplateMaster;
import com.param.global.org.dto.UnitMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class TemplateMasterDaoImpl extends GenericDao<TemplateMaster, Integer> implements ITemplateMasterDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(TemplateMasterDaoImpl.class);


	public TemplateMasterDaoImpl() {
		super(TemplateMaster.class);
	}

	@Autowired
	private DozerFactory factory;
	
	@Override
	public Response saveTemplate(TemplateMasterDto dto) throws ApplicationException {
		try{			
			if(dto.getTemplateObjectHtml() != null){			   
						String replacedString  = "<head><style> html{box-sizing:border-box;margin:15px;line-height:1.5}*,:after,:before{box-sizing:inherit}html{-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%}body{margin:0}article,aside,details,figcaption,figure,footer,header,main,menu,nav,section,summary{display:block}audio,canvas,progress,video{display:inline-block}progress{vertical-align:baseline}audio:not ([controls]){display:none;height:0}[hidden],template{display:none}a{background-color:transparent;-webkit-text-decoration-skip:objects}a:active,a:hover{outline-width:0}abbr[title]{border-bottom:none;text-decoration:underline;text-decoration:underline dotted}dfn{font-style:italic}mark{background:#ff0;color:#000}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-.25em}sup{top:-.5em}figure{margin:1em 40px}img{border-style:none}svg:not (:root){overflow:hidden}code,kbd,pre,samp{font-family:monospace,monospace;font-size:1em}hr{box-sizing:content-box;height:0;overflow:visible}button,input,select,textarea{font:inherit;margin:0}optgroup{font-weight:700}button,input{overflow:visible}button,select{text-transform:none}[type=reset],[type=submit],button,html [type=button]{-webkit-appearance:button}[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner,button::-moz-focus-inner{border-style:none;padding:0}[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring,button:-moz-focusring{outline:1px dotted ButtonText}textarea{overflow:auto;border-radius:4px}[type=checkbox],[type=radio]{padding:0}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px}[type=search]::-webkit-search-cancel-button,[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-input-placeholder{color:inherit;opacity:.54}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit}html{overflow-x:hidden}.w3-slim,.w3-wide,h1,h2,h3,h4,h5,h6{font-family:Bookman Old Style!important}h1{font-size:36px}h2{font-size:30px}h3{font-size:24px}h4{font-size:20px}h5{font-size:18px}h6{font-size:16px}.w3-serif{font-family:\"Times New Roman\",Times,serif}h1,h2,h3,h4,h5,h6{font-weight:400;margin:10px 0}.w3-wide{letter-spacing:4px}h1 a,h2 a,h3 a,h4 a,h5 a,h6 a{font-weight:inherit}hr{border:0;border-top:1px solid #eee;margin:20px 0}img{margin-bottom:-5px}a{color:inherit}.w3-image{max-width:100%;height:auto}.w3-table,.w3-table-all{border-collapse:collapse;border-spacing:0;width:100%;display:table}.w3-table-all{border:1px solid #ccc}.w3-bordered tr,.w3-table-all tr{border-bottom:1px solid #ddd}.w3-striped tbody tr:nth-child(even){background-color:#f1f1f1}.w3-table-all tr:nth-child(odd){background-color:#fff}.w3-table-all tr:nth-child(even){background-color:#f1f1f1}.w3-hoverable tbody tr:hover,.w3-ul.w3-hoverable li:hover{background-color:#ccc}.w3-centered tr td,.w3-centered tr th{text-align:center}.w3-table td,.w3-table th,.w3-table-all td,.w3-table-all th{padding:8px 8px;display:table-cell;text-align:left;vertical-align:top}.w3-table td:first-child,.w3-table th:first-child,.w3-table-all td:first-child,.w3-table-all th:first-child{padding-left:16px}.w3-btn,.w3-btn-block{border:none;display:inline-block;outline:0;padding:6px 16px;vertical-align:middle;overflow:hidden;text-decoration:none!important;color:#fff;background-color:#000;text-align:center;cursor:pointer;white-space:nowrap}.w3-btn-block:hover,.w3-btn-floating-large:hover,.w3-btn-floating:hover,.w3-btn:hover{box-shadow:0 8px 16px 0 rgba(0,0,0,.2),0 6px 20px 0 rgba(0,0,0,.19)}.w3-btn,.w3-btn-block,.w3-btn-floating,.w3-btn-floating-large,.w3-closenav,.w3-opennav{-webkit-touch-callout:none;-webkit-user-select:none;-khtml-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none}.w3-btn-floating,.w3-btn-floating-large{display:inline-block;text-align:center;color:#fff;background-color:#000;position:relative;overflow:hidden;z-index:1;padding:0;border-radius:50%;cursor:pointer;font-size:24px}.w3-btn-floating{width:40px;height:40px;line-height:40px}.w3-btn-floating-large{width:56px;height:56px;line-height:56px}.w3-btn-floating-large:disabled,.w3-btn-floating:disabled,.w3-btn:disabled,.w3-disabled{cursor:not-allowed;opacity:.3}.w3-btn-block.w3-disabled,.w3-btn-floating.w3-disabled *,.w3-btn-floating:disabled *,.w3-btn.w3-disabled *,.w3-btn:disabled *{pointer-events:none}.w3-btn-block.w3-disabled:hover,.w3-btn-floating-large.w3-disabled:hover,.w3-btn-floating-large:disabled:hover,.w3-btn-floating.w3-disabled:hover,.w3-btn-floating:disabled:hover,.w3-btn.w3-disabled:hover,.w3-btn:disabled:hover{box-shadow:none}.w3-btn-group .w3-btn{float:left}.w3-btn-block{width:100%}.w3-btn-bar .w3-btn{box-shadow:none;background-color:inherit;color:inherit;float:left}.w3-btn-bar .w3-btn:hover{background-color:#ccc}.w3-badge,.w3-sign,.w3-tag{background-color:#000;color:#fff;display:inline-block;padding-left:8px;padding-right:8px;text-align:center}.w3-badge{border-radius:50%}ul.w3-ul{list-style-type:none;padding:0;margin:0}ul.w3-ul li{padding:6px 2px 6px 16px;border-bottom:1px solid #ddd}ul.w3-ul li:last-child{border-bottom:none}.w3-display-container,.w3-tooltip{position:relative}.w3-tooltip .w3-text{display:none}.w3-tooltip:hover .w3-text{display:inline-block}.w3-navbar{list-style-type:none;margin:0;padding:0;overflow:hidden}.w3-navbar li{float:left}.w3-navbar li .w3-btn,.w3-navbar li .w3-input{border:none;outline:0;width:100%}.w3-navbar li a:hover{color:#000;background-color:#ccc}.w3-navbar .w3-dropdown-click,.w3-navbar .w3-dropdown-hover{position:static}.w3-navbar .w3-dropdown-click:hover,.w3-navbar .w3-dropdown-hover:first-child,.w3-navbar .w3-dropdown-hover:hover{background-color:#ccc;color:#000}.w3-accordion-content a,.w3-dropdown-content a,.w3-dropnav a,.w3-navbar a,.w3-sidenav a,.w3-topnav a{text-decoration:none!important}.w3-navbar .w3-opennav.w3-right{float:right!important}.w3-topnav{padding:8px 8px}.w3-topnav a{padding:0 8px;border-bottom:3px solid transparent;-webkit-transition:border-bottom .25s;transition:border-bottom .25s}.w3-topnav a:hover{border-bottom:3px solid #fff}.w3-topnav .w3-dropdown-hover a{border-bottom:0}.w3-closenav,.w3-opennav{color:inherit}.w3-closenav:hover,.w3-opennav:hover{cursor:pointer;opacity:.8}.w3-accordion-content a,.w3-btn,.w3-btn-block,.w3-btn-floating,.w3-btn-floating-large,.w3-closebtn,.w3-closenav,.w3-dropdown-click:hover,.w3-dropdown-content a,.w3-dropdown-hover:hover,.w3-dropnav a,.w3-hoverable li,.w3-hoverable tbody tr,.w3-navbar a,.w3-opennav,.w3-pagination li a,.w3-sidenav a,[class*=w3-hover-]{-webkit-transition:background-color .25s,color .15s,box-shadow .25s,opacity .25s,filter .25s,border .15s;transition:background-color .25s,color .15s,box-shadow .15s,opacity .25s,filter .25s,border .15s}.w3-ripple:active{opacity:.5}.w3-ripple{-webkit-transition:opacity 0s;transition:opacity 0s}.w3-sidenav{height:100%;width:200px;background-color:#fff;position:fixed!important;z-index:1;overflow:auto}.w3-sidenav a{padding:4px 2px 4px 16px}.w3-sidenav a:hover{background-color:#ccc}.w3-dropnav a,.w3-sidenav a{display:block}.w3-dropnav a:hover,.w3-sidenav .w3-dropdown-click:hover,.w3-sidenav .w3-dropdown-hover:first-child,.w3-sidenav .w3-dropdown-hover:hover{background-color:#ccc;color:#000}.w3-sidenav .w3-dropdown-click,.w3-sidenav .w3-dropdown-hover{width:100%}.w3-sidenav .w3-dropdown-click .w3-dropdown-content,.w3-sidenav .w3-dropdown-hover .w3-dropdown-content{min-width:100%}#main,.w3-main{transition:margin-left .4s}.w3-modal{z-index:3;display:none;padding-top:100px;position:fixed;left:0;top:0;width:100%;height:100%;overflow:auto;background-color:#000;background-color:rgba(0,0,0,.4)}.w3-modal-content{margin:auto;background-color:#fff;position:relative;padding:0;outline:0;width:600px}.w3-closebtn{text-decoration:none;float:right;font-size:24px;font-weight:700;color:inherit}.w3-closebtn:focus,.w3-closebtn:hover{color:#000;text-decoration:none;cursor:pointer}.w3-pagination{display:inline-block;padding:0;margin:0}.w3-pagination li{display:inline}.w3-pagination li a{text-decoration:none;color:#000;float:left;padding:8px 16px}.w3-pagination li a:hover{background-color:#ccc}.w3-group,.w3-input-group{margin-top:24px;margin-bottom:24px}.w3-input{padding:8px;display:block;border:none;border-bottom:1px solid grey;width:96%}.w3-label{color:#333;text-align:left;width:100%}.w3-input:not (:valid)~.w3-validate{color:#f44336}.w3-select{padding:9px 0;width:100%;color:#000;border:1px solid transparent;border-bottom:1px solid #009688}.w3-select select:focus{color:#000;border:1px solid #009688}.w3-select option[disabled]{color:#009688}.w3-dropdown-click,.w3-dropdown-hover{position:relative;display:inline-block;cursor:pointer}.w3-dropdown-hover:hover .w3-dropdown-content{display:block;z-index:1}.w3-dropdown-content{cursor:auto;color:#000;background-color:#fff;display:none;position:absolute;min-width:160px;margin:0;padding:0}.w3-dropdown-content a{padding:6px 16px;display:block}.w3-dropdown-content a:hover{background-color:#ccc}.w3-accordion{width:100%;cursor:pointer}.w3-accordion-content{cursor:auto;display:none;position:relative;width:100%;margin:0;padding:0}.w3-accordion-content a{padding:6px 16px;display:block}.w3-accordion-content a:hover{background-color:#ccc}.w3-progress-container{width:100%;height:1.5em;position:relative;background-color:#f1f1f1}.w3-progressbar{background-color:#757575;height:100%;position:absolute;line-height:inherit}input[type=checkbox].w3-check:checked+.w3-validate,input[type=radio].w3-radio:checked+.w3-validate{color:#009688}input[type=checkbox].w3-check:disabled+.w3-validate,input[type=radio].w3-radio:disabled+.w3-validate{color:#aaa}.w3-responsive{overflow-x:auto}.w3-btn-bar:after,.w3-btn-bar:before,.w3-btn-group:after,.w3-btn-group:before,.w3-clear:after,.w3-container:after,.w3-panel:after,.w3-row-padding:after,.w3-row:after,.w3-topnav:after{content:\"\";display:table;clear:both}.w3-col,.w3-half,.w3-quarter,.w3-third,.w3-threequarter,.w3-twothird{float:left;width:100%}.w3-col.s1{width:8.33333%}.w3-col.s2{width:16.66666%}.w3-col.s3{width:24.99999%}.w3-col.s4{width:33.33333%}.w3-col.s5{width:41.66666%}.w3-col.s6{width:49.99999%}.w3-col.s7{width:58.33333%}.w3-col.s8{width:66.66666%}.w3-col.s9{width:74.99999%}.w3-col.s10{width:83.33333%}.w3-col.s11{width:91.66666%}.w3-col.s12,.w3-half,.w3-quarter,.w3-third,.w3-threequarter,.w3-twothird{width:99.99999%}@media only screen and (min-width:601px){.w3-col.m1{width:8.33333%}.w3-col.m2{width:16.66666%}.w3-col.m3,.w3-quarter{width:24.99999%}.w3-col.m4,.w3-third{width:33.33333%}.w3-col.m5{width:41.66666%}.w3-col.m6,.w3-half{width:49.99999%}.w3-col.m7{width:58.33333%}.w3-col.m8,.w3-twothird{width:66.66666%}.w3-col.m9,.w3-threequarter{width:74.99999%}.w3-col.m10{width:83.33333%}.w3-col.m11{width:91.66666%}.w3-col.m12{width:99.99999%}}@media only screen and (min-width:993px){.w3-col.l1{width:8.33333%}.w3-col.l2{width:16.66666%}.w3-col.l3,.w3-quarter{width:24.99999%}.w3-col.l4,.w3-third{width:33.33333%}.w3-col.l5{width:41.66666%}.w3-col.l6,.w3-half{width:49.99999%}.w3-col.l7{width:58.33333%}.w3-col.l8,.w3-twothird{width:66.66666%}.w3-col.l9,.w3-threequarter{width:74.99999%}.w3-col.l10{width:83.33333%}.w3-col.l11{width:91.66666%}.w3-col.l12{width:99.99999%}}.w3-content{max-width:980px;margin:auto}.w3-rest{overflow:hidden}.w3-hide{display:none!important}.w3-show,.w3-show-block{display:block!important}.w3-show-inline-block{display:inline-block!important}@media (max-width :600px){.w3-modal-content{margin:0 10px;width:auto!important}.w3-modal{padding-top:30px}}@media (max-width :768px){.w3-modal-content{width:500px}.w3-modal{padding-top:50px}}@media (min-width :993px){.w3-modal-content{width:900px}}@media screen and (max-width:600px){.w3-topnav a{display:block}.w3-navbar li:not (.w3-opennav){float:none;width:100%!important}.w3-navbar li.w3-right{float:none!important}}@media screen and (max-width:600px){.w3-navbar .w3-dropdown-click .w3-dropdown-content,.w3-navbar .w3-dropdown-hover .w3-dropdown-content,.w3-topnav .w3-dropdown-hover .w3-dropdown-content{position:relative}}@media screen and (max-width:600px){.w3-navbar,.w3-topnav{text-align:center}}@media (max-width :600px){.w3-hide-small{display:none!important}}@media (max-width :992px) and (min-width:601px){.w3-hide-medium{display:none!important}}@media (min-width :993px){.w3-hide-large{display:none!important}}@media screen and (max-width:992px){.w3-sidenav.w3-collapse{display:none}.w3-main{margin-left:0!important;margin-right:0!important}}@media screen and (min-width:993px){.w3-sidenav.w3-collapse{display:block!important}}.w3-bottom,.w3-top{position:fixed;width:100%;z-index:1}.w3-top{top:0}.w3-bottom{bottom:0}.w3-overlay{position:fixed;display:none;width:100%;height:100%;top:0;left:0;right:0;bottom:0;background-color:rgba(0,0,0,.5);z-index:2}.w3-left{float:left!important}.w3-right{float:right!important}.w3-tiny{font-size:10px!important}.w3-small{font-size:12px!important}.w3-medium{font-size:15px!important}.w3-large{font-size:18px!important}.w3-xlarge{font-size:24px!important}.w3-xxlarge{font-size:36px!important}.w3-xxxlarge{font-size:48px!important}.w3-jumbo{font-size:64px!important}.w3-vertical{word-break:break-all;line-height:1;text-align:center;width:.6em}.w3-left-align{text-align:left!important}.w3-right-align{text-align:right!important}.w3-justify{text-align:justify!important}.w3-center{text-align:center!important}.w3-display-topleft{position:absolute;left:0;top:0}.w3-display-topright{position:absolute;right:0;top:0}.w3-display-bottomleft{position:absolute;left:0;bottom:0}.w3-display-bottomright{position:absolute;right:0;bottom:0}.w3-display-middle{position:absolute;top:50%;left:50%;transform:translate(-50%,-50%);-ms-transform:translate(-50%,-50%)}.w3-display-left{position:absolute;top:50%;left:0;transform:translate(0,-50%);-ms-transform:translate(0,-50%)}.w3-display-right{position:absolute;top:50%;right:0;transform:translate(0,-50%);-ms-transform:translate(0,-50%)}.w3-display-topmiddle{position:absolute;left:50%;top:0;transform:translate(-50%,0);-ms-transform:translate(-50%,0)}.w3-display-bottommiddle{position:absolute;left:50%;bottom:0;transform:translate(-50%,0);-ms-transform:translate(-50%,0)}.w3-display-container:hover .w3-display-hover{display:block}.w3-display-container:hover span.w3-display-hover{display:inline-block}.w3-display-hover{display:none}.w3-circle{border-radius:50%!important}.w3-round-small{border-radius:2px!important}.w3-round,.w3-round-medium{border-radius:4px!important}.w3-round-large{border-radius:8px!important}.w3-round-xlarge{border-radius:16px!important}.w3-round-xxlarge{border-radius:32px!important}.w3-round-jumbo{border-radius:64px!important}.w3-border-0{border:0!important}.w3-border{border:1px solid #ccc!important}.w3-border-top{border-top:1px solid #ccc!important}.w3-border-bottom{border-bottom:1px solid #ccc!important}.w3-border-left{border-left:1px solid #ccc!important}.w3-border-right{border-right:1px solid #ccc!important}.w3-margin{margin:16px!important}.w3-margin-0{margin:0!important}.w3-margin-top{margin-top:16px!important}.w3-margin-bottom{margin-bottom:16px!important}.w3-margin-left{margin-left:16px!important}.w3-margin-right{margin-right:16px!important}.w3-section{margin-top:16px!important;margin-bottom:16px!important}.w3-padding-tiny{padding:2px 4px!important}.w3-padding-small{padding:4px 8px!important}.w3-form,.w3-padding,.w3-padding-medium{padding:8px 16px!important}.w3-padding-large{padding:12px 24px!important}.w3-padding-xlarge{padding:16px 32px!important}.w3-padding-xxlarge{padding:24px 48px!important}.w3-padding-jumbo{padding:32px 64px!important}.w3-padding-4{padding-top:4px!important;padding-bottom:4px!important}.w3-padding-8{padding-top:8px!important;padding-bottom:8px!important}.w3-padding-12{padding-top:12px!important;padding-bottom:12px!important}.w3-padding-16{padding-top:16px!important;padding-bottom:16px!important}.w3-padding-24{padding-top:24px!important;padding-bottom:24px!important}.w3-padding-32{padding-top:32px!important;padding-bottom:32px!important}.w3-padding-48{padding-top:48px!important;padding-bottom:48px!important}.w3-padding-64{padding-top:64px!important;padding-bottom:64px!important}.w3-padding-128{padding-top:128px!important;padding-bottom:128px!important}.w3-padding-0{padding:0!important}.w3-padding-top{padding-top:8px!important}.w3-padding-bottom{padding-bottom:8px!important}.w3-padding-left{padding-left:16px!important}.w3-padding-right{padding-right:16px!important}.w3-topbar{border-top:6px solid #ccc!important}.w3-bottombar{border-bottom:6px solid #ccc!important}.w3-leftbar{border-left:6px solid #ccc!important}.w3-rightbar{border-right:6px solid #ccc!important}.w3-row-padding,.w3-row-padding>.w3-col,.w3-row-padding>.w3-half,.w3-row-padding>.w3-quarter,.w3-row-padding>.w3-third,.w3-row-padding>.w3-threequarter,.w3-row-padding>.w3-twothird{padding:0 8px}.w3-spin{animation:w3-spin 2s infinite linear;-webkit-animation:w3-spin 2s infinite linear}@-webkit-keyframes w3-spin{transform:rotate(0)}100%{-webkit-transform:rotate(359deg);transform:rotate(359deg)}100%{-webkit-transform:rotate(359deg);transform:rotate(359deg)}.w3-panel{padding:.01em 16px;margin-top:16px!important;margin-bottom:16px!important}.w3-example{background-color:#f1f1f1;padding:.01em 16px}.w3-code,.w3-codespan{font-family:Consolas,\"courier new\";font-size:16px}.w3-code{line-height:1.4;width:auto;background-color:#fff;padding:8px 12px;border-left:4px solid #4caf50;word-wrap:break-word}.w3-codespan{color:#dc143c;background-color:#f1f1f1;padding-left:4px;padding-right:4px;font-size:110%}.w3-code,.w3-example{margin:20px 0}.w3-card{border:1px solid #ccc}.w3-card-2,.w3-example{box-shadow:0 2px 4px 0 rgba(0,0,0,.16),0 2px 10px 0 rgba(0,0,0,.12)!important}.w3-card-4,.w3-hover-shadow:hover{box-shadow:0 4px 8px 0 rgba(0,0,0,.2),0 6px 20px 0 rgba(0,0,0,.19)!important}.w3-card-8{box-shadow:0 8px 16px 0 rgba(0,0,0,.2),0 6px 20px 0 rgba(0,0,0,.19)!important}.w3-card-12{box-shadow:0 12px 16px 0 rgba(0,0,0,.24),0 17px 50px 0 rgba(0,0,0,.19)!important}.w3-card-16{box-shadow:0 16px 24px 0 rgba(0,0,0,.22),0 25px 55px 0 rgba(0,0,0,.21)!important}.w3-card-24{box-shadow:0 24px 24px 0 rgba(0,0,0,.2),0 40px 77px 0 rgba(0,0,0,.22)!important}.w3-animate-fading{-webkit-animation:fading 10s infinite;animation:fading 10s infinite}50%{opacity:1}100%{opacity:0}50%{opacity:1}100%{opacity:0}to{opacity:1}to{opacity:1}@-webkit-keyframes animatetop{opacity:0}to{top:0;opacity:1}to{top:0;opacity:1}@-webkit-keyframes animateleft{opacity:0}to{left:0;opacity:1}to{left:0;opacity:1}@-webkit-keyframes animateright{opacity:0}to{right:0;opacity:1}to{right:0;opacity:1}@-webkit-keyframes animatebottom{opacity:0}to{bottom:0;opacity:1}to{bottom:0;opacity:1}to{-webkit-transform:scale(1)}to{transform:scale(1)}.w3-animate-input:focus{width:100%!important}.w3-hover-opacity:hover,.w3-opacity{opacity:.6;-webkit-backface-visibility:hidden}.w3-hover-opacity-off:hover,.w3-opacity-off{opacity:1;-webkit-backface-visibility:hidden}.w3-opacity-max{opacity:.25;-webkit-backface-visibility:hidden}.w3-opacity-min{opacity:.75;-webkit-backface-visibility:hidden}.w3-grayscale-max,.w3-greyscale-max,.w3-hover-grayscale:hover,.w3-hover-greyscale:hover{-webkit-filter:grayscale(100%);filter:grayscale(100%)}.w3-grayscale,.w3-greyscale{-webkit-filter:grayscale(75%);filter:grayscale(75%)}.w3-grayscale-min,.w3-greyscale-min{-webkit-filter:grayscale(50%);filter:grayscale(50%)}.w3-sepia{-webkit-filter:sepia(75%);filter:sepia(75%)}.w3-hover-sepia:hover,.w3-sepia-max{-webkit-filter:sepia(100%);filter:sepia(100%)}.w3-sepia-min{-webkit-filter:sepia(50%);filter:sepia(50%)}.w3-text-shadow{text-shadow:1px 1px 0 #444}.w3-text-shadow-white{text-shadow:1px 1px 0 #ddd}.w3-transparent{background-color:transparent!important}.w3-hover-none:hover{box-shadow:none!important;background-color:transparent!important}.w3-amber,.w3-hover-amber:hover{color:#000!important;background-color:#ffc107!important}.w3-aqua,.w3-hover-aqua:hover{color:#000!important;background-color:#0ff!important}.w3-blue,.w3-hover-blue:hover{color:#fff!important;background-color:#2196f3!important}.w3-hover-light-blue:hover,.w3-light-blue{color:#000!important;background-color:#87ceeb!important}.w3-brown,.w3-hover-brown:hover{color:#fff!important;background-color:#795548!important}.w3-cyan,.w3-hover-cyan:hover{color:#000!important;background-color:#00bcd4!important}.w3-blue-gray,.w3-blue-grey,.w3-hover-blue-gray:hover,.w3-hover-blue-grey:hover{color:#fff!important;background-color:#607d8b!important}.w3-green,.w3-hover-green:hover{color:#fff!important;background-color:#4caf50!important}.w3-hover-light-green:hover,.w3-light-green{color:#000!important;background-color:#8bc34a!important}.w3-hover-indigo:hover,.w3-indigo{color:#fff!important;background-color:#3f51b5!important}.w3-hover-khaki:hover,.w3-khaki{color:#000!important;background-color:khaki!important}.w3-hover-lime:hover,.w3-lime{color:#000!important;background-color:#cddc39!important}.w3-hover-orange:hover,.w3-orange{color:#000!important;background-color:#ff9800!important}.w3-deep-orange,.w3-hover-deep-orange:hover{color:#fff!important;background-color:#ff5722!important}.w3-hover-pink:hover,.w3-pink{color:#fff!important;background-color:#e91e63!important}.w3-hover-purple:hover,.w3-purple{color:#fff!important;background-color:#9c27b0!important}.w3-deep-purple,.w3-hover-deep-purple:hover{color:#fff!important;background-color:#673ab7!important}.w3-hover-red:hover,.w3-red{color:#fff!important;background-color:#f44336!important}.w3-hover-sand:hover,.w3-sand{color:#000!important;background-color:#fdf5e6!important}.w3-hover-teal:hover,.w3-teal{color:#fff!important;background-color:#3d5c92!important}.w3-hover-yellow:hover,.w3-yellow{color:#000!important;background-color:#ffeb3b!important}.w3-hover-white:hover,.w3-white{color:#000!important;background-color:#fff!important}.w3-black,.w3-hover-black:hover{color:#fff!important;background-color:#000!important}.w3-gray,.w3-grey,.w3-hover-gray:hover,.w3-hover-grey:hover{color:#000!important;background-color:#9e9e9e!important}.w3-hover-light-gray:hover,.w3-hover-light-grey:hover,.w3-light-gray,.w3-light-grey{color:#000!important;background-color:#f1f1f1!important}.w3-dark-gray,.w3-dark-grey,.w3-hover-dark-gray:hover,.w3-hover-dark-grey:hover{color:#fff!important;background-color:#616161!important}.w3-hover-pale-red:hover,.w3-pale-red{color:#000!important;background-color:#fdd!important}.w3-hover-pale-green:hover,.w3-pale-green{color:#000!important;background-color:#dfd!important}.w3-hover-pale-yellow:hover,.w3-pale-yellow{color:#000!important;background-color:#ffc!important}.w3-hover-pale-blue:hover,.w3-pale-blue{color:#000!important;background-color:#dff!important}.w3-hover-text-amber:hover,.w3-text-amber{color:#ffc107!important}.w3-hover-text-aqua:hover,.w3-text-aqua{color:#0ff!important}.w3-hover-text-blue:hover,.w3-text-blue{color:#2196f3!important}.w3-hover-text-light-blue:hover,.w3-text-light-blue{color:#87ceeb!important}.w3-hover-text-brown:hover,.w3-text-brown{color:#795548!important}.w3-hover-text-cyan:hover,.w3-text-cyan{color:#00bcd4!important}.w3-hover-text-blue-gray:hover,.w3-hover-text-blue-grey:hover,.w3-text-blue-gray,.w3-text-blue-grey{color:#607d8b!important}.w3-hover-text-green:hover,.w3-text-green{color:#4caf50!important}.w3-hover-text-light-green:hover,.w3-text-light-green{color:#8bc34a!important}.w3-hover-text-indigo:hover,.w3-text-indigo{color:#3f51b5!important}.w3-hover-text-khaki:hover,.w3-text-khaki{color:#b4aa50!important}.w3-hover-text-lime:hover,.w3-text-lime{color:#cddc39!important}.w3-hover-text-orange:hover,.w3-text-orange{color:#ff9800!important}.w3-hover-text-deep-orange:hover,.w3-text-deep-orange{color:#ff5722!important}.w3-hover-text-pink:hover,.w3-text-pink{color:#e91e63!important}.w3-hover-text-purple:hover,.w3-text-purple{color:#9c27b0!important}.w3-hover-text-deep-purple:hover,.w3-text-deep-purple{color:#673ab7!important}.w3-hover-text-red:hover,.w3-text-red{color:#f44336!important}.w3-hover-text-sand:hover,.w3-text-sand{color:#fdf5e6!important}.w3-hover-text-teal:hover,.w3-text-teal{color:#009688!important}.w3-hover-text-yellow:hover,.w3-text-yellow{color:#d2be0e!important}.w3-hover-text-white:hover,.w3-text-white{color:#fff!important}.w3-hover-text-black:hover,.w3-text-black{color:#000!important}.w3-hover-text-gray:hover,.w3-hover-text-grey:hover,.w3-text-gray,.w3-text-grey{color:#757575!important}.w3-hover-text-light-gray:hover,.w3-hover-text-light-grey:hover,.w3-text-light-gray,.w3-text-light-grey{color:#f1f1f1!important}.w3-hover-text-dark-gray:hover,.w3-hover-text-dark-grey:hover,.w3-text-dark-gray,.w3-text-dark-grey{color:#3a3a3a!important}.w3-border-amber,.w3-hover-border-amber:hover{border-color:#ffc107!important}.w3-border-aqua,.w3-hover-border-aqua:hover{border-color:#0ff!important}.w3-border-blue,.w3-hover-border-blue:hover{border-color:#2196f3!important}.w3-border-light-blue,.w3-hover-border-light-blue:hover{border-color:#87ceeb!important}.w3-border-brown,.w3-hover-border-brown:hover{border-color:#795548!important}.w3-border-cyan,.w3-hover-border-cyan:hover{border-color:#00bcd4!important}.w3-border-blue-gray,.w3-border-blue-grey,.w3-hover-border-blue-gray:hover,.w3-hover-border-blue-grey:hover{border-color:#607d8b!important}.w3-border-green,.w3-hover-border-green:hover{border-color:#4caf50!important}.w3-border-light-green,.w3-hover-border-light-green:hover{border-color:#8bc34a!important}.w3-border-indigo,.w3-hover-border-indigo:hover{border-color:#3f51b5!important}.w3-border-khaki,.w3-hover-border-khaki:hover{border-color:khaki!important}.w3-border-lime,.w3-hover-border-lime:hover{border-color:#cddc39!important}.w3-border-orange,.w3-hover-border-orange:hover{border-color:#ff9800!important}.w3-border-deep-orange,.w3-hover-border-deep-orange:hover{border-color:#ff5722!important}.w3-border-pink,.w3-hover-border-pink:hover{border-color:#e91e63!important}.w3-border-purple,.w3-hover-border-purple:hover{border-color:#9c27b0!important}.w3-border-deep-purple,.w3-hover-border-deep-purple:hover{border-color:#673ab7!important}.w3-border-red,.w3-hover-border-red:hover{border-color:#f44336!important}.w3-border-sand,.w3-hover-border-sand:hover{border-color:#fdf5e6!important}.w3-border-teal,.w3-hover-border-teal:hover{border-color:#009688!important}.w3-border-yellow,.w3-hover-border-yellow:hover{border-color:#ffeb3b!important}.w3-border-white,.w3-hover-border-white:hover{border-color:#fff!important}.w3-border-black,.w3-hover-border-black:hover{border-color:#000!important}.w3-border-gray,.w3-border-grey,.w3-hover-border-gray:hover,.w3-hover-border-grey:hover{border-color:#9e9e9e!important}.w3-border-light-gray,.w3-border-light-grey,.w3-hover-border-light-gray:hover,.w3-hover-border-light-grey:hover{border-color:#f1f1f1!important}.w3-border-dark-gray,.w3-border-dark-grey,.w3-hover-border-dark-gray:hover,.w3-hover-border-dark-grey:hover{border-color:#616161!important}.w3-border-pale-red,.w3-hover-border-pale-red:hover{border-color:#ffe7e7!important}.w3-border-pale-green,.w3-hover-border-pale-green:hover{border-color:#e7ffe7!important}.w3-border-pale-yellow,.w3-hover-border-pale-yellow:hover{border-color:#ffc!important}.w3-border-pale-blue,.w3-hover-border-pale-blue:hover{border-color:#e7ffff!important}.box-shadow{box-shadow:1px 0 1px 1px #ccc;margin-bottom:12px}.dropdown-toggle{width:200%!important}.vitals .btn-default{background-color:#fff!important}.vitals .btn-default:hover{background-color:#fff!important}.submit-button{float:right}.submit-button .btn-default{background-color:#00bcd4!important;color:#fff!important;border-color:#ddd!important}ul.dropdown-menu{width:260px!important;overflow-y:scroll!important}.padding-bottom{padding-bottom:15px}.card-margin{margin:16px}.bg-white{background-color:#fff}.bg-lightgrey{background-color:#fff}.padding-top{padding-top:15px}.panel-height{height:600px;overflow-x:scroll}.annotation-modal{background-color:transparent!important;margin-left:1%!important;width:98%!important;height:98%!important;margin-top:0!important;padding-right:0!important}.modal-open .modal{overflow-x:hidden!important;overflow-y:hidden!important}.modal{background-color:#fff;margin-left:25%;width:50%;height:70%;margin-top:4%;padding-right:0!important}#viewTemplateModal .modal{background-color:#fff;margin-left:0;width:80%;height:70%;margin-top:4%;padding-right:0!important}.header1{font-size:40px}.header2{font-size:30px}.header3{font-size:25px}.header4{font-size:18px}.header5{font-size:14px}.header6{font-size:12px}.heading-text-align-left{text-align:left!important}.heading-text-align-center{text-align:center}.table-td{border:1px solid rgba(128,128,128,.6);padding:2px;width:10%;max-width:10%}table{width:100%}#container{position:relative}.left-card-margin{margin:4px 0 4px 0}.dragged-radio{margin-left:5px!important;margin-right:15px!important}.padding0{padding-left:0} </style></head>" + dto.getTemplateObjectHtml();
						dto.setTemplateObjectHtml(replacedString);		    
					}  //dilip
			
			TemplateMaster master = factory.map(dto, TemplateMaster.class, null);
			master.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master = save(master);
			dto.setTemplateId(master.getTemplateId());
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, dto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getTemplateList(int orgId, int unitId) throws ApplicationException {
		try{
			List<TemplateMasterDto> listTemplateMasterDto = sessionFactory.getCurrentSession().getNamedQuery("GET_TEMPLATE_LIST").setParameter("orgId", orgId).setParameter("unitId", unitId)
						.setResultTransformer(Transformers.aliasToBean(TemplateMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTemplateMasterDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getTemplateById(int id) throws ApplicationException {
		try{
			TemplateMasterDto dto = (TemplateMasterDto)sessionFactory.getCurrentSession().getNamedQuery("GET_TEMPLATE_BY_ID").setParameter("templateId", id).setResultTransformer(Transformers.aliasToBean(TemplateMasterDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateTemplateStatus(int id, char status) throws ApplicationException {
		try{
			Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_TEMPLATE_STATUS_BY_ID").setParameter("id", id).setCharacter("status", status).executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, result);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateTemplate(TemplateMasterDto dto) throws ApplicationException {
		try{
			if(dto.getTemplateObjectHtml() != null){
				String replacedString  = "<head><style> html{box-sizing:border-box;margin:15px;line-height:1.5}*,:after,:before{box-sizing:inherit}html{-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%}body{margin:0}article,aside,details,figcaption,figure,footer,header,main,menu,nav,section,summary{display:block}audio,canvas,progress,video{display:inline-block}progress{vertical-align:baseline}audio:not ([controls]){display:none;height:0}[hidden],template{display:none}a{background-color:transparent;-webkit-text-decoration-skip:objects}a:active,a:hover{outline-width:0}abbr[title]{border-bottom:none;text-decoration:underline;text-decoration:underline dotted}dfn{font-style:italic}mark{background:#ff0;color:#000}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-.25em}sup{top:-.5em}figure{margin:1em 40px}img{border-style:none}svg:not (:root){overflow:hidden}code,kbd,pre,samp{font-family:monospace,monospace;font-size:1em}hr{box-sizing:content-box;height:0;overflow:visible}button,input,select,textarea{font:inherit;margin:0}optgroup{font-weight:700}button,input{overflow:visible}button,select{text-transform:none}[type=reset],[type=submit],button,html [type=button]{-webkit-appearance:button}[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner,button::-moz-focus-inner{border-style:none;padding:0}[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring,button:-moz-focusring{outline:1px dotted ButtonText}textarea{overflow:auto;border-radius:4px}[type=checkbox],[type=radio]{padding:0}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px}[type=search]::-webkit-search-cancel-button,[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-input-placeholder{color:inherit;opacity:.54}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit}html{overflow-x:hidden}.w3-slim,.w3-wide,h1,h2,h3,h4,h5,h6{font-family:Bookman Old Style!important}h1{font-size:36px}h2{font-size:30px}h3{font-size:24px}h4{font-size:20px}h5{font-size:18px}h6{font-size:16px}.w3-serif{font-family:\"Times New Roman\",Times,serif}h1,h2,h3,h4,h5,h6{font-weight:400;margin:10px 0}.w3-wide{letter-spacing:4px}h1 a,h2 a,h3 a,h4 a,h5 a,h6 a{font-weight:inherit}hr{border:0;border-top:1px solid #eee;margin:20px 0}img{margin-bottom:-5px}a{color:inherit}.w3-image{max-width:100%;height:auto}.w3-table,.w3-table-all{border-collapse:collapse;border-spacing:0;width:100%;display:table}.w3-table-all{border:1px solid #ccc}.w3-bordered tr,.w3-table-all tr{border-bottom:1px solid #ddd}.w3-striped tbody tr:nth-child(even){background-color:#f1f1f1}.w3-table-all tr:nth-child(odd){background-color:#fff}.w3-table-all tr:nth-child(even){background-color:#f1f1f1}.w3-hoverable tbody tr:hover,.w3-ul.w3-hoverable li:hover{background-color:#ccc}.w3-centered tr td,.w3-centered tr th{text-align:center}.w3-table td,.w3-table th,.w3-table-all td,.w3-table-all th{padding:8px 8px;display:table-cell;text-align:left;vertical-align:top}.w3-table td:first-child,.w3-table th:first-child,.w3-table-all td:first-child,.w3-table-all th:first-child{padding-left:16px}.w3-btn,.w3-btn-block{border:none;display:inline-block;outline:0;padding:6px 16px;vertical-align:middle;overflow:hidden;text-decoration:none!important;color:#fff;background-color:#000;text-align:center;cursor:pointer;white-space:nowrap}.w3-btn-block:hover,.w3-btn-floating-large:hover,.w3-btn-floating:hover,.w3-btn:hover{box-shadow:0 8px 16px 0 rgba(0,0,0,.2),0 6px 20px 0 rgba(0,0,0,.19)}.w3-btn,.w3-btn-block,.w3-btn-floating,.w3-btn-floating-large,.w3-closenav,.w3-opennav{-webkit-touch-callout:none;-webkit-user-select:none;-khtml-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none}.w3-btn-floating,.w3-btn-floating-large{display:inline-block;text-align:center;color:#fff;background-color:#000;position:relative;overflow:hidden;z-index:1;padding:0;border-radius:50%;cursor:pointer;font-size:24px}.w3-btn-floating{width:40px;height:40px;line-height:40px}.w3-btn-floating-large{width:56px;height:56px;line-height:56px}.w3-btn-floating-large:disabled,.w3-btn-floating:disabled,.w3-btn:disabled,.w3-disabled{cursor:not-allowed;opacity:.3}.w3-btn-block.w3-disabled,.w3-btn-floating.w3-disabled *,.w3-btn-floating:disabled *,.w3-btn.w3-disabled *,.w3-btn:disabled *{pointer-events:none}.w3-btn-block.w3-disabled:hover,.w3-btn-floating-large.w3-disabled:hover,.w3-btn-floating-large:disabled:hover,.w3-btn-floating.w3-disabled:hover,.w3-btn-floating:disabled:hover,.w3-btn.w3-disabled:hover,.w3-btn:disabled:hover{box-shadow:none}.w3-btn-group .w3-btn{float:left}.w3-btn-block{width:100%}.w3-btn-bar .w3-btn{box-shadow:none;background-color:inherit;color:inherit;float:left}.w3-btn-bar .w3-btn:hover{background-color:#ccc}.w3-badge,.w3-sign,.w3-tag{background-color:#000;color:#fff;display:inline-block;padding-left:8px;padding-right:8px;text-align:center}.w3-badge{border-radius:50%}ul.w3-ul{list-style-type:none;padding:0;margin:0}ul.w3-ul li{padding:6px 2px 6px 16px;border-bottom:1px solid #ddd}ul.w3-ul li:last-child{border-bottom:none}.w3-display-container,.w3-tooltip{position:relative}.w3-tooltip .w3-text{display:none}.w3-tooltip:hover .w3-text{display:inline-block}.w3-navbar{list-style-type:none;margin:0;padding:0;overflow:hidden}.w3-navbar li{float:left}.w3-navbar li .w3-btn,.w3-navbar li .w3-input{border:none;outline:0;width:100%}.w3-navbar li a:hover{color:#000;background-color:#ccc}.w3-navbar .w3-dropdown-click,.w3-navbar .w3-dropdown-hover{position:static}.w3-navbar .w3-dropdown-click:hover,.w3-navbar .w3-dropdown-hover:first-child,.w3-navbar .w3-dropdown-hover:hover{background-color:#ccc;color:#000}.w3-accordion-content a,.w3-dropdown-content a,.w3-dropnav a,.w3-navbar a,.w3-sidenav a,.w3-topnav a{text-decoration:none!important}.w3-navbar .w3-opennav.w3-right{float:right!important}.w3-topnav{padding:8px 8px}.w3-topnav a{padding:0 8px;border-bottom:3px solid transparent;-webkit-transition:border-bottom .25s;transition:border-bottom .25s}.w3-topnav a:hover{border-bottom:3px solid #fff}.w3-topnav .w3-dropdown-hover a{border-bottom:0}.w3-closenav,.w3-opennav{color:inherit}.w3-closenav:hover,.w3-opennav:hover{cursor:pointer;opacity:.8}.w3-accordion-content a,.w3-btn,.w3-btn-block,.w3-btn-floating,.w3-btn-floating-large,.w3-closebtn,.w3-closenav,.w3-dropdown-click:hover,.w3-dropdown-content a,.w3-dropdown-hover:hover,.w3-dropnav a,.w3-hoverable li,.w3-hoverable tbody tr,.w3-navbar a,.w3-opennav,.w3-pagination li a,.w3-sidenav a,[class*=w3-hover-]{-webkit-transition:background-color .25s,color .15s,box-shadow .25s,opacity .25s,filter .25s,border .15s;transition:background-color .25s,color .15s,box-shadow .15s,opacity .25s,filter .25s,border .15s}.w3-ripple:active{opacity:.5}.w3-ripple{-webkit-transition:opacity 0s;transition:opacity 0s}.w3-sidenav{height:100%;width:200px;background-color:#fff;position:fixed!important;z-index:1;overflow:auto}.w3-sidenav a{padding:4px 2px 4px 16px}.w3-sidenav a:hover{background-color:#ccc}.w3-dropnav a,.w3-sidenav a{display:block}.w3-dropnav a:hover,.w3-sidenav .w3-dropdown-click:hover,.w3-sidenav .w3-dropdown-hover:first-child,.w3-sidenav .w3-dropdown-hover:hover{background-color:#ccc;color:#000}.w3-sidenav .w3-dropdown-click,.w3-sidenav .w3-dropdown-hover{width:100%}.w3-sidenav .w3-dropdown-click .w3-dropdown-content,.w3-sidenav .w3-dropdown-hover .w3-dropdown-content{min-width:100%}#main,.w3-main{transition:margin-left .4s}.w3-modal{z-index:3;display:none;padding-top:100px;position:fixed;left:0;top:0;width:100%;height:100%;overflow:auto;background-color:#000;background-color:rgba(0,0,0,.4)}.w3-modal-content{margin:auto;background-color:#fff;position:relative;padding:0;outline:0;width:600px}.w3-closebtn{text-decoration:none;float:right;font-size:24px;font-weight:700;color:inherit}.w3-closebtn:focus,.w3-closebtn:hover{color:#000;text-decoration:none;cursor:pointer}.w3-pagination{display:inline-block;padding:0;margin:0}.w3-pagination li{display:inline}.w3-pagination li a{text-decoration:none;color:#000;float:left;padding:8px 16px}.w3-pagination li a:hover{background-color:#ccc}.w3-group,.w3-input-group{margin-top:24px;margin-bottom:24px}.w3-input{padding:8px;display:block;border:none;border-bottom:1px solid grey;width:96%}.w3-label{color:#333;text-align:left;width:100%}.w3-input:not (:valid)~.w3-validate{color:#f44336}.w3-select{padding:9px 0;width:100%;color:#000;border:1px solid transparent;border-bottom:1px solid #009688}.w3-select select:focus{color:#000;border:1px solid #009688}.w3-select option[disabled]{color:#009688}.w3-dropdown-click,.w3-dropdown-hover{position:relative;display:inline-block;cursor:pointer}.w3-dropdown-hover:hover .w3-dropdown-content{display:block;z-index:1}.w3-dropdown-content{cursor:auto;color:#000;background-color:#fff;display:none;position:absolute;min-width:160px;margin:0;padding:0}.w3-dropdown-content a{padding:6px 16px;display:block}.w3-dropdown-content a:hover{background-color:#ccc}.w3-accordion{width:100%;cursor:pointer}.w3-accordion-content{cursor:auto;display:none;position:relative;width:100%;margin:0;padding:0}.w3-accordion-content a{padding:6px 16px;display:block}.w3-accordion-content a:hover{background-color:#ccc}.w3-progress-container{width:100%;height:1.5em;position:relative;background-color:#f1f1f1}.w3-progressbar{background-color:#757575;height:100%;position:absolute;line-height:inherit}input[type=checkbox].w3-check:checked+.w3-validate,input[type=radio].w3-radio:checked+.w3-validate{color:#009688}input[type=checkbox].w3-check:disabled+.w3-validate,input[type=radio].w3-radio:disabled+.w3-validate{color:#aaa}.w3-responsive{overflow-x:auto}.w3-btn-bar:after,.w3-btn-bar:before,.w3-btn-group:after,.w3-btn-group:before,.w3-clear:after,.w3-container:after,.w3-panel:after,.w3-row-padding:after,.w3-row:after,.w3-topnav:after{content:\"\";display:table;clear:both}.w3-col,.w3-half,.w3-quarter,.w3-third,.w3-threequarter,.w3-twothird{float:left;width:100%}.w3-col.s1{width:8.33333%}.w3-col.s2{width:16.66666%}.w3-col.s3{width:24.99999%}.w3-col.s4{width:33.33333%}.w3-col.s5{width:41.66666%}.w3-col.s6{width:49.99999%}.w3-col.s7{width:58.33333%}.w3-col.s8{width:66.66666%}.w3-col.s9{width:74.99999%}.w3-col.s10{width:83.33333%}.w3-col.s11{width:91.66666%}.w3-col.s12,.w3-half,.w3-quarter,.w3-third,.w3-threequarter,.w3-twothird{width:99.99999%}@media only screen and (min-width:601px){.w3-col.m1{width:8.33333%}.w3-col.m2{width:16.66666%}.w3-col.m3,.w3-quarter{width:24.99999%}.w3-col.m4,.w3-third{width:33.33333%}.w3-col.m5{width:41.66666%}.w3-col.m6,.w3-half{width:49.99999%}.w3-col.m7{width:58.33333%}.w3-col.m8,.w3-twothird{width:66.66666%}.w3-col.m9,.w3-threequarter{width:74.99999%}.w3-col.m10{width:83.33333%}.w3-col.m11{width:91.66666%}.w3-col.m12{width:99.99999%}}@media only screen and (min-width:993px){.w3-col.l1{width:8.33333%}.w3-col.l2{width:16.66666%}.w3-col.l3,.w3-quarter{width:24.99999%}.w3-col.l4,.w3-third{width:33.33333%}.w3-col.l5{width:41.66666%}.w3-col.l6,.w3-half{width:49.99999%}.w3-col.l7{width:58.33333%}.w3-col.l8,.w3-twothird{width:66.66666%}.w3-col.l9,.w3-threequarter{width:74.99999%}.w3-col.l10{width:83.33333%}.w3-col.l11{width:91.66666%}.w3-col.l12{width:99.99999%}}.w3-content{max-width:980px;margin:auto}.w3-rest{overflow:hidden}.w3-hide{display:none!important}.w3-show,.w3-show-block{display:block!important}.w3-show-inline-block{display:inline-block!important}@media (max-width :600px){.w3-modal-content{margin:0 10px;width:auto!important}.w3-modal{padding-top:30px}}@media (max-width :768px){.w3-modal-content{width:500px}.w3-modal{padding-top:50px}}@media (min-width :993px){.w3-modal-content{width:900px}}@media screen and (max-width:600px){.w3-topnav a{display:block}.w3-navbar li:not (.w3-opennav){float:none;width:100%!important}.w3-navbar li.w3-right{float:none!important}}@media screen and (max-width:600px){.w3-navbar .w3-dropdown-click .w3-dropdown-content,.w3-navbar .w3-dropdown-hover .w3-dropdown-content,.w3-topnav .w3-dropdown-hover .w3-dropdown-content{position:relative}}@media screen and (max-width:600px){.w3-navbar,.w3-topnav{text-align:center}}@media (max-width :600px){.w3-hide-small{display:none!important}}@media (max-width :992px) and (min-width:601px){.w3-hide-medium{display:none!important}}@media (min-width :993px){.w3-hide-large{display:none!important}}@media screen and (max-width:992px){.w3-sidenav.w3-collapse{display:none}.w3-main{margin-left:0!important;margin-right:0!important}}@media screen and (min-width:993px){.w3-sidenav.w3-collapse{display:block!important}}.w3-bottom,.w3-top{position:fixed;width:100%;z-index:1}.w3-top{top:0}.w3-bottom{bottom:0}.w3-overlay{position:fixed;display:none;width:100%;height:100%;top:0;left:0;right:0;bottom:0;background-color:rgba(0,0,0,.5);z-index:2}.w3-left{float:left!important}.w3-right{float:right!important}.w3-tiny{font-size:10px!important}.w3-small{font-size:12px!important}.w3-medium{font-size:15px!important}.w3-large{font-size:18px!important}.w3-xlarge{font-size:24px!important}.w3-xxlarge{font-size:36px!important}.w3-xxxlarge{font-size:48px!important}.w3-jumbo{font-size:64px!important}.w3-vertical{word-break:break-all;line-height:1;text-align:center;width:.6em}.w3-left-align{text-align:left!important}.w3-right-align{text-align:right!important}.w3-justify{text-align:justify!important}.w3-center{text-align:center!important}.w3-display-topleft{position:absolute;left:0;top:0}.w3-display-topright{position:absolute;right:0;top:0}.w3-display-bottomleft{position:absolute;left:0;bottom:0}.w3-display-bottomright{position:absolute;right:0;bottom:0}.w3-display-middle{position:absolute;top:50%;left:50%;transform:translate(-50%,-50%);-ms-transform:translate(-50%,-50%)}.w3-display-left{position:absolute;top:50%;left:0;transform:translate(0,-50%);-ms-transform:translate(0,-50%)}.w3-display-right{position:absolute;top:50%;right:0;transform:translate(0,-50%);-ms-transform:translate(0,-50%)}.w3-display-topmiddle{position:absolute;left:50%;top:0;transform:translate(-50%,0);-ms-transform:translate(-50%,0)}.w3-display-bottommiddle{position:absolute;left:50%;bottom:0;transform:translate(-50%,0);-ms-transform:translate(-50%,0)}.w3-display-container:hover .w3-display-hover{display:block}.w3-display-container:hover span.w3-display-hover{display:inline-block}.w3-display-hover{display:none}.w3-circle{border-radius:50%!important}.w3-round-small{border-radius:2px!important}.w3-round,.w3-round-medium{border-radius:4px!important}.w3-round-large{border-radius:8px!important}.w3-round-xlarge{border-radius:16px!important}.w3-round-xxlarge{border-radius:32px!important}.w3-round-jumbo{border-radius:64px!important}.w3-border-0{border:0!important}.w3-border{border:1px solid #ccc!important}.w3-border-top{border-top:1px solid #ccc!important}.w3-border-bottom{border-bottom:1px solid #ccc!important}.w3-border-left{border-left:1px solid #ccc!important}.w3-border-right{border-right:1px solid #ccc!important}.w3-margin{margin:16px!important}.w3-margin-0{margin:0!important}.w3-margin-top{margin-top:16px!important}.w3-margin-bottom{margin-bottom:16px!important}.w3-margin-left{margin-left:16px!important}.w3-margin-right{margin-right:16px!important}.w3-section{margin-top:16px!important;margin-bottom:16px!important}.w3-padding-tiny{padding:2px 4px!important}.w3-padding-small{padding:4px 8px!important}.w3-form,.w3-padding,.w3-padding-medium{padding:8px 16px!important}.w3-padding-large{padding:12px 24px!important}.w3-padding-xlarge{padding:16px 32px!important}.w3-padding-xxlarge{padding:24px 48px!important}.w3-padding-jumbo{padding:32px 64px!important}.w3-padding-4{padding-top:4px!important;padding-bottom:4px!important}.w3-padding-8{padding-top:8px!important;padding-bottom:8px!important}.w3-padding-12{padding-top:12px!important;padding-bottom:12px!important}.w3-padding-16{padding-top:16px!important;padding-bottom:16px!important}.w3-padding-24{padding-top:24px!important;padding-bottom:24px!important}.w3-padding-32{padding-top:32px!important;padding-bottom:32px!important}.w3-padding-48{padding-top:48px!important;padding-bottom:48px!important}.w3-padding-64{padding-top:64px!important;padding-bottom:64px!important}.w3-padding-128{padding-top:128px!important;padding-bottom:128px!important}.w3-padding-0{padding:0!important}.w3-padding-top{padding-top:8px!important}.w3-padding-bottom{padding-bottom:8px!important}.w3-padding-left{padding-left:16px!important}.w3-padding-right{padding-right:16px!important}.w3-topbar{border-top:6px solid #ccc!important}.w3-bottombar{border-bottom:6px solid #ccc!important}.w3-leftbar{border-left:6px solid #ccc!important}.w3-rightbar{border-right:6px solid #ccc!important}.w3-row-padding,.w3-row-padding>.w3-col,.w3-row-padding>.w3-half,.w3-row-padding>.w3-quarter,.w3-row-padding>.w3-third,.w3-row-padding>.w3-threequarter,.w3-row-padding>.w3-twothird{padding:0 8px}.w3-spin{animation:w3-spin 2s infinite linear;-webkit-animation:w3-spin 2s infinite linear}@-webkit-keyframes w3-spin{transform:rotate(0)}100%{-webkit-transform:rotate(359deg);transform:rotate(359deg)}100%{-webkit-transform:rotate(359deg);transform:rotate(359deg)}.w3-panel{padding:.01em 16px;margin-top:16px!important;margin-bottom:16px!important}.w3-example{background-color:#f1f1f1;padding:.01em 16px}.w3-code,.w3-codespan{font-family:Consolas,\"courier new\";font-size:16px}.w3-code{line-height:1.4;width:auto;background-color:#fff;padding:8px 12px;border-left:4px solid #4caf50;word-wrap:break-word}.w3-codespan{color:#dc143c;background-color:#f1f1f1;padding-left:4px;padding-right:4px;font-size:110%}.w3-code,.w3-example{margin:20px 0}.w3-card{border:1px solid #ccc}.w3-card-2,.w3-example{box-shadow:0 2px 4px 0 rgba(0,0,0,.16),0 2px 10px 0 rgba(0,0,0,.12)!important}.w3-card-4,.w3-hover-shadow:hover{box-shadow:0 4px 8px 0 rgba(0,0,0,.2),0 6px 20px 0 rgba(0,0,0,.19)!important}.w3-card-8{box-shadow:0 8px 16px 0 rgba(0,0,0,.2),0 6px 20px 0 rgba(0,0,0,.19)!important}.w3-card-12{box-shadow:0 12px 16px 0 rgba(0,0,0,.24),0 17px 50px 0 rgba(0,0,0,.19)!important}.w3-card-16{box-shadow:0 16px 24px 0 rgba(0,0,0,.22),0 25px 55px 0 rgba(0,0,0,.21)!important}.w3-card-24{box-shadow:0 24px 24px 0 rgba(0,0,0,.2),0 40px 77px 0 rgba(0,0,0,.22)!important}.w3-animate-fading{-webkit-animation:fading 10s infinite;animation:fading 10s infinite}50%{opacity:1}100%{opacity:0}50%{opacity:1}100%{opacity:0}to{opacity:1}to{opacity:1}@-webkit-keyframes animatetop{opacity:0}to{top:0;opacity:1}to{top:0;opacity:1}@-webkit-keyframes animateleft{opacity:0}to{left:0;opacity:1}to{left:0;opacity:1}@-webkit-keyframes animateright{opacity:0}to{right:0;opacity:1}to{right:0;opacity:1}@-webkit-keyframes animatebottom{opacity:0}to{bottom:0;opacity:1}to{bottom:0;opacity:1}to{-webkit-transform:scale(1)}to{transform:scale(1)}.w3-animate-input:focus{width:100%!important}.w3-hover-opacity:hover,.w3-opacity{opacity:.6;-webkit-backface-visibility:hidden}.w3-hover-opacity-off:hover,.w3-opacity-off{opacity:1;-webkit-backface-visibility:hidden}.w3-opacity-max{opacity:.25;-webkit-backface-visibility:hidden}.w3-opacity-min{opacity:.75;-webkit-backface-visibility:hidden}.w3-grayscale-max,.w3-greyscale-max,.w3-hover-grayscale:hover,.w3-hover-greyscale:hover{-webkit-filter:grayscale(100%);filter:grayscale(100%)}.w3-grayscale,.w3-greyscale{-webkit-filter:grayscale(75%);filter:grayscale(75%)}.w3-grayscale-min,.w3-greyscale-min{-webkit-filter:grayscale(50%);filter:grayscale(50%)}.w3-sepia{-webkit-filter:sepia(75%);filter:sepia(75%)}.w3-hover-sepia:hover,.w3-sepia-max{-webkit-filter:sepia(100%);filter:sepia(100%)}.w3-sepia-min{-webkit-filter:sepia(50%);filter:sepia(50%)}.w3-text-shadow{text-shadow:1px 1px 0 #444}.w3-text-shadow-white{text-shadow:1px 1px 0 #ddd}.w3-transparent{background-color:transparent!important}.w3-hover-none:hover{box-shadow:none!important;background-color:transparent!important}.w3-amber,.w3-hover-amber:hover{color:#000!important;background-color:#ffc107!important}.w3-aqua,.w3-hover-aqua:hover{color:#000!important;background-color:#0ff!important}.w3-blue,.w3-hover-blue:hover{color:#fff!important;background-color:#2196f3!important}.w3-hover-light-blue:hover,.w3-light-blue{color:#000!important;background-color:#87ceeb!important}.w3-brown,.w3-hover-brown:hover{color:#fff!important;background-color:#795548!important}.w3-cyan,.w3-hover-cyan:hover{color:#000!important;background-color:#00bcd4!important}.w3-blue-gray,.w3-blue-grey,.w3-hover-blue-gray:hover,.w3-hover-blue-grey:hover{color:#fff!important;background-color:#607d8b!important}.w3-green,.w3-hover-green:hover{color:#fff!important;background-color:#4caf50!important}.w3-hover-light-green:hover,.w3-light-green{color:#000!important;background-color:#8bc34a!important}.w3-hover-indigo:hover,.w3-indigo{color:#fff!important;background-color:#3f51b5!important}.w3-hover-khaki:hover,.w3-khaki{color:#000!important;background-color:khaki!important}.w3-hover-lime:hover,.w3-lime{color:#000!important;background-color:#cddc39!important}.w3-hover-orange:hover,.w3-orange{color:#000!important;background-color:#ff9800!important}.w3-deep-orange,.w3-hover-deep-orange:hover{color:#fff!important;background-color:#ff5722!important}.w3-hover-pink:hover,.w3-pink{color:#fff!important;background-color:#e91e63!important}.w3-hover-purple:hover,.w3-purple{color:#fff!important;background-color:#9c27b0!important}.w3-deep-purple,.w3-hover-deep-purple:hover{color:#fff!important;background-color:#673ab7!important}.w3-hover-red:hover,.w3-red{color:#fff!important;background-color:#f44336!important}.w3-hover-sand:hover,.w3-sand{color:#000!important;background-color:#fdf5e6!important}.w3-hover-teal:hover,.w3-teal{color:#fff!important;background-color:#3d5c92!important}.w3-hover-yellow:hover,.w3-yellow{color:#000!important;background-color:#ffeb3b!important}.w3-hover-white:hover,.w3-white{color:#000!important;background-color:#fff!important}.w3-black,.w3-hover-black:hover{color:#fff!important;background-color:#000!important}.w3-gray,.w3-grey,.w3-hover-gray:hover,.w3-hover-grey:hover{color:#000!important;background-color:#9e9e9e!important}.w3-hover-light-gray:hover,.w3-hover-light-grey:hover,.w3-light-gray,.w3-light-grey{color:#000!important;background-color:#f1f1f1!important}.w3-dark-gray,.w3-dark-grey,.w3-hover-dark-gray:hover,.w3-hover-dark-grey:hover{color:#fff!important;background-color:#616161!important}.w3-hover-pale-red:hover,.w3-pale-red{color:#000!important;background-color:#fdd!important}.w3-hover-pale-green:hover,.w3-pale-green{color:#000!important;background-color:#dfd!important}.w3-hover-pale-yellow:hover,.w3-pale-yellow{color:#000!important;background-color:#ffc!important}.w3-hover-pale-blue:hover,.w3-pale-blue{color:#000!important;background-color:#dff!important}.w3-hover-text-amber:hover,.w3-text-amber{color:#ffc107!important}.w3-hover-text-aqua:hover,.w3-text-aqua{color:#0ff!important}.w3-hover-text-blue:hover,.w3-text-blue{color:#2196f3!important}.w3-hover-text-light-blue:hover,.w3-text-light-blue{color:#87ceeb!important}.w3-hover-text-brown:hover,.w3-text-brown{color:#795548!important}.w3-hover-text-cyan:hover,.w3-text-cyan{color:#00bcd4!important}.w3-hover-text-blue-gray:hover,.w3-hover-text-blue-grey:hover,.w3-text-blue-gray,.w3-text-blue-grey{color:#607d8b!important}.w3-hover-text-green:hover,.w3-text-green{color:#4caf50!important}.w3-hover-text-light-green:hover,.w3-text-light-green{color:#8bc34a!important}.w3-hover-text-indigo:hover,.w3-text-indigo{color:#3f51b5!important}.w3-hover-text-khaki:hover,.w3-text-khaki{color:#b4aa50!important}.w3-hover-text-lime:hover,.w3-text-lime{color:#cddc39!important}.w3-hover-text-orange:hover,.w3-text-orange{color:#ff9800!important}.w3-hover-text-deep-orange:hover,.w3-text-deep-orange{color:#ff5722!important}.w3-hover-text-pink:hover,.w3-text-pink{color:#e91e63!important}.w3-hover-text-purple:hover,.w3-text-purple{color:#9c27b0!important}.w3-hover-text-deep-purple:hover,.w3-text-deep-purple{color:#673ab7!important}.w3-hover-text-red:hover,.w3-text-red{color:#f44336!important}.w3-hover-text-sand:hover,.w3-text-sand{color:#fdf5e6!important}.w3-hover-text-teal:hover,.w3-text-teal{color:#009688!important}.w3-hover-text-yellow:hover,.w3-text-yellow{color:#d2be0e!important}.w3-hover-text-white:hover,.w3-text-white{color:#fff!important}.w3-hover-text-black:hover,.w3-text-black{color:#000!important}.w3-hover-text-gray:hover,.w3-hover-text-grey:hover,.w3-text-gray,.w3-text-grey{color:#757575!important}.w3-hover-text-light-gray:hover,.w3-hover-text-light-grey:hover,.w3-text-light-gray,.w3-text-light-grey{color:#f1f1f1!important}.w3-hover-text-dark-gray:hover,.w3-hover-text-dark-grey:hover,.w3-text-dark-gray,.w3-text-dark-grey{color:#3a3a3a!important}.w3-border-amber,.w3-hover-border-amber:hover{border-color:#ffc107!important}.w3-border-aqua,.w3-hover-border-aqua:hover{border-color:#0ff!important}.w3-border-blue,.w3-hover-border-blue:hover{border-color:#2196f3!important}.w3-border-light-blue,.w3-hover-border-light-blue:hover{border-color:#87ceeb!important}.w3-border-brown,.w3-hover-border-brown:hover{border-color:#795548!important}.w3-border-cyan,.w3-hover-border-cyan:hover{border-color:#00bcd4!important}.w3-border-blue-gray,.w3-border-blue-grey,.w3-hover-border-blue-gray:hover,.w3-hover-border-blue-grey:hover{border-color:#607d8b!important}.w3-border-green,.w3-hover-border-green:hover{border-color:#4caf50!important}.w3-border-light-green,.w3-hover-border-light-green:hover{border-color:#8bc34a!important}.w3-border-indigo,.w3-hover-border-indigo:hover{border-color:#3f51b5!important}.w3-border-khaki,.w3-hover-border-khaki:hover{border-color:khaki!important}.w3-border-lime,.w3-hover-border-lime:hover{border-color:#cddc39!important}.w3-border-orange,.w3-hover-border-orange:hover{border-color:#ff9800!important}.w3-border-deep-orange,.w3-hover-border-deep-orange:hover{border-color:#ff5722!important}.w3-border-pink,.w3-hover-border-pink:hover{border-color:#e91e63!important}.w3-border-purple,.w3-hover-border-purple:hover{border-color:#9c27b0!important}.w3-border-deep-purple,.w3-hover-border-deep-purple:hover{border-color:#673ab7!important}.w3-border-red,.w3-hover-border-red:hover{border-color:#f44336!important}.w3-border-sand,.w3-hover-border-sand:hover{border-color:#fdf5e6!important}.w3-border-teal,.w3-hover-border-teal:hover{border-color:#009688!important}.w3-border-yellow,.w3-hover-border-yellow:hover{border-color:#ffeb3b!important}.w3-border-white,.w3-hover-border-white:hover{border-color:#fff!important}.w3-border-black,.w3-hover-border-black:hover{border-color:#000!important}.w3-border-gray,.w3-border-grey,.w3-hover-border-gray:hover,.w3-hover-border-grey:hover{border-color:#9e9e9e!important}.w3-border-light-gray,.w3-border-light-grey,.w3-hover-border-light-gray:hover,.w3-hover-border-light-grey:hover{border-color:#f1f1f1!important}.w3-border-dark-gray,.w3-border-dark-grey,.w3-hover-border-dark-gray:hover,.w3-hover-border-dark-grey:hover{border-color:#616161!important}.w3-border-pale-red,.w3-hover-border-pale-red:hover{border-color:#ffe7e7!important}.w3-border-pale-green,.w3-hover-border-pale-green:hover{border-color:#e7ffe7!important}.w3-border-pale-yellow,.w3-hover-border-pale-yellow:hover{border-color:#ffc!important}.w3-border-pale-blue,.w3-hover-border-pale-blue:hover{border-color:#e7ffff!important}.box-shadow{box-shadow:1px 0 1px 1px #ccc;margin-bottom:12px}.dropdown-toggle{width:200%!important}.vitals .btn-default{background-color:#fff!important}.vitals .btn-default:hover{background-color:#fff!important}.submit-button{float:right}.submit-button .btn-default{background-color:#00bcd4!important;color:#fff!important;border-color:#ddd!important}ul.dropdown-menu{width:260px!important;overflow-y:scroll!important}.padding-bottom{padding-bottom:15px}.card-margin{margin:16px}.bg-white{background-color:#fff}.bg-lightgrey{background-color:#fff}.padding-top{padding-top:15px}.panel-height{height:600px;overflow-x:scroll}.annotation-modal{background-color:transparent!important;margin-left:1%!important;width:98%!important;height:98%!important;margin-top:0!important;padding-right:0!important}.modal-open .modal{overflow-x:hidden!important;overflow-y:hidden!important}.modal{background-color:#fff;margin-left:25%;width:50%;height:70%;margin-top:4%;padding-right:0!important}#viewTemplateModal .modal{background-color:#fff;margin-left:0;width:80%;height:70%;margin-top:4%;padding-right:0!important}.header1{font-size:40px}.header2{font-size:30px}.header3{font-size:25px}.header4{font-size:18px}.header5{font-size:14px}.header6{font-size:12px}.heading-text-align-left{text-align:left!important}.heading-text-align-center{text-align:center}.table-td{border:1px solid rgba(128,128,128,.6);padding:2px;width:10%;max-width:10%}table{width:100%}#container{position:relative}.left-card-margin{margin:4px 0 4px 0}.dragged-radio{margin-left:5px!important;margin-right:15px!important}.padding0{padding-left:0} </style></head>" + dto.getTemplateObjectHtml();
				dto.setTemplateObjectHtml(replacedString);		    
			}  //dilip
			
			TemplateMaster template = factory.map(dto, TemplateMaster.class, null);
			template.setCreatedDate( TimeZoneComponent.getDateByzone(dto.getUnitId()));
			template.setUpdatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
			update(template);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getTemplateTypes() throws ApplicationException {
		try{
			List<CommonDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_TEMPLATE_TYPES").setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getTemplatesForOPCoversheet(int doctorId, int specialtyId, int orgId, int unitId)throws ApplicationException {
		try{
			return null;
		}catch(Exception e){
			
			throw e;
		}
	}
			
	@Override
	public Response getTemplateByDoctorOrSpeciality(TemplateReqDto templateReqDto) throws ApplicationException {
		try{
			
			
			
			/*String templateQuery = "select "
					+ "	tmp.template_id as \"templateId\", "
					+ "	tmp.title as \"title\", "
					+ "	tmp.speciality_id as \"specialityId\", "
					+ "	tmp.sub_speciality_id as \"subSpecialityId\", "
					+ "	tmp.template_object as \"templateObject\", "
					+ "	tmp.status as \"status\", "
					+ "	tmp.doctor_id as \"doctorId\", "
					+ "	tmp.template_type_id as \"templateTypeId\" "
					+ "from "
					+ "	public.t_template_master tmp "
					+ " where "
					+ "	tmp.status = 'A' "
					+ "	and tmp.doctor_id is null "
					+ "	and tmp.speciality_id is null "
					+ "	and tmp.sub_speciality_id is null "
					+ "	and tmp.org_id = :orgId "
					+ "	and tmp.unit_id = :unitId "
					+ "	and tmp.template_type_id = :templateTypeId "
					+ " union "
					+ " select "
					+ "	tmp.template_id as \"templateId\", "
					+ "	tmp.title as \"title\", "
					+ "	tmp.speciality_id as \"specialityId\", "
					+ "	tmp.sub_speciality_id as \"subSpecialityId\", "
					+ "	tmp.template_object as \"templateObject\", "
					+ "	tmp.status as \"status\", "
					+ "	tmp.doctor_id as \"doctorId\", "
					+ "	tmp.template_type_id as \"templateTypeId\" "
					+ " from "
					+ "	public.t_template_master tmp "
					+ " inner join t_unit_sub_speciality_mapper subM on "
					+ " tmp.sub_speciality_id=subM.sub_speciality_id	"
					+ "  and subM.organization_id =:orgId "
					+ "  and subM.unit_id = :unitId "
					+ " where "
					+ "	tmp.status = 'A' "
					+ "	and tmp.org_id = :orgId "
					+ "	and tmp.unit_id = :unitId "
					+ "	and tmp.template_type_id = :templateTypeId "
					+ "	and tmp.speciality_id = "+templateReqDto.getSpecialityId()
					+ "	and tmp.doctor_id is null "
					+ " union "
					+ " select "
					+ "	tmp.template_id as \"templateId\", "
					+ "	tmp.title as \"title\", "
					+ "	tmp.speciality_id as \"specialityId\", "
					+ "	tmp.sub_speciality_id as \"subSpecialityId\", "
					+ "	tmp.template_object as \"templateObject\", "
					+ "	tmp.status as \"status\", "
					+ "	tmp.doctor_id as \"doctorId\", "
					+ "	tmp.template_type_id as \"templateTypeId\" "
					+ " from "
					+ "	public.t_template_master tmp "
					+ " where "
					+ "	tmp.status = 'A' "
					+ "	and tmp.org_id = :orgId "
					+ "	and tmp.unit_id = :unitId "
					+ "	and tmp.template_type_id = :templateTypeId "
					+ "	and tmp.doctor_id = "+templateReqDto.getDoctorId()
					+ "	and tmp.speciality_id is null "
					+ "	and tmp.sub_speciality_id is null "
					+ " union "
					+ " select "
					+ "	tmp.template_id as \"templateId\", "
					+ "	tmp.title as \"title\", "
					+ "	tmp.speciality_id as \"specialityId\", "
					+ "	tmp.sub_speciality_id as \"subSpecialityId\", "
					+ "	tmp.template_object as \"templateObject\", "
					+ "	tmp.status as \"status\", "
					+ "	tmp.doctor_id as \"doctorId\", "
					+ "	tmp.template_type_id as \"templateTypeId\" "
					+ " from "
					+ "	public.t_template_master tmp "
					+ " where "
					+ "	tmp.status = 'A' "
					+ "	and tmp.org_id = :orgId "
					+ "	and tmp.unit_id = :unitId "
					+ "	and tmp.template_type_id = :templateTypeId "
					+ "	and tmp.speciality_id = "+templateReqDto.getSpecialityId()	
					+ "	and tmp.doctor_id is null "
					+ "	and tmp.sub_speciality_id is null";*/
			
			
			String templateQuery = "select "
			+ "	tmp.template_id as \"templateId\", "
			+ "	tmp.title as \"title\", "
			+ "	tmp.speciality_id as \"specialityId\", "
			+ "	tmp.sub_speciality_id as \"subSpecialityId\", "
			+ "	tmp.template_object as \"templateObject\", "
			+ "	tmp.status as \"status\", "
			+ "	tmp.doctor_id as \"doctorId\", "
			+ "	tmp.template_type_id as \"templateTypeId\", "
			+ " tmp.template_format_id as \"templateFormatId\" , "
			+ " tmp.form_template_id as \"formTemplateId\" "
			+ "from "
			+ "	public.t_template_master tmp "
			+ "where "
			+ "	tmp.status = 'A' "
			+ "	and tmp.doctor_id is null "
			+ "	and tmp.speciality_id is null "
			+ "	and tmp.sub_speciality_id is null "
			+ "	and tmp.org_id = :orgId "
			+ "	and tmp.unit_id = :unitId "
			+ "	and tmp.template_type_id = :templateTypeId "
			+ " union "
			+ " select "
			+ "	tmp.template_id as \"templateId\", "
			+ "	tmp.title as \"title\", "
			+ "	tmp.speciality_id as \"specialityId\", "
			+ "	tmp.sub_speciality_id as \"subSpecialityId\", "
			+ "	tmp.template_object as \"templateObject\", "
			+ "	tmp.status as \"status\", "
			+ "	tmp.doctor_id as \"doctorId\", "
			+ "	tmp.template_type_id as \"templateTypeId\", "
			+ " tmp.template_format_id as \"templateFormatId\" , "
			+ " tmp.form_template_id as \"formTemplateId\" "
			+ "from "
			+ "	public.t_template_master tmp "
			+ "inner join t_unit_sub_speciality_mapper subM on "
			+ "	tmp.sub_speciality_id = subM.sub_speciality_id "
			+ "	and subM.organization_id =:orgId "
			+ "	and subM.unit_id = :unitId "
			+ "where "
			+ "	tmp.status = 'A' "
			+ "	and tmp.org_id = :orgId "
			+ "	and tmp.unit_id = :unitId "
			+ "	and tmp.template_type_id = :templateTypeId "
			+ "	and tmp.speciality_id = "+templateReqDto.getSpecialityId()	
			+ "	and tmp.doctor_id is null "
			+ " union "
			+ " select "
			+ "	tmp.template_id as \"templateId\", "
			+ "	tmp.title as \"title\", "
			+ "	tmp.speciality_id as \"specialityId\", "
			+ "	tmp.sub_speciality_id as \"subSpecialityId\", "
			+ "	tmp.template_object as \"templateObject\", "
			+ "	tmp.status as \"status\", "
			+ "	tmp.doctor_id as \"doctorId\", "
			+ "	tmp.template_type_id as \"templateTypeId\", "
			+ " tmp.template_format_id as \"templateFormatId\" ,"
			+ " tmp.form_template_id as \"formTemplateId\" "
			+ "from "
			+ "	public.t_template_master tmp "
			+ "where "
			+ "	tmp.status = 'A' "
			+ "	and tmp.org_id = :orgId "
			+ "	and tmp.unit_id = :unitId "
			+ "	and tmp.template_type_id = :templateTypeId "
			+ "	and tmp.doctor_id = "+templateReqDto.getDoctorId()
			+ "	and tmp.speciality_id is null "
			+ "	and tmp.sub_speciality_id is null "
			+ " union "
			+ " select "
			+ "	tmp.template_id as \"templateId\", "
			+ "	tmp.title as \"title\", "
			+ "	tmp.speciality_id as \"specialityId\", "
			+ "	tmp.sub_speciality_id as \"subSpecialityId\", "
			+ "	tmp.template_object as \"templateObject\", "
			+ "	tmp.status as \"status\", "
			+ "	tmp.doctor_id as \"doctorId\", "
			+ "	tmp.template_type_id as \"templateTypeId\", "
			+ " tmp.template_format_id as \"templateFormatId\", "
			+ " tmp.form_template_id as \"formTemplateId\" "
			+ "from "
			+ "	public.t_template_master tmp "
			+ "where "
			+ "	tmp.status = 'A' "
			+ "	and tmp.org_id = :orgId "
			+ "	and tmp.unit_id = :unitId "
			+ "	and tmp.template_type_id = :templateTypeId "
			+ "	and tmp.speciality_id = "+templateReqDto.getSpecialityId()	
			+ "	and tmp.doctor_id is null "
			+ " union "
			+ " select "
			+ "	tmp.template_id as \"templateId\", "
			+ "	tmp.title as \"title\", "
			+ "	tmp.speciality_id as \"specialityId\", "
			+ "	tmp.sub_speciality_id as \"subSpecialityId\", "
			+ "	tmp.template_object as \"templateObject\", "
			+ "	tmp.status as \"status\", "
			+ "	tmp.doctor_id as \"doctorId\", "
			+ "	tmp.template_type_id as \"templateTypeId\", "
			+ " tmp.template_format_id as \"templateFormatId\", "
			+ " tmp.form_template_id as \"formTemplateId\" "
			+ "from "
			+ "	public.t_template_master tmp "
			+ "where "
			+ "	tmp.status = 'A' "
			+ "	and tmp.org_id = :orgId "
			+ "	and tmp.unit_id = :unitId "
			+ "	and tmp.template_type_id = :templateTypeId "
			+ "	and tmp.speciality_id = "+templateReqDto.getSpecialityId()	
			+ "	and tmp.doctor_id is null "
			+ "	and tmp.sub_speciality_id is null "
			+ " order by title asc";
			
			List<TemplateMasterDto> listTemplateMasterDto = sessionFactory.getCurrentSession().createSQLQuery(templateQuery)
					.setParameter("orgId", templateReqDto.getOrgId())
					.setParameter("unitId", templateReqDto.getUnitId())
					.setParameter("templateTypeId", templateReqDto.getTemplateTypeId())
					.setResultTransformer(Transformers.aliasToBean(TemplateMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTemplateMasterDto, null);
			
			
		}catch(Exception e){
		logger.error("getTemplateByDoctorOrSpeciality failed", e);
		}
		return null;
	}

	
	@Override
	public Response getTemplateQuestionaaireRadiology(TemplateReqDto templateReqDto) throws ApplicationException {
		try{
			String templateQuery = "select "
			+ "	tmp.template_id as \"templateId\", "
			+ "	tmp.title as \"title\", "
			+ "	tmp.speciality_id as \"specialityId\", "
			+ "	tmp.sub_speciality_id as \"subSpecialityId\", "
			+ "	tmp.template_object as \"templateObject\", "
			+ "	tmp.status as \"status\", "
			+ "	tmp.doctor_id as \"doctorId\", "
			+ "	tmp.template_type_id as \"templateTypeId\", "
			+ " tmp.template_format_id as \"templateFormatId\" ,"
			+ " tmp.form_template_id as \"formTemplateId\" "
			+ "from "
			+ "	public.t_template_master tmp "
			+ "where "
			+ "	tmp.status = 'A' "
			+ "	and tmp.doctor_id is null "
			+ "	and tmp.speciality_id is null "
			+ "	and tmp.sub_speciality_id is null "
			+ "	and tmp.org_id = :orgId "
			+ "	and tmp.unit_id = :unitId "
			+ "	and tmp.template_type_id = :templateTypeId "
			+ " union "
			+ " select "
			+ "	tmp.template_id as \"templateId\", "
			+ "	tmp.title as \"title\", "
			+ "	tmp.speciality_id as \"specialityId\", "
			+ "	tmp.sub_speciality_id as \"subSpecialityId\", "
			+ "	tmp.template_object as \"templateObject\", "
			+ "	tmp.status as \"status\", "
			+ "	tmp.doctor_id as \"doctorId\", "
			+ "	tmp.template_type_id as \"templateTypeId\", "
			+ " tmp.template_format_id as \"templateFormatId\" ,"
			+ " tmp.form_template_id as \"formTemplateId\" "
			+ "from "
			+ "	public.t_template_master tmp "
			+ "inner join t_unit_sub_speciality_mapper subM on "
			+ "	tmp.sub_speciality_id = subM.sub_speciality_id "
			+ "	and subM.organization_id =:orgId "
			+ "	and subM.unit_id = :unitId "
			+ "where "
			+ "	tmp.status = 'A' "
			+ "	and tmp.org_id = :orgId "
			+ "	and tmp.unit_id = :unitId "
			+ "	and tmp.template_type_id = :templateTypeId "
			+ "	and tmp.speciality_id = "+templateReqDto.getSpecialityId()	
			+ "	and tmp.sub_speciality_id = "+templateReqDto.getSubSpecialityId() 
			+ "	and tmp.doctor_id is null "
			+ " union "
			+ " select "
			+ "	tmp.template_id as \"templateId\", "
			+ "	tmp.title as \"title\", "
			+ "	tmp.speciality_id as \"specialityId\", "
			+ "	tmp.sub_speciality_id as \"subSpecialityId\", "
			+ "	tmp.template_object as \"templateObject\", "
			+ "	tmp.status as \"status\", "
			+ "	tmp.doctor_id as \"doctorId\", "
			+ "	tmp.template_type_id as \"templateTypeId\", "
			+ " tmp.template_format_id as \"templateFormatId\" ,"
			+ " tmp.form_template_id as \"formTemplateId\" "
			+ "from "
			+ "	public.t_template_master tmp "
			+ "where "
			+ "	tmp.status = 'A' "
			+ "	and tmp.org_id = :orgId "
			+ "	and tmp.unit_id = :unitId "
			+ "	and tmp.template_type_id = :templateTypeId "
			+ "	and tmp.doctor_id = "+templateReqDto.getDoctorId()
			+ "	and tmp.speciality_id is null "
			+ "	and tmp.sub_speciality_id is null "
			+ " union "
			+ " select "
			+ "	tmp.template_id as \"templateId\", "
			+ "	tmp.title as \"title\", "
			+ "	tmp.speciality_id as \"specialityId\", "
			+ "	tmp.sub_speciality_id as \"subSpecialityId\", "
			+ "	tmp.template_object as \"templateObject\", "
			+ "	tmp.status as \"status\", "
			+ "	tmp.doctor_id as \"doctorId\", "
			+ "	tmp.template_type_id as \"templateTypeId\", "
			+ " tmp.template_format_id as \"templateFormatId\" ,"
			+ " tmp.form_template_id as \"formTemplateId\" "
			+ "from "
			+ "	public.t_template_master tmp "
			+ "where "
			+ "	tmp.status = 'A' "
			+ "	and tmp.org_id = :orgId "
			+ "	and tmp.unit_id = :unitId "
			+ "	and tmp.template_type_id = :templateTypeId "
			+ "	and tmp.speciality_id = "+templateReqDto.getSpecialityId()	
			+ "	and tmp.sub_speciality_id = "+templateReqDto.getSubSpecialityId() 
			+ "	and tmp.doctor_id is null "
			+ " union "
			+ " select "
			+ "	tmp.template_id as \"templateId\", "
			+ "	tmp.title as \"title\", "
			+ "	tmp.speciality_id as \"specialityId\", "
			+ "	tmp.sub_speciality_id as \"subSpecialityId\", "
			+ "	tmp.template_object as \"templateObject\", "
			+ "	tmp.status as \"status\", "
			+ "	tmp.doctor_id as \"doctorId\", "
			+ "	tmp.template_type_id as \"templateTypeId\", "
			+ " tmp.template_format_id as \"templateFormatId\" ,"
			+ " tmp.form_template_id as \"formTemplateId\" "
			+ "from "
			+ "	public.t_template_master tmp "
			+ "where "
			+ "	tmp.status = 'A' "
			+ "	and tmp.org_id = :orgId "
			+ "	and tmp.unit_id = :unitId "
			+ "	and tmp.template_type_id = :templateTypeId "
			+ "	and tmp.speciality_id = "+templateReqDto.getSpecialityId()	
			+ "	and tmp.sub_speciality_id = "+templateReqDto.getSubSpecialityId() 
			+ "	and tmp.doctor_id is null ";
		//	+ "	and tmp.sub_speciality_id is null";
			
			List<TemplateMasterDto> listTemplateMasterDto = sessionFactory.getCurrentSession().createSQLQuery(templateQuery)
					.setParameter("orgId", templateReqDto.getOrgId())
					.setParameter("unitId", templateReqDto.getUnitId())
					.setParameter("templateTypeId", templateReqDto.getTemplateTypeId())
					.setResultTransformer(Transformers.aliasToBean(TemplateMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTemplateMasterDto, null);
	}catch(Exception e){
		
		throw e;
	}
	}

	@Override
	public Response getTemplatesForComplaints(TemplateReqDto templateReqDto) throws ApplicationException {
		try{
			
			String append = "";
			/*
			 * if(templateReqDto.getVisitTypeId()!=0 && templateReqDto.getVisitTypeId()==1)
			 * { append =
			 * "      and template_tra.encounter_id = "+templateReqDto.getEncounterId(); }
			 * else if(templateReqDto.getVisitTypeId()!=0 &&
			 * templateReqDto.getVisitTypeId()==2) { append =
			 * "      and template_tra.admission_id = "+templateReqDto.getAdmissionId(); }
			 * 
			 * String query1 = sessionFactory.getCurrentSession().createSQLQuery("select " +
			 * "  distinct(template_mst.template_id) as \"templateId\", " +
			 * "  template_mst.title as \"title\", " +
			 * "  template_mst.speciality_id as \"specialityId\", " +
			 * "  template_mst.sub_speciality_id as \"subSpecialityId\", " +
			 * "  template_mst.template_object as \"templateObject\", " +
			 * "  template_mst.status as \"status\", " +
			 * "  template_mst.doctor_id as \"doctorId\", " +
			 * "  template_mst.template_type_id as \"templateTypeId\" " + "from " +
			 * "  public.t_template_master template_mst " +
			 * "  inner join public.t_icpc_template_mapping mppr on template_mst.template_id = mppr.template_id "
			 * + "where " + "  template_mst.template_type_id = :templateTypeId " +
			 * "  and template_mst.org_id = :orgId " +
			 * "  and template_mst.unit_id = :unitId " + "  and template_mst.status = 'A' "
			 * + "  and mppr.status = 'A' " + "  and mppr.is_deleted = 'N' " +
			 * "  or (template_mst.doctor_id is null and template_mst.speciality_id is null and  template_mst.sub_speciality_id is null and template_mst.template_type_id = :templateTypeId"
			 * + "  and template_mst.template_id not in ( " + "    select " +
			 * "      coalesce(template_tra.template_id, 0) " + "    from " +
			 * "      public.t_template_transaction template_tra " + "    where " +
			 * "      template_tra.org_id = :orgId " +
			 * "      and template_tra.unit_id = :unitId " +
			 * "      and template_tra.m_icpc_system_id = "+templateReqDto.getmIcpcSystemId(
			 * ) + "      and template_tra.m_icpc_id = "+templateReqDto.getmIcpcId()+append
			 * + "  ) " + ") " + "  and template_mst.template_id not in ( " + "    select "
			 * + "      coalesce(template_tra.template_id, 0) " + "    from " +
			 * "      public.t_template_transaction template_tra " + "    where " +
			 * "      template_tra.org_id = :orgId " +
			 * "      and template_tra.unit_id = :unitId " +
			 * "      and template_tra.m_icpc_system_id = "+templateReqDto.getmIcpcSystemId(
			 * ) + "      and template_tra.m_icpc_id = "+templateReqDto.getmIcpcId()+append
			 * + "  ) " +
			 * "  and mppr.m_icpc_id ="+templateReqDto.getmIcpcId()).getQueryString();
			 */
			
			
			String query = "select "
			+ "	distinct(template_mst.template_id) as \"templateId\", "
			+ "	template_mst.title as \"title\", "
			+ "	template_mst.speciality_id as \"specialityId\", "
			+ "	template_mst.sub_speciality_id as \"subSpecialityId\", "
			+ "	template_mst.template_object as \"templateObject\", "
			+ "	template_mst.status as \"status\", "
			+ "	template_mst.doctor_id as \"doctorId\", "
			+ "	template_mst.template_type_id as \"templateTypeId\" ,"
			+ "	template_mst.template_format_id as \"templateFormatId\" ,"
			+ " template_mst.form_template_id as \"formTemplateId\" "
			+ "from "
			+ "	public.t_template_master template_mst "
			+ "inner join public.t_icpc_template_mapping mppr on "
			+ "	template_mst.template_id = mppr.template_id "
			+ "where "
			+ "	template_mst.template_type_id = :templateTypeId "
			+ "	and template_mst.org_id = :orgId "
			+ "	and template_mst.unit_id = :unitId "
			+ "	and template_mst.status = 'A' "
			+ "	and mppr.status = 'A' "
			+ "	and mppr.is_deleted = 'N' "
			+ "	and template_mst.doctor_id is null "
			+ "	and template_mst.speciality_id is null "
			+ "	and template_mst.sub_speciality_id is null "
			+ "	and mppr.m_icpc_id = "+templateReqDto.getmIcpcId()
			+ " union select "
			+ "	distinct(template_mst.template_id) as \"templateId\", "
			+ "	template_mst.title as \"title\", "
			+ "	template_mst.speciality_id as \"specialityId\", "
			+ "	template_mst.sub_speciality_id as \"subSpecialityId\", "
			+ "	template_mst.template_object as \"templateObject\", "
			+ "	template_mst.status as \"status\", "
			+ "	template_mst.doctor_id as \"doctorId\", "
			+ "	template_mst.template_type_id as \"templateTypeId\" ,"
			+ "	template_mst.template_format_id as \"templateFormatId\" ,"
			+ " template_mst.form_template_id as \"formTemplateId\" "
			+ "from "
			+ "	public.t_template_master template_mst "
			+ "inner join public.t_icpc_template_mapping mppr on "
			+ "	template_mst.template_id = mppr.template_id "
			+ "where "
			+ "	template_mst.template_type_id = :templateTypeId "
			+ "	and template_mst.org_id = :orgId "
			+ "	and template_mst.unit_id = :unitId "
			+ "	and template_mst.status = 'A' "
			+ "	and mppr.status = 'A' "
			+ "	and mppr.is_deleted = 'N' "
			+ "	and template_mst.doctor_id is null "
			+ "	and template_mst.speciality_id  =  "+templateReqDto.getSpecialityId()
			+ "	and template_mst.sub_speciality_id  ="+templateReqDto.getSubSpecialityId()
			+ "	and mppr.m_icpc_id ="+templateReqDto.getmIcpcId()
			+ " union "
			+ "select "
			+ "	distinct(template_mst.template_id) as \"templateId\", "
			+ "	template_mst.title as \"title\", "
			+ "	template_mst.speciality_id as \"specialityId\", "
			+ "	template_mst.sub_speciality_id as \"subSpecialityId\", "
			+ "	template_mst.template_object as \"templateObject\", "
			+ "	template_mst.status as \"status\", "
			+ "	template_mst.doctor_id as \"doctorId\", "
			+ "	template_mst.template_type_id as \"templateTypeId\" ,"
			+ "	template_mst.template_format_id as \"templateFormatId\" ,"
			+ " template_mst.form_template_id as \"formTemplateId\" "
			+ "from "
			+ "	public.t_template_master template_mst "
			+ "inner join public.t_icpc_template_mapping mppr on "
			+ "	template_mst.template_id = mppr.template_id "
			+ " where "
			+ "	template_mst.template_type_id = :templateTypeId "
			+ "	and template_mst.org_id = :orgId "
			+ "	and template_mst.unit_id = :unitId "
			+ "	and template_mst.status = 'A' "
			+ "	and mppr.status = 'A' "
			+ "	and mppr.is_deleted = 'N' "
			+ "	and template_mst.doctor_id = "+templateReqDto.getDoctorId()
			+ "	and mppr.m_icpc_id = "+templateReqDto.getmIcpcId();
			
			
			List<TemplateMasterDto> listTemplateMasterDto = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("orgId", templateReqDto.getOrgId())
					.setParameter("unitId", templateReqDto.getUnitId())
					.setParameter("templateTypeId", templateReqDto.getTemplateTypeId())
					.setResultTransformer(Transformers.aliasToBean(TemplateMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTemplateMasterDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@Override
	public Response getTemplatesForDiagonosis(TemplateReqDto templateReqDto) throws ApplicationException {
		try{
			
			/*
			 * String append = ""; if(templateReqDto.getVisitTypeId()!=0 &&
			 * templateReqDto.getVisitTypeId()==1) { append =
			 * "      and template_tra.encounter_id = "+templateReqDto.getEncounterId(); }
			 * else if(templateReqDto.getVisitTypeId()!=0 &&
			 * templateReqDto.getVisitTypeId()==2) { append =
			 * "      and template_tra.admission_id = "+templateReqDto.getAdmissionId(); }
			 * 
			 * String query1 = sessionFactory.getCurrentSession().createSQLQuery("select " +
			 * "  distinct(template_mst.template_id) as \"templateId\", " +
			 * "  template_mst.title as \"title\", " +
			 * "  template_mst.speciality_id as \"specialityId\", " +
			 * "  template_mst.sub_speciality_id as \"subSpecialityId\", " +
			 * "  template_mst.template_object as \"templateObject\", " +
			 * "  template_mst.status as \"status\", " +
			 * "  template_mst.doctor_id as \"doctorId\", " +
			 * "  template_mst.template_type_id as \"templateTypeId\" " + "from " +
			 * "  public.t_template_master template_mst " +
			 * "  inner join public.t_template_diagnosis_mapper mppr on template_mst.template_id = mppr.template_id "
			 * + "where " + "  template_mst.template_type_id = :templateTypeId " +
			 * "  and template_mst.org_id = :orgId " +
			 * "  and template_mst.unit_id = :unitId " + "  and template_mst.status = 'A' "
			 * + "  and mppr.status = 'A' " + "  and mppr.is_deleted = 'N' " +
			 * "  or (template_mst.doctor_id is null and template_mst.speciality_id is null and  template_mst.sub_speciality_id is null and template_mst.template_type_id = :templateTypeId"
			 * + "  and template_mst.template_id not in ( " + "    select " +
			 * "      coalesce(template_tra.template_id, 0) " + "    from " +
			 * "      public.t_template_transaction template_tra " + "    where " +
			 * "      template_tra.org_id = :orgId " +
			 * "      and template_tra.unit_id = :unitId " +
			 * "      and template_tra.diagnosis_id = "+templateReqDto.getDiagnosisId()+
			 * append + "  ) " + ") " + "  and template_mst.template_id not in ( " +
			 * "    select " + "      coalesce(template_tra.template_id, 0) " + "    from "
			 * + "      public.t_template_transaction template_tra " + "    where " +
			 * "      template_tra.org_id = :orgId " +
			 * "      and template_tra.unit_id = :unitId " +
			 * "      and template_tra.diagnosis_id = "+templateReqDto.getDiagnosisId()+
			 * append + "  ) " +
			 * "  and mppr.diagnosis_id ="+templateReqDto.getDiagnosisId()).getQueryString()
			 * ;
			 */
			String query = "select "
			+ "	distinct(template_mst.template_id) as \"templateId\", "
			+ "	template_mst.title as \"title\", "
			+ "	template_mst.speciality_id as \"specialityId\", "
			+ "	template_mst.sub_speciality_id as \"subSpecialityId\", "
			+ "	template_mst.template_object as \"templateObject\", "
			+ "	template_mst.status as \"status\", "
			+ "	template_mst.doctor_id as \"doctorId\", "
			+ "	template_mst.template_type_id as \"templateTypeId\" ,"
			+ " template_mst.template_format_id as \"templateFormatId\" , "
			+ " template_mst.form_template_id as \"formTemplateId\" "
			+ "from "
			+ "	public.t_template_master template_mst "
			+ "inner join public.t_template_diagnosis_mapper mppr on "
			+ "	template_mst.template_id = mppr.template_id "
			+ "where "
			+ "	template_mst.template_type_id = :templateTypeId "
			+ "	and template_mst.org_id = :orgId "
			+ "	and template_mst.unit_id = :unitId "
			+ "	and template_mst.status = 'A' "
			+ "	and mppr.status = 'A' "
			+ "	and mppr.is_deleted = 'N' "
			+ "	and template_mst.doctor_id is null "
			+ "	and template_mst.speciality_id is null "
			+ "	and template_mst.sub_speciality_id is null "
			+ "	and mppr.diagnosis_id =  "+templateReqDto.getDiagnosisId()
			+ " union select "
			+ "	distinct(template_mst.template_id) as \"templateId\", "
			+ "	template_mst.title as \"title\", "
			+ "	template_mst.speciality_id as \"specialityId\", "
			+ "	template_mst.sub_speciality_id as \"subSpecialityId\", "
			+ "	template_mst.template_object as \"templateObject\", "
			+ "	template_mst.status as \"status\", "
			+ "	template_mst.doctor_id as \"doctorId\", "
			+ "	template_mst.template_type_id as \"templateTypeId\" ,"
			+ " template_mst.template_format_id as \"templateFormatId\" , "
			+ " template_mst.form_template_id as \"formTemplateId\" "
			+ "from "
			+ "	public.t_template_master template_mst "
			+ "inner join public.t_template_diagnosis_mapper mppr on "
			+ "	template_mst.template_id = mppr.template_id "
			+ "where "
			+ "	template_mst.template_type_id = :templateTypeId "
			+ "	and template_mst.org_id = :orgId "
			+ "	and template_mst.unit_id = :unitId "
			+ "	and template_mst.status = 'A' "
			+ "	and mppr.status = 'A' "
			+ "	and mppr.is_deleted = 'N' "
			+ "	and template_mst.doctor_id is null "
			+ "	and template_mst.speciality_id  = "+templateReqDto.getSpecialityId()
			+ "	and template_mst.sub_speciality_id  = "+templateReqDto.getSubSpecialityId()
			+ "	and mppr.diagnosis_id ="+templateReqDto.getDiagnosisId()
			+ " union "
			+ "select "
			+ "	distinct(template_mst.template_id) as \"templateId\", "
			+ "	template_mst.title as \"title\", "
			+ "	template_mst.speciality_id as \"specialityId\", "
			+ "	template_mst.sub_speciality_id as \"subSpecialityId\", "
			+ "	template_mst.template_object as \"templateObject\", "
			+ "	template_mst.status as \"status\", "
			+ "	template_mst.doctor_id as \"doctorId\", "
			+ "	template_mst.template_type_id as \"templateTypeId\" ,"
			+ " template_mst.template_format_id as \"templateFormatId\" , "
			+ " template_mst.form_template_id as \"formTemplateId\" "
			+ "from "
			+ "	public.t_template_master template_mst "
			+ "inner join public.t_template_diagnosis_mapper mppr on "
			+ "	template_mst.template_id = mppr.template_id "
			+ "where "
			+ "	template_mst.template_type_id = :templateTypeId "
			+ "	and template_mst.org_id = :orgId "
			+ "	and template_mst.unit_id = :unitId "
			+ "	and template_mst.status = 'A' "
			+ "	and mppr.status = 'A' "
			+ "	and mppr.is_deleted = 'N' "
			+ "	and template_mst.doctor_id = "+templateReqDto.getDoctorId()
			+ "	and mppr.diagnosis_id = "+templateReqDto.getDiagnosisId();
						
			List<TemplateMasterDto> listTemplateMasterDto = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("orgId", templateReqDto.getOrgId())
					.setParameter("unitId", templateReqDto.getUnitId())
					.setParameter("templateTypeId", templateReqDto.getTemplateTypeId())
					.setResultTransformer(Transformers.aliasToBean(TemplateMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTemplateMasterDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getTempalteByService(TemplateReqDto templateReqDto) throws ApplicationException {
		try{
			StringBuilder q1 = new StringBuilder(" select	distinct(template_mst.template_id) as \"templateId\", "
					+ "	template_mst.title as \"title\", "
					+ "	template_mst.speciality_id as \"specialityId\", "
					+ "	template_mst.sub_speciality_id as \"subSpecialityId\", "
					+ "	template_mst.template_object as \"templateObject\", "
					+ "	template_mst.is_html_print as \"isHtmlPrint\", "
					+ "	template_mst.status as \"status\", "
					+ "	template_mst.doctor_id as \"doctorId\", "
					+ "	template_mst.template_type_id as \"templateTypeId\" ,"
					+ "	template_mst.template_format_id as \"templateFormatId\" ,"
					+ " template_mst.form_template_id as \"formTemplateId\" "
					+ "from "
					+ "	public.t_template_master template_mst "
					+ "inner join lab.t_template_service_mapper mppr on "
					+ "	template_mst.template_id = mppr.template_id "
					+ " where "
					+ "	 template_mst.org_id = :orgId "
					+ "	and template_mst.unit_id = :unitId "
					+ "	and mppr.service_id = :serviceId "
					+ "	and mppr.is_deleted = 'N' "
					+ "	and mppr.status = 'A' "
					+ "	and mppr.status = 'A' "
					+ "	and template_mst.status = 'A' ");
			
				q1.append(templateReqDto.getIsTemplateTypeReq()!=null && templateReqDto.getIsTemplateTypeReq()=='Y' ? " and template_mst.template_type_id="+templateReqDto.getTemplateTypeId() : " ");
				q1.append(" order by template_mst.template_id desc ");
			
			List<TemplateMasterDto> list = sessionFactory.getCurrentSession().createSQLQuery(q1.toString())
					.setParameter("orgId", templateReqDto.getOrgId())
					.setParameter("unitId", templateReqDto.getUnitId())
					.setParameter("serviceId", templateReqDto.getServiceId())
					.setResultTransformer(Transformers.aliasToBean(TemplateMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getTemplatesByType(TemplateReqDto templateReqDto) throws ApplicationException 
	{
		try{
			String q1 = sessionFactory.getCurrentSession().createSQLQuery("select "
					+ "  distinct(template_mst.template_id) as \"templateId\", "
					+ "  template_mst.title as \"title\", "
					+ "  template_mst.speciality_id as \"specialityId\", "
					+ "  template_mst.sub_speciality_id as \"subSpecialityId\", "
					+ "  template_mst.template_object as \"templateObject\", "
					+ "  template_mst.status as \"status\", "
					+ "  template_mst.doctor_id as \"doctorId\", "
					+ "  template_mst.template_type_id as \"templateTypeId\" ,"
					+ "  template_mst.form_template_id as \"formTemplateId\" "
					+ " from "
					+ "  public.t_template_master template_mst "
					+ " where "
					+ "  template_mst.template_type_id = :templateTypeId "
					+ "  and template_mst.org_id = :orgId "
					+ "  and template_mst.unit_id = :unitId "
					+ "  and template_mst.status = 'A' ").getQueryString();
			List<TemplateMasterDto> list = sessionFactory.getCurrentSession().createSQLQuery(q1.toString())
					.setParameter("orgId", templateReqDto.getOrgId())
					.setParameter("unitId", templateReqDto.getUnitId())
					.setParameter("templateTypeId", templateReqDto.getTemplateTypeId())
					.setResultTransformer(Transformers.aliasToBean(TemplateMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	

	@Override
	public Response getTemplatesByTypeAndSpeciality(TemplateReqDto dto) throws ApplicationException {
		try{
			String q1 = sessionFactory.getCurrentSession().createSQLQuery("select "
					+ "  distinct(template_mst.template_id) as \"templateId\", "
					+ "  template_mst.title as \"title\", "
					+ "  template_mst.speciality_id as \"specialityId\", "
					+ "  template_mst.sub_speciality_id as \"subSpecialityId\", "
					+ "  template_mst.template_object as \"templateObject\", "
					+ "  template_mst.status as \"status\", "
					+ "  template_mst.doctor_id as \"doctorId\", "
					+ "  template_mst.template_type_id as \"templateTypeId\" ,"
					+ "  template_mst.form_template_id as \"formTemplateId\" "
					+ " from "
					+ "  public.t_template_master template_mst "
					+ " where "
					+ "  template_mst.template_type_id = :templateTypeId "
					+ "  and template_mst.org_id = :orgId "
					+ "  and template_mst.unit_id = :unitId "
					+ "  and template_mst.status = 'A'  "
					+ "  and template_mst.speciality_id = :specialityId "
					+ "  and template_mst.form_template_id is not null ").getQueryString();
			List<TemplateMasterDto> list = sessionFactory.getCurrentSession().createSQLQuery(q1.toString())
					.setParameter("orgId", dto.getOrgId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("templateTypeId", dto.getTemplateTypeId())
					.setParameter("specialityId", dto.getSpecialityId())
					.setResultTransformer(Transformers.aliasToBean(TemplateMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}

	}


	@Override
	public Response isRenderedingSpecilaity(TemplateReqDto templateReqDto) throws ApplicationException {
		
		try{
			String query  ="SELECT tmp.template_id as \"templateId\", tmp.title as \"title\", tmp.speciality_id as \"specialityId\", tmp.sub_speciality_id as \"subSpecialityId\",  tmp.template_format_id as \"templateFormatId\" ,"
					 + " tmp.template_object as \"templateObject\", tmp.status as \"status\", tmp.created_by as \"createdBy\", to_char(tmp.created_date,'DD-M-YYYY HH24:MI:SS') as \"createdDate\", "
					 + " tmp.org_id as \"orgId\", tmp.unit_id as \"unitId\", tmp.doctor_id as \"doctorId\", spc.speciality_name as \"speciality\", subspc.sub_speciality_name as \"subSpeciality\", "
					 + " coalesce(concat(doc.first_name,' ',doc.middle_name,' ',doc.last_name),null) as \"doctor\" , tmp.form_template_id as \"formTemplateId\" "
					 + " FROM	public.t_template_master tmp  "
					 + " left join doctor.m_speciality_master spc "
					 + " on spc.speciality_id = tmp.speciality_id "
					 + " left join doctor.m_sub_speciality_master subspc "
					 + " on subspc.sub_speciality_id = tmp.sub_speciality_id "
					 + " left join doctor.m_doctor_master doc "
					 + " on doc.doctor_id = tmp.doctor_id "
					 + " WHERE	tmp.org_id =:orgId and tmp.unit_id =:unitId and tmp.status = 'A' and   tmp.template_type_id in (9,12,13,18,20) ";
			List<TemplateMasterDto> listTemplateMasterDto = sessionFactory.getCurrentSession().createSQLQuery(query).setParameter("orgId", templateReqDto.getOrgId()).setParameter("unitId", templateReqDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(TemplateMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTemplateMasterDto, null);
		}catch(Exception e){
			
			throw e;
		}
	
	}

	@Override
	public Response getTemplateConfigByUnit(UnitTemplateConfigDto reqDto) throws ApplicationException {
		try{
			String query  ="select "
					+ "	config.master_id as \"masterId\", "
					+ "	config.org_id as \"orgId\", "
					+ "	config.unit_id  as \"unitId\", "
					+ "	config.api_key  as \"apiKey\", "
					+ "	config.template_url as \"templateUrl\", "
					+ "	config.template_form as \"templateForm\" "
					+ "from "
					+ "	public.unit_template_config_master config "
					+ "	where config.unit_id = :unitId "
					+ "    and config.status  = 'A'";
			UnitTemplateConfigDto unitTemplateConfigDto = (UnitTemplateConfigDto) sessionFactory.getCurrentSession().createSQLQuery(query).setParameter("unitId", reqDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(UnitTemplateConfigDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, unitTemplateConfigDto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response saveFormsEngineTransactionDetails(FormsEngineTransactionDto dto) throws ApplicationException {
		
		try{
			FormsEngineTransactionMaster  master=new  FormsEngineTransactionMaster();
			master.setEncounterId(dto.getEncounterId() != null ? dto.getEncounterId() : 0);
			master.setAdmissionId(dto.getAdmissionId() != null ? dto.getAdmissionId() : 0);
			master.setTemplateId(dto.getTemplateId() != null ? dto.getTemplateId() : 0);
			master.setConversationId(dto.getConversationId() != null ? dto.getConversationId() : "");
			master.setFormUrl(dto.getFormUrl() != null ? dto.getFormUrl() : "");
			master.setFormTemplateId(dto.getFormTemplateId() != null ? dto.getFormTemplateId() : "");
			master.setTemplateTypeId(dto.getTemplateTypeId() != null ? dto.getTemplateTypeId() : 0);
			master.setCreatedBy(dto.getCreatedBy() != null ? dto.getCreatedBy() : 1);
			master.setUpdatedBy(dto.getCreatedBy() != null ? dto.getCreatedBy() : 1);
			master.setOrgId(dto.getOrgId() != null ? dto.getOrgId() : 0);
			master.setUnitId(dto.getUnitId() != null ? dto.getUnitId() : 0);
			master.setDoctorId(dto.getDoctorId() != null ? dto.getDoctorId() : 0);
			master.setPatientId(dto.getPatientId() != null ? dto.getPatientId() :0);
			master.setDiagnosisId(dto.getDiagnosisId() != null ? dto.getDiagnosisId() : 0);
			master.setmIcpcId(dto.getmIcpcId() != null ? dto.getmIcpcId() : 0);
			master.setmIcpcSystemId(dto.getmIcpcSystemId() != null ? dto.getmIcpcSystemId() : 0);
			master.setIsProvisionalRelease(dto.getIsProvisionalRelease());
			master.setIsFinalRelease(dto.getIsFinalRelease());
			master.setIsReportRelease(dto.getIsReportRelease());
			master.setReviewTime(dto.getReviewTime() !=null ?GlobalCommonDateUtils.getDate(dto.getReviewTime(), "dd-MM-yyyy HH:mm:ss") :TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setIsQuestionary(dto.getIsQuestionary());
			master.settSubSpecimanId(dto.gettSubSpecimanId() != null ? dto.gettSubSpecimanId() : 0);
			master.setReportNo(dto.getReportNo() != null ? dto.getReportNo() : "");
			master.setStainingId(dto.getStainingId() != null ? dto.getStainingId() : 0);
			master.setLabSampleDtlsId(dto.getLabSampleDtlsId() != null ? dto.getLabSampleDtlsId() : 0);
			master.setTemplateResId(dto.getTemplateResId() != null ? dto.getTemplateResId() : 0);
			master.setTemplateTransactionId(dto.getTemplateTransactionId() != null ? dto.getTemplateTransactionId() : 0);
			master.setOrderDetailsId(dto.getOrderDetailsId() != null ? dto.getOrderDetailsId() : 0);
			master.setSampleStatusId(dto.getSampleStatusId() != null ? dto.getSampleStatusId() : 0);
			master.setAppointmentId(dto.getAppointmentId() != null ? dto.getAppointmentId() : 0);
			master.setAdmNoteId(dto.getAdmNoteId() != null ? dto.getAdmNoteId() : 0);
			master.setStatus('A');
			sessionFactory.getCurrentSession().save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, master);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getTemplateByConversationId(TemplateResponseDto dto) throws ApplicationException {
		try{
			List<FormsEngineTransactionDto> template = sessionFactory.getCurrentSession().getNamedQuery("GET_TEMPLATE_BY_CONVERSATION_ID").setParameter("conversationId", dto.getConversationId())
						.setResultTransformer(Transformers.aliasToBean(FormsEngineTransactionDto.class)).list();
			
			if(template!=null && template.size()>0)
				return new Response<>(SUCCESS, SUCCESS_CODE, null, template, template.get(0));
			
			return new Response<>(SUCCESS, SUCCESS_CODE, "No data found.", null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getTemplatesForHabit(TemplateReqDto templateReqDto) throws ApplicationException {
		try{
			String query = "select "
			+ "	distinct(template_mst.template_id) as \"templateId\", "
			+ "	template_mst.title as \"title\", "
			+ "	template_mst.speciality_id as \"specialityId\", "
			+ "	template_mst.sub_speciality_id as \"subSpecialityId\", "
			+ "	template_mst.template_object as \"templateObject\", "
			+ "	template_mst.status as \"status\", "
			+ "	template_mst.doctor_id as \"doctorId\", "
			+ "	template_mst.template_type_id as \"templateTypeId\" ,"
			+ " template_mst.template_format_id as \"templateFormatId\" , "
			+ " template_mst.form_template_id as \"formTemplateId\" "
			+ "from "
			+ "	public.t_template_master template_mst "
			+ "where "
			+ "	template_mst.template_type_id = :templateTypeId "
			+ "	and template_mst.org_id = :orgId "
			+ "	and template_mst.unit_id = :unitId "
			+ "	and template_mst.status = 'A' "
		//	+ "	and mppr.status = 'A' "
			//+ "	and mppr.is_deleted = 'N' "
			+ "	and template_mst.doctor_id is null "
			+ "	and template_mst.speciality_id is null "
			+ "	and template_mst.sub_speciality_id is null "
			;
						
			List<TemplateMasterDto> listTemplateMasterDto = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("orgId", templateReqDto.getOrgId())
					.setParameter("unitId", templateReqDto.getUnitId())
					.setParameter("templateTypeId", 25)
					.setResultTransformer(Transformers.aliasToBean(TemplateMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTemplateMasterDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getTemplateByDoctorOrSpecialityTemplateBySort(TemplateReqDto templateReqDto)
			throws ApplicationException {
		try{
			String templateQuery = "select "
			+ "	tmp.template_id as \"templateId\", "
			+ "	tmp.title as \"title\", "
			+ "	tmp.speciality_id as \"specialityId\", "
			+ "	tmp.sub_speciality_id as \"subSpecialityId\", "
			+ "	tmp.template_object as \"templateObject\", "
			+ "	tmp.status as \"status\", "
			+ "	tmp.doctor_id as \"doctorId\", "
			+ "	tmp.template_type_id as \"templateTypeId\", "
			+ " tmp.template_format_id as \"templateFormatId\" , "
			+ " tmp.form_template_id as \"formTemplateId\" "
			+ "from "
			+ "	public.t_template_master tmp "
			+ "where "
			+ "	tmp.status = 'A' "
			+ "	and tmp.doctor_id is null "
			+ "	and tmp.speciality_id is null "
			+ "	and tmp.sub_speciality_id is null "
			+ "	and tmp.org_id = :orgId "
			+ "	and tmp.unit_id = :unitId "
			+ "	and tmp.template_type_id = :templateTypeId "
			+ " and lower(coalesce(tmp.title, '')) like concat( '%',lower(:searchKeyward), '%')"
			+ " union "
			+ "select "
			+ "	tmp.template_id as \"templateId\", "
			+ "	tmp.title as \"title\", "
			+ "	tmp.speciality_id as \"specialityId\", "
			+ "	tmp.sub_speciality_id as \"subSpecialityId\", "
			+ "	tmp.template_object as \"templateObject\", "
			+ "	tmp.status as \"status\", "
			+ "	tmp.doctor_id as \"doctorId\", "
			+ "	tmp.template_type_id as \"templateTypeId\", "
			+ " tmp.template_format_id as \"templateFormatId\" , "
			+ " tmp.form_template_id as \"formTemplateId\" "
			+ "from "
			+ "	public.t_template_master tmp "
			+ "inner join t_unit_sub_speciality_mapper subM on "
			+ "	tmp.sub_speciality_id = subM.sub_speciality_id "
			+ "	and subM.organization_id =:orgId "
			+ "	and subM.unit_id = :unitId "
			+ "where "
			+ "	tmp.status = 'A' "
			+ "	and tmp.org_id = :orgId "
			+ "	and tmp.unit_id = :unitId "
			+ "	and tmp.template_type_id = :templateTypeId "
			+ "	and tmp.speciality_id =:specialityId "
			+ "	and tmp.doctor_id is null "
			+ " and lower(coalesce(tmp.title, '')) like concat('%', lower(:searchKeyward), '%')"
			+ " union "
			+ "select "
			+ "	tmp.template_id as \"templateId\", "
			+ "	tmp.title as \"title\", "
			+ "	tmp.speciality_id as \"specialityId\", "
			+ "	tmp.sub_speciality_id as \"subSpecialityId\", "
			+ "	tmp.template_object as \"templateObject\", "
			+ "	tmp.status as \"status\", "
			+ "	tmp.doctor_id as \"doctorId\", "
			+ "	tmp.template_type_id as \"templateTypeId\", "
			+ " tmp.template_format_id as \"templateFormatId\" ,"
			+ " tmp.form_template_id as \"formTemplateId\" "
			+ "from "
			+ "	public.t_template_master tmp "
			+ "where "
			+ "	tmp.status = 'A' "
			+ "	and tmp.org_id = :orgId "
			+ "	and tmp.unit_id = :unitId "
			+ "	and tmp.template_type_id = :templateTypeId "
			+ "	and tmp.doctor_id =:doctorId "
			+ "	and tmp.speciality_id is null "
			+ "	and tmp.sub_speciality_id is null "
			+ " and lower(coalesce(tmp.title, '')) like concat('%', lower(:searchKeyward), '%')"
			+ " union "
			+ "select "
			+ "	tmp.template_id as \"templateId\", "
			+ "	tmp.title as \"title\", "
			+ "	tmp.speciality_id as \"specialityId\", "
			+ "	tmp.sub_speciality_id as \"subSpecialityId\", "
			+ "	tmp.template_object as \"templateObject\", "
			+ "	tmp.status as \"status\", "
			+ "	tmp.doctor_id as \"doctorId\", "
			+ "	tmp.template_type_id as \"templateTypeId\", "
			+ " tmp.template_format_id as \"templateFormatId\", "
			+ " tmp.form_template_id as \"formTemplateId\" "
			+ "from "
			+ "	public.t_template_master tmp "
			+ "where "
			+ "	tmp.status = 'A' "
			+ "	and tmp.org_id = :orgId "
			+ "	and tmp.unit_id = :unitId "
			+ "	and tmp.template_type_id = :templateTypeId "
			+ "	and tmp.speciality_id =:specialityId "
			+ "	and tmp.doctor_id is null "
			+ " and lower(coalesce(tmp.title, '')) like concat('%', lower(:searchKeyward), '%')"
			+ " union "
			+ "select "
			+ "	tmp.template_id as \"templateId\", "
			+ "	tmp.title as \"title\", "
			+ "	tmp.speciality_id as \"specialityId\", "
			+ "	tmp.sub_speciality_id as \"subSpecialityId\", "
			+ "	tmp.template_object as \"templateObject\", "
			+ "	tmp.status as \"status\", "
			+ "	tmp.doctor_id as \"doctorId\", "
			+ "	tmp.template_type_id as \"templateTypeId\", "
			+ " tmp.template_format_id as \"templateFormatId\", "
			+ " tmp.form_template_id as \"formTemplateId\" "
			+ "from "
			+ "	public.t_template_master tmp "
			+ "where "
			+ "	tmp.status = 'A' "
			+ "	and tmp.org_id = :orgId "
			+ "	and tmp.unit_id = :unitId "
			+ "	and tmp.template_type_id = :templateTypeId "
			+ "	and tmp.speciality_id =:specialityId "
			+ "	and tmp.doctor_id is null "
			+ "	and tmp.sub_speciality_id is null"
			+ " and lower(coalesce(tmp.title, '')) like concat('%', lower(:searchKeyward), '%')";
			
			List<TemplateMasterDto> listTemplateMasterDto = sessionFactory.getCurrentSession().createSQLQuery(templateQuery)
					.setParameter("orgId", templateReqDto.getOrgId())
					.setParameter("unitId", templateReqDto.getUnitId())
					.setParameter("templateTypeId", templateReqDto.getTemplateTypeId())
					.setParameter("specialityId", templateReqDto.getSpecialityId())
					.setParameter("searchKeyward", templateReqDto.getSearchKeyword())
					.setParameter("doctorId",templateReqDto.getDoctorId())

					.setResultTransformer(Transformers.aliasToBean(TemplateMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTemplateMasterDto, null);
			
			
		}catch(Exception e){
			
			throw e;
		}
	}
//New complaint search sorting
	@Override
	public Response getTemplateByDoctorOrSpecialityComplaintTemplateBySort(TemplateReqDto templateReqDto)
			throws ApplicationException {
		try{
			
			String append = "";
			String query = "select "
			+ "	distinct(template_mst.template_id) as \"templateId\", "
			+ "	template_mst.title as \"title\", "
			+ "	template_mst.speciality_id as \"specialityId\", "
			+ "	template_mst.sub_speciality_id as \"subSpecialityId\", "
			+ "	template_mst.template_object as \"templateObject\", "
			+ "	template_mst.status as \"status\", "
			+ "	template_mst.doctor_id as \"doctorId\", "
			+ "	template_mst.template_type_id as \"templateTypeId\" ,"
			+ "	template_mst.template_format_id as \"templateFormatId\" ,"
			+ " template_mst.form_template_id as \"formTemplateId\" "
			+ "from "
			+ "	public.t_template_master template_mst "
			+ "inner join public.t_icpc_template_mapping mppr on "
			+ "	template_mst.template_id = mppr.template_id "
			+ "where "
			+ "	template_mst.template_type_id = :templateTypeId "
			+ "	and template_mst.org_id = :orgId "
			+ "	and template_mst.unit_id = :unitId "
			+ "	and template_mst.status = 'A' "
			+ "	and mppr.status = 'A' "
			+ "	and mppr.is_deleted = 'N' "
			+ "	and template_mst.doctor_id is null "
			+ "	and template_mst.speciality_id is null "
			+ "	and template_mst.sub_speciality_id is null "
			+ "	and mppr.m_icpc_id =:mIcpcId "
			+ " and lower(coalesce(template_mst.title, '')) like concat('%', lower(:searchKeyword), '%')"
			+ " union select "
			+ "	distinct(template_mst.template_id) as \"templateId\", "
			+ "	template_mst.title as \"title\", "
			+ "	template_mst.speciality_id as \"specialityId\", "
			+ "	template_mst.sub_speciality_id as \"subSpecialityId\", "
			+ "	template_mst.template_object as \"templateObject\", "
			+ "	template_mst.status as \"status\", "
			+ "	template_mst.doctor_id as \"doctorId\", "
			+ "	template_mst.template_type_id as \"templateTypeId\" ,"
			+ "	template_mst.template_format_id as \"templateFormatId\" ,"
			+ " template_mst.form_template_id as \"formTemplateId\" "
			+ "from "
			+ "	public.t_template_master template_mst "
			+ "inner join public.t_icpc_template_mapping mppr on "
			+ "	template_mst.template_id = mppr.template_id "
			+ "where "
			+ "	template_mst.template_type_id = :templateTypeId "
			+ "	and template_mst.org_id = :orgId "
			+ "	and template_mst.unit_id = :unitId "
			+ "	and template_mst.status = 'A' "
			+ "	and mppr.status = 'A' "
			+ "	and mppr.is_deleted = 'N' "
			+ "	and template_mst.doctor_id is null "
			+ "	and template_mst.speciality_id  =:specialityId  "
			+ "	and template_mst.sub_speciality_id  =:subSpecialityId "
			+ "	and mppr.m_icpc_id =:mIcpcId "
			+ " and lower(coalesce(template_mst.title, '')) like concat('%', lower(:searchKeyword ), '%')"
			+ " union "
			+ "select "
			+ "	distinct(template_mst.template_id) as \"templateId\", "
			+ "	template_mst.title as \"title\", "
			+ "	template_mst.speciality_id as \"specialityId\", "
			+ "	template_mst.sub_speciality_id as \"subSpecialityId\", "
			+ "	template_mst.template_object as \"templateObject\", "
			+ "	template_mst.status as \"status\", "
			+ "	template_mst.doctor_id as \"doctorId\", "
			+ "	template_mst.template_type_id as \"templateTypeId\" ,"
			+ "	template_mst.template_format_id as \"templateFormatId\" ,"
			+ " template_mst.form_template_id as \"formTemplateId\" "
			+ "from "
			+ "	public.t_template_master template_mst "
			+ "inner join public.t_icpc_template_mapping mppr on "
			+ "	template_mst.template_id = mppr.template_id "
			+ "where "
			+ "	template_mst.template_type_id = :templateTypeId "
			+ "	and template_mst.org_id = :orgId "
			+ "	and template_mst.unit_id = :unitId "
			+ "	and template_mst.status = 'A' "
			+ "	and mppr.status = 'A' "
			+ "	and mppr.is_deleted = 'N' "
			+ "	and template_mst.doctor_id =:doctorId "
			+ "	and mppr.m_icpc_id =:mIcpcId "
			+ " and lower(coalesce(template_mst.title, '')) like concat('%', lower(:searchKeyword), '%')";
			
			
			List<TemplateMasterDto> listTemplateMasterDto = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("orgId", templateReqDto.getOrgId())
					.setParameter("unitId", templateReqDto.getUnitId())
					.setParameter("templateTypeId", templateReqDto.getTemplateTypeId())
					.setParameter("specialityId", templateReqDto.getSpecialityId())
					.setParameter("subSpecialityId", templateReqDto.getSubSpecialityId())
					.setParameter("mIcpcId", templateReqDto.getmIcpcId())
					.setParameter("searchKeyword", templateReqDto.getSearchKeyword())
					.setParameter("doctorId", templateReqDto.getDoctorId())
					.setResultTransformer(Transformers.aliasToBean(TemplateMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTemplateMasterDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}
//

	@Override
    public Response updateTemplates(TemplateTransactionDto templateTransactionDto) throws ApplicationException {
        try {
			Session session = sessionFactory.getCurrentSession();
			String query = "UPDATE public.t_template_transaction " +
					"SET template_object = :templateObject " +
					"WHERE template_transaction_id = :templateTransactionId";
			int rowsUpdated = session.createSQLQuery(query)
					.setParameter("templateObject", templateTransactionDto.getTemplateObject())
					.setParameter("templateTransactionId", templateTransactionDto.getTemplateTransactionId())
					.executeUpdate();
			return new Response(SUCCESS, SUCCESS_CODE, "Template updated successfully", null, null);
        
        } catch (HibernateException he) {
        logger.error("updateTemplates failed", he);
        } catch (Exception e) {
        logger.error("updateTemplates failed", e);
        }
        return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
        }

	@Override
	public Response getCoversheetTemplateFavourite(TemplateReqDto templateReqDto) throws ApplicationException {
		try{
			
			String append = "";
			String query = "select    " + 
					"	distinct(temtt.template_id) as \"templateId\",    " + 
					"	template_mst.title as \"title\",    " + 
					"	template_mst.speciality_id as \"specialityId\",    " + 
					"	template_mst.sub_speciality_id as \"subSpecialityId\",    " + 
					"	template_mst.template_object as \"templateObject\",    " + 
					"	template_mst.status as \"status\",    " + 
					"	template_mst.doctor_id as \"doctorId\",    " + 
					"	template_mst.template_type_id as \"templateTypeId\" ,   " + 
					"	template_mst.template_format_id as \"templateFormatId\" ,   " + 
					"    template_mst.form_template_id as \"formTemplateId\" ,   " + 
					"	count(*)    " + 
					"from    " + 
					"	public.t_template_transaction temtt    " + 
					"inner join public.t_template_master template_mst on    " + 
					"	template_mst.template_id = temtt.template_id   " + 
					" where temtt.org_id =:orgId   " + 
					" and temtt.unit_id =:unitId   " + 
					" and template_mst.template_type_id =:templateTypeId   " + 
					"group by    " + 
					"	temtt.template_id ,    " + 
					"	template_mst.title  ,   " + 
					"	template_mst.speciality_id,    " + 
					"	template_mst.sub_speciality_id,    " + 
					"	template_mst.template_object,    " + 
					"	template_mst.status,    " + 
					"	template_mst.doctor_id,    " + 
					"	template_mst.template_type_id,   " + 
					"	template_mst.template_format_id,   " + 
					"	 template_mst.form_template_id   " + 
					"order by    " + 
					"	count(*) desc    " + 
					"limit 5 ";
			
			
			List<TemplateMasterDto> listTemplateMasterDto = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("orgId", templateReqDto.getOrgId())
					.setParameter("unitId", templateReqDto.getUnitId())
					.setParameter("templateTypeId", templateReqDto.getTemplateTypeId())
					.setResultTransformer(Transformers.aliasToBean(TemplateMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTemplateMasterDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getOrgAndUnitName(Integer unitId) throws ApplicationException {

		try{
			
			String query = " select mum.unit_name as \"unitName\",mom.organization_name  as \"organizationName\"  " + 
					" from m_unit_master mum  " + 
					" inner join m_organization_master mom  on mom.organization_id = mum.organization_id  " + 
					" where unit_id = :unitId and  mum.status ='A' ";
			UnitMasterDto listTemplateMasterDto = (UnitMasterDto)sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(UnitMasterDto.class)).uniqueResult();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, listTemplateMasterDto);
			
		}catch(Exception e){
		logger.error("getOrgAndUnitName failed", e);
		}
		return null;
	
	}

	@Override
	public Integer getTemplateTypeId(Integer templateId) throws ApplicationException {

		try{
			
			String query = "SELECT template_type_id FROM t_template_master WHERE template_id = :templateId";
			Integer templateTypeId = (Integer) sessionFactory.getCurrentSession()
			        .createSQLQuery(query)
			        .setParameter("templateId", templateId)
			        .uniqueResult();
			
			return templateTypeId;
			
		}catch(Exception e){
		logger.error("getTemplateTypeId failed", e);
		}
		return null;
	
	
	}

	@Override
	public Response getSpecialityName(String conversationId) throws ApplicationException {


		try{
			
			String query = " select   " + 
					"msm.speciality_name as \"specialityName\" " + 
					" from t_nabh_reporting_mapper ttt  " + 
					"inner join doctor.m_speciality_master msm  on msm.speciality_id  = ttt.speciality_id   " + 
					"where conversation_id = :conversationId " + 
					"union  " + 
					"select  " + 
					"msm.speciality_name as \"specialityName\"  " + 
					" from t_incident_reporting_mapper tirm  " + 
					"inner join doctor.m_speciality_master msm  on msm.speciality_id  = tirm.speciality_id   " + 
					"where conversation_id =:conversationId  limit 1 ";
			SpecialityMasterDto listTemplateMasterDto = (SpecialityMasterDto)sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("conversationId", conversationId)
					.setResultTransformer(Transformers.aliasToBean(SpecialityMasterDto.class)).uniqueResult();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, listTemplateMasterDto);
			
		}catch(Exception e){
		logger.error("getSpecialityName failed", e);
		}
		return null;
	
	
	}

	@Override
	public Response getUpdatePath(String docPath, Integer templateTransactionId) throws ApplicationException {

        try {
			Session session = sessionFactory.getCurrentSession();
			String query = "UPDATE public.t_template_transaction " +
					"SET nabh_ince_pdf_path = :docPath " +
					"WHERE template_transaction_id = :templateTransactionId";

			int rowsUpdated = session.createSQLQuery(query)
					.setParameter("docPath", docPath)
					.setParameter("templateTransactionId", templateTransactionId)
					.executeUpdate();


			return new Response(SUCCESS, SUCCESS_CODE, "Template updated successfully", null, null);
        
        } catch (HibernateException he) {
        logger.error("getUpdatePath failed", he);
        } catch (Exception e) {
        logger.error("getUpdatePath failed", e);
        }
        return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
        
	}


}
