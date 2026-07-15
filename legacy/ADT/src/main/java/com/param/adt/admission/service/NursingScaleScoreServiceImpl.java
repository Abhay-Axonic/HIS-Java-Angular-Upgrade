package com.param.adt.admission.service;

import com.param.adt.admission.dao.*;
import com.param.adt.admission.dto.*;
import com.param.adt.common.Response;
import com.param.global.common.ICommonConstants;
import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@SuppressWarnings({"rawtypes","unused","unchecked"})
public class NursingScaleScoreServiceImpl implements INursingScaleScoreService, ICommonConstants {

    @Autowired
    private INursingScaleScoreDao iNursingScaleScoreDao;

    @Autowired
    private IGcsScaleScoreDao iGcsScaleScoreDao;

    @Autowired
    private IGcsDraftScaleScoreDao iGcsDraftScaleScoreDao;

    @Autowired
    private IApacheScaleScoreDao iApacheScaleScoreDao;

    @Autowired
    private IApacheDraftScaleScoreDao iApacheDraftScaleScoreDao;

    @Autowired
    private ISofaScaleScoreDao iSofaScaleScoreDao;

    @Autowired
    private ISofaDraftScaleScoreDao iSofaDraftScaleScoreDao;

    @Autowired
    private IBradenScaleScoreDao iBradenScaleScoreDao;

    @Autowired
    private IBradenDraftScaleScoreDao iBradenDraftScaleScoreDao;

    @Autowired
    private INpuapScaleScoreDao iNpuapScaleScoreDao;

    @Autowired
    private IBishopScaleScoreDao iBishopScaleScoreDao;

    @Autowired
    private IAldreteScaleScoreDao iAldreteScaleScoreDao;

    @Autowired
    private IAPGARScaleScoreDao iAPGARScaleScoreDao;

    @Autowired
    private IMallamapattiClassDao iMallamapattiClassDao;
    
    @Autowired
    IFallRiskScoreDao iFallRiskScoreDao;

