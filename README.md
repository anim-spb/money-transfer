# Money Transfer

RESTful API (including data model and the backing implementation) for money transfers between accounts.

PUT, DELETE methods are omitted intentionally for the sake of simplicity. Same reason for only base fields are present.

## Requirements

Java 8  
Maven 3+

## Usage

mvn clean package  
java -jar target/money-transfer-*.jar [-p PORT]

-p PORT: port to listen, default: 8080 

## API

### Accounts    

```javascript
GET /accounts			// get all accounts

GET /accounts/{id}  		// get account by id (UUID)

POST /accounts			// create acount with initial balance
```

#### Payload Example:
```javascript
{
	"id":"2fd959ba-392b-439e-8091-05991b1839af",  			// OUT: transfer id (UUID)
	"requestId":"564929eb6164",					// IN/OUT: client-generated requestId unique for [fromAccountId,toAccountId] set (string)
	"fromAccountId":"6f29921b-33ec-4b55-a413-7697b47776f8",		// IN/OUT: account id to transfer from (UUID)
	"toAccountId":"c485c061-bf1d-4813-9c8e-9964caf900ce",   	// IN/OUT: account id to transfer to (UUID)
	"amount":1250.42,						// IN/OUT: amount to transfer (decimal)
	"createdAt":"2017-03-25T12:37:20Z",				// OUT: time transfer started (date/time)	
	"state":"FAILED",						// OUT: transfer state	(string)
	"stateReason":"INSUFFICIENT_FUNDS"				// OUT: reason for transfer state (only for incompeted)	(string)
}
```

#### Response statuses: 
**201 Created** - transfer sucessfully created and completed  
**202 Acepted** - incomplete transfer (PROCESSING, FAILED)  
**409 Conflict** - non-unique [requestId, fromAccountId, toAccountId] set  


### Transfers
```javascript
GET /transfers			// get all transfers  

GET /transfers/{id} 		// get transfer by id (UUID) 

POST /transfers              	// make new transfer   
```

#### Payload Example:
```javascript
{
	"id":"6f29921b-33ec-4b55-a413-7697b47776f8",	 		// OUT: account id (UUID)
	"owner":"Mandy Moo"						// IN/OUT: owner's full name (string)
	"balance":2000.50,						// IN/OUT: initial balance (decimal)
	"createdAt":"2017-03-23T15:37:25Z"				// time account created (date/time)
}
```

#### Response statuses: 
**201 Created** - account sucessfully created

### Common mean statuses:
**200 OK** - successfull retrieval  
**400 Bad Request** - non-valid input  
**404 Not found** - entity doesn't exist