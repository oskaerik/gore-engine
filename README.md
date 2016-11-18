# GORE Engine

## What is GORE Engine?
GORE Engine is an easy to use 2D game engine written in Java using Slick2D.

## How to use
Below are instructions on how to use the game engine.

### General
#### Settings
In the settings.txt-file, specify the width and height of the game window, the max FPS allowed and if debug mode should be enabled etc. The names of the fields are pretty self explanatory.

### Characters
#### Animations
In the `res/characters/` folder, create a folder for every character wanted in the game. The character `player` is obligatory.

Save the movement animations as .png-files and put them in the folder for the corresponding player. The movement animations should be named like:
* `down-1.png`
* `down-2.png`
* `up-1.png`

The needed directions for every character is `up`, `down`, `left` and `right`.

#### Dialogue
Add a file name `dialogue.txt` to every character and write what you want them to say.

The file should be formatted like this:
`
null,Item1,Item2,Item3
null:
What you want them to say when you haven't given them any items.
You can keep typing on multiple lines to have more dialogue.
Item1:
What the character should say when you give the character Item1.
`

#### Movement
Place a file in the characters directory names `movement.txt` and format it as following:

For a character that should only stay still, you can have a file that looks like this:
```
1 S
```
This means the character will stay still for 1 frame and then it will restart.

For a character that should move, you could have a file that looks something like this:
```
100 D
100 L
100 R
100 U
```
This character will walk downwards for 100 frames, then left, then right, then up, then repeat.

#### Placing characters in the world
Using Tiled, place a transparent tile on layer 3 and have the fields:
* `CharacterType` - This is either `friend` or `enemy`
* `CharacterName` - Name of the character, must match the name of the character's folder
If you want more than one of the same character in the room, add a number to the end of the character's name. You could have characters like this:
```
CharacterName=enemy1
CharacterName=enemy2
CharacterName=enemy3
```

### Items
#### Tiled
Items are placed using Tiled, place the items as transparent tiles on layer 2. The tile should have the field `ItemName` where the name of the item could be something line `Sword` or `Chest`.

#### Animation
Place the pictures for the animations that you want the item to have in the folder `res/items/` like this:
`ItemName-1.png`
`ItemName-2.png`
`ItemName-3.png`
Where ItemName is the actual name of the item. If you don't want the item to have a moving animation, just place the first file in the folder. Be sure to name it `ItemName-1.png` though.

### Exits
#### Tiled
Exits are placed on the layer 1 and have the fields:
* `Exit` String which should contain the name of the room the exit should lead to
* `SpawnX` and `SpawnY` String which should contain the spawn location you end up on

### Blocked tiles
#### Tiled
Blocked tiles are placed on layer 1 in tiled and should have a boolean field called "Blocked" which should be set to true (should be ticked in Tiled).

It is important that the top left tile of every map is a blocked tiled, since the rendering of the map itself begins on that tile.

## Credits
* Slick2D: http://slick.ninjacave.com/
* OpenGameArt: http://opengameart.org/
* LWJGL: https://www.lwjgl.org/