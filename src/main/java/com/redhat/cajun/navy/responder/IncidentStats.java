package com.redhat.cajun.navy.responder;

import java.util.Objects;

public class IncidentStats {

    private int cancelled;
    private int claimed;
    private int pickedUp;
    private int rescued;
    private int requested;

    public int getCancelled() {
        return cancelled;
    }

    public void setCancelled(int cancelled) {
        this.cancelled = cancelled;
    }

    public int getClaimed() {
        return claimed;
    }

    public void setClaimed(int claimed) {
        this.claimed = claimed;
    }

    public int getPickedUp() {
        return pickedUp;
    }

    public void setPickedUp(int pickedUp) {
        this.pickedUp = pickedUp;
    }

    public int getRescued() {
        return rescued;
    }

    public void setRescued(int rescued) {
        this.rescued = rescued;
    }

    public int getRequested() {
        return requested;
    }

    public void setRequested(int requested) {
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
