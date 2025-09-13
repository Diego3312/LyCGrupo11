package lyc.compiler.factories;

import java.io.Reader;

import lyc.compiler.Parser;

public final class ParserFactory {

    private ParserFactory(){}

    public static Parser create(String input) {
        return new Parser(LexerFactory.create(input));
    }

    public static Parser create(Reader reader) {
        return new Parser(LexerFactory.create(reader));
    }


}
