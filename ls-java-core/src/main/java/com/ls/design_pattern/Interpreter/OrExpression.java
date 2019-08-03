package com.ls.design_pattern.Interpreter;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-04
 */
public class OrExpression extends Expression {
    private Expression expression1 = null;
    private Expression expression2 = null;

    public OrExpression(Expression expression1, Expression expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    public boolean interpret(String str) {
        return expression1.interpret(str) || expression2.interpret(str);
    }
}
