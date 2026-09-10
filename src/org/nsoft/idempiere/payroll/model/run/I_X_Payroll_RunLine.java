/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.nsoft.idempiere.payroll.model.run;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for X_Payroll_RunLine
 *  @author iDempiere (generated) 
 *  @version Release 13
 */
@SuppressWarnings("all")
public interface I_X_Payroll_RunLine 
{

    /** TableName=X_Payroll_RunLine */
    public static final String Table_Name = "X_Payroll_RunLine";

    /** AD_Table_ID=1000020 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 4 - System 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(4);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Tenant.
	  * Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within tenant
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within tenant
	  */
	public int getAD_Org_ID();

    /** Column name BPJSBaseIncome */
    public static final String COLUMNNAME_BPJSBaseIncome = "BPJSBaseIncome";

	/** Set BPJSBaseIncome	  */
	public void setBPJSBaseIncome (BigDecimal BPJSBaseIncome);

	/** Get BPJSBaseIncome	  */
	public BigDecimal getBPJSBaseIncome();

    /** Column name CashGrossIncome */
    public static final String COLUMNNAME_CashGrossIncome = "CashGrossIncome";

	/** Set CashGrossIncome	  */
	public void setCashGrossIncome (BigDecimal CashGrossIncome);

	/** Get CashGrossIncome	  */
	public BigDecimal getCashGrossIncome();

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name CumulativeGrossBeforeThisRun */
    public static final String COLUMNNAME_CumulativeGrossBeforeThisRun = "CumulativeGrossBeforeThisRun";

	/** Set CumulativeGrossBeforeThisRun	  */
	public void setCumulativeGrossBeforeThisRun (BigDecimal CumulativeGrossBeforeThisRun);

	/** Get CumulativeGrossBeforeThisRun	  */
	public BigDecimal getCumulativeGrossBeforeThisRun();

    /** Column name HR_Employee_ID */
    public static final String COLUMNNAME_HR_Employee_ID = "HR_Employee_ID";

	/** Set Payroll Employee	  */
	public void setHR_Employee_ID (int HR_Employee_ID);

	/** Get Payroll Employee	  */
	public int getHR_Employee_ID();

	@Deprecated(since="13") // use better methods with cache
	public org.eevolution.model.I_HR_Employee getHR_Employee() throws RuntimeException;

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name NetIncome */
    public static final String COLUMNNAME_NetIncome = "NetIncome";

	/** Set NetIncome	  */
	public void setNetIncome (BigDecimal NetIncome);

	/** Get NetIncome	  */
	public BigDecimal getNetIncome();

    /** Column name PPh21WithheldPreviouslyThisPeriod */
    public static final String COLUMNNAME_PPh21WithheldPreviouslyThisPeriod = "PPh21WithheldPreviouslyThisPeriod";

	/** Set PPh21WithheldPreviouslyThisPeriod	  */
	public void setPPh21WithheldPreviouslyThisPeriod (BigDecimal PPh21WithheldPreviouslyThisPeriod);

	/** Get PPh21WithheldPreviouslyThisPeriod	  */
	public BigDecimal getPPh21WithheldPreviouslyThisPeriod();

    /** Column name PPh21_Amount */
    public static final String COLUMNNAME_PPh21_Amount = "PPh21_Amount";

	/** Set PPh21_Amount	  */
	public void setPPh21_Amount (BigDecimal PPh21_Amount);

	/** Get PPh21_Amount	  */
	public BigDecimal getPPh21_Amount();

    /** Column name TER_Category */
    public static final String COLUMNNAME_TER_Category = "TER_Category";

	/** Set TER_Category	  */
	public void setTER_Category (String TER_Category);

	/** Get TER_Category	  */
	public String getTER_Category();

    /** Column name TER_RateApplied */
    public static final String COLUMNNAME_TER_RateApplied = "TER_RateApplied";

	/** Set TER_RateApplied	  */
	public void setTER_RateApplied (BigDecimal TER_RateApplied);

	/** Get TER_RateApplied	  */
	public BigDecimal getTER_RateApplied();

    /** Column name TaxableGrossIncome */
    public static final String COLUMNNAME_TaxableGrossIncome = "TaxableGrossIncome";

	/** Set TaxableGrossIncome	  */
	public void setTaxableGrossIncome (BigDecimal TaxableGrossIncome);

	/** Get TaxableGrossIncome	  */
	public BigDecimal getTaxableGrossIncome();

    /** Column name TotalDeduction */
    public static final String COLUMNNAME_TotalDeduction = "TotalDeduction";

	/** Set TotalDeduction	  */
	public void setTotalDeduction (BigDecimal TotalDeduction);

	/** Get TotalDeduction	  */
	public BigDecimal getTotalDeduction();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();

    /** Column name X_Payroll_RunLine_ID */
    public static final String COLUMNNAME_X_Payroll_RunLine_ID = "X_Payroll_RunLine_ID";

	/** Set Payroll RunLine	  */
	public void setX_Payroll_RunLine_ID (int X_Payroll_RunLine_ID);

	/** Get Payroll RunLine	  */
	public int getX_Payroll_RunLine_ID();

    /** Column name X_Payroll_Run_ID */
    public static final String COLUMNNAME_X_Payroll_Run_ID = "X_Payroll_Run_ID";

	/** Set Payroll Run ID	  */
	public void setX_Payroll_Run_ID (int X_Payroll_Run_ID);

	/** Get Payroll Run ID	  */
	public int getX_Payroll_Run_ID();

	@Deprecated(since="13") // use better methods with cache
	public I_X_Payroll_Run getX_Payroll_Run() throws RuntimeException;
}
