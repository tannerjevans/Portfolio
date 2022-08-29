grammar While;

file: line* EOF;

line: expr END?;

expr:
     '(' expr ')'
    |  NOT '(' expr ')'
    | expr TIMES expr
    | expr PLUS expr
    | expr MINUS expr
    | expr AND expr
    | expr OR expr
    | expr EQUAL expr
    | expr LESSTHAN expr
    | expr LESSOREQUAL expr
    | expr GREATERTHAN expr
    | expr GREATEROREQUAL expr
    | expr ASSIGN expr
    | SKIPPER
    | WHILE expr DO (expr | line)* OD
    | IF expr THEN (expr | line)* (ELSE (expr | line)*)? FI
    | TRUE
    | FALSE
    | NUMBER
    | VARIABLE
    ;


NUMBER: [0-9]+;

END: ';';
NOT: 'not';
AND: 'and';
OR: 'or';
SKIPPER: 'skip';
IF: 'if';
THEN: 'then';
ELSE: 'else';
FI: 'fi';
WHILE: 'while';
DO: 'do';
OD: 'od';

TRUE: 'true';
FALSE: 'false';

ASSIGN: ':=';

TIMES: '*';
PLUS: '+';
MINUS: '-';
EQUAL: '=';
LESSTHAN: '<';
LESSOREQUAL: '<=';
GREATERTHAN: '>';
GREATEROREQUAL: '>=';

fragment LOWERCASE: [a-z];
fragment UPPERCASE: [A-Z];

VARIABLE: ([a-z] | [A-Z]) ([a-z] | [A-Z] | [0-9] | '_')*;

LETTERS: (UPPERCASE | LOWERCASE)+;
COMMENT: '--' ~( '\r' | '\n' )* ('\n' | '\r' | EOF) -> skip;
BLOCKCOMMENT: '{-' (BLOCKCOMMENT|.)*? '-}' -> skip;
WHITESPACE: [ \t\n\r] -> skip;

