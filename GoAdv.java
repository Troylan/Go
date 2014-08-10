/*
 * Program: Human vs Human GO
 * Author: Charles Reyes and Troylan Tempra
 * Date: December 14,2011
 * Description: The GO game methods/variables
 */

import java.io.*;

public class GoAdv
{
  String s = "";//String variable
  InputStreamReader isr = new InputStreamReader(System.in);//Input stream reader variable
  BufferedReader br = new BufferedReader(isr);//Buffered reader variable
  boolean foo = true;//boolean variable
  boolean firstpass = false;//boolean variable
  boolean secpass = false;//boolean variable
  int xax = 0;//int variable for the x axis, starting at 0
  int yax = 0;//int variable for the y axis, starting at 0
  int[][] board = new int [21][21];//the board. holds the position of every stone in every coordinate.
  //the board adopts the 19 by 19 traditional size.
  //the array is 21 by 21 to allow for corners. This also fixes the out of bounds error that arises for the capture algorithms.
  int[][] scoringboard = new int [21][21];//the scoring board. This allows methods to access a copy of the board without affecting it maliciously.
  //The data in the scoring board is more diverse than that on the board. 
  //the scoring board data is used to read the score of each player and is used for the methods that calculate scoring.
  //It is the same size as the board. Adopting the corners to compensate for the scoring algorithm.
  //The scoring board is recreated to copy the board every time the scoring algorithm begins.
  boolean[][][] affinity = new boolean[21][21][2];
  //Affiinity [this is not and official term in any way]
  //Used in the scoring algorithm. This is very similar to the board with an added third dimension.
  //This array stores data for empty spaces on the board. The spaces have affinities. Having an affinity means being beside a stone or associated to one.
  //ALL spaces will have and affinity as soon as any number of stones are in the board.
  //Position 0 on the third dimension would store booleans for the other 2 dimensions (empty locations) on wether or not it is affiliated with a black stone.
  //Position 1 on the third dimension would do the same for the spaces with white stones beside them
  //The coordinates of an empty space is used to access the first and second dimension.
  //*An important part of understanding the scoring algorithm and the use of this array is to know that,
  //*in this algorithm, the spaces are connected other empty spaces similarly to the way stones are connected.
  //*Meaning, HORIZONTALLY AND VERTICALLY ASSOCIATED SPACES SHARE THE SAME AFFINITIES.
  //An empty space with its first position of the third dimension true, and second positino of the third dimension false is black territory.
  //An empty space with its first position of the third dimension false, and second positino of the third dimension true is white territory.
  //An empty space with both positions of the third dimension true is shared territory.
  //*This array can be alternatively thought of as 2 arrays with 2 dimensions each (as if it were affinity_black[][] and affinity_white[][]).
  //Stones have no affinities as they are understood to be white/black
  boolean[][] liberty =  new boolean[21][21];
  //Liberty
  //Used the same way as the affinity array but with the factors playing the opposite roles.
  //This array stores data on wether or not a stone in a particular coordinate is beside an empty space, and therefore has a liberty.
  //As with the official rules, stones in a chain share the same liberty. This is addressed by the algorithm and cannot be seen in the format of the array alone.
  int white_score = 0;//total of the number of white stones and non-mutual white territory.
  int black_score = 0;//total of the number of black stones and non-mutual black territory.
  int board_size = 19;//board size is immediately set to traditional size.
  boolean menu_looper = true;//helps control flow between menu and game
  boolean board_size_option_looper = true;//controls flow between menu and change size option
  
  //the board is a list of statuses, represented by numbers
  /*Current Statuses:
   * empty = 0
   * black stone = 1
   * white stone = 2
   * corner stone = 7
   * Statuses may be added during coding
   */
  
