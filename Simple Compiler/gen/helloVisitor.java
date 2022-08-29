// Generated from C:/Users/tannerjevans/Documents/GitHub Files/cs454_project01/cs_454_project01/testing/src\SimpleCompiler.g4 by ANTLR 4.9.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link helloParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface helloVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link helloParser#file}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFile(helloParser.FileContext ctx);
	/**
	 * Visit a parse tree produced by {@link helloParser#line}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLine(helloParser.LineContext ctx);
	/**
	 * Visit a parse tree produced by {@link helloParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(helloParser.ExprContext ctx);
}