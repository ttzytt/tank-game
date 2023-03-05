package networkings.msgs;

import java.io.Serializable;

abstract public class EvtMsg implements Serializable, Comparable{
    long prio = 0; // usually time, smaller -> more important

    EvtMsg () {
        prio = System.currentTimeMillis();
    }

    EvtMsg (long prio) {
        this.prio = prio;
    }

    public long getPrio() {
        return prio;
    }

    public void setPrio(long evtTm) {
        this.prio = evtTm;
    }

    @Override
    // compare by event time
    public int compareTo(Object o) {
        if (o instanceof EvtMsg) {
            EvtMsg other = (EvtMsg) o;
            if (this.prio > other.prio) {
                return 1;
            } else if (this.prio < other.prio) {
                return -1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }
}