 public void createBoard()//Troylan Jr
  {//clears the board and creates the corners
    for (int x = 0; x < 21; x++)
    {
      for (int y = 0; y < 21; y++)
      {
        board[x][y] = 0;
      }
    }
    //all ends and corners are assigned the number 7
    for (int y = 0; y < 21; y++)
    {
      board[0][y] = 7;
      board[y][0] = 7;
      board[y][board_size+1] = 7;
      board[board_size+1][y] = 7;
    }
  }
  
  public void printPos (int x, int y)//Troylan Jr
  {//accesses the board[][] and prints a block of 3 character depending on x and y
    if (board[y][x] == 7)
    {//corner
      System.out.print("[=]");
    }
    else if(board[y][x] == 0)
    {//empty intersection/territory/liberty
      System.out.print("-+-");
    }
    else if(board[y][x] == 1)
    {//black stone
      System.out.print("(@)");
    }
    else if(board[y][x] == 2)
    {//white stone
      System.out.print("(O)");
    }
    else{System.out.print("==================[!ERROR!]========================");}
    //Should never print. Prints in case of a number that isn't supposed to be there being inside the board[][]
  }
  
  public void printBoard()//Troylan Jr
  {//prints out the board starting from the top going down
    for(int y = board_size+1; y >= 0; y--)
    {//board_size + 1 accommodates for a corner
      if (y!=0 & y!=20)
      {//prints the y axis on the left side before the rest of the board of that row
        if (y<10)//compensation for 1 digit numbers vs 2 digit numbers
          System.out.print(y +" ");
        else
          System.out.print(y);
      }
      else{
        System.out.print("Y ");}
      for (int x = 0; x < board_size+2; x++)
      {//prints out a 3 character block based ont the data in the position of the board using the above explained method
        printPos(y,x);
      }
      System.out.println("");//moves to next line/row of the board
    }
    System.out.print("  ");
    for (int x = 0; x < board_size+2 ; x++)
    {//prints X axis
      if (x!=0 & x!=20)
      {
        if (x<10)//compensation for 1 digit numbers vs 2 digit numbers
          System.out.print(" "+ x +" ");
        else
          System.out.print(" "+x);
      }
      else
      {
        System.out.print(" X ");
      }
    }
    System.out.println("");//move to next line if something else wants to be printed.
  }
  
  public boolean showMenu()//this method allows you to see the rules of the game or if you already know it, you can just start -- Charles R. //Edited by Troylan Jr
  {
    menu_looper = true;//allows repeated access of below while loop
    while (menu_looper)
    {
      System.out.println("Greetings mortals. Welcome to our unbelievable program.");//Welcoming the players
      System.out.println("Enter 'h' if you need help or 'c' to change board size. Simply press Enter if you would like to start.");//First instructions
      try//try statement
      {
        s = br.readLine();//read line. this is where you can type in 'h' or just press Enter to continue
      }
      catch(IOException ex)//catch statement
      {
        ex.printStackTrace();
      }
      
      if(s.equals("h"))//this will activate if you type in 'h'
      {//Troylan Edit: Had to move this out of the try and catch, which should really just be its own method. *End edit//
        System.out.println("This game is played by two players, black and white, take turns putting 'stones' of their respective colour. Black is always first.");//Rules of the game
        System.out.println("Stones will remain on the board as long it has a liberty. A liberty is an empty space adjacent to a stone.");//Rules of the game
        System.out.println("Stones are removed from the board when they have no liberties, but stones cannot be be moved or removed otherwise.");//Rules of the game
        System.out.println("Stones can be linked together horizontally or vertically beside each other in structures called 'chains'");//Rules of the game
        System.out.println("The player with the largest amount of territory wins the game. A territory is a space on the board occupied by the player's colour or surrounded by the player's colour.");//Rules of the game
        System.out.println("A territory in between both black and white is mutual and holds no value.");//Rules of the game
        System.out.println("Players may pass. When both players pass consecutively because there's no more good moves, the game is ended.");//Rules of the game
        System.out.println("The board cannot look exactly the same twice.");//Rules of the game
        System.out.println("The game will start now. Black will initiate the game.");//Start of the game after this line
      }
      else if(s.equals("q"))
      {
        return false;
      }
      //Troylan Edit: extra method to allow versatility Added Dec. 16/2011
      else if (s.equals("c"))
      {
        board_size_option_looper = true;//allows repeated access of this while loop
        while (board_size_option_looper)
        {
          System.out.println("Enter your desired board size(max 19):");
          try
          {
            s = br.readLine();
          }
          catch(IOException ex)
          {
            ex.printStackTrace();
          }
          try 
          {
            board_size = Integer.parseInt(s); 
          }
          catch(NumberFormatException ugh)//Move to blackMove() to see explanation on this
          {
            System.out.println("Invalid input or input is not a number.");
            board_size_option_looper = true;
          }
          if (board_size < 20)
          {
            System.out.println("Board size successsfully changed to: " + board_size);
            board_size_option_looper = false;
          }
          else 
          {
            System.out.println("Invalid input or input is not a number.");
          }
        }
      }
      else
      {
        menu_looper = false;
      }
    }
    //*End Edit
    return true;
  }
  
