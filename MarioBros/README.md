#Mario - LibGDX!

#Geração executário desktop
Entrar no diretório MarioBros e rodar o comando para gerar o JAR release:
`./gradlew desktop:dist`

#Geração WEB
Entrar no diretório MarioBros e rodar o comando para gerar a versão WEB
`./gradlew html:dist`

Em seguida rodar o comando abaixo para subir a versão web gerada para nuvem:
`./deployHTML.sh`

###dica: do index.html gerado, remova a linha que inicia com <a class="superdev" (refresh button)

quando terminar a execução, o jogo ficará disponível em:
`http://vps6174.publiccloud.com.br/mario`

#Geração APK Android

Entrar no diretório MarioBros e rodar o comando para gerar a APK release:
`./gradlew android:assembleRelease`

Será gerado no diretório abaixo um APK não assinado
`MarioBros/android/build/outputs/apk/release`

entre neste diretório
`cd android/build/outputs/apk/release`

Caso ainda não tenha criado uma keystore, crie uma com o comando abaixo:

`keytool -genkeypair -v -keystore mario.keystore -alias publishingdoc -keyalg RSA -keysize 2048 -validity 10000`


Depois rode o zipalign para otimizar o apk gerado
`/mnt/dados/Desenv/Android/Sdk/build-tools/30.0.2/zipalign -f -v 4 android-release-unsigned.apk mario-release-signed.apk`

Depois rode o apksigner para gerar o apk assinado
`/mnt/dados/Desenv/Android/Sdk/build-tools/30.0.2/apksigner sign --ks mario.keystore --ks-key-alias publishingdoc mario-release-signed.apk`
