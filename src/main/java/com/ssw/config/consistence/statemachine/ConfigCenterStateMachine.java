package com.ssw.config.consistence.statemachine;

import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Iterator;
import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.core.StateMachineAdapter;
import com.alipay.sofa.jraft.error.RaftException;
import com.alipay.sofa.jraft.storage.snapshot.SnapshotReader;
import com.alipay.sofa.jraft.storage.snapshot.SnapshotWriter;

/**
 * @ClassName ConfigCenterStateMachine
 * @Description
 * @Author sun
 * @Date 2021/12/10 10:28
 **/
public class ConfigCenterStateMachine extends StateMachineAdapter {


    @Override
    public void onApply(Iterator iterator) {

    }


    @Override
    public void onSnapshotSave(SnapshotWriter writer, Closure done) {
        super.onSnapshotSave(writer, done);
    }

    @Override
    public boolean onSnapshotLoad(SnapshotReader reader) {
        return super.onSnapshotLoad(reader);
    }

    @Override
    public void onLeaderStart(long term) {
        super.onLeaderStart(term);
    }

    @Override
    public void onLeaderStop(Status status) {
        super.onLeaderStop(status);
    }

    @Override
    public void onError(RaftException e) {
        super.onError(e);
    }


}
