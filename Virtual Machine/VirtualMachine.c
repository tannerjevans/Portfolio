#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <time.h>


int debug = 0;

/* DATA STRUCTURES */


/* GLOBAL VARIABLES */
uint32_t programCounter;
uint32_t registers[8];
uint32_t highestUnused = 0;
uint32_t numberOfReusables = 0;

struct WordArray {
	uint32_t * words;
	uint32_t size;
};

struct Table {
	struct WordArray * wordArrays[16];
	struct Table * tables[16];
};

struct Reusable {
	uint32_t id;
	struct Reusable * next;
};

struct Reusable * reusables;

struct Table * headTable;

struct WordArray * programArray;







/* METHOD PROTOTYPES */
	/* GENERAL METHODS */
void loadProgramFromFile(FILE *);
void executeInstruction(uint32_t);
uint8_t maskA(uint32_t);
uint8_t maskB(uint32_t);
uint8_t maskC(uint32_t);
	/* SKIPLIST MANIPULATION METHODS */
void initializeStorage();
uint32_t getValue(uint32_t array, uint32_t index);
void changeValue(uint32_t array, uint32_t index, uint32_t value);
uint32_t addArray(uint32_t size);
void removeArray(uint32_t array);
void replaceArray(uint32_t target, uint32_t source);
	/* OPCODE METHODS */
/* 0 */ void condMove_00(uint8_t, uint8_t, uint8_t);
/* 1 */ void arrayIndex_01(uint8_t, uint8_t, uint8_t);
/* 2 */ void arrayUpdate_02(uint8_t, uint8_t, uint8_t);
/* 3 */ void addition_03(uint8_t, uint8_t, uint8_t);
/* 4 */ void multiplication_04(uint8_t, uint8_t, uint8_t);
/* 5 */ void division_05(uint8_t, uint8_t, uint8_t);
/* 6 */ void nand_06(uint8_t, uint8_t, uint8_t);
/* 7 */ void halt_07();
/* 8 */ void allocation_08(uint8_t, uint8_t);
/* 9 */ void deallocation_09(uint8_t);
/* A */ void output_10(uint8_t);
/* B */ void input_11(uint8_t);
/* C */ void loadProgram_12(uint8_t, uint8_t);
/* D */ void loadImmediate_13(uint8_t, uint32_t);


/* GENERAL METHODS */

int main(int argc, char *argv[]) {
	for (int i = 0; i < 8; i++) {
		registers[i] = 0;
	}
	if (argc != 2) {
		printf("A single argument is expected, specifying the program file to be run.\n");
		exit(1);
	}
	//time_t t;
	loadProgramFromFile(fopen(argv[1], "rb"));
	while (1) {
		executeInstruction(programArray->words[programCounter]);
	}
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

		if (debug) printf("instr: %0x\n", instruction);
		newWords[i] = (uint32_t) instruction;
	}
	if (debug) printf("Instructions parsed.\n");
	fclose(file);
	programArray->size = fileSize;
	programArray->words = newWords;
	if (debug) printf("Leaving loadProgramFile().\n");
}

uint8_t maskA(uint32_t word) {
	uint8_t regIndex = (word & 0x000001C0) >> 6;
	return regIndex;
}

uint8_t maskB(uint32_t word) {
	uint8_t regIndex = (word & 0x00000038) >> 3;
	return regIndex;
}

uint8_t maskC(uint32_t word) {
	uint8_t regIndex = word & 0x00000007;
	return regIndex;
}

void executeInstruction(uint32_t word) {
	uint8_t opCode = (word>>28) & 0x0000000F;
	uint8_t a = (word & 0x000001C0) >> 6;
	uint8_t b = (word & 0x00000038) >> 3;
	uint8_t c = word & 0x00000007;

	switch (opCode) {
	case 0:
		condMove_00(a, b, c);
		break;
	case 1:
		arrayIndex_01(a, b, c);
		break;
	case 2:
		arrayUpdate_02(a, b, c);
		break;
	case 3:
		addition_03(a, b, c);
		break;
	case 4:
		multiplication_04(a, b, c);
		break;
	case 5:
		division_05(a, b, c);
		break;
	case 6:
		nand_06(a, b, c);
		break;
	case 7:
		halt_07();
		break;
	case 8:
		allocation_08(b, c);
		break;
	case 9:
		deallocation_09(c);
		break;
	case 10:
		output_10(c);
		break;
	case 11:
		input_11(c);
		break;
	case 12:
		loadProgram_12(b, c);
		break;
	case 13:
		a = (word >> 25) & 0x7;
		uint32_t loadValue = word & 0x01FFFFFF;
		loadImmediate_13(a, loadValue);
		break;
	default:
		exit(1);
	}

	if (opCode != 12) programCounter++;
}


