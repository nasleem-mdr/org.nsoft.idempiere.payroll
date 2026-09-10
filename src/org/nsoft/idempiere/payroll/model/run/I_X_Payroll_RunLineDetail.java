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

/** Generated Interface for X_Payroll_RunLineDetail
 *  @author iDempiere (generated) 
 *  @version Release 13
 */
@SuppressWarnings("all")
public interface I_X_Payroll_RunLineDetail 
{

    /** TableName=X_Payroll_RunLineDetail */
    public static final String Table_Name = "X_Payroll_RunLineDetail";

    /** AD_Table_ID=1000022 */
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

    /** Column name EmployeeAmount */
    public static final String COLUMNNAME_EmployeeAmount = "EmployeeAmount";

	/** Set EmployeeAmount	  */
	public void setEmployeeAmount (BigDecimal EmployeeAmount);

	/** Get EmployeeAmount	  */
	public BigDecimal getEmployeeAmount();

    /** Column name EmployeeRateApplied */
    public static final String COLUMNNAME_EmployeeRateApplied = "EmployeeRateApplied";

	/** Set EmployeeRateApplied	  */
	public void setEmployeeRateApplied (BigDecimal EmployeeRateApplied);

	/** Get EmployeeRateApplied	  */
	public BigDecimal getEmployeeRateApplied();

    /** Column name EmployerAmount */
    public static final String COLUMNNAME_EmployerAmount = "EmployerAmount";

	/** Set EmployerAmount	  */
	public void setEmployerAmount (BigDecimal EmployerAmount);

	/** Get EmployerAmount	  */
	public BigDecimal getEmployerAmount();

    /** Column name EmployerRateApplied */
    public static final String COLUMNNAME_EmployerRateApplied = "EmployerRateApplied";

	/** Set EmployerRateApplied	  */
	public void setEmployerRateApplied (BigDecimal EmployerRateApplied);

	/** Get EmployerRateApplied	  */
	public BigDecimal getEmployerRateApplied();

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

    /** Column name ProgramType */
    public static final String COLUMNNAME_ProgramType = "ProgramType";

	/** Set Program Type	  */
	public void setProgramType (String ProgramType);

	/** Get Program Type	  */
	public String getProgramType();

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

    /** Column name WageBase */
    public static final String COLUMNNAME_WageBase = "WageBase";

	/** Set WageBase	  */
	public void setWageBase (BigDecimal WageBase);

	/** Get WageBase	  */
	public BigDecimal getWageBase();

    /** Column name X_Payroll_BPJS_Rate_ID */
    public static final String COLUMNNAME_X_Payroll_BPJS_Rate_ID = "X_Payroll_BPJS_Rate_ID";

	/** Set BPJS Rate	  */
	public void setX_Payroll_BPJS_Rate_ID (int X_Payroll_BPJS_Rate_ID);

	/** Get BPJS Rate	  */
	public int getX_Payroll_BPJS_Rate_ID();

	@Deprecated(since="13") // use better methods with cache
	public I_X_Payroll_BPJS_Rate getX_Payroll_BPJS_Rate() throws RuntimeException;

    /** Column name X_Payroll_RunLineDetail_ID */
    public static final String COLUMNNAME_X_Payroll_RunLineDetail_ID = "X_Payroll_RunLineDetail_ID";

	/** Set Payroll RunLine Detail	  */
	public void setX_Payroll_RunLineDetail_ID (int X_Payroll_RunLineDetail_ID);

	/** Get Payroll RunLine Detail	  */
	public int getX_Payroll_RunLineDetail_ID();

    /** Column name X_Payroll_RunLine_ID */
    public static final String COLUMNNAME_X_Payroll_RunLine_ID = "X_Payroll_RunLine_ID";

	/** Set Payroll RunLine	  */
	public void setX_Payroll_RunLine_ID (int X_Payroll_RunLine_ID);

	/** Get Payroll RunLine	  */
	public int getX_Payroll_RunLine_ID();

	@Deprecated(since="13") // use better methods with cache
	public I_X_Payroll_RunLine getX_Payroll_RunLine() throws RuntimeException;
}
