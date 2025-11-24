# Build Instructions

## Prerequisites

Before building the project, ensure you have the following installed:

1. **Java Development Kit (JDK) 11 or higher**
   - Download from: https://adoptium.net/ or https://www.oracle.com/java/technologies/downloads/
   - Verify installation: `java -version` and `javac -version`

2. **Apache Maven 3.6 or higher**
   - Download from: https://maven.apache.org/download.cgi
   - Verify installation: `mvn -version`

## Building the Project

### Option 1: Using Maven (Recommended)

1. **Clean and compile the project:**
   ```bash
   mvn clean compile
   ```

2. **Package the application:**
   ```bash
   mvn clean package
   ```
   This creates a JAR file in the `target/` directory.

3. **Run the application:**
   ```bash
   mvn exec:java -Dexec.mainClass="com.sms.Main"
   ```

4. **Run tests:**
   ```bash
   mvn test
   ```

### Option 2: Manual Compilation

If Maven is not available, you can compile manually:

1. **Download dependencies:**
   You'll need to manually download the following JAR files:
   - postgresql-42.7.1.jar
   - gson-2.10.1.jar
   - dotenv-java-3.0.0.jar
   - slf4j-api-2.0.9.jar
   - slf4j-simple-2.0.9.jar

2. **Place JAR files in lib/ directory**

3. **Compile the source code:**
   ```bash
   javac -cp "lib/*" -d target/classes -sourcepath src/main/java src/main/java/com/sms/**/*.java
   ```

4. **Run the application:**
   ```bash
   java -cp "target/classes:lib/*" com.sms.Main
   ```

## Database Setup

Before running the application:

1. Create a Supabase account at https://supabase.com
2. Create a new project
3. The database schema is automatically applied via migrations
4. Update the `.env` file with your Supabase credentials:
   ```
   SUPABASE_URL=your_supabase_url
   SUPABASE_ANON_KEY=your_supabase_anon_key
   ```

## Running the Application

After successful build:

```bash
mvn exec:java -Dexec.mainClass="com.sms.Main"
```

Or if you packaged the application:

```bash
java -jar target/student-management-system-1.0.0.jar
```

## Troubleshooting

### Maven not found
- Ensure Maven is installed and added to your PATH
- Try: `export PATH=$PATH:/path/to/maven/bin`

### Java version issues
- Check Java version: `java -version`
- Ensure you're using Java 11 or higher
- Update JAVA_HOME environment variable if needed

### Database connection errors
- Verify `.env` file contains correct Supabase credentials
- Check internet connectivity
- Ensure Supabase project is active

### Compilation errors
- Clean the project: `mvn clean`
- Update dependencies: `mvn dependency:resolve`
- Check for any syntax errors in the code

## Development Mode

For active development:

```bash
mvn clean compile && mvn exec:java -Dexec.mainClass="com.sms.Main"
```

This cleans, compiles, and runs the application in one command.

## IDE Setup

### IntelliJ IDEA
1. Open the project directory
2. IntelliJ will automatically detect the Maven project
3. Wait for dependency resolution
4. Run `Main.java` from the IDE

### Eclipse
1. Import as Maven project
2. Right-click on project > Maven > Update Project
3. Run as Java Application (Main class: com.sms.Main)

### VS Code
1. Install Java Extension Pack
2. Open project folder
3. Use Command Palette > Java: Run

## Notes

- The first build may take longer due to dependency downloads
- Ensure `.env` file is properly configured before running
- Check `application.log` for detailed runtime logs
- The database schema is created automatically via Supabase migrations
