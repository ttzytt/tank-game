package networkings.msgs;

import java.io.Serializable;

abstract public class EvtMsg implements Serializable, Comparable{
    long evtTm = 0; // time when the event happend

    EvtMsg () {
        evtTm = System.currentTimeMillis();
    }

    public long getEvtTm() {
        return evtTm;
    }

    public void setEvtTm(long evtTm) {
        this.evtTm = evtTm;
    }

    @Override
    // compare by event time
    public int compareTo(Object o) {
        if (o instanceof EvtMsg) {
            EvtMsg other = (EvtMsg) o;
            if (this.evtTm > other.evtTm) {
                return 1;
            } else if (this.evtTm < other.evtTm) {
                return -1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }
}
