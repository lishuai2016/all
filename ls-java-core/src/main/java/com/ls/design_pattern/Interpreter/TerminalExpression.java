package com.ls.design_pattern.Interpreter;

import java.util.StringTokenizer;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-04
 */
public class TerminalExpression extends Expression {
    private String literal;
    public TerminalExpression(String literal) {
        this.literal = literal;
    }
    @Override
    public boolean interpret(String str) {
        StringTokenizer st = new StringTokenizer(str);
        while (st.hasMoreTokens()) {
            String test = st.nextToken();
            if (test.equals(literal)) {
                return true;
            }
        }
        return false;
    }
}
