#!/bin/bash

cd app/build/outputs/apk/debug/
curl -F document=@app-debug.apk https://api.telegram.org/bot{TL_TOKEN}/sendDocument?chat_id=@DigiBackApp
