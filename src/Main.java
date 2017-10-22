import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        ArrayList<Token> scan = LexicalAnalyzer.scan(Paths.get("C:\\Users\\lenovo\\IdeaProjects\\Compilateur\\src\\code"));
        scan.forEach(System.out::println);
    }
}
