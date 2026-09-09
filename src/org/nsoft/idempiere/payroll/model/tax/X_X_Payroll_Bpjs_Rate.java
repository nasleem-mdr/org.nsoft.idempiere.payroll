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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for X_Payroll_Bpjs_Rate
 *  @author iDempiere (generated)
 *  @version Release 13 - $Id$ */
@org.adempiere.base.Model(table="X_Payroll_Bpjs_Rate")
public class X_X_Payroll_Bpjs_Rate extends PO implements I_X_Payroll_Bpjs_Rate, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20260909L;

    /** Standard Constructor */
    public X_X_Payroll_Bpjs_Rate (Properties ctx, int X_Payroll_Bpjs_Rate_ID, String trxName)
    {
      super (ctx, X_Payroll_Bpjs_Rate_ID, trxName);
      /** if (X_Payroll_Bpjs_Rate_ID == 0)
        {
			setValidFrom (new Timestamp( System.currentTimeMillis() ));
			setX_Payroll_Bpjs_Rate_ID (0);
			setemployeerate (Env.ZERO);
			setemployerrate (Env.ZERO);
			setisemployercontributiontaxable (false);
// N
			setprogramtype (Env.ZERO);
        } */
    }

    /** Standard Constructor */
    public X_X_Payroll_Bpjs_Rate (Properties ctx, int X_Payroll_Bpjs_Rate_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, X_Payroll_Bpjs_Rate_ID, trxName, virtualColumns);
      /** if (X_Payroll_Bpjs_Rate_ID == 0)
        {
			setValidFrom (new Timestamp( System.currentTimeMillis() ));
			setX_Payroll_Bpjs_Rate_ID (0);
			setemployeerate (Env.ZERO);
			setemployerrate (Env.ZERO);
			setisemployercontributiontaxable (false);
// N
			setprogramtype (Env.ZERO);
        } */
    }

    /** Standard Constructor */
    public X_X_Payroll_Bpjs_Rate (Properties ctx, String X_Payroll_Bpjs_Rate_UU, String trxName)
    {
      super (ctx, X_Payroll_Bpjs_Rate_UU, trxName);
      /** if (X_Payroll_Bpjs_Rate_UU == null)
        {
			setValidFrom (new Timestamp( System.currentTimeMillis() ));
			setX_Payroll_Bpjs_Rate_ID (0);
			setemployeerate (Env.ZERO);
			setemployerrate (Env.ZERO);
			setisemployercontributiontaxable (false);
// N
			setprogramtype (Env.ZERO);
        } */
    }

    /** Standard Constructor */
    public X_X_Payroll_Bpjs_Rate (Properties ctx, String X_Payroll_Bpjs_Rate_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, X_Payroll_Bpjs_Rate_UU, trxName, virtualColumns);
      /** if (X_Payroll_Bpjs_Rate_UU == null)
        {
			setValidFrom (new Timestamp( System.currentTimeMillis() ));
			setX_Payroll_Bpjs_Rate_ID (0);
			setemployeerate (Env.ZERO);
			setemployerrate (Env.ZERO);
			setisemployercontributiontaxable (false);
// N
			setprogramtype (Env.ZERO);
        } */
    }

    /** Load Constructor */
    public X_X_Payroll_Bpjs_Rate (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_X_Payroll_Bpjs_Rate[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set BPJS Rate.
		@param X_Payroll_Bpjs_Rate_ID BPJS Rate
	*/
	public void setX_Payroll_Bpjs_Rate_ID (int X_Payroll_Bpjs_Rate_ID)
	{
		if (X_Payroll_Bpjs_Rate_ID < 1)
			set_ValueNoCheck (COLUMNNAME_X_Payroll_Bpjs_Rate_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_X_Payroll_Bpjs_Rate_ID, Integer.valueOf(X_Payroll_Bpjs_Rate_ID));
	}

	/** Get BPJS Rate.
		@return BPJS Rate	  */
	public int getX_Payroll_Bpjs_Rate_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_X_Payroll_Bpjs_Rate_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set employeerate.
		@param employeerate employeerate
	*/
	public void setemployeerate (BigDecimal employeerate)
	{
		set_Value (COLUMNNAME_employeerate, employeerate);
	}

	/** Get employeerate.
		@return employeerate	  */
	public BigDecimal getemployeerate()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_employeerate);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set employerrate.
		@param employerrate employerrate
	*/
	public void setemployerrate (BigDecimal employerrate)
	{
		set_Value (COLUMNNAME_employerrate, employerrate);
	}

	/** Get employerrate.
		@return employerrate	  */
	public BigDecimal getemployerrate()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_employerrate);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set isemployercontributiontaxable.
		@param isemployercontributiontaxable isemployercontributiontaxable
	*/
	public void setisemployercontributiontaxable (boolean isemployercontributiontaxable)
	{
		set_Value (COLUMNNAME_isemployercontributiontaxable, Boolean.valueOf(isemployercontributiontaxable));
	}

	/** Get isemployercontributiontaxable.
		@return isemployercontributiontaxable	  */
	public boolean isemployercontributiontaxable()
	{
		Object oo = get_Value(COLUMNNAME_isemployercontributiontaxable);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set programtype.
		@param programtype programtype
	*/
	public void setprogramtype (BigDecimal programtype)
	{
		set_Value (COLUMNNAME_programtype, programtype);
	}

	/** Get programtype.
		@return programtype	  */
	public BigDecimal getprogramtype()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_programtype);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set wagecaplower.
		@param wagecaplower wagecaplower
	*/
	public void setwagecaplower (BigDecimal wagecaplower)
	{
		set_Value (COLUMNNAME_wagecaplower, wagecaplower);
	}

	/** Get wagecaplower.
		@return wagecaplower	  */
	public BigDecimal getwagecaplower()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_wagecaplower);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set wagecapupper.
		@param wagecapupper wagecapupper
	*/
	public void setwagecapupper (BigDecimal wagecapupper)
	{
		set_Value (COLUMNNAME_wagecapupper, wagecapupper);
	}

	/** Get wagecapupper.
		@return wagecapupper	  */
	public BigDecimal getwagecapupper()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_wagecapupper);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}