# **Event Planner's Dashboard**

## **Table of Contents**
1. [Project Overview](#project-overview)
2. [Environment Setup](#environment-setup)
3. [Installation and Running the Project](#installation-and-running-the-project)
4. [File Structure](#file-structure)
5. [Core Features](#core-features)
6. [Design Patterns and Architecture](#design-patterns-and-architecture)
7. [APIs Used](#apis-used)
8. [Saved Preferences and History](#saved-preferences-and-history)
9. [Contributors](#contributors)

---

## **Project Overview**
The **Event Planner's Dashboard** is a desktop application designed to assist event planners in selecting the best dates and locations for outdoor events based on real-time weather forecasts and air quality data. The dashboard retrieves data from two separate APIs and presents it to the user through visualizations like line graphs, bar charts, and a color-coded calendar. Users can save their preferred configurations and access their history for future analysis.

### **Key Features:**
- **Real-time Weather Data**: Visualizations for temperature, humidity, wind speed, and precipitation.
- **Air Quality Analysis**: Tracks PM2.5 and PM10 levels in different locations.
- **Customizable Filters**: Users can customize parameters, save preferences, and view historical data.
- **Preferences and History**: Store and revisit previous configurations.

---

## **Environment Setup**
To run this application, ensure you have the following setup on your machine:

### **1. Prerequisites:**
- **Java Development Kit (JDK)**: Version 8 or higher.
- **JavaFX**: For building and rendering the UI.
- **IDE**: Use an IDE such as IntelliJ IDEA, Eclipse, or VS Code with Java support.
- **Maven**: For managing project dependencies.
- **Git**: To clone the project repository.

### **2. Required Libraries:**
The project uses the following libraries, which are managed through `pom.xml` in Maven:

- `org.json` for handling JSON data.
- `javafx.controls` for UI components.
- `javafx.fxml` for building the interface.
- `httpclient` for making API calls.

### **3. Environment Setup:**
1. Ensure JDK is installed by running:
   ```bash
   java -version
   ```
2. Install Maven:
   ```bash
   mvn -version
   ```

### **4. Additional Requirements:**
- A stable internet connection to fetch real-time weather and air quality data from the APIs.

---

## **Installation and Running the Project**

### **1. Clone the Repository:**
```bash
git clone <repository-url>
```

### **2. Navigate to the Project Directory:**
```bash
cd event-planners-dashboard
```

### **3. Build the Project:**
- Use Maven to build the project and resolve dependencies:
  ```bash
  mvn clean install
  ```

### **4. Run the Application:**
- To compile and execute the application, use:
  ```bash
  mvn javafx:run
  ```

- Alternatively, you can open the project in your preferred IDE and run the `Main.java` file.

### **5. Running the Application (Without Maven):**
- If you are not using Maven, ensure all required libraries are in your classpath and run:
  ```bash
  javac -cp ".:path_to_libraries/*" Main.java
  java -cp ".:path_to_libraries/*" Main
  ```

---

## **File Structure**

```
event-planners-dashboard/
├── README.md
├── pom.xml                 # Maven project file
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── controllers/
│   │   │   ├── models/
│   │   │   └── Main.java
│   │   └── resources/
│   └── test/               # Test files (if any)
├── docs/
│   └── design_document.md  # Detailed design document
└── assets/
    └── images/             # UI assets and icons
```

### **Explanation:**
- **`src/main/java/`**: Contains the core Java source code.
- **`controllers/`**: Manages the logic and interaction between the views and models.
- **`models/`**: Handles data processing and API calls.
- **`resources/`**: Stores FXML files for UI layout.
- **`docs/`**: Contains the design document and any additional documentation.
- **`assets/`**: Contains static assets such as images and icons.

---

## **Core Features**
1. **Location and Date Picker**: Dropdown menu to select regions and date range.
2. **Weather Visualizations**: Line graphs showing weather trends over the selected date range.
3. **Air Quality Visualizations**: Bar charts for air quality metrics (PM2.5, PM10).
4. **User Preferences**: Save preferred locations, date ranges, and settings for future use.
5. **History Tracking**: View previously viewed configurations and compare past trends.

---

## **Design Patterns and Architecture**
The application uses a **Model-View-Controller (MVC)** architecture, which separates backend logic, user interface, and control flow for better maintainability.

### **Design Patterns Used:**
1. **MVC Pattern**: For separating data, control flow, and presentation.
2. **Singleton Pattern**: Ensures only one instance of each API handler is active.
3. **Factory Pattern**: Handles object creation related to API requests.
4. **Observer Pattern**: (Optional) For live updates to UI components.

---

## **APIs Used**
The project uses two APIs:

1. **Weather API**:
   - Finnish Meteorological Institute’s Open Data API and OpenWeather API.
   - Provides real-time weather data: temperature, humidity, wind speed, and precipitation.
   
   Example Endpoint:
   ```
   GET https://opendata.fmi.fi/weather/forecast?location=<location>
   ```

2. **Air Quality API**:
   - Open AQ Platform API.
   - Provides real-time air quality data, including PM2.5 and PM10 levels.

   Example Endpoint:
   ```
   GET https://api.openaq.org/v1/latest?coordinates=<latitude,longitude>
   ```

---

## **Saved Preferences and History**

### **Saved Preferences:**
Users can store commonly used configurations, making it easy to reload their preferred settings.

- **Fields Saved**: Preference Name, Location, Date Range, and Metrics.
- **Actions**: Load, Edit, Delete.

### **History:**
Shows previously viewed configurations for easy comparison.

- **Fields Tracked**: Timestamp, Location, Date Range, Weather and Air Quality Summary.
- **Actions**: View Again, Compare.

---

## **Contributors**
Team Members:
- Janika Ranta
- Affira
- Zhiqing Feng
- Naveed Ali

