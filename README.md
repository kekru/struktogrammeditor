[![Build Status](https://travis-ci.org/kekru/struktogrammeditor.svg?branch=master)](https://travis-ci.org/kekru/struktogrammeditor)  
# Struktogrammeditor
Visual editor to create nassi-shneiderman diagrams.

This is an old school project of mine. It is one of my first personal java projects and I implemented it in 2010/2011.  
That's why the code is like a beginner's code and it is written in german.  
I decided to publish it, because there are some change requests, mostly from teachers, and I don't find time to implement them.

## Contribution  
Feel free to contribute to this project via PullRequests.  
Please write new code and git commit messages in english. Texts, that are shown in the UI, should still be german.  
Automatic tests would be awesome.

## Generate runnable jar  
Clone or download this repo and open a terminal.  
In Windows Powershell run  
```bash
.\mvnw.cmd clean package
```

In Linux or Mac terminal run  
```bash
chmod +x mvnw
./mvnw clean package
```

When finished a file `target/struktogrammeditor-1.7.2-SNAPSHOT-jar-with-dependencies.jar` is generated.  
When you have Java installed locally, then you can run it by double click   
or run `java -jar struktogrammeditor-1.7.2-SNAPSHOT-jar-with-dependencies.jar` in the terminal.   

## Download via Jitpack
Download of the latest runnable jar of master branch [struktogrammeditor.jar](https://jitpack.io/com/github/kekru/struktogrammeditor/master-SNAPSHOT/struktogrammeditor-master-SNAPSHOT-jar-with-dependencies.jar), [Buildlog](https://jitpack.io/com/github/kekru/struktogrammeditor/master-SNAPSHOT/build.log)  
