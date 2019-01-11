package com.redhat.cajun.navy.responder;

import java.util.Objects;

public class IncidentStats {

    private long cancelled;
    private long claimed;
    private long pickedUp;
    private long rescued;
    private long requested;

    public long getCancelled() {
        return cancelled;
    }

    public void setCancelled(long cancelled) {
        this.cancelled = cancelled;
    }

    public long getClaimed() {
        return claimed;
    }

    public void setClaimed(long claimed) {
        this.claimed = claimed;
    }

    public long getPickedUp() {
        return pickedUp;
    }

    public void setPickedUp(long pickedUp) {
        this.pickedUp = pickedUp;
    }

    public long getRescued() {
        return rescued;
    }

    public void setRescued(long rescued) {
        this.rescued = rescued;
    }

    public long getRequested() {
        return requested;
    }

    public void setRequested(long requested) {
        this.requested = requested;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IncidentStats that = (IncidentStats) o;
        return cancelled == that.cancelled &&
                claimed == that.claimed &&
                pickedUp == that.pickedUp &&
                rescued == that.rescued &&
                requested == that.requested;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cancelled, claimed, pickedUp, rescued, requested);
    }

    @Override
    public String toString() {
        return "IncidentStats{" +
                "cancelled=" + cancelled +
                ", claimed=" + claimed +
                ", pickedUp=" + pickedUp +
                ", rescued=" + rescued +
                ", requested=" + requested +
                '}';
    }
}
