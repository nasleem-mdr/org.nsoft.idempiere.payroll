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

/** Generated Model for X_Payroll_TaxRate
 *  @author iDempiere (generated)
 *  @version Release 13 - $Id$ */
@org.adempiere.base.Model(table="X_Payroll_TaxRate")
public class X_X_Payroll_TaxRate extends PO implements I_X_Payroll_TaxRate, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20260909L;

    /** Standard Constructor */
    public X_X_Payroll_TaxRate (Properties ctx, int X_Payroll_TaxRate_ID, String trxName)
    {
      super (ctx, X_Payroll_TaxRate_ID, trxName);
      /** if (X_Payroll_TaxRate_ID == 0)
        {
			setRate (Env.ZERO);
			setValidFrom (new Timestamp( System.currentTimeMillis() ));
			setX_Payroll_TaxRate_ID (0);
			setincomefrom (Env.ZERO);
			setschemetype (null);
        } */
    }

    /** Standard Constructor */
    public X_X_Payroll_TaxRate (Properties ctx, int X_Payroll_TaxRate_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, X_Payroll_TaxRate_ID, trxName, virtualColumns);
      /** if (X_Payroll_TaxRate_ID == 0)
        {
			setRate (Env.ZERO);
			setValidFrom (new Timestamp( System.currentTimeMillis() ));
			setX_Payroll_TaxRate_ID (0);
			setincomefrom (Env.ZERO);
			setschemetype (null);
        } */
    }

    /** Standard Constructor */
    public X_X_Payroll_TaxRate (Properties ctx, String X_Payroll_TaxRate_UU, String trxName)
    {
      super (ctx, X_Payroll_TaxRate_UU, trxName);
      /** if (X_Payroll_TaxRate_UU == null)
        {
			setRate (Env.ZERO);
			setValidFrom (new Timestamp( System.currentTimeMillis() ));
			setX_Payroll_TaxRate_ID (0);
			setincomefrom (Env.ZERO);
			setschemetype (null);
        } */
    }

    /** Standard Constructor */
    public X_X_Payroll_TaxRate (Properties ctx, String X_Payroll_TaxRate_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, X_Payroll_TaxRate_UU, trxName, virtualColumns);
      /** if (X_Payroll_TaxRate_UU == null)
        {
			setRate (Env.ZERO);
			setValidFrom (new Timestamp( System.currentTimeMillis() ));
			setX_Payroll_TaxRate_ID (0);
			setincomefrom (Env.ZERO);
			setschemetype (null);
        } */
    }

    /** Load Constructor */
    public X_X_Payroll_TaxRate (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_X_Payroll_TaxRate[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Category.
		@param Category Category
	*/
	public void setCategory (String Category)
	{
		set_Value (COLUMNNAME_Category, Category);
	}

	/** Get Category.
		@return Category	  */
	public String getCategory()
	{
		return (String)get_Value(COLUMNNAME_Category);
	}

	/** Set Rate.
		@param Rate Rate or Tax or Exchange
	*/
	public void setRate (BigDecimal Rate)
	{
		set_ValueNoCheck (COLUMNNAME_Rate, Rate);
	}

	/** Get Rate.
		@return Rate or Tax or Exchange
	  */
	public BigDecimal getRate()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Rate);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set Payroll Tax Rate.
		@param X_Payroll_TaxRate_ID Payroll Tax Rate
	*/
	public void setX_Payroll_TaxRate_ID (int X_Payroll_TaxRate_ID)
	{
		if (X_Payroll_TaxRate_ID < 1)
			set_ValueNoCheck (COLUMNNAME_X_Payroll_TaxRate_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_X_Payroll_TaxRate_ID, Integer.valueOf(X_Payroll_TaxRate_ID));
	}

	/** Get Payroll Tax Rate.
		@return Payroll Tax Rate	  */
	public int getX_Payroll_TaxRate_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_X_Payroll_TaxRate_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set incomefrom.
		@param incomefrom incomefrom
	*/
	public void setincomefrom (BigDecimal incomefrom)
	{
		set_Value (COLUMNNAME_incomefrom, incomefrom);
	}

	/** Get incomefrom.
		@return incomefrom	  */
	public BigDecimal getincomefrom()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_incomefrom);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set incometo.
		@param incometo incometo
	*/
	public void setincometo (BigDecimal incometo)
	{
		set_Value (COLUMNNAME_incometo, incometo);
	}

	/** Get incometo.
		@return incometo	  */
	public BigDecimal getincometo()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_incometo);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set schemetype.
		@param schemetype schemetype
	*/
	public void setschemetype (String schemetype)
	{
		set_Value (COLUMNNAME_schemetype, schemetype);
	}

	/** Get schemetype.
		@return schemetype	  */
	public String getschemetype()
	{
		return (String)get_Value(COLUMNNAME_schemetype);
	}
}