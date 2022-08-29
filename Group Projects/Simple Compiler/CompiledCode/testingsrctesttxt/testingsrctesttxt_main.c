#include <stdlib.h>
#include <stdio.h>

extern void testingsrctesttxt_function(long int * vars);
char * functionName = "testingsrctesttxt_function";
long int vars[16];
char * varNames[] = { "j", "b", "c", "d", "e", "f", "g", "h", "i", "k", "l", "m", "n", "o", "p", "a" };
int ordMap[] = { 15, 1, 2, 3, 4, 5, 6, 7, 8, 0, 9, 10, 11, 12, 13, 14 };
int numVars = 16;

int main(int argc, char ** argv) {
	int i;
	if (argc != numVars + 1) {
		printf("Parameters of %s: ", functionName);
		for (i = 0; i < numVars; i++)
			printf("%s ", varNames[ordMap[i]]);
		printf("\n");
		exit(1);
	}
	for (i = 0; i < numVars; i++)
		vars[ordMap[i]] = atol(argv[i + 1]);
	printf("Initial state:\n");
	for (i = 0; i < numVars; i++)
		printf("%s=%ld\n", varNames[ordMap[i]], vars[ordMap[i]]);
	testingsrctesttxt_function(vars);
	printf("Final state:\n");
	for (i = 0; i < numVars; i++)
		printf("%s=%ld\n", varNames[ordMap[i]], vars[ordMap[i]]);
}
