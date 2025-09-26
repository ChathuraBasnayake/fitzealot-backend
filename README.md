# FitZealot Fitness Service Backend

This project is a Spring Boot service for generating and managing workout plans, with integration to Google's Gemini API.

Configuration
- Configure via environment variables (recommended) to avoid committing secrets:
  - DB_URL (default: jdbc:mysql://localhost:3306/fitzealot-fitness?createDatabaseIfNotExist=true)
  - DB_USERNAME (default: root)
  - DB_PASSWORD (no default)
  - SERVER_PORT (default: 8083)
  - GEMINI_API_KEY (no default) – required to call Gemini
  - GEMINI_API_URL (default provided)

Run
- Build and run with Maven/your IDE. Ensure MySQL is running and the DB credentials are correct.

Endpoints
- POST /workouts/generate-plan – Generate and persist a workout plan
- PUT /workouts/update/{id} – Update an existing plan
- DELETE /workouts/delete/{id} – Delete a plan
- GET /workouts/get/{id} – Fetch a plan
- GET /workouts/get-all – Fetch all plans

Notable Fixes & Faults Addressed
- Passed args to SpringApplication.run in Main (previously omitted).
- Removed hardcoded secrets: database password and Gemini API key were committed; replaced with environment-variable placeholders in application.yml and application.properties.
- Avoided conflicting config by aligning properties between application.yml and application.properties and documenting usage.
- Added validation to WorkoutRequest to enforce input constraints when @Valid is used in controllers.

Security Notes
- Never commit real API keys or database passwords. Use environment variables or a secrets manager per environment.