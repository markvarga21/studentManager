<div id="top"></div>

<br />
<div align="center">

<h1 align="left">User manager</h1>

  <p align="center">
    This application lets authorized people to register users into a database.
    <br />
    <a href="https://github.com/markvarga21/userManager"><strong>Explore the docs »</strong></a>
    <br />
    <a href="https://github.com/markvarga21/userManager/issues">Report Bug</a>
    ·
    <a href="https://github.com/markvarga21/userManager/issues">Request Feature</a>
  </p>
</div>


<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#known-bugs">Known bugs</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>

## About The Project

<i>This repository is just a part of the whole application, but for easing its usage and the overall installation, the documentation, the features and the necessarry steps are listed and presented in this README below.</i>

### - User manager API -
This Spring Boot application lets you to manage users. It can store their name, address, birthdate, birthplace, phone number, email, gender and nationality.

It also provides accurate validation on the entered data. It allows to upload ID document or passport and also a selfie. The application uses <a href="https://azure.microsoft.com/en-gb/products/form-recognizer">Azure's Form Recognizer</a> service to validate the entered data against the data on the ID document. 

In addition, it validates the photo on the ID card against the uploaded selfie taken by the user. For this, it uses a custom made <b>Face API</b> which is based on <a href="https://github.com/serengil/deepface">Deepface</a>.

On the persistence part, the application uses a MySQL database which is being hosted in the cloud, using Azure's services.

Also, the application follows the styling rules listed in the ```checkstyle.xml``` file, thus resulting a more readable and extendable codebase. The main service class was also tested using Mockito.

Finally, the whole application is fully containerized and can be found in <a href="https://hub.docker.com/r/markvarga21/usermanager">this</a> Docker repository (more on this later). 

---
### - Face API -

This service was built using Flask on the backend side and Deepface on the service side. It is used for face-to-face comparison and validation.

<a href="https://github.com/markvarga21/faceApi">Link to the Face API repository</a>

---

### - Frontend -

This connects the applications backend with the user. It is built using ReactJS and for the styling Tailwind CSS. The frontend stores the uploaded selfie and displays it in the table row. This is achieved using <a href="https://firebase.google.com/docs/storage">Firebases's Storage</a> service.

The frontend also has the ability to inform the user about an error or exception in a custom error panel. In case, it is displayed on the user addition panel.

Finally, there is a minimal authentication service which is also provided by <a href="https://firebase.google.com/docs/auth">Firebase</a>. For accessing the application please reach out to me, for granting access to the application, because its strictly restricted to the previously added users by me.

<a href="https://github.com/markvarga21/userManagerFrontend">Link to the frontend repository</a>

<p align="right">(<a href="#top">back to top</a>)</p>

### Built With
**API**
* [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Maven](https://maven.apache.org/)
* [Azure Form Recognizer](https://azure.microsoft.com/en-gb/products/form-recognizer)
* [Azure Database for MySQL](https://azure.microsoft.com/en-us/products/mysql)
* [Mockito](https://site.mockito.org/)
* [Docker](https://www.docker.com/)

**Face API**
* [Python 3.11](https://www.python.org/)
* [Flask](https://flask.palletsprojects.com/en/2.3.x/)
* [Deepface](https://github.com/serengil/deepface)
* [Docker](https://www.docker.com/)

**Frontend**
* [React JS](https://spring.io/projects/spring-boot)
* [Tailwind CSS](https://spring.io/projects/spring-boot)
* [Firebase](https://spring.io/projects/spring-boot)
* [Docker](https://www.docker.com/)

<p align="right">(<a href="#top">back to top</a>)</p>

## Screenshots

TODO

## Getting Started

In this section you can see a step-by-step guide in order to start and use the application.

### Prerequisites

Here you can find the only, but the most important one, prerequisity to run the application:
* **Docker**
<br>See: https://www.docker.com/


### Installation

1. Install Docker desktop and then open it.
    <br>See: https://docs.docker.com/desktop/install/windows-install/
2. Pull the **Face API** image from Docker Hub
   ```
   docker pull markvarga21/faceapi
   ```
3. Pull the main **API** image
    ```
   docker pull markvarga21/usermanager
    ```
4. Pull the **Frontend** image
   ```sh
   docker pull markvarga21/usermanagerfrontend
   ```
5. Build the docker container using the provided ```docker-compose.yaml```
   ```sh
   docker compose up --detach
   ```
   This file is provided by me or by an admin for safety reasons.
6. Open the page in any browser using the link below
    ```
    http://localhost:3000/
    ```
    It could take some time for the application to start up, so it is adviced to wait a minute or so before opening this link.

<p align="right">(<a href="#top">back to top</a>)</p>

<div id="known-bugs"></div>

## Known Bugs
As any application, this app contains some bugs and some sensitive parts which are:
- The application is not yet fully implemented for verifying nationality on the passports, because the nationality part of the passports vary too much around the world.
    - Also, there could be problems with analyzing a passport, but hungarian ID documents work well. Thus, there is not any option for selecting the ID document type on the frontend yet.
- The app is tested only on hungarian ID cards, because not many valid ID card can be found on the web.
- For the selfies to load with the other data in the table there is a need to refresh the page before them showing up.
- When refreshing the page, the login page comes in for a slight second then dissapears if the user is logged in.
- If the user does not log out from the app, he/she will be remained logged in.
- The Face API uses my custom made backend service for comparing and validating faces, because of the lack of permission on the Azure Face API service.
- If the two addresses (birth place and living place) are the same, two entities will be saved in the database instead of one.
- It takes some time until the application is fully functional without lagging.
- It takes a bit long for the first user addition, because the Face API's Deepface models have to be downloaded and initialized before use.
- The backend API is not fully tested.
- The application is not deployed yet in a cloud service provider.
- There could be UI/UX inconsistencies when using the application.
- Lack of registration option and authorization services.

## Contact

József Márk Varga - vmark2145@gmail.com

<p align="right">(<a href="#top">back to top</a>)</p>