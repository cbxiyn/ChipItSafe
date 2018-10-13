package com.reply.hackaton.biothech.chipitsafe.tools;

public class ApplicationState {

    public static String NOTIFICATION_CHANNEL_ID = "my_channel_01";
    public static String NOTIFICATION_CHANNEL_NAME = "dangerToDoctor";

    public enum UserState {
        inDanger,
        rescuer,
        doctor
    }

    public static UserState state = UserState.inDanger;
}
