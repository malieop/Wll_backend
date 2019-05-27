# wll-backend

https://start.spring.io

#Users Organization

To create a User is needed, for example, the following Json Structure Information : 
{
        "name": "Jorge Sampaio",
        "location": {
            "location": "Avenida dos Aliados",
            "city": "Porto",
            "latitude": "41,1484",
            "longitude": "-8,6106"
        },
        "company": {
            "name": "PizzaHut",
            "email": "jorge@pizzahut.com",
            "job": "Cozinheiro",
            "startDate": "10/10/10"
        },
        "username": null,
        "password": null,
        "birthdate": null,
        "course": {
            "name": "Nutrição",
            "university": "Faculdade de Engenharia",
            "startDate": "2003",
            "endDate": "2008"
        },
        "email": "up201899999@fe.up.pt"
    }
    
To Each user is atached a status field which declares if the user is already validated(1) or not(0), or even ban(2).

##swagger
http://localhost:8080/swagger-ui.html
http://localhost:8080/browser/index.html#/

http://localhost:8080/actuator/health



