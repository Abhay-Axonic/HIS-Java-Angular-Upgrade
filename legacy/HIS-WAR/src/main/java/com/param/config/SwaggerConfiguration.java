/*package com.param.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {*/

/*	private ApiInfo apiInfo(String title, String description) {

		return new ApiInfoBuilder().title(title).description(description).termsOfServiceUrl("http://springfox.io")
				.license("Apache License Version 2.0")
				.licenseUrl("https://github.com/springfox/springfox/blob/master/LICENSE").version("1.0").build();
	}
*/
	/*@Bean
	public Docket adtApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo("ADT", "API for ADT module for HIS Application"))
				.groupName("ADT").select().apis(RequestHandlerSelectors.basePackage("com.param.adt"))
				.paths(PathSelectors.any()).build();
	}*/

	/*@Bean
	public Docket assetManagementApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("AssetManagement", "API for AssetManagement Module of HIS Application"))
				.groupName("AssetManagement").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.assetManagement")).paths(PathSelectors.any())
				.build();
	}
*/
	/*@Bean
	public Docket billingApi() {
		return new Docket(DocumentationType.SWAGGER_12)
				.apiInfo(apiInfo("BILLING", "API for Billing module for HIS Application")).groupName("Billing").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.billing")).paths(PathSelectors.any()).build();
	}

	@Bean
	public Docket bloodBankApi() {
		return new Docket(DocumentationType.SWAGGER_12)
				.apiInfo(apiInfo("BlOODBANK", "API for BloodBank module for HIS Application")).groupName("BloodBank")
				.select().apis(RequestHandlerSelectors.basePackage("com.param.bloodBank")).paths(PathSelectors.any())
				.build();
	}

	@Bean
	public Docket cssdMasterApi() {
		return new Docket(DocumentationType.SWAGGER_12)
				.apiInfo(apiInfo("CSSDMaster", "API for CSSDMaster module for HIS Application")).groupName("CSSDMaster")
				.select().apis(RequestHandlerSelectors.basePackage("com.param.CSSDMaster")).paths(PathSelectors.any())
				.build();
	}

	@Bean
	public Docket dietAndKitchenApi() {
		return new Docket(DocumentationType.SWAGGER_12)
				.apiInfo(apiInfo("Kitchen", "API for DietAndKitchen module for HIS Application")).groupName("Kitchen")
				.select().apis(RequestHandlerSelectors.basePackage("com.param.kitchen")).paths(PathSelectors.any())
				.build();
	}

	@Bean
	public Docket dietAndKitchenGlobalMasterApi() {
		return new Docket(DocumentationType.SWAGGER_12)
				.apiInfo(apiInfo("DietAndKitchenGlobalMaster", "API for DietAndKitchen module for HIS Application"))
				.groupName("DietAndKitchenGlobalMaster").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.global.master")).paths(PathSelectors.any())
				.build();
	}

	@Bean
	public Docket globalActionMasterApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("GlobalActionMaster", "API for Global Module of HIS Application"))
				.groupName("GlobalActionMaster").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.actionmaster")).paths(PathSelectors.any()).build();

	}

	@Bean
	public Docket globalEquipmentchecklistApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("GlobalEquipmentchecklist", "API for Global Module of HIS Application"))
				.groupName("Global_Equipmentchecklist").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.equipmentchecklist")).paths(PathSelectors.any())
				.build();

	}

	@Bean
	public Docket globalBiReportApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("GlobalBiReport", "API for Global Module of HIS Application"))
				.groupName("GlobalBiReport").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.global.bi.report")).paths(PathSelectors.any())
				.build();

	}

	@Bean
	public Docket globalBillingApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("GlobalBilling", "API for Global Module of HIS Application"))
				.groupName("GlobalBilling").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.global.billing")).paths(PathSelectors.any())
				.build();

	}*/

	/*@Bean
	public Docket globalApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("Global", "API for Global Module of HIS Application")).groupName("Global").select()//// UNAUTHORIZED
				.apis(RequestHandlerSelectors.basePackage("com.param.global")).paths(PathSelectors.any()).build();

	}*/

	/*@Bean
	public Docket globalDocatorApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("GlobalDoctor", "API for Global Module of HIS Application")).groupName("GlobalDoctor")
				.select().apis(RequestHandlerSelectors.basePackage("com.param.global.doctor"))
				.paths(PathSelectors.any()).build();

	}

	@Bean
	public Docket globalMpesaApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("GlobalMpesa", "API for Global Module of HIS Application")).groupName("GlobalMpesa")
				.select().apis(RequestHandlerSelectors.basePackage("com.param.global.mpesa")).paths(PathSelectors.any())
				.build();

	}

	@Bean
	public Docket globalMrdApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("GlobalMrd", "API for Global Module of HIS Application")).groupName("GlobalMrd")
				.select().apis(RequestHandlerSelectors.basePackage("com.param.global.mrd")).paths(PathSelectors.any())
				.build();

	}

	@Bean
	public Docket globalOpdApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("GlobalOpd", "API for Global Module of HIS Application")).groupName("GlobalOpd")
				.select().apis(RequestHandlerSelectors.basePackage("com.param.global.opd")).paths(PathSelectors.any())
				.build();

	}

	@Bean
	public Docket globalCommonApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("GlobalCommon", "API for Global Module of HIS Application")).groupName("GlobalCommon")
				.select().apis(RequestHandlerSelectors.basePackage("com.param.org.common")).paths(PathSelectors.any())
				.build();

	}

	@Bean
	public Docket globalOrgCommonApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("GlobalOrgCommon", "API for Global Module of HIS Application"))
				.groupName("GlobalOrgCommon").select()
				.apis(RequestHandlerSelectors.basePackage("com.param..global.org.common.controller"))
				.paths(PathSelectors.any()).build();

	}

	@Bean
	public Docket globalOrg() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("GlobalOrg", "API for Global Module of HIS Application")).groupName("GlobalOrg")
				.select().apis(RequestHandlerSelectors.basePackage("com.param..global.org.controller"))
				.paths(PathSelectors.any()).build();

	}

	@Bean
	public Docket inventoryApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("Inventory", "API for Inventory Module of HIS Application")).groupName("Inventory")
				.select().apis(RequestHandlerSelectors.basePackage("com.param.inventory")).paths(PathSelectors.any())
				.build();

	}

	@Bean
	public Docket linenLaundryApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("LinenLaundry", "API for LinenLaundry Module of HIS Application"))
				.groupName("LinenLaundry").select().apis(RequestHandlerSelectors.basePackage("com.param.linenLaundry"))
				.paths(PathSelectors.any()).build();

	}

	@Bean
	public Docket labCommonApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("LabCommon", "API for Lab Module of HIS Application")).groupName("LisCommon").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.lis.common")).paths(PathSelectors.any()).build();

	}

	@Bean
	public Docket lisGlobalApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("LIS_Global").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.lis.global.controller")).paths(PathSelectors.any())
				.build().apiInfo(apiInfo("LISGlobal", "Laboratory Information System(LIS) API'S FOR GLOBAL MASTERS."));

	}

	@Bean
	public Docket lisHistopathologyTransactionApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("Lis_Histopathology").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.lis.histopathology.transaction"))
				.paths(PathSelectors.any()).build().apiInfo(apiInfo("LisHistopathologyTransaction",
						"Laboratory Information System(LIS) API'S FOR HISTOPATHOLOGY TRANSACTION."));
	}

	@Bean
	public Docket lisTransactiontMicroApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("LIS_Transaction_Microbiology").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.lis.microbiology.transaction"))
				.paths(PathSelectors.any()).build()
				.apiInfo(apiInfo("LISTransactiontMicro", "Laboratory Information System(LIS) API'S FOR Microbiology."));
	}

	@Bean
	public Docket labReportApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("LIS_Report").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.lis.reports")).paths(PathSelectors.any()).build()
				.apiInfo(apiInfo("LabReport", "Laboratory Information System(LIS) API'S FOR LAB REPORT."));
	}

	@Bean
	public Docket lisSerologyTransactionApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("LIS_Serology_Transaction").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.lis.serology.transaction"))
				.paths(PathSelectors.any()).build()
				.apiInfo(apiInfo("LISSerologyTransaction", "Laboratory Information System(LIS) API'S FOR SEROLOGY TRANSACTION."));
	}*/
