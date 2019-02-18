package com.google.android.systemui.elmyra.feedback;

import android.content.ContentResolver;
import android.content.Context;
import android.os.PowerManager;
import android.os.UserHandle;
import android.provider.Settings;
import android.view.View;

import com.android.systemui.navigation.Navigator;
import com.android.systemui.SysUiServiceProvider;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.statusbar.phone.NavigationBarView;

import java.util.Arrays;
import java.util.List;

public class SquishyNavigationButtons extends NavigationBarEffect {

    private final KeyguardViewMediator mKeyguardViewMediator;
    private final SquishyViewController mViewController;

    private ContentResolver mResolver;
    private PowerManager mPm;

    public SquishyNavigationButtons(Context context) {
        super(context);
        mResolver = context.getContentResolver();
        mViewController = new SquishyViewController(context);
        mKeyguardViewMediator = (KeyguardViewMediator) SysUiServiceProvider.getComponent(
            context, KeyguardViewMediator.class);
        mPm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    }

    protected List<FeedbackEffect> findFeedbackEffects(Navigator navigationBarView) {
        mViewController.clearViews();
        // Back button views
        List views = navigationBarView.getBackButton().getViews();
        views.forEach(v -> mViewController.addLeftView((View) v));
        // Recent button views
        views = navigationBarView.getRecentsButton().getViews();
        views.forEach(v -> mViewController.addRightView((View) v));

        return Arrays.asList(new FeedbackEffect[]{mViewController});
    }

    @Override
    protected boolean isActiveFeedbackEffect(FeedbackEffect feedbackEffect) {
        boolean squeezeSelection = Settings.Secure.getIntForUser(mResolver,
                Settings.Secure.SQUEEZE_SELECTION, 0, UserHandle.USER_CURRENT) == 0;

        /* Make sure we're not calling the navbar animation if battery saver
           mode is on and/or if the screen is off.*/
        return !mPm.isPowerSaveMode() && !squeezeSelection && mPm.isScreenOn()
                && !mKeyguardViewMediator.isShowingAndNotOccluded();
    }

    @Override
    protected boolean validateFeedbackEffects(List<FeedbackEffect> list) {
        return mViewController.isAttachedToWindow();
    }
}
