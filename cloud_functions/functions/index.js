// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require("firebase-functions");

//Application Name for email
const APP_NAME = 'Chip-It-Safe: ECG First Aid';
// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require("firebase-admin");
admin.initializeApp();

//https://firebase.google.com/docs/reference/functions/functions.auth.UserRecord


/**
 * Trigger definitions
 */
/*exports.sendWelcomeEmail = functions.auth.user().onCreate((user) => {
  // [END onCreateTrigger]
    // [START eventAttributes]
    const email = user.email; // The email of the user.
    const displayName = user.displayName; // The display name of the user.
    // [END eventAttributes]
  
    return sendWelcomeEmail(email, displayName);
  });*/

exports.createUserConfig = functions.auth.user().onCreate(user => {
  // [END onCreateTrigger]
  // [START eventAttributes]
  var data = {
    uid: user.uid,
    email: user.email,
    displayName: user.displayName,
    phoneNumber: user.phoneNumber,
    emailVerified: user.emailVerified
  }
  // [END eventAttributes]

  createUserConfig(data);
});

/**
 * Create user configuration for user in user config DB
 */
function createUserConfig(data) {
    var setDoc =  db.collection('users').doc(data.uid).set(data);
}
/**
 * Sends a welcome email to new user.
 
async function sendWelcomeEmail(email, displayName) {
  const mailOptions = {
    from: `${APP_NAME} <noreply@firebase.com>`,
    to: email
  };

  // The user subscribed to the newsletter.
  mailOptions.subject = `Welcome to ${APP_NAME}!`;
  mailOptions.text = `Hey ${displayName ||
    ""}! Welcome to ${APP_NAME}. I hope you will enjoy our service.`;
  await mailTransport.sendMail(mailOptions);
  return console.log("New welcome email sent to:", email);
}*/
