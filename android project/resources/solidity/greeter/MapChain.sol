pragma solidity ^0.4.0;
contract MapChain {
    address player;
    address owner;

    bool game_status; //true - now leasing; false - now not leasing
    uint256 price; //price for 1 step
    uint256 balance; //now available
    uint256 amountForOwner;
    uint256 steps;
    uint256 current_steps;
    uint256 rating;

    uint start;
    uint end;

    struct Leaser {
        uint256 depositToReturn;
        //address leaser;
    }

    mapping(address => Leaser) leasers;

    function MapChain (uint256 _price, uint256 _steps) public {
        balance=0;
        amountForOwner=0;
        game_status=false;
        owner = msg.sender;
        price = _price;
        steps = _steps;
        current_steps = 0;
        rating=0;
    }

    function joinGame () public payable {
        require(msg.value > price);
        require(!game_status);

        player = msg.sender;
        game_status = true;
        balance = msg.value;
        current_steps = 0;
        rating += 1;
        start = now;
        end = 0;

        //add deposit to return of current leaser to balance
        balance += releasDeposit(player);
    }

    function releasDeposit(address _player) private returns (uint256 _deposit) {
        Leaser storage _l = leasers[_player];
        _deposit = 0;
        if (_l.depositToReturn > 0) {
           _deposit = _l.depositToReturn;
           _l.depositToReturn = 0;
        }
    }

    function stopGame() public {
        require(msg.sender==player);
        require(game_status);

        //increase deposit to return of leaser on unused balance

        if (balance > 0) {
            Leaser storage _l = leasers[player];
            _l.depositToReturn += balance;
            balance=0;
        }

        end = now;

        current_steps = 0;
        game_status = false;
    }

    function update_step () public {
        require(msg.sender==player);
        require(game_status);
        require(current_steps<steps);
        require(balance>price);

        amountForOwner = amountForOwner+price;
        balance = balance-price;
        current_steps = current_steps+1;
    }

    function take_amount_owner () public    {
        require(msg.sender==owner);
        require(amountForOwner>0);

        owner.transfer(amountForOwner);
        amountForOwner=0;
    }

    function take_deposit_player() public {
        uint256 _d = releasDeposit(msg.sender);
        if (_d > 0) {
            msg.sender.transfer(_d);
            balance=0;
        }
    }

    function status_game() public view returns (bool) {
        return game_status;
    }

    function current_balance_player() public view returns (uint256) {
        return balance;
    }

    function current_amount_owner() public view returns (uint256) {
        return amountForOwner;
    }

    function current_step() public view returns (uint256) {
        return current_steps;
    }

    function Rating_game() public view returns (uint256) {
        return rating;
    }

    function Game_time() public view returns (uint) {
        if (game_status==true) {
            end=now;
        }
        return end-start;
    }

    function add_balance () public payable   {
        require(msg.sender==player);
        require(game_status==true);
        require(msg.value > 0);

        player = msg.sender;
        balance += msg.value;
    }
}