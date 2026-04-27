# Cangqiong Takeout

## Project Introduction
Cangqiong Takeout is a customized software product designed for catering enterprises, comprising a system management backend and a mobile application. The system management backend is primarily used by internal staff to manage restaurant categories, dishes, set meals, orders, and employees, while also providing multi-dimensional data statistics and report export functionalities. The mobile application is targeted at consumers, allowing them to browse menus, add items to the shopping cart, and place orders online.

## Tech Stack
This project adopts a decoupled front-end and back-end architecture.

* **Backend Tech Stack:**
    * **Core Framework:** Spring Boot
    * **Persistence Layer:** MyBatis / MyBatis-Plus
    * **Database:** MySQL
    * **Cache:** Redis
    * **Report Export:** Apache POI
    * **Other Tools:** JWT (Authentication), Swagger/Knife4j (API Documentation), Lombok
* **Frontend Tech Stack (Reference):**
    * **Admin Panel:** Vue.js + ElementUI
    * **User Client:** WeChat Mini Program (Native)

---

## Core Modules

### 1. Data Statistics & Report Module (Core Highlight)
Provides comprehensive business data analysis to assist merchants in operational decision-making.
* **Turnover Statistics:** Supports custom time ranges, displaying daily turnover trends via line charts.
* **User Statistics:** Tracks daily new users and total users to grasp user growth trends.
* **Order Statistics:** Displays valid order volume and order completion rates across multiple dimensions.
* **Sales Ranking:** Calculates the Top 10 selling dishes/set meals within a specified period.
* **Excel Report Export:** Integrates Apache POI to support one-click exporting of nearly 30 days of operational data into formatted Excel reports, facilitating offline analysis and archiving for financial and operational staff.

### 2. Employee & Permission Management
* Employee login and logout functionalities.
* CRUD operations for employee information, paginated display, and account status toggling (enable/disable).

### 3. Dish & Set Meal Management
* **Category Management:** Maintenance of dish categories and set meal categories.
* **Dish Management:** Maintenance of dishes and their associated flavors, supporting image uploads and on-sale/off-sale status toggling.
* **Set Meal Management:** Maintenance of set meal combinations containing multiple dishes.

### 4. Order Management
* **User Client:** Browse menus, add to shopping cart, proceed with WeChat Pay, query historical orders, and cancel orders.
* **Admin Panel:** Real-time monitoring of order statuses, including taking, rejecting, delivering, and completing orders.

---

## Environment Setup & Running

### 1. Environment Requirements
* JDK 1.8+
* MySQL 5.7+
* Redis 5.0+
* Maven 3.6+

### 2. Database Preparation
1. Log into MySQL and create a database named `sky_take_out`.
2. Execute the initialization script `sky.sql` provided in the `sql` directory of the project.

### 3. Startup Steps

**Clone the project:**
```bash
git clone [https://github.com/g2jcrm57zv-hue/sky-takeout](https://github.com/g2jcrm57zv-hue/sky-takeout)
```

**Modify Configuration:**
Update the configuration information in the `sky-server/src/main/resources/application-dev.yml` file:
* MySQL database connection address, username, and password.
* Redis connection address and password.
* WeChat Mini Program related `appid` and `secret` (if you need to test the mini program login and payment features).

**Startup & Run:**
1. Start the Redis service.
2. Locate the startup class `SkyApplication` under the `sky-server` module and run the `main` method.
3. **Access API Docs:** Upon successful startup, visit `http://localhost:8080/doc.html` in your browser to view the backend API documentation.

---

## Directory Structure
```text
sky-take-out
├── sky-common       # Common module (utils, constants, exception handling, etc.)
├── sky-pojo         # Entity class module (DTO, VO, Entity, etc.)
└── sky-server       # Server module (Controller, Service, Mapper, config files, etc.)
```

## License
[MIT License](LICENSE)
