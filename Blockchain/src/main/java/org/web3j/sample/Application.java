package org.web3j.sample;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.sample.contracts.generated.MapChain;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;


public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        new Application().run();
    }

    private void run() throws Exception {

        // We start by creating a new web3j instance to connect to remote nodes on the network.
        // Note: if using web3j Android, use Web3jFactory.build(...
        Web3j web3j = Web3j.build(new HttpService("https://rinkeby.infura.io/1p6X1Vby9WW11tNcTTg0"));
        log.info("Connected to Ethereum client version: "
                + web3j.web3ClientVersion().send().getWeb3ClientVersion());

        // We then need to load our Ethereum wallet file
        // FIXME: Generate a new wallet file using the web3j command line tools https://docs.web3j.io/command_line.html
        Credentials credentials =
                WalletUtils.loadCredentials(
                        "123",
                        "UTC--2018-04-21T17-43-12.948279000Z--59ea0893ca2abe7bae02a5c2a8d564c5a2146ae2.json");
        log.info("Credentials loaded");

        // FIXME: Request some Ether for the Rinkeby test network at https://www.rinkeby.io/#faucet
        log.info("Sending 1 Wei ("
                + Convert.fromWei("1", Convert.Unit.ETHER).toPlainString() + " Ether)");

        /*
        TransactionReceipt transferReceipt = Transfer.sendFunds(
                web3j, credentials,
                "0x19e03255f667bdfd50a32722df860b1eeaf4d635",  // you can put any address here
                BigDecimal.ONE, Convert.Unit.WEI)  // 1 wei = 10^-18 Ether
                .send();
        log.info("Transaction complete, view it at https://rinkeby.etherscan.io/tx/"
                + transferReceipt.getTransactionHash());

        */
        // Now lets deploy a smart contract
        log.info("Deploying smart contract");

        BigInteger _price = BigInteger.ONE;
        _price = BigInteger.valueOf(10);

        BigInteger _steps = BigInteger.ONE;
        _steps = BigInteger.valueOf(3);

        MapChain contract = MapChain.deploy(
                web3j, credentials,
                ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT,
                _price, _steps).send();
        /*
        MapChain contract = MapChain.load(
                "0x59ea0893ca2abe7bae02a5c2a8d564c5a2146ae2",
                web3j, credentials,
                ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT
                );
        */
        String contractAddress = contract.getContractAddress();
        log.info("Smart contract deployed to address " + contractAddress);
        log.info("View contract at https://rinkeby.etherscan.io/address/" + contractAddress);

        //log.info("Value stored in remote smart contract: " + contract.greet().send());

        // Lets modify the value in our smart contract
        BigInteger value = BigInteger.ONE;
        value = BigInteger.valueOf(20);
        TransactionReceipt transactionReceipt = contract.joinGame(value).send();

        //log.info("New value stored in remote smart contract: " + contract.greet().send());
    }
}
