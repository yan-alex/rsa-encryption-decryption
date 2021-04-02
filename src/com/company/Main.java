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

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.net.URL;
import java.util.*;

public class Main extends Application implements Initializable {

    public TextField nInput;
    public TextField decryptNValue;
    public TextField decryptEValue;

    public Label pValue;
    public Label qValue;
    public Label pqTimeValue;
    public Label eValue;
    public Label dValue;

    private BigInteger p;
    private BigInteger q;
    private BigInteger n;
    private BigInteger e;
    private BigInteger d;
    private BigInteger phi;

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
    private void displayPAndQ(ActionEvent actionEvent) {

        HashMap<String, BigInteger> result = (HashMap<String, BigInteger>) calculatePAndQ(new BigInteger(nInput.getText()));
        this.n = new BigInteger(nInput.getText());
        if(result == null){
            System.out.println("No solution exists");
        }else{
            qValue.setText("Q value: " + String.valueOf(result.get("q").intValue()));
            pValue.setText("P value: " + String.valueOf(result.get("p").intValue()));

            this.q = result.get("q");
            this.p = result.get("p");
        }
    }

    @FXML
    private void findE(ActionEvent actionEvent) {
        this.phi = (this.p.subtract(BigInteger.valueOf(1))).multiply(this.q.subtract(BigInteger.valueOf(1)));

        System.out.println(this.phi);
        for (this.e = BigInteger.valueOf(2); this.e.compareTo(this.phi) < 0; this.e = this.e.add(BigInteger.ONE)) {
            // e is for public key exponent
            if (gcd(e, this.phi).equals(BigInteger.ONE)) {
                break;
            }
        }
        System.out.println(this.e);
        eValue.setText("E value: " + this.e);
    }

    @FXML
    private void findD(ActionEvent actionEvent) {
        String n = decryptNValue.getText();
        HashMap<String, BigInteger> pqResult = (HashMap<String, BigInteger>) calculatePAndQ(new BigInteger(n));

        BigInteger phi = (pqResult.get("p").subtract(BigInteger.ONE)).multiply(pqResult.get("q").subtract(BigInteger.ONE));

        BigInteger e = new BigInteger(decryptEValue.getText());

        for (int i = 0; i <= 9; i++) {
            BigInteger x = BigInteger.ONE.add(new BigInteger(String.valueOf(i)).multiply(phi));

            // d is for private key exponent
            if (x.mod(e).equals(BigInteger.ZERO)) {
                this.d = x.divide(e);
                break;
            }
        }
        dValue.setText("D value: " + this.d);
    }

    private ArrayList<BigInteger> encryptEncodedMessage (ArrayList<BigInteger> encodedMessage) {
        ArrayList<BigInteger> encryptedMessage = new ArrayList<>();

        for (BigInteger letter:
             encodedMessage) {
            BigInteger encryptedLetter = letter.modPow(this.e, this.n);
            encryptedMessage.add(encryptedLetter);
        }

        return encryptedMessage;
    }

        static BigInteger gcd(BigInteger e, BigInteger z)
    {
        if (e.equals(BigInteger.valueOf(0)))
            return z;
        else
            return gcd(z.mod(e), e);
    }

    private Map<String, BigInteger> calculatePAndQ(BigInteger n) {

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

                    //The end of the algorithm
                    return result;
                }
            }

            //p = the next prime number
            p = p.nextProbablePrime();
        }
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

    private ArrayList<Integer> encodeMessage(String value){
        ArrayList<Integer> encoded = new ArrayList<Integer>();

        for (int i = 0; i < value.length(); i++){
            encoded.add ((int)value.charAt(i));
            //Process char
        }
        return encoded;
    }
}
