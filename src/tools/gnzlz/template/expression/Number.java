package tools.gnzlz.template.expression;

import java.util.Stack;

public class Number {

    /**
     * operators
     */
    private Stack<String> operators = new Stack<String>();

    /**
     * values
     */
    private Stack<Object> values = new Stack<Object>();

    /**
     * pop
     */
    private double pop() {
        Object object = values.pop();
        if(object instanceof Number) {
            return ((Number) object).result();
        }
        return (double) object;
    }

    /**
     * addValue
     * @param value v
     */
    public void addValue(double value){
        values.push(value);
    }

    /**
     * addValue
     * @param value v
     */
    public void addValue(Number value){
        values.push(value);
    }

    /**
     * addOperator
     * @param operator o
     */
    public void addOperator(String operator){
        operators.push(operator);
    }

    /**
     * result
     */
    public double result() {
        double result = 0;
        boolean flag = true;
        while (!operators.isEmpty()){
            String op = operators.pop();
            if(flag){
                flag = false;
                result = pop();
            }
            if (op.equals("+")) {
                result += pop();
            } else if (op.equals("-")) {
                result -= pop();
            } else if (op.equals("*")) {
                result *= pop();
            }else if (op.equals("/")) {
                result /= pop();
            }
        }
        return flag ? pop() : result;
    }
}
