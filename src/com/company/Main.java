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
    public TextField decryptNValue;
    public TextField decryptEValue;
    public TextField messageValue;
    public TextField encryptedMessageValue;
    public TextField decryptEncryptedValue;
    public TextField decryptedMessageValue;

    public Label pValue;
    public Label qValue;
    public Label pqTimeValue;

    public Label eValue;
    public Label eTimeValue;

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
        stage.setScene(new Scene(root, 640, 540));
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
        long startTime = System.nanoTime();
        if(isNumeric(nInput.getText())) {
            HashMap<String, BigInteger> result = (HashMap<String, BigInteger>) calculatePAndQ(new BigInteger(nInput.getText()));
            long estimatedTime = System.nanoTime() - startTime;

            this.n = new BigInteger(nInput.getText());

            if (result == null) {
                pValue.setText("No solution exists");
                qValue.setText("");
            } else {
                qValue.setText("Q value: " + String.valueOf(result.get("q").intValue()));
                pValue.setText("P value: " + String.valueOf(result.get("p").intValue()));
                pqTimeValue.setText("It took: " + (int) estimatedTime * 0.000001 + " milliseconds.");

                this.q = result.get("q");
                this.p = result.get("p");
            }
        }else{
            pValue.setText("No valid N value found!");
            qValue.setText("");
        }
    }

    @FXML
    private void displayEncryptedMessage(ActionEvent actionEvent){
        ArrayList<BigInteger> encodeMessage = encodeMessage(messageValue.getText());
        ArrayList<BigInteger> encryptedMessage = encryptEncodedMessage(encodeMessage);

        encryptedMessageValue.setText(String.valueOf(encryptedMessage));
    }

    @FXML
    private void displayDecryptedMessage(ActionEvent actionEvent){

        String input = decryptEncryptedValue.getText().replace("[", "").replace("]", "").replace(" ", "");

        String[] inputAsArray = input.split(",");

        ArrayList<BigInteger> inputAsBigintegers = new ArrayList<>();

        for (String charValueOfLetter: inputAsArray) {
            inputAsBigintegers.add(new BigInteger(charValueOfLetter));
        }

        ArrayList<BigInteger> decryptedMessage = decryptEncryptedMessage(inputAsBigintegers);

        decryptedMessageValue.setText(decodeMessage(decryptedMessage));
    }

    @FXML
    private void findE(ActionEvent actionEvent) {
        if(this.p != null && this.q != null){
            ArrayList<BigInteger> values = new ArrayList<>();

            long startTime = System.nanoTime();

            this.phi = (this.p.subtract(BigInteger.valueOf(1))).multiply(this.q.subtract(BigInteger.valueOf(1)));

            for (this.e = BigInteger.valueOf(2); this.e.compareTo(this.phi) < 0; this.e = this.e.add(BigInteger.ONE)) {
                // e is for public key exponent
                if (gcd(e, this.phi).equals(BigInteger.ONE)) {
                    values.add(this.e);
                }
            }

            long estimatedTime = System.nanoTime() - startTime;

            this.e = values.get((int)(Math.random() * (values.size())));

            eTimeValue.setText("It took: " + (int) estimatedTime * 0.000001 + " milliseconds.");
            eValue.setText("E value: " + this.e);
        }else{
            eValue.setText("No P or Q found!");
        }
    }

    @FXML
    private void findD(ActionEvent actionEvent) {
        String nString = decryptNValue.getText();
        String eString = decryptEValue.getText();

        if(isNumeric(nString) && isNumeric(eString)){
            HashMap<String, BigInteger> pqResult = (HashMap<String, BigInteger>) calculatePAndQ(new BigInteger(nString));

            BigInteger phi = (pqResult.get("p").subtract(BigInteger.ONE)).multiply(pqResult.get("q").subtract(BigInteger.ONE));

            BigInteger e = new BigInteger(eString);

            this.d = e.modInverse(phi);

            dValue.setText("D value: " + this.d);
        }else if(isNumeric(nString) && !isNumeric(eString)){
            dValue.setText("E value is not valid!");

        }
        else if(!isNumeric(nString) && isNumeric(eString)){
            dValue.setText("N value is not valid!");

        }else{
            dValue.setText("E and N values are not valid!");
        }
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

    private ArrayList<BigInteger> decryptEncryptedMessage (ArrayList<BigInteger> encryptedMessage) {
        ArrayList<BigInteger> decryptedMessage = new ArrayList<>();

        for (BigInteger letter:
                encryptedMessage) {
            BigInteger decryptedLetter = letter.modPow(this.d, this.n);
            decryptedMessage.add(decryptedLetter);
        }

        return decryptedMessage;
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

    private ArrayList<BigInteger> encodeMessage(String value){
        ArrayList<BigInteger> encoded = new ArrayList<BigInteger>();

        for (int i = 0; i < value.length(); i++){
            encoded.add(new BigInteger(String.valueOf((int)value.charAt(i))));
            //Process char
        }
        System.out.println("Encoded:");
        System.out.println(encoded);
        System.out.println();
        return encoded;
    }

    private String decodeMessage(ArrayList<BigInteger> encodedMessage){
        System.out.println("Encoded after decrypt:");
        System.out.println(encodedMessage);
        System.out.println();

        StringBuilder decodedMessage = new StringBuilder();

        for (int i = 0; i < encodedMessage.size(); i++){
            decodedMessage.append(Character.toString((char) encodedMessage.get(i).intValue()));
        }

        System.out.println("Decoded message:");
        System.out.println(decodedMessage.toString());
        System.out.println();

        return decodedMessage.toString();
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
