package com.param.global.dao;

import java.io.IOException;

import javax.transaction.Transactional;

import com.param.entity.model.master.DifferentTypesScreenImageDTO;
import com.param.global.common.GlobalCommonDateUtils;
import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.param.entity.model.master.DifferentTypesScreenImage;
import com.param.global.common.DocUploadUtility;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.org.dao.IGlobalOrganisationMasterDao;
import com.param.global.org.dto.OrganizationMasterDto;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class DifferentTypesScreenImageDaoImpl extends GenericDao<DifferentTypesScreenImage, Integer>
        implements IDifferentTypesScreenImageDao, ICommonConstants {

    public DifferentTypesScreenImageDaoImpl() {
        super(DifferentTypesScreenImage.class);
    }

    @Autowired
    DocUploadUtility docUploadUtility;

    @Autowired
    IGlobalOrganisationMasterDao iGlobalOrganisationMasterDao;

    @Override
    public Response saveScreenImage(DifferentTypesScreenImageDTO differentTypesScreenImageDTO, MultipartFile inputFile) throws Exception {
        try {

            String returnMessage = "";

            Integer bucketType = (Integer) sessionFactory.getCurrentSession()
                    .createSQLQuery("select bucket_flag from t_unit_application_billing_configuration where unit_id = :unitId and organization_id = :organizationId")
                    .setParameter("unitId", differentTypesScreenImageDTO.getUnitId()) // Set parameter for unitId
                    .setParameter("organizationId", differentTypesScreenImageDTO.getOrganizationId()) // Set parameter for organizationId
                    .uniqueResult();
            // Bug fix, docs/ANGULAR_PENDING_FINDINGS.md entry 139 -- bucket_flag can be NULL
            // (confirmed live: t_unit_application_billing_configuration's row for org=1/unit=1
            // has no bucket_flag set), and `bucketType == 1` auto-unboxes a null Integer into an
            // NPE. Every other call site of this exact pattern in DocUploadUtility.java already
            // guards this; this DAO was the sole exception.
            if (bucketType == null) {
                bucketType = 1;
            }
            if (differentTypesScreenImageDTO.getOrganizationId() > 0 && !inputFile.isEmpty() && bucketType ==1) {

                String screenImageURL = uploadScreenImageAndGetURL(differentTypesScreenImageDTO, inputFile , bucketType);

                DifferentTypesScreenImage differentTypesScreenImageAdd = new DifferentTypesScreenImage();
                DifferentTypesScreenImage differentTypesScreenImageUpdate = (DifferentTypesScreenImage) sessionFactory.getCurrentSession()
                        .getNamedQuery("GET_IMAGE_BY_IMAGE_TYPE_ID")
                        .setParameter("imageTypeId", differentTypesScreenImageDTO.getImageTypeId())
                        .setParameter("organizationId", differentTypesScreenImageDTO.getOrganizationId())
                        .setCharacter("status", differentTypesScreenImageDTO.getStatus())
                        .setResultTransformer(Transformers.aliasToBean(DifferentTypesScreenImage.class)).uniqueResult();

                if (differentTypesScreenImageUpdate != null && differentTypesScreenImageUpdate.getImageTypeId() > 0) {

                    differentTypesScreenImageUpdate.setUpdatedBy(differentTypesScreenImageDTO.getUpdatedBy());
                    differentTypesScreenImageUpdate.setLogoName((differentTypesScreenImageDTO.getLogoName()!=null)?differentTypesScreenImageDTO.getLogoName():null);
                    differentTypesScreenImageUpdate.setImageURL((screenImageURL!=null)?screenImageURL:null);
                    differentTypesScreenImageUpdate.setUpdatedDate(
                            GlobalCommonDateUtils.getDate(differentTypesScreenImageDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
                    differentTypesScreenImageUpdate.setStatus('I');

                    update(differentTypesScreenImageUpdate);
                    returnMessage = "updated";

                    save(insertDataInDifferentTypesScreen(inputFile, screenImageURL, differentTypesScreenImageAdd, differentTypesScreenImageDTO));
                    returnMessage = returnMessage + " and inserted";
                } else {

                    save(insertDataInDifferentTypesScreen(inputFile, screenImageURL, differentTypesScreenImageAdd, differentTypesScreenImageDTO));
                    returnMessage = "inserted";
                }
                return new Response(SUCCESS, SUCCESS_CODE, returnMessage, null, null, null);
            }
        } catch (Exception e) {
            throw e;
        }

        return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
    }

    @Override
    public Response saveScreenApplicationName(DifferentTypesScreenImageDTO differentTypesScreenImageDTO) throws Exception {
        try {

            String returnMessage = "";

                DifferentTypesScreenImage differentTypesScreenImageAdd = new DifferentTypesScreenImage();
                DifferentTypesScreenImage differentTypesScreenImageUpdate = (DifferentTypesScreenImage) sessionFactory.getCurrentSession()
                        .getNamedQuery("GET_IMAGE_BY_IMAGE_TYPE_ID")
                        .setParameter("imageTypeId", differentTypesScreenImageDTO.getImageTypeId())
                        .setParameter("organizationId", differentTypesScreenImageDTO.getOrganizationId())
                        .setCharacter("status", differentTypesScreenImageDTO.getStatus())
                        .setResultTransformer(Transformers.aliasToBean(DifferentTypesScreenImage.class)).uniqueResult();

                if (differentTypesScreenImageUpdate != null && differentTypesScreenImageUpdate.getImageTypeId() > 0) {

                    differentTypesScreenImageUpdate.setUpdatedBy(differentTypesScreenImageDTO.getUpdatedBy());
                    differentTypesScreenImageUpdate.setLogoName(differentTypesScreenImageDTO.getLogoName());
                    differentTypesScreenImageUpdate.setUpdatedDate(
                            GlobalCommonDateUtils.getDate(differentTypesScreenImageDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
                    differentTypesScreenImageUpdate.setStatus('I');

                    update(differentTypesScreenImageUpdate);
                    returnMessage = "updated";

                    save(insertDataInDifferentTypesApplicationName(differentTypesScreenImageAdd, differentTypesScreenImageDTO));
                    returnMessage = returnMessage + " and inserted";
                } else {

                    save(insertDataInDifferentTypesApplicationName(differentTypesScreenImageAdd, differentTypesScreenImageDTO));
                    returnMessage = "inserted";
                }
                return new Response(SUCCESS, SUCCESS_CODE, returnMessage, null, null, null);

        } catch (Exception e) {
            throw e;
        }

//        return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
    }

    private String uploadScreenImageAndGetURL(DifferentTypesScreenImageDTO differentTypesScreenImageDTO, MultipartFile inputFile, Integer bucketType) throws Exception {
        Response unitRes = iGlobalOrganisationMasterDao.getOrganiazationFolderPath(differentTypesScreenImageDTO.getOrganizationId());
        OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) unitRes.getObject();
        return docUploadUtility.uploadScreenImage(inputFile, organizationMasterDto, differentTypesScreenImageDTO.getOrganizationId(),
                differentTypesScreenImageDTO.getUnitId(), bucketType); // Assuming bucketType 1 corresponds to the desired bucket type
    }

    public DifferentTypesScreenImage insertDataInDifferentTypesScreen( MultipartFile inputFile, String screenImageURL,
                                                                       DifferentTypesScreenImage differentTypesScreenImageAdd, DifferentTypesScreenImageDTO differentTypesScreenImageDTO) throws IOException {

        differentTypesScreenImageAdd.setImage(inputFile.getBytes());
        differentTypesScreenImageAdd.setImageURL(screenImageURL);
        differentTypesScreenImageAdd.setImageTypeId(differentTypesScreenImageDTO.getImageTypeId());
        differentTypesScreenImageAdd.setCreatedBy(differentTypesScreenImageDTO.getCreatedBy());
        differentTypesScreenImageAdd.setUpdatedBy(differentTypesScreenImageDTO.getUpdatedBy());
        differentTypesScreenImageAdd.setCreatedDate(
                GlobalCommonDateUtils.getDate(differentTypesScreenImageDTO.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
        differentTypesScreenImageAdd.setUpdatedDate(
                GlobalCommonDateUtils.getDate(differentTypesScreenImageDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
        differentTypesScreenImageAdd.setStatus(differentTypesScreenImageDTO.getStatus());
        differentTypesScreenImageAdd.setOrganizationId(differentTypesScreenImageDTO.getOrganizationId());

        return differentTypesScreenImageAdd;
    }

    public DifferentTypesScreenImage insertDataInDifferentTypesApplicationName( DifferentTypesScreenImage differentTypesScreenImageAdd,DifferentTypesScreenImageDTO differentTypesScreenImageDTO) throws IOException {

        differentTypesScreenImageAdd.setImageTypeId(differentTypesScreenImageDTO.getImageTypeId());
        differentTypesScreenImageAdd.setCreatedBy(differentTypesScreenImageDTO.getCreatedBy());
        differentTypesScreenImageAdd.setUpdatedBy(differentTypesScreenImageDTO.getUpdatedBy());
        differentTypesScreenImageAdd.setCreatedDate(
                GlobalCommonDateUtils.getDate(differentTypesScreenImageDTO.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
        differentTypesScreenImageAdd.setUpdatedDate(
                GlobalCommonDateUtils.getDate(differentTypesScreenImageDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
        differentTypesScreenImageAdd.setStatus(differentTypesScreenImageDTO.getStatus());
        differentTypesScreenImageAdd.setOrganizationId(differentTypesScreenImageDTO.getOrganizationId());
        differentTypesScreenImageAdd.setLogoName((differentTypesScreenImageDTO.getLogoName()!=null)?differentTypesScreenImageDTO.getLogoName():null);

        return differentTypesScreenImageAdd;
    }

    @Override
    public Response getScreenImageById(Integer organisationId, Integer imageTypeId) throws Exception {
        try {
            StringBuilder query = new StringBuilder("SELECT differentTypesScreenImage.screen_image_id as \"screenImageId\", " +
                    "differentTypesScreenImage.image_url as \"imageURL\", " +
                    "differentTypesScreenImage.logo_name as \"logoName\", " +
                    "differentTypesScreenImage.image_type_id as \"imageTypeId\", " +
                    "differentTypesScreenImage.created_by as \"createdBy\", " +
                    "TO_CHAR(differentTypesScreenImage.created_date, 'DD/MM/YYYY "
                    + "HH24:MI:SS') as \"createdDate\", " +
                    "differentTypesScreenImage.status as \"status\", " +
                    "differentTypesScreenImage.updated_by as \"updatedBy\", " +
                    "TO_CHAR(differentTypesScreenImage.updated_date, 'DD/MM/YYYY HH24:MI:SS') as \"updatedDate\", " +
                    "differentTypesScreenImage.organization_id as \"organizationId\" " +
                    "FROM public.m_different_types_screen_image differentTypesScreenImage " +
                    "WHERE image_type_id = :imageTypeId " +
                    "AND organization_id = :organizationId " +
                    "AND status = :status ");

            DifferentTypesScreenImageDTO differentTypesScreenImageDTO = (DifferentTypesScreenImageDTO) sessionFactory.getCurrentSession()
                    .createSQLQuery(query.toString())
                    .setParameter("imageTypeId", imageTypeId)
                    .setParameter("organizationId", organisationId)
                    .setParameter("status", 'A')
                    .setResultTransformer(Transformers.aliasToBean(DifferentTypesScreenImageDTO.class))
                    .setMaxResults(1)
                    .uniqueResult();


                return new Response(SUCCESS, SUCCESS_CODE, null, null, differentTypesScreenImageDTO, null);

        } catch (HibernateException he) {
            throw he;
        } catch (Exception e) {
            throw e;
        }

    }


}
