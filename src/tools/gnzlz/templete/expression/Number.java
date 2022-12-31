package tools.gnzlz.templete.expression;

import java.util.Stack;

public class Number {

    private Stack<String> operators = new Stack<String>();
    private Stack<Object> values = new Stack<Object>();

    public void addValue(double value){
        values.push(value);
    }

    public void addValue(Number value){
        values.push(value);
    }

    public void addOperator(String operator){
        operators.push(operator);
    }

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

    private double pop() {
        Object object = values.pop();
        if(object instanceof Number) {
            return ((Number) object).result();
        }
        return (double) object;
    }
}
