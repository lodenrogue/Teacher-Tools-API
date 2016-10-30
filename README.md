# Teacher Tools API

REST API for creating tools to assist teachers

===

## How to use
1. [Teachers](#teachers)
    1. [Create](#create-a-teacher)
    2. [Get](#get-a-teacher)
    3. [Update](#update-a-teacher)

===

### Teachers

#### Create a teacher
POST: /api/v1/teachers

~~~
{
  "firstName": "John",
  "lastName": "Smith"
}
~~~

Definitions:

| Key       | Type   | Definition                |
|-----------|--------|---------------------------|
| firstName | String | This teacher's first name |
| lastName  | String | This teacher's last name  |


#### Expected output

##### Status: 201 CREATED

~~~
{
  "id": 3,
  "firstName": "John",
  "lastName": "Smith",
  "createdDate": 1477845792394,
  "modifiedDate": null
}
~~~

Definitions:

| Key          | Type     | Definition                               |
|--------------|----------|------------------------------------------|
| id           | Integer  | The teacher's resource id                |
| firstName    | String   | The teacher's first name                 |
| lastName     | String   | The teacher's last name                  |
| createdDate  | DateTime | The datetime this resource was created   |
| modifiedDate | DateTime | The date time this resource was modified |

##### Status: 422 UNPROCESSABLE ENTITY

~~~
{
  "missingFields": [
    "firstName",
    "lastName"
  ]
}
~~~

===

#### Get a teacher
GET: /api/v1/teachers/{id}

#### Expected output

##### Status: 200 OK

~~~
{
  "id": 3,
  "firstName": "John",
  "lastName": "Smith",
  "createdDate": 1477845792394,
  "modifiedDate": null
}
~~~

Definitions:

| Key          | Type     | Definition                               |
|--------------|----------|------------------------------------------|
| id           | Integer  | The teacher's resource id                |
| firstName    | String   | The teacher's first name                 |
| lastName     | String   | The teacher's last name                  |
| createdDate  | DateTime | The datetime this resource was created   |
| modifiedDate | DateTime | The date time this resource was modified |


##### Status: 404 NOT FOUND

~~~
{
  "message": "No teacher with id 2 found"
}
~~~

===

#### Update a teacher
PUT: /api/v1/teachers/{id}

~~~
{
  "firstName": "John",
  "lastName": "Smith",
  "createdDate": 1477782838000,
  "modifiedDate": null
}
~~~

#### Expected output

##### Status: 200 OK

~~~
{
  "id": 3,
  "firstName": "John",
  "lastName": "Smith",
  "createdDate": 1477845792394,
  "modifiedDate": 1477847289027
}
~~~

Definitions:

| Key          | Type     | Definition                               |
|--------------|----------|------------------------------------------|
| id           | Integer  | The teacher's resource id                |
| firstName    | String   | The teacher's first name                 |
| lastName     | String   | The teacher's last name                  |
| createdDate  | DateTime | The datetime this resource was created   |
| modifiedDate | DateTime | The date time this resource was modified |


##### Status: 404 NOT FOUND

~~~
{
  "message": "No teacher with id 2 found"
}
~~~

##### Status: 422 UNPROCESSABLE ENTITY

~~~
{
  "missingFields": [
    "firstName",
    "lastName",
    "createdDate"
  ]
}
~~~
