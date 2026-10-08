package com.abm.mainet.common.master.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.abm.mainet.common.dao.AbstractDAO;
import com.abm.mainet.common.domain.ContractDetailEntity;

@Repository
public class ContractAgreementDaoImpl extends AbstractDAO<Long> implements IContractAgreementDao {

    private static Logger logger = Logger.getLogger(ContractAgreementDaoImpl.class);

    @Override
    public List<Object[]> getContractAgreementSummary(final Long orgId, final String contractNo, final Date contractDate,
            final Long deptId,
            final Long venderId, final String viewClosedCon, final String contRenewal) {

        String queryString = "	select  c.contId,c.contNo,dept.dpDeptdesc,c.contDate,d.contFromDate,d.contToDate, p1.contp1Name,ven.vmVendorname,c.contTndNo,c.loaNo,d.contAmount from "
                +
                " ContractMastEntity c ,ContractDetailEntity d,ContractPart1DetailEntity p1 ,ContractPart2DetailEntity p2, Department dept,TbAcVendormasterEntity ven "
                +
                " where c.contId=d.contId and c.contId=p1.contId and c.contId=p2.contId and dept.dpDeptid=c.contDept "
                + " and ven.vmVendorid=p2.vmVendorid and p1.contp1Type='U' and p2.contp2Type='V' and c.contActive='Y' and d.contdActive='Y' "
                + " and p2.contp2Primary='Y' and p1.contp1Active='Y' and p2.contvActive='Y'  and c.orgId=:orgId";

        if (contractNo != null && !contractNo.isEmpty()) {
            queryString += " and c.contNo =:contractNo";
        }

        if (contractDate != null) {
            queryString += " and c.contDate =:contractDate";
        }

        if (deptId != null) {
            queryString += " and p1.dpDeptid =:deptId";
        }

        if (venderId != null) {
            queryString += " and p2.vmVendorid =:venderId";
        }
        if (!StringUtils.isEmpty(viewClosedCon)) {
            queryString += " and c.contCloseFlag =:contCloseFlag";
        }
        if (!StringUtils.isEmpty(contRenewal)) {
            queryString += " and c.contRenewal =:contRenewal";
        }

        final Query query = createQuery(queryString);
        query.setParameter("orgId", orgId);
        if (contractNo != null && !contractNo.isEmpty()) {
            query.setParameter("contractNo", contractNo);
        }
        if (contractDate != null) {
            query.setParameter("contractDate", contractDate);
        }
        if (deptId != null) {
            query.setParameter("deptId", deptId);

        }
        if (venderId != null) {
            query.setParameter("venderId", venderId);
        }
        if (!StringUtils.isEmpty(viewClosedCon)) {
            query.setParameter("contCloseFlag", viewClosedCon);
        }
        // D#90462
        if (!StringUtils.isEmpty(contRenewal)) {
            query.setParameter("contRenewal", contRenewal);
        }
        final List<Object[]> list = query.getResultList();

        return list;
    }

    @Override
    public List<Object[]> findPrintContractAgreementByContId(final Long orgId, final Long contId) {
        final String queryString = "select o.ONlsOrgname,o.oNlsOrgnameMar,o.orgShortNm,cm.contId,cm.contTndNo,cm.contTndDate,cm.contRsoNo,cm.contRsoDate,"
                + "cd.contFromDate, cd.contToDate,cd.contAmount, ep.empname ,de.dsgname from ContractMastEntity cm ,ContractDetailEntity cd,ContractPart1DetailEntity p1 ,"
                + "ContractPart2DetailEntity p2 ,Organisation o , Employee ep , Designation de "
                + "where o.orgid= ?1 and  cm.contId = ?2 and o.orgid= p1.orgId and o.orgid = p2.orgId and p1.contId.contId= p2.contId.contId"
                + " and cm.contId=p1.contId.contId  and cm.contId = cd.contId.contId and de.dsgid = p1.dsgid and ep.empId=p1.empid "
                + "and cd.contdActive='Y' and p1.contp1Type='U' and p1.contp1Active='Y' "
                + "and p2.contp2Type='V' and p2.contvActive='Y' and p2.contp2Primary='Y' ";

        final Query query = createQuery(queryString);
        query.setParameter(1, orgId);
        query.setParameter(2, contId);
        @SuppressWarnings("unchecked")
        final List<Object[]> list = query.getResultList();

        return list;
    }

    @Override
    public List<Object[]> findByContractNo(final Long orgId, final String contNo) {

        final String queryString = "select c.contId,c.contNo,dept.dpDeptdesc,c.contDate,d.contFromDate,d.contToDate, p1.contp1Name,ven.vmVendorname,ven.vmVendoradd,ven.emailId,ven.mobileNo,d.contAmount , p2.contp2Name,dept.dpDeptcode from "
                +
                " ContractMastEntity c ,ContractDetailEntity d,ContractPart1DetailEntity p1 ,ContractPart2DetailEntity p2, Department dept,TbAcVendormasterEntity ven "
                +
                " where c.contId=d.contId and c.contId=p1.contId and c.contId=p2.contId and dept.dpDeptid=c.contDept "
                + " and ven.vmVendorid=p2.vmVendorid and p1.contp1Type='U' and p2.contp2Type='V' and c.contActive='Y' "
                + " and p2.contp2Primary='Y' and p1.contp1Active='Y' and p2.contvActive='Y' and c.orgId= ?1 and c.contNo = ?2";

        final Query query = createQuery(queryString);
        query.setParameter(1, orgId);
        query.setParameter(2, contNo);
        @SuppressWarnings("unchecked")
        final List<Object[]> list = query.getResultList();

        return list;
    }

    @Override
    public ContractDetailEntity saveUpdateContractDetailEntity(ContractDetailEntity contractDetailEntity) {
        return entityManager.merge(contractDetailEntity);
    }

    @Override
    public void updateContractDetailActiveFlag(Long contdId, Long empId) {
        final StringBuilder hql = new StringBuilder(
                "UPDATE ContractDetailEntity a set a.contdActive='N', a.updatedBy=:empId, a.updatedDate =:CURRENT_DATE WHERE a.contdId=:contdId");
        final Query query = createQuery(hql.toString());
        query.setParameter("contdId", contdId);
        query.setParameter("empId", empId);
        query.setParameter("CURRENT_DATE", new Date());
        query.executeUpdate();
    }

    @Override
    public List<Object[]> getContractFilterData(Long orgId, String contractNo, Date contractDate, String viewClosedCon) {
        String queryString = "select c.contId,c.contTndNo,c.loaNo,c.contDate,d.contFromDate,d.contToDate from "
                +
                " ContractMastEntity c ,ContractDetailEntity d "
                +

                " where  d.contId.contId = c.contId and  c.contActive='Y' and d.contdActive='Y' and c.contNo is null and  c.orgId=:orgId";

        if (contractDate != null) {
            queryString += " and c.contDate =:contractDate";
        }

        final Query query = createQuery(queryString);
        query.setParameter("orgId", orgId);

        if (contractDate != null) {
            query.setParameter("contractDate", contractDate);
        }

        final List<Object[]> list = query.getResultList();

        return list;
    }

}
