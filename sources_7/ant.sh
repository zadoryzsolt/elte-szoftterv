#!/bin/bash

# Forditas Java 7 alatt.
# Toltsd le az Oracle website-jarol a 1.7.0_71 -es verzioszamu
# JDK -t a .tar.gz kiterjesztesu disztrofuggetlen csomagban.
# Csomagold ki egy tetszoleges mappaba a letoltott JDK -t, es a lenti
# export parancsba, a JAVA_HOME -nak add meg a JDK gyokerkonyvtaranak 
# az elereset (a zarojelek fontosak).
# Az ANT program  bin/ant  scriptfile -ja lehet hogy nem futtahato, ekkor
#   chmod -c 777  ./lib/apache-ant-1.9.4/bin/ant  
# Ezutan futtasd ezt a shellscriptet:  
#   ./ant.sh test
# Ezzel el is indul a program.
# Ha csak le akarod forditani, akkor
#   ./ant.sh all
# Elvileg igy mennie kell.

export JAVA_HOME="../../jdk1.7.0_71"

./lib/apache-ant-1.9.4/bin/ant $1 $2 $3 $4