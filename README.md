schubber. andrea geisler, andreas gutheil, markus wagner.

# Build and Run
boot2docker up
./gradlew distDocker
docker run -d -P --name schubber schubber/schubber
docker tag schubber/schubber tutum.co/agutheil/schubber
docker push tutum.co/agutheil/schubber