  public boolean blackMove()//this method allows black to enter his/her moves -- Charles R. //Edited by Troylan Jr
  {
    try//try statement
    {
      System.out.println("It is black's turn.");//this line is saying that it is black's turn
      System.out.println("Type 'pass' if you would like to pass. Press Enter if you'd like to continue.");//this line is saying that you have two choices: pass or play
      s = br.readLine();//read line.this is where you can type in pass or simply press enter and make your move
      if(s.equals("pass"))//if statement. if you typed in 'pass' it is time for white's turn
      {
        firstpass = true;//boolean firstpass is not false
        return false;//return statement
      }
      else//else statement. this will happen if you didn't type in pass on the read line and if you pressed Enter
      {
        System.out.println("Where would you like to place your first move?");//the program is asking for your move
        System.out.println("Give me the X coordinate.");//the program is asking for the X coordinate
        s = br.readLine();//read line. this is where you type in a number for the X coordinate
        
        try //Troylan T: Had to heavily edit this code block.
        {//What this try and catch structure does is keep the program from crashing in case the user mistypes input and places
         //something that cannot be parsed (i.e. letters/words)
          xax = Integer.parseInt(s);//integer parsing
        }//The parsing method is kept in try
        catch(NumberFormatException ugh)//and Catch saves the program from a crash and uses this codeblock instead of making the program spit out an error report
        {//Notice the NumberFormatException instead of IOException
         //This structure works like an if structure where the string, s, is parsed if possible, and the codeblock inside catch runs if that is not possible.
          System.out.println("Invalid input or input is not a number.");
          return true;
        }//End editing comments of Troylan T//
        
        System.out.println("Give me the Y coordinate.");//the program is asking for the Y coordinate
        s = br.readLine();//read line. this is where you type in a number for the Y coordinate
        
        try 
        {
          yax = Integer.parseInt(s);//integer parsing
        }
        catch(NumberFormatException ugh)
        {
          System.out.println("Invalid input or input is not a number.");
          return true;
        }
      }
    }
    catch(IOException ex)//catch statement
    {
      ex.printStackTrace();
    }
    if (checkBoard())//if statement for checking the board. if the move is typed in twice, it will not activate and the program will ask for a different move
    {
      firstpass = false;//firstpass is false therefore this is true
      board[xax][yax] = 1;//if the board is already equal to 1(black) it cannot be moved/removed unless it is captured
      return false;//return statement
    }
    return true;//return statement
  }
  
