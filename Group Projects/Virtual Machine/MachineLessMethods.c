#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <time.h>
#include <string.h>

int debug = 0;

/* DATA STRUCTURES */


/* GLOBAL VARIABLES */
uint32_t programCounter;
uint32_t registers[8];
uint32_t highestUnused = 0;

struct timeval start, end;

struct WordArray {
	uint32_t * words;
	uint32_t size;
};

struct Table {
	struct WordArray * wordArrays[256];
	struct Table * tables[256];
};

struct Reusable {
	uint32_t id;
	struct WordArray * wordArray;
	struct Reusable * next;
};

struct Reusable * reusables;
struct Table * headTable;
struct WordArray * programArray;


struct Table * curr;
uint32_t newId;
uint32_t size;
struct Reusable * tempReusable;
uint32_t nextIndex;
struct WordArray * newWordArray;
int32_t * words;
struct Reusable * newReusable;
struct WordArray * tempWordArray;
char response;
struct WordArray * originalArray;
uint32_t * originalWords;

/* METHOD PROTOTYPES */
	/* GENERAL METHODS */
void loadProgramFromFile(FILE *);
void runProgram();
	/* SKIPLIST MANIPULATION METHODS */
void initializeStorage();


/* GENERAL METHODS */

int main(int argc, char *argv[]) {
	for (int i = 0; i < 8; i++) {
		registers[i] = 0;
	}
	if (argc != 2) {
		printf("A single argument is expected, specifying the program file to be run.\n");
		exit(1);
	}
	loadProgramFromFile(fopen(argv[1], "rb"));

	mingw_gettimeofday(&start, NULL);
	runProgram();
}


void loadProgramFromFile(FILE* file) {
	initializeStorage();
	programCounter = 0;
	fseek(file, 0, SEEK_END);
	uint32_t fileSize = (uint32_t) ftell(file) / 4;
	fseek(file, 0, SEEK_SET);

	uint32_t * newWords = malloc(sizeof(uint32_t) * fileSize);

	for (int i = 0; i < fileSize; i++) {
		uint32_t instruction = 0;
		fread(&instruction, 4, 1, file);
		uint32_t leftmost = (instruction & 0xFF000000) >> 24;
		uint32_t left = (instruction & 0x00FF0000) >> 8;
		uint32_t right = (instruction & 0x0000FF00) << 8;
		uint32_t rightmost = (instruction & 0x000000FF) << 24;
		instruction = (leftmost | left | right | rightmost);

		//if (debug) printf("instr: %0x\n", instruction);
		newWords[i] = (uint32_t) instruction;
	}
	//if (debug) printf("Instructions parsed.\n");
	fclose(file);
	programArray->size = fileSize;
	programArray->words = newWords;
	//if (debug) printf("Leaving loadProgramFile().\n");
}

