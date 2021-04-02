package com.company;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigInteger;
import java.net.URL;
import java.util.*;

public class Main extends Application implements Initializable {

    public TextField nInput;
    public Label pValue;
    public Label qValue;
    public Label pqTimeValue;
    public Label eValue;
    public Label dValue;

    private int p;
    private int q;
    private int e;
    private int d;
    private int phi;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        stage.setTitle("RSA applicaton");
        stage.setScene(new Scene(root, 600, 600));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
    }

    @FXML
    private void findPAndQ(ActionEvent actionEvent) {
        BigInteger n = new BigInteger(nInput.getText());

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
                // Check if q is prime. Otherwise, go to next prime p number
                if (q.isProbablePrime(1)) {
                    //Displays the result
                    Map<String, BigInteger> result = new HashMap<>();
                    result.put("p", p);
                    result.put("q", q);

                    //save p and q
                    this.p = p.intValue();
                    this.q = q.intValue();


                    //The end of the algorithm
                    pValue.setText("P value: " + p);
                    qValue.setText("Q value: " + q);
                    return;
                }
            }

            //p = the next prime number
            p = p.nextProbablePrime();
        }
        System.out.println("No solution exists");
    }

    @FXML
    private void findE(ActionEvent actionEvent) {
        this.phi = (this.p - 1) * (this.q - 1);

        System.out.println(this.phi);
        for (this.e = 2; this.e < this.phi; this.e++) {
            // e is for public key exponent
            if (gcd(e, this.phi) == 1) {
                break;
            }
        }
        System.out.println(this.e);
        eValue.setText("E value: " + this.e);
    }

    @FXML
    private void findD(ActionEvent actionEvent) {
        for (int i = 0; i <= 9; i++) {
            int x = 1 + (i * this.phi);

            // d is for private key exponent
            if (x % this.e == 0) {
                this.d = x / this.e;
                break;
            }
        }
        dValue.setText("D value: " + this.d);
    }

    static int gcd(int e, int z)
    {
        if (e == 0)
            return z;
        else
            return gcd(z % e, e);
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
