package com.param.lis.global.dao;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.param.entity.lis.global.AntibioticOrganismMpprMaster;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.dto.AntibioticOrganismMpprMasterDto;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AntibioticOrganismMpprMasterDaoImpl extends GenericDao<AntibioticOrganismMpprMaster, Integer>
		implements IAntibioticOrganismMpprMasterDao, ICommonConstants, IError {

	public AntibioticOrganismMpprMasterDaoImpl() {
		super(AntibioticOrganismMpprMaster.class);
	}

	@Autowired
	private DozerBeanMapper mapper;

	@Override
	public Response addAntibioticOrganismMpprMaster(AntibioticOrganismMpprMasterDto antibioticAdditionMasterDto)
			throws ApplicationException {
		try {
			if(antibioticAdditionMasterDto.getAntiboiticOrganismMpprId()!=null){
				AntibioticOrganismMpprMaster antibioticOrganismMpprMaster = findById(antibioticAdditionMasterDto.getAntiboiticOrganismMpprId());
				antibioticOrganismMpprMaster.setUpdatedBy(antibioticAdditionMasterDto.getUpdatedBy());
				antibioticOrganismMpprMaster.setUpdatedDate(antibioticAdditionMasterDto.getUpdatedDate());
				antibioticOrganismMpprMaster.setOrganismId(antibioticAdditionMasterDto.getOrganismId());
				antibioticOrganismMpprMaster.setAntiboiticId(antibioticAdditionMasterDto.getAntiboiticId());
				antibioticOrganismMpprMaster.setAntibioticGroupId(antibioticAdditionMasterDto.getAntiboiticGroupId());
				if(antibioticAdditionMasterDto.getStatus().equals('I')) {
					antibioticOrganismMpprMaster.setIsDeleted('Y');
				}else {
					antibioticOrganismMpprMaster.setIsDeleted('N');
				}
				antibioticOrganismMpprMaster.setAntiboiticOrganismMpprId(antibioticAdditionMasterDto.getAntiboiticOrganismMpprId());
				update(antibioticOrganismMpprMaster);

			}else{
				AntibioticOrganismMpprMaster antibioticOrganismMpprMaster = new AntibioticOrganismMpprMaster();
				antibioticOrganismMpprMaster.setStatus(antibioticAdditionMasterDto.getStatus());
				antibioticOrganismMpprMaster.setOrgId(antibioticAdditionMasterDto.getOrgId());
				antibioticOrganismMpprMaster.setUpdatedBy(antibioticAdditionMasterDto.getUpdatedBy());
				antibioticOrganismMpprMaster.setCreatedBy(antibioticAdditionMasterDto.getCreatedBy());
				antibioticOrganismMpprMaster.setCreatedDate(antibioticOrganismMpprMaster.getCreatedDate());
				antibioticOrganismMpprMaster.setOrganismId(antibioticAdditionMasterDto.getOrganismId());
				antibioticOrganismMpprMaster.setAntiboiticId(antibioticAdditionMasterDto.getAntiboiticId());
				antibioticOrganismMpprMaster.setAntibioticGroupId(antibioticAdditionMasterDto.getAntiboiticGroupId());
				antibioticOrganismMpprMaster.setUpdatedBy(antibioticAdditionMasterDto.getUpdatedBy());
				antibioticOrganismMpprMaster.setUpdatedDate(antibioticAdditionMasterDto.getUpdatedDate());
				if(antibioticAdditionMasterDto.getStatus().equals('I')) {
					antibioticOrganismMpprMaster.setIsDeleted('Y');
				}else {
					antibioticOrganismMpprMaster.setIsDeleted('N');
				}
				save(antibioticOrganismMpprMaster);
			}
			return new Response<>(SUCCESS, SUCCESS_200, ANTIBIOTICS_ORGANISM_ADD_SUCC, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response listAntibioticOrganismMpprMaster(Integer orgId, Integer offset, Integer recordPerPage)
			throws ApplicationException {
		try {
			List<AntibioticOrganismMpprMasterDto> listAntibioticAddtionClassMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAGINATED_ANTIBIOTIC_MAPPER_ORGANISM_MASTER_LIST").setParameter("orgId", orgId)
					.setFirstResult(offset != null ? offset : 0)
					.setMaxResults(recordPerPage != null ? recordPerPage : 10)
					.setResultTransformer(Transformers.aliasToBean(AntibioticOrganismMpprMasterDto.class)).list();
			return new Response(SUCCESS, null, null, listAntibioticAddtionClassMasterDto, null);

		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getToTalAntibioticOrganismMpprMaster(Integer orgId) throws ApplicationException {
		try {
			BigInteger antibioticAddtionClassMasterCount = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TOTAL_ANTIBIOTIC_MAPPER_ORGANISM_RECORD").setParameter("orgId", orgId)
					.uniqueResult();

			if (antibioticAddtionClassMasterCount.compareTo(BigInteger.ZERO) == 1) {
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, antibioticAddtionClassMasterCount);
			} else {
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, BigInteger.ZERO);
			}

		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response activateInactivateAntibioticOrganismMpprMaster(Integer orgId, Integer organismId,
			Character status) throws ApplicationException {

		try {
			if (organismId != 0) {

				Integer result = sessionFactory.getCurrentSession()
						.getNamedQuery("ACTIVE_INACTIVE_ANTIBIOTIC_MAPPER_ORGANISM").setParameter("orgId", orgId)
						.setParameter("status", status).setParameter("organismId", organismId)
						.executeUpdate();
				if (result > 0) {
					if (status.equals(ACTIVE)) {
						return new Response(SUCCESS, SUCCESS_200, ANTIBIOTICS_ORGANISM_ACTIVATE_SUCC, null, null);
					} else if (status.equals(INACTIVE)) {
						return new Response(SUCCESS, SUCCESS_200, ANTIBIOTICS_ORGANISM_INACTIVATE_SUCC, null, null);
					}
				} else {
					return new Response(ERROR, ERR_500, ANTIBIOTICS_ORGANISM_CORDS_NOT_FOUND, null, null);
				}

			} else {
				return new Response(ERROR, ERR_500, ANTIBIOTICS_ORGANISM_CORDS_NOT_FOUND, null, null);
			}

		} catch (Exception e) {
			
			throw e;
		}
		return new Response(ERROR, ERR_500, ANTIBIOTICS_ORGANISM_CORDS_NOT_FOUND, null, null);
	}

	@Override
	public Response updateAntibioticOrganismMpprMaster(
			List<AntibioticOrganismMpprMasterDto> listAntibioticOrganismMpprMasterDto) throws ApplicationException {
		try {

			Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_ANTIBIOTIC_MAPPER_ORGANISM")
					.setParameter("isDeleted", 'Y')
					.setParameter("orgId", listAntibioticOrganismMpprMasterDto.getFirst().getOrgId())
					.setParameter("organismId", listAntibioticOrganismMpprMasterDto.getFirst().getOrganismId())
					.executeUpdate();
			if (result > 0) {
				for (Iterator iterator = listAntibioticOrganismMpprMasterDto.iterator(); iterator.hasNext();) {
					AntibioticOrganismMpprMasterDto listAntibioticAdditionMaster = (AntibioticOrganismMpprMasterDto) iterator
							.next();
					AntibioticOrganismMpprMaster antibioticOrganismMpprMaster = mapper.map(listAntibioticAdditionMaster,
							AntibioticOrganismMpprMaster.class, "AntibioticOrganismMpprMasterDtoTOAntibioticOrganismMpprMaster");
					antibioticOrganismMpprMaster.setIsDeleted('N');
					antibioticOrganismMpprMaster.setCreatedBy(antibioticOrganismMpprMaster.getUpdatedBy());
					antibioticOrganismMpprMaster.setCreatedDate(antibioticOrganismMpprMaster.getUpdatedDate());
					save(antibioticOrganismMpprMaster);
				}
				return new Response(SUCCESS, SUCCESS_200, ANTIBIOTIC_UPDATE_SUCC, null, null);
			} else {
				return new Response(ERROR, ERR_500, ANTIBIOTICS_ORGANISM_UPDATE_FAIL, null, null);
			}
		} catch (

		Exception e) {
			
			throw e;
		}

	}

	@Override
	public Response checkAntibioticOrganismMpprMasterAvaiable(AntibioticOrganismMpprMasterDto antibioticAdditionMasterDto)
			throws ApplicationException {
		try {
			List<AntibioticOrganismMpprMasterDto> listAntibioticOrganismMpprMastersessionFactory = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ANTIBIOTIC_MAPPER_ORGANISM_CODE")
					.setParameter("orgId", antibioticAdditionMasterDto.getOrgId())
					.setParameter("organismId", antibioticAdditionMasterDto.getOrganismId())
					.setResultTransformer(Transformers.aliasToBean(AntibioticOrganismMpprMasterDto.class)).list();
			return new Response(SUCCESS, null, null, listAntibioticOrganismMpprMastersessionFactory, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getAntibioticOrganismMpprMasterById(Integer orgId, Integer organismId)
			throws ApplicationException {
		try {
			List<AntibioticOrganismMpprMasterDto> listAntibioticOrganismMpprMaster = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ANTIBIOTIC_MAPPER_BY_ORGANISM_ID").setParameter("orgId", orgId)
					.setParameter("organismId", organismId)
					.setResultTransformer(Transformers.aliasToBean(AntibioticOrganismMpprMasterDto.class)).list();

			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listAntibioticOrganismMpprMaster, null);

		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getAntibioticByOrganismId(Integer organismId) throws ApplicationException {
		try {
			List<AntibioticOrganismMpprMasterDto> listAntibioticOrganismMpprMaster = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ANTIBIOTIC_BY_ORGANISM_ID")
					.setParameter("organismId", organismId)
					.setResultTransformer(Transformers.aliasToBean(AntibioticOrganismMpprMasterDto.class)).list();

			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listAntibioticOrganismMpprMaster, null);

		} catch (Exception e) {
			
			throw e;
		}
	}

}
