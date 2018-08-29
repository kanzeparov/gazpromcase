pragma solidity ^0.4.11;

import "./ERC20Standard.sol";

contract LoyaltyToken is ERC20Standard {
	function LoyaltyToken() {
		totalSupply = 400;
		name = "UniversalLoyalty coin";
		decimals = 4;
		symbol = "BEC";
		version = "1.0";
		balances[msg.sender] = totalSupply;
	}
}