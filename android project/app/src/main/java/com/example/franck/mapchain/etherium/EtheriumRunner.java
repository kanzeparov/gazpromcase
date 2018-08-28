package com.example.franck.mapchain.etherium;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.example.franck.mapchain.contracts.generated.MapChain;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;
import org.web3j.utils.Convert;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class EtheriumRunner {
    private String TAG = "ETHERIUM";
    private MyTask mt;
    private Handler h;
    private ProgressDialog pd;
    private MapChain contractAdmin;
    private MyTaskForReturn mtfr;
    private String contractAddress = "0x7b14cc103e5f81b180950569d39ae58eedfc9ab8";
    public static String jsonUTC =
            "{\"address\":\"59ea0893ca2abe7bae02a5c2a8d564c5a2146ae2\",\"id\":\"65af76b4-3d1a-4abe-954d-7c6f9a3a68e8\",\"version\":3,\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"0029f1a85a9475e5037939628876a65bb8081eabe8b7356d4714b290b991840d\",\"cipherparams\":{\"iv\":\"d80368a31abda00525dfea21d22f4460\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":262144,\"p\":1,\"r\":8,\"salt\":\"9c9b94800e9c09d255e309048a7204c319318cf8f829786ea55988ee145889ee\"},\"mac\":\"ff237552eeb625c008f131824fd39b6f207ec90fd1aa541f824e799dceebc8c7\"}}\n";
    public static String jsonClient =
            "{\"address\":\"09a5dacb427cc8fd596e5b1640fa539dac1a5d6d\",\"id\":\"0f633608-267f-4e94-b4ef-4f4ba635ef89\",\"version\":3,\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"4fc75a27ac790c3308f11c02a356e46cfab8796c866812a5dea9c368c19f6b56\",\"cipherparams\":{\"iv\":\"137a3d2c8c934f1afd108bea643a9ce7\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":262144,\"p\":1,\"r\":8,\"salt\":\"e07cbff898dfc8db886058435d6409d32c11c247b3a9d7544ccb85894ed370e0\"},\"mac\":\"94c0986542d05b14aa481a46752d59c77ebf8443103678c44e2aa8899c326ccb\"}}";

    public void init(String password, File file, String s) throws Exception {
        mt = new MyTask();
        mt.execute(file.getAbsolutePath(), password, s);

//        Log.d(TAG,"Credentials loaded");
//
//        // FIXME: Request some Ether for the Rinkeby test network at https://www.rinkeby.io/#faucet
//        Log.d(TAG,"Sending 1 Wei ("
//                + Convert.fromWei("1", Convert.Unit.ETHER).toPlainString() + " Ether)");
    }

    public void returnMoney(String password, File file, Context context) {
        mtfr = new MyTaskForReturn(context);
        mtfr.execute(file.getAbsolutePath(), password);
    }

    class MyTaskForReturn extends AsyncTask<String, Void, Void> {
        private Context mContext;

        public MyTaskForReturn(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.d("ETHR", "ok pre exec");
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                TimeUnit.SECONDS.sleep(2);
                Web3j web3j = Web3jFactory.build(new HttpService("https://rinkeby.infura.io/1p6X1Vby9WW11tNcTTg0"));
//        Log.d(TAG, "Connected to Ethereum client version: "
//                + web3j.web3ClientVersion().send().getWeb3ClientVersion());

                String file = params[0];
                String password = params[1];
                Log.d("ETHR", file);
                Credentials credentials =
                        WalletUtils.loadCredentials(
                                password,
                                file);
                Log.d("ETHR", " after cred");

                BigInteger _price = BigInteger.ONE;
                _price = BigInteger.valueOf(10);

                BigInteger _steps = BigInteger.ONE;
                _steps = BigInteger.valueOf(3);
                MapChain contract;

                Log.d("ETHR", "int client");
                contract = MapChain.load(
                        contractAddress,
                        web3j, credentials,
                        ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT
                );

                BigInteger value = BigInteger.ONE;
                value = BigInteger.valueOf(20);
                contract.take_amount_owner();
                contract.stopGame();
                contract.take_deposit_player();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.d("ETHR", "ok post Exec");

        }
    }

    class MyTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            if (contractAdmin != null) {
                Log.d("ETHR", "contract admin is not null");
                contractAddress = contractAdmin.getContractAddress();
            }

            Log.d("ETHR", "ok pre exec");
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                TimeUnit.SECONDS.sleep(2);
                Web3j web3j = Web3jFactory.build(new HttpService("https://rinkeby.infura.io/1p6X1Vby9WW11tNcTTg0"));
//        Log.d(TAG, "Connected to Ethereum client version: "
//                + web3j.web3ClientVersion().send().getWeb3ClientVersion());

                String file = params[0];
                String password = params[1];

                Log.d("ETHR", file);
                Credentials credentials =
                        WalletUtils.loadCredentials(
                                password,
                                file);

                Log.d("ETHR", " after cred");

                BigInteger _price = BigInteger.ONE;
                _price = BigInteger.valueOf(10);

                BigInteger _steps = BigInteger.ONE;
                _steps = BigInteger.valueOf(3);
                MapChain contract;
            switch (params[2]) {

                case "creator":
                    contract = MapChain.deploy(
                            web3j, credentials,
                            ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT,
                            _price, _steps).send();
                    contractAdmin = contract;
                    break;

                case "client":
                    Log.d("ETHR", "int client");
                    contract = MapChain.load(
                            contractAddress,
                            web3j, credentials,
                            ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT
                    );

                    BigInteger value = BigInteger.ONE;
                    value = BigInteger.valueOf(20);
                    TransactionReceipt transactionReceipt = contract.joinGame(value).send();
                    break;
                default:
                    contract = null;
            }




                String contractAddress = contract.getContractAddress();
//            Log.d(TAG, "Game time " + contract.Game_time().send().toString());
//            Log.d(TAG, "Current Balance Player " + contract.current_balance_player().send().toString());
                Log.d(TAG, "Smart contract deployed to address " + contractAddress);
                Log.d(TAG, "View contract at https://rinkeby.etherscan.io/address/" + contractAddress);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
//        Log.d("ETHR", contractAdmin.getContractAddress());
            Log.d("ETHR", "ok post Exec");
        }
    }

}
