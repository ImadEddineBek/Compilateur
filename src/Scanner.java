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
                if ()

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
