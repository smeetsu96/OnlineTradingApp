/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etradingapp;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import java.util.Date;

/**
 *
 * @author smeet
 */
public class ETradingApp extends Application {
    Label accountType;
    Label transactionType;
    Label symbol;
    Label tradingSession;
    Label action;
    Label quantity;
    Label price;
    Label orderType;
    Label timeInForce;
    Label firstCurr;
    Label secondCurr;
    TextField symbolField;
    TextField quantityField;
    TextField priceField;
    TextField firstCurrField;
    TextField secondCurrField;
    RadioButton standardHrs;
    RadioButton extendedHrs;
    ObservableList<String> accounts;
    ComboBox accountBox;
    ObservableList<String> transactions;
    ComboBox transactionBox;
    ObservableList<String> actions;
    ComboBox actionBox;
    ObservableList<String> orders;
    ComboBox orderBox;
    ObservableList<String> timesInForce;
    ComboBox timeInForceBox;
    CheckBox skipPreview;
    
    @Override
    public void start(Stage primaryStage) {
        accountType = new Label("Account");
        accounts = FXCollections.observableArrayList("Brokerage Account", "Retirement Account");
        accountBox = new ComboBox(accounts);
        transactionType = new Label("Transaction Type");
        transactions = FXCollections.observableArrayList("Stocks/ETFs");
        transactionBox = new ComboBox(transactions);
        symbol = new Label("Symbol");
        symbolField = new TextField();
        firstCurr = new Label("First Currency");
        firstCurrField = new TextField();
        secondCurr = new Label("Second Currency");
        secondCurrField = new TextField();
        tradingSession = new Label("Trading Session");
        standardHrs = new RadioButton("Standard Hours");
        extendedHrs = new RadioButton("Extended Hours");
        action = new Label("Action");
        actions = FXCollections.observableArrayList("Buy", "Sell", "Sell All Shares", "Specific Shares");
        actionBox = new ComboBox(actions);
        quantity = new Label("Quantity");
        quantityField = new TextField();
        price = new Label("Price");
        priceField = new TextField();
        orderType = new Label("Order Type");
        orders = FXCollections.observableArrayList("Market Order", "Limit Order", "Stop Loss", "Stop Limit", "Trailing Stop Loss($)", "Trailing Stop Loss(%)", "Trailing Stop Limit($)", 
                "Trailing Stop Limit(%)");
        orderBox = new ComboBox(orders);
        timeInForce = new Label("Time In Force");
        timesInForce = FXCollections.observableArrayList("Day", "Good 'till Cancelled", "Fill or Kill", "Immediate or Cancel", "On the Open", "On the Close");
        timeInForceBox = new ComboBox(timesInForce);
        skipPreview = new CheckBox("Skip Preview");
        
        Button submit = new Button();
        submit.setText("Submit Order");
        
        Button cancel = new Button();
        cancel.setText("Cancel Order");
        
        Button replace = new Button();
        replace.setText("Replace Order");
        
        
        submit.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");
                Date date = new Date();
                String fixString;
                String header = "8=FIX4.4";
                String singleOrd = "^35=D";
                String iscID = "^49=IssuedSenderCompID";
                String targetCompID = "^56=MBT^34=388";
                String sendDate = "^52=" + dateFormat.format(date).toString();
                String possDupFlag = "^43=Y";
                String acctString = null;
                String exDest = "^100=MBTX";
                String quantTV = "^38=" + quantityField.getText();
                String symTV = "^55=/" + symbolField.getText();
                String handlInst = "^21=1";
                String side = null;
                String locateStock = "^114=Y";
                String tifString = null;
                String ordString = null;
                String clOrdID = "^11=ClOrdID";
                String transactTime = null;
                //String priceTV = "^44=" + priceField.getText();
                String trailString = "^553=IssuedSenderCompID^10=085^~";
                
                if(actionBox.getValue() == "Buy"){
                    side = "^54=1";
                }
                else if(actionBox.getValue() == "Sell"){
                    side = "^54=2";
                }
                else{
                    side = "^54=3";
                }
                
                if(orderBox.getValue() == "Market Order"){
                    ordString = "^40=1";
                }
                else if(orderBox.getValue() == "Limit Order"){
                    ordString = "^40=2";
                }
                else{
                    ordString = "^40=3";
                }
                
                if (accountBox.getValue() == "Brokerage Account"){
                    acctString = "^1=AcctNum";
                }
                else{
                    acctString = "^1=AcctNum";
                }
                
                if(transactionBox.getValue() == "Stocks/ETFs"){
                    transactTime = "^60=" + dateFormat.format(date).toString();
                }
                
                if(timeInForceBox.getValue() == "Day" && (orderBox.getValue() == "Market Order" || orderBox.getValue() == "Limit Order" || orderBox.getValue() == "Stop Loss" || 
                        orderBox.getValue() == "Stop Limit")){
                    tifString = "^59=0";
                }
                else if (timeInForceBox.getValue() == "Good 'till Cancelled" && (orderBox.getValue() == "Market Order" || orderBox.getValue() == "Limit Order" || orderBox.getValue() == "Stop Loss" || 
                        orderBox.getValue() == "Stop Limit")){
                    tifString = "^59=1";
                }
                else if (timeInForceBox.getValue() == "Immediate or Cancel" && (orderBox.getValue() == "Market Order" || orderBox.getValue() == "Limit Order" || orderBox.getValue() == "Stop Loss" || 
                        orderBox.getValue() == "Stop Limit")){
                    tifString = "^59=3";
                }
                else if (timeInForceBox.getValue() == "Fill or Kill" && orderBox.getValue() == "Market Order"){
                    tifString = "^59=4";
                }
                fixString = header + singleOrd + iscID + targetCompID + sendDate + possDupFlag + acctString + exDest + quantTV + symTV + handlInst + side + locateStock + tifString + ordString + 
                        clOrdID + transactTime + trailString;
                String bodyLen = "^9=" + fixString.length();
                fixString = header + bodyLen + singleOrd + iscID + targetCompID + sendDate + possDupFlag + acctString + exDest + quantTV + symTV + handlInst + side + locateStock + tifString + 
                        ordString + clOrdID + transactTime + trailString;
                System.out.println("FIX String: " + fixString);
                JDialog stringMsg = new javax.swing.JDialog();
                JOptionPane.showMessageDialog(stringMsg, "FIX Message: " + fixString, "Order String", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event){
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");
                Date date = new Date();
                String fixString;
                String header = "8=FIX4.4";
                String singleOrd = "^35=F";
                String iscID = "^49=IssuedSenderCompID";
                String targetCompID = "^56=MBT^34=35";
                String sendDate = "^52=" + dateFormat.format(date).toString();
                String acctString = null;
                String symTV = "^55=/" + firstCurrField.getText() + "/" + secondCurrField.getText();
                String side = null;
                String ordID = "^37=1u2ea2s:12yy";
                String clOrdID = "^11=ClOrdID";
                String origClOrdID = "^41=3^10017=1u2ea2s:12yy";
                String transactTime = null;
                String trailString = "^10=113^~";
                
                if(actionBox.getValue() == "Buy"){
                    side = "^54=1";
                }
                else if(actionBox.getValue() == "Sell"){
                    side = "^54=2";
                }
                else{
                    side = "^54=3";
                }
                
                if (accountBox.getValue() == "Brokerage Account"){
                    acctString = "^1=AcctNum";
                }
                else{
                    acctString = "^1=AcctNum";
                }
                
                if(transactionBox.getValue() == "Stocks/ETFs"){
                    transactTime = "^60=" + dateFormat.format(date).toString();
                }
                
                fixString = header + singleOrd + iscID + targetCompID + sendDate + acctString + symTV + side + ordID + clOrdID + origClOrdID + transactTime + trailString;
                String bodyLen = "^9=" + fixString.length();
                fixString = header + bodyLen + singleOrd + iscID + targetCompID + sendDate + acctString + symTV + side + ordID + clOrdID + origClOrdID + transactTime + trailString;
                JDialog stringMsg = new javax.swing.JDialog();
                JOptionPane.showMessageDialog(stringMsg, "FIX Message: " + fixString, "Order String", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        replace.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event){
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");
                Date date = new Date();
                String fixString;
                String header = "8=FIX4.4";
                String singleOrd = "^35=G";
                String iscID = "^49=IssuedSenderCompID";
                String targetCompID = "^56=MBT^34=288";
                String sendDate = "^52=" + dateFormat.format(date).toString();
                String acctString = null;
                String exDest = "^100=MBTX";
                String quantTV = "^38=" + quantityField.getText();
                String stopPriceTV = "^99=" + priceField.getText();
                String ordString = null;
                String clOrdID = "^11=ClOrdID";
                String origClOrdID = "^41=OriginalClOrdID";
                String priceTV = "^44=" + priceField.getText();
                String handlInst = "^21=1";
                String symTV = "^55=/" + firstCurrField.getText() + "/" + secondCurrField.getText();
                String side = null;
                String tifString = null;
                String transactTime = null;
                String trailString = "^10=148^~";
                
                if(actionBox.getValue() == "Buy"){
                    side = "^54=1";
                }
                else if(actionBox.getValue() == "Sell"){
                    side = "^54=2";
                }
                else{
                    side = "^54=3";
                }
                
                if(orderBox.getValue() == "Market Order"){
                    ordString = "^40=1";
                }
                else if(orderBox.getValue() == "Limit Order"){
                    ordString = "^40=2";
                }
                else{
                    ordString = "^40=3";
                }
                
                if (accountBox.getValue() == "Brokerage Account"){
                    acctString = "^1=AcctNum";
                }
                else{
                    acctString = "^1=AcctNum";
                }
                
                if(transactionBox.getValue() == "Stocks/ETFs"){
                    transactTime = "^60=" + dateFormat.format(date).toString();
                }
                
                if(timeInForceBox.getValue() == "Day" && (orderBox.getValue() == "Market Order" || orderBox.getValue() == "Limit Order" || orderBox.getValue() == "Stop Loss" || 
                        orderBox.getValue() == "Stop Limit")){
                    tifString = "^59=0";
                }
                else if (timeInForceBox.getValue() == "Good 'till Cancelled" && (orderBox.getValue() == "Market Order" || orderBox.getValue() == "Limit Order" || orderBox.getValue() == "Stop Loss" || 
                        orderBox.getValue() == "Stop Limit")){
                    tifString = "^59=1";
                }
                else if (timeInForceBox.getValue() == "Immediate or Cancel" && (orderBox.getValue() == "Market Order" || orderBox.getValue() == "Limit Order" || orderBox.getValue() == "Stop Loss" || 
                        orderBox.getValue() == "Stop Limit")){
                    tifString = "^59=3";
                }
                else if (timeInForceBox.getValue() == "Fill or Kill" && orderBox.getValue() == "Market Order"){
                    tifString = "^59=4";
                }
                fixString = header + singleOrd + iscID + targetCompID + sendDate + acctString + exDest + quantTV + stopPriceTV + ordString + clOrdID + origClOrdID + priceTV + handlInst + symTV
                        + side + tifString + transactTime + trailString;
                String bodyLen = "^9=" + fixString.length();
                fixString = header + bodyLen + singleOrd + iscID + targetCompID + sendDate + acctString + exDest + quantTV + stopPriceTV + ordString + clOrdID + origClOrdID + priceTV + handlInst + symTV
                        + side + tifString + transactTime + trailString;
                JDialog stringMsg = new javax.swing.JDialog();
                JOptionPane.showMessageDialog(stringMsg, "FIX Message: " + fixString, "Order String", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        GridPane trades = new GridPane();
        trades.setPadding(new Insets(13, 13, 13, 13));
        trades.setMinSize(600, 600);
        trades.setStyle("-fx-background-color: white;");
        trades.setVgap(5);
        trades.setHgap(5);
        trades.add(accountType, 0, 0);
        trades.add(accountBox, 0, 1);
        trades.add(transactionType, 0, 2);
        trades.add(transactionBox, 0, 3);
        trades.add(symbol, 1, 2);
        trades.add(firstCurr, 2, 2);
        trades.add(secondCurr, 3, 2);
        trades.add(symbolField, 1, 3);
        trades.add(firstCurrField, 2, 3);
        trades.add(secondCurrField, 3, 3);
        trades.add(tradingSession, 0, 4);
        trades.add(standardHrs, 0, 5);
        trades.add(extendedHrs, 1, 5);
        trades.add(action, 0, 6);
        trades.add(quantity, 1, 6);
        trades.add(actionBox, 0, 7);
        trades.add(quantityField, 1, 7);
        trades.add(price, 0, 8);
        trades.add(priceField, 0, 9);
        trades.add(orderType, 1, 8);
        trades.add(orderBox, 1, 9);
        trades.add(timeInForce, 0, 10);
        trades.add(timeInForceBox, 0, 11);
        trades.add(submit, 0, 12);
        trades.add(cancel, 1, 12);
        trades.add(replace, 2, 12);
        trades.add(skipPreview, 0, 13);
          
        Scene scene = new Scene(trades, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Trades");
        primaryStage.show();
        
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
