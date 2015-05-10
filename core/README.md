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
3. docker run -d -P --name mightymerce com.mightymerce.core/mightymerce:0.1-SNAPSHOT 
4. docker tag <IMAGE ID> tutum.co/agutheil/mightymerce:0.1-SNAPSHOT
5. docker push tutum.co/agutheil/mightymerce
