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
/** Generated Model - DO NOT CHANGE */
package org.nsoft.idempiere.payroll.model.tax;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for X_Payroll_EmployeeTaxProfile
 *  @author iDempiere (generated)
 *  @version Release 13 - $Id$ */
@org.adempiere.base.Model(table="X_Payroll_EmployeeTaxProfile")
public class X_X_Payroll_EmployeeTaxProfile extends PO implements I_X_Payroll_EmployeeTaxProfile, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20260910L;

    /** Standard Constructor */
    public X_X_Payroll_EmployeeTaxProfile (Properties ctx, int X_Payroll_EmployeeTaxProfile_ID, String trxName)
    {
      super (ctx, X_Payroll_EmployeeTaxProfile_ID, trxName);
      /** if (X_Payroll_EmployeeTaxProfile_ID == 0)
        {
			setHR_Employee_ID (0);
			setHasNPWP (false);
// N
			setSchemeType (null);
			setTER_CategoryOverride (null);
			setValidFrom (new Timestamp( System.currentTimeMillis() ));
			setX_Payroll_EmployeeTaxProfile_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_X_Payroll_EmployeeTaxProfile (Properties ctx, int X_Payroll_EmployeeTaxProfile_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, X_Payroll_EmployeeTaxProfile_ID, trxName, virtualColumns);
      /** if (X_Payroll_EmployeeTaxProfile_ID == 0)
        {
			setHR_Employee_ID (0);
			setHasNPWP (false);
// N
			setSchemeType (null);
			setTER_CategoryOverride (null);
			setValidFrom (new Timestamp( System.currentTimeMillis() ));
			setX_Payroll_EmployeeTaxProfile_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_X_Payroll_EmployeeTaxProfile (Properties ctx, String X_Payroll_EmployeeTaxProfile_UU, String trxName)
    {
      super (ctx, X_Payroll_EmployeeTaxProfile_UU, trxName);
      /** if (X_Payroll_EmployeeTaxProfile_UU == null)
        {
			setHR_Employee_ID (0);
			setHasNPWP (false);
// N
			setSchemeType (null);
			setTER_CategoryOverride (null);
			setValidFrom (new Timestamp( System.currentTimeMillis() ));
			setX_Payroll_EmployeeTaxProfile_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_X_Payroll_EmployeeTaxProfile (Properties ctx, String X_Payroll_EmployeeTaxProfile_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, X_Payroll_EmployeeTaxProfile_UU, trxName, virtualColumns);
      /** if (X_Payroll_EmployeeTaxProfile_UU == null)
        {
			setHR_Employee_ID (0);
			setHasNPWP (false);
// N
			setSchemeType (null);
			setTER_CategoryOverride (null);
			setValidFrom (new Timestamp( System.currentTimeMillis() ));
			setX_Payroll_EmployeeTaxProfile_ID (0);
        } */
    }

    /** Load Constructor */
    public X_X_Payroll_EmployeeTaxProfile (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 4 - System
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuilder sb = new StringBuilder ("X_X_Payroll_EmployeeTaxProfile[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	@Deprecated(since="13") // use better methods with cache
	public org.eevolution.model.I_HR_Employee getHR_Employee() throws RuntimeException
	{
		return (org.eevolution.model.I_HR_Employee)MTable.get(getCtx(), org.eevolution.model.I_HR_Employee.Table_ID)
			.getPO(getHR_Employee_ID(), get_TrxName());
	}

	/** Set Payroll Employee.
		@param HR_Employee_ID Payroll Employee
	*/
	public void setHR_Employee_ID (int HR_Employee_ID)
	{
		if (HR_Employee_ID < 1)
			set_Value (COLUMNNAME_HR_Employee_ID, null);
		else
			set_Value (COLUMNNAME_HR_Employee_ID, Integer.valueOf(HR_Employee_ID));
	}

	/** Get Payroll Employee.
		@return Payroll Employee	  */
	public int getHR_Employee_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Employee_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set HasNPWP.
		@param HasNPWP HasNPWP
	*/
	public void setHasNPWP (boolean HasNPWP)
	{
		set_Value (COLUMNNAME_HasNPWP, Boolean.valueOf(HasNPWP));
	}

	/** Get HasNPWP.
		@return HasNPWP	  */
	public boolean isHasNPWP()
	{
		Object oo = get_Value(COLUMNNAME_HasNPWP);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Reason.
		@param Reason Reason
	*/
	public void setReason (String Reason)
	{
		set_Value (COLUMNNAME_Reason, Reason);
	}

	/** Get Reason.
		@return Reason	  */
	public String getReason()
	{
		return (String)get_Value(COLUMNNAME_Reason);
	}

	/** Set SchemeType.
		@param SchemeType SchemeType
	*/
	public void setSchemeType (String SchemeType)
	{
		set_Value (COLUMNNAME_SchemeType, SchemeType);
	}

	/** Get SchemeType.
		@return SchemeType	  */
	public String getSchemeType()
	{
		return (String)get_Value(COLUMNNAME_SchemeType);
	}

	/** Set TER_CategoryOverride.
		@param TER_CategoryOverride TER_CategoryOverride
	*/
	public void setTER_CategoryOverride (String TER_CategoryOverride)
	{
		set_Value (COLUMNNAME_TER_CategoryOverride, TER_CategoryOverride);
	}

	/** Get TER_CategoryOverride.
		@return TER_CategoryOverride	  */
	public String getTER_CategoryOverride()
	{
		return (String)get_Value(COLUMNNAME_TER_CategoryOverride);
	}

	/** Set Valid from.
		@param ValidFrom Valid from including this date (first day)
	*/
	public void setValidFrom (Timestamp ValidFrom)
	{
		set_Value (COLUMNNAME_ValidFrom, ValidFrom);
	}

	/** Get Valid from.
		@return Valid from including this date (first day)
	  */
	public Timestamp getValidFrom()
	{
		return (Timestamp)get_Value(COLUMNNAME_ValidFrom);
	}

	/** Set Valid to.
		@param ValidTo Valid to including this date (last day)
	*/
	public void setValidTo (Timestamp ValidTo)
	{
		set_Value (COLUMNNAME_ValidTo, ValidTo);
	}

	/** Get Valid to.
		@return Valid to including this date (last day)
	  */
	public Timestamp getValidTo()
	{
		return (Timestamp)get_Value(COLUMNNAME_ValidTo);
	}

	/** Set Payroll Employee Program.
		@param X_Payroll_EmployeeTaxProfile_ID Payroll Employee Program
	*/
	public void setX_Payroll_EmployeeTaxProfile_ID (int X_Payroll_EmployeeTaxProfile_ID)
	{
		if (X_Payroll_EmployeeTaxProfile_ID < 1)
			set_ValueNoCheck (COLUMNNAME_X_Payroll_EmployeeTaxProfile_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_X_Payroll_EmployeeTaxProfile_ID, Integer.valueOf(X_Payroll_EmployeeTaxProfile_ID));
	}

	/** Get Payroll Employee Program.
		@return Payroll Employee Program	  */
	public int getX_Payroll_EmployeeTaxProfile_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_X_Payroll_EmployeeTaxProfile_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}