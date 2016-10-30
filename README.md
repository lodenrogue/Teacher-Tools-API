# Teacher-Tools-API

REST API for creating tools to assist teachers

===

## How to use

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

Status: 201 CREATED

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
