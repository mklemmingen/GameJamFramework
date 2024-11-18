# FrameworkGameJam

Use at max Java 18, because of the gradle version. Runs fine on Corretto 18

Run project via Gradle symbol (elephant) -> Tasks -> Other -> Run

### structural explanation 

GameJam is the Main Class of the Project that links to the engine adapters. 

In the frontend folder are all classes used for the frontend. 

In the backend folder is a class called BackMan. It should use the link-directories interface and classes. 

The link folder is the knowledge that is shared between frontend and backend. 
If one was to build a new frontend or backend, they would only need to use the preexisting information from
the link folder to build a fitting code.

### Tasks

Build your backend around BackMan and the Link.
You are free to edit any code inside your project, including the frontend if need be.
Make sure that it is still runnable via gradle. 
Your end result should be executable and playable.

Implement all pieces, all methods of the main linking interface and the rules 
as well as the regulations that come with it. 

Make sure by starting the game, the Board gets renewed. 
Make sure you update the Board if it should be updated. 
Make sure it gets updated correctly depending on the pieces.
Make sure you calculate the possible move coordinates correctly.
Make sure you return the correct boolean values, and to only use information as is presented by the link.
-> If you choose to change the datatypes, make sure you change/add through data in the link folder.

Make sure you follow the guidelines these guidelines to the rules of your game! :

Tip: Discuss and Plan within your team, then select groups with a task, work on the task, meet up, merge, test, and repeat. 
If you do not plan and share responsibilities, you will not be finished in time.

### Change the Game! -------------------------------------------------------------------

In order to change the game, simply change the link/ object enumerations, the gamestage asset loading and gamestage texture display

### Default implementation for: Chess ---------------------------------------------------

#### Pieces, Move- and Hit-Logic
- A Rook -> moves: Horizontally or vertically any number of squares. -> hits: Same as moves.
- A Pawn -> moves: Forward one square. On its first move, it can move forward two squares. -> hit: Pawns capture diagonally.
- A King -> One square in any direction. -> hit: Same as moves.
- A Queen -> Any number of squares in any direction (horizontally, vertically, or diagonally). -> hit: same as moves
- A Knight -> moves: In an “L” shape: two squares in one direction and then one square perpendicular, or one square in one direction and then two squares perpendicular. -> hit: same as moves.
- A Programmer -> One square in any direction, similar to the King. -> hit: same as moves | special -> it can change places with friendly pieces, exchanging their place.
- A Chancellor -> combines the moves of a rook and a knight -> hit: same as moves. | Special: Nothing except how it moves and hits.

#### special tiles

Barrier Pieces

Rule: Place immovable barrier pieces on the board that block movement and captures.
Implementation: Add barrier pieces to the board setup and modify movement and capture logic to account for these barriers.

#### Special Board setup

Programmers: Place two programmers somewhere in the initial lines of pawns. 
Chancellor: Place one chancellor somewhere inside the line of pawns.

#### Game End

a game ends, if a players king has been removed, if a player cannot move any pieces (deadlock) or if the last three moves of a player were the same. 

#### Game End

A game ends if a player cannot move any pieces (deadlock), if a player has no pieces left, or if a player captures all of the opponent’s pieces.
