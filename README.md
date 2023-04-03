# PixPayView (Backend)
> This repository is the backend of the PixPayView application that enables you to easily and quickly include individuals or companies to integrate PIX into your business and your employee to control whether the payment was approved or not, by the method used at this time, there are no paid market fees.

<img src="https://i.imgur.com/SdCYr1F.gif" alt="PixPayView image illustration">

The initial focus of the project is that it is Self-Hosted and anyone can use it quickly and easily, below I will list information about the API documentation.

See about the frontend: https://github.com/VictorBren0/pixpayview-frontend

## 📝 Documentation:
All requests must be in JSON, respecting the RESTful API standard

#### Authentication `(/auth)`
| Method | Url          | Description    | How to use                   |
|--------|--------------|----------------|------------------------------|
| POST   | /auth/login  | Login account  | [[VER MAIS]](#create-report) |
| POST   | /auth/signup | Create account | [[VER MAIS]](#list-reports)  | 
| POST   | /auth/logout | Logout Account | [[VER MAIS]](#list-reports)  | 
| POST   | /auth/token  | Refresh token  | [[VER MAIS]](#list-reports)  | 

#### Users `(/users)`
| Method | Url                  | Description    | How to use                   |
|--------|----------------------|----------------|------------------------------|
| GET    | /users/list          | List accounts  | [[VER MAIS]](#create-report) |
| PUT    | /users/{id}/settings | Edit account   | [[VER MAIS]](#list-reports)  | 
| DELETE | /users/{id}/delete   | Delete account | [[VER MAIS]](#list-reports)  |  

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