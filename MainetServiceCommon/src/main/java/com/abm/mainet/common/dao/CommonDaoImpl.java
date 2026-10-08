package com.abm.mainet.common.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Vivek.Kumar
 * @since 06-Feb-2016
 */
@Repository
public class CommonDaoImpl implements CommonDao {

    private static final Logger LOGGER = Logger.getLogger(CommonDaoImpl.class);

    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Object> getSequenceProc(final Object[] ipValues, final int[] sqlTypes) {

        final int inCount = ipValues.length;

        final List<Object> outParamList = new ArrayList<>();

        final StringBuilder procedureString = new StringBuilder("call PR_SQ_GENERATION(");

        for (int i = 0; i < (ipValues.length + sqlTypes.length); ++i) {
            if (i > 0) {
                procedureString.append(",");
            }

            procedureString.append("?");
        }

        procedureString.append(")");

        CallableStatement callableStatement = null;

        try {

            final SessionImplementor sessionImplementor = (SessionImplementor) entityManager.getDelegate();

            callableStatement = sessionImplementor.connection().prepareCall(procedureString.toString());

            for (int i = 0; i < sqlTypes.length; i++) {
                final int j = inCount + (i + 1);
                callableStatement.registerOutParameter(j, sqlTypes[i]);
            }

            for (int i = 0; i < ipValues.length; i++) {
                callableStatement.setObject((i + 1), ipValues[i]);
            }

            callableStatement.executeUpdate();

            for (int i = (inCount + 1); i <= (inCount + sqlTypes.length); i++) {
                final Object object = callableStatement.getObject(i);
                outParamList.add(object);

            }

        } catch (final SQLException ex) {
            LOGGER.error("Exception while calling PR_SQ_GENERATION " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(callableStatement);
        }

        return outParamList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Object> getJavaSequenceProc(final Object[] ipValues, final int[] sqlTypes) {

        final int inCount = ipValues.length;
        final List<Object> outParamList = new ArrayList<>();
        final StringBuilder procedureString = new StringBuilder("call PR_JAVA_SQ_GENERATION(");

        for (int i = 0; i < (ipValues.length + sqlTypes.length); ++i) {
            if (i > 0) {
                procedureString.append(",");
            }
            procedureString.append("?");
        }

        procedureString.append(")");
        CallableStatement callableStatement = null;
        try {

            final SessionImplementor sessionImplementor = (SessionImplementor) entityManager.getDelegate();
            callableStatement = sessionImplementor.connection().prepareCall(procedureString.toString());

            for (int i = 0; i < sqlTypes.length; i++) {
                final int j = inCount + (i + 1);
                callableStatement.registerOutParameter(j, sqlTypes[i]);
            }

            for (int i = 0; i < ipValues.length; i++) {
                callableStatement.setObject((i + 1), ipValues[i]);
            }

            callableStatement.executeUpdate();

            for (int i = (inCount + 1); i <= (inCount + sqlTypes.length); i++) {
                final Object object = callableStatement.getObject(i);
                outParamList.add(object);
            }

        } catch (final SQLException ex) {
            LOGGER.error("Exception while calling PR_JAVA_SQ_GENERATION " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(callableStatement);
        }

        return outParamList;
    }
    
    
    //To generate the customize sequence number
    //added by @Sadik.shaikh
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Object> getCustSequenceProc(final Object[] ipValues, final int[] sqlTypes) {

        final int inCount = ipValues.length;
        final List<Object> outParamList = new ArrayList<>();
        final StringBuilder procedureString = new StringBuilder("call PR_NW_SQ_GENERATION(");

        for (int i = 0; i < (ipValues.length + sqlTypes.length); ++i) {
            if (i > 0) {
                procedureString.append(",");
            }
            procedureString.append("?");
        }

        procedureString.append(")");
        CallableStatement callableStatement = null;
        try {

            final SessionImplementor sessionImplementor = (SessionImplementor) entityManager.getDelegate();
            callableStatement = sessionImplementor.connection().prepareCall(procedureString.toString());

            for (int i = 0; i < sqlTypes.length; i++) {
                final int j = inCount + (i + 1);
                callableStatement.registerOutParameter(j, sqlTypes[i]);
            }

            for (int i = 0; i < ipValues.length; i++) {
                callableStatement.setObject((i + 1), ipValues[i]);
            }

            callableStatement.executeUpdate();

            for (int i = (inCount + 1); i <= (inCount + sqlTypes.length); i++) {
                final Object object = callableStatement.getObject(i);
                outParamList.add(object);
            }

        } catch (final SQLException ex) {
            LOGGER.error("Exception while calling PR_NW_SQ_GENERATION " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(callableStatement);
        }
        LOGGER.info("Custom Sequence number to be return"+outParamList);
        return outParamList;
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateFlatNoChange(Object[] ipValues, int[] sqlTypes) {


        final int inCount = ipValues.length;


        final StringBuilder procedureString = new StringBuilder("call PR_ASS_FLATNO_CHANGE(");

        for (int i = 0; i < (ipValues.length); ++i) {
            if (i > 0) {
                procedureString.append(",");
            }

            procedureString.append("?");
        }

        procedureString.append(")");

        CallableStatement callableStatement = null;

        try {

            final SessionImplementor sessionImplementor = (SessionImplementor) entityManager.getDelegate();

            callableStatement = sessionImplementor.connection().prepareCall(procedureString.toString());

			/*
			 * for (int i = 0; i < sqlTypes.length; i++) { final int j = inCount + (i + 1);
			 * callableStatement.registerOutParameter(j, sqlTypes[i]); }
			 */

            for (int i = 0; i < ipValues.length; i++) {
                callableStatement.setObject((i + 1), ipValues[i]);
            }

            callableStatement.executeUpdate();


        } catch (final SQLException ex) {
            LOGGER.error("Exception while calling PR_ASS_FLATNO_CHANGE " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(callableStatement);
        }

    
		
	}
	
	public void callProcedureToDeleteDuplicateGeneratedBills(String ward, String zone) {
		LOGGER.error("starting call method callProcedureToDeleteDuplicateGeneratedBills()");
		CallableStatement callableStatement = null;
		try {
			String endDate = null;
			final SessionImplementor sessionImplementor = (SessionImplementor) entityManager.getDelegate();
			Connection connection = sessionImplementor.connection();
			PreparedStatement stmt = connection.prepareStatement("SELECT fn_wt_bill_sch_end_dt()");
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				endDate = rs.getString(1);
			}
			LOGGER.error("procedure pr_wt_del_dup_non_mtr_bill call initiated");
			callableStatement = connection.prepareCall("{call pr_wt_del_dup_non_mtr_bill(?, ?, ?)}");
			callableStatement.setString(1, zone);
			callableStatement.setString(2, ward);
			callableStatement.setString(3, endDate);
			callableStatement.executeUpdate();
		} catch (final SQLException ex) {
			LOGGER.error("Exception while calling pr_wt_del_dup_non_mtr_bill " + ex.getMessage());
		}catch(Exception e) {
			LOGGER.error("Exception while calling pr_wt_del_dup_non_mtr_bill " + e.getMessage());
		} finally {
			DbUtils.closeQuietly(callableStatement);
		}

	}
    
}
