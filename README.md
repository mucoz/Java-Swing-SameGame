# SameGame (Java Swing)

SameGame java implementation

![java_DleoWvBCaz](https://github.com/user-attachments/assets/eb14f665-5d87-4186-926c-163b73f72613)

## Prerequisites

- **Java Development Kit (JDK)**: Make sure you have JDK 8 or higher installed. You can download it from [Oracle's website](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) or use an open-source alternative like [OpenJDK](https://openjdk.java.net/).

- **Maven** (optional): If you are using Maven to manage dependencies and build the project. Download it from [Maven's website](https://maven.apache.org/download.cgi).

## Compilation

1. **Clone the Repository**

   ```sh
   git clone https://github.com/mucoz/Java-Swing-SameGame.git  
    ```

2. **Compile the application**
   
   If you're using Maven, you can compile the code using:

    ```sh
    mvn compile
    ```
   Alternatively, you can compile the Java files manually using javac. Navigate to the directory containing your .java files and run:

   ```sh
   javac -d bin src/main/java/*.java
   ```
   If you used the second method, copy "images" folder from "src/main/resources" to "bin" folder after compiling the files. 

2. **Converting to JAR (Optional)**

    Since there is MANIFEST.MF file inside the project folder, you can use the following command to convert the compiled files into "jar".

    ```sh
    jar cfm SameGame.jar MANIFEST.MF -C bin . -C src\main\resources .
    ```
    To run the jar file, use the command below :

    ```sh
    java -jar SameGame.jar
    ```

   
## Run the Application
If you used Maven to build the project, you can run the application with:
```sh
mvn exec:java
```
    
    
If you compiled manually, navigate to the bin directory and run:
```sh
java Main
```




## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
