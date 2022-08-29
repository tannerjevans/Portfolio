# Generated assembly code header:
		.file		"testingsrctesttxt_function.s"
		.option		nopic
		.text
		.align		1
		.globl		testingsrctesttxt_function
		.type		testingsrctesttxt_function, @function
# Generated assembly code prologue:
testingsrctesttxt_function:
		ADDI s1, x0, 10
		ADDI s2, x0, 2
		ADDI s3, x0, 3
		ADDI s4, x0, 4
		ADDI s5, x0, 5
		ADDI s6, x0, 6
		ADDI s7, x0, 7
		ADDI s8, x0, 8
		ADDI s9, x0, 9
		ADDI s10, x0, 11
		ADDI s11, x0, 12
		ADDI a1, x0, 13
		SW a1, 0(a0)
		ADDI a1, x0, 14
		SW a1, 8(a0)
		ADDI a1, x0, 15
		SW a1, 16(a0)
		ADDI a1, x0, 16
		SW a1, 24(a0)
		ADDI t6, x0, 10
		BLT s1, t6, OD0
	label1:
		ADDI t0, x0, 1
		SUB t0, s1, t0
		ADD s1, x0, t0
		ADD s2, x0, s1
		ADDI t6, x0, 10
				BGE s1, t6, label1
	OD0:
		LW t0, 0(a0)
		ADD a1, x0, t0
		SW a1, 32(a0)
		# Generated assembly code epilogue:
		JR		ra
		# Generated assembly code footer:
		.size		testingsrctesttxt_function, .-testingsrctesttxt_function
		.ident		"Team No Compiler Errors: Del, Nathan, and Tanner"
