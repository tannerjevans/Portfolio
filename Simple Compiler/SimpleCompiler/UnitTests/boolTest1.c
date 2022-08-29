#include <stdio.h>
#include <stdbool.h>

bool boolTest(int);

int main( int argc, char *argv[] )  {
    bool output = false;
    if(argc > 1)
    {
        output = boolTest(atoi (argv[1]));
    }
}

bool boolTest(int input)
{
    int n = input;

    if(n > 2)
    {return true;}
    else
    {return false;}
}