// Generated from C:/Users/tannerjevans/Documents/GitHub Files/cs454_project01/cs_454_project01/testing/src\SimpleCompiler.g4 by ANTLR 4.9.1
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class helloLexer extends Lexer {
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
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "NUMBER", "END", "NOT", "AND", "OR", "SKIPPER", "IF", 
			"THEN", "ELSE", "FI", "WHILE", "DO", "OD", "TRUE", "FALSE", "ASSIGN", 
			"TIMES", "PLUS", "MINUS", "EQUAL", "LESSTHAN", "LESSOREQUAL", "GREATERTHAN", 
			"GREATEROREQUAL", "LOWERCASE", "UPPERCASE", "VARIABLE", "LETTERS", "COMMENT", 
			"BLOCKCOMMENT", "WHITESPACE"
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


	public helloLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "While.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2!\u00d0\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\3\2\3\2\3\3\3\3\3\4\6\4K\n\4\r\4\16\4L\3\5\3\5\3\6\3\6\3\6"+
		"\3\6\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\13"+
		"\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\16\3"+
		"\16\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3"+
		"\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\24\3\24\3\25\3\25\3\26\3"+
		"\26\3\27\3\27\3\30\3\30\3\31\3\31\3\31\3\32\3\32\3\33\3\33\3\33\3\34\3"+
		"\34\3\35\3\35\3\36\5\36\u00a2\n\36\3\36\7\36\u00a5\n\36\f\36\16\36\u00a8"+
		"\13\36\3\37\3\37\6\37\u00ac\n\37\r\37\16\37\u00ad\3 \3 \3 \3 \7 \u00b4"+
		"\n \f \16 \u00b7\13 \3 \5 \u00ba\n \3 \3 \3!\3!\3!\3!\3!\7!\u00c3\n!\f"+
		"!\16!\u00c6\13!\3!\3!\3!\3!\3!\3\"\3\"\3\"\3\"\3\u00c4\2#\3\3\5\4\7\5"+
		"\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23"+
		"%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\29\2;\35=\36?\37A C!\3"+
		"\2\n\3\2\62;\3\2c|\3\2C\\\4\2C\\c|\6\2\62;C\\aac|\4\2\f\f\17\17\4\3\f"+
		"\f\17\17\5\2\13\f\17\17\"\"\2\u00d4\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2"+
		"\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23"+
		"\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2"+
		"\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2"+
		"\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3"+
		"\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\3E\3\2\2"+
		"\2\5G\3\2\2\2\7J\3\2\2\2\tN\3\2\2\2\13P\3\2\2\2\rT\3\2\2\2\17X\3\2\2\2"+
		"\21[\3\2\2\2\23`\3\2\2\2\25c\3\2\2\2\27h\3\2\2\2\31m\3\2\2\2\33p\3\2\2"+
		"\2\35v\3\2\2\2\37y\3\2\2\2!|\3\2\2\2#\u0081\3\2\2\2%\u0087\3\2\2\2\'\u008a"+
		"\3\2\2\2)\u008c\3\2\2\2+\u008e\3\2\2\2-\u0090\3\2\2\2/\u0092\3\2\2\2\61"+
		"\u0094\3\2\2\2\63\u0097\3\2\2\2\65\u0099\3\2\2\2\67\u009c\3\2\2\29\u009e"+
		"\3\2\2\2;\u00a1\3\2\2\2=\u00ab\3\2\2\2?\u00af\3\2\2\2A\u00bd\3\2\2\2C"+
		"\u00cc\3\2\2\2EF\7*\2\2F\4\3\2\2\2GH\7+\2\2H\6\3\2\2\2IK\t\2\2\2JI\3\2"+
		"\2\2KL\3\2\2\2LJ\3\2\2\2LM\3\2\2\2M\b\3\2\2\2NO\7=\2\2O\n\3\2\2\2PQ\7"+
		"p\2\2QR\7q\2\2RS\7v\2\2S\f\3\2\2\2TU\7c\2\2UV\7p\2\2VW\7f\2\2W\16\3\2"+
		"\2\2XY\7q\2\2YZ\7t\2\2Z\20\3\2\2\2[\\\7u\2\2\\]\7m\2\2]^\7k\2\2^_\7r\2"+
		"\2_\22\3\2\2\2`a\7k\2\2ab\7h\2\2b\24\3\2\2\2cd\7v\2\2de\7j\2\2ef\7g\2"+
		"\2fg\7p\2\2g\26\3\2\2\2hi\7g\2\2ij\7n\2\2jk\7u\2\2kl\7g\2\2l\30\3\2\2"+
		"\2mn\7h\2\2no\7k\2\2o\32\3\2\2\2pq\7y\2\2qr\7j\2\2rs\7k\2\2st\7n\2\2t"+
		"u\7g\2\2u\34\3\2\2\2vw\7f\2\2wx\7q\2\2x\36\3\2\2\2yz\7q\2\2z{\7f\2\2{"+
		" \3\2\2\2|}\7v\2\2}~\7t\2\2~\177\7w\2\2\177\u0080\7g\2\2\u0080\"\3\2\2"+
		"\2\u0081\u0082\7h\2\2\u0082\u0083\7c\2\2\u0083\u0084\7n\2\2\u0084\u0085"+
		"\7u\2\2\u0085\u0086\7g\2\2\u0086$\3\2\2\2\u0087\u0088\7<\2\2\u0088\u0089"+
		"\7?\2\2\u0089&\3\2\2\2\u008a\u008b\7,\2\2\u008b(\3\2\2\2\u008c\u008d\7"+
		"-\2\2\u008d*\3\2\2\2\u008e\u008f\7/\2\2\u008f,\3\2\2\2\u0090\u0091\7?"+
		"\2\2\u0091.\3\2\2\2\u0092\u0093\7>\2\2\u0093\60\3\2\2\2\u0094\u0095\7"+
		">\2\2\u0095\u0096\7?\2\2\u0096\62\3\2\2\2\u0097\u0098\7@\2\2\u0098\64"+
		"\3\2\2\2\u0099\u009a\7@\2\2\u009a\u009b\7?\2\2\u009b\66\3\2\2\2\u009c"+
		"\u009d\t\3\2\2\u009d8\3\2\2\2\u009e\u009f\t\4\2\2\u009f:\3\2\2\2\u00a0"+
		"\u00a2\t\5\2\2\u00a1\u00a0\3\2\2\2\u00a2\u00a6\3\2\2\2\u00a3\u00a5\t\6"+
		"\2\2\u00a4\u00a3\3\2\2\2\u00a5\u00a8\3\2\2\2\u00a6\u00a4\3\2\2\2\u00a6"+
		"\u00a7\3\2\2\2\u00a7<\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a9\u00ac\59\35\2"+
		"\u00aa\u00ac\5\67\34\2\u00ab\u00a9\3\2\2\2\u00ab\u00aa\3\2\2\2\u00ac\u00ad"+
		"\3\2\2\2\u00ad\u00ab\3\2\2\2\u00ad\u00ae\3\2\2\2\u00ae>\3\2\2\2\u00af"+
		"\u00b0\7/\2\2\u00b0\u00b1\7/\2\2\u00b1\u00b5\3\2\2\2\u00b2\u00b4\n\7\2"+
		"\2\u00b3\u00b2\3\2\2\2\u00b4\u00b7\3\2\2\2\u00b5\u00b3\3\2\2\2\u00b5\u00b6"+
		"\3\2\2\2\u00b6\u00b9\3\2\2\2\u00b7\u00b5\3\2\2\2\u00b8\u00ba\t\b\2\2\u00b9"+
		"\u00b8\3\2\2\2\u00ba\u00bb\3\2\2\2\u00bb\u00bc\b \2\2\u00bc@\3\2\2\2\u00bd"+
		"\u00be\7}\2\2\u00be\u00bf\7/\2\2\u00bf\u00c4\3\2\2\2\u00c0\u00c3\5A!\2"+
		"\u00c1\u00c3\13\2\2\2\u00c2\u00c0\3\2\2\2\u00c2\u00c1\3\2\2\2\u00c3\u00c6"+
		"\3\2\2\2\u00c4\u00c5\3\2\2\2\u00c4\u00c2\3\2\2\2\u00c5\u00c7\3\2\2\2\u00c6"+
		"\u00c4\3\2\2\2\u00c7\u00c8\7/\2\2\u00c8\u00c9\7\177\2\2\u00c9\u00ca\3"+
		"\2\2\2\u00ca\u00cb\b!\2\2\u00cbB\3\2\2\2\u00cc\u00cd\t\t\2\2\u00cd\u00ce"+
		"\3\2\2\2\u00ce\u00cf\b\"\2\2\u00cfD\3\2\2\2\r\2L\u00a1\u00a4\u00a6\u00ab"+
		"\u00ad\u00b5\u00b9\u00c2\u00c4\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}