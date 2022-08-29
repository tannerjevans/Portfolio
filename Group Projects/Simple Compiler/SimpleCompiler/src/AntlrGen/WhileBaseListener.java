// Generated from C:/Users/tannerjevans/Documents/GitHub Files/cs454_project01/cs_454_project01/SimpleCompiler/src\While.g4 by ANTLR 4.9.1
package AntlrGen;

import DataStructureGeneration.ASTBuilder;
import DataStructureGeneration.ASTNode;
import DataStructureGeneration.Tree;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashMap;
import java.util.Map;

/**
 * This class provides an empty implementation of {@link WhileListener},
 * which can be extended to create a listener which only needs to handle a subset
 * of the available methods.
 */
public class WhileBaseListener implements WhileListener {
	String leaves = "";
	String root = null;
	Map<String, ASTNode<String>> nodeMap = new HashMap<String, ASTNode<String>>();
	Map<String, Integer> dupCount = new HashMap<String, Integer>();
	Map< Integer,String> visitOrder = new HashMap<Integer,String>();
	int count = 0;
	public String[] operators = {":=", "+", "-", "*"};
	int state;
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterFile(WhileParser.FileContext ctx) {
		//System.out.println("entered file");

	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitFile(WhileParser.FileContext ctx) {
		//System.out.println("exiting parser" + ctx.getChildCount());
		//System.out.println(ctx.getChild(0).getChild(0).getChild(1).getChildCount());

		//System.out.println("Count" + count);
		nodeMap = Tree.fixChildNames(nodeMap, visitOrder);
		ASTBuilder.buildDotAST(nodeMap,visitOrder);
		Tree.Search(nodeMap,root);


	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterLine(WhileParser.LineContext ctx) {

	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitLine(WhileParser.LineContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterExpr(WhileParser.ExprContext ctx) {
		//System.out.println(ctx.toString());
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitExpr(WhileParser.ExprContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterEveryRule(ParserRuleContext ctx) {

		String childset = ctx.children.toString();
		String parent = "[]";
		if(ctx.getParent() != null) {
			parent = ctx.getParent().toString();
		}
		String name = ctx.getRuleContext().toString();
		if(childset.contains(",")) {
			childset = childset.substring(1,childset.length()-1);
		}
		String testingstring = ",";
		String [] split = childset.split(testingstring);


		if(!dupCount.containsKey(name)){
			dupCount.put(name, 1);
		} else {
			dupCount.put(name, dupCount.get(name) + 1);

		}

		if(root == null){
			root = name+dupCount.get(name).toString();
		}

		nodeMap.put(name+dupCount.get(name).toString(), new ASTNode<>(name+dupCount.get(name).toString(), split,
				parent+dupCount.get(parent).toString()));
		visitOrder.put(count, name+dupCount.get(name).toString());
		count++;

	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitEveryRule(ParserRuleContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void visitTerminal(TerminalNode node) {
		//System.out.println(node.getText());
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void visitErrorNode(ErrorNode node) { }
}
