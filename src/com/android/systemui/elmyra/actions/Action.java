package com.google.android.systemui.elmyra.actions;

import android.content.Context;
import android.os.Handler;
import com.google.android.systemui.elmyra.feedback.FeedbackEffect;
import com.google.android.systemui.elmyra.sensors.GestureSensor.DetectionProperties;
import java.util.ArrayList;
import java.util.List;

public abstract class Action {
    private final Context mContext;
    private final List<FeedbackEffect> mFeedbackEffects = new ArrayList();
    private final Handler mHandler;
    private Listener mListener;

    public interface Listener {
        void onActionAvailabilityChanged(Action action);
    }

    public Action(Context context, List<FeedbackEffect> list) {
        mContext = context;
        mHandler = new Handler(context.getMainLooper());
        if (list != null) {
            mFeedbackEffects.addAll(list);
        }
    }

    protected Context getContext() {
        return mContext;
    }

    public abstract boolean isAvailable();

    protected void notifyListener() {
        if (mListener != null) {
            mHandler.post(new ActionNotifyHelper(this));
        }
        if (!isAvailable()) {
            mHandler.post(new ActionFeedbackHelper(this));
        }
    }

    public void onProgress(float f, int i) {
    }

    public abstract void onTrigger(DetectionProperties detectionProperties);

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
	public String toString() {
        return getClass().getSimpleName();
    }

    protected void triggerFeedbackEffects(DetectionProperties detectionProperties) {
        if (isAvailable()) {
            mFeedbackEffects.forEach(
                feedbackEffect -> feedbackEffect.onResolve(detectionProperties));
        }
    }

    protected void updateFeedbackEffects(float f, int i) {
        if (f == 0.0f || i == 0) {
            mFeedbackEffects.forEach(
                    feedbackEffect -> feedbackEffect.onRelease());
        } else if (isAvailable()) {
            mFeedbackEffects.forEach(
                    feedbackEffect -> feedbackEffect.onProgress(f, i));
        }
    }

    // Small helper classes for Action class only, let's make them private.
    private class ActionNotifyHelper implements Runnable {
        private Action action;

        public ActionNotifyHelper(Action act) {
            action = act;
        }

        public void actionNotifyListener(Action act) {
            if (action.mListener != null) {
                action.mListener.onActionAvailabilityChanged(act);
            }
        }

        @Override
        public final void run() {
            actionNotifyListener(action);
        }
    }

    private class ActionFeedbackHelper implements Runnable {
        private Action action;

        public ActionFeedbackHelper(Action act) {
           action = act;
        }

        @Override
        public final void run() {
            action.updateFeedbackEffects(0.0f, 0);
        }
    }
}