/* OPCODE METHODS */

/*
 * condMove_00: a receives the value in b, unless c contains 0.
 */
void condMove_00(uint8_t a, uint8_t b, uint8_t c) {
	if (registers[c] != 0) registers[a] = registers[b];
}

/*
 * arrayIndex_01: a receives the value in array b at index c.
 */
//register A recieves value from
//array B at
//index C
void arrayIndex_01(uint8_t a, uint8_t b, uint8_t c) {
	uint32_t cVal = registers[c];
	registers[a] = getValue(registers[b], registers[c]);
}

/*
 * arrayUpdate_02: array a receives at index b the value in c.
 */
void arrayUpdate_02(uint8_t a, uint8_t b, uint8_t c) {
	uint32_t bVal = registers[b];
	changeValue(registers[a], registers[b], registers[c]);
}

/*
 * addition_03: a receives the value in b plus the value in c.
 */
void addition_03(uint8_t a, uint8_t b, uint8_t c) {
	uint64_t result = ((uint64_t) registers[b]) + ((uint64_t) registers[c]);
	//if we modulo'd a zero, we would get an error, so simply making sure that does not happen
	if (result != 0 && result > 0x00000000FFFFFFFF) {
		//4294967296 is 2^32. I chose to use the raw number to avoid any oddities with
		//casting a double to an uint32_t
		result = result % 4294967296;
	}
	registers[a] = result;
}

/*
 * multiplication_04: a receives the value in b multiplied by the value in c.
 */
void multiplication_04(uint8_t a, uint8_t b, uint8_t c) {
	uint64_t result = ((uint64_t) registers[b]) * ((uint64_t) registers[c]);
	//if we modulo'd a zero, we would get an error, so simply making sure that does not happen
	if (result != 0 && result > 0x00000000FFFFFFFF) {
		//4294967296 is 2^32. I chose to use the raw number to avoid any oddities with
		//casting a double to an uint32_t
		result = result % 4294967296;
	}
	registers[a] = result;
}

/*
 * division_05: a receives the value in b divided by the value in c, where each value
 * is treated as an unsigned 32-bit number.
 */
void division_05(uint8_t a, uint8_t b, uint8_t c) {
	uint32_t result = ((uint32_t) registers[b]) / ((uint32_t) registers[c]);
	registers[a] = result;
}

/*
 * nand_06: a receives the a bitwise nand_06 of the values in b and c.
 */
void nand_06(uint8_t a, uint8_t b, uint8_t c) {
	uint32_t result = ~( ((uint32_t) registers[b]) & ((uint32_t) registers[c]));
	registers[a] = result;
}

/*
 * halt_07: The machine stops computation.
 */
void halt_07() {
	exit(0);
}

/*
 * allocation_08: A new array is created with number of words equal to value in c. New
 * array is initialized to zeros. b receives the new array's identifier.
 */
void allocation_08(uint8_t b, uint8_t c) {
	registers[b] = addArray(registers[c]);
}

/*
 * deallocation_09: The array identified by c is deallocated.
 */
void deallocation_09(uint8_t c) {
	//removeNode(registers[c]);*/
	removeArray(registers[c]);
}

/*
 * output_10: The value in c is displayed on the console.
 */
void output_10(uint8_t c) {
	printf("%c", (char) registers[c]);
}

/*
 * input_11: c receives the value input_11 by the user, or all 1's if the end of input_11 is
 * signaled.
 */
void input_11(uint8_t c) {
	char response;
	int result = scanf("%c", &response);
	if (response == 13) registers[c] = 0xFFFFFFFF;
	else registers[c] = response;
}

/*
 * loadProgram_12: The array b is duplicated and replaces array 0. Program counter is
 * set to the value of c.
 */
void loadProgram_12(uint8_t b, uint8_t c) {
	programCounter = registers[c];
	if (registers[b] == 0) return;
	replaceArray(0, registers[b]);
}

/*
 * loadImmediate_13: The value in bits 24:0 of instruction is loaded to a, given
 * by bits 27:25.
 */
