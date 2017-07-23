[![Build Status](https://travis-ci.org/camy2408/automatic-mower.svg?branch=master)](https://travis-ci.org/camy2408/automatic-mower)
# automatic-mower


            $$$$$$     $$$$$$       $$$$$$$$     $$$$     $$$$$     $$$$         $$$   $$$$$$$$$$$$$
            $$$$$$     $$$$$$     $$$$$$$$$$$$   $$$$     $$$$$     $$$$         $$$   $$$$$$$$$$$$$
            $$$$$$     $$$$$$    $$$$$$$$$$$$$$  $$$$     $$$$$     $$$$         $$$   $$$$$$$$$$$$$
            $$$$$$$   $$$$$$$    $$$$$$$ $$$$$$  $$$$$   $$$$$$$   $$$$$         $$$        $$$     
            $$$$$$$   $$$$$$$   $$$$$      $$$$$  $$$$   $$$$$$$   $$$$          $$$        $$$     
            $$$$$$$   $$$$$$$   $$$$        $$$$  $$$$   $$$$$$$   $$$$          $$$        $$$     
            $$$$$$$$ $$$$$$$$   $$$$        $$$$  $$$$$  $$$ $$$  $$$$$          $$$        $$$     
            $$$$$$$$ $$$$$$$$   $$$$        $$$$   $$$$ $$$$ $$$$ $$$$           $$$        $$$     
            $$$$$$$$ $$$$$$$$   $$$          $$$   $$$$ $$$$ $$$$ $$$$           $$$        $$$     
            $$$$ $$$ $$$ $$$$   $$$$        $$$$   $$$$ $$$$ $$$$ $$$$           $$$        $$$     
            $$$$ $$$$$$$ $$$$   $$$$        $$$$    $$$$$$$   $$$$$$$            $$$        $$$     
            $$$$ $$$$$$$ $$$$   $$$$        $$$$    $$$$$$$   $$$$$$$            $$$        $$$     
            $$$$ $$$$$$$ $$$$   $$$$$      $$$$$    $$$$$$$   $$$$$$$            $$$        $$$     
            $$$$  $$$$$  $$$$    $$$$$$$ $$$$$$     $$$$$$$    $$$$$$            $$$        $$$     
            $$$$  $$$$$  $$$$    $$$$$$$$$$$$$$      $$$$$     $$$$$             $$$        $$$     
            $$$$  $$$$$  $$$$     $$$$$$$$$$$$       $$$$$     $$$$$             $$$        $$$     
            $$$   $$$$$   $$$       $$$$$$$$         $$$$$     $$$$$             $$$        $$$     





Description
===========

This application provides the following features :

* A mechanism to customize errors and messages per locale (default profile english)
* Application Settings via an external xml configuration file otherwise the application will use the default setting loader
* Bean Validator to check constraints  (mandatory fields)
* Sequencer to generate ids automatically for I/O 
* Strong Object Serialization and Deserialization mechanism used to realize CRUD operations (merge,delete,findById,findAll) for I/O
* Encryption system to crypt & decrypt password
* Monitoring and Reporting
* Alerting component
* Caching component

Design-pattern
==============

* Singleton
* Factory
* Abstract Factory
* Builder
* Observer
* Adapter
* Prototype
* Decorator

Build
=====
``` sh
gradle build automatic-mower
```

Requirements
============
* Java 1.8  
* Gradle 3.2.1 or later 



