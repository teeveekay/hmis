/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.divudi.bean.common;

import com.divudi.data.BillClassType;
import com.divudi.data.BillNumberSuffix;
import com.divudi.data.BillType;
import com.divudi.entity.Bill;
import com.divudi.entity.BillNumber;
import com.divudi.entity.BilledBill;
import com.divudi.entity.CancelledBill;
import com.divudi.entity.Department;
import com.divudi.entity.Institution;
import com.divudi.entity.Logins;
import com.divudi.entity.PreBill;
import com.divudi.entity.RefundBill;
import com.divudi.entity.WebUser;
import com.divudi.facade.BillFacade;
import com.divudi.facade.BillNumberFacade;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.TemporalType;

/**
 *
 * @author Buddhika
 */
@Named
@ApplicationScoped
public class ApplicationController {

    @EJB
    private BillFacade billFacade;
    @EJB
    BillNumberFacade billNumberFacade;

    Date startTime;
    Date storesExpiery;
    private List<BillNumber> insBillNumbers;
    private List<BillNumber> depBillNumbers;

//    List<SessionController> sessionControllers;
//    public List<SessionController> getSessionControllers() {
//        return sessionControllers;
//    }
//
//    public void setSessionControllers(List<SessionController> sessionControllers) {
//        this.sessionControllers = sessionControllers;
//    }
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @PostConstruct
    public void recordStart() {
        startTime = Calendar.getInstance().getTime();
//        if (sessionControllers == null) {
//            sessionControllers = new ArrayList<>();
//        }
    }

    List<Logins> loggins;

    public List<Logins> getLoggins() {
        if (loggins == null) {
            loggins = new ArrayList<>();
        }
        return loggins;
    }

    public void setLoggins(List<Logins> loggins) {
        this.loggins = loggins;
    }

    public Logins isLogged(WebUser u) {
        Logins tl = null;
        for (Logins l : getLoggins()) {
            if (l.getWebUser().equals(u)) {
                tl = l;
            }
        }
        return tl;
    }

    public void addToLoggins(SessionController sc) {
        Logins login = sc.getThisLogin();
        if (loggins == null) {
            loggins = new ArrayList<>();
        }
        try {
            loggins.add(login);
//            for (SessionController s : getSessionControllers()) {
//                if (s.getLoggedUser().equals(login.getWebUser())) {
//                    ////System.out.println("making log out");
//                    s.logout();
//                }
//            }
//            getSessionControllers().add(sc);
        } catch (Exception e) {
        }
    }

    public void removeLoggins(SessionController sc) {
        Logins login = sc.getThisLogin();
        ////System.out.println("sessions logged before removing is " + getLoggins().size());
        loggins.remove(login);
//        sessionControllers.remove(sc);
    }

    /**
     * Creates a new instance of ApplicationController
     */
    public ApplicationController() {
    }

