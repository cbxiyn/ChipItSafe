const functions = require('firebase-functions');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();


//https://firebase.google.com/docs/reference/functions/functions.auth.UserRecord

// Take the text parameter passed to this HTTP endpoint and insert it into the
// Realtime Database under the path /messages/:pushId/original
exports.createUserConfig = functions.auth.user().onCreate((user) => {
    // [END onCreateTrigger]
      // [START eventAttributes]
      const uid = user.uid;
      const email = user.email; // The email of the user.
      const displayName = user.displayName; // The display name of the user.
      // [END eventAttributes]
    
      return sendWelcomeEmail(email, displayName);
    });
    function createUserConfig()
{}
    async function sendWelcomeEmail(email, displayName) {
        const mailOptions = {
          from: `${APP_NAME} <noreply@firebase.com>`,
          to: email,
        };
      
        // The user subscribed to the newsletter.
        mailOptions.subject = `Welcome to ${APP_NAME}!`;
        mailOptions.text = `Hey ${displayName || ''}! Welcome to ${APP_NAME}. I hope you will enjoy our service.`;
        await mailTransport.sendMail(mailOptions);
        return console.log('New welcome email sent to:', email);
      }