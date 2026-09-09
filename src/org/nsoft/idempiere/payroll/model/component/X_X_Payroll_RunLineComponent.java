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
package org.nsoft.idempiere.payroll.model.component;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for X_Payroll_RunLineComponent
 *  @author iDempiere (generated)
 *  @version Release 13 - $Id$ */
@org.adempiere.base.Model(table="X_Payroll_RunLineComponent")
public class X_X_Payroll_RunLineComponent extends PO implements I_X_Payroll_RunLineComponent, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20260909L;

    /** Standard Constructor */
    public X_X_Payroll_RunLineComponent (Properties ctx, int X_Payroll_RunLineComponent_ID, String trxName)
    {
      super (ctx, X_Payroll_RunLineComponent_ID, trxName);
      /** if (X_Payroll_RunLineComponent_ID == 0)
        {
			setAmount (Env.ZERO);
			setX_Payroll_Component_ID (0);
			setX_Payroll_RunLineComponent_ID (0);
			setX_Payroll_RunLine_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_X_Payroll_RunLineComponent (Properties ctx, int X_Payroll_RunLineComponent_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, X_Payroll_RunLineComponent_ID, trxName, virtualColumns);
      /** if (X_Payroll_RunLineComponent_ID == 0)
        {
			setAmount (Env.ZERO);
			setX_Payroll_Component_ID (0);
			setX_Payroll_RunLineComponent_ID (0);
			setX_Payroll_RunLine_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_X_Payroll_RunLineComponent (Properties ctx, String X_Payroll_RunLineComponent_UU, String trxName)
    {
      super (ctx, X_Payroll_RunLineComponent_UU, trxName);
      /** if (X_Payroll_RunLineComponent_UU == null)
        {
			setAmount (Env.ZERO);
			setX_Payroll_Component_ID (0);
			setX_Payroll_RunLineComponent_ID (0);
			setX_Payroll_RunLine_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_X_Payroll_RunLineComponent (Properties ctx, String X_Payroll_RunLineComponent_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, X_Payroll_RunLineComponent_UU, trxName, virtualColumns);
      /** if (X_Payroll_RunLineComponent_UU == null)
        {
			setAmount (Env.ZERO);
			setX_Payroll_Component_ID (0);
			setX_Payroll_RunLineComponent_ID (0);
			setX_Payroll_RunLine_ID (0);
        } */
    }

    /** Load Constructor */
    public X_X_Payroll_RunLineComponent (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_X_Payroll_RunLineComponent[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Amount.
		@param Amount Amount in a defined currency
	*/
	public void setAmount (BigDecimal Amount)
	{
		set_ValueNoCheck (COLUMNNAME_Amount, Amount);
	}

	/** Get Amount.
		@return Amount in a defined currency
	  */
	public BigDecimal getAmount()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Amount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	@Deprecated(since="13") // use better methods with cache
	public I_X_Payroll_Component getX_Payroll_Component() throws RuntimeException
	{
		return (I_X_Payroll_Component)MTable.get(getCtx(), I_X_Payroll_Component.Table_ID)
			.getPO(getX_Payroll_Component_ID(), get_TrxName());
	}

	/** Set Payroll Component.
		@param X_Payroll_Component_ID Payroll Component
	*/
	public void setX_Payroll_Component_ID (int X_Payroll_Component_ID)
	{
		if (X_Payroll_Component_ID < 1)
			set_ValueNoCheck (COLUMNNAME_X_Payroll_Component_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_X_Payroll_Component_ID, Integer.valueOf(X_Payroll_Component_ID));
	}

	/** Get Payroll Component.
		@return Payroll Component	  */
	public int getX_Payroll_Component_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_X_Payroll_Component_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Payroll RunLine Component.
		@param X_Payroll_RunLineComponent_ID Payroll RunLine Component
	*/
	public void setX_Payroll_RunLineComponent_ID (int X_Payroll_RunLineComponent_ID)
	{
		if (X_Payroll_RunLineComponent_ID < 1)
			set_ValueNoCheck (COLUMNNAME_X_Payroll_RunLineComponent_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_X_Payroll_RunLineComponent_ID, Integer.valueOf(X_Payroll_RunLineComponent_ID));
	}

	/** Get Payroll RunLine Component.
		@return Payroll RunLine Component	  */
	public int getX_Payroll_RunLineComponent_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_X_Payroll_RunLineComponent_ID);
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
