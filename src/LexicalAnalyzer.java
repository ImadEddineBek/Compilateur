import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class LexicalAnalyzer {
    private String text;
    private int position;
    private List<String> source;
    private StringBuilder currentToken;
    private int state;
    private int line;
    private int linePosition;
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";

    public void reInitialize() {
        currentToken = new StringBuilder();
    }

    public LexicalAnalyzer(String path) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            source = Files.readAllLines(Paths.get(path));
            for (String s : source) {
                stringBuilder.append(s).append("\n");
            }
            text = stringBuilder.toString();
            position = 0;
            state = 0;
            line = 0;
            currentToken = new StringBuilder();
        } catch (IOException e) {
            source = new ArrayList<>();
        }
    }

    private boolean isLetter(char c) {
        return (c <= 'z' && c >= 'a') || (c <= 'Z' && c >= 'A');
    }

    private boolean isDigit(char c) {
        return (c <= '9' && c >= '0');
    }

    private boolean isOperator(char c) {
        return (c == '+' || c == '-' || c == '*' || c == '/' ||

                c == '=' || c == '<' || c == '>' || c == '\\' ||

                c == '&' || c == '@' || c == '%' || c == '^' ||

                c == '?' || c == '(' || c == ')' || c == ':' ||

                c == ',' || c == '~' || c == ';');
    }

    public Token getNextToken() {
        if (position >= text.length()) return null;
        char c = text.charAt(position);
        switch (state) {
            case 0:
                if (isaNotImportant(c)) {
                    state = 0;
                    linePosition++;
                    position++;
                    if (c == '\n') {
                        line++;
                        linePosition = 0;
                    }
                    return getNextToken();
                }
                if (isLetter(c)) {
                    state = 1;

                    return getNextToken();
                }
                if (isDigit(c)) {
                    state = 3;
                    return getNextToken();
                }
                if (isOperator(c)) {
                    state = 4;
                    return getNextToken();
                }
                if (c == '"') {
                    state = 2;
                    position++;
                    linePosition++;

                    return getNextToken();
                }
                if (c == '!') {
                    String s = source.get(line);
                    position += s.length() - linePosition + 1;
                    linePosition = 0;
                    line++;
                    return getNextToken();
                }
            case 1:
                if (isDigit(c) || isLetter(c) || c == '_') {
                    state = 1;
                    currentToken.append(c);
                    position++;
                    linePosition++;
                    return getNextToken();
                } else if (isKeyWord(currentToken.toString())) {
                    state = 0;

                    return new Token(Type.mot_cle, currentToken.toString());
                } else {
                    state = 0;

                    return new Token(Type.id, currentToken.toString());
                }
            case 2:
                if (c != '"') {
                    currentToken.append(c);
                    position++;
                    linePosition++;
                    state = 2;
                    return getNextToken();
                }
                state = 0;
                position++;
                linePosition++;
                return new Token(Type.string_Literal, currentToken.toString());
            case 3:
                if (isDigit(c)) {
                    currentToken.append(c);
                    position++;
                    linePosition++;
                    return getNextToken();
                } else if (c == '.') {
                    currentToken.append(c);
                    position++;
                    linePosition++;
                    state = 5;
                    return getNextToken();
                } else if (!isLetter(c)) {
                    state = 0;

                    return new Token(Type.integerLiteral, currentToken.toString());
                }
                else {
                    String s = source.get(line);
                    char[] chars = s.toCharArray();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("this number is followed by a letter");
                    for (int i = 0; i < chars.length; i++) {
                        if (i == linePosition) {
                            stringBuilder.append(ANSI_RED);
                            stringBuilder.append(chars[i]);
                            stringBuilder.append(ANSI_RESET);
                        } else stringBuilder.append(chars[i]);
                    }
                    System.out.println(stringBuilder);

                    return null;
                }
            case 4:
                if (c != '<' && c != '>') {
                    state = 0;
                    position++;
                    linePosition++;
                    return new Token(Type.symbol, c + "");
                }
                currentToken.append(c);
                position++;
                linePosition++;
                if (c == '<') {
                    state = 7;
                    return getNextToken();
                }
                // if (c == '>') {
                state = 8;
                return getNextToken();


            case 5:
                if (isDigit(c)) {
                    position++;
                    linePosition++;
                    currentToken.append(c);
                    state = 6;
                    return getNextToken();
                } else {
                    String s = source.get(line);
                    char[] chars = s.toCharArray();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("this . is not followed by a digit");
                    for (int i = 0; i < chars.length; i++) {
                        if (i == linePosition) {
                            stringBuilder.append(ANSI_RED);
                            stringBuilder.append(chars[i]);
                            stringBuilder.append(ANSI_RESET);
                        } else stringBuilder.append(chars[i]);
                    }
                    System.out.println(stringBuilder);
                    state = 0;

                    return null;
                }

            case 6:
                if (isDigit(c)) {
                    position++;
                    linePosition++;
                    currentToken.append(c);
                    state = 6;
                    return getNextToken();
                }
                if (!isLetter(c)) {
                    state = 0;

                    return new Token(Type.realLiteral, currentToken.toString());
                }
                else {
                    String s = source.get(line);
                    char[] chars = s.toCharArray();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("this number is followed by a letter");
                    for (int i = 0; i < chars.length; i++) {
                        if (i == linePosition) {
                            stringBuilder.append(ANSI_RED);
                            stringBuilder.append(chars[i]);
                            stringBuilder.append(ANSI_RESET);
                        } else stringBuilder.append(chars[i]);
                    }
                    System.out.println(stringBuilder);

                    return null;
                }
            case 7:
                if (c == '=' || c == '>') {
                    position++;
                    linePosition++;
                    currentToken.append(c);
                    state = 0;

                    return new Token(Type.symbol, currentToken.toString());
                }
                state = 0;

                return new Token(Type.symbol, currentToken.toString());
            case 8:
                if (c == '=') {
                    position++;
                    linePosition++;
                    currentToken.append(c);
                    state = 0;

                    return new Token(Type.symbol, currentToken.toString());
                }
                state = 0;

                return new Token(Type.symbol, currentToken.toString());

        }
        return null;
    }

    private boolean isaNotImportant(char c) {
        return c == ' ' || c == '\t' || c == '\n';
    }

    private boolean isKeyWord(String s) {
        switch (s) {
            case "begin":
            case "const":
            case "do":
            case "else":
            case "end":
            case "if":
            case "in":
            case "let":
            case "while":
            case "then":
            case "var":
            case "function":
            case "int":
            case "string":
                return true;
        }
        return false;
    }
}
