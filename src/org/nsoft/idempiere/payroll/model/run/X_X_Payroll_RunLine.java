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
	private static final long serialVersionUID = 20260910L;

    /** Standard Constructor */
    public X_X_Payroll_RunLine (Properties ctx, int X_Payroll_RunLine_ID, String trxName)
    {
      super (ctx, X_Payroll_RunLine_ID, trxName);
      /** if (X_Payroll_RunLine_ID == 0)
        {
			setBPJSBaseIncome (Env.ZERO);
			setCashGrossIncome (Env.ZERO);
			setCumulativeGrossBeforeThisRun (Env.ZERO);
			setHR_Employee_ID (0);
			setNetIncome (Env.ZERO);
			setPPh21WithheldPreviouslyThisPeriod (Env.ZERO);
			setPPh21_Amount (Env.ZERO);
			setTER_Category (null);
// A
			setTaxableGrossIncome (Env.ZERO);
			setTotalDeduction (Env.ZERO);
			setX_Payroll_RunLine_ID (0);
			setX_Payroll_Run_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_X_Payroll_RunLine (Properties ctx, int X_Payroll_RunLine_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, X_Payroll_RunLine_ID, trxName, virtualColumns);
      /** if (X_Payroll_RunLine_ID == 0)
        {
			setBPJSBaseIncome (Env.ZERO);
			setCashGrossIncome (Env.ZERO);
			setCumulativeGrossBeforeThisRun (Env.ZERO);
			setHR_Employee_ID (0);
			setNetIncome (Env.ZERO);
			setPPh21WithheldPreviouslyThisPeriod (Env.ZERO);
			setPPh21_Amount (Env.ZERO);
			setTER_Category (null);
// A
			setTaxableGrossIncome (Env.ZERO);
			setTotalDeduction (Env.ZERO);
			setX_Payroll_RunLine_ID (0);
			setX_Payroll_Run_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_X_Payroll_RunLine (Properties ctx, String X_Payroll_RunLine_UU, String trxName)
    {
      super (ctx, X_Payroll_RunLine_UU, trxName);
      /** if (X_Payroll_RunLine_UU == null)
        {
			setBPJSBaseIncome (Env.ZERO);
			setCashGrossIncome (Env.ZERO);
			setCumulativeGrossBeforeThisRun (Env.ZERO);
			setHR_Employee_ID (0);
			setNetIncome (Env.ZERO);
			setPPh21WithheldPreviouslyThisPeriod (Env.ZERO);
			setPPh21_Amount (Env.ZERO);
			setTER_Category (null);
// A
			setTaxableGrossIncome (Env.ZERO);
			setTotalDeduction (Env.ZERO);
			setX_Payroll_RunLine_ID (0);
			setX_Payroll_Run_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_X_Payroll_RunLine (Properties ctx, String X_Payroll_RunLine_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, X_Payroll_RunLine_UU, trxName, virtualColumns);
      /** if (X_Payroll_RunLine_UU == null)
        {
			setBPJSBaseIncome (Env.ZERO);
			setCashGrossIncome (Env.ZERO);
			setCumulativeGrossBeforeThisRun (Env.ZERO);
			setHR_Employee_ID (0);
			setNetIncome (Env.ZERO);
			setPPh21WithheldPreviouslyThisPeriod (Env.ZERO);
			setPPh21_Amount (Env.ZERO);
			setTER_Category (null);
// A
			setTaxableGrossIncome (Env.ZERO);
			setTotalDeduction (Env.ZERO);
			setX_Payroll_RunLine_ID (0);
			setX_Payroll_Run_ID (0);
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

	/** Set BPJSBaseIncome.
		@param BPJSBaseIncome BPJSBaseIncome
	*/
	public void setBPJSBaseIncome (BigDecimal BPJSBaseIncome)
	{
		set_Value (COLUMNNAME_BPJSBaseIncome, BPJSBaseIncome);
	}

	/** Get BPJSBaseIncome.
		@return BPJSBaseIncome	  */
	public BigDecimal getBPJSBaseIncome()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_BPJSBaseIncome);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set CashGrossIncome.
		@param CashGrossIncome CashGrossIncome
	*/
	public void setCashGrossIncome (BigDecimal CashGrossIncome)
	{
		set_Value (COLUMNNAME_CashGrossIncome, CashGrossIncome);
	}

	/** Get CashGrossIncome.
		@return CashGrossIncome	  */
	public BigDecimal getCashGrossIncome()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_CashGrossIncome);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set CumulativeGrossBeforeThisRun.
		@param CumulativeGrossBeforeThisRun CumulativeGrossBeforeThisRun
	*/
	public void setCumulativeGrossBeforeThisRun (BigDecimal CumulativeGrossBeforeThisRun)
	{
		set_Value (COLUMNNAME_CumulativeGrossBeforeThisRun, CumulativeGrossBeforeThisRun);
	}

	/** Get CumulativeGrossBeforeThisRun.
		@return CumulativeGrossBeforeThisRun	  */
	public BigDecimal getCumulativeGrossBeforeThisRun()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_CumulativeGrossBeforeThisRun);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set NetIncome.
		@param NetIncome NetIncome
	*/
	public void setNetIncome (BigDecimal NetIncome)
	{
		set_Value (COLUMNNAME_NetIncome, NetIncome);
	}

	/** Get NetIncome.
		@return NetIncome	  */
	public BigDecimal getNetIncome()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_NetIncome);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set PPh21WithheldPreviouslyThisPeriod.
		@param PPh21WithheldPreviouslyThisPeriod PPh21WithheldPreviouslyThisPeriod
	*/
	public void setPPh21WithheldPreviouslyThisPeriod (BigDecimal PPh21WithheldPreviouslyThisPeriod)
	{
		set_Value (COLUMNNAME_PPh21WithheldPreviouslyThisPeriod, PPh21WithheldPreviouslyThisPeriod);
	}

	/** Get PPh21WithheldPreviouslyThisPeriod.
		@return PPh21WithheldPreviouslyThisPeriod	  */
	public BigDecimal getPPh21WithheldPreviouslyThisPeriod()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PPh21WithheldPreviouslyThisPeriod);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set PPh21_Amount.
		@param PPh21_Amount PPh21_Amount
	*/
	public void setPPh21_Amount (BigDecimal PPh21_Amount)
	{
		set_Value (COLUMNNAME_PPh21_Amount, PPh21_Amount);
	}

	/** Get PPh21_Amount.
		@return PPh21_Amount	  */
	public BigDecimal getPPh21_Amount()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PPh21_Amount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** TK/0, TK/1, K/0 = A */
	public static final String TER_CATEGORY_TK0TK1K0 = "A";
	/** TK/2, TK/3, K/1, K/2 = B */
	public static final String TER_CATEGORY_TK2TK3K1K2 = "B";
	/** K/3 = C */
	public static final String TER_CATEGORY_K3 = "C";
	/** Set TER_Category.
		@param TER_Category TER_Category
	*/
	public void setTER_Category (String TER_Category)
	{

		set_Value (COLUMNNAME_TER_Category, TER_Category);
	}

	/** Get TER_Category.
		@return TER_Category	  */
	public String getTER_Category()
	{
		return (String)get_Value(COLUMNNAME_TER_Category);
	}

	/** Set TER_RateApplied.
		@param TER_RateApplied TER_RateApplied
	*/
	public void setTER_RateApplied (BigDecimal TER_RateApplied)
	{
		set_Value (COLUMNNAME_TER_RateApplied, TER_RateApplied);
	}

	/** Get TER_RateApplied.
		@return TER_RateApplied	  */
	public BigDecimal getTER_RateApplied()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TER_RateApplied);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set TaxableGrossIncome.
		@param TaxableGrossIncome TaxableGrossIncome
	*/
	public void setTaxableGrossIncome (BigDecimal TaxableGrossIncome)
	{
		set_Value (COLUMNNAME_TaxableGrossIncome, TaxableGrossIncome);
	}

	/** Get TaxableGrossIncome.
		@return TaxableGrossIncome	  */
	public BigDecimal getTaxableGrossIncome()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TaxableGrossIncome);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set TotalDeduction.
		@param TotalDeduction TotalDeduction
	*/
	public void setTotalDeduction (BigDecimal TotalDeduction)
	{
		set_Value (COLUMNNAME_TotalDeduction, TotalDeduction);
	}

	/** Get TotalDeduction.
		@return TotalDeduction	  */
	public BigDecimal getTotalDeduction()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalDeduction);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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
}