  public boolean whiteMove()//this method allows white to enter his/her moves -- Charles R.//Edited by Troylan Jr
  {
    try
    {
      System.out.println("It is white's turn.");//this line is indicating that it is time for white's turn
      System.out.println("Type 'pass' if you would like to pass. Press Enter to continue.");//this line is saying that you have two choices: pass or play
      s = br.readLine();//read line. this is where you type in 'pass' or just press enter to continue
      if(s.equals("pass"))//this if statement will activate if you typed in pass
      {
        firstpass = true;//firstpass is false but in this line, it's true
        return false;//return statement
      }
      else//else statement. this will activate when you pressed enter
      {
        System.out.println("Where would you like to place your first move?");//the program is asking for your move
        System.out.println("Give me the X coordinate.");//the program is asking for the X coordinate
        s = br.readLine();//read line. this is where you type in a number for the X coordinate
        try 
        {
          xax = Integer.parseInt(s);//integer parsing
        }
        catch(NumberFormatException ugh)
        {
          System.out.println("Invalid input or input is not a number.");
          return true;
        }
        System.out.println("Give me the Y coordinate.");//the program is asking for the Y coordinate
        s = br.readLine();//read line. this is where you type in a number for the Y coordinate
        try 
        {
          yax = Integer.parseInt(s);//integer parsing
        }
        catch(NumberFormatException ugh)
        {
          System.out.println("Invalid input or input is not a number.");
          return true;
        }
      }
    }
    catch(IOException ex)//catch statement
    {
      ex.printStackTrace();
    }
    if (checkBoard())//if statement for checking the board. if the move is typed in twice, it will not activate and the program will ask for a different move
    {
      firstpass = false;//firstpass is false, therefore this is true
      board[xax][yax] = 2;//if the board is already equal to 2(white) it cannot be moved/removed unless it is captured
      return false;//return statement
    }
    return true;//return statement
  }
  
  public boolean checkBoard()//this method will check for invalid moves -- Charles R.
  {
    if(board[xax][yax] == 1 | board[xax][yax] == 2 | xax > board_size | yax > board_size)//if the move is typed in twice, it will not activate
    {
      System.out.println("That's an invalid move. Try again.");//self-explanatory
      return false;//return statement
    }
    else//else statement
    {return true;}//return statement
  }
  
  public boolean check()//this method is going to for the consecutive passes -- Charles R.
  {
    boolean hah = true;//boolean variable
    if(firstpass & secpass)//if they both passed, this will activate
    {
      hah = false;//hah is equal to true, but in this line, it is false therefore it will end/stop
    }
    else//else statement. if they didn't pass or one pass only, the game continues
    {
      hah = true;//hah is true, therefore the game will continue
    }
    secpass = firstpass;//secpass is equal to firstpass
    return hah;//return statement
  }
  
//[=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]
//[=][=][=]   SCORING LOGIC  [=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]
//[=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]  
  
  
  
  public void checkLiberty(int x, int y)//Troylan Jr
  {//Analyzes the surroundings of the empty space
    if (scoringboard[x+1][y] == 1 | scoringboard[x-1][y] == 1 | scoringboard[x][y+1] ==1 |scoringboard[x][y-1] == 1)//chck for nearby black stone
      affinity[x][y][0] = true; //the first position of the third dimension of [][][] for the coordinate of the empty space is set to true if it has a black stone nearby (horizontally/vertically)
    
    if (scoringboard[x+1][y] == 2 | scoringboard[x-1][y] == 2 | scoringboard[x][y+1] ==2 |scoringboard[x][y-1] == 2)//check for nearby white stone
      affinity[x][y][1] = true; //the second position of the third dimension of [][][] for the coordinate of the empty space is set to true if it has a white stone nearby (horizontally/vertically)
  }
  
  public void shareDarkness(int x, int y)//Troylan Jr
  {//This method is used to share properties between chains of empty spaces this method particularly shares affinity of black stones
    if(scoringboard[x][y] == 0)//If an empty space is detected (this allows the code not to affect stones, and also makes stones walls where affinities cannot 'jump' over)
      affinity[x][y][0] = true;//That empty space will now share in the black affinity of the nearby empty space that caused this method to be called
    shareAffinities(x, y); //recursion. calls the method that calls this method but with new coordinates.
  }
  
