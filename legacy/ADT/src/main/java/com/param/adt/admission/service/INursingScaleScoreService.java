package com.param.adt.admission.service;

import com.param.adt.admission.dto.*;
import com.param.adt.common.Response;
import in.param.exception.ApplicationException;

import javax.transaction.Transactional;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface INursingScaleScoreService {

    public Response saveNursingScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO);

    public Response getAllNursingScaleScore(Integer unitId, Integer organizationId, Integer admissionId, Integer encounterId);

    public Response getAllNursingScoreById(Integer unitId, Integer organizationId, Integer admissionId, Integer tabNumber);

    public Response saveGcsScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO);

    public Response getGcsScaleScoreById(Integer unitId, Integer organizationId, Integer admissionId);

    public Response saveAsDraftGcsScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO);

    public Response getAllAsDraftGcsScaleScore(Integer unitId, Integer organizationId, Integer admissionId);

    public Response saveApacheScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO);

    public Response getApacheScaleScoreById(Integer unitId, Integer organizationId, Integer admissionId);

    public Response saveAsDraftApacheScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO);

    public Response getAllAsDraftApacheScaleScore(Integer unitId, Integer organizationId, Integer admissionId);

    public Response saveSofaScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO);

    public Response getSofaScaleScoreById(Integer unitId, Integer organizationId, Integer admissionId);

    public Response saveAsDraftSofaScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO);

    public Response getAllAsDraftSofaScaleScore(Integer unitId, Integer organizationId, Integer admissionId);

    public Response saveBradenScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO);

    public Response getBradenScaleScoreById(Integer unitId, Integer organizationId, Integer admissionId);

    public Response saveAsDraftBradenScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO);

    public Response getAllAsDraftBradenScaleScore(Integer unitId, Integer organizationId, Integer admissionId);

    public Response saveNpuapScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO);


    Response saveDraftedBishopScore(BishopScaleScoreDTO bishopScaleScoreDTO) throws ApplicationException;

    Response getDraftedBishopScore(Integer unitId, Integer organizationId, Integer admissionId, Integer encounterId) throws ApplicationException;

    Response saveAldreteScore(AldreteScaleScoreDto aldreteScaleScoreDto) throws ApplicationException;

    Response getDraftedAldreteScore(Integer unitId, Integer organizationId, Integer admissionId, Integer encounterId) throws ApplicationException;

    Response saveApgarScore(APGARScaleScoreDto apgarScaleScoreDto) throws ApplicationException;

    Response getAPGARDraftScaleScores(Integer unitId, Integer organizationId, Integer admissionId, Integer encounterId) throws ApplicationException;

    Response saveMallampattiClass(MallamapattiClassDto mallamapattiClassDto) throws ApplicationException;

    Response getDraftedMallampattiClass(Integer unitId, Integer organizationId, Integer admissionId, Integer encounterId) throws ApplicationException;

	public Response saveFallRiskScore(FallRiskScoreDto dto) throws ApplicationException;

    public Response getSpecialityMappedNursingScoreTabs(Integer unitId, Integer organizationId, Integer doctorId, Integer specialityId) throws ApplicationException;
}
