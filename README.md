README for mightymerce
==========================

# schubber. 
* andrea geisler
* andreas gutheil
* markus wagner
* till grupp

# Build and Run
1. boot2docker up
2. ./gradlew distDocker
3. docker run -d --name mongodb -p 27017:27017 dockerfile/mongodb 
4. docker run -d -P --name schubber --link mongodb:mongodb com.schubber.schubber/schubber:0.0.1-SNAPSHOT
5. docker tag schubber/schubber tutum.co/agutheil/schubber
6. docker push tutum.co/agutheil/schubber
