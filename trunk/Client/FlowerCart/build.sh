#!/bin/bash

today=$(date +%Y%m%d)

ENVS=( "prod" "staging" "qa2" "dev2" )
BRAND=$1
VERSION=$2
DATE=${3:-$today}

BRAND_NAME=$(echo $BRAND | sed -e 's/^\([a-z]\)/\u\1/')
STRIPPED_VERSION=$(echo $VERSION | sed -e 's/\.//g')

# Ensure that the build scripts are configured for the current system
android update project -p .

rm -rf bin

for ENV in ${ENVS[@]}; do
    echo "Building for $ENV"

    VERSION_STRING="$VERSION $ENV $DATE"
    APK="${DATE}_UUX_${BRAND_NAME}_${STRIPPED_VERSION}_${ENV}.apk"

    sed -i "s/\(android:versionName=\"\)[^\"]\+\"/\1$VERSION_STRING\"/" AndroidManifest.xml

    cp assets/www/config.js-$ENV assets/www/config.js

    ant clean
    ant release
    cp bin/MobileUUX-release.apk /tmp/$APK
done
