package com.reply.hackaton.biothech.chipitsafe.tools;

public class ApplicationState {

    public static String NOTIFICATION_CHANNEL_ID = "my_channel_01";
    public static String NOTIFICATION_CHANNEL_NAME = "dangerToDoctor";

    public enum HealthState {
        good,
        inDanger
    }

    public enum UserState {
        rescuer,
        doctor
    }

    public static UserState state = UserState.rescuer;
    public static HealthState healthState = HealthState.good;
}
