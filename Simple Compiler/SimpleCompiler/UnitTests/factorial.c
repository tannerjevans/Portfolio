#include <stdio.h>

void factorial(int);

int main( int argc, char *argv[] )  {
   if(argc > 1)
   {
       factorial(atoi (argv[1]));
   }
}

void factorial(int input)
{
    int y = input;
    int z = 1;
    while( y > 1)
    {
        z = z * y;
        y = y - 1;
    }
    y = 0;
    int output = z;
}