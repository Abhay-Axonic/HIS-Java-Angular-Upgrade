package com.param.lis.histopathology.transaction.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.entity.lis.histo.TSpecimanMaster;
import com.param.entity.lis.histo.TSubSpecimanMaster;
import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.entity.lis.transaction.LabSampleMaster;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.lis.global.common.CommonUtils;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.histopathology.transaction.dao.IHistoMacroScopicExaminationDao;
import com.param.lis.histopathology.transaction.dao.ISpecimanReceiptDao;
import com.param.lis.histopathology.transaction.dao.ITSubSpecimemDao;
import com.param.lis.histopathology.transaction.dto.TSpecimanMasterDto;
import com.param.lis.histopathology.transaction.dto.specimanReceiptDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SpecimanReceiptServiceImpl
		implements ISpecimanReceiptService, ICommonConstants, IError, ITransactionConstants,IBillingCommonConstants {





	@Autowired
	ISpecimanReceiptDao iSpecimanReceiptDao;

	@Autowired
	ITSubSpecimemDao iTSubSpecimemDao;

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private IHistoMacroScopicExaminationDao iHistoMicroScopicExaminationDao;

	@Autowired
    IGlobalUnitMasterDao iGlobalUnitMasterDao;
	
	@Override
	@Transactional
	public Response listSpecimanReceipt(SearchDto searchDto,Character isPreOrPostBilling) throws ApplicationException {
		try {
			return iSpecimanReceiptDao.listSpecimanReceipt(searchDto,isPreOrPostBilling);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getTotalSpecimanReceiptRecord(SearchDto searchDto,Character isPreOrPostBilling) throws ApplicationException {
		try {
			return iSpecimanReceiptDao.getTotalSpecimanReceiptRecord(searchDto,isPreOrPostBilling);
		} catch (Exception e) {
			
			throw e;
		}
	}             

	@Override
	@Transactional
	public Response collectionSpeciman(List<specimanReceiptDto> listspecimanReceiptDto) {
		try {
			if (!listspecimanReceiptDto.isEmpty()) {
				for (Iterator iterator = listspecimanReceiptDto.iterator(); iterator.hasNext();) {
					specimanReceiptDto specimanReceiptDto = (specimanReceiptDto) iterator.next();
					LabSampleMaster labSampleMaster = new LabSampleMaster();
					String SPECIMENNUMBER = iGlobalUnitMasterDao.getGeneratedNo(0, specimanReceiptDto.getOrgUnitId(), SPECIMEN_NUMBER);
					BigInteger sampleNo = (BigInteger) sessionFactory.getCurrentSession()
							.getNamedQuery("GENERATE_SAMPLE_NO").uniqueResult();
					String sampleNoString = "";
					LabSampleDetailsMaster labSampleDetailsMaster = new LabSampleDetailsMaster();
					List<LabSampleDetailsMaster> listLabSampleDetailsMaster = new ArrayList<>();

					labSampleMaster.setOrgId(specimanReceiptDto.getOrgId());
					labSampleMaster.setOrgUnitId(specimanReceiptDto.getOrgUnitId());

					labSampleMaster.setOrderId(specimanReceiptDto.getOrderId());
					labSampleMaster.setAdmissionId(specimanReceiptDto.getVisitAdmId());
					labSampleMaster.setEncounterId(specimanReceiptDto.getEncounterId());
					labSampleMaster.setCreatedBy(specimanReceiptDto.getCreatedBy());
					labSampleMaster.setCreatedDate(specimanReceiptDto.getCreatedDate());
					labSampleMaster.setPatientId(specimanReceiptDto.getPatientId());
					labSampleMaster.setVisitTypeId(specimanReceiptDto.getVisitTypeId());

					if (specimanReceiptDto.getIsHistoCyto() != null && specimanReceiptDto.getIsHistoCyto() == 'H') {
						
                        sampleNoString = CommonUtils.numberToString(sampleNo,SPECIMENNUMBER != null ? SPECIMENNUMBER : HISTOPATHLOGY_PREFIX, NUMBER_WIDTH);
					} else if (specimanReceiptDto.getIsHistoCyto() != null
							&& specimanReceiptDto.getIsHistoCyto() == 'C') {

						if (specimanReceiptDto.getIsGynac() != null && specimanReceiptDto.getIsGynac() == 'Y') {
                            sampleNoString = CommonUtils.numberToString(sampleNo,SPECIMENNUMBER != null ? SPECIMENNUMBER : CYTO_GYNAC_PREFIX, NUMBER_WIDTH);
						} else if (specimanReceiptDto.getIsGynac() != null && specimanReceiptDto.getIsGynac() == 'N') {
							if (specimanReceiptDto.getIsFnac() != null && specimanReceiptDto.getIsFnac() == 'Y') {
                                sampleNoString = CommonUtils.numberToString(sampleNo,SPECIMENNUMBER != null ? SPECIMENNUMBER : CYTO_NON_GYNAC_FNAC_PREFIX,
										NUMBER_WIDTH);
							} else if (specimanReceiptDto.getIsFnac() != null
									&& specimanReceiptDto.getIsFnac() == 'N') {
                                sampleNoString = CommonUtils.numberToString(sampleNo,SPECIMENNUMBER != null ? SPECIMENNUMBER : CYTO_NON_GYNAC_NON_FANAC_PREFIX,
										NUMBER_WIDTH);
							}
						}
					}

					labSampleMaster.setSampleId(sampleNoString);
					labSampleMaster.setSampleNo(sampleNoString);
					labSampleDetailsMaster.setSampleNo(sampleNo);
					labSampleDetailsMaster.setCreatedDate(specimanReceiptDto.getCreatedDate());
					labSampleDetailsMaster.setPatientVisitAge(specimanReceiptDto.getCreatedDate());
					labSampleDetailsMaster.setAssignUnit(specimanReceiptDto.getOrgUnitId());
					labSampleDetailsMaster.setPerformedUnit(specimanReceiptDto.getOrgUnitId());
					labSampleDetailsMaster.setSampleRecollectFlag('N');
					labSampleDetailsMaster.setTestId(specimanReceiptDto.getTestId());
					labSampleDetailsMaster.setDeptId(specimanReceiptDto.getDeptId());
					labSampleDetailsMaster.setSubDeptId(specimanReceiptDto.getSubDeptId());
					labSampleDetailsMaster.setCreatedBy(specimanReceiptDto.getCreatedBy());
					labSampleDetailsMaster.setOrgId(specimanReceiptDto.getOrgId());
					labSampleDetailsMaster.setOrgUnitId(specimanReceiptDto.getOrgUnitId());
					labSampleDetailsMaster.setOrderId(specimanReceiptDto.getOrderId());
					labSampleDetailsMaster.setOrderDetailsId(specimanReceiptDto.getOrderDetailsId());
					labSampleDetailsMaster.setPackageId(specimanReceiptDto.getPackageId());
					labSampleDetailsMaster.setSampleTypeId(specimanReceiptDto.getSampleId());
					labSampleDetailsMaster.setContainerId(specimanReceiptDto.getContainerId());
					labSampleDetailsMaster.setSampleReqCount(0);
					labSampleDetailsMaster.setSamplePendingCount(0);
					labSampleDetailsMaster.setIsOutsourced(specimanReceiptDto.getIsOutsourced());
					labSampleDetailsMaster.setPriorityId(specimanReceiptDto.getPriorityId());
					labSampleDetailsMaster.setPatientId(specimanReceiptDto.getPatientId());
					labSampleDetailsMaster.setDoctorId(specimanReceiptDto.getDoctorId());
					labSampleDetailsMaster.setGenderId(specimanReceiptDto.getGenderId());
					labSampleDetailsMaster.setSampleBarcode(sampleNoString);
					labSampleDetailsMaster.setUpdatedBy(specimanReceiptDto.getUpdatedBy()!=null ?specimanReceiptDto.getUpdatedBy() :specimanReceiptDto.getCreatedBy());
					labSampleDetailsMaster.setUpdatedDate(specimanReceiptDto.getUpdatedDate()!=null ? specimanReceiptDto.getUpdatedDate() :specimanReceiptDto.getCreatedDate() );
					listLabSampleDetailsMaster.add(labSampleDetailsMaster);
					labSampleMaster.setListLabSampleDetailsMaster(listLabSampleDetailsMaster);

					Response specimanRes = iSpecimanReceiptDao.collectionSpeciman(labSampleMaster);
					if (specimanRes.getCode().equals(SUCCESS_200) && specimanRes.getObject() != null) {
						LabSampleDetailsMaster labSampleDetailsMst = (LabSampleDetailsMaster) specimanRes.getObject();

						TSpecimanMasterDto tSpecimanMasterDto = new TSpecimanMasterDto();
						tSpecimanMasterDto.setOrgId(specimanReceiptDto.getOrgId());
						tSpecimanMasterDto.setOrgUnitId(specimanReceiptDto.getOrgUnitId());
						tSpecimanMasterDto.setSpecimanId(specimanReceiptDto.getSpecimanId());
						tSpecimanMasterDto.setSpecimanTypeId(specimanReceiptDto.getSpecimanTypeId());
						tSpecimanMasterDto.setSpecimanName(specimanReceiptDto.getSpecimanName());
						tSpecimanMasterDto.setIsDeleted('N');
						tSpecimanMasterDto.setCreatedBy(specimanReceiptDto.getCreatedBy());
						tSpecimanMasterDto.setCreatedDate(specimanReceiptDto.getCreatedDate());
						tSpecimanMasterDto.setLabSampleDtlsId(labSampleDetailsMst.getLabSampleDtlsId());
						tSpecimanMasterDto.setHistopathlogyNumber(sampleNoString);
						Response tSpecimanRes = iHistoMicroScopicExaminationDao.saveSpecimanDetails(tSpecimanMasterDto);

						if (tSpecimanRes.getCode().equals(SUCCESS_200) && tSpecimanRes.getObject() != null) {
							if (specimanReceiptDto.getIsHistoCyto() != null
									&& specimanReceiptDto.getIsHistoCyto() == 'C') {
								
								TSpecimanMaster tTSpecimanMaster = (TSpecimanMaster) tSpecimanRes.getObject();
								TSubSpecimanMaster tSubSpecimanMaster = new TSubSpecimanMaster();
								tSubSpecimanMaster.settSpecimanId(tTSpecimanMaster.gettSpecimanId());
								tSubSpecimanMaster.setOrgId(specimanReceiptDto.getOrgId());
								tSubSpecimanMaster.setOrgUnitId(specimanReceiptDto.getOrgUnitId());
								tSubSpecimanMaster.setIsDeleted('N');
								tSubSpecimanMaster.setCreatedBy(specimanReceiptDto.getCreatedBy());
								tSubSpecimanMaster.setCreatedDate(specimanReceiptDto.getCreatedDate());
								tSubSpecimanMaster.setLabSampleDtlsId(labSampleDetailsMst.getLabSampleDtlsId());
								tSubSpecimanMaster.setSubSpcimanNo(sampleNoString);
								tSubSpecimanMaster.setSubSpecimanName(specimanReceiptDto.getSpecimanName());
								tSubSpecimanMaster.setSubSpecimanNum(BigInteger.ONE);
								iTSubSpecimemDao.saveSubSpecimanDetails(tSubSpecimanMaster);
							}
						}
					} else {
						return new Response(ERROR, ERR_500, "Failed To Collect Specimen.", null, null);
					}

				}
				return new Response(SUCCESS, SUCCESS_200, "Specimen Accepted Successfully.", null, null);
			} else {
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
			}

		} catch (Exception e) {
			
			throw e;
		}
	}

}
