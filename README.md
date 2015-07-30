README for mightymerce
==========================

# schubber. 
* andrea geisler
* andreas gutheil
* markus wagner
* till grupp

# Build and Run
1. ./gradlew run

# Deploy
1. ./gradlew distDocker
2. docker run --rm -it --privileged -v $HOME/.dockercfg:/.dockercfg:ro -e GIT_REPO=https://github.com/agutheil/core-artifacts.git -e USERNAME=agutheil -e PASSWORD=rouponahybranvi84 -e EMAIL=andreas.gutheil@gmail.com -e DOCKERFILE_PATH=/build/docker tutum/builder tutum.co/agutheil/core:latest

# Umgebungsvariablen
* mightymerce.checkoutLink Verweist auf den CheckoutController.
