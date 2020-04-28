# Mega Man
A simple and fun retro-style multiplayer 2D shooting game made entirely from scratch with Java-FX and mega man sprites

![Game Start](Images/begin.png)


**Working code for the entire project is in [src/sample directory](src/sample/).**

* **Code for Client(Game) is in [src/sample/Game.java](src/sample/Game.java)**
* **Code for Server is in [src/sample/Server.java](src/sample/Server.java)**


**Other folders in /src directory contain .png sprites and a single folder contains game sounds.** 

*Game sounds are deactivated in the latest release*

## Running the Game on your machine
The Working JAR executable files are found in [JAR_Executables Folder](JAR_Executables/)

### Instructions for running the game
Since this is a multiplayer game, Exactly two clients must be connected to server in order to begin playing
The three files (One Server JAR file and 2 Client JAR files) can be run in three seperate machines as long as the machines are in the same LAN. 

**Java Runtime Environment needs to be installed on the machine(s) running the JAR files**

Download the Runtime from [java's website](https://www.java.com/en/download/)

There are three files in the JAR_Executables Folder](JAR_Executables/). 

1. [The mega-man-server file](JAR_Executables/mega-man-server.jar) is the server file that needs to be run first. There's no Graphical Interface on the server file, so it runs silently upon clicking it. This is intended behaviour.
2. [The mega-man-client-1 file](JAR_Executables/mega-man-client-1.jar) and [The mega-man-client-2 file](JAR_Executables/mega-man-client-2.jar) are identical. The files are seperate to make running the same client code twice easier in some machines. 
Run both the files in turn.
3.The client game files when run will ask for Server IP address (Defaults to 127.0.0.1) Use the default IP address if the server is running in the same machine as the client game file. If not, enter the machine's IP address where the server is running here. Then click ready.
![Enter Server's IP address here](Images/ip_address.png)
4. Do the same for the other client game file. 
5. Intro plays and the game starts. Enjoy!

### Controls
* Press the arrow keys to move around ("Up" Arrow key to jump)
* Press the "x" key to shoot fire bullets
* Press the "c" key to attack with your glorious sword

### Gameplay
![Shooting](Images/red_shoot.png)
![Sword Strike](Images/blue_slash.png)
![Jumping](Images/red_jump_slash.png)

* Going to the bubble on the top left of the screen gives the player a shield, rendering them immune to attacks for a limited time
* Going to the plus sign on the top right gives the player extra health
* Health of each player regenerates slowly with time
* Bullets run out and regenerate slowly over time


### Post-Game
![Dead](Images/dead.png)
* Once someone is dead, Clicking on the Restart button on both clients restarts the game!



