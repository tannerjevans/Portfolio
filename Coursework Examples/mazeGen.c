/******************************************************************************
 Tanner J. Evans
 mazeGen.c -- A file to generate a random maze of any specified width and
  height greater than or equal to three. Also provides a solution
  algorithm, and a print function for unsolved and solved mazes.
******************************************************************************/

#include <stdlib.h>
#include <stdio.h>
#include <stdint.h>
#include "mazegen.h"

static uint8_t **maze;
static int globWidth = 0;
static int globHeight = 0;
static int globWayPointX = 0;
static int globWayPointY = 0;
static int globWayPointAlleyLength = 0;
static double globWayPointDirectionPercent = 0;
static double globStraightProbability = 0;
static int globPrintAlgorithmSteps = 0;
short firstTop = 1;
short firstBottom = 1;
int randXBot;
int alleysToCarve = 4;
int cellsToCarve = 0;
int tailsToCarve = 4;
int tailCellsToCarve = 0;
int waypointEnds[4][2];

/******************************************************************************
 function carveMaze
 Parameters:
   short x: The x value of the current cell. Can be any value of a short,
     but is expected to be between 1 and the size of the maze, inclusive.
   short y: The y value of the current cell. Same value restrictions as x.
   uint8_t dir: The direction of the previous cell. This value is used to
     exclude the previous cell from calculations when determining which
     surrounding cells it is possible to move into. It can take any value
     from 0 to 3, and is used to access the DIRECTIONS_LIST array defined
     in mazetest.c and assign the value of each enumerated constant listed
     there to the current cell.
 Return:
   void
 This function recursively "carves" a maze. It starts at the initial
   provided coordinate and randomly steps forward in whatever open
   directions are available, calling itself each time. When it dead-ends,
   it returns.
 Algorithm:
  set current cell to direction it came from (opens a pipe towards it)
  while true:
    count the number of open directions and track which are open
    if there are open directions:
      choose one at random
      if this is the first call, overwrite cell data with data to make a
        pipe opening only into the new direction we're going
      else, add the value of our new direction to our current cell,
        leaving our pipe open to where we came from
      calculate the opposite of our new direction
      change flag to show that subsequent carveMaze calls won't be first
      call carveMaze with new x and y values, modified by the appropriate
        amount as provided by the index values of helper arrays, as well
        as the opposite of the direction were moving
      loop
    else break out of while loop if there are no open directions
  return;
******************************************************************************/

/******************************************************************************
 I will be using terms like "opens pipe to [blank]" throughout this code,
 as it speaks to the outcome rather than the addition or subtraction of
 numbers that is literally occurring in the code. Be aware that in terms
 of this program, adding a 1, 2, 4, or 8, (i.e. a 1 to the binary number in
 the 1st, 2nd, 3rd, or 4th bit) is equivalent to adding an opening to that
 cell's pipe character in the direction of N, E, S, and W, respectively.
 From here forward I will refer to these operations exclusively as opening
 pipes for clarity.
******************************************************************************/

