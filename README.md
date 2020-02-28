Quick and easy CRUD spring boot application to track your finances and set goals for each category. The application will send out an email each month with the expense statistics.

First Iteration of this application is just to see if I would use it in my daily life.
If so, I will rewrite it to include register/login and Reactive Streams.

The "client" is right now only commandline based. Will eventually be refactored in the future.
Command prompt is a copy of: https://codepen.io/AndrewBarfield/pen/qEqWMq

To run the application, update the application.properties and set the smtp client to send the email.
Then run docker-compose and start the spring boot application.
