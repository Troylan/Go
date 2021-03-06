Go Offline Documentation
===

<dl>
<dt>Created by:</dt>
<dd>Troylan Tempra Jr. </dd>
<dd>Charles Reyes</dd>
<dt>Program:</dt> <dd>Go</dd>
<dt>Created:</dt> <dd>December 2,2011</dd>
<dt>Mod:</dt> <dd>December 16, 2011</dd>
<dt>Description:</dt> <dd>A programmed version of the GO board game created in Java</dd>
</dl>

Go [here](http://en.wikipedia.org/wiki/Go_(game)) for the wikipedia page. 

Instructions
---

Run the program (GoAdv_run)

You will be immediately directed to the main menu. It should display the 
available commands.

From the main menu you may enter the following commands:

		- Enter: play
		- q: quit
		- h: help
		- c: change board size

Board size can be changed with a max of 19. The default is also 19.
19 is the traditional playing size.
		
Summary of rules
---

1. The game is played on a 19 x 19 board. This can be resized, 13 x 13 or 
9 x 9 being traditional alternate sizes. The board makes use of intersections,
not spaces.

2. The players, black and white, take turns putting "stones" of their
respective colour. Black always moves first.

3. Stones can remain on the board only as long as it has a liberty. A liberty
is an empty space adjacent to a stone. Stones are removed from the board when
they have no liberties, but stones cannot be removed or moved otherwise.

4. Stones can be linked together horizontally or vertically beside each other
in structures called "chains." All stones in the same chain share each others
liberties.

5. The player with the largest amount of territory wins. A territory is a space
on the board occuppied by the players colour or surrounded by only a players
colour. A territory between both black and white stones is mutual and holds no
scoring value.

6. Players may pass. When both players pass consecutively, the game ends.

7. The board cannot look exactly the same twice.
		

Notes
---

December 16, 2011

	Added variable board size (Max 19)
	Known Bugs: 
	 1. When a stone placement causes groups of both teams to be 
	 considered surrounded, both groups will be eliminated from the board.
	 In original Go, the aggressing group remains and the attacked group 
	 is removed. 
--------------------------------------------------------------------------------
December 14, 2011

	Program is fully operational
	Main Menu Commands:
		Enter: play
		q: quit
		h: help
--------------------------------------------------------------------------------
