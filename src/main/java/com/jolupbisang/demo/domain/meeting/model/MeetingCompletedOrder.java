package com.jolupbisang.demo.domain.meeting.model;

public final class MeetingCompletedOrder {

    private MeetingCompletedOrder() {
    }

    public static final int COMPLETED_EVENT_SENDING = 1;
    public static final int CONTEXT_SCHEDULING_TERMINATION = 2;
    public static final int LIVE_PARTICIPATION_SCHEDULING_TERMINATION = 3;
    public static final int LIVE_PARTICIPATION_SAVING = 4;
    public static final int RECAP_SUMMARY_CREATION = 5;
    public static final int AUDIO_MERGING = 6;
    public static final int CLEAN_UP_SESSION = 7;
}
