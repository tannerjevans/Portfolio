#include <stdio.h>

void collatz(int);

int main( int argc, char *argv[] )  {
   if(argc > 1)
   {
       collatz(atoi (argv[1]));
   }
}

void collatz(int input)
{
    int n = input;
    int steps = 0;
    while (n > 1)
    {
        int rem = n;
        int quot = 0;
        while(rem > 1)
        {
            rem = rem - 2;
            quot = quot + 1;
        }
        if(rem = 0)
            {n = quot;}
        else
            {n = 3 * n + 1;}

        steps = steps + 1;
    }

    int output = steps;
}