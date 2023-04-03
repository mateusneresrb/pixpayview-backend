# PixPayView (Backend)
> This repository is the backend of the PixPayView application that enables you to easily and quickly include individuals or companies to integrate PIX into your business and your employee to control whether the payment was approved or not, by the method used at this time, there are no paid market fees.

<img src="https://i.imgur.com/SdCYr1F.gif" alt="PixPayView image illustration">

The initial focus of the project is that it is Self-Hosted and anyone can use it quickly and easily, below I will list information about the API documentation.

See about the frontend: https://github.com/VictorBren0/pixpayview-frontend

## 📝 Documentation:
All requests must be in JSON, respecting the RESTful API standard

#### Authentication `(/auth)`
| Method | Url          | Description    | How to use               |
|--------|--------------|----------------|--------------------------|
| POST   | /auth/login  | Login account  | [[VER MAIS]](#login)     |
| POST   | /auth/signup | Create account | [[VER MAIS]](#signup)    | 
| POST   | /auth/logout | Logout Account | [[VER MAIS]](#logout)    | 
| POST   | /auth/token  | Refresh token  | [[VER MAIS]](#authtoken) | 

#### Users `(/users)`
| Method | Url                  | Description    | How to use                    |
|--------|----------------------|----------------|-------------------------------|
| GET    | /users/list          | List accounts  | [[VER MAIS]](#user-list)      |
| PUT    | /users/{id}/settings | Edit account   | [[VER MAIS]](#edit-settings)  | 
| DELETE | /users/{id}/delete   | Delete account | [[VER MAIS]](#delete-account) |  

#### Transactions `(/transactions)`
| Method | Url                       | Description        | How to use                   |
|--------|---------------------------|--------------------|------------------------------|
| GET    | /transactions/list        | List transactions  | [[VER MAIS]](#create-report) |
| GET    | /transactions/{id}        | View transaction   | [[VER MAIS]](#list-reports)  | 
| POST   | /transactions/{id}/create | Create transaction | [[VER MAIS]](#list-reports)  | 
| DELETE | /transactions/{id}/delete | Delete transaction | [[VER MAIS]](#list-reports)  |

#### Settings `(/settings)`
| Method | Url                     | Description          | How to use                   |
|--------|-------------------------|----------------------|------------------------------|
| GET    | /settings/token         | View payment token   | [[VER MAIS]](#create-report) |
| PUT    | /settings/token/update  | Update payment token | [[VER MAIS]](#list-reports)  | 

#### Exception table:
| Code | Name                          | Description                                                           |
|------|-------------------------------|-----------------------------------------------------------------------|
| 3000 | AccountAlreadyExistsException | When you already have an account with the registered                  |
| 3001 | EmailAlreadyExistsException   | When changing account infos, when using an already used email address |
| 4000 | AccountNotExistsException     | When the account does not exist                                       |
| 4001 | PaymentTokenNotFoundException | When the payment token is not defined                                 |
| 4002 | TransactionNotExistsException | When the queried transaction does not exist                           |
| 5000 | BadRequestException           | When you make a request without following specifications              |
| 7000 | ForbiddenException            | When you don't have enough permission                                 |
| 7001 | PaymentTokenInvalidException  | When the payment token is refused by the Mercado pago                 |

### API Examples:

<a id="login">Login (Request):</a>
```bash
curl -X POST \
  http://localhost:8080/auth/login \
  -H 'Content-Type: application/json' \
  -d '{
	"email": "example@pixpayview.com",
	"password": "pass1234"
  }'
```
<a id="signup">SignUP (Request):</a> `Only ROLE_ADMIN authenticated`
```bash
curl -X POST \
  http://localhost:8080/auth/signup \
  -H 'Content-Type: application/json' \
  -d '{
	"email": "example@pixpayview.com",
	"password": "pass1234",
	"name": "Mateus Neres",
	"role": "ROLE_USER"
  }'
```
<a id="logout">Logout:</a> 
```bash
curl -X POST \
  http://localhost:8080/auth/logout \
  -H 'Content-Type: application/json'
```
<a id="authtoken">Refresh token:</a>
```bash
curl -X POST \
  http://localhost:8080/auth/token \
  -H 'Content-Type: application/json'
```
---
<a id="list-users">List users</a> `Only ROLE_ADMIN authenticated`
```
curl -X 'GET' \
  'http://localhost:8080/users/list' \
  -H 'accept: application/json'
```
<a id="update-settings">Update Settings:</a> `Some values are optional and need permission.`
```bash
curl -X PUT \
  http://localhost:8080/auth/{id}/settings \
  -H 'Content-Type: application/json' \
  -d '{
	"email": "example@pixpayview.com",
	"password": "pass1234",
	"name": "Mateus Neres",
	"role": "ROLE_USER"
  }'
```
<a id="delete-account">Delete account:</a> `Only ROLE_ADMIN authenticated`
```bash
curl -X DELETE \
  http://localhost:8080/auth/{id}/delete \
  -H 'Content-Type: application/json'
```
---


//EXAMPLES CADA PARTE...

In construction...

## 🚀 How to use?

In construction...

## 🤝 Collaborators

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/mateusneresrb">
        <img src="https://avatars.githubusercontent.com/u/52140952?v=4" width="100px;" alt="Photo of Mateus Neres"/><br>
        <sub>
          <b>Mateus Neres</b>
        </sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/VictorBren0/">
        <img src="https://avatars.githubusercontent.com/u/87786280?v=4" width="100px;" alt="Photo of Mateus Neres"/><br>
        <sub>
          <b>Victor Breno</b>
        </sub>
      </a>
    </td>
  </tr>
</table>