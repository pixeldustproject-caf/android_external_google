package com.google.android.systemui.elmyra;

import android.content.Context;
import com.android.systemui.R;

public class SnapshotConfiguration {
    private final int mCompleteGestures;
    private final int mIncompleteGestures;
    private final int mSnapshotDelayAfterGesture;

    public SnapshotConfiguration() {
        mCompleteGestures = 0;
        mIncompleteGestures = 0;
        mSnapshotDelayAfterGesture = 0;
    }

    public SnapshotConfiguration(Context context) {
        mCompleteGestures = context.getResources().getInteger(R.integer.elmyra_snapshot_logger_gesture_size);
        mIncompleteGestures = context.getResources().getInteger(R.integer.elmyra_snapshot_logger_incomplete_gesture_size);
        mSnapshotDelayAfterGesture = context.getResources().getInteger(R.integer.elmyra_snapshot_gesture_delay_ms);
    }

    public int getCompleteGestures() {
        return mCompleteGestures;
    }

    public int getIncompleteGestures() {
        return mIncompleteGestures;
    }

    public int getSnapshotDelayAfterGesture() {
        return mSnapshotDelayAfterGesture;
    }
}
