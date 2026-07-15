package com.param.inventory.store.service.materialIssuePending;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.IndentType;
import com.param.entity.model.inventory.MaterialIssue;
import com.param.entity.model.inventory.MaterialIssueBatchMapper;
import com.param.entity.model.inventory.MaterialIssueDetail;
import com.param.entity.model.inventory.MaterialPicker;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreIndentDetail;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.Response;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.global.BatchProperty;
import com.param.global.dto.global.PickerResponse;
import com.param.global.dto.items.BatchResponse;
import com.param.global.dto.items.UomResponse;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.inventory.store.dao.materialIssue.IMaterialIssuePendingDao;
import com.param.inventory.store.dao.materialPicker.IMaterialPickerDao;
import com.param.inventory.store.dao.storeIndent.IStoreIndentDao;
import com.param.inventory.store.dto.materialIssuePending.IssueBatch;
import com.param.inventory.store.dto.materialPicker.MaterialPickerDetailRequest;
import com.param.inventory.store.dto.materialPicker.MaterialPickerDetailResponse;
import com.param.inventory.store.dto.materialPicker.MaterialPickerRequest;
import com.param.inventory.store.dto.materialPicker.MaterialPickerResponse;
import com.param.inventory.store.dto.materialPicker.SubstituteMaterialIssueDetailDto;
import com.param.inventory.store.dto.storeIndent.StoreIndentDropdownResponse;
import com.param.pharmacy.inPatient.dto.patientConsumableIssue.SubstitutePatientConsumableDto;
import com.param.pharmacy.master.unit.dao.applicationConfiguration.IApplicationConfigurationDao;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class MaterialIssuePendingService implements IMaterialIssuePendingService {

	@Autowired
	private SessionService sessionService;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IMaterialPickerDao MaterialPickerDao;

	@Autowired
	IMaterialIssuePendingDao MaterialIssuePendingDao;

	@Autowired
	IApplicationConfigurationDao ApplicationConfigurationDao;
	@Autowired
	IStoreIndentDao StoreIndentDao;

	private Integer screenId = Screen.MaterialIssuePending.getScreen();

	private com.param.entity.model.master.Screen screen;

	private Integer issueScreenId = Screen.MaterialIssuePending.getScreen();

	@Autowired
	IGlobalService GlobalService;

	@Override
	@Transactional
	public StoreIndentDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		StoreIndentDropdownResponse response = new StoreIndentDropdownResponse();

		for (Store store : GlobalDao.getStoreList(unitId)) {
			response.addFromStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (Store store : GlobalDao.getUserStoreList(unitId, orgId, userId)) {
			response.addToStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName(),
					store.getStoreType().getCode().equals("MS")));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}

		for (IndentType indentType : GlobalDao.getIndentTypeList(orgId, unitId)) {
			response.addIndentType(
					new com.param.global.dto.global.IndentType(indentType.getId(), indentType.getType()));
		}

		return response;
	}

	@Override
	@Transactional
	public Object getMaterialIssuePendingList(MaterialPickerRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return MaterialIssuePendingDao.searchMaterialIssuePendingList(request, screenId);
	}

	@Override
	@Transactional
	public Object saveMaterialIssue(MaterialPickerRequest request) {

		screen = GlobalDao.getScreen(screenId);

		MaterialIssue materialIssue = MaterialIssuePendingDao.getMaterialIssue(request.getId());
		
		    Date today = new Date();

		materialIssue.setAddedDate(GlobalCommonDateUtils.getDate(today, "yyyy-MM-dd HH:mm:ss.SSS"));
		Integer statusId=materialIssue.getStatus().getId();
		materialIssue.updateMaterialIssuePending(new User(request.getCarrierId()), request.getRemark(),
				new Status(request.getStatusId()), materialIssue.getStoreIndent().getToStore(),
				materialIssue.getStoreIndent().getFromStore(),new Status(request.getStatusId()));

		Integer storeId = materialIssue.getStoreIndent().getToStore().getStoreId();

		// Unit unit = GlobalDao.getUnit(sessionService.getUnitId());

		/*
		 * MaterialIssue materialIssuenew = new MaterialIssue(new
		 * User(sessionService.getUnitId()), issueNumber, new
		 * Status(com.param.common.constants.Status.Pending.getStatus()),
		 * materialIssue.getStoreIndent(), materialIssue.getMaterialPicker(),
		 * materialIssue.getFromStore(), materialIssue.getToStore());
		 * sessionService.setEntityDefaults(materialIssuenew); Boolean saveIssue =
		 * false;
		 */
		for (MaterialIssueDetail detail : MaterialIssuePendingDao.getMaterialIssueDetail(request.getId())) {
			// System.out.println("Inside First For");
			for (MaterialPickerDetailRequest item : request.getItems()) {
				if(item.getId() != null) {

					// System.out.println("Inside Second For");
					// System.out.println("Item Id : " + item.getId());
					// System.out.println("Details Id : " + detail.getId());
					if (item.getId() != null && item.getId().equals(detail.getId())) {
						if (detail.getMaterialPickerDetail() != null) {
							detail.getMaterialPickerDetail().setPickerQuantity(0);
						}

						detail.setIssueQuantity((detail.getIssueQuantity()!=null ? detail.getIssueQuantity():0)+item.getIssueQuantity());
						detail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));

						// detail.setUpdatedBy(detail.getUser());


						detail.setUpdatedByUser(request.getUserId());

					//	StoreIndentDetail storeIndentDetail = detail.getStoreIndentDetail();
						StoreIndentDetail storeIndentDetail =	StoreIndentDao.getStoreIndentDetailById(detail.getStoreIndentDetail().getId());
						Integer tQty = getIssueQuantity(item.getItemId(), item.getIssueQuantity(),
								item.getBatches().get(0).getUomTypeId(), request.getOrganizationId());
	// update in store stock total_qty
						Integer pendingQty1=getIssueQuantity(item.getItemId(), storeIndentDetail.getAuthorizedQuantity(),
								item.getBatches().get(0).getUomTypeId(), request.getOrganizationId());
						Integer pendingQty2=getIssueQuantity(item.getItemId(), item.getIssueQuantity(),
								item.getBatches().get(0).getUomTypeId(), request.getOrganizationId());
						storeIndentDetail.setPendingQuantity((pendingQty1-pendingQty2) <0 ? 0 : pendingQty1-pendingQty2);
						Integer pendingQty = storeIndentDetail.getPendingQuantity();
						StoreIndentDao.updateStoreIndentD(pendingQty2,detail.getStoreIndentDetail().getId());

						// System.out.println("Pending Qty : " + pendingQty);
						if (pendingQty > 0) {
							MaterialPicker materialPicker = MaterialPickerDao
									.fetchMaterialPicker(request.getPickerDetailsId());
							if (materialPicker != null) {
								materialPicker.setStatus(
										new Status(com.param.common.constants.Status.PartiallyCompleted.getStatus()));
								MaterialIssue materialIssueobj=new MaterialIssue(request.getPickerDetailsId(),
										new Status(com.param.common.constants.Status.PartiallyCompleted.getStatus()));
								materialIssueobj.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
								MaterialPickerDao.saveMaterialIssue(materialIssueobj);
							} else {
								// materialIssue.setStatus(new
								// Status(com.param.common.constants.Status.PartiallyCompleted.getStatus()));
								// MaterialIssuePendingDao.updateMaterialIssuePending(materialIssue);
							}

							
						}
						for (MaterialIssueBatchMapper batchMapper : MaterialIssuePendingDao
								.getMaterialIssueBatchList(item.getId())) {
							Integer issueBatchId = null;

							for (BatchResponse batch : item.getBatches()) {
								if (batch.getId() != null && batch.getId().equals(batchMapper.getId())) {
									// changecode from batchMapper.getBatch().getId() because its a batchMapper id
									// rather than batchId

									issueBatchId = batch.getId();
									
									List<StoreStock> StoreStockList = GlobalDao.getStoreStockListByBatchIdAndStoreId(
											batch.getBatchId(), storeId, request.getUnitId());
									int issuingQty = batch.getIssueQuantity();
									boolean batchMapperUpdateFlag = true;
									for (StoreStock storeStockObj : StoreStockList) {
										if (issuingQty != 0 && batch != null) {
											Long issueQuantity = Util.ConvertQuantity(
													Long.valueOf(storeStockObj.getAvailableQty()),
													storeStockObj.getBatch().getBatchUomList(),
													new UomType(batch.getUomTypeId()));
											Long batchId = storeStockObj.getBatch().getId();
											// if (issuingQty != 0 && batch != null) {
											if (issueQuantity >= issuingQty) {
												Batch issueBatch = BatchStoreTransactionForClubedBatch(storeId, batchId,
														issuingQty, batchMapper.getUomType(),
														materialIssue.getUnit().getUnitId(), request, storeIndentDetail.getItem(), materialIssue.getIssueNo(),detail);
												// issueBatch.setUpdatedBy(new User(sessionService.getUserId()));
												// issueBatch.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
												// sessionService.setEntityUpdateDefaults(issueBatch);
												issueBatch.setUpdateDefaults(request.getUserId(), materialIssue.getUnitId());

												// if (batchId.equals(batch.getBatchId())) {
												if (batchMapperUpdateFlag) {
													Integer issueQtyAdd=batchMapper.getIssueQuantity()!=null?batchMapper.getIssueQuantity() :0;
													batchMapper.updateBatchMapper(issueBatch, statusId.equals(2) ? issuingQty: (issueQtyAdd+issuingQty),
															new UomType(batch.getUomTypeId()),
															new UomUnit(batch.getUomUnitId()));
													batchMapperUpdateFlag = false;
												} else {
													MaterialIssueBatchMapper materialIssueBatchMapper = new MaterialIssueBatchMapper(
															new Batch(batchId), issuingQty,
															new UomType(batch.getUomTypeId()),
															new UomUnit(batch.getUomUnitId()), new Status(
																	com.param.common.constants.Status.Pending.getStatus()));
													// sessionService.setEntityDefaults(materialIssueBatchMapper);
													materialIssueBatchMapper.setCreateDefaults(request.getOrganizationId(),
															request.getUnitId(), request.getUserId());
													detail.addBatch(materialIssueBatchMapper);
												}
												issuingQty = 0;
												break;
											} else {
												int storeAvaliableQuantity = issueQuantity.intValue();

												Batch issueBatch = BatchStoreTransactionForClubedBatch(storeId, batchId,
														storeAvaliableQuantity, batchMapper.getUomType(),
														materialIssue.getUnit().getUnitId(), request,storeIndentDetail.getItem(), materialIssue.getIssueNo(),detail);
												// issueBatch.setUpdatedBy(new User(sessionService.getUserId()));
												// issueBatch.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
												// sessionService.setEntityUpdateDefaults(issueBatch);
												issueBatch.setUpdateDefaults(request.getUserId(), materialIssue.getUnitId());
												// if (batchId.equals(batch.getBatchId())) {
												if (batchMapperUpdateFlag) {
													Integer issueQtyAdd=batchMapper.getIssueQuantity()!=null?batchMapper.getIssueQuantity() :0;
													
												 
													batchMapper.updateBatchMapper(issueBatch, statusId.equals(2) ? batch.getIssueQuantity(): (issueQtyAdd+batch.getIssueQuantity()),
															new UomType(batch.getUomTypeId()),
															new UomUnit(batch.getUomUnitId()));
													batchMapperUpdateFlag = false;
												} else {
													MaterialIssueBatchMapper materialIssueBatchMapper = new MaterialIssueBatchMapper(
															new Batch(batchId), batch.getIssueQuantity(),
															new UomType(batch.getUomTypeId()),
															new UomUnit(batch.getUomUnitId()), new Status(
																	com.param.common.constants.Status.Pending.getStatus()));
													// sessionService.setEntityDefaults(materialIssueBatchMapper);
													materialIssueBatchMapper.setCreateDefaults(request.getOrganizationId(),
															request.getUnitId(), request.getUserId());
													detail.addBatch(materialIssueBatchMapper);
												}
												issuingQty = issuingQty - storeAvaliableQuantity;
											}
										}
									}
									// break; //comment to resolved HIS-7551 Material issue acceptance - Qty
									// mismatch on Material issue acceptance
									// single item can have multiple batch so cant break.

									// end code by minesh for consignmentlogic and clubBatch consumption/transfer

								}
							}

							if (issueBatchId == null) {
							//	batchMapper.setIsDeleted(true);
							}
							// batchMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
							// batchMapper.setUpdatedBy(detail.getUser());

							// sessionService.setEntityUpdateDefaults(batchMapper);
							batchMapper.setUpdateDefaults(request.getUserId(), materialIssue.getUnitId());
						}

						// start code for resolved HIS-9207 or HIS-9218 batchMapper issue added by
						// minesh
						if (MaterialIssuePendingDao.getMaterialIssueBatchList(item.getId()).isEmpty()) {
							for (BatchResponse batch : item.getBatches()) {
								Integer issueBatchId = batch.getId();

								List<StoreStock> StoreStockList = GlobalDao.getStoreStockListByBatchIdAndStoreId(
										batch.getBatchId(), storeId, request.getUnitId());
								int issuingQty = batch.getIssueQuantity();
								for (StoreStock storeStockObj : StoreStockList) {
									
									if (issuingQty != 0 && batch != null && storeStockObj.getBatch().getId().equals(batch.getBatchId())) {
										Long issueQuantity = Util.ConvertQuantity(
												Long.valueOf(storeStockObj.getAvailableQty()),
												storeStockObj.getBatch().getBatchUomList(),
												new UomType(batch.getUomTypeId()));
										Long batchId = storeStockObj.getBatch().getId();
										if (issueQuantity >= issuingQty) {
											Batch issueBatch = BatchStoreTransactionForClubedBatch(storeId, batchId,
													issuingQty, new UomType(batch.getUomTypeId()),
													materialIssue.getUnit().getUnitId(), request,storeIndentDetail.getItem(), materialIssue.getIssueNo(),detail);
											// issueBatch.setUpdatedBy(new User(sessionService.getUserId()));
											// issueBatch.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));

											// sessionService.setEntityUpdateDefaults(issueBatch);
											issueBatch.setUpdateDefaults(request.getUserId(), materialIssue.getUnitId());
											MaterialIssueBatchMapper materialIssueBatchMapper = new MaterialIssueBatchMapper(
													new Batch(batchId), issuingQty, new UomType(batch.getUomTypeId()),
													new UomUnit(batch.getUomUnitId()),
													new Status(com.param.common.constants.Status.Pending.getStatus()));
											// sessionService.setEntityDefaults(materialIssueBatchMapper);
											materialIssueBatchMapper.setCreateDefaults(request.getOrganizationId(),
													request.getUnitId(), request.getUserId());
											detail.addBatch(materialIssueBatchMapper);
											issuingQty = 0;
											break;
										} else {
											int storeAvaliableQuantity = issueQuantity.intValue();
											Batch issueBatch = BatchStoreTransactionForClubedBatch(storeId, batchId,
													storeAvaliableQuantity, new UomType(batch.getUomTypeId()),
													materialIssue.getUnit().getUnitId(), request,storeIndentDetail.getItem(), materialIssue.getIssueNo(), detail);
											// issueBatch.setUpdatedBy(new User(sessionService.getUserId()));
											// issueBatch.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));

											// sessionService.setEntityUpdateDefaults(issueBatch);
											issueBatch.setUpdateDefaults(request.getUserId(), materialIssue.getUnitId());
											MaterialIssueBatchMapper materialIssueBatchMapper = new MaterialIssueBatchMapper(
													new Batch(batchId), batch.getIssueQuantity(),
													new UomType(batch.getUomTypeId()), new UomUnit(batch.getUomUnitId()),
													new Status(com.param.common.constants.Status.Pending.getStatus()));
											// sessionService.setEntityDefaults(materialIssueBatchMapper);
											materialIssueBatchMapper.setCreateDefaults(request.getOrganizationId(),
													request.getUnitId(), request.getUserId());
											detail.addBatch(materialIssueBatchMapper);
											issuingQty = issuingQty - storeAvaliableQuantity;
										}
									}
								}
								// break; //comment to resolved HIS-7551 Material issue acceptance - Qty
								// mismatch on Material issue acceptance
								// single item can have multiple batch so cant break.
								// end code by minesh for consignmentlogic and clubBatch consumption/transfer
							}
						} else {
							// end code for resolved HIS 9207 or HIS-9218 batchMapper issue

							// add below code in else for if batchmapper and detail empty then not entry
							// again
							// below code is use for add new batchmapper in update case or somthing else.

							for (BatchResponse batch : item.getBatches()) {
								

								// comment because of logic change.(single entry but consume by clubbed)
								if (batch.getId() == null) {
									// start code by minesh for consignmentlogic and clubBatch consumption/transfer

									List<StoreStock> StoreStockList = GlobalDao.getStoreStockListByBatchIdAndStoreId(
											batch.getBatchId(), storeId, request.getUnitId());
									int issuingQty = batch.getIssueQuantity();
									for (StoreStock storeStockObj : StoreStockList) {
										if (issuingQty != 0 && batch != null && storeStockObj.getBatch().getId().equals(batch.getBatchId())) {

											UomType uomType = new UomType(batch.getUomTypeId());
											UomUnit uomUnit = new UomUnit(batch.getUomUnitId());
											Long issueQuantity = Util.ConvertQuantity(
													Long.valueOf(storeStockObj.getAvailableQty()),
													storeStockObj.getBatch().getBatchUomList(), uomType);
											Long batchId = storeStockObj.getBatch().getId();
											// if (issuingQty != 0 && batch != null) {
											if (issueQuantity >= issuingQty) {
												Batch issueBatch = BatchStoreTransactionForClubedBatch(storeId, batch.getBatchId(),
														issuingQty, uomType, materialIssue.getUnit().getUnitId(), request,storeIndentDetail.getItem(), materialIssue.getIssueNo(),detail);
												// sessionService.setEntityUpdateDefaults(issueBatch);
												issueBatch.setUpdateDefaults(request.getUserId(), materialIssue.getUnitId());

												MaterialIssueBatchMapper batchMapper = new MaterialIssueBatchMapper(
														issueBatch, issuingQty, uomType, uomUnit);
												batchMapper.setStatus(
														new Status(com.param.common.constants.Status.Pending.getStatus()));

												// sessionService.setEntityDefaults(batchMapper);
												batchMapper.setCreateDefaults(request.getOrganizationId(),
														request.getUnitId(), request.getUserId());
												detail.addBatch(batchMapper);

												issuingQty = 0;
												break;
											} else {
												int storeAvaliableQuantity = issueQuantity.intValue();

												Batch issueBatch = BatchStoreTransactionForClubedBatch(storeId, batchId,
														batch.getIssueQuantity(), uomType,
														materialIssue.getUnit().getUnitId(), request,storeIndentDetail.getItem(), materialIssue.getIssueNo(), detail);
												// sessionService.setEntityUpdateDefaults(issueBatch);
												issueBatch.setUpdateDefaults(request.getUserId(), materialIssue.getUnitId());

												MaterialIssueBatchMapper batchMapper = new MaterialIssueBatchMapper(
														issueBatch, batch.getIssueQuantity(), uomType, uomUnit);

												batchMapper.setStatus(
														new Status(com.param.common.constants.Status.Pending.getStatus()));
												// sessionService.setEntityDefaults(batchMapper);
												batchMapper.setCreateDefaults(request.getOrganizationId(),
														request.getUnitId(), request.getUserId());
												detail.addBatch(batchMapper);

												issuingQty = issuingQty - storeAvaliableQuantity;
											}
										}
									}
									// end code by minesh for consignmentlogic and clubBatch consumption/transfer
								} // end if (batch.getId() == null)
							} // end forloop

						} // add below code in else for if batchmapper and detail empty then not entry
							// again
							// below code is use for add new batchmapper in update case or somthing else.

//						break;
					}
					
				
				}
				if(item.getId() == null && item.getParentId() != null && item.getParentId().equals(detail.getId())){
					SubstituteMaterialIssueDetailDto materialIssueForSub = MaterialIssuePendingDao.getMaterialIssueForSub(request.getId());
					StoreIndentDetail storeIndentDetailSub= MaterialIssuePendingDao.getStoreIndentDetailsForSubstitute(item.getStoreIndentDetailId());
					
					StoreIndentDetail storeIndentSubDetail=new StoreIndentDetail();
					storeIndentSubDetail.setIsActive(true);
					storeIndentSubDetail.setIsDeleted(false);
					storeIndentSubDetail.setStoreIndent(storeIndentDetailSub.getStoreIndent());
					Item itm=new Item();
					itm.setId(item.getItemId());
					storeIndentSubDetail.setItem(itm);
					storeIndentSubDetail.setUomType(storeIndentDetailSub.getUomType());
					storeIndentSubDetail.setUomUnit(storeIndentDetailSub.getUomUnit());
					storeIndentSubDetail.setStatus(storeIndentDetailSub.getStatus());
					storeIndentSubDetail.setParentStoreIndentDtlId(storeIndentDetailSub.getId());
					storeIndentSubDetail.setIndentQuantity(storeIndentDetailSub.getIndentQuantity());
					storeIndentSubDetail.setAuthorizedQuantity(storeIndentDetailSub.getAuthorizedQuantity() != null ? storeIndentDetailSub.getAuthorizedQuantity() :0);
					storeIndentSubDetail.setRejectQuantity(storeIndentDetailSub.getRejectQuantity());
					storeIndentSubDetail.setRejectNote(storeIndentDetailSub.getRejectNote());
					storeIndentSubDetail.setRejectReason(storeIndentDetailSub.getRejectReason());
					storeIndentSubDetail.setPurchaseRequestDetail(storeIndentDetailSub.getPurchaseRequestDetail());
					storeIndentSubDetail.setInTransitQuantity(storeIndentDetailSub.getInTransitQuantity());
					storeIndentSubDetail.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
//					in_transit_quantity
					
					storeIndentSubDetail = StoreIndentDao.saveStoreIndentDetail(storeIndentSubDetail);
					
					MaterialIssueDetail mterialIssueDtl= MaterialIssuePendingDao.getMaterialIssueDetailsForSubstitute(item.getParentId());
					
					MaterialIssueDetail mterialDtl=new MaterialIssueDetail();
					mterialDtl.setIsActive(true);
					mterialDtl.setIsDeleted(false);
					//mterialDtl.setIssueQuantity(item.getIssueQuantity());
					Item itm1=new Item();
					itm1.setId(item.getItemId());
					mterialDtl.setItem(itm1);
					mterialDtl.setMaterialIssue(mterialIssueDtl.getMaterialIssue());
//					mterialDtl.setMaterialIssueBatchMappers(mterialIssueDtl.getMaterialIssueBatchMappers());
					mterialDtl.setParentMaterialIssueId(mterialIssueDtl.getId());
					mterialDtl.setStoreIndentDetail(mterialIssueDtl.getStoreIndentDetail());
					mterialDtl.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
					
					mterialDtl= MaterialIssuePendingDao.saveMaterialIssuePendingDetailsForSubstitute(mterialDtl);
					
					if (item.getParentId() != null && item.getParentId().equals(detail.getId())) {
						if (mterialDtl.getMaterialPickerDetail() != null) {
							mterialDtl.getMaterialPickerDetail().setPickerQuantity(0);
						}

						mterialDtl.setIssueQuantity((mterialDtl.getIssueQuantity()!=null ? mterialDtl.getIssueQuantity():0)+item.getIssueQuantity());
						mterialDtl.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
						mterialDtl.setUpdatedByUser(request.getUserId());

						StoreIndentDetail storeIndentDetail =	StoreIndentDao.getStoreIndentDetailById(storeIndentSubDetail.getId());
						Integer tQty = getIssueQuantity(item.getItemId(), item.getIssueQuantity(),
								item.getBatches().get(0).getUomTypeId(), request.getOrganizationId());
						Integer pendingQty1=getIssueQuantity(item.getItemId(), storeIndentDetail.getAuthorizedQuantity(),
								item.getBatches().get(0).getUomTypeId(), request.getOrganizationId());
						Integer pendingQty2=getIssueQuantity(item.getItemId(), item.getIssueQuantity(),
								item.getBatches().get(0).getUomTypeId(), request.getOrganizationId());
						storeIndentDetail.setPendingQuantity((pendingQty1-pendingQty2) <0 ? 0 : pendingQty1-pendingQty2);
						Integer pendingQty = storeIndentDetail.getPendingQuantity();
						StoreIndentDao.updateStoreIndentD(pendingQty2,mterialDtl.getStoreIndentDetail().getId());

						if (pendingQty > 0) {
							MaterialPicker materialPicker = MaterialPickerDao
									.fetchMaterialPicker(request.getPickerDetailsId());
							if (materialPicker != null) {
								materialPicker.setStatus(
										new Status(com.param.common.constants.Status.PartiallyCompleted.getStatus()));
								MaterialIssue materialIssueobj=new MaterialIssue(request.getPickerDetailsId(),
										new Status(com.param.common.constants.Status.PartiallyCompleted.getStatus()));
								materialIssueobj.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
								MaterialPickerDao.saveMaterialIssue(materialIssueobj);
							} else {
							}
						}
						for (MaterialIssueBatchMapper batchMapper : MaterialIssuePendingDao
								.getMaterialIssueBatchList(item.getId())) {
							Integer issueBatchId = null;

							for (BatchResponse batch : item.getBatches()) {
								if (batch.getId() != null && batch.getId().equals(batchMapper.getId())) {

									issueBatchId = batch.getId();
									List<StoreStock> StoreStockList = GlobalDao.getStoreStockListByBatchIdAndStoreId(
											batch.getBatchId(), storeId, request.getUnitId());
									int issuingQty = batch.getIssueQuantity();
									boolean batchMapperUpdateFlag = true;
									for (StoreStock storeStockObj : StoreStockList) {
										if (issuingQty != 0 && batch != null) {
											Long issueQuantity = Util.ConvertQuantity(
													Long.valueOf(storeStockObj.getAvailableQty()),
													storeStockObj.getBatch().getBatchUomList(),
													new UomType(batch.getUomTypeId()));
											Long batchId = storeStockObj.getBatch().getId();
											if (issueQuantity >= issuingQty) {
												Batch issueBatch = BatchStoreTransactionForClubedBatch(storeId, batchId,
														issuingQty, batchMapper.getUomType(),
														materialIssue.getUnit().getUnitId(), request, storeIndentDetail.getItem(), materialIssue.getIssueNo(),mterialDtl);
												issueBatch.setUpdateDefaults(request.getUserId(), materialIssue.getUnitId());

												if (batchMapperUpdateFlag) {
													Integer issueQtyAdd=batchMapper.getIssueQuantity()!=null?batchMapper.getIssueQuantity() :0;
													batchMapper.updateBatchMapper(issueBatch, statusId.equals(2) ? issuingQty: (issueQtyAdd+issuingQty),
															new UomType(batch.getUomTypeId()),
															new UomUnit(batch.getUomUnitId()));
													batchMapperUpdateFlag = false;
												} else {
													MaterialIssueBatchMapper materialIssueBatchMapper = new MaterialIssueBatchMapper(
															new Batch(batchId), issuingQty,
															new UomType(batch.getUomTypeId()),
															new UomUnit(batch.getUomUnitId()), new Status(
																	com.param.common.constants.Status.Pending.getStatus()));
													materialIssueBatchMapper.setCreateDefaults(request.getOrganizationId(),
															request.getUnitId(), request.getUserId());
													mterialDtl.addBatch(materialIssueBatchMapper);
												}
												issuingQty = 0;
												break;
											} else {
												int storeAvaliableQuantity = issueQuantity.intValue();

												Batch issueBatch = BatchStoreTransactionForClubedBatch(storeId, batchId,
														storeAvaliableQuantity, batchMapper.getUomType(),
														materialIssue.getUnit().getUnitId(), request,storeIndentDetail.getItem(), materialIssue.getIssueNo(),mterialDtl);
												issueBatch.setUpdateDefaults(request.getUserId(), materialIssue.getUnitId());
												if (batchMapperUpdateFlag) {
													Integer issueQtyAdd=batchMapper.getIssueQuantity()!=null?batchMapper.getIssueQuantity() :0;
													
												 
													batchMapper.updateBatchMapper(issueBatch, statusId.equals(2) ? batch.getIssueQuantity(): (issueQtyAdd+batch.getIssueQuantity()),
															new UomType(batch.getUomTypeId()),
															new UomUnit(batch.getUomUnitId()));
													batchMapperUpdateFlag = false;
												} else {
													MaterialIssueBatchMapper materialIssueBatchMapper = new MaterialIssueBatchMapper(
															new Batch(batchId), batch.getIssueQuantity(),
															new UomType(batch.getUomTypeId()),
															new UomUnit(batch.getUomUnitId()), new Status(
																	com.param.common.constants.Status.Pending.getStatus()));
													materialIssueBatchMapper.setCreateDefaults(request.getOrganizationId(),
															request.getUnitId(), request.getUserId());
													mterialDtl.addBatch(materialIssueBatchMapper);
												}
												issuingQty = issuingQty - storeAvaliableQuantity;
											}
										}
									}

								}
							}

							if (issueBatchId == null) {
							}
							batchMapper.setUpdateDefaults(request.getUserId(), materialIssue.getUnitId());
						}

						// minesh
						if (MaterialIssuePendingDao.getMaterialIssueBatchList(item.getId()).isEmpty()) {
							for (BatchResponse batch : item.getBatches()) {
								Integer issueBatchId = batch.getId();
								//Integer issueBatchId = batch.getBatchId()!=null?batch.getBatchId().intValue():batch.getId();

								List<StoreStock> StoreStockList = GlobalDao.getStoreStockListByBatchIdAndStoreId(
										batch.getBatchId(), storeId, request.getUnitId());
								int issuingQty = batch.getIssueQuantity();
								for (StoreStock storeStockObj : StoreStockList) {
									
									if (issuingQty != 0 && batch != null && storeStockObj.getBatch().getId().equals(batch.getBatchId())) {
										Long issueQuantity = Util.ConvertQuantity(
												Long.valueOf(storeStockObj.getAvailableQty()),
												storeStockObj.getBatch().getBatchUomList(),
												new UomType(batch.getUomTypeId()));
										Long batchId = storeStockObj.getBatch().getId();
										if (issueQuantity >= issuingQty) {
											Batch issueBatch = BatchStoreTransactionForClubedBatch(storeId, batchId,
													issuingQty, new UomType(batch.getUomTypeId()),
													materialIssue.getUnit().getUnitId(), request,storeIndentDetail.getItem(), materialIssue.getIssueNo(),mterialDtl);
											issueBatch.setUpdateDefaults(request.getUserId(), materialIssue.getUnitId());
											MaterialIssueBatchMapper materialIssueBatchMapper = new MaterialIssueBatchMapper(
													new Batch(batchId), issuingQty, new UomType(batch.getUomTypeId()),
													new UomUnit(batch.getUomUnitId()),
													new Status(com.param.common.constants.Status.Pending.getStatus()));
											materialIssueBatchMapper.setCreateDefaults(request.getOrganizationId(),
													request.getUnitId(), request.getUserId());
											mterialDtl.addBatch(materialIssueBatchMapper);
											issuingQty = 0;
											break;
										} else {
											int storeAvaliableQuantity = issueQuantity.intValue();
											Batch issueBatch = BatchStoreTransactionForClubedBatch(storeId, batchId,
													storeAvaliableQuantity, new UomType(batch.getUomTypeId()),
													materialIssue.getUnit().getUnitId(), request,storeIndentDetail.getItem(), materialIssue.getIssueNo(), mterialDtl);
											issueBatch.setUpdateDefaults(request.getUserId(), materialIssue.getUnitId());
											MaterialIssueBatchMapper materialIssueBatchMapper = new MaterialIssueBatchMapper(
													new Batch(batchId), batch.getIssueQuantity(),
													new UomType(batch.getUomTypeId()), new UomUnit(batch.getUomUnitId()),
													new Status(com.param.common.constants.Status.Pending.getStatus()));
											materialIssueBatchMapper.setCreateDefaults(request.getOrganizationId(),
													request.getUnitId(), request.getUserId());
											mterialDtl.addBatch(materialIssueBatchMapper);
											issuingQty = issuingQty - storeAvaliableQuantity;
										}
									}
									else {

									}
								}
							}
						} else {

							for (BatchResponse batch : item.getBatches()) {
								if (batch.getId() == null) {
									List<StoreStock> StoreStockList = GlobalDao.getStoreStockListByBatchIdAndStoreId(
											batch.getBatchId(), storeId, request.getUnitId());
									int issuingQty = batch.getIssueQuantity();
									for (StoreStock storeStockObj : StoreStockList) {
										if (issuingQty != 0 && batch != null && storeStockObj.getBatch().getId().equals(batch.getBatchId())) {

											UomType uomType = new UomType(batch.getUomTypeId());
											UomUnit uomUnit = new UomUnit(batch.getUomUnitId());
											Long issueQuantity = Util.ConvertQuantity(
													Long.valueOf(storeStockObj.getAvailableQty()),
													storeStockObj.getBatch().getBatchUomList(), uomType);
											Long batchId = storeStockObj.getBatch().getId();
											if (issueQuantity >= issuingQty) {
												Batch issueBatch = BatchStoreTransactionForClubedBatch(storeId, batch.getBatchId(),
														issuingQty, uomType, materialIssue.getUnit().getUnitId(), request,storeIndentDetail.getItem(), materialIssue.getIssueNo(),mterialDtl);
												issueBatch.setUpdateDefaults(request.getUserId(), materialIssue.getUnitId());

												MaterialIssueBatchMapper batchMapper = new MaterialIssueBatchMapper(
														issueBatch, issuingQty, uomType, uomUnit);
												batchMapper.setStatus(
														new Status(com.param.common.constants.Status.Pending.getStatus()));

												batchMapper.setCreateDefaults(request.getOrganizationId(),
														request.getUnitId(), request.getUserId());
												mterialDtl.addBatch(batchMapper);

												issuingQty = 0;
												break;
											} else {
												int storeAvaliableQuantity = issueQuantity.intValue();

												Batch issueBatch = BatchStoreTransactionForClubedBatch(storeId, batchId,
														batch.getIssueQuantity(), uomType,
														materialIssue.getUnit().getUnitId(), request,
														storeIndentDetail.getItem(), materialIssue.getIssueNo(),
														mterialDtl);
												issueBatch.setUpdateDefaults(request.getUserId(),
														materialIssue.getUnitId());

												MaterialIssueBatchMapper batchMapper = new MaterialIssueBatchMapper(
														issueBatch, batch.getIssueQuantity(), uomType, uomUnit);

												batchMapper.setStatus(new Status(
														com.param.common.constants.Status.Pending.getStatus()));
												batchMapper.setCreateDefaults(request.getOrganizationId(),
														request.getUnitId(), request.getUserId());
												mterialDtl.addBatch(batchMapper);

												issuingQty = issuingQty - storeAvaliableQuantity;
											}
										}
									}
									
								}
							} 

						} 

						
					}
					
					
				}
				
			}
			//break;
		}
		/*
		 * if(saveIssue) {
		 * MaterialIssuePendingDao.saveMaterialIssuePending(materialIssuenew); }
		 */

		if(materialIssue.getStoreIndent() != null && materialIssue.getStoreIndent().getId()!= null ) {
			if(materialIssue.getStoreIndent().getApprovalStatus().getId()==6)
			{
				MaterialIssuePendingDao.updateStoreIndentStatus(materialIssue.getStoreIndent().getId(),18);
			}
			else
			{
				MaterialIssuePendingDao.updateStoreIndentStatus(materialIssue.getStoreIndent().getId(),materialIssue.getStatus().getId());
			}
		}
	
		MaterialIssuePendingDao.updateMaterialIssue(materialIssue);

		CommonResponse response = new CommonResponse();
		response.setId(materialIssue.getId().longValue());
		response.setMessage("Material Issue saved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object getMaterialIssue(Integer id, Integer orgId, Integer unitId) {

		boolean isMaterialPickerRequired = ApplicationConfigurationDao.getApplicationConfigurationByUnitId(unitId)
				.getIssueMaterialPickerRequired();

		MaterialPickerResponse response = null;
		if (isMaterialPickerRequired) {
			response = MaterialIssuePendingDao.getMaterialIssuePending(id);
		}
		if (response == null && id != 0) // initial isMaterialPickerRequired false and after transaction if
											// isMaterialPickerRequired is true (first condition -> not found)
		{
			response = MaterialIssuePendingDao.getMaterialIssuePendingWithoutPicker(id);
		}
		if (response == null)// initial isMaterialPickerRequired true and after transaction if
								// isMaterialPickerRequired is false (first and second condition -> not found)
		{
			response = MaterialIssuePendingDao.getMaterialIssuePending(id);
		}
        List<UomResponse> stockAvailableAll=MaterialIssuePendingDao.getStoreStockByItemPharmacyAll(response != null && response.getToStoreId() != null ? response.getToStoreId() : 0,unitId);
        List<UomResponse> uomListAll=GlobalDao.getAllUomListAll(orgId);
        List<MaterialPickerDetailResponse> issueDetailsAll = MaterialIssuePendingDao.getMaterialIssuePendingDetail(id);
		List<Integer> itemIds = new ArrayList<>();
		for (MaterialPickerDetailResponse dto :issueDetailsAll){
			itemIds.add(dto.getItemId());
		}

		List<Batch> batchListAll = new ArrayList<>();
		if(itemIds != null && !itemIds.isEmpty()) {
			batchListAll = GlobalDao.getBatchDetailsAll(unitId, response != null && response.getToStoreId() != null ? response.getToStoreId() : 0, itemIds);
		}
		List<Integer> issueIds = issueDetailsAll.stream().map(MaterialPickerDetailResponse::getId).collect(Collectors.toList());
		List<MaterialIssueBatchMapper> BatchMapperAll = new ArrayList<>();
		List<MaterialIssueDetail> materialIssueDetailAll = new ArrayList<>();
		if(issueIds != null && !issueIds.isEmpty()) {
			BatchMapperAll = MaterialIssuePendingDao.getMaterialIssueBatchListAll(issueIds);
			materialIssueDetailAll = MaterialIssuePendingDao.getPickerQtyAll(issueIds);
		}

        List<Integer> materialIssueIds = issueDetailsAll.stream().map(MaterialPickerDetailResponse::getMaterialIssueId).collect(Collectors.toList());
		List<SubstituteMaterialIssueDetailDto> issueDetailSubAll = new ArrayList<>();
		if(materialIssueIds != null && !materialIssueIds.isEmpty()) {
			issueDetailSubAll = MaterialIssuePendingDao.getSubstituteIssueDetailsForMaterialIssueDetailAll(materialIssueIds);
		}
		 List<Integer> subDetailsIds = issueDetailSubAll.stream().map(SubstituteMaterialIssueDetailDto::getId).collect(Collectors.toList());
        List<MaterialIssueBatchMapper> bthMapprAll = new ArrayList<MaterialIssueBatchMapper>();
        if(subDetailsIds != null && subDetailsIds.size() > 0)
        {
        	bthMapprAll= MaterialIssuePendingDao.getMaaterialBatchMapperForSubstituteAll(subDetailsIds);
        }
        
		for (MaterialPickerDetailResponse issueDetails : issueDetailsAll) {
			
			List<SubstituteMaterialIssueDetailDto> subItemsList=new ArrayList<SubstituteMaterialIssueDetailDto>();
			
			Stream<SubstituteMaterialIssueDetailDto> issueDetailSubAllStream = issueDetailSubAll.stream();
            List<SubstituteMaterialIssueDetailDto> issueDetailSub=issueDetailSubAllStream
            .filter(str -> str.getMaterialIssueId()!=null && str.getMaterialIssueId().equals(issueDetails.getMaterialIssueId()))
            .collect(Collectors.toList());
		if(issueDetailSub != null && issueDetailSub.size() > 0) {	
			for (SubstituteMaterialIssueDetailDto substituteIssueDetail : issueDetailSub) {
				if(substituteIssueDetail.getParentMaterialIssueId().equals(issueDetails.getId())) {
				List<BatchResponse> bth=new ArrayList<BatchResponse>();
				
				 Stream<MaterialIssueBatchMapper> bthMapprAllStream = bthMapprAll.stream();
		            List<MaterialIssueBatchMapper> bthMappr=bthMapprAllStream
		            .filter(str -> str.getId()!=null && str.getId().equals(substituteIssueDetail.getId()))
		            .collect(Collectors.toList());
				
	            Stream<UomResponse> stockAvailableAllStream = stockAvailableAll.stream();
	            List<UomResponse> storeStock=stockAvailableAllStream
	            .filter(str -> str.getItemId()!=null && str.getItemId().equals(substituteIssueDetail.getItemId()))
	            .collect(Collectors.toList());
	            Long stockAvailable = (long) 0;
	            if(storeStock != null && storeStock.size() > 0){
	            	stockAvailable = storeStock.get(0).getStockAvailable();
	            }
				
				if (stockAvailable < 0) {
					stockAvailable = 0l;
				}
				for (MaterialIssueBatchMapper batchMapper : bthMappr) {
					// itemList.add(fillPatientConsumzableIssueItem(item,batchMapper,true)); // true
					// for getPatientIndentDetail
							BatchResponse b=new BatchResponse();
							b.setIsSelected(true);
							b.setIssueQuantity(batchMapper.getIssueQuantity());
							b.setBatchNo(batchMapper.getBatch().getBatchNo());
							b.setExpiry(batchMapper.getBatch().getExpiryDate());
							b.setSellingPrice(batchMapper.getBatch().getSellingPrice());
							b.setAvailableStockQuantity(batchMapper.getBatch().getCurrentQuantity());
							b.setUomTypeId(batchMapper.getUomType().getId());
							b.setUomUnitId(batchMapper.getUomType().getId());
							b.setUomType(batchMapper.getUomType().getType());
							b.setUomUnit(batchMapper.getUomUnit().getUnits());
							bth.add(b);

				}
				substituteIssueDetail.setItemName(substituteIssueDetail.getItemName());
				substituteIssueDetail.setItemCode(substituteIssueDetail.getItemCode());
				substituteIssueDetail.setGenericName(substituteIssueDetail.getGenericName());
				substituteIssueDetail.setAvailableStockQuantity(stockAvailable.intValue());
				substituteIssueDetail.setNewBatch(bth);
				substituteIssueDetail.setNewBatch(bth);
				subItemsList.add(substituteIssueDetail);
			}
			}
		}
			Integer itemId = issueDetails.getItemId();
			
			
			Stream<MaterialIssueDetail> materialIssueDetailStream = materialIssueDetailAll.stream();
            List<MaterialIssueDetail> materialIssueDetailList=materialIssueDetailStream
            .filter(str -> str.getId()!=null && str.getId().equals(issueDetails.getId()))
            .collect(Collectors.toList());
            MaterialIssueDetail materialIssueDetail=new MaterialIssueDetail();
			if(materialIssueDetailList!=null && materialIssueDetailList.size()>0)
			{
				 materialIssueDetail = materialIssueDetailList.get(0);
			}
			Integer pickerQty = 0;
			if (materialIssueDetail != null) {

				if (materialIssueDetail.getMaterialPickerDetail() != null) {
					pickerQty = materialIssueDetail.getMaterialPickerDetail().getPickerQuantity();
				}

			}

			issueDetails.setPickerQuantity(pickerQty);
			Stream<UomResponse> uomListStream = uomListAll.stream();
            List<UomResponse> uomList=uomListStream
            .filter(str -> str.getItemId()!=null && str.getItemId().equals(issueDetails.getItemId()))
            .collect(Collectors.toList());
			if (isMaterialPickerRequired) {
				issueDetails.setStockAvailable(
						getAvailableStock(itemId, response != null && response.getToStoreId() != null ? response.getToStoreId() : 0, issueDetails.getUomTypeId(), unitId, orgId));
				issueDetails.setPendingQuantity(getPendingQuantityMaterialIssue(uomList, issueDetails.getPendingQuantity(),
						issueDetails.getUomTypeId(), orgId));

			} else {
				// if isMaterialPickerRequired is false
				issueDetails.setStockAvailable(
						getAvailableStock(itemId, response != null && response.getToStoreId() != null ? response.getToStoreId() : 0, issueDetails.getUomTypeId(), unitId, orgId));
				issueDetails.setPendingQuantity(getPendingQuantityMaterialIssue(uomList, issueDetails.getPendingQuantity(),
						issueDetails.getUomTypeId(), orgId));
				issueDetails.setIssueQuantity(materialIssueDetail.getIssueQuantity()!=null ?materialIssueDetail.getIssueQuantity().longValue() : 0L );
			}

			BatchProperty batchProperty = new BatchProperty();
//			batchProperty.setIsIndent(false);
			Stream<Batch> batchListStream = batchListAll.stream();
            List<Batch> batchList=batchListStream
            .filter(str -> str.getItem().getId()!=null && str.getItem().getId().equals(issueDetails.getItemId()))
            .collect(Collectors.toList());
			issueDetails.setUom(uomList);
			if (batchList != null) {
				for (Batch batch : batchList) {
					issueDetails.addBatches(Util.PopulateBatchWithStock1(batch, response != null && response.getToStoreId() != null ? response.getToStoreId() : 0));
				}
			}

			 Stream<MaterialIssueBatchMapper> BatchMapperAllStream = BatchMapperAll.stream();
            List<MaterialIssueBatchMapper> batchMapperList=BatchMapperAllStream
            .filter(str -> str.getId()!=null && str.getId().equals(issueDetails.getId()))
            .collect(Collectors.toList());
            
			for (MaterialIssueBatchMapper batchMapper : batchMapperList) {

				IssueBatch bth = new IssueBatch(batchMapper.getId(), batchMapper.getBatch().getId(),
						batchMapper.getUomType().getId(), batchMapper.getUomUnit().getId(),
						batchMapper.getIssueQuantity());
				bth.setBatchNo(batchMapper.getBatch().getBatchNo());
				bth.setExpiry(batchMapper.getBatch().getExpiryDate());
				bth.setUom(Util.PopulateUom(batchMapper.getBatch()));
				bth.setAvailableStockQuantity(batchMapper.getBatch().getStoreStockList().get(0).getAvailableQty());
				bth.setPickerQty(pickerQty);
				issueDetails.addBatch(bth);
			}
			issueDetails.setSelectedItems(subItemsList);
			response.addItems(issueDetails);
		}
		if(response != null)
			response.setCarriers(PopulateCarriers(unitId));

		return response;
	}

	private Integer getPendingQuantityMaterialIssue(List<UomResponse> uomList, Integer pendingQuantity,
			Integer uomTypeId, Integer orgId) {
		Integer dispenseConversion=1;
		Integer stockConversion=1;
		Integer purchaseConversion=1;
		if(uomTypeId==3)
		{
			Stream<UomResponse> uomStream = uomList.stream();
	        List<UomResponse> uomListNew=uomStream
	        .filter(str -> str.getUomTypeId()!=null && str.getUomTypeId().equals(uomTypeId))
	        .collect(Collectors.toList());
	        dispenseConversion = uomListNew.get(0).getConversion();
		}
		if(uomTypeId==2)
		{
			Stream<UomResponse> uomStream = uomList.stream();
	        List<UomResponse> uomListNew=uomStream
	        .filter(str -> str.getUomTypeId()!=null && str.getUomTypeId().equals(uomTypeId))
	        .collect(Collectors.toList());
	        stockConversion = uomListNew.get(0).getConversion();
		}
		if(uomTypeId==1)
		{
			Stream<UomResponse> uomStream = uomList.stream();
	        List<UomResponse> uomListNew=uomStream
	        .filter(str -> str.getUomTypeId()!=null && str.getUomTypeId().equals(uomTypeId))
	        .collect(Collectors.toList());
	        purchaseConversion = uomListNew.get(0).getConversion();
		}
				Integer originalQty = pendingQuantity;
				Integer dispenseConversionQty = originalQty / dispenseConversion;
				Integer stockConversionQty = dispenseConversionQty / stockConversion;
				Integer purchaseConversionQty = stockConversionQty / purchaseConversion;
				if (uomTypeId == 1)
					if (purchaseConversionQty.equals(0)) {
						return pendingQuantity;
					} else {
						return purchaseConversionQty;
					}
				if (uomTypeId == 2)

					if (stockConversionQty.equals(0)) {
						return pendingQuantity;
					} else {
						return stockConversionQty;
					}

				if (uomTypeId == 3)
					return dispenseConversionQty;
				return dispenseConversionQty;
	}

	private List<PickerResponse> PopulateCarriers(Integer unitId) {
		List<PickerResponse> pickerResponseList = new ArrayList<PickerResponse>();

		for (User user : GlobalDao.getPickerUsers(unitId)) {
			PickerResponse pickerResponse = new PickerResponse(user.getUserId(), user.getUserName());
			pickerResponseList.add(pickerResponse);
		}
		return pickerResponseList;
	}

	private Batch BatchStoreTransaction(Integer storeId, Long batchId, Integer quantity, UomType uomType,
			Integer totQty, Integer unitId, MaterialPickerRequest request) {

//		Integer inTransitQuantity =0;

		// get batch needs to be issued
		Batch batch = GlobalDao.getBatch(storeId, batchId, unitId);

		// Get existing batch store stock
		Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batchId);

		// Get lease quantity
		Integer leaseQuantity = Util.GetLeaseQuantity(quantity, batch.getBatchUomList(), uomType);

		Integer balanceQuantity = avaialableStoreStock - leaseQuantity;

		// Deduct issued quantity from the batch store stock
		batch.getStoreStockList().get(0).setAvailableQty(balanceQuantity);
		batch.getStoreStockList().get(0).setTotalQty(batch.getStoreStockList().get(0).getTotalQty() - totQty);
		// batch.getStoreStockList().get(0).setTotalQty(totQty);

		Integer inTransitQuantity = GlobalDao.getTransitQuantityByBatch(storeId, batch.getId());

		Integer tQty = inTransitQuantity + leaseQuantity;

		batch.getStoreStockList().get(0).settQty(tQty);

		// Add stock transaction
		StockTransaction stockTransaction = new StockTransaction(balanceQuantity, screen, screen.getCode(),
				leaseQuantity, batch.getStoreStockList().get(0).getStore(),0);
		// sessionService.setEntityDefaults(stockTransaction);
		stockTransaction.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
		batch.addStockTransaction(stockTransaction);

		return batch;
	}

	/*
	 * private Batch BatchStoreTransactionForClubBatch(Integer storeId, Long
	 * batchId, Integer quantity, UomType uomType, Integer totQty) {
	 * List<StoreStock> StoreStockList =
	 * GlobalDao.getStoreStockListByBatchIdAndStoreId(batchId, storeId); int
	 * issueQtyFromRequest = quantity; for (StoreStock storeStockObj :
	 * StoreStockList) { if (issueQtyFromRequest != 0) { Long storeAvaliableQuantity
	 * = Util.ConvertQuantity(Long.valueOf(storeStockObj.getAvailableQty()),
	 * storeStockObj.getBatch().getBatchUomList(), uomType); Long batchIdFromStore =
	 * storeStockObj.getBatch().getId(); if (issueQtyFromRequest <=
	 * storeAvaliableQuantity) { totQty =
	 * getIssueQuantity(storeStockObj.getBatch().getItem().getId(),
	 * issueQtyFromRequest, uomType.getId()); BatchStoreTransaction(storeId,
	 * batchIdFromStore, issueQtyFromRequest, uomType, totQty); issueQtyFromRequest
	 * = 0; } else { int storeAvaliableQuantityINT =
	 * storeAvaliableQuantity.intValue(); totQty =
	 * getIssueQuantity(storeStockObj.getBatch().getItem().getId(),
	 * storeAvaliableQuantityINT, uomType.getId()); BatchStoreTransaction(storeId,
	 * batchIdFromStore, storeAvaliableQuantityINT, uomType, totQty);
	 * issueQtyFromRequest = issueQtyFromRequest - storeAvaliableQuantityINT; } } }
	 * return GlobalDao.getBatch(storeId, batchId); }
	 */

	private Batch BatchStoreTransactionForClubedBatch(Integer storeId, Long batchId, Integer quantity, UomType uomType,
			Integer unitId, MaterialPickerRequest request, Item item, String prefixNo, MaterialIssueDetail d) {

		// get batch needs to be issued
		Batch batch = GlobalDao.getBatch(storeId, batchId, unitId);

		// Get existing batch store stock
		Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batchId);

		// Get lease quantity
		Integer leaseQuantity = Util.GetLeaseQuantity(quantity, batch.getBatchUomList(), uomType);

		Integer balanceQuantity = avaialableStoreStock - leaseQuantity;
		Integer inTransitQuantity = 0;
		// Deduct issued quantity from the batch store stock
		// //System.out.println(batch.getStoreStockList().toString());
		// if (batch.getStoreStockList().toString().equals("null")) {

		// } else {

		if (batch.getStoreStockList() == null) {

		} else {
			batch.getStoreStockList().get(0).setAvailableQty(balanceQuantity);

			// start code by minesh for resolved bug no HIS-8018, HIS-8415
			// batch.getStoreStockList().get(0).setTotalQty(batch.getStoreStockList().get(0).getTotalQty()-leaseQuantity);
			// end code by minesh for resolved bug no HIS-8018, HIS-8415

			inTransitQuantity = GlobalDao.getTransitQuantityByBatch(storeId, batchId);
			Integer tQty = inTransitQuantity + leaseQuantity;
			batch.getStoreStockList().get(0).settQty(tQty);
		}

		// }

		// if (batch.getId() != null) {
		// inTransitQuantity = GlobalDao.getTransitQuantityByBatch(storeId, batchId);
		// }

		// Add stock transaction
		Store s = GlobalDao.getStore(storeId);
		StockTransaction stockTransaction = new StockTransaction(balanceQuantity, screen, screen.getCode(),
				leaseQuantity, s, d.getId());
		stockTransaction.setIsTransit(true);
		stockTransaction.setItem(item);
		stockTransaction.setTransitNo(prefixNo);
		stockTransaction.setTransactionId(d.getMaterialIssue().getId());
		stockTransaction.setOutQty(leaseQuantity);
		// sessionService.setEntityDefaults(stockTransaction);
		stockTransaction.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
		batch.addStockTransaction(stockTransaction);

		return batch;
	}

	Long getAvailableStock(Integer itemId, Integer storeId, Integer uomTypeId, Integer unitId, Integer orgId) {
		// System.out.println("Item Id : " + itemId);
		// System.out.println("Store Id : " + storeId);
		// System.out.println("UOM Type Id : " + uomTypeId);

		Integer dispenseConversion = GlobalDao.getDispenseConversionByItem(itemId, orgId);
		// System.out.println("Dispense Conversion : " + dispenseConversion);

		Integer stockConversion = GlobalDao.getStockConversionByItem(itemId, orgId);
		// System.out.println("Stock Conversion : " + stockConversion);

		Integer purchaseConversion = GlobalDao.getPurchaseConversionByItem(itemId, orgId);
		// System.out.println("Purchase Conversion : " + purchaseConversion);

		Long originalStockAvailable = GlobalDao.getStoreStockByItem(storeId, itemId, unitId);
		// System.out.println("Original Qty : " + originalStockAvailable);

		Long dispenseConversionQty = originalStockAvailable / dispenseConversion;
		// System.out.println("Dispense Conversion Qty : " + dispenseConversionQty);

		Long stockConversionQty = dispenseConversionQty / stockConversion;
		// System.out.println("Stock Conversion Qty : " + stockConversionQty);

		Long purchaseConversionQty = stockConversionQty / purchaseConversion;
		// System.out.println("Purchase Conversion Qty : " + purchaseConversionQty);

		if (uomTypeId == 1)
			return purchaseConversionQty;
		if (uomTypeId == 2)
			return stockConversionQty;
		if (uomTypeId == 3)
			return dispenseConversionQty;
		return dispenseConversionQty;

	}

	Integer getIssueQuantity(Integer itemId, Integer qty, Integer uomTypeId, Integer orgId) {
		// System.out.println("Item Id : " + itemId);
		// System.out.println("Qty Id : " + qty);
		// System.out.println("UOM Type Id : " + uomTypeId);

		Integer dispenseConversion = GlobalDao.getDispenseConversionByItem(itemId, orgId);
		// System.out.println("Dispense Conversion : " + dispenseConversion);

		Integer stockConversion = GlobalDao.getStockConversionByItem(itemId, orgId);
		// System.out.println("Stock Conversion : " + stockConversion);

		Integer purchaseConversion = GlobalDao.getPurchaseConversionByItem(itemId, orgId);
		// System.out.println("Purchase Conversion : " + purchaseConversion);

		Integer originalQty = qty;
		// System.out.println("Original Qty : " + originalQty);

		Integer dispenseConversionQty = originalQty * dispenseConversion;
		// System.out.println("Dispense Conversion Qty : " + dispenseConversionQty);

		Integer stockConversionQty = dispenseConversionQty * stockConversion;
		// System.out.println("Stock Conversion Qty : " + stockConversionQty);

		Integer purchaseConversionQty = stockConversionQty * purchaseConversion;
		// System.out.println("Purchase Conversion Qty : " + purchaseConversionQty);

		if (uomTypeId == 1)
			return purchaseConversionQty;
		if (uomTypeId == 2)
			return stockConversionQty;
		if (uomTypeId == 3)
			return dispenseConversionQty;
		return dispenseConversionQty;

	}

	Integer getPendingQuantity(Integer itemId, Integer qty, Integer uomTypeId, Integer orgId) {
		// System.out.println("Item Id : " + itemId);
		// System.out.println("Qty Id : " + qty);
		// System.out.println("UOM Type Id : " + uomTypeId);

		Integer dispenseConversion = GlobalDao.getDispenseConversionByItem(itemId, orgId);
		// System.out.println("Dispense Conversion : " + dispenseConversion);

		Integer stockConversion = GlobalDao.getStockConversionByItem(itemId, orgId);
		// System.out.println("Stock Conversion : " + stockConversion);

		Integer purchaseConversion = GlobalDao.getPurchaseConversionByItem(itemId, orgId);
		// System.out.println("Purchase Conversion : " + purchaseConversion);

		Integer originalQty = qty;
		// System.out.println("Original Qty : " + originalQty);

		Integer dispenseConversionQty = originalQty / dispenseConversion;
		// System.out.println("Dispense Conversion Qty : " + dispenseConversionQty);

		Integer stockConversionQty = dispenseConversionQty / stockConversion;
		// System.out.println("Stock Conversion Qty : " + stockConversionQty);

		Integer purchaseConversionQty = stockConversionQty / purchaseConversion;
		// System.out.println("Purchase Conversion Qty : " + purchaseConversionQty);

		if (uomTypeId == 1)
			if (purchaseConversionQty.equals(0)) {
				return qty;
			} else {
				return purchaseConversionQty;
			}
		if (uomTypeId == 2)

			if (stockConversionQty.equals(0)) {
				return qty;
			} else {
				return stockConversionQty;
			}

		if (uomTypeId == 3)
			return dispenseConversionQty;
		return dispenseConversionQty;

	}

	@Override
	@Transactional
	public Response materialIssueStatus(Integer id) {
        Integer issueSaveStatus = MaterialIssuePendingDao.saveMaterialIssue(id);
        return new Response<>(null, null, null, null, issueSaveStatus); 
	}
}
