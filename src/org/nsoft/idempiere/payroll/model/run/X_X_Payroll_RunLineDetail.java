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
package org.nsoft.idempiere.payroll.model.run;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for X_Payroll_RunLineDetail
 *  @author iDempiere (generated)
 *  @version Release 13 - $Id$ */
@org.adempiere.base.Model(table="X_Payroll_RunLineDetail")
public class X_X_Payroll_RunLineDetail extends PO implements I_X_Payroll_RunLineDetail, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20260910L;

    /** Standard Constructor */
    public X_X_Payroll_RunLineDetail (Properties ctx, int X_Payroll_RunLineDetail_ID, String trxName)
    {
      super (ctx, X_Payroll_RunLineDetail_ID, trxName);
      /** if (X_Payroll_RunLineDetail_ID == 0)
        {
			setEmployeeAmount (Env.ZERO);
			setEmployeeRateApplied (Env.ZERO);
			setEmployerAmount (Env.ZERO);
			setEmployerRateApplied (Env.ZERO);
			setProgramType (null);
			setWageBase (Env.ZERO);
			setX_Payroll_RunLineDetail_ID (0);
			setX_Payroll_RunLine_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_X_Payroll_RunLineDetail (Properties ctx, int X_Payroll_RunLineDetail_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, X_Payroll_RunLineDetail_ID, trxName, virtualColumns);
      /** if (X_Payroll_RunLineDetail_ID == 0)
        {
			setEmployeeAmount (Env.ZERO);
			setEmployeeRateApplied (Env.ZERO);
			setEmployerAmount (Env.ZERO);
			setEmployerRateApplied (Env.ZERO);
			setProgramType (null);
			setWageBase (Env.ZERO);
			setX_Payroll_RunLineDetail_ID (0);
			setX_Payroll_RunLine_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_X_Payroll_RunLineDetail (Properties ctx, String X_Payroll_RunLineDetail_UU, String trxName)
    {
      super (ctx, X_Payroll_RunLineDetail_UU, trxName);
      /** if (X_Payroll_RunLineDetail_UU == null)
        {
			setEmployeeAmount (Env.ZERO);
			setEmployeeRateApplied (Env.ZERO);
			setEmployerAmount (Env.ZERO);
			setEmployerRateApplied (Env.ZERO);
			setProgramType (null);
			setWageBase (Env.ZERO);
			setX_Payroll_RunLineDetail_ID (0);
			setX_Payroll_RunLine_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_X_Payroll_RunLineDetail (Properties ctx, String X_Payroll_RunLineDetail_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, X_Payroll_RunLineDetail_UU, trxName, virtualColumns);
      /** if (X_Payroll_RunLineDetail_UU == null)
        {
			setEmployeeAmount (Env.ZERO);
			setEmployeeRateApplied (Env.ZERO);
			setEmployerAmount (Env.ZERO);
			setEmployerRateApplied (Env.ZERO);
			setProgramType (null);
			setWageBase (Env.ZERO);
			setX_Payroll_RunLineDetail_ID (0);
			setX_Payroll_RunLine_ID (0);
        } */
    }

    /** Load Constructor */
    public X_X_Payroll_RunLineDetail (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_X_Payroll_RunLineDetail[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set EmployeeAmount.
		@param EmployeeAmount EmployeeAmount
	*/
	public void setEmployeeAmount (BigDecimal EmployeeAmount)
	{
		set_Value (COLUMNNAME_EmployeeAmount, EmployeeAmount);
	}

	/** Get EmployeeAmount.
		@return EmployeeAmount	  */
	public BigDecimal getEmployeeAmount()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_EmployeeAmount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set EmployeeRateApplied.
		@param EmployeeRateApplied EmployeeRateApplied
	*/
	public void setEmployeeRateApplied (BigDecimal EmployeeRateApplied)
	{
		set_Value (COLUMNNAME_EmployeeRateApplied, EmployeeRateApplied);
	}

	/** Get EmployeeRateApplied.
		@return EmployeeRateApplied	  */
	public BigDecimal getEmployeeRateApplied()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_EmployeeRateApplied);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set EmployerAmount.
		@param EmployerAmount EmployerAmount
	*/
	public void setEmployerAmount (BigDecimal EmployerAmount)
	{
		set_Value (COLUMNNAME_EmployerAmount, EmployerAmount);
	}

	/** Get EmployerAmount.
		@return EmployerAmount	  */
	public BigDecimal getEmployerAmount()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_EmployerAmount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set EmployerRateApplied.
		@param EmployerRateApplied EmployerRateApplied
	*/
	public void setEmployerRateApplied (BigDecimal EmployerRateApplied)
	{
		set_Value (COLUMNNAME_EmployerRateApplied, EmployerRateApplied);
	}

	/** Get EmployerRateApplied.
		@return EmployerRateApplied	  */
	public BigDecimal getEmployerRateApplied()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_EmployerRateApplied);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set WageBase.
		@param WageBase WageBase
	*/
	public void setWageBase (BigDecimal WageBase)
	{
		set_Value (COLUMNNAME_WageBase, WageBase);
	}

	/** Get WageBase.
		@return WageBase	  */
	public BigDecimal getWageBase()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_WageBase);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	@Deprecated(since="13") // use better methods with cache
	public I_X_Payroll_BPJS_Rate getX_Payroll_BPJS_Rate() throws RuntimeException
	{
		return (I_X_Payroll_BPJS_Rate)MTable.get(getCtx(), I_X_Payroll_BPJS_Rate.Table_ID)
			.getPO(getX_Payroll_BPJS_Rate_ID(), get_TrxName());
	}

	/** Set BPJS Rate.
		@param X_Payroll_BPJS_Rate_ID BPJS Rate
	*/
	public void setX_Payroll_BPJS_Rate_ID (int X_Payroll_BPJS_Rate_ID)
	{
		if (X_Payroll_BPJS_Rate_ID < 1)
			set_ValueNoCheck (COLUMNNAME_X_Payroll_BPJS_Rate_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_X_Payroll_BPJS_Rate_ID, Integer.valueOf(X_Payroll_BPJS_Rate_ID));
	}

	/** Get BPJS Rate.
		@return BPJS Rate	  */
	public int getX_Payroll_BPJS_Rate_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_X_Payroll_BPJS_Rate_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Payroll RunLine Detail.
		@param X_Payroll_RunLineDetail_ID Payroll RunLine Detail
	*/
	public void setX_Payroll_RunLineDetail_ID (int X_Payroll_RunLineDetail_ID)
	{
		if (X_Payroll_RunLineDetail_ID < 1)
			set_ValueNoCheck (COLUMNNAME_X_Payroll_RunLineDetail_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_X_Payroll_RunLineDetail_ID, Integer.valueOf(X_Payroll_RunLineDetail_ID));
	}

	/** Get Payroll RunLine Detail.
		@return Payroll RunLine Detail	  */
	public int getX_Payroll_RunLineDetail_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_X_Payroll_RunLineDetail_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	@Deprecated(since="13") // use better methods with cache
	public I_X_Payroll_RunLine getX_Payroll_RunLine() throws RuntimeException
	{
		return (I_X_Payroll_RunLine)MTable.get(getCtx(), I_X_Payroll_RunLine.Table_ID)
			.getPO(getX_Payroll_RunLine_ID(), get_TrxName());
	}

	/** Set Payroll RunLine.
		@param X_Payroll_RunLine_ID Payroll RunLine
	*/
	public void setX_Payroll_RunLine_ID (int X_Payroll_RunLine_ID)
	{
		if (X_Payroll_RunLine_ID < 1)
			set_ValueNoCheck (COLUMNNAME_X_Payroll_RunLine_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_X_Payroll_RunLine_ID, Integer.valueOf(X_Payroll_RunLine_ID));
	}

	/** Get Payroll RunLine.
		@return Payroll RunLine	  */
	public int getX_Payroll_RunLine_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_X_Payroll_RunLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}