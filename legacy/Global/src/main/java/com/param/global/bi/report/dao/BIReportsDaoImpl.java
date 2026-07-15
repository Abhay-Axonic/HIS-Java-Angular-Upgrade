package com.param.global.bi.report.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.transform.Transformers;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.bi.report.dto.BIBedDetailsReportDto;
import com.param.global.bi.report.dto.BIDateListDto;
import com.param.global.bi.report.dto.BIReportsAllCollectionDto;
import com.param.global.bi.report.dto.BIReportsBillPaymentsDto;
import com.param.global.bi.report.dto.BIReportsCommonDto;
import com.param.global.bi.report.dto.BIReportsDailyCollectionDto;
import com.param.global.bi.report.dto.BIReportsDailyPatientCountDto;
import com.param.global.bi.report.dto.BIReportsDto;
import com.param.global.bi.report.dto.KeyValuePairDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class BIReportsDaoImpl implements IBIReportsDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(BIReportsDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	HikariDataSource driverManager;

	@Override
	public Response getBIReportsAllForCsv(BIReportsDto bIReportsDto) throws ApplicationException {
		try {
			String query = "SELECT " + "   	ft.payment_mode_id as \"payment_mode_id\", "
					+ "   	ft.speciality_id as \"speciality_id\", " + "   	ft.payment_mode_desc as \"payment_mode_desc\", "
					+ "   	sum(ft.amount) as \"amount\" , "
					+ "   	to_char(ft.depositdate, 'YYYY-MM-DD') as \"depositdate\" , "
					+ "   	ft.organization_id as \"organization_id\" , " + "   	ft.unit_id as \"unitId\" " + "    FROM "
					+ "   	\"Reports\".bi_finaltotalnew1 ft  " + "   	WHERE " + "    ft.organization_id=:orgId"
					+ "    AND ft.unit_id=:unitId" + "    GROUP BY " + "   	ft.organization_id, " + "   	ft.unit_id,  "
					+ "   	to_char(ft.depositdate, 'YYYY-MM-DD'), " + "   	ft.payment_mode_desc, "
					+ "   	ft.payment_mode_id, " + "   	ft.speciality_id "
					+ "    order by to_char(ft.depositdate, 'YYYY-MM-DD') asc";

			List<BIReportsDto> listDto = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("orgId", bIReportsDto.getOrganizationId())
					.setParameter("unitId", bIReportsDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BIReportsDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listDto, null);

		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getBIReportsAllForCsv failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getBIReportsForSpecialityAndPayModes(BIReportsDto bIReportsDto) throws ApplicationException {
		try {
			String query = " select " + "   	bif.organization_id as \"organizationId\", "
					+ "   	bif.unit_id as \"unit_id\", " + "   	to_char(bif.trandate,'DD/MM/YYYY') as \"trandate\", "
					+ "   	bif.totalcash as \"totalcash\", " + "   	bif.online as \"online\", "
					+ "   	bif.cheque as \"cheque\", " + "   	bif.creditcard as \"creditcard\", "
					+ "   	bif.debitcard as \"debitcard\", " + "   	bif.mpesa as \"mpesa\", "
					+ "   	bif.loyaltycard as \"loyaltycard\" " + "    from " + "   	\"Reports\".bi_finaltotalnewz bif "
					+ "    where bif.organization_id=:orgId " + "    and bif.unit_id=:unitId "
			// " order by " +
			// " date(bif.trandate)"
			;

			StringBuffer subQuery = new StringBuffer();
			if (bIReportsDto.getFromDate() != null && bIReportsDto.getToDate() != null) {
				subQuery.append(" AND to_char(bif.trandate , 'YYYY-MM-DD')  BETWEEN  :fromDate AND :toDate ");
			}
			if (bIReportsDto.getSpecialityId() != null) {
				subQuery.append(" AND bif.specialityId=:specialityId");
			}
			if (bIReportsDto.getPaymentModeId() != null) {
				subQuery.append(" AND bif.paymentModeId=:paymentModeId");
			}
			subQuery.append(" order by date(bif.trandate) ");
			query = query.concat(subQuery.toString());

			List<BIReportsDto> listDto = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("orgId", bIReportsDto.getOrganizationId())
					.setParameter("unitId", bIReportsDto.getUnitId()).setParameter("fromDate", bIReportsDto.getFromDate())
					.setParameter("toDate", bIReportsDto.getToDate())
					.setParameter("paymentModeId", bIReportsDto.getPaymentModeId())
					.setParameter("specialityId", bIReportsDto.getSpecialityId())
					.setFirstResult(bIReportsDto.getOffset() != null ? bIReportsDto.getOffset() : 0)
					.setMaxResults(
							bIReportsDto.getNoOfRecordsPerPage() != null ? bIReportsDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(BIReportsDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listDto, null);

		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getBIReportsForSpecialityAndPayModes failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getBIReportsAll(BIReportsDto bIReportsDto) throws ApplicationException {
		try {

			String query = "select " + "   	ft.payment_mode_id as \"payment_mode_id\", "
					+ "   	ft.speciality_id as \"speciality_id\", " + "   	ft.payment_mode_desc as \"payment_mode_desc\", "
					+ "   	sum( ft.amount ) as \"amount\", " + "   	to_char( " + "   		ft.depositdate, "
					+ "   		'YYYY-MM-DD' " + "   	) as \"depositdate\", " + "   	ft.organization_id as \"organization_id\", "
					+ "   	ft.unit_id as \"unitId\" " + "   from " + "   	\"Reports\".bi_finaltotalnew1 ft " + "   where "
					+ "   	ft.organization_id =:orgId " + "   	and ft.unit_id =:unitId ";

			// + "    order by to_char(ft.depositdate, 'YYYY-MM-DD') asc";

			StringBuffer subQuery = new StringBuffer();
			if (bIReportsDto.getFromDate() != null && bIReportsDto.getToDate() != null) {
				subQuery.append(" AND to_char(ft.depositdate, 'YYYY-MM-DD') BETWEEN " + ":fromDate AND :toDate");
			}
			if (bIReportsDto.getSpecialityId() != null) {
				subQuery.append(" AND ft.speciality_id =:specialityId ");
			}
			if (bIReportsDto.getPaymentModeId() != null) {
				subQuery.append(" AND ft.payment_mode_id=:paymentModeId ");
			}
			subQuery.append(" GROUP BY " + "   	ft.organization_id, " + "   	ft.unit_id,  " + "   ft.depositdate, "
					+ "   	ft.payment_mode_desc, " + "   	ft.payment_mode_id, " + "   	ft.speciality_id ");
			subQuery.append("order by ft.depositdate asc");
			query = query.concat(subQuery.toString());

			Query sqlQuery =  sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("orgId", bIReportsDto.getOrganizationId())
					.setParameter("unitId", bIReportsDto.getUnitId())
					.setFirstResult(bIReportsDto.getOffset() != null ? bIReportsDto.getOffset() : 0)
					.setMaxResults(bIReportsDto.getNoOfRecordsPerPage() != null ? bIReportsDto.getNoOfRecordsPerPage() : 10);


			if (bIReportsDto.getFromDate() != null && bIReportsDto.getToDate() != null) {
				sqlQuery.setParameter("fromDate",bIReportsDto.getFromDate());
				sqlQuery.setParameter("toDate",bIReportsDto.getToDate());
			}
			if (bIReportsDto.getSpecialityId() != null) {
				sqlQuery.setParameter("specialityId",bIReportsDto.getSpecialityId());
			}
			if (bIReportsDto.getPaymentModeId() != null) {
				sqlQuery.setParameter("paymentModeId",bIReportsDto.getPaymentModeId());
			}
			List<BIReportsDto> list =sqlQuery.setResultTransformer(Transformers.aliasToBean(BIReportsDto.class)).list();
			List<BIDateListDto> finallist = new LinkedList<>();
			String prevDate = null;
			List<KeyValuePairDto> keyValPairList = null;
			int i = 0;
			for (BIReportsDto dto : list) {

				if (prevDate == null || !prevDate.equals(dto.getDepositdate())) {
					keyValPairList = new LinkedList<>();

					KeyValuePairDto keyValuePairDto = new KeyValuePairDto();
					keyValuePairDto.setKey(dto.getPayment_mode_desc());
					keyValuePairDto.setValue(dto.getAmount());
					keyValuePairDto.setSpecialityId(dto.getSpeciality_id());
					keyValuePairDto.setPaymentModeId(dto.getPayment_mode_id());
					keyValPairList.add(keyValuePairDto);

					BIDateListDto dateDto = new BIDateListDto();
					dateDto.setTrandate(dto.getDepositdate());
					dateDto.setParameterList(keyValPairList);
					finallist.add(dateDto);
					prevDate = dto.getDepositdate();
				} else {
					KeyValuePairDto keyValuePairDto = new KeyValuePairDto();
					keyValuePairDto.setKey(dto.getPayment_mode_desc());
					keyValuePairDto.setValue(dto.getAmount());
					keyValuePairDto.setSpecialityId(dto.getSpeciality_id());
					keyValuePairDto.setPaymentModeId(dto.getPayment_mode_id());
					keyValPairList.add(keyValuePairDto);
				}
				if (list.size() == i) {
					BIDateListDto dateDto = new BIDateListDto();
					dateDto.setTrandate(dto.getDepositdate());
					dateDto.setParameterList(keyValPairList);
					finallist.add(dateDto);
				}
				i++;
			}

			return new Response(SUCCESS, SUCCESS_CODE, null, finallist, null);
		} catch (HibernateException he) {
		logger.error("getBIReportsAll failed", he);
		} catch (Exception e) {
		logger.error("getBIReportsAll failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPendingAndPartialBillPayments(BIReportsCommonDto bIReportsCommonDto)
			throws ApplicationException {
		try {

			String query = " select " + "   	to_char( " + "   		bill.bill_date_time, " + "   		'YYYY-MM-DD' "
					+ "   	) as \"billDateTime\", " + "   	sum( bill.outstanding ) as \"outstanding\" " + "    from "
					+ "   	public.Outstandingbills bill " + "    where " + "   	bill.unit_id = :unitId ";

			StringBuffer subQuery = new StringBuffer();
			if (bIReportsCommonDto.getFromDate() != null && bIReportsCommonDto.getToDate() != null) {
				subQuery.append(" and to_char(bill.bill_date_time, 'YYYY-MM-DD') BETWEEN :fromDate   AND  :toDate ");
			}

			subQuery.append(" group by " + "   	bill.bill_date_time ");
			subQuery.append(" ORDER BY bill.bill_date_time asc ");
			query = query.concat(subQuery.toString());
			Query SQLQuery = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("unitId", bIReportsCommonDto.getUnitId())
					.setFirstResult(bIReportsCommonDto.getOffset() != null ? bIReportsCommonDto.getOffset() : 0)
					.setMaxResults(bIReportsCommonDto.getNoOfRecordsPerPage() != null
							? bIReportsCommonDto.getNoOfRecordsPerPage()
							: 10);
			if (bIReportsCommonDto.getFromDate() != null && bIReportsCommonDto.getToDate() != null) {
				SQLQuery.setParameter("fromDate",bIReportsCommonDto.getFromDate());
				SQLQuery.setParameter("toDate",bIReportsCommonDto.getToDate());
			}
			List<BIReportsBillPaymentsDto> listDto = SQLQuery.setResultTransformer(Transformers.aliasToBean(BIReportsBillPaymentsDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listDto, null);

		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getPendingAndPartialBillPayments failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getUnitWiseDailyPatientCountWithGender(BIReportsCommonDto bIReportsCommonDto)
			throws ApplicationException {
		try {

			String query = "select " + "    	sum(ptcount) as \"ptcount\", "
					+ "    	to_char(abc.created_date, 'DD/MM/YYYY') as \"createdDate\", "
					+ "     coalesce( sum( case when abc.gender_id = 1 then ptcount else null end ) , 0) as \"male\", "
					+ "     coalesce(	sum( case when abc.gender_id = 2 then ptcount else null end ) , 0) as \"female\" "
					+ "    from " + "    	( " + "    	select "
					+ "    		con.unit_id, con.unit_name, count(ptcount) ptcount, con.gender_id, con.gender_name, con.created_date "
					+ "    	from " + "    		\"Reports\".bi_collection_new con " + "    	where "
					+ "    		con.unit_id =:unitId " + "    	group by "
					+ "    		con.unit_name, con.gender_name, con.unit_id, con.gender_id, con.created_date ) abc "
					+ "    where " + "    	abc.unit_id =:unitId ";

			StringBuffer subQuery = new StringBuffer();
			if (bIReportsCommonDto.getFromDate() != null && bIReportsCommonDto.getToDate() != null) {
				subQuery.append(" and to_char(abc.created_date, 'YYYY-MM-DD') BETWEEN :fromDate   AND :toDate ");
			}
			subQuery.append(" group by " + "   	abc.unit_id, " + "   	abc.unit_name, " + "   	abc.created_date ");
			subQuery.append(" ORDER BY abc.created_date asc ");
			query = query.concat(subQuery.toString());
			Query SQLQuery = sessionFactory.getCurrentSession()
					.createSQLQuery(query).setParameter("unitId", bIReportsCommonDto.getUnitId())
					.setFirstResult(bIReportsCommonDto.getOffset() != null ? bIReportsCommonDto.getOffset() : 0)
					.setMaxResults(bIReportsCommonDto.getNoOfRecordsPerPage() != null
							? bIReportsCommonDto.getNoOfRecordsPerPage()
							: 10);

			if (bIReportsCommonDto.getFromDate() != null && bIReportsCommonDto.getToDate() != null) {
				SQLQuery.setParameter("fromDate", bIReportsCommonDto.getFromDate());
				SQLQuery.setParameter("toDate", bIReportsCommonDto.getToDate());
			}

			List<BIReportsDailyPatientCountDto> listDto =	SQLQuery.setResultTransformer(Transformers.aliasToBean(BIReportsDailyPatientCountDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listDto, null);

		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getUnitWiseDailyPatientCountWithGender failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getUnitWiseDailyCollection(BIReportsCommonDto bIReportsCommonDto) throws ApplicationException {
		try {

			String query = " select "
					+ "   	to_char(coll.depositdate, 'YYYY-MM-DD') as \"depositDate\", "
					+ "   	coll.unit_name as \"unitName\", " + "   	sum(coll.cash) as \"cash\", "
					+ "   	sum(coll.cheque) as \"cheque\", " + "   	sum(coll.creditcard) as \"creditcard\", "
					+ "   	sum(coll.debitcard) as \"debitcard\", " + "   	sum(coll.mpesa) as \"mpesa\", "
					+ "   	sum(coll.loyaltycard) as \"loyaltycard\", " + "   	sum(coll.e_transfer) as \"eTransfer\""
					+ "    from " + "   	\"Reports\".bi_collection coll " + "    where " + "    coll.unit_id = :unitId ";

			StringBuffer subQuery = new StringBuffer();
			if (bIReportsCommonDto.getFromDate() != null && bIReportsCommonDto.getToDate() != null) {
				subQuery.append(" AND to_char(coll.depositdate, 'YYYY-MM-DD') BETWEEN :fromDate AND :toDate ");
			}

			subQuery.append(" group by "  + "   	coll.unit_name , "
					+ "   	coll.depositdate " );
			subQuery.append(" ORDER BY coll.depositdate asc ");
			query = query.concat(subQuery.toString());
			Query SQLQuery = sessionFactory.getCurrentSession()
					.createSQLQuery(query).setParameter("unitId", bIReportsCommonDto.getUnitId())
					.setFirstResult(bIReportsCommonDto.getOffset() != null ? bIReportsCommonDto.getOffset() : 0)
					.setMaxResults(bIReportsCommonDto.getNoOfRecordsPerPage() != null
							? bIReportsCommonDto.getNoOfRecordsPerPage()
							: 10);
			if (bIReportsCommonDto.getFromDate() != null && bIReportsCommonDto.getToDate() != null) {
				SQLQuery.setParameter("fromDate", bIReportsCommonDto.getFromDate());
				SQLQuery.setParameter("toDate", bIReportsCommonDto.getToDate());
			}
			List<BIReportsDailyCollectionDto> listDto =	SQLQuery.setResultTransformer(Transformers.aliasToBean(BIReportsDailyCollectionDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listDto, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getUnitWiseDailyCollection failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getBIReportsAllCollectionList(BIReportsCommonDto bIReportsCommonDto) throws ApplicationException {
		JSONArray jsonArrayPay = new JSONArray();
		JSONArray jsonArrayRev = new JSONArray();
		JSONArray jsonArrayPat = new JSONArray();
		JSONArray jsonArraySubDept = new JSONArray();
		PreparedStatement pstmt = null;
		PreparedStatement pstmtPay = null;
		Connection conn = null;
		try {

			conn = driverManager.getConnection();
			String query = "SELECT DISTINCT TO_CHAR(paynew.trandate, 'YYYY-MM-DD') AS \"trandate\" " +
					"FROM \"Reports\".BI_Dashboard_Revenue_collection_patientcountNEW paynew " +
					"WHERE TO_CHAR(paynew.trandate, 'YYYY-MM-DD') BETWEEN ? AND ? " +
					"AND paynew.organization_id = ? " +
					"AND paynew.unit_id = ?";
			 pstmt = conn.prepareStatement(query);
			pstmt.setString(1, bIReportsCommonDto.getFromDate());   // Set the 'fromDate' parameter
			pstmt.setString(2, bIReportsCommonDto.getToDate());     // Set the 'toDate' parameter
			pstmt.setInt(3, bIReportsCommonDto.getOrganizationId()); // Set the 'organizationId' parameter
			pstmt.setInt(4, bIReportsCommonDto.getUnitId());         // Set the 'unitId' parameter
			ResultSet rs = pstmt.executeQuery();
			JSONObject jsonMain = new JSONObject();
			JSONObject jsonPayment;
			JSONObject jsonRevenue = null;
			JSONObject jsonPatientCount;
			JSONObject jsonSubDept;
			String tranDate = "";

			while (rs.next()) {
				tranDate = rs.getString(1);
				String queryPay = "SELECT DISTINCT jsonb(paynew.payment) AS \"payment\" " +
						"FROM \"Reports\".BI_Dashboard_Revenue_collection_patientcountNEW paynew " +
						"WHERE TO_CHAR(paynew.trandate, 'YYYY-MM-DD') = ? " +
						"AND paynew.organization_id = ? " +
						"AND paynew.unit_id = ?";
				 pstmtPay = conn.prepareStatement(queryPay);
				pstmtPay.setString(1, tranDate);                        // Set the 'tranDate' parameter
				pstmtPay.setInt(2, bIReportsCommonDto.getOrganizationId()); // Set the 'organizationId' parameter
				pstmtPay.setInt(3, bIReportsCommonDto.getUnitId());         // Set the 'unitId' parameter
				ResultSet rsPay = pstmtPay.executeQuery();


						while (rsPay.next()) {
							jsonPayment = new JSONObject(rsPay.getString(1));
							jsonArrayPay.put(jsonPayment);

						}

			}

			jsonMain.put("trandate", tranDate);
			jsonMain.put("payments", jsonArrayPay);
			jsonMain.put("revenue", jsonArrayRev);
			jsonMain.put("patientcount", jsonArrayPat);
			return new Response(SUCCESS, SUCCESS_CODE, null, null, jsonMain.toString());
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getBIReportsAllCollectionList failed", e);
		}finally{
			try{
				if (pstmt != null) {
					pstmt.close();
				}
				if (pstmtPay != null) {
					pstmtPay.close();
				}
				conn.setAutoCommit(true);
				conn.close();
			}catch(Exception e){
			logger.error("getBIReportsAllCollectionList failed", e);
			}
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getBIReportsAllCollectionNew(BIReportsCommonDto bIReportsDto) throws ApplicationException {
		try {

			String query = "select " + "   	ft.payment_mode_id as \"payment_mode_id\", "
					+ "   	ft.speciality_id as \"speciality_id\", " + "   	ft.payment_mode_desc as \"payment_mode_desc\", "
					+ "   	sum( ft.amount ) as \"amount\", " + "   	to_char( " + "   		ft.depositdate, "
					+ "   		'YYYY-MM-DD' " + "   	) as \"depositdate\", " + "   	ft.organization_id as \"organization_id\", "
					+ "   	ft.unit_id as \"unitId\" " + "   from " + "   	\"Reports\".bi_finaltotalnew1 ft " + "   where "
					+ "   	ft.organization_id =:orgId " + "   	and ft.unit_id =:unitId ";

			// + "    order by to_char(ft.depositdate, 'YYYY-MM-DD') asc";

			StringBuffer subQuery = new StringBuffer();
			if (bIReportsDto.getFromDate() != null && bIReportsDto.getToDate() != null) {
				subQuery.append(" AND to_char(ft.depositdate, 'YYYY-MM-DD') BETWEEN :fromDate AND :toDate ");
			}
			subQuery.append(" GROUP BY " + "   	ft.organization_id, " + "   	ft.unit_id,  " + "   ft.depositdate, "
					+ "   	ft.payment_mode_desc, " + "   	ft.payment_mode_id, " + "   	ft.speciality_id ");
			subQuery.append("order by ft.depositdate asc");
			query = query.concat(subQuery.toString());

			Query SQLQUERY = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("orgId", bIReportsDto.getOrganizationId())
					.setParameter("unitId", bIReportsDto.getUnitId())
					.setFirstResult(bIReportsDto.getOffset() != null ? bIReportsDto.getOffset() : 0)
					.setMaxResults(
							bIReportsDto.getNoOfRecordsPerPage() != null ? bIReportsDto.getNoOfRecordsPerPage() : 10);



			if (bIReportsDto.getFromDate() != null && bIReportsDto.getToDate() != null) {
				SQLQUERY.setParameter("fromDate", bIReportsDto.getFromDate());
				SQLQUERY.setParameter("toDate", bIReportsDto.getToDate());
			}
			List<BIReportsDto> list = SQLQUERY.setResultTransformer(Transformers.aliasToBean(BIReportsDto.class)).list();
			List<BIDateListDto> finallist = new LinkedList<>();
			String prevDate = null;
			List<KeyValuePairDto> keyValPairList = null;
			int i = 0;
			for (BIReportsDto dto : list) {

				if (prevDate == null || !prevDate.equals(dto.getDepositdate())) {
					keyValPairList = new LinkedList<>();

					KeyValuePairDto keyValuePairDto = new KeyValuePairDto();
					keyValuePairDto.setKey(dto.getPayment_mode_desc());
					keyValuePairDto.setValue(dto.getAmount());
					keyValuePairDto.setSpecialityId(dto.getSpeciality_id());
					keyValuePairDto.setPaymentModeId(dto.getPayment_mode_id());
					keyValPairList.add(keyValuePairDto);

					BIDateListDto dateDto = new BIDateListDto();
					dateDto.setTrandate(dto.getDepositdate());
					dateDto.setParameterList(keyValPairList);
					finallist.add(dateDto);
					prevDate = dto.getDepositdate();
				} else {
					KeyValuePairDto keyValuePairDto = new KeyValuePairDto();
					keyValuePairDto.setKey(dto.getPayment_mode_desc());
					keyValuePairDto.setValue(dto.getAmount());
					keyValuePairDto.setSpecialityId(dto.getSpeciality_id());
					keyValuePairDto.setPaymentModeId(dto.getPayment_mode_id());
					keyValPairList.add(keyValuePairDto);
				}
				if (list.size() == i) {
					BIDateListDto dateDto = new BIDateListDto();
					dateDto.setTrandate(dto.getDepositdate());
					dateDto.setParameterList(keyValPairList);
					finallist.add(dateDto);
				}
				i++;


			}

			return new Response(SUCCESS, SUCCESS_CODE, null, finallist, null);
		} catch (HibernateException he) {
		logger.error("getBIReportsAllCollectionNew failed", he);
		} catch (Exception e) {
		logger.error("getBIReportsAllCollectionNew failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAllCollectionListDateWise(BIReportsCommonDto bIReportsCommonDto,String inputDateFormat) throws ApplicationException {
		try {

			if(bIReportsCommonDto.getUnitId()!=null)
			{
				String query =   " select  "
						 + "  	0 as \"subSpecialityId\",  "
						 + "  	'-' as \"subSpecialityName\",  "
						 + "  	0 as \"serviceWiseCount\",  "
						 + "  	0 as \"specialityId\",  "
						 + "  	'-' as \"specialityName\",  "
						 + "  	cast(0 as numeric(10,2)) as \"serviceWiseNetAmount\",  "
						 + "  	'-' as \"serviceStandardName\",  "
						 + "  	0 as \"serviceId\",  "
						 + "  	mpmm.payment_mode_id as \"paymentModeId\",  "
						 + "  	mpmm.payment_mode_desc as \"paymentMode\",  "
						 + "  	cast(sum(tprd.amount) as numeric(10, 2)) as \"paymentModeNet\",  "
						 + "  	0 as \"OP\",  "
						 + "  	0 as \"IP\",  "
						 + "  	0 as \"Male\",  "
						 + "  	0 as \"Female\",  "
						 + "	cast(0 as numeric(10,2)) as \"discount\", "
						 + "	cast(0 as numeric(10,2)) as \"concession\", "
						 + "  	to_char(tprm.date_of_reciept , '"+inputDateFormat+"') as \"createdDate\",  "
						 + "  	tprd.orgnization_id as \"orgId\",  "
						 + "  	tprd.unit_id as \"unitId\",  "
						 + "  	1 as \"type\",  "
						 + "  	0 as \"payeeDue\",  "
						 + "  	'-' as \"payeeDesc\"  "
						 + "  from  "
						 + "  	billing.t_payment_reciept_details tprd  "
						 + "  inner join billing.t_payment_reciept_master tprm on  "
						 + "  	tprm.payment_reciept_id = tprd.payment_receipt_master_id  "
						 + "  inner join billing.m_billing_master bill on  "
						 + "  	bill.billing_master_id = tprm.billing_master_id  "
						 + "  inner join m_payment_mode_master mpmm on  "
						 + "  	tprd.payment_mode_id = mpmm.payment_mode_id  "
						 + "  where  "
						 + "  	tprd.orgnization_id = :orgId  "
						 + "  	and tprd.unit_id = :unitId  "
						 + "  	and to_char(tprm.date_of_reciept , '"+inputDateFormat+"') between :fromDate and :toDate  "
						 + "  	and bill.status = 'A'  "
						 + "  	and tprm.status = 'A'  "
						 + "  group by  "
						 + "  	tprd.orgnization_id,  "
						 + "  	tprd.unit_id,  "
						 + "  	to_char(tprm.date_of_reciept , '"+inputDateFormat+"'),  "
						 + "  	mpmm.payment_mode_id "
						 + "  union  "
						 + "  select  "
						 + "  	mssm.sub_speciality_id as \"subSpecialityId\",  "
						 + "  	mssm.sub_speciality_name as \"subSpecialityName\",  "
						 + "  	0 as \"serviceWiseCount\",  "
						 + "  	spl.speciality_id as \"specialityId\",  "
						 + "  	spl.speciality_name as \"specialityName\",  "
						 + "  	cast(sum(tbd.net_amt) as numeric(10, 2)) as \"serviceWiseNetAmount\",  "
						 + "  	msm.service_standard_name as \"serviceStandardName\",  "
						 + "  	msm.service_master_id \"serviceId\",  "
						 + "  	0 as \"paymentModeId\" ,  "
						 + "  	'-' as \"paymentMode\",  "
						 + "  	cast(0 as numeric(10,2)) as \"paymentModeNet\",  "
						 + "  	0 as \"OP\",  "
						 + "  	0 as \"IP\",  "
						 + "  	0 as \"Male\",  "
						 + "  	0 as \"Female\",  "
						 + "	cast(0 as numeric(10,2)) as \"discount\", "
						 + "	cast(0 as numeric(10,2)) as \"concession\", "
						 + "  	to_char(bill.bill_date_time , '"+inputDateFormat+"') as \"createdDate\",  "
						 + "  	tbd.organization_id as \"orgId\",  "
						 + "  	tbd.unit_id as \"unitId\",  "
						 + "  	2 as \"type\",  "
						 + "  	0 as \"payeeDue\",  "
						 + "  	'-' as \"payeeDesc\"  "
						 + "  from  "
						 + "  	billing.t_billing_details tbd  "
						 + "  inner join billing.m_billing_master bill on  "
						 + "  	bill.billing_master_id = tbd.billing_master_id  "
						 + "  inner join service.m_service_master msm on  "
						 + "  	tbd.service_id = msm.service_master_id  "
						 + "  inner join doctor.m_sub_speciality_master mssm on  "
						 + "  	mssm.sub_speciality_id = msm.sub_speciality_id  "
						 + "  inner join doctor.m_speciality_master spl on  "
						 + "  	msm.speciality_id = spl.speciality_id  "
						 + "  where  "
						 + "  	tbd.organization_id = :orgId  "
						 + "  	and tbd.unit_id = :unitId  "
						 + "  	and to_char(bill.bill_date_time , '"+inputDateFormat+"') between :fromDate and :toDate  "
						 + "  	and tbd.status = 'A'  "
						 + "  	and bill.status = 'A'  "
						 + "	and tbd.package_consumption_amt = 0 "
						 + "  group by  "
						 + "  	tbd.organization_id,  "
						 + "  	tbd.unit_id,  "
						 + "  	to_char(bill.bill_date_time , '"+inputDateFormat+"'),  "
						 + "  	spl.speciality_id,  "
						 + "  	mssm.sub_speciality_id,  "
						 + " 	msm.service_master_id "
						 + "  union  "
						 + "  select  "
						 + "  	0 as \"subSpecialityId\",  "
						 + "  	'-' as \"subSpecialityName\",  "
						 + "  	0 as \"serviceWiseCount\",  "
						 + "  	0 as \"specialityId\",  "
						 + "  	'-' as \"specialityName\",  "
						 + "  	cast(0 as numeric(10,2)) as \"serviceWiseNetAmount\",  "
						 + "  	'-' as \"serviceStandardName\",  "
						 + "  	0 as \"serviceId\",  "
						 + "  	0 as \"paymentModeId\" ,  "
						 + "  	'-' as \"paymentMode\",  "
						 + "  	cast(0 as numeric(10,2)) as \"paymentModeNet\",  "
						 + "  	coalesce(sum(op), 0) as \"OP\",  "
						 + "  	coalesce(sum(IP), 0) as \"IP\",  "
						 + "  	coalesce(sum(male), 0) as \"Male\",  "
						 + "  	coalesce(sum(female), 0)as \"Female\",  "
						 + "	cast(0 as numeric(10,2)) as \"discount\", "
						 + "	cast(0 as numeric(10,2)) as \"concession\", "
						 + "  	to_char(created_date , '"+inputDateFormat+"') as \"createdDate\",  "
						 + "  	organization_id as \"orgId\",  "
						 + "  	unit_id as \"unitId\",  "
						 + "  	5 as \"type\",  "
						 + "  	0 as \"payeeDue\",  "
						 + "  	'-' as \"payeeDesc\"  "
						 + "  from  "
						 + "  	(  "
						 + "  	select  "
						 + "  		bi_collection_new.created_date,  "
						 + "  		case  "
						 + "  			when bi_collection_new.type = 'OP' then bi_collection_new.ptcount  "
						 + "  		end as OP,  "
						 + "  		case  "
						 + "  			when bi_collection_new.type = 'IP' then bi_collection_new.ptcount  "
						 + "  		end as IP,  "
						 + "  		case  "
						 + "  			when bi_collection_new.gender_name = 'Male' then bi_collection_new.ptcount  "
						 + "  		end as Male,  "
						 + "  		case  "
						 + "  			when bi_collection_new.gender_name = 'Female' then bi_collection_new.ptcount  "
						 + "  		end as Female, bi_collection_new.organization_id, bi_collection_new.unit_id, bi_collection_new.ptcount  "
						 + "  	from  "
						 + "  		\"Reports\".bi_collection_new  "
						 + "  	where  "
						 + "  		to_char(created_date , '"+inputDateFormat+"') between :fromDate and :toDate  "
						 + "  		and unit_id = :unitId  "
						 + "  		and organization_id = :orgId) ptcounts  "
						 + "  group by  "
						 + "  	organization_id,  "
						 + "  	unit_id,  "
						 + "  	to_char(created_date , '"+inputDateFormat+"')  "
						 + "  union  "
						 + "  select  "
						 + "  	0 as \"subSpecialityId\",  "
						 + "  	'-' as \"subSpecialityName\",  "
						 + "  	0 as \"serviceWiseCount\",  "
						 + "  	0 as \"specialityId\",  "
						 + "  	'-' as \"specialityName\",  "
						 + "  	cast(0 as numeric(10,2)) as \"serviceWiseNetAmount\",  "
						 + "  	'-' as \"serviceStandardName\",  "
						 + "  	0 as \"serviceId\",  "
						 + "  	0 as \"paymentModeId\" ,  "
						 + "  	'-' as \"paymentMode\",  "
						 + "  	cast(0 as numeric(10,2)) as \"paymentModeNet\",  "
						 + "  	0 as \"OP\",  "
						 + "  	0 as \"IP\",  "
						 + "  	0 as \"Male\",  "
						 + "  	0 as \"Female\",  "
						 + "	cast(0 as numeric(10,2)) as \"discount\", "
						 + "	cast(0 as numeric(10,2)) as \"concession\", "
						 + "  	to_char(mbm.bill_date_time , '"+inputDateFormat+"') as \"createdDate\",  "
						 + "  	bp.org_id as \"orgId\",  "
						 + "  	bp.unit_id as \"unitId\",  "
						 + "  	4 as \"type\",  "
						 + "  	cast(sum(bp.due_amount) as numeric(10, 2)) as \"payeeDue\",  "
						 + "  	tcm.company_desc as \"payeeDesc\"  "
						 + "  from  "
						 + "  	billing.t_bill_payee_details bp  "
						 + "  inner join billing.m_billing_master mbm on  "
						 + "  	mbm.billing_master_id = bp.bill_id  "
						 + "  inner join t_company_master tcm on  "
						 + "  	bp.payee_id = tcm.company_id  "
						 + "  where  "
						 + "  	bp.org_id =:orgId  "
						 + "  	and bp.unit_id = :unitId  "
						 + "  	and to_char(mbm.bill_date_time, '"+inputDateFormat+"') between :fromDate and :toDate  "
						 + "  	and mbm.status = 'A'  "
						 + "  group by  "
						 + "  	bp.org_id,  "
						 + "  	bp.unit_id,  "
						 + "  	to_char(mbm.bill_date_time , '"+inputDateFormat+"'),  "
						 + "  	tcm.company_desc  "
						 + "  union  "
						 + "  select  "
						 + "  	mssm.sub_speciality_id as \"subSpecialityId\",  "
						 + "  	mssm.sub_speciality_name as \"subSpecialityName\",  "
						 + "  	count(tbd.billing_details_id)as \"serviceWiseCount\",  "
						 + "  	spl.speciality_id as \"specialityId\",  "
						 + "  	spl.speciality_name as \"specialityName\",  "
						 + "  	cast(0 as numeric(10,2)) as \"serviceWiseNetAmount\",  "
						 + "  	msm.service_standard_name as \"serviceStandardName\",  "
						 + "  	msm.service_master_id \"serviceId\",  "
						 + "  	0 as \"paymentModeId\",  "
						 + "  	'-' as \"paymentMode\",  "
						 + "  	cast(0 as numeric(10,2)) as \"paymentModeNet\",  "
						 + "  	0 as \"OP\",  "
						 + "  	0 as \"IP\",  "
						 + "  	0 as \"Male\",  "
						 + "  	0 as \"Female\",  "
						 + "	cast(0 as numeric(10,2)) as \"discount\", "
						 + "	cast(0 as numeric(10,2)) as \"concession\", "
						 + "  	to_char( bill.bill_date_time, '"+inputDateFormat+"') as \"createdDate\",  "
						 + "  	tbd.organization_id as \"orgId\",  "
						 + "  	tbd.unit_id as \"unitId\",  "
						 + "  	7 as \"type\",  "
						 + "  	0 as \"payeeDue\",  "
						 + "  	'-' as \"payeeDesc\"  "
						 + "  from  "
						 + "  	billing.t_billing_details tbd  "
						 + "  inner join billing.m_billing_master bill on  "
						 + "  	bill.billing_master_id = tbd.billing_master_id  "
						 + "  inner join service.m_service_master msm on  "
						 + "  	tbd.service_id = msm.service_master_id  "
						 + "  inner join doctor.m_sub_speciality_master mssm on  "
						 + "  	mssm.sub_speciality_id = msm.sub_speciality_id  "
						 + "  inner join doctor.m_speciality_master spl on  "
						 + "  	msm.speciality_id = spl.speciality_id  "
						 + "  where  "
						 + "  	tbd.organization_id = :orgId  "
						 + "  	and tbd.unit_id = :unitId  "
						 + "  	and to_char( bill.bill_date_time, '"+inputDateFormat+"') between :fromDate and :toDate  "
						 + "  	and tbd.status = 'A'  "
						 + "  	and bill.status = 'A'  "
						 + "  group by  "
						 + "  	tbd.organization_id,  "
						 + "  	tbd.unit_id,  "
						 + "  	to_char( bill.bill_date_time, '"+inputDateFormat+"'),  "
						 + "  	spl.speciality_id,  "
						 + "  	mssm.sub_speciality_id,  "
						 + " 	msm.service_master_id "
						 + "  union "
						 + "  select  "
						 + "  	0 as \" subSpecialityId\" ,  "
						 + "  	'-' as \" subSpecialityName\" ,  "
						 + "  	0 as \" serviceWiseCount\" ,  "
						 + "  	0 as \" specialityId\" ,  "
						 + "  	'-' as \" specialityName\" ,  "
						 + "  	cast(0 as numeric(10,2)) as \" serviceWiseNetAmount\" ,  "
						 + "  	'-' as \" serviceStandardName\" ,  "
						 + "  	0 as \" serviceId\" ,  "
						 + "  	0 as \" paymentModeId\" ,  "
						 + "  	'-' as \" paymentMode\" ,  "
						 + "  	cast(0 as numeric(10,2)) as \" paymentModeNet\" ,  "
						 + "  	0 as \" OP\" ,  "
						 + "  	0 as \" IP\" ,  "
						 + "  	0 as \" Male\" ,  "
						 + "  	0 as \" Female\" ,  "
						 + "  	cast(sum(bill.discount_amount+bill.round_off) as numeric(10,2)) as \" discount\" ,  "
						 + "  	cast(sum(bill.concession_amount) as numeric(10,2)) as \" concession\" ,  "
						 + "  	to_char( bill.bill_date_time, '"+inputDateFormat+"') as \" createdDate\" ,  "
						 + "  	bill.organisation_id as \" orgId\" ,  "
						 + "  	bill.unit_id as \" unitId\" ,  "
						 + "  	6 as \" type\" ,  "
						 + "  	0 as \" payeeDue\" ,  "
						 + "  	'-' as \" payeeDesc\"   "
						 + "  from  "
						 + "  	billing.m_billing_master bill  "
						 + "  where  "
						 + "  	bill.organisation_id = :orgId  "
						 + "  	and bill.unit_id = :unitId  "
						 + "  	and to_char( bill.bill_date_time, '"+inputDateFormat+"') between :fromDate and :toDate  "
						 + "  	and bill.status = 'A'  "
						 + "  group by  "
						 + "  	bill.organisation_id,  "
						 + "  	bill.unit_id,  "
						 + "  	to_char( bill.bill_date_time, '"+inputDateFormat+"')  "
						 + "  order by  "
						 + "  	\"orgId\",  "
						 + "  	\"unitId\",  "
						 + "  	\"createdDate\",  "
						 + "  	\"type\",  "
						 + "  	\"paymentModeId\",  "
						 + "  	\"specialityId\",  "
						 + " 	\"subSpecialityId\"  "  ;

				StringBuffer subQuery = new StringBuffer();

				query = query.concat(subQuery.toString());
				List<BIReportsAllCollectionDto> listDto = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("unitId", bIReportsCommonDto.getUnitId())
						.setParameter("orgId", bIReportsCommonDto.getOrganizationId())
						.setParameter("fromDate", bIReportsCommonDto.getFromDate())
						.setParameter("toDate", bIReportsCommonDto.getToDate())
						.setResultTransformer(Transformers.aliasToBean(BIReportsAllCollectionDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, listDto, null);
			}else{
				String query =   " select  "
						 + "  	0 as \"subSpecialityId\",  "
						 + "  	'-' as \"subSpecialityName\",  "
						 + "  	0 as \"serviceWiseCount\",  "
						 + "  	0 as \"specialityId\",  "
						 + "  	'-' as \"specialityName\",  "
						 + "  	cast(0 as numeric(10,2)) as \"serviceWiseNetAmount\",  "
						 + "  	'-' as \"serviceStandardName\",  "
						 + "  	0 as \"serviceId\",  "
						 + "  	mpmm.payment_mode_id as \"paymentModeId\",  "
						 + "  	mpmm.payment_mode_desc as \"paymentMode\",  "
						 + "  	cast(sum(tprd.amount) as numeric(10, 2)) as \"paymentModeNet\",  "
						 + "  	0 as \"OP\",  "
						 + "  	0 as \"IP\",  "
						 + "  	0 as \"Male\",  "
						 + "  	0 as \"Female\",  "
						 + "	cast(0 as numeric(10,2)) as \"discount\", "
						 + "	cast(0 as numeric(10,2)) as \"concession\", "
						 + "  	to_char(tprm.date_of_reciept , '"+inputDateFormat+"') as \"createdDate\",  "
						 + "  	tprd.orgnization_id as \"orgId\",  "
						 //+ "  	tprd.unit_id as \"unitId\",  "
						 + "  	1 as \"type\",  "
						 + "  	0 as \"payeeDue\",  "
						 + "  	'-' as \"payeeDesc\"  "
						 + "  from  "
						 + "  	billing.t_payment_reciept_details tprd  "
						 + "  inner join billing.t_payment_reciept_master tprm on  "
						 + "  	tprm.payment_reciept_id = tprd.payment_receipt_master_id  "
						 + "  inner join billing.m_billing_master bill on  "
						 + "  	bill.billing_master_id = tprm.billing_master_id  "
						 + "  inner join m_payment_mode_master mpmm on  "
						 + "  	tprd.payment_mode_id = mpmm.payment_mode_id  "
						 + "  where  "
						 + "  	tprd.orgnization_id = :orgId  "
						 //+ "  	and tprd.unit_id = :unitId  "
						 + "  	and to_char(tprm.date_of_reciept , '"+inputDateFormat+"') between :fromDate and :toDate  "
						 + "  	and bill.status = 'A'  "
						 + "  	and tprm.status = 'A'  "
						 + "  group by  "
						 + "  	tprd.orgnization_id,  "
						 //+ "  	tprd.unit_id,  "
						 + "  	to_char(tprm.date_of_reciept , '"+inputDateFormat+"'),  "
						 + "  	mpmm.payment_mode_id "
						 + "  union  "
						 + "  select  "
						 + "  	mssm.sub_speciality_id as \"subSpecialityId\",  "
						 + "  	mssm.sub_speciality_name as \"subSpecialityName\",  "
						 + "  	0 as \"serviceWiseCount\",  "
						 + "  	spl.speciality_id as \"specialityId\",  "
						 + "  	spl.speciality_name as \"specialityName\",  "
						 + "  	cast(sum(tbd.net_amt) as numeric(10, 2)) as \"serviceWiseNetAmount\",  "
						 + "  	msm.service_standard_name as \"serviceStandardName\",  "
						 + "  	msm.service_master_id \"serviceId\",  "
						 + "  	0 as \"paymentModeId\" ,  "
						 + "  	'-' as \"paymentMode\",  "
						 + "  	cast(0 as numeric(10,2)) as \"paymentModeNet\",  "
						 + "  	0 as \"OP\",  "
						 + "  	0 as \"IP\",  "
						 + "  	0 as \"Male\",  "
						 + "  	0 as \"Female\",  "
						 + "	cast(0 as numeric(10,2)) as \"discount\", "
						 + "	cast(0 as numeric(10,2)) as \"concession\", "
						 + "  	to_char(bill.bill_date_time , '"+inputDateFormat+"') as \"createdDate\",  "
						 + "  	tbd.organization_id as \"orgId\",  "
						 //+ "  	tbd.unit_id as \"unitId\",  "
						 + "  	2 as \"type\",  "
						 + "  	0 as \"payeeDue\",  "
						 + "  	'-' as \"payeeDesc\"  "
						 + "  from  "
						 + "  	billing.t_billing_details tbd  "
						 + "  inner join billing.m_billing_master bill on  "
						 + "  	bill.billing_master_id = tbd.billing_master_id  "
						 + "  inner join service.m_service_master msm on  "
						 + "  	tbd.service_id = msm.service_master_id  "
						 + "  inner join doctor.m_sub_speciality_master mssm on  "
						 + "  	mssm.sub_speciality_id = msm.sub_speciality_id  "
						 + "  inner join doctor.m_speciality_master spl on  "
						 + "  	msm.speciality_id = spl.speciality_id  "
						 + "  where  "
						 + "  	tbd.organization_id = :orgId  "
						 //+ "  	and tbd.unit_id = :unitId  "
						 + "  	and to_char(bill.bill_date_time , '"+inputDateFormat+"') between :fromDate and :toDate  "
						 + "  	and tbd.status = 'A'  "
						 + "  	and bill.status = 'A'  "
						 + "	and tbd.package_consumption_amt = 0 "
						 + "  group by  "
						 + "  	tbd.organization_id,  "
						 //+ "  	tbd.unit_id,  "
						 + "  	to_char(bill.bill_date_time , '"+inputDateFormat+"'),  "
						 + "  	spl.speciality_id,  "
						 + "  	mssm.sub_speciality_id,  "
						 + " 	msm.service_master_id "
						 + "  union  "
						 + "  select  "
						 + "  	0 as \"subSpecialityId\",  "
						 + "  	'-' as \"subSpecialityName\",  "
						 + "  	0 as \"serviceWiseCount\",  "
						 + "  	0 as \"specialityId\",  "
						 + "  	'-' as \"specialityName\",  "
						 + "  	cast(0 as numeric(10,2)) as \"serviceWiseNetAmount\",  "
						 + "  	'-' as \"serviceStandardName\",  "
						 + "  	0 as \"serviceId\",  "
						 + "  	0 as \"paymentModeId\" ,  "
						 + "  	'-' as \"paymentMode\",  "
						 + "  	cast(0 as numeric(10,2)) as \"paymentModeNet\",  "
						 + "  	coalesce(sum(op), 0) as \"OP\",  "
						 + "  	coalesce(sum(IP), 0) as \"IP\",  "
						 + "  	coalesce(sum(male), 0) as \"Male\",  "
						 + "  	coalesce(sum(female), 0)as \"Female\",  "
						 + "	cast(0 as numeric(10,2)) as \"discount\", "
						 + "	cast(0 as numeric(10,2)) as \"concession\", "
						 + "  	to_char(created_date , '"+inputDateFormat+"') as \"createdDate\",  "
						 + "  	organization_id as \"orgId\",  "
						 //+ "  	unit_id as \"unitId\",  "
						 + "  	5 as \"type\",  "
						 + "  	0 as \"payeeDue\",  "
						 + "  	'-' as \"payeeDesc\"  "
						 + "  from  "
						 + "  	(  "
						 + "  	select  "
						 + "  		bi_collection_new.created_date,  "
						 + "  		case  "
						 + "  			when bi_collection_new.type = 'OP' then bi_collection_new.ptcount  "
						 + "  		end as OP,  "
						 + "  		case  "
						 + "  			when bi_collection_new.type = 'IP' then bi_collection_new.ptcount  "
						 + "  		end as IP,  "
						 + "  		case  "
						 + "  			when bi_collection_new.gender_name = 'Male' then bi_collection_new.ptcount  "
						 + "  		end as Male,  "
						 + "  		case  "
						 + "  			when bi_collection_new.gender_name = 'Female' then bi_collection_new.ptcount  "
						 + "  		end as Female, bi_collection_new.organization_id, bi_collection_new.unit_id, bi_collection_new.ptcount  "
						 + "  	from  "
						 + "  		\"Reports\".bi_collection_new  "
						 + "  	where  "
						 + "  		to_char(created_date , '"+inputDateFormat+"') between :fromDate and :toDate  "
						// + "  		and unit_id = :unitId  "
						 + "  		and organization_id = :orgId) ptcounts  "
						 + "  group by  "
						 + "  	organization_id,  "
						 //+ "  	unit_id,  "
						 + "  	to_char(created_date , '"+inputDateFormat+"')  "
						 + "  union  "
						 + "  select  "
						 + "  	0 as \"subSpecialityId\",  "
						 + "  	'-' as \"subSpecialityName\",  "
						 + "  	0 as \"serviceWiseCount\",  "
						 + "  	0 as \"specialityId\",  "
						 + "  	'-' as \"specialityName\",  "
						 + "  	cast(0 as numeric(10,2)) as \"serviceWiseNetAmount\",  "
						 + "  	'-' as \"serviceStandardName\",  "
						 + "  	0 as \"serviceId\",  "
						 + "  	0 as \"paymentModeId\" ,  "
						 + "  	'-' as \"paymentMode\",  "
						 + "  	cast(0 as numeric(10,2)) as \"paymentModeNet\",  "
						 + "  	0 as \"OP\",  "
						 + "  	0 as \"IP\",  "
						 + "  	0 as \"Male\",  "
						 + "  	0 as \"Female\",  "
						 + "	cast(0 as numeric(10,2)) as \"discount\", "
						 + "	cast(0 as numeric(10,2)) as \"concession\", "
						 + "  	to_char(mbm.bill_date_time , '"+inputDateFormat+"') as \"createdDate\",  "
						 + "  	bp.org_id as \"orgId\",  "
						 //+ "  	bp.unit_id as \"unitId\",  "
						 + "  	4 as \"type\",  "
						 + "  	cast(sum(bp.due_amount) as numeric(10, 2)) as \"payeeDue\",  "
						 + "  	tcm.company_desc as \"payeeDesc\"  "
						 + "  from  "
						 + "  	billing.t_bill_payee_details bp  "
						 + "  inner join billing.m_billing_master mbm on  "
						 + "  	mbm.billing_master_id = bp.bill_id  "
						 + "  inner join t_company_master tcm on  "
						 + "  	bp.payee_id = tcm.company_id  "
						 + "  where  "
						 + "  	bp.org_id =:orgId  "
						 //+ "  	and bp.unit_id = :unitId  "
						 + "  	and to_char(mbm.bill_date_time, '"+inputDateFormat+"') between :fromDate and :toDate  "
						 + "  	and mbm.status = 'A'  "
						 + "  group by  "
						 + "  	bp.org_id,  "
						 //+ "  	bp.unit_id,  "
						 + "  	to_char(mbm.bill_date_time , '"+inputDateFormat+"'),  "
						 + "  	tcm.company_desc  "
						 + "  union  "
						 + "  select  "
						 + "  	mssm.sub_speciality_id as \"subSpecialityId\",  "
						 + "  	mssm.sub_speciality_name as \"subSpecialityName\",  "
						 + "  	count(tbd.billing_details_id)as \"serviceWiseCount\",  "
						 + "  	spl.speciality_id as \"specialityId\",  "
						 + "  	spl.speciality_name as \"specialityName\",  "
						 + "  	cast(0 as numeric(10,2)) as \"serviceWiseNetAmount\",  "
						 + "  	msm.service_standard_name as \"serviceStandardName\",  "
						 + "  	msm.service_master_id \"serviceId\",  "
						 + "  	0 as \"paymentModeId\",  "
						 + "  	'-' as \"paymentMode\",  "
						 + "  	cast(0 as numeric(10,2)) as \"paymentModeNet\",  "
						 + "  	0 as \"OP\",  "
						 + "  	0 as \"IP\",  "
						 + "  	0 as \"Male\",  "
						 + "  	0 as \"Female\",  "
						 + "	cast(0 as numeric(10,2)) as \"discount\", "
						 + "	cast(0 as numeric(10,2)) as \"concession\", "
						 + "  	to_char( bill.bill_date_time, '"+inputDateFormat+"') as \"createdDate\",  "
						 + "  	tbd.organization_id as \"orgId\",  "
						 //+ "  	tbd.unit_id as \"unitId\",  "
						 + "  	7 as \"type\",  "
						 + "  	0 as \"payeeDue\",  "
						 + "  	'-' as \"payeeDesc\"  "
						 + "  from  "
						 + "  	billing.t_billing_details tbd  "
						 + "  inner join billing.m_billing_master bill on  "
						 + "  	bill.billing_master_id = tbd.billing_master_id  "
						 + "  inner join service.m_service_master msm on  "
						 + "  	tbd.service_id = msm.service_master_id  "
						 + "  inner join doctor.m_sub_speciality_master mssm on  "
						 + "  	mssm.sub_speciality_id = msm.sub_speciality_id  "
						 + "  inner join doctor.m_speciality_master spl on  "
						 + "  	msm.speciality_id = spl.speciality_id  "
						 + "  where  "
						 + "  	tbd.organization_id = :orgId  "
						// + "  	and tbd.unit_id = :unitId  "
						 + "  	and to_char( bill.bill_date_time, '"+inputDateFormat+"') between :fromDate and :toDate  "
						 + "  	and tbd.status = 'A'  "
						 + "  	and bill.status = 'A'  "
						 + "  group by  "
						 + "  	tbd.organization_id,  "
						 //+ "  	tbd.unit_id,  "
						 + "  	to_char( bill.bill_date_time, '"+inputDateFormat+"'),  "
						 + "  	spl.speciality_id,  "
						 + "  	mssm.sub_speciality_id,  "
						 + " 	msm.service_master_id "
						 + "  union "
						 + "  select  "
						 + "  	0 as \" subSpecialityId\" ,  "
						 + "  	'-' as \" subSpecialityName\" ,  "
						 + "  	0 as \" serviceWiseCount\" ,  "
						 + "  	0 as \" specialityId\" ,  "
						 + "  	'-' as \" specialityName\" ,  "
						 + "  	cast(0 as numeric(10,2)) as \" serviceWiseNetAmount\" ,  "
						 + "  	'-' as \" serviceStandardName\" ,  "
						 + "  	0 as \" serviceId\" ,  "
						 + "  	0 as \" paymentModeId\" ,  "
						 + "  	'-' as \" paymentMode\" ,  "
						 + "  	cast(0 as numeric(10,2)) as \" paymentModeNet\" ,  "
						 + "  	0 as \" OP\" ,  "
						 + "  	0 as \" IP\" ,  "
						 + "  	0 as \" Male\" ,  "
						 + "  	0 as \" Female\" ,  "
						 + "  	cast(sum(bill.discount_amount+bill.round_off) as numeric(10,2)) as \" discount\" ,  "
						 + "  	cast(sum(bill.concession_amount) as numeric(10,2)) as \" concession\" ,  "
						 + "  	to_char( bill.bill_date_time, '"+inputDateFormat+"') as \" createdDate\" ,  "
						 + "  	bill.organisation_id as \" orgId\" ,  "
						 //+ "  	bill.unit_id as \" unitId\" ,  "
						 + "  	6 as \" type\" ,  "
						 + "  	0 as \" payeeDue\" ,  "
						 + "  	'-' as \" payeeDesc\"   "
						 + "  from  "
						 + "  	billing.m_billing_master bill  "
						 + "  where  "
						 + "  	bill.organisation_id = :orgId  "
						 //+ "  	and bill.unit_id = :unitId  "
						 + "  	and to_char( bill.bill_date_time, '"+inputDateFormat+"') between :fromDate and :toDate  "
						 + "  	and bill.status = 'A'  "
						 + "  group by  "
						 + "  	bill.organisation_id,  "
						// + "  	bill.unit_id,  "
						 + "  	to_char( bill.bill_date_time, '"+inputDateFormat+"')  "
						 + "  order by  "
						 + "  	\"orgId\",  "
						 //+ "  	\"unitId\",  "
						 + "  	\"createdDate\",  "
						 + "  	\"type\",  "
						 + "  	\"paymentModeId\",  "
						 + "  	\"specialityId\",  "
						 + " 	\"subSpecialityId\"  "  ;

				StringBuffer subQuery = new StringBuffer();

				query = query.concat(subQuery.toString());
				List<BIReportsAllCollectionDto> listDto = sessionFactory.getCurrentSession().createSQLQuery(query)
						//.setParameter("unitId", bIReportsCommonDto.getUnitId())
						.setParameter("orgId", bIReportsCommonDto.getOrganizationId())
						.setParameter("fromDate", bIReportsCommonDto.getFromDate())
						.setParameter("toDate", bIReportsCommonDto.getToDate())
						.setResultTransformer(Transformers.aliasToBean(BIReportsAllCollectionDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, listDto, null);
			}

		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getAllCollectionListDateWise failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getPatientSourceWiseData(BIReportsCommonDto bIReportsCommonDto,String inputDateFormat) throws ApplicationException {
		try {

			if(bIReportsCommonDto.getUnitId()!=null)
			{
				String query =      " select "
						 + " 	mssm.sub_speciality_id as \"subSpecialityId\", "
						 + " 	mssm.sub_speciality_name as \"subSpecialityName\", "
						 + " 	0 as \"serviceWiseCount\", "
						 + " 	spl.speciality_id as \"specialityId\", "
						 + " 	spl.speciality_name as \"specialityName\", "
						 + " 	cast(sum(tbd.net_amt) as numeric(10, 2)) as \"serviceWiseNetAmount\", "
						 + " 	msm.service_standard_name as \"serviceStandardName\", "
						 + " 	msm.service_master_id \"serviceId\", "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"') as \"createdDate\", "
						 + " 	tbd.organization_id as \"orgId\", "
						 + " 	tbd.unit_id as \"unitId\", "
						 + " 	1 as \"type\", "
						 + " 	coalesce (mpsm.patient_source_name,'-') as \"patientScource\",  "
						 + " 	coalesce (mpsm.patient_source_id,0) as \"patientScourceId\" "
						 + " from "
						 + " 	billing.t_billing_details tbd "
						 + " inner join billing.m_billing_master bill on "
						 + " 	bill.billing_master_id = tbd.billing_master_id "
						 + " left join opd.t_encounter_master tem on  "
						 + " 	tem.encounter_id = bill.encounter_id  "
						 + " left join adt.t_admission ta on  "
						 + " 	ta.admission_id = bill.admission_id  "
						 + " left join adt.m_patient_source_master mpsm  "
						 + "  	on mpsm.patient_source_id =(case when tem.encounter_id is not null then "
						 + " 		tem.source_id "
						 + " 	when ta.admission_id is not null then  "
						 + " 		ta.source_id end ) "
						 + " inner join service.m_service_master msm on "
						 + " 	tbd.service_id = msm.service_master_id "
						 + " inner join doctor.m_sub_speciality_master mssm on "
						 + " 	mssm.sub_speciality_id = msm.sub_speciality_id "
						 + " inner join doctor.m_speciality_master spl on "
						 + " 	msm.speciality_id = spl.speciality_id "
						 + " where "
						 + " 	tbd.organization_id = :orgId "
						 + " 	and tbd.unit_id = :unitId "
						 + " 	and tbd.package_consumption_amt = 0 "
						 + " 	and to_char(bill.bill_date_time , '"+inputDateFormat+"') between :fromDate and :toDate "
						 + " 	and tbd.status = 'A' "
						 + " 	and bill.status = 'A' "
						 + " group by "
						 + " 	tbd.organization_id, "
						 + " 	tbd.unit_id, "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"'), "
						 + " 	mpsm.patient_source_id, "
						 + " 	spl.speciality_id, "
						 + " 	mssm.sub_speciality_id, "
						 + " 	msm.service_master_id "
						 + " union "
						 + " select "
						 + " 	msm.sub_speciality_id as \"subSpecialityId\", "
						 + " 	mssm.sub_speciality_name as \"subSpecialityName\", "
						 + " 	count(tbd.billing_details_id)as \"serviceWiseCount\", "
						 + " 	msm.speciality_id as \"specialityId\", "
						 + " 	spl.speciality_name as \"specialityName\", "
						 + " 	0 as \"serviceWiseNetAmount\", "
						 + " 	msm.service_standard_name as \"serviceStandardName\", "
						 + " 	msm.service_master_id \"serviceId\", "
						 + " 	to_char( bill.bill_date_time, '"+inputDateFormat+"' ) as \"createdDate\", "
						 + " 	tbd.organization_id as \"orgId\", "
						 + " 	tbd.unit_id as \"unitId\", "
						 + " 	2 as \"type\", "
						 + " 	coalesce (mpsm.patient_source_name,'-') as \"patientScource\",  "
						 + " 	coalesce (mpsm.patient_source_id,0) as \"patientScourceId\" "
						 + " from "
						 + " 	billing.t_billing_details tbd "
						 + " inner join billing.m_billing_master bill on "
						 + " 	bill.billing_master_id = tbd.billing_master_id "
						 + " left join opd.t_encounter_master tem on  "
						 + " 	tem.encounter_id = bill.encounter_id  "
						 + " left join adt.t_admission ta on  "
						 + " 	ta.admission_id = bill.admission_id  "
						 + " left join adt.m_patient_source_master mpsm  "
						 + "  	on mpsm.patient_source_id = (case when tem.encounter_id is not null then "
						 + " 		tem.source_id "
						 + " 	when ta.admission_id is not null then  "
						 + " 		ta.source_id end )	 "
						 + " inner join service.m_service_master msm on "
						 + " 	tbd.service_id = msm.service_master_id "
						 + " inner join doctor.m_sub_speciality_master mssm on "
						 + " 	mssm.sub_speciality_id = msm.sub_speciality_id "
						 + " inner join doctor.m_speciality_master spl on "
						 + " 	msm.speciality_id = spl.speciality_id "
						 + " where "
						 + " 	tbd.organization_id = :orgId "
						 + " 	and tbd.unit_id = :unitId "
						 + " 	and to_char( bill.bill_date_time, '"+inputDateFormat+"' ) between :fromDate and :toDate "
						 + " 	and tbd.status = 'A' "
						 + " 	and bill.status = 'A' "
						 + " group by "
						 + " 	tbd.organization_id, "
						 + " 	tbd.unit_id, "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"'), "
						 + " 	mpsm.patient_source_id, "
						 + " 	spl.speciality_id, "
						 + " 	mssm.sub_speciality_id, "
						 + " 	msm.service_master_id "
						 + " order by "
						 + " 	\"orgId\", "
						 + " 	\"unitId\", "
						 + " 	\"createdDate\", "
						 + " 	\"type\", "
						 + " 	\"patientScourceId\", "
						 + " 	\"specialityId\", "
						 + " 	\"subSpecialityId\", "
						 + " 	\"serviceId\" ";

				StringBuffer subQuery = new StringBuffer();

				query = query.concat(subQuery.toString());
				List<BIReportsAllCollectionDto> listDto = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("unitId", bIReportsCommonDto.getUnitId())
						.setParameter("orgId", bIReportsCommonDto.getOrganizationId())
						.setParameter("fromDate", bIReportsCommonDto.getFromDate())
						.setParameter("toDate", bIReportsCommonDto.getToDate())
						.setResultTransformer(Transformers.aliasToBean(BIReportsAllCollectionDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, listDto, null);
			}else{
				String query =      " select "
						 + " 	mssm.sub_speciality_id as \"subSpecialityId\", "
						 + " 	mssm.sub_speciality_name as \"subSpecialityName\", "
						 + " 	0 as \"serviceWiseCount\", "
						 + " 	spl.speciality_id as \"specialityId\", "
						 + " 	spl.speciality_name as \"specialityName\", "
						 + " 	cast(sum(tbd.net_amt) as numeric(10, 2)) as \"serviceWiseNetAmount\", "
						 + " 	msm.service_standard_name as \"serviceStandardName\", "
						 + " 	msm.service_master_id \"serviceId\", "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"') as \"createdDate\", "
						 + " 	tbd.organization_id as \"orgId\", "
						 //+ " 	tbd.unit_id as \"unitId\", "
						 + " 	1 as \"type\", "
						 + " 	coalesce (mpsm.patient_source_name,'-') as \"patientScource\",  "
						 + " 	coalesce (mpsm.patient_source_id,0) as \"patientScourceId\" "
						 + " from "
						 + " 	billing.t_billing_details tbd "
						 + " inner join billing.m_billing_master bill on "
						 + " 	bill.billing_master_id = tbd.billing_master_id "
						 + " left join opd.t_encounter_master tem on  "
						 + " 	tem.encounter_id = bill.encounter_id  "
						 + " left join adt.t_admission ta on  "
						 + " 	ta.admission_id = bill.admission_id  "
						 + " left join adt.m_patient_source_master mpsm  "
						 + "  	on mpsm.patient_source_id =(case when tem.encounter_id is not null then "
						 + " 		tem.source_id "
						 + " 	when ta.admission_id is not null then  "
						 + " 		ta.source_id end ) "
						 + " inner join service.m_service_master msm on "
						 + " 	tbd.service_id = msm.service_master_id "
						 + " inner join doctor.m_sub_speciality_master mssm on "
						 + " 	mssm.sub_speciality_id = msm.sub_speciality_id "
						 + " inner join doctor.m_speciality_master spl on "
						 + " 	msm.speciality_id = spl.speciality_id "
						 + " where "
						 + " 	tbd.organization_id = :orgId "
						 //+ " 	and tbd.unit_id = :unitId "
						 + " 	and tbd.package_consumption_amt = 0 "
						 + " 	and to_char(bill.bill_date_time , '"+inputDateFormat+"') between :fromDate and :toDate "
						 + " 	and tbd.status = 'A' "
						 + " 	and bill.status = 'A' "
						 + " group by "
						 + " 	tbd.organization_id, "
						 //+ " 	tbd.unit_id, "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"'), "
						 + " 	mpsm.patient_source_id, "
						 + " 	spl.speciality_id, "
						 + " 	mssm.sub_speciality_id, "
						 + " 	msm.service_master_id "
						 + " union "
						 + " select "
						 + " 	msm.sub_speciality_id as \"subSpecialityId\", "
						 + " 	mssm.sub_speciality_name as \"subSpecialityName\", "
						 + " 	count(tbd.billing_details_id)as \"serviceWiseCount\", "
						 + " 	msm.speciality_id as \"specialityId\", "
						 + " 	spl.speciality_name as \"specialityName\", "
						 + " 	0 as \"serviceWiseNetAmount\", "
						 + " 	msm.service_standard_name as \"serviceStandardName\", "
						 + " 	msm.service_master_id \"serviceId\", "
						 + " 	to_char( bill.bill_date_time, '"+inputDateFormat+"' ) as \"createdDate\", "
						 + " 	tbd.organization_id as \"orgId\", "
						 //+ " 	tbd.unit_id as \"unitId\", "
						 + " 	2 as \"type\", "
						 + " 	coalesce (mpsm.patient_source_name,'-') as \"patientScource\",  "
						 + " 	coalesce (mpsm.patient_source_id,0) as \"patientScourceId\" "
						 + " from "
						 + " 	billing.t_billing_details tbd "
						 + " inner join billing.m_billing_master bill on "
						 + " 	bill.billing_master_id = tbd.billing_master_id "
						 + " left join opd.t_encounter_master tem on  "
						 + " 	tem.encounter_id = bill.encounter_id  "
						 + " left join adt.t_admission ta on  "
						 + " 	ta.admission_id = bill.admission_id  "
						 + " left join adt.m_patient_source_master mpsm  "
						 + "  	on mpsm.patient_source_id = (case when tem.encounter_id is not null then "
						 + " 		tem.source_id "
						 + " 	when ta.admission_id is not null then  "
						 + " 		ta.source_id end )	 "
						 + " inner join service.m_service_master msm on "
						 + " 	tbd.service_id = msm.service_master_id "
						 + " inner join doctor.m_sub_speciality_master mssm on "
						 + " 	mssm.sub_speciality_id = msm.sub_speciality_id "
						 + " inner join doctor.m_speciality_master spl on "
						 + " 	msm.speciality_id = spl.speciality_id "
						 + " where "
						 + " 	tbd.organization_id = :orgId "
						 //+ " 	and tbd.unit_id = :unitId "
						 + " 	and to_char( bill.bill_date_time, '"+inputDateFormat+"' ) between :fromDate and :toDate "
						 + " 	and tbd.status = 'A' "
						 + " 	and bill.status = 'A' "
						 + " group by "
						 + " 	tbd.organization_id, "
						 //+ " 	tbd.unit_id, "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"'), "
						 + " 	mpsm.patient_source_id, "
						 + " 	spl.speciality_id, "
						 + " 	mssm.sub_speciality_id, "
						 + " 	msm.service_master_id "
						 + " order by "
						 + " 	\"orgId\", "
						 //+ " 	\"unitId\", "
						 + " 	\"createdDate\", "
						 + " 	\"type\", "
						 + " 	\"patientScourceId\", "
						 + " 	\"specialityId\", "
						 + " 	\"subSpecialityId\", "
						 + " 	\"serviceId\" ";

				StringBuffer subQuery = new StringBuffer();

				query = query.concat(subQuery.toString());
				List<BIReportsAllCollectionDto> listDto = sessionFactory.getCurrentSession().createSQLQuery(query)
						//.setParameter("unitId", bIReportsCommonDto.getUnitId())
						.setParameter("orgId", bIReportsCommonDto.getOrganizationId())
						.setParameter("fromDate", bIReportsCommonDto.getFromDate())
						.setParameter("toDate", bIReportsCommonDto.getToDate())
						.setResultTransformer(Transformers.aliasToBean(BIReportsAllCollectionDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, listDto, null);
			}



		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getPatientSourceWiseData failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getEntitlementWiseData(BIReportsCommonDto bIReportsCommonDto,String inputDateFormat) throws ApplicationException {
		try {

			if(bIReportsCommonDto.getUnitId()!=null)
			{
				String query =    " select "
						 + " 	mssm.sub_speciality_id as \"subSpecialityId\", "
						 + " 	mssm.sub_speciality_name as \"subSpecialityName\", "
						 + " 	0 as \"serviceWiseCount\", "
						 + " 	spl.speciality_id as \"specialityId\", "
						 + " 	spl.speciality_name as \"specialityName\", "
						 + " 	cast(sum(tbdpm.payee_amount) as numeric(10, 2)) as \"serviceWiseNetAmount\", "
						 + " 	msm.service_standard_name as \"serviceStandardName\", "
						 + " 	msm.service_master_id \"serviceId\", "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"') as \"createdDate\", "
						 + " 	tbd.organization_id as \"orgId\", "
						 + " 	tbd.unit_id as \"unitId\", "
						 + " 	1 as \"type\", "
						 + " 	tcm.company_id as \"companyId\", "
						 + " 	tcm.company_desc as \"companyDesc\", "
						 + " 	ti.insurance_id as \"insuranceId\", "
						 + " 	ti.insurance_desc as \"insuranceDesc\" "
						 + " from "
						 + " 	billing.t_bill_details_payee_mapper tbdpm "
						 + " inner join billing.t_billing_details tbd on "
						 + " 	tbd.billing_details_id = tbdpm.bill_details_id "
						 + " inner join billing.m_billing_master bill on "
						 + " 	bill.billing_master_id = tbd.billing_master_id "
						 + " inner join service.m_service_master msm on "
						 + " 	tbd.service_id = msm.service_master_id "
						 + " inner join doctor.m_sub_speciality_master mssm on "
						 + " 	mssm.sub_speciality_id = msm.sub_speciality_id "
						 + " inner join doctor.m_speciality_master spl on "
						 + " 	msm.speciality_id = spl.speciality_id "
						 + " inner join t_company_master tcm on "
						 + " 	tcm.company_id = tbdpm.payee_id "
						 + " left join adt.t_insurance ti on "
						 + " 	ti.insurance_id = tbdpm.associate_company_id "
						 + " inner join t_unit_application_billing_configuration tuabc on "
						 + " 	tuabc.unit_id = tbdpm.unit_id 	 "
						 + " where "
						 + " 	tbd.organization_id = :orgId "
						 + " 	and tbd.unit_id = :unitId "
						 + " 	and to_char(bill.bill_date_time , '"+inputDateFormat+"') between :fromDate and :toDate "
						 + " 	and tbd.status = 'A' "
						 + " 	and bill.status = 'A' "
						 + " 	and tbd.package_consumption_amt = 0 "
						 + " 	and tuabc.self_payee_id = tbdpm.payee_id  "
						 + " group by "
						 + " 	tbd.organization_id, "
						 + " 	tbd.unit_id, "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"'), "
						 + " 	tcm.company_id , "
						 + " 	ti.insurance_id, "
						 + " 	spl.speciality_id, "
						 + " 	mssm.sub_speciality_id, "
						 + " 	msm.service_master_id	 "
						 + " union "
						 + " select "
						 + " 	mssm.sub_speciality_id as \"subSpecialityId\", "
						 + " 	mssm.sub_speciality_name as \"subSpecialityName\", "
						 + " 	0 as \"serviceWiseCount\", "
						 + " 	spl.speciality_id as \"specialityId\", "
						 + " 	spl.speciality_name as \"specialityName\", "
						 + " 	cast(sum(tbdpm.payee_amount) as numeric(10, 2)) as \"serviceWiseNetAmount\", "
						 + " 	msm.service_standard_name as \"serviceStandardName\", "
						 + " 	msm.service_master_id \"serviceId\", "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"') as \"createdDate\", "
						 + " 	tbd.organization_id as \"orgId\", "
						 + " 	tbd.unit_id as \"unitId\", "
						 + " 	3 as \"type\", "
						 + " 	tcm.company_id as \"companyId\", "
						 + " 	tcm.company_desc as \"companyDesc\", "
						 + " 	ti.insurance_id as \"insuranceId\", "
						 + " 	ti.insurance_desc as \"insuranceDesc\" "
						 + " from "
						 + " 	billing.t_bill_details_payee_mapper tbdpm "
						 + " inner join billing.t_billing_details tbd on "
						 + " 	tbd.billing_details_id = tbdpm.bill_details_id "
						 + " inner join billing.m_billing_master bill on "
						 + " 	bill.billing_master_id = tbd.billing_master_id "
						 + " inner join service.m_service_master msm on "
						 + " 	tbd.service_id = msm.service_master_id "
						 + " inner join doctor.m_sub_speciality_master mssm on "
						 + " 	mssm.sub_speciality_id = msm.sub_speciality_id "
						 + " inner join doctor.m_speciality_master spl on "
						 + " 	msm.speciality_id = spl.speciality_id "
						 + " inner join t_company_master tcm on "
						 + " 	tcm.company_id = tbdpm.payee_id "
						 + " left join adt.t_insurance ti on "
						 + " 	ti.insurance_id = tbdpm.associate_company_id "
						 + " inner join t_unit_application_billing_configuration tuabc on "
						 + " 	tuabc.unit_id = tbdpm.unit_id 	 "
						 + " where "
						 + " 	tbd.organization_id = :orgId "
						 + " 	and tbd.unit_id = :unitId "
						 + " 	and to_char(bill.bill_date_time , '"+inputDateFormat+"') between :fromDate and :toDate "
						 + " 	and tbd.status = 'A' "
						 + " 	and bill.status = 'A' "
						 + " 	and tbd.package_consumption_amt = 0 "
						 + " 	and tuabc.self_payee_id != tbdpm.payee_id  "
						 + " 	and ti.insurance_id is null "
						 + " group by "
						 + " 	tbd.organization_id, "
						 + " 	tbd.unit_id, "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"'), "
						 + " 	tcm.company_id , "
						 + " 	ti.insurance_id, "
						 + " 	spl.speciality_id, "
						 + " 	mssm.sub_speciality_id, "
						 + " 	msm.service_master_id "
						 + " union  "
						 + " select "
						 + " 	mssm.sub_speciality_id as \"subSpecialityId\", "
						 + " 	mssm.sub_speciality_name as \"subSpecialityName\", "
						 + " 	0 as \"serviceWiseCount\", "
						 + " 	spl.speciality_id as \"specialityId\", "
						 + " 	spl.speciality_name as \"specialityName\", "
						 + " 	cast(sum(tbdpm.payee_amount) as numeric(10, 2)) as \"serviceWiseNetAmount\", "
						 + " 	msm.service_standard_name as \"serviceStandardName\", "
						 + " 	msm.service_master_id \"serviceId\", "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"') as \"createdDate\", "
						 + " 	tbd.organization_id as \"orgId\", "
						 + " 	tbd.unit_id as \"unitId\", "
						 + " 	2 as \"type\", "
						 + " 	tcm.company_id as \"companyId\", "
						 + " 	tcm.company_desc as \"companyDesc\", "
						 + " 	ti.insurance_id as \"insuranceId\", "
						 + " 	ti.insurance_desc as \"insuranceDesc\" "
						 + " from "
						 + " 	billing.t_bill_details_payee_mapper tbdpm "
						 + " inner join billing.t_billing_details tbd on "
						 + " 	tbd.billing_details_id = tbdpm.bill_details_id "
						 + " inner join billing.m_billing_master bill on "
						 + " 	bill.billing_master_id = tbd.billing_master_id "
						 + " inner join service.m_service_master msm on "
						 + " 	tbd.service_id = msm.service_master_id "
						 + " inner join doctor.m_sub_speciality_master mssm on "
						 + " 	mssm.sub_speciality_id = msm.sub_speciality_id "
						 + " inner join doctor.m_speciality_master spl on "
						 + " 	msm.speciality_id = spl.speciality_id "
						 + " inner join t_company_master tcm on "
						 + " 	tcm.company_id = tbdpm.payee_id "
						 + " inner join adt.t_insurance ti on "
						 + " 	ti.insurance_id = tbdpm.associate_company_id "
						 + " inner join t_unit_application_billing_configuration tuabc on "
						 + " 	tuabc.unit_id = tbdpm.unit_id 	 "
						 + " where "
						 + " 	tbd.organization_id = :orgId "
						 + " 	and tbd.unit_id = :unitId "
						 + " 	and to_char(bill.bill_date_time , '"+inputDateFormat+"') between :fromDate and :toDate "
						 + " 	and tbd.status = 'A' "
						 + " 	and bill.status = 'A' "
						 + " 	and tbd.package_consumption_amt = 0 "
						 + " 	and tuabc.self_payee_id != tbdpm.payee_id  "
						 + " 	and ti.insurance_id is not null "
						 + " group by "
						 + " 	tbd.organization_id, "
						 + " 	tbd.unit_id, "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"'), "
						 + " 	tcm.company_id , "
						 + " 	ti.insurance_id, "
						 + " 	spl.speciality_id, "
						 + " 	mssm.sub_speciality_id, "
						 + " 	msm.service_master_id "
						 + " union "
						 + " select "
						 + " 	mssm.sub_speciality_id as \"subSpecialityId\", "
						 + " 	mssm.sub_speciality_name as \"subSpecialityName\", "
						 + " 	count(tbd.billing_details_id) as \"serviceWiseCount\", "
						 + " 	spl.speciality_id as \"specialityId\", "
						 + " 	spl.speciality_name as \"specialityName\", "
						 + " 	0 as \"serviceWiseNetAmount\", "
						 + " 	msm.service_standard_name as \"serviceStandardName\", "
						 + " 	msm.service_master_id \"serviceId\", "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"') as \"createdDate\", "
						 + " 	tbd.organization_id as \"orgId\", "
						 + " 	tbd.unit_id as \"unitId\", "
						 + " 	4 as \"type\", "
						 + " 	tcm.company_id as \"companyId\", "
						 + " 	tcm.company_desc as \"companyDesc\", "
						 + " 	ti.insurance_id as \"insuranceId\", "
						 + " 	ti.insurance_desc as \"insuranceDesc\" "
						 + " from "
						 + " 	billing.t_bill_details_payee_mapper tbdpm "
						 + " inner join billing.t_billing_details tbd on "
						 + " 	tbd.billing_details_id = tbdpm.bill_details_id "
						 + " inner join billing.m_billing_master bill on "
						 + " 	bill.billing_master_id = tbd.billing_master_id "
						 + " inner join service.m_service_master msm on "
						 + " 	tbd.service_id = msm.service_master_id "
						 + " inner join doctor.m_sub_speciality_master mssm on "
						 + " 	mssm.sub_speciality_id = msm.sub_speciality_id "
						 + " inner join doctor.m_speciality_master spl on "
						 + " 	msm.speciality_id = spl.speciality_id "
						 + " inner join t_company_master tcm on "
						 + " 	tcm.company_id = tbdpm.payee_id "
						 + " left join adt.t_insurance ti on "
						 + " 	ti.insurance_id = tbdpm.associate_company_id "
						 + " inner join t_unit_application_billing_configuration tuabc on "
						 + " 	tuabc.unit_id = tbdpm.unit_id 	 "
						 + " where "
						 + " 	tbd.organization_id = :orgId "
						 + " 	and tbd.unit_id = :unitId "
						 + " 	and to_char(bill.bill_date_time , '"+inputDateFormat+"') between :fromDate and :toDate "
						 + " 	and tbd.status = 'A' "
						 + " 	and bill.status = 'A' "
						 + " 	and tuabc.self_payee_id = tbdpm.payee_id  "
						 + " group by "
						 + " 	tbd.organization_id, "
						 + " 	tbd.unit_id, "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"'), "
						 + " 	tcm.company_id , "
						 + " 	ti.insurance_id, "
						 + " 	spl.speciality_id, "
						 + " 	mssm.sub_speciality_id, "
						 + " 	msm.service_master_id	 "
						 + " union "
						 + " select "
						 + " 	mssm.sub_speciality_id as \"subSpecialityId\", "
						 + " 	mssm.sub_speciality_name as \"subSpecialityName\", "
						 + " 	count(tbd.billing_details_id) as \"serviceWiseCount\", "
						 + " 	spl.speciality_id as \"specialityId\", "
						 + " 	spl.speciality_name as \"specialityName\", "
						 + " 	0 as \"serviceWiseNetAmount\", "
						 + " 	msm.service_standard_name as \"serviceStandardName\", "
						 + " 	msm.service_master_id \"serviceId\", "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"') as \"createdDate\", "
						 + " 	tbd.organization_id as \"orgId\", "
						 + " 	tbd.unit_id as \"unitId\", "
						 + " 	6 as \"type\", "
						 + " 	tcm.company_id as \"companyId\", "
						 + " 	tcm.company_desc as \"companyDesc\", "
						 + " 	ti.insurance_id as \"insuranceId\", "
						 + " 	ti.insurance_desc as \"insuranceDesc\" "
						 + " from "
						 + " 	billing.t_bill_details_payee_mapper tbdpm "
						 + " inner join billing.t_billing_details tbd on "
						 + " 	tbd.billing_details_id = tbdpm.bill_details_id "
						 + " inner join billing.m_billing_master bill on "
						 + " 	bill.billing_master_id = tbd.billing_master_id "
						 + " inner join service.m_service_master msm on "
						 + " 	tbd.service_id = msm.service_master_id "
						 + " inner join doctor.m_sub_speciality_master mssm on "
						 + " 	mssm.sub_speciality_id = msm.sub_speciality_id "
						 + " inner join doctor.m_speciality_master spl on "
						 + " 	msm.speciality_id = spl.speciality_id "
						 + " inner join t_company_master tcm on "
						 + " 	tcm.company_id = tbdpm.payee_id "
						 + " left join adt.t_insurance ti on "
						 + " 	ti.insurance_id = tbdpm.associate_company_id "
						 + " inner join t_unit_application_billing_configuration tuabc on "
						 + " 	tuabc.unit_id = tbdpm.unit_id 	 "
						 + " where "
						 + " 	tbd.organization_id = :orgId "
						 + " 	and tbd.unit_id = :unitId "
						 + " 	and to_char(bill.bill_date_time , '"+inputDateFormat+"') between :fromDate and :toDate "
						 + " 	and tbd.status = 'A' "
						 + " 	and bill.status = 'A' "
						 + " 	and tuabc.self_payee_id != tbdpm.payee_id  "
						 + " 	and ti.insurance_id is null "
						 + " group by "
						 + " 	tbd.organization_id, "
						 + " 	tbd.unit_id, "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"'), "
						 + " 	tcm.company_id , "
						 + " 	ti.insurance_id, "
						 + " 	spl.speciality_id, "
						 + " 	mssm.sub_speciality_id, "
						 + " 	msm.service_master_id "
						 + " union  "
						 + " select "
						 + " 	mssm.sub_speciality_id as \"subSpecialityId\", "
						 + " 	mssm.sub_speciality_name as \"subSpecialityName\", "
						 + " 	count(tbd.billing_details_id) as \"serviceWiseCount\", "
						 + " 	spl.speciality_id as \"specialityId\", "
						 + " 	spl.speciality_name as \"specialityName\", "
						 + " 	0 as \"serviceWiseNetAmount\", "
						 + " 	msm.service_standard_name as \"serviceStandardName\", "
						 + " 	msm.service_master_id \"serviceId\", "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"') as \"createdDate\", "
						 + " 	tbd.organization_id as \"orgId\", "
						 + " 	tbd.unit_id as \"unitId\", "
						 + " 	5 as \"type\", "
						 + " 	tcm.company_id as \"companyId\", "
						 + " 	tcm.company_desc as \"companyDesc\", "
						 + " 	ti.insurance_id as \"insuranceId\", "
						 + " 	ti.insurance_desc as \"insuranceDesc\" "
						 + " from "
						 + " 	billing.t_bill_details_payee_mapper tbdpm "
						 + " inner join billing.t_billing_details tbd on "
						 + " 	tbd.billing_details_id = tbdpm.bill_details_id "
						 + " inner join billing.m_billing_master bill on "
						 + " 	bill.billing_master_id = tbd.billing_master_id "
						 + " inner join service.m_service_master msm on "
						 + " 	tbd.service_id = msm.service_master_id "
						 + " inner join doctor.m_sub_speciality_master mssm on "
						 + " 	mssm.sub_speciality_id = msm.sub_speciality_id "
						 + " inner join doctor.m_speciality_master spl on "
						 + " 	msm.speciality_id = spl.speciality_id "
						 + " inner join t_company_master tcm on "
						 + " 	tcm.company_id = tbdpm.payee_id "
						 + " inner join adt.t_insurance ti on "
						 + " 	ti.insurance_id = tbdpm.associate_company_id "
						 + " inner join t_unit_application_billing_configuration tuabc on "
						 + " 	tuabc.unit_id = tbdpm.unit_id 	 "
						 + " where "
						 + " 	tbd.organization_id = :orgId "
						 + " 	and tbd.unit_id = :unitId "
						 + " 	and to_char(bill.bill_date_time , '"+inputDateFormat+"') between :fromDate and :toDate "
						 + " 	and tbd.status = 'A' "
						 + " 	and bill.status = 'A' "
						 + " 	and tuabc.self_payee_id != tbdpm.payee_id  "
						 + " group by "
						 + " 	tbd.organization_id, "
						 + " 	tbd.unit_id, "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"'), "
						 + " 	tcm.company_id , "
						 + " 	ti.insurance_id, "
						 + " 	spl.speciality_id, "
						 + " 	mssm.sub_speciality_id, "
						 + " 	msm.service_master_id "
						 + " order by "
						 + " 	\"orgId\", "
						 + " 	\"unitId\", "
						 + " 	\"createdDate\", "
						 + " 	\"type\", "
						 + " 	\"companyId\", "
						 + " 	\"insuranceId\", "
						 + " 	\"specialityId\", "
						 + " 	\"subSpecialityId\", "
						 + " 	\"serviceId\" "  ;

				StringBuffer subQuery = new StringBuffer();

				query = query.concat(subQuery.toString());
				List<BIReportsAllCollectionDto> listDto = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("unitId", bIReportsCommonDto.getUnitId())
						.setParameter("orgId", bIReportsCommonDto.getOrganizationId())
						.setParameter("fromDate", bIReportsCommonDto.getFromDate())
						.setParameter("toDate", bIReportsCommonDto.getToDate())
						.setResultTransformer(Transformers.aliasToBean(BIReportsAllCollectionDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, listDto, null);
			}else{
				String query =    " select "
						 + " 	mssm.sub_speciality_id as \"subSpecialityId\", "
						 + " 	mssm.sub_speciality_name as \"subSpecialityName\", "
						 + " 	0 as \"serviceWiseCount\", "
						 + " 	spl.speciality_id as \"specialityId\", "
						 + " 	spl.speciality_name as \"specialityName\", "
						 + " 	cast(sum(tbdpm.payee_amount) as numeric(10, 2)) as \"serviceWiseNetAmount\", "
						 + " 	msm.service_standard_name as \"serviceStandardName\", "
						 + " 	msm.service_master_id \"serviceId\", "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"') as \"createdDate\", "
						 + " 	tbd.organization_id as \"orgId\", "
						 //+ " 	tbd.unit_id as \"unitId\", "
						 + " 	1 as \"type\", "
						 + " 	tcm.company_id as \"companyId\", "
						 + " 	tcm.company_desc as \"companyDesc\", "
						 + " 	ti.insurance_id as \"insuranceId\", "
						 + " 	ti.insurance_desc as \"insuranceDesc\" "
						 + " from "
						 + " 	billing.t_bill_details_payee_mapper tbdpm "
						 + " inner join billing.t_billing_details tbd on "
						 + " 	tbd.billing_details_id = tbdpm.bill_details_id "
						 + " inner join billing.m_billing_master bill on "
						 + " 	bill.billing_master_id = tbd.billing_master_id "
						 + " inner join service.m_service_master msm on "
						 + " 	tbd.service_id = msm.service_master_id "
						 + " inner join doctor.m_sub_speciality_master mssm on "
						 + " 	mssm.sub_speciality_id = msm.sub_speciality_id "
						 + " inner join doctor.m_speciality_master spl on "
						 + " 	msm.speciality_id = spl.speciality_id "
						 + " inner join t_company_master tcm on "
						 + " 	tcm.company_id = tbdpm.payee_id "
						 + " left join adt.t_insurance ti on "
						 + " 	ti.insurance_id = tbdpm.associate_company_id "
						 + " inner join t_unit_application_billing_configuration tuabc on "
						 + " 	tuabc.unit_id = tbdpm.unit_id 	 "
						 + " where "
						 + " 	tbd.organization_id = :orgId "
						 //+ " 	and tbd.unit_id = :unitId "
						 + " 	and to_char(bill.bill_date_time , '"+inputDateFormat+"') between :fromDate and :toDate "
						 + " 	and tbd.status = 'A' "
						 + " 	and bill.status = 'A' "
						 + " 	and tbd.package_consumption_amt = 0 "
						 + " 	and tuabc.self_payee_id = tbdpm.payee_id  "
						 + " group by "
						 + " 	tbd.organization_id, "
						 //+ " 	tbd.unit_id, "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"'), "
						 + " 	tcm.company_id , "
						 + " 	ti.insurance_id, "
						 + " 	spl.speciality_id, "
						 + " 	mssm.sub_speciality_id, "
						 + " 	msm.service_master_id	 "
						 + " union "
						 + " select "
						 + " 	mssm.sub_speciality_id as \"subSpecialityId\", "
						 + " 	mssm.sub_speciality_name as \"subSpecialityName\", "
						 + " 	0 as \"serviceWiseCount\", "
						 + " 	spl.speciality_id as \"specialityId\", "
						 + " 	spl.speciality_name as \"specialityName\", "
						 + " 	cast(sum(tbdpm.payee_amount) as numeric(10, 2)) as \"serviceWiseNetAmount\", "
						 + " 	msm.service_standard_name as \"serviceStandardName\", "
						 + " 	msm.service_master_id \"serviceId\", "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"') as \"createdDate\", "
						 + " 	tbd.organization_id as \"orgId\", "
						 //+ " 	tbd.unit_id as \"unitId\", "
						 + " 	3 as \"type\", "
						 + " 	tcm.company_id as \"companyId\", "
						 + " 	tcm.company_desc as \"companyDesc\", "
						 + " 	ti.insurance_id as \"insuranceId\", "
						 + " 	ti.insurance_desc as \"insuranceDesc\" "
						 + " from "
						 + " 	billing.t_bill_details_payee_mapper tbdpm "
						 + " inner join billing.t_billing_details tbd on "
						 + " 	tbd.billing_details_id = tbdpm.bill_details_id "
						 + " inner join billing.m_billing_master bill on "
						 + " 	bill.billing_master_id = tbd.billing_master_id "
						 + " inner join service.m_service_master msm on "
						 + " 	tbd.service_id = msm.service_master_id "
						 + " inner join doctor.m_sub_speciality_master mssm on "
						 + " 	mssm.sub_speciality_id = msm.sub_speciality_id "
						 + " inner join doctor.m_speciality_master spl on "
						 + " 	msm.speciality_id = spl.speciality_id "
						 + " inner join t_company_master tcm on "
						 + " 	tcm.company_id = tbdpm.payee_id "
						 + " left join adt.t_insurance ti on "
						 + " 	ti.insurance_id = tbdpm.associate_company_id "
						 + " inner join t_unit_application_billing_configuration tuabc on "
						 + " 	tuabc.unit_id = tbdpm.unit_id 	 "
						 + " where "
						 + " 	tbd.organization_id = :orgId "
						 //+ " 	and tbd.unit_id = :unitId "
						 + " 	and to_char(bill.bill_date_time , '"+inputDateFormat+"') between :fromDate and :toDate "
						 + " 	and tbd.status = 'A' "
						 + " 	and bill.status = 'A' "
						 + " 	and tbd.package_consumption_amt = 0 "
						 + " 	and tuabc.self_payee_id != tbdpm.payee_id  "
						 + " 	and ti.insurance_id is null "
						 + " group by "
						 + " 	tbd.organization_id, "
						 //+ " 	tbd.unit_id, "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"'), "
						 + " 	tcm.company_id , "
						 + " 	ti.insurance_id, "
						 + " 	spl.speciality_id, "
						 + " 	mssm.sub_speciality_id, "
						 + " 	msm.service_master_id "
						 + " union  "
						 + " select "
						 + " 	mssm.sub_speciality_id as \"subSpecialityId\", "
						 + " 	mssm.sub_speciality_name as \"subSpecialityName\", "
						 + " 	0 as \"serviceWiseCount\", "
						 + " 	spl.speciality_id as \"specialityId\", "
						 + " 	spl.speciality_name as \"specialityName\", "
						 + " 	cast(sum(tbdpm.payee_amount) as numeric(10, 2)) as \"serviceWiseNetAmount\", "
						 + " 	msm.service_standard_name as \"serviceStandardName\", "
						 + " 	msm.service_master_id \"serviceId\", "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"') as \"createdDate\", "
						 + " 	tbd.organization_id as \"orgId\", "
						 //+ " 	tbd.unit_id as \"unitId\", "
						 + " 	2 as \"type\", "
						 + " 	tcm.company_id as \"companyId\", "
						 + " 	tcm.company_desc as \"companyDesc\", "
						 + " 	ti.insurance_id as \"insuranceId\", "
						 + " 	ti.insurance_desc as \"insuranceDesc\" "
						 + " from "
						 + " 	billing.t_bill_details_payee_mapper tbdpm "
						 + " inner join billing.t_billing_details tbd on "
						 + " 	tbd.billing_details_id = tbdpm.bill_details_id "
						 + " inner join billing.m_billing_master bill on "
						 + " 	bill.billing_master_id = tbd.billing_master_id "
						 + " inner join service.m_service_master msm on "
						 + " 	tbd.service_id = msm.service_master_id "
						 + " inner join doctor.m_sub_speciality_master mssm on "
						 + " 	mssm.sub_speciality_id = msm.sub_speciality_id "
						 + " inner join doctor.m_speciality_master spl on "
						 + " 	msm.speciality_id = spl.speciality_id "
						 + " inner join t_company_master tcm on "
						 + " 	tcm.company_id = tbdpm.payee_id "
						 + " inner join adt.t_insurance ti on "
						 + " 	ti.insurance_id = tbdpm.associate_company_id "
						 + " inner join t_unit_application_billing_configuration tuabc on "
						 + " 	tuabc.unit_id = tbdpm.unit_id 	 "
						 + " where "
						 + " 	tbd.organization_id = :orgId "
						 //+ " 	and tbd.unit_id = :unitId "
						 + " 	and to_char(bill.bill_date_time , '"+inputDateFormat+"') between :fromDate and :toDate "
						 + " 	and tbd.status = 'A' "
						 + " 	and bill.status = 'A' "
						 + " 	and tbd.package_consumption_amt = 0 "
						 + " 	and tuabc.self_payee_id != tbdpm.payee_id  "
						 + " 	and ti.insurance_id is not null "
						 + " group by "
						 + " 	tbd.organization_id, "
						 //+ " 	tbd.unit_id, "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"'), "
						 + " 	tcm.company_id , "
						 + " 	ti.insurance_id, "
						 + " 	spl.speciality_id, "
						 + " 	mssm.sub_speciality_id, "
						 + " 	msm.service_master_id "
						 + " union "
						 + " select "
						 + " 	mssm.sub_speciality_id as \"subSpecialityId\", "
						 + " 	mssm.sub_speciality_name as \"subSpecialityName\", "
						 + " 	count(tbd.billing_details_id) as \"serviceWiseCount\", "
						 + " 	spl.speciality_id as \"specialityId\", "
						 + " 	spl.speciality_name as \"specialityName\", "
						 + " 	0 as \"serviceWiseNetAmount\", "
						 + " 	msm.service_standard_name as \"serviceStandardName\", "
						 + " 	msm.service_master_id \"serviceId\", "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"') as \"createdDate\", "
						 + " 	tbd.organization_id as \"orgId\", "
						 //+ " 	tbd.unit_id as \"unitId\", "
						 + " 	4 as \"type\", "
						 + " 	tcm.company_id as \"companyId\", "
						 + " 	tcm.company_desc as \"companyDesc\", "
						 + " 	ti.insurance_id as \"insuranceId\", "
						 + " 	ti.insurance_desc as \"insuranceDesc\" "
						 + " from "
						 + " 	billing.t_bill_details_payee_mapper tbdpm "
						 + " inner join billing.t_billing_details tbd on "
						 + " 	tbd.billing_details_id = tbdpm.bill_details_id "
						 + " inner join billing.m_billing_master bill on "
						 + " 	bill.billing_master_id = tbd.billing_master_id "
						 + " inner join service.m_service_master msm on "
						 + " 	tbd.service_id = msm.service_master_id "
						 + " inner join doctor.m_sub_speciality_master mssm on "
						 + " 	mssm.sub_speciality_id = msm.sub_speciality_id "
						 + " inner join doctor.m_speciality_master spl on "
						 + " 	msm.speciality_id = spl.speciality_id "
						 + " inner join t_company_master tcm on "
						 + " 	tcm.company_id = tbdpm.payee_id "
						 + " left join adt.t_insurance ti on "
						 + " 	ti.insurance_id = tbdpm.associate_company_id "
						 + " inner join t_unit_application_billing_configuration tuabc on "
						 + " 	tuabc.unit_id = tbdpm.unit_id 	 "
						 + " where "
						 + " 	tbd.organization_id = :orgId "
						 //+ " 	and tbd.unit_id = :unitId "
						 + " 	and to_char(bill.bill_date_time , '"+inputDateFormat+"') between :fromDate and :toDate "
						 + " 	and tbd.status = 'A' "
						 + " 	and bill.status = 'A' "
						 + " 	and tuabc.self_payee_id = tbdpm.payee_id  "
						 + " group by "
						 + " 	tbd.organization_id, "
						// + " 	tbd.unit_id, "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"'), "
						 + " 	tcm.company_id , "
						 + " 	ti.insurance_id, "
						 + " 	spl.speciality_id, "
						 + " 	mssm.sub_speciality_id, "
						 + " 	msm.service_master_id	 "
						 + " union "
						 + " select "
						 + " 	mssm.sub_speciality_id as \"subSpecialityId\", "
						 + " 	mssm.sub_speciality_name as \"subSpecialityName\", "
						 + " 	count(tbd.billing_details_id) as \"serviceWiseCount\", "
						 + " 	spl.speciality_id as \"specialityId\", "
						 + " 	spl.speciality_name as \"specialityName\", "
						 + " 	0 as \"serviceWiseNetAmount\", "
						 + " 	msm.service_standard_name as \"serviceStandardName\", "
						 + " 	msm.service_master_id \"serviceId\", "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"') as \"createdDate\", "
						 + " 	tbd.organization_id as \"orgId\", "
						 //+ " 	tbd.unit_id as \"unitId\", "
						 + " 	6 as \"type\", "
						 + " 	tcm.company_id as \"companyId\", "
						 + " 	tcm.company_desc as \"companyDesc\", "
						 + " 	ti.insurance_id as \"insuranceId\", "
						 + " 	ti.insurance_desc as \"insuranceDesc\" "
						 + " from "
						 + " 	billing.t_bill_details_payee_mapper tbdpm "
						 + " inner join billing.t_billing_details tbd on "
						 + " 	tbd.billing_details_id = tbdpm.bill_details_id "
						 + " inner join billing.m_billing_master bill on "
						 + " 	bill.billing_master_id = tbd.billing_master_id "
						 + " inner join service.m_service_master msm on "
						 + " 	tbd.service_id = msm.service_master_id "
						 + " inner join doctor.m_sub_speciality_master mssm on "
						 + " 	mssm.sub_speciality_id = msm.sub_speciality_id "
						 + " inner join doctor.m_speciality_master spl on "
						 + " 	msm.speciality_id = spl.speciality_id "
						 + " inner join t_company_master tcm on "
						 + " 	tcm.company_id = tbdpm.payee_id "
						 + " left join adt.t_insurance ti on "
						 + " 	ti.insurance_id = tbdpm.associate_company_id "
						 + " inner join t_unit_application_billing_configuration tuabc on "
						 + " 	tuabc.unit_id = tbdpm.unit_id 	 "
						 + " where "
						 + " 	tbd.organization_id = :orgId "
						// + " 	and tbd.unit_id = :unitId "
						 + " 	and to_char(bill.bill_date_time , '"+inputDateFormat+"') between :fromDate and :toDate "
						 + " 	and tbd.status = 'A' "
						 + " 	and bill.status = 'A' "
						 + " 	and tuabc.self_payee_id != tbdpm.payee_id  "
						 + " 	and ti.insurance_id is null "
						 + " group by "
						 + " 	tbd.organization_id, "
						 //+ " 	tbd.unit_id, "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"'), "
						 + " 	tcm.company_id , "
						 + " 	ti.insurance_id, "
						 + " 	spl.speciality_id, "
						 + " 	mssm.sub_speciality_id, "
						 + " 	msm.service_master_id "
						 + " union  "
						 + " select "
						 + " 	mssm.sub_speciality_id as \"subSpecialityId\", "
						 + " 	mssm.sub_speciality_name as \"subSpecialityName\", "
						 + " 	count(tbd.billing_details_id) as \"serviceWiseCount\", "
						 + " 	spl.speciality_id as \"specialityId\", "
						 + " 	spl.speciality_name as \"specialityName\", "
						 + " 	0 as \"serviceWiseNetAmount\", "
						 + " 	msm.service_standard_name as \"serviceStandardName\", "
						 + " 	msm.service_master_id \"serviceId\", "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"') as \"createdDate\", "
						 + " 	tbd.organization_id as \"orgId\", "
						 //+ " 	tbd.unit_id as \"unitId\", "
						 + " 	5 as \"type\", "
						 + " 	tcm.company_id as \"companyId\", "
						 + " 	tcm.company_desc as \"companyDesc\", "
						 + " 	ti.insurance_id as \"insuranceId\", "
						 + " 	ti.insurance_desc as \"insuranceDesc\" "
						 + " from "
						 + " 	billing.t_bill_details_payee_mapper tbdpm "
						 + " inner join billing.t_billing_details tbd on "
						 + " 	tbd.billing_details_id = tbdpm.bill_details_id "
						 + " inner join billing.m_billing_master bill on "
						 + " 	bill.billing_master_id = tbd.billing_master_id "
						 + " inner join service.m_service_master msm on "
						 + " 	tbd.service_id = msm.service_master_id "
						 + " inner join doctor.m_sub_speciality_master mssm on "
						 + " 	mssm.sub_speciality_id = msm.sub_speciality_id "
						 + " inner join doctor.m_speciality_master spl on "
						 + " 	msm.speciality_id = spl.speciality_id "
						 + " inner join t_company_master tcm on "
						 + " 	tcm.company_id = tbdpm.payee_id "
						 + " inner join adt.t_insurance ti on "
						 + " 	ti.insurance_id = tbdpm.associate_company_id "
						 + " inner join t_unit_application_billing_configuration tuabc on "
						 + " 	tuabc.unit_id = tbdpm.unit_id 	 "
						 + " where "
						 + " 	tbd.organization_id = :orgId "
						 //+ " 	and tbd.unit_id = :unitId "
						 + " 	and to_char(bill.bill_date_time , '"+inputDateFormat+"') between :fromDate and :toDate "
						 + " 	and tbd.status = 'A' "
						 + " 	and bill.status = 'A' "
						 + " 	and tuabc.self_payee_id != tbdpm.payee_id  "
						 + " group by "
						 + " 	tbd.organization_id, "
						 //+ " 	tbd.unit_id, "
						 + " 	to_char(bill.bill_date_time , '"+inputDateFormat+"'), "
						 + " 	tcm.company_id , "
						 + " 	ti.insurance_id, "
						 + " 	spl.speciality_id, "
						 + " 	mssm.sub_speciality_id, "
						 + " 	msm.service_master_id "
						 + " order by "
						 + " 	\"orgId\", "
						 //+ " 	\"unitId\", "
						 + " 	\"createdDate\", "
						 + " 	\"type\", "
						 + " 	\"companyId\", "
						 + " 	\"insuranceId\", "
						 + " 	\"specialityId\", "
						 + " 	\"subSpecialityId\", "
						 + " 	\"serviceId\" "  ;

				StringBuffer subQuery = new StringBuffer();

				query = query.concat(subQuery.toString());
				List<BIReportsAllCollectionDto> listDto = sessionFactory.getCurrentSession().createSQLQuery(query)
						//.setParameter("unitId", bIReportsCommonDto.getUnitId())
						.setParameter("orgId", bIReportsCommonDto.getOrganizationId())
						.setParameter("fromDate", bIReportsCommonDto.getFromDate())
						.setParameter("toDate", bIReportsCommonDto.getToDate())
						.setResultTransformer(Transformers.aliasToBean(BIReportsAllCollectionDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, listDto, null);
			}

		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getEntitlementWiseData failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getExpenseData(BIReportsCommonDto bIReportsCommonDto, String inputDateFormat)throws ApplicationException {
		try {

			if(bIReportsCommonDto.getUnitId()!=null)
			{
				String query = " select "
						 + " 	cast(sum(me.amount) as numeric(10, 2)) as \"expenseNet\", "
						 + " 	0 as \"expenseCount\", "
						 + " 	acc.general_ledger_name as \"generalLedgerName\", "
						 + " 	acc.general_ledger_id as \"generalLedgerId\", "
						 + " 	me.bill_type as \"billType\", "
						 + " 	me.unit_id as \"unitId\", "
						 + " 	me.org_id as \"orgId\", "
						 + " 	to_char(me.bill_date_time,'"+inputDateFormat+"') as \"createdDate\" , "
						 + " 	1 as \"type\" "
						 + " from "
						 + " 	billing.t_miscellaneous_expense me "
						 + " inner join m_general_ledger_master acc on "
						 + " 	me.account_head = acc.general_ledger_id "
						 + " where "
						 + " 	me.is_cancelled = 'N' "
						 + " 	and me.org_id =:orgId "
						 + " 	and me.unit_id =:unitId "
						 + " 	and to_char(me.bill_date_time , '"+inputDateFormat+"') between :fromDate and :toDate "
						 + " group by "
						 + " 	me.org_id, "
						 + " 	me.unit_id, "
						 + " 	me.bill_type, "
						 + " 	to_char(me.bill_date_time,'"+inputDateFormat+"'), "
						 + " 	acc.general_ledger_id "
						 + " union  "
						 + " select "
						 + " 	0 as \"expenseNet\", "
						 + " 	count(me.expense_id) as \"expenseCount\", "
						 + " 	acc.general_ledger_name as \"generalLedgerName\", "
						 + " 	acc.general_ledger_id as \"generalLedgerId\", "
						 + " 	me.bill_type as \"billType\", "
						 + " 	me.unit_id as \"unitId\", "
						 + " 	me.org_id as \"orgId\", "
						 + " 	to_char(me.bill_date_time,'"+inputDateFormat+"') as \"createdDate\", "
						 + " 	2 as \"type\" "
						 + " from "
						 + " 	billing.t_miscellaneous_expense me "
						 + " inner join m_general_ledger_master acc on "
						 + " 	me.account_head = acc.general_ledger_id "
						 + " where "
						 + " 	me.is_cancelled = 'N' "
						 + " 	and me.org_id =:orgId "
						 + " 	and me.unit_id =:unitId "
						 + " 	and to_char(me.bill_date_time , '"+inputDateFormat+"') between :fromDate and :toDate "
						 + " group by "
						 + " 	me.org_id, "
						 + " 	me.unit_id, "
						 + " 	me.bill_type, "
						 + " 	to_char(me.bill_date_time,'"+inputDateFormat+"'), "
						 + " 	acc.general_ledger_id "
						 + " order by 	 "
						 + " 	\"orgId\", "
						 + " 	\"unitId\", "
						 + " 	\"type\", "
						 + " 	\"billType\", "
						 + " 	\"createdDate\", "
						 + " 	\"generalLedgerId\" ";

				StringBuffer subQuery = new StringBuffer();

				query = query.concat(subQuery.toString());
				List<BIReportsAllCollectionDto> listDto = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("unitId", bIReportsCommonDto.getUnitId())
						.setParameter("orgId", bIReportsCommonDto.getOrganizationId())
						.setParameter("fromDate", bIReportsCommonDto.getFromDate())
						.setParameter("toDate", bIReportsCommonDto.getToDate())
						.setResultTransformer(Transformers.aliasToBean(BIReportsAllCollectionDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, listDto, null);
			}else{
				String query = " select "
						 + " 	cast(sum(me.amount) as numeric(10, 2)) as \"expenseNet\", "
						 + " 	0 as \"expenseCount\", "
						 + " 	acc.general_ledger_name as \"generalLedgerName\", "
						 + " 	acc.general_ledger_id as \"generalLedgerId\", "
						 + " 	me.bill_type as \"billType\", "
						 //+ " 	me.unit_id as \"unitId\", "
						 + " 	me.org_id as \"orgId\", "
						 + " 	to_char(me.bill_date_time,'"+inputDateFormat+"') as \"createdDate\" , "
						 + " 	1 as \"type\" "
						 + " from "
						 + " 	billing.t_miscellaneous_expense me "
						 + " inner join m_general_ledger_master acc on "
						 + " 	me.account_head = acc.general_ledger_id "
						 + " where "
						 + " 	me.is_cancelled = 'N' "
						 + " 	and me.org_id =:orgId "
						 //+ " 	and me.unit_id =:unitId "
						 + " 	and to_char(me.bill_date_time , '"+inputDateFormat+"') between :fromDate and :toDate "
						 + " group by "
						 + " 	me.org_id, "
						 //+ " 	me.unit_id, "
						 + " 	me.bill_type, "
						 + " 	to_char(me.bill_date_time,'"+inputDateFormat+"'), "
						 + " 	acc.general_ledger_id "
						 + " union  "
						 + " select "
						 + " 	0 as \"expenseNet\", "
						 + " 	count(me.expense_id) as \"expenseCount\", "
						 + " 	acc.general_ledger_name as \"generalLedgerName\", "
						 + " 	acc.general_ledger_id as \"generalLedgerId\", "
						 + " 	me.bill_type as \"billType\", "
						 //+ " 	me.unit_id as \"unitId\", "
						 + " 	me.org_id as \"orgId\", "
						 + " 	to_char(me.bill_date_time,'"+inputDateFormat+"') as \"createdDate\", "
						 + " 	2 as \"type\" "
						 + " from "
						 + " 	billing.t_miscellaneous_expense me "
						 + " inner join m_general_ledger_master acc on "
						 + " 	me.account_head = acc.general_ledger_id "
						 + " where "
						 + " 	me.is_cancelled = 'N' "
						 + " 	and me.org_id =:orgId "
						// + " 	and me.unit_id =:unitId "
						 + " 	and to_char(me.bill_date_time , '"+inputDateFormat+"') between :fromDate and :toDate "
						 + " group by "
						 + " 	me.org_id, "
						 //+ " 	me.unit_id, "
						 + " 	me.bill_type, "
						 + " 	to_char(me.bill_date_time,'"+inputDateFormat+"'), "
						 + " 	acc.general_ledger_id "
						 + " order by 	 "
						 + " 	\"orgId\", "
						 //+ " 	\"unitId\", "
						 + " 	\"type\", "
						 + " 	\"billType\", "
						 + " 	\"createdDate\", "
						 + " 	\"generalLedgerId\" ";

				StringBuffer subQuery = new StringBuffer();

				query = query.concat(subQuery.toString());
				List<BIReportsAllCollectionDto> listDto = sessionFactory.getCurrentSession().createSQLQuery(query)
						//.setParameter("unitId", bIReportsCommonDto.getUnitId())
						.setParameter("orgId", bIReportsCommonDto.getOrganizationId())
						.setParameter("fromDate", bIReportsCommonDto.getFromDate())
						.setParameter("toDate", bIReportsCommonDto.getToDate())
						.setResultTransformer(Transformers.aliasToBean(BIReportsAllCollectionDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, listDto, null);
			}



		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getExpenseData failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getStatusWiseTestCount(BIReportsCommonDto bIReportsCommonDto, String inputDateFormat)throws ApplicationException {
		try {

			if(bIReportsCommonDto.getUnitId()!=null)
			{
				String query =   " select  "
						 + "  	count(lab_sample_dtls_id) as \"serviceWiseCount\",  "
						 + "  	mtm.test_id as \"serviceId\",  "
						 + "  	mtm.test_desc as \"serviceStandardName\",  "
						 + "  	mssm.sub_speciality_id as \"subSpecialityId\",  "
						 + "  	mssm.sub_speciality_name as \"subSpecialityName\",  "
						 + "  	tlsd.org_id as \"orgId\",  "
						 + "  	tlsd.org_unit_id as \"unitId\",  "
						 + "  	to_char(tlsd.created_date, '"+inputDateFormat+"') as \"createdDate\",  "
						 + "  	mtsm.test_status_id as \"statusId\",  "
						 + "  	mtsm.status_name as \"status\" , "
						 + "  	1 as \"type\" "
						 + "  from  "
						 + "  	lab.t_lab_sample_dtls tlsd  "
						 + "  inner join lab.m_test_master mtm on  "
						 + "  	mtm.test_id = tlsd.test_id  "
						 + "  inner join lab.m_test_status_master mtsm on  "
						 + "  	mtsm.test_status_id = tlsd.sample_status_id  "
						 + "  inner join doctor.m_sub_speciality_master mssm on  "
						 + "  	mssm.sub_speciality_id = tlsd.sub_dept_id  "
						 + "  where  "
						 + "  	tlsd.org_id =:orgId  "
						 + "  	and tlsd.org_unit_id =:unitId  "
						 + "  	and to_char(tlsd.created_date, '"+inputDateFormat+"') between :fromDate and :toDate  "
						 + "  group by  "
						 + "  	\"orgId\",  "
						 + "  	\"unitId\",  "
						 + "  	\"createdDate\",  "
						 + "  	\"subSpecialityId\",  "
						 + "  	\"statusId\",  "
						 + "  	\"serviceId\"  "
						 + "  union  "
						 + "  select  "
						 + "  	count(lab_sample_dtls_id) as \"serviceWiseCount\",  "
						 + "  		mtm.test_id as \"serviceId\",  "
						 + "  	mtm.test_desc as \"serviceStandardName\",  "
						 + "  	mssm.sub_speciality_id as \"subSpecialityId\",  "
						 + "  	mssm.sub_speciality_name as \"subSpecialityName\",  "
						 + "  	tod.org_id as \"orgId\",  "
						 + "  	tod.org_unit_id as \"unitId\",  "
						 + "  	to_char(tod.created_date, '"+inputDateFormat+"') as \"createdDate\",  "
						 + "  	1 as \"statusId\",  "
						 + "  	'Pending' as \"status\" , "
						 + "  	1 as \"type\" "
						 + "  from  "
						 + "  	t_order_details tod  "
						 + "  inner join lab.m_test_master mtm on  "
						 + "  	tod.service_id = mtm.service_id  "
						 + "  inner join service.m_service_master ser on  "
						 + "  	ser.service_master_id = mtm.service_id  "
						 + "  left join lab.t_lab_sample_dtls tlsd on  "
						 + "  	tod.order_details_id = tlsd.order_details_id  "
						 + "  inner join doctor.m_sub_speciality_master mssm on  "
						 + "  	mssm.sub_speciality_id = ser.sub_speciality_id  "
						 + "  where  "
						 + "  	tlsd.lab_sample_dtls_id is null  "
						 + "  	and tlsd.org_id =:orgId  "
						 + "  	and tlsd.org_unit_id =:unitId  "
						 + "  	and to_char(tod.created_date, '"+inputDateFormat+"') between :fromDate and :toDate  "
						 + "	and tod.status ='A' "
						 + "  group by  "
						 + "  	\"orgId\",  "
						 + "  	\"unitId\",  "
						 + "  	\"createdDate\",  "
						 + "  	\"subSpecialityId\",  "
						 + "  	\"statusId\",  "
						 + "  	\"serviceId\" "
						 + " union  "
						 + " select "
						 + " 	count(tod.order_details_id) as \"serviceWiseCount\", "
						 + " 	msm.service_master_id as \"serviceId\", "
						 + " 	msm.service_standard_name as \"serviceStandardName\", "
						 + " 	mssm.sub_speciality_id as \"subSpecialityId\", "
						 + " 	mssm.sub_speciality_name as \"subSpecialityName\", "
						 + " 	tod.org_id as \"orgId\", "
						 + " 	tod.org_unit_id as \"unitId\", "
						 + " 	to_char(tod.created_date, '"+inputDateFormat+"') as \"createdDate\", "
						 + " 	1 as \"statusId\", "
						 + " 	'Pending' as \"status\", "
						 + " 	2 as \"type\" "
						 + " from "
						 + " 	t_order_details tod "
						 + " inner join service.m_service_master msm on "
						 + " 	msm.service_master_id = tod.service_id "
						 + " inner join doctor.m_sub_speciality_master mssm on "
						 + " 	mssm.sub_speciality_id = msm.sub_speciality_id "
						 + " left join t_template_transaction ttt on "
						 + " 	ttt.order_details_id = tod.order_details_id "
						 + " where "
						 + " 	msm.speciality_id = ( "
						 + " 	select "
						 + " 		tuabc.radiology_speciality_id "
						 + " 	from "
						 + " 		t_unit_application_billing_configuration tuabc "
						 + " 	where "
						 + " 		unit_id =:unitId ) "
						 + " 	and ttt.template_transaction_id is null "
						 + " 	and to_char(tod.created_date, '"+inputDateFormat+"') between :fromDate and :toDate "
						 + " 	and tod.org_id =:orgId "
						 + " 	and tod.org_unit_id =:unitId "
						 + "	and tod.status ='A' "
						 + " group by "
						 + " 	\"orgId\", "
						 + " 	\"unitId\", "
						 + " 	\"createdDate\", "
						 + " 	\"subSpecialityId\", "
						 + " 	\"serviceId\" "
						 + " union "
						 + " select "
						 + " 	count(tod.order_details_id) as \"serviceWiseCount\", "
						 + " 	msm.service_master_id as \"serviceId\", "
						 + " 	msm.service_standard_name as \"serviceStandardName\", "
						 + " 	mssm.sub_speciality_id as \"subSpecialityId\", "
						 + " 	mssm.sub_speciality_name as \"subSpecialityName\", "
						 + " 	tod.org_id as \"orgId\", "
						 + " 	tod.org_unit_id as \"unitId\", "
						 + " 	to_char(tod.created_date, '"+inputDateFormat+"') as \"createdDate\", "
						 + " 	2 as \"statusId\", "
						 + " 	'Provisional Release' as \"status\", "
						 + " 	2 as \"type\" "
						 + " from "
						 + " 	t_order_details tod "
						 + " inner join service.m_service_master msm on "
						 + " 	msm.service_master_id = tod.service_id "
						 + " inner join doctor.m_sub_speciality_master mssm on "
						 + " 	mssm.sub_speciality_id = msm.sub_speciality_id "
						 + " inner join t_template_transaction ttt on "
						 + " 	ttt.order_details_id = tod.order_details_id "
						 + " where "
						 + " 	msm.speciality_id = ( "
						 + " 	select "
						 + " 		tuabc.radiology_speciality_id "
						 + " 	from "
						 + " 		t_unit_application_billing_configuration tuabc "
						 + " 	where "
						 + " 		unit_id =:unitId ) "
						 + " 	and ttt.is_provisional_release = 'Y' "
						 + " 	and (ttt.is_report_release is null "
						 + " 	or ttt.is_report_release = 'N') "
						 + " 	and (ttt.is_final_release is null "
						 + " 	or ttt.is_final_release = 'N') "
						 + " 	and to_char(tod.created_date, '"+inputDateFormat+"') between :fromDate and :toDate "
						 + " 	and tod.org_id =:orgId "
						 + " 	and tod.org_unit_id =:unitId "
						 + "	and tod.status ='A' "
						 + " group by "
						 + " 	\"orgId\", "
						 + " 	\"unitId\", "
						 + " 	\"createdDate\", "
						 + " 	\"subSpecialityId\", "
						 + " 	\"serviceId\" "
						 + " union "
						 + " select "
						 + " 	count(tod.order_details_id) as \"serviceWiseCount\", "
						 + " 	msm.service_master_id as \"serviceId\", "
						 + " 	msm.service_standard_name as \"serviceStandardName\", "
						 + " 	mssm.sub_speciality_id as \"subSpecialityId\", "
						 + " 	mssm.sub_speciality_name as \"subSpecialityName\", "
						 + " 	tod.org_id as \"orgId\", "
						 + " 	tod.org_unit_id as \"unitId\", "
						 + " 	to_char(tod.created_date, '"+inputDateFormat+"') as \"createdDate\", "
						 + " 	3 as \"statusId\", "
						 + " 	'Final Release' as \"status\", "
						 + " 	2 as \"type\" "
						 + " from "
						 + " 	t_order_details tod "
						 + " inner join service.m_service_master msm on "
						 + " 	msm.service_master_id = tod.service_id "
						 + " inner join doctor.m_sub_speciality_master mssm on "
						 + " 	mssm.sub_speciality_id = msm.sub_speciality_id "
						 + " inner join t_template_transaction ttt on "
						 + " 	ttt.order_details_id = tod.order_details_id "
						 + " where "
						 + " 	msm.speciality_id = ( "
						 + " 	select "
						 + " 		tuabc.radiology_speciality_id "
						 + " 	from "
						 + " 		t_unit_application_billing_configuration tuabc "
						 + " 	where "
						 + " 		unit_id =:unitId ) "
						 + " 	and (ttt.is_report_release is null "
						 + " 	or ttt.is_report_release = 'N') "
						 + " 	and ttt.is_final_release = 'Y' "
						 + " 	and to_char(tod.created_date, '"+inputDateFormat+"') between :fromDate and :toDate "
						 + " 	and tod.org_id =:orgId "
						 + " 	and tod.org_unit_id =:unitId "
						 + "	and tod.status ='A' "
						 + " group by "
						 + " 	\"orgId\", "
						 + " 	\"unitId\", "
						 + " 	\"createdDate\", "
						 + " 	\"subSpecialityId\", "
						 + " 	\"serviceId\" "
						 + " union "
						 + " select "
						 + " 	count(tod.order_details_id) as \"serviceWiseCount\", "
						 + " 	msm.service_master_id as \"serviceId\", "
						 + " 	msm.service_standard_name as \"serviceStandardName\", "
						 + " 	mssm.sub_speciality_id as \"subSpecialityId\", "
						 + " 	mssm.sub_speciality_name as \"subSpecialityName\", "
						 + " 	tod.org_id as \"orgId\", "
						 + " 	tod.org_unit_id as \"unitId\", "
						 + " 	to_char(tod.created_date, '"+inputDateFormat+"') as \"createdDate\", "
						 + " 	4 as \"statusId\", "
						 + " 	'Report Release' as \"status\", "
						 + " 	2 as \"type\" "
						 + " from "
						 + " 	t_order_details tod "
						 + " inner join service.m_service_master msm on "
						 + " 	msm.service_master_id = tod.service_id "
						 + " inner join doctor.m_sub_speciality_master mssm on "
						 + " 	mssm.sub_speciality_id = msm.sub_speciality_id "
						 + " inner join t_template_transaction ttt on "
						 + " 	ttt.order_details_id = tod.order_details_id "
						 + " where "
						 + " 	msm.speciality_id = ( "
						 + " 	select "
						 + " 		tuabc.radiology_speciality_id "
						 + " 	from "
						 + " 		t_unit_application_billing_configuration tuabc "
						 + " 	where "
						 + " 		unit_id =:unitId ) "
						 + " 	and ttt.is_report_release = 'Y' "
						 + " 	and to_char(tod.created_date, '"+inputDateFormat+"') between :fromDate and :toDate "
						 + " 	and tod.org_id =:orgId "
						 + " 	and tod.org_unit_id =:unitId "
						 + "	and tod.status ='A' "
						 + " group by "
						 + " 	\"orgId\", "
						 + " 	\"unitId\", "
						 + " 	\"createdDate\", "
						 + " 	\"subSpecialityId\", "
						 + " 	\"serviceId\" "
						 + " order by  "
						 + "  	\"type\",  "
						 + " 	\"orgId\",  "
						 + "  	\"unitId\",  "
						 + "  	\"createdDate\",  "
						 + "  	\"subSpecialityId\",  "
						 + "  	\"statusId\",  "
						 + "  	\"serviceId\" "  ;

				StringBuffer subQuery = new StringBuffer();

				query = query.concat(subQuery.toString());
				List<BIReportsAllCollectionDto> listDto = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("unitId", bIReportsCommonDto.getUnitId())
						.setParameter("orgId", bIReportsCommonDto.getOrganizationId())
						.setParameter("fromDate", bIReportsCommonDto.getFromDate())
						.setParameter("toDate", bIReportsCommonDto.getToDate())
						.setResultTransformer(Transformers.aliasToBean(BIReportsAllCollectionDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, listDto, null);
			}else{
				String query =   " select  "
						 + "  	count(lab_sample_dtls_id) as \"serviceWiseCount\",  "
						 + "  	mtm.test_id as \"serviceId\",  "
						 + "  	mtm.test_desc as \"serviceStandardName\",  "
						 + "  	mssm.sub_speciality_id as \"subSpecialityId\",  "
						 + "  	mssm.sub_speciality_name as \"subSpecialityName\",  "
						 + "  	tlsd.org_id as \"orgId\",  "
						 //+ "  	tlsd.org_unit_id as \"unitId\",  "
						 + "  	to_char(tlsd.created_date, '"+inputDateFormat+"') as \"createdDate\",  "
						 + "  	mtsm.test_status_id as \"statusId\",  "
						 + "  	mtsm.status_name as \"status\" , "
						 + "  	1 as \"type\" "
						 + "  from  "
						 + "  	lab.t_lab_sample_dtls tlsd  "
						 + "  inner join lab.m_test_master mtm on  "
						 + "  	mtm.test_id = tlsd.test_id  "
						 + "  inner join lab.m_test_status_master mtsm on  "
						 + "  	mtsm.test_status_id = tlsd.sample_status_id  "
						 + "  inner join doctor.m_sub_speciality_master mssm on  "
						 + "  	mssm.sub_speciality_id = tlsd.sub_dept_id  "
						 + "  where  "
						 + "  	tlsd.org_id =:orgId  "
						 //+ "  	and tlsd.org_unit_id =:unitId  "
						 + "  	and to_char(tlsd.created_date, '"+inputDateFormat+"') between :fromDate and :toDate  "
						 + "  group by  "
						 + "  	\"orgId\",  "
						 //+ "  	\"unitId\",  "
						 + "  	\"createdDate\",  "
						 + "  	\"subSpecialityId\",  "
						 + "  	\"statusId\",  "
						 + "  	\"serviceId\"  "
						 + "  union  "
						 + "  select  "
						 + "  	count(lab_sample_dtls_id) as \"serviceWiseCount\",  "
						 + "  		mtm.test_id as \"serviceId\",  "
						 + "  	mtm.test_desc as \"serviceStandardName\",  "
						 + "  	mssm.sub_speciality_id as \"subSpecialityId\",  "
						 + "  	mssm.sub_speciality_name as \"subSpecialityName\",  "
						 + "  	tod.org_id as \"orgId\",  "
						 //+ "  	tod.org_unit_id as \"unitId\",  "
						 + "  	to_char(tod.created_date, '"+inputDateFormat+"') as \"createdDate\",  "
						 + "  	1 as \"statusId\",  "
						 + "  	'Pending' as \"status\" , "
						 + "  	1 as \"type\" "
						 + "  from  "
						 + "  	t_order_details tod  "
						 + "  inner join lab.m_test_master mtm on  "
						 + "  	tod.service_id = mtm.service_id  "
						 + "  inner join service.m_service_master ser on  "
						 + "  	ser.service_master_id = mtm.service_id  "
						 + "  left join lab.t_lab_sample_dtls tlsd on  "
						 + "  	tod.order_details_id = tlsd.order_details_id  "
						 + "  inner join doctor.m_sub_speciality_master mssm on  "
						 + "  	mssm.sub_speciality_id = ser.sub_speciality_id  "
						 + "  where  "
						 + "  	tlsd.lab_sample_dtls_id is null  "
						 + "  	and tlsd.org_id =:orgId  "
						 //+ "  	and tlsd.org_unit_id =:unitId  "
						 + "  	and to_char(tod.created_date, '"+inputDateFormat+"') between :fromDate and :toDate  "
						 + "	and tod.status ='A' "
						 + "  group by  "
						 + "  	\"orgId\",  "
						 //+ "  	\"unitId\",  "
						 + "  	\"createdDate\",  "
						 + "  	\"subSpecialityId\",  "
						 + "  	\"statusId\",  "
						 + "  	\"serviceId\" "
						 + " union  "
						 + " select "
						 + " 	count(tod.order_details_id) as \"serviceWiseCount\", "
						 + " 	msm.service_master_id as \"serviceId\", "
						 + " 	msm.service_standard_name as \"serviceStandardName\", "
						 + " 	mssm.sub_speciality_id as \"subSpecialityId\", "
						 + " 	mssm.sub_speciality_name as \"subSpecialityName\", "
						 + " 	tod.org_id as \"orgId\", "
						// + " 	tod.org_unit_id as \"unitId\", "
						 + " 	to_char(tod.created_date, '"+inputDateFormat+"') as \"createdDate\", "
						 + " 	1 as \"statusId\", "
						 + " 	'Pending' as \"status\", "
						 + " 	2 as \"type\" "
						 + " from "
						 + " 	t_order_details tod "
						 + " inner join service.m_service_master msm on "
						 + " 	msm.service_master_id = tod.service_id "
						 + " inner join doctor.m_sub_speciality_master mssm on "
						 + " 	mssm.sub_speciality_id = msm.sub_speciality_id "
						 + " left join t_template_transaction ttt on "
						 + " 	ttt.order_details_id = tod.order_details_id "
						 + " where "
						 + " 	msm.speciality_id = ( "
						 + " 	select "
						 + " 		tuabc.radiology_speciality_id "
						 + " 	from "
						 + " 		t_unit_application_billing_configuration tuabc "
						 + " 	where "
						 + " 		unit_id = tod.org_unit_id ) "
						 + " 	and ttt.template_transaction_id is null "
						 + " 	and to_char(tod.created_date, '"+inputDateFormat+"') between :fromDate and :toDate "
						 + " 	and tod.org_id =:orgId "
						 //+ " 	and tod.org_unit_id =:unitId "
						 + "	and tod.status ='A' "
						 + " group by "
						 + " 	\"orgId\", "
						 //+ " 	\"unitId\", "
						 + " 	\"createdDate\", "
						 + " 	\"subSpecialityId\", "
						 + " 	\"serviceId\" "
						 + " union "
						 + " select "
						 + " 	count(tod.order_details_id) as \"serviceWiseCount\", "
						 + " 	msm.service_master_id as \"serviceId\", "
						 + " 	msm.service_standard_name as \"serviceStandardName\", "
						 + " 	mssm.sub_speciality_id as \"subSpecialityId\", "
						 + " 	mssm.sub_speciality_name as \"subSpecialityName\", "
						 + " 	tod.org_id as \"orgId\", "
						 //+ " 	tod.org_unit_id as \"unitId\", "
						 + " 	to_char(tod.created_date, '"+inputDateFormat+"') as \"createdDate\", "
						 + " 	2 as \"statusId\", "
						 + " 	'Provisional Release' as \"status\", "
						 + " 	2 as \"type\" "
						 + " from "
						 + " 	t_order_details tod "
						 + " inner join service.m_service_master msm on "
						 + " 	msm.service_master_id = tod.service_id "
						 + " inner join doctor.m_sub_speciality_master mssm on "
						 + " 	mssm.sub_speciality_id = msm.sub_speciality_id "
						 + " inner join t_template_transaction ttt on "
						 + " 	ttt.order_details_id = tod.order_details_id "
						 + " where "
						 + " 	msm.speciality_id = ( "
						 + " 	select "
						 + " 		tuabc.radiology_speciality_id "
						 + " 	from "
						 + " 		t_unit_application_billing_configuration tuabc "
						 + " 	where "
						 + " 		unit_id = tod.org_unit_id ) "
						 + " 	and ttt.is_provisional_release = 'Y' "
						 + " 	and (ttt.is_report_release is null "
						 + " 	or ttt.is_report_release = 'N') "
						 + " 	and (ttt.is_final_release is null "
						 + " 	or ttt.is_final_release = 'N') "
						 + " 	and to_char(tod.created_date, '"+inputDateFormat+"') between :fromDate and :toDate "
						 + " 	and tod.org_id =:orgId "
						 //+ " 	and tod.org_unit_id =:unitId "
						 + "	and tod.status ='A' "
						 + " group by "
						 + " 	\"orgId\", "
						 //+ " 	\"unitId\", "
						 + " 	\"createdDate\", "
						 + " 	\"subSpecialityId\", "
						 + " 	\"serviceId\" "
						 + " union "
						 + " select "
						 + " 	count(tod.order_details_id) as \"serviceWiseCount\", "
						 + " 	msm.service_master_id as \"serviceId\", "
						 + " 	msm.service_standard_name as \"serviceStandardName\", "
						 + " 	mssm.sub_speciality_id as \"subSpecialityId\", "
						 + " 	mssm.sub_speciality_name as \"subSpecialityName\", "
						 + " 	tod.org_id as \"orgId\", "
						 //+ " 	tod.org_unit_id as \"unitId\", "
						 + " 	to_char(tod.created_date, '"+inputDateFormat+"') as \"createdDate\", "
						 + " 	3 as \"statusId\", "
						 + " 	'Final Release' as \"status\", "
						 + " 	2 as \"type\" "
						 + " from "
						 + " 	t_order_details tod "
						 + " inner join service.m_service_master msm on "
						 + " 	msm.service_master_id = tod.service_id "
						 + " inner join doctor.m_sub_speciality_master mssm on "
						 + " 	mssm.sub_speciality_id = msm.sub_speciality_id "
						 + " inner join t_template_transaction ttt on "
						 + " 	ttt.order_details_id = tod.order_details_id "
						 + " where "
						 + " 	msm.speciality_id = ( "
						 + " 	select "
						 + " 		tuabc.radiology_speciality_id "
						 + " 	from "
						 + " 		t_unit_application_billing_configuration tuabc "
						 + " 	where "
						 + " 		unit_id = tod.org_unit_id ) "
						 + " 	and (ttt.is_report_release is null "
						 + " 	or ttt.is_report_release = 'N') "
						 + " 	and ttt.is_final_release = 'Y' "
						 + " 	and to_char(tod.created_date, '"+inputDateFormat+"') between :fromDate and :toDate "
						 + " 	and tod.org_id =:orgId "
						 //+ " 	and tod.org_unit_id =:unitId "
						 + "	and tod.status ='A' "
						 + " group by "
						 + " 	\"orgId\", "
						// + " 	\"unitId\", "
						 + " 	\"createdDate\", "
						 + " 	\"subSpecialityId\", "
						 + " 	\"serviceId\" "
						 + " union "
						 + " select "
						 + " 	count(tod.order_details_id) as \"serviceWiseCount\", "
						 + " 	msm.service_master_id as \"serviceId\", "
						 + " 	msm.service_standard_name as \"serviceStandardName\", "
						 + " 	mssm.sub_speciality_id as \"subSpecialityId\", "
						 + " 	mssm.sub_speciality_name as \"subSpecialityName\", "
						 + " 	tod.org_id as \"orgId\", "
						 //+ " 	tod.org_unit_id as \"unitId\", "
						 + " 	to_char(tod.created_date, '"+inputDateFormat+"') as \"createdDate\", "
						 + " 	4 as \"statusId\", "
						 + " 	'Report Release' as \"status\", "
						 + " 	2 as \"type\" "
						 + " from "
						 + " 	t_order_details tod "
						 + " inner join service.m_service_master msm on "
						 + " 	msm.service_master_id = tod.service_id "
						 + " inner join doctor.m_sub_speciality_master mssm on "
						 + " 	mssm.sub_speciality_id = msm.sub_speciality_id "
						 + " inner join t_template_transaction ttt on "
						 + " 	ttt.order_details_id = tod.order_details_id "
						 + " where "
						 + " 	msm.speciality_id = ( "
						 + " 	select "
						 + " 		tuabc.radiology_speciality_id "
						 + " 	from "
						 + " 		t_unit_application_billing_configuration tuabc "
						 + " 	where "
						 + " 		unit_id = tod.org_unit_id ) "
						 + " 	and ttt.is_report_release = 'Y' "
						 + " 	and to_char(tod.created_date, '"+inputDateFormat+"') between :fromDate and :toDate "
						 + " 	and tod.org_id =:orgId "
						 //+ " 	and tod.org_unit_id =:unitId "
						 + "	and tod.status ='A' "
						 + " group by "
						 + " 	\"orgId\", "
						 //+ " 	\"unitId\", "
						 + " 	\"createdDate\", "
						 + " 	\"subSpecialityId\", "
						 + " 	\"serviceId\" "
						 + " order by  "
						 + "  	\"type\",  "
						 + " 	\"orgId\",  "
						 //+ "  	\"unitId\",  "
						 + "  	\"createdDate\",  "
						 + "  	\"subSpecialityId\",  "
						 + "  	\"statusId\",  "
						 + "  	\"serviceId\" "  ;

				StringBuffer subQuery = new StringBuffer();

				query = query.concat(subQuery.toString());
				List<BIReportsAllCollectionDto> listDto = sessionFactory.getCurrentSession().createSQLQuery(query)
						//.setParameter("unitId", bIReportsCommonDto.getUnitId())
						.setParameter("orgId", bIReportsCommonDto.getOrganizationId())
						.setParameter("fromDate", bIReportsCommonDto.getFromDate())
						.setParameter("toDate", bIReportsCommonDto.getToDate())
						.setResultTransformer(Transformers.aliasToBean(BIReportsAllCollectionDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, listDto, null);
			}



		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getStatusWiseTestCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getBIReportsRoomOccupancy(BIReportsCommonDto bIReportsCommonDto) throws ApplicationException {
		try {
			String inputDateFormat = "yyyy-MM-dd";

			if(bIReportsCommonDto.getPeriod().equals("daily") || bIReportsCommonDto.getPeriod().equals("yesterday") )
				inputDateFormat = "yyyy-MM-dd";
			if(bIReportsCommonDto.getPeriod().equals("monthly"))
				inputDateFormat = "yyyy-MM";
	        else
	        	inputDateFormat = "yyyy-MM-dd";


			List<BIBedDetailsReportDto> listDto=null;
			if(bIReportsCommonDto.getUnitId()!=null  && bIReportsCommonDto.getUnitId().intValue()>0) {
				String query = "select "
						+ "    ta.organization_id as \"organizationId\", "
						+ "    ta.unit_id as \"unitId\", "
						+ "    mbcm.bed_category_desc as \"bedCategoryDesc\", mbcm.bed_category_id as \"bedCategoryId\", to_char(tad.doa,'"+inputDateFormat+"') as \"doa\", "
						+ "    concat(cast((cast(count(mbcm.bed_category_id) as float8) / cast((select count(admission_id) from adt.t_admission ta2 where status = 'A') as float8)* 100) as numeric (10, 2)), ' %') as \"percentage\" "
						+ "from "
						+ "    adt.t_admission ta "
						+ "inner join adt.t_admission_details tad on "
						+ "    tad.admission_id = ta.admission_id "
						+ "inner join adt.m_bed_category_master mbcm on "
						+ "    tad.bed_category_id = mbcm.bed_category_id "
						+ "where "
						+ "    tad.status = 'A' "
						+ "    and to_char(tad.doa,'"+inputDateFormat+"') between :fromDate and :toDate "
						+ "    and ta.organization_id=:orgId "
						+ "    and ta.unit_id =:unitId "
						+ "group by "
						+ "    ta.organization_id, "
						+ "    ta.unit_id, "
						+ "    to_char(tad.doa,'"+inputDateFormat+"'), "
						+ "    mbcm.bed_category_id "
						+ "order by "
						+ "    ta.organization_id, "
						+ "    ta.unit_id, "
						+ "    to_char(tad.doa,'"+inputDateFormat+"') ";

				 listDto = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("orgId", bIReportsCommonDto.getOrganizationId())
						.setParameter("unitId", bIReportsCommonDto.getUnitId())
						.setParameter("fromDate", bIReportsCommonDto.getFromDate())
						.setParameter("toDate", bIReportsCommonDto.getToDate())
						.setResultTransformer(Transformers.aliasToBean(BIBedDetailsReportDto.class)).list();
			}else {
				String query = "select "
						+ "    ta.organization_id as \"organizationId\", "
//						+ "    ta.unit_id as \"unitId\", "
						+ "    mbcm.bed_category_desc as \"bedCategoryDesc\", mbcm.bed_category_id as \"bedCategoryId\", to_char(tad.doa,'"+inputDateFormat+"') as \"doa\", "
						+ "    concat(cast((cast(count(mbcm.bed_category_id) as float8) / cast((select count(admission_id) from adt.t_admission ta2 where status = 'A') as float8)* 100) as numeric (10, 2)), ' %') as \"percentage\" "
						+ "from "
						+ "    adt.t_admission ta "
						+ "inner join adt.t_admission_details tad on "
						+ "    tad.admission_id = ta.admission_id "
						+ "inner join adt.m_bed_category_master mbcm on "
						+ "    tad.bed_category_id = mbcm.bed_category_id "
						+ "where "
						+ "    tad.status = 'A' "
						+ "    and to_char(tad.doa,'"+inputDateFormat+"') between :fromDate and :toDate "
						+ "    and ta.organization_id=:orgId "
//						+ "    and ta.unit_id =:unitId "
						+ "group by "
						+ "    ta.organization_id, "
//						+ "    ta.unit_id, "
						+ "    to_char(tad.doa,'"+inputDateFormat+"'), "
						+ "    mbcm.bed_category_id "
						+ "order by "
						+ "    ta.organization_id, "
//						+ "    ta.unit_id, "
						+ "    to_char(tad.doa,'"+inputDateFormat+"') ";

						listDto = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("orgId", bIReportsCommonDto.getOrganizationId())
//						.setParameter("unitId", bIReportsCommonDto.getUnitId())
						.setParameter("fromDate", bIReportsCommonDto.getFromDate())
						.setParameter("toDate", bIReportsCommonDto.getToDate())
						.setResultTransformer(Transformers.aliasToBean(BIBedDetailsReportDto.class)).list();
			}


			return new Response(SUCCESS, SUCCESS_CODE, null, listDto, null);

		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getBIReportsRoomOccupancy failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getBIReportsForDashboard(BIReportsCommonDto bIReportsCommonDto)
			throws ApplicationException {
		try {
			String inputDateFormat = "yyyy-MM-dd";

			if(bIReportsCommonDto.getPeriod().equals("daily") || bIReportsCommonDto.getPeriod().equals("yesterday") )
				inputDateFormat = "yyyy-MM-dd";
			if(bIReportsCommonDto.getPeriod().equals("monthly"))
				inputDateFormat = "yyyy-MM";
	        else
	        	inputDateFormat = "yyyy-MM-dd";


			List<BIBedDetailsReportDto> listDto=null;
			if(bIReportsCommonDto.getUnitId()!=null  && bIReportsCommonDto.getUnitId().intValue()>0) {
				String query = " select "
						+ "ta.organization_id as \"organizationId\", "
						+ "ta.unit_id as \"unitId\", "
						+ "mbcm.bed_category_desc as \"bedCategoryDesc\", "
						+ "mbcm.bed_category_id as \"bedCategoryId\", "
						+ "to_char(tad.doa, '"+inputDateFormat+"') as \"createdDate\", "
						+ "concat(cast((cast(count(mbcm.bed_category_id) as float8) / cast((select count(admission_id) from adt.t_admission ta2 where status = 'A') as float8)* 100) as numeric (10, 2)), ' %') as \"bedOccupancy\" , "
						+ "( "
						+ "    select "
						+ "       concat(cast((cast(count(ta.admission_id) as float8) / cast((select count(mbm.bed_id) from adt.m_bed_master as mbm where mbm.status = 'A' and mbm.unit_id =:unitId and mbm.organization_id=:orgId) as float8)* 100) as numeric (10, 2)), ' %')) as \"totalOccupancy\", "
						+ "( "
						+ "   select count(td.discharge_id) "
						+ "      from  adt.t_discharge td "
						+ "      where td.status = 'A' "
						+ "         and td.discharge_status_id = 5 "
						+ "         and td.unit_id =:unitId "
						+ "         and td.organization_id =:orgId "
						+ "         and to_char(td.updated_date , '"+inputDateFormat+"') between :fromDate and :toDate ) as \"dischargeCount\", "
						+ " ( select  count(ta.admission_id) "
						+ "   from adt.t_admission as ta "
						+ "    where ta.status = 'A' "
						+ "      and ta.unit_id =:unitId "
						+ "      and ta.organization_id =:orgId "
						+ "      and to_char(ta.created_date , '"+inputDateFormat+"') between :fromDate and :toDate ) as \"admissionCount\" "
						+ "   from adt.t_admission ta "
						+ "            inner join adt.t_admission_details tad on   tad.admission_id = ta.admission_id "
						+ "            inner join adt.m_bed_category_master mbcm on     tad.bed_category_id = mbcm.bed_category_id "
						+ "            inner join adt.m_bed_master bm on     bm.bed_id = tad.bed_id "
						+ "            where tad.status = 'A' "
						+ "               and ta.status = 'A' "
						+ "                and to_char(tad.doa, '"+inputDateFormat+"') between :fromDate and :toDate "
						+ "                and ta.organization_id =:orgId "
						+ "                and ta.unit_id =:unitId "
						+ "   group by "
						+ "            ta.organization_id, "
						+ "            ta.unit_id, "
						+ "            to_char(tad.doa, '"+inputDateFormat+"'), "
						+ "            mbcm.bed_category_id "
						+ "  order by "
						+ "		ta.organization_id, "
						+ "		ta.unit_id, "
						+ "		to_char(tad.doa, '"+inputDateFormat+"')";

				 listDto = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("orgId", bIReportsCommonDto.getOrganizationId())
						.setParameter("unitId", bIReportsCommonDto.getUnitId())
						.setParameter("fromDate", bIReportsCommonDto.getFromDate())
						.setParameter("toDate", bIReportsCommonDto.getToDate())
						.setResultTransformer(Transformers.aliasToBean(BIBedDetailsReportDto.class)).list();
			}else {
				String query = " select "
						+ "ta.organization_id as \"organizationId\", "
//						+ "ta.unit_id as \"unitId\", "
						+ "mbcm.bed_category_desc as \"bedCategoryDesc\", "
						+ "mbcm.bed_category_id as \"bedCategoryId\", "
						+ "to_char(tad.doa, '"+inputDateFormat+"') as \"createdDate\", "
						+ "concat(cast((cast(count(mbcm.bed_category_id) as float8) / cast((select count(admission_id) from adt.t_admission ta2 where status = 'A') as float8)* 100) as numeric (10, 2)), ' %') as \"bedOccupancy\" , "
						+ "( "
						+ "    select "
						+ "       concat(cast((cast(count(ta.admission_id) as float8) / cast((select count(mbm.bed_id) from adt.m_bed_master as mbm where mbm.status = 'A'  and mbm.organization_id=:orgId) as float8)* 100) as numeric (10, 2)), ' %')) as \"totalOccupancy\", "
						+ "( "
						+ "   select count(td.discharge_id) "
						+ "      from  adt.t_discharge td "
						+ "      where td.status = 'A' "
						+ "         and td.discharge_status_id = 5 "
//						+ "         and td.unit_id =:unitId "
						+ "         and td.organization_id =:orgId "
						+ "         and to_char(td.updated_date , '"+inputDateFormat+"') between :fromDate and :toDate ) as \"dischargeCount\", "
						+ " ( select  count(ta.admission_id) "
						+ "   from adt.t_admission as ta "
						+ "    where ta.status = 'A' "
//						+ "      and ta.unit_id =:unitId "
						+ "      and ta.organization_id =:orgId "
						+ "      and to_char(ta.created_date , '"+inputDateFormat+"') between :fromDate and :toDate ) as \"admissionCount\" "
						+ "   from adt.t_admission ta "
						+ "            inner join adt.t_admission_details tad on   tad.admission_id = ta.admission_id "
						+ "            inner join adt.m_bed_category_master mbcm on     tad.bed_category_id = mbcm.bed_category_id "
						+ "            inner join adt.m_bed_master bm on     bm.bed_id = tad.bed_id "
						+ "            where tad.status = 'A' "
						+ "               and ta.status = 'A' "
						+ "                and to_char(tad.doa, '"+inputDateFormat+"') between :fromDate and :toDate "
						+ "                and ta.organization_id =:orgId "
//						+ "                and ta.unit_id =:unitId "
						+ "   group by "
						+ "            ta.organization_id, "
//						+ "            ta.unit_id, "
						+ "            to_char(tad.doa, '"+inputDateFormat+"'), "
						+ "            mbcm.bed_category_id "
						+ "  order by "
						+ "		ta.organization_id, "
//						+ "		ta.unit_id, "
						+ "		to_char(tad.doa, '"+inputDateFormat+"')";

				 listDto = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("orgId", bIReportsCommonDto.getOrganizationId())
//						.setParameter("unitId", bIReportsCommonDto.getUnitId())
						.setParameter("fromDate", bIReportsCommonDto.getFromDate())
						.setParameter("toDate", bIReportsCommonDto.getToDate())
						.setResultTransformer(Transformers.aliasToBean(BIBedDetailsReportDto.class)).list();
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, listDto, null);		
			
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getBIReportsForDashboard failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}	
	
}