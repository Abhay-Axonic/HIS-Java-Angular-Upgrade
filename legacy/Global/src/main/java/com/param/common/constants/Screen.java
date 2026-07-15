package com.param.common.constants;

public enum Screen {
	WalkinSale(1), StoreIndent(2), OpeningBalance(3), PurchaseRequest(4), LocalPurchaseRequest(5), StoreIndentApproval(6), LocalPurchaseRequestApproval(7), 
	PurchaseRequestApproval(8), BillList(9), PurchaseOrderCreation(10), PurchaseOrderApproval(11), GoodReceiptNote(12), GoodReceiptNoteApproval(13), 
	PendingStoreIndent(14), SaleWorkList(15), SaleReturn(16), ItemRequest(17), ItemRequestApproval(18), MaterialPicker(19),ItemCreationPending(21), 
	MaterialIssue(20), PharmacologyValidation(22),HandoverWorklist(23), AssignPicker(24), InPatientPendingIssue(25), InPatientIssueAcceptance(26), PatientIndent(27), BillableConsumption(28),
	MaterialIssueAcceptance(29), PendingIssues(30), PhysicalInspection(31), IssueAcceptance(32), IssueRejection(33), MaterialRejectAcceptance(34),
	RejectedRejection(35), GRNAgainstPo(36), QuotationEnquiry(37), EnquirySendtoVendor(38), StockDashboard(39), BatchExpiry(40), MaterialReturn(41), BatchExpiryApproval(42),
	PatientIssueConsumable(43), NonBillableConsumption(44),	ConsumableAcceptance(45), MaterialReturnAcceptance(46), PendingHandoverWorklist(47), PatientIssueRejection(48),
	PatientReturnAcceptance(49), ReturnableGatepass(50), ReturnableGatepassApproval(51),QuotationFillIn(52),ScrapRequest(53),
    PatientIssueReturn(54), ScrapRequestApproval(55), IssueWorklist(56), RequestAssetDisposalWorklist(57),WasteAssetDisposalWorklist(58), WitnessApproval(59),
    ReturnOfEmptyAmpoules(60), DeliveryWorklist(61), AcceptanceEmptyAmpoules(62),ReturnBillableConsumption(64), TechnicalApproval(65), MaterialInward(66), MaterialInwardApproval(67), CrashCartRequest(68),
    CrashCartMedicationAdministrationApproval(69), AddNewFixedAsset(70), CrashCartRequestApproval(71),VendorNegociation(72),LoanIn(73),LoanInOut(74),LoanOut(75),StopTransaction(76),
    DrugRecall(77), AuctionRequisition(78), LoanOutReceive(79), ItemAuction(80),VendorSelection(81),RecallDrug(82), UploadStock(83), AuctionVendorApproval(84), UploadStockApproval(85),
    VarianceApproval(87), ComputeStock(86), AuctionRequisitionApproval(88), NonReturableGatepass(63), ConfirmStock(89), AssetIndent(90), AssetIndentApproval(91),WorkorderRequest(92),WorkorderRequestApproval(93),
    AssetNonReturnable(94), LinenIndent(95), LinenIndentApproval(96),WorkorderQuotationRequest(97),WorkorderQuotationSendtoVendor(98),
    WorkorderQuotationEntry(99),LinenIssue(100),LinenIssueAcceptance(101),WorkorderQuotationNegotiation(102),WorkorderQuotationSelection(103),AssetIssue(104), AssetIssueAcceptance(105),
    AssetRejectAcceptance(106), AssetReturn(109), AssetReturnAcceptance(107), LinenReturn(108), LinenReturnAcceptance(111), MaterialIssuePending(113), PatientPendingIssue(114),DischargeMedicationReconciliation(115),
    DischargeMedicationHandover(116),HomecareMedicationEntry(117),WorkorderQuotationCreation(118),LinenRejectAcceptance(112), ConsignmentInward(120),ConsignmentOutward(121),ImplantIndent(122),OTCompletion(127),
  
    PatientConsignment(128),ConsignmentReturn(129), ReturnAcceptance(130),ImplantIndentAcceptance(131),PatientReject(132),OpeningBalanceImplant(137),ConsignmentPurchaseOrder(139),ConsignmentPurchaseOrderApproval(145),
    MaterialInwardNote(162), MaterialInwardReplacement(163),ConsignmentConversion(167),LotNoCreation(168),PatientIssueAcceptance(171);


	private Integer screenId;

	public Integer getScreen() {
		return this.screenId;
	}

	private Screen(Integer screenId) {
		this.screenId = screenId;
	}
}