void carveMaze(short x, short y, uint8_t dir)
{
  maze[x][y] = DIRECTION_LIST[dir]; //opens a pipe towards previous cell

  if (firstTop && y == 1)
  {
    maze[x][y] += NORTH;
    firstTop = 0;
  }

  if (firstBottom && y == (globHeight-2))
  {
    maze[x][y] += SOUTH;
    firstBottom = 0;
  }

  while (1) //repeat until break
  {
    uint8_t count = 0; //counter for number of directions
    uint8_t directions[] = { 0, 0, 0, 0 }; //array to track each open direction
    for (uint8_t i = 0; i < 4; i++) //loops through each direction
    {                               //i can be seen as expressive of which direction we're checking
      if (maze[x+DIRECTION_DX[i]][y+DIRECTION_DY[i]] == 0) //if that cell is empty
      {
        directions[count] = i; //add which direction we're checking to the next value in the array
        count++; //increment counter
      }
      //else do nothing
    }
    if (count != 0) //if there are open directions
    {
      uint8_t xCall;
      uint8_t yCall;
      uint8_t newDir = directions[rand()%(count)];; //grab that direction value
      uint8_t oppDir; //value for opposite direction

      if (alleysToCarve)
      {

        maze[x][y] += SPECIAL;


        if (dir == 0 || dir == 1) //if N or E
        {
          newDir = dir + 2;  //switch to S or W
        }
        else
        {
          newDir = dir - 2; //switch to N or E
        }
        if (cellsToCarve && maze[x+DIRECTION_DX[newDir]][y+DIRECTION_DY[newDir]] == 0)
        {
          --cellsToCarve;
          xCall = x+DIRECTION_DX[newDir];
          yCall = y+DIRECTION_DY[newDir];
        }
        else
        {
          waypointEnds[alleysToCarve][0] = x;
          waypointEnds[alleysToCarve][1] = y;
          --alleysToCarve;
          cellsToCarve = globWayPointAlleyLength;
          xCall = globWayPointX;
          yCall = globWayPointY;
        }
      }
      else if (tailsToCarve)
      {
        if (tailCellsToCarve)
        {
          --tailCellsToCarve;
          xCall = x+DIRECTION_DX[newDir];
          yCall = y+DIRECTION_DY[newDir];
        }
        else
        {
          tailCellsToCarve = (globWidth-2)*(globHeight-2)*(globWayPointDirectionPercent);
          int randWay = rand()%(tailsToCarve);
          xCall = waypointEnds[randWay][0];
          yCall = waypointEnds[randWay][1];
          waypointEnds[randWay][0] = waypointEnds[tailsToCarve][0];
          waypointEnds[randWay][1] = waypointEnds[tailsToCarve][1];
          --tailsToCarve;
        }
      }
      else
      {
        xCall = x+DIRECTION_DX[newDir];
        yCall = y+DIRECTION_DY[newDir];
      }

      if (x == globWayPointX && y == globWayPointY) //if this is the first carve call
      {
        maze[x][y] = ALL_DIRECTIONS + SPECIAL; //close pipe opening and open pipe to new direction
      }
      else
      {
        maze[x][y] += DIRECTION_LIST[newDir]; //open pipe in new direction, leaving old one open
      }

      if (newDir == 0 || newDir == 1) //if N or E
      {
        oppDir = newDir + 2;  //switch to S or W
      }
      else
      {
        oppDir = newDir - 2; //switch to N or E
      }

      carveMaze(xCall, yCall, oppDir); //call self with opposite of new direction and x and y values of new direction
    }
    else
    {
      break;
    }
  }
  return;
}



/******************************************************************************
 function mazeGenerate
 Parameters:
   int width: The provided width of the maze to be generated. Can take any
     value larger than 3, but larger values of course become risky.
   int height: The provided height of the maze. Same limitations as width.
   int wayPointX: Unused in this iteration.
   int wayPointY: Unused in this iteration.
   int wayPointAlleyLength: Unused in this iteration.
   double wayPointDirectionPercent: Unused in this iteration.
   double straightProbability: Unused in this iteration.
   int printAlgorithmSteps: Unused in this iteration.
 Return:
   int: returns 0 if successful, 1 if invalid parameters were passed
 This function frees any previously allocated maze and then sets up a new
   one. It performs malloc calls to minimize the size of the maze. It
   then chooses a random location to begin a maze carve, and a random
   direction. It calls carveMaze with these values. After this is done,
   it punches an opening at random both along the top and along the
   bottom of the maze, saving the value at the bottom for use in finding
   the maze's solution.
******************************************************************************/