    @Override
    public Response saveNursingScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO) {
        try {

//            boolean isNursingScaleScorePresent = false;
//            Response res = iNursingScaleScoreDao.getAllNursingScaleScore(gcsScaleScoreDTO.getUnitId(), gcsScaleScoreDTO.getOrganizationId(), gcsScaleScoreDTO.getAdmissionId(), gcsScaleScoreDTO.getEncounterId());
//
//            List<NursingScaleScoreDTO> getAllNursingScaleScore = res.getListObject();
//
//            if (getAllNursingScaleScore.size() > 0 && getAllNursingScaleScore != null) {
//
//                for (NursingScaleScoreDTO details : getAllNursingScaleScore) {
//
//                    if (details.getTabNumber()==gcsScaleScoreDTO.getTabNumber()) {
//                        isNursingScaleScorePresent = true;
//                        iNursingScaleScoreDao.updateNursingScaleScoreDaoById(details.getNursingScaleScoreId(), gcsScaleScoreDTO);
//                    }
//                }
//
//                if (isNursingScaleScorePresent == false) {
//                    iNursingScaleScoreDao.saveNursingScaleScore(gcsScaleScoreDTO);
//                }
//
//            } else {
                iNursingScaleScoreDao.saveNursingScaleScore(gcsScaleScoreDTO);
//            }

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response getAllNursingScaleScore(Integer unitId, Integer organizationId, Integer admissionId, Integer encounterId) {
        try {
            return iNursingScaleScoreDao.getAllNursingScaleScore(unitId, organizationId, admissionId, encounterId);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response getAllNursingScoreById(Integer unitId, Integer organizationId, Integer admissionId, Integer tabNumber) {
        try {
            return iNursingScaleScoreDao.getAllNursingScoreById(unitId, organizationId, admissionId, tabNumber);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response saveGcsScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO) {
        try {
            iGcsScaleScoreDao.saveGcsScaleScore(gcsScaleScoreDTO);
            Response getGcsDraftScaleScores = iGcsDraftScaleScoreDao.getAllGcsDraftScaleScores(gcsScaleScoreDTO.getUnitId(),
                    gcsScaleScoreDTO.getOrganizationId(), gcsScaleScoreDTO.getAdmissionId());
            if (getGcsDraftScaleScores.getListObject().size() > 0 && getGcsDraftScaleScores.getListObject() != null) {
                Response updateGcsDraftScaleScores = iGcsDraftScaleScoreDao.inactiveSavedDraftScoreList(getGcsDraftScaleScores.getListObject());
            }
//            saveAsDraftGcsScaleScore(gcsScaleScoreDTO);
            saveNursingScaleScore(gcsScaleScoreDTO);

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response getGcsScaleScoreById(Integer unitId, Integer organizationId, Integer admissionId) {
        try {
            return iGcsScaleScoreDao.getGcsScaleScoreById(unitId, organizationId, admissionId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response saveAsDraftGcsScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO) {

        try {
            Response getGcsDraftScaleScores = iGcsDraftScaleScoreDao.getAllGcsDraftScaleScores(gcsScaleScoreDTO.getUnitId(),
                    gcsScaleScoreDTO.getOrganizationId(), gcsScaleScoreDTO.getAdmissionId());

            gcsScaleScoreDTO.setSaveAsDraft('Y');

            if (getGcsDraftScaleScores.getListObject().size() > 0 && getGcsDraftScaleScores.getListObject() != null) {
                Response updateGcsDraftScaleScores = iGcsDraftScaleScoreDao.inactiveSavedDraftScoreList(getGcsDraftScaleScores.getListObject());
                iGcsDraftScaleScoreDao.saveAsDraftGcsScaleScore(gcsScaleScoreDTO);
            } else {
                iGcsDraftScaleScoreDao.saveAsDraftGcsScaleScore(gcsScaleScoreDTO);
            }
            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response getAllAsDraftGcsScaleScore(Integer unitId, Integer organizationId, Integer admissionId) {
        try {
            return iGcsDraftScaleScoreDao.getAllGcsDraftScaleScores(unitId, organizationId, admissionId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response saveApacheScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO) {
        try {
            iApacheScaleScoreDao.saveApacheScaleScore(gcsScaleScoreDTO);

            Response getGcsDraftScaleScores = iApacheDraftScaleScoreDao.getAllApacheDraftScaleScores(gcsScaleScoreDTO.getUnitId(),
                    gcsScaleScoreDTO.getOrganizationId(), gcsScaleScoreDTO.getAdmissionId());

            if (getGcsDraftScaleScores.getListObject().size() > 0 && getGcsDraftScaleScores.getListObject() != null) {
                Response updateGcsDraftScaleScores = iApacheDraftScaleScoreDao.inactiveSavedApacheDraftScoreList(getGcsDraftScaleScores.getListObject());
            }
//            saveAsDraftApacheScaleScore(gcsScaleScoreDTO);
            saveNursingScaleScore(gcsScaleScoreDTO);

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response getApacheScaleScoreById(Integer unitId, Integer organizationId, Integer admissionId) {
        try {
            return iApacheScaleScoreDao.getApacheScaleScoreById(unitId, organizationId, admissionId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response saveAsDraftApacheScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO) {
        try {
            Response getGcsDraftScaleScores = iApacheDraftScaleScoreDao.getAllApacheDraftScaleScores(gcsScaleScoreDTO.getUnitId(),
                    gcsScaleScoreDTO.getOrganizationId(), gcsScaleScoreDTO.getAdmissionId());

            gcsScaleScoreDTO.setSaveAsDraft('Y');

            if (getGcsDraftScaleScores.getListObject().size() > 0 && getGcsDraftScaleScores.getListObject() != null) {
                Response updateGcsDraftScaleScores = iApacheDraftScaleScoreDao.inactiveSavedApacheDraftScoreList(getGcsDraftScaleScores.getListObject());
                iApacheDraftScaleScoreDao.saveAsDraftApacheScaleScore(gcsScaleScoreDTO);
            } else {
                iApacheDraftScaleScoreDao.saveAsDraftApacheScaleScore(gcsScaleScoreDTO);
            }
            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response getAllAsDraftApacheScaleScore(Integer unitId, Integer organizationId, Integer admissionId) {
        try {
            return iApacheDraftScaleScoreDao.getAllApacheDraftScaleScores(unitId, organizationId, admissionId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response saveSofaScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO) {
        try {
            iSofaScaleScoreDao.saveSofaScaleScore(gcsScaleScoreDTO);

            Response getSofaDraftScaleScores = iSofaDraftScaleScoreDao.getAllAsDraftSofaScaleScore(gcsScaleScoreDTO.getUnitId(),
                    gcsScaleScoreDTO.getOrganizationId(), gcsScaleScoreDTO.getAdmissionId());

            if (getSofaDraftScaleScores.getListObject().size() > 0 && getSofaDraftScaleScores.getListObject() != null) {
                Response updateGcsDraftScaleScores = iSofaDraftScaleScoreDao.inactiveSavedSofaDraftScoreList(getSofaDraftScaleScores.getListObject());
            }
//            saveAsDraftSofaScaleScore(gcsScaleScoreDTO);
            saveNursingScaleScore(gcsScaleScoreDTO);

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response getSofaScaleScoreById(Integer unitId, Integer organizationId, Integer admissionId) {
        try {
            return iSofaScaleScoreDao.getSofaScaleScoreById(unitId, organizationId, admissionId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response saveAsDraftSofaScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO) {
        try {
            Response getSofaDraftScaleScores = iSofaDraftScaleScoreDao.getAllAsDraftSofaScaleScore(gcsScaleScoreDTO.getUnitId(),
                    gcsScaleScoreDTO.getOrganizationId(), gcsScaleScoreDTO.getAdmissionId());

            gcsScaleScoreDTO.setSaveAsDraft('Y');

            if (getSofaDraftScaleScores.getListObject().size() > 0 && getSofaDraftScaleScores.getListObject() != null) {
                Response updateGcsDraftScaleScores = iSofaDraftScaleScoreDao.inactiveSavedSofaDraftScoreList(getSofaDraftScaleScores.getListObject());
                iSofaDraftScaleScoreDao.saveAsDraftSofaScaleScore(gcsScaleScoreDTO);
            } else {
                iSofaDraftScaleScoreDao.saveAsDraftSofaScaleScore(gcsScaleScoreDTO);
            }
            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response getAllAsDraftSofaScaleScore(Integer unitId, Integer organizationId, Integer admissionId) {
        try {
            return iSofaDraftScaleScoreDao.getAllAsDraftSofaScaleScore(unitId, organizationId, admissionId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response saveBradenScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO) {
        try {
            iBradenScaleScoreDao.saveBradenScaleScore(gcsScaleScoreDTO);

            Response getBradenDraftScaleScores = iBradenDraftScaleScoreDao.getAllBradenDraftScaleScores(gcsScaleScoreDTO.getUnitId(),
                    gcsScaleScoreDTO.getOrganizationId(), gcsScaleScoreDTO.getAdmissionId());

            if (getBradenDraftScaleScores.getListObject().size() > 0 && getBradenDraftScaleScores.getListObject() != null) {
                Response updateGcsDraftScaleScores = iBradenDraftScaleScoreDao.inactiveSavedDraftScoreList(getBradenDraftScaleScores.getListObject());
            }
//            saveAsDraftBradenScaleScore(gcsScaleScoreDTO);
            saveNursingScaleScore(gcsScaleScoreDTO);

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response getBradenScaleScoreById(Integer unitId, Integer organizationId, Integer admissionId) {
        try {
            return iBradenScaleScoreDao.getBradenScaleScoreById(unitId, organizationId, admissionId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response saveAsDraftBradenScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO) {
        try {
            Response getBradenDraftScaleScores = iBradenDraftScaleScoreDao.getAllBradenDraftScaleScores(gcsScaleScoreDTO.getUnitId(),
                    gcsScaleScoreDTO.getOrganizationId(), gcsScaleScoreDTO.getAdmissionId());

            gcsScaleScoreDTO.setSaveAsDraft('Y');

            if (getBradenDraftScaleScores.getListObject().size() > 0 && getBradenDraftScaleScores.getListObject() != null) {
                Response updateGcsDraftScaleScores = iBradenDraftScaleScoreDao.inactiveSavedDraftScoreList(getBradenDraftScaleScores.getListObject());
                iBradenDraftScaleScoreDao.saveAsDraftBradenScaleScore(gcsScaleScoreDTO);
            } else {
                iBradenDraftScaleScoreDao.saveAsDraftBradenScaleScore(gcsScaleScoreDTO);
            }
            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response getAllAsDraftBradenScaleScore(Integer unitId, Integer organizationId, Integer admissionId) {
        try {
            return iBradenDraftScaleScoreDao.getAllBradenDraftScaleScores(unitId, organizationId, admissionId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response saveNpuapScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO) {
        try {
            iNpuapScaleScoreDao.saveNpuapScaleScore(gcsScaleScoreDTO);

//            Response getBradenDraftScaleScores = iBradenDraftScaleScoreDao.getAllBradenDraftScaleScores(gcsScaleScoreDTO.getUnitId(),
//                    gcsScaleScoreDTO.getOrganizationId(), gcsScaleScoreDTO.getAdmissionId());
//
//            if (getBradenDraftScaleScores.getListObject().size() > 0 && getBradenDraftScaleScores.getListObject() != null) {
//                Response updateGcsDraftScaleScores = iBradenDraftScaleScoreDao.inactiveSavedDraftScoreList(getBradenDraftScaleScores.getListObject());
//            }
////            saveAsDraftBradenScaleScore(gcsScaleScoreDTO);
            saveNursingScaleScore(gcsScaleScoreDTO);

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Transactional
    public Response saveDraftedBishopScore(BishopScaleScoreDTO bishopScaleScoreDTO) {
        try {

            Response getBishopDraftScaleScores = iBishopScaleScoreDao.getAllBishopDraftScaleScores(bishopScaleScoreDTO.getUnitId(),
                    bishopScaleScoreDTO.getOrganizationId(), bishopScaleScoreDTO.getAdmissionId(),  bishopScaleScoreDTO.getEncounterId());
            List<BishopScaleScoreDTO> getAllBishopDraftedScaleScores = getBishopDraftScaleScores.getListObject();

            if(getBishopDraftScaleScores.getListObject()!= null && getBishopDraftScaleScores.getListObject().size()>0){
                for(BishopScaleScoreDTO dto : getAllBishopDraftedScaleScores){
                    Response updateBishopScaleScores = iBishopScaleScoreDao.updateBishopScaleScores(dto.getBishopScoreId(), bishopScaleScoreDTO);

                }
            }
            else{
                iBishopScaleScoreDao.saveDraftedBishopScore(bishopScaleScoreDTO);
            }
            if(bishopScaleScoreDTO.getFinalSave().equals('Y')){
                GcsScaleScoreDTO gcsScaleScoreDTO = new GcsScaleScoreDTO();
                gcsScaleScoreDTO.setEncounterId(bishopScaleScoreDTO.getEncounterId());
                gcsScaleScoreDTO.setAdmissionId(bishopScaleScoreDTO.getAdmissionId());
                gcsScaleScoreDTO.setNursingScoreName(bishopScaleScoreDTO.getNursingScoreName());
                gcsScaleScoreDTO.setTotalGcsScore(bishopScaleScoreDTO.getTotalBishopScore().toString());
                gcsScaleScoreDTO.setSaveAsDraft('N');
                gcsScaleScoreDTO.setTabNumber(bishopScaleScoreDTO.getTabNumber());
                gcsScaleScoreDTO.setUnitId(bishopScaleScoreDTO.getUnitId());
                gcsScaleScoreDTO.setOrganizationId(bishopScaleScoreDTO.getOrganizationId());
                gcsScaleScoreDTO.setStatus('A');
                gcsScaleScoreDTO.setCreatedBy(bishopScaleScoreDTO.getCreatedBy());
                gcsScaleScoreDTO.setUpdatedBy(bishopScaleScoreDTO.getUpdatedBy());
                gcsScaleScoreDTO.setCreatedDate(bishopScaleScoreDTO.getCreatedDate());
                gcsScaleScoreDTO.setUpdatedDate(bishopScaleScoreDTO.getUpdatedDate());
//                Response inactiveDraftedBishopScores = iBishopScaleScoreDao.inactiveDraftedBishopScores(getBishopDraftScaleScores.getListObject());

                saveNursingScaleScore(gcsScaleScoreDTO);
            }
            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Transactional
    public Response getDraftedBishopScore(Integer unitId, Integer organizationId, Integer admissionId, Integer encounterId) throws ApplicationException {
        try {
            return iBishopScaleScoreDao.getAllBishopDraftScaleScores(unitId, organizationId, admissionId, encounterId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Transactional
    public Response saveAldreteScore(AldreteScaleScoreDto aldreteScaleScoreDto) {
        try {

            Response getAldreteScaleScores = iAldreteScaleScoreDao.getAldreteDraftScaleScores(aldreteScaleScoreDto.getUnitId(),
                    aldreteScaleScoreDto.getOrganizationId(), aldreteScaleScoreDto.getAdmissionId(),  aldreteScaleScoreDto.getEncounterId());

            List<AldreteScaleScoreDto> getAllAldreteDraftedScaleScores = getAldreteScaleScores.getListObject();

            if(getAllAldreteDraftedScaleScores != null && !getAllAldreteDraftedScaleScores.isEmpty()){
                for(AldreteScaleScoreDto dto : getAllAldreteDraftedScaleScores){
                    Response updateAldreteScaleScores = iAldreteScaleScoreDao.updateAldreteScaleScores(dto.getAldreteScoreId(), aldreteScaleScoreDto);
                }
            }
            else{
                iAldreteScaleScoreDao.saveAldreteScore(aldreteScaleScoreDto);
            }
            if(aldreteScaleScoreDto.getFinalSave().equals('Y')){
                GcsScaleScoreDTO gcsScaleScoreDTO = new GcsScaleScoreDTO();
                gcsScaleScoreDTO.setEncounterId(aldreteScaleScoreDto.getEncounterId());
                gcsScaleScoreDTO.setAdmissionId(aldreteScaleScoreDto.getAdmissionId());
                gcsScaleScoreDTO.setNursingScoreName(aldreteScaleScoreDto.getNursingScoreName());
                gcsScaleScoreDTO.setTotalGcsScore(aldreteScaleScoreDto.getTotalAldreteScore().toString());
                gcsScaleScoreDTO.setSaveAsDraft('N');
                gcsScaleScoreDTO.setTabNumber(aldreteScaleScoreDto.getTabNumber());
                gcsScaleScoreDTO.setUnitId(aldreteScaleScoreDto.getUnitId());
                gcsScaleScoreDTO.setOrganizationId(aldreteScaleScoreDto.getOrganizationId());
                gcsScaleScoreDTO.setStatus('A');
                gcsScaleScoreDTO.setCreatedBy(aldreteScaleScoreDto.getCreatedBy());
                gcsScaleScoreDTO.setUpdatedBy(aldreteScaleScoreDto.getUpdatedBy());
                gcsScaleScoreDTO.setCreatedDate(aldreteScaleScoreDto.getCreatedDate());
                gcsScaleScoreDTO.setUpdatedDate(aldreteScaleScoreDto.getUpdatedDate());
//                Response inactiveDraftedAldreteScores = iAldreteScaleScoreDao.inactiveDraftedAldreteScores(getAllAldreteDraftedScaleScores);
                saveNursingScaleScore(gcsScaleScoreDTO);
            }
            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Transactional
    public Response getDraftedAldreteScore(Integer unitId, Integer organizationId, Integer admissionId, Integer encounterId) throws ApplicationException {
        try {
            return iAldreteScaleScoreDao.getAldreteDraftScaleScores(unitId, organizationId, admissionId, encounterId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Transactional
    public Response saveApgarScore(APGARScaleScoreDto apgarScaleScoreDto) {
        try {

            Response getApgarDraftScaleScores = iAPGARScaleScoreDao.getAPGARDraftScaleScores(apgarScaleScoreDto.getUnitId(),
                    apgarScaleScoreDto.getOrganizationId(), apgarScaleScoreDto.getAdmissionId(),  apgarScaleScoreDto.getEncounterId());

            List<APGARScaleScoreDto> getAllAPGARDraftedScaleScores = getApgarDraftScaleScores.getListObject();

            if(getAllAPGARDraftedScaleScores != null && !getAllAPGARDraftedScaleScores.isEmpty()){
                for(APGARScaleScoreDto dto : getAllAPGARDraftedScaleScores){
                    Response updateAldreteScaleScores = iAPGARScaleScoreDao.updateAldreteScaleScores(dto.getApgarScoreId(), apgarScaleScoreDto);
                }
            }
            else{
                iAPGARScaleScoreDao.saveApgarScore(apgarScaleScoreDto);
            }

            if(apgarScaleScoreDto.getFinalSave().equals('Y')){
                GcsScaleScoreDTO gcsScaleScoreDTO = new GcsScaleScoreDTO();
                gcsScaleScoreDTO.setEncounterId(apgarScaleScoreDto.getEncounterId());
                gcsScaleScoreDTO.setAdmissionId(apgarScaleScoreDto.getAdmissionId());
                gcsScaleScoreDTO.setNursingScoreName(apgarScaleScoreDto.getNursingScoreName());
                gcsScaleScoreDTO.setTotalGcsScore(apgarScaleScoreDto.getTotalApgarScore().toString());
                gcsScaleScoreDTO.setSaveAsDraft('N');
                gcsScaleScoreDTO.setTabNumber(apgarScaleScoreDto.getTabNumber());
                gcsScaleScoreDTO.setUnitId(apgarScaleScoreDto.getUnitId());
                gcsScaleScoreDTO.setOrganizationId(apgarScaleScoreDto.getOrganizationId());
                gcsScaleScoreDTO.setStatus('A');
                gcsScaleScoreDTO.setCreatedBy(apgarScaleScoreDto.getCreatedBy());
                gcsScaleScoreDTO.setUpdatedBy(apgarScaleScoreDto.getUpdatedBy());
                gcsScaleScoreDTO.setCreatedDate(apgarScaleScoreDto.getCreatedDate());
                gcsScaleScoreDTO.setUpdatedDate(apgarScaleScoreDto.getUpdatedDate());

                saveNursingScaleScore(gcsScaleScoreDTO);
            }
            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Transactional
    public Response getAPGARDraftScaleScores(Integer unitId, Integer organizationId, Integer admissionId, Integer encounterId) throws ApplicationException {
        try {
            return iAPGARScaleScoreDao.getAPGARDraftScaleScores(unitId, organizationId, admissionId, encounterId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Transactional
    public Response saveMallampattiClass(MallamapattiClassDto mallamapattiClassDto) throws ApplicationException {
        try {

            Response getApgarDraftScaleScores = iMallamapattiClassDao.getDraftedMallampattiClass(mallamapattiClassDto.getUnitId(),
                    mallamapattiClassDto.getOrganizationId(), mallamapattiClassDto.getAdmissionId(),  mallamapattiClassDto.getEncounterId());
            if(getApgarDraftScaleScores.getListObject()!= null && getApgarDraftScaleScores.getListObject().size()>0){
				Response updateMallampattiClass = iMallamapattiClassDao.inactiveDraftedMallampattiClass(getApgarDraftScaleScores.getListObject());
                iMallamapattiClassDao.saveMallampattiClass(mallamapattiClassDto);
            }
            else{
                iMallamapattiClassDao.saveMallampattiClass(mallamapattiClassDto);
            }
            if(mallamapattiClassDto.getFinalSave().equals('Y')){
                String className = "";
                if(mallamapattiClassDto.getTotalMallampattiScore()!= null && mallamapattiClassDto.getTotalMallampattiScore() == 1){
                    className = "CLASS-I";
                }
                if(mallamapattiClassDto.getTotalMallampattiScore()!= null && mallamapattiClassDto.getTotalMallampattiScore() == 2){
                    className = "CLASS-II";
                }
                if(mallamapattiClassDto.getTotalMallampattiScore()!= null && mallamapattiClassDto.getTotalMallampattiScore() == 3){
                    className = "CLASS-III";
                }
                if(mallamapattiClassDto.getTotalMallampattiScore()!= null && mallamapattiClassDto.getTotalMallampattiScore() == 4){
                    className = "CLASS-IV";
                }

                GcsScaleScoreDTO gcsScaleScoreDTO = new GcsScaleScoreDTO();
                gcsScaleScoreDTO.setEncounterId(mallamapattiClassDto.getEncounterId());
                gcsScaleScoreDTO.setAdmissionId(mallamapattiClassDto.getAdmissionId());
                gcsScaleScoreDTO.setNursingScoreName(mallamapattiClassDto.getNursingScoreName());
                gcsScaleScoreDTO.setTotalGcsScore(className);
                gcsScaleScoreDTO.setSaveAsDraft('N');
                gcsScaleScoreDTO.setTabNumber(mallamapattiClassDto.getTabNumber());
                gcsScaleScoreDTO.setUnitId(mallamapattiClassDto.getUnitId());
                gcsScaleScoreDTO.setOrganizationId(mallamapattiClassDto.getOrganizationId());
                gcsScaleScoreDTO.setStatus('A');
                gcsScaleScoreDTO.setCreatedBy(mallamapattiClassDto.getCreatedBy());
                gcsScaleScoreDTO.setUpdatedBy(mallamapattiClassDto.getUpdatedBy());
                gcsScaleScoreDTO.setCreatedDate(mallamapattiClassDto.getCreatedDate());
                gcsScaleScoreDTO.setUpdatedDate(mallamapattiClassDto.getUpdatedDate());

                saveNursingScaleScore(gcsScaleScoreDTO);
            }
            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Transactional
    public Response getDraftedMallampattiClass(Integer unitId, Integer organizationId, Integer admissionId, Integer encounterId) throws ApplicationException {
        try {
            return iMallamapattiClassDao.getDraftedMallampattiClass(unitId, organizationId, admissionId, encounterId);
        } catch (Exception e) {
            throw e;
        }
    }
    
    

	@Override
	 @Transactional
	public Response saveFallRiskScore(FallRiskScoreDto dto) throws ApplicationException {

        try {

            
        		iFallRiskScoreDao.saveFallRiskScore(dto);
            
                GcsScaleScoreDTO gcsScaleScoreDTO = new GcsScaleScoreDTO();
                gcsScaleScoreDTO.setEncounterId(dto.getEncounterId());
                gcsScaleScoreDTO.setAdmissionId(dto.getAdmissionId());
                gcsScaleScoreDTO.setNursingScoreName(dto.getNursingScoreName());
                gcsScaleScoreDTO.setTotalGcsScore(dto.getTotalFallRiskScore().toString());
                gcsScaleScoreDTO.setSaveAsDraft('N');
                gcsScaleScoreDTO.setTabNumber(dto.getTabNumber());
                gcsScaleScoreDTO.setUnitId(dto.getUnitId());
                gcsScaleScoreDTO.setOrganizationId(dto.getOrganizationId());
                gcsScaleScoreDTO.setStatus('A');
                gcsScaleScoreDTO.setCreatedBy(dto.getCreatedBy());
                gcsScaleScoreDTO.setUpdatedBy(dto.getUpdatedBy());
                gcsScaleScoreDTO.setCreatedDate(dto.getCreatedDate());
                gcsScaleScoreDTO.setUpdatedDate(dto.getUpdatedDate());

                saveNursingScaleScore(gcsScaleScoreDTO);
            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

        } catch (Exception e) {
            throw e;
        }
    
	}

    @Override
    public Response getSpecialityMappedNursingScoreTabs(Integer unitId, Integer organizationId, Integer doctorId, Integer specialityId) {
        try {
            return iNursingScaleScoreDao.getSpecialityMappedNursingScoreTabs(unitId, organizationId, doctorId, specialityId);

        } catch (Exception e) {
            throw e;
        }
    }
}
