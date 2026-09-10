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
package org.nsoft.idempiere.payroll.model.bpjs;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for X_Payroll_BPJS_Rate
 *  @author iDempiere (generated)
 *  @version Release 13 - $Id$ */
@org.adempiere.base.Model(table="X_Payroll_BPJS_Rate")
public class X_X_Payroll_BPJS_Rate extends PO implements I_X_Payroll_BPJS_Rate, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20260910L;

    /** Standard Constructor */
    public X_X_Payroll_BPJS_Rate (Properties ctx, int X_Payroll_BPJS_Rate_ID, String trxName)
    {
      super (ctx, X_Payroll_BPJS_Rate_ID, trxName);
      /** if (X_Payroll_BPJS_Rate_ID == 0)
        {
			setEmployeeRate (Env.ZERO);
			setEmployerRate (Env.ZERO);
			setIsEmployerContributionTaxable (false);
// N
			setProgramType (null);
			setValidFrom (new Timestamp( System.currentTimeMillis() ));
			setX_Payroll_BPJS_Rate_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_X_Payroll_BPJS_Rate (Properties ctx, int X_Payroll_BPJS_Rate_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, X_Payroll_BPJS_Rate_ID, trxName, virtualColumns);
      /** if (X_Payroll_BPJS_Rate_ID == 0)
        {
			setEmployeeRate (Env.ZERO);
			setEmployerRate (Env.ZERO);
			setIsEmployerContributionTaxable (false);
// N
			setProgramType (null);
			setValidFrom (new Timestamp( System.currentTimeMillis() ));
			setX_Payroll_BPJS_Rate_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_X_Payroll_BPJS_Rate (Properties ctx, String X_Payroll_BPJS_Rate_UU, String trxName)
    {
      super (ctx, X_Payroll_BPJS_Rate_UU, trxName);
      /** if (X_Payroll_BPJS_Rate_UU == null)
        {
			setEmployeeRate (Env.ZERO);
			setEmployerRate (Env.ZERO);
			setIsEmployerContributionTaxable (false);
// N
			setProgramType (null);
			setValidFrom (new Timestamp( System.currentTimeMillis() ));
			setX_Payroll_BPJS_Rate_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_X_Payroll_BPJS_Rate (Properties ctx, String X_Payroll_BPJS_Rate_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, X_Payroll_BPJS_Rate_UU, trxName, virtualColumns);
      /** if (X_Payroll_BPJS_Rate_UU == null)
        {
			setEmployeeRate (Env.ZERO);
			setEmployerRate (Env.ZERO);
			setIsEmployerContributionTaxable (false);
// N
			setProgramType (null);
			setValidFrom (new Timestamp( System.currentTimeMillis() ));
			setX_Payroll_BPJS_Rate_ID (0);
        } */
    }

    /** Load Constructor */
    public X_X_Payroll_BPJS_Rate (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_X_Payroll_BPJS_Rate[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Employee Rate.
		@param EmployeeRate Employee Rate
	*/
	public void setEmployeeRate (BigDecimal EmployeeRate)
	{
		set_Value (COLUMNNAME_EmployeeRate, EmployeeRate);
	}

	/** Get Employee Rate.
		@return Employee Rate	  */
	public BigDecimal getEmployeeRate()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_EmployeeRate);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Employer Rate.
		@param EmployerRate Employer Rate
	*/
	public void setEmployerRate (BigDecimal EmployerRate)
	{
		set_Value (COLUMNNAME_EmployerRate, EmployerRate);
	}

	/** Get Employer Rate.
		@return Employer Rate	  */
	public BigDecimal getEmployerRate()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_EmployerRate);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Employer Contribution Taxable.
		@param IsEmployerContributionTaxable Employer Contribution Taxable
	*/
	public void setIsEmployerContributionTaxable (boolean IsEmployerContributionTaxable)
	{
		set_Value (COLUMNNAME_IsEmployerContributionTaxable, Boolean.valueOf(IsEmployerContributionTaxable));
	}

	/** Get Employer Contribution Taxable.
		@return Employer Contribution Taxable	  */
	public boolean isEmployerContributionTaxable()
	{
		Object oo = get_Value(COLUMNNAME_IsEmployerContributionTaxable);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Program Type.
		@param ProgramType Program Type
	*/
	public void setProgramType (String ProgramType)
	{
		set_Value (COLUMNNAME_ProgramType, ProgramType);
	}

	/** Get Program Type.
		@return Program Type	  */
	public String getProgramType()
	{
		return (String)get_Value(COLUMNNAME_ProgramType);
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

	/** Set Wage Cap Lower.
		@param WageCapLower Wage Cap Lower
	*/
	public void setWageCapLower (BigDecimal WageCapLower)
	{
		set_Value (COLUMNNAME_WageCapLower, WageCapLower);
	}

	/** Get Wage Cap Lower.
		@return Wage Cap Lower	  */
	public BigDecimal getWageCapLower()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_WageCapLower);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Wage Cap Upper.
		@param WageCapUpper Wage Cap Upper
	*/
	public void setWageCapUpper (BigDecimal WageCapUpper)
	{
		set_Value (COLUMNNAME_WageCapUpper, WageCapUpper);
	}

	/** Get Wage Cap Upper.
		@return Wage Cap Upper	  */
	public BigDecimal getWageCapUpper()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_WageCapUpper);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set BPJS Rate ID.
		@param X_Payroll_BPJS_Rate_ID BPJS Rate ID
	*/
	public void setX_Payroll_BPJS_Rate_ID (int X_Payroll_BPJS_Rate_ID)
	{
		if (X_Payroll_BPJS_Rate_ID < 1)
			set_ValueNoCheck (COLUMNNAME_X_Payroll_BPJS_Rate_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_X_Payroll_BPJS_Rate_ID, Integer.valueOf(X_Payroll_BPJS_Rate_ID));
	}

	/** Get BPJS Rate ID.
		@return BPJS Rate ID	  */
	public int getX_Payroll_BPJS_Rate_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_X_Payroll_BPJS_Rate_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}