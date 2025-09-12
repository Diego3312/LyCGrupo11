package lyc.compiler;

import java_cup.runtime.Symbol;
import lyc.compiler.ParserSym;
import lyc.compiler.model.*;
import static lyc.compiler.constants.Constants.*;

%%

%public
%class Lexer
%unicode
%cup
%line
%column
%throws CompilerException
%eofval{
  return symbol(ParserSym.EOF);
%eofval}


%{
  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }
%}


LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
Identation =  [ \t\f]
DataType = "Int" | "Float" | "String"
MathOp = "+" | "*" | "-" | "/"
CompOp = "<" | ">" | "<=" | ">=" | "=="
Assig = "="
Numeral = "#"
QuoteMark = \"
OpenCBracket = "{"
CloseCBracket = "}"
Comma = ","
OpenBracket = "("
CloseBracket = ")"
Letter = [a-zA-Z]
Digit = [0-9]


WhiteSpace = {LineTerminator} | {Identation}
Identifier = {Letter} ({Letter}|{Digit})*
IntegerConstant = {Digit}+
FloatConstant = {Digit}+.{Digit}* | {Digit}*.{Digit}+ | {Digit}+.{Digit}+
StringConstant = {QuoteMark}{Letter}{0,50}{QuoteMark}
VarList = {VarList}{Comma}{Identifier} | {Identifier} | {WhiteSpace}
Factor = {Digit}+ | {Identifier}
Expression = {Factor} | {OpenBracket}{Factor}{MathOP}{Factor}{CloseBracket}
Comment = \#\+ ([^\#]|(\#[^+]))* \+\#
%%


/* keywords */

<YYINITIAL> {

  /*Temas Comunes*/
  "while"                                  { return symbol(ParserSym.WHILE); }     
  "if"                                     { return symbol(ParserSym.IF); }
  "else"                                   { return symbol(ParserSym.ELSE); }       
  "read"                                   { return symbol(ParserSym.READ); }   
  "write"                                  { return symbol(ParserSym.WRITE); }             
  "init"                                   { return symbol(ParserSym.INIT); }
  "AND"                                    { return symbol(ParserSym.AND); }
  "OR"                                     { return symbol(ParserSym.OR); }
  "NOT"                                    { return symbol(ParserSym.NOT); }

  /* Constants */
  {IntegerConstant}                        { return symbol(ParserSym.INTEGER_CONSTANT, yytext()); }
  {FloatConstant}                          { return symbol(ParserSym.FLOAT_CONSTANT, yytext());   }
  {StringConstant}                         { return symbol(ParserSym.STRING_CONSTANT, yytext());  }

  /* identifiers */
  {Identifier}                             { return symbol(ParserSym.IDENTIFIER, yytext()); }

  /* operators */
  {MathOp}                                  { return symbol(ParserSym.MATH_OP); }
  {OpenBracket}                             { return symbol(ParserSym.OPEN_BRACKET); }
  {CloseBracket}                            { return symbol(ParserSym.CLOSE_BRACKET); }
  {CompOp}                                  { return symbol(ParserSym.COMP_OP); }
  {OpenBracket}                             { return symbol(ParserSym.OPEN_BRACKET); }
  {CloseBracket}                            { return symbol(ParserSym.CLOSE_BRACKET); }
  {Comma}                                   { return symbol(ParserSym.COMMA); }
  {Numeral}                                 { return symbol(ParserSym.NUMERAL); }
  {DataType}                                { return symbol(ParserSym.DATA_TYPE, yytext()); }
  {OpenCBracket}                            { return symbol(ParserSym.OPEN_CBRACKET); }
  {CloseCBracket}                           { return symbol(ParserSym.CLOSE_CBRACKET); }

  /* whitespace */
  {Comment}                      { /* ignore */ } 
  {WhiteSpace}                   { /* ignore */ }
}


/* error fallback */
[^]                              { throw new UnknownCharacterException(yytext()); }
