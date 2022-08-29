#include <stdio.h>
#include <stdbool.h>

void orTest();

int main( int argc, char *argv[] )  {
    orTest();
}

void orTest()
{
    bool x = true;
    bool y = false;

    bool output = x || y;
}