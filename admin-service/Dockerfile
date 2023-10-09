# syntax=docker/dockerfile:1

FROM golang:1.19

# Set destination for COPY
WORKDIR /app

# Download Go modules
COPY . /app
RUN go mod download

# Build the go app
RUN go build -o main .

EXPOSE 8080

# Run
CMD ["./main"]