package com.param.inventory.store.service.materialInward;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Constants;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.InwardTaxComponent;
import com.param.entity.model.inventory.InwardsUomMapper;
import com.param.entity.model.inventory.MaterialInward;
import com.param.entity.model.inventory.MaterialInwardAccessories;
import com.param.entity.model.inventory.MaterialInwardDetail;
import com.param.entity.model.inventory.ReturnableGatepass;
import com.param.entity.model.inventory.ReturnableGatepassDetail;
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.RequestType;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.TaxComponent;
import com.param.entity.model.master.TaxDetail;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.Vendor;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.global.TaxDropDown;
import com.param.global.dto.items.AsseccoriesResponse;
import com.param.global.dto.items.UomResponse;
import com.param.global.service.global.IGlobalService;
import com.param.inventory.stock.dto.openingBalance.ItemRequest;
import com.param.inventory.stock.dto.openingBalance.ItemRequestNew;
import com.param.inventory.store.dao.materialInward.IMaterialInwardDao;
import com.param.inventory.store.dto.MaterialInwardRequestNew;
import com.param.inventory.store.dto.materialInward.CreditNoteVendorWise;
import com.param.inventory.store.dto.materialInward.MaterialInwardAccessoriesRequest;
import com.param.inventory.store.dto.materialInward.MaterialInwardDropdownResponse;
import com.param.inventory.store.dto.materialInward.MaterialInwardRequest;
import com.param.inventory.store.dto.materialInward.MaterialInwardResponse;
import com.param.inventory.store.dto.materialInward.MaterialItemResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("MaterialInwardService")
public class MaterialInwardService implements IMaterialInwardService {

	private Integer screenId = Screen.MaterialInward.getScreen();

	@Autowired
	private SessionService sessionService;

	@Autowired
	IMaterialInwardDao MaterialInwardDao;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;

