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

/** Generated Model for X_Payroll_RunLine
 *  @author iDempiere (generated)
 *  @version Release 13 - $Id$ */
@org.adempiere.base.Model(table="X_Payroll_RunLine")
public class X_X_Payroll_RunLine extends PO implements I_X_Payroll_RunLine, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20260909L;

    /** Standard Constructor */
    public X_X_Payroll_RunLine (Properties ctx, int X_Payroll_RunLine_ID, String trxName)
    {
      super (ctx, X_Payroll_RunLine_ID, trxName);
      /** if (X_Payroll_RunLine_ID == 0)
        {
			setHR_Employee_ID (0);
			setX_Payroll_RunLine_ID (0);
			setX_Payroll_Run_ID (0);
			setbpjsbaseincome (Env.ZERO);
			setcashgrossincome (Env.ZERO);
			setcumulativegrossbeforethisrun (Env.ZERO);
			setnetincome (Env.ZERO);
			setpph21_amount (Env.ZERO);
			setpph21withheldpreviouslythisperiod (Env.ZERO);
			settaxablegrossincome (Env.ZERO);
			setter_category (false);
// N
			settotaldeduction (Env.ZERO);
        } */
    }

    /** Standard Constructor */
    public X_X_Payroll_RunLine (Properties ctx, int X_Payroll_RunLine_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, X_Payroll_RunLine_ID, trxName, virtualColumns);
      /** if (X_Payroll_RunLine_ID == 0)
        {
			setHR_Employee_ID (0);
			setX_Payroll_RunLine_ID (0);
			setX_Payroll_Run_ID (0);
			setbpjsbaseincome (Env.ZERO);
			setcashgrossincome (Env.ZERO);
			setcumulativegrossbeforethisrun (Env.ZERO);
			setnetincome (Env.ZERO);
			setpph21_amount (Env.ZERO);
			setpph21withheldpreviouslythisperiod (Env.ZERO);
			settaxablegrossincome (Env.ZERO);
			setter_category (false);
// N
			settotaldeduction (Env.ZERO);
        } */
    }

    /** Standard Constructor */
    public X_X_Payroll_RunLine (Properties ctx, String X_Payroll_RunLine_UU, String trxName)
    {
      super (ctx, X_Payroll_RunLine_UU, trxName);
      /** if (X_Payroll_RunLine_UU == null)
        {
			setHR_Employee_ID (0);
			setX_Payroll_RunLine_ID (0);
			setX_Payroll_Run_ID (0);
			setbpjsbaseincome (Env.ZERO);
			setcashgrossincome (Env.ZERO);
			setcumulativegrossbeforethisrun (Env.ZERO);
			setnetincome (Env.ZERO);
			setpph21_amount (Env.ZERO);
			setpph21withheldpreviouslythisperiod (Env.ZERO);
			settaxablegrossincome (Env.ZERO);
			setter_category (false);
// N
			settotaldeduction (Env.ZERO);
        } */
    }

    /** Standard Constructor */
    public X_X_Payroll_RunLine (Properties ctx, String X_Payroll_RunLine_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, X_Payroll_RunLine_UU, trxName, virtualColumns);
      /** if (X_Payroll_RunLine_UU == null)
        {
			setHR_Employee_ID (0);
			setX_Payroll_RunLine_ID (0);
			setX_Payroll_Run_ID (0);
			setbpjsbaseincome (Env.ZERO);
			setcashgrossincome (Env.ZERO);
			setcumulativegrossbeforethisrun (Env.ZERO);
			setnetincome (Env.ZERO);
			setpph21_amount (Env.ZERO);
			setpph21withheldpreviouslythisperiod (Env.ZERO);
			settaxablegrossincome (Env.ZERO);
			setter_category (false);
// N
			settotaldeduction (Env.ZERO);
        } */
    }

    /** Load Constructor */
    public X_X_Payroll_RunLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_X_Payroll_RunLine[")
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

	@Deprecated(since="13") // use better methods with cache
	public I_X_Payroll_Run getX_Payroll_Run() throws RuntimeException
	{
		return (I_X_Payroll_Run)MTable.get(getCtx(), I_X_Payroll_Run.Table_ID)
			.getPO(getX_Payroll_Run_ID(), get_TrxName());
	}

	/** Set Payroll Run ID.
		@param X_Payroll_Run_ID Payroll Run ID
	*/
	public void setX_Payroll_Run_ID (int X_Payroll_Run_ID)
	{
		if (X_Payroll_Run_ID < 1)
			set_Value (COLUMNNAME_X_Payroll_Run_ID, null);
		else
			set_Value (COLUMNNAME_X_Payroll_Run_ID, Integer.valueOf(X_Payroll_Run_ID));
	}

	/** Get Payroll Run ID.
		@return Payroll Run ID	  */
	public int getX_Payroll_Run_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_X_Payroll_Run_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set bpjsbaseincome.
		@param bpjsbaseincome bpjsbaseincome
	*/
	public void setbpjsbaseincome (BigDecimal bpjsbaseincome)
	{
		set_Value (COLUMNNAME_bpjsbaseincome, bpjsbaseincome);
	}

	/** Get bpjsbaseincome.
		@return bpjsbaseincome	  */
	public BigDecimal getbpjsbaseincome()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_bpjsbaseincome);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set cashgrossincome.
		@param cashgrossincome cashgrossincome
	*/
	public void setcashgrossincome (BigDecimal cashgrossincome)
	{
		set_Value (COLUMNNAME_cashgrossincome, cashgrossincome);
	}

	/** Get cashgrossincome.
		@return cashgrossincome	  */
	public BigDecimal getcashgrossincome()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_cashgrossincome);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set cumulativegrossbeforethisrun.
		@param cumulativegrossbeforethisrun cumulativegrossbeforethisrun
	*/
	public void setcumulativegrossbeforethisrun (BigDecimal cumulativegrossbeforethisrun)
	{
		set_Value (COLUMNNAME_cumulativegrossbeforethisrun, cumulativegrossbeforethisrun);
	}

	/** Get cumulativegrossbeforethisrun.
		@return cumulativegrossbeforethisrun	  */
	public BigDecimal getcumulativegrossbeforethisrun()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_cumulativegrossbeforethisrun);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set netincome.
		@param netincome netincome
	*/
	public void setnetincome (BigDecimal netincome)
	{
		set_Value (COLUMNNAME_netincome, netincome);
	}

	/** Get netincome.
		@return netincome	  */
	public BigDecimal getnetincome()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_netincome);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set pph21_amount.
		@param pph21_amount pph21_amount
	*/
	public void setpph21_amount (BigDecimal pph21_amount)
	{
		set_Value (COLUMNNAME_pph21_amount, pph21_amount);
	}

	/** Get pph21_amount.
		@return pph21_amount	  */
	public BigDecimal getpph21_amount()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_pph21_amount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set pph21withheldpreviouslythisperiod.
		@param pph21withheldpreviouslythisperiod pph21withheldpreviouslythisperiod
	*/
	public void setpph21withheldpreviouslythisperiod (BigDecimal pph21withheldpreviouslythisperiod)
	{
		set_Value (COLUMNNAME_pph21withheldpreviouslythisperiod, pph21withheldpreviouslythisperiod);
	}

	/** Get pph21withheldpreviouslythisperiod.
		@return pph21withheldpreviouslythisperiod	  */
	public BigDecimal getpph21withheldpreviouslythisperiod()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_pph21withheldpreviouslythisperiod);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set taxablegrossincome.
		@param taxablegrossincome taxablegrossincome
	*/
	public void settaxablegrossincome (BigDecimal taxablegrossincome)
	{
		set_Value (COLUMNNAME_taxablegrossincome, taxablegrossincome);
	}

	/** Get taxablegrossincome.
		@return taxablegrossincome	  */
	public BigDecimal gettaxablegrossincome()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_taxablegrossincome);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set ter_category.
		@param ter_category ter_category
	*/
	public void setter_category (boolean ter_category)
	{
		set_Value (COLUMNNAME_ter_category, Boolean.valueOf(ter_category));
	}

	/** Get ter_category.
		@return ter_category	  */
	public boolean ister_category()
	{
		Object oo = get_Value(COLUMNNAME_ter_category);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set ter_rateapplied.
		@param ter_rateapplied ter_rateapplied
	*/
	public void setter_rateapplied (BigDecimal ter_rateapplied)
	{
		set_Value (COLUMNNAME_ter_rateapplied, ter_rateapplied);
	}

	/** Get ter_rateapplied.
		@return ter_rateapplied	  */
	public BigDecimal getter_rateapplied()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ter_rateapplied);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set totaldeduction.
		@param totaldeduction totaldeduction
	*/
	public void settotaldeduction (BigDecimal totaldeduction)
	{
		set_Value (COLUMNNAME_totaldeduction, totaldeduction);
	}

	/** Get totaldeduction.
		@return totaldeduction	  */
	public BigDecimal gettotaldeduction()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_totaldeduction);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}