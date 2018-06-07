package sample;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Algo {

    private static int N, M, MAX_K;
    private static BigDecimal LB;
    private static Integer[] P, S, K;
    private static BigDecimal[] Z;
    static BigDecimal[][] T;
    private static List<Integer> Nv;
    private static List<Integer> Ns;
    private static List<Integer> Nall;
    private static List<Integer> Jv;
    private static List<Integer> Jall;
    private static List<List<Integer>> N_CUP;
    private static Map<Integer, List<Integer>> result;
    static List<Log> history;


    public Algo(Scanner scanner) {
        N = scanner.nextInt();
        M = scanner.nextInt();
        MAX_K = scanner.nextInt();

        P = new Integer[N];
        S = new Integer[M];

        for (int i = 0; i < N; i++) {
            P[i] = scanner.nextInt();
        }

        for (int j = 0; j < M; j++) {
            S[j] = scanner.nextInt();
        }

        Arrays.sort(P, Collections.reverseOrder());
        Arrays.sort(S, Collections.reverseOrder());

        T = new BigDecimal[N][M];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                T[i][j] = divide(P[i], S[j]);
            }
        }
    }

    private static void init(boolean isFirst) {
        history = new ArrayList<>();
        Z = new BigDecimal[M];
        K = new Integer[M];
        for (int j = 0; j < M; j++) {
            K[j] = 0;
            Z[j] = BigDecimal.ZERO;
        }
        if (isFirst) {
            calculateLB();
        }
        N_CUP = new ArrayList<>();
        for (int i = 0; i < M; i++) {
            N_CUP.add(new ArrayList<>());
        }
        Ns = new ArrayList<>();
        Nall = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            Ns.add(i);
            Nall.add(i);
        }
        Jall = new ArrayList<>();
        for (int j = 0; j < M; j++) {
            Jall.add(j);
        }

        Nv = new ArrayList<>();
        Jv = new ArrayList<>();
        result = new HashMap<>();
    }

    private static BigDecimal sumP(int start, int end) {
        BigDecimal sumP = BigDecimal.ZERO;
        for (int i = start; i < end; i++) {
            sumP = sumP.add(new BigDecimal(P[i]));
        }
        return sumP;
    }

    private static BigDecimal sumS(int start, int end) {
        BigDecimal sumS = BigDecimal.ZERO;
        for (int j = start; j < end; j++) {
            sumS = sumS.add(new BigDecimal(S[j]));
        }
        return sumS;
    }

    private static BigDecimal divide(Integer a, Integer b) {
        return divide(new BigDecimal(a), new BigDecimal(b));
    }

    private static BigDecimal divide(BigDecimal a, BigDecimal b) {
        return a.divide(b, 2, BigDecimal.ROUND_HALF_UP);
    }

    private static void calculateLB() {
        BigDecimal sumP = sumP(0, N);
        BigDecimal sumQ = sumS(0, M);
        BigDecimal element1 = divide(sumP, sumQ);
        BigDecimal element2 = divide(P[0], S[0]);
        BigDecimal element3 = divide(P[0] + P[1], S[0]);
        BigDecimal element4 = divide(P[1], S[1]);
        LB = element1.max(element2.max(element3.min(element4)));
    }

    private static boolean lessOrEquals(BigDecimal a, BigDecimal b) {
        int result = a.compareTo(b);
        return result == -1 || result == 0;
    }

    private static boolean more(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) > 0;
    }

    private static boolean checkLB(int i, int j) {
        return lessOrEquals(Z[j].add(divide(P[i], S[j])), LB);
    }

    private static List<Integer> filter_more_LB(int j0) {
        List<Integer> temp = new ArrayList<>(Ns);
        return temp.stream().filter(i -> more(divide(P[i], S[j0]), LB)).collect(Collectors.toList());
    }

    private static void log(String situation) {
        history.add(new Log(situation, N_CUP, result, LB));
    }

    private static void firstStep() {
        int k = 0, j = 0;
        ArrayList<Integer> currentDevice = new ArrayList<>();
        while (j < M && k < Ns.size()) {
            int i = Ns.get(k);
            if (Jv.contains(j)) {
                j++;
                continue;
            }

            if (checkLB(i, j) && K[j] < MAX_K) {
                currentDevice.add(i);
                Z[j] = Z[j].add(divide(P[i], S[j]));
                K[j]++;
                k++;
            } else {
                N_CUP.set(j, new ArrayList<>(currentDevice));
                log("fill");
                currentDevice = new ArrayList<>();
                if (K[j] == MAX_K) {
                    doSituation1(true, j);
                    k = 0;
                    for (int y = 0; y < M; y++) {
                        if (!Jv.contains(y)) {
                            Z[y] = BigDecimal.ZERO;
                            K[y] = 0;
                        }
                    }
                    j = 0;
                } else if (K[j] == 0 || (j == M - 1 && i < N - 1)) {
                    doSituation2(j);
                    k = 0;
                    for (int y = 0; y < M; y++) {
                        if (!Jv.contains(y)) {
                            Z[y] = BigDecimal.ZERO;
                            K[y] = 0;
                        }
                    }
                    j = 0;
                } else {
                    j++;
                }
            }
        }
        N_CUP.set(j, new ArrayList<>(currentDevice));


        for (int l = 0; l < M; l++) {
            if (K[l] == 0) {
                doSituation2(l);
            }
        }
        for (int l = 0; l < M; l++) {
            if (!result.containsKey(l)) {
                result.put(l, N_CUP.get(l));
            }
        }
    }

    private static void doSituation1(boolean isHatch, int j0) {
        log("1");
        List<Integer> Nj0_hatch = null;
        if (isHatch) {
            try {
                Nj0_hatch = Ns.subList(0, MAX_K);
            } catch (IndexOutOfBoundsException e) {
                System.err.println("|Ns| < K");
            }
            if (Nj0_hatch == null) {
                System.exit(1);
            }
        } else {
            Nj0_hatch = new ArrayList<>(N_CUP.get(j0));
        }
        Integer sumP = Nj0_hatch.stream().mapToInt(i -> P[i]).sum();
        BigDecimal zTemp = divide(sumP, S[j0]);
        if (more(LB, zTemp)) {
            //situation 1.1
            log("1.1");
            BigDecimal LB1 = calculateEstimate1(Nj0_hatch, j0);
            if (lessOrEquals(LB1, LB)) {
                finishWithShop(Nj0_hatch, j0);
            }

        } else if (more(zTemp, LB) && lessOrEquals(zTemp, LB.multiply(new BigDecimal(2)))) {
            // situation 1.2
            log("1.2");
            finishWithShop(Nj0_hatch, j0);
        } else if (more(zTemp, LB.multiply(new BigDecimal(2)))) {
            // situation 1.3
            log("1.3");
            List<Integer> Nj0_cup = new ArrayList<>(N_CUP.get(j0));
            List<Integer> Nj0_complements = new ArrayList<>(Nj0_cup);
            Nj0_complements.removeAll(Nj0_hatch);
            boolean isFinish = false;
            for (int i = 0; i < Nj0_complements.size(); i++) {
                Integer value = Nj0_cup.remove(Nj0_cup.size() - 1);
                boolean isAdd = false;
                for (int j = 0; j < Nj0_cup.size() - 1; i++) {
                    if (value <= P[j] && value > P[j + 1]) {
                        Nj0_cup.add(j + 1, value);
                        isAdd = true;
                    }
                }
                if (!isAdd) {
                    Nj0_cup.add(value);
                }
                int sumP_0 = Nj0_cup.stream().mapToInt(l -> P[l]).sum();
                BigDecimal zTemp_0 = divide(sumP_0, S[j0]);
                if (more(LB, zTemp_0)) {
                    isFinish = true;
                    finishWithShop(Nj0_cup, j0);
                    break;
                }
            }
            if (!isFinish) {
                System.err.print("Error situation 1.3 not finished");
            }
        } else {
            System.err.println("SOMETHING WRONG");
        }
    }

    private static void doSituation2(int j0) {
        BigDecimal LB1 = null;
        List<Integer> possibleJobs = filter_more_LB(j0);
        if (K[j0] == 0) {
            log("2");
            LB1 = calculateEstimate2(possibleJobs, j0);
        }
        int i = 0;
        if (K[j0] != 0 || lessOrEquals(LB1, LB)) {
            log("2.2");
            for (int j = 0; j < j0; j++) {
                if (i == possibleJobs.size()) {
                    break;
                }
                ArrayList<Integer> currentDevice = new ArrayList<>();
                while (K[j] < MAX_K) {
                    K[j]++;
                    currentDevice.add(possibleJobs.get(i));
                    Z[j] = Z[j].add(divide(P[possibleJobs.get(i)], S[j]));
                    i++;
                    j++;
                    if (i == possibleJobs.size()) {
                        break;
                    }
                }
                if (!currentDevice.isEmpty()) {
                    List<Integer> additive = N_CUP.get(j);
                    additive.addAll(currentDevice);
                    additive.sort(Collections.reverseOrder());
                    N_CUP.set(j, additive);
                    doSituation1(false, j);
                }
            }
        } else {
            log("2.1");
            if (LB1 == null) {
                System.err.println("LB1 == null!!");
            }
            work(false);
        }
    }

    private static BigDecimal calculateEstimate1(List<Integer> Njo_hatch, int j0) {
        List<Integer> union = new ArrayList<>(Nv);
        union.addAll(Njo_hatch);
        List<Integer> newSet = new ArrayList<>(Nall);
        newSet.removeAll(union);
        Integer sumP = newSet.stream().mapToInt(i -> P[i]).sum();
        union = new ArrayList<>(Jv);
        union.add(j0);
        newSet = new ArrayList<>(Jall);
        newSet.removeAll(union);
        Integer sumQ = newSet.stream().mapToInt(i -> S[i]).sum();
        return divide(sumP, sumQ);
    }

    private static BigDecimal sumPrevArrayP(List<Integer> array, int index) {
        List<Integer> temp = array.subList(0, index);
        return new BigDecimal(temp.stream().mapToInt(i -> P[i]).sum());
    }

    private static BigDecimal calculateEstimate2(List<Integer> possibleJobs, int j1) {
        BigDecimal minGlobal = null;
        for (int i = 0; i < possibleJobs.size(); i++) {
            Integer pk = possibleJobs.get(i);
            BigDecimal minLocal = divide(P[pk], S[j1]).min(sumPrevArrayP(possibleJobs, i).min(sumS(0, j1)));
            if (minGlobal == null) {
                minGlobal = minLocal;
            } else {
                minGlobal = minGlobal.min(minLocal);
            }
        }
        return minGlobal;
    }

    public static void work(boolean isFirst) {
        init(isFirst);
        firstStep();
    }

    private static void finishWithShop(List<Integer> currentWorks, int j0) {
        List<Integer> temp = new ArrayList<>(currentWorks);
        result.put(j0, temp);
        Nv.addAll(temp);
        Jv.add(j0);
        Ns.removeAll(currentWorks);

    }

    public void start() {
        System.out.println(N + " " + M + " " + MAX_K);
        for (int i = 0; i < N; i++) {
            System.out.print(P[i] + " ");
        }
        System.out.println();
        for (int j = 0; j < M; j++) {
            System.out.print(S[j] + " ");
        }
        System.out.println();
        System.out.println(LB);
        work(true);
        for (Log log : history) {
            System.out.println(log);
        }

    }

}
