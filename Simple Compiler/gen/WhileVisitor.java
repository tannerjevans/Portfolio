// Generated from C:/Users/magid/Github Test/simple_compiler/cs_454_project01/SimpleCompiler/src\While.g4 by ANTLR 4.9.2
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link WhileParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface WhileVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link WhileParser#file}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFile(WhileParser.FileContext ctx);
	/**
	 * Visit a parse tree produced by {@link WhileParser#line}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLine(WhileParser.LineContext ctx);
	/**
	 * Visit a parse tree produced by {@link WhileParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(WhileParser.ExprContext ctx);
}