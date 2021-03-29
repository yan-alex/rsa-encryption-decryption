package com.company;

import java.math.BigInteger;
import java.util.*;
import java.math.BigInteger;

public class Main {

    public static void main(String[] args) {
        // write your code here
        Map <String, BigInteger> PAndQ = findPAndQ(new BigInteger("77"));
        assert PAndQ != null;
        System.out.println("p: " + PAndQ.get("p"));
        System.out.println("q: " + PAndQ.get("q"));
    }

    private static Map<String, BigInteger> findPAndQ(BigInteger n) {
        //The first prime number
        BigInteger INIT_NUMBER = new BigInteger("2");

        //Initialise p
        BigInteger p = INIT_NUMBER;

        //For each prime p
        while (p.compareTo(n.divide(INIT_NUMBER)) <= 0) {

            //If we find p
            if (n.mod(p).equals(BigInteger.ZERO)) {
                //Calculate q
                BigInteger q = n.divide(p);
                //Displays the result
                Map <String, BigInteger> result = new HashMap<>();
                result.put("p", p);
                result.put("q", q);
                //The end of the algorithm
                return result;
            }
            //p = the next prime number
            p = p.nextProbablePrime();
        }
        System.out.println("No solution exists");
        return null;
    }


    static List<Integer> getPrimeList(final int MAX_PRIME) {
        // Initialize a boolean array and set all values to true.
        Boolean[] isPrime = new Boolean[MAX_PRIME + 1];
        Arrays.fill(isPrime, true);

        List<Integer> primes = new ArrayList<>();

        // Start from 2. 0 and 1 are not prime.
        for (int i = 2; i * i <= MAX_PRIME; i++) {
            // If we've found a prime, set all it's multiples as composite,
            // and add this prime number to the list.
            if (isPrime[i]) {
                for (int j = i * i; j <= MAX_PRIME; j += i) isPrime[j] = false;
                primes.add(i);
            }
        }

        return primes;
    }


}
