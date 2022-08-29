#include <stdio.h>
#include <stdbool.h>

void andTest();

int main( int argc, char *argv[] )  {
    andTest();
}

void andTest()
{
    bool x = true;
    bool y = false;

    bool output = x && y;
}