void loadImmediate_13(uint8_t a, uint32_t loadValue) {
	registers[a] = loadValue;
}



/* WORD ARRAY MANIPULATION METHODS */

/*
 * getWordArray: Returns the WordArray struct identified by id.
 */
void initializeStorage() {
	headTable = (struct Table *) malloc(sizeof(struct Table));
	struct Table * curr = headTable;
	for (int i = 7; i > 0; i--) {
		curr->tables[0] = (struct Table *) malloc(sizeof(struct Table));
		for (int j = 1; j < 16; j++) curr->tables[j] = NULL;
		curr = curr->tables[0];
	}
	curr->wordArrays[0] = (struct WordArray *) malloc(sizeof(struct WordArray));
	for (int j = 1; j < 16; j++) curr->wordArrays[j] = NULL;
	programArray = curr->wordArrays[0];
	highestUnused = 1;
}


uint32_t getValue(uint32_t array, uint32_t index) {
	struct Table * curr = headTable;
	for (int i = 7; i > 0; i--) {
		curr = curr->tables[(array >> (i*4)) & 0xF];
	}
	uint32_t returnValue = curr->wordArrays[array & 0xF]->words[index];
	return returnValue;
}

void changeValue(uint32_t array, uint32_t index, uint32_t value) {
	struct Table * curr = headTable;
	for (int i = 7; i > 0; i--) {
		curr = curr->tables[(array >> (i*4)) & 0xF];
	}
	curr->wordArrays[array & 0xF]->words[index] = value;
}

uint32_t addArray(uint32_t size) {
	uint32_t newId;
	if (numberOfReusables != 0) {
		struct Reusable * temp  = reusables;
		newId = reusables->id;
		reusables = reusables->next;
		free(temp);
		numberOfReusables--;
	} else {
		newId = highestUnused;
		highestUnused++;
	}
	struct Table * curr = headTable;
	for (int i = 7; i > 0; i--) {
		uint32_t nextIndex = (newId >> (i*4)) & 0xF;
		if (curr->tables[nextIndex] == NULL) curr->tables[nextIndex] = (struct Table *) malloc(sizeof(struct Table));
		curr = curr->tables[nextIndex];
	}
	struct WordArray * newWordArray = (struct WordArray *) malloc(sizeof(struct WordArray));
	newWordArray->size = size;
	newWordArray->words = (uint32_t *) malloc(sizeof(uint32_t) * size);
	uint32_t * words = newWordArray->words;
	if (words == NULL) {
		printf("\n\nMalloc failure.");
		exit(1);
	}
	for (int i = 0; i < size; i++) words[i] = 0;
	curr->wordArrays[(newId & 0xF)] = newWordArray;
	return newId;
}

void removeArray(uint32_t array) {
	struct Table * curr = headTable;
	for (int i = 7; i > 0; i--) {
		curr = curr->tables[(array >> (i*4)) & 0xF];
	}
	struct Reusable * newReusable = (struct Reusable *) malloc(sizeof(struct Reusable));
	newReusable->id = array;
	newReusable->next = reusables;
	reusables = newReusable;

	struct WordArray * temp = curr->wordArrays[array & 0xF];
	free(temp->words);
	free(temp);

	numberOfReusables++;
}

void replaceArray(uint32_t target, uint32_t source) {
	struct Table * targetTable = headTable;
	for (int i = 7; i > 0; i--) {
		targetTable = targetTable->tables[(target >> (i*4)) & 0xF];
	}
	struct Table * sourceTable = headTable;
	for (int i = 7; i > 0; i--) {
		sourceTable = sourceTable->tables[(source >> (i*4)) & 0xF];
	}
	struct WordArray * targetArray = targetTable->wordArrays[target & 0xF];
	free(targetArray->words);
	free(targetArray);
	struct WordArray * duplicateArray = (struct WordArray *) malloc(sizeof(struct WordArray));
	targetTable->wordArrays[target & 0xF] = duplicateArray;
	struct WordArray * originalArray = sourceTable->wordArrays[source & 0xF];
	uint32_t size = originalArray->size;
	duplicateArray->size = size;
	uint32_t * words = (uint32_t *) malloc(sizeof(uint32_t) * size);
	duplicateArray->words = words;
	uint32_t * originalWords = originalArray->words;
	for (int i = 0; i < size; i++) words[i] = originalWords[i];
	if (target == 0) programArray = duplicateArray;
}
