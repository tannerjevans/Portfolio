# Generated assembly code header:
		.file		"test_function.s"
		.option		nopic
		.text
		.align		1
		.globl		test_function
		.type		test_function, @function
# Generated assembly code prologue:
test_function:
		ADDI		sp, sp, -112
		SD		s0, 0(sp)
		SD		s1, 8(sp)
		SD		s2, 16(sp)
		SD		s3, 24(sp)
		SD		s4, 32(sp)
		SD		s5, 40(sp)
		SD		s6, 48(sp)
		SD		s7, 56(sp)
		SD		s8, 64(sp)
		SD		s9, 72(sp)
		SD		s10, 80(sp)
		SD		s11, 88(sp)
		LW s1, 0(a0)
		ADDI s1, x0, 0
		SW s1, 0(a0)
		# Generated assembly code epilogue:
		LD		s0, 0(sp)
		LD		s1, 8(sp)
		LD		s2, 16(sp)
		LD		s3, 24(sp)
		LD		s4, 32(sp)
		LD		s5, 40(sp)
		LD		s6, 48(sp)
		LD		s7, 56(sp)
		LD		s8, 64(sp)
		LD		s9, 72(sp)
		LD		s10, 80(sp)
		LD		s11, 88(sp)
		ADDI		sp, sp, 112
		JR		ra
		# Generated assembly code footer:
		.size		test_function, .-test_function
		.ident		"Team No Compiler Errors: Del, Nathan, and Tanner"