int mazeGenerate(int width, int height,
  int wayPointX,
  int wayPointY,
  int wayPointAlleyLength,
  double wayPointDirectionPercent,
  double straightProbability,
  int printAlgorithmSteps)
{
  mazeFree(); //free previous maze allocations, if any

  if (width < 3 || height < 3 || wayPointX < 1 || wayPointX > width || wayPointY < 1 || wayPointY > height) //check for invalid parameters
  {
    printf("Unsupported dimensions.\n"); //if they exist, send notification and return True
    return 1;
  }

  globWidth = width+2; //set global variables
  globHeight = height+2;
  globWayPointX = wayPointX;
  globWayPointY = wayPointY;
  globWayPointAlleyLength = wayPointAlleyLength;
  globWayPointDirectionPercent = wayPointDirectionPercent;
  globStraightProbability = straightProbability;
  globPrintAlgorithmSteps = printAlgorithmSteps;
  cellsToCarve = globWayPointAlleyLength;
  tailCellsToCarve = (globWidth-2)*(globHeight-2)*(globWayPointDirectionPercent);

  maze = malloc((globWidth)*sizeof(*maze)); //allocate memory sufficient to hold width + 2 number of pointers
  for (int i = 0; i < (globWidth) ; i++) //for each pointer
  {
    maze[i] = malloc((globHeight)*sizeof(uint8_t)); //allocate space for an array of uint8_t (1-byte size integers)
    for (int j = 0; j < (globHeight); j++) //for each value in the array
    {
      if (i == 0 || i == globWidth-1 || j == 0 || j == globHeight-1) //test if this is a border cell
      {
        maze [i][j] = 17; //if so, initialize it to dummy value
      } else
      {
        maze[i][j] = 0; //if not, initialize it to zero
      }
    }
  }
  uint8_t dir = rand()%4; //random direction for carve
  carveMaze(wayPointX, wayPointY, dir); //carveMaze call
  return 0; //return success
}

/******************************************************************************
 function recursiveSolve
 Parameters:
   short xStart: The x value of the current cell. Can be any value of a
     short, but is expected to be between 1 and the size of the maze,
     inclusive.
   short yStart: The y value of the current cell. Same value restrictions
     as xStart.
   uint8_t dir: The direction of the previous cell. This value is used to
     exclude the previous cell from calculations when determining which
     surrounding cells it is possible to move into. It can take any value
     from 0 to 3, and is used to access the DIRECTION_LIST array defined
     in mazetest.c and assign the value of each enumerated constant listed
     there to the current cell.
 Return:
   uint8_t: Returns a 0 if the end was not found, and a 1 if it was.
 This function recursively solves a maze. It starts at the initial
   provided coordinate and steps forward in whatever open
   directions are available, calling itself each time. When it dead-ends,
   it returns.
 Algorithm:
  set current cell to 1
  if we have reached the goal, return 1
  count number of directions and which ones, excluding direction came from
  while directions are left:
    choose a new direction from end of possibles
    build opposite direction for recursive call
    call self with new values of x and y and direction
    if call returns 1: return 1
    else decrement count and repeat
  if no solution is found:
    change this cell in solution[][] to 0, since it is not part of the
      solution path
    return 0
******************************************************************************/

uint8_t recursiveSolve(short xStart, short yStart, uint8_t dir)
{
  maze[xStart][yStart] += VISITED; //mark this cell in solution[][] as part of solution

  if (yStart == globHeight-2 && ((maze[xStart][yStart] & SOUTH) != 0)) //if we've reached our goal
  {
    return 1; //return 1
  }

  uint8_t count = 0; //var for number of directions
  uint8_t holder = ((maze[xStart][yStart] & ALL_DIRECTIONS) - DIRECTION_LIST[dir]); //var for value in maze[][]
  uint8_t directions[] = { 0, 0, 0, 0 }; //array for storing directions

  for (uint8_t i = 0; i < 4; i++) //for each direction
  {
    if ((holder%2) == 1) //if pip opens in direction i
    {
      directions[count] = i; //count it
      count++;
    }
    holder /= 2; //move to next bit
  }
  while (count != 0) //as long as directions are possible
  {
    uint8_t newDirIndex = count-1; //new direction index
    uint8_t newDir = directions[newDirIndex]; //new direction is clockwise-most direction
    uint8_t oppDir;

    if (newDir == 0 || newDir == 1) //if N or E
    {
      oppDir = newDir + 2; //S or W
    }
    else
    {
      oppDir = newDir - 2; //N or E
    }

    if (recursiveSolve(xStart+DIRECTION_DX[newDir], yStart+DIRECTION_DY[newDir], oppDir)) //call self with new cell, if call returned solved
    {
      return 1;//return solved
    }
    --count; //decrement count
  }
  maze[xStart][yStart] -= VISITED; //if you run out of directions without a cell around returning solved
  return 0; //return not solved
}

