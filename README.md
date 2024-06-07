# Android_PhotosApp

Port the JavaFX Photos project to Android (Photos-JavaFx-UIs)

## Photos Android App
Photos is an Android application developed by Christine Yue for Software Methodology. This app allows users to manage photos in albums, add tags to photos, search for photos by tag-value pairs, and perform various other actions.

### Important Points

Developed using Android Studio with Java.

Must run correctly on the Nexus 4 (4.7 inch, 768x1280, xhdpi) device emulator.

Set compileSdk, minSdk, and targetSdk to API 34 in build.gradle.kts.

Implemented using only Java, not Kotlin.

Use GitHub to manage the code repository.

### Features

Home Screen: Upon launching the app, it loads album and photo data from the previous session (if any) and displays all albums with their names in plain text. Users can navigate to various functionalities from the home screen.

Album Management: Open, create, delete, and rename albums as listed in the JavaFX project description.
When opening an album, display all its photos with their thumbnail images.

Photo Management: Add, remove, or display a photo within an open album.
Photo display screen includes an option for a slideshow, allowing users to navigate backward or forward in the album one photo at a time with manual controls.

Tag Management: Add a tag to a photo. Only person and location are valid tag types. Delete a tag from a photo.Tags (if any) are visible when displaying a photo.

Move Photo: Move a photo from one album to another.

Search Photos: Search for photos by tag-value pairs (person and location). Implement conjunction and disjunction for tag-based searches. Matches allow auto-completion, making it easier for users to find photos by typing a starting substring.

### Implementation

Ported the FX-based photos project to Android, reusing relevant code.

Utilized Android Studio for development and GitHub for collaborative code management.

Implemented various functionalities using Java and Android SDK.

### Development Environment

Developed using Android Studio.

Project structure follows standard Android application guidelines.
