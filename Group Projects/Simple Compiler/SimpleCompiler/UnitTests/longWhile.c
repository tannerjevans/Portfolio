#include <stdio.h>

int longWhile();

int main( int argc, char *argv[] )  {
    int output = longWhile();
}

int longWhile()
{
    int bigNum = 999999999;
    int y = 1;
    int constantFold;

    while(bigNum > 0)
    {
        bigNum = bigNum - 1;
        constantFold = bigNum + 9 - 7 * y;
    }

    return constantFold;
}