
INSERT INTO public.m_organization_master
(organization_id, organization_code, organization_name, organization_country_id, organization_state_id, organization_city_id, organization_district_id, organization_area_id, organization_address, organization_contact, organization_email_id, organization_logo, created_by, created_date, updated_by, updated_date, status, organization_desc, area_id, city_id, country_id, district_id, post_code, state_id, organization_folder, registration_number)
VALUES(1, 'DEMO', 'DEMO', NULL, NULL, NULL, NULL, NULL, 'Delhi', NULL, NULL, NULL, 1, '2018-09-19 04:22:06.448', 1, NULL, 'A', NULL, NULL, NULL, NULL, NULL, 100110, NULL, 'abl10/', NULL);



INSERT INTO public.m_country_master
(country_id, country_code, country_name, country_initial, country_calling_code, created_by, created_date, status, updated_by, updated_date, organization_id, contact_digit)
VALUES(1, 'IN', 'India', 'IND', '91', 1, '2019-02-02 13:06:38.000', 'A', 1, '2019-02-04 23:56:45.000', 1, '10');

INSERT INTO public.m_state_master
(state_id, state_code, state_name, country_id, created_by, created_date, status, updated_by, updated_date, organization_id)
VALUES(1, 'MH', 'Maharashtra', 1, 1, '2017-11-08 11:54:26.000', 'A', 1, '2017-11-08 11:54:26.000', 1);

INSERT INTO public.m_district_master
(district_id, state_id, district_code, created_by, created_date, status, updated_by, updated_date, country_id, district_name, organization_id)
VALUES(1, 1, '001', 199, '2019-05-30 09:57:09.000', 'A', 253, '2019-08-26 15:01:06.000', 1, 'Pune', 1);

INSERT INTO public.m_city_master
(city_id, district_id, city_code, city_name, created_by, created_date, status, updated_by, updated_date, country_id, state_id, organization_id)
VALUES(1, 1, '002', 'Pune', 1, '2017-11-08 11:59:16.000', 'A', 199, '2019-05-30 09:58:33.000', 1, 1, 1);

INSERT INTO public.m_area_master
(area_id, city_id, area_code, area_name, created_by, created_date, status, updated_by, updated_date, country_id, state_id, district_id, post_code, organization_id)
VALUES(1, 1, 'SHVJNGR', 'Shivaji Nagar', 1, '2017-11-08 12:00:12.000', 'A', 5, '2019-01-25 12:58:14.000', 1, 1, 1, '411005', 1);


INSERT INTO public.m_unit_master
(unit_id, organization_id, unit_code, unit_name, unit_country_id, unit_state_id, unit_district_id, unit_city_id, unit_area_id, unit_address, unit_email_id, unit_logo, created_by, created_date, status, updated_by, updated_date, post_code, unit_description, unit_contact, unit_folder, registration_number, unit_contact_2, unit_contact_3, unit_address_2, unit_country_code, unit_website)
VALUES(1, 1, 'DEMO', 'DEMO', 1, 1, 1, 1, 1, 'Kenya', 'abc@gmail.com', NULL, 1, '2018-09-19 04:22:39.559', 'A', 1, '2018-09-19 04:22:39.559', '100110', '-', '0123456789', 'abl10/abl10/', '-', '-', '-', '-', '254', 'www.mpshah.com');


INSERT INTO public.m_user_master
(user_id, user_name, password, created_by, created_date, status, updated_by, updated_date, organization_id, unit_id, login_name, role_id, otp, otp_expiry_date, password_expiry_date, lab_pin)
VALUES(1, 'Admin', 'RGcKiWDPcb7Giyfymr6FSA==', 1, '2018-07-08 11:05:55.871', 'A', 1, '2018-07-08 11:05:55.871', 1, 1, 'admin', 1, NULL, NULL, '20-05-2020 00:00:00', '1112');


INSERT INTO public.m_asset_type
(id, organization_id, unit_id, code, "type", is_active, updated_by, updated_date, added_by, added_date, is_deleted)
VALUES(1, 1, 1, 'C', 'Consumable', true, 1, '2018-05-29 04:45:57.000', 1, '2018-05-29 04:45:57.000', false);

INSERT INTO public.m_asset_type
(id, organization_id, unit_id, code, "type", is_active, updated_by, updated_date, added_by, added_date, is_deleted)
VALUES(2, 1, 1, 'F', 'Fixed', true, 1, '2018-05-29 04:45:57.000', 1, '2018-05-29 04:45:57.000', false);

INSERT INTO public.m_role_master
(role_id, role_name, status, organisation_id, unit_id, created_by, created_date, updated_by, updated_date, is_counter_applicable, role_type_id, doctor_type_id, dashboard_id)
VALUES(1, 'Admin', 'A', 1, 1, 1, '2018-08-06 08:58:43.486', 277, '2019-09-19 19:35:19.000', 'Y', NULL, NULL, NULL);

INSERT INTO public.m_role_master
(role_id, role_name, status, organisation_id, unit_id, created_by, created_date, updated_by, updated_date, is_counter_applicable, role_type_id, doctor_type_id, dashboard_id)
VALUES(2, 'Doctor', 'A', 1, 1, 1, '2018-08-06 08:58:13.662', 199, '2019-05-28 17:53:04.000', 'N', 1, NULL, NULL);

INSERT INTO public.m_role_master
(role_id, role_name, status, organisation_id, unit_id, created_by, created_date, updated_by, updated_date, is_counter_applicable, role_type_id, doctor_type_id, dashboard_id)
VALUES(3, 'Nurse', 'A', 1, 8, 10, '2018-11-13 18:24:06.000', 10, '2018-11-13 18:24:06.000', 'N', NULL, NULL, NULL);


INSERT INTO privilege.privilege
(privilege_id, role_id, modules, module_type, organisation_id, unit_id)
VALUES(1, 1, '[{"id":828,"name":"Radiology","parentId":0,"moduleType":1},{"id":767,"name":"BI","parentId":0,"moduleType":1},{"id":779,"name":"Day Care","parentId":0,"moduleType":1},{"id":768,"name":"MIS Report","parentId":0,"moduleType":1},{"id":26,"name":"IPD","parentId":0,"moduleType":1},{"id":44,"name":"Nursing","parentId":0,"moduleType":1},{"id":101,"name":"Pharmacy","parentId":0,"moduleType":1},{"id":121,"name":"Inventory","parentId":0,"moduleType":1},{"id":161,"name":"Procurement","parentId":0,"moduleType":1},{"id":203,"name":"Asset Management","parentId":0,"moduleType":1},{"id":221,"name":"Equipment Management","parentId":0,"moduleType":1},{"id":231,"name":"CSSD","parentId":0,"moduleType":1},{"id":243,"name":"Clinical Workstation","parentId":0,"moduleType":1},{"id":270,"name":" Radiology Workbench","actionLink":"#/radiologyWorkbench","parentId":0,"moduleType":1},{"id":271,"name":" Physiotherapy Workbench","actionLink":"#/physiotherapyWorkbench","parentId":0,"moduleType":1},{"id":272,"name":"LIS","parentId":0,"moduleType":1},{"id":321,"name":"Theatre Management","parentId":0,"moduleType":1},{"id":328,"name":"Emergency Room","parentId":0,"moduleType":1},{"id":337,"name":"Diet & Kitchen","parentId":0,"moduleType":1},{"id":343,"name":"Linen-Laundry","parentId":0,"moduleType":1},{"id":359,"name":"House Keeping","parentId":0,"moduleType":1},{"id":362,"name":"Mortuary","parentId":0,"moduleType":1},{"id":369,"name":"Global Master","parentId":0,"moduleType":1},{"id":531,"name":"Unit Master","parentId":0,"moduleType":1},{"id":1,"name":"OPD","parentId":0,"moduleType":1},{"id":678,"name":"Back Office","parentId":0,"moduleType":1},{"id":716,"name":"Blood Bank","parentId":0,"moduleType":1},{"id":706,"name":"IVF","parentId":0,"moduleType":1},{"id":751,"name":"Loyalty Card","parentId":0,"moduleType":1},{"id":829,"name":"Workbench","parentId":828,"moduleType":1},{"id":830,"name":"Result Entry","parentId":828,"moduleType":1},{"id":831,"name":"Result Validation","parentId":828,"moduleType":1},{"id":832,"name":"Report Handover","parentId":828,"moduleType":1},{"id":780,"name":"Discharge","parentId":779,"moduleType":1},{"id":27,"name":"Admission","parentId":26,"moduleType":1},{"id":35,"name":"Transfers","parentId":26,"moduleType":1},{"id":37,"name":"Billing","parentId":26,"moduleType":1},{"id":799,"name":"Nursing Rostering","parentId":44,"moduleType":1},{"id":833,"name":"Labor Room Transfer","parentId":44,"moduleType":1},{"id":784,"name":"Day Care Discharge","parentId":44,"moduleType":1},{"id":57,"name":"Pharmacy","parentId":44,"moduleType":1},{"id":65,"name":"Transfer","parentId":44,"moduleType":1},{"id":70,"name":"Discharge","parentId":44,"moduleType":1},{"id":79,"name":"Homecare Medication","parentId":44,"moduleType":1},{"id":81,"name":"Narcotics Store","parentId":44,"moduleType":1},{"id":83,"name":"House Keeping","parentId":44,"moduleType":1},{"id":85,"name":"Crash Cart","parentId":44,"moduleType":1},{"id":45,"name":"Workstation","parentId":44,"moduleType":1},{"id":62,"name":"Inventory","parentId":44,"moduleType":1},{"id":88,"name":"CSSD","parentId":44,"moduleType":1},{"id":93,"name":"Linen-Laundry","parentId":44,"moduleType":1},{"id":98,"name":"Charting","parentId":44,"moduleType":1},{"id":692,"name":"Workstation","parentId":44,"moduleType":1},{"id":102,"name":"IP Pharmacy","parentId":101,"moduleType":1},{"id":110,"name":"OP Pharmacy","parentId":101,"moduleType":1},{"id":115,"name":"Narcotics Store","parentId":101,"moduleType":1},{"id":119,"name":"IN Patient Pharmacy","parentId":101,"moduleType":1},{"id":122,"name":" Nursing","actionLink":"No Link","parentId":121,"moduleType":1},{"id":123,"name":"Store","parentId":121,"moduleType":1},{"id":145,"name":"Stock Adjustments","parentId":121,"moduleType":1},{"id":153,"name":"Stock","parentId":121,"moduleType":1},{"id":155,"name":"Scrap","parentId":121,"moduleType":1},{"id":172,"name":"Quotation","parentId":161,"moduleType":1},{"id":162,"name":"Purchase","parentId":161,"moduleType":1},{"id":186,"name":"Work Order","parentId":161,"moduleType":1},{"id":195,"name":"Consignment","parentId":161,"moduleType":1},{"id":198,"name":"Loan In Out","parentId":161,"moduleType":1},{"id":204,"name":" Indent","parentId":203,"moduleType":1},{"id":208,"name":" Auction Requisition","actionLink":"#/auctionRequistion","parentId":203,"moduleType":1},{"id":209,"name":" Auction Requisition Approval","actionLink":"#/auctionRequisitionApproval","parentId":203,"moduleType":1},{"id":210,"name":" Items For Auction","actionLink":"#/itemsForAuction","parentId":203,"moduleType":1},{"id":211,"name":" Auction Vendor Approval","actionLink":"#/auctionVendorApproval","parentId":203,"moduleType":1},{"id":212,"name":" Non Returnable GatePass","actionLink":"#/nonReturnableGatePass","parentId":203,"moduleType":1},{"id":213,"name":" Asset PMS Scheduler","actionLink":"#/assetPMSScheduler","parentId":203,"moduleType":1},{"id":214,"name":" Asset Issue","actionLink":"#/assetIssueMaster","parentId":203,"moduleType":1},{"id":215,"name":" Asset Issue Acceptance","actionLink":"#/assetIssueAcceptance","parentId":203,"moduleType":1},{"id":216,"name":" Asset Return","actionLink":"#/assetReturn","parentId":203,"moduleType":1},{"id":217,"name":" Asset Return Acceptance","actionLink":"#/assetReturnAcceptance","parentId":203,"moduleType":1},{"id":218,"name":" Rejected Asset Acceptance","actionLink":"#/rejectedAssetAcceptance","parentId":203,"moduleType":1},{"id":219,"name":"Fixed Asset Opening Balance","parentId":203,"moduleType":1},{"id":222,"name":" Preventive Maintenance","parentId":221,"moduleType":1},{"id":227,"name":"Corrective Maintenance","parentId":221,"moduleType":1},{"id":232,"name":"Pre-Sterilization Process","parentId":231,"moduleType":1},{"id":237,"name":"Sterilization Process","parentId":231,"moduleType":1},{"id":240,"name":"Send to Microbiology","actionLink":"#/sendtoMicrobiology","parentId":231,"moduleType":1},{"id":241,"name":" Kit Issue","actionLink":"#/kitIssue","parentId":231,"moduleType":1},{"id":242,"name":" Application","actionLink":"#/application","parentId":231,"moduleType":1},{"id":682,"name":"Sterilization Request Acceptance","parentId":231,"moduleType":1},{"id":757,"name":"Casualty Worklist","parentId":243,"moduleType":1},{"id":244,"name":" History","parentId":243,"moduleType":1},{"id":247,"name":"Order","parentId":243,"moduleType":1},{"id":255,"name":"Transfer","parentId":243,"moduleType":1},{"id":261,"name":"Discharge","parentId":243,"moduleType":1},{"id":265,"name":"New Born","parentId":243,"moduleType":1},{"id":269,"name":" Progress Note","actionLink":"#/progressNote","parentId":243,"moduleType":1},{"id":758,"name":"Doctor Desk","parentId":243,"moduleType":1},{"id":770,"name":"Serology","parentId":272,"moduleType":1},{"id":273,"name":" Phlebotomy","parentId":272,"moduleType":1},{"id":279,"name":"Central Receiving","parentId":272,"moduleType":1},{"id":283,"name":"Biochemistry","parentId":272,"moduleType":1},{"id":289,"name":"Hematology","parentId":272,"moduleType":1},{"id":295,"name":"Histopathology","parentId":272,"moduleType":1},{"id":307,"name":"Microbiology","parentId":272,"moduleType":1},{"id":315,"name":" Report Handover","actionLink":"#/worklistForPrinting","parentId":272,"moduleType":1},{"id":316,"name":" Out Source","parentId":272,"moduleType":1},{"id":320,"name":" Workload Dashboard","actionLink":"#/workloadDashboard","parentId":272,"moduleType":1},{"id":322,"name":"OT Completion","actionLink":"#/OTCompletion","parentId":321,"moduleType":1},{"id":323,"name":"OT Billing Completion","actionLink":"#/surgeryNote","parentId":321,"moduleType":1},{"id":324,"name":"Surgery Note","actionLink":"#/surgeryNote","parentId":321,"moduleType":1},{"id":325,"name":"Anesthesia Note","actionLink":"#/anesthesiaNote","parentId":321,"moduleType":1},{"id":326,"name":"Implant Indent","actionLink":"#/implantIndent","parentId":321,"moduleType":1},{"id":327,"name":"Implant Indent Acceptance","actionLink":"#/implantIndentAcceptance","parentId":321,"moduleType":1},{"id":776,"name":"ER Mark For Discharge","parentId":328,"moduleType":1},{"id":789,"name":"ER Bed Transfer","parentId":328,"moduleType":1},{"id":329,"name":" ER-Encounter","actionLink":"#/unknownPatientReg/personalForUnknown","parentId":328,"moduleType":1},{"id":330,"name":" Triagework List","actionLink":"#/bedTypeAllocation","parentId":328,"moduleType":1},{"id":331,"name":" ER Patient List","actionLink":"#/erPatientList","parentId":328,"moduleType":1},{"id":332,"name":" Map Of Bed","actionLink":"#/mapOfBedForER","parentId":328,"moduleType":1},{"id":333,"name":" Due For Discharge","actionLink":"#/ERdueForDischarge","parentId":328,"moduleType":1},{"id":334,"name":" Ready For Billing","actionLink":"#/ERreadyForBilling","parentId":328,"moduleType":1},{"id":335,"name":" Final Discharge","actionLink":"#/ERfinalDischarge","parentId":328,"moduleType":1},{"id":336,"name":" Admission Note","actionLink":"#/admissionRequest","parentId":328,"moduleType":1},{"id":338,"name":" Diet Planning","actionLink":"#/dietPatientRequest","parentId":337,"moduleType":1},{"id":339,"name":" Diet Order","actionLink":"#/dietOrder","parentId":337,"moduleType":1},{"id":340,"name":" Diet Issue","actionLink":"#/dietIssue","parentId":337,"moduleType":1},{"id":341,"name":" Diet Acceptance","actionLink":"#/dietAcceptance","parentId":337,"moduleType":1},{"id":342,"name":" Rejected Diet List","actionLink":"#/rejectedDietList","parentId":337,"moduleType":1},{"id":344,"name":" Linen Issue","actionLink":"#/linenIssue","parentId":343,"moduleType":1},{"id":345,"name":" Linen Return","actionLink":"#/linenReturn","parentId":343,"moduleType":1},{"id":346,"name":" Linen Return Acceptance","actionLink":"#/linenReturnAcceptance","parentId":343,"moduleType":1},{"id":347,"name":" Rejected Linen Acceptance","actionLink":"#/rejectedLinenAcceptance","parentId":343,"moduleType":1},{"id":348,"name":" Send To Condemnation","actionLink":"#/sendToCondemnation","parentId":343,"moduleType":1},{"id":349,"name":" Acceptance of Condemnation","actionLink":"#/acceptanceOfCondemnation","parentId":343,"moduleType":1},{"id":350,"name":" Convert of Condemnation Stock","actionLink":"#/convertOfCondemnationStock","parentId":343,"moduleType":1},{"id":351,"name":" Issue To Staff","actionLink":"#/issueToStaff","parentId":343,"moduleType":1},{"id":352,"name":" Staff Exchange Linen","actionLink":"#/staffExchangeLinen","parentId":343,"moduleType":1},{"id":353,"name":" Staff Exchange Linen Issue","actionLink":"#/staffExchangelinenIssue","parentId":343,"moduleType":1},{"id":354,"name":" Washing Request","actionLink":"#/washingRequest","parentId":343,"moduleType":1},{"id":355,"name":" Acceptance Washing Request","actionLink":"#/acceptanceWashingRequest","parentId":343,"moduleType":1},{"id":356,"name":" Issue Dirty Linen","actionLink":"#/issueDirtyLinen","parentId":343,"moduleType":1},{"id":357,"name":" Acceptance Of Dirty Linen","actionLink":"#/acceptanceOfDirtyLinen","parentId":343,"moduleType":1},{"id":358,"name":" Issue Washed Linen","actionLink":"#/issueWashedLinen","parentId":343,"moduleType":1},{"id":681,"name":"Staff Linen Allocation","parentId":343,"moduleType":1},{"id":700,"name":"Washed Linen Acceptance","parentId":343,"moduleType":1},{"id":360,"name":" Worklist","actionLink":"#/houseKeeping","parentId":359,"moduleType":1},{"id":361,"name":" Under Maintenance","actionLink":"#/bedUnderMaintenance","parentId":359,"moduleType":1},{"id":363,"name":" Death Registration","actionLink":"#/deathRegistration","parentId":362,"moduleType":1},{"id":364,"name":" Pending List","actionLink":"#/morguePendinglist","parentId":362,"moduleType":1},{"id":365,"name":" Mortuary Availability","actionLink":"#/morgueAvailability","parentId":362,"moduleType":1},{"id":366,"name":" Mortuary Reservation","actionLink":"#/morgueReservation","parentId":362,"moduleType":1},{"id":367,"name":" Release Mortuary","actionLink":"#/morgueReleased","parentId":362,"moduleType":1},{"id":368,"name":" Mortuary Bed Management","actionLink":"#/mortuaryBedManagement","parentId":362,"moduleType":1},{"id":409,"name":"Rota Creation Master","actionLink":"#/rotaCreation","parentId":369,"moduleType":1},{"id":401,"name":"ADT","parentId":369,"moduleType":1},{"id":514,"name":"OTIMS","parentId":369,"moduleType":1},{"id":370,"name":" Organization Structure","parentId":369,"moduleType":1},{"id":380,"name":"Patient Registration","parentId":369,"moduleType":1},{"id":445,"name":"Billing","parentId":369,"moduleType":1},{"id":457,"name":"Pharmacy","parentId":369,"moduleType":1},{"id":410,"name":"Day wise Rota Creation","actionLink":"#/daywiseRotaCreation","parentId":369,"moduleType":1},{"id":490,"name":"Common","parentId":369,"moduleType":1},{"id":505,"name":"Baby Registration","parentId":369,"moduleType":1},{"id":507,"name":"Diet and Kitchen","parentId":369,"moduleType":1},{"id":411,"name":"LIS","parentId":369,"moduleType":1},{"id":503,"name":"OPD","parentId":369,"moduleType":1},{"id":476,"name":"Clinical Workstation","parentId":369,"moduleType":1},{"id":804,"name":"Inventory","parentId":531,"moduleType":1},{"id":791,"name":"QMS","parentId":531,"moduleType":1},{"id":807,"name":"Fund Management","parentId":531,"moduleType":1},{"id":820,"name":"Visitor Type","parentId":531,"moduleType":1},{"id":812,"name":"Ambulance Service","parentId":531,"moduleType":1},{"id":407,"name":"Nursing","parentId":531,"moduleType":1},{"id":532,"name":"Organization Structure","parentId":531,"moduleType":1},{"id":541,"name":"ADT","parentId":531,"moduleType":1},{"id":551,"name":"CSSD","parentId":531,"moduleType":1},{"id":570,"name":"Billing","parentId":531,"moduleType":1},{"id":622,"name":"Pacakge Configuration","parentId":531,"moduleType":1},{"id":628,"name":"Pharmacy","parentId":531,"moduleType":1},{"id":639,"name":"Clinical","actionLink":"No Link","parentId":531,"moduleType":1},{"id":640,"name":"Asset Management","parentId":531,"moduleType":1},{"id":647,"name":"Common","actionLink":"No Link","parentId":531,"moduleType":1},{"id":648,"name":" Mortuary","parentId":531,"moduleType":1},{"id":650,"name":"Staff Registration","actionLink":"#/staffRegistration","parentId":531,"moduleType":1},{"id":651,"name":"Doctor Registration","actionLink":"#/doctorRegistration","parentId":531,"moduleType":1},{"id":652,"name":"Scheduling","parentId":531,"moduleType":1},{"id":656,"name":"Schedule Block Unblock","parentId":531,"moduleType":1},{"id":659,"name":"Configuration","parentId":531,"moduleType":1},{"id":661,"name":"User Managment","parentId":531,"moduleType":1},{"id":667,"name":"Template Utility","parentId":531,"moduleType":1},{"id":556,"name":"LIS","parentId":531,"moduleType":1},{"id":728,"name":"Loyalty Card","parentId":531,"moduleType":1},{"id":730,"name":"Blood Bank","parentId":531,"moduleType":1},{"id":22,"name":"Doctor Share","parentId":1,"moduleType":1},{"id":2,"name":"Patient Management","parentId":1,"moduleType":1},{"id":10,"name":"Billing","parentId":1,"moduleType":1},{"id":602,"name":" Company Invoice Creation","parentId":678,"moduleType":1},{"id":603,"name":" Company Remainder","parentId":678,"moduleType":1},{"id":604,"name":" Company Query & Response","parentId":678,"moduleType":1},{"id":605,"name":" Company Settlement","parentId":678,"moduleType":1},{"id":606,"name":" Settled Invoices","parentId":678,"moduleType":1},{"id":607,"name":" Payment Collection","parentId":678,"moduleType":1},{"id":679,"name":"Cash Desk Management","parentId":678,"moduleType":1},{"id":718,"name":"Blood Bank Process","parentId":716,"moduleType":1},{"id":717,"name":"Donor","parentId":716,"moduleType":1},{"id":708,"name":"Stimulaion Calendar","parentId":706,"moduleType":1},{"id":781,"name":"Due For Discharge","parentId":780,"moduleType":1},{"id":782,"name":"Cancel Discharge","parentId":780,"moduleType":1},{"id":783,"name":"Discharge Summary","parentId":780,"moduleType":1},{"id":28,"name":"Admission Request","actionLink":"#/admissionRequest","parentId":27,"moduleType":1},{"id":29,"name":"Pending Admission","actionLink":"#/pendingAdmissionDischarge","parentId":27,"moduleType":1},{"id":30,"name":"Bed Reservation List","actionLink":"#/bedReservationList","parentId":27,"moduleType":1},{"id":31,"name":"Bed Availability","actionLink":"#/bedAvailability","parentId":27,"moduleType":1},{"id":32,"name":"Bed Allocation","actionLink":"#/bedAllocation","parentId":27,"moduleType":1},{"id":33,"name":"Admitted patient List","actionLink":"#/admittedPatientList","parentId":27,"moduleType":1},{"id":34,"name":"Issued Pass","actionLink":"#/passIssued","parentId":27,"moduleType":1},{"id":687,"name":"Admission Request","parentId":27,"moduleType":1},{"id":711,"name":"Admission","parentId":27,"moduleType":1},{"id":36,"name":"Acceptance Transfer Request","actionLink":"#/transferRequestAcceptance","parentId":35,"moduleType":1},{"id":38,"name":"Bill Estimation","actionLink":"#/billEstimation","parentId":37,"moduleType":1},{"id":39,"name":"IPD Charges","actionLink":"#/IPDCharges","parentId":37,"moduleType":1},{"id":40,"name":"IPD Billing","actionLink":"#/IPDBilling","parentId":37,"moduleType":1},{"id":41,"name":"Intermediate Billing","actionLink":"#/intermediateBill","parentId":37,"moduleType":1},{"id":42,"name":"Update Payee Information","actionLink":"#/updatePayeeInformation","parentId":37,"moduleType":1},{"id":43,"name":"Pre Authorization Request","actionLink":"#/preAuthorizationRequest","parentId":37,"moduleType":1},{"id":800,"name":"Nursing Allocation Dashboard","parentId":799,"moduleType":1},{"id":801,"name":"Nursing Shift Dashboard","parentId":799,"moduleType":1},{"id":834,"name":"Transfer Request Acceptance","parentId":833,"moduleType":1},{"id":787,"name":"Final Discharge","parentId":784,"moduleType":1},{"id":785,"name":"Marked For Discharge","parentId":784,"moduleType":1},{"id":823,"name":"Day Care Worklist","parentId":784,"moduleType":1},{"id":786,"name":"Ready For Billing","parentId":784,"moduleType":1},{"id":58,"name":"Issue Acceptance","actionLink":"#/patientIssueAcceptance","parentId":57,"moduleType":1},{"id":59,"name":"Patient Indent","actionLink":"#/patientIndent","parentId":57,"moduleType":1},{"id":60,"name":"Billable Consumption","actionLink":"#/billableConsumption","parentId":57,"moduleType":1},{"id":61,"name":"Return Billable Consumption","actionLink":"#/returnBillableConsumption","parentId":57,"moduleType":1},{"id":66,"name":" Initiate Bed Transfer","actionLink":"#/initiateBedTransfer/initiateRequestTransferBed","parentId":65,"moduleType":1},{"id":67,"name":" Transfer Request","actionLink":"#/transferRequest/initiateRequestTransferBed","parentId":65,"moduleType":1},{"id":68,"name":" OT Transfer","actionLink":"#/transferToOT/OTTransferRequest","parentId":65,"moduleType":1},{"id":69,"name":" Investigation Transfer","actionLink":"#/transferForInvestigation/investigationTransferRequest","parentId":65,"moduleType":1},{"id":71,"name":" Marked For Discharge","actionLink":"#/patientForDischarge","parentId":70,"moduleType":1},{"id":72,"name":" Medication Reconciliation","actionLink":"#/dischargeMedicationReconciliation","parentId":70,"moduleType":1},{"id":73,"name":" Medication Hand Over","actionLink":"#/dischargeMedicationHandover","parentId":70,"moduleType":1},{"id":74,"name":" Ready For Billing","actionLink":"#/readyForBilling","parentId":70,"moduleType":1},{"id":75,"name":" Final Discharge","actionLink":"#/finalDischarge","parentId":70,"moduleType":1},{"id":76,"name":" Absconding patient","actionLink":"#/abscondingPatient","parentId":70,"moduleType":1},{"id":77,"name":" Request Short Leave","actionLink":"#/shortLeaveDischarge","parentId":70,"moduleType":1},{"id":78,"name":" Short Leave Status","actionLink":"#/shortLeaveStatus","parentId":70,"moduleType":1},{"id":80,"name":" Medication Entry","actionLink":"#/homecareMedicationEntry","parentId":79,"moduleType":1},{"id":82,"name":" Return Empty Ampoules","actionLink":"#/returnOfEmptyAmpoules","parentId":81,"moduleType":1},{"id":84,"name":" Accept Cleaned Bed","actionLink":"#/cleanedBedAccept","parentId":83,"moduleType":1},{"id":86,"name":" Crash Cart Request","actionLink":"#/crashCartRequest","parentId":85,"moduleType":1},{"id":87,"name":" Crash Cart Request Approval","actionLink":"#/crashCartRequest","parentId":85,"moduleType":1},{"id":46,"name":"Patient Acceptance","actionLink":"#/patientAcceptance/newAdmissionList","parentId":45,"moduleType":1},{"id":47,"name":"Allergies","actionLink":"#/allergies","parentId":45,"moduleType":1},{"id":48,"name":"Map Of Bed","actionLink":"#/bedManagement","parentId":45,"moduleType":1},{"id":49,"name":"Drug Administration","actionLink":"#/drugAdministrator","parentId":45,"moduleType":1},{"id":50,"name":"Sample Collection","actionLink":"#/sampleCollection","parentId":45,"moduleType":1},{"id":51,"name":"Nursing Orders","actionLink":"#/nursingOrderView","parentId":45,"moduleType":1},{"id":52,"name":"ICU Booking","actionLink":"#/icuBedBookingStatus","parentId":45,"moduleType":1},{"id":53,"name":"Pending Investigations","actionLink":"#/pendingInvestigations","parentId":45,"moduleType":1},{"id":54,"name":"Homecare Medication Entry","actionLink":"#/homecareMedicationEntry","parentId":45,"moduleType":1},{"id":55,"name":"Patient Sales Return","actionLink":"#/patientIssueReturn","parentId":45,"moduleType":1},{"id":56,"name":"Periodicity Service","actionLink":"#/periodicityService","parentId":45,"moduleType":1},{"id":63,"name":"Non-Billable Consumption","actionLink":"#/nonBillableConsumption","parentId":62,"moduleType":1},{"id":64,"name":"Accept Consumable","actionLink":"#/consumableAcceptance","parentId":62,"moduleType":1},{"id":89,"name":" Kit Indent","actionLink":"#/KitIndent","parentId":88,"moduleType":1},{"id":90,"name":" Kit Indent Approval","actionLink":"#/KitIndentApproval","parentId":88,"moduleType":1},{"id":91,"name":" Kit Issue Acceptance","actionLink":"#/KitIndentAcceptance","parentId":88,"moduleType":1},{"id":92,"name":" Kit Return","actionLink":"#/KitReturn","parentId":88,"moduleType":1},{"id":683,"name":"Sterilization Request","actionLink":"#/sterilizationRequest","parentId":88,"moduleType":1},{"id":94,"name":" Linen Indent","actionLink":"#/linenIndent","parentId":93,"moduleType":1},{"id":95,"name":" Linen Indent Approval","actionLink":"#/linenIndentApproval","parentId":93,"moduleType":1},{"id":96,"name":" Linen Issue Acceptance","actionLink":"#/linenIssueacceptance","parentId":93,"moduleType":1},{"id":97,"name":" Washing Request","actionLink":"#/washingRequest","parentId":93,"moduleType":1},{"id":99,"name":" Input Output Charts","actionLink":"#/inputOutputCharts","parentId":98,"moduleType":1},{"id":100,"name":" Input Output Chart History","actionLink":"#/inputOutputChartHistory","parentId":98,"moduleType":1},{"id":691,"name":"Vitals","actionLink":"#/vitals","parentId":98,"moduleType":1},{"id":777,"name":"Administration Worklist","parentId":692,"moduleType":1},{"id":693,"name":"Service Rendering","actionLink":"#/serviceRendering","parentId":692,"moduleType":1},{"id":694,"name":"Nursing Progress Note","actionLink":"#/nursingProgressNote","parentId":692,"moduleType":1},{"id":103,"name":" Pharmacology Validation","actionLink":"#/pharmacologyValidaionWorklist","parentId":102,"moduleType":1},{"id":104,"name":" Assign Picker","actionLink":"#/assigningPickerDispenser","parentId":102,"moduleType":1},{"id":105,"name":" Pending Issues","actionLink":"#/pendingIssues","parentId":102,"moduleType":1},{"id":106,"name":" Pending Handover WorkList","actionLink":"#/pendingHandoverWorkList","parentId":102,"moduleType":1},{"id":107,"name":" Patient Issue Rejection","actionLink":"#/patientIssueRejection","parentId":102,"moduleType":1},{"id":108,"name":" Rejected Rejection","actionLink":"#/pendingRejectedRejections","parentId":102,"moduleType":1},{"id":109,"name":" Return Acceptance","actionLink":"#/patientReturnAcceptance","parentId":102,"moduleType":1},{"id":111,"name":" Sales Worklist","actionLink":"#/outPatientSalesWorklist","parentId":110,"moduleType":1},{"id":112,"name":" Sales Return","actionLink":"#/billSalesReturn","parentId":110,"moduleType":1},{"id":113,"name":" Bill List","actionLink":"#/pharmacyBillList","parentId":110,"moduleType":1},{"id":114,"name":" Walk in Sales","actionLink":"#/walkInSales","parentId":110,"moduleType":1},{"id":116,"name":" Issue Worklist","actionLink":"#/narcoticsIssueWorklist","parentId":115,"moduleType":1},{"id":117,"name":" Delivery Worklist","actionLink":"#/deliveryWokrlist","parentId":115,"moduleType":1},{"id":118,"name":" Empty Ampoules","actionLink":"#/acceptanceOfEmptyAmpoules","parentId":115,"moduleType":1},{"id":120,"name":" Patient Consumbale Issue","actionLink":"patientConsumableIssue","parentId":119,"moduleType":1},{"id":124,"name":" Store Indent","actionLink":"#/storeIndenting","parentId":123,"moduleType":1},{"id":125,"name":" Store Indent Approval","actionLink":"#/storeIndentApproval","parentId":123,"moduleType":1},{"id":126,"name":" Pending Store Indents","actionLink":"#/pendingStoreIndents","parentId":123,"moduleType":1},{"id":127,"name":" Material Issue","actionLink":"#/materialIssue/pendingIssue","parentId":123,"moduleType":1},{"id":128,"name":" Material Issue Acceptance","actionLink":"#/materialIssueAcceptance","parentId":123,"moduleType":1},{"id":129,"name":" Material Return","actionLink":"#/materialReturn","parentId":123,"moduleType":1},{"id":130,"name":" Material Return Acceptance","actionLink":"#/materialReturnAcceptance","parentId":123,"moduleType":1},{"id":131,"name":" Rejected Material Acceptance","actionLink":"#/rejectedMaterialAcceptance","parentId":123,"moduleType":1},{"id":132,"name":" Material Picker","actionLink":"#/materialPicker","parentId":123,"moduleType":1},{"id":133,"name":" Waste Store","actionLink":"#/wasteStore","parentId":123,"moduleType":1},{"id":134,"name":" Returnable Gatepass Approval","actionLink":"#/returnaleGatepassApproval","parentId":123,"moduleType":1},{"id":135,"name":" Store Stock Dashboard","actionLink":"#/storeStockDashboard","parentId":123,"moduleType":1},{"id":136,"name":" Pending For Shelf Allocation","actionLink":"#/pendingForShelfAllocation","parentId":123,"moduleType":1},{"id":137,"name":" Batch Exp Date Change","actionLink":"#/batchExpDateChange","parentId":123,"moduleType":1},{"id":138,"name":" Batch Exp Date Change Approval","actionLink":"#/batchExpDateChangeApproval","parentId":123,"moduleType":1},{"id":139,"name":" Drug Recall","actionLink":"#/drugRecall","parentId":123,"moduleType":1},{"id":140,"name":" Recall Drugs","actionLink":"#/recallDrugs","parentId":123,"moduleType":1},{"id":141,"name":" Material Inwards","actionLink":"#/materialInwards","parentId":123,"moduleType":1},{"id":142,"name":" Material Inwards Approval","actionLink":"#/materialInwardsApproval","parentId":123,"moduleType":1},{"id":143,"name":" Assest Support Status","actionLink":"#/assetsSupportStatus","parentId":123,"moduleType":1},{"id":144,"name":" Returnable Gatepass","actionLink":"#/returnableGatepass","parentId":123,"moduleType":1},{"id":695,"name":"Stock Register","actionLink":"#/stockRegister","parentId":123,"moduleType":1},{"id":146,"name":" Stop Transaction","actionLink":"#/stopTransaction","parentId":145,"moduleType":1},{"id":147,"name":" Upload Stock","actionLink":"#/uploadStock","parentId":145,"moduleType":1},{"id":148,"name":" Upload Stock Approval","actionLink":"#/uploadStockApproval","parentId":145,"moduleType":1},{"id":149,"name":" Rejected Upload","actionLink":"#/rejectedUploaded","parentId":145,"moduleType":1},{"id":150,"name":" Compute Stock","actionLink":"#/computeStock","parentId":145,"moduleType":1},{"id":151,"name":" Variance Approval","actionLink":"#/varianceApproval","parentId":145,"moduleType":1},{"id":152,"name":" Confirm Stock","actionLink":"#/confirmStock","parentId":145,"moduleType":1},{"id":154,"name":" Opening balance","actionLink":"#/openingBalance","parentId":153,"moduleType":1},{"id":156,"name":" Scrap Request","actionLink":"#/scrapRequest","parentId":155,"moduleType":1},{"id":157,"name":" Scrap Request Approval","actionLink":"#/approvalScrapRequest","parentId":155,"moduleType":1},{"id":158,"name":" Request For Assest Disposal Worklist","actionLink":"#/requestForAssetDisposalWorklist","parentId":155,"moduleType":1},{"id":159,"name":" Waste Assest Disposal Worklist","actionLink":"#/wasteAssetDisposalWorklist","parentId":155,"moduleType":1},{"id":160,"name":" Witness Approval","actionLink":"#/witnessApproval","parentId":155,"moduleType":1},{"id":173,"name":" Quotation Enquiry","actionLink":"#/quotationEnquiry","parentId":172,"moduleType":1},{"id":174,"name":" Quotation Enquiry Approval","actionLink":"#/quotationEnquiryApproval","parentId":172,"moduleType":1},{"id":175,"name":" Quotation Fill In","actionLink":"#/quotationFillIn","parentId":172,"moduleType":1},{"id":176,"name":" Technical Approval","actionLink":"#/technicalApproval","parentId":172,"moduleType":1},{"id":177,"name":" Vendor Negotitation","actionLink":"#/vendorNegotitation","parentId":172,"moduleType":1},{"id":178,"name":" Vendor Selection","actionLink":"#/vendorSelection","parentId":172,"moduleType":1},{"id":179,"name":" PO Creation","actionLink":"#/POCreation","parentId":172,"moduleType":1},{"id":180,"name":" PO Approval","actionLink":"#/POApproval","parentId":172,"moduleType":1},{"id":181,"name":" GRN","actionLink":"#/GRN","parentId":172,"moduleType":1},{"id":182,"name":" GRN Approval","actionLink":"#/GRNApproval","parentId":172,"moduleType":1},{"id":183,"name":" Barcode Printing","actionLink":"#/barcodePrinting","parentId":172,"moduleType":1},{"id":184,"name":" Physical Inspection","actionLink":"#/physicalInspec","parentId":172,"moduleType":1},{"id":185,"name":" Enquiry Sent to Vendor","actionLink":"#/enquirySentToVendor","parentId":172,"moduleType":1},{"id":163,"name":" Item Request","actionLink":"#/itemRequest","parentId":162,"moduleType":1},{"id":164,"name":" Item Approval","actionLink":"#/itemApproval","parentId":162,"moduleType":1},{"id":165,"name":" Item Creation Pending List","actionLink":"#/itemCreationPendingList","parentId":162,"moduleType":1},{"id":166,"name":" Physical Inventory","actionLink":"#/physicalInventory","parentId":162,"moduleType":1},{"id":167,"name":" GRN Against PO","actionLink":"#/GRNAgainstPO","parentId":162,"moduleType":1},{"id":168,"name":" Purchase Request","actionLink":"#/purchaseRequest","parentId":162,"moduleType":1},{"id":169,"name":" Purchase Request Approval","actionLink":"#/purchaseRequestApproval","parentId":162,"moduleType":1},{"id":170,"name":" Local Purchase Request","actionLink":"#/localPurchhaseRequest","parentId":162,"moduleType":1},{"id":171,"name":" Local Purchase Request Approval","actionLink":"#/localPurchhaseRequestApproval","parentId":162,"moduleType":1},{"id":187,"name":" Workorder Request","actionLink":"#/workorderRequest","parentId":186,"moduleType":1},{"id":188,"name":" Quotation Send To Vendor","actionLink":"#/quotationSendToVendor","parentId":186,"moduleType":1},{"id":189,"name":" Approval Worklist","actionLink":"#/workorderApprovalWorklist","parentId":186,"moduleType":1},{"id":190,"name":" Quotation Request","actionLink":"#/workorderQuotationRequest","parentId":186,"moduleType":1},{"id":191,"name":" Quotation Entry","actionLink":"#/workorderQuotationEntry","parentId":186,"moduleType":1},{"id":192,"name":" Quotation Negotiation","actionLink":"#/workorderQuotationNegotiation","parentId":186,"moduleType":1},{"id":193,"name":" Quotation Selection","actionLink":"#/workorderQuotationSelect","parentId":186,"moduleType":1},{"id":194,"name":" Workorder Creation","actionLink":"#/workorderCreation","parentId":186,"moduleType":1},{"id":196,"name":" Consignment Inward","actionLink":"#/consignmentInward","parentId":195,"moduleType":1},{"id":197,"name":" Consignment Outward","actionLink":"#/consignmentOutward","parentId":195,"moduleType":1},{"id":199,"name":" Loan In","actionLink":"#/loanIn","parentId":198,"moduleType":1},{"id":200,"name":" Loan Out","actionLink":"#/loanOut","parentId":198,"moduleType":1},{"id":201,"name":" Loan In Out","actionLink":"#/loanInOut","parentId":198,"moduleType":1},{"id":202,"name":" Loan Out Receive","actionLink":"#/loanOutReceive","parentId":198,"moduleType":1},{"id":205,"name":" Asset Indent","actionLink":"#/assetIndent","parentId":204,"moduleType":1},{"id":206,"name":" Asset Indent Approval","actionLink":"#/assetIndentApproval","parentId":204,"moduleType":1},{"id":207,"name":" Asset Issue","actionLink":"#/assetIssueIndent","parentId":204,"moduleType":1},{"id":220,"name":" Add Fixed Asset Master","actionLink":"#/addfixedAssetMaster","parentId":219,"moduleType":1},{"id":223,"name":" Asset PMS Scheduler","actionLink":"#/assetPMSScheduler","parentId":222,"moduleType":1},{"id":224,"name":" PM Request","actionLink":"#/PMRequest","parentId":222,"moduleType":1},{"id":225,"name":" PM Schedule","actionLink":"#/PMSchedule","parentId":222,"moduleType":1},{"id":226,"name":" Schedule Worklist","actionLink":"#/scheduleWorklist","parentId":222,"moduleType":1},{"id":228,"name":" Corrective Maintenance","actionLink":"#/correctiveMaintenance","parentId":227,"moduleType":1},{"id":229,"name":" Corrective Maintenance Worklist","actionLink":"#/correctiveMaintenanceWorklist","parentId":227,"moduleType":1},{"id":230,"name":" Corrective Maintenance Schedule Worklist","actionLink":"#/correctiveMaintenanceScheduleWorklist","parentId":227,"moduleType":1},{"id":234,"name":" Pending for Inspection","actionLink":"#/preSterilizationInspection","parentId":232,"moduleType":1},{"id":235,"name":" Pending for Dis-Infection","actionLink":"#/preSterilizationDisInfection","parentId":232,"moduleType":1},{"id":236,"name":" Pending for Kit Formation","actionLink":"#/preSterilizationKitFormation","parentId":232,"moduleType":1},{"id":233,"name":"Cleaned Worklist","actionLink":"#/preSterilizationCleaning","parentId":232,"moduleType":1},{"id":238,"name":" Load Batch","actionLink":"#/sterilizationLoadBatch","parentId":237,"moduleType":1},{"id":239,"name":" Pending for Indicators","actionLink":"#/sterilizationPendingIndicators","parentId":237,"moduleType":1},{"id":245,"name":" Allergies","actionLink":"#/allergies","parentId":244,"moduleType":1},{"id":246,"name":" Immunization History","actionLink":"#/immuniztionHistory","parentId":244,"moduleType":1},{"id":248,"name":" Nursing Order","actionLink":"#/nursingOrder","parentId":247,"moduleType":1},{"id":249,"name":" Consult Order","actionLink":"#/interconsultation","parentId":247,"moduleType":1},{"id":250,"name":" Pending Consult Order","actionLink":"#/pendingConsultOrderRequest","parentId":247,"moduleType":1},{"id":251,"name":" Blood Request","actionLink":"#/bloodRequest","parentId":247,"moduleType":1},{"id":252,"name":" Homecare Medication Approval","actionLink":"#/homecareMedicationApproval","parentId":247,"moduleType":1},{"id":253,"name":" Crashcart Medications Approvel","actionLink":"#/administrationApproval","parentId":247,"moduleType":1},{"id":254,"name":" Second Opinion Request","actionLink":"#/secondOpinionRequest","parentId":247,"moduleType":1},{"id":256,"name":" Transfer Request Acceptance","actionLink":"#/transferRequestAcceptance","parentId":255,"moduleType":1},{"id":257,"name":" Initiate Transfer Of Care","actionLink":"#/intiateTransferCare","parentId":255,"moduleType":1},{"id":258,"name":" Accept Transfer Of Care","actionLink":"#/acceptanceOfTransferOfCare","parentId":255,"moduleType":1},{"id":259,"name":" Pending Transfer","actionLink":"#/pendingTransferRequest","parentId":255,"moduleType":1},{"id":260,"name":" Transfer To ICU","actionLink":"#/transferToICU","parentId":255,"moduleType":1},{"id":763,"name":"Discharge Summary","parentId":261,"moduleType":1},{"id":262,"name":" Due For Discharge","actionLink":"#/dueForDischarge","parentId":261,"moduleType":1},{"id":263,"name":" Cancel Discharge","actionLink":"#/cancelForDischarge","parentId":261,"moduleType":1},{"id":264,"name":" Pending Short Leave","actionLink":"#/pendingShortLeave","parentId":261,"moduleType":1},{"id":266,"name":" Registration","actionLink":"#/newBornRegistration","parentId":265,"moduleType":1},{"id":267,"name":" List","actionLink":"#/newBornList","parentId":265,"moduleType":1},{"id":268,"name":" Bed Allocation","actionLink":"#/cradleBedAllocation","parentId":265,"moduleType":1},{"id":771,"name":"Report Entry","parentId":770,"moduleType":1},{"id":772,"name":"Report Validation","parentId":770,"moduleType":1},{"id":773,"name":"Report Release","parentId":770,"moduleType":1},{"id":274,"name":" Worklist","actionLink":"#/phlebotomyWorkList","parentId":273,"moduleType":1},{"id":275,"name":" Send TO CR","actionLink":"#/sendToCR","parentId":273,"moduleType":1},{"id":276,"name":" Patient Arrival","actionLink":"#/patientArrival","parentId":273,"moduleType":1},{"id":277,"name":" Repeat Sample","actionLink":"#/patientArrivalForRepeatSampleCollection","parentId":273,"moduleType":1},{"id":278,"name":" Sample Recollection","actionLink":"#/sampleRecollection","parentId":273,"moduleType":1},{"id":280,"name":" Centrifugation List","actionLink":"#/centrifugationWorkList","parentId":279,"moduleType":1},{"id":281,"name":" OP Samples","actionLink":"#/outPatient","parentId":279,"moduleType":1},{"id":282,"name":" IP Samples","actionLink":"#/inPatient","parentId":279,"moduleType":1},{"id":284,"name":" Pending Acceptance","actionLink":"#/pendingListForAcceptance","parentId":283,"moduleType":1},{"id":285,"name":" Worklist","actionLink":"#/worklistForDepartment/","parentId":283,"moduleType":1},{"id":286,"name":" Report Entry","actionLink":"#/worklistForReportEntry","parentId":283,"moduleType":1},{"id":287,"name":" Validation","actionLink":"#/worklistForValidation","parentId":283,"moduleType":1},{"id":288,"name":" Report Release","actionLink":"#/worklistForReportRelease","parentId":283,"moduleType":1},{"id":290,"name":" Pending Acceptance","actionLink":"#/pendingListForAcceptance","parentId":289,"moduleType":1},{"id":291,"name":" Worklist","actionLink":"#/worklistForDepartment","parentId":289,"moduleType":1},{"id":292,"name":" Report Entry","actionLink":"#/worklistForReportEntry","parentId":289,"moduleType":1},{"id":293,"name":" Validation","actionLink":"#/worklistForValidation","parentId":289,"moduleType":1},{"id":294,"name":" Report Release","actionLink":"#/worklistForReportRelease","parentId":289,"moduleType":1},{"id":296,"name":" Specimen Receipt","actionLink":"#/specimenReceipt","parentId":295,"moduleType":1},{"id":297,"name":" Macroscopic Examination","actionLink":"#/microscopeExamination","parentId":295,"moduleType":1},{"id":298,"name":" Block Creation","actionLink":"#/blockCreation","parentId":295,"moduleType":1},{"id":299,"name":" Slide Creation","actionLink":"#/slideCreation","parentId":295,"moduleType":1},{"id":300,"name":" Microscopic Examination","actionLink":"#/microscopicExaminiationWorkList","parentId":295,"moduleType":1},{"id":301,"name":" Restain Request Worklist","actionLink":"#/restainingRequestWorklist","parentId":295,"moduleType":1},{"id":302,"name":" Addendum Report","actionLink":"#/addendumReport","parentId":295,"moduleType":1},{"id":303,"name":" Storage","actionLink":"#/storage","parentId":295,"moduleType":1},{"id":304,"name":" Second Opinion Request","actionLink":"#/secondOpinionRequest","parentId":295,"moduleType":1},{"id":305,"name":" Slide For Second Opinion","actionLink":"#/pendingSlideForSecondOpinion","parentId":295,"moduleType":1},{"id":306,"name":" Frozen Section Request List","actionLink":"#/frozenSectionRequestList","parentId":295,"moduleType":1},{"id":308,"name":" Sample Acceptance","actionLink":"#/sampleAcceptance","parentId":307,"moduleType":1},{"id":309,"name":" WorkList","actionLink":"#/sampleProcessingWorkList","parentId":307,"moduleType":1},{"id":310,"name":" Incubation Master","actionLink":"#/IncubationMaster","parentId":307,"moduleType":1},{"id":311,"name":" Macroscopic Examination","actionLink":"#/macroscopicExaminationMaster","parentId":307,"moduleType":1},{"id":312,"name":" Biochemical Testing","actionLink":"#/biochemicalTesting","parentId":307,"moduleType":1},{"id":313,"name":" Microscopic Examination","actionLink":"#/microscopicExamination","parentId":307,"moduleType":1},{"id":314,"name":" Sensitivity Testing","actionLink":"#/sensitivityTestingProcess","parentId":307,"moduleType":1},{"id":317,"name":" Out Source Pending List","actionLink":"#/outSourcedPendinglistAssignment","parentId":316,"moduleType":1},{"id":318,"name":" Pending List For Sample Dispatch","actionLink":"#/pendinglistForSampleDispatch","parentId":316,"moduleType":1},{"id":319,"name":" Pending List For Pending Upload","actionLink":"#/pendinglistForReportUpload","parentId":316,"moduleType":1},{"id":402,"name":" Bed Type Master","actionLink":"#/bedTypeMaster","parentId":401,"moduleType":1},{"id":403,"name":" Admission Type","actionLink":"#/admissionTypeMaster","parentId":401,"moduleType":1},{"id":404,"name":" Occupancy Unit Type","actionLink":"#/occupancyUnitMaster","parentId":401,"moduleType":1},{"id":405,"name":" Reason For Discharge Master","actionLink":"#/dischargeTypeMaster","parentId":401,"moduleType":1},{"id":406,"name":" Short Leave Reason","actionLink":"#/shortLeaveReasonMaster","parentId":401,"moduleType":1},{"id":761,"name":"Resource Charges Configuration","parentId":514,"moduleType":1},{"id":515,"name":" Procedure Configuration","actionLink":"#/procedureConfiguration","parentId":514,"moduleType":1},{"id":516,"name":" Subsequent Surgery Configuration","actionLink":"#/subsequentSurgeryConfiguration","parentId":514,"moduleType":1},{"id":517,"name":"Resource Configuration","actionLink":"#/resourceConfiguration","parentId":514,"moduleType":1},{"id":518,"name":" Cancellation Master","actionLink":"#/cancellationMaster","parentId":514,"moduleType":1},{"id":519,"name":" Operation Theatrer","actionLink":"#/operation_Theatre","parentId":514,"moduleType":1},{"id":520,"name":" Procedure Type","actionLink":"#/procedureType","parentId":514,"moduleType":1},{"id":521,"name":" Operation Theatre Type","actionLink":"#/operationTheatreType","parentId":514,"moduleType":1},{"id":522,"name":" Operation Table","actionLink":"#/operationTable","parentId":514,"moduleType":1},{"id":523,"name":" Operation Table Type","actionLink":"#/operationTableType","parentId":514,"moduleType":1},{"id":524,"name":" Anaesthesia Type","actionLink":"#/anaesthesiaType","parentId":514,"moduleType":1},{"id":525,"name":" Checklist Item","actionLink":"#/checklistItem","parentId":514,"moduleType":1},{"id":526,"name":" Checklist Master","actionLink":"#/checklistMaster","parentId":514,"moduleType":1},{"id":527,"name":" Consent Master","actionLink":"#/consentMaster","parentId":514,"moduleType":1},{"id":528,"name":" Consent Type","actionLink":"#/consentType","parentId":514,"moduleType":1},{"id":529,"name":" Anaesthesia Note","actionLink":"#/noteMaster","parentId":514,"moduleType":1},{"id":530,"name":" Faculty","actionLink":"#/facultyMaster","parentId":514,"moduleType":1},{"id":696,"name":"Procedure Configurations","parentId":514,"moduleType":1},{"id":710,"name":"Surgical Calendar","parentId":514,"moduleType":1},{"id":371,"name":" Hospital Master","actionLink":"#/hospitalMaster","parentId":370,"moduleType":1},{"id":372,"name":" Department Master","actionLink":"#/specialityMaster","parentId":370,"moduleType":1},{"id":373,"name":" Sub Department Master","actionLink":"#/subSpecialityMaster","parentId":370,"moduleType":1},{"id":375,"name":" Holiday Calendar Master","actionLink":"#/holidayCalenderMaster","parentId":370,"moduleType":1},{"id":376,"name":" Service Master","actionLink":"#/serviceMaster","parentId":370,"moduleType":1},{"id":377,"name":" Organization Master","actionLink":"#/organizationMaster","parentId":370,"moduleType":1},{"id":378,"name":" Document Type Master","actionLink":"#/documentTypeMaster","parentId":370,"moduleType":1},{"id":379,"name":" License Type Master","actionLink":"#/licenseTypeMaster","parentId":370,"moduleType":1},{"id":381,"name":" Nationality Master","actionLink":"#/nationalityMaster","parentId":380,"moduleType":1},{"id":382,"name":" Race Master","actionLink":"#/raceMaster","parentId":380,"moduleType":1},{"id":383,"name":" Country Master","actionLink":"#/countryMaster","parentId":380,"moduleType":1},{"id":384,"name":" State Master","actionLink":"#/stateMaster","parentId":380,"moduleType":1},{"id":385,"name":" District Master","actionLink":"#/districtMaster","parentId":380,"moduleType":1},{"id":386,"name":" City Master","actionLink":"#/cityMaster","parentId":380,"moduleType":1},{"id":387,"name":" Area Master","actionLink":"#/areaMaster","parentId":380,"moduleType":1},{"id":388,"name":" Identification Card","actionLink":"#/identificationMaster","parentId":380,"moduleType":1},{"id":389,"name":" Sex Master","actionLink":"#/commonMaster/sexmaster","parentId":380,"moduleType":1},{"id":390,"name":" Relation Master","actionLink":"#/commonMaster/relationmaster","parentId":380,"moduleType":1},{"id":391,"name":" Marital Status Master","actionLink":"#/commonMaster/maritalstatus","parentId":380,"moduleType":1},{"id":392,"name":" Religion Master","actionLink":"#/commonMaster/religion","parentId":380,"moduleType":1},{"id":393,"name":" Occupation Master","actionLink":"#/commonMaster/occupation","parentId":380,"moduleType":1},{"id":394,"name":" Patient Source Master","actionLink":"#/commonMaster/patientsource","parentId":380,"moduleType":1},{"id":395,"name":" Referral Type Master","actionLink":"#/commonMaster/referraltype","parentId":380,"moduleType":1},{"id":396,"name":" Referral Master","actionLink":"#/referralMaster","parentId":380,"moduleType":1},{"id":397,"name":" Encounter Type Master","actionLink":"#/encounterTypeMaster","parentId":380,"moduleType":1},{"id":398,"name":" Prefix Master","actionLink":"#/prefixMaster","parentId":380,"moduleType":1},{"id":399,"name":" Consent Type Master","actionLink":"#/consentTypeMaster","parentId":380,"moduleType":1},{"id":400,"name":" Consent Master","actionLink":"#/consentMaster","parentId":380,"moduleType":1},{"id":446,"name":" Patient Category","actionLink":"#/patientCategory","parentId":445,"moduleType":1},{"id":447,"name":" Visit Type Master","actionLink":"#/commonMaster/visitType","parentId":445,"moduleType":1},{"id":448,"name":" Mode Of Payment","actionLink":"#/commonMaster/paymentMode","parentId":445,"moduleType":1},{"id":449,"name":" Payment Entitlement","actionLink":"#/commonMaster/paymentEntitment","parentId":445,"moduleType":1},{"id":450,"name":" Consultant Type","actionLink":"#/commonMaster/consultantType","parentId":445,"moduleType":1},{"id":451,"name":"Currency Master","actionLink":"#/currencyMaster","parentId":445,"moduleType":1},{"id":452,"name":"Currency Denomination","actionLink":"#/currencydenominatio","parentId":445,"moduleType":1},{"id":453,"name":" Doctor Category","actionLink":"#/doctorCategory","parentId":445,"moduleType":1},{"id":454,"name":" Employee Category","actionLink":"#/employeeCategory","parentId":445,"moduleType":1},{"id":455,"name":" Bank Branch","actionLink":"#/bankBranch","parentId":445,"moduleType":1},{"id":456,"name":" Bank Master","actionLink":"#/bankMaster","parentId":445,"moduleType":1},{"id":709,"name":"Billing Bed Category","parentId":445,"moduleType":1},{"id":826,"name":"Schedule Master","parentId":457,"moduleType":1},{"id":827,"name":"Generic Schedule Master","parentId":457,"moduleType":1},{"id":458,"name":" Product Category","actionLink":"#/pharmacyproductCategoryMaster","parentId":457,"moduleType":1},{"id":459,"name":" Formulation Type","actionLink":"#/pharmacyformulationTypeMaster","parentId":457,"moduleType":1},{"id":460,"name":" Schedule Master","actionLink":"#/commonMaster/","parentId":457,"moduleType":1},{"id":461,"name":" Manufacturer Master","actionLink":"#/manufacturerMaster","parentId":457,"moduleType":1},{"id":462,"name":" Supply Days Master","actionLink":"#/supplyDaysMaster","parentId":457,"moduleType":1},{"id":463,"name":" Generic Master","actionLink":"#/genericMaster","parentId":457,"moduleType":1},{"id":464,"name":" Item Master","actionLink":"#/itemMasterGlobal","parentId":457,"moduleType":1},{"id":465,"name":" Package Master","actionLink":"#/packageMaster","parentId":457,"moduleType":1},{"id":466,"name":" Unit Of Measurement","actionLink":"#/unitofMeasurement","parentId":457,"moduleType":1},{"id":467,"name":" Unit Master","actionLink":"#/pharmacyunitMaster","parentId":457,"moduleType":1},{"id":468,"name":" Strength Unit Master","actionLink":"#/pharmacyStrengthUnitMaster","parentId":457,"moduleType":1},{"id":469,"name":" Dispensing Master","actionLink":"#/dispensingMaster","parentId":457,"moduleType":1},{"id":470,"name":" Storage Unit Master","actionLink":"#/storageunitMaster","parentId":457,"moduleType":1},{"id":471,"name":" Kit Master","actionLink":"#/kitMaster","parentId":457,"moduleType":1},{"id":472,"name":" Combination Medication","actionLink":"#/combinationMedicationMaster","parentId":457,"moduleType":1},{"id":473,"name":" Tax Component Master","actionLink":"#/taxComponentMaster","parentId":457,"moduleType":1},{"id":474,"name":" Tax Master","actionLink":"#/taxMaster","parentId":457,"moduleType":1},{"id":475,"name":" Other Charges Master","actionLink":"#/otherCharges","parentId":457,"moduleType":1},{"id":491,"name":"Priority Master","actionLink":"#/commonMaster/priorityMaster","parentId":490,"moduleType":1},{"id":492,"name":"Discount Master","actionLink":"#/commonMaster/priorityMaster","parentId":490,"moduleType":1},{"id":493,"name":"Rejection Master","actionLink":"#/rejectionMaster","parentId":490,"moduleType":1},{"id":494,"name":"Reason Type Master","actionLink":"#/reasonTypeMaster","parentId":490,"moduleType":1},{"id":495,"name":"Reason Master","actionLink":"#/reasonMaster","parentId":490,"moduleType":1},{"id":496,"name":"Qualification Master","actionLink":"#/qualificationMaster","parentId":490,"moduleType":1},{"id":497,"name":"Degree Master","actionLink":"#/degreeMaster","parentId":490,"moduleType":1},{"id":498,"name":"Employee Category Master","actionLink":"#/employeeCategoryMaster","parentId":490,"moduleType":1},{"id":499,"name":"Doctor Category Master","actionLink":"#/doctorCategoryMaster","parentId":490,"moduleType":1},{"id":500,"name":"Designation Master","actionLink":"#/designationMaster","parentId":490,"moduleType":1},{"id":501,"name":"Inspection Master","actionLink":"#/inspectionMaster","parentId":490,"moduleType":1},{"id":502,"name":"Dose Master","actionLink":"#/doseMaster","parentId":490,"moduleType":1},{"id":508,"name":"Nutritional Master","actionLink":"#/nutritionalMaster","parentId":507,"moduleType":1},{"id":509,"name":"Key Ingredients Master","actionLink":"#/keyIngredientsMaster","parentId":507,"moduleType":1},{"id":510,"name":"Diet Type Master","actionLink":"#/dietTypeMaster","parentId":507,"moduleType":1},{"id":511,"name":"Dish Master","actionLink":"#/dishMaster","parentId":507,"moduleType":1},{"id":512,"name":"Meal Master","actionLink":"#/mealMaster","parentId":507,"moduleType":1},{"id":513,"name":"Day Wise Diet Template","actionLink":"#/mealMaster","parentId":507,"moduleType":1},{"id":825,"name":"Microbiology Stain Master","parentId":411,"moduleType":1},{"id":427,"name":" Colony Master","actionLink":"#/commonMaster/colonyMaster","parentId":411,"moduleType":1},{"id":412,"name":" Sample Master","actionLink":"#/commonMaster/sampleMaster","parentId":411,"moduleType":1},{"id":413,"name":" Container Master","actionLink":"#/commonMaster/containerMaster","parentId":411,"moduleType":1},{"id":414,"name":" Techinque Master","actionLink":"#/commonMaster/techinqueMaster","parentId":411,"moduleType":1},{"id":415,"name":" Prerequisites Master","actionLink":"#/commonMaster/prerequisitesMaster","parentId":411,"moduleType":1},{"id":416,"name":" Age Group Master","actionLink":"#/ageGroupMaster","parentId":411,"moduleType":1},{"id":417,"name":" Unit Master","actionLink":"#/commonMaster/unitMaster","parentId":411,"moduleType":1},{"id":418,"name":" Textual Reference Range","actionLink":"#/textualReferenceRangeMaster","parentId":411,"moduleType":1},{"id":419,"name":" Reagent Master","actionLink":"#/commonMaster/reagentMaster","parentId":411,"moduleType":1},{"id":420,"name":" Report Type Master","actionLink":"#/commonMaster/reportTypeMaster","parentId":411,"moduleType":1},{"id":421,"name":" Bacteria Shape","actionLink":"#/commonMaster/bacteriaShapMaster","parentId":411,"moduleType":1},{"id":422,"name":" Incubation Master","actionLink":"#/incubationPeriodMaster","parentId":411,"moduleType":1},{"id":423,"name":" Wet Film Study Master","actionLink":"#/commonMaster/wtifilmStudyMasterMaster","parentId":411,"moduleType":1},{"id":424,"name":" Media Master","actionLink":"#/commonMaster/mediaMaster","parentId":411,"moduleType":1},{"id":425,"name":" Incubation Test Master","actionLink":"#/commonMaster/incubationTestMaster","parentId":411,"moduleType":1},{"id":426,"name":" Sample Type Media Mapping Master","actionLink":"#/sampleTypeMediaMappingMaster","parentId":411,"moduleType":1},{"id":428,"name":" Bacteria Classification","actionLink":"#/commonMaster/bacteriaClassificationMaster","parentId":411,"moduleType":1},{"id":429,"name":" Antibiotic","actionLink":"#/commonMaster/antibioticsMaster","parentId":411,"moduleType":1},{"id":430,"name":" Antibiotics Class","actionLink":"#/commonMaster/antibioticMaster","parentId":411,"moduleType":1},{"id":431,"name":" Antibiotics Group","actionLink":"#/commonMaster/antibioticsGroupMaster","parentId":411,"moduleType":1},{"id":432,"name":" Addition of Antibiotics Against Antibiotic Class","actionLink":"#/additionofAntibiotic","parentId":411,"moduleType":1},{"id":433,"name":" Addition of Antibiotics Group Master","actionLink":"#/antibioticAdditionGroupMaster","parentId":411,"moduleType":1},{"id":434,"name":" Colony Test With Media Master","actionLink":"#/colonyTestWithMediaMaster","parentId":411,"moduleType":1},{"id":435,"name":" Organism Master","actionLink":"#/commonMaster/organismMaster","parentId":411,"moduleType":1},{"id":436,"name":" Organism Group Master","actionLink":"#/commonMaster/organismGroupMaster","parentId":411,"moduleType":1},{"id":437,"name":" Stainig Master","actionLink":"#/commonMaster/stainigMaster","parentId":411,"moduleType":1},{"id":438,"name":" Sensitivity Test Result","actionLink":"#/commonMaster/sensitivityTypeResultMaster","parentId":411,"moduleType":1},{"id":439,"name":" Block Master","actionLink":"#/commonMaster/blockMaster","parentId":411,"moduleType":1},{"id":440,"name":" Slide Procedure Master","actionLink":"#/commonMaster/slideProcedureMaster","parentId":411,"moduleType":1},{"id":441,"name":" Speciman Mapping With Block & Slide Procedure","actionLink":"#/specimanMapping","parentId":411,"moduleType":1},{"id":442,"name":" Organism Group Mapping With Organism","actionLink":"#/organismMapping","parentId":411,"moduleType":1},{"id":443,"name":" Mapping of Micro-organism","actionLink":"#/mappingofMicroorganism","parentId":411,"moduleType":1},{"id":444,"name":" Speciman Master","actionLink":"#/specimanMAster","parentId":411,"moduleType":1},{"id":504,"name":" Clinic Master","actionLink":"#/clinicMaster","parentId":503,"moduleType":1},{"id":753,"name":"Site Of Administration","parentId":476,"moduleType":1},{"id":477,"name":" Route of Administration","actionLink":"#/routeAdministration","parentId":476,"moduleType":1},{"id":478,"name":" Frequency Master","actionLink":"#/frequencyMaster","parentId":476,"moduleType":1},{"id":479,"name":" Formulation Type & ROA Master","actionLink":"#/formulationTypeROAMaster","parentId":476,"moduleType":1},{"id":480,"name":"Nursing Order Master","actionLink":"#/nursingOrderMaster","parentId":476,"moduleType":1},{"id":481,"name":"Blood Component","actionLink":"#/bloodComponent","parentId":476,"moduleType":1},{"id":482,"name":"Vaccination Master","actionLink":"#/vaccinationMaster","parentId":476,"moduleType":1},{"id":483,"name":"Immunization Schedule","actionLink":"#/immunizationSchedule","parentId":476,"moduleType":1},{"id":484,"name":"System Master","actionLink":"#/systemMaster","parentId":476,"moduleType":1},{"id":485,"name":"System Observation Master","actionLink":"#/systemObservationMaster","parentId":476,"moduleType":1},{"id":486,"name":"Surgery Grade Master","actionLink":"#/surgeryGradeMaster","parentId":476,"moduleType":1},{"id":487,"name":"Surgery Master","actionLink":"#/surgeryMaster","parentId":476,"moduleType":1},{"id":488,"name":"Allergy Type Master","actionLink":"#/allergyTypeMaster","parentId":476,"moduleType":1},{"id":489,"name":"Allergy Master","actionLink":"#/allergyMaster","parentId":476,"moduleType":1},{"id":750,"name":"Vaccination Schedule","parentId":476,"moduleType":1},{"id":805,"name":"Store Type Master","parentId":804,"moduleType":1},{"id":806,"name":"Implant Master","parentId":804,"moduleType":1},{"id":795,"name":"QMS Kiosk Master","parentId":791,"moduleType":1},{"id":796,"name":"QMS Department Master","parentId":791,"moduleType":1},{"id":797,"name":"QMS Counter Master","parentId":791,"moduleType":1},{"id":808,"name":"Fund Reconciliation","parentId":807,"moduleType":1},{"id":809,"name":"Fund Approval","parentId":807,"moduleType":1},{"id":810,"name":"Fund Requisition","parentId":807,"moduleType":1},{"id":811,"name":"Fund Approved List","parentId":807,"moduleType":1},{"id":821,"name":"Pass Type Master","parentId":820,"moduleType":1},{"id":822,"name":"Pass Bed Category Mapper","parentId":820,"moduleType":1},{"id":815,"name":"Service Provider Master","parentId":812,"moduleType":1},{"id":816,"name":"Ambulance Tarrif Master","parentId":812,"moduleType":1},{"id":817,"name":"Resource Allocation Master","parentId":812,"moduleType":1},{"id":818,"name":"Ambulance Facilities Master","parentId":812,"moduleType":1},{"id":819,"name":"Ambulance Charges","parentId":812,"moduleType":1},{"id":813,"name":"Ambulance Type Master","parentId":812,"moduleType":1},{"id":814,"name":"Ambulance Master","parentId":812,"moduleType":1},{"id":408,"name":" Shift Master","actionLink":"#/shiftMaster","parentId":407,"moduleType":1},{"id":802,"name":"Nurse Allocation Master","parentId":407,"moduleType":1},{"id":374,"name":" ICU Type Master","actionLink":"#/icutypeMaster","parentId":532,"moduleType":1},{"id":533,"name":"Department Master","actionLink":"#/departmentMasterUnit","parentId":532,"moduleType":1},{"id":534,"name":"Sub Department Master","actionLink":"#/subDepartmentMasterUnit","parentId":532,"moduleType":1},{"id":535,"name":" ICU Type Master","actionLink":"#/ICUtypeMasterUnit","parentId":532,"moduleType":1},{"id":536,"name":" Holiday Calendar Master","actionLink":"#/holidayCalendarMasterUnit","parentId":532,"moduleType":1},{"id":537,"name":" Floor Master","actionLink":"#/floorMaster","parentId":532,"moduleType":1},{"id":538,"name":" Floor Map Master","actionLink":"#/floorMapMaster","parentId":532,"moduleType":1},{"id":539,"name":" Amenity Master","actionLink":"#/amenityMaster","parentId":532,"moduleType":1},{"id":540,"name":" Wing Master","actionLink":"#/wingMaster","parentId":532,"moduleType":1},{"id":697,"name":"Weekend Master","parentId":532,"moduleType":1},{"id":698,"name":"Order Set Master","parentId":532,"moduleType":1},{"id":704,"name":"Counter Master","parentId":532,"moduleType":1},{"id":705,"name":"Cabin Master","parentId":532,"moduleType":1},{"id":550,"name":" Room Master","actionLink":"#/roomMaster","parentId":541,"moduleType":1},{"id":542,"name":" Billing Bed Category Master","actionLink":"#/bedCategoryMasterUnit","parentId":541,"moduleType":1},{"id":543,"name":" Bed Category Master","actionLink":"#/bedCategoryMaster","parentId":541,"moduleType":1},{"id":544,"name":" Billing Bed Category","actionLink":"#/billingBedCategoryMasterUnit","parentId":541,"moduleType":1},{"id":545,"name":" Ward Master","actionLink":"#/wardMaster","parentId":541,"moduleType":1},{"id":546,"name":" Bed Master","actionLink":"#/bedMaster","parentId":541,"moduleType":1},{"id":547,"name":" Nursing Station Master","actionLink":"#/nursingStationMaster","parentId":541,"moduleType":1},{"id":548,"name":" Nursing Ward Mapping","actionLink":"#/nursingStationWardMapping","parentId":541,"moduleType":1},{"id":549,"name":" Meal Preference Master","actionLink":"#/mealPreferenceMaster","parentId":541,"moduleType":1},{"id":552,"name":" Sterilization Type Master","actionLink":"#/sterilizationTypeMaster","parentId":551,"moduleType":1},{"id":553,"name":" Autoclave Master","actionLink":"#/autoclaveMaster","parentId":551,"moduleType":1},{"id":554,"name":" Wrapper expiry master","actionLink":"#/wrapperExpiryMaster","parentId":551,"moduleType":1},{"id":555,"name":" Kit Creation","actionLink":"#/kitCreation","parentId":551,"moduleType":1},{"id":765,"name":"Patient Type Details","parentId":570,"moduleType":1},{"id":764,"name":"Patient Type","parentId":570,"moduleType":1},{"id":788,"name":"Hospital Accounts Master","parentId":570,"moduleType":1},{"id":593,"name":" Charge Conversion","actionLink":"#/entitlementCoversion","parentId":570,"moduleType":1},{"id":594,"name":" Auto Charge Review","actionLink":"#/autoChargeReview","parentId":570,"moduleType":1},{"id":595,"name":" Write Off","actionLink":"#/writeOff","parentId":570,"moduleType":1},{"id":596,"name":" Write Off Details","actionLink":"#/writeOffDetails","parentId":570,"moduleType":1},{"id":599,"name":" Round Off Configuration","actionLink":"#/roundOffConfiguration","parentId":570,"moduleType":1},{"id":601,"name":" Remainder For NOK","actionLink":"#/reminderForNOK","parentId":570,"moduleType":1},{"id":608,"name":" Company Contract","actionLink":"#/companyContractMaster","parentId":570,"moduleType":1},{"id":609,"name":" Tariff Master","actionLink":"#/tariffMaster","parentId":570,"moduleType":1},{"id":610,"name":" Service Tariff Master","actionLink":"#/serviceTariffMaster","parentId":570,"moduleType":1},{"id":571,"name":" Insurance Master","actionLink":"#/insuranceMaster","parentId":570,"moduleType":1},{"id":572,"name":" TPA Master","actionLink":"#/TPAmaster","parentId":570,"moduleType":1},{"id":573,"name":" Insurance & TPA Mapping","actionLink":"#/InsuranceTPAMapping","parentId":570,"moduleType":1},{"id":574,"name":" Corporate Master","actionLink":"#/corporateMasterGlobal","parentId":570,"moduleType":1},{"id":575,"name":" Grade Creation Master","actionLink":"#/gradeCreationMaster","parentId":570,"moduleType":1},{"id":576,"name":" Billing Group Master","actionLink":"#/billingGroupMaster","parentId":570,"moduleType":1},{"id":577,"name":" Package List","actionLink":"#/packageList","parentId":570,"moduleType":1},{"id":578,"name":" Variable Pricing","actionLink":"#/variableSquencePricing/sequencePricing","parentId":570,"moduleType":1},{"id":579,"name":" Account Payable Master","actionLink":"#/accountPayableMaster","parentId":570,"moduleType":1},{"id":580,"name":" Account Payable","actionLink":"#/accountPayable","parentId":570,"moduleType":1},{"id":581,"name":" Account Payable Comparsion","actionLink":"#/accountPayableComparsion","parentId":570,"moduleType":1},{"id":582,"name":" Account Payable Status","actionLink":"#/accountPayableStatus","parentId":570,"moduleType":1},{"id":583,"name":" Unit Service Master","actionLink":"#/unitServiceMaster","parentId":570,"moduleType":1},{"id":584,"name":" Unit Service Mapping","actionLink":"#/unitServiceMapping","parentId":570,"moduleType":1},{"id":585,"name":" Unit Service Base Price","actionLink":"#/unitServiceBasePrice","parentId":570,"moduleType":1},{"id":586,"name":" Schedule Price Unit Service","actionLink":"#/schedulePriceUnitService","parentId":570,"moduleType":1},{"id":587,"name":" Order Set","actionLink":"#/orderSet","parentId":570,"moduleType":1},{"id":588,"name":" Order Set List","actionLink":"#/orderSetList","parentId":570,"moduleType":1},{"id":589,"name":" Admission Rejection List","actionLink":"#/admissionRejectionList","parentId":570,"moduleType":1},{"id":590,"name":" Refund CR List","actionLink":"#/refundCRList/cashForRefund","parentId":570,"moduleType":1},{"id":591,"name":" Find Bill List","actionLink":"#/findBillList","parentId":570,"moduleType":1},{"id":592,"name":" Charge Conversion","actionLink":"#/chargeConversion","parentId":570,"moduleType":1},{"id":611,"name":" OP Consulation Configuration","actionLink":"#/OPConsulationConfigurationMaster","parentId":570,"moduleType":1},{"id":612,"name":" Doctor Consulation","actionLink":"#/doctorConsulationMaster","parentId":570,"moduleType":1},{"id":613,"name":" Global Doctor Share","actionLink":"#/globalDoctorShare","parentId":570,"moduleType":1},{"id":614,"name":" Doctorwise Share","actionLink":"#/doctorWiseShare","parentId":570,"moduleType":1},{"id":615,"name":" Doctor Referral Share","actionLink":"#/doctorReferralShareMaster","parentId":570,"moduleType":1},{"id":616,"name":" AR Master","actionLink":"#/ARmaster","parentId":570,"moduleType":1},{"id":617,"name":" AP Master","actionLink":"#/APmaster","parentId":570,"moduleType":1},{"id":618,"name":" GL Code","actionLink":"#/GLcode","parentId":570,"moduleType":1},{"id":619,"name":" Periodicity Service Configuration","actionLink":"#/periodicityServiceConfig","parentId":570,"moduleType":1},{"id":620,"name":" IP Default Service Configuration","actionLink":"#/IPdefaultServiceConfig","parentId":570,"moduleType":1},{"id":621,"name":" Deposit Matrix Configuration","actionLink":"#/depositMatrixConfig","parentId":570,"moduleType":1},{"id":684,"name":"IP Consultation Configuration","parentId":570,"moduleType":1},{"id":685,"name":"Pending for PO Creation","parentId":570,"moduleType":1},{"id":623,"name":" EHC Package","actionLink":"#/EHCpackage","parentId":622,"moduleType":1},{"id":624,"name":" Package With CAP","actionLink":"#/packageWithCAP","parentId":622,"moduleType":1},{"id":626,"name":" Either Or Package","actionLink":"#/eitherOrPackage","parentId":622,"moduleType":1},{"id":627,"name":" Multicounter Package","actionLink":"#/multicounterPackage","parentId":622,"moduleType":1},{"id":625,"name":"All Inclusive Package","parentId":622,"moduleType":1},{"id":762,"name":"KIT Master Pharmacy","parentId":628,"moduleType":1},{"id":803,"name":"Pharmacy Tariff Master","parentId":628,"moduleType":1},{"id":629,"name":" Vendor Master","actionLink":"#/vendormaster","parentId":628,"moduleType":1},{"id":630,"name":" Rack Master","actionLink":"#/rackMaster","parentId":628,"moduleType":1},{"id":631,"name":" Main Store Master","actionLink":"#/mainstoremaster","parentId":628,"moduleType":1},{"id":632,"name":" Store Item Mapping Master","actionLink":"#/substoremaster","parentId":628,"moduleType":1},{"id":633,"name":" Product Restriction","actionLink":"#/productRestriction/productCategoryMapping","parentId":628,"moduleType":1},{"id":634,"name":" Pharmacological Classification","actionLink":"#/pharmacologicalClassificationMaster","parentId":628,"moduleType":1},{"id":635,"name":" Storewise Generic Mapper","actionLink":"#/storewiseGenericMapper","parentId":628,"moduleType":1},{"id":636,"name":" Consumable Barcode Configuration","actionLink":"#/consumableBarcodeConfig","parentId":628,"moduleType":1},{"id":637,"name":" Indent Template Master","actionLink":"#/indentTemplateMaster","parentId":628,"moduleType":1},{"id":638,"name":" ETO Item Configuration","actionLink":"#/ETOItemConfiguration","parentId":628,"moduleType":1},{"id":707,"name":"Order set Master","parentId":639,"moduleType":1},{"id":824,"name":"RMO Allocation Master","parentId":639,"moduleType":1},{"id":641,"name":" Asset Type Master","actionLink":"#/assetTypeMaster","parentId":640,"moduleType":1},{"id":642,"name":" Asset Category Master","actionLink":"#/assetCategoryMaster","parentId":640,"moduleType":1},{"id":643,"name":" Product Category Master","actionLink":"#/productCategoryMaster","parentId":640,"moduleType":1},{"id":644,"name":" Fixed Asset Master","actionLink":"#/fixedAssetMaster","parentId":640,"moduleType":1},{"id":646,"name":" General Insurace Master","actionLink":"#/generalInsuranceMaster","parentId":640,"moduleType":1},{"id":645,"name":" Equipment Master","actionLink":"#/commonMaster/","parentId":640,"moduleType":1},{"id":649,"name":" Mortuary Bed Master","actionLink":"#/mortuaryBedMaster","parentId":648,"moduleType":1},{"id":653,"name":" Appointment Scheduling","actionLink":"#/appointmentSchedulingUnitMaster","parentId":652,"moduleType":1},{"id":654,"name":" Modality Type Master","actionLink":"#/modalityTypeMaster","parentId":652,"moduleType":1},{"id":655,"name":" Modality Master","actionLink":"#/modalityMaster","parentId":652,"moduleType":1},{"id":657,"name":" Specialty Wise","actionLink":"#/scheduleWise","parentId":656,"moduleType":1},{"id":658,"name":" Doctor Wise","actionLink":"#/doctorWise","parentId":656,"moduleType":1},{"id":775,"name":"Auto Service Posting Configuration","parentId":659,"moduleType":1},{"id":660,"name":" Application Configuration","actionLink":"#/applicationConfiguration","parentId":659,"moduleType":1},{"id":699,"name":"Prefix Configuration","parentId":659,"moduleType":1},{"id":774,"name":"SMS Utility","parentId":659,"moduleType":1},{"id":836,"name":"Department Store Mapper","actionLink":"#/departmentStoreMapper","parentId":661,"moduleType":1},{"id":778,"name":"OTIMS Role Creation","parentId":661,"moduleType":1},{"id":677,"name":"Access Control","parentId":661,"moduleType":1},{"id":672,"name":"Role Creation","parentId":661,"moduleType":1},{"id":674,"name":"User Role Managment","parentId":661,"moduleType":1},{"id":675,"name":"Role Managment","parentId":661,"moduleType":1},{"id":676,"name":"User Authentication","parentId":661,"moduleType":1},{"id":715,"name":"User Counter Mapper","parentId":661,"moduleType":1},{"id":689,"name":"User Nursing Station Mapper","parentId":661,"moduleType":1},{"id":690,"name":"User Store Mapper","parentId":661,"moduleType":1},{"id":714,"name":"Doctor Cabin Mapper","parentId":661,"moduleType":1},{"id":673,"name":"Role Permission","actionLink":"#/accessControl1","parentId":661,"moduleType":1},{"id":746,"name":"Department Nursing Station Mapper","parentId":661,"moduleType":1},{"id":668,"name":" Data Object","actionLink":"#/dataObject","parentId":667,"moduleType":1},{"id":669,"name":" Create Template","actionLink":"#/templateUtility","parentId":667,"moduleType":1},{"id":670,"name":" Templates","actionLink":"#/templates","parentId":667,"moduleType":1},{"id":688,"name":"Template ICD Code Mapping","actionLink":"#/templateICDCodeMapping","parentId":667,"moduleType":1},{"id":798,"name":"Serology Test Finding Master","parentId":556,"moduleType":1},{"id":557,"name":"Phlebotomy","actionLink":"#/commonMaster/phlebotomyMaster","parentId":556,"moduleType":1},{"id":558,"name":"Test Master","actionLink":"#/testMaster","parentId":556,"moduleType":1},{"id":559,"name":" Histopathology Test","actionLink":"#/histopathologyTest","parentId":556,"moduleType":1},{"id":560,"name":" Speciman Type","actionLink":"#/specimanType","parentId":556,"moduleType":1},{"id":561,"name":"Header Master","actionLink":"#/commonMaster/headerMaster","parentId":556,"moduleType":1},{"id":562,"name":"Parameter Master","actionLink":"#/parameterMaster","parentId":556,"moduleType":1},{"id":563,"name":"Multiparameter Test Master","actionLink":"#/multiparameterTestMaster","parentId":556,"moduleType":1},{"id":564,"name":" Department Master","actionLink":"#/commonMaster/departmentMaster","parentId":556,"moduleType":1},{"id":565,"name":" Template Master","actionLink":"#/templateMaster","parentId":556,"moduleType":1},{"id":566,"name":" Parameter Formula","actionLink":"#/parameterFormulaMaster","parentId":556,"moduleType":1},{"id":567,"name":" Profile/Panel Test","actionLink":"#/profilepanelTestMaster","parentId":556,"moduleType":1},{"id":568,"name":" Storage Location Master","actionLink":"#/storageLocationMasterForSlideBlocks","parentId":556,"moduleType":1},{"id":569,"name":" Biochemical Test Master","actionLink":"#/commonMaster/biochemicalTest","parentId":556,"moduleType":1},{"id":712,"name":"Costing Parameter","parentId":556,"moduleType":1},{"id":713,"name":"Costing Sheet","parentId":556,"moduleType":1},{"id":729,"name":"Loyalty Card Creation","parentId":728,"moduleType":1},{"id":743,"name":"Loyalty Card Configuration","parentId":728,"moduleType":1},{"id":731,"name":"Blood Bag Type","parentId":730,"moduleType":1},{"id":732,"name":"Blood Donor Type","parentId":730,"moduleType":1},{"id":733,"name":"Blood Test Result Master","parentId":730,"moduleType":1},{"id":734,"name":"Blood Test","parentId":730,"moduleType":1},{"id":735,"name":"Blood Bag Capacity","parentId":730,"moduleType":1},{"id":736,"name":"Blood Component","parentId":730,"moduleType":1},{"id":737,"name":"Reason Of Discard","parentId":730,"moduleType":1},{"id":738,"name":"Screening Test","parentId":730,"moduleType":1},{"id":739,"name":"Blood Bag Type Component Master","parentId":730,"moduleType":1},{"id":740,"name":"Donor Rejection Master","parentId":730,"moduleType":1},{"id":741,"name":"Blood Group Matching Master","parentId":730,"moduleType":1},{"id":742,"name":"Blood Bank Master","parentId":730,"moduleType":1},{"id":23,"name":"Doctor Share Process","actionLink":"#/doctorShareProcess","parentId":22,"moduleType":1},{"id":25,"name":"Referral Doctor Share Process","actionLink":"#/referralDoctorShareProcess","parentId":22,"moduleType":1},{"id":24,"name":"Doctor Share Payment Voucher","actionLink":"#/doctorSharePaymentVoucher","parentId":22,"moduleType":1},{"id":759,"name":"Nuesing Assesment Display","parentId":2,"moduleType":1},{"id":760,"name":"Cabin Display","parentId":2,"moduleType":1},{"id":5,"name":"Encounter","actionLink":"#/encounter/encounterDetails","parentId":2,"moduleType":1},{"id":4,"name":"Appointment Scheduling","actionLink":"#/appointmentScheduling","parentId":2,"moduleType":1},{"id":6,"name":"Encounter Queue Management","actionLink":"#/encounterQueueManagement","parentId":2,"moduleType":1},{"id":7,"name":"Appointment List","actionLink":"#/appointmentList","parentId":2,"moduleType":1},{"id":8,"name":"Patient List","actionLink":"#/patientList","parentId":2,"moduleType":1},{"id":9,"name":"OPD Coversheet","actionLink":"#/encounterQueueManagement","parentId":2,"moduleType":1},{"id":680,"name":"Appointments","parentId":2,"moduleType":1},{"id":686,"name":"Encounter Queue EHC","parentId":2,"moduleType":1},{"id":702,"name":"Nursing Worklist","parentId":2,"moduleType":1},{"id":701,"name":"Counter Worklist","parentId":2,"moduleType":1},{"id":703,"name":"Self Registration","parentId":2,"moduleType":1},{"id":744,"name":"Growth Chart","parentId":2,"moduleType":1},{"id":3,"name":"Patient Registration","actionLink":"#/patientRegistration","parentId":2,"moduleType":1},{"id":747,"name":"Token Display","parentId":2,"moduleType":1},{"id":12,"name":"Deposit","actionLink":"#/deposit","parentId":10,"moduleType":1},{"id":13,"name":"Bill List","actionLink":"#/billList","parentId":10,"moduleType":1},{"id":14,"name":"Debit Note","actionLink":"#/debitNote","parentId":10,"moduleType":1},{"id":15,"name":"Debit Note List","actionLink":"#/debitNoteList","parentId":10,"moduleType":1},{"id":16,"name":"Credit Note","actionLink":"#/creditNote","parentId":10,"moduleType":1},{"id":17,"name":"Credit Note List","actionLink":"#/creditNoteList","parentId":10,"moduleType":1},{"id":18,"name":"Refund","actionLink":"#/refund","parentId":10,"moduleType":1},{"id":19,"name":"Refund Against Deposit","actionLink":"#/refundAgainstDeposit","parentId":10,"moduleType":1},{"id":20,"name":"Refund List","actionLink":"#/refundList","parentId":10,"moduleType":1},{"id":21,"name":"Refund Against CR","actionLink":"#/refundAgainstCR","parentId":10,"moduleType":1},{"id":754,"name":"Addendum Bill","parentId":10,"moduleType":1},{"id":11,"name":"OPD Bill","parentId":10,"moduleType":1},{"id":745,"name":"Discount Rights","parentId":10,"moduleType":1},{"id":748,"name":"Discount Request Worklist","parentId":10,"moduleType":1},{"id":749,"name":"Discount Request Approval","parentId":10,"moduleType":1},{"id":752,"name":"Assign Loyalty Card","parentId":10,"moduleType":1},{"id":598,"name":"Interim Handover","actionLink":"#/intrimCashHandover","parentId":679,"moduleType":1},{"id":597,"name":" Cash Opening Balance","actionLink":"#/cashOpeningBalance","parentId":679,"moduleType":1},{"id":600,"name":"Shift Closing","actionLink":"#/cashCounterClosing","parentId":679,"moduleType":1},{"id":722,"name":"Transfusion Transmitted Diseases","parentId":718,"moduleType":1},{"id":723,"name":"Red Cell Grouping Worklist","parentId":718,"moduleType":1},{"id":724,"name":"Red Cell Serum Worklist","parentId":718,"moduleType":1},{"id":725,"name":"Blood Bag Component Extraction","parentId":718,"moduleType":1},{"id":726,"name":"Blood Bag Stock Updation","parentId":718,"moduleType":1},{"id":727,"name":"Patient Requisition Worklist","parentId":718,"moduleType":1},{"id":719,"name":"Donor Registration","parentId":717,"moduleType":1},{"id":720,"name":"Donor Screening Worklist","parentId":717,"moduleType":1},{"id":721,"name":"Blood Bag Collection Worklist","parentId":717,"moduleType":1},{"id":1,"name":"Registration","parentId":0,"moduleType":2},{"id":2,"name":"Opd","parentId":0,"moduleType":2},{"id":3,"name":"Adt","parentId":0,"moduleType":2},{"id":4,"name":"Service_Ordering","parentId":0,"moduleType":2},{"id":5,"name":"Laboratory","parentId":0,"moduleType":2},{"id":6,"name":"Otims","parentId":0,"moduleType":2},{"id":7,"name":"Billing_Finance","parentId":0,"moduleType":2},{"id":8,"name":"Quality_Indicator","parentId":0,"moduleType":2},{"id":9,"name":"Clinical","parentId":0,"moduleType":2},{"id":10,"name":"Nursing","parentId":0,"moduleType":2},{"id":11,"name":"Tat_Report","parentId":0,"moduleType":2},{"id":12,"name":"Pharmacy","parentId":0,"moduleType":2},{"id":13,"name":"Registration Statistics","parentId":1,"moduleType":2},{"id":14,"name":"Registration Statistics","parentId":1,"moduleType":2},{"id":15,"name":"Registration Statistics","parentId":1,"moduleType":2},{"id":16,"name":"Timeliness of Appointment","parentId":2,"moduleType":2},{"id":17,"name":"Average Consultation Time","parentId":2,"moduleType":2},{"id":18,"name":"Type of Appointment Booking","parentId":2,"moduleType":2},{"id":19,"name":"Appointment Cancellation Analysis","parentId":2,"moduleType":2},{"id":20,"name":"Visit Analysis","parentId":2,"moduleType":2},{"id":21,"name":"Encounters Top 10 Consultants","parentId":2,"moduleType":2},{"id":22,"name":"Encounters Bottom 10 Consultants","parentId":2,"moduleType":2},{"id":23,"name":"Encounters Comparative","parentId":2,"moduleType":2},{"id":24,"name":"Top 10 Packages ","parentId":2,"moduleType":2},{"id":25,"name":"Bottom 10 packages","parentId":2,"moduleType":2},{"id":26,"name":"Packages Comparative","parentId":2,"moduleType":2},{"id":27,"name":"OPD Register","parentId":2,"moduleType":2},{"id":28,"name":"Executive Health Check up","parentId":2,"moduleType":2},{"id":29,"name":"No show cases","parentId":2,"moduleType":2},{"id":30,"name":"Notifiable Diseases","parentId":2,"moduleType":2},{"id":31,"name":"OPD Inflow Report","parentId":2,"moduleType":2},{"id":32,"name":"Appointments","parentId":2,"moduleType":2},{"id":33,"name":"Appointments to be Rescheduled","parentId":2,"moduleType":2},{"id":34,"name":"Cancelled Appointments","parentId":2,"moduleType":2},{"id":35,"name":"Overbooked appointments","parentId":2,"moduleType":2},{"id":36,"name":"EHC for the Day","parentId":2,"moduleType":2},{"id":37,"name":"Hemodialysis appointment for the day","parentId":2,"moduleType":2},{"id":38,"name":"Admission Analysis","parentId":3,"moduleType":2},{"id":39,"name":"Admission Analysis Comparative","parentId":3,"moduleType":2},{"id":40,"name":"Discharge Analysis ","parentId":3,"moduleType":2},{"id":41,"name":"Bed Occupancy Rate","parentId":3,"moduleType":2},{"id":42,"name":"Bed Occupancy Rate Comparative","parentId":3,"moduleType":2},{"id":43,"name":"Bed Availability Rate","parentId":3,"moduleType":2},{"id":44,"name":"Accident and Emergency Analysis â€“ Non-Clinical","parentId":3,"moduleType":2},{"id":45,"name":"OP Conversion Analysis","parentId":3,"moduleType":2},{"id":46,"name":"OP Conversion Analysis Comparative","parentId":3,"moduleType":2},{"id":47,"name":"Patient Transfer Analysis","parentId":3,"moduleType":2},{"id":48,"name":"Admission Analysis","parentId":3,"moduleType":2},{"id":49,"name":"Admission Analysis Comparative","parentId":3,"moduleType":2},{"id":50,"name":"Average Length of Stay","parentId":3,"moduleType":2},{"id":51,"name":"Average Length of Stay Comprative","parentId":3,"moduleType":2},{"id":52,"name":"Death Statistics","parentId":3,"moduleType":2},{"id":53,"name":"Death Statistics Comparative","parentId":3,"moduleType":2},{"id":54,"name":"Patient Satisfaction","parentId":3,"moduleType":2},{"id":55,"name":"Human Resource Planning","parentId":3,"moduleType":2},{"id":56,"name":"IPD Register","parentId":3,"moduleType":2},{"id":57,"name":"ER Register","parentId":3,"moduleType":2},{"id":58,"name":"Daycare Register","parentId":3,"moduleType":2},{"id":59,"name":"Birth Register","parentId":3,"moduleType":2},{"id":60,"name":"Mortality Register","parentId":3,"moduleType":2},{"id":61,"name":"Still Birth Register","parentId":3,"moduleType":2},{"id":62,"name":"MTP (Medical Termination of Pregnancy) Register","parentId":3,"moduleType":2},{"id":63,"name":"Discharges","parentId":3,"moduleType":2},{"id":64,"name":"Average Length of Stay","parentId":3,"moduleType":2},{"id":65,"name":"Average Length of Stay Procedure wise","parentId":3,"moduleType":2},{"id":66,"name":"Average length of stay diagnosis wise","parentId":3,"moduleType":2},{"id":67,"name":"Comorbidities diagnosed","parentId":3,"moduleType":2},{"id":68,"name":"List of patients based on diagnosis ","parentId":3,"moduleType":2},{"id":69,"name":"Code Blue Cases","parentId":3,"moduleType":2},{"id":70,"name":"Services Rendered to the patient","parentId":3,"moduleType":2},{"id":71,"name":"List of patient under gone surgery","parentId":3,"moduleType":2},{"id":72,"name":"List of patient due for Immunization","parentId":3,"moduleType":2},{"id":73,"name":"List of Patient due for Hemodialysis","parentId":3,"moduleType":2},{"id":74,"name":"Patients receiving High Risk Medications developing adverse drug event","parentId":3,"moduleType":2},{"id":75,"name":"Daycare Admissions","parentId":3,"moduleType":2},{"id":76,"name":"IP Admissions","parentId":3,"moduleType":2},{"id":77,"name":"Planned for discharge for the day","parentId":3,"moduleType":2},{"id":78,"name":"Marked for discharge for the day","parentId":3,"moduleType":2},{"id":79,"name":"Ready for Billing for the day","parentId":3,"moduleType":2},{"id":80,"name":"Ready for Discharge for the day","parentId":3,"moduleType":2},{"id":81,"name":"Bed Reservations","parentId":3,"moduleType":2},{"id":82,"name":"Consultant wise patient location","parentId":3,"moduleType":2},{"id":83,"name":"Patient Location Report for front office","parentId":3,"moduleType":2},{"id":84,"name":"Patients waiting for ICU","parentId":3,"moduleType":2},{"id":85,"name":"Mortuary Utilization Report","parentId":3,"moduleType":2},{"id":86,"name":"Services_ordered_by_consultant(value_wise)","parentId":4,"moduleType":2},{"id":87,"name":"Top_10_consultants_for_service_ordering","parentId":4,"moduleType":2},{"id":88,"name":"Bottom_10_consultants_for_service_ordering","parentId":4,"moduleType":2},{"id":89,"name":"Procedures_ordered_by_surgeon","parentId":4,"moduleType":2},{"id":90,"name":"number_of_services_ordered_by_consultant","parentId":4,"moduleType":2},{"id":91,"name":"Number_of_prescriptions_raised","parentId":4,"moduleType":2},{"id":97,"name":"vacutainer_mapping","parentId":5,"moduleType":2},{"id":92,"name":"Sub_department _wise_number_of_tests_performed","parentId":5,"moduleType":2},{"id":93,"name":"top_10_services_performed ","parentId":5,"moduleType":2},{"id":94,"name":"number_of_tests_performed","parentId":5,"moduleType":2},{"id":95,"name":"pending_worklist_summary ","parentId":5,"moduleType":2},{"id":96,"name":"pending_worklist_detailed","parentId":5,"moduleType":2},{"id":98,"name":"tat_report ","parentId":5,"moduleType":2},{"id":99,"name":"Top_10_surgeries_in_hospital","parentId":6,"moduleType":2},{"id":100,"name":"Top_10_surgeons_in_hospital","parentId":6,"moduleType":2},{"id":101,"name":"Surgeries_performed","parentId":6,"moduleType":2},{"id":102,"name":"surgery_cancellation_report","parentId":6,"moduleType":2},{"id":103,"name":"surgeon_wise_ot_utilization_report","parentId":6,"moduleType":2},{"id":104,"name":"equipment_utilization_report","parentId":6,"moduleType":2},{"id":105,"name":"list_of_confirmed_surgeries","parentId":6,"moduleType":2},{"id":106,"name":"specialty_wise_grade_of_suery_report","parentId":6,"moduleType":2},{"id":107,"name":"grade_wise_surgeries_performed_by_surgeon","parentId":6,"moduleType":2},{"id":108,"name":"time_stamps","parentId":6,"moduleType":2},{"id":109,"name":"ot_wise_turn_around_time","parentId":6,"moduleType":2},{"id":110,"name":"icu_booking_list","parentId":6,"moduleType":2},{"id":111,"name":"surgeries_planned","parentId":6,"moduleType":2},{"id":112,"name":"equipment_availability_status","parentId":6,"moduleType":2},{"id":113,"name":"operating_theatre_utilization_report","parentId":6,"moduleType":2},{"id":114,"name":"cssd_kit_utilization_patient_wise","parentId":6,"moduleType":2},{"id":115,"name":"cssd_kit_stock_report","parentId":6,"moduleType":2},{"id":116,"name":"implants_used_patient_wise","parentId":6,"moduleType":2},{"id":117,"name":"resources_utilization_report_surgery_wise","parentId":6,"moduleType":2},{"id":118,"name":"medication_utilization_report_patient_wise","parentId":6,"moduleType":2},{"id":119,"name":"Billing Analysis","parentId":7,"moduleType":2},{"id":120,"name":"Revenue by Doctor","parentId":7,"moduleType":2},{"id":121,"name":"Revenue by Doctor YoY Comparative","parentId":7,"moduleType":2},{"id":122,"name":"Discount Analysis","parentId":7,"moduleType":2},{"id":123,"name":"Wave off Analysis","parentId":7,"moduleType":2},{"id":124,"name":"Cost &EBIT Analysis","parentId":7,"moduleType":2},{"id":125,"name":"Cost Trend Analysis","parentId":7,"moduleType":2},{"id":126,"name":"Surgery Charge Breakup","parentId":7,"moduleType":2},{"id":127,"name":"OT Surgery Analysis","parentId":7,"moduleType":2},{"id":128,"name":"Inventory Consumption Analysis","parentId":7,"moduleType":2},{"id":129,"name":"Radiology Order Amount","parentId":7,"moduleType":2},{"id":130,"name":"Workflow Analysis","parentId":7,"moduleType":2},{"id":131,"name":"Lab test Analysis","parentId":7,"moduleType":2},{"id":132,"name":"Blood Request and issues Analysis","parentId":7,"moduleType":2},{"id":133,"name":"Revenue for the month","parentId":7,"moduleType":2},{"id":134,"name":"Discounts offered","parentId":7,"moduleType":2},{"id":135,"name":"Total collection Report - Summary","parentId":7,"moduleType":2},{"id":136,"name":"Total Collection Report - Details","parentId":7,"moduleType":2},{"id":137,"name":"Transction Ciode wise report","parentId":7,"moduleType":2},{"id":138,"name":"Revenue JV with Profit Centre","parentId":7,"moduleType":2},{"id":139,"name":"Collection JV with GL Code","parentId":7,"moduleType":2},{"id":140,"name":"Revenue for the Day","parentId":7,"moduleType":2},{"id":141,"name":"Patient Ledger Reconciliation","parentId":7,"moduleType":2},{"id":142,"name":"Inpatient Charges - Summary","parentId":7,"moduleType":2},{"id":143,"name":"Inpatient Charges - Details","parentId":7,"moduleType":2},{"id":144,"name":"Patient payable OP ( Date wise)","parentId":7,"moduleType":2},{"id":145,"name":"Patient Payable IP (Date wise)","parentId":7,"moduleType":2},{"id":146,"name":"Patient payable Daycare (Date wise)","parentId":7,"moduleType":2},{"id":147,"name":"Patient payable ER (Date wise)","parentId":7,"moduleType":2},{"id":148,"name":"Insurance Payable","parentId":7,"moduleType":2},{"id":149,"name":"Payable by Corporates","parentId":7,"moduleType":2},{"id":150,"name":"Bed category wise billing","parentId":7,"moduleType":2},{"id":151,"name":"Package Billing","parentId":7,"moduleType":2},{"id":152,"name":"Package Profitability Report","parentId":7,"moduleType":2},{"id":153,"name":"Surgery Billing (Speciality wise)","parentId":7,"moduleType":2},{"id":154,"name":"Doctor Payable","parentId":7,"moduleType":2},{"id":155,"name":"Home Collection","parentId":7,"moduleType":2},{"id":156,"name":"Discount Report","parentId":7,"moduleType":2},{"id":157,"name":"Discounted Registrations","parentId":7,"moduleType":2},{"id":158,"name":"Wave off Report","parentId":7,"moduleType":2},{"id":159,"name":"Active Deposit Report - Summary","parentId":7,"moduleType":2},{"id":160,"name":"Active Deposit Report - Details","parentId":7,"moduleType":2},{"id":161,"name":"Ambulance Charges","parentId":7,"moduleType":2},{"id":162,"name":"Mortuary Charges","parentId":7,"moduleType":2},{"id":163,"name":"Corporate wise Ledger","parentId":7,"moduleType":2},{"id":164,"name":"Insurance Company wise Ledger","parentId":7,"moduleType":2},{"id":165,"name":"Refund Report","parentId":7,"moduleType":2},{"id":166,"name":"Patient Billing for Admitted Patients","parentId":7,"moduleType":2},{"id":167,"name":"Ageing Report - Self Pay","parentId":7,"moduleType":2},{"id":168,"name":"Aging Report - Insurance Company","parentId":7,"moduleType":2},{"id":169,"name":"Ageing Report - Corporate","parentId":7,"moduleType":2},{"id":170,"name":"Cash flow report","parentId":7,"moduleType":2},{"id":171,"name":"Bank Status Report","parentId":7,"moduleType":2},{"id":172,"name":"Cancelled Services Report","parentId":7,"moduleType":2},{"id":173,"name":"Contract Validity Report","parentId":7,"moduleType":2},{"id":174,"name":"Patient Wise Transaction Report","parentId":7,"moduleType":2},{"id":175,"name":"Patient Category wise Billing Report","parentId":7,"moduleType":2},{"id":176,"name":"Package Validity Report","parentId":7,"moduleType":2},{"id":177,"name":"Service validity Report","parentId":7,"moduleType":2},{"id":178,"name":"Tariff master","parentId":7,"moduleType":2},{"id":179,"name":"Time taken for Initial Assessment of Indoor and Emergency patients","parentId":8,"moduleType":2},{"id":180,"name":"Time taken for Initial Assessment of Indoor and Emergency Patient-Comparative","parentId":8,"moduleType":2},{"id":181,"name":"Documentation of Nursing Care Plan","parentId":8,"moduleType":2},{"id":182,"name":"Admissions with Adverse Drug Reactions","parentId":8,"moduleType":2},{"id":183,"name":"Modification of Anesthesia plan","parentId":8,"moduleType":2},{"id":184,"name":"Unplanned Ventilation following Anesthesia","parentId":8,"moduleType":2},{"id":185,"name":"Unplanned Return to OT","parentId":8,"moduleType":2},{"id":186,"name":"Rescheduling of Surgeries","parentId":8,"moduleType":2},{"id":187,"name":"Patients receiving prophylactic antibiotic with in specified time frame","parentId":8,"moduleType":2},{"id":188,"name":"Wastage of Blood and Blood Products","parentId":8,"moduleType":2},{"id":189,"name":"Urinary Tract Infection rate","parentId":8,"moduleType":2},{"id":190,"name":"Pneumonia Rate","parentId":8,"moduleType":2},{"id":191,"name":"Blood stream infection rate","parentId":8,"moduleType":2},{"id":192,"name":"Surgical site infection rate","parentId":8,"moduleType":2},{"id":193,"name":"Return to ICU with 48 hours","parentId":8,"moduleType":2},{"id":194,"name":"Return to Emergency department with 72 hours","parentId":8,"moduleType":2},{"id":195,"name":"Stock outs","parentId":8,"moduleType":2},{"id":196,"name":"Incidence of bed sores after admission","parentId":8,"moduleType":2},{"id":197,"name":"OT utilization rate","parentId":8,"moduleType":2},{"id":198,"name":"ICU equipment utilization rate","parentId":8,"moduleType":2},{"id":199,"name":"Critical Equipment down time","parentId":8,"moduleType":2},{"id":200,"name":"Nurse to patient ratio for ICUs and wards","parentId":8,"moduleType":2},{"id":201,"name":"No. of patients with Hospital Acquired Infection","parentId":8,"moduleType":2},{"id":202,"name":"Death on OT Table","parentId":8,"moduleType":2},{"id":203,"name":"Readmission Analysis","parentId":8,"moduleType":2},{"id":204,"name":"Readmission Analysis Comparative","parentId":8,"moduleType":2},{"id":205,"name":"accident_and_emergency_analysis_clinical","parentId":9,"moduleType":2},{"id":206,"name":"age_wise_mortality_index_report","parentId":9,"moduleType":2},{"id":207,"name":"maternal_mortality_rate","parentId":9,"moduleType":2},{"id":208,"name":"maternal_mortality_report","parentId":9,"moduleType":2},{"id":209,"name":"medication_tat","parentId":9,"moduleType":2},{"id":210,"name":"c_section_rate","parentId":9,"moduleType":2},{"id":211,"name":"doctor_wise_age_wise_disease_index_report","parentId":9,"moduleType":2},{"id":212,"name":"drug_administration_delay","parentId":10,"moduleType":2},{"id":213,"name":"accident_er_tat(comparative)","parentId":11,"moduleType":2},{"id":214,"name":"accident_er_tat","parentId":11,"moduleType":2},{"id":215,"name":"discharge_tat","parentId":11,"moduleType":2},{"id":216,"name":"discharge_tat(comparative)","parentId":11,"moduleType":2},{"id":217,"name":"Product Category Wise Sales","parentId":12,"moduleType":2},{"id":218,"name":"Product Category Wise Stock","parentId":12,"moduleType":2},{"id":219,"name":"User Wise Issues","parentId":12,"moduleType":2},{"id":220,"name":"Top 10 users","parentId":12,"moduleType":2},{"id":221,"name":"Bottom 10 users","parentId":12,"moduleType":2},{"id":222,"name":"Stock Report","parentId":12,"moduleType":2},{"id":223,"name":"Stock in Transit Report","parentId":12,"moduleType":2},{"id":224,"name":"ABC and VED Analysis","parentId":12,"moduleType":2},{"id":225,"name":"Out Patient sales-Summary","parentId":12,"moduleType":2},{"id":226,"name":"Out Patient sales-Details","parentId":12,"moduleType":2},{"id":227,"name":"ER sales-Summary","parentId":12,"moduleType":2},{"id":228,"name":"ER sales-Details","parentId":12,"moduleType":2},{"id":229,"name":"Daycare sales-Summary","parentId":12,"moduleType":2},{"id":230,"name":"Daycare sales-Details","parentId":12,"moduleType":2},{"id":231,"name":"Inpatient sales-Summary","parentId":12,"moduleType":2},{"id":232,"name":"Inpatient sales-Details","parentId":12,"moduleType":2},{"id":233,"name":"Crash Cart Utilization Report","parentId":12,"moduleType":2},{"id":234,"name":"Expiry Report","parentId":12,"moduleType":2},{"id":235,"name":"Scrap Report","parentId":12,"moduleType":2},{"id":236,"name":"Billable Consumption Report","parentId":12,"moduleType":2},{"id":237,"name":"Non-Billable Consumption Report","parentId":12,"moduleType":2},{"id":238,"name":"Pending Store Indent Report","parentId":12,"moduleType":2},{"id":239,"name":"Pending prescription Report","parentId":12,"moduleType":2},{"id":240,"name":"Discount Report","parentId":12,"moduleType":2},{"id":241,"name":"Patient Returns Report","parentId":12,"moduleType":2},{"id":242,"name":"Pending Vendor Returns","parentId":12,"moduleType":2},{"id":243,"name":"Profit margin Report","parentId":12,"moduleType":2},{"id":244,"name":"Consignment Store Consumption Report","parentId":12,"moduleType":2},{"id":245,"name":"Consignment Store Stock Report","parentId":12,"moduleType":2},{"id":246,"name":"Drug Consumption Report-Patient wise","parentId":12,"moduleType":2},{"id":247,"name":"Stock Adjustment Report","parentId":12,"moduleType":2},{"id":248,"name":"Stock Ledger Report","parentId":12,"moduleType":2},{"id":249,"name":"High Risk Medication Stock Report","parentId":12,"moduleType":2},{"id":250,"name":"High Risk Medication Utilization Report-Patient Wise","parentId":12,"moduleType":2}]', NULL, 1, 1);

INSERT INTO public.t_user_role_mapper
(user_id, role_id, organisation_id, unit_id, status, created_by, created_date, updated_by, updated_date, user_role_mppr_id, dashboard_id)
VALUES(1, 1, 1, 1, 'A', 0, NULL, 1, '2018-11-25 19:43:20.000', 1, NULL);

INSERT INTO public.m_employee_master
(employee_id, unit_id, prefix_id, first_name, middle_name, last_name, gender_id, bar_code, vip_remark, is_blacklist, emp_category_id, is_vip, organization_id, dob, identification_type_id, identification_number, mobile_number, email, country_calling_code, alise_name, identification_expiry_date, occupation_id, speciality_id, sub_speciality_id, emp_designation_id, emp_code, date_of_joining, access_card_number, status, updated_by, updated_date, created_by, created_date, employee_image, user_id, registration_number, password_expiry)
VALUES(1, 1, 1, 'Admin', '', 'Admin', 1, '', '', NULL, 1, 'N', 1, '1971-05-16 00:00:00.000', 1, 'XYZ123', '9090909090', 'info@clinivantage.com', '+91', '', NULL, 1, 10, 11, 1, 'EMP123', '2017-01-01 00:00:00.000', 'ACC1234', 'A', 277, '0013-03-11 00:00:00.000', 1, '2018-05-16 00:00:00.000', NULL, 1, NULL, '30-12-2019 00:00:00');

INSERT INTO public.t_unit_application_billing_configuration
(unit_application_billing_configuration_id, organization_id, unit_id, default_self_tariff_id, is_pre_or_post_billing, created_date, created_by, updated_date, updated_by, status, is_appointment_required, radiology_speciality_id, is_counter_applicable, loyalticard_patient_category, doc_role_id, nurse_role_id, staff_role_id, admin_role_id, pdf_generation_url, is_service_wise_split_bill, patient_reg_ser_id, opd_billing_cat_id, self_payee_id, consultation_speciality_id, consultation_sub_speciality_id, sender_id, casualty_speciality_id, paediatrics_speciality_id, is_global_search, pharmacy_service_id, company_patient_category, insurance_patient_category, pharmacy_return_sale_service_id, is_kiosk_required, ot_rent_service_id, surgeon_charges_service_id, surcharge_service_id, mysql_smart_jdbc, is_nursing_assessment, is_rendered_service_rate_editable, er_speciality_id, is_show_patient_by_bed, admission_charges_service_id, is_smart_card_required, is_digital_signature_required, is_pin_pad_required, is_corporate_emp_verification_req, group_practice_no, group_practice_name, dob_format, practice_no, start_date_format, is_advise_tab_visible, is_pacs_required, obgyn_speciality_id, hemodialysis_speciality_id, is_consolidated_bill)
VALUES(1, 1, 1, NULL, 'Y', '2019-09-13 15:13:37.000', 1, '2019-09-13 15:13:37.000', 1, 'A', 'Y', NUll, 'Y', 9, 2, 3, 1, 1, '', 'Y', 1, 1, 1, 1000, 1000, '', 2027, 21, 'N', 187, 8, 7, 166, 'Y', 285, 284, 297, 'jdbc:mysql://192.168.137.1:3307/smartlink, Vishal, Vishal', 'Y', 'Y', 2056, 'N', 48, 'Y', 'N', 'N', 'Y', 'CLINI001', 'Clinivantage Healthcare', 'dd-MM-yyyy', '34534', NULL, 'Y', 'N', 19, 17, 'N');









INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(408, ' Shift Master', '#/shiftMaster', NULL, 407, 1, '2019-09-27 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(759, 'Nuesing Assesment Display', NULL, NULL, 2, 1, '2018-11-27 21:01:36.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(763, 'Discharge Summary', NULL, NULL, 261, 1, '2018-12-26 16:23:17.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(765, 'Patient Type Details', NULL, NULL, 570, 1, '2019-02-26 13:01:49.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(764, 'Patient Type', NULL, NULL, 570, 1, '2019-02-26 13:01:08.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(775, 'Auto Service Posting Configuration', NULL, NULL, 659, 1, '2019-04-01 16:41:31.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(776, 'ER Mark For Discharge', NULL, NULL, 328, 1, '2019-04-15 16:06:35.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(787, 'Final Discharge', NULL, NULL, 784, 1, '2019-04-30 15:01:16.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(788, 'Hospital Accounts Master', NULL, NULL, 570, 1, '2019-05-07 17:41:29.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(790, 'QMS', NULL, NULL, 0, 1, '2019-05-16 12:09:54.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(798, 'Serology Test Finding Master', NULL, NULL, 556, 1, '2019-05-29 15:05:48.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(799, 'Nursing Rostering', NULL, NULL, 44, 1, '2019-06-02 23:39:02.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(804, 'Inventory', NULL, NULL, 531, 1, '2019-06-20 10:44:39.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(815, 'Service Provider Master', NULL, NULL, 812, 1, '2019-06-20 10:53:11.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(802, 'Nurse Allocation Master', NULL, NULL, 407, 1, '2019-06-13 14:58:47.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(825, 'Microbiology Stain Master', NULL, NULL, 411, 1, '2019-06-25 10:48:59.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(826, 'Schedule Master', NULL, NULL, 457, 1, '2019-07-11 10:43:53.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(828, 'Radiology', NULL, NULL, 0, 1, '2019-07-15 16:22:25.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(374, ' ICU Type Master', '#/icutypeMaster', NULL, 532, 1, '2019-08-24 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(835, 'Role Permission', '#/doctorDashboard', NULL, 0, 1, '2019-07-16 15:52:14.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(1, 'OPD', NULL, NULL, 0, 1, '2018-08-16 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(22, 'Doctor Share', NULL, NULL, 1, 1, '2018-09-06 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(760, 'Cabin Display', NULL, NULL, 2, 1, '2018-11-27 22:17:19.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(762, 'KIT Master Pharmacy', NULL, NULL, 628, 1, '2018-12-25 12:43:32.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(777, 'Administration Worklist', NULL, NULL, 692, 1, '2019-04-18 16:39:06.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(789, 'ER Bed Transfer', NULL, NULL, 328, 1, '2019-05-07 17:41:16.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(791, 'QMS', NULL, NULL, 531, 1, '2019-05-16 12:10:14.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(800, 'Nursing Allocation Dashboard', NULL, NULL, 799, 1, '2019-06-02 23:40:14.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(805, 'Store Type Master', NULL, NULL, 804, 1, '2019-06-20 10:46:03.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(816, 'Ambulance Tarrif Master', NULL, NULL, 812, 1, '2019-06-20 10:54:21.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(409, 'Rota Creation Master', '#/rotaCreation', NULL, 369, 1, '2019-09-28 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(827, 'Generic Schedule Master', NULL, NULL, 457, 1, '2019-07-11 10:45:14.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(829, 'Workbench', NULL, NULL, 828, 1, '2019-07-15 16:22:58.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(836, 'Department Store Mapper', '#/departmentStoreMapper', NULL, 661, 1, '2019-09-06 11:47:02.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(598, 'Interim Handover', '#/intrimCashHandover', NULL, 679, 1, '2020-04-04 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(766, 'Surgery Safety', NULL, NULL, 0, 1, '2019-03-12 13:05:31.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(778, 'OTIMS Role Creation', NULL, NULL, 661, 1, '2019-04-25 15:17:26.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(792, 'QMS Token', NULL, NULL, 790, 1, '2019-05-16 12:10:37.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(801, 'Nursing Shift Dashboard', NULL, NULL, 799, 1, '2019-06-02 23:40:52.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(803, 'Pharmacy Tariff Master', NULL, NULL, 628, 1, '2019-06-18 10:34:48.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(806, 'Implant Master', NULL, NULL, 804, 1, '2019-06-20 10:48:11.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(817, 'Resource Allocation Master', NULL, NULL, 812, 1, '2019-06-20 10:56:56.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(830, 'Result Entry', NULL, NULL, 828, 1, '2019-07-15 16:23:36.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(173, ' Quotation Enquiry', '#/quotationEnquiry', NULL, 172, 1, '2019-02-04 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(761, 'Resource Charges Configuration', NULL, NULL, 514, 1, '2018-12-09 22:21:40.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(767, 'BI', NULL, NULL, 0, 1, '2019-03-12 13:14:04.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(779, 'Day Care', NULL, NULL, 0, 1, '2019-04-30 12:21:36.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(793, 'Call', NULL, NULL, 790, 1, '2019-05-16 12:10:58.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(807, 'Fund Management', NULL, NULL, 531, 1, '2019-06-20 10:48:46.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(818, 'Ambulance Facilities Master', NULL, NULL, 812, 1, '2019-06-20 10:57:51.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(831, 'Result Validation', NULL, NULL, 828, 1, '2019-07-15 16:24:14.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(768, 'MIS Report', NULL, NULL, 0, 1, '2019-03-12 13:14:31.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(780, 'Discharge', NULL, NULL, 779, 1, '2019-04-30 12:59:11.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(794, 'QMS Token Display', NULL, NULL, 790, 1, '2019-05-16 12:11:17.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(808, 'Fund Reconciliation', NULL, NULL, 807, 1, '2019-06-20 10:49:18.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(819, 'Ambulance Charges', NULL, NULL, 812, 1, '2019-06-20 10:58:27.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(832, 'Report Handover', NULL, NULL, 828, 1, '2019-07-15 16:25:02.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(401, 'ADT', NULL, NULL, 369, 1, '2019-09-20 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(769, 'Tele Consulting', NULL, NULL, 0, 1, '2019-03-12 13:15:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(781, 'Due For Discharge', NULL, NULL, 780, 1, '2019-04-30 13:01:03.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(795, 'QMS Kiosk Master', NULL, NULL, 791, 1, '2019-05-16 12:11:57.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(809, 'Fund Approval', NULL, NULL, 807, 1, '2019-06-20 10:49:49.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(820, 'Visitor Type', NULL, NULL, 531, 1, '2019-06-20 10:58:58.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(833, 'Labor Room Transfer', NULL, NULL, 44, 1, '2019-07-16 15:49:57.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(427, ' Colony Master', '#/commonMaster/colonyMaster', NULL, 411, 1, '2019-10-16 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(770, 'Serology', NULL, NULL, 272, 1, '2019-03-28 18:31:56.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(782, 'Cancel Discharge', NULL, NULL, 780, 1, '2019-04-30 14:36:04.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(796, 'QMS Department Master', NULL, NULL, 791, 1, '2019-05-16 12:12:18.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(810, 'Fund Requisition', NULL, NULL, 807, 1, '2019-06-20 10:50:20.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(821, 'Pass Type Master', NULL, NULL, 820, 1, '2019-06-20 10:59:40.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(834, 'Transfer Request Acceptance', NULL, NULL, 833, 1, '2019-07-16 15:52:14.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(514, 'OTIMS', NULL, NULL, 369, 1, '2020-01-11 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(771, 'Report Entry', NULL, NULL, 770, 1, '2019-03-28 18:32:59.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(783, 'Discharge Summary', NULL, NULL, 780, 1, '2019-04-30 14:36:28.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(797, 'QMS Counter Master', NULL, NULL, 791, 1, '2019-05-16 12:12:40.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(811, 'Fund Approved List', NULL, NULL, 807, 1, '2019-06-20 10:50:54.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(822, 'Pass Bed Category Mapper', NULL, NULL, 820, 1, '2019-06-20 11:00:20.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(593, ' Charge Conversion', '#/entitlementCoversion', NULL, 570, 1, '2020-03-30 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(594, ' Auto Charge Review', '#/autoChargeReview', NULL, 570, 1, '2020-03-31 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(595, ' Write Off', '#/writeOff', NULL, 570, 1, '2020-04-01 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(596, ' Write Off Details', '#/writeOffDetails', NULL, 570, 1, '2020-04-02 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(599, ' Round Off Configuration', '#/roundOffConfiguration', NULL, 570, 1, '2020-04-05 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(601, ' Remainder For NOK', '#/reminderForNOK', NULL, 570, 1, '2020-04-07 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(608, ' Company Contract', '#/companyContractMaster', NULL, 570, 1, '2020-04-14 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(609, ' Tariff Master', '#/tariffMaster', NULL, 570, 1, '2020-04-15 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(610, ' Service Tariff Master', '#/serviceTariffMaster', NULL, 570, 1, '2020-04-16 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(772, 'Report Validation', NULL, NULL, 770, 1, '2019-03-28 18:33:28.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(784, 'Day Care Discharge', NULL, NULL, 44, 1, '2019-04-30 14:49:37.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(812, 'Ambulance Service', NULL, NULL, 531, 1, '2019-06-20 10:51:19.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(677, 'Access Control', NULL, NULL, 661, 1, '2018-08-27 14:39:26.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(753, 'Site Of Administration', NULL, NULL, 476, 1, '2018-10-24 15:02:31.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(773, 'Report Release', NULL, NULL, 770, 1, '2019-03-28 18:33:58.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(785, 'Marked For Discharge', NULL, NULL, 784, 1, '2019-04-30 14:51:17.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(813, 'Ambulance Type Master', NULL, NULL, 812, 1, '2019-06-20 10:51:51.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(823, 'Day Care Worklist', NULL, NULL, 784, 1, '2019-06-24 12:39:29.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(26, 'IPD', NULL, NULL, 0, 1, '2018-09-10 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(27, 'Admission', NULL, NULL, 26, 1, '2018-09-11 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(35, 'Transfers', NULL, NULL, 26, 1, '2018-09-19 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(37, 'Billing', NULL, NULL, 26, 1, '2018-09-21 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(44, 'Nursing', NULL, NULL, 0, 1, '2018-09-28 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(57, 'Pharmacy', NULL, NULL, 44, 1, '2018-10-11 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(65, 'Transfer', NULL, NULL, 44, 1, '2018-10-19 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(66, ' Initiate Bed Transfer', '#/initiateBedTransfer/initiateRequestTransferBed', NULL, 65, 1, '2018-10-20 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(67, ' Transfer Request', '#/transferRequest/initiateRequestTransferBed', NULL, 65, 1, '2018-10-21 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(68, ' OT Transfer', '#/transferToOT/OTTransferRequest', NULL, 65, 1, '2018-10-22 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(69, ' Investigation Transfer', '#/transferForInvestigation/investigationTransferRequest', NULL, 65, 1, '2018-10-23 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(70, 'Discharge', NULL, NULL, 44, 1, '2018-10-24 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(71, ' Marked For Discharge', '#/patientForDischarge', NULL, 70, 1, '2018-10-25 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(72, ' Medication Reconciliation', '#/dischargeMedicationReconciliation', NULL, 70, 1, '2018-10-26 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(73, ' Medication Hand Over', '#/dischargeMedicationHandover', NULL, 70, 1, '2018-10-27 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(74, ' Ready For Billing', '#/readyForBilling', NULL, 70, 1, '2018-10-28 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(75, ' Final Discharge', '#/finalDischarge', NULL, 70, 1, '2018-10-29 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(76, ' Absconding patient', '#/abscondingPatient', NULL, 70, 1, '2018-10-30 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(77, ' Request Short Leave', '#/shortLeaveDischarge', NULL, 70, 1, '2018-10-31 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(78, ' Short Leave Status', '#/shortLeaveStatus', NULL, 70, 1, '2018-11-01 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(79, 'Homecare Medication', NULL, NULL, 44, 1, '2018-11-02 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(80, ' Medication Entry', '#/homecareMedicationEntry', NULL, 79, 1, '2018-11-03 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(81, 'Narcotics Store', NULL, NULL, 44, 1, '2018-11-04 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(82, ' Return Empty Ampoules', '#/returnOfEmptyAmpoules', NULL, 81, 1, '2018-11-05 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(83, 'House Keeping', NULL, NULL, 44, 1, '2018-11-06 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(84, ' Accept Cleaned Bed', '#/cleanedBedAccept', NULL, 83, 1, '2018-11-07 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(85, 'Crash Cart', NULL, NULL, 44, 1, '2018-11-08 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(86, ' Crash Cart Request', '#/crashCartRequest', NULL, 85, 1, '2018-11-09 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(172, 'Quotation', NULL, NULL, 161, 1, '2019-02-03 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(2, 'Patient Management', NULL, NULL, 1, 1, '2018-08-17 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(46, 'Patient Acceptance', '#/patientAcceptance/newAdmissionList', NULL, 45, 1, '2018-09-30 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(5, 'Encounter', '#/encounter/encounterDetails', NULL, 2, 1, '2018-08-20 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(4, 'Appointment Scheduling', '#/appointmentScheduling', NULL, 2, 1, '2018-08-19 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(6, 'Encounter Queue Management', '#/encounterQueueManagement', NULL, 2, 1, '2018-08-21 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(7, 'Appointment List', '#/appointmentList', NULL, 2, 1, '2018-08-22 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(8, 'Patient List', '#/patientList', NULL, 2, 1, '2018-08-23 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(9, 'OPD Coversheet', '#/encounterQueueManagement', NULL, 2, 1, '2018-08-24 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(12, 'Deposit', '#/deposit', NULL, 10, 1, '2018-08-27 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(13, 'Bill List', '#/billList', NULL, 10, 1, '2018-08-28 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(14, 'Debit Note', '#/debitNote', NULL, 10, 1, '2018-08-29 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(15, 'Debit Note List', '#/debitNoteList', NULL, 10, 1, '2018-08-30 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(16, 'Credit Note', '#/creditNote', NULL, 10, 1, '2018-08-31 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(17, 'Credit Note List', '#/creditNoteList', NULL, 10, 1, '2018-09-01 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(18, 'Refund', '#/refund', NULL, 10, 1, '2018-09-02 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(19, 'Refund Against Deposit', '#/refundAgainstDeposit', NULL, 10, 1, '2018-09-03 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(20, 'Refund List', '#/refundList', NULL, 10, 1, '2018-09-04 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(21, 'Refund Against CR', '#/refundAgainstCR', NULL, 10, 1, '2018-09-05 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(23, 'Doctor Share Process', '#/doctorShareProcess', NULL, 22, 1, '2018-09-07 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(25, 'Referral Doctor Share Process', '#/referralDoctorShareProcess', NULL, 22, 1, '2018-09-09 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(28, 'Admission Request', '#/admissionRequest', NULL, 27, 1, '2018-09-12 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(29, 'Pending Admission', '#/pendingAdmissionDischarge', NULL, 27, 1, '2018-09-13 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(30, 'Bed Reservation List', '#/bedReservationList', NULL, 27, 1, '2018-09-14 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(31, 'Bed Availability', '#/bedAvailability', NULL, 27, 1, '2018-09-15 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(32, 'Bed Allocation', '#/bedAllocation', NULL, 27, 1, '2018-09-16 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(33, 'Admitted patient List', '#/admittedPatientList', NULL, 27, 1, '2018-09-17 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(34, 'Issued Pass', '#/passIssued', NULL, 27, 1, '2018-09-18 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(36, 'Acceptance Transfer Request', '#/transferRequestAcceptance', NULL, 35, 1, '2018-09-20 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(38, 'Bill Estimation', '#/billEstimation', NULL, 37, 1, '2018-09-22 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(39, 'IPD Charges', '#/IPDCharges', NULL, 37, 1, '2018-09-23 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(40, 'IPD Billing', '#/IPDBilling', NULL, 37, 1, '2018-09-24 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(41, 'Intermediate Billing', '#/intermediateBill', NULL, 37, 1, '2018-09-25 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(42, 'Update Payee Information', '#/updatePayeeInformation', NULL, 37, 1, '2018-09-26 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(43, 'Pre Authorization Request', '#/preAuthorizationRequest', NULL, 37, 1, '2018-09-27 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(45, 'Workstation', NULL, NULL, 44, 1, '2018-09-29 00:00:00.000', 'D', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(47, 'Allergies', '#/allergies', NULL, 45, 1, '2018-10-01 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(48, 'Map Of Bed', '#/bedManagement', NULL, 45, 1, '2018-10-02 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(49, 'Drug Administration', '#/drugAdministrator', NULL, 45, 1, '2018-10-03 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(50, 'Sample Collection', '#/sampleCollection', NULL, 45, 1, '2018-10-04 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(51, 'Nursing Orders', '#/nursingOrderView', NULL, 45, 1, '2018-10-05 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(52, 'ICU Booking', '#/icuBedBookingStatus', NULL, 45, 1, '2018-10-06 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(53, 'Pending Investigations', '#/pendingInvestigations', NULL, 45, 1, '2018-10-07 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(54, 'Homecare Medication Entry', '#/homecareMedicationEntry', NULL, 45, 1, '2018-10-08 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(754, 'Addendum Bill', NULL, NULL, 10, 1, '2018-10-25 12:52:33.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(756, 'Document Generator', NULL, NULL, NULL, 1, '2018-10-31 16:33:06.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(757, 'Casualty Worklist', NULL, NULL, 243, 1, '2018-11-23 12:38:01.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(10, 'Billing', NULL, NULL, 1, 1, '2018-08-25 00:00:00.000', 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(55, 'Patient Sales Return', '#/patientIssueReturn', NULL, 45, 1, '2018-10-09 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(56, 'Periodicity Service', '#/periodicityService', NULL, 45, 1, '2018-10-10 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(58, 'Issue Acceptance', '#/patientIssueAcceptance', NULL, 57, 1, '2018-10-12 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(59, 'Patient Indent', '#/patientIndent', NULL, 57, 1, '2018-10-13 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(60, 'Billable Consumption', '#/billableConsumption', NULL, 57, 1, '2018-10-14 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(61, 'Return Billable Consumption', '#/returnBillableConsumption', NULL, 57, 1, '2018-10-15 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(63, 'Non-Billable Consumption', '#/nonBillableConsumption', NULL, 62, 1, '2018-10-17 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(64, 'Accept Consumable', '#/consumableAcceptance', NULL, 62, 1, '2018-10-18 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(62, 'Inventory', NULL, NULL, 44, 1, '2018-10-16 00:00:00.000', 'D', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(11, 'OPD Bill', NULL, NULL, 10, 1, '2018-08-26 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(87, ' Crash Cart Request Approval', '#/crashCartRequest', NULL, 85, 1, '2018-11-10 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(88, 'CSSD', NULL, NULL, 44, 1, '2018-11-11 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(89, ' Kit Indent', '#/KitIndent', NULL, 88, 1, '2018-11-12 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(90, ' Kit Indent Approval', '#/KitIndentApproval', NULL, 88, 1, '2018-11-13 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(91, ' Kit Issue Acceptance', '#/KitIndentAcceptance', NULL, 88, 1, '2018-11-14 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(92, ' Kit Return', '#/KitReturn', NULL, 88, 1, '2018-11-15 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(93, 'Linen-Laundry', NULL, NULL, 44, 1, '2018-11-16 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(94, ' Linen Indent', '#/linenIndent', NULL, 93, 1, '2018-11-17 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(95, ' Linen Indent Approval', '#/linenIndentApproval', NULL, 93, 1, '2018-11-18 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(96, ' Linen Issue Acceptance', '#/linenIssueacceptance', NULL, 93, 1, '2018-11-19 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(97, ' Washing Request', '#/washingRequest', NULL, 93, 1, '2018-11-20 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(98, 'Charting', NULL, NULL, 44, 1, '2018-11-21 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(99, ' Input Output Charts', '#/inputOutputCharts', NULL, 98, 1, '2018-11-22 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(100, ' Input Output Chart History', '#/inputOutputChartHistory', NULL, 98, 1, '2018-11-23 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(101, 'Pharmacy', NULL, NULL, 0, 1, '2018-11-24 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(102, 'IP Pharmacy', NULL, NULL, 101, 1, '2018-11-25 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(103, ' Pharmacology Validation', '#/pharmacologyValidaionWorklist', NULL, 102, 1, '2018-11-26 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(104, ' Assign Picker', '#/assigningPickerDispenser', NULL, 102, 1, '2018-11-27 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(105, ' Pending Issues', '#/pendingIssues', NULL, 102, 1, '2018-11-28 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(106, ' Pending Handover WorkList', '#/pendingHandoverWorkList', NULL, 102, 1, '2018-11-29 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(107, ' Patient Issue Rejection', '#/patientIssueRejection', NULL, 102, 1, '2018-11-30 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(108, ' Rejected Rejection', '#/pendingRejectedRejections', NULL, 102, 1, '2018-12-01 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(109, ' Return Acceptance', '#/patientReturnAcceptance', NULL, 102, 1, '2018-12-02 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(110, 'OP Pharmacy', NULL, NULL, 101, 1, '2018-12-03 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(111, ' Sales Worklist', '#/outPatientSalesWorklist', NULL, 110, 1, '2018-12-04 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(112, ' Sales Return', '#/billSalesReturn', NULL, 110, 1, '2018-12-05 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(113, ' Bill List', '#/pharmacyBillList', NULL, 110, 1, '2018-12-06 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(114, ' Walk in Sales', '#/walkInSales', NULL, 110, 1, '2018-12-07 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(115, 'Narcotics Store', NULL, NULL, 101, 1, '2018-12-08 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(116, ' Issue Worklist', '#/narcoticsIssueWorklist', NULL, 115, 1, '2018-12-09 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(117, ' Delivery Worklist', '#/deliveryWokrlist', NULL, 115, 1, '2018-12-10 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(118, ' Empty Ampoules', '#/acceptanceOfEmptyAmpoules', NULL, 115, 1, '2018-12-11 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(119, 'IN Patient Pharmacy', NULL, NULL, 101, 1, '2018-12-12 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(120, ' Patient Consumbale Issue', 'patientConsumableIssue', NULL, 119, 1, '2018-12-13 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(121, 'Inventory', NULL, NULL, 0, 1, '2018-12-14 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(122, ' Nursing', 'No Link', NULL, 121, 1, '2018-12-15 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(123, 'Store', NULL, NULL, 121, 1, '2018-12-16 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(124, ' Store Indent', '#/storeIndenting', NULL, 123, 1, '2018-12-17 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(125, ' Store Indent Approval', '#/storeIndentApproval', NULL, 123, 1, '2018-12-18 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(126, ' Pending Store Indents', '#/pendingStoreIndents', NULL, 123, 1, '2018-12-19 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(127, ' Material Issue', '#/materialIssue/pendingIssue', NULL, 123, 1, '2018-12-20 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(128, ' Material Issue Acceptance', '#/materialIssueAcceptance', NULL, 123, 1, '2018-12-21 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(129, ' Material Return', '#/materialReturn', NULL, 123, 1, '2018-12-22 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(130, ' Material Return Acceptance', '#/materialReturnAcceptance', NULL, 123, 1, '2018-12-23 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(131, ' Rejected Material Acceptance', '#/rejectedMaterialAcceptance', NULL, 123, 1, '2018-12-24 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(132, ' Material Picker', '#/materialPicker', NULL, 123, 1, '2018-12-25 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(133, ' Waste Store', '#/wasteStore', NULL, 123, 1, '2018-12-26 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(134, ' Returnable Gatepass Approval', '#/returnaleGatepassApproval', NULL, 123, 1, '2018-12-27 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(135, ' Store Stock Dashboard', '#/storeStockDashboard', NULL, 123, 1, '2018-12-28 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(136, ' Pending For Shelf Allocation', '#/pendingForShelfAllocation', NULL, 123, 1, '2018-12-29 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(137, ' Batch Exp Date Change', '#/batchExpDateChange', NULL, 123, 1, '2018-12-30 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(138, ' Batch Exp Date Change Approval', '#/batchExpDateChangeApproval', NULL, 123, 1, '2018-12-31 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(139, ' Drug Recall', '#/drugRecall', NULL, 123, 1, '2019-01-01 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(140, ' Recall Drugs', '#/recallDrugs', NULL, 123, 1, '2019-01-02 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(141, ' Material Inwards', '#/materialInwards', NULL, 123, 1, '2019-01-03 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(142, ' Material Inwards Approval', '#/materialInwardsApproval', NULL, 123, 1, '2019-01-04 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(143, ' Assest Support Status', '#/assetsSupportStatus', NULL, 123, 1, '2019-01-05 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(144, ' Returnable Gatepass', '#/returnableGatepass', NULL, 123, 1, '2019-01-06 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(145, 'Stock Adjustments', NULL, NULL, 121, 1, '2019-01-07 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(146, ' Stop Transaction', '#/stopTransaction', NULL, 145, 1, '2019-01-08 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(147, ' Upload Stock', '#/uploadStock', NULL, 145, 1, '2019-01-09 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(148, ' Upload Stock Approval', '#/uploadStockApproval', NULL, 145, 1, '2019-01-10 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(149, ' Rejected Upload', '#/rejectedUploaded', NULL, 145, 1, '2019-01-11 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(150, ' Compute Stock', '#/computeStock', NULL, 145, 1, '2019-01-12 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(151, ' Variance Approval', '#/varianceApproval', NULL, 145, 1, '2019-01-13 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(152, ' Confirm Stock', '#/confirmStock', NULL, 145, 1, '2019-01-14 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(153, 'Stock', NULL, NULL, 121, 1, '2019-01-15 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(154, ' Opening balance', '#/openingBalance', NULL, 153, 1, '2019-01-16 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(155, 'Scrap', NULL, NULL, 121, 1, '2019-01-17 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(156, ' Scrap Request', '#/scrapRequest', NULL, 155, 1, '2019-01-18 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(157, ' Scrap Request Approval', '#/approvalScrapRequest', NULL, 155, 1, '2019-01-19 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(158, ' Request For Assest Disposal Worklist', '#/requestForAssetDisposalWorklist', NULL, 155, 1, '2019-01-20 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(159, ' Waste Assest Disposal Worklist', '#/wasteAssetDisposalWorklist', NULL, 155, 1, '2019-01-21 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(160, ' Witness Approval', '#/witnessApproval', NULL, 155, 1, '2019-01-22 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(161, 'Procurement', NULL, NULL, 0, 1, '2019-01-23 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(162, 'Purchase', NULL, NULL, 161, 1, '2019-01-24 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(163, ' Item Request', '#/itemRequest', NULL, 162, 1, '2019-01-25 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(164, ' Item Approval', '#/itemApproval', NULL, 162, 1, '2019-01-26 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(165, ' Item Creation Pending List', '#/itemCreationPendingList', NULL, 162, 1, '2019-01-27 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(166, ' Physical Inventory', '#/physicalInventory', NULL, 162, 1, '2019-01-28 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(167, ' GRN Against PO', '#/GRNAgainstPO', NULL, 162, 1, '2019-01-29 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(168, ' Purchase Request', '#/purchaseRequest', NULL, 162, 1, '2019-01-30 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(169, ' Purchase Request Approval', '#/purchaseRequestApproval', NULL, 162, 1, '2019-01-31 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(170, ' Local Purchase Request', '#/localPurchhaseRequest', NULL, 162, 1, '2019-02-01 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(171, ' Local Purchase Request Approval', '#/localPurchhaseRequestApproval', NULL, 162, 1, '2019-02-02 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(174, ' Quotation Enquiry Approval', '#/quotationEnquiryApproval', NULL, 172, 1, '2019-02-05 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(175, ' Quotation Fill In', '#/quotationFillIn', NULL, 172, 1, '2019-02-06 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(176, ' Technical Approval', '#/technicalApproval', NULL, 172, 1, '2019-02-07 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(177, ' Vendor Negotitation', '#/vendorNegotitation', NULL, 172, 1, '2019-02-08 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(178, ' Vendor Selection', '#/vendorSelection', NULL, 172, 1, '2019-02-09 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(179, ' PO Creation', '#/POCreation', NULL, 172, 1, '2019-02-10 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(180, ' PO Approval', '#/POApproval', NULL, 172, 1, '2019-02-11 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(181, ' GRN', '#/GRN', NULL, 172, 1, '2019-02-12 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(182, ' GRN Approval', '#/GRNApproval', NULL, 172, 1, '2019-02-13 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(183, ' Barcode Printing', '#/barcodePrinting', NULL, 172, 1, '2019-02-14 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(184, ' Physical Inspection', '#/physicalInspec', NULL, 172, 1, '2019-02-15 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(185, ' Enquiry Sent to Vendor', '#/enquirySentToVendor', NULL, 172, 1, '2019-02-16 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(186, 'Work Order', NULL, NULL, 161, 1, '2019-02-17 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(187, ' Workorder Request', '#/workorderRequest', NULL, 186, 1, '2019-02-18 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(188, ' Quotation Send To Vendor', '#/quotationSendToVendor', NULL, 186, 1, '2019-02-19 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(189, ' Approval Worklist', '#/workorderApprovalWorklist', NULL, 186, 1, '2019-02-20 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(190, ' Quotation Request', '#/workorderQuotationRequest', NULL, 186, 1, '2019-02-21 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(191, ' Quotation Entry', '#/workorderQuotationEntry', NULL, 186, 1, '2019-02-22 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(192, ' Quotation Negotiation', '#/workorderQuotationNegotiation', NULL, 186, 1, '2019-02-23 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(193, ' Quotation Selection', '#/workorderQuotationSelect', NULL, 186, 1, '2019-02-24 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(194, ' Workorder Creation', '#/workorderCreation', NULL, 186, 1, '2019-02-25 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(195, 'Consignment', NULL, NULL, 161, 1, '2019-02-26 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(196, ' Consignment Inward', '#/consignmentInward', NULL, 195, 1, '2019-02-27 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(197, ' Consignment Outward', '#/consignmentOutward', NULL, 195, 1, '2019-02-28 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(198, 'Loan In Out', NULL, NULL, 161, 1, '2019-03-01 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(199, ' Loan In', '#/loanIn', NULL, 198, 1, '2019-03-02 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(200, ' Loan Out', '#/loanOut', NULL, 198, 1, '2019-03-03 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(201, ' Loan In Out', '#/loanInOut', NULL, 198, 1, '2019-03-04 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(202, ' Loan Out Receive', '#/loanOutReceive', NULL, 198, 1, '2019-03-05 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(203, 'Asset Management', NULL, NULL, 0, 1, '2019-03-06 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(204, ' Indent', NULL, NULL, 203, 1, '2019-03-07 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(205, ' Asset Indent', '#/assetIndent', NULL, 204, 1, '2019-03-08 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(206, ' Asset Indent Approval', '#/assetIndentApproval', NULL, 204, 1, '2019-03-09 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(207, ' Asset Issue', '#/assetIssueIndent', NULL, 204, 1, '2019-03-10 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(208, ' Auction Requisition', '#/auctionRequistion', NULL, 203, 1, '2019-03-11 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(209, ' Auction Requisition Approval', '#/auctionRequisitionApproval', NULL, 203, 1, '2019-03-12 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(210, ' Items For Auction', '#/itemsForAuction', NULL, 203, 1, '2019-03-13 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(211, ' Auction Vendor Approval', '#/auctionVendorApproval', NULL, 203, 1, '2019-03-14 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(212, ' Non Returnable GatePass', '#/nonReturnableGatePass', NULL, 203, 1, '2019-03-15 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(213, ' Asset PMS Scheduler', '#/assetPMSScheduler', NULL, 203, 1, '2019-03-16 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(214, ' Asset Issue', '#/assetIssueMaster', NULL, 203, 1, '2019-03-17 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(215, ' Asset Issue Acceptance', '#/assetIssueAcceptance', NULL, 203, 1, '2019-03-18 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(216, ' Asset Return', '#/assetReturn', NULL, 203, 1, '2019-03-19 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(217, ' Asset Return Acceptance', '#/assetReturnAcceptance', NULL, 203, 1, '2019-03-20 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(218, ' Rejected Asset Acceptance', '#/rejectedAssetAcceptance', NULL, 203, 1, '2019-03-21 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(219, 'Fixed Asset Opening Balance', NULL, NULL, 203, 1, '2019-03-22 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(220, ' Add Fixed Asset Master', '#/addfixedAssetMaster', NULL, 219, 1, '2019-03-23 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(221, 'Equipment Management', NULL, NULL, 0, 1, '2019-03-24 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(222, ' Preventive Maintenance', NULL, NULL, 221, 1, '2019-03-25 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(223, ' Asset PMS Scheduler', '#/assetPMSScheduler', NULL, 222, 1, '2019-03-26 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(224, ' PM Request', '#/PMRequest', NULL, 222, 1, '2019-03-27 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(225, ' PM Schedule', '#/PMSchedule', NULL, 222, 1, '2019-03-28 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(226, ' Schedule Worklist', '#/scheduleWorklist', NULL, 222, 1, '2019-03-29 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(227, 'Corrective Maintenance', NULL, NULL, 221, 1, '2019-03-30 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(228, ' Corrective Maintenance', '#/correctiveMaintenance', NULL, 227, 1, '2019-03-31 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(229, ' Corrective Maintenance Worklist', '#/correctiveMaintenanceWorklist', NULL, 227, 1, '2019-04-01 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(230, ' Corrective Maintenance Schedule Worklist', '#/correctiveMaintenanceScheduleWorklist', NULL, 227, 1, '2019-04-02 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(231, 'CSSD', NULL, NULL, 0, 1, '2019-04-03 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(232, 'Pre-Sterilization Process', NULL, NULL, 231, 1, '2019-04-04 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(234, ' Pending for Inspection', '#/preSterilizationInspection', NULL, 232, 1, '2019-04-06 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(235, ' Pending for Dis-Infection', '#/preSterilizationDisInfection', NULL, 232, 1, '2019-04-07 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(550, ' Room Master', '#/roomMaster', NULL, 541, 1, '2020-02-16 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(236, ' Pending for Kit Formation', '#/preSterilizationKitFormation', NULL, 232, 1, '2019-04-08 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(237, 'Sterilization Process', NULL, NULL, 231, 1, '2019-04-09 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(238, ' Load Batch', '#/sterilizationLoadBatch', NULL, 237, 1, '2019-04-10 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(239, ' Pending for Indicators', '#/sterilizationPendingIndicators', NULL, 237, 1, '2019-04-11 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(240, 'Send to Microbiology', '#/sendtoMicrobiology', NULL, 231, 1, '2019-04-12 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(241, ' Kit Issue', '#/kitIssue', NULL, 231, 1, '2019-04-13 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(242, ' Application', '#/application', NULL, 231, 1, '2019-04-14 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(243, 'Clinical Workstation', NULL, NULL, 0, 1, '2019-04-15 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(244, ' History', NULL, NULL, 243, 1, '2019-04-16 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(245, ' Allergies', '#/allergies', NULL, 244, 1, '2019-04-17 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(246, ' Immunization History', '#/immuniztionHistory', NULL, 244, 1, '2019-04-18 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(247, 'Order', NULL, NULL, 243, 1, '2019-04-19 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(248, ' Nursing Order', '#/nursingOrder', NULL, 247, 1, '2019-04-20 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(249, ' Consult Order', '#/interconsultation', NULL, 247, 1, '2019-04-21 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(250, ' Pending Consult Order', '#/pendingConsultOrderRequest', NULL, 247, 1, '2019-04-22 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(251, ' Blood Request', '#/bloodRequest', NULL, 247, 1, '2019-04-23 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(252, ' Homecare Medication Approval', '#/homecareMedicationApproval', NULL, 247, 1, '2019-04-24 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(253, ' Crashcart Medications Approvel', '#/administrationApproval', NULL, 247, 1, '2019-04-25 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(254, ' Second Opinion Request', '#/secondOpinionRequest', NULL, 247, 1, '2019-04-26 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(255, 'Transfer', NULL, NULL, 243, 1, '2019-04-27 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(256, ' Transfer Request Acceptance', '#/transferRequestAcceptance', NULL, 255, 1, '2019-04-28 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(257, ' Initiate Transfer Of Care', '#/intiateTransferCare', NULL, 255, 1, '2019-04-29 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(258, ' Accept Transfer Of Care', '#/acceptanceOfTransferOfCare', NULL, 255, 1, '2019-04-30 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(259, ' Pending Transfer', '#/pendingTransferRequest', NULL, 255, 1, '2019-05-01 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(260, ' Transfer To ICU', '#/transferToICU', NULL, 255, 1, '2019-05-02 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(261, 'Discharge', NULL, NULL, 243, 1, '2019-05-03 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(262, ' Due For Discharge', '#/dueForDischarge', NULL, 261, 1, '2019-05-04 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(263, ' Cancel Discharge', '#/cancelForDischarge', NULL, 261, 1, '2019-05-05 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(264, ' Pending Short Leave', '#/pendingShortLeave', NULL, 261, 1, '2019-05-06 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(265, 'New Born', NULL, NULL, 243, 1, '2019-05-07 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(266, ' Registration', '#/newBornRegistration', NULL, 265, 1, '2019-05-08 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(267, ' List', '#/newBornList', NULL, 265, 1, '2019-05-09 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(268, ' Bed Allocation', '#/cradleBedAllocation', NULL, 265, 1, '2019-05-10 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(269, ' Progress Note', '#/progressNote', NULL, 243, 1, '2019-05-11 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(270, ' Radiology Workbench', '#/radiologyWorkbench', NULL, 0, 1, '2019-05-12 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(271, ' Physiotherapy Workbench', '#/physiotherapyWorkbench', NULL, 0, 1, '2019-05-13 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(272, 'LIS', NULL, NULL, 0, 1, '2019-05-14 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(273, ' Phlebotomy', NULL, NULL, 272, 1, '2019-05-15 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(274, ' Worklist', '#/phlebotomyWorkList', NULL, 273, 1, '2019-05-16 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(275, ' Send TO CR', '#/sendToCR', NULL, 273, 1, '2019-05-17 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(276, ' Patient Arrival', '#/patientArrival', NULL, 273, 1, '2019-05-18 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(277, ' Repeat Sample', '#/patientArrivalForRepeatSampleCollection', NULL, 273, 1, '2019-05-19 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(278, ' Sample Recollection', '#/sampleRecollection', NULL, 273, 1, '2019-05-20 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(279, 'Central Receiving', NULL, NULL, 272, 1, '2019-05-21 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(280, ' Centrifugation List', '#/centrifugationWorkList', NULL, 279, 1, '2019-05-22 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(281, ' OP Samples', '#/outPatient', NULL, 279, 1, '2019-05-23 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(282, ' IP Samples', '#/inPatient', NULL, 279, 1, '2019-05-24 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(283, 'Biochemistry', NULL, NULL, 272, 1, '2019-05-25 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(284, ' Pending Acceptance', '#/pendingListForAcceptance', NULL, 283, 1, '2019-05-26 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(285, ' Worklist', '#/worklistForDepartment/', NULL, 283, 1, '2019-05-27 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(286, ' Report Entry', '#/worklistForReportEntry', NULL, 283, 1, '2019-05-28 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(287, ' Validation', '#/worklistForValidation', NULL, 283, 1, '2019-05-29 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(288, ' Report Release', '#/worklistForReportRelease', NULL, 283, 1, '2019-05-30 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(289, 'Hematology', NULL, NULL, 272, 1, '2019-05-31 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(290, ' Pending Acceptance', '#/pendingListForAcceptance', NULL, 289, 1, '2019-06-01 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(291, ' Worklist', '#/worklistForDepartment', NULL, 289, 1, '2019-06-02 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(292, ' Report Entry', '#/worklistForReportEntry', NULL, 289, 1, '2019-06-03 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(293, ' Validation', '#/worklistForValidation', NULL, 289, 1, '2019-06-04 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(294, ' Report Release', '#/worklistForReportRelease', NULL, 289, 1, '2019-06-05 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(295, 'Histopathology', NULL, NULL, 272, 1, '2019-06-06 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(296, ' Specimen Receipt', '#/specimenReceipt', NULL, 295, 1, '2019-06-07 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(297, ' Macroscopic Examination', '#/microscopeExamination', NULL, 295, 1, '2019-06-08 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(298, ' Block Creation', '#/blockCreation', NULL, 295, 1, '2019-06-09 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(299, ' Slide Creation', '#/slideCreation', NULL, 295, 1, '2019-06-10 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(300, ' Microscopic Examination', '#/microscopicExaminiationWorkList', NULL, 295, 1, '2019-06-11 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(301, ' Restain Request Worklist', '#/restainingRequestWorklist', NULL, 295, 1, '2019-06-12 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(302, ' Addendum Report', '#/addendumReport', NULL, 295, 1, '2019-06-13 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(303, ' Storage', '#/storage', NULL, 295, 1, '2019-06-14 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(304, ' Second Opinion Request', '#/secondOpinionRequest', NULL, 295, 1, '2019-06-15 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(305, ' Slide For Second Opinion', '#/pendingSlideForSecondOpinion', NULL, 295, 1, '2019-06-16 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(306, ' Frozen Section Request List', '#/frozenSectionRequestList', NULL, 295, 1, '2019-06-17 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(307, 'Microbiology', NULL, NULL, 272, 1, '2019-06-18 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(308, ' Sample Acceptance', '#/sampleAcceptance', NULL, 307, 1, '2019-06-19 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(309, ' WorkList', '#/sampleProcessingWorkList', NULL, 307, 1, '2019-06-20 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(310, ' Incubation Master', '#/IncubationMaster', NULL, 307, 1, '2019-06-21 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(311, ' Macroscopic Examination', '#/macroscopicExaminationMaster', NULL, 307, 1, '2019-06-22 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(312, ' Biochemical Testing', '#/biochemicalTesting', NULL, 307, 1, '2019-06-23 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(313, ' Microscopic Examination', '#/microscopicExamination', NULL, 307, 1, '2019-06-24 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(314, ' Sensitivity Testing', '#/sensitivityTestingProcess', NULL, 307, 1, '2019-06-25 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(315, ' Report Handover', '#/worklistForPrinting', NULL, 272, 1, '2019-06-26 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(316, ' Out Source', NULL, NULL, 272, 1, '2019-06-27 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(317, ' Out Source Pending List', '#/outSourcedPendinglistAssignment', NULL, 316, 1, '2019-06-28 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(318, ' Pending List For Sample Dispatch', '#/pendinglistForSampleDispatch', NULL, 316, 1, '2019-06-29 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(319, ' Pending List For Pending Upload', '#/pendinglistForReportUpload', NULL, 316, 1, '2019-06-30 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(320, ' Workload Dashboard', '#/workloadDashboard', NULL, 272, 1, '2019-07-01 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(321, 'Theatre Management', NULL, NULL, 0, 1, '2019-07-02 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(322, 'OT Completion', '#/OTCompletion', NULL, 321, 1, '2019-07-03 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(323, 'OT Billing Completion', '#/surgeryNote', NULL, 321, 1, '2019-07-04 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(324, 'Surgery Note', '#/surgeryNote', NULL, 321, 1, '2019-07-05 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(325, 'Anesthesia Note', '#/anesthesiaNote', NULL, 321, 1, '2019-07-06 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(326, 'Implant Indent', '#/implantIndent', NULL, 321, 1, '2019-07-07 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(327, 'Implant Indent Acceptance', '#/implantIndentAcceptance', NULL, 321, 1, '2019-07-08 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(328, 'Emergency Room', NULL, NULL, 0, 1, '2019-07-09 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(329, ' ER-Encounter', '#/unknownPatientReg/personalForUnknown', NULL, 328, 1, '2019-07-10 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(330, ' Triagework List', '#/bedTypeAllocation', NULL, 328, 1, '2019-07-11 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(331, ' ER Patient List', '#/erPatientList', NULL, 328, 1, '2019-07-12 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(332, ' Map Of Bed', '#/mapOfBedForER', NULL, 328, 1, '2019-07-13 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(333, ' Due For Discharge', '#/ERdueForDischarge', NULL, 328, 1, '2019-07-14 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(334, ' Ready For Billing', '#/ERreadyForBilling', NULL, 328, 1, '2019-07-15 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(335, ' Final Discharge', '#/ERfinalDischarge', NULL, 328, 1, '2019-07-16 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(336, ' Admission Note', '#/admissionRequest', NULL, 328, 1, '2019-07-17 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(337, 'Diet & Kitchen', NULL, NULL, 0, 1, '2019-07-18 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(338, ' Diet Planning', '#/dietPatientRequest', NULL, 337, 1, '2019-07-19 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(339, ' Diet Order', '#/dietOrder', NULL, 337, 1, '2019-07-20 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(340, ' Diet Issue', '#/dietIssue', NULL, 337, 1, '2019-07-21 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(341, ' Diet Acceptance', '#/dietAcceptance', NULL, 337, 1, '2019-07-22 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(342, ' Rejected Diet List', '#/rejectedDietList', NULL, 337, 1, '2019-07-23 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(343, 'Linen-Laundry', NULL, NULL, 0, 1, '2019-07-24 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(344, ' Linen Issue', '#/linenIssue', NULL, 343, 1, '2019-07-25 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(345, ' Linen Return', '#/linenReturn', NULL, 343, 1, '2019-07-26 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(346, ' Linen Return Acceptance', '#/linenReturnAcceptance', NULL, 343, 1, '2019-07-27 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(347, ' Rejected Linen Acceptance', '#/rejectedLinenAcceptance', NULL, 343, 1, '2019-07-28 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(348, ' Send To Condemnation', '#/sendToCondemnation', NULL, 343, 1, '2019-07-29 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(349, ' Acceptance of Condemnation', '#/acceptanceOfCondemnation', NULL, 343, 1, '2019-07-30 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(350, ' Convert of Condemnation Stock', '#/convertOfCondemnationStock', NULL, 343, 1, '2019-07-31 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(351, ' Issue To Staff', '#/issueToStaff', NULL, 343, 1, '2019-08-01 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(352, ' Staff Exchange Linen', '#/staffExchangeLinen', NULL, 343, 1, '2019-08-02 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(353, ' Staff Exchange Linen Issue', '#/staffExchangelinenIssue', NULL, 343, 1, '2019-08-03 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(354, ' Washing Request', '#/washingRequest', NULL, 343, 1, '2019-08-04 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(355, ' Acceptance Washing Request', '#/acceptanceWashingRequest', NULL, 343, 1, '2019-08-05 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(356, ' Issue Dirty Linen', '#/issueDirtyLinen', NULL, 343, 1, '2019-08-06 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(357, ' Acceptance Of Dirty Linen', '#/acceptanceOfDirtyLinen', NULL, 343, 1, '2019-08-07 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(358, ' Issue Washed Linen', '#/issueWashedLinen', NULL, 343, 1, '2019-08-08 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(359, 'House Keeping', NULL, NULL, 0, 1, '2019-08-09 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(360, ' Worklist', '#/houseKeeping', NULL, 359, 1, '2019-08-10 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(361, ' Under Maintenance', '#/bedUnderMaintenance', NULL, 359, 1, '2019-08-11 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(362, 'Mortuary', NULL, NULL, 0, 1, '2019-08-12 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(363, ' Death Registration', '#/deathRegistration', NULL, 362, 1, '2019-08-13 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(364, ' Pending List', '#/morguePendinglist', NULL, 362, 1, '2019-08-14 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(365, ' Mortuary Availability', '#/morgueAvailability', NULL, 362, 1, '2019-08-15 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(366, ' Mortuary Reservation', '#/morgueReservation', NULL, 362, 1, '2019-08-16 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(367, ' Release Mortuary', '#/morgueReleased', NULL, 362, 1, '2019-08-17 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(368, ' Mortuary Bed Management', '#/mortuaryBedManagement', NULL, 362, 1, '2019-08-18 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(369, 'Global Master', NULL, NULL, 0, 1, '2019-08-19 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(370, ' Organization Structure', NULL, NULL, 369, 1, '2019-08-20 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(371, ' Hospital Master', '#/hospitalMaster', NULL, 370, 1, '2019-08-21 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(372, ' Department Master', '#/specialityMaster', NULL, 370, 1, '2019-08-22 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(373, ' Sub Department Master', '#/subSpecialityMaster', NULL, 370, 1, '2019-08-23 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(375, ' Holiday Calendar Master', '#/holidayCalenderMaster', NULL, 370, 1, '2019-08-25 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(376, ' Service Master', '#/serviceMaster', NULL, 370, 1, '2019-08-26 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(377, ' Organization Master', '#/organizationMaster', NULL, 370, 1, '2019-08-27 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(378, ' Document Type Master', '#/documentTypeMaster', NULL, 370, 1, '2019-08-28 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(379, ' License Type Master', '#/licenseTypeMaster', NULL, 370, 1, '2019-08-29 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(380, 'Patient Registration', NULL, NULL, 369, 1, '2019-08-30 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(381, ' Nationality Master', '#/nationalityMaster', NULL, 380, 1, '2019-08-31 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(382, ' Race Master', '#/raceMaster', NULL, 380, 1, '2019-09-01 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(383, ' Country Master', '#/countryMaster', NULL, 380, 1, '2019-09-02 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(384, ' State Master', '#/stateMaster', NULL, 380, 1, '2019-09-03 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(385, ' District Master', '#/districtMaster', NULL, 380, 1, '2019-09-04 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(386, ' City Master', '#/cityMaster', NULL, 380, 1, '2019-09-05 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(387, ' Area Master', '#/areaMaster', NULL, 380, 1, '2019-09-06 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(388, ' Identification Card', '#/identificationMaster', NULL, 380, 1, '2019-09-07 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(389, ' Sex Master', '#/commonMaster/sexmaster', NULL, 380, 1, '2019-09-08 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(390, ' Relation Master', '#/commonMaster/relationmaster', NULL, 380, 1, '2019-09-09 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(391, ' Marital Status Master', '#/commonMaster/maritalstatus', NULL, 380, 1, '2019-09-10 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(392, ' Religion Master', '#/commonMaster/religion', NULL, 380, 1, '2019-09-11 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(393, ' Occupation Master', '#/commonMaster/occupation', NULL, 380, 1, '2019-09-12 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(394, ' Patient Source Master', '#/commonMaster/patientsource', NULL, 380, 1, '2019-09-13 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(395, ' Referral Type Master', '#/commonMaster/referraltype', NULL, 380, 1, '2019-09-14 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(396, ' Referral Master', '#/referralMaster', NULL, 380, 1, '2019-09-15 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(397, ' Encounter Type Master', '#/encounterTypeMaster', NULL, 380, 1, '2019-09-16 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(398, ' Prefix Master', '#/prefixMaster', NULL, 380, 1, '2019-09-17 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(399, ' Consent Type Master', '#/consentTypeMaster', NULL, 380, 1, '2019-09-18 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(400, ' Consent Master', '#/consentMaster', NULL, 380, 1, '2019-09-19 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(402, ' Bed Type Master', '#/bedTypeMaster', NULL, 401, 1, '2019-09-21 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(403, ' Admission Type', '#/admissionTypeMaster', NULL, 401, 1, '2019-09-22 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(404, ' Occupancy Unit Type', '#/occupancyUnitMaster', NULL, 401, 1, '2019-09-23 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(405, ' Reason For Discharge Master', '#/dischargeTypeMaster', NULL, 401, 1, '2019-09-24 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(406, ' Short Leave Reason', '#/shortLeaveReasonMaster', NULL, 401, 1, '2019-09-25 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(412, ' Sample Master', '#/commonMaster/sampleMaster', NULL, 411, 1, '2019-10-01 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(413, ' Container Master', '#/commonMaster/containerMaster', NULL, 411, 1, '2019-10-02 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(414, ' Techinque Master', '#/commonMaster/techinqueMaster', NULL, 411, 1, '2019-10-03 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(415, ' Prerequisites Master', '#/commonMaster/prerequisitesMaster', NULL, 411, 1, '2019-10-04 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(416, ' Age Group Master', '#/ageGroupMaster', NULL, 411, 1, '2019-10-05 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(417, ' Unit Master', '#/commonMaster/unitMaster', NULL, 411, 1, '2019-10-06 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(418, ' Textual Reference Range', '#/textualReferenceRangeMaster', NULL, 411, 1, '2019-10-07 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(419, ' Reagent Master', '#/commonMaster/reagentMaster', NULL, 411, 1, '2019-10-08 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(420, ' Report Type Master', '#/commonMaster/reportTypeMaster', NULL, 411, 1, '2019-10-09 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(421, ' Bacteria Shape', '#/commonMaster/bacteriaShapMaster', NULL, 411, 1, '2019-10-10 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(422, ' Incubation Master', '#/incubationPeriodMaster', NULL, 411, 1, '2019-10-11 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(423, ' Wet Film Study Master', '#/commonMaster/wtifilmStudyMasterMaster', NULL, 411, 1, '2019-10-12 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(424, ' Media Master', '#/commonMaster/mediaMaster', NULL, 411, 1, '2019-10-13 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(425, ' Incubation Test Master', '#/commonMaster/incubationTestMaster', NULL, 411, 1, '2019-10-14 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(426, ' Sample Type Media Mapping Master', '#/sampleTypeMediaMappingMaster', NULL, 411, 1, '2019-10-15 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(428, ' Bacteria Classification', '#/commonMaster/bacteriaClassificationMaster', NULL, 411, 1, '2019-10-17 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(429, ' Antibiotic', '#/commonMaster/antibioticsMaster', NULL, 411, 1, '2019-10-18 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(430, ' Antibiotics Class', '#/commonMaster/antibioticMaster', NULL, 411, 1, '2019-10-19 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(431, ' Antibiotics Group', '#/commonMaster/antibioticsGroupMaster', NULL, 411, 1, '2019-10-20 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(432, ' Addition of Antibiotics Against Antibiotic Class', '#/additionofAntibiotic', NULL, 411, 1, '2019-10-21 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(433, ' Addition of Antibiotics Group Master', '#/antibioticAdditionGroupMaster', NULL, 411, 1, '2019-10-22 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(434, ' Colony Test With Media Master', '#/colonyTestWithMediaMaster', NULL, 411, 1, '2019-10-23 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(435, ' Organism Master', '#/commonMaster/organismMaster', NULL, 411, 1, '2019-10-24 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(436, ' Organism Group Master', '#/commonMaster/organismGroupMaster', NULL, 411, 1, '2019-10-25 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(437, ' Stainig Master', '#/commonMaster/stainigMaster', NULL, 411, 1, '2019-10-26 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(438, ' Sensitivity Test Result', '#/commonMaster/sensitivityTypeResultMaster', NULL, 411, 1, '2019-10-27 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(439, ' Block Master', '#/commonMaster/blockMaster', NULL, 411, 1, '2019-10-28 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(440, ' Slide Procedure Master', '#/commonMaster/slideProcedureMaster', NULL, 411, 1, '2019-10-29 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(441, ' Speciman Mapping With Block & Slide Procedure', '#/specimanMapping', NULL, 411, 1, '2019-10-30 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(442, ' Organism Group Mapping With Organism', '#/organismMapping', NULL, 411, 1, '2019-10-31 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(443, ' Mapping of Micro-organism', '#/mappingofMicroorganism', NULL, 411, 1, '2019-11-01 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(444, ' Speciman Master', '#/specimanMAster', NULL, 411, 1, '2019-11-02 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(445, 'Billing', NULL, NULL, 369, 1, '2019-11-03 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(446, ' Patient Category', '#/patientCategory', NULL, 445, 1, '2019-11-04 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(447, ' Visit Type Master', '#/commonMaster/visitType', NULL, 445, 1, '2019-11-05 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(448, ' Mode Of Payment', '#/commonMaster/paymentMode', NULL, 445, 1, '2019-11-06 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(449, ' Payment Entitlement', '#/commonMaster/paymentEntitment', NULL, 445, 1, '2019-11-07 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(450, ' Consultant Type', '#/commonMaster/consultantType', NULL, 445, 1, '2019-11-08 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(451, 'Currency Master', '#/currencyMaster', NULL, 445, 1, '2019-11-09 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(452, 'Currency Denomination', '#/currencydenominatio', NULL, 445, 1, '2019-11-10 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(453, ' Doctor Category', '#/doctorCategory', NULL, 445, 1, '2019-11-11 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(454, ' Employee Category', '#/employeeCategory', NULL, 445, 1, '2019-11-12 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(455, ' Bank Branch', '#/bankBranch', NULL, 445, 1, '2019-11-13 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(456, ' Bank Master', '#/bankMaster', NULL, 445, 1, '2019-11-14 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(457, 'Pharmacy', NULL, NULL, 369, 1, '2019-11-15 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(458, ' Product Category', '#/pharmacyproductCategoryMaster', NULL, 457, 1, '2019-11-16 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(459, ' Formulation Type', '#/pharmacyformulationTypeMaster', NULL, 457, 1, '2019-11-17 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(460, ' Schedule Master', '#/commonMaster/', NULL, 457, 1, '2019-11-18 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(461, ' Manufacturer Master', '#/manufacturerMaster', NULL, 457, 1, '2019-11-19 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(462, ' Supply Days Master', '#/supplyDaysMaster', NULL, 457, 1, '2019-11-20 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(463, ' Generic Master', '#/genericMaster', NULL, 457, 1, '2019-11-21 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(464, ' Item Master', '#/itemMasterGlobal', NULL, 457, 1, '2019-11-22 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(465, ' Package Master', '#/packageMaster', NULL, 457, 1, '2019-11-23 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(466, ' Unit Of Measurement', '#/unitofMeasurement', NULL, 457, 1, '2019-11-24 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(467, ' Unit Master', '#/pharmacyunitMaster', NULL, 457, 1, '2019-11-25 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(468, ' Strength Unit Master', '#/pharmacyStrengthUnitMaster', NULL, 457, 1, '2019-11-26 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(410, 'Day wise Rota Creation', '#/daywiseRotaCreation', NULL, 369, 1, '2019-09-29 00:00:00.000', 'D', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(407, 'Nursing', NULL, NULL, 531, 1, '2019-09-26 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(469, ' Dispensing Master', '#/dispensingMaster', NULL, 457, 1, '2019-11-27 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(470, ' Storage Unit Master', '#/storageunitMaster', NULL, 457, 1, '2019-11-28 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(471, ' Kit Master', '#/kitMaster', NULL, 457, 1, '2019-11-29 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(472, ' Combination Medication', '#/combinationMedicationMaster', NULL, 457, 1, '2019-11-30 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(473, ' Tax Component Master', '#/taxComponentMaster', NULL, 457, 1, '2019-12-01 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(474, ' Tax Master', '#/taxMaster', NULL, 457, 1, '2019-12-02 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(475, ' Other Charges Master', '#/otherCharges', NULL, 457, 1, '2019-12-03 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(477, ' Route of Administration', '#/routeAdministration', NULL, 476, 1, '2019-12-05 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(478, ' Frequency Master', '#/frequencyMaster', NULL, 476, 1, '2019-12-06 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(479, ' Formulation Type & ROA Master', '#/formulationTypeROAMaster', NULL, 476, 1, '2019-12-07 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(480, 'Nursing Order Master', '#/nursingOrderMaster', NULL, 476, 1, '2019-12-08 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(481, 'Blood Component', '#/bloodComponent', NULL, 476, 1, '2019-12-09 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(482, 'Vaccination Master', '#/vaccinationMaster', NULL, 476, 1, '2019-12-10 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(483, 'Immunization Schedule', '#/immunizationSchedule', NULL, 476, 1, '2019-12-11 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(484, 'System Master', '#/systemMaster', NULL, 476, 1, '2019-12-12 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(485, 'System Observation Master', '#/systemObservationMaster', NULL, 476, 1, '2019-12-13 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(486, 'Surgery Grade Master', '#/surgeryGradeMaster', NULL, 476, 1, '2019-12-14 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(487, 'Surgery Master', '#/surgeryMaster', NULL, 476, 1, '2019-12-15 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(488, 'Allergy Type Master', '#/allergyTypeMaster', NULL, 476, 1, '2019-12-16 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(489, 'Allergy Master', '#/allergyMaster', NULL, 476, 1, '2019-12-17 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(490, 'Common', NULL, NULL, 369, 1, '2019-12-18 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(491, 'Priority Master', '#/commonMaster/priorityMaster', NULL, 490, 1, '2019-12-19 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(492, 'Discount Master', '#/commonMaster/priorityMaster', NULL, 490, 1, '2019-12-20 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(493, 'Rejection Master', '#/rejectionMaster', NULL, 490, 1, '2019-12-21 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(494, 'Reason Type Master', '#/reasonTypeMaster', NULL, 490, 1, '2019-12-22 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(495, 'Reason Master', '#/reasonMaster', NULL, 490, 1, '2019-12-23 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(496, 'Qualification Master', '#/qualificationMaster', NULL, 490, 1, '2019-12-24 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(497, 'Degree Master', '#/degreeMaster', NULL, 490, 1, '2019-12-25 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(498, 'Employee Category Master', '#/employeeCategoryMaster', NULL, 490, 1, '2019-12-26 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(499, 'Doctor Category Master', '#/doctorCategoryMaster', NULL, 490, 1, '2019-12-27 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(500, 'Designation Master', '#/designationMaster', NULL, 490, 1, '2019-12-28 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(501, 'Inspection Master', '#/inspectionMaster', NULL, 490, 1, '2019-12-29 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(502, 'Dose Master', '#/doseMaster', NULL, 490, 1, '2019-12-30 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(504, ' Clinic Master', '#/clinicMaster', NULL, 503, 1, '2020-01-01 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(505, 'Baby Registration', NULL, NULL, 369, 1, '2020-01-02 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(506, ' Delivery Type Master', '#/deliveryTypeMaster', NULL, 506, 1, '2020-01-03 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(507, 'Diet and Kitchen', NULL, NULL, 369, 1, '2020-01-04 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(508, 'Nutritional Master', '#/nutritionalMaster', NULL, 507, 1, '2020-01-05 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(509, 'Key Ingredients Master', '#/keyIngredientsMaster', NULL, 507, 1, '2020-01-06 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(510, 'Diet Type Master', '#/dietTypeMaster', NULL, 507, 1, '2020-01-07 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(511, 'Dish Master', '#/dishMaster', NULL, 507, 1, '2020-01-08 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(512, 'Meal Master', '#/mealMaster', NULL, 507, 1, '2020-01-09 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(513, 'Day Wise Diet Template', '#/mealMaster', NULL, 507, 1, '2020-01-10 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(515, ' Procedure Configuration', '#/procedureConfiguration', NULL, 514, 1, '2020-01-12 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(516, ' Subsequent Surgery Configuration', '#/subsequentSurgeryConfiguration', NULL, 514, 1, '2020-01-13 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(517, 'Resource Configuration', '#/resourceConfiguration', NULL, 514, 1, '2020-01-14 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(518, ' Cancellation Master', '#/cancellationMaster', NULL, 514, 1, '2020-01-15 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(519, ' Operation Theatrer', '#/operation_Theatre', NULL, 514, 1, '2020-01-16 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(520, ' Procedure Type', '#/procedureType', NULL, 514, 1, '2020-01-17 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(521, ' Operation Theatre Type', '#/operationTheatreType', NULL, 514, 1, '2020-01-18 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(522, ' Operation Table', '#/operationTable', NULL, 514, 1, '2020-01-19 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(523, ' Operation Table Type', '#/operationTableType', NULL, 514, 1, '2020-01-20 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(524, ' Anaesthesia Type', '#/anaesthesiaType', NULL, 514, 1, '2020-01-21 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(525, ' Checklist Item', '#/checklistItem', NULL, 514, 1, '2020-01-22 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(526, ' Checklist Master', '#/checklistMaster', NULL, 514, 1, '2020-01-23 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(527, ' Consent Master', '#/consentMaster', NULL, 514, 1, '2020-01-24 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(528, ' Consent Type', '#/consentType', NULL, 514, 1, '2020-01-25 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(529, ' Anaesthesia Note', '#/noteMaster', NULL, 514, 1, '2020-01-26 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(530, ' Faculty', '#/facultyMaster', NULL, 514, 1, '2020-01-27 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(531, 'Unit Master', NULL, NULL, 0, 1, '2020-01-28 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(532, 'Organization Structure', NULL, NULL, 531, 1, '2020-01-29 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(533, 'Department Master', '#/departmentMasterUnit', NULL, 532, 1, '2020-01-30 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(534, 'Sub Department Master', '#/subDepartmentMasterUnit', NULL, 532, 1, '2020-01-31 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(535, ' ICU Type Master', '#/ICUtypeMasterUnit', NULL, 532, 1, '2020-02-01 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(536, ' Holiday Calendar Master', '#/holidayCalendarMasterUnit', NULL, 532, 1, '2020-02-02 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(537, ' Floor Master', '#/floorMaster', NULL, 532, 1, '2020-02-03 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(538, ' Floor Map Master', '#/floorMapMaster', NULL, 532, 1, '2020-02-04 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(539, ' Amenity Master', '#/amenityMaster', NULL, 532, 1, '2020-02-05 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(540, ' Wing Master', '#/wingMaster', NULL, 532, 1, '2020-02-06 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(541, 'ADT', NULL, NULL, 531, 1, '2020-02-07 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(542, ' Billing Bed Category Master', '#/bedCategoryMasterUnit', NULL, 541, 1, '2020-02-08 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(543, ' Bed Category Master', '#/bedCategoryMaster', NULL, 541, 1, '2020-02-09 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(544, ' Billing Bed Category', '#/billingBedCategoryMasterUnit', NULL, 541, 1, '2020-02-10 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(545, ' Ward Master', '#/wardMaster', NULL, 541, 1, '2020-02-11 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(546, ' Bed Master', '#/bedMaster', NULL, 541, 1, '2020-02-12 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(547, ' Nursing Station Master', '#/nursingStationMaster', NULL, 541, 1, '2020-02-13 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(548, ' Nursing Ward Mapping', '#/nursingStationWardMapping', NULL, 541, 1, '2020-02-14 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(549, ' Meal Preference Master', '#/mealPreferenceMaster', NULL, 541, 1, '2020-02-15 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(551, 'CSSD', NULL, NULL, 531, 1, '2020-02-17 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(552, ' Sterilization Type Master', '#/sterilizationTypeMaster', NULL, 551, 1, '2020-02-18 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(553, ' Autoclave Master', '#/autoclaveMaster', NULL, 551, 1, '2020-02-19 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(554, ' Wrapper expiry master', '#/wrapperExpiryMaster', NULL, 551, 1, '2020-02-20 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(555, ' Kit Creation', '#/kitCreation', NULL, 551, 1, '2020-02-21 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(557, 'Phlebotomy', '#/commonMaster/phlebotomyMaster', NULL, 556, 1, '2020-02-23 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(558, 'Test Master', '#/testMaster', NULL, 556, 1, '2020-02-24 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(559, ' Histopathology Test', '#/histopathologyTest', NULL, 556, 1, '2020-02-25 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(560, ' Speciman Type', '#/specimanType', NULL, 556, 1, '2020-02-26 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(561, 'Header Master', '#/commonMaster/headerMaster', NULL, 556, 1, '2020-02-27 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(562, 'Parameter Master', '#/parameterMaster', NULL, 556, 1, '2020-02-28 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(563, 'Multiparameter Test Master', '#/multiparameterTestMaster', NULL, 556, 1, '2020-02-29 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(564, ' Department Master', '#/commonMaster/departmentMaster', NULL, 556, 1, '2020-03-01 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(565, ' Template Master', '#/templateMaster', NULL, 556, 1, '2020-03-02 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(566, ' Parameter Formula', '#/parameterFormulaMaster', NULL, 556, 1, '2020-03-03 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(567, ' Profile/Panel Test', '#/profilepanelTestMaster', NULL, 556, 1, '2020-03-04 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(568, ' Storage Location Master', '#/storageLocationMasterForSlideBlocks', NULL, 556, 1, '2020-03-05 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(569, ' Biochemical Test Master', '#/commonMaster/biochemicalTest', NULL, 556, 1, '2020-03-06 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(570, 'Billing', NULL, NULL, 531, 1, '2020-03-07 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(571, ' Insurance Master', '#/insuranceMaster', NULL, 570, 1, '2020-03-08 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(572, ' TPA Master', '#/TPAmaster', NULL, 570, 1, '2020-03-09 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(573, ' Insurance & TPA Mapping', '#/InsuranceTPAMapping', NULL, 570, 1, '2020-03-10 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(574, ' Corporate Master', '#/corporateMasterGlobal', NULL, 570, 1, '2020-03-11 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(575, ' Grade Creation Master', '#/gradeCreationMaster', NULL, 570, 1, '2020-03-12 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(576, ' Billing Group Master', '#/billingGroupMaster', NULL, 570, 1, '2020-03-13 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(577, ' Package List', '#/packageList', NULL, 570, 1, '2020-03-14 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(578, ' Variable Pricing', '#/variableSquencePricing/sequencePricing', NULL, 570, 1, '2020-03-15 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(579, ' Account Payable Master', '#/accountPayableMaster', NULL, 570, 1, '2020-03-16 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(580, ' Account Payable', '#/accountPayable', NULL, 570, 1, '2020-03-17 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(581, ' Account Payable Comparsion', '#/accountPayableComparsion', NULL, 570, 1, '2020-03-18 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(582, ' Account Payable Status', '#/accountPayableStatus', NULL, 570, 1, '2020-03-19 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(583, ' Unit Service Master', '#/unitServiceMaster', NULL, 570, 1, '2020-03-20 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(584, ' Unit Service Mapping', '#/unitServiceMapping', NULL, 570, 1, '2020-03-21 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(585, ' Unit Service Base Price', '#/unitServiceBasePrice', NULL, 570, 1, '2020-03-22 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(586, ' Schedule Price Unit Service', '#/schedulePriceUnitService', NULL, 570, 1, '2020-03-23 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(587, ' Order Set', '#/orderSet', NULL, 570, 1, '2020-03-24 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(588, ' Order Set List', '#/orderSetList', NULL, 570, 1, '2020-03-25 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(589, ' Admission Rejection List', '#/admissionRejectionList', NULL, 570, 1, '2020-03-26 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(590, ' Refund CR List', '#/refundCRList/cashForRefund', NULL, 570, 1, '2020-03-27 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(591, ' Find Bill List', '#/findBillList', NULL, 570, 1, '2020-03-28 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(592, ' Charge Conversion', '#/chargeConversion', NULL, 570, 1, '2020-03-29 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(611, ' OP Consulation Configuration', '#/OPConsulationConfigurationMaster', NULL, 570, 1, '2020-04-17 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(612, ' Doctor Consulation', '#/doctorConsulationMaster', NULL, 570, 1, '2020-04-18 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(613, ' Global Doctor Share', '#/globalDoctorShare', NULL, 570, 1, '2020-04-19 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(614, ' Doctorwise Share', '#/doctorWiseShare', NULL, 570, 1, '2020-04-20 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(615, ' Doctor Referral Share', '#/doctorReferralShareMaster', NULL, 570, 1, '2020-04-21 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(616, ' AR Master', '#/ARmaster', NULL, 570, 1, '2020-04-22 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(617, ' AP Master', '#/APmaster', NULL, 570, 1, '2020-04-23 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(618, ' GL Code', '#/GLcode', NULL, 570, 1, '2020-04-24 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(619, ' Periodicity Service Configuration', '#/periodicityServiceConfig', NULL, 570, 1, '2020-04-25 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(620, ' IP Default Service Configuration', '#/IPdefaultServiceConfig', NULL, 570, 1, '2020-04-26 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(621, ' Deposit Matrix Configuration', '#/depositMatrixConfig', NULL, 570, 1, '2020-04-27 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(622, 'Pacakge Configuration', NULL, NULL, 531, 1, '2020-04-28 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(623, ' EHC Package', '#/EHCpackage', NULL, 622, 1, '2020-04-29 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(624, ' Package With CAP', '#/packageWithCAP', NULL, 622, 1, '2020-04-30 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(626, ' Either Or Package', '#/eitherOrPackage', NULL, 622, 1, '2020-05-02 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(627, ' Multicounter Package', '#/multicounterPackage', NULL, 622, 1, '2020-05-03 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(628, 'Pharmacy', NULL, NULL, 531, 1, '2020-05-04 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(629, ' Vendor Master', '#/vendormaster', NULL, 628, 1, '2020-05-05 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(630, ' Rack Master', '#/rackMaster', NULL, 628, 1, '2020-05-06 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(631, ' Main Store Master', '#/mainstoremaster', NULL, 628, 1, '2020-05-07 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(632, ' Store Item Mapping Master', '#/substoremaster', NULL, 628, 1, '2020-05-08 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(633, ' Product Restriction', '#/productRestriction/productCategoryMapping', NULL, 628, 1, '2020-05-09 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(634, ' Pharmacological Classification', '#/pharmacologicalClassificationMaster', NULL, 628, 1, '2020-05-10 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(635, ' Storewise Generic Mapper', '#/storewiseGenericMapper', NULL, 628, 1, '2020-05-11 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(636, ' Consumable Barcode Configuration', '#/consumableBarcodeConfig', NULL, 628, 1, '2020-05-12 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(637, ' Indent Template Master', '#/indentTemplateMaster', NULL, 628, 1, '2020-05-13 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(638, ' ETO Item Configuration', '#/ETOItemConfiguration', NULL, 628, 1, '2020-05-14 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(639, 'Clinical', 'No Link', NULL, 531, 1, '2020-05-15 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(640, 'Asset Management', NULL, NULL, 531, 1, '2020-05-16 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(641, ' Asset Type Master', '#/assetTypeMaster', NULL, 640, 1, '2020-05-17 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(642, ' Asset Category Master', '#/assetCategoryMaster', NULL, 640, 1, '2020-05-18 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(643, ' Product Category Master', '#/productCategoryMaster', NULL, 640, 1, '2020-05-19 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(644, ' Fixed Asset Master', '#/fixedAssetMaster', NULL, 640, 1, '2020-05-20 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(646, ' General Insurace Master', '#/generalInsuranceMaster', NULL, 640, 1, '2020-05-22 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(647, 'Common', 'No Link', NULL, 531, 1, '2020-05-23 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(648, ' Mortuary', NULL, NULL, 531, 1, '2020-05-24 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(649, ' Mortuary Bed Master', '#/mortuaryBedMaster', NULL, 648, 1, '2020-05-25 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(650, 'Staff Registration', '#/staffRegistration', NULL, 531, 1, '2020-05-26 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(651, 'Doctor Registration', '#/doctorRegistration', NULL, 531, 1, '2020-05-27 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(652, 'Scheduling', NULL, NULL, 531, 1, '2020-05-28 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(653, ' Appointment Scheduling', '#/appointmentSchedulingUnitMaster', NULL, 652, 1, '2020-05-29 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(654, ' Modality Type Master', '#/modalityTypeMaster', NULL, 652, 1, '2020-05-30 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(655, ' Modality Master', '#/modalityMaster', NULL, 652, 1, '2020-05-31 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(656, 'Schedule Block Unblock', NULL, NULL, 531, 1, '2020-06-01 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(657, ' Specialty Wise', '#/scheduleWise', NULL, 656, 1, '2020-06-02 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(658, ' Doctor Wise', '#/doctorWise', NULL, 656, 1, '2020-06-03 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(659, 'Configuration', NULL, NULL, 531, 1, '2020-06-04 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(660, ' Application Configuration', '#/applicationConfiguration', NULL, 659, 1, '2020-06-05 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(661, 'User Managment', NULL, NULL, 531, 1, '2020-06-06 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(667, 'Template Utility', NULL, NULL, 531, 1, '2020-06-12 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(668, ' Data Object', '#/dataObject', NULL, 667, 1, '2020-06-13 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(669, ' Create Template', '#/templateUtility', NULL, 667, 1, '2020-06-14 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(670, ' Templates', '#/templates', NULL, 667, 1, '2020-06-15 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(672, 'Role Creation', NULL, NULL, 661, 1, '2018-08-27 14:36:19.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(674, 'User Role Managment', NULL, NULL, 661, 1, '2018-08-27 14:37:56.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(675, 'Role Managment', NULL, NULL, 661, 1, '2018-08-27 14:38:32.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(676, 'User Authentication', NULL, NULL, 661, 1, '2018-08-27 14:39:04.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(666, 'Access Control', '#/newAccessControl', NULL, 662, 1, '2020-06-11 00:00:00.000', 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(645, ' Equipment Master', '#/commonMaster/', NULL, 640, 1, '2020-05-21 00:00:00.000', 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(671, 'Sterilization Request', NULL, NULL, NULL, 1, '2018-08-21 11:50:14.000', 'D', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(678, 'Back Office', NULL, NULL, 0, 1, '2018-08-27 15:37:36.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(602, ' Company Invoice Creation', NULL, NULL, 678, 1, '2020-04-08 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(603, ' Company Remainder', NULL, NULL, 678, 1, '2020-04-09 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(604, ' Company Query & Response', NULL, NULL, 678, 1, '2020-04-10 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(605, ' Company Settlement', NULL, NULL, 678, 1, '2020-04-11 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(606, ' Settled Invoices', NULL, NULL, 678, 1, '2020-04-12 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(607, ' Payment Collection', NULL, NULL, 678, 1, '2020-04-13 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(680, 'Appointments', NULL, NULL, 2, 1, '2018-08-27 15:49:25.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(681, 'Staff Linen Allocation', NULL, NULL, 343, 1, '2018-08-27 15:54:02.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(682, 'Sterilization Request Acceptance', NULL, NULL, 231, 1, '2018-08-27 15:57:50.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(715, 'User Counter Mapper', NULL, NULL, 661, 1, '2018-10-08 14:52:52.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(662, ' Role Creation', '#/roleCreation', NULL, 662, 1, '2020-06-07 00:00:00.000', 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(663, ' User Role Managment', '#/userRoleManagement', NULL, 662, 1, '2020-06-08 00:00:00.000', 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(664, ' Role Managment', '#/roleManagement', NULL, 662, 1, '2020-06-09 00:00:00.000', 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(665, 'User Authentication', '#/userAuthentication', NULL, 662, 1, '2020-06-10 00:00:00.000', 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(686, 'Encounter Queue EHC', NULL, NULL, 2, 1, '2018-08-27 17:27:46.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(684, 'IP Consultation Configuration', NULL, NULL, 570, 1, '2018-08-27 16:48:14.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(685, 'Pending for PO Creation', NULL, NULL, 570, 1, '2018-08-27 16:50:24.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(689, 'User Nursing Station Mapper', NULL, NULL, 661, 1, '2018-08-31 11:49:29.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(690, 'User Store Mapper', NULL, NULL, 661, 1, '2018-08-31 11:50:14.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(716, 'Blood Bank', NULL, NULL, 0, 1, '2018-10-17 14:52:10.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(692, 'Workstation', NULL, NULL, 44, 1, '2018-09-03 16:07:14.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(696, 'Procedure Configurations', NULL, NULL, 514, 1, '2018-09-15 10:44:55.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(697, 'Weekend Master', NULL, NULL, 532, 1, '2018-09-15 10:45:56.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(698, 'Order Set Master', NULL, NULL, 532, 1, '2018-09-15 10:47:31.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(699, 'Prefix Configuration', NULL, NULL, 659, 1, '2018-09-15 10:48:35.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(700, 'Washed Linen Acceptance', NULL, NULL, 343, 1, '2018-09-15 10:53:35.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(597, ' Cash Opening Balance', '#/cashOpeningBalance', NULL, 679, 1, '2020-04-03 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(693, 'Service Rendering', '#/serviceRendering', NULL, 692, 1, '2018-09-03 16:08:34.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(694, 'Nursing Progress Note', '#/nursingProgressNote', NULL, 692, 1, '2018-09-05 11:10:50.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(683, 'Sterilization Request', '#/sterilizationRequest', NULL, 88, 1, '2018-08-27 15:59:38.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(695, 'Stock Register', '#/stockRegister', NULL, 123, 1, '2018-09-15 10:42:29.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(691, 'Vitals', '#/vitals', NULL, 98, 1, '2018-09-03 12:02:28.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(688, 'Template ICD Code Mapping', '#/templateICDCodeMapping', NULL, 667, 1, '2018-08-30 18:21:59.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(702, 'Nursing Worklist', NULL, NULL, 2, 1, '2018-09-18 17:35:10.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(701, 'Counter Worklist', NULL, NULL, 2, 1, '2018-09-18 17:34:12.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(703, 'Self Registration', NULL, NULL, 2, 1, '2018-09-18 18:26:48.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(687, 'Admission Request', NULL, NULL, 27, 1, '2018-08-28 12:05:38.000', 'D', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(704, 'Counter Master', NULL, NULL, 532, 1, '2018-09-20 16:46:49.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(705, 'Cabin Master', NULL, NULL, 532, 1, '2018-09-20 16:47:32.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(707, 'Order set Master', NULL, NULL, 639, 1, '2018-09-20 16:52:55.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(709, 'Billing Bed Category', NULL, NULL, 445, 1, '2018-09-20 17:03:35.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(744, 'Growth Chart', NULL, NULL, 2, 1, '2018-10-19 10:23:26.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(718, 'Blood Bank Process', NULL, NULL, 716, 1, '2018-10-17 14:59:15.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(708, 'Stimulaion Calendar', NULL, NULL, 706, 1, '2018-09-20 16:54:45.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(710, 'Surgical Calendar', NULL, NULL, 514, 1, '2018-09-24 11:19:54.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(711, 'Admission', NULL, NULL, 27, 1, '2018-09-24 11:22:43.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(712, 'Costing Parameter', NULL, NULL, 556, 1, '2018-09-25 11:24:59.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(713, 'Costing Sheet', NULL, NULL, 556, 1, '2018-09-25 11:27:48.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(556, 'LIS', NULL, NULL, 531, 1, '2020-02-22 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(411, 'LIS', NULL, NULL, 369, 1, '2019-09-30 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(3, 'Patient Registration', '#/patientRegistration', NULL, 2, 1, '2018-08-18 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(24, 'Doctor Share Payment Voucher', '#/doctorSharePaymentVoucher', NULL, 22, 1, '2018-09-08 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(233, 'Cleaned Worklist', '#/preSterilizationCleaning', NULL, 232, 1, '2019-04-05 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(714, 'Doctor Cabin Mapper', NULL, NULL, 661, 1, '2018-10-08 14:52:09.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(717, 'Donor', NULL, NULL, 716, 1, '2018-10-17 14:53:50.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(719, 'Donor Registration', NULL, NULL, 717, 1, '2018-10-17 15:03:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(600, 'Shift Closing', '#/cashCounterClosing', NULL, 679, 1, '2020-04-06 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(625, 'All Inclusive Package', NULL, NULL, 622, 1, '2020-05-01 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(679, 'Cash Desk Management', NULL, NULL, 678, 1, '2018-08-27 15:38:03.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(673, 'Role Permission', '#/accessControl1', NULL, 661, 1, '2018-08-27 14:37:39.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(720, 'Donor Screening Worklist', NULL, NULL, 717, 1, '2018-10-17 15:04:06.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(721, 'Blood Bag Collection Worklist', NULL, NULL, 717, 1, '2018-10-17 15:04:48.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(722, 'Transfusion Transmitted Diseases', NULL, NULL, 718, 1, '2018-10-17 15:06:50.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(723, 'Red Cell Grouping Worklist', NULL, NULL, 718, 1, '2018-10-17 15:09:26.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(724, 'Red Cell Serum Worklist', NULL, NULL, 718, 1, '2018-10-17 15:10:13.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(725, 'Blood Bag Component Extraction', NULL, NULL, 718, 1, '2018-10-17 15:11:04.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(726, 'Blood Bag Stock Updation', NULL, NULL, 718, 1, '2018-10-17 15:11:45.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(727, 'Patient Requisition Worklist', NULL, NULL, 718, 1, '2018-10-17 15:12:32.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(728, 'Loyalty Card', NULL, NULL, 531, 1, '2018-10-17 15:18:19.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(729, 'Loyalty Card Creation', NULL, NULL, 728, 1, '2018-10-17 15:19:26.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(730, 'Blood Bank', NULL, NULL, 531, 1, '2018-10-17 15:25:08.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(731, 'Blood Bag Type', NULL, NULL, 730, 1, '2018-10-17 15:26:01.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(732, 'Blood Donor Type', NULL, NULL, 730, 1, '2018-10-17 15:26:43.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(733, 'Blood Test Result Master', NULL, NULL, 730, 1, '2018-10-17 15:27:12.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(734, 'Blood Test', NULL, NULL, 730, 1, '2018-10-17 15:27:47.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(735, 'Blood Bag Capacity', NULL, NULL, 730, 1, '2018-10-17 15:28:54.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(736, 'Blood Component', NULL, NULL, 730, 1, '2018-10-17 15:29:42.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(737, 'Reason Of Discard', NULL, NULL, 730, 1, '2018-10-17 15:30:18.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(738, 'Screening Test', NULL, NULL, 730, 1, '2018-10-17 15:30:48.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(739, 'Blood Bag Type Component Master', NULL, NULL, 730, 1, '2018-10-17 15:31:32.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(740, 'Donor Rejection Master', NULL, NULL, 730, 1, '2018-10-17 15:32:15.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(741, 'Blood Group Matching Master', NULL, NULL, 730, 1, '2018-10-17 15:33:05.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(742, 'Blood Bank Master', NULL, NULL, 730, 1, '2018-10-17 15:34:26.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(743, 'Loyalty Card Configuration', NULL, NULL, 728, 1, '2018-10-17 15:35:45.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(706, 'IVF', NULL, NULL, 0, 1, '2018-09-20 16:54:15.000', 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(745, 'Discount Rights', NULL, NULL, 10, 1, '2018-10-19 10:49:32.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(503, 'OPD', NULL, NULL, 369, 1, '2019-12-31 00:00:00.000', 'D', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(746, 'Department Nursing Station Mapper', NULL, NULL, 661, 1, '2018-10-20 13:22:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(747, 'Token Display', NULL, NULL, 2, 1, '2018-10-23 14:41:05.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(748, 'Discount Request Worklist', NULL, NULL, 10, 1, '2018-10-23 14:42:07.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(749, 'Discount Request Approval', NULL, NULL, 10, 1, '2018-10-23 14:42:40.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(750, 'Vaccination Schedule', NULL, NULL, 476, 1, '2018-10-23 14:43:15.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(476, 'Clinical Workstation', NULL, NULL, 369, 1, '2019-12-04 00:00:00.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(758, 'Doctor Desk', NULL, NULL, 243, 1, '2018-11-23 12:40:15.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(751, 'Loyalty Card', NULL, NULL, 0, 1, '2018-10-24 14:39:10.000', 'D', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(752, 'Assign Loyalty Card', NULL, NULL, 10, 1, '2018-10-24 14:40:05.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(774, 'SMS Utility', NULL, NULL, 659, 1, '2019-04-01 11:55:44.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(786, 'Ready For Billing', NULL, NULL, 784, 1, '2019-04-30 14:52:12.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(814, 'Ambulance Master', NULL, NULL, 812, 1, '2019-06-20 10:52:24.000', 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(824, 'RMO Allocation Master', NULL, NULL, 639, 1, '2019-06-24 13:18:33.000', 'A', 1);

INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(1, 'Walkin Sale', true, 1, '2018-03-24 06:01:06.000', 1, '2018-03-24 06:01:06.000', false, 'WS', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(2, 'Store Indent', true, 1, '2018-03-24 06:01:28.000', 1, '2018-03-24 06:01:28.000', false, 'SI', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(3, 'Opening Balance', true, 1, '2018-03-24 06:02:45.000', 1, '2018-03-24 06:02:45.000', false, 'OPB', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(4, 'Purchase Request', true, 1, '2018-03-24 06:39:19.000', 1, '2018-03-24 06:39:19.000', false, 'PR', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(5, 'Local Purchase Request', true, 1, '2018-03-24 06:39:29.000', 1, '2018-03-24 06:39:29.000', false, 'PRL', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(6, 'Store Indent Approval', true, 1, '2018-03-25 06:12:10.000', 1, '2018-03-25 06:12:10.000', false, 'SIA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(7, 'Local Purchase Request Approval', true, 1, '2018-03-25 06:12:39.000', 1, '2018-03-25 06:12:39.000', false, 'LPRA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(8, 'Purchase Request Approval', true, 1, '2018-03-25 06:12:50.000', 1, '2018-03-25 06:12:50.000', false, 'PRA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(9, 'Bill List', true, 1, '2018-04-03 15:03:34.000', 1, '2018-04-03 15:03:34.000', false, 'BL', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(10, 'Purchase Order Creation', true, 1, '2018-04-07 10:26:42.000', 1, '2018-04-07 10:26:42.000', false, 'POC', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(11, 'Purchase Order Approval', true, 1, '2018-04-07 10:27:23.000', 1, '2018-04-07 10:27:23.000', false, 'POA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(12, 'Good Receipt Note', true, 1, '2018-04-07 10:27:49.000', 1, '2018-04-07 10:27:49.000', false, 'GRN', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(13, 'Good Receipt Note Approval', true, 1, '2018-04-07 10:28:04.000', 1, '2018-04-07 10:28:04.000', false, 'GRNA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(14, 'Pending Store Indent', true, 1, '2018-04-14 13:18:14.000', 1, '2018-04-14 13:18:14.000', false, 'PSI', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(15, 'Sale Worklist', true, 1, '2018-04-17 09:58:27.000', 1, '2018-04-17 09:58:27.000', false, 'SW', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(16, 'Sale Return', true, 1, '2018-04-19 12:41:19.000', 1, '2018-04-19 12:41:19.000', false, 'SR', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(17, 'Item Request', true, 1, '2018-04-23 09:10:48.000', 1, '2018-04-23 09:10:48.000', false, 'IR', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(18, 'Item Request Approval', true, 1, '2018-04-23 09:10:48.000', 1, '2018-04-23 09:10:48.000', false, 'IRA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(19, 'Material Picker', true, 1, '2018-04-23 12:45:45.000', 1, '2018-04-23 12:45:45.000', false, 'MP', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(20, 'Material Issue', true, 1, '2018-04-23 12:46:05.000', 1, '2018-04-23 12:46:05.000', false, 'MI', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(21, 'Item Creation Pending', true, 1, '2018-04-25 13:11:50.000', 1, '2018-04-25 13:11:50.000', false, 'ICP', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(22, 'Pharmacology Validaion Worklist', true, 1, '2018-04-26 05:48:21.000', 1, '2018-04-26 05:48:21.000', false, 'PVW', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(23, 'Handover Worklist', true, 1, '2018-05-04 12:03:32.000', 1, '2018-05-04 12:03:32.000', false, 'HW', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(24, 'Assign Picker', true, 1, '2018-05-05 05:59:40.000', 1, '2018-05-05 05:59:40.000', false, 'AP', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(25, 'In Patient Pending Issue', true, 1, '2018-05-05 06:00:24.000', 1, '2018-05-05 06:00:24.000', false, 'IPPI', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(26, 'In Patient Issue Acceptance', true, 1, '2018-05-05 06:01:03.000', 1, '2018-05-05 06:01:03.000', false, 'IPIA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(27, 'Patiennt Indent', true, 1, '2018-05-05 06:01:30.000', 1, '2018-05-05 06:01:30.000', false, 'PI', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(28, 'Billable Consumption', true, 1, '2018-05-05 06:02:01.000', 1, '2018-05-05 06:02:01.000', false, 'BC', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(29, 'Material Issue Acceptance', true, 1, '2018-05-07 08:23:59.000', 1, '2018-05-07 08:23:59.000', false, 'MIA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(30, 'Pending Issues', true, 1, '2018-05-10 05:40:00.000', 1, '2018-05-10 05:40:00.000', false, 'PISS', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(31, 'Physical Inspection', true, 1, '2018-05-10 05:43:25.000', 1, '2018-05-10 05:43:25.000', false, 'PINS', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(32, 'Issue Acceptance', true, 1, '2018-05-10 05:45:24.000', 1, '2018-05-10 05:45:24.000', false, 'IAC', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(33, 'Issue Rejection', true, 1, '2018-05-10 05:46:26.000', 1, '2018-05-10 05:46:26.000', false, 'IRJ', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(34, 'Material Reject Acceptance', true, 1, '2018-05-10 18:12:53.000', 1, '2018-05-10 18:12:53.000', false, 'MRA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(35, 'Pending Rejected Rejection', true, 1, '2018-05-14 14:12:27.000', 1, '2018-05-14 14:12:27.000', false, 'PRR', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(36, 'Grn Against Po', true, 1, '2018-05-17 12:54:42.000', 1, '2018-05-17 12:54:42.000', false, 'GRNAP', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(37, 'Quotation Enquiry', true, 1, '2018-05-22 11:45:44.000', 1, '2018-05-22 11:45:44.000', false, 'QE', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(38, 'Enquiry Send to Vendor', true, 1, '2018-05-22 11:46:13.000', 1, '2018-05-22 11:46:13.000', false, 'ESV', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(39, 'Stock Dashboard', true, 1, '2018-05-22 13:01:11.000', 1, '2018-05-22 13:01:11.000', false, 'SD', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(40, 'Batch Expiry', true, 1, '2018-05-25 17:48:07.000', 1, '2018-05-25 17:48:07.000', false, 'BE', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(41, 'Material Return', true, 1, '2018-05-26 15:08:12.000', 1, '2018-05-26 15:08:12.000', false, 'MR', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(42, 'Batch Expiry Approval', true, 1, '2018-05-26 16:41:57.000', 1, '2018-05-26 16:41:57.000', false, 'BEA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(43, 'Patient Issue Consumable', true, 1, '2018-05-28 07:36:43.682', 1, '2018-05-28 07:36:43.682', false, 'PIC', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(44, 'Non Billable Consumption ', true, 1, '2018-05-28 09:49:50.000', 1, '2018-05-28 09:49:50.000', false, 'NBCO', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(45, 'Consumable Acceptance ', true, 1, '2018-05-28 09:56:57.000', 1, '2018-05-28 09:56:57.000', false, 'CONAC', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(46, 'Material Return Acceptance', true, 1, '2018-05-28 19:29:19.000', 1, '2018-05-28 19:29:19.000', false, 'MRA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(47, 'Pending Handover Worklist', true, 1, '2018-05-30 09:56:55.000', 1, '2018-05-30 09:56:55.000', false, 'PHWO', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(48, 'Patient Issue Rejection', true, 1, '2018-05-30 09:59:10.000', 1, '2018-05-30 09:59:10.000', false, 'PIREJ', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(49, 'Patient Return Acceptance', true, 1, '2018-05-30 10:02:52.000', 1, '2018-05-30 10:02:52.000', false, 'PARAC', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(50, 'Returnable Gatepass', true, 1, '2018-05-31 17:07:51.000', 1, '2018-05-31 17:07:51.000', false, 'RG', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(51, 'Returnable Gatepass Approval', true, 1, '2018-05-31 17:09:58.000', 1, '2018-05-31 17:09:58.000', false, 'RGA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(52, 'Quotation Fill In', true, 1, '2018-06-01 05:41:19.000', 1, '2018-06-01 05:41:19.000', false, 'QFI', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(53, 'Scrap Request', true, 1, '2018-06-01 11:45:12.000', 1, '2018-06-01 11:45:12.000', false, 'SREQ', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(54, 'Patient Issue Return', true, 1, '2018-06-02 06:18:48.000', 1, '2018-06-02 06:18:48.000', false, 'PIR', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(55, 'Scrap Request Approval', true, 1, '2018-06-04 09:07:11.000', 1, '2018-06-04 09:07:11.000', false, 'SRAPP', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(56, 'Issue Worklist', true, 1, '2018-06-05 06:36:09.000', 1, '2018-06-05 06:36:09.000', false, 'ISSEW', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(57, 'Request Asset Disposal Worklist', true, 1, '2018-06-06 07:27:40.000', 1, '2018-06-06 07:27:40.000', false, 'RADWO', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(58, 'Waste Asset Disposal Worklist', true, 1, '2018-06-07 05:25:46.000', 1, '2018-06-07 05:25:46.000', false, 'WADWO', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(59, 'Witness Approval', true, 1, '2018-06-07 05:27:53.000', 1, '2018-06-07 05:27:53.000', false, 'WITAA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(60, 'Return Of Empty Ampoules', true, 1, '2018-06-08 09:59:17.000', 1, '2018-06-08 09:59:17.000', false, 'ROEA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(61, 'Delivery Worklist', true, 1, '2018-06-08 10:00:53.000', 1, '2018-06-08 10:00:53.000', false, 'DELW', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(62, 'Acceptance Of Empty Ampoules', true, 1, '2018-06-08 10:33:26.000', 1, '2018-06-08 10:33:26.000', false, 'AOEA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(63, 'Non Returnable Gatepass', true, 1, '2018-06-08 10:35:26.000', 1, '2018-06-08 10:35:26.000', false, 'NREG', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(64, 'Return Billable Consumption', true, 1, '2018-06-08 10:35:32.000', 1, '2018-06-08 10:35:32.000', false, 'RBICON', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(65, 'Technical Approval', true, 1, '2018-06-11 06:49:54.000', 1, '2018-06-11 06:49:54.000', false, 'TECHA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(66, 'Material Inwards', true, 1, '2018-06-11 11:26:32.000', 1, '2018-06-11 11:26:32.000', false, 'MATIN', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(67, 'Material Inwards Approval', true, 1, '2018-06-11 11:26:32.000', 1, '2018-06-11 11:26:32.000', false, 'MAIAP', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(68, 'Crash Cart Request', true, 1, '2018-06-14 11:37:57.000', 1, '2018-06-14 11:37:57.000', false, 'CCR', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(69, 'Crash Cart Medication Administration Approval', true, 1, '2018-06-14 11:49:01.000', 1, '2018-06-14 11:49:01.000', false, 'CCMAA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(70, 'Add New Fixed Asset', true, 1, '2018-06-18 10:18:55.000', 1, '2018-06-18 10:18:55.000', false, 'ANFA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(71, 'Crash Cart Approval Request ', true, 1, '2018-06-18 10:28:18.000', 1, '2018-06-18 10:28:18.000', false, 'CCAR', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(72, 'Vendor Negociation ', true, 1, '2018-06-18 11:59:17.000', 1, '2018-06-18 11:59:17.000', false, 'VENN', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(73, 'Loan In', true, 1, '2018-06-20 12:09:11.000', 1, '2018-06-20 12:09:11.000', false, 'LI', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(74, 'Loan In Out', true, 1, '2018-06-21 09:40:11.000', 1, '2018-06-21 09:40:11.000', false, 'LOIO', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(75, 'Loan Out', true, 1, '2018-06-21 09:50:41.000', 1, '2018-06-21 09:50:41.000', false, 'LOANO', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(76, 'Stop Transaction', true, 1, '2018-06-21 10:42:32.000', 1, '2018-06-21 10:42:32.000', false, 'STRAN', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(77, 'Drug Recall', true, 1, '2018-06-22 05:58:24.000', 1, '2018-06-22 05:58:24.000', false, 'DRUR', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(78, 'Auction Requisition', true, 1, '2018-06-25 11:29:32.000', 1, '2018-06-25 11:29:32.000', false, 'AUCRN', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(79, 'Loanout Receive', true, 1, '2018-06-25 11:29:32.000', 1, '2018-06-25 11:29:32.000', false, 'LOOTR', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(80, 'Item Auction', true, 1, '2018-06-25 11:57:02.000', 1, '2018-06-25 11:57:02.000', false, 'IAUC', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(81, 'Vendor Selection', true, 1, '2018-06-26 14:07:42.000', 1, '2018-06-26 14:07:42.000', false, 'VENSN', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(82, 'Recall Drug', true, 1, '2018-06-27 06:45:20.000', 1, '2018-06-27 06:45:20.000', false, 'RECD', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(83, 'Upload Stock', true, 1, '2018-06-27 12:36:15.000', 1, '2018-06-27 12:36:15.000', false, 'UPLS', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(84, 'Auction Vendor Approval', true, 1, '2018-06-28 12:31:56.000', 1, '2018-06-28 12:31:56.000', false, 'AVAP', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(85, 'Upload Stock Approval', true, 1, '2018-07-01 13:16:52.000', 1, '2018-07-01 13:16:52.000', false, 'USA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(86, 'Compute Stock', true, 1, '2018-07-01 13:18:59.000', 1, '2018-07-01 13:18:59.000', false, 'CS', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(87, 'Variance Approval', true, 1, '2018-07-01 13:20:20.000', 1, '2018-07-01 13:20:20.000', false, 'VA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(88, 'Auction Requisition Approval', true, 1, '2018-07-02 11:57:35.000', 1, '2018-07-02 11:57:35.000', false, 'ARQA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(89, 'Confirm Stock', true, 1, '2018-07-05 05:33:54.000', 1, '2018-07-05 05:33:54.000', false, 'COS', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(90, 'Asset Indent', true, 1, '2018-07-23 14:08:29.000', 1, '2018-07-23 14:08:29.000', false, 'AI', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(91, 'Asset Indent Approval', true, 1, '2018-07-23 14:15:49.000', 1, '2018-07-23 14:15:49.000', false, 'AIA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(92, 'Workorder Request', true, 1, '2018-07-23 18:15:25.000', 1, '2018-07-23 18:15:25.000', false, 'WoR', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(93, 'Workorder Request Approval', true, 1, '2018-07-23 18:18:01.000', 1, '2018-07-23 18:18:01.000', false, 'WoRA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(94, 'Asset Non Returnable', true, 1, '2018-07-30 07:31:10.000', 1, '2018-07-30 07:31:10.000', false, 'ASNT', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(95, 'Linen Indent', true, 1, '2018-08-01 10:27:10.000', 1, '2018-08-01 10:27:10.000', false, 'LI', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(96, 'Linen Indent Approval', true, 1, '2018-08-01 10:27:28.000', 1, '2018-08-01 10:27:28.000', false, 'LIA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(97, 'Workorder Quotation Request', true, 1, '2018-08-01 10:52:31.000', 1, '2018-08-01 10:52:31.000', false, 'WQR', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(98, 'Workorder Quotation Send to Vendor', true, 1, '2018-08-01 10:53:01.000', 1, '2018-08-01 10:53:01.000', false, 'WQSV', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(99, 'Workorder Quotation Entry', true, 1, '2018-08-01 10:53:27.000', 1, '2018-08-01 10:53:27.000', false, 'WQE', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(100, 'Linen Issue', true, 1, '2018-08-03 05:51:44.000', 1, '2018-08-03 05:51:44.000', false, 'Lis', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(101, 'Linen Issue Acceptance', true, 1, '2018-08-03 05:51:44.000', 1, '2018-08-03 05:51:44.000', false, 'LIA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(102, 'Workorder Quotation Negotiation', true, 1, '2018-08-06 05:38:07.000', 1, '2018-08-06 05:38:07.000', false, 'WQN', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(103, 'Workorder Quotation Selection', true, 1, '2018-08-06 05:38:55.000', 1, '2018-08-06 05:38:55.000', false, 'WQS', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(104, 'Asset Issue', true, 1, '2018-08-07 07:09:10.000', 1, '2018-08-07 07:09:10.000', false, 'AIs', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(105, 'Asset Issue Acceptance', true, 1, '2018-08-07 07:09:22.000', 1, '2018-08-07 07:09:22.000', false, 'AIsA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(106, 'Reject Acceptance', true, 1, '2018-08-08 04:37:18.000', 1, '2018-08-08 04:37:18.000', false, 'RA', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(107, 'Asset Return Acceptance', true, 1, '2018-08-08 05:06:16.000', 1, '2018-08-08 05:06:16.000', false, 'ARA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(108, 'Linen Return', true, 1, '2018-08-16 05:45:00.000', 1, '2018-08-16 05:45:00.000', false, 'LIR', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(109, 'Asset Return', true, 1, '2018-08-17 13:39:16.000', 1, '2018-08-17 13:39:16.000', false, 'AR', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(110, 'Asset Return Acceptance', true, 1, '2018-08-17 13:39:50.000', 1, '2018-08-17 13:39:50.000', false, 'ARA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(111, 'Linen Return Acceptance', true, 1, '2018-08-21 05:51:32.000', 1, '2018-08-21 05:51:32.000', false, 'LRA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(112, 'Linen Reject Acceptance', true, 1, '2018-08-21 05:51:58.000', 1, '2018-08-21 05:51:58.000', false, 'LReA', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(113, 'Material Issue Pending', true, 1, '2018-08-21 15:34:50.000', 1, '2018-08-21 15:34:50.000', false, 'PI', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(114, 'Patient Pending Issue ', true, 1, '2018-09-04 10:58:08.000', 1, '2018-09-04 10:58:08.000', false, 'PPI', false);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(115, 'Discharge Medication Reconciliation', true, 1, '2018-08-08 04:37:18.000', 1, '2018-08-08 04:37:18.000', false, 'OG', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(116, 'Discharge Medication Handover', true, 1, '2018-08-08 04:37:18.000', 1, '2018-08-08 04:37:18.000', false, 'BG', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(117, 'Homecare Medication Entry', true, 1, '2018-08-08 04:37:18.000', 1, '2018-08-08 04:37:18.000', false, 'RG', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(118, 'Workorder Creation', true, 1, '2018-08-08 04:37:18.000', 1, '2018-08-08 04:37:18.000', false, 'RG', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(119, 'Send to Condemnate', true, 1, '2018-08-08 04:37:18.000', 1, '2018-08-08 04:37:18.000', false, 'RG', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(120, 'Consignment Inward', true, 1, '2018-11-28 15:10:01.000', 1, '2018-11-28 15:10:01.000', false, 'CIn', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(121, 'Consignment Outward', true, 1, '2018-11-28 15:10:01.000', 1, '2018-11-28 15:10:01.000', false, 'COut', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(122, 'Implant Indent', true, 1, '2018-11-28 15:10:01.000', 1, '2018-11-28 15:10:01.000', false, 'ImpI', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(123, 'Deposit ', true, 1, '2018-11-28 15:10:01.000', 1, '2018-11-28 15:10:01.000', false, 'DEPO', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(124, 'Refund ', true, 1, '2018-11-28 15:10:01.000', 1, '2018-11-28 15:10:01.000', false, 'RF', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(125, 'Refund Against Deposit', true, 1, '2018-11-28 15:10:01.000', 1, '2018-11-28 15:10:01.000', false, 'RFD', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(126, 'Credit Note', true, 1, '2018-11-28 15:10:01.000', 1, '2018-11-28 15:10:01.000', false, 'CN', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(127, 'OT Completion', true, 1, '2018-11-28 15:10:01.000', 1, '2018-11-28 15:10:01.000', false, 'OTc', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(128, 'Patient Consignment Issue', true, 1, '2018-11-28 15:10:01.000', 1, '2018-11-28 15:10:01.000', false, 'PConIss', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(129, 'Consignment Return', true, 1, '2018-11-28 15:10:01.000', 1, '2018-11-28 15:10:01.000', false, 'CR', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(130, 'Consignment Return Acceptance', true, 1, '2018-11-28 15:10:01.000', 1, '2018-11-28 15:10:01.000', false, 'CRA', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(131, 'Implant Indent Acceptance', true, 1, '2018-11-28 15:10:01.000', 1, '2018-11-28 15:10:01.000', false, 'IIA', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(132, 'Patient Reject', true, 1, '2018-11-28 15:10:01.000', 1, '2018-11-28 15:10:01.000', false, 'PR', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(133, 'Normal Patient UHID', true, 1, '2018-11-28 15:10:01.000', 1, '2018-11-28 15:10:01.000', false, 'NP-UHID', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(134, 'OTC Patient UHID', true, 1, '2018-11-28 15:10:01.000', 1, '2018-11-28 15:10:01.000', false, 'OTC-UHID', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(135, 'Emergency Patient UHID', true, 1, '2018-11-28 15:10:01.000', 1, '2018-11-28 15:10:01.000', false, 'ER-UHID', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(136, 'PreRegistration Patient UHID', true, 1, '2018-11-28 15:10:01.000', 1, '2018-11-28 15:10:01.000', false, 'PR-UHID', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(137, 'Opening Balance Implant', true, 1, '2018-11-28 15:10:01.000', 1, '2018-11-28 15:10:01.000', false, 'OPBI', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(138, 'Pending Kit Formation', true, 1, '2018-11-28 15:10:01.000', 1, '2018-11-28 15:10:01.000', false, 'PKF', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(139, 'Consignment PO Creation', true, 1, '2019-03-02 14:24:29.000', 1, '2019-03-02 14:24:29.000', false, 'CPO', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(140, 'Order Number', true, 1, '2019-03-02 14:24:29.000', 1, '2019-03-02 14:24:29.000', false, 'CPO', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(141, 'Bill Number', true, 1, '2019-03-02 14:24:29.000', 1, '2019-03-02 14:24:29.000', false, 'CPO', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(142, 'Debit Note Number', true, 1, '2019-03-02 14:24:29.000', 1, '2019-03-02 14:24:29.000', false, 'CPO', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(143, 'Reciept Number', true, 1, '2019-03-02 14:24:29.000', 1, '2019-03-02 14:24:29.000', false, 'CPO', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(144, 'Deposit Consumption Number', true, 1, '2019-03-02 14:24:29.000', 1, '2019-03-02 14:24:29.000', false, 'CPO', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(145, 'Consignment PO Approval', true, 1, '2019-03-14 17:55:02.000', 1, '2019-03-14 17:55:02.000', false, 'CPOA', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(146, 'Donor Number', true, 1, '2019-03-02 14:24:29.000', 1, '2019-03-02 14:24:29.000', false, 'DN', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(147, 'Ambulance LPO Number', true, 1, '2019-05-17 14:24:29.000', 1, '2019-05-17 14:24:29.000', false, 'LPO', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(148, 'Ambulance Charges Bill', true, 1, '2019-05-17 14:24:29.000', 1, '2019-05-17 14:24:29.000', false, 'AMB', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(149, 'Blood Bag Issue Number', true, 1, '2019-05-17 14:24:29.000', 1, '2019-05-17 14:24:29.000', false, 'ISSN', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(150, 'Blood Bag Number', true, 1, '2019-05-17 14:24:29.000', 1, '2019-05-17 14:24:29.000', false, 'BN', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(151, 'Blood Component Ordering Requisition Number', true, 1, '2019-05-17 14:24:29.000', 1, '2019-05-17 14:24:29.000', false, 'BCORN', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(152, 'Baby Patient UHID', true, 1, '2019-05-17 14:24:29.000', 1, '2019-05-17 14:24:29.000', false, 'BABY-UHID', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(153, 'Intrim Bill Number', true, 1, '2019-06-04 15:33:39.000', 1, '2019-06-04 15:33:39.000', false, 'IntrimNo', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(154, 'Death Registraion/Still Birth', true, 1, '2019-06-05 13:13:22.000', 1, '2019-06-05 13:13:22.000', false, 'DeathReg', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(155, 'Baby Patient UHID Still', true, 1, '2019-06-05 13:13:22.000', 1, '2019-06-05 13:13:22.000', false, 'BABY-UHIDS', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(156, 'Company Invoice', true, 1, '2019-07-08 12:01:59.000', 1, '2019-07-08 12:01:59.000', false, 'CMPIVS', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(157, 'Encounter', true, 1, '2019-08-11 19:27:15.000', 1, '2019-08-11 19:27:15.000', false, 'ENC', true);
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(158, 'Admission', true, 1, '2019-08-11 19:27:15.000', 1, '2019-08-11 19:27:15.000', false, 'ADM', true);



INSERT INTO public.m_status
(id, code, status, is_active, updated_by, updated_date, added_by, added_date, is_deleted, organization_id)
VALUES(1, 'N', 'New', true, 1, '2018-11-26 01:19:10.192', 1, '2018-11-26 01:19:10.192', false, 1);
INSERT INTO public.m_status
(id, code, status, is_active, updated_by, updated_date, added_by, added_date, is_deleted, organization_id)
VALUES(2, 'P', 'Pending', true, 1, '2018-03-23 01:55:39.877', 1, '2018-03-23 01:55:39.877', false, 1);
INSERT INTO public.m_status
(id, code, status, is_active, updated_by, updated_date, added_by, added_date, is_deleted, organization_id)
VALUES(3, 'PI', 'Partially Issued', true, 1, '2018-03-23 01:55:46.466', 1, '2018-03-23 01:55:46.466', false, 1);
INSERT INTO public.m_status
(id, code, status, is_active, updated_by, updated_date, added_by, added_date, is_deleted, organization_id)
VALUES(4, 'c', 'Completed', true, 1, '2018-11-22 10:38:46.131', 1, '2018-11-22 10:38:46.131', false, 1);
INSERT INTO public.m_status
(id, code, status, is_active, updated_by, updated_date, added_by, added_date, is_deleted, organization_id)
VALUES(5, 'A', 'Approved', true, 1, '2018-03-23 01:56:02.170', 1, '2018-03-23 01:56:02.170', false, 1);
INSERT INTO public.m_status
(id, code, status, is_active, updated_by, updated_date, added_by, added_date, is_deleted, organization_id)
VALUES(6, 'PA', 'Partially Approved', true, 1, '2018-03-23 16:51:06.000', 1, '2018-03-23 16:51:06.000', false, 1);
INSERT INTO public.m_status
(id, code, status, is_active, updated_by, updated_date, added_by, added_date, is_deleted, organization_id)
VALUES(7, 'R', 'Reject', true, 1, '2018-03-23 16:52:10.000', 1, '2018-03-23 16:52:10.000', false, 1);
INSERT INTO public.m_status
(id, code, status, is_active, updated_by, updated_date, added_by, added_date, is_deleted, organization_id)
VALUES(8, 'Clo', 'Closed', true, 1, '2018-03-23 17:03:21.000', 1, '2018-03-23 17:03:21.000', false, 1);
INSERT INTO public.m_status
(id, code, status, is_active, updated_by, updated_date, added_by, added_date, is_deleted, organization_id)
VALUES(9, 'Ca', 'Cancelled', true, 1, '2018-11-22 11:00:56.993', 1, '2018-11-22 11:00:56.993', false, 1);
INSERT INTO public.m_status
(id, code, status, is_active, updated_by, updated_date, added_by, added_date, is_deleted, organization_id)
VALUES(10, 'PApp', 'Pending Approval', true, 1, '2018-11-26 01:21:11.654', 1, '2018-11-26 01:21:11.654', false, 1);
INSERT INTO public.m_status
(id, code, status, is_active, updated_by, updated_date, added_by, added_date, is_deleted, organization_id)
VALUES(11, 'I', 'Intervention', true, 1, '2018-04-25 14:21:31.000', 1, '2018-04-25 14:21:31.000', false, 1);
INSERT INTO public.m_status
(id, code, status, is_active, updated_by, updated_date, added_by, added_date, is_deleted, organization_id)
VALUES(12, 'IR', 'Intervention Resolved', true, 1, '2018-04-25 14:21:31.000', 1, '2018-04-25 14:21:31.000', false, 1);
INSERT INTO public.m_status
(id, code, status, is_active, updated_by, updated_date, added_by, added_date, is_deleted, organization_id)
VALUES(13, 'AM', 'Active Medication', true, 1, '2018-04-25 14:21:31.000', 1, '2018-04-25 14:21:31.000', false, 1);
INSERT INTO public.m_status
(id, code, status, is_active, updated_by, updated_date, added_by, added_date, is_deleted, organization_id)
VALUES(14, 'Pr', 'Prescribed', true, 1, '2018-04-25 14:21:31.000', 1, '2018-04-25 14:21:31.000', false, 1);
INSERT INTO public.m_status
(id, code, status, is_active, updated_by, updated_date, added_by, added_date, is_deleted, organization_id)
VALUES(15, 'PAP', 'Pending Assign Picker', true, 1, '2018-04-27 05:20:24.000', 1, '2018-04-27 05:20:24.000', false, 1);
INSERT INTO public.m_status
(id, code, status, is_active, updated_by, updated_date, added_by, added_date, is_deleted, organization_id)
VALUES(16, 'Iss', 'Issued', true, 1, '2018-05-03 10:32:04.000', 1, '2018-05-03 10:32:04.000', false, 1);
INSERT INTO public.m_status
(id, code, status, is_active, updated_by, updated_date, added_by, added_date, is_deleted, organization_id)
VALUES(17, 'PIss', 'Pending Issue', true, 1, '2018-05-03 10:44:24.000', 1, '2018-05-03 10:44:24.000', false, 1);
INSERT INTO public.m_status
(id, code, status, is_active, updated_by, updated_date, added_by, added_date, is_deleted, organization_id)
VALUES(18, 'PCompl', 'Partially Completed', true, 1, '2018-05-14 17:08:13.000', 1, '2018-05-14 17:08:13.000', false, 1);



INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(1, 1, 1, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(2, 2, 1, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(3, 1, 2, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(4, 2, 2, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(5, 9, 2, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(6, 8, 2, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(7, 1, 3, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(8, 6, 3, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(9, 8, 3, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(10, 1, 4, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(11, 2, 4, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(12, 9, 4, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(13, 8, 4, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(14, 1, 5, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(15, 2, 5, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(16, 9, 5, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(17, 8, 5, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(18, 2, 6, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(19, 5, 6, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(20, 6, 6, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(21, 9, 6, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(22, 8, 6, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(23, 2, 7, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(24, 6, 7, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(25, 5, 7, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(26, 9, 7, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(27, 8, 7, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(28, 2, 8, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(29, 6, 8, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(30, 5, 8, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(31, 9, 8, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(32, 8, 8, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(33, 1, 10, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(34, 2, 10, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(35, 8, 10, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(36, 9, 10, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(37, 2, 11, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(38, 5, 11, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(39, 6, 11, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(40, 8, 11, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(41, 1, 12, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(42, 2, 12, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(43, 8, 12, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(44, 9, 12, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(45, 10, 12, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(46, 5, 13, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(47, 2, 13, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(48, 8, 13, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(49, 9, 13, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(50, 6, 13, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(51, 1, 14, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(52, 2, 14, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(53, 6, 14, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(54, 5, 14, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(55, 9, 14, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(56, 8, 14, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(57, 1, 15, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(58, 2, 15, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(59, 8, 15, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(60, 1, 16, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(61, 2, 16, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(62, 8, 16, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(63, 9, 16, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(64, 1, 17, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(65, 2, 17, false, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', true, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(66, 9, 17, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(67, 8, 17, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(73, 2, 19, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(74, 4, 19, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(75, 17, 19, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(76, 2, 113, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(77, 3, 20, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(78, 16, 20, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(79, 4, 20, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(80, 5, 21, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(81, 6, 21, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(82, 2, 22, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(83, 11, 22, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(84, 12, 22, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(85, 8, 22, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(86, 9, 22, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(87, 16, 23, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(88, 3, 23, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(89, 8, 23, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(90, 2, 24, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(91, 4, 24, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(92, 18, 24, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(93, 1, 27, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(94, 2, 27, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(95, 9, 27, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(96, 8, 27, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(97, 1, 28, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(98, 2, 28, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(99, 9, 28, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(100, 8, 28, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(101, 4, 29, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(102, 8, 29, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(104, 8, 36, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(105, 5, 36, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(106, 2, 31, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(107, 4, 31, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(108, 2, 32, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(109, 4, 32, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(110, 2, 33, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(111, 4, 33, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(112, 4, 36, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(113, 2, 9, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(114, 2, 18, true, 1, '2018-05-22 04:38:07.000', 1, '2018-05-22 04:38:07.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(115, 9, 18, true, 1, '2018-05-22 04:38:07.000', 1, '2018-05-22 04:38:07.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(116, 8, 18, true, 1, '2018-05-22 04:38:07.000', 1, '2018-05-22 04:38:07.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(118, 2, 34, true, 1, '2018-05-22 05:46:03.000', 1, '2018-05-22 05:46:03.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(119, 4, 34, true, 1, '2018-05-22 05:46:03.000', 1, '2018-05-22 05:46:03.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(120, 1, 37, true, 1, '2018-05-22 11:48:12.000', 1, '2018-05-22 11:48:12.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(121, 2, 37, true, 1, '2018-05-22 11:48:12.000', 1, '2018-05-22 11:48:12.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(122, 8, 37, true, 1, '2018-05-22 11:48:12.000', 1, '2018-05-22 11:48:12.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(123, 18, 19, true, 1, '2018-05-23 06:54:05.000', 1, '2018-05-23 06:54:05.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(124, 1, 40, true, 1, '2018-05-25 17:49:16.000', 1, '2018-05-25 17:49:16.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(125, 2, 40, true, 1, '2018-05-25 17:49:16.000', 1, '2018-05-25 17:49:16.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(126, 8, 40, true, 1, '2018-05-25 17:49:16.000', 1, '2018-05-25 17:49:16.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(127, 9, 40, true, 1, '2018-05-25 17:49:16.000', 1, '2018-05-25 17:49:16.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(128, 2, 38, true, 1, '2018-05-26 09:32:40.000', 1, '2018-05-26 09:32:40.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(129, 8, 38, true, 1, '2018-05-26 09:32:40.000', 1, '2018-05-26 09:32:40.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(130, 9, 38, true, 1, '2018-05-26 09:32:40.000', 1, '2018-05-26 09:32:40.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(131, 18, 38, true, 1, '2018-05-26 09:32:40.000', 1, '2018-05-26 09:32:40.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(132, 1, 38, true, 1, '2018-05-26 10:54:20.000', 1, '2018-05-26 10:54:20.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(133, 2, 41, true, 1, '2018-05-26 15:09:10.000', 1, '2018-05-26 15:09:10.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(134, 4, 41, true, 1, '2018-05-26 15:09:10.000', 1, '2018-05-26 15:09:10.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(135, 8, 41, true, 1, '2018-05-26 15:09:10.000', 1, '2018-05-26 15:09:10.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(136, 9, 41, true, 1, '2018-05-26 15:09:10.000', 1, '2018-05-26 15:09:10.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(137, 2, 42, true, 1, '2018-05-26 16:44:13.000', 1, '2018-05-26 16:44:13.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(138, 8, 42, true, 1, '2018-05-26 16:44:13.000', 1, '2018-05-26 16:44:13.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(139, 9, 42, true, 1, '2018-05-26 16:44:13.000', 1, '2018-05-26 16:44:13.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(140, 1, 41, true, 1, '2018-05-28 04:59:17.000', 1, '2018-05-28 04:59:17.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(141, 1, 44, true, 1, '2018-05-28 09:52:04.000', 1, '2018-05-28 09:52:04.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(142, 2, 44, true, 1, '2018-05-28 09:52:04.000', 1, '2018-05-28 09:52:04.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(143, 8, 44, true, 1, '2018-05-28 09:52:04.000', 1, '2018-05-28 09:52:04.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(144, 9, 44, true, 1, '2018-05-28 09:52:04.000', 1, '2018-05-28 09:52:04.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(145, 2, 45, true, 1, '2018-05-28 09:57:52.000', 1, '2018-05-28 09:57:52.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(146, 8, 45, true, 1, '2018-05-28 09:57:52.000', 1, '2018-05-28 09:57:52.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(147, 9, 45, true, 1, '2018-05-28 09:57:52.000', 1, '2018-05-28 09:57:52.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(148, 2, 46, true, 1, '2018-05-28 19:29:53.000', 1, '2018-05-28 19:29:53.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(149, 8, 46, true, 1, '2018-05-28 19:30:15.000', 1, '2018-05-28 19:30:15.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(150, 9, 46, true, 1, '2018-05-28 19:30:15.000', 1, '2018-05-28 19:30:15.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(151, 2, 21, true, 1, '2018-05-30 09:32:17.000', 1, '2018-05-30 09:32:17.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(152, 4, 47, true, 1, '2018-05-30 11:39:58.000', 1, '2018-05-30 11:39:58.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(153, 8, 47, true, 1, '2018-05-30 11:39:58.000', 1, '2018-05-30 11:39:58.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(154, 2, 48, true, 1, '2018-05-30 11:40:50.000', 1, '2018-05-30 11:40:50.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(155, 4, 48, true, 1, '2018-05-30 11:40:50.000', 1, '2018-05-30 11:40:50.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(156, 2, 49, true, 1, '2018-05-30 11:41:18.000', 1, '2018-05-30 11:41:18.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(157, 8, 49, true, 1, '2018-05-30 11:41:18.000', 1, '2018-05-30 11:41:18.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(158, 1, 50, true, 1, '2018-05-31 17:08:46.000', 1, '2018-05-31 17:08:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(159, 2, 50, true, 1, '2018-05-31 17:08:46.000', 1, '2018-05-31 17:08:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(160, 8, 50, true, 1, '2018-05-31 17:08:46.000', 1, '2018-05-31 17:08:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(162, 2, 51, true, 1, '2018-05-31 17:09:58.000', 1, '2018-05-31 17:09:58.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(163, 8, 51, true, 1, '2018-05-31 17:09:58.000', 1, '2018-05-31 17:09:58.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(164, 1, 52, true, 1, '2018-06-01 05:41:19.000', 1, '2018-06-01 05:41:19.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(165, 2, 52, true, 1, '2018-06-01 05:41:19.000', 1, '2018-06-01 05:41:19.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(166, 8, 52, true, 1, '2018-06-01 05:41:19.000', 1, '2018-06-01 05:41:19.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(167, 1, 53, true, 1, '2018-06-01 11:53:40.000', 1, '2018-06-01 11:53:40.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(168, 2, 53, true, 1, '2018-06-01 11:53:40.000', 1, '2018-06-01 11:53:40.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(169, 8, 53, true, 1, '2018-06-01 11:53:40.000', 1, '2018-06-01 11:53:40.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(170, 1, 54, true, 1, '2018-06-02 06:18:48.000', 1, '2018-06-02 06:18:48.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(171, 2, 54, true, 1, '2018-06-02 06:18:48.000', 1, '2018-06-02 06:18:48.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(172, 8, 54, true, 1, '2018-06-02 06:18:48.000', 1, '2018-06-02 06:18:48.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(173, 2, 55, true, 1, '2018-06-04 09:20:01.000', 1, '2018-06-04 09:20:01.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(174, 9, 55, true, 1, '2018-06-04 09:20:01.000', 1, '2018-06-04 09:20:01.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(175, 8, 55, true, 1, '2018-06-04 09:20:01.000', 1, '2018-06-04 09:20:01.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(176, 2, 56, true, 1, '2018-06-05 06:39:20.000', 1, '2018-06-05 06:39:20.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(177, 4, 56, true, 1, '2018-06-05 06:39:20.000', 1, '2018-06-05 06:39:20.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(178, 1, 56, true, 1, '2018-06-05 09:58:30.000', 1, '2018-06-05 09:58:30.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(179, 2, 56, true, 1, '2018-06-06 07:27:40.000', 1, '2018-06-06 07:27:40.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(180, 4, 56, true, 1, '2018-06-06 07:27:40.000', 1, '2018-06-06 07:27:40.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(181, 2, 57, true, 1, '2018-06-07 05:24:25.000', 1, '2018-06-07 05:24:25.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(182, 4, 57, true, 1, '2018-06-07 05:24:25.000', 1, '2018-06-07 05:24:25.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(183, 2, 58, true, 1, '2018-06-07 05:25:46.000', 1, '2018-06-07 05:25:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(184, 4, 58, true, 1, '2018-06-07 05:25:46.000', 1, '2018-06-07 05:25:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(185, 2, 59, true, 1, '2018-06-07 05:27:53.000', 1, '2018-06-07 05:27:53.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(186, 4, 59, true, 1, '2018-06-07 05:27:53.000', 1, '2018-06-07 05:27:53.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(187, 6, 42, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(188, 2, 60, true, 1, '2018-06-08 10:29:40.000', 1, '2018-06-08 10:29:40.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(189, 8, 60, true, 1, '2018-06-08 10:29:40.000', 1, '2018-06-08 10:29:40.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(190, 2, 61, true, 1, '2018-06-08 10:29:58.000', 1, '2018-06-08 10:29:58.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(191, 4, 61, true, 1, '2018-06-08 10:29:58.000', 1, '2018-06-08 10:29:58.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(192, 2, 62, true, 1, '2018-06-08 10:33:26.000', 1, '2018-06-08 10:33:26.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(193, 8, 62, true, 1, '2018-06-08 10:33:26.000', 1, '2018-06-08 10:33:26.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(194, 1, 60, true, 1, '2018-06-08 11:29:52.000', 1, '2018-06-08 11:29:52.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(195, 1, 62, true, 1, '2018-06-11 05:14:30.891', 1, '2018-06-11 05:14:30.891', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(199, 1, 66, true, 1, '2018-06-11 11:26:32.000', 1, '2018-06-11 11:26:32.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(200, 2, 66, true, 1, '2018-06-11 11:26:32.000', 1, '2018-06-11 11:26:32.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(201, 8, 66, true, 1, '2018-06-11 11:26:32.000', 1, '2018-06-11 11:26:32.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(202, 2, 67, true, 1, '2018-06-11 11:26:32.000', 1, '2018-06-11 11:26:32.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(203, 5, 67, true, 1, '2018-06-11 11:26:32.000', 1, '2018-06-11 11:26:32.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(204, 8, 67, true, 1, '2018-06-11 11:26:32.000', 1, '2018-06-11 11:26:32.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(205, 4, 57, true, 1, '2018-06-12 12:19:49.000', 1, '2018-06-12 12:19:49.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(207, 4, 59, true, 1, '2018-06-12 12:19:49.000', 1, '2018-06-12 12:19:49.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(208, 2, 68, true, 1, '2018-06-14 11:40:05.000', 1, '2018-06-14 11:40:05.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(209, 8, 68, true, 1, '2018-06-14 11:40:05.000', 1, '2018-06-14 11:40:05.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(210, 2, 69, true, 1, '2018-06-14 11:51:19.000', 1, '2018-06-14 11:51:19.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(211, 6, 69, true, 1, '2018-06-14 11:51:19.000', 1, '2018-06-14 11:51:19.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(212, 5, 69, true, 1, '2018-06-14 11:51:19.000', 1, '2018-06-14 11:51:19.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(213, 8, 69, true, 1, '2018-06-14 11:51:19.000', 1, '2018-06-14 11:51:19.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(214, 1, 70, true, 1, '2018-06-18 13:24:56.000', 1, '2018-06-18 13:24:56.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(215, 8, 70, true, 1, '2018-06-18 13:24:56.000', 1, '2018-06-18 13:24:56.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(216, 4, 71, true, 1, '2018-06-18 13:26:00.000', 1, '2018-06-18 13:26:00.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(217, 6, 71, true, 1, '2018-06-18 13:26:00.000', 1, '2018-06-18 13:26:00.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(218, 8, 71, true, 1, '2018-06-18 13:26:00.000', 1, '2018-06-18 13:26:00.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(219, 2, 72, false, 1, '2018-06-18 13:26:39.000', 1, '2018-06-18 13:26:39.000', true, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(220, 4, 72, false, 1, '2018-06-18 13:26:39.000', 1, '2018-06-18 13:26:39.000', true, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(222, 1, 73, true, 1, '2018-06-20 12:15:39.000', 1, '2018-06-20 12:15:39.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(223, 2, 73, false, 1, '2018-06-20 12:15:39.000', 1, '2018-06-20 12:15:39.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(224, 1, 74, true, 1, '2018-06-21 09:50:00.000', 1, '2018-06-21 09:50:00.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(225, 4, 74, false, 1, '2018-06-21 09:50:00.000', 1, '2018-06-21 09:50:00.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(226, 18, 74, false, 1, '2018-06-21 09:50:00.000', 1, '2018-06-21 09:50:00.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(227, 1, 75, true, 1, '2018-06-21 09:50:41.000', 1, '2018-06-21 09:50:41.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(228, 4, 75, true, 1, '2018-06-21 09:50:41.000', 1, '2018-06-21 09:50:41.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(229, 18, 75, true, 1, '2018-06-21 09:50:41.000', 1, '2018-06-21 09:50:41.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(230, 1, 76, true, 1, '2018-06-21 10:43:53.000', 1, '2018-06-21 10:43:53.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(231, 2, 76, true, 1, '2018-06-21 10:43:53.000', 1, '2018-06-21 10:43:53.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(232, 8, 76, true, 1, '2018-06-21 10:43:53.000', 1, '2018-06-21 10:43:53.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(233, 1, 77, true, 1, '2018-06-22 05:59:06.000', 1, '2018-06-22 05:59:06.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(234, 2, 77, true, 1, '2018-06-22 05:59:06.000', 1, '2018-06-22 05:59:06.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(235, 8, 77, true, 1, '2018-06-22 05:59:06.000', 1, '2018-06-22 05:59:06.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(236, 2, 78, true, 1, '2018-06-25 11:29:32.000', 1, '2018-06-25 11:29:32.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(237, 1, 78, true, 1, '2018-06-25 11:29:32.000', 1, '2018-06-25 11:29:32.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(238, 1, 79, true, 1, '2018-06-25 11:29:32.000', 1, '2018-06-25 11:29:32.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(239, 8, 79, true, 1, '2018-06-25 11:29:32.000', 1, '2018-06-25 11:29:32.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(240, 2, 80, true, 1, '2018-06-25 11:57:02.000', 1, '2018-06-25 11:57:02.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(241, 8, 80, true, 1, '2018-06-25 11:57:02.000', 1, '2018-06-25 11:57:02.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(242, 2, 81, false, 1, '2018-06-26 14:08:34.000', 1, '2018-06-26 14:08:34.000', true, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(243, 8, 81, false, 1, '2018-06-26 14:08:34.000', 1, '2018-06-26 14:08:34.000', true, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(244, 2, 82, true, 1, '2018-06-27 06:45:45.000', 1, '2018-06-27 06:45:45.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(245, 8, 82, true, 1, '2018-06-27 06:45:45.000', 1, '2018-06-27 06:45:45.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(246, 1, 83, true, 1, '2018-06-27 12:36:15.000', 1, '2018-06-27 12:36:15.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(247, 8, 83, true, 1, '2018-06-27 12:36:15.000', 1, '2018-06-27 12:36:15.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(248, 2, 84, true, 1, '2018-06-28 12:31:56.000', 1, '2018-06-28 12:31:56.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(249, 1, 84, true, 1, '2018-06-28 12:31:56.000', 1, '2018-06-28 12:31:56.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(250, 6, 84, true, 1, '2018-06-28 12:33:43.000', 1, '2018-06-28 12:33:43.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(251, 6, 78, true, 1, '2018-06-29 08:38:08.000', 1, '2018-06-29 08:38:08.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(252, 9, 78, true, 1, '2018-06-29 08:38:39.000', 1, '2018-06-29 08:38:39.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(253, 2, 85, true, 1, '2018-07-01 13:18:00.000', 1, '2018-07-01 13:18:00.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(254, 5, 85, true, 1, '2018-07-01 13:18:00.000', 1, '2018-07-01 13:18:00.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(255, 8, 85, true, 1, '2018-07-01 13:18:00.000', 1, '2018-07-01 13:18:00.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(256, 2, 86, true, 1, '2018-07-01 13:18:59.000', 1, '2018-07-01 13:18:59.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(257, 8, 85, true, 1, '2018-07-01 13:18:59.000', 1, '2018-07-01 13:18:59.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(258, 2, 87, true, 1, '2018-07-01 13:20:20.000', 1, '2018-07-01 13:20:20.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(259, 8, 85, true, 1, '2018-07-01 13:20:20.000', 1, '2018-07-01 13:20:20.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(260, 2, 88, false, 1, '2018-07-02 11:58:37.000', 1, '2018-07-02 11:58:37.000', true, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(261, 4, 88, false, 1, '2018-07-02 11:58:37.000', 1, '2018-07-02 11:58:37.000', true, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(262, 2, 63, true, 1, '2018-07-02 14:16:36.000', 1, '2018-07-02 14:16:36.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(263, 4, 63, true, 1, '2018-07-02 14:16:36.000', 1, '2018-07-02 14:16:36.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(264, 2, 30, true, 1, '2018-07-02 14:17:47.000', 1, '2018-07-02 14:17:47.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(265, 4, 30, true, 1, '2018-07-02 14:17:47.000', 1, '2018-07-02 14:17:47.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(266, 2, 43, true, 1, '2018-07-02 14:18:18.000', 1, '2018-07-02 14:18:18.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(267, 4, 43, true, 1, '2018-07-02 14:18:18.000', 1, '2018-07-02 14:18:18.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(268, 2, 35, true, 1, '2018-07-03 05:12:26.000', 1, '2018-07-03 05:12:26.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(269, 4, 35, true, 1, '2018-07-03 05:12:26.000', 1, '2018-07-03 05:12:26.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(270, 2, 89, true, 1, '2018-07-05 05:35:18.000', 1, '2018-07-05 05:35:18.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(271, 4, 89, true, 1, '2018-07-05 05:35:18.000', 1, '2018-07-05 05:35:18.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(272, 9, 87, true, 1, '2018-07-12 07:28:39.000', 1, '2018-07-12 07:28:39.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(273, 8, 73, true, 1, '2018-07-17 10:22:13.000', 1, '2018-07-17 10:22:13.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(274, 9, 73, true, 1, '2018-07-17 10:22:20.000', 1, '2018-07-17 10:22:20.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(275, 8, 31, true, 1, '2018-07-17 12:00:15.000', 1, '2018-07-17 12:00:15.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(276, 1, 90, true, 1, '2018-07-23 14:14:24.000', 1, '2018-07-23 14:14:24.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(277, 2, 90, true, 1, '2018-07-23 14:14:24.000', 1, '2018-07-23 14:14:24.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(278, 9, 90, true, 1, '2018-07-23 14:14:24.000', 1, '2018-07-23 14:14:24.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(279, 8, 90, true, 1, '2018-07-23 14:14:24.000', 1, '2018-07-23 14:14:24.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(280, 2, 91, true, 1, '2018-07-23 14:15:57.000', 1, '2018-07-23 14:15:57.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(281, 5, 91, true, 1, '2018-07-23 14:15:57.000', 1, '2018-07-23 14:15:57.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(282, 6, 91, true, 1, '2018-07-23 14:15:57.000', 1, '2018-07-23 14:15:57.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(283, 9, 91, true, 1, '2018-07-23 14:15:57.000', 1, '2018-07-23 14:15:57.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(284, 8, 91, true, 1, '2018-07-23 14:15:57.000', 1, '2018-07-23 14:15:57.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(285, 1, 92, true, 1, '2018-07-23 18:17:49.000', 1, '2018-07-23 18:17:49.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(286, 2, 92, true, 1, '2018-07-23 18:17:49.000', 1, '2018-07-23 18:17:49.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(287, 9, 92, true, 1, '2018-07-23 18:17:49.000', 1, '2018-07-23 18:17:49.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(288, 8, 92, true, 1, '2018-07-23 18:17:49.000', 1, '2018-07-23 18:17:49.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(289, 2, 93, true, 1, '2018-07-23 18:18:10.000', 1, '2018-07-23 18:18:10.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(290, 5, 93, true, 1, '2018-07-23 18:18:10.000', 1, '2018-07-23 18:18:10.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(291, 6, 93, true, 1, '2018-07-23 18:18:10.000', 1, '2018-07-23 18:18:10.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(292, 9, 93, true, 1, '2018-07-23 18:18:10.000', 1, '2018-07-23 18:18:10.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(293, 8, 93, true, 1, '2018-07-23 18:18:10.000', 1, '2018-07-23 18:18:10.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(294, 2, 74, false, 1, '2018-07-25 13:27:55.000', 1, '2018-07-25 13:27:55.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(295, 8, 74, true, 1, '2018-07-25 13:28:10.000', 1, '2018-07-25 13:28:10.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(296, 9, 74, true, 1, '2018-07-25 13:28:24.000', 1, '2018-07-25 13:28:24.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(297, 2, 75, true, 1, '2018-07-26 03:18:25.000', 1, '2018-07-26 03:18:25.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(298, 8, 75, true, 1, '2018-07-26 03:18:25.000', 1, '2018-07-26 03:18:25.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(299, 9, 75, true, 1, '2018-07-26 03:18:25.000', 1, '2018-07-26 03:18:25.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(300, 2, 79, true, 1, '2018-07-26 03:21:04.000', 1, '2018-07-26 03:21:04.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(301, 4, 79, true, 1, '2018-07-26 03:21:04.000', 1, '2018-07-26 03:21:04.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(302, 9, 79, true, 1, '2018-07-26 03:21:04.000', 1, '2018-07-26 03:21:04.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(303, 15, 24, true, 1, '2018-07-26 05:32:23.000', 1, '2018-07-26 05:32:23.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(304, 1, 94, true, 1, '2018-07-30 07:33:06.000', 1, '2018-07-30 07:33:06.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(305, 2, 94, true, 1, '2018-07-30 07:33:11.000', 1, '2018-07-30 07:33:11.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(306, 8, 94, true, 1, '2018-07-30 07:33:15.000', 1, '2018-07-30 07:33:15.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(307, 9, 94, true, 1, '2018-07-30 07:33:20.000', 1, '2018-07-30 07:33:20.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(308, 4, 47, true, 1, '2018-08-01 10:04:28.000', 1, '2018-08-01 10:04:28.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(309, 4, 23, true, 1, '2018-08-01 10:06:02.000', 1, '2018-08-01 10:06:02.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(310, 1, 95, true, 1, '2018-08-01 10:29:36.000', 1, '2018-08-01 10:29:36.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(311, 2, 95, true, 1, '2018-08-01 10:29:36.000', 1, '2018-08-01 10:29:36.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(312, 9, 95, true, 1, '2018-08-01 10:29:36.000', 1, '2018-08-01 10:29:36.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(313, 8, 95, true, 1, '2018-08-01 10:29:36.000', 1, '2018-08-01 10:29:36.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(314, 2, 96, true, 1, '2018-08-01 10:29:53.000', 1, '2018-08-01 10:29:53.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(315, 5, 96, true, 1, '2018-08-01 10:29:53.000', 1, '2018-08-01 10:29:53.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(316, 6, 96, true, 1, '2018-08-01 10:29:53.000', 1, '2018-08-01 10:29:53.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(317, 9, 96, true, 1, '2018-08-01 10:29:53.000', 1, '2018-08-01 10:29:53.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(318, 8, 96, true, 1, '2018-08-01 10:29:53.000', 1, '2018-08-01 10:29:53.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(319, 1, 97, true, 1, '2018-08-01 10:54:26.000', 1, '2018-08-01 10:54:26.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(320, 2, 97, true, 1, '2018-08-01 10:54:57.000', 1, '2018-08-01 10:54:57.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(321, 8, 97, true, 1, '2018-08-01 10:55:11.000', 1, '2018-08-01 10:55:11.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(322, 9, 97, true, 1, '2018-08-01 10:55:16.000', 1, '2018-08-01 10:55:16.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(325, 2, 98, true, 1, '2018-08-06 07:46:37.000', 1, '2018-08-06 07:46:37.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(326, 4, 98, true, 1, '2018-08-06 07:46:44.000', 1, '2018-08-06 07:46:44.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(327, 2, 99, true, 1, '2018-08-06 07:47:03.000', 1, '2018-08-06 07:47:03.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(328, 4, 99, true, 1, '2018-08-06 07:47:09.000', 1, '2018-08-06 07:47:09.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(329, 8, 102, true, 1, '2018-08-06 07:47:23.000', 1, '2018-08-06 07:47:23.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(330, 2, 102, true, 1, '2018-08-06 07:47:27.000', 1, '2018-08-06 07:47:27.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(331, 2, 103, true, 1, '2018-08-06 07:47:32.000', 1, '2018-08-06 07:47:32.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(332, 8, 103, true, 1, '2018-08-06 07:47:37.000', 1, '2018-08-06 07:47:37.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(333, 2, 100, true, 1, '2018-08-06 09:40:48.000', 1, '2018-08-06 09:40:48.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(334, 4, 100, true, 1, '2018-08-06 09:40:53.000', 1, '2018-08-06 09:40:53.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(335, 2, 104, true, 1, '2018-08-07 07:39:09.000', 1, '2018-08-07 07:39:09.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(336, 4, 104, true, 1, '2018-08-07 07:39:09.000', 1, '2018-08-07 07:39:09.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(337, 2, 105, true, 1, '2018-08-07 07:39:14.000', 1, '2018-08-07 07:39:14.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(338, 4, 105, true, 1, '2018-08-07 07:39:14.000', 1, '2018-08-07 07:39:14.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(339, 2, 106, false, 1, '2018-08-08 04:37:26.000', 1, '2018-08-08 04:37:26.000', true, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(340, 4, 106, false, 1, '2018-08-08 04:37:26.000', 1, '2018-08-08 04:37:26.000', true, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(341, 2, 107, true, 1, '2018-08-08 05:06:26.000', 1, '2018-08-08 05:06:26.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(342, 4, 107, true, 1, '2018-08-08 05:06:26.000', 1, '2018-08-08 05:06:26.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(343, 1, 106, true, 1, '2018-08-08 05:08:56.000', 1, '2018-08-08 05:08:56.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(344, 2, 106, true, 1, '2018-08-08 05:08:56.000', 1, '2018-08-08 05:08:56.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(345, 9, 106, true, 1, '2018-08-08 05:08:56.000', 1, '2018-08-08 05:08:56.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(346, 8, 106, true, 1, '2018-08-08 05:08:56.000', 1, '2018-08-08 05:08:56.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(347, 1, 88, true, 1, '2018-08-08 10:07:38.000', 1, '2018-08-08 10:07:38.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(348, 6, 88, true, 1, '2018-08-08 10:07:38.000', 1, '2018-08-08 10:07:38.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(349, 9, 88, true, 1, '2018-08-08 10:07:38.000', 1, '2018-08-08 10:07:38.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(350, 2, 88, true, 1, '2018-08-08 10:07:38.000', 1, '2018-08-08 10:07:38.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(351, 2, 64, false, 1, '2018-08-08 10:54:04.000', 1, '2018-08-08 10:54:04.000', true, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(352, 4, 64, false, 1, '2018-08-08 10:54:04.000', 1, '2018-08-08 10:54:04.000', true, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(353, 2, 72, true, 1, '2018-08-08 10:54:11.000', 1, '2018-08-08 10:54:11.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(354, 4, 72, true, 1, '2018-08-08 10:54:11.000', 1, '2018-08-08 10:54:11.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(355, 2, 81, true, 1, '2018-08-08 10:54:17.000', 1, '2018-08-08 10:54:17.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(356, 4, 81, true, 1, '2018-08-08 10:54:17.000', 1, '2018-08-08 10:54:17.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(359, 2, 65, true, 1, '2018-08-08 11:29:31.000', 1, '2018-08-08 11:29:31.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(360, 4, 65, true, 1, '2018-08-08 11:29:31.000', 1, '2018-08-08 11:29:31.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(361, 2, 107, true, 1, '2018-08-14 06:54:47.000', 1, '2018-08-14 06:54:47.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(362, 7, 107, true, 1, '2018-08-14 06:55:04.000', 1, '2018-08-14 06:55:04.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(363, 7, 101, true, 1, '2018-08-14 06:58:35.000', 1, '2018-08-14 06:58:35.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(364, 2, 101, true, 1, '2018-08-14 06:58:41.000', 1, '2018-08-14 06:58:41.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(365, 4, 101, true, 1, '2018-08-14 06:58:47.000', 1, '2018-08-14 06:58:47.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(366, 9, 101, true, 1, '2018-08-14 06:59:22.000', 1, '2018-08-14 06:59:22.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(367, 18, 101, true, 1, '2018-08-14 06:59:28.000', 1, '2018-08-14 06:59:28.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(368, 1, 108, true, 1, '2018-08-16 05:46:31.000', 1, '2018-08-16 05:46:31.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(369, 2, 108, true, 1, '2018-08-16 05:46:35.000', 1, '2018-08-16 05:46:35.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(370, 4, 108, true, 1, '2018-08-16 05:47:05.000', 1, '2018-08-16 05:47:05.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(371, 8, 108, true, 1, '2018-08-16 05:47:10.000', 1, '2018-08-16 05:47:10.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(372, 9, 108, true, 1, '2018-08-16 05:47:14.000', 1, '2018-08-16 05:47:14.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(373, 2, 109, true, 1, '2018-08-17 13:43:22.000', 1, '2018-08-17 13:43:22.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(374, 4, 109, true, 1, '2018-08-17 13:43:22.000', 1, '2018-08-17 13:43:22.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(375, 8, 109, true, 1, '2018-08-17 13:43:22.000', 1, '2018-08-17 13:43:22.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(376, 9, 109, true, 1, '2018-08-17 13:43:22.000', 1, '2018-08-17 13:43:22.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(377, 1, 109, true, 1, '2018-08-17 13:43:22.000', 1, '2018-08-17 13:43:22.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(378, 2, 110, true, 1, '2018-08-17 13:43:53.000', 1, '2018-08-17 13:43:53.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(379, 8, 110, true, 1, '2018-08-17 13:43:53.000', 1, '2018-08-17 13:43:53.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(380, 9, 110, true, 1, '2018-08-17 13:43:53.000', 1, '2018-08-17 13:43:53.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(381, 9, 53, true, 1, '2018-08-19 16:41:27.000', 1, '2018-08-19 16:41:27.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(382, 1, 111, true, 1, '2018-08-21 06:01:56.000', 1, '2018-08-21 06:01:56.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(383, 2, 111, true, 1, '2018-08-21 06:02:01.000', 1, '2018-08-21 06:02:01.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(384, 8, 111, true, 1, '2018-08-21 06:02:26.000', 1, '2018-08-21 06:02:26.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(385, 9, 111, true, 1, '2018-08-21 06:02:33.000', 1, '2018-08-21 06:02:33.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(386, 2, 112, true, 1, '2018-08-21 06:03:20.000', 1, '2018-08-21 06:03:20.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(387, 4, 112, true, 1, '2018-08-21 06:03:37.000', 1, '2018-08-21 06:03:37.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(388, 9, 77, true, 1, '2018-08-29 05:51:47.000', 1, '2018-08-29 05:51:47.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(389, 9, 86, true, 1, '2018-08-29 09:54:06.000', 1, '2018-08-29 09:54:06.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(390, 5, 86, true, 1, '2018-08-29 09:54:33.000', 1, '2018-08-29 09:54:33.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(391, 5, 89, true, 1, '2018-08-29 09:56:27.000', 1, '2018-08-29 09:56:27.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(392, 4, 9, true, 1, '2018-08-31 14:54:24.000', 1, '2018-08-31 14:54:24.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(393, 1, 22, true, 1, '2018-08-31 20:53:49.000', 1, '2018-08-31 20:53:49.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(394, 18, 32, true, 1, '2018-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(395, 2, 114, true, 1, '2018-09-04 10:59:08.000', 1, '2018-09-04 10:59:08.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(396, 18, 30, true, 1, '2018-09-06 05:42:32.000', 1, '2018-09-06 05:42:32.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(397, 18, 114, true, 1, '2018-09-06 09:48:39.000', 1, '2018-09-06 09:48:39.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(398, 18, 15, true, 1, '2018-09-14 05:59:58.000', 1, '2018-09-14 05:59:58.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(399, 4, 15, true, 1, '2018-09-14 06:00:02.000', 1, '2018-09-14 06:00:02.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(400, 1, 120, true, 1, '2018-09-14 06:00:02.000', 1, '2018-09-14 06:00:02.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(401, 8, 120, true, 1, '2018-09-14 06:00:02.000', 1, '2018-09-14 06:00:02.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(402, 1, 121, true, 1, '2018-09-14 06:00:02.000', 1, '2018-09-14 06:00:02.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(403, 8, 121, true, 1, '2018-09-14 06:00:02.000', 1, '2018-09-14 06:00:02.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(404, 1, 115, true, 1, '2018-09-14 06:00:02.000', 1, '2018-09-14 06:00:02.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(405, 2, 115, true, 1, '2018-09-14 06:00:02.000', 1, '2018-09-14 06:00:02.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(406, 1, 98, true, 1, '2018-08-06 07:46:44.000', 1, '2018-08-06 07:46:44.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(407, 2, 122, true, 1, '2018-08-06 07:46:37.000', 1, '2018-08-06 07:46:37.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(408, 1, 122, true, 1, '2018-08-06 07:46:44.000', 1, '2018-08-06 07:46:44.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(409, 4, 128, true, 1, '2018-08-06 07:46:44.000', 1, '2018-08-06 07:46:44.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(410, 2, 128, true, 1, '2018-08-06 07:46:37.000', 1, '2018-08-06 07:46:37.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(411, 1, 129, true, 1, '2018-08-06 07:46:44.000', 1, '2018-08-06 07:46:44.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(412, 2, 129, true, 1, '2018-08-06 07:46:37.000', 1, '2018-08-06 07:46:37.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(413, 4, 130, true, 1, '2018-12-17 11:16:38.000', 1, '2018-12-17 11:16:38.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(414, 8, 130, true, 1, '2018-12-17 11:16:38.000', 1, '2018-12-17 11:16:38.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(415, 1, 99, true, 1, '2018-12-17 11:16:38.000', 1, '2018-12-17 11:16:38.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(416, 1, 118, true, 1, '2018-12-17 11:16:38.000', 1, '2018-12-17 11:16:38.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(417, 2, 118, true, 1, '2018-12-17 11:16:38.000', 1, '2018-12-17 11:16:38.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(418, 8, 118, true, 1, '2018-12-17 11:16:38.000', 1, '2018-12-17 11:16:38.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(419, 1, 139, true, 1, '2019-03-02 14:28:20.000', 1, '2019-03-02 14:28:20.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(420, 2, 139, true, 1, '2019-03-02 14:28:39.000', 1, '2019-03-02 14:28:39.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(421, 2, 131, true, 1, '2019-03-04 11:42:30.000', 1, '2019-03-04 11:42:30.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(422, 4, 131, true, 1, '2019-03-04 11:44:45.000', 1, '2019-03-04 11:44:45.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(423, 2, 145, true, 1, '2019-03-14 18:03:29.000', 1, '2019-03-14 18:03:29.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(424, 8, 145, true, 1, '2019-03-14 18:04:02.000', 1, '2019-03-14 18:04:02.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(425, 2, 130, true, 1, '2019-03-19 16:03:53.000', 1, '2019-03-19 16:03:53.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(426, 9, 76, true, 1, '2019-09-11 03:43:53.000', 1, '2019-09-11 03:43:53.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(427, 9, 9, true, 1, '2019-05-21 11:52:46.000', 1, '2018-05-21 11:52:46.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(428, 10, 53, false, 1, '2019-10-16 00:00:00.000', 1, '2019-10-16 00:00:00.000', false, 1);
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(429, 10, 58, false, 1, '2019-10-17 00:00:00.000', 1, '2019-10-17 00:00:00.000', false, 1);






INSERT INTO inventory.m_indent_type
(id, organization_id, unit_id, code, "type", is_active, updated_by, updated_date, added_by, added_date, is_deleted)
VALUES(1, 1, 1, 'M', 'Manual', true, 1, '2018-03-23 20:21:22.000', 1, '2018-03-23 20:21:22.000', false);


INSERT INTO inventory.m_indent_type
(id, organization_id, unit_id, code, "type", is_active, updated_by, updated_date, added_by, added_date, is_deleted)
VALUES(2, 1, 1, 'A', 'Auto', true, 1, '2018-03-23 20:21:30.000', 1, '2018-03-23 20:21:30.000', false);

INSERT INTO billing.m_card_type_master
(card_type_id, card_type_name, status, unit_id, orgnisation_id, created_date, created_by, updated_date, updated_by)
VALUES(1, 'Master Card', 'A', 1, 1, '2018-02-19 18:19:07.654', 1, '2018-02-19 18:19:07.654', 1);
INSERT INTO billing.m_card_type_master
(card_type_id, card_type_name, status, unit_id, orgnisation_id, created_date, created_by, updated_date, updated_by)
VALUES(2, 'Visa', 'A', 1, 1, '2018-05-17 17:00:50.184', 1, '2018-05-17 17:00:50.184', 1);
INSERT INTO billing.m_card_type_master
(card_type_id, card_type_name, status, unit_id, orgnisation_id, created_date, created_by, updated_date, updated_by)
VALUES(3, 'American Express', 'A', 1, 1, '2018-02-19 18:19:07.654', 1, '2018-02-19 18:19:07.654', 1);
INSERT INTO billing.m_card_type_master
(card_type_id, card_type_name, status, unit_id, orgnisation_id, created_date, created_by, updated_date, updated_by)
VALUES(4, 'RuPay', 'A', 1, 1, '2018-05-17 17:00:50.184', 1, '2018-05-17 17:00:50.184', 1);
INSERT INTO billing.m_card_type_master
(card_type_id, card_type_name, status, unit_id, orgnisation_id, created_date, created_by, updated_date, updated_by)
VALUES(5, 'Diners Club', 'A', 1, 1, '2018-05-17 17:00:50.184', 1, '2018-05-17 17:00:50.184', 1);
INSERT INTO billing.m_card_type_master
(card_type_id, card_type_name, status, unit_id, orgnisation_id, created_date, created_by, updated_date, updated_by)
VALUES(6, 'Maestro ', 'A', 1, 1, '2018-05-17 17:00:50.184', 1, '2018-05-17 17:00:50.184', 1);


INSERT INTO adt.m_hierarchy_master
(hierarchy_id, hierarchy_desc, created_by, created_date, status, updated_by, updated_date, hierarchy_code, organization_id)
VALUES(1, '1', 1, '2017-11-07 23:05:57.302', 'A', 1, '2017-11-07 23:05:57.302', 'One', 1);
INSERT INTO adt.m_hierarchy_master
(hierarchy_id, hierarchy_desc, created_by, created_date, status, updated_by, updated_date, hierarchy_code, organization_id)
VALUES(2, '2', 1, '2017-11-07 23:06:18.089', 'A', 1, '2017-11-07 23:06:18.089', 'Two', 1);
INSERT INTO adt.m_hierarchy_master
(hierarchy_id, hierarchy_desc, created_by, created_date, status, updated_by, updated_date, hierarchy_code, organization_id)
VALUES(3, '3', 1, '2017-11-14 01:47:41.927', 'A', 1, '2017-11-14 01:47:41.927', 'Three', 1);
INSERT INTO adt.m_hierarchy_master
(hierarchy_id, hierarchy_desc, created_by, created_date, status, updated_by, updated_date, hierarchy_code, organization_id)
VALUES(4, '4', 1, '2017-11-14 01:48:39.009', 'A', 1, '2017-11-14 01:48:39.009', 'Four', 1);
INSERT INTO adt.m_hierarchy_master
(hierarchy_id, hierarchy_desc, created_by, created_date, status, updated_by, updated_date, hierarchy_code, organization_id)
VALUES(5, '5', 1, '2017-11-14 01:49:39.537', 'A', 1, '2017-11-14 01:49:39.537', 'Five', NULL);


INSERT INTO adt.m_housekeeping_status_master
(housekeeping_status_id, organization_id, unit_id, housekeeping_status_code, housekeeping_status_desc, status)
VALUES(1, 1, 1, 'PEN', 'Pending', 'A');
INSERT INTO adt.m_housekeeping_status_master
(housekeeping_status_id, organization_id, unit_id, housekeeping_status_code, housekeeping_status_desc, status)
VALUES(2, 1, 1, 'ASND', 'Assigned', 'A');
INSERT INTO adt.m_housekeeping_status_master
(housekeeping_status_id, organization_id, unit_id, housekeeping_status_code, housekeeping_status_desc, status)
VALUES(3, 1, 1, 'CLN', 'Cleaned', 'A');
INSERT INTO adt.m_housekeeping_status_master
(housekeeping_status_id, organization_id, unit_id, housekeeping_status_code, housekeeping_status_desc, status)
VALUES(4, 1, 1, 'ACC', 'Accepted By Nursing', 'A');
INSERT INTO adt.m_housekeeping_status_master
(housekeeping_status_id, organization_id, unit_id, housekeeping_status_code, housekeeping_status_desc, status)
VALUES(5, 1, 1, 'REJ', 'Rejected By Nursing And Resent For Housekeeping', 'A');

INSERT INTO adt.m_discharge_status_master
(discharge_code, discharge_desc, discharge_status_id)
VALUES('MD', 'Marked For Discharge', 1);
INSERT INTO adt.m_discharge_status_master
(discharge_code, discharge_desc, discharge_status_id)
VALUES('RB', 'Ready For Billing', 2);
INSERT INTO adt.m_discharge_status_master
(discharge_code, discharge_desc, discharge_status_id)
VALUES('FD', 'Final Discharge', 3);
INSERT INTO adt.m_discharge_status_master
(discharge_code, discharge_desc, discharge_status_id)
VALUES('P', 'Pending', 4);
INSERT INTO adt.m_discharge_status_master
(discharge_code, discharge_desc, discharge_status_id)
VALUES('D', 'Discharged', 5);

INSERT INTO public.m_currency_master
(currency_id, currency_name, country_id, currency_symbol, status, unit_id, orgnisation_id, created_date, created_by, updated_date, updated_by, currency_code, sub_currency)
VALUES(1, 'Rupees', 1, 'INR', 'A', 1, 1, '2018-05-16 07:03:32.102', 1, '2018-05-16 07:03:32.102', 1, NULL, NULL);

INSERT INTO public.m_order_status_master
(order_status_id, order_code, order_desc)
VALUES(1, 'P', 'Pending');
INSERT INTO public.m_order_status_master
(order_status_id, order_code, order_desc)
VALUES(2, 'I', 'Inprocess');
INSERT INTO public.m_order_status_master
(order_status_id, order_code, order_desc)
VALUES(3, 'R', 'Render');
INSERT INTO public.m_order_status_master
(order_status_id, order_code, order_desc)
VALUES(4, 'C', 'Cancel');

INSERT INTO public.m_marital_status_master
(marital_status_id, marital_status_code, marital_status_name, created_by, created_date, status, updated_by, updated_date, organization_id)
VALUES(1, 'SNGL', 'Single', 1, '2017-11-08 12:05:33.000', 'A', 1, '2018-06-05 17:43:40.000', 1);
INSERT INTO public.m_marital_status_master
(marital_status_id, marital_status_code, marital_status_name, created_by, created_date, status, updated_by, updated_date, organization_id)
VALUES(2, 'MRD', 'Married', 1, '2017-11-08 12:05:43.000', 'A', 1, '2018-06-05 17:43:48.000', 1);




INSERT INTO adt.m_bed_status_master
(bed_status_id, bed_status_code, bed_status_desc, created_by, created_date, status, updated_by, updated_date, organization_id)
VALUES(1, 'PND', 'Pending', 1, '2017-10-11 12:26:44.000', 'A', 1, '2017-10-11 12:26:44.000', 1);
INSERT INTO adt.m_bed_status_master
(bed_status_id, bed_status_code, bed_status_desc, created_by, created_date, status, updated_by, updated_date, organization_id)
VALUES(5, 'OC', 'Occupied', 1, '2017-10-12 19:30:13.000', 'A', 1, '2017-10-12 19:30:13.000', 1);
INSERT INTO adt.m_bed_status_master
(bed_status_id, bed_status_code, bed_status_desc, created_by, created_date, status, updated_by, updated_date, organization_id)
VALUES(3, 'VACRDY', 'Vacant and Ready to Receive Patient', 1, '2017-10-11 12:28:12.000', 'A', 1, '2017-10-11 12:28:12.000', 1);
INSERT INTO adt.m_bed_status_master
(bed_status_id, bed_status_code, bed_status_desc, created_by, created_date, status, updated_by, updated_date, organization_id)
VALUES(2, 'UNHOU', 'Under Housekeeping', 1, '2017-10-11 12:27:41.000', 'A', 1, '2017-10-11 12:27:41.000', 1);
INSERT INTO adt.m_bed_status_master
(bed_status_id, bed_status_code, bed_status_desc, created_by, created_date, status, updated_by, updated_date, organization_id)
VALUES(4, 'RES', 'Reserved', 1, '2017-10-11 12:51:58.000', 'A', 1, '2017-10-11 12:51:58.000', 1);
INSERT INTO adt.m_bed_status_master
(bed_status_id, bed_status_code, bed_status_desc, created_by, created_date, status, updated_by, updated_date, organization_id)
VALUES(6, 'VAC', 'Vacant', 1, '2017-12-13 21:47:51.956', 'A', 1, '2017-12-13 21:47:51.956', 1);


INSERT INTO public.m_uom_type
(id, organization_id, unit_id, code, "type", is_active, updated_by, updated_date, added_by, added_date, is_deleted)
VALUES(1, 1, 1, 'P', 'Purchase', true, 1, '2018-06-07 14:51:59.000', 1, '2018-06-07 14:51:59.000', false);
INSERT INTO public.m_uom_type
(id, organization_id, unit_id, code, "type", is_active, updated_by, updated_date, added_by, added_date, is_deleted)
VALUES(2, 1, 1, 'S', 'Stock', true, 1, '2018-06-07 14:51:59.000', 1, '2018-06-07 14:51:59.000', false);
INSERT INTO public.m_uom_type
(id, organization_id, unit_id, code, "type", is_active, updated_by, updated_date, added_by, added_date, is_deleted)
VALUES(3, 1, 1, 'D', 'Dispense', true, 1, '2018-06-07 14:51:59.000', 1, '2018-06-07 14:51:59.000', false);


INSERT INTO emr.m_vital_master
(vital_id, vital_name, min_value, max_value, status, unit, remark, created_by, created_date, updated_by, updated_date, unit_id, organization_id, vital_icon_path, is_nursing_vital, vital_order, vital_order_opd)
VALUES(1, 'Weight', '0', '100', 'A', 'kg', 'weight', 1, '2016-11-28 12:57:00.415', NULL, '2017-12-05 04:48:17.716', 1, 1, 'resources/assets/images/icon/scale.png', false, NULL, NULL);
INSERT INTO emr.m_vital_master
(vital_id, vital_name, min_value, max_value, status, unit, remark, created_by, created_date, updated_by, updated_date, unit_id, organization_id, vital_icon_path, is_nursing_vital, vital_order, vital_order_opd)
VALUES(2, 'Height', '0', '7', 'A', 'cm', 'cm', 1, '2016-11-28 12:57:25.823', NULL, '2017-12-05 04:48:17.716', 1, 1, 'resources/assets/images/icon/height.png', false, NULL, NULL);
INSERT INTO emr.m_vital_master
(vital_id, vital_name, min_value, max_value, status, unit, remark, created_by, created_date, updated_by, updated_date, unit_id, organization_id, vital_icon_path, is_nursing_vital, vital_order, vital_order_opd)
VALUES(12, 'Oxygen Saturation (% on L/min O2)', '0', NULL, 'I', '% on L/min O2', 'Oxygen Saturation ', 1, '2016-11-28 12:57:00.000', NULL, '2016-11-28 12:57:00.000', 1, 1, NULL, true, 8, NULL);
INSERT INTO emr.m_vital_master
(vital_id, vital_name, min_value, max_value, status, unit, remark, created_by, created_date, updated_by, updated_date, unit_id, organization_id, vital_icon_path, is_nursing_vital, vital_order, vital_order_opd)
VALUES(13, 'Pain Score', '0', NULL, 'I', '0 No Pain , 1-3 Mild Pain , 4-6 Moderate Pain , 7-9 Severe , 10 Worst Pain', 'Pain Score', 1, '2016-11-28 12:58:19.000', NULL, '2017-12-05 04:48:17.000', 1, 1, NULL, true, 9, NULL);
INSERT INTO emr.m_vital_master
(vital_id, vital_name, min_value, max_value, status, unit, remark, created_by, created_date, updated_by, updated_date, unit_id, organization_id, vital_icon_path, is_nursing_vital, vital_order, vital_order_opd)
VALUES(999, 'Input', '0', NULL, 'I', 'ml', 'Input', 1, '2017-06-27 00:28:41.000', NULL, '2017-12-05 04:48:17.000', 1, 1, NULL, true, 999, NULL);
INSERT INTO emr.m_vital_master
(vital_id, vital_name, min_value, max_value, status, unit, remark, created_by, created_date, updated_by, updated_date, unit_id, organization_id, vital_icon_path, is_nursing_vital, vital_order, vital_order_opd)
VALUES(1000, 'Output', '0', NULL, 'I', 'ml', 'Output', 1, '2017-06-27 00:28:41.000', NULL, '2017-12-05 04:48:17.000', 1, 1, NULL, true, 1000, NULL);
INSERT INTO emr.m_vital_master
(vital_id, vital_name, min_value, max_value, status, unit, remark, created_by, created_date, updated_by, updated_date, unit_id, organization_id, vital_icon_path, is_nursing_vital, vital_order, vital_order_opd)
VALUES(7, 'Blood Presure(Diastolic)', '0', '100', 'A', 'mmHg', 'Blood Presure(Diastolic)', 1, '2017-06-28 12:00:16.461', NULL, '2017-12-05 04:48:17.716', 1, 1, 'resources/assets/images/icon/thermometer.png', true, 4, NULL);
INSERT INTO emr.m_vital_master
(vital_id, vital_name, min_value, max_value, status, unit, remark, created_by, created_date, updated_by, updated_date, unit_id, organization_id, vital_icon_path, is_nursing_vital, vital_order, vital_order_opd)
VALUES(8, 'BMI', '0', '30', 'A', 'kg/m2', 'Body Mass Index', 1, '2017-06-29 00:28:41.921', NULL, '2017-12-05 04:48:17.716', 1, 1, 'resources/assets/images/icon/body-mass-index.png', false, NULL, NULL);
INSERT INTO emr.m_vital_master
(vital_id, vital_name, min_value, max_value, status, unit, remark, created_by, created_date, updated_by, updated_date, unit_id, organization_id, vital_icon_path, is_nursing_vital, vital_order, vital_order_opd)
VALUES(3, 'Blood Pressure(Systolic)', '0', '100', 'A', 'mmHg', 'Blood Pressure(Systolic)', 1, '2016-11-28 12:57:54.015', NULL, '2017-12-05 04:48:17.716', 1, 1, 'resources/assets/images/icon/thermometer.png', true, 3, NULL);
INSERT INTO emr.m_vital_master
(vital_id, vital_name, min_value, max_value, status, unit, remark, created_by, created_date, updated_by, updated_date, unit_id, organization_id, vital_icon_path, is_nursing_vital, vital_order, vital_order_opd)
VALUES(4, 'Pulse Rate', '0', '200', 'A', 'bpm', 'Pulse Rate', 1, '2016-11-28 12:58:19.734', NULL, '2017-12-05 04:48:17.716', 1, 1, 'resources/assets/images/icon/pulse.png', true, 5, NULL);
INSERT INTO emr.m_vital_master
(vital_id, vital_name, min_value, max_value, status, unit, remark, created_by, created_date, updated_by, updated_date, unit_id, organization_id, vital_icon_path, is_nursing_vital, vital_order, vital_order_opd)
VALUES(5, 'Blood Sugar', '0', '200', 'A', 'mg/dl', 'Blood Sugar', 1, '2016-11-28 12:59:13.646', NULL, '2017-12-05 04:48:17.716', 1, 1, 'resources/assets/images/icon/sphygmomanometer.png', true, 10, NULL);
INSERT INTO emr.m_vital_master
(vital_id, vital_name, min_value, max_value, status, unit, remark, created_by, created_date, updated_by, updated_date, unit_id, organization_id, vital_icon_path, is_nursing_vital, vital_order, vital_order_opd)
VALUES(6, 'Temprature', '0', '150', 'A', '°C', 'Temprature', 1, '2016-11-28 12:59:46.110', NULL, '2017-12-05 04:48:17.716', 1, 1, 'resources/assets/images/icon/themprature.png', true, 1, NULL);
INSERT INTO emr.m_vital_master
(vital_id, vital_name, min_value, max_value, status, unit, remark, created_by, created_date, updated_by, updated_date, unit_id, organization_id, vital_icon_path, is_nursing_vital, vital_order, vital_order_opd)
VALUES(10, 'Respiration  ', '0', NULL, 'I', 'Breaths per min', 'Respiration  ', 1, '2016-11-28 12:57:00.000', NULL, '2016-11-28 12:57:00.000', 1, 1, NULL, true, 6, NULL);
INSERT INTO emr.m_vital_master
(vital_id, vital_name, min_value, max_value, status, unit, remark, created_by, created_date, updated_by, updated_date, unit_id, organization_id, vital_icon_path, is_nursing_vital, vital_order, vital_order_opd)
VALUES(11, 'Oxygen Saturation (% on RA)', '0', NULL, 'I', '% on RA', 'Oxygen Saturation ', 1, '2016-11-28 12:57:00.000', NULL, '2016-11-28 12:57:00.000', 1, 1, NULL, true, 7, NULL);


INSERT INTO public.m_gender_master
(gender_id, gender_code, gender_name, created_by, created_date, status, updated_by, updated_date, organization_id, gender_prefix)
VALUES(1, 'M', 'Male', 1, '2017-11-07 02:52:00.844', 'A', 1, '2018-06-23 12:16:17.000', 1, 'M');

INSERT INTO public.m_gender_master
(gender_id, gender_code, gender_name, created_by, created_date, status, updated_by, updated_date, organization_id, gender_prefix)
VALUES(2, 'F', 'Female', 1, '2017-11-07 02:52:00.844', 'A', 1, '2018-06-23 12:16:16.000', 1, 'F');


INSERT INTO public.m_prefix_master
(prefix_id, prefix_desc, created_by, created_date, status, updated_by, updated_date, prefix_code, gender_id, organization_id)
VALUES(1, 'Mr.', 1, '2019-02-02 13:11:10.000', 'A', 1, '2019-02-05 16:29:22.000', 'PRE_01', 1, 1);



INSERT INTO public.m_day_master
(day_id, "day", created_by, created_date, status, updated_by, updated_date, day_code, organization_id)
VALUES(1, 'Monday', 1, '2017-11-03 11:39:59.649', 'A', 1, '2017-11-03 11:39:59.649', 'MON', 1);
INSERT INTO public.m_day_master
(day_id, "day", created_by, created_date, status, updated_by, updated_date, day_code, organization_id)
VALUES(2, 'Tuesday', 1, '2017-11-07 22:04:54.132', 'A', 1, '2017-11-07 22:04:54.132', 'TUES', 1);
INSERT INTO public.m_day_master
(day_id, "day", created_by, created_date, status, updated_by, updated_date, day_code, organization_id)
VALUES(3, 'Wednesday', 1, '2017-11-07 22:05:08.206', 'A', 1, '2017-11-07 22:05:08.206', 'WED', 1);
INSERT INTO public.m_day_master
(day_id, "day", created_by, created_date, status, updated_by, updated_date, day_code, organization_id)
VALUES(4, 'Thursday', 1, '2017-11-07 22:05:58.615', 'A', 1, '2017-11-07 22:05:58.615', 'THUR', 1);
INSERT INTO public.m_day_master
(day_id, "day", created_by, created_date, status, updated_by, updated_date, day_code, organization_id)
VALUES(5, 'Friday', 1, '2017-11-07 22:06:27.254', 'A', 1, '2017-11-07 22:06:27.254', 'FRI', 1);
INSERT INTO public.m_day_master
(day_id, "day", created_by, created_date, status, updated_by, updated_date, day_code, organization_id)
VALUES(6, 'Saturday', 1, '2017-11-07 22:06:50.285', 'A', 1, '2017-11-07 22:06:50.285', 'SAT', 1);
INSERT INTO public.m_day_master
(day_id, "day", created_by, created_date, status, updated_by, updated_date, day_code, organization_id)
VALUES(7, 'Sunday', 1, '2017-11-07 22:07:05.130', 'A', 1, '2017-11-07 22:07:05.130', 'SUN', 1);

INSERT INTO public.t_lab_configuration
(t_lab_config_id, lab_speciality_id, biochemistry_id, hematology_id, histopathology_id, cystopathology_id, serology_id, is_prebarcode_generantion, status, created_by, created_date, updated_by, updated_date, organisation_id, unit_id, microbiology_id, is_pin_applicable)
VALUES(1, NULL, NULL, NULL, NULL, NULL, NULL, 'Y', 'A', NULL, '2019-02-04 15:02:21.000', 1, '2019-02-04 15:02:21.000', 1, 1, 8, false);


INSERT INTO public.m_application_configuration
(id, organization_id, unit_id, item_name, default_tax_id, issue_material_picker_required, is_active, updated_by, updated_date, added_by, added_date, is_deleted, tax_inclusive, assign_picker_required, is_markup, markup_up)
VALUES(1, 1, 1, false, 1, false, true, 1, '2019-01-17 12:56:09.200', 1, '2018-10-11 20:19:24.165', false, true, true, true, true);

INSERT INTO public.m_asset_category
(id, organization_id, unit_id, asset_type_id, code, category, is_active, updated_by, updated_date, added_by, added_date, is_deleted)
VALUES(1, 1, 1, 1, 'Pharma', 'Pharmaceutical', true, 1, '2019-02-06 08:44:52.262', 1, '2019-02-05 09:38:02.020', false);

INSERT INTO public.m_payment_entitlement_master
(payment_entitlement_id, payment_entitlement_code, payment_entitlement_desc, created_by, created_date, status, updated_by, updated_date, organization_id)
VALUES(1, 'Self', 'Self Entitlement', 1, '2017-11-08 02:32:45.316', 'A', 1, '2018-10-17 17:36:30.000', 1);
INSERT INTO public.m_payment_entitlement_master
(payment_entitlement_id, payment_entitlement_code, payment_entitlement_desc, created_by, created_date, status, updated_by, updated_date, organization_id)
VALUES(2, 'Insurance', 'Insurance', 1, '2017-11-08 02:32:59.469', 'A', 1, '2018-11-01 17:29:26.000', 1);
INSERT INTO public.m_payment_entitlement_master
(payment_entitlement_id, payment_entitlement_code, payment_entitlement_desc, created_by, created_date, status, updated_by, updated_date, organization_id)
VALUES(3, 'Company', 'Company', 1, '2017-11-08 02:33:23.165', 'A', 1, '2017-11-08 02:33:23.165', 1);

INSERT INTO public.m_payment_mode_master
(payment_mode_id, payment_mode_code, payment_mode_desc, created_by, created_date, status, updated_by, updated_date, organization_id)
VALUES(1, 'cash', 'Cash', 1, '2018-02-22 11:03:01.536', 'A', 1, '2018-02-22 11:03:01.536', 1);
INSERT INTO public.m_payment_mode_master
(payment_mode_id, payment_mode_code, payment_mode_desc, created_by, created_date, status, updated_by, updated_date, organization_id)
VALUES(2, 'cheque', 'Cheque', 1, '2018-02-22 11:03:34.915', 'A', 1, '2018-02-22 11:03:34.915', 1);
INSERT INTO public.m_payment_mode_master
(payment_mode_id, payment_mode_code, payment_mode_desc, created_by, created_date, status, updated_by, updated_date, organization_id)
VALUES(3, 'Card', 'Card Payment', 1, '2018-02-22 11:03:55.491', 'A', 1, '2018-02-22 11:03:55.491', 1);
INSERT INTO public.m_payment_mode_master
(payment_mode_id, payment_mode_code, payment_mode_desc, created_by, created_date, status, updated_by, updated_date, organization_id)
VALUES(4, 'debit card', 'Debit Card', 1, '2018-02-22 11:04:25.302', 'A', 1, '2018-02-22 11:04:25.302', 1);
INSERT INTO public.m_payment_mode_master
(payment_mode_id, payment_mode_code, payment_mode_desc, created_by, created_date, status, updated_by, updated_date, organization_id)
VALUES(5, 'Mpesa', 'Mpesa', 1, '2018-02-22 11:04:25.302', 'A', 1, '2018-02-22 11:04:25.302', 1);
INSERT INTO public.m_payment_mode_master
(payment_mode_id, payment_mode_code, payment_mode_desc, created_by, created_date, status, updated_by, updated_date, organization_id)
VALUES(6, 'Loyalty Card', 'Loyalty Card', 1, '2018-02-22 11:03:01.000', 'A', 1, '2018-02-22 11:03:01.000', 1);
INSERT INTO public.m_payment_mode_master
(payment_mode_id, payment_mode_code, payment_mode_desc, created_by, created_date, status, updated_by, updated_date, organization_id)
VALUES(7, 'Available Fund', 'Available Fund', 1, '2019-02-07 18:34:42.214', 'A', 1, '2019-02-07 18:34:42.214', 1);
INSERT INTO public.m_payment_mode_master
(payment_mode_id, payment_mode_code, payment_mode_desc, created_by, created_date, status, updated_by, updated_date, organization_id)
VALUES(8, 'E-Transfer', 'E-Transfer', 1, '2019-02-07 18:34:42.214', 'A', 1, '2019-02-07 18:34:42.214', 1);

INSERT INTO public.m_visit_type_master
(visit_type_id, visit_type_code, created_by, created_date, status, updated_by, updated_date, visit_type_name, organization_id)
VALUES(1, 'OP', 1, '2017-11-08 02:27:57.805', 'A', 5, '2019-04-24 15:50:16.000', 'Out Patient', 1);
INSERT INTO public.m_visit_type_master
(visit_type_id, visit_type_code, created_by, created_date, status, updated_by, updated_date, visit_type_name, organization_id)
VALUES(2, 'IP', 1, '2017-11-08 02:29:57.524', 'A', 1, '2017-11-08 02:29:57.524', 'In Patient', 1);
INSERT INTO public.m_visit_type_master
(visit_type_id, visit_type_code, created_by, created_date, status, updated_by, updated_date, visit_type_name, organization_id)
VALUES(3, 'DC', 1, '2017-11-08 02:30:19.352', 'A', 5, '2019-04-24 15:41:47.000', 'Day Care', 1);
INSERT INTO public.m_visit_type_master
(visit_type_id, visit_type_code, created_by, created_date, status, updated_by, updated_date, visit_type_name, organization_id)
VALUES(4, 'ER', 1, '2017-11-08 02:30:36.327', 'A', 253, '2019-09-15 12:54:04.000', 'Emergency', 1);


INSERT INTO lab.m_hour_master
(hour_id, hours)
VALUES(1, 24);
INSERT INTO lab.m_hour_master
(hour_id, hours)
VALUES(2, 48);
INSERT INTO lab.m_hour_master
(hour_id, hours)
VALUES(3, 72);
INSERT INTO lab.m_hour_master
(hour_id, hours)
VALUES(4, 1);
INSERT INTO lab.m_hour_master
(hour_id, hours)
VALUES(5, 2);


INSERT INTO lab.m_day_master
(day_master_id, status, created_by, created_date, updated_by, updated_date, org_id, "day")
VALUES(1, 'A', 1, '04:11:08', 1, '04:11:14', 1, '1');
INSERT INTO lab.m_day_master
(day_master_id, status, created_by, created_date, updated_by, updated_date, org_id, "day")
VALUES(2, 'A', 1, '04:11:08', 1, '04:11:15', 1, '2');
INSERT INTO lab.m_day_master
(day_master_id, status, created_by, created_date, updated_by, updated_date, org_id, "day")
VALUES(3, 'A', 1, '04:11:09', 1, '04:11:15', 1, '3');
INSERT INTO lab.m_day_master
(day_master_id, status, created_by, created_date, updated_by, updated_date, org_id, "day")
VALUES(4, 'A', 1, '04:11:10', 1, '04:11:16', 1, '4');
INSERT INTO lab.m_day_master
(day_master_id, status, created_by, created_date, updated_by, updated_date, org_id, "day")
VALUES(5, 'A', 1, '04:11:11', 1, '04:11:17', 1, '5');
INSERT INTO lab.m_day_master
(day_master_id, status, created_by, created_date, updated_by, updated_date, org_id, "day")
VALUES(6, 'A', 1, '04:11:11', 1, '04:11:17', 1, '6');
INSERT INTO lab.m_day_master
(day_master_id, status, created_by, created_date, updated_by, updated_date, org_id, "day")
VALUES(7, 'A', 1, '04:11:12', 1, '04:11:18', 1, '7');
INSERT INTO lab.m_day_master
(day_master_id, status, created_by, created_date, updated_by, updated_date, org_id, "day")
VALUES(8, 'A', 1, '04:11:13', 1, '04:11:20', 1, '8');


INSERT INTO adt.m_reason_type_master
(reason_type_id, organization_id, unit_id, reason_type_code, reason_type_desc, created_by, created_date, updated_by, updated_date, status)
VALUES(5, 1, NULL, 'Transfer Rejection', 'Transfer Rejection', 1, '2019-02-07 17:30:41.000', 1, '2019-02-07 17:30:41.000', 'A');
INSERT INTO adt.m_reason_type_master
(reason_type_id, organization_id, unit_id, reason_type_code, reason_type_desc, created_by, created_date, updated_by, updated_date, status)
VALUES(7, 1, NULL, 'IPD', 'IPD', 1, '2018-09-29 11:03:39.000', 1, '2018-09-29 11:03:39.000', 'A');
INSERT INTO adt.m_reason_type_master
(reason_type_id, organization_id, unit_id, reason_type_code, reason_type_desc, created_by, created_date, updated_by, updated_date, status)
VALUES(8, 1, NULL, 'Billing', 'Billing', 1, '2018-09-29 11:03:39.000', 1, '2018-09-29 11:03:39.000', 'A');
INSERT INTO adt.m_reason_type_master
(reason_type_id, organization_id, unit_id, reason_type_code, reason_type_desc, created_by, created_date, updated_by, updated_date, status)
VALUES(9, 1, NULL, 'Transfer', 'Transfer', 1, '2018-09-29 11:03:39.000', 1, '2018-09-29 11:03:39.000', 'A');
INSERT INTO adt.m_reason_type_master
(reason_type_id, organization_id, unit_id, reason_type_code, reason_type_desc, created_by, created_date, updated_by, updated_date, status)
VALUES(10, 1, NULL, 'Linen', 'Linen', 1, '2018-09-29 11:03:39.000', 1, '2018-09-29 11:03:39.000', 'A');
INSERT INTO adt.m_reason_type_master
(reason_type_id, organization_id, unit_id, reason_type_code, reason_type_desc, created_by, created_date, updated_by, updated_date, status)
VALUES(11, 1, NULL, 'HouseKeeping', 'HouseKeeping', 1, '2018-09-29 11:03:39.000', 1, '2018-09-29 11:03:39.000', 'A');
INSERT INTO adt.m_reason_type_master
(reason_type_id, organization_id, unit_id, reason_type_code, reason_type_desc, created_by, created_date, updated_by, updated_date, status)
VALUES(12, 1, NULL, 'Admission', 'Admission', 1, '2018-09-29 11:03:39.000', 1, '2018-09-29 11:03:39.000', 'A');


INSERT INTO public.m_dashboard_master
(dashboard_id, dashboard_desc, dashboard_link, created_by, created_date, status, updated_by, updated_date, organization_id)
VALUES(1, 'Dashboard 1', '/dashBoard', 1, '2019-05-17 14:24:29.000', 'A', 1, '2019-05-17 14:24:29.000', 1);
INSERT INTO public.m_dashboard_master
(dashboard_id, dashboard_desc, dashboard_link, created_by, created_date, status, updated_by, updated_date, organization_id)
VALUES(2, 'Dashboard 2', '/dashBoard2', 1, '2019-07-19 14:24:29.000', 'A', 1, '2019-07-19 14:24:29.000', 1);

INSERT INTO lab.m_incubation_time_master
(incubation_time_id, incubation_desc, status, created_by, created_date, updated_by, updated_date, org_id)
VALUES(1, 'Hours', 'A', 1, '2018-02-08 11:32:59.916', 1, '2018-02-08 11:32:59.916', 1);
INSERT INTO lab.m_incubation_time_master
(incubation_time_id, incubation_desc, status, created_by, created_date, updated_by, updated_date, org_id)
VALUES(2, 'Day', 'A', 1, '2018-02-08 11:32:59.916', 1, '2018-02-08 11:32:59.916', 1);


INSERT INTO lab.m_age_type_group
(age_type_grp_id, age_type_grp_name, age_type_grp_status)
VALUES(1, 'Day', 'A');
INSERT INTO lab.m_age_type_group
(age_type_grp_id, age_type_grp_name, age_type_grp_status)
VALUES(2, 'Month', 'A');
INSERT INTO lab.m_age_type_group
(age_type_grp_id, age_type_grp_name, age_type_grp_status)
VALUES(3, 'Year', 'A');

INSERT INTO lab.m_patient_status_master
(status_id, status_name)
VALUES(2, 'Arrived');
INSERT INTO lab.m_patient_status_master
(status_id, status_name)
VALUES(1, 'NotArrived');
INSERT INTO lab.m_patient_status_master
(status_id, status_name)
VALUES(3, 'Repat Patient');

INSERT INTO lab.m_send_throgh_master
(send_through_id, send_throug_desc, status, created_by, created_date, updated_by, updated_date)
VALUES(1, 'By Hand', 'A', 1, '2018-09-06 14:12:36.684', 1, '2018-09-06 14:12:36.684');
INSERT INTO lab.m_send_throgh_master
(send_through_id, send_throug_desc, status, created_by, created_date, updated_by, updated_date)
VALUES(2, 'Fax', 'A', 1, '2018-09-06 14:12:36.684', 1, '2018-09-06 14:12:36.684');
INSERT INTO lab.m_send_throgh_master
(send_through_id, send_throug_desc, status, created_by, created_date, updated_by, updated_date)
VALUES(3, 'Courier', 'A', 1, '2018-09-06 14:12:36.684', 1, '2018-09-06 14:12:36.684');


INSERT INTO lab.m_priority_master
(priority_id, priority_name, status, created_by, created_date, updated_by, updated_date, color_code)
VALUES(1, 'High', 'A', 1, '2017-10-17 17:32:10.955', 1, '2017-10-17 17:32:10.955', '#FF0000');
INSERT INTO lab.m_priority_master
(priority_id, priority_name, status, created_by, created_date, updated_by, updated_date, color_code)
VALUES(2, 'Normal', 'A', 1, '2017-10-17 17:32:15.294', 1, '2017-10-17 17:32:15.294', '#0000FF');
INSERT INTO lab.m_priority_master
(priority_id, priority_name, status, created_by, created_date, updated_by, updated_date, color_code)
VALUES(3, '-', 'A', 1, '2017-10-17 17:32:24.172', 1, '2017-10-17 17:32:24.172', '#088A08');

INSERT INTO lab.m_rejection_master
(rejection_id, rejection_name, status, created_by, created_date, updated_by, updated_date, org_id)
VALUES(1, 'Insufficient Blood', 'A', 1, '2017-10-31 14:57:15.944', 1, '2017-10-31 14:57:15.944', 1);
INSERT INTO lab.m_rejection_master
(rejection_id, rejection_name, status, created_by, created_date, updated_by, updated_date, org_id)
VALUES(3, 'Container Damaged', 'A', 1, '2017-10-31 14:57:27.301', 1, '2017-10-31 14:57:27.301', 1);
INSERT INTO lab.m_rejection_master
(rejection_id, rejection_name, status, created_by, created_date, updated_by, updated_date, org_id)
VALUES(2, 'Clots in Blood', 'A', 1, '2017-10-31 14:57:19.447', 1, '2017-10-31 14:57:19.447', 1);



INSERT INTO lab.m_microlab_priority_master
(microlab_priority_id, status, created_by, created_date, updated_by, updated_date, org_id, priority_desc)
VALUES(1, 'A', 1, '2018-01-31 11:27:38.820', 1, '2018-01-31 11:27:38.820', 1, 'A');
INSERT INTO lab.m_microlab_priority_master
(microlab_priority_id, status, created_by, created_date, updated_by, updated_date, org_id, priority_desc)
VALUES(3, 'A', 1, '2018-01-31 11:29:15.006', 1, '2018-01-31 11:29:15.006', 1, 'B');
INSERT INTO lab.m_microlab_priority_master
(microlab_priority_id, status, created_by, created_date, updated_by, updated_date, org_id, priority_desc)
VALUES(4, 'A', 1, '2018-01-31 11:29:15.006', 1, '2018-01-31 11:29:15.006', 1, 'C');



INSERT INTO lab.m_tat_time_master
(tat_time_id, tat_time_name, tat_time_status, created_by, created_date, updated_by, updated_date, org_id)
VALUES(2, 'Hour', 'A', 1, '2017-11-21 01:34:27.296', 1, '2017-11-21 01:34:27.296', 1);
INSERT INTO lab.m_tat_time_master
(tat_time_id, tat_time_name, tat_time_status, created_by, created_date, updated_by, updated_date, org_id)
VALUES(3, 'Day', 'A', 1, '2017-11-21 01:34:49.805', 1, '2017-11-21 01:34:49.805', 1);
INSERT INTO lab.m_tat_time_master
(tat_time_id, tat_time_name, tat_time_status, created_by, created_date, updated_by, updated_date, org_id)
VALUES(1, 'Minute', 'A', 1, '2017-11-21 01:34:06.187', 1, '2017-11-21 01:34:06.187', 1);


INSERT INTO lab.m_refrange_type_master
(refrange_type_id, refrange_type_name, status, org_id, org_unit_id, created_by, updated_by, updated_date, created_date)
VALUES(1, 'Reference Value', 'A', 1, 1, 1, 1, '2018-05-29 16:20:53.545', '2018-05-29 16:20:53.545');
INSERT INTO lab.m_refrange_type_master
(refrange_type_id, refrange_type_name, status, org_id, org_unit_id, created_by, updated_by, updated_date, created_date)
VALUES(2, 'Textual Range', 'A', 1, 1, 1, 1, '2018-05-29 16:20:54.207', '2018-05-29 16:20:57.937');
INSERT INTO lab.m_refrange_type_master
(refrange_type_id, refrange_type_name, status, org_id, org_unit_id, created_by, updated_by, updated_date, created_date)
VALUES(3, 'MultiValued Range', 'A', 1, 1, 1, 1, '2018-05-29 16:20:55.442', '2018-05-29 16:20:55.442');


INSERT INTO public.m_company_type_master
(company_type_id, company_type_code, company_type_desc, status, created_by, created_date, updated_by, updated_date, unit_id, organization_id)
VALUES(1, '001', 'Self', 'A', 1, '2016-09-23 12:29:50.734', NULL, '2017-12-06 03:01:29.045', 1, 1);
INSERT INTO public.m_company_type_master
(company_type_id, company_type_code, company_type_desc, status, created_by, created_date, updated_by, updated_date, unit_id, organization_id)
VALUES(2, '002', 'Insurance', 'A', 1, '2016-09-23 12:29:51.554', NULL, '2017-12-06 03:01:29.045', 1, 1);
INSERT INTO public.m_company_type_master
(company_type_id, company_type_code, company_type_desc, status, created_by, created_date, updated_by, updated_date, unit_id, organization_id)
VALUES(3, '003', 'Credit Company', 'A', 1, '2016-09-23 12:29:52.376', NULL, '2017-12-06 03:01:29.045', 1, 1);



INSERT INTO opd.m_slot_status_master
(slot_status_id, slot_status_code, slot_status_desc, status, created_by, created_date, updated_by, updated_date)
VALUES(2, 'CAN', 'Cancelled', 'A', 1, '2018-03-13 10:49:19.655', 1, '2018-03-13 10:49:19.655');
INSERT INTO opd.m_slot_status_master
(slot_status_id, slot_status_code, slot_status_desc, status, created_by, created_date, updated_by, updated_date)
VALUES(3, 'RES', 'Reschedulled', 'A', 1, '2018-03-13 10:49:53.555', 1, '2018-03-13 10:49:53.555');
INSERT INTO opd.m_slot_status_master
(slot_status_id, slot_status_code, slot_status_desc, status, created_by, created_date, updated_by, updated_date)
VALUES(4, 'BKD', 'Booked', 'A', 1, '2018-03-13 10:50:18.820', 1, '2018-03-13 10:50:18.820');
INSERT INTO opd.m_slot_status_master
(slot_status_id, slot_status_code, slot_status_desc, status, created_by, created_date, updated_by, updated_date)
VALUES(1, 'VAC', 'Vacant', 'A', 1, '2018-03-13 10:49:06.850', 1, '2018-03-13 10:49:06.850');
INSERT INTO opd.m_slot_status_master
(slot_status_id, slot_status_code, slot_status_desc, status, created_by, created_date, updated_by, updated_date)
VALUES(5, 'BLK', 'Blocked', 'A', 1, '2018-05-24 14:42:05.890', 1, '2018-05-24 14:42:05.890');


INSERT INTO opd.m_appointment_status_master
(appointment_status_id, appointment_status_code, appointment_status_desc, status, created_by, created_date, updated_by, updated_date, unit_id, organization_id)
VALUES(1, 'PEN', 'Pending', 'A', 1, '2018-03-13 10:47:24.669', 1, '2018-03-13 10:47:24.669', 1, 1);
INSERT INTO opd.m_appointment_status_master
(appointment_status_id, appointment_status_code, appointment_status_desc, status, created_by, created_date, updated_by, updated_date, unit_id, organization_id)
VALUES(2, 'CAN', 'Cancelled', 'A', 1, '2018-03-13 10:47:37.341', 1, '2018-03-13 10:47:37.341', 1, 1);
INSERT INTO opd.m_appointment_status_master
(appointment_status_id, appointment_status_code, appointment_status_desc, status, created_by, created_date, updated_by, updated_date, unit_id, organization_id)
VALUES(3, 'RES', 'Rescheduled', 'A', 1, '2018-03-13 10:47:48.701', 1, '2018-03-13 10:47:48.701', 1, 1);
INSERT INTO opd.m_appointment_status_master
(appointment_status_id, appointment_status_code, appointment_status_desc, status, created_by, created_date, updated_by, updated_date, unit_id, organization_id)
VALUES(4, 'BKD', 'Booked', 'A', 1, '2018-03-13 10:48:41.488', 1, '2018-03-13 10:48:41.488', 1, 1);
INSERT INTO opd.m_appointment_status_master
(appointment_status_id, appointment_status_code, appointment_status_desc, status, created_by, created_date, updated_by, updated_date, unit_id, organization_id)
VALUES(5, 'CHKIN', 'Checked In', 'A', 1, '2018-03-15 18:17:29.231', 1, '2018-03-15 18:17:29.231', 1, 1);
INSERT INTO opd.m_appointment_status_master
(appointment_status_id, appointment_status_code, appointment_status_desc, status, created_by, created_date, updated_by, updated_date, unit_id, organization_id)
VALUES(8, 'INPRO', 'In Process', 'A', 1, '2018-07-10 11:26:51.294', 1, '2018-07-10 11:26:51.294', 1, 1);
INSERT INTO opd.m_appointment_status_master
(appointment_status_id, appointment_status_code, appointment_status_desc, status, created_by, created_date, updated_by, updated_date, unit_id, organization_id)
VALUES(7, 'NSH', 'No show', 'A', 1, '2018-03-16 16:06:18.251', 1, '2018-03-16 16:06:18.251', 1, 1);
INSERT INTO opd.m_appointment_status_master
(appointment_status_id, appointment_status_code, appointment_status_desc, status, created_by, created_date, updated_by, updated_date, unit_id, organization_id)
VALUES(9, 'Closed', 'Closed', 'A', 1, '2019-02-21 12:44:42.184', 1, '2019-02-21 12:44:42.184', 1, 1);
INSERT INTO opd.m_appointment_status_master
(appointment_status_id, appointment_status_code, appointment_status_desc, status, created_by, created_date, updated_by, updated_date, unit_id, organization_id)
VALUES(6, 'CMPLT', 'Completed', 'A', 1, '2018-03-16 16:03:26.432', 1, '2018-03-16 16:03:26.432', 1, 1);

INSERT INTO public.t_company_master
(company_id, company_code, company_desc, company_type_id, credit_limit, report_path, contact_person_name, address, country_id, state_id, city_id, zipcode, mobile_no, residence_phone_no, updated_by, updated_date, created_date, created_by, unit_id, organization_id, status, fax_no, email_id, start_date, end_date, payment_terms, ar_code_id, contract_no, district_id, area_id, area, credit_limit_days, hospital_id, marathi_company_desc, person_address)
VALUES(1, NULL, 'Self', 1, NULL, NULL, NULL, NULL, 1, 19, 635, NULL, NULL, NULL, 1, '2017-10-06 17:23:36.939', '2017-12-06 02:56:38.996', 1, 1, 1, 'A', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO adt.m_admission_type_master
(admission_type_id, admission_type_code, admission_type_desc, created_by, created_date, organization_id, status, updated_by, updated_date)
VALUES(2, 'MA', 'Medical Admission', 1, '2017-11-08 12:31:25.000', 1, 'A', 1, '2017-11-08 12:31:25.000');
INSERT INTO adt.m_admission_type_master
(admission_type_id, admission_type_code, admission_type_desc, created_by, created_date, organization_id, status, updated_by, updated_date)
VALUES(1, 'SA', 'Surgical Admission', 1, '2017-11-08 12:31:10.000', 1, 'A', 1, '2017-11-08 12:31:10.000');


INSERT INTO billing.m_billing_status_master
(bill_status_id, bill_status_desc, created_by, created_date, status, bill_status_code, updated_date, updated_by, unit_id, orgnisation_id)
VALUES(1, 'Pending', 1, '2018-02-27 10:17:49.720', 'A', NULL, '2018-02-27 10:17:49.720', 1, 1, 1);
INSERT INTO billing.m_billing_status_master
(bill_status_id, bill_status_desc, created_by, created_date, status, bill_status_code, updated_date, updated_by, unit_id, orgnisation_id)
VALUES(2, 'OnHold', 1, '2018-02-27 10:18:07.760', 'A', NULL, '2018-02-27 10:18:07.760', 1, 1, 1);
INSERT INTO billing.m_billing_status_master
(bill_status_id, bill_status_desc, created_by, created_date, status, bill_status_code, updated_date, updated_by, unit_id, orgnisation_id)
VALUES(3, 'Settled', 1, '2018-02-27 10:18:25.643', 'A', NULL, '2018-02-27 10:18:25.643', 1, 1, 1);



INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(1, 'Test Ordered', 'A', 1, 1, '2017-10-24 17:35:03.924', '2017-10-24 17:35:03.924');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(2, 'Sample Collected', 'A', 1, 1, '2017-10-24 17:35:03.924', '2017-10-24 17:35:03.924');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(3, 'Sample Received', 'A', 1, 1, '2017-10-24 17:35:03.924', '2017-10-24 17:35:03.924');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(4, 'Sample Recollected', 'A', 1, 1, '2017-10-24 17:35:03.924', '2017-10-24 17:35:03.924');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(5, 'Sample under Processing', 'A', 1, 1, '2017-10-24 17:35:03.924', '2017-10-24 17:35:03.924');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(6, 'Result Entry done', 'A', 1, 1, '2017-10-24 17:35:03.924', '2017-10-24 17:35:03.924');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(7, 'Retesting', 'A', 1, 1, '2017-10-24 17:35:03.924', '2017-10-24 17:35:03.924');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(8, 'Report Released', 'A', 1, 1, '2017-10-24 17:35:03.924', '2017-10-24 17:35:03.924');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(9, 'Partial Report Released', 'A', 1, 1, '2017-10-24 17:35:03.924', '2017-10-24 17:35:03.924');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(10, 'Provisional Report Released', 'A', 1, 1, '2017-10-24 17:35:03.924', '2017-10-24 17:35:03.924');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(11, 'Final Report Released', 'A', 1, 1, '2017-10-24 17:35:03.924', '2017-10-24 17:35:03.924');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(12, 'Send To Central Receiving', 'A', 1, 1, '2017-10-24 17:35:03.924', '2017-10-24 17:35:03.924');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(13, 'Send To Department', 'A', 1, 1, '2017-11-29 22:19:38.346', '2017-11-29 22:19:38.346');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(14, 'Department Accepted', 'A', 1, 1, '2017-12-05 04:14:06.782', '2017-12-05 04:14:06.782');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(15, 'Department Rejected', 'A', 1, 1, '2017-12-05 04:14:48.631', '2017-12-05 04:14:48.631');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(16, 'Send To Report Entry', 'A', 1, 1, '2017-12-08 01:24:50.793', '2017-12-08 01:24:50.793');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(17, 'Result Validation Done', 'A', 1, 1, '2017-12-20 02:12:52.147', '2017-12-20 02:12:52.147');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(18, 'Release for Sensitivity', 'A', 1, 1, '2017-12-20 02:12:52.147', '2017-12-20 02:12:52.147');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(19, ' Accepted', 'A', 1, 1, '2018-01-03 10:44:52.145', '2018-01-03 10:44:52.145');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(20, ' Rejected', 'A', 1, 1, '2018-01-03 10:44:52.145', '2018-01-03 10:44:52.145');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(21, 'Specimen Accepted', 'A', 1, 1, '2018-03-09 12:59:17.511', '2018-03-09 12:59:17.511');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(22, 'Gross Created', 'A', 1, 1, '2018-03-09 12:59:17.511', '2018-03-09 12:59:17.511');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(23, 'Block Created', 'A', 1, 1, '2018-03-09 12:59:17.511', '2018-03-09 12:59:17.511');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(24, 'Slide Created', 'A', 1, 1, '2018-03-09 12:59:17.511', '2018-03-09 12:59:17.511');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(25, 'Out Source Sample', 'A', 1, 1, '2018-05-04 12:59:14.511', '2018-05-04 12:59:14.511');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(26, 'Send For Incubation Obervation', 'A', 1, 1, '2018-06-07 10:11:30.365', '2018-06-07 10:11:30.365');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(27, 'During Objservation', 'A', 1, 1, '2018-06-07 10:11:30.365', '2018-06-07 10:11:30.365');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(28, 'Incubation Objservation Completed', 'A', 1, 1, '2018-06-07 10:11:30.365', '2018-06-07 10:11:30.365');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(29, 'MacroScopic Examination', 'A', 1, 1, '2018-06-07 10:11:30.365', '2018-06-07 10:11:30.365');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(30, 'MacroScopic Examination Completed', 'A', 1, 1, '2018-06-07 10:11:30.365', '2018-06-07 10:11:30.365');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(31, 'During Biochemical Result Entry', 'A', 1, 1, '2018-06-07 10:11:30.365', '2018-06-07 10:11:30.365');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(32, 'Biochemical Result Entry', 'A', 1, 1, '2018-06-07 10:11:30.365', '2018-06-07 10:11:30.365');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(33, 'MicroScopic Examination', 'A', 1, 1, '2018-05-04 12:59:14.000', '2018-05-04 12:59:14.000');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(34, 'Sensitivity Test', 'A', 1, 1, '2018-05-04 12:59:14.000', '2018-05-04 12:59:14.000');
INSERT INTO lab.m_test_status_master
(test_status_id, status_name, status, created_by, updated_by, updated_date, created_date)
VALUES(35, 'Sensitivity Test Check', 'A', 1, 1, '2018-05-04 12:59:14.511', '2018-05-04 12:59:14.511');


INSERT INTO service.m_package_type_master
(package_type_id, package_type, unit_id, organisation_id, created_date, created_by, updated_date, updated_by, staus, package_type_code)
VALUES(1, 'EHC Package', 1, 1, '2018-05-30 14:27:48.854', 1, '2018-05-30 14:27:48.854', NULL, 'A', '000A1');
INSERT INTO service.m_package_type_master
(package_type_id, package_type, unit_id, organisation_id, created_date, created_by, updated_date, updated_by, staus, package_type_code)
VALUES(2, 'Either or Package', 1, 1, '2018-05-30 14:28:21.680', 1, '2018-05-30 14:28:21.680', NULL, 'A', '000A2');
INSERT INTO service.m_package_type_master
(package_type_id, package_type, unit_id, organisation_id, created_date, created_by, updated_date, updated_by, staus, package_type_code)
VALUES(3, 'Package with CAP', 1, 1, '2018-05-30 14:28:52.816', 1, '2018-05-30 14:28:52.816', NULL, 'A', '000A3');
INSERT INTO service.m_package_type_master
(package_type_id, package_type, unit_id, organisation_id, created_date, created_by, updated_date, updated_by, staus, package_type_code)
VALUES(4, 'All Inclusive Package', 1, 1, '2018-05-30 14:29:20.933', 1, '2018-05-30 14:29:20.933', NULL, 'A', '000A4');
INSERT INTO service.m_package_type_master
(package_type_id, package_type, unit_id, organisation_id, created_date, created_by, updated_date, updated_by, staus, package_type_code)
VALUES(5, 'Multiencounter Package', 1, 1, '2018-05-30 14:29:20.933', 1, '2018-05-30 14:29:20.933', NULL, 'A', '000A4');



INSERT INTO lab.m_sample_status_master
(sample_status_id, status_name, status, created_by, created_date, updated_by, updated_date, org_id)
VALUES(1, 'Pending', 'A', 1, '2017-10-30 18:25:04.642', 1, '2017-10-30 18:25:33.496', 1);
INSERT INTO lab.m_sample_status_master
(sample_status_id, status_name, status, created_by, created_date, updated_by, updated_date, org_id)
VALUES(2, 'Work in Progress', 'A', 1, '2017-10-30 18:25:26.828', 1, '2017-10-30 18:25:35.831', 1);



INSERT INTO adt.m_priority_master
(priority_id, organization_id, unit_id, priority_code, priority_desc, created_by, created_date, updated_by, updated_date, status)
VALUES(1, 1, 1, 'HG', 'High', 1, '2017-11-08 01:11:02.468', 253, '2019-08-28 15:49:48.000', 'A');
INSERT INTO adt.m_priority_master
(priority_id, organization_id, unit_id, priority_code, priority_desc, created_by, created_date, updated_by, updated_date, status)
VALUES(2, 1, 1, 'NR', 'Normal', 1, '2017-11-08 01:11:21.398', 253, '2019-09-14 12:13:47.000', 'A');
INSERT INTO adt.m_priority_master
(priority_id, organization_id, unit_id, priority_code, priority_desc, created_by, created_date, updated_by, updated_date, status)
VALUES(3, 1, 1, 'LW', 'Low', 1, '2017-11-08 01:11:37.771', 1, '2017-11-08 01:11:37.771', 'A');


INSERT INTO pharmacy.m_sale_type
(id, organization_id, unit_id, code, "type", is_active, updated_by, updated_date, added_by, added_date, is_deleted)
VALUES(1, 1, 1, 'IP', 'IP', true, 1, '2018-06-15 05:13:15.000', 1, '2018-06-15 05:13:15.000', false);
INSERT INTO pharmacy.m_sale_type
(id, organization_id, unit_id, code, "type", is_active, updated_by, updated_date, added_by, added_date, is_deleted)
VALUES(2, 1, 1, 'OP', 'OP', true, 1, '2018-06-15 05:13:15.000', 1, '2018-06-15 05:13:15.000', false);
INSERT INTO pharmacy.m_sale_type
(id, organization_id, unit_id, code, "type", is_active, updated_by, updated_date, added_by, added_date, is_deleted)
VALUES(3, 1, 1, 'W', 'Walk-in', true, 1, '2018-06-15 05:13:15.000', 1, '2018-06-15 05:13:15.000', false);
INSERT INTO pharmacy.m_sale_type
(id, organization_id, unit_id, code, "type", is_active, updated_by, updated_date, added_by, added_date, is_deleted)
VALUES(4, 1, 1, 'DC', 'Day Care', true, 1, '2019-06-17 15:53:15.000', 1, '2019-06-17 15:53:15.000', false);


INSERT INTO billing.m_transaction_type_master
(transaction_type_name, transaction_type_desc, status, created_by, created_date, updated_by, updated_date, transaction_type_id, org_id, unit_id)
VALUES('NEFT', 'National Electronic Fund Transfer', 'A', 1, '2019-06-05 18:11:04.000', 1, '2019-06-05 18:11:04.000', 1, 1, 1);
INSERT INTO billing.m_transaction_type_master
(transaction_type_name, transaction_type_desc, status, created_by, created_date, updated_by, updated_date, transaction_type_id, org_id, unit_id)
VALUES('RTGS', 'Real Time Gross Settlement', 'A', 1, '2019-06-05 18:11:04.000', 1, '2019-06-05 18:11:04.000', 2, 1, 1);


INSERT INTO adt.m_reason_for_discharge_master
(discharge_type_id, discharge_type_name, discharge_type_code, status, created_by, created_date, updated_by, updated_date, organization_id)
VALUES(4, 'Absconding', 'Absconding', 'A', 1, '2018-05-30 14:54:54.000', 1, '2018-11-12 16:44:24.000', 1);
INSERT INTO adt.m_reason_for_discharge_master
(discharge_type_id, discharge_type_name, discharge_type_code, status, created_by, created_date, updated_by, updated_date, organization_id)
VALUES(2, 'DAMA', 'DAMA', 'A', 1, '2017-11-08 12:32:46.000', 1, '2019-05-13 11:57:14.000', 1);
INSERT INTO adt.m_reason_for_discharge_master
(discharge_type_id, discharge_type_name, discharge_type_code, status, created_by, created_date, updated_by, updated_date, organization_id)
VALUES(3, 'Death', 'Death', 'A', 1, '2017-11-17 05:12:36.239', 1, '2017-12-06 14:04:02.000', 1);
INSERT INTO adt.m_reason_for_discharge_master
(discharge_type_id, discharge_type_name, discharge_type_code, status, created_by, created_date, updated_by, updated_date, organization_id)
VALUES(1, 'Normal(Planned)', 'Normal', 'A', 1, '2017-11-08 12:32:37.000', 1, '2017-11-08 12:32:37.000', 1);


INSERT INTO public.m_loyalty_card_configuration
(card_configuration_id, min_start_amount, threshold_amount, status, created_by, created_date, updated_by, updated_date, organisation_id, unit_id, is_deleted, min_recharge_amount)
VALUES(1, 1000.00, 5000.00, 'A', 1, '2019-10-23 13:20:52.000', 1, '2019-10-23 13:20:52.000', 1, 1, 'N', 1000.00);

INSERT INTO otims.m_procedure_role_master
(procedure_role_code, procedure_role_name, procedure_role_desc, status, org_id, unit_id, created_by, created_date, updated_by, updated_date, procedure_role_id)
VALUES('001', 'Surgeon', 'Surgeon', 'A', 1, 1, 1, NULL, NULL, NULL, 1);
INSERT INTO otims.m_procedure_role_master
(procedure_role_code, procedure_role_name, procedure_role_desc, status, org_id, unit_id, created_by, created_date, updated_by, updated_date, procedure_role_id)
VALUES('002', 'Ass. Surgeon', 'Ass. Surgeon', 'A', 1, 1, 1, NULL, NULL, NULL, 2);
INSERT INTO otims.m_procedure_role_master
(procedure_role_code, procedure_role_name, procedure_role_desc, status, org_id, unit_id, created_by, created_date, updated_by, updated_date, procedure_role_id)
VALUES('004', 'Visiting doctor', 'Visiting doctor', 'A', 1, 1, 1, NULL, NULL, NULL, 4);
INSERT INTO otims.m_procedure_role_master
(procedure_role_code, procedure_role_name, procedure_role_desc, status, org_id, unit_id, created_by, created_date, updated_by, updated_date, procedure_role_id)
VALUES('003', 'Anaesthetist', 'Anaesthetist', 'A', 1, 1, 1, NULL, NULL, NULL, 3);


	
INSERT INTO adt.m_transfer_status_master
(transfer_status_id, transfer_status_code, transfer_status_desc, organization_id)
VALUES(1, 'P', 'Pending', 1);
INSERT INTO adt.m_transfer_status_master
(transfer_status_id, transfer_status_code, transfer_status_desc, organization_id)
VALUES(2, 'R', 'Requested', 1);
INSERT INTO adt.m_transfer_status_master
(transfer_status_id, transfer_status_code, transfer_status_desc, organization_id)
VALUES(3, 'D', 'Request sent to doctor', 1);
INSERT INTO adt.m_transfer_status_master
(transfer_status_id, transfer_status_code, transfer_status_desc, organization_id)
VALUES(4, 'A', 'Request sent to ADT', 1);
INSERT INTO adt.m_transfer_status_master
(transfer_status_id, transfer_status_code, transfer_status_desc, organization_id)
VALUES(6, 'RDT', 'Ready For Transfer', 1);
INSERT INTO adt.m_transfer_status_master
(transfer_status_id, transfer_status_code, transfer_status_desc, organization_id)
VALUES(8, 'ABT', 'Abort', 1);
INSERT INTO adt.m_transfer_status_master
(transfer_status_id, transfer_status_code, transfer_status_desc, organization_id)
VALUES(9, 'TRA', 'Transferd', 1);
INSERT INTO adt.m_transfer_status_master
(transfer_status_id, transfer_status_code, transfer_status_desc, organization_id)
VALUES(7, 'AST', 'All set for transfer', 1);
INSERT INTO adt.m_transfer_status_master
(transfer_status_id, transfer_status_code, transfer_status_desc, organization_id)
VALUES(10, 'ICUT', 'ICU Transfer', 1);
INSERT INTO adt.m_transfer_status_master
(transfer_status_id, transfer_status_code, transfer_status_desc, organization_id)
VALUES(11, 'TRA', 'Accepted', 1);
INSERT INTO adt.m_transfer_status_master
(transfer_status_id, transfer_status_code, transfer_status_desc, organization_id)
VALUES(12, 'TRO', 'Transferd Back From OT', 1);
INSERT INTO adt.m_transfer_status_master
(transfer_status_id, transfer_status_code, transfer_status_desc, organization_id)
VALUES(5, 'RJ', 'Rejected', 1);


INSERT INTO adt.m_short_leave_status
(short_leave_status_id, organization_id, unit_id, short_leave_code, short_leave_desc, status)
VALUES(1, 1, 1, 'PEN', 'Pending', 'A');
INSERT INTO adt.m_short_leave_status
(short_leave_status_id, organization_id, unit_id, short_leave_code, short_leave_desc, status)
VALUES(4, 1, 1, 'SHLAPR', 'Short Leave Approved', 'A');
INSERT INTO adt.m_short_leave_status
(short_leave_status_id, organization_id, unit_id, short_leave_code, short_leave_desc, status)
VALUES(5, 1, 1, 'RET', 'Patient Retained', 'A');
INSERT INTO adt.m_short_leave_status
(short_leave_status_id, organization_id, unit_id, short_leave_code, short_leave_desc, status)
VALUES(6, 1, 1, 'LATE', 'Patient Is Late', 'A');
INSERT INTO adt.m_short_leave_status
(short_leave_status_id, organization_id, unit_id, short_leave_code, short_leave_desc, status)
VALUES(7, 1, 1, 'REJBYDOC', 'Request Rejected By Doctor', 'A');
INSERT INTO adt.m_short_leave_status
(short_leave_status_id, organization_id, unit_id, short_leave_code, short_leave_desc, status)
VALUES(8, 1, 1, 'REJBYBIL', 'Request Rejected By Billing', 'A');
INSERT INTO adt.m_short_leave_status
(short_leave_status_id, organization_id, unit_id, short_leave_code, short_leave_desc, status)
VALUES(2, 1, 1, 'STDOC', 'Request Accepted By Doctor', 'A');
INSERT INTO adt.m_short_leave_status
(short_leave_status_id, organization_id, unit_id, short_leave_code, short_leave_desc, status)
VALUES(3, 1, 1, 'STBLG', 'Request Accepted By Billing', 'A');
INSERT INTO adt.m_short_leave_status
(short_leave_status_id, organization_id, unit_id, short_leave_code, short_leave_desc, status)
VALUES(9, 1, 1, 'STNUR', 'Request Rejected By Nurse', 'A');

INSERT INTO public.m_reject_reason
(organization_id, unit_id, code, reason, is_active, updated_by, updated_date, added_by, added_date, is_deleted)
VALUES(0, 0, '', '', true, 0, now()::timestamp(0) without time zone, 0, now()::timestamp(0) without time zone, false);


INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(985, 'Transfusion Precheck Master', '', '', 730, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(984, 'Camp Master', '', '', 730, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(983, 'Donor Screening Master', '', '', 730, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(982, 'Blood Group Master', '', '', 730, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(981, 'ICPC Code Clinical Form Mapping', '', '', 667, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(980, 'Clinical Form Service Mapping', '', '', 667, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(979, 'Clinical Section Summary', '', '', 667, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(978, 'Notes Master', '', '', 514, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(977, 'Vaccination Master', '', '', 639, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(976, 'ICPC Codes Configuration', '', '', 639, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(975, 'Discharge Summary Field Master', '', '', 639, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(974, 'Discharge Summary Template', '', '', 639, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(973, 'Discount Category Master', '', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(972, 'Service Profit Margin', '', '', 556, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(971, 'Test Costing', '', '', 556, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(970, 'Test Cost Head', '', '', 556, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(969, 'Cytology Test', '', '', 556, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(968, 'Score Master', '', '', 407, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(967, 'Activity', '', '', 407, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(966, 'Intervention', '', '', 407, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(965, 'Care Plan', '', '', 407, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(964, 'Diagnosis', '', '', 407, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(963, 'Document Type Master', '', '', 541, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(962, 'Growth Chart Master', '', '', 532, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(961, 'Promotion Management', '', '', 960, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(960, 'Promotion Configuration', '', '', 369, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(959, 'Checklist Equipment Mapper', '', '', 957, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(958, 'Checklist Master', '', '', 957, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(957, 'Equipment Master', '', '', 369, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(956, 'Diet Unit Master', '', '', 507, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(955, 'Tube Feeding Products', '', '', 507, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(954, 'Vital Qualifier Mapper', '', '', 476, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(953, 'Vital Qualifier Master', '', '', 476, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(952, 'ICPC Mapper', '', '', 476, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(951, 'ICD Diagnosis Mapper', '', '', 476, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(950, 'Formulation Type to Dosage', '', '', 476, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(949, 'Transaction Type Master', '', '', 445, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(948, 'Card Type Master', '', '', 445, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(947, 'Bedside Investigations Master', '', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(946, 'Corrective Maintenance Return Worklist', '', '', 227, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(945, 'Implant Opening Balance', '', '', 153, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(944, 'Physiotherapy Workbench', '', '', 943, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(943, 'Physiotherapy', '', '', 0, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(942, 'Miscellaneous Expense', '', '', 886, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(941, 'Bill Cancellation', '', '', 886, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(940, 'Return Acceptance', '', '', 195, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(939, 'User Item Billing', '', '', 195, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(938, 'Patient Consignment Issue', '', '', 195, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(937, 'Consignment Manager Dashboard', '', '', 195, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(936, 'Consignment PO Approval', '', '', 195, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(935, 'Consignment PO Creation', '', '', 195, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(934, 'Workorder Settlement', '', '', 186, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(933, 'Workorder Approval', '', '', 186, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(932, 'OT Indent', '', '', 321, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(931, 'OT Manager Dashboard', '', '', 321, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(930, 'Patient Consumption', '', '', 321, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(929, 'OT Checklist', '', '', 321, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(928, 'OT Dashboard', '', '', 321, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(927, 'OT Reservation WorkList', '', '', 321, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(926, 'OT Reservation', '', '', 321, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(925, 'Test Result Trend Analysis', '', '', 924, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(924, 'Trend Analysis', '', '', 272, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(923, 'Discharge Patient', '', '', 359, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(922, 'Male Medical History', '', '', 706, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(921, 'Female Medical History', '', '', 706, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(920, 'Couple Registration', '', '', 706, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(919, 'Loan Out Approval', '', '', 718, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(918, 'Loan Out', '', '', 718, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(917, 'Loan In Approval', '', '', 718, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(916, 'Loan In', '', '', 718, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(915, 'Issue Blood Bag', '', '', 718, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(914, 'Blood Bag Reservation', '', '', 718, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(913, 'Cross Match Registe', '', '', 718, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(912, 'Blood Group Confirmation', '', '', 718, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(911, 'Linen Issue Acceptance', '', '', 343, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(910, 'Linen Indent Approval', '', '', 343, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(909, 'Linen Indent', '', '', 343, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(908, 'Access Control', '', '', 0, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(907, 'Transaction Log', '', '', 678, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(906, 'Doctor Advance Approval', '', '', 22, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(905, 'Doctor Advance', '', '', 22, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(904, 'Doctor Credit Approval', '', '', 22, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(903, 'Doctor Credit Note', '', '', 22, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(902, 'Doctor Referral Payment Voucher', '', '', 22, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(901, 'Company Settlement', '', '', 678, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(900, 'Fund Status Dashboard', '', '', 896, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(899, 'Saction Allocation Approval', '', '', 896, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(898, 'Saction Allocation Request', '', '', 896, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(897, 'Fund Inflow', '', '', 896, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(896, 'Fund Collection', '', '', 678, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(895, 'Trust Master', '', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(894, 'New Born Details', '', '', 265, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(893, 'Patient Self-Monitoring', '', '', 243, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(892, 'Nursing Handover', '', '', 692, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(891, 'Blood Issue Acceptance Worklist', '', '', 692, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(890, 'Scheduled Worklist', '', '', 886, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(889, 'MLC Reporting Worklist', '', '', 328, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(888, 'Bad Debt List', '', '', 886, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(887, 'Concession Management', '', '', 886, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(886, 'Billing', '', '', 0, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(884, 'Workbench', '', '', 883, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(883, 'Physiotherapy', '', '', 868, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(877, 'Report Handover', '', '', 873, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(876, 'Result Validation', '', '', 873, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(875, 'Result Entry', '', '', 873, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(874, 'Workbench', '', '', 873, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(873, 'Diagnostic Procedure', '', '', 868, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(872, 'Dialysis Record', '', '', 869, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(871, 'Hemodialysis Workload', '', '', 869, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(870, 'Hemodialysis Workbench', '', '', 869, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(869, 'Hemodialysis', '', '', 868, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(868, 'Procedure', '', '', 0, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(867, 'Dialysis Record', '', '', 842, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(866, 'Hemodialysis Workload', '', '', 842, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(865, 'Cyclic Appointment Scheduling', '', '', 2, 1, NULL, 'A', NULL);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(864, 'Bill Of Material', '#/OTbillOfMaterial', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(863, 'Consignment Return', '', '', 172, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(862, 'Walk In Prescription', '', '', 110, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(861, 'Consignment PO Creation', '', '', 172, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(860, 'Ward Store Mapper', '', '', 661, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(859, 'Short Leave Request Approval', '', '', 886, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(858, 'Consignment Stock Conversion', '', '', 172, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(857, 'Pending Handover Worklist', '', '', 110, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(856, 'Assign Picker', '', '', 110, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(855, 'Pharmacology Validaion', '', '', 110, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(854, 'Asset Management', '', '', 369, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(853, 'Day Wise Diet Template', '', '', 851, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(852, 'Dish Master', '', '', 851, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(851, 'Diet and Kitchen', '', '', 531, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(850, 'CSSD Opening Balance', '#/cssdFixedAssetMaster', '', 231, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(849, 'Auction Vendor Selection', '', '', 203, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(848, 'Auction Vendor Negotiation', '', '', 203, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(847, 'Auction Quotation Fill In', '', '', 203, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(846, 'Auction Enquiry Sent To Vendor', '', '', 203, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(845, 'GRN Settlement', '#/grnSettlement', '', 172, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(844, 'Language Master', '', '', 490, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(843, 'Hemodialysis Workbench', '#/hemodialysisWorkbench', '', 842, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(842, 'Hemodialysis', '', '', 0, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(841, 'Pathology Machine Parameter Linking', '#/pathologyMachineParametersLinking', '', 556, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(840, 'Machine Master', '#/machineMaster', '', 556, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(839, 'Machine Parameter Master', '#/machineParameterMaster', '', 556, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(838, 'Due Deposit Dashboard', '', '', 886, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(837, 'Cyclic Appointments', '', '', 0, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(836, 'Department Store Mapper', '#/departmentStoreMapper', '', 661, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(835, 'Role Permission', '#/doctorDashboard', '', 0, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(834, 'Transfer Request Acceptance', '', '', 65, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(833, 'Labor Room Transfer', '', '', 44, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(832, 'Report Handover', '', '', 828, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(831, 'Result Validation', '', '', 828, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(830, 'Result Entry', '', '', 828, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(829, 'Workbench', '', '', 828, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(828, 'Radiology', '', '', 0, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(827, 'Generic Schedule Master', '', '', 457, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(826, 'Schedule Master', '', '', 457, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(825, 'Microbiology Stain Master', '', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(824, 'RMO Allocation Master', '', '', 639, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(823, 'Day Care Worklist', '', '', 784, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(822, 'Pass Bed Category Mapper', '', '', 820, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(821, 'Pass Type Master', '', '', 820, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(820, 'Visitor Type', '', '', 531, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(819, 'Ambulance Charges', '', '', 812, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(818, 'Ambulance Facilities Master', '', '', 812, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(817, 'Resource Allocation Master', '', '', 812, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(816, 'Ambulance Tarrif Master', '', '', 812, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(815, 'Service Provider Master', '', '', 812, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(814, 'Ambulance Master', '', '', 812, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(813, 'Ambulance Type Master', '', '', 812, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(812, 'Ambulance Service', '', '', 531, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(811, 'Fund Approved List', '', '', 807, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(810, 'Fund Requisition', '', '', 807, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(809, 'Fund Approval', '', '', 807, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(808, 'Fund Reconciliation', '', '', 807, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(807, 'Fund Management', '', '', 531, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(806, 'Implant Master', '', '', 804, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(805, 'Store Type Master', '', '', 804, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(804, 'Inventory', '', '', 531, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(803, 'Pharmacy Tariff Master', '', '', 628, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(802, 'Nurse Allocation Master', '', '', 407, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(801, 'Nursing Shift Dashboard', '', '', 799, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(800, 'Nursing Allocation Dashboard', '', '', 799, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(799, 'Nursing Rostering', '', '', 44, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(798, 'Serology Test Finding Master', '', '', 556, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(797, 'QMS Counter Master', '', '', 791, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(796, 'QMS Department Master', '', '', 791, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(795, 'QMS Kiosk Master', '', '', 791, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(794, 'QMS Token Display', '', '', 790, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(793, 'Call', '', '', 790, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(792, 'QMS Token', '', '', 790, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(791, 'QMS', '', '', 531, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(790, 'QMS', '', '', 908, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(789, 'ER Bed Transfer', '', '', 328, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(788, 'Hospital Accounts Master', '', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(787, 'Final Discharge', '', '', 784, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(786, 'Ready For Billing', '', '', 784, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(785, 'Marked For Discharge', '', '', 784, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(784, 'Day Care Discharge', '', '', 44, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(783, 'Discharge Summary', '', '', 780, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(782, 'Cancel Discharge', '', '', 780, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(781, 'Due For Discharge', '', '', 784, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(780, 'Discharge', '', '', 779, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(779, 'Day Care', '', '', 0, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(778, 'OTIMS Role Creation', '', '', 661, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(777, 'Administration Worklist', '', '', 692, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(776, 'ER Mark For Discharge', '', '', 328, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(775, 'Auto Service Posting Configuration', '', '', 659, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(774, 'SMS Utility', '', '', 659, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(773, 'Report Release', '', '', 770, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(772, 'Report Validation', '', '', 770, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(771, 'Report Entry', '', '', 770, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(770, 'Serology', '', '', 272, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(769, 'Tele Consulting', '', '', 908, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(768, 'MIS Report', '', '', 908, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(767, 'BI', '', '', 908, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(766, 'Surgery Safety', '', '', 0, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(765, 'Patient Type Details', '', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(764, 'Patient Type', '', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(763, 'Discharge Summary', '', '', 261, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(762, 'KIT Master Pharmacy', '', '', 628, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(761, 'Resource Charges Configuration', '', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(760, 'Cabin Display', '', '', 2, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(759, 'Nuesing Assesment Display', '', '', 2, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(758, 'Doctor Desk', '', '', 243, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(757, 'Casualty Worklist', '', '', 243, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(756, 'Document Generator', '', '', NULL, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(754, 'Addendum Bill', '', '', 10, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(753, 'Site Of Administration', '', '', 476, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(752, 'Assign Loyalty Card', '', '', 886, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(751, 'Loyalty Card', '', '', 0, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(750, 'Vaccination Schedule', '', '', 476, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(749, 'Discount Request Approval', '', '', 886, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(748, 'Discount Request Worklist', '', '', 10, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(747, 'Token Display', '', '', 2, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(746, 'Department Nursing Station Mapper', '', '', 661, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(745, 'Discount Rights', '', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(744, 'Growth Chart', '', '', 243, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(743, 'Loyalty Card Configuration', '', '', 728, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(742, 'Blood Bank Master', '', '', 730, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(741, 'Blood Group Matching Master', '', '', 730, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(740, 'Donor Rejection Master', '', '', 730, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(739, 'Blood Bag Type Component Master', '', '', 730, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(738, 'Screening Test', '', '', 730, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(737, 'Reason Of Discard', '', '', 730, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(736, 'Blood Component', '', '', 730, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(735, 'Blood Bag Capacity', '', '', 730, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(734, 'Blood Test', '', '', 730, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(733, 'Blood Test Result Master', '', '', 730, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(732, 'Blood Donor Type', '', '', 730, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(731, 'Blood Bag Type', '', '', 730, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(730, 'Blood Bank', '', '', 531, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(729, 'Loyalty Card Creation', '', '', 728, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(728, 'Loyalty Card', '', '', 531, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(727, 'Patient Requisition Worklist', '', '', 718, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(726, 'Blood Bag Stock Updation', '', '', 718, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(725, 'Blood Bag Component Extraction', '', '', 718, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(724, 'Red Cell Serum Worklist', '', '', 718, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(723, 'Red Cell Grouping Worklist', '', '', 718, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(722, 'Transfusion Transmitted Diseases', '', '', 718, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(721, 'Blood Bag Collection Worklist', '', '', 717, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(720, 'Donor Screening Worklist', '', '', 717, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(719, 'Donor Registration', '', '', 717, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(718, 'Blood Bank Process', '', '', 716, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(717, 'Donor', '', '', 716, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(716, 'Blood Bank', '', '', 908, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(715, 'User Counter Mapper', '', '', 661, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(714, 'Doctor Cabin Mapper', '', '', 661, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(713, 'Costing Sheet', '', '', 556, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(712, 'Costing Parameter', '', '', 556, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(711, 'Admission', '', '', 27, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(710, 'Surgical Calendar', '', '', 514, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(709, 'Billing Bed Category', '', '', 445, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(708, 'Stimulaion Calendar', '', '', 706, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(707, 'Order set Master', '', '', 639, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(706, 'IVF', '', '', 908, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(705, 'Cabin Master', '', '', 532, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(704, 'Counter Master', '', '', 532, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(703, 'Kiosk', '', '', 2, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(702, 'Nursing Worklist', '', '', 692, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(701, 'Counter Worklist', '', '', 2, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(700, 'Washed Linen Acceptance', '', '', 343, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(699, 'Prefix Configuration', '', '', 659, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(698, 'Order Set Master', '', '', 532, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(697, 'Weekend Master', '', '', 532, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(696, 'Procedure Configurations', '', '', 514, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(695, 'Stock Register', '#/stockRegister', '', 123, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(694, 'Nursing Progress Note', '#/nursingProgressNote', '', 692, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(693, 'Service Rendering', '#/serviceRendering', '', 692, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(692, 'Workstation', '', '', 44, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(691, 'Vitals', '#/vitals', '', 98, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(690, 'User Store Mapper', '', '', 661, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(689, 'User Nursing Station Mapper', '', '', 661, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(688, 'Template ICD Code Mapping', '#/templateICDCodeMapping', '', 667, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(687, 'Admission Request', '', '', 27, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(686, 'Encounter Queue EHC', '', '', 2, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(685, 'Pending for PO Creation', '', '', 570, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(684, 'IP Consultation Configuration', '', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(683, 'Sterilization Request', '#/sterilizationRequest', '', 88, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(682, 'Sterilization Request Acceptance', '', '', 231, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(681, 'Staff Linen Allocation', '', '', 343, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(680, 'Appointments', '', '', 2, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(679, 'Cash Desk Management', '', '', 678, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(678, 'Back Office', '', '', 0, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(677, 'Access Control', '', '', 661, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(676, 'User Authentication', '', '', 661, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(675, 'Role Managment', '', '', 661, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(674, 'User Role Managment', '', '', 661, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(673, 'Role Permission', '#/accessControl1', '', 661, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(672, 'Role Creation', '', '', 661, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(671, 'Sterilization Request', '', '', NULL, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(670, 'Templates', '#/templates', '', 667, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(669, 'Create Template', '#/templateUtility', '', 667, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(668, 'Data Object', '#/dataObject', '', 667, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(667, 'Template Utility', '', '', 531, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(666, 'Access Control', '#/newAccessControl', '', 662, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(665, 'User Authentication', '#/userAuthentication', '', 662, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(664, 'Role Managment', '#/roleManagement', '', 662, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(663, 'User Role Managment', '#/userRoleManagement', '', 662, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(662, 'Role Creation', '#/roleCreation', '', 662, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(661, 'User Managment', '', '', 531, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(660, 'Application Configuration', '#/applicationConfiguration', '', 659, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(659, 'Configuration', '', '', 531, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(658, 'Doctor Wise', '#/doctorWise', '', 656, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(657, 'Specialty Wise', '#/scheduleWise', '', 656, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(656, 'Schedule Block Unblock', '', '', 531, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(655, 'Modality Master', '#/modalityMaster', '', 652, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(654, 'Modality Type Master', '#/modalityTypeMaster', '', 652, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(653, 'Appointment Scheduling', '#/appointmentSchedulingUnitMaster', '', 652, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(652, 'Scheduling', '', '', 531, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(651, 'Doctor Registration', '#/doctorRegistration', '', 369, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(650, 'Staff Registration', '#/staffRegistration', '', 369, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(649, 'Mortuary Bed Master', '#/mortuaryBedMaster', '', 648, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(648, 'Mortuary', '', '', 531, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(647, 'Common', 'No Link', '', 531, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(646, 'General Insurace Master', '#/generalInsuranceMaster', '', 640, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(645, 'Equipment Master', '#/commonMaster/', '', 640, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(644, 'Fixed Asset Master', '#/fixedAssetMaster', '', 640, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(643, 'Product Category Master', '#/productCategoryMaster', '', 854, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(642, 'Asset Category Master', '#/assetCategoryMaster', '', 854, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(641, 'Asset Type Master', '#/assetTypeMaster', '', 854, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(640, 'Asset Management', '', '', 531, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(639, 'Clinical', 'No Link', '', 531, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(638, 'ETO Item Configuration', '#/ETOItemConfiguration', '', 628, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(637, 'Indent Template Master', '#/indentTemplateMaster', '', 628, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(636, 'Consumable Barcode Configuration', '#/consumableBarcodeConfig', '', 628, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(635, 'Storewise Generic Mapper', '#/storewiseGenericMapper', '', 628, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(634, 'Pharmacological Classification', '#/pharmacologicalClassificationMaster', '', 457, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(633, 'Product Restriction', '#/productRestriction/productCategoryMapping', '', 628, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(632, 'Store Item Mapping Master', '#/substoremaster', '', 628, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(631, 'Main Store Master', '#/mainstoremaster', '', 628, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(630, 'Rack Master', '#/rackMaster', '', 628, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(629, 'Vendor Master', '#/vendormaster', '', 628, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(628, 'Pharmacy', '', '', 531, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(627, 'Multicounter Package', '#/multicounterPackage', '', 622, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(626, 'Either Or Package', '#/eitherOrPackage', '', 622, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(625, 'All Inclusive Package', '', '', 622, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(624, 'Package With CAP', '#/packageWithCAP', '', 622, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(623, 'EHC Package', '#/EHCpackage', '', 622, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(622, 'Pacakge Configuration', '', '', 531, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(621, 'Deposit Matrix Configuration', '#/depositMatrixConfig', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(620, 'IP Default Service Configuration', '#/IPdefaultServiceConfig', '', 570, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(619, 'Periodicity Service Configuration', '#/periodicityServiceConfig', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(618, 'GL Code', '#/GLcode', '', 370, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(617, 'AP Master', '#/APmaster', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(616, 'AR Master', '#/ARmaster', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(615, 'Doctor Referral Share', '#/doctorReferralShareMaster', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(614, 'Doctorwise Share', '#/doctorWiseShare', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(613, 'Global Doctor Share', '#/globalDoctorShare', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(612, 'Doctor Consulation', '#/doctorConsulationMaster', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(611, 'OP Consulation Configuration', '#/OPConsulationConfigurationMaster', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(610, 'Service Tariff Master', '#/serviceTariffMaster', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(609, 'Tariff Master', '#/tariffMaster', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(608, 'Company Contract', '#/companyContractMaster', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(607, 'Payment Collection', '', '', 678, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(606, 'Settled Invoices', '', '', 901, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(605, 'Company Settlement', '', '', 901, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(604, 'Company Query & Response', '', '', 901, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(603, 'Company Remainder', '', '', 901, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(602, 'Company Invoice Creation', '', '', 901, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(601, 'Remainder For NOK', '#/reminderForNOK', '', 570, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(600, 'Shift Closing', '#/cashCounterClosing', '', 679, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(599, 'Round Off Configuration', '#/roundOffConfiguration', '', 570, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(598, 'Interim Handover', '#/intrimCashHandover', '', 679, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(597, 'Cash Opening Balance', '#/cashOpeningBalance', '', 679, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(596, 'Write Off Details', '#/writeOffDetails', '', 570, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(595, 'Write Off', '#/writeOff', '', 570, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(594, 'Auto Charge Review', '#/autoChargeReview', '', 570, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(593, 'Charge Conversion', '#/entitlementCoversion', '', 570, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(592, 'Charge Conversion', '#/chargeConversion', '', 570, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(591, 'Find Bill List', '#/findBillList', '', 570, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(590, 'Refund CR List', '#/refundCRList/cashForRefund', '', 570, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(589, 'Admission Rejection List', '#/admissionRejectionList', '', 570, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(588, 'Order Set List', '#/orderSetList', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(587, 'Order Set', '#/orderSet', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(586, 'Schedule Price Unit Service', '#/schedulePriceUnitService', '', 570, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(585, 'Unit Service Base Price', '#/unitServiceBasePrice', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(584, 'Unit Service Mapping', '#/unitServiceMapping', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(583, 'Unit Service Master', '#/unitServiceMaster', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(582, 'Account Payable Status', '#/accountPayableStatus', '', 570, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(581, 'Account Payable Comparsion', '#/accountPayableComparsion', '', 570, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(580, 'Account Payable', '#/accountPayable', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(579, 'Account Payable Master', '#/accountPayableMaster', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(578, 'Variable Pricing', '#/variableSquencePricing/sequencePricing', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(577, 'Package List', '#/packageList', '', 570, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(576, 'Billing Group Master', '#/billingGroupMaster', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(575, 'Grade Creation Master', '#/gradeCreationMaster', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(574, 'Corporate Master', '#/corporateMasterGlobal', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(573, 'Insurance & TPA Mapping', '#/InsuranceTPAMapping', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(572, 'TPA Master', '#/TPAmaster', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(571, 'Insurance Master', '#/insuranceMaster', '', 570, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(570, 'Billing', '', '', 531, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(569, 'Biochemical Test Master', '#/commonMaster/biochemicalTest', '', 556, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(567, 'Profile/Panel Test', '#/profilepanelTestMaster', '', 556, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(566, 'Parameter Formula', '#/parameterFormulaMaster', '', 556, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(565, 'Template Master', '#/templateMaster', '', 556, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(564, 'Department Master', '#/commonMaster/departmentMaster', '', 556, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(563, 'Multiparameter Test Master', '#/multiparameterTestMaster', '', 556, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(562, 'Parameter Master', '#/parameterMaster', '', 556, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(561, 'Header Master', '#/commonMaster/headerMaster', '', 556, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(560, 'Speciman Type', '#/specimanType', '', 556, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(559, 'Histopathology Test', '#/histopathologyTest', '', 556, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(558, 'Test Master', '#/testMaster', '', 556, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(557, 'Phlebotomy', '#/commonMaster/phlebotomyMaster', '', 556, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(556, 'LIS', '', '', 531, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(555, 'Kit Creation', '#/kitCreation', '', 551, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(554, 'Wrapper expiry master', '#/wrapperExpiryMaster', '', 551, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(553, 'Autoclave Master', '#/autoclaveMaster', '', 551, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(552, 'Sterilization Type Master', '#/sterilizationTypeMaster', '', 551, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(551, 'CSSD', '', '', 531, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(550, 'Room Master', '#/roomMaster', '', 541, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(549, 'Meal Preference Master', '#/mealPreferenceMaster', '', 541, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(548, 'Nursing Ward Mapping', '#/nursingStationWardMapping', '', 541, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(547, 'Nursing Station Master', '#/nursingStationMaster', '', 541, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(546, 'Bed Master', '#/bedMaster', '', 541, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(545, 'Ward Master', '#/wardMaster', '', 541, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(544, 'Billing Bed Category', '#/billingBedCategoryMasterUnit', '', 541, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(543, 'Bed Category Master', '#/bedCategoryMaster', '', 541, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(542, 'Billing Bed Category Master', '#/bedCategoryMasterUnit', '', 541, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(541, 'ADT', '', '', 531, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(540, 'Wing Master', '#/wingMaster', '', 532, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(539, 'Amenity Master', '#/amenityMaster', '', 532, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(538, 'Floor Map Master', '#/floorMapMaster', '', 532, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(537, 'Floor Master', '#/floorMaster', '', 532, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(536, 'Holiday Calendar Master', '#/holidayCalendarMasterUnit', '', 532, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(535, 'ICU Type Master', '#/ICUtypeMasterUnit', '', 532, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(534, 'Sub Department Master', '#/subDepartmentMasterUnit', '', 532, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(533, 'Department Master', '#/departmentMasterUnit', '', 532, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(532, 'Organization Structure', '', '', 531, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(531, 'Unit Master', '', '', 0, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(530, 'Faculty', '#/facultyMaster', '', 514, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(529, 'Anaesthesia Note', '#/noteMaster', '', 514, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(528, 'Consent Type', '#/consentType', '', 514, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(527, 'Consent Master', '#/consentMaster', '', 514, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(526, 'Checklist Master', '#/checklistMaster', '', 514, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(525, 'Checklist Item', '#/checklistItem', '', 514, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(524, 'Anaesthesia Type', '#/anaesthesiaType', '', 514, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(523, 'Operation Table Type', '#/operationTableType', '', 514, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(522, 'Operation Table', '#/operationTable', '', 514, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(521, 'Operation Theatre Type', '#/operationTheatreType', '', 514, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(520, 'Procedure Type', '#/procedureType', '', 514, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(519, 'Operation Theatrer', '#/operation_Theatre', '', 514, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(518, 'Cancellation Master', '#/cancellationMaster', '', 514, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(517, 'Resource Configuration', '#/resourceConfiguration', '', 514, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(516, 'Subsequent Surgery Configuration', '#/subsequentSurgeryConfiguration', '', 514, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(515, 'Additional OT Time Configuration', '', '', 514, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(514, 'OTIMS', '', '', 531, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(513, 'Day Wise Diet Template', '#/mealMaster', '', 507, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(512, 'Meal Master', '#/mealMaster', '', 507, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(511, 'Dish Master', '#/dishMaster', '', 507, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(510, 'Diet Type Master', '#/dietTypeMaster', '', 507, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(509, 'Key Ingredients Master', '#/keyIngredientsMaster', '', 507, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(508, 'Nutritional Master', '#/nutritionalMaster', '', 507, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(507, 'Diet and Kitchen', '', '', 369, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(506, 'Delivery Type Master', '#/deliveryTypeMaster', '', 506, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(505, 'Baby Registration', '', '', 369, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(504, 'Clinic Master', '#/clinicMaster', '', 532, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(503, 'OPD', '', '', 369, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(502, 'Dose Master', '#/doseMaster', '', 490, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(501, 'Inspection Master', '#/inspectionMaster', '', 490, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(500, 'Designation Master', '#/designationMaster', '', 490, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(499, 'Doctor Category Master', '#/doctorCategoryMaster', '', 490, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(498, 'Employee Category Master', '#/employeeCategoryMaster', '', 490, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(497, 'Degree Master', '#/degreeMaster', '', 490, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(496, 'Qualification Master', '#/qualificationMaster', '', 490, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(495, 'Reason Master', '#/reasonMaster', '', 490, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(494, 'Reason Type Master', '#/reasonTypeMaster', '', 490, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(493, 'Rejection Master', '#/rejectionMaster', '', 490, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(492, 'Discount Master', '#/commonMaster/priorityMaster', '', 490, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(491, 'Priority Master', '#/commonMaster/priorityMaster', '', 490, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(490, 'Common', '', '', 369, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(489, 'Allergy Master', '#/allergyMaster', '', 476, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(488, 'Allergy Type Master', '#/allergyTypeMaster', '', 476, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(487, 'Surgery Master', '#/surgeryMaster', '', 476, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(486, 'Surgery Grade Master', '#/surgeryGradeMaster', '', 476, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(485, 'System Observation Master', '#/systemObservationMaster', '', 476, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(484, 'System Master', '#/systemMaster', '', 476, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(483, 'Immunization Schedule', '#/immunizationSchedule', '', 476, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(482, 'Vaccination Master', '#/vaccinationMaster', '', 476, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(481, 'Blood Component', '#/bloodComponent', '', 476, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(480, 'Nursing Order Master', '#/nursingOrderMaster', '', 476, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(479, 'Formulation Type & ROA Master', '#/formulationTypeROAMaster', '', 476, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(478, 'Frequency Master', '#/frequencyMaster', '', 476, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(477, 'Route of Administration', '#/routeAdministration', '', 476, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(476, 'Clinical Workstation', '', '', 369, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(475, 'Other Charges Master', '#/otherCharges', '', 457, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(474, 'Tax Master', '#/taxMaster', '', 457, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(473, 'Tax Component Master', '#/taxComponentMaster', '', 457, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(472, 'Combination Medication', '#/combinationMedicationMaster', '', 457, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(471, 'Kit Master', '#/kitMaster', '', 457, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(470, 'Storage Unit Master', '#/storageunitMaster', '', 457, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(469, 'Dispensing Master', '#/dispensingMaster', '', 457, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(468, 'Strength Unit Master', '#/pharmacyStrengthUnitMaster', '', 457, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(467, 'Unit Master', '#/pharmacyunitMaster', '', 457, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(466, 'Unit Of Measurement', '#/unitofMeasurement', '', 457, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(465, 'Package Master', '#/packageMaster', '', 457, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(464, 'Item Master', '#/itemMasterGlobal', '', 457, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(463, 'Generic Master', '#/genericMaster', '', 457, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(462, 'Supply Days Master', '#/supplyDaysMaster', '', 457, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(461, 'Manufacturer Master', '#/manufacturerMaster', '', 457, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(460, 'Schedule Master', '#/commonMaster/', '', 457, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(459, 'Formulation Type', '#/pharmacyformulationTypeMaster', '', 457, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(458, 'Product Category', '#/pharmacyproductCategoryMaster', '', 457, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(457, 'Pharmacy', '', '', 369, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(456, 'Bank Master', '#/bankMaster', '', 445, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(455, 'Bank Branch', '#/bankBranch', '', 445, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(454, 'Employee Category', '#/employeeCategory', '', 445, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(453, 'Doctor Category', '#/doctorCategory', '', 445, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(452, 'Currency Denomination', '#/currencydenominatio', '', 445, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(451, 'Currency Master', '#/currencyMaster', '', 445, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(450, 'Consultant Type', '#/commonMaster/consultantType', '', 445, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(449, 'Payment Entitlement', '#/commonMaster/paymentEntitment', '', 445, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(448, 'Mode Of Payment', '#/commonMaster/paymentMode', '', 445, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(447, 'Visit Type Master', '#/commonMaster/visitType', '', 445, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(446, 'Patient Category', '#/patientCategory', '', 445, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(445, 'Billing', '', '', 369, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(444, 'Speciman Master', '#/specimanMAster', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(443, 'Mapping of Micro-organism', '#/mappingofMicroorganism', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(442, 'Organism Group Mapping With Organism', '#/organismMapping', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(441, 'Speciman Mapping With Block & Slide Procedure', '#/specimanMapping', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(440, 'Slide Procedure Master', '#/commonMaster/slideProcedureMaster', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(439, 'Block Master', '#/commonMaster/blockMaster', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(438, 'Sensitivity Test Result', '#/commonMaster/sensitivityTypeResultMaster', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(437, 'Stainig Master', '#/commonMaster/stainigMaster', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(436, 'Organism Group Master', '#/commonMaster/organismGroupMaster', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(435, 'Organism Master', '#/commonMaster/organismMaster', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(434, 'Colony Test With Media Master', '#/colonyTestWithMediaMaster', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(433, 'Addition of Antibiotics Group Master', '#/antibioticAdditionGroupMaster', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(432, 'Addition of Antibiotics Against Antibiotic Class', '#/additionofAntibiotic', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(431, 'Antibiotics Group', '#/commonMaster/antibioticsGroupMaster', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(430, 'Antibiotics Class', '#/commonMaster/antibioticMaster', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(429, 'Antibiotic', '#/commonMaster/antibioticsMaster', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(428, 'Bacteria Classification', '#/commonMaster/bacteriaClassificationMaster', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(427, 'Colony Master', '#/commonMaster/colonyMaster', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(426, 'Sample Type Media Mapping Master', '#/sampleTypeMediaMappingMaster', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(425, 'Incubation Test Master', '#/commonMaster/incubationTestMaster', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(424, 'Media Master', '#/commonMaster/mediaMaster', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(423, 'Wet Film Study Master', '#/commonMaster/wtifilmStudyMasterMaster', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(422, 'Incubation Master', '#/incubationPeriodMaster', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(421, 'Bacteria Shape', '#/commonMaster/bacteriaShapMaster', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(420, 'Report Type Master', '#/commonMaster/reportTypeMaster', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(419, 'Reagent Master', '#/commonMaster/reagentMaster', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(418, 'Textual Reference Range', '#/textualReferenceRangeMaster', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(417, 'Unit Master', '#/commonMaster/unitMaster', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(416, 'Age Group Master', '#/ageGroupMaster', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(415, 'Prerequisites Master', '#/commonMaster/prerequisitesMaster', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(414, 'Techinque Master', '#/commonMaster/techinqueMaster', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(413, 'Container Master', '#/commonMaster/containerMaster', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(412, 'Sample Master', '#/commonMaster/sampleMaster', '', 411, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(411, 'LIS', '', '', 369, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(410, 'Day wise Rota Creation', '#/daywiseRotaCreation', '', 369, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(409, 'Rota Creation Master', '#/rotaCreation', '', 369, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(408, 'Shift Master', '#/shiftMaster', '', 647, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(407, 'Nursing', '', '', 531, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(406, 'Short Leave Reason', '#/shortLeaveReasonMaster', '', 401, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(405, 'Reason For Discharge Master', '#/dischargeTypeMaster', '', 401, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(404, 'Occupancy Unit Type', '#/occupancyUnitMaster', '', 401, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(403, 'Admission Type', '#/admissionTypeMaster', '', 401, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(402, 'Bed Type Master', '#/bedTypeMaster', '', 401, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(401, 'ADT', '', '', 369, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(400, 'Consent Master', '#/consentMaster', '', 380, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(399, 'Consent Type Master', '#/consentTypeMaster', '', 380, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(398, 'Prefix Master', '#/prefixMaster', '', 380, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(397, 'Encounter Type Master', '#/encounterTypeMaster', '', 380, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(396, 'Referral Master', '#/referralMaster', '', 380, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(395, 'Referral Type Master', '#/commonMaster/referraltype', '', 380, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(394, 'Patient Source Master', '#/commonMaster/patientsource', '', 380, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(393, 'Occupation Master', '#/commonMaster/occupation', '', 380, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(392, 'Religion Master', '#/commonMaster/religion', '', 380, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(391, 'Marital Status Master', '#/commonMaster/maritalstatus', '', 380, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(390, 'Relation Master', '#/commonMaster/relationmaster', '', 380, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(389, 'Sex Master', '#/commonMaster/sexmaster', '', 380, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(388, 'Identification Card', '#/identificationMaster', '', 380, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(387, 'Area Master', '#/areaMaster', '', 380, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(386, 'City Master', '#/cityMaster', '', 380, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(385, 'District Master', '#/districtMaster', '', 380, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(384, 'State Master', '#/stateMaster', '', 380, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(383, 'Country Master', '#/countryMaster', '', 380, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(382, 'Race Master', '#/raceMaster', '', 380, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(381, 'Nationality Master', '#/nationalityMaster', '', 380, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(380, 'Patient Registration', '', '', 369, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(379, 'License Type Master', '#/licenseTypeMaster', '', 370, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(378, 'Document Type Master', '#/documentTypeMaster', '', 370, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(377, 'Organization Master', '#/organizationMaster', '', 370, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(376, 'Service Master', '#/serviceMaster', '', 370, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(375, 'Holiday Calendar Master', '#/holidayCalenderMaster', '', 370, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(374, 'ICU Type Master', '#/icutypeMaster', '', 532, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(373, 'Sub Department Master', '#/subSpecialityMaster', '', 370, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(372, 'Department Master', '#/specialityMaster', '', 370, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(371, 'Hospital Master', '#/hospitalMaster', '', 370, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(370, 'Organization Structure', '', '', 369, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(369, 'Global Master', '', '', 0, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(368, 'Mortuary Bed Management', '#/mortuaryBedManagement', '', 362, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(367, 'Release Mortuary', '#/morgueReleased', '', 362, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(366, 'Mortuary Reservation', '#/morgueReservation', '', 362, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(365, 'Mortuary Availability', '#/morgueAvailability', '', 362, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(364, 'Pending List', '#/morguePendinglist', '', 362, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(363, 'Death Registration', '#/deathRegistration', '', 362, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(362, 'Mortuary', '', '', 0, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(361, 'Under Maintenance', '#/bedUnderMaintenance', '', 359, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(360, 'Worklist', '#/houseKeeping', '', 359, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(359, 'House Keeping', '', '', 0, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(358, 'Issue Washed Linen', '#/issueWashedLinen', '', 343, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(357, 'Acceptance Of Dirty Linen', '#/acceptanceOfDirtyLinen', '', 343, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(356, 'Issue Dirty Linen', '#/issueDirtyLinen', '', 343, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(355, 'Acceptance Washing Request', '#/acceptanceWashingRequest', '', 343, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(354, 'Washing Request', '#/washingRequest', '', 343, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(353, 'Staff Exchange Linen Issue', '#/staffExchangelinenIssue', '', 343, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(352, 'Staff Exchange Linen', '#/staffExchangeLinen', '', 343, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(351, 'Issue To Staff', '#/issueToStaff', '', 343, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(350, 'Convert of Condemnation Stock', '#/convertOfCondemnationStock', '', 343, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(349, 'Acceptance of Condemnation', '#/acceptanceOfCondemnation', '', 343, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(348, 'Send To Condemnation', '#/sendToCondemnation', '', 343, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(347, 'Rejected Linen Acceptance', '#/rejectedLinenAcceptance', '', 343, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(346, 'Linen Return Acceptance', '#/linenReturnAcceptance', '', 343, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(345, 'Linen Return', '#/linenReturn', '', 343, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(344, 'Linen Issue', '#/linenIssue', '', 343, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(343, 'Linen-Laundry', '', '', 908, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(342, 'Rejected Diet List', '#/rejectedDietList', '', 337, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(341, 'Diet Acceptance', '#/dietAcceptance', '', 337, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(340, 'Diet Issue', '#/dietIssue', '', 337, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(339, 'Diet Order', '#/dietOrder', '', 337, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(338, 'Diet Planning', '#/dietPatientRequest', '', 337, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(337, 'Diet & Kitchen', '', '', 0, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(336, 'Admission Note', '#/admissionRequest', '', 328, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(335, 'Final Discharge', '#/ERfinalDischarge', '', 328, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(334, 'Ready For Billing', '#/ERreadyForBilling', '', 328, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(333, 'Due For Discharge', '#/ERdueForDischarge', '', 328, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(332, 'Map Of Bed', '#/mapOfBedForER', '', 328, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(331, 'ER Patient List', '#/erPatientList', '', 328, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(330, 'Triagework List', '#/bedTypeAllocation', '', 328, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(329, 'ER-Encounter', '#/unknownPatientReg/personalForUnknown', '', 328, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(328, 'Emergency Room', '', '', 0, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(327, 'Implant Indent Acceptance', '#/implantIndentAcceptance', '', 321, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(326, 'Implant Indent', '#/implantIndent', '', 321, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(325, 'Anesthesia Note', '#/anesthesiaNote', '', 321, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(324, 'Surgery Note', '#/surgeryNote', '', 321, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(323, 'OT Billing Completion', '#/surgeryNote', '', 321, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(322, 'OT Completion', '#/OTCompletion', '', 321, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(321, 'Theatre Management', '', '', 0, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(320, 'Workload Dashboard', '#/workloadDashboard', '', 272, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(319, 'Pending List For Pending Upload', '#/pendinglistForReportUpload', '', 316, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(318, 'Pending List For Sample Dispatch', '#/pendinglistForSampleDispatch', '', 316, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(317, 'Out Source Pending List', '#/outSourcedPendinglistAssignment', '', 316, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(316, 'Out Source', '', '', 272, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(315, 'Report Handover', '#/worklistForPrinting', '', 272, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(314, 'Sensitivity Testing', '#/sensitivityTestingProcess', '', 307, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(313, 'Microscopic Examination', '#/microscopicExamination', '', 307, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(312, 'Biochemical Testing', '#/biochemicalTesting', '', 307, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(311, 'Macroscopic Examination', '#/macroscopicExaminationMaster', '', 307, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(310, 'Incubation Master', '#/IncubationMaster', '', 307, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(309, 'WorkList', '#/sampleProcessingWorkList', '', 307, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(308, 'Sample Acceptance', '#/sampleAcceptance', '', 307, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(307, 'Microbiology', '', '', 272, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(306, 'Frozen Section Request List', '#/frozenSectionRequestList', '', 295, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(305, 'Slide For Second Opinion', '#/pendingSlideForSecondOpinion', '', 295, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(304, 'Second Opinion Request', '#/secondOpinionRequest', '', 295, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(303, 'Storage', '#/storage', '', 295, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(302, 'Addendum Report', '#/addendumReport', '', 295, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(301, 'Restain Request Worklist', '#/restainingRequestWorklist', '', 295, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(300, 'Microscopic Examination', '#/microscopicExaminiationWorkList', '', 295, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(299, 'Slide Creation', '#/slideCreation', '', 295, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(298, 'Block Creation', '#/blockCreation', '', 295, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(297, 'Macroscopic Examination', '#/microscopeExamination', '', 295, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(296, 'Specimen Receipt', '#/specimenReceipt', '', 295, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(295, 'Histopathology', '', '', 272, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(294, 'Report Release', '#/worklistForReportRelease', '', 289, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(293, 'Validation', '#/worklistForValidation', '', 289, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(292, 'Report Entry', '#/worklistForReportEntry', '', 289, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(291, 'Worklist', '#/worklistForDepartment', '', 289, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(290, 'Pending Acceptance', '#/pendingListForAcceptance', '', 289, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(289, 'Hematology', '', '', 272, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(288, 'Report Release', '#/worklistForReportRelease', '', 283, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(287, 'Validation', '#/worklistForValidation', '', 283, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(286, 'Report Entry', '#/worklistForReportEntry', '', 283, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(285, 'Worklist', '#/worklistForDepartment/', '', 283, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(284, 'Pending Acceptance', '#/pendingListForAcceptance', '', 283, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(283, 'Biochemistry', '', '', 272, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(282, 'IP Samples', '#/inPatient', '', 279, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(281, 'OP Samples', '#/outPatient', '', 279, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(280, 'Centrifugation List', '#/centrifugationWorkList', '', 279, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(279, 'Central Receiving', '', '', 272, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(278, 'Sample Recollection', '#/sampleRecollection', '', 273, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(277, 'Repeat Sample', '#/patientArrivalForRepeatSampleCollection', '', 273, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(276, 'Patient Arrival', '#/patientArrival', '', 273, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(275, 'Send TO CR', '#/sendToCR', '', 273, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(274, 'Worklist', '#/phlebotomyWorkList', '', 273, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(273, 'Phlebotomy', '', '', 272, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(272, 'LIS', '', '', 0, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(271, 'Physiotherapy Workbench', '#/physiotherapyWorkbench', '', 0, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(270, 'Radiology Workbench', '#/radiologyWorkbench', '', 0, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(269, 'Progress Note', '#/progressNote', '', 243, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(268, 'Bed Allocation', '#/cradleBedAllocation', '', 265, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(267, 'List', '#/newBornList', '', 265, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(266, 'Registration', '#/newBornRegistration', '', 265, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(265, 'New Born', '', '', 243, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(264, 'Pending Short Leave', '#/pendingShortLeave', '', 261, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(263, 'Cancel Discharge', '#/cancelForDischarge', '', 261, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(262, 'Due For Discharge', '#/dueForDischarge', '', 261, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(261, 'Discharge', '', '', 243, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(260, 'Transfer To ICU', '#/transferToICU', '', 255, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(259, 'Pending Transfer', '#/pendingTransferRequest', '', 255, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(258, 'Accept Transfer Of Care', '#/acceptanceOfTransferOfCare', '', 255, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(257, 'Initiate Transfer Of Care', '#/intiateTransferCare', '', 255, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(256, 'Transfer Request Acceptance', '#/transferRequestAcceptance', '', 255, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(255, 'Transfer', '', '', 243, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(254, 'Second Opinion Request', '#/secondOpinionRequest', '', 247, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(253, 'Crashcart Medications Approvel', '#/administrationApproval', '', 247, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(252, 'Homecare Medication Approval', '#/homecareMedicationApproval', '', 247, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(251, 'Blood Request', '#/bloodRequest', '', 247, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(250, 'Pending Consult Order', '#/pendingConsultOrderRequest', '', 247, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(249, 'Consult Order', '#/interconsultation', '', 247, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(248, 'Nursing Order', '#/nursingOrder', '', 247, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(247, 'Order', '', '', 243, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(246, 'Immunization History', '#/immuniztionHistory', '', 244, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(245, 'Allergies', '#/allergies', '', 244, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(244, 'History', '', '', 243, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(243, 'Clinical Workstation', '', '', 0, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(242, 'Application', '#/application', '', 231, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(241, 'Kit Issue', '#/kitIssue', '', 231, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(240, 'Send to Microbiology', '#/sendtoMicrobiology', '', 231, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(239, 'Pending for Indicators', '#/sterilizationPendingIndicators', '', 237, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(238, 'Load Batch', '#/sterilizationLoadBatch', '', 237, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(237, 'Sterilization Process', '', '', 231, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(236, 'Pending for Kit Formation', '#/preSterilizationKitFormation', '', 232, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(235, 'Pending for Dis-Infection', '#/preSterilizationDisInfection', '', 232, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(234, 'Pending for Inspection', '#/preSterilizationInspection', '', 232, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(233, 'Cleaned Worklist', '#/preSterilizationCleaning', '', 232, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(232, 'Pre-Sterilization Process', '', '', 231, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(231, 'CSSD', '', '', 0, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(230, 'Corrective Maintenance Schedule Worklist', '#/correctiveMaintenanceScheduleWorklist', '', 227, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(229, 'Corrective Maintenance Worklist', '#/correctiveMaintenanceWorklist', '', 227, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(228, 'Corrective Maintenance', '#/correctiveMaintenance', '', 227, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(227, 'Corrective Maintenance', '', '', 221, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(226, 'Schedule Worklist', '#/scheduleWorklist', '', 222, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(225, 'PM Schedule', '#/PMSchedule', '', 222, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(224, 'PM Request', '#/PMRequest', '', 222, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(223, 'Asset PMS Scheduler', '#/assetPMSScheduler', '', 222, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(222, 'Preventive Maintenance', '', '', 221, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(221, 'Equipment Management', '', '', 0, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(220, 'Add Fixed Asset Master', '#/addfixedAssetMaster', '', 219, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(219, 'Fixed Asset Opening Balance', '', '', 203, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(218, 'Rejected Asset Acceptance', '#/rejectedAssetAcceptance', '', 203, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(217, 'Asset Return Acceptance', '#/assetReturnAcceptance', '', 203, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(216, 'Asset Return', '#/assetReturn', '', 203, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(215, 'Asset Issue Acceptance', '#/assetIssueAcceptance', '', 203, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(214, 'Asset Issue', '#/assetIssueMaster', '', 203, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(213, 'Asset PMS Scheduler', '#/assetPMSScheduler', '', 203, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(212, 'Non Returnable GatePass', '#/nonReturnableGatePass', '', 203, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(211, 'Auction Vendor Approval', '#/auctionVendorApproval', '', 203, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(210, 'Items For Auction', '#/itemsForAuction', '', 203, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(209, 'Auction Requisition Approval', '#/auctionRequisitionApproval', '', 203, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(208, 'Auction Requisition', '#/auctionRequistion', '', 203, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(207, 'Asset Issue', '#/assetIssueIndent', '', 204, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(206, 'Asset Indent Approval', '#/assetIndentApproval', '', 204, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(205, 'Asset Indent', '#/assetIndent', '', 204, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(204, 'Indent', '', '', 203, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(203, 'Asset Management', '', '', 0, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(202, 'Loan Out Receive', '#/loanOutReceive', '', 198, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(201, 'Loan In Out', '#/loanInOut', '', 198, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(200, 'Loan Out', '#/loanOut', '', 198, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(199, 'Loan In', '#/loanIn', '', 198, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(198, 'Loan In Out', '', '', 161, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(197, 'Consignment Outward', '#/consignmentOutward', '', 195, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(196, 'Consignment Inward', '#/consignmentInward', '', 195, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(195, 'Consignment', '', '', 161, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(194, 'Workorder Creation', '#/workorderCreation', '', 186, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(193, 'Quotation Selection', '#/workorderQuotationSelect', '', 186, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(192, 'Quotation Negotiation', '#/workorderQuotationNegotiation', '', 186, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(191, 'Quotation Entry', '#/workorderQuotationEntry', '', 186, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(190, 'Quotation Request', '#/workorderQuotationRequest', '', 186, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(189, 'Approval Worklist', '#/workorderApprovalWorklist', '', 186, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(188, 'Quotation Send To Vendor', '#/quotationSendToVendor', '', 186, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(187, 'Workorder Request', '#/workorderRequest', '', 186, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(186, 'Work Order', '', '', 161, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(185, 'Enquiry Sent to Vendor', '#/enquirySentToVendor', '', 172, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(184, 'Physical Inspection', '#/physicalInspec', '', 172, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(183, 'Barcode Printing', '#/barcodePrinting', '', 172, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(182, 'GRN Approval', '#/GRNApproval', '', 172, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(181, 'GRN', '#/GRN', '', 172, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(180, 'PO Approval', '#/POApproval', '', 172, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(179, 'PO Creation', '#/POCreation', '', 172, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(178, 'Vendor Selection', '#/vendorSelection', '', 172, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(177, 'Vendor Negotitation', '#/vendorNegotitation', '', 172, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(176, 'Technical Approval', '#/technicalApproval', '', 172, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(175, 'Quotation Fill In', '#/quotationFillIn', '', 172, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(174, 'Quotation Enquiry Approval', '#/quotationEnquiryApproval', '', 172, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(173, 'Quotation Enquiry', '#/quotationEnquiry', '', 172, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(172, 'Quotation', '', '', 161, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(171, 'Local Purchase Request Approval', '#/localPurchhaseRequestApproval', '', 162, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(170, 'Local Purchase Request', '#/localPurchhaseRequest', '', 162, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(169, 'Purchase Request Approval', '#/purchaseRequestApproval', '', 162, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(168, 'Purchase Request', '#/purchaseRequest', '', 162, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(167, 'GRN Against PO', '#/GRNAgainstPO', '', 162, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(166, 'Physical Inventory', '#/physicalInventory', '', 162, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(165, 'Item Creation Pending List', '#/itemCreationPendingList', '', 162, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(164, 'Item Approval', '#/itemApproval', '', 162, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(163, 'Item Request', '#/itemRequest', '', 162, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(162, 'Purchase', '', '', 161, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(161, 'Procurement', '', '', 0, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(160, 'Witness Approval', '#/witnessApproval', '', 155, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(159, 'Waste Assest Disposal Worklist', '#/wasteAssetDisposalWorklist', '', 155, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(158, 'Request For Assest Disposal Worklist', '#/requestForAssetDisposalWorklist', '', 155, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(157, 'Scrap Request Approval', '#/approvalScrapRequest', '', 155, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(156, 'Scrap Request', '#/scrapRequest', '', 155, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(155, 'Scrap', '', '', 121, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(154, 'Opening balance', '#/openingBalance', '', 153, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(153, 'Stock', '', '', 121, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(152, 'Confirm Stock', '#/confirmStock', '', 145, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(151, 'Variance Approval', '#/varianceApproval', '', 145, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(150, 'Compute Stock', '#/computeStock', '', 145, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(149, 'Rejected Upload', '#/rejectedUploaded', '', 145, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(148, 'Upload Stock Approval', '#/uploadStockApproval', '', 145, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(147, 'Upload Stock', '#/uploadStock', '', 145, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(146, 'Stop Transaction', '#/stopTransaction', '', 145, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(145, 'Stock Adjustments', '', '', 121, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(144, 'Returnable Gatepass', '#/returnableGatepass', '', 123, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(143, 'Assest Support Status', '#/assetsSupportStatus', '', 123, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(142, 'Material Inwards Approval', '#/materialInwardsApproval', '', 123, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(141, 'Material Inwards', '#/materialInwards', '', 123, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(140, 'Recall Drugs', '#/recallDrugs', '', 123, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(139, 'Drug Recall', '#/drugRecall', '', 123, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(138, 'Batch Exp Date Change Approval', '#/batchExpDateChangeApproval', '', 123, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(137, 'Batch Exp Date Change', '#/batchExpDateChange', '', 123, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(136, 'Pending For Shelf Allocation', '#/pendingForShelfAllocation', '', 123, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(135, 'Store Stock Dashboard', '#/storeStockDashboard', '', 123, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(134, 'Returnable Gatepass Approval', '#/returnaleGatepassApproval', '', 123, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(133, 'Waste Store', '#/wasteStore', '', 123, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(132, 'Material Picker', '#/materialPicker', '', 123, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(131, 'Rejected Material Acceptance', '#/rejectedMaterialAcceptance', '', 123, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(130, 'Material Return Acceptance', '#/materialReturnAcceptance', '', 123, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(129, 'Material Return', '#/materialReturn', '', 123, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(128, 'Material Issue Acceptance', '#/materialIssueAcceptance', '', 123, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(127, 'Material Issue', '#/materialIssue/pendingIssue', '', 123, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(126, 'Pending Store Indents', '#/pendingStoreIndents', '', 123, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(125, 'Store Indent Approval', '#/storeIndentApproval', '', 123, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(124, 'Store Indent', '#/storeIndenting', '', 123, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(123, 'Store', '', '', 121, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(122, 'Nursing', 'No Link', '', 121, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(121, 'Inventory', '', '', 0, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(120, 'Patient Consumbale Issue', 'patientConsumableIssue', '', 119, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(119, 'IN Patient Pharmacy', '', '', 101, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(118, 'Empty Ampoules', '#/acceptanceOfEmptyAmpoules', '', 115, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(117, 'Delivery Worklist', '#/deliveryWokrlist', '', 115, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(116, 'Issue Worklist', '#/narcoticsIssueWorklist', '', 115, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(115, 'Narcotics Store', '', '', 101, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(114, 'Walk in Sales', '#/walkInSales', '', 110, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(113, 'Bill List', '#/pharmacyBillList', '', 110, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(112, 'Sales Return', '#/billSalesReturn', '', 110, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(111, 'Sales Worklist', '#/outPatientSalesWorklist', '', 110, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(110, 'OP Pharmacy', '', '', 101, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(109, 'Return Acceptance', '#/patientReturnAcceptance', '', 102, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(108, 'Rejected Rejection', '#/pendingRejectedRejections', '', 102, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(107, 'Patient Issue Rejection', '#/patientIssueRejection', '', 102, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(106, 'Pending Handover WorkList', '#/pendingHandoverWorkList', '', 102, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(105, 'Pending Issues', '#/pendingIssues', '', 102, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(104, 'Assign Picker', '#/assigningPickerDispenser', '', 102, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(103, 'Pharmacology Validation', '#/pharmacologyValidaionWorklist', '', 102, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(102, 'IP Pharmacy', '', '', 101, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(101, 'Pharmacy', '', '', 0, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(100, 'Input Output Chart History', '#/inputOutputChartHistory', '', 243, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(99, 'Input Output Charts', '#/inputOutputCharts', '', 692, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(98, 'Charting', '', '', 44, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(97, 'Washing Request', '#/washingRequest', '', 93, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(96, 'Linen Issue Acceptance', '#/linenIssueacceptance', '', 93, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(95, 'Linen Indent Approval', '#/linenIndentApproval', '', 93, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(94, 'Linen Indent', '#/linenIndent', '', 93, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(93, 'Linen-Laundry', '', '', 44, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(92, 'Kit Return', '#/KitReturn', '', 88, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(91, 'Kit Issue Acceptance', '#/KitIndentAcceptance', '', 88, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(90, 'Kit Indent Approval', '#/KitIndentApproval', '', 88, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(89, 'Kit Indent', '#/KitIndent', '', 88, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(88, 'CSSD', '', '', 44, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(87, 'Crash Cart Request Approval', '#/crashCartRequest', '', 85, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(86, 'Crash Cart Request', '#/crashCartRequest', '', 85, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(85, 'Crash Cart', '', '', 44, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(84, 'Accept Cleaned Bed', '#/cleanedBedAccept', '', 83, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(83, 'House Keeping', '', '', 44, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(82, 'Return Empty Ampoules', '#/returnOfEmptyAmpoules', '', 81, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(81, 'Narcotics Store', '', '', 44, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(80, 'Medication Entry', '#/homecareMedicationEntry', '', 79, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(79, 'Homecare Medication', '', '', 44, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(78, 'Short Leave Status', '#/shortLeaveStatus', '', 70, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(77, 'Request Short Leave', '#/shortLeaveDischarge', '', 70, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(76, 'Absconding patient', '#/abscondingPatient', '', 70, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(75, 'Final Discharge', '#/finalDischarge', '', 70, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(74, 'Ready For Billing', '#/readyForBilling', '', 70, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(73, 'Medication Hand Over', '#/dischargeMedicationHandover', '', 70, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(72, 'Medication Reconciliation', '#/dischargeMedicationReconciliation', '', 70, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(71, 'Marked For Discharge', '#/patientForDischarge', '', 70, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(70, 'Discharge', '', '', 44, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(69, 'Investigation Transfer', '#/transferForInvestigation/investigationTransferRequest', '', 65, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(68, 'OT Transfer', '#/transferToOT/OTTransferRequest', '', 65, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(67, 'Transfer Request', '#/transferRequest/initiateRequestTransferBed', '', 65, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(66, 'Initiate Bed Transfer', '#/initiateBedTransfer/initiateRequestTransferBed', '', 65, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(65, 'Transfer', '', '', 44, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(64, 'Accept Consumable', '#/consumableAcceptance', '', 62, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(63, 'Non-Billable Consumption', '#/nonBillableConsumption', '', 62, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(62, 'Inventory', '', '', 44, 1, NULL, 'D', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(61, 'Return Billable Consumption', '#/returnBillableConsumption', '', 57, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(60, 'Billable Consumption', '#/billableConsumption', '', 57, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(59, 'Patient Indent', '#/patientIndent', '', 57, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(58, 'Issue Acceptance', '#/patientIssueAcceptance', '', 57, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(57, 'Pharmacy', '', '', 44, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(56, 'Periodicity Service', '#/periodicityService', '', 692, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(55, 'Patient Issue Return', '#/patientIssueReturn', '', 692, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(54, 'Homecare Medication Entry', '#/homecareMedicationEntry', '', 692, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(53, 'Pending Investigations', '#/pendingInvestigations', '', 692, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(52, 'ICU Booking', '#/icuBedBookingStatus', '', 45, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(51, 'Nursing Orders', '#/nursingOrderView', '', 45, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(50, 'Sample Collection', '#/sampleCollection', '', 692, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(49, 'Drug Administration', '#/drugAdministrator', '', 692, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(48, 'Map Of Bed', '#/bedManagement', '', 692, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(47, 'Allergies', '#/allergies', '', 692, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(46, 'Patient Acceptance', '#/patientAcceptance/newAdmissionList', '', 692, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(45, 'Workstation', '', '', 44, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(44, 'Nursing', '', '', 0, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(43, 'Pre Authorization Request', '#/preAuthorizationRequest', '', 886, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(42, 'Update Payee Information', '#/updatePayeeInformation', '', 886, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(41, 'Intermediate Billing', '#/intermediateBill', '', 37, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(40, 'IPD Billing', '#/IPDBilling', '', 37, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(39, 'IPD Charges', '#/IPDCharges', '', 886, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(38, 'Bill Estimation', '#/billEstimation', '', 37, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(37, 'Billing', '', '', 26, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(36, 'Acceptance Transfer Request', '#/transferRequestAcceptance', '', 35, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(35, 'Transfers', '', '', 26, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(34, 'Issued Pass', '#/passIssued', '', 27, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(33, 'Admitted patient List', '#/admittedPatientList', '', 27, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(32, 'Bed Allocation', '#/bedAllocation', '', 27, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(31, 'Bed Availability', '#/bedAvailability', '', 27, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(30, 'Bed Reservation List', '#/bedReservationList', '', 27, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(29, 'Pending Admission', '#/pendingAdmissionDischarge', '', 27, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(28, 'Admission Request', '#/admissionRequest', '', 27, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(27, 'Admission', '', '', 26, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(26, 'ADT', '', '', 0, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(25, 'Referral Doctor Share Process', '#/referralDoctorShareProcess', '', 22, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(24, 'Doctor Share Payment Voucher', '#/doctorSharePaymentVoucher', '', 22, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(23, 'Doctor Share Process', '#/doctorShareProcess', '', 22, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(22, 'Doctor Share', '', '', 678, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(21, 'Refund Against CR', '#/refundAgainstCR', '', 886, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(20, 'Refund List', '#/refundList', '', 886, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(19, 'Refund Against Deposit', '#/refundAgainstDeposit', '', 886, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(18, 'Refund', '#/refund', '', 10, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(17, 'Credit Note List', '#/creditNoteList', '', 10, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(16, 'Credit Note', '#/creditNote', '', 886, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(15, 'Debit Note List', '#/debitNoteList', '', 886, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(14, 'Debit Note', '#/debitNote', '', 886, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(13, 'Bill List', '#/billList', '', 886, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(12, 'Deposit', '#/deposit', '', 886, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(11, 'OPD Bill', '', '', 886, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(10, 'Billing', '', '', 1, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(9, 'OPD Coversheet', '#/encounterQueueManagement', '', 2, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(8, 'Patient List', '#/patientList', '', 2, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(7, 'Appointment List', '#/appointmentList', '', 2, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(6, 'Encounter Queue Management', '#/encounterQueueManagement', '', 2, 1, NULL, 'I', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(5, 'Encounter', '#/encounter/encounterDetails', '', 2, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(4, 'Appointment Scheduling', '#/appointmentScheduling', '', 2, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(3, 'Patient Registration', '#/patientRegistration', '', 2, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(2, 'Patient Management', '', '', 1, 1, NULL, 'A', 1);
INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(1, 'OPD', '', '', 0, 1, NULL, 'A', 1);


INSERT INTO public.m_store_type
(id, organization_id, unit_id, code, "type", is_active, updated_by, updated_date, added_by, added_date, is_deleted)
VALUES(1, 1, 1, 'MS', 'Main Store', true, 1, '2018-03-25 11:15:01.000', 1, '2018-03-25 11:15:01.000', false);
INSERT INTO public.m_store_type
(id, organization_id, unit_id, code, "type", is_active, updated_by, updated_date, added_by, added_date, is_deleted)
VALUES(2, 1, 1, 'SS', 'Sub Store', true, 1, '2018-03-25 11:16:15.000', 1, '2018-03-25 11:16:15.000', false);
INSERT INTO public.m_store_type
(id, organization_id, unit_id, code, "type", is_active, updated_by, updated_date, added_by, added_date, is_deleted)
VALUES(3, 1, 1, 'CS', 'Consignment Store', true, 1, '2018-03-25 11:16:15.000', 1, '2018-03-25 11:16:15.000', false);
INSERT INTO public.m_store_type
(id, organization_id, unit_id, code, "type", is_active, updated_by, updated_date, added_by, added_date, is_deleted)
VALUES(4, 1, 1, 'CSSDS', 'CSSD Store', true, 1, '2018-03-25 11:16:15.000', 1, '2018-03-25 11:16:15.000', false);
INSERT INTO public.m_store_type
(id, organization_id, unit_id, code, "type", is_active, updated_by, updated_date, added_by, added_date, is_deleted)
VALUES(5, 1, 1, 'LS', 'Linen Store', true, 1, '2018-03-25 11:16:15.000', 1, '2018-03-25 11:16:15.000', false);
INSERT INTO public.m_store_type
(id, organization_id, unit_id, code, "type", is_active, updated_by, updated_date, added_by, added_date, is_deleted)
VALUES(6, 1, 1, 'NS', 'Narcotics Store', true, 1, '2018-03-25 11:16:15.000', 1, '2018-03-25 11:16:15.000', false);



INSERT INTO billing.m_request_type_master
(request_type_id, request_type_desc, status, created_by, created_date, updated_by, updated_date)
VALUES(1, 'PreAuthorization Request', 'A', 1, '2018-09-24 00:00:00.000', 1, '2018-09-24 00:00:00.000');
INSERT INTO billing.m_request_type_master
(request_type_id, request_type_desc, status, created_by, created_date, updated_by, updated_date)
VALUES(2, 'Top up Request', 'A', 1, '2018-09-24 00:00:00.000', 1, '2018-09-24 00:00:00.000');
INSERT INTO billing.m_request_type_master
(request_type_id, request_type_desc, status, created_by, created_date, updated_by, updated_date)
VALUES(3, 'Final Request', 'A', 1, '2018-09-24 00:00:00.000', 1, '2018-09-24 00:00:00.000');

INSERT INTO lab.m_send_throgh_master
(send_through_id, send_throug_desc, status, created_by, created_date, updated_by, updated_date)
VALUES(1, 'By Hand', 'A', 1, '2018-09-06 14:12:36.684', 1, '2018-09-06 14:12:36.684');
INSERT INTO lab.m_send_throgh_master
(send_through_id, send_throug_desc, status, created_by, created_date, updated_by, updated_date)
VALUES(2, 'Fax', 'A', 1, '2018-09-06 14:12:36.684', 1, '2018-09-06 14:12:36.684');
INSERT INTO lab.m_send_throgh_master
(send_through_id, send_throug_desc, status, created_by, created_date, updated_by, updated_date)
VALUES(3, 'Courier', 'A', 1, '2018-09-06 14:12:36.684', 1, '2018-09-06 14:12:36.684');

INSERT INTO billing.m_authorization_status_master
(auth_status_id, auth_status_desc, status, created_by, created_date, updated_by, updated_date)
VALUES(1, 'Approved', 'A', 1, '2018-09-24 00:00:00.000', 1, '2018-09-24 00:00:00.000');
INSERT INTO billing.m_authorization_status_master
(auth_status_id, auth_status_desc, status, created_by, created_date, updated_by, updated_date)
VALUES(3, 'Reject', 'A', 1, '2018-09-24 00:00:00.000', 1, '2018-09-24 00:00:00.000');
INSERT INTO billing.m_authorization_status_master
(auth_status_id, auth_status_desc, status, created_by, created_date, updated_by, updated_date)
VALUES(4, 'On Hold', 'A', 1, '2018-09-24 00:00:00.000', 1, '2018-09-24 00:00:00.000');
INSERT INTO billing.m_authorization_status_master
(auth_status_id, auth_status_desc, status, created_by, created_date, updated_by, updated_date)
VALUES(5, 'Cancel', 'A', 1, '2018-09-24 00:00:00.000', 1, '2018-09-24 00:00:00.000');
INSERT INTO billing.m_authorization_status_master
(auth_status_id, auth_status_desc, status, created_by, created_date, updated_by, updated_date)
VALUES(6, 'Pending', 'A', 1, '2018-09-24 00:00:00.000', 1, '2018-09-24 00:00:00.000');
INSERT INTO billing.m_authorization_status_master
(auth_status_id, auth_status_desc, status, created_by, created_date, updated_by, updated_date)
VALUES(2, 'Partially Approved', 'A', 1, '2018-09-24 00:00:00.000', 1, '2018-09-24 00:00:00.000');


---- MRD
INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required)
VALUES(189, 'MRD File Management', true, 1, '2018-03-24 06:01:06.000', 1, '2018-03-24 06:01:06.000', false, 'MRD', true);

INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required, organization_id, is_global)
VALUES(190, 'MRD Request No', true, 1, '2018-03-24 06:01:06.000', 1, '2018-03-24 06:01:06.000', false, 'MRD-REQ', true, null, false);

INSERT INTO public.m_screen
(id, "name", is_active, updated_by, updated_date, added_by, added_date, is_deleted, code, is_prefix_required, organization_id, is_global)
VALUES(191, 'MRD Issue No', true, 1, '2018-03-24 06:01:06.000', 1, '2018-03-24 06:01:06.000', false, 'MRD-ISS', true, null, false);

INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(1065, 'POC', '#/visitorInformation', '', 1041, 1, NULL, 'A', 1);

INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(1066, 'Visitor Process', '#/visitorProcess', '', 1041, 1, NULL, 'A', 1);

INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(1068, 'MRD Rack Master', '#/rackMasters', '', 532, 1, NULL, 'A', 1);

INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(1069, 'MRD Box Master', '#/boxMasters', '', 532, 1, NULL, 'A', 1);

INSERT INTO public.m_action_master
(action_id, action_name, action_link, action_icon, parent_id, created_by, created_date, status, module_type)
VALUES(1070, 'MRD Row Master', '#/rowMasters', '', 532, 1, NULL, 'A', 1);



