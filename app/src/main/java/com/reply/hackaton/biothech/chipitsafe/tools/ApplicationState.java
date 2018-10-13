package com.reply.hackaton.biothech.chipitsafe.tools;

public class ApplicationState {

    public static int NOTIFICATION_CHANNEL_ID = 1;
    public static String NOTIFICATION_CHANNEL_NAME = "dangerToDoctor";

    public enum UserState {
        inDanger,
        rescuer,
        doctor
    }

    public static UserState state = UserState.inDanger;
}
