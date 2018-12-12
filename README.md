# transfer-manager
A service for money transfers between accounts 

## Prerequisites
- Java 8
- Maven 3
- [Lombok](https://projectlombok.org/) IDE plugin   
(So your IDE could resolve Lombok generated methods) 


## Build
Build by Maven with unit and integration tests execution  

    mvn clean verify

## Run
Entry point is 

    org.zinaliev.transfermanager.Application.main 

Pass config file location as the first CLI argument, otherwise `resources/config.yaml` will be used 

## API
The main abstraction in the service is **wallet**. A wallet stores money in a single currency    
It's implied that wallets belong to some persons/accounts/cards but these relations are meant to be out of this service scope  
  

| Method        | Type   | URL                   | Request Body         | Response Body       |  
| :---:         | :---:  | :---:                 | :---:                | :---:               |
| Create wallet | POST   | /wallet/{id}          | WalletModel          | ResponseModel       |
| Get wallet    | GET    | /wallet/{id}          | -                    | WalletResponseModel |
| Update wallet | PATCH  | /wallet/{id}          | UpdateWalletModel    | ResponseModel       |
| Delete wallet | DELETE | /wallet/{id}          | -                    | ResponseModel       |
| Transfer      | POST   | /wallet/{id}/transfer | TransferModel        | ResponseModel       |

#### Models
WalletModel
```json
{
  "currencyCode": "RUB",
  "amount": 100.5
}
```

UpdateWalletModel
```json
{
  "amount": 100.5
}
```

TransferModel
```json
{
  "targetWallet": "f7461c77-8463-438d-bb4e-55073bff88bf",
  "amount": 100.5
}
```

ResponseModel

```json
{
  "message": "OK",
  "codeEx": 200
}

```

WalletResponseModel

```json
{
  "message": "OK",
  "codeEx": 200,
  "data": {
    "currencyCode": "RUB",
    "amount": 100.5
  }
}
```