package com.example.calculator.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Safe arithmetic expression evaluator using a recursive-descent parser.
 * No eval / ScriptEngine is used.
 *
 * Grammar:
 *   expression := term (('+' | '-') term)*
 *   term       := factor (('*' | '/') factor)*
 *   factor     := ('+' | '-') factor | primary
 *   primary    := NUMBER | '(' expression ')'
 */
public class ExpressionParser {

    public static class CalcException extends RuntimeException {
        public CalcException(String message) {
            super(message);
        }
    }

    private static class Token {
        String type;
        double value;
        Token(String type, double value) {
            this.type = type;
            this.value = value;
        }
        Token(String type) {
            this.type = type;
        }
    }

    private final List<Token> tokens;
    private int pos;

    private ExpressionParser(List<Token> tokens) {
        this.tokens = tokens;
        this.pos = 0;
    }

    private static List<Token> tokenize(String expr) {
        List<Token> tokens = new ArrayList<>();
        int i = 0;
        int n = expr.length();
        while (i < n) {
            char ch = expr.charAt(i);
            if (Character.isWhitespace(ch)) {
                i++;
            } else if (Character.isDigit(ch) || ch == '.') {
                int j = i;
                int dotCount = 0;
                while (j < n && (Character.isDigit(expr.charAt(j)) || expr.charAt(j) == '.')) {
                    if (expr.charAt(j) == '.') dotCount++;
                    j++;
                }
                if (dotCount > 1) {
                    throw new CalcException("Invalid number: multiple decimal points");
                }
                tokens.add(new Token("NUM", Double.parseDouble(expr.substring(i, j))));
                i = j;
            } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '(' || ch == ')') {
                tokens.add(new Token(String.valueOf(ch)));
                i++;
            } else {
                throw new CalcException("Invalid character: '" + ch + "'");
            }
        }
        return tokens;
    }

    private Token peek() {
        return pos < tokens.size() ? tokens.get(pos) : null;
    }

    private Token next() {
        return tokens.get(pos++);
    }

    private double parseExpression() {
        double value = parseTerm();
        while (peek() != null && ("+".equals(peek().type) || "-".equals(peek().type))) {
            String op = next().type;
            double right = parseTerm();
            value = op.equals("+") ? value + right : value - right;
        }
        return value;
    }

    private double parseTerm() {
        double value = parseFactor();
        while (peek() != null && ("*".equals(peek().type) || "/".equals(peek().type))) {
            String op = next().type;
            double right = parseFactor();
            if (op.equals("*")) {
                value *= right;
            } else {
                if (right == 0) throw new CalcException("Division by zero");
                value /= right;
            }
        }
        return value;
    }

    private double parseFactor() {
        Token t = peek();
        if (t != null && "+".equals(t.type)) {
            next();
            return parseFactor();
        }
        if (t != null && "-".equals(t.type)) {
            next();
            return -parseFactor();
        }
        return parsePrimary();
    }

    private double parsePrimary() {
        Token t = peek();
        if (t == null) throw new CalcException("Unexpected end of expression");
        if ("NUM".equals(t.type)) {
            next();
            return t.value;
        }
        if ("(".equals(t.type)) {
            next();
            double value = parseExpression();
            if (peek() == null || !")".equals(peek().type)) {
                throw new CalcException("Missing closing parenthesis");
            }
            next();
            return value;
        }
        throw new CalcException("Unexpected token: '" + t.type + "'");
    }

    public static Object calculate(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new CalcException("Empty expression");
        }
        List<Token> tokens = tokenize(expression);
        if (tokens.isEmpty()) {
            throw new CalcException("Empty expression");
        }
        ExpressionParser parser = new ExpressionParser(tokens);
        double result = parser.parseExpression();
        if (parser.pos != tokens.size()) {
            throw new CalcException("Unexpected token: '" + parser.peek().type + "'");
        }
        return normalize(result);
    }

    private static Object normalize(double result) {
        if (Double.isInfinite(result) || Double.isNaN(result)) {
            return result;
        }
        if (Math.abs(result - Math.rint(result)) < 1e-9) {
            return (long) result;
        }
        java.math.BigDecimal bd = new java.math.BigDecimal(Double.toString(result))
                .setScale(10, java.math.RoundingMode.HALF_UP)
                .stripTrailingZeros();
        return bd.doubleValue();
    }
}
