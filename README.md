# <img src="https://raw.githubusercontent.com/Everc0in/JavaAPI/master/evercoin-logo.png" height="30" width="auto" >  Java library

[Evercoin](https://evercoin.com) is a an instant-access cryptocurrency exchange. This Java API enables you to easily integrate cryptocurrency exchange funtionality into your Java app.

## Requirements
- Java 1.6 or higher
- API Key // contact support@evercoin.com to obtain your API key

## Download

Download a version of the Evercoin java's jar from [JavaAPI](https://github.com/Everc0in/Download/raw/master/JavaAPI.jar) 

## Usage Example
```java
public class Test {

    public static void main(String[] args) throws InterruptedException {
        // Make sure to use test endpoint during development and testing.
        // System.setProperty("evercoin.api.endpoint", "https://test.evercoin.com/");
        final String API_KEY = "Your API Key"; // contact support@evercoin.com to obtain yours
        final String version = "v1";
        final String deposit = "BTC";
        final String destination = "ETH";
        final String refundMainAddress = "n4MZ1KydkCEokrr8fVAcGSghsSMjHvNexo";
        final String destinationMainAddress = "0xb306e1D76E4C4bd6462F370d4551F842eB4fFcad";
        final String depositAmount = "1.0";
        Evercoin evercoin = EvercoinFactory.create(new EvercoinApiConfig(API_KEY, version));
        CoinsResponse coins = evercoin.getCoins();
        Coin depositCoin = coins.getCoin(deposit);
        Coin destinationCoin = coins.getCoin(destination);
        if (depositCoin != null && !depositCoin.isFromAvailable()) {
            //Exchanging from BTC is currently available.
            return;
        }
        if (destinationCoin != null && !destinationCoin.isToAvailable()) {
            //Exchanging to ETH is currently available.
            return;
        }
        if (!evercoin.validateAddress(deposit, refundMainAddress).isValid()) {
            //Your BTC address is not valid.
            return;
        }
        Address refundAddress = new Address(refundMainAddress);
        if (!evercoin.validateAddress(destination, destinationMainAddress).isValid()) {
            //Your ETH address is not valid.
            return;
        }
        Address destinationAddress = new Address(destinationMainAddress);
        PriceResponse priceResponse = evercoin.getPrice(deposit, destination, new BigDecimal(depositAmount), null);
        if (priceResponse.isSuccess()) {
            OrderResponse orderResponse = evercoin.createOrder(priceResponse, refundAddress, destinationAddress);
            if (orderResponse.isSuccess()) {
                System.out.println("You should deposit to this address: " + orderResponse.getDepositAddress().getMainAddress());
                while (true) {
                    Thread.sleep(10000);
                    StatusResponse statusResponse = evercoin.getStatus(orderResponse.getOrderId());
                    if (statusResponse.isSuccess()) {
                        if (statusResponse.getExchangeStatus().getId() == Status.Awaiting_Deposit.getId()) {
                            System.out.println("Send " + depositAmount + " " + deposit + " to the " + orderResponse.getDepositAddress().getMainAddress());
                        } else if (statusResponse.getExchangeStatus().getId() == Status.Awaiting_Confirm.getId()) {
                            System.out.println("Looks like you sent "+ deposit + ". Waiting for confirmation on the blockchain.");
                        } else if (statusResponse.getExchangeStatus().getId() == Status.Awaiting_Exchange.getId()) {
                            System.out.println("Your " + destination + " is on the way.");
                        } else if (statusResponse.getExchangeStatus().getId() == Status.All_Done.getId()) {
                            System.out.println("Success! Enjoy your " + destination);
                            return;
                        }
                    }
                }
            } else {
                System.err.println("There is an error in order creation: " + orderResponse.getError());
            }
        }
    }
}
```
## More Info

More information and API documentation can be found at https://evercoin.com/api/

## Getting help

Please contact support@evercoin.com
