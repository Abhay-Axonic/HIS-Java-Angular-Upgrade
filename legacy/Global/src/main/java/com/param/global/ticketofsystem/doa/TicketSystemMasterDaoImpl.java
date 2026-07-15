package com.param.global.ticketofsystem.doa;

import in.param.exception.ApplicationException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.param.global.common.Response;
import com.param.global.model.TicketSystemMaster;
import com.param.global.ticketofsystem.dto.TicketSystemMasterDto;
import static com.param.global.common.ICommonConstants.*;
import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import in.param.common.dao.GenericDao;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class TicketSystemMasterDaoImpl extends GenericDao<TicketSystemMaster,Integer> implements ITicketSystemMasterDao {
	final static Logger logger = Logger.getLogger(TicketSystemMasterDaoImpl.class);

	
	
	@Autowired
    private SessionFactory sessionFactory;
	
	@Autowired
	private ModelMapper mapper;
	
	public TicketSystemMasterDaoImpl() {
		super(TicketSystemMaster.class);
	}
	
	@Override
	public Response getTicketSystemMasterById(Integer ticketMasterId) {
		try {
			Optional<TicketSystemMaster> ticketSystemMaster = Optional.of(findById(ticketMasterId));
			if (ticketSystemMaster.isPresent()) {

				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null,
						mapper.map(ticketSystemMaster.get(), TicketSystemMasterDto.class));
			} else {
				return new Response<>(NOT_FOUND, NOT_FOUND_CODE, null, null, null);
			}
		} catch (Exception e) {
		logger.error("getTicketSystemMasterById failed", e);
			String exceptionErrorMsg = TicketSystemMasterDaoImpl.class.getName() + ":" + e.getMessage();
			return new Response<>(ERROR, ERROR_CODE, exceptionErrorMsg, null, null);
		}
	}
		

	@Override
	@Transactional
	public Response getAllTicketSystemMasterList(Integer unitId, Integer organizationId) {
	 	try {
	 		List<TicketSystemMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LETEST_10_TICKET_MASTER")
					.setParameter("orgId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(TicketSystemMasterDto.class)).list();

			BigInteger count = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TICKET_MASTER_LIST_COUNT")
					.setParameter("orgId", organizationId)
					.setParameter("unitId", unitId)
					.uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, count.intValue());
		
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}

	}

	@Override
	@Transactional
	public Response addTicketSystemMaster(TicketSystemMasterDto ticketSystemMasterDto) {
		try {
			
			TicketSystemMaster ticketSystemMaster = mapper.map(ticketSystemMasterDto, TicketSystemMaster.class);
			TicketSystemMaster savedTicket = save(ticketSystemMaster);
			TicketSystemMasterDto savedTicketSystemMasterDto = mapper.map(savedTicket, TicketSystemMasterDto.class);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, savedTicketSystemMasterDto);	
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}

	}

	@Override
	@Transactional
	public Response updateTicketSystemMaster(TicketSystemMasterDto ticketSystemMasterDto) {
 
		try {
			 
				 		int updatedCount = sessionFactory.getCurrentSession()
						.getNamedQuery("UPDATE_TICKET_SYSTEM_MASTER")
						.setParameter("ticketStatus",ticketSystemMasterDto.getTicketStatus() == ACTIVE ? ACTIVE : ACTIVE)
						.setParameter("ticketReportedBy", ticketSystemMasterDto.getTicketReportedBy())
						.setParameter("ticketIssueDescription", ticketSystemMasterDto.getTicketIssueDescription())
						.setParameter("ticketContactNumber", ticketSystemMasterDto.getTicketContactNumber())
						.setParameter("ticketPriority", ticketSystemMasterDto.getTicketPriority())
						.setParameter("ticketIssueType", ticketSystemMasterDto.getTicketIssueType())
						.setParameter("ticketUpdatedDate", new Date())
						.setParameter("ticketMasterId", ticketSystemMasterDto.getTicketMasterId())
						.setParameter("orgId", ticketSystemMasterDto.getOrganizationId())
						.setParameter("unitId", ticketSystemMasterDto.getUnitId())
						.executeUpdate();
			if(updatedCount>0 && updatedCount!=0 ) {
				return new Response(SUCCESS, SUCCESS_MESSAGE, "Ticket Updated Succesfully",null,ticketSystemMasterDto);
			}
           else {
               return new Response(NO_DATA_FOUND, NOT_FOUND_CODE, "No Content found.",
                       null, ticketSystemMasterDto.getTicketMasterId());
           }
            
		   } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}

	}

	@Override
	@Transactional
	public Response changeTicketSystemMasterStatus(Integer ticketMasterId, Integer unitId, Integer organizationId,Character ticketStatus){
		try {
            Integer updatedNumber = sessionFactory.getCurrentSession()
           .getNamedQuery("UPDATE_TICKET_SYSTEM_MASTER_STATUS")
           .setParameter("ticketStatus", ticketStatus) 
           .setParameter("ticketMasterId", ticketMasterId)
           .setParameter("orgId", organizationId)
           .setParameter("unitId", unitId)
           .executeUpdate();
           if (updatedNumber > 0) {
        	   if(ticketStatus=='A') {
               return new Response(SUCCESS, SUCCESS_CODE, " Ticket Activated successfully.",
                       null, ticketMasterId);
        	   }
        	   else {
        		   return new Response(SUCCESS, SUCCESS_CODE, " Ticket Removed successfully.",
                           null, ticketMasterId);
			}
           }
           else {
               return new Response(NO_DATA_FOUND, NOT_FOUND_CODE, "No Content found.",
                       null, ticketMasterId);
           }
            
		   }catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		}
	
	@Override
	@Transactional
	public Response getDataForPdsSave (Integer unitId) {
		try {	
		 List<TicketSystemMasterDto> list= sessionFactory.getCurrentSession()
                 .getNamedQuery("GET_TICKET_DATA_FOR_PDS_SAVE") 
                 .setParameter("unitId",unitId)
                 .setResultTransformer(Transformers.aliasToBean(TicketSystemMasterDto.class)).list();
		 return new Response<>(SUCCESS, SUCCESS_CODE, null ,list, null ); 
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}

	}

	@Override
	@Transactional
	public Response searchTicketSystem(TicketSystemMasterDto ticketSystemMasterDto) {

		try {
			StringBuilder query = new StringBuilder(
					sessionFactory.getCurrentSession().getNamedQuery("SEARCH_TICKET_SYSTEM_MASTER").getQueryString());
			
			if(ticketSystemMasterDto.getSearchKeyWord()==null) {
			
			query.append(" ORDER BY ticket_master_id DESC ");	
			
			query.append(
					(ticketSystemMasterDto.getOffSet() != null && ticketSystemMasterDto.getNoOfRecordsPerPage() != null)
							? " LIMIT " + ticketSystemMasterDto.getNoOfRecordsPerPage() + " OFFSET "
									+ ticketSystemMasterDto.getOffSet()
							: " ");
			
			List<TicketSystemMasterDto> ticketSystemMasterDtoList = sessionFactory.getCurrentSession()
					.createSQLQuery(query.toString()).setParameter("orgId", ticketSystemMasterDto.getOrganizationId())
					.setParameter("unitId", ticketSystemMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(TicketSystemMasterDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, ticketSystemMasterDtoList, null);
			}
			else {

				query.append( " and (lower(ticket_mst.ticket_issue_description ) like :searchKeyWord "
						+ " OR lower(ticket_mst.ticket_contact_number ) like :searchKeyWord)");
				
				query.append(" ORDER BY ticket_master_id DESC ");
				
				query.append(
						(ticketSystemMasterDto.getOffSet() != null && ticketSystemMasterDto.getNoOfRecordsPerPage() != null)
								? " LIMIT " + ticketSystemMasterDto.getNoOfRecordsPerPage() + " OFFSET "
										+ ticketSystemMasterDto.getOffSet()
								: " ");
				List<TicketSystemMasterDto> ticketSystemMasterDtoList = sessionFactory.getCurrentSession()
						.createSQLQuery(query.toString())
						.setParameter("orgId", ticketSystemMasterDto.getOrganizationId())
						.setParameter("unitId", ticketSystemMasterDto.getUnitId())
						.setParameter("searchKeyWord","%"+ticketSystemMasterDto.getSearchKeyWord()+"%")
						.setParameter("searchKeyWord","%"+ticketSystemMasterDto.getSearchKeyWord()+"%")
						.setResultTransformer(Transformers.aliasToBean(TicketSystemMasterDto.class)).list();
				
				return new Response<>(SUCCESS, SUCCESS_CODE, null, ticketSystemMasterDtoList, ticketSystemMasterDtoList.size());
			}
			
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}

	}
	
	@Override
	public void updateTicketNumberInMaster(TicketSystemMasterDto ticketSystemMasterDto) {
		 		 sessionFactory.getCurrentSession()
				.getNamedQuery("UPDATE_TICKET_NUMBER_MASTER")
				.setParameter("ticketNumber", ticketSystemMasterDto.getTicketNumber())
				.setParameter("ticketMasterId", ticketSystemMasterDto.getTicketMasterId())
				.setParameter("orgId", ticketSystemMasterDto.getOrganizationId())
				.setParameter("unitId", ticketSystemMasterDto.getUnitId())
				.executeUpdate();
		
	}
}
