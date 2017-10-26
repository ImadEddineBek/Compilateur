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
    private Token currentToken;
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
                if (isaNotImportant(c)) state = 0;
                return getNextToken();
            case 1:

        }
        return null;
    }

    private boolean isaNotImportant(char c) {
        return c == ' ' || c == '\t' || c == '\n';
    }
}