/*
	@Bean
	public Docket lisTransactiontApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("LIS_Transaction").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.lis.transaction")).paths(PathSelectors.any())// UNAUTHORIZED
				.build().apiInfo(apiInfo("LisTransaction", "Laboratory Information System(LIS) API'S FOR TRANSACTIONS."));
	}

	@Bean
	public Docket lisUnitApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("LIS_Unit").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.lis.unit")).paths(PathSelectors.any()).build()// UNAUTHORIZED
				.apiInfo(apiInfo("LISUnit", "Laboratory Information System(LIS) API'S FOR UNIT MASTERS."));
	}*/

	/*@Bean
	public Docket otimsApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("OTIMS", "API for OTIMS Module of HIS Application")).groupName("OTIMS").select()//UNAUTHORIZED
				.apis(RequestHandlerSelectors.basePackage("com.param.otims")).paths(PathSelectors.any()).build();

	}*/

	/*@Bean
	public Docket pharmacyInpatientApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("PharmacyInpateint", "API for Pharmacy Module of HIS Application"))
				.groupName("PharmacyInpatient").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.pharmacy.inPatient")).paths(PathSelectors.any())
				.build();
	}

	@Bean
	public Docket pharmacyIpApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("PharmacyIp", "API for Pharmacy Module of HIS Application")).groupName("PharmacyIp")
				.select().apis(RequestHandlerSelectors.basePackage("com.param.pharmacy.ip")).paths(PathSelectors.any())
				.build();
	}

	@Bean
	public Docket pharmacyMasterGlobalApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("PharmacyMasterGlobal", "API for Pharmacy Module of HIS Application"))
				.groupName("PharmacyMasterGlobal").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.pharmacy.master.global"))
				.paths(PathSelectors.any()).build();
	}

	@Bean
	public Docket pharmacyMasterUnitApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("PharmacyMasterUnit", "API for Pharmacy Module of HIS Application"))
				.groupName("PharmacyMasterUnit").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.pharmacy.master.unit")).paths(PathSelectors.any())
				.build();
	}

	@Bean
	public Docket pharmacyNarcoticsAcceptanceOfEmptyAmpoulesApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("PharmacyNarcoticsAcceptanceOfEmptyAmpoules",
						"API for Pharmacy Module of HIS Application"))
				.groupName("PharmacyNarcoticsAcceptanceOfEmptyAmpoules").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.pharmacy.narcotics.acceptanceOfEmptyAmpoules"))
				.paths(PathSelectors.any()).build();
	}

	@Bean
	public Docket pharmacyNarcoticsDeliveryWorklistApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("PharmacyNarcoticsDeliveryWorklist", "API for Pharmacy Module of HIS Application"))
				.groupName("PharmacyNarcoticsDeliveryWorklist").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.pharmacy.narcotics.deliveryWorklist"))
				.paths(PathSelectors.any()).build();
	}*/

	/*@Bean
	public Docket pharmacyNarcoticsIssueWorkApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("PharmacyNarcoticsIssueWorkList", "API for Pharmacy Module of HIS Application"))
				.groupName("PharmacyNarcoticsIssueWorkList").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.pharmacy.narcotics.issueWorklist"))
				.paths(PathSelectors.any())// UNAUTHORIZED
				.build();
	}

	@Bean
	public Docket pharmacyNursingCrashCartApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("PharmacyNursingCrashCart", "API for Pharmacy Module of HIS Application"))
				.groupName("PharmacyNursingCrashCart").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.pharmacy.nursing.crashCart"))
				.paths(PathSelectors.any())// UNAUTHORIZED
				.build();
	}*/

	/*@Bean
	public Docket pharmacyNursingDischargeApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("PharmacyNursingDischarge", "API for Pharmacy Module of HIS Application"))
				.groupName("PharmacyNursingDischarge").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.pharmacy.nursing.discharge"))
				.paths(PathSelectors.any()).build();
	}
*/
	/*@Bean
	public Docket pharmacyNursingApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("PharmacyNursing", "API for Pharmacy Module of HIS Application"))
				.groupName("PharmacyNursing").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.pharmacy.nursing.pharmacy"))
				.paths(PathSelectors.any()) // UNAUTHORIZED
				.build();
	}*/

	/*@Bean
	public Docket pharmacyNursingWorkstationApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("PharmacyNursingWorkstation", "API for Pharmacy Module of HIS Application"))
				.groupName("PharmacyNursingWorkstation") // UNAUTHORIZED
				.select().apis(RequestHandlerSelectors.basePackage("com.param.pharmacy.nursing.workstation"))
				.paths(PathSelectors.any()).build();
	}

	@Bean
	public Docket pharmacyOpApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("PharmacyPrescription", "API for Pharmacy Module of HIS Application"))
				.groupName("PharmacyPrescription") // UNAUTHORIZED
				.select().apis(RequestHandlerSelectors.basePackage("com.param.pharmacy.op")).paths(PathSelectors.any())
				.build();
	}*/

	/*@Bean
	public Docket pharmacyPrescriptionApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("PharmacyPrescription", "API for Pharmacy Module of HIS Application"))
				.groupName("PharmacyPrescription").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.pharmacy.prescription")).paths(PathSelectors.any())
				.build();
	}

	@Bean
	public Docket pharmacyQmsApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("PharmacyQms", "API for Pharmacy Module of HIS Application")).groupName("PharmacyQms")
				.select().apis(RequestHandlerSelectors.basePackage("com.param.pharmacy.qms")).paths(PathSelectors.any())
				.build();
	}

	@Bean
	public Docket procurementConsignmentApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("ProcurementConsignment", "API for Procurement Module of HIS Application"))// UNAUTHORIZED
				.groupName("ProcurementConsignment").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.procurement.consignment"))
				.paths(PathSelectors.any()).build();

	}

	@Bean
	public Docket procurementConsignmentreturnApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("ProcurementConsignmentreturn", "API for Procurement Module of HIS Application"))
				.groupName("ProcurementConsignmentreturn").select()
				.apis(RequestHandlerSelectors
						.basePackage("com.param.procurement.consignment.controller.consignmentreturn"))
				.paths(PathSelectors.any()).build();

	}

	@Bean
	public Docket procurementImplantReturnAcceptanceApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("ProcurementImplantReturnAcceptance", "API for Procurement Module of HIS Application"))
				.groupName("ProcurementImplantReturnAcceptance").select()
				.apis(RequestHandlerSelectors
						.basePackage("com.param.procurement.consignment.controller.ImplantReturnAcceptance"))
				.paths(PathSelectors.any()).build();

	}

	@Bean
	public Docket procurementloanInOutApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("ProcurementloanInOut", "API for Procurement Module of HIS Application"))
				.groupName("ProcurementloanInOut").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.procurement.loanInOut")).paths(PathSelectors.any())
				.build();

	}

	@Bean
	public Docket procurementPurchaseApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("ProcurementPurchase", "API for Procurement Module of HIS Application"))
				.groupName("ProcurementPurchase").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.procurement.purchase")).paths(PathSelectors.any())
				.build();

	}

	@Bean
	public Docket procurementQuotationApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("ProcurementQuotation", "API for Procurement Module of HIS Application"))
				.groupName("ProcurementQuotation").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.procurement.quotation"))// UNAUTHORIZED
				.paths(PathSelectors.any()).build();

	}

	@Bean
	public Docket procurementWorkOrderApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("ProcurementWorkOrder", "API for Procurement Module of HIS Application"))
				.groupName("ProcurementWorkOrder").select()
				.apis(RequestHandlerSelectors.basePackage("com.param.procurement.workOrder")).paths(PathSelectors.any())
				.build();

	}*/

//	}
