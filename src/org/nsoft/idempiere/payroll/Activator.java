package org.nsoft.idempiere.payroll;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Plain BundleActivator — TIDAK perlu OSGI-INF sendiri (beda dengan
 * IModelValidatorFactory di atas yang pakai Declarative Services).
 * Bundle-Activator di-declare langsung di MANIFEST.MF header.
 */
public class Activator implements BundleActivator {

    @Override
    public void start(BundleContext context) throws Exception {
        // Tidak ada inisialisasi khusus yang dibutuhkan saat ini —
        // SvrProcess di-load on-demand lewat AD_Process.ClassName,
        // tidak perlu registrasi eksplisit di sini.
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        // no-op
    }
}
