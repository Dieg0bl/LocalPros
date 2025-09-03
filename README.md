# LocalPros - Version 0.6

LocalPros is an Android application built with Kotlin and Jetpack Compose that connects local service providers with customers.

## Features

- **User Authentication**: Firebase-based authentication system
- **Location Services**: Google Maps integration for service provider discovery
- **Service Management**: Platform for managing local professional services
- **Modern UI**: Built with Jetpack Compose for a modern Android experience

## Technical Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with Hilt for dependency injection
- **Database**: Firebase Firestore
- **Authentication**: Firebase Auth
- **Maps**: Google Maps API
- **Build Tool**: Gradle with Kotlin DSL

## Setup

### Prerequisites

- Android Studio Arctic Fox or later
- Android SDK API Level 23 or higher
- Google Maps API Key

### Configuration

1. Clone the repository
2. Open the project in Android Studio
3. **Configure Google Maps API Key**: Replace `YOUR_GOOGLE_MAPS_API_KEY_HERE` in both:
   - `app/build.gradle.kts`
   - `app/src/main/AndroidManifest.xml`
4. **Configure Firebase**: Update `app/google-services.json` with your Firebase project configuration:
   - Replace `YOUR_PROJECT_NUMBER` with your Firebase project number
   - Replace `your-project-id` with your Firebase project ID
   - Replace `YOUR_APP_ID` with your Android app ID
   - Replace `YOUR_FIREBASE_API_KEY` with your Firebase API key
   - Update the Firebase URL and storage bucket accordingly
5. Build and run the project

## Version Information

- **Version**: 0.6
- **Version Code**: 6
- **Target SDK**: API Level 34
- **Minimum SDK**: API Level 23

## Architecture

The app follows modern Android development practices:

- **Hilt**: For dependency injection
- **Navigation Compose**: For app navigation
- **Firebase**: For backend services
- **Coroutines**: For asynchronous operations
- **Material Design 3**: For UI components

## Repository Status

This repository is prepared for public archival as of version 0.6. This represents a stable delivery version of the LocalPros application.

**Note**: All API keys and sensitive configuration values have been replaced with placeholders for security. You will need to configure your own API keys and Firebase project settings to run the application.

---

*For questions or issues, please refer to the commit history or contact the development team.*