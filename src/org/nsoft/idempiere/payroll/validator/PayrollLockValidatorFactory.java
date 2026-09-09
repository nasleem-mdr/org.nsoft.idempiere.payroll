package org.nsoft.idempiere.payroll.validator;

import org.adempiere.base.IModelValidatorFactory;
import org.compiere.model.ModelValidator;
import org.compiere.util.Env;

import java.util.Properties;

/**
 * Intermediary factory — WAJIB ada terpisah dari validator itu sendiri,
 * ini pola yang sudah established di org.nsoft.workflow.activities untuk
 * registrasi ModelValidator via OSGi Declarative Services.
 */
public class PayrollLockValidatorFactory implements IModelValidatorFactory {
    @Override
    public ModelValidator newInstance(Properties ctx, int adClientId) {
        return new PayrollLockValidator();
    }
}
