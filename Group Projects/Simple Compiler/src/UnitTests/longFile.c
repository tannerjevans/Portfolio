#include <stdio.h>

void longFile();

int main( int argc, char *argv[] )  {
    longFile();
}

void longFile()
{
    int x = 0;
    int steps = 10;
    int output;
    int y;

    while(steps > 0)
    {
        while(steps > 2)
        {
            y = (steps - 2) + ((2 - 1) * (3 + 1));
        }
        steps = steps - 1;

        if(y > 9)
        {output = y;}
        else
        {output = 0;}
    }

    if(y > 0)
    {}
    else
    {}

    bool temp = 0;
    if(y = 2)
    {temp = 1;}

    bool z = temp + 1 * x + temp;

    int tempy = 10;
    int tempz = 1;
    while(tempy > 1)
    {
        tempz = tempz * tempy;
        tempy = tempy - 1;
    }
    tempy = 0;
    int factorialOutput = tempz;
}