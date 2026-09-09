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

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for X_Payroll_Component
 *  @author iDempiere (generated)
 *  @version Release 13 - $Id$ */
@org.adempiere.base.Model(table="X_Payroll_Component")
public class X_X_Payroll_Component extends PO implements I_X_Payroll_Component, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20260909L;

    /** Standard Constructor */
    public X_X_Payroll_Component (Properties ctx, int X_Payroll_Component_ID, String trxName)
    {
      super (ctx, X_Payroll_Component_ID, trxName);
      /** if (X_Payroll_Component_ID == 0)
        {
			setComponentType (null);
// CO
			setName (null);
			setX_Payroll_Component_ID (0);
			setisbpjsbase (false);
// N
			setistaxable (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_X_Payroll_Component (Properties ctx, int X_Payroll_Component_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, X_Payroll_Component_ID, trxName, virtualColumns);
      /** if (X_Payroll_Component_ID == 0)
        {
			setComponentType (null);
// CO
			setName (null);
			setX_Payroll_Component_ID (0);
			setisbpjsbase (false);
// N
			setistaxable (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_X_Payroll_Component (Properties ctx, String X_Payroll_Component_UU, String trxName)
    {
      super (ctx, X_Payroll_Component_UU, trxName);
      /** if (X_Payroll_Component_UU == null)
        {
			setComponentType (null);
// CO
			setName (null);
			setX_Payroll_Component_ID (0);
			setisbpjsbase (false);
// N
			setistaxable (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_X_Payroll_Component (Properties ctx, String X_Payroll_Component_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, X_Payroll_Component_UU, trxName, virtualColumns);
      /** if (X_Payroll_Component_UU == null)
        {
			setComponentType (null);
// CO
			setName (null);
			setX_Payroll_Component_ID (0);
			setisbpjsbase (false);
// N
			setistaxable (false);
// N
        } */
    }

    /** Load Constructor */
    public X_X_Payroll_Component (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_X_Payroll_Component[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** ComponentType AD_Reference_ID=53225 */
	public static final int COMPONENTTYPE_AD_Reference_ID=53225;
	/** By-Product = BY */
	public static final String COMPONENTTYPE_By_Product = "BY";
	/** Component = CO */
	public static final String COMPONENTTYPE_Component = "CO";
	/** Co-Product = CP */
	public static final String COMPONENTTYPE_Co_Product = "CP";
	/** Option = OP */
	public static final String COMPONENTTYPE_Option = "OP";
	/** Phantom = PH */
	public static final String COMPONENTTYPE_Phantom = "PH";
	/** Packing = PK */
	public static final String COMPONENTTYPE_Packing = "PK";
	/** Planning = PL */
	public static final String COMPONENTTYPE_Planning = "PL";
	/** Tools = TL */
	public static final String COMPONENTTYPE_Tools = "TL";
	/** Variant = VA */
	public static final String COMPONENTTYPE_Variant = "VA";
	/** Set Component Type.
		@param ComponentType Component Type for a Bill of Material or Formula
	*/
	public void setComponentType (String ComponentType)
	{

		set_Value (COLUMNNAME_ComponentType, ComponentType);
	}

	/** Get Component Type.
		@return Component Type for a Bill of Material or Formula
	  */
	public String getComponentType()
	{
		return (String)get_Value(COLUMNNAME_ComponentType);
	}

	/** Set Name.
		@param Name Alphanumeric identifier of the entity
	*/
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName()
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

	/** Set Sequence.
		@param SeqNo Method of ordering records; lowest number comes first
	*/
	public void setSeqNo (int SeqNo)
	{
		set_Value (COLUMNNAME_SeqNo, Integer.valueOf(SeqNo));
	}

	/** Get Sequence.
		@return Method of ordering records; lowest number comes first
	  */
	public int getSeqNo()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SeqNo);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set isbpjsbase.
		@param isbpjsbase isbpjsbase
	*/
	public void setisbpjsbase (boolean isbpjsbase)
	{
		set_Value (COLUMNNAME_isbpjsbase, Boolean.valueOf(isbpjsbase));
	}

	/** Get isbpjsbase.
		@return isbpjsbase	  */
	public boolean isbpjsbase()
	{
		Object oo = get_Value(COLUMNNAME_isbpjsbase);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set istaxable.
		@param istaxable istaxable
	*/
	public void setistaxable (boolean istaxable)
	{
		set_Value (COLUMNNAME_istaxable, Boolean.valueOf(istaxable));
	}

	/** Get istaxable.
		@return istaxable	  */
	public boolean istaxable()
	{
		Object oo = get_Value(COLUMNNAME_istaxable);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}
}