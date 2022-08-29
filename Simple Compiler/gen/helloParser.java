// Generated from C:/Users/tannerjevans/Documents/GitHub Files/cs454_project01/cs_454_project01/testing/src\SimpleCompiler.g4 by ANTLR 4.9.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class helloParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

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

	public helloParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class FileContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(helloParser.EOF, 0); }
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
			if ( listener instanceof helloListener ) ((helloListener)listener).enterFile(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof helloListener ) ((helloListener)listener).exitFile(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof helloVisitor ) return ((helloVisitor<? extends T>)visitor).visitFile(this);
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
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << NUMBER) | (1L << NOT) | (1L << IF) | (1L << WHILE) | (1L << TRUE) | (1L << FALSE) | (1L << VARIABLE))) != 0)) {
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
		public TerminalNode END() { return getToken(helloParser.END, 0); }
		public LineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_line; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof helloListener ) ((helloListener)listener).enterLine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof helloListener ) ((helloListener)listener).exitLine(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof helloVisitor ) return ((helloVisitor<? extends T>)visitor).visitLine(this);
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
		public TerminalNode NOT() { return getToken(helloParser.NOT, 0); }
		public TerminalNode WHILE() { return getToken(helloParser.WHILE, 0); }
		public TerminalNode DO() { return getToken(helloParser.DO, 0); }
		public TerminalNode OD() { return getToken(helloParser.OD, 0); }
		public List<LineContext> line() {
			return getRuleContexts(LineContext.class);
		}
		public LineContext line(int i) {
			return getRuleContext(LineContext.class,i);
		}
		public TerminalNode IF() { return getToken(helloParser.IF, 0); }
		public TerminalNode THEN() { return getToken(helloParser.THEN, 0); }
		public TerminalNode FI() { return getToken(helloParser.FI, 0); }
		public TerminalNode ELSE() { return getToken(helloParser.ELSE, 0); }
		public TerminalNode TRUE() { return getToken(helloParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(helloParser.FALSE, 0); }
		public TerminalNode NUMBER() { return getToken(helloParser.NUMBER, 0); }
		public TerminalNode VARIABLE() { return getToken(helloParser.VARIABLE, 0); }
		public TerminalNode TIMES() { return getToken(helloParser.TIMES, 0); }
		public TerminalNode PLUS() { return getToken(helloParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(helloParser.MINUS, 0); }
		public TerminalNode AND() { return getToken(helloParser.AND, 0); }
		public TerminalNode OR() { return getToken(helloParser.OR, 0); }
		public TerminalNode EQUAL() { return getToken(helloParser.EQUAL, 0); }
		public TerminalNode LESSTHAN() { return getToken(helloParser.LESSTHAN, 0); }
		public TerminalNode LESSOREQUAL() { return getToken(helloParser.LESSOREQUAL, 0); }
		public TerminalNode GREATERTHAN() { return getToken(helloParser.GREATERTHAN, 0); }
		public TerminalNode GREATEROREQUAL() { return getToken(helloParser.GREATEROREQUAL, 0); }
		public TerminalNode ASSIGN() { return getToken(helloParser.ASSIGN, 0); }
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof helloListener ) ((helloListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof helloListener ) ((helloListener)listener).exitExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof helloVisitor ) return ((helloVisitor<? extends T>)visitor).visitExpr(this);
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
			setState(66);
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
			case WHILE:
				{
				setState(28);
				match(WHILE);
				setState(29);
				expr(0);
				setState(30);
				match(DO);
				setState(35);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << NUMBER) | (1L << NOT) | (1L << IF) | (1L << WHILE) | (1L << TRUE) | (1L << FALSE) | (1L << VARIABLE))) != 0)) {
					{
					setState(33);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
					case 1:
						{
						setState(31);
						expr(0);
						}
						break;
					case 2:
						{
						setState(32);
						line();
						}
						break;
					}
					}
					setState(37);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(38);
				match(OD);
				}
				break;
			case IF:
				{
				setState(40);
				match(IF);
				setState(41);
				expr(0);
				setState(42);
				match(THEN);
				setState(47);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << NUMBER) | (1L << NOT) | (1L << IF) | (1L << WHILE) | (1L << TRUE) | (1L << FALSE) | (1L << VARIABLE))) != 0)) {
					{
					setState(45);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
					case 1:
						{
						setState(43);
						expr(0);
						}
						break;
					case 2:
						{
						setState(44);
						line();
						}
						break;
					}
					}
					setState(49);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(58);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELSE) {
					{
					setState(50);
					match(ELSE);
					setState(55);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << NUMBER) | (1L << NOT) | (1L << IF) | (1L << WHILE) | (1L << TRUE) | (1L << FALSE) | (1L << VARIABLE))) != 0)) {
						{
						setState(53);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
						case 1:
							{
							setState(51);
							expr(0);
							}
							break;
						case 2:
							{
							setState(52);
							line();
							}
							break;
						}
						}
						setState(57);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(60);
				match(FI);
				}
				break;
			case TRUE:
				{
				setState(62);
				match(TRUE);
				}
				break;
			case FALSE:
				{
				setState(63);
				match(FALSE);
				}
				break;
			case NUMBER:
				{
				setState(64);
				match(NUMBER);
				}
				break;
			case VARIABLE:
				{
				setState(65);
				match(VARIABLE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(103);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(101);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
					case 1:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(68);
						if (!(precpred(_ctx, 17))) throw new FailedPredicateException(this, "precpred(_ctx, 17)");
						setState(69);
						match(TIMES);
						setState(70);
						expr(18);
						}
						break;
					case 2:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(71);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(72);
						match(PLUS);
						setState(73);
						expr(17);
						}
						break;
					case 3:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(74);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(75);
						match(MINUS);
						setState(76);
						expr(16);
						}
						break;
					case 4:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(77);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(78);
						match(AND);
						setState(79);
						expr(15);
						}
						break;
					case 5:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(80);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(81);
						match(OR);
						setState(82);
						expr(14);
						}
						break;
					case 6:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(83);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(84);
						match(EQUAL);
						setState(85);
						expr(13);
						}
						break;
					case 7:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(86);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(87);
						match(LESSTHAN);
						setState(88);
						expr(12);
						}
						break;
					case 8:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(89);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(90);
						match(LESSOREQUAL);
						setState(91);
						expr(11);
						}
						break;
					case 9:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(92);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(93);
						match(GREATERTHAN);
						setState(94);
						expr(10);
						}
						break;
					case 10:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(95);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(96);
						match(GREATEROREQUAL);
						setState(97);
						expr(9);
						}
						break;
					case 11:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(98);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(99);
						match(ASSIGN);
						setState(100);
						expr(8);
						}
						break;
					}
					} 
				}
				setState(105);
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
			return precpred(_ctx, 17);
		case 1:
			return precpred(_ctx, 16);
		case 2:
			return precpred(_ctx, 15);
		case 3:
			return precpred(_ctx, 14);
		case 4:
			return precpred(_ctx, 13);
		case 5:
			return precpred(_ctx, 12);
		case 6:
			return precpred(_ctx, 11);
		case 7:
			return precpred(_ctx, 10);
		case 8:
			return precpred(_ctx, 9);
		case 9:
			return precpred(_ctx, 8);
		case 10:
			return precpred(_ctx, 7);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3!m\4\2\t\2\4\3\t\3"+
		"\4\4\t\4\3\2\7\2\n\n\2\f\2\16\2\r\13\2\3\2\3\2\3\3\3\3\5\3\23\n\3\3\4"+
		"\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\7\4$\n\4\f\4"+
		"\16\4\'\13\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\7\4\60\n\4\f\4\16\4\63\13\4\3"+
		"\4\3\4\3\4\7\48\n\4\f\4\16\4;\13\4\5\4=\n\4\3\4\3\4\3\4\3\4\3\4\3\4\5"+
		"\4E\n\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\7\4h\n\4\f\4\16\4k\13\4\3\4\2\3\6\5\2\4\6\2\2\2\u0084\2\13\3\2\2\2\4"+
		"\20\3\2\2\2\6D\3\2\2\2\b\n\5\4\3\2\t\b\3\2\2\2\n\r\3\2\2\2\13\t\3\2\2"+
		"\2\13\f\3\2\2\2\f\16\3\2\2\2\r\13\3\2\2\2\16\17\7\2\2\3\17\3\3\2\2\2\20"+
		"\22\5\6\4\2\21\23\7\6\2\2\22\21\3\2\2\2\22\23\3\2\2\2\23\5\3\2\2\2\24"+
		"\25\b\4\1\2\25\26\7\3\2\2\26\27\5\6\4\2\27\30\7\4\2\2\30E\3\2\2\2\31\32"+
		"\7\7\2\2\32\33\7\3\2\2\33\34\5\6\4\2\34\35\7\4\2\2\35E\3\2\2\2\36\37\7"+
		"\17\2\2\37 \5\6\4\2 %\7\20\2\2!$\5\6\4\2\"$\5\4\3\2#!\3\2\2\2#\"\3\2\2"+
		"\2$\'\3\2\2\2%#\3\2\2\2%&\3\2\2\2&(\3\2\2\2\'%\3\2\2\2()\7\21\2\2)E\3"+
		"\2\2\2*+\7\13\2\2+,\5\6\4\2,\61\7\f\2\2-\60\5\6\4\2.\60\5\4\3\2/-\3\2"+
		"\2\2/.\3\2\2\2\60\63\3\2\2\2\61/\3\2\2\2\61\62\3\2\2\2\62<\3\2\2\2\63"+
		"\61\3\2\2\2\649\7\r\2\2\658\5\6\4\2\668\5\4\3\2\67\65\3\2\2\2\67\66\3"+
		"\2\2\28;\3\2\2\29\67\3\2\2\29:\3\2\2\2:=\3\2\2\2;9\3\2\2\2<\64\3\2\2\2"+
		"<=\3\2\2\2=>\3\2\2\2>?\7\16\2\2?E\3\2\2\2@E\7\22\2\2AE\7\23\2\2BE\7\5"+
		"\2\2CE\7\35\2\2D\24\3\2\2\2D\31\3\2\2\2D\36\3\2\2\2D*\3\2\2\2D@\3\2\2"+
		"\2DA\3\2\2\2DB\3\2\2\2DC\3\2\2\2Ei\3\2\2\2FG\f\23\2\2GH\7\25\2\2Hh\5\6"+
		"\4\24IJ\f\22\2\2JK\7\26\2\2Kh\5\6\4\23LM\f\21\2\2MN\7\27\2\2Nh\5\6\4\22"+
		"OP\f\20\2\2PQ\7\b\2\2Qh\5\6\4\21RS\f\17\2\2ST\7\t\2\2Th\5\6\4\20UV\f\16"+
		"\2\2VW\7\30\2\2Wh\5\6\4\17XY\f\r\2\2YZ\7\31\2\2Zh\5\6\4\16[\\\f\f\2\2"+
		"\\]\7\32\2\2]h\5\6\4\r^_\f\13\2\2_`\7\33\2\2`h\5\6\4\fab\f\n\2\2bc\7\34"+
		"\2\2ch\5\6\4\13de\f\t\2\2ef\7\24\2\2fh\5\6\4\ngF\3\2\2\2gI\3\2\2\2gL\3"+
		"\2\2\2gO\3\2\2\2gR\3\2\2\2gU\3\2\2\2gX\3\2\2\2g[\3\2\2\2g^\3\2\2\2ga\3"+
		"\2\2\2gd\3\2\2\2hk\3\2\2\2ig\3\2\2\2ij\3\2\2\2j\7\3\2\2\2ki\3\2\2\2\16"+
		"\13\22#%/\61\679<Dgi";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}