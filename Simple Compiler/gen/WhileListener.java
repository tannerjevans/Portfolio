// Generated from C:/Users/magid/Github Test/simple_compiler/cs_454_project01/SimpleCompiler/src\While.g4 by ANTLR 4.9.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link WhileParser}.
 */
public interface WhileListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link WhileParser#file}.
	 * @param ctx the parse tree
	 */
	void enterFile(WhileParser.FileContext ctx);
	/**
	 * Exit a parse tree produced by {@link WhileParser#file}.
	 * @param ctx the parse tree
	 */
	void exitFile(WhileParser.FileContext ctx);
	/**
	 * Enter a parse tree produced by {@link WhileParser#line}.
	 * @param ctx the parse tree
	 */
	void enterLine(WhileParser.LineContext ctx);
	/**
	 * Exit a parse tree produced by {@link WhileParser#line}.
	 * @param ctx the parse tree
	 */
	void exitLine(WhileParser.LineContext ctx);
	/**
	 * Enter a parse tree produced by {@link WhileParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(WhileParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link WhileParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(WhileParser.ExprContext ctx);
}