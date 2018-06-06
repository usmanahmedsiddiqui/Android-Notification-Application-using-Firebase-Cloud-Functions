'use-strict'

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);
exports.sendNotifications = functions.database.ref('/Users/{user_id}/Notifications/{notification_id}')
.onWrite((change,context)=>{
    const notification_id = context.params.notification_id;
     const user_id = context.params.user_id;
        
    
//        console.log("UserID: " + user_id +" |NotificationID: "+ notification_id);
  return admin.database().ref("/Users").child(user_id).child("Notifications").child(notification_id).once('value').then(queryResult=>{
      

    const from_user_id = queryResult.val().from;
    const from_message = queryResult.val().message;
    const from_data = admin.database().ref("/Users").child(from_user_id).once('value');
    const to_data = admin.database().ref("/Users").child(user_id).once('value');
    
   

    return Promise.all([from_data,to_data]).then(result=>{

    const from_name  = result[0].val().name;
    const to_name = result[1].val().name;
    const token_id = result[1].val().token_id;
    
    const payload ={
        notification :{
            title : "Notification From : "+from_name,
            body : from_message,
            icon: "default",
            click_action : "com.google.firebase.NotificationTarget"
        },
        data: {
            message : from_message,
            from_user_id : from_user_id
        }
    };
    return admin.messaging().sendToDevice(token_id,payload).then(result=>{
        return console.log("Notification sent");
    });
    

   
    
    });



  
});


});


