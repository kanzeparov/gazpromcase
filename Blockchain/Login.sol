pragma solidity ^0.4.11;

contract Login {

    event LoginEvent(address sender, string challenge);

    function login(string challenge) {
        LoginEvent(msg.sender, challenge);
    }
}