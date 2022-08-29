// Generated from C:/Users/magid/Github Test/simple_compiler/cs_454_project01/SimpleCompiler/src\While.g4 by ANTLR 4.9.2
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class WhileParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, NUMBER=3, END=4, NOT=5, AND=6, OR=7, SKIPPER=8, IF=9, 
		THEN=10, ELSE=11, FI=12, WHILE=13, DO=14, OD=15, TRUE=16, FALSE=17, ASSIGN=18, 
		TIMES=19, PLUS=20, MINUS=21, EQUAL=22, LESSTHAN=23, LESSOREQUAL=24, GREATERTHAN=25, 
		GREATEROREQUAL=26, VARIABLE=27, LETTERS=28, COMMENT=29, BLOCKCOMMENT=30, 
		WHITESPACE=31;
	public static final int
		RULE_file = 0, RULE_line = 1, RULE_expr = 2;
	private static String[] makeRuleNames() {
		return new String[] {
			"file", "line", "expr"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", null, "';'", "'not'", "'and'", "'or'", "'skip'", 
			"'if'", "'then'", "'else'", "'fi'", "'while'", "'do'", "'od'", "'true'", 
			"'false'", "':='", "'*'", "'+'", "'-'", "'='", "'<'", "'<='", "'>'", 
			"'>='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, "NUMBER", "END", "NOT", "AND", "OR", "SKIPPER", "IF", 
			"THEN", "ELSE", "FI", "WHILE", "DO", "OD", "TRUE", "FALSE", "ASSIGN", 
			"TIMES", "PLUS", "MINUS", "EQUAL", "LESSTHAN", "LESSOREQUAL", "GREATERTHAN", 
			"GREATEROREQUAL", "VARIABLE", "LETTERS", "COMMENT", "BLOCKCOMMENT", "WHITESPACE"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "While.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public WhileParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class FileContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(WhileParser.EOF, 0); }
		public List<LineContext> line() {
			return getRuleContexts(LineContext.class);
		}
		public LineContext line(int i) {
			return getRuleContext(LineContext.class,i);
		}
		public FileContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_file; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WhileListener ) ((WhileListener)listener).enterFile(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WhileListener ) ((WhileListener)listener).exitFile(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WhileVisitor ) return ((WhileVisitor<? extends T>)visitor).visitFile(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FileContext file() throws RecognitionException {
		FileContext _localctx = new FileContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_file);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(9);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << NUMBER) | (1L << NOT) | (1L << SKIPPER) | (1L << IF) | (1L << WHILE) | (1L << TRUE) | (1L << FALSE) | (1L << VARIABLE))) != 0)) {
				{
				{
				setState(6);
				line();
				}
				}
				setState(11);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(12);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LineContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode END() { return getToken(WhileParser.END, 0); }
		public LineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_line; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WhileListener ) ((WhileListener)listener).enterLine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WhileListener ) ((WhileListener)listener).exitLine(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WhileVisitor ) return ((WhileVisitor<? extends T>)visitor).visitLine(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LineContext line() throws RecognitionException {
		LineContext _localctx = new LineContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_line);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(14);
			expr(0);
			setState(16);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==END) {
				{
				setState(15);
				match(END);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode NOT() { return getToken(WhileParser.NOT, 0); }
		public TerminalNode SKIPPER() { return getToken(WhileParser.SKIPPER, 0); }
		public TerminalNode WHILE() { return getToken(WhileParser.WHILE, 0); }
		public TerminalNode DO() { return getToken(WhileParser.DO, 0); }
		public TerminalNode OD() { return getToken(WhileParser.OD, 0); }
		public List<LineContext> line() {
			return getRuleContexts(LineContext.class);
		}
		public LineContext line(int i) {
			return getRuleContext(LineContext.class,i);
		}
		public TerminalNode IF() { return getToken(WhileParser.IF, 0); }
		public TerminalNode THEN() { return getToken(WhileParser.THEN, 0); }
		public TerminalNode FI() { return getToken(WhileParser.FI, 0); }
		public TerminalNode ELSE() { return getToken(WhileParser.ELSE, 0); }
		public TerminalNode TRUE() { return getToken(WhileParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(WhileParser.FALSE, 0); }
		public TerminalNode NUMBER() { return getToken(WhileParser.NUMBER, 0); }
		public TerminalNode VARIABLE() { return getToken(WhileParser.VARIABLE, 0); }
		public TerminalNode TIMES() { return getToken(WhileParser.TIMES, 0); }
		public TerminalNode PLUS() { return getToken(WhileParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(WhileParser.MINUS, 0); }
		public TerminalNode AND() { return getToken(WhileParser.AND, 0); }
		public TerminalNode OR() { return getToken(WhileParser.OR, 0); }
		public TerminalNode EQUAL() { return getToken(WhileParser.EQUAL, 0); }
		public TerminalNode LESSTHAN() { return getToken(WhileParser.LESSTHAN, 0); }
		public TerminalNode LESSOREQUAL() { return getToken(WhileParser.LESSOREQUAL, 0); }
		public TerminalNode GREATERTHAN() { return getToken(WhileParser.GREATERTHAN, 0); }
		public TerminalNode GREATEROREQUAL() { return getToken(WhileParser.GREATEROREQUAL, 0); }
		public TerminalNode ASSIGN() { return getToken(WhileParser.ASSIGN, 0); }
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WhileListener ) ((WhileListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WhileListener ) ((WhileListener)listener).exitExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WhileVisitor ) return ((WhileVisitor<? extends T>)visitor).visitExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 4;
		enterRecursionRule(_localctx, 4, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(67);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
				{
				setState(19);
				match(T__0);
				setState(20);
				expr(0);
				setState(21);
				match(T__1);
				}
				break;
			case NOT:
				{
				setState(23);
				match(NOT);
				setState(24);
				match(T__0);
				setState(25);
				expr(0);
				setState(26);
				match(T__1);
				}
				break;
			case SKIPPER:
				{
				setState(28);
				match(SKIPPER);
				}
				break;
			case WHILE:
				{
				setState(29);
				match(WHILE);
				setState(30);
				expr(0);
				setState(31);
				match(DO);
				setState(36);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << NUMBER) | (1L << NOT) | (1L << SKIPPER) | (1L << IF) | (1L << WHILE) | (1L << TRUE) | (1L << FALSE) | (1L << VARIABLE))) != 0)) {
					{
					setState(34);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
					case 1:
						{
						setState(32);
						expr(0);
						}
						break;
					case 2:
						{
						setState(33);
						line();
						}
						break;
					}
					}
					setState(38);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(39);
				match(OD);
				}
				break;
			case IF:
				{
				setState(41);
				match(IF);
				setState(42);
				expr(0);
				setState(43);
				match(THEN);
				setState(48);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << NUMBER) | (1L << NOT) | (1L << SKIPPER) | (1L << IF) | (1L << WHILE) | (1L << TRUE) | (1L << FALSE) | (1L << VARIABLE))) != 0)) {
					{
					setState(46);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
					case 1:
						{
						setState(44);
						expr(0);
						}
						break;
					case 2:
						{
						setState(45);
						line();
						}
						break;
					}
					}
					setState(50);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(59);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELSE) {
					{
					setState(51);
					match(ELSE);
					setState(56);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << NUMBER) | (1L << NOT) | (1L << SKIPPER) | (1L << IF) | (1L << WHILE) | (1L << TRUE) | (1L << FALSE) | (1L << VARIABLE))) != 0)) {
						{
						setState(54);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
						case 1:
							{
							setState(52);
							expr(0);
							}
							break;
						case 2:
							{
							setState(53);
							line();
							}
							break;
						}
						}
						setState(58);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(61);
				match(FI);
				}
				break;
			case TRUE:
				{
				setState(63);
				match(TRUE);
				}
				break;
			case FALSE:
				{
				setState(64);
				match(FALSE);
				}
				break;
			case NUMBER:
				{
				setState(65);
				match(NUMBER);
				}
				break;
			case VARIABLE:
				{
				setState(66);
				match(VARIABLE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(104);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(102);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
					case 1:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(69);
						if (!(precpred(_ctx, 18))) throw new FailedPredicateException(this, "precpred(_ctx, 18)");
						setState(70);
						match(TIMES);
						setState(71);
						expr(19);
						}
						break;
					case 2:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(72);
						if (!(precpred(_ctx, 17))) throw new FailedPredicateException(this, "precpred(_ctx, 17)");
						setState(73);
						match(PLUS);
						setState(74);
						expr(18);
						}
						break;
					case 3:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(75);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(76);
						match(MINUS);
						setState(77);
						expr(17);
						}
						break;
					case 4:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(78);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(79);
						match(AND);
						setState(80);
						expr(16);
						}
						break;
					case 5:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(81);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(82);
						match(OR);
						setState(83);
						expr(15);
						}
						break;
					case 6:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(84);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(85);
						match(EQUAL);
						setState(86);
						expr(14);
						}
						break;
					case 7:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(87);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(88);
						match(LESSTHAN);
						setState(89);
						expr(13);
						}
						break;
					case 8:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(90);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(91);
						match(LESSOREQUAL);
						setState(92);
						expr(12);
						}
						break;
					case 9:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(93);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(94);
						match(GREATERTHAN);
						setState(95);
						expr(11);
						}
						break;
					case 10:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(96);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(97);
						match(GREATEROREQUAL);
						setState(98);
						expr(10);
						}
						break;
					case 11:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(99);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(100);
						match(ASSIGN);
						setState(101);
						expr(9);
						}
						break;
					}
					} 
				}
				setState(106);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 2:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 18);
		case 1:
			return precpred(_ctx, 17);
		case 2:
			return precpred(_ctx, 16);
		case 3:
			return precpred(_ctx, 15);
		case 4:
			return precpred(_ctx, 14);
		case 5:
			return precpred(_ctx, 13);
		case 6:
			return precpred(_ctx, 12);
		case 7:
			return precpred(_ctx, 11);
		case 8:
			return precpred(_ctx, 10);
		case 9:
			return precpred(_ctx, 9);
		case 10:
			return precpred(_ctx, 8);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3!n\4\2\t\2\4\3\t\3"+
		"\4\4\t\4\3\2\7\2\n\n\2\f\2\16\2\r\13\2\3\2\3\2\3\3\3\3\5\3\23\n\3\3\4"+
		"\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\7\4%\n\4"+
		"\f\4\16\4(\13\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\7\4\61\n\4\f\4\16\4\64\13"+
		"\4\3\4\3\4\3\4\7\49\n\4\f\4\16\4<\13\4\5\4>\n\4\3\4\3\4\3\4\3\4\3\4\3"+
		"\4\5\4F\n\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\7\4i\n\4\f\4\16\4l\13\4\3\4\2\3\6\5\2\4\6\2\2\2\u0086\2\13\3\2\2"+
		"\2\4\20\3\2\2\2\6E\3\2\2\2\b\n\5\4\3\2\t\b\3\2\2\2\n\r\3\2\2\2\13\t\3"+
		"\2\2\2\13\f\3\2\2\2\f\16\3\2\2\2\r\13\3\2\2\2\16\17\7\2\2\3\17\3\3\2\2"+
		"\2\20\22\5\6\4\2\21\23\7\6\2\2\22\21\3\2\2\2\22\23\3\2\2\2\23\5\3\2\2"+
		"\2\24\25\b\4\1\2\25\26\7\3\2\2\26\27\5\6\4\2\27\30\7\4\2\2\30F\3\2\2\2"+
		"\31\32\7\7\2\2\32\33\7\3\2\2\33\34\5\6\4\2\34\35\7\4\2\2\35F\3\2\2\2\36"+
		"F\7\n\2\2\37 \7\17\2\2 !\5\6\4\2!&\7\20\2\2\"%\5\6\4\2#%\5\4\3\2$\"\3"+
		"\2\2\2$#\3\2\2\2%(\3\2\2\2&$\3\2\2\2&\'\3\2\2\2\')\3\2\2\2(&\3\2\2\2)"+
		"*\7\21\2\2*F\3\2\2\2+,\7\13\2\2,-\5\6\4\2-\62\7\f\2\2.\61\5\6\4\2/\61"+
		"\5\4\3\2\60.\3\2\2\2\60/\3\2\2\2\61\64\3\2\2\2\62\60\3\2\2\2\62\63\3\2"+
		"\2\2\63=\3\2\2\2\64\62\3\2\2\2\65:\7\r\2\2\669\5\6\4\2\679\5\4\3\28\66"+
		"\3\2\2\28\67\3\2\2\29<\3\2\2\2:8\3\2\2\2:;\3\2\2\2;>\3\2\2\2<:\3\2\2\2"+
		"=\65\3\2\2\2=>\3\2\2\2>?\3\2\2\2?@\7\16\2\2@F\3\2\2\2AF\7\22\2\2BF\7\23"+
		"\2\2CF\7\5\2\2DF\7\35\2\2E\24\3\2\2\2E\31\3\2\2\2E\36\3\2\2\2E\37\3\2"+
		"\2\2E+\3\2\2\2EA\3\2\2\2EB\3\2\2\2EC\3\2\2\2ED\3\2\2\2Fj\3\2\2\2GH\f\24"+
		"\2\2HI\7\25\2\2Ii\5\6\4\25JK\f\23\2\2KL\7\26\2\2Li\5\6\4\24MN\f\22\2\2"+
		"NO\7\27\2\2Oi\5\6\4\23PQ\f\21\2\2QR\7\b\2\2Ri\5\6\4\22ST\f\20\2\2TU\7"+
		"\t\2\2Ui\5\6\4\21VW\f\17\2\2WX\7\30\2\2Xi\5\6\4\20YZ\f\16\2\2Z[\7\31\2"+
		"\2[i\5\6\4\17\\]\f\r\2\2]^\7\32\2\2^i\5\6\4\16_`\f\f\2\2`a\7\33\2\2ai"+
		"\5\6\4\rbc\f\13\2\2cd\7\34\2\2di\5\6\4\fef\f\n\2\2fg\7\24\2\2gi\5\6\4"+
		"\13hG\3\2\2\2hJ\3\2\2\2hM\3\2\2\2hP\3\2\2\2hS\3\2\2\2hV\3\2\2\2hY\3\2"+
		"\2\2h\\\3\2\2\2h_\3\2\2\2hb\3\2\2\2he\3\2\2\2il\3\2\2\2jh\3\2\2\2jk\3"+
		"\2\2\2k\7\3\2\2\2lj\3\2\2\2\16\13\22$&\60\628:=Ehj";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}