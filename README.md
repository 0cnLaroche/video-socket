# Video Streamer

## Server

## Streamer

Librairies utilisées:

 - [OpenCV](https://opencv.org/) (Open Source Computer Vision);

#### Protocol

 1. **client** crée connection avec serveur (3 way handshake)
 1. **client** dit: "session:" *nom de la session*
 1. **serveur** dit: *nom de la session*":OK"
 1. **client** envois données images
 1. Quand le server ou le client dit "Bye.", la connection est fermé

## Viewer

## Références

http://rapidprogrammer.com/how-to-access-camera-with-opencv-and-java
