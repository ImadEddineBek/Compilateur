import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LexicalAnalyzer {
    public ArrayList<Id> ids = new ArrayList<>();

    public static ArrayList<Token> scan(Path path) throws IOException {
        List<String> strings = Files.readAllLines(path);
        ArrayList<Token> tokens = new ArrayList<>();
        for (String string : strings) {
            StringBuilder sb = new StringBuilder();
            char[] chars = string.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char aChar = chars[i];
                if (aChar == ' ' && sb.length() == 0)
                    continue;
                else if (aChar == ' ' && sb.length() != 0) {
                    Token detect = detect(sb.toString());
                    sb = new StringBuilder();
                    if (detect.getValue().equals("!")) break;
                    tokens.add(detect);
                } else {
                    sb.append(aChar);
                }
            }
            Token detect = detect(sb.toString());
            sb = new StringBuilder();
            if (!detect.getValue().equals("!"))
                tokens.add(detect);
        }
        return tokens;
    }


    private static Token detect(String s) {
        switch (s) {
            case ":":
            case ",":
            case ";":
            case ":=":
            case "-":
            case "+":
            case "*":
            case "(":
            case ")":
            case "/":
            case "<":
            case ">":
            case "=":
            case "!":
            case "\\":
                return new Token(Type.symbol, s);
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
                return new Token(Type.mot_cle, s);
            default:
                if (isId(s.charAt(0))) return new Token(Type.id, s);
                else {
                    try {
                        double d = Double.parseDouble(s);
                        return new Token(Type.literal, s);
                    } catch (NumberFormatException e) {
                        return new Token(null, s);
                    }
                }
        }
    }

    private static boolean isId(char c) {
        return (c <= 'z' && c >= 'a') || (c <= 'Z' && c >= 'A');
    }
}
