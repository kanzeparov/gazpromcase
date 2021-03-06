package com.example.franck.mapchain.contracts.generated;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.3.1.
 */
public class MapChain extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b506040516040806105cc83398101604052805160209091015160006003819055600481905560018054600160a060020a033316600160a860020a0319909116179055600292909255600555600681905560075561055a806100726000396000f3006080604052600436106100b95763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416632b0579df81146100be5780632d4c5ab9146100e557806349613c17146100fa5780634ec0bcf41461010f57806383bd72ba146101265780638d98d69d1461013b57806390278ea3146101435780639e5ef27014610158578063a7ded5d21461016d578063c92499c614610182578063d4f77b1c14610197578063e3c057fa1461019f575b600080fd5b3480156100ca57600080fd5b506100d36101c8565b60408051918252519081900360200190f35b3480156100f157600080fd5b506100d36101ce565b34801561010657600080fd5b506100d36101d4565b34801561011b57600080fd5b506101246101da565b005b34801561013257600080fd5b5061012461024d565b6101246102dd565b34801561014f57600080fd5b50610124610351565b34801561016457600080fd5b506100d36103a7565b34801561017957600080fd5b506100d36103d1565b34801561018e57600080fd5b506101246103d7565b610124610445565b3480156101ab57600080fd5b506101b46104ee565b604080519115158252519081900360200190f35b60075490565b60035490565b60065490565b60005433600160a060020a039081169116146101f557600080fd5b60015460a060020a900460ff16151561020d57600080fd5b6005546006541061021d57600080fd5b6002546003541161022d57600080fd5b600254600480548201905560038054919091039055600680546001019055565b6000805433600160a060020a0390811691161461026957600080fd5b60015460a060020a900460ff16151561028157600080fd5b600060035411156102b3575060008054600160a060020a03168152600a60205260408120600380548254018255919091555b504260095560006006556001805474ff000000000000000000000000000000000000000019169055565b60005433600160a060020a039081169116146102f857600080fd5b6001805460a060020a900460ff1615151461031257600080fd5b6000341161031f57600080fd5b6000805473ffffffffffffffffffffffffffffffffffffffff191633600160a060020a03161790556003805434019055565b600061035c336104fe565b905060008111156103a457604051600160a060020a0333169082156108fc029083906000818181858888f1935050505015801561039d573d6000803e3d6000fd5b5060006003555b50565b6001805460009160a060020a90910460ff16151514156103c657426009555b506008546009540390565b60045490565b60015433600160a060020a039081169116146103f257600080fd5b60045460001061040157600080fd5b600154600454604051600160a060020a039092169181156108fc0291906000818181858888f1935050505015801561043d573d6000803e3d6000fd5b506000600455565b600254341161045357600080fd5b60015460a060020a900460ff161561046a57600080fd5b60008054600160a060020a0333811673ffffffffffffffffffffffffffffffffffffffff19909216919091178083556001805474ff0000000000000000000000000000000000000000191660a060020a178155346003556006849055600780549091019055426008556009929092556104e391166104fe565b600380549091019055565b60015460a060020a900460ff1690565b600160a060020a0381166000908152600a6020526040812080548210156105285780546000825591505b509190505600a165627a7a723058205881f376a5e58954c6f17f3a812035e4e24db6bc90d90e998bc9270804378d2b0029\n";

    protected MapChain(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected MapChain(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<BigInteger> Rating_game() {
        final Function function = new Function("Rating_game", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> current_balance_player() {
        final Function function = new Function("current_balance_player", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> current_step() {
        final Function function = new Function("current_step", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> update_step() {
        final Function function = new Function(
                "update_step", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> stopGame() {
        final Function function = new Function(
                "stopGame", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> add_balance(BigInteger weiValue) {
        final Function function = new Function(
                "add_balance", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<TransactionReceipt> take_deposit_player() {
        final Function function = new Function(
                "take_deposit_player", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> Game_time() {
        final Function function = new Function("Game_time", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> current_amount_owner() {
        final Function function = new Function("current_amount_owner", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> take_amount_owner() {
        final Function function = new Function(
                "take_amount_owner", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> joinGame(BigInteger weiValue) {
        final Function function = new Function(
                "joinGame", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<Boolean> status_game() {
        final Function function = new Function("status_game", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public static RemoteCall<MapChain> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger _price, BigInteger _steps) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Uint256(_price),
                new Uint256(_steps)));
        return deployRemoteCall(MapChain.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<MapChain> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _price, BigInteger _steps) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Uint256(_price),
                new Uint256(_steps)));
        return deployRemoteCall(MapChain.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static MapChain load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new MapChain(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static MapChain load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new MapChain(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}