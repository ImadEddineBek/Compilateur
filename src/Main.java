import java.io.IOException;

class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner("C:\\Users\\lenovo\\IdeaProjects\\Compilateur\\src\\code");
        Token t;
        int i = 0;
        while ((t = scanner.getNextToken()) != null && i < 50) {
            System.out.println(t);
            scanner.reInitialize();
            i++;
        }
    }
}
