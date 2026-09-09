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

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for X_Payroll_Period
 *  @author iDempiere (generated)
 *  @version Release 13 - $Id$ */
@org.adempiere.base.Model(table="X_Payroll_Period")
public class X_X_Payroll_Period extends PO implements I_X_Payroll_Period, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20260909L;

    /** Standard Constructor */
    public X_X_Payroll_Period (Properties ctx, String X_Payroll_Period_UU, String trxName)
    {
      super (ctx, X_Payroll_Period_UU, trxName);
      /** if (X_Payroll_Period_UU == null)
        {
			setDateFrom (new Timestamp( System.currentTimeMillis() ));
			setDateTo (new Timestamp( System.currentTimeMillis() ));
			setDocStatus (null);
// DR
			setX_Payroll_Period_ID (0);
			setisdecemberreconciliation (false);
// N
			setperiodname (null);
        } */
    }

    /** Standard Constructor */
    public X_X_Payroll_Period (Properties ctx, String X_Payroll_Period_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, X_Payroll_Period_UU, trxName, virtualColumns);
      /** if (X_Payroll_Period_UU == null)
        {
			setDateFrom (new Timestamp( System.currentTimeMillis() ));
			setDateTo (new Timestamp( System.currentTimeMillis() ));
			setDocStatus (null);
// DR
			setX_Payroll_Period_ID (0);
			setisdecemberreconciliation (false);
// N
			setperiodname (null);
        } */
    }

    /** Load Constructor */
    public X_X_Payroll_Period (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_X_Payroll_Period[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Date From.
		@param DateFrom Starting date for a range
	*/
	public void setDateFrom (Timestamp DateFrom)
	{
		set_Value (COLUMNNAME_DateFrom, DateFrom);
	}

	/** Get Date From.
		@return Starting date for a range
	  */
	public Timestamp getDateFrom()
	{
		return (Timestamp)get_Value(COLUMNNAME_DateFrom);
	}

	/** Set Date To.
		@param DateTo End date of a date range
	*/
	public void setDateTo (Timestamp DateTo)
	{
		set_Value (COLUMNNAME_DateTo, DateTo);
	}

	/** Get Date To.
		@return End date of a date range
	  */
	public Timestamp getDateTo()
	{
		return (Timestamp)get_Value(COLUMNNAME_DateTo);
	}

	/** DocStatus AD_Reference_ID=131 */
	public static final int DOCSTATUS_AD_Reference_ID=131;
	/** Unknown = ?? */
	public static final String DOCSTATUS_Unknown = "??";
	/** Approved = AP */
	public static final String DOCSTATUS_Approved = "AP";
	/** Closed = CL */
	public static final String DOCSTATUS_Closed = "CL";
	/** Completed = CO */
	public static final String DOCSTATUS_Completed = "CO";
	/** Drafted = DR */
	public static final String DOCSTATUS_Drafted = "DR";
	/** Invalid = IN */
	public static final String DOCSTATUS_Invalid = "IN";
	/** In Progress = IP */
	public static final String DOCSTATUS_InProgress = "IP";
	/** Not Approved = NA */
	public static final String DOCSTATUS_NotApproved = "NA";
	/** Reversed = RE */
	public static final String DOCSTATUS_Reversed = "RE";
	/** Voided = VO */
	public static final String DOCSTATUS_Voided = "VO";
	/** Waiting Confirmation = WC */
	public static final String DOCSTATUS_WaitingConfirmation = "WC";
	/** Waiting Payment = WP */
	public static final String DOCSTATUS_WaitingPayment = "WP";
	/** Set Document Status.
		@param DocStatus The current status of the document
	*/
	public void setDocStatus (String DocStatus)
	{

		set_Value (COLUMNNAME_DocStatus, DocStatus);
	}

	/** Get Document Status.
		@return The current status of the document
	  */
	public String getDocStatus()
	{
		return (String)get_Value(COLUMNNAME_DocStatus);
	}

	@Deprecated(since="13") // use better methods with cache
	public I_X_Payroll_Period getX_Payroll_Period() throws RuntimeException
	{
		return (I_X_Payroll_Period)MTable.get(getCtx(), I_X_Payroll_Period.Table_ID)
			.getPO(getX_Payroll_Period_ID(), get_TrxName());
	}

	/** Set Payroll ID.
		@param X_Payroll_Period_ID Payroll ID
	*/
	public void setX_Payroll_Period_ID (int X_Payroll_Period_ID)
	{
		if (X_Payroll_Period_ID < 1)
			set_Value (COLUMNNAME_X_Payroll_Period_ID, null);
		else
			set_Value (COLUMNNAME_X_Payroll_Period_ID, Integer.valueOf(X_Payroll_Period_ID));
	}

	/** Get Payroll ID.
		@return Payroll ID	  */
	public int getX_Payroll_Period_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_X_Payroll_Period_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set isdecemberreconciliation.
		@param isdecemberreconciliation isdecemberreconciliation
	*/
	public void setisdecemberreconciliation (boolean isdecemberreconciliation)
	{
		set_Value (COLUMNNAME_isdecemberreconciliation, Boolean.valueOf(isdecemberreconciliation));
	}

	/** Get isdecemberreconciliation.
		@return isdecemberreconciliation	  */
	public boolean isdecemberreconciliation()
	{
		Object oo = get_Value(COLUMNNAME_isdecemberreconciliation);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set periodname.
		@param periodname periodname
	*/
	public void setperiodname (String periodname)
	{
		set_Value (COLUMNNAME_periodname, periodname);
	}

	/** Get periodname.
		@return periodname	  */
	public String getperiodname()
	{
		return (String)get_Value(COLUMNNAME_periodname);
	}
}