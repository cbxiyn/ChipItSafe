package com.reply.hackaton.biotech.chipitsafe.Firebase;

public class FirstAidRequest {
    FirebaseDatabaseHelper firebaseDatabaseHelper =  new FirebaseDatabaseHelper();
    /**
     * This method calls the createFirstAidDocument method in the Firebase Database class
     * it checks if the UID is null before calling the method.
     * @param uid   The user's Firebase user ID
     * @param bps The users current Beats per second (BPS) reading from the ecg monitor
     */
    public void updateBPS(String uid, int bps)
    {
        if(uid != null) {

            firebaseDatabaseHelper.createFirstAidDocument(uid, bps);
        }
    }
    /**
     * This method calls the deleteFirstAidDocument method in the Firebase database class
     * it checks if the UID is null before calling the method.
     * @param uid   The user's Firebase user ID
     */
    public void deleteFirstAidDocument(String uid)
    {
        if(uid != null)
        {
            firebaseDatabaseHelper.deleteFirstAidDocument(uid);
        }

    }
    public void sendNotificationToRescuers(String uid){

    }
}