	@Override
	@Transactional
	public MaterialInwardDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId) {

		MaterialInwardDropdownResponse response = new MaterialInwardDropdownResponse();

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}

		for (AssetType assetType : GlobalDao.getAssetTypeList(0,0)) {
			response.addAssetType(new com.param.global.dto.global.AssetType(assetType.getId(), assetType.getType()));
		}
		return response;
	}

	@Override
	@Transactional
	public Object getMaterialInwardList(MaterialInwardRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return MaterialInwardDao.searchMaterialInwardList(request, screenId);
	}

	@Override
	@Transactional
	public Object saveMaterialInward(MaterialInwardRequestNew request) {

		CommonResponse response = new CommonResponse();
		// for (MaterialInwardRequest request : req) {
		/*
		 * Integer storeId = request.getStoreId();
		 * 
		 * Store storeNew = GlobalDao.getStore(storeId);
		 * 
		 * // System.out.println("Request storeId:-" + storeNew);
		 * 
		 * Unit unit = GlobalDao.getUnit(sessionService.getUnitId());
		 * 
		 * // System.out.println("session UnitId :-" + unit + "screenId:-" + screenId);
		 */
		String inwardNumber = GlobalService.getBillNoByGeneratedCode(request.getStoreId(), request.getUnitId(),
				screenId);

		/*
		 * MaterialInward materialInward = new MaterialInward(new
		 * Store(request.getStoreId()), new Vendor(request.getVendorId()), new
		 * AssetType(request.getAssetTypeId()), new
		 * RequestType(request.getRequestTypeId()), new
		 * ReturnableGatepass(request.getReturnableGetPassId()),
		 * request.getTotalAmount(), request.getTotalTaxAmount(),
		 * request.getTotalNetAmount(), inwardNumber, request.getRemark(), new
		 * Status(request.getStatusId()), new Status(request.getApprovalStatusId()),
		 * request.getDocumentNo(), request.getDocumentDate(),
		 * request.getReplacementId());
		 */
		String replaceType = null;
		if (request.getRequestTypeId() == Constants.replacementId) {
			replaceType = GlobalService.getBillNoByGeneratedCode(request.getStoreId(), request.getUnitId(),
					Screen.MaterialInwardReplacement.getScreen());
			if(inwardNumber==null)
			{
				inwardNumber=replaceType;
			}
		} else {
			replaceType = GlobalService.getBillNoByGeneratedCode(request.getStoreId(), request.getUnitId(),
					Screen.MaterialInwardNote.getScreen());
			if(inwardNumber==null)
			{
				inwardNumber=replaceType;
			}
		}

		MaterialInward materialInward = new MaterialInward(new Store(request.getStoreId()),
				new Vendor(request.getVendorId()), new AssetType(request.getAssetTypeId()),
				new RequestType(request.getRequestTypeId()), inwardNumber, request.getRemark(),
				new Status(request.getStatusId()), new Status(request.getApprovalStatusId()), request.getDocumentNo(),
				request.getDocumentDate(), request.getReplacementId(), replaceType);
		materialInward.setTotalAmount(request.getTotalAmount());
		materialInward.setTotalTaxAmount(request.getTotalTaxAmount());
		materialInward.setNetAmount(request.getNetAmount());
		materialInward.setReturnAmount(request.getReturnAmt());

		if (request.getNetAmount() != null && request.getReturnAmt() != null) {
			if (request.getNetAmount() < request.getReturnAmt()) {
				materialInward.setCreditNote(request.getReturnAmt() - request.getNetAmount());
				materialInward.setCreditNetAmount(request.getReturnAmt() - request.getNetAmount());
				materialInward.setCreditTaxAmount(0.00);
				materialInward.setVendorPayable(0.00);
			} else if (request.getNetAmount() > request.getReturnAmt()) {
				materialInward.setVendorPayable(request.getNetAmount() - request.getReturnAmt());
				materialInward.setCreditNote(0.00);
				materialInward.setCreditTaxAmount(0.00);
				materialInward.setCreditNetAmount(0.00);
			} else if (request.getNetAmount() == request.getReturnAmt()) {
				materialInward.setCreditNote(0.00);
				materialInward.setCreditTaxAmount(0.00);
				materialInward.setCreditNetAmount(0.00);
				materialInward.setVendorPayable(0.00);
			}
		}

		MaterialInwardDetail materialInwardDetail = null;
		MaterialInwardDetail materialInwardDetail2 =null;
		// sessionService.setEntityDefaults(materialInwardDetail);
		Double creNoteAmt = 0.00;
		Double creNoteTaxAmt = 0.00;
		Double creNetAmt = 0.00;

		for (ItemRequestNew item : request.getItem()) {

			if (request.getRequestTypeId() == Constants.replacementId) {

				if (request.getAssetTypeId() == Constants.consumableId) {
					// request.
					ReturnableGatepass returnableGatepass = new ReturnableGatepass(item.getReturnableGetPassId());
					 materialInwardDetail2 = new MaterialInwardDetail(
							/*returnableGatepass*/
							new ReturnableGatepassDetail(item.getReturnableGatepassDetailId()),/* change code for resolved HIS-5496 Material Inward - Replacement - Not working */
							new Item(item.getItemId()), item.getQuantity(), new UomType(item.getUomTypeId()),
							new UomUnit(item.getUomUnitId()), item.getBatchNo(), item.getExpiryDate(), item.getCop(),
							item.getMrp(), new Tax(item.getTaxId()), item.getTaxPercent(), item.getTaxAmount(),
							item.getTotalAmount(), item.getNetAmount(), item.getSpecification(), item.getMarkup(),
							new UomType(item.getMrpUomTypeId()), new Status(request.getStatusId()),
							item.getNetAmount() > 0 ? item.getNetAmount() - returnableGatepass.getNetAmount() : 0.00,
							item.getNetAmount() > 0 ? item.getNetAmount() - returnableGatepass.getNetAmount() : 0.00);
					materialInwardDetail2.setReturnQuantity(item.getReturnQuantity());
					materialInwardDetail2.setReturnAmount(item.getReturnAmount());
					materialInwardDetail2.setIsReject(false);
				//	sessionService.setEntityDefaults(materialInwardDetail2);
					materialInwardDetail2.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
				   materialInward.addMaterialInwardDetailList(materialInwardDetail2);
					materialInward.setReturnableGatepass(new ReturnableGatepass(item.getReturnableGetPassId()));

					if (item.getUom() != null) {
						for (UomResponse uom : item.getUom()) {
							InwardsUomMapper inwardsUomMapper = new InwardsUomMapper(new UomType(uom.getUomTypeId()),
									new UomUnit(uom.getUomUnitId()), uom.getConversion(), uom.getIpUom(),
									uom.getOpUom(), uom.getStoreUom());
						//	sessionService.setEntityDefaults(inwardsUomMapper);
							inwardsUomMapper.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
							materialInwardDetail2.addInwardsUomMapper(inwardsUomMapper);
						}
					}
				} else if (request.getAssetTypeId() == Constants.fixedAssetId) {
					ReturnableGatepass returnableGatepass = new ReturnableGatepass(item.getReturnableGetPassId());
					 materialInwardDetail2 = new MaterialInwardDetail(
							//new ReturnableGatepass(item.getReturnableGetPassId()), 
							new ReturnableGatepassDetail(item.getReturnableGatepassDetailId()), /* change code for resolved HIS-5496 Material Inward - Replacement - Not working */
							new Item(item.getItemId()),
							item.getQuantity(), item.getBatchNo(), item.getExpiryDate(), item.getCop(), item.getMrp(),
							new Tax(item.getTaxId()), item.getTaxPercent(), item.getTaxAmount(), item.getTotalAmount(),
							item.getNetAmount(), item.getSpecification(), item.getMarkup(),
							new Status(request.getStatusId()));
					materialInwardDetail2.setNetAmount(item.getNetAmount());
					materialInwardDetail2.setCreditNetAmount(
							item.getNetAmount() > 0 ? item.getNetAmount() - returnableGatepass.getNetAmount() : 0.00);
					materialInwardDetail2.setAvailableAmount(
							item.getNetAmount() > 0 ? item.getNetAmount() - returnableGatepass.getNetAmount() : 0.00);
					materialInwardDetail2.setIsReject(false);
				//	sessionService.setEntityDefaults(materialInwardDetail2);
					materialInwardDetail2.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
					materialInward.addMaterialInwardDetailList(materialInwardDetail2);
					materialInward.setReturnableGatepass(new ReturnableGatepass(item.getReturnableGetPassId()));

					if (item.getMaterialInwardAccessoriesRequest() != null) {
						for (MaterialInwardAccessoriesRequest access : item.getMaterialInwardAccessoriesRequest()) {
							MaterialInwardAccessories materialInwardAccessories = new MaterialInwardAccessories(
									new Item(item.getItemId()), access.getReturnQuantity(),
									access.getApprovedQuantity(), access.getSpecification());
						//	sessionService.setEntityDefaults(materialInwardAccessories);
							materialInwardAccessories.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
							materialInwardDetail2.addMaterialInwardAccessories(materialInwardAccessories);
						}
					}

				}

				for (TaxDetail taxDetail : GlobalDao.getTaxDetail(item.getTaxId())) {

					Integer taxCompId = taxDetail.getTaxComponent().getId();
					Double taxPercentage = taxDetail.getTaxPercentage();
					Double amt = item.getTotalAmount();

					Double calTaxAmt = (amt * taxPercentage) / 100;
					InwardTaxComponent inwardTaxComponent = new InwardTaxComponent(calTaxAmt,
							new MaterialInwardDetail(materialInward.getMaterialInwardsDetailList().getFirst().getId()),
							taxPercentage, new TaxComponent(taxCompId));
				//	sessionService.setEntityDefaults(inwardTaxComponent);
					inwardTaxComponent.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
					materialInwardDetail2.addInwardTaxCom(inwardTaxComponent);
				}

			//	sessionService.setEntityDefaults(materialInwardDetail2);
				materialInwardDetail2.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
			} else if (request.getRequestTypeId() == Constants.creditNoteId) {
				 materialInwardDetail2 = new MaterialInwardDetail(item.getCreditNoteAmount(),
						item.getCreditTaxAmount(), item.getCreditNetAmount(), new Status(request.getStatusId()),
						item.getTotalAmount(), item.getTotalTaxAmount(), item.getTotalNetAmount(),
						//new ReturnableGatepass(item.getReturnableGetPassId())
						new ReturnableGatepassDetail(item.getReturnableGatepassDetailId()) /* change code for resolved HIS-5496 Material Inward - Replacement - Not working */
						);
				 materialInwardDetail2.setIsReject(false);
			//	sessionService.setEntityDefaults(materialInwardDetail2);
				 materialInwardDetail2.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
				//materialInwardDetail = materialInward.addMaterialInwardDetailList(materialInwardDetail2);
				materialInward.setReturnableGatepass(new ReturnableGatepass(item.getReturnableGetPassId()));
				 materialInward.addMaterialInwardDetailList(materialInwardDetail2);
				
				creNoteAmt = creNoteAmt + (item.getCreditNoteAmount()!=null ? item.getCreditNoteAmount() :0);
				if (item.getCreditTaxAmount() != null) {
					creNoteTaxAmt = creNoteTaxAmt + (item.getCreditTaxAmount()!=null ? item.getCreditTaxAmount():0);
				}

				creNetAmt = creNetAmt + (item.getCreditNetAmount()!=null ? item.getCreditNetAmount() :0);

				// quantityToOrdered
				/*
				 * for (UomResponse uom : item.getUom()) { InwardsUomMapper inwardsUomMapper =
				 * new InwardsUomMapper(new UomType(uom.getUomTypeId()), new
				 * UomUnit(uom.getUomUnitId()), uom.getConversion(), uom.getIpUom(),
				 * uom.getOpUom(), uom.getStoreUom());
				 * sessionService.setEntityDefaults(inwardsUomMapper);
				 * materialInwardDetail.addInwardsUomMapper(inwardsUomMapper); }
				 */

				/*
				 * for (UomResponse uom : item.getUom()) {
				 * materialInwardDetail.addInwardsUomMapper( new InwardsUomMapper(new
				 * UomType(uom.getUomTypeId()), new UomUnit(uom.getUomUnitId()),
				 * uom.getConversion(), uom.getIpUom(), uom.getOpUom(), uom.getStoreUom()));
				 * 
				 * }
				 */
			}

		}

		if (request.getRequestTypeId() == Constants.creditNoteId) {
			materialInward.setCreditNote(creNoteAmt);
			materialInward.setCreditTaxAmount(creNoteTaxAmt);
			materialInward.setCreditNetAmount(creNetAmt);
			materialInward.setAvailableAmount(creNetAmt);

			// materialInward.setTotalAmount(item.getTotalAmount());
			// materialInward.setTotalTaxAmount(item.getTotalTaxAmount());
			// materialInward.setNetAmount(item.getTotalNetAmount());
		}

	//	sessionService.setEntityDefaults(materialInward);
		materialInward.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
		MaterialInwardDao.saveMaterialInward(materialInward);
		response.setId(materialInward.getId().longValue());
		response.setMessage("Material Inward saved successfully");

		// }
		return response;
	}

	@Override
	@Transactional
	public Object updateMaterialInward(MaterialInwardRequest request) {

		MaterialInward materialInward = MaterialInwardDao.getMaterialInward(request.getId());

		/*
		 * materialInward.updateMaterialInward(request.getDocumentNo(),
		 * request.getDocumentDate(), request.getTotalTaxAmount(),
		 * request.getTotalAmount(), request.getTotalNetAmount(), request.getRemark());
		 */
		if (request.getDocumentNo() != null) {
			materialInward.setDocumentNo(request.getDocumentNo());
		}
		if (request.getDocumentDate() != null) {
			materialInward.setDocumentDate(request.getDocumentDate());
		}
		if (request.getTotalTaxAmount() != null) {
			materialInward.setTotalTaxAmount(request.getTotalTaxAmount());
		}
		if (request.getTotalAmount() != null) {
			materialInward.setTotalAmount(request.getTotalAmount());
		}
		if (request.getTotalNetAmount() != null) {
			materialInward.setNetAmount(request.getTotalNetAmount());
		}
		if (request.getRemark() != null) {
			materialInward.setRemark(request.getRemark());
		}
		Double creNoteAmt = 0.00;
		Double creNoteTaxAmt = 0.00;
		Double creNetAmt = 0.00;
		for (MaterialInwardDetail detail : materialInward.getMaterialInwardsDetailList()) {
			Integer id = null;

			for (ItemRequest item : request.getItem()) {
				if (item.getId() != null && item.getId().equals(detail.getId())) {
					id = item.getId();
					if (materialInward.getRequestType().getId() == Constants.replacementId) {

						if (item.getBatchNo() != null) {
							detail.setBatchNo(item.getBatchNo());
						}
						if (item.getExpiryDate() != null) {
							detail.setExpiryDate(item.getExpiryDate());
						}
						if (item.getCop() != null) {
							detail.setCop(item.getCop());
						}
						if (item.getMrp() != null) {
							detail.setMrp(item.getMrp());
						}
						if (item.getMarkup() != null) {
							detail.setMarkup(item.getMarkup());
						}
						if (item.getNetAmount() != null) {
							detail.setNetAmount(item.getNetAmount());
						}
						if (item.getSpecification() != null) {
							detail.setSpecification(item.getSpecification());
						}
						if (item.getTaxAmount() != null) {
							detail.setTaxAmount(item.getTaxAmount());
						}
						if (item.getTotalAmount() != null) {
							detail.setTotalAmount(item.getTotalAmount());
						}
						if (item.getReceivedQuantity() != null) {
							detail.setReceivedQty(item.getReceivedQuantity());
						}

						if (request.getAssetTypeId() == Constants.consumableId) {

							/*
							 * detail.updateMaterialInwardDetail(item.getBatchNo(), item.getExpiryDate(),
							 * item.getCop(), item.getMrp(), item.getMarkup(), item.getNetAmount(),
							 * item.getSpecification(), item.getTaxAmount(), item.getTotalAmount(),
							 * item.getReceivedQuantity(), new UomType(item.getMrpUomTypeId()), new
							 * UomType(item.getUomTypeId()), new UomUnit(item.getUomUnitId()));
							 */

							if (item.getUomTypeId() != null) {
								detail.setUomType(new UomType(item.getUomTypeId()));
							}
							if (item.getUomUnitId() != null) {
								detail.setUomUnit(new UomUnit(item.getUomUnitId()));
							}
							if (item.getMrpUomTypeId() != null) {
								detail.setMrpUomType(new UomType(item.getMrpUomTypeId()));
							}
						} else if (request.getAssetTypeId() == Constants.fixedAssetId) {

							Integer accessoriesId = null;
							if(item.getMaterialInwardAccessoriesRequest()!=null)
							{
							for (MaterialInwardAccessoriesRequest access : item.getMaterialInwardAccessoriesRequest()) {
								accessoriesId = access.getId();
								if (access.getId() != null) {

									MaterialInwardAccessories accessories = new MaterialInwardAccessories();
									if (access.getId() != null) {
										accessories.setId(access.getId());
									}
									if (item.getItemId() != null) {
										accessories.setItem(new Item(item.getItemId()));
									}
									if (access.getReturnQuantity() != null) {
										accessories.setQuantity(access.getReturnQuantity());
									}
									if (access.getApprovedQuantity() != null) {
										accessories.setApprovedQty(access.getApprovedQuantity());
									}
									if (access.getSpecification() != null) {
										accessories.setSpecification(access.getSpecification());
									}
									accessories.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
									detail.addMaterialInwardAccessories(accessories);

									break;
								}

								if (accessoriesId == null) {
									detail.setIsDeleted(true);

								}
								detail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
							//	detail.setUpdatedBy(detail.getUser());
								detail.setUpdatedByUser(request.getUserId());
							}
						

							for (MaterialInwardAccessoriesRequest access : item.getMaterialInwardAccessoriesRequest()) {

								if (access.getId() == null) {
									MaterialInwardAccessories accData=new MaterialInwardAccessories(
											new Item(item.getItemId()), access.getReturnQuantity(),
											access.getApprovedQuantity(), access.getSpecification());
									accData.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
									detail.addMaterialInwardAccessories(accData);

									break;
								}
							}
						}

						}

						for (TaxDetail taxDetail : GlobalDao.getTaxDetail(detail.getTax().getId())) {

							Integer taxCompId = taxDetail.getTaxComponent().getId();
							Double taxPercentage = taxDetail.getTaxPercentage();
							Double amt = item.getTotalAmount();

							Double calTaxAmt = (amt * taxPercentage) / 100;
							InwardTaxComponent inwardTaxComponentData=new InwardTaxComponent(calTaxAmt,
									new MaterialInwardDetail(
											materialInward.getMaterialInwardsDetailList().getFirst().getId()),
									taxPercentage, new TaxComponent(taxCompId));
							inwardTaxComponentData.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
							detail.addInwardTaxCom(inwardTaxComponentData);
						}
					} else if (materialInward.getRequestType().getId() == Constants.creditNoteId) {
						detail.updateMaterialInwardDetail(item.getCreditNoteAmount(), item.getCreditTaxAmount(),
								item.getCreditNetAmount());

						creNoteAmt = creNoteAmt + item.getCreditNoteAmount();
						if (item.getCreditTaxAmount() != null) {
							creNoteTaxAmt = creNoteTaxAmt + item.getCreditTaxAmount();
						}
						creNetAmt = creNetAmt + item.getCreditNetAmount();
						/*
						 * Double creNoteAmt = 0.00; Double creNoteTaxAmt = 0.00; Double creNetAmt =
						 * 0.00;
						 */
					}
					break;
				}
			}
			if (id == null) {
				detail.setIsDeleted(true);
			}
			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
		//	detail.setUpdatedBy(detail.getUser());
			detail.setUpdatedByUser(request.getUserId());
		}

		MaterialInwardDetail materialInwardDetail = null;
		for (ItemRequest item : request.getItem()) {
			if (item.getId() == null) {

				if (request.getAssetTypeId() == Constants.consumableId) {
					MaterialInwardDetail materialInwardDetail2 = new MaterialInwardDetail(
							//new ReturnableGatepass(item.getReturnableGetPassId()),
							new ReturnableGatepassDetail(item.getReturnableGatepassDetailId()), /* change code for resolved HIS-5496 Material Inward - Replacement - Not working */
							new Item(item.getItemId()),
							item.getQuantity(), new UomType(item.getUomTypeId()), new UomUnit(item.getUomUnitId()),
							item.getBatchNo(), item.getExpiryDate(), item.getCop(), item.getMrp(),
							new Tax(item.getTaxId()), item.getTaxPercent(), item.getTaxAmount(), item.getTotalAmount(),
							item.getNetAmount(), item.getSpecification(), item.getMarkup(),
							new UomType(item.getMrpUomTypeId()), new Status(request.getStatusId()),
							item.getCreditNetAmount(), item.getTotalAmount());
				//	sessionService.setEntityDefaults(materialInwardDetail2);
					materialInwardDetail2.setIsReject(false);
					materialInwardDetail2.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
					materialInwardDetail = materialInward.addMaterialInwardDetailList(materialInwardDetail2);

					for (UomResponse uom : item.getUom()) {
						InwardsUomMapper inwardsUomMapper = new InwardsUomMapper(new UomType(uom.getUomTypeId()),
								new UomUnit(uom.getUomUnitId()), uom.getConversion(), uom.getIpUom(), uom.getOpUom(),
								uom.getStoreUom());
					//	sessionService.setEntityDefaults(inwardsUomMapper);
						inwardsUomMapper.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
						materialInwardDetail.addInwardsUomMapper(inwardsUomMapper);
					}

				} else if (request.getAssetTypeId() == Constants.fixedAssetId) {
					MaterialInwardDetail materialInwardDetail2 = new MaterialInwardDetail(
							//new ReturnableGatepass(item.getReturnableGetPassId()), 
							new ReturnableGatepassDetail(item.getReturnableGatepassDetailId()),/* change code for resolved HIS-5496 Material Inward - Replacement - Not working */
							new Item(item.getItemId()),
							item.getQuantity(), item.getBatchNo(), item.getExpiryDate(), item.getCop(), item.getMrp(),
							new Tax(item.getTaxId()), item.getTaxPercent(), item.getTaxAmount(), item.getTotalAmount(),
							item.getNetAmount(), item.getSpecification(), item.getMarkup(),
							new Status(request.getStatusId()));
					materialInwardDetail2.setIsReject(false);
				//	sessionService.setEntityDefaults(materialInwardDetail2);
					materialInwardDetail2.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
					materialInwardDetail = materialInward.addMaterialInwardDetailList(materialInwardDetail2);

					for (MaterialInwardAccessoriesRequest access : item.getMaterialInwardAccessoriesRequest()) {
						MaterialInwardAccessories materialInwardAccessories = new MaterialInwardAccessories(
								new Item(item.getItemId()), access.getReturnQuantity(), access.getApprovedQuantity(),
								access.getSpecification());
				//		sessionService.setEntityDefaults(materialInwardAccessories);
						materialInwardAccessories.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
						materialInwardDetail.addMaterialInwardAccessories(materialInwardAccessories);
					}
				}

				for (TaxDetail taxDetail : GlobalDao.getTaxDetail(item.getTaxId())) {
					Integer taxCompId = taxDetail.getTaxComponent().getId();
					Double taxPercentage = taxDetail.getTaxPercentage();
					Double amt = item.getTotalAmount();

					Double calTaxAmt = (amt * taxPercentage) / 100;
					InwardTaxComponent inwardTaxComponent = new InwardTaxComponent(calTaxAmt,
							new MaterialInwardDetail(materialInward.getMaterialInwardsDetailList().getFirst().getId()),
							taxPercentage, new TaxComponent(taxCompId));
					inwardTaxComponent.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
					materialInwardDetail.addInwardTaxCom(inwardTaxComponent);
				}

				if (materialInward.getRequestType().getId() == Constants.creditNoteId) {
					creNoteAmt = creNoteAmt + item.getCreditNoteAmount();
					if (item.getCreditTaxAmount() != null) {
						creNoteTaxAmt = creNoteTaxAmt + item.getCreditTaxAmount();
					}
					creNetAmt = creNetAmt + item.getCreditNetAmount();
				}

			}
		}

		/*
		 * if (materialInward.getRequestType().getId() == Constants.creditNoteId) {
		 * 
		 * materialInward.setCreditNote(creNoteAmt);
		 * materialInward.setCreditTaxAmount(creNoteTaxAmt);
		 * materialInward.setCreditNetAmount(creNetAmt);
		 * materialInward.setAvailableAmount(creNetAmt); }
		 */

		materialInward.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
	//	materialInward.setUpdatedBy(materialInward.getUser());
		materialInward.setUpdatedByUser(request.getUserId());
		materialInward.setCreditNetAmount(creNetAmt);
		materialInward.setCreditNote(creNoteAmt);
		materialInward.setCreditTaxAmount(creNoteTaxAmt);

		/*
		 * creNoteAmt=creNoteAmt+item.getCreditNoteAmount();
		 * creNoteTaxAmt=creNoteTaxAmt+item.getCreditTaxAmount();
		 * creNetAmt=creNetAmt+item.getCreditNetAmount();
		 */
		MaterialInwardDao.updateMaterialInward(materialInward);

		CommonResponse response = new CommonResponse();
		response.setMessage("Material Inward updated successfully");

		return response;
	}

	@Override
	@Transactional
	public Object getMaterialInward(Integer id,Integer unitId, Integer orgId) {

		MaterialInward materialInward = MaterialInwardDao.getMaterialInward(id);

		MaterialInwardResponse response = new MaterialInwardResponse(materialInward.getId(),
				materialInward.getInwordNo(), materialInward.getAddedDate(), materialInward.getStore().getStoreId(),
				materialInward.getStore().getStoreName(), materialInward.getVendor().getId(),
				materialInward.getVendor().getName(), materialInward.getAssetType().getId(),
				materialInward.getAssetType().getType(), materialInward.getRequestType().getId(),
				materialInward.getRequestType().getType(), materialInward.getReturnableGatepass().getId(),
				materialInward.getReturnableGatepass().getRetGpNo(), materialInward.getDocumentNo(),
				materialInward.getDocumentDate(), materialInward.getTotalAmount(), materialInward.getTotalTaxAmount(),
				materialInward.getNetAmount(), materialInward.getStatus().getId(),
				materialInward.getStatus().getStatus());
		response.setAddedBY(materialInward.getAddedBy().getUserName());
		response.setRemarks(materialInward.getRemark());
		response.setReturnAmount(materialInward.getReturnAmount());
		response.setVendorPayable(materialInward.getVendorPayable());
		response.setCreditNote(materialInward.getCreditNote());

		Double totalAmount = 0.00;
		Double totalTaxAmount = 0.00;
		Double totalNetAmount = 0.00;

		for (MaterialInwardDetail detail : materialInward.getMaterialInwardsDetailList()) {
			if (materialInward.getRequestType().getId() == Constants.replacementId) {

				if (response.getAssetTypeId() == Constants.consumableId) {
					response.addItems(new MaterialItemResponse(detail.getId(), 
							//detail.getReturnableGatepassId().getId(),
							detail.getMaterialInward().getReturnableGatepass().getId(),
							detail.getItem().getId(), detail.getItem().getCode(), detail.getItem().getName(),
							detail.getUomType().getId(), detail.getUomType().getType(), detail.getUomUnit().getId(),
							detail.getUomUnit().getUnits(), detail.getBatchNo(), detail.getExpiryDate(),
							detail.getCop(), detail.getTax() != null ? detail.getTax().getId() : null,
							detail.getTax() != null ? detail.getTax().getName() : null, detail.getTaxPercentage(),
							detail.getTaxAmount(), detail.getTotalAmount(), detail.getNetAmount(),
							detail.getReceivedQty(), detail.getStatus() != null ? detail.getStatus().getId() : null,
							detail.getStatus() != null ? detail.getStatus().getStatus() : null,
							detail.getMarkup() != null ? detail.getMarkup() : null, detail.getReturnQuantity(),
							detail.getMrp(), detail.getSpecification(), detail.getReturnAmount(),
							detail.getUomType().getId(),
							GlobalDao.getUnitOfMeasurementListByItem(detail.getItem().getId(),orgId), getTaxList(orgId), 
							detail.getItem().getIsThreeUOMApplicable()));
					totalAmount = totalAmount + detail.getTotalAmount();
					totalTaxAmount = totalTaxAmount + detail.getTaxAmount();
					totalNetAmount = totalNetAmount + detail.getNetAmount();

				} else if (response.getAssetTypeId() == Constants.fixedAssetId) {

					response.addItems(new MaterialItemResponse(detail.getId(), 
							//detail.getReturnableGatepassId().getId(),
							detail.getMaterialInward().getReturnableGatepass().getId(),
							detail.getItem().getId(), detail.getItem().getCode(), detail.getItem().getName(),
							detail.getBatchNo(), detail.getExpiryDate(), detail.getCop(),
							detail.getTax() != null ? detail.getTax().getId() : null,
							detail.getTax() != null ? detail.getTax().getName() : null, detail.getTaxPercentage(),
							detail.getTaxAmount(), detail.getTotalAmount(), detail.getNetAmount(),
							detail.getReceivedQty(), detail.getStatus() != null ? detail.getStatus().getId() : null,
							detail.getStatus() != null ? detail.getStatus().getStatus() : null,
							detail.getMarkup() != null ? detail.getMarkup() : null,
							//detail.getReturnableGatepassId().getReturnableGatepassDetails().get(0).getApprovedQty(),
							detail.getMaterialInward().getReturnableGatepass().getReturnableGatepassDetails().getFirst().getApprovedQty(),
							detail.getMrp(), detail.getSpecification(),
							//detail.getMrp() != null ? null : detail.getReturnableGatepassId().getNetAmount(),
							detail.getMrp() != null ? null : detail.getMaterialInward().getReturnableGatepass().getReturnableGatepassDetails().getFirst().getNetAmount(),
							PopulateAccessories(MaterialInwardDao.getInwardAccessoriesList(detail.getId())),
							getTaxList(orgId), detail.getItem().getIsThreeUOMApplicable()));
					totalAmount = totalAmount + detail.getTotalAmount();
					totalTaxAmount = totalTaxAmount + detail.getTaxAmount();
					totalNetAmount = totalNetAmount + detail.getNetAmount();

				}
			}

			if (materialInward.getRequestType().getId() == Constants.creditNoteId) {
				/*
				 * response.addItems(new
				 * MaterialItemResponse(materialInward.getReturnableGatepass().getId(),
				 * materialInward.getReturnableGatepass().getAddedDate(),
				 * materialInward.getReturnableGatepass().getTotalAmt(),
				 * materialInward.getReturnableGatepass().getNetAmount(),
				 * materialInward.getReturnableGatepass().getTotalTaxAmount(),
				 * materialInward.getStatus().getId(), materialInward.getStatus().getStatus(),
				 * materialInward.getCreditNote(), materialInward.getCreditTaxAmount(),
				 * materialInward.getCreditNetAmount()));
				 */
				//ReturnableGatepass rg=MaterialInwardDao.getReturnableGatepass(detail.getReturnableGatepassId());
				ReturnableGatepass rg=MaterialInwardDao.getReturnableGatepass(detail.getReturnableGatepassDetail().getReturnableGatepass());
				MaterialItemResponse mres =new MaterialItemResponse(detail.getId(),
						rg.getAddedDate(), detail.getTotalAmount(),
						detail.getNetAmount(), detail.getTaxAmount(), materialInward.getStatus().getId(),
						materialInward.getStatus().getStatus(), detail.getCreditNoteAmount(),
						 detail.getCreditNetAmount(),detail.getCreditTaxAmount());
				mres.setReturnableGatePassNo(rg.getRetGpNo());
				response.addItems(mres);

				// totalAmount = totalAmount + materialInward.getTotalAmount();
				// totalTaxAmount = totalTaxAmount + materialInward.getTotalTaxAmount();
				// totalNetAmount = totalNetAmount + materialInward.getNetAmount();
				//break;
			}

		}

		// response.setTotalAmount(totalAmount);
		// response.setTotalTaxAmount(totalTaxAmount);
		// response.setTotalNetAmount(totalNetAmount);
		return response;
	}

	private List<UomResponse> PopulateUom(List<InwardsUomMapper> uomMapper) {
		List<UomResponse> response = new ArrayList<UomResponse>();

		for (InwardsUomMapper uom : uomMapper) {
			response.add(new UomResponse(uom.getId(), uom.getUomType().getType(), uom.getUomType().getId(),
					uom.getUomUnit().getId(), uom.getUomUnit().getUnits(), uom.getConversion(), uom.getIpUom(),
					uom.getOpUom(), uom.getStoreUom()));
		}
		return response;
	}

	private List<AsseccoriesResponse> PopulateAccessories(List<MaterialInwardAccessories> InwardAccessories) {
		List<AsseccoriesResponse> response = new ArrayList<AsseccoriesResponse>();

		for (MaterialInwardAccessories accessories : InwardAccessories) {
			response.add(new AsseccoriesResponse(accessories.getId(), accessories.getQuantity(),
					accessories.getSpecification(), accessories.getApprovedQty()));
		}
		return response;
	}

	@Override
	@Transactional
	public Object cancelMaterialInward(MaterialInwardRequest request) {

		MaterialInward materialInward = MaterialInwardDao.getMaterialInward(request.getId());

		materialInward.setCancelReason(new CancelReason(request.getCancelReasonId()));
		materialInward.setCancelNote(request.getCancelNote());

		materialInward.setStatus(new Status(request.getStatusId()));

		materialInward.setUpdatedDate(TimeZoneComponent.getDateByzone(materialInward.getUnitId()));
	//	materialInward.setUpdatedBy(materialInward.getUser());
		materialInward.setUpdatedByUser(request.getUserId());
		
		MaterialInwardDao.updateMaterialInward(materialInward);

		CommonResponse response = new CommonResponse();
		response.setMessage("Material inward cancelled successfully");

		return response;
	}

	@Override
	@Transactional
	public Object getReturnableGatepassList(MaterialInwardRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		List<MaterialInwardResponse> response = new ArrayList<MaterialInwardResponse>();

		for (ReturnableGatepass returnableGatepass : MaterialInwardDao.searchReturnableGatepassList(request,
				screenId)) {
			boolean quantityCheckFlag=false;
			Integer getApprovedQuantity=MaterialInwardDao.getApprovedQuantity(returnableGatepass);
			if(getApprovedQuantity==null)
			{
				getApprovedQuantity=0;
			}
			
			MaterialInwardResponse result = new MaterialInwardResponse(returnableGatepass.getId(),
					returnableGatepass.getRetGpNo(), returnableGatepass.getAddedDate(),
					returnableGatepass.getAssetType().getId(), returnableGatepass.getAssetType().getType(),
					returnableGatepass.getRequestType().getId(), returnableGatepass.getRequestType().getType(),
					returnableGatepass.getStore().getStoreId(), returnableGatepass.getStore().getStoreName(),
					returnableGatepass.getVendor().getId(), returnableGatepass.getVendor().getName());

			for (ReturnableGatepassDetail detail : returnableGatepass.getReturnableGatepassDetails()) {
				MaterialItemResponse mres = new MaterialItemResponse(detail.getId(),
						detail.getBatch().getItem().getCode(), detail.getBatch().getItem().getName(),
						detail.getUomType()!=null ? detail.getUomType().getId() :null, detail.getUomType()!=null ? detail.getUomType().getType() :null
					   ,  detail.getUomUnit()!=null ? detail.getUomUnit().getId() :null,
					   detail.getUomUnit()!=null ?  detail.getUomUnit().getCode() :null, detail.getBatch().getId(), detail.getBatch().getBatchNo(),
						detail.getBatch().getExpiryDate()!=null ? detail.getBatch().getExpiryDate() :null, detail.getTax()!=null ? detail.getTax().getId() :null, detail.getTax()!=null ? detail.getTax().getName():null,
						detail.getTaxPercentage()!=null ? detail.getTaxPercentage():null, (detail.getApprovedQty()-getApprovedQuantity),
						GlobalDao.getUnitOfMeasurementListByItem(detail.getBatch().getItem().getId(),request.getOrganizationId()),
						detail.getBatch().getCop(), detail.getBatch().getItem().getId(), detail.getBatch().getItem().getIsThreeUOMApplicable());
				mres.setTaxCode(detail.getTax()!=null ? detail.getTax().getCode():null);
				mres.setReturnAmount(detail.getTotalAmt());
				if((detail.getApprovedQty()-getApprovedQuantity)>0)
				{
					quantityCheckFlag=true;
					result.addItems(mres);
				}

			}
			if(quantityCheckFlag)
			{
				response.add(result);
			}
		}

		return response;
	}

	@Override
	@Transactional
	public Object getReturnableGatepassSearchList(MaterialInwardRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		List<MaterialInwardResponse> response = new ArrayList<MaterialInwardResponse>();
		for (ReturnableGatepass returnableGatepass : MaterialInwardDao.searchReturnableGatepassList(request.getId(),
				screenId,sessionService.getUnitId())) {

			MaterialInwardResponse result = new MaterialInwardResponse(returnableGatepass.getId(),
					returnableGatepass.getRetGpNo(), returnableGatepass.getAddedDate(),
					returnableGatepass.getAssetType().getId(), returnableGatepass.getAssetType().getType(),
					returnableGatepass.getRequestType().getId(), returnableGatepass.getRequestType().getType(),
					returnableGatepass.getStore().getStoreId(), returnableGatepass.getStore().getStoreName(),
					returnableGatepass.getVendor().getId(), returnableGatepass.getVendor().getName(),
					returnableGatepass.getTotalAmt(), returnableGatepass.getTotalTaxAmount(),
					returnableGatepass.getNetAmount());

			for (ReturnableGatepassDetail detail : returnableGatepass.getReturnableGatepassDetails()) {

				for (Integer item : request.getItemsId()) {

					if (item != null && item.equals(detail.getId())) {
						result.addItems(new MaterialItemResponse(detail.getId(), detail.getBatch().getItem().getId(),
								detail.getBatch().getItem().getCode(), detail.getBatch().getItem().getName(),
								detail.getUomType().getId(), detail.getUomType().getType(), detail.getUomUnit().getId(),
								detail.getUomUnit().getUnits(), detail.getTax().getId(), detail.getTax().getName(),
								detail.getTaxPercentage(), detail.getReturnQty(), detail.getNetAmount(),
								GlobalDao.getUnitOfMeasurementListByItem(detail.getBatch().getItem().getId(),null),
								getTaxList(sessionService.getOrganizationId()), detail.getBatch().getItem().getIsThreeUOMApplicable()));
					}

				}
				response.add(result);
			}
		}
		return response;
	}

	@Override
	@Transactional
	public Object getMultipleReturnableGatepassSearch(List<MaterialInwardRequest> req,Integer orgId,Integer unitId) {
		List<MaterialInwardResponse> response = new ArrayList<MaterialInwardResponse>();
		for (MaterialInwardRequest request : req) {
			request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

			for (ReturnableGatepass returnableGatepass : MaterialInwardDao.searchReturnableGatepassList(request.getId(),
					screenId,unitId)) {

				MaterialInwardResponse result = new MaterialInwardResponse(returnableGatepass.getId(),
						returnableGatepass.getRetGpNo(), returnableGatepass.getAddedDate(),
						returnableGatepass.getAssetType().getId(), returnableGatepass.getAssetType().getType(),
						returnableGatepass.getRequestType().getId(), returnableGatepass.getRequestType().getType(),
						returnableGatepass.getStore().getStoreId(), returnableGatepass.getStore().getStoreName(),
						returnableGatepass.getVendor().getId(), returnableGatepass.getVendor().getName(),
						returnableGatepass.getTotalAmt(), returnableGatepass.getTotalTaxAmount(),
						returnableGatepass.getNetAmount());

				for (ReturnableGatepassDetail detail : returnableGatepass.getReturnableGatepassDetails()) {

					//for (Integer item : request.getItemsId()) {

						//if (item != null && item.equals(detail.getBatch().getItem().getId())) { // removed because credit note changed to request level
					if (true) {
							result.addItems(new MaterialItemResponse(/*returnableGatepass.getId()*/detail.getId(),//code change for resolved HIS-5496 Material Inward - Replacement - Not working
									detail.getBatch().getItem().getId(), detail.getBatch().getItem().getCode(),
									detail.getBatch().getItem().getName(),detail.getUomType()!=null ? detail.getUomType().getId() :null,
									detail.getUomType()!=null ? detail.getUomType().getType():null, detail.getUomUnit()!=null ?detail.getUomUnit().getId():null,
									detail.getUomUnit()!=null ?detail.getUomUnit().getUnits():null, detail.getTax()!=null ? detail.getTax().getId():null, detail.getTax()!=null ?detail.getTax().getName():null,
									detail.getTaxPercentage()!=null ?detail.getTaxPercentage():null, detail.getApprovedQty(), detail.getNetAmount(),
									GlobalDao.getUnitOfMeasurementListByItem(detail.getBatch().getItem().getId(),orgId),
									getTaxList(orgId), detail.getBatch().getItem().getIsThreeUOMApplicable()));
						}

					//}
					// response.add(result);
				}

				response.add(result);
			}

		}

		return response;

	}

	private List<TaxDropDown> getTaxList(Integer orgId) {
		List<TaxDropDown> taxList = new ArrayList<TaxDropDown>();
		for (Tax tax : GlobalDao.getTaxList(orgId)) {

			TaxDropDown taxDto = new TaxDropDown(tax.getId(), tax.getName(), tax.getTaxPercentage());
			taxList.add(taxDto);
		}
		return taxList;
	}

	@Override
	@Transactional
	public Object getReturnableGatepassVendorWiseSearch(Integer vendorid) {

		List<CreditNoteVendorWise> data = MaterialInwardDao.getReturnableGatepassVendorWiseSearch(vendorid);
		Integer creditDays=MaterialInwardDao.getNumberOfCreditDays(vendorid);
		
		if(data == null || data.size()==0)
		{
			CreditNoteVendorWise dto = new CreditNoteVendorWise();
			dto.setCreditDays(creditDays!=null ? creditDays:0);
			data.add(dto);
			
		}else {
			for (CreditNoteVendorWise c : data) {
				List<CreditNoteVendorWise> d = MaterialInwardDao.getReturnableGatepassDetails(c.getMateinwardid());
				c.setCreditDays(creditDays!=null ? creditDays:0);
				c.setRgDetails(d);
			}
		}
		return data;
	}

	@Override
	public Object saveMaterialInwardNew(MaterialInwardRequestNew request) {

		CommonResponse response = new CommonResponse();
		Integer organizationId = request.getOrganizationId()!=null ?request.getOrganizationId() : sessionService.getOrganizationId();
		Integer unitId = request.getUnitId()!=null ?request.getUnitId() : sessionService.getUnitId();
		Integer userId = request.getUserId()!=null ?request.getUserId() : sessionService.getUserId();
		// for (MaterialInwardRequest request : req) {
		/*
		 * Integer storeId = request.getStoreId();
		 * 
		 * Store storeNew = GlobalDao.getStore(storeId);
		 * 
		 * // System.out.println("Request storeId:-" + storeNew);
		 * 
		 * Unit unit = GlobalDao.getUnit(sessionService.getUnitId());
		 * 
		 * // System.out.println("session UnitId :-" + unit + "screenId:-" + screenId);
		 */
		String inwardNumber = GlobalService.getBillNoByGeneratedCode(request.getStoreId(), sessionService.getUnitId(),
				screenId);

		/*
		 * MaterialInward materialInward = new MaterialInward(new
		 * Store(request.getStoreId()), new Vendor(request.getVendorId()), new
		 * AssetType(request.getAssetTypeId()), new
		 * RequestType(request.getRequestTypeId()), new
		 * ReturnableGatepass(request.getReturnableGetPassId()),
		 * request.getTotalAmount(), request.getTotalTaxAmount(),
		 * request.getTotalNetAmount(), inwardNumber, request.getRemark(), new
		 * Status(request.getStatusId()), new Status(request.getApprovalStatusId()),
		 * request.getDocumentNo(), request.getDocumentDate(),
		 * request.getReplacementId());
		 */
		String replaceType = null;
		if (request.getRequestTypeId() == Constants.replacementId) {
			replaceType = GlobalService.getBillNoByGeneratedCode(request.getStoreId(), sessionService.getUnitId(),
					Screen.MaterialInwardReplacement.getScreen());
		} else {
			replaceType = GlobalService.getBillNoByGeneratedCode(request.getStoreId(), sessionService.getUnitId(),
					Screen.MaterialInwardNote.getScreen());
		}

		MaterialInward materialInward = new MaterialInward(new Store(request.getStoreId()),
				new Vendor(request.getVendorId()), new AssetType(request.getAssetTypeId()),
				new RequestType(request.getRequestTypeId()), inwardNumber, request.getRemark(),
				new Status(request.getStatusId()), new Status(request.getApprovalStatusId()), request.getDocumentNo(),
				request.getDocumentDate(), request.getReplacementId(), replaceType);
		materialInward.setTotalAmount(request.getTotalAmount());
		materialInward.setTotalTaxAmount(request.getTotalTaxAmount());
		materialInward.setNetAmount(request.getNetAmount());
		materialInward.setReturnAmount(request.getReturnAmt());

		if (request.getNetAmount() != null && request.getReturnAmt() != null) {
			if (request.getNetAmount() < request.getReturnAmt()) {
				materialInward.setCreditNote(request.getReturnAmt() - request.getNetAmount());
				materialInward.setCreditNetAmount(request.getReturnAmt() - request.getNetAmount());
				materialInward.setCreditTaxAmount(0.00);
				materialInward.setVendorPayable(0.00);
			} else if (request.getNetAmount() > request.getReturnAmt()) {
				materialInward.setVendorPayable(request.getNetAmount() - request.getReturnAmt());
				materialInward.setCreditNote(0.00);
				materialInward.setCreditTaxAmount(0.00);
				materialInward.setCreditNetAmount(0.00);
			} else if (request.getNetAmount() == request.getReturnAmt()) {
				materialInward.setCreditNote(0.00);
				materialInward.setCreditTaxAmount(0.00);
				materialInward.setCreditNetAmount(0.00);
				materialInward.setVendorPayable(0.00);
			}
		}

		MaterialInwardDetail materialInwardDetail = null;
		// sessionService.setEntityDefaults(materialInwardDetail);
		Double creNoteAmt = 0.00;
		Double creNoteTaxAmt = 0.00;
		Double creNetAmt = 0.00;

		for (ItemRequestNew item : request.getItem()) {

			if (request.getRequestTypeId() == Constants.replacementId) {

				if (request.getAssetTypeId() == Constants.consumableId) {
					// request.
					ReturnableGatepass returnableGatepass = new ReturnableGatepass(item.getReturnableGetPassId());
					MaterialInwardDetail materialInwardDetail2 = new MaterialInwardDetail(
							//returnableGatepass,
							new ReturnableGatepassDetail(item.getReturnableGatepassDetailId()),/* change code for resolved HIS-5496 Material Inward - Replacement - Not working */
							new Item(item.getItemId()), item.getQuantity(), new UomType(item.getUomTypeId()),
							new UomUnit(item.getUomUnitId()), item.getBatchNo(), item.getExpiryDate(), item.getCop(),
							item.getMrp(), new Tax(item.getTaxId()), item.getTaxPercent(), item.getTaxAmount(),
							item.getTotalAmount(), item.getNetAmount(), item.getSpecification(), item.getMarkup(),
							new UomType(item.getMrpUomTypeId()), new Status(request.getStatusId()),
							item.getNetAmount() > 0 ? item.getNetAmount() - returnableGatepass.getNetAmount() : 0.00,
							item.getNetAmount() > 0 ? item.getNetAmount() - returnableGatepass.getNetAmount() : 0.00);
					materialInwardDetail2.setReturnQuantity(item.getReturnQuantity());
					materialInwardDetail2.setReturnAmount(item.getReturnAmount());
				//	sessionService.setEntityDefaults(materialInwardDetail2);
					materialInwardDetail2.setCreateDefaults(organizationId ,unitId,userId);
					materialInwardDetail = materialInward.addMaterialInwardDetailList(materialInwardDetail2);
					materialInward.setReturnableGatepass(new ReturnableGatepass(item.getReturnableGetPassId()));

					if (item.getUom() != null) {
						for (UomResponse uom : item.getUom()) {
							InwardsUomMapper inwardsUomMapper = new InwardsUomMapper(new UomType(uom.getUomTypeId()),
									new UomUnit(uom.getUomUnitId()), uom.getConversion(), uom.getIpUom(),
									uom.getOpUom(), uom.getStoreUom());
						//	sessionService.setEntityDefaults(inwardsUomMapper);
							inwardsUomMapper.setCreateDefaults(organizationId ,unitId,userId);
							materialInwardDetail.addInwardsUomMapper(inwardsUomMapper);
						}
					}
				} else if (request.getAssetTypeId() == Constants.fixedAssetId) {
					ReturnableGatepass returnableGatepass = new ReturnableGatepass(item.getReturnableGetPassId());
					MaterialInwardDetail materialInwardDetail2 = new MaterialInwardDetail(
							//new ReturnableGatepass(item.getReturnableGetPassId()),
							new ReturnableGatepassDetail(item.getReturnableGatepassDetailId()),/* change code for resolved HIS-5496 Material Inward - Replacement - Not working */
							new Item(item.getItemId()),
							item.getQuantity(), item.getBatchNo(), item.getExpiryDate(), item.getCop(), item.getMrp(),
							new Tax(item.getTaxId()), item.getTaxPercent(), item.getTaxAmount(), item.getTotalAmount(),
							item.getNetAmount(), item.getSpecification(), item.getMarkup(),
							new Status(request.getStatusId()));
					materialInwardDetail2.setNetAmount(item.getNetAmount());
					materialInwardDetail2.setCreditNetAmount(
							item.getNetAmount() > 0 ? item.getNetAmount() - returnableGatepass.getNetAmount() : 0.00);
					materialInwardDetail2.setAvailableAmount(
							item.getNetAmount() > 0 ? item.getNetAmount() - returnableGatepass.getNetAmount() : 0.00);
				//	sessionService.setEntityDefaults(materialInwardDetail2);
					materialInwardDetail2.setCreateDefaults(organizationId ,unitId,userId);
					materialInwardDetail = materialInward.addMaterialInwardDetailList(materialInwardDetail2);
					materialInward.setReturnableGatepass(new ReturnableGatepass(item.getReturnableGetPassId()));

					if (item.getMaterialInwardAccessoriesRequest() != null) {
						for (MaterialInwardAccessoriesRequest access : item.getMaterialInwardAccessoriesRequest()) {
							MaterialInwardAccessories materialInwardAccessories = new MaterialInwardAccessories(
									new Item(item.getItemId()), access.getReturnQuantity(),
									access.getApprovedQuantity(), access.getSpecification());
						//	sessionService.setEntityDefaults(materialInwardAccessories);
							materialInwardAccessories.setCreateDefaults(organizationId ,unitId,userId);
							materialInwardDetail.addMaterialInwardAccessories(materialInwardAccessories);
						}
					}

				}

				for (TaxDetail taxDetail : GlobalDao.getTaxDetail(item.getTaxId())) {

					Integer taxCompId = taxDetail.getTaxComponent().getId();
					Double taxPercentage = taxDetail.getTaxPercentage();
					Double amt = item.getTotalAmount();

					Double calTaxAmt = (amt * taxPercentage) / 100;
					InwardTaxComponent inwardTaxComponent = new InwardTaxComponent(calTaxAmt,
							new MaterialInwardDetail(materialInward.getMaterialInwardsDetailList().getFirst().getId()),
							taxPercentage, new TaxComponent(taxCompId));
				//	sessionService.setEntityDefaults(inwardTaxComponent);
					inwardTaxComponent.setCreateDefaults(organizationId ,unitId,userId);
					materialInwardDetail.addInwardTaxCom(inwardTaxComponent);
				}

				//sessionService.setEntityDefaults(materialInwardDetail);
				materialInwardDetail.setCreateDefaults(organizationId ,unitId,userId);
			} else if (request.getRequestTypeId() == Constants.creditNoteId) {
				MaterialInwardDetail materialInwardDetail2 = new MaterialInwardDetail(item.getCreditNoteAmount(),
						item.getCreditTaxAmount(), item.getCreditNetAmount(), new Status(request.getStatusId()),
						item.getTotalAmount(), item.getTotalTaxAmount(), item.getTotalNetAmount(),
						//new ReturnableGatepass(item.getReturnableGetPassId())
						new ReturnableGatepassDetail(item.getReturnableGatepassDetailId())/* change code for resolved HIS-5496 Material Inward - Replacement - Not working */
						);
			//	sessionService.setEntityDefaults(materialInwardDetail2);
				materialInwardDetail2.setCreateDefaults(organizationId ,unitId,userId);
				materialInwardDetail = materialInward.addMaterialInwardDetailList(materialInwardDetail2);
				materialInward.setReturnableGatepass(new ReturnableGatepass(item.getReturnableGetPassId()));
				creNoteAmt = creNoteAmt + item.getCreditNoteAmount();
				if (item.getCreditTaxAmount() != null) {
					creNoteTaxAmt = creNoteTaxAmt + item.getCreditTaxAmount();
				}

				creNetAmt = creNetAmt + item.getCreditNetAmount();

				// quantityToOrdered
				/*
				 * for (UomResponse uom : item.getUom()) { InwardsUomMapper inwardsUomMapper =
				 * new InwardsUomMapper(new UomType(uom.getUomTypeId()), new
				 * UomUnit(uom.getUomUnitId()), uom.getConversion(), uom.getIpUom(),
				 * uom.getOpUom(), uom.getStoreUom());
				 * sessionService.setEntityDefaults(inwardsUomMapper);
				 * materialInwardDetail.addInwardsUomMapper(inwardsUomMapper); }
				 */

				/*
				 * for (UomResponse uom : item.getUom()) {
				 * materialInwardDetail.addInwardsUomMapper( new InwardsUomMapper(new
				 * UomType(uom.getUomTypeId()), new UomUnit(uom.getUomUnitId()),
				 * uom.getConversion(), uom.getIpUom(), uom.getOpUom(), uom.getStoreUom()));
				 * 
				 * }
				 */
			}

		}

		if (request.getRequestTypeId() == Constants.creditNoteId) {
			materialInward.setCreditNote(creNoteAmt);
			materialInward.setCreditTaxAmount(creNoteTaxAmt);
			materialInward.setCreditNetAmount(creNetAmt);
			materialInward.setAvailableAmount(creNetAmt);

			// materialInward.setTotalAmount(item.getTotalAmount());
			// materialInward.setTotalTaxAmount(item.getTotalTaxAmount());
			// materialInward.setNetAmount(item.getTotalNetAmount());
		}

	//	sessionService.setEntityDefaults(materialInward);
		materialInward.setCreateDefaults(organizationId ,unitId,userId);
		MaterialInwardDao.saveMaterialInward(materialInward);
		response.setId(materialInward.getId().longValue());
		response.setMessage("Material Inward saved successfully");

		// }
		return response;

	}

}
