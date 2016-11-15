# GORE Engine

## What is GORE Engine?
GORE Engine is an easy to use 2D game engine written in Java using Slick2D.

## How to use
Below are instructions on how to use the game engine.

### General
#### Settings
In the settings.txt-file, specify the width and height of the game window, the max FPS allowed and if debug mode should be enabled.

### Resources
Resources are placed in the ```res/``` folder
#### Characters
In the ```res/characters/``` folder, create a folder for every character wanted in the game. The character ```player``` is obligatory.

Save the movement animations as .png-files and put them in the folder for the corresponding player. The movement animations should be named like:
* ```down-1.png```
* ```down-2.png```
* ```up-1.png```

The needed directions for every character is ```up```, ```down```, ```left``` and ```right```.

## Credits
* Slick2D: http://slick.ninjacave.com/
* OpenGameArt: http://opengameart.org/
* LWJGL: https://www.lwjgl.org/