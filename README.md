# Electricity Bill Management System (JSP/Servlet + Derby)

This is a runnable JSP/Servlet web app using an embedded Apache Derby database.

Features:
- Customer registration and login
- Customer dashboard with total consumption, last payment, and dues
- View bills with search; pay (complete/partial)
- Admin login (built-in `admin/admin123`) to list customers, see dues, add bills, and delete customers with zero due

## Run in Eclipse
1. File → Import → Existing Maven Projects → select this folder.
2. Add to a Tomcat 10+ server (Servlet 5 / Jakarta) or compatible.
3. Start and open `http://localhost:8080/ebms/`.

Derby files are created under `${catalina.base}/ebms-db`.

## Endpoints
- `/register`, `/login`, `/logout`
- `/dashboard`, `/billlist`, `/paybill`
- `/admin/customers`, `/admin/addbill`, `/admin/customers/delete`

## Notes
- Passwords hashed with SHA-256 (see `com.ebms.security.PasswordUtil`).
- Change admin credentials in `LoginServlet`.