package com.google.android.systemui.elmyra;

import android.os.Binder;
import com.google.android.systemui.elmyra.proto.nano.ElmyraChassis.SensorEvent;
import com.google.android.systemui.elmyra.proto.nano.SnapshotMessages;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class SnapshotLogger {
    private final int mSnapshotCapacity;
    private List<Snapshot> mSnapshots;

    public class Snapshot {
        final SnapshotMessages.Snapshot mSnapshot;
        final long mTimestamp;

        Snapshot(SnapshotMessages.Snapshot snapshot, long timeStamp) {
            mSnapshot = snapshot;
            mTimestamp = timeStamp;
        }

        public SnapshotMessages.Snapshot getSnapshot() {
            return mSnapshot;
        }

        long getTimestamp() {
            return mTimestamp;
        }
    }

    public SnapshotLogger(int size) {
        mSnapshotCapacity = size;
        mSnapshots = new ArrayList(size);
    }

    public void addSnapshot(SnapshotMessages.Snapshot snapshot, long timeStamp) {
        if (mSnapshots.size() == mSnapshotCapacity) {
            mSnapshots.remove(0);
        }
        mSnapshots.add(new Snapshot(snapshot, timeStamp));
    }

    public void didReceiveQuery() {
        if (mSnapshots.size() > 0) {
            ((Snapshot) mSnapshots.get(mSnapshots.size() - 1)).getSnapshot().header.feedback = 1;
        }
    }

    public List<Snapshot> getSnapshots() {
        return mSnapshots;
    }
}
