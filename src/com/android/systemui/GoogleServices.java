package com.google.android.systemui;

import com.android.systemui.SysUiServiceProvider;
import com.android.systemui.VendorServices;
import com.android.systemui.statusbar.phone.StatusBar;
import com.google.android.systemui.elmyra.ElmyraContext;
import com.google.android.systemui.elmyra.ElmyraService;
import com.google.android.systemui.elmyra.ServiceConfigurationGoogle;
import java.util.ArrayList;

public class GoogleServices extends VendorServices {
    private ArrayList<Object> mServices = new ArrayList();

    private void addService(Object obj) {
        if (obj != null) {
            this.mServices.add(obj);
        }
    }

    public void start() {
        StatusBar statusBar = (StatusBar) SysUiServiceProvider.getComponent(this.mContext, StatusBar.class);

        // If the active edge sensors are available, fire up the Elmyra service.
        if (new ElmyraContext(this.mContext).isAvailable()) {
            addService(new ElmyraService(this.mContext, new ServiceConfigurationGoogle(this.mContext)));
        }
    }
}