  public void shareLight(int x, int y)//Troylan Jr
  {//same as above. shares affinity for white stones
    if(scoringboard[x][y] == 0)
      affinity[x][y][1] = true;//shares white affinity instead. note the third dimension.
    shareAffinities(x, y);//recursion
  }
  
  public void shareAffinities(int x, int y)//Troylan Jr
  {//called for each empty space, this method will allow that empty space to spread it's affinities to horizontally and vertically attached empty spaces which have yet to recieve the affinity
   //once the affinity have been shared, the method is called for the empty space that just recieved the affinity, spreading it to the nearby empty spaces of that coordinate.
    if (scoringboard[x][y] != 7)
    {//If that empty space has an affinity and is not a corner (the edge empty spaces are protected by corners to prevent out of bounds errors. the corners themselves must not be touched to prevent the same error)
      if (affinity[x][y][0])
      {//If that empty space has a black affinity
       //Test if any of the coordinates beside it has not shared an affinity yet
        if (affinity[x+1][y][0] == false)//if coordinate on the right has not shared the black affinity yet
          shareDarkness(x+1, y);//the coordinate on the right recieves the affinity and this method is called for that coordinate (see above method)
       //do the same for all coordinates directly to the left, above, and below. 
        if (affinity[x-1][y][0] == false)
          shareDarkness(x-1, y);
        
        if (affinity[x][y+1][0] == false)
          shareDarkness(x, y+1);
        
        if (affinity[x][y-1][0] == false)
          shareDarkness(x, y-1);
      }
      
      if (affinity [x][y][1])//note the 3rd dimension for affinity[][][]
      {//exact copy of the above method but shares the white affinity instead
        if (affinity[x+1][y][1] == false)
          shareLight(x+1, y);//note the 3rd dimension used in this method for affiniy[][][]
        
        if (affinity[x-1][y][1] == false)
          shareLight(x-1, y);
        
        if (affinity[x][y+1][1] == false)
          shareLight(x, y+1);
        
        if (affinity[x][y-1][1] == false)
          shareLight(x, y-1);
      }
    }
  }

  public void checkTerritories()//Troylan Jr
  {//This is a really big method that mainly controls the checking algorithm. It is the method that calls the method directly above it
    for (int y = 0; y < 21; y++)
    {
      for (int x = 0; x < 21; x++)
      {
        scoringboard[x][y] = board[x][y];//The algorithm starts by copying the board into the scoring board
        affinity[x][y][0] = false;//It then empties the affinities for everything
        affinity[x][y][1] = false;
      }
    }
  
    for (int x = 0; x < board_size+2; x++)
    {
      for (int y = 0; y < board_size+2; y++)
      {//The algorithm follows by analysing the liberties for each empty space
        if(scoringboard[x][y] == 0)
        {//stones and corners are not affected
          checkLiberty(x, y);
        }
      }
    }
    
    for (int x = 0; x < 20; x++)
    {
      for (int y = 0; y < 20; y++)
      {//all affinities are then shared
        if(scoringboard[x][y] == 0)
        {
          shareAffinities(x, y);
        }
      }
    }
    //after the 3 processes, each affinity will then have its affinities ready to be analysed for scoring
  }
  
   public void checkScore()//Troylan Jr
  {//checks score
    black_score = 0;//clear from last check
    white_score = 0;
    for (int y = 0; y < 21; y++)
    {
      for (int x = 0; x < 21; x++)
      {//check every coordinate on the board
        if (scoringboard[x][y] == 1 | (affinity[x][y][0] & affinity [x][y][1] == false))
          black_score++;//It is a point for black if it is occupied by a black stone or if it is and empty space with ONLY a black affinity
        else if(scoringboard[x][y] == 2 | (affinity[x][y][0] == false & affinity [x][y][1]))
          white_score++;//It is a point for white if it is occupied by a white stone or if it is and empty space with ONLY a white affinity
      }//empty spaces with both affinities are of no score value to either side
    }
    System.out.println("Black score is: " + black_score);//notify of score
    System.out.println("White score is: " + white_score);
    //printScoreBoard();//testing call only
  }
  
//[=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]
//[=][=][=]   CAPTURE LOGIC  [=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]
//[=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]   
  
  
  
