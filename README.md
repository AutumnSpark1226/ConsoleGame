[![Java test](https://github.com/AutumnSpark1226/ConsoleGame/actions/workflows/java.yml/badge.svg)](https://github.com/AutumnSpark1226/ConsoleGame/actions/workflows/java.yml)
[![CodeFactor](https://www.codefactor.io/repository/github/autumnspark1226/consolegame/badge)](https://www.codefactor.io/repository/github/autumnspark1226/consolegame)
# ConsoleGame
ConsoleGame is a dungeon game that can be played in a console.
# Game

0  = player  
8  = enemy  
_  = floor  
\[] = exit  

inventory:  
HPO  = health potion  
HD   = health double  
ADP  = attack damage plus  
CHP  = critical hit chance plus
# Compile  
1. Download the source code  
   - gh repo clone AutumnSpark1226/ConsoleGame  
   - git clone https://github.com/AutumnSpark1226/ConsoleGame.git   
2. Go to the folder
   - Windows: cd {downloaded folder}\\src  
   - Linux/Mac OS/etc: cd {downloaded folder}/src  
3. Compile the file
   - javac ConsoleGame.java  
4. Execute the file  
   - java ConsoleGame

# How to play
Enter the command and press enter  
Commands are:  
- d or right (go right)
- a or left (go left)
- w or weapon (use your weapon and attack the enemy)
- s or heal (use a health potion to heal your injuries)
- i or inventory (look at your inventory)
- t or stats (look at your stats)
- u or use <item name or short form> (use this item)
- show (print the game)
- exit (close the game)
- getsavestate (print a string that contains the current game)
- loadsavestate <savestate> (load a Savestate)
