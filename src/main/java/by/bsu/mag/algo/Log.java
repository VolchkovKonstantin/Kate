package by.bsu.mag.algo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Log {

    private final String situation;
    private final List<List<Integer>> NCup;
    private final Map<Integer, List<Integer>> result;
    private final BigDecimal LB;

    public Log(String situation, List<List<Integer>> NCup, Map<Integer, List<Integer>> result, BigDecimal LB) {
        this.situation = situation;
        this.NCup = new ArrayList<>();
        for (int i = 0; i < NCup.size(); i++) {
            this.NCup.add(new ArrayList<>(NCup.get(i)));
        }
        this.result = new HashMap<>(result);
        this.LB = LB;
    }

    public String getSituation() {
        return situation;
    }

    public List<List<Integer>> getNCup() {
        return NCup;
    }

    public Map<Integer, List<Integer>> getResult() {
        return result;
    }

    public BigDecimal getLB() {
        return LB;
    }

    @Override
    public String toString() {
        return "Log{" +
                "situation='" + situation + '\'' +
                ", NCup=" + NCup +
                ", result=" + result +
                ", LB=" + LB +
                '}';
    }
}