/******************************************************************************
  function mazeSolve
  Parameters:
    none
  Return:
    none
  mazeSolve is called from main. It instantiates a solution[][] array the
    same size as our maze[][] array, using malloc in the same way that
    our maze[][] setup did. It initializes all values to 0, which is read
    as "not part of the solution." It then finds the point in the top of
    the maze which contains an open upwards pipe (the entrance), and
    calls recursiveSolve with that beginning x value at the top of the
    maze, and the direction it must be approaching from: North.
******************************************************************************/

void mazeSolve()
{
  int xStart; //starting value
  for (int m = 1; m < globWidth; m++) //for each cell on top
  {
    if (maze[m][1]%2 == 1) //if it is odd (has a pipe open up)
    {
      xStart = m; //it is our start value
      break; //break because done
    }
  }
  recursiveSolve(xStart, 1, 0); //find recursive solution
}

/******************************************************************************
  function mazePrint
  Parameters:
    none
  Return:
    none
  mazePrint is a function called from main. It outputs a header as prescribed
    by project guidelines, and then prints out one of two possible versions
    of the maze: one without a solution and one with one, depending on if
    a solution call has been made since the last generate. If no solution call
    has been made, it prints the maze all in white. If it has, it prints
    the values of maze[][] whose coordinates correspond to a 1 in solution[][]
    as green. The rest it prints white.
******************************************************************************/

void mazePrint()
{
  textcolor(TEXTCOLOR_WHITE); //ensure that terminal is printing in white
  printf("\n\n");
  printf("========================\n");
  printf("Maze(%d x %d): (%d, %d)\n",
         (globWidth-2), (globHeight-2), globWayPointX, globWayPointY);
  printf("========================\n"); //print out current maze information

  for (int i = 1; i < globHeight-1 ; i++) //for each row
  {
    for (int j = 1; j < globWidth-1; j++) //for each column
    {
      if (maze[j][i] == 17) //if it's a boundary value
      {
        break; //ignore it
      }
      if ((maze[j][i] & VISITED) != 0) //if this coordinate is on the solution path
      {
        textcolor(TEXTCOLOR_GREEN); //print next stuff in green
      }
      else if ((maze[j][i] & SPECIAL) != 0) //if this coordinate is an alley
      {
        textcolor(TEXTCOLOR_RED); //print next stuff in red
      }
      else //if it's not
      {
        textcolor(TEXTCOLOR_WHITE); //print next stuff in white
      }
      printf("%c", pipeList[(maze[j][i] & ALL_DIRECTIONS)]); //print the character
    }
    printf("\n");
  }
  textcolor(TEXTCOLOR_WHITE); //reset to white
}

/******************************************************************************
  function mazeFree
  Parameters:
    none
  Return:
    none
  mazeFree is called from main and from generateMaze on each of its calls. It
    deallocates memory from each allocation made in the construction of a maze
    or its solution. Also resets a few values specific to specific mazes.
******************************************************************************/

void mazeFree()
{
  for (int i = 0; i < globWidth; i++) //for each array
  {
    free(maze[i]); //deallocate
  }
  if (globWidth) //for first alloc
  {
    free(maze); //deallocate
  }
  globWidth = 0; //resets
  globHeight = 0;
  globWayPointAlleyLength = 0;
  globWayPointDirectionPercent = 0;
  globStraightProbability = 0;
  globPrintAlgorithmSteps = 0;
  firstTop = 1;
  firstBottom = 1;
  alleysToCarve = 4;
  tailsToCarve = 4;
  tailCellsToCarve = 0;
  cellsToCarve = 0;
}