    public Date getStoresExpiery() {
        if (storesExpiery == null) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, 2020);
            c.set(Calendar.MONTH, 0);
            c.set(Calendar.DATE, 1);
            storesExpiery = c.getTime();
        }
        return storesExpiery;
    }

    public void setStoresExpiery(Date storesExpiery) {
        this.storesExpiery = storesExpiery;
    }

    private BillNumber fetchLastBillNumber(Institution institution, List<BillType> billTypes, BillClassType billClassType) {
        String sql = "SELECT b FROM "
                + " BillNumber b "
                + " where b.retired=false "
                + " and b.billClassType=:class "
                + " and b.institution=:ins "
                + " and b.billType is null ";
        HashMap hm = new HashMap();
        hm.put("class", billClassType);
        hm.put("ins", institution);
        BillNumber billNumber = billNumberFacade.findFirstBySQL(sql, hm);

        if (billNumber == null) {
            billNumber = new BillNumber();
            billNumber.setBillClassType(billClassType);
            billNumber.setInstitution(institution);
            HashMap m = new HashMap();

            sql = "SELECT count(b) FROM Bill b"
                    + "  where type(b)=:class "
                    + " and b.retired=false"
                    + " and b.institution=:ins "
                    + " and b.billType in :bt "
                    + " and b.createdAt is not null";

            hm.put("ins", institution);
            hm.put("bt", billTypes);

            switch (billClassType) {
                case BilledBill:
                    hm.put("class", BilledBill.class);
                    break;
                case CancelledBill:
                    hm.put("class", CancelledBill.class);
                    break;
                case RefundBill:
                    hm.put("class", RefundBill.class);
                    break;
                case PreBill:
                    hm.put("class", PreBill.class);
                    break;
            }

            Long dd = getBillFacade().findAggregateLong(sql, hm, TemporalType.DATE);

            if (dd == null) {
                dd = 0l;
            }

            billNumber.setLastBillNumber(dd);

            billNumberFacade.create(billNumber);
        }

        return billNumber;

    }

    public String institutionBillNumberGenerator(Department dep, BillType billType, BillClassType billClassType, BillNumberSuffix billNumberSuffix) {

        BillNumber billNumber = fetchLastBillNumber(dep, billType, billClassType);
        StringBuilder result = new StringBuilder();
        Long b = billNumber.getLastBillNumber();

        result.append(dep.getDepartmentCode());

        if (billNumberSuffix != BillNumberSuffix.NONE) {
            result.append(billNumberSuffix);
        }

        result.append("/");
        result.append(++b);

        billNumber.setLastBillNumber(b);
        billNumberFacade.edit(billNumber);

        return result.toString();
    }

    public String institutionBillNumberGenerator(Institution ins, BillType billType, BillClassType billClassType, BillNumberSuffix billNumberSuffix) {

        BillNumber insBillNumber = getInsBillNumberFromTheList(ins, billClassType);
        if (insBillNumber == null) {
            insBillNumber = fetchLastBillNumber(ins, billType, billClassType);
            getInsBillNumbers().add(insBillNumber);
        }
        StringBuilder result = new StringBuilder();
        Long b = insBillNumber.getLastBillNumber();

        result.append(ins.getInstitutionCode());
        result.append(billNumberSuffix.getSuffix());

        result.append("/");
        result.append(++b);

        insBillNumber.setLastBillNumber(b);
        billNumberFacade.edit(insBillNumber);

        return result.toString();
    }

    public String institutionBillNumberGenerator(Institution institution, List<BillType> billTypes, BillClassType billClassType, String suffix) {
        BillNumber insBillNumber = getInsBillNumberFromTheList(institution, billClassType);
        if (insBillNumber == null) {
            insBillNumber = fetchLastBillNumber(institution, billTypes, billClassType);
            getInsBillNumbers().add(insBillNumber);
        }
        StringBuilder result = new StringBuilder();
        Long b = insBillNumber.getLastBillNumber();

        result.append(suffix);

        result.append("/");

        result.append(++b);

        insBillNumber.setLastBillNumber(b);

        billNumberFacade.edit(insBillNumber);

        return result.toString();

    }

    public String institutionBillNumberGenerator(Institution institution, Department toDepartment, BillType billType, BillClassType billClassType, BillNumberSuffix billNumberSuffix) {
        BillNumber insBillNumber = getInsBillNumberFromTheList(institution, billClassType);
        if (insBillNumber == null) {
            insBillNumber = fetchLastBillNumber(institution, billType, billClassType);
            getInsBillNumbers().add(insBillNumber);
        }
        StringBuilder result = new StringBuilder();
        Long b = insBillNumber.getLastBillNumber();
        //System.err.println("fff " + b);

        result.append(institution.getInstitutionCode());
//        System.err.println("R1 " + result);
        if (toDepartment != null) {
            result.append(toDepartment.getDepartmentCode());
//            System.err.println("R1 " + result);
        }

        if (BillNumberSuffix.NONE != billNumberSuffix) {
            result.append(billNumberSuffix);
//            System.err.println("R1 " + result);
        }

        result.append("/");

        result.append(++b);
//        System.err.println("R1 " + result);

//        System.err.println("3 " + billNumber.getLastBillNumber());
        insBillNumber.setLastBillNumber(b);
//        System.err.println("4 " + billNumber.getLastBillNumber());
        billNumberFacade.edit(insBillNumber);
//        System.err.println("5 " + billNumber.getLastBillNumber());
//        System.err.println("Bill Num " + result);
        return result.toString();

    }

    public String institutionBillNumberGenerator(Institution institution, BillType billType, BillClassType billClassType, String suffix) {
        BillNumber insBillNumber = getInsBillNumberFromTheList(institution, billClassType);
        if (insBillNumber == null) {
            insBillNumber = fetchLastBillNumber(institution, billType, billClassType);
            getInsBillNumbers().add(insBillNumber);
        }
        StringBuilder result = new StringBuilder();
        Long b = insBillNumber.getLastBillNumber();

        result.append(suffix);

        result.append("/");

        result.append(++b);

        insBillNumber.setLastBillNumber(b);

        billNumberFacade.edit(insBillNumber);

        return result.toString();

    }

    public BillNumber getInsBillNumberFromTheList(Institution institution, BillClassType billClassType) {
        BillNumber temBillNumber = null;
        for (BillNumber bn : getInsBillNumbers()) {
            boolean matching = true;
            if (!bn.getInstitution().equals(institution)) {
                matching = false;
            }
            if (!bn.getBillClassType().equals(billClassType)) {
                matching = false;
            }
            if (matching) {
                temBillNumber = bn;
            }
        }
        return temBillNumber;
    }

    public BillNumber getDepBillNumberFromTheList(Institution ins, Department dept, BillClassType billClassType) {
        BillNumber temBillNumber = null;
        for (BillNumber bn : getDepBillNumbers()) {
            boolean matching = true;
            if (!bn.getToDepartment().equals(dept)) {
                matching = false;
            }
            if (!bn.getInstitution().equals(ins)) {
                matching = false;
            }
            if (!bn.getBillClassType().equals(billClassType)) {
                matching = false;
            }
            if (matching) {
                temBillNumber = bn;
            }
        }
        return temBillNumber;
    }

    public String generateBillNumberInsId(Bill bill, Institution institution) {
        String suffix = institution.getInstitutionCode();
        BillClassType billClassType = null;
        BillType[] billTypes = {BillType.ChannelAgent, BillType.ChannelCash, BillType.ChannelOnCall, BillType.ChannelStaff};
        List<BillType> bts = Arrays.asList(billTypes);
        BillType billType = null;
        String insId = null;
        if (bill instanceof BilledBill) {

            billClassType = BillClassType.BilledBill;
            if (bill.getBillType() == BillType.ChannelOnCall || bill.getBillType() == BillType.ChannelStaff) {
                billType = bill.getBillType();
                if (billType == BillType.ChannelOnCall) {
                    suffix += "BKONCALL";
                } else {
                    suffix += "BKSTAFF";
                }
                insId = institutionBillNumberGenerator(institution, billType, billClassType, suffix);
            } else {
                suffix += "CHANN";
                insId = institutionBillNumberGenerator(institution, bts, billClassType, suffix);
            }
        }

        if (bill instanceof CancelledBill) {
            suffix += "CHANNCAN";
            billClassType = BillClassType.CancelledBill;
            insId = institutionBillNumberGenerator(institution, bts, billClassType, suffix);
        }

        if (bill instanceof RefundBill) {
            suffix += "CHANNREF";
            billClassType = BillClassType.RefundBill;
            insId = institutionBillNumberGenerator(institution, bts, billClassType, suffix);
        }

        System.out.println("billClassType = " + billClassType);
        System.out.println("insId = " + insId);

        return insId;
    }

    private BillNumber fetchLastBillNumber(Department department, Department toDepartment, BillType billType, BillClassType billClassType) {
        String sql = "SELECT b FROM "
                + " BillNumber b "
                + " where b.retired=false "
                + " and b.billType=:bTp "
                + " and b.billClassType=:bcl"
                + " and b.department=:dep "
                + " and b.toDepartment=:tDep";
        HashMap hm = new HashMap();
        hm.put("bTp", billType);
        hm.put("bcl", billClassType);
        hm.put("dep", department);
        hm.put("tDep", toDepartment);
        BillNumber billNumber = billNumberFacade.findFirstBySQL(sql, hm);

        if (billNumber == null) {
            billNumber = new BillNumber();
            billNumber.setBillType(billType);
            billNumber.setBillClassType(billClassType);
            billNumber.setDepartment(department);
            billNumber.setToDepartment(toDepartment);

            sql = "SELECT count(b) FROM Bill b "
                    + " where b.billType=:bTp "
                    + " and b.retired=false"
                    + " and b.deptId is not null "
                    + " and type(b)=:class"
                    + " and b.department=:dep "
                    + " and b.toDepartment=:tDep";
            hm = new HashMap();
            hm.put("bTp", billType);
            hm.put("dep", department);
            hm.put("tDep", toDepartment);

            switch (billClassType) {
                case BilledBill:
                    hm.put("class", BilledBill.class);
                    break;
                case CancelledBill:
                    hm.put("class", CancelledBill.class);
                    break;
                case RefundBill:
                    hm.put("class", RefundBill.class);
                    break;
                case PreBill:
                    hm.put("class", PreBill.class);
                    break;
            }

            Long dd = getBillFacade().findAggregateLong(sql, hm, TemporalType.DATE);

            if (dd == null) {
                dd = 0l;
            }

            billNumber.setLastBillNumber(dd);
            billNumberFacade.create(billNumber);
        }

        return billNumber;

    }

    private BillNumber fetchLastBillNumber(Department department, BillType billType, BillClassType billClassType) {
        String sql = "SELECT b FROM "
                + " BillNumber b "
                + " where b.retired=false "
                + " and  b.billType=:bTp "
                + " and b.billClassType=:bcl "
                + " and b.department=:dep ";
        HashMap hm = new HashMap();
        hm.put("bTp", billType);
        hm.put("bcl", billClassType);
        hm.put("dep", department);
        BillNumber billNumber = billNumberFacade.findFirstBySQL(sql, hm);

        if (billNumber == null) {
            billNumber = new BillNumber();
            billNumber.setBillType(billType);
            billNumber.setBillClassType(billClassType);
            billNumber.setDepartment(department);

            sql = "SELECT count(b) FROM Bill b "
                    + " where b.billType=:bTp "
                    + " and b.retired=false"
                    + " and b.deptId is not null "
                    + " and type(b)=:class"
                    + " and b.department=:dep ";
            hm = new HashMap();
            hm.put("bTp", billType);
            hm.put("dep", department);

            switch (billClassType) {
                case BilledBill:
                    hm.put("class", BilledBill.class);
                    break;
                case CancelledBill:
                    hm.put("class", CancelledBill.class);
                    break;
                case RefundBill:
                    hm.put("class", RefundBill.class);
                    break;
                case PreBill:
                    hm.put("class", PreBill.class);
                    break;
            }

            Long dd = getBillFacade().findAggregateLong(sql, hm, TemporalType.DATE);

            if (dd == null) {
                dd = 0l;
            }

            billNumber.setLastBillNumber(dd);

            billNumberFacade.create(billNumber);
        }

        return billNumber;

    }

    private BillNumber fetchLastBillNumber(Institution institution, Department toDepartment, BillType billType, BillClassType billClassType) {
        String sql = "SELECT b FROM "
                + " BillNumber b "
                + " where b.retired=false "
                + " and b.billType=:bTp "
                + " and b.billClassType=:bcl"
                + " and b.institution=:ins "
                + " AND b.toDepartment=:tDep";
        HashMap hm = new HashMap();
        hm.put("bTp", billType);
        hm.put("bcl", billClassType);
        hm.put("ins", institution);
        hm.put("tDep", toDepartment);
        BillNumber billNumber = billNumberFacade.findFirstBySQL(sql, hm);

        if (billNumber == null) {
            billNumber = new BillNumber();
            billNumber.setBillType(billType);
            billNumber.setBillClassType(billClassType);
            billNumber.setInstitution(institution);
            billNumber.setToDepartment(toDepartment);

            sql = "SELECT count(b) FROM Bill b "
                    + " where b.billType=:bTp "
                    + " and b.retired=false"
                    + " and type(b)=:class"
                    + " and b.institution=:ins "
                    + " and b.toDepartment=:tDep";
            hm = new HashMap();
            hm.put("bTp", billType);
            hm.put("ins", institution);
            hm.put("tDep", toDepartment);

            switch (billClassType) {
                case BilledBill:
                    hm.put("class", BilledBill.class);
                    break;
                case CancelledBill:
                    hm.put("class", CancelledBill.class);
                    break;
                case RefundBill:
                    hm.put("class", RefundBill.class);
                    break;
                case PreBill:
                    hm.put("class", PreBill.class);
                    break;
            }

            Long dd = getBillFacade().findAggregateLong(sql, hm, TemporalType.DATE);

            if (dd == null) {
                dd = 0l;
            }

            billNumber.setLastBillNumber(dd);

            billNumberFacade.create(billNumber);
        }

        return billNumber;

    }

    private BillNumber fetchLastBillNumber(Institution institution, BillType billType, BillClassType billClassType) {
        String sql = "SELECT b FROM "
                + " BillNumber b "
                + " where b.retired=false "
                + " and b.billType=:bTp "
                + " and b.billClassType=:bcl"
                + " and b.institution=:ins "
                + " and b.toDepartment is null ";
        HashMap hm = new HashMap();
        hm.put("bTp", billType);
        hm.put("bcl", billClassType);
        hm.put("ins", institution);
        BillNumber billNumber = billNumberFacade.findFirstBySQL(sql, hm);

        if (billNumber == null) {
            billNumber = new BillNumber();
            billNumber.setBillType(billType);
            billNumber.setBillClassType(billClassType);
            billNumber.setInstitution(institution);

            sql = "SELECT count(b) FROM Bill b "
                    + " where b.billType=:bTp "
                    + " and b.retired=false"
                    + " and b.deptId is not null "
                    + " and type(b)=:class"
                    + " and b.institution=:ins "
                    + " and b.toDepartment is null  ";
            hm = new HashMap();
            hm.put("bTp", billType);
            hm.put("ins", institution);

            switch (billClassType) {
                case BilledBill:
                    hm.put("class", BilledBill.class);
                    break;
                case CancelledBill:
                    hm.put("class", CancelledBill.class);
                    break;
                case RefundBill:
                    hm.put("class", RefundBill.class);
                    break;
                case PreBill:
                    hm.put("class", PreBill.class);
                    break;
            }

            Long dd = getBillFacade().findAggregateLong(sql, hm, TemporalType.DATE);

            if (dd == null) {
                dd = 0l;
            }

            billNumber.setLastBillNumber(dd);

            billNumberFacade.create(billNumber);
        }

        return billNumber;

    }

    private BillNumber fetchLastBillNumber(Institution institution, BillType billType, BillClassType billClassType, Department department) {
        String sql = "SELECT b FROM "
                + " BillNumber b "
                + " where b.retired=false "
                + " and b.billType=:bTp "
                + " and b.billClassType=:bcl"
                + " and b.institution=:ins "
                + " and b.department=:dep ";
        HashMap hm = new HashMap();
        hm.put("bTp", billType);
        hm.put("bcl", billClassType);
        hm.put("ins", institution);
        hm.put("dep", department);
        BillNumber billNumber = billNumberFacade.findFirstBySQL(sql, hm);

        if (billNumber == null) {
            billNumber = new BillNumber();
            billNumber.setBillType(billType);
            billNumber.setBillClassType(billClassType);
            billNumber.setInstitution(institution);
            billNumber.setDepartment(department);

            sql = "SELECT count(b) FROM Bill b "
                    + " where b.billType=:bTp "
                    + " and b.retired=false "
                    + " and type(b)=:class "
                    + " and b.institution=:ins "
                    + " and b.department=:dep  ";
            hm = new HashMap();
            hm.put("bTp", billType);
            hm.put("ins", institution);
            hm.put("dep", department);

            switch (billClassType) {
                case BilledBill:
                    hm.put("class", BilledBill.class);
                    break;
                case CancelledBill:
                    hm.put("class", CancelledBill.class);
                    break;
                case RefundBill:
                    hm.put("class", RefundBill.class);
                    break;
                case PreBill:
                    hm.put("class", PreBill.class);
                    break;
            }

            Long dd = getBillFacade().findAggregateLong(sql, hm, TemporalType.DATE);

            if (dd == null) {
                dd = 0l;
            }

            billNumber.setLastBillNumber(dd);

            billNumberFacade.create(billNumber);
        }

        return billNumber;

    }

    public String institutionBillNumberGeneratorWithReference(Institution ins, Bill bill, BillType billType, BillNumberSuffix billNumberSuffix) {

        String sql = "SELECT count(b) "
                + " FROM Bill b"
                + " where type(b)=:type "
                + "and b.retired=false"
                + " AND  b.institution=:ins"
                + " AND b.billType=:btp"
                + " and b.createdAt is not null"
                + " and b.referenceBill is not null";
        StringBuilder result = new StringBuilder();
        HashMap hm = new HashMap();
        hm.put("ins", ins);
        hm.put("btp", billType);
        hm.put("type", bill.getClass());
        Long i = getBillFacade().findAggregateLong(sql, hm, TemporalType.DATE);

        result.append(ins.getInstitutionCode());
        result.append(billNumberSuffix.getSuffix());
        result.append("/");
        if (i != null) {
            result.append(i + 1);
        } else {
            result.append(1);
        }

        return result.toString();
    }

    public String institutionBillNumberGeneratorByPayment(Institution ins, Bill bill, BillType billType, BillNumberSuffix billNumberSuffix) {

        String sql = "SELECT count(b) FROM Bill b "
                + " where type(b)=:type "
                + " and b.retired=false "
                + " AND b.institution=:ins"
                + " AND b.billType=:btp "
                + " and b.createdAt is not null";
//                + " and (b.netTotal >0 or b.total >0)  ";
        StringBuilder result = new StringBuilder();
        HashMap hm = new HashMap();
        hm.put("ins", ins);
        hm.put("btp", billType);
        hm.put("type", bill.getClass());
        Long i = getBillFacade().findAggregateLong(sql, hm, TemporalType.DATE);

        result.append(ins.getInstitutionCode());
        result.append(billNumberSuffix.getSuffix());
        result.append("/");

        if (i != null) {
            result.append(i + 1);
        } else {
            result.append(1);
        }
        return result.toString();
    }

    private BillNumber fetchLastBillNumber(Institution institution, Department department, List<BillType> billTypes, BillClassType billClassType) {
        String sql = "SELECT b FROM "
                + " BillNumber b "
                + " where b.retired=false "
                + " and b.billClassType=:class "
                + " and b.institution=:ins "
                + " and b.department=:dep "
                + " and b.billType is null ";
        HashMap hm = new HashMap();
        hm.put("class", billClassType);
        hm.put("ins", institution);
        hm.put("dep", department);
        BillNumber billNumber = billNumberFacade.findFirstBySQL(sql, hm);

        if (billNumber == null) {
            billNumber = new BillNumber();
            billNumber.setBillClassType(billClassType);
            billNumber.setInstitution(institution);
            billNumber.setDepartment(department);
            HashMap m = new HashMap();

            sql = "SELECT count(b) FROM Bill b"
                    + "  where type(b)=:class "
                    + " and b.retired=false"
                    + " and b.institution=:ins "
                    + " and b.department=:dep "
                    + " and b.billType in :bt "
                    + " and b.createdAt is not null";

            hm.put("ins", institution);
            hm.put("bt", billTypes);
            hm.put("dep", department);

            switch (billClassType) {
                case BilledBill:
                    hm.put("class", BilledBill.class);
                    break;
                case CancelledBill:
                    hm.put("class", CancelledBill.class);
                    break;
                case RefundBill:
                    hm.put("class", RefundBill.class);
                    break;
                case PreBill:
                    hm.put("class", PreBill.class);
                    break;
            }

            Long dd = getBillFacade().findAggregateLong(sql, hm, TemporalType.DATE);

            if (dd == null) {
                dd = 0l;
            }

            billNumber.setLastBillNumber(dd);

            billNumberFacade.create(billNumber);
        }

        return billNumber;

    }

    public BillFacade getBillFacade() {
        return billFacade;
    }

    public BillNumberFacade getBillNumberFacade() {
        return billNumberFacade;
    }

    public String departmentBillNumberGenerator(Institution institution, Department department, List<BillType> billTypes, BillClassType billClassType, String suffix) {
        BillNumber billNumber = getDepBillNumberFromTheList(institution, department, billClassType);
        if (billNumber == null) {
            billNumber = fetchLastBillNumber(institution, department, billTypes, billClassType);
            getDepBillNumbers().add(billNumber);
        }
        StringBuilder result = new StringBuilder();
        Long b = billNumber.getLastBillNumber();

        result.append(suffix);

        result.append("/");

        result.append(++b);

        billNumber.setLastBillNumber(b);

        billNumberFacade.edit(billNumber);

        return result.toString();

    }

    public String departmentBillNumberGenerator(Institution institution, Department department, BillType billType, BillClassType billClassType, String suffix) {
        BillNumber billNumber = getDepBillNumberFromTheList(institution, department, billClassType);
        if (billNumber == null) {
            billNumber = fetchLastBillNumber(institution, department, billType, billClassType);
            getDepBillNumbers().add(billNumber);
        }

        StringBuilder result = new StringBuilder();
        Long b = billNumber.getLastBillNumber();

        result.append(suffix);

        result.append("/");

        result.append(++b);

        billNumber.setLastBillNumber(b);

        billNumberFacade.edit(billNumber);

        return result.toString();

    }

    public List<BillNumber> getInsBillNumbers() {
        if (insBillNumbers == null) {
            insBillNumbers = new ArrayList<>();
        }
        return insBillNumbers;
    }

    public void setInsBillNumbers(List<BillNumber> insBillNumbers) {
        this.insBillNumbers = insBillNumbers;
    }

    public List<BillNumber> getDepBillNumbers() {
        if (depBillNumbers == null) {
            depBillNumbers = new ArrayList<>();
        }
        return depBillNumbers;
    }

    public void setDepBillNumbers(List<BillNumber> depBillNumbers) {
        this.depBillNumbers = depBillNumbers;
    }

}
