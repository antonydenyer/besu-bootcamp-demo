pragma solidity 0.5.9;

contract SimpleStorage {
    uint data;

    function set(uint value) public {
        data = value;
    }

    function get() public view returns (uint) {
        return data;
    }
}