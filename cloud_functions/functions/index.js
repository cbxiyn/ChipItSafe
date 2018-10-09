// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require("firebase-functions");
const admin = require("firebase-admin");

//Application Name for email
const APP_NAME = "Chip-It-Safe: ECG First Aid";

// The Firebase Admin SDK to access the Firebase Realtime Database.
admin.initializeApp();

var db = admin.firestore();

/**
 * Trigger declarations
 */

exports.createUserConfig = functions.auth.user().onCreate(user => {
  // [END onCreateTrigger]
  // [START eventAttributes]
  var data = {
    uid: user.uid,
    email: user.email,
    displayName: user.displayName,
    phoneNumber: user.phoneNumber,
    emailVerified: user.emailVerified,
    //TODO: Implement the datatype below
    applicationType: "Wearer"
  };
  // [END eventAttributes]
  return createUserConfig(data);
});
exports.deleteUserConfig = functions.auth.user().onDelete(user => {
  // [END onCreateTrigger]
  // [START eventAttributes]
  // [END eventAttributes]
  return deleteUserConfig(user.uid);
});

/**
 * Cloud Functions implementations
 */

//Create user configuration for user in user config DB when user is created
function createUserConfig(data) {
   db.collection("users").doc(data.uid).set(data);
   return console.log("User config created");
}
//Delete user configuration for user in user config DB when user is deleted
function deleteUserConfig(uid) {
  db.collection("users").doc(uid).delete();
  return console.log("User config deleted");
}
