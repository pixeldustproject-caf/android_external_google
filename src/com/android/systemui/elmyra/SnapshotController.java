package com.google.android.systemui.elmyra;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.google.android.systemui.elmyra.proto.nano.SnapshotMessages.SnapshotHeader;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import com.google.android.systemui.elmyra.sensors.GestureSensor.DetectionProperties;
import java.util.Random;

public final class SnapshotController implements GestureSensor.Listener {
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            if (message.what == 1) {
                requestSnapshot((SnapshotHeader) message.obj);
            }
        }
    };
    private int mLastGestureStage = 0;
    private final int mSnapshotDelayAfterGesture;
    private Listener mSnapshotListener;

    public interface Listener {
        void onSnapshotRequested(SnapshotHeader snapshotHeader);
    }

    public SnapshotController(SnapshotConfiguration snapshotConfiguration) {
       mSnapshotDelayAfterGesture = snapshotConfiguration.getSnapshotDelayAfterGesture();
    }

    private void requestSnapshot(SnapshotHeader snapshotHeader) {
        if (mSnapshotListener != null) {
           mSnapshotListener.onSnapshotRequested(snapshotHeader);
        }
    }

    public void onGestureDetected(GestureSensor gestureSensor, DetectionProperties detectionProperties) {
        SnapshotHeader snapshotHeader = new SnapshotHeader();
        snapshotHeader.gestureType = 1;
        snapshotHeader.identifier = detectionProperties != null ? detectionProperties.getActionId() : 0;

        mLastGestureStage = 0;
        mHandler.sendMessageDelayed(mHandler.obtainMessage(1, snapshotHeader), (long) mSnapshotDelayAfterGesture);
    }

    public void onGestureProgress(GestureSensor gestureSensor, float f, int i) {
        if (mLastGestureStage == 2 && i != 2) {
            SnapshotHeader snapshotHeader = new SnapshotHeader();
            snapshotHeader.identifier = new Random().nextLong();
            snapshotHeader.gestureType = 2;
            requestSnapshot(snapshotHeader);
        }
       mLastGestureStage = i;
    }

    public void setListener(Listener listener) {
       mSnapshotListener = listener;
    }
}
