/*
 * Program: Human vs Human GO Runner
 * Author: Charles Reyes and Troylan Tempra
 * Date: December 14, 2011
 * Description: The GO game main/runner
 *  This version, Go+ has an option to allow the changing of board size.
 */

import java.io.*;

public class GoAdv_run
{
  public static void main(String[] args)//main class
  {
    GoAdv nstc = new GoAdv();//this line called out the other class named GO
    String s = "";//string variable
    InputStreamReader isr = new InputStreamReader(System.in);//Input stream reader variable
    BufferedReader br = new BufferedReader(isr);//buffered reader variable
    boolean invalid_black = true;//boolean variable
    boolean invalid_white = true;//boolean variable
    boolean continue_game = true;//boolean variable
    boolean continue_program = true; // 
  
    while (continue_program)
    {
      continue_game = nstc.showMenu();//show menu method. this outputs the menu
      continue_program = continue_game;
      nstc.createBoard();
      nstc.printBoard();
      while(continue_game)//while loop
      {
        invalid_black = true;//invalid_black is true therefore this statement is true
        while(invalid_black)//while loop for black moves
        {
          invalid_black = nstc.blackMove();//blackMove is equal to true
        }
        continue_game = nstc.check();//check method is true
        nstc.capture();
        nstc.printBoard();
        nstc.checkTerritories();
        nstc.checkScore();
        invalid_white = true;//invalid_white is true therefore this statement is true
        while(invalid_white)//while loop for white moves
        {
          invalid_white = nstc.whiteMove();//whiteMove is equal to true
        }
        continue_game = nstc.check();//check method is true
        nstc.capture();
        nstc.printBoard();
        nstc.checkTerritories();
        nstc.checkScore();
      }
      if(continue_program)
      {
        continue_program = nstc.askReplay();
      }
    }
  }
}
