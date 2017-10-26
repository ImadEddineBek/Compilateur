import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Scanner {
    private String text;
    private int position;
    private List<String> source;
    private char currentChar;
    private StringBuilder currentToken;
    private int state;
    private int line;
    private int linePosition;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public Scanner(String path) {
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
        } catch (IOException e) {
            source = new ArrayList<>();
        }
    }

    public boolean isLetter(char c) {
        return (c <= 'z' && c >= 'a') || (c <= 'Z' && c >= 'A');
    }

    public boolean isDigit(char c) {
        return (c <= '9' && c >= '0');
    }

    public boolean isOperator(char c) {
        return (c == '+' || c == '-' || c == '*' || c == '/' ||

                c == '=' || c == '<' || c == '>' || c == '\\' ||

                c == '&' || c == '@' || c == '%' || c == '^' ||

                c == '?');
    }

    public Token getNextToken() {
        char c = text.charAt(position);
        switch (state) {
            case 0:
                if (isaNotImportant(c)) {
                    state = 0;
                    position++;
                    if (c == '\n') {
                        line++;
                        linePosition = 0;
                    }
                    return getNextToken();
                }
                if (isLetter(c)) {
                    state = 1;
                    currentToken = new StringBuilder();
                    return getNextToken();
                }
                if (isDigit(c)) {
                    state = 3;
                    currentToken = new StringBuilder();
                    return getNextToken();
                }
                if (isOperator(c)) {
                    state = 4;
                    currentToken = new StringBuilder();
                    return getNextToken();
                }
                if (c == '"') {
                    state = 2;
                    position++;
                    currentToken = new StringBuilder();
                    return getNextToken();
                }
            case 1:
                if (isDigit(c) || isLetter(c) || c == '_') {
                    state = 1;
                    currentToken.append(c);
                    position++;
                    return getNextToken();
                } else if (isKeyWord(currentToken.toString())) return new Token(Type.mot_cle, currentToken.toString());
                else return new Token(Type.id, currentToken.toString());
            case 2:
                if (c != '"') {
                    currentToken.append(c);
                    position++;
                    state = 2;
                    return getNextToken();
                }
                return new Token(Type.string_Literal, currentToken.toString());
            case 3:
                if (isDigit(c)) {
                    currentToken.append(c);
                    position++;
                    return getNextToken();
                } else if (c == '.') {
                    currentToken.append(c);
                    position++;
                    state = 5;
                    return getNextToken();
                } else if (!isLetter(c)) return new Token(Type.integerLiteral, currentToken.toString());
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
                if (c != '<' && c != '>') return new Token(Type.symbol, c + "");
                currentToken.append(c);
                position++;
                if (c == '<') {
                    state = 7;
                    return getNextToken();
                }
                if (c == '>') {
                    state = 8;
                    return getNextToken();
                }

            case 5:
                if (isDigit(c)) {
                    position++;
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
                    return null;
                }

            case 6:
                if (isDigit(c)) {
                    position++;
                    currentToken.append(c);
                    state = 6;
                    return getNextToken();
                }
                if (!isLetter(c)) return new Token(Type.integerLiteral, currentToken.toString());
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
                    currentToken.append(c);
                    return new Token(Type.symbol, currentToken.toString());
                }
                return new Token(Type.symbol, currentToken.toString());
            case 8:
                if (c == '=') {
                    position++;
                    currentToken.append(c);
                    return new Token(Type.symbol, currentToken.toString());
                }
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
