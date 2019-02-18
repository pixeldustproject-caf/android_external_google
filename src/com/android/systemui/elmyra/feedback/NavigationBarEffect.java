package com.google.android.systemui.elmyra.feedback;

import android.content.Context;

import com.android.systemui.navigation.Navigator;
import com.android.systemui.SysUiServiceProvider;
import com.android.systemui.statusbar.phone.NavigationBarView;
import com.android.systemui.statusbar.phone.StatusBar;
import com.google.android.systemui.elmyra.sensors.GestureSensor.DetectionProperties;

import java.util.ArrayList;
import java.util.List;

public abstract class NavigationBarEffect implements FeedbackEffect {
    private final Context mContext;
    private final List<FeedbackEffect> mFeedbackEffects = new ArrayList();

    public NavigationBarEffect(Context context) {
        mContext = context;
    }

    private void refreshFeedbackEffects() {
        StatusBar statusBar = (StatusBar) SysUiServiceProvider.getComponent(mContext, StatusBar.class);
        Navigator navigationBarView = statusBar.getNavigationBarView();
        if (statusBar == null || navigationBarView == null
                || navigationBarView.isFullGestureMode()) {
            mFeedbackEffects.clear();
            return;
        }
        if (!validateFeedbackEffects(mFeedbackEffects)) {
            mFeedbackEffects.clear();
        }
        if (mFeedbackEffects.isEmpty()) {
            mFeedbackEffects.addAll(findFeedbackEffects(navigationBarView));
        }
    }

    protected abstract List<FeedbackEffect> findFeedbackEffects(Navigator navigationBarView);

    protected boolean isActiveFeedbackEffect(FeedbackEffect feedbackEffect) {
        return true;
    }

    @Override
	public void onProgress(float f, int i) {
        refreshFeedbackEffects();
        mFeedbackEffects.forEach(
                feedbackEffect -> feedbackEffect.onProgress(f, i));
    }

    @Override
	public void onRelease() {
        refreshFeedbackEffects();
        mFeedbackEffects.forEach(
                feedbackEffect -> feedbackEffect.onRelease());
    }

    public void onResolve(DetectionProperties detectionProperties) {
        refreshFeedbackEffects();
        mFeedbackEffects.forEach(
                feedbackEffect -> feedbackEffect.onResolve(detectionProperties));
    }

    protected abstract boolean validateFeedbackEffects(List<FeedbackEffect> list);
}
