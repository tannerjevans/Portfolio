#include <stdlib.h>
#include <stdio.h>

extern void test_function(long int * vars);
char * functionName = "test_function";
long int vars[1];
char * varNames[] = { "output" };
int ordMap[] = { 0 };
int numVars = 1;

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
	test_function(vars);
	printf("Final state:\n");
	for (i = 0; i < numVars; i++)
		printf("%s=%ld\n", varNames[ordMap[i]], vars[ordMap[i]]);
}
