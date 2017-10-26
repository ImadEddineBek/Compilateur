import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Scanner {
    private List<String> source;
    private char currentChar;
    private Token currentToken;

    public Scanner(String path) {
        try {
            source = Files.readAllLines(Paths.get(path));
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
        return null;
    }

    public ArrayList<Token> scan() {
        for (String line : source) {
            char[] chars = line.toCharArray();
            for (char aChar : chars) {

            }
        }
    }
}