void runProgram() {
	while (1) {
		uint32_t word = programArray->words[programCounter];
		uint8_t opCode = (word>>28) & 0x0000000F;
		uint8_t a = (word & 0x000001C0) >> 6;
		uint8_t b = (word & 0x00000038) >> 3;
		uint8_t c = word & 0x00000007;
		uint32_t aVal = registers[a];
		uint32_t bVal = registers[b];
		uint32_t cVal = registers[c];

		//if (debug) printf("op %x, a %u, b %u, c %u\n", word, aVal, bVal, cVal);

		switch (opCode) {
		case 0:;
			if (cVal != 0) registers[a] = bVal;
			break;
		case 1:;
			curr = headTable;
			for (int i = 3; i > 0; i--) {
				curr = curr->tables[(bVal >> (i*8)) & 0xFF];
			}
			registers[a] = curr->wordArrays[bVal & 0xFF]->words[cVal];
			break;
		case 2:;
			curr = headTable;
			for (int i = 3; i > 0; i--) {
				curr = curr->tables[(aVal >> (i*8)) & 0xFF];
			}
			curr->wordArrays[aVal & 0xFF]->words[bVal] = cVal;
			break;
		case 3:;
			registers[a] = bVal + cVal;
			break;
		case 4:;
			registers[a] = bVal * cVal;
			break;
		case 5:;
			registers[a] =  bVal / cVal;
			break;
		case 6:;
			registers[a] = ~(bVal & cVal);
			break;
		case 7:;
			mingw_gettimeofday(&end, NULL);
			printf("\nTime elapsed = %d seconds\n", (end.tv_sec - start.tv_sec));
			exit(0);
			break;
		case 8:;
			size = cVal;
			if (reusables != NULL) {
				tempReusable  = reusables;
				newId = reusables->id;
				newWordArray = reusables->wordArray;
				reusables = reusables->next;
				free(tempReusable);
			} else {
				newId = highestUnused;
				highestUnused++;
				curr = headTable;
				for (int i = 3; i > 0; i--) {
					nextIndex = (newId >> (i*8)) & 0xFF;
					if (curr->tables[nextIndex] == NULL) curr->tables[nextIndex] = (struct Table *) malloc(sizeof(struct Table));
					curr = curr->tables[nextIndex];
				}
				newWordArray = (struct WordArray *) malloc(sizeof(struct WordArray));
				curr->wordArrays[(newId & 0xFF)] = newWordArray;
			}
			newWordArray->size = size;
			newWordArray->words = (uint32_t *) malloc(sizeof(uint32_t) * size);
			words = newWordArray->words;
			memset(words, 0, sizeof(uint32_t) * size);
			registers[b] = newId;
			break;
		case 9:;
			curr = headTable;
			for (int i = 3; i > 0; i--) {
				curr = curr->tables[(cVal >> (i*8)) & 0xFF];
			}
			newReusable = (struct Reusable *) malloc(sizeof(struct Reusable));
			newReusable->id = cVal;
			newReusable->next = reusables;
			newReusable->wordArray = curr->wordArrays[cVal & 0xFF];
			reusables = newReusable;

			free(newReusable->wordArray->words);
			break;
		case 10:;
			putchar(cVal);
			break;
		case 11:;
			response = getchar();
			if (response == 13) registers[c] = 0xFFFFFFFF;
			else registers[c] = response;
			break;
		case 12:;
			programCounter = cVal;
			if (bVal == 0) break;
			curr = headTable;
			for (int i = 3; i > 0; i--) {
				curr = curr->tables[(bVal >> (i*8)) & 0xFF];
			}
			free(programArray->words);
			originalArray = curr->wordArrays[bVal & 0xFF];
			size = originalArray->size;
			programArray->size = size;
			words = (uint32_t *) malloc(sizeof(uint32_t) * size);
			programArray->words = words;
			originalWords = originalArray->words;
			for (int i = 0; i < size; i++) words[i] = originalWords[i];
			break;
		case 13:;
			registers[(word >> 25) & 0x7] = word & 0x01FFFFFF;
			break;
		default:
			exit(1);
		}


		if (opCode != 12) programCounter++;
	}
}


/* WORD ARRAY MANIPULATION METHODS */

/*
 * getWordArray: Returns the WordArray struct identified by id.
 */
void initializeStorage() {
	headTable = (struct Table *) malloc(sizeof(struct Table));
	struct Table * curr = headTable;
	for (int i = 3; i > 0; i--) {
		curr->tables[0] = (struct Table *) malloc(sizeof(struct Table));
		for (int j = 1; j < 256; j++) curr->tables[j] = NULL;
		curr = curr->tables[0];
	}
	curr->wordArrays[0] = (struct WordArray *) malloc(sizeof(struct WordArray));
	for (int j = 1; j < 256; j++) curr->wordArrays[j] = NULL;
	programArray = curr->wordArrays[0];
	highestUnused = 1;
	reusables = NULL;
}

