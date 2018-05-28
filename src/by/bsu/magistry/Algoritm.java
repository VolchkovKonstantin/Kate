package by.bsu.magistry;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Algoritm {

    private static int N, M, MAX_K, J0;
    private static double LB;
    private static Integer[] P, Q, K;
    private static double[] Z;
    private static ArrayList<Integer> Nv = new ArrayList<>();
    private static ArrayList<Integer> Ns = new ArrayList<>();
    private static HashMap<Integer, List<Integer>> result = new HashMap<>();

    private static double sumP(int start, int end) {
        double sumP = 0;
        for (int i = start ; i < end; i ++) {
            sumP += P[i];
        }
        return sumP;
    }

    private static double sumQ(int start, int end) {
        double sumQ = 0;
        for (int i = start ; i < end; i ++) {
            sumQ += Q[i];
        }
        return sumQ;
    }

    private static void calculateLB() {
        double sumP = sumP(0, N);
        double sumQ = sumQ(0, M);
        double element1 = sumP / sumQ;
        double element2 = (double)P[0] / Q[0];
        double element3 = ((double) (P[0] + P[1])) / Q[0];
        double element4 = (double)P[1] / Q[1];
        LB = Math.max(element1, Math.max(element2, Math.min(element3, element4)));
    }

    private static void initWorks() {
        for (int i = 0; i < N; i++) {
            Ns.add(i);
        }
    }

    private static void readInput() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("test.txt"));
        N = scanner.nextInt();
        M = scanner.nextInt();
        MAX_K = scanner.nextInt();

        P = new Integer[N];
        Q = new Integer[M];

        for (int i = 0; i < N; i ++) {
            P[i] = scanner.nextInt();
        }

        for (int j = 0; j < M;j ++) {
            Q[j] = scanner.nextInt();
        }
        Arrays.sort(P, Collections.reverseOrder());
        Arrays.sort(Q, Collections.reverseOrder());
    }

    private static void init() {
        Z = new double[M];
        K = new Integer[M];
        for (int j = 0; j < M; j++) {
            K[j] = 0;
        }
        J0 = 1;
        calculateLB();
        initWorks();
        N_CUP = new ArrayList<ArrayList<Integer>>();
    }

    private static void printArray(int[] arr) {
        for (int  i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }
    private static void printArray(Integer[] arr) {
        for (int  i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    private static boolean checkLB(int i, int j) {
        return (Z[j] + (double)P[i]/ Q[j]) <= LB;
    }

    private static int getNumberSituation(ArrayList<Integer> temp, int j0) {
        double value = (double)temp.stream().mapToInt(Integer::intValue).sum() / Q[j0];
        if ((LB < value) && (value <= 2 * LB)) {
            return 0;
        }
        if (value > 2 * LB) {
            return 1;
        }
        return 2;
    }

    private static void doSituation0(ArrayList<Integer> temp, int j0) {
        Nv.addAll(temp);
        Ns.removeAll(temp);
        result.put(j0, temp);
    }

    private static ArrayList<ArrayList<Integer>> N_CUP;

    private static void firstStep() {
        int i = 0, j = 0;
        ArrayList<Integer>currentDevice = new ArrayList<>();
        while(i < N && j < M) {
            if (checkLB(i, j) && K[j] <= MAX_K) {
                currentDevice.add(i);
                Ns.remove((Integer)i);
                Z[j] += (double)P[i] / Q[j];
                K[j]++;
                i++;
            } else {
                N_CUP.add(currentDevice);
                j++;
                currentDevice = new ArrayList<>();
            }
        }
    }

    private static List<Integer> filter_less_LB(int j0) {
        List<Integer> temp = new ArrayList<>(Ns);
        return temp.stream().filter(p -> (double)p / Q[j0] <= LB).collect(Collectors.toList());
    }

    private static List<Integer> filter_more_LB(int j0) {
        List<Integer> temp = new ArrayList<>(Ns);
        return temp.stream().filter(p -> (double)p / Q[j0] > LB).collect(Collectors.toList());
    }

    private static void situation1(int j) {
        List<Integer> n_cup_j = N_CUP.get(j);

    }

    private static void secondStep() {
        for (int j = 0; j < M; j++) {
            if (K[j] == MAX_K) {
                List<Integer> Nj0 = filter_less_LB(j);
                List<Integer> Nj0_hatch = null;
                try {
                     Nj0_hatch = Nj0.subList(0, MAX_K);
                } catch (IndexOutOfBoundsException e) {
                    System.err.println("|Ns| < K");
                }
                if (Nj0_hatch == null) {
                    System.exit(1);
                }
                double sumP = (double)Nj0_hatch.stream().mapToInt(i -> i).sum();
                double zTemp = sumP / Q[j];
                if (LB < zTemp && zTemp <= 2 * LB) {
                    // situation 1.1
                    result.put(j, Nj0_hatch);
                    Nv.add(j);
                    Ns.removeAll(Nj0_hatch);
                } else if (zTemp > 2 * LB) {
                    // situation 1.2
                    List<Integer> Nj0_cup = new ArrayList<>(N_CUP.get(j));
                    List<Integer> Nj0_complements = new ArrayList<>(Nj0_cup);
                    Nj0_complements.removeAll(Nj0_hatch);
                    int l = 0;
                    while(l < Nj0_complements.size()) {
                        Nj0_cup.remove(Nj0_cup.size() - 1);
                        Nj0_cup.add(Nj0_complements.get(l));
                        double sumP_0 = (double) Nj0_cup.stream().mapToInt(i -> i).sum();
                        double zTemp_0 = sumP_0 / Q[j];
                        if (zTemp_0 < 2 * LB) {
                            result.put(j, Nj0_cup);
                            Nv.add(j);
                            Ns.removeAll(Nj0_cup);
                            break;
                        }
                    }
                    if (l == Nj0_complements.size()) {
                    // situation 1.3
                        recount();
                    }

                } else {
                    System.err.println("Sotuation 1 sum < LB");
                }
            }
            if (K[j] == 0) {
                List<Integer> possibleJobs = filter_more_LB(j);
                double LB1 = calculateEstimate(possibleJobs, j);
                if (LB1 > LB) {
                    //situation 2.1
                    recount(LB1);
                } else {
                    //situation 2.2
                    skip();
                }
            }
        }
    }

    private static double sumPBeforeJ(int j1) {
        double result = 0;
        for (int i = 0; i < j1; i++) {
            List<Integer> pList = N_CUP.get(j1);
            for (Integer pi : pList) {
                result += P[pi];
            }
        }
        return result;
    }

    private static double calculateEstimate(List<Integer> possibleJobs, int j1) {
        double minGlobal = -1;
        for (Integer pk : possibleJobs) {
            double minLocal = Math.min((double) pk / Q[j1], sumPBeforeJ(j1)/ sumQ(0, j1));
            if (minGlobal == -1) {
                minGlobal = minLocal;
            } else {
                minGlobal = Math.min(minGlobal, minLocal);
            }
        }
        return minGlobal;
    }

    public static void main(String[] args) throws FileNotFoundException {
        readInput();
        printArray(P);
        printArray(Q);
        init();
        printArray(K);
        System.out.println(LB);
        double[][]T = new double[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                T[i][j] = (double) P[i] / Q[j];
                System.out.print(T[i][j] + " ");
            }
            System.out.println();
        }
        firstStep();
        for (int j = 0; j < M; j++) {
            ArrayList<Integer> works = N_CUP.get(j);
            for (Integer work : works) {
                System.out.print(work + " ");
            }
            System.out.println();
        }

        for (int j = 0; j < Ns.size(); j++) {
            System.out.print(Ns.get(j) + " ");
        }
        System.out.println();
    }
}
