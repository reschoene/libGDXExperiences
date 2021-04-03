#!/bin/bash

cd html/build/dist

echo ======== DEPLOY mario : INÍCIO ========

echo compactando arquivos de recursos html, css, js...
echo 
rm ./mario.tar.bz2
tar -cvjf mario.tar.bz2 ./*
echo 
echo fim da compactacao dos arquivos de recursos

echo Subindo mario.tar.bz2...
echo 
sshpass -vp $LOCAWEB_PASSWORD scp ./mario.tar.bz2 root@vps6174.publiccloud.com.br:/home/renato/mario.tar.bz2
echo 
echo Fim upload mario.tar.bz2

echo Conectando ao servidor da Locaweb...
sshpass -p $LOCAWEB_PASSWORD ssh -o StrictHostKeyChecking=no root@vps6174.publiccloud.com.br << EOF_OF_COMMAND
  echo Parando serviço nginx
  service nginx stop

  echo excluindo versao antiga
  cd /usr/share/nginx/html
  rm -R mario

  echo preparando arquivo para descompactacao
  cd  /home/renato
  rm -R mario
  mkdir mario
  mv mario.tar.bz2 ./mario
  cd mario

  echo descompactando arquivo mario.tar.bz2
  tar -xvjf mario.tar.bz2

  echo excluindo arquivo compactado
  rm ./mario.tar.bz2

  echo movendo arquivo descompactado para o diretorio do servidor nginx
  mv /home/renato/mario /usr/share/nginx/html/mario

  echo Iniciar serviço nginx
  service nginx start
EOF_OF_COMMAND

echo ======== DEPLOY mario : FIM ========
