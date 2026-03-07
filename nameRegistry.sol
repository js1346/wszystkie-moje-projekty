//SPDX-License-Identifier:MIT
pragma solidity ^0.6.4;

contract nameregistry{
    
    mapping( string => address ) public order;

    function setName(string memory names) public{
        require(order[names]==address(0));
        order[names]=msg.sender;
        
    }

    function getAdress(string memory names) view public returns(address){
            return order[names];
    }



}
