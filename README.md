# <img src="https://raw.githubusercontent.com/Everc0in/JavaAPI/master/evercoin-logo.png" height="30" width="auto" >  Java library

Evercoin is a an instant-access cryptocurrency exchange. And this library is for the crypto currency buying and selling using Evercoin service

## Requirements
- Java 1.6 or higher
- API Key // 

## Download

Download a version of the Evercoin java's jar from [JavaAPI](https://github.com/Everc0in/Download/raw/master/JavaAPI.jar) 

## Usage Example
```java
public class Test {

    public static void main(String[] args) throws InterruptedException {
        //System.setProperty("evercoin.api.endpoint", "https://test.evercoin.com/");
        final String API_KEY = "Your API Key"; 
        final String version = "v1";
        final String from = "ETC";
        final String to = "ETH";
        final String fromAddress = "0x4f78e407b312e6dde8af699ca73b7c15dddfea42";
        final String toAddress = "0x4f78e407b312e6dde8af699ca73b7c15dddfea42";
        final String fromAmount = "2.0";
        Evercoin evercoin = EvercoinFactory.create(new EvercoinApiConfig(API_KEY, version));
        CoinsResponse coins = evercoin.getCoins();
        Coin fromCoin = coins.getCoin(from);
        Coin toCoin = coins.getCoin(to);
        if (!fromCoin.isFrom()) {
            //Evercoin is not buying ETH now.
            return;
        }
        if (!toCoin.isTo()) {
            //Evercoin is not selling ETC now.
            return;
        }
        if (!evercoin.validateAddress(from, fromAddress).isValid()) {
            //ETH address is not valid.
            return;
        }
        Address refundAddress = new Address(fromAddress);
        if (!evercoin.validateAddress(to, toAddress).isValid()) {
            //ETC address is not valid.
            return;
        }
        Address destinationAddress = new Address(toAddress);
        PriceResponse priceResponse = evercoin.getPrice(from, to, new BigDecimal(fromAmount), null);
        if (priceResponse.isSuccess()) {
            OrderResponse orderResponse = evercoin.createOrder(priceResponse, refundAddress, destinationAddress);
            if (orderResponse.isSuccess()) {
                StatusResponse statusResponse = evercoin.getStatus(orderResponse.getOrderId());
                System.out.println("You should deposit to this address: " + orderResponse.getDepositAddress().getMainAddress());
                while (true) {
                    Thread.sleep(10000);
                    if (statusResponse.isSuccess()) {
                        System.out.println(statusResponse.getExchangeStatus().getText());
                        if (statusResponse.getExchangeStatus().getId() == Status.All_Done.getId()) {
                            //Enjoy your ETC
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

More information and API documentation can be found at https://evercoin.com/API/

## Getting help

Please contact support@evercoin.com