  public void checkStone(int x, int y)//Troylan Jr
  {//same as checkLiberty, but instead, it checks if the stone has a liberty instead of if an empty space is affiliated with a stone
    if (board[x+1][y] == 0 | board[x-1][y] == 0 | board[x][y+1] == 0 | board[x][y-1] == 0)
      liberty[x][y] = true;
  }
  
  public void shareLiberty(int x, int y)//Troylan Jr
  {//same as the shareDarkness/Light methods above, but with the liberty[][] instead of the affinity[][][]
    if(board[x][y] == 1 | board[x][y] == 2)
      liberty[x][y] = true;
    shareLiberties(x, y);//recursively calls the method that called it as well
  }
  
  public void shareLiberties(int x, int y)//Troylan Jr
  {//Same as shareAffinities but using the liberty[][] as well AND ONLY STONES OF THE SAME COLOR ARE AFFECTED BY THE STONE IN THE CURRENT X,Y COORDINATE
    if (liberty[x][y] & scoringboard[x][y] != 7)
    {
      if (liberty[x+1][y] == false & board[x+1][y] == board[x][y])
      {
        shareLiberty(x+1, y);
      }
      
      if (liberty[x-1][y] == false & board[x-1][y] == board[x][y])
      {
        shareLiberty(x-1, y);
      }
      
      if (liberty[x][y+1] == false & board[x][y+1] == board[x][y])
      {
        shareLiberty(x, y+1);
      }
      
      if (liberty[x][y-1] == false & board[x][y-1] == board[x][y])
      {
        shareLiberty(x, y-1);
      }
    }
  }
  
  public void redrawBoard()//Troylan Jr
  {//method that removes stones/chains that no longer have a liberty
    for (int y = 0; y < 21; y++)
    {
      for (int x = 0; x < 21; x++)
      {//check every coordinate
        if (liberty[x][y] == false & board[x][y] != 7)
          board[x][y] = 0;//if that coordinate is not a corner and has no liberties, it becomes an empty space.(stones without liberties disappear)
      }
    }
  }
  
  public void capture()//Troylan Jr
  {//same as the checkTerritories method but with one extra process at the end, defined by the above method
   //The three process in the checkTerritories() are mirrored but for the stones using the above methods
   //mainly: Clear, Analyse(for liberty), Share.
    for (int y = 0; y < 21; y++)
    {
      for (int x = 0; x < 21; x++)
      {
        liberty[x][y] = false;
      }
    }
    
    for (int x = 0; x < 21; x++)
    {
      for (int y = 0; y < 21; y++)
      {
        if(board[x][y] == 1 | board[x][y] == 2)
        {
          checkStone(x, y);
        }
      }
    }
    
    for (int x = 0; x < 20; x++)
    {
      for (int y = 0; y < 20; y++)
      {
        if(board[x][y] == 1 | board[x][y] == 2 & scoringboard[x][y] != 7)
        {
          shareLiberties(x, y);
        }
      }
    }
    redrawBoard();//remove all stones without liberties
  }
  
 public boolean askReplay()
 {
   if (black_score>white_score)
   {
     System.out.println("BLACK WINS!");
   }
   else if (black_score<white_score)
   {
     System.out.println("WHITE WINS!");
   }
   else if (black_score<white_score)
   {
     System.out.println("IT IS A DRAW!");
   }
   System.out.println("Press Enter to return to the main menu or q to end the program.");
   try
   {
     s = br.readLine();
   }
   catch(IOException ex)
   {
     ex.printStackTrace();
   }
   if (s.equals("q"))
   {
     return false;
   }
   else
   {
     return true;
   }
 }
  
//[=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]
// TESTING ONLY
//[=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]
// METHODS
//[=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]
  
