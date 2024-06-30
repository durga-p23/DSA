import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PatternMatching {

    public static void main(String[] args) {
        // Example usage
        System.out.println(modPatternMatch(1000000007, "CD", "ABCDE"));
        // System.out.println(modPatternMatchWildcard(1000000007, "CD", "ABCDE"));
        // System.out.println(modPatternMatchWildcard(88843, "?EUPRJVNMAX", "AAAAEUPRJVNMAXABCDE"));
        // System.out.println(modPatternMatchWildcard(1000000007, "AA", "AAAAA"));
        // System.out.println(modPatternMatchWildcard(1000000007, "D?", "ABCDE"));
        // System.out.println(modPatternMatchWildcard(79, "JA?S", "AMADBOXERSHOTQUICKGLOVEDJABSTOTHEJAWSOFHISDIZZYOPPONENTATTHEJAMSROCKSHUFFLE"));
        // System.out.println(modPatternMatchWildcard(19, "JA?S", "AMADBOXERSHOTQUICKGLOVEDJABSTOTHEJAWSOFHISDIZZYOPPONENTATTHEJAMSROCKSHUFFLE"));
    }

    // To generate random prime less than N
    public static int randPrime(int N) {
        List<Integer> primes = new ArrayList<>();
        for (int q = 2; q <= N; q++) {
            if (isPrime(q)) {
                primes.add(q);
            }
        }
        Random rand = new Random();
        return primes.get(rand.nextInt(primes.size()));
    }

    // To check if a number is prime
    public static boolean isPrime(int q) {
        if (q > 1) {
            for (int i = 2; i <= Math.sqrt(q); i++) {
                if (q % i == 0) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    // Return appropriate N that satisfies the error bounds
    public static int findN(double eps, int m) {
        double log26 = Math.log(26) / Math.log(2);
        return (int) Math.ceil((2 * m * log26 / eps) * Math.log(2));
    }

    // // This function is made to reduce the space complexity consumed by 26^ (i.e. power of 26 ...)
    // public static int powermod(int power, int q) {
    //     int ans = 1;
    //     for (int i = 0; i < power; i++) {
    //         ans = (ans * (26 % q)) % q;
    //     }
    //     return ans;
    // }

    // Return sorted list of starting indices where p matches x
    public static List<Integer> modPatternMatch(int q, String p, String x) {
        List<Integer> answer = new ArrayList<>();

        // Setting the f mod q value of p string to be zero.
        int f_pq = 0;

        // Calculating f_pq
        for (int i = 0; i < p.length(); i++) {
            f_pq += (powermod(p.length() - i - 1, q) * ((p.charAt(i) - 'A') % q)) % q;
            f_pq = f_pq % q;
        }

        // Calculating f mod q value of first m characters of string x, that is first substring of x of length m.
        int f_xq = 0;
        for (int i = 0; i < p.length(); i++) {
            f_xq += (powermod(p.length() - i - 1, q) * ((x.charAt(i) - 'A') % q)) % q;
            f_xq = f_xq % q;
        }

        // Checking if the f_p mod q and f_x (of the length m) mod q are equal or not, if equal then append
        if (f_xq == f_pq) {
            answer.add(0);
        }

        // Loop through the rest of the string x to find matches
        for (int j = 1; j <= x.length() - p.length(); j++) {
            f_xq = (26 % q) * ((f_xq - (powermod(p.length() - 1, q) * ((x.charAt(j - 1) - 'A') % q)) % q) % q);
            f_xq = f_xq % q;

            f_xq += (x.charAt(j + p.length() - 1) - 'A') % q;
            f_xq = f_xq % q;

            if (f_xq == f_pq) {
                answer.add(j);
            }
        }

        return answer;
    }

    // Return sorted list of starting indices where p matches x with wildcard
    public static List<Integer> modPatternMatchWildcard(int q, String p, String x) {
        List<Integer> answer = new ArrayList<>();

        // Setting the f mod q value of p string to be zero.
        int f_pq = 0;
        int index = -1; // Index of '?' in the string p

        // Calculating f_pq
        for (int i = 0; i < p.length(); i++) {
            if (p.charAt(i) != '?') {
                f_pq += (powermod(p.length() - i - 1, q) * ((p.charAt(i) - 'A') % q)) % q;
            }
            if (p.charAt(i) == '?') {
                index = i;
            }
            f_pq = f_pq % q;
        }

        // Calculating f mod q value of first m characters of string x, that is first substring of x of length m.
        int f_xq = 0;
        for (int i = 0; i < p.length(); i++) {
            if (i != index) {
                f_xq += (powermod(p.length() - i - 1, q) * ((x.charAt(i) - 'A') % q)) % q;
            }
            f_xq = f_xq % q;
        }

        // Checking if the f_p mod q and f_x (of the length m) mod q are equal or not, if equal then append
        if (f_xq == f_pq) {
            answer.add(0);
        }

        // Loop through the rest of the string x to find matches
        for (int j = 1; j <= x.length() - p.length(); j++) {
            if (index != 0) {
                f_xq = ((26 % q) * ((f_xq - (powermod(p.length() - 1, q) * ((x.charAt(j - 1) - 'A') % q)) % q) % q)) % q;
            }
            if (index != p.length() - 1 && index != 0) {
                f_xq = (f_xq % q - (powermod(p.length() - index - 1, q) * ((x.charAt(j + index) - 'A') % q)) % q) % q;
            }
            if (index == 0) {
                f_xq = (f_xq % q - (powermod(p.length() - 2, q) * ((x.charAt(j) - 'A') % q)) % q) % q;
                f_xq = (26 % q * f_xq) % q;
            }
            if (index != 0) {
                f_xq = (f_xq % q + (powermod(p.length() - index, q) * ((x.charAt(j + index - 1) - 'A') % q)) % q) % q;
            }

            // Checking if the f_p mod q and f_x (of the length m) mod q are equal or not, if equal then append
            if (f_xq == f_pq) {
                answer.add(j);
            }
        }

        return answer;
    }

    //This function is made to reduce the space complexity consumed by 26^ (i.e. power of 26 ...)
    public static int powermod(int power, int q) {
        int ans = 1;
        for (int i = 0; i < power; i++) {
            ans = (ans * (26 % q)) % q;
        }
        return ans;
    }
}
