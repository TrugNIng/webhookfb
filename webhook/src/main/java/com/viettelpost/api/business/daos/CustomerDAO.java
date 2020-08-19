package com.viettelpost.api.business.daos;

import com.viettelpost.api.base.BaseDAO;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CustomerDAO extends BaseDAO {
    Logger logger = Logger.getLogger(CustomerDAO.class);

    @Autowired
    @Qualifier("customerFactory")
    SessionFactory customerFactory;

    @Transactional(rollbackFor = Exception.class)
    public String getReply(String text){
        String message = "";
        try{
            Query query = customerFactory.getCurrentSession().createSQLQuery("select " +
                    "(case when (select count(*) from message where upper(text) = upper(?)) > 0 then (select answer from message where upper(text)=upper(?))" +
                    "else (select answer from message where text = \"help\") end ) answer from dual")
                    .addScalar("answer", new StringType());
            query.setParameter(0, text);
            query.setParameter(1, text);
            return query.list().get(0).toString();
        } catch (Exception ex){
            logger.error(ex.getMessage(),ex);
        }
        return message;
    }

//    @Transactional(rollbackFor = Exception.class)
//    public boolean createAccount(Long cusId, String isdn, Long vplusId, String token){
//        try{
//            getResultSet(customerFactory,"ERP_WALLET.PKG_VPLUS.CREATE_ACCOUNT", Arrays.asList(cusId, isdn, vplusId, token), OracleTypes.NULL);
//            return true;
//        } catch (Exception ex){
//            logger.error(ex.getMessage(),ex);
//        }
//        return false;
//    }
//
//    @Transactional
//    public CustomerData getCustomerInfo(Long cusId){
//        CustomerData data = new CustomerData();
//        try{
//            ResultSet rs = (ResultSet) getResultSet(customerFactory,"ERP_WALLET.PKG_VPLUS.GET_TOKEN", Arrays.asList(cusId), OracleTypes.CURSOR);
//            while (rs.next()){
//                data.setCusId(rs.getLong("CUS_ID"));
//                data.setVplusId(rs.getLong("VPLUS_ID"));
//                data.setIsdn(rs.getString("ISDN"));
//                data.setName(rs.getString("NAME"));
//                data.setToken(rs.getString("TOKEN"));
//                data.setRanking(rs.getLong("RANKING"));
//                data.setConsumer(rs.getLong("CONSUMER"));
//                data.setUsed(rs.getLong("USED"));
//                data.setMappedDate(rs.getTimestamp("MAPPED_DATE"));
//            }
//        } catch (Exception ex){
//            logger.error(ex.getMessage(),ex);
//        }
//        return data;
//    }

}
