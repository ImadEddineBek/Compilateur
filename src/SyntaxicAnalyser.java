public class SyntaxicAnalyser {

    public SyntaxicAnalyser(LexicalAnalyzer scanner) {
        this.scanner = scanner;
    }

    private Token currentToken;
    private LexicalAnalyzer scanner;

    private void acceptIt() {
        currentToken = scanner.getNextToken();
    }

    public void parse() throws Exception {
        acceptIt(); //Get the first token
        parseProgram();
    }

    private void accept(Token expectedToken, boolean checkValue) throws Exception {
        if (expectedToken.type.equals(currentToken.type)) {
            if (checkValue) {
                if (expectedToken.value.equals(currentToken.value)) {
                    scanner.reInitialize();
                    currentToken = scanner.getNextToken();

                } else {
                    System.out.println("expected : " + expectedToken + " ,found : " + currentToken);
                    throw new Exception();
                }
            } else {
                scanner.reInitialize();
                currentToken = scanner.getNextToken();

            }
        } else {
            System.out.println("expected : " + expectedToken + " ,found : " + currentToken);
            throw new Exception();
        }
    }

    private void parseProgram() throws Exception {
        parseSingleCommand();
    }

    private void parseCommand() throws Exception {
        parseSingleCommand();
        parseCommandRight();
    }

    private void parseCommandRight() throws Exception {
        if (currentToken.type.equals(Type.symbol) && currentToken.value.equals(";")) {
            accept(new Token(Type.symbol, ";"), true);
            parseSingleCommand();
            parseCommandRight();
        }
    }

    private void parseExpression() throws Exception {
        parsePrimaryExpression();
        parseExpressionRight();
    }

    private boolean isOperator(char c) {
        return (c == '+' || c == '-' || c == '*' || c == '/' ||
                c == '=' || c == '<' || c == '>');
    }

    private void parseExpressionRight() throws Exception {
        if (currentToken.type.equals(Type.symbol) && isOperator(currentToken.value.charAt(0))) {
            accept(new Token(Type.symbol, ";"), false);
            parsePrimaryExpression();
            parseExpressionRight();
        }
    }

    private void parsePrimaryExpression() throws Exception {
        switch (inFirstOf("primExp")) {
            case 1:
                accept(new Token(Type.integerLiteral, "1"), false);
                break;
            case 2:
                accept(new Token(Type.id, ""), false);
                break;
            case 3:
                parseOpenB();
                parseExpression();
                parseCloseB();
                break;
            default:
                System.out.println("expected an integer , an Identifier or ( ; found : " + currentToken);
                throw new Exception();
        }
    }

    private void parseDeclaration() throws Exception {
        parseSingleDeclaration();
        parseDeclarationRight();
    }

    private void parseDeclarationRight() throws Exception {
        if (currentToken.type.equals(Type.symbol) && currentToken.value.equals(";")) {
            accept(new Token(Type.symbol, ";"), true);
            parseSingleDeclaration();
            parseDeclarationRight();
        }
    }

    private void parseSingleDeclaration() throws Exception {
        switch (inFirstOf("singleDec")) {
            case 1:
                accept(new Token(Type.mot_cle, "var"), true);
                parseIdentifier();
                accept(new Token(Type.symbol, "~"), true);
                parseExpression();
                break;
            case 2:
                accept(new Token(Type.mot_cle, "const"), true);
                parseIdentifier();
                accept(new Token(Type.symbol, ":"), true);
                accept(new Token(Type.integerLiteral, ""), false);
                break;
            default:
                System.out.println("expected an const or var ; found : " + currentToken);
                throw new Exception();
        }
    }

    private void parseSingleCommand() throws Exception {
        int singleCom = inFirstOf("singleCom");
        if (singleCom != -10) {
            switch (singleCom) {
                case 1:
                    parseIdentifier();
                    if (!currentToken.type.equals(Type.symbol)) {
                        System.out.println("expected a symbol ( / =");
                        return;
                    }
                    if (currentToken.value.equals("(")) {
                        parseOpenB();
                        parseExpression();
                        parseCloseB();
                        return;
                    }
                    if (currentToken.value.equals("=")) {
                        parseEquals();
                        parseExpression();
                        return;
                    }
                    System.out.println("expected a symbol ( / =");
                    return;
                case 2:
                    parseBegin();
                    parseCommand();
                    parseEnd();
                    return;
                case 3:
                    parseIf();
                    parseExpression();
                    parseThen();
                    parseSingleCommand();
                    parseElse();
                    parseSingleCommand();
                    return;
                case 4:
                    parseWhile();
                    parseExpression();
                    parseDo();
                    parseSingleCommand();
                    return;
                case 5:
                    parseLet();
                    parseDeclaration();
                    parseIn();
                    parseSingleCommand();
                    return;
                default:
                    System.out.println("expected an Identifier , IF , WHILE , LET , BEGIN");
            }
        } else {
            System.out.println("expected a single command");
        }

    }

    private void parseIf() throws Exception {
        accept(new Token(Type.mot_cle, "if"), true);
    }

    private void parseThen() throws Exception {
        accept(new Token(Type.mot_cle, "then"), true);
    }

    private void parseElse() throws Exception {
        accept(new Token(Type.mot_cle, "else"), true);
    }

    private void parseWhile() throws Exception {
        accept(new Token(Type.mot_cle, "while"), true);
    }

    private void parseDo() throws Exception {
        accept(new Token(Type.mot_cle, "do"), true);
    }

    private void parseIn() throws Exception {
        accept(new Token(Type.mot_cle, "in"), true);
    }

    private void parseLet() throws Exception {
        accept(new Token(Type.mot_cle, "let"), true);
    }

    private void parseEnd() throws Exception {
        accept(new Token(Type.mot_cle, "end"), true);
    }

    private void parseBegin() throws Exception {
        accept(new Token(Type.mot_cle, "begin"), true);
    }

    private void parseEquals() throws Exception {
        accept(new Token(Type.symbol, "="), true);
    }

    private void parseCloseB() throws Exception {
        accept(new Token(Type.symbol, ")"), true);
    }

    private void parseOpenB() throws Exception {
        accept(new Token(Type.symbol, "("), true);
    }

    private void parseIdentifier() throws Exception {
        accept(new Token(Type.id, ""), false);
    }

    private int inFirstOf(String nonTerminal) {
        switch (nonTerminal) {
            case "singleDec":
                if (currentToken.type.equals(Type.mot_cle) && currentToken.value.equals("var")) return 1;
                if (currentToken.type.equals(Type.mot_cle) && currentToken.value.equals("const")) return 2;
                return -10;
            case "Dec":
                if (currentToken.type.equals(Type.mot_cle) && currentToken.value.equals("var")) return 1;
                if (currentToken.type.equals(Type.mot_cle) && currentToken.value.equals("const")) return 2;
                return -10;
            case "DecRight":
                if (currentToken.type.equals(Type.symbol) && currentToken.value.equals(";")) return 1;
                return -10;
            case "Program":
                if (currentToken.type.equals(Type.id)) return 1;
                if (!currentToken.type.equals(Type.mot_cle)) return -10;
                if (currentToken.value.equals("begin")) return 2;
                if (currentToken.value.equals("if")) return 3;
                if (currentToken.value.equals("while")) return 4;
                if (currentToken.value.equals("let")) return 5;
            case "primExp":
                if (currentToken.type.equals(Type.integerLiteral)) return 1;
                if (currentToken.type.equals(Type.id)) return 2;
                if (currentToken.type.equals(Type.symbol) && currentToken.value.equals("(")) return 3;
                return -10;
            case "Exp":
                if (currentToken.type.equals(Type.integerLiteral)) return 1;
                if (currentToken.type.equals(Type.id)) return 2;
                if (currentToken.type.equals(Type.symbol) && currentToken.value.equals("(")) return 3;
                return -10;
            case "ExpRight":
                return currentToken.type.equals(Type.symbol) ? 1 : -10;
            case "singleCom":
                if (currentToken.type.equals(Type.id)) return 1;
                if (!currentToken.type.equals(Type.mot_cle)) return -10;
                if (currentToken.value.equals("begin")) return 2;
                if (currentToken.value.equals("if")) return 3;
                if (currentToken.value.equals("while")) return 4;
                if (currentToken.value.equals("let")) return 5;
            case "ComRight":
                if (currentToken.type.equals(Type.symbol) && currentToken.value.equals(";")) return 1;
                return -10;
            case "Com":
                if (currentToken.type.equals(Type.id)) return 1;
                if (!currentToken.type.equals(Type.mot_cle)) return -10;
                if (currentToken.value.equals("begin")) return 2;
                if (currentToken.value.equals("if")) return 3;
                if (currentToken.value.equals("while")) return 4;
                if (currentToken.value.equals("let")) return 5;
            default:
                return -10;
        }
    }
}
