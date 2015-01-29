schubber. andrea geisler, andreas gutheil, markus wagner, till grupp

# Build and Run
boot2docker up
./gradlew distDocker
docker run -d -P --name schubber schubber/schubber
docker tag schubber/schubber tutum.co/agutheil/schubber
docker push tutum.co/agutheil/schubber


# Abfragen aller Produkte eines Nutzers
curl -L -i http://localhost:8080/api/v1/agutheil/products

# Abfragen eines Produkts eines Nutzers
curl -L -i http://localhost:8080/api/v1/agutheil/products/1

# Anlegen eines Produkts
curl -L -i -H "Content-Type: application/json" -d '{"title":"Mein xtes Produkt","price":7.99,"currency":"EUR","stock":200}' http://localhost:8080/api/v1/agutheil/products




