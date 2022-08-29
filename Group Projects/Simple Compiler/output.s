# Generated assembly code header:
		.file		"testingsrctesttxt_function.s"
		.option		nopic
		.text
		.align		1
		.globl		testingsrctesttxt_function
		.type		testingsrctesttxt_function, @function
# Generated assembly code prologue:
testingsrctesttxt_function:
		LW t0, 8(a0)
		ADD a1, x0, t0
		SW a1, 0(a0)
		ADDI a1, x0, 0
		SW a1, 16(a0)
	label1:
		LW t0, 0(a0)
		ADD a1, x0, t0
		SW a1, 24(a0)
		ADDI a1, x0, 0
		SW a1, 32(a0)
	label2:
		ADDI t1, x0, 2
		LW t0, 24(a0)
		SUB t1, t0, t1
		ADD a1, x0, t1
		SW a1, 24(a0)
		LW t1, 32(a0)
		ADDI t0, t1, 1
		ADD a1, x0, t0
		SW a1, 32(a0)
		ADDI t6, x0, 1
		ADDI t6, t6, 1
		LW t0, 24(a0)
		BGE t0, t6, label2
		ADDI t6, x0, 0
		LW t0, 24(a0)
		BNE t0, t6, ELSE0
		LW t1, 32(a0)
		ADD a1, x0, t1
		SW a1, 0(a0)
		BEQ x0, x0, EXIT0
	ELSE0:
		ADDI t0, x0, 3
		LW t1, 0(a0)
		MUL t0, t1, t0
		ADDI t1, t0, 1
		ADD a1, x0, t1
		SW a1, 0(a0)
	EXIT0:
		LW t1, 16(a0)
		ADDI t0, t1, 1
		ADD a1, x0, t0
		SW a1, 16(a0)
		ADDI t6, x0, 1
		ADDI t6, t6, 1
		LW t0, 0(a0)
		BGE t0, t6, label1
		LW t0, 16(a0)
		ADD a1, x0, t0
		SW a1, 40(a0)
		# Generated assembly code epilogue:
		jr		ra
		# Generated assembly code footer:
		.size		testingsrctesttxt_function, .-testingsrctesttxt_function
		.ident		"Team No Compiler Errors: Del, Nathan, and Tanner"
		.section		.note.GNU-stack,"",@progbits
