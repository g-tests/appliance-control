Appliance Control 
=================

> #### Assignment
> The task is to design and implement a backend service to control an appliance such as a wash machine or an oven. The API should be REST
> based and the state of the appliance should be persisted to any form of persistent storage. There is no need for any frontend but we expect there
> to be a README.md file with build directions and examples of how to invoke the REST API (e.g. curl).
> The project should be implemented using Java or Node.js. Feel free to use any 3rd party library that you are comfortable with. Unit tests are
> expected and the assignment will be assessed based on good programming practices and design.
> Please use GitHub or Bitbucket to publish your source code.

## Building
The project is built with Gradle 4.5. To build it from sources, use gradle wrapper supplied with the sources:
```
   $ ./gradlew build    # for linux
   $ gradlew.bat build  # for windows
```

## IDE integration
You can use [the IDEA gradle plugin](https://docs.gradle.org/current/userguide/idea_plugin.html) to generate idea project files:
```
    $ ./gradlew idea    # for linux
    $ gradlew.bat idea  # for windows
```

## Running
The project uses Java 8 as target platform. Make sure to use 8 version runtime environment when starting the application.

The project uses Spring Boot framework. That means the application 
can be launched in a number of ways. 
The recommended way is to use generated jar file directly as it contains API documentation:
```
    $ java -jar build/libs/appliance-control-0.1.0.jar
```

Optionally, you can use gradle `bootRun` task or your favorite IDE integration plugin to run it as any other Boot/gradle project.

## API documentation

Asciidoc documentation with details on API resources and calls is packaged 
into the application jar and can be found at [http://localhost:8080/docs/api-docs.html](http://localhost:8080/docs/api-docs.html)

## Further development
Further development may include:
- making json schema exist as sub-resources of an owning entity 
- OpenAPI integration to document the api in a standardized way
- stable pagination implementation for resource listing
- support for [JSON Merge Patch RFC](https://tools.ietf.org/html/rfc7396) in CRUD controllers