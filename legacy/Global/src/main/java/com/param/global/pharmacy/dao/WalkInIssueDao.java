package com.param.global.pharmacy.dao;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.Sale;
import com.param.global.common.ServiceException;
import com.param.service.global.SessionService;

import in.param.exception.ApplicationException;

@Repository
public class WalkInIssueDao extends AbstractDao<Integer, Issue> implements IWalkInIssueDao {

    private static final Logger logger = Logger.getLogger(WalkInIssueDao.class);

    @Autowired
    SessionFactory sessionFactory;
    
    @Autowired
    private SessionService sessionService;
    
    @Override
    public Issue saveIssue(Issue issue) throws ApplicationException{
    try {
        getSession().save(issue);
        return issue;
    }catch (Exception e) {
    logger.error("saveIssue failed", e);
    }
    return issue;
    }

	@Override
	public Issue updateSaleforIssueId(Issue issue, Sale sale) {
		try {
			
			String qry1 = null;
			
			 qry1 = " update pharmacy.t_sale  set issue_id =:issueId  where id =:id ";
			 sessionFactory.getCurrentSession()
				.createSQLQuery(qry1)
                     .setParameter("issueId",issue.getId())
                     .setParameter("id",sale.getId())
		        .executeUpdate();
			return issue;
		}catch(Exception e) {
		logger.error("updateSaleforIssueId failed", e);
		}
		return issue;
	}
}
