# MGKCT-Timetable


### In order to build a project, you need to get and create a key for the API

> To get api_key you could write `/api` to telegram bot [@mgke_slave_bot](https://t.me/mgke_slave_bot)

1. Create a folder named assets in the YOUR_PROJECT\app\src\main\assets directory of your project. If the assets folder already exists, skip this step.
2. Inside the assets folder, create a file named config.properties.
3. Open the config.properties file and add your keys and configuration parameters:

```properties
api_key=your_key
```

### Also this project uses Google Analytics, so you have to set up new firebase project

#### 1. Create a Firebase Project

- Go to the [Firebase Website](https://firebase.google.com/).
- Follow the prompts to create a new Firebase project.

#### 2. Register the App with Firebase

- In the Firebase Console, select your project.
- Click on "Add app" and choose the appropriate platform (Android in this case).
- Follow the setup instructions, including adding the package name of your Android app.

### 3. Download the Config File

- Once completed, download the `google-services.json` config file.
- Place the downloaded `google-services.json` file into the specified directory of your Android project.

## **Important:** Make sure that the `config.properties` and `google-services.json` files are not added to version control (e.g., Git) or otherwise publicly accessible to avoid leaking confidential data.
