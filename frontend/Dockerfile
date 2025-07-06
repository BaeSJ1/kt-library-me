# Stage 1: Build the React application
FROM node:18 AS build

WORKDIR /app

COPY package.json package-lock.json ./
RUN npm install

COPY . .
RUN npm run build

# Stage 2: Serve the built application from a lightweight server
FROM node:18-alpine

WORKDIR /app

COPY --from=build /app/build .

# Install serve to run the application
RUN npm install -g serve

# Expose the port the app runs on
EXPOSE 3000

# Serve the app
CMD ["serve", "-s", ".", "-l", "3000"]
