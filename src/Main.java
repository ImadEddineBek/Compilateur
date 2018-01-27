import java.io.IOException;

class Main {
    public static void main(String[] args) throws IOException {
        // code correcte Lexicalement et syntaxiquement
        System.out.println("//////////////aucune faute////////////////");
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer("C:\\Users\\lenovo\\IdeaProjects\\Compilateur\\src\\code");
        Token t;

//        while ((t = lexicalAnalyzer.getNextToken()) != null) {
//            //corriger les erreurs dans le code fichier pour avoir un resultat different ou ajouter d'autre erreurs
//            System.out.println(t);
//            lexicalAnalyzer.reInitialize();
//
//        }
        SyntaxicAnalyser syntaxicAnalyser = new SyntaxicAnalyser(lexicalAnalyzer);
        try {
            syntaxicAnalyser.parse();
        } catch (Exception e) {
        }
        System.out.println("//////////////faute lexical 5.a////////////////");
        // code erroné lexicalement erreur 3.55a
        lexicalAnalyzer = new LexicalAnalyzer("C:\\Users\\lenovo\\IdeaProjects\\Compilateur\\src\\code1");
        t = null;

        while ((t = lexicalAnalyzer.getNextToken()) != null) {
            //corriger les erreurs dans le code fichier pour avoir un resultat different ou ajouter d'autre erreurs
            System.out.println(t);
            lexicalAnalyzer.reInitialize();
        }
        // code correcte Lexicalement et syntaxiquement
        System.out.println("//////////////correcte lexicalement erone systaxiquement ////////////////");
        lexicalAnalyzer = new LexicalAnalyzer("C:\\Users\\lenovo\\IdeaProjects\\Compilateur\\src\\code2");

//        while ((t = lexicalAnalyzer.getNextToken()) != null) {
//            //corriger les erreurs dans le code fichier pour avoir un resultat different ou ajouter d'autre erreurs
//            System.out.println(t);
//            lexicalAnalyzer.reInitialize();
//        }
        syntaxicAnalyser = new SyntaxicAnalyser(lexicalAnalyzer);
        try {
            syntaxicAnalyser.parse();
        } catch (Exception e) {
        }
    }
}
