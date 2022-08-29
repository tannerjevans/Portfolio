// Generated from C:/Users/tannerjevans/Documents/GitHub Files/cs454_project01/cs_454_project01/testing/src\SimpleCompiler.g4 by ANTLR 4.9.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link helloParser}.
 */
public interface helloListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link helloParser#file}.
	 * @param ctx the parse tree
	 */
	void enterFile(helloParser.FileContext ctx);
	/**
	 * Exit a parse tree produced by {@link helloParser#file}.
	 * @param ctx the parse tree
	 */
	void exitFile(helloParser.FileContext ctx);
	/**
	 * Enter a parse tree produced by {@link helloParser#line}.
	 * @param ctx the parse tree
	 */
	void enterLine(helloParser.LineContext ctx);
	/**
	 * Exit a parse tree produced by {@link helloParser#line}.
	 * @param ctx the parse tree
	 */
	void exitLine(helloParser.LineContext ctx);
	/**
	 * Enter a parse tree produced by {@link helloParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(helloParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link helloParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(helloParser.ExprContext ctx);
}