  public void printScoreBoard()//Troylan Jr
  {//shows the state of the scoring board with relation to affinity.
    for(int y = 20; y >= 0; y--)
    {
      if (y!=0 & y!=20)
      {
        if (y<10)
          System.out.print(y +" ");
        else
          System.out.print(y);
      }
      else{
        System.out.print("Y ");}
      for (int x = 0; x < 21; x++)
      {
        printSPos(y,x);
      }
      System.out.println("");
    }
    System.out.print("  ");
    for (int x = 0; x < 21; x++)
    {
      if (x!=0 & x!=20)
      {
        if (x<10)
          System.out.print(" "+ x +" ");
        else
          System.out.print(" "+x);
      }
      else
      {
        System.out.print(" X ");
      }
    }
    System.out.println("");
  }
  
  public void printSPos (int x, int y)//Troylan Jr
  {
    if (scoringboard[y][x] == 7)
    {
      System.out.print("[=]");
    }
    else if(scoringboard[y][x] == 0)
    {
      if (affinity[y][x][0] & affinity [y][x][1])
      {
        System.out.print("<S>");
      }
      else if (affinity[y][x][0] & affinity [y][x][1]==false)
      {
        System.out.print("-B-");
      }
      else if (affinity[y][x][0]==false & affinity [y][x][1])
      {
        System.out.print("-W-");
      }
      else
      {
        System.out.print("-!-");
      }
    }
    else if(scoringboard[y][x] == 1)
    {
      System.out.print("[@]");
    }
    else if(scoringboard[y][x] == 2)
    {
      System.out.print("[0]");
    }
    else
    {
      System.out.print("!!!");
    }
  }
  
  public void recreateBoard()//testing method only,
  {//places a stones on the board for testing algorithms
    //board[x][y]
    board[4][4]=1;
    board[3][5]=1;
    board[3][6]=1;
    board[3][7]=1; 
    board[4][8]=1;
    board[5][5]=1;
    board[5][6]=1;
    board[5][7]=1;
    
    board[11][6]=2;
    board[12][5]=2;
    board[13][5]=2;
    board[14][5]=2; 
    board[15][6]=2;
    board[12][7]=2;
    board[13][7]=2;
    board[13][8]=2;
    board[15][7]=2;
    board[15][8]=2;
    board[14][8]=2;
    
    board[4][7]=1;
    
    board[9][11]=1;
    board[8][12]=1;
    board[8][13]=1;
    board[9][14]=2;
    board[10][13]=2;
    board[10][12]=2;
    
    board[18][1]=1;
    board[19][2]=1;
    
    board[19][6]=1;
    board[18][7]=1;
    board[19][8]=1;
    
    //4/14
    board[4][15]=1;
    board[4][13]=1;
    board[5][14]=1;
    board[3][14]=1;
    
    board[7][19]=1;
    board[8][18]=1;
    board[9][18]=1;
    board[10][18]=1;
    board[11][19]=1;
    
    board[18][19]=1;
    board[18][18]=1;
    board[18][17]=1;
    board[19][16]=1;
    
    board[2][1]=1;
    board[2][2]=1;
    board[2][3]=1;
    board[2][4]=1;
    board[2][5]=1;
    board[1][6]=1;
    
    board[1][8]=1;
    board[2][9]=1;
    board[2][10]=1;
    board[2][11]=1;
    board[1][12]=1;
    
    
    board[14][12]=1;
    board[15][12]=1;
    board[16][12]=1;
    board[13][13]=1;
    board[13][14]=1;
    board[14][15]=1;
    board[15][15]=1;
    board[16][15]=1;
    board[17][13]=1;
    board[17][14]=1;
    
    board[4][14]=2;
    
    board[12][6]=1;
    board[13][6]=1;
    board[14][6]=1;
    board[14][7]=1;
    
    board[14][13]=2;
    board[14][14]=2;
    board[15][14]=2;
    
  }
}
