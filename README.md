# LibGDX experiences

### Build desktop executable:
Type `./gradlew desktop:dist` to 
generate a fat jar file in the directory `desktop/build/libs` 
that you can run with `java -jar desktop.jar`

### Build android APK file:
Type `./gradlew android:assembleDebug` to generate a debug apk file in the 
directory `android/build/outputs/apk`
that you can send to users manually if they have disabled APK source checking.
