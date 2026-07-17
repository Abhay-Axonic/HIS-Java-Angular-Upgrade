package com.param.procurement.workOrder.service.workOrderCreation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.entity.model.procurement.NewWorkOrderCreationStagedDetailsMapper;
import com.param.global.billing.dao.IVendorServiceRateConfigDao;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.pharmacy.dto.PendingServicesForWorkOrderCreationDto;
import com.param.global.pharmacy.dto.WorkOrderCreationRequestDetailsDto;
import com.param.global.pharmacy.dto.WorkOrderCreationRequestDto;
import com.param.procurement.workOrder.dao.workOrderCreation.INewWorkOrderCreationDao;
import com.param.procurement.workOrder.dao.workOrderCreation.IServiceWorkOrderCreationDao;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class ServiceWorkOrderCreationServiceImpl implements IServiceWorkOrderCreationService,ICommonConstants{

	@Autowired
	IServiceWorkOrderCreationDao iServiceWorkOrderCreationDao;
	
	@Autowired
	IVendorServiceRateConfigDao iVendorServiceRateConfigDao; 
	
	@Autowired
	INewWorkOrderCreationDao iNewWorkOrderCreationDao;
	
	@Override
	@Transactional
	public Response getPendingDataForWorkOrderCreation(PendingServicesForWorkOrderCreationDto requestDto) throws ApplicationException {
		Response res = iServiceWorkOrderCreationDao.getPendingDataForWorkOrderCreation(requestDto);
		List<PendingServicesForWorkOrderCreationDto> forWorkOrderCreationDtosList =  res.getListObject();
		for(PendingServicesForWorkOrderCreationDto creationDto : forWorkOrderCreationDtosList)
		{
			creationDto.setUnitId(requestDto.getUnitId());
			List<PendingServicesForWorkOrderCreationDto> detailsRes = iServiceWorkOrderCreationDao.forWorkOrderStatus(creationDto);
			long orderCount = detailsRes.stream().filter(animal -> animal.getOrderDetailsId()!=null && animal.getOrderDetailsId()>0).count();
			long workOrderCount = detailsRes.stream().filter(animal -> animal.getWorkorderRequestDetialsId()!=null && animal.getWorkorderRequestDetialsId()>0).count();
			
			if(orderCount==workOrderCount)
				creationDto.setWorOrderStatus("Complete");
			else if(orderCount!=workOrderCount && workOrderCount>0)
				creationDto.setWorOrderStatus("Partially Complete");
			else
				creationDto.setWorOrderStatus("Pending");
			
			creationDto.setPendingServicesCount(orderCount-workOrderCount);
		}
		return res;
	}

	@Override
	@Transactional
	public Response getDetails(PendingServicesForWorkOrderCreationDto requestDto) {
		
		Response res = iServiceWorkOrderCreationDao.getDetails(requestDto);
		List<PendingServicesForWorkOrderCreationDto> orderCreationDtosList = res.getListObject();
		for(PendingServicesForWorkOrderCreationDto creationDto : orderCreationDtosList)
		{
			creationDto.setUnitId(requestDto.getUnitId());
			creationDto.setBillingBedCategoryId(creationDto.getBillingBedCategoryId()!=null ? creationDto.getBillingBedCategoryId() : requestDto.getOpBillingBedCategoryId());
			creationDto.setVendorList(iVendorServiceRateConfigDao.getVendorRatesListForService(creationDto));
		}
		return res;
	}

	@Override
	@Transactional
	public Response saveWorkOrderForServices(WorkOrderCreationRequestDto workOrderCreationRequestDto) throws ApplicationException {

		Set<Integer> vendors = new HashSet<>();
		for(WorkOrderCreationRequestDetailsDto requestDetailsDto : workOrderCreationRequestDto.getRequestDetailsDto())
		{
			NewWorkOrderCreationStagedDetailsMapper creationStagedDetailsMapper = new NewWorkOrderCreationStagedDetailsMapper();
				creationStagedDetailsMapper.setPercentage(100.00);
				creationStagedDetailsMapper.setStageName("-");
			List<NewWorkOrderCreationStagedDetailsMapper> orderCreationStagedDetailsMappersList = new LinkedList<>();
				orderCreationStagedDetailsMappersList.add(creationStagedDetailsMapper);
			requestDetailsDto.setWorkOrderCreationStagedListData(orderCreationStagedDetailsMappersList);
			
			vendors.add(requestDetailsDto.getVendorId());
		}
        
		List<WorkOrderCreationRequestDetailsDto> forFilteration = new ArrayList<WorkOrderCreationRequestDetailsDto>();
        forFilteration.addAll(workOrderCreationRequestDto.getRequestDetailsDto());
		
		for(Integer vendor : vendors)
		{
			WorkOrderCreationRequestDto woCreationRequest = new WorkOrderCreationRequestDto();
				woCreationRequest.setRequestDetailsDto(forFilteration.stream().filter(detailsList->detailsList.getVendorId().equals(vendor)).collect(Collectors.toList()));
				woCreationRequest.setWorkorderRequestId(woCreationRequest.getRequestDetailsDto().getFirst().getWorkorderRequestId());
				woCreationRequest.setWorkorderRequestNumber(woCreationRequest.getRequestDetailsDto().getFirst().getWorkorderRequestNumber());
				woCreationRequest.setVendorId(vendor);
				woCreationRequest.setStoreId(workOrderCreationRequestDto.getStoreId());
				woCreationRequest.setGuaranteeWarranty(workOrderCreationRequestDto.getGuaranteeWarranty());
				woCreationRequest.setPaymentTerms(workOrderCreationRequestDto.getPaymentTerms());
				woCreationRequest.setContractTypeId(3);
				woCreationRequest.setBillTypeId(workOrderCreationRequestDto.getBillTypeId());
				woCreationRequest.setAgainstTypeId(workOrderCreationRequestDto.getAgainstTypeId());
				woCreationRequest.setDeliveryDate(workOrderCreationRequestDto.getDeliveryDate());
				woCreationRequest.setBillId(workOrderCreationRequestDto.getBillId());
				woCreationRequest.setEncounterId(workOrderCreationRequestDto.getEncounterId()>0 ? workOrderCreationRequestDto.getEncounterId() : null);
				woCreationRequest.setAdmissionId(workOrderCreationRequestDto.getAdmissionId()>0 ? workOrderCreationRequestDto.getAdmissionId() : null);
				woCreationRequest.setCreatedBy(workOrderCreationRequestDto.getCreatedBy());
				woCreationRequest.setOrgId(workOrderCreationRequestDto.getOrgId());
				woCreationRequest.setUnitId(workOrderCreationRequestDto.getUnitId());
				woCreationRequest.setTotalAmount(woCreationRequest.getRequestDetailsDto().stream().mapToDouble(o -> o.getItemRate()).sum());
				woCreationRequest.setTotalDiscountAmount(woCreationRequest.getRequestDetailsDto().stream().mapToDouble(o -> o.getItemDiscAmount()).sum());
				woCreationRequest.setTotalTaxAmount(woCreationRequest.getRequestDetailsDto().stream().mapToDouble(o -> o.getItemTaxAmount()).sum());
				woCreationRequest.setTotalNetAmount(woCreationRequest.getRequestDetailsDto().stream().mapToDouble(o -> o.getItemNetAmount()).sum());
			iNewWorkOrderCreationDao.saveCreationRequest(woCreationRequest);
		}
		
		return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
	}
	
	
}
