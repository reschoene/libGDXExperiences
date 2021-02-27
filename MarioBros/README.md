#Geração executário desktop
Entrar no diretório MarioBros e rodar o comando para gerar o JAR release:
`./gradlew desktop:dist`

#Geração APK Android

Entrar no diretório MarioBros e rodar o comando para gerar a APK release:
`./gradlew android:assembleRelease`

Será gerado no diretório abaixo um APK não assinado
`MarioBros/android/build/outputs/apk/release`

Caso ainda não tenha criado uma keystore, crie uma com o comando abaixo:

`keytool -genkey -v -keystore "/mnt/dados/Desenv/libGDXExperiences/MarioBros/android/build/outputs/apk/release/mario-release-key.jks" -keyalg RSA -keysize 2048 -validity 10000 -alias Mario`

`jarsigner -verbose -keystore "/mnt/dados/Desenv/libGDXExperiences/MarioBros/android/build/outputs/apk/release/mario-release-key.jks" -storepass yourpass -keypass yourpass "/mnt/dados/Desenv/libGDXExperiences/MarioBros/android/build/outputs/apk/release/android-release-unsigned.apk" Mario`
