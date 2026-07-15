package com.param.global.dao;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.admission.model.MedicoLeagal;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.AdmissionDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class MedicolegalDaoImpl  extends GenericDao<MedicoLeagal, Integer>
implements ICommonConstants, IMedicolegalDao {
	final static Logger logger = Logger.getLogger(MedicolegalDaoImpl.class);


@Autowired
SessionFactory sessionFactory;

@Autowired
DozerBeanMapper mapper;

public MedicolegalDaoImpl() {
super(MedicoLeagal.class);

}

@Override
public Response updateMedicolegalDetails(AdmissionDto admissionDto) throws ApplicationException {
	try {

		MedicoLeagal medicoLeagal = findById(admissionDto.getMedicoLegalId());
		medicoLeagal.setAccompaniedBy(admissionDto.getAccompaniedBy());
		medicoLeagal.setFirNo(admissionDto.getFirNo());
		medicoLeagal.setPriorityId(admissionDto.getPriorityId());
		medicoLeagal.setPoliceBuckleNo(admissionDto.getPoliceBuckleNo());
		medicoLeagal.setPoliceStationName(admissionDto.getPoliceStationName());
		medicoLeagal.setMlcContactNo(admissionDto.getMlcContactNo());
		medicoLeagal.setMlcRelationId(admissionDto.getMlcRelationId());
		medicoLeagal.setUnitId(admissionDto.getUnitId());
		medicoLeagal.setOrganizationId(admissionDto.getOrganizationId());
		medicoLeagal.setStatus(admissionDto.getStatus());
		medicoLeagal.setRemark(admissionDto.getRemark());
		medicoLeagal.setUpdatedBy(admissionDto.getUpdatedBy());
		medicoLeagal.setUpdatedDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId()));
		update(medicoLeagal);

		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
	} catch (HibernateException he) {
	logger.error("updateMedicolegalDetails failed", he);
	} catch (Exception e) {
	logger.error("updateMedicolegalDetails failed", e);
	}
	return new Response(ERROR, null, null, null, null);
}
}

/*accompaniedBy : $scope.accompaniedName,
firNo        :$scope.firNo,
policeBuckleNo :$scope.policeBuckleNo,
policeStationName:$scope.policeStationName,
mlcContactNo:$scope.mlcContactNo,
mlcRelationId:$scope.mlcRelationId,
mlcRelationId:($scope.mlcRelationId != "0" &&  $scope.mlcRelationId != null)?  $scope.mlcRelationId : null,
priorityId : ($scope.priorityId != "0" &&  $scope.priorityId != null)?  $scope.priorityId : null,
isMedicoLegal : 'Y',
status : 'A',
createdBy : $scope.userId,
updatedBy : $scope.userId,
organizationId : $scope.organizationId,
unitId : $scope.unitId,
admissionId:$scope.patientDetails.admissionId,
createdDate:$scope.currentDate,
dDate:$scope.currentDate,
visitTypeId:4,
admissionTypeId : 4,*/