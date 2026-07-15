package com.param.lis.transaction.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.unit.dao.IPathologyMachineParameterDao;
import com.param.lis.unit.dto.MachineParameterDto;
import com.param.lis.unit.dto.MachineParameterMpperDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked","unused" })
public class PathologyMachineParameterServiceImpl implements IPathologyMachineParameterService, ICommonConstants, IError {
	

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private  IPathologyMachineParameterDao  iPathologyMachineParaDao;

	@Override
	@Transactional
	public Response saveMachineParameterMapping(
			MachineParameterMpperDto machineParameterMpperDto) throws ApplicationException {
         try{
        	 MachineParameterMpperDto dto=new MachineParameterMpperDto();
        	 dto=machineParameterMpperDto.getMachinePathalogyParameterMpperDtoList().get(0);
        	 Response res=iPathologyMachineParaDao.getPathalogyMachineParameterListByMachineId(dto);
        	 if(res.getStatus().equals("success")) {
        		 return new Response(ERROR, null, "Machine already mapped", null, null);
        	 }
        	return iPathologyMachineParaDao.saveMachineParameterMapping(machineParameterMpperDto);
			
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getPathalogyMachineParameterList(
			MachineParameterMpperDto machineParameterMpperDto) {
		try{
       	 return iPathologyMachineParaDao.getPathalogyMachineParameterList(machineParameterMpperDto);
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getPathalogyMachineParameterListByMachineId(MachineParameterMpperDto machineParameterMpperDto)
			throws ApplicationException {
		try{
	       	 return iPathologyMachineParaDao.getPathalogyMachineParameterListByMachineId(machineParameterMpperDto);
			}catch(Exception e){
				throw e;
			}
	}

	@Override
	@Transactional
	public Response updateMachineParaMapping(MachineParameterMpperDto machineParameterMpperDto)
			throws ApplicationException {
		try{
		if(machineParameterMpperDto.getMachinePathalogyParameterMpperDtoList().size()>0) {
			Response res=iPathologyMachineParaDao.saveMachineParameterMapping(machineParameterMpperDto);
		}
		if(machineParameterMpperDto.getInactivePathalogyParameterMpperDtoList().size()>0) {
		return iPathologyMachineParaDao.updateMachineParaMapping(machineParameterMpperDto);
		}
		return new Response(SUCCESS, null, "Machine Updated Successfully", null, null);
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updatePathalogyMachineParameterStatus(MachineParameterMpperDto machineParameterMpperDto) {
		try {
			return iPathologyMachineParaDao.updatePathalogyMachineParameterStatus(machineParameterMpperDto);
		}catch(Exception e){
			throw e;
		}
	}

	
	
}
