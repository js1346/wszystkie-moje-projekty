pragma solidity >=0.6.0 <0.7.0;

contract counter{

    uint256 counteer=0;

    function increment() public{
        counteer ++;
    }

    function decrement() public{
        require(counteer>0);
        counteer--;
    }

    function get_count() view public returns(uint256){
        return counteer;
    }

}
